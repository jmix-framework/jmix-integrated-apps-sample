package com.company.orders.service;

import com.company.orders.entity.products.Product;
import io.jmix.core.EntitySerialization;
import io.jmix.restds.util.RestDataStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// tag::service[]
@Component
public class InventoryService {

    @Autowired
    private RestDataStoreUtils restDataStoreUtils;
    @Autowired
    private EntitySerialization entitySerialization;

    public Double getAvailableInStock(Product product) {
        RestClient restClient = restDataStoreUtils.getRestClient("products");

        String productJson = entitySerialization.toJson(product);

        String result = restClient.post()
                .uri("/rest/services/InventoryService/getAvailableInStock")
                .body("""
                        {
                            "product": %s
                        }
                        """.formatted(productJson))
                .retrieve()
                .body(String.class);

        return Double.valueOf(result);
    }
}
// end::service[]
