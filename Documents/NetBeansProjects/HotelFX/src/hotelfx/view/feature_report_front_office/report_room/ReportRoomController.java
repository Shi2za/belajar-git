/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office.report_room;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanOccupancyKamar;
import hotelfx.helper.PrintModel.ClassPrintLaporanOccupancyKamarDetail;
import hotelfx.persistence.model.LogRoomTypeHistory;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.view.feature_report_front_office.FeatureReportFrontOfficeController;
import java.math.BigDecimal;
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
public class ReportRoomController implements Initializable {
    
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXCheckBox chbRoomType;
    @FXML
    private AnchorPane ancRoomType;
    private JFXCComboBoxTablePopup<TblRoomType>cbpRoomType;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane showReportLayout;
    
    private void initFormReportRoom(){
      btnShow.setOnAction((e)->{
         reportRoomPrintHandle();
      });
      
      initDataPopup();
      
      cbpRoomType.setVisible(false);
      chbRoomType.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             cbpRoomType.setVisible(true);
             cbpRoomType.setValue(cbpRoomType.getTableView().getItems().get(0));
            // TblRoomType tblRoomType = new 
            // cbpRoomType.valueProperty().addListener();
           }
           else{
             cbpRoomType.setVisible(false);
           }
      });
      
      JRViewer jrView = new JRViewer(null);
      SwingNode swingNode = new SwingNode();
      swingNode.setContent(jrView);
      reportRoomLayout(swingNode);
      
      initDateCalendar();
    }
    
    private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
     
    private void initDataPopup(){
       TableView<TblRoomType>tblRoomType = new TableView();
       TableColumn<TblRoomType,String>roomTypeName = new TableColumn("Tipe Kamar");
       roomTypeName.setCellValueFactory(cellData -> cellData.getValue().roomTypeNameProperty());
       tblRoomType.getColumns().add(roomTypeName);
       ObservableList<TblRoomType>roomTypeList = FXCollections.observableArrayList(loadAllDataRoomType());
       cbpRoomType = new JFXCComboBoxTablePopup(TblRoomType.class,tblRoomType,roomTypeList,"","Tipe Kamar",false,400,300);
       
       ancRoomType.getChildren().clear();
       AnchorPane.setBottomAnchor(cbpRoomType,0.0);
       AnchorPane.setLeftAnchor(cbpRoomType,0.0);
       AnchorPane.setRightAnchor(cbpRoomType,0.0);
       AnchorPane.setTopAnchor(cbpRoomType,0.0);
       ancRoomType.getChildren().add(cbpRoomType);
    }
    
    private List<TblRoomType>loadAllDataRoomType(){
       List<TblRoomType>list = new ArrayList();
       List<TblRoomType>listRoomType = parentController.getFReportManager().getAllDataRoomType();
       TblRoomType roomType = new TblRoomType();
       roomType.setRoomTypeName("Semua Tipe Kamar");
       roomType.setIdroomType(0);
       list.add(roomType);
       list.addAll(listRoomType);
       return list;
    }
    
    private SwingNode printReportRoomType(Date startDate,Date endDate,TblRoomType roomType){
      List<ClassPrintLaporanOccupancyKamar>listLaporanOccupancyKamar = new ArrayList();
      ClassPrintLaporanOccupancyKamar laporanOccupancyKamar = new ClassPrintLaporanOccupancyKamar();
      laporanOccupancyKamar.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      laporanOccupancyKamar.setPeriode(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+
      new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate));
      List<ClassPrintLaporanOccupancyKamarDetail>listLaporanOccupancyKamarDetail = new ArrayList();
      for(Date date = Date.valueOf(dpStartDate.getValue());date.before(Date.valueOf(dpEndDate.getValue().plusDays(1)));date = Date.valueOf(date.toLocalDate().plusDays(1))){   
           if(roomType.getIdroomType()==0){
               List<TblRoomType>listRoomType = parentController.getFReportManager().getAllDataRoomType();
               for(TblRoomType getRoomType : listRoomType){
                 ClassPrintLaporanOccupancyKamarDetail laporanOccupancyKamarDetail = new ClassPrintLaporanOccupancyKamarDetail();
                 laporanOccupancyKamarDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(date));
                 laporanOccupancyKamarDetail.setTipeKamar(getRoomType.getRoomTypeName());
                 laporanOccupancyKamarDetail.setJumlahKamar(countRoomAll(date,getRoomType));
                 //  laporanRekapKamarDetail.setJumlahReservasi(countRoomReservation(date,getRoomType));
                 laporanOccupancyKamarDetail.setJumlahTerjual(countRoomSold(date,getRoomType));
                 laporanOccupancyKamarDetail.setOccupancy(new BigDecimal((laporanOccupancyKamarDetail.getJumlahTerjual()*1.0/laporanOccupancyKamarDetail.getJumlahKamar()*1.0)*100));
                //   laporanRekapKamarDetail.setJumlahKosong(laporanRekapKamarDetail.getJumlahKamar()-(laporanRekapKamarDetail.getJumlahTerisi()+laporanRekapKamarDetail.getJumlahReservasi()));
                 listLaporanOccupancyKamarDetail.add(laporanOccupancyKamarDetail);
               }
           }
           else{
              ClassPrintLaporanOccupancyKamarDetail laporanOccupancyKamarDetail = new ClassPrintLaporanOccupancyKamarDetail();
              laporanOccupancyKamarDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(date));
                 laporanOccupancyKamarDetail.setTipeKamar(roomType.getRoomTypeName());
                 laporanOccupancyKamarDetail.setJumlahKamar(countRoomAll(date,roomType));
                 //  laporanRekapKamarDetail.setJumlahReservasi(countRoomReservation(date,getRoomType));
                 laporanOccupancyKamarDetail.setJumlahTerjual(countRoomSold(date,roomType));
                 laporanOccupancyKamarDetail.setOccupancy(new BigDecimal((laporanOccupancyKamarDetail.getJumlahTerjual()*1.0/laporanOccupancyKamarDetail.getJumlahKamar()*1.0)*100));
                //   laporanRekapKamarDetail.setJumlahKosong(laporanRekapKamarDetail.getJumlahKamar()-(laporanRekapKamarDetail.getJumlahTerisi()+laporanRekapKamarDetail.getJumlahReservasi()));
                 listLaporanOccupancyKamarDetail.add(laporanOccupancyKamarDetail);
           }
       }
      laporanOccupancyKamar.setListOccupancyKamarDetail(listLaporanOccupancyKamarDetail);
      listLaporanOccupancyKamar.add(laporanOccupancyKamar);
      return ClassPrinter.printLaporanOccupancyTipeKamar(listLaporanOccupancyKamar);
     /* List<ClassPrintLaporanOccupancyKamar>listLaporanOccupancyKamar = new ArrayList();
      ClassPrintLaporanOccupancyKamar laporanOccupancyKamar = new ClassPrintLaporanOccupancyKamar();
      laporanOccupancyKamar.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      laporanOccupancyKamar.setPeriode(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+
      new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate));
      List<ClassPrintLaporanOccupancyKamarDetail>listLaporanOccupancyKamarDetail = new ArrayList();
      List<TblRoomType>listRoomType = parentController.getFReportManager().getAllDataRoomType();
       for(Date date = Date.valueOf(dpStartDate.getValue());date.before(Date.valueOf(dpEndDate.getValue().plusDays(1)));date = Date.valueOf(date.toLocalDate().plusDays(1))){
           for(TblRoomType getRoomType : listRoomType){
             ClassPrintLaporanOccupancyKamarDetail laporanOccupancyKamarDetail = new ClassPrintLaporanOccupancyKamarDetail();
             laporanOccupancyKamarDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(date));
             laporanOccupancyKamarDetail.setTipeKamar(getRoomType.getRoomTypeName());
             laporanOccupancyKamarDetail.setJumlahKamar(countRoomAll(date,getRoomType));
          //  laporanRekapKamarDetail.setJumlahReservasi(countRoomReservation(date,getRoomType));
             laporanOccupancyKamarDetail.setJumlahTerjual(countRoomSold(date,getRoomType));
             laporanOccupancyKamarDetail.setOccupancy(new BigDecimal((laporanOccupancyKamarDetail.getJumlahTerjual()*1.0/laporanOccupancyKamarDetail.getJumlahKamar()*1.0)*100));
          //   laporanRekapKamarDetail.setJumlahKosong(laporanRekapKamarDetail.getJumlahKamar()-(laporanRekapKamarDetail.getJumlahTerisi()+laporanRekapKamarDetail.getJumlahReservasi()));
             listLaporanOccupancyKamarDetail.add(laporanOccupancyKamarDetail);
            }
       } 
       laporanOccupancyKamar.setListOccupancyKamarDetail(listLaporanOccupancyKamarDetail);
       listLaporanOccupancyKamar.add(laporanOccupancyKamar);
       ClassPrinter.printLaporanOccupancyKamar(listLaporanOccupancyKamar); */
    }
    
     private SwingNode printReportRoom(Date startDate,Date endDate){
      List<ClassPrintLaporanOccupancyKamar>listLaporanOccupancyKamar = new ArrayList();
      ClassPrintLaporanOccupancyKamar laporanOccupancyKamar = new ClassPrintLaporanOccupancyKamar();
      laporanOccupancyKamar.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      laporanOccupancyKamar.setPeriode(new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+
      new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate));
      List<ClassPrintLaporanOccupancyKamarDetail>listLaporanOccupancyKamarDetail = new ArrayList();
      List<TblRoomType>listRoomType = parentController.getFReportManager().getAllDataRoomType();
      
       for(Date date = Date.valueOf(dpStartDate.getValue());date.before(Date.valueOf(dpEndDate.getValue().plusDays(1)));date = Date.valueOf(date.toLocalDate().plusDays(1))){
           ClassPrintLaporanOccupancyKamarDetail laporanOccupancyKamarDetail = new ClassPrintLaporanOccupancyKamarDetail();
           long countRoom = 0;
           long countRoomSold = 0;
           for(TblRoomType roomType : listRoomType){
              countRoom+=countRoomAll(date,roomType);
              countRoomSold+=countRoomSold(date,roomType);
            }
           laporanOccupancyKamarDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(date));
           laporanOccupancyKamarDetail.setJumlahKamar(countRoom);
           laporanOccupancyKamarDetail.setJumlahTerjual(countRoomSold);
           laporanOccupancyKamarDetail.setOccupancy(new BigDecimal((laporanOccupancyKamarDetail.getJumlahTerjual()*1.0/laporanOccupancyKamarDetail.getJumlahKamar()*1.0)*100));
           listLaporanOccupancyKamarDetail.add(laporanOccupancyKamarDetail);
       } 
       laporanOccupancyKamar.setListOccupancyKamarDetail(listLaporanOccupancyKamarDetail);
       listLaporanOccupancyKamar.add(laporanOccupancyKamar);
       return ClassPrinter.printLaporanOccupancyKamar(listLaporanOccupancyKamar);
    }
     
    private long countRoomAll(Date date,TblRoomType roomType){
       List<LogRoomTypeHistory>list = parentController.getFReportManager().getAllRoomTypeHistory(roomType);
       System.out.println("hsl :"+list.size());
       long count  = 0;
       
       for(LogRoomTypeHistory getLogRoomTypeHistory : list){
           if(date.before(getLogRoomTypeHistory.getHistoryDate()) && roomType.getIdroomType()==getLogRoomTypeHistory.getTblRoomType().getIdroomType()){
                 // count = getLogRoomTypeHistory.getRoomTypeQuantity();
             break;
            }
           else{
             count = getLogRoomTypeHistory.getRoomTypeQuantity();
            }
        }
      return count;
    }
    
    private long countRoomReservation(Date date,TblRoomType roomType){
      List<TblReservationRoomTypeDetailRoomPriceDetail>list = parentController.getFReportManager().getAllRoomTypeReservation(date, roomType);
      long count = 0;
      for(TblReservationRoomTypeDetailRoomPriceDetail getRoomReservation : list){
           if(getRoomReservation.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()==roomType.getIdroomType() && getRoomReservation.getTblReservationRoomPriceDetail().getDetailDate().equals(date)){
             count++;
           }
       }
      return count;
    }
    
    private long countRoomSold(Date date,TblRoomType roomType){
       List<TblReservationRoomTypeDetailRoomPriceDetail>list = parentController.getFReportManager().getAllRoomSold(date, roomType);
       System.out.println("hsl room sold : "+list.size());
       long count = 0;
       for(TblReservationRoomTypeDetailRoomPriceDetail getReservationSold : list){
           if(roomType!=null){
               if(getReservationSold.getTblReservationRoomPriceDetail().getDetailDate().equals(date) && roomType.getIdroomType()==getReservationSold.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()){
                 count++;
                }
           }
           else{
               if(getReservationSold.getTblReservationRoomPriceDetail().getDetailDate().equals(date)){
                 count++;
               }
           }
        }
      return count;
    }
    
    
    private void reportRoomPrintHandle(){
       if(checkDataInput()){
         Date startDate = Date.valueOf(dpStartDate.getValue());
         Date endDate = Date.valueOf(dpEndDate.getValue());
         SwingNode swingNode = new SwingNode();
           if(chbRoomType.isSelected()==true){
               if(cbpRoomType.getValue()!=null){
                 swingNode = printReportRoomType(startDate,endDate,cbpRoomType.getValue());
                 reportRoomLayout(swingNode);
               }
           }
           else{
             swingNode = printReportRoom(startDate,endDate);
             reportRoomLayout(swingNode);
           }
        
       }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput, null);
       }
    }
    
    private void reportRoomLayout(SwingNode swingNode){
      showReportLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,15.0);
      showReportLayout.getChildren().add(swingNode);
    }
            
    String errDataInput;
    private boolean checkDataInput(){
       boolean check = true;
       errDataInput = "";
       if(dpStartDate.getValue()==null){
          errDataInput += "Periode Awal : "+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(dpEndDate.getValue()==null){
         errDataInput +="Periode Akhir : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       
      return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportRoom();
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportRoomController(FeatureReportFrontOfficeController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportFrontOfficeController parentController;
}
