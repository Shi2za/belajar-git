/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_location;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FLaundryManager;
import hotelfx.persistence.service.FLaundryManagerImpl;
import hotelfx.persistence.service.FLocationManager;
import hotelfx.persistence.service.FLocationManagerImpl;
import hotelfx.persistence.service.FWarehouseManager;
import hotelfx.persistence.service.FWarehouseManagerImpl;
import hotelfx.view.feature_laundry.laundry.LaundryController;
import hotelfx.view.feature_location.building.BuildingController;
import hotelfx.view.feature_location.floor.FloorController;
import hotelfx.view.feature_location.location.LocationController;
import hotelfx.view.feature_warehouse.warehouse.WarehouseController;
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
public class FeatureLocationController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "location":
                    loader.setLocation(HotelFX.class.getResource("view/feature_location/location/LocationView.fxml"));

                    LocationController locationController = new LocationController(this);
                    loader.setController(locationController);
                    break;
                case "building":
                    loader.setLocation(HotelFX.class.getResource("view/feature_location/building/BuildingView.fxml"));

                    BuildingController buildingController = new BuildingController(this);
                    loader.setController(buildingController);
                    break;
                case "floor":
                    loader.setLocation(HotelFX.class.getResource("view/feature_location/floor/FloorView.fxml"));

                    FloorController FloorController = new FloorController(this);
                    loader.setController(FloorController);
                    break;
                case "warehouse":
                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse/WarehouseView.fxml"));

                    WarehouseController warehouseController = new WarehouseController(this);
                    loader.setController(warehouseController);
                    break;
                case "Laundry":
                    loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry/LaundryView.fxml"));
                    
                    LaundryController laundryController = new LaundryController(this);
                    loader.setController(laundryController);
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

            //set 'sub feature content' into the center of 'feature_location' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Location
     */
    @FXML
    private JFXButton btnShowLocation;

    @FXML
    private AnchorPane btnShowLocationLayout;

    /**
     * Building
     */
    @FXML
    private JFXButton btnShowBuilding;

    @FXML
    private AnchorPane btnShowBuildingLayout;

    /**
     * Floor
     */
    @FXML
    private JFXButton btnShowFloor;

    @FXML
    private AnchorPane btnShowFloorLayout;

    /**
     * Warehouse
     */
    @FXML
    private JFXButton btnShowWarehouse;

    @FXML
    private AnchorPane btnShowWarehouseLayout;

    /**
     * Laundry
     */
    @FXML
    private JFXButton btnShowLaundry;

    @FXML
    private AnchorPane btnShowLaundryLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowLocationLayout.getStyleClass().add("sub-feature-layout");
        btnShowBuildingLayout.getStyleClass().add("sub-feature-layout");
        btnShowFloorLayout.getStyleClass().add("sub-feature-layout");
        btnShowWarehouseLayout.getStyleClass().add("sub-feature-layout");
        btnShowLaundryLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false, false, false);

        btnShowLocation.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false, false);

                selectedSubFeature.set("location");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false, false);

                    selectedSubFeature.set("location");
                }
            }
        });
        btnShowLocation.setVisible(false);
        btnShowLocationLayout.setVisible(false);

        btnShowBuilding.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false, false);

                selectedSubFeature.set("building");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false, false);

                    selectedSubFeature.set("building");
                }
            }
        });

        btnShowFloor.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false, false);

                selectedSubFeature.set("floor");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false, false);

                    selectedSubFeature.set("floor");
                }
            }
        });

        btnShowWarehouse.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true, false);

                selectedSubFeature.set("warehouse");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true, false);

                    selectedSubFeature.set("warehouse");
                }
            }
        });

        btnShowLaundry.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, true);

                selectedSubFeature.set("Laundry");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, true);

                    selectedSubFeature.set("Laundry");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean location, boolean building, boolean floor, boolean warehouse, boolean laundry) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowLocationLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, location);
        btnShowBuildingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, building);
        btnShowFloorLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, floor);
        btnShowWarehouseLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, warehouse);
        btnShowLaundryLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, laundry);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FLocationManager fLocationManager;

    private FWarehouseManager fWarehouseManager;

    private FLaundryManager fLaundryManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fLocationManager = new FLocationManagerImpl();
        fWarehouseManager = new FWarehouseManagerImpl();
        fLaundryManager = new FLaundryManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FLocationManager getFLocationManager() {
        return this.fLocationManager;
    }

    public FWarehouseManager getFWarehouseManager() {
        return this.fWarehouseManager;
    }

    public FLaundryManager getFLaundryManager() {
        return this.fLaundryManager;
    }

}
