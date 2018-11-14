/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_leave.leave;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefEmployeeLeaveType;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblCalendarEmployeeLeave;
import hotelfx.persistence.model.TblCalendarEmployeeOvertime;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeOvertime;
import hotelfx.persistence.model.TblEmployeeSchedule;
import hotelfx.persistence.model.TblFloor;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_leave.FeatureLeaveController;
import hotelfx.view.feature_schedule.employee_schedule.EmployeeScheduleInputDialogController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class LeaveController implements Initializable{
    
       /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataLeave;

    private DoubleProperty dataLeaveFormShowStatus;

    @FXML
    private AnchorPane contentLayout;
    
    @FXML
    private AnchorPane ancBorderPaneLayout;

    @FXML
    private AnchorPane tableDataLeaveLayoutDisableLayer;
    
    ClassFilteringTable cftLeave;
    
    private boolean isTimeLinePlaying = false;
    
    private void setDataOffworkSplitpane() {
        spDataLeave.setDividerPositions(1);
        
        dataLeaveFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataLeaveFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataLeave.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataLeave.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataLeaveFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if(dataInputStatus!=3){
               if (newVal.doubleValue() == 0.0) {    //enable
                ancBorderPaneLayout.setDisable(false);
                tableDataLeaveLayoutDisableLayer.setDisable(true);
                ancBorderPaneLayout.toFront();
               }
               if (newVal.doubleValue() == 1) {  //disable
                ancBorderPaneLayout.setDisable(true);
                tableDataLeaveLayoutDisableLayer.setDisable(false);
                tableDataLeaveLayoutDisableLayer.toFront();
               }
            }
           
        });
        
        dataLeaveFormShowStatus.set(0.0);

    }
    
    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataLeaveLayout;
    
    @FXML
    private AnchorPane ancFiltering;
    
    private ClassTableWithControl tableDataLeave;

    private void initTableDataOffwork() {
        //set table
        setTableDataLeave();
        //set control
        setTableControlDataLeave();
        //set table-control to content-view
        tableDataLeaveLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataLeave, 15.0);
        AnchorPane.setLeftAnchor(tableDataLeave, 15.0);
        AnchorPane.setRightAnchor(tableDataLeave, 15.0);
        AnchorPane.setTopAnchor(tableDataLeave, 15.0);
        tableDataLeaveLayout.getChildren().add(tableDataLeave);
    }

