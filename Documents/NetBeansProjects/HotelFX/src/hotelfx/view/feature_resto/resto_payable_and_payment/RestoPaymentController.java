/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_resto.resto_payable_and_payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.service.FRestoManager;
import hotelfx.view.DashboardController;
import java.net.URL;
import java.time.ZoneId;
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
 * @author ABC-Programmer
 */
public class RestoPaymentController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRestoPayment;

    private DoubleProperty dataRestoPaymentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRestoPaymentLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataRestoPaymentSplitpane() {
        spDataRestoPayment.setDividerPositions(1);

        dataRestoPaymentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRestoPaymentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRestoPayment.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRestoPayment.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRestoPaymentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataRestoPaymentLayout.setDisable(false);
                    tableDataRestoPaymentLayoutDisableLayer.setDisable(true);
                    tableDataRestoPaymentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataRestoPaymentLayout.setDisable(true);
                    tableDataRestoPaymentLayoutDisableLayer.setDisable(false);
                    tableDataRestoPaymentLayoutDisableLayer.toFront();
                }
            }
        });

        dataRestoPaymentFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataRestoPaymentLayout;

    private ClassFilteringTable<TblHotelFinanceTransaction> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRestoPayment;

    private void initTableDataRestoPayment() {
        //set table
        setTableDataRestoPayment();
        //set control
        setTableControlDataRestoPayment();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRestoPayment, 15.0);
        AnchorPane.setLeftAnchor(tableDataRestoPayment, 15.0);
        AnchorPane.setRightAnchor(tableDataRestoPayment, 15.0);
        AnchorPane.setTopAnchor(tableDataRestoPayment, 15.0);
        ancBodyLayout.getChildren().add(tableDataRestoPayment);
    }

    private void setTableDataRestoPayment() {
        TableView<TblHotelFinanceTransaction> tableView = new TableView();

        TableColumn<TblHotelFinanceTransaction, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory(cellData -> cellData.getValue().codeTransactionProperty());
        codePayment.setMinWidth(120);

        TableColumn<TblHotelFinanceTransaction, String> paymentType = new TableColumn("Tipe Pembayaran");
        paymentType.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType() != null
                                ? (param.getValue().getIsReturnTransaction() ? ("Return" + " - ") : "") + param.getValue().getRefFinanceTransactionPaymentType().getTypeName() : "-",
                        param.getValue().refFinanceTransactionPaymentTypeProperty()));
        paymentType.setMinWidth(160);

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

        tableView.getColumns().addAll(codePayment, paymentNominal, paymentRoundingValue, paymentType, paymentDate);
        tableView.setItems(loadAllDataHotelFinanceTransaction());

        tableView.setRowFactory(tv -> {
            TableRow<TblHotelFinanceTransaction> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataRestoPaymentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataRestoPaymentShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataRestoPaymentShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataRestoPayment = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblHotelFinanceTransaction.class,
                tableDataRestoPayment.getTableView(),
                tableDataRestoPayment.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataRestoPayment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataRestoPaymentPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataRestoPayment.addButtonControl(buttonControls);
    }

    private ObservableList<TblHotelFinanceTransaction> loadAllDataHotelFinanceTransaction() {
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
        List<TblReservationAdditionalService> rass = parentController.getService().getAllDataReservationAdditionalService();
        for (TblReservationAdditionalService ras : rass) {
            if (ras.getTblRoomService().getIdroomService() == 6     //Room Dining = '6'
                    || ras.getTblRoomService().getIdroomService() == 7) {   //Dine in Resto = '7'
                if (ras.getTblHotelPayable() != null) {
                    List<TblHotelFinanceTransactionHotelPayable> hfthpList = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(ras.getTblHotelPayable().getIdhotelPayable());
                    for (TblHotelFinanceTransactionHotelPayable hfthpData : hfthpList) {
                        boolean found = false;
                        TblHotelFinanceTransaction hftData = parentController.getService().getDataHotelFinanceTransaction(hfthpData.getTblHotelFinanceTransaction().getIdtransaction());
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
    private GridPane gpFormDataRestoPayment;

    @FXML
    private ScrollPane spFormDataRestoPayment;

    @FXML
    private JFXTextField txtCodeHotelFinanceTransaction;

    @FXML
    private JFXDatePicker dtpHotelFinanceTransactionDate;

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

    private void initFormDataRestoPayment() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRestoPayment.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRestoPayment.setOnScroll((ScrollEvent scroll) -> {
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
            dataRestoPaymentBackHandle();
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

        txtPaymentType.setText(selectedData.getRefFinanceTransactionPaymentType() != null
                ? (selectedData.getIsReturnTransaction() ? ("Return - ") : "") + selectedData.getRefFinanceTransactionPaymentType().getTypeName() : "-");
        txtRoundingValue.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionRoundingValue()));

        lblHotelTransactionNominal.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionNominal()));

        //init data detail
        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeHotelFinanceTransaction.setDisable(true);
        dtpHotelFinanceTransactionDate.setDisable(true);
        txtPaymentType.setDisable(true);
        txtRoundingValue.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataRestoPayment, dataInputStatus == 3,
                txtCodeHotelFinanceTransaction,
                dtpHotelFinanceTransactionDate,
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

    private void dataRestoPaymentShowHandle() {
        if (tableDataRestoPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getService().getDataHotelFinanceTransaction(((TblHotelFinanceTransaction) tableDataRestoPayment.getTableView().getSelectionModel().getSelectedItem()).getIdtransaction());
            setSelectedDataToInputForm();
            dataRestoPaymentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataRestoPaymentUnshowHandle() {
        refreshDataTableRestoPayment();
        dataRestoPaymentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataRestoPaymentPrintHandle() {

    }

    private void dataRestoPaymentBackHandle() {
        //refresh data from table & close form data detail
        refreshDataTableRestoPayment();
        dataRestoPaymentFormShowStatus.set(0.0);
        isShowStatus.set(false);
    }

    public void refreshDataTableRestoPayment() {
        tableDataRestoPayment.getTableView().setItems(loadAllDataHotelFinanceTransaction());
        cft.refreshFilterItems(tableDataRestoPayment.getTableView().getItems());
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
        TableView<TblHotelFinanceTransactionHotelPayable> tableView = new TableView();

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codeRestoBill = new TableColumn("No. Tagihan (Resto)");
        codeRestoBill.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getCodeRestoBill(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeRestoBill.setMinWidth(140);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeInvoice.setMinWidth(120);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> nominalTransaction = new TableColumn("Nominal");
        nominalTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getNominalTransaction()),
                        param.getValue().nominalTransactionProperty()));
        nominalTransaction.setMinWidth(150);

        tableView.getColumns().addAll(codeRestoBill, codeInvoice, nominalTransaction);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataRestoPaymentDetail()));
        tableDataDetail = tableView;
    }

    private String getCodeRestoBill(TblHotelPayable hotelPayable) {
        if (hotelPayable != null) {
            TblReservationAdditionalService dataReservationAdditionalService = parentController.getService().getDataReservationAdditionalServiceByIDHotelPayable(hotelPayable.getIdhotelPayable());
            if (dataReservationAdditionalService != null) {
                return dataReservationAdditionalService.getRestoTransactionNumber();
            }
        }
        return "-";
    }

    private String getCodeInvoice(TblHotelPayable hotelPayable) {
        if (hotelPayable != null
                && hotelPayable.getTblHotelInvoice() != null) {
            return hotelPayable.getTblHotelInvoice().getCodeHotelInvoice();
        }
        return "-";
    }

    private List<TblHotelFinanceTransactionHotelPayable> loadAllDataRestoPaymentDetail() {
        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
        if (selectedData != null
                && selectedData.getIdtransaction() != 0L) {
            list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction(selectedData.getIdtransaction());
            for (TblHotelFinanceTransactionHotelPayable data : list) {
                //data hotel payable
                data.setTblHotelPayable(parentController.getService().getDataHotelPayable(data.getTblHotelPayable().getIdhotelPayable()));
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
        setDataRestoPaymentSplitpane();

        //init table
        initTableDataRestoPayment();

        //init form
        initFormDataRestoPayment();

        spDataRestoPayment.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRestoPaymentFormShowStatus.set(0.0);
        });
    }

    public RestoPaymentController(RestoPayableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final RestoPayableAndPaymentController parentController;

    public FRestoManager getService() {
        return parentController.getService();
    }

}
