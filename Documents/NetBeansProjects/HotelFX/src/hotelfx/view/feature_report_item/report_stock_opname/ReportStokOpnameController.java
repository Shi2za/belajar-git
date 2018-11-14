/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_item.report_stock_opname;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassLocationName;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanStokOpname;
import hotelfx.helper.PrintModel.ClassPrintLaporanStokOpnameDetail;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblStockOpname;
import hotelfx.persistence.model.TblStockOpnameDetail;
import hotelfx.persistence.model.TblStockOpnameDetailItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameDetailPropertyBarcode;
import hotelfx.view.feature_report_item.FeatureReportItemController;
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
public class ReportStokOpnameController implements Initializable{
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbLocation;
    @FXML
    private AnchorPane cbpLocationLayout;
    private JFXCComboBoxTablePopup<ClassLocationName>cbpLocation;
    @FXML
    private JFXCheckBox chbItem;
    @FXML
    private AnchorPane cbpItemLayout;
    private JFXCComboBoxTablePopup<TblItem>cbpItem;
    @FXML
    private JFXCheckBox chbCodeStockOpname;
    @FXML
    private AnchorPane cbpCodeStockOpnameLayout;
    private JFXCComboBoxTablePopup<TblStockOpname>cbpCodeStockOpname;
    @FXML
    private AnchorPane showReportLayout;
    @FXML
    private JFXButton btnShow;
    
