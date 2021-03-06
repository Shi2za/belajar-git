/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_closing;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
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
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_purchase_order.FeaturePurchaseOrderController;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PurchaseOrderClosingController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPO;

    private DoubleProperty dataPOFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPOLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPOSplitpane() {
        spDataPO.setDividerPositions(1);

        dataPOFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPOFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPO.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPO.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPOFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPOLayout.setDisable(false);
                    tableDataPOLayoutDisableLayer.setDisable(true);
                    tableDataPOLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPOLayout.setDisable(true);
                    tableDataPOLayoutDisableLayer.setDisable(false);
                    tableDataPOLayoutDisableLayer.toFront();
                }
            }
        });

        dataPOFormShowStatus.set(0.0);
    }

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
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPOLayout;

    private ClassFilteringTable<TblPurchaseOrder> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataPO;

    private void initTableDataPO() {
        //set table
        setTableDataPO();
        //set control
        setTableControlDataPO();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPO, 15.0);
        AnchorPane.setLeftAnchor(tableDataPO, 15.0);
        AnchorPane.setRightAnchor(tableDataPO, 15.0);
        AnchorPane.setTopAnchor(tableDataPO, 15.0);
        ancBodyLayout.getChildren().add(tableDataPO);
    }

    private void setTableDataPO() {
        TableView<TblPurchaseOrder> tableView = new TableView();

        TableColumn<TblPurchaseOrder, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory(cellData -> cellData.getValue().codePoProperty());
        codePO.setMinWidth(100);

        TableColumn<TblPurchaseOrder, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSupplier().getSupplierName(), param.getValue().tblSupplierProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> codePR = new TableColumn("No. MR");
        codePR.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblPurchaseRequest()) != null
                                ? param.getValue().getTblPurchaseRequest().getCodePr()
                                : "-", param.getValue().tblPurchaseRequestProperty()));
        codePR.setMinWidth(100);

        TableColumn<TblPurchaseOrder, String> poDueDate = new TableColumn<>("Estimasi \n Tgl. Pengiriman");
        poDueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getPodueDate() != null)
                                ? ClassFormatter.dateFormate.format(param.getValue().getPodueDate())
                                : "-", param.getValue().podueDateProperty()));
        poDueDate.setMinWidth(120);

        TableColumn<TblPurchaseOrder, String> createdDateBy = new TableColumn<>("Buat");
        createdDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCreatedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCreatedBy() != null)
                                ? param.getValue().getTblEmployeeByCreatedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().createdDateProperty()));
        createdDateBy.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> approvedDateBy = new TableColumn<>("Persetujuan");
        approvedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getApprovedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByApprovedBy() != null)
                                ? param.getValue().getTblEmployeeByApprovedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().approvedDateProperty()));
        approvedDateBy.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> orderedDateBy = new TableColumn<>("Pemesanan");
        orderedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getOrderedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getOrderedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByOrderedBy() != null)
                                ? param.getValue().getTblEmployeeByOrderedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().orderedDateProperty()));
        orderedDateBy.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> canceledDateBy = new TableColumn<>("Pembatalan");
        canceledDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
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

        TableColumn<TblPurchaseOrder, String> dateByTitle = new TableColumn("Tanggal-Oleh");
        dateByTitle.getColumns().addAll(createdDateBy, approvedDateBy, orderedDateBy);

