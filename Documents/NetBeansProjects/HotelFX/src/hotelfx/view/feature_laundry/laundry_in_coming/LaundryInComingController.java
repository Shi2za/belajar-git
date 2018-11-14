/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_laundry.laundry_in_coming;

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
import hotelfx.persistence.model.TblInComing;
import hotelfx.persistence.model.TblInComingDetail;
import hotelfx.persistence.model.TblInComingDetailItemMutationHistory;
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
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
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
public class LaundryInComingController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataIC;

    private DoubleProperty dataICFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataICLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataICSplitpane() {
        spDataIC.setDividerPositions(1);

        dataICFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataICFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataIC.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataIC.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataICFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataICLayout.setDisable(false);
                    tableDataICLayoutDisableLayer.setDisable(true);
                    tableDataICLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataICLayout.setDisable(true);
                    tableDataICLayoutDisableLayer.setDisable(false);
                    tableDataICLayoutDisableLayer.toFront();
                }
            }
        });

        dataICFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataICLayout;

    private ClassFilteringTable<TblInComing> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataIC;

    private void initTableDataIC() {
        //set table
        setTableDataIC();
        //set control
        setTableControlDataIC();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataIC, 15.0);
        AnchorPane.setLeftAnchor(tableDataIC, 15.0);
        AnchorPane.setRightAnchor(tableDataIC, 15.0);
        AnchorPane.setTopAnchor(tableDataIC, 15.0);
        ancBodyLayout.getChildren().add(tableDataIC);
    }

    private void setTableDataIC() {
        TableView<TblInComing> tableView = new TableView();

        TableColumn<TblInComing, String> icDate = new TableColumn<>("Tgl. Incoming");
        icDate.setCellValueFactory((TableColumn.CellDataFeatures<TblInComing, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getIcdate()),
                        param.getValue().icdateProperty()));
        icDate.setMinWidth(140);

        TableColumn<TblInComing, String> codeIC = new TableColumn("No. IC");
        codeIC.setCellValueFactory(cellData -> cellData.getValue().codeIcProperty());
        codeIC.setMinWidth(120);

        TableColumn<TblInComing, String> createdBy = new TableColumn<>("Dibuat Oleh");
        createdBy.setCellValueFactory((TableColumn.CellDataFeatures<TblInComing, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblEmployeeByCreateBy() != null)
                                ? param.getValue().getTblEmployeeByCreateBy().getTblPeople().getFullName()
                                : "-",
                        param.getValue().tblEmployeeByCreateByProperty()));
        createdBy.setMinWidth(140);

        TableColumn<TblInComing, String> sentBy = new TableColumn<>("Dikirim");
        sentBy.setCellValueFactory((TableColumn.CellDataFeatures<TblInComing, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblEmployeeByIdsender() != null)
                                ? param.getValue().getTblEmployeeByIdsender().getTblPeople().getFullName()
                                : "-",
                        param.getValue().tblEmployeeByIdsenderProperty()));
        sentBy.setMinWidth(140);

        TableColumn<TblInComing, String> byTitle = new TableColumn("Karyawan");
        byTitle.getColumns().addAll(createdBy, sentBy);

        TableColumn<TblInComing, String> sourceLocation = new TableColumn<>("Dari");
        sourceLocation.setCellValueFactory((TableColumn.CellDataFeatures<TblInComing, String> param)
                -> Bindings.createStringBinding(() -> getSourceLocationName(param.getValue()),
                        param.getValue().tblLocationByIdlocationSourceProperty()));
        sourceLocation.setMinWidth(160);

        TableColumn<TblInComing, String> destinationLocation = new TableColumn<>("Laundry"); //Ke
        destinationLocation.setCellValueFactory((TableColumn.CellDataFeatures<TblInComing, String> param)
                -> Bindings.createStringBinding(() -> getDestinationLocationName(param.getValue()),
                        param.getValue().tblLocationByIdlocationDestinationProperty()));
        destinationLocation.setMinWidth(160);

        TableColumn<TblInComing, String> locationTitle = new TableColumn("Laundry");
        locationTitle.getColumns().addAll(sourceLocation, destinationLocation);

        tableView.getColumns().addAll(icDate, codeIC, createdBy, destinationLocation);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataIC()));

        tableView.setRowFactory(tv -> {
            TableRow<TblInComing> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataICUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataICShowHandle();
                        }
                    }
                }
            });
            return row;
        });

        tableDataIC = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblInComing.class,
                tableDataIC.getTableView(),
                tableDataIC.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getSourceLocationName(TblInComing dataIC) {
        if (dataIC != null) {
            return getNameLocation(dataIC.getTblLocationByIdlocationSource());
        }
        return "-";
    }

    private String getDestinationLocationName(TblInComing dataIC) {
        if (dataIC != null) {
            return getNameLocation(dataIC.getTblLocationByIdlocationDestination());
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

    private void setTableControlDataIC() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Incoming");
            buttonControl.setOnMouseClicked((e) -> {
                //listener create
                dataICCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataIC.addButtonControl(buttonControls);
    }

    private List<TblInComing> loadAllDataIC() {
        List<TblInComing> list = parentController.getFLaundryManager().getAllDataInComing();
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            //data location (source)
            list.get(i).setTblLocationByIdlocationSource(parentController.getFLaundryManager().getLocation(list.get(i).getTblLocationByIdlocationSource().getIdlocation()));
            //data location (destination)
            list.get(i).setTblLocationByIdlocationDestination(parentController.getFLaundryManager().getLocation(list.get(i).getTblLocationByIdlocationDestination().getIdlocation()));
            //data employee (receiver)
            list.get(i).setTblEmployeeByIdsender(parentController.getFLaundryManager().getDataEmployee(list.get(i).getTblEmployeeByIdsender().getIdemployee()));
            //data employee (create)
            list.get(i).setTblEmployeeByCreateBy(parentController.getFLaundryManager().getDataEmployee(list.get(i).getTblEmployeeByCreateBy().getIdemployee()));
            if (list.get(i).getTblLocationByIdlocationDestination().getRefLocationType().getIdtype() == 2) {    //Laundry = '2'
                if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                    if (list.get(i).getTblLocationByIdlocationDestination().getTblGroup() == null
                            || list.get(i).getTblLocationByIdlocationDestination().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                        list.remove(i);
                    }
                }
            }else{
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
    private GridPane gpFormDataIC;

    @FXML
    private ScrollPane spFormDataIC;

    @FXML
    private Label lblCodeData;

    @FXML
    private AnchorPane ancSentByLayout;
    private JFXCComboBoxTablePopup<TblEmployee> cbpSender;

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

    public TblInComing selectedData;

    private void initFormDataIC() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataIC.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataIC.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Incoming (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataICSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataICCancelHandle();
        });

        initDataPopup();

        cbpSourceLocation.setDisable(true);
        cbpSourceLocation.setVisible(false);

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpSender,
                cbpSourceLocation,
                cbpDestinationLocation,
                txtNote);
    }

    private void initDataPopup() {
        //Employee (Sent By)
        TableView<TblEmployee> tableSentBy = new TableView<>();

        TableColumn<TblEmployee, String> codeSentBy = new TableColumn<>("ID");
        codeSentBy.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        codeSentBy.setMinWidth(100);

        TableColumn<TblEmployee, String> sentByName = new TableColumn<>("Nama");
        sentByName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(),
                        param.getValue().tblPeopleProperty()));
        sentByName.setMinWidth(140);

        tableSentBy.getColumns().addAll(codeSentBy, sentByName);

        ObservableList<TblEmployee> sentByItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpSender = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableSentBy, sentByItems, "", "Pengirim *", true, 250, 200
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
                ClassLocation.class, tableLocationSource, sourceLocationItems, "", "Laundry *", true, 150, 200
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
                ClassLocation.class, tableLocationDestination, destinationLocationItems, "", "Laundry *", true, 150, 200
        );

        AnchorPane.setBottomAnchor(cbpSender, 0.0);
        AnchorPane.setLeftAnchor(cbpSender, 0.0);
