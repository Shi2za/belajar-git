/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_resto.resto_payable_and_payment;

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
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblResto;
import hotelfx.persistence.service.FRestoManager;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
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
 * @author ABC-Programmer
 */
public class RestoPayableController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRestoPayable;

    private DoubleProperty dataRestoPayableFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRestoPayableLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPOPayableSplitpane() {
        spDataRestoPayable.setDividerPositions(1);

        dataRestoPayableFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRestoPayableFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRestoPayable.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRestoPayable.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRestoPayableFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataRestoPayableLayout.setDisable(false);
            tableDataRestoPayableLayoutDisableLayer.setDisable(true);
            tableDataRestoPayableLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataRestoPayableLayout.setDisable(false);
                    tableDataRestoPayableLayoutDisableLayer.setDisable(true);
                    tableDataRestoPayableLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataRestoPayableLayout.setDisable(true);
                    tableDataRestoPayableLayoutDisableLayer.setDisable(false);
                    tableDataRestoPayableLayoutDisableLayer.toFront();
                }
            }
        });

        dataRestoPayableFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataRestoPayableLayout;

    private ClassFilteringTable<TblResto> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRestoPayable;

    private void initTableDataRestoPayable() {
        //set table
        setTableDataRestoPayable();
        //set control
        setTableControlDataRestoPayable();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRestoPayable, 15.0);
        AnchorPane.setLeftAnchor(tableDataRestoPayable, 15.0);
        AnchorPane.setRightAnchor(tableDataRestoPayable, 15.0);
        AnchorPane.setTopAnchor(tableDataRestoPayable, 15.0);
        ancBodyLayout.getChildren().add(tableDataRestoPayable);
    }

    private void setTableDataRestoPayable() {
        TableView<TblResto> tableView = new TableView();

        TableColumn<TblResto, String> codeResto = new TableColumn("ID");
        codeResto.setCellValueFactory(cellData -> cellData.getValue().codeRestoProperty());
        codeResto.setMinWidth(120);

        TableColumn<TblResto, String> restoName = new TableColumn("Resto");
        restoName.setCellValueFactory(cellData -> cellData.getValue().restoNameProperty());
        restoName.setMinWidth(140);

//        TableColumn<TblResto, String> picName = new TableColumn("Nama");
//        picName.setCellValueFactory(cellData -> cellData.getValue().picnameProperty());
//        picName.setMinWidth(140);
//
//        TableColumn<TblResto, String> picPhoneNumber = new TableColumn("Telepon");
//        picPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().picphoneNumberProperty());
//        picPhoneNumber.setMinWidth(120);
//
//        TableColumn<TblResto, String> picEmailAddress = new TableColumn("Email");
//        picEmailAddress.setCellValueFactory(cellData -> cellData.getValue().picemailAddressProperty());
//        picEmailAddress.setMinWidth(200);
//
//        TableColumn<TblResto, String> picTitle = new TableColumn("PIC");
//        picTitle.getColumns().addAll(picName, picPhoneNumber, picEmailAddress);
        TableColumn<TblResto, String> totalHotelPayable = new TableColumn("Total Tagihan");
        totalHotelPayable.setCellValueFactory((TableColumn.CellDataFeatures<TblResto, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(getTotalHotelPayable(param.getValue())),
                        param.getValue().idrestoProperty()));
        totalHotelPayable.setMinWidth(140);

//        TableColumn<TblResto, String> minDueDate = new TableColumn("Tgl. Estimasi Bayar\n"
//                + "(paling dekat)");
//        minDueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblResto, String> param)
//                -> Bindings.createStringBinding(() -> getMinDueDate(param.getValue()),
//                        param.getValue().idrestoProperty()));
//        minDueDate.setMinWidth(160);
        tableView.getColumns().addAll(codeResto, restoName, totalHotelPayable);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataResto()));

        tableView.setRowFactory(tv -> {
            TableRow<TblResto> row = new TableRow<>();
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

        tableDataRestoPayable = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblResto.class,
                tableDataRestoPayable.getTableView(),
                tableDataRestoPayable.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private BigDecimal getTotalHotelPayable(TblResto resto) {
        BigDecimal result = new BigDecimal("0");
        if (resto != null) {
            List<TblReservationAdditionalService> rasList = loadAllDataReservationAdditionalService(resto);
            for (TblReservationAdditionalService rasData : rasList) {
                if (rasData.getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                        || rasData.getTblRoomService().getIdroomService() == 7) {    //Dine in Resto = '7'
                    if (rasData.getTblHotelPayable() != null) {
                        result = result.add(rasData.getTblHotelPayable().getHotelPayableNominal());
                        //hotel finance transaction
                        List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(rasData.getTblHotelPayable().getIdhotelPayable());
                        for (TblHotelFinanceTransactionHotelPayable data : list) {
                            if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                                result = result.add(data.getNominalTransaction());
                            } else {
                                result = result.subtract(data.getNominalTransaction());
                            }
                        }
                    } else {
                        result = result.add(calculationTotal(rasData));
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal getTotalHotelPayableJustHaveCodeInvoiceForPayment(TblResto resto) {
        BigDecimal result = new BigDecimal("0");
        if (resto != null) {
            List<TblReservationAdditionalService> rasList = loadAllDataReservationAdditionalService(resto);
            for (TblReservationAdditionalService rasData : rasList) {
                if ((rasData.getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                        || rasData.getTblRoomService().getIdroomService() == 7) //Dine in Resto = '7'
                        && rasData.getTblHotelPayable() != null) {
                    BigDecimal rob = rasData.getTblHotelPayable().getHotelPayableNominal();
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(rasData.getTblHotelPayable().getIdhotelPayable());
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

    private BigDecimal getTotalHotelPayableJustHaveCodeInvoiceForReturn(TblResto resto) {
        BigDecimal result = new BigDecimal("0");
        if (resto != null) {
            List<TblReservationAdditionalService> rasList = loadAllDataReservationAdditionalService(resto);
            for (TblReservationAdditionalService rasData : rasList) {
                if ((rasData.getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                        || rasData.getTblRoomService().getIdroomService() == 7) //Dine in Resto = '7'
                        && rasData.getTblHotelPayable() != null) {
                    BigDecimal rob = rasData.getTblHotelPayable().getHotelPayableNominal();
                    //hotel finance transaction
                    List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(rasData.getTblHotelPayable().getIdhotelPayable());
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

    private String getMinDueDate(TblResto resto) {
        if (resto != null) {
            LocalDate localDate = null;
            List<TblReservationAdditionalService> rasList = loadAllDataReservationAdditionalService(resto);
            for (TblReservationAdditionalService rasData : rasList) {
                if ((rasData.getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                        || rasData.getTblRoomService().getIdroomService() == 7) //Dine in Resto = '7'
                        && rasData.getTblHotelPayable() != null
                        && (rasData.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 0 //Belum Dibayar = '0'
                        || rasData.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 1) //Dibayar Sebagian = '1'
                        && rasData.getTblHotelPayable().getTblHotelInvoice() != null
                        && rasData.getTblHotelPayable().getTblHotelInvoice().getDueDate() != null) {
                    LocalDate countDate = LocalDate.of(rasData.getTblHotelPayable().getTblHotelInvoice().getDueDate().getYear() + 1900,
                            rasData.getTblHotelPayable().getTblHotelInvoice().getDueDate().getMonth() + 1,
                            rasData.getTblHotelPayable().getTblHotelInvoice().getDueDate().getDate());
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

    private List<TblResto> loadAllDataResto() {
        List<TblResto> list = parentController.getService().getAllDataResto();
        return list;
    }

    public BigDecimal calculationTotal(TblReservationAdditionalService dataRAS) {
        return dataRAS.getPrice();
    }

    public BigDecimal calculationTotalBill(TblReservationAdditionalService dataRAS) {
        BigDecimal result = new BigDecimal("0");
        if (dataRAS != null) {
            if (dataRAS.getTblHotelPayable() != null) {
                result = dataRAS.getTblHotelPayable().getHotelPayableNominal();
            } else {
                result = calculationTotal(dataRAS);
            }
        }
        return result;
    }

    public BigDecimal calculationTotalPayment(TblReservationAdditionalService dataRAS) {
        BigDecimal result = new BigDecimal("0");
        if (dataRAS != null
                && dataRAS.getTblHotelPayable() != null) {
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(dataRAS.getTblHotelPayable().getIdhotelPayable());
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

    public BigDecimal calculationTotalRestOfBill(TblReservationAdditionalService dataRAS) {
        return calculationTotalBill(dataRAS).subtract(calculationTotalPayment(dataRAS));
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

    private void setTableControlDataRestoPayable() {
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
        tableDataRestoPayable.addButtonControl(buttonControls);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

//    @FXML
//    private GridPane gpFormDataRestoPayable;
    @FXML
    private ScrollPane spFormDataRestoPayable;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXTextField txtResto;

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

    public TblResto selectedData;

    public TblReservationAdditionalService selectedDataRAS;

    private void initFormDataRestoPayable() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRestoPayable.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
//        gpFormDataRestoPayable.setOnScroll((ScrollEvent scroll) -> {
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
            dataRestoPayableBackHandle();
        });
        btnBack.setVisible(false);

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
     * TABLE DATA RESTO PAYABLE
     */
    private final PseudoClass notPaidPseudoClass = PseudoClass.getPseudoClass("notpaid");

    private final PseudoClass overPaymentPseudoClass = PseudoClass.getPseudoClass("overpayment");

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getRestoName());

        txtResto.setText(selectedData.getRestoName());

//        txtPICName.setText(selectedData.getPicname());
//        txtPICPhoneNumber.setText(selectedData.getPicphoneNumber());
        refreshDataTotalHotelPayable();

        initTableDataDetail(selectedData);

//        //set selected data input form detail
//        setSelectedDataToInputFormDetail(tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1
//                ? ((TblReservationAdditionalService) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblHotelPayable()
//                : null);
        setSelectedDataToInputFormFunctionalComponent();
    }

    public void refreshDataTotalHotelPayable() {
        lblTotalHotelPayable.setText(ClassFormatter.currencyFormat.format(getTotalHotelPayable(selectedData)));
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ancTableDetailLayout.setDisable(dataInputStatus == 3);

//        gpFormDataRestoPayable.setDisable(dataInputStatus == 3);
//        btnSave.setVisible(dataInputStatus != 3);
//        btnPrint.setDisable(false);
    }

    public ClassTableWithControl tableDataDetail;

    private void initTableDataDetail(TblResto dataResto) {
        //set table
        setTableDataDetail(dataResto);
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

    private void setTableDataDetail(TblResto dataResto) {
        TableView<TblReservationAdditionalService> tableView = new TableView();

        TableColumn<TblReservationAdditionalService, String> codeRestoBill = new TableColumn("No. Tagihan");
        codeRestoBill.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRestoTransactionNumber(),
                        param.getValue().restoTransactionNumberProperty()));
        codeRestoBill.setMinWidth(120);

        TableColumn<TblReservationAdditionalService, String> totalBill = new TableColumn("Total Tagihan");
        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())),
                        param.getValue().idadditionalProperty()));
        totalBill.setMinWidth(140);

        TableColumn<TblReservationAdditionalService, String> paymentsDate = new TableColumn("Tanggal \nPembayaran");
        paymentsDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> getRestoPaymentsDate(param.getValue()),
                        param.getValue().idadditionalProperty()));
        paymentsDate.setMinWidth(110);

//        TableColumn<TblReservationAdditionalService, String> totalPayment = new TableColumn("Nominal Pembayaran");
//        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
//                -> Bindings.createStringBinding(() -> getPOPaymentNominal(param.getValue()),
//                        param.getValue().idadditionalProperty()));
//        totalPayment.setMinWidth(140);
        TableColumn<TblReservationAdditionalService, String> totalPayment = new TableColumn("Total Pembayaran");
        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())),
                        param.getValue().idadditionalProperty()));
        totalPayment.setMinWidth(140);

//        TableColumn<TblReservationAdditionalService, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
//        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
//                -> Bindings.createStringBinding(() -> getPOPaymentRestOfBill(param.getValue()),
//                        param.getValue().idpoProperty()));
//        totalRestOfBill.setMinWidth(140);
        TableColumn<TblReservationAdditionalService, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idadditionalProperty()));
        totalRestOfBill.setMinWidth(140);

        TableColumn<TblReservationAdditionalService, String> totalNominal = new TableColumn("Total Nominal");
        totalNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> "Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())) + "\n"
                        + "Pembayaran " + ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())) + "\n"
                        + "Sisa Tagihan " + ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idadditionalProperty()));
        totalNominal.setMinWidth(180);

        TableColumn<TblReservationAdditionalService, String> dueDate = new TableColumn("Tanggal \nEstimasi Bayar");
        dueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> getPOPaymentDueDate(param.getValue()),
                        param.getValue().idadditionalProperty()));
        dueDate.setMinWidth(110);

        TableColumn<TblReservationAdditionalService, String> paymentStatus = new TableColumn("Status");
        paymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelPayable() != null
                                ? param.getValue().getTblHotelPayable().getRefFinanceTransactionStatus().getStatusName()
                                : "Belum Dibayar", param.getValue().tblHotelPayableProperty()));
        paymentStatus.setMinWidth(120);

        TableColumn<TblReservationAdditionalService, String> paymentNote = new TableColumn("Keterangan");
        paymentNote.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getNote(),
                        param.getValue().noteProperty()));
        paymentNote.setMinWidth(190);

        TableColumn<TblReservationAdditionalService, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeInvoice.setMinWidth(100);

