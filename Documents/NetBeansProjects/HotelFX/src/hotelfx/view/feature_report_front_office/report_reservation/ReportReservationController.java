/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office.report_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassReservationType;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintDataLaporanReservasi;
import hotelfx.helper.PrintModel.ClassPrintDataLaporanReservasiDetail;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.view.feature_report_front_office.FeatureReportFrontOfficeController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
public class ReportReservationController implements Initializable{
    @FXML
    private JFXCheckBox chbCodeReservation;
    @FXML
    private JFXCheckBox chbTypeReservation;
    @FXML
    private JFXCheckBox chbStatusReservation;
    @FXML
    private JFXCheckBox chbTravelAgent;
    @FXML
    private JFXDatePicker dpPeriodeStart;
    @FXML
    private JFXDatePicker dpPeriodeEnd;
    @FXML
    private AnchorPane ancReportReservation;
    @FXML
    private JFXButton btnShowPrint;
    @FXML
    private AnchorPane ancReservationCode;
    JFXCComboBoxTablePopup<TblReservation>cbpReservationCode;
    @FXML
    private AnchorPane ancReservationStatus;
    JFXCComboBoxTablePopup<RefReservationStatus>cbpReservationStatus;
    @FXML
    private AnchorPane ancReservationTravelAgent;
    JFXCComboBoxTablePopup<TblTravelAgent>cbpTravelAgent;
    @FXML
    private AnchorPane ancReservationType;
    JFXCComboBoxTablePopup<RefReservationOrderByType>cbpReservationType;
    ToggleGroup tglReport;
    
    private List<TblReservation>loadAllDataReservation(Date startDate, Date endDate,
                                                       RefReservationStatus reservationStatus,
                                                       TblTravelAgent travelAgent,
                                                       RefReservationOrderByType reservationType,
                                                       TblReservation reservation){
      List<TblReservation>list = new ArrayList();
         list = parentController.getFReportManager().getAllDataReservationByPeriode(startDate, endDate, reservationStatus, travelAgent, reservationType,reservation);  
      return list;
    }
    
    private SwingNode printReportReservation(List<TblReservation>list){
       List<ClassPrintDataLaporanReservasi>listLaporanReservasi = new ArrayList();
       List<ClassPrintDataLaporanReservasiDetail>listLaporanReservasiDetail = new ArrayList();
       //SwingNode swingNode = new SwingNode();
       
       ClassPrintDataLaporanReservasi laporanReservasi = new ClassPrintDataLaporanReservasi();
       laporanReservasi.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       
       for(TblReservation getDataReservation : list){
         ClassPrintDataLaporanReservasiDetail dataReservasi = new ClassPrintDataLaporanReservasiDetail();
         dataReservasi.setKodeReservasi(getDataReservation.getCodeReservation());
         dataReservasi.setNamaCustomer(getDataReservation.getTblCustomer().getTblPeople().getFullName());
         dataReservasi.setStatus(getDataReservation.getRefReservationStatus().getStatusName());
         dataReservasi.setTanggalReservasi(new SimpleDateFormat("dd MMMM yyyy",new java.util.Locale("id")).format(getDataReservation.getReservationDate()));
         dataReservasi.setTravelAgent(getDataReservation.getTblPartner()==null ? "-" : getDataReservation.getTblPartner().getPartnerName());
         dataReservasi.setTotalTagihan(totalBill(getDataReservation.getIdreservation()));
         dataReservasi.setTanggalMasuk(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(getDateMin(getDataReservation.getIdreservation())));
         dataReservasi.setTanggalKeluar(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(getDateMax(getDataReservation.getIdreservation())));
         listLaporanReservasiDetail.add(dataReservasi);
       }
       laporanReservasi.setReservasiDetail(listLaporanReservasiDetail);
       listLaporanReservasi.add(laporanReservasi);
       
       String periode = "";
       if(dpPeriodeStart.getValue()==null && dpPeriodeEnd.getValue()==null){
          periode = "Semua tanggal reservasi";
       }
       else{
           if(dpPeriodeStart.getValue().equals(dpPeriodeEnd.getValue())){
             periode = new SimpleDateFormat("dd MMMM yyyy").format(Date.valueOf(dpPeriodeStart.getValue()));
            }
           else{
             periode = new SimpleDateFormat("dd MMMM yyyy").format(Date.valueOf(dpPeriodeStart.getValue()))+" - "+
                    new SimpleDateFormat("dd MMMM yyyy").format(Date.valueOf(dpPeriodeEnd.getValue()));
           }      
       }
       
       return ClassPrinter.printReportReservation(listLaporanReservasi,periode);
    }
    
