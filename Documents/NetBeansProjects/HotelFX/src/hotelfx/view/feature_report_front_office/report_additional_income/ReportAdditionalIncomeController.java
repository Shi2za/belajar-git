/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office.report_additional_income;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.helper.ClassAdditionalType;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanPendapatanTambahan;
import hotelfx.helper.PrintModel.ClassPrintLaporanPendapatanTambahanDetail;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.view.feature_report_front_office.FeatureReportFrontOfficeController;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Andreas
 */
public class ReportAdditionalIncomeController implements Initializable{
    @FXML
    private JFXDatePicker dpStartPeriode;
    @FXML
    private JFXDatePicker dpEndPeriode;
    @FXML
    private JFXCheckBox chbAdditionalType;
    @FXML
    private AnchorPane ancAdditionalType;
    @FXML
    private JFXCheckBox chbAdditionalService;
    @FXML
    private AnchorPane ancAdditionalService;
    @FXML
    private JFXCheckBox chbAdditionalItem;
    @FXML
    private AnchorPane ancAdditionalItem;
    @FXML
    private AnchorPane ancReportView;
    @FXML
    private JFXButton btnShow;
    ToggleGroup tglReport;
    
    JFXCComboBoxTablePopup<TblRoomService> cbpService;
    JFXCComboBoxTablePopup<TblItem> cbpItem;
    JFXCComboBoxTablePopup<ClassAdditionalType>cbpAdditionalType;
    
