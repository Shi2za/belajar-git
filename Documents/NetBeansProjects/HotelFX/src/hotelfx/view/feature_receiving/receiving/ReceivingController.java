/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_receiving.receiving;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintReceiving;
import hotelfx.helper.PrintModel.ClassPrintReceivingDetail;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailPropertyBarcode;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.service.FReceivingManager;
import hotelfx.persistence.service.FReceivingManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_receiving.FeatureReceivingController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
 * @author ANDRI
 */
public class ReceivingController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataReceiving;

    private DoubleProperty dataReceivingFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReceivingLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReceivingSplitpane() {
        spDataReceiving.setDividerPositions(1);

        dataReceivingFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReceivingFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataReceiving.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataReceiving.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReceivingFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReceivingLayout.setDisable(false);
                    tableDataReceivingLayoutDisableLayer.setDisable(true);
                    tableDataReceivingLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReceivingLayout.setDisable(true);
                    tableDataReceivingLayoutDisableLayer.setDisable(false);
                    tableDataReceivingLayoutDisableLayer.toFront();
                }
            }
        });

        dataReceivingFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataReceivingLayout;

    private ClassFilteringTable<TblMemorandumInvoice> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataReceiving;

    private void initTableDataReceiving() {
        //set table
        setTableDataReceiving();
        //set control
        setTableControlDataReceiving();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataReceiving, 15.0);
        AnchorPane.setLeftAnchor(tableDataReceiving, 15.0);
        AnchorPane.setRightAnchor(tableDataReceiving, 15.0);
        AnchorPane.setTopAnchor(tableDataReceiving, 15.0);
        ancBodyLayout.getChildren().add(tableDataReceiving);
    }

    private void setTableDataReceiving() {
        TableView<TblMemorandumInvoice> tableView = new TableView();

        TableColumn<TblMemorandumInvoice, String> codeReceiving = new TableColumn("No. Penerimaan");
        codeReceiving.setCellValueFactory(cellData -> cellData.getValue().codeMiProperty());
        codeReceiving.setMinWidth(140);

        TableColumn<TblMemorandumInvoice, String> deliveryNumber = new TableColumn("No. Surat Jalan");
        deliveryNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDeliveryNumber() != null
                                ? param.getValue().getDeliveryNumber() : "-",
                        param.getValue().deliveryNumberProperty()));
        deliveryNumber.setMinWidth(140);

        TableColumn<TblMemorandumInvoice, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPurchaseOrder().getCodePo(), param.getValue().tblPurchaseOrderProperty()));
        codePO.setMinWidth(140);

        TableColumn<TblMemorandumInvoice, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPurchaseOrder().getTblSupplier().getSupplierName(), param.getValue().tblPurchaseOrderProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblMemorandumInvoice, String> receivingDate = new TableColumn<>("Tanggal Penerimaan");
        receivingDate.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getReceivingDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getReceivingDate())
                                : "-", param.getValue().receivingDateProperty()));
        receivingDate.setMinWidth(160);

        tableView.getColumns().addAll(codeReceiving, deliveryNumber, codePO, receivingDate, supplierName);

        tableView.setItems(loadAllDataReceiving());

        tableView.setRowFactory(tv -> {
            TableRow<TblMemorandumInvoice> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataReceivingUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataReceivingShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataReceivingShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataReceiving = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblMemorandumInvoice.class,
                tableDataReceiving.getTableView(),
                tableDataReceiving.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataReceiving() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Data Penerimaan Barang");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReceivingCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataReceivingPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataReceiving.addButtonControl(buttonControls);
    }

    private ObservableList<TblMemorandumInvoice> loadAllDataReceiving() {
        List<TblMemorandumInvoice> list = getService().getAllDataMemorandumInvoice();
        for (TblMemorandumInvoice data : list) {
            //data purchase order
            data.setTblPurchaseOrder(getService().getDataPurchaseOrder(data.getTblPurchaseOrder().getIdpo()));
            //data supplier
            data.getTblPurchaseOrder().setTblSupplier(getService().getDataSupplier(data.getTblPurchaseOrder().getTblSupplier().getIdsupplier()));
            //data location (supplier)
            data.getTblPurchaseOrder().getTblSupplier().setTblLocation(getService().getDataLocation(data.getTblPurchaseOrder().getTblSupplier().getTblLocation().getIdlocation()));
            //data memorandum invoice status
            data.setRefMemorandumInvoiceStatus(getService().getDataMemorandumInvoiceStatus(data.getRefMemorandumInvoiceStatus().getIdstatus()));
        }
        //remove data isn't used
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getRefMemorandumInvoiceStatus().getIdstatus() == 2 //Dibatalkan = '2'
                    || list.get(i).getRefMemorandumInvoiceStatus().getIdstatus() == 3) { //Sudah Tidak Berlaku = '3'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

//    private List<DetailLocation> loadAllDataReceivingDetail() {
//        List<DetailLocation> list = new ArrayList<>();
//        if (selectedData.getIdmi() != 0L) {
//            List<TblMemorandumInvoiceDetail> dataMIDetails = getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(selectedData.getIdmi());
//            for (TblMemorandumInvoiceDetail dataMIDetail : dataMIDetails) {
//                //data mi - detail
//                TblMemorandumInvoiceDetail dataDetail = dataMIDetail;
//                dataDetail.setTblMemorandumInvoice(selectedData);
//                dataDetail.setTblItem(getService().getDataItem(dataDetail.getTblItem().getIditem()));
//                dataDetail.getTblItem().setTblUnit(getService().getDataUnit(dataDetail.getTblItem().getTblUnit().getIdunit()));
//                dataDetail.getTblItem().setRefItemType(getService().getDataItemType(dataDetail.getTblItem().getTblItemType().getIdtype()));
//                dataDetail.setTblLocation(getService().getDataLocation(dataDetail.getTblLocation().getIdlocation()));
//                //data mi - detail - property barcode
//                List<TblMemorandumInvoiceDetailPropertyBarcode> dataMIDetailPropertyBarcodes
//                        = getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(dataDetail.getIddetail());
//                for (TblMemorandumInvoiceDetailPropertyBarcode dataMIDetailPropertyBarcode : dataMIDetailPropertyBarcodes) {
//                    //data mi - detail
//                    dataMIDetailPropertyBarcode.setTblMemorandumInvoiceDetail(dataMIDetail);
//                    //data property barcode
//                    dataMIDetailPropertyBarcode.setTblPropertyBarcode(getService().getDataPropertyBarcode(dataMIDetailPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
//                }
//                //data mi - detail - item expired date
//                List<TblMemorandumInvoiceDetailItemExpiredDate> dataMIDetailPropertyItemExpiredDates
//                        = getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(dataDetail.getIddetail());
//                for (TblMemorandumInvoiceDetailItemExpiredDate dataMIDetailPropertyItemExpiredDate : dataMIDetailPropertyItemExpiredDates) {
//                    //data mi - detail
//                    dataMIDetailPropertyItemExpiredDate.setTblMemorandumInvoiceDetail(dataMIDetail);
//                    //data item expired
//                    dataMIDetailPropertyItemExpiredDate.setTblItemExpiredDate(getService().getDataItemExpiredDate(dataMIDetailPropertyItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
////                    //data item
////                    dataMIDetailPropertyItemExpiredDate.getTblItemExpiredDate().setTblItem(getService().getDataItem(dataMIDetailPropertyItemExpiredDate.getTblMemorandumInvoiceDetail().getTblItem().getIditem()));
//                }
//                //data mi - detail - item expired date - quantity
//                List<DetailItemExpiredDateQuantity> dataDetailItemExpiredDateQuantities = new ArrayList<>();
//                for (TblMemorandumInvoiceDetailItemExpiredDate dataMIDetailPropertyItemExpiredDate : dataMIDetailPropertyItemExpiredDates) {
//                    boolean found = false;
//                    for (DetailItemExpiredDateQuantity dataDetailItemExpiredDateQuantity : dataDetailItemExpiredDateQuantities) {
//                        if (dataMIDetailPropertyItemExpiredDate.getTblMemorandumInvoiceDetail().getIddetail()
//                                == dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblMemorandumInvoiceDetail().getIddetail()
//                                && dataMIDetailPropertyItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate().equals(dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate())) {
//                            dataDetailItemExpiredDateQuantity.setItemQuantity(dataDetailItemExpiredDateQuantity.getItemQuantity().add(new BigDecimal("1")));
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (!found) {
//                        DetailItemExpiredDateQuantity detailItemExpiredDateQuantity = new DetailItemExpiredDateQuantity(dataMIDetailPropertyItemExpiredDate,
//                                new BigDecimal("1"));
//                        dataDetailItemExpiredDateQuantities.add(detailItemExpiredDateQuantity);
//                    }
//                }
//                //data detail location
//                DetailLocation detailLocation = new DetailLocation(dataDetail,
//                        getService().getDataWarehouseByIDLocation(dataDetail.getTblLocation().getIdlocation()),
//                        dataMIDetailPropertyBarcodes,
//                        dataDetailItemExpiredDateQuantities,
//                        getQuantityPO(dataMIDetail));
//                //add data to list 'detail_location'
//                list.add(detailLocation);
//            }
//        }
//        return list;
//    }
    private List<DetailLocation> loadAllDataReceivingDetail() {
        List<DetailLocation> list = new ArrayList<>();
        if (selectedData.getIdmi() != 0L) {
            List<TblMemorandumInvoiceDetail> dataMIDetails = getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(selectedData.getIdmi());
            for (TblMemorandumInvoiceDetail dataMIDetail : dataMIDetails) {
                //data mi - detail
                TblMemorandumInvoiceDetail dataDetail = dataMIDetail;
                dataDetail.setTblMemorandumInvoice(selectedData);
                if (dataDetail.getTblSupplierItem() != null) {
                    dataDetail.setTblSupplierItem(getService().getDataSupplierItem(dataDetail.getTblSupplierItem().getIdrelation()));
                    dataDetail.getTblSupplierItem().setTblItem(getService().getDataItem(dataDetail.getTblSupplierItem().getTblItem().getIditem()));
                    dataDetail.getTblSupplierItem().getTblItem().setTblUnit(getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                    if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                        dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                        dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                }
                if (dataDetail.getTblItem() != null) {
                    dataDetail.setTblItem(getService().getDataItem(dataDetail.getTblItem().getIditem()));
                    dataDetail.getTblItem().setTblUnit(getService().getDataUnit(dataDetail.getTblItem().getTblUnit().getIdunit()));
                    if (dataDetail.getTblItem().getTblItemTypeHk() != null) {
                        dataDetail.getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(dataDetail.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    if (dataDetail.getTblItem().getTblItemTypeWh() != null) {
                        dataDetail.getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(dataDetail.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                }
                dataDetail.setTblLocation(getService().getDataLocation(dataDetail.getTblLocation().getIdlocation()));
                //data mi - detail - property barcode
                List<TblMemorandumInvoiceDetailPropertyBarcode> dataMIDetailPropertyBarcodes
                        = getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(dataDetail.getIddetail());
                for (TblMemorandumInvoiceDetailPropertyBarcode dataMIDetailPropertyBarcode : dataMIDetailPropertyBarcodes) {
                    //data mi - detail
                    dataMIDetailPropertyBarcode.setTblMemorandumInvoiceDetail(dataMIDetail);
                    //data property barcode
                    dataMIDetailPropertyBarcode.setTblPropertyBarcode(getService().getDataPropertyBarcode(dataMIDetailPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                }
                //data mi - detail - item expired date
                List<TblMemorandumInvoiceDetailItemExpiredDate> dataMIDetailPropertyItemExpiredDates
                        = getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(dataDetail.getIddetail());
                for (TblMemorandumInvoiceDetailItemExpiredDate dataMIDetailPropertyItemExpiredDate : dataMIDetailPropertyItemExpiredDates) {
                    //data mi - detail
                    dataMIDetailPropertyItemExpiredDate.setTblMemorandumInvoiceDetail(dataMIDetail);
                    //data item expired
                    dataMIDetailPropertyItemExpiredDate.setTblItemExpiredDate(getService().getDataItemExpiredDate(dataMIDetailPropertyItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
//                    //data item
//                    dataMIDetailPropertyItemExpiredDate.getTblItemExpiredDate().setTblItem(getService().getDataItem(dataMIDetailPropertyItemExpiredDate.getTblMemorandumInvoiceDetail().getTblItem().getIditem()));
                }
                //data mi - detail - item expired date - quantity
                List<DetailItemExpiredDateQuantity> dataDetailItemExpiredDateQuantities = new ArrayList<>();
                for (TblMemorandumInvoiceDetailItemExpiredDate dataMIDetailPropertyItemExpiredDate : dataMIDetailPropertyItemExpiredDates) {
                    DetailItemExpiredDateQuantity detailItemExpiredDateQuantity = new DetailItemExpiredDateQuantity(dataMIDetailPropertyItemExpiredDate,
                            dataMIDetailPropertyItemExpiredDate.getItemQuantity());
                    dataDetailItemExpiredDateQuantities.add(detailItemExpiredDateQuantity);
                }
                if (dataInputStatus == 3) {   //just for show all detail data
                    if (!dataMIDetailPropertyBarcodes.isEmpty()) {    //Asset
                        for (TblMemorandumInvoiceDetailPropertyBarcode dataMIDetailPropertyBarcode : dataMIDetailPropertyBarcodes) {
                            //data detail location
                            DetailLocation detailLocation = new DetailLocation(dataDetail,
                                    getService().getDataWarehouseByIDLocation(dataDetail.getTblLocation().getIdlocation()),
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    getQuantityPO(dataMIDetail));
                            detailLocation.setDetailBarcode(dataMIDetailPropertyBarcode.getTblPropertyBarcode().getCodeBarcode());
                            detailLocation.setDetailQuantity(new BigDecimal("1"));
                            //add data to list 'detail_location'
                            list.add(detailLocation);
                        }
                    } else {
                        if (!dataDetailItemExpiredDateQuantities.isEmpty()) { //Consumable
                            for (DetailItemExpiredDateQuantity dataDetailItemExpiredDateQuantity : dataDetailItemExpiredDateQuantities) {
                                //data detail location
                                DetailLocation detailLocation = new DetailLocation(dataDetail,
                                        getService().getDataWarehouseByIDLocation(dataDetail.getTblLocation().getIdlocation()),
                                        new ArrayList<>(),
                                        new ArrayList<>(),
                                        getQuantityPO(dataMIDetail));
                                detailLocation.setDetailExpiredDate(dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate());
                                detailLocation.setDetailQuantity(new BigDecimal(String.valueOf(dataDetailItemExpiredDateQuantity.getItemQuantity())));
                                //add data to list 'detail_location'
                                list.add(detailLocation);
                            }
                        } else {  //Another
                            //data detail location
                            DetailLocation detailLocation = new DetailLocation(dataDetail,
                                    getService().getDataWarehouseByIDLocation(dataDetail.getTblLocation().getIdlocation()),
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    getQuantityPO(dataMIDetail));
                            detailLocation.setDetailQuantity(new BigDecimal(String.valueOf(detailLocation.getTblDetail().getItemQuantity())));
                            //add data to list 'detail_location'
                            list.add(detailLocation);
                        }
                    }
                } else {
                    //data detail location
                    DetailLocation detailLocation = new DetailLocation(dataDetail,
                            getService().getDataWarehouseByIDLocation(dataDetail.getTblLocation().getIdlocation()),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            getQuantityPO(dataMIDetail));
                    //add data to list 'detail_location'
                    list.add(detailLocation);
                }
            }
        }
        return list;
    }

    private BigDecimal getQuantityPO(TblMemorandumInvoiceDetail dataMIDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataMIDetail.getTblItem() == null) {
            result = getService().getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(dataMIDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataMIDetail.getTblSupplierItem().getIdrelation()).getItemQuantity();
        }
        return result;
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataReceiving;

    @FXML
    private ScrollPane spFormDataReceiving;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeReceiving;

    @FXML
    private JFXTextField txtDeliveryNumber;

    @FXML
    private JFXTextField txtCodeSupplier;

    @FXML
    private AnchorPane ancPOLayout;
    private JFXCComboBoxTablePopup<TblPurchaseOrder> cbpPO;

    @FXML
    private JFXTextArea txtReceivingNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnAddDetailBonus;

    @FXML
    private JFXButton btnRemoveDetailBonus;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblMemorandumInvoice selectedData;

    private void initFormDataReceiving() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataReceiving.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataReceiving.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.getStyleClass().add("button-save");
        btnSave.setTooltip(new Tooltip("Simpan (Data Penerimaan)"));
        btnSave.setOnAction((e) -> {
            dataReceivingSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataReceivingCancelHandle();
        });

        btnAddDetailBonus.setTooltip(new Tooltip("Buat (Data Bonus)"));
        btnAddDetailBonus.setOnAction((e) -> {
            dataReceivingDetailBonusCreateHandle();
        });

        btnRemoveDetailBonus.setTooltip(new Tooltip("Hapus (Data Bonus)"));
        btnRemoveDetailBonus.setOnAction((e) -> {
            dataReceivingDetailBonusDeleteHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtDeliveryNumber,
                cbpPO);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal");
    }

    private void initDataPopup() {
        //Purchase Order
        TableView<TblPurchaseOrder> tablePO = new TableView<>();

        TableColumn<TblPurchaseOrder, String> codePO = new TableColumn<>("No. PO");
        codePO.setCellValueFactory(cellData -> cellData.getValue().codePoProperty());
        codePO.setMinWidth(120);

        TableColumn<TblPurchaseOrder, String> supplierName = new TableColumn<>("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblSupplier() != null)
                                ? param.getValue().getTblSupplier().getSupplierName()
                                : "-",
                        param.getValue().tblSupplierProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> codeInvoice = new TableColumn("Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeInvoice.setMinWidth(120);

        TableColumn<TblPurchaseOrder, String> poDate = new TableColumn<>("Tanggal PO");
        poDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getPodate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getPodate())
                                : "-", param.getValue().podateProperty()));
        poDate.setMinWidth(160);

        TableColumn<TblPurchaseOrder, String> poDueDate = new TableColumn<>("Estimasi Tgl. Kirim");
        poDueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getPodueDate() != null)
                                ? ClassFormatter.dateFormate.format(param.getValue().getPodueDate())
                                : "-", param.getValue().podueDateProperty()));
        poDueDate.setMinWidth(160);

        tablePO.getColumns().addAll(codePO, supplierName, codeInvoice, poDate, poDueDate);

        ObservableList<TblPurchaseOrder> poItems = FXCollections.observableArrayList(loadAllDataPO());

        cbpPO = new JFXCComboBoxTablePopup<>(
                TblPurchaseOrder.class, tablePO, poItems, "", "ID - PO *", true, 720, 200
        );

        //attached to grid-pane
        ancPOLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpPO, 0.0);
        AnchorPane.setLeftAnchor(cbpPO, 0.0);
        AnchorPane.setRightAnchor(cbpPO, 0.0);
        AnchorPane.setTopAnchor(cbpPO, 0.0);
        ancPOLayout.getChildren().add(cbpPO);
    }

    public String getCodeInvoice(TblHotelPayable hotelPayable) {
        String result = "-";
        if (hotelPayable != null) {
            TblHotelPayable dataHP = getService().getDataHotelPayable(hotelPayable.getIdhotelPayable());
            if (dataHP != null
                    && dataHP.getTblHotelInvoice() != null) {
                dataHP.setTblHotelInvoice(getService().getDataHotelInvoice(dataHP.getTblHotelInvoice().getIdhotelInvoice()));
                result = dataHP.getTblHotelInvoice().getCodeHotelInvoice();
            }
        }
        return result;
    }

    private ObservableList<TblPurchaseOrder> loadAllDataPO() {
        //selected all data po in periode (due date)
//        List<TblPurchaseOrder> list = getService().getAllDataPurchaseOrderByMinPODueDate(Date.valueOf(LocalDate.now()));
        List<TblPurchaseOrder> list = getService().getAllDataPurchaseOrder();
        for (TblPurchaseOrder data : list) {
            //data supplier
            data.setTblSupplier(getService().getDataSupplier(data.getTblSupplier().getIdsupplier()));
            //data location (supplier)
            data.getTblSupplier().setTblLocation(getService().getDataLocation(data.getTblSupplier().getTblLocation().getIdlocation()));
            //data po - status
            data.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(data.getRefPurchaseOrderStatus().getIdstatus()));
            //data po - arrive-status
            data.setRefPurchaseOrderItemArriveStatus(getService().getDataPurchaseOrderItemArriveStatus(data.getRefPurchaseOrderItemArriveStatus().getIdstatus()));
        }
        //remove data po with status : '5' (!order) or data po with arrive-status = '2' (arrived)
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getRefPurchaseOrderStatus().getIdstatus() != 5
                    || list.get(i).getRefPurchaseOrderItemArriveStatus().getIdstatus() == 2) {
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Purchase Order
        ObservableList<TblPurchaseOrder> poItems = FXCollections.observableArrayList(loadAllDataPO());
        cbpPO.setItems(poItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeMi() != null
                ? selectedData.getCodeMi() : "");

        txtCodeReceiving.textProperty().bindBidirectional(selectedData.codeMiProperty());

        txtDeliveryNumber.textProperty().bindBidirectional(selectedData.deliveryNumberProperty());

        if (selectedData.getTblPurchaseOrder() != null) {
            txtCodeSupplier.setText(selectedData.getTblPurchaseOrder().getTblSupplier().getSupplierName());
        } else {
            txtCodeSupplier.setText(null);
        }

        txtReceivingNote.textProperty().bindBidirectional(selectedData.receivingNoteProperty());

        cbpPO.valueProperty().bindBidirectional(selectedData.tblPurchaseOrderProperty());
        cbpPO.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                //set data supplier
                txtCodeSupplier.setText(newVal == null
                        ? null
                        : newVal.getTblSupplier().getSupplierName());
                if (dataInputStatus != 3) {
                    //generate data detail
                    generateDataDetail(newVal);
                    //set data detail
                    setDataDetail();
                }
            }
        });

        cbpPO.hide();

        setDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeReceiving.setDisable(true);
        txtCodeSupplier.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataReceiving, dataInputStatus == 3,
                txtCodeReceiving,
                txtCodeSupplier);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    public void dataReceivingCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblMemorandumInvoice();
        detailLocations = new ArrayList<>();
        setSelectedDataToInputForm();
        //open form data receiving
        dataReceivingFormShowStatus.set(0.0);
        dataReceivingFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

//    private void dataReceivingUpdateHandle() {
//        if (tableDataReceiving.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputStatus = 1;
//            selectedData = getService().getDataMemorandumInvoice(((TblMemorandumInvoice) tableDataReceiving.getTableView().getSelectionModel().getSelectedItem()).getIdmi());
//            selectedData.setTblPurchaseOrder(getService().getDataPurchaseOrder(selectedData.getTblPurchaseOrder().getIdpo()));
//            selectedData.getTblPurchaseOrder().setTblSupplier(getService().getDataSupplier(selectedData.getTblPurchaseOrder().getTblSupplier().getIdsupplier()));
//            setSelectedDataToInputForm();
//            //open form data receiving
//            dataReceivingFormShowStatus.set(0.0);
//            dataReceivingFormShowStatus.set(1.0);
//    //set unsaving data input -> 'true'
//        ClassSession.unSavingDataInput = true;
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
//        }
//    }
    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataReceivingShowHandle() {
        if (tableDataReceiving.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getDataMemorandumInvoice(((TblMemorandumInvoice) tableDataReceiving.getTableView().getSelectionModel().getSelectedItem()).getIdmi());
            selectedData.setTblPurchaseOrder(getService().getDataPurchaseOrder(selectedData.getTblPurchaseOrder().getIdpo()));
            selectedData.getTblPurchaseOrder().setTblSupplier(getService().getDataSupplier(selectedData.getTblPurchaseOrder().getTblSupplier().getIdsupplier()));
            detailLocations = loadAllDataReceivingDetail();
            setSelectedDataToInputForm();
            dataReceivingFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataReceivingUnshowHandle() {
        refreshDataTableReceiving();
        dataReceivingFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataReceivingPrintHandle() {
        if (tableDataReceiving.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = getService().getDataMemorandumInvoice(((TblMemorandumInvoice) tableDataReceiving.getTableView().getSelectionModel().getSelectedItem()).getIdmi());
            selectedData.setTblSupplier(selectedData.getTblSupplier());
            // System.out.println("hsl >>"+selectedData.getTblPurchaseOrder().getTblSupplier().getSupplierName());
            ClassPrintReceiving dataReceiving = new ClassPrintReceiving();
            List<ClassPrintReceiving> listDataReceiving = new ArrayList();
            dataReceiving.setKodePO(selectedData.getTblPurchaseOrder().getCodePo());
            dataReceiving.setKodeReceiving(selectedData.getCodeMi());
            dataReceiving.setNamaSupplier(selectedData.getTblPurchaseOrder().getTblSupplier().getSupplierName());
            dataReceiving.setAlamatSupplier(selectedData.getTblPurchaseOrder().getTblSupplier().getSupplierAddress());
            dataReceiving.setNoTeleponSupplier(selectedData.getTblPurchaseOrder().getTblSupplier().getSupplierPhoneNumber());

            List<TblMemorandumInvoiceDetail> listReceivingDetail = getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(selectedData.getIdmi());
            List<ClassPrintReceivingDetail> listPrintReceivingDetail = new ArrayList();

            for (TblMemorandumInvoiceDetail receivingDetail : listReceivingDetail) {
                ClassPrintReceivingDetail receivingPrintDetail = new ClassPrintReceivingDetail();
                TblLocationOfWarehouse locWarehouse = getService().getDataWarehouseByIDLocation(receivingDetail.getTblLocation().getIdlocation());
                if (receivingDetail.getTblItem() == null) {
                    receivingPrintDetail.setIdBarang(receivingDetail.getTblSupplierItem().getTblItem().getCodeItem());
                    receivingPrintDetail.setNamaBarang(receivingDetail.getTblSupplierItem().getTblItem().getItemName());
                    receivingPrintDetail.setJumlah(receivingDetail.getItemQuantity());
                    receivingPrintDetail.setSatuan(receivingDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName());
                    receivingPrintDetail.setBonus("-");
                    //      receivingPrintDetail.setKeterangan(receivingDetail.getItemNote()==null ? "" : receivingDetail.getItemNote());
                } else {
                    receivingPrintDetail.setIdBarang(receivingDetail.getTblItem().getCodeItem());
                    receivingPrintDetail.setNamaBarang(receivingDetail.getTblItem().getItemName());
                    receivingPrintDetail.setJumlah(receivingDetail.getItemQuantity());
                    receivingPrintDetail.setLokasiBarang(receivingDetail.getTblLocation().getLocationName());
                    receivingPrintDetail.setSatuan(receivingDetail.getTblItem().getTblUnit().getUnitName());
                    receivingPrintDetail.setBonus("Bonus");
                }
                receivingPrintDetail.setLokasiBarang(locWarehouse.getWarehouseName());
                receivingPrintDetail.setKeterangan(receivingDetail.getItemNote() == null ? "" : receivingDetail.getItemNote());
                listPrintReceivingDetail.add(receivingPrintDetail);
            }
            dataReceiving.setReceivingDetail(listPrintReceivingDetail);
            listDataReceiving.add(dataReceiving);

            ClassPrinter.printReceiving(listDataReceiving, dataReceiving.getKodeReceiving());
        }
    }

    private void dataReceivingSaveHandle() {
        if (checkDataInputDataReceiving()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //set location (from selected data 'LocationOfWarehouse')
                for (DetailLocation detailLocation : detailLocations) {
                    if (detailLocation.getTblLocationOfWarehouse() != null) {
                        detailLocation.getTblDetail().setTblLocation(new TblLocation(detailLocation.getTblLocationOfWarehouse().getTblLocation()));
                    }
                }
                //dummy entry
                TblMemorandumInvoice dummySelectedData = new TblMemorandumInvoice(selectedData);
                dummySelectedData.setTblPurchaseOrder(new TblPurchaseOrder(dummySelectedData.getTblPurchaseOrder()));
                dummySelectedData.getTblPurchaseOrder().setRefPurchaseOrderItemArriveStatus(getPOItemArriveStatus(selectedData, detailLocations));
                List<DetailLocation> dummyDetailLocations = new ArrayList<>();
                for (DetailLocation detailLocation : detailLocations) {
                    DetailLocation dummyDetailLocation = new DetailLocation(
                            new TblMemorandumInvoiceDetail(detailLocation.getTblDetail()),
                            detailLocation.getTblLocationOfWarehouse() != null
                                    ? new TblLocationOfWarehouse(detailLocation.getTblLocationOfWarehouse())
                                    : null,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            detailLocation.getQuantityPO());
                    dummyDetailLocation.getTblDetail().setTblMemorandumInvoice(dummySelectedData);
                    if (dummyDetailLocation.getTblDetail().getTblSupplierItem() != null) {
                        dummyDetailLocation.getTblDetail().setTblSupplierItem(new TblSupplierItem(dummyDetailLocation.getTblDetail().getTblSupplierItem()));
                        dummyDetailLocation.getTblDetail().getTblSupplierItem().setTblItem(new TblItem(dummyDetailLocation.getTblDetail().getTblSupplierItem().getTblItem()));
                    }
                    if (dummyDetailLocation.getTblDetail().getTblItem() != null) {
                        dummyDetailLocation.getTblDetail().setTblItem(new TblItem(dummyDetailLocation.getTblDetail().getTblItem()));
                    }
                    if (dummyDetailLocation.getTblDetail().getTblLocation() != null) {
                        dummyDetailLocation.getTblDetail().setTblLocation(new TblLocation(dummyDetailLocation.getTblDetail().getTblLocation()));
                    }
                    if (dummyDetailLocation.getTblLocationOfWarehouse() != null
                            && dummyDetailLocation.getTblLocationOfWarehouse().getTblLocation() != null) {
                        dummyDetailLocation.getTblLocationOfWarehouse().setTblLocation(new TblLocation(dummyDetailLocation.getTblLocationOfWarehouse().getTblLocation()));
                    }
                    List<TblMemorandumInvoiceDetailPropertyBarcode> dummyDataMemorandumInvoiceDetailPropertyBarcodes = new ArrayList<>();
                    for (TblMemorandumInvoiceDetailPropertyBarcode dataMemorandumInvoiceDetailPropertyBarcode : detailLocation.getTblMemorandumInvoiceDetailPropertyBarcodes()) {
                        TblMemorandumInvoiceDetailPropertyBarcode dummyDataMemorandumInvoiceDetailPropertyBarcode = new TblMemorandumInvoiceDetailPropertyBarcode(dataMemorandumInvoiceDetailPropertyBarcode);
                        dummyDataMemorandumInvoiceDetailPropertyBarcode.setTblMemorandumInvoiceDetail(dummyDetailLocation.getTblDetail());
                        dummyDataMemorandumInvoiceDetailPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(dummyDataMemorandumInvoiceDetailPropertyBarcode.getTblPropertyBarcode()));
                        if (dummyDetailLocation.getTblDetail().getTblSupplierItem() != null) {
                            dummyDataMemorandumInvoiceDetailPropertyBarcode.getTblPropertyBarcode().setTblItem(dummyDetailLocation.getTblDetail().getTblSupplierItem().getTblItem());
                        }
                        if (dummyDetailLocation.getTblDetail().getTblItem() != null) {
                            dummyDataMemorandumInvoiceDetailPropertyBarcode.getTblPropertyBarcode().setTblItem(dummyDetailLocation.getTblDetail().getTblItem());
                        }
                        dummyDataMemorandumInvoiceDetailPropertyBarcode.getTblPropertyBarcode().setTblFixedTangibleAsset(new TblFixedTangibleAsset(dummyDataMemorandumInvoiceDetailPropertyBarcode.getTblPropertyBarcode().getTblFixedTangibleAsset()));
                        dummyDataMemorandumInvoiceDetailPropertyBarcodes.add(dummyDataMemorandumInvoiceDetailPropertyBarcode);
                    }
                    dummyDetailLocation.setTblMemorandumInvoiceDetailPropertyBarcodes(dummyDataMemorandumInvoiceDetailPropertyBarcodes);
                    List<DetailItemExpiredDateQuantity> dummyDetailItemExpiredDateQuantities = new ArrayList<>();
                    for (DetailItemExpiredDateQuantity detailItemExpiredDateQuantity : detailLocation.getDetailItemExpiredDateQuantities()) {
                        DetailItemExpiredDateQuantity dummyDetailItemExpiredDateQuantity = new DetailItemExpiredDateQuantity(
                                new TblMemorandumInvoiceDetailItemExpiredDate(detailItemExpiredDateQuantity.getDataDetailItemExporedDate()),
                                detailItemExpiredDateQuantity.getItemQuantity());
                        dummyDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().setTblMemorandumInvoiceDetail(dummyDetailLocation.getTblDetail());
                        dummyDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().setTblItemExpiredDate(new TblItemExpiredDate(dummyDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate()));
                        dummyDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().setTblItem(new TblItem(dummyDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem()));
                        dummyDetailItemExpiredDateQuantities.add(dummyDetailItemExpiredDateQuantity);
                    }
                    dummyDetailLocation.setDetailItemExpiredDateQuantities(dummyDetailItemExpiredDateQuantities);
                    dummyDetailLocations.add(dummyDetailLocation);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (getService().insertDataMemorandumInvoice(dummySelectedData,
                                dummyDetailLocations) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data receiving
                            refreshDataTableReceiving();
                            dataReceivingFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (getService().updateDataMemorandumInvoice(dummySelectedData,
                                dummyDetailLocations)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data receiving
                            refreshDataTableReceiving();
                            dataReceivingFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(getService().getErrorMessage(), null);
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

    private RefPurchaseOrderItemArriveStatus getPOItemArriveStatus(TblMemorandumInvoice dataMI,
            List<DetailLocation> detailLocations) {
        List<TblPurchaseOrderDetail> poDetails = getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(dataMI.getTblPurchaseOrder().getIdpo());
        List<TblMemorandumInvoiceDetail> midDetails = new ArrayList<>();
        List<TblMemorandumInvoice> mis = getService().getAllDataMemorandumInvoiceByIDPurchaseOrder(dataMI.getTblPurchaseOrder().getIdpo());
        for (TblMemorandumInvoice mi : mis) {
            if (dataMI.getIdmi() == 0L
                    || dataMI.getIdmi() != mi.getIdmi()) {
                List<TblMemorandumInvoiceDetail> mids = getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(mi.getIdmi());
                for (TblMemorandumInvoiceDetail mid : mids) {
                    //add data to list
                    midDetails.add(mid);
                }
            }
        }
        for (DetailLocation detailLocation : detailLocations) {
            //add data to list
            midDetails.add(detailLocation.getTblDetail());
        }
        int idItemArriveStatus = 0; //Belum Diterima = '0'
        for (TblPurchaseOrderDetail poDetail : poDetails) {
            boolean found = false;
            BigDecimal poQuantity = poDetail.getItemQuantity();
            for (TblMemorandumInvoiceDetail midDetail : midDetails) {
                if (midDetail.getTblSupplierItem() != null //!bonus
                        && midDetail.getTblSupplierItem().getIdrelation() == poDetail.getIddetail()) {   //same item (suplier-item)
                    poQuantity = poQuantity.subtract(midDetail.getItemQuantity());
                    found = true;
                }
            }
            if (found) {
                if (poQuantity.compareTo(new BigDecimal("0")) == 0) { //found all data
                    if (idItemArriveStatus == 0) { //Belum Diterima = '0'
                        idItemArriveStatus = 2; //Sudah Diterima = '2'
                    }
                } else {
                    idItemArriveStatus = 1; //Diterima Sebagian = '1'
                }
            }
        }
        return getService().getDataPurchaseOrderItemArriveStatus(idItemArriveStatus);
    }

    private void dataReceivingCancelHandle() {
        //refresh data from table & close form data receiving
        refreshDataTableReceiving();
        dataReceivingFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableReceiving() {
        tableDataReceiving.getTableView().setItems(loadAllDataReceiving());
        cft.refreshFilterItems(tableDataReceiving.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataReceiving() {
        boolean dataInput = true;
        errDataInput = "";
        dataNotEmpty = false;
        if (txtDeliveryNumber.getText() == null
                || txtDeliveryNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "No. Surat Jalan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPO.getValue() == null) {
            dataInput = false;
            errDataInput += "Purchase Order : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (!checkDataInputDataDetail()) {
            dataInput = false;
            errDataInput += "Terdapat kesalahan pada data detail barang yang diterima..! \n";
        }
        if (dataInput
                && !dataNotEmpty) {  //data was empty
            dataInput = false;
            errDataInput += "Tidak ada data barang yang harus diterima (kosong)..! \n";
        }
        return dataInput;
    }

    private final String errorMarkCSS = "button-mark-error";

    private final String warningMarkCSS = "button-mark-warning";

    private final String validMarkCSS = "button-mark-valid";

    private boolean dataNotEmpty;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        for (DetailLocation data : detailLocations) {
            data.getBtnMessage().setText("");
            setButtonMessageInfo(data.getBtnMessage(), validMarkCSS, "data telah sesuai..");
            if (data.getTblDetail().getTblItem() == null
                    && (data.getTblDetail().getTblSupplierItem() == null
                    || data.getTblDetail().getTblSupplierItem().getTblItem() == null)) {
                dataInput = false;
                setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data barang : " + ClassMessage.defaultErrorNullValueMessage + "");
            } else {
//                if (data.getTblLocation() == null) {
                if (data.getTblLocationOfWarehouse() == null
                        && (data.getTblDetail().getItemQuantity()
                        .compareTo(new BigDecimal("0")) == 1)) {
                    dataInput = false;
                    setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data lokasi : " + ClassMessage.defaultErrorNullValueMessage + "");
                } else {
                    if (data.getTblDetail().getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {
                        dataInput = false;
                        setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data jumlah barang : nilai tidak dapat kurang dari '0'");
                    } else {
                        if (data.getTblDetail().getTblItem() != null) {
                            if (data.getTblDetail().getItemQuantity()
                                    .compareTo(new BigDecimal("0")) == 0) {
                                dataInput = false;
                                setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data jumlah barang : nilai boleh kosong (sama dengan '0')");
                            } else {
                                boolean errFound = false;
                                for (TblMemorandumInvoiceDetailPropertyBarcode midPB : data.getTblMemorandumInvoiceDetailPropertyBarcodes()) {
//                                    if (midPB.getTblPropertyBarcode().getCodeBarcode() == null
//                                            || midPB.getTblPropertyBarcode().getCodeBarcode().equals("")
//                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName() == null
//                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName().equals("")) {
//                                        errFound = true;
//                                        break;
//                                    }
                                    if (midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName() == null //                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName().equals("")
                                            ) {
                                        errFound = true;
                                        break;
                                    }
                                }
                                if (errFound) {
                                    dataInput = false;
                                    setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data properti (detail) : tidak boleh kosong..");
                                }
                            }
                        } else {
                            BigDecimal maxQuantityAvailable = getMaxQuantityAvailable(data.getTblDetail());
                            if (data.getTblDetail().getItemQuantity()
                                    .compareTo(maxQuantityAvailable) == 1) {
                                dataInput = false;
                                setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data jumlah barang : nilai tidak dapat lebih besar dari '" + maxQuantityAvailable + "'");
                            } else {
                                boolean errFound = false;
                                for (TblMemorandumInvoiceDetailPropertyBarcode midPB : data.getTblMemorandumInvoiceDetailPropertyBarcodes()) {
//                                    if (midPB.getTblPropertyBarcode().getCodeBarcode() == null
//                                            || midPB.getTblPropertyBarcode().getCodeBarcode().equals("")
//                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName() == null
//                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName().equals("")) {
                                    if (midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName() == null
                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName().equals("")) {
                                        errFound = true;
                                        break;
                                    }
                                }
                                for (DetailItemExpiredDateQuantity midIEDQty : data.getDetailItemExpiredDateQuantities()) {
//                                    if (midPB.getTblPropertyBarcode().getCodeBarcode() == null
//                                            || midPB.getTblPropertyBarcode().getCodeBarcode().equals("")
//                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName() == null
//                                            || midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName().equals("")) {
                                    if (midIEDQty.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate() == null
                                            || (midIEDQty.getItemQuantity().compareTo(new BigDecimal("0")) < 1)) {
                                        errFound = true;
                                        break;
                                    }
                                }
                                if (errFound) {
                                    dataInput = false;
                                    setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data tanggal kadaluarsa (detail) : " + ClassMessage.defaultErrorNullValueMessage + "");
                                } else {
                                    if ((data.getTblDetail().getItemQuantity().compareTo(new BigDecimal("0")) == 0)
                                            && (maxQuantityAvailable.compareTo(new BigDecimal("0")) == 1)) {
                                        setButtonMessageInfo(data.getBtnMessage(), warningMarkCSS, "data jumlah barang : nilai dapat diisi dengan '" + maxQuantityAvailable + "'");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (data.getTblDetail().getItemQuantity().compareTo(new BigDecimal("0")) == 1) {
                dataNotEmpty = true;
            }
        }
        return dataInput;
    }

    private void setButtonMessageInfo(JFXButton buttonMessage, String classCSSName, String tooltipInfo) {
        buttonMessage.getStyleClass().clear();
        buttonMessage.getStyleClass().add(classCSSName);
        buttonMessage.setTooltip(new Tooltip(tooltipInfo));
    }

    private BigDecimal getMaxQuantityAvailable(TblMemorandumInvoiceDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataDetail.getTblItem() == null) {
            result = getService().getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataDetail.getTblSupplierItem().getIdrelation()).getItemQuantity();
            //+ (%allowance * po)
            SysDataHardCode receivingPercentageAllowance = getService().getDataSysDataHardCode(28);  //ReceivingPercentageAllowance = '28'
            if (receivingPercentageAllowance != null) {
                result = result.add((((new BigDecimal(receivingPercentageAllowance.getDataHardCodeValue())).divide(new BigDecimal("100"))).multiply(result)));
            }
            List<TblMemorandumInvoiceDetail> list = getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataDetail.getTblSupplierItem().getIdrelation());
            for (TblMemorandumInvoiceDetail data : list) {
                //- (receiving(s))
                result = result.subtract(data.getItemQuantity());
                List<TblReturDetail> listRD = getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(data.getTblMemorandumInvoice().getIdmi(),
                        dataDetail.getTblSupplierItem().getIdrelation());
                for (TblReturDetail dataRD : listRD) {
                    if (dataRD.getTblRetur().getRefReturStatus().getIdstatus() == 1) {    //Disetujui = '1'
                        //+ (retur(s))
                        result = result.add(dataRD.getItemQuantity());
                    }
                }
            }
        }
        return result;
    }

    /**
     * DATA (Detail)
     */
    private void generateDataDetail(TblPurchaseOrder dataPO) {
        detailLocations = new ArrayList<>();
        if (dataPO != null) {
            List<TblPurchaseOrderDetail> dataPODetails = getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(dataPO.getIdpo());
            for (TblPurchaseOrderDetail dataPODetail : dataPODetails) {
                //data mi - detail
                TblMemorandumInvoiceDetail dataDetail = new TblMemorandumInvoiceDetail();
                dataDetail.setTblMemorandumInvoice(selectedData);
                dataDetail.setTblSupplierItem(getService().getDataSupplierItem(dataPODetail.getTblSupplierItem().getIdrelation()));
                dataDetail.getTblSupplierItem().setTblItem(getService().getDataItem(dataDetail.getTblSupplierItem().getTblItem().getIditem()));
                dataDetail.getTblSupplierItem().getTblItem().setTblUnit(getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                dataDetail.setItemCost(new BigDecimal("0"));
                dataDetail.setItemQuantity(new BigDecimal("0"));
                dataDetail.setItemDiscount(new BigDecimal("0"));
                //data detail location
                DetailLocation detailLocation = new DetailLocation(dataDetail, null, new ArrayList<>(), new ArrayList<>(), dataPODetail.getItemQuantity());
                //add data to list 'detail_location'
                detailLocations.add(detailLocation);
            }
        }
    }

    @FXML
    private AnchorPane ancDataTableDetail;

    public TableView tableReceivingDetail;

//    private TableView<DetailLocation> tableView;
    public void setDataDetail() {
        ancDataTableDetail.getChildren().clear();

        tableReceivingDetail = new TableView();
//        tableReceivingDetail.setEditable(true);
        tableReceivingDetail.setEditable(dataInputStatus != 3);

        TableColumn<DetailLocation, String> location = new TableColumn("Lokasi");
        location.setMinWidth(120);
        location.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblLocationOfWarehouse() == null
                                ? ""
                                : param.getValue().getTblLocationOfWarehouse().getWarehouseName(), param.getValue().tblLocationWarehouseProperty()));
        Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory
                = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellWarehouse();
                    }
                };
        location.setCellFactory(cellFactory);
        location.setEditable(true);

//        TableColumn<DetailLocation, String> item = new TableColumn("Barang");
//        item.setMinWidth(140);
//        item.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
//                -> Bindings.createStringBinding(()
//                        -> getItemName(param.getValue()),
//                        param.getValue().tblDetailProperty()));
//        Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory1
//                = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
//                    @Override
//                    public TableCell call(TableColumn p) {
//                        return new EditingCellItem();
//                    }
//                };
//        item.setCellFactory(cellFactory1);
//        item.setEditable(true);
//
//        TableColumn<DetailLocation, String> itemType = new TableColumn("Tipe");
//        itemType.setMinWidth(150);
//        itemType.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
//                -> Bindings.createStringBinding(()
//                        -> getItemTypeName(param.getValue()),
//                        param.getValue().tblDetailProperty()));
        TableColumn<DetailLocation, String> itemSistem = new TableColumn("(Sistem)");
        itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> getItemSistem(param.getValue()),
                        param.getValue().tblDetailProperty()));
        itemSistem.setMinWidth(140);

        TableColumn<DetailLocation, String> itemSupplier = new TableColumn("(Supplier)");
        itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> getItemSupplier(param.getValue()),
                        param.getValue().tblDetailProperty()));
        itemSupplier.setMinWidth(140);

        TableColumn<DetailLocation, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(itemSistem, itemSupplier);

        TableColumn<DetailLocation, String> unitName = new TableColumn("Satuan");
        unitName.setMinWidth(120);
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> getItemUnitName(param.getValue()),
                        param.getValue().tblDetailProperty()));

        TableColumn<DetailLocation, String> isBonus = new TableColumn("Bonus");
        isBonus.setMinWidth(60);
        isBonus.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> "", param.getValue().getTblDetail().tblItemProperty()));
        isBonus.setCellFactory((TableColumn<DetailLocation, String> param) -> new CheckBoxCell<>());

        if (dataInputStatus == 3) {
            //Just Info
            TableColumn<DetailLocation, String> barcodeJustInfo = new TableColumn("Barcode");
            barcodeJustInfo.setMinWidth(120);
            barcodeJustInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(()
                            -> param.getValue().getDetailBarcode() == null
                                    ? "-"
                                    : param.getValue().getDetailBarcode(),
                            param.getValue().detailBarcode));

            TableColumn<DetailLocation, String> expiredDateJustInfo = new TableColumn("Tgl. Kadaluarsa");
            expiredDateJustInfo.setMinWidth(160);
            expiredDateJustInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(()
                            -> param.getValue().getDetailExpiredDate() == null
                                    ? "-"
                                    : ClassFormatter.dateFormate.format(param.getValue().getDetailExpiredDate()),
                            param.getValue().detailExpiredDateProperty()));

//            TableColumn<DetailLocation, String> quantityJustInfo = new TableColumn("Jumlah");
//            quantityJustInfo.setMinWidth(120);
//            quantityJustInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
//                    -> Bindings.createStringBinding(()
//                            -> param.getValue().getTblDetail() == null
//                                    ? ""
//                                    : ClassFormatter.decimalFormat.cFormat(param.getValue().getTblDetail().getItemQuantity()),
//                            param.getValue().getTblDetail().itemQuantityProperty()));
            TableColumn<DetailLocation, String> quantityJustInfo = new TableColumn("Jumlah");
            quantityJustInfo.setMinWidth(120);
            quantityJustInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(()
                            -> param.getValue().getDetailQuantity() == null
                                    ? ""
                                    : ClassFormatter.decimalFormat.cFormat(param.getValue().getDetailQuantity()),
                            param.getValue().detailQuantityProperty()));

            tableReceivingDetail.getColumns().addAll(isBonus, itemTitle, barcodeJustInfo, expiredDateJustInfo, quantityJustInfo, unitName, location);
        } else {
            TableColumn<DetailLocation, String> quantityPO = new TableColumn("Sisa PO");
            quantityPO.setMinWidth(80);
            quantityPO.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getQuantityRestOfPO(param.getValue())),
                            param.getValue().quantityPOProperty()));

            TableColumn<DetailLocation, String> percentageAllowance = new TableColumn("%allowance");
            percentageAllowance.setMinWidth(80);
            percentageAllowance.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getAllowancePercentage(param.getValue())) + "%",
                            param.getValue().tblDetailProperty()));

            TableColumn<DetailLocation, String> quantityHasBeenGet = new TableColumn("Sudah\nDiterima");
            quantityHasBeenGet.setMinWidth(80);
            quantityHasBeenGet.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getQuatityItemHasBeenGet(param.getValue())),
                            param.getValue().tblDetailProperty()));

            TableColumn<DetailLocation, String> quantityHasBeenRetur = new TableColumn("Sudah\nDi-Retur");
            quantityHasBeenRetur.setMinWidth(80);
            quantityHasBeenRetur.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getQuantityItemHasBeenRetur(param.getValue())),
                            param.getValue().tblDetailProperty()));

            TableColumn<DetailLocation, String> quantityWillGet = new TableColumn("Diterima");
            quantityWillGet.setMinWidth(80);
            quantityWillGet.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(()
                            -> param.getValue().getTblDetail() == null
                                    ? ""
                                    : ClassFormatter.decimalFormat.cFormat(param.getValue().getTblDetail().getItemQuantity()),
                            param.getValue().getTblDetail().itemQuantityProperty()));
            Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory2
                    = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
                        @Override
                        public TableCell call(TableColumn p) {
                            return new EditingCellItemQuantity();
                        }
                    };
            quantityWillGet.setCellFactory(cellFactory2);
            quantityWillGet.setEditable(true);

            TableColumn<DetailLocation, String> quantity = new TableColumn("Jumlah");
