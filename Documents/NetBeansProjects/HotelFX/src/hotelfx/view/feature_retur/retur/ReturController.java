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
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.RefReturPaymentStatus;
import hotelfx.persistence.model.RefReturType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.service.FReturManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_retur.FeatureReturController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author ANDRI
 */
public class ReturController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRetur;

    private DoubleProperty dataReturFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReturLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReturSplitpane() {
        spDataRetur.setDividerPositions(1);

        dataReturFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReturFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRetur.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRetur.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReturFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReturLayout.setDisable(false);
                    tableDataReturLayoutDisableLayer.setDisable(true);
                    tableDataReturLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReturLayout.setDisable(true);
                    tableDataReturLayoutDisableLayer.setDisable(false);
                    tableDataReturLayoutDisableLayer.toFront();
                }
            }
        });

        dataReturFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataReturLayout;

    private ClassFilteringTable<TblRetur> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRetur;

    private void initTableDataRetur() {
        //set table
        setTableDataRetur();
        //set control
        setTableControlDataRetur();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRetur, 15.0);
        AnchorPane.setLeftAnchor(tableDataRetur, 15.0);
        AnchorPane.setRightAnchor(tableDataRetur, 15.0);
        AnchorPane.setTopAnchor(tableDataRetur, 15.0);
        ancBodyLayout.getChildren().add(tableDataRetur);
    }

    private void setTableDataRetur() {
        TableView<TblRetur> tableView = new TableView();

        TableColumn<TblRetur, String> codeRetur = new TableColumn("ID-Retur");
        codeRetur.setCellValueFactory(cellData -> cellData.getValue().codeReturProperty());
        codeRetur.setMinWidth(120);

        TableColumn<TblRetur, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSupplier().getSupplierName(), param.getValue().tblSupplierProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblRetur, String> returDate = new TableColumn<>("Tanggal Retur");
        returDate.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getReturDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getReturDate())
                                : "-", param.getValue().returDateProperty()));
        returDate.setMinWidth(160);

        tableView.getColumns().addAll(codeRetur, supplierName, returDate);

        tableView.setItems(loadAllDataRetur());

        tableView.setRowFactory(tv -> {
            TableRow<TblRetur> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataReturUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataReturShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataReturShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataRetur = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblRetur.class,
                tableDataRetur.getTableView(),
                tableDataRetur.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataRetur() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReturCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataReturUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    //listener delete
//                    dataReturDeleteHandle();
//                }
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getApproveData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Approve");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener approve
//                dataReturApproveHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataReturPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataRetur.addButtonControl(buttonControls);
    }

    private ObservableList<TblRetur> loadAllDataRetur() {
        List<TblRetur> list = parentController.getFReturManager().getAllDataRetur();
        for (TblRetur data : list) {
            //data supplier
            data.setTblSupplier(parentController.getFReturManager().getDataSupplier(data.getTblSupplier().getIdsupplier()));
            //data location (supplier)
            data.getTblSupplier().setTblLocation(parentController.getFReturManager().getDataLocation(data.getTblSupplier().getTblLocation().getIdlocation()));
            //data hotel receivable
            if (data.getTblHotelReceivable() != null) {
                data.setTblHotelReceivable(parentController.getFReturManager().getDataHotelReceivable(data.getTblHotelReceivable().getIdhotelReceivable()));
            }
            //data retur status
            data.setRefReturStatus(parentController.getFReturManager().getDataReturStatus(data.getRefReturStatus().getIdstatus()));
        }
        //remove data isn't used
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getRefReturStatus().getIdstatus() == 2 //Dibatalkan = '2'
                    || list.get(i).getRefReturStatus().getIdstatus() == 3) { //Sudah Tidak Berlaku = '3'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private List<DetailLocation> loadAllDataReturDetail() {
        List<DetailLocation> list = new ArrayList<>();
        if (selectedData.getIdretur() != 0L) {
            List<TblReturDetail> dataReturDetails = parentController.getFReturManager().getAllDataReturDetailByIDRetur(selectedData.getIdretur());
            for (TblReturDetail dataReturDetail : dataReturDetails) {
                //data retur - detail
                TblReturDetail dataDetail = dataReturDetail;
                dataDetail.setTblRetur(selectedData);
                dataDetail.setTblMemorandumInvoice(parentController.getFReturManager().getDataMemorandumInvoice(dataDetail.getTblMemorandumInvoice().getIdmi()));
                dataDetail.setTblSupplierItem(parentController.getFReturManager().getDataSupplierItem(dataDetail.getTblSupplierItem().getIdrelation()));
                dataDetail.getTblSupplierItem().setTblItem(parentController.getFReturManager().getDataItem(dataDetail.getTblSupplierItem().getTblItem().getIditem()));
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(parentController.getFReturManager().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(parentController.getFReturManager().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                dataDetail.getTblSupplierItem().getTblItem().setTblUnit(parentController.getFReturManager().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                dataDetail.setTblLocation(parentController.getFReturManager().getDataLocation(dataDetail.getTblLocation().getIdlocation()));
                if (dataDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) { //Property
                    List<TblReturDetailPropertyBarcode> dataReturDetailPropertyBarcodes = parentController.getFReturManager().getAllDataReturDetailPropertyBarcodeByIDReturDetail(dataDetail.getIddetailRetur());
                    for (TblReturDetailPropertyBarcode dataReturDetailPropertyBarcode : dataReturDetailPropertyBarcodes) {
                        //data retur-detail : quantity = 1
                        dataDetail.setItemQuantity(new BigDecimal("1"));
                        //data retur-detail-property-barcode
                        TblReturDetailPropertyBarcode dataDetailPropertyBarcode = dataReturDetailPropertyBarcode;
                        dataDetailPropertyBarcode.setTblReturDetail(dataDetail);
                        dataDetailPropertyBarcode.setTblPropertyBarcode(parentController.getFReturManager().getDataPropertyBarcode(dataDetailPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                        //add data to list 'detail_location'
                        DetailLocation detailLocation = generateDataDetailLocation(dataDetail, dataDetailPropertyBarcode);
                        detailLocation.setDetailQuantity(new BigDecimal(String.valueOf(dataDetail.getItemQuantity())));
                        list.add(detailLocation);
                    }
                } else {
                    if (dataInputStatus == 3 //just for show info data
                            && dataDetail.getTblSupplierItem().getTblItem().getConsumable()) {    //consumable
                        //data retur - detail - item expired date
                        List<TblReturDetailItemExpiredDate> dataReturDetailPropertyItemExpiredDates
                                = parentController.getFReturManager().getAllDataReturDetailItemExpiredDateByIDReturDetail(dataDetail.getIddetailRetur());
                        for (TblReturDetailItemExpiredDate dataReturDetailPropertyItemExpiredDate : dataReturDetailPropertyItemExpiredDates) {
                            //data retur - detail
                            dataReturDetailPropertyItemExpiredDate.setTblReturDetail(dataReturDetail);
                            //data item expired
                            dataReturDetailPropertyItemExpiredDate.setTblItemExpiredDate(parentController.getFReturManager().getDataItemExpiredDate(dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
//                    //data item
//                    dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().setTblItem(parentController.getFReceivingManager().getDataItem(dataReturDetailPropertyItemExpiredDate.getTblReturDetail().getTblItem().getIditem()));
                        }
                        //data retur - detail - item expired date - quantity
                        List<DetailItemExpiredDateQuantity> dataDetailItemExpiredDateQuantities = new ArrayList<>();
                        for (TblReturDetailItemExpiredDate dataReturDetailPropertyItemExpiredDate : dataReturDetailPropertyItemExpiredDates) {
                            boolean found = false;
                            for (DetailItemExpiredDateQuantity dataDetailItemExpiredDateQuantity : dataDetailItemExpiredDateQuantities) {
                                if (dataReturDetailPropertyItemExpiredDate.getTblReturDetail().getIddetailRetur()
                                        == dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblReturDetail().getIddetailRetur()
                                        && dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate().equals(dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate())) {
                                    dataDetailItemExpiredDateQuantity.setItemQuantity(dataDetailItemExpiredDateQuantity.getItemQuantity().add(new BigDecimal("1")));
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                DetailItemExpiredDateQuantity detailItemExpiredDateQuantity = new DetailItemExpiredDateQuantity(dataReturDetailPropertyItemExpiredDate,
                                        new BigDecimal("1"));
                                dataDetailItemExpiredDateQuantities.add(detailItemExpiredDateQuantity);
                            }
                        }
                        for (DetailItemExpiredDateQuantity dataDetailItemExpiredDateQuantity : dataDetailItemExpiredDateQuantities) {
                            //data detail location
                            DetailLocation detailLocation = generateDataDetailLocation(dataDetail, null);
                            detailLocation.setDetailExpiredDate(dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate());
                            detailLocation.setDetailQuantity(new BigDecimal(String.valueOf(dataDetailItemExpiredDateQuantity.getItemQuantity())));
                            //add data to list 'detail_location'
                            list.add(detailLocation);
                        }
                    } else {
                        //add data to list 'detail_location'
                        DetailLocation detailLocation = generateDataDetailLocation(dataDetail, null);
                        detailLocation.setDetailQuantity(new BigDecimal(String.valueOf(dataDetail.getItemQuantity())));
                        list.add(detailLocation);
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
    private GridPane gpFormDataRetur;

    @FXML
    private ScrollPane spFormDataRetur;

    @FXML
    private Label lblCodeData;
    ;
    
    @FXML
    private JFXTextField txtCodeRetur;

    @FXML
    private Label lblTotalRetur;

    @FXML
    private AnchorPane ancSupplierLayout;
    private JFXCComboBoxTablePopup<TblSupplier> cbpSupplier;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnAddDetailItem;

    @FXML
    private JFXButton btnRemoveDetailItem;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblRetur selectedData;

    private void initFormDataRetur() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRetur.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRetur.setOnScroll((ScrollEvent scroll) -> {
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
        btnSave.setTooltip(new Tooltip("Simpan (Data Retur)"));
        btnSave.setOnAction((e) -> {
            dataReturPreSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataReturCancelHandle();
        });

//        btnAddDetailItem.setTooltip(new Tooltip("Tambah (Data Barang)"));
//        btnAddDetailItem.setOnAction((e) -> {
//            dataReturDetailItemCreateHandle();
//        });
//
//        btnRemoveDetailItem.setTooltip(new Tooltip("Hapus (Data Barang)"));
//        btnRemoveDetailItem.setOnAction((e) -> {
//            dataReturDetailItemRemoveHandle();
//        });
        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpSupplier);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal");
    }

    private void initDataPopup() {
        //Supplier
        TableView<TblSupplier> tableSupplier = new TableView<>();

        TableColumn<TblSupplier, String> codeSupplier = new TableColumn<>("ID");
        codeSupplier.setCellValueFactory(cellData -> cellData.getValue().codeSupplierProperty());
        codeSupplier.setMinWidth(120);

        TableColumn<TblSupplier, String> supplierName = new TableColumn<>("Supplier");
        supplierName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        supplierName.setMinWidth(140);

        tableSupplier.getColumns().addAll(codeSupplier, supplierName);

        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());

        cbpSupplier = new JFXCComboBoxTablePopup<>(
                TblSupplier.class, tableSupplier, supplierItems, "", "Supplier *", true, 280, 200
        );

        //attached to grid-pane
        ancSupplierLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpSupplier, 0.0);
        AnchorPane.setLeftAnchor(cbpSupplier, 0.0);
        AnchorPane.setRightAnchor(cbpSupplier, 0.0);
        AnchorPane.setTopAnchor(cbpSupplier, 0.0);
        ancSupplierLayout.getChildren().add(cbpSupplier);
    }

    private ObservableList<TblSupplier> loadAllDataSupplier() {
        List<TblSupplier> list = parentController.getFReturManager().getAllDataSupplier();
        for (TblSupplier data : list) {
            //data location (supplier)
            data.setTblLocation(parentController.getFReturManager().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Supplier
        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
        cbpSupplier.setItems(supplierItems);
    }

//    private boolean editing = false;
    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeRetur() != null
                ? selectedData.getCodeRetur() : "");

        txtCodeRetur.textProperty().bindBidirectional(selectedData.codeReturProperty());

        cbpSupplier.valueProperty().bindBidirectional(selectedData.tblSupplierProperty());

        cbpSupplier.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal != null) {
//                if (oldVal != null) {
//                    if (!editing) {
//                        editing = true;
//                        Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "Are you sure want to change data supplier? All detail data item has been created, will be deleted?", null);
//                        if (alert.getResult() == ButtonType.OK) {
                    //reset data details
                    detailLocations = new ArrayList<>();
                    //set data detail
//                    setDataDetail();
                    tableReturDetail.getTableView().setItems(FXCollections.observableArrayList(detailLocations));
                    //calculation total retur
                    lblTotalRetur.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRetur()));
//                        } else {
//                            cbpSupplier.setValue(oldVal);
//                        }
//                        editing = false;
//                    }
//                } else {
//                    //reset data details
//                    detailLocations = new ArrayList<>();
//                    //set data details
//                    setDataDetail();
//                    //calculation total retur
//                    lblTotalRetur.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRetur()));
//                }
                }
            }
        });

        cbpSupplier.hide();

        //set data detail
//        setDataDetail();
        //calculation total retur
        lblTotalRetur.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRetur()));

        //init table detail
        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeRetur.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataRetur,
                dataInputStatus == 3,
                txtCodeRetur);

        btnSave.setVisible(dataInputStatus != 3);
    }

    public BigDecimal calculationTotalRetur() {
        BigDecimal result = new BigDecimal("0");
        for (DetailLocation data : detailLocations) {
            if (data.getTblDetail() != null) {
                result = result.add(((new BigDecimal("1")).add(data.getTblDetail().getItemTaxPercentage()))
                        .multiply((data.getTblDetail().getItemCost().subtract(data.getTblDetail().getItemDiscount()))
                                .multiply(data.getTblDetail().getItemQuantity())));
            }
        }
        result = result.add(selectedData.getDeliveryCost());
        return result;
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    public void dataReturCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblRetur();
//        selectedData.setTblSupplier(new TblSupplier());
        selectedData.setDeliveryCost(new BigDecimal("0"));
        selectedData.setTaxPercentage(new BigDecimal("0"));
        detailLocations = new ArrayList<>();
        setSelectedDataToInputForm();
        //open form data retur
        dataReturFormShowStatus.set(0.0);
        dataReturFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

//    private void dataReturUpdateHandle() {
//        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputStatus = 1;
//            selectedData = parentController.getFReturManager().getDataMemorandumInvoice(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()).getIdmi());
//            selectedData.setTblPurchaseOrder(parentController.getFReturManager().getDataPurchaseOrder(selectedData.getTblPurchaseOrder().getIdpo()));
//            selectedData.getTblPurchaseOrder().setTblSupplier(parentController.getFReturManager().getDataSupplier(selectedData.getTblPurchaseOrder().getTblSupplier().getIdsupplier()));
//            setSelectedDataToInputForm();
//            //open form data receiving
//            dataReturFormShowStatus.set(0.0);
//            dataReturFormShowStatus.set(1.0);
//    //set unsaving data input -> 'true'
//        ClassSession.unSavingDataInput = true;
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
//        }
//    }
//
//    private void dataReturDeleteHandle() {
//        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            if (parentController.getFReturManager().deleteDataMemorandumInvoice((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
//                HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Deleting data successed..!", null);
//                //refresh data from table & close form data receiving
//                tableDataRetur.getTableView().setItems(loadAllDataPO());
//    cft.refreshFilterItems(tableDataRetur.getTableView().getItems());
//                dataReturFormShowStatus.set(0.0);
//            } else {
//                HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Deleting data failed..!", parentController.getFReturManager().getErrorMessage());
//            }
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
//        }
//    }
    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataReturShowHandle() {
        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFReturManager().getDataRetur(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()).getIdretur());
            selectedData.setTblSupplier(parentController.getFReturManager().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
            detailLocations = loadAllDataReturDetail();
            setSelectedDataToInputForm();
            dataReturFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataReturUnshowHandle() {
        refreshDataTableRetur();
        dataReturFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataReturPrintHandle() {
        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printRetur(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printRetur(TblRetur dataRetur) {
//        if (idRetur != 0L) {
        String hotelName = "";
        SysDataHardCode sdhHotelName = parentController.getFReturManager().getDataSysDataHardCode((long) 12);  //HotelName = '12'
        if (sdhHotelName != null
                && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }
        String hotelAddress = "";
        SysDataHardCode sdhHotelAddress = parentController.getFReturManager().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
        if (sdhHotelAddress != null
                && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }
        String hotelPhoneNumber = "";
        SysDataHardCode sdhHotelPhoneNumber = parentController.getFReturManager().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
        if (sdhHotelPhoneNumber != null
                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
        }
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = parentController.getFReturManager().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        try {
//            ClassPrinter.printRetur(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    dataRetur);
//        } catch (Throwable ex) {
//            Logger.getLogger(ReturController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
//        }
    }

    private void dataReturPreSaveHandle() {
        if (checkDataInputDataRetur()) {
            //open form pre-save
            showReturTypeDialog();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void showReturTypeDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur/ReturTypeDialog.fxml"));

            ReturTypeController controller = new ReturTypeController(this);
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

    public void dataReturSaveHandle() {
//        if (checkDataInputDataRetur()) {
        Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
        if (alert.getResult() == ButtonType.OK) {
            //set location (from selected data 'LocationOfWarehouse')
            for (DetailLocation detailLocation : detailLocations) {
                if (detailLocation.getTblLocationOfWarehouse() != null) {
                    detailLocation.getTblDetail().setTblLocation(new TblLocation(detailLocation.getTblLocationOfWarehouse().getTblLocation()));
                }
            }
            //dummy entry
            TblRetur dummySelectedData = new TblRetur(selectedData);
            dummySelectedData.setTblSupplier(new TblSupplier(dummySelectedData.getTblSupplier()));
            dummySelectedData.setRefReturType(new RefReturType(dummySelectedData.getRefReturType()));
            if (dummySelectedData.getTblHotelReceivable() != null) {
                dummySelectedData.setTblHotelReceivable(new TblHotelReceivable(dummySelectedData.getTblHotelReceivable()));
            }
            if (dummySelectedData.getRefReturPaymentStatus() != null) {
                dummySelectedData.setRefReturPaymentStatus(new RefReturPaymentStatus(dummySelectedData.getRefReturPaymentStatus()));
            }
            List<DetailLocation> dummyDetailLocations = new ArrayList<>();
            for (DetailLocation detailLocation : detailLocations) {
                DetailLocation dummyDetailLocation = new DetailLocation(
                        new TblReturDetail(detailLocation.getTblDetail()),
                        detailLocation.getTblLocationOfWarehouse() != null
                                ? new TblLocationOfWarehouse(detailLocation.getTblLocationOfWarehouse())
                                : null,
                        detailLocation.getTblDetailPropertyBarcode() != null
                                ? new TblReturDetailPropertyBarcode(detailLocation.getTblDetailPropertyBarcode())
                                : null,
                        new ArrayList<>(),
                        detailLocation.getCreated());
                dummyDetailLocation.getTblDetail().setTblRetur(dummySelectedData);
                dummyDetailLocation.getTblDetail().setTblMemorandumInvoice(new TblMemorandumInvoice(dummyDetailLocation.getTblDetail().getTblMemorandumInvoice()));
                dummyDetailLocation.getTblDetail().setTblSupplierItem(new TblSupplierItem(dummyDetailLocation.getTblDetail().getTblSupplierItem()));
                dummyDetailLocation.getTblDetail().getTblSupplierItem().setTblItem(new TblItem(dummyDetailLocation.getTblDetail().getTblSupplierItem().getTblItem()));
                if (dummyDetailLocation.getTblDetail().getTblLocation() != null) {
                    dummyDetailLocation.getTblDetail().setTblLocation(new TblLocation(dummyDetailLocation.getTblDetail().getTblLocation()));
                }
                if (dummyDetailLocation.getTblLocationOfWarehouse() != null
                        && dummyDetailLocation.getTblLocationOfWarehouse().getTblLocation() != null) {
                    dummyDetailLocation.getTblLocationOfWarehouse().setTblLocation(new TblLocation(dummyDetailLocation.getTblLocationOfWarehouse().getTblLocation()));
                }
                if (dummyDetailLocation.getTblDetailPropertyBarcode() != null) {
                    dummyDetailLocation.getTblDetailPropertyBarcode().setTblReturDetail(dummyDetailLocation.getTblDetail());
                    dummyDetailLocation.getTblDetailPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(dummyDetailLocation.getTblDetailPropertyBarcode().getTblPropertyBarcode()));
                }
                List<DetailItemExpiredDateSelected> dummyDetailItemExpiredDateSelecteds = new ArrayList<>();
                for (DetailItemExpiredDateSelected detailItemExpiredDateSelected : detailLocation.getDetailItemExpiredDateSelecteds()) {
                    DetailItemExpiredDateSelected dummyDetailItemExpiredDateSelected = new DetailItemExpiredDateSelected(new TblReturDetailItemExpiredDate(detailItemExpiredDateSelected.getDataDetailItemExporedDate()),
                            detailItemExpiredDateSelected.isSelected());
                    dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().setTblReturDetail(dummyDetailLocation.getTblDetail());
                    dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().setTblItemExpiredDate(new TblItemExpiredDate(dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate()));
                    dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().setTblItem(new TblItem(dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem()));
//                    if (dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().getRefItemExpiredDateStatus() != null) {
//                        dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().setRefItemExpiredDateStatus(new RefItemExpiredDateStatus(dummyDetailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().getRefItemExpiredDateStatus()));
//                    }
                    dummyDetailItemExpiredDateSelecteds.add(dummyDetailItemExpiredDateSelected);
                }
                dummyDetailLocation.setDetailItemExpiredDateQuantities(dummyDetailItemExpiredDateSelecteds);
                dummyDetailLocations.add(dummyDetailLocation);
            }
            switch (dataInputStatus) {
                case 0:
                    if (parentController.getFReturManager().insertDataRetur(dummySelectedData,
                            dummyDetailLocations) != null) {
                        ClassMessage.showSucceedInsertingDataMessage("", null);
                        //refresh data from table & close form data receiving
                        refreshDataTableRetur();
                        dataReturFormShowStatus.set(0.0);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                    } else {
//                        //data retur : reset id to null, code to null
//                        selectedData.setIdretur(0L);
//                        selectedData.setCodeRetur(null);
//                        for (DetailLocation detailLocation : detailLocations) {
//                            //data retur detail  : reset id to null
//                            detailLocation.getTblDetail().setIddetailRetur(0L);
//                            if (detailLocation.getTblDetailPropertyBarcode() != null) {
//                                //data retur detail - property barcode: reset id to null
//                                detailLocation.getTblDetailPropertyBarcode().setIdrelation(0L);
//                                //data retur detail (property) : item quantity (reset to 1)
//                                detailLocation.getTblDetail().setItemQuantity(new BigDecimal("1"));
//                            }
//                            for (DetailItemExpiredDateSelected detailItemExpiredDateSelected : detailLocation.getDetailItemExpiredDateSelecteds()) {
//                                if (detailItemExpiredDateSelected.getDataDetailItemExporedDate() != null) {
//                                    //data retur detail - item expired date: reset id to null
//                                    detailItemExpiredDateSelected.getDataDetailItemExporedDate().setIdrelation(0L);
//                                }
//                            }
//                        }
                        ClassMessage.showFailedInsertingDataMessage(parentController.getFReturManager().getErrorMessage(), null);
                    }
                    break;
//                case 1:   //need maintenance
//                    if (parentController.getFReturManager().updateDataRetur(dummySelectedData,
//                            dummyDetailLocations)) {
//                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Updating data successed..!", null);
//                        //refresh data from table & close form data receiving
//                        refreshDataTableRetur();
//                        dataReturFormShowStatus.set(0.0);
//                    //set unsaving data input -> 'false'
//        ClassSession.unSavingDataInput = false;
//                    } else {
//                        //reset id to null
//                        
//                        
//                        for (DetailLocation detailLocation : detailLocations) {
//                            
//                            //data retur detail (property) : item quantity (reset to 1)
//                            if (detailLocation.getTblDetail().getTblItem().getPropertyStatus()) {
//                                detailLocation.getTblDetail().setItemQuantity(new BigDecimal("1"));
//                            }
//                        }
//                        HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Updating data failed..!", parentController.getFReturManager().getErrorMessage());
//                    }
//                    break;
                default:
                    break;
            }
        }
//        } else {
//            ClassMessage.showWarningInputDataMessage(errDataInput, null);
//        }
    }

    private void dataReturCancelHandle() {
        //refresh data from table & close form data retur
        refreshDataTableRetur();
        dataReturFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableRetur() {
        tableDataRetur.getTableView().setItems(loadAllDataRetur());
        cft.refreshFilterItems(tableDataRetur.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataRetur() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpSupplier.getValue() == null) {
            dataInput = false;
            errDataInput += "Supplier : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (detailLocations.isEmpty()) {
            dataInput = false;
            errDataInput += "Barang Retur : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (!checkDataInputDataDetail()) {
                dataInput = false;
//            errDataInput += "Invalid data input detail " + errDataInputZeroAll + "..! \n";
                errDataInput += "Terdapat kesalahan pada data detail barang yang akan diretur..! \n";
            }
        }
        return dataInput;
    }

    private final String errorMarkCSS = "button-mark-error";

    private final String warningMarkCSS = "button-mark-warning";

    private final String validMarkCSS = "button-mark-valid";

//    private String errDataInputZeroAll = "";
    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
//        boolean zeroAll = true;
//        errDataInputZeroAll = "";
        for (DetailLocation data : detailLocations) {
            data.getBtnMessage().setText("");
            setButtonMessageInfo(data.getBtnMessage(), validMarkCSS, "data telah sesuai..");
            if (data.getTblLocationOfWarehouse() == null) {
                dataInput = false;
                setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data lokasi : " + ClassMessage.defaultErrorNullValueMessage + "");
                //check quantity item is avalable in warehouse
            } else {
                if (data.getTblDetail().getItemQuantity()
                        .compareTo(new BigDecimal("0")) < 1) {
                    dataInput = false;
                    setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data jumlah barang : " + ClassMessage.defaultErrorZeroValueMessage + "");
                } else {
                    if (!(data.getTblDetail().getTblSupplierItem().getTblItem().getPropertyStatus())) {   //Property
                        BigDecimal maxQuantityAvailable = getMaxQuantityAvailable(data.getTblDetail());
                        if (data.getTblDetail().getItemQuantity()
                                .compareTo(maxQuantityAvailable) == 1) {
                            dataInput = false;
                            setButtonMessageInfo(data.getBtnMessage(), errorMarkCSS, "data jumlah barang : nilai tidak dapat lebih besar dari '" + maxQuantityAvailable + "'");
                        }
                    } else {
                        System.out.println("xccsd");
                    }
//                    } else {
//                        if (data.getTblDetail().getItemQuantity().compareTo(new BigDecimal("0")) == 0
//                                && maxQuantityAvailable.compareTo(new BigDecimal("0")) == 1) {
//                            setButtonMessageInfo(data.getBtnMessage(), warningMarkCSS, "data quantity : value can be insert with " + maxQuantityAvailable + "");
//                        }
//                    }
//                    if (data.getTblDetail().getItemQuantity().compareTo(new BigDecimal("0")) == 1) {
//                        zeroAll = false;
//                    }
                }
            }
        }
//        if (zeroAll) {    //data quantity item cannt alll 'zero'
//            errDataInputZeroAll = "(No Data Item to Retur)";
//            dataInput = false;
//        }
        return dataInput;
    }

    private void setButtonMessageInfo(JFXButton buttonMessage, String classCSSName, String tooltipInfo) {
        buttonMessage.getStyleClass().clear();
        buttonMessage.getStyleClass().add(classCSSName);
        buttonMessage.setTooltip(new Tooltip(tooltipInfo));
    }

    private BigDecimal getMaxQuantityAvailable(TblReturDetail dataDetail) {
        BigDecimal result = parentController.getFReturManager().getDataMemorandumInvoiceDetailByIDMemorandumInvoiceAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getIdmi(),
                dataDetail.getTblSupplierItem().getIdrelation()).getItemQuantity();
        List<TblReturDetail> list = parentController.getFReturManager().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getIdmi(),
                dataDetail.getTblSupplierItem().getIdrelation());
        for (TblReturDetail data : list) {
            result = result.subtract(data.getItemQuantity());
        }
        return result;
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDataTableDetail;

    public ClassTableWithControl tableReturDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set control
        setTableControlDataDetail();
        //set table-control to content-view
        ancDataTableDetail.getChildren().clear();
        AnchorPane.setBottomAnchor(tableReturDetail, 0.0);
        AnchorPane.setLeftAnchor(tableReturDetail, 0.0);
        AnchorPane.setRightAnchor(tableReturDetail, 0.0);
        AnchorPane.setTopAnchor(tableReturDetail, 0.0);
        ancDataTableDetail.getChildren().add(tableReturDetail);
    }

    public void setTableDataDetail() {
        TableView<DetailLocation> tableView = new TableView();
//        tableView.setEditable(true);
        tableView.setEditable(dataInputStatus != 3);

        TableColumn<DetailLocation, String> location = new TableColumn("Lokasi");
        location.setMinWidth(100);
        location.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblLocationOfWarehouse() == null
                                ? "-"
                                : param.getValue().getTblLocationOfWarehouse().getWarehouseName(), param.getValue().tblLocationWarehouseProperty()));
//        Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory
//                = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
//                    @Override
//                    public TableCell call(TableColumn p) {
//                        return new EditingCellWarehouse();
//                    }
//                };
//        location.setCellFactory(cellFactory);
//        location.setEditable(true);

        TableColumn<DetailLocation, String> codePO = new TableColumn("PO");
        codePO.setMinWidth(95);
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblDetail() == null
                        || param.getValue().getTblDetail().getTblMemorandumInvoice() == null
                        || param.getValue().getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder() == null
                                ? "-" : param.getValue().getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo(), param.getValue().getTblDetail().tblMemorandumInvoiceProperty()));

        TableColumn<DetailLocation, String> codeReceiving = new TableColumn("Penerimaan");
        codeReceiving.setMinWidth(95);
        codeReceiving.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblDetail() == null
                        || param.getValue().getTblDetail().getTblMemorandumInvoice() == null
                                ? "-" : param.getValue().getTblDetail().getTblMemorandumInvoice().getCodeMi(), param.getValue().getTblDetail().tblMemorandumInvoiceProperty()));

        TableColumn<DetailLocation, String> titleCodeNumber = new TableColumn("Nomor");
        titleCodeNumber.getColumns().addAll(codePO, codeReceiving);

        TableColumn<DetailLocation, String> itemSistem = new TableColumn("(Sistem)");
        itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> getItemSistem(param.getValue()),
                        param.getValue().tblDetailProperty()));
        itemSistem.setMinWidth(120);

        TableColumn<DetailLocation, String> itemSupplier = new TableColumn("(Supplier)");
        itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> getItemSupplier(param.getValue()),
                        param.getValue().tblDetailProperty()));
        itemSupplier.setMinWidth(120);

        TableColumn<DetailLocation, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(itemSistem, itemSupplier);

