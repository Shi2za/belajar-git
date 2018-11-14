/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse.warehouse_transfer_item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassDataTransferItem;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassInputLocation;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_warehouse.FeatureWarehouseController;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
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
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class WarehouseTransferItemController implements Initializable {

    //splitpane table and form
    @FXML
    private SplitPane spDataWarehouseTransferItem;

    private DoubleProperty dataWarehouseTransferItemFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataWarehouseTransferItemLayoutDisableLayer;

    private final PseudoClass expiredPseudoClass = PseudoClass.getPseudoClass("expired");

    private boolean isTimeLinePlaying = false;

    private void setDataWarehouseTransferItemSplitPane() {
        spDataWarehouseTransferItem.setDividerPositions(1);

        dataWarehouseTransferItemFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataWarehouseTransferItemFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataWarehouseTransferItem.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataWarehouseTransferItem.getDividers().get(0);
        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataWarehouseTransferItemFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 0.0) {
                tableDataWarehouseTransferItemLayout.setDisable(false);
                tableDataWarehouseTransferItemLayoutDisableLayer.setDisable(true);
                tableDataWarehouseTransferItemLayout.toFront();
            }

            if (newVal.doubleValue() == 1.0) {
                tableDataWarehouseTransferItemLayout.setDisable(true);
                tableDataWarehouseTransferItemLayoutDisableLayer.setDisable(false);
                tableDataWarehouseTransferItemLayoutDisableLayer.toFront();
            }

        });

        dataWarehouseTransferItemFormShowStatus.set(0);

    }

    // show combobox location..
    @FXML
    private GridPane gpComboBoxDataWarehouseTransferItem;

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpLocation;

    private TblItemMutationHistory selectedDataMutation;

    private final LongProperty idWarehouse = new SimpleLongProperty();
    private final LongProperty idItemLoc = new SimpleLongProperty();
    private final StringProperty nameLoc = new SimpleStringProperty("");

    private void initDataComboBox() {
        //data warehouse (main)
        TableView<TblLocationOfWarehouse> tableView = new TableView();

        TableColumn<TblLocationOfWarehouse, String> nameLocation = new TableColumn("Gudang");
        nameLocation.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        nameLocation.setMinWidth(140);

        tableView.getColumns().addAll(nameLocation);

        ObservableList<TblLocationOfWarehouse> locationItems = FXCollections.observableArrayList(loadAllDataWarehouseMain());

        cbpLocation = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableView, locationItems, "", "Gudang *", false, 200, 200
        );

        gpComboBoxDataWarehouseTransferItem.add(cbpLocation, 1, 0);

        //data location type
        TableView<RefLocationType> tableTypeLocation = new TableView();

        TableColumn<RefLocationType, String> nameTypeLocation = new TableColumn("Tipe Lokasi");
        nameTypeLocation.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        nameTypeLocation.setMinWidth(140);

        tableTypeLocation.getColumns().addAll(nameTypeLocation);

        ObservableList<RefLocationType> locationTypeItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataTypeLocation());
        cbpMoveTypeLocationTransfer = new JFXCComboBoxTablePopup<>(
                RefLocationType.class, tableTypeLocation, locationTypeItems, "", "Tipe Lokasi *", true, 200, 200
        );

        //data obsolete by
        TableView<RefItemObsoleteBy> tableObsoleteBy = new TableView();

        TableColumn<RefItemObsoleteBy, String> nameObsoleteBy = new TableColumn("Rusak Oleh");
        nameObsoleteBy.setCellValueFactory(cellData -> cellData.getValue().obsoleteByNameProperty());
        nameObsoleteBy.setMinWidth(140);

        tableObsoleteBy.getColumns().addAll(nameObsoleteBy);

        ObservableList<RefItemObsoleteBy> obsoleteByItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataObsoleteBy());
        for (int i = 0; i < obsoleteByItems.size(); i++) {
            RefItemObsoleteBy obsoleteBy = obsoleteByItems.get(i);
            if (obsoleteBy.getIdobsoleteBy() == 0) {
                obsoleteByItems.remove(obsoleteBy);
            }
        }
        cbpObsoleteBy = new JFXCComboBoxTablePopup<>(
                RefItemObsoleteBy.class, tableObsoleteBy, obsoleteByItems, "", "Rusak Oleh *", true, 200, 200
        );

        //data warehouse - destination
        TableView<TblLocationOfWarehouse> tableDestinationLocationWarehouse = new TableView();

        TableColumn<TblLocationOfWarehouse, String> floorLocationWarehouse = new TableColumn("Lantai");
        floorLocationWarehouse.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfWarehouse, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblFloor().getFloorName(), param.getValue().getTblLocation().getTblFloor().floorNameProperty()));
        floorLocationWarehouse.setMinWidth(140);

        TableColumn<TblLocationOfWarehouse, String> buildLocationWarehouse = new TableColumn("Gedung");
        buildLocationWarehouse.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfWarehouse, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblBuilding().getBuildingName(), param.getValue().getTblLocation().getTblBuilding().buildingNameProperty()));
        buildLocationWarehouse.setMinWidth(140);

        TableColumn<TblLocationOfWarehouse, String> nameDestinationLocationWarehouse = new TableColumn("Gudang");
        nameDestinationLocationWarehouse.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        nameDestinationLocationWarehouse.setMinWidth(140);

        tableDestinationLocationWarehouse.getColumns().addAll(buildLocationWarehouse, floorLocationWarehouse, nameDestinationLocationWarehouse);

        ObservableList<TblLocationOfWarehouse> destinationLocationWarehouseWarehouse = FXCollections.observableArrayList(loadAllDataWarehouse());

        cbpMoveLocationWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableDestinationLocationWarehouse, destinationLocationWarehouseWarehouse, "", "Gudang (Tujuan) *", true, 430, 300
        );

        //data room - destination
        TableView<TblRoom> tableDestinationLocationRoom = new TableView();

        TableColumn<TblRoom, String> floorDestinationLocationRoom = new TableColumn("Lantai");
        floorDestinationLocationRoom.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblFloor().getFloorName(), param.getValue().getTblLocation().getTblFloor().floorNameProperty()));
        floorDestinationLocationRoom.setMinWidth(140);

        TableColumn<TblRoom, String> buildDestinationLocationRoom = new TableColumn("Gedung");
        buildDestinationLocationRoom.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblBuilding().getBuildingName(), param.getValue().getTblLocation().getTblBuilding().buildingNameProperty()));
        buildDestinationLocationRoom.setMinWidth(140);

        TableColumn<TblRoom, String> nameDestinationLocationRoom = new TableColumn("Kamar");
        nameDestinationLocationRoom.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        nameDestinationLocationRoom.setMinWidth(140);

        tableDestinationLocationRoom.getColumns().addAll(buildDestinationLocationRoom, floorDestinationLocationRoom, nameDestinationLocationRoom);

        ObservableList<TblRoom> destinationLocationRoom = FXCollections.observableArrayList(loadAllDataRoom());

        cbpMoveLocationRoom = new JFXCComboBoxTablePopup<>(
                TblRoom.class, tableDestinationLocationRoom, destinationLocationRoom, "", "Kamar (Tujuan) *", true, 430, 300
        );

        //data laundry - destination
        TableView<TblLocationOfLaundry> tableDestinationLocationLaundry = new TableView();

        TableColumn<TblLocationOfLaundry, String> floorDestinationLocationLaundry = new TableColumn("Lantai");
        floorDestinationLocationLaundry.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfLaundry, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblFloor().getFloorName(), param.getValue().getTblLocation().getTblFloor().floorNameProperty()));
        floorDestinationLocationLaundry.setMinWidth(140);

        TableColumn<TblLocationOfLaundry, String> buildDestinationLocationLaundry = new TableColumn("Gedung");
        buildDestinationLocationLaundry.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfLaundry, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblBuilding().getBuildingName(), param.getValue().getTblLocation().getTblBuilding().buildingNameProperty()));
        buildDestinationLocationLaundry.setMinWidth(140);

        TableColumn<TblLocationOfLaundry, String> nameDestinationLocationLaundry = new TableColumn("Laundry");
        nameDestinationLocationLaundry.setCellValueFactory(cellData -> cellData.getValue().laundryNameProperty());
        nameDestinationLocationLaundry.setMinWidth(140);

        tableDestinationLocationLaundry.getColumns().addAll(buildDestinationLocationLaundry, floorDestinationLocationLaundry, nameDestinationLocationLaundry);

        ObservableList<TblLocationOfLaundry> destinationLocationLaundry = FXCollections.observableArrayList(loadAllDataLaundry());

        cbpMoveLocationLaundry = new JFXCComboBoxTablePopup<>(
                TblLocationOfLaundry.class, tableDestinationLocationLaundry, destinationLocationLaundry, "", "Laundry (Tujuan) *", true, 430, 300
        );

        //data bin - destination
        TableView<TblLocationOfBin> tableDestinationLocationBin = new TableView();

        TableColumn<TblLocationOfBin, String> nameDestinationLocationBin = new TableColumn("Bin");
        nameDestinationLocationBin.setCellValueFactory(cellData -> cellData.getValue().binNameProperty());
        nameDestinationLocationBin.setMinWidth(140);

        tableDestinationLocationBin.getColumns().addAll(nameDestinationLocationBin);

        ObservableList<TblLocationOfBin> destinationLocationBin = FXCollections.observableArrayList(loadAllDataBin());

        cbpMoveLocationBin = new JFXCComboBoxTablePopup<>(
                TblLocationOfBin.class, tableDestinationLocationBin, destinationLocationBin, "", "Bin (Tujuan) *", true, 200, 200
        );

        //data obsolete by - employee
        TableView<TblEmployee> tableObsoleteByEmployee = new TableView();

        TableColumn<TblEmployee, String> codeEmployee = new TableColumn("ID");
        codeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeEmployee(),
                        param.getValue().codeEmployeeProperty()));
        codeEmployee.setMinWidth(100);

        TableColumn<TblEmployee, String> employeeName = new TableColumn("Nama");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(),
                        param.getValue().getTblPeople().fullNameProperty()));
        employeeName.setMinWidth(140);

        tableObsoleteByEmployee.getColumns().addAll(codeEmployee, employeeName);

        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpObsoleteByEmployee = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableObsoleteByEmployee, employeeItems, "", "Karyawan *", true, 250, 200
        );

        //attached to layout
        cbpNameEmployeeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpObsoleteByEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpObsoleteByEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpObsoleteByEmployee, 0.0);
        AnchorPane.setTopAnchor(cbpObsoleteByEmployee, 0.0);
        cbpNameEmployeeLayout.getChildren().add(cbpObsoleteByEmployee);
    }

    private void refreshDataPopupComboBoxLocationWarehouse() {
        ObservableList<TblLocationOfWarehouse> warehouseNameItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataWarehouse());
        cbpLocation.setItems(warehouseNameItems);
    }

    @FXML
    private AnchorPane tableDataWarehouseTransferItemLayout;

    @FXML
    private JFXButton btnTransfer;

    @FXML
    private JFXButton btnBroken;

    private final StringProperty status = new SimpleStringProperty();

    private ClassFilteringTable<ClassDataTransferItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataWarehouseTransferItem;

    private void initTableDataWarehouseTransferItem() {

        setTableDataWarehouseTransferItem();
        setTableControlDataWarehouseTransferItem();
        //initTableFormDataProperty();

        ancBodyLayout.getChildren().clear();

        AnchorPane ach = new AnchorPane();
        ach.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataWarehouseTransferItem, 0.0);
        AnchorPane.setLeftAnchor(tableDataWarehouseTransferItem, 0.0);
        AnchorPane.setBottomAnchor(tableDataWarehouseTransferItem, 0.0);
        AnchorPane.setRightAnchor(tableDataWarehouseTransferItem, 0.0);
        ach.getChildren().add(tableDataWarehouseTransferItem);

        TitledPane tp = new TitledPane("List Data Barang", ach);
        tp.setCollapsible(false);
        tp.setExpanded(true);

        AnchorPane.setTopAnchor(tp, 15.0);
        AnchorPane.setLeftAnchor(tp, 15.0);
        AnchorPane.setBottomAnchor(tp, 15.0);
        AnchorPane.setRightAnchor(tp, 15.0);
        ancBodyLayout.getChildren().add(tp);
    }

    private void setTableDataWarehouseTransferItem() {
        TableView<ClassDataTransferItem> tableView = new TableView();

        TableColumn<ClassDataTransferItem, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemLocation().getTblItem().getCodeItem(), param.getValue().getTblItemLocation().getTblItem().codeItemProperty()));
        codeItem.setMinWidth(120);

        TableColumn<ClassDataTransferItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemLocation().getTblItem().getItemName(), param.getValue().getTblItemLocation().getTblItem().itemNameProperty()));
        itemName.setMinWidth(140);

        TableColumn<ClassDataTransferItem, String> itemQuantity = new TableColumn("Stok");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getItemStock(param.getValue())),
                        param.getValue().getTblItemLocation().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<ClassDataTransferItem, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemLocation().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItemLocation().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(120);

        TableColumn<ClassDataTransferItem, String> propertyBarcode = new TableColumn("Barcode");
        propertyBarcode.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblItemLocationPropertyBarcode() == null)
                                ? "-" : param.getValue().getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getCodeBarcode(),
                        param.getValue().tblItemLocationPropertyBarcodeProperty()));
        propertyBarcode.setMinWidth(120);

        TableColumn<ClassDataTransferItem, String> expiredDate = new TableColumn("Tgl. Kadarluarsa");
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblItemLocationItemExpiredDate() == null)
                                ? "-" : ClassFormatter.dateFormate.format(param.getValue().getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate()),
                        param.getValue().tblItemLocationItemExpiredDateProperty()));
        expiredDate.setMinWidth(120);

        TableColumn<ClassDataTransferItem, String> itemTypeHK = new TableColumn("House Keeping");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemLocation().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getTblItemLocation().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-",
                        param.getValue().getTblItemLocation().getTblItem().tblItemTypeHkProperty()));
        itemTypeHK.setMinWidth(140);

        TableColumn<ClassDataTransferItem, String> itemTypeWH = new TableColumn("Warehouse");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataTransferItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemLocation().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getTblItemLocation().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-",
                        param.getValue().getTblItemLocation().getTblItem().tblItemTypeWhProperty()));
        itemTypeWH.setMinWidth(140);

        TableColumn<ClassDataTransferItem, String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);

        tableView.getColumns().addAll(codeItem, titledItemType, itemName, propertyBarcode, expiredDate, itemQuantity, itemUnit);

        tableView.setRowFactory(tv -> {
            TableRow<ClassDataTransferItem> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(expiredPseudoClass, checkExpired(newVal));
                } else {
                    row.pseudoClassStateChanged(expiredPseudoClass, false);
                }
            });
            return row;
        });

        tableDataWarehouseTransferItem = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                ClassDataTransferItem.class,
                tableDataWarehouseTransferItem.getTableView(),
                tableDataWarehouseTransferItem.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
