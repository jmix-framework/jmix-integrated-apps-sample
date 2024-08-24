package com.company.orders.view.customers.contact;

import com.company.orders.entity.customers.Contact;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "contacts/:id", layout = MainView.class)
@ViewController("Contact.detail")
@ViewDescriptor("contact-detail-view.xml")
@EditedEntityContainer("contactDc")
public class ContactDetailView extends StandardDetailView<Contact> {
}