//        TableColumn<DetailLocation, String> itemInfo = new TableColumn("Barang");
//        itemInfo.setMinWidth(150);
//        itemInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
//                -> Bindings.createStringBinding(() -> getDataItemInfo(param.getValue()), 
//                        param.getValue().getTblDetail().tblSupplierItemProperty()));
//
//        TableColumn<DetailLocation, String> item = new TableColumn("Barang");
//        item.setMinWidth(150);
//        item.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblDetail() == null
//                        || param.getValue().getTblDetail().getTblSupplierItem() == null
//                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem() == null
//                                ? "-" : param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getItemName(), 
//                        param.getValue().getTblDetail().tblSupplierItemProperty()));
//
//        TableColumn<DetailLocation, String> itemType = new TableColumn("Tipe");
//        itemType.setMinWidth(150);
//        itemType.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblDetail() == null
//                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem() == null
//                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem() == null
//                                ? "-" : param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getTblItemType().getTypeName()
//                                + (param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getGuestStatus() //Guest
//                                        ? " (G)" : " (NG)"),
//                        param.getValue().getTblDetail().tblSupplierItemProperty()));
        TableColumn<DetailLocation, String> codeBarcode = new TableColumn("Barcode");
        codeBarcode.setMinWidth(120);
        codeBarcode.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblDetailPropertyBarcode() == null
                        || param.getValue().getTblDetailPropertyBarcode().getTblPropertyBarcode() == null
                                ? "-" : param.getValue().getTblDetailPropertyBarcode().getTblPropertyBarcode().getCodeBarcode(), param.getValue().tblDetailPropertyBarcodeProperty()));

        TableColumn<DetailLocation, String> cost = new TableColumn("Harga");
        cost.setMinWidth(100);
        cost.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemCost()), param.getValue().getTblDetail().itemCostProperty()));

        TableColumn<DetailLocation, String> discount = new TableColumn("Diskon");
        discount.setMinWidth(90);
        discount.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemDiscount()), param.getValue().getTblDetail().itemDiscountProperty()));

        TableColumn<DetailLocation, String> quantity = new TableColumn("Jumlah");
        quantity.setMinWidth(80);
        quantity.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.decimalFormat.cFormat(param.getValue().getTblDetail().getItemQuantity()), param.getValue().getTblDetail().itemQuantityProperty()));
