/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.view.feature_reservation.RCTransactionInputDetailController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;

/**
 *
 * @author ANDRI
 */
public class RCTIDepositController implements Initializable {
    
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
                txtTotalDeposit);
    }
    
    private void setSelectedDataToInputForm() {
        txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat(rcTransactionInputDetailController.getParentController().getParentController().selectedData.getTblCustomer().getDeposit()));
        Bindings.bindBidirectional(txtNominalDeposit.textProperty(), rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
    }
    
    private void dataRCTIDepositSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            System.out.println("Function unsupported yet..!");
//            //save data transaction
//            rcTransactionInputDetailController.saveDataDiscountAndTransaction();
//            //update data deposit customer
//            if (rcTransactionInputDetailController.getParentController().selectedDataTransaction.getRefFinanceTransactionPaymentType().getTypeName().equals("Open Deposit (Cash)")) {   //plus
//                rcTransactionInputDetailController.getParentController().selectedData.getTblCustomer().setDeposit(
//                        rcTransactionInputDetailController.getParentController().selectedData.getTblCustomer().getDeposit()
//                        + rcTransactionInputDetailController.getParentController().selectedDataTransaction.getUnitNominal());
//            } else {  //minus
//                rcTransactionInputDetailController.getParentController().selectedData.getTblCustomer().setDeposit(
//                        rcTransactionInputDetailController.getParentController().selectedData.getTblCustomer().getDeposit()
//                        - rcTransactionInputDetailController.getParentController().selectedDataTransaction.getUnitNominal());
//            }
//            //do next aciton
//            rcTransactionInputDetailController.dataRCTTransactionSaveHandle();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcTransactionInputDetailController.getParentController().dialogStage);
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
            if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                        .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(
                                        rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
                                .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Nilai tidak dapat lebih besar dari '" + (rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(
                            rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
                } else {
                    if (rcTransactionInputDetailController.getParentController().getParentController().selectedData.getTblCustomer().getDeposit()
                            .compareTo(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()) == -1) {
                        dataInput = false;
                        errDataInput += "Nominal Transaksi : Nilai tidak dapat lebih besar dari '" + rcTransactionInputDetailController.getParentController().getParentController().selectedData.getTblCustomer().getDeposit() + "' \n";
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
    
    public RCTIDepositController(RCTransactionInputDetailController parentController) {
        rcTransactionInputDetailController = parentController;
    }
    
    private final RCTransactionInputDetailController rcTransactionInputDetailController;
    
}
