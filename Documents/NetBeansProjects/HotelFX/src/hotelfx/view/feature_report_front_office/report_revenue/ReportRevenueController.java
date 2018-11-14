/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office.report_revenue;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueBulanan;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueBulananDetail;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueHarian;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueHarianDetail;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueTahunan;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueTahunanDetail;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.view.feature_report_front_office.FeatureReportFrontOfficeController;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Andreas
 */



public class ReportRevenueController implements Initializable{
    @FXML
    private JFXDatePicker dpTanggal;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane ancCbpTypeReport;
    @FXML
    private AnchorPane ancDaily;
    @FXML
    private AnchorPane ancMonthly;
    @FXML
    private AnchorPane ancYearly;
    @FXML
    private HBox hbReportRevenueMonthly;
    @FXML
    private Spinner<Integer> spnMonthYearly;
    @FXML
    private Spinner<Integer>spnYearly;
    @FXML
    private AnchorPane ancCbpMonthly;
    @FXML
    private AnchorPane ancReportView;
    
    ToggleGroup tglReport;
    JFXCComboBoxTablePopup<Month> cbpMonth;
    JFXCComboBoxTablePopup<TypeReport>cbpTypeReport;
    
    private void initForm(){
      btnShow.setOnAction((e)->{
         ReportRevenuePrintHandle();
      });
      
      initDataPopUpTypeReport();
      
       ancDaily.setVisible(false);
       ancMonthly.setVisible(false);
       ancYearly.setVisible(false);
             
      cbpTypeReport.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.getIdType()==1){
             ancDaily.setVisible(true);
             ancMonthly.setVisible(false);
             ancYearly.setVisible(false);
            }
           
            if(newVal.getIdType()==2){
             ancDaily.setVisible(false);
             ancMonthly.setVisible(true);
             ancYearly.setVisible(false);
             initDataPopUpMonthly();
            }
            
