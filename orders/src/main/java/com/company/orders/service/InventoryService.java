package com.company.orders.service;

import com.company.orders.entity.products.Product;
import io.jmix.restds.annotation.RemoteService;

@RemoteService(store = "products")
public interface InventoryService {

    Double getAvailableInStock(Product product);
}
