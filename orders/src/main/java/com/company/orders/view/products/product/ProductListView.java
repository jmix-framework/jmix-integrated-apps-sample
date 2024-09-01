package com.company.orders.view.products.product;

import com.company.orders.entity.products.Product;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "products", layout = MainView.class)
@ViewController("Product.list")
@ViewDescriptor("product-list-view.xml")
@LookupComponent("productsDataGrid")
@DialogMode(width = "50em")
public class ProductListView extends StandardListView<Product> {
}
