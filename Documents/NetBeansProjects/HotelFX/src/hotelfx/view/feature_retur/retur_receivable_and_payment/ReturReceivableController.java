/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_receivable_and_payment;

import com.jfoenix.controls.JFXButton;
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
import hotelfx.helper.PrintModel.ClassPrintInvoiceRetur;
import hotelfx.helper.PrintModel.ClassPrintInvoiceReturDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FReturManager;
import hotelfx.view.DashboardController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
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
public class ReturReceivableController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataReturReceivable;

    private DoubleProperty dataReturReceivableFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReturReceivableLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReturReceivableSplitpane() {
        spDataReturReceivable.setDividerPositions(1);

        dataReturReceivableFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReturReceivableFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataReturReceivable.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataReturReceivable.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReturReceivableFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataReturReceivableLayout.setDisable(false);
            tableDataReturReceivableLayoutDisableLayer.setDisable(true);
            tableDataReturReceivableLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReturReceivableLayout.setDisable(false);
                    tableDataReturReceivableLayoutDisableLayer.setDisable(true);
                    tableDataReturReceivableLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReturReceivableLayout.setDisable(true);
                    tableDataReturReceivableLayoutDisableLayer.setDisable(false);
                    tableDataReturReceivableLayoutDisableLayer.toFront();
                }
            }
        });

        dataReturReceivableFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataReturReceivableLayout;

    private ClassFilteringTable<TblSupplier> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataReturReceivable;

    private void initTableDataReturReceivable() {
        //set table
        setTableDataReturReceivable();
        //set control
        setTableControlDataReturReceivable();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataReturReceivable, 15.0);
        AnchorPane.setLeftAnchor(tableDataReturReceivable, 15.0);
        AnchorPane.setRightAnchor(tableDataReturReceivable, 15.0);
        AnchorPane.setTopAnchor(tableDataReturReceivable, 15.0);
        ancBodyLayout.getChildren().add(tableDataReturReceivable);
    }

    private void setTableDataReturReceivable() {
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

        TableColumn<TblSupplier, String> totalHotelReceivable = new TableColumn("Total Tagihan");
        totalHotelReceivable.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplier, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(getTotalHotelReceivable(param.getValue())),
                        param.getValue().idsupplierProperty()));
        totalHotelReceivable.setMinWidth(140);

        TableColumn<TblSupplier, String> minDueDate = new TableColumn("Tgl. Estimasi Bayar\n"
                + "(paling dekat)");
        minDueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplier, String> param)
                -> Bindings.createStringBinding(() -> getMinDueDate(param.getValue()),
                        param.getValue().idsupplierProperty()));
        minDueDate.setMinWidth(160);

        tableView.getColumns().addAll(codeSupplier, supplierName, picTitle, totalHotelReceivable, minDueDate);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataSupplier()));

        tableView.setRowFactory(tv -> {
            TableRow<TblSupplier> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataHotelReceivableUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataHotelReceivableUpdateHandle();
                            } else {
                                dataHotelReceivableShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataHotelReceivableUpdateHandle();
//                            } else {
//                                dataHotelReceivableShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataReturReceivable = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblSupplier.class,
                tableDataReturReceivable.getTableView(),
                tableDataReturReceivable.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private BigDecimal getTotalHotelReceivable(TblSupplier supplier) {
        BigDecimal result = new BigDecimal("0");
        if (supplier != null) {
            List<TblRetur> returList = loadAllDataRetur(supplier);
            for (TblRetur returData : returList) {
                if (returData.getTblHotelReceivable() != null) {
                    result = result.add(returData.getTblHotelReceivable().getHotelReceivableNominal());
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(returData.getTblHotelReceivable().getIdhotelReceivable());
                    for (TblHotelFinanceTransactionHotelReceivable data : list) {
                        if(data.getTblHotelFinanceTransaction().getIsReturnTransaction()){
                            result = result.add(data.getNominalTransaction());
                        }else{
                            result = result.subtract(data.getNominalTransaction());
                        }
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal getTotalHotelReceivableJustHaveCodeInvoice(TblSupplier supplier) {
        BigDecimal result = new BigDecimal("0");
        if (supplier != null) {
            List<TblRetur> returList = loadAllDataRetur(supplier);
            for (TblRetur returData : returList) {
                if (returData.getTblHotelReceivable() != null) {
                    result = result.add(returData.getTblHotelReceivable().getHotelReceivableNominal());
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(returData.getTblHotelReceivable().getIdhotelReceivable());
                    for (TblHotelFinanceTransactionHotelReceivable data : list) {
                        if(data.getTblHotelFinanceTransaction().getIsReturnTransaction()){
                            result = result.add(data.getNominalTransaction());
                        }else{
                            result = result.subtract(data.getNominalTransaction());
                        }
                    }
                }
            }
        }
        return result;
    }

    private String getMinDueDate(TblSupplier supplier) {
        if (supplier != null) {
            LocalDate localDate = null;
            List<TblRetur> returList = loadAllDataRetur(supplier);
            for (TblRetur returData : returList) {
                if (returData.getTblHotelReceivable() != null
                        && returData.getTblHotelReceivable().getTblHotelInvoice() != null
                        && returData.getTblHotelReceivable().getTblHotelInvoice().getDueDate() != null) {
                    LocalDate countDate = LocalDate.of(returData.getTblHotelReceivable().getTblHotelInvoice().getDueDate().getYear() + 1900,
                            returData.getTblHotelReceivable().getTblHotelInvoice().getDueDate().getMonth() + 1,
                            returData.getTblHotelReceivable().getTblHotelInvoice().getDueDate().getDate());
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

    public BigDecimal calculationTotalBill(TblRetur retur) {
        BigDecimal result = new BigDecimal("0");
        if (retur != null) {
            if (retur.getTblHotelReceivable() != null) {
                result = retur.getTblHotelReceivable().getHotelReceivableNominal();
            }
        }
        return result;
    }

    public BigDecimal calculationTotalPayment(TblRetur retur) {
        BigDecimal result = new BigDecimal("0");
        if (retur != null
                && retur.getTblHotelReceivable() != null) {
            List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(retur.getTblHotelReceivable().getIdhotelReceivable());
            for (TblHotelFinanceTransactionHotelReceivable data : list) {
                if(data.getTblHotelFinanceTransaction().getIsReturnTransaction()){
                    result = result.subtract(data.getNominalTransaction());
                }else{
                    result = result.add(data.getNominalTransaction());
                }
            }
        }
        return result;
    }

    public BigDecimal calculationTotalRestOfBill(TblRetur retur) {
        return calculationTotalBill(retur).subtract(calculationTotalPayment(retur));
    }

    public String getCodeInvoice(TblHotelReceivable dataHotelReceivable) {
        String result = "-";
        if (dataHotelReceivable != null
                && dataHotelReceivable.getTblHotelInvoice() != null) {
            dataHotelReceivable.setTblHotelInvoice(getService().getDataHotelInvoice(dataHotelReceivable.getTblHotelInvoice().getIdhotelInvoice()));
            result = dataHotelReceivable.getTblHotelInvoice().getCodeHotelInvoice();
        }
        return result;
    }

    private void setTableControlDataReturReceivable() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Set Invoice / Buat Transaksi Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataHotelReceivableUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataReturReceivable.addButtonControl(buttonControls);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataReturReceivable;

    @FXML
    private ScrollPane spFormDataReturReceivable;

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
    private Label lblTotalHotelReceivable;

    @FXML
    private AnchorPane ancTableDetailLayout;

    @FXML
    private JFXTextField txtCodeInvoice;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    private JFXDatePicker dtpIssueDate;

    @FXML
    private JFXDatePicker dtpDueDate;

    @FXML
    private JFXTextArea txtTerm;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private AnchorPane ancTableHotelFinanceTransaction;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblSupplier selectedData;

    public TblRetur selectedDataRetur;

    private void initFormDataReturReceivable() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataReturReceivable.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataReturReceivable.setOnScroll((ScrollEvent scroll) -> {
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

        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            //back handle
            dataReturReceivableBackHandle();
        });

        btnSave.setTooltip(new Tooltip("Simpan (data invoice)"));
        btnSave.setOnAction((e) -> {
            //save invoice handle
            dataInvoiceSaveHandle();
        });

        btnPrint.setTooltip(new Tooltip("Cetak"));
        btnPrint.setOnAction((e) -> {
            //print invoice handle
            dataInvoicePrintHandle();
        });

        //data invoice - event
        txtCodeInvoice.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtSubject.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        dtpIssueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        dtpDueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtTerm.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
    }

    /**
     * TABLE DATA RETUR RECEIVABLE
     */
    
    private final PseudoClass notPaidPseudoClass = PseudoClass.getPseudoClass("notpaid");
    
    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodeSupplier() + " - " + selectedData.getSupplierName());

        txtSupplier.setText(selectedData.getCodeSupplier() + " - " + selectedData.getSupplierName());
        txtPICName.setText(selectedData.getPicname());
        txtPICPhoneNumber.setText(selectedData.getPicphoneNumber());

        lblTotalHotelReceivable.setText(ClassFormatter.currencyFormat.format(getTotalHotelReceivable(selectedData)));

        initTableDataDetail(selectedData);

        //set selected data input form detail
        setSelectedDataToInputFormDetail(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1
                ? ((TblRetur) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelReceivable()
                : null);

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ancTableDetailLayout.setDisable(dataInputStatus == 3);

        gpFormDataReturReceivable.setDisable(dataInputStatus == 3);
        txtCodeInvoice.setDisable(true);

        btnSave.setVisible(dataInputStatus != 3);
        btnPrint.setDisable(false);
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
        TableView<TblRetur> tableView = new TableView();

        TableColumn<TblRetur, String> codeRetur = new TableColumn("No. Retur");
        codeRetur.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeRetur(),
                        param.getValue().codeReturProperty()));
        codeRetur.setMinWidth(85);

        TableColumn<TblRetur, String> totalBill = new TableColumn("Total Tagihan (Retur)");
        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())),
                        param.getValue().idreturProperty()));
        totalBill.setMinWidth(140);

        TableColumn<TblRetur, String> totalPayment = new TableColumn("Total Pembayaran");
        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())),
                        param.getValue().idreturProperty()));
        totalPayment.setMinWidth(140);

        TableColumn<TblRetur, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idreturProperty()));
        totalRestOfBill.setMinWidth(140);

        TableColumn<TblRetur, String> totalNominal = new TableColumn("Total Nominal");
        totalNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(()
                        -> "Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())) + "\n"
                        + "Pembayaran " + ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())) + "\n"
                        + "Sisa Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idreturProperty()));
        totalNominal.setMinWidth(180);

        TableColumn<TblRetur, String> transactionDate = new TableColumn("Tanggal \nRetur");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getReturDate()),
                        param.getValue().returDateProperty()));
        transactionDate.setMinWidth(110);

        TableColumn<TblRetur, String> paymentStatus = new TableColumn("Status");
        paymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelReceivable() != null
                                ? param.getValue().getTblHotelReceivable().getRefFinanceTransactionStatus().getStatusName()
                                : "Belum Dibayar",
                        param.getValue().tblHotelReceivableProperty()));
        paymentStatus.setMinWidth(120);

        TableColumn<TblRetur, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeInvoice.setMinWidth(100);

        tableView.getColumns().addAll(codeInvoice, codeRetur, transactionDate, totalNominal, paymentStatus);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataRetur(dataSupplier)));

        tableView.setRowFactory(tv -> {
            TableRow<TblRetur> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (!row.isEmpty()) {
                    if (!ClassSession.unSavingDataInput.get()) {
                        //set data form input detail
                        setSelectedDataToInputFormDetail(
                                ((TblRetur) row.getItem()) != null
                                        ? ((TblRetur) row.getItem()).getTblHotelReceivable()
                                        : null);
                    } else {  //unsaving data input
                        Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                        if (alert.getResult() == ButtonType.OK) {
                            ClassSession.unSavingDataInput.set(false);
                            //set data form input detail
                            setSelectedDataToInputFormDetail(
                                    ((TblRetur) row.getItem()) != null
                                            ? ((TblRetur) row.getItem()).getTblHotelReceivable()
                                            : null);
                        } else {
                            if (tableDataDetail != null
                                    && tableDataDetail.getTableView() != null) {
                                tableDataDetail.getTableView().getSelectionModel().clearSelection();
                                if (idInvoice != 0L) {
                                    for (int i = 0; i < tableDataDetail.getTableView().getItems().size(); i++) {
                                        if (((TblRetur) tableDataDetail.getTableView().getItems().get(i)).getTblHotelReceivable() != null
                                                && ((TblRetur) tableDataDetail.getTableView().getItems().get(i)).getTblHotelReceivable().getTblHotelInvoice() != null
                                                && ((TblRetur) tableDataDetail.getTableView().getItems().get(i)).getTblHotelReceivable().getTblHotelInvoice().getIdhotelInvoice()
                                                == idInvoice) {
                                            tableDataDetail.getTableView().getSelectionModel().select(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
            
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(notPaidPseudoClass, isNotPaid(newVal));
                } else {
                    row.pseudoClassStateChanged(notPaidPseudoClass, false);
                }                
            });
            
            return row;
        });
        
//        tableView.setRowFactory(tv -> {
//            TableRow<TblRetur> row = new TableRow<>();
//
//            row.itemProperty().addListener((obs, oldVal, newVal) -> {
//                if (newVal != null) {
//                    row.pseudoClassStateChanged(notPaidPseudoClass, isNotPaid(newVal));
//                } else {
//                    row.pseudoClassStateChanged(notPaidPseudoClass, false);
//                }                
//            });
//            return row;
//        });

        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private boolean isNotPaid(TblRetur dataRetur){
        boolean isNotPaid = false;
        if(dataRetur != null
                && dataRetur.getTblHotelReceivable()!= null){
            return dataRetur.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() != 2; //Sudah Dibayar = '2'
        }
        return isNotPaid;
    }
    
    private List<TblRetur> loadAllDataRetur(TblSupplier dataSuplier) {
        List<TblRetur> list = new ArrayList<>();
        if (dataSuplier != null) {
            list = parentController.getService().getAllDataReturByIDSupplier(dataSuplier.getIdsupplier());
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getRefReturType().getIdtype() == 1 //Tukar Barang = '1'
                        || list.get(i).getRefReturStatus().getIdstatus() == 2 //Dibatalkan = '2'
                        || list.get(i).getRefReturStatus().getIdstatus() == 3) {    //Sudah Tidak Berlaku = '3'
                    list.remove(i);
                }
            }
            for (TblRetur data : list) {
                //data hotel receivable
                if (data.getTblHotelReceivable() != null) {
                    data.setTblHotelReceivable(parentController.getService().getDataHotelReceivable(data.getTblHotelReceivable().getIdhotelReceivable()));
                    //data invoice
                    if (data.getTblHotelReceivable().getTblHotelInvoice() != null) {
                        data.getTblHotelReceivable().setTblHotelInvoice(parentController.getService().getDataHotelInvoice(data.getTblHotelReceivable().getTblHotelInvoice().getIdhotelInvoice()));
                    }
                    //data hotel receivable - type
                    data.getTblHotelReceivable().setRefHotelReceivableType(parentController.getService().getDataHotelReceivableType(data.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
                    //data hotel receivable - status
                    data.getTblHotelReceivable().setRefFinanceTransactionStatus(parentController.getService().getDataFinanceTransactionStatus(data.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
                } else {
                    //do something here..?
                }
                //data supplier
                data.setTblSupplier(parentController.getService().getDataSupplier(data.getTblSupplier().getIdsupplier()));
                //data retur - type
                data.setRefReturType(parentController.getService().getDataReturType(data.getRefReturType().getIdtype()));
                //data retur - status
                data.setRefReturStatus(parentController.getService().getDataReturStatus(data.getRefReturStatus().getIdstatus()));
                //data retur - payment status
                if (data.getRefReturPaymentStatus() != null) {
                    data.setRefReturPaymentStatus(parentController.getService().getDataReturPaymentStatus(data.getRefReturPaymentStatus().getIdstatus()));
                }
            }
            //remove data isn't used
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getTblHotelReceivable() != null
                        && list.get(i).getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 3) {   //Sudah Tidak Berlaku = '3'
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
            buttonControl.setText("Buat Transaksi Pembayaran");
            buttonControl.setTooltip(new Tooltip("Buat Transaksi Pembayaran"));
            buttonControl.setOnMouseClicked((e) -> {
                //listener create
                dataHotelFinanceTransactionCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataDetail.addButtonControl(buttonControls);
    }

    private Long idInvoice = 0L;

    private void setSelectedDataToInputFormDetail(TblHotelReceivable hotelReceivable) {
        if (hotelReceivable != null
                && hotelReceivable.getTblHotelInvoice() != null) {
            idInvoice = hotelReceivable.getTblHotelInvoice().getIdhotelInvoice();
            txtCodeInvoice.setText(hotelReceivable.getTblHotelInvoice().getCodeHotelInvoice());
            txtSubject.setText(hotelReceivable.getTblHotelInvoice().getHotelInvoiceSubject());
            if (hotelReceivable.getTblHotelInvoice().getIssueDate() != null) {
                dtpIssueDate.setValue(hotelReceivable.getTblHotelInvoice().getIssueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            } else {
                dtpIssueDate.setValue(null);
            }
            if (hotelReceivable.getTblHotelInvoice().getDueDate() != null) {
                dtpDueDate.setValue(hotelReceivable.getTblHotelInvoice().getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            } else {
                dtpDueDate.setValue(null);
            }
            txtTerm.setText(hotelReceivable.getTblHotelInvoice().getHotelInvoiceNote());
        } else {
            idInvoice = 0L;
            txtCodeInvoice.setText("");
            txtSubject.setText("");
            dtpIssueDate.setValue(null);
            dtpDueDate.setValue(null);
            txtTerm.setText("");
        }
        //set enable or disable
        if (tableDataDetail != null
                && tableDataDetail.getTableView() != null
                && tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {   //enable
            txtSubject.setDisable(false);
            dtpIssueDate.setDisable(false);
            dtpDueDate.setDisable(false);
            txtTerm.setDisable(false);
        } else {  //disable
            txtSubject.setDisable(true);
            dtpIssueDate.setDisable(true);
            dtpDueDate.setDisable(true);
            txtTerm.setDisable(true);
        }
        //set table detail (transaction)
        initTableDataDetailTransaction(hotelReceivable);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public TableView tableDataDetailTransaction;

    private void initTableDataDetailTransaction(TblHotelReceivable hotelReceivable) {
        //set table
        setTableDataDetailTransaction(hotelReceivable);
        //set table to content-view
        ancTableHotelFinanceTransaction.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetailTransaction, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetailTransaction, 0.0);
        AnchorPane.setRightAnchor(tableDataDetailTransaction, 0.0);
        AnchorPane.setTopAnchor(tableDataDetailTransaction, 0.0);
        ancTableHotelFinanceTransaction.getChildren().add(tableDataDetailTransaction);
    }

    private void setTableDataDetailTransaction(TblHotelReceivable hotelReceivable) {
        TableView<TblHotelFinanceTransactionHotelReceivable> tableView = new TableView();

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelFinanceTransaction().getCodeTransaction(),
                        param.getValue().tblHotelFinanceTransactionProperty()));
        codePayment.setMinWidth(120);

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> paymentNominal = new TableColumn("Nominal Pembayaran");
        paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblHotelFinanceTransaction().getIsReturnTransaction() ? "-" : "")
                        + ClassFormatter.currencyFormat.cFormat(param.getValue().getNominalTransaction()),
                        param.getValue().nominalTransactionProperty()));
        paymentNominal.setMinWidth(140);

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> paymentDate = new TableColumn("Tanggal Pembayaran");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getTblHotelFinanceTransaction().getCreateDate()),
                        param.getValue().tblHotelFinanceTransactionProperty()));
        paymentDate.setMinWidth(140);

        tableView.getColumns().addAll(codePayment, paymentNominal, paymentDate);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataHotelFinanceTransactionHotelReceivable(hotelReceivable)));

        tableDataDetailTransaction = tableView;
    }

    private List<TblHotelFinanceTransactionHotelReceivable> loadAllDataHotelFinanceTransactionHotelReceivable(TblHotelReceivable hotelReceivable) {
        List<TblHotelFinanceTransactionHotelReceivable> list = new ArrayList<>();
        if (hotelReceivable != null) {
            list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(hotelReceivable.getIdhotelReceivable());
        }
        return list;
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private int dataInputStatus = 0;

    private void dataHotelReceivableUpdateHandle() {
        if (tableDataReturReceivable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = (TblSupplier) tableDataReturReceivable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataReturReceivableFormShowStatus.set(0);
            dataReturReceivableFormShowStatus.set(1);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataHotelReceivableShowHandle() {
        if (tableDataReturReceivable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = (TblSupplier) tableDataReturReceivable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataReturReceivableFormShowStatus.set(1);
            isShowStatus.set(true);
        }
    }

    private void dataHotelReceivableUnshowHandle() {
        refreshDataTableReturReceivable();
        dataReturReceivableFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    public TblHotelFinanceTransaction selectedDataHFT;

    public TblHotelFinanceTransactionWithCash selectedDataHFTWithCash;

    public TblHotelFinanceTransactionWithTransfer selectedDataHFTWithTransfer;

    public TblHotelFinanceTransactionWithCekGiro selectedDataHFTWithCekGiro;

    public List<TblHotelFinanceTransactionHotelReceivable> selectedDataHFTHRs;

    private void dataHotelFinanceTransactionCreateHandle() {
        if (checkEnableToCreateHotelFinanceTransaction()) {
            selectedDataHFT = new TblHotelFinanceTransaction();
            selectedDataHFT.setTransactionNominal(new BigDecimal("0"));
            selectedDataHFT.setTransactionRoundingValue(new BigDecimal("0"));
            selectedDataHFT.setIsReturnTransaction(false);
            selectedDataHFT.setRefFinanceTransactionType(parentController.getService().getDataFinanceTransactionType(0));    //receivable = '1'
            selectedDataHFTWithCash = new TblHotelFinanceTransactionWithCash();
            selectedDataHFTWithCash.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTWithTransfer = new TblHotelFinanceTransactionWithTransfer();
            selectedDataHFTWithTransfer.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
            selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(selectedDataHFT);
            selectedDataHFTHRs = new ArrayList<>();
            for (TblRetur dataRetur : (List<TblRetur>) tableDataDetail.getTableView().getItems()) {
                if (dataRetur.getTblHotelReceivable() != null //has been set as hotel receivable
                        && calculationTotalRestOfBill(dataRetur).compareTo(new BigDecimal("0")) == 1) {     //nominal rest of bill more then '0'
                    TblHotelFinanceTransactionHotelReceivable dataHFTHR = new TblHotelFinanceTransactionHotelReceivable();
                    dataHFTHR.setTblHotelFinanceTransaction(selectedDataHFT);
                    dataHFTHR.setTblHotelReceivable(dataRetur.getTblHotelReceivable());
                    dataHFTHR.setNominalTransaction(new BigDecimal("0"));
                    //add to list
                    selectedDataHFTHRs.add(dataHFTHR);
                }
            }
            showDataHotelFinanceTransactionDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MEMBUAT TRANSAKSI PEMBAYARAN",
                    "Tidak ada data piutang yang harus dibayar..!", null);
        }
    }

    private boolean checkEnableToCreateHotelFinanceTransaction() {
        return getTotalHotelReceivableJustHaveCodeInvoice(selectedData).compareTo(new BigDecimal("0")) == 1;
    }

    public Stage dialogStageDetal;

    private void showDataHotelFinanceTransactionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_receivable_and_payment/HotelFinanceTransactionInputDialog.fxml"));

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

    private void dataInvoiceSaveHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataInputInvoice()) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    selectedDataRetur = ((TblRetur) tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
                    //dummy
                    TblRetur dummySelectedDataRetur = new TblRetur(selectedDataRetur);
                    dummySelectedDataRetur.setTblSupplier(new TblSupplier(dummySelectedDataRetur.getTblSupplier()));
                    if (dummySelectedDataRetur.getTblHotelReceivable() != null) {
                        dummySelectedDataRetur.setTblHotelReceivable(new TblHotelReceivable(dummySelectedDataRetur.getTblHotelReceivable()));
                        dummySelectedDataRetur.getTblHotelReceivable().setTblHotelInvoice(new TblHotelInvoice(dummySelectedDataRetur.getTblHotelReceivable().getTblHotelInvoice()));
                    }
                    //set data from field input
                    dummySelectedDataRetur.getTblHotelReceivable().getTblHotelInvoice().setCodeHotelInvoice(txtCodeInvoice.getText());
                    dummySelectedDataRetur.getTblHotelReceivable().getTblHotelInvoice().setHotelInvoiceSubject(txtSubject.getText());
                    dummySelectedDataRetur.getTblHotelReceivable().getTblHotelInvoice().setIssueDate(dtpIssueDate.getValue() != null
                            ? Date.valueOf(dtpIssueDate.getValue())
                            : null);
                    dummySelectedDataRetur.getTblHotelReceivable().getTblHotelInvoice().setDueDate(dtpDueDate.getValue() != null
                            ? Date.valueOf(dtpDueDate.getValue())
                            : null);
                    dummySelectedDataRetur.getTblHotelReceivable().getTblHotelInvoice().setHotelInvoiceNote(txtTerm.getText());
                    if (parentController.getService().insertDataHotelInvoice(dummySelectedDataRetur) != null) {
                        ClassMessage.showSucceedInsertingDataMessage("", null);
                        //set selected data input form (table-retur-hr)
                        tableDataDetail.getTableView().setItems(FXCollections.observableArrayList(loadAllDataRetur(selectedData)));
                        //set selected data input form detail
                        setSelectedDataToInputFormDetail(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1
                                ? ((TblRetur) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelReceivable()
                                : null);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                    } else {
                        ClassMessage.showFailedInsertingDataMessage(parentController.getService().getErrorMessage(), null);
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage(errDataInput, null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private String errDataInput;

    private boolean checkDataInputInvoice() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeInvoice.getText() == null
                || txtCodeInvoice.getText().equals("")) {
            dataInput = false;
            errDataInput += "No. Invoice : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private void dataInvoicePrintHandle() {
        if(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size()==1){
           TblRetur selectedData = parentController.getService().getDataReturByIDHotelReceivable(((TblRetur)tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelReceivable().getIdhotelReceivable());
            System.out.println("hsl :"+selectedData.getTblHotelReceivable().getTblHotelInvoice().getCodeHotelInvoice());
           ClassPrintInvoiceRetur invoiceRetur = new ClassPrintInvoiceRetur();
           invoiceRetur.setAlamatPerusahaan(selectedData.getTblHotelReceivable().getTblHotelInvoice().getTblSupplier().getSupplierAddress());
           invoiceRetur.setKeterangan(selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceNote()==null ? "-":selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceNote());
           invoiceRetur.setKodeInvoice(selectedData.getTblHotelReceivable().getTblHotelInvoice().getCodeHotelInvoice());
           invoiceRetur.setNamaPerusahaan(selectedData.getTblHotelReceivable().getTblHotelInvoice().getTblSupplier().getSupplierName());
           invoiceRetur.setSubject(selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceSubject()!=null ? selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceSubject():"-");
           invoiceRetur.setTanggalFaktur(selectedData.getTblHotelReceivable().getTblHotelInvoice().getIssueDate()!=null?new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(selectedData.getTblHotelReceivable().getTblHotelInvoice().getIssueDate()):"-");
           invoiceRetur.setTanggalJatuhTempo(selectedData.getTblHotelReceivable().getTblHotelInvoice().getDueDate()!=null? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(selectedData.getTblHotelReceivable().getTblHotelInvoice().getDueDate()):"-");
           BigDecimal tempTotalPembayaran = new BigDecimal(0);
           List<TblHotelFinanceTransactionHotelReceivable>listPayment  = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(selectedData.getTblHotelReceivable().getIdhotelReceivable());
           for(TblHotelFinanceTransactionHotelReceivable getPayment : listPayment){
             tempTotalPembayaran = tempTotalPembayaran.add(getPayment.getNominalTransaction());
           }
           invoiceRetur.setTotalPembayaran(tempTotalPembayaran);
           
           List<TblReturDetail>list = parentController.getService().getAllDataReturDetailByIDRetur(selectedData.getIdretur());
           List<ClassPrintInvoiceReturDetail>listPrintInvoiceReturDetail = new ArrayList();
           List<ClassPrintInvoiceRetur>listPrintInvoiceRetur = new ArrayList();
           
           for(TblReturDetail getReturDetail : list){
             ClassPrintInvoiceReturDetail invoiceReturDetail = new ClassPrintInvoiceReturDetail();
             invoiceReturDetail.setKodePo(getReturDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo());
             invoiceReturDetail.setDiskon(getReturDetail.getItemDiscount());
             invoiceReturDetail.setHarga(getReturDetail.getItemCost());
             invoiceReturDetail.setNamaBarang(getReturDetail.getTblSupplierItem().getSupplierItemName());
             invoiceReturDetail.setKodeBarang(getReturDetail.getTblSupplierItem().getSupllierItemCode());
             invoiceReturDetail.setPajak(getReturDetail.getItemTaxPercentage());
             invoiceReturDetail.setJumlah(getReturDetail.getItemQuantity());
             invoiceReturDetail.setSatuan(getReturDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName());
             BigDecimal totalHarga = invoiceReturDetail.getHarga().subtract(invoiceReturDetail.getDiskon());
             BigDecimal totalPajak = totalHarga.multiply(invoiceReturDetail.getPajak());
             BigDecimal totalHargaPajak = totalHarga.add(totalPajak);
             invoiceReturDetail.setTotal(totalHargaPajak.multiply(invoiceReturDetail.getJumlah()));
             listPrintInvoiceReturDetail.add(invoiceReturDetail);
           }
           invoiceRetur.setListInvoiceReturDetail(listPrintInvoiceReturDetail);
            System.out.println("hsl invoice retur :"+invoiceRetur.getNamaPerusahaan());
           listPrintInvoiceRetur.add(invoiceRetur);
           System.out.println("size:"+listPrintInvoiceRetur.size());
           ClassPrinter.printInvoiceRetur(listPrintInvoiceRetur,invoiceRetur.getNamaPerusahaan(),invoiceRetur.getKodeInvoice(),invoiceRetur.getTanggalFaktur());
        }
    }

    private void dataReturReceivableBackHandle() {
        //refresh data from table & close form data detail
        refreshDataTableReturReceivable();
        dataReturReceivableFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    public void refreshDataTableReturReceivable() {
        tableDataReturReceivable.getTableView().setItems(FXCollections.observableArrayList(loadAllDataSupplier()));
        cft.refreshFilterItems(tableDataReturReceivable.getTableView().getItems());
    }

    public void refreshSelectedData() {
        setSelectedDataToInputForm();
        dataReturReceivableFormShowStatus.set(1);
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
        setDataReturReceivableSplitpane();

        //init table
        initTableDataReturReceivable();

        //init form
        initFormDataReturReceivable();

        spDataReturReceivable.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReturReceivableFormShowStatus.set(0.0);
        });
    }

    public ReturReceivableController(ReturReceivableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final ReturReceivableAndPaymentController parentController;

    public FReturManager getService() {
        return parentController.getService();
    }

}