//    private final PseudoClass cellColor = PseudoClass.getPseudoClass("date");
    private void setTableDataLeave() {
        TableView<TblCalendarEmployeeLeave> tableView = new TableView();
        TableColumn<TblCalendarEmployeeLeave,String> dateOffwork = new TableColumn("Tanggal Cuti");
        dateOffwork.setMinWidth(100);
        dateOffwork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeLeave,String> param)
                -> Bindings.createStringBinding(()->(new SimpleDateFormat("dd MMM yyyy")).format(param.getValue().getSysCalendar().getCalendarDate()), param.getValue().getSysCalendar().calendarDateProperty()));
        TableColumn<TblCalendarEmployeeLeave,String>codeEmployee = new TableColumn("ID");
        codeEmployee.setMinWidth(100);
        codeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeLeave, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(), param.getValue().getTblEmployeeByIdemployee().codeEmployeeProperty()));
        TableColumn<TblCalendarEmployeeLeave,String>typeEmployee = new TableColumn("Tipe");
        typeEmployee.setMinWidth(120);
        typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeLeave, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(), param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().typeNameProperty()));
        TableColumn<TblCalendarEmployeeLeave, String>nameEmployee = new TableColumn("Nama");
        nameEmployee.setMinWidth(160);
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeLeave, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(), param.getValue().getTblEmployeeByIdemployee().getTblPeople().fullNameProperty()));
        TableColumn<TblCalendarEmployeeLeave,String>jobEmployee = new TableColumn("Jabatan");
        jobEmployee.setMinWidth(120);
        jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeLeave,String>param)
               ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblJob().getJobName(),param.getValue().tblEmployeeByIdemployeeProperty()));
        TableColumn<TblCalendarEmployeeLeave,String>employee = new TableColumn("Karyawan");
        employee.getColumns().addAll(typeEmployee,codeEmployee,nameEmployee,jobEmployee);
        TableColumn<TblCalendarEmployeeLeave, String>typeOffwork = new TableColumn("Tipe");
        typeOffwork.setMinWidth(120);
        typeOffwork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeLeave, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefEmployeeLeaveType().getTypeName(), param.getValue().getRefEmployeeLeaveType().typeNameProperty()));
        TableColumn<TblCalendarEmployeeLeave, String>cutLeaveStatus = new TableColumn("Status");
        cutLeaveStatus.setMinWidth(160);
        cutLeaveStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeLeave, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getCutLeaveStatus().booleanValue()==false)?"Tidak Potong Cuti":"Potong Cuti", param.getValue().cutLeaveStatusProperty()));
        TableColumn<TblCalendarEmployeeLeave,String>leave = new TableColumn("Cuti");
        leave.getColumns().addAll(typeOffwork,cutLeaveStatus);
        TableColumn<TblCalendarEmployeeLeave, String> offworkNote = new TableColumn("Keterangan");
        offworkNote.setMinWidth(200);
        offworkNote.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
        
        tableView.getColumns().addAll(dateOffwork,employee,leave,offworkNote);
        tableView.setItems(loadAllDataLeave());
        
        tableDataLeave = new ClassTableWithControl(tableView);
        
         tableDataLeave.getTableView().setRowFactory(tv->{
            TableRow<TblCalendarEmployeeLeave>row = new TableRow<>();
            row.itemProperty().addListener((obs,oldVal,newVal)->{
                if(newVal!=null){
                  row.pseudoClassStateChanged(datebeforePseudoClass,newVal.getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now())));
                  //row.pseudoClassStateChanged(leaveAnnualPseudoClass,newVal.getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now())) && newVal.getRefEmployeeLeaveType().getIdtype()==0);
                  //System.out.println(">>"+newVal.getTblEmployeeByIdemployee().getCodeEmployee());
                }
               //row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
              });
            
            row.setOnMouseClicked((e)->{
               if(e.getClickCount()==2){
                   if(isShowStatus.get()){
                     dataLeaveUnshowHandle();
                    }
                   else{
                       if(!row.isEmpty()){
                           if(row.getItem().getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))){
                             dataLeaveShowHandle();
                           }
                           else if(DashboardController.feature.getSelectedRoleFeature().getUpdateData()){
                              dataLeaveUpdateHandle(); 
                           }
                        }
                    }
               }
               else{
                   if(!row.isEmpty()){
                       if(isShowStatus.get()){
                           if(row.getItem().getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))){
                              dataLeaveShowHandle();
                           }
                           else if(DashboardController.feature.getSelectedRoleFeature().getUpdateData()){
                              dataLeaveUpdateHandle();
                           }
                        }
                    }
                }
            });
            /*for(int i = 0; i<tableDataLeave.getTableView().getItems().size();i++){
              row.itemProperty().addListener((obs,oldVal,newVal)->{
               row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
              });
            }*/
          
           //row.itemProperty().get()
           return row;
        });
         
        cftLeave = new ClassFilteringTable(TblCalendarEmployeeLeave.class,tableDataLeave.getTableView(),tableDataLeave.getTableView().getItems());
        ancFiltering.getChildren().clear();
        AnchorPane.setTopAnchor(cftLeave,25.0);
        AnchorPane.setBottomAnchor(cftLeave,15.0);
        //AnchorPane.setLeftAnchor(cft,15.0);
        AnchorPane.setRightAnchor(cftLeave,15.0);
        ancFiltering.getChildren().add(cftLeave);
    }
    
    private ObservableList<TblCalendarEmployeeLeave> loadAllDataLeave() {
        return FXCollections.observableArrayList(parentController.getFLeaveManager().getAllDataLeave());
    }
    
    
   private void setTableControlDataLeave() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
              showLeaveTypeDayInputDialog();
                //listener add
             // dataLeaveCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
              dataLeaveUpdateHandle();
                //listener add
             //dataOvertimeUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
               dataLeaveDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
              //dataSchedulePrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataLeave.addButtonControl(buttonControls);
    }
    
    @FXML
    private AnchorPane formAnchor;
    @FXML
    private AnchorPane ancLeaveType;
    @FXML
    private AnchorPane ancEmployeeType;
    @FXML
    private AnchorPane ancEmployeeName;
    @FXML
    private GridPane gpFormDataLeave;
    @FXML
    private AnchorPane ancFilteringSchedule;
    
    @FXML
    private ScrollPane spFormDataLeave;
    @FXML
    private AnchorPane ancDateLeave;
    @FXML
    private JFXDatePicker dpDateLeave;
    @FXML
    private AnchorPane ancStartLeave;
    @FXML
    private JFXDatePicker dpStartLeave;
    @FXML
    private JFXDatePicker dpEndLeave;
    @FXML
    private Label lblRemainLeave;
    @FXML
    private Label lblLeaveTaken;
    @FXML
    private JFXTextArea txtLeaveNote;
    @FXML
    private JFXCheckBox chbCutLeave;
    
    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;
    @FXML
    private Label lblEmployeeSchedule;
    
    @FXML
    private AnchorPane ancTblEmployeeSchedule;
    
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    
     private final PseudoClass datebeforePseudoClass = PseudoClass.getPseudoClass("dateBefore");
    
    private final PseudoClass schedulePseudoClass = PseudoClass.getPseudoClass("schedule");
    
    private final PseudoClass refreshPseudoClass = PseudoClass.getPseudoClass("refresh");
    
    private final PseudoClass leaveAnnualPseudoClass = PseudoClass.getPseudoClass("holiday");
     
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblCalendarEmployeeLeave selectedData;
    
    private final JFXCComboBoxPopup<RefEmployeeType>cbpTypeEmployee = new JFXCComboBoxPopup(RefEmployeeType.class);
    JFXCComboBoxTablePopup<TblEmployee>cbpNameEmployee;
    private final JFXCComboBoxPopup<RefEmployeeLeaveType>cbpTypeLeave = new JFXCComboBoxPopup(RefEmployeeLeaveType.class);
    private ObservableList<TblCalendarEmployeeLeave>listEmployeeLeave = FXCollections.observableArrayList();
    private ObservableList<TblCalendarEmployeeSchedule>listEmployeeSchedule = FXCollections.observableArrayList();
    private ObservableList<TblCalendarEmployeeLeave>listEmployeeAnnualLeave = FXCollections.observableArrayList();
    ClassFilteringTable cftSchedule;
    public String statusForm;
    private int amountLeaveTaken;
    private int amountLeaveRemain = 0;
    private int tempLeaveTaken;
    private boolean leaveStatus; 
    private BooleanProperty leaveSubtract = new SimpleBooleanProperty(false);
    private TableView<TblCalendarEmployeeSchedule> tblEmployeeSchedule;
    
    private void initFormDataLeave() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataLeave.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataLeave.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            //scroll end..!
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("err " + e.getMessage());
                }

            });
            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setOnAction((e) -> {
            dataScheduleSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
          dataLeaveCancelHandle();
        });
        
        initDataPopupLayout();
        
        initDateCalendar();
        
        initImportantFieldColor();
        
       
        initTableEmployeeSchedule();
        cbpNameEmployee.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
              refreshFilteringSchedule(newVal.getIdemployee());
            }
        });
    }
    
    private void initDataPopupLayout(){
        initDataPopup();
        initDataPopupEmployeeByTypeEmployee(); 
        
        ancLeaveType.getChildren().clear();   
        AnchorPane.setTopAnchor(cbpTypeLeave, 0.0);
        AnchorPane.setBottomAnchor(cbpTypeLeave, 0.0);
        AnchorPane.setLeftAnchor(cbpTypeLeave, 0.0);
        AnchorPane.setRightAnchor(cbpTypeLeave, 0.0);
        ancLeaveType.getChildren().add(cbpTypeLeave);  
        
        ancEmployeeType.getChildren().clear();   
        AnchorPane.setTopAnchor(cbpTypeEmployee, 0.0);
        AnchorPane.setBottomAnchor(cbpTypeEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpTypeEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpTypeEmployee, 0.0);
        ancEmployeeType.getChildren().add(cbpTypeEmployee);  
        
        ancEmployeeName.getChildren().clear();   
        AnchorPane.setTopAnchor(cbpNameEmployee, 0.0);
        AnchorPane.setBottomAnchor(cbpNameEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpNameEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpNameEmployee, 0.0);
        ancEmployeeName.getChildren().add(cbpNameEmployee);  
        
    }
    
    private void initTableEmployeeSchedule(){
      setTableEmployeeSchedule();
      ancTblEmployeeSchedule.getChildren().clear();
      AnchorPane.setTopAnchor(tblEmployeeSchedule,0.0);
      AnchorPane.setBottomAnchor(tblEmployeeSchedule,0.0);
      AnchorPane.setLeftAnchor(tblEmployeeSchedule,0.0);
      AnchorPane.setRightAnchor(tblEmployeeSchedule,0.0);
      ancTblEmployeeSchedule.getChildren().add(tblEmployeeSchedule); 
      /*Separator separator = new Separator();
      separator.setOrientation(Orientation.VERTICAL);
      AnchorPane.setTopAnchor(separator,15.0);
      AnchorPane.setBottomAnchor(separator,15.0);
      AnchorPane.setLeftAnchor(separator,0.0);
      AnchorPane.setRightAnchor(separator,450.0);
      AnchorPane.setTopAnchor(tblEmployeeSchedule,50.0);
      AnchorPane.setBottomAnchor(tblEmployeeSchedule,15.0);
      AnchorPane.setLeftAnchor(tblEmployeeSchedule,15.0);
      AnchorPane.setRightAnchor(tblEmployeeSchedule,15.0);
      //ancTblEmployeeSchedule.getChildren().add(lblEmployeeSchedule);*/
      //ancTblEmployeeSchedule.getChildren().add(separator);
      //ancTblEmployeeSchedule.getChildren().add(tblEmployeeSchedule);  
    }
    
    private void initDataPopup() {
        TableView<RefEmployeeType>tableEmployeeType = new TableView();
        TableColumn<RefEmployeeType,String> typeEmployee = new TableColumn<>("Tipe Karyawan");
        typeEmployee.setMinWidth(160);
        typeEmployee.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        tableEmployeeType.getColumns().addAll(typeEmployee);
        ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllEmployeeType());
        cbpTypeLeave.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
               if(newVal.getIdtype()==0){
                   for(int i = 0; i<employeeTypeItems.size();i++){
                       if(employeeTypeItems.get(i).getIdtype()==0){
                          employeeTypeItems.setAll(employeeTypeItems.get(i));
                          break;
                        }
                    }
                }
                else{
                  employeeTypeItems.setAll(loadAllEmployeeType());
                }
            }
        });
        setFunctionInitPopup(cbpTypeEmployee, tableEmployeeType,employeeTypeItems, "typeName", "Tipe Karyawan*",400,300);
        
        TableView<RefEmployeeLeaveType>tableLeaveType = new TableView();
        TableColumn<RefEmployeeLeaveType,String> typeLeave = new TableColumn<>("Tipe Cuti");
        typeLeave.setMinWidth(160);
        typeLeave.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        tableLeaveType.getColumns().addAll(typeLeave);
        ObservableList<RefEmployeeLeaveType>leaveTypeItems = FXCollections.observableArrayList(loadAllLeaveType()); 
        setFunctionInitPopup(cbpTypeLeave, tableLeaveType,leaveTypeItems, "typeName", "Tipe Cuti*",300,300);
          //setFunctionInitPopup(cbpBuilding, tableBuilding, buildingItems, "buildingName", "Building *");
        
          
       // gpFormDataLeave.add(cbpTypeEmployee, 1,3);
       // gpFormDataLeave.add(cbpTypeLeave,1,1);
    }
    
    private void initDateCalendar(){
      ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),dpDateLeave,dpStartLeave,dpEndLeave);
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpDateLeave,dpStartLeave,dpEndLeave);
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpTypeLeave, 
                dpStartLeave, 
                dpEndLeave, 
                cbpTypeEmployee, 
                cbpNameEmployee);
    }
    
    private List<RefEmployeeType>loadAllEmployeeType(){
        List<RefEmployeeType>list = new ArrayList<>();
        RefEmployeeType all = new RefEmployeeType();
        all.setTypeName("Semua Tipe Karyawan");
        all.setIdtype(3);
        list.add(all);
        list.addAll(parentController.getFLeaveManager().getAllEmployeeType());
        return list;
    }
     
    private List<RefEmployeeLeaveType>loadAllLeaveType(){
        List<RefEmployeeLeaveType>list = new ArrayList<>();
        list.addAll(parentController.getFLeaveManager().getAllLeaveType());
        return list;
    }
    
    private void initDataPopupEmployeeByTypeEmployee() {
       // System.out.println("employee Type:"+employeeType.getTypeName());
        TableView<TblEmployee>tableEmployee = new TableView();
        TableColumn<TblEmployee,String> codeEmployee = new TableColumn<>("ID");
        codeEmployee.setMinWidth(100);
        codeEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        TableColumn<TblEmployee,String>typeEmployee = new TableColumn<>("Tipe");
        typeEmployee.setMinWidth(120);
        typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
         ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeType().getTypeName(),param.getValue().refEmployeeTypeProperty()));
        TableColumn<TblEmployee,String> nameEmployee = new TableColumn<>("Nama");
        nameEmployee.setMinWidth(160);
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
         ->Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().getTblPeople().fullNameProperty()));
        TableColumn<TblEmployee,String>jobEmployee = new TableColumn("Jabatan");
        jobEmployee.setMinWidth(120);
        jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblJob().getJobName(),param.getValue().tblJobProperty()));
        tableEmployee.getColumns().addAll(codeEmployee,typeEmployee,nameEmployee,jobEmployee);
        
        ObservableList<TblEmployee>employeeItems = FXCollections.observableArrayList();
        cbpTypeEmployee.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null && cbpTypeLeave.getValue()!=null){
               if(cbpTypeLeave.valueProperty().get().getIdtype()==1){
                 employeeItems.setAll(loadAllDataEmployee(newVal)); 
                }
            }
        });
        
        cbpNameEmployee = new JFXCComboBoxTablePopup(TblEmployee.class,tableEmployee,employeeItems,"","Nama Karyawan *",true,500,300);
        //setFunctionInitPopup(cbpNameEmployee, tableEmployee,employeeItems, "tblPeople", "Nama Karyawan *",500,300);
    }
    
    private List<TblEmployee>loadAllDataEmployee(RefEmployeeType employeeType){
        List<TblEmployee>list = parentController.getFLeaveManager().getAllDataEmployee(employeeType);
        return list;
    }
    
    private void setFunctionInitPopup(JFXCComboBoxPopup cbp, TableView table,ObservableList items,String namedFilter,String promptText,double prefWidth,double prefHeight){
      
     table.getSelectionModel().selectedIndexProperty().addListener((obs,oldVal,newVal)->{
        if(newVal.intValue()!= -1)
        {
          cbp.valueProperty().set(table.getItems().get(newVal.intValue()));
          //System.out.println(newVal);
        }
        cbp.hide();
      });
      
      cbp.setPropertyNameForFiltered(namedFilter);
      cbp.setItems(items);
      
      cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });
      
      table.itemsProperty().bind(cbp.filteredItemsProperty());
      
      JFXButton button = new JFXButton("▾");
      button.setOnMouseClicked((e)->cbp.show());
      
      BorderPane content = new BorderPane(new JFXButton("SHOW"));
      
      content.setPrefSize(prefWidth,prefHeight);
      content.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
      content.setCenter(table);
      
      cbp.setPopupEditor(true);
      cbp.promptTextProperty().set(promptText);
      cbp.setLabelFloat(true);
      cbp.setPopupButton(button);
      cbp.settArrowButton(true);
      cbp.setPopupContent(content);
      
      cbp.setMinSize(USE_COMPUTED_SIZE,USE_COMPUTED_SIZE);
      cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
      cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    }
    
    private void refreshDataPopUp(){
      amountLeaveTaken = 0;
      amountLeaveRemain = 0;
     
      
      ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllEmployeeType());
      ObservableList<TblEmployee>employeeItems = FXCollections.observableArrayList();
      ObservableList<RefEmployeeLeaveType>leaveTypeItems = FXCollections.observableArrayList(loadAllLeaveType());
      
      cbpTypeLeave.setItems(leaveTypeItems);
      cbpTypeEmployee.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null && cbpTypeLeave.getValue()!=null){
               if(cbpTypeLeave.valueProperty().get().getIdtype()==1){
                 employeeItems.setAll(loadAllDataEmployee(newVal));
               }
           }
      });
      cbpNameEmployee.setItems(employeeItems);
      
      cbpTypeLeave.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
                if(newVal.getIdtype()==0){
                    for(int i = 0; i<employeeTypeItems.size();i++){
                        if(employeeTypeItems.get(i).getIdtype()==0){
                            employeeTypeItems.setAll(employeeTypeItems.get(i));
                            break;
                        }
                    }
                }
                else{
                  employeeTypeItems.setAll(loadAllEmployeeType());
                }
            }
      });
      
      cbpTypeEmployee.setItems(employeeTypeItems);
      lblLeaveTaken.textProperty().set("");
      lblRemainLeave.textProperty().set("");
      listEmployeeSchedule.setAll();
      tblEmployeeSchedule.setItems(FXCollections.observableArrayList());
    }
    
    private void refreshDataPopUpUpdate(){
     /* ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllEmployeeType());
      cbpTypeEmployee.setItems(employeeTypeItems);
      RefEmployeeType all = new RefEmployeeType();
      all.setIdtype(3);
      all.setTypeName("All");
      ObservableList<TblEmployee>employeeItems = FXCollections.observableArrayList(loadAllDataEmployee(all));
      cbpNameEmployee.setItems(employeeItems);*/
      
      amountLeaveTaken = 0;
      amountLeaveRemain = 0;
      lblLeaveTaken.textProperty().set("");
      lblRemainLeave.textProperty().set("");
    }
    
    private void refreshFilteringSchedule(long id){
      tblEmployeeSchedule.setItems(FXCollections.observableArrayList(loadAllDataSchedule(id))); 
      cftSchedule.refreshFilterItems(tblEmployeeSchedule.getItems());
    }
    
    private void refreshFilteringLeave(){
      tableDataLeave.getTableView().setItems(FXCollections.observableArrayList(loadAllDataLeave())); 
      cftLeave.refreshFilterItems(tableDataLeave.getTableView().getItems());
    }
    
      
    private void setTableEmployeeSchedule(){
        tblEmployeeSchedule = new TableView();
        
        TableColumn<TblCalendarEmployeeSchedule,String> date = new TableColumn("Tanggal");
        date.setMinWidth(90);
        date.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String> param)
                -> Bindings.createStringBinding(()->(new SimpleDateFormat("dd MMM yyyy")).format(param.getValue().getSysCalendar().getCalendarDate()), param.getValue().getSysCalendar().calendarDateProperty()));
        TableColumn<TblCalendarEmployeeSchedule,String>nameSchedule = new TableColumn("Jadwal");
        nameSchedule.setMinWidth(160);
        nameSchedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeSchedule().getScheduleName()));
        TableColumn<TblCalendarEmployeeSchedule, String>startWork = new TableColumn("Masuk");
        startWork.setMinWidth(80);
        startWork.setMaxWidth(80);
        startWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeSchedule().getBeginTime().toString(), param.getValue().getTblEmployeeByIdemployee().getTblPeople().fullNameProperty()));
        TableColumn<TblCalendarEmployeeSchedule, String>endWork = new TableColumn("Keluar");
        endWork.setMinWidth(80);
        endWork.setMaxWidth(80);
        endWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeSchedule().getEndTime().toString(), param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().typeNameProperty()));
        TableColumn<TblCalendarEmployeeSchedule,String>timeWork = new TableColumn("Jam Kerja");
        timeWork.getColumns().addAll(startWork,endWork);
        tblEmployeeSchedule.getColumns().addAll(date,nameSchedule,timeWork);
        cbpNameEmployee.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
              tblEmployeeSchedule.setItems(FXCollections.observableArrayList(loadAllDataSchedule(newVal.getIdemployee()))); 
           }
           else{
             tblEmployeeSchedule.setItems(null);
           }
          
          /* if(newVal!=null){
              if(statusForm.equalsIgnoreCase("one day")){
                dpDateLeave.valueProperty().addListener((obsDateLeave,oldDateleave,newDateLeave)->{
                   if(newDateLeave!=null){
                     listEmployeeSchedule.setAll(loadAllDataSchedule(newVal.getIdemployee(),statusForm,Date.valueOf(newDateLeave),null,null));
                   }
                });
              }
              else{
                dpStartLeave.valueProperty().addListener((obsStartLeave,oldStartLeave,newStartLeave)->{
                  dpEndLeave.valueProperty().addListener((obsEndLeave,oldEndLeave,newEndLeave)->{
                     if(newStartLeave!=null && newEndLeave!=null){
                       listEmployeeSchedule.setAll(parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployee(newVal.getIdemployee(),statusForm,null,Date.valueOf(newStartLeave),Date.valueOf(newEndLeave))); 
                     }       
           });
         });
              }
           }*/
        });
        
        tblEmployeeSchedule.setRowFactory(tv->{
            TableRow<TblCalendarEmployeeSchedule>rowSchedule = new TableRow<>();
            
            rowSchedule.itemProperty().addListener((obs,oldVal,newVal)->{
               
                if(newVal!=null){
                    List<TblCalendarEmployeeLeave>list = parentController.getFLeaveManager().getAllDataLeaveByDateAndIdEmployee(newVal.getSysCalendar(),newVal.getTblEmployeeByIdemployee());
                    boolean foundLeave = false;
                    boolean foundLeaveAnnual = false;
                    for(TblCalendarEmployeeLeave getEmployeeLeave : list){
                       if(getEmployeeLeave.getSysCalendar().getIdcalendar()==newVal.getSysCalendar().getIdcalendar() && getEmployeeLeave.getTblEmployeeByIdemployee().getIdemployee()==newVal.getTblEmployeeByIdemployee().getIdemployee() && getEmployeeLeave.getRefEmployeeLeaveType().getIdtype()==1){
                         foundLeave = true;
                       }
                       else if(getEmployeeLeave.getSysCalendar().getIdcalendar()==newVal.getSysCalendar().getIdcalendar() && getEmployeeLeave.getTblEmployeeByIdemployee().getIdemployee()==newVal.getTblEmployeeByIdemployee().getIdemployee() && getEmployeeLeave.getRefEmployeeLeaveType().getIdtype()==0){
                         foundLeaveAnnual = true;
                       }
                      //rowSchedule.pseudoClassStateChanged(refreshPseudoClass,getEmployeeLeave.getSysCalendar().getIdcalendar()!=newVal.getSysCalendar().getIdcalendar() && getEmployeeLeave.getTblEmployeeByIdemployee().getIdemployee()!=newVal.getTblEmployeeByIdemployee().getIdemployee());          
                     
                    }
                    
                     rowSchedule.pseudoClassStateChanged(schedulePseudoClass,foundLeave);
                     rowSchedule.pseudoClassStateChanged(refreshPseudoClass,!foundLeave);          
                     rowSchedule.pseudoClassStateChanged(leaveAnnualPseudoClass,foundLeaveAnnual);
                     rowSchedule.pseudoClassStateChanged(refreshPseudoClass,!foundLeaveAnnual);   
                    /*for(int i = 0; i<tableDataLeave.getTableView().getItems().size();i++){
                        row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getIdcalendar()==newVal.getSysCalendar().getIdcalendar() && ((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getTblEmployeeByIdemployee().getIdemployee()==newVal.getTblEmployeeByIdemployee().getIdemployee()); 
                    }*/
                  //System.out.println(">>"+newVal.getTblEmployeeByIdemployee().getCodeEmployee());
                }
         
                 rowSchedule.pseudoClassStateChanged(refreshPseudoClass,newVal==null);
              
               //row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
              });
            /*for(int i = 0; i<tableDataLeave.getTableView().getItems().size();i++){
              row.itemProperty().addListener((obs,oldVal,newVal)->{
               row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
              });
            }*/
          
           //row.itemProperty().get()
           return rowSchedule;
        });
        
        cftSchedule = new ClassFilteringTable<>(TblCalendarEmployeeSchedule.class,tblEmployeeSchedule,tblEmployeeSchedule.getItems());
        ancFilteringSchedule.getChildren().clear();
        AnchorPane.setTopAnchor(cftSchedule,25.0);
        AnchorPane.setBottomAnchor(cftSchedule,15.0);
        AnchorPane.setRightAnchor(cftSchedule,15.0);
        ancFilteringSchedule.getChildren().add(cftSchedule);
    }
    
    private List<TblCalendarEmployeeSchedule> loadAllDataSchedule(long id){
       List<TblCalendarEmployeeSchedule>listEmployeeSchedule = new ArrayList();
       listEmployeeSchedule= parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployee(id);
       //List<TblCalendarEmployeeSchedule>listEmployeeScheduleSort = parentController.getFLeaveManager().sortEmployeeSchedule(listEmployeeSchedule);
       return listEmployeeSchedule;
    }
    
    private void refreshInputForm(){
      cbpTypeEmployee.setValue(null);
      cbpNameEmployee.setValue(null);
      tblEmployeeSchedule.setItems(null);
      cbpTypeEmployee.setValue(null);
      cbpNameEmployee.setValue(null);
      dpDateLeave.setValue(null);
      dpStartLeave.setValue(null);
      dpEndLeave.setValue(null);
      tempLeaveTaken = 0;
      amountLeaveTaken = 0;
      lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf("")));
      lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf("")));
    }
    
    private void setSelectedDataToInputForm(){
        refreshDataPopUp();
        refreshInputForm();
        cbpTypeEmployee.setDisable(false);
        cbpNameEmployee.setDisable(false);
        cbpNameEmployee.setVisible(false);
          
          //cbpNameEmployee.setValue(null);
          cbpTypeLeave.setDisable(false);
          
          ObjectProperty<Date>dateLeave = new SimpleObjectProperty();
          
        cbpTypeLeave.valueProperty().bindBidirectional(selectedData.refEmployeeLeaveTypeProperty());
        cbpTypeLeave.valueProperty().addListener((obs,oldVal,newVal)->{
            if(newVal!=null && dataInputStatus!=3){
                if(dataInputStatus==0){
                   refreshInputForm();
                
                   if(newVal.getIdtype()==0){
                     chbCutLeave.setDisable(true);
                     chbCutLeave.selectedProperty().set(Boolean.TRUE);
                     cbpNameEmployee.setVisible(false);
               
                     lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf("")));
                     //lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf("")));
                    }
                    else{
                     chbCutLeave.setDisable(false);
                     chbCutLeave.selectedProperty().set(Boolean.FALSE);
                     cbpNameEmployee.setVisible(true);
                     lblLeaveTaken.setText(String.valueOf(tempLeaveTaken));
                    }
                }
            }
        });
         
        cbpTypeEmployee.valueProperty().addListener((obs,oldTypeEmployee,newTypeEmployee)->{
           if(newTypeEmployee!=null && dataInputStatus==0 && cbpTypeLeave.getValue()!=null){
              cbpNameEmployee.setValue(null);
               if(cbpTypeLeave.getValue().getIdtype()!=0){
                   if(newTypeEmployee.getIdtype()!=0){
                      chbCutLeave.setDisable(true);
                      chbCutLeave.selectedProperty().set(false);
                      lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(""));
                    }
                   else{
                     chbCutLeave.setDisable(false);
                     chbCutLeave.selectedProperty().set(true);
                    } 
                }
               else{
                   if(statusForm.equalsIgnoreCase("one day")){
                       if(dpDateLeave.getValue()!=null && dpStartLeave.getValue()==null && dpEndLeave.getValue()==null){
                            listEmployeeAnnualLeave.setAll(getAllSysCalendarEmployeeAnnualLeave(Date.valueOf(dpDateLeave.getValue()),null,null,newTypeEmployee));
                            checkEmployeeAnnualSchedule(listEmployeeAnnualLeave);
                            for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeAnnualLeave){
                              System.out.println("cut leave:"+getEmployeeLeave.getCutLeaveStatus());
                            }
                        }
                    }
                    else{
                        if(dpDateLeave.getValue()==null && dpStartLeave.getValue()!=null && dpEndLeave.getValue()!=null){
                            listEmployeeAnnualLeave.setAll(getAllSysCalendarEmployeeAnnualLeave(null,Date.valueOf(dpStartLeave.getValue()),Date.valueOf(dpEndLeave.getValue()),newTypeEmployee));
                            checkEmployeeAnnualSchedule(listEmployeeAnnualLeave);
                            for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeAnnualLeave){
                              System.out.println("cut leave:"+getEmployeeLeave.getCutLeaveStatus());
                            }
                        }
                    }
                }
            }
        });
        
        cbpNameEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());
        cbpNameEmployee.valueProperty().addListener((obs,oldNameEmployee,newNameEmployee)->{
            if(newNameEmployee!=null && cbpTypeEmployee.getValue()!=null && cbpTypeLeave!=null && dataInputStatus!=3){
                
                if(newNameEmployee.getRefEmployeeType().getIdtype()!=0){
                  chbCutLeave.setDisable(true);
                  chbCutLeave.selectedProperty().set(false);
                  amountLeaveRemain = 0;
                  lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(""));  
                }
                else{
                  chbCutLeave.setDisable(false);
                  chbCutLeave.selectedProperty().set(true);
                  amountLeaveRemain = newNameEmployee.getCurrentLeaveAmount();
                  lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveRemain))); 
                }
                
                if(statusForm.equalsIgnoreCase("one day")){
                    if(dpDateLeave.getValue()!=null){
                      boolean check= checkEmployeeSchedule(Date.valueOf(dpDateLeave.getValue()),newNameEmployee,selectedData);
                       if(check==true){
                          amountLeaveTaken = 1;
                          lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                        }
                        else{
                         amountLeaveTaken = 0;
                         lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                        }
                        tempLeaveTaken = amountLeaveTaken;
                    }
                }
                else{
                   if(dpStartLeave.getValue()!=null && dpEndLeave.getValue()!=null){
                     amountLeaveTaken = checkAllEmployeeLeave(Date.valueOf(dpStartLeave.getValue()),Date.valueOf(dpEndLeave.getValue()),newNameEmployee);
                     tempLeaveTaken = amountLeaveTaken;
                     lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                   }
                }
            }
        });
        
         dpStartLeave.setValue(null);
         dpEndLeave.setValue(null);
         dpDateLeave.setValue(null);
         
        if(statusForm.equalsIgnoreCase("one day")){
          ancDateLeave.setVisible(true);
          ancStartLeave.setVisible(false);
          dpEndLeave.setVisible(false);
          
          dpDateLeave.valueProperty().addListener((obsDate,oldDateLeave,newDateLeave)->{
           if(newDateLeave!=null && cbpTypeLeave.getValue()!=null && dataInputStatus!=3){
               if(cbpTypeLeave.valueProperty().get().getIdtype()==0){
                   if(dpStartLeave.getValue()==null && dpEndLeave.getValue()==null && cbpTypeEmployee!=null){
                      listEmployeeAnnualLeave.setAll(getAllSysCalendarEmployeeAnnualLeave(Date.valueOf(newDateLeave),null,null,cbpTypeEmployee.getValue()));
                      checkEmployeeAnnualSchedule(listEmployeeAnnualLeave);
                       for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeAnnualLeave){
                          System.out.println("cut leave:"+getEmployeeLeave.getCutLeaveStatus());
                       }
                    }
                  amountLeaveTaken = 1;
                  lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                }
                else{
                  boolean check= checkEmployeeSchedule(Date.valueOf(newDateLeave),cbpNameEmployee.getValue(),selectedData);
                   if(check==true){
                      amountLeaveTaken = 1;
                      lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                    }
                   else{
                      amountLeaveTaken = 0;
                      selectedData.setSysCalendar(null);
                      lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                    }
                }
                //dateLeave.set(Date.valueOf(newDateLeave));
            }
            tempLeaveTaken = amountLeaveTaken;
          });  
        }
        else{
          ancStartLeave.setVisible(true);
          ancDateLeave.setVisible(false);
          dpEndLeave.setVisible(true);
          
          dpStartLeave.valueProperty().addListener((obs,oldStartDate,newStartDate)->{
              if(newStartDate!=null && dpEndLeave.getValue()!=null && dataInputStatus!=3){
                   if(cbpTypeLeave.getValue().getIdtype()==0){
                       if(dpDateLeave==null && cbpTypeEmployee.getValue()!=null){
                          listEmployeeAnnualLeave.setAll(getAllSysCalendarEmployeeAnnualLeave(null,Date.valueOf(newStartDate),Date.valueOf(dpEndLeave.getValue()),cbpTypeEmployee.getValue()));
                          checkEmployeeAnnualSchedule(listEmployeeAnnualLeave);
                          int count = 0;
                           for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeAnnualLeave){
                            System.out.println("cut leave:"+getEmployeeLeave.getCutLeaveStatus());
                            }
                        }
                    } 
                amountLeaveTaken = checkAllEmployeeLeave(Date.valueOf(newStartDate),Date.valueOf(dpEndLeave.getValue()),cbpNameEmployee.getValue());
                lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));  
              }
              tempLeaveTaken = amountLeaveTaken;
          });
          
          dpEndLeave.valueProperty().addListener((obs,oldEndDate,newEndDate)->{
             if(newEndDate!=null && dpStartLeave.getValue()!=null && dataInputStatus!=3){
                 if(cbpTypeLeave.getValue().getIdtype()==0){
                       if(dpDateLeave==null && cbpTypeEmployee.getValue()!=null){
                          listEmployeeAnnualLeave.setAll(getAllSysCalendarEmployeeAnnualLeave(null,Date.valueOf(dpStartLeave.getValue()),Date.valueOf(newEndDate),cbpTypeEmployee.getValue()));
                          checkEmployeeAnnualSchedule(listEmployeeAnnualLeave);
                        }
                    } 
                 amountLeaveTaken = checkAllEmployeeLeave(Date.valueOf(dpStartLeave.getValue()),Date.valueOf(newEndDate),cbpNameEmployee.getValue());
                  //amountLeaveTaken = checkAllEmployeeLeave(Date.valueOf(newStartDate),Date.valueOf(dpEndLeave.getValue()),cbpNameEmployee.getValue());
                //amountLeaveTaken = checkAllEmployeeLeave(Date.valueOf(dpStartLeave.getValue()),Date.valueOf(newEndDate),cbpNameEmployee.getValue());
                lblLeaveTaken.setText(String.valueOf(amountLeaveTaken)); 
             }
             tempLeaveTaken = amountLeaveTaken;
          });
        }
        
        chbCutLeave.selectedProperty().bindBidirectional(selectedData.cutLeaveStatusProperty());
         
        chbCutLeave.selectedProperty().addListener((obsCutLeave,oldCutLeave,newCutLeave)->{
           if(newCutLeave!=null && dataInputStatus!=3){
               if(newCutLeave.booleanValue()==false){
                  tempLeaveTaken = 0;
                  lblLeaveTaken.setText(String.valueOf(tempLeaveTaken));
                }
               else{
                  tempLeaveTaken = amountLeaveTaken;
                  lblLeaveTaken.setText(String.valueOf(tempLeaveTaken));
              //chbCutLeave.selectedProperty().bindBidirectional(selectedData.cutLeaveStatusProperty());
                }
            }
        });
        
        /*cbpTypeEmployee.valueProperty().addListener((obs,oldTypeEmployee,newTypeEmployee)->{
           if(newTypeEmployee!=null){
              if(selectedData.getRefEmployeeLeaveType().getIdtype()!=0){
                 if(newTypeEmployee.getIdtype()!=0 || newTypeEmployee.getIdtype()!=3){
                   chbCutLeave.setDisable(true);
                   chbCutLeave.selectedProperty().set(false);
                   lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(""));
                }
                 else{
                  chbCutLeave.setDisable(false);
                  chbCutLeave.selectedProperty().set(false);
                } 
              }
           }
        });
        
        cbpNameEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());
        cbpNameEmployee.valueProperty().addListener((obs,oldVal,newVal)->{
            if(newVal!=null){
                if(newVal.getRefEmployeeType().getIdtype()!=0){
                  chbCutLeave.setDisable(true);
                  chbCutLeave.selectedProperty().set(false);
                  lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(""));
                }
                else{
                  chbCutLeave.setDisable(false);
                  chbCutLeave.selectedProperty().set(false);
                  amountLeaveRemain = newVal.getCurrentLeaveAmount();
                  lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveRemain)));
                }
                //listEmployeeSchedule = new ArrayList();
                
                //listEmployeeSchedule.setAll(loadAllDataSchedule(newVal.getIdemployee(),statusForm,Date.valueOf(dpDateLeave.valueProperty().get()),Date.valueOf(dpStartLeave.valueProperty().get()),Date.valueOf(dpEndLeave.valueProperty().get())));
            }
        });
        //txtScheduleName.textProperty().bindBidirectional(selectedData.scheduleNameProperty());
        
         dpStartLeave.setValue(null);
         dpEndLeave.setValue(null);
         dpDateLeave.setValue(null);
        
        if(statusForm.equalsIgnoreCase("one day")){
          ancDateLeave.setVisible(true);
          ancStartLeave.setVisible(false);
          dpEndLeave.setVisible(false);
         
          dpDateLeave.valueProperty().addListener((obsDateLeave,oldDateLeave,newDateLeave)->{
            if(newDateLeave!=null){
                if(cbpTypeLeave.valueProperty().get().getIdtype()==0){
                  amountLeaveTaken = 1;
                }
                else{
                    if(selectedData.getTblEmployeeByIdemployee()!=null){
                        boolean check =  checkEmployeeSchedule(Date.valueOf(newDateLeave),selectedData);
                        if(check==true){
                           amountLeaveTaken = 1;
                            System.out.println("hsl date>>"+selectedData.getSysCalendar().getCalendarDate());
                           //amountLeaveRemain = amountLeaveRemain-amountLeaveTaken;
                        }
                        else{
                          amountLeaveTaken = 0;
                          selectedData.setSysCalendar(null);
                          //amountLeaveRemain = amountLeaveRemain-amountLeaveTaken;
                        }
                    } 
                }
              lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)));
            }
                
          });
        }
        else{
           ancStartLeave.setVisible(true);
           ancDateLeave.setVisible(false);
           dpEndLeave.setVisible(true);
           
            dpStartLeave.valueProperty().addListener((obsStartLeave,oldStartLeave,newStartLeave)->{
                dpEndLeave.valueProperty().addListener((obsEndLeave,oldEndLeave,newEndLeave)->{
                    if(newStartLeave!=null && newEndLeave!=null){
                        if(cbpTypeLeave.valueProperty().get().getIdtype()==0){
                          amountLeaveTaken = (int)(long)ChronoUnit.DAYS.between(newStartLeave,newEndLeave)+1;
                        }
                        else{
                          amountLeaveTaken = 0;
                          for(TblCalendarEmployeeSchedule getEmployeeSchedule:listEmployeeSchedule){
                            boolean found = false;
                            for(Date date=Date.valueOf(newStartLeave);!date.after(Date.valueOf(newEndLeave));date=Date.valueOf(date.toLocalDate().plusDays(1))){
                                if(date.equals(getEmployeeSchedule.getSysCalendar().getCalendarDate())){
                                    found = true;
                                }   
                            }
                            
                            if(found==true){
                                amountLeaveTaken++;
                            }
                          }
                        }
                    lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)));
                }
                   
                });
            });
        }
        
        chbCutLeave.selectedProperty().bindBidirectional(selectedData.cutLeaveStatusProperty());
        chbCutLeave.selectedProperty().addListener((obsCutLeave,oldCutLeave,newCutLeave)->{
           if(newCutLeave!=null){
            if(newCutLeave.booleanValue()==false){
              tempLeaveTaken = 0;
              lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(tempLeaveTaken)));
            }
            else{
              tempLeaveTaken = amountLeaveTaken;
              lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(tempLeaveTaken)));
            }
          }
        });
        
        cbpTypeLeave.valueProperty().bindBidirectional(selectedData.refEmployeeLeaveTypeProperty());
        cbpTypeLeave.valueProperty().addListener((obs,oldVal,newVal)->{
          if(newVal!=null){
            if(newVal.getIdtype()==0){
              chbCutLeave.setDisable(true);
              chbCutLeave.selectedProperty().set(true);
              cbpNameEmployee.setVisible(false);
              lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf("")));
              lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf("")));
            }
            else{
              chbCutLeave.setDisable(false);
              chbCutLeave.selectedProperty().set(false);
              cbpNameEmployee.setVisible(true);
              lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(tempLeaveTaken)));
            }
          }
          
        });*/
        
       
        cbpTypeEmployee.hide();
        cbpNameEmployee.hide();
        txtLeaveNote.textProperty().bindBidirectional(selectedData.noteProperty());
        
        //selectedDataToFormFunctionalComponent();
    }
    
    private List<TblCalendarEmployeeLeave>getAllSysCalendarEmployeeAnnualLeave(Date date,Date startDate,Date endDate,RefEmployeeType employeeType){
        List<TblCalendarEmployeeLeave>listEmployeeAnnualLeave = new ArrayList();
        if(statusForm=="one day"){
           if(date!=null && employeeType!=null){
             List<TblEmployee>listEmployee = parentController.getFLeaveManager().getAllDataEmployee(employeeType);
             List<SysCalendar>listCalendar = parentController.getFLeaveManager().getAllSysCalendar(date,null,null, statusForm);
               for(SysCalendar getCalendar : listCalendar){
                   for(TblEmployee getEmployee : listEmployee){
                     TblCalendarEmployeeLeave employeeLeave = new TblCalendarEmployeeLeave();
                     employeeLeave.setSysCalendar(getCalendar);
                     employeeLeave.setTblEmployeeByIdemployee(getEmployee);
                     employeeLeave.setCutLeaveStatus(chbCutLeave.isSelected()); 
                     employeeLeave.setRefEmployeeLeaveType(selectedData.getRefEmployeeLeaveType());
                     //employeeLeave.setNote(selectedData.getNote());
                     listEmployeeAnnualLeave.add(employeeLeave);
                    }
                } 
            }
        }
       else{
           if(startDate!=null && endDate!=null){
             List<TblEmployee>listEmployee = parentController.getFLeaveManager().getAllDataEmployee(employeeType);
             List<SysCalendar>listCalendar = parentController.getFLeaveManager().getAllSysCalendar(null,startDate,endDate, statusForm);
               for(SysCalendar getCalendar : listCalendar){
                   for(TblEmployee getEmployee : listEmployee){
                     TblCalendarEmployeeLeave employeeLeave = new TblCalendarEmployeeLeave();
                     employeeLeave.setSysCalendar(getCalendar);
                     employeeLeave.setTblEmployeeByIdemployee(getEmployee);
                     employeeLeave.setCutLeaveStatus(chbCutLeave.isSelected()); 
                     employeeLeave.setRefEmployeeLeaveType(selectedData.getRefEmployeeLeaveType());
                     //employeeLeave.setNote(selectedData.getNote());
                     listEmployeeAnnualLeave.add(employeeLeave);
                    }
               } 
            }
        }
        return listEmployeeAnnualLeave;
    }
    
    private void checkEmployeeAnnualSchedule(List<TblCalendarEmployeeLeave>listEmployeeAnnounceLeave){
       for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeAnnounceLeave){
          List<TblCalendarEmployeeSchedule>listEmployeeSchedule = parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployeeAndDate(getEmployeeLeave.getTblEmployeeByIdemployee().getIdemployee(),(java.sql.Date)getEmployeeLeave.getSysCalendar().getCalendarDate());//getAllDataEmployeeScheduleByIdEmployee(getEmployeeLeave.getTblEmployeeByIdemployee().getIdemployee()));
            if(listEmployeeSchedule.size()==0){
             getEmployeeLeave.setCutLeaveStatus(Boolean.FALSE);
             //amountLeaveTaken = 0;
            }
            else if(getEmployeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount()==0){
              getEmployeeLeave.setCutLeaveStatus(Boolean.FALSE);
              //amountLeaveTaken = 0;
            }
            //System.out.println("size:"+listEmployeeSchedule.size());
        }
    }
    
    private boolean checkEmployeeSchedule(Date date,TblEmployee employee,TblCalendarEmployeeLeave employeeLeave){
        boolean found = false;
       
       if(date!=null && employee!=null){
            ObservableList<TblCalendarEmployeeSchedule>listEmployeeSchedule = FXCollections.observableArrayList(parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployee(employee.getIdemployee()));
            for(TblCalendarEmployeeSchedule getEmployeeSchedule:listEmployeeSchedule){
               if(getEmployeeSchedule.getSysCalendar().getCalendarDate().equals(date)){
                   if(cbpTypeLeave.getValue().getIdtype()==0){
                     found = true;
                    }
                   else{
                     employeeLeave.setSysCalendar(getEmployeeSchedule.getSysCalendar());
                     found=true; 
                    }
                }
            }
           /* for(int i = 0; i<tblEmployeeSchedule.getItems().size();i++){
                if(tblEmployeeSchedule.getItems().get(i).getSysCalendar().getCalendarDate().equals(date)){
                   employeeLeave.setSysCalendar(tblEmployeeSchedule.getItems().get(i).getSysCalendar());
                   found=true;
                }  
                     // listEmployeeSchedule.setAll(parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployee(newEmployee.getIdemployee()));
            }*/
        }
        System.out.println("date>>"+date);
        //System.out.println("karyawan:"+employeeLeave.getTblEmployeeByIdemployee().getCodeEmployee());
        
       

       
        /* if(statusForm.equalsIgnoreCase("one day")){
           listEmployeeSchedule.setAll(parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployee(employeeLeave.getTblEmployeeByIdemployee().getIdemployee(),statusForm,Date.valueOf(dpDateLeave.valueProperty().get()),null,null));
        }
        else{
          listEmployeeSchedule.setAll(parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployee(employeeLeave.getTblEmployeeByIdemployee().getIdemployee(),statusForm,null,Date.valueOf(dpStartLeave.valueProperty().get()),Date.valueOf(dpEndLeave.valueProperty().get())));
        }
        //loadAllDataSchedule(employeeLeave.getTblEmployeeByIdemployee().getIdemployee(),statusForm);//parentController.getFLeaveManager().getAllDataEmployeeScheduleByIdEmployee(employeeLeave.getTblEmployeeByIdemployee().getIdemployee());
        for(TblCalendarEmployeeSchedule getEmployeeSchedule:listEmployeeSchedule){
            System.out.println("get Employee Schedule:"+getEmployeeSchedule.getSysCalendar().getCalendarDate());
           if(getEmployeeSchedule.getSysCalendar().getCalendarDate().equals(date)){
                found  = true;
                employeeLeave.setSysCalendar(getEmployeeSchedule.getSysCalendar());
                System.out.println("Ketemu");
                break;
            }
        }*/
        return found;
    }
    
    
    private int checkAllEmployeeLeave(Date startDate,Date endDate,TblEmployee employee){
        List<TblCalendarEmployeeLeave>listTempEmployeeLeave = new ArrayList();
        int count = 0;
        //boolean found = false;
        //System.out.println("hsl cut leave:"+selectedData.getCutLeaveStatus());
        if(cbpTypeLeave.getValue().getIdtype()==0){
            if(startDate!=null && endDate!=null){
              count = (int)(long)ChronoUnit.DAYS.between(startDate.toLocalDate(),endDate.toLocalDate())+1;
            }
        }
        else{
           if(startDate!=null && endDate!=null && employee!=null){
                for(int i = 0; i<tblEmployeeSchedule.getItems().size();i++){
                    boolean found = false; 
                    for(Date date=startDate;!date.after(endDate);date=Date.valueOf(date.toLocalDate().plusDays(1))){
                        if(tblEmployeeSchedule.getItems().get(i).getSysCalendar().getCalendarDate().equals(date)){
                          TblCalendarEmployeeLeave getEmployeeLeave = new TblCalendarEmployeeLeave();
                          getEmployeeLeave.setSysCalendar(tblEmployeeSchedule.getItems().get(i).getSysCalendar());
                          getEmployeeLeave.setTblEmployeeByIdemployee(employee);
                          getEmployeeLeave.setRefEmployeeLeaveType(selectedData.getRefEmployeeLeaveType());
                          //getEmployeeLeave.setCutLeaveStatus(selectedData.getCutLeaveStatus());
                          listTempEmployeeLeave.add(getEmployeeLeave);
                          listEmployeeLeave.setAll(listTempEmployeeLeave);
                          found = true;
                        }
                    }
                    
                    if(found){
                      count++;
                    }
                }
            }
        }
        
        /*for(TblCalendarEmployeeSchedule employeeSchedule:listEmployeeSchedule){
            for(Date date=Date.valueOf(dpStartLeave.getValue());!date.after(Date.valueOf(dpEndLeave.getValue()));date=Date.valueOf(date.toLocalDate().plusDays(1))){
                if(employeeSchedule.getSysCalendar().getCalendarDate().equals(date)){
                  TblCalendarEmployeeLeave getEmployeeLeave = new TblCalendarEmployeeLeave();
                  getEmployeeLeave.setSysCalendar(employeeSchedule.getSysCalendar());
                  getEmployeeLeave.setTblEmployeeByIdemployee(selectedData.getTblEmployeeByIdemployee());
                  getEmployeeLeave.setRefEmployeeLeaveType(selectedData.getRefEmployeeLeaveType());
                  getEmployeeLeave.setCutLeaveStatus(selectedData.getCutLeaveStatus().booleanValue());
                  getEmployeeLeave.setNote(selectedData.getNote());
                  listTempEmployeeLeave.add(getEmployeeLeave);
                  listEmployeeLeave.setAll(listTempEmployeeLeave);
                } 
            }
        }*/
        return count;
    }
    
   private void setSelectedDataToUpdateForm(){
        refreshDataPopUpUpdate();
        //lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(0)));  
        dpDateLeave.valueProperty().set(((java.sql.Date)selectedData.getSysCalendar().getCalendarDate()).toLocalDate());
        ObjectProperty<Date>dateUpdate = new SimpleObjectProperty();
        BooleanProperty check = new SimpleBooleanProperty();
       
        cbpTypeLeave.valueProperty().bindBidirectional(selectedData.refEmployeeLeaveTypeProperty());
        cbpTypeLeave.setDisable(true);
        cbpTypeEmployee.setDisable(true);
        if(selectedData.getTblEmployeeByIdemployee()!=null){
           cbpTypeEmployee.setValue(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType());
           cbpTypeEmployee.setDisable(true);
        }
        
        if(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType().getIdtype()!=0){
           cbpTypeLeave.setDisable(true);
        }
        
        ancStartLeave.setVisible(false);
        dpEndLeave.setVisible(false);
        ancDateLeave.setVisible(true);
        //dpDateLeave.setVisible(true);
        cbpNameEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());
        System.out.println("hsl>>"+selectedData.getTblEmployeeByIdemployee().getCodeEmployee());
        
        lblRemainLeave.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount())));
        
        cbpNameEmployee.setDisable(true);
        //dateUpdate.set((java.sql.Date)selectedData.getSysCalendar().getCalendarDate());
        //check.set(checkEmployeeSchedule(listEmployeeSchedule,Date.valueOf(dpDateLeave.getValue()),selectedData));
        dpDateLeave.valueProperty().addListener((obs,oldDate,newDate)->{
            if(newDate!=null && dataInputStatus !=3){
                if(selectedData.getRefEmployeeLeaveType().getIdtype()==0){
                  amountLeaveTaken = 0;
                  
                  lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));  
                }
               else{
                    boolean checkDate = checkEmployeeSchedule(Date.valueOf(newDate),selectedData.getTblEmployeeByIdemployee(),selectedData);
                    if(checkDate==true){
                     amountLeaveTaken =  1;
                     lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));  
                    }
                    else{
                     amountLeaveTaken = 0;
                     selectedData.setSysCalendar(null);
                     lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));  
                    }
                }
            }
        });
        
       
        chbCutLeave.selectedProperty().bindBidirectional(selectedData.cutLeaveStatusProperty());
        //cbpTypeLeave.setDisable(true);
       

       
        
        tempLeaveTaken = amountLeaveTaken;
        
        chbCutLeave.selectedProperty().addListener((obs,oldLeaveStatus,newLeaveStatus)->{
           if(dpDateLeave.getValue()!=null && dataInputStatus!=3){
               if(selectedData.getRefEmployeeLeaveType().getIdtype()!=0){
                   boolean checkDate = checkEmployeeSchedule(Date.valueOf(dpDateLeave.getValue()),selectedData.getTblEmployeeByIdemployee(),selectedData);
              
                   if(leaveStatus==false){
                       if(newLeaveStatus.booleanValue()==false){
                           if(checkDate==true){
                              amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                              amountLeaveTaken = 0;
                              lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                            }
                           else{
                             selectedData.setSysCalendar(null);
                             amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                             amountLeaveTaken = 0;
                             lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                            }
                        }
                        else{
                           if(checkDate==true){
                             amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-1;
                             amountLeaveTaken = 1;
                             leaveSubtract.setValue(Boolean.TRUE);
                             lblLeaveTaken.setText(String.valueOf(amountLeaveTaken)+"(Jumlah cuti dikurangi)");
                            }
                           else{
                             selectedData.setSysCalendar(null);
                             //amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                             amountLeaveTaken = 0;
                             lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                            }
                        }
                    }
                   else{
                       if(newLeaveStatus.booleanValue()==false){
                           if(checkDate==true){
                             amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()+1;
                             amountLeaveTaken = 1;
                             lblLeaveTaken.setText(String.valueOf(amountLeaveTaken)+"(Jumlah cuti ditambah)");
                            }
                           else{
                              selectedData.setSysCalendar(null);
                             //amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                             amountLeaveTaken = 0;
                             lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                            }
                        }
                       else{
                           if(checkDate==true){
                             amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                             amountLeaveTaken = 0;
                             lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                            }
                           else{
                             selectedData.setSysCalendar(null);
                             //amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                             amountLeaveTaken = 0;
                             lblLeaveTaken.setText(String.valueOf(amountLeaveTaken));
                            }
                        }  
                    }
                }
            }
        });
        
        if(dataInputStatus==3){
            lblLeaveTaken.setText(String.valueOf(selectedData.getTblEmployeeByIdemployee().getBaseLeaveAmount()-selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()));
        }
        /*chbCutLeave.selectedProperty().addListener((obs,oldLeaveStatus,newLeaveStatus)->{
            if(selectedData.getRefEmployeeLeaveType().getIdtype()==0){
              amountLeaveTaken = 0;
            }
            else{
              boolean checkDate = checkEmployeeSchedule(dateUpdate.get(),selectedData.getTblEmployeeByIdemployee(),selectedData);
                if(leaveStatus==false){
                    if(newLeaveStatus.booleanValue()==false){
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti tidak dikurangi)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                    }
                    else{
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-1;
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti dikurangi)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                    }
                }
                else{
                    if(newLeaveStatus.booleanValue()==false){
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()+1;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti ditambah)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                    }
                    else{
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti tidak dikurangi)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                    }
                }
            }
            
        });
        /*cbpTypeLeave.valueProperty().addListener((obsTypeLeave,oldTypeLeave,newTypeLeave)->{
                System.out.println("hsl date:"+dateUpdate.get());
                boolean checkDate = checkEmployeeSchedule(dateUpdate.get(),selectedData);
                System.out.println("hsl type leave:"+selectedData.getRefEmployeeLeaveType().getTypeName());
                if(typeLeave.getIdtype()==0){
                    if(newTypeLeave.getIdtype()==0){
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti tidak dikurangi)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                     /* amountLeaveTaken = 0;
                      amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getLeaveAmount();
                      lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(jumlah cuti tidak dikurangi)"));
                    }
                    else{
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-1;
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti dikurangi)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                    }
                }
                else{
                    if(newTypeLeave.getIdtype()==0){
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()+1;
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti ditambah)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                    }
                    else{
                        if(checkDate==true){
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(Jumlah cuti tidak dikurangi)"));
                        }
                        else{
                          selectedData.setSysCalendar(null);
                          amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount();
                          amountLeaveTaken = 0;
                          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                        }
                      /*amountLeaveTaken = 0;
                      amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getLeaveAmount();
                      lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(jumlah cuti tidak dikurangi)")); */
                    //}
                //}
                
               /* if(newTypeLeave.getIdtype()==0){
                if(checkDate==true){
                 amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getLeaveAmount()+1;
                 amountLeaveTaken = 0;
                 lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(cuti ditambah)"));
                }
                else{
                 selectedData.setSysCalendar(null);
                 amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getLeaveAmount();
                 amountLeaveTaken = 0;
                 lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
               }
            }
          else{
                if(checkDate==true){
                 amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getLeaveAmount()-1;
                 amountLeaveTaken = 1;
                 lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(cuti dikurangi)"));
                }
                else{
                 selectedData.setSysCalendar(null);
                 amountLeaveRemain = selectedData.getTblEmployeeByIdemployee().getLeaveAmount();
                 amountLeaveTaken = 0;
                 lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(libur)"));
                } 
          }*/
       // });
        
        /*dpDateLeave.valueProperty().addListener((obs,oldVal,newVal)->{
            System.out.println(">>"+newVal);
            
                if(newVal!=null){
                cbpTypeLeave.valueProperty().addListener((obsTypeLeave,oldTypeLeave,newTypeLeave)->{
                     boolean found = false;
            for(TblCalendarEmployeeSchedule getEmployeeSchedule:listEmployeeSchedule){
                System.out.println("Date Schedule:"+getEmployeeSchedule.getSysCalendar().getCalendarDate());
                            if(getEmployeeSchedule.getSysCalendar().getCalendarDate().equals(Date.valueOf(newVal))){
                                found  = true;
                                System.out.println("Ketemu");
                                break;
                            }
                        }
                    if(newTypeLeave.getIdtype()==0){
                       if(found==true){
                         amountLeaveTaken = 0;
                         selectedData.getTblEmployeeByIdemployee().setLeaveAmount(selectedData.getTblEmployeeByIdemployee().getLeaveAmount()+1);
                         System.out.println("sisa cuti:"+selectedData.getTblEmployeeByIdemployee().getLeaveAmount());
                         lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(ada Jadwal,cuti ditambah)"));
                        }
                       else{
                         lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(tidak ada jadwal, cuti tidak ditambah)"));  
                       }
                    }
                    else{
                      if(found==true){
                        amountLeaveTaken = 1;
                        selectedData.getTblEmployeeByIdemployee().setLeaveAmount(selectedData.getTblEmployeeByIdemployee().getLeaveAmount()-1);
                        lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)));
                      }
                      else{
                        amountLeaveTaken = 0;
                        lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(amountLeaveTaken)+"(tidak ada jadwal)"));  
                    }
                }
                });
            }
        });
        if(selectedData.getRefEmployeeLeaveType().getIdtype()==0){
          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(0)));   
        }
        else{
          lblLeaveTaken.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(1))); 
        }*/
        txtLeaveNote.textProperty().bindBidirectional(selectedData.noteProperty()); 
        
        selectedDataToFormFunctionalComponent();
    }
   
    private void selectedDataToFormFunctionalComponent(){
       //gpFormDataLeave.setDisable(dataInputStatus==3);
       if(selectedData.getRefEmployeeLeaveType().getIdtype()==0 || (selectedData.getRefEmployeeLeaveType().getIdtype()==1 && selectedData.getTblEmployeeByIdemployee().getRefEmployeeType().getIdtype()!=0)){
           chbCutLeave.setDisable(true);
        }
       ClassViewSetting.setDisableForAllInputNode(gpFormDataLeave,dataInputStatus==3,cbpTypeLeave,cbpTypeEmployee,cbpNameEmployee,chbCutLeave);
       btnSave.setVisible(dataInputStatus!=3);
       btnCancel.setVisible(dataInputStatus!=3);
    }
    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;
    
    public Stage dialogStage;
    private void showLeaveTypeDayInputDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_leave/leave/LeaveTypeDayInputDialog.fxml"));
            LeaveTypeDayInputDialogController leaveTypeDayInputDialogController = new LeaveTypeDayInputDialogController(this);
            loader.setController(leaveTypeDayInputDialogController);
            
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
            Logger.getLogger(LeaveController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void dataLeaveCreateHandle() {
        dataInputStatus = 0;
       // System.out.println("day>>"+dpEndOffwork.getValue().getDayOfMonth());
          selectedData = new TblCalendarEmployeeLeave();
          setSelectedDataToInputForm();
          
          dataLeaveFormShowStatus.set(1);
          //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }
    
    private void dataLeaveUpdateHandle() {
        if (tableDataLeave.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if(!((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getSelectionModel().getSelectedItem()).getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))){
              dataInputStatus = 1;
              selectedData = parentController.getFLeaveManager().getDataEmployeeLeave(((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
              
              statusForm = "one day";
              leaveStatus = selectedData.getCutLeaveStatus().booleanValue();
              setSelectedDataToUpdateForm();
                //open form data warehouse
              dataLeaveFormShowStatus.set(1);
              //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
            }
            else{
              ClassMessage.showWarningSelectedDataFromDateNow(null,null);
              //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item from today..", null);
            }    
        } else {
            ClassMessage.showWarningSelectedDataMessage(null,null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
        }
    }
    
    private BooleanProperty isShowStatus = new SimpleBooleanProperty(false);
    private void dataLeaveShowHandle(){
        if(tableDataLeave.getTableView().getSelectionModel().getSelectedItems().size() == 1){
          dataInputStatus = 3;
          selectedData = parentController.getFLeaveManager().getDataEmployeeLeave(((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
          statusForm = "one day";
          leaveStatus = selectedData.getCutLeaveStatus().booleanValue();
          setSelectedDataToUpdateForm();
          isShowStatus.set(true);
          dataLeaveFormShowStatus.set(1);
        }
    }
    
    private void dataLeaveUnshowHandle(){
       tableDataLeave.getTableView().setItems(loadAllDataLeave());
       dataLeaveFormShowStatus.set(0);
       isShowStatus.set(false);
    }
    
    private void dataLeaveDeleteHandle() {
        if (tableDataLeave.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if(!((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getSelectionModel().getSelectedItem()).getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))){
              Alert alert = ClassMessage.showConfirmationDeletingDataMessage(null,null);//HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION,"CONFIRMATION","Are you sure want to delete this data?", null);
              if(alert.getResult()==ButtonType.OK){
                selectedData = (TblCalendarEmployeeLeave)tableDataLeave.getTableView().getSelectionModel().getSelectedItem();
                if(selectedData.getCutLeaveStatus()==true){
                   selectedData.getTblEmployeeByIdemployee().setCurrentLeaveAmount(selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()+1);
                }
                if(parentController.getFLeaveManager().deleteDataLeave(selectedData)){
                   ClassMessage.showSucceedDeletingDataMessage(null,null);
                  //HotelFX.showAlert(Alert.AlertType.INFORMATION,"SUCCESSED","Deleting data successed!!",null);
                  tableDataLeave.getTableView().setItems(loadAllDataLeave());
                  dataLeaveFormShowStatus.set(0);
                }
                else{
                  ClassMessage.showFailedDeletingDataMessage(parentController.getFLeaveManager().getErrorMessage(),null);
                   //HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Deleting data failed..!", parentController.getFLeaveManager().getErrorMessage()); 
                }
              }
              else{
                dataLeaveFormShowStatus.set(0); 
              }
            }
            else{
              ClassMessage.showWarningSelectedDataFromDateNow(null,null);
              //HotelFX.showAlert(Alert.AlertType.WARNING,"INVALID DATA INPUT","Pilih data mulai tanggal hari ini..!",null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null,null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Pilih data terlebih dahulu..!", null);
        }
    }

    private void dataSchedulePrintHandle() {

    }

    private void dataScheduleSaveHandle() {
        if (checkDataInputDataLeaveNull() && checkDataInDataBase() && checkCurrentLeave()){
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null,null);//HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION,"CONFIRMATION","Are you sure want to input this data?", null);
          if(alert.getResult()==ButtonType.OK){
             List<TblEmployee>listEmployee = new ArrayList(loadAllDataEmployee(cbpTypeEmployee.getValue()));
            TblCalendarEmployeeLeave dummySelectedData = new TblCalendarEmployeeLeave(selectedData);
            if(selectedData.getTblEmployeeByIdemployee()!=null && selectedData.getRefEmployeeLeaveType()!=null){
              dummySelectedData.setTblEmployeeByIdemployee(dummySelectedData.getTblEmployeeByIdemployee());
              dummySelectedData.setRefEmployeeLeaveType(dummySelectedData.getRefEmployeeLeaveType());
            }
            
            switch (dataInputStatus) {
                case 0:
                    if(statusForm.equalsIgnoreCase("one day")){
                       if(dummySelectedData.getRefEmployeeLeaveType().getIdtype()==0){
                          System.out.println("Amount Leave Taken:"+amountLeaveTaken);
                           if(parentController.getFLeaveManager().insertDataEmployeeAllLeave(listEmployeeAnnualLeave,amountLeaveTaken,selectedData,statusForm)){
                                //HotelFX.showAlert(Alert.AlertType.INFORMATION,"BERHASIL","Input data berhasil!!",null);
                              ClassMessage.showSucceedInsertingDataMessage(null,null);
                              tableDataLeave.getTableView().setItems(loadAllDataLeave());
                              dataLeaveFormShowStatus.set(0);
                              refreshFilteringLeave();
                              //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
                            }
                            else{
                              ClassMessage.showFailedInsertingDataMessage(parentController.getFLeaveManager().getErrorMessage(),null);
                            }
                        }
                      else{
                            if(dummySelectedData.getTblEmployeeByIdemployee().getRefEmployeeType().getIdtype()!=0){
                               dummySelectedData.getTblEmployeeByIdemployee().setCurrentLeaveAmount(0);
                            }
                            else{
                                System.out.println("amount leave taken:"+tempLeaveTaken);
                                dummySelectedData.getTblEmployeeByIdemployee().setCurrentLeaveAmount(dummySelectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-tempLeaveTaken);
                            }
                        
                           if(parentController.getFLeaveManager().insertDataEmployeeLeave(null,dummySelectedData,"one day")){
                              //HotelFX.showAlert(Alert.AlertType.INFORMATION,"BERHASIL","Input data berhasil!!",null);
                              ClassMessage.showSucceedInsertingDataMessage(null,null);
                              tableDataLeave.getTableView().setItems(loadAllDataLeave());
                              dataLeaveFormShowStatus.set(0);
                              refreshFilteringLeave();
                              //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
                            }
                           else{
                             ClassMessage.showFailedInsertingDataMessage(parentController.getFLeaveManager().getErrorMessage(),null);
                            }
                        }
                    }
                   else{
                       if(dummySelectedData.getRefEmployeeLeaveType().getIdtype()==0){
                           if(parentController.getFLeaveManager().insertDataEmployeeAllLeave(listEmployeeAnnualLeave,amountLeaveTaken,selectedData,statusForm)){
                              //HotelFX.showAlert(Alert.AlertType.INFORMATION,"BERHASIL","Input data berhasil!!",null);
                             ClassMessage.showSucceedInsertingDataMessage(null,null);
                             tableDataLeave.getTableView().setItems(loadAllDataLeave());
                             dataLeaveFormShowStatus.set(0);
                             refreshFilteringLeave();
                             //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
                            }
                           else{
                             ClassMessage.showFailedInsertingDataMessage(parentController.getFLeaveManager().getErrorMessage(),null);
                            }
                        }
                       else{
                           System.out.println("amount Leave Taken:"+tempLeaveTaken);
                           if(dummySelectedData.getTblEmployeeByIdemployee().getRefEmployeeType().getIdtype()!=0){
                              dummySelectedData.getTblEmployeeByIdemployee().setCurrentLeaveAmount(0);
                            }
                           else{
                             dummySelectedData.getTblEmployeeByIdemployee().setCurrentLeaveAmount(dummySelectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-tempLeaveTaken);
                            }
                            //checkAllEmployeeLeave();
                            System.out.println(">>"+listEmployeeSchedule.size());
                        
                           if(parentController.getFLeaveManager().insertDataEmployeeLeave(listEmployeeLeave,dummySelectedData,"one more day")){
                             ClassMessage.showSucceedInsertingDataMessage(null,null);
                             //HotelFX.showAlert(Alert.AlertType.INFORMATION,"BERHASIL","Input data berhasil!!",null);
                             tableDataLeave.getTableView().setItems(loadAllDataLeave());
                             dataLeaveFormShowStatus.set(0);
                             refreshFilteringLeave();
                             //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
                            }
                           else{
                              ClassMessage.showFailedInsertingDataMessage(parentController.getFLeaveManager().getErrorMessage(),null);
                            }
                        }
                        //System.out.println(">>"+listEmployeeLeave.size()); 
                    }
                break;
                case 1:
                    dummySelectedData.getTblEmployeeByIdemployee().setCurrentLeaveAmount(amountLeaveRemain);
                    
                    //TblCalendarEmployeeLeave employeeLeave = new TblCalendarEmployeeLeave(selectedData);
                    if(parentController.getFLeaveManager().updateDataLeave(dummySelectedData,Date.valueOf(dpDateLeave.getValue()))){
                        ClassMessage.showSucceedUpdatingDataMessage(null,null);
                        //HotelFX.showAlert(Alert.AlertType.INFORMATION,"BERHASIL","Update data berhasil!!",null);
                        tableDataLeave.getTableView().setItems(loadAllDataLeave());
                        dataLeaveFormShowStatus.set(0);
                        refreshFilteringLeave();
                        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
                    } else {
                       ClassMessage.showFailedUpdatingDataMessage(parentController.getFLeaveManager().getErrorMessage(),null);
                        //HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Update data gagal..!", parentController.getFLeaveManager().getErrorMessage());
                    }
                break;
                default:
                break;
            }
          }
          else{
           dataLeaveFormShowStatus.set(1);
          }
        } else {
            ClassMessage.showWarningInputDataMessage(errInput,null);
           // HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please check data input..!", errInput);
        }
    }

    private void dataLeaveCancelHandle() {
        tableDataLeave.getTableView().setItems(loadAllDataLeave());
        dataLeaveFormShowStatus.set(0);
        refreshFilteringLeave();
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }
    
    private String errInput;
    private boolean checkDataInputDataLeaveNull() {
        
        System.out.println("sys calendar:"+selectedData.getSysCalendar());
        System.out.println("hsl chb:"+selectedData.getCutLeaveStatus().booleanValue());
        
        boolean dataInput = true;
        errInput = "";
        if(cbpTypeLeave.getValue()==null){
          dataInput = false;
          errInput+="Tipe Cuti: "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(cbpTypeEmployee.getValue()==null){
          dataInput = false;
          errInput+="Tipe Karyawan: "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(cbpTypeLeave.getValue()!=null && cbpTypeLeave.getValue().getIdtype()==1){
            if(cbpNameEmployee.getValue()==null){
              dataInput = false;
              errInput+="Nama Karyawan: "+ClassMessage.defaultErrorNullValueMessage+"\n";
            }
        }
        
        if(statusForm.equalsIgnoreCase("one day")){
            if (dpDateLeave.getValue()==null){
                dataInput = false;
                errInput+="Tanggal Cuti: " +ClassMessage.defaultErrorNullValueMessage+"\n";
            }
        }
        else
        {
         //One More Day
            if (dpStartLeave.getValue() == null) {
                dataInput = false;
                errInput+="Tanggal Awal Cuti: " +ClassMessage.defaultErrorNullValueMessage+"\n";
            }
          
            if(dpEndLeave.getValue() == null){
                dataInput = false;
                errInput+="Tanggal Akhir Cuti:"+ClassMessage.defaultErrorNullValueMessage+ "\n";
            }
        }
        
      return dataInput;
    }
    
    //check sisa cuti yang dipunya user
    private boolean checkCurrentLeave(){
       boolean dataInput = true;
       if(cbpTypeLeave.getValue()!=null && (cbpTypeEmployee.getValue()!=null || cbpNameEmployee.getValue()!=null) && (dpDateLeave.getValue()!=null || (dpStartLeave!=null && dpEndLeave!=null))){
           if(selectedData.getRefEmployeeLeaveType().getIdtype()==1){
                if(dataInputStatus == 1 && leaveStatus==false){
                    if(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType().getIdtype()==0 && selectedData.getCutLeaveStatus().booleanValue()==true){
                     System.out.println("kkkkkk");
                        if(selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()<amountLeaveTaken && selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()!=0){
                          dataInput = false;
                          errInput +="Sisa cuti tidak mencukupi! \n";
                        }
                        else{
                         System.out.println("nnnnnn");
                            if(selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()==0){
                              dataInput = false;
                              errInput+="Sisa cuti habis!! \n";  
                            }
                        }
                    }
                }
           
                if(dataInputStatus==0){
                    if(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType().getIdtype()==0 && selectedData.getCutLeaveStatus().booleanValue()==true){
                        System.out.println("kkkkkk");
                        if(selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()<amountLeaveTaken && selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()!=0){
                          dataInput = false;
                          errInput +="Sisa cuti tidak mencukupi! \n";
                        }
                        else{
                          System.out.println("nnnnnn");
                            if(selectedData.getTblEmployeeByIdemployee().getCurrentLeaveAmount()==0){
                             dataInput = false;
                             errInput+="Sisa cuti habis!! \n";  
                            }
                        }
                    }
                }
            }
        }
       
       return dataInput;
    }
    
    //check data yang diinput sudah ada atau blm d db
    private boolean checkDataInDataBase(){
        List<TblCalendarEmployeeLeave>listEmployeeCheckLeave = new ArrayList();
        boolean dataInput = true;
        if(statusForm.equalsIgnoreCase("one day")){
           if(cbpTypeLeave.getValue()!=null && (cbpTypeEmployee.getValue()!=null || cbpNameEmployee.getValue()!=null) && (dpDateLeave.getValue()!=null)){
                System.out.println("mmmmmmm");
                boolean found = false;
                if(cbpTypeLeave.getValue().getIdtype()==0){
                    listEmployeeCheckLeave = parentController.getFLeaveManager().getAllDataEmployeeLeaveByDateAndLeaveType(Date.valueOf(dpDateLeave.getValue()));
                    if(listEmployeeCheckLeave.size()!=0){
                        if(dataInputStatus==1){
                           boolean checkEmployee = false;
                           for(TblCalendarEmployeeLeave getEmployeeLeave:listEmployeeCheckLeave){
                               if(getEmployeeLeave.getTblEmployeeByIdemployee().getIdemployee()==selectedData.getTblEmployeeByIdemployee().getIdemployee()){
                                  checkEmployee = true;
                               } 
                            }
                           
                           if(checkEmployee){
                              dataInput=false;
                              errInput+="Data sudah ada di database!!";
                           }
                        }
                        else{
                          dataInput=false;
                          errInput+="Data sudah ada di database!!";
                        }
                    }
                }
                else{
                    System.out.println("jjjjj");
                    listEmployeeCheckLeave = parentController.getFLeaveManager().getAllDataEmployeeLeaveByDateAndIdEmployee(selectedData.getTblEmployeeByIdemployee(),Date.valueOf(dpDateLeave.getValue()));
                    if(listEmployeeCheckLeave.size()>0){
                        for(int i = 0; i<listEmployeeCheckLeave.size();i++){
                            if(dataInputStatus==1){
                                if(listEmployeeCheckLeave.get(i).getCutLeaveStatus().booleanValue()==selectedData.getCutLeaveStatus().booleanValue()){
                                  found = true;
                                }
                            }
                        }
                        
                        if(dataInputStatus==0){
                           if(listEmployeeCheckLeave.size()==1){
                              found = true;
                           }
                        }
                        
                        if(found==true){
                          dataInput = false;
                          errInput+="Data sudah ada di database!!";
                        }
                    }
                   else{
                      System.out.println("hhhhh");
                       if(selectedData.getSysCalendar()==null){
                         dataInput=false;
                         errInput +="Tidak ada jadwal!!\n"; 
                       }
                    }
                }
            }
        }
        else{
            if(cbpTypeLeave.getValue()!=null && (cbpTypeEmployee.getValue()!=null || cbpNameEmployee.getValue()!=null) && dpStartLeave.getValue()!=null && dpEndLeave.getValue()!=null){
                boolean found = false;
                if(cbpTypeLeave.getValue().getIdtype()==0){
                    listEmployeeCheckLeave = parentController.getFLeaveManager().getAllDataEmployeeLeaveByStartDateEndDateAndTypeLeave(Date.valueOf(dpStartLeave.getValue()),Date.valueOf(dpEndLeave.getValue()));
                    System.out.println("size check leave:"+listEmployeeCheckLeave);
                    if(listEmployeeCheckLeave.size()>0){
                        for(Date date=Date.valueOf(dpStartLeave.getValue());!date.after(Date.valueOf(dpEndLeave.getValue()));date=Date.valueOf(date.toLocalDate().plusDays(1))){
                          boolean dateFound = false;
                           for(TblCalendarEmployeeLeave getEmployeeLeave :listEmployeeCheckLeave){
                               if(getEmployeeLeave.getSysCalendar().getCalendarDate().equals(date)){
                                  dateFound=true;
                                }
                            }
                        
                            if(dateFound==true){
                             dataInput = false;
                             errInput +=" Data tanggal"+" "+ new SimpleDateFormat("dd/MM/yyyy").format(date)+" sudah disimpan!!\n";
                            }
                        //System.out.println("hsl group:"+getEmployeeCheckLeave.getSysCalendar().getCalendarDate());
                        }
                    }
                }
                else{
                   listEmployeeCheckLeave = parentController.getFLeaveManager().getAllDataEmployeeLeaveByStartDateEndDateAndIdEmployee(selectedData.getTblEmployeeByIdemployee(),Date.valueOf(dpStartLeave.getValue()),Date.valueOf(dpEndLeave.getValue()));
                    System.out.println("size list employee check:"+listEmployeeCheckLeave.size()); 
                   if(listEmployeeCheckLeave.size()>0){
                        for(Date date=Date.valueOf(dpStartLeave.getValue());!date.after(Date.valueOf(dpEndLeave.getValue()));date=Date.valueOf(date.toLocalDate().plusDays(1))){
                            boolean dateFound = false;
                            for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeCheckLeave){
                               if(getEmployeeLeave.getSysCalendar().getCalendarDate().equals(date)){
                                 dateFound = true;
                                }
                            }
                            
                            if(dateFound){
                              dataInput = false;
                              errInput +=" Data tanggal"+" "+ new SimpleDateFormat("dd/MM/yyyy").format(date)+" sudah disimpan!!\n"; 
                            }
                        } 
                    }
                    else{
                        if(amountLeaveTaken==0 && selectedData.getCutLeaveStatus()==true){
                         dataInput = false;
                         errInput += "Tidak ada jadwal!! \n";
                        }
                    }
                }
            }
        }
        return dataInput; 
    }
  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      setDataOffworkSplitpane();
      initTableDataOffwork();
      initFormDataLeave();
      
      spDataLeave.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataLeaveFormShowStatus.set(0);
        });
     
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public LeaveController(FeatureLeaveController parentController){
      this.parentController = parentController;    
    }
    
    private final FeatureLeaveController parentController;
    
}
