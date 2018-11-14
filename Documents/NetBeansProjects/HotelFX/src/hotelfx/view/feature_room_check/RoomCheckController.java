/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check;

import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FRoomCheckManager;
import hotelfx.persistence.service.FRoomCheckManagerImpl;
import hotelfx.view.feature_room_check.room_check_history.RoomCheckHistoryController;
import hotelfx.view.feature_room_check.room_status.RoomStatusController;
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
public class RoomCheckController implements Initializable {

    @FXML
    private AnchorPane ancRoomStatusLayout;

    @FXML
    private AnchorPane ancRoomCheckHistory;

    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();

    @FXML
    private TabPane tabPaneLayout;

    private void setDataRoomCheckTabpane() {
        unSavingDataInput.bind(ClassSession.unSavingDataInput);

        unSavingDataInput.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                for (Tab tab : tabPaneLayout.getTabs()) {
                    if (tabPaneLayout.getSelectionModel().getSelectedItem() != null
                            && tabPaneLayout.getSelectionModel().getSelectedItem().equals(tab)) {
                        tab.setDisable(false);
                    } else {
                        tab.setDisable(true);
                    }
                }
            } else {
                for (Tab tab : tabPaneLayout.getTabs()) {
                    tab.setDisable(false);
                }
            }
        });

        tabPaneLayout.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null
                    && newVal.intValue() == 1) {
                //room check history
                setRoomCheckHistoryContent();
            } else {
                //room status
                setRoomStatusContent();
            }
        });
    }

    private void setDataContent() {
        //room status
        setRoomStatusContent();
        //room check history
        setRoomCheckHistoryContent();
    }

    private void setRoomStatusContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/RoomStatusView.fxml"));

            //set controller
            RoomStatusController roomStatusController = new RoomStatusController(this);
            loader.setController(roomStatusController);

            Node roomChekcDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(roomChekcDataContent, 0.0);
            AnchorPane.setLeftAnchor(roomChekcDataContent, 0.0);
            AnchorPane.setRightAnchor(roomChekcDataContent, 0.0);
            AnchorPane.setBottomAnchor(roomChekcDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancRoomStatusLayout.getChildren().clear();
            ancRoomStatusLayout.getChildren().add(roomChekcDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private void setRoomCheckHistoryContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_check_history/RoomCheckHistoryView.fxml"));

            //set controller
            RoomCheckHistoryController roomCheckHistoryController = new RoomCheckHistoryController(this);
            loader.setController(roomCheckHistoryController);

            Node roomCheckHistoryDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(roomCheckHistoryDataContent, 0.0);
            AnchorPane.setLeftAnchor(roomCheckHistoryDataContent, 0.0);
            AnchorPane.setRightAnchor(roomCheckHistoryDataContent, 0.0);
            AnchorPane.setBottomAnchor(roomCheckHistoryDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancRoomCheckHistory.getChildren().clear();
            ancRoomCheckHistory.getChildren().add(roomCheckHistoryDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    public void refreshAllContent() {
        setDataContent();
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FRoomCheckManager fRoomCheckManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fRoomCheckManager = new FRoomCheckManagerImpl();

        //set tab pane
        setDataRoomCheckTabpane();

        //set data content
        setDataContent();

        //set tab-pane listener
        tabPaneLayout.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.getText().equals("Status Kamar")) {
                    setRoomCheckHistoryContent();
                } else {
                    setRoomStatusContent();
                }
            }
        });
    }

    public FRoomCheckManager getFRoomCheckManager() {
        return this.fRoomCheckManager;
    }
}
