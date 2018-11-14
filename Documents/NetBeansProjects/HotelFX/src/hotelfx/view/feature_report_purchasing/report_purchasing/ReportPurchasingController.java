/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_purchasing.report_purchasing;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanPembelianBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanPembelianBarangDetail;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblPurchaseRequest;
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
public class ReportPurchasingController implements Initializable {
    
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbCodePO;
    @FXML
    private AnchorPane ancCodePO;
    private JFXCComboBoxTablePopup<TblPurchaseOrder>cbpCodePO;
    @FXML
    private JFXCheckBox chbStatusPO;
    @FXML
    private AnchorPane ancStatusPO;
    private JFXCComboBoxTablePopup<RefPurchaseOrderStatus>cbpStatusPO;
    @FXML
    private JFXCheckBox chbItem;
    @FXML
    private AnchorPane ancItem;
    private JFXCComboBoxTablePopup<TblItem>cbpItem;
    @FXML
    private JFXCheckBox chbSupplier;
    @FXML
    private AnchorPane ancSupplier;
    private JFXCComboBoxTablePopup<TblSupplier>cbpSupplier;
    @FXML
    private JFXCheckBox chbCodeMR;
    @FXML
    private AnchorPane ancCodeMR;
    private JFXCComboBoxTablePopup<TblPurchaseRequest>cbpCodeMR;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane printReportPurchasingLayout;
    
    private void initFormReportPurchaseOrder(){
      initDataPopup();
      btnShow.setOnAction((e)->{
        reportPurchasingPrintHandle();
      });
      
      cbpCodePO.setVisible(false);
      cbpStatusPO.setVisible(false);
      cbpItem.setVisible(false);
      cbpSupplier.setVisible(false);
      cbpCodeMR.setVisible(false);
      
      chbCodePO.selectedProperty().addListener((obs,oldVal,newVal)->{
         cbpCodePO.setVisible(newVal==true);
         cbpCodePO.setValue(null);
      });
      
      chbStatusPO.selectedProperty().addListener((obs,oldVal,newVal)->{
         cbpStatusPO.setVisible(newVal==true);
         cbpStatusPO.setValue(null);
      });
      
      chbItem.selectedProperty().addListener((obs,oldVal,newVal)->{
         cbpItem.setVisible(newVal == true);
         cbpItem.setValue(null);
      });
      
      chbSupplier.selectedProperty().addListener((obs,oldVal,newVal)->{
         cbpSupplier.setVisible(newVal == true);
         cbpSupplier.setValue(null);
      });
      
      chbCodeMR.selectedProperty().addListener((obs,oldVal,newVal)->{
         cbpCodeMR.setVisible(newVal == true);
         cbpCodeMR.setValue(null);
      });
      
      JRViewer jrViewer = new JRViewer(null);
      SwingNode swingNode = new SwingNode();
      swingNode.setContent(jrViewer);
      reportPurchasingLayout(swingNode);
      
      initDateCalendar();
    }
    
    private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
    
