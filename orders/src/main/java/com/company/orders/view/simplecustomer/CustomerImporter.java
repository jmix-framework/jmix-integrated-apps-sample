package com.company.orders.view.simplecustomer;

import com.company.orders.entity.SimpleCustomer;
import com.company.orders.entity.customers.Address;
import com.company.orders.entity.customers.Contact;
import com.company.orders.entity.customers.Customer;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.Id;
import org.springframework.stereotype.Component;

import java.util.Set;

// tag::import[]
@Component
public class CustomerImporter {

    private final DataManager dataManager;

    public CustomerImporter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public SimpleCustomer importCustomer(Id<Customer> customerId) {
        Customer fullCustomer = dataManager.load(customerId)
//                .fetchPlan("customer-full")
                .fetchPlan(fpb -> fpb
                        .addFetchPlan(FetchPlan.BASE)
                        .add("region", FetchPlan.BASE)
                        .add("contacts", FetchPlan.BASE))
                .one();

        SimpleCustomer simpleCustomer = dataManager.load(SimpleCustomer.class)
                .query("e.externalId = ?1", customerId.getValue())
                .optional()
                .orElseGet(() -> {
                    SimpleCustomer sc = dataManager.create(SimpleCustomer.class);
                    sc.setExternalId(fullCustomer.getId());
                    return sc;
                });
        simpleCustomer.setName(fullCustomer.getName());
        simpleCustomer.setEmail(fullCustomer.getEmail());
        simpleCustomer.setRegionName(fullCustomer.getRegion() == null ? null : fullCustomer.getRegion().getName());
        simpleCustomer.setAddressText(formatAddressText(fullCustomer.getAddress()));
        simpleCustomer.setPreferredContact(formatPreferredContact(fullCustomer.getContacts()));

        SimpleCustomer importedCustomer = dataManager.save(simpleCustomer);
        return importedCustomer;
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
// end::import[]