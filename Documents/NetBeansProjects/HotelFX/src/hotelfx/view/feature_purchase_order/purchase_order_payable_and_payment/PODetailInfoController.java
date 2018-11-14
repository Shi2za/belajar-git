/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailPropertyBarcode;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PODetailInfoController implements Initializable {

    /**
     * SPLITPANE BETWEEN DATA SUMMARY, DATA HISTORY
     */
    @FXML
    private SplitPane spFormDataReceivingAndRetur;

    private DoubleProperty dataHistoryFormShowStatus;

    private boolean isTimeLinePlayingRARSH = false;

    private void setDataReceivingAndReturSummaryHistorySplitpane() {
        spFormDataReceivingAndRetur.setDividerPositions(1);

        dataHistoryFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataHistoryFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlayingRARSH = true;
            KeyValue keyValue = new KeyValue(spFormDataReceivingAndRetur.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlayingRARSH = false;
            });
        });

        SplitPane.Divider div = spFormDataReceivingAndRetur.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlayingRARSH) {
                div.setPosition(divPosition.get());
            }
        });

        dataHistoryFormShowStatus.set(0.0);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private TabPane tabPaneFormLayout;

    @FXML
    private Tab tabDataPO;

    @FXML
    private Tab tabDataRecevingAndRetur;

    @FXML
    private Tab tabPOPayment;

    /**
     * PO
     */
    @FXML
    private GridPane gpFormDataPO;

    @FXML
    private JFXTextField txtDueDate;

    @FXML
    private JFXTextField txtPOPaymentTypeInformation;

    @FXML
    private JFXTextArea txtPONote;

    @FXML
    private JFXTextField txtSubTotal;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private JFXTextField txtTax;

    @FXML
    private Spinner<Double> spnTaxPercentage;

    @FXML
    private Label lblTaxPercentage;

    @FXML
    private JFXTextField txtDeliveryCost;

    @FXML
    private Label lblTotal;

    /**
     * RECEIVING & RETUR
     */
    @FXML
    private GridPane gpFormDataReceiving;

    @FXML
    private JFXButton btnShowReceivingAndReturHistory;

    @FXML
    private GridPane gpFormDataReceivingReturHistory;

    @FXML
    private JFXButton btnShowReceivingAndReturSummary;

    /**
     * PO - PAYMENT
     */
    @FXML
    private GridPane gpFormDataPOPayment;

    @FXML
    private JFXTextField txtCodeInvoice;

    @FXML
    private JFXTextField txtPaymentDueDate;

    @FXML
    private JFXTextField txtTotalHotelPayableBySupplier;

    @FXML
    private JFXTextField txtTotalHotelPayableBySystem;

    @FXML
    private JFXTextField txtTotalHotelPayment;

    @FXML
    private JFXTextField txtTotalHotelRestOfBill;

    @FXML
    private Label lblPaymentStatus;

    //--------------------------------------------------------------------------
    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnBack;

    private void initFormDataPO() {
        spnTaxPercentage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 100.00));
        spnTaxPercentage.setEditable(false);

        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            dataPOClosingCancelHandle();
        });

        btnShowReceivingAndReturHistory.setTooltip(new Tooltip("Lihat History"));
        btnShowReceivingAndReturHistory.setOnAction((e) -> {
            dataHistoryFormShowStatus.set(1.0);
        });

        btnShowReceivingAndReturSummary.setTooltip(new Tooltip("Lihat Summary"));
        btnShowReceivingAndReturSummary.setOnAction((e) -> {
            dataHistoryFormShowStatus.set(0.0);
        });

        initDateCalendar();

        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern
        );
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now()
        );
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField();
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtDiscount,
                txtDeliveryCost
        );
    }

    private void setSelectedDataToInputForm() {
        //label code
        lblCodeData.setText((purchaseOrderPayableController.selectedDataPO.getCodePo() != null
                ? (purchaseOrderPayableController.selectedDataPO.getCodePo()
                + (purchaseOrderPayableController.selectedDataPO.getTblPurchaseRequest() != null
                        ? (" (" + purchaseOrderPayableController.selectedDataPO.getTblPurchaseRequest().getCodePr() + ") ")
                        : "")
                + (purchaseOrderPayableController.selectedDataPO.getTblSupplier() != null
                        ? (" - " + purchaseOrderPayableController.selectedDataPO.getTblSupplier().getSupplierName())
                        : ""))
                : ""));
        //data PO
        setSelectedDataToInputFormPO();
        //data receiving & retur
        setSelectedDataToInputFormMIAndRetur();
        //data PO payment
        setSelectedDataToInputFormPOPayment();

        tabPaneFormLayout.getSelectionModel().selectFirst();
    }

    private void setSelectedDataToInputFormPO() {
        if (purchaseOrderPayableController.selectedDataPO.getPodueDate() != null) {
            txtDueDate.setText(ClassFormatter.dateFormate.format(purchaseOrderPayableController.selectedDataPO.getPodueDate()));
        } else {
            txtDueDate.setText("-");
        }

        txtPOPaymentTypeInformation.textProperty().bindBidirectional(purchaseOrderPayableController.selectedDataPO.popaymentTypeInformationProperty());
        txtPONote.textProperty().bindBidirectional(purchaseOrderPayableController.selectedDataPO.canceledNoteProperty());

        Bindings.bindBidirectional(txtDiscount.textProperty(), purchaseOrderPayableController.selectedDataPO.nominalDiscountProperty(), new ClassFormatter.CBigDecimalStringConverter());
        purchaseOrderPayableController.selectedDataPO.nominalDiscountProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        spnTaxPercentage.getValueFactory().setValue(purchaseOrderPayableController.selectedDataPO.getTaxPecentage() != null
                ? purchaseOrderPayableController.selectedDataPO.getTaxPecentage().doubleValue() * 100
                : 0);
        spnTaxPercentage.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                purchaseOrderPayableController.selectedDataPO.setTaxPecentage(BigDecimal.valueOf(newVal / 100));
            }
        });
        purchaseOrderPayableController.selectedDataPO.taxPecentageProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        Bindings.bindBidirectional(txtDeliveryCost.textProperty(), purchaseOrderPayableController.selectedDataPO.deliveryCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        purchaseOrderPayableController.selectedDataPO.deliveryCostProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        //set table - po
        setTableDataDetail();

        refreshDataBill();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormMIAndRetur() {
        //set table - receiving & retur (summary)
        setTableDataMIDetail();
        //set table - receiving & retur (history)
        setTableDataMIHistory();
        //set show data summary
        dataHistoryFormShowStatus.set(1.0);
        dataHistoryFormShowStatus.set(0.0);
    }

    private void setSelectedDataToInputFormPOPayment() {
        txtCodeInvoice.setText(getCodeInvoice());
        txtPaymentDueDate.setText(getPaymentDueDate());
        txtTotalHotelPayableBySupplier.setText(ClassFormatter.currencyFormat.cFormat(getTotalHotelPayableBySupplier()));
        txtTotalHotelPayableBySystem.setText(ClassFormatter.currencyFormat.cFormat(getTotalHotelPayableBySystem()));
        txtTotalHotelPayment.setText(ClassFormatter.currencyFormat.cFormat(getTotalHotelPayment()));
        txtTotalHotelRestOfBill.setText(ClassFormatter.currencyFormat.cFormat(getTotalHotelRestOfBill()));
        lblPaymentStatus.setText(getTotalHotelPaymentStatus());

        //set table transaction payment
        setTableDataHotelTransactionPaymentDetail();
    }

    private String getCodeInvoice() {
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            return purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice().getCodeHotelInvoice();
        }
        return "-";
    }

    private String getPaymentDueDate() {
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            return ClassFormatter.dateFormate.format(purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice().getDueDate());
        }
        return "-";
    }

    private BigDecimal getTotalHotelPayableBySupplier() {
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            return purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getHotelPayableNominal();
        }
        return new BigDecimal("0");
    }

    private BigDecimal getTotalHotelPayableBySystem() {
        BigDecimal result = new BigDecimal("0");
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            List<PurchaseOrderDetailCreated> podcs = loadAllDataPODetailCreated();
            List<MIDetail> mids = loadAllDataMIDetail();
            for (PurchaseOrderDetailCreated podc : podcs) {
                if (podc.getCreateStatus()) {
                    for (MIDetail mid : mids) {
                        if (!mid.getIsBonus()
                                && mid.getDataMIDetail().getTblSupplierItem().getIdrelation() == podc.getDataPODetail().getTblSupplierItem().getIdrelation()) {
                            result = result.add((podc.getDataPODetail().getItemCost()
                                    .subtract(podc.getDataPODetail().getItemDiscount()))
                                    .multiply((getQuatityItemHasBeenGet(mid.getDataMIDetail())
                                            .subtract(getQuantityItemHasBeenRetur(mid.getDataMIDetail())))));
                            break;
                        }
                    }
                }
            }
            result = result.subtract(purchaseOrderPayableController.selectedDataPO.getNominalDiscount());
            result = ((new BigDecimal("1")).add((purchaseOrderPayableController.selectedDataPO.getTaxPecentage().divide(new BigDecimal("1"))))).multiply(result);
            result = result.add(purchaseOrderPayableController.selectedDataPO.getDeliveryCost());
        }
        return result;
    }

    private BigDecimal getTotalHotelPayment() {
        BigDecimal result = new BigDecimal("0");
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            List<TblHotelFinanceTransactionHotelPayable> list = purchaseOrderPayableController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getIdhotelPayable());
            for (TblHotelFinanceTransactionHotelPayable data : list) {
                result = result.add(data.getNominalTransaction());
            }
        }
        return result;
    }

    private BigDecimal getTotalHotelRestOfBill() {
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            return getTotalHotelPayableBySupplier()
                    .subtract(getTotalHotelPayment());
        }
        return new BigDecimal("0");
    }

    private String getTotalHotelPaymentStatus() {
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getTblHotelInvoice() != null) {
            return "(" + purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getRefFinanceTransactionStatus().getStatusName() + ")";
        }
        return "";
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtSubTotal.setDisable(true);
        txtTax.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataPO,
                true,
                txtSubTotal,
                txtTax
        );

        btnBack.setDisable(false);
    }

    private BigDecimal calculationSubTotal() {
        BigDecimal result = new BigDecimal("0");
        if (tableDataDetail != null) {
            List<PurchaseOrderDetailCreated> list = (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems();
            for (PurchaseOrderDetailCreated data : list) {
                if (data.getCreateStatus()) {
                    result = result.add(calculationTotalCost(data.getDataPODetail()));
                }
            }
        }
        return result;
    }

    private BigDecimal calculationTax() {
        return (purchaseOrderPayableController.selectedDataPO.getTaxPecentage())
                .multiply(calculationSubTotal().subtract(purchaseOrderPayableController.selectedDataPO.getNominalDiscount()));
    }

    private BigDecimal calculationTotal() {
        return (((new BigDecimal("1")).add(purchaseOrderPayableController.selectedDataPO.getTaxPecentage()))
                .multiply(calculationSubTotal().subtract(purchaseOrderPayableController.selectedDataPO.getNominalDiscount())))
                .add(purchaseOrderPayableController.selectedDataPO.getDeliveryCost());
    }

    public void refreshDataBill() {
        txtSubTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationSubTotal()));
        txtTax.setText(ClassFormatter.currencyFormat.cFormat(calculationTax()));
        lblTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationTotal()));
    }

    private void dataPOClosingCancelHandle() {
        //closing dialog detail info
        purchaseOrderPayableController.dialogStageDetal.close();
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDetailLayout;

    public TableView tableDataDetail;

    private void setTableDataDetail() {
        TableView<PurchaseOrderDetailCreated> tableView = new TableView();

        TableColumn<PurchaseOrderDetailCreated, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getCodeItem(),
                        param.getValue().dataPODetailProperty()));
        codeItem.setMinWidth(120);

        TableColumn<PurchaseOrderDetailCreated, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getItemName(),
                        param.getValue().dataPODetailProperty()));
        itemName.setMinWidth(140);

        TableColumn<PurchaseOrderDetailCreated, String> itemTypeHK = new TableColumn("Tipe");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-"
                                + (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getGuestStatus() //Guest
                                        ? " (G)" : " (NG)"),
                        param.getValue().dataPODetailProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<PurchaseOrderDetailCreated, String> itemTypeWH = new TableColumn("Tipe");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-"
                                + (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getGuestStatus() //Guest
                                        ? " (G)" : " (NG)"),
                        param.getValue().dataPODetailProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<PurchaseOrderDetailCreated, String> itemSistem = new TableColumn("(Sistem)");
        itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getItemName() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getItemName() : "-")
                        + "\n(" + (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getCodeItem() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getCodeItem() : "-") + ")",
                        param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().iditemProperty()));
        itemSistem.setMinWidth(140);

        TableColumn<PurchaseOrderDetailCreated, String> itemSupplier = new TableColumn("(Supplier)");
        itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataPODetail().getTblSupplierItem().getSupplierItemName() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getSupplierItemName() : "-")
                        + "\n(" + (param.getValue().getDataPODetail().getTblSupplierItem().getSupllierItemCode() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getSupllierItemCode() : "-") + ")",
                        param.getValue().getDataPODetail().getTblSupplierItem().idrelationProperty()));
        itemSupplier.setMinWidth(140);

        TableColumn<PurchaseOrderDetailCreated, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(itemSistem, itemSupplier);

        TableColumn<PurchaseOrderDetailCreated, String> itemCost = new TableColumn("Harga Barang");
        itemCost.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataPODetail().getItemCost()), param.getValue().dataPODetailProperty()));
        itemCost.setMinWidth(120);

        TableColumn<PurchaseOrderDetailCreated, String> itemDiscount = new TableColumn("Diskon (Satuan)");
        itemDiscount.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataPODetail().getItemDiscount()), param.getValue().dataPODetailProperty()));
        itemDiscount.setMinWidth(120);

        TableColumn<PurchaseOrderDetailCreated, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataPODetail().getItemQuantity()), param.getValue().dataPODetailProperty()));
        itemQuantity.setMinWidth(110);

        TableColumn<PurchaseOrderDetailCreated, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataPODetailProperty()));
        unitName.setMinWidth(110);

        TableColumn<PurchaseOrderDetailCreated, String> totalCost = new TableColumn("Total Harga");
        totalCost.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalCost(param.getValue().getDataPODetail())), param.getValue().dataPODetailProperty()));
        totalCost.setMinWidth(150);

        tableView.getColumns().addAll(itemTitle, itemCost, itemDiscount, itemQuantity, unitName, totalCost);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataPODetailCreated()));
        tableDataDetail = tableView;

        //attach to layout
        ancDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailLayout.getChildren().add(tableDataDetail);
    }

    private BigDecimal calculationTotalCost(TblPurchaseOrderDetail dataDetail) {
        return (dataDetail.getItemCost().subtract(dataDetail.getItemDiscount())).multiply(dataDetail.getItemQuantity());
    }

    private List<PurchaseOrderDetailCreated> loadAllDataPODetailCreated() {
        List<PurchaseOrderDetailCreated> list = new ArrayList<>();
        //data po - detail
        List<TblPurchaseOrderDetail> poList = purchaseOrderPayableController.getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(purchaseOrderPayableController.selectedDataPO.getIdpo());
        for (TblPurchaseOrderDetail poData : poList) {
            //set data po
            poData.setTblPurchaseOrder(purchaseOrderPayableController.selectedDataPO);
            //set data supplier item
            poData.setTblSupplierItem((purchaseOrderPayableController.getService().getDataSupplierItem(poData.getTblSupplierItem().getIdrelation())));
            //set data item
            poData.getTblSupplierItem().setTblItem(purchaseOrderPayableController.getService().getDataItem(poData.getTblSupplierItem().getTblItem().getIditem()));
            //set data unit
            poData.getTblSupplierItem().getTblItem().setTblUnit(purchaseOrderPayableController.getService().getDataUnit(poData.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
            //set data item type hk
            if (poData.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                poData.getTblSupplierItem().getTblItem().setTblItemTypeHk(purchaseOrderPayableController.getService().getDataItemTypeHK(poData.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type wh
            if (poData.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                poData.getTblSupplierItem().getTblItem().setTblItemTypeWh(purchaseOrderPayableController.getService().getDataItemTypeWH(poData.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        for (TblPurchaseOrderDetail poData : poList) {
            //set data purchase order detail created
            PurchaseOrderDetailCreated data = new PurchaseOrderDetailCreated();
            data.setDataPODetail(poData);
            data.setCreateStatus(true);
            //add to list
            list.add(data);
        }
        return list;
    }

    public class PurchaseOrderDetailCreated {

        private final ObjectProperty<TblPurchaseOrderDetail> dataPODteail = new SimpleObjectProperty<>();

        private final BooleanProperty createStatus = new SimpleBooleanProperty();

        public PurchaseOrderDetailCreated() {

            createStatus.addListener((obs, oldVal, newVal) -> {
                refreshDataBill();
            });

        }

        public ObjectProperty<TblPurchaseOrderDetail> dataPODetailProperty() {
            return dataPODteail;
        }

        public TblPurchaseOrderDetail getDataPODetail() {
            return dataPODetailProperty().get();
        }

        public void setDataPODetail(TblPurchaseOrderDetail dataPODetail) {
            dataPODetailProperty().set(dataPODetail);
        }

        public BooleanProperty createStatusProperty() {
            return createStatus;
        }

        public boolean getCreateStatus() {
            return createStatusProperty().get();
        }

        public void setCreateStatus(boolean createStatus) {
            createStatusProperty().set(createStatus);
        }

    }

    @FXML
    private AnchorPane ancDataTableDetail;

    public TableView tableReceivingDetail;

    public void setTableDataMIDetail() {
        ancDataTableDetail.getChildren().clear();

        tableReceivingDetail = new TableView();

        TableColumn<MIDetail, String> itemSistem = new TableColumn("(Sistem)");
        itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> getItemSystem(param.getValue().getDataMIDetail()),
                        param.getValue().dataMIDetailProperty()));
        itemSistem.setMinWidth(140);

        TableColumn<MIDetail, String> itemSupplier = new TableColumn("(Supplier)");
        itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> getItemSupplier(param.getValue().getDataMIDetail()),
                        param.getValue().dataMIDetailProperty()));
        itemSupplier.setMinWidth(140);

        TableColumn<MIDetail, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(itemSistem, itemSupplier);

        TableColumn<MIDetail, String> unitName = new TableColumn("Satuan");
        unitName.setMinWidth(120);
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(()
                        -> getItemUnitName(param.getValue().getDataMIDetail()),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIDetail, String> isBonus = new TableColumn("Bonus");
        isBonus.setMinWidth(60);
        isBonus.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> "", param.getValue().isBonusProperty()));
        isBonus.setCellFactory((TableColumn<MIDetail, String> param) -> new CheckBoxCell<>());

        TableColumn<MIDetail, String> quantityPO = new TableColumn("PO");
        quantityPO.setMinWidth(80);
        quantityPO.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getQuantityPO()),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIDetail, String> percentageAllowance = new TableColumn("%allowance");
        percentageAllowance.setMinWidth(80);
        percentageAllowance.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getAllowancePercentage(param.getValue().getDataMIDetail())) + "%",
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIDetail, String> quantityHasBeenGet = new TableColumn("Sudah\nDiterima");
        quantityHasBeenGet.setMinWidth(80);
        quantityHasBeenGet.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getQuatityItemHasBeenGet(param.getValue().getDataMIDetail())),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIDetail, String> quantityHasBeenRetur = new TableColumn("Sudah\nDi-Retur");
        quantityHasBeenRetur.setMinWidth(80);
        quantityHasBeenRetur.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getQuantityItemHasBeenRetur(param.getValue().getDataMIDetail())),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIDetail, String> quantityDifferent = new TableColumn("Selisih");
        quantityDifferent.setMinWidth(80);
        quantityDifferent.setCellValueFactory((TableColumn.CellDataFeatures<MIDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getQuantityItemDifferent(param.getValue())),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIDetail, String> quantity = new TableColumn("Jumlah");
        quantity.getColumns().addAll(quantityPO, percentageAllowance, quantityHasBeenGet, quantityHasBeenRetur, quantityDifferent);

        tableReceivingDetail.getColumns().addAll(isBonus, itemTitle, quantity, unitName);

        tableReceivingDetail.setItems(FXCollections.observableArrayList(loadAllDataMIDetail()));
        AnchorPane.setBottomAnchor(tableReceivingDetail, 0.0);
        AnchorPane.setLeftAnchor(tableReceivingDetail, 0.0);
        AnchorPane.setRightAnchor(tableReceivingDetail, 0.0);
        AnchorPane.setTopAnchor(tableReceivingDetail, 0.0);
        ancDataTableDetail.getChildren().add(tableReceivingDetail);
    }

    private String getItemSystem(TblMemorandumInvoiceDetail data) {
        String result = "-";
        if (data != null) {
            if (data.getTblSupplierItem() == null) {   //bonus
                result = data.getTblItem().getItemName()
                        + "\n(" + data.getTblItem().getCodeItem() + ")";
            } else {  //not bonus
                result = data.getTblSupplierItem().getTblItem().getItemName()
                        + "\n(" + data.getTblSupplierItem().getTblItem().getCodeItem() + ")";
            }
        }
        return result;
    }

    private String getItemSupplier(TblMemorandumInvoiceDetail data) {
        String result = "-";
        if (data != null) {
            if (data.getTblSupplierItem() == null) {   //bonus
                result = "-";
            } else {  //not bonus
                if (data.getTblSupplierItem().getSupplierItemName() != null) {
                    result = data.getTblSupplierItem().getSupplierItemName()
                            + "\n(" + data.getTblSupplierItem().getSupllierItemCode() + ")";
                }
            }
        }
        return result;
    }

    private String getItemUnitName(TblMemorandumInvoiceDetail data) {
        String result = "-";
        if (data != null) {
            if (data.getTblSupplierItem() == null) {   //bonus
                result = data.getTblItem().getTblUnit().getUnitName();
            } else {  //not bonus
                result = data.getTblSupplierItem().getTblItem().getTblUnit().getUnitName();
            }
        }
        return result;
    }

    private BigDecimal getAllowancePercentage(TblMemorandumInvoiceDetail data) {
        SysDataHardCode sdhc = purchaseOrderPayableController.getService().getDataSysDataHardCode(28);   //ReceivingPercentageAllowance = '28'
        if (sdhc != null
                && data.getTblItem() == null) {  //isn't a bonus
            return new BigDecimal(sdhc.getDataHardCodeValue());
        }
        return new BigDecimal("0");
    }

    private BigDecimal getQuatityItemHasBeenGet(TblMemorandumInvoiceDetail data) {
        if (data != null
                && data.getTblItem() == null) {  //isn't a bonus
            BigDecimal result = new BigDecimal("0");
            List<TblMemorandumInvoiceDetail> listMD = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(data.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    data.getTblSupplierItem().getIdrelation());
            for (TblMemorandumInvoiceDetail dataMD : listMD) {
                //+ (receiving(s))
                result = result.add(dataMD.getItemQuantity());
            }
            return result;
        }
        return new BigDecimal("0");
    }

    private BigDecimal getQuantityItemHasBeenRetur(TblMemorandumInvoiceDetail data) {
        if (data != null
                && data.getTblItem() == null) {  //isn't a bonus
            BigDecimal result = new BigDecimal("0");
            List<TblMemorandumInvoiceDetail> listMD = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(data.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    data.getTblSupplierItem().getIdrelation());
            for (TblMemorandumInvoiceDetail dataMD : listMD) {
                List<TblReturDetail> listRD = purchaseOrderPayableController.getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataMD.getTblMemorandumInvoice().getIdmi(),
                        data.getTblSupplierItem().getIdrelation());
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

    private BigDecimal getQuantityItemDifferent(MIDetail data) {
        return getQuatityItemHasBeenGet(data.getDataMIDetail())
                .subtract(getQuantityItemHasBeenRetur(data.getDataMIDetail()))
                .subtract(data.getQuantityPO());
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
                        checkBox.selectedProperty().bind(Bindings.isNotNull(((MIDetail) data).getDataMIDetail().tblItemProperty()));
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

    private List<MIDetail> loadAllDataMIDetail() {
        List<MIDetail> list = new ArrayList<>();
        //not a bonus
        List<TblPurchaseOrderDetail> poDetails = purchaseOrderPayableController.getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(purchaseOrderPayableController.selectedDataPO.getIdpo());
        for (TblPurchaseOrderDetail poDetail : poDetails) {
            TblMemorandumInvoiceDetail mid = new TblMemorandumInvoiceDetail();
            mid.setTblMemorandumInvoice(new TblMemorandumInvoice());
            mid.getTblMemorandumInvoice().setTblPurchaseOrder(poDetail.getTblPurchaseOrder());
            mid.setItemCost(poDetail.getItemCost());
            mid.setItemDiscount(poDetail.getItemDiscount());
            mid.setItemQuantity(poDetail.getItemQuantity());
            mid.setTblItem(null);
            mid.setTblSupplierItem(poDetail.getTblSupplierItem());
            list.add(new MIDetail(mid, false, poDetail.getItemQuantity()));
        }
        //a bonus
        List<TblMemorandumInvoice> mis = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceByIDPurchaseOrder(purchaseOrderPayableController.selectedDataPO.getIdpo());
        for (TblMemorandumInvoice mi : mis) {
            List<TblMemorandumInvoiceDetail> mids = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(mi.getIdmi());
            for (TblMemorandumInvoiceDetail mid : mids) {
                if (mid.getTblItem() != null) {   //bonus
                    list.add(new MIDetail(mid, true, new BigDecimal("0")));
                }
            }
        }
        return list;
    }

    public class MIDetail {

        private final ObjectProperty<TblMemorandumInvoiceDetail> dataMIDetail = new SimpleObjectProperty<>();

        private final BooleanProperty isBonus = new SimpleBooleanProperty();

        private final ObjectProperty<BigDecimal> quantityPO = new SimpleObjectProperty<>();

        public MIDetail(TblMemorandumInvoiceDetail dataMIDetail,
                boolean isBonus,
                BigDecimal quantityPO) {
            this.dataMIDetail.set(dataMIDetail);
            this.isBonus.set(isBonus);
            this.quantityPO.set(quantityPO);
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

        public BooleanProperty isBonusProperty() {
            return isBonus;
        }

        public boolean getIsBonus() {
            return isBonusProperty().get();
        }

        public void setIsBonus(boolean isBonus) {
            isBonusProperty().set(isBonus);
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

    }

    @FXML
    private AnchorPane ancDataTableHistory;

    public TableView tableReceivingHistory;

    public void setTableDataMIHistory() {
        ancDataTableHistory.getChildren().clear();

        tableReceivingHistory = new TableView();

        TableColumn<MIReturDetail, String> codeMIRetur = new TableColumn("No. MI/Retur");
        codeMIRetur.setMinWidth(100);
        codeMIRetur.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> getMIReturDetailCode(param.getValue()),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIReturDetail, String> date = new TableColumn("Tanggal\n Terima/Retur");
        date.setMinWidth(115);
        date.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> getMIReturDetailDate(param.getValue()),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIReturDetail, String> itemSistem = new TableColumn("(Sistem)");
        itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> getMIReturDetailItemSystem(param.getValue()),
                        param.getValue().dataMIDetailProperty()));
        itemSistem.setMinWidth(135);

        TableColumn<MIReturDetail, String> itemSupplier = new TableColumn("(Supplier)");
        itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> getMIReturDetailItemSystem(param.getValue()),
                        param.getValue().dataMIDetailProperty()));
        itemSupplier.setMinWidth(135);

        TableColumn<MIReturDetail, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(itemSistem, itemSupplier);

        TableColumn<MIReturDetail, String> unitName = new TableColumn("Satuan");
        unitName.setMinWidth(90);
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(()
                        -> getMIReturDetailUnitName(param.getValue()),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIReturDetail, String> isBonus = new TableColumn("Bonus");
        isBonus.setMinWidth(55);
        isBonus.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> "", param.getValue().isBonusProperty()));
        isBonus.setCellFactory((TableColumn<MIReturDetail, String> param) -> new CheckBoxCellRRD<>());

        TableColumn<MIReturDetail, String> barcode = new TableColumn("Barcode");
        barcode.setMinWidth(100);
        barcode.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDetailBarcode() != null
                                ? param.getValue().getDetailBarcode()
                                : "-",
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIReturDetail, String> expiredDate = new TableColumn("Tgl. Expr.");
        expiredDate.setMinWidth(100);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDetailExpiredDate() != null
                                ? ClassFormatter.dateFormate.format(param.getValue().getDetailExpiredDate())
                                : "-",
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIReturDetail, String> quantity = new TableColumn("Jumlah");
        quantity.setMinWidth(80);
        quantity.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> getMIReturDetailQuantity(param.getValue()),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIReturDetail, String> location = new TableColumn("Lokasi");
        location.setMinWidth(135);
        location.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> getMIReturDetailLocation(param.getValue()),
                        param.getValue().dataMIDetailProperty()));

        TableColumn<MIReturDetail, String> status = new TableColumn("Status");
        status.setMinWidth(100);
        status.setCellValueFactory((TableColumn.CellDataFeatures<MIReturDetail, String> param)
                -> Bindings.createStringBinding(() -> getMIReturDetailStatus(param.getValue()),
                        param.getValue().dataMIDetailProperty()));
        
        tableReceivingHistory.getColumns().addAll(date, codeMIRetur, isBonus, itemTitle, barcode, expiredDate, quantity, unitName, location, status);
        
        tableReceivingHistory.setItems(FXCollections.observableArrayList(loadAllDataMIReturDetail()));
        
        AnchorPane.setBottomAnchor(tableReceivingHistory, 0.0);
        AnchorPane.setLeftAnchor(tableReceivingHistory, 0.0);
        AnchorPane.setRightAnchor(tableReceivingHistory, 0.0);
        AnchorPane.setTopAnchor(tableReceivingHistory, 0.0);
        ancDataTableHistory.getChildren().add(tableReceivingHistory);
    }

    private String getMIReturDetailCode(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return miReturDetail.getDataMIDetail().getTblMemorandumInvoice().getCodeMi();
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return miReturDetail.getDataReturDetail().getTblRetur().getCodeRetur();
                }
            }
        }
        return "-";
    }

    private String getMIReturDetailDeliveryNumber(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return miReturDetail.getDataMIDetail().getTblMemorandumInvoice().getDeliveryNumber();
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return miReturDetail.getDataReturDetail().getTblRetur().getDeliveryNumber();
                }
            }
        }
        return "-";
    }

    private String getMIReturDetailDate(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return ClassFormatter.dateTimeFormate.format(miReturDetail.getDataMIDetail().getTblMemorandumInvoice().getReceivingDate());
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return ClassFormatter.dateTimeFormate.format(miReturDetail.getDataReturDetail().getTblRetur().getApprovedDate());
                }
            }
        }
        return "-";
    }

    private String getMIReturDetailItemSupplier(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return getItemSupplier(miReturDetail.getDataMIDetail());
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return getItemSupplier(miReturDetail.getDataReturDetail());
                }
            }
        }
        return "-";
    }

    private String getItemSupplier(TblReturDetail data) {
        String result = "-";
        if (data != null) {
            if (data.getTblSupplierItem().getSupplierItemName() != null) {
                result = data.getTblSupplierItem().getSupplierItemName()
                        + "\n(" + data.getTblSupplierItem().getSupllierItemCode() + ")";
            }
        }
        return result;
    }

    private String getMIReturDetailItemSystem(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return getItemSystem(miReturDetail.getDataMIDetail());
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return getItemSystem(miReturDetail.getDataReturDetail());
                }
            }
        }
        return "-";
    }

    private String getItemSystem(TblReturDetail data) {
        String result = "-";
        if (data != null) {
            result = data.getTblSupplierItem().getTblItem().getItemName()
                    + "\n(" + data.getTblSupplierItem().getTblItem().getCodeItem() + ")";
        }
        return result;
    }

    private String getMIReturDetailQuantity(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return ClassFormatter.decimalFormat.cFormat(miReturDetail.getDataMIDetail().getItemQuantity());
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return ClassFormatter.decimalFormat.cFormat(miReturDetail.getDataReturDetail().getItemQuantity());
                }
            }
        }
        return "-";
    }

    private String getMIReturDetailUnitName(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return getItemUnitName(miReturDetail.getDataMIDetail());
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return getItemUnitName(miReturDetail.getDataReturDetail());
                }
            }
        }
        return "-";
    }

    private String getItemUnitName(TblReturDetail data) {
        String result = "-";
        if (data != null) {
            result = data.getTblSupplierItem().getTblItem().getTblUnit().getUnitName();
        }
        return result;
    }

    private String getMIReturDetailLocation(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return getLocationName(miReturDetail.getDataMIDetail().getTblLocation());
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return getLocationName(miReturDetail.getDataReturDetail().getTblLocation());
                }
            }
        }
        return "-";
    }

    public String getLocationName(TblLocation location) {
        String name = "-";
        switch (location.getRefLocationType().getIdtype()) {
            case 0:
                TblRoom room = purchaseOrderPayableController.getService().getDataRoomByIDLocation(location.getIdlocation());
                if (room != null) {
                    name = room.getRoomName();
                }
                break;
            case 1:
                TblLocationOfWarehouse warehouse = purchaseOrderPayableController.getService().getDataWarehouseByIDLocation(location.getIdlocation());
                if (warehouse != null) {
                    name = warehouse.getWarehouseName();
                }
                break;
            case 2:
                TblLocationOfLaundry laundry = purchaseOrderPayableController.getService().getDataLaundryByIDLocation(location.getIdlocation());
                if (laundry != null) {
                    name = laundry.getLaundryName();
                }
                break;
            case 4:
                TblLocationOfBin bin = purchaseOrderPayableController.getService().getDataBinByIDLocation(location.getIdlocation());
                if (bin != null) {
                    name = bin.getBinName();
                }
                break;
        }
        return name;
    }

    private String getMIReturDetailStatus(MIReturDetail miReturDetail) {
        if (miReturDetail != null) {
            if (miReturDetail.getDataMIDetail() != null) {
                return "Penerimaan";
            } else {
                if (miReturDetail.getDataReturDetail() != null) {
                    return "Retur";
                }
            }
        }
        return "-";
    }
    
    public class CheckBoxCellRRD<S, T> extends TableCell<S, T> {

        private final JFXCheckBox checkBox;

        public CheckBoxCellRRD() {
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
                        checkBox.selectedProperty().bind(((MIReturDetail) data).isBonusProperty());
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

    private List<MIReturDetail> loadAllDataMIReturDetail() {
        List<MIReturDetail> list = new ArrayList<>();
        List<TblMemorandumInvoice> mis = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceByIDPurchaseOrder(purchaseOrderPayableController.selectedDataPO.getIdpo());
        for (TblMemorandumInvoice mi : mis) {
            //mi detail
            List<TblMemorandumInvoiceDetail> mids = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(mi.getIdmi());
            for (TblMemorandumInvoiceDetail mid : mids) {
                if (mid.getTblItem() != null) {   //bonus
                    if (mid.getTblItem().getPropertyStatus()) {   //Property barcode
                        List<TblMemorandumInvoiceDetailPropertyBarcode> midpbs = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(mid.getIddetail());
                        for (TblMemorandumInvoiceDetailPropertyBarcode midpb : midpbs) {
                            mid.setItemQuantity(new BigDecimal("1"));
                            list.add(new MIReturDetail(mid, null, true, midpb.getTblPropertyBarcode().getCodeBarcode(), null));
                        }
                    } else {
                        if (mid.getTblItem().getConsumable()) {   //Consumable
                            List<TblMemorandumInvoiceDetailItemExpiredDate> midieds = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(mid.getIddetail());
                            for (TblMemorandumInvoiceDetailItemExpiredDate midied : midieds) {
                                mid.setItemQuantity(midied.getItemQuantity());
                                list.add(new MIReturDetail(mid, null, true, null, midied.getTblItemExpiredDate().getItemExpiredDate()));
                            }
                        } else {  //Another
                            list.add(new MIReturDetail(mid, null, true, null, null));
                        }
                    }
                } else {
                    if (mid.getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property barcode
                        List<TblMemorandumInvoiceDetailPropertyBarcode> midpbs = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(mid.getIddetail());
                        for (TblMemorandumInvoiceDetailPropertyBarcode midpb : midpbs) {
                            mid.setItemQuantity(new BigDecimal("1"));
                            list.add(new MIReturDetail(mid, null, false, midpb.getTblPropertyBarcode().getCodeBarcode(), null));
                        }
                    } else {
                        if (mid.getTblSupplierItem().getTblItem().getConsumable()) {   //Consumable
                            List<TblMemorandumInvoiceDetailItemExpiredDate> midieds = purchaseOrderPayableController.getService().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(mid.getIddetail());
                            for (TblMemorandumInvoiceDetailItemExpiredDate midied : midieds) {
                                mid.setItemQuantity(midied.getItemQuantity());
                                list.add(new MIReturDetail(mid, null, false, null, midied.getTblItemExpiredDate().getItemExpiredDate()));
                            }
                        } else {  //Another
                            list.add(new MIReturDetail(mid, null, false, null, null));
                        }
                    }
                }
            }
            //retur detail
            List<TblReturDetail> returds = purchaseOrderPayableController.getService().getAllDataReturDetailByIDMemorandumInvoice(mi.getIdmi());
            for (TblReturDetail returd : returds) {
                if (returd.getTblRetur().getRefReturStatus().getIdstatus() == 1) {  //Disetujui = '1'
                    if (returd.getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property barcode
                        List<TblReturDetailPropertyBarcode> returdpbs = purchaseOrderPayableController.getService().getAllDataReturDetailPropertyBarcodeByIDReturDetail(returd.getIddetailRetur());
                        for (TblReturDetailPropertyBarcode returdpb : returdpbs) {
                            returd.setItemQuantity(new BigDecimal("1"));
                            list.add(new MIReturDetail(null, returd, false, returdpb.getTblPropertyBarcode().getCodeBarcode(), null));
                        }
                    } else {
                        if (returd.getTblSupplierItem().getTblItem().getConsumable()) {   //Consumable
                            List<TblReturDetailItemExpiredDate> returdieds = purchaseOrderPayableController.getService().getAllDataReturDetailItemExpiredDateByIDReturDetail(returd.getIddetailRetur());
                            for (TblReturDetailItemExpiredDate returdied : returdieds) {
                                returd.setItemQuantity(returdied.getItemQuantity());
                                list.add(new MIReturDetail(null, returd, false, null, returdied.getTblItemExpiredDate().getItemExpiredDate()));
                            }
                        } else {  //Another
                            list.add(new MIReturDetail(null, returd, false, null, null));
                        }
                    }
                }
            }
        }
        //sort by date
        sortingMIReturDetailByDateASC(list);
        return list;
    }

    private void sortingMIReturDetailByDateASC(List<MIReturDetail> miReturDetails) {
        boolean swapped = true;
        int j = 0;
        MIReturDetail tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < miReturDetails.size() - j; i++) {
                if (isAfter(miReturDetails.get(i), miReturDetails.get(i + 1))) {
                    tmp = miReturDetails.get(i);
                    miReturDetails.set(i, miReturDetails.get(i + 1));
                    miReturDetails.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }

    private boolean isAfter(MIReturDetail a, MIReturDetail b) {
        if (a.getDataMIDetail() != null) {
            if (b.getDataMIDetail() != null) {
                return a.getDataMIDetail().getTblMemorandumInvoice().getReceivingDate()
                        .after(b.getDataMIDetail().getTblMemorandumInvoice().getReceivingDate());
            } else {
                return a.getDataMIDetail().getTblMemorandumInvoice().getReceivingDate()
                        .after(b.getDataReturDetail().getTblRetur().getApprovedDate());
            }
        } else {
            if (b.getDataMIDetail() != null) {
                return a.getDataReturDetail().getTblRetur().getApprovedDate()
                        .after(b.getDataMIDetail().getTblMemorandumInvoice().getReceivingDate());
            } else {
                return a.getDataReturDetail().getTblRetur().getApprovedDate()
                        .after(b.getDataReturDetail().getTblRetur().getApprovedDate());
            }
        }
    }

    public class MIReturDetail {

        private final ObjectProperty<TblMemorandumInvoiceDetail> dataMIDetail = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReturDetail> dataReturDetail = new SimpleObjectProperty<>();

        private final BooleanProperty isBonus = new SimpleBooleanProperty();

        private final StringProperty detailBarcode = new SimpleStringProperty();

        private final ObjectProperty<java.util.Date> detailExpiredDate = new SimpleObjectProperty<>();

        public MIReturDetail(TblMemorandumInvoiceDetail dataMIDetail,
                TblReturDetail dataReturDetail,
                boolean isBonus,
                String detailbarcode,
                java.util.Date detailExpiredDate) {
            this.dataMIDetail.set(dataMIDetail);
            this.dataReturDetail.set(dataReturDetail);
            this.isBonus.set(isBonus);
            this.detailBarcode.set(detailbarcode);
            this.detailExpiredDate.set(detailExpiredDate);
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

        public ObjectProperty<TblReturDetail> dataReturDetailProperty() {
            return dataReturDetail;
        }

        public TblReturDetail getDataReturDetail() {
            return dataReturDetailProperty().get();
        }

        public void setDataReturDetail(TblReturDetail dataReturDetail) {
            dataReturDetailProperty().set(dataReturDetail);
        }

        public BooleanProperty isBonusProperty() {
            return isBonus;
        }

        public boolean getIsBonus() {
            return isBonusProperty().get();
        }

        public void setIsBonus(boolean isBonus) {
            isBonusProperty().set(isBonus);
        }

        public StringProperty detailBarcodeProperty() {
            return detailBarcode;
        }

        public String getDetailBarcode() {
            return detailBarcodeProperty().get();
        }

        public void setDetailBarcode(String detailBarcode) {
            detailBarcodeProperty().set(detailBarcode);
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

    @FXML
    private AnchorPane ancHotelTransactionPaymentLayout;

    public TableView tableHotelTransactionPaymentDetail;

    private void setTableDataHotelTransactionPaymentDetail() {
        tableHotelTransactionPaymentDetail = new TableView();

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelFinanceTransaction().getCodeTransaction(),
                        param.getValue().tblHotelFinanceTransactionProperty()));
        codePayment.setMinWidth(120);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> paymentNominal = new TableColumn("Nominal Pembayaran");
        paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblHotelFinanceTransaction().getIsReturnTransaction() ? "-" : "")
                        + ClassFormatter.currencyFormat.cFormat(param.getValue().getNominalTransaction()),
                        param.getValue().nominalTransactionProperty()));
        paymentNominal.setMinWidth(140);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> paymentDate = new TableColumn("Tanggal Pembayaran");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getTblHotelFinanceTransaction().getCreateDate()),
                        param.getValue().tblHotelFinanceTransactionProperty()));
        paymentDate.setMinWidth(140);

        tableHotelTransactionPaymentDetail.getColumns().addAll(codePayment, paymentNominal, paymentDate);
        tableHotelTransactionPaymentDetail.setItems(FXCollections.observableArrayList(loadAllDataHotelFinanceTransactionHotelPayable()));

        //attached to layout
        ancHotelTransactionPaymentLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableHotelTransactionPaymentDetail, 0.0);
        AnchorPane.setLeftAnchor(tableHotelTransactionPaymentDetail, 0.0);
        AnchorPane.setRightAnchor(tableHotelTransactionPaymentDetail, 0.0);
        AnchorPane.setTopAnchor(tableHotelTransactionPaymentDetail, 0.0);
        ancHotelTransactionPaymentLayout.getChildren().add(tableHotelTransactionPaymentDetail);
    }

    private List<TblHotelFinanceTransactionHotelPayable> loadAllDataHotelFinanceTransactionHotelPayable() {
        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
        if (purchaseOrderPayableController.selectedDataPO != null
                && purchaseOrderPayableController.selectedDataPO.getTblHotelPayable() != null) {
            list = purchaseOrderPayableController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(purchaseOrderPayableController.selectedDataPO.getTblHotelPayable().getIdhotelPayable());
        }
        return list;
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
        setDataReceivingAndReturSummaryHistorySplitpane();

        //init form
        initFormDataPO();

        //set selected data
        setSelectedDataToInputForm();
        
        spFormDataReceivingAndRetur.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataHistoryFormShowStatus.set(0.0);
        });
    }

    public PODetailInfoController(PurchaseOrderPayableController parentController) {
        purchaseOrderPayableController = parentController;
    }

    private final PurchaseOrderPayableController purchaseOrderPayableController;

    public PurchaseOrderPayableController getParentController() {
        return purchaseOrderPayableController;
    }

}
