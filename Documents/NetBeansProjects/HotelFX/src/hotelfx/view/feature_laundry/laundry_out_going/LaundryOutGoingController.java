/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_laundry.laundry_out_going;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
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
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblOutGoing;
import hotelfx.persistence.model.TblOutGoingDetail;
import hotelfx.persistence.model.TblOutGoingDetailItemMutationHistory;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStoreRequest;
import hotelfx.persistence.model.TblStoreRequestDetail;
import hotelfx.persistence.service.FLaundryManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_laundry.FeatureLaundryController;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class LaundryOutGoingController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataOG;

    private DoubleProperty dataOGFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataOGLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataOGSplitpane() {
        spDataOG.setDividerPositions(1);

        dataOGFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataOGFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataOG.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataOG.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataOGFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataOGLayout.setDisable(false);
                    tableDataOGLayoutDisableLayer.setDisable(true);
                    tableDataOGLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataOGLayout.setDisable(true);
                    tableDataOGLayoutDisableLayer.setDisable(false);
                    tableDataOGLayoutDisableLayer.toFront();
                }
            }
        });

        dataOGFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataOGLayout;

    private ClassFilteringTable<TblOutGoing> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataOG;

    private void initTableDataOG() {
        //set table
        setTableDataOG();
        //set control
        setTableControlDataOG();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataOG, 15.0);
        AnchorPane.setLeftAnchor(tableDataOG, 15.0);
        AnchorPane.setRightAnchor(tableDataOG, 15.0);
        AnchorPane.setTopAnchor(tableDataOG, 15.0);
        ancBodyLayout.getChildren().add(tableDataOG);
    }

    private void setTableDataOG() {
        TableView<TblOutGoing> tableView = new TableView();

        TableColumn<TblOutGoing, String> ogDate = new TableColumn<>("Tgl. Outgoing");
        ogDate.setCellValueFactory((TableColumn.CellDataFeatures<TblOutGoing, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getOgdate()),
                        param.getValue().ogdateProperty()));
        ogDate.setMinWidth(140);

        TableColumn<TblOutGoing, String> codeOG = new TableColumn("No. OG");
        codeOG.setCellValueFactory(cellData -> cellData.getValue().codeOgProperty());
        codeOG.setMinWidth(120);

        TableColumn<TblOutGoing, String> codeSR = new TableColumn<>("No. SR");
        codeSR.setCellValueFactory((TableColumn.CellDataFeatures<TblOutGoing, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblStoreRequest() != null
                                ? param.getValue().getTblStoreRequest().getCodeSr() : "-",
                        param.getValue().tblStoreRequestProperty()));
        codeSR.setMinWidth(120);

        TableColumn<TblOutGoing, String> createdBy = new TableColumn<>("Buat");
        createdBy.setCellValueFactory((TableColumn.CellDataFeatures<TblOutGoing, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblEmployeeByCreateBy() != null)
                                ? param.getValue().getTblEmployeeByCreateBy().getTblPeople().getFullName()
                                : "-",
                        param.getValue().tblEmployeeByCreateByProperty()));
        createdBy.setMinWidth(140);

        TableColumn<TblOutGoing, String> receivedBy = new TableColumn<>("Diterima");
        receivedBy.setCellValueFactory((TableColumn.CellDataFeatures<TblOutGoing, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblEmployeeByIdreceiver() != null)
                                ? param.getValue().getTblEmployeeByIdreceiver().getTblPeople().getFullName()
                                : "-",
                        param.getValue().tblEmployeeByIdreceiverProperty()));
        receivedBy.setMinWidth(140);

        TableColumn<TblOutGoing, String> byTitle = new TableColumn("Karyawan");
        byTitle.getColumns().addAll(createdBy, receivedBy);

        TableColumn<TblOutGoing, String> sourceLocation = new TableColumn<>("Laundry");
        sourceLocation.setCellValueFactory((TableColumn.CellDataFeatures<TblOutGoing, String> param)
                -> Bindings.createStringBinding(() -> getSourceLocationName(param.getValue()),
                        param.getValue().tblLocationByIdlocationSourceProperty()));
        sourceLocation.setMinWidth(160);

        TableColumn<TblOutGoing, String> destinationLocation = new TableColumn<>("Ke");
        destinationLocation.setCellValueFactory((TableColumn.CellDataFeatures<TblOutGoing, String> param)
                -> Bindings.createStringBinding(() -> getDestinationLocationName(param.getValue()),
                        param.getValue().tblLocationByIdlocationDestinationProperty()));
        destinationLocation.setMinWidth(160);

        TableColumn<TblOutGoing, String> locationTitle = new TableColumn("Laundry");
        locationTitle.getColumns().addAll(sourceLocation, destinationLocation);

        tableView.getColumns().addAll(ogDate, codeOG, codeSR, byTitle, sourceLocation);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataOG()));

        tableView.setRowFactory(tv -> {
            TableRow<TblOutGoing> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataOGUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataOGShowHandle();
                        }
                    }
                }
            });
            return row;
        });

        tableDataOG = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblStoreRequest.class,
                tableDataOG.getTableView(),
                tableDataOG.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getSourceLocationName(TblOutGoing dataOG) {
        if (dataOG != null) {
            return getNameLocation(dataOG.getTblLocationByIdlocationSource());
        }
        return "-";
    }

    private String getDestinationLocationName(TblOutGoing dataOG) {
        if (dataOG != null) {
            return getNameLocation(dataOG.getTblLocationByIdlocationDestination());
        }
        return "-";
    }

    public String getNameLocation(TblLocation dataLocation) {
        if (dataLocation != null
                && dataLocation.getRefLocationType() != null) {
            switch (dataLocation.getRefLocationType().getIdtype()) {
                case 0: //Room
                    TblRoom room = parentController.getFLaundryManager().getDataRoomByIDLocation(dataLocation.getIdlocation());
                    return room != null ? room.getRoomName() : "-";
                case 1: //Warehouse
                    TblLocationOfWarehouse warehouse = parentController.getFLaundryManager().getDataWarehouseByIdLocation(dataLocation.getIdlocation());
                    return warehouse != null ? warehouse.getWarehouseName() : "-";
                case 2: //Laundry
                    TblLocationOfLaundry laundry = parentController.getFLaundryManager().getDataLaundryByIDLocation(dataLocation.getIdlocation());
                    return laundry != null ? laundry.getLaundryName() : "-";
            }
        }
        return "-";
    }

    private void setTableControlDataOG() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Outgoing");
            buttonControl.setOnMouseClicked((e) -> {
                //listener create
                dataOGCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataOG.addButtonControl(buttonControls);
    }

    private List<TblOutGoing> loadAllDataOG() {
        List<TblOutGoing> list = parentController.getFLaundryManager().getAllDataOutGoing();
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            //data location (source)
            list.get(i).setTblLocationByIdlocationSource(parentController.getFLaundryManager().getLocation(list.get(i).getTblLocationByIdlocationSource().getIdlocation()));
            //data location (destination)
            list.get(i).setTblLocationByIdlocationDestination(parentController.getFLaundryManager().getLocation(list.get(i).getTblLocationByIdlocationDestination().getIdlocation()));
            //data store request
            if (list.get(i).getTblStoreRequest() != null) {
                list.get(i).setTblStoreRequest(parentController.getFLaundryManager().getDataStoreRequest(list.get(i).getTblStoreRequest().getIdsr()));
            }
            //data employee (receiver)
            list.get(i).setTblEmployeeByIdreceiver(parentController.getFLaundryManager().getDataEmployee(list.get(i).getTblEmployeeByIdreceiver().getIdemployee()));
            //data employee (create)
            list.get(i).setTblEmployeeByCreateBy(parentController.getFLaundryManager().getDataEmployee(list.get(i).getTblEmployeeByCreateBy().getIdemployee()));
            if (list.get(i).getTblLocationByIdlocationSource().getRefLocationType().getIdtype() == 2) {    //Laundry = '2'
                if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                    if (list.get(i).getTblLocationByIdlocationSource().getTblGroup() == null
                            || list.get(i).getTblLocationByIdlocationSource().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                        list.remove(i);
                    }
                }
            } else {
                list.remove(i);
            }
        }
        return list;
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataOG;

    @FXML
    private ScrollPane spFormDataOG;

    @FXML
    private Label lblCodeData;

    @FXML
    private AnchorPane ancSRLayout;
    private JFXCComboBoxTablePopup<TblStoreRequest> cbpSR;

    @FXML
    private AnchorPane ancReceivedByLayout;
    private JFXCComboBoxTablePopup<TblEmployee> cbpReceiver;

    @FXML
    private AnchorPane ancSourceLocationLayout;
    private JFXCComboBoxTablePopup<ClassLocation> cbpSourceLocation;

    @FXML
    private AnchorPane ancDestinationLocationLayout;
    private JFXCComboBoxTablePopup<ClassLocation> cbpDestinationLocation;

    @FXML
    private JFXTextArea txtNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblOutGoing selectedData;

    private void initFormDataOG() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataOG.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataOG.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Outgoing (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataOGSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataOGCancelHandle();
        });

        initDataPopup();

        cbpDestinationLocation.setDisable(true);
        cbpDestinationLocation.setVisible(false);

        ancSRLayout.setVisible(false);
        ancDestinationLocationLayout.setVisible(false);

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpReceiver,
                cbpSourceLocation,
                cbpDestinationLocation,
                txtNote);
    }

    private void initDataPopup() {
        //Store Request
        TableView<TblStoreRequest> tableStoreRequest = new TableView<>();

        TableColumn<TblStoreRequest, String> codeSR = new TableColumn<>("No. SR");
        codeSR.setCellValueFactory(cellData -> cellData.getValue().codeSrProperty());
        codeSR.setMinWidth(120);

        TableColumn<TblStoreRequest, String> srDate = new TableColumn<>("Tgl. Store Request");
        srDate.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate()),
                        param.getValue().approvedDateProperty()));
        srDate.setMinWidth(120);

        tableStoreRequest.getColumns().addAll(codeSR);

        ObservableList<TblStoreRequest> storeRequestItems = FXCollections.observableArrayList(loadAllDataStoreRequest());

        cbpSR = new JFXCComboBoxTablePopup<>(
                TblStoreRequest.class, tableStoreRequest, storeRequestItems, "", "No. SR", true, 130, 200
        );

        //Employee (Received By)
        TableView<TblEmployee> tableReceivedBy = new TableView<>();

        TableColumn<TblEmployee, String> codeReceivedBy = new TableColumn<>("ID");
        codeReceivedBy.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        codeReceivedBy.setMinWidth(100);

        TableColumn<TblEmployee, String> receivedByName = new TableColumn<>("Nama");
        receivedByName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(),
                        param.getValue().tblPeopleProperty()));
        receivedByName.setMinWidth(140);

        tableReceivedBy.getColumns().addAll(codeReceivedBy, receivedByName);

        ObservableList<TblEmployee> receivedByItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpReceiver = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableReceivedBy, receivedByItems, "", "Penerima *", true, 250, 200
        );

        //Class Location (Source)
        TableView<ClassLocation> tableLocationSource = new TableView<>();

        TableColumn<ClassLocation, String> sourceLocationName = new TableColumn<>("Lokasi");
        sourceLocationName.setCellValueFactory((TableColumn.CellDataFeatures<ClassLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().toString(),
                        param.getValue().dataLocationProperty()));
        sourceLocationName.setMinWidth(140);

        tableLocationSource.getColumns().addAll(sourceLocationName);

        ObservableList<ClassLocation> sourceLocationItems = FXCollections.observableArrayList(loadAllDataSourceClassLocation());

        cbpSourceLocation = new JFXCComboBoxTablePopup<>(
                ClassLocation.class, tableLocationSource, sourceLocationItems, "", "Laundry (Sumber) *", true, 150, 200
        );

        //Class Location (Destination)
        TableView<ClassLocation> tableLocationDestination = new TableView<>();

        TableColumn<ClassLocation, String> destinationLocationName = new TableColumn<>("Lokasi");
        destinationLocationName.setCellValueFactory((TableColumn.CellDataFeatures<ClassLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().toString(),
                        param.getValue().dataLocationProperty()));
        destinationLocationName.setMinWidth(120);

        tableLocationDestination.getColumns().addAll(destinationLocationName);

        ObservableList<ClassLocation> destinationLocationItems = FXCollections.observableArrayList(loadAllDataDestinationClassLocation());

        cbpDestinationLocation = new JFXCComboBoxTablePopup<>(
                ClassLocation.class, tableLocationDestination, destinationLocationItems, "", "Laundry (Tujuan) *", true, 150, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpSR, 0.0);
        AnchorPane.setLeftAnchor(cbpSR, 0.0);
