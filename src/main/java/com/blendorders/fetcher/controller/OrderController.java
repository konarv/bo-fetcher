package com.blendorders.fetcher.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blendorders.fetcher.beans.Order;
import com.blendorders.fetcher.service.OrderFetcher;
import com.blendorders.fetcher.service.OrderFetcherFactory;

@RestController
public class OrderController {

	@Autowired
	OrderFetcherFactory orderFetcherFactory;

	@Autowired
	ExecutorService executor;
	
	@RequestMapping("orders")
	public List<Order> getSingleOrders(@RequestParam Optional<String> vendor) {
		
		List<Order> result = new ArrayList<>();
		
		if(vendor.isPresent()) {
			List<Order> orderList = orderFetcherFactory.getFetcher(vendor.get()).getOrders();
			result.addAll(orderList);
		} else {
			
			Future<List<Order>> zomatoFuture = executor.submit(() -> {
				return orderFetcherFactory.getFetcher(OrderFetcher.ZOMATO).getOrders();
			});
			
			Future<List<Order>> swiggyFuture = executor.submit(() -> {
				return orderFetcherFactory.getFetcher(OrderFetcher.SWIGGY).getOrders();
			});
			
			try {
				result.addAll(swiggyFuture.get());
				result.addAll(zomatoFuture.get());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException("Error getting orders");
			}
			
		}

		return result;
	}
	
	
	
}