//        TableColumn<TblPurchaseOrder, String> poStatus = new TableColumn<>("Status");
//        poStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefPurchaseOrderStatus().getStatusName(),
//                        param.getValue().refPurchaseOrderStatusProperty()));
//        poStatus.setMinWidth(90);
        TableColumn<TblPurchaseOrder, String> poItemmArriveStatus = new TableColumn<>("Penerimaan");
        poItemmArriveStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefPurchaseOrderItemArriveStatus().getStatusName(),
                        param.getValue().refPurchaseOrderItemArriveStatusProperty()));
        poItemmArriveStatus.setMinWidth(110);

        TableColumn<TblPurchaseOrder, String> poPaymentStatus = new TableColumn<>("Pembayaran");
        poPaymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefPurchaseOrderPaymentStatus().getStatusName(),
                        param.getValue().refPurchaseOrderPaymentStatusProperty()));
        poPaymentStatus.setMinWidth(110);

        TableColumn<TblPurchaseOrder, String> statusTitle = new TableColumn("Status");
        statusTitle.getColumns().addAll(poItemmArriveStatus, poPaymentStatus);

        TableColumn<TblPurchaseOrder, String> revisionInfo = new TableColumn<>("Keterangan (Revisi)");
        revisionInfo.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> getDataRevisionInfo(param.getValue()),
                        param.getValue().idpoProperty()));
        revisionInfo.setMinWidth(250);

        tableView.getColumns().addAll(codePO, supplierName, codePR, poDueDate, dateByTitle, statusTitle);

        tableView.setItems(loadAllDataPO());

        tableView.setRowFactory(tv -> {
            TableRow<TblPurchaseOrder> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPOUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
                                dataPOClosingCreateHandle();
                            } else {
                                dataPOShowHandle();
                            }
                        }
                    }
                }
            });
            return row;
        });

        tableDataPO = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblPurchaseOrder.class,
                tableDataPO.getTableView(),
                tableDataPO.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getDataRevisionInfo(TblPurchaseOrder dataPO) {
        String result = "";
//        if(dataPO != null){
//            List<TblPurchaseOrderRevisionHistory> dataPORevisions = getService().getAllDataPurchaseOrderRevisionByIDPurchaseOrderSource(dataPO.getIdpo());
//            if(!dataPORevisions.isEmpty()){
//                for(TblPurchaseOrderRevisionHistory dataPORevision : dataPORevisions){
//                    
//                }
//            }
//        }
        return result.equals("") ? "-" : result;
    }

    private void setTableControlDataPO() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tutup");
            buttonControl.setOnMouseClicked((e) -> {
                //listener closing
                dataPOClosingCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPO.addButtonControl(buttonControls);
    }

    private ObservableList<TblPurchaseOrder> loadAllDataPO() {
        List<TblPurchaseOrder> list = parentController.getFPurchaseOrderManager().getAllDataPurchaseOrder();
//        for (TblPurchaseOrder data : list) {
//            //data purchase request
//            if (data.getTblPurchaseRequest() != null) {
//                data.setTblPurchaseRequest(parentController.getFPurchaseOrderManager().getDataPurchaseRequest(data.getTblPurchaseRequest().getIdpr()));
//            }
//            //data retur
//            if (data.getTblRetur() != null) {
//                data.setTblRetur(parentController.getFPurchaseOrderManager().getDataRetur(data.getTblRetur().getIdretur()));
//            }
//            //data hotel payable
//            if (data.getTblHotelPayable() != null) {
//                data.setTblHotelPayable(parentController.getFPurchaseOrderManager().getDataHotelPayable(data.getTblHotelPayable().getIdhotelPayable()));
//            }
//            //data supplier
//            data.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(data.getTblSupplier().getIdsupplier()));
//            //data po - status
//            data.setRefPurchaseOrderStatus(parentController.getFPurchaseOrderManager().getDataPurchaseOrderStatus(data.getRefPurchaseOrderStatus().getIdstatus()));
//            //data po - item-arrive status
//            data.setRefPurchaseOrderItemArriveStatus(parentController.getFPurchaseOrderManager().getDataPurchaseOrderItemArriveStatus(data.getRefPurchaseOrderItemArriveStatus().getIdstatus()));
//            //data po - payment status
//            data.setRefPurchaseOrderPaymentStatus(parentController.getFPurchaseOrderManager().getDataPurchaseOrderPaymentStatus(data.getRefPurchaseOrderPaymentStatus().getIdstatus()));
//        }
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getRefPurchaseOrderStatus().getIdstatus() != 5) { //Dipesan = '5'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

//    @FXML
//    private TitledPane tpPO;
//
//    @FXML
//    private TitledPane tpRecevingAndRetur;
//
//    @FXML
//    private TitledPane tpPOPayment;
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

//    @FXML
//    private ScrollPane spFormDataPO;
//    @FXML
//    private JFXTextField txtCodePO;
    @FXML
    private JFXTextField txtDueDate;

//    @FXML
//    private JFXTextField txtPR;
//
//    @FXML
//    private JFXTextField txtSupplierName;
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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnBack;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblPurchaseOrder selectedData;

    private void initFormDataPO() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
//            spFormDataPO.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPO.setOnScroll((ScrollEvent scroll) -> {
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

        spnTaxPercentage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 100.00));
        spnTaxPercentage.setEditable(false);

        btnSave.setTooltip(new Tooltip("Tutup (Data Purchase Order)"));
        btnSave.setOnAction((e) -> {
            dataPOClosingSaveHandle();
        });

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
        //                ,
        //                txtTotalHotelPayableBySupplier,
        //                txtTotalHotelPayableBySystem,
        //                txtTotalHotelPayment,
        //                txtTotalHotelRestOfBill
        );
    }

    private void setSelectedDataToInputForm() {
        //label code
        lblCodeData.setText((selectedData.getCodePo() != null
                ? (selectedData.getCodePo()
                + (selectedData.getTblPurchaseRequest() != null
                        ? (" (" + selectedData.getTblPurchaseRequest().getCodePr() + ") ")
                        : "")
                + (selectedData.getTblSupplier() != null
                        ? (" - " + selectedData.getTblSupplier().getSupplierName())
                        : ""))
                : ""));
        //data PO
//        tpPO.setText("Data Purchase Order");
//        tpPO.setExpanded(true);
        setSelectedDataToInputFormPO();
        //data receiving & retur
//        tpRecevingAndRetur.setText("Data Penerimaan & Retur (" + selectedData.getRefPurchaseOrderItemArriveStatus().getStatusName() + ")");
        setSelectedDataToInputFormMIAndRetur();
        //data PO payment
//        tpPOPayment.setText("Data Pembayaran (" + selectedData.getRefPurchaseOrderPaymentStatus().getStatusName() + ")");
        setSelectedDataToInputFormPOPayment();

        tabPaneFormLayout.getSelectionModel().selectFirst();
    }

    private void setSelectedDataToInputFormPO() {
//        txtCodePO.textProperty().bindBidirectional(selectedData.codePoProperty());

        if (selectedData.getPodueDate() != null) {
            txtDueDate.setText(ClassFormatter.dateFormate.format(selectedData.getPodueDate()));
        } else {
            txtDueDate.setText("-");
        }

//        txtPR.textProperty().bindBidirectional(selectedData.getTblPurchaseRequest().codePrProperty());
//        txtSupplierName.textProperty().bindBidirectional(selectedData.getTblSupplier().supplierNameProperty());
        txtPOPaymentTypeInformation.textProperty().bindBidirectional(selectedData.popaymentTypeInformationProperty());
        txtPONote.textProperty().bindBidirectional(selectedData.canceledNoteProperty());

        Bindings.bindBidirectional(txtDiscount.textProperty(), selectedData.nominalDiscountProperty(), new ClassFormatter.CBigDecimalStringConverter());
        selectedData.nominalDiscountProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        spnTaxPercentage.getValueFactory().setValue(selectedData.getTaxPecentage() != null
                ? selectedData.getTaxPecentage().doubleValue() * 100
                : 0);
        spnTaxPercentage.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setTaxPecentage(BigDecimal.valueOf(newVal / 100));
            }
        });
        selectedData.taxPecentageProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        Bindings.bindBidirectional(txtDeliveryCost.textProperty(), selectedData.deliveryCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        selectedData.deliveryCostProperty().addListener((obs, oldVal, newVal) -> {
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
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null
                && selectedData.getTblHotelPayable().getTblHotelInvoice() != null) {
            return selectedData.getTblHotelPayable().getTblHotelInvoice().getCodeHotelInvoice();
        }
        return "-";
    }

    private String getPaymentDueDate() {
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null
                && selectedData.getTblHotelPayable().getTblHotelInvoice() != null) {
            return ClassFormatter.dateFormate.format(selectedData.getTblHotelPayable().getTblHotelInvoice().getDueDate());
        }
        return "-";
    }

    private BigDecimal getTotalHotelPayableBySupplier() {
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null
                && selectedData.getTblHotelPayable().getTblHotelInvoice() != null) {
            return selectedData.getTblHotelPayable().getHotelPayableNominal();
        }
        return new BigDecimal("0");
    }

    private BigDecimal getTotalHotelPayableBySystem() {
        BigDecimal result = new BigDecimal("0");
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null
                && selectedData.getTblHotelPayable().getTblHotelInvoice() != null) {
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
            result = result.subtract(selectedData.getNominalDiscount());
            result = ((new BigDecimal("1")).add((selectedData.getTaxPecentage().divide(new BigDecimal("100"))))).multiply(result);
            result = result.add(selectedData.getDeliveryCost());
        }
        return result;
    }

    private BigDecimal getTotalHotelPayment() {
        BigDecimal result = new BigDecimal("0");
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null
                && selectedData.getTblHotelPayable().getTblHotelInvoice() != null) {
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getFPurchaseOrderManager().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(selectedData.getTblHotelPayable().getIdhotelPayable());
            for (TblHotelFinanceTransactionHotelPayable data : list) {
                result = result.add(data.getNominalTransaction());
            }
        }
        return result;
    }

    private BigDecimal getTotalHotelRestOfBill() {
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null
                && selectedData.getTblHotelPayable().getTblHotelInvoice() != null) {
            return getTotalHotelPayableBySupplier()
                    .subtract(getTotalHotelPayment());
        }
        return new BigDecimal("0");
    }

    private String getTotalHotelPaymentStatus() {
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null
                && selectedData.getTblHotelPayable().getTblHotelInvoice() != null) {
            return "(" + selectedData.getTblHotelPayable().getRefFinanceTransactionStatus().getStatusName() + ")";
        }
        return "";
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        txtCodePO.setDisable(true);
        txtSubTotal.setDisable(true);
        txtTax.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataPO,
                dataInputStatus == 3 || dataInputStatus == 6,
                //                txtCodePO,
                txtSubTotal,
                txtTax
        );

        btnSave.setVisible(dataInputStatus != 3);
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
        return (selectedData.getTaxPecentage()).multiply(calculationSubTotal().subtract(selectedData.getNominalDiscount()));
