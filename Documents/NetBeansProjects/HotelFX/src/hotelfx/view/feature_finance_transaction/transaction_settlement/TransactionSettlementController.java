/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_finance_transaction.transaction_settlement;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.view.feature_finance_transaction.FeatureFinanceTransactionController;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class TransactionSettlementController implements Initializable {
    
    private ClassFilteringTable<DataTransactionSettlement> cft;
    
    @FXML
    private AnchorPane ancHeaderLayout;
    
    @FXML
    private AnchorPane ancBodyLayout;
    
    private ClassTableWithControl tableDataTransactionSettlement;
    
    @FXML
    private AnchorPane ancTableLayout;
    
    @FXML
    private AnchorPane ancDetailLayout;
    
    @FXML
    private Label lblDetailTransaction;
    
    @FXML
    private JFXButton btnSettle;
    
    private void initFormDataTransactionSettlementDetail() {
        lblDetailTransaction.setText("");
        
        initTableDataTransactionSettlement();
    }
    
    private void initTableDataTransactionSettlement() {
        //set table
        setTableDataTransactionSettlement();
        //set control
        setTableControlDataTransactionSettlement();
        //set table-control to content-view
        ancTableLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataTransactionSettlement, 0.0);
        AnchorPane.setLeftAnchor(tableDataTransactionSettlement, 0.0);
        AnchorPane.setBottomAnchor(tableDataTransactionSettlement, 0.0);
        AnchorPane.setRightAnchor(tableDataTransactionSettlement, 0.0);
        
        ancTableLayout.getChildren().add(tableDataTransactionSettlement);
    }
    
    private void setTableDataTransactionSettlement() {
        TableView<DataTransactionSettlement> tableView = new TableView();
        
        TableColumn<DataTransactionSettlement, String> receiverBankAccount = new TableColumn("Kas");
        receiverBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<DataTransactionSettlement, String> param)
                -> Bindings.createStringBinding(() -> getBankAccoutnAndBank(param.getValue()),
                        param.getValue().dataReservationPaymentProperty()));
        receiverBankAccount.setMinWidth(180);
        
        TableColumn<DataTransactionSettlement, String> transactionDate = new TableColumn("Tanggal");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<DataTransactionSettlement, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDataReservationPayment().getPaymentDate()),
                        param.getValue().dataReservationPaymentProperty()));
        transactionDate.setMinWidth(120);
        
        TableColumn<DataTransactionSettlement, String> transactionNominal = new TableColumn("Nominal");
        transactionNominal.setCellValueFactory((TableColumn.CellDataFeatures<DataTransactionSettlement, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataReservationPayment().getUnitNominal()),
                        param.getValue().dataReservationPaymentProperty()));
        transactionNominal.setMinWidth(120);
        
        TableColumn<DataTransactionSettlement, String> transactionType = new TableColumn("Tipe");
        transactionType.setCellValueFactory((TableColumn.CellDataFeatures<DataTransactionSettlement, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationPayment().getRefFinanceTransactionPaymentType().getTypeName(),
                        param.getValue().dataReservationPaymentProperty()));
        transactionType.setMinWidth(140);
        
        TableColumn<DataTransactionSettlement, String> cashierName = new TableColumn("Kasir");
        cashierName.setCellValueFactory((TableColumn.CellDataFeatures<DataTransactionSettlement, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationPayment().getTblEmployeeByIdcashier().getTblPeople().getFullName(),
                        param.getValue().dataReservationPaymentProperty()));
        cashierName.setMinWidth(140);
        
        TableColumn<DataTransactionSettlement, String> note = new TableColumn("Note");
        note.setCellValueFactory((TableColumn.CellDataFeatures<DataTransactionSettlement, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationPayment().getPaymentNote(),
                        param.getValue().dataReservationPaymentProperty()));
        note.setMinWidth(250);
        
        tableView.getColumns().addAll(receiverBankAccount, transactionDate, transactionNominal, transactionType, cashierName);
        
        tableView.setItems(loadAllDataTransactionSettlement());
        
        tableView.setRowFactory(tv -> {
            TableRow<DataTransactionSettlement> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (tableView.getSelectionModel().getSelectedItems().size() == 1) {
                    lblDetailTransaction.setText(getDataDetail((DataTransactionSettlement) tableView.getSelectionModel().getSelectedItem()));
                } else {
                    lblDetailTransaction.setText("");
                }
            });
            return row;
        });
        
        tableDataTransactionSettlement = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                DataTransactionSettlement.class,
                tableDataTransactionSettlement.getTableView(),
                tableDataTransactionSettlement.getTableView().getItems());
        
        AnchorPane.setBottomAnchor(cft, 12.5);
        AnchorPane.setLeftAnchor(cft, 15.0);
