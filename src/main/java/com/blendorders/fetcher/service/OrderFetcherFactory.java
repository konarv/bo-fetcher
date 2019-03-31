package com.blendorders.fetcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderFetcherFactory {
	
	@Autowired
	ZomatoFetcher zomato;
	@Autowired
	SwiggyFetcher swiggy;

	public OrderFetcher getFetcher(String vendor){
		switch (vendor) {
		case OrderFetcher.ZOMATO:
			return zomato;
		case OrderFetcher.SWIGGY:
			return swiggy;
		default:
			throw new RuntimeException("Unknown vendor");
		}
	}
	
}
