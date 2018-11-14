/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_house_keeping.report_found_information;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanTemuanBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanTemuanBarangDetail;
import hotelfx.persistence.model.RefLostFoundStatus;
import hotelfx.persistence.model.TblFoundInformation;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
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
public class ReportFoundInformationController implements Initializable {
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnShow;
    @FXML
    private JFXCheckBox chbStatusFound;
    @FXML
    private AnchorPane ancStatusFoundLayout;
    private JFXCComboBoxTablePopup<RefLostFoundStatus>cbpStatusFound;
    @FXML
    private AnchorPane reportFoundInformationLayout;
    
    private void initFormReportFoundInformation(){
       initDataPopup();
       btnShow.setOnAction((e)->{
          reportFoundInformationPrintHandle();
        
       });
       cbpStatusFound.setVisible(false);
       
     
       cbpStatusFound.setVisible(false);
       chbStatusFound.selectedProperty().addListener((obs,oldVal,newVal)->{
          cbpStatusFound.setVisible(newVal==true);
          cbpStatusFound.setValue(null);
       });
       
      SwingNode swingNode = new SwingNode();
       JRViewer jrView = new JRViewer(null);
       swingNode.setContent(jrView);
       reportFoundInformationLayout(swingNode);
       
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
      cbpStatusFound = new JFXCComboBoxTablePopup(RefLostFoundStatus.class,tblLostFoundStatus,listLostFoundStatus,"","Status",false,400,300);
      cbpStatusFound.setLabelFloat(false);
      
      ancStatusFoundLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpStatusFound,0.0);
      AnchorPane.setLeftAnchor(cbpStatusFound,0.0);
      AnchorPane.setRightAnchor(cbpStatusFound,0.0);
      AnchorPane.setTopAnchor(cbpStatusFound,0.0);
      ancStatusFoundLayout.getChildren().add(cbpStatusFound);
    }
    
    private List<RefLostFoundStatus>loadDataLostFoundStatus(){
      List<RefLostFoundStatus>list = parentController.getFReportManager().getAllLostFoundStatus();
      for(int i = list.size()-1; i>=0 ; i--){
         RefLostFoundStatus lostFoundStatus = list.get(i);
         if(lostFoundStatus.getIdstatus()==0){
            list.remove(lostFoundStatus);
         }
      }
      return list;
    }
    
    private SwingNode printFoundInformation(List<TblFoundInformation>list,Date startDate,Date endDate){
       List<ClassPrintLaporanTemuanBarang>listLaporanBarangTemuan = new ArrayList();
       ClassPrintLaporanTemuanBarang laporanTemuanBarang = new ClassPrintLaporanTemuanBarang();
       laporanTemuanBarang.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+"-"+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate) : "-");
       laporanTemuanBarang.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       List<ClassPrintLaporanTemuanBarangDetail>listLaporanTemuanBarangDetail = new ArrayList();
       for(TblFoundInformation getFoundDetail : list){
           ClassPrintLaporanTemuanBarangDetail foundDetail = new ClassPrintLaporanTemuanBarangDetail();
           if(getFoundDetail.getRefLostFoundStatus().getIdstatus()==2){
               List<TblLostFoundInformationDetail>listLostFoundDetail = parentController.getFReportManager().getAllDataReturnInformation(null,getFoundDetail);
               System.out.println("size :"+listLostFoundDetail.size());
               for(TblLostFoundInformationDetail lostFoundDetail : listLostFoundDetail){
                 foundDetail.setStatus(getFoundDetail.getRefLostFoundStatus().getStatusName()+" oleh:"+lostFoundDetail.getReturnName()+"\nTanggal:"+new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(lostFoundDetail.getReturnDate()));
                }
           }
           else{
              foundDetail.setStatus(getFoundDetail.getRefLostFoundStatus().getStatusName());
           }
       
         foundDetail.setIdFound(getFoundDetail.getCodeFound());
         foundDetail.setKeterangan(getFoundDetail.getFoundNote());
         foundDetail.setLokasiDitemukan(getFoundDetail.getFoundLocation());
         foundDetail.setNamaBarang(getFoundDetail.getItemName());
         foundDetail.setNamaPelapor(getFoundDetail.getFounderName());
         foundDetail.setTipe(getFoundDetail.getRefFounderType().getTypeName());
         foundDetail.setTanggalLapor(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getFoundDetail.getFoundDate()));
         listLaporanTemuanBarangDetail.add(foundDetail);
       }
      laporanTemuanBarang.setListLaporanTemuanBarangDetail(listLaporanTemuanBarangDetail);
      listLaporanBarangTemuan.add(laporanTemuanBarang);
      return ClassPrinter.printLaporanTemuanBarang(listLaporanBarangTemuan);
    }
    
    private void reportFoundInformationPrintHandle(){
      Date startDate = null;
      Date endDate = null;
      SwingNode swingNode = new SwingNode();
      
      RefLostFoundStatus lostFoundStatus = null;
      
      List<TblFoundInformation>list = new ArrayList();
       if(dpStartDate.getValue()!=null && dpEndDate.getValue()!=null){
         startDate = Date.valueOf(dpStartDate.getValue());
         endDate = Date.valueOf(dpEndDate.getValue());
       }
       
       if(chbStatusFound.isSelected()==true){
           if(cbpStatusFound.getValue()!=null){
             lostFoundStatus = cbpStatusFound.getValue();
           } 
       }
       
      list = parentController.getFReportManager().getAllDataFoundInformation(startDate,endDate,lostFoundStatus);
      swingNode = printFoundInformation(list,startDate,endDate);
      reportFoundInformationLayout(swingNode);
    }
    
    private void reportFoundInformationLayout(SwingNode swingNode){
      reportFoundInformationLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,15.0);
      reportFoundInformationLayout.getChildren().add(swingNode);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportFoundInformation();
      // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     public ReportFoundInformationController(FeatureReportHouseKeepingController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportHouseKeepingController parentController;
}
