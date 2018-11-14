/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_employee.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
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
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class EmployeeBankAccountController implements Initializable {

    @FXML
    private AnchorPane ancFormEmployeeBankAccount;

    @FXML
    private GridPane gpFormDataEmployeeBankAccount;

    @FXML
    private JFXTextField txtCodeEmployeeBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    private final JFXCComboBoxPopup<TblBank> cbpBank = new JFXCComboBoxPopup<>(TblBank.class);

//    private final JFXCComboBoxPopup<RefBankAccountHolderStatus> cbpBankAccountStatus = new JFXCComboBoxPopup<>(RefBankAccountHolderStatus.class);
    @FXML
    private JFXButton btnSaveEmployeeBankAccount;

    @FXML
    private JFXButton btnCancelEmployeeBankAccount;

    private void initFormDataEmployeeBankAccount() {

        btnSaveEmployeeBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening)"));
        btnSaveEmployeeBankAccount.setOnAction((e) -> {
            dataEmployeeBankAccountSaveHandle();
        });

        btnCancelEmployeeBankAccount.setTooltip(new Tooltip("Batal"));
        btnCancelEmployeeBankAccount.setOnAction((e) -> {
            dataEmployeeBankAccountCancelHandle();
        });

        initDataEmployeeBankAccountPopup();

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeEmployeeBankAccount,
                cbpBank,
                txtBankAccountHolderName);
    }

    private void initDataEmployeeBankAccountPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(employeeController.getService().getAllDataBank());

        setFunctionPopup(cbpBank, tableBank, bankItems, "bankName", "Bank *", 200, 200);

//        //Bank Account Status
//        TableView<RefBankAccountHolderStatus> tableBankAccountHolderStatus = new TableView<>();
//        TableColumn<RefBankAccountHolderStatus, String> bankAccountHolderStatusName = new TableColumn<>("Status");
//        bankAccountHolderStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
//        tableBankAccountHolderStatus.getColumns().addAll(bankAccountHolderStatusName);
//
//        ObservableList<RefBankAccountHolderStatus> bankAccountHolderStatusItems = FXCollections.observableArrayList(employeeController.getService().getAllDataBankAccountHolderStatus());
//
//        setFunctionPopup(cbpBankAccountStatus, tableBankAccountHolderStatus, bankAccountHolderStatusItems, "statusName", "Status *");
        //attached to grid-pane
        gpFormDataEmployeeBankAccount.add(cbpBank, 2, 1);
//        gpFormDataEmployeeBankAccount.add(cbpBankAccountStatus, 2, 2);
    }

    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(employeeController.getService().getAllDataBank());
        cbpBank.setItems(bankItems);

//        //Bank Account Holder Status
//        ObservableList<RefBankAccountHolderStatus> bankAccountStatusItems = FXCollections.observableArrayList(employeeController.getService().getAllDataBankAccountHolderStatus());
//        cbpBankAccountStatus.setItems(bankAccountStatusItems);
    }

    private void setFunctionPopup(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText,
            double prefWidth,
            double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != -1) {
                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
                    }
                    cbp.hide();
                });

        cbp.setPropertyNameForFiltered(nameFiltered);

        cbp.setItems(items);
        
        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth, prefHeight);

        content.setCenter(table);

        cbp.setPopupEditor(true);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(true);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);
    }
    
    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtCodeEmployeeBankAccount.textProperty().bindBidirectional(employeeController.selectedDataEmployeeBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(employeeController.selectedDataEmployeeBankAccount.getTblBankAccount().bankAccountHolderNameProperty());
        
        cbpBank.valueProperty().bindBidirectional(employeeController.selectedDataEmployeeBankAccount.getTblBankAccount().tblBankProperty());
//        cbpBankAccountStatus.valueProperty().bindBidirectional(employeeController.selectedDataEmployeeBankAccount.refBankAccountHolderStatusProperty());

        cbpBank.hide();
//        cbpBankAccountStatus.hide();
    }

    private void dataEmployeeBankAccountSaveHandle() {
        if (checkDataInputDataEmployeeBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", employeeController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (employeeController.dataInputEmployeeBankAccountStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", employeeController.dialogStage);
                        employeeController.tableDataEmployeeBankAccount.getTableView().getItems().add(employeeController.selectedDataEmployeeBankAccount);
                        //close form data employee bank account
                        employeeController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", employeeController.dialogStage);
                        employeeController.tableDataEmployeeBankAccount.getTableView().getItems().set(employeeController.tableDataEmployeeBankAccount.getTableView().getSelectionModel().getSelectedIndex(),
                                employeeController.selectedDataEmployeeBankAccount);
                        //close form data employee bank account
                        employeeController.dialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, employeeController.dialogStage);
        }
    }

    private void dataEmployeeBankAccountCancelHandle() {
        //close form data employee bank account
        employeeController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataEmployeeBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeEmployeeBankAccount.getText() == null || txtCodeEmployeeBankAccount.getText().equals("")) {
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
//            errDataInput += "Bank Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataEmployeeBankAccount();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public EmployeeBankAccountController(EmployeeController parentController) {
        employeeController = parentController;
    }

    private final EmployeeController employeeController;

}
