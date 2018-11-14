/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.partner_receivable_and_payment;

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
import hotelfx.helper.PrintModel.ClassPrintInvoiceHotel;
import hotelfx.helper.PrintModel.ClassPrintInvoiceHotelDetail;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.service.FPartnerManager;
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
public class PartnerReceivableController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPartnerReceivable;

    private DoubleProperty dataPartnerReceivableFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPartnerReceivableLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPartnerReceivableSplitpane() {
        spDataPartnerReceivable.setDividerPositions(1);

        dataPartnerReceivableFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPartnerReceivableFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPartnerReceivable.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPartnerReceivable.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPartnerReceivableFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataPartnerReceivableLayout.setDisable(false);
            tableDataPartnerReceivableLayoutDisableLayer.setDisable(true);
            tableDataPartnerReceivableLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPartnerReceivableLayout.setDisable(false);
                    tableDataPartnerReceivableLayoutDisableLayer.setDisable(true);
                    tableDataPartnerReceivableLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPartnerReceivableLayout.setDisable(true);
                    tableDataPartnerReceivableLayoutDisableLayer.setDisable(false);
                    tableDataPartnerReceivableLayoutDisableLayer.toFront();
                }
            }
        });

        dataPartnerReceivableFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPartnerReceivableLayout;

    private ClassFilteringTable<TblPartner> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataPartnerReceivable;

    private void initTableDataPartnerReceivable() {
        //set table
        setTableDataPartnerReceivable();
        //set control
        setTableControlDataPartnerReceivable();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPartnerReceivable, 15.0);
        AnchorPane.setLeftAnchor(tableDataPartnerReceivable, 15.0);
        AnchorPane.setRightAnchor(tableDataPartnerReceivable, 15.0);
        AnchorPane.setTopAnchor(tableDataPartnerReceivable, 15.0);
        ancBodyLayout.getChildren().add(tableDataPartnerReceivable);
    }

    private void setTableDataPartnerReceivable() {
        TableView<TblPartner> tableView = new TableView();

        TableColumn<TblPartner, String> codePartner = new TableColumn("ID");
        codePartner.setCellValueFactory(cellData -> cellData.getValue().codePartnerProperty());
        codePartner.setMinWidth(120);

        TableColumn<TblPartner, String> partnerName = new TableColumn("Partner");
        partnerName.setCellValueFactory(cellData -> cellData.getValue().partnerNameProperty());
        partnerName.setMinWidth(140);

        TableColumn<TblPartner, String> picName = new TableColumn("Nama");
        picName.setCellValueFactory(cellData -> cellData.getValue().picnameProperty());
        picName.setMinWidth(140);

        TableColumn<TblPartner, String> picPhoneNumber = new TableColumn("Telepon");
        picPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().picphoneNumberProperty());
        picPhoneNumber.setMinWidth(120);

        TableColumn<TblPartner, String> picEmailAddress = new TableColumn("Email");
        picEmailAddress.setCellValueFactory(cellData -> cellData.getValue().picemailAddressProperty());
        picEmailAddress.setMinWidth(200);

        TableColumn<TblPartner, String> picTitle = new TableColumn("PIC");
        picTitle.getColumns().addAll(picName, picPhoneNumber, picEmailAddress);

        TableColumn<TblPartner, String> totalHotelReceivable = new TableColumn("Total Tagihan");
        totalHotelReceivable.setCellValueFactory((TableColumn.CellDataFeatures<TblPartner, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(getTotalHotelReceivable(param.getValue())),
                        param.getValue().idpartnerProperty()));
        totalHotelReceivable.setMinWidth(140);

        TableColumn<TblPartner, String> minDueDate = new TableColumn("Tgl. Estimasi Bayar\n"
                + "(paling dekat)");
        minDueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPartner, String> param)
                -> Bindings.createStringBinding(() -> getMinDueDate(param.getValue()),
                        param.getValue().idpartnerProperty()));
        minDueDate.setMinWidth(160);

        tableView.getColumns().addAll(codePartner, partnerName, picTitle, totalHotelReceivable, minDueDate);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataPartner()));

        tableView.setRowFactory(tv -> {
            TableRow<TblPartner> row = new TableRow<>();
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

        tableDataPartnerReceivable = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblPartner.class,
                tableDataPartnerReceivable.getTableView(),
                tableDataPartnerReceivable.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private BigDecimal getTotalHotelReceivable(TblPartner partner) {
        BigDecimal result = new BigDecimal("0");
        if (partner != null) {
            List<TblReservationPaymentWithGuaranteePayment> rpwgpList = loadAllDataRPWGP(partner);
            for (TblReservationPaymentWithGuaranteePayment rpwgpData : rpwgpList) {
                if (rpwgpData.getTblHotelReceivable() != null
                        && rpwgpData.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() != 4) { //Dibatalkan = '4'
                    result = result.add(rpwgpData.getTblHotelReceivable().getHotelReceivableNominal());
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(rpwgpData.getTblHotelReceivable().getIdhotelReceivable());
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

    private BigDecimal getTotalHotelReceivableJustHaveCodeInvoice(TblPartner partner) {
        BigDecimal result = new BigDecimal("0");
        if (partner != null) {
            List<TblReservationPaymentWithGuaranteePayment> rpwgpList = loadAllDataRPWGP(partner);
            for (TblReservationPaymentWithGuaranteePayment rpwgpData : rpwgpList) {
                if (rpwgpData.getTblHotelReceivable() != null
                        && rpwgpData.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() != 4) { //Dibatalkan = '4'
                    result = result.add(rpwgpData.getTblHotelReceivable().getHotelReceivableNominal());
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(rpwgpData.getTblHotelReceivable().getIdhotelReceivable());
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

    private String getMinDueDate(TblPartner partner) {
        if (partner != null) {
            LocalDate localDate = null;
            List<TblReservationPaymentWithGuaranteePayment> rpwgpList = loadAllDataRPWGP(partner);
            for (TblReservationPaymentWithGuaranteePayment rpwgpData : rpwgpList) {
                if (rpwgpData.getTblHotelReceivable() != null
                        && rpwgpData.getTblHotelReceivable().getTblHotelInvoice() != null
                        && rpwgpData.getTblHotelReceivable().getTblHotelInvoice().getDueDate() != null) {
                    LocalDate countDate = LocalDate.of(rpwgpData.getTblHotelReceivable().getTblHotelInvoice().getDueDate().getYear() + 1900,
                            rpwgpData.getTblHotelReceivable().getTblHotelInvoice().getDueDate().getMonth() + 1,
                            rpwgpData.getTblHotelReceivable().getTblHotelInvoice().getDueDate().getDate());
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

    private List<TblPartner> loadAllDataPartner() {
        List<TblPartner> list = parentController.getService().getAllDataPartner();
        return list;
    }

    public BigDecimal calculationTotalBill(TblReservationPaymentWithGuaranteePayment rpwgp) {
        BigDecimal result = new BigDecimal("0");
        if (rpwgp != null
                && rpwgp.getIsDebt()
                && rpwgp.getTblHotelReceivable() != null) {
            result = rpwgp.getTblHotelReceivable().getHotelReceivableNominal();
        }
        return result;
    }

    public BigDecimal calculationTotalPayment(TblReservationPaymentWithGuaranteePayment rpwgp) {
        BigDecimal result = new BigDecimal("0");
        if (rpwgp != null
                && rpwgp.getIsDebt()
                && rpwgp.getTblHotelReceivable() != null) {
            List<TblHotelFinanceTransactionHotelReceivable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(rpwgp.getTblHotelReceivable().getIdhotelReceivable());
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

    public BigDecimal calculationTotalRestOfBill(TblReservationPaymentWithGuaranteePayment rpwgp) {
        if (rpwgp.getTblHotelReceivable() != null
                && rpwgp.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 4) {  //Dibatalkan = '4'
            return new BigDecimal("0");
        }
        return calculationTotalBill(rpwgp).subtract(calculationTotalPayment(rpwgp));
    }

    public String getCodeReservationPayment(TblReservationPayment reservationPayment) {
        if (reservationPayment != null) {
            return reservationPayment.getCodePayment();
        }
        return "-";
    }

    public String getReservationpaymentDate(TblReservationPayment reservationPayment) {
        if (reservationPayment != null) {
            return ClassFormatter.dateTimeFormate.format(reservationPayment.getPaymentDate());
        }
        return "-";
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

    private void setTableControlDataPartnerReceivable() {
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
        tableDataPartnerReceivable.addButtonControl(buttonControls);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataPartnerReceivable;

    @FXML
    private ScrollPane spFormDataPartnerReceivable;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXTextField txtPartner;

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

    public TblPartner selectedData;

    public TblReservationPaymentWithGuaranteePayment selectedDataRPWGP;

    private void initFormDataPartnerReceivable() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPartnerReceivable.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPartnerReceivable.setOnScroll((ScrollEvent scroll) -> {
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
            dataPartnerReceivableBackHandle();
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
     * TABLE DATA PARTNER RECEIVABLE
     */
    
    private final PseudoClass notPaidPseudoClass = PseudoClass.getPseudoClass("notpaid");
    
    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodePartner() + " - " + selectedData.getPartnerName());

        txtPartner.setText(selectedData.getCodePartner() + " - " + selectedData.getPartnerName());
        txtPICName.setText(selectedData.getPicname());
        txtPICPhoneNumber.setText(selectedData.getPicphoneNumber());

        lblTotalHotelReceivable.setText(ClassFormatter.currencyFormat.format(getTotalHotelReceivable(selectedData)));

        initTableDataDetail(selectedData);

        //set selected data input form detail
        setSelectedDataToInputFormDetail(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1
                ? ((TblReservationPaymentWithGuaranteePayment) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelReceivable()
                : null);

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ancTableDetailLayout.setDisable(dataInputStatus == 3);

        gpFormDataPartnerReceivable.setDisable(dataInputStatus == 3);
        txtCodeInvoice.setDisable(true);

        btnSave.setVisible(dataInputStatus != 3);
        btnPrint.setDisable(false);
    }

    public ClassTableWithControl tableDataDetail;

    private void initTableDataDetail(TblPartner dataPartner) {
        //set table
        setTableDataDetail(dataPartner);
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

    private void setTableDataDetail(TblPartner dataPartner) {
        TableView<TblReservationPaymentWithGuaranteePayment> tableView = new TableView();

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> codeReservationPayment = new TableColumn("No. Transaksi\n (Reservasi)");
        codeReservationPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> getCodeReservationPayment(param.getValue().getTblReservationPayment()),
                        param.getValue().tblReservationPaymentProperty()));
        codeReservationPayment.setMinWidth(85);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> totalBill = new TableColumn("Total Tagihan");
        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())),
                        param.getValue().iddetailProperty()));
        totalBill.setMinWidth(140);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> totalPayment = new TableColumn("Total Pembayaran");
        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())),
                        param.getValue().iddetailProperty()));
        totalPayment.setMinWidth(140);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().iddetailProperty()));
        totalRestOfBill.setMinWidth(140);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> totalNominal = new TableColumn("Total Nominal");
        totalNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(()
                        -> "Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())) + "\n"
                        + "Pembayaran " + ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())) + "\n"
                        + "Sisa Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().iddetailProperty()));
        totalNominal.setMinWidth(180);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> transactionDate = new TableColumn(" Tanggal\n Transaksi\n(Reservasi)");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> getReservationpaymentDate(param.getValue().getTblReservationPayment()),
                        param.getValue().tblReservationPaymentProperty()));
        transactionDate.setMinWidth(110);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> paymentStatus = new TableColumn("Status");
        paymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelReceivable() != null
                                ? param.getValue().getTblHotelReceivable().getRefFinanceTransactionStatus().getStatusName()
                                : "Belum Dibayar",
                        param.getValue().tblHotelReceivableProperty()));
        paymentStatus.setMinWidth(120);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeInvoice.setMinWidth(100);

        tableView.getColumns().addAll(codeInvoice, codeReservationPayment, transactionDate, totalNominal, paymentStatus);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataRPWGP(dataPartner)));

        tableView.setRowFactory(tv -> {
            TableRow<TblReservationPaymentWithGuaranteePayment> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (!row.isEmpty()) {
                    if (!ClassSession.unSavingDataInput.get()) {
                        //set data form input detail
                        setSelectedDataToInputFormDetail(
                                ((TblReservationPaymentWithGuaranteePayment) row.getItem()) != null
                                        ? ((TblReservationPaymentWithGuaranteePayment) row.getItem()).getTblHotelReceivable()
                                        : null);
                    } else {  //unsaving data input
                        Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                        if (alert.getResult() == ButtonType.OK) {
                            ClassSession.unSavingDataInput.set(false);
                            //set data form input detail
                            setSelectedDataToInputFormDetail(
                                    ((TblReservationPaymentWithGuaranteePayment) row.getItem()) != null
                                            ? ((TblReservationPaymentWithGuaranteePayment) row.getItem()).getTblHotelReceivable()
                                            : null);
                        } else {
                            if (tableDataDetail != null
                                    && tableDataDetail.getTableView() != null) {
                                tableDataDetail.getTableView().getSelectionModel().clearSelection();
                                if (idInvoice != 0L) {
                                    for (int i = 0; i < tableDataDetail.getTableView().getItems().size(); i++) {
                                        if (((TblReservationPaymentWithGuaranteePayment) tableDataDetail.getTableView().getItems().get(i)).getTblHotelReceivable() != null
                                                && ((TblReservationPaymentWithGuaranteePayment) tableDataDetail.getTableView().getItems().get(i)).getTblHotelReceivable().getTblHotelInvoice() != null
                                                && ((TblReservationPaymentWithGuaranteePayment) tableDataDetail.getTableView().getItems().get(i)).getTblHotelReceivable().getTblHotelInvoice().getIdhotelInvoice()
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
//            TableRow<TblReservationPaymentWithGuaranteePayment> row = new TableRow<>();
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

    private boolean isNotPaid(TblReservationPaymentWithGuaranteePayment dataRPWGP){
        boolean isNotPaid = false;
        if(dataRPWGP != null
                && dataRPWGP.getTblHotelReceivable()!= null){
            return dataRPWGP.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() != 2; //Sudah Dibayar = '2'
        }
        return isNotPaid;
    }
    
    private List<TblReservationPaymentWithGuaranteePayment> loadAllDataRPWGP(TblPartner dataPartner) {
        List<TblReservationPaymentWithGuaranteePayment> list = new ArrayList<>();
        if (dataPartner != null) {
            list = parentController.getService().getAllDataReservationPaymentWithGuaranteePaymentByIDPartner(dataPartner.getIdpartner());
            for (int i = list.size() - 1; i > -1; i--) {
                if (!list.get(i).getIsDebt()) {    //!debt
                    list.remove(i);
                }
            }
            for (TblReservationPaymentWithGuaranteePayment data : list) {
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
                //data partner
                data.setTblPartner(parentController.getService().getDataPartner(data.getTblPartner().getIdpartner()));
                //data reservation payment
                data.setTblReservationPayment(parentController.getService().getDataReservationPayment(data.getTblReservationPayment().getIdpayment()));
                //data reservation bill
                data.getTblReservationPayment().setTblReservationBill(parentController.getService().getDataReservationBill(data.getTblReservationPayment().getTblReservationBill().getIdbill()));
                //data employee : cashier
                data.getTblReservationPayment().setTblEmployeeByIdcashier(parentController.getService().getDataEmployee(data.getTblReservationPayment().getTblEmployeeByIdcashier().getIdemployee()));
                //data reservation
                data.getTblReservationPayment().getTblReservationBill().setTblReservation(parentController.getService().getDataReservation(data.getTblReservationPayment().getTblReservationBill().getTblReservation().getIdreservation()));
                //data reservation status
                data.getTblReservationPayment().getTblReservationBill().getTblReservation().setRefReservationStatus(parentController.getService().getDataReservationStatus(data.getTblReservationPayment().getTblReservationBill().getTblReservation().getRefReservationStatus().getIdstatus()));
                //data rpwgp - payment status...???
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
        if (tableDataPartnerReceivable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = (TblPartner) tableDataPartnerReceivable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataPartnerReceivableFormShowStatus.set(0);
            dataPartnerReceivableFormShowStatus.set(1);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataHotelReceivableShowHandle() {
        if (tableDataPartnerReceivable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = (TblPartner) tableDataPartnerReceivable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataPartnerReceivableFormShowStatus.set(1);
            isShowStatus.set(true);
        }
    }

    private void dataHotelReceivableUnshowHandle() {
        refreshDataTablePartnerReceivable();
        dataPartnerReceivableFormShowStatus.set(0);
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
            for (TblReservationPaymentWithGuaranteePayment dataRPWGP : (List<TblReservationPaymentWithGuaranteePayment>) tableDataDetail.getTableView().getItems()) {
                if (dataRPWGP.getTblHotelReceivable() != null //has been set as hotel receivable
                        && calculationTotalRestOfBill(dataRPWGP).compareTo(new BigDecimal("0")) == 1) {     //nominal rest of bill more than '0'
                    TblHotelFinanceTransactionHotelReceivable dataHFTHR = new TblHotelFinanceTransactionHotelReceivable();
                    dataHFTHR.setTblHotelFinanceTransaction(selectedDataHFT);
                    dataHFTHR.setTblHotelReceivable(dataRPWGP.getTblHotelReceivable());
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
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_receivable_and_payment/HotelFinanceTransactionInputDialog.fxml"));

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
                    selectedDataRPWGP = ((TblReservationPaymentWithGuaranteePayment) tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
                    //dummy
                    TblReservationPaymentWithGuaranteePayment dummySelectedDataRPWGP = new TblReservationPaymentWithGuaranteePayment(selectedDataRPWGP);
                    dummySelectedDataRPWGP.setTblPartner(new TblPartner(dummySelectedDataRPWGP.getTblPartner()));
                    if (dummySelectedDataRPWGP.getTblHotelReceivable() != null) {
                        dummySelectedDataRPWGP.setTblHotelReceivable(new TblHotelReceivable(dummySelectedDataRPWGP.getTblHotelReceivable()));
                        dummySelectedDataRPWGP.getTblHotelReceivable().setTblHotelInvoice(new TblHotelInvoice(dummySelectedDataRPWGP.getTblHotelReceivable().getTblHotelInvoice()));
                    }
                    //set data from field input
                    dummySelectedDataRPWGP.getTblHotelReceivable().getTblHotelInvoice().setCodeHotelInvoice(txtCodeInvoice.getText());
                    dummySelectedDataRPWGP.getTblHotelReceivable().getTblHotelInvoice().setHotelInvoiceSubject(txtSubject.getText());
                    dummySelectedDataRPWGP.getTblHotelReceivable().getTblHotelInvoice().setIssueDate(dtpIssueDate.getValue() != null
                            ? Date.valueOf(dtpIssueDate.getValue())
                            : null);
                    dummySelectedDataRPWGP.getTblHotelReceivable().getTblHotelInvoice().setDueDate(dtpDueDate.getValue() != null
                            ? Date.valueOf(dtpDueDate.getValue())
                            : null);
                    dummySelectedDataRPWGP.getTblHotelReceivable().getTblHotelInvoice().setHotelInvoiceNote(txtTerm.getText());
                    if (parentController.getService().insertDataHotelInvoice(dummySelectedDataRPWGP) != null) {
                        ClassMessage.showSucceedInsertingDataMessage("", null);
                        //set selected data input form (table-partner-hr)
                        tableDataDetail.getTableView().setItems(FXCollections.observableArrayList(loadAllDataRPWGP(selectedData)));
                        //set selected data input form detail
                        setSelectedDataToInputFormDetail(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1
                                ? ((TblReservationPaymentWithGuaranteePayment) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelReceivable()
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
         TblReservationPaymentWithGuaranteePayment selectedData = parentController.getService().getDataReservationPaymentWithGuaranteePayment(((TblReservationPaymentWithGuaranteePayment)tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getIddetail());
         List<ClassPrintInvoiceHotel>listInvoiceHotel = new ArrayList();
         ClassPrintInvoiceHotel printInvoiceHotel = new ClassPrintInvoiceHotel();
         printInvoiceHotel.setKodeInvoice(selectedData.getTblHotelReceivable().getTblHotelInvoice().getCodeHotelInvoice());
         printInvoiceHotel.setNamaPerusahaan(selectedData.getTblHotelReceivable().getTblHotelInvoice().getTblPartner().getPartnerName());
         printInvoiceHotel.setAlamatPerusahaan(selectedData.getTblHotelReceivable().getTblHotelInvoice().getTblPartner().getPartnerAddress());
         printInvoiceHotel.setTanggalFaktur(selectedData.getTblHotelReceivable().getTblHotelInvoice().getIssueDate()!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(selectedData.getTblHotelReceivable().getTblHotelInvoice().getIssueDate()):"-");
         printInvoiceHotel.setTanggalJatuhTempo(selectedData.getTblHotelReceivable().getTblHotelInvoice().getDueDate()!=null ? new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(selectedData.getTblHotelReceivable().getTblHotelInvoice().getDueDate()):"-");
         printInvoiceHotel.setSubject(selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceSubject()!=null ? selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceSubject():"-");
         printInvoiceHotel.setKeterangan(selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceNote()==null ? "-" : selectedData.getTblHotelReceivable().getTblHotelInvoice().getHotelInvoiceNote());
         printInvoiceHotel.setKodeReservasi(selectedData.getTblReservationPayment().getTblReservationBill().getTblReservation().getCodeReservation());
         BigDecimal totalPembayaran = new BigDecimal(0);
         List<TblHotelFinanceTransactionHotelReceivable>listPayment = new ArrayList();
         listPayment = parentController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(selectedData.getTblHotelReceivable().getIdhotelReceivable());
         for(TblHotelFinanceTransactionHotelReceivable getPayment : listPayment){
            totalPembayaran = totalPembayaran.add(getPayment.getNominalTransaction());
         }
         printInvoiceHotel.setTotalPembayaran(totalPembayaran);
         
         BigDecimal totalHarga = new BigDecimal(0);
         BigDecimal totalHargaServiceCharge = new BigDecimal(0);
         List<ClassPrintInvoiceHotelDetail>listHotelDetail = new ArrayList();
          List<TblGuaranteeLetterItemDetail>list = parentController.getService().getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(selectedData.getIddetail());
           for(TblGuaranteeLetterItemDetail getDetailPayment : list){
             ClassPrintInvoiceHotelDetail invoiceHotelDetail = new ClassPrintInvoiceHotelDetail();
           //  invoiceHotelDetail.setKodeTransaksi(getDetailPayment.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getCodePayment());
           //  invoiceHotelDetail.setKodeReservasi(getDetailPayment.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getTblReservation().getCodeReservation());
             invoiceHotelDetail.setNamaBarang(getDetailPayment.getDetailName()+" - "+new SimpleDateFormat("dd MMMM yyyy",new Locale("id")).format(getDetailPayment.getDetailDate()));
             invoiceHotelDetail.setHarga(getDetailPayment.getDetailCost());
             invoiceHotelDetail.setJumlah(getDetailPayment.getDetailQuantity());
             invoiceHotelDetail.setServiceCharge(getDetailPayment.getServiceChargePercentage());
             invoiceHotelDetail.setPajak(getDetailPayment.getTaxPercentage());
             invoiceHotelDetail.setTotal(invoiceHotelDetail.getHarga().multiply(invoiceHotelDetail.getJumlah()));
             listHotelDetail.add(invoiceHotelDetail);
           }
         printInvoiceHotel.setListInvoiceHotelDetail(listHotelDetail);
         listInvoiceHotel.add(printInvoiceHotel);
         ClassPrinter.printInvoiceHotel(listInvoiceHotel,printInvoiceHotel.getKodeInvoice(),printInvoiceHotel.getNamaPerusahaan(),printInvoiceHotel.getTanggalFaktur());
       }
    }

    private void dataPartnerReceivableBackHandle() {
        refreshDataTablePartnerReceivable();
        dataPartnerReceivableFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    public void refreshDataTablePartnerReceivable() {
        //refresh data from table & close form data detail
        tableDataPartnerReceivable.getTableView().setItems(FXCollections.observableArrayList(loadAllDataPartner()));
        cft.refreshFilterItems(tableDataPartnerReceivable.getTableView().getItems());
    }

    public void refreshSelectedData() {
        setSelectedDataToInputForm();
        dataPartnerReceivableFormShowStatus.set(1);
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
        setDataPartnerReceivableSplitpane();

        //init table
        initTableDataPartnerReceivable();

        //init form
        initFormDataPartnerReceivable();

        spDataPartnerReceivable.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPartnerReceivableFormShowStatus.set(0.0);
        });
    }

    public PartnerReceivableController(PartnerReceivableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final PartnerReceivableAndPaymentController parentController;

    public FPartnerManager getService() {
        return parentController.getService();
    }

}
