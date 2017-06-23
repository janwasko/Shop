package com.jasiek.shop.model;

import javafx.beans.property.*;

public class Product {
    private final StringProperty name;
    private final IntegerProperty numberOfPackages;
    private final ObjectProperty<ProductType> type;
    private final BooleanProperty prescriptionOnly;

    public Product() {
        this.name = new SimpleStringProperty("");
        this.numberOfPackages = new SimpleIntegerProperty(0);
        this.type = new SimpleObjectProperty<ProductType>(ProductType.PILL);
        this.prescriptionOnly = new SimpleBooleanProperty(false);
    }

    public Product(String name, int numberOfPackages, ProductType type, boolean prescriptionOnly) {
        this.name = new SimpleStringProperty(name);
        this.numberOfPackages = new SimpleIntegerProperty(numberOfPackages);
        this.type = new SimpleObjectProperty<ProductType>(type);
        this.prescriptionOnly = new SimpleBooleanProperty(prescriptionOnly);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getNumberOfPackages() {
        return numberOfPackages.get();
    }

    public IntegerProperty numberOfPackagesProperty() {
        return numberOfPackages;
    }

    public void setNumberOfPackages(int numberOfPackages) {
        this.numberOfPackages.set(numberOfPackages);
    }

    public ProductType getType() {
        return type.get();
    }

    public ObjectProperty<ProductType> typeProperty() {
        return type;
    }

    public void setType(ProductType type) {
        this.type.set(type);
    }

    public boolean isPrescriptionOnly() {
        return prescriptionOnly.get();
    }

    public BooleanProperty prescriptionOnlyProperty() {
        return prescriptionOnly;
    }

    public void setPrescriptionOnly(boolean prescriptionOnly) {
        this.prescriptionOnly.set(prescriptionOnly);
    }
}
