/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.cash_flow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassDataCompanyBalanceTransferReceived;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintLaporanTransaksiArusKas;
import hotelfx.helper.PrintModel.ClassPrintLaporanTransaksiArusKasDetail;
import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.view.feature_balance.FeatureBalanceController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class CashFlowController implements Initializable{
    @FXML
    private AnchorPane ancTableCashFlow;
    private ClassTableWithControl tblCashFlow;
    
    @FXML
    private AnchorPane ancCbpBalanceType;
    private JFXCComboBoxTablePopup<TblCompanyBalance>cbpBalanceType;
    
    @FXML
    private AnchorPane ancCbpBalanceBankAccount;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount>cbpBalanceBankAccount;
    
    
    @FXML
    private JFXDatePicker dpStartPeriode;
    
    @FXML
    private JFXDatePicker dpEndPeriode;
    
    @FXML
    private JFXButton btnShow;
    
    private void initForm(){
      initDataPopup();
      btnShow.setOnAction((e)->{
           if(checkDataInput()){
               TblCompanyBalanceBankAccount balanceBankAccount = null;
               ObservableList<ClassDataCompanyBalanceTransferReceived>list = FXCollections.observableArrayList();
               if(cbpBalanceBankAccount.getValue()!=null){
                 balanceBankAccount = cbpBalanceBankAccount.getValue();
                 list = FXCollections.observableArrayList(loadAllDataCompanyBalanceCashFlow(cbpBalanceType.getValue(),balanceBankAccount.getTblBankAccount(),Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue())));
                }else{
                 list = FXCollections.observableArrayList(loadAllDataCompanyBalanceCashFlow(cbpBalanceType.getValue(),null,Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue())));
               }
             tblCashFlow.getTableView().setItems(list);
           }
           else{
             ClassMessage.showWarningInputDataMessage(errDataInput, null);
           }   
      });
      
      cbpBalanceType.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.getIdbalance()==1){
             ancCbpBalanceBankAccount.setVisible(true);
           }
           else{
             ancCbpBalanceBankAccount.setVisible(false);
           }
           cbpBalanceBankAccount.setValue(null);
      });
      ancCbpBalanceBankAccount.setVisible(false);
      initImportantField();
      initDatePattern();
    }
    
    private void initImportantField(){
      ClassViewSetting.setImportantField(cbpBalanceBankAccount,cbpBalanceType,dpStartPeriode,dpEndPeriode);
    }
    
    private void initDatePattern(){
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartPeriode,dpEndPeriode);
    }
    
    private void initDataPopup(){
    //BALANCE TYPE  
       TableView<TblCompanyBalance>tblBalanceType = new TableView();
       TableColumn<TblCompanyBalance,String>balanceTypeName = new TableColumn("Kas");
       balanceTypeName.setMinWidth(120);
       balanceTypeName.setCellValueFactory(cellData -> cellData.getValue().balanceNameProperty());
       tblBalanceType.getColumns().addAll(balanceTypeName);
       
       ObservableList<TblCompanyBalance>balanceTypeList = FXCollections.observableArrayList(loadAllDataCompanyBalance());
       cbpBalanceType = new JFXCComboBoxTablePopup(TblCompanyBalance.class,tblBalanceType,balanceTypeList,"","Kas *",false,400,300);
       
    //BALANCE BANK ACCOUNT
       TableView<TblCompanyBalanceBankAccount>tblBalanceBankAccount = new TableView();
       TableColumn<TblCompanyBalanceBankAccount,String>codeBankAccount = new TableColumn("No.Rekening");
       codeBankAccount.setMinWidth(120);
       codeBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getCodeBankAccount(),param.getValue().tblBankAccountProperty()));
       TableColumn<TblCompanyBalanceBankAccount,String>nameBank = new TableColumn("Bank");
       nameBank.setMinWidth(140);
       nameBank.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getTblBank().getBankName(),param.getValue().getTblBankAccount().tblBankProperty()));
       TableColumn<TblCompanyBalanceBankAccount,String>holderBankAccount = new TableColumn("Pemegang Rekening");
       holderBankAccount.setMinWidth(160);
       holderBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getBankAccountHolderName(),param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
       tblBalanceBankAccount.getColumns().addAll(codeBankAccount,holderBankAccount,nameBank);
       ObservableList<TblCompanyBalanceBankAccount>companyBalanceBankAccountList = FXCollections.observableArrayList(parentController.getFBalanceManager().getAllDataCompanyBalanceBankAccount((long)1));
       cbpBalanceBankAccount = new JFXCComboBoxTablePopup(TblCompanyBalanceBankAccount.class,tblBalanceBankAccount,companyBalanceBankAccountList,"","No. Rekening *",true,500,300);
       
       ancCbpBalanceType.getChildren().clear();
       AnchorPane.setTopAnchor(cbpBalanceType,0.0);
       AnchorPane.setBottomAnchor(cbpBalanceType,0.0);
       AnchorPane.setLeftAnchor(cbpBalanceType,0.0);
       AnchorPane.setRightAnchor(cbpBalanceType,0.0);
       ancCbpBalanceType.getChildren().add(cbpBalanceType);
       
       ancCbpBalanceBankAccount.getChildren().clear();
       AnchorPane.setTopAnchor(cbpBalanceBankAccount,0.0);
       AnchorPane.setBottomAnchor(cbpBalanceBankAccount,0.0);
       AnchorPane.setLeftAnchor(cbpBalanceBankAccount,0.0);
       AnchorPane.setRightAnchor(cbpBalanceBankAccount,0.0);
       ancCbpBalanceBankAccount.getChildren().add(cbpBalanceBankAccount);
    }
    
    private List<TblCompanyBalance>loadAllDataCompanyBalance(){
      return parentController.getFBalanceManager().getAllDataCompanyBalance();
    }
    
    private void refreshDataPopup(){
       ObservableList<TblCompanyBalance>balanceTypeList = FXCollections.observableArrayList(loadAllDataCompanyBalance());
       cbpBalanceType.setItems(balanceTypeList);
       
       ObservableList<TblCompanyBalanceBankAccount>companyBalanceBankAccountList = FXCollections.observableArrayList(parentController.getFBalanceManager().getAllDataCompanyBalanceBankAccount((long)1));
       cbpBalanceBankAccount.setItems(companyBalanceBankAccountList);
    }
    
    private void initTableDataCashFlow(){
       setTableDataCashFlow();
       initTableControlDataCashFlow();
       ancTableCashFlow.getChildren().clear();
       AnchorPane.setTopAnchor(tblCashFlow,15.0);
       AnchorPane.setBottomAnchor(tblCashFlow,15.0);
       AnchorPane.setLeftAnchor(tblCashFlow,15.0);
       AnchorPane.setRightAnchor(tblCashFlow,15.0);
       ancTableCashFlow.getChildren().add(tblCashFlow);
    }
    
    private void setTableDataCashFlow(){
      TableView<ClassDataCompanyBalanceTransferReceived>tableCashFlow = new TableView();
      TableColumn<ClassDataCompanyBalanceTransferReceived,String>transactionDate = new TableColumn("Tanggal\nTransaksi");
      transactionDate.setMinWidth(100);
      transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()-> param.getValue().getCompanyBalanceCashFlow().getHistoryDate()!=null ? (new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(param.getValue().getCompanyBalanceCashFlow().getHistoryDate())) : "-",param.getValue().getCompanyBalanceCashFlow().historyDateProperty()));
      TableColumn<ClassDataCompanyBalanceTransferReceived,String>descriptionBalance = new TableColumn("Deskripsi");
      descriptionBalance.setMinWidth(300);
      descriptionBalance.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getNoteBalace(),param.getValue().noteBalaceProperty()));
    /*TableColumn<LogCompanyBalanceCashFlow,String>balanceReceived = new TableColumn("Tujuan");
      balanceReceived.setMinWidth(140);
      balanceReceived.setCellValueFactory((TableColumn.CellDataFeatures<LogCompanyBalanceCashFlow,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblCompanyBalanceByIdreceiverCompanyBalance()!=null ? param.getValue().getTblCompanyBalanceByIdreceiverCompanyBalance().getBalanceName() : "-",param.getValue().tblCompanyBalanceByIdreceiverCompanyBalanceProperty()));
      balanceType.getColumns().addAll(balanceSender,balanceReceived); */
      
     TableColumn<ClassDataCompanyBalanceTransferReceived,String>balanceNominalTransfer = new TableColumn("Keluar");
      balanceNominalTransfer.setMinWidth(120);
      balanceNominalTransfer.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataCompanyBalanceTransferReceived,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getIsTransfer() ? ClassFormatter.currencyFormat.cFormat(param.getValue().getCompanyBalanceCashFlow().getTransferNominal()):"0",param.getValue().getCompanyBalanceCashFlow().transferNominalProperty()));
      TableColumn<ClassDataCompanyBalanceTransferReceived,String>balanceNominalReceived = new TableColumn("Masuk");
      balanceNominalReceived.setMinWidth(120);
      balanceNominalReceived.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataCompanyBalanceTransferReceived,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getIsReceived() ? ClassFormatter.currencyFormat.cFormat(param.getValue().getCompanyBalanceCashFlow().getTransferNominal()):"0",param.getValue().getCompanyBalanceCashFlow().transferNominalProperty()));
      TableColumn<ClassDataCompanyBalanceTransferReceived,String>balanceNominal = new TableColumn("Nominal");
      balanceNominal.getColumns().addAll(balanceNominalReceived,balanceNominalTransfer);
    /*  TableColumn<LogCompanyBalanceCashFlow,String>balanceBankAccountTransfer = new TableColumn("Keluar");
      balanceBankAccountTransfer.setMinWidth(140);
      TableColumn<LogCompanyBalanceCashFlow,String>balanceBankAccountReceived = new TableColumn("Masuk");
      balanceBankAccountReceived.setMinWidth(140);
    /*  TableColumn<LogCompanyBalanceCashFlow,String>balanceBankAccount = new TableColumn("Akun Bank");
      balanceBankAccount.getColumns().addAll(balanceBankAccountTransfer,balanceBankAccountReceived); */
      TableColumn<ClassDataCompanyBalanceTransferReceived,String>balance = new TableColumn("Saldo");
      balance.setMinWidth(120);
      balance.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getBalance()),param.getValue().balanceProperty()));
      TableColumn<ClassDataCompanyBalanceTransferReceived,String>noteBalance = new TableColumn("Keterangan");
      noteBalance.setMinWidth(160);
      noteBalance.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataCompanyBalanceTransferReceived,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getCompanyBalanceCashFlow().getHistoryNote() != null ? (param.getValue().getCompanyBalanceCashFlow().getHistoryNote() + "\n") : ""
              + (param.getValue().getCompanyBalanceCashFlow().getHistoryStystemNote() != null ? param.getValue().getCompanyBalanceCashFlow().getHistoryStystemNote() : ""),
              param.getValue().getCompanyBalanceCashFlow().historyNoteProperty()));
      tableCashFlow.getColumns().addAll(transactionDate,descriptionBalance,balanceNominal,balance,noteBalance);
      tblCashFlow = new ClassTableWithControl(tableCashFlow);
     // balanceNominalReceived.setMinWidth(120);
     // balanceNominalReceived.setCellValueFactory((TableColumn.CellDataFeatures<LogCompanyBalanceCashFlow,String>param)
     // ->Bindings.createStringBinding(()->param.getValue().getTblCompanyBalanceByIdreceiverCompanyBalance()!=null, dependencies));
    }
    
   /* private String transferOrReceived(LogCompanyBalanceCashFlow cashFlow,TblCompanyBalance companyBalance){
       String hsl = "";
       if(cashFlow.getTblCompanyBalanceByIdsenderCompanyBalance().getIdbalance()==companyBalance.getIdbalance()){
          hsl = "Transfer";
       }
       
       if(cashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance().getIdbalance()==companyBalance.getIdbalance()){
         hsl = "Received";
       }
        return hsl;
    } */
    
    private String noteBalance(ClassDataCompanyBalanceTransferReceived cashFlow){
       String hsl = "";
       if(cashFlow.getIsTransfer()){
          hsl = cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdreceiverCbbankAccount()!=null ? ("Transfer ke :"+(cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdreceiverCompanyBalance()!=null?cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdreceiverCompanyBalance().getBalanceName() :"-")+"\n"+
             "Rekening :"+cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdreceiverCbbankAccount().getCodeBankAccount()+"-"+cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdreceiverCbbankAccount().getBankAccountHolderName()+"/"+cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdreceiverCbbankAccount().getTblBank().getBankName()):
             "Transfer ke :"+(cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdreceiverCompanyBalance()!=null ?cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdreceiverCompanyBalance().getBalanceName() :"-");
       }
       
       if(cashFlow.getIsReceived()){
         hsl =  cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdsenderCbbankAccount()!=null ? ("Terima dari :"+(cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdsenderCompanyBalance()!=null?cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdsenderCompanyBalance().getBalanceName() :"-")+"\n"+
             "Rekening :"+cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdsenderCbbankAccount().getCodeBankAccount()+"-"+cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdsenderCbbankAccount().getBankAccountHolderName()+"/"+cashFlow.getCompanyBalanceCashFlow().getTblBankAccountByIdsenderCbbankAccount().getTblBank().getBankName()):
             "Terima dari :"+(cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdsenderCompanyBalance()!=null ?cashFlow.getCompanyBalanceCashFlow().getTblCompanyBalanceByIdsenderCompanyBalance().getBalanceName() :"-");
        } 
       
      return hsl;
    }
    
    private List<ClassDataCompanyBalanceTransferReceived>loadAllDataCompanyBalanceCashFlow(TblCompanyBalance companyBalance,TblBankAccount bankAccount,Date startDate,Date endDate){      
      List<LogCompanyBalanceCashFlow>list  = parentController.getFBalanceManager().getAllDataCashFlow(companyBalance,bankAccount,startDate,endDate);
      List<ClassDataCompanyBalanceTransferReceived>newList = new ArrayList();
      ClassDataCompanyBalanceTransferReceived saldoAwal = new ClassDataCompanyBalanceTransferReceived();
      saldoAwal.setBalance(parentController.getFBalanceManager().getBalance(companyBalance, startDate,bankAccount));
      saldoAwal.setCompanyBalanceCashFlow(new LogCompanyBalanceCashFlow());
      saldoAwal.setNoteBalace("Saldo Awal");
      BigDecimal saldo  = new BigDecimal(0); 
      saldo = saldoAwal.getBalance();
      newList.add(saldoAwal);
      for(LogCompanyBalanceCashFlow cashFlow : list){     
           if(cashFlow.getTblCompanyBalanceByIdsenderCompanyBalance()!=null && cashFlow.getTblCompanyBalanceByIdsenderCompanyBalance().getIdbalance()==cbpBalanceType.getValue().getIdbalance()){
             ClassDataCompanyBalanceTransferReceived cashFlowNew = new ClassDataCompanyBalanceTransferReceived();
             cashFlowNew.setCompanyBalanceCashFlow(cashFlow);
             cashFlowNew.setIsTransfer(true);
             saldo = saldo.subtract(cashFlow.getTransferNominal());
             cashFlowNew.setBalance(saldo);
             cashFlowNew.setNoteBalace(noteBalance(cashFlowNew));
             newList.add(cashFlowNew);
            }
           
            if(cashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance()!=null && cashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance().getIdbalance()==cbpBalanceType.getValue().getIdbalance()){
             ClassDataCompanyBalanceTransferReceived cashFlowNew = new ClassDataCompanyBalanceTransferReceived();
             cashFlowNew.setCompanyBalanceCashFlow(cashFlow);
             cashFlowNew.setIsReceived(true);
             saldo = saldo.add(cashFlow.getTransferNominal());
             cashFlowNew.setBalance(saldo);
             cashFlowNew.setNoteBalace(noteBalance(cashFlowNew));
             newList.add(cashFlowNew);
            }
        }
      return newList;
    }
     
    private String balanceTotal(ClassDataCompanyBalanceTransferReceived cashFlow){
       BigDecimal debit = cashFlow.getIsReceived() ? cashFlow.getCompanyBalanceCashFlow().getTransferNominal() : new BigDecimal(0);
       BigDecimal credit = cashFlow.getIsTransfer() ? cashFlow.getCompanyBalanceCashFlow().getTransferNominal() : new BigDecimal(0);
       BigDecimal total = debit.subtract(credit);
       cashFlow.setBalance(new BigDecimal(0));
       cashFlow.setBalance(cashFlow.getBalance().add(total));
       return  ClassFormatter.currencyFormat.cFormat(cashFlow.getBalance());
    }
    
    String errDataInput;
    
    private void initTableControlDataCashFlow(){
       ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(true){
         buttonControl = new JFXButton();
         buttonControl.setText("Cetak");
         buttonControl.setOnMouseClicked((e)->{
           cashFlowPrintHandle();
         });
         buttonControls.add(buttonControl);
       }
       tblCashFlow.addButtonControl(buttonControls);
    }
    
    private void cashFlowPrintHandle(){
        String periode = new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpStartPeriode.getValue()))+"-"+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpEndPeriode.getValue()));
        List<ClassPrintLaporanTransaksiArusKas>listPrintTransaksiArusKas = new ArrayList();
        ClassPrintLaporanTransaksiArusKas printLaporanTransaksiArusKas = new ClassPrintLaporanTransaksiArusKas();
        printLaporanTransaksiArusKas.setPrintedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
        printLaporanTransaksiArusKas.setPeriode(periode);
        printLaporanTransaksiArusKas.setTipeKas(cbpBalanceType.getValue().getIdbalance()==1 ? cbpBalanceType.getValue().getBalanceName()+"("+cbpBalanceBankAccount.getValue().getTblBankAccount().getCodeBankAccount()+"-"+cbpBalanceBankAccount.getValue().getTblBankAccount().getBankAccountHolderName()+"/"+cbpBalanceBankAccount.getValue().getTblBankAccount().getTblBank().getBankName()+")" : cbpBalanceType.getValue().getBalanceName());
        String kas = cbpBalanceType.getValue().getIdbalance()==1 ? cbpBalanceType.getValue().getBalanceName()+"-"+cbpBalanceBankAccount.getValue().getTblBankAccount().getCodeBankAccount()+"-"+cbpBalanceBankAccount.getValue().getTblBankAccount().getBankAccountHolderName()+"-"+cbpBalanceBankAccount.getValue().getTblBankAccount().getTblBank().getBankName() : cbpBalanceType.getValue().getBalanceName();
        List<ClassPrintLaporanTransaksiArusKasDetail>listArusKasDetail = new ArrayList();
        for(int i = 0; i<tblCashFlow.getTableView().getItems().size();i++){
          ClassDataCompanyBalanceTransferReceived dataTransferReceived = (ClassDataCompanyBalanceTransferReceived)tblCashFlow.getTableView().getItems().get(i);
          ClassPrintLaporanTransaksiArusKasDetail printLaporanTransaksiArusKasDetail = new ClassPrintLaporanTransaksiArusKasDetail();
          printLaporanTransaksiArusKasDetail.setDeskripsi(dataTransferReceived.getNoteBalace());
          printLaporanTransaksiArusKasDetail.setKeterangan(dataTransferReceived.getCompanyBalanceCashFlow().getHistoryNote()!=null ? dataTransferReceived.getCompanyBalanceCashFlow().getHistoryNote() : "-");
          printLaporanTransaksiArusKasDetail.setNominalKeluar(dataTransferReceived.getIsTransfer()==true ? dataTransferReceived.getCompanyBalanceCashFlow().getTransferNominal() : new BigDecimal(0));
          printLaporanTransaksiArusKasDetail.setNominalMasuk(dataTransferReceived.getIsReceived()==true ? dataTransferReceived.getCompanyBalanceCashFlow().getTransferNominal(): new BigDecimal(0));
          printLaporanTransaksiArusKasDetail.setSaldo(dataTransferReceived.getBalance());
          printLaporanTransaksiArusKasDetail.setTanggalTransaksi(dataTransferReceived.getCompanyBalanceCashFlow().getHistoryDate()!=null ? new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(dataTransferReceived.getCompanyBalanceCashFlow().getHistoryDate()) : "-");
          listArusKasDetail.add(printLaporanTransaksiArusKasDetail);
        }
        printLaporanTransaksiArusKas.setListLaporanTransaksiArusKasDetail(listArusKasDetail);
        listPrintTransaksiArusKas.add(printLaporanTransaksiArusKas);
        ClassPrinter.printLaporanTransaksiArusKas(listPrintTransaksiArusKas,periode,kas);
    }
    
    private boolean checkDataInput(){
      errDataInput = "";
      boolean check = true;
       if(dpStartPeriode.getValue()==null){   
         errDataInput+="Periode Awal :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
        }
       
       if(dpEndPeriode.getValue()==null){
          errDataInput+="Periode Akhir :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(cbpBalanceType.getValue()==null){
          errDataInput+="Kas :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       else{
           if(cbpBalanceType.getValue().getIdbalance()==1){
               if(cbpBalanceBankAccount.getValue()==null){
                  errDataInput+="Akun Bank :"+ClassMessage.defaultErrorNullValueMessage+"\n";
                  check = false;
                } 
           }
       }
       
      
      return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     initForm();
     initTableDataCashFlow();
     refreshDataPopup();
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public CashFlowController(FeatureBalanceController parentController){
       this.parentController = parentController;
    }
    
    private final FeatureBalanceController parentController;
}
