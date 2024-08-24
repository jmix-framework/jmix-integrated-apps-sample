package com.company.orders.view.simplecustomer;

import com.company.orders.entity.SimpleCustomer;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "simpleCustomers", layout = MainView.class)
@ViewController("SimpleCustomer.list")
@ViewDescriptor("simple-customer-list-view.xml")
@LookupComponent("simpleCustomersDataGrid")
@DialogMode(width = "64em")
public class SimpleCustomerListView extends StandardListView<SimpleCustomer> {
}