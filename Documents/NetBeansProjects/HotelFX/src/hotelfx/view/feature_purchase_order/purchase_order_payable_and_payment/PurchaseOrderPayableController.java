/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.view.DashboardController;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
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
public class PurchaseOrderPayableController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPOPayable;

    private DoubleProperty dataPOPayableFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPOPayableLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPOPayableSplitpane() {
        spDataPOPayable.setDividerPositions(1);

        dataPOPayableFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPOPayableFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPOPayable.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPOPayable.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPOPayableFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataPOPayableLayout.setDisable(false);
            tableDataPOPayableLayoutDisableLayer.setDisable(true);
            tableDataPOPayableLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPOPayableLayout.setDisable(false);
                    tableDataPOPayableLayoutDisableLayer.setDisable(true);
                    tableDataPOPayableLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPOPayableLayout.setDisable(true);
                    tableDataPOPayableLayoutDisableLayer.setDisable(false);
                    tableDataPOPayableLayoutDisableLayer.toFront();
                }
            }
        });

        dataPOPayableFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPOPayableLayout;

    private ClassFilteringTable<TblSupplier> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataPOPayable;

    private void initTableDataPOPayable() {
        //set table
        setTableDataPOPayable();
        //set control
        setTableControlDataPOPayable();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPOPayable, 15.0);
        AnchorPane.setLeftAnchor(tableDataPOPayable, 15.0);
        AnchorPane.setRightAnchor(tableDataPOPayable, 15.0);
        AnchorPane.setTopAnchor(tableDataPOPayable, 15.0);
        ancBodyLayout.getChildren().add(tableDataPOPayable);
    }

    private void setTableDataPOPayable() {
        TableView<TblSupplier> tableView = new TableView();

        TableColumn<TblSupplier, String> codeSupplier = new TableColumn("ID");
        codeSupplier.setCellValueFactory(cellData -> cellData.getValue().codeSupplierProperty());
        codeSupplier.setMinWidth(120);

        TableColumn<TblSupplier, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        supplierName.setMinWidth(140);

        TableColumn<TblSupplier, String> picName = new TableColumn("Nama");
        picName.setCellValueFactory(cellData -> cellData.getValue().picnameProperty());
        picName.setMinWidth(140);

        TableColumn<TblSupplier, String> picPhoneNumber = new TableColumn("Telepon");
        picPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().picphoneNumberProperty());
        picPhoneNumber.setMinWidth(120);

        TableColumn<TblSupplier, String> picEmailAddress = new TableColumn("Email");
        picEmailAddress.setCellValueFactory(cellData -> cellData.getValue().picemailAddressProperty());
        picEmailAddress.setMinWidth(200);

        TableColumn<TblSupplier, String> picTitle = new TableColumn("PIC");
        picTitle.getColumns().addAll(picName, picPhoneNumber, picEmailAddress);

        TableColumn<TblSupplier, String> totalHotelPayable = new TableColumn("Total Tagihan");
        totalHotelPayable.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplier, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(getTotalHotelPayable(param.getValue())),
                        param.getValue().idsupplierProperty()));
        totalHotelPayable.setMinWidth(140);

        TableColumn<TblSupplier, String> minDueDate = new TableColumn("Tgl. Estimasi Bayar\n"
                + "(paling dekat)");
        minDueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplier, String> param)
                -> Bindings.createStringBinding(() -> getMinDueDate(param.getValue()),
                        param.getValue().idsupplierProperty()));
        minDueDate.setMinWidth(160);

        tableView.getColumns().addAll(codeSupplier, supplierName, picTitle, totalHotelPayable, minDueDate);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataSupplier()));

        tableView.setRowFactory(tv -> {
            TableRow<TblSupplier> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataHotelPayableUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataHotelPayableUpdateHandle();
                            } else {
                                dataHotelPayableShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataHotelPayableUpdateHandle();
//                            } else {
//                                dataHotelPayableShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataPOPayable = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblSupplier.class,
                tableDataPOPayable.getTableView(),
                tableDataPOPayable.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private BigDecimal getTotalHotelPayable(TblSupplier supplier) {
        BigDecimal result = new BigDecimal("0");
        if (supplier != null) {
            List<TblPurchaseOrder> poList = loadAllDataPurchaseOrder(supplier);
            for (TblPurchaseOrder poData : poList) {
                if (poData.getTblHotelPayable() != null) {
                    result = result.add(poData.getTblHotelPayable().getHotelPayableNominal());
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(poData.getTblHotelPayable().getIdhotelPayable());
                    for (TblHotelFinanceTransactionHotelPayable data : list) {
                        if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                            result = result.add(data.getNominalTransaction());
                        } else {
                            result = result.subtract(data.getNominalTransaction());
                        }
                    }
                } else {
                    result = result.add(calculationTotal(poData));
                }
            }
        }
        return result;
    }

    private BigDecimal getTotalHotelPayableJustHaveCodeInvoiceForPayment(TblSupplier supplier) {
        BigDecimal result = new BigDecimal("0");
        if (supplier != null) {
            List<TblPurchaseOrder> poList = loadAllDataPurchaseOrder(supplier);
            for (TblPurchaseOrder poData : poList) {
                if (poData.getTblHotelPayable() != null) {
                    BigDecimal rob = poData.getTblHotelPayable().getHotelPayableNominal();
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(poData.getTblHotelPayable().getIdhotelPayable());
                    for (TblHotelFinanceTransactionHotelPayable data : list) {
                        if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                            rob = rob.add(data.getNominalTransaction());
                        } else {
                            rob = rob.subtract(data.getNominalTransaction());
                        }
                    }
                    if (rob.compareTo(new BigDecimal("0")) == 1) {
                        result = result.add(rob);
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal getTotalHotelPayableJustHaveCodeInvoiceForReturn(TblSupplier supplier) {
        BigDecimal result = new BigDecimal("0");
        if (supplier != null) {
            List<TblPurchaseOrder> poList = loadAllDataPurchaseOrder(supplier);
            for (TblPurchaseOrder poData : poList) {
                if (poData.getTblHotelPayable() != null) {
                    BigDecimal rob = poData.getTblHotelPayable().getHotelPayableNominal();
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(poData.getTblHotelPayable().getIdhotelPayable());
                    for (TblHotelFinanceTransactionHotelPayable data : list) {
                        if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                            rob = rob.add(data.getNominalTransaction());
                        } else {
                            rob = rob.subtract(data.getNominalTransaction());
                        }
                    }
                    if (rob.compareTo(new BigDecimal("0")) == -1) {
                        result = result.add(rob);
                    }
                }
            }
        }
        return result;
    }

    private String getMinDueDate(TblSupplier supplier) {
        if (supplier != null) {
            LocalDate localDate = null;
            List<TblPurchaseOrder> poList = loadAllDataPurchaseOrder(supplier);
            for (TblPurchaseOrder poData : poList) {
                if (poData.getTblHotelPayable() != null
                        && (poData.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 0 //Belum Dibayar = '0'
                        || poData.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 1) //Dibayar Sebagian = '1'
                        && poData.getTblHotelPayable().getTblHotelInvoice() != null
                        && poData.getTblHotelPayable().getTblHotelInvoice().getDueDate() != null) {
                    LocalDate countDate = LocalDate.of(poData.getTblHotelPayable().getTblHotelInvoice().getDueDate().getYear() + 1900,
                            poData.getTblHotelPayable().getTblHotelInvoice().getDueDate().getMonth() + 1,
                            poData.getTblHotelPayable().getTblHotelInvoice().getDueDate().getDate());
                    if (localDate == null
                            || countDate.isBefore(localDate)) {
                        localDate = countDate;
                    }
                }
            }
            if (localDate != null) {
                return ClassFormatter.dateFormate.format(Date.valueOf(localDate));
            }
        }
        return "-";
    }

    private List<TblSupplier> loadAllDataSupplier() {
        List<TblSupplier> list = parentController.getService().getAllDataSupplier();
        return list;
    }

    private BigDecimal calculationTotalCost(TblPurchaseOrderDetail dataDetail) {
        return (dataDetail.getItemCost().subtract(dataDetail.getItemDiscount())).multiply(dataDetail.getItemQuantity());
    }

    private BigDecimal calculationSubTotal(TblPurchaseOrder dataPO) {
        BigDecimal result = new BigDecimal("0");
        if (dataPO != null) {
            List<TblPurchaseOrderDetail> list = parentController.getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(dataPO.getIdpo());
            for (TblPurchaseOrderDetail data : list) {
                result = result.add(calculationTotalCost(data));
            }
        }
        return result;
    }

    public BigDecimal calculationTotal(TblPurchaseOrder dataPO) {
        return (((new BigDecimal("1")).add(dataPO.getTaxPecentage()))
                .multiply(calculationSubTotal(dataPO).subtract(dataPO.getNominalDiscount())))
                .add(dataPO.getDeliveryCost());
    }

    public BigDecimal calculationTotalBill(TblPurchaseOrder purchaseOrder) {
        BigDecimal result = new BigDecimal("0");
        if (purchaseOrder != null) {
            if (purchaseOrder.getTblHotelPayable() != null) {
//            if(dataHPPO.getTblHotelPayable().getIdhotelPayable() == 0L){
//                result = calculationTotal(dataHPPO.getTblPurchaseOrder());
//            }else{
                result = purchaseOrder.getTblHotelPayable().getHotelPayableNominal();
//            }
            } else {
                result = calculationTotal(purchaseOrder);
            }
        }
        return result;
    }

    public BigDecimal calculationTotalPayment(TblPurchaseOrder purchaseOrder) {
        BigDecimal result = new BigDecimal("0");
        if (purchaseOrder != null
                && purchaseOrder.getTblHotelPayable() != null) {
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(purchaseOrder.getTblHotelPayable().getIdhotelPayable());
            for (TblHotelFinanceTransactionHotelPayable data : list) {
                if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                    result = result.subtract(data.getNominalTransaction());
                } else {
                    result = result.add(data.getNominalTransaction());
                }
            }
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

    private void setTableControlDataPOPayable() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Set Invoice / Buat Transaksi Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataHotelPayableUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPOPayable.addButtonControl(buttonControls);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

//    @FXML
//    private GridPane gpFormDataPOPayable;
    @FXML
    private ScrollPane spFormDataPOPayable;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXTextField txtSupplier;

    @FXML
    private JFXTextField txtPICName;

    @FXML
    private JFXTextField txtPICPhoneNumber;

    @FXML
    private Label lblTotalHotelPayable;

    @FXML
    private AnchorPane ancTableDetailLayout;

//    @FXML
//    private JFXTextField txtCodeInvoice;
//
//    @FXML
//    private JFXTextField txtSubject;
//
//    @FXML
//    private JFXDatePicker dtpIssueDate;
//
//    @FXML
//    private JFXDatePicker dtpDueDate;
//
//    @FXML
//    private JFXTextArea txtTerm;
//    @FXML
//    private JFXButton btnSave;
//    @FXML
//    private JFXButton btnPrint;
//    @FXML
//    private AnchorPane ancTableHotelFinanceTransaction;
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblSupplier selectedData;

    public TblPurchaseOrder selectedDataPO;

    private void initFormDataPOPayable() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPOPayable.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
//        gpFormDataPOPayable.setOnScroll((ScrollEvent scroll) -> {
//            isFormScroll.set(true);
//
//            scrollCounter++;
//
//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    Platform.runLater(() -> {
//                        if (scrollCounter == 1) {
//                            //scroll end..!
//                            isFormScroll.set(false);
//                        }
//                        scrollCounter--;
//                    });
//                } catch (Exception e) {
//                    System.out.println("err " + e.getMessage());
//                }
//
//            });
//            thread.setDaemon(true);
//            thread.start();
//        });
        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            //back handle
            dataPOPayableBackHandle();
        });

//        btnSave.setTooltip(new Tooltip("Simpan (data invoice)"));
//        btnSave.setOnAction((e) -> {
//            //save invoice handle
//            dataInvoiceSaveHandle();
//        });
//        btnPrint.setTooltip(new Tooltip("Cetak"));
//        btnPrint.setOnAction((e) -> {
//            //print invoice handle
//            dataInvoicePrintHandle();
//        });
//        btnPrint.setVisible(false);
//        //data invoice - event
//        txtCodeInvoice.textProperty().addListener((obs, oldVal, newVal) -> {
//            //set unsaving data input -> 'true'
//            ClassSession.unSavingDataInput.set(true);
//        });
//        txtSubject.textProperty().addListener((obs, oldVal, newVal) -> {
//            //set unsaving data input -> 'true'
//            ClassSession.unSavingDataInput.set(true);
//        });
//        dtpIssueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
//            //set unsaving data input -> 'true'
//            ClassSession.unSavingDataInput.set(true);
//        });
//        dtpDueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
//            //set unsaving data input -> 'true'
//            ClassSession.unSavingDataInput.set(true);
//        });
//        txtTerm.textProperty().addListener((obs, oldVal, newVal) -> {
//            //set unsaving data input -> 'true'
//            ClassSession.unSavingDataInput.set(true);
//        });
    }

    /**
     * TABLE DATA PURCHASE ORDER PAYABLE
     */
    private final PseudoClass notPaidPseudoClass = PseudoClass.getPseudoClass("notpaid");

    private final PseudoClass overPaymentPseudoClass = PseudoClass.getPseudoClass("overpayment");

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodeSupplier() + " - " + selectedData.getSupplierName());

        txtSupplier.setText(selectedData.getCodeSupplier() + " - " + selectedData.getSupplierName());
        txtPICName.setText(selectedData.getPicname());
        txtPICPhoneNumber.setText(selectedData.getPicphoneNumber());

        refreshDataTotalHotelPayable();

        initTableDataDetail(selectedData);

//        //set selected data input form detail
//        setSelectedDataToInputFormDetail(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1
//                ? ((TblPurchaseOrder) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelPayable()
//                : null);
        setSelectedDataToInputFormFunctionalComponent();
    }

    public void refreshDataTotalHotelPayable() {
        lblTotalHotelPayable.setText(ClassFormatter.currencyFormat.format(getTotalHotelPayable(selectedData)));
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ancTableDetailLayout.setDisable(dataInputStatus == 3);

//        gpFormDataPOPayable.setDisable(dataInputStatus == 3);
//        btnSave.setVisible(dataInputStatus != 3);
//        btnPrint.setDisable(false);
    }

    public ClassTableWithControl tableDataDetail;

    private void initTableDataDetail(TblSupplier dataSupplier) {
        //set table
        setTableDataDetail(dataSupplier);
        //set table control
        setTableControlDataDetail();
        //set table to content-view
        ancTableDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancTableDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail(TblSupplier dataSupplier) {
        TableView<TblPurchaseOrder> tableView = new TableView();

        TableColumn<TblPurchaseOrder, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodePo(),
                        param.getValue().codePoProperty()));
        codePO.setMinWidth(85);

        TableColumn<TblPurchaseOrder, String> totalBill = new TableColumn("Total Tagihan");
        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())),
                        param.getValue().idpoProperty()));
        totalBill.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> paymentsDate = new TableColumn("Tanggal \nPembayaran");
        paymentsDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> getPOPaymentsDate(param.getValue()),
                        param.getValue().idpoProperty()));
        paymentsDate.setMinWidth(110);

