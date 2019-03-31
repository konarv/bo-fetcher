package com.blendorders.fetcher.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blendorders.fetcher.beans.Customer;
import com.blendorders.fetcher.beans.Order;
import com.blendorders.fetcher.beans.OrderStatus;

@Service
public class ZomatoFetcher implements OrderFetcher {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${zomato_orders_url}")
	String baseUrl;
	
	String cookies;

	@Override
	public List<Order> getOrders() throws OrderFetchException {

		logger.debug("+getOrders");
		
		Document doc;
		
		try {
			doc = Jsoup.connect(baseUrl).get();
		} catch (IOException e) {
			e.printStackTrace();
			throw new OrderFetchException("Unable to read document");
		}
		
		logger.debug("Page title: {}", doc.title());
		
		List<Order> result = new ArrayList<>();
		
		Elements orderDivs = doc.select(".delivery_order");
		for (Element orderDiv : orderDivs) {
			
			String creatorName = orderDiv.selectFirst(".creatorName").html();
			String phoneText = orderDiv.selectFirst(".phoneText").html();
			String addressInfo = orderDiv.selectFirst(".addressInfo").html();
			
			String[] previousOrdersText = orderDiv.selectFirst(".chip").html().split(" ");
			int previousOrders = Integer.parseInt(previousOrdersText[previousOrdersText.length - 1]);
			
			
			Elements colElements = orderDiv.select(".col.s2.orderBlocks.section.z-index-3.valign");
			
			String orderId = null;
			String restaurant = null;
			String orderCreated = null;
			String orderDelivered = null;
			String paymentStatus = null;
			
			if(colElements.size() > 0){
				Element col2Div = colElements.get(0);
				Elements col2Elements = col2Div.select(".textInfo");
				
				if (col2Elements.size() == 2) {
					orderId = col2Elements.get(0).html();
					restaurant = col2Elements.get(1).html();
				}
			}
			
			if(colElements.size() == 2){
				Element col3Div = colElements.get(1);
				Elements col3Elements = col3Div.select("div");
				
				if (col3Elements.size() == 4) {
					orderCreated = col3Elements.get(1).html() + " " + col3Elements.get(2).html();
					orderDelivered = col3Elements.get(3).html() + " " + col3Elements.get(2).html();
					//03:12 PM December 08, 2018
					
				}
			}
			
			if(orderDiv.select(".unpaid").size() == 1)
				paymentStatus = Order.PaymentStatuses.UNPAID;
			else if(orderDiv.select(".paid").size() == 1)
				paymentStatus = Order.PaymentStatuses.PAID;
			else
				paymentStatus = Order.PaymentStatuses.UNKNOWN;
			
			Order order = new Order();
			order.setId(orderId);
			order.setSystem(OrderFetcher.ZOMATO);
			order.setRestaurant(restaurant);
			order.setPaymentStatus(paymentStatus);

			Customer customer = new Customer();
			customer.setSystem(OrderFetcher.ZOMATO);
			customer.setAddress(addressInfo);
			customer.setName(creatorName);
			customer.setPhoneNumber(phoneText);
			customer.setPreviousOrders(previousOrders);
			order.setCustomer(customer);
			
			String pattern = "hh:mm a MMMMM dd, yyyy";
			SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
			try {
				order.getOrderStatus().add(
						new OrderStatus(Order.OrderStatuses.CREATED, simpleDateFormat.parse(orderCreated)));
				order.getOrderStatus().add(
						new OrderStatus(Order.OrderStatuses.DELIVERED, simpleDateFormat.parse(orderDelivered)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			result.add(order);
		}

		logger.debug("-getOrders");
		return result;
	}

}
