/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
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
 * @author ANDRI
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
        txtTotalAdditionalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(reservationController.additionalCardUsedNumber.get()))
                .multiply(reservationController.selectedDataCheckIn.getBrokenCardCharge())));
        reservationController.additionalCardUsedNumber.addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalAdditionalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(reservationController.selectedDataCheckIn.getBrokenCardCharge())));
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
    
    private void setSelectedDataToInputForm() {

        txtRoomName.textProperty().bind(reservationController.selectedDataCheckIn.getTblRoom().roomNameProperty());

        Bindings.bindBidirectional(txtAdditionalCardUsedNumber.textProperty(), reservationController.additionalCardUsedNumber, new NumberStringConverter());
        Bindings.bindBidirectional(txtCardBrokenCharge.textProperty(), reservationController.selectedDataCheckIn.brokenCardChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataCheckInAddCardUsedNumberSaveHandle() {
        if (checkDataInputDataCheckInAddCardUsedNumber()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
            //save and set data checkin
//            reservationController.selectedDataCheckIn.setCheckInDateTime(Date.valueOf(LocalDate.now()));

                //update data bill
                reservationController.refreshDataBill(reservationController.selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        ? "reservation" : "checkout");
                //save data to database
                if(reservationController.dataReservationSaveHandle(13)){
                    //close form data checkin input
                    reservationController.dialogStage.close();
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCheckInAddCardUsedNumberCancelHandle() {
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data checkin
        reservationController.dialogStage.close();
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
            if (reservationController.additionalCardUsedNumber.get() < 1) {
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

    public ReservationCheckInAddCardUsedInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