//        TableColumn<TblPurchaseOrder, String> totalPayment = new TableColumn("Nominal Pembayaran");
//        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> getPOPaymentNominal(param.getValue()),
//                        param.getValue().idpoProperty()));
//        totalPayment.setMinWidth(140);
        TableColumn<TblPurchaseOrder, String> totalPayment = new TableColumn("Total Pembayaran");
        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())),
                        param.getValue().idpoProperty()));
        totalPayment.setMinWidth(140);

//        TableColumn<TblPurchaseOrder, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
//        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> getPOPaymentRestOfBill(param.getValue()),
//                        param.getValue().idpoProperty()));
//        totalRestOfBill.setMinWidth(140);
        TableColumn<TblPurchaseOrder, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idpoProperty()));
        totalRestOfBill.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> totalNominal = new TableColumn("Total Nominal");
        totalNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(()
                        -> "Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())) + "\n"
                        + "Pembayaran " + ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())) + "\n"
                        + "Sisa Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idpoProperty()));
        totalNominal.setMinWidth(180);

        TableColumn<TblPurchaseOrder, String> dueDate = new TableColumn("Tanggal \nEstimasi Bayar");
        dueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> getPOPaymentDueDate(param.getValue()),
                        param.getValue().idpoProperty()));
        dueDate.setMinWidth(110);

        TableColumn<TblPurchaseOrder, String> paymentStatus = new TableColumn("Status");
        paymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelPayable() != null
                                ? param.getValue().getTblHotelPayable().getRefFinanceTransactionStatus().getStatusName()
                                : "Belum Dibayar", param.getValue().tblHotelPayableProperty()));
        paymentStatus.setMinWidth(120);

        TableColumn<TblPurchaseOrder, String> paymentNote = new TableColumn("Keterangan");
        paymentNote.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getPonote(),
                        param.getValue().ponoteProperty()));
        paymentNote.setMinWidth(190);

        TableColumn<TblPurchaseOrder, String> infoButton = new TableColumn("Detail");
        infoButton.setMinWidth(55);
        infoButton.setCellFactory((TableColumn<TblPurchaseOrder, String> param) -> new ButtonCellInfo<>());

        TableColumn<TblPurchaseOrder, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeInvoice.setMinWidth(100);

