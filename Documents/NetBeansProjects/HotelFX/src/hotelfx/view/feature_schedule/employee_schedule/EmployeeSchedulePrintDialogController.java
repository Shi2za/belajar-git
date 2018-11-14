/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_schedule.employee_schedule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintSchedule;
import hotelfx.helper.PrintModel.ClassPrintScheduleDetail;
import javafx.scene.paint.Color;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployeeSchedule;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.util.Callback;

/**
 *
 * @author Andreas
 */
public class EmployeeSchedulePrintDialogController implements Initializable{
    @FXML
    private AnchorPane ancEmployeeSchedulePrintDialog;
    @FXML
    private AnchorPane ancEmployeeType;
    @FXML
    private AnchorPane ancSchedule;
    @FXML
    private GridPane gpEmployeeSchedulePrintDialog;
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnViewSchedule;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private JFXButton btnClose;
    
    private JFXCComboBoxTablePopup<RefEmployeeType>cbpEmployeeType;
    private JFXCComboBoxTablePopup<TblEmployeeSchedule>cbpEmployeeSchedule;
    TableView<TblCalendarEmployeeSchedule>tblEmployeeSchedulePrintDialog  = new TableView();
    
    private void initTableEmployeeSchedulePrintDialog(){
       //setTableEmployeeSchedulePrint();
      // SwingNode swingNode=  
      
    }
    
    /*private void setTableEmployeeSchedulePrint(){
       TableView<TblCalendarEmployeeSchedule>tableView  = new TableView();
       TableColumn<TblCalendarEmployeeSchedule,String>dateSchedule = new TableColumn("Tanggal");
       dateSchedule.setMinWidth(100);
       dateSchedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
        -> Bindings.createStringBinding(()-> new SimpleDateFormat("EEEE,dd MMMM yyyy").format(param.getValue().getSysCalendar().getCalendarDate()),param.getValue().getSysCalendar().calendarDateProperty()));
       TableColumn<TblCalendarEmployeeSchedule,String>idEmployee = new TableColumn("ID");
       idEmployee.setMinWidth(90);
       idEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
       -> Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee()==null?"-":param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(), param.getValue().tblEmployeeByIdemployeeProperty()));
        TableColumn<TblCalendarEmployeeSchedule,String>nameEmployee = new TableColumn("Nama");
        nameEmployee.setMinWidth(160);
       nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
       -> Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee()==null?"-" : param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(), param.getValue().tblEmployeeByIdemployeeProperty()));
       TableColumn<TblCalendarEmployeeSchedule,String>typeEmployee = new TableColumn("Tipe");
       typeEmployee.setMinWidth(120);
       typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
       -> Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee()==null ? "-" : param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(), param.getValue().tblEmployeeByIdemployeeProperty())); 
        TableColumn<TblCalendarEmployeeSchedule,String>jobEmployee = new TableColumn("Jabatan");
        jobEmployee.setMinWidth(120);
       jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
       -> Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee()==null ? "-" : param.getValue().getTblEmployeeByIdemployee().getTblJob().getJobName(), param.getValue().tblEmployeeByIdemployeeProperty()));
        TableColumn<TblCalendarEmployeeSchedule,String>employee = new TableColumn("Karyawan");
       employee.getColumns().addAll(idEmployee,nameEmployee,typeEmployee,jobEmployee);
       TableColumn<TblCalendarEmployeeSchedule,String>schedule = new TableColumn("Jadwal");
       schedule.setMinWidth(160);
        schedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
       -> Bindings.createStringBinding(()->param.getValue().getTblEmployeeSchedule()==null ? "-" : param.getValue().getTblEmployeeSchedule().getScheduleName()+"\n"+param.getValue().getTblEmployeeSchedule().getBeginTime()+"-"+param.getValue().getTblEmployeeSchedule().getEndTime(), param.getValue().tblEmployeeScheduleProperty()));
       
       tableView.getColumns().addAll(dateSchedule,schedule,employee);
       
        
        tblEmployeeSchedulePrintDialog = tableView;
    }*/
    
    private List<SysCalendar>loadAllDataEmployeeSchedulePrint(Date startDate,Date endDate){
       List<SysCalendar>listCalendar = new ArrayList();
       List<TblCalendarEmployeeSchedule>listSchedule = new ArrayList();
       listCalendar = parentController.getService().getAllCalendar(startDate, endDate,null);
      
       return listCalendar;
    }
    
    private void initForm(){
       initDataPopupLayout();
       btnClose.setOnAction((e)->{
           parentController.dialogStagePrint.close();
       });
       
       btnPrint.setOnAction((e)->{
           if(checkDataInput()){
              employeeSchedulePrintHandle();
           }
           else{
             ClassMessage.showWarningInputDataMessage(errDataInput,parentController.dialogStagePrint);
           }
       });
       
       initFieldColor();
    }
    
    private void initFieldColor(){
       ClassViewSetting.setImportantField(dpStartDate,dpEndDate,cbpEmployeeType,cbpEmployeeSchedule);
    }
    