//        Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory2
//                = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
//                    @Override
//                    public TableCell call(TableColumn p) {
//                        return new EditingCellQuantity();
//                    }
//                };
//        quantity.setCellFactory(cellFactory2);
//        quantity.setEditable(true);

        TableColumn<DetailLocation, String> tax = new TableColumn("Tax(%)");
        tax.setMinWidth(70);
        tax.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "0%"
                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemTaxPercentage().multiply(new BigDecimal("100"))) + "%", param.getValue().getTblDetail().itemTaxPercentageProperty()));

        TableColumn<DetailLocation, String> unitName = new TableColumn("Satuan");
        unitName.setMinWidth(100);
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                        || param.getValue().getTblDetail().getTblSupplierItem() == null
                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem() == null
                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getTblUnit() == null
                                ? "-"
                                : param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblDetail().tblSupplierItemProperty()));

        TableColumn<DetailLocation, String> dataDetails = new TableColumn("Detail");
        dataDetails.setMinWidth(100);
        dataDetails.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> "-", param.getValue().cbpListDetailProperty()));
        Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory3
                = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellReturDDetails();
                    }
                };
        dataDetails.setCellFactory(cellFactory3);
        dataDetails.setEditable(true);

        TableColumn<DetailLocation, String> dataDetailsInfo = new TableColumn("Detail");
        dataDetailsInfo.setMinWidth(100);
        dataDetailsInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> "-", param.getValue().cbpListDetailProperty()));
        Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>> cellFactory3Info
                = new Callback<TableColumn<DetailLocation, String>, TableCell<DetailLocation, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellReturDDetailsInfo();
                    }
                };
        dataDetailsInfo.setCellFactory(cellFactory3Info);
        dataDetailsInfo.setEditable(true);

        TableColumn<DetailLocation, String> infoButton = new TableColumn("Status");
        infoButton.setMinWidth(75);
        infoButton.setCellFactory((TableColumn<DetailLocation, String> param) -> new ButtonCellInfo<>());

        TableColumn<DetailLocation, Boolean> delButton = new TableColumn("Delete");
        delButton.setMinWidth(80);
        delButton.setCellFactory((TableColumn<DetailLocation, Boolean> param) -> new ButtonCellDelete<>());

        TableColumn<DetailLocation, String> totalCost = new TableColumn("Total Harga");
        totalCost.setMinWidth(110);
        totalCost.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.currencyFormat.cFormat(calculationTotalCost(param.getValue().getTblDetail())), param.getValue().getTblDetail().itemQuantityProperty()));

        TableColumn<DetailLocation, String> expiredDateJustInfo = new TableColumn("Tgl. Kadaluarsa");
        expiredDateJustInfo.setMinWidth(115);
        expiredDateJustInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getDetailExpiredDate() == null
                                ? "-"
                                : ClassFormatter.dateFormate.format(param.getValue().getDetailExpiredDate()),
                        param.getValue().detailExpiredDateProperty()));

        TableColumn<DetailLocation, String> quantityInfo = new TableColumn("Jumlah");
        quantityInfo.setMinWidth(80);
        quantityInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDetailQuantity()), param.getValue().detailQuantityProperty()));

        TableColumn<DetailLocation, String> totalCostInfo = new TableColumn("Total Harga");
        totalCostInfo.setMinWidth(105);
        totalCostInfo.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.currencyFormat.cFormat(calculationTotalCostInfo(param.getValue())), param.getValue().getTblDetail().itemQuantityProperty()));

        if (dataInputStatus == 3) {   //just for show
            tableView.getColumns().addAll(itemTitle, expiredDateJustInfo, titleCodeNumber, cost, discount, quantityInfo, unitName, tax, totalCostInfo, location);
        } else {
            tableView.getColumns().addAll(itemTitle, titleCodeNumber, cost, discount, quantity, unitName, dataDetailsInfo, tax, totalCost, location);
        }

        tableView.setItems(FXCollections.observableArrayList(detailLocations));

        tableReturDetail = new ClassTableWithControl(tableView);
    }

    private String getItemSistem(DetailLocation data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            result = data.getTblDetail().getTblSupplierItem().getTblItem().getItemName()
                    + "\n(" + data.getTblDetail().getTblSupplierItem().getTblItem().getCodeItem() + ")";
        }
        return result;
    }

    private String getItemSupplier(DetailLocation data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem().getSupplierItemName() != null) {
                result = data.getTblDetail().getTblSupplierItem().getSupplierItemName()
                        + "\n(" + data.getTblDetail().getTblSupplierItem().getSupllierItemCode() + ")";
            }
        }
        return result;
    }

