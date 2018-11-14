/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_supplier;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FSupplierManager;
import hotelfx.persistence.service.FSupplierManagerImpl;
import hotelfx.view.feature_supplier.supplier.SupplierController;
import hotelfx.view.feature_supplier.supplier_mutation_history.SupplierMutationHistoryController;
import java.net.URL;
import java.util.ResourceBundle;
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
public class FeatureSupplierController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "supplier":
                    loader.setLocation(HotelFX.class.getResource("view/feature_supplier/supplier/SupplierView.fxml"));

                    SupplierController supplierController = new SupplierController(this);
                    loader.setController(supplierController);
                    break;
                case "supplier_mutation_history":
                    loader.setLocation(HotelFX.class.getResource("view/feature_supplier/supplier_mutation_history/SupplierMutationHistoryView.fxml"));

                    SupplierMutationHistoryController supplierMutationHistoryController = new SupplierMutationHistoryController(this);
                    loader.setController(supplierMutationHistoryController);
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

            //set 'sub feature content' into the center of 'feature_room' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Supplier
     */
    @FXML
    private JFXButton btnShowSupplier;

    @FXML
    private AnchorPane btnShowSupplierLayout;

    /**
     * Mutation History
     */
    @FXML
    private JFXButton btnShowSupplierMutationHistory;

    @FXML
    private AnchorPane btnShowSupplierMutationHistoryLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowSupplierLayout.getStyleClass().add("sub-feature-layout");
        btnShowSupplierMutationHistoryLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowSupplier.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("supplier");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("supplier");
                }
            }
        });

        btnShowSupplierMutationHistory.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("supplier_mutation_history");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("supplier_mutation_history");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean supplier, boolean supplierMutationHistory) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowSupplierLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, supplier);
        btnShowSupplierMutationHistoryLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, supplierMutationHistory);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FSupplierManager fSupplierManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fSupplierManager = new FSupplierManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
        
        //auto select firt sub feature
        setButtonDataPseudoClass(true, true, false);
        selectedSubFeature.set("supplier");
    }

    public FSupplierManager getFSupplierManager() {
        return this.fSupplierManager;
    }

}
