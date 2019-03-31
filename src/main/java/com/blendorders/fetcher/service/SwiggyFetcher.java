package com.blendorders.fetcher.service;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blendorders.fetcher.beans.Customer;
import com.blendorders.fetcher.beans.DeliveryMan;
import com.blendorders.fetcher.beans.Order;
import com.blendorders.fetcher.beans.OrderStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SwiggyFetcher implements OrderFetcher {
	
	@Autowired
    private ObjectMapper objectMapper;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${swiggy_orders_url}")
	String baseUrl;
	
	String cookies;
	
	@Override
	public List<Order> getOrders() {
		logger.debug("+getOrders");
		List<Order> result = new ArrayList<>();
		
		JsonNode node = null;
		try {
			node = objectMapper.readTree(new URL(baseUrl));
		} catch (IOException e) {
			e.printStackTrace();
			throw new OrderFetchException("Unable to fetch Swiggy orders");
		}
		
		JsonNode ordersNode = node.findValue("orders");
		
		if(ordersNode == null || !ordersNode.isArray())
			throw new OrderFetchException("Orders not found");
		
		for (JsonNode orderNode : ordersNode) {
			Order order = new Order();
			
			order.setId(orderNode.findValue("order_id").asText());
			order.setSystem(OrderFetcher.SWIGGY);
			order.setAmount(orderNode.findValue("bill").asLong());
			
			Customer customer = new Customer();
			customer.setSystem(OrderFetcher.SWIGGY);
			customer.setId(orderNode.findValue("customer_id").asText());
			customer.setLatitude(orderNode.findValue("customer_lat").asText());
			customer.setLongitude(orderNode.findValue("customer_lng").asText());
			customer.setDistance(orderNode.findValue("customer_distance").asInt());
			order.setCustomer(customer);
			
			DeliveryMan deliveryMan = new DeliveryMan();
			JsonNode deliveryManNode = orderNode.findValue("delivery_boy");
			deliveryMan.setName(deliveryManNode.findValue("name").asText());
			deliveryMan.setPhone(deliveryManNode.findValue("mobile").asText());
			deliveryMan.setImage(deliveryManNode.findValue("imageUrl").asText());
			
			//2018-12-08T16:36:56
			String pattern = "yyyy-MM-dd'T'HH:mm:ss";
			SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);

			try {
				JsonNode createdTimeNode = orderNode.findValue("assigned_time");
				if(createdTimeNode != null)
					order.getOrderStatus().add(
							new OrderStatus(Order.OrderStatuses.CREATED, 
									simpleDateFormat.parse(createdTimeNode.asText())));
				
				JsonNode pickupTimeNode = orderNode.findValue("pickedup_time");
				if(pickupTimeNode != null)
					order.getOrderStatus().add(
							new OrderStatus(Order.OrderStatuses.PICKUP,
									simpleDateFormat.parse(pickupTimeNode.asText())));
				
				JsonNode deliveredTimeNode = orderNode.findValue("delivered_time");
				if(deliveredTimeNode != null)
					order.getOrderStatus().add(
							new OrderStatus(Order.OrderStatuses.DELIVERED, 
									simpleDateFormat.parse(deliveredTimeNode.asText())));
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			result.add(order);
		}
		
		logger.debug("+getOrders");
		return result;
	}

}
