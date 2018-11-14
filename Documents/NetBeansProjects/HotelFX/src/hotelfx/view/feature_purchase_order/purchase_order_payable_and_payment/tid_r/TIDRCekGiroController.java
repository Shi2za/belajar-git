/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.tid_r;

import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.HotelFinanceTransactionReturnInputController;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class TIDRCekGiroController implements Initializable {

    @FXML
    private GridPane gpTIDRCekGiro;
    
    @FXML
    private JFXTextField txtCekGiroNumber;
    
    @FXML
    private JFXTextField txtSenderName;
    
    @FXML
    private JFXTextField txtReceiverName;
    
    @FXML
    private JFXDatePicker dtpIssueDate;
    
    @FXML
    private JFXDatePicker dtpDueDate;
    
    @FXML
    private AnchorPane ancBankIssue;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;
    
    @FXML
    private AnchorPane ancReceiverBankAccount;    
    private JFXCComboBoxTablePopup<TblBankAccount> cbpBankAccount;
    
    private void initFormDataTIDRCekGiro() {
        initDataTIDRCekGiroPopup();
        
        txtReceiverName.setDisable(true);
        
        initDateCalendar();
        
        initImportantFieldColor();
    }
    
    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpIssueDate,
                dtpDueDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                //                dtpIssueDate,
                dtpDueDate);
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCekGiroNumber,
                dtpIssueDate,
                dtpDueDate,
                cbpBank,
                cbpBankAccount, 
                txtSenderName, 
                txtReceiverName);
    }
    
    private void initDataTIDRCekGiroPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();
        
        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);
        
        tableBank.getColumns().addAll(bankName);
        
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(hotelFinanceTransactionReturnInputController.getParentController().getService().getAllDataBank());
        
        cbpBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableBank, bankItems, "", "Bank Penerbit *", true, 200, 200
        );

        //Bank Account (Receiver)
        TableView<TblBankAccount> tableBankAccount = new TableView<>();
        
        TableColumn<TblBankAccount, String> receiverBankName = new TableColumn("Bank");
        receiverBankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
        receiverBankName.setMinWidth(140);
        
        TableColumn<TblBankAccount, String> receiverBankAccount = new TableColumn("Nomor Rekening");
        receiverBankAccount.setCellValueFactory(cellData -> cellData.getValue().codeBankAccountProperty());
        receiverBankAccount.setMinWidth(140);
        
        TableColumn<TblBankAccount, String> receiverBankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        receiverBankAccountHolderName.setCellValueFactory(cellData -> cellData.getValue().bankAccountHolderNameProperty());
        receiverBankAccountHolderName.setMinWidth(200);
        
        tableBankAccount.getColumns().addAll(receiverBankName, receiverBankAccount, receiverBankAccountHolderName);
        
        ObservableList<TblBankAccount> bankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
        
        cbpBankAccount = new JFXCComboBoxTablePopup<>(
                TblBankAccount.class, tableBankAccount, bankAccountItems, "", "Nomor Rekening (Penerima) *", true, 500, 250
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpBank, 0.0);
        AnchorPane.setLeftAnchor(cbpBank, 0.0);
        AnchorPane.setRightAnchor(cbpBank, 0.0);
        AnchorPane.setTopAnchor(cbpBank, 0.0);
        ancBankIssue.getChildren().clear();
        ancBankIssue.getChildren().add(cbpBank);
        AnchorPane.setBottomAnchor(cbpBankAccount, 0.0);
        AnchorPane.setLeftAnchor(cbpBankAccount, 0.0);
        AnchorPane.setRightAnchor(cbpBankAccount, 0.0);
        AnchorPane.setTopAnchor(cbpBankAccount, 0.0);
        ancReceiverBankAccount.getChildren().clear();
        ancReceiverBankAccount.getChildren().add(cbpBankAccount);
    }
    
    private List<TblBankAccount> loadAllDataHotelBankAccount() {
        List<TblCompanyBalanceBankAccount> cbbaList = hotelFinanceTransactionReturnInputController.getParentController().getService().getAllDataCompanyBalanceBankAccount((long) 1);  //Kas Besar = '1'
        List<TblBankAccount> list = new ArrayList<>();
        for (TblCompanyBalanceBankAccount cbbaData : cbbaList) {
            //data bank account
            TblBankAccount data = hotelFinanceTransactionReturnInputController.getParentController().getService().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
            //data bank in data bank account
            data.setTblBank(hotelFinanceTransactionReturnInputController.getParentController().getService().getDataBank(data.getTblBank().getIdbank()));
            //add data bank account to list
            list.add(data);
        }
        return list;
    }
    
    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(hotelFinanceTransactionReturnInputController.getParentController().getService().getAllDataBank());
        cbpBank.setItems(bankItems);

        //Bank Account
        ObservableList<TblBankAccount> bankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
        cbpBankAccount.setItems(bankAccountItems);
    }
    
    private void setSelectedDataToInputForm() {
        refreshDataPopup();
        
        txtCekGiroNumber.textProperty().bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.codeCekGiroProperty());
        
        txtSenderName.textProperty().bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.senderNameProperty());
        txtReceiverName.textProperty().bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.receiverNameProperty());
        
        if (hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.getIssueDateTime() != null) {
            dtpIssueDate.setValue(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.getIssueDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpIssueDate.setValue(null);
        }
        dtpIssueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.setIssueDateTime(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
        
        if (hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.getValidUntilDateTime() != null) {
            dtpDueDate.setValue(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.getValidUntilDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpDueDate.setValue(null);
        }
        dtpDueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.setValidUntilDateTime(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
        
        cbpBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.setReceiverName(newVal.getBankAccountHolderName());
            }
        });
        
        cbpBank.valueProperty().bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.tblBankProperty());
        cbpBankAccount.valueProperty().bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCekGiro.tblBankAccountByReceiverBankAccountProperty());
        
        cbpBank.hide();
        cbpBankAccount.hide();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataTIDRCekGiro();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public TIDRCekGiroController(HotelFinanceTransactionReturnInputController parentController) {
        hotelFinanceTransactionReturnInputController = parentController;
    }
    
    private final HotelFinanceTransactionReturnInputController hotelFinanceTransactionReturnInputController;
    
}
