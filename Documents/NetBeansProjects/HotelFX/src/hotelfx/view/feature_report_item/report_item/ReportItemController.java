/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_item.report_item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintLaporanMasukKeluarBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanMasukKeluarBarangDetail;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.view.feature_report_item.FeatureReportItemController;
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
public class ReportItemController implements Initializable{
   @FXML
   private JFXDatePicker dpStartDate;
   @FXML
   private JFXDatePicker dpEndDate;
   @FXML
   private AnchorPane ancCbpWarehouse;
   private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpWarehouse;
   @FXML
   private JFXButton btnShow;
   @FXML
   private AnchorPane ancShowReport;
   
   private void initForm(){
     initDataPopup();
     
     SwingNode swingNode = new SwingNode();
     JRViewer jrView = new JRViewer(null);
     swingNode.setContent(jrView);
     swingNodeLayout(swingNode);
     
     btnShow.setOnAction((e)->{
       if(checkDataInput()){
          reportItemPrintHandle();
        }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput, null);
       }
        
     });
     
     initColourField();
     initDateCalendar();
   }
   
   private void initColourField(){
     ClassViewSetting.setImportantField(dpStartDate,dpEndDate,cbpWarehouse);
   }
   
   private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
   
   private void initDataPopup(){
      TableView<TblLocationOfWarehouse>tblWarehouse = new TableView();
      TableColumn<TblLocationOfWarehouse,String>warehouseName = new TableColumn("Gudang");
      warehouseName.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfWarehouse,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getWarehouseName(),param.getValue().warehouseNameProperty()));
      tblWarehouse.getColumns().addAll(warehouseName);
      
      ObservableList<TblLocationOfWarehouse>list = FXCollections.observableArrayList(loadAllDataWarehouse());
      
      cbpWarehouse = new JFXCComboBoxTablePopup(TblLocationOfWarehouse.class,tblWarehouse,list,"","Gudang *",false,500,300);
      cbpWarehouse.setLabelFloat(false);
      ancCbpWarehouse.getChildren().clear();
      AnchorPane.setTopAnchor(cbpWarehouse,0.0);
      AnchorPane.setBottomAnchor(cbpWarehouse,0.0);
      AnchorPane.setLeftAnchor(cbpWarehouse,0.0);
      AnchorPane.setRightAnchor(cbpWarehouse,0.0);
      ancCbpWarehouse.getChildren().add(cbpWarehouse);
    }
    
   private List<TblLocationOfWarehouse>loadAllDataWarehouse(){
      List<TblLocationOfWarehouse>list = parentController.getFReportManager().getAllDataWarehouse();
      return list;
   }
   
   private void reportItemPrintHandle(){
       Date startDate = null;
       Date endDate = null;
       TblLocation location = null;
       List<TblItemMutationHistory>list = new ArrayList();
                
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(cbpWarehouse.getValue()!=null){
         location = cbpWarehouse.getValue().getTblLocation();
       }
       
       list = parentController.getFReportManager().getAllDataItemMutationHistoryByIDWarehouse(location,startDate,endDate);
       SwingNode swingNode = printReportItem(list);
       swingNodeLayout(swingNode);
   }
   
   private SwingNode printReportItem( List<TblItemMutationHistory>list){
      List<ClassPrintLaporanMasukKeluarBarang>listLaporanMasukKeluarBarang = new ArrayList();
      ClassPrintLaporanMasukKeluarBarang laporanMasukKeluarBarang = new ClassPrintLaporanMasukKeluarBarang();
      laporanMasukKeluarBarang.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      laporanMasukKeluarBarang.setGudang(cbpWarehouse.getValue().getWarehouseName());
      laporanMasukKeluarBarang.setPeriode(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpStartDate.getValue()))+"-"+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpEndDate.getValue())));
      List<ClassPrintLaporanMasukKeluarBarangDetail>listLaporanMasukKeluarBarangDetail = new ArrayList();
       BigDecimal totalStokAkhir = new BigDecimal(0);
       
           for(TblItemMutationHistory itemMutationHistory : list){
             boolean found = false;
             for(ClassPrintLaporanMasukKeluarBarangDetail getLaporanMasukKeluarBarangDetail : listLaporanMasukKeluarBarangDetail){
               BigDecimal totalStokKeluar = new BigDecimal(0); 
               BigDecimal totalStokMasuk = new BigDecimal(0);
              
               if(itemMutationHistory.getTblItem().getCodeItem().equalsIgnoreCase(getLaporanMasukKeluarBarangDetail.getKodeBarang())){
                   getLaporanMasukKeluarBarangDetail.setStokKeluar(stokKeluar(list,getLaporanMasukKeluarBarangDetail.getKodeBarang()));
                   getLaporanMasukKeluarBarangDetail.setStokMasuk(stokMasuk(list,getLaporanMasukKeluarBarangDetail.getKodeBarang()));
                   totalStokAkhir = getLaporanMasukKeluarBarangDetail.getStokAwal().add(getLaporanMasukKeluarBarangDetail.getStokMasuk()).subtract(getLaporanMasukKeluarBarangDetail.getStokKeluar());
                   getLaporanMasukKeluarBarangDetail.setStokAkhir(totalStokAkhir);
                   found = true;
                }
             }
           
             if(found == false){
                ClassPrintLaporanMasukKeluarBarangDetail laporanMasukKeluarBarangDetail = new ClassPrintLaporanMasukKeluarBarangDetail();
                laporanMasukKeluarBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(itemMutationHistory.getMutationDate()));
                laporanMasukKeluarBarangDetail.setKodeBarang(itemMutationHistory.getTblItem().getCodeItem());
                laporanMasukKeluarBarangDetail.setNamaBarang(itemMutationHistory.getTblItem().getItemName());
                laporanMasukKeluarBarangDetail.setSatuan(itemMutationHistory.getTblItem().getTblUnit().getUnitName());
                laporanMasukKeluarBarangDetail.setStokAwal(parentController.getFReportManager().getStock(cbpWarehouse.getValue().getTblLocation(),itemMutationHistory.getTblItem(),Date.valueOf(dpStartDate.getValue())));
                laporanMasukKeluarBarangDetail.setStokKeluar(stokKeluar(list,laporanMasukKeluarBarangDetail.getKodeBarang()));
                laporanMasukKeluarBarangDetail.setStokMasuk(stokMasuk(list,laporanMasukKeluarBarangDetail.getKodeBarang()));
                totalStokAkhir = laporanMasukKeluarBarangDetail.getStokAwal().add(laporanMasukKeluarBarangDetail.getStokMasuk()).subtract(laporanMasukKeluarBarangDetail.getStokKeluar());
                laporanMasukKeluarBarangDetail.setStokAkhir(totalStokAkhir);
                listLaporanMasukKeluarBarangDetail.add(laporanMasukKeluarBarangDetail);
             }
              //List<TblItemMutationHistory>list = parentController.getFReportManager().getAllDataItemMutationHistoryByIDWarehouse()
        }
        laporanMasukKeluarBarang.setListLaporanMasukKeluarBarangDetail(listLaporanMasukKeluarBarangDetail);
        listLaporanMasukKeluarBarang.add(laporanMasukKeluarBarang);
        return ClassPrinter.printLaporanMasukKeluarBarang(listLaporanMasukKeluarBarang, laporanMasukKeluarBarang);
    }
   
    private void swingNodeLayout(SwingNode swingNode){
       ancShowReport.getChildren().clear();
       AnchorPane.setTopAnchor(swingNode,15.0);
       AnchorPane.setLeftAnchor(swingNode,15.0);
       AnchorPane.setRightAnchor(swingNode,15.0);
       AnchorPane.setBottomAnchor(swingNode,15.0);
       ancShowReport.getChildren().add(swingNode);
    }
   
    private BigDecimal stokMasuk(List<TblItemMutationHistory>list,String kodeItem){
       BigDecimal totalStokMasuk = new BigDecimal(0);
       
       for(TblItemMutationHistory itemMutation : list){
           if(itemMutation.getTblItem().getCodeItem().equalsIgnoreCase(kodeItem)){
               if(itemMutation.getMutationDate().after(Date.valueOf(dpStartDate.getValue().minusDays(1))) 
                  && itemMutation.getMutationDate().before(Date.valueOf(dpEndDate.getValue().plusDays(1))) 
                  && itemMutation.getTblLocationByIdlocationOfDestination().getIdlocation()==cbpWarehouse.getValue().getTblLocation().getIdlocation()){
                 totalStokMasuk = totalStokMasuk.add(itemMutation.getItemQuantity());
               }  
           }
       }
       return totalStokMasuk;
    }
    
     private BigDecimal stokKeluar(List<TblItemMutationHistory>list,String kodeItem){
       BigDecimal totalStokKeluar = new BigDecimal(0);
       
       for(TblItemMutationHistory itemMutation : list){
           if(itemMutation.getTblItem().getCodeItem().equalsIgnoreCase(kodeItem)){
               if(itemMutation.getMutationDate().after(Date.valueOf(dpStartDate.getValue().minusDays(1))) 
                 && itemMutation.getMutationDate().before(Date.valueOf(dpEndDate.getValue().plusDays(1))) 
                 && itemMutation.getTblLocationByIdlocationOfSource().getIdlocation()==cbpWarehouse.getValue().getTblLocation().getIdlocation()){
                 totalStokKeluar = totalStokKeluar.add(itemMutation.getItemQuantity());
               }  
            }
        }
       return totalStokKeluar;
    }
    
    String errDataInput;
    private boolean checkDataInput(){
      errDataInput = "";
      boolean check = true;
      
       if(dpStartDate.getValue()==null){
         errDataInput+="Periode Awal : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
        }
       
       if(dpEndDate.getValue()==null){
          errDataInput+="Periode Akhir : "+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(cbpWarehouse.getValue()==null){
          errDataInput+="Gudang : "+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       return check;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      System.out.println("masuk!!");
      initForm();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportItemController(FeatureReportItemController parentController){
      System.out.println("jjjj");
      this.parentController = parentController;
    }
    
    private final FeatureReportItemController parentController;
}
