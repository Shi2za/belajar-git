/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.cashier_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblCompanyBalance;
import java.math.BigDecimal;
import java.net.URL;
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
public class MinNominalBalanceInputController implements Initializable {

    @FXML
    private AnchorPane ancFormMinNominalBalance;

    @FXML
    private GridPane gpFormDataMinNominalBalance;

    @FXML
    private JFXTextField txtMinNominalBalance;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataMinNominalBalance() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Minimun Nominal Kas)"));
        btnSave.setOnAction((e) -> {
            dataSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCancelHandle();
        });

        initImportantFieldColor();
        
        initNumbericField();
        
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtMinNominalBalance);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtMinNominalBalance);
    }
    
    private ObjectProperty<BigDecimal> minNominalBalance;

    private void setSelectedDataToInputForm() {

        minNominalBalance = new SimpleObjectProperty<>(cashierBalanceController.selectedBalance.getMinimalBalanceNominal());

        Bindings.bindBidirectional(txtMinNominalBalance.textProperty(), minNominalBalance, new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", cashierBalanceController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblCompanyBalance dummySelectedData = new TblCompanyBalance(cashierBalanceController.selectedBalance);
                dummySelectedData.setMinimalBalanceNominal(minNominalBalance.get());
                if (cashierBalanceController.getService().updateDataCompanyBalance(dummySelectedData)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", cashierBalanceController.dialogStage);
                    //refresh data balance
                    cashierBalanceController.setSelectedDataToInputForm();
                    //close form data input
                    cashierBalanceController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(cashierBalanceController.getService().getErrorMessage(), cashierBalanceController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, cashierBalanceController.dialogStage);
        }
    }

    private void dataCancelHandle() {
        //refresh data balance
        cashierBalanceController.setSelectedDataToInputForm();
        //close form data input
        cashierBalanceController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInput() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtMinNominalBalance.getText() == null 
                || txtMinNominalBalance.getText().equals("")
                || txtMinNominalBalance.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Minimum Nominal Kas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (minNominalBalance.getValue().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Minimum Nominal Kas : Tidak boleh kurang dari '0' \n";
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
        initFormDataMinNominalBalance();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public MinNominalBalanceInputController(CashierBalanceController parentController) {
        cashierBalanceController = parentController;
    }

    private final CashierBalanceController cashierBalanceController;

}
