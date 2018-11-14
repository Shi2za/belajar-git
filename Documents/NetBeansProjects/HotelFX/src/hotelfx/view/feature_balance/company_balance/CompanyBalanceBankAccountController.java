/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.company_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
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
public class CompanyBalanceBankAccountController implements Initializable {

    @FXML
    private AnchorPane ancFormCompanyBalanceBankAccount;

    @FXML
    private GridPane gpFormDataCompanyBalanceBankAccount;

    @FXML
    private JFXTextField txtCodeCompanyBalanceBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private JFXTextField txtBankAccountNominalBalance;

    @FXML
    private AnchorPane ancBankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private JFXButton btnSaveCompanyBalanceBankAccount;

    @FXML
    private JFXButton btnCancelCompanyBalanceBankAccount;

    private void initFormDataCompanyBalanceBankAccount() {

        initDataCompanyBalanceBankAccountPopup();
        
        btnSaveCompanyBalanceBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening)"));
        btnSaveCompanyBalanceBankAccount.setOnAction((e) -> {
            dataCompanyBalanceBankAccountSaveHandle();
        });

        btnCancelCompanyBalanceBankAccount.setTooltip(new Tooltip("Batal"));
        btnCancelCompanyBalanceBankAccount.setOnAction((e) -> {
            dataCompanyBalanceBankAccountCancelHandle();
        });

        initImportantFieldColor();
        
        initNumbericField();

//        txtBankAccountNominalBalance.setDisable(companyBalanceController.dataInputCompanyBalanceBankAccountStatus != 0);    //insert = '0'
        txtBankAccountNominalBalance.setDisable(true);

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeCompanyBalanceBankAccount,
                cbpBank,
                txtBankAccountHolderName);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtBankAccountNominalBalance);
    }
    
    private void initDataCompanyBalanceBankAccountPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(companyBalanceController.getService().getAllDataBank());

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
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(companyBalanceController.getService().getAllDataBank());
        cbpBank.setItems(bankItems);

//        //Bank Account Holder Status
//        ObservableList<RefBankAccountHolderStatus> bankAccountStatusItems = FXCollections.observableArrayList(companyBalanceController.getService().getAllDataBankAccountHolderStatus());
//        cbpBankAccountStatus.setItems(bankAccountStatusItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtCodeCompanyBalanceBankAccount.textProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.getTblBankAccount().bankAccountHolderNameProperty());

        txtBankAccountNominalBalance.textProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.companyBalanceBankAccountNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        cbpBank.valueProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.getTblBankAccount().tblBankProperty());
//        cbpBankAccountStatus.valueProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.refBankAccountHolderStatusProperty());

        cbpBank.hide();
//        cbpBankAccountStatus.hide();
    }

    private void dataCompanyBalanceBankAccountSaveHandle() {
        if (checkDataInputDataCompanyBalanceBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", companyBalanceController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblCompanyBalanceBankAccount dummySelectedDataCompanyBalanceBankAccount = new TblCompanyBalanceBankAccount(companyBalanceController.selectedDataCompanyBalanceBankAccount);
                dummySelectedDataCompanyBalanceBankAccount.setTblCompanyBalance(new TblCompanyBalance(dummySelectedDataCompanyBalanceBankAccount.getTblCompanyBalance()));
                dummySelectedDataCompanyBalanceBankAccount.setTblBankAccount(new TblBankAccount(dummySelectedDataCompanyBalanceBankAccount.getTblBankAccount()));
                switch (companyBalanceController.dataInputCompanyBalanceBankAccountStatus) {
                    case 0:
                        if (companyBalanceController.getService().insertDataCompanyBalanceBankAccount(dummySelectedDataCompanyBalanceBankAccount)
                                != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", companyBalanceController.dialogStage);
                            //refresh data companyBalance bank account
                            companyBalanceController.refreshDataTableCompanyBalanceBankAccount();
                            //close form data companyBalance bank account
                            companyBalanceController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(companyBalanceController.getService().getErrorMessage(), companyBalanceController.dialogStage);
                        }
                        break;
                    case 1:
                        if (companyBalanceController.getService().updateDataCompanyBalanceBankAccount(dummySelectedDataCompanyBalanceBankAccount)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", companyBalanceController.dialogStage);
                            //refresh data companyBalance bank account
                            companyBalanceController.refreshDataTableCompanyBalanceBankAccount();
                            //close form data companyBalance bank account
                            companyBalanceController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(companyBalanceController.getService().getErrorMessage(), companyBalanceController.dialogStage);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, companyBalanceController.dialogStage);
        }
    }

    private void dataCompanyBalanceBankAccountCancelHandle() {
        //refresh data companyBalance bank account
        companyBalanceController.refreshDataTableCompanyBalanceBankAccount();
        //close form data companyBalance bank account
        companyBalanceController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCompanyBalanceBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeCompanyBalanceBankAccount.getText() == null || txtCodeCompanyBalanceBankAccount.getText().equals("")) {
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
        if (txtBankAccountNominalBalance.getText() == null 
                || txtBankAccountNominalBalance.getText().equals("")
                || txtBankAccountNominalBalance.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Kas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpBankAccountStatus.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Bank Account Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
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
        initFormDataCompanyBalanceBankAccount();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public CompanyBalanceBankAccountController(CompanyBalanceController parentController) {
        companyBalanceController = parentController;
    }

    private final CompanyBalanceController companyBalanceController;

}