    private void initDataPopupLayout(){
      initDataPopup();
      
      ancEmployeeType.getChildren().clear();
      AnchorPane.setTopAnchor(cbpEmployeeType,0.0);
      AnchorPane.setBottomAnchor(cbpEmployeeType,0.0);
      AnchorPane.setLeftAnchor(cbpEmployeeType,0.0);
      AnchorPane.setRightAnchor(cbpEmployeeType,0.0);
      ancEmployeeType.getChildren().add(cbpEmployeeType);
      
      ancSchedule.getChildren().clear();
      AnchorPane.setTopAnchor(cbpEmployeeSchedule,0.0);
      AnchorPane.setBottomAnchor(cbpEmployeeSchedule,0.0);
      AnchorPane.setLeftAnchor(cbpEmployeeSchedule,0.0);
      AnchorPane.setRightAnchor(cbpEmployeeSchedule,0.0);
      ancSchedule.getChildren().add(cbpEmployeeSchedule);
    }
    
    private void initDataPopup(){
      TableView<RefEmployeeType>tableEmployeeType = new TableView();
      TableColumn<RefEmployeeType,String>employeeType = new TableColumn("Tipe Karyawan");
      employeeType.setCellValueFactory(cellData->cellData.getValue().typeNameProperty());
      tableEmployeeType.getColumns().addAll(employeeType);
      ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllDataEmployeeType());
      
