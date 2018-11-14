/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_house_keeping.report_lost_information;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanBarangHilang;
import hotelfx.helper.PrintModel.ClassPrintLaporanBarangHilangDetail;
import hotelfx.persistence.model.RefLostFoundStatus;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
import hotelfx.persistence.model.TblLostInformation;
import hotelfx.view.feature_report_house_keeping.FeatureReportHouseKeepingController;
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
public class ReportLostInformationController implements Initializable {
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnShow;
    @FXML
    private AnchorPane ancStatusLostLayout;
    private JFXCComboBoxTablePopup<RefLostFoundStatus>cbpStatusLost;
    @FXML
    private AnchorPane reportLostInformationLayout;
    
    private void initFormReportLostInformation(){
       initDataPopup();
       btnShow.setOnAction((e)->{
          reportLostInformationPrintHandle();
       });
       
      SwingNode swingNode = new SwingNode();
       JRViewer jrView = new JRViewer(null);
       swingNode.setContent(jrView);
       reportLostInformationLayout(swingNode);
       
       initDateCalendar();
    }
    
     private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartDate,dpEndDate);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
     
    private void initDataPopup(){
      TableView<RefLostFoundStatus>tblLostFoundStatus = new TableView();
      TableColumn<RefLostFoundStatus,String>lostFoundStatus = new TableColumn("Status");
      lostFoundStatus.setMinWidth(200);
      lostFoundStatus.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
      tblLostFoundStatus.getColumns().addAll(lostFoundStatus);
      ObservableList<RefLostFoundStatus>listLostFoundStatus = FXCollections.observableArrayList(loadDataLostFoundStatus());
      cbpStatusLost = new JFXCComboBoxTablePopup(RefLostFoundStatus.class,tblLostFoundStatus,listLostFoundStatus,"","Status",false,400,300);
      
      ancStatusLostLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpStatusLost,0.0);
      AnchorPane.setLeftAnchor(cbpStatusLost,0.0);
      AnchorPane.setRightAnchor(cbpStatusLost,0.0);
      AnchorPane.setTopAnchor(cbpStatusLost,0.0);
      ancStatusLostLayout.getChildren().add(cbpStatusLost);
    }
    
    private List<RefLostFoundStatus>loadDataLostFoundStatus(){
      List<RefLostFoundStatus>list = parentController.getFReportManager().getAllLostFoundStatus();
      for(int i = list.size()-1; i>0 ; i--){
         RefLostFoundStatus lostFoundStatus = list.get(i);
         if(lostFoundStatus.getIdstatus()==1){
            list.remove(lostFoundStatus);
         }
      }
      return list;
    }
    
    private SwingNode printLostInformation(List<TblLostInformation>list,Date startDate,Date endDate){
       List<ClassPrintLaporanBarangHilang>listLaporanBarangHilang = new ArrayList();
       ClassPrintLaporanBarangHilang laporanBarangHilang = new ClassPrintLaporanBarangHilang();
       laporanBarangHilang.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate) : "-");
       laporanBarangHilang.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       List<ClassPrintLaporanBarangHilangDetail>listLaporanBarangHilangDetail = new ArrayList();
       for(TblLostInformation getLostDetail : list){
           ClassPrintLaporanBarangHilangDetail lostDetail = new ClassPrintLaporanBarangHilangDetail();
           if(getLostDetail.getRefLostFoundStatus().getIdstatus()==2){
               List<TblLostFoundInformationDetail>listLostFoundDetail = parentController.getFReportManager().getAllDataReturnInformation(getLostDetail,null);
               System.out.println("size :"+listLostFoundDetail.size());
               for(TblLostFoundInformationDetail lostFoundDetail : listLostFoundDetail){
                 lostDetail.setStatus(getLostDetail.getRefLostFoundStatus().getStatusName()+" oleh:"+lostFoundDetail.getReturnName()+"\nTanggal:"+new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(lostFoundDetail.getReturnDate()));
                }
           }
           else{
              lostDetail.setStatus(getLostDetail.getRefLostFoundStatus().getStatusName());
           }
        
         lostDetail.setAlamatPelapor(getLostDetail.getTblPeople().getAddress());
         lostDetail.setIdLost(getLostDetail.getCodeLost());
         lostDetail.setKeterangan(getLostDetail.getLostNote());
         lostDetail.setKontakPelapor(getLostDetail.getTblPeople().getHpnumber());
         lostDetail.setLokasiKehilangan(getLostDetail.getLostLocation());
         lostDetail.setNamaBarang(getLostDetail.getItemName());
         lostDetail.setNamaPelapor(getLostDetail.getTblPeople().getFullName());
         lostDetail.setTanggalLapor(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getLostDetail.getLostDate()));
         listLaporanBarangHilangDetail.add(lostDetail);
       }
      laporanBarangHilang.setListLaporanBarangHilangDetail(listLaporanBarangHilangDetail);
      listLaporanBarangHilang.add(laporanBarangHilang);
      return ClassPrinter.printLaporanBarangHilang(listLaporanBarangHilang);
    }
    
    private void reportLostInformationPrintHandle(){
      Date startDate = null;
      Date endDate = null;
      SwingNode swingNode = new SwingNode();
      
      RefLostFoundStatus lostFoundStatus = null;
      
      List<TblLostInformation>list = new ArrayList();
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(cbpStatusLost.getValue()!=null){
          lostFoundStatus = cbpStatusLost.getValue();
       }
       
      list = parentController.getFReportManager().getAllDataLostInformation(startDate,endDate,lostFoundStatus);
      swingNode = printLostInformation(list,startDate,endDate);
      reportLostInformationLayout(swingNode);
    }
    
    private void reportLostInformationLayout(SwingNode swingNode){
      reportLostInformationLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,15.0);
      reportLostInformationLayout.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportLostInformation();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportLostInformationController(FeatureReportHouseKeepingController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportHouseKeepingController parentController;
}
