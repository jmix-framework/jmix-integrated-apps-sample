package com.company.orders.view.orderline;

import com.company.orders.entity.OrderLine;
import com.company.orders.entity.products.Product;
import com.company.orders.service.InventoryService;
import com.company.orders.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "orderLines/:id", layout = MainView.class)
@ViewController("OrderLine.detail")
@ViewDescriptor("order-line-detail-view.xml")
@EditedEntityContainer("orderLineDc")
public class OrderLineDetailView extends StandardDetailView<OrderLine> {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private Notifications notifications;

    @Subscribe(id = "availableBtn", subject = "clickListener")
    public void onAvailableBtnClick(final ClickEvent<JmixButton> event) {
        Product product = getEditedEntity().getProduct();
        if (product != null) {
            Double inStock = inventoryService.getAvailableInStock(product);
            notifications.show("Available in stock: " + inStock);
        }
    }
}