//    private String getDataItemInfo(DetailLocation dataDetailLocation) {
//        String result = "";
//        if (dataDetailLocation != null
//                && dataDetailLocation.getTblDetail() != null) {
//            if (dataDetailLocation.getTblDetail().getTblSupplierItem()!= null
//                    && dataDetailLocation.getTblDetail().getTblSupplierItem().getTblItem() != null) {
//                //data item
//                result += dataDetailLocation.getTblDetail().getTblSupplierItem().getTblItem().getItemName();
//                result += "\n";
//                //data item type - item gust type
//                result += dataDetailLocation.getTblDetail().getTblSupplierItem().getTblItem().getMononnonaka().getTypeName() + " "
//                        + (dataDetailLocation.getTblDetail().getTblSupplierItem().getTblItem().getGuestStatus() //Guest
//                                ? "(G)" : "(NG)");
//                //data item - barcode
//                if (dataDetailLocation.getTblDetailPropertyBarcode() != null
//                        && dataDetailLocation.getTblDetailPropertyBarcode().getTblPropertyBarcode() != null) {
//                    result += "\n";
//                    result += "(" + dataDetailLocation.getTblDetailPropertyBarcode().getTblPropertyBarcode().getCodeBarcode() + ")";
//                }
//            }
//        }
//        return result;
//    }
    private BigDecimal calculationTotalCost(TblReturDetail dataDetail) {
        return ((new BigDecimal("1").add(dataDetail.getItemTaxPercentage())))
                .multiply((dataDetail.getItemCost().subtract(dataDetail.getItemDiscount()))
                        .multiply(dataDetail.getItemQuantity()));
    }

    private BigDecimal calculationTotalCostInfo(DetailLocation detailLocation) {
        return ((new BigDecimal("1")).add(detailLocation.getTblDetail().getItemTaxPercentage()))
                .multiply((detailLocation.getTblDetail().getItemCost().subtract(detailLocation.getTblDetail().getItemDiscount()))
                        .multiply(detailLocation.getDetailQuantity()));
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
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataDetailUpdateHandle();
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
        tableReturDetail.addButtonControl(buttonControls);
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

                cbpLocationOfWarehouse.hide();

                cbpLocationOfWarehouse.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                cbpLocationOfWarehouse.valueProperty().bindBidirectional(((DetailLocation) this.getTableRow().getItem()).tblLocationWarehouseProperty());

                setText(null);
                setGraphic(cbpLocationOfWarehouse);
                cbpLocationOfWarehouse.getEditor().selectAll();

//                //cell input color
//                if (this.getTableRow() != null
//                        && dataInputStatus != 3) {
//                    if (this.getTableRow().getIndex() % 2 == 0) {
//                        getStyleClass().remove("cell-input-even");
//                        getStyleClass().add("cell-input-even-edit");
//                    } else {
//                        getStyleClass().remove("cell-input-odd");
//                        getStyleClass().add("cell-input-odd-edit");
//                    }
//                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            cbpLocationOfWarehouse.valueProperty().unbindBidirectional(((DetailLocation) this.getTableRow().getItem()).tblLocationWarehouseProperty());

            setText((String) getItem());
            setGraphic(null);

//            //cell input color
//            if (this.getTableRow() != null
//                    && dataInputStatus != 3) {
//                if (this.getTableRow().getIndex() % 2 == 0) {
//                    getStyleClass().remove("cell-input-even-edit");
//                    getStyleClass().add("cell-input-even");
//                } else {
//                    getStyleClass().remove("cell-input-odd-edit");
//                    getStyleClass().add("cell-input-odd");
//                }
//            }
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
//                        //cell input color
//                        if (dataInputStatus != 3) {
//                            if (this.getTableRow().getIndex() % 2 == 0) {
//                                getStyleClass().add("cell-input-even");
//
//                            } else {
//                                getStyleClass().add("cell-input-odd");
//                            }
//                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
        }

    }

    class EditingCellQuantity extends TableCell<DetailLocation, String> {

        private JFXTextField tItemQuantity;

        public EditingCellQuantity() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                if (((DetailLocation) this.getTableRow().getItem()).getTblDetail() != null
                        && !(((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) //Property
                        && !((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblSupplierItem().getTblItem().getConsumable()) {  //!consumable
                    tItemQuantity = new JFXTextField();
                    tItemQuantity.setPromptText("Jumlah (Barang)");

                    tItemQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                    ClassFormatter.setToNumericField("BigDecimal", tItemQuantity);

                    Bindings.bindBidirectional(tItemQuantity.textProperty(), ((DetailLocation) this.getTableRow().getItem()).getTblDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

                    setText(null);
                    setGraphic(tItemQuantity);
                    tItemQuantity.selectAll();

//                    //cell input color
//                    if (this.getTableRow() != null
//                            && !(((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblItem().getPropertyStatus()) //Property
//                            && !((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblItem().getConsumable() //!consumable
//                            && dataInputStatus != 3) {
//                        if (this.getTableRow().getIndex() % 2 == 0) {
//                            getStyleClass().remove("cell-input-even");
//                            getStyleClass().add("cell-input-even-edit");
//                        } else {
//                            getStyleClass().remove("cell-input-odd");
//                            getStyleClass().add("cell-input-odd-edit");
//                        }
//                    }
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            if (((DetailLocation) this.getTableRow().getItem()).getTblDetail() != null
                    && !(((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) //Property
                    && !((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblSupplierItem().getTblItem().getConsumable()) {  //!consumable

                tItemQuantity.textProperty().unbindBidirectional(((DetailLocation) this.getTableRow().getItem()).getTblDetail().itemQuantityProperty());

                //calculation total retur
                lblTotalRetur.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRetur()));

                setText((String) getItem());
                setGraphic(null);

            }

//            //cell input color
//            if (this.getTableRow() != null
//                    && !(((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblItem().getPropertyStatus()) //Property
//                    && !((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblItem().getConsumable() //!consumable
//                    && dataInputStatus != 3) {
//                if (this.getTableRow().getIndex() % 2 == 0) {
//                    getStyleClass().remove("cell-input-even-edit");
//                    getStyleClass().add("cell-input-even");
//                } else {
//                    getStyleClass().remove("cell-input-odd-edit");
//                    getStyleClass().add("cell-input-odd");
//                }
//            }
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
//                        //cell input color
//                        if (!(((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblItem().getPropertyStatus()) //Property
//                                && !((DetailLocation) this.getTableRow().getItem()).getTblDetail().getTblItem().getConsumable() //!consumable
//                                && dataInputStatus != 3) {
//                            if (this.getTableRow().getIndex() % 2 == 0) {
//                                getStyleClass().add("cell-input-even");
//
//                            } else {
//                                getStyleClass().add("cell-input-odd");
//                            }
//                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
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
                            refreshData();
//                            setDataDetail();
                        }
                    });
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

    class EditingCellReturDDetails extends TableCell<DetailLocation, String> {

        private JFXCComboBoxPopup cbpPropertyDetails;

        public EditingCellReturDDetails() {

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

//                        cbpPropertyDetails.getStyleClass().add("detail-combo-box-popup");
                        cbpPropertyDetails.showingProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal) {
                                this.getTableView().getSelectionModel().clearAndSelect(this.getTableRow().getIndex());

                            }
                        });

                        cbpPropertyDetails.hide();

                        cbpPropertyDetails.setPrefSize(100, 25);
                        setAlignment(Pos.CENTER);

                        cbpPropertyDetails.setDisable(!(((DetailLocation) data).getTblDetail().getTblSupplierItem().getTblItem().getConsumable())); //consumable

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

    }

    class EditingCellReturDDetailsInfo extends TableCell<DetailLocation, String> {

        private JFXCComboBoxPopup cbpPropertyDetails;

        public EditingCellReturDDetailsInfo() {

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
                        DetailLocation a = (DetailLocation) data;
                        if (((DetailLocation) data).getTblDetail().getTblSupplierItem() == null
                                || ((DetailLocation) data).getTblDetail().getTblSupplierItem().getTblItem() == null
                                || !((DetailLocation) data).getTblDetail().getTblSupplierItem().getTblItem().getConsumable()) {   //!consumable
                            cbpPropertyDetails = getComboBoxDetails();
                        } else {
                            cbpPropertyDetails = getComboBoxItemExpiredDateDetails(((DetailLocation) data).getDetailItemExpiredDateSelecteds());
                        }

                        cbpPropertyDetails.showingProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal) {
                                this.getTableView().getSelectionModel().clearAndSelect(this.getTableRow().getIndex());

                            }
                        });

                        //!!!@@@###
                        cbpPropertyDetails.hide();

                        cbpPropertyDetails.setPrefSize(100, 25);
                        setAlignment(Pos.CENTER);

                        cbpPropertyDetails.setDisable(!(((DetailLocation) data).getTblDetail().getTblSupplierItem().getTblItem().getConsumable())); //consumable

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

    }

    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> getComboBoxWarehouse() {
        TableView<TblLocationOfWarehouse> tableWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn<>("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(140);

        tableWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());

        JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableWarehouse, warehouseItems, "", "Gudang *", false, 200, 200
        );

        return cbpWarehouse;
    }

    private List<TblLocationOfWarehouse> loadAllDataLocationOfWarehouse() {
        List<TblLocationOfWarehouse> list = parentController.getFReturManager().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(parentController.getFReturManager().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private JFXCComboBoxPopup getComboBoxItemExpiredDateDetails(List<DetailItemExpiredDateSelected> detailItemExpiredDateSelecteds) {

        JFXCComboBoxPopup cbpItemExpiredDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-details");
        button.setTooltip(new Tooltip("Tanggal Kadaluarsa"));
        button.setOnMouseClicked((e) -> cbpItemExpiredDetails.show());

        AnchorPane ancDataTableProperties = new AnchorPane();

        //set data table
        TableView<DetailItemExpiredDateSelected> tableItemExpiredDate = new TableView<>();
//        tableItemExpiredDate.setEditable(true);
        tableItemExpiredDate.setEditable(dataInputStatus != 3);

        TableColumn<DetailItemExpiredDateSelected, Boolean> selected = new TableColumn("Pilih");
        selected.setMinWidth(80);
        selected.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selected.setCellFactory((TableColumn<DetailItemExpiredDateSelected, Boolean> param) -> new CheckBoxCell<>(dataInputStatus == 3));
//        selected.setCellFactory(CheckBoxTableCell.forTableColumn(selected));
        selected.setEditable(true);

        TableColumn<DetailItemExpiredDateSelected, String> codeItemExpiredDate = new TableColumn("ID Barang");
        codeItemExpiredDate.setMinWidth(120);
        codeItemExpiredDate.setCellValueFactory((TableColumn.CellDataFeatures<DetailItemExpiredDateSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataDetailItemExporedDate() == null
                        || param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate() == null
                                ? "" : param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate().getCodeItemExpiredDate(), param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate().codeItemExpiredDateProperty()));

        TableColumn<DetailItemExpiredDateSelected, String> expiredDate = new TableColumn("Tanggal Kadaluarsa");
        expiredDate.setMinWidth(140);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<DetailItemExpiredDateSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataDetailItemExporedDate() == null
                        || param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate() == null
                                ? "-" : ClassFormatter.dateFormate.format(param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate()), param.getValue().getDataDetailItemExporedDate().getTblItemExpiredDate().itemExpiredDateProperty()));

        tableItemExpiredDate.getColumns().addAll(codeItemExpiredDate, expiredDate);
        tableItemExpiredDate.setItems(FXCollections.observableArrayList(detailItemExpiredDateSelecteds));

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
        content.setPrefSize(280, 300);

        content.setCenter(ancContent);

        cbpItemExpiredDetails.setPopupEditor(false);
        cbpItemExpiredDetails.promptTextProperty().set("");
        cbpItemExpiredDetails.setLabelFloat(false);
        cbpItemExpiredDetails.setPopupButton(button);
        cbpItemExpiredDetails.settArrowButton(true);
        cbpItemExpiredDetails.setPopupContent(content);

        cbpItemExpiredDetails.getStyleClass().add("detail-combo-box-popup");

        return cbpItemExpiredDetails;
    }

    public class CheckBoxCell<S, T> extends TableCell<S, T> {

        private final CheckBox checkBox;
        private ObservableValue<T> ov;

//        public CheckBoxCell() {
//            checkBox = new CheckBox();
//            checkBox.setAlignment(Pos.CENTER);
//            setAlignment(Pos.CENTER);
//            setGraphic(checkBox);
//        }
        public CheckBoxCell(boolean disable) {
            checkBox = new CheckBox();
            checkBox.setAlignment(Pos.CENTER);
            setAlignment(Pos.CENTER);
            checkBox.setDisable(disable);
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

    private JFXCComboBoxPopup getComboBoxDetails() {

        JFXCComboBoxPopup cbpDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-details");
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
        content.setPrefSize(300, 300);

        content.setCenter(ancContent);

        cbpDetails.setPopupEditor(false);
        cbpDetails.promptTextProperty().set("");
        cbpDetails.setLabelFloat(false);
        cbpDetails.setPopupButton(button);
        cbpDetails.settArrowButton(true);
        cbpDetails.setPopupContent(content);

        cbpDetails.getStyleClass().add("detail-combo-box-popup");

        return cbpDetails;
    }

    public class DetailLocation {

        private final ObjectProperty<TblReturDetail> tblDetail = new SimpleObjectProperty<>();

        private final ObjectProperty<TblLocationOfWarehouse> tblLocationOfWarehouse = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReturDetailPropertyBarcode> tblDetailPropertyBarcode = new SimpleObjectProperty<>();

        private final ObservableList<DetailItemExpiredDateSelected> detailItemExpiredDateSelecteds = FXCollections.observableArrayList();

        private final ObjectProperty<JFXCComboBoxPopup> cbpListDetail = new SimpleObjectProperty<>();

        private JFXButton btnMessage = new JFXButton("?");

        private final BooleanProperty created = new SimpleBooleanProperty();

        //FOR VIEW DETAIL (detailInputStatus == 3)
        private final ObjectProperty<java.util.Date> detailExpiredDate = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> detailQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        public DetailLocation(TblReturDetail tblDetail,
                TblLocationOfWarehouse tblLocationOfWarehouse,
                TblReturDetailPropertyBarcode tblDetailPropertyBarcode,
                List<DetailItemExpiredDateSelected> detailItemExpiredDateSelecteds,
                boolean created) {
            //data detail
            this.tblDetail.set(tblDetail);

            //data location(warehouse)
            this.tblLocationOfWarehouse.set(tblLocationOfWarehouse);
            //data rd - pb list
            this.tblDetailPropertyBarcode.set(tblDetailPropertyBarcode);
            //data rd - ie - s list
            this.detailItemExpiredDateSelecteds.setAll(detailItemExpiredDateSelecteds);
            if (this.tblDetail == null
                    || this.tblDetail.get().getTblSupplierItem() == null
                    || this.tblDetail.get().getTblSupplierItem().getTblItem() == null
                    || !this.tblDetail.get().getTblSupplierItem().getTblItem().getConsumable()) {   //!consumable
                //data cb-list combobox
                this.cbpListDetail.set(getComboBoxDetails());
            } else {
                //data cb-list combobox
                this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.detailItemExpiredDateSelecteds));
            }
            //data create
            this.created.set(created);
            //button-message
            this.btnMessage.setPrefSize(25, 25);
        }

        public ObjectProperty<TblReturDetail> tblDetailProperty() {
            return tblDetail;
        }

        public TblReturDetail getTblDetail() {
            return tblDetailProperty().get();
        }

        public void setTblDetail(TblReturDetail detail) {
            tblDetailProperty().set(detail);
        }

        public ObjectProperty<TblReturDetailPropertyBarcode> tblDetailPropertyBarcodeProperty() {
            return tblDetailPropertyBarcode;
        }

        public TblReturDetailPropertyBarcode getTblDetailPropertyBarcode() {
            return tblDetailPropertyBarcodeProperty().get();
        }

        public void setTblDetailPropertyBarcode(TblReturDetailPropertyBarcode detailPB) {
            tblDetailPropertyBarcodeProperty().set(detailPB);
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

        public List<DetailItemExpiredDateSelected> getDetailItemExpiredDateSelecteds() {
            return detailItemExpiredDateSelecteds;
        }

        public void setDetailItemExpiredDateQuantities(List<DetailItemExpiredDateSelected> detailItemExpiredDateSelecteds) {
            this.detailItemExpiredDateSelecteds.setAll(detailItemExpiredDateSelecteds);
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

        public BooleanProperty createdProperty() {
            return created;
        }

        public boolean getCreated() {
            return createdProperty().get();
        }

        public void setCreated(boolean created) {
            createdProperty().set(created);
        }

        //----------------------------------------------------------------------
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

    public class DetailItemExpiredDateSelected {

        private final ObjectProperty<TblReturDetailItemExpiredDate> dataDetailItemExporedDate = new SimpleObjectProperty<>();

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public DetailItemExpiredDateSelected(TblReturDetailItemExpiredDate dataDetailItemExporedDate,
                boolean selected) {
            //data detail item expired date
            this.dataDetailItemExporedDate.set(dataDetailItemExporedDate);
            //data selected
            this.selected.set(selected);
            //set listener to selected
            this.selected.addListener((obs, oldVal, newVal) -> {
                //refresh detail - item expired date list
                refreshDetailLocationDetailItemExpuredDateSelectedList(this.dataDetailItemExporedDate.get(), newVal);
                //refresh detail - item quantity
                refreshDetailLocationItemQuantity();
            });
        }

        public ObjectProperty<TblReturDetailItemExpiredDate> dataDetailItemExporedDateProperty() {
            return dataDetailItemExporedDate;
        }

        public TblReturDetailItemExpiredDate getDataDetailItemExporedDate() {
            return dataDetailItemExporedDateProperty().get();
        }

        public void setDataDetailItemExporedDate(TblReturDetailItemExpiredDate dataDetailItemExporedDate) {
            dataDetailItemExporedDateProperty().set(dataDetailItemExporedDate);
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

    public class DetailItemExpiredDateQuantity {

        private final ObjectProperty<TblReturDetailItemExpiredDate> dataDetailItemExporedDate = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> itemQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        public DetailItemExpiredDateQuantity(
                TblReturDetailItemExpiredDate dataDetailItemExporedDate,
                BigDecimal itemQuantity) {

            this.dataDetailItemExporedDate.set(dataDetailItemExporedDate);
            this.itemQuantity.set(itemQuantity);
        }

        public ObjectProperty<TblReturDetailItemExpiredDate> dataDetailItemExporedDateProperty() {
            return dataDetailItemExporedDate;
        }

        public TblReturDetailItemExpiredDate getDataDetailItemExporedDate() {
            return dataDetailItemExporedDateProperty().get();
        }

        public void setDataDetailItemExporedDate(TblReturDetailItemExpiredDate dataDetailItemExporedDate) {
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

    public DetailItemExpiredDateSelected getReturDetailItemExpiredDateSelectedFromMIDetailItemExpiredDateSelected(
            TblReturDetail returDetail,
            ReturDetailLocationController.MIDetailItemExpiredDateSelected midDetailItemExpiredDateSelected) {
        if (midDetailItemExpiredDateSelected != null) {
            TblReturDetailItemExpiredDate returDetailItemExpiredDate = new TblReturDetailItemExpiredDate();
            returDetailItemExpiredDate.setTblReturDetail(returDetail);
            returDetailItemExpiredDate.setTblItemExpiredDate(midDetailItemExpiredDateSelected.getDataDetailItemExpiredDate().getTblItemExpiredDate());
            DetailItemExpiredDateSelected detailItemExpiredDateSelected = new DetailItemExpiredDateSelected(
                    returDetailItemExpiredDate,
                    midDetailItemExpiredDateSelected.isSelected());
            return detailItemExpiredDateSelected;
        }
        return null;
    }

    private void refreshDetailLocationItemQuantity() {
        for (DetailLocation detailLocation : detailLocations) {
            if (detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getConsumable()) {   //consumable
                double countItemExpiredDateSelected = 0;
                for (DetailItemExpiredDateSelected detailItemExpiredDateSelected : detailLocation.getDetailItemExpiredDateSelecteds()) {
                    if (detailItemExpiredDateSelected.isSelected()) {
                        countItemExpiredDateSelected++;
                    }
                }
                detailLocation.getTblDetail().setItemQuantity(new BigDecimal(String.valueOf(countItemExpiredDateSelected)));
            }
        }
        //calculation total retur
        lblTotalRetur.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRetur()));
    }

    private void refreshDetailLocationDetailItemExpuredDateSelectedList(
            TblReturDetailItemExpiredDate dataDetailItemExporedDate,
            boolean selected) {
        if (dataDetailItemExporedDate != null) {
            if (selected) {   //selected
                for (DetailLocation detailLocation : detailLocations) {
                    if (detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getConsumable() //consumable
                            //                            && detailLocation.getTblDetail().getTblItem().getIditem() == dataDetailItemExporedDate.getTblReturDetail().getTblItem().getIditem()
                            && !detailLocation.getTblDetail().equals(dataDetailItemExporedDate.getTblReturDetail())) {   //retur detail was not same
                        for (int i = detailLocation.getDetailItemExpiredDateSelecteds().size() - 1; i > -1; i--) {
                            if (detailLocation.getDetailItemExpiredDateSelecteds().get(i).getDataDetailItemExporedDate().getTblItemExpiredDate().getIditemExpiredDate()
                                    == dataDetailItemExporedDate.getTblItemExpiredDate().getIditemExpiredDate()) {
                                detailLocation.getDetailItemExpiredDateSelecteds().remove(i);
                                //reset data list combobox on detail location
                                detailLocation.setCbpListDetail(getComboBoxItemExpiredDateDetails(detailLocation.getDetailItemExpiredDateSelecteds()));
                                break;
                            }
                        }
                    }
                }
            } else {  //unselected
                for (DetailLocation detailLocation : detailLocations) {
                    if (detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getConsumable() //consumable
                            //                            && detailLocation.getTblDetail().getTblItem().getIditem() == dataDetailItemExporedDate.getTblReturDetail().getTblItem().getIditem()
                            && !detailLocation.getTblDetail().equals(dataDetailItemExporedDate.getTblReturDetail())) {   //retur detail was not same
                        //data item expired date
                        TblItemExpiredDate itemExpiredDate = new TblItemExpiredDate(dataDetailItemExporedDate.getTblItemExpiredDate());
                        itemExpiredDate.setTblItem(new TblItem(itemExpiredDate.getTblItem()));
                        if (itemExpiredDate.getTblItem().getTblItemTypeHk() != null) {
                            itemExpiredDate.getTblItem().setTblItemTypeHk(new TblItemTypeHk(itemExpiredDate.getTblItem().getTblItemTypeHk()));
                        }
                        if (itemExpiredDate.getTblItem().getTblItemTypeWh() != null) {
                            itemExpiredDate.getTblItem().setTblItemTypeWh(new TblItemTypeWh(itemExpiredDate.getTblItem().getTblItemTypeWh()));
                        }
                        //data detail item expired date
                        TblReturDetailItemExpiredDate returDetailItemExpiredDate = new TblReturDetailItemExpiredDate();
                        returDetailItemExpiredDate.setTblReturDetail(detailLocation.getTblDetail());
                        returDetailItemExpiredDate.setTblItemExpiredDate(itemExpiredDate);
                        //data detail item expired date - selected
                        DetailItemExpiredDateSelected detailItemExpiredDateSelected = new DetailItemExpiredDateSelected(returDetailItemExpiredDate, false);
                        detailLocation.getDetailItemExpiredDateSelecteds().add(detailItemExpiredDateSelected);
                        bubbleSort(detailLocation.getDetailItemExpiredDateSelecteds());
                        //reset data list combobox on detail location
                        detailLocation.setCbpListDetail(getComboBoxItemExpiredDateDetails(detailLocation.getDetailItemExpiredDateSelecteds()));
                    }
                }
            }
        }
    }

    private void bubbleSort(List<DetailItemExpiredDateSelected> arr) {
        boolean swapped = true;
        int j = 0;
        DetailItemExpiredDateSelected tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < arr.size() - j; i++) {
                if (arr.get(i).getDataDetailItemExporedDate().getTblItemExpiredDate().getIditemExpiredDate()
                        > arr.get(i + 1).getDataDetailItemExporedDate().getTblItemExpiredDate().getIditemExpiredDate()) {
                    tmp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }

    public DetailLocation generateDataDetailLocation(TblReturDetail dataDetail,
            TblReturDetailPropertyBarcode dataDetailPropertyBarcode) {
        List<DetailItemExpiredDateSelected> detailItemExpiredDateSelecteds = new ArrayList<>();
        if (dataDetail != null
                && dataDetail.getTblSupplierItem() != null
                && dataDetail.getTblSupplierItem().getTblItem() != null) {
            if (dataDetail.getIddetailRetur() == 0L) {    //new data
                List<TblItemExpiredDate> itemExpiredDates = parentController.getFReturManager().getAllDataItemExpiredDateByIDItem(dataDetail.getTblSupplierItem().getTblItem().getIditem());
                for (TblItemExpiredDate itemExpiredDate : itemExpiredDates) {
//                    if (itemExpiredDate.getRefItemExpiredDateStatus().getIdstatus() == 1) { //1 = 'Ada'
                        //data item
                        itemExpiredDate.setTblItem(parentController.getFReturManager().getDataItem(itemExpiredDate.getTblItem().getIditem()));
                        //data item type hk
                        if (itemExpiredDate.getTblItem().getTblItemTypeHk() != null) {
                            itemExpiredDate.getTblItem().setTblItemTypeHk(parentController.getFReturManager().getDataItemTypeHK(itemExpiredDate.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                        }
                        //data item type wh
                        if (itemExpiredDate.getTblItem().getTblItemTypeWh() != null) {
                            itemExpiredDate.getTblItem().setTblItemTypeWh(parentController.getFReturManager().getDataItemTypeWH(itemExpiredDate.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                        }
                        //data detail item expired date
                        TblReturDetailItemExpiredDate detailItemExpiredDate = new TblReturDetailItemExpiredDate();
                        detailItemExpiredDate.setTblReturDetail(dataDetail);
                        detailItemExpiredDate.setTblItemExpiredDate(itemExpiredDate);
                        //data detail item expired date - selected
                        DetailItemExpiredDateSelected detailItemExpiredDateSelected = new DetailItemExpiredDateSelected(detailItemExpiredDate, false);
                        //remove data has been selected with another data location
                        boolean found = false;
                        for (DetailLocation tempDetailLocation : detailLocations) {
                            if (tempDetailLocation.getTblDetail().getTblSupplierItem().getTblItem().getIditem()
                                    == detailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem().getIditem()) {
                                for (DetailItemExpiredDateSelected tempDetailItemExpiredDateSelected : tempDetailLocation.getDetailItemExpiredDateSelecteds()) {
                                    if (tempDetailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().getIditemExpiredDate()
                                            == detailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().getIditemExpiredDate()) {
                                        found = tempDetailItemExpiredDateSelected.isSelected();
                                        break;
                                    }
                                }
                                if (found) {
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            detailItemExpiredDateSelecteds.add(detailItemExpiredDateSelected);
                        }
//                    }
                }
            } else {    //load data (data has been saved)
                List<TblReturDetailItemExpiredDate> returDetailItemExpiredDates = parentController.getFReturManager().getAllDataReturDetailItemExpiredDateByIDReturDetail(dataDetail.getIddetailRetur());
                for (TblReturDetailItemExpiredDate returDetailItemExpiredDate : returDetailItemExpiredDates) {
                    //data detail item expired date
                    returDetailItemExpiredDate.setTblReturDetail(dataDetail);
                    returDetailItemExpiredDate.setTblItemExpiredDate(parentController.getFReturManager().getDataItemExpiredDate(returDetailItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                    returDetailItemExpiredDate.getTblItemExpiredDate().setTblItem(parentController.getFReturManager().getDataItem(returDetailItemExpiredDate.getTblItemExpiredDate().getTblItem().getIditem()));
                    //data detail item expired date - selected
                    detailItemExpiredDateSelecteds.add(new DetailItemExpiredDateSelected(returDetailItemExpiredDate, true));
                }
            }
        }
        DetailLocation data = new DetailLocation(dataDetail,
                (dataDetail != null && dataDetail.getTblLocation() != null)
                        ? parentController.getFReturManager().getDataWarehouseByIDLocation(dataDetail.getTblLocation().getIdlocation())
                        : null,
                dataDetailPropertyBarcode,
                detailItemExpiredDateSelecteds,
                false);
        return data;
    }

    public List<DetailLocation> detailLocations = new ArrayList<>();

    public List<DetailLocation> selectedDataDetailLocations = new ArrayList<>();

    public DetailLocation selectedDataDetailLocation;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailCreateHandle() {
        if (selectedData.getTblSupplier() != null) {
            dataInputDetailStatus = 0;
            TblReturDetail dataReturDetail = new TblReturDetail();
            dataReturDetail.setItemCost(new BigDecimal("0"));
            dataReturDetail.setItemQuantity(new BigDecimal("0"));
            dataReturDetail.setItemDiscount(new BigDecimal("0"));
            dataReturDetail.setItemTaxPercentage(new BigDecimal("0"));
            selectedDataDetailLocation = new DetailLocation(dataReturDetail, null, null, new ArrayList<>(), true);
            selectedDataDetailLocation.getTblDetail().setTblRetur(selectedData);
            //open form data - detail
            showDataDetailItemDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK ADA DATA SUPPLIER YANG DIPILIH", "Silahkan pilih data supplier terlebih dahulu..!", null);
        }
    }

    public void dataDetailUpdateHandle() {
        if (tableReturDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputDetailStatus = 1;
            DetailLocation dataDetailLocation = ((DetailLocation) tableReturDetail.getTableView().getSelectionModel().getSelectedItem());
            List<DetailItemExpiredDateSelected> list = new ArrayList<>();
            for (DetailItemExpiredDateSelected dataDetailItemExpiredDateSelected : dataDetailLocation.getDetailItemExpiredDateSelecteds()) {
                DetailItemExpiredDateSelected data = new DetailItemExpiredDateSelected(new TblReturDetailItemExpiredDate(dataDetailItemExpiredDateSelected.getDataDetailItemExporedDate()),
                        dataDetailItemExpiredDateSelected.isSelected());
                list.add(data);
            }
            selectedDataDetailLocation = new DetailLocation(
                    new TblReturDetail(dataDetailLocation.getTblDetail()),
                    new TblLocationOfWarehouse(dataDetailLocation.getTblLocationOfWarehouse()),
                    dataDetailLocation.getTblDetailPropertyBarcode() != null
                            ? new TblReturDetailPropertyBarcode(dataDetailLocation.getTblDetailPropertyBarcode())
                            : null,
                    list,
                    dataDetailLocation.getCreated());
            //data detail ...
            selectedDataDetailLocation.getTblDetail().setTblRetur(selectedData);
            selectedDataDetailLocation.getTblDetail().setTblMemorandumInvoice(new TblMemorandumInvoice(selectedDataDetailLocation.getTblDetail().getTblMemorandumInvoice()));
            selectedDataDetailLocation.getTblDetail().setTblSupplierItem(new TblSupplierItem(selectedDataDetailLocation.getTblDetail().getTblSupplierItem()));
            selectedDataDetailLocation.getTblDetail().getTblSupplierItem().setTblItem(new TblItem(selectedDataDetailLocation.getTblDetail().getTblSupplierItem().getTblItem()));
            selectedDataDetailLocation.getTblDetail().setTblLocation(selectedDataDetailLocation.getTblDetail().getTblLocation() != null
                    ? selectedDataDetailLocation.getTblDetail().getTblLocation() : null);
            //data location ...
            selectedDataDetailLocation.getTblLocationOfWarehouse().setTblLocation(new TblLocation());
            //data property barcode
            if (selectedDataDetailLocation.getTblDetailPropertyBarcode() != null) {
                selectedDataDetailLocation.getTblDetailPropertyBarcode().setTblReturDetail(selectedDataDetailLocation.getTblDetail());
                selectedDataDetailLocation.getTblDetailPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(selectedDataDetailLocation.getTblDetailPropertyBarcode().getTblPropertyBarcode()));
            }
            //data item expired date (list) ...
            for (DetailItemExpiredDateSelected data : selectedDataDetailLocation.getDetailItemExpiredDateSelecteds()) {
                data.setDataDetailItemExporedDate(new TblReturDetailItemExpiredDate(data.getDataDetailItemExporedDate()));
                data.getDataDetailItemExporedDate().setTblReturDetail(selectedDataDetailLocation.getTblDetail());
                data.getDataDetailItemExporedDate().setTblItemExpiredDate(new TblItemExpiredDate(data.getDataDetailItemExporedDate().getTblItemExpiredDate()));
                data.getDataDetailItemExporedDate().getTblItemExpiredDate().setTblItem(new TblItem(data.getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem()));
            }
            //open form data - detail
            showDataDetailItemDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataDetailDeleteHandle() {
        if (tableReturDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                detailLocations.remove((DetailLocation) tableReturDetail.getTableView().getSelectionModel().getSelectedItem());
                //refresh bill
                refreshData();
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataDetailItemDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur/ReturDetailLocationDialog.fxml"));

            ReturDetailLocationController controller = new ReturDetailLocationController(this);
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

    public void refreshData() {
        //refresh data detail
//        setTableDataDetail();
        tableReturDetail.getTableView().setItems(FXCollections.observableArrayList(detailLocations));
        //calculation total retur
        lblTotalRetur.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRetur()));
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
        setDataReturSplitpane();

        //init table
        initTableDataRetur();

        //init form
        initFormDataRetur();

        spDataRetur.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReturFormShowStatus.set(0.0);
        });
    }

    public ReturController(FeatureReturController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReturController parentController;

    public FReturManager getService() {
        return parentController.getFReturManager();
    }

}