//        AnchorPane.setRightAnchor(cbpSR, 0.0);
        AnchorPane.setTopAnchor(cbpSR, 0.0);
        ancSRLayout.getChildren().add(cbpSR);

        AnchorPane.setBottomAnchor(cbpReceiver, 0.0);
        AnchorPane.setLeftAnchor(cbpReceiver, 0.0);
//        AnchorPane.setRightAnchor(cbpReceiver, 0.0);
        AnchorPane.setTopAnchor(cbpReceiver, 0.0);
        ancReceivedByLayout.getChildren().add(cbpReceiver);

        AnchorPane.setBottomAnchor(cbpSourceLocation, 0.0);
        AnchorPane.setLeftAnchor(cbpSourceLocation, 0.0);
//        AnchorPane.setRightAnchor(cbpSourceLocation, 0.0);
        AnchorPane.setTopAnchor(cbpSourceLocation, 0.0);
        ancSourceLocationLayout.getChildren().add(cbpSourceLocation);

        AnchorPane.setBottomAnchor(cbpDestinationLocation, 0.0);
        AnchorPane.setLeftAnchor(cbpDestinationLocation, 0.0);
//        AnchorPane.setRightAnchor(cbpDestinationLocation, 0.0);
        AnchorPane.setTopAnchor(cbpDestinationLocation, 0.0);
        ancDestinationLocationLayout.getChildren().add(cbpDestinationLocation);
    }

    private List<TblStoreRequest> loadAllDataStoreRequest() {
//        List<TblStoreRequest> list = parentController.getFLaundryManager().getAllDataStoreRequestByIDStoreRequestStatus((int)4);   //Dipesan = '4'
        List<TblStoreRequest> list = parentController.getFLaundryManager().getAllDataStoreRequestByIDStoreRequestStatus((int) 0);   //Dibuat = '0'
        for (TblStoreRequest data : list) {
            //data location (source)
            data.setTblLocationByIdlocationSource(parentController.getFLaundryManager().getLocation(data.getTblLocationByIdlocationSource().getIdlocation()));
            //data location type (source)
            data.getTblLocationByIdlocationSource().setRefLocationType(parentController.getFLaundryManager().getDataLocationType(data.getTblLocationByIdlocationSource().getRefLocationType().getIdtype()));
            //data location (destination)
            data.setTblLocationByIdlocationDestination(parentController.getFLaundryManager().getLocation(data.getTblLocationByIdlocationDestination().getIdlocation()));
            //data location type (destination)
            data.getTblLocationByIdlocationDestination().setRefLocationType(parentController.getFLaundryManager().getDataLocationType(data.getTblLocationByIdlocationDestination().getRefLocationType().getIdtype()));
        }
        //data null -> 0L = '-'
        TblStoreRequest sr = new TblStoreRequest();
        sr.setCodeSr("-");
        list.add(0, sr);
        return list;
    }

    private List<TblEmployee> loadAllDataEmployee() {
        List<TblEmployee> list = parentController.getFLaundryManager().getAllDataEmployee();
        for (TblEmployee data : list) {
            //data people
            data.setTblPeople(parentController.getFLaundryManager().getDataPeople(data.getTblPeople().getIdpeople()));
            //data job
            if (data.getTblJob() != null) {
                data.setTblJob(parentController.getFLaundryManager().getJob(data.getTblJob().getIdjob()));
            }
            //data group
            if (data.getTblGroup() != null) {
                data.setTblGroup(parentController.getFLaundryManager().getDataGroup(data.getTblGroup().getIdgroup()));
            }
        }
        return list;
    }

    private List<ClassLocation> loadAllDataSourceClassLocation() {
        List<ClassLocation> list = new ArrayList<>();
        List<TblLocation> locations = parentController.getFLaundryManager().getAllDataLocationByIDLocationType(2); //Laundry = '2'
        for (TblLocation location : locations) {
            //data group
            location.setTblGroup(parentController.getFLaundryManager().getDataGroup(location.getTblGroup().getIdgroup()));
            //data location type
            location.setRefLocationType(parentController.getFLaundryManager().getDataLocationType((int) location.getRefLocationType().getIdtype()));
            //data class location
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (location.getTblGroup() != null
                        && location.getTblGroup().getIdgroup() == ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.add(new ClassLocation(location));
                }
            } else {
                list.add(new ClassLocation(location));
            }
        }
        return list;
    }

    private List<ClassLocation> loadAllDataDestinationClassLocation() {
        List<ClassLocation> list = new ArrayList<>();
        List<TblLocation> locations = parentController.getFLaundryManager().getAllDataLocationByIDLocationType(2); //Laundry = '2'
        for (TblLocation location : locations) {
            //data group
            location.setTblGroup(parentController.getFLaundryManager().getDataGroup(location.getTblGroup().getIdgroup()));
            //data location type
            location.setRefLocationType(parentController.getFLaundryManager().getDataLocationType(location.getRefLocationType().getIdtype()));
            //data class location
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (location.getTblGroup() == null
                        || location.getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.add(new ClassLocation(location));
                }
            } else {
                list.add(new ClassLocation(location));
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Store Request
        ObservableList<TblStoreRequest> storeRequestItems = FXCollections.observableArrayList(loadAllDataStoreRequest());
        cbpSR.setItems(storeRequestItems);

        //Employee (Received By)
        ObservableList<TblEmployee> receivedByItems = FXCollections.observableArrayList(loadAllDataEmployee());
        cbpReceiver.setItems(receivedByItems);

        //Class Location (Source)
        ObservableList<ClassLocation> sourceLocationItems = FXCollections.observableArrayList(loadAllDataSourceClassLocation());
        cbpSourceLocation.setItems(sourceLocationItems);

        //Class Location (Destination)
        ObservableList<ClassLocation> destinationLocationItems = FXCollections.observableArrayList(loadAllDataDestinationClassLocation());
        cbpDestinationLocation.setItems(destinationLocationItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeOg() != null
                ? selectedData.getCodeOg() : "");

        txtNote.textProperty().bindBidirectional(selectedData.ognoteProperty());

        cbpSR.valueProperty().bindBidirectional(selectedData.tblStoreRequestProperty());
        cbpSR.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal != null
                        && newVal.getIdsr() != 0L) {
                    if (newVal.getTblEmployeeByReceivedBy() != null) {
                        cbpReceiver.setValue(newVal.getTblEmployeeByReceivedBy());
                    }
                    cbpSourceLocation.setValue(new ClassLocation(newVal.getTblLocationByIdlocationSource()));
                    cbpSourceLocation.setDisable(true);
                    cbpDestinationLocation.setValue(new ClassLocation(newVal.getTblLocationByIdlocationDestination()));
                    cbpDestinationLocation.setDisable(true);
                    cbpDestinationLocation.setVisible(true);
                } else {
                    cbpSourceLocation.setValue(null);
                    cbpSourceLocation.setDisable(false);
                    cbpDestinationLocation.setValue(new ClassLocation(getDataUsedLocation()));
                    cbpDestinationLocation.setDisable(true);
                    cbpDestinationLocation.setVisible(false);
                }
            }
            //refresh data table detail
            tableDataDetail.getTableView().setItems(loadAllDataOGDIMH());
        });

        cbpReceiver.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdreceiverProperty());

        if (selectedData.getTblLocationByIdlocationSource() != null) {
            cbpSourceLocation.setValue(new ClassLocation(selectedData.getTblLocationByIdlocationSource()));
        } else {
            cbpSourceLocation.setValue(null);
        }
        cbpSourceLocation.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setTblLocationByIdlocationSource(newVal.getDataLocation());
            } else {
                selectedData.setTblLocationByIdlocationSource(null);
            }
            //refresh data table detail
            tableDataDetail.getTableView().setItems(loadAllDataOGDIMH());
        });

        if (selectedData.getTblLocationByIdlocationDestination() != null) {
            cbpDestinationLocation.setValue(new ClassLocation(selectedData.getTblLocationByIdlocationDestination()));
        } else {
            cbpDestinationLocation.setValue(null);
        }
        cbpDestinationLocation.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setTblLocationByIdlocationDestination(newVal.getDataLocation());
            } else {
                selectedData.setTblLocationByIdlocationDestination(null);
            }
            //refresh data table detail
            tableDataDetail.getTableView().setItems(loadAllDataOGDIMH());
        });

        cbpSR.hide();
        cbpReceiver.hide();
        cbpSourceLocation.hide();
        cbpDestinationLocation.hide();

        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private String getDataLocationLaundry(TblLocation tblLocation) {
        if (tblLocation != null) {
            return parentController.getFLaundryManager().getDataLaundryByIdLocation(tblLocation.getIdlocation()).getLaundryName();
        }
        return "-";
    }

    private TblLocation getDataUsedLocation() {
        TblLocation usedLocation = parentController.getFLaundryManager().getLocationByIDLocationType(6);  //Digunakan = '6'
        if (usedLocation != null) {
            //location type
            usedLocation.setRefLocationType(parentController.getFLaundryManager().getDataLocationType(usedLocation.getRefLocationType().getIdtype()));
        }
        return usedLocation;
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataOG,
                dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
    }

    public class ClassLocation {

        private final ObjectProperty<TblLocation> dataLocation = new SimpleObjectProperty<>();

        public ClassLocation(TblLocation dataLocation) {
            this.dataLocation.set(dataLocation);
        }

        public ObjectProperty<TblLocation> dataLocationProperty() {
            return dataLocation;
        }

        public TblLocation getDataLocation() {
            return dataLocationProperty().get();
        }

        public void setDataLocation(TblLocation dataLocation) {
            dataLocationProperty().set(dataLocation);
        }

        @Override
        public String toString() {
            return getNameLocation(getDataLocation());
        }

    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    public void dataOGCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblOutGoing();
        selectedData.setTblLocationByIdlocationDestination(getDataUsedLocation());
        setSelectedDataToInputForm();
        //open form data out going
        dataOGFormShowStatus.set(0.0);
        dataOGFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataOGShowHandle() {
        if (tableDataOG.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLaundryManager().getDataOutGoing(((TblOutGoing) tableDataOG.getTableView().getSelectionModel().getSelectedItem()).getIdog());
            if (selectedData.getTblStoreRequest() != null) {
                selectedData.setTblStoreRequest(parentController.getFLaundryManager().getDataStoreRequest(selectedData.getTblStoreRequest().getIdsr()));
            }
            selectedData.setTblEmployeeByIdreceiver(parentController.getFLaundryManager().getDataEmployee(selectedData.getTblEmployeeByIdreceiver().getIdemployee()));
            selectedData.setTblLocationByIdlocationSource(parentController.getFLaundryManager().getLocation(selectedData.getTblLocationByIdlocationSource().getIdlocation()));
            selectedData.setTblLocationByIdlocationDestination(parentController.getFLaundryManager().getLocation(selectedData.getTblLocationByIdlocationDestination().getIdlocation()));
            setSelectedDataToInputForm();
            dataOGFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataOGUnshowHandle() {
        refreshDataTableOG();
        dataOGFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataOGPrintHandle() {
        if (tableDataOG.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printOG(((TblOutGoing) tableDataOG.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printOG(TblOutGoing dataOG) {
        String hotelName = "";
        SysDataHardCode sdhHotelName = parentController.getFLaundryManager().getDataSysDataHardCode((long) 12);  //HotelName = '12'
        if (sdhHotelName != null
                && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }
        String hotelAddress = "";
        SysDataHardCode sdhHotelAddress = parentController.getFLaundryManager().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
        if (sdhHotelAddress != null
                && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }
        String hotelPhoneNumber = "";
        SysDataHardCode sdhHotelPhoneNumber = parentController.getFLaundryManager().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
        if (sdhHotelPhoneNumber != null
                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
        }
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = parentController.getFLaundryManager().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        try {
//            ClassPrinter.printOG(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    dataSR);
//        } catch (Throwable ex) {
//            Logger.getLogger(LaundryStoreRequestController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void dataOGSaveHandle() {
        if (checkDataInputDataOG()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblOutGoing dummySelectedData = new TblOutGoing(selectedData);
                List<OGDetailItemMutationHistory> dummyDataOGDetailItemMutationHistories = new ArrayList<>();
                for (OGDetailItemMutationHistory ogdp : (List<OGDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
                    TblOutGoingDetailItemMutationHistory dummyOGIMH = new TblOutGoingDetailItemMutationHistory(ogdp.getDataOGDIMH());
                    dummyOGIMH.setTblOutGoingDetail(new TblOutGoingDetail(dummyOGIMH.getTblOutGoingDetail()));
                    dummyOGIMH.getTblOutGoingDetail().setTblOutGoing(dummySelectedData);
                    dummyOGIMH.getTblOutGoingDetail().setTblItem(new TblItem(dummyOGIMH.getTblOutGoingDetail().getTblItem()));
                    dummyOGIMH.setTblItemMutationHistory(new TblItemMutationHistory(dummyOGIMH.getTblItemMutationHistory()));
                    dummyOGIMH.getTblItemMutationHistory().setTblItem(new TblItem(dummyOGIMH.getTblItemMutationHistory().getTblItem()));
                    dummyOGIMH.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation(dummyOGIMH.getTblItemMutationHistory().getTblLocationByIdlocationOfSource()));
                    dummyOGIMH.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(new TblLocation(dummyOGIMH.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()));
                    List<IMHPropertyBarcodeSelected> dummyIMHPBSs = new ArrayList<>();
                    for (IMHPropertyBarcodeSelected imhpbs : ogdp.getDataIMHPBSs()) {
                        IMHPropertyBarcodeSelected dummyIMHPBS = new IMHPropertyBarcodeSelected(
                                new TblItemMutationHistoryPropertyBarcode(imhpbs.getDataItemMutationHistoryPropertyBarcode()),
                                imhpbs.isSelected());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblItemMutationHistory(dummyOGIMH.getTblItemMutationHistory());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode()));
                        dummyIMHPBSs.add(dummyIMHPBS);
                    }
                    List<TblItemMutationHistoryItemExpiredDate> dummyIMHIEDs = new ArrayList<>();
                    for (TblItemMutationHistoryItemExpiredDate imhied : ogdp.getDataIMHIEDs()) {
                        TblItemMutationHistoryItemExpiredDate dummyIMHIED = new TblItemMutationHistoryItemExpiredDate(imhied);
                        dummyIMHIED.setTblItemMutationHistory(dummyOGIMH.getTblItemMutationHistory());
                        dummyIMHIED.setTblItemExpiredDate(new TblItemExpiredDate(dummyIMHIED.getTblItemExpiredDate()));
                        dummyIMHIEDs.add(dummyIMHIED);
                    }
                    OGDetailItemMutationHistory dummyDataOGDetailItemMutationHistory = new OGDetailItemMutationHistory(
                            dummyOGIMH,
                            dummyIMHPBSs,
                            dummyIMHIEDs);
                    dummyDataOGDetailItemMutationHistories.add(dummyDataOGDetailItemMutationHistory);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLaundryManager().insertDataOutGoing(
                                dummySelectedData,
                                dummyDataOGDetailItemMutationHistories)
                                != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data out going
                            refreshDataTableOG();
                            dataOGFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                            //pribt data out going
                            printOG(selectedData);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFLaundryManager().getErrorMessage(), null);
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

    private void dataOGCancelHandle() {
        //refresh data from table & close form data out going
        refreshDataTableOG();
        dataOGFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableOG() {
        tableDataOG.getTableView().setItems(FXCollections.observableArrayList(loadAllDataOG()));
        cft.refreshFilterItems(tableDataOG.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataOG() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpReceiver.getValue() == null) {
            dataInput = false;
            errDataInput += "Penerima : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpSourceLocation.getValue() == null) {
            dataInput = false;
            errDataInput += "Laundry (Sumber) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (tableDataDetail.getTableView().getItems().isEmpty()) {
                dataInput = false;
                errDataInput += "Tabel (Detail-Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (checkDataInputInputDataOGSRDetailOutGoingQuantityHaveNullValue()) {
                    dataInput = false;
                    errDataInput += "Minimal jumlah stok 'outgoing' satu \n";
                } else {
                    if (cbpSR.getValue() != null
                            && cbpSR.getValue().getIdsr() != 0L) {   //by data store request
                        if (!checkDataInputDataOGSRDetailQuantityNeeded()) {
                            dataInput = false;
                            errDataInput += "Jumlah barang 'outgoing' yang dimasukan, \ntidak sesuai dengan jumlah barang pada 'store request' \n";
                        } else {
                            if (!checkDataInputDataOGDetailQuantityAvailable()) {
                                dataInput = false;
                                errDataInput += "Jumlah stok barang yang tersedia, \ntidak dapat memenuhi jumlah barang pada 'outgoing' \n";
                            }
                        }
                    } else {
                        if (!checkDataInputDataOGDetailQuantityAvailable()) {
                            dataInput = false;
                            errDataInput += "Jumlah stok barang yang tersedia, \ntidak dapat memenuhi jumlah barang pada 'outgoing' \n";
                        }
                    }
                }
            }
        }
        if (txtNote.getText() == null
                || txtNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Keterangan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private boolean checkDataInputInputDataOGSRDetailOutGoingQuantityHaveNullValue() {
        for (OGDetailItemMutationHistory ogdimh : (List<OGDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
            if (ogdimh.getDataOGDIMH().getTblOutGoingDetail().getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDataInputDataOGSRDetailQuantityNeeded() {
        if (selectedData.getTblStoreRequest() != null
                && selectedData.getTblStoreRequest().getIdsr() != 0L) {
            for (OGDetailItemMutationHistory ogdimh : (List<OGDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
                TblStoreRequestDetail srd = parentController.getFLaundryManager().getDataStoreRequestDetailByIDStoreRequestAndIDItem(
                        selectedData.getTblStoreRequest().getIdsr(),
                        ogdimh.getDataOGDIMH().getTblOutGoingDetail().getTblItem().getIditem());
                if (srd != null) {
                    if (srd.getItemQuantity()
                            .compareTo(ogdimh.getDataOGDIMH().getTblOutGoingDetail().getItemQuantity()) != 0) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkDataInputDataOGDetailQuantityAvailable() {
        for (OGDetailItemMutationHistory ogdimh : (List<OGDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
            if (ogdimh.getDataOGDIMH().getTblOutGoingDetail().getTblItem().getPropertyStatus()) { //property
                for (IMHPropertyBarcodeSelected imhpbs : ogdimh.getDataIMHPBSs()) {
                    if (imhpbs.isSelected()) {
                        if ((new BigDecimal("1"))
                                .compareTo(getItemLocationAvailableStockPB(
                                                imhpbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode(),
                                                ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem(),
                                                ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                }
            } else {
                if (ogdimh.getDataOGDIMH().getTblOutGoingDetail().getTblItem().getPropertyStatus()) { //consumable
                    for (TblItemMutationHistoryItemExpiredDate imhied : ogdimh.getDataIMHIEDs()) {
                        if (imhied.getItemQuantity()
                                .compareTo(getItemLocationAvailableStockIED(
                                                imhied.getTblItemExpiredDate(),
                                                ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem(),
                                                ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                } else {  //another
                    if (ogdimh.getDataOGDIMH().getTblItemMutationHistory().getItemQuantity()
                            .compareTo(getItemLocationAvailableStock(
                                            ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem(),
                                            ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                            == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private BigDecimal getItemLocationAvailableStock(
            TblItem dataItem,
            TblLocation dataLocation) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && dataLocation != null) {
            TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                return dataItemLocation.getItemQuantity();
            }
        }
        return result;
    }

    private BigDecimal getItemLocationAvailableStockPB(
            TblPropertyBarcode dataPropertyBarcode,
            TblItem dataItem,
            TblLocation dataLocation) {
        if (dataPropertyBarcode != null
                && dataItem != null
                && dataLocation != null) {
            TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = parentController.getFLaundryManager().getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                        dataItemLocation.getIdrelation(),
                        dataPropertyBarcode.getIdbarcode()
                );
                if (dataItemLocationPropertyBarcode != null) {
                    return new BigDecimal("1");
                }
            }
        }
        return new BigDecimal("0");
    }

    private BigDecimal getItemLocationAvailableStockIED(
            TblItemExpiredDate dataItemExpiredDate,
            TblItem dataItem,
            TblLocation dataLocation) {
        BigDecimal result = new BigDecimal("0");
        if (dataItemExpiredDate != null
                && dataItem != null
                && dataLocation != null) {
            TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = parentController.getFLaundryManager().getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                        dataItemLocation.getIdrelation(),
                        dataItemExpiredDate.getIditemExpiredDate()
                );
                if (dataItemLocationItemExpiredDate != null) {
                    return dataItemLocationItemExpiredDate.getItemQuantity();
                }
            }
        }
        return result;
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
        TableView<OGDetailItemMutationHistory> tableView = new TableView();
        tableView.setEditable(dataInputStatus != 3);

        TableColumn<OGDetailItemMutationHistory, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getCodeItem(),
                        param.getValue().dataOGDIMHProperty()));
        codeItem.setMinWidth(120);

        TableColumn<OGDetailItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getItemName(),
                        param.getValue().dataOGDIMHProperty()));
        itemName.setMinWidth(140);

        TableColumn<OGDetailItemMutationHistory, String> itemTypeHK = new TableColumn("House Keeping");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + (param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataOGDIMHProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<OGDetailItemMutationHistory, String> itemTypeWH = new TableColumn("Warehouse");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataOGDIMHProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<OGDetailItemMutationHistory, String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);

        TableColumn<OGDetailItemMutationHistory, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataOGDIMH().getTblOutGoingDetail().getItemQuantity()),
                        param.getValue().dataOGDIMHProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<OGDetailItemMutationHistory, String> srItemQuantity = new TableColumn("SR");
        srItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblOutGoing().getTblStoreRequest() != null
                        && param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblOutGoing().getTblStoreRequest().getIdsr() != 0L)
                                ? ClassFormatter.decimalFormat.cFormat(getSRQuantity(param.getValue()))
                                : "~",
                        param.getValue().dataOGDIMHProperty()));
        srItemQuantity.setMinWidth(100);

        TableColumn<OGDetailItemMutationHistory, String> ogItemQuantity = new TableColumn("Jumlah Barang");
        ogItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataOGDIMH().getTblItemMutationHistory().getItemQuantity()),
                        //                        param.getValue().dataOGDIMHProperty()));
                        param.getValue().getDataOGDIMH().getTblItemMutationHistory().itemQuantityProperty()));
        ogItemQuantity.setMinWidth(120);
        Callback<TableColumn<OGDetailItemMutationHistory, String>, TableCell<OGDetailItemMutationHistory, String>> cellFactory
                = new Callback<TableColumn<OGDetailItemMutationHistory, String>, TableCell<OGDetailItemMutationHistory, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemQuantity();
                    }
                };
        ogItemQuantity.setCellFactory(cellFactory);
        ogItemQuantity.setEditable(true);

        TableColumn<OGDetailItemMutationHistory, String> detail = new TableColumn("Detail");
        detail.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> "",
                        param.getValue().dataOGDIMHProperty()));
        detail.setMinWidth(100);
        Callback<TableColumn<OGDetailItemMutationHistory, String>, TableCell<OGDetailItemMutationHistory, String>> cellFactory1
                = new Callback<TableColumn<OGDetailItemMutationHistory, String>, TableCell<OGDetailItemMutationHistory, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellDetailStockOG();
                    }
                };
        detail.setCellFactory(cellFactory1);

        TableColumn<OGDetailItemMutationHistory, String> titledItemQuantity = new TableColumn("Jumlah Barang");
        titledItemQuantity.getColumns().addAll(srItemQuantity, ogItemQuantity);

        TableColumn<OGDetailItemMutationHistory, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataOGDIMHProperty()));
        unitName.setMinWidth(120);

        TableColumn<OGDetailItemMutationHistory, String> itemStock = new TableColumn("Stok Tersedia");
        itemStock.setCellValueFactory((TableColumn.CellDataFeatures<OGDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getCurrentAvailableStock(param.getValue().getDataOGDIMH().getTblOutGoingDetail().getTblItem())),
                        param.getValue().dataOGDIMHProperty()));
        itemStock.setMinWidth(120);

        if (dataInputStatus != 3) {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, ogItemQuantity, detail, itemStock, unitName);
        } else {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, itemQuantity, unitName);
        }
        tableView.setItems(loadAllDataOGDIMH());
        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private BigDecimal getSRQuantity(OGDetailItemMutationHistory ogDetailItemMutationHistory) {
        if (ogDetailItemMutationHistory != null
                && ogDetailItemMutationHistory.getDataOGDIMH() != null
                && ogDetailItemMutationHistory.getDataOGDIMH().getTblOutGoingDetail() != null
                && ogDetailItemMutationHistory.getDataOGDIMH().getTblOutGoingDetail().getTblOutGoing() != null
                && ogDetailItemMutationHistory.getDataOGDIMH().getTblOutGoingDetail().getTblOutGoing().getTblStoreRequest() != null
                && ogDetailItemMutationHistory.getDataOGDIMH().getTblOutGoingDetail().getTblItem() != null) {
            TblStoreRequestDetail srd = parentController.getFLaundryManager().getDataStoreRequestDetailByIDStoreRequestAndIDItem(
                    ogDetailItemMutationHistory.getDataOGDIMH().getTblOutGoingDetail().getTblOutGoing().getTblStoreRequest().getIdsr(),
                    ogDetailItemMutationHistory.getDataOGDIMH().getTblOutGoingDetail().getTblItem().getIditem()
            );
            if (srd != null) {
                return srd.getItemQuantity();
            }
        }
        return new BigDecimal("0");
    }

    private BigDecimal getCurrentAvailableStock(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && selectedData != null
                && selectedData.getTblLocationByIdlocationSource() != null) {
            TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    selectedData.getTblLocationByIdlocationSource().getIdlocation());
            if (dataItemLocation != null) {
                result = dataItemLocation.getItemQuantity();
            }
        }
        return result;
    }

    class EditingCellItemQuantity extends TableCell<OGDetailItemMutationHistory, String> {

        private JFXTextField tItemQuantity;

        public EditingCellItemQuantity() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null
                    && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getPropertyStatus() //!property
                    && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getConsumable()) {       //!consumable
                super.startEdit();
                tItemQuantity = new JFXTextField();
                tItemQuantity.setPromptText("Jumlah Barang");

                ClassViewSetting.setImportantField(
                        tItemQuantity);

                tItemQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", tItemQuantity);

                Bindings.bindBidirectional(tItemQuantity.textProperty(), ((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(tItemQuantity);
                tItemQuantity.selectAll();

                //cell input color
                if (this.getTableRow() != null
                        && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getPropertyStatus() //!property
                        && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getConsumable() //!consumable
                        && dataInputStatus != 3) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even");
                        getStyleClass().add("cell-input-even-edit");
                    } else {
                        getStyleClass().remove("cell-input-odd");
                        getStyleClass().add("cell-input-odd-edit");
                    }
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            tItemQuantity.textProperty().unbindBidirectional(((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().itemQuantityProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null
                    && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getPropertyStatus() //!property
                    && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getConsumable() //!consumable
                    && dataInputStatus != 3) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit");
                    getStyleClass().add("cell-input-even");
                } else {
                    getStyleClass().remove("cell-input-odd-edit");
                    getStyleClass().add("cell-input-odd");
                }
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            if (((OGDetailItemMutationHistory) data).getDataOGDIMH() != null
                                    && ((OGDetailItemMutationHistory) data).getDataOGDIMH().getTblOutGoingDetail() != null) {
                                setText(ClassFormatter.decimalFormat.cFormat(((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getItemQuantity()));
                            } else {
                                setText("");
                            }
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (data != null
                                && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getPropertyStatus() //!property
                                && !((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail().getTblItem().getConsumable() //!consumable
                                && dataInputStatus != 3) {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().add("cell-input-even");
                            } else {
                                getStyleClass().add("cell-input-odd");
                            }
                        } else {
                            getStyleClass().remove("cell-input-even");
                            getStyleClass().remove("cell-input-odd");
                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            } else {
                setText(null);
                setGraphic(null);
                getStyleClass().remove("cell-input-even");
                getStyleClass().remove("cell-input-odd");
            }
        }

    }

    class EditingCellDetailStockOG extends TableCell<OGDetailItemMutationHistory, String> {

        private JFXCComboBoxPopup cbpStockOGDetail;

        public EditingCellDetailStockOG() {

        }

        @Override
        public void startEdit() {
            super.startEdit();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (this.getTableRow() != null) {
                    Object data = this.getTableRow().getItem();
                    if (data != null
                            && (isProperty(((OGDetailItemMutationHistory) data).getDataOGDIMH().getTblOutGoingDetail()) //property barcode
                            || isConsumable(((OGDetailItemMutationHistory) data).getDataOGDIMH().getTblOutGoingDetail()))) {  //consumable
                        cbpStockOGDetail = ((OGDetailItemMutationHistory) data).getCbpListDetail();

                        cbpStockOGDetail.getStyleClass().add("detail-combo-box-popup");

                        cbpStockOGDetail.hide();

                        cbpStockOGDetail.setPrefSize(100, 25);
                        setAlignment(Pos.CENTER);

                        if (isProperty(((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail()) //property barcode
                                || isConsumable(((OGDetailItemMutationHistory) this.getTableRow().getItem()).getDataOGDIMH().getTblOutGoingDetail())) { //consumable
                            cbpStockOGDetail.setDisable(false);
                        } else {
                            cbpStockOGDetail.setDisable(true);
                        }

                        setText(null);
                        setGraphic(cbpStockOGDetail);

                    } else {

                        setText(null);
                        setGraphic(null);

                    }

                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        }

        private boolean isProperty(TblOutGoingDetail dataOGDetail) {
            return dataOGDetail != null
                    && dataOGDetail.getTblItem() != null
                    && dataOGDetail.getTblItem().getPropertyStatus();   //Property
        }

        private boolean isConsumable(TblOutGoingDetail dataOGDetail) {
            return dataOGDetail != null
                    && dataOGDetail.getTblItem() != null
                    && dataOGDetail.getTblItem().getConsumable();   //Consumable
        }

    }

    private JFXCComboBoxPopup getComboBoxDetails(TblOutGoingDetailItemMutationHistory ogdimh) {

        JFXCComboBoxPopup cbpDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-property-details");
        button.setTooltip(new Tooltip(""));
        button.setOnMouseClicked((e) -> cbpDetails.show());

        AnchorPane ancDataTableProperties = new AnchorPane();

        //layout
        AnchorPane ancContent = new AnchorPane();
        AnchorPane.setBottomAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setLeftAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setRightAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setTopAnchor(ancDataTableProperties, 0.0);
        ancContent.getChildren().add(ancDataTableProperties);

        //popup content
        BorderPane content = new BorderPane();
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(435, 360);

        content.setCenter(ancContent);

        cbpDetails.setPopupEditor(false);
        cbpDetails.promptTextProperty().set("");
        cbpDetails.setLabelFloat(false);
        cbpDetails.setPopupButton(button);
        cbpDetails.settArrowButton(true);
        cbpDetails.setPopupContent(content);

        return cbpDetails;
    }

    private JFXCComboBoxPopup getComboBoxPropertyDetails(List<IMHPropertyBarcodeSelected> IMHPBSs) {

        JFXCComboBoxPopup cbpPropertyDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-property-details");
        button.setTooltip(new Tooltip("Properti"));
        if (dataInputStatus != 3) {
            button.getStyleClass().add("button-detail-input");
        }
        button.setOnMouseClicked((e) -> cbpPropertyDetails.show());

        AnchorPane ancDataTableProperties = new AnchorPane();

        //set data table
        TableView<IMHPropertyBarcodeSelected> tablePropretyBarcode = new TableView<>();
        tablePropretyBarcode.setEditable(true);

        TableColumn<IMHPropertyBarcodeSelected, Boolean> selected = new TableColumn("Pilih");
        selected.setMinWidth(80);
        selected.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selected.setCellFactory((TableColumn<IMHPropertyBarcodeSelected, Boolean> param) -> new CheckBoxCell<>());
        selected.setEditable(dataInputStatus != 3);

        TableColumn<IMHPropertyBarcodeSelected, String> codeBarcode = new TableColumn("Barcode");
        codeBarcode.setMinWidth(120);
        codeBarcode.setCellValueFactory((TableColumn.CellDataFeatures<IMHPropertyBarcodeSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataItemMutationHistoryPropertyBarcode() == null
                        || param.getValue().getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode() == null
                                ? "" : param.getValue().getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getCodeBarcode(),
                        param.getValue().dataItemMutationHistoryPropertyBarcodeProperty()));

        tablePropretyBarcode.getColumns().addAll(codeBarcode, selected);
        tablePropretyBarcode.setItems(FXCollections.observableArrayList(IMHPBSs));

        //set to layout (data popup) 
        AnchorPane.setBottomAnchor(tablePropretyBarcode, 0.0);
        AnchorPane.setLeftAnchor(tablePropretyBarcode, 0.0);
        AnchorPane.setRightAnchor(tablePropretyBarcode, 0.0);
        AnchorPane.setTopAnchor(tablePropretyBarcode, 0.0);
        ancDataTableProperties.getChildren().add(tablePropretyBarcode);

        //layout
        AnchorPane ancContent = new AnchorPane();
        AnchorPane.setBottomAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setLeftAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setRightAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setTopAnchor(ancDataTableProperties, 0.0);
        ancContent.getChildren().add(ancDataTableProperties);

        //popup content
        BorderPane content = new BorderPane();
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(220, 300);

        content.setCenter(ancContent);

        cbpPropertyDetails.setPopupEditor(false);
        cbpPropertyDetails.promptTextProperty().set("");
        cbpPropertyDetails.setLabelFloat(false);
        cbpPropertyDetails.setPopupButton(button);
        cbpPropertyDetails.settArrowButton(true);
        cbpPropertyDetails.setPopupContent(content);

        return cbpPropertyDetails;
    }

    public class CheckBoxCell<S, T> extends TableCell<S, T> {

        private final CheckBox checkBox;
        private ObservableValue<T> ov;

        public CheckBoxCell() {
            checkBox = new CheckBox();
            checkBox.setAlignment(Pos.CENTER);
            setAlignment(Pos.CENTER);
            setGraphic(checkBox);
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(checkBox);
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                }
                ov = getTableColumn().getCellObservableValue(getIndex());
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
                }
            }
        }

        @Override
        public void startEdit() {
//            super.startEdit();
        }

        @Override
        public void cancelEdit() {
//            super.cancelEdit();
        }

    }

    private JFXCComboBoxPopup getComboBoxItemExpiredDateDetails(List<TblItemMutationHistoryItemExpiredDate> imhieds) {

        JFXCComboBoxPopup cbpItemExpiredDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-property-details");
        button.setTooltip(new Tooltip("Expired Date"));
        if (dataInputStatus != 3) {
            button.getStyleClass().add("button-detail-input");
        }
        button.setOnMouseClicked((e) -> cbpItemExpiredDetails.show());

        AnchorPane ancDataTableItemExpiredDates = new AnchorPane();

        //set data table
        TableView<TblItemMutationHistoryItemExpiredDate> tableItemExpiredDate = new TableView<>();
        tableItemExpiredDate.setEditable(dataInputStatus != 3);

        TableColumn<TblItemMutationHistoryItemExpiredDate, String> expiredDate = new TableColumn("Tgl. Kadaluarsa");
        expiredDate.setMinWidth(160);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<TblItemMutationHistoryItemExpiredDate, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblItemExpiredDate() == null
                                ? ""
                                : (param.getValue().getTblItemExpiredDate().getItemExpiredDate() == null
                                        ? ""
                                        : ClassFormatter.dateFormate.format(param.getValue().getTblItemExpiredDate().getItemExpiredDate())),
                        param.getValue().getTblItemExpiredDate().itemExpiredDateProperty()));

        TableColumn<TblItemMutationHistoryItemExpiredDate, String> quantity = new TableColumn("Jumlah (Barang)");
        quantity.setMinWidth(120);
        quantity.setCellValueFactory((TableColumn.CellDataFeatures<TblItemMutationHistoryItemExpiredDate, String> param)
                -> Bindings.createStringBinding(()
                        -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()),
                        param.getValue().itemQuantityProperty()));
        Callback<TableColumn<TblItemMutationHistoryItemExpiredDate, String>, TableCell<TblItemMutationHistoryItemExpiredDate, String>> cellFactory
                = new Callback<TableColumn<TblItemMutationHistoryItemExpiredDate, String>, TableCell<TblItemMutationHistoryItemExpiredDate, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemExpiredDateQuantity();
                    }
                };
        quantity.setCellFactory(cellFactory);
        quantity.setEditable(dataInputStatus != 3);

        TableColumn<TblItemMutationHistoryItemExpiredDate, String> stockAvailable = new TableColumn("Stok (Tersedia)");
        stockAvailable.setMinWidth(120);
        stockAvailable.setCellValueFactory((TableColumn.CellDataFeatures<TblItemMutationHistoryItemExpiredDate, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblItemExpiredDate() == null
                                ? "~"
                                : ClassFormatter.decimalFormat.format(getCurrentAvailableStockIED(param.getValue().getTblItemExpiredDate())),
                        param.getValue().tblItemExpiredDateProperty()));

        tableItemExpiredDate.getColumns().addAll(expiredDate, quantity, stockAvailable);
        tableItemExpiredDate.setItems(FXCollections.observableArrayList(imhieds));

        //set to layout (data popup) 
        AnchorPane.setBottomAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setLeftAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setRightAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setTopAnchor(tableItemExpiredDate, 0.0);
        ancDataTableItemExpiredDates.getChildren().add(tableItemExpiredDate);

        //layout
        AnchorPane ancContent = new AnchorPane();
        AnchorPane.setBottomAnchor(ancDataTableItemExpiredDates, 0.0);
        AnchorPane.setLeftAnchor(ancDataTableItemExpiredDates, 0.0);
        AnchorPane.setRightAnchor(ancDataTableItemExpiredDates, 0.0);
        AnchorPane.setTopAnchor(ancDataTableItemExpiredDates, 0.0);
        ancContent.getChildren().add(ancDataTableItemExpiredDates);

        //popup content
        BorderPane content = new BorderPane();
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(415, 300);

        content.setCenter(ancContent);

        cbpItemExpiredDetails.setPopupEditor(false);
        cbpItemExpiredDetails.promptTextProperty().set("");
        cbpItemExpiredDetails.setLabelFloat(false);
        cbpItemExpiredDetails.setPopupButton(button);
        cbpItemExpiredDetails.settArrowButton(true);
        cbpItemExpiredDetails.setPopupContent(content);

        return cbpItemExpiredDetails;
    }

    private BigDecimal getCurrentAvailableStockIED(TblItemExpiredDate dataItemExpiredDate) {
        BigDecimal result = new BigDecimal("0");
        if (dataItemExpiredDate != null
                && selectedData != null
                && selectedData.getTblLocationByIdlocationSource() != null) {
            TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItemExpiredDate.getTblItem().getIditem(),
                    selectedData.getTblLocationByIdlocationSource().getIdlocation());
            if (dataItemLocation != null) {
                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = parentController.getFLaundryManager().getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                        dataItemLocation.getIdrelation(),
                        dataItemExpiredDate.getIditemExpiredDate());
                if (dataItemLocationItemExpiredDate != null) {
                    result = dataItemLocationItemExpiredDate.getItemQuantity();
                }
            }
        }
        return result;
    }

    class EditingCellItemExpiredDateQuantity extends TableCell<TblItemMutationHistoryItemExpiredDate, String> {

        private JFXTextField tItemExpiredQuantity;

        public EditingCellItemExpiredDateQuantity() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                tItemExpiredQuantity = new JFXTextField();
                tItemExpiredQuantity.setPromptText("Jumlah Barang");

                ClassViewSetting.setImportantField(
                        tItemExpiredQuantity);

                tItemExpiredQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", tItemExpiredQuantity);

                Bindings.bindBidirectional(tItemExpiredQuantity.textProperty(), ((TblItemMutationHistoryItemExpiredDate) this.getTableRow().getItem()).itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(tItemExpiredQuantity);
                tItemExpiredQuantity.selectAll();

                //cell input color
                if (this.getTableRow() != null
                        && dataInputStatus != 3) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even");
                        getStyleClass().add("cell-input-even-edit");
                    } else {
                        getStyleClass().remove("cell-input-odd");
                        getStyleClass().add("cell-input-odd-edit");
                    }
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            tItemExpiredQuantity.textProperty().unbindBidirectional(((TblItemMutationHistoryItemExpiredDate) this.getTableRow().getItem()).itemQuantityProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null
                    && dataInputStatus != 3) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit");
                    getStyleClass().add("cell-input-even");
                } else {
                    getStyleClass().remove("cell-input-odd-edit");
                    getStyleClass().add("cell-input-odd");
                }
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            setText(ClassFormatter.decimalFormat.cFormat(((TblItemMutationHistoryItemExpiredDate) data).getItemQuantity()));
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (dataInputStatus != 3) {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().add("cell-input-even");

                            } else {
                                getStyleClass().add("cell-input-odd");
                            }
                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
        }

    }

    private ObservableList<OGDetailItemMutationHistory> loadAllDataOGDIMH() {
        List<OGDetailItemMutationHistory> list = new ArrayList<>();
        if (dataInputStatus == 3) {   //just for show data
            //data out going - detail
            List<TblOutGoingDetail> ogds = parentController.getFLaundryManager().getAllDataOutGoingDetailByIDOutGoing(selectedData.getIdog());
            for (TblOutGoingDetail ogd : ogds) {
                //set data item
                ogd.setTblItem(parentController.getFLaundryManager().getDataItem(ogd.getTblItem().getIditem()));
                //set data unit
                ogd.getTblItem().setTblUnit(parentController.getFLaundryManager().getDataUnit(ogd.getTblItem().getTblUnit().getIdunit()));
                //set data item type (hk)
                if (ogd.getTblItem().getTblItemTypeHk() != null) {
                    ogd.getTblItem().setTblItemTypeHk(parentController.getFLaundryManager().getDataItemTypeHK(ogd.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type (wh)
                if (ogd.getTblItem().getTblItemTypeWh() != null) {
                    ogd.getTblItem().setTblItemTypeWh(parentController.getFLaundryManager().getDataItemTypeWH(ogd.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                //data og detail - imh
                TblOutGoingDetailItemMutationHistory ogdimh = parentController.getFLaundryManager().getDataOutGoingDetailItemMutationHistoryByIDOutGoingDetail(ogd.getIddetail());
                ogdimh.setTblOutGoingDetail(ogd);
                ogdimh.setTblItemMutationHistory(parentController.getFLaundryManager().getDataItemMutationHistory(ogdimh.getTblItemMutationHistory().getIdmutation()));
                //data imh - property (s)
                List<TblItemMutationHistoryPropertyBarcode> imhpbs = parentController.getFLaundryManager().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(ogdimh.getTblItemMutationHistory().getIdmutation());
                List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                    imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, true));
                }
                //data imh - item expired date (s)
                List<TblItemMutationHistoryItemExpiredDate> imhieds = parentController.getFLaundryManager().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(ogdimh.getTblItemMutationHistory().getIdmutation());
                //data 'OGDetailItemMutationHistory'
                OGDetailItemMutationHistory data = new OGDetailItemMutationHistory(ogdimh,
                        imhpbss,
                        imhieds);
                list.add(data);
            }
        } else {  //for input (generate) data
            if (selectedData.getTblLocationByIdlocationSource() != null
                    && selectedData.getTblLocationByIdlocationDestination() != null) {
                if (selectedData.getTblStoreRequest() != null
                        && selectedData.getTblStoreRequest().getIdsr() != 0L) {  //data from store request
                    //data store request - detail
                    List<TblStoreRequestDetail> srds = parentController.getFLaundryManager().getAllDataStoreRequestDetailByIDStoreRequest(selectedData.getTblStoreRequest().getIdsr());
                    for (TblStoreRequestDetail srd : srds) {
                        //set data out going detail
                        TblOutGoingDetail ogd = new TblOutGoingDetail();
                        ogd.setTblOutGoing(selectedData);
                        //set data item
                        ogd.setTblItem(parentController.getFLaundryManager().getDataItem(srd.getTblItem().getIditem()));
                        //set data unit
                        ogd.getTblItem().setTblUnit(parentController.getFLaundryManager().getDataUnit(srd.getTblItem().getTblUnit().getIdunit()));
                        //set data item type (hk)
                        if (srd.getTblItem().getTblItemTypeHk() != null) {
                            ogd.getTblItem().setTblItemTypeHk(parentController.getFLaundryManager().getDataItemTypeHK(srd.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                        }
                        //set data item type (wh)
                        if (srd.getTblItem().getTblItemTypeWh() != null) {
                            ogd.getTblItem().setTblItemTypeWh(parentController.getFLaundryManager().getDataItemTypeWH(srd.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                        }
                        //item quantity
                        ogd.setItemQuantity(new BigDecimal("0"));
                        //data ogd - imh
                        if (ogd.getTblItem().getPropertyStatus()) {   //property
                            //data og detail - imh
                            TblOutGoingDetailItemMutationHistory ogdimh = new TblOutGoingDetailItemMutationHistory();
                            ogdimh.setTblOutGoingDetail(ogd);
                            ogdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                            ogdimh.getTblItemMutationHistory().setTblItem(ogd.getTblItem());
                            ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                            ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                            ogdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                            ogdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(ogd.itemQuantityProperty());
                            //data imh - property (s)
                            List<TblItemMutationHistoryPropertyBarcode> imhpbs = new ArrayList<>();
                            TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDLocationAndIDItem(
                                    selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                                    ogd.getTblItem().getIditem()
                            );
                            List<TblItemLocationPropertyBarcode> ilpbs = parentController.getFLaundryManager().getAllDataItemLocationPropertyBarcodeByIDItemLocation(dataItemLocation.getIdrelation());
                            for (TblItemLocationPropertyBarcode ilpb : ilpbs) {
                                TblItemMutationHistoryPropertyBarcode imhpb = new TblItemMutationHistoryPropertyBarcode();
                                imhpb.setTblItemMutationHistory(ogdimh.getTblItemMutationHistory());
                                imhpb.setTblPropertyBarcode(ilpb.getTblPropertyBarcode());
                                imhpbs.add(imhpb);
                            }
                            List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                            for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                                imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, false));
                            }
                            //data 'OGDetailItemMutationHistory'
                            OGDetailItemMutationHistory data = new OGDetailItemMutationHistory(ogdimh,
                                    imhpbss,
                                    new ArrayList<>());
                            list.add(data);
                        } else {
                            if (ogd.getTblItem().getConsumable()) {   //consumable
                                //data og detail - imh
                                TblOutGoingDetailItemMutationHistory ogdimh = new TblOutGoingDetailItemMutationHistory();
                                ogdimh.setTblOutGoingDetail(ogd);
                                ogdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                                ogdimh.getTblItemMutationHistory().setTblItem(ogd.getTblItem());
                                ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                                ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                                ogdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                                ogdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(ogd.itemQuantityProperty());
                                //data imh - item expired date (s)
                                List<TblItemMutationHistoryItemExpiredDate> imhieds = new ArrayList<>();
                                TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDLocationAndIDItem(
                                        selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                                        ogd.getTblItem().getIditem()
                                );
                                List<TblItemLocationItemExpiredDate> ilieds = parentController.getFLaundryManager().getAllDataItemLocationItemExpiredDateByIDItemLocation(dataItemLocation.getIdrelation());
                                for (TblItemLocationItemExpiredDate ilied : ilieds) {
                                    TblItemMutationHistoryItemExpiredDate imhied = new TblItemMutationHistoryItemExpiredDate();
                                    imhied.setTblItemMutationHistory(ogdimh.getTblItemMutationHistory());
                                    imhied.setTblItemExpiredDate(ilied.getTblItemExpiredDate());
                                    imhied.setItemQuantity(new BigDecimal("0"));
                                    imhieds.add(imhied);
                                }
                                //data 'OGDetailItemMutationHistory'
                                OGDetailItemMutationHistory data = new OGDetailItemMutationHistory(ogdimh,
                                        new ArrayList<>(),
                                        imhieds);
                                list.add(data);
                            } else {  //another
                                //data og detail - imh
                                TblOutGoingDetailItemMutationHistory ogdimh = new TblOutGoingDetailItemMutationHistory();
                                ogdimh.setTblOutGoingDetail(ogd);
                                ogdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                                ogdimh.getTblItemMutationHistory().setTblItem(ogd.getTblItem());
                                ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                                ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                                ogdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                                ogdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(ogd.itemQuantityProperty());
                                //data 'OGDetailItemMutationHistory'
                                OGDetailItemMutationHistory data = new OGDetailItemMutationHistory(ogdimh,
                                        new ArrayList<>(),
                                        new ArrayList<>());
                                list.add(data);
                            }
                        }
                    }
                } else {
                    //do nothing...
                }
            } else {
                //do nothing...
            }
        }
        return FXCollections.observableArrayList(list);
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
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataDetail.addButtonControl(buttonControls);
    }

    public class OGDetailItemMutationHistory {

        private final ObjectProperty<TblOutGoingDetailItemMutationHistory> dataOGDIMH = new SimpleObjectProperty<>();

        private List<IMHPropertyBarcodeSelected> dataIMHPBSs = new ArrayList<>();

        private List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = new ArrayList<>();

        private final ObjectProperty<JFXCComboBoxPopup> cbpListDetail = new SimpleObjectProperty<>();

        public OGDetailItemMutationHistory(
                TblOutGoingDetailItemMutationHistory dataOGDIMH,
                List<IMHPropertyBarcodeSelected> dataIMHPBSs,
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs) {
            //data ogd - imh
            this.dataOGDIMH.set(dataOGDIMH);

            //data imh - pb (s)
            for (IMHPropertyBarcodeSelected dataIMHPBS : dataIMHPBSs) {
                this.dataIMHPBSs.add(dataIMHPBS);
            }

            //data imh - ied (s)
            for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                this.dataIMHIEDs.add(dataIMHIED);
            }

            //data cbListDetail
            if (this.dataOGDIMH.get() == null
                    || this.dataOGDIMH.get().getTblOutGoingDetail() == null
                    || this.dataOGDIMH.get().getTblOutGoingDetail().getTblItem() == null) {
                //data cb-list combobox
                this.cbpListDetail.set(getComboBoxDetails(this.dataOGDIMH.get()));
            } else {
                if (this.dataOGDIMH.get().getTblOutGoingDetail().getTblItem().getPropertyStatus()) {   //Property
                    //data cb-list combobox
                    this.cbpListDetail.set(getComboBoxPropertyDetails(this.dataIMHPBSs));
                } else {
                    if (this.dataOGDIMH.get().getTblOutGoingDetail().getTblItem().getConsumable()) {    //cosumable
                        for (TblItemMutationHistoryItemExpiredDate dataIMHIED : this.dataIMHIEDs) {
                            dataIMHIED.itemQuantityProperty().addListener((obs, oldVale, newVal) -> {
                                this.dataOGDIMH.get().getTblItemMutationHistory().setItemQuantity(calculationTotalItemExpiredDateOG(
                                        this.dataIMHIEDs
                                ));
                            });
                        }
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.dataIMHIEDs));
                    } else {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails(this.dataOGDIMH.get()));
                    }
                }
            }
        }

        private BigDecimal calculationTotalItemExpiredDateOG(List<TblItemMutationHistoryItemExpiredDate> dataIMHPBSs) {
            BigDecimal result = new BigDecimal("0");
            for (TblItemMutationHistoryItemExpiredDate dataIMHPBS : dataIMHPBSs) {
                if (dataIMHPBS.getItemQuantity() != null) {
                    result = result.add(dataIMHPBS.getItemQuantity());
                }
            }
            return result;
        }

        public ObjectProperty<TblOutGoingDetailItemMutationHistory> dataOGDIMHProperty() {
            return dataOGDIMH;
        }

        public TblOutGoingDetailItemMutationHistory getDataOGDIMH() {
            return dataOGDIMHProperty().get();
        }

        public void setDataOGDIMH(TblOutGoingDetailItemMutationHistory dataOGDIMH) {
            dataOGDIMHProperty().set(dataOGDIMH);
        }

        public List<IMHPropertyBarcodeSelected> getDataIMHPBSs() {
            return this.dataIMHPBSs;
        }

        public void setDataIMHPBs(List<IMHPropertyBarcodeSelected> dataIMHPBSs) {
            this.dataIMHPBSs = new ArrayList<>();
            for (IMHPropertyBarcodeSelected dataIMHPBS : dataIMHPBSs) {
                this.dataIMHPBSs.add(dataIMHPBS);
            }
        }

        public List<TblItemMutationHistoryItemExpiredDate> getDataIMHIEDs() {
            return this.dataIMHIEDs;
        }

        public void setDataIMHIEDs(List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs) {
            this.dataIMHIEDs = new ArrayList<>();
            for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                this.dataIMHIEDs.add(dataIMHIED);
            }
        }

        public ObjectProperty<JFXCComboBoxPopup> cbpListDetailProperty() {
            return cbpListDetail;
        }

        public JFXCComboBoxPopup getCbpListDetail() {
            return cbpListDetailProperty().get();
        }

        public void setCbpListDetail(JFXCComboBoxPopup cbpListDetail) {
            this.cbpListDetailProperty().set(cbpListDetail);
        }

    }

    public class IMHPropertyBarcodeSelected {

        private final ObjectProperty<TblItemMutationHistoryPropertyBarcode> dataItemMutationHistoryPropertyBarcode = new SimpleObjectProperty<>();

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public IMHPropertyBarcodeSelected(
                TblItemMutationHistoryPropertyBarcode dataItemMutationHistoryPropertyBarcode,
                boolean selected) {
            //data item mutation history - property barcode
            this.dataItemMutationHistoryPropertyBarcode.set(dataItemMutationHistoryPropertyBarcode);
            //data selected
            this.selected.set(selected);
            //set listener to selected
            this.selected.addListener((obs, oldVal, newVal) -> {
                //refresh store request item mutation history - item quantity
                if (newVal) { //selected
                    dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().setItemQuantity(dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().getItemQuantity().add(new BigDecimal("1")));
                } else {  //unselected
                    dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().setItemQuantity(dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().getItemQuantity().subtract(new BigDecimal("1")));
                }
            });
        }

        public ObjectProperty<TblItemMutationHistoryPropertyBarcode> dataItemMutationHistoryPropertyBarcodeProperty() {
            return dataItemMutationHistoryPropertyBarcode;
        }

        public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcode() {
            return dataItemMutationHistoryPropertyBarcodeProperty().get();
        }

        public void setDataItemMutationHistoryPropertyBarcode(TblItemMutationHistoryPropertyBarcode dataItemMutationHistoryPropertyBarcode) {
            dataItemMutationHistoryPropertyBarcodeProperty().set(dataItemMutationHistoryPropertyBarcode);
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selectedProperty().get();
        }

        public void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

    }

    public OGDetailItemMutationHistory selectedDataDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailCreateHandle() {
        if (selectedData.getTblStoreRequest() == null
                || selectedData.getTblStoreRequest().getIdsr() == 0L) {
            if (selectedData.getTblLocationByIdlocationSource() != null) {
                dataInputDetailStatus = 0;
                selectedDataDetail = new OGDetailItemMutationHistory(null, new ArrayList<>(), new ArrayList<>());
                //open form data - detail
                showDataDetailDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA", "Pilih data laundry (sumber) terlebih dahulu..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA", "Data harus disesuaikan dengan data store request..!", null);
        }
    }

    public void dataDetailDeleteHandle() {
        if (selectedData.getTblStoreRequest() == null
                || selectedData.getTblStoreRequest().getIdsr() == 0L) {
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
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA", "Data harus disesuaikan dengan data store request..!", null);
        }
    }

    public OGDetailItemMutationHistory generateDataOGDetailItemMutationHistory(TblItem dataItem) {
        if (selectedData != null
                && selectedData.getTblLocationByIdlocationSource() != null
                && selectedData.getTblLocationByIdlocationDestination() != null
                && dataItem != null) {
            //set data out going detail
            TblOutGoingDetail ogd = new TblOutGoingDetail();
            ogd.setTblOutGoing(selectedData);
            //set data item
            ogd.setTblItem(parentController.getFLaundryManager().getDataItem(dataItem.getIditem()));
            //set data unit
            ogd.getTblItem().setTblUnit(parentController.getFLaundryManager().getDataUnit(dataItem.getTblUnit().getIdunit()));
            //set data item type (hk)
            if (dataItem.getTblItemTypeHk() != null) {
                ogd.getTblItem().setTblItemTypeHk(parentController.getFLaundryManager().getDataItemTypeHK(dataItem.getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type (wh)
            if (dataItem.getTblItemTypeWh() != null) {
                ogd.getTblItem().setTblItemTypeWh(parentController.getFLaundryManager().getDataItemTypeWH(dataItem.getTblItemTypeWh().getIditemTypeWh()));
            }
            //item quantity
            ogd.setItemQuantity(new BigDecimal("0"));
            //data ogd - imh
            if (ogd.getTblItem().getPropertyStatus()) {   //property
                //data og detail - imh
                TblOutGoingDetailItemMutationHistory ogdimh = new TblOutGoingDetailItemMutationHistory();
                ogdimh.setTblOutGoingDetail(ogd);
                ogdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                ogdimh.getTblItemMutationHistory().setTblItem(ogd.getTblItem());
                ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                ogdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                ogdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(ogd.itemQuantityProperty());
                //data imh - property (s)
                List<TblItemMutationHistoryPropertyBarcode> imhpbs = new ArrayList<>();
                TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDLocationAndIDItem(
                        selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                        ogd.getTblItem().getIditem()
                );
                List<TblItemLocationPropertyBarcode> ilpbs = parentController.getFLaundryManager().getAllDataItemLocationPropertyBarcodeByIDItemLocation(dataItemLocation.getIdrelation());
                for (TblItemLocationPropertyBarcode ilpb : ilpbs) {
                    TblItemMutationHistoryPropertyBarcode imhpb = new TblItemMutationHistoryPropertyBarcode();
                    imhpb.setTblItemMutationHistory(ogdimh.getTblItemMutationHistory());
                    imhpb.setTblPropertyBarcode(ilpb.getTblPropertyBarcode());
                    imhpbs.add(imhpb);
                }
                List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                    imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, false));
                }
                //data 'OGDetailItemMutationHistory'
                OGDetailItemMutationHistory data = new OGDetailItemMutationHistory(ogdimh,
                        imhpbss,
                        new ArrayList<>());
                return data;
            } else {
                if (ogd.getTblItem().getConsumable()) {   //consumable
                    //data og detail - imh
                    TblOutGoingDetailItemMutationHistory ogdimh = new TblOutGoingDetailItemMutationHistory();
                    ogdimh.setTblOutGoingDetail(ogd);
                    ogdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                    ogdimh.getTblItemMutationHistory().setTblItem(ogd.getTblItem());
                    ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                    ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                    ogdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    ogdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(ogd.itemQuantityProperty());
                    //data imh - item expired date (s)
                    List<TblItemMutationHistoryItemExpiredDate> imhieds = new ArrayList<>();
                    TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDLocationAndIDItem(
                            selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                            ogd.getTblItem().getIditem()
                    );
                    List<TblItemLocationItemExpiredDate> ilieds = parentController.getFLaundryManager().getAllDataItemLocationItemExpiredDateByIDItemLocation(dataItemLocation.getIdrelation());
                    for (TblItemLocationItemExpiredDate ilied : ilieds) {
                        TblItemMutationHistoryItemExpiredDate imhied = new TblItemMutationHistoryItemExpiredDate();
                        imhied.setTblItemMutationHistory(ogdimh.getTblItemMutationHistory());
                        imhied.setTblItemExpiredDate(ilied.getTblItemExpiredDate());
                        imhied.setItemQuantity(new BigDecimal("0"));
                        imhieds.add(imhied);
                    }
                    //data 'OGDetailItemMutationHistory'
                    OGDetailItemMutationHistory data = new OGDetailItemMutationHistory(ogdimh,
                            new ArrayList<>(),
                            imhieds);
                    return data;
                } else {  //another
                    //data og detail - imh
                    TblOutGoingDetailItemMutationHistory ogdimh = new TblOutGoingDetailItemMutationHistory();
                    ogdimh.setTblOutGoingDetail(ogd);
                    ogdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                    ogdimh.getTblItemMutationHistory().setTblItem(ogd.getTblItem());
                    ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                    ogdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                    ogdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    ogdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(ogd.itemQuantityProperty());
                    //data 'OGDetailItemMutationHistory'
                    OGDetailItemMutationHistory data = new OGDetailItemMutationHistory(ogdimh,
                            new ArrayList<>(),
                            new ArrayList<>());
                    return data;
                }
            }
        }
        return null;
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry_out_going/LaundryOutGoingDetailDialog.fxml"));

            LaundryOutGoingDetailController controller = new LaundryOutGoingDetailController(this);
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
        setDataOGSplitpane();

        //init table
        initTableDataOG();

        //init form
        initFormDataOG();

        spDataOG.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataOGFormShowStatus.set(0.0);
        });
    }

    public LaundryOutGoingController(FeatureLaundryController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLaundryController parentController;

    public FLaundryManager getService() {
        return parentController.getFLaundryManager();
    }

}
