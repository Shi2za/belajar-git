/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rcr.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.view.feature_reservation.rcr.RCRRCTransactionInputController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCRRCTIDepositController implements Initializable {

    @FXML
    private JFXTextField txtTotalDeposit;

    @FXML
    private JFXTextField txtNominalDeposit;

    @FXML
    private JFXButton btnSave;

    private void initFormDataRCTIDeposit() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataRCTIDepositSaveHandle();
        });

        initNumbericField();
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtNominalDeposit);
    }
    
    private void setSelectedDataToInputForm() {
        txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat(rcrrcTransactionInputController.getParentController().getParentController().selectedData.getTblCustomer().getDeposit()));
        Bindings.bindBidirectional(txtNominalDeposit.textProperty(), rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
    }

    private void dataRCTIDepositSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            System.out.println("Function unsupported yet..!");
//            //save data transaction
//            rcrrcTransactionInputController.saveDataDiscountAndTransaction();
//            //update data deposit customer
//            if (rcrrcTransactionInputController.getParentController().selectedDataTransaction.getRefFinanceTransactionPaymentType().getTypeName().equals("Open Deposit (Cash)")) {   //plus
//                rcrrcTransactionInputController.getParentController().selectedData.getTblCustomer().setDeposit(
//                        rcrrcTransactionInputController.getParentController().selectedData.getTblCustomer().getDeposit()
//                        + rcrrcTransactionInputController.getParentController().selectedDataTransaction.getUnitNominal());
//            } else {  //minus
//                rcrrcTransactionInputController.getParentController().selectedData.getTblCustomer().setDeposit(
//                        rcrrcTransactionInputController.getParentController().selectedData.getTblCustomer().getDeposit()
//                        - rcrrcTransactionInputController.getParentController().selectedDataTransaction.getUnitNominal());
//            }
//            //do next aciton
//            rcrrcTransactionInputController.dataRCTTransactionSaveHandle();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcrrcTransactionInputController.getParentController().dialogStage);
        }
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationPayment() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtNominalDeposit.getText() == null 
                || txtNominalDeposit.getText().equals("")
                || txtNominalDeposit.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()
                        .compareTo((rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                                .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Nilai tidak dapat lebih besar dari '" + (rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                            .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
                } else {
                    if (rcrrcTransactionInputController.getParentController().getParentController().selectedData.getTblCustomer().getDeposit()
                            .compareTo(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()) == -1) {
                        dataInput = false;
                        errDataInput += "Nominal Transaksi : Nilai tidak dapat lebih besar dari '" + rcrrcTransactionInputController.getParentController().getParentController().selectedData.getTblCustomer().getDeposit() + "' \n";
                    }
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
        initFormDataRCTIDeposit();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCRRCTIDepositController(RCRRCTransactionInputController parentController) {
        rcrrcTransactionInputController = parentController;
    }

    private final RCRRCTransactionInputController rcrrcTransactionInputController;

}
