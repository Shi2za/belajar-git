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
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
public class DataOutController implements Initializable {

    @FXML
    private AnchorPane ancFormDataOut;

    @FXML
    private GridPane gpFormDataDataOut;

    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    private JFXCComboBoxTablePopup<TblPropertyBarcode> cbpPropertyBarcode;
    
    private JFXCComboBoxTablePopup<TblItemExpiredDate> cbpItemExpiredDate;

    private JFXCComboBoxTablePopup<RefItemMutationType> cbpItemMutationType;

    private JFXCComboBoxTablePopup<RefItemObsoleteBy> cbpItemObsoleteBy;

    private JFXCComboBoxTablePopup<RefLocationType> cbpLocationType;

    private JFXCComboBoxTablePopup<TblRoom> cbpRoom;

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpLocationOfWarehouse;

    private JFXCComboBoxTablePopup<TblLocationOfLaundry> cbpLocationOfLaundry;

    private JFXCComboBoxTablePopup<TblLocationOfBin> cbpLocationOfBin;

    @FXML
    private AnchorPane ancLocationTypeLayout;

    @FXML
    private AnchorPane ancItemObsoleteByLayout;

    @FXML
    private AnchorPane ancRoomLayout;

    @FXML
    private AnchorPane ancWarehouseLayout;

    @FXML
    private AnchorPane ancLaundryLayout;

