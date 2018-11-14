/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_amenity;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FAmenityManager;
import hotelfx.persistence.service.FAmenityManagerImpl;
import hotelfx.view.feature_amenity.amenity.AmenityController;
import hotelfx.view.feature_amenity.amenity_expired_date.AmenityExpiredDateController;
import hotelfx.view.feature_amenity.amenity_stock.AmenityStockController;
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
public class FeatureAmenityController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "amenity":
                    loader.setLocation(HotelFX.class.getResource("view/feature_amenity/amenity/AmenityView.fxml"));

                    AmenityController amenityController = new AmenityController(this);
                    loader.setController(amenityController);
                    break;

                case "amenity_stock":
                    loader.setLocation(HotelFX.class.getResource("view/feature_amenity/amenity_stock/AmenityStockView.fxml"));
                    AmenityStockController amenityStockController = new AmenityStockController(this);
                    loader.setController(amenityStockController);
                    break;

                case "amenity_expired_date":
                    loader.setLocation(HotelFX.class.getResource("view/feature_amenity/amenity_expired_date/AmenityExpiredDateView.fxml"));

                    AmenityExpiredDateController amenityExpiredDateController = new AmenityExpiredDateController(this);
                    loader.setController(amenityExpiredDateController);
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

            //set 'sub feature content' into the center of 'feature_amenity' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Amenity
     */
    @FXML
    private JFXButton btnShowAmenity;

    @FXML
    private JFXButton btnShowAmenityStock;

    @FXML
    private AnchorPane btnShowAmenityLayout;

    @FXML
    private AnchorPane btnShowAmenityStockLayout;

    /**
     * Amenity
     */
    @FXML
    private JFXButton btnShowAmenityExpiredDate;

    @FXML
    private AnchorPane btnShowAmenityExpiredDateLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowAmenityLayout.getStyleClass().add("sub-feature-layout");
        btnShowAmenityStockLayout.getStyleClass().add("sub-feature-layout");
        btnShowAmenityExpiredDateLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false);

        btnShowAmenity.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false);

                selectedSubFeature.set("amenity");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false);

                    selectedSubFeature.set("amenity");
                }
            }
        });

        btnShowAmenityStock.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false);

                selectedSubFeature.set("amenity_stock");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false);

                    selectedSubFeature.set("amenity_stock");
                }
            }
        });

        btnShowAmenityExpiredDate.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true);

                selectedSubFeature.set("amenity_expired_date");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true);

                    selectedSubFeature.set("amenity_expired_date");
                }
            }

        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean amenity, boolean amenityStock, boolean amenityExpiredDate) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowAmenityLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, amenity);
        btnShowAmenityStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, amenityStock);
        btnShowAmenityExpiredDateLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, amenityExpiredDate);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FAmenityManager fAmenityManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fAmenityManager = new FAmenityManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FAmenityManager getFAmenityManager() {
        return this.fAmenityManager;
    }

}
