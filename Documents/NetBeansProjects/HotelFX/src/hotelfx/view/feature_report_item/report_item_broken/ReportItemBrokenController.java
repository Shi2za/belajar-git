/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_item.report_item_broken;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassLocationName;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanRusakBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanRusakBarangDetail;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.view.feature_report_item.FeatureReportItemController;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class ReportItemBrokenController implements Initializable{
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbItem;
    @FXML
    private AnchorPane cbpItemLayout;
    private JFXCComboBoxTablePopup<TblItem>cbpItem;
    @FXML
    private JFXCheckBox chbLocation;
    @FXML
    private AnchorPane cbpLocationLayout;
    private JFXCComboBoxTablePopup<ClassLocationName>cbpLocation;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane showReportLayout;
    
    private void initFormReportItemBroken(){
      initDataPopup();
      btnShow.setOnAction((e)->{
          reportItemBrokenPrintHandle();
      });
      
      cbpItem.setVisible(false);
      cbpLocation.setVisible(false);
      
      chbItem.selectedProperty().addListener((obs,oldVal,newVal)->{
         cbpItem.setVisible(newVal==true);
         cbpItem.setValue(null);
      });
      
      chbLocation.selectedProperty().addListener((obs,oldVal,newVal)->{
         cbpLocation.setVisible(newVal==true);
         cbpLocation.setValue(null);
      });
      initDateCalendar();
      
      JRViewer jrViewer = new JRViewer(null);
      SwingNode swingNode = new SwingNode();
      swingNode.setContent(jrViewer);
      showReportLayout(swingNode);
    }
    
    private void initDateCalendar(){
       ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
       ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
    
    private void initDataPopup(){
      TableView<TblItem>tblItem = new TableView();
      TableColumn<TblItem,String>itemCode = new TableColumn("Kode Barang");
      itemCode.setMinWidth(120);
      itemCode.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
      TableColumn<TblItem,String>itemName = new TableColumn("Nama Barang");
      itemName.setMinWidth(200);
      itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
      tblItem.getColumns().addAll(itemCode,itemName);
      ObservableList<TblItem>itemList = FXCollections.observableArrayList(loadAllDataItem());
      cbpItem = new JFXCComboBoxTablePopup(TblItem.class,tblItem,itemList,"","Barang",false,500,300);
      cbpItem.setLabelFloat(false);
      
      TableView<ClassLocationName>tblLocation = new TableView();
      TableColumn<ClassLocationName,String>locationName = new TableColumn("Lokasi");
      locationName.setCellValueFactory(cellData -> cellData.getValue().nameLocationProperty());
      locationName.setMinWidth(200);
      tblLocation.getColumns().addAll(locationName);
      ObservableList<ClassLocationName>locationItem = FXCollections.observableArrayList(loadAllDataLocation());
      cbpLocation = new JFXCComboBoxTablePopup(ClassLocationName.class,tblLocation,locationItem,"","Lokasi",false,500,300);
      cbpLocation.setLabelFloat(false);
      
      cbpItemLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpItem,0.0);
      AnchorPane.setLeftAnchor(cbpItem,0.0);
      AnchorPane.setRightAnchor(cbpItem,0.0);
      AnchorPane.setTopAnchor(cbpItem,0.0);
      cbpItemLayout.getChildren().add(cbpItem);
      
      cbpLocationLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpLocation,0.0);
      AnchorPane.setLeftAnchor(cbpLocation,0.0);
      AnchorPane.setRightAnchor(cbpLocation,0.0);
      AnchorPane.setTopAnchor(cbpLocation,0.0);
      cbpLocationLayout.getChildren().add(cbpLocation);
    }
    
    private List<TblItem>loadAllDataItem(){
      return parentController.getFReportManager().getAllDataItem();
    }
    
    private List<ClassLocationName>loadAllDataLocation(){
      List<TblLocationOfWarehouse>listWarehouse = parentController.getFReportManager().getAllDataWarehouse();
      List<TblLocationOfLaundry>listLaundry = parentController.getFReportManager().getAllDataLaundry();
      List<TblRoom>listRoom = parentController.getFReportManager().getAllDataRoom();
      List<ClassLocationName>list = new ArrayList();
      
       for(TblLocationOfWarehouse getWarehouse : listWarehouse){
         ClassLocationName locationName = new ClassLocationName();
         locationName.setLocation(getWarehouse.getTblLocation());
         locationName.setNameLocation(getWarehouse.getWarehouseName());
         list.add(locationName);
       }
      
      for(TblLocationOfLaundry getLaundry : listLaundry){
         ClassLocationName locationName = new ClassLocationName();
         locationName.setLocation(getLaundry.getTblLocation());
         locationName.setNameLocation(getLaundry.getLaundryName());
         list.add(locationName);
       }
      
      for(TblRoom getRoom : listRoom){
         ClassLocationName locationName = new ClassLocationName();
         locationName.setLocation(getRoom.getTblLocation());
         locationName.setNameLocation(getRoom.getRoomName());
         list.add(locationName);
      }
      
      return list;
    }
    
    private SwingNode printReportItemBroken(List<TblItemMutationHistory>list,Date startDate,Date endDate){
       List<ClassPrintLaporanRusakBarang>listPrintLaporanRusakBarang = new ArrayList();
       ClassPrintLaporanRusakBarang printLaporanRusakBarang = new ClassPrintLaporanRusakBarang();
       printLaporanRusakBarang.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate): "-");
       printLaporanRusakBarang.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       List<ClassPrintLaporanRusakBarangDetail>listPrintLaporanRusakBarangDetail = new ArrayList();
       for(TblItemMutationHistory getMutationHistory : list){
          ClassPrintLaporanRusakBarangDetail printLaporanRusakBarangDetail  = new ClassPrintLaporanRusakBarangDetail();
          printLaporanRusakBarangDetail.setIdBarang(getMutationHistory.getTblItem().getCodeItem());
          printLaporanRusakBarangDetail.setJumlah(getMutationHistory.getItemQuantity());
          printLaporanRusakBarangDetail.setKeterangan(getMutationHistory.getMutationNote()!=null ? getMutationHistory.getMutationNote() : "");
          printLaporanRusakBarangDetail.setLokasi(getLocation(getMutationHistory.getTblLocationByIdlocationOfSource()));
          printLaporanRusakBarangDetail.setNamaBarang(getMutationHistory.getTblItem().getItemName());
          printLaporanRusakBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getMutationHistory.getMutationDate()));
          printLaporanRusakBarangDetail.setUnit(getMutationHistory.getTblItem().getTblUnit().getUnitName());
          listPrintLaporanRusakBarangDetail.add(printLaporanRusakBarangDetail);
       }
       printLaporanRusakBarang.setListLaporanRusakBarangDetail(listPrintLaporanRusakBarangDetail);
       listPrintLaporanRusakBarang.add(printLaporanRusakBarang);
       return ClassPrinter.printLaporanRusakBarang(listPrintLaporanRusakBarang);
    }
    
    private void reportItemBrokenPrintHandle(){
      Date startDate = null;
      Date endDate = null;
      TblItem item = null;
      TblLocation location = null;
      SwingNode swingNode = new SwingNode();
      
      List<TblItemMutationHistory>list = new ArrayList();
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(chbItem.isSelected()){
           if(cbpItem.getValue()!=null){
             item = cbpItem.getValue();
            }
       }
      
       if(chbLocation.isSelected()){
           if(cbpLocation.getValue()!=null){
             location = cbpLocation.getValue().getLocation();
            }
       }
       
       list = parentController.getFReportManager().getAllDataItemMutationHistoryForReport(startDate,endDate,item,location);
       swingNode = printReportItemBroken(list,startDate,endDate);
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
    
    private String getLocation(TblLocation location){
       List<TblLocationOfWarehouse>listWarehouse= parentController.getFReportManager().getAllDataWareHouseByIdLocation(location.getIdlocation());
       List<TblLocationOfLaundry>listLaundry = parentController.getFReportManager().getAllDataLaundryByIdLocation(location.getIdlocation());
       List<TblRoom>listRoom = parentController.getFReportManager().getAllDataRoomByIdLocation(location.getIdlocation());
       
       String result = "";
       if(listWarehouse.size()>0){
           for(TblLocationOfWarehouse getWarehouse : listWarehouse){
             result = getWarehouse.getWarehouseName();
            }
       }
       else if(listLaundry.size()>0){
           for(TblLocationOfLaundry getLaundry : listLaundry){
             result = getLaundry.getLaundryName();
           }
       }
       else if(listRoom.size()>0){
           for(TblRoom getRoom : listRoom){
             result = getRoom.getRoomName();
           } 
       }
      return result;
    }
       
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportItemBroken();
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportItemBrokenController(FeatureReportItemController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportItemController parentController;
}