//            quantity.getColumns().addAll(quantityPO, percentageAllowance, quantityHasBeenGet, quantityHasBeenRetur, quantityWillGet);
            quantity.getColumns().addAll(quantityPO, quantityWillGet);

            TableColumn<DetailLocation, String> dataDetails = new TableColumn("Detail");
            dataDetails.setMinWidth(100);
            dataDetails.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                    -> Bindings.createStringBinding(()
                            -> "", param.getValue().cbpListDetailProperty()));
            Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory3
                    = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
                        @Override
                        public TableCell call(TableColumn p) {
                            return new EditingCellMIDDetails();
                        }
                    };
            dataDetails.setCellFactory(cellFactory3);
            dataDetails.setEditable(true);

//            TableColumn<DetailLocation, Boolean> delButton = new TableColumn("Delete");
//            delButton.setMinWidth(75);
//            delButton.setCellFactory((TableColumn<DetailLocation, Boolean> param) -> new ButtonCellDelete<>());
            TableColumn<DetailLocation, String> infoButton = new TableColumn("Status");
            infoButton.setMinWidth(60);
            infoButton.setCellFactory((TableColumn<DetailLocation, String> param) -> new ButtonCellInfo<>());

            tableReceivingDetail.getColumns().addAll(isBonus, itemTitle, quantity, percentageAllowance, unitName, dataDetails, location, infoButton);
        }
        tableReceivingDetail.setItems(FXCollections.observableArrayList(detailLocations));
        AnchorPane.setBottomAnchor(tableReceivingDetail, 0.0);
        AnchorPane.setLeftAnchor(tableReceivingDetail, 0.0);
        AnchorPane.setRightAnchor(tableReceivingDetail, 0.0);
        AnchorPane.setTopAnchor(tableReceivingDetail, 0.0);
        ancDataTableDetail.getChildren().add(tableReceivingDetail);
    }

    private String getItemSistem(DetailLocation data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem() == null) {   //bonus
                result = data.getTblDetail().getTblItem().getItemName()
                        + "\n(" + data.getTblDetail().getTblItem().getCodeItem() + ")";
            } else {  //not bonus
                result = data.getTblDetail().getTblSupplierItem().getTblItem().getItemName()
                        + "\n(" + data.getTblDetail().getTblSupplierItem().getTblItem().getCodeItem() + ")";
            }
        }
        return result;
    }

    private String getItemSupplier(DetailLocation data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem() == null) {   //bonus
                result = "-";
            } else {  //not bonus
                if (data.getTblDetail().getTblSupplierItem().getSupplierItemName() != null) {
                    result = data.getTblDetail().getTblSupplierItem().getSupplierItemName()
                            + "\n(" + data.getTblDetail().getTblSupplierItem().getSupllierItemCode() + ")";
                }
            }
        }
        return result;
    }

    private String getItemName(DetailLocation data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem() == null) {   //bonus
                result = data.getTblDetail().getTblItem().getItemName();
            } else {  //not bonus
                result = data.getTblDetail().getTblSupplierItem().getTblItem().getItemName();
            }
        }
        return result;
    }

    private String getItemTypeName(DetailLocation data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem() == null) {   //bonus
                result = (data.getTblDetail().getTblItem().getTblItemTypeHk() != null
                        ? data.getTblDetail().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + "/"
                        + (data.getTblDetail().getTblItem().getTblItemTypeWh() != null
                                ? data.getTblDetail().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (data.getTblDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)");
            } else {  //not bonus
                result = (data.getTblDetail().getTblSupplierItem().getTblItem().getTblItemTypeHk() != null
                        ? data.getTblDetail().getTblSupplierItem().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + "/"
                        + (data.getTblDetail().getTblSupplierItem().getTblItem().getTblItemTypeWh() != null
                                ? data.getTblDetail().getTblSupplierItem().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (data.getTblDetail().getTblSupplierItem().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)");
            }
        }
        return result;
    }

    private String getItemUnitName(DetailLocation data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem() == null) {   //bonus
                result = data.getTblDetail().getTblItem().getTblUnit().getUnitName();
            } else {  //not bonus
                result = data.getTblDetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName();
            }
        }
        return result;
    }

    private BigDecimal getAllowancePercentage(DetailLocation data) {
        if (data != null
                && data.getTblDetail().getTblItem() == null) {  //isn't a bonus
            SysDataHardCode sdhc = getService().getDataSysDataHardCode(28);   //ReceivingPercentageAllowance = '28'
            if (sdhc != null) {
                return new BigDecimal(sdhc.getDataHardCodeValue());
            }
        }
        return new BigDecimal("0");
    }

    private BigDecimal getQuantityRestOfPO(DetailLocation data){
        BigDecimal result = new BigDecimal("0");
        if(data != null
                && data.getTblDetail().getTblItem() == null){  //isn't a bonus
            result = data.getQuantityPO().subtract(getQuatityItemHasBeenGet(data)).add(getQuantityItemHasBeenRetur(data));
        }
        return result.compareTo(new BigDecimal("0")) == -1 ? new BigDecimal("0") : result;
    }
    
    private BigDecimal getQuatityItemHasBeenGet(DetailLocation data) {
        if (data != null
                && data.getTblDetail().getTblItem() == null) {  //isn't a bonus
            BigDecimal result = new BigDecimal("0");
            List<TblMemorandumInvoiceDetail> listMD = getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(data.getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    data.getTblDetail().getTblSupplierItem().getIdrelation());
            for (TblMemorandumInvoiceDetail dataMD : listMD) {
                //+ (receiving(s))
                result = result.add(dataMD.getItemQuantity());
            }
            return result;
        }
        return new BigDecimal("0");
    }

    private BigDecimal getQuantityItemHasBeenRetur(DetailLocation data) {
        if (data != null
                && data.getTblDetail().getTblItem() == null) {  //isn't a bonus
            BigDecimal result = new BigDecimal("0");
            List<TblMemorandumInvoiceDetail> listMD = getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(data.getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    data.getTblDetail().getTblSupplierItem().getIdrelation());
            for (TblMemorandumInvoiceDetail dataMD : listMD) {
                List<TblReturDetail> listRD = getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataMD.getTblMemorandumInvoice().getIdmi(),
                        data.getTblDetail().getTblSupplierItem().getIdrelation());
                for (TblReturDetail dataRD : listRD) {
                    if (dataRD.getTblRetur().getRefReturStatus().getIdstatus() == 1) {    //Disetujui = '1'
                        //+ (retur(s))
                        result = result.add(dataRD.getItemQuantity());
                    }
                }
            }
            return result;
        }
        return new BigDecimal("0");
    }

    class EditingCellWarehouse extends TableCell<DetailLocation, String> {

        private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpLocationOfWarehouse;

        public EditingCellWarehouse() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                cbpLocationOfWarehouse = getComboBoxWarehouse();

                ClassViewSetting.setImportantField(
                        cbpLocationOfWarehouse);

                cbpLocationOfWarehouse.hide();

                cbpLocationOfWarehouse.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                cbpLocationOfWarehouse.valueProperty().bindBidirectional(((DetailLocation) this.getTableRow().getItem()).tblLocationWarehouseProperty());

                setText(null);
                setGraphic(cbpLocationOfWarehouse);
                cbpLocationOfWarehouse.getEditor().selectAll();

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

            cbpLocationOfWarehouse.valueProperty().unbindBidirectional(((DetailLocation) this.getTableRow().getItem()).tblLocationWarehouseProperty());

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
                            if (((DetailLocation) data).getTblLocationOfWarehouse() != null) {
                                setText(((DetailLocation) data).getTblLocationOfWarehouse().getWarehouseName());
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

//    class EditingCellItem extends TableCell<DetailLocation, String> {
//
//        private JFXCComboBoxTablePopup<TblItem> cbpItem;
//
//        public EditingCellItem() {
//        }
//
//        @Override
//        public void startEdit() {
//            if (!isEmpty()) {
//                super.startEdit();
//                if (((DetailLocation) this.getTableRow().getItem()).getTblDetail() != null
//                        && ((DetailLocation) this.getTableRow().getItem()).getTblDetail().getIsBonus()) {
//                    cbpItem = getComboBoxItem(((DetailLocation) this.getTableRow().getItem()));
//
//                    cbpItem.hide();
//
//                    cbpItem.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
//
//                    cbpItem.valueProperty().bindBidirectional(((DetailLocation) this.getTableRow().getItem()).getTblDetail().tblItemProperty());
//
//                    setText(null);
//                    setGraphic(cbpItem);
//                    cbpItem.getEditor().selectAll();
//
//                }
//            }
//        }
//
//        @Override
//        public void cancelEdit() {
//            super.cancelEdit();
//
//            if (((DetailLocation) this.getTableRow().getItem()).getTblDetail() != null
//                    && ((DetailLocation) this.getTableRow().getItem()).getTblDetail().getIsBonus()) {
//
//                cbpItem.valueProperty().unbindBidirectional(((DetailLocation) this.getTableRow().getItem()).getTblDetail().tblItemProperty());
//
//                setText((String) getItem());
//                setGraphic(null);
//            }
//        }
//
//        @Override
//        public void updateItem(String item, boolean empty) {
//            super.updateItem(item, empty);
//            if (!empty) {
//                if (!isEditing()) {
//                    if (this.getTableRow() != null) {
//                        Object data = this.getTableRow().getItem();
//                        if (data != null) {
//                            if (((DetailLocation) data).getTblDetail() != null
//                                    && ((DetailLocation) data).getTblDetail().getTblItem() != null) {
//                                setText(((DetailLocation) data).getTblDetail().getTblItem().getItemName());
//                            } else {
//                                setText("");
//                            }
//                            setGraphic(null);
//                        } else {
//                            setText(null);
//                            setGraphic(null);
//                        }
//                    } else {
//                        setText(null);
//                        setGraphic(null);
//                    }
//                }
//            }
//        }
//
//    }
    class EditingCellItemQuantity extends TableCell<DetailLocation, String> {

        private JFXTextField tItemQuantity;

        public EditingCellItemQuantity() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null
                    && !isConsumable(((DetailLocation) this.getTableRow().getItem()).getTblDetail())) {  //!consumable
                super.startEdit();
                tItemQuantity = new JFXTextField();
                tItemQuantity.setPromptText("Jumlah Barang");

                ClassViewSetting.setImportantField(
                        tItemQuantity);

                tItemQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", tItemQuantity);

                Bindings.bindBidirectional(tItemQuantity.textProperty(), ((DetailLocation) this.getTableRow().getItem()).getTblDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(tItemQuantity);
                tItemQuantity.selectAll();

                //cell input color
                if (this.getTableRow() != null
                        && !isConsumable(((DetailLocation) this.getTableRow().getItem()).getTblDetail()) //!consumable
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

            tItemQuantity.textProperty().unbindBidirectional(((DetailLocation) this.getTableRow().getItem()).getTblDetail().itemQuantityProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null
                    && !isConsumable(((DetailLocation) this.getTableRow().getItem()).getTblDetail()) //!consumable
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
                            if (((DetailLocation) data).getTblDetail() != null) {
                                setText(ClassFormatter.decimalFormat.cFormat(((DetailLocation) data).getTblDetail().getItemQuantity()));
                            } else {
                                setText("");
                            }
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (!isConsumable(((DetailLocation) this.getTableRow().getItem()).getTblDetail()) //!consumable
                                && dataInputStatus != 3) {
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

        private boolean isConsumable(TblMemorandumInvoiceDetail dataDetail) {
            if (dataDetail != null) {
                if (dataDetail.getTblSupplierItem() == null) {    //bonus
                    return dataDetail.getTblItem().getConsumable();
                } else {  //not bonus
                    return dataDetail.getTblSupplierItem().getTblItem().getConsumable();
                }
            }
            return false;
        }

    }

    class EditingCellMIDDetails extends TableCell<DetailLocation, String> {

        private JFXCComboBoxPopup cbpPropertyDetails;

        public EditingCellMIDDetails() {

        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (this.getTableRow() != null) {
                    Object data = this.getTableRow().getItem();

                    if (data != null) {
                        cbpPropertyDetails = ((DetailLocation) data).getCbpListDetail();

                        cbpPropertyDetails.getStyleClass().add("detail-combo-box-popup");

                        cbpPropertyDetails.showingProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal) {
                                this.getTableView().getSelectionModel().clearAndSelect(this.getTableRow().getIndex());
                            }
                        });

                        cbpPropertyDetails.hide();

                        cbpPropertyDetails.setPrefSize(100, 25);
                        setAlignment(Pos.CENTER);

                        if (isConsumable(((DetailLocation) this.getTableRow().getItem()).getTblDetail())) { //consumable
                            cbpPropertyDetails.setDisable(false);
                        } else {
                            if (isAsset(((DetailLocation) data).getTblDetail())
                                    && dataInputStatus == 3) {
                                cbpPropertyDetails.setDisable(false);
                            } else {
                                cbpPropertyDetails.setDisable(true);
                            }
                        }

                        setText(null);
                        setGraphic(cbpPropertyDetails);

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

        @Override
        public void startEdit() {
//            super.startEdit();
        }

        @Override
        public void cancelEdit() {
//            super.cancelEdit();
        }

        private boolean isConsumable(TblMemorandumInvoiceDetail dataDetail) {
            if (dataDetail != null) {
                if (dataDetail.getTblSupplierItem() == null) {    //bonus
                    return dataDetail.getTblItem().getConsumable();
                } else {  //not bonus
                    return dataDetail.getTblSupplierItem().getTblItem().getConsumable();
                }
            }
            return false;
        }

        private boolean isAsset(TblMemorandumInvoiceDetail dataDetail) {
            if (dataDetail != null) {
                if (dataDetail.getTblSupplierItem() == null) {    //bonus
                    return dataDetail.getTblItem().getPropertyStatus();   //Property
                } else {  //not bonus
                    return dataDetail.getTblSupplierItem().getTblItem().getPropertyStatus();  //Property
                }
            }
            return false;
        }

    }

    public class CheckBoxCell<S, T> extends TableCell<S, T> {

        private final JFXCheckBox checkBox;

        public CheckBoxCell() {
            checkBox = new JFXCheckBox();
            checkBox.setAlignment(Pos.CENTER);
            checkBox.setDisable(true);
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
                if (this.getTableRow() != null) {
                    Object data = this.getTableRow().getItem();
                    if (data != null) {
                        checkBox.selectedProperty().bind(Bindings.isNotNull(((DetailLocation) data).getTblDetail().tblItemProperty()));
                        setAlignment(Pos.CENTER);

                        setText(null);
                        setGraphic(checkBox);
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

        @Override
        public void startEdit() {
            super.startEdit();
            if (isEmpty()) {
                return;
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

    }

    public class ButtonCellDelete<S, T> extends TableCell<S, T> {

        private JFXButton button = new JFXButton();

        public ButtonCellDelete() {

        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (this.getTableRow() != null) {
                Object data = this.getTableRow().getItem();
                if (data != null) {
                    button = new JFXButton();
                    button.getStyleClass().add("button-delete");
                    button.setButtonType(JFXButton.ButtonType.RAISED);
                    button.setTooltip(new Tooltip("Hapus Data"));
                    button.setPrefSize(25, 25);
                    setAlignment(Pos.CENTER);
                    button.setOnAction((e) -> {
                        Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
                        if (alert.getResult() == ButtonType.OK) {
                            //remove data from list
                            detailLocations.remove((DetailLocation) data);
                            //refresh data view
                            setDataDetail();
                        }
                    });
//                    button.setDisable(!((DetailLocation) data).getTblDetail().getTblItem().getPropertyStatus());
                    button.setDisable(((DetailLocation) data).getTblDetail().getTblItem() == null);
                } else {
                    button = new JFXButton();
                    button.setDisable(true);
                }
            }
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(null);
                setGraphic(button);
            }
        }

        @Override
        public void startEdit() {

        }

        @Override
        public void cancelEdit() {

        }

    }

    public class ButtonCellInfo<S, T> extends TableCell<S, T> {

        private JFXButton button;

        public ButtonCellInfo() {

        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (this.getTableRow() != null) {
                    Object data = this.getTableRow().getItem();
                    if (data != null) {
                        this.button = ((DetailLocation) data).getBtnMessage();
                        setAlignment(Pos.CENTER);

                        setText(null);
                        setGraphic(button);
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

        @Override
        public void startEdit() {

        }

        @Override
        public void cancelEdit() {

        }

    }

    class EditingCellCodePB extends TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String> {

        private JFXTextField tCodePB;

        public EditingCellCodePB() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                tCodePB = new JFXTextField();
                tCodePB.setPromptText("Properti");

                tCodePB.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                tCodePB.textProperty().bindBidirectional(((TblMemorandumInvoiceDetailPropertyBarcode) this.getTableRow().getItem()).getTblPropertyBarcode().codeBarcodeProperty());

                setText(null);
                setGraphic(tCodePB);
                tCodePB.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            tCodePB.textProperty().unbindBidirectional(((TblMemorandumInvoiceDetailPropertyBarcode) this.getTableRow().getItem()).getTblPropertyBarcode().codeBarcodeProperty());

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            if (((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode() != null) {
                                setText(((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode().getCodeBarcode());
                            } else {
                                setText("");
                            }
                            setGraphic(null);
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
        }

    }

    class EditingCellAssetName extends TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String> {

        private JFXTextField tAssetName;

        public EditingCellAssetName() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                tAssetName = new JFXTextField();
                tAssetName.setPromptText("Aset *");

                tAssetName.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                tAssetName.textProperty().bindBidirectional(((TblMemorandumInvoiceDetailPropertyBarcode) this.getTableRow().getItem()).getTblPropertyBarcode().getTblFixedTangibleAsset().assetNameProperty());

                setText(null);
                setGraphic(tAssetName);
                tAssetName.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            tAssetName.textProperty().unbindBidirectional(((TblMemorandumInvoiceDetailPropertyBarcode) this.getTableRow().getItem()).getTblPropertyBarcode().getTblFixedTangibleAsset().assetNameProperty());

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            if (((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode() != null
                                    && ((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode().getTblFixedTangibleAsset() != null) {
                                setText(((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName());
                            } else {
                                setText("");
                            }
                            setGraphic(null);
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
        }

    }

    class EditingCellAssetNote extends TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String> {

        private JFXTextField tAssetNote;

        public EditingCellAssetNote() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                tAssetNote = new JFXTextField();
                tAssetNote.setPromptText("Keterangan *");

                tAssetNote.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                tAssetNote.textProperty().bindBidirectional(((TblMemorandumInvoiceDetailPropertyBarcode) this.getTableRow().getItem()).getTblPropertyBarcode().getTblFixedTangibleAsset().assetNoteProperty());

                setText(null);
                setGraphic(tAssetNote);
                tAssetNote.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            tAssetNote.textProperty().unbindBidirectional(((TblMemorandumInvoiceDetailPropertyBarcode) this.getTableRow().getItem()).getTblPropertyBarcode().getTblFixedTangibleAsset().assetNoteProperty());

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            if (((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode() != null
                                    && ((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode().getTblFixedTangibleAsset() != null) {
                                setText(((TblMemorandumInvoiceDetailPropertyBarcode) data).getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetNote());
                            } else {
                                setText("");
                            }
                            setGraphic(null);
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
        }

    }

    class EditingCellItemExpiredDate extends TableCell<DetailItemExpiredDateQuantity, String> {

        private JFXDatePicker dtpItemExpiredDate;

        public EditingCellItemExpiredDate() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                dtpItemExpiredDate = new JFXDatePicker();
                dtpItemExpiredDate.setPromptText("Tgl. Kadaluarsa *");
//                dtpItemExpiredDate.setLabelFloat(false);@@@
                dtpItemExpiredDate.setEditable(false);

                ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                        dtpItemExpiredDate);
                ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                        dtpItemExpiredDate);

                ClassViewSetting.setImportantField(
                        dtpItemExpiredDate);

                dtpItemExpiredDate.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                if (((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate() != null) {
//                    dtpItemExpiredDate.setValue(((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    dtpItemExpiredDate.setValue(LocalDate.of(((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate().getYear() + 1900,
                            ((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate().getMonth() + 1,
                            ((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate().getDate()));
                }

                dtpItemExpiredDate.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        ((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).getDataDetailItemExporedDate().getTblItemExpiredDate().setItemExpiredDate(Date.valueOf(newVal));
                    }
                });

                setText(null);
                setGraphic(dtpItemExpiredDate);
//                tAssetNote.selectAll();

//                //cell input color
//                if (this.getTableRow() != null
//                        && dataInputStatus != 3) {
//                    if (this.getTableRow().getIndex() % 2 == 0) {
//                        getStyleClass().add("cell-input-even-edit");
//
//                    } else {
//                        getStyleClass().add("cell-input-odd-edit");
//                    }
//                }
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
            setText((String) getItem());
            setGraphic(null);

//            //cell input color
//            if (this.getTableRow() != null
//                    && dataInputStatus != 3) {
//                if (this.getTableRow().getIndex() % 2 == 0) {
//                    getStyleClass().add("cell-input-even");
//
//                } else {
//                    getStyleClass().add("cell-input-odd");
//                }
//            }
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
                            if (((DetailItemExpiredDateQuantity) data).getDataDetailItemExporedDate() != null
                                    && ((DetailItemExpiredDateQuantity) data).getDataDetailItemExporedDate().getTblItemExpiredDate() != null) {
                                setText(ClassFormatter.dateFormate.format(((DetailItemExpiredDateQuantity) data).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate()));
                            } else {
                                setText("");
                            }
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
//                        //cell input color
//                        if (this.getTableRow() != null
//                                && dataInputStatus != 3) {
//                            if (this.getTableRow().getIndex() % 2 == 0) {
//                                getStyleClass().add("cell-input-even");
//
//                            } else {
//                                getStyleClass().add("cell-input-odd");
//                            }
//                        }
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

    class EditingCellItemExpiredDateQuantity extends TableCell<DetailItemExpiredDateQuantity, String> {

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

                Bindings.bindBidirectional(tItemExpiredQuantity.textProperty(), ((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

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

            tItemExpiredQuantity.textProperty().unbindBidirectional(((DetailItemExpiredDateQuantity) this.getTableRow().getItem()).itemQuantityProperty());

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
                            setText(ClassFormatter.decimalFormat.cFormat(((DetailItemExpiredDateQuantity) data).getItemQuantity()));
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

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> getComboBoxWarehouse() {
        TableView<TblLocationOfWarehouse> tableWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn<>("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(140);

        tableWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());

        JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableWarehouse, warehouseItems, "", " ", true, 200, 200
        );

        cbpWarehouse.setLabelFloat(false);

        return cbpWarehouse;
    }

//    private JFXCComboBoxTablePopup<TblItem> getComboBoxItem(DetailLocation detailLocation) {
//        TableView<TblItem> tableItem = new TableView<>();
//        TableColumn<TblItem, String> codeItem = new TableColumn<>("ID Barang");
//        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
//        codeItem.setMinWidth(120);
//
//        TableColumn<TblItem, String> itemName = new TableColumn<>("Barang");
//        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
//        itemName.setMinWidth(140);
//
//        tableItem.getColumns().addAll(codeItem, itemName);
//
//        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem(detailLocation));
//
//        JFXCComboBoxTablePopup<TblItem> cbpItem = new JFXCComboBoxTablePopup<>(
//                TblItem.class, tableItem, itemItems, "", "Barang *", true, 280, 200
//        );
//
//        cbpItem.setLabelFloat(false);
//
//        return cbpItem;
//    }
    private JFXCComboBoxPopup getComboBoxPropertyDetails(List<TblMemorandumInvoiceDetailPropertyBarcode> tblMemorandumInvoiceDetailPropertyBarcodes) {

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

//        Label label = new Label("Properties --");
//        label.getStyleClass().add("label-popup-properties");
//        label.setPrefSize(140, 30);
        AnchorPane ancDataTableProperties = new AnchorPane();

        TableView<TblMemorandumInvoiceDetailPropertyBarcode> tableProperties = new TableView<>();
//        tableProperties.setEditable(true);
        tableProperties.setEditable(dataInputStatus != 3);

        TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String> codeProperty = new TableColumn("Barcode");
        codeProperty.setMinWidth(140);
        codeProperty.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoiceDetailPropertyBarcode, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblPropertyBarcode() == null
                                ? ""
                                : param.getValue().getTblPropertyBarcode().getCodeBarcode(), param.getValue().getTblPropertyBarcode().codeBarcodeProperty()));
//        Callback<TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String>, TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String>> cellFactory1
//                = new Callback<TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String>, TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String>>() {
//                    @Override
//                    public TableCell call(TableColumn p) {
//                        return new EditingCellCodePB();
//                    }
//                };
//        codeProperty.setCellFactory(cellFactory1);
//        codeProperty.setEditable(true);

        TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String> assetName = new TableColumn("Aset");
        assetName.setMinWidth(140);
        assetName.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoiceDetailPropertyBarcode, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblPropertyBarcode() == null
                                ? ""
                                : (param.getValue().getTblPropertyBarcode().getTblFixedTangibleAsset() == null
                                        ? ""
                                        : param.getValue().getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetName()), param.getValue().getTblPropertyBarcode().getTblFixedTangibleAsset().assetNameProperty()));
        Callback<TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String>, TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String>> cellFactory2
                = new Callback<TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String>, TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellAssetName();
                    }
                };
        assetName.setCellFactory(cellFactory2);
        assetName.setEditable(true);

        TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String> assetNote = new TableColumn("Keterangan");
        assetNote.setMinWidth(140);
        assetNote.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoiceDetailPropertyBarcode, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblPropertyBarcode() == null
                                ? ""
                                : (param.getValue().getTblPropertyBarcode().getTblFixedTangibleAsset() == null
                                        ? ""
                                        : param.getValue().getTblPropertyBarcode().getTblFixedTangibleAsset().getAssetNote()), param.getValue().getTblPropertyBarcode().getTblFixedTangibleAsset().assetNoteProperty()));
        Callback<TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String>, TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String>> cellFactory3
                = new Callback<TableColumn<TblMemorandumInvoiceDetailPropertyBarcode, String>, TableCell<TblMemorandumInvoiceDetailPropertyBarcode, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellAssetNote();
                    }
                };
        assetNote.setCellFactory(cellFactory3);
        assetNote.setEditable(true);

        tableProperties.getColumns().addAll(codeProperty, assetName);
        tableProperties.setItems(FXCollections.observableArrayList(tblMemorandumInvoiceDetailPropertyBarcodes));
        AnchorPane.setBottomAnchor(tableProperties, 0.0);
        AnchorPane.setLeftAnchor(tableProperties, 0.0);
        AnchorPane.setRightAnchor(tableProperties, 0.0);
        AnchorPane.setTopAnchor(tableProperties, 0.0);
        ancDataTableProperties.getChildren().add(tableProperties);

//        GridPane gpContent = new GridPane();
//        gpContent.setHgap(20);
//        gpContent.setVgap(15);
//        gpContent.add(ancDataTableProperties, 1, 1, 2, 1);
//        gpContent.add(label, 1, 1);
//        gpContent.add(ancDataTableProperties, 1, 2, 2, 1);
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
        content.setPrefSize(300, 200);

        content.setCenter(ancContent);

        cbpPropertyDetails.setPopupEditor(false);
        cbpPropertyDetails.promptTextProperty().set("");
        cbpPropertyDetails.setLabelFloat(false);
        cbpPropertyDetails.setPopupButton(button);
        cbpPropertyDetails.settArrowButton(true);
        cbpPropertyDetails.setPopupContent(content);

        return cbpPropertyDetails;
    }

    private JFXCComboBoxPopup getComboBoxItemExpiredDateDetails(List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities) {

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

//        Label label = new Label("Properties --");
//        label.getStyleClass().add("label-popup-properties");
//        label.setPrefSize(140, 30);
        AnchorPane ancDataTableProperties = new AnchorPane();

        //set data table
        TableView<DetailItemExpiredDateQuantity> tableItemExpiredDate = new TableView<>();
//        tableItemExpiredDate.setEditable(true);
        tableItemExpiredDate.setEditable(dataInputStatus != 3);

        TableColumn<DetailItemExpiredDateQuantity, String> expiredDate = new TableColumn("Tgl. Kadaluarsa");
        expiredDate.setMinWidth(160);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<DetailItemExpiredDateQuantity, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getDataDetailItemExporedDate() == null
                                ? ""
                                : (param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate() == null
                                        ? ""
                                        : ClassFormatter.dateFormate.format(param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate())),
                        param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate().itemExpiredDateProperty()));
        Callback<TableColumn<DetailItemExpiredDateQuantity, String>, TableCell<DetailItemExpiredDateQuantity, String>> cellFactory2
                = new Callback<TableColumn<DetailItemExpiredDateQuantity, String>, TableCell<DetailItemExpiredDateQuantity, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemExpiredDate();
                    }
                };
        expiredDate.setCellFactory(cellFactory2);
        expiredDate.setEditable(true);

        TableColumn<DetailItemExpiredDateQuantity, String> justSpace = new TableColumn("");
        justSpace.setMinWidth(3);
        justSpace.setMaxWidth(3);

        TableColumn<DetailItemExpiredDateQuantity, String> quantity = new TableColumn("Jumlah (Barang)");
        quantity.setMinWidth(140);
        quantity.setCellValueFactory((TableColumn.CellDataFeatures<DetailItemExpiredDateQuantity, String> param)
                -> Bindings.createStringBinding(()
                        -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()),
                        param.getValue().itemQuantityProperty()));
        Callback<TableColumn<DetailItemExpiredDateQuantity, String>, TableCell<DetailItemExpiredDateQuantity, String>> cellFactory3
                = new Callback<TableColumn<DetailItemExpiredDateQuantity, String>, TableCell<DetailItemExpiredDateQuantity, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemExpiredDateQuantity();
                    }
                };
        quantity.setCellFactory(cellFactory3);
        quantity.setEditable(true);

        tableItemExpiredDate.getColumns().addAll(expiredDate, justSpace, quantity);
        tableItemExpiredDate.setItems(FXCollections.observableArrayList(detailItemExpiredDateQuantities));

        //set data table - control        
        ClassTableWithControl tableControl = new ClassTableWithControl(tableItemExpiredDate);

        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("+");
            buttonControl.setTooltip(new Tooltip("Tambah Data"));
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReceivingDetailItemExpiredDateCreateHandle(cbpItemExpiredDetails,
                        detailItemExpiredDateQuantities);
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("x");
            buttonControl.setTooltip(new Tooltip("Hapus Data"));
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReceivingDetailItemExpiredDateDeleteHandle(cbpItemExpiredDetails,
                        tableControl,
                        detailItemExpiredDateQuantities);
            });
            buttonControls.add(buttonControl);
        }
        tableControl.addButtonControl(buttonControls);
        tableControl.getToolbarControl().setDisable(dataInputStatus == 3);

        //set to layout (data popup) 
        AnchorPane.setBottomAnchor(tableControl, 0.0);
        AnchorPane.setLeftAnchor(tableControl, 0.0);
        AnchorPane.setRightAnchor(tableControl, 0.0);
        AnchorPane.setTopAnchor(tableControl, 0.0);
        ancDataTableProperties.getChildren().add(tableControl);

//        GridPane gpContent = new GridPane();
//        gpContent.setHgap(20);
//        gpContent.setVgap(15);
//        gpContent.add(ancDataTableProperties, 1, 1, 2, 1);
//        gpContent.add(label, 1, 1);
//        gpContent.add(ancDataTableProperties, 1, 2, 2, 1);
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
        content.setPrefSize(360, 300);

        content.setCenter(ancContent);

        cbpItemExpiredDetails.setPopupEditor(false);
        cbpItemExpiredDetails.promptTextProperty().set("");
        cbpItemExpiredDetails.setLabelFloat(false);
        cbpItemExpiredDetails.setPopupButton(button);
        cbpItemExpiredDetails.settArrowButton(true);
        cbpItemExpiredDetails.setPopupContent(content);

        return cbpItemExpiredDetails;
    }

    public DetailItemExpiredDateQuantity selectedDataDetailItemExpiredDateQuantity;

    /**
     * HANDLE (MIDetail - ItemExpiredDate)
     */
//    public Stage dialogStageDetalItemExpiredDate;   
    public int dataDetailItemExpiredDateQuantityInputStatus = 1;    //update

    private void dataReceivingDetailItemExpiredDateCreateHandle(JFXCComboBoxPopup comboBoxPopup,
            List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities) {
        dataDetailItemExpiredDateQuantityInputStatus = 0;   //insert
        //data mi detail
        TblMemorandumInvoiceDetail dataMIDetail = ((DetailLocation) tableReceivingDetail.getSelectionModel().getSelectedItem()).getTblDetail();
        //data item expireddate
        TblItemExpiredDate dataItemExpiredDate = new TblItemExpiredDate();
        if (dataMIDetail.getTblSupplierItem() == null) {  //bonus
            dataItemExpiredDate.setTblItem(dataMIDetail.getTblItem());
        } else {  //not bonus
            dataItemExpiredDate.setTblItem(dataMIDetail.getTblSupplierItem().getTblItem());
        }
        //data mid detail - item expireddate
        TblMemorandumInvoiceDetailItemExpiredDate dataMIDetailItemExpiredDate = new TblMemorandumInvoiceDetailItemExpiredDate();
        dataMIDetailItemExpiredDate.setTblMemorandumInvoiceDetail(dataMIDetail);
        dataMIDetailItemExpiredDate.setTblItemExpiredDate(dataItemExpiredDate);
        //data detail - item expireddate - quantity
        selectedDataDetailItemExpiredDateQuantity = new DetailItemExpiredDateQuantity(dataMIDetailItemExpiredDate,
                new BigDecimal("0"));
        //open form data - item-expireddate
        showDataDetailItemExpiredDateDialog(comboBoxPopup,
                detailItemExpiredDateQuantities);
    }

    private void dataReceivingDetailItemExpiredDateDeleteHandle(
            JFXCComboBoxPopup comboBoxPopup,
            ClassTableWithControl tableControl,
            List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities) {
        dataDetailItemExpiredDateQuantityInputStatus = 2;   //delete
        if (tableControl.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //remove data from list
                DetailItemExpiredDateQuantity dataItemExpiredDateQuantity = (DetailItemExpiredDateQuantity) tableControl.getTableView().getSelectionModel().getSelectedItem();
                detailItemExpiredDateQuantities.remove(dataItemExpiredDateQuantity);
                //set data mi detail (item quantity)
                TblMemorandumInvoiceDetail dataMIDetail = ((DetailLocation) tableReceivingDetail.getSelectionModel().getSelectedItem()).getTblDetail();
                dataMIDetail.setItemQuantity(dataMIDetail.getItemQuantity().subtract(dataItemExpiredDateQuantity.getItemQuantity()));
//            comboBoxPopup.show();
            } else {
                comboBoxPopup.show();
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
            comboBoxPopup.show();
        }
        //reset data detail item expired date qunatity input status to '1' (update)
        dataDetailItemExpiredDateQuantityInputStatus = 1;   //update
    }

    private void showDataDetailItemExpiredDateDialog(JFXCComboBoxPopup comboBoxPopup,
            List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_receiving/receiving/ReceivingDetailItemExpiredDateQuantityDialog.fxml"));

            ReceivingDetailItemExpiredDateQuantityController controller = new ReceivingDetailItemExpiredDateQuantityController(this,
                    comboBoxPopup,
                    detailItemExpiredDateQuantities);
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

    private JFXCComboBoxPopup getComboBoxDetails() {

        JFXCComboBoxPopup cbpDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-property-details");
        button.setTooltip(new Tooltip("Properti"));
        button.setOnMouseClicked((e) -> cbpDetails.show());

//        Label label = new Label("Properties --");
//        label.getStyleClass().add("label-popup-properties");
//        label.setPrefSize(140, 30);
        AnchorPane ancDataTableProperties = new AnchorPane();

//        GridPane gpContent = new GridPane();
//        gpContent.setHgap(20);
//        gpContent.setVgap(15);
//        gpContent.add(ancDataTableProperties, 1, 1, 2, 1);
//        gpContent.add(label, 1, 1);
//        gpContent.add(ancDataTableProperties, 1, 2, 2, 1);
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

//    private List<TblItem> loadAllDataItem(DetailLocation detailLocation) {
//        List<TblItem> list = getService().getAllDataItem();
//        for (TblItem data : list) {
//            //data unit
//            data.setTblUnit(getService().getDataUnit(data.getTblUnit().getIdunit()));
//            //data item type
//            data.setRefItemType(getService().getDataItemType(data.getPropertyStatus()));
//        }
//        for (int i = list.size() - 1; i > -1; i--) {
//            for (int j = 0; j < detailLocations.size(); j++) {
//                if (detailLocations.get(j).getTblDetail().getTblItem().getIditem() == list.get(i).getIditem()
//                        && detailLocations.get(j).getTblDetail().getIsBonus()) {
//                    if (detailLocation.getTblDetail().getTblItem().getIditem() != list.get(i).getIditem()) {
//                        list.remove(i);
//                    }
//                    break;
//                }
//            }
//        }
//        return list;
//    }
    private List<TblLocationOfWarehouse> loadAllDataLocationOfWarehouse() {
        List<TblLocationOfWarehouse> list = getService().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    public class DetailLocation {

        private final ObjectProperty<TblMemorandumInvoiceDetail> tblDetail = new SimpleObjectProperty<>();

        private final ObjectProperty<TblLocationOfWarehouse> tblLocationOfWarehouse = new SimpleObjectProperty<>();

        private final ObservableList<TblMemorandumInvoiceDetailPropertyBarcode> tblMemorandumInvoiceDetailPropertyBarcodes = FXCollections.observableArrayList();

        private final ObservableList<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities = FXCollections.observableArrayList();

        private final ObjectProperty<JFXCComboBoxPopup> cbpListDetail = new SimpleObjectProperty<>();

        private JFXButton btnMessage = new JFXButton("?");

        private final ObjectProperty<BigDecimal> quantityPO = new SimpleObjectProperty<>(new BigDecimal("0"));

        //FOR VIEW DETAIL (detailInputStatus == 3)
        private final StringProperty detailBarcode = new SimpleStringProperty();

        private final ObjectProperty<java.util.Date> detailExpiredDate = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> detailQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        public DetailLocation(TblMemorandumInvoiceDetail tblDetail,
                TblLocationOfWarehouse tblLocationOfWarehouse,
                List<TblMemorandumInvoiceDetailPropertyBarcode> tblMemorandumInvoiceDetailPropertyBarcodes,
                List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities,
                BigDecimal quantityPO) {
            //data detail
            this.tblDetail.set(tblDetail);
            if (this.tblDetail.get().getTblSupplierItem() == null) {  //bonus
                //add listener for item & item qunatity -> change mi_detail - property_barcode list
                this.tblDetail.get().tblItemProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null) {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails());
                    } else {
                        if (newVal.getPropertyStatus()) {   //Property
                            //data cb-list combobox
                            if (this.tblDetail.get().getItemQuantity() != null) {
                                List<TblMemorandumInvoiceDetailPropertyBarcode> listData = new ArrayList<>();
                                for (double i = 0; i < this.tblDetail.get().getItemQuantity().doubleValue(); i++) {
                                    TblMemorandumInvoiceDetailPropertyBarcode newData = new TblMemorandumInvoiceDetailPropertyBarcode();
                                    newData.setTblMemorandumInvoiceDetail(this.tblDetail.get());
                                    newData.setTblPropertyBarcode(new TblPropertyBarcode());
                                    newData.getTblPropertyBarcode().setTblItem(this.tblDetail.get().getTblItem());
                                    newData.getTblPropertyBarcode().setTblFixedTangibleAsset(new TblFixedTangibleAsset());
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setBeginValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setCurrentValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setEconomicLife(new BigDecimal("0"));
                                    listData.add(newData);
                                }
                                this.tblMemorandumInvoiceDetailPropertyBarcodes.setAll(listData);
                                this.cbpListDetail.set(getComboBoxPropertyDetails(this.tblMemorandumInvoiceDetailPropertyBarcodes));
                            }
                        } else {
                            if (newVal.getConsumable()) {    //consumable
                                //data cb-list combobox
                                List<DetailItemExpiredDateQuantity> listData = new ArrayList<>();
                                this.detailItemExpiredDateQuantities.setAll(listData);
                                this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.detailItemExpiredDateQuantities));
                            } else {
                                //data cb-list combobox
                                this.cbpListDetail.set(getComboBoxDetails());
                            }
                        }
                    }
                });
                this.tblDetail.get().itemQuantityProperty().addListener((obs, oldVal, newVal) -> {
                    if (this.tblDetail.get().getTblItem() == null) {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails());
                    } else {
                        if (this.tblDetail.get().getTblItem().getPropertyStatus()) {   //Property
                            //data cb-list combobox
                            if (newVal != null) {
                                List<TblMemorandumInvoiceDetailPropertyBarcode> listData = new ArrayList<>();
                                for (double i = 0; i < newVal.doubleValue(); i++) {
                                    TblMemorandumInvoiceDetailPropertyBarcode newData = new TblMemorandumInvoiceDetailPropertyBarcode();
                                    newData.setTblMemorandumInvoiceDetail(this.tblDetail.get());
                                    newData.setTblPropertyBarcode(new TblPropertyBarcode());
                                    newData.getTblPropertyBarcode().setTblItem(this.tblDetail.get().getTblItem());
                                    newData.getTblPropertyBarcode().setTblFixedTangibleAsset(new TblFixedTangibleAsset());
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setBeginValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setCurrentValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setEconomicLife(new BigDecimal("0"));
                                    //set asset name
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setAssetName(newData.getTblMemorandumInvoiceDetail().getTblItem().getItemName());
                                    listData.add(newData);
                                }
                                this.tblMemorandumInvoiceDetailPropertyBarcodes.setAll(listData);
                                this.cbpListDetail.set(getComboBoxPropertyDetails(this.tblMemorandumInvoiceDetailPropertyBarcodes));
                            }
                        } else {
                            if (this.tblDetail.get().getTblItem().getConsumable()) {    //consumable
                                if (dataDetailItemExpiredDateQuantityInputStatus != 1) {    //if not updating data, 1 = 'update'
                                    //data cb-list combobox
                                    this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.detailItemExpiredDateQuantities));
                                }
                            } else {
                                //data cb-list combobox
                                this.cbpListDetail.set(getComboBoxDetails());
                            }
                        }
                    }
                });
                //data location(warehouse)
                this.tblLocationOfWarehouse.set(tblLocationOfWarehouse);
                //data mid - pb list
                this.tblMemorandumInvoiceDetailPropertyBarcodes.setAll(tblMemorandumInvoiceDetailPropertyBarcodes);
                //data mid - ie - q list
                this.detailItemExpiredDateQuantities.setAll(detailItemExpiredDateQuantities);
                if (this.tblDetail == null
                        || this.tblDetail.get().getTblItem() == null) {
                    //data cb-list combobox
                    this.cbpListDetail.set(getComboBoxDetails());
                } else {
                    if (this.tblDetail.get().getTblItem().getPropertyStatus()) {   //Property
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxPropertyDetails(this.tblMemorandumInvoiceDetailPropertyBarcodes));
                    } else {
                        if (this.tblDetail.get().getTblItem().getConsumable()) {    //cosumable
                            //data cb-list combobox
                            this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.detailItemExpiredDateQuantities));
                        } else {
                            //data cb-list combobox
                            this.cbpListDetail.set(getComboBoxDetails());
                        }
                    }
                }
            } else {  //not bonus
                //add listener for item & item qunatity -> change mi_detail - property_barcode list
                this.tblDetail.get().getTblSupplierItem().tblItemProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null) {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails());
                    } else {
                        if (newVal.getPropertyStatus()) {   //Property
                            //data cb-list combobox
                            if (this.tblDetail.get().getItemQuantity() != null) {
                                List<TblMemorandumInvoiceDetailPropertyBarcode> listData = new ArrayList<>();
                                for (double i = 0; i < this.tblDetail.get().getItemQuantity().doubleValue(); i++) {
                                    TblMemorandumInvoiceDetailPropertyBarcode newData = new TblMemorandumInvoiceDetailPropertyBarcode();
                                    newData.setTblMemorandumInvoiceDetail(this.tblDetail.get());
                                    newData.setTblPropertyBarcode(new TblPropertyBarcode());
                                    if (this.tblDetail.get().getTblSupplierItem() == null) {   //bonus
                                        newData.getTblPropertyBarcode().setTblItem(this.tblDetail.get().getTblItem());
                                    } else {  //not bonus
                                        newData.getTblPropertyBarcode().setTblItem(this.tblDetail.get().getTblSupplierItem().getTblItem());
                                    }
                                    newData.getTblPropertyBarcode().setTblFixedTangibleAsset(new TblFixedTangibleAsset());
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setBeginValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setCurrentValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setEconomicLife(new BigDecimal("0"));
                                    listData.add(newData);
                                }
                                this.tblMemorandumInvoiceDetailPropertyBarcodes.setAll(listData);
                                this.cbpListDetail.set(getComboBoxPropertyDetails(this.tblMemorandumInvoiceDetailPropertyBarcodes));
                            }
                        } else {
                            if (newVal.getConsumable()) {    //consumable
                                //data cb-list combobox
                                List<DetailItemExpiredDateQuantity> listData = new ArrayList<>();
                                this.detailItemExpiredDateQuantities.setAll(listData);
                                this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.detailItemExpiredDateQuantities));
                            } else {
                                //data cb-list combobox
                                this.cbpListDetail.set(getComboBoxDetails());
                            }
                        }
                    }
                });
                this.tblDetail.get().itemQuantityProperty().addListener((obs, oldVal, newVal) -> {
                    if (this.tblDetail.get().getTblSupplierItem() == null
                            || this.tblDetail.get().getTblSupplierItem().getTblItem() == null) {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails());
                    } else {
                        if (this.tblDetail.get().getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property
                            //data cb-list combobox
                            if (newVal != null) {
                                List<TblMemorandumInvoiceDetailPropertyBarcode> listData = new ArrayList<>();
                                for (double i = 0; i < newVal.doubleValue(); i++) {
                                    TblMemorandumInvoiceDetailPropertyBarcode newData = new TblMemorandumInvoiceDetailPropertyBarcode();
                                    newData.setTblMemorandumInvoiceDetail(this.tblDetail.get());
                                    newData.setTblPropertyBarcode(new TblPropertyBarcode());
                                    newData.getTblPropertyBarcode().setTblItem(this.tblDetail.get().getTblSupplierItem().getTblItem());
                                    newData.getTblPropertyBarcode().setTblFixedTangibleAsset(new TblFixedTangibleAsset());
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setBeginValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setCurrentValue(new BigDecimal("0"));
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setEconomicLife(new BigDecimal("0"));
                                    //set asset name
                                    newData.getTblPropertyBarcode().getTblFixedTangibleAsset().setAssetName(newData.getTblMemorandumInvoiceDetail().getTblSupplierItem().getTblItem().getItemName());
                                    listData.add(newData);
                                }
                                this.tblMemorandumInvoiceDetailPropertyBarcodes.setAll(listData);
                                this.cbpListDetail.set(getComboBoxPropertyDetails(this.tblMemorandumInvoiceDetailPropertyBarcodes));
                            }
                        } else {
                            if (this.tblDetail.get().getTblSupplierItem().getTblItem().getConsumable()) {    //consumable
                                if (dataDetailItemExpiredDateQuantityInputStatus != 1) {    //if not updating data, 1 = 'update'
                                    //data cb-list combobox
                                    this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.detailItemExpiredDateQuantities));
                                }
                            } else {
                                //data cb-list combobox
                                this.cbpListDetail.set(getComboBoxDetails());
                            }
                        }
                    }
                });
                //data location(warehouse)
                this.tblLocationOfWarehouse.set(tblLocationOfWarehouse);
                //data mid - pb list
                this.tblMemorandumInvoiceDetailPropertyBarcodes.setAll(tblMemorandumInvoiceDetailPropertyBarcodes);
                //data mid - ie - q list
                this.detailItemExpiredDateQuantities.setAll(detailItemExpiredDateQuantities);
                if (this.tblDetail == null
                        || this.tblDetail.get().getTblSupplierItem() == null
                        || this.tblDetail.get().getTblSupplierItem().getTblItem() == null) {
                    //data cb-list combobox
                    this.cbpListDetail.set(getComboBoxDetails());
                } else {
                    if (this.tblDetail.get().getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxPropertyDetails(this.tblMemorandumInvoiceDetailPropertyBarcodes));
                    } else {
                        if (this.tblDetail.get().getTblSupplierItem().getTblItem().getConsumable()) {    //cosumable
                            //data cb-list combobox
                            this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.detailItemExpiredDateQuantities));
                        } else {
                            //data cb-list combobox
                            this.cbpListDetail.set(getComboBoxDetails());
                        }
                    }
                }
            }
            //button-message
            this.btnMessage.setPrefSize(25, 25);
            //data quantity po
            this.quantityPO.set(quantityPO);
        }

        public ObjectProperty<TblMemorandumInvoiceDetail> tblDetailProperty() {
            return tblDetail;
        }

        public TblMemorandumInvoiceDetail getTblDetail() {
            return tblDetailProperty().get();
        }

        public void setTblDetail(TblMemorandumInvoiceDetail detail) {
            tblDetailProperty().set(detail);
        }

        public ObjectProperty<TblLocationOfWarehouse> tblLocationWarehouseProperty() {
            return tblLocationOfWarehouse;
        }

        public TblLocationOfWarehouse getTblLocationOfWarehouse() {
            return tblLocationWarehouseProperty().get();
        }

        public void setTblLocationOfWarehouse(TblLocationOfWarehouse locationOfWarehouse) {
            tblLocationWarehouseProperty().set(locationOfWarehouse);
        }

        public List<TblMemorandumInvoiceDetailPropertyBarcode> getTblMemorandumInvoiceDetailPropertyBarcodes() {
            return tblMemorandumInvoiceDetailPropertyBarcodes;
        }

        public void setTblMemorandumInvoiceDetailPropertyBarcodes(List<TblMemorandumInvoiceDetailPropertyBarcode> tblMemorandumInvoiceDetailPropertyBarcodes) {
            this.tblMemorandumInvoiceDetailPropertyBarcodes.setAll(tblMemorandumInvoiceDetailPropertyBarcodes);
        }

        public List<DetailItemExpiredDateQuantity> getDetailItemExpiredDateQuantities() {
            return detailItemExpiredDateQuantities;
        }

        public void setDetailItemExpiredDateQuantities(List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities) {
            this.detailItemExpiredDateQuantities.setAll(detailItemExpiredDateQuantities);
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

        public JFXButton getBtnMessage() {
            return btnMessage;
        }

        public void setBtnMessage(JFXButton btnMessage) {
            this.btnMessage = btnMessage;
        }

        public ObjectProperty<BigDecimal> quantityPOProperty() {
            return quantityPO;
        }

        public BigDecimal getQuantityPO() {
            return quantityPOProperty().get();
        }

        public void setQuantityPO(BigDecimal quantityPO) {
            quantityPOProperty().set(quantityPO);
        }

        public StringProperty detailBarcodeProperty() {
            return detailBarcode;
        }

        //----------------------------------------------------------------------
        public String getDetailBarcode() {
            return detailBarcodeProperty().get();
        }

        public void setDetailBarcode(String detailBarcode) {
            detailBarcodeProperty().set(detailBarcode);
        }

        public ObjectProperty<BigDecimal> detailQuantityProperty() {
            return detailQuantity;
        }

        public BigDecimal getDetailQuantity() {
            return detailQuantityProperty().get();
        }

        public void setDetailQuantity(BigDecimal detailQuantity) {
            detailQuantityProperty().set(detailQuantity);
        }

        public ObjectProperty<java.util.Date> detailExpiredDateProperty() {
            return detailExpiredDate;
        }

        public java.util.Date getDetailExpiredDate() {
            return detailExpiredDateProperty().get();
        }

        public void setDetailExpiredDate(java.util.Date detailExpiredDate) {
            detailExpiredDateProperty().set(detailExpiredDate);
        }

    }

    public class DetailItemExpiredDateQuantity {

        private final ObjectProperty<TblMemorandumInvoiceDetailItemExpiredDate> dataDetailItemExporedDate = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> itemQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        public DetailItemExpiredDateQuantity(
                TblMemorandumInvoiceDetailItemExpiredDate dataDetailItemExporedDate,
                BigDecimal itemQuantity) {

            this.dataDetailItemExporedDate.set(dataDetailItemExporedDate);
            this.itemQuantity.set(itemQuantity);

            this.itemQuantity.addListener((obs, oldVal, newVal) -> {
                if (this.dataDetailItemExporedDate.get().getTblMemorandumInvoiceDetail() != null
                        && dataDetailItemExpiredDateQuantityInputStatus == 1
                        && dataInputStatus != 3) { //update
                    //reset mi detail - item quantity
                    this.dataDetailItemExporedDate.get().getTblMemorandumInvoiceDetail().setItemQuantity(this.dataDetailItemExporedDate.get().getTblMemorandumInvoiceDetail().getItemQuantity()
                            .subtract(oldVal != null ? oldVal : new BigDecimal("0"))
                            .add(newVal != null ? newVal : new BigDecimal("0")));
                }
            });
        }

        public ObjectProperty<TblMemorandumInvoiceDetailItemExpiredDate> dataDetailItemExporedDateProperty() {
            return dataDetailItemExporedDate;
        }

        public TblMemorandumInvoiceDetailItemExpiredDate getDataDetailItemExporedDate() {
            return dataDetailItemExporedDateProperty().get();
        }

        public void setDataDetailItemExporedDate(TblMemorandumInvoiceDetailItemExpiredDate dataDetailItemExporedDate) {
            dataDetailItemExporedDateProperty().set(dataDetailItemExporedDate);
        }

        public ObjectProperty<BigDecimal> itemQuantityProperty() {
            return itemQuantity;
        }

        public BigDecimal getItemQuantity() {
            return itemQuantityProperty().get();
        }

        public void setItemQuantity(BigDecimal itemQuantity) {
            itemQuantityProperty().set(itemQuantity);
        }

    }

    public List<DetailLocation> detailLocations = new ArrayList<>();

    public DetailLocation selectedDataDetailLocation;

    /**
     * HANDLE FROM DATA INPUT
     */
    public Stage dialogStageDetal;

    private void dataReceivingDetailBonusCreateHandle() {
        if (selectedData.getTblPurchaseOrder() != null
                && selectedData.getTblPurchaseOrder().getTblSupplier() != null) {
            //data mi - detail
            TblMemorandumInvoiceDetail dataDetail = new TblMemorandumInvoiceDetail();
            dataDetail.setTblMemorandumInvoice(selectedData);
            dataDetail.setItemCost(new BigDecimal("0"));
            dataDetail.setItemQuantity(new BigDecimal("0"));
            dataDetail.setItemDiscount(new BigDecimal("0"));
            //data detail location
            selectedDataDetailLocation = new DetailLocation(dataDetail, null, new ArrayList<>(), new ArrayList<>(), new BigDecimal("0"));
            //open form data - detail-bonus
            showDataDetailBonusDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAHKAN BONUS",
                    "Silahkan pilih data purchase order terlebih dahulu..!", null);
        }
    }

    private void dataReceivingDetailBonusDeleteHandle() {
        if (tableReceivingDetail.getSelectionModel().getSelectedItems().size() == 1) {
            if (((DetailLocation) tableReceivingDetail.getSelectionModel().getSelectedItem()).getTblDetail().getTblItem() != null) {
                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    //successed removing data (info)
                    ClassMessage.showSucceedRemovingDataMessage("", null);
                    //remove data from list
                    detailLocations.remove((DetailLocation) tableReceivingDetail.getSelectionModel().getSelectedItem());
                    //refresh data view
                    setDataDetail();
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS",
                        "Selain data 'bonus' tidak dapat dihapus..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataDetailBonusDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_receiving/receiving/ReceivingDetailBonusDialog.fxml"));

            ReceivingDetailBonusController controller = new ReceivingDetailBonusController(this);
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
    private FReceivingManager fReceivingManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        if (fReceivingManager == null) {
            fReceivingManager = new FReceivingManagerImpl();
        }

        //set splitpane
        setDataReceivingSplitpane();

        //init table
        initTableDataReceiving();

        //init form
        initFormDataReceiving();

        spDataReceiving.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReceivingFormShowStatus.set(0.0);
        });
    }

    public ReceivingController() {

    }

    public ReceivingController(FeatureReceivingController parentController) {
        this.parentController = parentController;
        this.fReceivingManager = parentController.getFReceivingManager();
    }

    private FeatureReceivingController parentController;

    public FReceivingManager getService() {
        return fReceivingManager;
    }

}
