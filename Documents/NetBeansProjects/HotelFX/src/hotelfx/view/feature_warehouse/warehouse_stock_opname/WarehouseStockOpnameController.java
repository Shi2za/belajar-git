/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse.warehouse_stock_opname;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStockOpname;
import hotelfx.persistence.model.TblStockOpnameDetail;
import hotelfx.persistence.model.TblStockOpnameDetailItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FStockOpnameManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_warehouse.FeatureWarehouseController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class WarehouseStockOpnameController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataStockOpname;

    private DoubleProperty dataStockOpnameFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataStockOpnameLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataStockOpnameSplitpane() {
        spDataStockOpname.setDividerPositions(1);

        dataStockOpnameFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataStockOpnameFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataStockOpname.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataStockOpname.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataStockOpnameFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataStockOpnameLayout.setDisable(false);
                    tableDataStockOpnameLayoutDisableLayer.setDisable(true);
                    tableDataStockOpnameLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataStockOpnameLayout.setDisable(true);
                    tableDataStockOpnameLayoutDisableLayer.setDisable(false);
                    tableDataStockOpnameLayoutDisableLayer.toFront();
                }
            }
        });

        dataStockOpnameFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataStockOpnameLayout;

    private ClassFilteringTable<TblStockOpname> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataStockOpname;

    private void initTableDataStockOpname() {
        //set table
        setTableDataStockOpname();
        //set control
        setTableControlDataStockOpname();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataStockOpname, 15.0);
        AnchorPane.setLeftAnchor(tableDataStockOpname, 15.0);
        AnchorPane.setRightAnchor(tableDataStockOpname, 15.0);
        AnchorPane.setTopAnchor(tableDataStockOpname, 15.0);
        ancBodyLayout.getChildren().add(tableDataStockOpname);
    }

    private void setTableDataStockOpname() {
        TableView<TblStockOpname> tableView = new TableView();

//        TableColumn<TblStockOpname, String> codeAccount = new TableColumn<>("Kode Akun");
//        codeAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblStockOpname, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getSysAccount().getCodeAccount(),
//                        param.getValue().sysAccountProperty()));
//        codeAccount.setMinWidth(120);
        TableColumn<TblStockOpname, String> codeSO = new TableColumn("No. StockOpname");
        codeSO.setCellValueFactory(cellData -> cellData.getValue().codeStockOpnameProperty());
        codeSO.setMinWidth(140);

        TableColumn<TblStockOpname, String> locationName = new TableColumn<>("Lokasi");
        locationName.setCellValueFactory((TableColumn.CellDataFeatures<TblStockOpname, String> param)
                -> Bindings.createStringBinding(() -> getNameLocation(param.getValue().getTblLocation()),
                        param.getValue().tblLocationProperty()));
        locationName.setMinWidth(140);

        TableColumn<TblStockOpname, String> soNote = new TableColumn<>("Keterangan");
        soNote.setCellValueFactory((TableColumn.CellDataFeatures<TblStockOpname, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getStockOpnameNote(),
                        param.getValue().stockOpnameNoteProperty()));
        soNote.setMinWidth(200);

        TableColumn<TblStockOpname, String> soDate = new TableColumn<>("Tanggal StockOpname");
        soDate.setCellValueFactory((TableColumn.CellDataFeatures<TblStockOpname, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getStockOpanameDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getStockOpanameDate())
                                : "-", param.getValue().stockOpanameDateProperty()));
        soDate.setMinWidth(160);

        TableColumn<TblStockOpname, String> employeeName = new TableColumn<>("Dibuat Oleh");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblStockOpname, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeByIdemployeeProperty()));
        employeeName.setMinWidth(140);

        tableView.getColumns().addAll(codeSO, locationName, soNote, soDate, employeeName);
        tableView.setItems(loadAllDataStockOpname());

        tableView.setRowFactory(tv -> {
            TableRow<TblStockOpname> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataStockOpnameUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataStockOpnameShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataStockOpnameShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataStockOpname = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblStockOpname.class,
                tableDataStockOpname.getTableView(),
                tableDataStockOpname.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataStockOpname() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataStockOpnameCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataStockOpnamePrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataStockOpname.addButtonControl(buttonControls);
    }

    private ObservableList<TblStockOpname> loadAllDataStockOpname() {
        List<TblStockOpname> list = parentController.getFStockOpnameManager().getAllDataStockOpname();
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            //data account
            if (list.get(i).getSysAccount() != null) {
                list.get(i).setSysAccount(parentController.getFStockOpnameManager().getDataAccount(list.get(i).getSysAccount().getIdaccount()));
            }
            //data location
            list.get(i).setTblLocation(parentController.getFStockOpnameManager().getDataLocation(list.get(i).getTblLocation().getIdlocation()));
            //data employee
            list.get(i).setTblEmployeeByIdemployee(parentController.getFStockOpnameManager().getDataEmployee(list.get(i).getTblEmployeeByIdemployee().getIdemployee()));
            //data people
            list.get(i).getTblEmployeeByIdemployee().setTblPeople(parentController.getFStockOpnameManager().getDataPeople(list.get(i).getTblEmployeeByIdemployee().getTblPeople().getIdpeople()));
            if (list.get(i).getTblLocation().getRefLocationType().getIdtype() == 1) {    //Gudang = '1'
                if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                    if (list.get(i).getTblLocation().getTblGroup() == null
                            || list.get(i).getTblLocation().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                        list.remove(i);
                    }
                }
            } else {
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private List<ClassSODetail> loadAllDataSODetail() {
        List<ClassSODetail> list = new ArrayList<>();
        if (selectedData.getIdstockOpname() != 0L) {
            List<TblStockOpnameDetail> dataSODetails = parentController.getFStockOpnameManager().getAllDataStockOpnameDetailByIDStockOpname(selectedData.getIdstockOpname());
            for (TblStockOpnameDetail dataSODetail : dataSODetails) {
                //data item
                dataSODetail.setTblItem(parentController.getFStockOpnameManager().getDataItem(dataSODetail.getTblItem().getIditem()));
                //data unit
                dataSODetail.getTblItem().setTblUnit(parentController.getFStockOpnameManager().getDataUnit(dataSODetail.getTblItem().getTblUnit().getIdunit()));
                //data item type hk
                if (dataSODetail.getTblItem().getTblItemTypeHk() != null) {
                    dataSODetail.getTblItem().setTblItemTypeHk(parentController.getFStockOpnameManager().getDataItemTypeHK(dataSODetail.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //data item type wh
                if (dataSODetail.getTblItem().getTblItemTypeWh() != null) {
                    dataSODetail.getTblItem().setTblItemTypeWh(parentController.getFStockOpnameManager().getDataItemTypeWH(dataSODetail.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                if (dataSODetail.getTblItem().getPropertyStatus()) {  //Property
                    List<TblStockOpnameDetailPropertyBarcode> sodpbs = parentController.getFStockOpnameManager().getAllDataStockOpnameDetailPropertyBarcodeByIDStockOpnameDetail(dataSODetail.getIddetail());
                    for (TblStockOpnameDetailPropertyBarcode sodpb : sodpbs) {
                        //add so detail to list
                        list.add(new ClassSODetail(dataSODetail, sodpb, null));
                    }
                } else {
                    if (dataSODetail.getTblItem().getConsumable()) {  //Consumable
                        List<TblStockOpnameDetailItemExpiredDate> sodieds = parentController.getFStockOpnameManager().getAllDataStockOpnameDetailItemExpiredDateByIDStockOpnameDetail(dataSODetail.getIddetail());
                        for (TblStockOpnameDetailItemExpiredDate sodied : sodieds) {
                            //add so detail to list
                            list.add(new ClassSODetail(dataSODetail, null, sodied));
                        }
                    } else {
                        //add so detail to list
                        list.add(new ClassSODetail(dataSODetail, null, null));
                    }
                }
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
    private GridPane gpFormDataStockOpname;

    @FXML
    private ScrollPane spFormDataStockOpname;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeStockOpname;

    @FXML
    private JFXTextField txtStockOpnameDate;

    @FXML
    private JFXTextArea txtStockOpnameNote;

    @FXML
    private AnchorPane ancDataLocationLayout;
    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpLocationOfWarehouse;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblStockOpname selectedData;

    private void initFormDataStockOpname() {

        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataStockOpname.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataStockOpname.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data StockOpname)"));
        btnSave.setOnAction((e) -> {
            dataStockOpnameSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataStockOpnameCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpLocationOfWarehouse
        );
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal");
    }

    private void initDataPopup() {
        //Location of Warehouse
        TableView<TblLocationOfWarehouse> tableLocationOfWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn<>("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(160);

        tableLocationOfWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> locationOfWarehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());

        cbpLocationOfWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableLocationOfWarehouse, locationOfWarehouseItems, "", "Gudang *", true, 200, 200
        );

        AnchorPane.setBottomAnchor(cbpLocationOfWarehouse, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationOfWarehouse, 0.0);
        AnchorPane.setRightAnchor(cbpLocationOfWarehouse, 0.0);
        AnchorPane.setTopAnchor(cbpLocationOfWarehouse, 0.0);
        ancDataLocationLayout.getChildren().add(cbpLocationOfWarehouse);
    }

    public String getNameLocation(TblLocation location) {
        String name = "";
        switch (location.getRefLocationType().getIdtype()) {
            case 0: //Room = '0'
                TblRoom room = parentController.getFStockOpnameManager().getDataRoomByIDLocation(location.getIdlocation());
                name = room.getRoomName();
                break;
            case 1: //Warehouse = '1'
                TblLocationOfWarehouse warehouse = parentController.getFStockOpnameManager().getDataWarehouseByIDLocation(location.getIdlocation());
                name = warehouse.getWarehouseName();
                break;
            case 2: //Laundry = '2'
                TblLocationOfLaundry laundry = parentController.getFStockOpnameManager().getDataLaundryByIDLocation(location.getIdlocation());
                name = laundry.getLaundryName();
                break;
            case 3: //Suppleir = '3'
                TblSupplier supplier = parentController.getFStockOpnameManager().getDataSupplierByIDLocation(location.getIdlocation());
                name = supplier.getSupplierName();
                break;
            case 4: //Bin = '4'
                TblLocationOfBin bin = parentController.getFStockOpnameManager().getDataBinByIDLocation(location.getIdlocation());
                name = bin.getBinName();
                break;
        }
        return name;
    }

    private List<TblLocationOfWarehouse> loadAllDataLocationOfWarehouse() {
        List<TblLocationOfWarehouse> list = parentController.getFStockOpnameManager().getAllDataWarehouse();
        for (int i = list.size() - 1; i > -1; i--) {
            //data location
            list.get(i).setTblLocation(parentController.getFStockOpnameManager().getDataLocation(list.get(i).getTblLocation().getIdlocation()));
            //data location type
            list.get(i).getTblLocation().setRefLocationType(parentController.getFStockOpnameManager().getDataLocationType(list.get(i).getTblLocation().getRefLocationType().getIdtype()));
            //remove data not used
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (list.get(i).getTblLocation().getTblGroup() == null
                        || list.get(i).getTblLocation().getTblGroup().getIdgroup() == ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //data location of warehouse
        ObservableList<TblLocationOfWarehouse> locationOfWarehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());
        cbpLocationOfWarehouse.setItems(locationOfWarehouseItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeStockOpname() != null
                ? selectedData.getCodeStockOpname() : "");

        txtCodeStockOpname.textProperty().bindBidirectional(selectedData.codeStockOpnameProperty());

        txtStockOpnameDate.setText(ClassFormatter.dateTimeFormate.format(selectedData.getStockOpanameDate()));

        txtStockOpnameNote.textProperty().bindBidirectional(selectedData.stockOpnameNoteProperty());

        cbpLocationOfWarehouse.setValue(getDataLocationWarehouse(selectedData.getTblLocation()));

        cbpLocationOfWarehouse.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3
                    && newVal != null) {
                selectedData.setTblLocation(newVal.getTblLocation());
                //generate data detail
                generateDataDetail(newVal.getTblLocation());
                //set data detail
                setDataDetail();
            }else{
                selectedData.setTblLocation(null);
            }
        });

        cbpLocationOfWarehouse.hide();

        setDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private TblLocationOfWarehouse getDataLocationWarehouse(TblLocation tblLocation) {
        if (tblLocation != null) {
            return parentController.getFWarehouseManager().getDataWarehouseByIdLocation(tblLocation.getIdlocation());
        }
        return null;
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeStockOpname.setDisable(true);
        txtStockOpnameDate.setDisable(true);

        ClassViewSetting.setDisableForAllInputNode(gpFormDataStockOpname,
                dataInputStatus == 3,
                txtCodeStockOpname,
                txtStockOpnameDate);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataStockOpnameCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblStockOpname();
        selectedData.setStockOpanameDate(Timestamp.valueOf(LocalDateTime.now()));
        soDetails = new ArrayList<>();
        setSelectedDataToInputForm();
        //open form data stock opname
        dataStockOpnameFormShowStatus.set(0.0);
        dataStockOpnameFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataStockOpnameShowHandle() {
        if (tableDataStockOpname.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFStockOpnameManager().getDataStockOpname(((TblStockOpname) tableDataStockOpname.getTableView().getSelectionModel().getSelectedItem()).getIdstockOpname());
            selectedData.setTblLocation(parentController.getFStockOpnameManager().getDataLocation(selectedData.getTblLocation().getIdlocation()));
            selectedData.getTblLocation().setRefLocationType(parentController.getFStockOpnameManager().getDataLocationType(selectedData.getTblLocation().getRefLocationType().getIdtype()));
            soDetails = loadAllDataSODetail();
            setSelectedDataToInputForm();
            dataStockOpnameFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataStockOpnameUnshowHandle() {
        refreshDataTableStockOpname();
        dataStockOpnameFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataStockOpnamePrintHandle() {

    }

    private void dataStockOpnameSaveHandle() {
        if (checkDataInputDataStockOpname()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblStockOpname dummySelectedData = new TblStockOpname(selectedData);
                dummySelectedData.setTblLocation(new TblLocation(selectedData.getTblLocation()));
                dummySelectedData.getTblLocation().setRefLocationType(new RefLocationType(dummySelectedData.getTblLocation().getRefLocationType()));
                List<ClassSODetail> dummyClassSODetails = new ArrayList<>();
                for (ClassSODetail classSODetail : (List<ClassSODetail>) tableSODetail.getItems()) {
                    //data stock opname detail
                    TblStockOpnameDetail dummyDataStockOpnameDetail = new TblStockOpnameDetail(classSODetail.getDataSODetail());
                    dummyDataStockOpnameDetail.setTblStockOpname(dummySelectedData);
                    dummyDataStockOpnameDetail.setTblItem(new TblItem(dummyDataStockOpnameDetail.getTblItem()));
                    //data stock opname detail - property barcode
                    TblStockOpnameDetailPropertyBarcode dummyDataStockOpnameDetailPropertyBarcode = null;
                    if (classSODetail.getDataSODetailPropertyBarcode() != null) {
                        dummyDataStockOpnameDetailPropertyBarcode = new TblStockOpnameDetailPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode());
                        dummyDataStockOpnameDetailPropertyBarcode.setTblStockOpnameDetail(dummyDataStockOpnameDetail);
                        dummyDataStockOpnameDetailPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(dummyDataStockOpnameDetailPropertyBarcode.getTblPropertyBarcode()));
                    }
                    //data stock opname detail - item expired date
                    TblStockOpnameDetailItemExpiredDate dummyDataStockOpnameDetailItemExpiredDate = null;
                    if (classSODetail.getDataSODetailItemExpiredDate() != null) {
                        dummyDataStockOpnameDetailItemExpiredDate = new TblStockOpnameDetailItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate());
                        dummyDataStockOpnameDetailItemExpiredDate.setTblStockOpnameDetail(dummyDataStockOpnameDetail);
                        dummyDataStockOpnameDetailItemExpiredDate.setTblItemExpiredDate(new TblItemExpiredDate(dummyDataStockOpnameDetailItemExpiredDate.getTblItemExpiredDate()));
                    }
                    dummyClassSODetails.add(new ClassSODetail(dummyDataStockOpnameDetail, dummyDataStockOpnameDetailPropertyBarcode, dummyDataStockOpnameDetailItemExpiredDate));
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFStockOpnameManager().insertDataStockOpnameWarehouse(dummySelectedData,
                                dummyClassSODetails) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data stock opname
                            refreshDataTableStockOpname();
                            dataStockOpnameFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFStockOpnameManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFStockOpnameManager().updateDataStockOpname(dummySelectedData,
                                new ArrayList<>(),
                                new ArrayList<>())) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data stock opname
                            refreshDataTableStockOpname();
                            dataStockOpnameFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFStockOpnameManager().getErrorMessage(), null);
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

    private void dataStockOpnameCancelHandle() {
        //refresh data from table & close form data stock opname
        refreshDataTableStockOpname();
        dataStockOpnameFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableStockOpname() {
        tableDataStockOpname.getTableView().setItems(loadAllDataStockOpname());
        cft.refreshFilterItems(tableDataStockOpname.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataStockOpname() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpLocationOfWarehouse.getValue() == null) {
            dataInput = false;
            errDataInput += "Gudang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (checkMinusValueInDetailData()) {
            dataInput = false;
            errDataInput += "Stok (Real) : Tidak boleh kurang dari '0' \n";
        }
        return dataInput;
    }

    private boolean checkMinusValueInDetailData() {
        boolean minuValue = false;
        for (ClassSODetail soDetail : soDetails) {
            if (soDetail.getDataSODetail().getItemQuantityReal()
                    .compareTo(new BigDecimal("0")) == -1) {
                minuValue = true;
                break;
            }
        }
        return minuValue;
    }

    /**
     * DATA (Detail)
     */
    private List<ClassSODetail> soDetails;

    private void generateDataDetail(TblLocation dataLocation) {
        soDetails = new ArrayList<>();
        if (dataLocation != null) {
            List<TblItemLocation> dataItemLocations = parentController.getFStockOpnameManager().getAllDataItemLocationByIDLocation(dataLocation.getIdlocation());
            for (TblItemLocation dataItemLocation : dataItemLocations) {
                //data item 
                dataItemLocation.setTblItem(parentController.getFStockOpnameManager().getDataItem(dataItemLocation.getTblItem().getIditem()));
                //data unit
                dataItemLocation.getTblItem().setTblUnit(parentController.getFStockOpnameManager().getDataUnit(dataItemLocation.getTblItem().getTblUnit().getIdunit()));
                //data item type hk
                if (dataItemLocation.getTblItem().getTblItemTypeHk() != null) {
                    dataItemLocation.getTblItem().setTblItemTypeHk(parentController.getFStockOpnameManager().getDataItemTypeHK(dataItemLocation.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //data item type wh
                if (dataItemLocation.getTblItem().getTblItemTypeWh() != null) {
                    dataItemLocation.getTblItem().setTblItemTypeWh(parentController.getFStockOpnameManager().getDataItemTypeWH(dataItemLocation.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                if (dataItemLocation.getTblItem().getPropertyStatus()) {    //Property
                    List<TblItemLocationPropertyBarcode> ilpbs = parentController.getFStockOpnameManager().getAllDataItemLocationPropertyBarcodeByIDItemLocation(dataItemLocation.getIdrelation());
                    for (TblItemLocationPropertyBarcode ilpb : ilpbs) {
                        //create so detail
                        TblStockOpnameDetail dataDetail = new TblStockOpnameDetail();
                        dataDetail.setTblStockOpname(selectedData);
                        dataDetail.setTblItem(new TblItem(dataItemLocation.getTblItem()));
                        dataDetail.setItemQuantitySystem(new BigDecimal("1"));
                        dataDetail.setItemQuantityReal(new BigDecimal("1"));
                        //create so detail - property barcode
                        TblStockOpnameDetailPropertyBarcode dataDetailPropertyBarcode = new TblStockOpnameDetailPropertyBarcode();
                        dataDetailPropertyBarcode.setTblStockOpnameDetail(dataDetail);
                        dataDetailPropertyBarcode.setTblPropertyBarcode(parentController.getFStockOpnameManager().getDataPropertyBarcode(ilpb.getTblPropertyBarcode().getIdbarcode()));
                        //add so detail to list
                        soDetails.add(new ClassSODetail(dataDetail, dataDetailPropertyBarcode, null));
                    }
                } else {
                    if (dataItemLocation.getTblItem().getConsumable()) {  //Consumable
                        List<TblItemLocationItemExpiredDate> ilieds = parentController.getFStockOpnameManager().getAllDataItemLocationItemExpiredDateByIDItemLocation(dataItemLocation.getIdrelation());
                        for (TblItemLocationItemExpiredDate ilied : ilieds) {
                            //create so detail
                            TblStockOpnameDetail dataDetail = new TblStockOpnameDetail();
                            dataDetail.setTblStockOpname(selectedData);
                            dataDetail.setTblItem(new TblItem(dataItemLocation.getTblItem()));
                            dataDetail.setItemQuantitySystem(ilied.getItemQuantity());
                            dataDetail.setItemQuantityReal(ilied.getItemQuantity());
                            //create so detail - item expired date
                            TblStockOpnameDetailItemExpiredDate dataDetailItemExpiredDate = new TblStockOpnameDetailItemExpiredDate();
                            dataDetailItemExpiredDate.setTblStockOpnameDetail(dataDetail);
                            dataDetailItemExpiredDate.setTblItemExpiredDate(parentController.getFStockOpnameManager().getDataItemExpiredDate(ilied.getTblItemExpiredDate().getIditemExpiredDate()));
                            //add so detail to list
                            soDetails.add(new ClassSODetail(dataDetail, null, dataDetailItemExpiredDate));
                        }
                    } else {
                        //create so detail
                        TblStockOpnameDetail dataDetail = new TblStockOpnameDetail();
                        dataDetail.setTblStockOpname(selectedData);
                        dataDetail.setTblItem(new TblItem(dataItemLocation.getTblItem()));
                        dataDetail.setItemQuantitySystem(dataItemLocation.getItemQuantity());
                        dataDetail.setItemQuantityReal(dataItemLocation.getItemQuantity());
                        //event for real quantity
                        dataDetail.itemQuantityRealProperty().addListener((obs, oldVal, newVal) -> {
                            dataDetail.setLastUpdateDate(Date.valueOf(LocalDate.now()));
                        });
                        //add so detail to list
                        soDetails.add(new ClassSODetail(dataDetail, null, null));
                    }
                }
            }
        }
        //sort data with zero value (system stock) to last data
        sortZeroValueToLastData(soDetails);
    }

    private void sortZeroValueToLastData(List<ClassSODetail> arr) {
        boolean swapped = true;
        int j = 0;
        ClassSODetail tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < arr.size() - j; i++) {
                if (arr.get(i).getDataSODetail().getItemQuantitySystem().compareTo(new BigDecimal("0"))
                        == 0) {
                    tmp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }

    public class ClassSODetail {

        private final ObjectProperty<TblStockOpnameDetail> dataSODetail = new SimpleObjectProperty<>();

        private final ObjectProperty<TblStockOpnameDetailPropertyBarcode> dataSODetailPropertyBarcode = new SimpleObjectProperty<>();

        private final ObjectProperty<TblStockOpnameDetailItemExpiredDate> dataSODetailItemExpiredDate = new SimpleObjectProperty<>();

        public ClassSODetail(
                TblStockOpnameDetail dataSODetail,
                TblStockOpnameDetailPropertyBarcode dataSODetailPropertyBarcode,
                TblStockOpnameDetailItemExpiredDate dataSODetailItemExpiredDate) {
            this.dataSODetail.set(dataSODetail);
            this.dataSODetailPropertyBarcode.set(dataSODetailPropertyBarcode);
            this.dataSODetailItemExpiredDate.set(dataSODetailItemExpiredDate);
            if (this.dataSODetailPropertyBarcode.get() != null) {
                this.dataSODetail.get().itemQuantityRealProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        if (newVal.compareTo(new BigDecimal("0")) == -1) {
                            this.dataSODetail.get().setItemQuantityReal(new BigDecimal("0"));
                        } else {
                            if (newVal.compareTo(new BigDecimal("1")) == 1) {
                                this.dataSODetail.get().setItemQuantityReal(new BigDecimal("1"));
                            }
                        }
                    } else {
                        this.dataSODetail.get().setItemQuantityReal(new BigDecimal("0"));
                    }
                });
            }
        }

        public ObjectProperty<TblStockOpnameDetail> dataSODetailProperty() {
            return dataSODetail;
        }

        public TblStockOpnameDetail getDataSODetail() {
            return dataSODetailProperty().get();
        }

        public void setDataSODetail(TblStockOpnameDetail dataSODetail) {
            dataSODetailProperty().set(dataSODetail);
        }

        public ObjectProperty<TblStockOpnameDetailPropertyBarcode> dataSODetailPropertyBarcodeProperty() {
            return dataSODetailPropertyBarcode;
        }

        public TblStockOpnameDetailPropertyBarcode getDataSODetailPropertyBarcode() {
            return dataSODetailPropertyBarcodeProperty().get();
        }

        public void setDataSODetailPropertyBarcode(TblStockOpnameDetailPropertyBarcode dataSODetailPropertyBarcode) {
            dataSODetailPropertyBarcodeProperty().set(dataSODetailPropertyBarcode);
        }

        public ObjectProperty<TblStockOpnameDetailItemExpiredDate> dataSODetailItemExpiredDateProperty() {
            return dataSODetailItemExpiredDate;
        }

        public TblStockOpnameDetailItemExpiredDate getDataSODetailItemExpiredDate() {
            return dataSODetailItemExpiredDateProperty().get();
        }

        public void setDataSODetailItemExpiredDate(TblStockOpnameDetailItemExpiredDate dataSODetailItemExpiredDate) {
            dataSODetailItemExpiredDateProperty().set(dataSODetailItemExpiredDate);
        }

    }

    @FXML
    private AnchorPane ancDataTableDetail;

    public TableView tableSODetail;

    public void setDataDetail() {
        ancDataTableDetail.getChildren().clear();

        tableSODetail = new TableView();
        tableSODetail.setEditable(dataInputStatus != 3);

        TableColumn<ClassSODetail, String> codeItem = new TableColumn("ID");
        codeItem.setMinWidth(120);
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataSODetail().getTblItem().getCodeItem(),
                        param.getValue().dataSODetailProperty()));

        TableColumn<ClassSODetail, String> itemName = new TableColumn("Barang");
        itemName.setMinWidth(140);
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataSODetail().getTblItem().getItemName(),
                        param.getValue().dataSODetailProperty()));

        TableColumn<ClassSODetail, String> barcode = new TableColumn("Barcode");
        barcode.setMinWidth(140);
        barcode.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(() -> getDataBarcode(param.getValue()),
                        param.getValue().dataSODetailProperty()));

        TableColumn<ClassSODetail, String> expiredDate = new TableColumn("Tgl. Kadarluarsa");
        expiredDate.setMinWidth(140);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(() -> getDataExpiredDate(param.getValue()),
                        param.getValue().dataSODetailProperty()));

        TableColumn<ClassSODetail, String> unitName = new TableColumn("Satuan");
        unitName.setMinWidth(140);
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataSODetail().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataSODetailProperty()));

        TableColumn<ClassSODetail, String> quantitySystem = new TableColumn("Sistem");
        quantitySystem.setMinWidth(140);
        quantitySystem.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataSODetail().getItemQuantitySystem()),
                        param.getValue().getDataSODetail().itemQuantitySystemProperty()));

        TableColumn<ClassSODetail, String> quantityReal = new TableColumn("Real");
        quantityReal.setMinWidth(140);
        quantityReal.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(()
                        -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataSODetail().getItemQuantityReal()),
                        param.getValue().getDataSODetail().itemQuantityRealProperty()));
        Callback<TableColumn<ClassSODetail, String>, TableCell<ClassSODetail, String>> cellFactory
                = new Callback<TableColumn<ClassSODetail, String>, TableCell<ClassSODetail, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemQuantityReal();
                    }
                };
        quantityReal.setCellFactory(cellFactory);
        quantityReal.setEditable(true);

        TableColumn<ClassSODetail, String> quantityDiffrence = new TableColumn("Selisih");
        quantityDiffrence.setMinWidth(140);
        quantityDiffrence.setCellValueFactory((TableColumn.CellDataFeatures<ClassSODetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat((param.getValue().getDataSODetail().getItemQuantityReal()).subtract(param.getValue().getDataSODetail().getItemQuantitySystem())),
                        param.getValue().getDataSODetail().itemQuantityRealProperty()));

        TableColumn<ClassSODetail, String> quantity = new TableColumn("Jumlah");
        quantity.getColumns().addAll(quantitySystem, quantityReal, quantityDiffrence);

        tableSODetail.getColumns().addAll(codeItem, itemName, barcode, expiredDate, quantity, unitName);

        tableSODetail.setItems(FXCollections.observableArrayList(soDetails));

        AnchorPane.setBottomAnchor(tableSODetail, 0.0);
        AnchorPane.setLeftAnchor(tableSODetail, 0.0);
        AnchorPane.setRightAnchor(tableSODetail, 0.0);
        AnchorPane.setTopAnchor(tableSODetail, 0.0);
        ancDataTableDetail.getChildren().clear();
        ancDataTableDetail.getChildren().add(tableSODetail);
    }

    private String getDataBarcode(ClassSODetail classSODetail) {
        if (classSODetail != null
                && classSODetail.getDataSODetailPropertyBarcode() != null
                && classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode() != null) {
            return classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getCodeBarcode();
        }
        return "-";
    }

    private String getDataExpiredDate(ClassSODetail classSODetail) {
        if (classSODetail != null
                && classSODetail.getDataSODetailItemExpiredDate() != null
                && classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate() != null) {
            return ClassFormatter.dateFormate.format(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate());
        }
        return "-";
    }

    class EditingCellItemQuantityReal extends TableCell<ClassSODetail, String> {

        private JFXTextField tItemQuantity;

        public EditingCellItemQuantityReal() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null) {
                super.startEdit();
                tItemQuantity = new JFXTextField();
                tItemQuantity.setPromptText("Jumlah (Real)");

                ClassViewSetting.setImportantField(
                        tItemQuantity);

                tItemQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", tItemQuantity);

                Bindings.bindBidirectional(tItemQuantity.textProperty(), ((ClassSODetail) this.getTableRow().getItem()).getDataSODetail().itemQuantityRealProperty(), new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(tItemQuantity);
                tItemQuantity.selectAll();

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

            tItemQuantity.textProperty().unbindBidirectional(((ClassSODetail) this.getTableRow().getItem()).getDataSODetail().itemQuantityRealProperty());

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
                            if (((ClassSODetail) data).getDataSODetail().getItemQuantityReal() != null) {
                                setText(ClassFormatter.decimalFormat.cFormat(((ClassSODetail) data).getDataSODetail().getItemQuantityReal()));
                            } else {
                                setText("");
                            }
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataStockOpnameSplitpane();

        //init table
        initTableDataStockOpname();

        //init form
        initFormDataStockOpname();

        spDataStockOpname.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataStockOpnameFormShowStatus.set(0.0);
        });
    }

    public WarehouseStockOpnameController(FeatureWarehouseController parentController) {
        this.parentController = parentController;
    }

    private final FeatureWarehouseController parentController;

    public FStockOpnameManager getService() {
        return parentController.getFStockOpnameManager();
    }

}