//        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
        
    }
    
    private String getBankAccoutnAndBank(DataTransactionSettlement dataTransactionSettlement) {
        if (dataTransactionSettlement != null
                && dataTransactionSettlement.getDataReservationPayment() != null) {
            if (dataTransactionSettlement.getDataTransfer() != null) {    //transfer
                return dataTransactionSettlement.getDataTransfer().getTblBankAccountByReceiverBankAccount().getCodeBankAccount()
                        + " - " + dataTransactionSettlement.getDataTransfer().getTblBankAccountByReceiverBankAccount().getTblBank().getBankName();
            }
            if (dataTransactionSettlement.getDataBankCard() != null) {    //bank card
                return dataTransactionSettlement.getDataBankCard().getTblBankAccount().getCodeBankAccount()
                        + " - " + dataTransactionSettlement.getDataBankCard().getTblBankAccount().getTblBank().getBankName();
            }
            if (dataTransactionSettlement.getDataCekGiro() != null) { //cek-giro
                return dataTransactionSettlement.getDataCekGiro().getTblBankAccountByReceiverBankAccount().getCodeBankAccount()
                        + " - " + dataTransactionSettlement.getDataCekGiro().getTblBankAccountByReceiverBankAccount().getTblBank().getBankName();
            }
            if (dataTransactionSettlement.getDataGuaranteeLetter() != null) { //guarantee letter
                return dataTransactionSettlement.getDataGuaranteeLetter().getTblBankAccountByReceiverBankAccount().getCodeBankAccount()
                        + " - " + dataTransactionSettlement.getDataGuaranteeLetter().getTblBankAccountByReceiverBankAccount().getTblBank().getBankName();
            }
        }
        return "";
    }
    
    private String getDataDetail(DataTransactionSettlement dataTransactionSettlement) {
        if (dataTransactionSettlement != null
                && dataTransactionSettlement.getDataReservationPayment() != null) {
            String dataDetail = "Nomor Transaksi : " + dataTransactionSettlement.getDataReservationPayment().getCodePayment() + "\n";
            if (dataTransactionSettlement.getDataTransfer() != null) { //transfer
                dataDetail += "Pengirim : " + dataTransactionSettlement.getDataTransfer().getSenderName()
                        + " (" + dataTransactionSettlement.getDataTransfer().getTblBankAccountBySenderBankAccount().getCodeBankAccount() + ")\n";
                return dataDetail;
            }
            if (dataTransactionSettlement.getDataBankCard() != null) { //bank card
                dataDetail += "App. Code : " + dataTransactionSettlement.getDataBankCard().getApprovalCode() + "\n";
                dataDetail += "No. Kartu : " + dataTransactionSettlement.getDataBankCard().getBankCardNumber() + "\n";
                if (dataTransactionSettlement.getDataReservationPayment().getRefFinanceTransactionPaymentType().getIdtype() == 3) {   //Kartu Kredit = '3'
                    dataDetail += "Nama : " + dataTransactionSettlement.getDataBankCard().getBankCardHolder() + "\n";
                    dataDetail += "Expr. : " + (new SimpleDateFormat("MM/yy")).format(dataTransactionSettlement.getDataBankCard().getBankCardExprDate()) + "\n";
                }
                dataDetail += "Bank : " + dataTransactionSettlement.getDataBankCard().getTblBank().getBankName() + "\n";
                dataDetail += "Jaringan : " + dataTransactionSettlement.getDataBankCard().getTblBankNetworkCard().getNetworkCardName() + "\n";
                dataDetail += "EDC : " + dataTransactionSettlement.getDataBankCard().getTblBankEdc().getEdcname() + "\n";
                dataDetail += "Nominal MDR : " + ClassFormatter.currencyFormat.cFormat(dataTransactionSettlement.getDataBankCard().getPaymentCharge()) + "\n";
                return dataDetail;
            }
            if (dataTransactionSettlement.getDataCekGiro() != null) {  //cek-giro
                dataDetail += "No. Cek/Giro : " + dataTransactionSettlement.getDataCekGiro().getCodeCekGiro() + "\n";
                dataDetail += "Bank Penerbit : " + dataTransactionSettlement.getDataCekGiro().getTblBank().getBankName() + "\n";
                dataDetail += "Tgl. Buat : " + ClassFormatter.dateFormate.format(dataTransactionSettlement.getDataCekGiro().getIssueDateTime()) + "\n";
                dataDetail += "Tgl. jatuh Tempo : " + ClassFormatter.dateFormate.format(dataTransactionSettlement.getDataCekGiro().getValidUntilDateTime()) + "\n";
                dataDetail += "Atas Nama : " + dataTransactionSettlement.getDataCekGiro().getSenderName() + "\n";
                return dataDetail;
            }
            if (dataTransactionSettlement.getDataGuaranteeLetter() != null) {  //guarantee letter
                dataDetail += "Partner : " + dataTransactionSettlement.getDataGuaranteeLetter().getTblPartner().getPartnerName() + "\n";
                dataDetail += "Pengirim : " + dataTransactionSettlement.getDataGuaranteeLetter().getSenderName()
                        + " (" + dataTransactionSettlement.getDataGuaranteeLetter().getTblBankAccountBySenderBankAccount().getCodeBankAccount() + ")\n";
                return dataDetail;
            }
        }
        return "";
    }
    
    private void setTableControlDataTransactionSettlement() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Settle");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataTransactionSettleHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataTransactionSettlement.addButtonControl(buttonControls);
    }
    
    public ObservableList<DataTransactionSettlement> loadAllDataTransactionSettlement() {
        List<DataTransactionSettlement> list = new ArrayList<>();
        List<TblReservationPayment> reservationPayments = parentController.getFFinanceTransactionManager().getAllDataReservationPayment();
        System.out.println("size : " + reservationPayments.size());
        for (TblReservationPayment reservationPayment : reservationPayments) {
            if (reservationPayment.getSettleDate() == null) {
                if (reservationPayment.getTblReservationBill() != null) {
                    //data bill
                    reservationPayment.setTblReservationBill(parentController.getFFinanceTransactionManager().getDataReservationBill(reservationPayment.getTblReservationBill().getIdbill()));
                    //data reservation
                    reservationPayment.getTblReservationBill().setTblReservation(parentController.getFFinanceTransactionManager().getDataReservation(reservationPayment.getTblReservationBill().getTblReservation().getIdreservation()));
                    //data customer
                    reservationPayment.getTblReservationBill().getTblReservation().setTblCustomer(parentController.getFFinanceTransactionManager().getDataCustomer(reservationPayment.getTblReservationBill().getTblReservation().getTblCustomer().getIdcustomer()));
                    //data customer - people
                    reservationPayment.getTblReservationBill().getTblReservation().getTblCustomer().setTblPeople(parentController.getFFinanceTransactionManager().getDataPeople(reservationPayment.getTblReservationBill().getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
                }
                //data employee
                reservationPayment.setTblEmployeeByIdcashier(parentController.getFFinanceTransactionManager().getDataEmployee(reservationPayment.getTblEmployeeByIdcashier().getIdemployee()));
                //data employee - people
                reservationPayment.getTblEmployeeByIdcashier().setTblPeople(parentController.getFFinanceTransactionManager().getDataPeople(reservationPayment.getTblEmployeeByIdcashier().getTblPeople().getIdpeople()));
                //data finance transaction payment type
                reservationPayment.setRefFinanceTransactionPaymentType(parentController.getFFinanceTransactionManager().getDataFinanceTransactionPaymentType(reservationPayment.getRefFinanceTransactionPaymentType().getIdtype()));
                switch (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
                    case 1:    //Transfer = '1'
                        TblReservationPaymentWithTransfer dataTransfer = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithTransferByIDReservationPayment(reservationPayment.getIdpayment());
                        dataTransfer.setTblReservationPayment(reservationPayment);
                        dataTransfer.setTblBankAccountBySenderBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataTransfer.getTblBankAccountBySenderBankAccount().getIdbankAccount()));
                        dataTransfer.getTblBankAccountBySenderBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataTransfer.getTblBankAccountBySenderBankAccount().getTblBank().getIdbank()));
                        dataTransfer.setTblBankAccountByReceiverBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount()));
                        dataTransfer.getTblBankAccountByReceiverBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataTransfer.getTblBankAccountByReceiverBankAccount().getTblBank().getIdbank()));
                        list.add(new DataTransactionSettlement(reservationPayment, dataTransfer, null, null, null));
                        break;
                    case 2:    //Kartu Debit = '2'
                        TblReservationPaymentWithBankCard dataDebitCard = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithbankCardByIDReservationPayment(reservationPayment.getIdpayment());
                        if (dataDebitCard.getSettleDate() != null
                                && dataDebitCard.getRefEdctransactionStatus().getIdstatus() == 0) { //Sale = '0'
                            dataDebitCard.setTblReservationPayment(reservationPayment);
                            dataDebitCard.setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataDebitCard.getTblBank().getIdbank()));
                            dataDebitCard.setTblBankNetworkCard(parentController.getFFinanceTransactionManager().getDataBankNetworkCard(dataDebitCard.getTblBankNetworkCard().getIdnetworkCard()));
                            dataDebitCard.setTblBankEdc(parentController.getFFinanceTransactionManager().getDataBankEDC(dataDebitCard.getTblBankEdc().getIdedc()));
                            dataDebitCard.setTblBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataDebitCard.getTblBankAccount().getIdbankAccount()));
                            dataDebitCard.getTblBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataDebitCard.getTblBankAccount().getTblBank().getIdbank()));
                            list.add(new DataTransactionSettlement(reservationPayment, null, dataDebitCard, null, null));
                        }
                        break;
                    case 3:    //Kartu Kredit = '3'
                        TblReservationPaymentWithBankCard dataCreaditCard = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithbankCardByIDReservationPayment(reservationPayment.getIdpayment());
                        if (dataCreaditCard.getSettleDate() != null
                                && dataCreaditCard.getRefEdctransactionStatus().getIdstatus() == 0) { //Sale = '0'
                            dataCreaditCard.setTblReservationPayment(reservationPayment);
                            dataCreaditCard.setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataCreaditCard.getTblBank().getIdbank()));
                            dataCreaditCard.setTblBankNetworkCard(parentController.getFFinanceTransactionManager().getDataBankNetworkCard(dataCreaditCard.getTblBankNetworkCard().getIdnetworkCard()));
                            dataCreaditCard.setTblBankEdc(parentController.getFFinanceTransactionManager().getDataBankEDC(dataCreaditCard.getTblBankEdc().getIdedc()));
                            dataCreaditCard.setTblBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataCreaditCard.getTblBankAccount().getIdbankAccount()));
                            dataCreaditCard.getTblBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataCreaditCard.getTblBankAccount().getTblBank().getIdbank()));
                            list.add(new DataTransactionSettlement(reservationPayment, null, dataCreaditCard, null, null));
                        }
                        break;
                    case 4:    //Cek = '4'
                        TblReservationPaymentWithCekGiro dataCek = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithCekGiroByIDReservationPayment(reservationPayment.getIdpayment());
                        dataCek.setTblReservationPayment(reservationPayment);
                        dataCek.setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataCek.getTblBank().getIdbank()));
                        dataCek.setTblBankAccountByReceiverBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataCek.getTblBankAccountByReceiverBankAccount().getIdbankAccount()));
                        dataCek.getTblBankAccountByReceiverBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataCek.getTblBankAccountByReceiverBankAccount().getTblBank().getIdbank()));
                        list.add(new DataTransactionSettlement(reservationPayment, null, null, dataCek, null));
                        break;
                    case 5:    //Giro = '5'
                        TblReservationPaymentWithCekGiro dataGiro = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithCekGiroByIDReservationPayment(reservationPayment.getIdpayment());
                        dataGiro.setTblReservationPayment(reservationPayment);
                        dataGiro.setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataGiro.getTblBank().getIdbank()));
                        dataGiro.setTblBankAccountByReceiverBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount()));
                        dataGiro.getTblBankAccountByReceiverBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataGiro.getTblBankAccountByReceiverBankAccount().getTblBank().getIdbank()));
                        list.add(new DataTransactionSettlement(reservationPayment, null, null, dataGiro, null));
                        break;
                    case 6:    //Travel Agent = '6'
                        TblReservationPaymentWithGuaranteePayment dataTravelAgent = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithGuaranteePaymentByIDReservationPayment(reservationPayment.getIdpayment());
                        if (!dataTravelAgent.getIsDebt()) {
                            dataTravelAgent.setTblReservationPayment(reservationPayment);
                            dataTravelAgent.setTblPartner(parentController.getFFinanceTransactionManager().getDataPartner(dataTravelAgent.getTblPartner().getIdpartner()));
                            dataTravelAgent.setTblBankAccountBySenderBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataTravelAgent.getTblBankAccountBySenderBankAccount().getIdbankAccount()));
                            dataTravelAgent.getTblBankAccountBySenderBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataTravelAgent.getTblBankAccountBySenderBankAccount().getTblBank().getIdbank()));
                            dataTravelAgent.setTblBankAccountByReceiverBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataTravelAgent.getTblBankAccountByReceiverBankAccount().getIdbankAccount()));
                            dataTravelAgent.getTblBankAccountByReceiverBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataTravelAgent.getTblBankAccountByReceiverBankAccount().getTblBank().getIdbank()));
                            list.add(new DataTransactionSettlement(reservationPayment, null, null, null, dataTravelAgent));
                        }
                        break;
                    case 7:    //Guarantee Letter (Corporate) = '7'
                        TblReservationPaymentWithGuaranteePayment dataGLCorporate = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithGuaranteePaymentByIDReservationPayment(reservationPayment.getIdpayment());
                        if (!dataGLCorporate.getIsDebt()) {
                            dataGLCorporate.setTblReservationPayment(reservationPayment);
                            dataGLCorporate.setTblPartner(parentController.getFFinanceTransactionManager().getDataPartner(dataGLCorporate.getTblPartner().getIdpartner()));
                            dataGLCorporate.setTblBankAccountBySenderBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataGLCorporate.getTblBankAccountBySenderBankAccount().getIdbankAccount()));
                            dataGLCorporate.getTblBankAccountBySenderBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataGLCorporate.getTblBankAccountBySenderBankAccount().getTblBank().getIdbank()));
                            dataGLCorporate.setTblBankAccountByReceiverBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataGLCorporate.getTblBankAccountByReceiverBankAccount().getIdbankAccount()));
                            dataGLCorporate.getTblBankAccountByReceiverBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataGLCorporate.getTblBankAccountByReceiverBankAccount().getTblBank().getIdbank()));
                            list.add(new DataTransactionSettlement(reservationPayment, null, null, null, dataGLCorporate));
                        }
                        break;
                    case 8:    //Guarantee Letter (Government) = '8'
                        TblReservationPaymentWithGuaranteePayment dataGLGovernment = parentController.getFFinanceTransactionManager().getDataReservationPaymentWithGuaranteePaymentByIDReservationPayment(reservationPayment.getIdpayment());
                        if (!dataGLGovernment.getIsDebt()) {
                            dataGLGovernment.setTblReservationPayment(reservationPayment);
                            dataGLGovernment.setTblPartner(parentController.getFFinanceTransactionManager().getDataPartner(dataGLGovernment.getTblPartner().getIdpartner()));
                            dataGLGovernment.setTblBankAccountBySenderBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataGLGovernment.getTblBankAccountBySenderBankAccount().getIdbankAccount()));
                            dataGLGovernment.getTblBankAccountBySenderBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataGLGovernment.getTblBankAccountBySenderBankAccount().getTblBank().getIdbank()));
                            dataGLGovernment.setTblBankAccountByReceiverBankAccount(parentController.getFFinanceTransactionManager().getDataBankAccount(dataGLGovernment.getTblBankAccountByReceiverBankAccount().getIdbankAccount()));
                            dataGLGovernment.getTblBankAccountByReceiverBankAccount().setTblBank(parentController.getFFinanceTransactionManager().getDataBank(dataGLGovernment.getTblBankAccountByReceiverBankAccount().getTblBank().getIdbank()));
                            list.add(new DataTransactionSettlement(reservationPayment, null, null, null, dataGLGovernment));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return FXCollections.observableArrayList(list);
    }
    
    public class DataTransactionSettlement {
        
        private final ObjectProperty<TblReservationPayment> dataReservationPayment = new SimpleObjectProperty<>();
        
        private final ObjectProperty<TblReservationPaymentWithTransfer> dataTransfer = new SimpleObjectProperty<>();
        
        private final ObjectProperty<TblReservationPaymentWithBankCard> dataBankCard = new SimpleObjectProperty<>();
        
        private final ObjectProperty<TblReservationPaymentWithCekGiro> dataCekGiro = new SimpleObjectProperty<>();
        
        private final ObjectProperty<TblReservationPaymentWithGuaranteePayment> dataGuaranteeLetter = new SimpleObjectProperty<>();
        
        public DataTransactionSettlement(
                TblReservationPayment dataReservationPayment,
                TblReservationPaymentWithTransfer dataTransfer,
                TblReservationPaymentWithBankCard dataBankCard,
                TblReservationPaymentWithCekGiro dataCekGiro,
                TblReservationPaymentWithGuaranteePayment dataGuaranteeLetter) {
            this.dataReservationPayment.set(dataReservationPayment);
            this.dataTransfer.set(dataTransfer);
            this.dataBankCard.set(dataBankCard);
            this.dataCekGiro.set(dataCekGiro);
            this.dataGuaranteeLetter.set(dataGuaranteeLetter);
        }
        
        public ObjectProperty<TblReservationPayment> dataReservationPaymentProperty() {
            return dataReservationPayment;
        }
        
        public TblReservationPayment getDataReservationPayment() {
            return dataReservationPaymentProperty().get();
        }
        
        public void setDataReservationPayment(TblReservationPayment dataReservationPayment) {
            dataReservationPaymentProperty().set(dataReservationPayment);
        }
        
        public ObjectProperty<TblReservationPaymentWithTransfer> dataTransferProperty() {
            return dataTransfer;
        }
        
        public TblReservationPaymentWithTransfer getDataTransfer() {
            return dataTransferProperty().get();
        }
        
        public void setDataTransfer(TblReservationPaymentWithTransfer dataTransfer) {
            dataTransferProperty().set(dataTransfer);
        }
        
        public ObjectProperty<TblReservationPaymentWithBankCard> dataBankCardProperty() {
            return dataBankCard;
        }
        
        public TblReservationPaymentWithBankCard getDataBankCard() {
            return dataBankCardProperty().get();
        }
        
        public void setDataBankCard(TblReservationPaymentWithBankCard dataBankCard) {
            dataBankCardProperty().set(dataBankCard);
        }
        
        public ObjectProperty<TblReservationPaymentWithCekGiro> dataCekGiroProperty() {
            return dataCekGiro;
        }
        
        public TblReservationPaymentWithCekGiro getDataCekGiro() {
            return dataCekGiroProperty().get();
        }
        
        public void setDataCekGiro(TblReservationPaymentWithCekGiro dataCekGiro) {
            dataCekGiroProperty().set(dataCekGiro);
        }
        
        public ObjectProperty<TblReservationPaymentWithGuaranteePayment> dataGuaranteeLetterProperty() {
            return dataGuaranteeLetter;
        }
        
        public TblReservationPaymentWithGuaranteePayment getDataGuaranteeLetter() {
            return dataGuaranteeLetterProperty().get();
        }
        
        public void setDataGuaranteeLetter(TblReservationPaymentWithGuaranteePayment dataGuaranteeLetter) {
            dataGuaranteeLetterProperty().set(dataGuaranteeLetter);
        }
        
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private void dataTransactionSettleHandle() {
        if (tableDataTransactionSettlement.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin men-settle data ini?", "");
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                DataTransactionSettlement dummySelectedData = (DataTransactionSettlement) tableDataTransactionSettlement.getTableView().getSelectionModel().getSelectedItem();
                dummySelectedData.setDataReservationPayment(new TblReservationPayment(dummySelectedData.getDataReservationPayment()));
                TblBankAccount customerBankAccount = null;
                TblCompanyBalanceBankAccount companyBalanceBankAccount = null;
                BigDecimal paymentCharge = new BigDecimal("0");
                BigDecimal paymentDiscount = new BigDecimal("0");
                if (dummySelectedData.getDataTransfer() != null) {    //transfer
                    customerBankAccount = dummySelectedData.getDataTransfer().getTblBankAccountBySenderBankAccount();
                    companyBalanceBankAccount = parentController.getFFinanceTransactionManager().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //hotel balance = '1'
                            dummySelectedData.getDataTransfer().getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                }
                if (dummySelectedData.getDataBankCard() != null) {    //bank card
                    customerBankAccount = null;
                    companyBalanceBankAccount = parentController.getFFinanceTransactionManager().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //hotel balance = '1'
                            dummySelectedData.getDataBankCard().getTblBankAccount().getIdbankAccount());
                    paymentDiscount = paymentDiscount.add(dummySelectedData.getDataBankCard().getPaymentDiscount());
                    paymentCharge = paymentCharge.add(dummySelectedData.getDataBankCard().getPaymentCharge());
                }
                if (dummySelectedData.getDataCekGiro() != null) { //cek/giro
                    customerBankAccount = dummySelectedData.getDataCekGiro().getTblBankAccountBySenderBankAccount();
                    companyBalanceBankAccount = parentController.getFFinanceTransactionManager().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //hotel balance = '1'
                            dummySelectedData.getDataCekGiro().getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                }
                if (dummySelectedData.getDataGuaranteeLetter() != null) { //guarantee letter
                    customerBankAccount = dummySelectedData.getDataGuaranteeLetter().getTblBankAccountBySenderBankAccount();
                    companyBalanceBankAccount = parentController.getFFinanceTransactionManager().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //hotel balance = '1'
                            dummySelectedData.getDataGuaranteeLetter().getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                }
                //settle data transaction
                if (parentController.getFFinanceTransactionManager().updateSettleDataTransaction(
                        dummySelectedData.getDataReservationPayment(),
                        customerBankAccount,
                        companyBalanceBankAccount,
                        paymentCharge,
                        paymentDiscount
                )) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh data from table sytem log
                    tableDataTransactionSettlement.getTableView().setItems(loadAllDataTransactionSettlement());
                    //refresh data form input
                    setSelectedDataToInputForm();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(parentController.getFFinanceTransactionManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }
    
    public void setSelectedDataToInputForm() {
        lblDetailTransaction.setText("");
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form
        initFormDataTransactionSettlementDetail();

        //refresh data form input
        setSelectedDataToInputForm();
    }
    
    public TransactionSettlementController(FeatureFinanceTransactionController parentController) {
        this.parentController = parentController;
    }
    
    private final FeatureFinanceTransactionController parentController;
    
}
