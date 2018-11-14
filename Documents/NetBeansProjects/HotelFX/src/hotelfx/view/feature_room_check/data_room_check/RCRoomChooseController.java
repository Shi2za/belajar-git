/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.data_room_check;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblRoom;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCRoomChooseController implements Initializable {

    @FXML
    private AnchorPane ancFormRoomChoose;

    @FXML
    private GridPane gpFormDataRoomChoose;

    private JFXCComboBoxTablePopup<TblRoom> cbpRoom;

    @FXML
    private JFXButton btnNext;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataRoomChoose() {
        initDataRoomChoosePopup();

        btnNext.setTooltip(new Tooltip("Langkah Selanjutnya.."));
        btnNext.setOnAction((e) -> {
            dataRoomChooseNextHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoomChooseCancelHandle();
        });

    }

    private void initDataRoomChoosePopup() {
        //Room
        TableView<TblRoom> tableRoom = new TableView<>();

        TableColumn<TblRoom, String> roomName = new TableColumn<>("Kamar");
        roomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        roomName.setMinWidth(140);

        tableRoom.getColumns().addAll(roomName);

        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());

        cbpRoom = new JFXCComboBoxTablePopup<>(
                TblRoom.class, tableRoom, roomItems, "", "Kamar *", true, 200, 200
        );

        //attached to grid-pane
        gpFormDataRoomChoose.add(cbpRoom, 1, 1, 2, 1);
    }

    private ObservableList<TblRoom> loadAllDataRoom() {
        return FXCollections.observableArrayList(dataRoomCheckController.getService().getAllDataRoom());
    }

    private void refreshDataPopup() {
        //Room
        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());
        cbpRoom.setItems(roomItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        cbpRoom.valueProperty().bindBidirectional(dataRoomCheckController.selectedData.tblRoomProperty());
        cbpRoom.hide();
    }

    private void dataRoomChooseNextHandle() {
        if (checkDataInputDataRoomChoose()) {
            //close and load room check with room choosen
            dataRoomCheckController.dataRoomCheckCreateHandle();
            dataRoomCheckController.dialogStage.close();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, dataRoomCheckController.dialogStage);
        }
    }

    private void dataRoomChooseCancelHandle() {
        //close form data room choose
        dataRoomCheckController.dialogStage.close();
    }

    private String errDataInput;
    
    private boolean checkDataInputDataRoomChoose() {
        boolean dataInput = true;
        errDataInput = "";
        if(cbpRoom.getValue() == null){
            dataInput = false;
            errDataInput += "Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataRoomChoose();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCRoomChooseController(DataRoomCheckController parentController) {
        dataRoomCheckController = parentController;
    }

    private final DataRoomCheckController dataRoomCheckController;

}
