package com.company.orders.view.customers.customer;

import com.company.orders.entity.customers.Customer;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.flowui.view.*;

import java.util.Collection;
import java.util.List;

@Route(value = "customers", layout = MainView.class)
@ViewController("Customer.list")
@ViewDescriptor("customer-list-view.xml")
@LookupComponent("customersDataGrid")
@DialogMode(width = "50em")
public class CustomerListView extends StandardListView<Customer> {
}
