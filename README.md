# Jmix Integrated Applications Sample

This project demonstrates how to integrate Jmix applications using [REST API](https://docs.jmix.io/jmix/rest) and [REST DataStore](https://docs.jmix.io/jmix/rest-ds) add-ons.

The resulting distributed system consists of three web applications and provides functionality for managing products, customers and orders.

The Orders application depends on Customers and Products applications:

![system](doc/system.svg)

Each application uses its own database. The Orders application obtains data from Customers and Products applications through the REST DataStore.

Below are the data models of the applications (JPA entities are in grey, DTO entities are in blue).

Customers application:

![customers](doc/customers.svg)

Products application:

![products](doc/products.svg)

Orders application:

![orders](doc/orders.svg)

The Orders application demonstrates two approaches to the integration:

1. Replicating data from an external application.
    
    The Orders application contains the `SimpleCustomer` JPA entity which represents the customers in the bounded context of orders. The `Order` entity has a reference to `SimpleCustomer` and doesn't directly depend on the external application. The customers data is aggregated and stored in the `SimpleCustomer` entity upon request.

2. Using cross-datastore references.

    The `OrderLine` JPA entity has a reference to the `Product` DTO entity which is directly loaded from the Products application using the REST DataStore.   

The first approach provides loose coupling between the applications: users can create orders for existing customers even when the Customers application is not available. The second approach leads to tight coupling between the applications, but it is simpler as it doesn't require writing any special code for the integration.

In the replication scenario, the sample also demonstrates a possible approach to selecting and returning external data by redirecting users between applications. 

## Setup

All three applications are configured to be run on different ports. To avoid clashing of session cookies between web applications, their host names must also be different. So it is assumed that the applications are available on the following URLs:

- Orders: http://localhost:8080
- Customers: http://host1:8081
- Products: http://host2:8082

Add `host1` and `host2` names pointing to localhost to your `hosts` file: 

```
127.0.0.1       host1
127.0.0.1       host2
```

Now you can run all three applications on localhost from the IDE and access them using the URLs from above (however, the Products application has no UI). 

## Running

Open the root project in IntelliJ with Jmix Studio plugin installed and use `Customers Jmix Application`, `Products Jmix Application` and `Orders Jmix Application` run/debug configurations to run the applications.

Open http://localhost:8080 in your web browser and login as `admin`. 

Open **Orders** from the main menu. Create a new order and try to select a customer. Initially, the dropdown is empty, so click the ellipsis button to open the **Simple customers** lookup view. The table is also empty, so you need to click **Import Customers...** and select the one of the options:

- **Show external entities in this app**: the Orders application will open its views for DTO entities mapped to the data of the Customers application. You can use these views to manage customers as you would in the Customers application, with a single exception: you cannot create new regions.

- **Open external app**: this option opens the Customers application in the new browser tab. You should log in as `admin`. The application will show the detail view for creating a new customer. After entering data into fields and clicking OK, you will be redirected back to the **Simple customers** view of the Orders application and a dialog notifying about successful import will be shown. If you click **Close and select** button in the dialog, the new tab will be closed and the imported customer will be selected in the order.

When creating order lines, you can select, create, update or delete products using the views of the Orders application.

For the demo purposes, all Orders views working with data from external applications have "DTO" in their titles, for example **Products (DTO)**.

## Implementation Details

The Customers and Products applications include the [REST API](https://docs.jmix.io/jmix/rest) add-on. The Customers application also has a UI, while the Products application is a headless service.

The Orders application includes the [REST DataStore](https://docs.jmix.io/jmix/rest-ds) add-on for accessing data of Customers and Orders applications.

There are two additional REST data stores configured in the [application.properties](orders/src/main/resources/application.properties) file of the Orders application: `customers` and `products`. These data stores are specified for the corresponding DTO entities, e.g. [Customer](orders/src/main/java/com/company/orders/entity/customers/Customer.java) and [Product](orders/src/main/java/com/company/orders/entity/products/Product.java).

The views working with DTO entities from a REST data store use specific JSON-based queries. See an example in the `entityComboBox.itemsQuery` component for selecting `Region` in [customer-detail-view.xml](orders/src/main/resources/com/company/orders/view/customers/customer/customer-detail-view.xml).

The Orders application defines fetch plans for external entities in the [fetch-plans.xml](orders/src/main/resources/com/company/orders/fetch-plans.xml) file. These fetch plans correspond by names to fetch plans of JPA entities in the external applications.

The [CustomerImporter](orders/src/main/java/com/company/orders/view/simplecustomer/CustomerImporter.java) bean encapsulates the logic of loading a customer data from the Customers application and creating a `SimpleCustomer` entity in the Orders application.

When using **Open external app** option for importing customers, [SimpleCustomerListView](orders/src/main/java/com/company/orders/view/simplecustomer/SimpleCustomerListView.java) opens a new web page with the Customers application, the route to create a new customer and an additional `redirectTo=orders.simpleCustomers` query parameter. The opened [CustomerDetailView](customers/src/main/java/com/company/customers/view/customer/CustomerDetailView.java) of the Customers application detects this parameter and redirects back to Orders after successful saving of the new customer. The redirecting URL includes the `importCustomer=<ID>` parameter that instructs the opened [SimpleCustomerListView](orders/src/main/java/com/company/orders/view/simplecustomer/SimpleCustomerListView.java) to import a customer with the provided ID. After the import, the user can click the **Close and select** button in the dialog. In this case, the current browser tab (which was used for opening the external application) will close and `SelectCustomerEvent` will be sent to notify all views of the current session about imported customer. If another browser tab has `SimpleCustomerListView` opened in lookup mode (as is the case for selecting a customer for an order), the imported customer will be selected.
