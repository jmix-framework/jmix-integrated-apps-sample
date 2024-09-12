package com.company.products.service;

import com.company.products.entity.Product;
import io.jmix.rest.annotation.RestMethod;
import io.jmix.rest.annotation.RestService;

@RestService("InventoryService")
public class InventoryService {

    @RestMethod
    public Double getAvailableInStock(Product product) {
        return (double) Math.round(Math.random() * 100);
    }
}
