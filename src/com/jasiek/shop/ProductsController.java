package com.jasiek.shop;

import com.jasiek.shop.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;

public class ProductsController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private Label nameLabel;

    @FXML
    private Label numberOfPackagesLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label prescriptionOnlyLabel;

    private Main main;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        showProductDetails(null);
        productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showProductDetails(newValue));
    }

    @FXML
    private void handleDeleteProduct() {
        int selectedIndex = productTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            productTable.getItems().remove(selectedIndex);
        } else {
            noProductSelected();
        }
    }

    @FXML
    private void handleNewProduct() {
        Product tempProduct = new Product();
        boolean okClicked = main.showPersonEditDialog(tempProduct);
        if (okClicked) {
            main.getProductData().add(tempProduct);
        }
    }

    @FXML
    private void handleEditPerson() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            boolean okClicked = main.showPersonEditDialog(selectedProduct);
            if (okClicked) {
                showProductDetails(selectedProduct);
            }
        } else {
            noProductSelected();
        }
    }

    private void noProductSelected() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(main.getPrimaryStage());
        alert.setTitle("No selection");
        alert.setHeaderText("No product selected");
        alert.setContentText("Please select a product in the table.");
        alert.showAndWait();
    }

    private void showProductDetails(Product product) {
        if (product != null) {
            nameLabel.setText(product.getName());
            numberOfPackagesLabel.setText(String.valueOf(product.getNumberOfPackages()));
            typeLabel.setText(String.valueOf(product.getType()));
            prescriptionOnlyLabel.setText(String.valueOf(product.isPrescriptionOnly()));
        } else {
            nameLabel.setText("");
            numberOfPackagesLabel.setText("");
            typeLabel.setText("");
            prescriptionOnlyLabel.setText("");
        }
    }

    public void setMain(Main main) {
        this.main = main;
        productTable.setItems(main.getProductData());
    }

    public void setMainApp(Main main) {
        this.main = main;
    }

    @FXML
    private void handleNew() {
        main.getProductData().clear();
        main.setProductFilePath(null);
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show open file dialog
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());
        if (file != null) {
            main.loadProductDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File productFile = main.getProductFilePath();
        if (productFile != null) {
            main.saveProductDataToFile(productFile);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.saveProductDataToFile(file);
        }
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About");
        alert.setContentText("Author: Jan Wasko\n");
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
