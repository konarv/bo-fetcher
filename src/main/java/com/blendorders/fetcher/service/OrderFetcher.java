package com.blendorders.fetcher.service;

import java.util.List;

import com.blendorders.fetcher.beans.Order;

public interface OrderFetcher {
	
	public static final String SWIGGY = "SWIGGY";
	public static final String ZOMATO = "ZOMATO";
	public static final String FOODPANDA = "FOODPANDA";
	
	List<Order> getOrders();
}