//        AnchorPane.setRightAnchor(cbpSender, 0.0);
        AnchorPane.setTopAnchor(cbpSender, 0.0);
        ancSentByLayout.getChildren().add(cbpSender);

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

    private List<ClassLocation> loadAllDataDestinationClassLocation() {
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

    private List<ClassLocation> loadAllDataSourceClassLocation() {
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
        //Employee (Sent By)
        ObservableList<TblEmployee> sentByItems = FXCollections.observableArrayList(loadAllDataEmployee());
        cbpSender.setItems(sentByItems);

        //Class Location (Source)
        ObservableList<ClassLocation> sourceLocationItems = FXCollections.observableArrayList(loadAllDataSourceClassLocation());
        cbpSourceLocation.setItems(sourceLocationItems);

        //Class Location (Destination)
        ObservableList<ClassLocation> destinationLocationItems = FXCollections.observableArrayList(loadAllDataDestinationClassLocation());
        cbpDestinationLocation.setItems(destinationLocationItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeIc() != null
                ? selectedData.getCodeIc() : "");

        txtNote.textProperty().bindBidirectional(selectedData.icnoteProperty());

        cbpSender.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdsenderProperty());

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
            tableDataDetail.getTableView().setItems(loadAllDataICDIMH());
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
            tableDataDetail.getTableView().setItems(loadAllDataICDIMH());
        });

        cbpSender.hide();
        cbpSourceLocation.hide();
        cbpDestinationLocation.hide();

        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

