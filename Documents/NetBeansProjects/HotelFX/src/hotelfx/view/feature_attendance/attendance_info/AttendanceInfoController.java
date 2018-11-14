/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_attendance.attendance_info;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassOptionViewTable;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefEmployeeAttendanceEndStatus;
import hotelfx.persistence.model.RefEmployeeAttendanceStartStatus;
import hotelfx.persistence.model.RefEmployeeAttendanceStatus;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.TblCalendarEmployeeAttendance;
import hotelfx.view.feature_attendance.FeatureAttendanceController;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class AttendanceInfoController implements Initializable{
         /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataAttendanceInfo;

    private DoubleProperty dataAttendanceInfoFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataAttendanceInfoLayoutDisableLayer;
    
    @FXML
    private AnchorPane tableDataAttendanceInfoLayout;
    
    private boolean isTimeLinePlaying = false;
    
    private void setDataAttendanceInfoSplitpane() {
        spDataAttendanceInfo.setDividerPositions(1);
        
        dataAttendanceInfoFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataAttendanceInfoFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataAttendanceInfo.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataAttendanceInfo.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });
        
        dataAttendanceInfoFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if(dataInputStatus!=3){
                if (newVal.doubleValue() == 0.0) {    //enable
                 tableDataAttendanceInfoLayout.setDisable(false);
                 tableDataAttendanceInfoLayoutDisableLayer.setDisable(true);
                 tableDataAttendanceInfoLayout.toFront();
                }
                
                if (newVal.doubleValue() == 1) {  //disable
                 tableDataAttendanceInfoLayout.setDisable(true);
                 tableDataAttendanceInfoLayoutDisableLayer.setDisable(false);
                 tableDataAttendanceInfoLayoutDisableLayer.toFront();
                }
            }    
        });
        
        
        
        dataAttendanceInfoFormShowStatus.set(0.0);

    }
    
    @FXML
    private AnchorPane tableAttendanceInfoLayout;
    @FXML
    private JFXRadioButton rdIsOvertime;
    
    private ClassTableWithControl tblAttendanceInfo;
    
    private ObservableList<TblCalendarEmployeeAttendance>listAttendance = FXCollections.observableArrayList();
    
    private void initTableAttendanceInfo(){
      setTableAttendanceInfo();
      setTableControlAttendanceInfo();
      tableAttendanceInfoLayout.getChildren().clear();
      AnchorPane.setTopAnchor(tblAttendanceInfo,15.0);
      AnchorPane.setBottomAnchor(tblAttendanceInfo,15.0);
      AnchorPane.setLeftAnchor(tblAttendanceInfo,15.0);
      AnchorPane.setRightAnchor(tblAttendanceInfo,15.0);
      tableAttendanceInfoLayout.getChildren().add(tblAttendanceInfo);
    }
    
    private void setTableAttendanceInfo(){
      TableView<TblCalendarEmployeeAttendance>tblEmployeeAttendance = new TableView();
      TableColumn<TblCalendarEmployeeAttendance,String>dateAttendance = new TableColumn<>(" Tanggal\nAbsen");
      dateAttendance.setMinWidth(90);
      dateAttendance.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getSysCalendar().getCalendarDate()),param.getValue().getSysCalendar().calendarDateProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>codeEmployee = new TableColumn<>("ID");
      codeEmployee.setMinWidth(90);
        codeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(),param.getValue().getTblEmployeeByIdemployee().codeEmployeeProperty())); 
      TableColumn<TblCalendarEmployeeAttendance,String>typeEmployee = new TableColumn<>("Tipe");
      typeEmployee.setMinWidth(90);
        typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(),param.getValue().getTblEmployeeByIdemployee().refEmployeeTypeProperty()));  
      TableColumn<TblCalendarEmployeeAttendance,String>nameEmployee = new TableColumn<>("Nama");
      nameEmployee.setMinWidth(140);
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(),param.getValue().getTblEmployeeByIdemployee().getTblPeople().fullNameProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>employee = new TableColumn<>("Karyawan");
      employee.getColumns().addAll(codeEmployee,typeEmployee,nameEmployee);
        TableColumn<TblCalendarEmployeeAttendance,String>nameSchedule = new TableColumn<>("Jadwal");
        nameSchedule.setMinWidth(120);
        nameSchedule.setCellValueFactory(cellData->cellData.getValue().scheduleNameProperty());
      TableColumn<TblCalendarEmployeeAttendance,String>startTimeSchedule = new TableColumn("Masuk");
      startTimeSchedule.setMinWidth(70);
      startTimeSchedule.setMaxWidth(70);
      startTimeSchedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getBeginSchedule()!=null?param.getValue().getBeginSchedule().toString():"-",param.getValue().beginScheduleProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>endTimeSchedule = new TableColumn("Keluar");
      endTimeSchedule.setMinWidth(70);
      endTimeSchedule.setMaxWidth(70);
      endTimeSchedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getEndSchedule()!=null?param.getValue().getEndSchedule().toString():"-",param.getValue().endScheduleProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>timeWorkSchedule = new TableColumn("Jam Kerja");
      timeWorkSchedule.getColumns().addAll(startTimeSchedule,endTimeSchedule);
      TableColumn<TblCalendarEmployeeAttendance,String>typeAttendance = new TableColumn<>("Kehadiran");
      typeAttendance.setMinWidth(100);
      typeAttendance.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeAttendanceStatus().getStatusName(),param.getValue().getRefEmployeeAttendanceStatus().statusNameProperty()));
         TableColumn<TblCalendarEmployeeAttendance,String>startTimeRealWork = new TableColumn("Masuk");
         startTimeRealWork.setMinWidth(140);
      startTimeRealWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getBeginReal()!=null?(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(param.getValue().getBeginReal())):"-",param.getValue().beginRealProperty()));
      TableColumn<TblCalendarEmployeeAttendance,String>endTimeRealWork = new TableColumn("Keluar");
      endTimeRealWork.setMinWidth(140);
        endTimeRealWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getEndReal()!=null?(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(param.getValue().getEndReal())):"-",param.getValue().endRealProperty()));
        TableColumn<TblCalendarEmployeeAttendance,String>timeWorkReal = new TableColumn("Jam Real");
        timeWorkReal.getColumns().addAll(startTimeRealWork,endTimeRealWork);
        TableColumn<TblCalendarEmployeeAttendance,Boolean>isOvertime = new TableColumn("Lembur");
        isOvertime.setMinWidth(70);
        isOvertime.setMaxWidth(70);
        isOvertime.setCellFactory(CheckBoxTableCell.forTableColumn(isOvertime));
        isOvertime.setCellValueFactory(cellData->cellData.getValue().isOvertimeProperty());
        TableColumn<TblCalendarEmployeeAttendance,String>statusStartAttendance = new TableColumn<>("Masuk");
        statusStartAttendance.setMinWidth(90);
        statusStartAttendance.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeAttendanceStartStatus()!=null?param.getValue().getRefEmployeeAttendanceStartStatus().getStatusName():"-",param.getValue().refEmployeeAttendanceStartStatusProperty()));
       TableColumn<TblCalendarEmployeeAttendance,String>statusEndAttendance = new TableColumn<>("Keluar");
       statusEndAttendance.setMinWidth(90);
      statusEndAttendance.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeAttendance,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeAttendanceEndStatus()!=null?param.getValue().getRefEmployeeAttendanceEndStatus().getStatusName():"-",param.getValue().refEmployeeAttendanceEndStatusProperty()));  
      TableColumn<TblCalendarEmployeeAttendance,String>statusAttendance = new TableColumn<>("Status Absen");
      statusAttendance.getColumns().addAll(statusStartAttendance,statusEndAttendance,typeAttendance);
      tblEmployeeAttendance.getColumns().addAll(dateAttendance,employee,timeWorkSchedule,timeWorkReal,isOvertime,statusAttendance);
        
      tblEmployeeAttendance.setRowFactory(tv->{
         TableRow<TblCalendarEmployeeAttendance>row = new TableRow<>();
         row.setOnMouseClicked((e)->{
           if(e.getClickCount()==2){
               if(isShowStatus.get()){
                  dataAttendanceInfoUnshowHandle();
                }
               else{
                   if(!row.isEmpty()){
                      dataAttendanceInfoShowHandle();
                   }
                }
            }
           else{
                if(!row.isEmpty()){
                    if(isShowStatus.get()){
                      dataAttendanceInfoShowHandle();
                    }
                }
            }
         });
         return row;
      });
        tblAttendanceInfo = new ClassTableWithControl(tblEmployeeAttendance);
    }
    
    private List<TblCalendarEmployeeAttendance>loadAllEmployeeAttendance(RefEmployeeType employeeType,ClassOptionViewTable listView,Date startDate,Date endDate){
      ObservableList<TblCalendarEmployeeAttendance>list = FXCollections.observableArrayList();
      if(employeeType!=null && startDate!=null && endDate!=null && employeeType!=null && listView!=null){
        list.setAll(parentController.getFAttendanceManager().getAllEmployeeAttendance(employeeType.getIdtype(),listView.getIdOption(),startDate,endDate));
      }
      return list;
    }
    
    private void setTableControlAttendanceInfo(){
      ObservableList<Node>buttonControls = FXCollections.observableArrayList();
      JFXButton buttonControlUpdate = new JFXButton();
      buttonControlUpdate.setText("Ubah");
      buttonControls.add(buttonControlUpdate);
      
      buttonControlUpdate.setOnAction((e)->{
         dataAttendanceInfoUpdateHandle();
      });
      
      JFXButton buttonControlPrint = new JFXButton();
      buttonControlPrint.setText("Cetak");
      buttonControls.add(buttonControlPrint);
      
      buttonControlPrint.setOnAction((e)->{
         //dataAttendanceInfoUpdateHandle();
      });
      
      tblAttendanceInfo.addButtonControl(buttonControls);
    }
    
    @FXML
    private GridPane gpViewAttendance;
    @FXML
    private AnchorPane ancTypeEmployee;
    @FXML
    private AnchorPane ancListAbsensi;
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnViewAttendance;
    
    private JFXCComboBoxPopup<RefEmployeeType>cbpEmployeeType = new JFXCComboBoxPopup(RefEmployeeType.class);
    private JFXCComboBoxPopup<ClassOptionViewTable>cbpViewTable = new JFXCComboBoxPopup(ClassOptionViewTable.class);
    
    
     
    private void initTopFormAttendanceInfo(){
      initDataPopupTopFormLayout();
      
      initDateCalendar();
      //initDataPopupTopForm();
      btnViewAttendance.setOnAction((e)->{
       if(checkDataInput()){
          tblAttendanceInfo.getTableView().setItems(FXCollections.observableArrayList(loadAllEmployeeAttendance(cbpEmployeeType.getValue(),cbpViewTable.getValue(),Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()))));
       }
       else{
        ClassMessage.showWarningInputDataMessage(errInput,null);
         //HotelFX.showAlert(Alert.AlertType.WARNING,"INVALID INPUT","Periksa kembali data yang diinput!!", errInput);
       }
      });
     
    }
    
    private void initDataPopupTopFormLayout(){
       initDataPopupTopForm();
        
       ancTypeEmployee.getChildren().clear();
       AnchorPane.setTopAnchor(cbpEmployeeType,0.0);
       AnchorPane.setBottomAnchor(cbpEmployeeType,0.0);
       AnchorPane.setLeftAnchor(cbpEmployeeType,0.0);
       AnchorPane.setRightAnchor(cbpEmployeeType,0.0);
       ancTypeEmployee.getChildren().add(cbpEmployeeType);
       
       ancListAbsensi.getChildren().clear();
       AnchorPane.setTopAnchor(cbpViewTable,0.0);
       AnchorPane.setBottomAnchor(cbpViewTable,0.0);
       AnchorPane.setLeftAnchor(cbpViewTable,0.0);
       AnchorPane.setRightAnchor(cbpViewTable,0.0);
       ancListAbsensi.getChildren().add(cbpViewTable);
    }
    
    private void initDateCalendar(){
       ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpStartDate,dpEndDate);
    }
    
    private void initDataPopupTopForm(){
      TableView<RefEmployeeType>tblEmployeeType = new TableView();
      TableColumn<RefEmployeeType,String>nameTypeEmployee = new TableColumn<RefEmployeeType,String>("Tipe Karyawan");
      nameTypeEmployee.setMinWidth(200);
      nameTypeEmployee.setCellValueFactory(cellData->cellData.getValue().typeNameProperty());
      tblEmployeeType.getColumns().addAll(nameTypeEmployee);
      
      ObservableList<RefEmployeeType>typeEmployeeItems = FXCollections.observableArrayList(loadAllDataEmployeeType());
      setFunctionPopup(cbpEmployeeType,tblEmployeeType,typeEmployeeItems,"typeName","Tipe Karyawan *",300,300);
      
      TableView<ClassOptionViewTable>tblOptionalView = new TableView();
      TableColumn<ClassOptionViewTable,String>nameOptional = new TableColumn<ClassOptionViewTable,String>("Tipe Absensi");
      nameOptional.setMinWidth(200);
      nameOptional.setCellValueFactory(cellData->cellData.getValue().optionNameProperty());
      tblOptionalView.getColumns().addAll(nameOptional);
      ObservableList<ClassOptionViewTable>viewTableItems = FXCollections.observableArrayList(loadAllDataTableOption());
      setFunctionPopup(cbpViewTable,tblOptionalView,viewTableItems,"optionName","Tipe Absensi *",300,300);
      
      //gpViewAttendance.add(cbpEmployeeType,3, 1);
      //gpViewAttendance.add(cbpViewTable,5,1);
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
        content.setPrefSize(prefWidth, prefHeight);

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
    
    private List<RefEmployeeType>loadAllDataEmployeeType(){
      List<RefEmployeeType>listEmployeeType = new ArrayList();
      RefEmployeeType employeeType = new RefEmployeeType();
      employeeType.setIdtype(3);
      employeeType.setTypeName("Semua Tipe Karyawan");
      listEmployeeType.add(employeeType);
      listEmployeeType.addAll(parentController.getFAttendanceManager().getTypeEmployee());
      return listEmployeeType;
    }
    
    private List<ClassOptionViewTable>loadAllDataTableOption(){
      List<ClassOptionViewTable>listOption = new ArrayList<>();
      listOption.add(new ClassOptionViewTable(1,"Semua Absensi"));
      listOption.add(new ClassOptionViewTable(2,"Absensi Kerja"));
      listOption.add(new ClassOptionViewTable(3,"Absensi Lembur"));
      return listOption;
    }
    /*private void selectedDataForView(){
       cbpEmployeeType.valueProperty().addListener((obs,oldEmployeeType,newEmployeeType)->{
           if(newEmployeeType!=null){
             listAttendance.setAll(loadAllEmployeeAttendance(newEmployeeType,cbpViewTable.getValue(),Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()))); 
           } 
        });
        
        dpStartDate.valueProperty().addListener((obs,oldStartDate,newStartDate)->{
            System.out.println(">>"+cbpEmployeeType.getValue());
           if(newStartDate!=null){
            listAttendance.setAll(loadAllEmployeeAttendance(cbpEmployeeType.getValue(),cbpViewTable.getValue(),Date.valueOf(newStartDate),Date.valueOf(dpEndDate.getValue()))); 
           } 
        });
        
        dpEndDate.valueProperty().addListener((obs,oldEndDate,newEndDate)->{
           if(newEndDate!=null){
             listAttendance.setAll(loadAllEmployeeAttendance(cbpEmployeeType.getValue(),cbpViewTable.getValue(),Date.valueOf(dpStartDate.getValue()),Date.valueOf(newEndDate))); 
           }
        });
        
         cbpViewTable.valueProperty().addListener((obs,oldList,newList)->{
           if(newList!=null){
             listAttendance.setAll(loadAllEmployeeAttendance(cbpEmployeeType.getValue(),newList,Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()))); 
           }
        });
        
    }*/
    
    //form update info
    @FXML
    private AnchorPane formAnchor;
    @FXML
    private ScrollPane spFormAttendanceInfo;
    @FXML
    private GridPane gpFormAttendanceInfo;
    @FXML
    private JFXDatePicker dpDatePeriode;
    @FXML
    private JFXTextField txtTypeEmployee;
    @FXML
    private JFXTextField txIdEmployee;
    @FXML
    private JFXTextField txtNameEmployee;
    @FXML
    private JFXTextField txtNameSchedule;
    @FXML
    private JFXTimePicker tpStartWorkSchedule;
    @FXML
    private JFXTimePicker tpEndWorkSchedule;
    @FXML
    private JFXTimePicker tpStartWorkReal;
    @FXML
    private JFXTimePicker tpEndWorkReal;
    @FXML
    private JFXCheckBox chbIsOvertime;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;
    
    private TblCalendarEmployeeAttendance selectedData;
    
    private JFXCComboBoxPopup<RefEmployeeType>cbpEmployeeTypeForm = new JFXCComboBoxPopup(RefEmployeeType.class);
    private JFXCComboBoxPopup<RefEmployeeAttendanceStatus>cbpAttendanceStatus = new JFXCComboBoxPopup(RefEmployeeAttendanceStatus.class);
    private JFXCComboBoxPopup<RefEmployeeAttendanceStartStatus>cbpAttendanceStartStatus = new JFXCComboBoxPopup(RefEmployeeAttendanceStartStatus.class);
    private JFXCComboBoxPopup<RefEmployeeAttendanceEndStatus>cbpAttendanceEndStatus = new JFXCComboBoxPopup(RefEmployeeAttendanceEndStatus.class);
    
    private int dataInputStatus = 1;
    private void initFormAttendanceInfo(){
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormAttendanceInfo.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormAttendanceInfo.setOnScroll((ScrollEvent scroll) -> {
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
        
        btnSave.setOnAction((e)->{
           dataAttendanceInfoSaveHandle();
        });
        
        btnCancel.setOnAction((e)->{
          dataAttendanceInfoCancelHandle();
        });
        
        initDataPopup();
        
        initImportantFieldColor();
    }
    
   
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpEmployeeType,
                cbpViewTable, 
                dpStartDate, 
                dpEndDate);
    }
    
    private void initDataPopup(){
      TableView<RefEmployeeType>tblEmployeeType = new TableView<>();
      TableColumn<RefEmployeeType,String>nameEmployeeType = new TableColumn<>("Tipe Karyawan");
      nameEmployeeType.setCellValueFactory(cellData->cellData.getValue().typeNameProperty());
      tblEmployeeType.getColumns().add(nameEmployeeType);
      ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(parentController.getFAttendanceManager().getTypeEmployee());
      setFunctionPopup(cbpEmployeeTypeForm,tblEmployeeType,employeeTypeItems,"typeName","Tipe Karyawan",300,300);
      
      TableView<RefEmployeeAttendanceStartStatus>tblEmployeeStartStatus = new TableView<>();
      TableColumn<RefEmployeeAttendanceStartStatus,String>nameAttendanceStartStatus = new TableColumn<>("Status Masuk");
      nameAttendanceStartStatus.setCellValueFactory(cellData->cellData.getValue().statusNameProperty());
      tblEmployeeStartStatus.getColumns().add(nameAttendanceStartStatus);
      ObservableList<RefEmployeeAttendanceStartStatus>employeeStartStatusItems = FXCollections.observableArrayList(parentController.getFAttendanceManager().getAllAttendanceStartStatus());
      setFunctionPopup(cbpAttendanceStartStatus,tblEmployeeStartStatus,employeeStartStatusItems,"statusName","Status Masuk",300,300);
      
      TableView<RefEmployeeAttendanceEndStatus>tblEmployeeAttendanceEndStatus = new TableView<>();
      TableColumn<RefEmployeeAttendanceEndStatus,String>nameAttendanceEndStatus = new TableColumn<>("Status Keluar");
      nameAttendanceEndStatus.setCellValueFactory(cellData->cellData.getValue().statusNameProperty());
      ObservableList<RefEmployeeAttendanceEndStatus>employeeEndStatusItems = FXCollections.observableArrayList(parentController.getFAttendanceManager().getAllAttendanceEndStatus());
      tblEmployeeAttendanceEndStatus.getColumns().add(nameAttendanceEndStatus);
      setFunctionPopup(cbpAttendanceEndStatus,tblEmployeeAttendanceEndStatus,employeeEndStatusItems,"statusName","Status Keluar",300,300);
      
      TableView<RefEmployeeAttendanceStatus>tblEmployeeAttendanceStatus = new TableView<>();
      TableColumn<RefEmployeeAttendanceStatus,String>nameAttendanceStatus = new TableColumn<>("Status Kehadiran");
      nameAttendanceStatus.setMinWidth(200);
      nameAttendanceStatus.setCellValueFactory(cellData->cellData.getValue().statusNameProperty());
      ObservableList<RefEmployeeAttendanceStatus>employeeAttendanceStatusItems = FXCollections.observableArrayList(parentController.getFAttendanceManager().getAllAttendanceStatus());
      setFunctionPopup(cbpAttendanceStatus,tblEmployeeAttendanceStatus,employeeAttendanceStatusItems,"statusName","Status Kehadiran",300,300);
      tblEmployeeAttendanceStatus.getColumns().add(nameAttendanceStatus);
      
      gpFormAttendanceInfo.add(cbpEmployeeTypeForm,2,1);
      gpFormAttendanceInfo.add(cbpAttendanceStartStatus,1,6);
      gpFormAttendanceInfo.add(cbpAttendanceEndStatus,2,6);
      gpFormAttendanceInfo.add(cbpAttendanceStatus,1,7);
    }
    
    private void setSelectedDataToUpdateForm(){
      
       if(selectedData.getSysCalendar()!=null){
         dpDatePeriode.setValue(((java.sql.Date)selectedData.getSysCalendar().getCalendarDate()).toLocalDate());
         dpDatePeriode.setDisable(true);
       }
       
       cbpEmployeeTypeForm.valueProperty().set(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType());
       cbpEmployeeTypeForm.setDisable(true);
       txIdEmployee.textProperty().set(selectedData.getTblEmployeeByIdemployee().getCodeEmployee());
       txIdEmployee.setDisable(true);
       txtNameEmployee.textProperty().set(selectedData.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       txtNameEmployee.setDisable(true);
       
       txtNameSchedule.textProperty().bindBidirectional(selectedData.scheduleNameProperty());
       txtNameSchedule.setDisable(true);
       
       chbIsOvertime.selectedProperty().bindBidirectional(selectedData.isOvertimeProperty());
       chbIsOvertime.setDisable(true);
       
       if(selectedData.getBeginSchedule()!=null){
         tpStartWorkSchedule.setValue(selectedData.getBeginSchedule().toLocalTime());
       }
       else{
        tpStartWorkSchedule.setValue(null);
       }
        tpStartWorkSchedule.setDisable(true);
        
        
        if(selectedData.getBeginReal()!=null){
          Time timeBeginReal = new Time(selectedData.getBeginReal().getTime());
          tpStartWorkReal.setValue(timeBeginReal.toLocalTime());
        }
        else{
         tpStartWorkReal.setValue(null);
        }  
        tpStartWorkReal.setDisable(true);
         
         if(selectedData.getEndReal()!=null)
        {
          Time timeEndReal = new Time(selectedData.getEndReal().getTime());
          tpEndWorkReal.setValue(timeEndReal.toLocalTime());
          //
        }
         else{
           tpEndWorkReal.setValue(null);
         }
         tpEndWorkReal.setDisable(true);
         
        if(selectedData.getEndSchedule()!=null)
        {
          //Time time = new Time(selectedData.getEndSchedule().getTime());
          tpEndWorkSchedule.setValue(selectedData.getEndSchedule().toLocalTime());
        }
        else{
          tpEndWorkSchedule.setValue(null);
        }
        tpEndWorkSchedule.setDisable(true);
        
        cbpAttendanceStartStatus.valueProperty().bindBidirectional(selectedData.refEmployeeAttendanceStartStatusProperty());
        cbpAttendanceStartStatus.setDisable(true);
        cbpAttendanceEndStatus.valueProperty().bindBidirectional(selectedData.refEmployeeAttendanceEndStatusProperty());
        cbpAttendanceEndStatus.setDisable(true);
        cbpAttendanceStatus.valueProperty().bindBidirectional(selectedData.refEmployeeAttendanceStatusProperty());
        cbpAttendanceStatus.hide();
        
        setSelectedDataToUpdateFormFunctionalComponent();
    }
    
    private void setSelectedDataToUpdateFormFunctionalComponent(){
     ClassViewSetting.setDisableForAllInputNode(gpFormAttendanceInfo,dataInputStatus==3,
             cbpAttendanceStartStatus,cbpAttendanceEndStatus,tpStartWorkSchedule,tpEndWorkSchedule,cbpEmployeeTypeForm,
             txIdEmployee,txtNameEmployee,chbIsOvertime,dpDatePeriode);
       btnSave.setVisible(dataInputStatus!=3);
       btnCancel.setVisible(dataInputStatus!=3);
    }
    
    private void dataAttendanceInfoUpdateHandle(){
      if(tblAttendanceInfo.getTableView().getSelectionModel().getSelectedItems().size()==1){
        dataInputStatus = 1;
        selectedData = parentController.getFAttendanceManager().getEmployeeAttendance(((TblCalendarEmployeeAttendance)tblAttendanceInfo.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
          //System.out.println("hsl employee:"+selectedData.getTblEmployeeByIdemployee().getCodeEmployee());
        setSelectedDataToUpdateForm();
        dataAttendanceInfoFormShowStatus.set(0);
        dataAttendanceInfoFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
      }
      else {
        ClassMessage.showWarningSelectedDataMessage(null,null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Silahkan pilih data yang akan di ubah..!", null);
        }
    }
    
    private BooleanProperty isShowStatus = new SimpleBooleanProperty(false);
    private void dataAttendanceInfoShowHandle(){
      if(tblAttendanceInfo.getTableView().getSelectionModel().getSelectedItems().size()==1){
        dataInputStatus =3;
        selectedData = parentController.getFAttendanceManager().getEmployeeAttendance(((TblCalendarEmployeeAttendance)tblAttendanceInfo.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
          //System.out.println("hsl employee:"+selectedData.getTblEmployeeByIdemployee().getCodeEmployee());
        setSelectedDataToUpdateForm();
        
        dataAttendanceInfoFormShowStatus.set(1);
        isShowStatus.set(true);
      }
    }
    
    private void dataAttendanceInfoUnshowHandle(){
       tblAttendanceInfo.getTableView().setItems(FXCollections.observableArrayList(loadAllEmployeeAttendance(cbpEmployeeType.getValue(),cbpViewTable.getValue(),Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()))));
       dataAttendanceInfoFormShowStatus.set(0);
       isShowStatus.set(false);
    }
    
    private void dataAttendanceInfoSaveHandle(){
        Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
          if(alert.getResult()==ButtonType.OK){
           //dummy entry
         selectedData.setBeginReal(Timestamp.valueOf(LocalDateTime.of(dpDatePeriode.getValue(),tpStartWorkReal.getValue())));
         selectedData.setEndReal(Timestamp.valueOf(LocalDateTime.of(dpDatePeriode.getValue(),tpEndWorkReal.getValue())));
         TblCalendarEmployeeAttendance dummySelectedData = new TblCalendarEmployeeAttendance(selectedData);
        /* if(selectedData.getRefEmployeeAttendanceStartStatus()!=null && selectedData.getRefEmployeeAttendanceEndStatus()!=null){
         dummySelectedData.setRefEmployeeAttendanceStartStatus(dummySelectedData.getRefEmployeeAttendanceStartStatus());
         dummySelectedData.setRefEmployeeAttendanceEndStatus(dummySelectedData.getRefEmployeeAttendanceEndStatus());
         dummySelectedData.setRefEmployeeAttendanceStatus(dummySelectedData.getRefEmployeeAttendanceStatus());
         dummySelectedData.setSysCalendar(dummySelectedData.getSysCalendar());
         dummySelectedData.setTblEmployeeByIdemployee(dummySelectedData.getTblEmployeeByIdemployee());
         }*/
         
        if(parentController.getFAttendanceManager().updateDataAttendance(dummySelectedData)){
          ClassMessage.showSucceedUpdatingDataMessage(null,null);
          tblAttendanceInfo.getTableView().setItems(FXCollections.observableArrayList(loadAllEmployeeAttendance(cbpEmployeeType.getValue(),cbpViewTable.getValue(),Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()))));
          dataAttendanceInfoFormShowStatus.set(0);
          //set unsaving data input -> 'false'
          ClassSession.unSavingDataInput.set(false);
         }
         else{
          ClassMessage.showFailedUpdatingDataMessage(parentController.getFAttendanceManager().getErrorMessage(),null);
          //HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Update data gagal..!", parentController.getFAttendanceManager().getErrorMessage());
          }
          }
          else{
            dataAttendanceInfoFormShowStatus.set(1);
          }
    }
    
    private void dataAttendanceInfoCancelHandle(){
      tblAttendanceInfo.getTableView().setItems(FXCollections.observableArrayList(loadAllEmployeeAttendance(cbpEmployeeType.getValue(),cbpViewTable.getValue(),Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()))));
      dataAttendanceInfoFormShowStatus.set(0);
      isShowStatus.set(false);
      //set unsaving data input -> 'false'
      ClassSession.unSavingDataInput.set(false);
    }
    
     private String errInput;
    private boolean checkDataInput(){
      boolean check = true;
      errInput = "";
      if(dpStartDate.getValue()==null){
        check = false;
        errInput+="Tanggal Periode Awal: "+ClassMessage.defaultErrorNullValueMessage+"\n";
      }
      
      if(dpEndDate.getValue()==null){
         check = false;
         errInput+="Tanggal Periode Akhir: "+ClassMessage.defaultErrorNullValueMessage+"\n";
      }
      
      if(cbpEmployeeType.getValue()==null){
        check = false;
        errInput+="Tipe Karyawan: "+ClassMessage.defaultErrorNullValueMessage+"\n";
      }
      
      if(cbpViewTable.getValue()==null){
        check = false;
        errInput+="List View: "+ClassMessage.defaultErrorNullValueMessage+"\n";
      }
      
      return check;
    }
    
    public void initialize(URL location, ResourceBundle resources) {
        setDataAttendanceInfoSplitpane();
         initTopFormAttendanceInfo();
         //selectedDataForView();
        initTableAttendanceInfo();
        initFormAttendanceInfo();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public AttendanceInfoController(FeatureAttendanceController parentController){
      this.parentController = parentController;
    }
    
    private final FeatureAttendanceController parentController;
}
