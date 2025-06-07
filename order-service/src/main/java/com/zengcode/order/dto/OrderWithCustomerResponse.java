package com.zengcode.order.dto;


public class OrderWithCustomerResponse {
    private Order order;
    private Customer customer;

    public OrderWithCustomerResponse() {}

    public OrderWithCustomerResponse(Order order, Customer customer) {
        this.order = order;
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}