            if(newVal.getIdType()==3){
             ancDaily.setVisible(false);
             ancMonthly.setVisible(false);
             ancYearly.setVisible(true);
            }
      });
     /*  dpTanggal.setVisible(false);
       hbReportRevenueMonthly.setVisible(false);
       spnYearly.setVisible(false);
       
       rdReportRevenueDaily.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             dpTanggal.setVisible(true);
           }
           else{
             dpTanggal.setVisible(false);
           }
       });
       
       rdReportRevenueMonthly.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
              hbReportRevenueMonthly.setVisible(true);
              initDataPopUpMonthly();
           }
           else{
             hbReportRevenueMonthly.setVisible(false);
           }
       });
       
       rdReportRevenueYearly.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             spnYearly.setVisible(true);
            }
           else{
             spnYearly.setVisible(false);
           }
       }); */
       
       SwingNode swingNode = new SwingNode();
       JRViewer jrView = new JRViewer(null);
       swingNode.setContent(jrView);
       reportLayout(swingNode);
       
       spnMonthYearly.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(LocalDate.now().getYear(),100000000));
       spnMonthYearly.setEditable(true);
       
       spnYearly.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(LocalDate.now().getYear(),100000000));
       spnYearly.setEditable(true);
    }
    
     private void initDataPopUpTypeReport(){
      TableView<TypeReport>tblTypeReport = new TableView();
      TableColumn<TypeReport,String>typeReportName = new TableColumn("Jenis Laporan");
      typeReportName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
      ObservableList<TypeReport>listTypeReport = FXCollections.observableArrayList(loadAllDataTypeReport());
      cbpTypeReport = new JFXCComboBoxTablePopup(Month.class,tblTypeReport,listTypeReport,"","Jenis Laporan",false,500,300);
      tblTypeReport.getColumns().addAll(typeReportName);
      
      ancCbpTypeReport.getChildren().clear();
      AnchorPane.setTopAnchor(cbpTypeReport,0.0);
      AnchorPane.setBottomAnchor(cbpTypeReport,0.0);
      AnchorPane.setLeftAnchor(cbpTypeReport,0.0);
      AnchorPane.setRightAnchor(cbpTypeReport,0.0);
      ancCbpTypeReport.getChildren().add(cbpTypeReport);
    }
     
    private void initDataPopUpMonthly(){
      TableView<Month>tblMonth = new TableView();
      TableColumn<Month,String>monthName = new TableColumn("Bulan");
      monthName.setCellValueFactory(cellData -> cellData.getValue().bulanProperty());
      ObservableList<Month>listMonth = FXCollections.observableArrayList(loadAllDataMonth());
      cbpMonth = new JFXCComboBoxTablePopup(Month.class,tblMonth,listMonth,"","Bulan",false,500,300);
      tblMonth.getColumns().addAll(monthName);
      
      ancCbpMonthly.getChildren().clear();
      AnchorPane.setTopAnchor(cbpMonth,0.0);
      AnchorPane.setBottomAnchor(cbpMonth,0.0);
      AnchorPane.setLeftAnchor(cbpMonth,0.0);
      AnchorPane.setRightAnchor(cbpMonth,0.0);
      ancCbpMonthly.getChildren().add(cbpMonth);
    }
    
    private void reportLayout(SwingNode swingNode){
      ancReportView.getChildren().clear();
      AnchorPane.setTopAnchor(swingNode,15.0);
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      ancReportView.getChildren().add(swingNode);
    }
    
    private List<TypeReport>loadAllDataTypeReport(){
       List<TypeReport>listTypeReport = new ArrayList();
       listTypeReport.add(new TypeReport(1,"Laporan Harian"));
       listTypeReport.add(new TypeReport(2,"Laporan Bulanan"));
       listTypeReport.add(new TypeReport(3,"Laporan Tahunan"));
       return listTypeReport;
    }
     
    private List<Month>loadAllDataMonth(){
       List<Month>listMonth = new ArrayList();
       listMonth.add(new Month("Januari",1));
       listMonth.add(new Month("Februari",2));
       listMonth.add(new Month("Maret",3));
       listMonth.add(new Month("April",4));
       listMonth.add(new Month("Mei",5));
       listMonth.add(new Month("Juni",6));
       listMonth.add(new Month("Juli",7));
       listMonth.add(new Month("Agustus",8));
       listMonth.add(new Month("September",9));
       listMonth.add(new Month("Oktober",10));
       listMonth.add(new Month("November",11));
       listMonth.add(new Month("Desember",12));
       return listMonth;
    }
    
    private void ReportRevenuePrintHandle(){
       List<TblReservationAdditionalService>listAdditionalService  = new ArrayList();
       List<TblReservationAdditionalItem>listAdditionalItem = new ArrayList(); 
       List<TblReservationRoomTypeDetailRoomPriceDetail>listRoomPrice = new ArrayList();
       
       if(cbpTypeReport.getValue().getIdType() == 1){
         Date date = Date.valueOf(dpTanggal.getValue());
         Date dateBreakfast = Date.valueOf(dpTanggal.getValue().minusDays(1));
         listAdditionalService = parentController.getFReportManager().getAllDataAdditionalServiceBreakfastByDate(dateBreakfast);
         listAdditionalService.addAll(parentController.getFReportManager().getAllDataAdditionalServiceOtherByDate(date));
         
         listAdditionalItem  = parentController.getFReportManager().getAllDataAdditionalItemByDate(date);
        
         listRoomPrice = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByDate(date);
       
         SwingNode swingNode = printReportRevenueDaily(listAdditionalService,listAdditionalItem,listRoomPrice,date);
         
         reportLayout(swingNode);
       }
       
       if(cbpTypeReport.getValue().getIdType()==2){
           String monthYear = spnMonthYearly.getValue()+"-"+cbpMonth.getValue().getValueBulan();
           if(monthYear.length()<7){
             monthYear = spnMonthYearly.getValue()+"-0"+cbpMonth.getValue().getValueBulan();
           }
          else{
             monthYear = spnMonthYearly.getValue()+"-"+cbpMonth.getValue().getValueBulan(); 
            }
           
         listRoomPrice = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByMonth(monthYear);
         listAdditionalService = parentController.getFReportManager().getAllDataAdditionalServiceBreakfastByMonth(monthYear);
         listAdditionalService.addAll(parentController.getFReportManager().getAllDataAdditionalServiceOtherByMonth(monthYear));
         listAdditionalItem = parentController.getFReportManager().getAllDataAdditionalItemByMonth(monthYear);
         
         SwingNode swingNode = printReportRevenueMonthly(listAdditionalService,listAdditionalItem,listRoomPrice,cbpMonth.getValue(),spnMonthYearly.getValue());
         reportLayout(swingNode);
       }
       
       if(cbpTypeReport.getValue().getIdType()==3){
         listRoomPrice = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByYear(spnYearly.getValue());
         listAdditionalService = parentController.getFReportManager().getAllDataAdditionalServiceBreakfastByYear(spnYearly.getValue());
         listAdditionalService.addAll(parentController.getFReportManager().getAllDataAdditionalServiceOtherByYear(spnYearly.getValue()));
         listAdditionalItem = parentController.getFReportManager().getAllDataAdditionalItemByYear(spnYearly.getValue());
         
         SwingNode swingNode = printReportRevenueYearly(listAdditionalService,listAdditionalItem,listRoomPrice,spnYearly.getValue());
         reportLayout(swingNode);
       }
    /*   if(rdReportRevenueDaily.isSelected()){
         Date date = Date.valueOf(dpTanggal.getValue());
         Date dateBreakfast = Date.valueOf(dpTanggal.getValue().minusDays(1));
         listAdditionalService = parentController.getFReportManager().getAllDataAdditionalServiceBreakfastByDate(dateBreakfast);
         listAdditionalService.addAll(parentController.getFReportManager().getAllDataAdditionalServiceOtherByDate(date));
         
         listAdditionalItem  = parentController.getFReportManager().getAllDataAdditionalItemByDate(date);
        
         listRoomPrice = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByDate(date);
       
         SwingNode swingNode = printReportRevenueDaily(listAdditionalService,listAdditionalItem,listRoomPrice,date);
         
         reportLayout(swingNode);
       }
      
       if(rdReportRevenueMonthly.isSelected()){
         String monthYear = spnMonthYearly.getValue()+"-"+cbpMonth.getValue().getValueBulan();
           if(monthYear.length()<7){
             monthYear = spnMonthYearly.getValue()+"-0"+cbpMonth.getValue().getValueBulan();
           }
          else{
             monthYear = spnMonthYearly.getValue()+"-"+cbpMonth.getValue().getValueBulan(); 
            }
           
         listRoomPrice = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByMonth(monthYear);
         listAdditionalService = parentController.getFReportManager().getAllDataAdditionalServiceBreakfastByMonth(monthYear);
         listAdditionalService.addAll(parentController.getFReportManager().getAllDataAdditionalServiceOtherByMonth(monthYear));
         listAdditionalItem = parentController.getFReportManager().getAllDataAdditionalItemByMonth(monthYear);
         
         SwingNode swingNode = printReportRevenueMonthly(listAdditionalService,listAdditionalItem,listRoomPrice,cbpMonth.getValue(),spnMonthYearly.getValue());
         reportLayout(swingNode);
       }
       
       if(rdReportRevenueYearly.isSelected()){
         listRoomPrice = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByYear(spnYearly.getValue());
         listAdditionalService = parentController.getFReportManager().getAllDataAdditionalServiceBreakfastByYear(spnYearly.getValue());
         listAdditionalService.addAll(parentController.getFReportManager().getAllDataAdditionalServiceOtherByYear(spnYearly.getValue()));
         listAdditionalItem = parentController.getFReportManager().getAllDataAdditionalItemByYear(spnYearly.getValue());
         
         SwingNode swingNode = printReportRevenueYearly(listAdditionalService,listAdditionalItem,listRoomPrice,spnYearly.getValue());
         reportLayout(swingNode);
       } */
      // List<ClassPrintLaporanRevenueBulanan>listLaporanRevenueBulanan = printReportRevenueMonthly(listLaporanRevenueHarian,monthyear);
    }

