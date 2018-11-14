/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_and_service_md;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FRoomManager;
import hotelfx.persistence.service.FRoomManagerImpl;
import hotelfx.view.feature_room.room.RoomController;
import hotelfx.view.feature_room.room_service.RoomServiceController;
import hotelfx.view.feature_room.room_type.RoomTypeController;
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
public class FeatureRoomAndServiceMDController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            //room rate (feature)
            switch (subFeature) {
                case "room":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room/room/RoomView.fxml"));

                    RoomController roomController = new RoomController(this);
                    loader.setController(roomController);
                    break;
                case "room_type":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room/room_type/RoomTypeView.fxml"));

                    RoomTypeController roomTypeController = new RoomTypeController(this);
                    loader.setController(roomTypeController);
                    break;
                case "room_service":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room/room_service/RoomServiceView.fxml"));

                    RoomServiceController roomServiceController = new RoomServiceController(this);
                    loader.setController(roomServiceController);
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
     * Room
     */
    @FXML
    private JFXButton btnShowRoom;

    @FXML
    private AnchorPane btnShowRoomLayout;

    /**
     * Room Type
     */
    @FXML
    private JFXButton btnShowRoomType;

    @FXML
    private AnchorPane btnShowRoomTypeLayout;

    /**
     * Room Service
     */
    @FXML
    private JFXButton btnShowRoomService;

    @FXML
    private AnchorPane btnShowRoomServiceLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowRoomLayout.getStyleClass().add("sub-feature-layout");
        btnShowRoomTypeLayout.getStyleClass().add("sub-feature-layout");
        btnShowRoomServiceLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false);

        btnShowRoom.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false);

                selectedSubFeature.set("room");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false);

                    selectedSubFeature.set("room");
                }
            }
        });

        btnShowRoomType.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false);

                selectedSubFeature.set("room_type");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false);

                    selectedSubFeature.set("room_type");
                }
            }
        });

        btnShowRoomService.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true);

                selectedSubFeature.set("room_service");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true);

                    selectedSubFeature.set("room_service");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc,
            boolean room, boolean roomType, boolean roomService) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);

        btnShowRoomLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, room);
        btnShowRoomTypeLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, roomType);
        btnShowRoomServiceLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, roomService);
    }
    
    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FRoomManager fRoomManager;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fRoomManager = new FRoomManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }    
    
    public FRoomManager getFRoomManager() {
        return this.fRoomManager;
    }
    
}
