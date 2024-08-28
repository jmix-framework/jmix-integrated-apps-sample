package com.company.customers.view.customer;

import com.company.customers.entity.Customer;
import com.company.customers.view.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Route(value = "customers/:id", layout = MainView.class)
@ViewController("Customer.detail")
@ViewDescriptor("customer-detail-view.xml")
@EditedEntityContainer("customerDc")
public class CustomerDetailView extends StandardDetailView<Customer> {

    private String redirectTo;

    @Autowired
    private Environment environment;

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {
        // When the URL contains the "redirectTo" parameter, save it to redirect after save
        event.getQueryParameters().getSingleParameter("redirectTo").ifPresent(param -> {
            redirectTo = param;
        });
    }

    @Subscribe
    public void onAfterSave(final AfterSaveEvent event) {
        if (redirectTo != null) {
            // Get full URL and redirect to it
            String baseUrl = environment.getProperty(redirectTo + ".url");
            UI.getCurrent().getPage().setLocation(baseUrl + "?importCustomer=" + getEditedEntity().getId());
        }
    }
}