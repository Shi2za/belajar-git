/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_expense_transaction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
public class ExpanseTransactionDetailController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtItemCost;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private JFXTextArea txtDetailNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Beban)"));
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
                txtItemName,
                txtItemCost,
                txtItemQuantity);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemCost,
                txtItemQuantity);
    }
    
    private void setSelectedDataToInputForm() {

        txtItemName.textProperty().bindBidirectional(expenseTransactionController.selectedDataDetail.itemNameProperty());
        txtDetailNote.textProperty().bindBidirectional(expenseTransactionController.selectedDataDetail.detailNoteProperty());

        Bindings.bindBidirectional(txtItemCost.textProperty(), expenseTransactionController.selectedDataDetail.itemCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemQuantity.textProperty(), expenseTransactionController.selectedDataDetail.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", expenseTransactionController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                switch (expenseTransactionController.dataInputDetailStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", expenseTransactionController.dialogStageDetal);
                        expenseTransactionController.tableDataDetail.getTableView().getItems().add(expenseTransactionController.selectedDataDetail);
                        //refresh data bill
                        expenseTransactionController.refreshDataBill();
                        //close form data detail
                        expenseTransactionController.dialogStageDetal.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", expenseTransactionController.dialogStageDetal);
                        expenseTransactionController.tableDataDetail.getTableView().getItems().set(expenseTransactionController.tableDataDetail.getTableView().getSelectionModel().getSelectedIndex(),
                                expenseTransactionController.selectedDataDetail);
                        //refresh data bill
                        expenseTransactionController.refreshDataBill();
                        //close form data detail
                        expenseTransactionController.dialogStageDetal.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, expenseTransactionController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        expenseTransactionController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtItemName.getText() == null || txtItemName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Beban : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtItemCost.getText() == null 
                || txtItemCost.getText().equals("")
                || txtItemCost.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga (Satuan) Beban : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (expenseTransactionController.selectedDataDetail.getItemCost()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Harga (Satuan) Beban : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        if (txtItemQuantity.getText() == null 
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah (Beban) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (expenseTransactionController.selectedDataDetail.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah (Beban) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
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
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ExpanseTransactionDetailController(ExpenseTransactionController parentController) {
        expenseTransactionController = parentController;
    }

    private final ExpenseTransactionController expenseTransactionController;

}