    private Date getDateMin(long id){
       List<TblReservationRoomTypeDetailRoomPriceDetail>list = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByIDReservation(id);
       Date dateResult = null;
       List<Date>dateCheckIn = new ArrayList();
       for(TblReservationRoomTypeDetailRoomPriceDetail getRoomPriceDetail :list){
         dateCheckIn.add(new Date(getRoomPriceDetail.getTblReservationRoomTypeDetail().getCheckInDateTime().getTime()));
               
        }
        dateResult = dateCheckIn.stream().min(Date::compareTo).get(); 
      return dateResult;
    }
    
    private Date getDateMax(long id){
       List<TblReservationRoomTypeDetailRoomPriceDetail>list = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByIDReservation(id);
       Date dateResult = null;
       List<Date>dateCheckOut = new ArrayList();
       for(TblReservationRoomTypeDetailRoomPriceDetail getRoomPriceDetail :list){
          dateCheckOut.add(new Date(getRoomPriceDetail.getTblReservationRoomTypeDetail().getCheckOutDateTime().getTime()));
        }
       dateResult = dateCheckOut.stream().max(Date::compareTo).get(); 
      return dateResult;
    }
    
    private BigDecimal totalBill(long id){
      BigDecimal totalBillReservation = new BigDecimal(0);
      BigDecimal totalBillCheckOut = new BigDecimal(0);
      BigDecimal total = new BigDecimal(0);
      List<TblReservationBill>listBillReservation = parentController.getFReportManager().getAllDataReservationBill(id,(int)0);
      List<TblReservationBill>listBillCheckOut = parentController.getFReportManager().getAllDataReservationBill(id,(int)1);
      List<TblReservationPayment>listPaymentReservation = parentController.getFReportManager().getAllDataReservationPayment(id,(int)0);
      List<TblReservationPayment>listPaymentCheckOut = parentController.getFReportManager().getAllDataReservationPayment(id,(int)1);
      
      BigDecimal totalRoomPrice = new BigDecimal(0);
      BigDecimal totalItemPriceReservation = new BigDecimal(0);
      BigDecimal totalItemPriceCheckOut = new BigDecimal(0);
      BigDecimal totalServicePriceReservation = new BigDecimal(0);
      BigDecimal totalServicePriceCheckOut = new BigDecimal(0);
      BigDecimal totalBrokenItemPrice = new BigDecimal(0);
      
      List<TblReservationRoomTypeDetailRoomPriceDetail>listRoom = parentController.getFReportManager().getAllDataReservationRoomPriceDetailByIDReservation(id);
      List<TblReservationAdditionalItem>listItem = parentController.getFReportManager().getAllDataReservationAdditionalItemDetailByIDReservation(id);
      List<TblReservationAdditionalService>listService = parentController.getFReportManager().getAllDataReservationAdditionalServiceByIDReservation(id);
      List<TblReservationBrokenItem>listBrokenItem = parentController.getFReportManager().getAllDataBrokenItem(id);
      
       for(TblReservationRoomTypeDetailRoomPriceDetail getRoomPrice : listRoom){
         BigDecimal roomPriceDiscount = getRoomPrice.getTblReservationRoomPriceDetail().getDetailPrice().multiply(getRoomPrice.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal(100))).add(getRoomPrice.getTblReservationRoomPriceDetail().getDetailComplimentary());
         totalRoomPrice = totalRoomPrice.add(getRoomPrice.getTblReservationRoomPriceDetail().getDetailPrice().subtract(roomPriceDiscount));
       }
      
       for(TblReservationAdditionalItem getItem : listItem){
           BigDecimal itemPrice = new BigDecimal(0);;
           BigDecimal itemPriceDiscount = new BigDecimal(0);
           if(getItem.getRefReservationBillType().getIdtype()==0){
             itemPrice = getItem.getItemCharge().multiply(getItem.getItemQuantity());
             itemPriceDiscount =  itemPrice.multiply(getItem.getDiscountPercentage().divide(new BigDecimal(100)));
             totalItemPriceReservation = totalItemPriceReservation.add(itemPrice.subtract(itemPriceDiscount));
           }
           
           if(getItem.getRefReservationBillType().getIdtype()==1){
             itemPrice = getItem.getItemCharge().multiply(getItem.getItemQuantity());
             itemPriceDiscount =  itemPrice.multiply(getItem.getDiscountPercentage().divide(new BigDecimal(100)));
             totalItemPriceCheckOut =  totalItemPriceCheckOut.add(itemPrice.subtract(itemPriceDiscount));
           }
        }
       