//        tableView.getColumns().addAll(codeInvoice, codePO, dueDate, totalBill, paymentsDate, totalPayment, totalRestOfBill, paymentStatus);
        tableView.getColumns().addAll(codeInvoice, codeRestoBill, dueDate, totalBill, totalPayment, totalRestOfBill, paymentStatus, paymentNote);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataReservationAdditionalService(dataResto)));

//        tableView.setRowFactory(tv -> {
//            TableRow<TblReservationAdditionalService> row = new TableRow<>();
//            row.setOnMouseClicked((e) -> {
//                if (!row.isEmpty()) {
//                    if (!ClassSession.unSavingDataInput.get()) {
//                        //set data form input detail
//                        setSelectedDataToInputFormDetail(
//                                ((TblReservationAdditionalService) row.getItem()) != null
//                                        ? ((TblReservationAdditionalService) row.getItem()).getTblHotelPayable()
//                                        : null);
//                    } else {  //unsaving data input
//                        Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
//                        if (alert.getResult() == ButtonType.OK) {
//                            ClassSession.unSavingDataInput.set(false);
//                            //set data form input detail
//                            setSelectedDataToInputFormDetail(
//                                    ((TblReservationAdditionalService) row.getItem()) != null
//                                            ? ((TblReservationAdditionalService) row.getItem()).getTblHotelPayable()
//                                            : null);
//                        } else {
//                            if (tableDataDetail != null
//                                    && tableDataDetail.getTableView() != null) {
//                                tableDataDetail.getTableView().getSelectionModel().clearSelection();
//                                if (idInvoice != 0L) {
//                                    for(int i=0; i<tableDataDetail.getTableView().getItems().size(); i++){
//                                        if(((TblReservationAdditionalService)tableDataDetail.getTableView().getItems().get(i)).getTblHotelPayable()!= null
//                                                && ((TblReservationAdditionalService)tableDataDetail.getTableView().getItems().get(i)).getTblHotelPayable().getTblHotelInvoice() != null
//                                                && ((TblReservationAdditionalService)tableDataDetail.getTableView().getItems().get(i)).getTblHotelPayable().getTblHotelInvoice().getIdhotelInvoice()
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
            TableRow<TblReservationAdditionalService> row = new TableRow<>();

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

    private String getPOPaymentDueDate(TblReservationAdditionalService ras) {
        if (ras != null
                && ras.getTblHotelPayable() != null
                && ras.getTblHotelPayable().getTblHotelInvoice() != null
                && ras.getTblHotelPayable().getTblHotelInvoice().getDueDate() != null) {
            return ClassFormatter.dateFormate.format(ras.getTblHotelPayable().getTblHotelInvoice().getDueDate());
        }
        return "-";
    }

    private String getRestoPaymentsDate(TblReservationAdditionalService ras) {
        if (ras != null
                && ras.getTblHotelPayable() != null) {
            String result = "";
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(ras.getTblHotelPayable().getIdhotelPayable());
            for (int i = 0; i < list.size(); i++) {
                result += ClassFormatter.dateFormate.format(list.get(i).getTblHotelFinanceTransaction().getCreateDate()) + "\n";
            }
            return list.isEmpty() ? "-" : result;
        }
        return "-";
    }

    private String getRestoPaymentNominal(TblReservationAdditionalService ras) {
        if (ras != null
                && ras.getTblHotelPayable() != null) {
            String result = "";
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(ras.getTblHotelPayable().getIdhotelPayable());
            for (int i = 0; i < list.size(); i++) {
                result += (list.get(i).getTblHotelFinanceTransaction().getIsReturnTransaction() ? "-" : "")
                        + ClassFormatter.currencyFormat.cFormat(list.get(i).getNominalTransaction()) + "\n";
            }
            return list.isEmpty() ? "0" : result;
        }
        return "0";
    }

    private String getRestoPaymentRestOfBill(TblReservationAdditionalService ras) {
        if (ras != null
                && ras.getTblHotelPayable() != null) {
            String result = "";
            BigDecimal restOfBill = ras.getTblHotelPayable().getHotelPayableNominal();
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(ras.getTblHotelPayable().getIdhotelPayable());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                    restOfBill = restOfBill.add(list.get(i).getNominalTransaction());
                } else {
                    restOfBill = restOfBill.subtract(list.get(i).getNominalTransaction());
                }
                result += (list.get(i).getTblHotelFinanceTransaction().getIsReturnTransaction() ? "-" : "")
                        + ClassFormatter.currencyFormat.cFormat(restOfBill) + "\n";
            }
            return list.isEmpty() ? ClassFormatter.currencyFormat.cFormat(ras.getTblHotelPayable().getHotelPayableNominal()) : result;
        }
        return ClassFormatter.currencyFormat.cFormat(calculationTotal(ras));
    }

    private boolean isNotPaid(TblReservationAdditionalService ras) {
        boolean isNotPaid = false;
        if (ras != null
                && ras.getTblHotelPayable() != null) {
            return ras.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() != 2 //Sudah Dibayar = '2'
                    && ras.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() != 5; //Kelebihan Bayar = '5'
        }
        return isNotPaid;
    }

    private boolean isOverPayment(TblReservationAdditionalService ras) {
        boolean isNotPaid = false;
        if (ras != null
                && ras.getTblHotelPayable() != null) {
            return ras.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 5; //Kelebihan Bayar = '5'
        }
        return isNotPaid;
    }

    private List<TblReservationAdditionalService> loadAllDataReservationAdditionalService(TblResto dataResto) {
        List<TblReservationAdditionalService> list = new ArrayList<>();
        if (dataResto != null) {
            list = parentController.getService().getAllDataReservationAdditionalService();
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getTblRoomService().getIdroomService() != 6     //Room Dining = '6'
                        && list.get(i).getTblRoomService().getIdroomService() != 7) {   //Dine in Resto = '7'
                    list.remove(i);
                }
            }
            for (TblReservationAdditionalService data : list) {
                //data room service
                data.setTblRoomService(parentController.getService().getDataRoomService(data.getTblRoomService().getIdroomService()));
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
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Set/Update Data Invoice");
//            buttonControl.setTooltip(new Tooltip("Set/Update Data Invoice"));
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener create/update data invoice
//                dataSetInvoiceUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
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
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Buat Transaksi Pengembalian Pembayaran");
//            buttonControl.setTooltip(new Tooltip("Buat Transaksi Pengembalian Pembayaran"));
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener create return - payment
//                dataHotelFinanceTransactionReturnCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
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
        if (tableDataRestoPayable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = (TblResto) tableDataRestoPayable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataRestoPayableFormShowStatus.set(0);
            dataRestoPayableFormShowStatus.set(1);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataHotelPayableShowHandle() {
        if (tableDataRestoPayable.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = (TblResto) tableDataRestoPayable.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataRestoPayableFormShowStatus.set(1);
            isShowStatus.set(true);
        }
    }

    private void dataHotelPayableUnshowHandle() {
        refreshDataTableRestoPayable();
        dataRestoPayableFormShowStatus.set(0);
        isShowStatus.set(false);
    }

//    private void dataSetInvoiceUpdateHandle() {
//        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            if (checkDataPOEnableToUpdateInvoice(((TblPurchaseOrder) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()))) {
//                selectedDataPO = ((TblPurchaseOrder) tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
//                showDataInvoiceDialog();
//            } else {
//                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT SET/UPDATE DATA PO",
//                        "Data PO sudah tidak berlaku (data PO telah ditutup)..!", null);
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    private boolean checkDataPOEnableToUpdateInvoice(TblPurchaseOrder dataPO) {
//        return dataPO.getRefPurchaseOrderStatus().getIdstatus() != 6;   //Selesai = '6'
//    }
//
//    private void showDataInvoiceDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/PurchaseOrderPaymentInvoiceDialog.fxml"));
//
//            PurchaseOrderPaymentInvoiceController controller = new PurchaseOrderPaymentInvoiceController(this);
//            loader.setController(controller);
//
//            Region page = loader.load();
//
//            // Create the dialog Stage.
//            dialogStageDetal = new Stage();
//            dialogStageDetal.initModality(Modality.WINDOW_MODAL);
//            dialogStageDetal.initOwner(HotelFX.primaryStage);
//
//            //undecorated
//            Undecorator undecorator = new Undecorator(dialogStageDetal, page);
//            undecorator.getStylesheets().add("skin/undecorator.css");
//            undecorator.getMenuButton().setVisible(false);
//            undecorator.getMaximizeButton().setVisible(false);
//            undecorator.getMinimizeButton().setVisible(false);
//            undecorator.getFullScreenButton().setVisible(false);
//            undecorator.getCloseButton().setVisible(false);
//
//            Scene scene = new Scene(undecorator);
//            scene.setFill(Color.TRANSPARENT);
//
//            dialogStageDetal.initStyle(StageStyle.TRANSPARENT);
//            dialogStageDetal.setScene(scene);
//            dialogStageDetal.setResizable(false);
//
//            // Show the dialog and wait until the user closes it
//            dialogStageDetal.showAndWait();
//        } catch (IOException e) {
//            System.out.println("Err >> " + e.toString());
//        }
//    }
    public void refreshDataTableDetail() {
        tableDataDetail.getTableView().setItems(FXCollections.observableArrayList(loadAllDataReservationAdditionalService(selectedData)));
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
            for (TblReservationAdditionalService dataRAS : (List<TblReservationAdditionalService>) tableDataDetail.getTableView().getItems()) {
                if (dataRAS.getTblHotelPayable() != null //has been set as hotel payable
                        && calculationTotalRestOfBill(dataRAS).compareTo(new BigDecimal("0")) == 1) {     //nominal rest of bill more then '0'
                    TblHotelFinanceTransactionHotelPayable dataHFTHP = new TblHotelFinanceTransactionHotelPayable();
                    dataHFTHP.setTblHotelFinanceTransaction(selectedDataHFT);
                    dataHFTHP.setTblHotelPayable(dataRAS.getTblHotelPayable());
                    dataHFTHP.setNominalTransaction(new BigDecimal("0"));
                    //add to list
                    selectedDataHFTHPs.add(dataHFTHP);
                }
            }
            showDataHotelFinanceTransactionDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MEMBUAT TRANSAKSI PEMBAYARAN",
                    "Tidak ada data hutang yang harus dibayar..!", null);
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
            loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/HotelFinanceTransactionInputDialog.fxml"));

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

//    private void dataHotelFinanceTransactionReturnCreateHandle() {
//        if (checkEnableToCreateHotelFinanceTransactionReturn()) {
//            selectedDataHFT = new TblHotelFinanceTransaction();
//            selectedDataHFT.setTransactionNominal(new BigDecimal("0"));
//            selectedDataHFT.setTransactionRoundingValue(new BigDecimal("0"));
//            selectedDataHFT.setIsReturnTransaction(true);
//            selectedDataHFT.setRefFinanceTransactionType(parentController.getService().getDataFinanceTransactionType(0));    //payable = '0'
//            selectedDataHFTWithCash = new TblHotelFinanceTransactionWithCash();
//            selectedDataHFTWithCash.setTblHotelFinanceTransaction(selectedDataHFT);
//            selectedDataHFTWithTransfer = new TblHotelFinanceTransactionWithTransfer();
//            selectedDataHFTWithTransfer.setTblHotelFinanceTransaction(selectedDataHFT);
//            selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
//            selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(selectedDataHFT);
//            selectedDataHFTHPs = new ArrayList<>();
//            for (TblPurchaseOrder dataPO : (List<TblPurchaseOrder>) tableDataDetail.getTableView().getItems()) {
//                if (dataPO.getTblHotelPayable() != null //has been set as hotel payable
//                        && calculationTotalRestOfBill(dataPO).compareTo(new BigDecimal("0")) == -1) {     //nominal rest of bill lsss then '0'
//                    TblHotelFinanceTransactionHotelPayable dataHFTHP = new TblHotelFinanceTransactionHotelPayable();
//                    dataHFTHP.setTblHotelFinanceTransaction(selectedDataHFT);
//                    dataHFTHP.setTblHotelPayable(dataPO.getTblHotelPayable());
//                    dataHFTHP.setNominalTransaction(new BigDecimal("0"));
//                    //add to list
//                    selectedDataHFTHPs.add(dataHFTHP);
//                }
//            }
//            showDataHotelFinanceTransactionReturnDialog();
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MEMBUAT TRANSAKSI PENGEMBALIAN PEMBAYARAN",
//                    "Tidak ada data pembayaran hutang yang harus dikembalikan \n(*semua data hutang harus memiliki nomor invoice)..!", null);
//        }
//    }
//
//    private boolean checkEnableToCreateHotelFinanceTransactionReturn() {
//        return getTotalHotelPayableJustHaveCodeInvoiceForReturn(selectedData).compareTo(new BigDecimal("0")) == -1;
//    }
//
//    private void showDataHotelFinanceTransactionReturnDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/HotelFinanceTransactionReturnInputDialog.fxml"));
//
//            HotelFinanceTransactionReturnInputController controller = new HotelFinanceTransactionReturnInputController(this);
//            loader.setController(controller);
//
//            Region page = loader.load();
//
//            // Create the dialog Stage.
//            dialogStageDetal = new Stage();
//            dialogStageDetal.initModality(Modality.WINDOW_MODAL);
//            dialogStageDetal.initOwner(HotelFX.primaryStage);
//
//            //undecorated
//            Undecorator undecorator = new Undecorator(dialogStageDetal, page);
//            undecorator.getStylesheets().add("skin/undecorator.css");
//            undecorator.getMenuButton().setVisible(false);
//            undecorator.getMaximizeButton().setVisible(false);
//            undecorator.getMinimizeButton().setVisible(false);
//            undecorator.getFullScreenButton().setVisible(false);
//            undecorator.getCloseButton().setVisible(false);
//
//            Scene scene = new Scene(undecorator);
//            scene.setFill(Color.TRANSPARENT);
//
//            dialogStageDetal.initStyle(StageStyle.TRANSPARENT);
//            dialogStageDetal.setScene(scene);
//            dialogStageDetal.setResizable(false);
//
//            // Show the dialog and wait until the user closes it
//            dialogStageDetal.showAndWait();
//        } catch (IOException e) {
//            System.out.println("Err >> " + e.toString());
//        }
//    }
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
    private void dataRestoPayableBackHandle() {
        refreshDataTableRestoPayable();
        dataRestoPayableFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    public void refreshDataTableRestoPayable() {
        //refresh data from table & close form data detail
        tableDataRestoPayable.getTableView().setItems(FXCollections.observableArrayList(loadAllDataResto()));
        cft.refreshFilterItems(tableDataRestoPayable.getTableView().getItems());
    }

    public void refreshSelectedData() {
        setSelectedDataToInputForm();
        dataRestoPayableFormShowStatus.set(1);
    }

//    public void refreshData() {
//        //data table
//        tableDataRestoPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelPayableReservationAdditionalService()));
//        //data table detail
//        setSelectedDataToInputForm(null);
//        //close form table detail
//        dataRestoPaymentFormShowStatus.set(0.0);
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
        initTableDataRestoPayable();

        //init form
        initFormDataRestoPayable();

        spDataRestoPayable.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRestoPayableFormShowStatus.set(1.0);
        });
        //auto select first data & open form dialog input
        tableDataRestoPayable.getTableView().getSelectionModel().select(0);
        dataHotelPayableUpdateHandle();
    }

    public RestoPayableController(RestoPayableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final RestoPayableAndPaymentController parentController;

    public FRestoManager getService() {
        return parentController.getService();
    }

}
