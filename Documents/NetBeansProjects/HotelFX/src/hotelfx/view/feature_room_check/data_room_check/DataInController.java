/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.data_room_check;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailItem;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomTypeItem;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class DataInController implements Initializable {

    @FXML
    private AnchorPane ancFormDataIn;

    @FXML
    private GridPane gpFormDataDataIn;

    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    private JFXCComboBoxTablePopup<DataFilter> cbpDataFilter;

    private JFXCComboBoxTablePopup<RefLocationType> cbpLocationType;

    private JFXCComboBoxTablePopup<TblRoom> cbpRoom;

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpLocationOfWarehouse;

    private JFXCComboBoxTablePopup<TblLocationOfLaundry> cbpLocationOfLaundry;

    private JFXCComboBoxTablePopup<TblPropertyBarcode> cbpPropertyBarcode;

    private JFXCComboBoxTablePopup<TblItemExpiredDate> cbpItemExpiredDate;

    @FXML
    private AnchorPane ancRoomLayout;

    @FXML
    private AnchorPane ancWarehouseLayout;

    @FXML
    private AnchorPane ancLaundryLayout;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private Label lblUnit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDataIn() {
        initDataInPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataInSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataInCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();

        ancRoomLayout.setVisible(false);
        ancWarehouseLayout.setVisible(false);
        ancLaundryLayout.setVisible(false);

        cbpDataFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            //Item
            ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem(newVal));
            cbpItem.setItems(itemItems);
            cbpItem.setValue(null);
        });

        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblUnit.setText("");
            txtItemQuantity.setEditable(true);
            //location type
            ObservableList<RefLocationType> locatonTypeItems = FXCollections.observableArrayList(loadAllDataLocationType(newVal));
            cbpLocationType.setItems(locatonTypeItems);
            cbpLocationType.setValue(null);
            if (newVal != null) {
                if (newVal.getPropertyStatus()) {   //Property
                    //property barcode
                    ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(
                            newVal,
                            getSourceLocation()));
                    cbpPropertyBarcode.setItems(propertyBarcodeItems);
                    cbpPropertyBarcode.setValue(null);
                    dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().setItemQuantity(new BigDecimal("1"));
                    txtItemQuantity.setEditable(false);
                }
                if (newVal.getConsumable()) {   //Consumable
                    //item expired date
                    ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(
                            newVal,
                            getSourceLocation()));
                    cbpItemExpiredDate.setItems(itemExpiredDateItems);
                    cbpItemExpiredDate.setValue(null);
                    dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    txtItemQuantity.setEditable(true);
                }
                lblUnit.setText(newVal.getTblUnit().getUnitName());
            }
            refreshDataPropertyBarcodeSelectedPopup(newVal);
            refreshDataItemExpiredDateSelectedPopup(newVal);
        });

        cbpLocationType.valueProperty().addListener((obs, oldVal, newVal) -> {
            cbpRoom.setValue(null);
            cbpLocationOfWarehouse.setValue(null);
            cbpLocationOfLaundry.setValue(null);
            ancRoomLayout.setVisible(false);
            ancWarehouseLayout.setVisible(false);
            ancLaundryLayout.setVisible(false);
            if (newVal != null) {
                switch (newVal.getIdtype()) {
                    case 0:  //Kamar = '0'
                        //Room
                        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom(cbpItem.getValue()));
                        cbpRoom.setItems(roomItems);
                        //Layout
                        ancRoomLayout.setVisible(true);
                        ancRoomLayout.toFront();
                        break;
                    case 1:   //Gudang = '1'
                        //Warehouse
                        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataWarehouse(cbpItem.getValue()));
                        cbpLocationOfWarehouse.setItems(warehouseItems);
                        //Layout
                        ancWarehouseLayout.setVisible(true);
                        ancWarehouseLayout.toFront();
                        break;
                    case 2: //Laundry = '2'
                        //Laundry
                        ObservableList<TblLocationOfLaundry> laundryItems = FXCollections.observableArrayList(loadAllDataLaundry(cbpItem.getValue()));
                        cbpLocationOfLaundry.setItems(laundryItems);
                        //Layout
                        ancLaundryLayout.setVisible(true);
                        ancLaundryLayout.toFront();
                        break;
                    default:
                        break;
                }
            }
        });

        cbpRoom.valueProperty().addListener((obs, oldVal, newVal) -> {
            //property barcode
            ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(
                    cbpItem.getValue(),
                    newVal != null ? newVal.getTblLocation() : null));
            cbpPropertyBarcode.setItems(propertyBarcodeItems);
            cbpPropertyBarcode.setValue(null);
            refreshDataPropertyBarcodeSelectedPopup(cbpItem.getValue());
            //item expired date
            ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(
                    cbpItem.getValue(),
                    newVal != null ? newVal.getTblLocation() : null));
            cbpItemExpiredDate.setItems(itemExpiredDateItems);
            cbpItemExpiredDate.setValue(null);
            refreshDataItemExpiredDateSelectedPopup(cbpItem.getValue());
        });

        cbpLocationOfWarehouse.valueProperty().addListener((obs, oldVal, newVal) -> {
            //property barcode
            ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(
                    cbpItem.getValue(),
                    newVal != null ? newVal.getTblLocation() : null));
            cbpPropertyBarcode.setItems(propertyBarcodeItems);
            cbpPropertyBarcode.setValue(null);
            refreshDataPropertyBarcodeSelectedPopup(cbpItem.getValue());
            //item expired date
            ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(
                    cbpItem.getValue(),
                    newVal != null ? newVal.getTblLocation() : null));
            cbpItemExpiredDate.setItems(itemExpiredDateItems);
            cbpItemExpiredDate.setValue(null);
            refreshDataItemExpiredDateSelectedPopup(cbpItem.getValue());
        });

        cbpLocationOfLaundry.valueProperty().addListener((obs, oldVal, newVal) -> {
            //property barcode
            ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(
                    cbpItem.getValue(),
                    newVal != null ? newVal.getTblLocation() : null));
            cbpPropertyBarcode.setItems(propertyBarcodeItems);
            cbpPropertyBarcode.setValue(null);
            refreshDataPropertyBarcodeSelectedPopup(cbpItem.getValue());
            //item expired date
            ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(
                    cbpItem.getValue(),
                    newVal != null ? newVal.getTblLocation() : null));
            cbpItemExpiredDate.setItems(itemExpiredDateItems);
            cbpItemExpiredDate.setValue(null);
            refreshDataItemExpiredDateSelectedPopup(cbpItem.getValue());
        });
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpItem,
                cbpDataFilter,
                cbpLocationType,
                cbpRoom,
                cbpLocationOfWarehouse,
                cbpLocationOfLaundry,
                cbpPropertyBarcode,
                cbpItemExpiredDate,
                txtItemQuantity);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }

    private void refreshDataPropertyBarcodeSelectedPopup(
            TblItem dataItem) {
        cbpPropertyBarcode.setVisible(false);
        if (dataItem != null) {
            if (dataItem.getPropertyStatus()) { //Property
                cbpPropertyBarcode.setVisible(true);
            }
        }
    }

    private void refreshDataItemExpiredDateSelectedPopup(
            TblItem dataItem) {
        cbpItemExpiredDate.setVisible(false);
        if (dataItem != null) {
            if (dataItem.getConsumable()) { //consumable
                cbpItemExpiredDate.setVisible(true);
            }
        }
    }

    private void initDataInPopup() {
        //Data Filter
        TableView<DataFilter> tableDataFilter = new TableView<>();

        TableColumn<DataFilter, String> filterName = new TableColumn("Filter");
        filterName.setCellValueFactory((TableColumn.CellDataFeatures<DataFilter, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getFilterName(), param.getValue().filterNameProperty()));
        filterName.setMinWidth(180);

        tableDataFilter.getColumns().addAll(filterName);

        ObservableList<DataFilter> dataFilterItems = FXCollections.observableArrayList(loadAllDataFilter());

        cbpDataFilter = new JFXCComboBoxTablePopup<>(
                DataFilter.class, tableDataFilter, dataFilterItems, "", "Filter *", true, 200, 200
        );

        //Item
        TableView<TblItem> tableItem = new TableView<>();

        TableColumn<TblItem, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeItem(), param.getValue().codeItemProperty()));
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getItemName(), param.getValue().itemBrandProperty()));
        itemName.setMinWidth(140);

        tableItem.getColumns().addAll(codeItem, itemName);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem(cbpDataFilter.getValue()));

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 300, 300
        );

        //Location Type
        TableView<RefLocationType> tableLocationtype = new TableView<>();

        TableColumn<RefLocationType, String> locationTypeName = new TableColumn<>("Tipe Lokasi");
        locationTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        locationTypeName.setMinWidth(140);

        tableLocationtype.getColumns().addAll(locationTypeName);

        ObservableList<RefLocationType> locationTypeItems = FXCollections.observableArrayList(loadAllDataLocationType(cbpItem.getValue()));

        cbpLocationType = new JFXCComboBoxTablePopup<>(
                RefLocationType.class, tableLocationtype, locationTypeItems, "", "Tipe Lokasi *", true, 200, 200
        );

        //Room
        TableView<TblRoom> tableRoom = new TableView<>();

        TableColumn<TblRoom, String> roomName = new TableColumn<>("Kamar");
        roomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        roomName.setMinWidth(140);

        tableRoom.getColumns().addAll(roomName);

        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom(cbpItem.getValue()));

        cbpRoom = new JFXCComboBoxTablePopup<>(
                TblRoom.class, tableRoom, roomItems, "", "Kamar *", true, 200, 200
        );

        //Warehouse
        TableView<TblLocationOfWarehouse> tableWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn<>("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(140);

        tableWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataWarehouse(cbpItem.getValue()));

        cbpLocationOfWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableWarehouse, warehouseItems, "", "Gudang *", true, 200, 200
        );

        //Laundry
        TableView<TblLocationOfLaundry> tableLaundry = new TableView<>();

        TableColumn<TblLocationOfLaundry, String> laundryName = new TableColumn<>("Laundry");
        laundryName.setCellValueFactory(cellData -> cellData.getValue().laundryNameProperty());
        laundryName.setMinWidth(140);

        tableLaundry.getColumns().addAll(laundryName);

        ObservableList<TblLocationOfLaundry> laundryItems = FXCollections.observableArrayList(loadAllDataLaundry(cbpItem.getValue()));

        cbpLocationOfLaundry = new JFXCComboBoxTablePopup<>(
                TblLocationOfLaundry.class, tableLaundry, laundryItems, "", "Laundry *", true, 200, 200
        );

        //Property Barcode
        TableView<TblPropertyBarcode> tablePropertyBarcode = new TableView<>();

        TableColumn<TblPropertyBarcode, String> barcode = new TableColumn("Barcode");
        barcode.setCellValueFactory((TableColumn.CellDataFeatures<TblPropertyBarcode, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeBarcode(),
                        param.getValue().codeBarcodeProperty()));
        barcode.setMinWidth(120);

        tablePropertyBarcode.getColumns().addAll(barcode);

        ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(cbpItem.getValue(), getSourceLocation()));

        cbpPropertyBarcode = new JFXCComboBoxTablePopup<>(
                TblPropertyBarcode.class, tablePropertyBarcode, propertyBarcodeItems, "", "Barcode *", true, 140, 250
        );

        //Item Expired Date
        TableView<TblItemExpiredDate> tableItemExpiredDate = new TableView<>();

        TableColumn<TblItemExpiredDate, String> expiredDate = new TableColumn("Tgl. Kadarluarsa");
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getItemExpiredDate()),
                        param.getValue().itemExpiredDateProperty()));
        expiredDate.setMinWidth(120);

        tableItemExpiredDate.getColumns().addAll(expiredDate);

        ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(cbpItem.getValue(), getSourceLocation()));

        cbpItemExpiredDate = new JFXCComboBoxTablePopup<>(
                TblItemExpiredDate.class, tableItemExpiredDate, itemExpiredDateItems, "", "Tgl. Kadarluarsa *", true, 140, 250
        );

        //attached to grid-pane
        gpFormDataDataIn.add(cbpItem, 1, 0);
        gpFormDataDataIn.add(cbpDataFilter, 0, 0);
        gpFormDataDataIn.add(cbpLocationType, 0, 1);

        AnchorPane.setBottomAnchor(cbpRoom, 0.0);
        AnchorPane.setLeftAnchor(cbpRoom, 0.0);
        AnchorPane.setRightAnchor(cbpRoom, 0.0);
        AnchorPane.setTopAnchor(cbpRoom, 0.0);
        ancRoomLayout.getChildren().add(cbpRoom);

        AnchorPane.setBottomAnchor(cbpLocationOfWarehouse, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationOfWarehouse, 0.0);
        AnchorPane.setRightAnchor(cbpLocationOfWarehouse, 0.0);
        AnchorPane.setTopAnchor(cbpLocationOfWarehouse, 0.0);
        ancWarehouseLayout.getChildren().add(cbpLocationOfWarehouse);

        AnchorPane.setBottomAnchor(cbpLocationOfLaundry, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationOfLaundry, 0.0);
        AnchorPane.setRightAnchor(cbpLocationOfLaundry, 0.0);
        AnchorPane.setTopAnchor(cbpLocationOfLaundry, 0.0);
        ancLaundryLayout.getChildren().add(cbpLocationOfLaundry);

        gpFormDataDataIn.add(cbpPropertyBarcode, 0, 2);
        gpFormDataDataIn.add(cbpItemExpiredDate, 0, 2);
    }

    private List<TblItem> loadAllDataItem(DataFilter dataFilter) {
        List<TblItem> list = new ArrayList<>();
        if (dataFilter != null) {
            switch (dataFilter.getFilterName()) {
                case "Semua":
                    list = dataRoomCheckController.getService().getAllDataItem();
                    for (int i = list.size() - 1; i > -1; i--) {
                        if (!list.get(i).getGuestStatus()) {  //!Guest
                            list.remove(i);
                        } else {
                            //data unit
                            list.get(i).setTblUnit(dataRoomCheckController.getService().getDataUnit(list.get(i).getTblUnit().getIdunit()));
                            //data item type hk
                            if (list.get(i).getTblItemTypeHk() != null) {
                                list.get(i).setTblItemTypeHk(dataRoomCheckController.getService().getDataItemTypeHK(list.get(i).getTblItemTypeHk().getIditemTypeHk()));
                            }
                            //data item type wh
                            if (list.get(i).getTblItemTypeWh() != null) {
                                list.get(i).setTblItemTypeWh(dataRoomCheckController.getService().getDataItemTypeWH(list.get(i).getTblItemTypeWh().getIditemTypeWh()));
                            }
                        }
                    }
                    break;
                case "Tipe Kamar":
                    List<TblRoomTypeItem> roomTypeItems = dataRoomCheckController.getService().getAllDataRoomTypeItemByIDRoomType(dataRoomCheckController.selectedData.getTblRoom().getTblRoomType().getIdroomType());
                    for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                        if (roomTypeItem.getTblItem().getGuestStatus()) {  //Guest
                            //data item
                            TblItem data = dataRoomCheckController.getService().getDataItem(roomTypeItem.getTblItem().getIditem());
                            //data unit
                            data.setTblUnit(dataRoomCheckController.getService().getDataUnit(data.getTblUnit().getIdunit()));
                            //data item type hk
                            if (data.getTblItemTypeHk() != null) {
                                data.setTblItemTypeHk(dataRoomCheckController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
                            }
                            //data item type wh
                            if (data.getTblItemTypeWh() != null) {
                                data.setTblItemTypeWh(dataRoomCheckController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
                            }
                            //add data item to list
                            list.add(data);
                        }
                    }
                    break;
                case "Reservasi Kamar":
                    TblReservationRoomTypeDetail previousDataRRTD = getPreviousRRTD(dataRoomCheckController.selectedData.getTblRoom(), Date.valueOf(LocalDate.now().minusDays(1)));
                    TblReservationRoomTypeDetail nextDataRRTD = getNextRRTD(dataRoomCheckController.selectedData.getTblRoom(), Date.valueOf(LocalDate.now()));
                    if (previousDataRRTD != null
                            && nextDataRRTD != null) {
                        if (previousDataRRTD.getIddetail()
                                == nextDataRRTD.getIddetail()) {
                            SysCurrentHotelDate schd = dataRoomCheckController.getService().getDataCurrentHotelDate(2); //House Keeping - Daily Cleaning = '2'
                            if (LocalDateTime.now().isBefore(LocalDateTime.of(schd.getCurrentHotelDate().getYear() + 1900,
                                    schd.getCurrentHotelDate().getMonth() + 1,
                                    schd.getCurrentHotelDate().getDate(),
                                    schd.getCurrentHotelDate().getHours(),
                                    schd.getCurrentHotelDate().getMinutes()))) {
                                list = getListItemByDataRRTDAndDate(previousDataRRTD, Date.valueOf(LocalDate.now().minusDays(1)));
                            } else {
                                list = getListItemByDataRRTDAndDate(nextDataRRTD, Date.valueOf(LocalDate.now()));
                            }
                        } else {
                            list = getListItemByDataRRTDAndDate(previousDataRRTD, Date.valueOf(LocalDate.now().minusDays(1)));
                        }
                    } else {
                        if (previousDataRRTD != null) {
                            list = getListItemByDataRRTDAndDate(previousDataRRTD, Date.valueOf(LocalDate.now().minusDays(1)));
                        } else {
                            if (nextDataRRTD != null) {
                                list = getListItemByDataRRTDAndDate(nextDataRRTD, Date.valueOf(LocalDate.now()));
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    private List<TblItem> getListItemByDataRRTDAndDate(
            TblReservationRoomTypeDetail dataRRTD,
            Date date) {
        List<TblItem> list = new ArrayList<>();
        //data reservation room type detail - item
        List<TblReservationRoomTypeDetailItem> dataReservationRoomTypeDetailItems = dataRoomCheckController.getService().getAllDataReservationRoomTypeDetailItemByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
        for (TblReservationRoomTypeDetailItem dataReservationRoomTypeDetailItem : dataReservationRoomTypeDetailItems) {
            boolean hasBeenAdded = false;
            for (TblItem data : list) {
                if (data.getIditem() == dataReservationRoomTypeDetailItem.getTblItem().getIditem()) {
                    hasBeenAdded = true;
                    break;
                }
            }
            if (!hasBeenAdded) {
                //data item
                TblItem data = dataRoomCheckController.getService().getDataItem(dataReservationRoomTypeDetailItem.getTblItem().getIditem());
                //data unit
                data.setTblUnit(dataRoomCheckController.getService().getDataUnit(data.getTblUnit().getIdunit()));
                //data item type hk
                if (data.getTblItemTypeHk() != null) {
                    data.setTblItemTypeHk(dataRoomCheckController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
                }
                //data item type wh
                if (data.getTblItemTypeWh() != null) {
                    data.setTblItemTypeWh(dataRoomCheckController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
                }
                //add data item to list
                list.add(data);
            }
        }
        //data reservation addtional item -> by reservation room type detail
        List<TblReservationAdditionalItem> dataReservationAdditionalItems = dataRoomCheckController.getService().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAndAdditionalDate(
                dataRRTD.getIddetail(),
                date
        );
        for (TblReservationAdditionalItem dataReservationAdditionalItem : dataReservationAdditionalItems) {
            boolean hasBeenAdded = false;
            for (TblItem data : list) {
                if (data.getIditem() == dataReservationAdditionalItem.getTblItem().getIditem()) {
                    hasBeenAdded = true;
                    break;
                }
            }
            if (!hasBeenAdded) {
                //data item
                TblItem data = dataRoomCheckController.getService().getDataItem(dataReservationAdditionalItem.getTblItem().getIditem());
                //data unit
                data.setTblUnit(dataRoomCheckController.getService().getDataUnit(data.getTblUnit().getIdunit()));
                //data item type hk
                if (data.getTblItemTypeHk() != null) {
                    data.setTblItemTypeHk(dataRoomCheckController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
                }
                //data item type wh
                if (data.getTblItemTypeWh() != null) {
                    data.setTblItemTypeWh(dataRoomCheckController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
                }
                //add data item to list
                list.add(data);
            }
        }
        return list;
    }

    private TblReservationRoomTypeDetail getPreviousRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = dataRoomCheckController.getService().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = dataRoomCheckController.getService().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = dataRoomCheckController.getService().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(dataRoomCheckController.getService().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(dataRoomCheckController.getService().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(dataRoomCheckController.getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0 //Ready to Check In = '0'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
                            //data reservation
                            rrtd.setTblReservation(dataRoomCheckController.getService().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(dataRoomCheckController.getService().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //retuurn rrtd
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private TblReservationRoomTypeDetail getNextRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = dataRoomCheckController.getService().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = dataRoomCheckController.getService().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = dataRoomCheckController.getService().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(dataRoomCheckController.getService().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(dataRoomCheckController.getService().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(dataRoomCheckController.getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0 //Ready to Check In = '0'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
                            //data reservation
                            rrtd.setTblReservation(dataRoomCheckController.getService().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(dataRoomCheckController.getService().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //retuurn rrtd
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private List<DataFilter> loadAllDataFilter() {
        List<DataFilter> list = new ArrayList<>();
        list.add(new DataFilter("Semua"));
        list.add(new DataFilter("Tipe Kamar"));
        list.add(new DataFilter("Reservasi Kamar"));
        return list;
    }

    private List<RefLocationType> loadAllDataLocationType(TblItem dataItem) {
        List<RefLocationType> list = new ArrayList<>();
        if (dataItem != null) {
            list = dataRoomCheckController.getService().getAllDataLocationType();
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getIdtype() == 3 //Supplier = '3'
                        || list.get(i).getIdtype() == 4  //Bin = '4'
                        || list.get(i).getIdtype() == 5) {  //StockOpname = '5'
                    list.remove(i);
                } else {
                    List<TblItemLocation> itemLocations = dataRoomCheckController.getService().getAllDataItemLocationByIDLocationTypeAndItemQuantityNotZero(list.get(i).getIdtype());
                    if (itemLocations.isEmpty()) {
                        list.remove(i);
                    }
                }
            }
        }
        return list;
    }

    private List<TblRoom> loadAllDataRoom(TblItem dataItem) {
        List<TblRoom> list = new ArrayList<>();
        if (dataItem != null) {
            list = dataRoomCheckController.getService().getAllDataRoom();
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getIdroom()
                        == dataRoomCheckController.selectedData.getTblRoom().getIdroom()) {
                    list.remove(i);
                } else {
                    list.get(i).setTblLocation(dataRoomCheckController.getService().getDataLocation(list.get(i).getTblLocation().getIdlocation()));
                    TblItemLocation itemLocation = dataRoomCheckController.getService().getDataItemLocationByIDLocationAndIDItem(list.get(i).getTblLocation().getIdlocation(), dataItem.getIditem());
                    if (itemLocation == null
                            || (itemLocation.getItemQuantity().compareTo(new BigDecimal("0")) < 1)) {
                        list.remove(i);
                    }
                }
            }
        }
        return list;
    }

    private List<TblLocationOfWarehouse> loadAllDataWarehouse(TblItem dataItem) {
        List<TblLocationOfWarehouse> list = new ArrayList<>();
        if (dataItem != null) {
            list = dataRoomCheckController.getService().getAllDataWarehouse();
            for (int i = list.size() - 1; i > -1; i--) {
                list.get(i).setTblLocation(dataRoomCheckController.getService().getDataLocation(list.get(i).getTblLocation().getIdlocation()));
                TblItemLocation itemLocation = dataRoomCheckController.getService().getDataItemLocationByIDLocationAndIDItem(list.get(i).getTblLocation().getIdlocation(), dataItem.getIditem());
                if (itemLocation == null
                        || (itemLocation.getItemQuantity().compareTo(new BigDecimal("0")) < 1)) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private List<TblLocationOfLaundry> loadAllDataLaundry(TblItem dataItem) {
        List<TblLocationOfLaundry> list = new ArrayList<>();
        if (dataItem != null) {
            list = dataRoomCheckController.getService().getAllDataLaundry();
            for (int i = list.size() - 1; i > -1; i--) {
                list.get(i).setTblLocation(dataRoomCheckController.getService().getDataLocation(list.get(i).getTblLocation().getIdlocation()));
                TblItemLocation itemLocation = dataRoomCheckController.getService().getDataItemLocationByIDLocationAndIDItem(list.get(i).getTblLocation().getIdlocation(), dataItem.getIditem());
                if (itemLocation == null
                        || (itemLocation.getItemQuantity().compareTo(new BigDecimal("0")) < 1)) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private List<TblPropertyBarcode> loadAllDataPropertyBarcode(
            TblItem dataItem,
            TblLocation dataLocation) {
        List<TblPropertyBarcode> list = new ArrayList<>();
        if (dataItem != null
                && dataItem.getPropertyStatus() //Property
                && dataLocation != null) {
            TblItemLocation itemLocation = dataRoomCheckController.getService().getDataItemLocationByIDLocationAndIDItem(
                    dataLocation.getIdlocation(),
                    dataItem.getIditem());
            if (itemLocation != null) {
                List<TblItemLocationPropertyBarcode> tempList = dataRoomCheckController.getService().getAllDataItemLocationPropertyBarcodeByIDItemLocation(itemLocation.getIdrelation());
                for (TblItemLocationPropertyBarcode tempData : tempList) {
                    //data property barcode
                    TblPropertyBarcode data = dataRoomCheckController.getService().getDataPropertyBarcode(tempData.getTblPropertyBarcode().getIdbarcode());
                    data.setTblItem(dataItem);
                    data.setTblFixedTangibleAsset(dataRoomCheckController.getService().getDataFixedTangibleAsset(data.getTblFixedTangibleAsset().getIdasset()));
                    //add data property barcode to list
                    list.add(data);
                }
            }
        }
        return list;
    }

    private List<TblItemExpiredDate> loadAllDataItemExpiredDate(
            TblItem dataItem,
            TblLocation dataLocation) {
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (dataItem != null
                && dataItem.getConsumable()//Consumable
                && dataLocation != null) {
            TblItemLocation itemLocation = dataRoomCheckController.getService().getDataItemLocationByIDLocationAndIDItem(
                    dataLocation.getIdlocation(),
                    dataItem.getIditem());
            if (itemLocation != null) {
                List<TblItemLocationItemExpiredDate> tempList = dataRoomCheckController.getService().getAllDataItemLocationItemExpiredDateByIDItemLocation(itemLocation.getIdrelation());
                for (TblItemLocationItemExpiredDate tempData : tempList) {
                    //data item expired date
                    TblItemExpiredDate data = dataRoomCheckController.getService().getDataItemExpiredDate(tempData.getTblItemExpiredDate().getIditemExpiredDate());
                    data.setTblItem(dataItem);
                    //add data item expired date to list
                    list.add(data);
                }
            }
        }
        return list;
    }
    
    private void refreshDataPopup() {
        //Item
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem(cbpDataFilter.getValue()));
        cbpItem.setItems(itemItems);

//        //Data Filter
//        ObservableList<DataFilter> dataFilterItems = FXCollections.observableArrayList(loadAllDataFilter());
//        cbpDataFilter.setItems(dataFilterItems);
        //Location Type
        ObservableList<RefLocationType> locationTypeItems = FXCollections.observableArrayList(loadAllDataLocationType(cbpItem.getValue()));
        cbpLocationType.setItems(locationTypeItems);

        //Room
        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom(cbpItem.getValue()));
        cbpRoom.setItems(roomItems);

        //Warehouse
        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataWarehouse(cbpItem.getValue()));
        cbpLocationOfWarehouse.setItems(warehouseItems);

        //Laundry
        ObservableList<TblLocationOfLaundry> laundryItems = FXCollections.observableArrayList(loadAllDataLaundry(cbpItem.getValue()));
        cbpLocationOfLaundry.setItems(laundryItems);

        //Property Barcode
        ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(cbpItem.getValue(), getSourceLocation()));
        cbpPropertyBarcode.setItems(propertyBarcodeItems);

        //Item Expired Date
        ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(cbpItem.getValue(), getSourceLocation()));
        cbpItemExpiredDate.setItems(itemExpiredDateItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        cbpDataFilter.setValue(new DataFilter("Semua"));

        lblUnit.setText("");
        Bindings.bindBidirectional(txtItemQuantity.textProperty(), dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        cbpPropertyBarcode.setVisible(false);
        cbpItemExpiredDate.setVisible(false);

//        cbpLocationType.valueProperty().bindBidirectional(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().refLocationTypeProperty());
//
//        cbpItem.valueProperty().bindBidirectional(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().tblItemProperty());
        cbpItem.hide();
        cbpDataFilter.hide();
        cbpLocationType.hide();
        cbpRoom.hide();
        cbpLocationOfWarehouse.hide();
        cbpLocationOfLaundry.hide();
        cbpPropertyBarcode.hide();
        cbpItemExpiredDate.hide();
    }

    private void dataInSaveHandle() {
        if (checkDataInputDataIn()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", dataRoomCheckController.dialogStageIn);
            if (alert.getResult() == ButtonType.OK) {
                dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(getSourceLocation());
                dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().setTblItem(cbpItem.getValue());
                dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().setRefItemMutationType(dataRoomCheckController.getService().getDataMutationType(0));   //Dipindahkan '0'
                switch (dataRoomCheckController.dataInputInStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", dataRoomCheckController.dialogStageIn);
                        //data rc - itmh
                        dataRoomCheckController.tableDataIn.getTableView().getItems().add(0, dataRoomCheckController.selectedDataIn);
                        //data itmh - pb
                        if (cbpItem.getValue().getPropertyStatus()) { //Property
                            TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            itemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory());
                            itemMutationHistoryPropertyBarcode.setTblPropertyBarcode(cbpPropertyBarcode.getValue());
                            dataRoomCheckController.itemMutationHistoryPropertyBarcodes.add(itemMutationHistoryPropertyBarcode);
                        }
                        //data itmh - expired date
                        if (cbpItem.getValue().getConsumable()) { //Consumable
                            TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                            itemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory());
                            itemMutationHistoryItemExpiredDate.setTblItemExpiredDate(cbpItemExpiredDate.getValue());
                            dataRoomCheckController.itemMutationHistoryItemExpiredDates.add(itemMutationHistoryItemExpiredDate);
                        }
                        //close form data - in
                        dataRoomCheckController.dialogStageIn.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", dataRoomCheckController.dialogStageIn);
                        //data rc - itmh
                        dataRoomCheckController.tableDataIn.getTableView().getItems().remove(dataRoomCheckController.tableDataIn.getTableView().getSelectionModel().getSelectedItem());
                        dataRoomCheckController.tableDataIn.getTableView().getItems().add(dataRoomCheckController.selectedDataIn);
                        //data itmh - pb
                        for (int i = dataRoomCheckController.itemMutationHistoryPropertyBarcodes.size() - 1; i > -1; i--) {
                            if (dataRoomCheckController.itemMutationHistoryPropertyBarcodes.get(i).getTblItemMutationHistory().equals(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory())) {
                                dataRoomCheckController.itemMutationHistoryPropertyBarcodes.remove(i);
                                break;
                            }
                        }
                        if (cbpItem.getValue().getPropertyStatus()) { //Property
                            TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            itemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory());
                            itemMutationHistoryPropertyBarcode.setTblPropertyBarcode(cbpPropertyBarcode.getValue());
                            dataRoomCheckController.itemMutationHistoryPropertyBarcodes.add(itemMutationHistoryPropertyBarcode);
                        }
                        //data itmh - expired date
                        for (int i = dataRoomCheckController.itemMutationHistoryItemExpiredDates.size() - 1; i > -1; i--) {
                            if (dataRoomCheckController.itemMutationHistoryItemExpiredDates.get(i).getTblItemMutationHistory().equals(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory())) {
                                dataRoomCheckController.itemMutationHistoryItemExpiredDates.remove(i);
                                break;
                            }
                        }
                        if (cbpItem.getValue().getConsumable()) { //Consumable
                            TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                            itemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dataRoomCheckController.selectedDataIn.getTblItemMutationHistory());
                            itemMutationHistoryItemExpiredDate.setTblItemExpiredDate(cbpItemExpiredDate.getValue());
                            dataRoomCheckController.itemMutationHistoryItemExpiredDates.add(itemMutationHistoryItemExpiredDate);
                        }
                        //close form data - in
                        dataRoomCheckController.dialogStageIn.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, dataRoomCheckController.dialogStageIn);
        }
    }

    private TblLocation getSourceLocation() {
        if (cbpLocationType.getValue() != null) {
            switch (cbpLocationType.getValue().getIdtype()) {
                case 0:  //Kamar = '0'
                    if (cbpRoom.getValue() != null) {
                        return cbpRoom.getValue().getTblLocation();
                    }
                    return null;
                case 1:   //Gudang = '1'
                    if (cbpLocationOfWarehouse.getValue() != null) {
                        return cbpLocationOfWarehouse.getValue().getTblLocation();
                    }
                    return null;
                case 2: //Laundry = '2'
                    if (cbpLocationOfLaundry.getValue() != null) {
                        return cbpLocationOfLaundry.getValue().getTblLocation();
                    }
                    return null;
                default:
                    return null;
            }
        }
        return null;
    }

    private void dataInCancelHandle() {
        //close form data - in
        dataRoomCheckController.dialogStageIn.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataIn() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpDataFilter.getValue() == null) {
            dataInput = false;
            errDataInput += "Data Filter : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (cbpItem.getValue().getPropertyStatus() //Property
                    && cbpPropertyBarcode.getValue() == null) {
                dataInput = false;
                errDataInput += "Barcode : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (cbpItem.getValue().getConsumable()//Consumable
                        && cbpItemExpiredDate.getValue() == null) {
                    dataInput = false;
                    errDataInput += "Tgl. Kadarluarsa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                }
            }
        }
        if (cbpLocationType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Lokasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            switch (cbpLocationType.getValue().getIdtype()) {
                case 0:  //Kamar = '0'
                    if (cbpRoom.getValue() == null) {
                        dataInput = false;
                        errDataInput += "Kamar (Sumber) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 1:   //Gudang = '1'
                    if (cbpLocationOfWarehouse.getValue() == null) {
                        dataInput = false;
                        errDataInput += "Gudang (Sumber) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 2: //Laundry = '2'
                    if (cbpLocationOfLaundry.getValue() == null) {
                        dataInput = false;
                        errDataInput += "Laundry (Sumber) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                default:
                    dataInput = false;
                    break;
            }
        }
        if (txtItemQuantity.getText() == null
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (dataRoomCheckController.selectedDataIn.getTblItemMutationHistory().getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        return dataInput;
    }

    public class DataFilter {

        private final StringProperty filterName = new SimpleStringProperty();

        public DataFilter(String filterName) {
            this.filterName.set(filterName);
        }

        public StringProperty filterNameProperty() {
            return filterName;
        }

        public String getFilterName() {
            return filterNameProperty().get();
        }

        public void setFilterName(String filterName) {
            filterNameProperty().set(filterName);
        }

        @Override
        public String toString() {
            return getFilterName();
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
        //init form input
        initFormDataDataIn();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public DataInController(DataRoomCheckController parentController) {
        dataRoomCheckController = parentController;
    }

    private final DataRoomCheckController dataRoomCheckController;

}