       for(TblReservationAdditionalService getService : listService){
           BigDecimal servicePriceDiscount = new BigDecimal(0);
           if(getService.getRefReservationBillType().getIdtype()==0){
             servicePriceDiscount = getService.getPrice().multiply(getService.getDiscountPercentage().divide(new BigDecimal(100)));
             totalServicePriceReservation = totalServicePriceReservation.add(getService.getPrice().subtract(servicePriceDiscount));
           }
           
           if(getService.getRefReservationBillType().getIdtype()==1){
             servicePriceDiscount = getService.getPrice().multiply(getService.getDiscountPercentage().divide(new BigDecimal(100)));
             totalServicePriceCheckOut = totalServicePriceCheckOut.add(getService.getPrice().subtract(servicePriceDiscount));
           }
        }
        
       for(TblReservationBrokenItem getBrokenItem : listBrokenItem){
         BigDecimal brokenItemPrice = getBrokenItem.getItemCharge().multiply(getBrokenItem.getItemQuantity());
         totalBrokenItemPrice = totalBrokenItemPrice.add(brokenItemPrice);
       }
       for(TblReservationBill getBill : listBillReservation){
         BigDecimal temp = totalRoomPrice.add(totalItemPriceReservation).add(totalServicePriceReservation);
         BigDecimal tempServiceCharge = temp.multiply(getBill.getServiceChargePercentage());
         BigDecimal tempTax = (temp.add(tempServiceCharge)).multiply(getBill.getTaxPercentage());
         totalBillReservation = temp.add(tempServiceCharge).add(tempTax);
       }
       
       for(TblReservationBill getBill : listBillCheckOut){
          BigDecimal temp = totalItemPriceCheckOut.add(totalServicePriceCheckOut).add(totalBrokenItemPrice);
          BigDecimal tempServiceCharge = temp.multiply(getBill.getServiceChargePercentage());
          BigDecimal tempTax = (temp.add(tempServiceCharge)).multiply(getBill.getTaxPercentage());
          totalBillCheckOut = temp.add(tempServiceCharge).add(tempTax);
       }
       
       for(TblReservationPayment getPayment : listPaymentReservation){
         totalBillReservation = totalBillReservation.add(getPayment.getRoundingValue());
       }
       
       for(TblReservationPayment getPayment : listPaymentCheckOut){
         totalBillCheckOut = totalBillCheckOut.add(getPayment.getRoundingValue());
       } 
        
       total = totalBillReservation.add(totalBillCheckOut);
       
