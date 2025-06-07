package com.zengcode.order.dto;

public class Order {
    private String id;
    private String customerId;
    private String item;
    private double price;

    public Order() {}

    public Order(String id, String customerId, String item, double price) {
        this.id = id;
        this.customerId = customerId;
        this.item = item;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
