/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_purchasing.report_receiving_po;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMemorandumInvoiceBonusType;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanPenerimaanPO;
import hotelfx.helper.PrintModel.ClassPrintLaporanPenerimaanPODetail;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_report_purchasing.FeatureReportPurchasingController;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class ReportReceivingPOController implements Initializable{
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbCodePO;
    @FXML
    private AnchorPane ancCbpCodePO;
    private JFXCComboBoxTablePopup<TblPurchaseOrder>cbpCodePO;
    @FXML
    private JFXCheckBox chbSupplier;
    @FXML
    private AnchorPane ancCbpSupplier;
    private JFXCComboBoxTablePopup<TblSupplier>cbpSupplier;
    @FXML
    private JFXCheckBox chbItem;
    @FXML
    private AnchorPane ancCbpItem;
    private JFXCComboBoxTablePopup<TblItem>cbpItem;
    @FXML
    private JFXCheckBox chbBonus;
    @FXML
    private AnchorPane ancCbpBonus;
    private JFXCComboBoxTablePopup<ClassMemorandumInvoiceBonusType>cbpBonusType;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane reportReceivingPOLayout;
    
   private void initFormReportReceivingPO(){
      initDataPopup();
      btnShow.setOnAction((e)->{
         reportReceivingPrintHandle(); 
      });
      
       cbpCodePO.setVisible(false);
       cbpSupplier.setVisible(false);
       cbpItem.setVisible(false);
       cbpBonusType.setVisible(false);
       
       chbCodePO.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpCodePO.setVisible(newVal==true);
          cbpCodePO.setValue(null);
       });
       
       chbSupplier.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpSupplier.setVisible(newVal==true);
          cbpSupplier.setValue(null);
       });
       
       chbItem.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpItem.setValue(null);
          cbpItem.setVisible(newVal==true);
       });
       
       chbBonus.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpBonusType.setValue(null);
          cbpBonusType.setVisible(newVal==true);
       });
        
        JRViewer jrView = new JRViewer(null);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(jrView);
        reportReceivingPOLayout(swingNode);
        
        initDateCalendar();
   }
   
   private void initDateCalendar(){
     ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
     ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
   }
   
   private void initDataPopup(){
     TableView<TblPurchaseOrder>tblPO = new TableView();
     TableColumn<TblPurchaseOrder,String>codePO = new TableColumn("Kode PO");
     codePO.setMinWidth(120);
     codePO.setCellValueFactory(cellData -> cellData.getValue().codePoProperty());
     TableColumn<TblPurchaseOrder,String>supplier = new TableColumn("Supplier");
     supplier.setMinWidth(200);
     supplier.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder,String>param)
     ->Bindings.createStringBinding(()->param.getValue().getTblSupplier().getSupplierName(),param.getValue().tblSupplierProperty()));
     tblPO.getColumns().addAll(codePO,supplier);
     ObservableList<TblPurchaseOrder>listPOItems = FXCollections.observableArrayList(loadAllDataPO());
     cbpCodePO = new JFXCComboBoxTablePopup(TblPurchaseOrder.class,tblPO,listPOItems,"","Nomor PO",false,500,300);
     cbpCodePO.setLabelFloat(false);
     
     TableView<TblSupplier>tblSupplier = new TableView();
     TableColumn<TblSupplier,String>codeSupplier = new TableColumn("Kode Supplier");
     codeSupplier.setMinWidth(120);
     codeSupplier.setCellValueFactory(cellData -> cellData.getValue().codeSupplierProperty());
     TableColumn<TblSupplier,String>nameSupplier = new TableColumn("Nama Supplier");
     nameSupplier.setMinWidth(200);
     nameSupplier.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
     tblSupplier.getColumns().addAll(codeSupplier,nameSupplier);
     ObservableList<TblSupplier>listSupplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
     cbpSupplier = new JFXCComboBoxTablePopup(TblSupplier.class,tblSupplier,listSupplierItems,"","Supplier",false,500,300);
     cbpSupplier.setLabelFloat(false);
     
     TableView<TblItem>tblItem = new TableView();
     TableColumn<TblItem,String>codeItem = new TableColumn("Kode Barang");
     codeItem.setMinWidth(120);
     codeItem.setCellValueFactory(cellData->cellData.getValue().codeItemProperty());
     TableColumn<TblItem,String>nameItem = new TableColumn("Nama Barang");
     nameItem.setMinWidth(200);
     nameItem.setCellValueFactory(cellData ->cellData.getValue().itemNameProperty());
     tblItem.getColumns().addAll(codeItem,nameItem);
     ObservableList<TblItem>listItem = FXCollections.observableArrayList(loadAllDataItem());
     cbpItem = new JFXCComboBoxTablePopup(TblItem.class,tblItem,listItem,"","Barang",false,500,300);
     cbpItem.setLabelFloat(false);
     
     TableView<ClassMemorandumInvoiceBonusType>tblBonusType = new TableView();
     TableColumn<ClassMemorandumInvoiceBonusType,String>bonusType = new TableColumn("Tipe Bonus");
     bonusType.setMinWidth(180);
     bonusType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
     tblBonusType.getColumns().addAll(bonusType);
     ObservableList<ClassMemorandumInvoiceBonusType>listBonusTypeItems = FXCollections.observableArrayList(loadAllDataBonusType());
     cbpBonusType = new JFXCComboBoxTablePopup(ClassMemorandumInvoiceBonusType.class,tblBonusType,listBonusTypeItems,"","Tipe Bonus",false,500,300);
     cbpBonusType.setLabelFloat(false);
     
      ancCbpCodePO.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpCodePO,0.0);
      AnchorPane.setLeftAnchor(cbpCodePO,0.0);
      AnchorPane.setRightAnchor(cbpCodePO,0.0);
      AnchorPane.setTopAnchor(cbpCodePO,0.0);
      ancCbpCodePO.getChildren().add(cbpCodePO);
      
      ancCbpSupplier.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpSupplier,0.0);
      AnchorPane.setLeftAnchor(cbpSupplier,0.0);
      AnchorPane.setRightAnchor(cbpSupplier,0.0);
      AnchorPane.setTopAnchor(cbpSupplier,0.0);
      ancCbpSupplier.getChildren().add(cbpSupplier);
      
      ancCbpItem.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpItem,0.0);
      AnchorPane.setLeftAnchor(cbpItem,0.0);
      AnchorPane.setRightAnchor(cbpItem,0.0);
      AnchorPane.setTopAnchor(cbpItem,0.0);
      ancCbpItem.getChildren().add(cbpItem);
      
      ancCbpBonus.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpBonusType,0.0);
      AnchorPane.setLeftAnchor(cbpBonusType,0.0);
      AnchorPane.setRightAnchor(cbpBonusType,0.0);
      AnchorPane.setTopAnchor(cbpBonusType,0.0);
      ancCbpBonus.getChildren().add(cbpBonusType);
   }
   
   private List<TblPurchaseOrder>loadAllDataPO(){
     List<TblPurchaseOrder>list = parentController.getFReportManager().getAllDataPurchaseOrder();
     return list;
   }
   
   private List<TblSupplier>loadAllDataSupplier(){
     List<TblSupplier>list = parentController.getFReportManager().getAllDataSupplier();
     return list;
   }
   
   private List<TblItem>loadAllDataItem(){
     List<TblItem>list = parentController.getFReportManager().getAllDataItem();
     return list;
   }
   
   private List<ClassMemorandumInvoiceBonusType>loadAllDataBonusType(){
     List<ClassMemorandumInvoiceBonusType>list = new ArrayList();
     ClassMemorandumInvoiceBonusType bonus = new ClassMemorandumInvoiceBonusType();
     bonus.setIdtype(1);
     bonus.setTypeName("Bonus");
     
     ClassMemorandumInvoiceBonusType nonBonus = new ClassMemorandumInvoiceBonusType();
     nonBonus.setIdtype(2);
     nonBonus.setTypeName("Non-Bonus");
     
     list.add(bonus);
     list.add(nonBonus);
     
     return list;
   }
   
   private void reportReceivingPrintHandle(){
      Date startDate = null;
      Date endDate = null;
      TblPurchaseOrder po = null;
      TblSupplier supplier = null;
      TblItem item = null;
      ClassMemorandumInvoiceBonusType bonusType = null;
      SwingNode swingNode = new SwingNode();
      
      List<TblPurchaseOrder>list = new ArrayList();
      
       if(dpStartDate!=null && dpEndDate!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
        // list = parentController.getFReportManager().getAllDataPurchaseOrder(null, null)
       }
       
       if(chbCodePO.isSelected()){
          if(cbpCodePO.getValue()!=null){
             po = cbpCodePO.getValue();
          }
       }
       
       if(chbSupplier.isSelected()){
          if(cbpSupplier.getValue()!=null){
             supplier = cbpSupplier.getValue();
          }
       }
       
       if(chbItem.isSelected()){
          if(cbpItem.getValue()!=null){
             item = cbpItem.getValue();
          }
       }
       
       if(chbBonus.isSelected()){
           if(cbpBonusType.getValue()!=null){
             bonusType = cbpBonusType.getValue();
           } 
       }
       
       list = parentController.getFReportManager().getAllDataPurchaseOrder(startDate, endDate,po,supplier);
       swingNode = printReportReceivingPO(list,startDate,endDate,item,bonusType);
       reportReceivingPOLayout(swingNode);
   }
   
   private void reportReceivingPOLayout(SwingNode swingNode){
     reportReceivingPOLayout.getChildren().clear();
     AnchorPane.setBottomAnchor(swingNode,15.0);
     AnchorPane.setLeftAnchor(swingNode,15.0);
     AnchorPane.setRightAnchor(swingNode,15.0);
     AnchorPane.setTopAnchor(swingNode,0.0);
     reportReceivingPOLayout.getChildren().add(swingNode);
   }
   
   private SwingNode printReportReceivingPO(List<TblPurchaseOrder>list,Date startDate,Date endDate,TblItem item,ClassMemorandumInvoiceBonusType bonusType){
       List<ClassPrintLaporanPenerimaanPO>listLaporanPenerimaanPO = new ArrayList();
       ClassPrintLaporanPenerimaanPO laporanPenerimaanPO = new ClassPrintLaporanPenerimaanPO();
       laporanPenerimaanPO.setPeriode(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate));
       laporanPenerimaanPO.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());      
       ClassPrintLaporanPenerimaanPODetail laporanPenerimaanPODetail  = null;
       List<ClassPrintLaporanPenerimaanPODetail>listLaporanPenerimaanPODetail = new ArrayList();
       for(TblPurchaseOrder getPO : list){
            List<TblMemorandumInvoiceDetail>listMIDetail = new ArrayList();
           List<TblPurchaseOrderDetail>listPODetail = new ArrayList(); 
           
           if(bonusType==null){
              listMIDetail = parentController.getFReportManager().getAllDataMIDetail(getPO,item);
              listPODetail = parentController.getFReportManager().getAllDataPODetail(getPO,item);
           }
           else{
               if(bonusType.getIdtype()==1){
                 listMIDetail = parentController.getFReportManager().getAllDataMIDetail(getPO,item);
               }
               else{
                 listPODetail = parentController.getFReportManager().getAllDataPODetail(getPO,item);
               }
           }
          // List<TblMemorandumInvoiceDetail>listMIDetail = parentController.getFReportManager().getAllDataMIDetail(getPO,item);
         //  System.out.println("hsl MI detail :"+listMIDetail.size());
         //  List<TblPurchaseOrderDetail>listPODetail = parentController.getFReportManager().getAllDataPODetail(getPO,item);
         //  System.out.println("hsl PO detail :"+listPODetail.size());
           for(TblMemorandumInvoiceDetail miDetail : listMIDetail){
               if(miDetail.getTblItem()!=null){
                 laporanPenerimaanPODetail = new ClassPrintLaporanPenerimaanPODetail();
                 laporanPenerimaanPODetail.setTanggalPO(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(miDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getPodate()));
                 laporanPenerimaanPODetail.setKodeBarang(miDetail.getTblItem().getCodeItem());
                 laporanPenerimaanPODetail.setNamaBarang(miDetail.getTblItem().getItemName());
                 laporanPenerimaanPODetail.setKodePO(miDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
                 laporanPenerimaanPODetail.setKodeSupplier(miDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getTblSupplier().getCodeSupplier());
                 laporanPenerimaanPODetail.setNamaSupplier(miDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getTblSupplier().getSupplierName());
                 laporanPenerimaanPODetail.setQtyPO(new BigDecimal(0));
                 laporanPenerimaanPODetail.setQtyTerima(miDetail.getItemQuantity());
                 laporanPenerimaanPODetail.setQtyRetur(new BigDecimal(0));
                 laporanPenerimaanPODetail.setBalance(laporanPenerimaanPODetail.getQtyTerima().subtract(laporanPenerimaanPODetail.getQtyRetur()).subtract(laporanPenerimaanPODetail.getQtyPO()));
                 laporanPenerimaanPODetail.setBalancePercent(BigDecimal.ZERO);
                 laporanPenerimaanPODetail.setSatuan(miDetail.getTblItem().getTblUnit().getUnitName());
                 laporanPenerimaanPODetail.setStatus("Bonus");
                 listLaporanPenerimaanPODetail.add(laporanPenerimaanPODetail);
               }
           }
          
           for(TblPurchaseOrderDetail poDetail : listPODetail){
            // System.out.println("tgl po : "+poDetail.getTblPurchaseOrder().getPodate());
             laporanPenerimaanPODetail = new ClassPrintLaporanPenerimaanPODetail();
             laporanPenerimaanPODetail.setTanggalPO(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(poDetail.getTblPurchaseOrder().getPodate()));
             laporanPenerimaanPODetail.setKodeBarang(poDetail.getTblSupplierItem().getTblItem().getCodeItem());
             laporanPenerimaanPODetail.setKodePO(poDetail.getTblPurchaseOrder().getCodePo());
             laporanPenerimaanPODetail.setKodeSupplier(poDetail.getTblPurchaseOrder().getTblSupplier().getCodeSupplier());
             laporanPenerimaanPODetail.setNamaBarang(poDetail.getTblSupplierItem().getTblItem().getItemName());
             laporanPenerimaanPODetail.setNamaSupplier(poDetail.getTblPurchaseOrder().getTblSupplier().getSupplierName());
             laporanPenerimaanPODetail.setQtyPO(poDetail.getItemQuantity());
             laporanPenerimaanPODetail.setQtyTerima(getReceiving(poDetail.getTblPurchaseOrder()));
             laporanPenerimaanPODetail.setQtyRetur(getRetur(poDetail.getTblPurchaseOrder()));
             laporanPenerimaanPODetail.setBalance(laporanPenerimaanPODetail.getQtyTerima().subtract(laporanPenerimaanPODetail.getQtyRetur()).subtract(laporanPenerimaanPODetail.getQtyPO()));
             laporanPenerimaanPODetail.setBalancePercent((laporanPenerimaanPODetail.getBalance().divide(laporanPenerimaanPODetail.getQtyPO(),4,RoundingMode.HALF_UP)).multiply(new BigDecimal(100)));
             laporanPenerimaanPODetail.setSatuan(poDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName());
             laporanPenerimaanPODetail.setStatus("Non Bonus");
             listLaporanPenerimaanPODetail.add(laporanPenerimaanPODetail);
           }
       }
       laporanPenerimaanPO.setListLaporanPenerimaanPODetail(listLaporanPenerimaanPODetail);
       listLaporanPenerimaanPO.add(laporanPenerimaanPO);
       return ClassPrinter.printLaporanPenerimaanPO(listLaporanPenerimaanPO);
    } 
    
   private BigDecimal getReceiving(TblPurchaseOrder po){
       List<TblMemorandumInvoiceDetail>list = parentController.getFReportManager().getAllDataMIDetail(po, null);
       BigDecimal qtyMI = new BigDecimal(0);
       for(TblMemorandumInvoiceDetail getMIDetail : list){
           if(getMIDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo()==po.getIdpo()){
               if(getMIDetail.getTblItem()==null && getMIDetail.getTblSupplierItem()!=null){
                  qtyMI = qtyMI.add(getMIDetail.getItemQuantity());
               }
            }
       }
      return qtyMI;
    }
   
    private BigDecimal getRetur(TblPurchaseOrder po){
      List<TblReturDetail>list = parentController.getFReportManager().getAllDataReturDetail(null, po);
      BigDecimal qtyRetur = new BigDecimal(0);
       for(TblReturDetail getReturDetail : list){
           if(getReturDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo()==po.getIdpo()){
             qtyRetur = qtyRetur.add(getReturDetail.getItemQuantity());
           }
       }
      return qtyRetur;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportReceivingPO();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportReceivingPOController(FeatureReportPurchasingController parentController){
      this.parentController = parentController;
    }
    private final FeatureReportPurchasingController parentController;
}
