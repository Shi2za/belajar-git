/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office.report_checkout;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanCheckOut;
import hotelfx.helper.PrintModel.ClassPrintLaporanCheckOutDetail;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.view.feature_report_front_office.FeatureReportFrontOfficeController;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class ReportCheckOutController implements Initializable{
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane reportCheckOutLayout;
    
    private SwingNode printReportCheckOut(List<TblReservationRoomTypeDetail>list,Date startDate,Date endDate){
      List<ClassPrintLaporanCheckOut>listLaporanCheckOut = new ArrayList();
      List<ClassPrintLaporanCheckOutDetail>listLaporanCheckOutDetail = new ArrayList();
      ClassPrintLaporanCheckOut laporanCheckOut = new ClassPrintLaporanCheckOut();
      laporanCheckOut.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      laporanCheckOut.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate):"-");
      for(TblReservationRoomTypeDetail getDataCheckOut : list){
         ClassPrintLaporanCheckOutDetail laporanCheckOutDetail = new ClassPrintLaporanCheckOutDetail();
         laporanCheckOutDetail.setNamaTamu(getDataCheckOut.getTblReservation().getTblCustomer().getTblPeople().getFullName());
         laporanCheckOutDetail.setNoKamar(getDataCheckOut.getTblReservationCheckInOut().getTblRoom().getRoomName());
         laporanCheckOutDetail.setNoReservasi(getDataCheckOut.getTblReservation().getCodeReservation());
         laporanCheckOutDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy HH:mm",new Locale("id")).format(getDataCheckOut.getTblReservationCheckInOut().getCheckOutDateTime()));
         listLaporanCheckOutDetail.add(laporanCheckOutDetail);
      }
      laporanCheckOut.setListLaporanCheckOutDetail(listLaporanCheckOutDetail);
      listLaporanCheckOut.add(laporanCheckOut);
      return ClassPrinter.printLaporanCheckOut(listLaporanCheckOut);
    }
    
    private void initFormReportCheckOut(){
      btnShow.setOnAction((e)->{
         reportCheckOutPrintHandle();
      });
      JRViewer jrViewer = new JRViewer(null);
      SwingNode swingNode = new SwingNode();
      swingNode.setContent(jrViewer);
      reportCheckOutLayout(swingNode);
      
      initDateCalendar();
    }
    
     private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
     
    private void reportCheckOutPrintHandle(){
      List<TblReservationRoomTypeDetail>list = new ArrayList();
      Date startDate = null;
      Date endDate = null;
      SwingNode swingNode = new SwingNode();
      
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       list = parentController.getFReportManager().getAllReservationCheckOut(startDate, endDate);
       swingNode = printReportCheckOut(list,startDate,endDate);
       reportCheckOutLayout(swingNode);
    }
    
    private void reportCheckOutLayout(SwingNode swingNode){
       reportCheckOutLayout.getChildren().clear();
       AnchorPane.setBottomAnchor(swingNode,15.0);
       AnchorPane.setLeftAnchor(swingNode,15.0);
       AnchorPane.setRightAnchor(swingNode,15.0);
       AnchorPane.setTopAnchor(swingNode,15.0);
       reportCheckOutLayout.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportCheckOut();
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportCheckOutController(FeatureReportFrontOfficeController parentController){
       this.parentController = parentController;
    }
    
    private final FeatureReportFrontOfficeController parentController;
}