//        return (selectedData.getTaxPecentage()).multiply(calculationSubTotal());
    }

    private BigDecimal calculationTotal() {
        return (((new BigDecimal("1")).add(selectedData.getTaxPecentage())).multiply(calculationSubTotal().subtract(selectedData.getNominalDiscount())))
                .add(selectedData.getDeliveryCost());
//        return (((new BigDecimal("1")).add(selectedData.getTaxPecentage())).multiply(calculationSubTotal()))
//                .subtract(selectedData.getNominalDiscount())
//                .add(selectedData.getDeliveryCost());
    }

    public void refreshDataBill() {
        txtSubTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationSubTotal()));
        txtTax.setText(ClassFormatter.currencyFormat.cFormat(calculationTax()));
        lblTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationTotal()));
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPOShowHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFPurchaseOrderManager().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
            if (selectedData.getTblPurchaseRequest() != null) {
                selectedData.setTblPurchaseRequest(parentController.getFPurchaseOrderManager().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
            }
            selectedData.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
            selectedData.setRefPurchaseOrderStatus(parentController.getFPurchaseOrderManager().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
            setSelectedDataToInputForm();
            dataPOFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPOUnshowHandle() {
        refreshDataTablePO();
        dataPOFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPOClosingCreateHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToClosing((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                purchaseOrderClosingHandle();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat ditutup..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void purchaseOrderClosingHandle() {
        dataInputStatus = 6;    //Closing
        selectedData = parentController.getFPurchaseOrderManager().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        if (selectedData.getTblPurchaseRequest() != null) {
            selectedData.setTblPurchaseRequest(parentController.getFPurchaseOrderManager().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
        }
        selectedData.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        selectedData.setRefPurchaseOrderStatus(parentController.getFPurchaseOrderManager().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data purchase order
        dataPOFormShowStatus.set(0.0);
        dataPOFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Tutup (Data Purchase Order)"));
    }

    private boolean checkDataPOEnableToClosing(TblPurchaseOrder dataPO) {
        return true;
    }

    private void dataPOClosingSaveHandle() {
        Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk menutup data PO?", "");
        if (alert.getResult() == ButtonType.OK) {
            if (parentController.getFPurchaseOrderManager().updateClosingDataPurchaseOrder((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data PO berhasil ditutup..!!", null);
                //refresh data from table & close form data purchase order
                refreshDataTablePO();
                dataPOFormShowStatus.set(0.0);
                //set unsaving data input -> 'false'
                ClassSession.unSavingDataInput.set(false);
            } else {
                HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data PO gagal ditutup..!!", parentController.getFPurchaseOrderManager().getErrorMessage());
            }
        }
    }

    private void dataPOClosingCancelHandle() {
        //refresh data from table & close form data purchase order
        refreshDataTablePO();
        dataPOFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTablePO() {
        tableDataPO.getTableView().setItems(loadAllDataPO());
        cft.refreshFilterItems(tableDataPO.getTableView().getItems());
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
        List<TblPurchaseOrderDetail> poList = parentController.getFPurchaseOrderManager().getAllDataPurchaseOrderDetailByIDPurchaseOrder(selectedData.getIdpo());
        for (TblPurchaseOrderDetail poData : poList) {
            //set data po
            poData.setTblPurchaseOrder(selectedData);
            //set data supplier item
            poData.setTblSupplierItem((parentController.getFPurchaseOrderManager().getDataSupplierItem(poData.getTblSupplierItem().getIdrelation())));
            //set data item
            poData.getTblSupplierItem().setTblItem(parentController.getFPurchaseOrderManager().getDataItem(poData.getTblSupplierItem().getTblItem().getIditem()));
            //set data unit
            poData.getTblSupplierItem().getTblItem().setTblUnit(parentController.getFPurchaseOrderManager().getDataUnit(poData.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
            //set data item type hk
            if (poData.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                poData.getTblSupplierItem().getTblItem().setTblItemTypeHk(parentController.getFPurchaseOrderManager().getDataItemTypeHK(poData.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type wh
            if (poData.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                poData.getTblSupplierItem().getTblItem().setTblItemTypeWh(parentController.getFPurchaseOrderManager().getDataItemTypeWH(poData.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
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
        SysDataHardCode sdhc = parentController.getFPurchaseOrderManager().getDataSysDataHardCode(28);   //ReceivingPercentageAllowance = '28'
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
            List<TblMemorandumInvoiceDetail> listMD = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(data.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
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
            List<TblMemorandumInvoiceDetail> listMD = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(data.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    data.getTblSupplierItem().getIdrelation());
            for (TblMemorandumInvoiceDetail dataMD : listMD) {
                List<TblReturDetail> listRD = parentController.getFPurchaseOrderManager().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(dataMD.getTblMemorandumInvoice().getIdmi(),
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
        List<TblPurchaseOrderDetail> poDetails = parentController.getFPurchaseOrderManager().getAllDataPurchaseOrderDetailByIDPurchaseOrder(selectedData.getIdpo());
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
        List<TblMemorandumInvoice> mis = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceByIDPurchaseOrder(selectedData.getIdpo());
        for (TblMemorandumInvoice mi : mis) {
            List<TblMemorandumInvoiceDetail> mids = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(mi.getIdmi());
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
        unitName.setMinWidth(100);
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
        barcode.setMinWidth(110);
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

        tableReceivingHistory.getColumns().addAll(date, codeMIRetur, isBonus, itemTitle, barcode, expiredDate, quantity, unitName, location);

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
                TblRoom room = parentController.getFPurchaseOrderManager().getDataRoomByIDLocation(location.getIdlocation());
                if (room != null) {
                    name = room.getRoomName();
                }
                break;
            case 1:
                TblLocationOfWarehouse warehouse = parentController.getFPurchaseOrderManager().getDataWarehouseByIDLocation(location.getIdlocation());
                if (warehouse != null) {
                    name = warehouse.getWarehouseName();
                }
                break;
            case 2:
                TblLocationOfLaundry laundry = parentController.getFPurchaseOrderManager().getDataLaundryByIDLocation(location.getIdlocation());
                if (laundry != null) {
                    name = laundry.getLaundryName();
                }
                break;
            case 4:
                TblLocationOfBin bin = parentController.getFPurchaseOrderManager().getDataBinByIDLocation(location.getIdlocation());
                if (bin != null) {
                    name = bin.getBinName();
                }
                break;
        }
        return name;
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
        List<TblMemorandumInvoice> mis = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceByIDPurchaseOrder(selectedData.getIdpo());
        for (TblMemorandumInvoice mi : mis) {
            //mi detail
            List<TblMemorandumInvoiceDetail> mids = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(mi.getIdmi());
            for (TblMemorandumInvoiceDetail mid : mids) {
                if (mid.getTblItem() != null) {   //bonus
                    if (mid.getTblItem().getPropertyStatus()) {   //Property barcode
                        List<TblMemorandumInvoiceDetailPropertyBarcode> midpbs = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(mid.getIddetail());
                        for (TblMemorandumInvoiceDetailPropertyBarcode midpb : midpbs) {
                            mid.setItemQuantity(new BigDecimal("1"));
                            list.add(new MIReturDetail(mid, null, true, midpb.getTblPropertyBarcode().getCodeBarcode(), null));
                        }
                    } else {
                        if (mid.getTblItem().getConsumable()) {   //Consumable
                            List<TblMemorandumInvoiceDetailItemExpiredDate> midieds = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(mid.getIddetail());
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
                        List<TblMemorandumInvoiceDetailPropertyBarcode> midpbs = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(mid.getIddetail());
                        for (TblMemorandumInvoiceDetailPropertyBarcode midpb : midpbs) {
                            mid.setItemQuantity(new BigDecimal("1"));
                            list.add(new MIReturDetail(mid, null, false, midpb.getTblPropertyBarcode().getCodeBarcode(), null));
                        }
                    } else {
                        if (mid.getTblSupplierItem().getTblItem().getConsumable()) {   //Consumable
                            List<TblMemorandumInvoiceDetailItemExpiredDate> midieds = parentController.getFPurchaseOrderManager().getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(mid.getIddetail());
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
            List<TblReturDetail> returds = parentController.getFPurchaseOrderManager().getAllDataReturDetailByIDMemorandumInvoice(mi.getIdmi());
            for (TblReturDetail returd : returds) {
                if (returd.getTblRetur().getRefReturStatus().getIdstatus() == 1) {  //Disetujui = '1'
                    if (returd.getTblSupplierItem().getTblItem().getPropertyStatus()) {   //Property barcode
                        List<TblReturDetailPropertyBarcode> returdpbs = parentController.getFPurchaseOrderManager().getAllDataReturDetailPropertyBarcodeByIDReturDetail(returd.getIddetailRetur());
                        for (TblReturDetailPropertyBarcode returdpb : returdpbs) {
                            returd.setItemQuantity(new BigDecimal("1"));
                            list.add(new MIReturDetail(null, returd, false, returdpb.getTblPropertyBarcode().getCodeBarcode(), null));
                        }
                    } else {
                        if (returd.getTblSupplierItem().getTblItem().getConsumable()) {   //Consumable
                            List<TblReturDetailItemExpiredDate> returdieds = parentController.getFPurchaseOrderManager().getAllDataReturDetailItemExpiredDateByIDReturDetail(returd.getIddetailRetur());
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
        if (selectedData != null
                && selectedData.getTblHotelPayable() != null) {
            list = parentController.getFPurchaseOrderManager().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(selectedData.getTblHotelPayable().getIdhotelPayable());
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
        setDataPOSplitpane();
        setDataReceivingAndReturSummaryHistorySplitpane();

        //init table
        initTableDataPO();

        //init form
        initFormDataPO();

        spDataPO.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPOFormShowStatus.set(0.0);
        });
        spFormDataReceivingAndRetur.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataHistoryFormShowStatus.set(0.0);
        });
    }

    public PurchaseOrderClosingController(FeaturePurchaseOrderController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePurchaseOrderController parentController;

    public FPurchaseOrderManager getService() {
        return parentController.getFPurchaseOrderManager();
    }

}
