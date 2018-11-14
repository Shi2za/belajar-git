/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_customer.customer;

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
public class CustomerBankAccountController implements Initializable {

    @FXML
    private AnchorPane ancFormCustomerBankAccount;

    @FXML
    private GridPane gpFormDataCustomerBankAccount;

    @FXML
    private JFXTextField txtCodeCustomerBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private AnchorPane ancBankLayout;
    private final JFXCComboBoxPopup<TblBank> cbpBank = new JFXCComboBoxPopup<>(TblBank.class);

//    private final JFXCComboBoxPopup<RefBankAccountHolderStatus> cbpBankAccountStatus = new JFXCComboBoxPopup<>(RefBankAccountHolderStatus.class);
    @FXML
    private JFXButton btnSaveCustomerBankAccount;

    @FXML
    private JFXButton btnCancelCustomerBankAccount;

    private void initFormDataCustomerBankAccount() {

        btnSaveCustomerBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening Bank)"));
        btnSaveCustomerBankAccount.setOnAction((e) -> {
            dataCustomerBankAccountSaveHandle();
        });

        btnCancelCustomerBankAccount.setTooltip(new Tooltip("Batal"));
        btnCancelCustomerBankAccount.setOnAction((e) -> {
            dataCustomerBankAccountCancelHandle();
        });

        initDataCustomerBankAccountPopup();

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeCustomerBankAccount,
                cbpBank,
                txtBankAccountHolderName);
    }

    private void initDataCustomerBankAccountPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(customerController.getService().getAllDataBank());

        setFunctionPopup(cbpBank, tableBank, bankItems, "bankName", "Bank *");

//        //Bank Account Status
//        TableView<RefBankAccountHolderStatus> tableBankAccountHolderStatus = new TableView<>();
//        TableColumn<RefBankAccountHolderStatus, String> bankAccountHolderStatusName = new TableColumn<>("Status");
//        bankAccountHolderStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
//        tableBankAccountHolderStatus.getColumns().addAll(bankAccountHolderStatusName);
//
//        ObservableList<RefBankAccountHolderStatus> bankAccountHolderStatusItems = FXCollections.observableArrayList(customerController.getService().getAllDataBankAccountHolderStatus());
//
//        setFunctionPopup(cbpBankAccountStatus, tableBankAccountHolderStatus, bankAccountHolderStatusItems, "statusName", "Status *");
        //attached to grid-pane
        ancBankLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBank, 0.0);
        AnchorPane.setLeftAnchor(cbpBank, 0.0);
        AnchorPane.setRightAnchor(cbpBank, 0.0);
        AnchorPane.setTopAnchor(cbpBank, 0.0);
        ancBankLayout.getChildren().add(cbpBank);
//        gpFormDataCustomerBankAccount.add(cbpBankAccountStatus, 2, 2);
    }

    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(customerController.getService().getAllDataBank());
        cbpBank.setItems(bankItems);

//        //Bank Account Holder Status
//        ObservableList<RefBankAccountHolderStatus> bankAccountStatusItems = FXCollections.observableArrayList(customerController.getService().getAllDataBankAccountHolderStatus());
//        cbpBankAccountStatus.setItems(bankAccountStatusItems);
    }

    private void setFunctionPopup(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText) {
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
        content.setPrefSize(200, 200);

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

        txtCodeCustomerBankAccount.textProperty().bindBidirectional(customerController.selectedDataCustomerBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(customerController.selectedDataCustomerBankAccount.getTblBankAccount().bankAccountHolderNameProperty());

        cbpBank.valueProperty().bindBidirectional(customerController.selectedDataCustomerBankAccount.getTblBankAccount().tblBankProperty());
//        cbpBankAccountStatus.valueProperty().bindBidirectional(customerController.selectedDataCustomerBankAccount.refBankAccountHolderStatusProperty());

        cbpBank.hide();
//        cbpBankAccountStatus.hide();
    }

    private void dataCustomerBankAccountSaveHandle() {
        if (checkDataInputDataCustomerBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", customerController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (customerController.dataInputCustomerBankAccountStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", customerController.dialogStage);
                        customerController.tableDataCustomerBankAccount.getTableView().getItems().add(customerController.selectedDataCustomerBankAccount);
                        //close form data customer bank account
                        customerController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", customerController.dialogStage);
                        customerController.tableDataCustomerBankAccount.getTableView().getItems().set(customerController.tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedIndex(),
                                customerController.selectedDataCustomerBankAccount);
                        //close form data customer bank account
                        customerController.dialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, customerController.dialogStage);
        }
    }

    private void dataCustomerBankAccountCancelHandle() {
        //close form data customer bank account
        customerController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCustomerBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeCustomerBankAccount.getText() == null || txtCodeCustomerBankAccount.getText().equals("")) {
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
        initFormDataCustomerBankAccount();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public CustomerBankAccountController(CustomerController parentController) {
        customerController = parentController;
    }

    private final CustomerController customerController;

}
