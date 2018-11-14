/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_attendance.attendance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
//import hotelfx.helper.ClassDataSchedule;
import hotelfx.helper.ClassDataUserAccess;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefEmployeeAttendanceStatus;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeAttendance;
import hotelfx.persistence.model.TblCalendarEmployeeLeave;
import hotelfx.persistence.model.TblCalendarEmployeeOvertime;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.service.FAttendanceManager;
import hotelfx.view.feature_attendance.FeatureAttendanceController;
import hotelfx.view.feature_schedule.employee_schedule.EmployeeScheduleController;
import hotelfx.view.feature_schedule.employee_schedule.EmployeeScheduleInputDialogController;
import hotelfx.view.feature_user_access.user_access.UserAccessController;
import insidefx.undecorator.Undecorator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Andreas
 */

public class AttendanceController implements Initializable{
    
    @FXML
    private AnchorPane tableAttendanceLayout;
    
    private TableView tableAttendance;
    
     private final PseudoClass cellRedColor = PseudoClass.getPseudoClass("redColor");
     
      private final PseudoClass cellOrangeColor = PseudoClass.getPseudoClass("orangeColor");
    private void initTableAttendance(){
       setTableAttendance();
       tableAttendanceLayout.getChildren().clear();
       AnchorPane.setTopAnchor(tableAttendance,15.0);
       AnchorPane.setBottomAnchor(tableAttendance, 15.0);
       AnchorPane.setLeftAnchor(tableAttendance, 15.0);
       AnchorPane.setRightAnchor(tableAttendance, 15.0);
       tableAttendanceLayout.getChildren().add(tableAttendance);
    }
    
    private void setTableAttendance(){
      TableView<TblCalendarEmployeeAttendance>tblAttendance = new TableView();
      tblAttendance.setEditable(true);
      TableColumn<TblCalendarEmployeeAttendance,String>date = new TableColumn("Tanggal \n Absen");
      date.setMinWidth(90);
      date.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getSysCalendar().getCalendarDate()),param.getValue().getSysCalendar().calendarDateProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>codeFingerPrint = new TableColumn("Finger \n Print");
      codeFingerPrint.setMinWidth(60);
      codeFingerPrint.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getCodeFingerPrint(),param.getValue().getTblEmployeeByIdemployee().codeFingerPrintProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>employeeCode = new TableColumn("ID");
      employeeCode.setMinWidth(90);
      employeeCode.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(),param.getValue().getTblEmployeeByIdemployee().codeEmployeeProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>employeeType = new TableColumn("Tipe");
      employeeType.setMinWidth(100);
      employeeType.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(),param.getValue().getTblEmployeeByIdemployee().refEmployeeTypeProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>employeeName = new TableColumn("Nama");
      employeeName.setMinWidth(140);
      employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(),param.getValue().getTblEmployeeByIdemployee().getTblPeople().fullNameProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>employee = new TableColumn("Karyawan");
     employee.getColumns().addAll(employeeCode,employeeType,employeeName);
      /*employee.setMinWidth(140);
      employee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->"ID :"+param.getValue().getTblEmployeeByIdemployee().getCodeEmployee()+"\n"+
          "Nama :"+param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName()+"\n"
          +"Tipe :"+param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(),param.getValue().getTblEmployeeByIdemployee().getTblPeople().fullNameProperty()));*/
      
