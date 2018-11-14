/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payment_x;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_purchase_order.FeaturePurchaseOrderController;
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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
public class PurchaseOrderPaymentController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPurchaseOrderPayment;

    private DoubleProperty dataPurchaseOrderPaymentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPurchaseOrderPaymentLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPurchaseOrderPaymentSplitpane() {
        spDataPurchaseOrderPayment.setDividerPositions(1);

        dataPurchaseOrderPaymentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPurchaseOrderPaymentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPurchaseOrderPayment.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPurchaseOrderPayment.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPurchaseOrderPaymentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataPurchaseOrderPaymentLayout.setDisable(false);
            tableDataPurchaseOrderPaymentLayoutDisableLayer.setDisable(true);
            tableDataPurchaseOrderPaymentLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPurchaseOrderPaymentLayout.setDisable(false);
                    tableDataPurchaseOrderPaymentLayoutDisableLayer.setDisable(true);
                    tableDataPurchaseOrderPaymentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPurchaseOrderPaymentLayout.setDisable(true);
                    tableDataPurchaseOrderPaymentLayoutDisableLayer.setDisable(false);
                    tableDataPurchaseOrderPaymentLayoutDisableLayer.toFront();
                }
            }
        });

        dataPurchaseOrderPaymentFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPurchaseOrderPaymentLayout;

    private ClassTableWithControl tableDataPurchaseOrderPayment;

    private void initTableDataPurchaseOrderPayment() {
        //set table
        setTableDataPurchaseOrderPayment();
        //set control
        setTableControlDataPurchaseOrderPayment();
        //set table-control to content-view
        tableDataPurchaseOrderPaymentLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPurchaseOrderPayment, 15.0);
        AnchorPane.setLeftAnchor(tableDataPurchaseOrderPayment, 15.0);
        AnchorPane.setRightAnchor(tableDataPurchaseOrderPayment, 15.0);
        AnchorPane.setTopAnchor(tableDataPurchaseOrderPayment, 15.0);
        tableDataPurchaseOrderPaymentLayout.getChildren().add(tableDataPurchaseOrderPayment);
    }

    private void setTableDataPurchaseOrderPayment() {
        TableView<TblPurchaseOrder> tableView = new TableView();

        TableColumn<TblPurchaseOrder, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodePo(),
                        param.getValue().codePoProperty()));
        codePO.setMinWidth(120);

        TableColumn<TblPurchaseOrder, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSupplier().getSupplierName(),
                        param.getValue().tblSupplierProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> totalBill = new TableColumn("Total Tagihan (PO)");
        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())),
                        param.getValue().idpoProperty()));
        totalBill.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> totalPayment = new TableColumn("Total Pembayaran");
        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())),
                        param.getValue().idpoProperty()));
        totalPayment.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idpoProperty()));
        totalRestOfBill.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> transactionDate = new TableColumn("Tanggal Transaksi");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getPodate()),
                        param.getValue().podateProperty()));
        transactionDate.setMinWidth(160);

        TableColumn<TblPurchaseOrder, String> paymentStatus = new TableColumn("Status");
        paymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelPayable() != null
                                ? param.getValue().getTblHotelPayable().getRefFinanceTransactionStatus().getStatusName()
                                : "Belum Dibayar", param.getValue().tblHotelPayableProperty()));
        paymentStatus.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeInvoice.setMinWidth(120);

        tableView.getColumns().addAll(codePO, supplierName, totalBill, totalPayment, totalRestOfBill, transactionDate, paymentStatus, codeInvoice);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataHotelPayablePurchaseOrder()));

        tableView.setRowFactory(tv -> {
            TableRow<TblPurchaseOrder> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPaymentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataPaymentShowHandle();
                        }
                    }
                } else {
                    if (!row.isEmpty()) {
                        if (isShowStatus.get()) {
                            dataPaymentShowHandle();
                        }
                    }
                }
            });
            return row;
        });

        tableDataPurchaseOrderPayment = new ClassTableWithControl(tableView);
    }

    private BigDecimal calculationTotalCost(TblPurchaseOrderDetail dataDetail) {
        return (dataDetail.getItemCost().subtract(dataDetail.getItemDiscount())).multiply(dataDetail.getItemQuantity());
    }

    private BigDecimal calculationSubTotal(TblPurchaseOrder dataPO) {
        BigDecimal result = new BigDecimal("0");
        if (dataPO != null) {
            List<TblPurchaseOrderDetail> list = parentController.getFPurchaseOrderManager().getAllDataPurchaseOrderDetailByIDPurchaseOrder(dataPO.getIdpo());
            for (TblPurchaseOrderDetail data : list) {
                result = result.add(calculationTotalCost(data));
            }
        }
        return result;
    }

    private BigDecimal calculationTotal(TblPurchaseOrder dataPO) {
        return (((new BigDecimal("1")).add(dataPO.getTaxPecentage()))
                .multiply(calculationSubTotal(dataPO).subtract(dataPO.getNominalDiscount())))
                .add(dataPO.getDeliveryCost());
    }

    public BigDecimal calculationTotalBill(TblPurchaseOrder purchaseOrder) {
        BigDecimal result = new BigDecimal("0");
        if (purchaseOrder != null
                && purchaseOrder.getTblHotelPayable() != null) {
//            if(dataHPPO.getTblHotelPayable().getIdhotelPayable() == 0L){
//                result = calculationTotal(dataHPPO.getTblPurchaseOrder());
//            }else{
            result = purchaseOrder.getTblHotelPayable().getHotelPayableNominal();
//            }
        }
        return result;
    }

    public BigDecimal calculationTotalPayment(TblPurchaseOrder purchaseOrder) {
        BigDecimal result = new BigDecimal("0");
        if (purchaseOrder != null
                && purchaseOrder.getTblHotelPayable() != null) {
//            List<TblHotelFinanceTransaction> list = parentController.getFPurchaseOrderManager().getAllDataHotelFinanceTransactionByIDHotelPayable(purchaseOrder.getTblHotelPayable().getIdhotelPayable());
//            for (TblHotelFinanceTransaction data : list) {
//                result = result.add(data.getTransactionNominal());
//            }
        }
        return result;
    }

    public BigDecimal calculationTotalRestOfBill(TblPurchaseOrder purchaseOrder) {
        return calculationTotalBill(purchaseOrder).subtract(calculationTotalPayment(purchaseOrder));
    }

    public String getCodeInvoice(TblHotelPayable dataHotelPayable) {
        String result = "-";
        if (dataHotelPayable != null
                && dataHotelPayable.getTblHotelInvoice() != null) {
            dataHotelPayable.setTblHotelInvoice(getService().getDataHotelInvoice(dataHotelPayable.getTblHotelInvoice().getIdhotelInvoice()));
            result = dataHotelPayable.getTblHotelInvoice().getCodeHotelInvoice();
        }
        return result;
    }

    public TblHotelInvoice getDataHotelInvoice(TblHotelPayable dataHotelPayable) {
        if (dataHotelPayable != null
                && dataHotelPayable.getIdhotelPayable() != 0L
                && dataHotelPayable.getTblHotelInvoice() != null) {
            TblHotelInvoice dataHotelInvoice = parentController.getFPurchaseOrderManager().getDataHotelInvoice(dataHotelPayable.getTblHotelInvoice().getIdhotelInvoice());
            if (dataHotelInvoice != null) {
                //data supplier
                dataHotelInvoice.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(dataHotelInvoice.getTblSupplier().getIdsupplier()));
                //data hotel invoice type
                dataHotelInvoice.setRefHotelInvoiceType(parentController.getFPurchaseOrderManager().getDataHotelInvoiceType(dataHotelInvoice.getRefHotelInvoiceType().getIdtype()));
                return dataHotelInvoice;
            }
        }
        return null;
    }

    private void setTableControlDataPurchaseOrderPayment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Transaksi Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataPurchaseOrderPaymentCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Set Invoice");
            buttonControl.setOnMouseClicked((e) -> {
                //listener set invoice
                dataPurchaseOrderPaymentSetInvoiceHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataPurchaseOrderPaymentPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataPurchaseOrderPayment.addButtonControl(buttonControls);
    }

    private List<TblPurchaseOrder> loadAllDataHotelPayablePurchaseOrder() {
        List<TblPurchaseOrder> list = new ArrayList<>();
        //data po
        List<TblPurchaseOrder> poList = parentController.getFPurchaseOrderManager().getAllDataPurchaseOrder();
        for (int i = poList.size() - 1; i > -1; i--) {
            if (poList.get(i).getRefPurchaseOrderStatus().getIdstatus() != 5) {    //status po != 5 ('Order')
                poList.remove(i);
            }
        }
        for (TblPurchaseOrder poData : poList) {
            if (poData.getTblHotelPayable() != null) {   //data founded (load)
                //data hotel payable
                poData.setTblHotelPayable(parentController.getFPurchaseOrderManager().getDataHotelPayable(poData.getTblHotelPayable().getIdhotelPayable()));
                //data hotel payable type
                poData.getTblHotelPayable().setRefHotelPayableType(parentController.getFPurchaseOrderManager().getDataHotelPayableType(poData.getTblHotelPayable().getRefHotelPayableType().getIdtype()));
                //data finance transaction status
                poData.getTblHotelPayable().setRefFinanceTransactionStatus(parentController.getFPurchaseOrderManager().getDataFinanceTransactionStatus(poData.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus()));
                //data supplier
                poData.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(poData.getTblSupplier().getIdsupplier()));
            } else {  //data not found (create)
                //data hotel payable
                TblHotelPayable dataHP = new TblHotelPayable();
                dataHP.setHotelPayableNominal(calculationTotal(poData));
                //data hotel payable type
                dataHP.setRefHotelPayableType(parentController.getFPurchaseOrderManager().getDataHotelPayableType(1));   //Purchase Order = '1'
                //data finance transaction status
                dataHP.setRefFinanceTransactionStatus(parentController.getFPurchaseOrderManager().getDataFinanceTransactionStatus(0));    //Belum Dibayar = '0'
                //data supplier
                poData.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(poData.getTblSupplier().getIdsupplier()));
                //data purchase order (hotel payable)
                poData.setTblHotelPayable(dataHP);
            }
            list.add(poData);
        }
        //remove data isn't used
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 3) {   //Sudah Tidak Berlaku = '3'
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
    private GridPane gpFormDataPurchaseOrderPayment;

    @FXML
    private ScrollPane spFormDataPurchaseOrderPayment;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblPurchaseOrder selectedDataPurchaseOrder;

    public TblHotelFinanceTransaction selectedData;

    public TblHotelInvoice selectedDataHotelInvoice;

    private void initFormDataPurchaseOrderPayment() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPurchaseOrderPayment.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPurchaseOrderPayment.setOnScroll((ScrollEvent scroll) -> {
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

    }

    /**
     * TABLE DATA PURCHASE ORDER PAYMENT
     */
    private void setSelectedDataToInputForm(TblPurchaseOrder dataPO) {
        initTableDataDetail(dataPO);

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        gpFormDataPurchaseOrderPayment.setDisable(dataInputStatus == 3);

//        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    @FXML
    private AnchorPane ancDetailPaymentLayout;

    public TableView<TblHotelFinanceTransaction> tableDataDetail;

    private void initTableDataDetail(TblPurchaseOrder dataPO) {
        //set table
        setTableDataDetail(dataPO);
        //set table to content-view
        ancDetailPaymentLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailPaymentLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail(TblPurchaseOrder dataPO) {
        tableDataDetail = new TableView();

        TableColumn<TblHotelFinanceTransaction, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory(cellData -> cellData.getValue().codeTransactionProperty());
        codePayment.setMinWidth(120);

        TableColumn<TblHotelFinanceTransaction, String> paymentNominal = new TableColumn("Nominal Pembayaran");
        paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionNominal()), param.getValue().transactionNominalProperty()));
        paymentNominal.setMinWidth(160);

        TableColumn<TblHotelFinanceTransaction, String> paymentRoundingValue = new TableColumn("Nominal Pembulatan");
        paymentRoundingValue.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionRoundingValue()), 
                        param.getValue().transactionRoundingValueProperty()));
        paymentRoundingValue.setMinWidth(160);
        
        TableColumn<TblHotelFinanceTransaction, String> paymentDate = new TableColumn("Tanggal Pembayaran");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCreateDate()), param.getValue().createDateProperty()));
        paymentDate.setMinWidth(160);

        tableDataDetail.getColumns().addAll(codePayment, paymentDate, paymentNominal, paymentRoundingValue);
        tableDataDetail.setItems(FXCollections.observableArrayList(loadAllDataHotelFinanceTransaction(dataPO)));
    }

    private List<TblHotelFinanceTransaction> loadAllDataHotelFinanceTransaction(TblPurchaseOrder dataPO) {
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
//        if (dataPO != null
//                && dataPO.getTblHotelPayable() != null) {
//            list = parentController.getFPurchaseOrderManager().getAllDataHotelFinanceTransactionByIDHotelPayable(dataPO.getTblHotelPayable().getIdhotelPayable());
//            for (TblHotelFinanceTransaction data : list) {
//                //data hotel payable
//                data.setTblHotelPayable(parentController.getFPurchaseOrderManager().getDataHotelPayable(data.getTblHotelPayable().getIdhotelPayable()));
//                //data finance transaction type
//                data.setRefFinanceTransactionType(parentController.getFPurchaseOrderManager().getDataFinanceTransactionType(data.getRefFinanceTransactionType().getIdtype()));
//            }
//        }
        return list;
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private int dataInputStatus = 0;

    public Stage dialogStageDetal;

    private void dataPurchaseOrderPaymentCreateHandle() {
//        if (tableDataPurchaseOrderPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            selectedDataPurchaseOrder = (TblPurchaseOrder) tableDataPurchaseOrderPayment.getTableView().getSelectionModel().getSelectedItem();
//            if (selectedDataPurchaseOrder.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() != 2) {   //2 = 'Lunas'
//                if (selectedDataPurchaseOrder.getTblHotelPayable() != null) {
//                    selectedDataHotelInvoice = getDataHotelInvoice(selectedDataPurchaseOrder.getTblHotelPayable());
//                    if (selectedDataHotelInvoice == null
//                            || selectedDataHotelInvoice.getCodeHotelInvoice() == null
//                            || selectedDataHotelInvoice.getCodeHotelInvoice().equals("")) {
//                        HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan masukan Nomor Invoice terlebih dahulu..!", null);
//                    } else {
//                        selectedData = new TblHotelFinanceTransaction();
//                        selectedData.setTblHotelPayable(selectedDataPurchaseOrder.getTblHotelPayable());
//                        selectedData.setTransactionNominal(new BigDecimal("0"));
//                        selectedData.setRefFinanceTransactionType(parentController.getFPurchaseOrderManager().getDataFinanceTransactionType(0));    //payable = '0'
//                        //open form data - detail (po - payment)
//                        showDataDetailDialog();
//                    }
//                } else {
//                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan masukan Nomor Invoice terlebih dahulu..!", null);
//                }
//            } else {
//                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Data Purchase Order telah dibayar (lunas)..!", null);
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPaymentShowHandle() {
        if (tableDataPurchaseOrderPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedDataPurchaseOrder = (TblPurchaseOrder) tableDataPurchaseOrderPayment.getTableView().getSelectionModel().getSelectedItem();
//            selectedDataHotelPayablePurchaseOrder = parentController.getFPurchaseOrderManager().getDataHotelPayablePurchaseOrder(((TblHotelPayablePurchaseOrder) tableDataPurchaseOrderPayment.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
//            selectedDataHotelPayablePurchaseOrder.setTblHotelPayable(parentController.getFPurchaseOrderManager().getDataHotelPayable(selectedDataHotelPayablePurchaseOrder.getTblHotelPayable().getIdhotelPayable()));
//            selectedDataHotelPayablePurchaseOrder.setTblPurchaseOrder(parentController.getFPurchaseOrderManager().getDataPurchaseOrder(selectedDataHotelPayablePurchaseOrder.getTblPurchaseOrder().getIdpo()));
            setSelectedDataToInputForm(selectedDataPurchaseOrder);
            dataPurchaseOrderPaymentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPaymentUnshowHandle() {
        tableDataPurchaseOrderPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelPayablePurchaseOrder()));
        dataPurchaseOrderPaymentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPurchaseOrderPaymentSetInvoiceHandle() {
        if (tableDataPurchaseOrderPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedDataPurchaseOrder = (TblPurchaseOrder) tableDataPurchaseOrderPayment.getTableView().getSelectionModel().getSelectedItem();
            selectedDataHotelInvoice = getDataHotelInvoice(selectedDataPurchaseOrder.getTblHotelPayable());
            if (selectedDataHotelInvoice == null) {
                selectedDataHotelInvoice = new TblHotelInvoice();
                selectedDataHotelInvoice.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(selectedDataPurchaseOrder.getTblSupplier().getIdsupplier()));
                selectedDataHotelInvoice.setRefHotelInvoiceType(parentController.getFPurchaseOrderManager().getDataHotelInvoiceType(0));    //Payable = '0' 
            }
            //open form data - detail (po - payment) : set invoice
            showDataDetailSetInvoiceDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataPurchaseOrderPaymentPrintHandle() {

    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payment/PurchaseOrderPaymentDetailDialog.fxml"));

            PurchaseOrderPaymentDetailController controller = new PurchaseOrderPaymentDetailController(this);
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

    private void showDataDetailSetInvoiceDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payment/PurchaseOrderPaymentDetailSetInvoiceDialog.fxml"));

            PurchaseOrderPaymentDetailSetInvoiceController controller = new PurchaseOrderPaymentDetailSetInvoiceController(this);
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
        //data table
        tableDataPurchaseOrderPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelPayablePurchaseOrder()));
        //data table detail
        setSelectedDataToInputForm(null);
        //close form table detail
        dataPurchaseOrderPaymentFormShowStatus.set(0.0);
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
        setDataPurchaseOrderPaymentSplitpane();

        //init table
        initTableDataPurchaseOrderPayment();

        //init form
        initFormDataPurchaseOrderPayment();

        spDataPurchaseOrderPayment.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPurchaseOrderPaymentFormShowStatus.set(0.0);
        });
    }

    public PurchaseOrderPaymentController(FeaturePurchaseOrderController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePurchaseOrderController parentController;

    public FPurchaseOrderManager getService() {
        return parentController.getFPurchaseOrderManager();
    }

}
