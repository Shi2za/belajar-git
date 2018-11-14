/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_finance.report_finance_transaction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanPengeluaran;
import hotelfx.helper.PrintModel.ClassPrintLaporanPengeluaranDetail;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.TblHotelExpenseTransaction;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.view.feature_report_finance.FeatureReportFinanceController;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Andreas
 */
public class ReportFinanceTransactionController implements Initializable{
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbPaymentType;
    @FXML
    private AnchorPane ancPaymentType;
    private JFXCComboBoxTablePopup<RefFinanceTransactionPaymentType>cbpPaymentType;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane showReportLayout;
    
    private void initFormReportFinanceTransaction(){
       initDataPopup();
       btnShow.setOnAction((e)->{
         reportFinanceTransactionPrintHandle(); 
       });
       cbpPaymentType.setVisible(false);
       chbPaymentType.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpPaymentType.setVisible(newVal==true);
          cbpPaymentType.setValue(null);
       });
       
       SwingNode swingNode = new SwingNode();
       JRViewer jrView = new JRViewer(null);
       swingNode.setContent(jrView);
       showReportLayout(swingNode);
       
       initFieldDate();
    }
    
    private void initFieldDate(){
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
    }
    
    private void initDataPopup(){
      TableView<RefFinanceTransactionPaymentType>tblPayment = new TableView();
      TableColumn<RefFinanceTransactionPaymentType,String>paymentType = new TableColumn("Tipe Pembayaran");
      paymentType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
      tblPayment.getColumns().addAll(paymentType);
      ObservableList<RefFinanceTransactionPaymentType>paymentList = FXCollections.observableArrayList(loadAllDataPaymentType());
      cbpPaymentType = new JFXCComboBoxTablePopup(RefFinanceTransactionPaymentType.class,tblPayment,paymentList,"","Tipe Pembayaran",false,400,300);
      cbpPaymentType.setLabelFloat(false);
      
      ancPaymentType.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpPaymentType,0.0);
      AnchorPane.setLeftAnchor(cbpPaymentType,0.0);
      AnchorPane.setRightAnchor(cbpPaymentType,0.0);
      AnchorPane.setTopAnchor(cbpPaymentType,0.0);
      ancPaymentType.getChildren().add(cbpPaymentType);
    }
    
    
    private List<RefFinanceTransactionPaymentType>loadAllDataPaymentType(){
       List<RefFinanceTransactionPaymentType>list = parentController.getFReportManager().getAllDataPaymentType();
       for(int i=list.size()-1; i>=0;i--){
          RefFinanceTransactionPaymentType paymentType = list.get(i);
           if(paymentType.getIdtype()>5){
             list.remove(paymentType);
           }
           
           if(paymentType.getIdtype()==2 || paymentType.getIdtype()==3){
             list.remove(paymentType);
           }
       }
      return list;
    }
    
    private void reportFinanceTransactionPrintHandle(){
       Date startDate = null;
       Date endDate = null;
       RefFinanceTransactionPaymentType financeType = null;
       SwingNode swingNode = new SwingNode();
       
       List<TblHotelFinanceTransactionHotelPayable>listFinance = new ArrayList();
       List<TblHotelExpenseTransactionDetail>listExpense = new ArrayList();
       
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(chbPaymentType.isSelected()==true){
          if(cbpPaymentType.getValue()!=null){
             financeType = cbpPaymentType.getValue();
          }
       }
       
       listFinance = parentController.getFReportManager().getAllFinanceTransaction(startDate,endDate,financeType);
       System.out.println("list finance : "+listFinance.size());
       listExpense = parentController.getFReportManager().getAllDataExpense(startDate,endDate,financeType);
       System.out.println("list expense : "+listExpense.size());
       
      swingNode = printReportFinanceTransaction(listFinance,listExpense,startDate,endDate);
      showReportLayout(swingNode);
    }
    
    private void showReportLayout(SwingNode swingNode){
      showReportLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,5.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,0.0);
      showReportLayout.getChildren().add(swingNode);
    }
    
    private SwingNode printReportFinanceTransaction(List<TblHotelFinanceTransactionHotelPayable>listHotelPayable,List<TblHotelExpenseTransactionDetail>listHotelExpense,Date startDate,Date endDate){
       List<ClassPrintLaporanPengeluaran>listPrintLaporanPengeluaran = new ArrayList();
       ClassPrintLaporanPengeluaran printLaporanPengeluaran = new ClassPrintLaporanPengeluaran();
       printLaporanPengeluaran.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       printLaporanPengeluaran.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate) : "-");
       List<ClassPrintLaporanPengeluaranDetail>listLaporanPengeluaranDetail = new ArrayList();
       for(TblHotelFinanceTransactionHotelPayable getFinanceTransaction : listHotelPayable){
         ClassPrintLaporanPengeluaranDetail printPengeluaranDetail = new ClassPrintLaporanPengeluaranDetail();
         printPengeluaranDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getFinanceTransaction.getTblHotelFinanceTransaction().getCreateDate()));
         printPengeluaranDetail.setDeskripsi("Pembayaran : "+getFinanceTransaction.getTblHotelPayable().getRefHotelPayableType().getTypeName());
         printPengeluaranDetail.setNominalPembayaran(getFinanceTransaction.getNominalTransaction());
         printPengeluaranDetail.setTipePembayaran(getFinanceTransaction.getTblHotelFinanceTransaction().getRefFinanceTransactionPaymentType().getTypeName()+" "+(getDetailTransferCekGiro(getFinanceTransaction.getTblHotelFinanceTransaction())==null ? "":getDetailTransferCekGiro(getFinanceTransaction.getTblHotelFinanceTransaction())));
         printPengeluaranDetail.setKeterangan("No Invoice : "+getFinanceTransaction.getTblHotelPayable().getTblHotelInvoice().getCodeHotelInvoice());
         listLaporanPengeluaranDetail.add(printPengeluaranDetail);
        }
       
       for(TblHotelExpenseTransactionDetail getExpenseTransaction : listHotelExpense){
         ClassPrintLaporanPengeluaranDetail printPengeluaranDetail = new ClassPrintLaporanPengeluaranDetail();
         printPengeluaranDetail.setDeskripsi(getExpenseTransaction.getItemName());
         printPengeluaranDetail.setKeterangan(getExpenseTransaction.getTblHotelExpenseTransaction().getExpenseTransactionNote());
         printPengeluaranDetail.setNominalPembayaran(getExpenseTransaction.getItemCost());
         printPengeluaranDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getExpenseTransaction.getTblHotelExpenseTransaction().getExpenseTransactionDate()));
         printPengeluaranDetail.setTipePembayaran(getExpenseTransaction.getTblHotelExpenseTransaction().getRefFinanceTransactionPaymentType().getTypeName()+(getExpenseTransaction.getTblHotelExpenseTransaction().getRefFinanceTransactionPaymentType().getIdtype()==1?("#"+getExpenseTransaction.getTblHotelExpenseTransaction().getTblBankAccountByIdbankAccount().getCodeBankAccount()+"-"+getExpenseTransaction.getTblHotelExpenseTransaction().getTblBankAccountByIdbankAccount().getBankAccountHolderName()+" "+getExpenseTransaction.getTblHotelExpenseTransaction().getTblBankAccountByIdbankAccount().getTblBank().getBankName()):""));
         listLaporanPengeluaranDetail.add(printPengeluaranDetail);
       }
       printLaporanPengeluaran.setListLaporanPengeluaranDetail(listLaporanPengeluaranDetail);
       listPrintLaporanPengeluaran.add(printLaporanPengeluaran);
       
      return ClassPrinter.printLaporanPengeluaran(listPrintLaporanPengeluaran);
    }
    
    private String getDetailTransferCekGiro(TblHotelFinanceTransaction finance){
      List<TblHotelFinanceTransactionWithCekGiro>listCekGiro = parentController.getFReportManager().getAllDataCekGiro(finance);
      List<TblHotelFinanceTransactionWithTransfer>listTransfer = parentController.getFReportManager().getAllDataTransfer(finance);
      String hslTransaction = "";
       for(TblHotelFinanceTransactionWithCekGiro getCekGiro : listCekGiro){
         hslTransaction = "#"+getCekGiro.getCodeCekGiro();
        }
       
       for(TblHotelFinanceTransactionWithTransfer getTransfer : listTransfer){
         hslTransaction = "#"+getTransfer.getTblBankAccountBySenderBankAccount().getCodeBankAccount()+"-"+getTransfer.getTblBankAccountBySenderBankAccount().getBankAccountHolderName()+" "+getTransfer.getTblBankAccountBySenderBankAccount().getTblBank().getBankName();
       }
      return hslTransaction;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportFinanceTransaction();
      // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportFinanceTransactionController(FeatureReportFinanceController parentController){
      this.parentController = parentController;
    }
    private final FeatureReportFinanceController parentController;
}
