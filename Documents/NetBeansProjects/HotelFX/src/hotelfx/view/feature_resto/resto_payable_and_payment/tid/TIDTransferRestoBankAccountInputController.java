/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_resto.resto_payable_and_payment.tid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblRestoBankAccount;
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
 * @author ABC-Programmer
 */
public class TIDTransferRestoBankAccountInputController implements Initializable {

    @FXML
    private AnchorPane ancFormRestoBankAccount;

    @FXML
    private GridPane gpFormDataRestoBankAccount;

    @FXML
    private JFXTextField txtCodeRestoBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private AnchorPane ancBankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private JFXButton btnSaveRestoBankAccount;

    @FXML
    private JFXButton btnCancelRestoBankAccount;

    private void initFormDataRestoBankAccount() {
        initDataRestoBankAccountPopup();

        btnSaveRestoBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening)"));
        btnSaveRestoBankAccount.setOnAction((e) -> {
            dataRestoBankAccountSaveHandle();
        });

        btnCancelRestoBankAccount.setTooltip(new Tooltip("Batal"));
        btnCancelRestoBankAccount.setOnAction((e) -> {
            dataRestoBankAccountCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeRestoBankAccount,
                cbpBank,
                txtBankAccountHolderName);
    }

    private void initDataRestoBankAccountPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(tidTransferController.getParentController().getParentController().getService().getAllDataBank());

        cbpBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableBank, bankItems, "", "Bank *", true, 200, 200
        );

        //attached to grid-pane
        ancBankLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBank, 0.0);
        AnchorPane.setLeftAnchor(cbpBank, 0.0);
        AnchorPane.setRightAnchor(cbpBank, 0.0);
        AnchorPane.setTopAnchor(cbpBank, 0.0);
        ancBankLayout.getChildren().add(cbpBank);
    }

    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(tidTransferController.getParentController().getParentController().getService().getAllDataBank());
        cbpBank.setItems(bankItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtCodeRestoBankAccount.textProperty().bindBidirectional(tidTransferController.selectedDataRestoBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(tidTransferController.selectedDataRestoBankAccount.getTblBankAccount().bankAccountHolderNameProperty());

        cbpBank.valueProperty().bindBidirectional(tidTransferController.selectedDataRestoBankAccount.getTblBankAccount().tblBankProperty());

        cbpBank.hide();
    }

    private void dataRestoBankAccountSaveHandle() {
        if (checkDataInputDataRestoBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", tidTransferController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblRestoBankAccount dummySelectedData = new TblRestoBankAccount(tidTransferController.selectedDataRestoBankAccount);
                dummySelectedData.setTblResto(dummySelectedData.getTblResto());
                dummySelectedData.setTblBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccount()));
                if (tidTransferController.getParentController().getParentController().getService().insertDataRestoBankAccount(dummySelectedData) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", tidTransferController.dialogStage);
                    //refresh data resto - bank account popup
                    tidTransferController.refreshRestoBankAccountDataPopup();
                    //reload and set data on field
                    tidTransferController.selectedDataRestoBankAccount = tidTransferController.getParentController().getParentController().getService().getDataRestoBankAccount(dummySelectedData.getIdrelation());
                    tidTransferController.selectedDataRestoBankAccount.setTblResto(tidTransferController.getParentController().getParentController().getService().getDataResto(tidTransferController.selectedDataRestoBankAccount.getTblResto().getIdresto()));
                    tidTransferController.selectedDataRestoBankAccount.setTblBankAccount(tidTransferController.getParentController().getParentController().getService().getBankAccount(tidTransferController.selectedDataRestoBankAccount.getTblBankAccount().getIdbankAccount()));
                    tidTransferController.getParentController().getParentController().selectedDataHFTWithTransfer.setTblBankAccountByReceiverBankAccount(tidTransferController.selectedDataRestoBankAccount.getTblBankAccount());
                    //close form data resto bank account
                    tidTransferController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(tidTransferController.getParentController().getParentController().getService().getErrorMessage(), tidTransferController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, tidTransferController.dialogStage);
        }
    }

    private void dataRestoBankAccountCancelHandle() {
        //close form data resto - bank account
        tidTransferController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataRestoBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeRestoBankAccount.getText() == null || txtCodeRestoBankAccount.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBank.getValue() == null) {
            dataInput = false;
            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtBankAccountHolderName.getText() == null || txtBankAccountHolderName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Pemegang Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpBankAccountStatus.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Bank Account (Status) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
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
        initFormDataRestoBankAccount();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public TIDTransferRestoBankAccountInputController(TIDTransferController parentController) {
        tidTransferController = parentController;
    }

    private final TIDTransferController tidTransferController;
    
}
