/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse.warehouse_store_request;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblStoreRequest;
import hotelfx.persistence.model.TblStoreRequestDetail;
import hotelfx.persistence.service.FWarehouseManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_warehouse.FeatureWarehouseController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
public class WarehouseStoreRequestController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataSR;

    private DoubleProperty dataSRFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataSRLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataSRSplitpane() {
        spDataSR.setDividerPositions(1);

        dataSRFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataSRFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataSR.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataSR.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataSRFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataSRLayout.setDisable(false);
                    tableDataSRLayoutDisableLayer.setDisable(true);
                    tableDataSRLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataSRLayout.setDisable(true);
                    tableDataSRLayoutDisableLayer.setDisable(false);
                    tableDataSRLayoutDisableLayer.toFront();
                }
            }
        });

        dataSRFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataSRLayout;

    private ClassFilteringTable<TblStoreRequest> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataSR;

    private void initTableDataSR() {
        //set table
        setTableDataSR();
        //set control
        setTableControlDataSR();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataSR, 15.0);
        AnchorPane.setLeftAnchor(tableDataSR, 15.0);
        AnchorPane.setRightAnchor(tableDataSR, 15.0);
        AnchorPane.setTopAnchor(tableDataSR, 15.0);
        ancBodyLayout.getChildren().add(tableDataSR);
    }

    private void setTableDataSR() {
        TableView<TblStoreRequest> tableView = new TableView();

        TableColumn<TblStoreRequest, String> codeSR = new TableColumn("No. SR");
        codeSR.setCellValueFactory(cellData -> cellData.getValue().codeSrProperty());
        codeSR.setMinWidth(120);

        TableColumn<TblStoreRequest, String> createdDateBy = new TableColumn<>("Buat");
        createdDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCreatedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCreatedBy() != null)
                                ? param.getValue().getTblEmployeeByCreatedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().createdDateProperty()));
        createdDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> approvedDateBy = new TableColumn<>("Disetuji");
        approvedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getApprovedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByApprovedBy() != null)
                                ? param.getValue().getTblEmployeeByApprovedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().approvedDateProperty()));
        approvedDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> receivedDateBy = new TableColumn<>("Diterima");
        receivedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getReceivedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getReceivedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByReceivedBy() != null)
                                ? param.getValue().getTblEmployeeByReceivedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().receivedDateProperty()));
        receivedDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> canceledDateBy = new TableColumn<>("Pembatalan");
        canceledDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCanceledDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCanceledDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCanceledBy() != null)
                                ? param.getValue().getTblEmployeeByCanceledBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().canceledDateProperty()));
        canceledDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> dateByTitle = new TableColumn("Tanggal-Oleh");
        dateByTitle.getColumns().addAll(createdDateBy, approvedDateBy, receivedDateBy, canceledDateBy);

        TableColumn<TblStoreRequest, String> srStatus = new TableColumn<>("Status");
        srStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefStoreRequestStatus().getStatusName(),
                        param.getValue().refStoreRequestStatusProperty()));
        srStatus.setMinWidth(140);

        tableView.getColumns().addAll(codeSR, dateByTitle, srStatus);

        tableView.setItems(loadAllDataSR());

        tableView.setRowFactory(tv -> {
            TableRow<TblStoreRequest> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataSRUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                if (checkDataSREnableToUpdate((TblStoreRequest) row.getItem())) {
//                                    dataSRUpdateHandleDetail();
                                    dataSRShowHandle();
                                } else {
                                    dataSRShowHandle();
                                }
                            } else {
                                dataSRShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                if (checkDataSREnableToUpdate((TblStoreRequest) row.getItem())) {
//                                    dataSRUpdateHandleDetail();
//                                } else {
//                                    dataSRShowHandle();
//                                }
//                            } else {
//                                dataSRShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataSR = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblStoreRequest.class,
                tableDataSR.getTableView(),
                tableDataSR.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataSR() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataSRCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Batal");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataSRDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataSR.addButtonControl(buttonControls);
    }

    private ObservableList<TblStoreRequest> loadAllDataSR() {
        List<TblStoreRequest> list = parentController.getFWarehouseManager().getAllDataStoreRequest();
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            //data location (source)
            list.get(i).setTblLocationByIdlocationSource(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocationByIdlocationSource().getIdlocation()));
            //data location (destination)
            list.get(i).setTblLocationByIdlocationDestination(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocationByIdlocationDestination().getIdlocation()));
            //data sr - status
            list.get(i).setRefStoreRequestStatus(parentController.getFWarehouseManager().getDataStoreRequestStatus(list.get(i).getRefStoreRequestStatus().getIdstatus()));
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (list.get(i).getTblLocationByIdlocationDestination().getTblGroup() == null
                        || list.get(i).getTblLocationByIdlocationDestination().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
            }
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataSR;

    @FXML
    private ScrollPane spFormDataSR;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeSR;

    @FXML
    private AnchorPane ancReceivedByLayout;
    private JFXCComboBoxTablePopup<TblEmployee> cbpEmployee;

    @FXML
    private AnchorPane ancLocationSourceLayout;
    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpLocationSource;

    @FXML
    private AnchorPane ancLocationDestinationLayout;
    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpLocationDestination;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblStoreRequest selectedData;

    private void initFormDataSR() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataSR.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataSR.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Store Request)"));
        btnSave.setOnAction((e) -> {
            dataSRSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataSRCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpEmployee,
                cbpLocationSource,
                cbpLocationDestination);
    }

    private void initDataPopup() {
        TableView<TblEmployee> tableReceivedEmployee = new TableView();

        TableColumn<TblEmployee, String> codeReceivedEmployee = new TableColumn<>("ID");
        codeReceivedEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        codeReceivedEmployee.setMinWidth(120);

        TableColumn<TblEmployee, String> receivedEmployeeName = new TableColumn<>("Karyawan");
        receivedEmployeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(),
                        param.getValue().tblPeopleProperty()));
        receivedEmployeeName.setMinWidth(140);

        tableReceivedEmployee.getColumns().addAll(codeReceivedEmployee, receivedEmployeeName);

        TableView<TblLocationOfWarehouse> tableSourceLocation = new TableView();

        TableColumn<TblLocationOfWarehouse, String> sourceLocationName = new TableColumn<>("Gudang (Sumber) *");
        sourceLocationName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        sourceLocationName.setMinWidth(140);

        tableSourceLocation.getColumns().addAll(sourceLocationName);

        TableView<TblLocationOfWarehouse> tableDestinationLocation = new TableView();

        TableColumn<TblLocationOfWarehouse, String> destinationLocationName = new TableColumn<>("Gudang (Tujuan) *");
        destinationLocationName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        destinationLocationName.setMinWidth(140);

        tableDestinationLocation.getColumns().addAll(destinationLocationName);

        ObservableList<TblEmployee> receivedEmployeeItems = FXCollections.observableArrayList(loadAllDataReceivedBy());
        ObservableList<TblLocationOfWarehouse> sourceLocationItems = FXCollections.observableArrayList(loadAllDataSourceLocation());
        ObservableList<TblLocationOfWarehouse> destinationLocationItems = FXCollections.observableArrayList(loadAllDataDestinationLocation());

        cbpEmployee = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableReceivedEmployee, receivedEmployeeItems, "", "Penerima *", true, 270, 200
        );
        cbpLocationSource = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableSourceLocation, sourceLocationItems, "", "Gudang (Sumber) *", true, 200, 200
        );
        cbpLocationDestination = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableDestinationLocation, destinationLocationItems, "", "Gudang (Tujuan) *", true, 200, 200
        );

        ancReceivedByLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpEmployee, 0.0);
        AnchorPane.setBottomAnchor(cbpEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpEmployee, 0.0);
        ancReceivedByLayout.getChildren().add(cbpEmployee);

        ancLocationSourceLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpLocationSource, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationSource, 0.0);
        AnchorPane.setRightAnchor(cbpLocationSource, 0.0);
        AnchorPane.setBottomAnchor(cbpLocationSource, 0.0);
        ancLocationSourceLayout.getChildren().add(cbpLocationSource);

        ancLocationDestinationLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpLocationDestination, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationDestination, 0.0);
        AnchorPane.setRightAnchor(cbpLocationDestination, 0.0);
        AnchorPane.setBottomAnchor(cbpLocationDestination, 0.0);
        ancLocationDestinationLayout.getChildren().add(cbpLocationDestination);
    }

    private List<TblEmployee> loadAllDataReceivedBy() {
        List<TblEmployee> list = parentController.getFWarehouseManager().getAllDataEmployee();
        for (int i = list.size() - 1; i > -1; i--) {
            //data people
            list.get(i).setTblPeople(parentController.getFWarehouseManager().getDataPeople(list.get(i).getTblPeople().getIdpeople()));
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (list.get(i).getTblGroup() == null
                        || list.get(i).getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private List<TblLocationOfWarehouse> loadAllDataSourceLocation() {
        List<TblLocationOfWarehouse> list = parentController.getFWarehouseManager().getAllDataWarehouse();
        for (int i = list.size() - 1; i > -1; i--) {
            //data location
            list.get(i).setTblLocation(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocation().getIdlocation()));
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (list.get(i).getTblLocation().getTblGroup() == null
                        || list.get(i).getTblLocation().getTblGroup().getIdgroup() == ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private List<TblLocationOfWarehouse> loadAllDataDestinationLocation() {
        List<TblLocationOfWarehouse> list = parentController.getFWarehouseManager().getAllDataWarehouse();
        for (int i = list.size() - 1; i > -1; i--) {
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                //data location
                list.get(i).setTblLocation(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocation().getIdlocation()));
                if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                    if (list.get(i).getTblLocation().getTblGroup() == null
                            || list.get(i).getTblLocation().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                        list.remove(i);
                    }
                }
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Received By
        ObservableList<TblEmployee> receivedEmployeeItems = FXCollections.observableArrayList(loadAllDataReceivedBy());
        cbpEmployee.setItems(receivedEmployeeItems);

        //Source Location
        ObservableList<TblLocationOfWarehouse> sourceLocationItems = FXCollections.observableArrayList(loadAllDataSourceLocation());
        cbpLocationSource.setItems(sourceLocationItems);

        //Destination Location
        ObservableList<TblLocationOfWarehouse> destinationLocationItems = FXCollections.observableArrayList(loadAllDataDestinationLocation());
        cbpLocationDestination.setItems(destinationLocationItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeSr() != null
                ? selectedData.getCodeSr() : "");

        txtCodeSR.textProperty().bindBidirectional(selectedData.codeSrProperty());

        cbpEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByReceivedByProperty());

        cbpLocationSource.setValue(getDataLocationWarehouse(selectedData.getTblLocationByIdlocationSource()));
        cbpLocationDestination.setValue(getDataLocationWarehouse(selectedData.getTblLocationByIdlocationDestination()));

        cbpEmployee.hide();
        cbpLocationSource.hide();
        cbpLocationDestination.hide();

        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private TblLocationOfWarehouse getDataLocationWarehouse(TblLocation tblLocation) {
        if (tblLocation != null) {
            return parentController.getFWarehouseManager().getDataWarehouseByIdLocation(tblLocation.getIdlocation());
        }
        return null;
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeSR.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataSR,
                dataInputStatus == 3,
                txtCodeSR);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    public void dataSRCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblStoreRequest();
        setSelectedDataToInputForm();
        //open form data store request
        dataSRFormShowStatus.set(0.0);
        dataSRFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataSRShowHandle() {
        if (tableDataSR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFWarehouseManager().getDataStoreRequest(((TblStoreRequest) tableDataSR.getTableView().getSelectionModel().getSelectedItem()).getIdsr());
            selectedData.setRefStoreRequestStatus(parentController.getFWarehouseManager().getDataStoreRequestStatus(selectedData.getRefStoreRequestStatus().getIdstatus()));
            setSelectedDataToInputForm();
            dataSRFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataSRUnshowHandle() {
        refreshDataTableSR();
        dataSRFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private boolean checkDataSREnableToUpdate(TblStoreRequest dataSR) {
        return (dataSR.getRefStoreRequestStatus().getIdstatus() == 0 //0 = 'Created'
                || dataSR.getRefStoreRequestStatus().getIdstatus() == 1); //1 = 'Approved'
    }

    private void dataSRDeleteHandle() {
        if (tableDataSR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataSREnableToDelete((TblStoreRequest) tableDataSR.getTableView().getSelectionModel().getSelectedItem())) {
                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membatalkan data ini?", null);
                if (alert.getResult() == ButtonType.OK) {
                    if (parentController.getFWarehouseManager().deleteDataStoreRequest((TblStoreRequest) tableDataSR.getTableView().getSelectionModel().getSelectedItem())) {
                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dibatalkan..!!", null);
                        //refresh data from table & close form data store request
                        refreshDataTableSR();
                        dataSRFormShowStatus.set(0.0);
                    } else {
                        HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibatalkan..!!", parentController.getFWarehouseManager().getErrorMessage());
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat dibatalkan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataSREnableToDelete(TblStoreRequest dataSR) {
        return (dataSR.getRefStoreRequestStatus().getIdstatus() == 0);   //0 = 'Created'
    }

    private void dataSRPrintHandle() {
        if (tableDataSR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printSR(((TblStoreRequest) tableDataSR.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printSR(TblStoreRequest dataSR) {
        String hotelName = "";
        SysDataHardCode sdhHotelName = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 12);  //HotelName = '12'
        if (sdhHotelName != null
                && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }
        String hotelAddress = "";
        SysDataHardCode sdhHotelAddress = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
        if (sdhHotelAddress != null
                && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }
        String hotelPhoneNumber = "";
        SysDataHardCode sdhHotelPhoneNumber = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
        if (sdhHotelPhoneNumber != null
                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
        }
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        try {
//            ClassPrinter.printSR(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    dataSR);
//        } catch (Throwable ex) {
//            Logger.getLogger(WarehouseStoreRequestController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void dataSRSaveHandle() {
        if (checkDataInputDataSR()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                selectedData.setTblLocationByIdlocationSource(cbpLocationSource.getValue().getTblLocation());
                selectedData.setTblLocationByIdlocationDestination(cbpLocationDestination.getValue().getTblLocation());
                //dummy entry
                TblStoreRequest dummySelectedData = new TblStoreRequest(selectedData);
                dummySelectedData.setTblEmployeeByReceivedBy(new TblEmployee(dummySelectedData.getTblEmployeeByReceivedBy()));
                dummySelectedData.setTblLocationByIdlocationSource(new TblLocation(dummySelectedData.getTblLocationByIdlocationSource()));
                dummySelectedData.setTblLocationByIdlocationDestination(new TblLocation(dummySelectedData.getTblLocationByIdlocationDestination()));
                List<TblStoreRequestDetail> dummyDataStoreRequestDetails = new ArrayList<>();
                for (TblStoreRequestDetail dataStoreRequestDetail : (List<TblStoreRequestDetail>) tableDataDetail.getTableView().getItems()) {
                    TblStoreRequestDetail dummyDataStoreRequestDetail = new TblStoreRequestDetail(dataStoreRequestDetail);
                    dummyDataStoreRequestDetail.setTblStoreRequest(dummySelectedData);
                    dummyDataStoreRequestDetail.setTblItem(new TblItem(dummyDataStoreRequestDetail.getTblItem()));
                    dummyDataStoreRequestDetails.add(dummyDataStoreRequestDetail);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFWarehouseManager().insertDataStoreRequest(dummySelectedData,
                                dummyDataStoreRequestDetails) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data store request
                            refreshDataTableSR();
                            dataSRFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                            //show data sr, print-handle
                            printSR(selectedData);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFWarehouseManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFWarehouseManager().updateDataStoreRequest(dummySelectedData,
                                dummyDataStoreRequestDetails)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data store request
                            refreshDataTableSR();
                            dataSRFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                            //show data sr, print-handle
                            printSR(selectedData);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFWarehouseManager().getErrorMessage(), null);
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

    private void dataSRCancelHandle() {
        //refresh data from table & close form data store request
        refreshDataTableSR();
        dataSRFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableSR() {
        tableDataSR.getTableView().setItems(loadAllDataSR());
        cft.refreshFilterItems(tableDataSR.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataSR() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpEmployee.getValue() == null) {
            dataInput = false;
            errDataInput += "Penerima : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpLocationSource.getValue() == null) {
            dataInput = false;
            errDataInput += "Gudang (Sumber) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpLocationDestination.getValue() == null) {
            dataInput = false;
            errDataInput += "Gudang (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (tableDataDetail.getTableView().getItems().isEmpty()) {
            dataInput = false;
            errDataInput += "Tabel (Detail-Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDetailLayout;

    public ClassTableWithControl tableDataDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set control
        setTableControlDataDetail();
        //set table-control to content-view
        ancDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail() {
        TableView<TblStoreRequestDetail> tableView = new TableView();

        TableColumn<TblStoreRequestDetail, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
        codeItem.setMinWidth(120);

        TableColumn<TblStoreRequestDetail, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblStoreRequestDetail, String> itemTypeHK = new TableColumn("House Keeping");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblItem().getTblItemTypeHk() != null 
                        ? param.getValue().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + (param.getValue().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().tblItemProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<TblStoreRequestDetail, String> itemTypeWH = new TableColumn("Warehouse");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblItem().getTblItemTypeWh() != null 
                        ? param.getValue().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (param.getValue().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().tblItemProperty()));
        itemTypeWH.setMinWidth(150);
        
        TableColumn<TblStoreRequestDetail,String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);
        
        TableColumn<TblStoreRequestDetail, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblStoreRequestDetail, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(), param.getValue().tblItemProperty()));
        unitName.setMinWidth(120);

        tableView.getColumns().addAll(titledItemType, codeItem, itemName, itemQuantity, unitName);
        tableView.setItems(loadAllDataSRDetail());
        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataDetailUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataDetail.addButtonControl(buttonControls);
    }

    private ObservableList<TblStoreRequestDetail> loadAllDataSRDetail() {
        ObservableList<TblStoreRequestDetail> list = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataStoreRequestDetailByIDStoreRequest(selectedData.getIdsr()));
        for (TblStoreRequestDetail data : list) {
            //set data item
            data.setTblItem(parentController.getFWarehouseManager().getDataItem(data.getTblItem().getIditem()));
            //set data unit
            data.getTblItem().setTblUnit(parentController.getFWarehouseManager().getDataUnit(data.getTblItem().getTblUnit().getIdunit()));
            //set data item type hk
            if (data.getTblItem().getTblItemTypeHk() != null) {
                data.getTblItem().setTblItemTypeHk(parentController.getFWarehouseManager().getDataItemTypeHK(data.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type wh
            if (data.getTblItem().getTblItemTypeWh() != null) {
                data.getTblItem().setTblItemTypeWh(parentController.getFWarehouseManager().getDataItemTypeWH(data.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        return list;
    }

    public TblStoreRequestDetail selectedDataDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailCreateHandle() {
        dataInputDetailStatus = 0;
        selectedDataDetail = new TblStoreRequestDetail();
        selectedDataDetail.setTblStoreRequest(selectedData);
        selectedDataDetail.setItemQuantity(new BigDecimal("0"));
        //open form data - detail
        showDataDetailDialog();
    }

    public void dataDetailUpdateHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputDetailStatus = 1;
            selectedDataDetail = new TblStoreRequestDetail();
            selectedDataDetail.setTblStoreRequest(selectedData);
            selectedDataDetail.setItemQuantity(((TblStoreRequestDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getItemQuantity());
            selectedDataDetail.setTblItem(parentController.getFWarehouseManager().getDataItem(((TblStoreRequestDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblItem().getIditem()));
            selectedDataDetail.getTblItem().setTblUnit(parentController.getFWarehouseManager().getDataUnit(selectedDataDetail.getTblItem().getTblUnit().getIdunit()));
            if(selectedDataDetail.getTblItem().getTblItemTypeHk() != null){
                selectedDataDetail.getTblItem().setTblItemTypeHk(parentController.getFWarehouseManager().getDataItemTypeHK(selectedDataDetail.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            if(selectedDataDetail.getTblItem().getTblItemTypeWh() != null){
                selectedDataDetail.getTblItem().setTblItemTypeWh(parentController.getFWarehouseManager().getDataItemTypeWH(selectedDataDetail.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
            //open form data - detail
            showDataDetailDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataDetailDeleteHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataDetail.getTableView().getItems().remove(tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_warehouse/warehouse_store_request/WarehouseStoreRequestDetailDialog.fxml"));

            WarehouseStoreRequestDetailController controller = new WarehouseStoreRequestDetailController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageDetal = new Stage();
            dialogStageDetal.initModality(Modality.WINDOW_MODAL);
            dialogStageDetal.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageDetal, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageDetal.initStyle(StageStyle.TRANSPARENT);
            dialogStageDetal.setScene(scene);
            dialogStageDetal.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageDetal.showAndWait();
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
        setDataSRSplitpane();

        //init table
        initTableDataSR();

        //init form
        initFormDataSR();

        spDataSR.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataSRFormShowStatus.set(0.0);
        });
    }

    public WarehouseStoreRequestController(FeatureWarehouseController parentController) {
        this.parentController = parentController;
    }

    private final FeatureWarehouseController parentController;

    public FWarehouseManager getService() {
        return parentController.getFWarehouseManager();
    }

}
