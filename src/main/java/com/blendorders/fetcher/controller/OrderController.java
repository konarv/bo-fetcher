package com.blendorders.fetcher.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blendorders.fetcher.beans.Order;
import com.blendorders.fetcher.service.OrderFetchException;
import com.blendorders.fetcher.service.OrderFetcher;
import com.blendorders.fetcher.service.OrderFetcherFactory;

@RestController
public class OrderController {

	@Autowired
	OrderFetcherFactory orderFetcherFactory;

	@RequestMapping("orders")
	public List<Order> getSingleOrders(@RequestParam Optional<String> vendor) {
		
		List<Order> result = new ArrayList<>();
		
		if(vendor.isPresent()) {
			List<Order> orderList = orderFetcherFactory.getFetcher(vendor.get()).getOrders();
			result.addAll(orderList);
		} else {
			List<Order> zomatoList = orderFetcherFactory.getFetcher(OrderFetcher.ZOMATO).getOrders();
			List<Order> swiggyList = orderFetcherFactory.getFetcher(OrderFetcher.SWIGGY).getOrders();

			result.addAll(zomatoList);
			result.addAll(swiggyList);
		}

		return result;
	}
	
}