//REPORT REVENUE DAILY
     private SwingNode printReportRevenueDaily(List<TblReservationAdditionalService>listAdditionalService,
                                             List<TblReservationAdditionalItem>listAdditionalItem,
                                             List<TblReservationRoomTypeDetailRoomPriceDetail>listRoomPrice,
                                             Date dpTanggal){
      
       List<ClassPrintLaporanRevenueHarian>listPendapatanHarian = new ArrayList();
       List<ClassPrintLaporanRevenueHarianDetail>listPendapatanHarianDetail = new ArrayList();
       
        BigDecimal diskon = new BigDecimal(0);
        BigDecimal totalHarga = new BigDecimal(0);
        BigDecimal totalHargaDiskon = new BigDecimal(0);
        BigDecimal totalHargaDiskonServiceCharge = new BigDecimal(0);
        String kodeReservasi = "";
        String deskripsi = "";
        String statusReservasi = "";
        BigDecimal hargaBarang = new BigDecimal(0);
        BigDecimal jumlah = new BigDecimal(0);
        BigDecimal serviceCharge  = new BigDecimal(0);
        BigDecimal pajak = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);
        BigDecimal compliment = new BigDecimal(0);
        String satuan = "";
        String tanggal = new SimpleDateFormat("dd MMMM yyyy").format(dpTanggal);
        
        ClassPrintLaporanRevenueHarian laporanRevenueHarian = new ClassPrintLaporanRevenueHarian();
        laporanRevenueHarian.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      //  laporanRevenueHarian.setTanggal(tanggal);
        for(TblReservationRoomTypeDetailRoomPriceDetail getRoomPrice : listRoomPrice){
          kodeReservasi = getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation();
          deskripsi = "Kamar - "+getRoomPrice.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName();
          statusReservasi = getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getTblPartner()==null?"Personal":"Travel Agent";
          satuan = "-";
          jumlah = new BigDecimal(1);
          hargaBarang = getRoomPrice.getTblReservationRoomPriceDetail().getDetailPrice();
          totalHarga = hargaBarang.multiply(jumlah);
          diskon = totalHarga.multiply(getRoomPrice.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal(100)));
          compliment = getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary(); 
          totalHargaDiskon = totalHarga.subtract(diskon).subtract(compliment);
          serviceCharge = getServiceCharge(getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totalHargaDiskon);
          
          totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceCharge);
          pajak = getTax(getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totalHargaDiskonServiceCharge);
          total = totalHargaDiskonServiceCharge.add(pajak);
          ClassPrintLaporanRevenueHarianDetail revenueHarianDetail = new ClassPrintLaporanRevenueHarianDetail(kodeReservasi,deskripsi,statusReservasi,hargaBarang,jumlah,satuan,totalHarga,diskon,compliment,pajak,serviceCharge,total);
          listPendapatanHarianDetail.add(revenueHarianDetail);
        }
        
  //insert data layanan     
       for(TblReservationAdditionalService getAdditionalService : listAdditionalService){
          kodeReservasi = getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation();
          deskripsi = "Layanan - "+getAdditionalService.getTblRoomService().getServiceName();
          statusReservasi = getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getTblPartner()==null?"Personal":"Travel Agent";
          satuan = "-";
          jumlah = new BigDecimal(1);
          hargaBarang = getAdditionalService.getPrice();
          totalHarga = hargaBarang.multiply(jumlah);
          diskon = totalHarga.multiply(getAdditionalService.getDiscountPercentage().divide(new BigDecimal(100)));
          totalHargaDiskon = totalHarga.subtract(diskon);
          serviceCharge = getServiceCharge(getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getAdditionalService.getRefReservationBillType().getIdtype(),totalHargaDiskon);
      //    compliment = getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary(); 
          totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceCharge);
          pajak = getTax(getAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totalHargaDiskonServiceCharge);
          total = totalHargaDiskonServiceCharge.add(pajak);
          ClassPrintLaporanRevenueHarianDetail revenueHarianDetail = new ClassPrintLaporanRevenueHarianDetail(kodeReservasi,deskripsi,statusReservasi,hargaBarang,jumlah,satuan,totalHarga,diskon,compliment,pajak,serviceCharge,total);
          listPendapatanHarianDetail.add(revenueHarianDetail);
       }
       
  //insert data barang     
       for(TblReservationAdditionalItem getAdditionalItem : listAdditionalItem){
        // ClassPrintLaporanPendapatanTambahanDetail additionalItem = new ClassPrintLaporanPendapatanTambahanDetail();
          kodeReservasi = getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation();
          deskripsi = "Barang - "+getAdditionalItem.getTblItem().getItemName();
          statusReservasi = getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getTblPartner()==null?"Personal":"Travel Agent";
          satuan = getAdditionalItem.getTblItem().getTblUnit().getUnitName();
          jumlah = getAdditionalItem.getItemQuantity();
          hargaBarang = getAdditionalItem.getItemCharge();
          totalHarga = hargaBarang.multiply(jumlah);
          diskon = totalHarga.multiply(getAdditionalItem.getDiscountPercentage().divide(new BigDecimal(100)));
          totalHargaDiskon = totalHarga.subtract(diskon);
          serviceCharge = getServiceCharge(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getAdditionalItem.getRefReservationBillType().getIdtype(),totalHargaDiskon);
      //    compliment = getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary(); 
          totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceCharge);
          pajak = getTax(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totalHargaDiskonServiceCharge);
          total = totalHargaDiskonServiceCharge.add(pajak);
          ClassPrintLaporanRevenueHarianDetail revenueHarianDetail = new ClassPrintLaporanRevenueHarianDetail(kodeReservasi,deskripsi,statusReservasi,hargaBarang,jumlah,satuan,totalHarga,diskon,compliment,pajak,serviceCharge,total);
          listPendapatanHarianDetail.add(revenueHarianDetail);
       }
       laporanRevenueHarian.setLaporanPendapatanDetail(listPendapatanHarianDetail);
       listPendapatanHarian.add(laporanRevenueHarian);
       
       return ClassPrinter.printReportRevenueDaily(listPendapatanHarian,tanggal);
     //  return listPendapatanHarian;
    }
    
