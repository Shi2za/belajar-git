/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_overtime.overtime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.TblCalendarEmployeeOvertime;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_overtime.FeatureOvertimeController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.converter.BigDecimalStringConverter;

/**
 *
 * @author Andreas
 */
public class OvertimeController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataOvertime;

    private DoubleProperty dataOvertimeFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataOvertimeLayoutDisableLayer;

    @FXML
    private AnchorPane borderPaneLayout;
    
    @FXML
    private AnchorPane ancFilteringTable;
    
    ClassFilteringTable cft;

    private boolean isTimeLinePlaying = false;
    
    private void setDataOvertimeSplitpane() {
        spDataOvertime.setDividerPositions(1);
        
        dataOvertimeFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataOvertimeFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataOvertime.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataOvertime.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataOvertimeFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    borderPaneLayout.setDisable(false);
                    tableDataOvertimeLayoutDisableLayer.setDisable(true);
                    borderPaneLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {  //disable
                    borderPaneLayout.setDisable(true);
                    tableDataOvertimeLayoutDisableLayer.setDisable(false);
                    tableDataOvertimeLayoutDisableLayer.toFront();
                }
            }

        });

        dataOvertimeFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataOvertimeLayout;

    private ClassTableWithControl tableDataOvertime;

    private final PseudoClass schedulePseudoClass = PseudoClass.getPseudoClass("datebefore");

    private void initTableDataOvertime() {
        //set table
        //lblColorData.setText("Data tidak dapat diubah / dihapus");
        setTableDataOvertime();
        //set control
        setTableControlDataOvertime();
        //set table-control to content-view
        tableDataOvertimeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataOvertime, 15.0);
        AnchorPane.setLeftAnchor(tableDataOvertime, 15.0);
        AnchorPane.setRightAnchor(tableDataOvertime, 15.0);
        AnchorPane.setTopAnchor(tableDataOvertime, 15.0);
        tableDataOvertimeLayout.getChildren().add(tableDataOvertime);
    }

    //private final PseudoClass cellColor = PseudoClass.getPseudoClass("date");
    private void setTableDataOvertime() {
        TableView<TblCalendarEmployeeOvertime> tableView = new TableView();
        TableColumn<TblCalendarEmployeeOvertime, String> dateOvertime = new TableColumn("Tanggal");
        dateOvertime.setMinWidth(90);
        dateOvertime.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeOvertime, String> param)
                -> Bindings.createStringBinding(() -> (new SimpleDateFormat("dd MMM yyyy")).format(param.getValue().getSysCalendar().getCalendarDate()), param.getValue().getSysCalendar().calendarDateProperty()));
        TableColumn<TblCalendarEmployeeOvertime, String> codeEmployee = new TableColumn("ID");
        codeEmployee.setMaxWidth(100);
        codeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeOvertime, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(), param.getValue().getTblEmployeeByIdemployee().codeEmployeeProperty()));
        TableColumn<TblCalendarEmployeeOvertime, String> nameEmployee = new TableColumn("Nama");
        nameEmployee.setMinWidth(160);
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeOvertime, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(), param.getValue().getTblEmployeeByIdemployee().getTblPeople().fullNameProperty()));
        TableColumn<TblCalendarEmployeeOvertime, String> typeEmployee = new TableColumn("Tipe");
        typeEmployee.setMinWidth(120);
        typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeOvertime, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(), param.getValue().getTblEmployeeByIdemployee().refEmployeeTypeProperty()));
        TableColumn<TblCalendarEmployeeOvertime,String>jobEmployee = new TableColumn("Jabatan");
        jobEmployee.setMinWidth(140);
        jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeOvertime,String>param)
                ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblJob().getJobName(),param.getValue().tblEmployeeByIdemployeeProperty()));
        TableColumn<TblCalendarEmployeeOvertime, String> employee = new TableColumn("Karyawan");
        employee.getColumns().addAll(typeEmployee,codeEmployee, nameEmployee,jobEmployee);
        
        TableColumn<TblCalendarEmployeeOvertime, Time> startWork = new TableColumn("Masuk");
        startWork.setMinWidth(100);
        startWork.setCellValueFactory(cellData -> cellData.getValue().beginTimeProperty());
        TableColumn<TblCalendarEmployeeOvertime, Time> endWork = new TableColumn("Keluar");
        endWork.setMinWidth(100);
        endWork.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        TableColumn<TblCalendarEmployeeOvertime, String> timeWork = new TableColumn("Jam Kerja");
        timeWork.getColumns().addAll(startWork, endWork);
        TableColumn<TblCalendarEmployeeOvertime, String> nominalOvertime = new TableColumn<>("Nominal");
        nominalOvertime.setMinWidth(120);
        nominalOvertime.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeOvertime, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getNominal()),
                        param.getValue().nominalProperty()));
        TableColumn<TblCalendarEmployeeOvertime, String> overtimeNote = new TableColumn("Keterangan");
        overtimeNote.setMinWidth(200);
        overtimeNote.setCellValueFactory(cellData -> cellData.getValue().noteProperty());

        tableView.getColumns().addAll(dateOvertime, employee, timeWork, nominalOvertime, overtimeNote);
        tableView.setItems(loadAllDataOvertime());

        tableView.setRowFactory(tv -> {
            TableRow<TblCalendarEmployeeOvertime> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(schedulePseudoClass, newVal.getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now())));
                    //System.out.println(">>"+newVal.getTblEmployeeByIdemployee().getCodeEmployee());
                }

                //row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
            });

            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataOvertimeUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            System.out.println(">>"+row.getItem().getSysCalendar().getCalendarDate());
                            if(row.getItem().getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))){
                              dataOvertimeShowHandle();
                            }
                            else if(DashboardController.feature.getSelectedRoleFeature().getUpdateData()){
                              dataOvertimeUpdateHandle();
                            }
                        }
                    }
                } else {
                    if ((!row.isEmpty())) {
                        if (isShowStatus.get()) {
                           if(row.getItem().getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))){
                              dataOvertimeShowHandle();
                            }
                           else if(DashboardController.feature.getSelectedRoleFeature().getUpdateData()){
                              dataOvertimeUpdateHandle(); 
                           }
                           
                        }
                    }
                }
            });
            return row;
        });
        tableDataOvertime = new ClassTableWithControl(tableView);
        
        cft = new ClassFilteringTable<>(TblCalendarEmployeeOvertime.class,tableDataOvertime.getTableView(),tableDataOvertime.getTableView().getItems());
        ancFilteringTable.getChildren().clear();
        AnchorPane.setTopAnchor(cft,20.0);
        AnchorPane.setRightAnchor(cft,15.0);
        AnchorPane.setBottomAnchor(cft,15.0);
        //AnchorPane.setLeftAnchor(cft,15.0);
        ancFilteringTable.getChildren().add(cft);
    }

    private ObservableList<TblCalendarEmployeeOvertime> loadAllDataOvertime() {
        return FXCollections.observableArrayList(parentController.getFOvertimeManager().getAllDataOvertime());
    }

    private void setTableControlDataOvertime() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;

        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataOvertimeCreateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataOvertimeUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataOvertimeDeleteHandle();
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
        tableDataOvertime.addButtonControl(buttonControls);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;
    @FXML
    private AnchorPane ancEmployeeType;
    @FXML
    private AnchorPane ancEmployeeName;
    @FXML
    private GridPane gpFormDataOvertime;

    @FXML
    private ScrollPane spFormDataOvertime;
    @FXML
    private JFXDatePicker dpOvertime;
    @FXML
    private JFXTimePicker tmpStartWork;
    @FXML
    private JFXTimePicker tmpEndWork;
    @FXML
    private JFXTextField txtNominal;
    @FXML
    private JFXTextArea txtOvertimeNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblCalendarEmployeeOvertime selectedData;

    private final JFXCComboBoxPopup<RefEmployeeType> cbpTypeEmployee = new JFXCComboBoxPopup(RefEmployeeType.class);
    JFXCComboBoxTablePopup<TblEmployee> cbpNameEmployee;

    private void initFormDataOvertime() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataOvertime.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataOvertime.setOnScroll((ScrollEvent scroll) -> {
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
            dataScheduleCancelHandle();
        });

        cbpTypeEmployee.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {

            }

        });

        initDataPopupLayout();

        initDateCalendar();

        initImportantFieldColor();
        
        initNumbericField();
    }

    private void initDataPopupLayout() {
        initDataPopupEmployeeType();
        ancEmployeeType.getChildren().clear();
        AnchorPane.setTopAnchor(cbpTypeEmployee, 0.0);
        AnchorPane.setBottomAnchor(cbpTypeEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpTypeEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpTypeEmployee, 0.0);
        ancEmployeeType.getChildren().add(cbpTypeEmployee);

        initDataPopupEmployeeByTypeEmployee();
        ancEmployeeName.getChildren().clear();
        AnchorPane.setTopAnchor(cbpNameEmployee, 0.0);
        AnchorPane.setBottomAnchor(cbpNameEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpNameEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpNameEmployee, 0.0);
        ancEmployeeName.getChildren().add(cbpNameEmployee);
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(), dpOvertime);
        ClassFormatter.setDatePickersPattern("dd MMM yyyy", dpOvertime);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dpOvertime,
                cbpTypeEmployee,
                cbpNameEmployee,
                tmpStartWork,
                tmpEndWork,
                txtNominal);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtNominal);
    }
    
    private void initDataPopupEmployeeType() {
        TableView<RefEmployeeType> tableEmployeeType = new TableView();
        TableColumn<RefEmployeeType, String> typeEmployee = new TableColumn<>("Tipe Karyawan");
        typeEmployee.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        tableEmployeeType.getColumns().addAll(typeEmployee);

        ObservableList<RefEmployeeType> employeeTypeItems = FXCollections.observableArrayList(loadAllEmployeeType());

        setFunctionInitPopup(cbpTypeEmployee, tableEmployeeType, employeeTypeItems, "typeName", "Tipe Karyawan *", 300, 300);
          //setFunctionInitPopup(cbpBuilding, tableBuilding, buildingItems, "buildingName", "Building *");

        //gpFormDataOvertime.add(cbpTypeEmployee, 1, 2);
    }

    private List<RefEmployeeType> loadAllEmployeeType() {
        List<RefEmployeeType> list = new ArrayList<>();
        RefEmployeeType all = new RefEmployeeType();
        all.setTypeName("Semua Tipe Karyawan");
        all.setIdtype(3);
        list.add(all);
        list.addAll(parentController.getFOvertimeManager().getAllDataTypeEmployee());
        return list;
    }

    private void initDataPopupEmployeeByTypeEmployee() {
        // System.out.println("employee Type:"+employeeType.getTypeName());
        TableView<TblEmployee> tableEmployee = new TableView();
        TableColumn<TblEmployee, String> codeEmployee = new TableColumn<>("ID");
        codeEmployee.setMinWidth(100);
        codeEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        TableColumn<TblEmployee, String> typeEmployee = new TableColumn<>("Tipe");
        typeEmployee.setMinWidth(120);
        typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefEmployeeType().getTypeName(), param.getValue().refEmployeeTypeProperty()));
        TableColumn<TblEmployee, String> nameEmployee = new TableColumn<>("Nama");
        nameEmployee.setMinWidth(160);
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().getTblPeople().fullNameProperty()));
        TableColumn<TblEmployee,String>jobEmployee = new TableColumn<>("Jabatan");
        jobEmployee.setMinWidth(120);
        jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
                ->Bindings.createStringBinding(()->param.getValue().getTblJob().getJobName(),param.getValue().tblJobProperty()));
        tableEmployee.getColumns().addAll(typeEmployee,codeEmployee,nameEmployee,jobEmployee);

        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList();
        cbpTypeEmployee.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                employeeItems.setAll(loadAllDataEmployee(newVal));
            }
        });
        
        cbpNameEmployee = new JFXCComboBoxTablePopup(TblEmployee.class,tableEmployee,employeeItems,"","Nama Karyawan *",true,500,300);
        //setFunctionInitPopup(cbpNameEmployee, tableEmployee, employeeItems, "codeEmployee", "Nama Karyawan *", 400, 300);
          //setFunctionInitPopup(cbpBuilding, tableBuilding, buildingItems, "buildingName", "Building *");

        //gpFormDataOvertime.add(cbpNameEmployee, 2,2);
    }

    private List<TblEmployee> loadAllDataEmployee(RefEmployeeType employeeType) {
        List<TblEmployee> list = parentController.getFOvertimeManager().getAllDataEmployee(employeeType);
        return list;
    }

    private void setFunctionInitPopup(JFXCComboBoxPopup cbp, TableView table, ObservableList items, String namedFilter, String promptText, double prefWidth, double prefHeight) {

        table.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() != -1) {
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
        button.setOnMouseClicked((e) -> cbp.show());

        BorderPane content = new BorderPane(new JFXButton("SHOW"));

        content.setPrefSize(prefWidth, prefHeight);
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
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

    private void refreshDataPopUp() {
        ObservableList<RefEmployeeType> employeeTypeItems = FXCollections.observableArrayList(loadAllEmployeeType());
        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList();
        cbpTypeEmployee.setItems(employeeTypeItems);
        cbpTypeEmployee.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                employeeItems.setAll(loadAllDataEmployee(newVal));
            }

        });
        cbpNameEmployee.setItems(employeeItems);
    }

    private void refreshDataPopUpUpdate() {
        //ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllEmployeeType());
        //cbpTypeEmployee.setItems(employeeTypeItems);
        RefEmployeeType all = new RefEmployeeType();
        all.setTypeName("Semua Tipe Karyawan");
        all.setIdtype(3);
        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee(all));
        cbpNameEmployee.setItems(employeeItems);
    }
    
    private void setRefreshFiltering(){
       tableDataOvertime.getTableView().setItems((loadAllDataOvertime()));
       cft.refreshFilterItems(tableDataOvertime.getTableView().getItems());
    }
    private void setSelectedDataToInputForm() {
        refreshDataPopUp();
        //txtScheduleName.textProperty().bindBidirectional(selectedData.scheduleNameProperty());
       /*if(selectedData.getSysCalendar()!=null){
         dpOvertime.setValue(((java.sql.Date)selectedData.getSysCalendar().getCalendarDate()).toLocalDate());
         }
         else{
         dpOvertime.setValue(null);
         }
       
         if(selectedData.getBeginTime()!=null)
         {
         tmpStartWork.setValue(selectedData.getBeginTime().toLocalTime());
         }
         else
         {
         tmpStartWork.setValue(null);
         }*/
        dpOvertime.setValue(null);
        //tmpStartWork.setValue(null);
        //tmpEndWork.setValue(null);
        cbpTypeEmployee.setValue(null);
        cbpTypeEmployee.setDisable(false);
        cbpNameEmployee.setDisable(false);
        tmpStartWork.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setBeginTime(Time.valueOf(newVal));
            }
        });

        //selectedData.setBeginTime(Time.valueOf(tmpStartWork.valueProperty().get()));
        if (selectedData.getBeginTime() != null) {
            tmpStartWork.setValue(selectedData.getBeginTime().toLocalTime());
        } else {
            tmpStartWork.setValue(null);
        }

        if (selectedData.getEndTime() != null) {
            tmpEndWork.setValue(selectedData.getEndTime().toLocalTime());
        } else {
            tmpEndWork.setValue(null);
        }

        //selectedData.setEndTime(Time.valueOf(tmpEndWork.valueProperty().get()));
        /*if(selectedData.getTblEmployeeByIdemployee()!=null){
          
         }
         else{
         cbpTypeEmployee.setValue(null);
         }*/
        cbpTypeEmployee.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dataInputStatus == 0) {
                cbpNameEmployee.setValue(null);
            }
        });

        cbpNameEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());
        cbpNameEmployee.hide();
        Bindings.bindBidirectional(txtNominal.textProperty(), selectedData.nominalProperty(), new BigDecimalStringConverter());
        txtOvertimeNote.textProperty().bindBidirectional(selectedData.noteProperty());
        setSelectedDataToInputFormFunctionalComponent();
    }

    private void selectedDataToUpdateForm() {
        refreshDataPopUpUpdate();
        cbpTypeEmployee.setDisable(true);
        cbpNameEmployee.setDisable(true);
        if (selectedData.getSysCalendar() != null) {
            dpOvertime.setValue(((java.sql.Date) selectedData.getSysCalendar().getCalendarDate()).toLocalDate());
        }

        if (selectedData.getBeginTime() != null) {
            tmpStartWork.setValue(selectedData.getBeginTime().toLocalTime());
        }

        tmpStartWork.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setBeginTime(Time.valueOf(newVal));
            }
        });

        if (selectedData.getEndTime() != null) {
            tmpEndWork.setValue(selectedData.getEndTime().toLocalTime());
        }

        tmpEndWork.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setEndTime(Time.valueOf(newVal));
            }
        });

        cbpTypeEmployee.setValue(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType());
        cbpNameEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());

        if (selectedData.getTblEmployeeByIdemployee() != null) {
          
            cbpNameEmployee.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && dataInputStatus == 1) {
                    cbpTypeEmployee.setValue(newVal.getRefEmployeeType());
                }
            });
        }

        Bindings.bindBidirectional(txtNominal.textProperty(), selectedData.nominalProperty(), new BigDecimalStringConverter());
        txtOvertimeNote.textProperty().bindBidirectional(selectedData.noteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //gpFormDataOvertime.setDisable(dataInputStatus == 3);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataOvertime,
                dataInputStatus == 3,dpOvertime,cbpTypeEmployee,cbpNameEmployee);
        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }
    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataOvertimeCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblCalendarEmployeeOvertime();
        selectedData.setNominal(new BigDecimal("0"));
        setSelectedDataToInputForm();
        //open form data warehouse

        dataOvertimeFormShowStatus.set(0);
        dataOvertimeFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataOvertimeUpdateHandle() {
        if (tableDataOvertime.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (!((TblCalendarEmployeeOvertime) tableDataOvertime.getTableView().getSelectionModel().getSelectedItem()).getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))) {
                dataInputStatus = 1;
                selectedData = parentController.getFOvertimeManager().getDataOvertime(((TblCalendarEmployeeOvertime) tableDataOvertime.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
                selectedData.setTblEmployeeByIdemployee(parentController.getFOvertimeManager().getDataOvertime(((TblCalendarEmployeeOvertime) tableDataOvertime.getTableView().getSelectionModel().getSelectedItem()).getIdrelation()).getTblEmployeeByIdemployee());
                selectedDataToUpdateForm();

                dataOvertimeFormShowStatus.set(0);
                dataOvertimeFormShowStatus.set(1);
                //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
            } else {
                ClassMessage.showWarningSelectedDataFromDateNow(null, null);
                //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Silahkan pilih data dimulai dari hari ini..", null);
            }

        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Silahkan pilih data yang akan di ubah..!", null);
        }
    }

    private BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataOvertimeShowHandle() {
        if (tableDataOvertime.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFOvertimeManager().getDataOvertime(((TblCalendarEmployeeOvertime) tableDataOvertime.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
            selectedData.setTblEmployeeByIdemployee(parentController.getFOvertimeManager().getDataOvertime(((TblCalendarEmployeeOvertime) tableDataOvertime.getTableView().getSelectionModel().getSelectedItem()).getIdrelation()).getTblEmployeeByIdemployee());
            selectedDataToUpdateForm();
            dataOvertimeFormShowStatus.set(1);
            isShowStatus.set(true);
        }
    }

    private void dataOvertimeUnshowHandle() {
        ObservableList<TblCalendarEmployeeOvertime> employeeAttendanceItems = FXCollections.observableArrayList(loadAllDataOvertime());
        tableDataOvertime.getTableView().setItems(employeeAttendanceItems);
        dataOvertimeFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataOvertimeDeleteHandle() {
        if (tableDataOvertime.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (!((TblCalendarEmployeeOvertime) tableDataOvertime.getTableView().getSelectionModel().getSelectedItem()).getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now()))) {
                Alert alertDelete = ClassMessage.showConfirmationDeletingDataMessage(null, null);
                if (alertDelete.getResult() == ButtonType.OK) {
                    if (parentController.getFOvertimeManager().deleteDataOvertime((TblCalendarEmployeeOvertime) tableDataOvertime.getTableView().getSelectionModel().getSelectedItem())) {
                        ClassMessage.showSucceedDeletingDataMessage(null, null);
                        tableDataOvertime.getTableView().setItems(loadAllDataOvertime());
                        dataOvertimeFormShowStatus.set(0);
                    } else {
                        ClassMessage.showFailedDeletingDataMessage(parentController.getFOvertimeManager().getErrorMessage(), null);
                    }
                } else {
                    dataOvertimeFormShowStatus.set(0);
                }
            } else {
                ClassMessage.showWarningSelectedDataFromDateNow(null, null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataSchedulePrintHandle() {

    }

    private void dataScheduleSaveHandle() {
        if (checkDataInputDataOvertime()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);//HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION,"CONFIRMATION","Are you sure want to input this data?", null);
            if (alert.getResult() == ButtonType.OK) {
                // dummySelected : object cloning u/ disimpan ke database
                selectedData.setBeginTime(Time.valueOf(tmpStartWork.getValue()));
                selectedData.setEndTime(Time.valueOf(tmpEndWork.getValue()));
                TblCalendarEmployeeOvertime dummySelectedData = new TblCalendarEmployeeOvertime(selectedData);
                if (selectedData.getTblEmployeeByIdemployee() != null) {
                    dummySelectedData.setTblEmployeeByIdemployee(dummySelectedData.getTblEmployeeByIdemployee());
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFOvertimeManager().insertDataOvertime(dummySelectedData, Date.valueOf(dpOvertime.getValue())) != null) {
                            // HotelFX.showAlert(Alert.AlertType.INFORMATION,"BERHASIL","Input data berhasil!!",null);
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            tableDataOvertime.getTableView().setItems(loadAllDataOvertime());
                            dataOvertimeFormShowStatus.set(0);
                            setRefreshFiltering();
                            //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFOvertimeManager().getErrorMessage(), null);
                            //HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Input data gagal..!", parentController.getFOvertimeManager().getErrorMessage());
                        }
                        break;
                    case 1:
                        if (parentController.getFOvertimeManager().updateDataOvertime(dummySelectedData, Date.valueOf(dpOvertime.getValue()))) {
                            //refresh data from table & close form data warehouse
                            //HotelFX.showAlert(Alert.AlertType.INFORMATION,"BERHASIL","Data berhasil diubah!!",null);
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            tableDataOvertime.getTableView().setItems(loadAllDataOvertime());
                            dataOvertimeFormShowStatus.set(0);
                            setRefreshFiltering();
                            //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFOvertimeManager().getErrorMessage(), null);
                            //HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal diubah..!", parentController.getFOvertimeManager().getErrorMessage());
                        }
                        break;
                    default:
                        break;
                }
            } else {
                dataOvertimeFormShowStatus.set(1);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errInput, null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Silahkan periksa data yang diinput..!", errInput);
        }
    }

    private void dataScheduleCancelHandle() {
        //refresh data from table & close form data warehouse
        tableDataOvertime.getTableView().setItems(loadAllDataOvertime());
        dataOvertimeFormShowStatus.set(0);
        setRefreshFiltering();
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private String errInput;

    private boolean checkDataInputDataOvertime() {
        boolean dataInput = true;
        errInput = "";

        if (dpOvertime.getValue() == null) {
            dataInput = false;
            errInput += "Tanggal Lembur:" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }
        if (tmpStartWork.getValue() == null) {
            dataInput = false;
            errInput += "Jam Masuk: " + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (tmpEndWork.getValue() == null) {
            dataInput = false;
            errInput += "Jam Keluar:" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (cbpTypeEmployee.getValue() == null) {
            dataInput = false;
            errInput += "Tipe Karyawan:" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (cbpNameEmployee.getValue() == null) {
            dataInput = false;
            errInput += "Nama Karyawan:" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (txtNominal.getText() == null 
                || txtNominal.getText().equals("")
                || txtNominal.getText().equals("-")) {
            dataInput = false;
            errInput += "Nominal:" + ClassMessage.defaultErrorNullValueMessage + "\n";
        } else {
            if (selectedData.getNominal().compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errInput += "Nominal: " + ClassMessage.defaultErrorZeroValueMessage + "\n";
            }
        }

        return dataInput;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataOvertimeSplitpane();
        initTableDataOvertime();
        initFormDataOvertime();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public OvertimeController(FeatureOvertimeController parentController) {
        this.parentController = parentController;
    }

    private final FeatureOvertimeController parentController;

}
