/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_purchasing.report_retur;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanReturBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanReturBarangDetail;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_report_purchasing.FeatureReportPurchasingController;
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
public class ReportReturBarangController implements Initializable{
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbNoRetur;
    @FXML
    private AnchorPane ancCbpNoRetur;
    private JFXCComboBoxTablePopup<TblRetur> cbpNoRetur;
    @FXML
    private JFXCheckBox chbNoPO;
    @FXML
    private AnchorPane ancCbpNoPO;
    private JFXCComboBoxTablePopup<TblPurchaseOrder> cbpNoPO;
    @FXML
    private JFXCheckBox chbSupplier;
    @FXML
    private AnchorPane ancCbpSupplier;
    private JFXCComboBoxTablePopup<TblSupplier> cbpSupplier;
    @FXML
    private JFXCheckBox chbItem;
    @FXML
    private AnchorPane ancCbpItem;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane ancReportReturBarang;
    
    private void initFormLaporanReturBarang(){
       initDataPopup();
       btnShow.setOnAction((e)->{
         reportReturBarangDetailPrintHandle();  
       });
       
       cbpNoRetur.setVisible(false);
       cbpItem.setVisible(false);
       cbpSupplier.setVisible(false);
       cbpNoPO.setVisible(false);
       
       chbNoRetur.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             cbpNoRetur.setVisible(true);
             cbpNoRetur.setValue(null);
           }
           else{
             cbpNoRetur.setVisible(false);
           }
       });
       
       chbItem.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             cbpItem.setVisible(true);
             cbpItem.setValue(null);
           }
           else{
             cbpItem.setVisible(false);
           }
       });
       
       chbSupplier.selectedProperty().addListener((obs,oldVal,newVal)->{
          if(newVal==true){
             cbpSupplier.setVisible(true);
             cbpSupplier.setValue(null);
           }
           else{
             cbpSupplier.setVisible(false);
           } 
       });
       
       chbNoPO.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             cbpNoPO.setVisible(true);
             cbpNoPO.setValue(null);
           }
           else{
             cbpNoPO.setVisible(false);
           }
       });
       
       initDataCalendar();
       
       JRViewer jrView = new JRViewer(null);
       SwingNode swingNode = new SwingNode();
       swingNode.setContent(jrView);
       reportLaporanReturBarangLayout(swingNode);
    }
    
    private void initDataCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
    
    private void initDataPopup(){
      TableView<TblRetur>tblRetur = new TableView();
      TableColumn<TblRetur,String>returCode = new TableColumn("Kode Retur");
      returCode.setMinWidth(120);
      returCode.setCellValueFactory(cellData ->cellData.getValue().codeReturProperty());
      TableColumn<TblRetur,String>returSupplier = new TableColumn("Supplier");
      returSupplier.setMinWidth(180);
      returSupplier.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblSupplier().getSupplierName(),param.getValue().tblSupplierProperty()));
      TableColumn<TblRetur,String>returDeliveryNumber = new TableColumn("No. Surat Jalan");
      returDeliveryNumber.setMinWidth(120);
      returDeliveryNumber.setCellValueFactory(cellData -> cellData.getValue().deliveryNumberProperty());
      tblRetur.getColumns().addAll(returCode,returSupplier,returDeliveryNumber);
      ObservableList<TblRetur>listReturItems = FXCollections.observableArrayList(loadAllDataRetur());
      cbpNoRetur = new JFXCComboBoxTablePopup(TblRetur.class,tblRetur,listReturItems,"","No. Retur ",false,500,300);
      cbpNoRetur.setLabelFloat(false);
      
      TableView<TblPurchaseOrder>tblPO = new TableView();
      TableColumn<TblPurchaseOrder,String>poCode = new TableColumn("Kode PO");
      poCode.setMinWidth(120);
      poCode.setCellValueFactory(cellData -> cellData.getValue().codePoProperty());
      TableColumn<TblPurchaseOrder,String>poSupplier = new TableColumn("Supplier");
      poSupplier.setMinWidth(180);
      poSupplier.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblSupplier().getSupplierName(),param.getValue().tblSupplierProperty()));
      tblPO.getColumns().addAll(poCode,poSupplier);
      ObservableList<TblPurchaseOrder>listPOItems = FXCollections.observableArrayList(loadAllDataPurchaseOrder());
      cbpNoPO = new JFXCComboBoxTablePopup(TblPurchaseOrder.class,tblPO,listPOItems,"","Kode PO ",false,500,300);
      cbpNoPO.setLabelFloat(false);
      
      TableView<TblSupplier>tblSupplier = new TableView();
      TableColumn<TblSupplier,String>supplierCode = new TableColumn("Kode");
      supplierCode.setMinWidth(120);
      supplierCode.setCellValueFactory(cellData -> cellData.getValue().codeSupplierProperty());
      TableColumn<TblSupplier,String>supplierName = new TableColumn("Nama");
      supplierName.setMinWidth(180);
      supplierName.setCellValueFactory(cellData->cellData.getValue().supplierNameProperty());
      tblSupplier.getColumns().addAll(supplierCode,supplierName);
      ObservableList<TblSupplier>listSupplierItem = FXCollections.observableArrayList(loadAllDataSupplier());
      cbpSupplier = new JFXCComboBoxTablePopup(TblSupplier.class,tblSupplier,listSupplierItem,"","Supplier",false,500,300);
      cbpSupplier.setLabelFloat(false);
      
      TableView<TblItem>tblItem = new TableView();
      TableColumn<TblItem,String>itemCode = new TableColumn("Kode");
      itemCode.setMinWidth(120);
      itemCode.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
      TableColumn<TblItem,String>itemName = new TableColumn("Nama");
      itemName.setMinWidth(180);
      itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getItemName(),param.getValue().itemNameProperty()));
      tblItem.getColumns().addAll(itemCode,itemName);
      ObservableList<TblItem>listItem = FXCollections.observableArrayList(loadAllDataItem());
      cbpItem = new JFXCComboBoxTablePopup(TblItem.class,tblItem,listItem,"","Barang",false,500,300);
      cbpItem.setLabelFloat(false);
      
       ancCbpNoRetur.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpNoRetur,0.0);
       AnchorPane.setLeftAnchor(cbpNoRetur,0.0);
       AnchorPane.setRightAnchor(cbpNoRetur,0.0);
       AnchorPane.setTopAnchor(cbpNoRetur,0.0);
       ancCbpNoRetur.getChildren().add(cbpNoRetur);
       
       ancCbpNoPO.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpNoPO,0.0);
       AnchorPane.setLeftAnchor(cbpNoPO,0.0);
       AnchorPane.setRightAnchor(cbpNoPO,0.0);
       AnchorPane.setTopAnchor(cbpNoPO,0.0);
       ancCbpNoPO.getChildren().add(cbpNoPO);
       
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
    }
    
    private List<TblPurchaseOrder>loadAllDataPurchaseOrder(){
       return parentController.getFReportManager().getAllDataPurchaseOrder();
    }
    
    private List<TblItem>loadAllDataItem(){
      return parentController.getFReportManager().getAllDataItem();
    }
    
    private List<TblSupplier>loadAllDataSupplier(){
      return parentController.getFReportManager().getAllDataSupplier();
    }
    
    private List<TblRetur>loadAllDataRetur(){
      return parentController.getFReportManager().getAllDataRetur();
    }
    
    private SwingNode printLaporanReturBarang(List<TblReturDetail>list,Date startDate,Date endDate,TblSupplier supplier,TblItem item,TblPurchaseOrder po,TblRetur retur){
       List<ClassPrintLaporanReturBarang>listLaporanReturBarang =  new ArrayList();
       ClassPrintLaporanReturBarang laporanReturBarang = new ClassPrintLaporanReturBarang();
       laporanReturBarang.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       laporanReturBarang.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate): "-");
       List<ClassPrintLaporanReturBarangDetail>listLaporanReturBarangDetail = new ArrayList();
       for(TblReturDetail getReturDetail : list){
          ClassPrintLaporanReturBarangDetail laporanReturBarangDetail = new ClassPrintLaporanReturBarangDetail();
          laporanReturBarangDetail.setHarga(getReturDetail.getItemCost().subtract(getReturDetail.getItemDiscount()));
          laporanReturBarangDetail.setKodeBarang(getReturDetail.getTblSupplierItem() !=null ? getReturDetail.getTblSupplierItem().getTblItem().getCodeItem() : "-");
          laporanReturBarangDetail.setNamaBarang(getReturDetail.getTblSupplierItem() !=null ? getReturDetail.getTblSupplierItem().getTblItem().getItemName() : "-");
          laporanReturBarangDetail.setNoPO(getReturDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
          laporanReturBarangDetail.setNoSuratJalan(getReturDetail.getTblRetur().getCodeRetur());
          laporanReturBarangDetail.setQty(getReturDetail.getItemQuantity());
          laporanReturBarangDetail.setSupplier(getReturDetail.getTblRetur().getTblSupplier().getCodeSupplier()+"-"+getReturDetail.getTblRetur().getTblSupplier().getSupplierName());
          laporanReturBarangDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getReturDetail.getTblRetur().getReturDate()));
          laporanReturBarangDetail.setTotal(laporanReturBarangDetail.getQty().multiply(laporanReturBarangDetail.getHarga()));
          laporanReturBarangDetail.setUnit(getReturDetail.getTblSupplierItem() !=null ? getReturDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName() : "-");
          laporanReturBarangDetail.setNoRetur(getReturDetail.getTblRetur().getCodeRetur());
          listLaporanReturBarangDetail.add(laporanReturBarangDetail);
       }
      laporanReturBarang.setListLaporanReturBarangDetail(listLaporanReturBarangDetail);
      listLaporanReturBarang.add(laporanReturBarang);
      return ClassPrinter.printLaporanReturBarang(listLaporanReturBarang);
    }
    
    private void reportReturBarangDetailPrintHandle(){
       List<TblReturDetail>list = new ArrayList();
       Date startDate = null;
       Date endDate = null;
       TblRetur retur = null;
       TblPurchaseOrder po = null;
       TblSupplier supplier = null;
       TblItem item = null;
       SwingNode swingNode = new SwingNode();
       
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
          startDate = Date.valueOf(dpStartDate.getValue());
          endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(chbNoRetur.isSelected()==true){
           if(cbpNoRetur.getValue()!=null){
              retur = cbpNoRetur.getValue();
           }
       }
      
       if(chbSupplier.isSelected()==true){
           if(cbpSupplier.getValue()!=null){
             supplier = cbpSupplier.getValue();
           }
       }
      
       if(chbItem.isSelected()==true){
           if(cbpItem.getValue()!=null){
             item = cbpItem.getValue();
            }
       }
      
       if(chbNoPO.isSelected()==true){
           if(cbpNoPO.getValue()!=null){
             po = cbpNoPO.getValue();
           } 
       }
      
       list = parentController.getFReportManager().getAllReturDetail(startDate, endDate,supplier,item,po,retur);
       
       swingNode = printLaporanReturBarang(list,startDate,endDate,supplier,item,po,retur);
       reportLaporanReturBarangLayout(swingNode);
    }
    
    private void reportLaporanReturBarangLayout(SwingNode swingNode){
      ancReportReturBarang.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,0.0);
      ancReportReturBarang.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormLaporanReturBarang();
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportReturBarangController(FeatureReportPurchasingController parentController){
       this.parentController = parentController;
    }
    
    private final FeatureReportPurchasingController parentController;
}
