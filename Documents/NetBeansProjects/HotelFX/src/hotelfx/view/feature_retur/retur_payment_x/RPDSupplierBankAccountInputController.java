/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_payment_x;

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
public class RPDSupplierBankAccountInputController implements Initializable {

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
//        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(returPaymentDetailController.getParentController().getService().getAllDataBank());
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
//        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(returPaymentDetailController.getParentController().getService().getAllDataBank());
//        cbpBank.setItems(bankItems);
//    }
//
//    private void setSelectedDataToInputForm() {
//
//        refreshDataPopup();
//
//        txtCodeSupplierBankAccount.textProperty().bindBidirectional(returPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().codeBankAccountProperty());
//        txtBankAccountHolderName.textProperty().bindBidirectional(returPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().bankAccountHolderNameProperty());
//
//        cbpBank.valueProperty().bindBidirectional(returPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().tblBankProperty());
//
//        cbpBank.hide();
//    }
//
//    private void dataSupplierBankAccountSaveHandle() {
//        if (checkDataInputDataSupplierBankAccount()) {
//            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", returPaymentDetailController.dialogStage);
//            if (alert.getResult() == ButtonType.OK) {
//                //dummy entry
//                TblSupplierBankAccount dummySelectedData = new TblSupplierBankAccount(returPaymentDetailController.selectedDataSupplierBankAccount);
//                dummySelectedData.setTblSupplier(dummySelectedData.getTblSupplier());
//                dummySelectedData.setTblBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccount()));
//                if (returPaymentDetailController.getParentController().getService().insertDataSupplierBankAccount(dummySelectedData) != null) {
//                    ClassMessage.showSucceedInsertingDataMessage("", returPaymentDetailController.dialogStage);
//                    //refresh data supplier - bank account popup
//                    returPaymentDetailController.refreshSupplierBankAccountDataPopup();
//                    //reload and set data on field
//                    returPaymentDetailController.selectedDataSupplierBankAccount = returPaymentDetailController.getParentController().getService().getDataSupplierBankAccount(dummySelectedData.getIdrelation());
//                    returPaymentDetailController.selectedDataSupplierBankAccount.setTblSupplier(returPaymentDetailController.getParentController().getService().getDataSupplier(returPaymentDetailController.selectedDataSupplierBankAccount.getTblSupplier().getIdsupplier()));
//                    returPaymentDetailController.selectedDataSupplierBankAccount.setTblBankAccount(returPaymentDetailController.getParentController().getService().getBankAccount(returPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount().getIdbankAccount()));
//                    returPaymentDetailController.getParentController().selectedData.setTblBankAccountBySenderBankAccount(returPaymentDetailController.selectedDataSupplierBankAccount.getTblBankAccount());
//                    //close form data supplier bank account
//                    returPaymentDetailController.dialogStage.close();
//                } else {
//                    ClassMessage.showFailedInsertingDataMessage(returPaymentDetailController.getParentController().getService().getErrorMessage(), returPaymentDetailController.dialogStage);
//                }
//            }
//        } else {
//            ClassMessage.showWarningInputDataMessage(errDataInput, returPaymentDetailController.dialogStage);
//        }
//    }
//
//    private void dataSupplierBankAccountCancelHandle() {
//        //close form data supplier - bank account
//        returPaymentDetailController.dialogStage.close();
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

    public RPDSupplierBankAccountInputController(ReturPaymentDetailController parentController) {
        returPaymentDetailController = parentController;
    }

    private final ReturPaymentDetailController returPaymentDetailController;

}
