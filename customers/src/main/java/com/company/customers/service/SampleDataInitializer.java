package com.company.customers.service;

import com.company.customers.entity.Contact;
import com.company.customers.entity.ContactType;
import com.company.customers.entity.Customer;
import com.company.customers.entity.Region;
import io.jmix.core.DataManager;
import io.jmix.core.security.Authenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleDataInitializer {

    private static final Logger log = LoggerFactory.getLogger(SampleDataInitializer.class);
    private final DataManager dataManager;

    public SampleDataInitializer(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventListener(ApplicationStartedEvent.class)
    @Authenticated
    public void init() {
        List<Customer> customers = dataManager.load(Customer.class).all().maxResults(1).list();
        if (!customers.isEmpty()) {
            log.info("Customers already exist, skipping initialization");
        } else {
            List<Region> regions = createRegions();
            createCustomers(regions);
        }
    }

    private List<Region> createRegions() {
        log.info("Creating regions");
        Region region = dataManager.create(Region.class);
        region.setName("North America");
        Region saved = dataManager.save(region);
        log.info("Regions created");
        return List.of(saved);
    }

    public void createCustomers(List<Region> regions) {
        log.info("Creating customers");
        Customer customer = dataManager.create(Customer.class);
        customer.setName("Robert Taylor");
        customer.setEmail("robert@example.com");
        customer.setRegion(regions.getFirst());

        // create and save two customer contacts
        Contact contact1 = dataManager.create(Contact.class);
        contact1.setCustomer(customer);
        contact1.setContactType(ContactType.PHONE);
        contact1.setContactValue("555-555-5555");
        contact1.setPreferred(true);

        Contact contact2 = dataManager.create(Contact.class);
        contact2.setCustomer(customer);
        contact2.setContactType(ContactType.EMAIL);
        contact2.setContactValue("robert@example.com");

        dataManager.save(customer, contact1, contact2);

        log.info("Customers created");
    }
}