/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_item_and_asset_md;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FAssetManagerImpl;
import hotelfx.persistence.service.FItemAndAssetManager;
import hotelfx.persistence.service.FItemAndAssetManagerImpl;
import hotelfx.persistence.service.FPropertyBarcodeManagerImpl;
import hotelfx.persistence.service.FUnitManagerImpl;
import hotelfx.view.feature_asset.FeatureAssetController;
import hotelfx.view.feature_asset.asset.AssetController;
import hotelfx.view.feature_item_and_asset_md.item.ItemController;
import hotelfx.view.feature_item_and_asset_md.item_expired_date.ItemExpiredDateController;
import hotelfx.view.feature_item_and_asset_md.item_stock.ItemStockController;
import hotelfx.view.feature_item_and_asset_md.item_type.ItemTypeController;
import hotelfx.view.feature_property_barcode.FeaturePropertyBarcodeController;
import hotelfx.view.feature_property_barcode.property_barcode.PropertyBarcodeController;
import hotelfx.view.feature_property_barcode.property_barcode_stock.PropertyBarcodeStockController;
import hotelfx.view.feature_unit.FeatureUnitController;
import hotelfx.view.feature_unit.unit.UnitController;
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
public class FeatureItemAndAssetMDController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            //Property Barcode Controller
            FeaturePropertyBarcodeController featurePropertyBarcodeController = new FeaturePropertyBarcodeController();
            featurePropertyBarcodeController.setFPropertyBarcodeManager(new FPropertyBarcodeManagerImpl());
            //Asset Controller
            FeatureAssetController featureAssetController = new FeatureAssetController();
            featureAssetController.setFAssetManager(new FAssetManagerImpl());
            //Unit Controller
            FeatureUnitController featureUnitController = new FeatureUnitController();
            featureUnitController.setFUnitManager(new FUnitManagerImpl());
            switch (subFeature) {
                case "item":
                    loader.setLocation(HotelFX.class.getResource("view/feature_item_and_asset_md/item/ItemView.fxml"));

                    ItemController itemController = new ItemController(this);
                    loader.setController(itemController);
                    break;

                case "item_type":
                    loader.setLocation(HotelFX.class.getResource("view/feature_item_and_asset_md/item_type/ItemTypeView.fxml"));

                    ItemTypeController itemTypeController = new ItemTypeController(this);
                    loader.setController(itemTypeController);
                    break;
                    
                case "item_stock":
                    loader.setLocation(HotelFX.class.getResource("view/feature_item_and_asset_md/item_stock/ItemStockView.fxml"));

                    ItemStockController itemStockController = new ItemStockController(this);
                    loader.setController(itemStockController);
                    break;

                case "item_expired_date":
                    loader.setLocation(HotelFX.class.getResource("view/feature_item_and_asset_md/item_expired_date/ItemExpiredDateView.fxml"));

                    ItemExpiredDateController itemExpiredDateController = new ItemExpiredDateController(this);
                    loader.setController(itemExpiredDateController);
                    break;

                case "property_barcode":
                    loader.setLocation(HotelFX.class.getResource("view/feature_property_barcode/property_barcode/PropertyBarcodeView.fxml"));

                    PropertyBarcodeController propertyBarcodeController = new PropertyBarcodeController(featurePropertyBarcodeController);
                    loader.setController(propertyBarcodeController);
                    break;

                case "property_barcode_stock":
                    loader.setLocation(HotelFX.class.getResource("view/feature_property_barcode/property_barcode_stock/PropertyBarcodeStockView.fxml"));
                    PropertyBarcodeStockController propertyBarcodeStockController = new PropertyBarcodeStockController(featurePropertyBarcodeController);
                    loader.setController(propertyBarcodeStockController);
                    break;

                case "asset":
                    loader.setLocation(HotelFX.class.getResource("view/feature_asset/asset/AssetView.fxml"));
                    AssetController assetController = new AssetController(featureAssetController);
                    loader.setController(assetController);
                    break;

                case "unit":
                    loader.setLocation(HotelFX.class.getResource("view/feature_unit/unit/UnitView.fxml"));

                    UnitController unitController = new UnitController(featureUnitController);
                    loader.setController(unitController);
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

            //set 'sub feature content' into the center of 'item' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Item
     */
    @FXML
    private JFXButton btnShowItem;

    @FXML
    private AnchorPane btnShowItemLayout;
    
    /**
     * Item Type
     */
    @FXML
    private JFXButton btnShowItemType;

    @FXML
    private AnchorPane btnShowItemTypeLayout;

    /**
     * Item Stock
     */
    @FXML
    private JFXButton btnShowItemStock;

    @FXML
    private AnchorPane btnShowItemStockLayout;

    /**
     * Item Expired Date
     */
    @FXML
    private JFXButton btnShowItemExpiredDate;

    @FXML
    private AnchorPane btnShowItemExpiredDateLayout;

    /**
     * Property Barcode
     */
    @FXML
    private JFXButton btnShowPropertyBarcode;

    @FXML
    private AnchorPane btnShowPropertyBarcodeLayout;

    /**
     * Property Barcode - Stock
     */
    @FXML
    private JFXButton btnShowPropertyBarcodeStock;

    @FXML
    private AnchorPane btnShowPropertyBarcodeStockLayout;

    /**
     * Asset
     */
    @FXML
    private JFXButton btnShowAsset;

    @FXML
    private AnchorPane btnShowAssetLayout;
    
    /**
     * Unit
     */
    @FXML
    private JFXButton btnShowUnit;

    @FXML
    private AnchorPane btnShowUnitLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowItemLayout.getStyleClass().add("sub-feature-layout");
        btnShowItemTypeLayout.getStyleClass().add("sub-feature-layout");
        btnShowItemStockLayout.getStyleClass().add("sub-feature-layout");
        btnShowItemExpiredDateLayout.getStyleClass().add("sub-feature-layout");
        btnShowPropertyBarcodeLayout.getStyleClass().add("sub-feature-layout");
        btnShowPropertyBarcodeStockLayout.getStyleClass().add("sub-feature-layout");
        btnShowAssetLayout.getStyleClass().add("sub-feature-layout");
        btnShowUnitLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false, false, false, false, false, false);

        btnShowItem.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false, false, false, false, false);

                selectedSubFeature.set("item");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false, false, false, false, false);

                    selectedSubFeature.set("item");
                }
            }
        });

        btnShowItemType.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false, false, false, false, false);

                selectedSubFeature.set("item_type");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false, false, false, false, false);

                    selectedSubFeature.set("item_type");
                }
            }
        });
        
        btnShowItemStock.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false, false, false, false, false);

                selectedSubFeature.set("item_stock");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false, false, false, false, false);

                    selectedSubFeature.set("item_stock");
                }
            }
        });

        btnShowItemExpiredDate.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true, false, false, false, false);

                selectedSubFeature.set("item_expired_date");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true, false, false, false, false);

                    selectedSubFeature.set("item_expired_date");
                }
            }
        });

        btnShowPropertyBarcode.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, true, false, false, false);

                selectedSubFeature.set("property_barcode");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, true, false, false, false);

                    selectedSubFeature.set("property_barcode");
                }
            }
        });

        btnShowPropertyBarcodeStock.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, false, true, false, false);

                selectedSubFeature.set("property_barcode_stock");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, false, true, false, false);

                    selectedSubFeature.set("property_barcode_stock");
                }
            }
        });

        btnShowAsset.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, false, false, true, false);
                
                selectedSubFeature.set("asset");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    
                    setButtonDataPseudoClass(true, false, false, false, false, false, false, true, false);
                    
                    selectedSubFeature.set("asset");
                }
            }

        });
        
        btnShowUnit.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, false, false, false, true);

                selectedSubFeature.set("unit");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, false, false, false, true);

                    selectedSubFeature.set("unit");
                }
            }
        });

    }

    private void setButtonDataPseudoClass(boolean sfc, 
            boolean item, boolean itemType, 
            boolean itemStock, boolean itemExpiredDate,
            boolean propertyBarcode, boolean propertyBarcodeStock, boolean asset, 
            boolean unit) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        
        btnShowItemLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, item);
        btnShowItemTypeLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, itemType);
        
        btnShowItemStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, itemStock);
        btnShowItemExpiredDateLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, itemExpiredDate);

        btnShowPropertyBarcodeLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, propertyBarcode);
        btnShowPropertyBarcodeStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, propertyBarcodeStock);
        
        btnShowAssetLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, asset);

        btnShowUnitLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, unit);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FItemAndAssetManager fItemAndAssetManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fItemAndAssetManager = new FItemAndAssetManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FItemAndAssetManager getFItemAndAssetManager() {
        return this.fItemAndAssetManager;
    }

}
