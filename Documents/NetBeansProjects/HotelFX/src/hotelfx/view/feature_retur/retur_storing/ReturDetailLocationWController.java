/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_storing;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailPropertyBarcode;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
public class ReturDetailLocationWController implements Initializable {

    @FXML
    private AnchorPane ancFormDetailLocation;

    @FXML
    private GridPane gpFormDataDetailLocation;

    @FXML
    private JFXTextField txtItemTypeName;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtPropertyBarcode;

    @FXML
    private JFXTextField txtExpiredDate;

//    @FXML
//    private JFXTextField txtItemCost;
//
//    @FXML
//    private JFXTextField txtItemDiscount;
    @FXML
    private JFXTextField txtItemReceivedQuantity;

    @FXML
    private JFXTextField txtItemReturedQuantity;

    @FXML
    private JFXTextField txtItemQuantity;

//    @FXML
//    private JFXTextField txtReturCost;
    @FXML
    private AnchorPane ancMIDetailItemLayout;

    @FXML
    private AnchorPane ancLocationLayout;

    private JFXCComboBoxTablePopup<MIDetail> cbpMIDetail;

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpWarehouse;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetailLocation() {
        initDataDetailPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpMIDetail,
                txtItemQuantity,
                cbpWarehouse);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }

    private void initDataDetailPopup() {
        //mi - detail (data item)
        TableView<MIDetail> tableMIDetail = new TableView<>();

        TableColumn<MIDetail, String> codeItem = new TableColumn<>("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataMIDetail() != null)
                                ? param.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getCodeItem()
                                : "-", param.getValue().dataMIDetailProperty()));
        codeItem.setMinWidth(100);

        TableColumn<MIDetail, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataMIDetail() != null)
                                ? param.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getItemName()
                                : "-", param.getValue().dataMIDetailProperty()));
        itemName.setMinWidth(140);

        TableColumn<MIDetail, String> itemTypeHK = new TableColumn<>("Tipe");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataMIDetail() != null)
                                ? (param.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getTblItemTypeHk() != null
                                        ? param.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                                : "-",
                        param.getValue().dataMIDetailProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<MIDetail, String> itemTypeWH = new TableColumn<>("Tipe");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataMIDetail() != null)
                                ? (param.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getTblItemTypeWh() != null
                                        ? param.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                                : "-",
                        param.getValue().dataMIDetailProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<MIDetail, String> propertyBarcode = new TableColumn<>("Barcode");
        propertyBarcode.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataPropertyBarcode() != null)
                                ? param.getValue().getDataPropertyBarcode().getTblPropertyBarcode().getCodeBarcode()
                                : "-", param.getValue().dataPropertyBarcodeProperty()));
        propertyBarcode.setMinWidth(100);

        TableColumn<MIDetail, String> expiredDate = new TableColumn<>("Tgl. Kadarluarsa");
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataItemExpiredDate() != null)
                                ? ClassFormatter.dateFormate.format(param.getValue().getDataItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate())
                                : "-", param.getValue().dataItemExpiredDateProperty()));
        expiredDate.setMinWidth(120);

        TableColumn<MIDetail, String> itemReceivedQuantity = new TableColumn<>("Diterima");
        itemReceivedQuantity.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemReceivedQuantity()),
                        param.getValue().itemReceivedQuantityProperty()));
        itemReceivedQuantity.setMinWidth(100);

        TableColumn<MIDetail, String> itemReturedQuantity = new TableColumn<>("Di-retur");
        itemReturedQuantity.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemReturedQuantity()),
                        param.getValue().itemReturedQuantityProperty()));
        itemReturedQuantity.setMinWidth(100);

        TableColumn<MIDetail, String> quantityTitle = new TableColumn("Jumlah");
        quantityTitle.getColumns().addAll(itemReceivedQuantity, itemReturedQuantity);

        tableMIDetail.getColumns().addAll(codeItem, itemName, itemTypeWH, propertyBarcode, expiredDate, quantityTitle);

        ObservableList<MIDetail> miDetailItems = FXCollections.observableArrayList(loadAllDataMIDetail(null));

        cbpMIDetail = new JFXCComboBoxTablePopup<>(
                MIDetail.class, tableMIDetail, miDetailItems, "", "ID Barang *", true, 830, 300
        );

        //Locaton (Warehouse)
        TableView<TblLocationOfWarehouse> tableWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn<>("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(140);

        tableWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> loacationOfWarehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());

        cbpWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableWarehouse, loacationOfWarehouseItems, "", "Lokasi *", true, 200, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpMIDetail, 0.0);
        AnchorPane.setLeftAnchor(cbpMIDetail, 0.0);
        AnchorPane.setRightAnchor(cbpMIDetail, 0.0);
        AnchorPane.setTopAnchor(cbpMIDetail, 0.0);
        ancMIDetailItemLayout.getChildren().add(cbpMIDetail);
        AnchorPane.setBottomAnchor(cbpWarehouse, 0.0);
        AnchorPane.setLeftAnchor(cbpWarehouse, 0.0);
        AnchorPane.setRightAnchor(cbpWarehouse, 0.0);
        AnchorPane.setTopAnchor(cbpWarehouse, 0.0);
        ancLocationLayout.getChildren().add(cbpWarehouse);
    }

    private List<MIDetail> loadAllDataMIDetail(TblMemorandumInvoice dataMI) {
        List<MIDetail> list = new ArrayList<>();
        if (dataMI != null) {
            List<TblMemorandumInvoiceDetail> miDetails = returWController.getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(dataMI.getIdmi());
            for (TblMemorandumInvoiceDetail miDetail : miDetails) {
                if (miDetail.getTblSupplierItem() != null) {   //not bonus
                    miDetail.setTblMemorandumInvoice(dataMI);
                    miDetail.setTblSupplierItem(returWController.getService().getDataSupplierItem(miDetail.getTblSupplierItem().getIdrelation()));
                    miDetail.getTblSupplierItem().setTblItem(returWController.getService().getDataItem(miDetail.getTblSupplierItem().getTblItem().getIditem()));
                    if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                        miDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(returWController.getService().getDataItemTypeHK(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                        miDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(returWController.getService().getDataItemTypeWH(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                    if (miDetail.getTblSupplierItem().getTblItem().getConsumable()) {  //consumable
                        List<TblMemorandumInvoiceDetailItemExpiredDate> dataItemExpiredDates = returWController.getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                        for (TblMemorandumInvoiceDetailItemExpiredDate dataItemExpiredDate : dataItemExpiredDates) {
                            dataItemExpiredDate.setTblMemorandumInvoiceDetail(miDetail);
                            dataItemExpiredDate.setTblItemExpiredDate(returWController.getService().getDataItemExpiredDate(dataItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                            dataItemExpiredDate.getTblItemExpiredDate().setTblItem(returWController.getService().getDataItem(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getIditem()));
                            if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk() != null) {
                                dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeHk(returWController.getService().getDataItemTypeHK(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                            }
                            if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh() != null) {
                                dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeWh(returWController.getService().getDataItemTypeWH(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                            }
                            TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                            list.add(new MIDetail(miDetail, null, dataItemExpiredDate, miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null, dataItemExpiredDate),
                                    poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                    poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                        }
                    } else {
                        if (miDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property
                            List<TblMemorandumInvoiceDetailPropertyBarcode> dataPBs = returWController.getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                            for (TblMemorandumInvoiceDetailPropertyBarcode dataPB : dataPBs) {
                                dataPB.setTblMemorandumInvoiceDetail(miDetail);
                                dataPB.setTblPropertyBarcode(new TblPropertyBarcode(returWController.getService().getDataPropertyBarcode(dataPB.getTblPropertyBarcode().getIdbarcode())));
                                TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                                list.add(new MIDetail(miDetail, dataPB, null, new BigDecimal("1"), getItemReturedQuantity(miDetail, dataPB, null),
                                        poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                        poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                            }
                        } else {
                            TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                            list.add(new MIDetail(miDetail, null, null, miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null, null),
                                    poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                    poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                        }
                    }
                }
            }
            //remove data has been used
            for (int i = list.size() - 1; i > -1; i--) {
                for (ReturWController.DetailLocation data : returWController.detailLocations) {
                    if (data.getTblDetail().getTblMemorandumInvoice().getIdmi() == list.get(i).getDataMIDetail().getTblMemorandumInvoice().getIdmi()
                            && data.getTblDetail().getTblSupplierItem().getIdrelation() == list.get(i).getDataMIDetail().getTblSupplierItem().getIdrelation()) {
                        if ((list.get(i).getDataMIDetail().getTblSupplierItem().getTblItem().getPropertyStatus())) {   //Property
                            //same property
                            if (data.getTblDetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode()
                                    == list.get(i).getDataPropertyBarcode().getTblPropertyBarcode().getIdbarcode()) {
                                list.remove(i);
                                break;
                            }
                        } else {
                            if ((list.get(i).getDataMIDetail().getTblSupplierItem().getTblItem().getConsumable())) {  //Consumable
                                //same item expired date
                                if (data.getTblDetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate()
                                        == list.get(i).getDataItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate()) {
                                    list.remove(i);
                                    break;
                                }
                            } else {
                                //not property barccode & not consumable
                                list.remove(i);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private BigDecimal getItemReturedQuantity(TblMemorandumInvoiceDetail dataMIDetail,
            TblMemorandumInvoiceDetailPropertyBarcode dataMIDetailPO,
            TblMemorandumInvoiceDetailItemExpiredDate dataMIDetailIED) {
        BigDecimal result = new BigDecimal("0");
        if (dataMIDetail != null) {
            if (dataMIDetail.getTblSupplierItem().getTblItem().getConsumable()) {    //Consumable
                if (dataMIDetailIED != null) {
                    List<TblReturDetail> dataReturDetails = returWController.getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataMIDetail.getTblMemorandumInvoice().getIdmi(),
                            dataMIDetail.getTblSupplierItem().getIdrelation());
                    for (TblReturDetail dataReturDetail : dataReturDetails) {
                        if (dataReturDetail.getTblRetur().getRefReturStatus().getIdstatus() == 1) {   //Disetujui = '1'
                            List<TblReturDetailItemExpiredDate> returDetailItemExpiredDates = returWController.getService().getAllDataReturDetailItemExpiredDateByIDReturDetailAndIDItemExpiredDate(
                                    dataReturDetail.getIddetailRetur(),
                                    dataMIDetailIED.getTblItemExpiredDate().getIditemExpiredDate());
                            for (TblReturDetailItemExpiredDate returDetailItemExpiredDate : returDetailItemExpiredDates) {
                                result = result.add(returDetailItemExpiredDate.getItemQuantity());
                            }
                        }
                    }
                }
            } else {
                if (dataMIDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) {    //Property
                    if (dataMIDetailPO != null) {
                        TblReturDetailPropertyBarcode dataReturDetailPropertyBarcode = returWController.getService().getDataReturDetailPropertyBarcodeByIDPropertyBarcode(dataMIDetailPO.getTblPropertyBarcode().getIdbarcode());
                        if (dataReturDetailPropertyBarcode != null) {
                            result = new BigDecimal("1");
                        }
                    }
                } else {
                    List<TblReturDetail> dataReturDetails = returWController.getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataMIDetail.getTblMemorandumInvoice().getIdmi(),
                            dataMIDetail.getTblSupplierItem().getIdrelation());
                    for (TblReturDetail dataReturDetail : dataReturDetails) {
                        if (dataReturDetail.getTblRetur().getRefReturStatus().getIdstatus() == 1) {   //Disetujui = '1'
                            result = result.add(dataReturDetail.getItemQuantity());
                        }
                    }
                }
            }
        }
        return result;
    }

    public TblPurchaseOrderDetail getDataPurchaseOrderDetail(TblMemorandumInvoiceDetail dataMIDetail) {
        TblPurchaseOrderDetail dataPODetail = null;
        if (dataMIDetail != null) {
            dataPODetail = returWController.getService().getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(
                    dataMIDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataMIDetail.getTblSupplierItem().getIdrelation());
        }
        return dataPODetail;
    }

    private List<TblLocationOfWarehouse> loadAllDataLocationOfWarehouse() {
        List<TblLocationOfWarehouse> list = returWController.getService().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(returWController.getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private void refreshDataPopup() {
        //mi - detail
        ObservableList<MIDetail> miDetailItems = FXCollections.observableArrayList(loadAllDataMIDetail(returWController.cbpMI.getValue()));
        cbpMIDetail.setItems(miDetailItems);

        //location - warehouse
        ObservableList<TblLocationOfWarehouse> locationOfWarehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());
        cbpWarehouse.setItems(locationOfWarehouseItems);
    }

    public class MIDetail {

        private final ObjectProperty<TblMemorandumInvoiceDetail> dataMIDetail = new SimpleObjectProperty<>();

        private final ObjectProperty<TblMemorandumInvoiceDetailPropertyBarcode> dataPropertyBarcode = new SimpleObjectProperty<>();

        private final ObjectProperty<TblMemorandumInvoiceDetailItemExpiredDate> dataItemExpiredDate = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> itemReceivedQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        private final ObjectProperty<BigDecimal> itemReturedQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        private final ObjectProperty<BigDecimal> itemCost = new SimpleObjectProperty<>(new BigDecimal("0"));

        private final ObjectProperty<BigDecimal> itemDiscount = new SimpleObjectProperty<>(new BigDecimal("0"));

        public MIDetail(TblMemorandumInvoiceDetail dataMIDetail,
                TblMemorandumInvoiceDetailPropertyBarcode dataPropertyBarcode,
                TblMemorandumInvoiceDetailItemExpiredDate dataItemExpiredDate,
                BigDecimal itemReceivedQuantity,
                BigDecimal itemReturedQuantity,
                BigDecimal itemCost,
                BigDecimal itemDiscount) {

            this.dataMIDetail.set(dataMIDetail);

            this.dataPropertyBarcode.set(dataPropertyBarcode);

            this.dataItemExpiredDate.set(dataItemExpiredDate);

            this.itemReceivedQuantity.set(itemReceivedQuantity);

            this.itemReturedQuantity.set(itemReturedQuantity);

            this.itemCost.set(itemCost);

            this.itemDiscount.set(itemDiscount);
        }

        public ObjectProperty<TblMemorandumInvoiceDetail> dataMIDetailProperty() {
            return dataMIDetail;
        }

        public TblMemorandumInvoiceDetail getDataMIDetail() {
            return dataMIDetailProperty().get();
        }

        public void setDataMIDetail(TblMemorandumInvoiceDetail dataMIDetail) {
            dataMIDetailProperty().set(dataMIDetail);
        }

        public ObjectProperty<TblMemorandumInvoiceDetailPropertyBarcode> dataPropertyBarcodeProperty() {
            return dataPropertyBarcode;
        }

        public TblMemorandumInvoiceDetailPropertyBarcode getDataPropertyBarcode() {
            return dataPropertyBarcodeProperty().get();
        }

        public void setDataPropertyBarcode(TblMemorandumInvoiceDetailPropertyBarcode dataPropertyBarcode) {
            dataPropertyBarcodeProperty().set(dataPropertyBarcode);
        }

        public ObjectProperty<TblMemorandumInvoiceDetailItemExpiredDate> dataItemExpiredDateProperty() {
            return dataItemExpiredDate;
        }

        public TblMemorandumInvoiceDetailItemExpiredDate getDataItemExpiredDate() {
            return dataItemExpiredDateProperty().get();
        }

        public void setDataItemExpiredDate(TblMemorandumInvoiceDetailItemExpiredDate dataItemExpiredDate) {
            dataItemExpiredDateProperty().set(dataItemExpiredDate);
        }

        public ObjectProperty<BigDecimal> itemReturedQuantityProperty() {
            return itemReturedQuantity;
        }

        public BigDecimal getItemReturedQuantity() {
            return itemReturedQuantityProperty().get();
        }

        public void setItemReturedQuantity(BigDecimal itemReturedQuantity) {
            itemReturedQuantityProperty().set(itemReturedQuantity);
        }

        public ObjectProperty<BigDecimal> itemReceivedQuantityProperty() {
            return itemReceivedQuantity;
        }

        public BigDecimal getItemReceivedQuantity() {
            return itemReceivedQuantityProperty().get();
        }

        public void setItemReceivedQuantity(BigDecimal itemReceivedQuantity) {
            itemReceivedQuantityProperty().set(itemReceivedQuantity);
        }

        public ObjectProperty<BigDecimal> itemCostProperty() {
            return itemCost;
        }

        public BigDecimal getItemCost() {
            return itemCostProperty().get();
        }

        public void setItemCost(BigDecimal itemCost) {
            itemCostProperty().set(itemCost);
        }

        public ObjectProperty<BigDecimal> itemDiscountProperty() {
            return itemDiscount;
        }

        public BigDecimal getItemDiscount() {
            return itemDiscountProperty().get();
        }

        public void setItemDiscount(BigDecimal itemDiscount) {
            itemDiscountProperty().set(itemDiscount);
        }

        @Override
        public String toString() {
            return getDataMIDetail().getTblSupplierItem().getTblItem().getCodeItem();
        }

    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtItemQuantity.textProperty().bindBidirectional(returWController.selectedDataDetailLocation.getTblDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        returWController.selectedDataDetailLocation.getTblDetail().itemQuantityProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
//                //data retur cost
//                txtReturCost.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReturCost()));
            }
        });

        cbpMIDetail.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.getDataMIDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) {    //Property
                    txtItemQuantity.setDisable(true);
                } else {
                    txtItemQuantity.setDisable(false);
                }
            } else {
                txtItemQuantity.setDisable(false);
            }
            //data item
            refreshDataItem();
//            //data retur cost
//            txtReturCost.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReturCost()));
        });

        cbpWarehouse.valueProperty().bindBidirectional(returWController.selectedDataDetailLocation.tblLocationWarehouseProperty());

        cbpMIDetail.hide();
        cbpWarehouse.hide();

        if (returWController.dataInputDetailStatus == 1) { //update
            //set current data
            cbpMIDetail.setValue(getMIDetailFromDetailLocation(returWController.selectedDataDetailLocation));
            //disable data input fields
            cbpMIDetail.setDisable(true);
        }
    }

    private MIDetail getMIDetailFromDetailLocation(ReturWController.DetailLocation dataDetailLocation) {
        if (dataDetailLocation != null) {
            TblMemorandumInvoiceDetail miDetail = returWController.getService().getDataMemorandumInvoiceDetailByIDMemorandumInvoiceAndIDSupplierItem(
                    dataDetailLocation.getTblDetail().getTblMemorandumInvoice().getIdmi(),
                    dataDetailLocation.getTblDetail().getTblSupplierItem().getIdrelation());
            miDetail.setTblMemorandumInvoice(dataDetailLocation.getTblDetail().getTblMemorandumInvoice());
            miDetail.setTblSupplierItem(returWController.getService().getDataSupplierItem(miDetail.getTblSupplierItem().getIdrelation()));
            miDetail.getTblSupplierItem().setTblItem(returWController.getService().getDataItem(miDetail.getTblSupplierItem().getTblItem().getIditem()));
            if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                miDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(returWController.getService().getDataItemTypeHK(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                miDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(returWController.getService().getDataItemTypeWH(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
            if (miDetail.getTblSupplierItem().getTblItem().getConsumable()) {  //consumable
                List<TblMemorandumInvoiceDetailItemExpiredDate> dataItemExpiredDates = returWController.getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                for (TblMemorandumInvoiceDetailItemExpiredDate dataItemExpiredDate : dataItemExpiredDates) {
                    TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                    dataItemExpiredDate.setTblMemorandumInvoiceDetail(miDetail);
                    dataItemExpiredDate.setTblItemExpiredDate(returWController.getService().getDataItemExpiredDate(dataItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                    dataItemExpiredDate.getTblItemExpiredDate().setTblItem(returWController.getService().getDataItem(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getIditem()));
                    if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk() != null) {
                        dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeHk(returWController.getService().getDataItemTypeHK(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh() != null) {
                        dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeWh(returWController.getService().getDataItemTypeWH(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                    return (new MIDetail(miDetail, null, dataItemExpiredDate, miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null, dataItemExpiredDate),
                            poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                            poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                }
            } else {
                if (miDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property
                    List<TblMemorandumInvoiceDetailPropertyBarcode> dataPBs = returWController.getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                    for (TblMemorandumInvoiceDetailPropertyBarcode dataPB : dataPBs) {
                        dataPB.setTblMemorandumInvoiceDetail(miDetail);
                        dataPB.setTblPropertyBarcode(new TblPropertyBarcode(returWController.getService().getDataPropertyBarcode(dataPB.getTblPropertyBarcode().getIdbarcode())));
                        TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                        return (new MIDetail(miDetail, dataPB, null, new BigDecimal("1"), getItemReturedQuantity(miDetail, dataPB, null),
                                poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                    }
                } else {
                    TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                    return (new MIDetail(miDetail, null, null, miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null, null),
                            poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                            poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                }
            }
        }
        return null;
    }

    private void refreshDataItem() {
        if (returWController.selectedDataDetailLocation.getTblDetail().getTblMemorandumInvoice() != null
                && returWController.cbpMI.getValue() != null
                && cbpMIDetail.getValue() != null) {
            txtItemTypeName.setText(cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getTblItemTypeWh().getItemTypeWhname());
            txtItemName.setText(cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getItemName());
            txtPropertyBarcode.setText(cbpMIDetail.getValue().getDataPropertyBarcode() != null
                    ? cbpMIDetail.getValue().getDataPropertyBarcode().getTblPropertyBarcode().getCodeBarcode() : "-");
            txtExpiredDate.setText(cbpMIDetail.getValue().getDataItemExpiredDate() != null
                    ? ClassFormatter.dateFormate.format(cbpMIDetail.getValue().getDataItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate()) : "-");
//            txtItemCost.setText(ClassFormatter.currencyFormat.cFormat(cbpMIDetail.getValue().getItemCost()));
//            txtItemDiscount.setText(ClassFormatter.currencyFormat.cFormat(cbpMIDetail.getValue().getItemDiscount()));
            txtItemReceivedQuantity.setText(ClassFormatter.decimalFormat.cFormat(cbpMIDetail.getValue().getItemReceivedQuantity()));
            txtItemReturedQuantity.setText(ClassFormatter.decimalFormat.cFormat(cbpMIDetail.getValue().getItemReturedQuantity()));
            if (returWController.dataInputDetailStatus == 0) {  //insert
                if (cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) {    //Property
                    returWController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal("1"));
                } else {
                    returWController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal("0"));
                }
            }
        } else {
            txtItemTypeName.setText("");
            txtItemName.setText("");
            txtPropertyBarcode.setText("");
            txtExpiredDate.setText("");
//            txtItemCost.setText("0");
//            txtItemDiscount.setText("0");
            txtItemReceivedQuantity.setText("0");
            txtItemReturedQuantity.setText("0");
            if (returWController.dataInputDetailStatus == 0) {  //insert
                returWController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal("0"));
            }
        }
    }

    private BigDecimal calculationTotalReturCost() {
        BigDecimal result = new BigDecimal("0");
        if (returWController.selectedDataDetailLocation.getTblDetail().getTblMemorandumInvoice() != null
                && returWController.cbpMI.getValue() != null
                && cbpMIDetail.getValue() != null) {
            result = (returWController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                    .multiply((cbpMIDetail.getValue().getItemCost().subtract(cbpMIDetail.getValue().getItemDiscount()))))
                    .multiply((new BigDecimal("1")).add(returWController.cbpMI.getValue().getTblPurchaseOrder().getTaxPecentage()));
        }
        return result;
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", returWController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                if (returWController.dataInputDetailStatus == 0) { //insert
                    ClassMessage.showSucceedAddingDataMessage("", returWController.dialogStageDetal);
                    //set new data
                    returWController.selectedDataDetailLocation.getTblDetail().setTblMemorandumInvoice(returWController.cbpMI.getValue());
                    returWController.selectedDataDetailLocation.getTblDetail().setTblSupplierItem(cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem());
                    returWController.selectedDataDetailLocation.getTblDetail().setTblLocation(cbpWarehouse.getValue().getTblLocation());
                    returWController.selectedDataDetailLocation.getTblDetail().setItemCost(cbpMIDetail.getValue().getItemCost());
                    returWController.selectedDataDetailLocation.getTblDetail().setItemDiscount(cbpMIDetail.getValue().getItemDiscount());
                    returWController.selectedDataDetailLocation.getTblDetail().setItemTaxPercentage(returWController.cbpMI.getValue().getTblPurchaseOrder().getTaxPecentage());
                    if (cbpMIDetail.getValue().getDataPropertyBarcode() != null) {
                        TblReturDetailPropertyBarcode dataReturDetailPropertyBarcode = new TblReturDetailPropertyBarcode();
                        dataReturDetailPropertyBarcode.setTblReturDetail(returWController.selectedDataDetailLocation.getTblDetail());
                        dataReturDetailPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(cbpMIDetail.getValue().getDataPropertyBarcode().getTblPropertyBarcode()));
                        returWController.selectedDataDetailLocation.setTblDetailPropertyBarcode(dataReturDetailPropertyBarcode);
                    }
                    if (cbpMIDetail.getValue().getDataItemExpiredDate() != null) {
                        TblReturDetailItemExpiredDate dataReturDetailItemExpiredDate = new TblReturDetailItemExpiredDate();
                        dataReturDetailItemExpiredDate.setTblReturDetail(returWController.selectedDataDetailLocation.getTblDetail());
                        dataReturDetailItemExpiredDate.setTblItemExpiredDate(new TblItemExpiredDate(cbpMIDetail.getValue().getDataItemExpiredDate().getTblItemExpiredDate()));
                        returWController.selectedDataDetailLocation.setTblDetailItemExpiredDate(dataReturDetailItemExpiredDate);
                    }
                    //add to list
                    returWController.detailLocations.add(returWController.selectedDataDetailLocation);
                } else {  //update
                    ClassMessage.showSucceedUpdatingDataMessage("", returWController.dialogStageDetal);
                    //set to list
                    returWController.detailLocations.set(
                            getSectedIndex((ReturWController.DetailLocation) returWController.tableReturDetail.getTableView().getSelectionModel().getSelectedItem(),
                                    returWController.detailLocations),
                            returWController.selectedDataDetailLocation);
                }
                //refresh data retur
                returWController.refreshData();
                //close form data detail
                returWController.dialogStageDetal.close();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, returWController.dialogStageDetal);
        }
    }

    private int getSectedIndex(ReturWController.DetailLocation dataLocation,
            List<ReturWController.DetailLocation> dataLocations) {
        for (int i = 0; i < dataLocations.size(); i++) {
            if (dataLocations.get(i).equals(dataLocation)) {
                return i;
            }
        }
        return -1;
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        returWController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpMIDetail.getValue() == null) {
            dataInput = false;
            errDataInput += "ID Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtItemQuantity.getText() == null
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah yang akan di-retur : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (returWController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah yang akan di-retur : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (cbpMIDetail.getValue() != null) {
                    BigDecimal maxReturQuantity = cbpMIDetail.getValue().getItemReceivedQuantity().subtract(cbpMIDetail.getValue().getItemReturedQuantity());
                    if (returWController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                            .compareTo(maxReturQuantity) == 1) {
                        dataInput = false;
                        errDataInput += "Jumlah yang akan di-retur : tidak dapat lebih dari " + ClassFormatter.decimalFormat.cFormat(maxReturQuantity) + " \n";
                    }
                }
            }
        }
        if (cbpWarehouse.getValue() == null) {
            dataInput = false;
            errDataInput += "Lokasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataDetailLocation();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReturDetailLocationWController(ReturWController parentController) {
        returWController = parentController;
    }

    private final ReturWController returWController;

}
