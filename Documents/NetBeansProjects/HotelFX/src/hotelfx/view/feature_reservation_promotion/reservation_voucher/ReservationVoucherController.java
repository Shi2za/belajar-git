/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_promotion.reservation_voucher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefVoucherStatus;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_reservation_promotion.FeatureReservationPromotionController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationVoucherController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataVoucher;

    private DoubleProperty dataVoucherFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataVoucherLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataVoucherSplitpane() {
        spDataVoucher.setDividerPositions(1);

        dataVoucherFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataVoucherFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataVoucher.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataVoucher.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataVoucherFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataVoucherLayout.setDisable(false);
                    tableDataVoucherLayoutDisableLayer.setDisable(true);
                    tableDataVoucherLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataVoucherLayout.setDisable(true);
                    tableDataVoucherLayoutDisableLayer.setDisable(false);
                    tableDataVoucherLayoutDisableLayer.toFront();
                }
            }
        });

        dataVoucherFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataVoucherLayout;

    private ClassFilteringTable<TblReservationVoucher> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataVoucher;

    private void initTableDataVoucher() {
        //set table
        setTableDataVoucher();
        //set control
        setTableControlDataVoucher();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataVoucher, 15.0);
        AnchorPane.setLeftAnchor(tableDataVoucher, 15.0);
        AnchorPane.setRightAnchor(tableDataVoucher, 15.0);
        AnchorPane.setTopAnchor(tableDataVoucher, 15.0);
        ancBodyLayout.getChildren().add(tableDataVoucher);
    }

    private void setTableDataVoucher() {
        TableView<TblReservationVoucher> tableView = new TableView();

        TableColumn<TblReservationVoucher, String> codeVoucher = new TableColumn("Code");
        codeVoucher.setCellValueFactory(cellData -> cellData.getValue().codeVoucherProperty());
        codeVoucher.setMinWidth(120);

        TableColumn<TblReservationVoucher, String> voucherName = new TableColumn("Voucher");
        voucherName.setCellValueFactory(cellData -> cellData.getValue().voucherNameProperty());
        voucherName.setMinWidth(140);

        TableColumn<TblReservationVoucher, String> voucherNominal = new TableColumn("Nominal Voucher");
        voucherNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationVoucher, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getNominal()), param.getValue().nominalProperty()));
        voucherNominal.setMinWidth(140);

        TableColumn<TblReservationVoucher, String> voucherMinTransaction = new TableColumn("Minimal Transaksi");
        voucherMinTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationVoucher, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getMinimumPayment()), param.getValue().minimumPaymentProperty()));
        voucherMinTransaction.setMinWidth(180);

        TableColumn<TblReservationVoucher, String> voucherValidUtil = new TableColumn("Tanggal Kadaluarsa");
        voucherValidUtil.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationVoucher, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getValidUntil()), param.getValue().validUntilProperty()));
        voucherValidUtil.setMinWidth(180);

        TableColumn<TblReservationVoucher, String> voucherStatus = new TableColumn("Status");
        voucherStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationVoucher, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefVoucherStatus().getStatusName(), param.getValue().refVoucherStatusProperty()));
        voucherStatus.setMinWidth(140);

        tableView.getColumns().addAll(codeVoucher, voucherName, voucherNominal, voucherMinTransaction, voucherValidUtil, voucherStatus);
        tableView.setItems(loadAllDataVoucher());

        tableView.setRowFactory(tv -> {
            TableRow<TblReservationVoucher> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataVoucherUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataVoucherShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataVoucherShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataVoucher = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblReservationVoucher.class,
                tableDataVoucher.getTableView(),
                tableDataVoucher.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataVoucher() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataVoucherCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tidak Berlaku");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataVoucherUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataVoucherDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataVoucherPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataVoucher.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationVoucher> loadAllDataVoucher() {
        return FXCollections.observableArrayList(parentController.getFReservationPromotionManager().getAllDataVoucher());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataVoucher;

    @FXML
    private ScrollPane spFormDataVoucher;

    @FXML
    private JFXTextField txtVoucherName;

    @FXML
    private JFXTextField txtVoucherNominal;

    @FXML
    private JFXTextField txtVoucherMinTransaction;

    @FXML
    private JFXDatePicker dtpVoucherValidUtil;

    @FXML
    private JFXTextField txtVoucherNumber;

    @FXML
    private JFXTextArea txtVoucherNote;

    @FXML
    private Label lblVoucher;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private NewDataVoucher newDataVoucher;

    private TblReservationVoucher selectedData;

    private void initFormDataVoucher() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataVoucher.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataVoucher.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Voucher)"));
        btnSave.setOnAction((e) -> {
            dataVoucherSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataVoucherCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpVoucherValidUtil);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpVoucherValidUtil);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtVoucherName,
                txtVoucherNominal,
                dtpVoucherValidUtil,
                txtVoucherMinTransaction,
                txtVoucherNumber);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtVoucherNominal,
                txtVoucherMinTransaction);
        ClassFormatter.setToNumericField(
                "Integer",
                txtVoucherNumber);
    }

    private void setSelectedDataToInputForm() {
        txtVoucherName.textProperty().bindBidirectional(newDataVoucher.getDataVoucher().voucherNameProperty());

        Bindings.bindBidirectional(txtVoucherNominal.textProperty(), newDataVoucher.getDataVoucher().nominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtVoucherMinTransaction.textProperty(), newDataVoucher.getDataVoucher().minimumPaymentProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtVoucherNumber.textProperty(), newDataVoucher.voucherNumberProperty(), new NumberStringConverter());

        txtVoucherNote.textProperty().bindBidirectional(newDataVoucher.getDataVoucher().voucherNoteProperty());

        if (newDataVoucher.getDataVoucher().getValidUntil() != null) {
            dtpVoucherValidUtil.setValue(newDataVoucher.getDataVoucher().getValidUntil().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpVoucherValidUtil.setValue(null);
        }
        dtpVoucherValidUtil.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                newDataVoucher.getDataVoucher().setValidUntil(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

//        dtpVoucherValidUtil.setValue(LocalDate.now());
        setSelectedDataToInputFormFunctionalComponent();

    }

    class NewDataVoucher {

        private TblReservationVoucher dataVoucher;

        private final IntegerProperty voucherNumber = new SimpleIntegerProperty();

        public NewDataVoucher(TblReservationVoucher dataVoucher,
                int voucherNumber) {
            this.dataVoucher = dataVoucher;
            this.voucherNumber.set(voucherNumber);
        }

        public TblReservationVoucher getDataVoucher() {
            return dataVoucher;
        }

        public void setDataVoucher(TblReservationVoucher dataVoucher) {
            this.dataVoucher = dataVoucher;
        }

        public IntegerProperty voucherNumberProperty() {
            return voucherNumber;
        }

        public int getVoucherNumber() {
            return voucherNumberProperty().get();
        }

        public void setVoucherNumber(int voucherNumber) {
            voucherNumberProperty().set(voucherNumber);
        }

        public List<TblReservationVoucher> getDataArray() {
            List<TblReservationVoucher> list = new ArrayList<>();
            for (int i = 0; i < getVoucherNumber(); i++) {
                TblReservationVoucher data = new TblReservationVoucher();
                data.setCodeVoucher(getVoucherCode(dataVoucher.getValidUntil(), i));
                data.setVoucherName(dataVoucher.getVoucherName());
                data.setNominal(dataVoucher.getNominal());
                data.setMinimumPayment(dataVoucher.getMinimumPayment());
                data.setValidUntil(dataVoucher.getValidUntil());
                data.setVoucherNote(dataVoucher.getVoucherNote());
                list.add(data);
            }
            return list;
        }

        private String getVoucherCode(java.util.Date vaildUtil, int count) {
            String vcID = String.valueOf(count);
            return ("RVC"
                    + (new SimpleDateFormat("yyMMdd")).format(new java.util.Date())
                    + "-"
                    + (new SimpleDateFormat("yyMMdd")).format(vaildUtil)
                    + "-"
                    + ("0000" + vcID).substring(vcID.length()));
        }

    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataVoucher, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        // btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataVoucherCreateHandle() {
        dataInputStatus = 0;
        TblReservationVoucher dataReservationVoucher = new TblReservationVoucher();
        dataReservationVoucher.setNominal(new BigDecimal("0"));
        dataReservationVoucher.setMinimumPayment(new BigDecimal("0"));
        newDataVoucher = new NewDataVoucher(dataReservationVoucher, 0);
        lblVoucher.setText("");
        setSelectedDataToInputForm();
        //open form data voucher
        dataVoucherFormShowStatus.set(0.0);
        dataVoucherFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataVoucherUpdateHandle() {
        if (tableDataVoucher.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFReservationPromotionManager().getVoucher(((TblReservationVoucher) tableDataVoucher.getTableView().getSelectionModel().getSelectedItem()).getIdvoucher());
            selectedData.setRefVoucherStatus(parentController.getFReservationPromotionManager().getDataVoucherStatus(selectedData.getRefVoucherStatus().getIdstatus()));
            lblVoucher.setText(selectedData.getCodeVoucher() + " - " + selectedData.getVoucherName());
            dataVoucherSaveHandle();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataVoucherShowHandle() {
        if (tableDataVoucher.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            newDataVoucher = new NewDataVoucher(
                    parentController.getFReservationPromotionManager().getVoucher(((TblReservationVoucher) tableDataVoucher.getTableView().getSelectionModel().getSelectedItem()).getIdvoucher()),
                    1);
            selectedData = parentController.getFReservationPromotionManager().getVoucher(((TblReservationVoucher) tableDataVoucher.getTableView().getSelectionModel().getSelectedItem()).getIdvoucher());
            lblVoucher.setText(selectedData.getCodeVoucher() + " - " + selectedData.getVoucherName());
            setSelectedDataToInputForm();
            dataVoucherFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataVoucherUnshowHandle() {
        refreshDataTableVoucher();
        dataVoucherFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataVoucherDeleteHandle() {
        if (tableDataVoucher.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFReservationPromotionManager().deleteDataVoucher((TblReservationVoucher) tableDataVoucher.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data voucher
                    refreshDataTableVoucher();
                    dataVoucherFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataVoucherPrintHandle() {

    }

    private void dataVoucherSaveHandle() {
        if (checkDataInputDataVoucher()) {
            switch (dataInputStatus) {
                case 0:
                    Alert alert1 = ClassMessage.showConfirmationSavingDataMessage("", null);
                    if (alert1.getResult() == ButtonType.OK) {
                        List<TblReservationVoucher> dummyNewDataVouchers = new ArrayList<>();
                        for (TblReservationVoucher dataVoucher : newDataVoucher.getDataArray()) {
                            TblReservationVoucher dummyNewDataVoucher = new TblReservationVoucher(dataVoucher);
                            dummyNewDataVouchers.add(dummyNewDataVoucher);
                        }
                        if (parentController.getFReservationPromotionManager().insertDataVoucher(dummyNewDataVouchers)) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data voucher
                            refreshDataTableVoucher();
                            dataVoucherFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                        }
                    } else {
                        dataVoucherCancelHandle();
                    }
                    break;
                case 1:
                    Alert alert2 = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin mengubah status data voucher menjadi 'tidak berlaku'?", null);
                    if (alert2.getResult() == ButtonType.OK) {
                        //dummy entry
                        TblReservationVoucher dummySelectedData = new TblReservationVoucher(selectedData);
                        dummySelectedData.setRefVoucherStatus(new RefVoucherStatus(dummySelectedData.getRefVoucherStatus()));
                        if (parentController.getFReservationPromotionManager().updateDataVoucherToObsolete(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data voucher
                            refreshDataTableVoucher();
                            dataVoucherFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                        }
                    } else {
                        dataVoucherCancelHandle();
                    }
                    break;
                default:
                    break;
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataVoucherCancelHandle() {
        //refresh data from table & close form data voucher
        refreshDataTableVoucher();
        dataVoucherFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableVoucher() {
        tableDataVoucher.getTableView().setItems(loadAllDataVoucher());
        cft.refreshFilterItems(tableDataVoucher.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataVoucher() {
        boolean dataInput = true;
        errDataInput = "";
        if (dataInputStatus == 0) { //insert
            if (txtVoucherName.getText() == null || txtVoucherName.getText().equals("")) {
                dataInput = false;
                errDataInput += "Voucher : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
            if (txtVoucherNominal.getText() == null
                    || txtVoucherNominal.getText().equals("")
                    || txtVoucherNominal.getText().equals("-")) {
                dataInput = false;
                errDataInput += "Nominal Voucher : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
            if (dtpVoucherValidUtil.getValue() == null) {
                dataInput = false;
                errDataInput += "Tanggal Kadaluarsa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
            if (txtVoucherMinTransaction.getText() == null
                    || txtVoucherMinTransaction.getText().equals("")
                    || txtVoucherMinTransaction.getText().equals("-")) {
                dataInput = false;
                errDataInput += "Minimal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
            if (txtVoucherNumber.getText() == null
                    || txtVoucherNumber.getText().equals("")
                    || txtVoucherNumber.getText().equals("-")) {
                dataInput = false;
                errDataInput += "Jumlah Voucher : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (newDataVoucher.getVoucherNumber() <= 0) {
                    dataInput = false;
                    errDataInput += "Jumlah Voucher : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
                }
            }
        } else {
            if (dataInputStatus == 1) {   //update
                if (selectedData.getRefVoucherStatus().getIdstatus() == 2) {  //Obsolete = '2'
                    dataInput = false;
                    errDataInput += "Status Voucher : Telah tidak berlaku..!! \n";
                }
            }
        }
        return dataInput;
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
        setDataVoucherSplitpane();

        //init table
        initTableDataVoucher();

        //init form
        initFormDataVoucher();

        spDataVoucher.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataVoucherFormShowStatus.set(0.0);
        });
    }

    public ReservationVoucherController(FeatureReservationPromotionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReservationPromotionController parentController;

}
