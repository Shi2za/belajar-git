/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_promotion.hotel_event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_reservation_promotion.FeatureReservationPromotionController;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class HotelEventController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataHotelEvent;

    private DoubleProperty dataHotelEventFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataHotelEventLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataHotelEventSplitpane() {
        spDataHotelEvent.setDividerPositions(1);

        dataHotelEventFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataHotelEventFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataHotelEvent.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataHotelEvent.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataHotelEventFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataHotelEventLayout.setDisable(false);
                    tableDataHotelEventLayoutDisableLayer.setDisable(true);
                    tableDataHotelEventLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataHotelEventLayout.setDisable(true);
                    tableDataHotelEventLayoutDisableLayer.setDisable(false);
                    tableDataHotelEventLayoutDisableLayer.toFront();
                }
            }
        });

        dataHotelEventFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataHotelEventLayout;

    private ClassFilteringTable<TblHotelEvent> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataHotelEvent;

    private void initTableDataHotelEvent() {
        //set table
        setTableDataHotelEvent();
        //set control
        setTableControlDataHotelEvent();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataHotelEvent, 15.0);
        AnchorPane.setLeftAnchor(tableDataHotelEvent, 15.0);
        AnchorPane.setRightAnchor(tableDataHotelEvent, 15.0);
        AnchorPane.setTopAnchor(tableDataHotelEvent, 15.0);
        ancBodyLayout.getChildren().add(tableDataHotelEvent);
    }

    private void setTableDataHotelEvent() {
        TableView<TblHotelEvent> tableView = new TableView();

        TableColumn<TblHotelEvent, String> codeHotelEvent = new TableColumn("ID");
        codeHotelEvent.setCellValueFactory(cellData -> cellData.getValue().codeEventProperty());
        codeHotelEvent.setMinWidth(100);

        TableColumn<TblHotelEvent, String> hotelEventName = new TableColumn("Event");
        hotelEventName.setCellValueFactory(cellData -> cellData.getValue().eventNameProperty());
        hotelEventName.setMinWidth(140);

        TableColumn<TblHotelEvent, String> dayOfWeekName = new TableColumn("Hari");
        dayOfWeekName.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> getDayNameInIndonesian(param.getValue().getDayOfWeek()), param.getValue().dayOfWeekProperty()));
        dayOfWeekName.setMinWidth(120);

        TableColumn<TblHotelEvent, String> beginEventDate = new TableColumn("Awal");
        beginEventDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getBeginEventDate()), param.getValue().beginEventDateProperty()));
        beginEventDate.setMinWidth(100);

        TableColumn<TblHotelEvent, String> endEventDate = new TableColumn("Akhir");
        endEventDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getEndEventDate()), param.getValue().endEventDateProperty()));
        endEventDate.setMinWidth(100);

        TableColumn<TblHotelEvent, String> eventDateTitle = new TableColumn("Tanggal Event");
        eventDateTitle.getColumns().addAll(beginEventDate, endEventDate);

        TableColumn<TblHotelEvent, String> minTransaction = new TableColumn("Minimal Transaksi");
        minTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getMinTransaction()), param.getValue().minTransactionProperty()));
        minTransaction.setMinWidth(180);

        TableColumn<TblHotelEvent, String> roomDiscountPercentage = new TableColumn("Kamar");
        roomDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getRoomDiscountPercentage()) + "%", param.getValue().roomDiscountPercentageProperty()));
        roomDiscountPercentage.setMinWidth(80);

        TableColumn<TblHotelEvent, String> itemDiscountPercentage = new TableColumn("Barang");
        itemDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemDiscountPercentage()) + "%", param.getValue().itemDiscountPercentageProperty()));
        itemDiscountPercentage.setMinWidth(80);

        TableColumn<TblHotelEvent, String> serviceDiscountPercentage = new TableColumn("Layanan");
        serviceDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getServiceDiscountPercentage()) + "%", param.getValue().serviceDiscountPercentageProperty()));
        serviceDiscountPercentage.setMinWidth(80);

        TableColumn<TblHotelEvent, String> discountPercentageTitle = new TableColumn("Diskon (%)");
        discountPercentageTitle.getColumns().addAll(roomDiscountPercentage, itemDiscountPercentage, serviceDiscountPercentage);

        TableColumn<TblHotelEvent, String> maxDiscountNominal = new TableColumn("Maksimal Nominal Diskon");
        maxDiscountNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDiscountNominal()), param.getValue().discountNominalProperty()));
        maxDiscountNominal.setMinWidth(180);

        TableColumn<TblHotelEvent, String> eventNote = new TableColumn("Keterangan");
        eventNote.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
                -> Bindings.createStringBinding(()
                        -> "Min. Transaksi : "
                        + ClassFormatter.currencyFormat.cFormat(param.getValue().getMinTransaction()) + "\n"
                        + "Max. Nominal Diskon : "
                        + ClassFormatter.currencyFormat.cFormat(param.getValue().getDiscountNominal()),
                        param.getValue().ideventProperty()));
        eventNote.setMinWidth(300);

        tableView.getColumns().addAll(codeHotelEvent, hotelEventName, dayOfWeekName,
                eventDateTitle, discountPercentageTitle, eventNote);
        tableView.setItems(loadAllDataHotelEvent());

        tableView.setRowFactory(tv -> {
            TableRow<TblHotelEvent> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataHotelEventUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataHotelEventUpdateHandle();
                            } else {
                                dataHotelEventShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataHotelEventUpdateHandle();
//                            } else {
//                                dataHotelEventShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataHotelEvent = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblHotelEvent.class,
                tableDataHotelEvent.getTableView(),
                tableDataHotelEvent.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getDayNameInIndonesian(int dayOfWeek) {
        String dayNameInIndonesian = ClassFormatter.convertDayToIndonesian(dayOfWeek);
        return !dayNameInIndonesian.equals("") ? dayNameInIndonesian : "-";
    }

    private void setTableControlDataHotelEvent() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataHotelEventCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataHotelEventUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataHotelEventDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataHotelEventPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataHotelEvent.addButtonControl(buttonControls);
    }

    private ObservableList<TblHotelEvent> loadAllDataHotelEvent() {
        return FXCollections.observableArrayList(parentController.getFReservationPromotionManager().getAllDataHotelEvent());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataHotelEvent;

    @FXML
    private ScrollPane spFormDataHotelEvent;

    @FXML
    private JFXTextField txtCodeEvent;

    @FXML
    private JFXTextField txtEventName;

    @FXML
    private JFXDatePicker dtpBeginEventDate;

    @FXML
    private JFXDatePicker dtpEndEventDate;

    @FXML
    private JFXTextField txtMinTransaction;

//    @FXML
//    private JFXTextField txtDiscountPercentage;
    @FXML
    private JFXTextField txtRoomDiscountPercentage;

    @FXML
    private JFXTextField txtItemDiscountPercentage;

    @FXML
    private JFXTextField txtServiceDiscountPercentage;

    @FXML
    private JFXTextField txtMaxDiscountNominal;

    @FXML
    private JFXTextArea txtNote;

    @FXML
    private Label lblHotelEvent;

    @FXML
    private AnchorPane dayNameLayout;
    private JFXCComboBoxTablePopup<DayName> cbpDayName;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblHotelEvent selectedData;

    private void initFormDataHotelEvent() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataHotelEvent.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataHotelEvent.setOnScroll((ScrollEvent scroll) -> {
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

        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Hotel-Event)"));
        btnSave.setOnAction((e) -> {
            dataHotelEventSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataHotelEventCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpBeginEventDate,
                dtpEndEventDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpBeginEventDate,
                dtpEndEventDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtEventName,
                cbpDayName,
                dtpBeginEventDate,
                dtpEndEventDate,
                txtMinTransaction,
                txtRoomDiscountPercentage,
                txtItemDiscountPercentage,
                txtServiceDiscountPercentage,
                txtMaxDiscountNominal);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtMinTransaction,
                //                txtDiscountPercentage,
                txtRoomDiscountPercentage,
                txtItemDiscountPercentage,
                txtServiceDiscountPercentage,
                txtMaxDiscountNominal);
    }

    private void initDataPopup() {
        //Day Name
        TableView<DayName> tableDayName = new TableView<>();
        TableColumn<DayName, String> dayNameInIndonesian = new TableColumn<>("Hari");
        dayNameInIndonesian.setCellValueFactory(cellData -> cellData.getValue().dayNameInIndonesianProperty());
        tableDayName.getColumns().addAll(dayNameInIndonesian);

        ObservableList<DayName> dayNameItems = FXCollections.observableArrayList(loadAllDataDayName());

        cbpDayName = new JFXCComboBoxTablePopup<>(
                DayName.class, tableDayName, dayNameItems, "", "Hari *", true, 200, 300
        );

        //attached to grid-pane
        //gpFormDataHotelEvent.add(cbpDayName, 2, 3);
        AnchorPane.setTopAnchor(cbpDayName, 0.0);
        AnchorPane.setBottomAnchor(cbpDayName, 0.0);
        AnchorPane.setLeftAnchor(cbpDayName, 0.0);
        AnchorPane.setRightAnchor(cbpDayName, 0.0);
        dayNameLayout.getChildren().clear();
        dayNameLayout.getChildren().add(cbpDayName);
    }

    private void refreshDataPopup() {
        //Day Name
        ObservableList<DayName> dayNameItems = FXCollections.observableArrayList(loadAllDataDayName());
        cbpDayName.setItems(dayNameItems);
    }

    private List<DayName> loadAllDataDayName() {
        List<DayName> list = new ArrayList<>();
        //none (-, 0)
        list.add(new DayName("-", 0));
        //Monday (Senin, 1)
        list.add(new DayName("Senin", 1));
        //Tuesday (Selasa, 2)
        list.add(new DayName("Selasa", 2));
        //Wednesday (Rabu, 3)
        list.add(new DayName("Rabu", 3));
        //Thursday (Kamis, 4)
        list.add(new DayName("Kamis", 4));
        //Friday (Jumat, 5)
        list.add(new DayName("Jumat", 5));
        //Saturday (Sabtu, 6)
        list.add(new DayName("Sabtu", 6));
        //Sunday (Minggu, 7)
        list.add(new DayName("Minggu", 7));
        return list;
    }

    public class DayName {

        private final StringProperty dayNameInIndonesian = new SimpleStringProperty();
        private final IntegerProperty dayOfWeekValue = new SimpleIntegerProperty();

        public DayName(String dayNameInIndonesian, int dayOfWekkValue) {
            this.dayNameInIndonesian.set(dayNameInIndonesian);
            this.dayOfWeekValue.set(dayOfWekkValue);
        }

        public StringProperty dayNameInIndonesianProperty() {
            return dayNameInIndonesian;
        }

        public String getDayNameInIndonesian() {
            return dayNameInIndonesianProperty().get();
        }

        public void setDayNameInIndonesian(String dayNameInIndonesian) {
            dayNameInIndonesianProperty().set(dayNameInIndonesian);
        }

        public IntegerProperty dayOfWeekValueProperty() {
            return dayOfWeekValue;
        }

        public int getDayOfWeekValue() {
            return dayOfWeekValueProperty().get();
        }

        public void setDayOfWeekValue(int dayOfWeekValue) {
            dayOfWeekValueProperty().set(dayOfWeekValue);
        }

        @Override
        public String toString() {
            return dayNameInIndonesianProperty().get();
        }

    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        //txtCodeEvent.textProperty().bindBidirectional(selectedData.codeEventProperty());
        txtEventName.textProperty().bindBidirectional(selectedData.eventNameProperty());
        txtNote.textProperty().bindBidirectional(selectedData.eventNoteProperty());

        Bindings.bindBidirectional(txtMinTransaction.textProperty(), selectedData.minTransactionProperty(), new ClassFormatter.CBigDecimalStringConverter());

        Bindings.bindBidirectional(txtRoomDiscountPercentage.textProperty(), selectedData.roomDiscountPercentageProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemDiscountPercentage.textProperty(), selectedData.itemDiscountPercentageProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtServiceDiscountPercentage.textProperty(), selectedData.serviceDiscountPercentageProperty(), new ClassFormatter.CBigDecimalStringConverter());

        Bindings.bindBidirectional(txtMaxDiscountNominal.textProperty(), selectedData.discountNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        if (selectedData.getBeginEventDate() != null) {
            dtpBeginEventDate.setValue(selectedData.getBeginEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpBeginEventDate.setValue(null);
        }
        dtpBeginEventDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setBeginEventDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        if (selectedData.getEndEventDate() != null) {
            dtpEndEventDate.setValue(selectedData.getEndEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpEndEventDate.setValue(null);
        }
        dtpEndEventDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setEndEventDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        for (DayName data : loadAllDataDayName()) {
            if (data.getDayOfWeekValue() == selectedData.getDayOfWeek()) {
                cbpDayName.setValue(data);
                break;
            }
        }
        cbpDayName.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setDayOfWeek(newVal.getDayOfWeekValue());
            }
        });

        cbpDayName.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        /* txtCodeEvent.setDisable(true);
         ClassViewSetting.setDisableForAllInputNode(gpFormDataHotelEvent,
         dataInputStatus == 3,
         txtCodeEvent); */

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataHotelEventCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblHotelEvent();
        selectedData.setDayOfWeek(0);
        selectedData.setMinTransaction(new BigDecimal("0"));
        selectedData.setMaxTransaction(new BigDecimal("0"));
        selectedData.setDiscountPercentage(new BigDecimal("0"));
        selectedData.setRoomDiscountPercentage(new BigDecimal("0"));
        selectedData.setItemDiscountPercentage(new BigDecimal("0"));
        selectedData.setServiceDiscountPercentage(new BigDecimal("0"));
        selectedData.setDiscountNominal(new BigDecimal("0"));
        lblHotelEvent.setText("");
        setSelectedDataToInputForm();
        //open form data hotelEvent
        dataHotelEventFormShowStatus.set(0.0);
        dataHotelEventFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataHotelEventUpdateHandle() {
        if (tableDataHotelEvent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFReservationPromotionManager().getHotelEvent(((TblHotelEvent) tableDataHotelEvent.getTableView().getSelectionModel().getSelectedItem()).getIdevent());
            lblHotelEvent.setText(selectedData.getCodeEvent() + " - " + selectedData.getEventName());
            setSelectedDataToInputForm();
            //open form data hotelEvent
            dataHotelEventFormShowStatus.set(0.0);
            dataHotelEventFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataHotelEventShowHandle() {
        if (tableDataHotelEvent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFReservationPromotionManager().getHotelEvent(((TblHotelEvent) tableDataHotelEvent.getTableView().getSelectionModel().getSelectedItem()).getIdevent());
            setSelectedDataToInputForm();
            dataHotelEventFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataHotelEventUnshowHandle() {
        refreshDataTableHotelEvent();
        dataHotelEventFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataHotelEventDeleteHandle() {
        if (tableDataHotelEvent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFReservationPromotionManager().deleteDataHotelEvent((TblHotelEvent) tableDataHotelEvent.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data hotelEvent
                    refreshDataTableHotelEvent();
                    dataHotelEventFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataHotelEventPrintHandle() {

    }

    private void dataHotelEventSaveHandle() {
        if (checkDataInputDataHotelEvent()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblHotelEvent dummySelectedData = new TblHotelEvent(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFReservationPromotionManager().insertDataHotelEvent(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data hotelEvent
                            refreshDataTableHotelEvent();
                            dataHotelEventFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFReservationPromotionManager().updateDataHotelEvent(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data hotelEvent
                            refreshDataTableHotelEvent();
                            dataHotelEventFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataHotelEventCancelHandle() {
        //refresh data from table & close form data hotelEvent
        refreshDataTableHotelEvent();
        dataHotelEventFormShowStatus.set(0.0);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
        isShowStatus.set(false);
    }

    public void refreshDataTableHotelEvent() {
        tableDataHotelEvent.getTableView().setItems(loadAllDataHotelEvent());
        cft.refreshFilterItems(tableDataHotelEvent.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataHotelEvent() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtEventName.getText() == null || txtEventName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Event : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpDayName.getValue() == null) {
            dataInput = false;
            errDataInput += "Hari Event : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpBeginEventDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Awal Event : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpEndEventDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Akhir Event : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtMinTransaction.getText() == null
//                || txtMinTransaction.getText().equals("")
//                || txtMinTransaction.getText().equals("-")) {
//            dataInput = false;
//            errDataInput += "Minimal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        } else {
//            if (selectedData.getMinTransaction()
//                    .compareTo(new BigDecimal("0")) == -1) {
//                dataInput = false;
//                errDataInput += "Minimal Transaksi : Nilai tidak dapat kurang dari '0' \n";
//            }
//        }
        if (txtRoomDiscountPercentage.getText() == null
                || txtRoomDiscountPercentage.getText().equals("")
                || txtRoomDiscountPercentage.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Diskon Kamar(%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getRoomDiscountPercentage()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Diskon Kamar(%) : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (txtItemDiscountPercentage.getText() == null
                || txtItemDiscountPercentage.getText().equals("")
                || txtItemDiscountPercentage.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Diskon Barang(%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getItemDiscountPercentage()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Diskon Barang(%) : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (txtServiceDiscountPercentage.getText() == null
                || txtServiceDiscountPercentage.getText().equals("")
                || txtServiceDiscountPercentage.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Diskon Layanan(%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getServiceDiscountPercentage()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Diskon Layanan(%) : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (txtMaxDiscountNominal.getText() == null
                || txtMaxDiscountNominal.getText().equals("")
                || txtMaxDiscountNominal.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Maksimal Nominal Diskon : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getDiscountNominal()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Maksimal Nominal Diskon : Nilai tidak dapat kurang dari '0' \n";
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
        setDataHotelEventSplitpane();

        //init table
        initTableDataHotelEvent();

        //init form
        initFormDataHotelEvent();

        spDataHotelEvent.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataHotelEventFormShowStatus.set(0.0);
        });
    }

    public HotelEventController(FeatureReservationPromotionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReservationPromotionController parentController;

}