//        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);

        idWarehouse.addListener((obs, oldVal, newVal) -> {
            refreshDataTableWarehouseTransferItem(newVal.longValue());
        });
    }

    private BigDecimal getItemStock(ClassDataTransferItem classDataTransferItem) {
        if (classDataTransferItem != null) {
            if (classDataTransferItem.getTblItemLocationPropertyBarcode() != null) {  //property
                return new BigDecimal("1");
            } else {
                if (classDataTransferItem.getTblItemLocationItemExpiredDate() != null) {  //consumable
                    return classDataTransferItem.getTblItemLocationItemExpiredDate().getItemQuantity();
                } else {
                    return classDataTransferItem.getTblItemLocation().getItemQuantity();
                }
            }
        }
        return new BigDecimal("0");
    }

    private boolean checkExpired(ClassDataTransferItem classDataTransferItem) {
        if (classDataTransferItem != null
                && classDataTransferItem.getTblItemLocationItemExpiredDate() != null
                && classDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate() != null
                && classDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate() != null) {
            long differentDays = DAYS.between(
                    LocalDate.now(),
                    LocalDate.of(
                            classDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate().getYear() + 1900,
                            classDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate().getMonth() + 1,
                            classDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate().getDate()));
            SysDataHardCode sdhIED = parentController.getFWarehouseManager().getDataSysDataHardCode(34);    //WarningItemExpiredDate = '34'
            if(sdhIED != null){
                long warningDifferentDaysNote = Long.valueOf(sdhIED.getDataHardCodeValue());
                return differentDays < warningDifferentDaysNote;
            }
        }
        return false;
    }

    private List<TblLocationOfWarehouse> loadAllDataWarehouseMain() {
        List<TblLocationOfWarehouse> list = parentController.getFWarehouseManager().getAllDataWarehouse();
        if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
            for (int i = list.size() - 1; i > -1; i--) {
                //data location
                list.get(i).setTblLocation(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocation().getIdlocation()));
                //data group
                if (list.get(i).getTblLocation().getTblGroup() != null) {
                    list.get(i).getTblLocation().setTblGroup(parentController.getFWarehouseManager().getDataGroup(list.get(i).getTblLocation().getTblGroup().getIdgroup()));
                }
                //revome data if not used
                if (list.get(i).getTblLocation().getTblGroup() == null
                        || list.get(i).getTblLocation().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private ObservableList<ClassDataTransferItem> loadAllDataWarehouseLocation(long id) {
        List<ClassDataTransferItem> list = new ArrayList<>();
        //data item location
        List<TblItemLocation> dataItemLocations = parentController.getFWarehouseManager().getAllDataItemLocation(id);
        for (TblItemLocation dataItemLocation : dataItemLocations) {
            //data location
            dataItemLocation.setTblLocation(parentController.getFWarehouseManager().getLocation(dataItemLocation.getTblLocation().getIdlocation()));
            //data item
            dataItemLocation.setTblItem(parentController.getFWarehouseManager().getDataItem(dataItemLocation.getTblItem().getIditem()));
            //data item location - property barcode
            if (dataItemLocation.getTblItem().getPropertyStatus()) {    //property
                List<TblItemLocationPropertyBarcode> dataItemLocationPropertyBarcodes = parentController.getFWarehouseManager().getAllDataItemLocationPropertyBarcodeByIDItemLocation(dataItemLocation.getIdrelation());
                for (TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode : dataItemLocationPropertyBarcodes) {
                    ClassDataTransferItem classDataTransferItem = new ClassDataTransferItem();
                    classDataTransferItem.setTblItemLocation(dataItemLocation);
                    classDataTransferItem.setTblItemLocationPropertyBarcode(dataItemLocationPropertyBarcode);
                    classDataTransferItem.getTblItemLocationPropertyBarcode().setTblPropertyBarcode(parentController.getFWarehouseManager().getDataPropertyBarcode(classDataTransferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getIdbarcode()));
                    list.add(classDataTransferItem);
                }
            } else {
                //data item location - item expired date
                if (dataItemLocation.getTblItem().getConsumable()) {    //consumable
                    List<TblItemLocationItemExpiredDate> dataItemLocationItemExpiredDates = parentController.getFWarehouseManager().getAllDataItemLocationItemExpiredDateByIDItemLocation(dataItemLocation.getIdrelation());
                    for (TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate : dataItemLocationItemExpiredDates) {
                        ClassDataTransferItem classDataTransferItem = new ClassDataTransferItem();
                        classDataTransferItem.setTblItemLocation(dataItemLocation);
                        classDataTransferItem.setTblItemLocationItemExpiredDate(dataItemLocationItemExpiredDate);
                        classDataTransferItem.getTblItemLocationItemExpiredDate().setTblItemExpiredDate(parentController.getFWarehouseManager().getDataItemExpiredDate(classDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate()));
                        list.add(classDataTransferItem);
                    }
                } else {
                    ClassDataTransferItem classDataTransferItem = new ClassDataTransferItem();
                    classDataTransferItem.setTblItemLocation(dataItemLocation);
                    list.add(classDataTransferItem);
                }
            }

        }
        //remove data with zero value
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblItemLocationItemExpiredDate() != null) {
                if (list.get(i).getTblItemLocationItemExpiredDate().getItemQuantity()
                        .compareTo(new BigDecimal("0")) < 1) {
                    list.remove(i);
                }
            } else {
                if (list.get(i).getTblItemLocation().getItemQuantity()
                        .compareTo(new BigDecimal("0")) < 1) {
                    list.remove(i);
                }
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private void setTableControlDataWarehouseTransferItem() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonTransferItem;
        JFXButton buttonBroken;
        JFXButton buttonPrint;

        buttonTransferItem = new JFXButton();
        buttonTransferItem.setTooltip(new Tooltip("Transfer Barang"));
        buttonTransferItem.setText("Transfer Barang");
        buttonTransferItem.setOnMouseClicked((e) -> {
            //listener add
            if (cbpLocation.getValue() != null) {
                status.set("Transfer");
                dataTransferItemHandle(status.getValue());

                cbpMoveTypeLocationTransfer.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null) {
                        cbpNameLocationLayout.getChildren().clear();
                    } else {
                        refreshNameLocation(newVal.getIdtype());
                    }
                });

            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan pilih gudang terlebih dahulu!!", null);
            }

        });
        buttonControls.add(buttonTransferItem);

        buttonBroken = new JFXButton();
        buttonBroken.setTooltip(new Tooltip("Transfer Barang Rusak"));
        buttonBroken.setText("Transfer Barang Rusak");
        buttonBroken.setOnMouseClicked((e) -> {
            //listener add
            if (cbpLocation.getValue() != null) {
                status.set("Broken");
                dataTransferItemHandle(status.getValue());
                cbpNameLocationLayout.getChildren().clear();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan pilih gudang terlebih dahulu!!", null);
            }
        });
        buttonControls.add(buttonBroken);

        buttonPrint = new JFXButton();
        buttonPrint.setTooltip(new Tooltip("Cetak Data Barang"));
        buttonPrint.setText("Cetak");
        buttonControls.add(buttonPrint);

        tableDataWarehouseTransferItem.addButtonControl(buttonControls);
    }

    //form transfer item
    @FXML
    private AnchorPane formAnchor;
    @FXML
    private AnchorPane tableFormPropertyBarcodeLayout;
    @FXML
    private ScrollPane spFormDataWarehouseTransferItem;
    @FXML
    private TitledPane tlpTranserItem;
    @FXML
    private GridPane gpFormDataWarehouseTransferItem;
    @FXML
    private JFXTextField txtItemName;
    @FXML
    private JFXTextField txtPropertyBarcode;
    @FXML
    private JFXTextField txtExpiredDate;
    @FXML
    private JFXTextField txtItemQuantity;
    @FXML
    private JFXTextField txtItemTransferQuantity;
    @FXML
    private TableView tableFormPropertyBarcode;
    @FXML
    private Label lblTypeItem;
    @FXML
    private Label lblUnit1;
    @FXML
    private Label lblUnit2;
    @FXML
    private JFXTextArea noteMutation;
    @FXML
    private JFXButton btnFormTransfer;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnSelected;
    @FXML
    private JFXButton btnSelectedAll;
    @FXML
    private AnchorPane cbpNameLocationLayout;
    @FXML
    private AnchorPane cbpNameEmployeeLayout;
    @FXML
    private AnchorPane cbpNameTypeLocationLayout;

    private JFXCComboBoxTablePopup<RefLocationType> cbpMoveTypeLocationTransfer;

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpMoveLocationWarehouse;
    private JFXCComboBoxTablePopup<TblRoom> cbpMoveLocationRoom;
    private JFXCComboBoxTablePopup<TblLocationOfLaundry> cbpMoveLocationLaundry;
    private JFXCComboBoxTablePopup<TblLocationOfBin> cbpMoveLocationBin;

    private JFXCComboBoxTablePopup<RefItemObsoleteBy> cbpObsoleteBy;
    private JFXCComboBoxTablePopup<TblEmployee> cbpObsoleteByEmployee;

    private JFXCComboBoxPopup cbpItemExpiredDateSelected = new JFXCComboBoxPopup<>(null);

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty();

    private final StringProperty filter = new SimpleStringProperty("");
    private int dataInputStatus = 0;

    private TblLocationOfWarehouse selectedDataLocationOfWarehouse;

    private List<TblLocationOfWarehouse> listWarehouse = new ArrayList();
    private ClassInputLocation selectedDataInputLocation;
    private ClassDataTransferItem selectedDataTransferItem;

    private int scrollCounter = 0;

    private int idMutationType;

    private void initFormDataWarehouseTransferItem() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataWarehouseTransferItem.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataWarehouseTransferItem.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("err:" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        btnFormTransfer.setTooltip(new Tooltip("Simpan (Data Transfer Barang)"));
        btnFormTransfer.setOnAction((c) -> {
            if (status.getValue().equalsIgnoreCase("Transfer")) {
                dataWarehouseTransferItemMutationHandle();
            } else {
                dataWarehouseBrokenMutationHandle();
            }
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataWarehouseTransferItemCancelHandle();
        });

        status.addListener((obs, oldVal, newVal) -> {
            refreshFormMutationPopup(newVal);
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtItemTransferQuantity,
                cbpMoveTypeLocationTransfer,
                cbpMoveLocationRoom,
                cbpMoveLocationWarehouse,
                cbpMoveLocationLaundry,
                cbpMoveLocationBin,
                cbpObsoleteBy,
                cbpObsoleteByEmployee);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemTransferQuantity);
    }

    private void refreshFormMutationPopup(String status) {
        cbpMoveTypeLocationTransfer.setValue(null);
        cbpObsoleteBy.setValue(null);
        if (status.equalsIgnoreCase("Transfer")) {
            ObservableList<RefLocationType> locationTypeItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataTypeLocation());
            cbpMoveTypeLocationTransfer.setItems(locationTypeItems);

            cbpNameTypeLocationLayout.getChildren().clear();
            AnchorPane.setBottomAnchor(cbpMoveTypeLocationTransfer, 0.0);
            AnchorPane.setLeftAnchor(cbpMoveTypeLocationTransfer, 0.0);
            AnchorPane.setRightAnchor(cbpMoveTypeLocationTransfer, 0.0);
            AnchorPane.setTopAnchor(cbpMoveTypeLocationTransfer, 0.0);
            cbpNameTypeLocationLayout.getChildren().add(cbpMoveTypeLocationTransfer);
        } else if (status.equalsIgnoreCase("Broken")) {
            ObservableList<RefItemObsoleteBy> obsoleteByItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataObsoleteBy());
            for (int i = 0; i < obsoleteByItems.size(); i++) {
                RefItemObsoleteBy obsoleteBy = obsoleteByItems.get(i);
                if (obsoleteBy.getIdobsoleteBy() == 0) {
                    obsoleteByItems.remove(obsoleteBy);
                }
            }
            cbpObsoleteBy.setItems(obsoleteByItems);

            cbpNameTypeLocationLayout.getChildren().clear();
            AnchorPane.setBottomAnchor(cbpObsoleteBy, 0.0);
            AnchorPane.setLeftAnchor(cbpObsoleteBy, 0.0);
            AnchorPane.setRightAnchor(cbpObsoleteBy, 0.0);
            AnchorPane.setTopAnchor(cbpObsoleteBy, 0.0);
            cbpNameTypeLocationLayout.getChildren().add(cbpObsoleteBy);
        }
    }

    private void refreshNameLocation(int id) {

        switch (id) {
            case 0:
                ObservableList<TblRoom> destinationLocationRoom = FXCollections.observableArrayList(loadAllDataRoom());
                cbpMoveLocationRoom.setItems(destinationLocationRoom);
                cbpNameLocationLayout.getChildren().clear();
                AnchorPane.setBottomAnchor(cbpMoveLocationRoom, 0.0);
                AnchorPane.setLeftAnchor(cbpMoveLocationRoom, 0.0);
                AnchorPane.setRightAnchor(cbpMoveLocationRoom, 0.0);
                AnchorPane.setTopAnchor(cbpMoveLocationRoom, 0.0);
                cbpNameLocationLayout.getChildren().add(cbpMoveLocationRoom);
                cbpMoveLocationWarehouse.setValue(null);
                cbpMoveLocationLaundry.setValue(null);
                cbpMoveLocationBin.setValue(null);
                break;
            case 1:
                ObservableList<TblLocationOfWarehouse> destinationLocationWarehouse = FXCollections.observableArrayList(loadAllDataWarehouse());
                cbpMoveLocationWarehouse.setItems(destinationLocationWarehouse);
                cbpNameLocationLayout.getChildren().clear();
                AnchorPane.setBottomAnchor(cbpMoveLocationWarehouse, 0.0);
                AnchorPane.setLeftAnchor(cbpMoveLocationWarehouse, 0.0);
                AnchorPane.setRightAnchor(cbpMoveLocationWarehouse, 0.0);
                AnchorPane.setTopAnchor(cbpMoveLocationWarehouse, 0.0);
                cbpNameLocationLayout.getChildren().add(cbpMoveLocationWarehouse);
                cbpMoveLocationRoom.setValue(null);
                cbpMoveLocationLaundry.setValue(null);
                cbpMoveLocationBin.setValue(null);
                break;
            case 2:
                ObservableList<TblLocationOfLaundry> destinationLocationLaundry = FXCollections.observableArrayList(loadAllDataLaundry());
                cbpMoveLocationLaundry.setItems(destinationLocationLaundry);
                cbpNameLocationLayout.getChildren().clear();
                AnchorPane.setBottomAnchor(cbpMoveLocationLaundry, 0.0);
                AnchorPane.setLeftAnchor(cbpMoveLocationLaundry, 0.0);
                AnchorPane.setRightAnchor(cbpMoveLocationLaundry, 0.0);
                AnchorPane.setTopAnchor(cbpMoveLocationLaundry, 0.0);
                cbpNameLocationLayout.getChildren().add(cbpMoveLocationLaundry);
                cbpMoveLocationRoom.setValue(null);
                cbpMoveLocationWarehouse.setValue(null);
                cbpMoveLocationBin.setValue(null);
                break;
            case 4:
                ObservableList<TblLocationOfBin> destinationLocationBin = FXCollections.observableArrayList(loadAllDataBin());
                cbpMoveLocationBin.setItems(destinationLocationBin);
                cbpNameLocationLayout.getChildren().clear();
                AnchorPane.setBottomAnchor(cbpMoveLocationBin, 0.0);
                AnchorPane.setLeftAnchor(cbpMoveLocationBin, 0.0);
                AnchorPane.setRightAnchor(cbpMoveLocationBin, 0.0);
                AnchorPane.setTopAnchor(cbpMoveLocationBin, 0.0);
                cbpNameLocationLayout.getChildren().add(cbpMoveLocationBin);
                cbpMoveLocationRoom.setValue(null);
                cbpMoveLocationWarehouse.setValue(null);
                cbpMoveLocationLaundry.setValue(null);
                break;
        }
    }

    private List<TblLocationOfWarehouse> loadAllDataWarehouse() {
        List<TblLocationOfWarehouse> list = parentController.getFWarehouseManager().getAllDataWarehouse();
        for (int i = list.size() - 1; i > -1; i--) {
            //data location
            list.get(i).setTblLocation(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocation().getIdlocation()));
            //data group
            if (list.get(i).getTblLocation().getTblGroup() != null) {
                list.get(i).getTblLocation().setTblGroup(parentController.getFWarehouseManager().getDataGroup(list.get(i).getTblLocation().getTblGroup().getIdgroup()));
            }
            //revome data if not used
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (list.get(i).getTblLocation().getTblGroup() == null
                        || list.get(i).getTblLocation().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()
                        || (cbpLocation.getValue() != null
                        && list.get(i).getIdwarehouse() == cbpLocation.getValue().getIdwarehouse())) {
                    list.remove(i);
                }
            } else {
                if (cbpLocation.getValue() != null
                        && list.get(i).getIdwarehouse() == cbpLocation.getValue().getIdwarehouse()) {
                    list.remove(i);
                }
            }
        }

        return list;
    }

    private List<TblRoom> loadAllDataRoom() {
        List<TblRoom> list = parentController.getFWarehouseManager().getAllDataRoom();
        if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
            for (int i = list.size() - 1; i > -1; i--) {
                //data location
                list.get(i).setTblLocation(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocation().getIdlocation()));
                //data group
                if (list.get(i).getTblLocation().getTblGroup() != null) {
                    list.get(i).getTblLocation().setTblGroup(parentController.getFWarehouseManager().getDataGroup(list.get(i).getTblLocation().getTblGroup().getIdgroup()));
                }
                //revome data if not used
                if (list.get(i).getTblLocation().getTblGroup() == null
                        || list.get(i).getTblLocation().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private List<TblLocationOfLaundry> loadAllDataLaundry() {
        List<TblLocationOfLaundry> list = parentController.getFWarehouseManager().getAllDataLaundry();
        if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
            for (int i = list.size() - 1; i > -1; i--) {
                //data location
                list.get(i).setTblLocation(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocation().getIdlocation()));
                //data group
                if (list.get(i).getTblLocation().getTblGroup() != null) {
                    list.get(i).getTblLocation().setTblGroup(parentController.getFWarehouseManager().getDataGroup(list.get(i).getTblLocation().getTblGroup().getIdgroup()));
                }
                //revome data if not used
                if (list.get(i).getTblLocation().getTblGroup() == null
                        || list.get(i).getTblLocation().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private List<TblLocationOfBin> loadAllDataBin() {
        return parentController.getFWarehouseManager().getAllDataBin();
    }

    private List<TblEmployee> loadAllDataEmployee() {
        List<TblEmployee> list = parentController.getFWarehouseManager().getAllDataEmployee();
        for (TblEmployee data : list) {
            //data people
            data.setTblPeople(parentController.getFWarehouseManager().getDataPeople(data.getTblPeople().getIdpeople()));
        }
        return list;
    }

    private void setSelectedDataItemLocationToInputForm() {
        txtItemName.textProperty().bindBidirectional(selectedDataTransferItem.getTblItemLocation().getTblItem().itemNameProperty());

        txtPropertyBarcode.setText("-");
        txtExpiredDate.setText("-");
        if (selectedDataTransferItem.getTblItemLocationPropertyBarcode() != null) {
            txtPropertyBarcode.setText(selectedDataTransferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getCodeBarcode());
        }
        if (selectedDataTransferItem.getTblItemLocationItemExpiredDate() != null) {
            txtExpiredDate.setText(ClassFormatter.dateFormate.format(selectedDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate()));
        }

        if (selectedDataTransferItem.getTblItemLocationPropertyBarcode() != null) {   //property barcode
            txtItemQuantity.setText(ClassFormatter.decimalFormat.cFormat(new BigDecimal("1")));
        } else {
            if (selectedDataTransferItem.getTblItemLocationItemExpiredDate() != null) {   //item expired date
                txtItemQuantity.setText(ClassFormatter.decimalFormat.cFormat(selectedDataTransferItem.getTblItemLocationItemExpiredDate().getItemQuantity()));
            } else {
                txtItemQuantity.setText(ClassFormatter.decimalFormat.cFormat(selectedDataTransferItem.getTblItemLocation().getItemQuantity()));
            }
        }

        lblUnit1.setText(selectedDataTransferItem.getTblItemLocation().getTblItem().getTblUnit().getUnitName());
        lblUnit2.setText(selectedDataTransferItem.getTblItemLocation().getTblItem().getTblUnit().getUnitName());

        cbpNameEmployeeLayout.setVisible(false);
    }

    private void dataTransferItemHandle(String status) {
        if (tableDataWarehouseTransferItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataTransferItem = (ClassDataTransferItem) tableDataWarehouseTransferItem.getTableView().getSelectionModel().getSelectedItem();
            selectedDataTransferItem.setTblItemLocation(parentController.getFWarehouseManager().getDataItemLocation(selectedDataTransferItem.getTblItemLocation().getIdrelation()));
            selectedDataTransferItem.getTblItemLocation().setTblItem(parentController.getFWarehouseManager().getDataItem(selectedDataTransferItem.getTblItemLocation().getTblItem().getIditem()));
            //selectedDataTransferItem.setTblItemLocationPropertyBarcode(selectedDataItemLocationPropertyBarcode);
            if (status.equalsIgnoreCase("Transfer")) {
                tlpTranserItem.setText("Data Transfer");
            } else {
                tlpTranserItem.setText("Data Transfer (Barang Rusak)");
            }

            if (selectedDataTransferItem.getTblItemLocationPropertyBarcode() != null) {
                selectedDataTransferItem.setTblItemLocationPropertyBarcode(parentController.getFWarehouseManager().getDataItemLocationPropertyBarcode(selectedDataTransferItem.getTblItemLocationPropertyBarcode().getIdrelation()));
                selectedDataTransferItem.getTblItemLocationPropertyBarcode().setTblPropertyBarcode(parentController.getFWarehouseManager().getDataPropertyBarcode(selectedDataTransferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getIdbarcode()));
            }

            if (selectedDataTransferItem.getTblItemLocationItemExpiredDate() != null) {
                selectedDataTransferItem.setTblItemLocationItemExpiredDate(parentController.getFWarehouseManager().getDataItemLocationItemExpiredDate(selectedDataTransferItem.getTblItemLocationItemExpiredDate().getIdrelation()));
                selectedDataTransferItem.getTblItemLocationItemExpiredDate().setTblItemExpiredDate(parentController.getFWarehouseManager().getDataItemExpiredDate(selectedDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate()));
            }

            lblTypeItem.setText((selectedDataTransferItem.getTblItemLocation().getTblItem().getTblItemTypeHk() != null
                    ? selectedDataTransferItem.getTblItemLocation().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                    + " / "
                    + (selectedDataTransferItem.getTblItemLocation().getTblItem().getTblItemTypeWh() != null
                            ? selectedDataTransferItem.getTblItemLocation().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-"));
            lblTypeItem.setVisible(false);

            selectedDataMutation = new TblItemMutationHistory();
            selectedDataMutation.setItemQuantity(new BigDecimal("0"));

            setSelectedDataItemLocationToInputForm();

            if (status.equalsIgnoreCase("Transfer")) {

                selectedDataMutation.setTblLocationByIdlocationOfDestination(new TblLocation());

                selectedDataInputLocation = new ClassInputLocation();

                setSelectedDataMutationToInputForm();
            } else {
                selectedDataMutation.setTblLocationByIdlocationOfDestination(parentController.getFWarehouseManager().getLocationByIDLocationType(4)); //Bin = '4'
                selectedDataMutation.setRefItemMutationType(parentController.getFWarehouseManager().getMutationType(2));

                setSelectedDataMutationBrokenToInputForm();
            }
            dataWarehouseTransferItemFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void refreshDataPopupFormWarehouse() {
        ObservableList<RefLocationType> moveTypeItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataTypeLocation());
        for (int i = moveTypeItems.size() - 1; i > -1; i--) {
            RefLocationType typeLocation = moveTypeItems.get(i);
            if (typeLocation.getIdtype() == 3 //Supplier = '3'
                    || typeLocation.getIdtype() == 4 //Bin = '4'
                    || typeLocation.getIdtype() == 5 //StockOpname = '5'
                    || typeLocation.getIdtype() == 6) { //Digunakan = '6'
                moveTypeItems.remove(typeLocation);
            }
        }
        cbpMoveTypeLocationTransfer.setItems(moveTypeItems);

        ObservableList<TblLocationOfWarehouse> destinationLocationWarehouseItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataWarehouse());
        cbpMoveLocationWarehouse.setItems(destinationLocationWarehouseItems);
        ObservableList<TblRoom> destinationLocationRoomItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataRoom());
        cbpMoveLocationRoom.setItems(destinationLocationRoomItems);
        ObservableList<TblLocationOfLaundry> destinationLocationLaundryItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataLaundry());
        cbpMoveLocationLaundry.setItems(destinationLocationLaundryItems);

        ObservableList<RefItemObsoleteBy> obsoleteByItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataObsoleteBy());
        for (int i = 0; i < obsoleteByItems.size(); i++) {
            RefItemObsoleteBy obsoleteBy = obsoleteByItems.get(i);
            if (obsoleteBy.getIdobsoleteBy() == 0) {
                obsoleteByItems.remove(obsoleteBy);
            }
        }
        cbpObsoleteBy.setItems(obsoleteByItems);

        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
        cbpObsoleteByEmployee.setItems(employeeItems);
    }

    private void setSelectedDataMutationToInputForm() {
        //Status isItem:false(property) ; true(bukan property)
        refreshDataPopupFormWarehouse();
        txtItemTransferQuantity.setPromptText("Jumlah Barang (Transfer) *");
        //selectedDataMutation.itemQuantityProperty().set(BigDecimal.valueOf(tableFormPropertyBarcode.getSelectionModel().getSelectedItems().size()));
        if (selectedDataTransferItem.getTblItemLocationPropertyBarcode() != null) {
            txtItemTransferQuantity.setDisable(true);
            selectedDataMutation.setItemQuantity(BigDecimal.valueOf(1));
            Bindings.bindBidirectional(txtItemTransferQuantity.textProperty(), selectedDataMutation.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());
        } else {
            txtItemTransferQuantity.setDisable(false);
            Bindings.bindBidirectional(txtItemTransferQuantity.textProperty(), selectedDataMutation.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());
        }

        cbpMoveTypeLocationTransfer.valueProperty().bindBidirectional(selectedDataMutation.getTblLocationByIdlocationOfDestination().refLocationTypeProperty());

        cbpMoveTypeLocationTransfer.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                cbpMoveLocationRoom.valueProperty().set(null);
                cbpMoveLocationWarehouse.valueProperty().set(null);
                cbpMoveLocationLaundry.valueProperty().set(null);
                cbpMoveLocationBin.valueProperty().set(null);
            } else {
                switch (newVal.getIdtype()) {
                    case 0:
                        cbpMoveLocationRoom.valueProperty().bindBidirectional(selectedDataInputLocation.roomProperty());
                        break;
                    case 1:
                        cbpMoveLocationWarehouse.valueProperty().bindBidirectional(selectedDataInputLocation.warehouseProperty());
                        break;
                    case 2:
                        cbpMoveLocationLaundry.valueProperty().bindBidirectional(selectedDataInputLocation.laundryProperty());
                        break;
                    case 4:
                        cbpMoveLocationBin.valueProperty().bindBidirectional(selectedDataInputLocation.binProperty());
                        break;
                }

                if (newVal.getIdtype() != 4) {
                    selectedDataMutation.setRefItemMutationType(parentController.getFWarehouseManager().getMutationType(0));
                } else {
                    selectedDataMutation.setRefItemMutationType(parentController.getFWarehouseManager().getMutationType(1));
                }
            }

        });

        cbpMoveTypeLocationTransfer.hide();
        cbpMoveLocationRoom.hide();
        cbpMoveLocationWarehouse.hide();
        cbpMoveLocationLaundry.hide();

        noteMutation.textProperty().bindBidirectional(selectedDataMutation.mutationNoteProperty());

        cbpNameEmployeeLayout.setVisible(false);
    }

    private void dataWarehouseTransferItemMutationHandle() {
        if (checkDataInputWarehouseTransferItem()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin mentransfer barang ini?", null);
            if (alert.getResult() == ButtonType.OK) {
                System.out.println(">>Masuk Transfer Item");
                dataWarehouseTransferItemHandle(status.getValue());
            } else {
                dataWarehouseTransferItemFormShowStatus.set(1);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }

    }

    private void dataWarehouseTransferItemCancelHandle() {
        refreshDataTableWarehouseTransferItem(idWarehouse.getValue());
        dataWarehouseTransferItemFormShowStatus.set(0);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableWarehouseTransferItem(long idWR) {
        tableDataWarehouseTransferItem.getTableView().setItems(loadAllDataWarehouseLocation(idWR));
        cft.refreshFilterItems(tableDataWarehouseTransferItem.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputWarehouseTransferItem() {

        boolean check = true;
        errDataInput = "";
        if (txtItemTransferQuantity.getText() == null
                || txtItemTransferQuantity.getText().equals("")
                || txtItemTransferQuantity.getText().equals("-")) {
            check = false;
            errDataInput += "Jumlah barang yang ditransfer : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedDataMutation.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                check = false;
                errDataInput += "Jumlah barang yang di transfer : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (selectedDataTransferItem.getTblItemLocation().getItemQuantity()
                        .compareTo(new BigDecimal("0")) != 0) {
                    if (selectedDataTransferItem.getTblItemLocationPropertyBarcode() != null) { //property
                        if (selectedDataMutation.getItemQuantity()
                                .compareTo(new BigDecimal("1")) == 1) {
                            check = false;
                            errDataInput += "Stok barang tidak mencukupi!! \n";
                        }
                    } else {
                        if (selectedDataTransferItem.getTblItemLocationItemExpiredDate() != null) { //consumable
                            if (selectedDataMutation.getItemQuantity()
                                    .compareTo(selectedDataTransferItem.getTblItemLocationItemExpiredDate().getItemQuantity()) == 1) {
                                check = false;
                                errDataInput += "Stok barang tidak mencukupi!! \n";
                            }
                        } else {
                            if (selectedDataMutation.getItemQuantity()
                                    .compareTo(selectedDataTransferItem.getTblItemLocation().getItemQuantity()) == 1) {
                                check = false;
                                errDataInput += "Stok barang tidak mencukupi!! \n";
                            }
                        }
                    }
                } else {
                    check = false;
                    errDataInput += "Stok barang habis";
                }
            }
        }

        if (status.getValue().equals("Transfer")) {
            if (cbpMoveTypeLocationTransfer.getValue() == null) {
                check = false;
                errDataInput += "Tipe Lokasi (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                switch (cbpMoveTypeLocationTransfer.getValue().getIdtype()) {
                    case 0:
                        if (cbpMoveLocationRoom.getValue() == null) {
                            check = false;
                            errDataInput += "Kamar (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                        }
                        break;
                    case 1:
                        if (cbpMoveLocationWarehouse.getValue() == null) {
                            check = false;
                            errDataInput += "Gudang (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                        }
                        break;
                    case 2:
                        if (cbpMoveLocationLaundry.getValue() == null) {
                            check = false;
                            errDataInput += "Laundry (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                        }
                        break;
                    case 4:
                        if (cbpMoveLocationBin.getValue() == null) {
                            check = false;
                            errDataInput += "Bin (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                        }
                        break;
                }
            }
        } else {
            if (cbpObsoleteBy.getValue() == null) {
                check = false;
                errDataInput += "Rusak oleh : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (cbpObsoleteBy.getValue().getIdobsoleteBy() == 1 //Karyawan (1) = '1'
                        && cbpObsoleteByEmployee.getValue() == null) {
                    check = false;
                    errDataInput += "Karyawan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                }
            }
        }

        return check;
    }

    @FXML
    private AnchorPane cbpObsoleteByLayout;

    private void setSelectedDataMutationBrokenToInputForm() {
        refreshDataPopupFormWarehouse();

        txtItemTransferQuantity.setPromptText("Jumlah Barang (Rusak) *");
        if (selectedDataTransferItem.getTblItemLocationPropertyBarcode() != null) {
            txtItemTransferQuantity.setDisable(true);
            selectedDataMutation.setItemQuantity(BigDecimal.valueOf(1));
            Bindings.bindBidirectional(txtItemTransferQuantity.textProperty(), selectedDataMutation.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());
        } else {
            txtItemTransferQuantity.setDisable(false);
            Bindings.bindBidirectional(txtItemTransferQuantity.textProperty(), selectedDataMutation.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());
        }

        cbpObsoleteBy.valueProperty().bindBidirectional(selectedDataMutation.refItemObsoleteByProperty());
        cbpObsoleteBy.valueProperty().addListener((obs, oldVal, newVal) -> {
            cbpObsoleteByEmployee.setValue(null);
            if (newVal == null) {
                cbpNameEmployeeLayout.setVisible(false);
            } else {
                switch (newVal.getIdobsoleteBy()) {
                    case 1:    //Karyawan (1) = '1'
                        cbpNameEmployeeLayout.setVisible(true);
                        break;
                    default:
                        cbpNameEmployeeLayout.setVisible(false);
                        break;
                }
            }
        });

        cbpObsoleteBy.hide();
        cbpObsoleteByEmployee.hide();

        cbpObsoleteByEmployee.valueProperty().bindBidirectional(selectedDataMutation.tblEmployeeByIdemployeeProperty());
        noteMutation.textProperty().bindBidirectional(selectedDataMutation.mutationNoteProperty());

        cbpNameEmployeeLayout.setVisible(false);
    }

    private void dataWarehouseBrokenMutationHandle() {
        if (checkDataInputWarehouseTransferItem()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin mentransfer barang ini?", null);
            if (alert.getResult() == ButtonType.OK) {
                System.out.println(">>Masuk Broken");
                dataWarehouseTransferItemHandle(status.getValue());
            } else {
                dataWarehouseTransferItemFormShowStatus.set(1);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }

    }

    private void dataWarehouseTransferItemHandle(String status) {
        //dummy entry
        ClassDataTransferItem dummySelectedDataTransferItem = new ClassDataTransferItem();
        dummySelectedDataTransferItem.setTblItemLocation(new TblItemLocation(selectedDataTransferItem.getTblItemLocation()));
        dummySelectedDataTransferItem.getTblItemLocation().setTblItem(new TblItem(dummySelectedDataTransferItem.getTblItemLocation().getTblItem()));
        dummySelectedDataTransferItem.getTblItemLocation().setTblLocation(new TblLocation(dummySelectedDataTransferItem.getTblItemLocation().getTblLocation()));
        if (selectedDataTransferItem.getTblItemLocationPropertyBarcode() != null) {
            dummySelectedDataTransferItem.setTblItemLocationPropertyBarcode(new TblItemLocationPropertyBarcode(selectedDataTransferItem.getTblItemLocationPropertyBarcode()));
//            dummySelectedDataTransferItem.getTblItemLocationPropertyBarcode().setTblItemLocation(new TblItemLocation(dummySelectedDataTransferItem.getTblItemLocation()));
            dummySelectedDataTransferItem.getTblItemLocationPropertyBarcode().setTblItemLocation(new TblItemLocation(dummySelectedDataTransferItem.getTblItemLocationPropertyBarcode().getTblItemLocation()));
            dummySelectedDataTransferItem.getTblItemLocationPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(dummySelectedDataTransferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode()));
        }
        if (selectedDataTransferItem.getTblItemLocationItemExpiredDate() != null) {
            dummySelectedDataTransferItem.setTblItemLocationItemExpiredDate(new TblItemLocationItemExpiredDate(selectedDataTransferItem.getTblItemLocationItemExpiredDate()));
//            dummySelectedDataTransferItem.getTblItemLocationItemExpiredDate().setTblItemLocation(new TblItemLocation(dummySelectedDataTransferItem.getTblItemLocation()));
            dummySelectedDataTransferItem.getTblItemLocationItemExpiredDate().setTblItemLocation(new TblItemLocation(dummySelectedDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemLocation()));
            dummySelectedDataTransferItem.getTblItemLocationItemExpiredDate().setTblItemExpiredDate(new TblItemExpiredDate(dummySelectedDataTransferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate()));
        }
        TblItemMutationHistory dummySelectedDataMutation = new TblItemMutationHistory(selectedDataMutation);
        if (dummySelectedDataMutation.getTblItem() != null) {
            dummySelectedDataMutation.setTblItem(new TblItem(dummySelectedDataMutation.getTblItem()));
        }
        if (dummySelectedDataMutation.getTblLocationByIdlocationOfSource() != null) {
            dummySelectedDataMutation.setTblLocationByIdlocationOfSource(new TblLocation(dummySelectedDataMutation.getTblLocationByIdlocationOfSource()));
        }
        if (dummySelectedDataMutation.getTblLocationByIdlocationOfDestination() != null) {
            dummySelectedDataMutation.setTblLocationByIdlocationOfDestination(new TblLocation(dummySelectedDataMutation.getTblLocationByIdlocationOfDestination()));
        }
        if (dummySelectedDataMutation.getTblEmployeeByIdemployee() != null) {
            dummySelectedDataMutation.setTblEmployeeByIdemployee(new TblEmployee(dummySelectedDataMutation.getTblEmployeeByIdemployee()));
        }
        if (dummySelectedDataMutation.getRefItemMutationType() != null) {
            dummySelectedDataMutation.setRefItemMutationType(new RefItemMutationType(dummySelectedDataMutation.getRefItemMutationType()));
        }
        if (dummySelectedDataMutation.getRefItemObsoleteBy() != null) {
            dummySelectedDataMutation.setRefItemObsoleteBy(new RefItemObsoleteBy(dummySelectedDataMutation.getRefItemObsoleteBy()));
        }
        ClassInputLocation dummySelectedDataInputLocation = new ClassInputLocation();
        if (status.equals("Transfer")) {
            if (selectedDataInputLocation.getRoom() != null) {
                dummySelectedDataInputLocation.setRoom(new TblRoom(selectedDataInputLocation.getRoom()));
                dummySelectedDataInputLocation.getRoom().setTblLocation(new TblLocation(dummySelectedDataInputLocation.getRoom().getTblLocation()));
            }
            if (selectedDataInputLocation.getWarehouse() != null) {
                dummySelectedDataInputLocation.setWarehouse(new TblLocationOfWarehouse(selectedDataInputLocation.getWarehouse()));
                dummySelectedDataInputLocation.getWarehouse().setTblLocation(new TblLocation(dummySelectedDataInputLocation.getWarehouse().getTblLocation()));
            }
            if (selectedDataInputLocation.getLaundry() != null) {
                dummySelectedDataInputLocation.setLaundry(new TblLocationOfLaundry(selectedDataInputLocation.getLaundry()));
                dummySelectedDataInputLocation.getLaundry().setTblLocation(new TblLocation(dummySelectedDataInputLocation.getLaundry().getTblLocation()));
            }
            if (selectedDataInputLocation.getSupplier() != null) {
                dummySelectedDataInputLocation.setSupplier(new TblSupplier(selectedDataInputLocation.getSupplier()));
                dummySelectedDataInputLocation.getSupplier().setTblLocation(new TblLocation(dummySelectedDataInputLocation.getSupplier().getTblLocation()));
            }
            if (selectedDataInputLocation.getBin() != null) {
                dummySelectedDataInputLocation.setBin(new TblLocationOfBin(selectedDataInputLocation.getBin()));
                dummySelectedDataInputLocation.getBin().setTblLocation(new TblLocation(dummySelectedDataInputLocation.getBin().getTblLocation()));
            }
        }
        String dummyStatus = status;
        if (parentController.getFWarehouseManager().updateDataWarehouseTransferItem(dummySelectedDataTransferItem,
                dummySelectedDataMutation,
                dummySelectedDataInputLocation,
                dummyStatus)) {
            ClassMessage.showSucceedInsertingDataMessage("", null);
            refreshDataTableWarehouseTransferItem(idWarehouse.getValue());
            dataWarehouseTransferItemFormShowStatus.set(0);
            //set unsaving data input -> 'false'
            ClassSession.unSavingDataInput.set(false);
        } else {
            ClassMessage.showFailedInsertingDataMessage(parentController.getFWarehouseManager().getErrorMessage(), null);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataWarehouseTransferItemSplitPane();

        initDataComboBox();

        ClassViewSetting.setImportantField(
                cbpLocation);

        cbpLocation.valueProperty().addListener((obs, oldVal, newVal) -> {
            idWarehouse.set(newVal.getTblLocation().getIdlocation());
        });

        initTableDataWarehouseTransferItem();

        initFormDataWarehouseTransferItem();
    }

    public WarehouseTransferItemController(FeatureWarehouseController parentController) {
        this.parentController = parentController;
    }

    private final FeatureWarehouseController parentController;
}
