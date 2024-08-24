package com.company.orders.view.simplecustomer;

import com.company.orders.entity.SimpleCustomer;
import com.company.orders.entity.customers.Address;
import com.company.orders.entity.customers.Contact;
import com.company.orders.entity.customers.Customer;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;


@Route(value = "simpleCustomers", layout = MainView.class)
@ViewController("SimpleCustomer.list")
@ViewDescriptor("simple-customer-list-view.xml")
@LookupComponent("simpleCustomersDataGrid")
@DialogMode(width = "64em")
public class SimpleCustomerListView extends StandardListView<SimpleCustomer> {

    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private DataManager dataManager;

    @ViewComponent
    private CollectionLoader<SimpleCustomer> simpleCustomersDl;
    @Autowired
    private Notifications notifications;

    @Subscribe(id = "importButton", subject = "clickListener")
    public void onImportButtonClick(final ClickEvent<JmixButton> event) {
        dialogWindows.lookup(this, Customer.class)
                .withSelectHandler(this::importCustomers)
                .open();
    }

    private void importCustomers(Collection<Customer> customers) {
        for (Customer customer : customers) {
            Customer fullCustomer = dataManager.load(Customer.class)
                    .id(customer.getId())
                    .fetchPlan("customer-full")
                    .one();

            SimpleCustomer simpleCustomer = dataManager.load(SimpleCustomer.class)
                    .query("e.externalId = ?1", customer.getId())
                    .optional()
                    .orElseGet(() -> {
                        SimpleCustomer sc = dataManager.create(SimpleCustomer.class);
                        sc.setExternalId(fullCustomer.getId());
                        return sc;
                    });
            simpleCustomer.setName(fullCustomer.getName());
            simpleCustomer.setEmail(fullCustomer.getEmail());
            simpleCustomer.setRegionName(fullCustomer.getRegion().getName());
            simpleCustomer.setAddressText(formatAddressText(fullCustomer.getAddress()));
            simpleCustomer.setPreferredContact(formatPreferredContact(fullCustomer.getContacts()));

            dataManager.save(simpleCustomer);
        }
        simpleCustomersDl.load();
        notifications.create("Imported successfully").show();
    }

    private String formatAddressText(Address address) {
        return address.getCity() + ", " + address.getPostCode() + ", " + address.getAddressLine();
    }

    private String formatPreferredContact(Set<Contact> contacts) {
        return contacts.stream()
                .filter(contact -> Boolean.TRUE.equals(contact.getPreferred()))
                .findFirst()
                .map(contact -> contact.getContactType() + ": " + contact.getContactValue())
                .orElse("");
    }
}