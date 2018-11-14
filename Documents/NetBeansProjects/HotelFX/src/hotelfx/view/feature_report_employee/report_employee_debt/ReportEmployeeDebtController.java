/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_employee.report_employee_debt;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassDataDebtStatus;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.PrintModel.ClassPrintLaporanHutangKaryawan;
import hotelfx.helper.PrintModel.ClassPrintLaporanHutangKaryawanDetail;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.view.feature_report_employee.FeatureReportEmployeeController;
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
public class ReportEmployeeDebtController implements Initializable{
    
    @FXML
    private JFXDatePicker dpStartPeriode;
    @FXML 
    private JFXDatePicker dpEndPeriode;
    @FXML
    private JFXCheckBox chbEmployee;
    @FXML
    private AnchorPane ancCbpEmployee;
    private JFXCComboBoxTablePopup<TblEmployee> cbpEmployee;
    
    @FXML
    private JFXCheckBox chbDebtStatus;
    @FXML
    private AnchorPane ancDebtStatus;
    private JFXCComboBoxTablePopup<ClassDataDebtStatus> cbpDebtStatus;
    
    @FXML
    private AnchorPane ancShowReportEmployeeDebt;
    @FXML
    private JFXButton btnShow;
    
    private void initFormReportEmployee(){
       initDataPopup();
       cbpEmployee.setVisible(false);
       cbpDebtStatus.setVisible(false);
       
       SwingNode swingNode = new SwingNode();
       
       chbEmployee.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.booleanValue()==false){
             cbpEmployee.setValue(null);
             cbpEmployee.setVisible(false);
           }
           else{
             cbpEmployee.setVisible(true);
           }
       });
       
       chbDebtStatus.selectedProperty().addListener((obs,oldVal,newVal)->{
          // cbpDebtStatus.setVisible(newVal==true);
           if(newVal.booleanValue()==false){
             cbpDebtStatus.setValue(null);
             cbpDebtStatus.setVisible(false);
            }
           else{
             cbpDebtStatus.setVisible(true);
           }
       });
       
       btnShow.setOnAction((e)->{
          reportEmployeeDebtPrintHandle();
       });
       
       JRViewer jrView = new JRViewer(null);
       swingNode.setContent(jrView);
       printReporyEmployeeLayout(swingNode);
       
       initDateCalendar();
    }
    
   private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpStartPeriode,dpEndPeriode);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartPeriode,dpEndPeriode);
    }
    
    private void initDataPopup(){
      TableView<TblEmployee>tblEmployee = new TableView();
      TableColumn<TblEmployee,String>employeeCode = new TableColumn("ID");
      employeeCode.setMinWidth(100);
      employeeCode.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
      TableColumn<TblEmployee,String>employeeName = new TableColumn("Nama");
      employeeName.setMinWidth(160);
      employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().tblPeopleProperty()));
      TableColumn<TblEmployee,String>employeeType = new TableColumn("Tipe");
      employeeType.setMinWidth(140);
      employeeType.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeType().getTypeName(),param.getValue().refEmployeeTypeProperty()));
      TableColumn<TblEmployee,String>employeeJob = new TableColumn("Jabatan");
      employeeJob.setMinWidth(140);
      employeeJob.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblJob().getJobName(),param.getValue().refEmployeeTypeProperty()));
      tblEmployee.getColumns().addAll(employeeCode,employeeName,employeeType,employeeJob);
      ObservableList<TblEmployee>listEmployeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
      cbpEmployee = new JFXCComboBoxTablePopup(TblEmployee.class,tblEmployee,listEmployeeItems,"","Karyawan",true,600,300);
      cbpEmployee.setLabelFloat(false);
      
      TableView<ClassDataDebtStatus>tblDebtStatus = new TableView();
      TableColumn<ClassDataDebtStatus,String>statusName = new TableColumn("Status Pinjaman");
      statusName.setMinWidth(160);
      statusName.setCellValueFactory(cellData->cellData.getValue().statusNameProperty());
      tblDebtStatus.getColumns().addAll(statusName);
      ObservableList<ClassDataDebtStatus>listDebtStatusItems = FXCollections.observableArrayList(loadAllDataDebtStatus());
      cbpDebtStatus = new JFXCComboBoxTablePopup(ClassDataDebtStatus.class,tblDebtStatus,listDebtStatusItems,"","Status",true,400,300);
      cbpDebtStatus.setLabelFloat(false);
      
      ancCbpEmployee.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpEmployee,0.0);
      AnchorPane.setLeftAnchor(cbpEmployee,0.0);
      AnchorPane.setRightAnchor(cbpEmployee,0.0);
      AnchorPane.setTopAnchor(cbpEmployee,0.0);
      ancCbpEmployee.getChildren().add(cbpEmployee);
      
      ancDebtStatus.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpDebtStatus,0.0);
      AnchorPane.setLeftAnchor(cbpDebtStatus,0.0);
      AnchorPane.setRightAnchor(cbpDebtStatus,0.0);
      AnchorPane.setTopAnchor(cbpDebtStatus,0.0);
      ancDebtStatus.getChildren().add(cbpDebtStatus);
    }
    
    private List<TblEmployee>loadAllDataEmployee(){
      return parentController.getFReportManager().getAllDataEmployee();
    }
    
    private List<ClassDataDebtStatus>loadAllDataDebtStatus(){
       List<ClassDataDebtStatus>list = new ArrayList();
       ClassDataDebtStatus debtStatusApproved = new ClassDataDebtStatus();
       debtStatusApproved.setIdStatus(1);
       debtStatusApproved.setStatusName("Disetujui");
       
       ClassDataDebtStatus debtStatusReject = new ClassDataDebtStatus();
       debtStatusReject.setIdStatus(2);
       debtStatusReject.setStatusName("Ditolak");
       
       ClassDataDebtStatus debtStatusCreate = new ClassDataDebtStatus();
       debtStatusCreate.setIdStatus(3);
       debtStatusCreate.setStatusName("Dibuat");
       
       list.add(debtStatusCreate);
       list.add(debtStatusApproved);
       list.add(debtStatusReject);
       
       return list;
    }
    
    private SwingNode printReportEmployee(List<TblCalendarEmployeeDebt>list,Date startDate,Date endDate){
      List<ClassPrintLaporanHutangKaryawan>listLaporanHutangKaryawan = new ArrayList();
      ClassPrintLaporanHutangKaryawan reportEmployeeDebt= new ClassPrintLaporanHutangKaryawan();
      reportEmployeeDebt.setCreatedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      reportEmployeeDebt.setPeriode(startDate!=null && endDate!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(startDate)+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(endDate) : "-");
      List<ClassPrintLaporanHutangKaryawanDetail>listLaporanHutangKaryawanDetail  = new ArrayList();
      for(TblCalendarEmployeeDebt getEmployeeDebt : list){
         ClassPrintLaporanHutangKaryawanDetail reportEmployeeDebtDetail = new ClassPrintLaporanHutangKaryawanDetail();
         reportEmployeeDebtDetail.setIdKaryawan(getEmployeeDebt.getTblEmployeeByIdemployee().getCodeEmployee());
         reportEmployeeDebtDetail.setJabatanKaryawan(getEmployeeDebt.getTblEmployeeByIdemployee().getTblJob().getJobName());
         reportEmployeeDebtDetail.setNamaKaryawan(getEmployeeDebt.getTblEmployeeByIdemployee().getTblPeople().getFullName());
         reportEmployeeDebtDetail.setNominalPinjaman(getEmployeeDebt.getEmployeeDebtNominal());
         reportEmployeeDebtDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(new java.sql.Date(getEmployeeDebt.getCreatedDate().getTime())));
       //  reportEmployeeDebtDetail.setTanggalPinjam(new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(getEmployeeDebt.getSysCalenda));
         reportEmployeeDebtDetail.setTipeKaryawan(getEmployeeDebt.getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName());
         reportEmployeeDebtDetail.setStatusPinjaman(getEmployeeDebt.getEmployeeDebtStatus());
         listLaporanHutangKaryawanDetail.add(reportEmployeeDebtDetail);
       }
      reportEmployeeDebt.setListLaporanHutangKaryawanDetail(listLaporanHutangKaryawanDetail);
      listLaporanHutangKaryawan.add(reportEmployeeDebt);
      return ClassPrinter.printLaporanHutangKaryawan(listLaporanHutangKaryawan);
    }
    
    private void reportEmployeeDebtPrintHandle(){
      List<TblCalendarEmployeeDebt>list = new ArrayList();
      TblEmployee employee = null;
      ClassDataDebtStatus debtStatus = null;
      Date startDate = null;
      Date endDate = null;
      SwingNode swingNode = null;
      
       if(dpStartPeriode.getValue()!=null && dpEndPeriode.getValue()!=null){
         startDate = Date.valueOf(dpStartPeriode.getValue());
         endDate = Date.valueOf(dpEndPeriode.getValue());
       }
       
       if(cbpEmployee.getValue()!=null){
          employee = cbpEmployee.getValue();
       }
       
       if(cbpDebtStatus.getValue()!=null){
           debtStatus = cbpDebtStatus.getValue();
       }
       
       list = parentController.getFReportManager().getAllDataDebt(employee, startDate, endDate, debtStatus);
       swingNode = printReportEmployee(list,startDate,endDate);
       printReporyEmployeeLayout(swingNode);
    }
    
    private void printReporyEmployeeLayout(SwingNode swingNode){
      ancShowReportEmployeeDebt.getChildren().clear();
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode,15.0);
      AnchorPane.setTopAnchor(swingNode,15.0);
      ancShowReportEmployeeDebt.getChildren().add(swingNode);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormReportEmployee();
     // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReportEmployeeDebtController(FeatureReportEmployeeController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureReportEmployeeController parentController;
}
