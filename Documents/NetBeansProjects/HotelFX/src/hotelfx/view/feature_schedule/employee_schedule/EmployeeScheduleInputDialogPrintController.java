/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_schedule.employee_schedule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintSchedule;
import hotelfx.helper.PrintModel.ClassPrintScheduleDetail;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployeeSchedule;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class EmployeeScheduleInputDialogPrintController implements Initializable{
    @FXML
    private AnchorPane ancEmployeeType;
    @FXML
    private AnchorPane ancEmployeeSchedule;
    @FXML
    private AnchorPane ancPrintPreview;
    @FXML
    private Label lblPeriodeSchedule;
    @FXML
    private JFXButton btnPrintPreview;
    @FXML
    private JFXButton btnCancel;
    
    private JFXCComboBoxTablePopup<RefEmployeeType>cbpEmployeeType;
    private JFXCComboBoxTablePopup<TblEmployeeSchedule>cbpEmployeeSchedule;
    
    private void initForm(){
      initDataPopupLayout();
     
      btnCancel.setOnAction((e)->{
         employeeScheduleDialogPrintCancelHandle();
      });
      
      btnPrintPreview.setOnAction((e)->{
           if(checkDataInput()){
             employeeScheduleDialogPrintHandle();
           }
           else{
             ClassMessage.showWarningInputDataMessage(errDataInput,employeeScheduleDialogController.dialogStage);
           }
      });
      
      initFiled();
    }
    
    private void initDataPopupLayout(){
      initDataPopup();
      
      ancEmployeeType.getChildren().clear();
      AnchorPane.setTopAnchor(cbpEmployeeType,0.0);
      AnchorPane.setBottomAnchor(cbpEmployeeType,0.0);
      AnchorPane.setLeftAnchor(cbpEmployeeType,0.0);
      AnchorPane.setRightAnchor(cbpEmployeeType,0.0);
      ancEmployeeType.getChildren().add(cbpEmployeeType); 
      
      ancEmployeeSchedule.getChildren().clear();
      AnchorPane.setTopAnchor(cbpEmployeeSchedule,0.0);
      AnchorPane.setBottomAnchor(cbpEmployeeSchedule,0.0);
      AnchorPane.setLeftAnchor(cbpEmployeeSchedule,0.0);
      AnchorPane.setRightAnchor(cbpEmployeeSchedule,0.0);
      ancEmployeeSchedule.getChildren().add(cbpEmployeeSchedule);
    }
    
    private void initFiled(){
      ClassViewSetting.setImportantField(cbpEmployeeType,cbpEmployeeSchedule);
    }
    
    private void initDataPopup(){
      TableView<RefEmployeeType>tblEmployeeType = new TableView();
      TableColumn<RefEmployeeType,String>employeeType = new TableColumn("Tipe Karyawan");
      employeeType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
      tblEmployeeType.getColumns().addAll(employeeType);
      ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllDataEmployeeType());
      
      TableView<TblEmployeeSchedule>tblSchedule = new TableView();
      TableColumn<TblEmployeeSchedule,String>schedule = new TableColumn("Jadwal");
      schedule.setCellValueFactory(cellData -> cellData.getValue().scheduleNameProperty());
      tblSchedule.getColumns().addAll(schedule);
      ObservableList<TblEmployeeSchedule>scheduleItems = FXCollections.observableArrayList(loadAllDataSchedule());
      
      cbpEmployeeType = new JFXCComboBoxTablePopup(RefEmployeeType.class,tblEmployeeType,employeeTypeItems,"","Tipe Karyawan *",true,400,300);
      cbpEmployeeType.setLabelFloat(false);
      cbpEmployeeSchedule = new JFXCComboBoxTablePopup(TblEmployeeSchedule.class,tblSchedule,scheduleItems,"","Jadwal *",true,400,300);
      cbpEmployeeSchedule.setLabelFloat(false);
    }
    
   private List<RefEmployeeType>loadAllDataEmployeeType(){
       List<RefEmployeeType>list = new ArrayList();
       RefEmployeeType allEmployeeType = new RefEmployeeType();
       allEmployeeType.setIdtype(3);
       allEmployeeType.setTypeName("Semua Tipe Karyawan");
       list.add(allEmployeeType);
       list.addAll(employeeScheduleDialogController.employeeScheduleController.getService().getAllDataEmployeeType());
       
       return list;
    }
    
   private List<TblEmployeeSchedule>loadAllDataSchedule(){
      List<TblEmployeeSchedule>list = new ArrayList();
      TblEmployeeSchedule allSchedule = new TblEmployeeSchedule();
      allSchedule.setIdschedule(0);
      allSchedule.setScheduleName("Semua Jadwal");
      list.add(allSchedule);
      list.addAll(employeeScheduleDialogController.employeeScheduleController.getService().getAllDataSchedule());
      
      return list;
   }
   
   private void refreshDataPopup(){
     ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllDataEmployeeType());
     cbpEmployeeType.setItems(employeeTypeItems);
     
     ObservableList<TblEmployeeSchedule>employeeScheduleItems = FXCollections.observableArrayList(loadAllDataSchedule());
     cbpEmployeeSchedule.setItems(employeeScheduleItems);
   }
   
   private void setSelectedDataToPrintDefault(){
     refreshDataPopup();
     
     RefEmployeeType allEmployeeType = new RefEmployeeType();
     allEmployeeType.setIdtype(3);
     allEmployeeType.setTypeName("Semua Tipe Karyawan");
     cbpEmployeeType.setValue(allEmployeeType);
       
     TblEmployeeSchedule allSchedule = new TblEmployeeSchedule();
     allSchedule.setIdschedule(0);
     allSchedule.setScheduleName("Semua Jadwal");
     cbpEmployeeSchedule.setValue(allSchedule);
   }
   
   private void employeeSchedulePrintDefault(){
      lblPeriodeSchedule.setText(new SimpleDateFormat("dd MMMM yyyy",new java.util.Locale("id")).format(employeeScheduleDialogController.employeeScheduleController.selectedData.getSysCalendar().getCalendarDate()));
      setSelectedDataToPrintDefault();
      employeeScheduleDialogPrintHandle();
   }
   
   private SwingNode setDataPrintEmployeeSchedule(){
     List<TblCalendarEmployeeSchedule>list = employeeScheduleDialogController.employeeScheduleController.getService().getAllEmployeeSchedulePrint(employeeScheduleDialogController.employeeScheduleController.selectedData.getSysCalendar(),cbpEmployeeType.getValue(),cbpEmployeeSchedule.getValue());
     List<ClassPrintSchedule>listPrintSchedule = new ArrayList();
     List<ClassPrintScheduleDetail>listDetailPrintSchedule = new ArrayList();
     ClassPrintSchedule printSchedule = new ClassPrintSchedule();
       printSchedule.setJadwalKerja(new SimpleDateFormat("EEEE,dd MMMM yyyy",new java.util.Locale("id")).format(employeeScheduleDialogController.employeeScheduleController.selectedData.getSysCalendar().getCalendarDate()));
       for(TblCalendarEmployeeSchedule getSchedule : list){
          ClassPrintScheduleDetail printScheduleDetail = new ClassPrintScheduleDetail();
          printScheduleDetail.setIdKaryawan(getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
          printScheduleDetail.setJabatanKaryawan(getSchedule.getTblEmployeeByIdemployee().getTblJob().getJobName());
          printScheduleDetail.setJadwalKaryawan(getSchedule.getTblEmployeeSchedule().getScheduleName());
          printScheduleDetail.setJamKeluar(new SimpleDateFormat("HH:mm").format(getSchedule.getTblEmployeeSchedule().getEndTime()));
          printScheduleDetail.setJamMasuk(new SimpleDateFormat("HH:mm").format(getSchedule.getTblEmployeeSchedule().getBeginTime()));
          printScheduleDetail.setNamaKaryawan(getSchedule.getTblEmployeeByIdemployee().getTblPeople().getFullName());
          printScheduleDetail.setTipeKaryawan(getSchedule.getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName());
          listDetailPrintSchedule.add(printScheduleDetail);
        }
       printSchedule.setDetailJadwal(listDetailPrintSchedule);
       listPrintSchedule.add(printSchedule);
       
       return ClassPrinter.printSchedule(employeeScheduleDialogController.employeeScheduleController.selectedData.getSysCalendar().getCalendarDate(), null, listPrintSchedule);
   }
   
   private void employeeScheduleDialogPrintHandle(){
      SwingNode swingNode = setDataPrintEmployeeSchedule();
      ancPrintPreview.getChildren().clear();
      AnchorPane.setTopAnchor(swingNode,0.0);
      AnchorPane.setBottomAnchor(swingNode,15.0);
      AnchorPane.setLeftAnchor(swingNode,15.0);
      AnchorPane.setRightAnchor(swingNode, 15.0);
      ancPrintPreview.getChildren().add(swingNode);
   }
   
   private void employeeScheduleDialogPrintCancelHandle(){
      employeeScheduleDialogController.dialogStage.close();
   }
   
   String errDataInput;
   private boolean checkDataInput(){
       errDataInput = "";
       boolean check = true;
       if(cbpEmployeeType.getValue()==null){
         errDataInput+="Tipe Karyawan :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       
       if(cbpEmployeeSchedule.getValue()==null){
         errDataInput+="Jadwal :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
      return check;
   }
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initForm();
      employeeSchedulePrintDefault();  
    
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public EmployeeScheduleInputDialogPrintController(EmployeeScheduleInputDialogController employeeScheduleDialogController){
       this.employeeScheduleDialogController = employeeScheduleDialogController;
    }
    private final EmployeeScheduleInputDialogController employeeScheduleDialogController;
}
