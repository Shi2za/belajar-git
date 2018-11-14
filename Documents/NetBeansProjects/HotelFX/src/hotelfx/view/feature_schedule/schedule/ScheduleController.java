/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_schedule.schedule;

import hotelfx.view.feature_warehouse.warehouse.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblEmployeeSchedule;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_schedule.FeatureScheduleController;
import hotelfx.view.feature_warehouse.FeatureWarehouseController;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ScheduleController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataSchedule;

    private DoubleProperty dataScheduleFormShowStatus;

    @FXML
    private AnchorPane borderPaneLayout;
    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataScheduleLayoutDisableLayer;

    private ClassFilteringTable cft;

    private boolean isTimeLinePlaying = false;

    private void setDataScheduleSplitpane() {
        spDataSchedule.setDividerPositions(1);

        dataScheduleFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataScheduleFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataSchedule.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataSchedule.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataScheduleFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    borderPaneLayout.setDisable(false);
                    tableDataScheduleLayoutDisableLayer.setDisable(true);
                    borderPaneLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    borderPaneLayout.setDisable(true);
                    tableDataScheduleLayoutDisableLayer.setDisable(false);
                    tableDataScheduleLayoutDisableLayer.toFront();
                }
            }

        });

        dataScheduleFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataScheduleLayout;
    @FXML
    private AnchorPane ancFiltering;

    private ClassTableWithControl tableDataSchedule;

    private void initTableDataSchedule() {
        //set table
        setTableDataSchedule();
        //set control
        setTableControlDataSchedule();
        //set table-control to content-view
        tableDataScheduleLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataSchedule, 15.0);
        AnchorPane.setLeftAnchor(tableDataSchedule, 15.0);
        AnchorPane.setRightAnchor(tableDataSchedule, 15.0);
        AnchorPane.setTopAnchor(tableDataSchedule, 15.0);
        tableDataScheduleLayout.getChildren().add(tableDataSchedule);
    }

    private void setTableDataSchedule() {
        TableView<TblEmployeeSchedule> tableView = new TableView();
        TableColumn<TblEmployeeSchedule, String> scheduleName = new TableColumn("Nama Jadwal");
        scheduleName.setMinWidth(160);
        scheduleName.setCellValueFactory(cellData -> cellData.getValue().scheduleNameProperty());
        TableColumn<TblEmployeeSchedule, Time> startWork = new TableColumn("Masuk");
        startWork.setMinWidth(120);
        startWork.setCellValueFactory(cellData -> cellData.getValue().beginTimeProperty());
        TableColumn<TblEmployeeSchedule, Time> endWork = new TableColumn("Keluar");
        endWork.setMinWidth(120);
        endWork.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        TableColumn<TblEmployeeSchedule, String> timeWork = new TableColumn("Jam Kerja");
        timeWork.getColumns().addAll(startWork, endWork);
        TableColumn<TblEmployeeSchedule, String> scheduleNote = new TableColumn("Keterangan");
        scheduleNote.setMinWidth(200);
        scheduleNote.setCellValueFactory(cellData -> cellData.getValue().scheduleNoteProperty());
        tableView.getColumns().addAll(scheduleName, timeWork, scheduleNote);
        tableView.setItems(loadAllDataSchedule());

        tableView.setRowFactory(tv -> {
            TableRow<TblEmployeeSchedule> row = new TableRow();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataScheduleUnshowHandle();
                    } else {
                        if (!row.isEmpty()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataScheduleUpdateHandle();
                            } else {
                                dataScheduleShowHandle();
                            }

                        }
                    }
                } else {
                    if (!row.isEmpty()) {
                        if (isShowStatus.get()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataScheduleUpdateHandle();
                            } else {
                                dataScheduleShowHandle();
                            }

                        }
                    }
                }
            });
            return row;
        });
        tableDataSchedule = new ClassTableWithControl(tableView);

        cft = new ClassFilteringTable<>(TblEmployeeSchedule.class, tableDataSchedule.getTableView(), tableDataSchedule.getTableView().getItems());
        ancFiltering.getChildren().clear();
        AnchorPane.setTopAnchor(cft, 15.0);
        AnchorPane.setBottomAnchor(cft, 0.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        ancFiltering.getChildren().add(cft);
    }

    private ObservableList<TblEmployeeSchedule> loadAllDataSchedule() {
        return FXCollections.observableArrayList(parentController.getScheduleManager().getAllDataSchedule());
    }

    private void setTableControlDataSchedule() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataScheduleCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataScheduleUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataScheduleDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataSchedulePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataSchedule.addButtonControl(buttonControls);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataSchedule;

    @FXML
    private ScrollPane spFormDataSchedule;
    @FXML
    private JFXTextField txtIdSchedule;
    @FXML
    private JFXTextField txtScheduleName;
    @FXML
    private JFXTimePicker tmpStartWork;
    @FXML
    private JFXTimePicker tmpEndWork;
    @FXML
    private JFXTextArea txtScheduleNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblEmployeeSchedule selectedData;

    private void initFormDataSchedule() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataSchedule.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataSchedule.setOnScroll((ScrollEvent scroll) -> {
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

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtScheduleName,
                tmpStartWork,
                tmpEndWork);
    }

    private void setRefreshFiltering() {
        tableDataSchedule.getTableView().setItems(loadAllDataSchedule());
        cft.refreshFilterItems(tableDataSchedule.getTableView().getItems());
    }

    private void setSelectedDataToInputForm() {
        //txtIdSchedule.textProperty().bindBidirectional(selectedData.codeScheduleProperty());
        txtScheduleName.textProperty().bindBidirectional(selectedData.scheduleNameProperty());
        if (selectedData.getBeginTime() != null) {
            tmpStartWork.setValue(selectedData.getBeginTime().toLocalTime());
        } else {
            tmpStartWork.setValue(null);
        }

        /*tmpStartWork.valueProperty().addListener((obs, oldVal, newVal) -> {
         selectedData.setBeginTime(Time.valueOf(newVal));
         });*/
        if (selectedData.getEndTime() != null) {
            tmpEndWork.setValue(selectedData.getEndTime().toLocalTime());
        } else {
            tmpEndWork.setValue(null);
        }

        /*tmpEndWork.valueProperty().addListener((obs, oldVal, newVal) -> {
         selectedData.setEndTime(Time.valueOf(newVal));
         });*/
        txtScheduleNote.textProperty().bindBidirectional(selectedData.scheduleNoteProperty());
        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        gpFormDataSchedule.setDisable(dataInputStatus == 3);
        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataScheduleCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblEmployeeSchedule();
        setSelectedDataToInputForm();
        //open form data warehouse
        dataScheduleFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataScheduleUpdateHandle() {
        if (tableDataSchedule.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getScheduleManager().getSchedule(((TblEmployeeSchedule) tableDataSchedule.getTableView().getSelectionModel().getSelectedItem()).getIdschedule());
            setSelectedDataToInputForm();
            //open form data warehouse
            dataScheduleFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
        }
    }

    private BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataScheduleShowHandle() {
        if (tableDataSchedule.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getScheduleManager().getSchedule(((TblEmployeeSchedule) tableDataSchedule.getTableView().getSelectionModel().getSelectedItem()).getIdschedule());
            setSelectedDataToInputForm();
            isShowStatus.set(true);
            dataScheduleFormShowStatus.set(1);
        }
    }

    private void dataScheduleUnshowHandle() {
        tableDataSchedule.getTableView().setItems(loadAllDataSchedule());
        dataScheduleFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataScheduleDeleteHandle() {
        if (tableDataSchedule.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage(null, null);//HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION,"CONFIRMATION","Are you sure want to delete this data?", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getScheduleManager().deleteDataSchedule((TblEmployeeSchedule) tableDataSchedule.getTableView().getSelectionModel().getSelectedItem())) {
                    //refresh data from table & close form data warehouse
                    //HotelFX.showAlert(Alert.AlertType.INFORMATION,"SUCCESSED","Deleting data successed!!",null);
                    ClassMessage.showSucceedDeletingDataMessage(null, null);
                    tableDataSchedule.getTableView().setItems(loadAllDataSchedule());
                    dataScheduleFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getScheduleManager().getErrorMessage(), null);
                    //HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Deleting data failed..!", parentController.getScheduleManager().getErrorMessage());
                }
            } else {
                dataScheduleFormShowStatus.set(0);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
        }
    }

    private void dataSchedulePrintHandle() {

    }

    private void dataScheduleSaveHandle() {
        if (checkDataInputDataSchedule()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);//HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION,"CONFIRMATION","Are you sure want to input this data?", null);
            if (alert.getResult() == ButtonType.OK) {
                selectedData.setBeginTime(Time.valueOf(tmpStartWork.getValue()));
                selectedData.setEndTime(Time.valueOf(tmpEndWork.getValue()));
                TblEmployeeSchedule dummySelected = new TblEmployeeSchedule(selectedData);

                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getScheduleManager().insertDataSchedule(selectedData) != null) {
                            //refresh data from table & close form data warehouse
                            //HotelFX.showAlert(Alert.AlertType.INFORMATION,"SUCCESSED","Inserting data successed!!",null);
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            tableDataSchedule.getTableView().setItems(loadAllDataSchedule());
                            dataScheduleFormShowStatus.set(0);
                            setRefreshFiltering();
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getScheduleManager().getErrorMessage(), null);
                            //HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Inserting data failed..!", parentController.getScheduleManager().getErrorMessage());
                        }
                        break;
                    case 1:
                        if (parentController.getScheduleManager().updateDataSchedule(selectedData)) {
                            //refresh data from table & close form data warehouse
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //HotelFX.showAlert(Alert.AlertType.INFORMATION,"SUCCESSED","Updating data successed!!",null);
                            tableDataSchedule.getTableView().setItems(loadAllDataSchedule());
                            dataScheduleFormShowStatus.set(0);
                            setRefreshFiltering();
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getScheduleManager().getErrorMessage(), null);
                            //HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Updating data failed..!", parentController.getScheduleManager().getErrorMessage());
                        }
                        break;
                    default:
                        break;
                }
            } else {
                dataScheduleFormShowStatus.set(1);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errInput, null);
            //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please check data input..!", errInput);
        }
    }

    private void dataScheduleCancelHandle() {
        //refresh data from table & close form data warehouse
        tableDataSchedule.getTableView().setItems(loadAllDataSchedule());
        dataScheduleFormShowStatus.set(0);
        setRefreshFiltering();
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private String errInput;

    private boolean checkDataInputDataSchedule() {
        boolean dataInput = true;
        errInput = "";
        if (txtScheduleName.getText() == null || txtScheduleName.getText().equals("")) {
            dataInput = false;
            errInput += "Nama Jadwal:" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (tmpStartWork.getValue() == null) {
            dataInput = false;
            errInput += "Jam Masuk: " + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (tmpEndWork.getValue() == null) {
            dataInput = false;
            errInput += "Jam Keluar:" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        return dataInput;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataScheduleSplitpane();

        //init table
        initTableDataSchedule();

        //init form
        initFormDataSchedule();

        spDataSchedule.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataScheduleFormShowStatus.set(0);
        });
    }

    public ScheduleController(FeatureScheduleController parentController) {
        this.parentController = parentController;
    }

    private final FeatureScheduleController parentController;

}
