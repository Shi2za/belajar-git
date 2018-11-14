/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_broken_item;

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
import hotelfx.persistence.model.TblBrokenItem;
import hotelfx.persistence.model.TblBrokenItemDetail;
import hotelfx.persistence.model.TblBrokenItemDetailItemMutationHistory;
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
import hotelfx.persistence.service.FBrokenItemManager;
import hotelfx.persistence.service.FBrokenItemManagerImpl;
import hotelfx.view.DashboardController;
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
public class BrokenItemController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataBrokenItem;

    private DoubleProperty dataBrokenItemFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataBrokenItemLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataBrokenItemSplitpane() {
        spDataBrokenItem.setDividerPositions(1);

        dataBrokenItemFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataBrokenItemFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataBrokenItem.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataBrokenItem.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataBrokenItemFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataBrokenItemLayout.setDisable(false);
                    tableDataBrokenItemLayoutDisableLayer.setDisable(true);
                    tableDataBrokenItemLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataBrokenItemLayout.setDisable(true);
                    tableDataBrokenItemLayoutDisableLayer.setDisable(false);
                    tableDataBrokenItemLayoutDisableLayer.toFront();
                }
            }
        });

        dataBrokenItemFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataBrokenItemLayout;

    private ClassFilteringTable<TblBrokenItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataBrokenItem;

    private void initTableDataBrokenItem() {
        //set table
        setTableDataBrokenItem();
        //set control
        setTableControlDataBrokenItem();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataBrokenItem, 15.0);
        AnchorPane.setLeftAnchor(tableDataBrokenItem, 15.0);
        AnchorPane.setRightAnchor(tableDataBrokenItem, 15.0);
        AnchorPane.setTopAnchor(tableDataBrokenItem, 15.0);
        ancBodyLayout.getChildren().add(tableDataBrokenItem);
    }

    private void setTableDataBrokenItem() {
        TableView<TblBrokenItem> tableView = new TableView();

        TableColumn<TblBrokenItem, String> brokenItemDate = new TableColumn<>("Tgl. Rusak");
        brokenItemDate.setCellValueFactory((TableColumn.CellDataFeatures<TblBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getBidate()),
                        param.getValue().bidateProperty()));
        brokenItemDate.setMinWidth(140);

        TableColumn<TblBrokenItem, String> codeBrokenItem = new TableColumn("No. Rusak");
        codeBrokenItem.setCellValueFactory(cellData -> cellData.getValue().codeBiProperty());
        codeBrokenItem.setMinWidth(120);

        TableColumn<TblBrokenItem, String> createdBy = new TableColumn<>("Dibuat Oleh");
        createdBy.setCellValueFactory((TableColumn.CellDataFeatures<TblBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblEmployeeByCreateBy() != null)
                                ? param.getValue().getTblEmployeeByCreateBy().getTblPeople().getFullName()
                                : "-",
                        param.getValue().tblEmployeeByCreateByProperty()));
        createdBy.setMinWidth(140);

        TableColumn<TblBrokenItem, String> sourceLocation = new TableColumn<>("Tempat Ditemukan");  //Dari
        sourceLocation.setCellValueFactory((TableColumn.CellDataFeatures<TblBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> getSourceLocationName(param.getValue()),
                        param.getValue().tblLocationByIdlocationSourceProperty()));
        sourceLocation.setMinWidth(160);

        TableColumn<TblBrokenItem, String> destinationLocation = new TableColumn<>("Gudang"); //Ke
        destinationLocation.setCellValueFactory((TableColumn.CellDataFeatures<TblBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> getDestinationLocationName(param.getValue()),
                        param.getValue().tblLocationByIdlocationDestinationProperty()));
        destinationLocation.setMinWidth(160);

        TableColumn<TblBrokenItem, String> locationTitle = new TableColumn("Gudang");
        locationTitle.getColumns().addAll(sourceLocation, destinationLocation);

        tableView.getColumns().addAll(brokenItemDate, codeBrokenItem, createdBy, sourceLocation);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataBrokenItem()));

        tableView.setRowFactory(tv -> {
            TableRow<TblBrokenItem> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataBrokenItemUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataBrokenItemShowHandle();
                        }
                    }
                }
            });
            return row;
        });

        tableDataBrokenItem = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblBrokenItem.class,
                tableDataBrokenItem.getTableView(),
                tableDataBrokenItem.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getSourceLocationName(TblBrokenItem dataBrokenItem) {
        if (dataBrokenItem != null) {
            return getNameLocation(dataBrokenItem.getTblLocationByIdlocationSource());
        }
        return "-";
    }

    private String getDestinationLocationName(TblBrokenItem dataBrokenItem) {
        if (dataBrokenItem != null) {
            return getNameLocation(dataBrokenItem.getTblLocationByIdlocationDestination());
        }
        return "-";
    }

    public String getNameLocation(TblLocation dataLocation) {
        if (dataLocation != null
                && dataLocation.getRefLocationType() != null) {
            switch (dataLocation.getRefLocationType().getIdtype()) {
                case 0: //Room
                    TblRoom room = getService().getDataRoomByIDLocation(dataLocation.getIdlocation());
                    return room != null ? room.getRoomName() : "-";
                case 1: //Warehouse
                    TblLocationOfWarehouse warehouse = getService().getDataWarehouseByIdLocation(dataLocation.getIdlocation());
                    return warehouse != null ? warehouse.getWarehouseName() : "-";
                case 2: //Laundry
                    TblLocationOfLaundry laundry = getService().getDataLaundryByIDLocation(dataLocation.getIdlocation());
                    return laundry != null ? laundry.getLaundryName() : "-";
            }
        }
        return "-";
    }

    private void setTableControlDataBrokenItem() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah (Barang Rusak)");
            buttonControl.setOnMouseClicked((e) -> {
                //listener create
                dataBrokenItemCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataBrokenItem.addButtonControl(buttonControls);
    }

    private List<TblBrokenItem> loadAllDataBrokenItem() {
        List<TblBrokenItem> list = getService().getAllDataBrokenItem();
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            //data location (source)
            list.get(i).setTblLocationByIdlocationSource(getService().getDataLocation(list.get(i).getTblLocationByIdlocationSource().getIdlocation()));
            //data location (destination)
            list.get(i).setTblLocationByIdlocationDestination(getService().getDataLocation(list.get(i).getTblLocationByIdlocationDestination().getIdlocation()));
            //data employee (create)
            list.get(i).setTblEmployeeByCreateBy(getService().getDataEmployee(list.get(i).getTblEmployeeByCreateBy().getIdemployee()));
            if (list.get(i).getTblLocationByIdlocationDestination().getRefLocationType().getIdtype() == 1) {    //Gudang = '1'
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
    private GridPane gpFormDataBrokenItem;

    @FXML
    private ScrollPane spFormDataBrokenItem;

    @FXML
    private Label lblCodeData;

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

    public TblBrokenItem selectedData;

    private void initFormDataBrokenItem() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataBrokenItem.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataBrokenItem.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Barang Rusak)"));
        btnSave.setOnAction((e) -> {
            dataBrokenItemSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataBrokenItemCancelHandle();
        });

        initDataPopup();

        cbpDestinationLocation.setDisable(true);
        cbpDestinationLocation.setVisible(false);

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(                
                cbpSourceLocation,
                cbpDestinationLocation,
                txtNote);
    }

    private void initDataPopup() {
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
                ClassLocation.class, tableLocationSource, sourceLocationItems, "", "Tempat Ditemukan *", true, 150, 200
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
                ClassLocation.class, tableLocationDestination, destinationLocationItems, "", "Bin *", true, 150, 200
        );

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

    private List<ClassLocation> loadAllDataDestinationClassLocation() {
        List<ClassLocation> list = new ArrayList<>();
        List<TblLocation> locations = getService().getAllDataLocationByIDLocationType(1); //Gudang = '1'
        for (TblLocation location : locations) {
            //data group
            location.setTblGroup(getService().getDataGroup(location.getTblGroup().getIdgroup()));
            //data location type
            location.setRefLocationType(getService().getDataLocationType((int) location.getRefLocationType().getIdtype()));
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
        List<TblLocation> locations = getService().getAllDataLocationByIDLocationType(1); //Gudang = '1'
        for (TblLocation location : locations) {
            //data group
            location.setTblGroup(getService().getDataGroup(location.getTblGroup().getIdgroup()));
            //data location type
            location.setRefLocationType(getService().getDataLocationType(location.getRefLocationType().getIdtype()));
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
        //Class Location (Source)
        ObservableList<ClassLocation> sourceLocationItems = FXCollections.observableArrayList(loadAllDataSourceClassLocation());
        cbpSourceLocation.setItems(sourceLocationItems);

        //Class Location (Destination)
        ObservableList<ClassLocation> destinationLocationItems = FXCollections.observableArrayList(loadAllDataDestinationClassLocation());
        cbpDestinationLocation.setItems(destinationLocationItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeBi() != null
                ? selectedData.getCodeBi() : "");

        txtNote.textProperty().bindBidirectional(selectedData.binoteProperty());

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
            tableDataDetail.getTableView().setItems(loadAllDataBIDIMH());
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
            tableDataDetail.getTableView().setItems(loadAllDataBIDIMH());
        });

        cbpSourceLocation.hide();
        cbpDestinationLocation.hide();

        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private String getDataLocationBin(TblLocation tblLocation) {
        if (tblLocation != null) {
            return getService().getDataBinByIdLocation(tblLocation.getIdlocation()).getBinName();
        }
        return "-";
    }

    private TblLocation getDataUsedLocation() {
        TblLocation usedLocation = getService().getDataLocationByIDLocationType(6);  //Digunakan = '6'
        if (usedLocation != null) {
            //location type
            usedLocation.setRefLocationType(getService().getDataLocationType(usedLocation.getRefLocationType().getIdtype()));
        }
        return usedLocation;
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataBrokenItem,
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

    public void dataBrokenItemCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblBrokenItem();
        selectedData.setTblLocationByIdlocationSource(getDataUsedLocation());
        setSelectedDataToInputForm();
        //open form data broken item
        dataBrokenItemFormShowStatus.set(0.0);
        dataBrokenItemFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataBrokenItemShowHandle() {
        if (tableDataBrokenItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getDataBrokenItem(((TblBrokenItem) tableDataBrokenItem.getTableView().getSelectionModel().getSelectedItem()).getIdbi());
            selectedData.setTblLocationByIdlocationSource(getService().getDataLocation(selectedData.getTblLocationByIdlocationSource().getIdlocation()));
            selectedData.setTblLocationByIdlocationDestination(getService().getDataLocation(selectedData.getTblLocationByIdlocationDestination().getIdlocation()));
            setSelectedDataToInputForm();
            dataBrokenItemFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataBrokenItemUnshowHandle() {
        refreshDataTableBrokenItem();
        dataBrokenItemFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataBrokenItemPrintHandle() {
        if (tableDataBrokenItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printBrokenItem(((TblBrokenItem) tableDataBrokenItem.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printBrokenItem(TblBrokenItem dataBrokenItem) {
        String hotelName = "";
        SysDataHardCode sdhHotelName = getService().getDataSysDataHardCode((long) 12);  //HotelName = '12'
        if (sdhHotelName != null
                && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }
        String hotelAddress = "";
        SysDataHardCode sdhHotelAddress = getService().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
        if (sdhHotelAddress != null
                && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }
        String hotelPhoneNumber = "";
        SysDataHardCode sdhHotelPhoneNumber = getService().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
        if (sdhHotelPhoneNumber != null
                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
        }
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = getService().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        try {
//            ClassPrinter.printBrokenItem(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    dataBrokenItem);
//        } catch (Throwable ex) {
//            Logger.getLogger(BrokenItemController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void dataBrokenItemSaveHandle() {
        if (checkDataInputDataBrokenItem()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblBrokenItem dummySelectedData = new TblBrokenItem(selectedData);
                List<BIDetailItemMutationHistory> dummyDataBIDetailItemMutationHistories = new ArrayList<>();
                for (BIDetailItemMutationHistory bidp : (List<BIDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
                    TblBrokenItemDetailItemMutationHistory dummyBIIMH = new TblBrokenItemDetailItemMutationHistory(bidp.getDataBIDIMH());
                    dummyBIIMH.setTblBrokenItemDetail(new TblBrokenItemDetail(dummyBIIMH.getTblBrokenItemDetail()));
                    dummyBIIMH.getTblBrokenItemDetail().setTblBrokenItem(dummySelectedData);
                    dummyBIIMH.getTblBrokenItemDetail().setTblItem(new TblItem(dummyBIIMH.getTblBrokenItemDetail().getTblItem()));
                    dummyBIIMH.setTblItemMutationHistory(new TblItemMutationHistory(dummyBIIMH.getTblItemMutationHistory()));
                    dummyBIIMH.getTblItemMutationHistory().setTblItem(new TblItem(dummyBIIMH.getTblItemMutationHistory().getTblItem()));
                    dummyBIIMH.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation(dummyBIIMH.getTblItemMutationHistory().getTblLocationByIdlocationOfSource()));
                    dummyBIIMH.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(new TblLocation(dummyBIIMH.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()));
                    List<IMHPropertyBarcodeSelected> dummyIMHPBSs = new ArrayList<>();
                    for (IMHPropertyBarcodeSelected imhpbs : bidp.getDataIMHPBSs()) {
                        IMHPropertyBarcodeSelected dummyIMHPBS = new IMHPropertyBarcodeSelected(
                                new TblItemMutationHistoryPropertyBarcode(imhpbs.getDataItemMutationHistoryPropertyBarcode()),
                                imhpbs.isSelected());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblItemMutationHistory(dummyBIIMH.getTblItemMutationHistory());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode()));
                        dummyIMHPBSs.add(dummyIMHPBS);
                    }
                    List<TblItemMutationHistoryItemExpiredDate> dummyIMHIEDs = new ArrayList<>();
                    for (TblItemMutationHistoryItemExpiredDate imhied : bidp.getDataIMHIEDs()) {
                        TblItemMutationHistoryItemExpiredDate dummyIMHIED = new TblItemMutationHistoryItemExpiredDate(imhied);
                        dummyIMHIED.setTblItemMutationHistory(dummyBIIMH.getTblItemMutationHistory());
                        dummyIMHIED.setTblItemExpiredDate(new TblItemExpiredDate(dummyIMHIED.getTblItemExpiredDate()));
                        dummyIMHIEDs.add(dummyIMHIED);
                    }
                    BIDetailItemMutationHistory dummyDataBIDetailItemMutationHistory = new BIDetailItemMutationHistory(
                            dummyBIIMH,
                            dummyIMHPBSs,
                            dummyIMHIEDs);
                    dummyDataBIDetailItemMutationHistories.add(dummyDataBIDetailItemMutationHistory);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (getService().insertDataBrokenItem(
                                dummySelectedData,
                                dummyDataBIDetailItemMutationHistories)
                                != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data broken item
                            refreshDataTableBrokenItem();
                            dataBrokenItemFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                            //pribt data broken item
                            printBrokenItem(selectedData);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
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

    private void dataBrokenItemCancelHandle() {
        //refresh data from table & close form data broken item
        refreshDataTableBrokenItem();
        dataBrokenItemFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableBrokenItem() {
        tableDataBrokenItem.getTableView().setItems(FXCollections.observableArrayList(loadAllDataBrokenItem()));
        cft.refreshFilterItems(tableDataBrokenItem.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataBrokenItem() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpSourceLocation.getValue() == null) {
            dataInput = false;
            errDataInput += "Tempat Ditemukan  : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (tableDataDetail.getTableView().getItems().isEmpty()) {
                dataInput = false;
                errDataInput += "Tabel (Detail-Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (checkDataInputInputDataBrokenItemDetailBrokenItemQuantityHaveNullValue()) {
                    dataInput = false;
                    errDataInput += "Minimal jumlah stok 'rusak' satu \n";
                } else {
                    if (!checkDataInputDataBrokenItemDetailQuantityAvailable()) {
                        dataInput = false;
                        errDataInput += "Jumlah stok barang yang tersedia (yang tesedia), \ntidak dapat memenuhi jumlah barang rusak \n";
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

    private boolean checkDataInputInputDataBrokenItemDetailBrokenItemQuantityHaveNullValue() {
        for (BIDetailItemMutationHistory bidimh : (List<BIDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
            if (bidimh.getDataBIDIMH().getTblBrokenItemDetail().getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDataInputDataBrokenItemDetailQuantityAvailable() {
        for (BIDetailItemMutationHistory bidimh : (List<BIDetailItemMutationHistory>) tableDataDetail.getTableView().getItems()) {
            if (bidimh.getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getPropertyStatus()) { //property
                for (IMHPropertyBarcodeSelected imhpbs : bidimh.getDataIMHPBSs()) {
                    if (imhpbs.isSelected()) {
                        if ((new BigDecimal("1"))
                                .compareTo(getItemLocationAvailableStockPB(
                                                imhpbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode(),
                                                bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem(),
                                                bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                }
            } else {
                if (bidimh.getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getPropertyStatus()) { //consumable
                    for (TblItemMutationHistoryItemExpiredDate imhied : bidimh.getDataIMHIEDs()) {
                        if (imhied.getItemQuantity()
                                .compareTo(getItemLocationAvailableStockIED(
                                                imhied.getTblItemExpiredDate(),
                                                bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem(),
                                                bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                } else {  //another
                    if (bidimh.getDataBIDIMH().getTblItemMutationHistory().getItemQuantity()
                            .compareTo(getItemLocationAvailableStock(
                                            bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem(),
                                            bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
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
            TblItemLocation dataItemLocation = getService().getDataItemLocationByIDItemAndIDLocation(
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
            TblItemLocation dataItemLocation = getService().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = getService().getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
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
            TblItemLocation dataItemLocation = getService().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getService().getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
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
        TableView<BIDetailItemMutationHistory> tableView = new TableView();
        tableView.setEditable(dataInputStatus != 3);

        TableColumn<BIDetailItemMutationHistory, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getCodeItem(),
                        param.getValue().dataBIDIMHProperty()));
        codeItem.setMinWidth(120);

        TableColumn<BIDetailItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getItemName(),
                        param.getValue().dataBIDIMHProperty()));
        itemName.setMinWidth(140);

        TableColumn<BIDetailItemMutationHistory, String> itemTypeHK = new TableColumn("House Keeping");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + (param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataBIDIMHProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<BIDetailItemMutationHistory, String> itemTypeWH = new TableColumn("Warehouse");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataBIDIMHProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<BIDetailItemMutationHistory, String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);

        TableColumn<BIDetailItemMutationHistory, String> biItemQuantity = new TableColumn("Jumlah");
        biItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataBIDIMH().getTblItemMutationHistory().getItemQuantity()),
                        //                        param.getValue().dataBIDIMHProperty()));
                        param.getValue().getDataBIDIMH().getTblItemMutationHistory().itemQuantityProperty()));
        biItemQuantity.setMinWidth(100);
        Callback<TableColumn<BIDetailItemMutationHistory, String>, TableCell<BIDetailItemMutationHistory, String>> cellFactory
                = new Callback<TableColumn<BIDetailItemMutationHistory, String>, TableCell<BIDetailItemMutationHistory, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemQuantity();
                    }
                };
        biItemQuantity.setCellFactory(cellFactory);
        biItemQuantity.setEditable(true);

        TableColumn<BIDetailItemMutationHistory, String> detail = new TableColumn("Detail");
        detail.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> "",
                        param.getValue().dataBIDIMHProperty()));
        detail.setMinWidth(100);
        Callback<TableColumn<BIDetailItemMutationHistory, String>, TableCell<BIDetailItemMutationHistory, String>> cellFactory1
                = new Callback<TableColumn<BIDetailItemMutationHistory, String>, TableCell<BIDetailItemMutationHistory, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellDetailStockBI();
                    }
                };
        detail.setCellFactory(cellFactory1);

        TableColumn<BIDetailItemMutationHistory, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataBIDIMHProperty()));
        unitName.setMinWidth(120);

        TableColumn<BIDetailItemMutationHistory, String> itemStock = new TableColumn("Stok Tersedia\n (Digunakan)");
        itemStock.setCellValueFactory((TableColumn.CellDataFeatures<BIDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getCurrentAvailableStock(param.getValue().getDataBIDIMH().getTblBrokenItemDetail().getTblItem())),
                        param.getValue().dataBIDIMHProperty()));
        itemStock.setMinWidth(120);

        if (dataInputStatus != 3) {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, biItemQuantity, detail, itemStock, unitName);
        } else {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, biItemQuantity, unitName);
        }
        tableView.setItems(loadAllDataBIDIMH());
        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private BigDecimal getCurrentAvailableStock(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && selectedData != null
                && selectedData.getTblLocationByIdlocationSource() != null) {
            TblItemLocation dataItemLocation = getService().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    selectedData.getTblLocationByIdlocationSource().getIdlocation());
            if (dataItemLocation != null) {
                result = dataItemLocation.getItemQuantity();
            }
        }
        return result;
    }

    class EditingCellItemQuantity extends TableCell<BIDetailItemMutationHistory, String> {

        private JFXTextField tItemQuantity;

        public EditingCellItemQuantity() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null
                    && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getPropertyStatus() //!property
                    && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getConsumable()) {       //!consumable
                super.startEdit();
                tItemQuantity = new JFXTextField();
                tItemQuantity.setPromptText("Jumlah Barang");

                ClassViewSetting.setImportantField(
                        tItemQuantity);

                tItemQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", tItemQuantity);

                Bindings.bindBidirectional(tItemQuantity.textProperty(), ((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(tItemQuantity);
                tItemQuantity.selectAll();

                //cell input color
                if (this.getTableRow() != null
                        && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getPropertyStatus() //!property
                        && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getConsumable() //!consumable
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

            tItemQuantity.textProperty().unbindBidirectional(((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().itemQuantityProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null
                    && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getPropertyStatus() //!property
                    && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getConsumable() //!consumable
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
                            if (((BIDetailItemMutationHistory) data).getDataBIDIMH() != null
                                    && ((BIDetailItemMutationHistory) data).getDataBIDIMH().getTblBrokenItemDetail() != null) {
                                setText(ClassFormatter.decimalFormat.cFormat(((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getItemQuantity()));
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
                                && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getPropertyStatus() //!property
                                && !((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail().getTblItem().getConsumable() //!consumable
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

    class EditingCellDetailStockBI extends TableCell<BIDetailItemMutationHistory, String> {

        private JFXCComboBoxPopup cbpStockBIDetail;

        public EditingCellDetailStockBI() {

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
                            && (isProperty(((BIDetailItemMutationHistory) data).getDataBIDIMH().getTblBrokenItemDetail()) //property barcode
                            || isConsumable(((BIDetailItemMutationHistory) data).getDataBIDIMH().getTblBrokenItemDetail()))) {  //consumable
                        cbpStockBIDetail = ((BIDetailItemMutationHistory) data).getCbpListDetail();

                        cbpStockBIDetail.getStyleClass().add("detail-combo-box-popup");

                        cbpStockBIDetail.hide();

                        cbpStockBIDetail.setPrefSize(100, 25);
                        setAlignment(Pos.CENTER);

                        if (isProperty(((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail()) //property barcode
                                || isConsumable(((BIDetailItemMutationHistory) this.getTableRow().getItem()).getDataBIDIMH().getTblBrokenItemDetail())) { //consumable
                            cbpStockBIDetail.setDisable(false);
                        } else {
                            cbpStockBIDetail.setDisable(true);
                        }

                        setText(null);
                        setGraphic(cbpStockBIDetail);

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

        private boolean isProperty(TblBrokenItemDetail dataBIDetail) {
            return dataBIDetail != null
                    && dataBIDetail.getTblItem() != null
                    && dataBIDetail.getTblItem().getPropertyStatus();   //Property
        }

        private boolean isConsumable(TblBrokenItemDetail dataBIDetail) {
            return dataBIDetail != null
                    && dataBIDetail.getTblItem() != null
                    && dataBIDetail.getTblItem().getConsumable();   //Consumable
        }

    }

    private JFXCComboBoxPopup getComboBoxDetails(TblBrokenItemDetailItemMutationHistory bidimh) {

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
            TblItemLocation dataItemLocation = getService().getDataItemLocationByIDItemAndIDLocation(
                    dataItemExpiredDate.getTblItem().getIditem(),
                    selectedData.getTblLocationByIdlocationSource().getIdlocation());
            if (dataItemLocation != null) {
                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getService().getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
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

    private ObservableList<BIDetailItemMutationHistory> loadAllDataBIDIMH() {
        List<BIDetailItemMutationHistory> list = new ArrayList<>();
        if (dataInputStatus == 3) {   //just for show data
            //data broken item - detail
            List<TblBrokenItemDetail> bids = getService().getAllDataBrokenItemDetailByIDBrokenItem(selectedData.getIdbi());
            for (TblBrokenItemDetail bid : bids) {
                //set data item
                bid.setTblItem(getService().getDataItem(bid.getTblItem().getIditem()));
                //set data unit
                bid.getTblItem().setTblUnit(getService().getDataUnit(bid.getTblItem().getTblUnit().getIdunit()));
                //set data item type (hk)
                if (bid.getTblItem().getTblItemTypeHk() != null) {
                    bid.getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(bid.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type (wh)
                if (bid.getTblItem().getTblItemTypeWh() != null) {
                    bid.getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(bid.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                //data bi detail - imh
                TblBrokenItemDetailItemMutationHistory bidimh = getService().getDataBrokenItemDetailItemMutationHistoryByIDBrokenItemDetail(bid.getIddetail());
                bidimh.setTblBrokenItemDetail(bid);
                bidimh.setTblItemMutationHistory(getService().getDataItemMutationHistory(bidimh.getTblItemMutationHistory().getIdmutation()));
                //data imh - property (s)
                List<TblItemMutationHistoryPropertyBarcode> imhpbs = getService().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(bidimh.getTblItemMutationHistory().getIdmutation());
                List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                    imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, true));
                }
                //data imh - item expired date (s)
                List<TblItemMutationHistoryItemExpiredDate> imhieds = getService().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(bidimh.getTblItemMutationHistory().getIdmutation());
                //data 'BIDetailItemMutationHistory'
                BIDetailItemMutationHistory data = new BIDetailItemMutationHistory(bidimh,
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

    public class BIDetailItemMutationHistory {

        private final ObjectProperty<TblBrokenItemDetailItemMutationHistory> dataBIDIMH = new SimpleObjectProperty<>();

        private List<IMHPropertyBarcodeSelected> dataIMHPBSs = new ArrayList<>();

        private List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = new ArrayList<>();

        private final ObjectProperty<JFXCComboBoxPopup> cbpListDetail = new SimpleObjectProperty<>();

        public BIDetailItemMutationHistory(
                TblBrokenItemDetailItemMutationHistory dataBIDIMH,
                List<IMHPropertyBarcodeSelected> dataIMHPBSs,
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs) {
            //data bid - imh
            this.dataBIDIMH.set(dataBIDIMH);

            //data imh - pb (s)
            for (IMHPropertyBarcodeSelected dataIMHPBS : dataIMHPBSs) {
                this.dataIMHPBSs.add(dataIMHPBS);
            }

            //data imh - ied (s)
            for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                this.dataIMHIEDs.add(dataIMHIED);
            }

            //data cbListDetail
            if (this.dataBIDIMH.get() == null
                    || this.dataBIDIMH.get().getTblBrokenItemDetail() == null
                    || this.dataBIDIMH.get().getTblBrokenItemDetail().getTblItem() == null) {
                //data cb-list combobox
                this.cbpListDetail.set(getComboBoxDetails(this.dataBIDIMH.get()));
            } else {
                if (this.dataBIDIMH.get().getTblBrokenItemDetail().getTblItem().getPropertyStatus()) {   //Property
                    //data cb-list combobox
                    this.cbpListDetail.set(getComboBoxPropertyDetails(this.dataIMHPBSs));
                } else {
                    if (this.dataBIDIMH.get().getTblBrokenItemDetail().getTblItem().getConsumable()) {    //cosumable
                        for (TblItemMutationHistoryItemExpiredDate dataIMHIED : this.dataIMHIEDs) {
                            dataIMHIED.itemQuantityProperty().addListener((obs, oldVale, newVal) -> {
                                this.dataBIDIMH.get().getTblItemMutationHistory().setItemQuantity(calculationTotalItemExpiredDateBI(
                                        this.dataIMHIEDs
                                ));
                            });
                        }
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.dataIMHIEDs));
                    } else {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails(this.dataBIDIMH.get()));
                    }
                }
            }
        }

        private BigDecimal calculationTotalItemExpiredDateBI(List<TblItemMutationHistoryItemExpiredDate> dataIMHPBSs) {
            BigDecimal result = new BigDecimal("0");
            for (TblItemMutationHistoryItemExpiredDate dataIMHPBS : dataIMHPBSs) {
                if (dataIMHPBS.getItemQuantity() != null) {
                    result = result.add(dataIMHPBS.getItemQuantity());
                }
            }
            return result;
        }

        public ObjectProperty<TblBrokenItemDetailItemMutationHistory> dataBIDIMHProperty() {
            return dataBIDIMH;
        }

        public TblBrokenItemDetailItemMutationHistory getDataBIDIMH() {
            return dataBIDIMHProperty().get();
        }

        public void setDataBIDIMH(TblBrokenItemDetailItemMutationHistory dataBIDIMH) {
            dataBIDIMHProperty().set(dataBIDIMH);
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

    public BIDetailItemMutationHistory selectedDataDetail;

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
            selectedDataDetail = new BIDetailItemMutationHistory(null, new ArrayList<>(), new ArrayList<>());
            //open form data - detail
            showDataDetailDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA", "Pilih data 'tempat ditemukan' terlebih dahulu..!", null);
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

    public BIDetailItemMutationHistory generateDataBIDetailItemMutationHistory(TblItem dataItem) {
        if (selectedData != null
                && selectedData.getTblLocationByIdlocationSource() != null
                && selectedData.getTblLocationByIdlocationDestination() != null
                && dataItem != null) {
            //set data broken item detail
            TblBrokenItemDetail bid = new TblBrokenItemDetail();
            bid.setTblBrokenItem(selectedData);
            //set data item
            bid.setTblItem(getService().getDataItem(dataItem.getIditem()));
            //set data unit
            bid.getTblItem().setTblUnit(getService().getDataUnit(dataItem.getTblUnit().getIdunit()));
            //set data item type (hk)
            if (dataItem.getTblItemTypeHk() != null) {
                bid.getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(dataItem.getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type (wh)
            if (dataItem.getTblItemTypeWh() != null) {
                bid.getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(dataItem.getTblItemTypeWh().getIditemTypeWh()));
            }
            //item quantity
            bid.setItemQuantity(new BigDecimal("0"));
            //data bid - imh
            if (bid.getTblItem().getPropertyStatus()) {   //property
                //data bi detail - imh
                TblBrokenItemDetailItemMutationHistory bidimh = new TblBrokenItemDetailItemMutationHistory();
                bidimh.setTblBrokenItemDetail(bid);
                bidimh.setTblItemMutationHistory(new TblItemMutationHistory());
                bidimh.getTblItemMutationHistory().setTblItem(bid.getTblItem());
                bidimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                bidimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                bidimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                bidimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(bid.itemQuantityProperty());
                //data imh - property (s)
                List<TblItemMutationHistoryPropertyBarcode> imhpbs = new ArrayList<>();
                TblItemLocation dataItemLocation = getService().getDataItemLocationByIDLocationAndIDItem(
                        selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                        bid.getTblItem().getIditem()
                );
                List<TblItemLocationPropertyBarcode> ilpbs = getService().getAllDataItemLocationPropertyBarcodeByIDItemLocation(dataItemLocation.getIdrelation());
                for (TblItemLocationPropertyBarcode ilpb : ilpbs) {
                    TblItemMutationHistoryPropertyBarcode imhpb = new TblItemMutationHistoryPropertyBarcode();
                    imhpb.setTblItemMutationHistory(bidimh.getTblItemMutationHistory());
                    imhpb.setTblPropertyBarcode(ilpb.getTblPropertyBarcode());
                    imhpbs.add(imhpb);
                }
                List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                    imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, false));
                }
                //data 'BIDetailItemMutationHistory'
                BIDetailItemMutationHistory data = new BIDetailItemMutationHistory(bidimh,
                        imhpbss,
                        new ArrayList<>());
                return data;
            } else {
                if (bid.getTblItem().getConsumable()) {   //consumable
                    //data bi detail - imh
                    TblBrokenItemDetailItemMutationHistory bidimh = new TblBrokenItemDetailItemMutationHistory();
                    bidimh.setTblBrokenItemDetail(bid);
                    bidimh.setTblItemMutationHistory(new TblItemMutationHistory());
                    bidimh.getTblItemMutationHistory().setTblItem(bid.getTblItem());
                    bidimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                    bidimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                    bidimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    bidimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(bid.itemQuantityProperty());
                    //data imh - item expired date (s)
                    List<TblItemMutationHistoryItemExpiredDate> imhieds = new ArrayList<>();
                    TblItemLocation dataItemLocation = getService().getDataItemLocationByIDLocationAndIDItem(
                            selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                            bid.getTblItem().getIditem()
                    );
                    List<TblItemLocationItemExpiredDate> ilieds = getService().getAllDataItemLocationItemExpiredDateByIDItemLocation(dataItemLocation.getIdrelation());
                    for (TblItemLocationItemExpiredDate ilied : ilieds) {
                        TblItemMutationHistoryItemExpiredDate imhied = new TblItemMutationHistoryItemExpiredDate();
                        imhied.setTblItemMutationHistory(bidimh.getTblItemMutationHistory());
                        imhied.setTblItemExpiredDate(ilied.getTblItemExpiredDate());
                        imhied.setItemQuantity(new BigDecimal("0"));
                        imhieds.add(imhied);
                    }
                    //data 'BIDetailItemMutationHistory'
                    BIDetailItemMutationHistory data = new BIDetailItemMutationHistory(bidimh,
                            new ArrayList<>(),
                            imhieds);
                    return data;
                } else {  //another
                    //data bi detail - imh
                    TblBrokenItemDetailItemMutationHistory bidimh = new TblBrokenItemDetailItemMutationHistory();
                    bidimh.setTblBrokenItemDetail(bid);
                    bidimh.setTblItemMutationHistory(new TblItemMutationHistory());
                    bidimh.getTblItemMutationHistory().setTblItem(bid.getTblItem());
                    bidimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                    bidimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                    bidimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    bidimh.getTblItemMutationHistory().itemQuantityProperty().bindBidirectional(bid.itemQuantityProperty());
                    //data 'BIDetailItemMutationHistory'
                    BIDetailItemMutationHistory data = new BIDetailItemMutationHistory(bidimh,
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
            loader.setLocation(HotelFX.class.getResource("view/feature_broken_item/BrokenItemDetailDialog.fxml"));

            BrokenItemDetailController controller = new BrokenItemDetailController(this);
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
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FBrokenItemManager fBrokenItemManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fBrokenItemManager = new FBrokenItemManagerImpl();

        //set splitpane
        setDataBrokenItemSplitpane();

        //init table
        initTableDataBrokenItem();

        //init form
        initFormDataBrokenItem();

        spDataBrokenItem.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataBrokenItemFormShowStatus.set(0);
        });
    }

    public FBrokenItemManager getService() {
        return fBrokenItemManager;
    }

}
