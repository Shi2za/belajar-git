/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payment_x;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblSupplier;
import java.net.URL;
import java.util.ResourceBundle;
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
public class PurchaseOrderPaymentDetailSetInvoiceController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtCodePO;

    @FXML
    private JFXTextField txtSupplierName;
    
    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private JFXTextField txtCodeInvoice;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Invoice)"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });
        
        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeInvoice);
    }

    private void setSelectedDataToInputForm() {

        txtCodePO.textProperty().bind(purchaseOrderPaymentController.selectedDataPurchaseOrder.codePoProperty());
        txtSupplierName.textProperty().bind(purchaseOrderPaymentController.selectedDataPurchaseOrder.getTblSupplier().supplierNameProperty());

        txtTotalBill.setText(ClassFormatter.currencyFormat.cFormat(purchaseOrderPaymentController.calculationTotalBill(purchaseOrderPaymentController.selectedDataPurchaseOrder)));

        txtCodeInvoice.textProperty().bindBidirectional(purchaseOrderPaymentController.selectedDataHotelInvoice.codeHotelInvoiceProperty());

    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderPaymentController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblHotelInvoice dummySelectedDataHotelInvoice = new TblHotelInvoice(purchaseOrderPaymentController.selectedDataHotelInvoice);
                dummySelectedDataHotelInvoice.setTblSupplier(new TblSupplier(dummySelectedDataHotelInvoice.getTblSupplier()));
                dummySelectedDataHotelInvoice.setRefHotelInvoiceType(new RefHotelInvoiceType(dummySelectedDataHotelInvoice.getRefHotelInvoiceType()));
                TblPurchaseOrder dummySelectedDataPurchaseOrder = new TblPurchaseOrder(purchaseOrderPaymentController.selectedDataPurchaseOrder);
                dummySelectedDataPurchaseOrder.setTblHotelPayable(new TblHotelPayable(dummySelectedDataPurchaseOrder.getTblHotelPayable()));
                dummySelectedDataPurchaseOrder.getTblHotelPayable().setTblHotelInvoice(dummySelectedDataHotelInvoice);
                if (purchaseOrderPaymentController.getService().insertDataHotelInvoice(dummySelectedDataHotelInvoice, 
                        dummySelectedDataPurchaseOrder) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", purchaseOrderPaymentController.dialogStageDetal);
                    //refresh data table PO - Payment
                    purchaseOrderPaymentController.refreshData();
                    //close form data detail
                    purchaseOrderPaymentController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(purchaseOrderPaymentController.getService().getErrorMessage(), purchaseOrderPaymentController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPaymentController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //refresh data table PO - Payment
        purchaseOrderPaymentController.refreshData();
        //close form data detail
        purchaseOrderPaymentController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeInvoice.getText() == null || txtCodeInvoice.getText().equals("")) {
            dataInput = false;
            errDataInput += "No. Invoice : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public PurchaseOrderPaymentDetailSetInvoiceController(PurchaseOrderPaymentController parentController) {
        purchaseOrderPaymentController = parentController;
    }

    private final PurchaseOrderPaymentController purchaseOrderPaymentController;

    public PurchaseOrderPaymentController getParentController() {
        return purchaseOrderPaymentController;
    }
    
}
