package com.company.orders.view.products.productcategory;

import com.company.orders.entity.products.ProductCategory;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "productCategories", layout = MainView.class)
@ViewController("ProductCategory.list")
@ViewDescriptor("product-category-list-view.xml")
@LookupComponent("productCategoriesDataGrid")
@DialogMode(width = "50em")
public class ProductCategoryListView extends StandardListView<ProductCategory> {
}
