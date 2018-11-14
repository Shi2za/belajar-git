/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.partner_payment_x;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblPartnerBankAccount;
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
public class PPDPartnerBankAccountInputController implements Initializable {

//    @FXML
//    private AnchorPane ancFormPartnerBankAccount;
//
//    @FXML
//    private GridPane gpFormDataPartnerBankAccount;
//
//    @FXML
//    private JFXTextField txtCodePartnerBankAccount;
//
//    @FXML
//    private JFXTextField txtBankAccountHolderName;
//
//    private JFXCComboBoxTablePopup<TblBank> cbpBank;
//
//    @FXML
//    private JFXButton btnSavePartnerBankAccount;
//
//    @FXML
//    private JFXButton btnCancelPartnerBankAccount;
//
//    private void initFormDataPartnerBankAccount() {
//        initDataPartnerBankAccountPopup();
//
//        btnSavePartnerBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening)"));
//        btnSavePartnerBankAccount.setOnAction((e) -> {
//            dataPartnerBankAccountSaveHandle();
//        });
//
//        btnCancelPartnerBankAccount.setTooltip(new Tooltip("Batal"));
//        btnCancelPartnerBankAccount.setOnAction((e) -> {
//            dataPartnerBankAccountCancelHandle();
//        });
//
//        initImportantFieldColor();
//
//    }
//
//    private void initImportantFieldColor() {
//        ClassViewSetting.setImportantField(
//                txtCodePartnerBankAccount,
//                cbpBank,
//                txtBankAccountHolderName);
//    }
//
//    private void initDataPartnerBankAccountPopup() {
//        //Bank
//        TableView<TblBank> tableBank = new TableView<>();
//
//        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
//        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
//        bankName.setMinWidth(140);
//
//        tableBank.getColumns().addAll(bankName);
//
//        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(partnerPaymentDetailController.getParentController().getService().getAllDataBank());
//        
//        cbpBank = new JFXCComboBoxTablePopup<>(
//                TblBank.class, tableBank, bankItems, "", "Bank *", true, 200, 200
//        );
//
//        //attached to grid-pane
//        gpFormDataPartnerBankAccount.add(cbpBank, 2, 1);
//    }
//
//    private void refreshDataPopup() {
//        //Bank
//        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(partnerPaymentDetailController.getParentController().getService().getAllDataBank());
//        cbpBank.setItems(bankItems);
//    }
//
//    private void setSelectedDataToInputForm() {
//
//        refreshDataPopup();
//
//        txtCodePartnerBankAccount.textProperty().bindBidirectional(partnerPaymentDetailController.selectedDataPartnerBankAccount.getTblBankAccount().codeBankAccountProperty());
//        txtBankAccountHolderName.textProperty().bindBidirectional(partnerPaymentDetailController.selectedDataPartnerBankAccount.getTblBankAccount().bankAccountHolderNameProperty());
//
//        cbpBank.valueProperty().bindBidirectional(partnerPaymentDetailController.selectedDataPartnerBankAccount.getTblBankAccount().tblBankProperty());
//
//        cbpBank.hide();
//    }
//
//    private void dataPartnerBankAccountSaveHandle() {
//        if (checkDataInputDataPartnerBankAccount()) {
//            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", partnerPaymentDetailController.dialogStage);
//            if (alert.getResult() == ButtonType.OK) {
//                //dummy entry
//                TblPartnerBankAccount dummySelectedData = new TblPartnerBankAccount(partnerPaymentDetailController.selectedDataPartnerBankAccount);
//                dummySelectedData.setTblPartner(dummySelectedData.getTblPartner());
//                dummySelectedData.setTblBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccount()));
//                if (partnerPaymentDetailController.getParentController().getService().insertDataPartnerBankAccount(dummySelectedData) != null) {
//                    ClassMessage.showSucceedInsertingDataMessage("", partnerPaymentDetailController.dialogStage);
//                    //refresh data partner - bank account popup
//                    partnerPaymentDetailController.refreshPartnerBankAccountDataPopup();
//                    //reload and set data on field
//                    partnerPaymentDetailController.selectedDataPartnerBankAccount = partnerPaymentDetailController.getParentController().getService().getPartnerBankAccount(dummySelectedData.getIdrelation());
//                    partnerPaymentDetailController.selectedDataPartnerBankAccount.setTblPartner(partnerPaymentDetailController.getParentController().getService().getDataPartner(partnerPaymentDetailController.selectedDataPartnerBankAccount.getTblPartner().getIdpartner()));
//                    partnerPaymentDetailController.selectedDataPartnerBankAccount.setTblBankAccount(partnerPaymentDetailController.getParentController().getService().getBankAccount(partnerPaymentDetailController.selectedDataPartnerBankAccount.getTblBankAccount().getIdbankAccount()));
//                    partnerPaymentDetailController.getParentController().selectedData.setTblBankAccountBySenderBankAccount(partnerPaymentDetailController.selectedDataPartnerBankAccount.getTblBankAccount());
//                    //close form data partner bank account
//                    partnerPaymentDetailController.dialogStage.close();
//                } else {
//                    ClassMessage.showFailedInsertingDataMessage(partnerPaymentDetailController.getParentController().getService().getErrorMessage(), partnerPaymentDetailController.dialogStage);
//                }
//            }
//        } else {
//            ClassMessage.showWarningInputDataMessage(errDataInput, partnerPaymentDetailController.dialogStage);
//        }
//    }
//
//    private void dataPartnerBankAccountCancelHandle() {
//        //close form data partner - bank account
//        partnerPaymentDetailController.dialogStage.close();
//    }
//
//    private String errDataInput;
//
//    private boolean checkDataInputDataPartnerBankAccount() {
//        boolean dataInput = true;
//        errDataInput = "";
//        if (txtCodePartnerBankAccount.getText() == null || txtCodePartnerBankAccount.getText().equals("")) {
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
//        initFormDataPartnerBankAccount();
//        //refresh data form input
//        setSelectedDataToInputForm();
    }

    public PPDPartnerBankAccountInputController(PartnerPaymentDetailController parentController) {
        partnerPaymentDetailController = parentController;
    }

    private final PartnerPaymentDetailController partnerPaymentDetailController;

}
