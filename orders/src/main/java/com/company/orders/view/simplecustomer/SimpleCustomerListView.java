package com.company.orders.view.simplecustomer;

import com.company.orders.entity.SimpleCustomer;
import com.company.orders.entity.customers.Customer;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import io.jmix.core.Id;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.kit.component.dropdownbutton.DropdownButtonItem;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;

import java.util.Collection;
import java.util.UUID;


@Route(value = "simpleCustomers", layout = MainView.class)
@ViewController("SimpleCustomer.list")
@ViewDescriptor("simple-customer-list-view.xml")
@LookupComponent("simpleCustomersDataGrid")
@DialogMode(width = "64em")
public class SimpleCustomerListView extends StandardListView<SimpleCustomer> {

    @Autowired
    private CustomerImporter customerImporter;
    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private UiEventPublisher uiEventPublisher;

    @ViewComponent
    private CollectionLoader<SimpleCustomer> simpleCustomersDl;
    @ViewComponent
    private DataGrid<SimpleCustomer> simpleCustomersDataGrid;

    @Value("${customers.baseUrl}")
    private String customersBaseUrl;

    @Subscribe("importButton.showExternalEntitiesItem")
    public void onImportButtonShowExternalEntitiesItemClick(final DropdownButtonItem.ClickEvent event) {
        // Show Customer DTO list view for looking up a customer and importing it
        dialogWindows.lookup(this, Customer.class)
                .withSelectHandler(this::importCustomers)
                .open();
    }

    @Subscribe("importButton.openExternalAppItem")
    public void onImportButtonOpenExternalAppItemClick(final DropdownButtonItem.ClickEvent event) {
        // Open the Customers application with the customers list in a new tab and
        // provide a URL parameter to redirect back to this view
        String url = customersBaseUrl + "/customers/new?redirectTo=orders.simpleCustomers";
        UI.getCurrent().getPage().executeJs("window.open('" + url + "', '_blank');");
    }

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {
        // When redirecting from the Customers app back, the URL contains the "importCustomer"
        // parameter with the ID of the customer to import
        event.getQueryParameters().getSingleParameter("importCustomer").ifPresent(param -> {
            Id<Customer> customerId = Id.of(UUID.fromString(param), Customer.class);
            SimpleCustomer importedCustomer = importCustomer(customerId);

            String thisUrl = RouteConfiguration.forSessionScope().getUrl(this.getClass());
            UI.getCurrent().getPage().getHistory().replaceState(null, thisUrl);

            // Show a success notification and close the browser window
            dialogs.createOptionDialog()
                    .withText("Successfully imported '" + importedCustomer.getName() + "'")
                    .withActions(
                            new DialogAction(DialogAction.Type.OK)
                                    .withText("Close and select")
                                    .withHandler(actionPerformedEvent ->
                                            closeBrowserWindowAndNotify(importedCustomer)),
                            new BaseAction("continue")
                                    .withText("Continue")
                    )
                    .open();
        });
    }

    private void importCustomers(Collection<Customer> customers) {
        for (Customer customer : customers) {
            importCustomer(Id.of(customer));
        }
        simpleCustomersDl.load();
        notifications.create("Imported successfully").show();
    }

    private SimpleCustomer importCustomer(Id<Customer> customerId) {
        // Delegate to the CustomerImporter bean to import the customer
        return customerImporter.importCustomer(customerId);
    }

    private void closeBrowserWindowAndNotify(SimpleCustomer importedCustomer) {
        // Close the browser window and notify the UI about the imported customer
        UI.getCurrent().getPage().executeJs("window.close();");
        uiEventPublisher.publishEvent(new SelectCustomerEvent(this, importedCustomer));
    }

    @EventListener
    private void onSelectImportedCustomer(final SelectCustomerEvent event) {
        // If a customer was imported by another instance of this view,
        // select it and return to the calling view
        getSelectAction().ifPresent(action -> {
            simpleCustomersDataGrid.select(event.getCustomer());
            action.actionPerform(this);
        });
    }
}