//        tableView.getColumns().addAll(codeInvoice, codePO, dueDate, totalBill, paymentsDate, totalPayment, totalRestOfBill, paymentStatus);
        tableView.getColumns().addAll(codeInvoice, codePO, dueDate, totalBill, totalPayment, totalRestOfBill, paymentStatus, paymentNote, infoButton);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataPurchaseOrder(dataSupplier)));

//        tableView.setRowFactory(tv -> {
//            TableRow<TblPurchaseOrder> row = new TableRow<>();
//            row.setOnMouseClicked((e) -> {
//                if (!row.isEmpty()) {
//                    if (!ClassSession.unSavingDataInput.get()) {
//                        //set data form input detail
//                        setSelectedDataToInputFormDetail(
//                                ((TblPurchaseOrder) row.getItem()) != null
//                                        ? ((TblPurchaseOrder) row.getItem()).getTblHotelPayable()
//                                        : null);
//                    } else {  //unsaving data input
//                        Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
//                        if (alert.getResult() == ButtonType.OK) {
//                            ClassSession.unSavingDataInput.set(false);
//                            //set data form input detail
//                            setSelectedDataToInputFormDetail(
//                                    ((TblPurchaseOrder) row.getItem()) != null
//                                            ? ((TblPurchaseOrder) row.getItem()).getTblHotelPayable()
//                                            : null);
//                        } else {
//                            if (tableDataDetail != null
//                                    && tableDataDetail.getTableView() != null) {
//                                tableDataDetail.getTableView().getSelectionModel().clearSelection();
//                                if (idInvoice != 0L) {
//                                    for(int i=0; i<tableDataDetail.getTableView().getItems().size(); i++){
//                                        if(((TblPurchaseOrder)tableDataDetail.getTableView().getItems().get(i)).getTblHotelPayable()!= null
//                                                && ((TblPurchaseOrder)tableDataDetail.getTableView().getItems().get(i)).getTblHotelPayable().getTblHotelInvoice() != null
//                                                && ((TblPurchaseOrder)tableDataDetail.getTableView().getItems().get(i)).getTblHotelPayable().getTblHotelInvoice().getIdhotelInvoice()
//                                                == idInvoice){
//                                            tableDataDetail.getTableView().getSelectionModel().select(i);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            });
//            return row;
//        });
        tableView.setRowFactory(tv -> {
            TableRow<TblPurchaseOrder> row = new TableRow<>();

            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(notPaidPseudoClass, isNotPaid(newVal));
                    row.pseudoClassStateChanged(overPaymentPseudoClass, isOverPayment(newVal));
                } else {
                    row.pseudoClassStateChanged(notPaidPseudoClass, false);
                    row.pseudoClassStateChanged(overPaymentPseudoClass, false);
                }
            });
            return row;
        });

        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private String getPOPaymentDueDate(TblPurchaseOrder po) {
        if (po != null
                && po.getTblHotelPayable() != null
                && po.getTblHotelPayable().getTblHotelInvoice() != null
                && po.getTblHotelPayable().getTblHotelInvoice().getDueDate() != null) {
            return ClassFormatter.dateFormate.format(po.getTblHotelPayable().getTblHotelInvoice().getDueDate());
        }
        return "-";
    }

    private String getPOPaymentsDate(TblPurchaseOrder po) {
        if (po != null
                && po.getTblHotelPayable() != null) {
            String result = "";
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(po.getTblHotelPayable().getIdhotelPayable());
            for (int i = 0; i < list.size(); i++) {
                result += ClassFormatter.dateFormate.format(list.get(i).getTblHotelFinanceTransaction().getCreateDate()) + "\n";
            }
            return list.isEmpty() ? "-" : result;
        }
        return "-";
    }

    private String getPOPaymentNominal(TblPurchaseOrder po) {
        if (po != null
                && po.getTblHotelPayable() != null) {
            String result = "";
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(po.getTblHotelPayable().getIdhotelPayable());
            for (int i = 0; i < list.size(); i++) {
                result += (list.get(i).getTblHotelFinanceTransaction().getIsReturnTransaction() ? "-" : "")
                        + ClassFormatter.currencyFormat.cFormat(list.get(i).getNominalTransaction()) + "\n";
            }
            return list.isEmpty() ? "0" : result;
        }
        return "0";
    }

    private String getPOPaymentRestOfBill(TblPurchaseOrder po) {
        if (po != null
                && po.getTblHotelPayable() != null) {
            String result = "";
            BigDecimal restOfBill = po.getTblHotelPayable().getHotelPayableNominal();
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(po.getTblHotelPayable().getIdhotelPayable());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                    restOfBill = restOfBill.add(list.get(i).getNominalTransaction());
                } else {
                    restOfBill = restOfBill.subtract(list.get(i).getNominalTransaction());
                }
                result += (list.get(i).getTblHotelFinanceTransaction().getIsReturnTransaction() ? "-" : "")
                        + ClassFormatter.currencyFormat.cFormat(restOfBill) + "\n";
            }
            return list.isEmpty() ? ClassFormatter.currencyFormat.cFormat(po.getTblHotelPayable().getHotelPayableNominal()) : result;
        }
        return ClassFormatter.currencyFormat.cFormat(calculationTotal(po));
    }

    private boolean isNotPaid(TblPurchaseOrder dataPO) {
        boolean isNotPaid = false;
        if (dataPO != null
                && dataPO.getTblHotelPayable() != null) {
            return dataPO.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() != 2 //Sudah Dibayar = '2'
                    && dataPO.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() != 5; //Kelebihan Bayar = '5'
        }
        return isNotPaid;
    }

    private boolean isOverPayment(TblPurchaseOrder dataPO) {
        boolean isNotPaid = false;
        if (dataPO != null
                && dataPO.getTblHotelPayable() != null) {
            return dataPO.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 5; //Kelebihan Bayar = '5'
        }
        return isNotPaid;
    }

    public class ButtonCellInfo<S, T> extends TableCell<S, T> {

        private final JFXButton button;

        public ButtonCellInfo() {
            this.button = new JFXButton();
            this.button.getStyleClass().add("button-mark-valid");
            this.button.setPrefSize(25, 25);
            this.button.setButtonType(JFXButton.ButtonType.RAISED);
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
                        this.button.setOnMouseClicked((e) -> {
                            showDetailHandle(((TblPurchaseOrder) data));
                        });
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
    
    /**
     * Button Cell Handle
     */
    private void showDetailHandle(TblPurchaseOrder dataPO){
        //set selected date
        selectedDataPO = dataPO;
        //show dialog data detail
        showDataPODetail();
    }

    private void showDataPODetail() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/PODetailInfoDialog.fxml"));

            PODetailInfoController controller = new PODetailInfoController(this);
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
    
    private List<TblPurchaseOrder> loadAllDataPurchaseOrder(TblSupplier dataSuplier) {
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (dataSuplier != null) {
            list = parentController.getService().getAllDataPurchaseOrderByIDSupplier(dataSuplier.getIdsupplier());
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getRefPurchaseOrderStatus().getIdstatus() != 5) {    //status po != 5 ('Order')
                    list.remove(i);
                }
            }
            for (TblPurchaseOrder data : list) {
                //data purchase request
                if (data.getTblPurchaseRequest() != null) {
                    data.setTblPurchaseRequest(parentController.getService().getDataPurchaseRequest(data.getTblPurchaseRequest().getIdpr()));
                }
                //data retur
                if (data.getTblRetur() != null) {
                    data.setTblRetur(parentController.getService().getDataRetur(data.getTblRetur().getIdretur()));
                }
                //data hotel payable
                if (data.getTblHotelPayable() != null) {
                    data.setTblHotelPayable(parentController.getService().getDataHotelPayable(data.getTblHotelPayable().getIdhotelPayable()));
                    //data invoice
                    if (data.getTblHotelPayable().getTblHotelInvoice() != null) {
                        data.getTblHotelPayable().setTblHotelInvoice(parentController.getService().getDataHotelInvoice(data.getTblHotelPayable().getTblHotelInvoice().getIdhotelInvoice()));
                    }
                    //data hotel payable - type
                    data.getTblHotelPayable().setRefHotelPayableType(parentController.getService().getDataHotelPayableType(data.getTblHotelPayable().getRefHotelPayableType().getIdtype()));
                    //data hotel payable - status
                    data.getTblHotelPayable().setRefFinanceTransactionStatus(parentController.getService().getDataFinanceTransactionStatus(data.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus()));
                } else {
                    //do something here..?
                }
                //data supplier
                data.setTblSupplier(parentController.getService().getDataSupplier(data.getTblSupplier().getIdsupplier()));
                //data po - status
                data.setRefPurchaseOrderStatus(parentController.getService().getDataPurchaseOrderStatus(data.getRefPurchaseOrderStatus().getIdstatus()));
                //data po - item-arrive status
                data.setRefPurchaseOrderItemArriveStatus(parentController.getService().getDataPurchaseOrderItemArriveStatus(data.getRefPurchaseOrderItemArriveStatus().getIdstatus()));
                //data po - payment status
                data.setRefPurchaseOrderPaymentStatus(parentController.getService().getDataPurchaseOrderPaymentStatus(data.getRefPurchaseOrderPaymentStatus().getIdstatus()));
            }
            //remove data isn't used
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getTblHotelPayable() != null
                        && list.get(i).getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 3) {   //Sudah Tidak Berlaku = '3'
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private void setTableControlDataDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Set/Update Data Invoice");
            buttonControl.setTooltip(new Tooltip("Set/Update Data Invoice"));
            buttonControl.setOnMouseClicked((e) -> {
                //listener create/update data invoice
                dataSetInvoiceUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Transaksi Pembayaran");
            buttonControl.setTooltip(new Tooltip("Buat Transaksi Pembayaran"));
            buttonControl.setOnMouseClicked((e) -> {
                //listener create payment
                dataHotelFinanceTransactionCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Transaksi Pengembalian Pembayaran");
            buttonControl.setTooltip(new Tooltip("Buat Transaksi Pengembalian Pembayaran"));
            buttonControl.setOnMouseClicked((e) -> {
                //listener create return - payment
                dataHotelFinanceTransactionReturnCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataDetail.addButtonControl(buttonControls);
    }

//    private Long idInvoice = 0L;
//    
//    private void setSelectedDataToInputFormDetail(TblHotelPayable hotelPayable) {
//        if (hotelPayable != null
//                && hotelPayable.getTblHotelInvoice() != null) {
//            idInvoice = hotelPayable.getTblHotelInvoice().getIdhotelInvoice();
//            txtCodeInvoice.setText(hotelPayable.getTblHotelInvoice().getCodeHotelInvoice());
//            txtSubject.setText(hotelPayable.getTblHotelInvoice().getHotelInvoiceSubject());
//            if (hotelPayable.getTblHotelInvoice().getIssueDate() != null) {
//                dtpIssueDate.setValue(hotelPayable.getTblHotelInvoice().getIssueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            } else {
//                dtpIssueDate.setValue(null);
//            }
//            if (hotelPayable.getTblHotelInvoice().getDueDate() != null) {
//                dtpDueDate.setValue(hotelPayable.getTblHotelInvoice().getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            } else {
//                dtpDueDate.setValue(null);
//            }
//            txtTerm.setText(hotelPayable.getTblHotelInvoice().getHotelInvoiceNote());
//        } else {
//            idInvoice = 0L;
//            txtCodeInvoice.setText("");
//            txtSubject.setText("");
//            dtpIssueDate.setValue(null);
//            dtpDueDate.setValue(null);
//            txtTerm.setText("");
//        }
//        //set enable or disable
//        if(tableDataDetail != null
//                && tableDataDetail.getTableView() != null
//                && tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1){   //enable
//            txtCodeInvoice.setDisable(false);
//            txtSubject.setDisable(false);
//            dtpIssueDate.setDisable(false);
//            dtpDueDate.setDisable(false);
//            txtTerm.setDisable(false);
//        }else{  //disable
//            txtCodeInvoice.setDisable(true);
//            txtSubject.setDisable(true);
//            dtpIssueDate.setDisable(true);
//            dtpDueDate.setDisable(true);
//            txtTerm.setDisable(true);
//        }
////        //set table detail (transaction)
////        initTableDataDetailTransaction(hotelPayable);
//        //set unsaving data input -> 'false'
//        ClassSession.unSavingDataInput.set(false);
//    }
//    public TableView tableDataDetailTransaction;
//
//    private void initTableDataDetailTransaction(TblHotelPayable hotelPayable) {
//        //set table
//        setTableDataDetailTransaction(hotelPayable);
//        //set table to content-view
//        ancTableHotelFinanceTransaction.getChildren().clear();
//        AnchorPane.setBottomAnchor(tableDataDetailTransaction, 0.0);
//        AnchorPane.setLeftAnchor(tableDataDetailTransaction, 0.0);
//        AnchorPane.setRightAnchor(tableDataDetailTransaction, 0.0);
//        AnchorPane.setTopAnchor(tableDataDetailTransaction, 0.0);
//        ancTableHotelFinanceTransaction.getChildren().add(tableDataDetailTransaction);
//    }
//
//    private void setTableDataDetailTransaction(TblHotelPayable hotelPayable) {
//        TableView<TblHotelFinanceTransactionHotelPayable> tableView = new TableView();
//
//        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codePayment = new TableColumn("No. Pembayaran");
//        codePayment.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelFinanceTransaction().getCodeTransaction(),
//                        param.getValue().tblHotelFinanceTransactionProperty()));
//        codePayment.setMinWidth(120);
//
//        TableColumn<TblHotelFinanceTransactionHotelPayable, String> paymentNominal = new TableColumn("Nominal Pembayaran");
//        paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getNominalTransaction()),
//                        param.getValue().nominalTransactionProperty()));
//        paymentNominal.setMinWidth(140);
//
//        TableColumn<TblHotelFinanceTransactionHotelPayable, String> paymentDate = new TableColumn("Tanggal Pembayaran");
//        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getTblHotelFinanceTransaction().getCreateDate()),
//                        param.getValue().tblHotelFinanceTransactionProperty()));
//        paymentDate.setMinWidth(140);
//
//        tableView.getColumns().addAll(codePayment, paymentNominal, paymentDate);
//        tableView.setItems(FXCollections.observableArrayList(loadAllDataHotelFinanceTransactionHotelPayable(hotelPayable)));
//
//        tableDataDetailTransaction = tableView;
//    }
//
//    private List<TblHotelFinanceTransactionHotelPayable> loadAllDataHotelFinanceTransactionHotelPayable(TblHotelPayable hotelPayable) {
//        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
//        if (hotelPayable != null) {
//            list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(hotelPayable.getIdhotelPayable());
//        }
//        return list;
//    }
    /**
     * HANDLE FOR DATA INPUT
     */
    private int dataInputStatus = 0;

    private void dataHotelPayableUpdateHandle() {
        if (tableDataPOPayable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = (TblSupplier) tableDataPOPayable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataPOPayableFormShowStatus.set(0);
            dataPOPayableFormShowStatus.set(1);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataHotelPayableShowHandle() {
        if (tableDataPOPayable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = (TblSupplier) tableDataPOPayable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataPOPayableFormShowStatus.set(1);
            isShowStatus.set(true);
        }
    }

    private void dataHotelPayableUnshowHandle() {
        refreshDataTablePOPayable();
        dataPOPayableFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataSetInvoiceUpdateHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToUpdateInvoice(((TblPurchaseOrder) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()))) {
                selectedDataPO = ((TblPurchaseOrder) tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
                showDataInvoiceDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT SET/UPDATE DATA PO",
                        "Data PO sudah tidak berlaku (data PO telah ditutup)..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataPOEnableToUpdateInvoice(TblPurchaseOrder dataPO) {
        return dataPO.getRefPurchaseOrderStatus().getIdstatus() != 6;   //Selesai = '6'
    }

    private void showDataInvoiceDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/PurchaseOrderPaymentInvoiceDialog.fxml"));

            PurchaseOrderPaymentInvoiceController controller = new PurchaseOrderPaymentInvoiceController(this);
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

    public void refreshDataTableDetail() {
        tableDataDetail.getTableView().setItems(FXCollections.observableArrayList(loadAllDataPurchaseOrder(selectedData)));
    }

    public TblHotelFinanceTransaction selectedDataHFT;

    public TblHotelFinanceTransactionWithCash selectedDataHFTWithCash;

    public TblHotelFinanceTransactionWithTransfer selectedDataHFTWithTransfer;

    public TblHotelFinanceTransactionWithCekGiro selectedDataHFTWithCekGiro;

    public List<TblHotelFinanceTransactionHotelPayable> selectedDataHFTHPs;

    private void dataHotelFinanceTransactionCreateHandle() {
        if (checkEnableToCreateHotelFinanceTransaction()) {
            selectedDataHFT = new TblHotelFinanceTransaction();
            selectedDataHFT.setTransactionNominal(new BigDecimal("0"));
            selectedDataHFT.setTransactionRoundingValue(new BigDecimal("0"));
            selectedDataHFT.setIsReturnTransaction(false);
            selectedDataHFT.setRefFinanceTransactionType(parentController.getService().getDataFinanceTransactionType(0));    //payable = '0'
            selectedDataHFTWithCash = new TblHotelFinanceTransactionWithCash();
            selectedDataHFTWithCash.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTWithTransfer = new TblHotelFinanceTransactionWithTransfer();
            selectedDataHFTWithTransfer.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
            selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTHPs = new ArrayList<>();
            for (TblPurchaseOrder dataPO : (List<TblPurchaseOrder>) tableDataDetail.getTableView().getItems()) {
                if (dataPO.getTblHotelPayable() != null //has been set as hotel payable
                        && calculationTotalRestOfBill(dataPO).compareTo(new BigDecimal("0")) == 1) {     //nominal rest of bill more then '0'
                    TblHotelFinanceTransactionHotelPayable dataHFTHP = new TblHotelFinanceTransactionHotelPayable();
                    dataHFTHP.setTblHotelFinanceTransaction(selectedDataHFT);
                    dataHFTHP.setTblHotelPayable(dataPO.getTblHotelPayable());
                    dataHFTHP.setNominalTransaction(new BigDecimal("0"));
                    //add to list
                    selectedDataHFTHPs.add(dataHFTHP);
                }
            }
            showDataHotelFinanceTransactionDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MEMBUAT TRANSAKSI PEMBAYARAN",
                    "Tidak ada data hutang yang harus dibayar \n(*semua data hutang yang ingin dibayarkan harus memiliki nomor invoice)..!", null);
        }
    }

    private boolean checkEnableToCreateHotelFinanceTransaction() {
        return getTotalHotelPayableJustHaveCodeInvoiceForPayment(selectedData).compareTo(new BigDecimal("0")) == 1;
    }

    public Stage dialogStageDetal;

    private void showDataHotelFinanceTransactionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/HotelFinanceTransactionInputDialog.fxml"));

            HotelFinanceTransactionInputController controller = new HotelFinanceTransactionInputController(this);
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

    private void dataHotelFinanceTransactionReturnCreateHandle() {
        if (checkEnableToCreateHotelFinanceTransactionReturn()) {
            selectedDataHFT = new TblHotelFinanceTransaction();
            selectedDataHFT.setTransactionNominal(new BigDecimal("0"));
            selectedDataHFT.setTransactionRoundingValue(new BigDecimal("0"));
            selectedDataHFT.setIsReturnTransaction(true);
            selectedDataHFT.setRefFinanceTransactionType(parentController.getService().getDataFinanceTransactionType(0));    //payable = '0'
            selectedDataHFTWithCash = new TblHotelFinanceTransactionWithCash();
            selectedDataHFTWithCash.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTWithTransfer = new TblHotelFinanceTransactionWithTransfer();
            selectedDataHFTWithTransfer.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
            selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTHPs = new ArrayList<>();
            for (TblPurchaseOrder dataPO : (List<TblPurchaseOrder>) tableDataDetail.getTableView().getItems()) {
                if (dataPO.getTblHotelPayable() != null //has been set as hotel payable
                        && calculationTotalRestOfBill(dataPO).compareTo(new BigDecimal("0")) == -1) {     //nominal rest of bill lsss then '0'
                    TblHotelFinanceTransactionHotelPayable dataHFTHP = new TblHotelFinanceTransactionHotelPayable();
                    dataHFTHP.setTblHotelFinanceTransaction(selectedDataHFT);
                    dataHFTHP.setTblHotelPayable(dataPO.getTblHotelPayable());
                    dataHFTHP.setNominalTransaction(new BigDecimal("0"));
                    //add to list
                    selectedDataHFTHPs.add(dataHFTHP);
                }
            }
            showDataHotelFinanceTransactionReturnDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MEMBUAT TRANSAKSI PENGEMBALIAN PEMBAYARAN",
                    "Tidak ada data pembayaran hutang yang harus dikembalikan \n(*semua data hutang harus memiliki nomor invoice)..!", null);
        }
    }

    private boolean checkEnableToCreateHotelFinanceTransactionReturn() {
        return getTotalHotelPayableJustHaveCodeInvoiceForReturn(selectedData).compareTo(new BigDecimal("0")) == -1;
    }

    private void showDataHotelFinanceTransactionReturnDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/HotelFinanceTransactionReturnInputDialog.fxml"));

            HotelFinanceTransactionReturnInputController controller = new HotelFinanceTransactionReturnInputController(this);
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

//    private void dataInvoiceSaveHandle() {
//        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            if (checkDataInputInvoice()) {
//                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    selectedDataPO = ((TblPurchaseOrder) tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
//                    //dummy
//                    TblPurchaseOrder dummySelectedDataPO = new TblPurchaseOrder(selectedDataPO);
//                    dummySelectedDataPO.setTblSupplier(new TblSupplier(dummySelectedDataPO.getTblSupplier()));
//                    if (dummySelectedDataPO.getTblHotelPayable() != null) {
//                        dummySelectedDataPO.setTblHotelPayable(new TblHotelPayable(dummySelectedDataPO.getTblHotelPayable()));
//                        dummySelectedDataPO.getTblHotelPayable().setTblHotelInvoice(new TblHotelInvoice(dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice()));
//                    } else {
//                        dummySelectedDataPO.setTblHotelPayable(new TblHotelPayable());
//                        dummySelectedDataPO.getTblHotelPayable().setHotelPayableNominal(calculationTotal(dummySelectedDataPO));
//                        dummySelectedDataPO.getTblHotelPayable().setRefHotelPayableType(parentController.getService().getDataHotelPayableType(1));   //Purchase Order = '1'
//                        dummySelectedDataPO.getTblHotelPayable().setRefFinanceTransactionStatus(parentController.getService().getDataFinanceTransactionStatus(0));    //Belum Dibayar = '0'
//                        dummySelectedDataPO.getTblHotelPayable().setTblHotelInvoice(new TblHotelInvoice());
//                        dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setTblSupplier(parentController.getService().getDataSupplier(dummySelectedDataPO.getTblSupplier().getIdsupplier()));
//                        dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setRefHotelInvoiceType(parentController.getService().getDataHotelInvoiceType(0));    //Payable = '0' 
//                    }
//                    //set data from field input
//                    dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setCodeHotelInvoice(txtCodeInvoice.getText());
//                    dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setHotelInvoiceSubject(txtSubject.getText());
//                    dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setIssueDate(dtpIssueDate.getValue() != null
//                            ? Date.valueOf(dtpIssueDate.getValue())
//                            : null);
//                    dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setDueDate(dtpDueDate.getValue() != null
//                            ? Date.valueOf(dtpDueDate.getValue())
//                            : null);
//                    dummySelectedDataPO.getTblHotelPayable().getTblHotelInvoice().setHotelInvoiceNote(txtTerm.getText());
//                    if (parentController.getService().insertDataHotelInvoice(dummySelectedDataPO) != null) {
//                        ClassMessage.showSucceedInsertingDataMessage("", null);
//                        //set selected data input form (table-op-hp)
//                        tableDataDetail.getTableView().setItems(FXCollections.observableArrayList(loadAllDataPurchaseOrder(selectedData)));
//                        //set selected data input form detail
//                        setSelectedDataToInputFormDetail(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1
//                                ? ((TblPurchaseOrder) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelPayable()
//                                : null);
//                        //set unsaving data input -> 'false'
//                        ClassSession.unSavingDataInput.set(false);
//                    } else {
//                        ClassMessage.showFailedInsertingDataMessage(parentController.getService().getErrorMessage(), null);
//                    }
//                }
//            } else {
//                ClassMessage.showWarningInputDataMessage(errDataInput, null);
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//    private String errDataInput;
//
//    private boolean checkDataInputInvoice() {
//        boolean dataInput = true;
//        errDataInput = "";
//        if (txtCodeInvoice.getText() == null
//                || txtCodeInvoice.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "No. Invoice : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        return dataInput;
//    }
//
//    private void dataInvoicePrintHandle() {
//
//    }
    private void dataPOPayableBackHandle() {
        refreshDataTablePOPayable();
        dataPOPayableFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    public void refreshDataTablePOPayable() {
        //refresh data from table & close form data detail
        tableDataPOPayable.getTableView().setItems(FXCollections.observableArrayList(loadAllDataSupplier()));
        cft.refreshFilterItems(tableDataPOPayable.getTableView().getItems());
    }

    public void refreshSelectedData() {
        setSelectedDataToInputForm();
        dataPOPayableFormShowStatus.set(1);
    }

//    public void refreshData() {
//        //data table
//        tableDataPurchaseOrderPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelPayablePurchaseOrder()));
//        //data table detail
//        setSelectedDataToInputForm(null);
//        //close form table detail
//        dataPurchaseOrderPaymentFormShowStatus.set(0.0);
//    }
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataPOPayableSplitpane();

        //init table
        initTableDataPOPayable();

        //init form
        initFormDataPOPayable();

        spDataPOPayable.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPOPayableFormShowStatus.set(0.0);
        });
    }

    public PurchaseOrderPayableController(PurchaseOrderPayableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final PurchaseOrderPayableAndPaymentController parentController;

    public FPurchaseOrderManager getService() {
        return parentController.getService();
    }

}
