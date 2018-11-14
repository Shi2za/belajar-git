/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FStockOpnameManager;
import hotelfx.persistence.service.FStockOpnameManagerImpl;
import hotelfx.persistence.service.FWarehouseManager;
import hotelfx.persistence.service.FWarehouseManagerImpl;
import hotelfx.view.feature_warehouse.warehouse_in_coming.WarehouseInComingController;
import hotelfx.view.feature_warehouse.warehouse_mutation_history.WarehouseMutationHistoryController;
import hotelfx.view.feature_warehouse.warehouse_out_going.WarehouseOutGoingController;
import hotelfx.view.feature_warehouse.warehouse_stock_opname.WarehouseStockOpnameController;
import hotelfx.view.feature_warehouse.warehouse_store_request.WarehouseStoreRequestController;
import hotelfx.view.feature_warehouse.warehouse_transfer_item.WarehouseTransferItemController;
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
public class FeatureWarehouseController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "transfer_item":
                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_transfer_item/WarehouseTransferItemView.fxml"));

                    WarehouseTransferItemController warehouseTransferItemController = new WarehouseTransferItemController(this);
                    loader.setController(warehouseTransferItemController);
                    break;

                case "store_request":
                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_store_request/WarehouseStoreRequestView.fxml"));

                    WarehouseStoreRequestController warehouseStoreRequestController = new WarehouseStoreRequestController(this);
                    loader.setController(warehouseStoreRequestController);
                    break;

//                case "out_going":
//                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_out_going/WarehouseOutGoingView.fxml"));
//
//                    WarehouseOutGoingController warehouseOutGoingController = new WarehouseOutGoingController(this);
//                    loader.setController(warehouseOutGoingController);
//                    break;
                case "in_coming":
                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_in_coming/WarehouseInComingView.fxml"));

                    WarehouseInComingController warehouseInComingController = new WarehouseInComingController(this);
                    loader.setController(warehouseInComingController);
                    break;

                case "out_going":
                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_out_going/WarehouseOutGoingView.fxml"));

                    WarehouseOutGoingController warehouseOutGoingController = new WarehouseOutGoingController(this);
                    loader.setController(warehouseOutGoingController);
                    break;

                case "stock_opname":
                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_stock_opname/WarehouseStockOpnameView.fxml"));

                    WarehouseStockOpnameController warehouseStockOpnameController = new WarehouseStockOpnameController(this);
                    loader.setController(warehouseStockOpnameController);
                    break;

                case "mutation_history":
                    loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_mutation_history/WarehouseMutationHistoryView.fxml"));

                    WarehouseMutationHistoryController warehouseMutationHistoryController = new WarehouseMutationHistoryController(this);
                    loader.setController(warehouseMutationHistoryController);
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

    @FXML
    private JFXButton btnShowWarehouseTransferItem;

    @FXML
    private AnchorPane btnShowWarehouseTransferItemLayout;

    @FXML
    private JFXButton btnShowWarehouseSR;

    @FXML
    private AnchorPane btnShowWarehouseSRLayout;

    @FXML
    private JFXButton btnShowWarehouseInComing;

    @FXML
    private AnchorPane btnShowWarehouseInComingLayout;

    @FXML
    private JFXButton btnShowWarehouseOutGoing;

    @FXML
    private AnchorPane btnShowWarehouseOutGoingLayout;

    @FXML
    private JFXButton btnShowWarehouseStockOpname;

    @FXML
    private AnchorPane btnShowWarehouseStockOpnameLayout;
    
    @FXML
    private JFXButton btnShowWarehouseMutationHistory;

    @FXML
    private AnchorPane btnShowWarehouseMutationHistoryLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowWarehouseTransferItemLayout.getStyleClass().add("sub-feature-layout");
        btnShowWarehouseSRLayout.getStyleClass().add("sub-feature-layout");
        btnShowWarehouseInComingLayout.getStyleClass().add("sub-feature-layout");
        btnShowWarehouseOutGoingLayout.getStyleClass().add("sub-feature-layout");
        btnShowWarehouseStockOpnameLayout.getStyleClass().add("sub-feature-layout");
        btnShowWarehouseMutationHistoryLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false, false, false, false);

        btnShowWarehouseTransferItem.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false, false, false);

                selectedSubFeature.set("transfer_item");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false, false, false);

                    selectedSubFeature.set("transfer_item");
                }
            }
        });

        btnShowWarehouseSR.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false, false, false);

                selectedSubFeature.set("store_request");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false, false, false);

                    selectedSubFeature.set("store_request");
                }
            }
        });

        btnShowWarehouseInComing.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false, false, false);

                selectedSubFeature.set("in_coming");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false, false, false);

                    selectedSubFeature.set("in_coming");
                }
            }
        });

        btnShowWarehouseOutGoing.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true, false, false);

                selectedSubFeature.set("out_going");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true, false, false);

                    selectedSubFeature.set("out_going");
                }
            }
        });

        btnShowWarehouseStockOpname.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, true, false);

                selectedSubFeature.set("stock_opname");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, true, false);

                    selectedSubFeature.set("stock_opname");
                }
            }
        });
        
        btnShowWarehouseMutationHistory.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, false, true);

                selectedSubFeature.set("mutation_history");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, false, true);

                    selectedSubFeature.set("mutation_history");
                }
            }
        });

    }

    private void setButtonDataPseudoClass(boolean sfc, boolean transferItem, boolean storeRequest, 
            boolean inComing, boolean outGoing, boolean stockOpname, boolean mutationHistory) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowWarehouseTransferItemLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, transferItem);
        btnShowWarehouseSRLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, storeRequest);
        btnShowWarehouseInComingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, inComing);
        btnShowWarehouseOutGoingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, outGoing);
        btnShowWarehouseStockOpnameLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, stockOpname);
        btnShowWarehouseMutationHistoryLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, mutationHistory);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FWarehouseManager fWarehouseManager;

    private FStockOpnameManager fStockOpnameManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fWarehouseManager = new FWarehouseManagerImpl();
        fStockOpnameManager = new FStockOpnameManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FWarehouseManager getFWarehouseManager() {
        return this.fWarehouseManager;
    }

    public FStockOpnameManager getFStockOpnameManager() {
        return this.fStockOpnameManager;
    }

}
