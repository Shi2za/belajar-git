/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailPropertyBarcode;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReturDetailLocationController implements Initializable {

    @FXML
    private AnchorPane ancFormDetailLocation;

    @FXML
    private GridPane gpFormDataDetailLocation;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private JFXTextField txtPOTaxPercentage;

    @FXML
    private JFXTextField txtItemTypeName;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtPropertyBarcode;

    @FXML
    private JFXTextField txtItemCost;

    @FXML
    private JFXTextField txtItemDiscount;

    @FXML
    private JFXTextField txtItemReceivedQuantity;

    @FXML
    private JFXTextField txtItemReturedQuantity;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private JFXTextField txtReturCost;

    @FXML
    private AnchorPane ancPOLayout;

    @FXML
    private AnchorPane ancMILayout;

    @FXML
    private AnchorPane ancMIDetailItemLayout;

    @FXML
    private AnchorPane ancLocationLayout;

    private JFXCComboBoxTablePopup<TblPurchaseOrder> cbpPurchaseOrder;

    private JFXCComboBoxTablePopup<TblMemorandumInvoice> cbpReceiving;

    private JFXCComboBoxTablePopup<MIDetail> cbpMIDetail;

    private JFXCComboBoxPopup cbpItemExpiredDateSelected = new JFXCComboBoxPopup<>(null);

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpWarehouse;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetailLocation() {
        initDataDetailPopup();

//        btnSave.getStyleClass().add("button-save");
        btnSave.setTooltip(new Tooltip("Simpan (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

//        btnCancel.getStyleClass().add("button-cancel");
        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpPurchaseOrder,
                cbpReceiving,
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
        //Purchase Order
        TableView<TblPurchaseOrder> tablePO = new TableView<>();

        TableColumn<TblPurchaseOrder, String> codePO = new TableColumn<>("No. PO");
        codePO.setCellValueFactory(cellData -> cellData.getValue().codePoProperty());
        codePO.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> poDate = new TableColumn<>("Tanggal PO");
        poDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getPodate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getPodate())
                                : "-", param.getValue().podateProperty()));
        poDate.setMinWidth(160);

        tablePO.getColumns().addAll(codePO, poDate);

        ObservableList<TblPurchaseOrder> poItems = FXCollections.observableArrayList(loadAllDataPurchaseOrder(returController.selectedDataDetailLocation.getTblDetail().getTblRetur().getTblSupplier()));

        cbpPurchaseOrder = new JFXCComboBoxTablePopup<>(
                TblPurchaseOrder.class, tablePO, poItems, "", "No. PO *", true, 320, 200
        );

        //Receiving
        TableView<TblMemorandumInvoice> tableReceiving = new TableView<>();

        TableColumn<TblMemorandumInvoice, String> codeReceiving = new TableColumn<>("No. Penerimaan");
        codeReceiving.setCellValueFactory(cellData -> cellData.getValue().codeMiProperty());
        codeReceiving.setMinWidth(140);

        TableColumn<TblMemorandumInvoice, String> receivingDate = new TableColumn<>("Tanggal Penerimaan");
        receivingDate.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getReceivingDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getReceivingDate())
                                : "-", param.getValue().receivingDateProperty()));
        receivingDate.setMinWidth(160);

        tableReceiving.getColumns().addAll(codeReceiving, receivingDate);

        ObservableList<TblMemorandumInvoice> receivingItems = FXCollections.observableArrayList(loadAllDataReceiving(null));

        cbpReceiving = new JFXCComboBoxTablePopup<>(
                TblMemorandumInvoice.class, tableReceiving, receivingItems, "", "No. Penerimaan *", true, 320, 200
        );

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

        tableMIDetail.getColumns().addAll(codeItem, itemName, itemTypeWH, propertyBarcode, quantityTitle);

        ObservableList<MIDetail> miDetailItems = FXCollections.observableArrayList(loadAllDataMIDetail(null));

        cbpMIDetail = new JFXCComboBoxTablePopup<>(
                MIDetail.class, tableMIDetail, miDetailItems, "", "ID Barang *", true, 710, 300
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
        AnchorPane.setBottomAnchor(cbpPurchaseOrder, 0.0);
        AnchorPane.setLeftAnchor(cbpPurchaseOrder, 0.0);
        AnchorPane.setRightAnchor(cbpPurchaseOrder, 0.0);
        AnchorPane.setTopAnchor(cbpPurchaseOrder, 0.0);
        ancPOLayout.getChildren().add(cbpPurchaseOrder);
        AnchorPane.setBottomAnchor(cbpReceiving, 0.0);
        AnchorPane.setLeftAnchor(cbpReceiving, 0.0);
        AnchorPane.setRightAnchor(cbpReceiving, 0.0);
        AnchorPane.setTopAnchor(cbpReceiving, 0.0);
        ancMILayout.getChildren().add(cbpReceiving);
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

    private List<TblPurchaseOrder> loadAllDataPurchaseOrder(TblSupplier dataSupplier) {
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (dataSupplier != null) {
            list = returController.getService().getAllDataPurchaseOrderByIDSupplier(dataSupplier.getIdsupplier());
            for (TblPurchaseOrder data : list) {
                //data supplier
                data.setTblSupplier(dataSupplier);
                //data purchase order status
                data.setRefPurchaseOrderStatus(returController.getService().getDataPurchaseOrderStatus(data.getRefPurchaseOrderStatus().getIdstatus()));
            }
            //remove data not used
//            for(int i=list.size()-1; i>-1; i--){
//                if(list.get(i).getRefPurchaseOrderStatus().getIdstatus() == 2   //Dibatalkan = '2'
//                        || list.get(i).getRefPurchaseOrderStatus().getIdstatus() == 3){   //Sudah Tidak Berlaku = '3'
//                   list.remove(i);
//                }
//            }
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getRefPurchaseOrderStatus().getIdstatus() != 5) {   //Dipesan = '5'
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private List<TblMemorandumInvoice> loadAllDataReceiving(TblPurchaseOrder dataPO) {
        List<TblMemorandumInvoice> list = new ArrayList<>();
        if (dataPO != null) {
            list = returController.getService().getAllDataMemorandumInvoiceByIDPurchaseOrder(dataPO.getIdpo());
            for (TblMemorandumInvoice data : list) {
                //data purchase order
                data.setTblPurchaseOrder(dataPO);
                //data supplier
                data.getTblPurchaseOrder().setTblSupplier(returController.getService().getDataSupplier(data.getTblPurchaseOrder().getTblSupplier().getIdsupplier()));
            }
        }
        return list;
    }

    private List<MIDetail> loadAllDataMIDetail(TblMemorandumInvoice dataMI) {
        List<MIDetail> list = new ArrayList<>();
        if (dataMI != null) {
            List<TblMemorandumInvoiceDetail> miDetails = returController.getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(dataMI.getIdmi());
            for (TblMemorandumInvoiceDetail miDetail : miDetails) {
                if (miDetail.getTblSupplierItem() != null) {   //not bonus
                    miDetail.setTblMemorandumInvoice(dataMI);
                    miDetail.setTblSupplierItem(returController.getService().getDataSupplierItem(miDetail.getTblSupplierItem().getIdrelation()));
                    miDetail.getTblSupplierItem().setTblItem(returController.getService().getDataItem(miDetail.getTblSupplierItem().getTblItem().getIditem()));
                    if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                        miDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(returController.getService().getDataItemTypeHK(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                        miDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(returController.getService().getDataItemTypeWH(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                    if (miDetail.getTblSupplierItem().getTblItem().getConsumable()) {  //consumable
                        List<TblMemorandumInvoiceDetailItemExpiredDate> dataItemExpiredDates = returController.getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                        for (TblMemorandumInvoiceDetailItemExpiredDate dataItemExpiredDate : dataItemExpiredDates) {
                            dataItemExpiredDate.setTblMemorandumInvoiceDetail(miDetail);
                            dataItemExpiredDate.setTblItemExpiredDate(returController.getService().getDataItemExpiredDate(dataItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                            dataItemExpiredDate.getTblItemExpiredDate().setTblItem(returController.getService().getDataItem(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getIditem()));
                            if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk() != null) {
                                dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeHk(returController.getService().getDataItemTypeHK(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                            }
                            if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh() != null) {
                                dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeWh(returController.getService().getDataItemTypeWH(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                            }
                        }
                        TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                        list.add(new MIDetail(miDetail, null, getListMIDetailItemExpiredDateSelected(dataItemExpiredDates), miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null),
                                poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                    } else {
                        if (miDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property
                            List<TblMemorandumInvoiceDetailPropertyBarcode> dataPBs = returController.getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                            for (TblMemorandumInvoiceDetailPropertyBarcode dataPB : dataPBs) {
                                dataPB.setTblMemorandumInvoiceDetail(miDetail);
                                dataPB.setTblPropertyBarcode(new TblPropertyBarcode(returController.getService().getDataPropertyBarcode(dataPB.getTblPropertyBarcode().getIdbarcode())));
                                TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                                list.add(new MIDetail(miDetail, dataPB, new ArrayList<>(), new BigDecimal("1"), getItemReturedQuantity(miDetail, dataPB),
                                        poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                        poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                            }
                        } else {
                            TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                            list.add(new MIDetail(miDetail, null, new ArrayList<>(), miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null),
                                    poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                    poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                        }
                    }
                }
            }
            //remove data has been used
            for (int i = list.size() - 1; i > -1; i--) {
                for (ReturController.DetailLocation data : returController.detailLocations) {
                    if (data.getTblDetail().getTblMemorandumInvoice().getIdmi() == list.get(i).getDataMIDetail().getTblMemorandumInvoice().getIdmi()
                            && data.getTblDetail().getTblSupplierItem().getIdrelation() == list.get(i).getDataMIDetail().getTblSupplierItem().getIdrelation()) {
                        if (!(list.get(i).getDataMIDetail().getTblSupplierItem().getTblItem().getPropertyStatus())) {   //Property
                            //not property barccode
                            list.remove(i);
                            break;
                        } else {
                            //same property
                            if (data.getTblDetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode()
                                    == list.get(i).getDataPropertyBarcode().getTblPropertyBarcode().getIdbarcode()) {
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

    private List<MIDetailItemExpiredDateSelected> getListMIDetailItemExpiredDateSelected(List<TblMemorandumInvoiceDetailItemExpiredDate> dataItemExpiredDates) {
        List<MIDetailItemExpiredDateSelected> list = new ArrayList<>();
        for (TblMemorandumInvoiceDetailItemExpiredDate dataItemExpiredDate : dataItemExpiredDates) {
            list.add(new MIDetailItemExpiredDateSelected(dataItemExpiredDate, false));
        }
        return list;
    }

    private BigDecimal getItemReturedQuantity(TblMemorandumInvoiceDetail dataMIDetail,
            TblMemorandumInvoiceDetailPropertyBarcode dataMIDetailPO) {
        BigDecimal result = new BigDecimal("0");
        if (dataMIDetail != null) {
            if (dataMIDetail.getTblSupplierItem().getTblItem().getConsumable()) {    //Consumable
                List<TblReturDetail> dataReturDetails = returController.getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataMIDetail.getIddetail(),
                        dataMIDetail.getTblSupplierItem().getIdrelation());
                for (TblReturDetail dataReturDetail : dataReturDetails) {
                    result = result.add(dataReturDetail.getItemQuantity());
                }
            } else {
                if (dataMIDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) {    //Property
                    if (dataMIDetailPO != null) {
                        TblReturDetailPropertyBarcode dataReturDetailPropertyBarcode = returController.getService().getDataReturDetailPropertyBarcodeByIDPropertyBarcode(dataMIDetailPO.getTblPropertyBarcode().getIdbarcode());
                        if (dataReturDetailPropertyBarcode != null) {
                            result = new BigDecimal("1");
                        }
                    }
                } else {
                    List<TblReturDetail> dataReturDetails = returController.getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataMIDetail.getIddetail(),
                            dataMIDetail.getTblSupplierItem().getIdrelation());
                    for (TblReturDetail dataReturDetail : dataReturDetails) {
                        result = result.add(dataReturDetail.getItemQuantity());
                    }
                }
            }
        }
        return result;
    }

    public TblPurchaseOrderDetail getDataPurchaseOrderDetail(TblMemorandumInvoiceDetail dataMIDetail) {
        TblPurchaseOrderDetail dataPODetail = null;
        if (dataMIDetail != null) {
            dataPODetail = returController.getService().getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(
                    dataMIDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataMIDetail.getTblSupplierItem().getIdrelation());
        }
        return dataPODetail;
    }

    private List<TblLocationOfWarehouse> loadAllDataLocationOfWarehouse() {
        List<TblLocationOfWarehouse> list = returController.getService().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(returController.getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private void refreshDataPopup() {
        //purchase order
        ObservableList<TblPurchaseOrder> poItems = FXCollections.observableArrayList(loadAllDataPurchaseOrder(returController.selectedDataDetailLocation.getTblDetail().getTblRetur().getTblSupplier()));
        cbpPurchaseOrder.setItems(poItems);

        //receiving
        ObservableList<TblMemorandumInvoice> receivingItems = FXCollections.observableArrayList(loadAllDataReceiving(null));
        cbpReceiving.setItems(receivingItems);

        //mi - detail
        ObservableList<MIDetail> miDetailItems = FXCollections.observableArrayList(loadAllDataMIDetail(null));
        cbpMIDetail.setItems(miDetailItems);

        //location - warehouse
        ObservableList<TblLocationOfWarehouse> locationOfWarehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());
        cbpWarehouse.setItems(locationOfWarehouseItems);
    }

    private void refreshDataItemExpiredDateSelectedPopup() {
        gpFormDataDetailLocation.getChildren().remove(cbpItemExpiredDateSelected);
        cbpItemExpiredDateSelected = getComboBoxItemExpiredDateDetails(cbpMIDetail.getValue());
        cbpItemExpiredDateSelected.setVisible(cbpMIDetail.getValue() != null
                && cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getConsumable());
        gpFormDataDetailLocation.add(cbpItemExpiredDateSelected, 1, 9);
    }

    private JFXCComboBoxPopup getComboBoxItemExpiredDateDetails(MIDetail miDetail) {

        JFXCComboBoxPopup cbpItemExpiredDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Details");
        button.setPrefSize(100, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-details");
        button.setTooltip(new Tooltip("Data Tanggal Kadaluarsa"));
        button.setOnMouseClicked((e) -> cbpItemExpiredDetails.show());

        AnchorPane ancDataTableProperties = new AnchorPane();

        //set data table
        TableView<MIDetailItemExpiredDateSelected> tableItemExpiredDate = new TableView<>();
        tableItemExpiredDate.setEditable(true);

        TableColumn<MIDetailItemExpiredDateSelected, Boolean> selected = new TableColumn("Pilih");
        selected.setMinWidth(80);
        selected.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selected.setCellFactory((TableColumn<MIDetailItemExpiredDateSelected, Boolean> param) -> new CheckBoxCell<>());
        selected.setEditable(true);

        TableColumn<MIDetailItemExpiredDateSelected, String> codeItemExpiredDate = new TableColumn("ID Barang");
        codeItemExpiredDate.setMinWidth(120);
        codeItemExpiredDate.setCellValueFactory((TableColumn.CellDataFeatures<MIDetailItemExpiredDateSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataDetailItemExpiredDate() == null
                        || param.getValue().getDataDetailItemExpiredDate().getTblItemExpiredDate() == null
                                ? "" : param.getValue().getDataDetailItemExpiredDate().getTblItemExpiredDate().getCodeItemExpiredDate(), param.getValue().getDataDetailItemExpiredDate().getTblItemExpiredDate().codeItemExpiredDateProperty()));

        TableColumn<MIDetailItemExpiredDateSelected, String> expiredDate = new TableColumn("Tanggal Kadaluarsa");
        expiredDate.setMinWidth(140);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<MIDetailItemExpiredDateSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataDetailItemExpiredDate() == null
                        || param.getValue().getDataDetailItemExpiredDate().getTblItemExpiredDate() == null
                                ? "-" : ClassFormatter.dateFormate.format(param.getValue().getDataDetailItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate()), param.getValue().getDataDetailItemExpiredDate().getTblItemExpiredDate().itemExpiredDateProperty()));

        tableItemExpiredDate.getColumns().addAll(selected, codeItemExpiredDate, expiredDate);

        tableItemExpiredDate.setItems(FXCollections.observableArrayList(cbpMIDetail.getValue() != null
                ? cbpMIDetail.getValue().getListItemExpiredDate()
                : new ArrayList<>()));

        //set to layout (data popup) 
        AnchorPane.setBottomAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setLeftAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setRightAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setTopAnchor(tableItemExpiredDate, 0.0);
        ancDataTableProperties.getChildren().add(tableItemExpiredDate);

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
        content.setPrefSize(350, 350);

        content.setCenter(ancContent);

        cbpItemExpiredDetails.setPopupEditor(false);
        cbpItemExpiredDetails.promptTextProperty().set("");
        cbpItemExpiredDetails.setLabelFloat(false);
        cbpItemExpiredDetails.setPopupButton(button);
        cbpItemExpiredDetails.settArrowButton(true);
        cbpItemExpiredDetails.setPopupContent(content);

        return cbpItemExpiredDetails;
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
//            if (isEmpty()) {
//                return;
//            }
//            checkBox.setDisable(false);
//            checkBox.requestFocus();
        }

        @Override
        public void cancelEdit() {
//            super.cancelEdit();
//            checkBox.setDisable(true);
        }

    }

    public class MIDetail {

        private final ObjectProperty<TblMemorandumInvoiceDetail> dataMIDetail = new SimpleObjectProperty<>();

        private final ObjectProperty<TblMemorandumInvoiceDetailPropertyBarcode> dataPropertyBarcode = new SimpleObjectProperty<>();

        private final ObservableList<MIDetailItemExpiredDateSelected> listItemExpiredDate = FXCollections.observableArrayList();

        private final ObjectProperty<BigDecimal> itemReceivedQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        private final ObjectProperty<BigDecimal> itemReturedQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        private final ObjectProperty<BigDecimal> itemCost = new SimpleObjectProperty<>(new BigDecimal("0"));

        private final ObjectProperty<BigDecimal> itemDiscount = new SimpleObjectProperty<>(new BigDecimal("0"));

        public MIDetail(TblMemorandumInvoiceDetail dataMIDetail,
                TblMemorandumInvoiceDetailPropertyBarcode dataPropertyBarcode,
                List<MIDetailItemExpiredDateSelected> listItemExpiredDate,
                BigDecimal itemReceivedQuantity,
                BigDecimal itemReturedQuantity,
                BigDecimal itemCost,
                BigDecimal itemDiscount) {

            this.dataMIDetail.set(dataMIDetail);

            this.dataPropertyBarcode.set(dataPropertyBarcode);

            this.listItemExpiredDate.setAll(listItemExpiredDate);

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

        public List<MIDetailItemExpiredDateSelected> getListItemExpiredDate() {
            return listItemExpiredDate;
        }

        public void setListItemExpiredDate(List<MIDetailItemExpiredDateSelected> listItemExpiredDate) {
            this.listItemExpiredDate.setAll(listItemExpiredDate);
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

    public class MIDetailItemExpiredDateSelected {

        private final ObjectProperty<TblMemorandumInvoiceDetailItemExpiredDate> dataDetailItemExpiredDate = new SimpleObjectProperty<>();

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public MIDetailItemExpiredDateSelected(TblMemorandumInvoiceDetailItemExpiredDate dataDetailItemExpiredDate,
                boolean selected) {
            //data detail item expired date
            this.dataDetailItemExpiredDate.set(dataDetailItemExpiredDate);
            //data selected
            this.selected.set(selected);
            //set listener to selected
            this.selected.addListener((obs, oldVal, newVal) -> {
                //refresh detail - item quantity
                refreshDetailLocationItemQuantity();
            });
        }

        public ObjectProperty<TblMemorandumInvoiceDetailItemExpiredDate> dataDetailItemExpiredDateProperty() {
            return dataDetailItemExpiredDate;
        }

        public TblMemorandumInvoiceDetailItemExpiredDate getDataDetailItemExpiredDate() {
            return dataDetailItemExpiredDateProperty().get();
        }

        public void setDataDetailItemExpiredDate(TblMemorandumInvoiceDetailItemExpiredDate dataDetailItemExpiredDate) {
            dataDetailItemExpiredDateProperty().set(dataDetailItemExpiredDate);
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

    private void refreshDetailLocationItemQuantity() {
        if (cbpMIDetail.getValue() != null) {
            double countItemExpiredDateSelected = 0;
            for (MIDetailItemExpiredDateSelected miDetailItemExpiredDateSelected : cbpMIDetail.getValue().getListItemExpiredDate()) {
                if (miDetailItemExpiredDateSelected.isSelected()) {
                    countItemExpiredDateSelected++;
                }
            }
            returController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal(String.valueOf(countItemExpiredDateSelected)));
            //calculation total retur
            txtReturCost.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReturCost()));
        }
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        refreshDataItemExpiredDateSelectedPopup();

        txtSupplierName.textProperty().bind(returController.selectedDataDetailLocation.getTblDetail().getTblRetur().getTblSupplier().supplierNameProperty());

        txtItemQuantity.textProperty().bindBidirectional(returController.selectedDataDetailLocation.getTblDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        returController.selectedDataDetailLocation.getTblDetail().itemQuantityProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                //data retur cost
                txtReturCost.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReturCost()));
            }
        });

        cbpPurchaseOrder.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtPOTaxPercentage.setText((cbpPurchaseOrder.getValue().getTaxPecentage().multiply(new BigDecimal("100"))) + "%");
            } else {
                txtPOTaxPercentage.setText("0%");
            }
            //data receiving
            ObservableList<TblMemorandumInvoice> receivingItems = FXCollections.observableArrayList(loadAllDataReceiving(cbpPurchaseOrder.getValue()));
            cbpReceiving.setItems(receivingItems);
            cbpReceiving.setValue(null);
        });

        cbpReceiving.valueProperty().addListener((obs, oldVal, newVal) -> {
            //mi - detail
            ObservableList<MIDetail> miDetailItems = FXCollections.observableArrayList(loadAllDataMIDetail(cbpReceiving.getValue()));
            cbpMIDetail.setItems(miDetailItems);
            cbpMIDetail.setValue(null);
        });

        cbpMIDetail.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.getDataMIDetail().getTblSupplierItem().getTblItem().getConsumable() //Consumable
                        || newVal.getDataMIDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) {    //Property
                    txtItemQuantity.setDisable(true);
                } else {
                    txtItemQuantity.setDisable(false);
                }
            } else {
                txtItemQuantity.setDisable(false);
            }
            //data item
            refreshDataItem();
            //data item expired date
            refreshDataItemExpiredDateSelectedPopup();
            //data retur cost
            txtReturCost.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReturCost()));
        });

        cbpWarehouse.valueProperty().bindBidirectional(returController.selectedDataDetailLocation.tblLocationWarehouseProperty());

        cbpPurchaseOrder.hide();
        cbpReceiving.hide();
        cbpMIDetail.hide();
        cbpWarehouse.hide();

        if (returController.dataInputDetailStatus == 1) { //update
            //set current data
            cbpPurchaseOrder.setValue(returController.selectedDataDetailLocation.getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder());
            cbpReceiving.setValue(returController.selectedDataDetailLocation.getTblDetail().getTblMemorandumInvoice());
            cbpMIDetail.setValue(getMIDetailFromDetailLocation(returController.selectedDataDetailLocation));
            //disable data input fields
            cbpPurchaseOrder.setDisable(true);
            cbpReceiving.setDisable(true);
            cbpMIDetail.setDisable(true);
        }
    }

    private MIDetail getMIDetailFromDetailLocation(ReturController.DetailLocation dataDetailLocation) {
        if (dataDetailLocation != null) {
            TblMemorandumInvoiceDetail miDetail = returController.getService().getDataMemorandumInvoiceDetailByIDMemorandumInvoiceAndIDSupplierItem(
                    dataDetailLocation.getTblDetail().getTblMemorandumInvoice().getIdmi(),
                    dataDetailLocation.getTblDetail().getTblSupplierItem().getIdrelation());
            miDetail.setTblMemorandumInvoice(dataDetailLocation.getTblDetail().getTblMemorandumInvoice());
            miDetail.setTblSupplierItem(returController.getService().getDataSupplierItem(miDetail.getTblSupplierItem().getIdrelation()));
            miDetail.getTblSupplierItem().setTblItem(returController.getService().getDataItem(miDetail.getTblSupplierItem().getTblItem().getIditem()));
            if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                miDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(returController.getService().getDataItemTypeHK(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            if (miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                miDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(returController.getService().getDataItemTypeWH(miDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
            if (miDetail.getTblSupplierItem().getTblItem().getConsumable()) {  //consumable
                List<TblMemorandumInvoiceDetailItemExpiredDate> dataItemExpiredDates = returController.getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                for (TblMemorandumInvoiceDetailItemExpiredDate dataItemExpiredDate : dataItemExpiredDates) {
                    dataItemExpiredDate.setTblMemorandumInvoiceDetail(miDetail);
                    dataItemExpiredDate.setTblItemExpiredDate(returController.getService().getDataItemExpiredDate(dataItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                    dataItemExpiredDate.getTblItemExpiredDate().setTblItem(returController.getService().getDataItem(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getIditem()));
                    if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk() != null) {
                        dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeHk(returController.getService().getDataItemTypeHK(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    if (dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh() != null) {
                        dataItemExpiredDate.getTblItemExpiredDate().getTblItem().setTblItemTypeWh(returController.getService().getDataItemTypeWH(dataItemExpiredDate.getTblItemExpiredDate().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                }
                TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                List<MIDetailItemExpiredDateSelected> miDetailItemExpiredDateSelecteds = getListMIDetailItemExpiredDateSelected(dataItemExpiredDates);
                //set selected data item expired date (consumable)
                for (MIDetailItemExpiredDateSelected miDetailItemExpiredDateSelected : miDetailItemExpiredDateSelecteds) {
                    for (ReturController.DetailItemExpiredDateSelected detailItemExpiredDateSelected : dataDetailLocation.getDetailItemExpiredDateSelecteds()) {
                        if (detailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().getIditemExpiredDate()
                                == miDetailItemExpiredDateSelected.getDataDetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate()) {
                            miDetailItemExpiredDateSelected.setSelected(true);
                            break;
                        }
                    }
                }
                return (new MIDetail(miDetail, null, miDetailItemExpiredDateSelecteds, miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null),
                        poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                        poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
            } else {
                if (miDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property
                    List<TblMemorandumInvoiceDetailPropertyBarcode> dataPBs = returController.getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(miDetail.getIddetail());
                    for (TblMemorandumInvoiceDetailPropertyBarcode dataPB : dataPBs) {
                        dataPB.setTblMemorandumInvoiceDetail(miDetail);
                        dataPB.setTblPropertyBarcode(new TblPropertyBarcode(returController.getService().getDataPropertyBarcode(dataPB.getTblPropertyBarcode().getIdbarcode())));
                        TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                        return (new MIDetail(miDetail, dataPB, new ArrayList<>(), new BigDecimal("1"), getItemReturedQuantity(miDetail, dataPB),
                                poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                                poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                    }
                } else {
                    TblPurchaseOrderDetail poDetail = getDataPurchaseOrderDetail(miDetail);
                    return (new MIDetail(miDetail, null, new ArrayList<>(), miDetail.getItemQuantity(), getItemReturedQuantity(miDetail, null),
                            poDetail != null ? poDetail.getItemCost() : new BigDecimal("0"),
                            poDetail != null ? poDetail.getItemDiscount() : new BigDecimal("0")));
                }
            }
        }
        return null;
    }

    private void refreshDataItem() {
        if (returController.selectedDataDetailLocation.getTblDetail().getTblRetur().getTblSupplier() != null
                && cbpPurchaseOrder.getValue() != null
                && cbpReceiving.getValue() != null
                && cbpMIDetail.getValue() != null) {
            txtItemTypeName.setText(cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getTblItemTypeWh().getItemTypeWhname());
            txtItemName.setText(cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getItemName());
            txtPropertyBarcode.setText(cbpMIDetail.getValue().getDataPropertyBarcode() != null
                    ? cbpMIDetail.getValue().getDataPropertyBarcode().getTblPropertyBarcode().getCodeBarcode() : "-");
            txtItemCost.setText(ClassFormatter.currencyFormat.cFormat(cbpMIDetail.getValue().getItemCost()));
            txtItemDiscount.setText(ClassFormatter.currencyFormat.cFormat(cbpMIDetail.getValue().getItemDiscount()));
            txtItemReceivedQuantity.setText(ClassFormatter.decimalFormat.cFormat(cbpMIDetail.getValue().getItemReceivedQuantity()));
            txtItemReturedQuantity.setText(ClassFormatter.decimalFormat.cFormat(cbpMIDetail.getValue().getItemReturedQuantity()));
            if (returController.dataInputDetailStatus == 0) {  //insert
                if (cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) {    //Property
                    returController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal("1"));
                } else {
                    returController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal("0"));
                }
            }
        } else {
            txtItemTypeName.setText("");
            txtItemName.setText("");
            txtPropertyBarcode.setText("");
            txtItemCost.setText("0");
            txtItemDiscount.setText("0");
            txtItemReceivedQuantity.setText("0");
            txtItemReturedQuantity.setText("0");
            if (returController.dataInputDetailStatus == 0) {  //insert
                returController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal("0"));
            }
        }
    }

    private BigDecimal calculationTotalReturCost() {
        BigDecimal result = new BigDecimal("0");
        if (returController.selectedDataDetailLocation.getTblDetail().getTblRetur().getTblSupplier() != null
                && cbpPurchaseOrder.getValue() != null
                && cbpReceiving.getValue() != null
                && cbpMIDetail.getValue() != null) {
            result = (returController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                    .multiply((cbpMIDetail.getValue().getItemCost().subtract(cbpMIDetail.getValue().getItemDiscount()))))
                    .multiply((new BigDecimal("1")).add(cbpPurchaseOrder.getValue().getTaxPecentage()));
        }
        return result;
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", returController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                if (returController.dataInputDetailStatus == 0) { //insert
                    ClassMessage.showSucceedAddingDataMessage("", returController.dialogStageDetal);
                    //set new data
                    returController.selectedDataDetailLocation.getTblDetail().setTblMemorandumInvoice(cbpReceiving.getValue());
                    returController.selectedDataDetailLocation.getTblDetail().setTblSupplierItem(cbpMIDetail.getValue().getDataMIDetail().getTblSupplierItem());
                    returController.selectedDataDetailLocation.getTblDetail().setTblLocation(cbpWarehouse.getValue().getTblLocation());
                    returController.selectedDataDetailLocation.getTblDetail().setItemCost(cbpMIDetail.getValue().getItemCost());
                    returController.selectedDataDetailLocation.getTblDetail().setItemDiscount(cbpMIDetail.getValue().getItemDiscount());
                    returController.selectedDataDetailLocation.getTblDetail().setItemTaxPercentage(cbpPurchaseOrder.getValue().getTaxPecentage());
                    if (cbpMIDetail.getValue().getDataPropertyBarcode() != null) {
                        TblReturDetailPropertyBarcode dataReturDetailPropertyBarcode = new TblReturDetailPropertyBarcode();
                        dataReturDetailPropertyBarcode.setTblReturDetail(returController.selectedDataDetailLocation.getTblDetail());
                        dataReturDetailPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(cbpMIDetail.getValue().getDataPropertyBarcode().getTblPropertyBarcode()));
                        returController.selectedDataDetailLocation.setTblDetailPropertyBarcode(dataReturDetailPropertyBarcode);
                    }
                    List<ReturController.DetailItemExpiredDateSelected> list = new ArrayList<>();
                    for (MIDetailItemExpiredDateSelected miDetailItemExpiredDateSelected : cbpMIDetail.getValue().getListItemExpiredDate()) {
                        if (miDetailItemExpiredDateSelected.isSelected()) {
                            list.add(returController.getReturDetailItemExpiredDateSelectedFromMIDetailItemExpiredDateSelected(
                                    returController.selectedDataDetailLocation.getTblDetail(),
                                    miDetailItemExpiredDateSelected));
                        }
                    }
                    returController.selectedDataDetailLocation.setDetailItemExpiredDateQuantities(list);
                    //add to list
                    returController.detailLocations.add(returController.selectedDataDetailLocation);
                } else {  //update
                    ClassMessage.showSucceedUpdatingDataMessage("", returController.dialogStageDetal);
                    //set selected data
                    List<ReturController.DetailItemExpiredDateSelected> list = new ArrayList<>();
                    for (MIDetailItemExpiredDateSelected miDetailItemExpiredDateSelected : cbpMIDetail.getValue().getListItemExpiredDate()) {
                        if (miDetailItemExpiredDateSelected.isSelected()) {
                            list.add(returController.getReturDetailItemExpiredDateSelectedFromMIDetailItemExpiredDateSelected(
                                    returController.selectedDataDetailLocation.getTblDetail(),
                                    miDetailItemExpiredDateSelected));
                        }
                    }
                    returController.selectedDataDetailLocation.setDetailItemExpiredDateQuantities(list);
                    //set to list
                    returController.detailLocations.set(
                            getSectedIndex((ReturController.DetailLocation) returController.tableReturDetail.getTableView().getSelectionModel().getSelectedItem(),
                                    returController.detailLocations),
                            returController.selectedDataDetailLocation);
                }
                //refresh data retur
                returController.refreshData();
                //close form data detail
                returController.dialogStageDetal.close();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, returController.dialogStageDetal);
        }
    }

    private int getSectedIndex(ReturController.DetailLocation dataLocation,
            List<ReturController.DetailLocation> dataLocations) {
        for (int i = 0; i < dataLocations.size(); i++) {
            if (dataLocations.get(i).equals(dataLocation)) {
                return i;
            }
        }
        return -1;
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        returController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpPurchaseOrder.getValue() == null) {
            dataInput = false;
            errDataInput += "No. PO : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpReceiving.getValue() == null) {
            dataInput = false;
            errDataInput += "No. Penerimaan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
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
            if (returController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah yang akan di-retur : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (cbpMIDetail.getValue() != null) {
                    BigDecimal maxReturQuantity = cbpMIDetail.getValue().getItemReceivedQuantity().subtract(cbpMIDetail.getValue().getItemReturedQuantity());
                    if (returController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                            .compareTo(maxReturQuantity) == 1) {
                        dataInput = false;
                        errDataInput += "Jumlah yang akan di-retur : " + ClassFormatter.decimalFormat.cFormat(maxReturQuantity) + " \n";
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

    public ReturDetailLocationController(ReturController parentController) {
        returController = parentController;
    }

    private final ReturController returController;

}