      TableView<TblEmployeeSchedule>tableSchedule = new TableView();
      TableColumn<TblEmployeeSchedule,String>employeeSchedule = new TableColumn("Jadwal");
      employeeSchedule.setCellValueFactory(cellData->cellData.getValue().scheduleNameProperty());
      tableSchedule.getColumns().addAll(employeeSchedule);
      ObservableList<TblEmployeeSchedule>scheduleItems = FXCollections.observableArrayList(loadAllDataSchedule());
      
      
      cbpEmployeeType = new JFXCComboBoxTablePopup(RefEmployeeType.class,tableEmployeeType,employeeTypeItems,"","Tipe Karyawan *",false,400,300);
      cbpEmployeeType.setLabelFloat(false);     
      cbpEmployeeSchedule = new JFXCComboBoxTablePopup(TblEmployeeSchedule.class,tableSchedule,scheduleItems,"","Jadwal *",true,500,300);
      cbpEmployeeSchedule.setLabelFloat(false);
      
    }
    
    private List<RefEmployeeType>loadAllDataEmployeeType(){
       List<RefEmployeeType>list = new ArrayList();
       RefEmployeeType typeAllEmployee = new RefEmployeeType();
       typeAllEmployee.setIdtype(3);
       typeAllEmployee.setTypeName("Semua Tipe Karyawan");
       list.add(typeAllEmployee);
       list.addAll(parentController.getService().getAllDataEmployeeType());
       
       return list;
    }
    
    private List<TblEmployeeSchedule>loadAllDataSchedule(){
       List<TblEmployeeSchedule>list = new ArrayList();
       TblEmployeeSchedule typeAllSchedule = new TblEmployeeSchedule();
       typeAllSchedule.setIdschedule(0);
       typeAllSchedule.setScheduleName("Semua Jadwal");
       list.add(typeAllSchedule);
       list.addAll(parentController.getService().getAllDataSchedule());
       
       return list;
    }
    
    private void refreshDataPopup(){
      ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllDataEmployeeType());
      cbpEmployeeType.setItems(employeeTypeItems);
      
      ObservableList<TblEmployeeSchedule>scheduleItems = FXCollections.observableArrayList(loadAllDataSchedule());
      cbpEmployeeSchedule.setItems(scheduleItems);
    }
    
    private void setSelectedDataToPrintDefault(){
      refreshDataPopup();
      
      RefEmployeeType typeAllEmployee = new RefEmployeeType();
      typeAllEmployee.setIdtype(3);
      typeAllEmployee.setTypeName("Semua Tipe Karyawan");
      cbpEmployeeType.setValue(typeAllEmployee);
      
      TblEmployeeSchedule typeAllSchedule = new TblEmployeeSchedule();
      typeAllSchedule.setIdschedule(0);
      typeAllSchedule.setScheduleName("Semua Jadwal");
      cbpEmployeeSchedule.setValue(typeAllSchedule);
      
      dpStartDate.setValue(LocalDate.now());
      dpEndDate.setValue(LocalDate.now().plusDays(1));
    }
    
    private void employeeSchedulePrintDefaultHandle(){
       setSelectedDataToPrintDefault();
       employeeSchedulePrintHandle();
    }
    
    private void employeeSchedulePrintHandle(){
       SwingNode swingNode = setEmployeeSchedulePrint(Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()),cbpEmployeeType.getValue(),cbpEmployeeSchedule.getValue());
       ancEmployeeSchedulePrintDialog.getChildren().clear();
       AnchorPane.setTopAnchor(swingNode,0.0);
       AnchorPane.setBottomAnchor(swingNode,15.0);
       AnchorPane.setLeftAnchor(swingNode, 15.0);
       AnchorPane.setRightAnchor(swingNode, 15.0);
       
       ancEmployeeSchedulePrintDialog.getChildren().add(swingNode); 
    }
    private SwingNode setEmployeeSchedulePrint(Date startDate,Date endDate,RefEmployeeType employeeType,TblEmployeeSchedule employeeSchedule){
       SwingNode swingNode = new SwingNode();
       List<ClassPrintSchedule>listPrintSchedule = new ArrayList();
       List<SysCalendar>listCalendar = parentController.getService().getAllCalendar(startDate, endDate,null);
       //List<TblCalendarEmployeeSchedule>listEmployeeSchedule  = parentController.getService().getAllEmployeeScheduleByTypeEmployeeAndCalendarOneMoreDay(startDate, endDate, employeeType);
       //List<SysCalendar>listCalendar = parentController.getService().getAllCalendar(startDate,endDate,null);
      
       for(SysCalendar getCalendar : listCalendar){
           ClassPrintSchedule printSchedule = new ClassPrintSchedule();
           printSchedule.setJadwalKerja(new SimpleDateFormat("EEEE,dd MMMM yyyy").format(getCalendar.getCalendarDate()));
           List<TblCalendarEmployeeSchedule>listDetailSchedule = parentController.getService().getAllEmployeeSchedulePrint(getCalendar, employeeType,employeeSchedule);
           List<ClassPrintScheduleDetail>listPrintScheduleDetail = new ArrayList();
           for(TblCalendarEmployeeSchedule getDetailSchedule : listDetailSchedule){
             ClassPrintScheduleDetail detailPrintSchedule = new ClassPrintScheduleDetail();
             detailPrintSchedule.setIdKaryawan(getDetailSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
             detailPrintSchedule.setNamaKaryawan(getDetailSchedule.getTblEmployeeByIdemployee().getTblPeople().getFullName());
             detailPrintSchedule.setJabatanKaryawan(getDetailSchedule.getTblEmployeeByIdemployee().getTblJob().getJobName());
             detailPrintSchedule.setJadwalKaryawan(getDetailSchedule.getTblEmployeeSchedule().getScheduleName());
             detailPrintSchedule.setJamMasuk(new SimpleDateFormat("HH:mm").format(getDetailSchedule.getTblEmployeeSchedule().getBeginTime()));
             detailPrintSchedule.setJamKeluar(new SimpleDateFormat("HH:mm").format(getDetailSchedule.getTblEmployeeSchedule().getEndTime()));
             detailPrintSchedule.setTipeKaryawan(getDetailSchedule.getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName());
             listPrintScheduleDetail.add(detailPrintSchedule);
           }
           printSchedule.setDetailJadwal(listPrintScheduleDetail);
           if(printSchedule.getDetailJadwal().size()>0){
             listPrintSchedule.add(printSchedule);
           }
          
        }
         
           
           /*for(TblCalendarEmployeeSchedule getScheduleDetail : listScheduleDetail){
               List<ClassPrintScheduleDetail>listPrintDetailSchedule = new ArrayList(); 
               if(getScheduleDetail.getSysCalendar().getCalendarDate().equals(getCalendar.getCalendarDate())){
                 ClassPrintSchedule printSchedule = new ClassPrintSchedule();
                 printSchedule.setJadwalKerja(getCalendar.getCalendarDate().toString());
                 ClassPrintScheduleDetail printDetailSchedule = new ClassPrintScheduleDetail();
                 printDetailSchedule.setIdKaryawan(getScheduleDetail.getTblEmployeeByIdemployee().getCodeEmployee());
                 printDetailSchedule.setNamaKaryawan(getScheduleDetail.getTblEmployeeByIdemployee().getTblPeople().getFullName());
                 listPrintDetailSchedule.add(printDetailSchedule);
                 printSchedule.setDetailJadwal(listPrintDetailSchedule);
                 listPrintSchedule.add(printSchedule);
                }
              
            }*/
        
       swingNode = ClassPrinter.printSchedule(startDate, endDate,listPrintSchedule);
       return swingNode;
    }
    
     private String errDataInput;
    private boolean checkDataInput(){
       errDataInput = "";
       boolean check = true;
       if(dpStartDate.getValue()==null){
          errDataInput+="Periode Awal :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(dpEndDate.getValue()==null){
          errDataInput+="Periode Akhir :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(cbpEmployeeType.getValue()==null){
          errDataInput+="Tipe Karyawan :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(cbpEmployeeSchedule.getValue()==null){
          errDataInput+="Jadwal :" + ClassMessage.defaultErrorNullValueMessage+"\n";
       }
       return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      //initTableEmployeeSchedulePrintDialog();
      initForm();
      employeeSchedulePrintDefaultHandle();
      // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public EmployeeSchedulePrintDialogController(EmployeeScheduleController parentController){
       this.parentController = parentController;
    }
    
    private final EmployeeScheduleController parentController;
}
