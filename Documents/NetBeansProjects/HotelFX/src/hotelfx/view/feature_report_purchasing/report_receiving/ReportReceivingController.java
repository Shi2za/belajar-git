/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_purchasing.report_receiving;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMemorandumInvoiceBonusType;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanPenerimaanBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanPenerimaanBarangDetail;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.view.feature_report_purchasing.FeatureReportPurchasingController;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ReportReceivingController implements Initializable{
    
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbPOCode;
    @FXML
    private AnchorPane ancPOCode;
    private JFXCComboBoxTablePopup<TblPurchaseOrder>cbpPOCode;
    @FXML
    private JFXCheckBox chbItemCode;
    @FXML
    private AnchorPane ancItemCode;
    private JFXCComboBoxTablePopup<TblItem> cbpItemCode;
    @FXML
    private JFXCheckBox chbReceivingCode;
    @FXML
    private AnchorPane ancReceivingCode;
    private JFXCComboBoxTablePopup<TblMemorandumInvoice>cbpReceivingCode;
    @FXML
    private JFXCheckBox chbBonusType;
    @FXML
    private AnchorPane ancBonusType;
    private JFXCComboBoxTablePopup<ClassMemorandumInvoiceBonusType>cbpBonusType;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane showReportReceivingLayout;
    
    private void initFormReportReceiving(){
      initDataPopup();
      btnShow.setOnAction((e)->{
         reportReceivingPrintHandle(); 
      });
      initDateCalendar();
      
      cbpPOCode.setVisible(false);
      chbPOCode.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             cbpPOCode.setVisible(true);
             cbpPOCode.setValue(null);
           }
           else{
             cbpPOCode.setVisible(false);
           }
      });
      
      cbpItemCode.setVisible(false);
      chbItemCode.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             cbpItemCode.setVisible(true);
             cbpItemCode.setValue(null);
            }
           else{
             cbpItemCode.setVisible(false);
           }
      });
      
      cbpReceivingCode.setVisible(false);
      chbReceivingCode.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
              cbpReceivingCode.setVisible(true);
              cbpReceivingCode.setValue(null);
           }
           else{
             cbpReceivingCode.setVisible(false);
           }
      });
      
      cbpBonusType.setVisible(false);
      chbBonusType.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             cbpBonusType.setVisible(true);
             cbpBonusType.setValue(null);
           }
           else{
             cbpBonusType.setVisible(false);
           }
      });
      
      JRViewer jrView = new JRViewer(null);
      SwingNode swingNode = new SwingNode();
      swingNode.setContent(jrView);
      printReportReceivingLayout(swingNode);
   }
    
    private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
    
    private void initDataPopup(){
       TableView<TblPurchaseOrder>tblPO = new TableView();
       TableColumn<TblPurchaseOrder,String>poCode = new TableColumn("Kode PO");
       poCode.setMinWidth(100);
       poCode.setCellValueFactory(cellData->cellData.getValue().codePoProperty());
       TableColumn<TblPurchaseOrder,String>supplierName = new TableColumn("Nama Supplier");
       supplierName.setMinWidth(160);
       supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblSupplier().getSupplierName(),param.getValue().tblSupplierProperty()));
       tblPO.getColumns().addAll(poCode,supplierName);
       ObservableList<TblPurchaseOrder>listPOItems = FXCollections.observableArrayList(loadAllDataPurchaseOrder());
       cbpPOCode = new JFXCComboBoxTablePopup(TblPurchaseOrder.class,tblPO,listPOItems,"","Kode PO",false,500,300);
       cbpPOCode.setLabelFloat(false);
       
       TableView<TblMemorandumInvoice>tblMI = new TableView();
       TableColumn<TblMemorandumInvoice,String>miCode = new TableColumn("Kode Penerimaan");
       miCode.setMinWidth(120);
       miCode.setCellValueFactory(cellData -> cellData.getValue().codeMiProperty());
       TableColumn<TblMemorandumInvoice,String>deliveryNumber = new TableColumn("No.Surat Jalan");
       deliveryNumber.setMinWidth(160);
       tblMI.getColumns().addAll(miCode,deliveryNumber);
       ObservableList<TblMemorandumInvoice>listMIItems = FXCollections.observableArrayList(loadAllDataMI());
       cbpReceivingCode = new JFXCComboBoxTablePopup(TblMemorandumInvoice.class,tblMI,listMIItems,"","Kode Penerimaan",false,500,300);
       cbpReceivingCode.setLabelFloat(false);
       
       TableView<TblItem>tblItem = new TableView();
       TableColumn<TblItem,String>itemCode = new TableColumn("Kode Barang");
       itemCode.setMinWidth(100);
       itemCode.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
       TableColumn<TblItem,String>itemName = new TableColumn("Nama Barang");
       itemName.setMinWidth(160);
       itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
       tblItem.getColumns().addAll(itemCode,itemName);
       ObservableList<TblItem>listItem = FXCollections.observableArrayList(loadAllDataItem());
       cbpItemCode = new JFXCComboBoxTablePopup(TblItem.class,tblItem,listItem,"","Nama Barang",false,500,300);
       cbpItemCode.setLabelFloat(false);
       
       TableView<ClassMemorandumInvoiceBonusType>tblBonusType = new TableView();
       TableColumn<ClassMemorandumInvoiceBonusType,String>bonusType = new TableColumn("Tipe Bonus");
       bonusType.setMinWidth(160);
       bonusType.setCellValueFactory(cellData->cellData.getValue().typeNameProperty());
       tblBonusType.getColumns().addAll(bonusType);
       ObservableList<ClassMemorandumInvoiceBonusType>listBonusTypeItems = FXCollections.observableArrayList(loadAllDataBonusType());
       cbpBonusType = new JFXCComboBoxTablePopup(ClassMemorandumInvoiceBonusType.class,tblBonusType,listBonusTypeItems,"","Tipe Bonus",false,500,300);
       cbpBonusType.setLabelFloat(false);
       
       ancPOCode.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpPOCode,0.0);
       AnchorPane.setLeftAnchor(cbpPOCode,0.0);
       AnchorPane.setRightAnchor(cbpPOCode,0.0);
       AnchorPane.setTopAnchor(cbpPOCode,0.0);
       ancPOCode.getChildren().add(cbpPOCode);
       
       ancItemCode.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpItemCode,0.0);
       AnchorPane.setLeftAnchor(cbpItemCode,0.0);
       AnchorPane.setRightAnchor(cbpItemCode,0.0);
       AnchorPane.setTopAnchor(cbpItemCode,0.0);
       ancItemCode.getChildren().add(cbpItemCode);
       
       ancReceivingCode.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpReceivingCode,0.0);
       AnchorPane.setLeftAnchor(cbpReceivingCode,0.0);
       AnchorPane.setRightAnchor(cbpReceivingCode,0.0);
       AnchorPane.setTopAnchor(cbpReceivingCode,0.0);
       ancReceivingCode.getChildren().add(cbpReceivingCode);
       
       ancBonusType.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpBonusType,0.0);
       AnchorPane.setLeftAnchor(cbpBonusType,0.0);
       AnchorPane.setRightAnchor(cbpBonusType,0.0);
       AnchorPane.setTopAnchor(cbpBonusType,0.0);
       ancBonusType.getChildren().add(cbpBonusType);
    }
    
    private List<TblPurchaseOrder>loadAllDataPurchaseOrder(){
      return parentController.getFReportManager().getAllDataPurchaseOrder();
    }
    
    private List<TblItem>loadAllDataItem(){
      return parentController.getFReportManager().getAllDataItem();
    }
    
    private List<TblMemorandumInvoice>loadAllDataMI(){
      return parentController.getFReportManager().getAllDataMemorandumInvoice();
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
    
    private SwingNode printReportReceiving(List<TblMemorandumInvoiceDetail>list,Date startDate,Date endDate){
      List<ClassPrintLaporanPenerimaanBarang>listLaporanPenerimaanBarang = new ArrayList();
      ClassPrintLaporanPenerimaanBarang laporanPenerimaanBarang = new ClassPrintLaporanPenerimaanBarang();
      laporanPenerimaanBarang.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      laporanPenerimaanBarang.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate): "-");
      List<ClassPrintLaporanPenerimaanBarangDetail>listLaporanPenerimaanBarangDetail = new ArrayList();
      listLaporanPenerimaanBarangDetail.addAll(getBonus(list));
      listLaporanPenerimaanBarangDetail.addAll(getNonBonus(list));
      Collections.sort(listLaporanPenerimaanBarangDetail,new Comparator<ClassPrintLaporanPenerimaanBarangDetail>(){
            @Override
          public int compare(ClassPrintLaporanPenerimaanBarangDetail ol, ClassPrintLaporanPenerimaanBarangDetail o2) {
              // Date dateSql1 = null;
             //  Date dateSql2 = null;
               int compare1 = 0;
               int compare2 = 0;
              try{ 
                 java.util.Date date1 = new SimpleDateFormat("dd MMM yyyy",new Locale("id")).parse(ol.getTanggal());
                 java.util.Date date2 = new SimpleDateFormat("dd MMM yyyy",new Locale("id")).parse(o2.getTanggal());
                    
                 Date dateSql1 = new Date(date1.getTime());
                 Date dateSql2 = new Date(date2.getTime());
                 
                   compare1 = dateSql1.compareTo(date2);
                   if(compare1==0){
                     return ol.getKodePenerimaan().compareTo(o2.getKodePenerimaan());
                   }
                } catch (ParseException ex) {
                    Logger.getLogger(ReportReceivingController.class.getName()).log(Level.SEVERE, null, ex);
                }
             return compare1;//dateSql1.compareTo(dateSql2),ol.getKodePenerimaan().compareTo(o2.getKodePenerimaan());
           }
      });
          // ClassPrintLaporanPenerimaanBarangDetail laporanPenerimaanBarangDetail = new ClassPrintLaporanPenerimaanBarangDetail();
           
           
        
      /* laporanPenerimaanBarangDetail.setKodeBarang(getMemorandumInvoiceDetail.getTblItem().getCodeItem());
         laporanPenerimaanBarangDetail.setKodePembelian(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
         laporanPenerimaanBarangDetail.setKodePenerimaan(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getCodeMi());
         laporanPenerimaanBarangDetail.setNamaBarang(getMemorandumInvoiceDetail.getTblItem().getItemName());
         laporanPenerimaanBarangDetail.setNoSuratJalan(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getDeliveryNumber());
         laporanPenerimaanBarangDetail.setQty(getMemorandumInvoiceDetail.getItemQuantity());
         laporanPenerimaanBarangDetail.setSatuan(getMemorandumInvoiceDetail.getTblItem().getTblUnit().getUnitName());
         laporanPenerimaanBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getReceivingDate()));
       //  laporanPenerimaanBarangDetail.setTanggalExp(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getMemorandumInvoiceDetail.));
         listLaporanPenerimaanBarangDetail.add(laporanPenerimaanBarangDetail);
        */
       
      laporanPenerimaanBarang.setListLaporanPenerimaanBarangDetail(listLaporanPenerimaanBarangDetail);
      listLaporanPenerimaanBarang.add(laporanPenerimaanBarang);
    
       return  ClassPrinter.printLaporanPenerimaanBarang(listLaporanPenerimaanBarang);
    }
    
    private List<ClassPrintLaporanPenerimaanBarangDetail> getBonus(List<TblMemorandumInvoiceDetail>listMemorandumInvoiceDetail){
       List<ClassPrintLaporanPenerimaanBarangDetail>listLaporanPenerimaanBarangExp = new ArrayList();
       for(TblMemorandumInvoiceDetail getMemorandumInvoiceDetail : listMemorandumInvoiceDetail){
         if(getMemorandumInvoiceDetail.getTblItem()!=null){ 
           if(getMemorandumInvoiceDetail.getTblItem().getConsumable()==true){
               List<TblMemorandumInvoiceDetailItemExpiredDate>list = parentController.getFReportManager().getDataExpiredDate(getMemorandumInvoiceDetail.getIddetail());
               for(TblMemorandumInvoiceDetailItemExpiredDate memorandumInvoiceDetailItemExp : list){
                   ClassPrintLaporanPenerimaanBarangDetail laporanPenerimaanBarangDetail = new ClassPrintLaporanPenerimaanBarangDetail();
                   laporanPenerimaanBarangDetail.setBonus("Bonus");
                   laporanPenerimaanBarangDetail.setKodeBarang(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getTblItem().getCodeItem());
                   laporanPenerimaanBarangDetail.setKodePembelian(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
                   laporanPenerimaanBarangDetail.setNamaBarang(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getTblItem().getItemName());
                   laporanPenerimaanBarangDetail.setNoSuratJalan(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getDeliveryNumber());
                   laporanPenerimaanBarangDetail.setQty(memorandumInvoiceDetailItemExp.getItemQuantity());
                   laporanPenerimaanBarangDetail.setSatuan(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getTblItem().getTblUnit().getUnitName());
                   laporanPenerimaanBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getReceivingDate()));
                   laporanPenerimaanBarangDetail.setTanggalExp(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getItemExpiredDate()));
                   laporanPenerimaanBarangDetail.setKodePenerimaan(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getCodeMi());
                   listLaporanPenerimaanBarangExp.add(laporanPenerimaanBarangDetail);
               }
            }
            else{
              ClassPrintLaporanPenerimaanBarangDetail laporanPenerimaanBarangDetail = new ClassPrintLaporanPenerimaanBarangDetail();
                   laporanPenerimaanBarangDetail.setBonus("Bonus");
                   laporanPenerimaanBarangDetail.setKodeBarang(getMemorandumInvoiceDetail.getTblItem().getCodeItem());
                   laporanPenerimaanBarangDetail.setKodePembelian(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
                   laporanPenerimaanBarangDetail.setNamaBarang(getMemorandumInvoiceDetail.getTblItem().getItemName());
                   laporanPenerimaanBarangDetail.setNoSuratJalan(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getDeliveryNumber());
                   laporanPenerimaanBarangDetail.setQty(getMemorandumInvoiceDetail.getItemQuantity());
                   laporanPenerimaanBarangDetail.setSatuan(getMemorandumInvoiceDetail.getTblItem().getTblUnit().getUnitName());
                   laporanPenerimaanBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getReceivingDate()));
                   laporanPenerimaanBarangDetail.setTanggalExp("-");
                   laporanPenerimaanBarangDetail.setKodePenerimaan(getMemorandumInvoiceDetail.getTblMemorandumInvoice().getCodeMi());
                   listLaporanPenerimaanBarangExp.add(laporanPenerimaanBarangDetail);
            }
         }
       }
       return listLaporanPenerimaanBarangExp; 
    }
    
    private List<ClassPrintLaporanPenerimaanBarangDetail>getNonBonus(List<TblMemorandumInvoiceDetail>listMemorandumInvoiceDetail){
      List<ClassPrintLaporanPenerimaanBarangDetail>listPenerimaanBarangDetail = new ArrayList();
      for(TblMemorandumInvoiceDetail memorandumInvoiceDetail : listMemorandumInvoiceDetail){
           if(memorandumInvoiceDetail.getTblItem()==null && memorandumInvoiceDetail.getTblSupplierItem()!=null){
               if(memorandumInvoiceDetail.getTblSupplierItem().getTblItem().getConsumable()==true){
                   List<TblMemorandumInvoiceDetailItemExpiredDate>list = parentController.getFReportManager().getDataExpiredDate(memorandumInvoiceDetail.getIddetail());
                   for(TblMemorandumInvoiceDetailItemExpiredDate memorandumInvoiceDetailItemExp : list){
                      ClassPrintLaporanPenerimaanBarangDetail laporanPenerimaanBarangDetail = new ClassPrintLaporanPenerimaanBarangDetail();
                      laporanPenerimaanBarangDetail.setBonus("Non Bonus");
                      laporanPenerimaanBarangDetail.setKodeBarang(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getTblItem().getCodeItem());
                      laporanPenerimaanBarangDetail.setKodePembelian(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
                      laporanPenerimaanBarangDetail.setNamaBarang(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getTblItem().getItemName());
                      laporanPenerimaanBarangDetail.setNoSuratJalan(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getDeliveryNumber());
                      laporanPenerimaanBarangDetail.setQty(memorandumInvoiceDetailItemExp.getItemQuantity());
                      laporanPenerimaanBarangDetail.setSatuan(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getTblItem().getTblUnit().getUnitName());
                      laporanPenerimaanBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getReceivingDate()));
                      laporanPenerimaanBarangDetail.setTanggalExp(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(memorandumInvoiceDetailItemExp.getTblItemExpiredDate().getItemExpiredDate()));
                      laporanPenerimaanBarangDetail.setKodePenerimaan(memorandumInvoiceDetailItemExp.getTblMemorandumInvoiceDetail().getTblMemorandumInvoice().getCodeMi());
                      listPenerimaanBarangDetail.add(laporanPenerimaanBarangDetail);
                    }
                }
               else{
                  ClassPrintLaporanPenerimaanBarangDetail laporanPenerimaanBarangDetail = new ClassPrintLaporanPenerimaanBarangDetail();
                  laporanPenerimaanBarangDetail.setBonus("Non Bonus");
                  laporanPenerimaanBarangDetail.setKodeBarang(memorandumInvoiceDetail.getTblSupplierItem().getTblItem().getCodeItem());
                  laporanPenerimaanBarangDetail.setKodePembelian(memorandumInvoiceDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
                  laporanPenerimaanBarangDetail.setKodePenerimaan(memorandumInvoiceDetail.getTblMemorandumInvoice().getCodeMi());
                  laporanPenerimaanBarangDetail.setNamaBarang(memorandumInvoiceDetail.getTblSupplierItem().getTblItem().getItemName());
                  laporanPenerimaanBarangDetail.setNoSuratJalan(memorandumInvoiceDetail.getTblMemorandumInvoice().getDeliveryNumber());
                  laporanPenerimaanBarangDetail.setQty(memorandumInvoiceDetail.getItemQuantity());
                  laporanPenerimaanBarangDetail.setSatuan(memorandumInvoiceDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName());
                  laporanPenerimaanBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(memorandumInvoiceDetail.getTblMemorandumInvoice().getReceivingDate()));
                  laporanPenerimaanBarangDetail.setTanggalExp("-");
                  listPenerimaanBarangDetail.add(laporanPenerimaanBarangDetail);
               }
           }
       }
      return listPenerimaanBarangDetail; 
    }
    
    private void reportReceivingPrintHandle(){
       Date startDate = null;
       Date endDate = null;
       TblMemorandumInvoice mi = null; 
       ClassMemorandumInvoiceBonusType bonusType = null;
       TblItem item = null;
       TblPurchaseOrder po = null;
       
       List<TblMemorandumInvoiceDetail>listMemorandumInvoice = new ArrayList<>();
       
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
          startDate = Date.valueOf(dpStartDate.getValue());
          endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(chbItemCode.isSelected()){
           if(cbpItemCode.getValue()!=null){
             item = cbpItemCode.getValue();
            }
       }
      
       if(chbPOCode.isSelected()){
           if(cbpPOCode.getValue()!=null){
             po = cbpPOCode.getValue();
            }
       }
      
       if(chbBonusType.isSelected()){
           if(cbpBonusType.getValue()!=null){
             bonusType = cbpBonusType.getValue();
           }
       }
       
       if(chbReceivingCode.isSelected()){
           if(cbpReceivingCode.getValue()!=null){
             mi = cbpReceivingCode.getValue();
           }
       }
       
       listMemorandumInvoice = parentController.getFReportManager().getAllMemorandumInvoiceDetail(startDate, endDate,po,mi,item,bonusType);
       System.out.println("size :"+listMemorandumInvoice.size());
       SwingNode swingNode = new SwingNode();
       swingNode = printReportReceiving(listMemorandumInvoice,startDate,endDate);
       printReportReceivingLayout(swingNode);
    }
    
    private void printReportReceivingLayout(SwingNode swingNode){
      showReportReceivingLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,0.0);
      showReportReceivingLayout.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     initFormReportReceiving();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportReceivingController(FeatureReportPurchasingController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportPurchasingController parentController;
}
