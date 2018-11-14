/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_open_close_cashier_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefOpenCloseCashierBalanceStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import java.math.BigDecimal;
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
public class OpenCloseCashierBalanceDetailCloseController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtCashierName;

    @FXML
    private JFXTextField txtBeginBalanceNominal;

    @FXML
    private JFXTextField txtIncomeNominal;
    
    @FXML
    private JFXTextField txtExpenseNominal;
    
    @FXML
    private JFXTextField txtSystemEndBalanceNominal;
    
    @FXML
    private JFXTextField txtRealEndBalanceNominal;
    
    @FXML
    private JFXTextArea txtNote;
    
    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {

        btnSave.setTooltip(new Tooltip("Tutup Kas Kasir"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });

        initImportantFieldColor();
        
        initNumbericField();
    }    

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtRealEndBalanceNominal);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtRealEndBalanceNominal);
    }
    
    private void setSelectedDataToInputForm() {

        txtCashierName.setText(openCloseCashierBalanceController.selectedData.getTblEmployeeByIdcashier().getCodeEmployee()
                + " - " 
                + openCloseCashierBalanceController.selectedData.getTblEmployeeByIdcashier().getTblPeople().getFullName());
        
        txtBeginBalanceNominal.setText(ClassFormatter.currencyFormat.cFormat(openCloseCashierBalanceController.selectedData.getBeginBalanceNominal()));
        
        txtIncomeNominal.setText(ClassFormatter.currencyFormat.cFormat(openCloseCashierBalanceController.calculationTotalIncome(openCloseCashierBalanceController.selectedData)));
        txtExpenseNominal.setText(ClassFormatter.currencyFormat.cFormat(openCloseCashierBalanceController.calculationTotalExpense(openCloseCashierBalanceController.selectedData)));
        
        txtSystemEndBalanceNominal.setText(ClassFormatter.currencyFormat.cFormat(openCloseCashierBalanceController.selectedData.getSystemEndBalanceNominal()));
        
        txtRealEndBalanceNominal.textProperty().bindBidirectional(openCloseCashierBalanceController.selectedData.realEndBalanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        
        txtNote.textProperty().bindBidirectional(openCloseCashierBalanceController.selectedData.openCloseCashierBalanceNoteProperty());

    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menutup kas kasir?", "", openCloseCashierBalanceController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblOpenCloseCashierBalance dummyOpenCloseCashierBalance = new TblOpenCloseCashierBalance(openCloseCashierBalanceController.selectedData);
                dummyOpenCloseCashierBalance.setTblEmployeeByIdcashier(new TblEmployee(dummyOpenCloseCashierBalance.getTblEmployeeByIdcashier()));
                if(dummyOpenCloseCashierBalance.getRefOpenCloseCashierBalanceStatus() != null){
                    dummyOpenCloseCashierBalance.setRefOpenCloseCashierBalanceStatus(new RefOpenCloseCashierBalanceStatus(dummyOpenCloseCashierBalance.getRefOpenCloseCashierBalanceStatus()));
                }
                if (openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().updateOpenCloseCashierBalance(dummyOpenCloseCashierBalance)) {
                    HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Berhasil menutup kas kasir..!!", "", openCloseCashierBalanceController.dialogStage);
                    //refresh data
                    openCloseCashierBalanceController.refreshData();
                    //close form data detail
                    openCloseCashierBalanceController.dialogStage.close();
                } else {
                    HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Gagal menutup kas kasir..!!", openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().getErrorMessage(), openCloseCashierBalanceController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, openCloseCashierBalanceController.dialogStage);
        }
    }

    private void dataDetailCancelHandle() {
        //refresh data
        openCloseCashierBalanceController.refreshData();
        //close form data detail
        openCloseCashierBalanceController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if(txtRealEndBalanceNominal.getText() == null 
                || txtRealEndBalanceNominal.getText().equals("")
                || txtRealEndBalanceNominal.getText().equals("-")){
            dataInput = false;
            errDataInput += "Kas Akhir (Real) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }else{
            if(openCloseCashierBalanceController.selectedData.getRealEndBalanceNominal()
                    .compareTo(new BigDecimal("0")) == -1){
                dataInput = false;
                errDataInput += "Kas Akhir (Real) : Tidak boleh kurang dari '0' \n";
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
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public OpenCloseCashierBalanceDetailCloseController(OpenCloseCashierBalanceController parentController) {
        openCloseCashierBalanceController = parentController;
    }

    private final OpenCloseCashierBalanceController openCloseCashierBalanceController;
    
}