    private void initFormReportStokOpname(){
       initDataPopup();
       btnShow.setOnAction((e)->{
          reportStokOpnamePrintHandle();
       });
       
       cbpLocation.setVisible(false);
       cbpItem.setVisible(false);
       cbpCodeStockOpname.setVisible(false);
       
       chbLocation.selectedProperty().addListener((obs,oldVal,newVal)->{
           cbpLocation.setVisible(newVal==true);
           cbpLocation.setValue(null);
       });
       
       chbItem.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpItem.setVisible(newVal==true);
          cbpItem.setValue(null);
       });
       
       chbCodeStockOpname.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpCodeStockOpname.setVisible(newVal==true);
          cbpCodeStockOpname.setValue(null);
       });
       
       initDateFormat();
    }
    
    private void initDateFormat(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
    
    private void initDataPopup(){
       TableView<TblStockOpname>tblStockOpname = new TableView();
       TableColumn<TblStockOpname,String>codeStockOpname = new TableColumn("Kode Stock Opname");
       codeStockOpname.setMinWidth(160);
       codeStockOpname.setCellValueFactory(cellData ->cellData.getValue().codeStockOpnameProperty());
       TableColumn<TblStockOpname,String>locationStockOpname = new TableColumn("Lokasi");
       locationStockOpname.setMinWidth(180);
       locationStockOpname.setCellValueFactory((TableColumn.CellDataFeatures<TblStockOpname,String>param)
       ->Bindings.createStringBinding(()->getWarehouseLaundry(param.getValue().getTblLocation()),param.getValue().tblLocationProperty()));
       TableColumn<TblStockOpname,String>dateStockOpname = new TableColumn("Tanggal");
       dateStockOpname.setMinWidth(120);
       dateStockOpname.setCellValueFactory((TableColumn.CellDataFeatures<TblStockOpname,String>param)
       ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyy",new Locale("id")).format(param.getValue().getStockOpanameDate()),param.getValue().stockOpanameDateProperty()));
       tblStockOpname.getColumns().addAll(codeStockOpname,locationStockOpname);
       ObservableList<TblStockOpname>stockOpnameList = FXCollections.observableArrayList(loadAllDataStockOpname());
       cbpCodeStockOpname = new JFXCComboBoxTablePopup(TblStockOpname.class,tblStockOpname,stockOpnameList,"","Kode Stock Opname",false,500,300);
       cbpCodeStockOpname.setLabelFloat(false);
       
       TableView<TblItem>tblItem = new TableView();
       TableColumn<TblItem,String>codeItem = new TableColumn("ID Barang");
       codeItem.setMinWidth(120);
       codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
       TableColumn<TblItem,String>nameItem = new TableColumn("Nama Barang");
       nameItem.setMinWidth(180);
       nameItem.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
       tblItem.getColumns().addAll(codeItem,nameItem);
       ObservableList<TblItem>itemList = FXCollections.observableArrayList(loadAllDataItem());
       cbpItem = new JFXCComboBoxTablePopup(TblItem.class,tblItem,itemList,"","Barang ",false,500,300);
       cbpItem.setLabelFloat(false);
       
       TableView<ClassLocationName>tblLocation = new TableView();
       TableColumn<ClassLocationName,String>nameLocation = new TableColumn("Lokasi");
       nameLocation.setMinWidth(200);
       nameLocation.setCellValueFactory((TableColumn.CellDataFeatures<ClassLocationName,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getNameLocation(),param.getValue().nameLocationProperty()));
       tblLocation.getColumns().addAll(nameLocation);
       ObservableList<ClassLocationName>locationList = FXCollections.observableArrayList(loadAllDataLocation());
       cbpLocation = new JFXCComboBoxTablePopup(TblLocation.class,tblLocation,locationList,"","Lokasi",false,500,300);
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
       
       cbpCodeStockOpnameLayout.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpCodeStockOpname,0.0);
       AnchorPane.setLeftAnchor(cbpCodeStockOpname,0.0);
       AnchorPane.setRightAnchor(cbpCodeStockOpname,0.0);
       AnchorPane.setTopAnchor(cbpCodeStockOpname,0.0);
       cbpCodeStockOpnameLayout.getChildren().add(cbpCodeStockOpname);
       
       JRViewer jrView = new JRViewer(null);
       SwingNode swingNode = new SwingNode();
       swingNode.setContent(jrView);
       showReportLayout(swingNode);
    }
    
    private List<TblStockOpname>loadAllDataStockOpname(){
      return parentController.getFReportManager().getAllDataStockOpname();
    }
    
    private List<TblItem>loadAllDataItem(){
      return parentController.getFReportManager().getAllDataItem();
    }
    
    private List<ClassLocationName>loadAllDataLocation(){
       List<TblLocationOfWarehouse>listWarehouse = parentController.getFReportManager().getAllDataWarehouse();
       List<TblLocationOfLaundry>listLaundry = parentController.getFReportManager().getAllDataLaundry();
       List<ClassLocationName>list = new ArrayList();
       
       for(TblLocationOfWarehouse getWarehouse :listWarehouse){
          ClassLocationName locationName = new ClassLocationName();
          locationName.setLocation(getWarehouse.getTblLocation());
          locationName.setNameLocation(getWarehouse.getWarehouseName());
          list.add(locationName);
       }
       
       for(TblLocationOfLaundry getLaundry : listLaundry){
          ClassLocationName locationName = new ClassLocationName();
          locationName.setNameLocation(getLaundry.getLaundryName());
          locationName.setLocation(getLaundry.getTblLocation());
          list.add(locationName);
       }
       
       return list;
    }
    
    private SwingNode printReportStokOpname(List<TblStockOpnameDetail>list,Date startDate,Date endDate){
      List<ClassPrintLaporanStokOpname>listPrintStokOpname = new ArrayList();
      ClassPrintLaporanStokOpname stokOpname = new ClassPrintLaporanStokOpname();
      stokOpname.setPeriode(startDate!=null && endDate !=null ? new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(startDate)+"-"+new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(endDate) : "-");
      stokOpname.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      List<ClassPrintLaporanStokOpnameDetail>listPrintStokOpnameDetail = new ArrayList();
      for(TblStockOpnameDetail getTblStockOpnameDetail : list){
         ClassPrintLaporanStokOpnameDetail printStokOpnameDetail = new ClassPrintLaporanStokOpnameDetail();
         printStokOpnameDetail.setKodeBarang(getTblStockOpnameDetail.getTblItem().getCodeItem());
         printStokOpnameDetail.setKodeStokOpname(getTblStockOpnameDetail.getTblStockOpname().getCodeStockOpname());
         printStokOpnameDetail.setNamaBarang(getTblStockOpnameDetail.getTblItem().getItemName());
         printStokOpnameDetail.setQtyReal(getTblStockOpnameDetail.getItemQuantityReal());
         printStokOpnameDetail.setQtySistem(getTblStockOpnameDetail.getItemQuantitySystem());
         printStokOpnameDetail.setBalance(printStokOpnameDetail.getQtyReal().subtract(printStokOpnameDetail.getQtySistem()));
         printStokOpnameDetail.setSatuan(getTblStockOpnameDetail.getTblItem().getTblUnit().getUnitName());
         printStokOpnameDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getTblStockOpnameDetail.getTblStockOpname().getStockOpanameDate()));
         printStokOpnameDetail.setTanggalKadaluarsa(getTblStockOpnameDetail.getTblItemExpiredDate()!=null ? new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getTblStockOpnameDetail.getTblItemExpiredDate().getItemExpiredDate()) : "-");
         printStokOpnameDetail.setKodeBarcode(getTblStockOpnameDetail.getTblPropertyBarcode()!=null ? getTblStockOpnameDetail.getTblPropertyBarcode().getCodeBarcode() : "-");
         printStokOpnameDetail.setGudang(getWarehouseLaundry(getTblStockOpnameDetail.getTblStockOpname().getTblLocation()));
         listPrintStokOpnameDetail.add(printStokOpnameDetail);
       }
       stokOpname.setListStokOpnameDetail(listPrintStokOpnameDetail);
       listPrintStokOpname.add(stokOpname);
       return ClassPrinter.printLaporanStokOpname(listPrintStokOpname);
    }
    
    private String getWarehouseLaundry(TblLocation location){
       List<TblLocationOfWarehouse>listWarehouse= parentController.getFReportManager().getAllDataWareHouseByIdLocation(location.getIdlocation());
       List<TblLocationOfLaundry>listLaundry = parentController.getFReportManager().getAllDataLaundryByIdLocation(location.getIdlocation());
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
      
      return result;
    }
    
    
    private void reportStokOpnamePrintHandle(){
       Date startDate = null;
       Date endDate = null;
       TblLocation location = null;
       TblItem item = null;
       TblStockOpname stockOpname = null;
       SwingNode swingNode = new SwingNode();
       
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
          startDate = Date.valueOf(dpStartDate.getValue());
          endDate = Date.valueOf(dpEndDate.getValue());
        }
       
       if(chbLocation.isSelected()==true){
           if(cbpLocation.getValue()!=null){
             location = cbpLocation.getValue().getLocation();
           }
       }
       
       if(chbItem.isSelected()==true){
           if(cbpItem.getValue()!=null){
             item = cbpItem.getValue();
           }
       }
      
       if(chbCodeStockOpname.isSelected()==true){
           if(cbpCodeStockOpname.getValue()!=null){
             stockOpname = cbpCodeStockOpname.getValue();
           }
       }
      
       
     List<TblStockOpnameDetail>list = parentController.getFReportManager().getAllDataStockOpnameForReport(startDate,endDate,stockOpname,item,location);
     swingNode = printReportStokOpname(list,startDate,endDate);
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportStokOpname();
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportStokOpnameController(FeatureReportItemController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportItemController parentController;
}
