package com.company.orders.view.products.product;

import com.company.orders.entity.products.Product;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "products/:id", layout = MainView.class)
@ViewController("Product.detail")
@ViewDescriptor("product-detail-view.xml")
@EditedEntityContainer("productDc")
public class ProductDetailView extends StandardDetailView<Product> {
}
