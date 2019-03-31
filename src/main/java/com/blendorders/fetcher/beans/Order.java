package com.blendorders.fetcher.beans;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	public interface PaymentStatuses {
		public static final String PAID = "PAID";
		public static final String UNPAID = "UNPAID";
		public static final String UNKNOWN = "UNKNOWN";
	}
	
	public interface OrderStatuses {
		public static final String CREATED = "CREATED";
		public static final String PICKUP = "PICKUP";
		public static final String DELIVERED = "DELIVERED";
	}
	
	String id;
	String system;
	long amount;
	String restaurant;
	String paymentStatus;

	DeliveryMan deliveryMan;
	Customer customer;
	List<OrderStatus> orderStatus;

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public DeliveryMan getDeliveryMan() {
		return deliveryMan;
	}
	public void setDeliveryMan(DeliveryMan deliveryMan) {
		this.deliveryMan = deliveryMan;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public List<OrderStatus> getOrderStatus() {
		if (orderStatus == null) {
			orderStatus = new ArrayList<>();
		}
		return orderStatus;
	}
	public void setOrderStatus(List<OrderStatus> orderStatus) {
		this.orderStatus = orderStatus;
	}
}
