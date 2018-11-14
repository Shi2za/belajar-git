/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblSupplier;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
public class PurchaseOrderPaymentInvoiceController implements Initializable {

    @FXML
    private AnchorPane ancFormInvoice;

    @FXML
    private GridPane gpFormDataInvoice;

    @FXML
    private JFXTextField txtCodeInvoice;

    @FXML
    private JFXDatePicker dtpDueDate;

    @FXML
    private JFXTextField txtHotelPayableNominal;

    @FXML
    private JFXTextArea txtInvoiceNote;

    @FXML
    public JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataInvoice() {
        btnSave.setTooltip(new Tooltip("Simpan (Data Invoice)"));
        btnSave.setOnAction((e) -> {
            dataInvoiceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataInvoiceCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeInvoice,
                dtpDueDate,
                txtHotelPayableNominal);
    }

    private final ObjectProperty<BigDecimal> hotelPayableNominal = new SimpleObjectProperty<>(new BigDecimal("0"));

    private void setSelectedDataToInputForm() {
        Bindings.bindBidirectional(txtHotelPayableNominal.textProperty(), hotelPayableNominal, new ClassFormatter.CBigDecimalStringConverter());

        if (purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            hotelPayableNominal.set(purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getHotelPayableNominal());

            txtCodeInvoice.setText(purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice().getCodeHotelInvoice());
            if (purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice().getDueDate() != null) {
                dtpDueDate.setValue(purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice().getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            } else {
                dtpDueDate.setValue(null);
            }
            txtInvoiceNote.setText(purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice().getHotelInvoiceNote());
        } else {
            hotelPayableNominal.set(purchaseOrderPayableController.calculationTotalBill(purchaseOrderPayableController.selectedDataPO));

            txtCodeInvoice.setText("");
            dtpDueDate.setValue(null);
            txtInvoiceNote.setText("");
        }
    }

    private void dataInvoiceSaveHandle() {
        if (checkDataInputInvoice()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblPurchaseOrder dummySelectedDataPO = new TblPurchaseOrder(purchaseOrderPayableController.selectedDataPO);
                dummySelectedDataPO.setTblSupplier(new TblSupplier(dummySelectedDataPO.getTblSupplier()));
                if (dummySelectedDataPO.getTblHotelPayable() != null) {
                    dummySelectedDataPO.setTblHotelPayable(new TblHotelPayable(dummySelectedDataPO.getTblHotelPayable()));
                    dummySelectedDataPO.getTblHotelPayable().setTblHotelInvoice(new TblHotelInvoice(dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice()));
                } else {
                    dummySelectedDataPO.setTblHotelPayable(new TblHotelPayable());
                    dummySelectedDataPO.getTblHotelPayable().setHotelPayableNominal(purchaseOrderPayableController.calculationTotal(dummySelectedDataPO));
                    dummySelectedDataPO.getTblHotelPayable().setRefHotelPayableType(purchaseOrderPayableController.getService().getDataHotelPayableType(1));   //Purchase Order = '1'
                    dummySelectedDataPO.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(0));    //Belum Dibayar = '0'
                    dummySelectedDataPO.getTblHotelPayable().setTblHotelInvoice(new TblHotelInvoice());
                    dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setTblSupplier(purchaseOrderPayableController.getService().getDataSupplier(dummySelectedDataPO.getTblSupplier().getIdsupplier()));
                    dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setRefHotelInvoiceType(purchaseOrderPayableController.getService().getDataHotelInvoiceType(0));    //Payable = '0' 
                }
                //set data from field input
                dummySelectedDataPO.getTblHotelPayable().setHotelPayableNominal(hotelPayableNominal.get());
                dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setCodeHotelInvoice(txtCodeInvoice.getText());
                dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setDueDate(dtpDueDate.getValue() != null
                        ? Date.valueOf(dtpDueDate.getValue())
                        : null);
                dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setHotelInvoiceNote(txtInvoiceNote.getText());
                //Hotel Payable - Status
                if (dummySelectedDataPO.getTblHotelPayable().getIdhotelPayable() != 0L) {
                    List<TblHotelFinanceTransactionHotelPayable> hfthps = purchaseOrderPayableController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(dummySelectedDataPO.getTblHotelPayable().getIdhotelPayable());
                    if (!hfthps.isEmpty()) {
                        BigDecimal totalPayments = new BigDecimal("0");
                        for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
                            if(hfthp.getTblHotelFinanceTransaction().getIsReturnTransaction()){
                                totalPayments = totalPayments.subtract(hfthp.getNominalTransaction());
                            }else{
                                totalPayments = totalPayments.add(hfthp.getNominalTransaction());
                            }
                        }
                        if (dummySelectedDataPO.getTblHotelPayable().getHotelPayableNominal().compareTo(totalPayments) == 0) {
                            dummySelectedDataPO.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(2));    //Sudah Dibayar = '2'
                        } else {
                            if (dummySelectedDataPO.getTblHotelPayable().getHotelPayableNominal().compareTo(totalPayments) == 1) {
                                dummySelectedDataPO.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(1));    //Dibayar Sebagian = '1'
                            } else {
                                if (dummySelectedDataPO.getTblHotelPayable().getHotelPayableNominal().compareTo(totalPayments) == -1) {
                                    dummySelectedDataPO.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(5));    //Kelebihan Bayar = '5'
                                }
                            }
                        }
                    }
                }
                if (purchaseOrderPayableController.getService().insertDataHotelInvoice(dummySelectedDataPO) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", null);
                    //set selected data input form (table-op-hp)
                    purchaseOrderPayableController.refreshDataTableDetail();
                    //refresh data total hotel payable
                    purchaseOrderPayableController.refreshDataTotalHotelPayable();
                    //close form data invoice
                    purchaseOrderPayableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(purchaseOrderPayableController.getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPayableController.dialogStageDetal);
        }
    }

    private void dataInvoiceCancelHandle() {
        //close form data invoice
        purchaseOrderPayableController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputInvoice() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeInvoice.getText() == null
                || txtCodeInvoice.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Invoice : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpDueDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Estimasi Bayar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtHotelPayableNominal.getText() == null
                || txtHotelPayableNominal.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nominal Tagihan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (hotelPayableNominal.get().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Nominal Tagihan : Nilai tidak boleh kurang dari '0' \n";
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
        initFormDataInvoice();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public PurchaseOrderPaymentInvoiceController(PurchaseOrderPayableController parentController) {
        purchaseOrderPayableController = parentController;
    }

    private final PurchaseOrderPayableController purchaseOrderPayableController;

    public PurchaseOrderPayableController getParentController() {
        return purchaseOrderPayableController;
    }

}
