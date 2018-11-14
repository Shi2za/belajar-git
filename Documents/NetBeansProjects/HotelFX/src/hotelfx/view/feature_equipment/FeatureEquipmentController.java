/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_equipment;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FEquipmentManager;
import hotelfx.persistence.service.FEquipmentManagerImpl;
import hotelfx.view.feature_equipment.equipment.EquipmentController;
import hotelfx.view.feature_equipment.equipment_expired_date.EquipmentExpiredDateController;
import hotelfx.view.feature_equipment.equipment_stock.EquipmentStockController;
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
public class FeatureEquipmentController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "equipment":
                    loader.setLocation(HotelFX.class.getResource("view/feature_equipment/equipment/EquipmentView.fxml"));

                    EquipmentController equipmentController = new EquipmentController(this);
                    loader.setController(equipmentController);
                    break;

                case "stock_equipment":
                    loader.setLocation(HotelFX.class.getResource("view/feature_equipment/equipment_stock/EquipmentStockView.fxml"));
                    EquipmentStockController equipmentStockController = new EquipmentStockController(this);
                    loader.setController(equipmentStockController);
                    break;

                case "equipment_expired_date":
                    loader.setLocation(HotelFX.class.getResource("view/feature_equipment/equipment_expired_date/EquipmentExpiredDateView.fxml"));
                    EquipmentExpiredDateController equipmentExpiredDateController = new EquipmentExpiredDateController(this);
                    loader.setController(equipmentExpiredDateController);
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

            //set 'sub feature content' into the center of 'feature_equipment' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Equipment
     */
    @FXML
    private JFXButton btnShowEquipment;

    @FXML
    private AnchorPane btnShowEquipmentLayout;

    @FXML
    private JFXButton btnShowEquipmentStock;

    @FXML
    private AnchorPane btnShowEquipmentStockLayout;

    @FXML
    private JFXButton btnShowEquipmentExpiredDate;

    @FXML
    private AnchorPane btnShowEquipmentExpiredDateLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowEquipmentLayout.getStyleClass().add("sub-feature-layout");
        btnShowEquipmentStockLayout.getStyleClass().add("sub-feature-layout");
        btnShowEquipmentExpiredDateLayout.getStyleClass().add("sub-feature-layout");

        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, false);
        btnShowEquipmentLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, false);
        btnShowEquipmentStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, false);
        btnShowEquipmentExpiredDateLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, false);

        btnShowEquipment.setOnMouseClicked((e) -> {

            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false);

                selectedSubFeature.set("equipment");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false);

                    selectedSubFeature.set("equipment");
                }
            }
        });

        btnShowEquipmentStock.setOnMouseClicked((e) -> {

            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false);

                selectedSubFeature.set("stock_equipment");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false);

                    selectedSubFeature.set("stock_equipment");
                }
            }
        });

        btnShowEquipmentExpiredDate.setOnMouseClicked((e) -> {

            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true);

                selectedSubFeature.set("equipment_expired_date");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true);

                    selectedSubFeature.set("equipment_expired_date");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean equipment, boolean stockEquipment, boolean equipmentExpiredDate) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowEquipmentLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, equipment);
        btnShowEquipmentStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, stockEquipment);
        btnShowEquipmentExpiredDateLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, equipmentExpiredDate);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FEquipmentManager fEquipmentManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fEquipmentManager = new FEquipmentManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FEquipmentManager getFEquipmentManager() {
        return this.fEquipmentManager;
    }

}
