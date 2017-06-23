package com.jasiek.shop;

import com.jasiek.shop.model.Product;
import com.jasiek.shop.model.ProductType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductEditDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberOfPackagesField;

    @FXML
    private ComboBox typeField;

    @FXML
    private CheckBox prescriptionOnlyField;

    private Stage dialogStage;
    private Product product;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        typeField.getItems().setAll(ProductType.values());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setProduct(Product product) {
        this.product = product;
        nameField.setText(product.getName());
        numberOfPackagesField.setText(String.valueOf(product.getNumberOfPackages()));
        typeField.setValue(product.getType());
        prescriptionOnlyField.setSelected(product.isPrescriptionOnly());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            product.setName(nameField.getText());
            product.setNumberOfPackages(Integer.parseInt(numberOfPackagesField.getText()));
            product.setType((ProductType) typeField.getValue());
            product.setPrescriptionOnly(prescriptionOnlyField.isSelected());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (numberOfPackagesField.getText() == null || numberOfPackagesField.getText().length() == 0) {
            errorMessage += "No valid number of packages!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
