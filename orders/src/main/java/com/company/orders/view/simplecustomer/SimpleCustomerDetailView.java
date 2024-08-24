package com.company.orders.view.simplecustomer;

import com.company.orders.entity.SimpleCustomer;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "simpleCustomers/:id", layout = MainView.class)
@ViewController("SimpleCustomer.detail")
@ViewDescriptor("simple-customer-detail-view.xml")
@EditedEntityContainer("simpleCustomerDc")
public class SimpleCustomerDetailView extends StandardDetailView<SimpleCustomer> {
}