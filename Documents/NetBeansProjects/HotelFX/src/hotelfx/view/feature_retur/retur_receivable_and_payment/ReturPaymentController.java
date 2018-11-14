/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_receivable_and_payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintKuitansiPembayaranInvoiceHotel;
import hotelfx.helper.PrintModel.ClassPrintKuitansiPembayaranInvoiceHotelDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FReturManager;
import hotelfx.view.DashboardController;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReturPaymentController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataReturPayment;

    private DoubleProperty dataReturPaymentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReturPaymentLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReturPaymentSplitpane() {
        spDataReturPayment.setDividerPositions(1);

        dataReturPaymentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReturPaymentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataReturPayment.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataReturPayment.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReturPaymentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReturPaymentLayout.setDisable(false);
                    tableDataReturPaymentLayoutDisableLayer.setDisable(true);
                    tableDataReturPaymentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReturPaymentLayout.setDisable(true);
                    tableDataReturPaymentLayoutDisableLayer.setDisable(false);
                    tableDataReturPaymentLayoutDisableLayer.toFront();
                }
            }
        });

        dataReturPaymentFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataReturPaymentLayout;

    private ClassFilteringTable<TblHotelFinanceTransaction> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataReturPayment;

    private void initTableDataReturPayment() {
        //set table
        setTableDataReturPayment();
        //set control
        setTableControlDataReturPayment();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataReturPayment, 15.0);
        AnchorPane.setLeftAnchor(tableDataReturPayment, 15.0);
        AnchorPane.setRightAnchor(tableDataReturPayment, 15.0);
        AnchorPane.setTopAnchor(tableDataReturPayment, 15.0);
        ancBodyLayout.getChildren().add(tableDataReturPayment);
    }

    private void setTableDataReturPayment() {
        TableView<TblHotelFinanceTransaction> tableView = new TableView();

        TableColumn<TblHotelFinanceTransaction, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory(cellData -> cellData.getValue().codeTransactionProperty());
        codePayment.setMinWidth(120);

        TableColumn<TblHotelFinanceTransaction, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> getSupplierName(param.getValue()),
                        param.getValue().idtransactionProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblHotelFinanceTransaction, String> paymentType = new TableColumn("Tipe Pembayaran");
        paymentType.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType() != null
                                ? (param.getValue().getIsReturnTransaction() ? "Return - " : "") + param.getValue().getRefFinanceTransactionPaymentType().getTypeName() : "-",
                        param.getValue().refFinanceTransactionPaymentTypeProperty()));
        paymentType.setMinWidth(140);

        TableColumn<TblHotelFinanceTransaction, String> paymentNominal = new TableColumn("Nominal Pembayaran");
        paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionNominal()),
                        param.getValue().transactionNominalProperty()));
        paymentNominal.setMinWidth(160);

        TableColumn<TblHotelFinanceTransaction, String> paymentRoundingValue = new TableColumn("Nominal Pembulatan");
        paymentRoundingValue.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionRoundingValue()),
                        param.getValue().transactionRoundingValueProperty()));
        paymentRoundingValue.setMinWidth(160);

        TableColumn<TblHotelFinanceTransaction, String> paymentDate = new TableColumn("Tanggal Pembayaran");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCreateDate()),
                        param.getValue().createDateProperty()));
        paymentDate.setMinWidth(160);

        tableView.getColumns().addAll(codePayment, supplierName, paymentNominal, paymentRoundingValue, paymentType, paymentDate);
        tableView.setItems(loadAllDataHotelFinanceTransaction());

        tableView.setRowFactory(tv -> {
            TableRow<TblHotelFinanceTransaction> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataReturPaymentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataReturPaymentShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataReturPaymentShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataReturPayment = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblHotelFinanceTransaction.class,
                tableDataReturPayment.getTableView(),
                tableDataReturPayment.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataReturPayment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataReturPaymentPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataReturPayment.addButtonControl(buttonControls);
    }

    private ObservableList<TblHotelFinanceTransaction> loadAllDataHotelFinanceTransaction() {
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
        List<TblRetur> returList = parentController.getService().getAllDataRetur();
        for (TblRetur returData : returList) {
            if (returData.getRefReturType().getIdtype() == 2 //Kembali Uang = '2'
                    && returData.getRefReturStatus().getIdstatus() != 2 //Dibatalkan = '2'
                    && returData.getRefReturStatus().getIdstatus() != 3) {  //Sudah Tidak Berlaku = '3'
                if (returData.getTblHotelReceivable() != null) {
                    List<TblHotelFinanceTransactionHotelReceivable> hfthrList = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(returData.getTblHotelReceivable().getIdhotelReceivable());
                    for (TblHotelFinanceTransactionHotelReceivable hfthrData : hfthrList) {
                        boolean found = false;
                        TblHotelFinanceTransaction hftData = parentController.getService().getDataHotelFinanceTransaction(hfthrData.getTblHotelFinanceTransaction().getIdtransaction());
                        if (hftData != null) {
                            for (TblHotelFinanceTransaction data : list) {
                                if (data.getIdtransaction() == hftData.getIdtransaction()) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                list.add(hftData);
                            }
                        }
                    }
                }
            }
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataReturPayment;

    @FXML
    private ScrollPane spFormDataReturPayment;

    @FXML
    private JFXTextField txtCodeHotelFinanceTransaction;

    @FXML
    private JFXDatePicker dtpHotelFinanceTransactionDate;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private JFXTextField txtPaymentType;

    @FXML
    private JFXTextField txtRoundingValue;

    @FXML
    private Label lblHotelTransactionNominal;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnBack;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblHotelFinanceTransaction selectedData;

    private void initFormDataReturPayment() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataReturPayment.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataReturPayment.setOnScroll((ScrollEvent scroll) -> {
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
            dataReturPaymentBackHandle();
        });

        initDateCalendar();

        initImportantFieldColor();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpHotelFinanceTransactionDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField();
    }

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodeTransaction());

        txtCodeHotelFinanceTransaction.textProperty().bind(selectedData.codeTransactionProperty());

        if (selectedData.getCreateDate() != null) {
            dtpHotelFinanceTransactionDate.setValue(selectedData.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpHotelFinanceTransactionDate.setValue(null);
        }

        txtSupplierName.setText(getSupplierName(selectedData));

        txtPaymentType.setText(selectedData.getRefFinanceTransactionPaymentType() != null
                ? (selectedData.getIsReturnTransaction() ? ("Return - ") : "") + selectedData.getRefFinanceTransactionPaymentType().getTypeName() : "-");
        txtRoundingValue.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionRoundingValue()));

        lblHotelTransactionNominal.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionNominal()));

        //init data detail
        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private String getSupplierName(TblHotelFinanceTransaction dataHFT) {
        if (dataHFT != null) {
            List<TblHotelFinanceTransactionHotelReceivable> hfthrList = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(dataHFT.getIdtransaction());
            if (!hfthrList.isEmpty()) {
                TblHotelReceivable hr = parentController.getService().getDataHotelReceivable(hfthrList.get(0).getTblHotelReceivable().getIdhotelReceivable());
                if (hr != null) {
                    TblRetur retur = parentController.getService().getDataReturByIDHotelReceivable(hr.getIdhotelReceivable());
                    if (retur != null) {
                        TblSupplier supplier = parentController.getService().getDataSupplier(retur.getTblSupplier().getIdsupplier());
                        if (supplier != null) {
                            return supplier.getSupplierName();
                        }
                    }
                }
            }
        }
        return "-";
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeHotelFinanceTransaction.setDisable(true);
        dtpHotelFinanceTransactionDate.setDisable(true);
        txtSupplierName.setDisable(true);
        txtPaymentType.setDisable(true);
        txtRoundingValue.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataReturPayment, dataInputStatus == 3,
                txtCodeHotelFinanceTransaction,
                dtpHotelFinanceTransactionDate,
                txtSupplierName,
                txtPaymentType,
                txtRoundingValue);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataReturPaymentShowHandle() {
        if (tableDataReturPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getService().getDataHotelFinanceTransaction(((TblHotelFinanceTransaction) tableDataReturPayment.getTableView().getSelectionModel().getSelectedItem()).getIdtransaction());
            setSelectedDataToInputForm();
            dataReturPaymentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataReturPaymentUnshowHandle() {
        refreshDataTableReturPayment();
        dataReturPaymentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataReturPaymentPrintHandle() {
        if (tableDataReturPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = parentController.getService().getDataHotelFinanceTransaction(((TblHotelFinanceTransaction) tableDataReturPayment.getTableView().getSelectionModel().getSelectedItem()).getIdtransaction());
            ClassPrintKuitansiPembayaranInvoiceHotel kuitansiInvoiceHotel = new ClassPrintKuitansiPembayaranInvoiceHotel();
            kuitansiInvoiceHotel.setKodeTransaksi(selectedData.getCodeTransaction());
            kuitansiInvoiceHotel.setPembulatan(selectedData.getTransactionRoundingValue());
            kuitansiInvoiceHotel.setTanggalBayar(new SimpleDateFormat("dd MMMM yyyy", new Locale("id")).format(selectedData.getCreateDate()));
            kuitansiInvoiceHotel.setTipePembayaran(selectedData.getRefFinanceTransactionPaymentType().getTypeName());

            List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(selectedData.getIdtransaction());
            List<ClassPrintKuitansiPembayaranInvoiceHotelDetail> listPembayaranInvoiceReturDetail = new ArrayList();
            List<ClassPrintKuitansiPembayaranInvoiceHotel> listPembayaranInvoiceRetur = new ArrayList();
            for (TblHotelFinanceTransactionHotelReceivable getHotelReceivable : list) {
                TblRetur returPayment = parentController.getService().getDataReturByIDHotelReceivable(getHotelReceivable.getTblHotelReceivable().getIdhotelReceivable());
                kuitansiInvoiceHotel.setNamaPerusahaan(returPayment.getTblSupplier().getSupplierName());
                kuitansiInvoiceHotel.setAlamatPerusahaan(returPayment.getTblSupplier().getSupplierAddress());

                ClassPrintKuitansiPembayaranInvoiceHotelDetail pembayaranInvoiceHotelDetail = new ClassPrintKuitansiPembayaranInvoiceHotelDetail();
                pembayaranInvoiceHotelDetail.setKodeInvoice(getHotelReceivable.getTblHotelReceivable().getTblHotelInvoice() == null ? "-" : getHotelReceivable.getTblHotelReceivable().getTblHotelInvoice().getCodeHotelInvoice());
                pembayaranInvoiceHotelDetail.setNominalPembayaran(getHotelReceivable.getNominalTransaction());
                listPembayaranInvoiceReturDetail.add(pembayaranInvoiceHotelDetail);
            }
            kuitansiInvoiceHotel.setListKuitansiPembayaranInvoiceHotelDetail(listPembayaranInvoiceReturDetail);
            listPembayaranInvoiceRetur.add(kuitansiInvoiceHotel);
            ClassPrinter.printKuitansiPartner(listPembayaranInvoiceRetur, kuitansiInvoiceHotel.getKodeTransaksi(), kuitansiInvoiceHotel.getNamaPerusahaan(), kuitansiInvoiceHotel.getTanggalBayar());
        }
    }

    private void dataReturPaymentBackHandle() {
        //refresh data from table & close form data detail
        refreshDataTableReturPayment();
        dataReturPaymentFormShowStatus.set(0.0);
        isShowStatus.set(false);
    }

    public void refreshDataTableReturPayment() {
        tableDataReturPayment.getTableView().setItems(loadAllDataHotelFinanceTransaction());
        cft.refreshFilterItems(tableDataReturPayment.getTableView().getItems());
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancTableDetailLayout;

    public TableView tableDataDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set table-control to content-view
        ancTableDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 10.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 10.0);
        AnchorPane.setRightAnchor(tableDataDetail, 10.0);
        AnchorPane.setTopAnchor(tableDataDetail, 10.0);
        ancTableDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail() {
        TableView<TblHotelFinanceTransactionHotelReceivable> tableView = new TableView();

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> codeRetur = new TableColumn("No. Retur");
        codeRetur.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> getCodeRetur(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeRetur.setMinWidth(120);

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeInvoice.setMinWidth(120);

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> nominalTransaction = new TableColumn("Nominal");
        nominalTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getNominalTransaction()),
                        param.getValue().nominalTransactionProperty()));
        nominalTransaction.setMinWidth(150);

        tableView.getColumns().addAll(codeRetur, codeInvoice, nominalTransaction);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataReturPaymentDetail()));
        tableDataDetail = tableView;
    }

    private String getCodeRetur(TblHotelReceivable hotelReceivable) {
        if (hotelReceivable != null) {
            TblRetur dataRetur = parentController.getService().getDataReturByIDHotelReceivable(hotelReceivable.getIdhotelReceivable());
            if (dataRetur != null) {
                return dataRetur.getCodeRetur();
            }
        }
        return "-";
    }

    private String getCodeInvoice(TblHotelReceivable hotelReceivable) {
        if (hotelReceivable != null
                && hotelReceivable.getTblHotelInvoice() != null) {
            return hotelReceivable.getTblHotelInvoice().getCodeHotelInvoice();
        }
        return "-";
    }

    private List<TblHotelFinanceTransactionHotelReceivable> loadAllDataReturPaymentDetail() {
        List<TblHotelFinanceTransactionHotelReceivable> list = new ArrayList<>();
        if (selectedData != null
                && selectedData.getIdtransaction() != 0L) {
            list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(selectedData.getIdtransaction());
            for (TblHotelFinanceTransactionHotelReceivable data : list) {
                //data hotel receivable
                data.setTblHotelReceivable(parentController.getService().getDataHotelReceivable(data.getTblHotelReceivable().getIdhotelReceivable()));
            }
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
        setDataReturPaymentSplitpane();

        //init table
        initTableDataReturPayment();

        //init form
        initFormDataReturPayment();

        spDataReturPayment.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReturPaymentFormShowStatus.set(0.0);
        });
    }

    public ReturPaymentController(ReturReceivableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final ReturReceivableAndPaymentController parentController;

    public FReturManager getService() {
        return parentController.getService();
    }

}
