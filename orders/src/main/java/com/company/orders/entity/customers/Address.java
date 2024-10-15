package com.company.orders.entity.customers;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class Address {

    private String city;

    private String postCode;

    private String addressLine;

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}