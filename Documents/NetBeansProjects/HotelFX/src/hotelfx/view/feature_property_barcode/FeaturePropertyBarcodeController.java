/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_property_barcode;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FPropertyBarcodeManager;
import hotelfx.persistence.service.FPropertyBarcodeManagerImpl;
import hotelfx.view.feature_job.FeatureJobController;
import hotelfx.view.feature_property_barcode.property.PropertyController;
import hotelfx.view.feature_property_barcode.property_barcode.PropertyBarcodeController;
import hotelfx.view.feature_property_barcode.property_barcode_stock.PropertyBarcodeStockController;
import hotelfx.view.feature_property_barcode.property_stock.PropertyStockController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class FeaturePropertyBarcodeController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "property_barcode":
                    loader.setLocation(HotelFX.class.getResource("view/feature_property_barcode/property_barcode/PropertyBarcodeView.fxml"));

                    PropertyBarcodeController propertyBarcodeController = new PropertyBarcodeController(this);
                    loader.setController(propertyBarcodeController);
                    break;
                case "property":
                    loader.setLocation(HotelFX.class.getResource("view/feature_property_barcode/property/PropertyView.fxml"));

                    System.out.println("dksdjsald");

                    PropertyController propertyController = new PropertyController(this);
                    loader.setController(propertyController);
                    break;

                case "property_barcode_stock":
                    loader.setLocation(HotelFX.class.getResource("view/feature_property_barcode/property_barcode_stock/PropertyBarcodeStockView.fxml"));
                    PropertyBarcodeStockController propertyBarcodeStockController = new PropertyBarcodeStockController(this);
                    loader.setController(propertyBarcodeStockController);
                    break;

                case "property_stock":
                    loader.setLocation(HotelFX.class.getResource("view/feature_property_barcode/property_stock/PropertyStockView.fxml"));
                    PropertyStockController propertyStockController = new PropertyStockController(this);
                    loader.setController(propertyStockController);
                    break;

                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }

            //load loader to node
            Node subContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);

            //set 'sub feature content' into the center of 'feature_property_barcode' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (IOException ex) {
            Logger.getLogger(FeatureJobController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Property Barcode
     */
    @FXML
    private JFXButton btnShowPropertyBarcode;

    @FXML
    private JFXButton btnShowProperty;

    @FXML
    private JFXButton btnShowPropertyBarcodeStock;

    @FXML
    private JFXButton btnShowPropertyStock;

    @FXML
    private AnchorPane btnShowPropertyBarcodeLayout;

    @FXML
    private AnchorPane btnShowPropertyLayout;

    @FXML
    private AnchorPane btnShowPropertyBarcodeStockLayout;

    @FXML
    private AnchorPane btnShowPropertyStockLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowPropertyBarcodeLayout.getStyleClass().add("sub-feature-layout");
        btnShowPropertyLayout.getStyleClass().add("sub-feature-layout");
        btnShowPropertyStockLayout.getStyleClass().add("sub-feature-layout");
        btnShowPropertyBarcodeStockLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false, false);

        btnShowPropertyBarcode.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false);

                selectedSubFeature.set("property_barcode");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false);

                    selectedSubFeature.set("property_barcode");
                }
            }
        });
        btnShowProperty.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false);

                selectedSubFeature.set("property");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false);

                    selectedSubFeature.set("property");
                }
            }
        });

        btnShowPropertyBarcodeStock.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false);

                selectedSubFeature.set("property_barcode_stock");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false);

                    selectedSubFeature.set("property_barcode_stock");
                }
            }
        });

        btnShowPropertyStock.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true);

                selectedSubFeature.set("property_stock");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true);

                    selectedSubFeature.set("property_stock");
                }
            }
        });

        btnShowPropertyBarcodeStock.setText("           Stok \n(Properti Barcode)");
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean propertyBarcode, boolean property, boolean propertyBarcodeStock, boolean propertyStock) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowPropertyBarcodeLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, propertyBarcode);
        btnShowPropertyLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, property);
        btnShowPropertyBarcodeStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, propertyBarcodeStock);
        btnShowPropertyStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, propertyStock);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FPropertyBarcodeManager fPropertyBarcodeManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fPropertyBarcodeManager = new FPropertyBarcodeManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public void setFPropertyBarcodeManager(FPropertyBarcodeManager fPropertyBarcodeManager) {
        this.fPropertyBarcodeManager = fPropertyBarcodeManager;
    }
    
    public FPropertyBarcodeManager getFPropertyBarcodeManager() {
        return this.fPropertyBarcodeManager;
    }

}
