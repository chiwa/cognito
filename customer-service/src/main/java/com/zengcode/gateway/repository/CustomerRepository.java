package com.zengcode.gateway.repository;

import com.zengcode.gateway.dto.Customer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {

    private static final Map<String, Customer> DATA = new ConcurrentHashMap<>();

    static {
        DATA.put("c1", new Customer("c1", "พีพี", "0812345678"));
        DATA.put("c2", new Customer("c2", "แนน", "0899999999"));
    }

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(DATA.get(id));
    }
}
