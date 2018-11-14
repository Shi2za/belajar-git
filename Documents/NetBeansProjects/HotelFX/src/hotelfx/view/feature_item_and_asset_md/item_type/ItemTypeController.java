/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_item_and_asset_md.item_type;

import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FItemAndAssetManager;
import hotelfx.view.feature_item_and_asset_md.FeatureItemAndAssetMDController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ItemTypeController implements Initializable {

    @FXML
    private AnchorPane ancItemTypeHK;
    
    @FXML
    private AnchorPane ancItemTypeWH;
    
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();
    
    @FXML
    private TabPane tabPaneLayout;
    
    private void setDataPOPAPTabpane() {
        unSavingDataInput.bind(ClassSession.unSavingDataInput);

        unSavingDataInput.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                for (Tab tab : tabPaneLayout.getTabs()) {
                    if(tabPaneLayout.getSelectionModel().getSelectedItem() != null
                            && tabPaneLayout.getSelectionModel().getSelectedItem().equals(tab)){
                        tab.setDisable(false);
                    }else{
                        tab.setDisable(true);
                    }
                }
            } else {
                for (Tab tab : tabPaneLayout.getTabs()) {
                    tab.setDisable(false);
                }
            }
        });
    }
    
    private void setDataContent(){
        //item type hk
        setItemTypeHKContent();
        //item type wh
        setItemTypeWHContent();
    }
    
    private void setItemTypeHKContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_item_and_asset_md/item_type/ItemTypeHKView.fxml"));

            //set controller
            ItemTypeHKController itemTypeHKController = new ItemTypeHKController(this);
            loader.setController(itemTypeHKController);

            Node dataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(dataContent, 0.0);
            AnchorPane.setLeftAnchor(dataContent, 0.0);
            AnchorPane.setRightAnchor(dataContent, 0.0);
            AnchorPane.setBottomAnchor(dataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancItemTypeHK.getChildren().clear();
            ancItemTypeHK.getChildren().add(dataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    private void setItemTypeWHContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_item_and_asset_md/item_type/ItemTypeWHView.fxml"));

            //set controller
            ItemTypeWHController itemTypeWHController = new ItemTypeWHController(this);
            loader.setController(itemTypeWHController);

            Node dataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(dataContent, 0.0);
            AnchorPane.setLeftAnchor(dataContent, 0.0);
            AnchorPane.setRightAnchor(dataContent, 0.0);
            AnchorPane.setBottomAnchor(dataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancItemTypeWH.getChildren().clear();
            ancItemTypeWH.getChildren().add(dataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    public void refreshAllContent(){
        setDataContent();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set tab pane
        setDataPOPAPTabpane();
        
        //set data content
        setDataContent();
        
        //set tab-pane listener
        tabPaneLayout.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                if(newVal.getText().equals("House Keeping")){
                    setItemTypeHKContent();
                }else{
                    setItemTypeWHContent();
                }
            }
        });
    }    
    
    public ItemTypeController(FeatureItemAndAssetMDController parentController) {
        this.parentController = parentController;
    }

    private final FeatureItemAndAssetMDController parentController;
 
    public FItemAndAssetManager getService() {
        return parentController.getFItemAndAssetManager();
    }
    
}
