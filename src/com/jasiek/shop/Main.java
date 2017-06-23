package com.jasiek.shop;

import com.jasiek.shop.model.Product;
import com.jasiek.shop.model.ProductListWrapper;
import com.jasiek.shop.model.ProductType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private ObservableList<Product> productData = FXCollections.observableArrayList();

    public Main() {
        productData.add(new Product("Xanax", 5, ProductType.PILL, true));
        productData.add(new Product("Bepanthen", 10, ProductType.OINTMENT, false));
        productData.add(new Product("Claritine", 15, ProductType.SYRUP, false));
    }

    public ObservableList<Product> getProductData() {
        return productData;
    }

    public boolean showPersonEditDialog(Product product) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ProductEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle("Edit Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Set the person into the controller.
            ProductEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduct(product);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Products.fxml"));
        Parent root = loader.load();
        ProductsController productsController = loader.getController();
        productsController.setMain(this);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Chemist's");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public File getProductFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setProductFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");
            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }

    public void loadProductDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ProductListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            // Reading XML from the file and unmarshalling.
            ProductListWrapper wrapper = (ProductListWrapper) um.unmarshal(file);
            productData.clear();
            productData.addAll(wrapper.getProducts());
            // Save the file path to the registry.
            setProductFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public void saveProductDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ProductListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Wrapping our person data.
            ProductListWrapper wrapper = new ProductListWrapper();
            wrapper.setProducts(productData);
            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);
            // Save the file path to the registry.
            setProductFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public void initApp() {
        try {
            // Load from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class
                    .getResource("view/Products.fxml"));
            rootLayout = (AnchorPane) loader.load();
            // Show the scene.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            // Give the controller access to the main app.
            ProductsController controller = loader.getController();
            controller.setMain(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Try to load last opened person file.
        File file = getProductFilePath();
        if (file != null) {
            loadProductDataFromFile(file);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
