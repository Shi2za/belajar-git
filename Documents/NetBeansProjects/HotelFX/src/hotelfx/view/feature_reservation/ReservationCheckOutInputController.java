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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
public class ReservationCheckOutInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCheckOut;

    @FXML
    private GridPane gpFormDataCheckOut;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtCardUsedNumber;
    
    @FXML
    private JFXTextField txtTotalDeposit;

    @FXML
    private JFXTextField txtCardReturnNumber;
    
    @FXML
    private JFXTextField txtCardBrokenNumber;
    
    @FXML
    private JFXTextField txtCardBrokenCharge;

    @FXML
    private JFXTextField txtTotalCharge;

    @FXML
    private JFXTextField txtTotalDepositMustBeReturn;
    
    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCheckOut() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Check Out)"));
        btnSave.setOnAction((e) -> {
            dataCheckOutSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCheckOutCancelHandle();
        });

        //total deposit
        txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(reservationController.selectedDataCheckOut.getCardUsed()))
                .multiply(reservationController.selectedDataCheckOut.getBrokenCardCharge())));

        //total charge
        txtTotalCharge.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(reservationController.selectedDataCheckOut.getCardMissed()))
                .multiply(reservationController.selectedDataCheckOut.getBrokenCardCharge())));
        reservationController.selectedDataCheckOut.cardMissedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalCharge.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(reservationController.selectedDataCheckOut.getBrokenCardCharge())));
            } else {
                txtTotalCharge.setText(ClassFormatter.currencyFormat.cFormat(0));
            }
        });
        
        //total deposit must be return
        txtTotalDepositMustBeReturn.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(cardReturnNumber.get()))
                .multiply(reservationController.selectedDataCheckOut.getBrokenCardCharge())));
        cardReturnNumber.addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalDepositMustBeReturn.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(reservationController.selectedDataCheckOut.getBrokenCardCharge())));
                reservationController.selectedDataCheckOut.setCardMissed(reservationController.selectedDataCheckOut.getCardUsed() - newValue.intValue());
            } else {
                txtTotalDepositMustBeReturn.setText(ClassFormatter.currencyFormat.cFormat(0));
                reservationController.selectedDataCheckOut.setCardMissed(reservationController.selectedDataCheckOut.getCardUsed());
            }
        });
        
        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCardReturnNumber);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtCardBrokenCharge);
        ClassFormatter.setToNumericField(
                "Integer",
                txtCardUsedNumber,
                txtCardReturnNumber,
                txtCardBrokenNumber);
    }
    
    private final IntegerProperty cardReturnNumber = new SimpleIntegerProperty();
    
    private void setSelectedDataToInputForm() {
        cardReturnNumber.set(reservationController.selectedDataCheckOut.getCardUsed());
        Bindings.bindBidirectional(txtCardReturnNumber.textProperty(), cardReturnNumber, new NumberStringConverter());
        
        txtRoomName.textProperty().bindBidirectional(reservationController.selectedDataCheckOut.getTblRoom().roomNameProperty());

        Bindings.bindBidirectional(txtCardUsedNumber.textProperty(), reservationController.selectedDataCheckOut.cardUsedProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCardBrokenCharge.textProperty(), reservationController.selectedDataCheckOut.brokenCardChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtCardBrokenNumber.textProperty(), reservationController.selectedDataCheckOut.cardMissedProperty(), new NumberStringConverter());

    }

    private void dataCheckOutSaveHandle() {
        if (checkDataInputDataCheckOut()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //save and set data checkout
                reservationController.selectedDataCheckOut.setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.now()));
                reservationController.selectedDataCheckOut.setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(3));

                //update data bill
                reservationController.refreshDataBill(reservationController.selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        ? "reservation" : "checkout");
                //save data to database
                if(reservationController.dataReservationSaveHandle(6)){
                    //close form data checkout input
                    reservationController.dialogStage.close();
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCheckOutCancelHandle() {
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data checkout
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCheckOut() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCardReturnNumber.getText() == null 
                || txtCardReturnNumber.getText().equals("")
                || txtCardReturnNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Kartu (Dikembalikan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (cardReturnNumber.get() < 0) {
                dataInput = false;
                errDataInput += "Jumlah Kartu (Dikembalikan) : Nilai tidak dapat lebih kurang dari '0' \n";
            } else {
                if (cardReturnNumber.get() > reservationController.selectedDataCheckOut.getCardUsed()) {
                    dataInput = false;
                    errDataInput += "Jumlah Kartu (Dikembalikan) : Nilai tidak dapat lebih besar dari '" + reservationController.selectedDataCheckOut.getCardUsed() + "' \n";
                }
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
        initFormDataCheckOut();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationCheckOutInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
