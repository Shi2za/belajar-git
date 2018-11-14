/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.data_room_check;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
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
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomCheck;
import hotelfx.persistence.model.TblRoomCheckItemMutationHistory;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FRoomCheckManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_room_check.RoomCheckController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.ZoneId;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class DataRoomCheckController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRoomCheck;

    private DoubleProperty dataRoomCheckFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRoomCheckLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataRoomCheckSplitpane() {
        spDataRoomCheck.setDividerPositions(1);

        dataRoomCheckFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRoomCheckFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRoomCheck.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRoomCheck.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRoomCheckFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataRoomCheckLayout.setDisable(false);
                    tableDataRoomCheckLayoutDisableLayer.setDisable(true);
                    tableDataRoomCheckLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataRoomCheckLayout.setDisable(true);
                    tableDataRoomCheckLayoutDisableLayer.setDisable(false);
                    tableDataRoomCheckLayoutDisableLayer.toFront();
                }
            }
        });

        dataRoomCheckFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataRoomCheckLayout;

    private ClassFilteringTable<TblRoomCheck> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRoomCheck;

    private void initTableDataRoomCheck() {
        //set table
        setTableDataRoomCheck();
        //set control
        setTableControlDataRoomCheck();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoomCheck, 15.0);
        AnchorPane.setLeftAnchor(tableDataRoomCheck, 15.0);
        AnchorPane.setRightAnchor(tableDataRoomCheck, 15.0);
        AnchorPane.setTopAnchor(tableDataRoomCheck, 15.0);
        ancBodyLayout.getChildren().add(tableDataRoomCheck);
    }

    private void setTableDataRoomCheck() {
        TableView<TblRoomCheck> tableView = new TableView();

        TableColumn<TblRoomCheck, String> checkDateTime = new TableColumn("Tgl/Waktu Cek");
        checkDateTime.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheck, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckDateTime()), param.getValue().checkDateTimeProperty()));
        checkDateTime.setMinWidth(160);

        TableColumn<TblRoomCheck, String> roomName = new TableColumn<>("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheck, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoom().getRoomName(), param.getValue().tblRoomProperty()));
        roomName.setMinWidth(140);

        TableColumn<TblRoomCheck, String> checkerID = new TableColumn<>("Pengecek");
        checkerID.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheck, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdchecker().getCodeEmployee(), param.getValue().tblEmployeeByIdcheckerProperty()));
        checkerID.setMinWidth(140);

        tableView.getColumns().addAll(checkDateTime, roomName, checkerID);

        tableView.setItems(loadAllDataRoomCheck());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoomCheck> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataRoomCheckUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataRoomCheckUpdateHandle();
                            } else {
                                dataRoomCheckShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataRoomCheckUpdateHandle();
//                            } else {
//                                dataRoomCheckShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataRoomCheck = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblRoomCheck.class,
                tableDataRoomCheck.getTableView(),
                tableDataRoomCheck.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataRoomCheck() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataRoomCheckPreCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomCheckUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "Are you sure want to delete this data?", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    //listener delete
//                    dataRoomCheckDeleteHandle();
//                }
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataRoomCheckPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataRoomCheck.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomCheck> loadAllDataRoomCheck() {
        List<TblRoomCheck> list = parentController.getFRoomCheckManager().getAllDataRoomCheck();
        for (TblRoomCheck data : list) {
            //data room
            data.setTblRoom(parentController.getFRoomCheckManager().getDataRoom(data.getTblRoom().getIdroom()));
            //data employee
            data.setTblEmployeeByIdchecker(parentController.getFRoomCheckManager().getDataEmployee(data.getTblEmployeeByIdchecker().getIdemployee()));
            //data room change status history
            data.setTblRoomStatusChangeHistory(parentController.getFRoomCheckManager().getDataRoomStatusChangeHistory(data.getTblRoomStatusChangeHistory().getIdchangeRoomStatus()));
            //data room status (old)
            data.getTblRoomStatusChangeHistory().setRefRoomStatusByRoomStatusOld(parentController.getFRoomCheckManager().getDataRoomStatus(data.getTblRoomStatusChangeHistory().getRefRoomStatusByRoomStatusOld().getIdstatus()));
            //data room status (new)
            data.getTblRoomStatusChangeHistory().setRefRoomStatusByRoomStatusNew(parentController.getFRoomCheckManager().getDataRoomStatus(data.getTblRoomStatusChangeHistory().getRefRoomStatusByRoomStatusNew().getIdstatus()));

        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataRoomCheck;

    @FXML
    private ScrollPane spFormDataRoomCheck;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private AnchorPane ancEmployeeLayout;
    private JFXCComboBoxTablePopup<TblEmployee> cbpEmployee;

    @FXML
    private JFXDatePicker dtpCheckDate;

    @FXML
    private JFXTimePicker tmpCheckTime;

    @FXML
    private JFXTextField txtRoomStatusChangeHistory;

    @FXML
    private JFXTextArea txtNote;

    @FXML
    private Label lblNotes;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblRoomCheck selectedData;

    public List<TblItemMutationHistoryPropertyBarcode> itemMutationHistoryPropertyBarcodes = new ArrayList<>();

    public List<TblItemMutationHistoryItemExpiredDate> itemMutationHistoryItemExpiredDates = new ArrayList<>();

    private void initFormDataRoomCheck() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRoomCheck.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRoomCheck.setOnScroll((ScrollEvent scroll) -> {
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

        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Cek-Kamar)"));
        btnSave.setOnAction((e) -> {
            dataRoomCheckSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoomCheckCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpCheckDate);
//        ClassFormatter.setDatePickersEnableDate(LocalDate.now(),
//                dtpCheckDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpEmployee);
    }

    private void initDataPopup() {
        //Employee
        TableView<TblEmployee> tableEmployee = new TableView<>();

        TableColumn<TblEmployee, String> codeEmployee = new TableColumn<>("ID");
        codeEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        codeEmployee.setMinWidth(140);

        TableColumn<TblEmployee, String> employeeName = new TableColumn<>("Nama");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().tblPeopleProperty()));
        employeeName.setMinWidth(140);

        tableEmployee.getColumns().addAll(codeEmployee, employeeName);

        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpEmployee = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableEmployee, employeeItems, "", "Pengecek *", true, 300, 300
        );

        //attached to grid-pane
        ancEmployeeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpEmployee, 0.0);
        AnchorPane.setTopAnchor(cbpEmployee, 0.0);
        ancEmployeeLayout.getChildren().add(cbpEmployee);
    }

    private ObservableList<TblEmployee> loadAllDataEmployee() {
        List<TblEmployee> list = parentController.getFRoomCheckManager().getAllDataEmployee();
        for (TblEmployee data : list) {
            data.setTblPeople(parentController.getFRoomCheckManager().getDataPeople(data.getTblPeople().getIdpeople()));
        }
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Employee
        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
        cbpEmployee.setItems(employeeItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeRoomCheck() != null
                ? "" : "");

        txtRoomName.textProperty().bind(selectedData.getTblRoom().roomNameProperty());
        txtRoomStatusChangeHistory.setText("Perubahan Status Kamar dari '"
                + selectedData.getTblRoomStatusChangeHistory().getRefRoomStatusByRoomStatusOld().getStatusName()
                + "' menjadi '"
                + selectedData.getTblRoomStatusChangeHistory().getRefRoomStatusByRoomStatusNew().getStatusName()
                + "'");
        txtNote.textProperty().bindBidirectional(selectedData.checkNoteProperty());

        if (selectedData.getCheckDateTime() != null) {
            dtpCheckDate.setValue(selectedData.getCheckDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            tmpCheckTime.setValue(selectedData.getCheckDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        } else {
            dtpCheckDate.setValue(null);
            tmpCheckTime.setValue(null);
        }

//        dtpCheckDate.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null && tmpCheckTime.getValue() != null) {
//                selectedData.getCheckDateTime().setDate(Date.valueOf(newVal).getDate());
//            }
//        });
//        tmpCheckTime.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null && dtpCheckDate.getValue() != null) {
//                selectedData.getCheckDateTime().setTime(Time.valueOf(newVal).getTime());
//            }
//        });
        cbpEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdcheckerProperty());

        cbpEmployee.hide();

        lblNotes.setVisible(dataInputStatus != 3);

        initTableDataIn();

        initTableDataOut();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //if updating, data can't be changed
        dtpCheckDate.setDisable(dataInputStatus == 1);
        tmpCheckTime.setDisable(dataInputStatus == 1);
        txtRoomStatusChangeHistory.setDisable(dataInputStatus == 1);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataRoomCheck,
                dataInputStatus == 3,
                dtpCheckDate,
                tmpCheckTime,
                txtRoomStatusChangeHistory);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    public Stage dialogStage;

    private void dataRoomCheckPreCreateHandle() {
        selectedData = new TblRoomCheck();
        //show dialog
        showDataRoomCheckRoomChooseDialog();
    }

    private void showDataRoomCheckRoomChooseDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/data_room_check/RCRoomChooseDialog.fxml"));

            RCRoomChooseController controller = new RCRoomChooseController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
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

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    public void dataRoomCheckCreateHandle() {
        dataInputStatus = 0;
//        selectedData = new TblRoomCheck();
        itemMutationHistoryPropertyBarcodes = new ArrayList<>();
        itemMutationHistoryItemExpiredDates = new ArrayList<>();
        setSelectedDataToInputForm();
        //open form data room check
        dataRoomCheckFormShowStatus.set(0.0);
        dataRoomCheckFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataRoomCheckUpdateHandle() {
        if (tableDataRoomCheck.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFRoomCheckManager().getDataRoomCheck(((TblRoomCheck) tableDataRoomCheck.getTableView().getSelectionModel().getSelectedItem()).getIdroomCheck());
            selectedData.setTblRoom(parentController.getFRoomCheckManager().getDataRoom(selectedData.getTblRoom().getIdroom()));
            selectedData.getTblRoom().setTblRoomType(parentController.getFRoomCheckManager().getDataRoomType(selectedData.getTblRoom().getTblRoomType().getIdroomType()));
            selectedData.setTblEmployeeByIdchecker(parentController.getFRoomCheckManager().getDataEmployee(selectedData.getTblEmployeeByIdchecker().getIdemployee()));
            itemMutationHistoryPropertyBarcodes = new ArrayList<>();
            itemMutationHistoryItemExpiredDates = new ArrayList<>();
            setSelectedDataToInputForm();
            //open form data room check
            dataRoomCheckFormShowStatus.set(0.0);
            dataRoomCheckFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataRoomCheckShowHandle() {
        if (tableDataRoomCheck.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFRoomCheckManager().getDataRoomCheck(((TblRoomCheck) tableDataRoomCheck.getTableView().getSelectionModel().getSelectedItem()).getIdroomCheck());
            selectedData.setTblRoom(parentController.getFRoomCheckManager().getDataRoom(selectedData.getTblRoom().getIdroom()));
            selectedData.getTblRoom().setTblRoomType(parentController.getFRoomCheckManager().getDataRoomType(selectedData.getTblRoom().getTblRoomType().getIdroomType()));
            selectedData.setTblEmployeeByIdchecker(parentController.getFRoomCheckManager().getDataEmployee(selectedData.getTblEmployeeByIdchecker().getIdemployee()));
            itemMutationHistoryPropertyBarcodes = new ArrayList<>();
            itemMutationHistoryItemExpiredDates = new ArrayList<>();
            setSelectedDataToInputForm();
            dataRoomCheckFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataRoomCheckUnshowHandle() {
        refreshDataTableRoomCheck();
        dataRoomCheckFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataRoomCheckDeleteHandle() {
        if (tableDataRoomCheck.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (parentController.getFRoomCheckManager().deleteDataRoomCheck((TblRoomCheck) tableDataRoomCheck.getTableView().getSelectionModel().getSelectedItem())) {
                ClassMessage.showSucceedDeletingDataMessage("", null);
                //refresh data from table & close form data room check
                refreshDataTableRoomCheck();
                dataRoomCheckFormShowStatus.set(0.0);
            } else {
                ClassMessage.showFailedDeletingDataMessage(parentController.getFRoomCheckManager().getErrorMessage(), null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataRoomCheckPrintHandle() {

    }

    private void dataRoomCheckSaveHandle() {
        if (checkDataInputDataRoomCheck()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //            selectedData.setCheckDateTime(Date.valueOf(dtpCheckDate.getValue()));
//            selectedData.getCheckDateTime().setTime(Time.valueOf(tmpCheckTime.getValue()).getTime());
                //dummy entry
                TblRoomCheck dummySelectedData = new TblRoomCheck(selectedData);
                dummySelectedData.setTblRoom(new TblRoom(dummySelectedData.getTblRoom()));
                List<TblRoomCheckItemMutationHistory> dummyDataRCIMHins = new ArrayList<>();
                List<TblRoomCheckItemMutationHistory> dummyDataRCIMHouts = new ArrayList<>();
                List<TblItemMutationHistoryPropertyBarcode> dummyItemMutationHistoryPropertyBarcodes = new ArrayList<>();
                List<TblItemMutationHistoryItemExpiredDate> dummyItemMutationHistoryItemExpiredDates = new ArrayList<>();
                for (TblRoomCheckItemMutationHistory dataRCIMHin : (List<TblRoomCheckItemMutationHistory>) tableDataIn.getTableView().getItems()) {
                    TblRoomCheckItemMutationHistory dummyDataRCIMHin = new TblRoomCheckItemMutationHistory(dataRCIMHin);
                    dummyDataRCIMHin.setTblRoomCheck(dummySelectedData);
                    dummyDataRCIMHin.setTblItemMutationHistory(new TblItemMutationHistory(dummyDataRCIMHin.getTblItemMutationHistory()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setTblItem(new TblItem(dummyDataRCIMHin.getTblItemMutationHistory().getTblItem()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation(dummyDataRCIMHin.getTblItemMutationHistory().getTblLocationByIdlocationOfSource()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(new TblLocation(dummyDataRCIMHin.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setRefItemMutationType(new RefItemMutationType(dummyDataRCIMHin.getTblItemMutationHistory().getRefItemMutationType()));
                    if (dummyDataRCIMHin.getTblItemMutationHistory().getTblEmployeeByIdemployee() != null) {
                        dummyDataRCIMHin.getTblItemMutationHistory().setTblEmployeeByIdemployee(new TblEmployee(dummyDataRCIMHin.getTblItemMutationHistory().getTblEmployeeByIdemployee()));
                    }
                    if (dummyDataRCIMHin.getTblItemMutationHistory().getRefItemObsoleteBy() != null) {
                        dummyDataRCIMHin.getTblItemMutationHistory().setRefItemObsoleteBy(new RefItemObsoleteBy(dummyDataRCIMHin.getTblItemMutationHistory().getRefItemObsoleteBy()));
                    }
                    for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : itemMutationHistoryPropertyBarcodes) {
                        if (itemMutationHistoryPropertyBarcode.getTblItemMutationHistory().equals(dataRCIMHin.getTblItemMutationHistory())) {
                            TblItemMutationHistoryPropertyBarcode dummyItemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode(itemMutationHistoryPropertyBarcode);
                            dummyItemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dummyDataRCIMHin.getTblItemMutationHistory());
                            dummyItemMutationHistoryPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(dummyItemMutationHistoryPropertyBarcode.getTblPropertyBarcode()));
                            dummyItemMutationHistoryPropertyBarcodes.add(dummyItemMutationHistoryPropertyBarcode);
                        }
                    }
                    for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : itemMutationHistoryItemExpiredDates) {
                        if (itemMutationHistoryItemExpiredDate.getTblItemMutationHistory().equals(dataRCIMHin.getTblItemMutationHistory())) {
                            TblItemMutationHistoryItemExpiredDate dummyItemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate(itemMutationHistoryItemExpiredDate);
                            dummyItemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dummyDataRCIMHin.getTblItemMutationHistory());
                            dummyItemMutationHistoryItemExpiredDate.setTblItemExpiredDate(new TblItemExpiredDate(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate()));
                            dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().setTblItem(new TblItem(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getTblItem()));
                            dummyItemMutationHistoryItemExpiredDates.add(dummyItemMutationHistoryItemExpiredDate);
                        }
                    }
                    dummyDataRCIMHins.add(dummyDataRCIMHin);
                }
                for (TblRoomCheckItemMutationHistory dataRCIMHout : (List<TblRoomCheckItemMutationHistory>) tableDataOut.getTableView().getItems()) {
                    TblRoomCheckItemMutationHistory dummyDataRCIMHout = new TblRoomCheckItemMutationHistory(dataRCIMHout);
                    dummyDataRCIMHout.setTblRoomCheck(dummySelectedData);
                    dummyDataRCIMHout.setTblItemMutationHistory(new TblItemMutationHistory(dummyDataRCIMHout.getTblItemMutationHistory()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setTblItem(new TblItem(dummyDataRCIMHout.getTblItemMutationHistory().getTblItem()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation(dummyDataRCIMHout.getTblItemMutationHistory().getTblLocationByIdlocationOfSource()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(new TblLocation(dummyDataRCIMHout.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setRefItemMutationType(new RefItemMutationType(dummyDataRCIMHout.getTblItemMutationHistory().getRefItemMutationType()));
                    if (dummyDataRCIMHout.getTblItemMutationHistory().getTblEmployeeByIdemployee() != null) {
                        dummyDataRCIMHout.getTblItemMutationHistory().setTblEmployeeByIdemployee(new TblEmployee(dummyDataRCIMHout.getTblItemMutationHistory().getTblEmployeeByIdemployee()));
                    }
                    if (dummyDataRCIMHout.getTblItemMutationHistory().getRefItemObsoleteBy() != null) {
                        dummyDataRCIMHout.getTblItemMutationHistory().setRefItemObsoleteBy(new RefItemObsoleteBy(dummyDataRCIMHout.getTblItemMutationHistory().getRefItemObsoleteBy()));
                    }
                    for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : itemMutationHistoryPropertyBarcodes) {
                        if (itemMutationHistoryPropertyBarcode.getTblItemMutationHistory().equals(dataRCIMHout.getTblItemMutationHistory())) {
                            TblItemMutationHistoryPropertyBarcode dummyItemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode(itemMutationHistoryPropertyBarcode);
                            dummyItemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dummyDataRCIMHout.getTblItemMutationHistory());
                            dummyItemMutationHistoryPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(dummyItemMutationHistoryPropertyBarcode.getTblPropertyBarcode()));
                            dummyItemMutationHistoryPropertyBarcodes.add(dummyItemMutationHistoryPropertyBarcode);
                        }
                    }
                    for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : itemMutationHistoryItemExpiredDates) {
                        if (itemMutationHistoryItemExpiredDate.getTblItemMutationHistory().equals(dataRCIMHout.getTblItemMutationHistory())) {
                            TblItemMutationHistoryItemExpiredDate dummyItemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate(itemMutationHistoryItemExpiredDate);
                            dummyItemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dummyDataRCIMHout.getTblItemMutationHistory());
                            dummyItemMutationHistoryItemExpiredDate.setTblItemExpiredDate(new TblItemExpiredDate(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate()));
                            dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().setTblItem(new TblItem(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getTblItem()));
                            dummyItemMutationHistoryItemExpiredDates.add(dummyItemMutationHistoryItemExpiredDate);
                        }
                    }
                    dummyDataRCIMHouts.add(dummyDataRCIMHout);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFRoomCheckManager().insertDataRoomCheck(dummySelectedData,
                                dummyDataRCIMHins,
                                dummyDataRCIMHouts,
                                dummyItemMutationHistoryPropertyBarcodes,
                                dummyItemMutationHistoryItemExpiredDates
                        ) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data room check
//                        refreshDataTableRoomCheck();
//                        dataRoomCheckFormShowStatus.set(0.0);
                            parentController.refreshAllContent();
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFRoomCheckManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFRoomCheckManager().updateDataRoomCheck(dummySelectedData,
                                dummyDataRCIMHins,
                                dummyDataRCIMHouts,
                                dummyItemMutationHistoryPropertyBarcodes,
                                dummyItemMutationHistoryItemExpiredDates
                        )) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data room check
//                        refreshDataTableRoomCheck();
//                        dataRoomCheckFormShowStatus.set(0.0);
                            parentController.refreshAllContent();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFRoomCheckManager().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataRoomCheckCancelHandle() {
        //refresh data from table & close form data room check
        refreshDataTableRoomCheck();
        dataRoomCheckFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableRoomCheck() {
        tableDataRoomCheck.getTableView().setItems(loadAllDataRoomCheck());
        cft.refreshFilterItems(tableDataRoomCheck.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataRoomCheck() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpEmployee.getValue() == null) {
            dataInput = false;
            errDataInput += "Pengecek Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpCheckDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Cek Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (tmpCheckTime.getValue() == null) {
            dataInput = false;
            errDataInput += "Waktu Cek Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private final PseudoClass uneditablePseudoClass = PseudoClass.getPseudoClass("uneditable");

    /**
     * DATA (IN)
     */
    @FXML
    private AnchorPane tableDataInLayout;

    public ClassTableWithControl tableDataIn;

    private void initTableDataIn() {
        //set table
        setTableDataIn();
        //set control
        setTableControlDataIn();
        //set table-control to content-view
        tableDataInLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataIn, 0.0);
        AnchorPane.setLeftAnchor(tableDataIn, 0.0);
        AnchorPane.setRightAnchor(tableDataIn, 0.0);
        AnchorPane.setTopAnchor(tableDataIn, 0.0);
        tableDataInLayout.getChildren().add(tableDataIn);
    }

    private void setTableDataIn() {
        TableView<TblRoomCheckItemMutationHistory> tableView = new TableView();

        TableColumn<TblRoomCheckItemMutationHistory, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getCodeItem(), param.getValue().tblItemMutationHistoryProperty()));
        codeItem.setMinWidth(90);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getItemName()
                        + getDataPropertyBarcode(param.getValue().getTblItemMutationHistory())
                        + getDataExpiredDate(param.getValue().getTblItemMutationHistory()),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemName.setMinWidth(130);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getTblItemMutationHistory().getItemQuantity()), param.getValue().tblItemMutationHistoryProperty()));
        itemQuantity.setMinWidth(90);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemUnit.setMinWidth(100);

        TableColumn<TblRoomCheckItemMutationHistory, String> sourceLocationName = new TableColumn("Lokasi (Sumber)");
        sourceLocationName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> getLocationName(param.getValue().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()), param.getValue().tblItemMutationHistoryProperty()));
        sourceLocationName.setMinWidth(140);

        TableColumn<TblRoomCheckItemMutationHistory, String> mutationAllStatus = new TableColumn("Status");
        mutationAllStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemMutationType().getTypeName()
                        + (param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy() != null
                                ? "\n(" + param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy().getObsoleteByName() + ")"
                                : ""),
                        param.getValue().tblItemMutationHistoryProperty()));
        mutationAllStatus.setMinWidth(150);

//        TableColumn<TblRoomCheckItemMutationHistory, String> mutationType = new TableColumn("Status");
//        mutationType.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemMutationType().getTypeName(), param.getValue().tblItemMutationHistoryProperty()));
//        mutationType.setMinWidth(100);
//
//        TableColumn<TblRoomCheckItemMutationHistory, String> obsoleteBy = new TableColumn("Rusak Oleh");
//        obsoleteBy.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy() != null
//                                ? param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy().getObsoleteByName()
//                                : "-", param.getValue().tblItemMutationHistoryProperty()));
//        obsoleteBy.setMinWidth(100);
        TableColumn<TblRoomCheckItemMutationHistory, String> editableData = new TableColumn("Deletable");
        editableData.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCreateDate() == null
                                ? "" : "X", param.getValue().createDateProperty()));
        editableData.setMinWidth(80);

        tableView.getColumns().addAll(codeItem, itemName, itemQuantity, itemUnit, sourceLocationName, mutationAllStatus);
        tableView.setItems(loadAllDataIn());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoomCheckItemMutationHistory> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null
                        && dataInputStatus != 3) {    //update
                    row.pseudoClassStateChanged(uneditablePseudoClass, (newVal.getCreateDate() != null));
                } else {
                    row.pseudoClassStateChanged(uneditablePseudoClass, false);
                }
            });
            return row;
        });

        tableDataIn = new ClassTableWithControl(tableView);
    }

    private String getDataPropertyBarcode(TblItemMutationHistory dataItemMutationHistory) {
        String result = "";
        if (dataItemMutationHistory != null) {
            for (TblItemMutationHistoryPropertyBarcode dataItemMutationHistoryPropertyBarcode : itemMutationHistoryPropertyBarcodes) {
                if (dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().equals(dataItemMutationHistory)) {
                    result += "\n(" + dataItemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getCodeBarcode() + ")";
                    break;
                }
            }
        }
        return result;
    }

    private String getDataExpiredDate(TblItemMutationHistory dataItemMutationHistory) {
        String result = "";
        if (dataItemMutationHistory != null) {
            for (TblItemMutationHistoryItemExpiredDate dataItemMutationHistoryItemExpiredDate : itemMutationHistoryItemExpiredDates) {
                if (dataItemMutationHistoryItemExpiredDate.getTblItemMutationHistory().equals(dataItemMutationHistory)) {
                    result += "\n(Exp. : " + ClassFormatter.dateFormate.format(dataItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate()) + ")";
                    break;
                }
            }
        }
        return result;
    }
    
    private String getLocationName(TblLocation tblLocation) {
        String locationName = "";
        switch (tblLocation.getRefLocationType().getIdtype()) {
            case 0:  //Kamar = '0'
                TblRoom room = parentController.getFRoomCheckManager().getDataRoomByIDLocation(tblLocation.getIdlocation());
                locationName = room != null ? room.getRoomName() : "";
                break;
            case 1:   //Gudang = '1'
                TblLocationOfWarehouse warehouse = parentController.getFRoomCheckManager().getDataWarehouseByIDLocation(tblLocation.getIdlocation());
                locationName = warehouse != null ? warehouse.getWarehouseName() : "";
                break;
            case 2: //Laundry = '2'
                TblLocationOfLaundry laundry = parentController.getFRoomCheckManager().getDataLaundryByIDLocation(tblLocation.getIdlocation());
                locationName = laundry != null ? laundry.getLaundryName() : "";
                break;
            case 3:    //Supplier = '3'
                TblSupplier supplier = parentController.getFRoomCheckManager().getDataSupplierByIDLocation(tblLocation.getIdlocation());
                locationName = supplier != null ? supplier.getSupplierName() : "";
                break;
            case 4: //Bin = '4'
                TblLocationOfBin bin = parentController.getFRoomCheckManager().getDataBinByIDLocation(tblLocation.getIdlocation());
                locationName = bin != null ? bin.getBinName() : "";
                break;
            default:
                break;
        }
        return locationName;
    }

    private void setTableControlDataIn() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataInCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataInUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataInDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataIn.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomCheckItemMutationHistory> loadAllDataIn() {
        ObservableList<TblRoomCheckItemMutationHistory> list = FXCollections.observableArrayList(parentController.getFRoomCheckManager().getAllDataRoomCheckItemMutationHistoryByIDRoomCheck(selectedData.getIdroomCheck()));
        for (TblRoomCheckItemMutationHistory data : list) {
            //set data room check
            data.setTblRoomCheck(parentController.getFRoomCheckManager().getDataRoomCheck(data.getTblRoomCheck().getIdroomCheck()));
            //set data item mutation
            data.setTblItemMutationHistory(parentController.getFRoomCheckManager().getDataItemMutationHistory(data.getTblItemMutationHistory().getIdmutation()));
            //set data item
            data.getTblItemMutationHistory().setTblItem(parentController.getFRoomCheckManager().getDataItem(data.getTblItemMutationHistory().getTblItem().getIditem()));
            //set data unit
            data.getTblItemMutationHistory().getTblItem().setTblUnit(parentController.getFRoomCheckManager().getDataUnit(data.getTblItemMutationHistory().getTblItem().getTblUnit().getIdunit()));
            //set data item type hk
            if(data.getTblItemMutationHistory().getTblItem().getTblItemTypeHk() != null){
                data.getTblItemMutationHistory().getTblItem().setTblItemTypeHk(parentController.getFRoomCheckManager().getDataItemTypeHK(data.getTblItemMutationHistory().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type wh
            if(data.getTblItemMutationHistory().getTblItem().getTblItemTypeWh() != null){
                data.getTblItemMutationHistory().getTblItem().setTblItemTypeWh(parentController.getFRoomCheckManager().getDataItemTypeWH(data.getTblItemMutationHistory().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
            //set data location
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(parentController.getFRoomCheckManager().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()));
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(parentController.getFRoomCheckManager().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
            //set data mutation type
            data.getTblItemMutationHistory().setRefItemMutationType(parentController.getFRoomCheckManager().getDataMutationType(data.getTblItemMutationHistory().getRefItemMutationType().getIdtype()));
            //set data obsolete by
            if (data.getTblItemMutationHistory().getRefItemMutationType().getIdtype() == 2) {   //Rusak = '2'
                data.getTblItemMutationHistory().setRefItemObsoleteBy(parentController.getFRoomCheckManager().getDataObsoleteBy(data.getTblItemMutationHistory().getRefItemObsoleteBy().getIdobsoleteBy()));
            }
        }
        //data - in
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()
                    == selectedData.getTblRoom().getTblLocation().getIdlocation()) {
                list.remove(i);
            }
        }
        //data - item location mutation history property barcode
        for (TblRoomCheckItemMutationHistory data : list) {
            if (data.getTblItemMutationHistory().getTblItem().getPropertyStatus()) {   //Property
                List<TblItemMutationHistoryPropertyBarcode> dataIMHPBs = parentController.getFRoomCheckManager().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for (TblItemMutationHistoryPropertyBarcode dataIMHPB : dataIMHPBs) {
                    dataIMHPB.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHPB.setTblPropertyBarcode(parentController.getFRoomCheckManager().getDataPropertyBarcode(dataIMHPB.getTblPropertyBarcode().getIdbarcode()));
                    itemMutationHistoryPropertyBarcodes.add(dataIMHPB);
                }
            }
        }
        //data - item location mutation history item expired date
        for(TblRoomCheckItemMutationHistory data : list){
            if(data.getTblItemMutationHistory().getTblItem().getConsumable()){
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = parentController.getFRoomCheckManager().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for(TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs){
                    dataIMHIED.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHIED.setTblItemExpiredDate(parentController.getFRoomCheckManager().getDataItemExpiredDate(dataIMHIED.getTblItemExpiredDate().getIditemExpiredDate()));
                    itemMutationHistoryItemExpiredDates.add(dataIMHIED);
                }
            }
        }
        return list;
    }

    public TblRoomCheckItemMutationHistory selectedDataIn;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputInStatus = 0;

    public Stage dialogStageIn;

    public void dataInCreateHandle() {
        dataInputInStatus = 0;
        selectedDataIn = new TblRoomCheckItemMutationHistory();
        selectedDataIn.setTblRoomCheck(selectedData);
        selectedDataIn.setTblItemMutationHistory(new TblItemMutationHistory());
        selectedDataIn.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation());
        selectedDataIn.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblRoom().getTblLocation());
        selectedDataIn.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
        selectedDataIn.getTblItemMutationHistory().setRefItemMutationType(parentController.getFRoomCheckManager().getDataMutationType(0));  //Dipindahkan = '0'
        //open form data - in
        showDataInDialog();
    }

    public void dataInUpdateHandle() {
        if (tableDataIn.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputInStatus = 1;
            selectedDataIn = (TblRoomCheckItemMutationHistory) tableDataIn.getTableView().getSelectionModel().getSelectedItem();
            //open form data - in
            showDataInDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataInDeleteHandle() {
        if (tableDataIn.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblRoomCheckItemMutationHistory) tableDataIn.getTableView().getSelectionModel().getSelectedItem()).getCreateDate() == null) {
                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassMessage.showSucceedRemovingDataMessage("", null);
                    //remove data item mutation history - proeprty barcode
                    for (int i = itemMutationHistoryPropertyBarcodes.size() - 1; i > -1; i--) {
                        if (itemMutationHistoryPropertyBarcodes.get(i).getTblItemMutationHistory().equals(tableDataIn.getTableView().getSelectionModel().getSelectedItem())) {
                            itemMutationHistoryPropertyBarcodes.remove(i);
                            break;
                        }
                    }
                    //remove data from table items list
                    tableDataIn.getTableView().getItems().remove(tableDataIn.getTableView().getSelectionModel().getSelectedItem());
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Data tidak dapat dihapus..!", "");
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataInDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/data_room_check/DataInDialog.fxml"));

            DataInController controller = new DataInController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageIn = new Stage();
            dialogStageIn.initModality(Modality.WINDOW_MODAL);
            dialogStageIn.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageIn, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageIn.initStyle(StageStyle.TRANSPARENT);
            dialogStageIn.setScene(scene);
            dialogStageIn.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageIn.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    /**
     * DATA (OUT)
     */
    @FXML
    private AnchorPane tableDataOutLayout;

    public ClassTableWithControl tableDataOut;

    private void initTableDataOut() {
        //set table
        setTableDataOut();
        //set control
        setTableControlDataOut();
        //set table-control to content-view
        tableDataOutLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataOut, 0.0);
        AnchorPane.setLeftAnchor(tableDataOut, 0.0);
        AnchorPane.setRightAnchor(tableDataOut, 0.0);
        AnchorPane.setTopAnchor(tableDataOut, 0.0);
        tableDataOutLayout.getChildren().add(tableDataOut);
    }

    private void setTableDataOut() {
        TableView<TblRoomCheckItemMutationHistory> tableView = new TableView();

        TableColumn<TblRoomCheckItemMutationHistory, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getCodeItem(), param.getValue().tblItemMutationHistoryProperty()));
        codeItem.setMinWidth(90);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getItemName()
                        + getDataPropertyBarcode(param.getValue().getTblItemMutationHistory())
                        + getDataExpiredDate(param.getValue().getTblItemMutationHistory()),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemName.setMinWidth(130);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getTblItemMutationHistory().getItemQuantity()), param.getValue().tblItemMutationHistoryProperty()));
        itemQuantity.setMinWidth(90);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemUnit.setMinWidth(100);

        TableColumn<TblRoomCheckItemMutationHistory, String> destinationLocationName = new TableColumn("Lokasi (Tujuan)");
        destinationLocationName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> getLocationName(param.getValue().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()), param.getValue().tblItemMutationHistoryProperty()));
        destinationLocationName.setMinWidth(140);

        TableColumn<TblRoomCheckItemMutationHistory, String> mutationAllStatus = new TableColumn("Status");
        mutationAllStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemMutationType().getTypeName()
                        + (param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy() != null
                                ? "\n(" + param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy().getObsoleteByName() + ")"
                                : ""),
                        param.getValue().tblItemMutationHistoryProperty()));
        mutationAllStatus.setMinWidth(150);

