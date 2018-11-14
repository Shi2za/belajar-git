/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.partner_payment_x;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_partner.FeaturePartnerController;
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
            tableDataPartnerPaymentLayout.setDisable(false);
            tableDataPartnerPaymentLayoutDisableLayer.setDisable(true);
            tableDataPartnerPaymentLayout.toFront();
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

    private ClassTableWithControl tableDataPartnerPayment;

    private void initTableDataPartnerPayment() {
        //set table
        setTableDataPartnerPayment();
        //set control
        setTableControlDataPartnerPayment();
        //set table-control to content-view
        tableDataPartnerPaymentLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPartnerPayment, 15.0);
        AnchorPane.setLeftAnchor(tableDataPartnerPayment, 15.0);
        AnchorPane.setRightAnchor(tableDataPartnerPayment, 15.0);
        AnchorPane.setTopAnchor(tableDataPartnerPayment, 15.0);
        tableDataPartnerPaymentLayout.getChildren().add(tableDataPartnerPayment);
    }

    private void setTableDataPartnerPayment() {
        TableView<TblReservationPaymentWithGuaranteePayment> tableView = new TableView();

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> codeReservationPayment = new TableColumn("No. Transaksi");
        codeReservationPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationPayment().getCodePayment(),
                        param.getValue().tblReservationPaymentProperty()));
        codeReservationPayment.setMinWidth(140);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> partnerType = new TableColumn("Tipe");
        partnerType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getRefPartnerType().getTypeName(),
                        param.getValue().tblPartnerProperty()));
        partnerType.setMinWidth(120);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> partnerName = new TableColumn("Nama");
        partnerName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPartnerName(),
                        param.getValue().tblPartnerProperty()));
        partnerName.setMinWidth(140);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> partnerGroup = new TableColumn("Partner");
        partnerGroup.getColumns().addAll(partnerType, partnerName);

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

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> transactionDate = new TableColumn("Tanggal Transaksi");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getTblReservationPayment().getPaymentDate()),
                        param.getValue().tblReservationPaymentProperty()));
        transactionDate.setMinWidth(180);

        TableColumn<TblReservationPaymentWithGuaranteePayment, String> paymentStatus = new TableColumn("Status");
        paymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelReceivable().getRefFinanceTransactionStatus().getStatusName(),
                        param.getValue().tblHotelReceivableProperty()));
        paymentStatus.setMinWidth(140);
        
        TableColumn<TblReservationPaymentWithGuaranteePayment, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithGuaranteePayment, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeInvoice.setMinWidth(120);

        tableView.getColumns().addAll(codeReservationPayment, partnerGroup, totalBill, totalPayment, totalRestOfBill, transactionDate, paymentStatus, codeInvoice);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataHotelReceivalbeReservationPaymentWithGuaranteeLetter()));
        
        tableView.setRowFactory(tv -> {
            TableRow<TblReservationPaymentWithGuaranteePayment> row = new TableRow<>();
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

        tableDataPartnerPayment = new ClassTableWithControl(tableView);
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
    
    public BigDecimal calculationTotalBill(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        BigDecimal result = new BigDecimal("0");
        if (dataRPWGP != null
                && dataRPWGP.getTblHotelReceivable() != null) {
//            result = dataRPWGP.getTblReservationPayment().getUnitNominal();
            result = dataRPWGP.getTblHotelReceivable().getHotelReceivableNominal();
        }
        return result;
    }

    public BigDecimal calculationTotalPayment(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        BigDecimal result = new BigDecimal("0");
        if (dataRPWGP != null
                && dataRPWGP.getTblHotelReceivable() != null) {
            List<TblHotelFinanceTransaction> list = getService().getAllDataHotelFinanceTransactionByIDHotelReceivable(dataRPWGP.getTblHotelReceivable().getIdhotelReceivable());
            for (TblHotelFinanceTransaction data : list) {
                result = result.add(data.getTransactionNominal());
            }
        }
        return result;
    }

    public BigDecimal calculationTotalRestOfBill(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        return calculationTotalBill(dataRPWGP).subtract(calculationTotalPayment(dataRPWGP));
    }

    private void setTableControlDataPartnerPayment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Transaksi Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataPartnerPaymentCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
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

    private List<TblReservationPaymentWithGuaranteePayment> loadAllDataHotelReceivalbeReservationPaymentWithGuaranteeLetter() {
        List<TblReservationPaymentWithGuaranteePayment> list = new ArrayList<>();
        List<TblReservationPaymentWithGuaranteePayment> rpwgpList = getService().getAllDataReservationPaymentWithGuaranteePayment();
        for (TblReservationPaymentWithGuaranteePayment rpwgpData : rpwgpList) {
            if (rpwgpData.getTblHotelReceivable() != null) {
                //data hotel receivable
                rpwgpData.setTblHotelReceivable(getService().getDataHotelReceivable(rpwgpData.getTblHotelReceivable().getIdhotelReceivable()));
                //data hotel receivable type
                rpwgpData.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(rpwgpData.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
                //data finance transaction status
                rpwgpData.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(rpwgpData.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
                //data partner
                rpwgpData.setTblPartner(getService().getDataPartner(rpwgpData.getTblPartner().getIdpartner()));
                //data partner type
                rpwgpData.getTblPartner().setRefPartnerType(getService().getDataPartnerType(rpwgpData.getTblPartner().getRefPartnerType().getIdtype()));
                //data reservation payment
                rpwgpData.setTblReservationPayment(getService().getDataReservationPayment(rpwgpData.getTblReservationPayment().getIdpayment()));
                //data employee (created)
                rpwgpData.setTblEmployeeByCreateBy(getService().getDataEmployee(rpwgpData.getTblEmployeeByCreateBy().getIdemployee()));
                //add data to list
                list.add(rpwgpData);
            }
        }
        //remove data isn't used
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 3) {   //Sudah Tidak Berlaku = '3'
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
    private GridPane gpFormDataPartnerPayment;

    @FXML
    private ScrollPane spFormDataPartnerPayment;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblReservationPaymentWithGuaranteePayment selectedDataReservationPaymentWithGuaranteePayment;

    public TblHotelFinanceTransaction selectedData;

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

    }

    /**
     * TABLE DATA PURCHASE ORDER PAYMENT
     */
    private void setSelectedDataToInputForm(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        initTableDataDetail(dataRPWGP);

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        gpFormDataPartnerPayment.setDisable(dataInputStatus == 3);

//        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    @FXML
    private AnchorPane ancDetailPaymentLayout;

    public TableView<TblHotelFinanceTransaction> tableDataDetail;

    private void initTableDataDetail(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        //set table
        setTableDataDetail(dataRPWGP);
        //set table to content-view
        ancDetailPaymentLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailPaymentLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        tableDataDetail = new TableView();

        TableColumn<TblHotelFinanceTransaction, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory(cellData -> cellData.getValue().codeTransactionProperty());
        codePayment.setMinWidth(140);

        TableColumn<TblHotelFinanceTransaction, String> codeEmployee = new TableColumn("Employee");
        codeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByCreateBy().getCodeEmployee(), param.getValue().tblEmployeeByCreateByProperty()));
        codeEmployee.setMinWidth(120);

        TableColumn<TblHotelFinanceTransaction, String> paymentNominal = new TableColumn("Nominal Pembayaran");
        paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionNominal()), param.getValue().transactionNominalProperty()));
        paymentNominal.setMinWidth(150);

        TableColumn<TblHotelFinanceTransaction, String> paymentRoundingValue = new TableColumn("Nominal Pembulatan");
        paymentRoundingValue.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionRoundingValue()), 
                        param.getValue().transactionRoundingValueProperty()));
        paymentRoundingValue.setMinWidth(150);
        
        TableColumn<TblHotelFinanceTransaction, String> paymentDate = new TableColumn("Tanggal Pembayaran");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCreateDate()), param.getValue().createDateProperty()));
        paymentDate.setMinWidth(150);

        tableDataDetail.getColumns().addAll(codePayment, paymentDate, paymentNominal, paymentRoundingValue, codeEmployee);
        tableDataDetail.setItems(FXCollections.observableArrayList(loadAllDataHotelFinanceTransaction(dataRPWGP)));
    }

    private List<TblHotelFinanceTransaction> loadAllDataHotelFinanceTransaction(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
//        if (dataRPWGP != null
//                && dataRPWGP.getTblHotelReceivable() != null) {
//            list = getService().getAllDataHotelFinanceTransactionByIDHotelReceivable(dataRPWGP.getTblHotelReceivable().getIdhotelReceivable());
//            for (TblHotelFinanceTransaction data : list) {
//                //data hotel receivable
//                data.setTblHotelReceivable(getService().getDataHotelReceivable(data.getTblHotelReceivable().getIdhotelReceivable()));
//                //data finance transaction type
//                data.setRefFinanceTransactionType(getService().getDataFinanceTransactionType(data.getRefFinanceTransactionType().getIdtype()));
//            }
//        }
        return list;
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private int dataInputStatus = 0;

    public Stage dialogStageDetal;

    private void dataPartnerPaymentCreateHandle() {
//        if (tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            if (((TblReservationPaymentWithGuaranteePayment) tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItem()).getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() != 2) {   //2 = 'Lunas'
//                selectedDataReservationPaymentWithGuaranteePayment = (TblReservationPaymentWithGuaranteePayment) tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItem();
//                selectedData = new TblHotelFinanceTransaction();
//                selectedData.setTblHotelReceivable(selectedDataReservationPaymentWithGuaranteePayment.getTblHotelReceivable());
//                selectedData.setTransactionNominal(new BigDecimal("0"));
//                selectedData.setRefFinanceTransactionType(getService().getDataFinanceTransactionType(1));    //receivable = 1'
//                //open form data - detail (partner - payment)
//                showDataDetailDialog();
//            } else {
//                ClassMessage.showWarningInputDataMessage("Data tagihan Partner telah dibayar (lunas)..!!", null);;
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPaymentShowHandle() {
        if (tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedDataReservationPaymentWithGuaranteePayment = (TblReservationPaymentWithGuaranteePayment) tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItem();
//            selectedDataHotelReceivalbeReservationPaymentWithGuaranteeLetter = getService().getDataHotelReceivalbeReservationPaymentWithGuaranteeLetter(((TblHotelReceivalbeReservationPaymentWithGuaranteeLetter) tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
//            selectedDataHotelReceivalbeReservationPaymentWithGuaranteeLetter.setTblHotelReceivable(getService().getDataHotelReceivable(selectedDataHotelReceivalbeReservationPaymentWithGuaranteeLetter.getTblHotelReceivable().getIdhotelReceivable()));
//            selectedDataHotelReceivalbeReservationPaymentWithGuaranteeLetter.setTblReservationPaymentWithGuaranteePayment(getService().getDataReservationPaymentWithGuaranteePayment(selectedDataHotelReceivalbeReservationPaymentWithGuaranteeLetter.getTblReservationPaymentWithGuaranteePayment().getIddetail()));
            setSelectedDataToInputForm(selectedDataReservationPaymentWithGuaranteePayment);
            dataPartnerPaymentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPaymentUnshowHandle() {
        tableDataPartnerPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelReceivalbeReservationPaymentWithGuaranteeLetter()));
        dataPartnerPaymentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPartnerPaymentPrintHandle() {
        if (tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printPartnerPayment(((TblReservationPaymentWithGuaranteePayment) tableDataPartnerPayment.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printPartnerPayment(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = getService().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        //print data - preview
//        ClassPrinter.printKuitansiPartner(hotelLogoName,
//                dataRPWGP);
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_payment/PartnerPaymentDetailDialog.fxml"));

            PartnerPaymentDetailController controller = new PartnerPaymentDetailController(this);
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
        tableDataPartnerPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelReceivalbeReservationPaymentWithGuaranteeLetter()));
        //data table detail
        setSelectedDataToInputForm(null);
        //close form table detail
        dataPartnerPaymentFormShowStatus.set(0.0);
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

    public PartnerPaymentController(FeaturePartnerController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePartnerController parentController;

    public FPartnerManager getService() {
        return parentController.getFPartnerManager();
    }

}
