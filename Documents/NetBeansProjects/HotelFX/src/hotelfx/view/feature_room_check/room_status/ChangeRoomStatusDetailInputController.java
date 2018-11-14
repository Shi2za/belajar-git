/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomStatusDetail;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
 * @author ANDRI
 */
public class ChangeRoomStatusDetailInputController implements Initializable {

    @FXML
    private AnchorPane ancFormDataRoomStatusDetailInput;

    @FXML
    private GridPane gpFormDataRoomStatusDetailInput;

    @FXML
    private JFXDatePicker dtpDetailDate;

    @FXML
    private JFXTimePicker tmpDetailTime;

    @FXML
    private JFXTextArea txtRoomStatusDetail;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataChangeRoomStatusDetailInput() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Detail Status)"));
        btnSave.setOnAction((e) -> {
            dataRoomStatusDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoomStatusDetailCancelHandle();
        });

        initDateCalendar();
        
        initImportantFieldColor();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpDetailDate);
        ClassFormatter.setDatePickersEnableDate(
                (LocalDate.now()).minusDays(1),
                (LocalDate.now()).plusDays(1),
                dtpDetailDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dtpDetailDate, 
                tmpDetailTime, 
                txtRoomStatusDetail);
    }
    
    private void setSelectedDataToInputForm() {

        txtRoomStatusDetail.textProperty().bindBidirectional(changeRoomStatusDetailController.selectedDataRoomStatusDetail.roomStatusDetailProperty());

        if (changeRoomStatusDetailController.selectedDataRoomStatusDetail.getRoomStatusDetailDate() != null) {
            dtpDetailDate.setValue(changeRoomStatusDetailController.selectedDataRoomStatusDetail.getRoomStatusDetailDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            tmpDetailTime.setValue(changeRoomStatusDetailController.selectedDataRoomStatusDetail.getRoomStatusDetailDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        } else {
            dtpDetailDate.setValue(null);
            tmpDetailTime.setValue(null);
        }

    }

    private void dataRoomStatusDetailSaveHandle() {
        if (checkDataRoomStatusDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", changeRoomStatusDetailController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                LocalDateTime localDateTime = LocalDateTime.of(dtpDetailDate.getValue(), tmpDetailTime.getValue());
                changeRoomStatusDetailController.selectedDataRoomStatusDetail.setRoomStatusDetailDate(Timestamp.valueOf(localDateTime));
                //dummy entry
                TblRoomStatusDetail dummySelectedDataRoomStatusDetail = new TblRoomStatusDetail(changeRoomStatusDetailController.selectedDataRoomStatusDetail);
                dummySelectedDataRoomStatusDetail.setTblRoom(new TblRoom(dummySelectedDataRoomStatusDetail.getTblRoom()));
                switch (changeRoomStatusDetailController.dataInputRoomStatusDetailStatus) {
                    case 0:
                        if (changeRoomStatusDetailController.getPartentController().getService().insertDataRoomStatusDetail(dummySelectedDataRoomStatusDetail) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", changeRoomStatusDetailController.dialogStage);
                            //refresh data table (room status detail : room) and  close form data customer bank account
                            changeRoomStatusDetailController.tableDataRoomStatusDetail.getTableView().setItems(FXCollections.observableArrayList(changeRoomStatusDetailController.loadAllDataRoomStatusDetail()));
                            changeRoomStatusDetailController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(changeRoomStatusDetailController.getPartentController().getService().getErrorMessage(), changeRoomStatusDetailController.dialogStage);
                        }
                        break;
                    case 1:
                        if (changeRoomStatusDetailController.getPartentController().getService().updateDataRoomStatusDetail(dummySelectedDataRoomStatusDetail)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", changeRoomStatusDetailController.dialogStage);
                            //refresh data table (room status detail : room) and  close form data customer bank account
                            changeRoomStatusDetailController.tableDataRoomStatusDetail.getTableView().setItems(FXCollections.observableArrayList(changeRoomStatusDetailController.loadAllDataRoomStatusDetail()));
                            changeRoomStatusDetailController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(changeRoomStatusDetailController.getPartentController().getService().getErrorMessage(), changeRoomStatusDetailController.dialogStage);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, changeRoomStatusDetailController.dialogStage);
        }
    }

    private void dataRoomStatusDetailCancelHandle() {
        //refresh data table (room status detail : room) and  close form data customer bank account
        changeRoomStatusDetailController.tableDataRoomStatusDetail.getTableView().setItems(FXCollections.observableArrayList(changeRoomStatusDetailController.loadAllDataRoomStatusDetail()));
        changeRoomStatusDetailController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataRoomStatusDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (dtpDetailDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {

        }
        if (tmpDetailTime.getValue() == null) {
            dataInput = false;
            errDataInput += "Waktu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomStatusDetail.getText() == null || txtRoomStatusDetail.getText().equals("")) {
            dataInput = false;
            errDataInput += "Status (Detail) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataChangeRoomStatusDetailInput();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ChangeRoomStatusDetailInputController(ChangeRoomStatusDetailController parentController) {
        changeRoomStatusDetailController = parentController;
    }

    private final ChangeRoomStatusDetailController changeRoomStatusDetailController;

}
