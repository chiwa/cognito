package com.zengcode.order.controller;

import com.zengcode.order.dto.Customer;
import com.zengcode.order.dto.Order;
import com.zengcode.order.dto.OrderWithCustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderWithCustomerResponse> getOrderWithCustomer(@PathVariable String id) {
        // Mock Order
        Order order = new Order(id, "c1", "MacBook Pro", 89900.0);

        // Call Customer Service
        String url = "http://customer-service:8080/customers/" + order.getCustomerId();
        Customer customer = restTemplate.getForObject(url, Customer.class);

        // รวมข้อมูล
        OrderWithCustomerResponse response = new OrderWithCustomerResponse(order, customer);
        return ResponseEntity.ok(response);
    }
}