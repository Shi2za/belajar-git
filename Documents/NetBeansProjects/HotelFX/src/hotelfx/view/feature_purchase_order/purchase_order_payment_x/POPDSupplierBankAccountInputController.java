/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payment_x;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblSupplierBankAccount;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class POPDSupplierBankAccountInputController implements Initializable {

//    @FXML
//    private AnchorPane ancFormSupplierBankAccount;
//
//    @FXML
//    private GridPane gpFormDataSupplierBankAccount;
//
//    @FXML
//    private JFXTextField txtCodeSupplierBankAccount;
//
//    @FXML
//    private JFXTextField txtBankAccountHolderName;
//
//    private JFXCComboBoxTablePopup<TblBank> cbpBank;
//
//    @FXML
//    private JFXButton btnSaveSupplierBankAccount;
//
//    @FXML
//    private JFXButton btnCancelSupplierBankAccount;
//
//    private void initFormDataSupplierBankAccount() {
//        initDataSupplierBankAccountPopup();
//
//        btnSaveSupplierBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening)"));
//        btnSaveSupplierBankAccount.setOnAction((e) -> {
//            dataSupplierBankAccountSaveHandle();
//        });
//
//        btnCancelSupplierBankAccount.setTooltip(new Tooltip("Batal"));
//        btnCancelSupplierBankAccount.setOnAction((e) -> {
//            dataSupplierBankAccountCancelHandle();
//        });
//
//        initImportantFieldColor();
//
//    }
//
//    private void initImportantFieldColor() {
//        ClassViewSetting.setImportantField(
//                txtCodeSupplierBankAccount,
//                cbpBank,
//                txtBankAccountHolderName);
//    }
//
//    private void initDataSupplierBankAccountPopup() {
//        //Bank
//        TableView<TblBank> tableBank = new TableView<>();
//
//        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
//        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
//        bankName.setMinWidth(140);
//
//        tableBank.getColumns().addAll(bankName);
//
//        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(purchaseOrderPaymentDetailController.getParentController().getService().getAllDataBank());
//
//        cbpBank = new JFXCComboBoxTablePopup<>(
//                TblBank.class, tableBank, bankItems, "", "Bank *", true, 200, 200
//        );
//        
//        //attached to grid-pane
//        gpFormDataSupplierBankAccount.add(cbpBank, 2, 1);
//    }
//
//    private void refreshDataPopup() {
//        //Bank
//        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(purchaseOrderPaymentDetailController.getParentController().getService().getAllDataBank());
//        cbpBank.setItems(bankItems);
//    }
//
//    private void setSelectedDataToInputForm() {
//
//        refreshDataPopup();
//
//        txtCodeSupplierBankAccount.textProperty().bindBidirectional(purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().codeBankAccountProperty());
//        txtBankAccountHolderName.textProperty().bindBidirectional(purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().bankAccountHolderNameProperty());
//
//        cbpBank.valueProperty().bindBidirectional(purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().tblBankProperty());
//
//        cbpBank.hide();
//    }
//
//    private void dataSupplierBankAccountSaveHandle() {
//        if (checkDataInputDataSupplierBankAccount()) {
//            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderPaymentDetailController.dialogStage);
//            if (alert.getResult() == ButtonType.OK) {
//                //dummy entry
//                TblSupplierBankAccount dummySelectedData = new TblSupplierBankAccount(purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount);
//                dummySelectedData.setTblSupplier(dummySelectedData.getTblSupplier());
//                dummySelectedData.setTblBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccount()));
//                if (purchaseOrderPaymentDetailController.getParentController().getService().insertDataSupplierBankAccount(dummySelectedData) != null) {
//                    ClassMessage.showSucceedInsertingDataMessage("", purchaseOrderPaymentDetailController.dialogStage);
//                    //refresh data supplier - bank account popup
//                    purchaseOrderPaymentDetailController.refreshSupplierBankAccountDataPopup();
//                    //reload and set data on field
//                    purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount = purchaseOrderPaymentDetailController.getParentController().getService().getDataSupplierBankAccount(dummySelectedData.getIdrelation());
//                    purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.setTblSupplier(purchaseOrderPaymentDetailController.getParentController().getService().getDataSupplier(purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.getTblSupplier().getIdsupplier()));
//                    purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.setTblBankAccount(purchaseOrderPaymentDetailController.getParentController().getService().getBankAccount(purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().getIdbankAccount()));
//                    purchaseOrderPaymentDetailController.getParentController().selectedData.setTblBankAccountByReceiverBankAccount(purchaseOrderPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount());
//                    //close form data supplier bank account
//                    purchaseOrderPaymentDetailController.dialogStage.close();
//                } else {
//                    ClassMessage.showFailedInsertingDataMessage(purchaseOrderPaymentDetailController.getParentController().getService().getErrorMessage(), purchaseOrderPaymentDetailController.dialogStage);
//                }
//            }
//        } else {
//            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPaymentDetailController.dialogStage);
//        }
//    }
//
//    private void dataSupplierBankAccountCancelHandle() {
//        //close form data supplier - bank account
//        purchaseOrderPaymentDetailController.dialogStage.close();
//    }
//
//    private String errDataInput;
//
//    private boolean checkDataInputDataSupplierBankAccount() {
//        boolean dataInput = true;
//        errDataInput = "";
//        if (txtCodeSupplierBankAccount.getText() == null || txtCodeSupplierBankAccount.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Nomor Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (cbpBank.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (txtBankAccountHolderName.getText() == null || txtBankAccountHolderName.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Nama Pemegang Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
////        if (cbpBankAccountStatus.getValue() == null) {
////            dataInput = false;
////            errDataInput += "Bank Account (Status) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
////        }
//        return dataInput;
//    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        //init form input
//        initFormDataSupplierBankAccount();
//        //refresh data form input
//        setSelectedDataToInputForm();
    }

    public POPDSupplierBankAccountInputController(PurchaseOrderPaymentDetailController parentController) {
        purchaseOrderPaymentDetailController = parentController;
    }

    private final PurchaseOrderPaymentDetailController purchaseOrderPaymentDetailController;

}
