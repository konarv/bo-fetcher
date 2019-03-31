package com.blendorders.fetcher.beans;

import java.util.Date;

public class OrderStatus {

	String state;
	Date date;
	
	public OrderStatus() {
	}
	
	public OrderStatus(String state, Date date) {
		this.state = state;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
