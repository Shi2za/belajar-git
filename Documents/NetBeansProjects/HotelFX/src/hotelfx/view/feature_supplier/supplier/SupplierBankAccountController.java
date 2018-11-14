/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_supplier.supplier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
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
public class SupplierBankAccountController implements Initializable {

    @FXML
    private AnchorPane ancFormSupplierBankAccount;

    @FXML
    private GridPane gpFormDataSupplierBankAccount;

    @FXML
    private JFXTextField txtCodeSupplierBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private AnchorPane ancBankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;
    
    @FXML
    private JFXButton btnSaveSupplierBankAccount;

    @FXML
    private JFXButton btnCancelSupplierBankAccount;

    private void initFormDataSupplierBankAccount() {
        initDataSupplierBankAccountPopup();

        btnSaveSupplierBankAccount.setTooltip(new Tooltip("Simpan (Data Rekening Bank)"));
        btnSaveSupplierBankAccount.setOnAction((e) -> {
            dataSupplierBankAccountSaveHandle();
        });

        btnCancelSupplierBankAccount.setTooltip(new Tooltip("Batal"));
        btnCancelSupplierBankAccount.setOnAction((e) -> {
            dataSupplierBankAccountCancelHandle();
        });        

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeSupplierBankAccount,
                cbpBank,
                txtBankAccountHolderName);
    }

    private void initDataSupplierBankAccountPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(supplierController.getService().getAllDataBank());

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
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(supplierController.getService().getAllDataBank());
        cbpBank.setItems(bankItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtCodeSupplierBankAccount.textProperty().bindBidirectional(supplierController.selectedDataSupplierBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(supplierController.selectedDataSupplierBankAccount.getTblBankAccount().bankAccountHolderNameProperty());

        cbpBank.valueProperty().bindBidirectional(supplierController.selectedDataSupplierBankAccount.getTblBankAccount().tblBankProperty());

        cbpBank.hide();
    }

    private void dataSupplierBankAccountSaveHandle() {
        if (checkDataInputDataSupplierBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", supplierController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (supplierController.dataInputSupplierBankAccountStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", supplierController.dialogStage);
                        supplierController.tableDataSupplierBankAccount.getTableView().getItems().add(supplierController.selectedDataSupplierBankAccount);
                        //close form data supplier bank account
                        supplierController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", supplierController.dialogStage);
                        supplierController.tableDataSupplierBankAccount.getTableView().getItems().set(supplierController.tableDataSupplierBankAccount.getTableView().getSelectionModel().getSelectedIndex(),
                                supplierController.selectedDataSupplierBankAccount);
                        //close form data supplier bank account
                        supplierController.dialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, supplierController.dialogStage);
        }
    }

    private void dataSupplierBankAccountCancelHandle() {
        //close form data supplier bank account
        supplierController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataSupplierBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeSupplierBankAccount.getText() == null || txtCodeSupplierBankAccount.getText().equals("")) {
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
        initFormDataSupplierBankAccount();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public SupplierBankAccountController(SupplierController parentController) {
        supplierController = parentController;
    }

    private final SupplierController supplierController;

}