    @FXML
    private AnchorPane ancBinLayout;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private Label lblUnit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDataOut() {
        initDataOutPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataOutSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataOutCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();

        ancItemObsoleteByLayout.setVisible(false);

        ancRoomLayout.setVisible(false);
        ancWarehouseLayout.setVisible(false);
        ancLaundryLayout.setVisible(false);
        ancBinLayout.setVisible(false);

        cbpItem.valueProperty().addListener((obs, oldval, newVal) -> {
            lblUnit.setText("");
            txtItemQuantity.setEditable(true);
            if (newVal != null) {
                if (newVal.getPropertyStatus()) {   //Property
                    ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(newVal));
                    cbpPropertyBarcode.setItems(propertyBarcodeItems);
                    cbpPropertyBarcode.setValue(null);
                    dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().setItemQuantity(new BigDecimal("1"));
                    txtItemQuantity.setEditable(false);
                } else {
                    if (newVal.getConsumable()) { //consumable
                        ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(newVal));
                        cbpItemExpiredDate.setItems(itemExpiredDateItems);
                        cbpItemExpiredDate.setValue(null);
                        dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                        txtItemQuantity.setEditable(true);
                    }
                }
                lblUnit.setText(newVal.getTblUnit().getUnitName());
            }
            refreshDataPropertyBarcodeAndItemExpiredDateSelectedPopup(newVal,
                    cbpLocationOfBin.getValue());
        });

        cbpItemMutationType.valueProperty().addListener((obs, oldVal, newVal) -> {
            ancItemObsoleteByLayout.setVisible(false);
            ancRoomLayout.setVisible(false);
            ancWarehouseLayout.setVisible(false);
            ancLaundryLayout.setVisible(false);
            ancBinLayout.setVisible(false);
            //Layout - Location Type - Location (Bin)
            ancLocationTypeLayout.setDisable(false);
            ancBinLayout.setDisable(false);
            if (newVal != null) {
                switch (newVal.getIdtype()) {
                    case 0:   //Dipindahkan = '0'
                        //Location Type
                        ObservableList<RefLocationType> locationTypeItems = FXCollections.observableArrayList(loadAllDataLocationType());
                        cbpLocationType.setItems(locationTypeItems);
                        cbpLocationType.setValue(null);
                        break;
                    case 2: //Rusak = '2'
                        //Item Obsolete By
                        ObservableList<RefItemObsoleteBy> itemObsoleteByItems = FXCollections.observableArrayList(loadAllDataItemObsoleteBy());
                        cbpItemObsoleteBy.setItems(itemObsoleteByItems);
                        cbpItemObsoleteBy.setValue(null);
                        //Layout
                        ancItemObsoleteByLayout.setVisible(true);
                        //Layout - Location Type - Location (Bin)
                        ancLocationTypeLayout.setDisable(true);
                        cbpLocationType.setValue(dataRoomCheckController.getService().getDataLocationType(4)); //Bin = '4'                        
                        ancBinLayout.setDisable(true);
                        break;
                    default:
                        break;
                }
            }
        });

        cbpLocationType.valueProperty().addListener((obs, oldVal, newVal) -> {
            cbpRoom.setValue(null);
            cbpLocationOfWarehouse.setValue(null);
            cbpLocationOfLaundry.setValue(null);
            cbpLocationOfBin.setValue(null);
            ancRoomLayout.setVisible(false);
            ancWarehouseLayout.setVisible(false);
            ancLaundryLayout.setVisible(false);
            ancBinLayout.setVisible(false);
            if (newVal != null) {
                switch (newVal.getIdtype()) {
                    case 0:  //Kamar = '0'
                        //Room
                        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());
                        cbpRoom.setItems(roomItems);
                        //Layout
                        ancRoomLayout.setVisible(true);
                        ancRoomLayout.toFront();
                        break;
                    case 1:   //Gudang = '1'
                        //Warehouse
                        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataWarehouse());
                        cbpLocationOfWarehouse.setItems(warehouseItems);
                        //Layout
                        ancWarehouseLayout.setVisible(true);
                        ancWarehouseLayout.toFront();
                        break;
                    case 2: //Laundry = '2'
                        //Laundry
                        ObservableList<TblLocationOfLaundry> laundryItems = FXCollections.observableArrayList(loadAllDataLaundry());
                        cbpLocationOfLaundry.setItems(laundryItems);
                        //Layout
                        ancLaundryLayout.setVisible(true);
                        ancLaundryLayout.toFront();
                        break;
                    case 3:    //Supplier = '3'
                        break;
                    case 4: //Bin = '4'
                        //Bin
                        ObservableList<TblLocationOfBin> binItems = FXCollections.observableArrayList(loadAllDataBin());
                        cbpLocationOfBin.setItems(binItems);
                        if (cbpItemMutationType.getValue() != null
                                && cbpItemMutationType.getValue().getIdtype() == 2) {    //Rusak = '2'
                            cbpLocationOfBin.setValue(dataRoomCheckController.getService().getDataBin());
                        }
                        //Layout
                        ancBinLayout.setVisible(true);
                        ancBinLayout.toFront();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpItem,
                cbpPropertyBarcode,
                cbpItemExpiredDate,
                cbpItemMutationType,
                cbpItemObsoleteBy,
                cbpLocationType,
                cbpRoom,
                cbpLocationOfWarehouse,
                cbpLocationOfLaundry,
                cbpLocationOfBin,
                txtItemQuantity);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }

    private void refreshDataPropertyBarcodeAndItemExpiredDateSelectedPopup(
            TblItem dataItem,
            TblLocationOfBin dataBin) {
        if (dataItem != null) {
            if (dataItem.getPropertyStatus()) { //Property
                cbpPropertyBarcode.setVisible(true);
                cbpItemExpiredDate.setVisible(false);
            } else {
                if (dataItem.getConsumable()) {   //consumable
                    cbpPropertyBarcode.setVisible(false);
                cbpItemExpiredDate.setVisible(true);
                }
            }
        }
    }

    private void initDataOutPopup() {
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

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 300, 300
        );

        //Property Barcode
        TableView<TblPropertyBarcode> tablePropertyBarcode = new TableView<>();

        TableColumn<TblPropertyBarcode, String> barcode = new TableColumn("Barcode");
        barcode.setCellValueFactory((TableColumn.CellDataFeatures<TblPropertyBarcode, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeBarcode(),
                        param.getValue().codeBarcodeProperty()));
        barcode.setMinWidth(120);

        tablePropertyBarcode.getColumns().addAll(barcode);

        ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(cbpItem.getValue()));

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

        ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(cbpItem.getValue()));

        cbpItemExpiredDate = new JFXCComboBoxTablePopup<>(
                TblItemExpiredDate.class, tableItemExpiredDate, itemExpiredDateItems, "", "Tgl. Kadarluarsa *", true, 140, 250
        );
        
        //Item Mutation Type
        TableView<RefItemMutationType> tableItemMutationType = new TableView<>();

        TableColumn<RefItemMutationType, String> itemMutationTypeName = new TableColumn<>("Status");
        itemMutationTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        itemMutationTypeName.setMinWidth(140);

        tableItemMutationType.getColumns().addAll(itemMutationTypeName);

        ObservableList<RefItemMutationType> itemMutationTypeItems = FXCollections.observableArrayList(loadAllDataItemMutationType());

        cbpItemMutationType = new JFXCComboBoxTablePopup<>(
                RefItemMutationType.class, tableItemMutationType, itemMutationTypeItems, "", "Status *", true, 200, 200
        );

        //Item Obsolete Type
        TableView<RefItemObsoleteBy> tableItemObsoleteBy = new TableView<>();

        TableColumn<RefItemObsoleteBy, String> itemObsoleteByName = new TableColumn<>("Rusak Oleh");
        itemObsoleteByName.setCellValueFactory(cellData -> cellData.getValue().obsoleteByNameProperty());
        itemObsoleteByName.setMinWidth(140);

        tableItemObsoleteBy.getColumns().addAll(itemObsoleteByName);

        ObservableList<RefItemObsoleteBy> itemObsoleteByItems = FXCollections.observableArrayList(loadAllDataItemObsoleteBy());

        cbpItemObsoleteBy = new JFXCComboBoxTablePopup<>(
                RefItemObsoleteBy.class, tableItemObsoleteBy, itemObsoleteByItems, "", "Rusak Oleh *", true, 200, 200
        );

        //Location Type
        TableView<RefLocationType> tableLocationtype = new TableView<>();

        TableColumn<RefLocationType, String> locationTypeName = new TableColumn<>("Tipe Lokasi");
        locationTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        locationTypeName.setMinWidth(140);

        tableLocationtype.getColumns().addAll(locationTypeName);

        ObservableList<RefLocationType> locationTypeItems = FXCollections.observableArrayList(loadAllDataLocationType());

        cbpLocationType = new JFXCComboBoxTablePopup<>(
                RefLocationType.class, tableLocationtype, locationTypeItems, "", "Tipe Lokasi *", true, 200, 200
        );

        //Room
        TableView<TblRoom> tableRoom = new TableView<>();

        TableColumn<TblRoom, String> roomName = new TableColumn<>("Kamar");
        roomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        roomName.setMinWidth(140);

        tableRoom.getColumns().addAll(roomName);

        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());

        cbpRoom = new JFXCComboBoxTablePopup<>(
                TblRoom.class, tableRoom, roomItems, "", "Kamar *", true, 160, 200
        );

        //Warehouse
        TableView<TblLocationOfWarehouse> tableWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn<>("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(140);

        tableWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataWarehouse());

        cbpLocationOfWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableWarehouse, warehouseItems, "", "Gudang *", true, 160, 200
        );

        //Laundry
        TableView<TblLocationOfLaundry> tableLaundry = new TableView<>();

        TableColumn<TblLocationOfLaundry, String> laundryName = new TableColumn<>("Laundry");
        laundryName.setCellValueFactory(cellData -> cellData.getValue().laundryNameProperty());
        laundryName.setMinWidth(140);

        tableLaundry.getColumns().addAll(laundryName);

        ObservableList<TblLocationOfLaundry> laundryItems = FXCollections.observableArrayList(loadAllDataLaundry());

        cbpLocationOfLaundry = new JFXCComboBoxTablePopup<>(
                TblLocationOfLaundry.class, tableLaundry, laundryItems, "", "Laundry *", true, 160, 200
        );

        //Bin
        TableView<TblLocationOfBin> tableBin = new TableView<>();

        TableColumn<TblLocationOfBin, String> binName = new TableColumn<>("Bin");
        binName.setCellValueFactory(cellData -> cellData.getValue().binNameProperty());
        binName.setMinWidth(140);

        tableBin.getColumns().addAll(binName);

        ObservableList<TblLocationOfBin> binItems = FXCollections.observableArrayList(loadAllDataBin());

        cbpLocationOfBin = new JFXCComboBoxTablePopup<>(
                TblLocationOfBin.class, tableBin, binItems, "", "Bin *", true, 160, 200
        );

        //attached to grid-pane
        gpFormDataDataOut.add(cbpItemMutationType, 0, 1);

        AnchorPane.setBottomAnchor(cbpItemObsoleteBy, 0.0);
        AnchorPane.setLeftAnchor(cbpItemObsoleteBy, 0.0);
        AnchorPane.setRightAnchor(cbpItemObsoleteBy, 0.0);
        AnchorPane.setTopAnchor(cbpItemObsoleteBy, 0.0);
        ancItemObsoleteByLayout.getChildren().add(cbpItemObsoleteBy);

        AnchorPane.setBottomAnchor(cbpLocationType, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationType, 0.0);
        AnchorPane.setRightAnchor(cbpLocationType, 0.0);
        AnchorPane.setTopAnchor(cbpLocationType, 0.0);
        ancLocationTypeLayout.getChildren().add(cbpLocationType);

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

        AnchorPane.setBottomAnchor(cbpLocationOfBin, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationOfBin, 0.0);
        AnchorPane.setRightAnchor(cbpLocationOfBin, 0.0);
        AnchorPane.setTopAnchor(cbpLocationOfBin, 0.0);
        ancBinLayout.getChildren().add(cbpLocationOfBin);

        gpFormDataDataOut.add(cbpItem, 0, 0);
        gpFormDataDataOut.add(cbpPropertyBarcode, 1, 0);
        gpFormDataDataOut.add(cbpItemExpiredDate, 1, 0);
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list = new ArrayList<>();
        List<TblItemLocation> tempList = dataRoomCheckController.getService().getAllDataItemLocationByIDLocation(dataRoomCheckController.selectedDataOut.getTblRoomCheck().getTblRoom().getTblLocation().getIdlocation());
        for (TblItemLocation tempData : tempList) {
            if ((tempData.getItemQuantity().compareTo(new BigDecimal("0")) == 1) //item quantity must be greater than '0'
                    && tempData.getTblItem().getGuestStatus()) {
                //data item
                TblItem data = dataRoomCheckController.getService().getDataItem(tempData.getTblItem().getIditem());
                data.setTblUnit(dataRoomCheckController.getService().getDataUnit(data.getTblUnit().getIdunit()));
                if (data.getTblItemTypeHk() != null) {
                    data.setTblItemTypeHk(dataRoomCheckController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
                }
                if (data.getTblItemTypeWh() != null) {
                    data.setTblItemTypeWh(dataRoomCheckController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
                }
                //add data item to list
                list.add(data);
            }
        }
        return list;
    }

    private List<TblPropertyBarcode> loadAllDataPropertyBarcode(TblItem dataItem) {
        List<TblPropertyBarcode> list = new ArrayList<>();
        if (dataItem != null
                && dataItem.getPropertyStatus()) { //Property
            TblItemLocation itemLocation = dataRoomCheckController.getService().getDataItemLocationByIDLocationAndIDItem(
                    dataRoomCheckController.selectedData.getTblRoom().getTblLocation().getIdlocation(),
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

    private List<TblItemExpiredDate> loadAllDataItemExpiredDate(TblItem dataItem) {
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (dataItem != null
                && dataItem.getConsumable()) { //Consumable
            TblItemLocation itemLocation = dataRoomCheckController.getService().getDataItemLocationByIDLocationAndIDItem(
                    dataRoomCheckController.selectedData.getTblRoom().getTblLocation().getIdlocation(),
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
    
    private List<RefItemMutationType> loadAllDataItemMutationType() {
        List<RefItemMutationType> list = dataRoomCheckController.getService().getAllDataItemMutationType();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdtype() == 1) { //Habis = '1'
                list.remove(i);
            }
        }
        return list;
    }

    private List<RefItemObsoleteBy> loadAllDataItemObsoleteBy() {
        List<RefItemObsoleteBy> list = dataRoomCheckController.getService().getAllDataItemObsoleteBy();
        return list;
    }

    private List<RefLocationType> loadAllDataLocationType() {
        List<RefLocationType> list = dataRoomCheckController.getService().getAllDataLocationType();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdtype() == 3 //Supplier = '3'
                    || list.get(i).getIdtype() == 5) { //StockOpname = '5'
                list.remove(i);
            }
        }
        return list;
    }

    private List<TblRoom> loadAllDataRoom() {
        List<TblRoom> list = dataRoomCheckController.getService().getAllDataRoom();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdroom()
                    == dataRoomCheckController.selectedData.getTblRoom().getIdroom()) {
                list.remove(i);
            } else {
                list.get(i).setTblLocation(dataRoomCheckController.getService().getDataLocation(list.get(i).getTblLocation().getIdlocation()));
            }
        }
        return list;
    }

    private List<TblLocationOfWarehouse> loadAllDataWarehouse() {
        List<TblLocationOfWarehouse> list = dataRoomCheckController.getService().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            data.setTblLocation(dataRoomCheckController.getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private List<TblLocationOfLaundry> loadAllDataLaundry() {
        List<TblLocationOfLaundry> list = dataRoomCheckController.getService().getAllDataLaundry();
        for (TblLocationOfLaundry data : list) {
            data.setTblLocation(dataRoomCheckController.getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private List<TblLocationOfBin> loadAllDataBin() {
        List<TblLocationOfBin> list = dataRoomCheckController.getService().getAllDataBin();
        for (TblLocationOfBin data : list) {
            data.setTblLocation(dataRoomCheckController.getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private void refreshDataPopup() {
        //Item
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());
        cbpItem.setItems(itemItems);

        //Property Barcode
        ObservableList<TblPropertyBarcode> propertyBarcodeItems = FXCollections.observableArrayList(loadAllDataPropertyBarcode(cbpItem.getValue()));
        cbpPropertyBarcode.setItems(propertyBarcodeItems);

        //Item Expired Date
        ObservableList<TblItemExpiredDate> itemExpiredDateItems = FXCollections.observableArrayList(loadAllDataItemExpiredDate(cbpItem.getValue()));
        cbpItemExpiredDate.setItems(itemExpiredDateItems);
        
        //Item Mutation Type
        ObservableList<RefItemMutationType> itemMutationTypeItems = FXCollections.observableArrayList(loadAllDataItemMutationType());
        cbpItemMutationType.setItems(itemMutationTypeItems);

        //Item Obsolete By
        ObservableList<RefItemObsoleteBy> itemObsoleteByItems = FXCollections.observableArrayList(loadAllDataItemObsoleteBy());
        cbpItemObsoleteBy.setItems(itemObsoleteByItems);

        //Location Type
        ObservableList<RefLocationType> locationTypeItems = FXCollections.observableArrayList(loadAllDataLocationType());
        cbpLocationType.setItems(locationTypeItems);

        //Room
        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());
        cbpRoom.setItems(roomItems);

        //Warehouse
        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataWarehouse());
        cbpLocationOfWarehouse.setItems(warehouseItems);

        //Laundry
        ObservableList<TblLocationOfLaundry> laundryItems = FXCollections.observableArrayList(loadAllDataLaundry());
        cbpLocationOfLaundry.setItems(laundryItems);

        //Bin
        ObservableList<TblLocationOfBin> binItems = FXCollections.observableArrayList(loadAllDataBin());
        cbpLocationOfBin.setItems(binItems);

    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        lblUnit.setText("");
        Bindings.bindBidirectional(txtItemQuantity.textProperty(), dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        cbpPropertyBarcode.setVisible(false);
        cbpItemExpiredDate.setVisible(false);

        cbpItem.hide();
        cbpPropertyBarcode.hide();
        cbpItemExpiredDate.hide();
        cbpItemMutationType.hide();
        cbpItemObsoleteBy.hide();
        cbpLocationType.hide();
        cbpRoom.hide();
        cbpLocationOfWarehouse.hide();
        cbpLocationOfLaundry.hide();
        cbpLocationOfBin.hide();
    }

    private void dataOutSaveHandle() {
        if (checkDataOutputDataOut()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", dataRoomCheckController.dialogStageOut);
            if (alert.getResult() == ButtonType.OK) {
                dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().setRefItemMutationType(cbpItemMutationType.getValue());
                if (cbpItemMutationType.getValue().getIdtype() == 2) {  //Rusak = '2'
                    dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().setRefItemObsoleteBy(cbpItemObsoleteBy.getValue());
                    TblLocationOfBin dataBin = dataRoomCheckController.getService().getDataBin();
                    if (dataBin != null) {
                        dataBin.setTblLocation(dataRoomCheckController.getService().getDataLocation(dataBin.getTblLocation().getIdlocation()));
                        dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(dataBin.getTblLocation());
                    }
                } else { //Moved
                    dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(getDestinationLocation());
                }
                dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().setTblItem(cbpItem.getValue());
                switch (dataRoomCheckController.dataInputInStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", dataRoomCheckController.dialogStageOut);
                        //data rc - itmh
                        dataRoomCheckController.tableDataOut.getTableView().getItems().add(0, dataRoomCheckController.selectedDataOut);
                        //data itmh - pb
                        if (cbpItem.getValue().getPropertyStatus()) { //Property
                            TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            itemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dataRoomCheckController.selectedDataOut.getTblItemMutationHistory());
                            itemMutationHistoryPropertyBarcode.setTblPropertyBarcode(cbpPropertyBarcode.getValue());
                            dataRoomCheckController.itemMutationHistoryPropertyBarcodes.add(itemMutationHistoryPropertyBarcode);
                        }
                        //data itmh - expired date
                        if (cbpItem.getValue().getConsumable()) { //Consumable
                            TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                            itemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dataRoomCheckController.selectedDataOut.getTblItemMutationHistory());
                            itemMutationHistoryItemExpiredDate.setTblItemExpiredDate(cbpItemExpiredDate.getValue());
                            dataRoomCheckController.itemMutationHistoryItemExpiredDates.add(itemMutationHistoryItemExpiredDate);
                        }
                        //close form data - out
                        dataRoomCheckController.dialogStageOut.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", dataRoomCheckController.dialogStageOut);
                        //data rc - itmh
                        dataRoomCheckController.tableDataOut.getTableView().getItems().remove(dataRoomCheckController.tableDataOut.getTableView().getSelectionModel().getSelectedItem());
                        dataRoomCheckController.tableDataOut.getTableView().getItems().add(dataRoomCheckController.selectedDataOut);
                        //data itmh - pb
                        for (int i = dataRoomCheckController.itemMutationHistoryPropertyBarcodes.size() - 1; i > -1; i--) {
                            if (dataRoomCheckController.itemMutationHistoryPropertyBarcodes.get(i).getTblItemMutationHistory().equals(dataRoomCheckController.selectedDataOut.getTblItemMutationHistory())) {
                                dataRoomCheckController.itemMutationHistoryPropertyBarcodes.remove(i);
                                break;
                            }
                        }
                        if (cbpItem.getValue().getPropertyStatus()) { //Property
                            TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            itemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dataRoomCheckController.selectedDataOut.getTblItemMutationHistory());
                            itemMutationHistoryPropertyBarcode.setTblPropertyBarcode(cbpPropertyBarcode.getValue());
                            dataRoomCheckController.itemMutationHistoryPropertyBarcodes.add(itemMutationHistoryPropertyBarcode);
                        }
                        //data itmh - expired date
                        for (int i = dataRoomCheckController.itemMutationHistoryItemExpiredDates.size() - 1; i > -1; i--) {
                            if (dataRoomCheckController.itemMutationHistoryItemExpiredDates.get(i).getTblItemMutationHistory().equals(dataRoomCheckController.selectedDataOut.getTblItemMutationHistory())) {
                                dataRoomCheckController.itemMutationHistoryItemExpiredDates.remove(i);
                                break;
                            }
                        }
                        if (cbpItem.getValue().getConsumable()) { //Consumable
                            TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                            itemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dataRoomCheckController.selectedDataOut.getTblItemMutationHistory());
                            itemMutationHistoryItemExpiredDate.setTblItemExpiredDate(cbpItemExpiredDate.getValue());
                            dataRoomCheckController.itemMutationHistoryItemExpiredDates.add(itemMutationHistoryItemExpiredDate);
                        }
                        //close form data - out
                        dataRoomCheckController.dialogStageOut.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, dataRoomCheckController.dialogStageOut);
        }
    }

    private TblLocation getDestinationLocation() {
        switch (cbpLocationType.getValue().getIdtype()) {
            case 0:  //Kamar = '0'
                return cbpRoom.getValue().getTblLocation();
            case 1:   //Gudang = '1'
                return cbpLocationOfWarehouse.getValue().getTblLocation();
            case 2: //Laundry = '2'
                return cbpLocationOfLaundry.getValue().getTblLocation();
            case 3:    //Supplier = '3'
                return null;
            case 4: //Bin = '4'
                return cbpLocationOfBin.getValue().getTblLocation();
            default:
                return null;
        }
    }

    private void dataOutCancelHandle() {
        //close form data - out
        dataRoomCheckController.dialogStageOut.close();
    }

    private String errDataInput;

    private boolean checkDataOutputDataOut() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (cbpItem.getValue().getPropertyStatus()) {   //Property
                if (cbpPropertyBarcode.getValue() == null) {
                    dataInput = false;
                    errDataInput += "Barcode : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                }
            } else {
                if (cbpItem.getValue().getConsumable()) {   //Consumable
                    if (cbpItemExpiredDate.getValue() == null) {
                        dataInput = false;
                        errDataInput += "Tgl. Kadarluarsa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                }
            }
        }
        if (cbpItemMutationType.getValue() == null) {
            dataInput = false;
            errDataInput += "Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (cbpItemMutationType.getValue().getIdtype() == 2) {  //Rusak = '2'
                if (cbpItemObsoleteBy.getValue() == null) {
                    dataInput = false;
                    errDataInput += "Rusak Oleh : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                }
            } else {
                if (cbpLocationType.getValue() == null) {
                    dataInput = false;
                    errDataInput += "Tipe Lokasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                } else {
                    switch (cbpLocationType.getValue().getIdtype()) {
                        case 0: //Kamar = '0'
                            if (cbpRoom.getValue() == null) {
                                dataInput = false;
                                errDataInput += "Kamar (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                            }
                            break;
                        case 1:   //Gudang = '1'
                            if (cbpLocationOfWarehouse.getValue() == null) {
                                dataInput = false;
                                errDataInput += "Gudang (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                            }
                            break;
                        case 2: //Laundry = '2'
                            if (cbpLocationOfLaundry.getValue() == null) {
                                dataInput = false;
                                errDataInput += "Laundry (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                            }
                            break;
                        case 3: //Supplier = '3'
                            dataInput = false;
                            break;
                        case 4: //Bin = '4'
                            if (cbpLocationOfBin.getValue() == null) {
                                dataInput = false;
                                errDataInput += "Bin (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                            }
                            break;
                        default:
                            dataInput = false;
                            break;
                    }
                }
            }
        }
        if (txtItemQuantity.getText() == null
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (dataRoomCheckController.selectedDataOut.getTblItemMutationHistory().getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
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
        //init form input
        initFormDataDataOut();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public DataOutController(DataRoomCheckController parentController) {
        dataRoomCheckController = parentController;
    }

    private final DataRoomCheckController dataRoomCheckController;

}
