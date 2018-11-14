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
public class DepositInputController implements Initializable {

    @FXML
    private AnchorPane ancFormDeposit;

    @FXML
    private GridPane gpFormDataDeposit;

    @FXML
    private JFXTextField txtCodeCustomer;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtDeposit;

    @FXML
    private JFXCheckBox chbGetDepositNeeded;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDeposit() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Deposit)"));
        btnSave.setOnAction((e) -> {
            dataDepositSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDepositCancelHandle();
        });

        //deposit needed
        chbGetDepositNeeded.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set deposit needed
                reservationController.selectedDataTransaction.setUnitNominal(reservationController.calculationTotalDepositNeeded());
            }
        });

        reservationController.selectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal
                    .compareTo(reservationController.calculationTotalDepositNeeded()) != 0) {
                chbGetDepositNeeded.setSelected(false);
            }
        });

        initNumbericField();
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtDeposit);
    }
    
    private void setSelectedDataToInputForm() {

        txtCodeCustomer.textProperty().bindBidirectional(reservationController.selectedDataCustomer.codeCustomerProperty());
        txtFullName.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().fullNameProperty());

        Bindings.bindBidirectional(txtDeposit.textProperty(), reservationController.selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataDepositSaveHandle() {
        if (checkDataInputDataDeposit()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data transaction (deposit)
                reservationController.selectedDataTransaction.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationController.selectedDataTransaction.setTblEmployeeByIdcashier(ClassSession.currentUser.getTblEmployeeByIdemployee());
                reservationController.selectedDataTransaction.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(2));    //Deposit = '2'
                //data customer (deposit)
                reservationController.selectedDataCustomer.setDeposit(reservationController.selectedDataCustomer.getDeposit()
                        .add(reservationController.selectedDataTransaction.getUnitNominal()));
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

    private void dataDepositCancelHandle() {
        //close form data open deposit input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDeposit() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtDeposit.getText() == null 
                || txtDeposit.getText().equals("")
                || txtDeposit.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Deposit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedDataTransaction.getUnitNominal().compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Deposit : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
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
        initFormDataDeposit();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public DepositInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
