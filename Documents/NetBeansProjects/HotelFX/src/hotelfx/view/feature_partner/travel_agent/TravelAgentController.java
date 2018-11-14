/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.travel_agent;

import com.jfoenix.controls.JFXTabPane;
import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.view.feature_partner.FeaturePartnerController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class TravelAgentController implements Initializable {

    /**
     * TAB-PANE
     */
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();
    
    @FXML
    private JFXTabPane tpTravelAgent;
    
    private void setDataTravelAgentTabpane() {
        unSavingDataInput.bind(ClassSession.unSavingDataInput);

        unSavingDataInput.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                for (Tab tab : tpTravelAgent.getTabs()) {
                    if(tpTravelAgent.getSelectionModel().getSelectedItem() != null
                            && tpTravelAgent.getSelectionModel().getSelectedItem().equals(tab)){
                        tab.setDisable(false);
                    }else{
                        tab.setDisable(true);
                    }
                }
            } else {
                for (Tab tab : tpTravelAgent.getTabs()) {
                    tab.setDisable(false);
                }
            }
        });
    }
    
    @FXML
    private AnchorPane ancTravelAgentDataLayout;
    
    @FXML
    private AnchorPane ancTravelAgentRoomTypeAvailableLayout;
    
    private void setDataContent(){
        //travel agent data
        setTravelAgentDataContent();
        //travel agent - room type (setting)
        setTravelAgentRoomTypeSettingInputContent();
//        //travel agent - room type (available)
//        setTravelAgentRoomTypeAvailableContent();
    }
    
    private void setTravelAgentDataContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/travel_agent/TravelAgentDataView.fxml"));

            //set controller
            TravelAgentDataController travelAgentDataController = new TravelAgentDataController(this);
            loader.setController(travelAgentDataController);

            Node dataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(dataContent, 0.0);
            AnchorPane.setLeftAnchor(dataContent, 0.0);
            AnchorPane.setRightAnchor(dataContent, 0.0);
            AnchorPane.setBottomAnchor(dataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancTravelAgentDataLayout.getChildren().clear();
            ancTravelAgentDataLayout.getChildren().add(dataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    private void setTravelAgentRoomTypeSettingInputContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/travel_agent/TravelAgentRoomTypeSettingInputView.fxml"));

            //set controller
            TravelAgentRoomTypeSettingInputController travelAgentRoomTypeSettingInputController = new TravelAgentRoomTypeSettingInputController(this);
            loader.setController(travelAgentRoomTypeSettingInputController);

            Node dataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(dataContent, 15.0);
            AnchorPane.setLeftAnchor(dataContent, 10.0);
            AnchorPane.setRightAnchor(dataContent, 10.0);
            AnchorPane.setBottomAnchor(dataContent, 10.0);

            //set 'data' into the center of dashboard.
            ancTravelAgentRoomTypeAvailableLayout.getChildren().clear();
            ancTravelAgentRoomTypeAvailableLayout.getChildren().add(dataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
//    private void setTravelAgentRoomTypeAvailableContent() {
//        try {
//            //loader data (path)
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_partner/travel_agent/TravelAgentRoomTypeAvailableView.fxml"));
//
//            //set controller
//            TravelAgentRoomTypeAvailableController travelAgentRoomTypeAvailableController = new TravelAgentRoomTypeAvailableController(this);
//            loader.setController(travelAgentRoomTypeAvailableController);
//
//            Node dataContent = loader.load();
//
//            //set anchor position
//            AnchorPane.setTopAnchor(dataContent, 0.0);
//            AnchorPane.setLeftAnchor(dataContent, 0.0);
//            AnchorPane.setRightAnchor(dataContent, 0.0);
//            AnchorPane.setBottomAnchor(dataContent, 0.0);
//
//            //set 'data' into the center of dashboard.
//            ancTravelAgentRoomTypeAvailableLayout.getChildren().clear();
//            ancTravelAgentRoomTypeAvailableLayout.getChildren().add(dataContent);
//
//        } catch (Exception e) {
//            System.out.println("err >> " + e.getMessage());
//        }
//    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set tab pane
        setDataTravelAgentTabpane();
        
        //set data content
        setDataContent();
    }

    public TravelAgentController(FeaturePartnerController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePartnerController parentController;

    public FPartnerManager getService(){
        return parentController.getFPartnerManager();
    }
    
}
