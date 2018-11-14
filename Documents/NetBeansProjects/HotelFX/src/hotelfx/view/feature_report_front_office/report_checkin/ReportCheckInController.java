/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office.report_checkin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanCheckIn;
import hotelfx.helper.PrintModel.ClassPrintLaporanCheckInDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.view.feature_report_front_office.FeatureReportFrontOfficeController;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Andreas
 */
public class ReportCheckInController implements Initializable{
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private AnchorPane reportCheckInLayout;
    @FXML
    private JFXButton btnShow;
    
    private void initFormReportCheckIn(){
      btnShow.setOnAction((e)->{
         reportCheckInPrintHandle();
      });
      JRViewer jrView = new JRViewer(null);
      SwingNode swingNode = new SwingNode();
      swingNode.setContent(jrView);
      reportCheckInPrintLayout(swingNode);
      initDateCalendar();
    }
    
     private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
    
    private SwingNode printReportCheckIn(List<TblReservationRoomTypeDetail>list,Date startDate,Date endDate){
      List<ClassPrintLaporanCheckIn>listLaporanCheckIn = new ArrayList();
      ClassPrintLaporanCheckIn laporanCheckIn = new ClassPrintLaporanCheckIn();
      laporanCheckIn.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      laporanCheckIn.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate) : "-");
      List<ClassPrintLaporanCheckInDetail>listLaporanCheckInDetail = new ArrayList();
      for(TblReservationRoomTypeDetail getRoomTypeDetail : list){
         ClassPrintLaporanCheckInDetail laporanCheckInDetail  = new ClassPrintLaporanCheckInDetail();
         laporanCheckInDetail.setAlamat(getRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getAddress()!=null ? getRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getAddress() : "-");
         laporanCheckInDetail.setJumlahDewasa(getRoomTypeDetail.getTblReservationCheckInOut().getAdultNumber());
         laporanCheckInDetail.setJumlahAnak(getRoomTypeDetail.getTblReservationCheckInOut().getChildNumber());
         laporanCheckInDetail.setKota(getRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getTown()!=null ? getRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getTown() : "-");
         laporanCheckInDetail.setNoKamar(getRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRoomName());
         laporanCheckInDetail.setNoReservasi(getRoomTypeDetail.getTblReservation().getCodeReservation());
         laporanCheckInDetail.setTanggalCheckIn(new SimpleDateFormat("dd MMM yyyy HH:mm:ss",new Locale("id")).format(new java.sql.Date(getRoomTypeDetail.getTblReservationCheckInOut().getCheckInDateTime().getTime())));
         laporanCheckInDetail.setTanggalCheckOut(new SimpleDateFormat("dd MMM yyyy HH:mm:ss",new Locale("id")).format(new java.sql.Date(getRoomTypeDetail.getCheckOutDateTime().getTime())));
         laporanCheckInDetail.setNamaTamu(getRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getFullName());
         laporanCheckInDetail.setLamaHari(ChronoUnit.DAYS.between(new java.sql.Date(getRoomTypeDetail.getTblReservationCheckInOut().getCheckInDateTime().getTime()).toLocalDate(),new java.sql.Date(getRoomTypeDetail.getCheckOutDateTime().getTime()).toLocalDate()));
         laporanCheckInDetail.setNegara(getRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getRefCountry()!=null ? getRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getRefCountry().getCountryName():"-");
         listLaporanCheckInDetail.add(laporanCheckInDetail);
       }
       laporanCheckIn.setListLaporanCheckInDetail(listLaporanCheckInDetail);
       listLaporanCheckIn.add(laporanCheckIn);
       return ClassPrinter.printLaporanCheckIn(listLaporanCheckIn);
    }
    
    private void reportCheckInPrintHandle(){
      Date startDate = null;
      Date endDate = null;
      List<TblReservationRoomTypeDetail>list = new ArrayList();
      SwingNode swingNode = new SwingNode();
      
       if(dpStartDate.getValue()!=null && dpEndDate!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       list = parentController.getFReportManager().getAllDataRoomTypeDetailByPeriode(startDate, endDate);
       swingNode = printReportCheckIn(list,startDate,endDate);
       reportCheckInPrintLayout(swingNode);
    }
    
    private void reportCheckInPrintLayout(SwingNode swingNode){
      reportCheckInLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,15.0);
      reportCheckInLayout.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportCheckIn();
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportCheckInController(FeatureReportFrontOfficeController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportFrontOfficeController parentController;
}
