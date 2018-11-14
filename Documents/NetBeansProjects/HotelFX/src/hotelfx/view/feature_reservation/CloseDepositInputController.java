/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
public class CloseDepositInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCloseDeposit;

    @FXML
    private GridPane gpFormDataCloseDeposit;

    @FXML
    private JFXTextField txtCodeCustomer;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtCloseDeposit;

    @FXML
    private JFXCheckBox chbGetMaxDeposit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCloseDeposit() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Tutup Deposit)"));
        btnSave.setOnAction((e) -> {
            dataCloseDepositSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCloseDepositCancelHandle();
        });

        //all deposit
        chbGetMaxDeposit.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set all deposit customer haved
                reservationController.selectedDataTransaction.setUnitNominal(reservationController.selectedDataCustomer.getDeposit());
            }
        });

        reservationController.selectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.compareTo(reservationController.selectedDataCustomer.getDeposit()) != 0) {
                chbGetMaxDeposit.setSelected(false);
            }
        });

        initNumbericField();
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtCloseDeposit);
    }
    
    private void setSelectedDataToInputForm() {

        txtCodeCustomer.textProperty().bindBidirectional(reservationController.selectedDataCustomer.codeCustomerProperty());
        txtFullName.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().fullNameProperty());

        Bindings.bindBidirectional(txtCloseDeposit.textProperty(), reservationController.selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataCloseDepositSaveHandle() {
        if (checkDataInputDataCloseDeposit()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data transaction (deposit)
                reservationController.selectedDataTransaction.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationController.selectedDataTransaction.setTblEmployeeByIdcashier(ClassSession.currentUser.getTblEmployeeByIdemployee());
                reservationController.selectedDataTransaction.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(2));    //Deposit = '2'
                //data customer (deposit)
                reservationController.selectedDataCustomer.setDeposit(reservationController.selectedDataCustomer.getDeposit()
                        .subtract(reservationController.selectedDataTransaction.getUnitNominal()));
                if (reservationController.getFReservationManager().updateDataCustomerDeposit(reservationController.selectedDataCustomer,
                        reservationController.selectedDataTransaction)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", reservationController.dialogStage);
                    //refresh data customer popup
                    reservationController.refreshDataPopup();
                    //set data on field
                    reservationController.selectedData.setTblCustomer(reservationController.selectedDataCustomer);
                    //close form data deposit input
                    reservationController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(reservationController.getFReservationManager().getErrorMessage(), reservationController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCloseDepositCancelHandle() {
        //close form data close deposit input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCloseDeposit() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCloseDeposit.getText() == null 
                || txtCloseDeposit.getText().equals("")
                || txtCloseDeposit.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Deposit (Tutup) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedDataTransaction.getUnitNominal().compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Deposit (Tutup) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (reservationController.selectedDataTransaction.getUnitNominal()
                        .compareTo(reservationController.selectedDataCustomer.getDeposit()) == 1) {
                    dataInput = false;
                    errDataInput += "Deposit (Tutup) : Nilai tidak dapat lebih besar dari '" + reservationController.selectedDataCustomer.getDeposit() + "' \n";
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
        initFormDataCloseDeposit();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public CloseDepositInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
