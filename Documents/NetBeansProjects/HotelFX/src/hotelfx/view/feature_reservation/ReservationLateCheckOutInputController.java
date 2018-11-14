/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationDefaultBar;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
public class ReservationLateCheckOutInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCheckOut;

    @FXML
    private GridPane gpFormDataCheckOut;

    @FXML
    private GridPane gpFormDataLateCheckOut;
    
    @FXML
    private JFXTextField txtCheckOutDateTimeDefault;

    @FXML
    private JFXTextField txtCheckOutDateTimeNow;

    @FXML
    private JFXTextField txtTimeDistance;

    @FXML
    private JFXTextField txtAdditionalCharge;
    
    @FXML
    private JFXCheckBox chbGetFullRoomTypeDetailCost;

    @FXML
    private JFXTextArea txtAdditionalNote;

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

    private void initFormDataLateCheckOut() {

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
                txtCardReturnNumber, 
                txtAdditionalCharge);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAdditionalCharge,
                txtCardReturnNumber,
                txtCardBrokenCharge);
        ClassFormatter.setToNumericField(
                "Integer",
                txtCardUsedNumber,
                txtCardBrokenNumber);
    }
    
    private final IntegerProperty cardReturnNumber = new SimpleIntegerProperty();
    
    private void setSelectedDataToInputForm() {
        //data late checkout
        txtCheckOutDateTimeDefault.setText(ClassFormatter.dateFormate.format(reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime())
                + " " + (ClassFormatter.timeFormate.format(reservationController.defaultCheckOutTime)));
        txtCheckOutDateTimeNow.setText(ClassFormatter.dateTimeFormate.format(Timestamp.valueOf(LocalDateTime.now())));
        txtTimeDistance.setText(ClassFormatter.decimalFormat.cFormat(calculationTimeDistance()) + " Jam");
        Bindings.bindBidirectional(txtAdditionalCharge.textProperty(), reservationController.selectedAdditionalService.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtAdditionalNote.textProperty().bindBidirectional(reservationController.selectedAdditionalService.noteProperty());
        
        chbGetFullRoomTypeDetailCost.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                 reservationController.selectedAdditionalService.setPrice(getFullRoomTypeDetailCost());
            }
        });
        chbGetFullRoomTypeDetailCost.setSelected(false);
        
        reservationController.selectedAdditionalService.priceProperty().addListener((obs, oldVal, newVal) -> {
            if(chbGetFullRoomTypeDetailCost.isSelected()
                    && (newVal.compareTo(getFullRoomTypeDetailCost()) != 0)){
                chbGetFullRoomTypeDetailCost.setSelected(false);
            }
        });

        //data checkout
        cardReturnNumber.set(reservationController.selectedDataCheckOut.getCardUsed());
        Bindings.bindBidirectional(txtCardReturnNumber.textProperty(), cardReturnNumber, new NumberStringConverter());
        
        txtRoomName.textProperty().bindBidirectional(reservationController.selectedDataCheckOut.getTblRoom().roomNameProperty());
        Bindings.bindBidirectional(txtCardUsedNumber.textProperty(), reservationController.selectedDataCheckOut.cardUsedProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCardBrokenCharge.textProperty(), reservationController.selectedDataCheckOut.brokenCardChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtCardBrokenNumber.textProperty(), reservationController.selectedDataCheckOut.cardMissedProperty(), new NumberStringConverter());
    }

    private double calculationTimeDistance() {
        double result = ChronoUnit.HOURS.between(LocalDateTime.now(), LocalDateTime.of(
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate(),
                reservationController.defaultCheckOutTime.getHours(), reservationController.defaultCheckOutTime.getMinutes()));
        return result < 0 ? result * (-1) : result;
    }

    //room type - date (after = 'checkout')
    private BigDecimal getFullRoomTypeDetailCost() {
        TblReservationBar dataBAR;
        TblReservationCalendarBar calendarBAR = reservationController.getFReservationManager().getReservationCalendarBARByCalendarDate(Date.valueOf((LocalDate.of(
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate()))));
        if (calendarBAR == null) {
            TblReservationDefaultBar defaultBAR = reservationController.getFReservationManager().getReservationDefaultBARByDayOfWeek(((LocalDate.of(
                    reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                    reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                    reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate())))
                    .getDayOfWeek().getValue());
            dataBAR = reservationController.getFReservationManager().getReservationBAR(defaultBAR.getTblReservationBar().getIdbar());
        } else {
            dataBAR = reservationController.getFReservationManager().getReservationBAR(calendarBAR.getTblReservationBar().getIdbar());
        }
        return reservationController.selectedDataRoomTypeDetail.getTblRoomType().getRoomTypePrice().multiply(dataBAR.getBarpercentage());
    }
    
    private void dataCheckOutSaveHandle() {
        if (checkDataInputDataCheckOut()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data additional service (late checkin)
                reservationController.selectedAdditionalService.setAdditionalDate(Date.valueOf(LocalDate.now()));
                if (reservationController.selectedData.getRefReservationStatus().getIdstatus() == 1) {  //Reserved = '1'
                    reservationController.selectedAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));  //Reservation = '0'
                } else {  //checkout (Booked)
                    reservationController.selectedAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(1)); //CheckOut = '1'
                }

                //data checkout
                reservationController.selectedDataCheckOut.setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.now()));
                reservationController.selectedDataCheckOut.setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(3));

                //update data bill
                reservationController.refreshDataBill(reservationController.selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        ? "reservation" : "checkout");
                //save data to database
                if(reservationController.dataReservationSaveHandle(12)){
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
        if (txtAdditionalCharge.getText() == null 
                || txtAdditionalCharge.getText().equals("")
                || txtAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Tambahan Biaya : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedAdditionalService.getPrice().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Tambahan Biaya : Tidak dapat kurang dari '0' \n";
            }
        }
        if (txtCardReturnNumber.getText() == null 
                || txtCardReturnNumber.getText().equals("")
                || txtCardReturnNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Kartu (Dikembalikan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (cardReturnNumber.get() < 0) {
                dataInput = false;
                errDataInput += "Jumlah Kartu (Dikembalikan) : Nilai tidak dapat kurang dari '0' \n";
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
        initFormDataLateCheckOut();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationLateCheckOutInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
