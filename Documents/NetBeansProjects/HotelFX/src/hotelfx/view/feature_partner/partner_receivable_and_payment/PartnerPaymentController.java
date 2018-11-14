/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.partner_receivable_and_payment;

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
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.service.FPartnerManager;
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
public class PartnerPaymentController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPartnerPayment;

    private DoubleProperty dataPartnerPaymentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPartnerPaymentLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPartnerPaymentSplitpane() {
        spDataPartnerPayment.setDividerPositions(1);

        dataPartnerPaymentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPartnerPaymentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPartnerPayment.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPartnerPayment.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPartnerPaymentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPartnerPaymentLayout.setDisable(false);
                    tableDataPartnerPaymentLayoutDisableLayer.setDisable(true);
                    tableDataPartnerPaymentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPartnerPaymentLayout.setDisable(true);
                    tableDataPartnerPaymentLayoutDisableLayer.setDisable(false);
                    tableDataPartnerPaymentLayoutDisableLayer.toFront();
                }
            }
        });

        dataPartnerPaymentFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPartnerPaymentLayout;

    private ClassFilteringTable<TblHotelFinanceTransaction> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataPartnerPayment;

    private void initTableDataPartnerPayment() {
        //set table
        setTableDataPartnerPayment();
        //set control
        setTableControlDataPartnerPayment();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPartnerPayment, 15.0);
        AnchorPane.setLeftAnchor(tableDataPartnerPayment, 15.0);
        AnchorPane.setRightAnchor(tableDataPartnerPayment, 15.0);
        AnchorPane.setTopAnchor(tableDataPartnerPayment, 15.0);
        ancBodyLayout.getChildren().add(tableDataPartnerPayment);
    }

    private void setTableDataPartnerPayment() {
        TableView<TblHotelFinanceTransaction> tableView = new TableView();

        TableColumn<TblHotelFinanceTransaction, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory(cellData -> cellData.getValue().codeTransactionProperty());
        codePayment.setMinWidth(120);

        TableColumn<TblHotelFinanceTransaction, String> partnerName = new TableColumn("Nama");
        partnerName.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> getPartnerName(param.getValue()),
                        param.getValue().idtransactionProperty()));
        partnerName.setMinWidth(140);

        TableColumn<TblHotelFinanceTransaction, String> partnerType = new TableColumn("Tipe");
        partnerType.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> getPartnerType(param.getValue()),
                        param.getValue().idtransactionProperty()));
        partnerType.setMinWidth(140);

        TableColumn<TblHotelFinanceTransaction, String> partnerTitle = new TableColumn("Partner");
        partnerTitle.getColumns().addAll(partnerName, partnerType);

        TableColumn<TblHotelFinanceTransaction, String> paymentType = new TableColumn("Tipe Pembayaran");
        paymentType.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType() != null
                                ? (param.getValue().getIsReturnTransaction() ? ("Return" + " - ") : "") + param.getValue().getRefFinanceTransactionPaymentType().getTypeName() : "-",
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
        paymentRoundingValue.setMinWidth(150);

        TableColumn<TblHotelFinanceTransaction, String> paymentDate = new TableColumn("Tanggal Pembayaran");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCreateDate()),
                        param.getValue().createDateProperty()));
        paymentDate.setMinWidth(160);

        tableView.getColumns().addAll(codePayment, partnerTitle, paymentNominal, paymentRoundingValue, paymentType, paymentDate);
        tableView.setItems(loadAllDataHotelFinanceTransaction());

        tableView.setRowFactory(tv -> {
            TableRow<TblHotelFinanceTransaction> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPartnerPaymentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataPartnerPaymentShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataPartnerPaymentShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataPartnerPayment = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblHotelFinanceTransaction.class,
                tableDataPartnerPayment.getTableView(),
                tableDataPartnerPayment.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataPartnerPayment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataPartnerPaymentPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPartnerPayment.addButtonControl(buttonControls);
    }

    private ObservableList<TblHotelFinanceTransaction> loadAllDataHotelFinanceTransaction() {
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
        List<TblReservationPaymentWithGuaranteePayment> rpwgpList = parentController.getService().getAllDataReservationPaymentWithGuaranteePayment();
        for (TblReservationPaymentWithGuaranteePayment rpwgpData : rpwgpList) {
            if (rpwgpData.getTblHotelReceivable() != null) {
                List<TblHotelFinanceTransactionHotelReceivable> hfthrList = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(rpwgpData.getTblHotelReceivable().getIdhotelReceivable());
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
        //sorting data
        bubbleSort(list);
        return FXCollections.observableArrayList(list);
    }

    private void bubbleSort(List<TblHotelFinanceTransaction> arr) {
        boolean swapped = true;
        int j = 0;
        TblHotelFinanceTransaction tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < arr.size() - j; i++) {
                if (arr.get(i).getCreateDate().before(
                        arr.get(i + 1).getCreateDate())) {
                    tmp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }
    
    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataPartnerPayment;

    @FXML
    private ScrollPane spFormDataPartnerPayment;

    @FXML
    private JFXTextField txtCodeHotelFinanceTransaction;

    @FXML
    private JFXDatePicker dtpHotelFinanceTransactionDate;

    @FXML
    private JFXTextField txtPartnerName;

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

    private void initFormDataPartnerPayment() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPartnerPayment.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPartnerPayment.setOnScroll((ScrollEvent scroll) -> {
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
            dataPartnerPaymentBackHandle();
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

        txtPartnerName.setText(getPartnerName(selectedData));

        txtPaymentType.setText(selectedData.getRefFinanceTransactionPaymentType() != null
                ? (selectedData.getIsReturnTransaction() ? ("Return - ") : "") + selectedData.getRefFinanceTransactionPaymentType().getTypeName() : "-");
        txtRoundingValue.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionRoundingValue()));

        lblHotelTransactionNominal.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionNominal()));

        //init data detail
        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private String getPartnerName(TblHotelFinanceTransaction dataHFT) {
        if (dataHFT != null) {
            List<TblHotelFinanceTransactionHotelReceivable> hfthrList = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(dataHFT.getIdtransaction());
            if (!hfthrList.isEmpty()) {
                TblHotelReceivable hr = parentController.getService().getDataHotelReceivable(hfthrList.get(0).getTblHotelReceivable().getIdhotelReceivable());
                if (hr != null) {
                    TblReservationPaymentWithGuaranteePayment rpwgp = parentController.getService().getDataReservationPaymentWithGuaranteePaymentByIDHotelReceivable(hr.getIdhotelReceivable());
                    if (rpwgp != null) {
                        TblPartner partner = parentController.getService().getDataPartner(rpwgp.getTblPartner().getIdpartner());
                        if (partner != null) {
                            return partner.getPartnerName();
                        }
                    }
                }
            }
        }
        return "-";
    }

    private String getPartnerType(TblHotelFinanceTransaction dataHFT) {
        if (dataHFT != null) {
            List<TblHotelFinanceTransactionHotelReceivable> hfthrList = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(dataHFT.getIdtransaction());
            if (!hfthrList.isEmpty()) {
                TblHotelReceivable hr = parentController.getService().getDataHotelReceivable(hfthrList.get(0).getTblHotelReceivable().getIdhotelReceivable());
                if (hr != null) {
                    TblReservationPaymentWithGuaranteePayment rpwgp = parentController.getService().getDataReservationPaymentWithGuaranteePaymentByIDHotelReceivable(hr.getIdhotelReceivable());
                    if (rpwgp != null) {
                        TblPartner partner = parentController.getService().getDataPartner(rpwgp.getTblPartner().getIdpartner());
                        if (partner != null) {
                            return partner.getRefPartnerType().getTypeName();
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
        txtPartnerName.setDisable(true);
        txtPaymentType.setDisable(true);
        txtRoundingValue.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataPartnerPayment, dataInputStatus == 3,
                txtCodeHotelFinanceTransaction,
                dtpHotelFinanceTransactionDate,
                txtPartnerName,
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

    private void dataPartnerPaymentShowHandle() {
        if (tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getService().getDataHotelFinanceTransaction(((TblHotelFinanceTransaction) tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItem()).getIdtransaction());
            setSelectedDataToInputForm();
            dataPartnerPaymentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPartnerPaymentUnshowHandle() {
        refreshDataTablePartnerPayment();
        dataPartnerPaymentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPartnerPaymentPrintHandle() {
        if (tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = parentController.getService().getDataHotelFinanceTransaction(((TblHotelFinanceTransaction) tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItem()).getIdtransaction());
            List<ClassPrintKuitansiPembayaranInvoiceHotel> listPembayaranInvoiceHotel = new ArrayList();

            ClassPrintKuitansiPembayaranInvoiceHotel pembayaranInvoiceHotel = new ClassPrintKuitansiPembayaranInvoiceHotel();
            pembayaranInvoiceHotel.setKodeTransaksi(selectedData.getCodeTransaction());
            pembayaranInvoiceHotel.setTanggalBayar(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(selectedData.getCreateDate()));
            pembayaranInvoiceHotel.setTipePembayaran(selectedData.getRefFinanceTransactionPaymentType().getTypeName());
            pembayaranInvoiceHotel.setPembulatan(selectedData.getTransactionRoundingValue());

            List<ClassPrintKuitansiPembayaranInvoiceHotelDetail> listPembayaranDetail = new ArrayList();
            List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(selectedData.getIdtransaction());
            for (TblHotelFinanceTransactionHotelReceivable getHotelReceivable : list) {
                TblReservationPaymentWithGuaranteePayment guaranteePayment = parentController.getService().getDataReservationPaymentWithGuaranteePaymentByIDHotelReceivable(getHotelReceivable.getTblHotelReceivable().getIdhotelReceivable());
                pembayaranInvoiceHotel.setNamaPerusahaan(guaranteePayment.getTblPartner().getPartnerName());
                pembayaranInvoiceHotel.setAlamatPerusahaan(guaranteePayment.getTblPartner().getPartnerAddress());

                ClassPrintKuitansiPembayaranInvoiceHotelDetail kuitansiInvoiceHotelDetail = new ClassPrintKuitansiPembayaranInvoiceHotelDetail();
                kuitansiInvoiceHotelDetail.setKodeInvoice(getHotelReceivable.getTblHotelReceivable().getTblHotelInvoice().getCodeHotelInvoice());
                kuitansiInvoiceHotelDetail.setNominalPembayaran(getHotelReceivable.getNominalTransaction());
                listPembayaranDetail.add(kuitansiInvoiceHotelDetail);
            }
            pembayaranInvoiceHotel.setListKuitansiPembayaranInvoiceHotelDetail(listPembayaranDetail);
            listPembayaranInvoiceHotel.add(pembayaranInvoiceHotel);

            ClassPrinter.printKuitansiPartner(listPembayaranInvoiceHotel, pembayaranInvoiceHotel.getKodeTransaksi(), pembayaranInvoiceHotel.getNamaPerusahaan(), pembayaranInvoiceHotel.getTanggalBayar());
        }
    }

    private void dataPartnerPaymentBackHandle() {
        //refresh data from table & close form data detail
        refreshDataTablePartnerPayment();
        dataPartnerPaymentFormShowStatus.set(0.0);
        isShowStatus.set(false);
    }

    public void refreshDataTablePartnerPayment() {
        tableDataPartnerPayment.getTableView().setItems(loadAllDataHotelFinanceTransaction());
        cft.refreshFilterItems(tableDataPartnerPayment.getTableView().getItems());
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

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> codeRP = new TableColumn("No. Transaksi\n (Reservasi)");
        codeRP.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> getCodeReservationPayment(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeRP.setMinWidth(120);

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

        tableView.getColumns().addAll(codeRP, codeInvoice, nominalTransaction);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataPartnerPaymentDetail()));
        tableDataDetail = tableView;
    }

    private String getCodeReservationPayment(TblHotelReceivable hotelReceivable) {
        if (hotelReceivable != null) {
            TblReservationPaymentWithGuaranteePayment dataRPWGP = parentController.getService().getDataReservationPaymentWithGuaranteePaymentByIDHotelReceivable(hotelReceivable.getIdhotelReceivable());
            if (dataRPWGP != null) {
                TblReservationPayment dataRP = parentController.getService().getDataResrevationPayment(dataRPWGP.getTblReservationPayment().getIdpayment());
                if (dataRP != null) {
                    return dataRP.getCodePayment();
                }
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

    private List<TblHotelFinanceTransactionHotelReceivable> loadAllDataPartnerPaymentDetail() {
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
        setDataPartnerPaymentSplitpane();

        //init table
        initTableDataPartnerPayment();

        //init form
        initFormDataPartnerPayment();

        spDataPartnerPayment.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPartnerPaymentFormShowStatus.set(0.0);
        });
    }

    public PartnerPaymentController(PartnerReceivableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final PartnerReceivableAndPaymentController parentController;

    public FPartnerManager getService() {
        return parentController.getService();
    }

}