    private void initDataPopup(){
      TableView<TblPurchaseOrder>tblPO = new TableView();
      TableColumn<TblPurchaseOrder,String>poCode = new TableColumn("Kode PO");
      poCode.setMinWidth(100);
      poCode.setCellValueFactory(cellData -> cellData.getValue().codePoProperty());
      TableColumn<TblPurchaseOrder,String>supplier = new TableColumn("Supplier");
      supplier.setMinWidth(200);
      supplier.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblSupplier().getSupplierName(),param.getValue().tblSupplierProperty()));
      tblPO.getColumns().addAll(poCode,supplier);
      
      TableView<RefPurchaseOrderStatus>tblPOStatus = new TableView();
      TableColumn<RefPurchaseOrderStatus,String>poStatus = new TableColumn("Status PO");
      poStatus.setMinWidth(200);
      poStatus.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
      tblPOStatus.getColumns().addAll(poStatus);
      
      TableView<TblItem>tblItem = new TableView();
      TableColumn<TblItem,String>codeItem = new TableColumn("Kode Barang");
      codeItem.setMinWidth(100);
      codeItem.setCellValueFactory(cellData->cellData.getValue().codeItemProperty());
      TableColumn<TblItem,String>nameItem = new TableColumn("Nama Barang");
      nameItem.setMinWidth(200);
      nameItem.setCellValueFactory(cellData->cellData.getValue().itemNameProperty());
      tblItem.getColumns().addAll(codeItem,nameItem);
      
      TableView<TblSupplier>tblSupplier = new TableView();
      TableColumn<TblSupplier,String>codeSupplier = new TableColumn("Kode Supplier");
      codeSupplier.setMinWidth(100);
      codeSupplier.setCellValueFactory(cellData->cellData.getValue().codeSupplierProperty());
      TableColumn<TblSupplier,String>nameSupplier = new TableColumn("Nama Supplier");
      nameSupplier.setMinWidth(160);
      nameSupplier.setCellValueFactory(cellData->cellData.getValue().supplierNameProperty());
      tblSupplier.getColumns().addAll(codeSupplier,nameSupplier);
      
      TableView<TblPurchaseRequest>tblMR = new TableView();
      TableColumn<TblPurchaseRequest,String>codeMR = new TableColumn("Kode MR");
      codeMR.setMinWidth(120);
      codeMR.setCellValueFactory(cellData -> cellData.getValue().codePrProperty());
      TableColumn<TblPurchaseRequest,String>employeeName = new TableColumn("Nama");
      employeeName.setMinWidth(180);
      employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByCreatedBy().getTblPeople().getFullName(),param.getValue().tblEmployeeByCreatedByProperty()));
      TableColumn<TblPurchaseRequest,String>employeeCode = new TableColumn("ID");
      employeeCode.setMinWidth(120);
      employeeCode.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByCreatedBy().getCodeEmployee(),param.getValue().tblEmployeeByCreatedByProperty()));
      TableColumn<TblPurchaseRequest,String>employeeJob = new TableColumn("Jabatan");
      employeeJob.setMinWidth(160);
      employeeJob.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByCreatedBy().getTblJob()!=null ? param.getValue().getTblEmployeeByCreatedBy().getTblJob().getJobName() : "-",param.getValue().tblEmployeeByCreatedByProperty()));
      TableColumn<TblPurchaseRequest,String>employee = new TableColumn("Karyawan");
      employee.getColumns().addAll(employeeCode,employeeName,employeeJob);
      tblMR.getColumns().addAll(codeMR,employee);
      
      ObservableList<TblPurchaseOrder>listPOItems = FXCollections.observableArrayList(loadAllDataPurchaseOrder());
      cbpCodePO = new JFXCComboBoxTablePopup(TblPurchaseOrder.class,tblPO,listPOItems,"","Kode PO",false,500,300);
      cbpCodePO.setLabelFloat(false);
      
      ancCodePO.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpCodePO,0.0);
      AnchorPane.setLeftAnchor(cbpCodePO,0.0);
      AnchorPane.setRightAnchor(cbpCodePO,0.0);
      AnchorPane.setTopAnchor(cbpCodePO,0.0);
      ancCodePO.getChildren().add(cbpCodePO);
      
      ObservableList<RefPurchaseOrderStatus>listPoStatusItems = FXCollections.observableArrayList(loadAllDataPurchaseOrderStatus());
      cbpStatusPO = new JFXCComboBoxTablePopup(RefPurchaseOrderStatus.class,tblPOStatus,listPoStatusItems,"","Status PO",false,500,300);
      cbpStatusPO.setLabelFloat(false);
      ancStatusPO.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpStatusPO,0.0);
      AnchorPane.setLeftAnchor(cbpStatusPO,0.0);
      AnchorPane.setRightAnchor(cbpStatusPO,0.0);
      AnchorPane.setTopAnchor(cbpStatusPO,0.0);
      ancStatusPO.getChildren().add(cbpStatusPO);
      
      ObservableList<TblItem>listItems = FXCollections.observableArrayList(loadAllDataItem());
      cbpItem = new JFXCComboBoxTablePopup(TblItem.class,tblItem,listItems,"","Barang",false,500,300);
      cbpItem.setLabelFloat(false);
      ancItem.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpItem,0.0);
      AnchorPane.setLeftAnchor(cbpItem,0.0);
      AnchorPane.setRightAnchor(cbpItem,0.0);
      AnchorPane.setTopAnchor(cbpItem,0.0);
      ancItem.getChildren().add(cbpItem);
      
      ObservableList<TblSupplier>listSupplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
      cbpSupplier = new JFXCComboBoxTablePopup(TblSupplier.class,tblSupplier,listSupplierItems,"","Supplier",false,500,300);
      cbpSupplier.setLabelFloat(false);
      ancSupplier.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpSupplier,0.0);
      AnchorPane.setLeftAnchor(cbpSupplier,0.0);
      AnchorPane.setRightAnchor(cbpSupplier,0.0);
      AnchorPane.setTopAnchor(cbpSupplier,0.0);
      ancSupplier.getChildren().add(cbpSupplier);
      
      ObservableList<TblPurchaseRequest>listMaterialRequestItems = FXCollections.observableArrayList(loadAllDataMaterialRequest());
      cbpCodeMR = new JFXCComboBoxTablePopup(TblPurchaseRequest.class,tblMR,listMaterialRequestItems,"","Kode MR",false,500,300);
      cbpCodeMR.setLabelFloat(false);
      ancCodeMR.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpCodeMR,0.0);
      AnchorPane.setLeftAnchor(cbpCodeMR,0.0);
      AnchorPane.setRightAnchor(cbpCodeMR,0.0);
      AnchorPane.setTopAnchor(cbpCodeMR,0.0);
      ancCodeMR.getChildren().add(cbpCodeMR);
    }
    
    private List<TblPurchaseOrder>loadAllDataPurchaseOrder(){
      return parentController.getFReportManager().getAllDataPurchaseOrder();
    }
    
   private List<RefPurchaseOrderStatus>loadAllDataPurchaseOrderStatus(){
     return parentController.getFReportManager().getAllPurchasingStatus();
   }
   
   private List<TblItem>loadAllDataItem(){
     return parentController.getFReportManager().getAllDataItem();
   }
   
   private List<TblSupplier>loadAllDataSupplier(){
     return parentController.getFReportManager().getAllDataSupplier();
   }
   
   private List<TblPurchaseRequest>loadAllDataMaterialRequest(){
     return parentController.getFReportManager().getAllTblPurchaseRequest();
   }
   
    private SwingNode printReportPurchaseOrder(List<TblPurchaseOrderDetail>list,Date startDate,Date endDate){
       List<ClassPrintLaporanPembelianBarang>listLaporanPembelianBarang = new ArrayList();
       ClassPrintLaporanPembelianBarang laporanPembelianBarang = new ClassPrintLaporanPembelianBarang();
       laporanPembelianBarang.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       laporanPembelianBarang.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+" - "+ new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate) : "-");
       List<ClassPrintLaporanPembelianBarangDetail>listPembelianBarangDetail = new ArrayList();
       for(TblPurchaseOrderDetail getPurchaseOrder : list){
          ClassPrintLaporanPembelianBarangDetail laporanPurchasingDetail = new ClassPrintLaporanPembelianBarangDetail();
          laporanPurchasingDetail.setDiskon(getPurchaseOrder.getItemDiscount());
          laporanPurchasingDetail.setHarga(getPurchaseOrder.getItemCost());
          laporanPurchasingDetail.setJumlah(getPurchaseOrder.getItemQuantity());
          laporanPurchasingDetail.setKodeBarang(getPurchaseOrder.getTblSupplierItem().getTblItem().getCodeItem());
          laporanPurchasingDetail.setKodePO(getPurchaseOrder.getTblPurchaseOrder().getCodePo());
          laporanPurchasingDetail.setKodeMR(getPurchaseOrder.getTblPurchaseOrder().getTblPurchaseRequest().getCodePr());
          laporanPurchasingDetail.setKodeSupplier(getPurchaseOrder.getTblPurchaseOrder().getTblSupplier().getCodeSupplier());
          laporanPurchasingDetail.setNamaBarang(getPurchaseOrder.getTblSupplierItem().getTblItem().getItemName());
       //   laporanPurchasingDetail.setNamaSupplier(getPurchaseOrder.getTblPurchaseOrder().getTblSupplier().getSupplierName());
          laporanPurchasingDetail.setStatus(getPurchaseOrder.getTblPurchaseOrder().getRefPurchaseOrderStatus().getStatusName());
          laporanPurchasingDetail.setUnit(getPurchaseOrder.getTblSupplierItem().getTblItem().getTblUnit().getUnitName());
          laporanPurchasingDetail.setTotal(getPurchaseOrder.getItemQuantity().multiply(getPurchaseOrder.getItemCost().subtract(getPurchaseOrder.getItemDiscount())));
          laporanPurchasingDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(new Date(getPurchaseOrder.getTblPurchaseOrder().getPodate().getTime())));
          listPembelianBarangDetail.add(laporanPurchasingDetail);
       }
       laporanPembelianBarang.setListLaporanPembelianBarangDetail(listPembelianBarangDetail);
       listLaporanPembelianBarang.add(laporanPembelianBarang);
       return ClassPrinter.printLaporanPembelianBarang(listLaporanPembelianBarang);
    }
    
    private void reportPurchasingPrintHandle(){
      Date startDate = null;
      Date endDate = null;
      TblItem item = null;
      RefPurchaseOrderStatus purchaseOrderStatus = null;
      TblPurchaseOrder purchaseOrder = null;
      TblSupplier supplier = null;
      TblPurchaseRequest materialRequest = null;
      SwingNode swingNode = new SwingNode();
      
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(chbCodePO.isSelected()==true){
           if(cbpCodePO.getValue()!=null){
             purchaseOrder = cbpCodePO.getValue();
           }
       }
       
       if(chbStatusPO.isSelected()==true){
           if(cbpStatusPO.getValue()!=null){
             purchaseOrderStatus = cbpStatusPO.getValue();
           } 
        }
       
       if(chbItem.isSelected()==true){
          if(cbpItem.getValue()!=null){
             item = cbpItem.getValue();
          }
       }
       
       if(chbSupplier.isSelected()==true){
           if(cbpSupplier.getValue()!=null){
             supplier = cbpSupplier.getValue();
            }    
        }
       
       if(chbCodeMR.isSelected()==true){
          if(cbpCodeMR.getValue()!=null){
             materialRequest = cbpCodeMR.getValue();
          } 
       }
       
       List<TblPurchaseOrderDetail>list = parentController.getFReportManager().getAllDataPurchaseOrderDetail(supplier,purchaseOrder,purchaseOrderStatus,item,startDate,endDate,materialRequest);
       
       System.out.println("list size : "+list.size());
       swingNode = printReportPurchaseOrder(list,startDate,endDate);
       reportPurchasingLayout(swingNode);
    }
    
    private void reportPurchasingLayout(SwingNode swingNode){
      printReportPurchasingLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,0.0);
      printReportPurchasingLayout.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportPurchaseOrder();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportPurchasingController(FeatureReportPurchasingController parentController){
        this.parentController = parentController;
    }
    private final FeatureReportPurchasingController parentController;
}
