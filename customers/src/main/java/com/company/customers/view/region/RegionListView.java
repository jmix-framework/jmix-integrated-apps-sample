package com.company.customers.view.region;

import com.company.customers.entity.Region;
import com.company.customers.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;

@Route(value = "regions", layout = MainView.class)
@ViewController("Region.list")
@ViewDescriptor("region-list-view.xml")
@LookupComponent("regionsDataGrid")
@DialogMode(width = "64em")
public class RegionListView extends StandardListView<Region> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<Region> regionsDc;

    @ViewComponent
    private InstanceContainer<Region> regionDc;

    @ViewComponent
    private InstanceLoader<Region> regionDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Subscribe
    public void onInit(final InitEvent event) {
        updateControls(false);
    }

    @Subscribe("regionsDataGrid.create")
    public void onRegionsDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        Region entity = dataContext.create(Region.class);
        regionDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("regionsDataGrid.edit")
    public void onRegionsDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        Region item = regionDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        regionsDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        regionDl.load();
        updateControls(false);
    }

    @Subscribe(id = "regionsDc", target = Target.DATA_CONTAINER)
    public void onRegionsDcItemChange(final InstanceContainer.ItemChangeEvent<Region> event) {
        Region entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            regionDl.setEntityId(entity.getId());
            regionDl.load();
        } else {
            regionDl.setEntityId(null);
            regionDc.setItem(null);
        }
    }

    protected ValidationErrors validateView(Region entity) {
        ViewValidation viewValidation = getViewValidation();
        ValidationErrors validationErrors = viewValidation.validateUiComponents(form);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        validationErrors.addAll(viewValidation.validateBeanGroup(UiCrossFieldChecks.class, entity));
        return validationErrors;
    }

    private void updateControls(boolean editing) {
        form.getChildren().forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
    }

    private ViewValidation getViewValidation() {
        return getApplicationContext().getBean(ViewValidation.class);
    }
}