//    private String getDataLocationLaundry(TblLocation tblLocation) {
//        if (tblLocation != null) {
//            return parentController.getFLaundryManager().getDataLaundryByIdLocation(tblLocation.getIdlocation()).getLaundryName();
//        }
//        return "-";
//    }
    private TblLocation getDataUsedLocation() {
        TblLocation usedLocation = parentController.getFLaundryManager().getLocationByIDLocationType(6);  //Digunakan = '6'
        if (usedLocation != null) {
            //location type
            usedLocation.setRefLocationType(parentController.getFLaundryManager().getDataLocationType(usedLocation.getRefLocationType().getIdtype()));
        }
        return usedLocation;
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataIC,
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

    public void dataICCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblInComing();
        selectedData.setTblLocationByIdlocationSource(getDataUsedLocation());
        setSelectedDataToInputForm();
        //open form data in coming
        dataICFormShowStatus.set(0.0);
        dataICFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataICShowHandle() {
        if (tableDataIC.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLaundryManager().getDataInComing(((TblInComing) tableDataIC.getTableView().getSelectionModel().getSelectedItem()).getIdic());
            selectedData.setTblEmployeeByIdsender(parentController.getFLaundryManager().getDataEmployee(selectedData.getTblEmployeeByIdsender().getIdemployee()));
            selectedData.setTblLocationByIdlocationSource(parentController.getFLaundryManager().getLocation(selectedData.getTblLocationByIdlocationSource().getIdlocation()));
            selectedData.setTblLocationByIdlocationDestination(parentController.getFLaundryManager().getLocation(selectedData.getTblLocationByIdlocationDestination().getIdlocation()));
            setSelectedDataToInputForm();
            dataICFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataICUnshowHandle() {
        refreshDataTableIC();
        dataICFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataICPrintHandle() {
        if (tableDataIC.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printIC(((TblInComing) tableDataIC.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printIC(TblInComing dataIC) {
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
//            ClassPrinter.printIC(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    dataSR);
//        } catch (Throwable ex) {
//            Logger.getLogger(LaundryStoreRequestController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void dataICSaveHandle() {
        if (checkDataInputDataIC()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblInComing dummySelectedData = new TblInComing(selectedData);
                List<ICDetailItemMutationHistory> dummyDataICDetailItemMutationHistories = new ArrayList<>();
                for (ICDetailItemMutationHistory icdp : (List<ICDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
                    TblInComingDetailItemMutationHistory dummyICIMH = new TblInComingDetailItemMutationHistory(icdp.getDataICDIMH());
                    dummyICIMH.setTblInComingDetail(new TblInComingDetail(dummyICIMH.getTblInComingDetail()));
                    dummyICIMH.getTblInComingDetail().setTblInComing(dummySelectedData);
                    dummyICIMH.getTblInComingDetail().setTblItem(new TblItem(dummyICIMH.getTblInComingDetail().getTblItem()));
                    dummyICIMH.setTblItemMutationHistory(new TblItemMutationHistory(dummyICIMH.getTblItemMutationHistory()));
                    dummyICIMH.getTblItemMutationHistory().setTblItem(new TblItem(dummyICIMH.getTblItemMutationHistory().getTblItem()));
                    dummyICIMH.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation(dummyICIMH.getTblItemMutationHistory().getTblLocationByIdlocationOfSource()));
                    dummyICIMH.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(new TblLocation(dummyICIMH.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()));
                    List<IMHPropertyBarcodeSelected> dummyIMHPBSs = new ArrayList<>();
                    for (IMHPropertyBarcodeSelected imhpbs : icdp.getDataIMHPBSs()) {
                        IMHPropertyBarcodeSelected dummyIMHPBS = new IMHPropertyBarcodeSelected(
                                new TblItemMutationHistoryPropertyBarcode(imhpbs.getDataItemMutationHistoryPropertyBarcode()),
                                imhpbs.isSelected());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblItemMutationHistory(dummyICIMH.getTblItemMutationHistory());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode()));
                        dummyIMHPBSs.add(dummyIMHPBS);
                    }
                    List<TblItemMutationHistoryItemExpiredDate> dummyIMHIEDs = new ArrayList<>();
                    for (TblItemMutationHistoryItemExpiredDate imhied : icdp.getDataIMHIEDs()) {
                        TblItemMutationHistoryItemExpiredDate dummyIMHIED = new TblItemMutationHistoryItemExpiredDate(imhied);
                        dummyIMHIED.setTblItemMutationHistory(dummyICIMH.getTblItemMutationHistory());
                        dummyIMHIED.setTblItemExpiredDate(new TblItemExpiredDate(dummyIMHIED.getTblItemExpiredDate()));
                        dummyIMHIEDs.add(dummyIMHIED);
                    }
                    ICDetailItemMutationHistory dummyDataICDetailItemMutationHistory = new ICDetailItemMutationHistory(
                            dummyICIMH,
                            dummyIMHPBSs,
                            dummyIMHIEDs);
                    dummyDataICDetailItemMutationHistories.add(dummyDataICDetailItemMutationHistory);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLaundryManager().insertDataInComing(
                                dummySelectedData,
                                dummyDataICDetailItemMutationHistories)
                                != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data in coming
                            refreshDataTableIC();
                            dataICFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                            //pribt data in coming
                            printIC(selectedData);
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

    private void dataICCancelHandle() {
        //refresh data from table & close form data in coming
        refreshDataTableIC();
        dataICFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableIC() {
        tableDataIC.getTableView().setItems(FXCollections.observableArrayList(loadAllDataIC()));
        cft.refreshFilterItems(tableDataIC.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataIC() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpSender.getValue() == null) {
            dataInput = false;
            errDataInput += "Pengirim : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpSourceLocation.getValue() == null) {
            dataInput = false;
            errDataInput += "Laundry : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (tableDataDetail.getTableView().getItems().isEmpty()) {
                dataInput = false;
                errDataInput += "Tabel (Detail-Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (checkDataInputInputDataICDetailInComingQuantityHaveNullValue()) {
                    dataInput = false;
                    errDataInput += "Minimal jumlah stok 'incoming' satu \n";
                } else {
                    if (!checkDataInputDataICDetailQuantityAvailable()) {
                        dataInput = false;
                        errDataInput += "Jumlah stok barang yang tersedia (sedang digunakan), \ntidak dapat memenuhi jumlah barang pada 'incoming' \n";
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

    private boolean checkDataInputInputDataICDetailInComingQuantityHaveNullValue() {
        for (ICDetailItemMutationHistory icdimh : (List<ICDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
            if (icdimh.getDataICDIMH().getTblInComingDetail().getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDataInputDataICDetailQuantityAvailable() {
        for (ICDetailItemMutationHistory icdimh : (List<ICDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
            if (icdimh.getDataICDIMH().getTblInComingDetail().getTblItem().getPropertyStatus()) { //property
                for (IMHPropertyBarcodeSelected imhpbs : icdimh.getDataIMHPBSs()) {
                    if (imhpbs.isSelected()) {
                        if ((new BigDecimal("1"))
                                .compareTo(getItemLocationAvailableStockPB(
                                                imhpbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode(),
                                                icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem(),
                                                icdimh.getDataICDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                }
            } else {
                if (icdimh.getDataICDIMH().getTblInComingDetail().getTblItem().getPropertyStatus()) { //consumable
                    for (TblItemMutationHistoryItemExpiredDate imhied : icdimh.getDataIMHIEDs()) {
                        if (imhied.getItemQuantity()
                                .compareTo(getItemLocationAvailableStockIED(
                                                imhied.getTblItemExpiredDate(),
                                                icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem(),
                                                icdimh.getDataICDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                } else {  //another
                    if (icdimh.getDataICDIMH().getTblItemMutationHistory().getItemQuantity()
                            .compareTo(getItemLocationAvailableStock(
                                            icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem(),
                                            icdimh.getDataICDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
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
        TableView<ICDetailItemMutationHistory> tableView = new TableView();
        tableView.setEditable(dataInputStatus != 3);

        TableColumn<ICDetailItemMutationHistory, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getCodeItem(),
                        param.getValue().dataICDIMHProperty()));
        codeItem.setMinWidth(120);

        TableColumn<ICDetailItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getItemName(),
                        param.getValue().dataICDIMHProperty()));
        itemName.setMinWidth(140);

        TableColumn<ICDetailItemMutationHistory, String> itemTypeHK = new TableColumn("House Keeping");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + (param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataICDIMHProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<ICDetailItemMutationHistory, String> itemTypeWH = new TableColumn("Warehouse");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataICDIMHProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<ICDetailItemMutationHistory, String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);

        TableColumn<ICDetailItemMutationHistory, String> icItemQuantity = new TableColumn("Jumlah");
        icItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataICDIMH().getTblItemMutationHistory().getItemQuantity()),
                        //                        param.getValue().dataICDIMHProperty()));
                        param.getValue().getDataICDIMH().getTblItemMutationHistory().itemQuantityProperty()));
        icItemQuantity.setMinWidth(100);
        Callback<TableColumn<ICDetailItemMutationHistory, String>, TableCell<ICDetailItemMutationHistory, String>> cellFactory
                = new Callback<TableColumn<ICDetailItemMutationHistory, String>, TableCell<ICDetailItemMutationHistory, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemQuantity();
                    }
                };
        icItemQuantity.setCellFactory(cellFactory);
        icItemQuantity.setEditable(true);

        TableColumn<ICDetailItemMutationHistory, String> detail = new TableColumn("Detail");
        detail.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> "",
                        param.getValue().dataICDIMHProperty()));
        detail.setMinWidth(100);
        Callback<TableColumn<ICDetailItemMutationHistory, String>, TableCell<ICDetailItemMutationHistory, String>> cellFactory1
                = new Callback<TableColumn<ICDetailItemMutationHistory, String>, TableCell<ICDetailItemMutationHistory, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellDetailStockIC();
                    }
                };
        detail.setCellFactory(cellFactory1);

        TableColumn<ICDetailItemMutationHistory, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataICDIMHProperty()));
        unitName.setMinWidth(120);

        TableColumn<ICDetailItemMutationHistory, String> itemStock = new TableColumn("Stok Tersedia\n (Digunakan)");
        itemStock.setCellValueFactory((TableColumn.CellDataFeatures<ICDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getCurrentAvailableStock(param.getValue().getDataICDIMH().getTblInComingDetail().getTblItem())),
                        param.getValue().dataICDIMHProperty()));
        itemStock.setMinWidth(120);

        if (dataInputStatus != 3) {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, icItemQuantity, detail, itemStock, unitName);
        } else {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, icItemQuantity, unitName);
        }
        tableView.setItems(loadAllDataICDIMH());
        tableDataDetail = new ClassTableWithControl(tableView);
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

    class EditingCellItemQuantity extends TableCell<ICDetailItemMutationHistory, String> {

        private JFXTextField tItemQuantity;

        public EditingCellItemQuantity() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null
                    && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getPropertyStatus() //!property
                    && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getConsumable()) {       //!consumable
                super.startEdit();
                tItemQuantity = new JFXTextField();
                tItemQuantity.setPromptText("Jumlah Barang");

                ClassViewSetting.setImportantField(
                        tItemQuantity);

                tItemQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", tItemQuantity);

                Bindings.bindBidirectional(tItemQuantity.textProperty(), ((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(tItemQuantity);
                tItemQuantity.selectAll();

                //cell input color
                if (this.getTableRow() != null
                        && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getPropertyStatus() //!property
                        && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getConsumable() //!consumable
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

            tItemQuantity.textProperty().unbindBidirectional(((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().itemQuantityProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null
                    && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getPropertyStatus() //!property
                    && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getConsumable() //!consumable
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
                            if (((ICDetailItemMutationHistory) data).getDataICDIMH() != null
                                    && ((ICDetailItemMutationHistory) data).getDataICDIMH().getTblInComingDetail() != null) {
                                setText(ClassFormatter.decimalFormat.cFormat(((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getItemQuantity()));
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
                                && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getPropertyStatus() //!property
                                && !((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail().getTblItem().getConsumable() //!consumable
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

    class EditingCellDetailStockIC extends TableCell<ICDetailItemMutationHistory, String> {

        private JFXCComboBoxPopup cbpStockICDetail;

        public EditingCellDetailStockIC() {

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
                            && (isProperty(((ICDetailItemMutationHistory) data).getDataICDIMH().getTblInComingDetail()) //property barcode
                            || isConsumable(((ICDetailItemMutationHistory) data).getDataICDIMH().getTblInComingDetail()))) {  //consumable
                        cbpStockICDetail = ((ICDetailItemMutationHistory) data).getCbpListDetail();

                        cbpStockICDetail.getStyleClass().add("detail-combo-box-popup");

                        cbpStockICDetail.hide();

                        cbpStockICDetail.setPrefSize(100, 25);
                        setAlignment(Pos.CENTER);

                        if (isProperty(((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail()) //property barcode
                                || isConsumable(((ICDetailItemMutationHistory) this.getTableRow().getItem()).getDataICDIMH().getTblInComingDetail())) { //consumable
                            cbpStockICDetail.setDisable(false);
                        } else {
                            cbpStockICDetail.setDisable(true);
                        }

                        setText(null);
                        setGraphic(cbpStockICDetail);

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

        private boolean isProperty(TblInComingDetail dataICDetail) {
            return dataICDetail != null
                    && dataICDetail.getTblItem() != null
                    && dataICDetail.getTblItem().getPropertyStatus();   //Property
        }

        private boolean isConsumable(TblInComingDetail dataICDetail) {
            return dataICDetail != null
                    && dataICDetail.getTblItem() != null
                    && dataICDetail.getTblItem().getConsumable();   //Consumable
        }

    }

    private JFXCComboBoxPopup getComboBoxDetails(TblInComingDetailItemMutationHistory icdimh) {

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

    private ObservableList<ICDetailItemMutationHistory> loadAllDataICDIMH() {
        List<ICDetailItemMutationHistory> list = new ArrayList<>();
        if (dataInputStatus == 3) {   //just for show data
            //data in coming - detail
            List<TblInComingDetail> icds = parentController.getFLaundryManager().getAllDataInComingDetailByIDInComing(selectedData.getIdic());
            for (TblInComingDetail icd : icds) {
                //set data item
                icd.setTblItem(parentController.getFLaundryManager().getDataItem(icd.getTblItem().getIditem()));
                //set data unit
                icd.getTblItem().setTblUnit(parentController.getFLaundryManager().getDataUnit(icd.getTblItem().getTblUnit().getIdunit()));
                //set data item type (hk)
                if (icd.getTblItem().getTblItemTypeHk() != null) {
                    icd.getTblItem().setTblItemTypeHk(parentController.getFLaundryManager().getDataItemTypeHK(icd.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type (wh)
                if (icd.getTblItem().getTblItemTypeWh() != null) {
                    icd.getTblItem().setTblItemTypeWh(parentController.getFLaundryManager().getDataItemTypeWH(icd.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                //data ic detail - imh
                TblInComingDetailItemMutationHistory icdimh = parentController.getFLaundryManager().getDataInComingDetailItemMutationHistoryByIDInComingDetail(icd.getIddetail());
                icdimh.setTblInComingDetail(icd);
                icdimh.setTblItemMutationHistory(parentController.getFLaundryManager().getDataItemMutationHistory(icdimh.getTblItemMutationHistory().getIdmutation()));
                //data imh - property (s)
                List<TblItemMutationHistoryPropertyBarcode> imhpbs = parentController.getFLaundryManager().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(icdimh.getTblItemMutationHistory().getIdmutation());
                List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                    imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, true));
                }
                //data imh - item expired date (s)
                List<TblItemMutationHistoryItemExpiredDate> imhieds = parentController.getFLaundryManager().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(icdimh.getTblItemMutationHistory().getIdmutation());
                //data 'ICDetailItemMutationHistory'
                ICDetailItemMutationHistory data = new ICDetailItemMutationHistory(icdimh,
                        imhpbss,
                        imhieds);
                list.add(data);
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

    public class ICDetailItemMutationHistory {

        private final ObjectProperty<TblInComingDetailItemMutationHistory> dataICDIMH = new SimpleObjectProperty<>();

        private List<IMHPropertyBarcodeSelected> dataIMHPBSs = new ArrayList<>();

        private List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = new ArrayList<>();

        private final ObjectProperty<JFXCComboBoxPopup> cbpListDetail = new SimpleObjectProperty<>();

        public ICDetailItemMutationHistory(
                TblInComingDetailItemMutationHistory dataICDIMH,
                List<IMHPropertyBarcodeSelected> dataIMHPBSs,
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs) {
            //data icd - imh
            this.dataICDIMH.set(dataICDIMH);

            //data imh - pb (s)
            for (IMHPropertyBarcodeSelected dataIMHPBS : dataIMHPBSs) {
                this.dataIMHPBSs.add(dataIMHPBS);
            }

            //data imh - ied (s)
            for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                this.dataIMHIEDs.add(dataIMHIED);
            }

            //data cbListDetail
            if (this.dataICDIMH.get() == null
                    || this.dataICDIMH.get().getTblInComingDetail() == null
                    || this.dataICDIMH.get().getTblInComingDetail().getTblItem() == null) {
                //data cb-list combobox
                this.cbpListDetail.set(getComboBoxDetails(this.dataICDIMH.get()));
            } else {
                if (this.dataICDIMH.get().getTblInComingDetail().getTblItem().getPropertyStatus()) {   //Property
                    //data cb-list combobox
                    this.cbpListDetail.set(getComboBoxPropertyDetails(this.dataIMHPBSs));
                } else {
                    if (this.dataICDIMH.get().getTblInComingDetail().getTblItem().getConsumable()) {    //cosumable
                        for (TblItemMutationHistoryItemExpiredDate dataIMHIED : this.dataIMHIEDs) {
                            dataIMHIED.itemQuantityProperty().addListener((obs, oldVale, newVal) -> {
                                this.dataICDIMH.get().getTblItemMutationHistory().setItemQuantity(calculationTotalItemExpiredDateIC(
                                        this.dataIMHIEDs
                                ));
                            });
                        }
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.dataIMHIEDs));
                    } else {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails(this.dataICDIMH.get()));
                    }
                }
            }
        }

        private BigDecimal calculationTotalItemExpiredDateIC(List<TblItemMutationHistoryItemExpiredDate> dataIMHPBSs) {
            BigDecimal result = new BigDecimal("0");
            for (TblItemMutationHistoryItemExpiredDate dataIMHPBS : dataIMHPBSs) {
                if (dataIMHPBS.getItemQuantity() != null) {
                    result = result.add(dataIMHPBS.getItemQuantity());
                }
            }
            return result;
        }

        public ObjectProperty<TblInComingDetailItemMutationHistory> dataICDIMHProperty() {
            return dataICDIMH;
        }

        public TblInComingDetailItemMutationHistory getDataICDIMH() {
            return dataICDIMHProperty().get();
        }

        public void setDataICDIMH(TblInComingDetailItemMutationHistory dataICDIMH) {
            dataICDIMHProperty().set(dataICDIMH);
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

    public ICDetailItemMutationHistory selectedDataDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailCreateHandle() {
        if (selectedData.getTblLocationByIdlocationSource() != null
                && selectedData.getTblLocationByIdlocationDestination() != null) {
            dataInputDetailStatus = 0;
            selectedDataDetail = new ICDetailItemMutationHistory(null, new ArrayList<>(), new ArrayList<>());
            //open form data - detail
            showDataDetailDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA", "Pilih data laundry terlebih dahulu..!", null);
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

    public ICDetailItemMutationHistory generateDataICDetailItemMutationHistory(TblItem dataItem) {
        if (selectedData != null
                && selectedData.getTblLocationByIdlocationSource() != null
                && selectedData.getTblLocationByIdlocationDestination() != null
                && dataItem != null) {
            //set data in coming detail
            TblInComingDetail icd = new TblInComingDetail();
            icd.setTblInComing(selectedData);
            //set data item
            icd.setTblItem(parentController.getFLaundryManager().getDataItem(dataItem.getIditem()));
            //set data unit
            icd.getTblItem().setTblUnit(parentController.getFLaundryManager().getDataUnit(dataItem.getTblUnit().getIdunit()));
            //set data item type (hk)
            if (dataItem.getTblItemTypeHk() != null) {
                icd.getTblItem().setTblItemTypeHk(parentController.getFLaundryManager().getDataItemTypeHK(dataItem.getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type (wh)
            if (dataItem.getTblItemTypeWh() != null) {
                icd.getTblItem().setTblItemTypeWh(parentController.getFLaundryManager().getDataItemTypeWH(dataItem.getTblItemTypeWh().getIditemTypeWh()));
            }
            //item quantity
            icd.setItemQuantity(new BigDecimal("0"));
            //data icd - imh
            if (icd.getTblItem().getPropertyStatus()) {   //property
                //data ic detail - imh
                TblInComingDetailItemMutationHistory icdimh = new TblInComingDetailItemMutationHistory();
                icdimh.setTblInComingDetail(icd);
                icdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                icdimh.getTblItemMutationHistory().setTblItem(icd.getTblItem());
                icdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                icdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                icdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                icdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(icd.itemQuantityProperty());
                //data imh - property (s)
                List<TblItemMutationHistoryPropertyBarcode> imhpbs = new ArrayList<>();
                TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDLocationAndIDItem(
                        selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                        icd.getTblItem().getIditem()
                );
                List<TblItemLocationPropertyBarcode> ilpbs = parentController.getFLaundryManager().getAllDataItemLocationPropertyBarcodeByIDItemLocation(dataItemLocation.getIdrelation());
                for (TblItemLocationPropertyBarcode ilpb : ilpbs) {
                    TblItemMutationHistoryPropertyBarcode imhpb = new TblItemMutationHistoryPropertyBarcode();
                    imhpb.setTblItemMutationHistory(icdimh.getTblItemMutationHistory());
                    imhpb.setTblPropertyBarcode(ilpb.getTblPropertyBarcode());
                    imhpbs.add(imhpb);
                }
                List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                    imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, false));
                }
                //data 'ICDetailItemMutationHistory'
                ICDetailItemMutationHistory data = new ICDetailItemMutationHistory(icdimh,
                        imhpbss,
                        new ArrayList<>());
                return data;
            } else {
                if (icd.getTblItem().getConsumable()) {   //consumable
                    //data ic detail - imh
                    TblInComingDetailItemMutationHistory icdimh = new TblInComingDetailItemMutationHistory();
                    icdimh.setTblInComingDetail(icd);
                    icdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                    icdimh.getTblItemMutationHistory().setTblItem(icd.getTblItem());
                    icdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                    icdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                    icdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    icdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(icd.itemQuantityProperty());
                    //data imh - item expired date (s)
                    List<TblItemMutationHistoryItemExpiredDate> imhieds = new ArrayList<>();
                    TblItemLocation dataItemLocation = parentController.getFLaundryManager().getDataItemLocationByIDLocationAndIDItem(
                            selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                            icd.getTblItem().getIditem()
                    );
                    List<TblItemLocationItemExpiredDate> ilieds = parentController.getFLaundryManager().getAllDataItemLocationItemExpiredDateByIDItemLocation(dataItemLocation.getIdrelation());
                    for (TblItemLocationItemExpiredDate ilied : ilieds) {
                        TblItemMutationHistoryItemExpiredDate imhied = new TblItemMutationHistoryItemExpiredDate();
                        imhied.setTblItemMutationHistory(icdimh.getTblItemMutationHistory());
                        imhied.setTblItemExpiredDate(ilied.getTblItemExpiredDate());
                        imhied.setItemQuantity(new BigDecimal("0"));
                        imhieds.add(imhied);
                    }
                    //data 'ICDetailItemMutationHistory'
                    ICDetailItemMutationHistory data = new ICDetailItemMutationHistory(icdimh,
                            new ArrayList<>(),
                            imhieds);
                    return data;
                } else {  //another
                    //data ic detail - imh
                    TblInComingDetailItemMutationHistory icdimh = new TblInComingDetailItemMutationHistory();
                    icdimh.setTblInComingDetail(icd);
                    icdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                    icdimh.getTblItemMutationHistory().setTblItem(icd.getTblItem());
                    icdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                    icdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                    icdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    icdimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(icd.itemQuantityProperty());
                    //data 'ICDetailItemMutationHistory'
                    ICDetailItemMutationHistory data = new ICDetailItemMutationHistory(icdimh,
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
            loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry_in_coming/LaundryInComingDetailDialog.fxml"));

            LaundryInComingDetailController controller = new LaundryInComingDetailController(this);
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
        setDataICSplitpane();

        //init table
        initTableDataIC();

        //init form
        initFormDataIC();

        spDataIC.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataICFormShowStatus.set(0.0);
        });
    }

    public LaundryInComingController(FeatureLaundryController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLaundryController parentController;

    public FLaundryManager getService() {
        return parentController.getFLaundryManager();
    }

}
