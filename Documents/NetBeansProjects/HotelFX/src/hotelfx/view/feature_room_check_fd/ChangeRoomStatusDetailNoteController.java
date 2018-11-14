/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblRoom;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ChangeRoomStatusDetailNoteController implements Initializable {

    @FXML
    private AnchorPane ancFormDataRoomStatusDetailNote;

    @FXML
    private GridPane gpFormDataRoomStatusDetailNote;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtRoomStatusDetailNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataChangeRoomStatusDetailNote() {        
        btnSave.setTooltip(new Tooltip("Simpan (Data Status Detail Kamar)"));
        btnSave.setOnAction((e) -> {
            dataChangeRoomStatusDetailNoteSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataChangeRoomStatusDetailNoteCancelHandle();
        });
    }
    
    private void setSelectedDataToInputForm() {

        txtRoomName.setText(roomStatusController.selectedDataRoom.getRoomName());
        
        txtRoomStatusDetailNote.textProperty().bindBidirectional(roomStatusController.selectedDataRoom.roomStatusNoteProperty());
    }

    private void dataChangeRoomStatusDetailNoteSaveHandle() {
        if (checkDataInputDataChangeRoomStatusDetailNote()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblRoom dummySeletedDataRoom = new TblRoom(roomStatusController.selectedDataRoom);
                dummySeletedDataRoom.setRefRoomStatus(new RefRoomStatus(dummySeletedDataRoom.getRefRoomStatus()));
                if (roomStatusController.getService().updateDataRoomStatus(dummySeletedDataRoom)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", roomStatusController.dialogStage);
                    // refresh all data and close form data - change room status
                    roomStatusController.setSelectedDataToInputForm();
                    roomStatusController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(roomStatusController.getService().getErrorMessage(), roomStatusController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusController.dialogStage);
        }
    }

    private void dataChangeRoomStatusDetailNoteCancelHandle() {
        //refresh all data and close form data - change room status
        roomStatusController.setSelectedDataToInputForm();
        roomStatusController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataChangeRoomStatusDetailNote() {
        boolean dataInput = true;
        errDataInput = "";
        return dataInput;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataChangeRoomStatusDetailNote();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public ChangeRoomStatusDetailNoteController(RoomStatusFDController parentController) {
        roomStatusController = parentController;
    }

    private final RoomStatusFDController roomStatusController;
    
}
