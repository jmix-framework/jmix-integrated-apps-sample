package com.company.orders.view.simplecustomer;

import com.company.orders.entity.SimpleCustomer;
import org.springframework.context.ApplicationEvent;

public class SelectCustomerEvent extends ApplicationEvent {

    private final SimpleCustomer customer;

    public SelectCustomerEvent(Object source, SimpleCustomer customer) {
        super(source);
        this.customer = customer;
    }

    public SimpleCustomer getCustomer() {
        return customer;
    }
}
