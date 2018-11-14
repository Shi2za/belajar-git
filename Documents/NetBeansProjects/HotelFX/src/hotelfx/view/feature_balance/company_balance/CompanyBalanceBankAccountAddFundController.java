/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.company_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class CompanyBalanceBankAccountAddFundController implements Initializable {

    @FXML
    private AnchorPane ancFormCompanyBalanceBankAccountAddFund;

    @FXML
    private GridPane gpFormDataCompanyBalanceBankAccountAddFund;

    @FXML
    private JFXTextField txtCodeCompanyBalanceBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private JFXTextField txtBankAccountNominalBalance;

    @FXML
    private JFXTextField txtAddFundNominal;

    @FXML
    private JFXTextArea txtAddFundNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCompanyBalanceBankAccountAddFund() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Tambahan Dana)"));
        btnSave.setOnAction((e) -> {
            dataCompanyBalanceBankAccountAddFundSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCompanyBalanceBankAccountAddFundCancelHandle();
        });
        
        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtAddFundNominal,
                txtAddFundNote);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtBankAccountNominalBalance,
                txtAddFundNominal);
    }
    
    private final ObjectProperty<BigDecimal> addFundNominal = new SimpleObjectProperty<>(new BigDecimal("0"));

    private final StringProperty addFundNote = new SimpleStringProperty();

    private void setSelectedDataToInputForm() {

        txtCodeCompanyBalanceBankAccount.textProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.getTblBankAccount().bankAccountHolderNameProperty());
        txtBankAccountNominalBalance.textProperty().bindBidirectional(companyBalanceController.selectedDataCompanyBalanceBankAccount.companyBalanceBankAccountNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        addFundNominal.set(new BigDecimal("0"));
        addFundNote.set("");

        txtAddFundNominal.textProperty().bindBidirectional(addFundNominal, new ClassFormatter.CBigDecimalStringConverter());
        txtAddFundNote.textProperty().bindBidirectional(addFundNote);

    }

    private void dataCompanyBalanceBankAccountAddFundSaveHandle() {
        if (checkDataInputDataCompanyBalanceBankAccountAddFund()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menambahkan dana pada data kas ini?", "", companyBalanceController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblCompanyBalanceBankAccount dummySelectedDataCompanyBalanceBankAccount = new TblCompanyBalanceBankAccount(companyBalanceController.selectedDataCompanyBalanceBankAccount);
                dummySelectedDataCompanyBalanceBankAccount.setTblCompanyBalance(new TblCompanyBalance(dummySelectedDataCompanyBalanceBankAccount.getTblCompanyBalance()));
                dummySelectedDataCompanyBalanceBankAccount.setTblBankAccount(new TblBankAccount(dummySelectedDataCompanyBalanceBankAccount.getTblBankAccount()));
                if (companyBalanceController.getService().updateDataCompanyBalanceBankAccountAddFund(
                        dummySelectedDataCompanyBalanceBankAccount,
                        addFundNominal.get(),
                        addFundNote.get())) {
                    ClassMessage.showSucceedUpdatingDataMessage("", companyBalanceController.dialogStage);
                    //refresh data companyBalance bank account
                    companyBalanceController.refreshDataTableCompanyBalanceBankAccount();
                    //close form data companyBalance bank account
                    companyBalanceController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(companyBalanceController.getService().getErrorMessage(), companyBalanceController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, companyBalanceController.dialogStage);
        }
    }

    private void dataCompanyBalanceBankAccountAddFundCancelHandle() {
        //refresh data companyBalance bank account
        companyBalanceController.refreshDataTableCompanyBalanceBankAccount();
        //close form data companyBalance bank account
        companyBalanceController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCompanyBalanceBankAccountAddFund() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtAddFundNominal.getText() == null 
                || txtAddFundNominal.getText().equals("")
                || txtAddFundNominal.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Tambahan Dana : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (addFundNominal.get().compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah Tambahan Dana : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        if (txtAddFundNote.getText() == null || txtAddFundNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Keterangan Tambahan Dana : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataCompanyBalanceBankAccountAddFund();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public CompanyBalanceBankAccountAddFundController(CompanyBalanceController parentController) {
        companyBalanceController = parentController;
    }

    private final CompanyBalanceController companyBalanceController;

}
