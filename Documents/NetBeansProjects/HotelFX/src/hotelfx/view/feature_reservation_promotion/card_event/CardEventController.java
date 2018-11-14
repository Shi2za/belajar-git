/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_promotion.card_event;

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
import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEventCard;
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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
public class CardEventController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataCardEvent;

    private DoubleProperty dataCardEventFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataCardEventLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataCardEventSplitpane() {
        spDataCardEvent.setDividerPositions(1);

        dataCardEventFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataCardEventFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataCardEvent.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataCardEvent.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataCardEventFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataCardEventLayout.setDisable(false);
                    tableDataCardEventLayoutDisableLayer.setDisable(true);
                    tableDataCardEventLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataCardEventLayout.setDisable(true);
                    tableDataCardEventLayoutDisableLayer.setDisable(false);
                    tableDataCardEventLayoutDisableLayer.toFront();
                }
            }
        });

        dataCardEventFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataCardEventLayout;

    private ClassFilteringTable<TblBankEventCard> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataCardEvent;

    private void initTableDataCardEvent() {
        //set table
        setTableDataCardEvent();
        //set control
        setTableControlDataCardEvent();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataCardEvent, 15.0);
        AnchorPane.setLeftAnchor(tableDataCardEvent, 15.0);
        AnchorPane.setRightAnchor(tableDataCardEvent, 15.0);
        AnchorPane.setTopAnchor(tableDataCardEvent, 15.0);
        ancBodyLayout.getChildren().add(tableDataCardEvent);
    }

    private void setTableDataCardEvent() {
        TableView<TblBankEventCard> tableView = new TableView();

        TableColumn<TblBankEventCard, String> codeCardEvent = new TableColumn("ID");
        codeCardEvent.setCellValueFactory(cellData -> cellData.getValue().codeEventProperty());
        codeCardEvent.setMinWidth(100);

        TableColumn<TblBankEventCard, String> cardEventName = new TableColumn("Event");
        cardEventName.setCellValueFactory(cellData -> cellData.getValue().eventNameProperty());
        cardEventName.setMinWidth(140);

        TableColumn<TblBankEventCard, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblBankEventCard, String> bankCardName = new TableColumn("Kartu Bank");
        bankCardName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankCard() != null
                                ? param.getValue().getTblBankCard().getBankCardName() : "-", param.getValue().tblBankCardProperty()));
        bankCardName.setMinWidth(140);

        TableColumn<TblBankEventCard, String> beginEventDate = new TableColumn("Awal");
        beginEventDate.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getBeginEventDate()), param.getValue().beginEventDateProperty()));
        beginEventDate.setMinWidth(100);

        TableColumn<TblBankEventCard, String> endEventDate = new TableColumn("Akhir");
        endEventDate.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getEndEventDate()), param.getValue().endEventDateProperty()));
        endEventDate.setMinWidth(100);

        TableColumn<TblBankEventCard, String> eventDateTitle = new TableColumn("Tanggal Event");
        eventDateTitle.getColumns().addAll(beginEventDate, endEventDate);

        TableColumn<TblBankEventCard, String> minTransaction = new TableColumn("Minimal Transaksi");
        minTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getMinTransaction()), param.getValue().minTransactionProperty()));
        minTransaction.setMinWidth(180);

        TableColumn<TblBankEventCard, String> roomDiscountPercentage = new TableColumn("Kamar");
        roomDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getRoomDiscountPercentage()) + "%", param.getValue().roomDiscountPercentageProperty()));
        roomDiscountPercentage.setMinWidth(80);

        TableColumn<TblBankEventCard, String> itemDiscountPercentage = new TableColumn("Barang");
        itemDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemDiscountPercentage()) + "%", param.getValue().itemDiscountPercentageProperty()));
        itemDiscountPercentage.setMinWidth(80);

        TableColumn<TblBankEventCard, String> serviceDiscountPercentage = new TableColumn("Layanan");
        serviceDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getServiceDiscountPercentage()) + "%", param.getValue().serviceDiscountPercentageProperty()));
        serviceDiscountPercentage.setMinWidth(80);

        TableColumn<TblBankEventCard, String> discountPercentageTitle = new TableColumn("Diskon (%)");
        discountPercentageTitle.getColumns().addAll(roomDiscountPercentage, itemDiscountPercentage, serviceDiscountPercentage);

        TableColumn<TblBankEventCard, String> maxDiscountNominal = new TableColumn("Maksimal Nominal Diskon");
        maxDiscountNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDiscountNominal()), param.getValue().discountNominalProperty()));
        maxDiscountNominal.setMinWidth(180);

        TableColumn<TblBankEventCard, String> eventNote = new TableColumn("Keterangan");
        eventNote.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
                -> Bindings.createStringBinding(()
                        -> "Min. Transaksi : \n"
                        + ClassFormatter.currencyFormat.cFormat(param.getValue().getMinTransaction()) + "\n"
                        + "Max. Nominal Diskon : \n"
                        + ClassFormatter.currencyFormat.cFormat(param.getValue().getDiscountNominal()),
                        param.getValue().ideventProperty()));
        eventNote.setMinWidth(160);

        tableView.getColumns().addAll(codeCardEvent, cardEventName, bankName, bankCardName,
                eventDateTitle, discountPercentageTitle, eventNote);
        tableView.setItems(loadAllDataCardEvent());

        tableView.setRowFactory(tv -> {
            TableRow<TblBankEventCard> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataCardEventUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataCardEventUpdateHandle();
                            } else {
                                dataCardEventShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataCardEventUpdateHandle();
//                            } else {
//                                dataCardEventShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataCardEvent = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblBankEventCard.class,
                tableDataCardEvent.getTableView(),
                tableDataCardEvent.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataCardEvent() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataCardEventCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataCardEventUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataCardEventDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataCardEvent.addButtonControl(buttonControls);
    }

    private ObservableList<TblBankEventCard> loadAllDataCardEvent() {
        List<TblBankEventCard> list = parentController.getFReservationPromotionManager().getAllDataBankEventCard();
        for (TblBankEventCard data : list) {
            data.setTblBank(parentController.getFReservationPromotionManager().getDataBank(data.getTblBank().getIdbank()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataCardEvent;

    @FXML
    private ScrollPane spFormDataCardEvent;

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
    private Label lblCardEvent;

    @FXML
    private AnchorPane bankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private AnchorPane bankCardLayout;
    private JFXCComboBoxTablePopup<TblBankCard> cbpBankCard;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblBankEventCard selectedData;

    private void initFormDataCardEvent() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataCardEvent.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataCardEvent.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Event Kartu)"));
        btnSave.setOnAction((e) -> {
            dataCardEventSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCardEventCancelHandle();
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
                cbpBank,
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
                txtRoomDiscountPercentage,
                txtItemDiscountPercentage,
                txtServiceDiscountPercentage,
                txtMaxDiscountNominal);
    }

    private void initDataPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(loadAllDataBank());

        cbpBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableBank, bankItems, "", "Bank *", true, 200, 200
        );

        //Bank Card
        TableView<TblBankCard> tableBankCard = new TableView<>();

        TableColumn<TblBankCard, String> bankCardName = new TableColumn<>("Kartu Bank");
        bankCardName.setCellValueFactory(cellData -> cellData.getValue().bankCardNameProperty());
        bankCardName.setMinWidth(140);

        TableColumn<TblBankCard, String> bankCardClassName = new TableColumn<>("Tipe Kartu");
        bankCardClassName.setCellValueFactory(cellData -> cellData.getValue().bankCardClassNameProperty());
        bankCardClassName.setMinWidth(140);

        TableColumn<TblBankCard, String> bankCardType = new TableColumn<>("Jenis Kartu");
        bankCardType.setCellValueFactory((TableColumn.CellDataFeatures<TblBankCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefBankCardType().getTypeName(), param.getValue().refBankCardTypeProperty()));
        bankCardType.setMinWidth(140);

        tableBankCard.getColumns().addAll(bankCardName, bankCardClassName, bankCardType);

        ObservableList<TblBankCard> bankCardItems = FXCollections.observableArrayList(loadAllDataBankCard(null));

        cbpBankCard = new JFXCComboBoxTablePopup<>(
                TblBankCard.class, tableBankCard, bankCardItems, "", "Kartu Bank *", true, 420, 250
        );

        //attached to grid-pane
        //gpFormDataCardEvent.add(cbpBank, 1, 3);
        //gpFormDataCardEvent.add(cbpBankCard, 2, 3);
        AnchorPane.setTopAnchor(cbpBank, 0.0);
        AnchorPane.setBottomAnchor(cbpBank, 0.0);
        AnchorPane.setRightAnchor(cbpBank, 0.0);
        AnchorPane.setLeftAnchor(cbpBank, 0.0);
        bankLayout.getChildren().clear();
        bankLayout.getChildren().add(cbpBank);

        AnchorPane.setTopAnchor(cbpBankCard, 0.0);
        AnchorPane.setBottomAnchor(cbpBankCard, 0.0);
        AnchorPane.setRightAnchor(cbpBankCard, 0.0);
        AnchorPane.setLeftAnchor(cbpBankCard, 0.0);
        bankCardLayout.getChildren().clear();
        bankCardLayout.getChildren().add(cbpBankCard);

    }

    private List<TblBank> loadAllDataBank() {
        List<TblBank> list = parentController.getFReservationPromotionManager().getAllDataBank();
        return list;
    }

    private List<TblBankCard> loadAllDataBankCard(TblBank dataBank) {
        List<TblBankCard> list = new ArrayList<>();
        if (dataBank != null) {
            list = parentController.getFReservationPromotionManager().getAllDataBankCardByIDBank(dataBank.getIdbank());
            for (TblBankCard data : list) {
                //data bank
                data.setTblBank(parentController.getFReservationPromotionManager().getDataBank(data.getTblBank().getIdbank()));
                //data bank card type
                data.setRefBankCardType(parentController.getFReservationPromotionManager().getDataBankCardType(data.getRefBankCardType().getIdtype()));
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(loadAllDataBank());
        cbpBank.setItems(bankItems);

        //Bank Card
        refreshDataPopupBankCard();
    }

    private void refreshDataPopupBankCard() {
        //Bank Card
        ObservableList<TblBankCard> bankCardItems = FXCollections.observableArrayList(loadAllDataBankCard(selectedData.getTblBank()));
        cbpBankCard.setItems(bankCardItems);
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

        selectedData.tblBankProperty().addListener((obs, oldVal, newVal) -> {
            refreshDataPopupBankCard();
            cbpBankCard.setValue(null);
        });

        cbpBank.valueProperty().bindBidirectional(selectedData.tblBankProperty());
        cbpBankCard.valueProperty().bindBidirectional(selectedData.tblBankCardProperty());

        cbpBank.hide();
        cbpBankCard.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        /*txtCodeEvent.setDisable(true);
         ClassViewSetting.setDisableForAllInputNode(gpFormDataCardEvent,
         dataInputStatus == 3,
         txtCodeEvent);*/

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataCardEventCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblBankEventCard();
        selectedData.setMinTransaction(new BigDecimal("0"));
        selectedData.setMaxTransaction(new BigDecimal("0"));
        selectedData.setDiscountPercentage(new BigDecimal("0"));
        selectedData.setRoomDiscountPercentage(new BigDecimal("0"));
        selectedData.setItemDiscountPercentage(new BigDecimal("0"));
        selectedData.setServiceDiscountPercentage(new BigDecimal("0"));
        selectedData.setDiscountPercentageBc(new BigDecimal("0"));
        selectedData.setDiscountPercentageHc(new BigDecimal("0"));
        selectedData.setDiscountNominalBc(new BigDecimal("0"));
        selectedData.setDiscountNominalHc(new BigDecimal("0"));
        selectedData.setDiscountNominal(new BigDecimal("0"));
        lblCardEvent.setText("");
        setSelectedDataToInputForm();
        //open form data cardEvent
        dataCardEventFormShowStatus.set(0.0);
        dataCardEventFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataCardEventUpdateHandle() {
        if (tableDataCardEvent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFReservationPromotionManager().getBankEventCard(((TblBankEventCard) tableDataCardEvent.getTableView().getSelectionModel().getSelectedItem()).getIdevent());
            lblCardEvent.setText(selectedData.getCodeEvent() + " - " + selectedData.getEventName());
            setSelectedDataToInputForm();
            //open form data cardEvent
            dataCardEventFormShowStatus.set(0.0);
            dataCardEventFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataCardEventShowHandle() {
        if (tableDataCardEvent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFReservationPromotionManager().getBankEventCard(((TblBankEventCard) tableDataCardEvent.getTableView().getSelectionModel().getSelectedItem()).getIdevent());
            setSelectedDataToInputForm();
            dataCardEventFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataCardEventUnshowHandle() {
        refreshDataTableCardEvent();
        dataCardEventFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataCardEventDeleteHandle() {
        if (tableDataCardEvent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFReservationPromotionManager().deleteDataBankEventCard((TblBankEventCard) tableDataCardEvent.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data cardEvent
                    refreshDataTableCardEvent();
                    dataCardEventFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataCardEventPrintHandle() {

    }

    private void dataCardEventSaveHandle() {
        if (checkDataInputDataCardEvent()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblBankEventCard dummySelectedData = new TblBankEventCard(selectedData);
                dummySelectedData.setTblBank(new TblBank(dummySelectedData.getTblBank()));
                if (dummySelectedData.getTblBankCard() != null) {
                    dummySelectedData.setTblBankCard(new TblBankCard(dummySelectedData.getTblBankCard()));
                    dummySelectedData.getTblBankCard().setTblBank(new TblBank(dummySelectedData.getTblBankCard().getTblBank()));
                    dummySelectedData.getTblBankCard().setRefBankCardType(new RefBankCardType(dummySelectedData.getTblBankCard().getRefBankCardType()));
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFReservationPromotionManager().insertDataBankEventCard(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data cardEvent
                            refreshDataTableCardEvent();
                            dataCardEventFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFReservationPromotionManager().updateDataBankEventCard(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data cardEvent
                            refreshDataTableCardEvent();
                            dataCardEventFormShowStatus.set(0.0);
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

    private void dataCardEventCancelHandle() {
        //refresh data from table & close form data cardEvent
        refreshDataTableCardEvent();
        dataCardEventFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableCardEvent() {
        tableDataCardEvent.getTableView().setItems(loadAllDataCardEvent());
        cft.refreshFilterItems(tableDataCardEvent.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataCardEvent() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtEventName.getText() == null || txtEventName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Event : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBank.getValue() == null) {
            dataInput = false;
            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpBankCard.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Kartu Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
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
        setDataCardEventSplitpane();

        //init table
        initTableDataCardEvent();

        //init form
        initFormDataCardEvent();

        spDataCardEvent.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataCardEventFormShowStatus.set(0.0);
        });
    }

    public CardEventController(FeatureReservationPromotionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReservationPromotionController parentController;

}
