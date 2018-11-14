/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_open_close_cashier_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import hotelfx.persistence.service.FOpenCloseCashierBalanceManager;
import hotelfx.persistence.service.FOpenCloseCashierBalanceManagerImpl;
import hotelfx.view.DashboardController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
public class OpenCloseCashierBalanceController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataOpenCloseCashierBalance;

    private DoubleProperty dataOpenCloseCashierBalanceFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataOpenCloseCashierBalanceLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataOpenCloseCashierBalanceSplitpane() {
        spDataOpenCloseCashierBalance.setDividerPositions(1);
        
        dataOpenCloseCashierBalanceFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataOpenCloseCashierBalanceFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataOpenCloseCashierBalance.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataOpenCloseCashierBalance.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataOpenCloseCashierBalanceFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataOpenCloseCashierBalanceLayout.setDisable(false);
            tableDataOpenCloseCashierBalanceLayoutDisableLayer.setDisable(true);
            tableDataOpenCloseCashierBalanceLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataOpenCloseCashierBalanceLayout.setDisable(false);
                    tableDataOpenCloseCashierBalanceLayoutDisableLayer.setDisable(true);
                    tableDataOpenCloseCashierBalanceLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataOpenCloseCashierBalanceLayout.setDisable(true);
                    tableDataOpenCloseCashierBalanceLayoutDisableLayer.setDisable(false);
                    tableDataOpenCloseCashierBalanceLayoutDisableLayer.toFront();
                }
            }
        });

        dataOpenCloseCashierBalanceFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataOpenCloseCashierBalanceLayout;

    private ClassFilteringTable<TblOpenCloseCashierBalance> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;
    
    private ClassTableWithControl tableDataOpenCloseCashierBalance;

    private final PseudoClass activePseudoClass = PseudoClass.getPseudoClass("aktif");

    private void initTableDataOpenCloseCashierBalance() {
        //set table
        setTableDataOpenCloseCashierBalance();
        //set control
        setTableControlDataOpenCloseCashierBalance();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataOpenCloseCashierBalance, 15.0);
        AnchorPane.setLeftAnchor(tableDataOpenCloseCashierBalance, 15.0);
        AnchorPane.setRightAnchor(tableDataOpenCloseCashierBalance, 15.0);
        AnchorPane.setTopAnchor(tableDataOpenCloseCashierBalance, 15.0);
        ancBodyLayout.getChildren().add(tableDataOpenCloseCashierBalance);
    }

    private void setTableDataOpenCloseCashierBalance() {
        TableView<TblOpenCloseCashierBalance> tableView = new TableView();

        TableColumn<TblOpenCloseCashierBalance, String> cashier = new TableColumn("Kasir");
        cashier.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdcashier().getCodeEmployee()
                        + " - "
                        + param.getValue().getTblEmployeeByIdcashier().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeByIdcashierProperty()));
        cashier.setMinWidth(240);

        TableColumn<TblOpenCloseCashierBalance, String> startDateTime = new TableColumn("Buka");
        startDateTime.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getOpenDateTime() != null
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getOpenDateTime()) : "-",
                        param.getValue().openDateTimeProperty()));
        startDateTime.setMinWidth(160);

        TableColumn<TblOpenCloseCashierBalance, String> endDateTime = new TableColumn("Tutup");
        endDateTime.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getCloseDateTime() != null
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCloseDateTime()) : "-",
                        param.getValue().closeDateTimeProperty()));
        endDateTime.setMinWidth(160);

        TableColumn<TblOpenCloseCashierBalance, String> dateTime = new TableColumn("Tanggal/Jam");
        dateTime.getColumns().addAll(startDateTime, endDateTime);

        TableColumn<TblOpenCloseCashierBalance, String> beginBalanceNominal = new TableColumn("Kas Awal");
        beginBalanceNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBeginBalanceNominal()),
                        param.getValue().beginBalanceNominalProperty()));
        beginBalanceNominal.setMinWidth(120);

        TableColumn<TblOpenCloseCashierBalance, String> incomeNominal = new TableColumn("Pemasukan");
        incomeNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalIncome(param.getValue())),
                        param.getValue().idopenCloseCashierBalanceProperty()));
        incomeNominal.setMinWidth(120);

        TableColumn<TblOpenCloseCashierBalance, String> expenseNominal = new TableColumn("Pengeluaran");
        expenseNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalExpense(param.getValue())),
                        param.getValue().idopenCloseCashierBalanceProperty()));
        expenseNominal.setMinWidth(120);

        TableColumn<TblOpenCloseCashierBalance, String> systemEndBalanceNominal = new TableColumn("System");
        systemEndBalanceNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefOpenCloseCashierBalanceStatus().getIdstatus() == 0 //Tidak Aktif = '0'
                        ? ClassFormatter.currencyFormat.cFormat(param.getValue().getSystemEndBalanceNominal())
                        : ClassFormatter.currencyFormat.cFormat(getCurrentCashierBalanceNominal()),
                        param.getValue().systemEndBalanceNominalProperty()));
        systemEndBalanceNominal.setMinWidth(120);

        TableColumn<TblOpenCloseCashierBalance, String> realEndBalanceNominal = new TableColumn("Real");
        realEndBalanceNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getRealEndBalanceNominal(), "-"),
                        param.getValue().realEndBalanceNominalProperty()));
        realEndBalanceNominal.setMinWidth(120);

        TableColumn<TblOpenCloseCashierBalance, String> endBalanceNominalTitle = new TableColumn("Kas Akhir");
        endBalanceNominalTitle.getColumns().addAll(systemEndBalanceNominal, realEndBalanceNominal);

        TableColumn<TblOpenCloseCashierBalance, String> openCloseStatus = new TableColumn("Status");
        openCloseStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalance, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefOpenCloseCashierBalanceStatus().getStatusName(),
                        param.getValue().refOpenCloseCashierBalanceStatusProperty()));
        openCloseStatus.setMinWidth(120);

        tableView.getColumns().addAll(cashier, dateTime, beginBalanceNominal, incomeNominal, expenseNominal, endBalanceNominalTitle, openCloseStatus);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataOpenCloseCashierBalance()));

        tableView.setRowFactory(tv -> {
            TableRow<TblOpenCloseCashierBalance> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(activePseudoClass, newVal.getRefOpenCloseCashierBalanceStatus().getIdstatus() == 1);   //Aktif = '1'
                } else {
                    row.pseudoClassStateChanged(activePseudoClass, false);
                }
            });
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPaymentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataPaymentShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataPaymentShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataOpenCloseCashierBalance = new ClassTableWithControl(tableView);
        
        //set filter
        cft = new ClassFilteringTable<>(
                TblOpenCloseCashierBalance.class,
                tableDataOpenCloseCashierBalance.getTableView(),
                tableDataOpenCloseCashierBalance.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public BigDecimal calculationTotalIncome(TblOpenCloseCashierBalance dataOCCB) {
        BigDecimal result = new BigDecimal("0");
        List<TblOpenCloseCashierBalanceDetail> occbDetails = fOpenCloseCashierBalanceManager.getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalance(dataOCCB.getIdopenCloseCashierBalance());
        for (TblOpenCloseCashierBalanceDetail occbDetail : occbDetails) {
            //data reservation payment
            if (occbDetail.getTblReservationPayment() != null) {
                occbDetail.setTblReservationPayment(fOpenCloseCashierBalanceManager.getDataReservationPayment(occbDetail.getTblReservationPayment().getIdpayment()));
                occbDetail.getTblReservationPayment().setRefFinanceTransactionPaymentType(fOpenCloseCashierBalanceManager.getDataFinanceTransactionPaymentType(occbDetail.getTblReservationPayment().getRefFinanceTransactionPaymentType().getIdtype()));
                if (occbDetail.getTblReservationPayment().getRefFinanceTransactionPaymentType().getIdtype() == 0) {   //Tunai = '0'
                    result = result.add(occbDetail.getTblReservationPayment().getUnitNominal());
                }
            }
            //data company balnce (cashier) - cash flow
            if (occbDetail.getLogCompanyBalanceCashFlow() != null) {
                occbDetail.setLogCompanyBalanceCashFlow(fOpenCloseCashierBalanceManager.getDataLogCompanyBalanceCashFlow(occbDetail.getLogCompanyBalanceCashFlow().getIdhistory()));
                if (occbDetail.getLogCompanyBalanceCashFlow().getTblCompanyBalanceByIdreceiverCompanyBalance() != null
                        && occbDetail.getLogCompanyBalanceCashFlow().getTblCompanyBalanceByIdreceiverCompanyBalance().getIdbalance() == 3) { //Kas Kasir = '3'
                    result = result.add(occbDetail.getLogCompanyBalanceCashFlow().getTransferNominal());
                }
            }
        }
        return result;
    }

    public BigDecimal calculationTotalExpense(TblOpenCloseCashierBalance dataOCCB) {
        BigDecimal result = new BigDecimal("0");
        List<TblOpenCloseCashierBalanceDetail> occbDetails = fOpenCloseCashierBalanceManager.getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalance(dataOCCB.getIdopenCloseCashierBalance());
        for (TblOpenCloseCashierBalanceDetail occbDetail : occbDetails) {
            //data reservation payment
            if (occbDetail.getTblReservationPayment() != null) {
                occbDetail.setTblReservationPayment(fOpenCloseCashierBalanceManager.getDataReservationPayment(occbDetail.getTblReservationPayment().getIdpayment()));
                occbDetail.getTblReservationPayment().setRefFinanceTransactionPaymentType(fOpenCloseCashierBalanceManager.getDataFinanceTransactionPaymentType(occbDetail.getTblReservationPayment().getRefFinanceTransactionPaymentType().getIdtype()));
                if (occbDetail.getTblReservationPayment().getRefFinanceTransactionPaymentType().getIdtype() == 16) {  //Canceling Fee = '16'
                    result = result.add(occbDetail.getTblReservationPayment().getUnitNominal());
                }
            }
            //data hotel expense transaction
            if (occbDetail.getTblHotelExpenseTransaction() != null) {
                occbDetail.setTblHotelExpenseTransaction(fOpenCloseCashierBalanceManager.getDataHotelExpenseTransaction(occbDetail.getTblHotelExpenseTransaction().getIdexpenseTransaction()));
                occbDetail.getTblHotelExpenseTransaction().setTblCompanyBalance(fOpenCloseCashierBalanceManager.getDataCompanyBalance(occbDetail.getTblHotelExpenseTransaction().getTblCompanyBalance().getIdbalance()));
                if (occbDetail.getTblHotelExpenseTransaction().getTblCompanyBalance().getIdbalance() == 3) {   //Kas Kasir = '3'
                    List<TblHotelExpenseTransactionDetail> hetDetails = fOpenCloseCashierBalanceManager.getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(occbDetail.getTblHotelExpenseTransaction().getIdexpenseTransaction());
                    for (TblHotelExpenseTransactionDetail hetDetail : hetDetails) {
                        result = result.add((hetDetail.getItemCost().multiply(hetDetail.getItemQuantity())));
                    }
                }
            }
            //data company balnce (cashier) - cash flow
            if (occbDetail.getLogCompanyBalanceCashFlow() != null) {
                occbDetail.setLogCompanyBalanceCashFlow(fOpenCloseCashierBalanceManager.getDataLogCompanyBalanceCashFlow(occbDetail.getLogCompanyBalanceCashFlow().getIdhistory()));
                if (occbDetail.getLogCompanyBalanceCashFlow().getTblCompanyBalanceByIdsenderCompanyBalance() != null
                        && occbDetail.getLogCompanyBalanceCashFlow().getTblCompanyBalanceByIdsenderCompanyBalance().getIdbalance() == 3) { //Kas Kasir = '3'
                    result = result.add(occbDetail.getLogCompanyBalanceCashFlow().getTransferNominal());
                }
            }
        }
        return result;
    }

    private void setTableControlDataOpenCloseCashierBalance() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buka");
            buttonControl.setOnMouseClicked((e) -> {
                //listener open
                dataOpenCloseCashierBalanceCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tutup");
            buttonControl.setOnMouseClicked((e) -> {
                //listener close
                dataOpenCloseCashierBalanceUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataOpenCloseCashierBalance.addButtonControl(buttonControls);
    }

    private List<TblOpenCloseCashierBalance> loadAllDataOpenCloseCashierBalance() {
        List<TblOpenCloseCashierBalance> list = fOpenCloseCashierBalanceManager.getAllDataOpenCloseCashierBalance();
        for (TblOpenCloseCashierBalance data : list) {
            //data employee
            data.setTblEmployeeByIdcashier(fOpenCloseCashierBalanceManager.getDataEmployee(data.getTblEmployeeByIdcashier().getIdemployee()));
            //data people
            data.getTblEmployeeByIdcashier().setTblPeople(fOpenCloseCashierBalanceManager.getDataPeople(data.getTblEmployeeByIdcashier().getTblPeople().getIdpeople()));
            //data open close cashier balance - status
            data.setRefOpenCloseCashierBalanceStatus(fOpenCloseCashierBalanceManager.getDataOpenCloseCashierBalanceStatus(data.getRefOpenCloseCashierBalanceStatus().getIdstatus()));
        }
        return list;
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataOpenCloseCashierBalance;

    @FXML
    private Label lblCodeData;
    
    @FXML
    private JFXTextField txtCashierName;

    @FXML
    private JFXTextField txtBeginDateTime;

    @FXML
    private JFXTextField txtEndDateTime;

    @FXML
    private JFXTextField txtBeginBalanceNominal;

    @FXML
    private JFXTextField txtIncome;

    @FXML
    private JFXTextField txtExpense;

    @FXML
    private JFXTextField txtSystemEndBalanceNominal;
    
    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXTextField txtRealEndBalanceNominal;

    @FXML
    private ScrollPane spFormDataOpenCloseCashierBalance;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblOpenCloseCashierBalance selectedData;

    private void initFormDataOpenCloseCashierBalance() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataOpenCloseCashierBalance.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataOpenCloseCashierBalance.setOnScroll((ScrollEvent scroll) -> {
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

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            refreshData();
        });
    }

    /**
     * TABLE DATA PURCHASE ORDER PAYMENT
     */
    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getRefOpenCloseCashierBalanceStatus() != null 
                ? selectedData.getRefOpenCloseCashierBalanceStatus().getStatusName() : "");
        
        txtCashierName.setText(selectedData.getTblEmployeeByIdcashier().getCodeEmployee()
                + " - "
                + selectedData.getTblEmployeeByIdcashier().getTblPeople().getFullName());

        if (selectedData.getOpenDateTime() != null) {
            txtBeginDateTime.setText(ClassFormatter.dateTimeFormate.format(selectedData.getOpenDateTime()));
        } else {
            txtBeginDateTime.setText("-");
        }
        if (selectedData.getCloseDateTime() != null) {
            txtEndDateTime.setText(ClassFormatter.dateTimeFormate.format(selectedData.getCloseDateTime()));
        } else {
            txtEndDateTime.setText("-");
        }

        txtBeginBalanceNominal.setText(ClassFormatter.currencyFormat.cFormat(selectedData.getBeginBalanceNominal()));
        if (selectedData.getRefOpenCloseCashierBalanceStatus().getIdstatus() == 0) {  //Tidak Aktif = '0'
            txtSystemEndBalanceNominal.setPromptText("Kas Akhir (Sistem)");
            txtSystemEndBalanceNominal.setText(ClassFormatter.currencyFormat.cFormat(selectedData.getSystemEndBalanceNominal()));
            txtRealEndBalanceNominal.setText(ClassFormatter.currencyFormat.cFormat(selectedData.getRealEndBalanceNominal()));
            txtRealEndBalanceNominal.setVisible(true);
        } else {
            txtSystemEndBalanceNominal.setPromptText("Kas Sementara (Sistem)");
            txtSystemEndBalanceNominal.setText(ClassFormatter.currencyFormat.cFormat(getCurrentCashierBalanceNominal()));
            txtRealEndBalanceNominal.setText("");
            txtRealEndBalanceNominal.setVisible(false);
        }

        txtIncome.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalIncome(selectedData)));
        txtExpense.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalExpense(selectedData)));

        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        gpFormDataOpenCloseCashierBalance.setDisable(dataInputStatus == 3);
    }

    @FXML
    private AnchorPane ancOpenCloseCashierBalanceDetailLayout;

    public TableView<TblOpenCloseCashierBalanceDetail> tableDataDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set table to content-view
        ancOpenCloseCashierBalanceDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancOpenCloseCashierBalanceDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail() {
        tableDataDetail = new TableView();

        TableColumn<TblOpenCloseCashierBalanceDetail, String> detailData = new TableColumn("Detail Data");
        detailData.setCellValueFactory((TableColumn.CellDataFeatures<TblOpenCloseCashierBalanceDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDetailData(),
                        param.getValue().detailDataProperty()));
        detailData.setMinWidth(620);

        tableDataDetail.getColumns().addAll(detailData);
        tableDataDetail.setItems(FXCollections.observableArrayList(loadAllDataOpenCloseCashierBalanceDetail()));
    }

    private List<TblOpenCloseCashierBalanceDetail> loadAllDataOpenCloseCashierBalanceDetail() {
        List<TblOpenCloseCashierBalanceDetail> list = fOpenCloseCashierBalanceManager.getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalance(selectedData.getIdopenCloseCashierBalance());
        for (TblOpenCloseCashierBalanceDetail data : list) {
            //data open close cashier balance
            data.setTblOpenCloseCashierBalance(fOpenCloseCashierBalanceManager.getDataOpenCloseCashierBalance(data.getTblOpenCloseCashierBalance().getIdopenCloseCashierBalance()));
            //data reservation payment
            if (data.getTblReservationPayment() != null) {
                data.setTblReservationPayment(fOpenCloseCashierBalanceManager.getDataReservationPayment(data.getTblReservationPayment().getIdpayment()));
            }
            //data hotel expense transaction
            if (data.getTblHotelExpenseTransaction() != null) {
                data.setTblHotelExpenseTransaction(fOpenCloseCashierBalanceManager.getDataHotelExpenseTransaction(data.getTblHotelExpenseTransaction().getIdexpenseTransaction()));
            }
            //data log company balance cash flow
            if (data.getLogCompanyBalanceCashFlow() != null) {
                data.setLogCompanyBalanceCashFlow(fOpenCloseCashierBalanceManager.getDataLogCompanyBalanceCashFlow(data.getLogCompanyBalanceCashFlow().getIdhistory()));
            }
        }
        return list;
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private int dataInputStatus = 0;

    public Stage dialogStage;

    private void dataOpenCloseCashierBalanceCreateHandle() {
        if (checkAvailableToOpen()) {
            selectedData = new TblOpenCloseCashierBalance();
            selectedData.setBeginBalanceNominal(getCurrentCashierBalanceNominal());
            selectedData.setSystemEndBalanceNominal(new BigDecimal("0"));
            selectedData.setRealEndBalanceNominal(new BigDecimal("0"));
            //open form data - detail (open)
            showDataOpenDetailDialog();
        } else {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Masih terdapat kas kasir yang masih aktif", "Tutup kas kasir terlebih dahulu?", null);
            if (alert.getResult() == ButtonType.OK) {
                selectedData = getDataOpenCloseCashierBalanceIsActive();
                //open form data - detail (close)
                showDataCloseDetailDialog();
            }
//            ClassMessage.showWarningInputDataMessage("Masih terdapat kas kasir yang masih aktif..!!", null);
        }
    }

    //another data must not 'active'
    private boolean checkAvailableToOpen() {
        return getDataOpenCloseCashierBalanceIsActive() == null;
    }

    private BigDecimal getCurrentCashierBalanceNominal() {
        BigDecimal result = new BigDecimal("0");
        TblCompanyBalance dataCashier = fOpenCloseCashierBalanceManager.getDataCompanyBalance((long) 3);    //Kas Kasir = '3'
        if (dataCashier != null) {
            result = dataCashier.getBalanceNominal();
        }
        return result;
    }

    private TblOpenCloseCashierBalance getDataOpenCloseCashierBalanceIsActive() {
        TblOpenCloseCashierBalance data = fOpenCloseCashierBalanceManager.getDataOpenCloseCashierBalanceByIDOpenCloseStatus(1);   //'Aktif'
        if (data != null) {
            //data employee
            data.setTblEmployeeByIdcashier(fOpenCloseCashierBalanceManager.getDataEmployee(data.getTblEmployeeByIdcashier().getIdemployee()));
            //data people
            data.getTblEmployeeByIdcashier().setTblPeople(fOpenCloseCashierBalanceManager.getDataPeople(data.getTblEmployeeByIdcashier().getTblPeople().getIdpeople()));
            //data open close cashier balance - status
            data.setRefOpenCloseCashierBalanceStatus(fOpenCloseCashierBalanceManager.getDataOpenCloseCashierBalanceStatus(data.getRefOpenCloseCashierBalanceStatus().getIdstatus()));
        }
        return data;
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPaymentShowHandle() {
        if (tableDataOpenCloseCashierBalance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = (TblOpenCloseCashierBalance) tableDataOpenCloseCashierBalance.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm();
            dataOpenCloseCashierBalanceFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPaymentUnshowHandle() {
        refreshDataTableOpenCloseCashierBalance();
        dataOpenCloseCashierBalanceFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataOpenCloseCashierBalanceUpdateHandle() {
        if (tableDataOpenCloseCashierBalance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkAvailableToClose()) {
                selectedData = fOpenCloseCashierBalanceManager.getDataOpenCloseCashierBalance(((TblOpenCloseCashierBalance) tableDataOpenCloseCashierBalance.getTableView().getSelectionModel().getSelectedItem()).getIdopenCloseCashierBalance());
                selectedData.setSystemEndBalanceNominal(getCurrentCashierBalanceNominal());
                selectedData.setRealEndBalanceNominal(getCurrentCashierBalanceNominal());
                //open form data - detail (close)
                showDataCloseDetailDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Data Kasir telah tidak aktif..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkAvailableToClose() {
        return ((TblOpenCloseCashierBalance) tableDataOpenCloseCashierBalance.getTableView().getSelectionModel().getSelectedItem()).getRefOpenCloseCashierBalanceStatus().getIdstatus() == 1;   //Aktif = '1'
    }

    private void showDataOpenDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_open_close_cashier_balance/OpenCloseCashierBalanceDetailOpenDialog.fxml"));

            OpenCloseCashierBalanceDetailOpenController controller = new OpenCloseCashierBalanceDetailOpenController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    private void showDataCloseDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_open_close_cashier_balance/OpenCloseCashierBalanceDetailCloseDialog.fxml"));

            OpenCloseCashierBalanceDetailCloseController controller = new OpenCloseCashierBalanceDetailCloseController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    public void refreshData() {
        //data table
        refreshDataTableOpenCloseCashierBalance();
        //close form table detail
        dataOpenCloseCashierBalanceFormShowStatus.set(0.0);
        isShowStatus.set(false);
    }
    
    public void refreshDataTableOpenCloseCashierBalance(){
        tableDataOpenCloseCashierBalance.getTableView().setItems(FXCollections.observableArrayList(loadAllDataOpenCloseCashierBalance()));
        cft.refreshFilterItems(tableDataOpenCloseCashierBalance.getTableView().getItems());
    }
    
    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FOpenCloseCashierBalanceManager fOpenCloseCashierBalanceManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fOpenCloseCashierBalanceManager = new FOpenCloseCashierBalanceManagerImpl();

        //set splitpane
        setDataOpenCloseCashierBalanceSplitpane();

        //init table
        initTableDataOpenCloseCashierBalance();

        //init form
        initFormDataOpenCloseCashierBalance();

        spDataOpenCloseCashierBalance.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataOpenCloseCashierBalanceFormShowStatus.set(0.0);
        });
    }

    public FOpenCloseCashierBalanceManager getFOpenCloseCashierBalanceManager() {
        return this.fOpenCloseCashierBalanceManager;
    }

}