//        TableColumn<TblRoomCheckItemMutationHistory, String> mutationType = new TableColumn("Status");
//        mutationType.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemMutationType().getTypeName(), param.getValue().tblItemMutationHistoryProperty()));
//        mutationType.setMinWidth(140);
//
//        TableColumn<TblRoomCheckItemMutationHistory, String> obsoleteBy = new TableColumn("Rusak Oleh");
//        obsoleteBy.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy() != null
//                                ? param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy().getObsoleteByName()
//                                : "", param.getValue().tblItemMutationHistoryProperty()));
//        obsoleteBy.setMinWidth(140);
        TableColumn<TblRoomCheckItemMutationHistory, String> editableData = new TableColumn("Deletable");
        editableData.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCreateDate() == null
                                ? "" : "X", param.getValue().createDateProperty()));
        editableData.setMinWidth(80);

        tableView.getColumns().addAll(codeItem, itemName, itemQuantity, itemUnit, destinationLocationName, mutationAllStatus);
        tableView.setItems(loadAllDataOut());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoomCheckItemMutationHistory> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null
                        && dataInputStatus != 3) {    //update
                    row.pseudoClassStateChanged(uneditablePseudoClass, (newVal.getCreateDate() != null));
                } else {
                    row.pseudoClassStateChanged(uneditablePseudoClass, false);
                }
            });
            return row;
        });

        tableDataOut = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataOut() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataOutCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataOutUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataOutDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataOut.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomCheckItemMutationHistory> loadAllDataOut() {
        ObservableList<TblRoomCheckItemMutationHistory> list = FXCollections.observableArrayList(parentController.getFRoomCheckManager().getAllDataRoomCheckItemMutationHistoryByIDRoomCheck(selectedData.getIdroomCheck()));
        for (TblRoomCheckItemMutationHistory data : list) {
            //set data room check
            data.setTblRoomCheck(parentController.getFRoomCheckManager().getDataRoomCheck(data.getTblRoomCheck().getIdroomCheck()));
            //set data item mutation
            data.setTblItemMutationHistory(parentController.getFRoomCheckManager().getDataItemMutationHistory(data.getTblItemMutationHistory().getIdmutation()));
            //set data item
            data.getTblItemMutationHistory().setTblItem(parentController.getFRoomCheckManager().getDataItem(data.getTblItemMutationHistory().getTblItem().getIditem()));
            //set data unit
            data.getTblItemMutationHistory().getTblItem().setTblUnit(parentController.getFRoomCheckManager().getDataUnit(data.getTblItemMutationHistory().getTblItem().getTblUnit().getIdunit()));
            //set data location
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(parentController.getFRoomCheckManager().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()));
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(parentController.getFRoomCheckManager().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
            //set data mutation type
            data.getTblItemMutationHistory().setRefItemMutationType(parentController.getFRoomCheckManager().getDataMutationType(data.getTblItemMutationHistory().getRefItemMutationType().getIdtype()));
            //set data obsolete by
            if (data.getTblItemMutationHistory().getRefItemMutationType().getIdtype() == 2) {   //Rusak = '2'
                data.getTblItemMutationHistory().setRefItemObsoleteBy(parentController.getFRoomCheckManager().getDataObsoleteBy(data.getTblItemMutationHistory().getRefItemObsoleteBy().getIdobsoleteBy()));
            }
        }
        //data - out
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()
                    == selectedData.getTblRoom().getTblLocation().getIdlocation()) {
                list.remove(i);
            }
        }
        //data - item location mutation history property barcode
        for (TblRoomCheckItemMutationHistory data : list) {
            if (data.getTblItemMutationHistory().getTblItem().getPropertyStatus()) {   //Property
                List<TblItemMutationHistoryPropertyBarcode> dataIMHPBs = parentController.getFRoomCheckManager().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for (TblItemMutationHistoryPropertyBarcode dataIMHPB : dataIMHPBs) {
                    dataIMHPB.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHPB.setTblPropertyBarcode(parentController.getFRoomCheckManager().getDataPropertyBarcode(dataIMHPB.getTblPropertyBarcode().getIdbarcode()));
                    itemMutationHistoryPropertyBarcodes.add(dataIMHPB);
                }
            }
        }
        //data - item location mutation history item expired date
        for (TblRoomCheckItemMutationHistory data : list) {
            if (data.getTblItemMutationHistory().getTblItem().getConsumable()) {
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = parentController.getFRoomCheckManager().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                    dataIMHIED.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHIED.setTblItemExpiredDate(parentController.getFRoomCheckManager().getDataItemExpiredDate(dataIMHIED.getTblItemExpiredDate().getIditemExpiredDate()));
                    itemMutationHistoryItemExpiredDates.add(dataIMHIED);
                }
            }
        }
        return list;
    }

    public TblRoomCheckItemMutationHistory selectedDataOut;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputOutStatus = 0;

    public Stage dialogStageOut;

    public void dataOutCreateHandle() {
        dataInputOutStatus = 0;
        selectedDataOut = new TblRoomCheckItemMutationHistory();
        selectedDataOut.setTblRoomCheck(selectedData);
        selectedDataOut.setTblItemMutationHistory(new TblItemMutationHistory());
        selectedDataOut.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblRoom().getTblLocation());
        selectedDataOut.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
        //open form data - out
        showDataOutDialog();
    }

    public void dataOutUpdateHandle() {
        if (tableDataOut.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputOutStatus = 1;
            selectedDataOut = (TblRoomCheckItemMutationHistory) tableDataOut.getTableView().getSelectionModel().getSelectedItem();
            //open form data - out
            showDataOutDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataOutDeleteHandle() {
        if (tableDataOut.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblRoomCheckItemMutationHistory) tableDataOut.getTableView().getSelectionModel().getSelectedItem()).getCreateDate() == null) {
                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassMessage.showSucceedRemovingDataMessage("", null);
                    //remove data item mutation history - proeprty barcode
                    for (int i = itemMutationHistoryPropertyBarcodes.size() - 1; i > -1; i--) {
                        if (itemMutationHistoryPropertyBarcodes.get(i).getTblItemMutationHistory().equals(tableDataOut.getTableView().getSelectionModel().getSelectedItem())) {
                            itemMutationHistoryPropertyBarcodes.remove(i);
                            break;
                        }
                    }
                    //remove data item mutation history - item expired date
                    for (int i = itemMutationHistoryItemExpiredDates.size() - 1; i > -1; i--) {
                        if (itemMutationHistoryItemExpiredDates.get(i).getTblItemMutationHistory().equals(((TblRoomCheckItemMutationHistory) tableDataOut.getTableView().getSelectionModel().getSelectedItem()).getTblItemMutationHistory())) {
                            itemMutationHistoryItemExpiredDates.remove(i);
                        }
                    }
                    //remove data from table items list
                    tableDataOut.getTableView().getItems().remove(tableDataOut.getTableView().getSelectionModel().getSelectedItem());
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Data tidak dapat dihapus..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataOutDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/data_room_check/DataOutDialog.fxml"));

            DataOutController controller = new DataOutController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageOut = new Stage();
            dialogStageOut.initModality(Modality.WINDOW_MODAL);
            dialogStageOut.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageOut, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageOut.initStyle(StageStyle.TRANSPARENT);
            dialogStageOut.setScene(scene);
            dialogStageOut.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageOut.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
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
        setDataRoomCheckSplitpane();

        //init table
        initTableDataRoomCheck();

        //init form
        initFormDataRoomCheck();

        spDataRoomCheck.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRoomCheckFormShowStatus.set(0.0);
        });
    }

    public DataRoomCheckController(RoomCheckController parentController) {
        this.parentController = parentController;
    }

    private final RoomCheckController parentController;

    public FRoomCheckManager getService() {
        return parentController.getFRoomCheckManager();
    }

}
