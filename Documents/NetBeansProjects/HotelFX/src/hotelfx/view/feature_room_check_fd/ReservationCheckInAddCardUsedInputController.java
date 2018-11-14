/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblRoomType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ReservationCheckInAddCardUsedInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCheckIn;

    @FXML
    private GridPane gpFormDataCheckIn;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtAdditionalCardUsedNumber;

    @FXML
    private JFXTextField txtCardBrokenCharge;

    @FXML
    private JFXTextField txtTotalAdditionalDeposit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCheckInAddCardUsedNumber() {
        selectedDataCheckIn = roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut();

        btnSave.setTooltip(new Tooltip("Simpan (Data Tambahan Kartu Kamar)"));
        btnSave.setOnAction((e) -> {
            dataCheckInAddCardUsedNumberSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCheckInAddCardUsedNumberCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();

        //total deposit (needed)
        txtTotalAdditionalDeposit.setText(ClassFormatter.currencyFormat.cFormat(
                (new BigDecimal(roomStatusFDController.additionalCardUsedNumber.get()))
                .multiply(selectedDataCheckIn.getBrokenCardCharge())));
        roomStatusFDController.additionalCardUsedNumber.addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalAdditionalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(selectedDataCheckIn.getBrokenCardCharge())));
            } else {
                txtTotalAdditionalDeposit.setText(ClassFormatter.currencyFormat.cFormat(0));
            }
        });
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtAdditionalCardUsedNumber);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtCardBrokenCharge);
        ClassFormatter.setToNumericField(
                "Integer",
                txtAdditionalCardUsedNumber);
    }

    private TblReservationCheckInOut selectedDataCheckIn;

    private void setSelectedDataToInputForm() {

        txtRoomName.textProperty().bind(selectedDataCheckIn.getTblRoom().roomNameProperty());

        Bindings.bindBidirectional(txtAdditionalCardUsedNumber.textProperty(), roomStatusFDController.additionalCardUsedNumber, new NumberStringConverter());
        Bindings.bindBidirectional(txtCardBrokenCharge.textProperty(), selectedDataCheckIn.brokenCardChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataCheckInAddCardUsedNumberSaveHandle() {
        if (checkDataInputDataCheckInAddCardUsedNumber()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusFDController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
                TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail = new TblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
                dummySelectedDataReservationRoomTypeDetail.setTblReservation(dummySelectedData);
                dummySelectedDataReservationRoomTypeDetail.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail.getTblRoomType()));
                dummySelectedDataReservationRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut()));
                if (roomStatusFDController.getServiceRV2().updateDataReservationRoomTypeDetailCheckInAddCardUsedNumber(
                        dummySelectedData,
                        dummySelectedDataReservationRoomTypeDetail,
                        roomStatusFDController.additionalCardUsedNumber.get())) {
                    ClassMessage.showSucceedInsertingDataMessage("", roomStatusFDController.dialogStage);
                    // refresh all data and close form data - reservation additional service
                    roomStatusFDController.setSelectedDataToInputForm();
                    roomStatusFDController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(roomStatusFDController.getServiceRV2().getErrorMessage(), roomStatusFDController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusFDController.dialogStage);
        }
    }

    private void dataCheckInAddCardUsedNumberCancelHandle() {
        //refresh all data and close form data - reservation additional service
        roomStatusFDController.setSelectedDataToInputForm();
        roomStatusFDController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCheckInAddCardUsedNumber() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtAdditionalCardUsedNumber.getText() == null
                || txtAdditionalCardUsedNumber.getText().equals("")
                || txtAdditionalCardUsedNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Tambahan Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomStatusFDController.additionalCardUsedNumber.get() < 1) {
                dataInput = false;
                errDataInput += "Tambahan Kartu : Tidak dapat kurang dari '1' \n";
            }
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
        initFormDataCheckInAddCardUsedNumber();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationCheckInAddCardUsedInputController(RoomStatusFDController parentController) {
        roomStatusFDController = parentController;
    }

    private final RoomStatusFDController roomStatusFDController;

}