      TableColumn<TblCalendarEmployeeAttendance,String>startTimeRealWork = new TableColumn("Masuk");
      startTimeRealWork.setMinWidth(140);
      startTimeRealWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getBeginReal()!=null?(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(param.getValue().getBeginReal())):"-",param.getValue().beginRealProperty()));
       Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>cellStartRealFactory = 
       new Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>(){
          @Override
          public TableCell call(TableColumn p) {
            return new RealStartAttendanceCell();
          } 
       };  
      startTimeRealWork.setCellFactory(cellStartRealFactory);
      startTimeRealWork.setEditable(true);
      TableColumn<TblCalendarEmployeeAttendance,String>endTimeRealWork = new TableColumn("Keluar");
      endTimeRealWork.setMinWidth(140);
        endTimeRealWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getEndReal()!=null?(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(param.getValue().getEndReal())):"-",param.getValue().endRealProperty()));
       Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>cellEndRealFactory = 
       new Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>(){
          @Override
          public TableCell call(TableColumn p) {
            return new RealEndAttendanceCell();
          } 
       };  
      endTimeRealWork.setCellFactory(cellEndRealFactory);
      endTimeRealWork.setEditable(true);
        TableColumn<TblCalendarEmployeeAttendance,String>timeWorkReal = new TableColumn("Jam Real");
          timeWorkReal.getColumns().addAll(startTimeRealWork,endTimeRealWork);
      TableColumn<TblCalendarEmployeeAttendance,String>startTimeSchedule = new TableColumn("Masuk");
      startTimeSchedule.setMinWidth(70);
      startTimeSchedule.setMaxWidth(70);
      startTimeSchedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getBeginSchedule()!=null && param.getValue().getIsOvertime()==false?param.getValue().getBeginSchedule().toString():"-",param.getValue().beginScheduleProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>endTimeSchedule = new TableColumn("Keluar");
      endTimeSchedule.setMinWidth(70);
      endTimeSchedule.setMaxWidth(70);
      endTimeSchedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getEndSchedule()!=null && param.getValue().getIsOvertime()==false?param.getValue().getEndSchedule().toString():"-",param.getValue().endScheduleProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>timeWorkSchedule = new TableColumn("Jam Kerja");
       timeWorkSchedule.getColumns().addAll(startTimeSchedule,endTimeSchedule);
       TableColumn<TblCalendarEmployeeAttendance,String>startTimeScheduleOvertime = new TableColumn("Masuk");
       startTimeScheduleOvertime.setMinWidth(70);
       startTimeScheduleOvertime.setMaxWidth(70);
      startTimeScheduleOvertime.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getBeginSchedule()!=null && param.getValue().getIsOvertime()==true?param.getValue().getBeginSchedule().toString():"-",param.getValue().beginScheduleProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>endTimeScheduleOvertime = new TableColumn("Keluar");
      endTimeScheduleOvertime.setMinWidth(70);
      endTimeScheduleOvertime.setMaxWidth(70);
      endTimeScheduleOvertime.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getEndSchedule()!=null && param.getValue().getIsOvertime()==true?param.getValue().getEndSchedule().toString():"-",param.getValue().endScheduleProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>timeWorkScheduleOvertime = new TableColumn("Jam Lembur");
       timeWorkScheduleOvertime.getColumns().addAll(startTimeScheduleOvertime,endTimeScheduleOvertime);
      TableColumn<TblCalendarEmployeeAttendance,String>statusStartTimeWork = new TableColumn("Masuk");
      statusStartTimeWork.setMinWidth(100);
      statusStartTimeWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeAttendanceStartStatus()==null?"-":param.getValue().getRefEmployeeAttendanceStartStatus().getStatusName(),param.getValue().refEmployeeAttendanceStartStatusProperty()));
       TableColumn<TblCalendarEmployeeAttendance,String>statusEndTimeWork = new TableColumn("Keluar");
       statusEndTimeWork.setMinWidth(100);
      statusEndTimeWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeAttendanceEndStatus()==null?"-":param.getValue().getRefEmployeeAttendanceEndStatus().getStatusName(),param.getValue().refEmployeeAttendanceEndStatusProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>statusAttendance = new TableColumn("Kehadiran");
      statusAttendance.setMinWidth(100);
      statusAttendance.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeAttendanceStatus().getStatusName(),param.getValue().getRefEmployeeAttendanceStatus().statusNameProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>statusEmployeeAttendance = new TableColumn("Status Absen");
      statusEmployeeAttendance.getColumns().addAll(statusStartTimeWork,statusEndTimeWork,statusAttendance);
      Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>cellFactory = 
       new Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>(){
          @Override
          public TableCell call(TableColumn p) {
              return new StatusAttendanceCell();
          } 
       };  
      statusAttendance.setCellFactory(cellFactory);
      statusAttendance.setEditable(true);
      /*TableColumn<TblCalendarEmployeeAttendance,String>statusAttendance = new TableColumn("Status Kehadiran");
      statusAttendance.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeAttendanceStatus().getStatusName(),param.getValue().getRefEmployeeAttendanceStatus().statusNameProperty()));
      Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>cellFactory = 
       new Callback<TableColumn<TblCalendarEmployeeAttendance,String>,TableCell<TblCalendarEmployeeAttendance,String>>(){
          @Override
          public TableCell call(TableColumn p) {
              return new StatusAttendanceCell();
          }
           
       };  
      statusAttendance.setCellFactory(cellFactory)*/;
      //isOverTime.setCellValueFactory(cellData->cellData.getValue().isOvertimeProperty());
      
      tblAttendance.getColumns().addAll(date,codeFingerPrint,employee,timeWorkSchedule,timeWorkScheduleOvertime,timeWorkReal,statusAttendance);
      
      tableAttendance = tblAttendance;
    }
    
    private JFXCComboBoxPopup<RefEmployeeAttendanceStatus>getAttendanceStatus(TblCalendarEmployeeAttendance employeeAttendance){
       JFXCComboBoxPopup<RefEmployeeAttendanceStatus>cbpAttendanceStatus = new JFXCComboBoxPopup<>(RefEmployeeAttendanceStatus.class);
       TableView<RefEmployeeAttendanceStatus> tblAttendanceStatus = new TableView();
       TableColumn<RefEmployeeAttendanceStatus,String>nameAttendance = new TableColumn("Status");
       nameAttendance.setCellValueFactory(cellData->cellData.getValue().statusNameProperty());
      
       tblAttendanceStatus.getColumns().add(nameAttendance);
       
       // System.out.println("hsl employeeAttendance"+employeeAttendance.getTblEmployeeByIdemployee().getCodeEmployee() + " -"+employeeAttendance.getBeginReal());
       ObservableList<RefEmployeeAttendanceStatus>listAttendanceStatus = FXCollections.observableArrayList(parentController.getFAttendanceManager().getAllAttendanceStatus());
       boolean found = false;
       for(int i = 0; i<listAttendanceStatus.size();i++){
         
          // System.out.println(employeeAttendance.getBeginReal()==null && employeeAttendance.getEndReal()==null);
            if(employeeAttendance.getBeginReal()==null && employeeAttendance.getEndReal()==null){
                if(listAttendanceStatus.get(i).getIdstatus()==2){
                  listAttendanceStatus.remove(listAttendanceStatus.get(i));
                  found = true;
                }
            } 
       }
       
       setFunctionPopup(cbpAttendanceStatus,tblAttendanceStatus,listAttendanceStatus,"statusName","Status Absen",300,200);
       cbpAttendanceStatus.setLabelFloat(false);
       return cbpAttendanceStatus;
    }
    
    private void setFunctionPopup(JFXCComboBoxPopup cbp, TableView table, ObservableList items, String nameFiltered, String promptText,double prefWidth,double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal.intValue() != -1) {
                cbp.valueProperty().set(table.getItems().get(newVal.intValue()));
            }
            cbp.hide();
        });

        cbp.setPropertyNameForFiltered(nameFiltered);
        cbp.setItems(items);
        
        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        table.itemsProperty().bind(cbp.filteredItemsProperty());

        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth,prefHeight);

        content.setCenter(table);

        cbp.setPopupEditor(true);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(true);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);

        cbp.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

    }
    
    public class StatusAttendanceCell extends TableCell<RefEmployeeAttendanceStatus,String> {

        private JFXCComboBoxPopup<RefEmployeeAttendanceStatus>cbpAttendanceStatus;
       
        public StatusAttendanceCell(){
          
        }

     @Override
     public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                cbpAttendanceStatus = getAttendanceStatus((TblCalendarEmployeeAttendance)this.getTableRow().getItem());
                cbpAttendanceStatus.hide();
                cbpAttendanceStatus.setMinWidth(this.getWidth()-this.getGraphicTextGap()*2);
                cbpAttendanceStatus.valueProperty().bindBidirectional(((TblCalendarEmployeeAttendance)this.getTableRow().getItem()).refEmployeeAttendanceStatusProperty());
                setText(null);
                setGraphic(cbpAttendanceStatus);
                
                cbpAttendanceStatus.getEditor().selectAll();
                
                if(this.getTableRow()!=null){
                   getStyleClass().remove("cell-input");
                   getStyleClass().add("cell-input-edit");
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            cbpAttendanceStatus.valueProperty().unbindBidirectional(((TblCalendarEmployeeAttendance)this.getTableRow().getItem()).refEmployeeAttendanceStatusProperty());
            setText((String)cbpAttendanceStatus.getValue().getStatusName());
            
            setGraphic(null);
            
               if(this.getTableRow()!=null){
                   getStyleClass().remove("cell-input-edit");
                   getStyleClass().add("cell-input");
                }
        }
        
         @Override
     public void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
            if (!empty) {
              if(!isEditing()){
                if(this.getTableRow()!=null){
                  Object data = this.getTableRow().getItem();
                   if(data!=null){
                     if(((TblCalendarEmployeeAttendance)data).getRefEmployeeAttendanceStatus()!=null){  
                        //textProperty().bindBidirectional(new SimpleStringProperty(((TblCalendarEmployeeAttendance)data).getRefEmployeeAttendanceStatus().getStatusName()));
                         setText(((TblCalendarEmployeeAttendance)data).getRefEmployeeAttendanceStatus().getStatusName());
                       }
                       else{
                         setText("");
                       }
                       setGraphic(null);
                       //RefEmployeeAttendanceStatus attendanceStatus =  getTypeAttendance((java.sql.Date)((TblCalendarEmployeeAttendance)data).getSysCalendar().getCalendarDate(),((TblCalendarEmployeeAttendance)data).getTblEmployeeByIdemployee().getIdemployee());
                      
                   }
                   else{
                     setText(null);
                     setGraphic(null);
                   }
                   
                   
                   if(this.getTableRow()!=null){
                      getStyleClass().add("cell-input");
                    }
                }
                else{
                  setText(null);
                  setGraphic(null);
                }
              }
              else{
                setText(null);
                setGraphic(null);
              }
                
            } else {
                setText(null);
                setGraphic(null); 
                getStyleClass().remove("cell-input");
            }
        }
    }
    
   public class RealStartAttendanceCell extends TableCell<RefEmployeeAttendanceStatus,String> {

       // private JFXCComboBoxPopup<RefEmployeeAttendanceStatus>cbpAttendanceStatus;
       
        public RealStartAttendanceCell(){
          
        }

    /* @Override
     public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                cbpAttendanceStatus = getAttendanceStatus((TblCalendarEmployeeAttendance)this.getTableRow().getItem());
                cbpAttendanceStatus.hide();
                cbpAttendanceStatus.setMinWidth(this.getWidth()-this.getGraphicTextGap()*2);
                cbpAttendanceStatus.valueProperty().bindBidirectional(((TblCalendarEmployeeAttendance)this.getTableRow().getItem()).refEmployeeAttendanceStatusProperty());
                setText(null);
                setGraphic(cbpAttendanceStatus);
                
                cbpAttendanceStatus.getEditor().selectAll();
                
                if(this.getTableRow()!=null){
                   getStyleClass().add("cell-input-odd-edit");
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            cbpAttendanceStatus.valueProperty().unbindBidirectional(((TblCalendarEmployeeAttendance)this.getTableRow().getItem()).refEmployeeAttendanceStatusProperty());
            setText((String)cbpAttendanceStatus.getValue().getStatusName());
            
            setGraphic(null);
            
               if(this.getTableRow()!=null){
                   getStyleClass().add("cell-input-odd-edit");
                }
        }*/
        
     @Override
     public void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
            if (!empty) {
               if(item!=null){
                   if(this.getTableRow()!=null){
                       setText(item);
                       TblCalendarEmployeeAttendance attendance = (TblCalendarEmployeeAttendance)this.getTableRow().getItem();
                       if(attendance!=null){
                            if(attendance.getRefEmployeeAttendanceStartStatus()!=null && !item.equalsIgnoreCase("-")){
                               if(attendance.getRefEmployeeAttendanceStartStatus().getIdstatus()==2){
                                 //System.out.println("masuk>>");
                                 setStyle("-fx-background-color: red;"
                                         + "-fx-border-color: black ;");
                                 //pseudoClassStateChanged(cellOrangeColor,true);
                                 //getStyleClass().add("cell-atttendance-late");
                                }
                               else if(attendance.getRefEmployeeAttendanceStartStatus().getIdstatus()==0){
                                   setStyle("-fx-background-color: #ffc107;"
                                         + "-fx-border-color: black ;");
                               }
                            }
                            else{
                              setStyle("");
                            }
                        }
                   }
                   else{
                     setText(null);
                     setStyle("");
                   }
                }
            } else {
                setText(null);
                setGraphic(null);
                setStyle("");
            }
        }
    }
    
     public class RealEndAttendanceCell extends TableCell<RefEmployeeAttendanceStatus,String> {

       // private JFXCComboBoxPopup<RefEmployeeAttendanceStatus>cbpAttendanceStatus;
       
        public RealEndAttendanceCell(){
          
        }

    /* @Override
     public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                cbpAttendanceStatus = getAttendanceStatus((TblCalendarEmployeeAttendance)this.getTableRow().getItem());
                cbpAttendanceStatus.hide();
                cbpAttendanceStatus.setMinWidth(this.getWidth()-this.getGraphicTextGap()*2);
                cbpAttendanceStatus.valueProperty().bindBidirectional(((TblCalendarEmployeeAttendance)this.getTableRow().getItem()).refEmployeeAttendanceStatusProperty());
                setText(null);
                setGraphic(cbpAttendanceStatus);
                
                cbpAttendanceStatus.getEditor().selectAll();
                
                if(this.getTableRow()!=null){
                   getStyleClass().add("cell-input-odd-edit");
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            cbpAttendanceStatus.valueProperty().unbindBidirectional(((TblCalendarEmployeeAttendance)this.getTableRow().getItem()).refEmployeeAttendanceStatusProperty());
            setText((String)cbpAttendanceStatus.getValue().getStatusName());
            
            setGraphic(null);
            
               if(this.getTableRow()!=null){
                   getStyleClass().add("cell-input-odd-edit");
                }
        }*/
        
         @Override
     public void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
            if (!empty) {
               if(item!=null){
                   if(this.getTableRow()!=null){
                       setText(item);
                       TblCalendarEmployeeAttendance attendance = (TblCalendarEmployeeAttendance)this.getTableRow().getItem();
                       if(attendance!=null){
                            if(attendance.getRefEmployeeAttendanceEndStatus()!=null && !item.equalsIgnoreCase("-")){
                               if(attendance.getRefEmployeeAttendanceEndStatus().getIdstatus()==2){
                                 //System.out.println("masuk>>");
                                 setStyle("-fx-background-color: #ffc107;"
                                         + "-fx-border-color: black ;");
                                 //pseudoClassStateChanged(cellOrangeColor,true);
                                 //getStyleClass().add("cell-atttendance-late");
                                }
                               else if(attendance.getRefEmployeeAttendanceEndStatus().getIdstatus()==0){
                                   setStyle("-fx-background-color: #f44336;"
                                         + "-fx-border-color: black ;");
                               }
                               else{
                                 setStyle("");
                               }
                              
                            }
                            else{
                                setStyle("");
                            }
                        }
                   }
                }
                
            } else {
                setText(null);
                setGraphic(null);
                setStyle("");
            }
        }
    }
    
    //upload file
    @FXML
    private JFXDatePicker dpStartPeriode;
    @FXML
    private JFXDatePicker dpEndPeriode;
    @FXML
    private JFXTextField txtNamePath;
    @FXML
    private JFXButton btnBrowseFile;
    @FXML
    private JFXButton btnUploadFile;
    @FXML
    private JFXButton btnSetting;
    @FXML
    private JFXButton btnSave;
    
    ObservableList<TblCalendarEmployeeAttendance>listAttendance = FXCollections.observableArrayList();
    
    private void initButtonControl(){
      txtNamePath.setText(null);
      btnSetting.setOnAction((e)->{
          showSettingFingerPrintDialog();
              });
        
      btnBrowseFile.setOnAction((e)->{
         txtNamePath.setText(browseFile());
      });
      
      btnUploadFile.setOnAction((e)->{
          if(checkDataInput()){
             listAttendance = FXCollections.observableArrayList(readFileAttendanceWithOutDate(txtNamePath.getText()));
             getTypeAttendance(listAttendance);
            // getAllEmployeeSchedule(listAttendance);
             //getAllEmployeeScheduleOvertime(listAttendance);
             tableAttendance.setItems(listAttendance);
             //set unsaving data input -> 'true'
             ClassSession.unSavingDataInput.set(true);
          }
          else{
             ClassMessage.showWarningInputDataMessage(errInput, null);
          }
         
      });
      
      btnSave.setOnAction((e)->{
            insertEmployeeAttendance(listAttendance);
      });
        initDateCalendar();
        initImportantFieldColor();
    }
    
     private void initDateCalendar(){
       ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartPeriode,dpEndPeriode);
    }
     
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dpStartPeriode,
                dpEndPeriode, 
                txtNamePath);
    }
    
    private String browseFile(){
      FileChooser fileChooser = new FileChooser();
      fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
      fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Microsoft Excel","*.xlsx"));
      File file = fileChooser.showOpenDialog(HotelFX.primaryStage);
     // path.set(file.getAbsolutePath().toString()); 
       if(file != null){
           return file.getAbsolutePath();
       }else{
           return "";
       }
    }
    
    private List<TblCalendarEmployeeAttendance>readFileAttendanceWithOutDate(String path){
        List<TblCalendarEmployeeAttendance>listAttendance = new ArrayList();
        try {
            
            ObservableList<TblCalendarEmployeeAttendance>listAttendanceExcel = FXCollections.observableArrayList();
            FileInputStream fileAttendance = new FileInputStream(new File(path));
            Workbook workBook = new XSSFWorkbook(fileAttendance);
            Sheet sheet = workBook.getSheetAt(0);
            Iterator<Row>iterator = sheet.iterator();
            
            List<SysCalendar>listCalendar = parentController.getFAttendanceManager().getSysCalendarByDate(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()),null,"no excel");
            List<TblEmployee>listEmployee = parentController.getFAttendanceManager().getAllEmployee();
            //List<ClassDataSchedule>listSchedule = parentController.getFAttendanceManager().getAllDataScheduleByPeriode(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()));
            
            while(iterator.hasNext()){
                Row nextRow = iterator.next();
                if(nextRow.getRowNum()!=0){
                  Iterator<Cell>cellIterator = nextRow.cellIterator();
                  TblCalendarEmployeeAttendance employeeAttendance = new TblCalendarEmployeeAttendance();
                
                  while(cellIterator.hasNext()){
                   Cell nextCell = cellIterator.next();
                   int columnIndex = nextCell.getColumnIndex();
                  
                  switch(columnIndex){
                       
                      case 0:
                        List<TblEmployee>listEmployeeExcel = parentController.getFAttendanceManager().getAllEmployeeByFingerPrint((String)getCellValue(nextCell));
                        for(TblEmployee getEmployee:listEmployeeExcel){
                          System.out.println("employee:"+getEmployee.getTblPeople().getFullName());
                          employeeAttendance.setTblEmployeeByIdemployee(getEmployee);
                          employeeAttendance.setCodeFingerPrint(getEmployee.getCodeFingerPrint());
                        }
                        
                     break;
                     
                      case 1:
                         if(nextCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                            if(HSSFDateUtil.isCellDateFormatted(nextCell)){
                              java.sql.Date dateExcel = new java.sql.Date(nextCell.getDateCellValue().getTime());
                              employeeAttendance.setBeginReal(dateExcel);
                            }
                        }
                        break;
                        
                      case 2:
                         if(nextCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                           if(HSSFDateUtil.isCellDateFormatted(nextCell)){
                              java.sql.Date dateExcel = new java.sql.Date(nextCell.getDateCellValue().getTime());
                              employeeAttendance.setEndReal(dateExcel);
                           } 
                         }
                        break;
                    }
                  for(Date date=(java.sql.Date)Date.valueOf(dpStartPeriode.getValue());!date.after((java.sql.Date)Date.valueOf(dpEndPeriode.getValue()));date = Date.valueOf(date.toLocalDate().plusDays(1))){
                    if(employeeAttendance.getBeginReal()!=null && employeeAttendance.getBeginReal().toString().equalsIgnoreCase(date.toString())){
                      listAttendanceExcel.add(employeeAttendance);
                    }  
                  }
                  //System.out.println("");
                  
                }
                  
               /* List<SysCalendar>listCalendarExcel = parentController.getFAttendanceManager().getSysCalendarByDate(null,null,(java.sql.Date)employeeAttendance.getBeginReal(),"excel");
                  for(SysCalendar getCalendarExcel : listCalendarExcel){
                    employeeAttendance.setSysCalendar(getCalendarExcel);
                  }*/
                  
                }
        }
        
           for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
               System.out.println("hsl excel : "+getEmployeeAttendanceExcel.getBeginReal());
           }
        /*for(TblCalendarEmployeeAttendance getEmployeeAttendance:listAttendance){
              for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
                if(getEmployeeAttendanceExcel.getBeginReal().equals(getEmployeeAttendance.getSysCalendar().getCalendarDate())){
                   if(getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()==getEmployeeAttendance.getTblEmployeeByIdemployee().getIdemployee()){
                      getEmployeeAttendance.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                      getEmployeeAttendance.setEndReal(getEmployeeAttendanceExcel.getEndReal());
                   }
                 }
              }
            }*/
              
            /*for(SysCalendar getCalendar : listCalendar){
                for(TblEmployee getEmployee : listEmployee){
                    TblCalendarEmployeeAttendance employeeAttendance = new TblCalendarEmployeeAttendance();
                    employeeAttendance.setSysCalendar(getCalendar);
                    
                    List<TblCalendarEmployeeSchedule>listEmployeeSchedule = new ArrayList();
                    //TblCalendarEmployeeSchedule employeeSchedule = new TblCalendarEmployeeSchedule();
                    getAllEmployeeScheduleWithOutDate(getCalendar,getEmployee,listEmployeeSchedule);
                    //System.out.println("employee"+getEmployee.getCodeEmployee()+" "+listEmployeeSchedule.size());
                    //TblCalendarEmployeeOvertime employeeOvertime = new TblCalendarEmployeeOvertime();
                    List<TblCalendarEmployeeOvertime>listEmployeeOvertime = new ArrayList();
                    getAllEmployeeOvertimeWithOutDate(getCalendar,getEmployee,listEmployeeOvertime);
                    //System.out.println("hsl Overtime:"+employeeOvertime.getBeginTime());
                    
                    boolean notNull = false;
                    if(!listEmployeeSchedule.isEmpty()){
                      getAllEmployeeRealWithOutDate(listEmployeeSchedule,getCalendar,getEmployee,listAttendanceExcel,listAttendance);  
                      notNull = true;
                    }
                    
                    if(!listEmployeeOvertime.isEmpty()){
                      getAllEmployeeRealOvertimeWithOutDate(listEmployeeOvertime,getCalendar,getEmployee,listAttendanceExcel,listAttendance);  
                      notNull = true;
                    }
                    
                    if(!notNull){
                      TblCalendarEmployeeAttendance newAttendance = new TblCalendarEmployeeAttendance();
                      newAttendance.setSysCalendar(getCalendar);
                      newAttendance.setTblEmployeeByIdemployee(getEmployee);
                      insertDataAttendance(newAttendance,listAttendance);
                    }
                }
            }*/
           
            for(Date date=(java.sql.Date)Date.valueOf(dpStartPeriode.getValue());!date.after((java.sql.Date)Date.valueOf(dpEndPeriode.getValue()));date = Date.valueOf(date.toLocalDate().plusDays(1))){
               for(SysCalendar getCalendar : listCalendar){
                   for(TblEmployee getEmployee : listEmployee){
                       if(date.equals(getCalendar.getCalendarDate())){
                         TblCalendarEmployeeAttendance employeeAttendanceWork = new TblCalendarEmployeeAttendance();
                         employeeAttendanceWork.setSysCalendar(getCalendar);
                         employeeAttendanceWork.setTblEmployeeByIdemployee(getEmployee);
                         getAllEmployeeScheduleWithOutDate(employeeAttendanceWork);
                         getAllEmployeeRealWithOutDate(listAttendanceExcel,employeeAttendanceWork);
                         insertDataAttendance(employeeAttendanceWork,listAttendance);
                         
                         TblCalendarEmployeeAttendance employeeAttendanceOvertime = new TblCalendarEmployeeAttendance();
                         employeeAttendanceOvertime.setSysCalendar(getCalendar);
                         employeeAttendanceOvertime.setTblEmployeeByIdemployee(getEmployee);
                         employeeAttendanceOvertime.setIsOvertime(Boolean.TRUE);
                         getAllEmployeeOvertimeWithOutDate(employeeAttendanceOvertime);
                         getAllEmployeeRealWithOutDate(listAttendanceExcel,employeeAttendanceOvertime);
                         insertDataAttendance(employeeAttendanceOvertime,listAttendance);
                        }
                    }
               }
            }
        
          workBook.close();
          fileAttendance.close();
        }catch (FileNotFoundException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return listAttendance;
    }
    
    //handle file excel
   /* private List<TblCalendarEmployeeAttendance>readFileAttendanceWithDate(String path){
        List<TblCalendarEmployeeAttendance>listAttendance = new ArrayList();
        try { 
         ObservableList<TblCalendarEmployeeAttendance>listAttendanceExcel = FXCollections.observableArrayList();
          FileInputStream fileAttendance = new FileInputStream(new File(path));
            Workbook workBook = new XSSFWorkbook(fileAttendance);
            Sheet sheet = workBook.getSheetAt(0);
            Iterator<Row>iterator = sheet.iterator();
            
            List<SysCalendar>listCalendar = parentController.getFAttendanceManager().getSysCalendarByDate(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()),null,"no excel");
            List<TblEmployee>listEmployee = parentController.getFAttendanceManager().getAllEmployee();
            //List<ClassDataSchedule>listSchedule = parentController.getFAttendanceManager().getAllDataScheduleByPeriode(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()));
            
            while(iterator.hasNext()){
                Row nextRow = iterator.next();
                if(nextRow.getRowNum()!=0){
                  Iterator<Cell>cellIterator = nextRow.cellIterator();
                  TblCalendarEmployeeAttendance employeeAttendance = new TblCalendarEmployeeAttendance();
                
                  while(cellIterator.hasNext()){
                   Cell nextCell = cellIterator.next();
                   int columnIndex = nextCell.getColumnIndex();
                  
                  switch(columnIndex){
                      case 0:
                       if(nextCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                          if(HSSFDateUtil.isCellDateFormatted(nextCell)){
                             java.sql.Date dateExcel = new java.sql.Date(nextCell.getDateCellValue().getTime());
                             List<SysCalendar>listCalendarExcel = parentController.getFAttendanceManager().getSysCalendarByDate(null,null,dateExcel,"excel");
                             for(SysCalendar getCalendarExcel : listCalendarExcel){
                               employeeAttendance.setSysCalendar(getCalendarExcel);
                             }
                          }
                       }
                       
                       break;
                       
                      case 1:
                        List<TblEmployee>listEmployeeExcel = parentController.getFAttendanceManager().getAllEmployeeByFingerPrint((String)getCellValue(nextCell));
                        
                        for(TblEmployee getEmployee:listEmployeeExcel){
                          System.out.println("employee:"+getEmployee.getTblPeople().getFullName());
                          employeeAttendance.setTblEmployeeByIdemployee(getEmployee);
                          employeeAttendance.setCodeFingerPrint(getEmployee.getCodeFingerPrint());
                        }
                        
                     break;
                     
                      case 2:
                         if(nextCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                            if(HSSFDateUtil.isCellDateFormatted(nextCell)){
                              java.sql.Date dateExcel = new java.sql.Date(nextCell.getDateCellValue().getTime());
                              employeeAttendance.setBeginReal(dateExcel);
                            }
                        }
                        break;
                        
                      case 3:
                         if(nextCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                           if(HSSFDateUtil.isCellDateFormatted(nextCell)){
                              java.sql.Date dateExcel = new java.sql.Date(nextCell.getDateCellValue().getTime());
                              employeeAttendance.setEndReal(dateExcel);
                           } 
                         }
                        break;
                    }
                  
                }
                   listAttendanceExcel.addAll(employeeAttendance);
                }
           
        }
        
           for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
               System.out.println("hsl Excel>>"+getEmployeeAttendanceExcel.getBeginReal());
            }
       
         /*for(TblCalendarEmployeeAttendance getEmployeeAttendance:listAttendance){
              for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
                if(getEmployeeAttendanceExcel.getSysCalendar().getIdcalendar()==getEmployeeAttendance.getSysCalendar().getIdcalendar()){
                   if(getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()==getEmployeeAttendance.getTblEmployeeByIdemployee().getIdemployee()){
                      getEmployeeAttendance.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                      getEmployeeAttendance.setEndReal(getEmployeeAttendanceExcel.getEndReal());
                   }
                 }
              }
            }*/
           
           
           /*for(SysCalendar getCalendar : listCalendar){
                for(TblEmployee getEmployee : listEmployee){
                   boolean check = false;
                  for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
                       if(getEmployeeAttendanceExcel.getSysCalendar().getIdcalendar()==getCalendar.getIdcalendar()){
                           if(getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()==getEmployee.getIdemployee()){
                               TblCalendarEmployeeAttendance employeeSchedule = new TblCalendarEmployeeAttendance(getEmployeeAttendanceExcel);
                               employeeSchedule.setTblEmployeeByIdemployee(new TblEmployee(employeeSchedule.getTblEmployeeByIdemployee()));
                               employeeSchedule.setSysCalendar(new SysCalendar(employeeSchedule.getSysCalendar()));
                               getAllEmployeeSchedule(employeeSchedule);
                               //getAllEmployeeOvertime(employeeSchedule);
                               TblCalendarEmployeeAttendance employeeOvertime = new TblCalendarEmployeeAttendance(getEmployeeAttendanceExcel);
                               employeeOvertime.setTblEmployeeByIdemployee(new TblEmployee(employeeOvertime.getTblEmployeeByIdemployee()));
                               employeeOvertime.setSysCalendar(new SysCalendar(employeeOvertime.getSysCalendar()));
                               getAllEmployeeSchedule(employeeOvertime);
                               getAllEmployeeOvertime(employeeOvertime);
                              insertDataAttendance(employeeSchedule,listAttendance);
                              insertDataAttendance(employeeOvertime,listAttendance);
                               System.out.println("size list Attendance:"+listAttendance.size());
                               check = true;
                            }  
                        }
                    }
                    
                    if(check==false){
                     TblCalendarEmployeeAttendance newAttendance = new TblCalendarEmployeeAttendance();
                     newAttendance.setSysCalendar(getCalendar);
                     newAttendance.setTblEmployeeByIdemployee(getEmployee);
                     newAttendance.setCodeFingerPrint(getEmployee.getCodeFingerPrint());
                     getAllEmployeeSchedule(newAttendance);
                     getAllEmployeeOvertime(newAttendance);
                     listAttendance.add(newAttendance);
                    }      
                }
            }
          workBook.close();
          fileAttendance.close();
        }catch (FileNotFoundException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return listAttendance;
    }*/
    
    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
           case Cell.CELL_TYPE_STRING:
              return cell.getStringCellValue();
 
           case Cell.CELL_TYPE_BOOLEAN:
              return cell.getBooleanCellValue();
 
          case Cell.CELL_TYPE_NUMERIC:
              return cell.getNumericCellValue();
        }
 
        return null;
    }
    
    //get data schedule
    
     private void getAllEmployeeScheduleWithOutDate(TblCalendarEmployeeAttendance employeeAttendance){
       List<TblCalendarEmployeeSchedule>listSchedule = parentController.getFAttendanceManager().getAllDataScheduleByPeriode(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()));
        // System.out.println("size list schedule:"+listSchedule);
      
       for(TblCalendarEmployeeSchedule getSchedule : listSchedule){
           
            //boolean found = false;
            if(getSchedule.getTblEmployeeByIdemployee().getIdemployee()==employeeAttendance.getTblEmployeeByIdemployee().getIdemployee() && getSchedule.getSysCalendar().getIdcalendar()==employeeAttendance.getSysCalendar().getIdcalendar()){
              //TblCalendarEmployeeAttendance employeeAttendance = new TblCalendarEmployeeAttendance();
              employeeAttendance.setSysCalendar(getSchedule.getSysCalendar());
              employeeAttendance.setTblEmployeeByIdemployee(getSchedule.getTblEmployeeByIdemployee());
              employeeAttendance.setBeginSchedule(getSchedule.getTblEmployeeSchedule().getBeginTime());
              employeeAttendance.setEndSchedule(getSchedule.getTblEmployeeSchedule().getEndTime());
            }
            /*employeeAttendance.setSysCalendar(getSchedule.getSysCalendar());
            employeeAttendance.setTblEmployeeByIdemployee(getSchedule.getTblEmployeeByIdemployee());
            employeeAttendance.setBeginSchedule(getSchedule.getTblEmployeeSchedule().getBeginTime());
            employeeAttendance.setEndSchedule(getSchedule.getTblEmployeeSchedule().getEndTime());
            insertDataAttendance(employeeAttendance,listAttendance);*/
            /*if(getSchedule.getTblEmployeeByIdemployee().getIdemployee()==employee.getIdemployee() 
                && getSchedule.getSysCalendar().getIdcalendar()==calendar.getIdcalendar()){
               TblCalendarEmployeeSchedule employeeSchedule = new TblCalendarEmployeeSchedule();
               employeeSchedule.setSysCalendar(getSchedule.getSysCalendar());
               employeeSchedule.setTblEmployeeByIdemployee(getSchedule.getTblEmployeeByIdemployee());
               employeeSchedule.setTblEmployeeSchedule(getSchedule.getTblEmployeeSchedule());
               listEmployeeSchedule.add(getSchedule);
            }*/
        }     //System.out.println("schedule:"+getSchedule.getSysCalendar().getCalendarDate()+"-"+getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
    }
    
    private void getAllEmployeeOvertimeWithOutDate(TblCalendarEmployeeAttendance employeeAttendance){
       List<TblCalendarEmployeeOvertime>listOvertime = parentController.getFAttendanceManager().getAllDataOvertimeByPeriode(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()));
        System.out.println("size overtime :"+listOvertime.size()); 
       for(TblCalendarEmployeeOvertime getOvertime : listOvertime){
            boolean found = false;
            if(getOvertime.getSysCalendar().getIdcalendar()==employeeAttendance.getSysCalendar().getIdcalendar() && getOvertime.getTblEmployeeByIdemployee().getIdemployee()==employeeAttendance.getTblEmployeeByIdemployee().getIdemployee()){
               //TblCalendarEmployeeAttendance employeeAttendance = new TblCalendarEmployeeAttendance();
               employeeAttendance.setSysCalendar(getOvertime.getSysCalendar());
               employeeAttendance.setTblEmployeeByIdemployee(getOvertime.getTblEmployeeByIdemployee());
               employeeAttendance.setBeginSchedule(getOvertime.getBeginTime());
               employeeAttendance.setEndSchedule(getOvertime.getEndTime());
               employeeAttendance.setIsOvertime(Boolean.TRUE);
            }
            /*if(getOvertime.getTblEmployeeByIdemployee().getIdemployee()==employee.getIdemployee() 
                && getOvertime.getSysCalendar().getIdcalendar()==calendar.getIdcalendar()){
                /*employeeOvertime.setBeginTime(getOvertime.getBeginTime());
                employeeOvertime.setEndTime(getOvertime.getEndTime());
                employeeOvertime.setSysCalendar(getOvertime.getSysCalendar());
                employeeOvertime.setTblEmployeeByIdemployee(getOvertime.getTblEmployeeByIdemployee());*/
              //  listEmployeeOvertime.add(getOvertime);
            //}
        }   
           //System.out.println("schedule:"+getSchedule.getSysCalendar().getCalendarDate()+"-"+getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
    }
    
    //data from Excel
    private void getAllEmployeeRealWithOutDate(List<TblCalendarEmployeeAttendance>listAttendanceExcel,TblCalendarEmployeeAttendance employeeAttendance){
       
        for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
           if(getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()==employeeAttendance.getTblEmployeeByIdemployee().getIdemployee()){
               if(employeeAttendance.getBeginSchedule()!=null && employeeAttendance.getEndSchedule()!=null){
                  Time timeBeginReal = new Time(getEmployeeAttendanceExcel.getBeginReal().getTime());
                  Time timeEndReal = new Time(getEmployeeAttendanceExcel.getEndReal().getTime());
                  LocalDateTime ldtBeginReal = LocalDateTime.of(getEmployeeAttendanceExcel.getBeginReal().getYear()+1900,getEmployeeAttendanceExcel.getBeginReal().getMonth()+1,getEmployeeAttendanceExcel.getBeginReal().getDate(),timeBeginReal.getHours(),timeBeginReal.getMinutes(),timeBeginReal.getSeconds());
                  LocalDateTime ldtEndReal = LocalDateTime.of(getEmployeeAttendanceExcel.getEndReal().getYear()+1900,getEmployeeAttendanceExcel.getEndReal().getMonth()+1,getEmployeeAttendanceExcel.getEndReal().getDate(),timeEndReal.getHours(),timeEndReal.getMinutes(),timeEndReal.getSeconds()); 
                  LocalDateTime ldtBeginSchedule = LocalDateTime.of(employeeAttendance.getSysCalendar().getCalendarDate().getYear()+1900,employeeAttendance.getSysCalendar().getCalendarDate().getMonth()+1,employeeAttendance.getSysCalendar().getCalendarDate().getDate(),employeeAttendance.getBeginSchedule().getHours(),employeeAttendance.getBeginSchedule().getMinutes(),employeeAttendance.getBeginSchedule().getSeconds());
                  LocalDateTime ldtEndSchedule = LocalDateTime.of(employeeAttendance.getSysCalendar().getCalendarDate().getYear()+1900,employeeAttendance.getSysCalendar().getCalendarDate().getMonth()+1,employeeAttendance.getSysCalendar().getCalendarDate().getDate(),employeeAttendance.getEndSchedule().getHours(),employeeAttendance.getEndSchedule().getMinutes(),employeeAttendance.getEndSchedule().getSeconds());
          
                   if(ldtBeginReal.isBefore(employeeAttendance.getBeginSchedule().toLocalTime().isBefore(employeeAttendance.getEndSchedule().toLocalTime()) ? ldtEndSchedule:ldtEndSchedule.plusDays(1)) && ldtEndReal.isAfter(ldtBeginSchedule)){  
                     employeeAttendance.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                     employeeAttendance.setEndReal(getEmployeeAttendanceExcel.getEndReal());     
                   }
               }
               else if(employeeAttendance.getSysCalendar().getCalendarDate().toString().equalsIgnoreCase(getEmployeeAttendanceExcel.getBeginReal().toString())){
                  employeeAttendance.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                  employeeAttendance.setEndReal(getEmployeeAttendanceExcel.getEndReal());
               }
           }
           
        }
        /*for(TblCalendarEmployeeSchedule getEmployeeSchedule : listEmployeeSchedule){
           TblCalendarEmployeeAttendance employeeAttendanceWork = new TblCalendarEmployeeAttendance();
           employeeAttendanceWork.setBeginSchedule(getEmployeeSchedule.getTblEmployeeSchedule().getBeginTime());
           employeeAttendanceWork.setEndSchedule(getEmployeeSchedule.getTblEmployeeSchedule().getEndTime());
           employeeAttendanceWork.setScheduleName(getEmployeeSchedule.getTblEmployeeSchedule().getScheduleName());
           employeeAttendanceWork.setSysCalendar(getEmployeeSchedule.getSysCalendar());
           employeeAttendanceWork.setTblEmployeeByIdemployee(getEmployeeSchedule.getTblEmployeeByIdemployee());
            //System.out.println("hsl:"+employeeAttendanceWork.getTblEmployeeByIdemployee().getCodeEmployee() + employeeAttendanceWork.getScheduleName());
            insertDataAttendance(employeeAttendanceWork,listAttendance);
           for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
            
            System.out.println("hsl Employee Schedule:"+getEmployeeSchedule.getSysCalendar().getCalendarDate()+"-"+getEmployeeSchedule.getTblEmployeeByIdemployee().getIdemployee());
            System.out.println("hsl Employee Real:"+getEmployeeAttendanceExcel.getBeginReal()+"-"+getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee());
            boolean checkNotNull=false;
               
              if(getEmployeeSchedule.getTblEmployeeByIdemployee().getIdemployee()==getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()){
                
                Time timeBeginReal = new Time(getEmployeeAttendanceExcel.getBeginReal().getTime());
                Time timeEndReal = new Time(getEmployeeAttendanceExcel.getEndReal().getTime());
                LocalDateTime ldtBeginReal = LocalDateTime.of(getEmployeeAttendanceExcel.getBeginReal().getYear()+1900,getEmployeeAttendanceExcel.getBeginReal().getMonth()+1,getEmployeeAttendanceExcel.getBeginReal().getDate(),timeBeginReal.getHours(),timeBeginReal.getMinutes(),timeBeginReal.getSeconds());
                LocalDateTime ldtEndReal = LocalDateTime.of(getEmployeeAttendanceExcel.getEndReal().getYear()+1900,getEmployeeAttendanceExcel.getEndReal().getMonth()+1,getEmployeeAttendanceExcel.getEndReal().getDate(),timeEndReal.getHours(),timeEndReal.getMinutes(),timeEndReal.getSeconds()); 
                LocalDateTime ldtBeginSchedule = LocalDateTime.of(getEmployeeSchedule.getSysCalendar().getCalendarDate().getYear()+1900,getEmployeeSchedule.getSysCalendar().getCalendarDate().getMonth()+1,getEmployeeSchedule.getSysCalendar().getCalendarDate().getDate(),getEmployeeSchedule.getTblEmployeeSchedule().getBeginTime().getHours(),getEmployeeSchedule.getTblEmployeeSchedule().getBeginTime().getMinutes(),getEmployeeSchedule.getTblEmployeeSchedule().getBeginTime().getSeconds());
                LocalDateTime ldtEndSchedule = LocalDateTime.of(getEmployeeSchedule.getSysCalendar().getCalendarDate().getYear()+1900,getEmployeeSchedule.getSysCalendar().getCalendarDate().getMonth()+1,getEmployeeSchedule.getSysCalendar().getCalendarDate().getDate(),getEmployeeSchedule.getTblEmployeeSchedule().getEndTime().getHours(),getEmployeeSchedule.getTblEmployeeSchedule().getEndTime().getMinutes(),getEmployeeSchedule.getTblEmployeeSchedule().getEndTime().getSeconds());
          
                if(ldtBeginReal.isBefore(getEmployeeSchedule.getTblEmployeeSchedule().getBeginTime().toLocalTime().isBefore(getEmployeeSchedule.getTblEmployeeSchedule().getEndTime().toLocalTime()) ? ldtEndSchedule:ldtEndSchedule.plusDays(1)) && ldtEndReal.isAfter(ldtBeginSchedule)){  
                    employeeAttendanceWork.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                    employeeAttendanceWork.setEndReal(getEmployeeAttendanceExcel.getEndReal());
                    insertDataAttendance(employeeAttendanceWork,listAttendance);
                }
            }
        }
         
             /*if(employeeSchedule.getTblEmployeeSchedule()==null){
              if(calendar.getCalendarDate().toString().equals(getEmployeeAttendanceExcel.getBeginReal().toString())){
                if(employee.getIdemployee()==getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()){
                    employeeAttendanceWork.setSysCalendar(calendar);
                    employeeAttendanceWork.setTblEmployeeByIdemployee(getEmployeeAttendanceExcel.getTblEmployeeByIdemployee());
                    employeeAttendanceWork.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                    employeeAttendanceWork.setEndReal(getEmployeeAttendanceExcel.getEndReal());
                    insertDataAttendance(employeeAttendanceWork,listAttendance);
                }
            }
            }*/
            
        //}
        
    }
    
    private void getAllEmployeeRealOvertimeWithOutDate(List<TblCalendarEmployeeOvertime> listEmployeeOvertime,SysCalendar calendar,TblEmployee employee,List<TblCalendarEmployeeAttendance>listAttendanceExcel,List<TblCalendarEmployeeAttendance>listAttendance){
        for(TblCalendarEmployeeOvertime getEmployeeOvertime : listEmployeeOvertime){
         TblCalendarEmployeeAttendance employeeAttendanceWork = new TblCalendarEmployeeAttendance();
         employeeAttendanceWork.setBeginSchedule(getEmployeeOvertime.getBeginTime());
         employeeAttendanceWork.setEndSchedule(getEmployeeOvertime.getEndTime());
         employeeAttendanceWork.setSysCalendar(getEmployeeOvertime.getSysCalendar());
         employeeAttendanceWork.setTblEmployeeByIdemployee(getEmployeeOvertime.getTblEmployeeByIdemployee());
         employeeAttendanceWork.setIsOvertime(Boolean.TRUE);
         insertDataAttendance(employeeAttendanceWork,listAttendance);
        
           for(TblCalendarEmployeeAttendance getEmployeeAttendanceExcel : listAttendanceExcel){
            //System.out.println("hsl:"+employeeSchedule.getTblEmployeeByIdemployee());
            boolean checkNotNull=false;
              if(getEmployeeOvertime.getTblEmployeeByIdemployee().getIdemployee()==getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()){
                System.out.println("hsl AttendanceWork Overtime:"+employeeAttendanceWork.getTblEmployeeByIdemployee().getCodeEmployee()+" "+employeeAttendanceWork.getBeginSchedule());
                Time timeBeginReal = new Time(getEmployeeAttendanceExcel.getBeginReal().getTime());
                Time timeEndReal = new Time(getEmployeeAttendanceExcel.getEndReal().getTime());
                LocalDateTime ldtBeginReal = LocalDateTime.of(getEmployeeAttendanceExcel.getBeginReal().getYear()+1900,getEmployeeAttendanceExcel.getBeginReal().getMonth()+1,getEmployeeAttendanceExcel.getBeginReal().getDate(),timeBeginReal.getHours(),timeBeginReal.getMinutes(),timeBeginReal.getSeconds());
                LocalDateTime ldtEndReal = LocalDateTime.of(getEmployeeAttendanceExcel.getEndReal().getYear()+1900,getEmployeeAttendanceExcel.getEndReal().getMonth()+1,getEmployeeAttendanceExcel.getEndReal().getDate(),timeEndReal.getHours(),timeEndReal.getMinutes(),timeEndReal.getSeconds()); 
                LocalDateTime ldtBeginSchedule = LocalDateTime.of(getEmployeeOvertime.getSysCalendar().getCalendarDate().getYear()+1900,getEmployeeOvertime.getSysCalendar().getCalendarDate().getMonth()+1,getEmployeeOvertime.getSysCalendar().getCalendarDate().getDate(),getEmployeeOvertime.getBeginTime().getHours(),getEmployeeOvertime.getBeginTime().getMinutes(),getEmployeeOvertime.getBeginTime().getSeconds());
                LocalDateTime ldtEndSchedule = LocalDateTime.of(getEmployeeOvertime.getSysCalendar().getCalendarDate().getYear()+1900,getEmployeeOvertime.getSysCalendar().getCalendarDate().getMonth()+1,getEmployeeOvertime.getSysCalendar().getCalendarDate().getDate(),getEmployeeOvertime.getEndTime().getHours(),getEmployeeOvertime.getEndTime().getMinutes(),getEmployeeOvertime.getEndTime().getSeconds());
                
                if(ldtBeginReal.isBefore((getEmployeeOvertime.getBeginTime().toLocalTime().isBefore(getEmployeeOvertime.getEndTime().toLocalTime()))?ldtEndSchedule:ldtEndSchedule.plusDays(1)) && ldtEndReal.isAfter(ldtBeginSchedule)){  
                    employeeAttendanceWork.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                    employeeAttendanceWork.setEndReal(getEmployeeAttendanceExcel.getEndReal());
                    employeeAttendanceWork.setIsOvertime(Boolean.TRUE);
                    insertDataAttendance(employeeAttendanceWork,listAttendance);
                }
            }
             /*if(employeeSchedule.getTblEmployeeSchedule()==null){
              if(calendar.getCalendarDate().toString().equals(getEmployeeAttendanceExcel.getBeginReal().toString())){
                if(employee.getIdemployee()==getEmployeeAttendanceExcel.getTblEmployeeByIdemployee().getIdemployee()){
                    employeeAttendanceWork.setSysCalendar(calendar);
                    employeeAttendanceWork.setTblEmployeeByIdemployee(getEmployeeAttendanceExcel.getTblEmployeeByIdemployee());
                    employeeAttendanceWork.setBeginReal(getEmployeeAttendanceExcel.getBeginReal());
                    employeeAttendanceWork.setEndReal(getEmployeeAttendanceExcel.getEndReal());
                    insertDataAttendance(employeeAttendanceWork,listAttendance);
                }
            }
            }*/
            
        }
           }
          
        
    }
     
    private void getAllEmployeeSchedule(TblCalendarEmployeeAttendance employeeSchedule){
       List<TblCalendarEmployeeSchedule>listSchedule = parentController.getFAttendanceManager().getAllDataScheduleByPeriode(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()));
       for(TblCalendarEmployeeSchedule getSchedule : listSchedule){
            if(getSchedule.getSysCalendar().getIdcalendar()==employeeSchedule.getSysCalendar().getIdcalendar() && 
              getSchedule.getTblEmployeeByIdemployee().getIdemployee()==employeeSchedule.getTblEmployeeByIdemployee().getIdemployee()){
                boolean found = false;
                if(employeeSchedule.getBeginReal()!=null && employeeSchedule.getEndReal()!=null){
                  Time timeBeginReal = new Time(employeeSchedule.getBeginReal().getTime());
                  Time timeEndReal = new Time(employeeSchedule.getEndReal().getTime());
                    System.out.println("time:" + timeBeginReal.toLocalTime().isBefore(getSchedule.getTblEmployeeSchedule().getEndTime().toLocalTime()));
                    if(timeBeginReal.toLocalTime().isBefore(getSchedule.getTblEmployeeSchedule().getEndTime().toLocalTime()) && timeEndReal.toLocalTime().isAfter(getSchedule.getTblEmployeeSchedule().getBeginTime().toLocalTime())){
                      employeeSchedule.setBeginSchedule(getSchedule.getTblEmployeeSchedule().getBeginTime());
                      employeeSchedule.setEndSchedule(getSchedule.getTblEmployeeSchedule().getEndTime());
                      found=true;
                    }
                }
                
                if(!found){
                    if(employeeSchedule.getBeginReal()==null && employeeSchedule.getEndReal()==null){
                     employeeSchedule.setBeginSchedule(getSchedule.getTblEmployeeSchedule().getBeginTime());
                     employeeSchedule.setEndSchedule(getSchedule.getTblEmployeeSchedule().getEndTime());
                    }
                }
                
            }
        }
           //System.out.println("schedule:"+getSchedule.getSysCalendar().getCalendarDate()+"-"+getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
    }
    
    private void getAllEmployeeOvertime(TblCalendarEmployeeAttendance employeeOvertime){
        System.out.println("employee schedule:"+employeeOvertime.getBeginSchedule());
       List<TblCalendarEmployeeOvertime>listSchedule = parentController.getFAttendanceManager().getAllDataOvertimeByPeriode(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()));
        System.out.println(">>"+listSchedule.size());
      
       for(TblCalendarEmployeeOvertime getSchedule : listSchedule){
           System.out.println("hsl Overtime:"+getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
            if(getSchedule.getSysCalendar().getIdcalendar()==employeeOvertime.getSysCalendar().getIdcalendar()&& 
               getSchedule.getTblEmployeeByIdemployee().getIdemployee()==employeeOvertime.getTblEmployeeByIdemployee().getIdemployee()){
                 /* employeeOvertime.setBeginSchedule(getSchedule.getBeginTime());
                  employeeOvertime.setEndSchedule(getSchedule.getEndTime());
                  employeeOvertime.setIsOvertime(Boolean.TRUE);*/
                  Time timeBeginReal = new Time(employeeOvertime.getBeginReal().getTime());
                  Time timeEndReal = new Time(employeeOvertime.getEndReal().getTime());
                  
                  boolean checkNotNull = false;
                  if(employeeOvertime.getBeginSchedule()!=null && employeeOvertime.getEndSchedule()!=null){
                     boolean foundNotSchedule = false;
                    if(!timeBeginReal.equals(employeeOvertime.getBeginSchedule())&& !timeEndReal.equals(employeeOvertime.getEndSchedule())){
                       if(timeBeginReal.toLocalTime().isAfter(employeeOvertime.getEndSchedule().toLocalTime())){
                            if(timeBeginReal.toLocalTime().isBefore(getSchedule.getEndTime().toLocalTime()) && timeEndReal.toLocalTime().isAfter(getSchedule.getBeginTime().toLocalTime())){
                              employeeOvertime.setBeginSchedule(getSchedule.getBeginTime());
                              employeeOvertime.setEndSchedule(getSchedule.getEndTime());
                              employeeOvertime.setIsOvertime(Boolean.TRUE);
                              foundNotSchedule  = true;
                              checkNotNull = true;
                            }
                       }
                    }
                    
                    if(!foundNotSchedule){
                        if(timeEndReal.toLocalTime().isAfter(getSchedule.getBeginTime().toLocalTime())){
                          employeeOvertime.setBeginSchedule(getSchedule.getBeginTime());
                          employeeOvertime.setEndSchedule(getSchedule.getEndTime());
                          employeeOvertime.setIsOvertime(Boolean.TRUE);
                          checkNotNull = true;
                        } 
                    }
                  }
                  
                    if(!checkNotNull){
                        if(employeeOvertime.getBeginSchedule()==null && employeeOvertime.getEndSchedule()==null){
                         employeeOvertime.setBeginSchedule(getSchedule.getBeginTime());
                         employeeOvertime.setEndSchedule(getSchedule.getEndTime());
                         employeeOvertime.setIsOvertime(Boolean.TRUE);
                        }
                    }
                } 
          
            
                /*Time timeBeginReal = new Time(employeeOvertime.getBeginReal().getTime());
              Time timeEndReal = new Time(employeeOvertime.getEndReal().getTime());
                if(timeBeginReal!=getSchedule.getTblSchedule().getTblEmployeeSchedule().getBeginTime() && timeEndReal!=getSchedule.getTblSchedule().getTblEmployeeSchedule().getEndTime()){
                    if(timeBeginReal==getSchedule.getTblOvertime().getBeginTime() || timeBeginReal.toLocalTime().isAfter(getSchedule.getTblOvertime().getBeginTime().toLocalTime())){
                      employeeOvertime.setBeginSchedule(getSchedule.getTblOvertime().getBeginTime());
                      employeeOvertime.setEndSchedule(getSchedule.getTblOvertime().getEndTime());
                      employeeOvertime.setIsOvertime(Boolean.TRUE);
                    }
                }
                else{
                 employeeOvertime.setBeginSchedule(getSchedule.getTblOvertime().getBeginTime());
                 employeeOvertime.setEndSchedule(getSchedule.getTblOvertime().getEndTime());
                 employeeOvertime.setIsOvertime(Boolean.TRUE);
                }*/
        }
           //System.out.println("schedule:"+getSchedule.getSysCalendar().getCalendarDate()+"-"+getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
    }
    
    private List<TblCalendarEmployeeAttendance> insertDataAttendance(TblCalendarEmployeeAttendance employeeAttendance,List<TblCalendarEmployeeAttendance>listAttendance){
        boolean found = false;
         
         System.out.println("isOvertime:"+employeeAttendance.getIsOvertime());
        for(int i = 0; i<listAttendance.size();i++){
            boolean checkScheduleNotNull=false;
            boolean checkRealNotNull = false;
            
           if(listAttendance.get(i).getTblEmployeeByIdemployee().getIdemployee()==employeeAttendance.getTblEmployeeByIdemployee().getIdemployee() 
               && listAttendance.get(i).getSysCalendar().getIdcalendar()==employeeAttendance.getSysCalendar().getIdcalendar()){
               if(listAttendance.get(i).getBeginSchedule()!=null && listAttendance.get(i).getEndSchedule()!=null
                   && employeeAttendance.getBeginSchedule()!=null && employeeAttendance.getEndSchedule()!=null){
                   if(listAttendance.get(i).getBeginSchedule().equals(employeeAttendance.getBeginSchedule())
                       && listAttendance.get(i).getEndSchedule().equals(employeeAttendance.getEndSchedule())){
                       if(listAttendance.get(i).getBeginReal()!=null && listAttendance.get(i).getBeginReal()!=null
                         && employeeAttendance.getBeginReal()!=null && employeeAttendance.getEndReal()!=null){
                           if(listAttendance.get(i).getBeginReal().equals(employeeAttendance.getBeginReal())
                              && listAttendance.get(i).getEndReal().equals(employeeAttendance.getEndReal())){
                               if(listAttendance.get(i).getIsOvertime().booleanValue()==employeeAttendance.getIsOvertime().booleanValue()){
                                 found = true;
                               }
                           }
                       }
                      else{
                           if(listAttendance.get(i).getIsOvertime().booleanValue()==employeeAttendance.getIsOvertime().booleanValue()){
                             found = true;
                           }
                       }
                   }
               }
               else{
                 found= true;
               }
               
           }
        }
        
        System.out.println("hsl found:"+found);
        
        if(!found){
          listAttendance.add(employeeAttendance);
        }
        
       /* if(employeeAttendanceSchedule.getTblEmployeeByIdemployee().getIdemployee()==4){
                System.out.println("coba:");
                System.out.println(employeeAttendanceSchedule.getSysCalendar().getIdcalendar());
                System.out.println(employeeAttendanceSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
                System.out.println(employeeAttendanceSchedule.getBeginSchedule());
                System.out.println(employeeAttendanceSchedule.getEndSchedule());
                System.out.println(employeeAttendanceSchedule.getIsOvertime().booleanValue());
                System.out.println("--- " + found);
            }*/
        
        return listAttendance;
    }
    /*private void getAllEmployeeScheduleOvertime(List<TblCalendarEmployeeAttendance>listAttendance){
       List<TblCalendarEmployeeOvertime>listOvertime = parentController.getFAttendanceManager().getAllDataOvertimeByPeriode(Date.valueOf(dpStartPeriode.getValue()),Date.valueOf(dpEndPeriode.getValue()));
       for(TblCalendarEmployeeOvertime getOvertime : listOvertime){
           for(TblCalendarEmployeeAttendance getAttendance : listAttendance){
             if(getAttendance.getBeginReal().getHours()>=getOvertime.getBeginTime().getHours()){
                if(getOvertime.getSysCalendar().getIdcalendar()==getAttendance.getSysCalendar().getIdcalendar()){
                    if(getOvertime.getTblEmployeeByIdemployee().getIdemployee()==getAttendance.getTblEmployeeByIdemployee().getIdemployee()){
                      getAttendance.setIsOvertime(Boolean.TRUE);
                      getAttendance.setBeginSchedule(getOvertime.getBeginTime());
                      getAttendance.setEndSchedule(getOvertime.getEndTime());
                    }
                }
             }
           }
           //System.out.println("schedule:"+getSchedule.getSysCalendar().getCalendarDate()+"-"+getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
       }
    }*/
    
     private void getTypeAttendance(List<TblCalendarEmployeeAttendance>listAttendance){
        for(TblCalendarEmployeeAttendance getEmployeeAttendance : listAttendance){
          List<TblCalendarEmployeeLeave>listLeave = parentController.getFAttendanceManager().getAllEmployeeLeaveByDateAndIdEmployee((java.sql.Date)getEmployeeAttendance.getSysCalendar().getCalendarDate(),getEmployeeAttendance.getTblEmployeeByIdemployee().getIdemployee());  
            System.out.println("size leave:"+listLeave.size());
            //System.out.println("hsl status start:"+getEmployeeAttendance.getRefEmployeeAttendanceStartStatus());
          if(getEmployeeAttendance.getBeginSchedule()==null && getEmployeeAttendance.getEndSchedule()==null && getEmployeeAttendance.getIsOvertime()==false){  
            getEmployeeAttendance.setRefEmployeeAttendanceStatus(parentController.getFAttendanceManager().getAttendanceStatus((int)8));
          }
          else if(getEmployeeAttendance.getBeginReal()==null && getEmployeeAttendance.getEndReal()==null && getEmployeeAttendance.getBeginSchedule()!=null && getEmployeeAttendance.getEndSchedule()!=null && (getEmployeeAttendance.getIsOvertime()==false || getEmployeeAttendance.getIsOvertime()==true)){
            boolean leave = false;
            if(listLeave.size()!=0){
              getEmployeeAttendance.setRefEmployeeAttendanceStatus(parentController.getFAttendanceManager().getAttendanceStatus((int)0));
            }
            else{
              getEmployeeAttendance.setRefEmployeeAttendanceStatus(parentController.getFAttendanceManager().getAttendanceStatus((int)9));
            }
          }
          else if(getEmployeeAttendance.getBeginReal()!=null && getEmployeeAttendance.getEndReal()!=null && getEmployeeAttendance.getBeginSchedule()!=null && getEmployeeAttendance.getEndSchedule()!=null){
              Time timeBeginReal = new Time(getEmployeeAttendance.getBeginReal().getTime());
              Time timeEndReal = new Time(getEmployeeAttendance.getEndReal().getTime());
              
               LocalDateTime ldtBeginReal = LocalDateTime.of(getEmployeeAttendance.getBeginReal().getYear()+1900,getEmployeeAttendance.getBeginReal().getMonth()+1,getEmployeeAttendance.getBeginReal().getDate(),timeBeginReal.getHours(),timeBeginReal.getMinutes(),timeBeginReal.getSeconds());
               LocalDateTime ldtEndReal = LocalDateTime.of(getEmployeeAttendance.getEndReal().getYear()+1900,getEmployeeAttendance.getEndReal().getMonth()+1,getEmployeeAttendance.getEndReal().getDate(),timeEndReal.getHours(),timeEndReal.getMinutes(),timeEndReal.getSeconds()); 
               LocalDateTime ldtBeginSchedule = LocalDateTime.of(getEmployeeAttendance.getSysCalendar().getCalendarDate().getYear()+1900,getEmployeeAttendance.getSysCalendar().getCalendarDate().getMonth()+1,getEmployeeAttendance.getSysCalendar().getCalendarDate().getDate(),getEmployeeAttendance.getBeginSchedule().getHours(),getEmployeeAttendance.getBeginSchedule().getMinutes(),getEmployeeAttendance.getBeginSchedule().getSeconds());
               LocalDateTime ldtEndSchedule = LocalDateTime.of(getEmployeeAttendance.getSysCalendar().getCalendarDate().getYear()+1900,getEmployeeAttendance.getSysCalendar().getCalendarDate().getMonth()+1,getEmployeeAttendance.getSysCalendar().getCalendarDate().getDate(),getEmployeeAttendance.getEndSchedule().getHours(),getEmployeeAttendance.getEndSchedule().getMinutes(),getEmployeeAttendance.getEndSchedule().getSeconds());
                
              System.out.println("hsl time begin real:"+timeBeginReal);
              System.out.println("hsl begin schedule:"+getEmployeeAttendance.getBeginSchedule());
              System.out.println("hsl equals:"+timeBeginReal.equals(getEmployeeAttendance.getBeginSchedule()));
              System.out.println("hsl equals:"+String.valueOf(timeBeginReal).equals(String.valueOf(getEmployeeAttendance.getBeginSchedule())));
              getEmployeeAttendance.setRefEmployeeAttendanceStatus(parentController.getFAttendanceManager().getAttendanceStatus((int)2));
                if(getEmployeeAttendance.getIsOvertime().booleanValue()==false){
                    if(ldtBeginReal.isBefore(ldtBeginSchedule)){
                        getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(0));              
                    }
                    else if(ldtBeginReal.toString().equals(ldtBeginSchedule.toString())){
                        getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(1));
                        //getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(1));
                    }
                    else{
                      getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(2));
                       //getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(2));
                    }
                    
                    if(ldtEndReal.isBefore((getEmployeeAttendance.getBeginSchedule().toLocalTime().isBefore(getEmployeeAttendance.getEndSchedule().toLocalTime()))?ldtEndSchedule:ldtEndSchedule.plusDays(1))){
                        getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(0));              
                    }
                    else if(ldtEndReal.toString().equals((getEmployeeAttendance.getBeginSchedule().toLocalTime().isBefore(getEmployeeAttendance.getEndSchedule().toLocalTime()))?ldtEndSchedule.toString():ldtEndSchedule.plusDays(1).toString())){
                        getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(1));
                    }
                    else{
                      //getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(2));
                      getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(2));
                    }
                }
                else{
                  if(ldtBeginReal.isBefore(ldtBeginSchedule)){
                        getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(0));              
                    }
                    else if(ldtBeginReal.toString().equals(ldtBeginSchedule.toString())){
                        getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(1));
                        //getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(1));
                    }
                    else{
                      getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(2));
                       //getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(2));
                    }
                    
                    if(ldtEndReal.isBefore((getEmployeeAttendance.getBeginSchedule().toLocalTime().isBefore(getEmployeeAttendance.getEndSchedule().toLocalTime()))?ldtEndSchedule:ldtEndSchedule.plusDays(1))){
                        getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(0));              
                    }
                    else if(ldtEndReal.toString().equals((getEmployeeAttendance.getBeginSchedule().toLocalTime().isBefore(getEmployeeAttendance.getEndSchedule().toLocalTime()))?ldtEndSchedule.toString():ldtEndSchedule.plusDays(1).toString())){
                        getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(1));
                    }
                    else{
                      //getEmployeeAttendance.setRefEmployeeAttendanceStartStatus(parentController.getFAttendanceManager().getAttendanceStartStatus(2));
                      getEmployeeAttendance.setRefEmployeeAttendanceEndStatus(parentController.getFAttendanceManager().getAttendanceEndStatus(2));
                    }
              }
          }
          
       }
     
      //return attendanceStatus;
    }
     
    private void insertEmployeeAttendance(List<TblCalendarEmployeeAttendance>listAllAttendance){
        if(listAllAttendance.size()!=0){
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
          if(alert.getResult()==ButtonType.OK){
              if(parentController.getFAttendanceManager().insertDataAttendance(listAllAttendance)){
              ClassMessage.showSucceedInsertingDataMessage("", null);
              tableAttendance.setItems(FXCollections.observableArrayList(listAllAttendance));
              //set unsaving data input -> 'false'
              ClassSession.unSavingDataInput.set(false);
            }
            else{
              ClassMessage.showFailedInsertingDataMessage(parentController.getFAttendanceManager().getErrorMessage(), null);
            }
          }
          else{
             tableAttendance.setItems(listAttendance);
          }
        }
        else{
           HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Tidak ada data yang diinput..!!",null);  
        }
    }
     
    public Stage dialogStage;
    private void showSettingFingerPrintDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_attendance/attendance/AttendanceDialogPopup.fxml"));
            AttendanceDialogPopupController attendanceDialogPopupController = new AttendanceDialogPopupController(this);
            loader.setController(attendanceDialogPopupController);
            
            Region page = loader.load();
            
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);
            
            Undecorator undecorator = new Undecorator(dialogStage,page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);
            
            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);
            
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(EmployeeScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String errInput;
    private boolean checkDataInput(){
      boolean check = true;
      errInput = "";
      if(dpStartPeriode.getValue()==null){
        check = false;
        errInput+="Tanggal Periode Awal: "+ClassMessage.defaultErrorNullValueMessage+"\n";
      }
      
      if(dpEndPeriode.getValue()==null){
         check = false;
         errInput+="Tanggal Periode Akhir: "+ClassMessage.defaultErrorNullValueMessage+"\n";
      }
      
      if(txtNamePath.getText()==null || txtNamePath.getText()==""){
        check = false;
        errInput+="Nama File: "+ClassMessage.defaultErrorNullValueMessage+"\n";
      }
      
      return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       initTableAttendance();
       initButtonControl();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public AttendanceController(FeatureAttendanceController parentController){
        this.parentController = parentController;
    }
    
    private final FeatureAttendanceController parentController;
    
     public FAttendanceManager getService(){
        return parentController.getFAttendanceManager();
    }
}