       return total;
    }
    
    private void initForm(){
      btnShowPrint.setOnAction((e)->{
         showReportReservationHandle();
      });
      
     // tglReport = new ToggleGroup();
     /* rdCodeReservation.setToggleGroup(tglReport);
      rdTypeReservation.setToggleGroup(tglReport);
      rdStatusReservation.setToggleGroup(tglReport); */
      
      chbCodeReservation.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.booleanValue()==true){
             initDialogPopupReservation();
           }
           else{
             cbpReservationCode.setVisible(false);
           }
          
      });
      
      chbTypeReservation.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.booleanValue()==true){
             initDataPopupReservationType();
           }
          else{
             cbpReservationType.setVisible(false);
            }
      });
      
      chbStatusReservation.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.booleanValue()==true){
             initDataPopupReservationStatus();
           }
           else{
             cbpReservationStatus.setVisible(false);
           }
      });
      
      chbTravelAgent.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.booleanValue()==true){
             initDataPopupTravelAgent();
           }
           else{
             cbpTravelAgent.setVisible(false);
           }
      });
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpPeriodeStart,dpPeriodeEnd);
      
      //List<TblReservation>listReservation = new ArrayList();
       SwingNode swingNode = new SwingNode();
       JRViewer jrView = new JRViewer(null);
       swingNode.setContent(jrView);
      swingNodeLayout(swingNode);
      
      initDateCalendar();
    }
    
     private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpPeriodeStart,dpPeriodeEnd);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpPeriodeStart,dpPeriodeEnd);
    }
     
    private void showReportReservationHandle(){
     // printReportReservation();
        List<TblReservation>listReservation = null;
       
         //System.out.println("total list :"+listReservation.size());
        Date dateStart = null;
        Date dateEnd = null;
        RefReservationOrderByType reservationType = null;
        RefReservationStatus reservationStatus= null;
        TblTravelAgent travelAgent = null;
        TblReservation reservation = null;
        
        if(dpPeriodeStart.getValue()!=null && dpPeriodeEnd.getValue()!=null){
           dateStart = Date.valueOf(dpPeriodeStart.getValue());
           dateEnd = Date.valueOf(dpPeriodeEnd.getValue());
        }
        else{
          dateStart = null;
          dateEnd = null;
        }
           
        if(chbStatusReservation.isSelected()){
          reservationStatus = cbpReservationStatus.getValue();
        }
   
       if(chbTypeReservation.isSelected()){              
          reservationType = cbpReservationType.getValue();
        }
        
       if(chbTravelAgent.isSelected()){
          travelAgent = cbpTravelAgent.getValue();
        }
       
       if(chbCodeReservation.isSelected()){
          reservation = cbpReservationCode.getValue();
        }
        
        listReservation = loadAllDataReservation(dateStart,dateEnd,reservationStatus,travelAgent,reservationType,reservation);
       
        SwingNode swingNode = printReportReservation(listReservation);
        swingNodeLayout(swingNode);
      //rdTypeReservation.setToggleGroup(tglReport);
      //rdStatusReservation.setToggleGroup(tglReport);
    }
    
    private void swingNodeLayout(SwingNode swingNode){
       ancReportReservation.getChildren().clear();
       AnchorPane.setTopAnchor(swingNode,0.0);
       AnchorPane.setBottomAnchor(swingNode,15.0);
       AnchorPane.setLeftAnchor(swingNode,15.0);
       AnchorPane.setRightAnchor(swingNode,15.0);
       ancReportReservation.getChildren().add(swingNode);
    }
    
    private void initDialogPopupReservation(){
      TableView<TblReservation>tblReservation = new TableView();
      TableColumn<TblReservation,String>reservationCode = new TableColumn("Kode Reservasi");
      reservationCode.setMinWidth(100);
      reservationCode.setCellValueFactory(cellData -> cellData.getValue().codeReservationProperty());
      TableColumn<TblReservation,String>customerName = new TableColumn("Nama Customer");
      customerName.setMinWidth(160);
      customerName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblCustomer() == null ? "-" : param.getValue().getTblCustomer().getTblPeople().getFullName(),param.getValue().getTblCustomer().tblPeopleProperty()));
      tblReservation.getColumns().addAll(reservationCode,customerName);
      ObservableList<TblReservation>listReservation = FXCollections.observableArrayList(loadAllDataReservation());
      
      cbpReservationCode = new JFXCComboBoxTablePopup(TblReservation.class,tblReservation,listReservation,"","Nomor Reservasi",true,500,300);
      cbpReservationCode.setLabelFloat(false);
      
      ancReservationCode.getChildren().clear();
      AnchorPane.setTopAnchor(cbpReservationCode,0.0);
      AnchorPane.setBottomAnchor(cbpReservationCode,0.0);
      AnchorPane.setLeftAnchor(cbpReservationCode,0.0);
      AnchorPane.setRightAnchor(cbpReservationCode,0.0);
      ancReservationCode.getChildren().add(cbpReservationCode);
    }
    
    private void initDataPopupReservationStatus(){
      TableView<RefReservationStatus>tblReservationStatus = new TableView();
      TableColumn<RefReservationStatus,String>reservationStatus = new TableColumn("Satus Reservasi");
      reservationStatus.setMinWidth(140);
      reservationStatus.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
      tblReservationStatus.getColumns().addAll(reservationStatus);
      ObservableList<RefReservationStatus>listReservationStatus = FXCollections.observableArrayList(loadAllDataReservationStatus());
      
       cbpReservationStatus = new JFXCComboBoxTablePopup(RefReservationStatus.class,tblReservationStatus,listReservationStatus,"","Status Reservasi",false,500,300);
       cbpReservationStatus.setLabelFloat(false);
       
        ancReservationStatus.getChildren().clear();
        AnchorPane.setTopAnchor(cbpReservationStatus,0.0);
        AnchorPane.setBottomAnchor(cbpReservationStatus,0.0);
        AnchorPane.setLeftAnchor(cbpReservationStatus,0.0);
        AnchorPane.setRightAnchor(cbpReservationStatus,0.0);
        ancReservationStatus.getChildren().add(cbpReservationStatus);
    }
    
    private void initDataPopupReservationType(){
      TableView<RefReservationOrderByType>tblReservationType = new TableView();
      TableColumn<RefReservationOrderByType,String>reservationType = new TableColumn("Tipe Reservasi");
      reservationType.setMinWidth(120);
      reservationType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
      tblReservationType.getColumns().addAll(reservationType);
      ObservableList<RefReservationOrderByType>listReservationType = FXCollections.observableArrayList(loadAllDataReservationType());
      
      cbpReservationType = new JFXCComboBoxTablePopup(RefReservationOrderByType.class,tblReservationType,listReservationType,"","Tipe Reservasi",false,500,300);
      cbpReservationType.setLabelFloat(false);
      
      ancReservationType.getChildren().clear();
      AnchorPane.setTopAnchor(cbpReservationType,0.0);
      AnchorPane.setBottomAnchor(cbpReservationType,0.0);
      AnchorPane.setLeftAnchor(cbpReservationType,0.0);
      AnchorPane.setRightAnchor(cbpReservationType,0.0);
      ancReservationType.getChildren().add(cbpReservationType);
    }
    
    private void initDataPopupTravelAgent(){
      TableView<TblTravelAgent>tblTravelAgent = new TableView();
      TableColumn<TblTravelAgent,String>travelAgentName = new TableColumn("Travel Agent");
      travelAgentName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblPartner().getPartnerName(),param.getValue().getTblPartner().partnerNameProperty()));
      tblTravelAgent.getColumns().addAll(travelAgentName);
      ObservableList<TblTravelAgent>listTravelAgent = FXCollections.observableArrayList(loadAllDataTravelAgent());
      
      cbpTravelAgent = new JFXCComboBoxTablePopup(TblTravelAgent.class,tblTravelAgent,listTravelAgent,"","Travel Agent",false,500,300);
      cbpTravelAgent.setLabelFloat(false);
      
      ancReservationTravelAgent.getChildren().clear();
      AnchorPane.setTopAnchor(cbpTravelAgent,0.0);
      AnchorPane.setBottomAnchor(cbpTravelAgent,0.0);
      AnchorPane.setLeftAnchor(cbpTravelAgent,0.0);
      AnchorPane.setRightAnchor(cbpTravelAgent,0.0);
      ancReservationTravelAgent.getChildren().add(cbpTravelAgent);
    }
    
    private List<TblReservation>loadAllDataReservation(){
      List<TblReservation>listReservation = new ArrayList();
     /* TblReservation reservation = new TblReservation();
      TblCustomer customer = new TblCustomer();
      TblPeople people = new TblPeople();
      people.setFullName("Semua nama customer");
      customer.setTblPeople(people);
      
      reservation.setIdreservation(0);
      reservation.setCodeReservation("-");
      reservation.setTblCustomer(customer);
      listReservation.add(reservation); */
      listReservation.addAll(parentController.getFReportManager().getAllDataReservation());
      return listReservation;
    }
    
    private List<RefReservationStatus>loadAllDataReservationStatus(){
      List<RefReservationStatus>listReservationStatus = new ArrayList();
    /*  RefReservationStatus reservationStatus = new RefReservationStatus();
      reservationStatus.setIdstatus(8);
      reservationStatus.setStatusName("Semua Status Reservasi");
      
      listReservationStatus.add(reservationStatus); */
      listReservationStatus.addAll(parentController.getFReportManager().getAllDataReservationStatus());
      return listReservationStatus;
    }
    
    private List<TblTravelAgent>loadAllDataTravelAgent(){
      List<TblTravelAgent>listTravelAgent = new ArrayList();
    /*  TblTravelAgent travelAgent = new TblTravelAgent();
      TblPartner tblPartner = new TblPartner();
      tblPartner.setPartnerName("Semua Travel Agent");
      travelAgent.setIdtravelAgent(0);
      travelAgent.setTblPartner(tblPartner);
      listTravelAgent.add(travelAgent); */
      listTravelAgent.addAll(parentController.getFReportManager().getAllDataTravelAgent());
      return listTravelAgent;
    }
    
    private List<RefReservationOrderByType>loadAllDataReservationType(){
      List<RefReservationOrderByType>list = new ArrayList();
      
    /*  ClassReservationType reservationAllType = new ClassReservationType();
      reservationAllType.setIdType(0);
      reservationAllType.setReservationName("Semua Tipe Reservasi"); */
      
    /*  ClassReservationType reservationTypeCustomer = new ClassReservationType();
      reservationTypeCustomer.setIdType(1);
      reservationTypeCustomer.setReservationName("Customer");
      
      ClassReservationType reservationTypeTravelAgent = new ClassReservationType();
      reservationTypeTravelAgent.setIdType(2);
      reservationTypeTravelAgent.setReservationName("Travel Agent");
      
    //  list.add(reservationAllType);
      list.add(reservationTypeCustomer);
      list.add(reservationTypeTravelAgent); */
      
      list = parentController.getFReportManager().getAllDataReservationType();
      return list;
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     initForm();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportReservationController(FeatureReportFrontOfficeController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportFrontOfficeController parentController;
}