//REPORT REVENUE YEARLY
    private SwingNode printReportRevenueYearly(List<TblReservationAdditionalService>listAdditionalService,
                                           List<TblReservationAdditionalItem>listAdditionalItem,
                                           List<TblReservationRoomTypeDetailRoomPriceDetail>listRoomPrice,
                                           int year){    
       List<ClassPrintLaporanRevenueTahunanDetail>listLaporanRevenueTahunanDetail = new ArrayList();
        List<ClassPrintLaporanRevenueTahunan>listLaporanRevenueTahunan = new ArrayList();
        ClassPrintLaporanRevenueTahunan laporanRevenueTahunan = new ClassPrintLaporanRevenueTahunan();
        laporanRevenueTahunan.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
        laporanRevenueTahunan.setTahun(""+year);
      //  laporanRevenueHarian.setTanggal(tanggal);
        LocalDate dateStart = LocalDate.of(year,1,1);
        LocalDate dateEnd = LocalDate.of(year+1,1,1);
       
        for(Date date = Date.valueOf(dateStart); date.before(Date.valueOf(dateEnd)); date = Date.valueOf(date.toLocalDate().plusMonths(1))){
         BigDecimal diskon = new BigDecimal(0);
         BigDecimal totalHarga = new BigDecimal(0);
         BigDecimal compliment = new BigDecimal(0);
         BigDecimal serviceCharge = new BigDecimal(0);
         BigDecimal pajak = new BigDecimal(0);
         BigDecimal totalRevenue = new BigDecimal(0);
         
         ClassPrintLaporanRevenueTahunanDetail laporanRevenueTahunanDetail = new ClassPrintLaporanRevenueTahunanDetail();
         laporanRevenueTahunanDetail.setBulan(new SimpleDateFormat("MMMM yyyy",new Locale("id")).format(date));
           boolean check = false;
           for(TblReservationRoomTypeDetailRoomPriceDetail getRoomPrice : listRoomPrice){
               String roomPriceMonth = new SimpleDateFormat("MMMM yyyy",new Locale("id")).format(getRoomPrice.getTblReservationRoomPriceDetail().getDetailDate());
               if(roomPriceMonth.equalsIgnoreCase(laporanRevenueTahunanDetail.getBulan())){
                   BigDecimal jumlahKamar = new BigDecimal(1);
                   BigDecimal hargaKamar = getRoomPrice.getTblReservationRoomPriceDetail().getDetailPrice();
                   BigDecimal totalHargaKamar = hargaKamar.multiply(jumlahKamar);
                   BigDecimal complimentKamar = getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary();
                   BigDecimal diskonKamar = totalHargaKamar.multiply(getRoomPrice.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal(100)));
                   BigDecimal totHargaDiskonCompliment = (totalHargaKamar.subtract(diskonKamar)).subtract(complimentKamar);
                   BigDecimal serviceChargeKamar = getServiceCharge(getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totHargaDiskonCompliment);
                   BigDecimal totalHargaDiskonServiceCharge = totHargaDiskonCompliment.add(serviceChargeKamar);
                   BigDecimal pajakKamar = getTax(getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totalHargaDiskonServiceCharge);
                   BigDecimal total = totalHargaDiskonServiceCharge.add(pajakKamar);
         
                   diskon = diskon.add(diskonKamar);
                   totalHarga = totalHarga.add(totalHargaKamar);
                   serviceCharge = serviceCharge.add(serviceChargeKamar);
                   pajak = pajak.add(pajakKamar);
                   totalRevenue = totalRevenue.add(total);
                   compliment = compliment.add(complimentKamar);
                   check = true; 
                }
            }
           
           for(TblReservationAdditionalService getService : listAdditionalService){
               String serviceMonth = new SimpleDateFormat("MMMM yyyy",new Locale("id")).format(getService.getAdditionalDate());
               String serviceBreakfastMonth = new SimpleDateFormat("MMMM yyyy",new Locale("id")).format(Date.valueOf(((java.sql.Date)getService.getAdditionalDate()).toLocalDate().plusDays(1)));
               if(getService.getTblRoomService().getIdroomService()==1){
                  if(serviceBreakfastMonth.equalsIgnoreCase(laporanRevenueTahunanDetail.getBulan())){
                      BigDecimal jumlahLayanan = new BigDecimal(1);
                      BigDecimal hargaLayanan = getService.getPrice();
                      BigDecimal totalHargaLayanan = hargaLayanan.multiply(jumlahLayanan);
                      BigDecimal diskonLayanan = totalHargaLayanan.multiply(getService.getDiscountPercentage().divide(new BigDecimal(100)));
                      BigDecimal totalHargaDiskon = totalHargaLayanan.subtract(diskonLayanan);
                      BigDecimal serviceChargeLayanan = getServiceCharge(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskon);
                      BigDecimal totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceChargeLayanan);
                      BigDecimal pajakLayanan = getTax(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskonServiceCharge);
                      BigDecimal total = totalHargaDiskonServiceCharge.add(pajakLayanan);

                      diskon = diskon.add(diskonLayanan);
                      totalHarga = totalHarga.add(totalHargaLayanan);
                      serviceCharge = serviceCharge.add(serviceChargeLayanan);
                      pajak = pajak.add(pajakLayanan);
                      totalRevenue = totalRevenue.add(total);
                      compliment = compliment.add(new BigDecimal(0));
                      check = true; 
                   }
                }
               else{
                   if(serviceMonth.equalsIgnoreCase(laporanRevenueTahunanDetail.getBulan())){
                     BigDecimal jumlahLayanan = new BigDecimal(1);
                     BigDecimal hargaLayanan = getService.getPrice();
                     BigDecimal totalHargaLayanan = hargaLayanan.multiply(jumlahLayanan);
                     BigDecimal diskonLayanan = totalHargaLayanan.multiply(getService.getDiscountPercentage().divide(new BigDecimal(100)));
                     BigDecimal totalHargaDiskon = totalHargaLayanan.subtract(diskonLayanan);
                     BigDecimal serviceChargeLayanan = getServiceCharge(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskon);
                     BigDecimal totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceChargeLayanan);
                     BigDecimal pajakLayanan = getTax(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskonServiceCharge);
                     BigDecimal total = totalHargaDiskonServiceCharge.add(pajakLayanan);
         
                     diskon = diskon.add(diskonLayanan);
                     totalHarga = totalHarga.add(totalHargaLayanan);
                     serviceCharge = serviceCharge.add(serviceChargeLayanan);
                     pajak = pajak.add(pajakLayanan);
                     totalRevenue = totalRevenue.add(total);
                     compliment = compliment.add(new BigDecimal(0));
                     check = true; 
                   }
               }
            }
              
           
           for(TblReservationAdditionalItem getAdditionalItem : listAdditionalItem){
              String additionalItemMonth = new SimpleDateFormat("MMMM yyyy",new Locale("id")).format(getAdditionalItem.getAdditionalDate());
              if(additionalItemMonth.equalsIgnoreCase(laporanRevenueTahunanDetail.getBulan())){
                  BigDecimal jumlah = getAdditionalItem.getItemQuantity();
                  BigDecimal hargaBarang = getAdditionalItem.getItemCharge();
                  BigDecimal totalHargaBarang = hargaBarang.multiply(jumlah);
                  BigDecimal diskonBarang = totalHargaBarang.multiply(getAdditionalItem.getDiscountPercentage().divide(new BigDecimal(100)));
                  BigDecimal totalHargaDiskon = totalHargaBarang.subtract(diskonBarang);
                  BigDecimal serviceChargeBarang = getServiceCharge(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getAdditionalItem.getRefReservationBillType().getIdtype(),totalHargaDiskon);
      //    compliment = getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary(); 
                  BigDecimal totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceChargeBarang);
                  BigDecimal pajakBarang = getTax(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getAdditionalItem.getRefReservationBillType().getIdtype(),totalHargaDiskonServiceCharge);
                  BigDecimal total = totalHargaDiskonServiceCharge.add(pajakBarang);
                  
                  diskon = diskon.add(diskonBarang);
                  totalHarga = totalHarga.add(totalHargaBarang);
                  serviceCharge = serviceCharge.add(serviceChargeBarang);
                  pajak = pajak.add(pajakBarang);
                  totalRevenue = totalRevenue.add(total);
                  compliment = compliment.add(new BigDecimal(0));
                  check = true; 
                }
            }
           
           if(check){
             laporanRevenueTahunanDetail.setDiskon(diskon);
             laporanRevenueTahunanDetail.setCompliment(compliment);
             laporanRevenueTahunanDetail.setPajak(pajak);
             laporanRevenueTahunanDetail.setServiceCharge(serviceCharge);
             laporanRevenueTahunanDetail.setTotal(totalRevenue);
             laporanRevenueTahunanDetail.setTotalHarga(totalHarga);
             listLaporanRevenueTahunanDetail.add(laporanRevenueTahunanDetail);
           }
           else{
             laporanRevenueTahunanDetail.setDiskon(new BigDecimal(0));
             laporanRevenueTahunanDetail.setCompliment(new BigDecimal(0));
             laporanRevenueTahunanDetail.setPajak(new BigDecimal(0));
             laporanRevenueTahunanDetail.setServiceCharge(new BigDecimal(0));
             laporanRevenueTahunanDetail.setTotal(new BigDecimal(0));
             laporanRevenueTahunanDetail.setTotalHarga(new BigDecimal(0));
             listLaporanRevenueTahunanDetail.add(laporanRevenueTahunanDetail);  
           }
        }
        laporanRevenueTahunan.setLaporanRevenueTahunanDetail(listLaporanRevenueTahunanDetail);
        listLaporanRevenueTahunan.add(laporanRevenueTahunan);
        return ClassPrinter.printReportRevenueYearly(listLaporanRevenueTahunan,""+year);
    }
    
    //REPORT REVENUE MONTHLY
    private SwingNode printReportRevenueMonthly(List<TblReservationAdditionalService>listAdditionalService,
                                           List<TblReservationAdditionalItem>listAdditionalItem,
                                           List<TblReservationRoomTypeDetailRoomPriceDetail>listRoomPrice,
                                           Month month,int year){     
       List<ClassPrintLaporanRevenueBulanan>listPendapatanBulanan = new ArrayList();
       List<ClassPrintLaporanRevenueBulananDetail>listPendapatanBulananDetail = new ArrayList();
      
        
        List<ClassPrintLaporanRevenueBulanan>listLaporanRevenueBulanan = new ArrayList();
        ClassPrintLaporanRevenueBulanan laporanRevenueBulanan = new ClassPrintLaporanRevenueBulanan();
        laporanRevenueBulanan.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
        laporanRevenueBulanan.setMonthly(month.getBulan()+" "+year);
      //  laporanRevenueHarian.setTanggal(tanggal);
        LocalDate dateStart = LocalDate.of(year,month.getValueBulan(),1);
        LocalDate dateEnd = LocalDate.of(year,month.getValueBulan()+1,1);
        
        List<ClassPrintLaporanRevenueBulananDetail>listLaporanRevenueBulananDetail = new ArrayList();
        for(Date date = Date.valueOf(dateStart); date.before(Date.valueOf(dateEnd)); date = Date.valueOf(date.toLocalDate().plusDays(1))){
         BigDecimal diskon = new BigDecimal(0);
         BigDecimal totalHarga = new BigDecimal(0);
         BigDecimal compliment = new BigDecimal(0);
         BigDecimal serviceCharge = new BigDecimal(0);
         BigDecimal pajak = new BigDecimal(0);
         BigDecimal totalRevenue = new BigDecimal(0);
         ClassPrintLaporanRevenueBulananDetail laporanRevenueBulananDetail = new ClassPrintLaporanRevenueBulananDetail();
           laporanRevenueBulananDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(date));
           boolean check = false;
           for(TblReservationRoomTypeDetailRoomPriceDetail getRoomPrice : listRoomPrice){
               String roomPriceDate = new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getRoomPrice.getTblReservationRoomPriceDetail().getDetailDate());
               if(roomPriceDate.equalsIgnoreCase(laporanRevenueBulananDetail.getTanggal())){
                   BigDecimal jumlahKamar = new BigDecimal(1);
                   BigDecimal hargaKamar = getRoomPrice.getTblReservationRoomPriceDetail().getDetailPrice();
                   BigDecimal totalHargaKamar = hargaKamar.multiply(jumlahKamar);
                   BigDecimal complimentKamar = getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary();
                   BigDecimal diskonKamar = totalHargaKamar.multiply(getRoomPrice.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal(100)));
                   BigDecimal totHargaDiskonCompliment = (totalHargaKamar.subtract(diskonKamar)).subtract(complimentKamar);
                   BigDecimal serviceChargeKamar = getServiceCharge(getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totHargaDiskonCompliment);
                   BigDecimal totalHargaDiskonServiceCharge = totHargaDiskonCompliment.add(serviceChargeKamar);
                   BigDecimal pajakKamar = getTax(getRoomPrice.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),0,totalHargaDiskonServiceCharge);
                   BigDecimal total = totalHargaDiskonServiceCharge.add(pajakKamar);
         
                   diskon = diskon.add(diskonKamar);
                   totalHarga = totalHarga.add(totalHargaKamar);
                   serviceCharge = serviceCharge.add(serviceChargeKamar);
                   pajak = pajak.add(pajakKamar);
                   totalRevenue = totalRevenue.add(total);
                   compliment = compliment.add(complimentKamar);
                   check = true; 
                }
            }
           
           for(TblReservationAdditionalService getService : listAdditionalService){
               String serviceDate = new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getService.getAdditionalDate());
               String serviceBreakfast = new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(Date.valueOf(((java.sql.Date)getService.getAdditionalDate()).toLocalDate().plusDays(1)));
               if(getService.getTblRoomService().getIdroomService()==1){
                  if(serviceBreakfast.equalsIgnoreCase(laporanRevenueBulananDetail.getTanggal())){
                      BigDecimal jumlahLayanan = new BigDecimal(1);
                      BigDecimal hargaLayanan = getService.getPrice();
                      BigDecimal totalHargaLayanan = hargaLayanan.multiply(jumlahLayanan);
                      BigDecimal diskonLayanan = totalHargaLayanan.multiply(getService.getDiscountPercentage().divide(new BigDecimal(100)));
                      BigDecimal totalHargaDiskon = totalHargaLayanan.subtract(diskonLayanan);
                      BigDecimal serviceChargeLayanan = getServiceCharge(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskon);
                      BigDecimal totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceChargeLayanan);
                      BigDecimal pajakLayanan = getTax(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskonServiceCharge);
                      BigDecimal total = totalHargaDiskonServiceCharge.add(pajakLayanan);

                      diskon = diskon.add(diskonLayanan);
                      totalHarga = totalHarga.add(totalHargaLayanan);
                      serviceCharge = serviceCharge.add(serviceChargeLayanan);
                      pajak = pajak.add(pajakLayanan);
                      totalRevenue = totalRevenue.add(total);
                      compliment = compliment.add(new BigDecimal(0));
                      check = true; 
                   }
                }
               else{
                   if(serviceDate.equalsIgnoreCase(laporanRevenueBulananDetail.getTanggal())){
                     BigDecimal jumlahLayanan = new BigDecimal(1);
                     BigDecimal hargaLayanan = getService.getPrice();
                     BigDecimal totalHargaLayanan = hargaLayanan.multiply(jumlahLayanan);
                     BigDecimal diskonLayanan = totalHargaLayanan.multiply(getService.getDiscountPercentage().divide(new BigDecimal(100)));
                     BigDecimal totalHargaDiskon = totalHargaLayanan.subtract(diskonLayanan);
                     BigDecimal serviceChargeLayanan = getServiceCharge(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskon);
                     BigDecimal totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceChargeLayanan);
                     BigDecimal pajakLayanan = getTax(getService.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getService.getRefReservationBillType().getIdtype(),totalHargaDiskonServiceCharge);
                     BigDecimal total = totalHargaDiskonServiceCharge.add(pajakLayanan);
         
                     diskon = diskon.add(diskonLayanan);
                     totalHarga = totalHarga.add(totalHargaLayanan);
                     serviceCharge = serviceCharge.add(serviceChargeLayanan);
                     pajak = pajak.add(pajakLayanan);
                     totalRevenue = totalRevenue.add(total);
                     compliment = compliment.add(new BigDecimal(0));
                     check = true; 
                   }
               }
            }
              
           
           for(TblReservationAdditionalItem getAdditionalItem : listAdditionalItem){
              String additionalItemDate = new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getAdditionalItem.getAdditionalDate());
              if(additionalItemDate.equalsIgnoreCase(laporanRevenueBulananDetail.getTanggal())){
                  BigDecimal jumlah = getAdditionalItem.getItemQuantity();
                  BigDecimal hargaBarang = getAdditionalItem.getItemCharge();
                  BigDecimal totalHargaBarang = hargaBarang.multiply(jumlah);
                  BigDecimal diskonBarang = totalHargaBarang.multiply(getAdditionalItem.getDiscountPercentage().divide(new BigDecimal(100)));
                  BigDecimal totalHargaDiskon = totalHargaBarang.subtract(diskonBarang);
                  BigDecimal serviceChargeBarang = getServiceCharge(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getAdditionalItem.getRefReservationBillType().getIdtype(),totalHargaDiskon);
      //    compliment = getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary(); 
                  BigDecimal totalHargaDiskonServiceCharge = totalHargaDiskon.add(serviceChargeBarang);
                  BigDecimal pajakBarang = getTax(getAdditionalItem.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),getAdditionalItem.getRefReservationBillType().getIdtype(),totalHargaDiskonServiceCharge);
                  BigDecimal total = totalHargaDiskonServiceCharge.add(pajakBarang);
                  
                  diskon = diskon.add(diskonBarang);
                  totalHarga = totalHarga.add(totalHargaBarang);
                  serviceCharge = serviceCharge.add(serviceChargeBarang);
                  pajak = pajak.add(pajakBarang);
                  totalRevenue = totalRevenue.add(total);
                  compliment = compliment.add(new BigDecimal(0));
                  check = true; 
                }
            }
           
           if(check){
             laporanRevenueBulananDetail.setDiskon(diskon);
             laporanRevenueBulananDetail.setCompliment(compliment);
             laporanRevenueBulananDetail.setPajak(pajak);
             laporanRevenueBulananDetail.setServiceCharge(serviceCharge);
             laporanRevenueBulananDetail.setTotal(totalRevenue);
             laporanRevenueBulananDetail.setTotalHarga(totalHarga);
             listLaporanRevenueBulananDetail.add(laporanRevenueBulananDetail);
           }
           else{
             laporanRevenueBulananDetail.setDiskon(new BigDecimal(0));
             laporanRevenueBulananDetail.setCompliment(new BigDecimal(0));
             laporanRevenueBulananDetail.setPajak(new BigDecimal(0));
             laporanRevenueBulananDetail.setServiceCharge(new BigDecimal(0));
             laporanRevenueBulananDetail.setTotal(new BigDecimal(0));
             laporanRevenueBulananDetail.setTotalHarga(new BigDecimal(0));
             listLaporanRevenueBulananDetail.add(laporanRevenueBulananDetail);  
           }
        }
        laporanRevenueBulanan.setListLaporanRevenueBulananDetail(listLaporanRevenueBulananDetail);
        listLaporanRevenueBulanan.add(laporanRevenueBulanan);
        return ClassPrinter.printReportRevenueMonthly(listLaporanRevenueBulanan,month+" "+year);
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
    
   private BigDecimal totalHargaRevenueHarian(List<ClassPrintLaporanRevenueHarianDetail>list){
      BigDecimal totalHarga = new BigDecimal(0);
      for(ClassPrintLaporanRevenueHarianDetail getLaporanRevenueHarianDetail : list){
          totalHarga = totalHarga.add(getLaporanRevenueHarianDetail.getTotalHarga());
        } 
      return totalHarga;
    }
    
  /*  private BigDecimal totalDiscount(List<ClassPrintLaporanPendapatanTambahan>list){
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
    } */
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      initForm();
    }
    
    public ReportRevenueController(FeatureReportFrontOfficeController parentController){
       this.parentController = parentController; 
    }
    
    private final FeatureReportFrontOfficeController parentController;
}

  class Month{
        private final StringProperty bulan;
        private final IntegerProperty valueBulan;
        
        public Month() {
          this.bulan = new SimpleStringProperty();
          this.valueBulan = new SimpleIntegerProperty();
         }

        public Month(String bulan,int valueBulan) {
          this();
          bulanProperty().set(bulan);
          valueBulanProperty().set(valueBulan);
        }

        public final StringProperty bulanProperty() {
             return this.bulan;
        }

        public String getBulan() {
             return bulanProperty().get();
         }

        public void setBulan(String bulan) {
             bulanProperty().set(bulan);
        }
        
        public final IntegerProperty valueBulanProperty() {
             return this.valueBulan;
        }

        public int getValueBulan() {
             return valueBulanProperty().get();
         }

        public void setValueBulan(int valueBulan) {
             valueBulanProperty().set(valueBulan);
        }
        
        @Override
        public String toString(){
           return getBulan(); 
        }
   }

   class TypeReport{
        private final IntegerProperty idType;
        private final StringProperty typeName;

        public TypeReport() {
          this.idType = new SimpleIntegerProperty();
          this.typeName = new SimpleStringProperty();
        }

       public TypeReport(int idType) {
         this();
       }

       public TypeReport(int idType, String typeName) {
         this();
         idTypeProperty().set(idType);
         typeNameProperty().set(typeName);
       }

       public final IntegerProperty idTypeProperty() {
        return this.idType;
       }

       public int getIdType() {
         return idTypeProperty().get();
       }

       public void setIdType(int idType) {
               idTypeProperty().set(idType);
       }

       public final StringProperty typeNameProperty() {
               return this.typeName;
       }

       public String getTypeName() {
               return typeNameProperty().get();
       }

       public void setTypeName(String typeName) {
               typeNameProperty().set(typeName);
       }

        @Override
        public String toString(){
           return getTypeName(); 
        }

   }