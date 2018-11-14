/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.deposit_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.view.feature_balance.FeatureBalanceController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class DepositBalanceController implements Initializable {

    @FXML
    private AnchorPane ancFormDepositBalance;

    @FXML
    private GridPane gpFormDataDepositBalance;

    @FXML
    private JFXTextField txtBalanceName;

    @FXML
    private JFXTextField txtBalanceNominal;

    private ToggleGroup groupBalance;

    @FXML
    private JFXRadioButton rdbBalanceCompany;

    @FXML
    private JFXRadioButton rdbBalanceBackOffice;

    @FXML
    private JFXTextField txtTransferNominal;

    @FXML
    private JFXButton btnTransfer;

    private void initFormDataDepositBalance() {

        btnTransfer.setTooltip(new Tooltip("Transfer"));
        btnTransfer.setOnAction((e) -> {
            dataTransferHandle();
        });

        groupBalance = new ToggleGroup();
        rdbBalanceCompany.setToggleGroup(groupBalance);
        rdbBalanceBackOffice.setToggleGroup(groupBalance);

        initNumbericField();
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtBalanceNominal,
                txtTransferNominal);
    }
    
    private TblCompanyBalance selectedBalance;

    private final ObjectProperty<BigDecimal> transferNominal = new SimpleObjectProperty<>(new BigDecimal("0"));

    private TblCompanyBalance loadDataDepositBalance() {
        return parentController.getFBalanceManager().getDataDepositBalance();
    }

    private void setSelectedDataToInputForm() {

        selectedBalance = loadDataDepositBalance();

        txtBalanceName.textProperty().bindBidirectional(selectedBalance.balanceNameProperty());
        Bindings.bindBidirectional(txtBalanceNominal.textProperty(), selectedBalance.balanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        transferNominal.set(new BigDecimal("0"));

        rdbBalanceCompany.setSelected(false);
        rdbBalanceBackOffice.setSelected(false);

        Bindings.bindBidirectional(txtTransferNominal.textProperty(), transferNominal, new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataTransferHandle() {
//        if (checkDataInputDataTransfer()) {
//            //data current balance
//            selectedBalance.setBalanceNominal(selectedBalance.getBalanceNominal()
//                    - transferNominal.get());
//            //data another balance
//            TblCompanyBalance anotherBalance;
//            if (rdbBalanceCompany.isSelected()) {  //Company
//                anotherBalance = parentController.getFBalanceManager().getDataCompanyBalance();
//            } else {  //Back-Office
//                anotherBalance = parentController.getFBalanceManager().getDataBackOfficeBalance();
//            }
//            anotherBalance.setBalanceNominal(anotherBalance.getBalanceNominal()
//                    + transferNominal.get());
//            //dummy entry
//            TblCompanyBalance dummySelectedBalance = new TblCompanyBalance(selectedBalance);
//            TblCompanyBalance dummyAnotherBalance = new TblCompanyBalance(anotherBalance);
//            if (parentController.getFBalanceManager().updateDataBalanceTransfer(dummySelectedBalance, dummyAnotherBalance)) {
//                HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Updating data successed..!", null);
//                //refresh data input
//                setSelectedDataToInputForm();
//            } else {
//                HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Updating data failed..!", parentController.getFBalanceManager().getErrorMessage());
//            }
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please check data input..!", errDataInput);
//        }
    }

    private String errDataInput;

    private boolean checkDataInputDataTransfer() {
        boolean dataInput = true;
        errDataInput = "";
        if (!rdbBalanceCompany.isSelected() && !rdbBalanceBackOffice.isSelected()) {
            dataInput = false;
            errDataInput += "Kas (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtTransferNominal.getText() == null 
                || txtTransferNominal.getText().equals("")
                || txtTransferNominal.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal (Transfer) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if ((transferNominal.get().compareTo(new BigDecimal("0")) < 1)
                    || (transferNominal.get().compareTo(selectedBalance.getBalanceNominal()) == 1)) {
                dataInput = false;
                errDataInput += "Nominal (Transfer) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
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
        initFormDataDepositBalance();
        //refresh data form input (transfer)
        setSelectedDataToInputForm();
    }    
    
    public DepositBalanceController(FeatureBalanceController parentController) {
        this.parentController = parentController;
    }

    private final FeatureBalanceController parentController;
    
}
