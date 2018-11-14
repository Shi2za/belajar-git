/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office.report_room_income;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassReservationType;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanPendapatanKamar;
import hotelfx.helper.PrintModel.ClassPrintLaporanPendapatanKamarDetail;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.view.feature_report_front_office.FeatureReportFrontOfficeController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toCollection;
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
public class ReportRoomIncomeController implements Initializable{
   
   @FXML
   private JFXCheckBox chbTravelAgent;
   @FXML
   private JFXCheckBox chbRoomType;
   @FXML
   private JFXCheckBox chbReservationType;
   @FXML
   private AnchorPane ancRoomType;
   JFXCComboBoxTablePopup<TblRoomType>cbpRoomType;
   @FXML
   private AnchorPane ancTravelAgent;
   JFXCComboBoxTablePopup<TblTravelAgent>cbpTravelAgent;
   @FXML
   private AnchorPane ancReservationType;
   JFXCComboBoxTablePopup<RefReservationOrderByType>cbpReservationType;
   @FXML
   private JFXDatePicker dpStartPeriode;
   @FXML
   private JFXDatePicker dpEndPeriode;
   @FXML
   private AnchorPane ancReportView;
   @FXML
   private JFXButton btnShow;
 
   ToggleGroup tglReport;
    
   private void initForm(){
       btnShow.setOnAction((e)->{
         reportRoomIncomePrintHandle();
       });
       
    /*   tglReport = new ToggleGroup();
       rdTravelAgent.setToggleGroup(tglReport);
       rdRoomType.setToggleGroup(tglReport); */
       
       chbTravelAgent.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             initDataPopUpTravelAgent();  
           }
           else{
             cbpTravelAgent.setVisible(false);
           }
       });
       
       chbRoomType.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
              initDataPopUpRoomType(); 
           }
           else{
             cbpRoomType.setVisible(false);
           }
       });
       
       chbReservationType.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
              initDataPopUpReservationType(); 
           }
           else{
              cbpReservationType.setVisible(false);
           }
       });
               
       SwingNode swingNode = new SwingNode();
       JRViewer jrView = new JRViewer(null);
       swingNode.setContent(jrView);
       swingNodeLayout(swingNode);
       
       ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartPeriode,dpEndPeriode);
   }
   
   private void initDataPopUpTravelAgent(){
      TableView<TblTravelAgent>tblTravelAgent = new TableView();
      TableColumn<TblTravelAgent,String>travelAgentName = new TableColumn("Travel Agent");
      travelAgentName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblPartner().getPartnerName(),param.getValue().tblPartnerProperty()));
      tblTravelAgent.getColumns().addAll(travelAgentName);
      
      ObservableList<TblTravelAgent>list = FXCollections.observableArrayList(loadAllDataTravelAgent());
      
      cbpTravelAgent = new JFXCComboBoxTablePopup(TblTravelAgent.class,tblTravelAgent,list,"","Travel Agent",false,500,300);
      cbpTravelAgent.setLabelFloat(false);
      ancTravelAgent.getChildren().clear();
      AnchorPane.setTopAnchor(cbpTravelAgent,0.0);
      AnchorPane.setBottomAnchor(cbpTravelAgent,0.0);
      AnchorPane.setLeftAnchor(cbpTravelAgent,0.0);
      AnchorPane.setRightAnchor(cbpTravelAgent,0.0);
      ancTravelAgent.getChildren().add(cbpTravelAgent);
   }
   
   private void initDataPopUpRoomType(){
      TableView<TblRoomType>tblRoomType = new TableView();
      TableColumn<TblRoomType,String>roomTypeName = new TableColumn("Tipe Kamar");
      roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomType,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getRoomTypeName(),param.getValue().roomTypeNameProperty()));
      tblRoomType.getColumns().addAll(roomTypeName);
      
       ObservableList<TblRoomType>list = FXCollections.observableArrayList(loadAllDataRoomType());
       
      cbpRoomType = new JFXCComboBoxTablePopup(TblRoomType.class,tblRoomType,list,"","Tipe Kamar",false,500,300);
      cbpRoomType.setLabelFloat(false);
      ancRoomType.getChildren().clear();
      AnchorPane.setTopAnchor(cbpRoomType,0.0);
      AnchorPane.setBottomAnchor(cbpRoomType,0.0);
      AnchorPane.setLeftAnchor(cbpRoomType,0.0);
      AnchorPane.setRightAnchor(cbpRoomType,0.0);
      ancRoomType.getChildren().add(cbpRoomType);
   }
   
   private void initDataPopUpReservationType(){
      TableView<RefReservationOrderByType>tblReservationType = new TableView();
      TableColumn<RefReservationOrderByType,String>reservationTypeName = new TableColumn("Tipe Reservasi");
      reservationTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
      tblReservationType.getColumns().addAll(reservationTypeName);
      
      ObservableList<RefReservationOrderByType>listReservationType = FXCollections.observableArrayList(loadAllDataReservationType());
      
      cbpReservationType = new JFXCComboBoxTablePopup(RefReservationOrderByType.class,tblReservationType,listReservationType,"","Tipe Reservasi",false,400,300);
      cbpReservationType.setLabelFloat(false);
      ancReservationType.getChildren().clear();
      AnchorPane.setTopAnchor(cbpReservationType,0.0);
      AnchorPane.setBottomAnchor(cbpReservationType,0.0);
      AnchorPane.setLeftAnchor(cbpReservationType,0.0);
      AnchorPane.setRightAnchor(cbpReservationType,0.0);
      ancReservationType.getChildren().add(cbpReservationType);
   }
   
   private List<RefReservationOrderByType>loadAllDataReservationType(){
     return parentController.getFReportManager().getAllDataReservationType();
   }
   
   private List<TblTravelAgent>loadAllDataTravelAgent(){
      List<TblTravelAgent>list = new ArrayList();
    /*  TblTravelAgent travelAgent = new TblTravelAgent();
      TblPartner partner = new TblPartner();
      partner.setPartnerName("Semua Travel Agent");
      travelAgent.setIdtravelAgent(0);
      travelAgent.setTblPartner(partner);
      list.add(travelAgent); */
      list.addAll(parentController.getFReportManager().getAllDataTravelAgent());
      return list;
   }
   
   private List<TblRoomType>loadAllDataRoomType(){
      List<TblRoomType>list = new ArrayList();
    /*  TblRoomType roomType = new TblRoomType();
      roomType.setIdroomType(0);
      roomType.setRoomTypeName("Semua Tipe Kamar");
      list.add(roomType); */
      list.addAll(parentController.getFReportManager().getAllDataRoomType());
      return list;
   }
   
   private SwingNode printReportRoomIncome(List<TblReservationRoomTypeDetailRoomPriceDetail>listReservation){
      List<ClassPrintLaporanPendapatanKamar>listLaporanPendapatan = new ArrayList();
     // List<ClassPrintLaporanPendapatanKamarDate>listLaporanPendapatanDate = new ArrayList();
       
      ClassPrintLaporanPendapatanKamar pendapatanKamar = null;
      
      SwingNode swingNode = new SwingNode();
      
       for(TblReservationRoomTypeDetailRoomPriceDetail getDataReservation : listReservation){
          
           String kodeReservasi = getDataReservation.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation();
           String tipeKamar = getDataReservation.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName();
           String travelAgent = getDataReservation.getTblReservationRoomTypeDetail().getTblReservation().getTblPartner()==null ? "-" 
                                                     : getDataReservation.getTblReservationRoomTypeDetail().getTblReservation().getTblPartner().getPartnerName();
            BigDecimal hargaKamar = getDataReservation.getTblReservationRoomPriceDetail().getDetailPrice();
            String namaCustomer = getDataReservation.getTblReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getFullName();
            BigDecimal diskon = totalDiscount(hargaKamar,getDataReservation.getTblReservationRoomPriceDetail().getDetailDiscountPercentage());
            System.out.println("hsl diskon :"+diskon);
            BigDecimal compliment = getDataReservation.getTblReservationRoomPriceDetail().getDetailComplimentary();
            BigDecimal serviceCharge = totalServiceCharge(getDataReservation.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),
                                                       (hargaKamar.subtract(diskon.subtract(compliment))));
            BigDecimal pajak = totalTax(getDataReservation.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),
                                                ((hargaKamar.subtract(diskon).subtract(compliment).add(serviceCharge))));
            
           if(pendapatanKamar==null){
             pendapatanKamar = new ClassPrintLaporanPendapatanKamar();
             pendapatanKamar.setTanggalReservasi(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(getDataReservation.getTblReservationRoomPriceDetail().getDetailDate()));
             List<ClassPrintLaporanPendapatanKamarDetail>list = new ArrayList();
             ClassPrintLaporanPendapatanKamarDetail laporanPendapatanKamarDetail = 
             new ClassPrintLaporanPendapatanKamarDetail(kodeReservasi,tipeKamar,namaCustomer,travelAgent,hargaKamar,diskon,compliment,serviceCharge,pajak);
             list.add(laporanPendapatanKamarDetail);
             pendapatanKamar.setLaporanPendapatanDetail(list);
             pendapatanKamar.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
             listLaporanPendapatan.add(pendapatanKamar);
            }
           else{
               if(pendapatanKamar.getTanggalReservasi().equalsIgnoreCase(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(getDataReservation.getTblReservationRoomPriceDetail().getDetailDate()))){
                 ClassPrintLaporanPendapatanKamarDetail laporanPendapatanKamarDetail = 
                 new ClassPrintLaporanPendapatanKamarDetail(kodeReservasi,tipeKamar,namaCustomer,travelAgent,hargaKamar,diskon,compliment,serviceCharge,pajak);
                 pendapatanKamar.getLaporanPendapatanDetail().add(laporanPendapatanKamarDetail);
               }
               else{
                  pendapatanKamar = new ClassPrintLaporanPendapatanKamar();
                  pendapatanKamar.setTanggalReservasi(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(getDataReservation.getTblReservationRoomPriceDetail().getDetailDate()));
                  pendapatanKamar.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
                  List<ClassPrintLaporanPendapatanKamarDetail>list = new ArrayList();
                  ClassPrintLaporanPendapatanKamarDetail laporanPendapatanKamarDetail = 
                  new ClassPrintLaporanPendapatanKamarDetail(kodeReservasi,tipeKamar,namaCustomer,travelAgent,hargaKamar,diskon,compliment,serviceCharge,pajak);
                  list.add(laporanPendapatanKamarDetail);
                  pendapatanKamar.setLaporanPendapatanDetail(list);
                  listLaporanPendapatan.add(pendapatanKamar);
               }
           }
         //  listLaporanPendapatan.add(pendapatanKamar);
       }
          
       BigDecimal totalHargaKamar = totalHargaKamar(listLaporanPendapatan);
       BigDecimal totalDiskon = totalDiskon(listLaporanPendapatan);
       BigDecimal totalCompliment = totalCompliment(listLaporanPendapatan);
       BigDecimal totalServiceCharge = totalServiceChargeAll(listLaporanPendapatan);
       BigDecimal totalPajak = totalPajak(listLaporanPendapatan);
       
       String periode = "";
       if(dpStartPeriode.getValue()!=null && dpEndPeriode!=null){
          periode = new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpStartPeriode.getValue()))+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(Date.valueOf(dpEndPeriode.getValue()));
       }
       else{
         periode = "-";
       }
       
       System.out.println("list :"+listLaporanPendapatan.size());
     //  pendapatanKamar.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
     //  pendapatanKamar.setLaporanPendapatanDate(listLaporanPendapatanDate);
       
       
      swingNode =  ClassPrinter.printReportRoomIncome(listLaporanPendapatan, periode,totalHargaKamar,totalDiskon,totalCompliment,totalServiceCharge,totalPajak);
      return swingNode;
   }
   
   private BigDecimal totalDiscount(BigDecimal price,BigDecimal discount){
      System.out.println("harga :"+price +", Diskon" + discount);
      BigDecimal totalDiscount = new BigDecimal(0);
      totalDiscount =  price.multiply(discount.divide(new BigDecimal(100)));
       System.out.println("total diskon :"+totalDiscount);
      return totalDiscount;
   }
   
   private BigDecimal totalServiceCharge(long id,BigDecimal price){
     List<TblReservationBill>listBillReservation = parentController.getFReportManager().getAllDataReservationBill(id,(int)0);
     
     BigDecimal totalServiceCharge = new BigDecimal(0);
     for(TblReservationBill getReservationBill : listBillReservation){
        totalServiceCharge = price.multiply(getReservationBill.getServiceChargePercentage());
     }
     
     return totalServiceCharge;
   }
   
   private BigDecimal totalTax(long id,BigDecimal price){
     List<TblReservationBill>listBillReservation = parentController.getFReportManager().getAllDataReservationBill(id,(int)0);
     
     BigDecimal totalTax = new BigDecimal(0);
     
     for(TblReservationBill getReservationBill : listBillReservation){
       totalTax = price.multiply(getReservationBill.getTaxPercentage());
     }
     
     return totalTax;
   }
   
   private BigDecimal totalHargaKamar(List<ClassPrintLaporanPendapatanKamar>listPendapatanKamar){
       BigDecimal totalHargaKamar = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanKamar getPendapatanKamar : listPendapatanKamar){
           for(ClassPrintLaporanPendapatanKamarDetail getPendapatanKamarDetail : getPendapatanKamar.getLaporanPendapatanDetail()){
             totalHargaKamar = totalHargaKamar.add(getPendapatanKamarDetail.getHargaKamar());
            }
        }
      return totalHargaKamar;
   }
   
   private BigDecimal totalDiskon(List<ClassPrintLaporanPendapatanKamar>listPendapatanKamar){
       BigDecimal totalDiskon = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanKamar getPendapatanKamar : listPendapatanKamar){
           for(ClassPrintLaporanPendapatanKamarDetail getPendapatanKamarDetail : getPendapatanKamar.getLaporanPendapatanDetail()){
             totalDiskon = totalDiskon.add(getPendapatanKamarDetail.getDiskon());
            }
        }
      return totalDiskon;
   }
   
   private BigDecimal totalServiceChargeAll(List<ClassPrintLaporanPendapatanKamar>listPendapatanKamar){
       BigDecimal totalServiceCharge = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanKamar getPendapatanKamar : listPendapatanKamar){
           for(ClassPrintLaporanPendapatanKamarDetail getPendapatanKamarDetail : getPendapatanKamar.getLaporanPendapatanDetail()){
             totalServiceCharge = totalServiceCharge.add(getPendapatanKamarDetail.getServiceCharge());
            }
        }
      return totalServiceCharge;
   }
   
   private BigDecimal totalPajak(List<ClassPrintLaporanPendapatanKamar>listPendapatanKamar){
       BigDecimal totalPajak = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanKamar getPendapatanKamar : listPendapatanKamar){
           for(ClassPrintLaporanPendapatanKamarDetail getPendapatanKamarDetail : getPendapatanKamar.getLaporanPendapatanDetail()){
             totalPajak = totalPajak.add(getPendapatanKamarDetail.getPajak());
            }
        }
      return totalPajak;
   }
   
   private BigDecimal totalCompliment(List<ClassPrintLaporanPendapatanKamar>listPendapatanKamar){
       BigDecimal totalHargaCompliment = new BigDecimal(0);
       for(ClassPrintLaporanPendapatanKamar getPendapatanKamar : listPendapatanKamar){
           for(ClassPrintLaporanPendapatanKamarDetail getPendapatanKamarDetail : getPendapatanKamar.getLaporanPendapatanDetail()){
             totalHargaCompliment = totalHargaCompliment.add(getPendapatanKamarDetail.getCompliment());
            }
        }
      return totalHargaCompliment;
   }
   
   private void reportRoomIncomePrintHandle(){
     Date startDate = null;
     Date endDate = null;
     TblTravelAgent travelAgent = null;
     TblRoomType roomType = null;
     RefReservationOrderByType reservationType = null;
     
       if(dpStartPeriode.getValue()!=null && dpEndPeriode.getValue()!=null){
         startDate = Date.valueOf(dpStartPeriode.getValue());
         endDate = Date.valueOf(dpEndPeriode.getValue());
       }
       
       if(chbRoomType.isSelected()){
           if(cbpRoomType.getValue()!=null){
             roomType = cbpRoomType.getValue();
            }
       }
       
       if(chbTravelAgent.isSelected()){
           if(cbpTravelAgent.getValue()!=null){
             travelAgent = cbpTravelAgent.getValue();
           } 
       }
       
       if(chbReservationType.isSelected()){
           if(cbpReservationType.getValue()!=null){
              reservationType = cbpReservationType.getValue();
           }
       }
           
/*     List<TblReservationRoomTypeDetailRoomPriceDetail>list = parentController.getFReportManager().getAllDataReservationRoomPriceDetailPeriode(
                                                             startDate,endDate,travelAgent,roomType,reservationType); */
    
 //    SwingNode swingNode = printReportRoomIncome(list);
 //    swingNodeLayout(swingNode);
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
    
    public ReportRoomIncomeController(FeatureReportFrontOfficeController parentController){
       this.parentController = parentController;
    }
    
    private final FeatureReportFrontOfficeController parentController;
}
