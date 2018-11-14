/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_storing;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
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
import hotelfx.helper.PrintModel.ClassPrintSuratJalan;
import hotelfx.helper.PrintModel.ClassPrintSuratJalanDetail;
import hotelfx.persistence.model.RefReturPaymentStatus;
import hotelfx.persistence.model.RefReturType;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.service.FReturManager;
import hotelfx.persistence.service.FReturManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_retur.FeatureReturWController;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReturWController implements Initializable {

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

        TableColumn<TblRetur, String> codeRetur = new TableColumn("No. Retur");
        codeRetur.setCellValueFactory(cellData -> cellData.getValue().codeReturProperty());
        codeRetur.setMinWidth(120);

        TableColumn<TblRetur, String> deliveryNumber = new TableColumn("No. Surat Jalan");
        deliveryNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDeliveryNumber(),
                        param.getValue().deliveryNumberProperty()));
        deliveryNumber.setMinWidth(120);

        TableColumn<TblRetur, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSupplier().getSupplierName(), param.getValue().tblSupplierProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblRetur, String> returDate = new TableColumn<>("Tanggal Retur");
        returDate.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getReturDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getReturDate())
                                : "-", param.getValue().returDateProperty()));
        returDate.setMinWidth(140);

        TableColumn<TblRetur, String> createdDateBy = new TableColumn<>("Buat");
        createdDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCreatedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCreatedBy() != null)
                                ? param.getValue().getTblEmployeeByCreatedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().createdDateProperty()));
        createdDateBy.setMinWidth(140);

        TableColumn<TblRetur, String> approvedDateBy = new TableColumn<>("Persetujuan");
        approvedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getApprovedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByApprovedBy() != null)
                                ? param.getValue().getTblEmployeeByApprovedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().approvedDateProperty()));
        approvedDateBy.setMinWidth(140);

        TableColumn<TblRetur, String> canceledDateBy = new TableColumn<>("Pembatalan");
        canceledDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCanceledDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCanceledDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCanceledBy() != null)
                                ? param.getValue().getTblEmployeeByCanceledBy().getTblPeople().getFullName()
                                : "")
                        + (param.getValue().getCanceledNote() != null
                                ? ("\n" + param.getValue().getCanceledNote())
                                : ""),
                        param.getValue().canceledDateProperty()));
        canceledDateBy.setMinWidth(140);

        TableColumn<TblRetur, String> dateByTitle = new TableColumn("Tanggal-Oleh");
        dateByTitle.getColumns().addAll(createdDateBy, approvedDateBy, canceledDateBy);

        TableColumn<TblRetur, String> returStatus = new TableColumn<>("Status");
        returStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReturStatus().getStatusName(),
                        param.getValue().refReturStatusProperty()));
        returStatus.setMinWidth(90);

        tableView.getColumns().addAll(codeRetur, deliveryNumber, supplierName, returDate, dateByTitle, returStatus);

        tableView.setItems(loadAllDataRetur());

        tableView.setRowFactory(tv -> {
            TableRow<TblRetur> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataReturUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (checkDataReturDeliveryNumberEnableToPrint((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
                                dataReturDeliveryNumberPrintHandleDetail();
                            } else {
                                dataReturShowHandle();
                            }
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
                dataReturWCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReturWDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak Surat Jalan");
            buttonControl.setOnMouseClicked((e) -> {
                dataReturDeliveryNumberPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getApproveData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Setujui");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener approve
//                dataReturPApproveHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Batal");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener cancel
//                dataReturPDeleteHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataRetur.addButtonControl(buttonControls);
    }

    private ObservableList<TblRetur> loadAllDataRetur() {
        List<TblRetur> list = getService().getAllDataRetur();
        for (TblRetur data : list) {
            //data supplier
            data.setTblSupplier(getService().getDataSupplier(data.getTblSupplier().getIdsupplier()));
            //data location (supplier)
            data.getTblSupplier().setTblLocation(getService().getDataLocation(data.getTblSupplier().getTblLocation().getIdlocation()));
            //data hotel receivable
            if (data.getTblHotelReceivable() != null) {
                data.setTblHotelReceivable(getService().getDataHotelReceivable(data.getTblHotelReceivable().getIdhotelReceivable()));
            }
            //data retur status
            data.setRefReturStatus(getService().getDataReturStatus(data.getRefReturStatus().getIdstatus()));
        }
        //remove data isn't used
        for (int i = list.size() - 1; i > -1; i--) {
//            if (list.get(i).getRefReturStatus().getIdstatus() == 2 //Dibatalkan = '2'
//                    || list.get(i).getRefReturStatus().getIdstatus() == 3) { //Sudah Tidak Berlaku = '3'
//                list.remove(i);
//            }
            if (list.get(i).getRefReturStatus().getIdstatus() == 3) { //Sudah Tidak Berlaku = '3'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private List<DetailLocation> loadAllDataReturDetail() {
        List<DetailLocation> list = new ArrayList<>();
        if (selectedData.getIdretur() != 0L) {
            List<TblReturDetail> dataReturDetails = getService().getAllDataReturDetailByIDRetur(selectedData.getIdretur());
            for (TblReturDetail dataReturDetail : dataReturDetails) {
                //data retur - detail
                TblReturDetail dataDetail = dataReturDetail;
                dataDetail.setTblRetur(selectedData);
                dataDetail.setTblMemorandumInvoice(getService().getDataMemorandumInvoice(dataDetail.getTblMemorandumInvoice().getIdmi()));
                dataDetail.setTblSupplierItem(getService().getDataSupplierItem(dataDetail.getTblSupplierItem().getIdrelation()));
                dataDetail.getTblSupplierItem().setTblItem(getService().getDataItem(dataDetail.getTblSupplierItem().getTblItem().getIditem()));
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                dataDetail.getTblSupplierItem().getTblItem().setTblUnit(getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                dataDetail.setTblLocation(getService().getDataLocation(dataDetail.getTblLocation().getIdlocation()));
                if (dataDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) { //Property
                    List<TblReturDetailPropertyBarcode> dataReturDetailPropertyBarcodes = getService().getAllDataReturDetailPropertyBarcodeByIDReturDetail(dataDetail.getIddetailRetur());
                    for (TblReturDetailPropertyBarcode dataReturDetailPropertyBarcode : dataReturDetailPropertyBarcodes) {
                        //data retur-detail : quantity = 1
                        dataDetail.setItemQuantity(new BigDecimal("1"));
                        //data retur-detail-property-barcode
                        TblReturDetailPropertyBarcode dataDetailPropertyBarcode = dataReturDetailPropertyBarcode;
                        dataDetailPropertyBarcode.setTblReturDetail(dataDetail);
                        dataDetailPropertyBarcode.setTblPropertyBarcode(getService().getDataPropertyBarcode(dataDetailPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                        //add data to list 'detail_location'
                        DetailLocation detailLocation = generateDataDetailLocation(dataDetail, dataDetailPropertyBarcode, null);
                        detailLocation.setDetailQuantity(new BigDecimal(String.valueOf(dataDetail.getItemQuantity())));
                        list.add(detailLocation);
                    }
                } else {
                    if (dataDetail.getTblSupplierItem().getTblItem().getConsumable()) {    //consumable
                        //data retur - detail - item expired date
                        List<TblReturDetailItemExpiredDate> dataReturDetailPropertyItemExpiredDates
                                = getService().getAllDataReturDetailItemExpiredDateByIDReturDetail(dataDetail.getIddetailRetur());
                        for (TblReturDetailItemExpiredDate dataReturDetailPropertyItemExpiredDate : dataReturDetailPropertyItemExpiredDates) {
                            //data retur - detail
                            dataReturDetailPropertyItemExpiredDate.setTblReturDetail(dataReturDetail);
                            //data item expired
                            dataReturDetailPropertyItemExpiredDate.setTblItemExpiredDate(getService().getDataItemExpiredDate(dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                            //data detail location
                            DetailLocation detailLocation = generateDataDetailLocation(dataDetail, null, dataReturDetailPropertyItemExpiredDate);
                            detailLocation.setDetailExpiredDate(dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate());
                            detailLocation.setDetailQuantity(dataReturDetailPropertyItemExpiredDate.getItemQuantity());
                            //add data to list 'detail_location'
                            list.add(detailLocation);
                        }
                    } else {
                        //add data to list 'detail_location'
                        DetailLocation detailLocation = generateDataDetailLocation(dataDetail, null, null);
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
    private AnchorPane btnPrintLayout;

    @FXML
    private AnchorPane btnSaveLayout;

    @FXML
    private GridPane gpFormDataRetur;

    @FXML
    private ScrollPane spFormDataRetur;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeRetur;

    @FXML
    private JFXTextField txtDeliveryNumberR;

    @FXML
    private JFXTextArea txtReturNote;

    @FXML
    private Label lblTotalRetur;

    @FXML
    private AnchorPane ancMILayout;
    public JFXCComboBoxTablePopup<TblMemorandumInvoice> cbpMI;

    @FXML
    private JFXTextField txtDeliveryNumberS;

    @FXML
    private JFXTextField txtCodePO;

    @FXML
    private JFXTextField txtTaxPercentage;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private AnchorPane ancSupplierLayout;
    private JFXCComboBoxTablePopup<TblSupplier> cbpSupplier;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnPrint;

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
            dataReturSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataReturCancelHandle();
        });

        btnPrint.getStyleClass().add("button-printer");
        btnPrint.setTooltip(new Tooltip("Cetak Surat Jalan"));
        btnPrint.setOnAction((e) -> {
            printDeliveryNumber();
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
        cbpMI.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtDeliveryNumberS.setText(newVal.getDeliveryNumber());
                txtCodePO.setText(newVal.getTblPurchaseOrder().getCodePo());
                txtTaxPercentage.setText(ClassFormatter.decimalFormat.cFormat(newVal.getTblPurchaseOrder().getTaxPecentage().multiply(new BigDecimal("100"))) + "%");
                txtSupplierName.setText(newVal.getTblPurchaseOrder().getTblSupplier().getSupplierName());
                cbpSupplier.setValue(newVal.getTblPurchaseOrder().getTblSupplier());

            } else {
                txtDeliveryNumberS.setText(null);
                txtCodePO.setText(null);
                txtTaxPercentage.setText(null);
                txtSupplierName.setText(null);
                cbpSupplier.setValue(null);

            }
        });

        txtCodeRetur.setVisible(false);
        txtDeliveryNumberR.setVisible(false);

        ancSupplierLayout.setVisible(false);

        txtTaxPercentage.setVisible(false);
        lblTotalRetur.setVisible(false);

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpMI,
                cbpSupplier,
                txtReturNote);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal");
    }

    private void initDataPopup() {
        //Memorandum Invoice
        TableView<TblMemorandumInvoice> tableMI = new TableView<>();

        TableColumn<TblMemorandumInvoice, String> codeMI = new TableColumn<>("No. Penerimaan");
        codeMI.setCellValueFactory(cellData -> cellData.getValue().codeMiProperty());
        codeMI.setMinWidth(120);

        TableColumn<TblMemorandumInvoice, String> deliveryNumber = new TableColumn("No. Surat Jalan");
        deliveryNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDeliveryNumber(),
                        param.getValue().deliveryNumberProperty()));
        deliveryNumber.setMinWidth(140);

        TableColumn<TblMemorandumInvoice, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPurchaseOrder().getCodePo(),
                        param.getValue().tblPurchaseOrderProperty()));
        codePO.setMinWidth(120);

        TableColumn<TblMemorandumInvoice, String> supplier = new TableColumn("Supplier");
        supplier.setCellValueFactory((TableColumn.CellDataFeatures<TblMemorandumInvoice, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPurchaseOrder().getTblSupplier().getSupplierName(),
                        param.getValue().tblPurchaseOrderProperty()));
        supplier.setMinWidth(140);

        tableMI.getColumns().addAll(codeMI, deliveryNumber, codePO, supplier);

        ObservableList<TblMemorandumInvoice> miItems = FXCollections.observableArrayList(loadAllDataMI());

        cbpMI = new JFXCComboBoxTablePopup<>(
                TblMemorandumInvoice.class, tableMI, miItems, "", "No. Penerimaan *", true, 530, 250
        );

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
        ancMILayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpMI, 0.0);
        AnchorPane.setLeftAnchor(cbpMI, 0.0);
        AnchorPane.setRightAnchor(cbpMI, 0.0);
        AnchorPane.setTopAnchor(cbpMI, 0.0);
        ancMILayout.getChildren().add(cbpMI);

        ancSupplierLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpSupplier, 0.0);
        AnchorPane.setLeftAnchor(cbpSupplier, 0.0);
        AnchorPane.setRightAnchor(cbpSupplier, 0.0);
        AnchorPane.setTopAnchor(cbpSupplier, 0.0);
        ancSupplierLayout.getChildren().add(cbpSupplier);
    }

    private ObservableList<TblSupplier> loadAllDataSupplier() {
        List<TblSupplier> list = getService().getAllDataSupplier();
        for (TblSupplier data : list) {
            //data location (supplier)
            data.setTblLocation(getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return FXCollections.observableArrayList(list);
    }

    private ObservableList<TblMemorandumInvoice> loadAllDataMI() {
        List<TblMemorandumInvoice> list = getService().getAllDataMemorandumInvoice();
        for (int i = list.size() - 1; i > -1; i--) {
            //data po
            list.get(i).setTblPurchaseOrder(getService().getDataPurchaseOrder(list.get(i).getTblPurchaseOrder().getIdpo()));
            //data supplier
            list.get(i).getTblPurchaseOrder().setTblSupplier(getService().getDataSupplier(list.get(i).getTblPurchaseOrder().getTblSupplier().getIdsupplier()));
            //data po - status
            list.get(i).getTblPurchaseOrder().setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(list.get(i).getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus()));
            if (list.get(i).getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus() != 5) {   //Dipesan = '5'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Memorandum Invoice
        ObservableList<TblMemorandumInvoice> miItems = FXCollections.observableArrayList(loadAllDataMI());
        cbpMI.setItems(miItems);

        //Supplier
        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
        cbpSupplier.setItems(supplierItems);
    }

//    private boolean editing = false;
    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeRetur() != null
                ? (selectedData.getCodeRetur() + " (N.S.J : " + selectedData.getDeliveryNumber() + ")") : "");

        txtReturNote.textProperty().bindBidirectional(selectedData.returNoteProperty());

        txtCodeRetur.textProperty().bindBidirectional(selectedData.codeReturProperty());

        cbpMI.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3 && dataInputStatus != 4 && dataInputStatus != 5) {
                if (newVal != null) {
                    //reset data details
                    detailLocations = new ArrayList<>();
                    //set data detail
                    tableReturDetail.getTableView().setItems(FXCollections.observableArrayList(detailLocations));
                    //calculation total retur
                    lblTotalRetur.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRetur()));
                }
            }
        });
        if (dataInputStatus != 0) {
            cbpMI.setValue(detailLocations.isEmpty() ? null : detailLocations.get(0).getTblDetail().getTblMemorandumInvoice());
        } else {
            cbpMI.setValue(null);
        }

        cbpMI.hide();
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
                (dataInputStatus == 3 || dataInputStatus == 4 || dataInputStatus == 5),
                txtCodeRetur);
        btnSaveLayout.setVisible(dataInputStatus == 0);
        btnPrintLayout.setVisible(dataInputStatus == 5 || dataInputStatus == 4);
        btnSave.setVisible(dataInputStatus == 0);
        btnPrint.setVisible(dataInputStatus == 5 || dataInputStatus == 4);
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
    //4 = 'approve'
    private int dataInputStatus = 0;

    public void dataReturWCreateHandle() {
        btnPrintLayout.setVisible(false);
        btnSaveLayout.setVisible(true);
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
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Simpan (Data Retur)"));
    }

//    private void dataReturUpdateHandle() {
//        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputStatus = 1;
//            selectedData = getService().getDataMemorandumInvoice(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()).getIdmi());
//            selectedData.setTblPurchaseOrder(getService().getDataPurchaseOrder(selectedData.getTblPurchaseOrder().getIdpo()));
//            selectedData.getTblPurchaseOrder().setTblSupplier(getService().getDataSupplier(selectedData.getTblPurchaseOrder().getTblSupplier().getIdsupplier()));
//            setSelectedDataToInputForm();
//            //open form data retur
//            dataReturFormShowStatus.set(0.0);
//            dataReturFormShowStatus.set(1.0);
//    //set unsaving data input -> 'true'
//        ClassSession.unSavingDataInput = true;
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
//        }
//    }
    private void dataReturPApproveHandle() {
        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPEnableToApprove((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
                dataInputStatus = 4;
                selectedData = getService().getDataRetur(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()).getIdretur());
                selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
                detailLocations = loadAllDataReturDetail();
                setSelectedDataToInputForm();
                //open form data retur
                dataReturFormShowStatus.set(0.0);
                dataReturFormShowStatus.set(1.0);
                //set unsaving data input -> 'true'
                ClassSession.unSavingDataInput.set(true);
                //set button tooltip
                btnSave.setTooltip(new Tooltip("Setujui (Data Retur)"));
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat disetujui..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataFromDateNow("", null);
        }
    }

    private boolean checkDataPEnableToApprove(TblRetur dataRetur) {
        return dataRetur.getRefReturStatus().getIdstatus() == 0;    //Dibuat = '0'
    }

    private void dataReturWDeleteHandle() {
        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataWEanbleToDelete((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    if (getService().deleteDataReturWV2((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        //refresh data from table & close form data retur
                        tableDataRetur.getTableView().setItems(loadAllDataRetur());
                        cft.refreshFilterItems(tableDataRetur.getTableView().getItems());
                        dataReturFormShowStatus.set(0.0);
                    } else {
                        ClassMessage.showFailedDeletingDataMessage(getService().getErrorMessage(), null);
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat dihapus..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataFromDateNow("", null);
        }
    }

    private boolean checkDataWEanbleToDelete(TblRetur dataRetur) {
        return dataRetur.getRefReturStatus().getIdstatus() == 0;    //Dibuat = '0'
    }

    private void dataReturPDeleteHandle() {
        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPEnableToDelete((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
                selectedData = getService().getDataRetur(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()).getIdretur());
                if (selectedData.getTblSupplier() != null) {
                    selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
                }
                selectedData.setRefReturStatus(getService().getDataReturStatus(selectedData.getRefReturStatus().getIdstatus()));
                //show dialog canceled
                showDataCanceledDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat dibatalkan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataFromDateNow("", null);
        }
    }

    private boolean checkDataPEnableToDelete(TblRetur dataRetur) {
        return dataRetur.getRefReturStatus().getIdstatus() == 0;    //Dibuat = '0'
    }

    private void showDataCanceledDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_storing/ReturCanceledWDialog.fxml"));

            ReturCanceledWController controller = new ReturCanceledWController(this);
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

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataReturShowHandle() {
        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getDataRetur(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()).getIdretur());
            selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
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

    private void dataReturDeliveryNumberPrintHandle() {
        if (tableDataRetur.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataReturDeliveryNumberEnableToPrint((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
                dataReturDeliveryNumberPrintHandleDetail();
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak dapat membuat surat jalan pada data retur yang belum disejutui..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataReturDeliveryNumberPrintHandleDetail() {
        btnPrintLayout.setVisible(true);
        btnSaveLayout.setVisible(false);
        dataInputStatus = 5;
        selectedData = getService().getDataRetur(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()).getIdretur());
        selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        detailLocations = loadAllDataReturDetail();
        setSelectedDataToInputForm();
        //printDeliveryNumber(selectedData);       
        dataReturFormShowStatus.set(0.0);
        dataReturFormShowStatus.set(1.0);
        // printRetur(((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem()));
    }

    private boolean checkDataReturDeliveryNumberEnableToPrint(TblRetur dataRetur) {
        return dataRetur.getRefReturStatus().getIdstatus() == 1;    //Disetujui = '1'
    }

    private void printDeliveryNumber() {
      // selectedData = getService().getDataRetur(selectedData.getIdretur());
        // selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        List<ClassPrintSuratJalan> listPrintSuratJalan = new ArrayList();
        ClassPrintSuratJalan printSuratJalan = new ClassPrintSuratJalan();
        printSuratJalan.setAlamatSupplier(selectedData.getTblSupplier().getSupplierAddress());
        printSuratJalan.setJabatanPembuat(selectedData.getTblEmployeeByCreateBy().getTblJob() != null ? selectedData.getTblEmployeeByCreateBy().getTblJob().getJobName() : "-");
        printSuratJalan.setKeterangan(selectedData.getReturNote());
        printSuratJalan.setNamaPIC(selectedData.getTblSupplier().getPicname());
        printSuratJalan.setNamaPembuat(selectedData.getTblEmployeeByCreateBy().getTblPeople().getFullName());
        printSuratJalan.setNamaSupplier(selectedData.getTblSupplier().getSupplierName());
        printSuratJalan.setNoTeleponPIC(selectedData.getTblSupplier().getPicphoneNumber());
        printSuratJalan.setNoTeleponSupplier(selectedData.getTblSupplier().getSupplierPhoneNumber());
        printSuratJalan.setKodeSuratJalan(selectedData.getDeliveryNumber());
        List<ClassPrintSuratJalanDetail> listReturDetail = new ArrayList();
        List<DetailLocation> listDetail = loadAllDataReturDetail();
        for (DetailLocation getReturDetail : listDetail) {
            ClassPrintSuratJalanDetail printSuratJalanDetail = new ClassPrintSuratJalanDetail();
            printSuratJalanDetail.setJumlah(getReturDetail.getDetailQuantity());
            printSuratJalanDetail.setKodeBarang(getReturDetail.getTblDetail().getTblSupplierItem().getSupllierItemCode());
            printSuratJalanDetail.setNamaBarang(getReturDetail.getTblDetail().getTblSupplierItem().getSupplierItemName());
            printSuratJalanDetail.setSatuan(getReturDetail.getTblDetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName());
            listReturDetail.add(printSuratJalanDetail);
        }
        printSuratJalan.setDetailSuratJalan(listReturDetail);
        listPrintSuratJalan.add(printSuratJalan);
        ClassPrinter.printDeliveryNumber(listPrintSuratJalan, printSuratJalan.getKodeSuratJalan());
    }

    public void dataReturSaveHandle() {
        if (dataInputStatus == 4) { //approve
            if (checkDataInputDataReturForApprove()) {
                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk menyetujui data ini?", "");
                if (alert.getResult() == ButtonType.OK) {
                    if (getService().approveDataReturPV2((TblRetur) tableDataRetur.getTableView().getSelectionModel().getSelectedItem())) {
                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil disetujui..!!", null);
                        //refresh data from table & close form data retur
                        refreshDataTableRetur();
                        dataReturFormShowStatus.set(0.0);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                    } else {
                        HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal disetujui..!!", getService().getErrorMessage());
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage(errDataInput, null);
            }
        } else {
            if (checkDataInputDataRetur()) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    //set location (from selected data 'LocationOfWarehouse')
                    for (DetailLocation detailLocation : detailLocations) {
                        if (detailLocation.getTblLocationOfWarehouse() != null) {
                            detailLocation.getTblDetail().setTblLocation(new TblLocation(detailLocation.getTblLocationOfWarehouse().getTblLocation()));
                        }
                    }
                    //set supplier
                    selectedData.setTblSupplier(cbpSupplier.getValue());
                    //dummy entry
                    TblRetur dummySelectedData = new TblRetur(selectedData);
                    dummySelectedData.setTblSupplier(new TblSupplier(dummySelectedData.getTblSupplier()));
                    if (dummySelectedData.getRefReturType() != null) {
                        dummySelectedData.setRefReturType(new RefReturType(dummySelectedData.getRefReturType()));
                    }
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
                                detailLocation.getTblDetailItemExpiredDate()!= null
                                        ? new TblReturDetailItemExpiredDate(detailLocation.getTblDetailItemExpiredDate())
                                        : null);
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
                        if (dummyDetailLocation.getTblDetailItemExpiredDate()!= null) {
                            dummyDetailLocation.getTblDetailItemExpiredDate().setTblReturDetail(dummyDetailLocation.getTblDetail());
                            dummyDetailLocation.getTblDetailItemExpiredDate().setTblItemExpiredDate(new TblItemExpiredDate(dummyDetailLocation.getTblDetailItemExpiredDate().getTblItemExpiredDate()));
                        }
                        dummyDetailLocations.add(dummyDetailLocation);
                    }
                    switch (dataInputStatus) {
                        case 0: //insert
                            if (getService().insertDataReturWV2(dummySelectedData,
                                    dummyDetailLocations) != null) {
                                ClassMessage.showSucceedInsertingDataMessage("", null);
                                //refresh data from table & close form data retur
                                refreshDataTableRetur();
                                dataReturFormShowStatus.set(0.0);
                                //set unsaving data input -> 'false'
                                ClassSession.unSavingDataInput.set(false);
                            } else {
                                ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
                            }
                            break;
//                case 1:   //need maintenance
//                    if (getService().updateDataRetur(dummySelectedData,
//                            dummyDetailLocations)) {
//                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Updating data successed..!", null);
//                        //refresh data from table & close form data retur
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
//                            if (detailLocation.getTblDetail().getTblItem().getRefItemType().getTypeName().contains("Property")) {
//                                detailLocation.getTblDetail().setItemQuantity(new BigDecimal("1"));
//                            }
//                        }
//                        HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Updating data failed..!", getService().getErrorMessage());
//                    }
//                    break;
                        default:
                            break;
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage(errDataInput, null);
            }
        }
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
        if (cbpMI.getValue() == null) {
            dataInput = false;
            errDataInput += "No. Penerimaan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtReturNote.getText() == null
                || txtReturNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Keterangan Retur : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (detailLocations.isEmpty()) {
            dataInput = false;
            errDataInput += "Barang Retur : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }    

    private BigDecimal getMaxQuantityAvailable(TblReturDetail dataDetail) {
        BigDecimal result = getService().getDataMemorandumInvoiceDetailByIDMemorandumInvoiceAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getIdmi(),
                dataDetail.getTblSupplierItem().getIdrelation()).getItemQuantity();
        System.out.println("a : " + result);
        List<TblReturDetail> list = getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getIdmi(),
                dataDetail.getTblSupplierItem().getIdrelation());
        for (TblReturDetail data : list) {
            if (data.getTblRetur().getRefReturStatus().getIdstatus() == 1) {  //Disetujui = '1'
                System.out.println("b : " + data.getItemQuantity());
                result = result.subtract(data.getItemQuantity());
            }
        }
        return result;
    }

    private boolean checkDataInputDataReturForApprove() {
        boolean dataInput = true;
        errDataInput = "";
        TblPurchaseOrder dataPO = null;
        if (!detailLocations.isEmpty()) {
            dataPO = getService().getDataPurchaseOrder(detailLocations.get(0).getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo());
        }
        if (dataPO != null
                && dataPO.getRefPurchaseOrderStatus().getIdstatus() != 5) {  //Dipesan = '5'
            dataInput = false;
            errDataInput += "Data Purchase Order sudah tidak berlaku..! \n";
        }
        List<TblReturDetail> returDetails = getService().getAllDataReturDetailByIDRetur(selectedData.getIdretur());
        for (TblReturDetail returDetail : returDetails) {
            BigDecimal maxQuantityAvailable = getMaxQuantityAvailable(returDetail);
            if (returDetail.getItemQuantity()
                    .compareTo(maxQuantityAvailable) == 1) {
                dataInput = false;
                errDataInput += "Terdapat data barang yang jumlahnya melebih jumlah barang yang dapat di-retur..! \n";
                break;
            }
        }
        return dataInput;
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

        TableColumn<DetailLocation, String> location = new TableColumn("Lokasi");
        location.setMinWidth(150);
        location.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblLocationOfWarehouse() == null
                                ? "-"
                                : param.getValue().getTblLocationOfWarehouse().getWarehouseName(), param.getValue().tblLocationWarehouseProperty()));

        TableColumn<DetailLocation, String> codePO = new TableColumn("PO");
        codePO.setMinWidth(95);
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblDetail() == null
                        || param.getValue().getTblDetail().getTblMemorandumInvoice() == null
                        || param.getValue().getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder() == null
                                ? "-" : param.getValue().getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo(), param.getValue().getTblDetail().tblMemorandumInvoiceProperty()));

        TableColumn<DetailLocation, String> codeReceiving = new TableColumn("No. Penerimaan");
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

        TableColumn<DetailLocation, String> codeBarcode = new TableColumn("Barcode");
        codeBarcode.setMinWidth(120);
        codeBarcode.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblDetailPropertyBarcode() == null
                        || param.getValue().getTblDetailPropertyBarcode().getTblPropertyBarcode() == null
                                ? "-" : param.getValue().getTblDetailPropertyBarcode().getTblPropertyBarcode().getCodeBarcode(), param.getValue().tblDetailPropertyBarcodeProperty()));

        TableColumn<DetailLocation, String> expiredDate = new TableColumn("Tgl. Kadarluarsa");
        expiredDate.setMinWidth(120);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblDetailItemExpiredDate()== null
                        || param.getValue().getTblDetailItemExpiredDate().getTblItemExpiredDate()== null
                                ? "-" : ClassFormatter.dateFormate.format(param.getValue().getTblDetailItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate()), 
                        param.getValue().tblDetailItemExpiredDateProperty()));
        
        TableColumn<DetailLocation, String> cost = new TableColumn("Harga");
        cost.setMinWidth(120);
        cost.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemCost()), param.getValue().getTblDetail().itemCostProperty()));

        TableColumn<DetailLocation, String> discount = new TableColumn("Diskon");
        discount.setMinWidth(120);
        discount.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemDiscount()), param.getValue().getTblDetail().itemDiscountProperty()));

        TableColumn<DetailLocation, String> quantity = new TableColumn("Jumlah");
        quantity.setMinWidth(100);
        quantity.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "-"
                                : ClassFormatter.decimalFormat.cFormat(param.getValue().getTblDetail().getItemQuantity()), 
                        param.getValue().getTblDetail().itemQuantityProperty()));

        TableColumn<DetailLocation, String> tax = new TableColumn("Tax(%)");
        tax.setMinWidth(70);
        tax.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                                ? "0%"
                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemTaxPercentage().multiply(new BigDecimal("100"))) + "%", param.getValue().getTblDetail().itemTaxPercentageProperty()));

        TableColumn<DetailLocation, String> unitName = new TableColumn("Satuan");
        unitName.setMinWidth(120);
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<DetailLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblDetail() == null
                        || param.getValue().getTblDetail().getTblSupplierItem() == null
                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem() == null
                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getTblUnit() == null
                                ? "-"
                                : param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblDetail().tblSupplierItemProperty()));

        TableColumn<DetailLocation, Boolean> delButton = new TableColumn("Delete");
        delButton.setMinWidth(80);
        delButton.setCellFactory((TableColumn<DetailLocation, Boolean> param) -> new ButtonCellDelete<>());

        TableColumn<DetailLocation, String> totalCost = new TableColumn("Total Harga");
        totalCost.setMinWidth(140);
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

        if (dataInputStatus == 3 || dataInputStatus == 4 || dataInputStatus == 5) {   //just for show
            tableView.getColumns().addAll(itemTitle, codeBarcode, expiredDate, quantityInfo, unitName, location);
        } else {
            tableView.getColumns().addAll(itemTitle, codeBarcode, expiredDate, quantity, unitName, location);
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
//                result += dataDetailLocation.getTblDetail().getTblSupplierItem().getTblItem().getTblItemType().getTypeName() + " "
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
        List<TblLocationOfWarehouse> list = getService().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    public class DetailLocation {

        private final ObjectProperty<TblReturDetail> tblDetail = new SimpleObjectProperty<>();

        private final ObjectProperty<TblLocationOfWarehouse> tblLocationOfWarehouse = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReturDetailPropertyBarcode> tblDetailPropertyBarcode = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReturDetailItemExpiredDate> tblDetailItemExpiredDate = new SimpleObjectProperty<>();

        //FOR VIEW DETAIL (detailInputStatus == 3)
        private final ObjectProperty<java.util.Date> detailExpiredDate = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> detailQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));

        public DetailLocation(TblReturDetail tblDetail,
                TblLocationOfWarehouse tblLocationOfWarehouse,
                TblReturDetailPropertyBarcode tblDetailPropertyBarcode,
                TblReturDetailItemExpiredDate tblDetailItemExpiredDate) {
            //data detail
            this.tblDetail.set(tblDetail);

            //data location(warehouse)
            this.tblLocationOfWarehouse.set(tblLocationOfWarehouse);
            //data rd - pb list
            this.tblDetailPropertyBarcode.set(tblDetailPropertyBarcode);
            //data rd - ied list
            this.tblDetailItemExpiredDate.set(tblDetailItemExpiredDate);
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

        public ObjectProperty<TblReturDetailItemExpiredDate> tblDetailItemExpiredDateProperty() {
            return tblDetailItemExpiredDate;
        }

        public TblReturDetailItemExpiredDate getTblDetailItemExpiredDate() {
            return tblDetailItemExpiredDateProperty().get();
        }

        public void setTblDetailItemExpiredDate(TblReturDetailItemExpiredDate detailIED) {
            tblDetailItemExpiredDateProperty().set(detailIED);
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

    public DetailLocation generateDataDetailLocation(TblReturDetail dataDetail,
            TblReturDetailPropertyBarcode dataDetailPropertyBarcode, 
            TblReturDetailItemExpiredDate dataDetailItemExpiredDate) {
        DetailLocation data = new DetailLocation(dataDetail,
                (dataDetail != null && dataDetail.getTblLocation() != null)
                        ? getService().getDataWarehouseByIDLocation(dataDetail.getTblLocation().getIdlocation())
                        : null,
                dataDetailPropertyBarcode,
                dataDetailItemExpiredDate);
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
        if (cbpMI.getValue() != null) {
//        if (selectedData.getTblSupplier() != null) {
            dataInputDetailStatus = 0;
            TblReturDetail dataReturDetail = new TblReturDetail();
            dataReturDetail.setTblMemorandumInvoice(cbpMI.getValue());
            dataReturDetail.setItemCost(new BigDecimal("0"));
            dataReturDetail.setItemQuantity(new BigDecimal("0"));
            dataReturDetail.setItemDiscount(new BigDecimal("0"));
            dataReturDetail.setItemTaxPercentage(new BigDecimal("0"));
            selectedDataDetailLocation = new DetailLocation(dataReturDetail, null, null, null);
            selectedDataDetailLocation.getTblDetail().setTblRetur(selectedData);
            //open form data - detail
            showDataDetailItemDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK ADA DATA PENERIMAAN YANG DIPILIH", "Silahkan pilih data penerimaan terlebih dahulu..!", null);
        }
    }

    public void dataDetailUpdateHandle() {
        if (tableReturDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputDetailStatus = 1;
            DetailLocation dataDetailLocation = ((DetailLocation) tableReturDetail.getTableView().getSelectionModel().getSelectedItem());
            selectedDataDetailLocation = new DetailLocation(
                    new TblReturDetail(dataDetailLocation.getTblDetail()),
                    new TblLocationOfWarehouse(dataDetailLocation.getTblLocationOfWarehouse()),
                    dataDetailLocation.getTblDetailPropertyBarcode() != null
                            ? new TblReturDetailPropertyBarcode(dataDetailLocation.getTblDetailPropertyBarcode())
                            : null,
                    dataDetailLocation.getTblDetailItemExpiredDate()!= null
                            ? new TblReturDetailItemExpiredDate(dataDetailLocation.getTblDetailItemExpiredDate())
                            : null);
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
            //data item expired date
            if (selectedDataDetailLocation.getTblDetailItemExpiredDate()!= null) {
                selectedDataDetailLocation.getTblDetailItemExpiredDate().setTblReturDetail(selectedDataDetailLocation.getTblDetail());
                selectedDataDetailLocation.getTblDetailItemExpiredDate().setTblItemExpiredDate(new TblItemExpiredDate(selectedDataDetailLocation.getTblDetailItemExpiredDate().getTblItemExpiredDate()));
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
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_storing/ReturDetailLocationWDialog.fxml"));

            ReturDetailLocationWController controller = new ReturDetailLocationWController(this);
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
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FReturManager fReturManager;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        if(fReturManager == null){
            fReturManager = new FReturManagerImpl();
        }
        
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

    public ReturWController(){
        
    }
    
    public ReturWController(FeatureReturWController parentController) {
        this.parentController = parentController;
        this.fReturManager = parentController.getFReturManager();
    }

    private FeatureReturWController parentController;

    public FReturManager getService() {
        return fReturManager;
    }

}
