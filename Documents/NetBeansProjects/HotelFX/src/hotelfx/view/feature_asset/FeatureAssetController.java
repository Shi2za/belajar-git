/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_asset;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FAssetManager;
import hotelfx.persistence.service.FAssetManagerImpl;
import hotelfx.view.feature_asset.asset.AssetController;
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
 *
 * @author Andreas
 */
public class FeatureAssetController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");
    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "asset":
                    loader.setLocation(HotelFX.class.getResource("view/feature_asset/asset/AssetView.fxml"));
                    AssetController assetController = new AssetController(this);
                    loader.setController(assetController);
                    break;
                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }

            Node subContent = loader.load();
            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);

            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (IOException ex) {
            Logger.getLogger(FeatureAssetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private JFXButton btnShowAsset;

    @FXML
    private AnchorPane btnShowAssetLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowAssetLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false, false);
        
        btnShowAsset.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true);
                
                selectedSubFeature.set("asset");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    
                    setButtonDataPseudoClass(true, true);
                    
                    selectedSubFeature.set("asset");
                }
            }

        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean asset) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowAssetLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, asset);
    }
    
    private FAssetManager fAssetManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fAssetManager = new FAssetManagerImpl();
        setButtonDataShow();
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public void setFAssetManager(FAssetManager fAssetManager) {
        this.fAssetManager = fAssetManager;
    }
    
    public FAssetManager getFAssetManager() {
        return this.fAssetManager;
    }
}