    private void initForm(){
      btnShow.setOnAction((e)->{
        reportAdditionalIncomePrintHandle();  
      });
      
      /* tglReport = new ToggleGroup();
       rdAllAdditional.setToggleGroup(tglReport);
       rdAdditionalService.setToggleGroup(tglReport);
       rdAdditionalItem.setToggleGroup(tglReport); */
       
       chbAdditionalService.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             initDataPopUpAdditionalService(); 
            }
           else{
             cbpService.setVisible(false);
           }
       });
       
       chbAdditionalItem.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             initDataPopUpAdditionalItem();  
           }
           else{
              cbpItem.setVisible(false);
           }
       });
       
       chbAdditionalType.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             initDataPopupAdditionalType();
           }
           else{
             cbpAdditionalType.setVisible(false);
           }
       });
       
        SwingNode swingNode = new SwingNode();
        JRViewer jrView = new JRViewer(null);
        swingNode.setContent(jrView);
        swingNodeLayout(swingNode);
        
        ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartPeriode,dpEndPeriode);
    }
    
    private void initDataPopUpAdditionalService(){
       TableView<TblRoomService>tblService = new TableView();
       TableColumn<TblRoomService,String>serviceName = new TableColumn("Layanan");
       serviceName.setCellValueFactory(cellData -> cellData.getValue().serviceNameProperty());
       tblService.getColumns().add(serviceName);
       ObservableList<TblRoomService>listRoomService = FXCollections.observableArrayList(loadAllDataAdditionalService());
       
      cbpService = new JFXCComboBoxTablePopup(TblRoomService.class,tblService,listRoomService,"","Layanan",false,500,300);
      cbpService.setLabelFloat(false);
      ancAdditionalService.getChildren().clear();
      AnchorPane.setTopAnchor(cbpService,0.0);
      AnchorPane.setBottomAnchor(cbpService,0.0);
      AnchorPane.setLeftAnchor(cbpService,0.0);
      AnchorPane.setRightAnchor(cbpService,0.0);
      ancAdditionalService.getChildren().add(cbpService);
    }
    
     private void initDataPopUpAdditionalItem(){
       TableView<TblItem>tblAdditionalItem = new TableView();
       TableColumn<TblItem,String>itemName = new TableColumn("Barang");
       itemName.setCellValueFactory(cellData ->cellData.getValue().itemNameProperty());
       tblAdditionalItem.getColumns().add(itemName);
       ObservableList<TblItem>listItem = FXCollections.observableArrayList(loadAllDataAdditionalItem());
       
      cbpItem = new JFXCComboBoxTablePopup(TblRoomService.class,tblAdditionalItem,listItem,"","Barang",false,500,300);
      cbpItem.setLabelFloat(false);
      ancAdditionalItem.getChildren().clear();
      AnchorPane.setTopAnchor(cbpItem,0.0);
      AnchorPane.setBottomAnchor(cbpItem,0.0);
      AnchorPane.setLeftAnchor(cbpItem,0.0);
      AnchorPane.setRightAnchor(cbpItem,0.0);
      ancAdditionalItem.getChildren().add(cbpItem);
     }
     
     private void initDataPopupAdditionalType(){
       TableView<ClassAdditionalType>tblAdditionalType = new TableView();
       TableColumn<ClassAdditionalType,String>additionalTypeName = new TableColumn("Tipe");
       additionalTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
       tblAdditionalType.getColumns().addAll(additionalTypeName);
       
       ObservableList<ClassAdditionalType>listAdditionalType = FXCollections.observableArrayList(loadAllDataAdditionalType());
       
       cbpAdditionalType = new JFXCComboBoxTablePopup(ClassAdditionalType.class,tblAdditionalType,listAdditionalType,"","Tipe",false,500,300);
       ancAdditionalType.getChildren().clear();
       AnchorPane.setTopAnchor(cbpAdditionalType,0.0);
       AnchorPane.setBottomAnchor(cbpAdditionalType,0.0);
       AnchorPane.setLeftAnchor(cbpAdditionalType,0.0);
       AnchorPane.setRightAnchor(cbpAdditionalType,0.0);
       ancAdditionalType.getChildren().add(cbpAdditionalType);
     }
     
    private List<TblRoomService>loadAllDataAdditionalService(){
       List<TblRoomService>list = new ArrayList();
     //  TblReservationAdditionalService additionalService = new TblReservationAdditionalService();
    /*   TblRoomService roomService = new TblRoomService();
       roomService.setServiceName("Semua Layanan");
      // additionalService.setTblRoomService(roomService);
       list.add(roomService); */
       list.addAll(parentController.getFReportManager().getAllDataRoomService());
       
       return list;
    }
    
    private List<TblItem>loadAllDataAdditionalItem(){
       List<TblItem>list = new ArrayList();
     /*  TblItem item = new TblItem();
       item.setItemName("Semua Barang");
       list.add(item); */
       list.addAll(parentController.getFReportManager().getAllDataAdditionalItemByGuestStatus(true));   //Guest
       
       return list;
    }
    
    private List<ClassAdditionalType>loadAllDataAdditionalType(){
       List<ClassAdditionalType>list = new ArrayList();
       ClassAdditionalType additionalTypeService = new ClassAdditionalType();
       additionalTypeService.setIdType(1);
       additionalTypeService.setTypeName("Layanan");
       
       ClassAdditionalType additionalTypeItem = new ClassAdditionalType();
       additionalTypeItem.setIdType(2);
       additionalTypeItem.setTypeName("Barang");
       
       list.add(additionalTypeItem);
       list.add(additionalTypeService);
       
       return list;
    }
    
    private SwingNode printReportAdditionalIncome(List<TblReservationAdditionalService>listAdditionalService,
                                             List<TblReservationAdditionalItem>listAdditionalItem){
      
       List<ClassPrintLaporanPendapatanTambahan>listPendapatanTambahan = new ArrayList();
       
       BigDecimal harga = new BigDecimal(0);
       BigDecimal diskon = new BigDecimal(0);
       BigDecimal tempHargaDiskon = new BigDecimal(0);
       BigDecimal serviceCharge = new BigDecimal(0);
       BigDecimal tempHargaDiskonServiceCharge = new BigDecimal(0);
       BigDecimal tax = new BigDecimal(0);
       BigDecimal totalHarga = new BigDecimal(0);
       BigDecimal totalDiskon = new BigDecimal(0);
       BigDecimal totalServiceCharge = new BigDecimal(0);
       BigDecimal totalTax = new BigDecimal(0);
       
       String kodeReservasi = "";
       String tipeTambahan = "";
       BigDecimal jumlah = new BigDecimal(0);
       String satuan = "";
       BigDecimal total = new BigDecimal(0);
       String namaBarang = "";
       
       ClassPrintLaporanPendapatanTambahan pendapatanTambahan = null;
       
  //insert data layanan     
       for(TblReservationAdditionalService getAdditionalService : listAdditionalService){
         kodeReservasi = getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation();
         diskon = getAdditionalService.getPrice().multiply(getAdditionalService.getDiscountPercentage().divide(new BigDecimal(100)));
         tempHargaDiskon = getAdditionalService.getPrice().subtract(diskon);
         serviceCharge = getServiceCharge(getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),
                                         getAdditionalService.getRefReservationBillType().getIdtype(),tempHargaDiskon);
         tempHargaDiskonServiceCharge = tempHargaDiskon.add(serviceCharge);
         tax = getTax(getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),
                                      getAdditionalService.getRefReservationBillType().getIdtype(),tempHargaDiskonServiceCharge);
         tipeTambahan = "Layanan";
         harga = getAdditionalService.getPrice();
         jumlah = new BigDecimal(1);
         total = tempHargaDiskonServiceCharge.add(tax);
         satuan = "-";
         namaBarang = getAdditionalService.getTblRoomService().getServiceName();
         kodeReservasi = getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation();
         
           if(pendapatanTambahan==null){
             List<ClassPrintLaporanPendapatanTambahanDetail>listPendapatanTambahanDetail = new ArrayList();
             pendapatanTambahan = new ClassPrintLaporanPendapatanTambahan();
             pendapatanTambahan.setTanggalReservasi(Date.valueOf(((java.sql.Date)getAdditionalService.getAdditionalDate()).toLocalDate().plusDays(1)));
             
             ClassPrintLaporanPendapatanTambahanDetail additionalService = 
             new ClassPrintLaporanPendapatanTambahanDetail(kodeReservasi,tipeTambahan,namaBarang,harga,jumlah,satuan,harga.multiply(jumlah),diskon,tax,serviceCharge,total);
             listPendapatanTambahanDetail.add(additionalService);
             pendapatanTambahan.setLaporanPendapatanDetail(listPendapatanTambahanDetail);
             pendapatanTambahan.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
             listPendapatanTambahan.add(pendapatanTambahan);
            }
           else{
               if(pendapatanTambahan.getTanggalReservasi().equals(Date.valueOf(((java.sql.Date)getAdditionalService.getAdditionalDate()).toLocalDate().plusDays(1)))){
                 ClassPrintLaporanPendapatanTambahanDetail additionalService = 
                 new ClassPrintLaporanPendapatanTambahanDetail(kodeReservasi,tipeTambahan,namaBarang,harga,jumlah,satuan,harga.multiply(jumlah),diskon,tax,serviceCharge,total);
                 pendapatanTambahan.getLaporanPendapatanDetail().add(additionalService);
               }
               else{
                 List<ClassPrintLaporanPendapatanTambahanDetail>listPendapatanTambahanDetail = new ArrayList();
                 pendapatanTambahan = new ClassPrintLaporanPendapatanTambahan();
                 pendapatanTambahan.setTanggalReservasi(Date.valueOf(((java.sql.Date)getAdditionalService.getAdditionalDate()).toLocalDate().plusDays(1)));
                 ClassPrintLaporanPendapatanTambahanDetail additionalService = 
                 new ClassPrintLaporanPendapatanTambahanDetail(kodeReservasi,tipeTambahan,namaBarang,harga,jumlah,satuan,harga.multiply(jumlah),diskon,tax,serviceCharge,total);
                 listPendapatanTambahanDetail.add(additionalService);
                 pendapatanTambahan.setLaporanPendapatanDetail(listPendapatanTambahanDetail);
                 pendapatanTambahan.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
                 listPendapatanTambahan.add(pendapatanTambahan);
               }
            }
         //listPendapatanTambahanDetail.add(additionalService);
       }
       
  //insert data barang     
       for(TblReservationAdditionalItem getAdditionalItem : listAdditionalItem){
        // ClassPrintLaporanPendapatanTambahanDetail additionalItem = new ClassPrintLaporanPendapatanTambahanDetail();
         BigDecimal totalHargaItem = getAdditionalItem.getItemCharge().multiply(getAdditionalItem.getItemQuantity());
         diskon = totalHargaItem.multiply(getAdditionalItem.getDiscountPercentage().divide(new BigDecimal(100)));
         tempHargaDiskon = totalHargaItem.subtract(diskon);
         serviceCharge = getServiceCharge(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),
                                               getAdditionalItem.getRefReservationBillType().getIdtype(),tempHargaDiskon);
         tempHargaDiskonServiceCharge = tempHargaDiskon.add(serviceCharge);
         tax = getTax(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),
                           getAdditionalItem.getRefReservationBillType().getIdtype(),tempHargaDiskonServiceCharge);
         tipeTambahan = "Barang";
         harga = getAdditionalItem.getItemCharge();
         jumlah = getAdditionalItem.getItemQuantity();
         total = tempHargaDiskonServiceCharge.add(tax);
         satuan = getAdditionalItem.getTblItem().getTblUnit().getUnitName();
         namaBarang = getAdditionalItem.getTblItem().getItemName();
         kodeReservasi = getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation();
         
         boolean check = false;
           for(ClassPrintLaporanPendapatanTambahan getPendapatanTambahan : listPendapatanTambahan){
               if(getPendapatanTambahan.getTanggalReservasi().equals(getAdditionalItem.getAdditionalDate())){
                   ClassPrintLaporanPendapatanTambahanDetail additionalItem = 
                   new ClassPrintLaporanPendapatanTambahanDetail(kodeReservasi,tipeTambahan,namaBarang,harga,jumlah,satuan,totalHargaItem,diskon,tax,serviceCharge,total);
                  getPendapatanTambahan.getLaporanPendapatanDetail().add(additionalItem);
                  check = true;
               }
            }
           
           if(!check){
             List<ClassPrintLaporanPendapatanTambahanDetail>listPendapatanTambahanDetail = new ArrayList();
             pendapatanTambahan = new ClassPrintLaporanPendapatanTambahan();
             pendapatanTambahan.setTanggalReservasi(getAdditionalItem.getAdditionalDate());
             ClassPrintLaporanPendapatanTambahanDetail additionalItem = 
             new ClassPrintLaporanPendapatanTambahanDetail(kodeReservasi,tipeTambahan,namaBarang,harga,jumlah,satuan,totalHargaItem,diskon,tax,serviceCharge,total);
             listPendapatanTambahanDetail.add(additionalItem);
             pendapatanTambahan.setLaporanPendapatanDetail(listPendapatanTambahanDetail);
             pendapatanTambahan.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
             listPendapatanTambahan.add(pendapatanTambahan);
           }
       }
       
       Collections.sort(listPendapatanTambahan,Comparator.comparing(ClassPrintLaporanPendapatanTambahan::getTanggalReservasi));
      //laporanPendapatanTambahan.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
     // laporanPendapatanTambahan.setLaporanPendapatanDetail(listPendapatanTambahanDetail);
    //  laporanPendapatanTambahan.setTanggalReservasi(null);
       String periode = ""; 
       if(dpStartPeriode.getValue()!=null && dpEndPeriode!=null){
          periode = new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpStartPeriode.getValue()))+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpEndPeriode.getValue()));
       }
       else{
         periode = "-";  
       }
       totalHarga = totalHarga(listPendapatanTambahan);
       totalDiskon = totalDiscount(listPendapatanTambahan);
       System.out.println("total Diskon :"+totalDiskon);
       totalServiceCharge = totalServiceCharge(listPendapatanTambahan);
       System.out.println("total service charge :"+totalServiceCharge);
       totalTax = totalTax(listPendapatanTambahan);
       System.out.println("total pajak :"+totalTax);
       
       return ClassPrinter.printReportAdditionalIncome(listPendapatanTambahan,periode,totalHarga,totalDiskon,totalServiceCharge,totalTax);
    }
    
    private BigDecimal getServiceCharge(long id,int idType,BigDecimal price){
       List<TblReservationBill>list = parentController.getFReportManager().getAllDataReservationBill(id,idType);
       BigDecimal serviceCharge = new BigDecimal(0);
       for(TblReservationBill getReservationBill : list){
         serviceCharge = price.multiply(getReservationBill.getServiceChargePercentage());
       }
      return serviceCharge;
    }
    
    private BigDecimal getTax(long id,int idType,BigDecimal price){
       List<TblReservationBill>list = parentController.getFReportManager().getAllDataReservationBill(id,idType);
       BigDecimal tax = new BigDecimal(0);
       for(TblReservationBill getReservationBill : list){
         tax = price.multiply(getReservationBill.getTaxPercentage());
       }
      return tax;
    }
    
    private BigDecimal totalHarga(List<ClassPrintLaporanPendapatanTambahan>list){
      BigDecimal totalHarga = new BigDecimal(0);
      for(ClassPrintLaporanPendapatanTambahan getLaporanPendapatanTambahan : list){
           for(ClassPrintLaporanPendapatanTambahanDetail getLaporanPendapatanTambahanDetail : getLaporanPendapatanTambahan.getLaporanPendapatanDetail()){
             totalHarga = totalHarga.add(getLaporanPendapatanTambahanDetail.getTotalHarga());
            } 
        }
      return totalHarga;
    }
    
    private BigDecimal totalDiscount(List<ClassPrintLaporanPendapatanTambahan>list){
       BigDecimal totalDiskon = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanTambahan getLaporanPendapatanTambahan : list){
           for(ClassPrintLaporanPendapatanTambahanDetail getLaporanPendapatanTambahanDetail : getLaporanPendapatanTambahan.getLaporanPendapatanDetail()){
             totalDiskon = totalDiskon.add(getLaporanPendapatanTambahanDetail.getDiskon());
           }
        }
       return totalDiskon;
    }
    
    private BigDecimal totalServiceCharge(List<ClassPrintLaporanPendapatanTambahan>list){
       BigDecimal totalServiceCharge = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanTambahan getLaporanPendapatanTambahan : list){
           for(ClassPrintLaporanPendapatanTambahanDetail getLaporanPendapatanTambahanDetail : getLaporanPendapatanTambahan.getLaporanPendapatanDetail()){
             totalServiceCharge = totalServiceCharge.add(getLaporanPendapatanTambahanDetail.getServiceCharge());
           }
       }
      return totalServiceCharge;
    }
    
    private BigDecimal totalTax(List<ClassPrintLaporanPendapatanTambahan>list){
       BigDecimal totalTax = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanTambahan getLaporanPendapatanTambahan : list){
           for(ClassPrintLaporanPendapatanTambahanDetail getLaporanPendapatanTambahanDetail : getLaporanPendapatanTambahan.getLaporanPendapatanDetail()){
             totalTax = totalTax.add(getLaporanPendapatanTambahanDetail.getPajak());
           }
        }
       return totalTax;
    }
    
    private void reportAdditionalIncomePrintHandle(){
      Date startDate = null;
      Date endDate = null;
      TblRoomService roomService = null;
      TblItem tblItem = null;
      ClassAdditionalType additionalType= null;
      
      List<TblReservationAdditionalService>listService = new ArrayList();
      List<TblReservationAdditionalItem>listItem = new ArrayList();
       
       if(dpStartPeriode.getValue()!=null && dpEndPeriode.getValue()!=null){
         startDate = Date.valueOf(dpStartPeriode.getValue());
         endDate = Date.valueOf(dpEndPeriode.getValue());
        }
       
       if(chbAdditionalService.isSelected()){
          if(cbpService.getValue()!=null){
             roomService = cbpService.getValue();
           }
       }
      
      if(chbAdditionalItem.isSelected()){
           if(cbpItem.getValue()!=null){
             tblItem = cbpItem.getValue();
           }
        // roomService = null;
         
      }
      
    /*  if(chbAdditionalType.isSelected()){
           if(cbpAdditionalType.getValue()!=null){
               additionalType = cbpAdditionalType.getValue();
               if(cbpAdditionalType.getValue().getIdType()==1){
                 listService = parentController.getFReportManager().getAllDataAdditionalServicePerPeriode(startDate, endDate, roomService,additionalType);
                 listItem = new ArrayList();
               }
               if(cbpAdditionalType.getValue().getIdType()==2){
                 listItem = parentController.getFReportManager().getAllDataAdditionalItemPerPeriode(startDate, endDate, tblItem,additionalType);
                 listService = new ArrayList();
               }
            }
           else{
             listService = parentController.getFReportManager().getAllDataAdditionalServicePerPeriode(startDate,endDate,roomService,additionalType);
             listItem = parentController.getFReportManager().getAllDataAdditionalItemPerPeriode(startDate, endDate, tblItem,additionalType);
           }
       //  listService = parentController.getFReportManager().getAllDataAdditionalServicePerPeriode(startDate,endDate,roomService);
       //  listItem = parentController.getFReportManager().getAllDataAdditionalItemPerPeriode(startDate, endDate, tblItem);
      }
      else{
         listService = parentController.getFReportManager().getAllDataAdditionalServicePerPeriode(startDate,endDate,roomService,additionalType);
         listItem = parentController.getFReportManager().getAllDataAdditionalItemPerPeriode(startDate, endDate, tblItem,additionalType);
      }
     // List<TblReservationAdditionalService>listService = parentController.getFReportManager().getAllDataAdditionalServicePerPeriode(startDate,endDate,roomService);
      System.out.println("size service:"+listService.size());
     
      System.out.println("size item:"+listItem.size());
         
      SwingNode swingNode = printReportAdditionalIncome(listService,listItem);
      swingNodeLayout(swingNode); */
    }
    
    private void swingNodeLayout(SwingNode swingNode){
       ancReportView.getChildren().clear();
       AnchorPane.setTopAnchor(swingNode,0.0);
       AnchorPane.setBottomAnchor(swingNode,15.0);
       AnchorPane.setLeftAnchor(swingNode,15.0);
       AnchorPane.setRightAnchor(swingNode,15.0);
       ancReportView.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initForm();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public ReportAdditionalIncomeController(FeatureReportFrontOfficeController parentController){
       this.parentController = parentController;
    }
    private final FeatureReportFrontOfficeController parentController;
}
