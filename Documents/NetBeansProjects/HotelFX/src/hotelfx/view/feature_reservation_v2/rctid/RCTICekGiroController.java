/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.view.feature_reservation_v2.RCTransactionInputDetailController;
import java.math.BigDecimal;
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
public class RCTICekGiroController implements Initializable {

    @FXML
    private GridPane gpRCTICekGiro;
    
    @FXML
    private JFXTextField txtTotalBill;
    
    @FXML
    private JFXCheckBox chbGetRestOfBill;
    
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
    
    @FXML
    private AnchorPane ancReceiverBankAccount;
    
    private JFXCComboBoxTablePopup<TblBank> cbpBank;
    
    private JFXCComboBoxTablePopup<TblBankAccount> cbpBankAccount;
    
    @FXML
    private JFXButton btnSave;
    
    private void initFormDataRCTICekGiro() {
        initDataRCTICekGiroPopup();
        
        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataRCTICekGiroSaveHandle();
        });

        //rest of bill
        chbGetRestOfBill.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set rest of bill
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                        .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
            }
        });
        
        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal
                    .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) != 0) {
                chbGetRestOfBill.setSelected(false);
            }
        });
        
        txtReceiverName.setDisable(true);
        
        initDateCalendar();
        
        initImportantFieldColor();
        
        initNumbericField();
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
                txtTotalBill,
                txtCekGiroNumber,
                dtpIssueDate,
                dtpDueDate,
                cbpBank,
                txtSenderName,
                cbpBankAccount);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill);
    }
    
    private void initDataRCTICekGiroPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();
        
        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);
        
        tableBank.getColumns().addAll(bankName);
        
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getAllDataBank());
        
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

//        gpRCTICekGiro.add(cbpBank, 0, 3);
//        gpRCTICekGiro.add(cbpBankAccount, 0, 5, 2, 1);
    }
    
    private List<TblBankAccount> loadAllDataHotelBankAccount() {
        List<TblCompanyBalanceBankAccount> cbbaList = rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getAllDataCompanyBalanceBankAccount((long) 1);  //hotel baalnce = '1'
        List<TblBankAccount> list = new ArrayList<>();
        for (TblCompanyBalanceBankAccount cbbaData : cbbaList) {
            //data bank account
            TblBankAccount data = rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
            //data bank in data bank account
            data.setTblBank(rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getBank(data.getTblBank().getIdbank()));
            //add data bank account to list
            list.add(data);
        }
        return list;
    }
    
    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getAllDataBank());
        cbpBank.setItems(bankItems);

        //Bank Account
        ObservableList<TblBankAccount> bankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
        cbpBankAccount.setItems(bankAccountItems);
    }
    
    private void setSelectedDataToInputForm() {
        refreshDataPopup();
        
        Bindings.bindBidirectional(txtTotalBill.textProperty(), rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtCekGiroNumber.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.codeCekGiroProperty());
        
        txtSenderName.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.senderNameProperty());
        txtReceiverName.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.receiverNameProperty());
        
        if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.getIssueDateTime() != null) {
            dtpIssueDate.setValue(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.getIssueDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpIssueDate.setValue(null);
        }
        dtpIssueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.setIssueDateTime(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
        
        if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.getValidUntilDateTime() != null) {
            dtpDueDate.setValue(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.getValidUntilDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpDueDate.setValue(null);
        }
        dtpDueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.setValidUntilDateTime(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
        
        cbpBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.setReceiverName(newVal.getBankAccountHolderName());
            }
        });
        
        cbpBank.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.tblBankProperty());
        cbpBankAccount.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithCekGiro.tblBankAccountByReceiverBankAccountProperty());
        
        cbpBank.hide();
        cbpBankAccount.hide();
    }
    
    private void dataRCTICekGiroSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcTransactionInputDetailController.getParentController().dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //save data transaction & close dialog data rc-transaction detail
                rcTransactionInputDetailController.getParentController().saveDataTransaction();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcTransactionInputDetailController.getParentController().dialogStage);
        }
    }
    
    private String errDataInput;
    
    private boolean checkDataInputDataReservationPayment() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtTotalBill.getText() == null 
                || txtTotalBill.getText().equals("")
                || txtTotalBill.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                        .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                                .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Nilai tidak dapat lebih besar dari '" + (rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
                }
            }
        }
        if (txtCekGiroNumber.getText() == null || txtCekGiroNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Cek/Giro : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpIssueDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Buat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpDueDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Jatuh Tempo : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBank.getValue() == null) {
            dataInput = false;
            errDataInput += "Bank Penerbit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtSenderName.getText() == null || txtSenderName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtReceiverName.getText() == null || txtReceiverName.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (cbpBankAccount.getValue() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataRCTICekGiro();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public RCTICekGiroController(RCTransactionInputDetailController parentController) {
        rcTransactionInputDetailController = parentController;
    }
    
    private final RCTransactionInputDetailController rcTransactionInputDetailController;
    
}
