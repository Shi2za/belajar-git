/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FRoomManager;
import hotelfx.persistence.service.FRoomManagerImpl;
import hotelfx.view.feature_room.room_mutation_history.RoomMutationHistoryController;
import hotelfx.view.feature_room.room_transfer_item.RoomTransferItemController;
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
public class FeatureRoomController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "room_transfer_item":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room/room_transfer_item/RoomTransferItemView.fxml"));

                    RoomTransferItemController roomTransferItemController = new RoomTransferItemController(this);
                    loader.setController(roomTransferItemController);
                    break;
                case "room_mutation_history":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room/room_mutation_history/RoomMutationHistoryView.fxml"));

                    RoomMutationHistoryController roomMutationHistoryController = new RoomMutationHistoryController(this);
                    loader.setController(roomMutationHistoryController);
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
     * Transfer Item
     */
    @FXML
    private JFXButton btnShowRoomTransferItem;

    @FXML
    private AnchorPane btnShowRoomTransferItemLayout;

    /**
     * Mutation History
     */
    @FXML
    private JFXButton btnShowRoomMutationHistory;

    @FXML
    private AnchorPane btnShowRoomMutationHistoryLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowRoomTransferItemLayout.getStyleClass().add("sub-feature-layout");
        btnShowRoomMutationHistoryLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowRoomTransferItem.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("room_transfer_item");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("room_transfer_item");
                }
            }
        });

        btnShowRoomMutationHistory.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("room_mutation_history");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("room_mutation_history");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc,
            boolean roomTransferItem, boolean roomMutationHistory) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);

        btnShowRoomTransferItemLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, roomTransferItem);
        btnShowRoomMutationHistoryLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, roomMutationHistory);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FRoomManager fRoomManager;

    /**
     * Initializes the controller class.
     *
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
