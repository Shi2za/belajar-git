/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCDatePicker;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillDiscountType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblRoomTypeRoomService;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationRoomTypeDetailInputController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRRTD;

    private DoubleProperty dataRRTDFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane calendarDataRTAvailableLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataRRTDSplitpane() {
        spDataRRTD.setDividerPositions(1);
        
        dataRRTDFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRRTDFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRRTD.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRRTD.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRRTDFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 0.0) {    //enable
                calendarDataRTAvailableLayout.setDisable(false);
                calendarDataRTAvailableLayoutDisableLayer.setDisable(true);
                calendarDataRTAvailableLayout.toFront();
            }
            if (newVal.doubleValue() == 1) {  //disable
                calendarDataRTAvailableLayout.setDisable(true);
                calendarDataRTAvailableLayoutDisableLayer.setDisable(false);
                calendarDataRTAvailableLayoutDisableLayer.toFront();
            }
        });

        dataRRTDFormShowStatus.set(0.0);

    }

    /**
     * CALENDAR - ROOM TYPE (AVAILABLE) DATA
     */
    @FXML
    private AnchorPane calendarDataRTAvailableLayout;

    @FXML
    private GridPane gpHeaderLayout;

    private JFXCComboBoxTablePopup<SelectedDiscount> cbpSelectedDiscount;

    @FXML
    private JFXCheckBox chbUseBreakfast;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXCDatePicker btnGoToDate;

    @FXML
    private JFXButton btnPrevious;

    @FXML
    private JFXButton btnNext;

    @FXML
    private JFXButton btnGotToCurrentDatePosition;

    @FXML
    private Label lblCalendarTitleMin3;

    @FXML
    private Label lblCalendarTitleMin2;

    @FXML
    private Label lblCalendarTitleMin1;

    @FXML
    private Label lblCalendarTitle;

    @FXML
    private Label lblCalendarTitlePlus1;

    @FXML
    private Label lblCalendarTitlePlus2;

    @FXML
    private Label lblCalendarTitlePlus3;

    @FXML
    private Label lblCalendarTitlePlus4;

    @FXML
    private GridPane gpRoomTypes;

    @FXML
    private GridPane gpCalendars;

    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();

    private ObservableList<TblRoomType> roomTypes = FXCollections.observableArrayList();

    private void initCalendarRoomTypeAvailable() {
        //set current date
        selectedDate.set(LocalDate.now());
        //set calendar function
        setCalendarFunction();
        //set room type
        setRoomType();
        //set calendar - room type (available)
        setCalendarRoomTypeAvailable();
        //set visible 'false'
        chbUseBreakfast.setVisible(false);
    }

    private void setCalendarFunction() {
        initDataPopup();

        spnDayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spnDayNumber.setEditable(false);
        spnDayNumber.getValueFactory().setValue(0);

        spnRoomNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spnRoomNumber.setEditable(false);
        spnRoomNumber.getValueFactory().setValue(0);
        
        //back
        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnMouseClicked((e) -> {
            if (reservationController.dataInputStatus == 1) {   //update
                //refresh data
                reservationController.refreshDataSelectedReservation();
            }
            //close dialog
            reservationController.dialogStage.close();
        });

        //previous
        btnPrevious.setTooltip(new Tooltip("Sebelum"));
        btnPrevious.setOnMouseClicked((e) -> {
            selectedDate.set(selectedDate.get().minusDays(1));
        });

        //next
        btnNext.setTooltip(new Tooltip("Setelah"));
        btnNext.setOnMouseClicked((e) -> {
            selectedDate.set(selectedDate.get().plusDays(1));
        });

        //current
        btnGotToCurrentDatePosition.setText(ClassFormatter.dateFormate.format(Date.valueOf(LocalDate.now())));
        btnGotToCurrentDatePosition.setTooltip(new Tooltip("Kembali ke Tanggal Sekarang.."));
        btnGotToCurrentDatePosition.setOnMouseClicked((e) -> {
//            btnGotToCurrentDatePosition.setText(ClassFormatter.dateFormate.format(Date.valueOf(LocalDate.now())));
            selectedDate.set(LocalDate.now());
        });

        selectedDate.addListener((obs, oldVal, newVal) -> {
            refreshCalendarRoomTypeAvailableContetnt();
        });

        //Breakfast
        chbUseBreakfast.setSelected(false);
        chbUseBreakfast.selectedProperty().addListener((obs, oldVal, newVal) -> {
            refreshCalendarRoomTypeAvailableContetnt();
        });

        initDateCalendar();

        initImportantFieldColor();

        cbpSelectedDiscount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshCalendarRoomTypeAvailableContetnt();
            }
        });

        setCalendarBinding();

        spnDayNumber.valueProperty().addListener((obs, oldVal, newVal) -> {
            refreshDataCheckInOutDayNumber(dtpCheckInDate.getValue(),
                    dtpCheckOutDate.getValue(),
                    newVal, 
                    "day-number");
        });

        dtpCheckInDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            refreshDataCheckInOutDayNumber(newVal,
                    dtpCheckOutDate.getValue(),
                    spnDayNumber.getValue(), 
                    "checkin-date");
        });

        dtpCheckOutDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            refreshDataCheckInOutDayNumber(dtpCheckInDate.getValue(),
                    newVal,
                    spnDayNumber.getValue(), 
                    "checkout-date");
        });

        refreshDataCheckInOutDayNumber(dtpCheckInDate.getValue(),
                dtpCheckOutDate.getValue(),
                spnDayNumber.getValue(), 
                "");
        
        spnRoomNumber.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                roomNumber.set(newVal);
            }else{
                //set ... ??
            }
        });
    }

    private void refreshDataCheckInOutDayNumber(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int dayNumber, 
            String newVal) {
        int reCountDayNumber;
        switch(newVal) {
            case "checkin-date" : 
                if(checkInDate != null
                        && checkOutDate != null){
                    reCountDayNumber = (int)ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                    spnDayNumber.getValueFactory().setValue(reCountDayNumber);
                }
                break;
            case "checkout-date" : 
                if(checkInDate != null
                        && checkOutDate != null){
                    reCountDayNumber = (int)ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                    spnDayNumber.getValueFactory().setValue(reCountDayNumber);
                }
                break;
            case "day-number" : 
                if(checkInDate != null
                        && checkOutDate != null){
                    dtpCheckOutDate.setValue(checkInDate.plusDays(dayNumber));
                }
                break;
            default :
                break;
        }
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                btnGoToDate,
                dtpCheckInDate,
                dtpCheckOutDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpCheckInDate,
                dtpCheckOutDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dtpCheckInDate,
                dtpCheckOutDate,
                spnDayNumber, 
                spnRoomNumber);
    }

    private void initDataPopup() {
        //data selected discount
        TableView<SelectedDiscount> tableSelectedDiscount = new TableView<>();

        TableColumn<SelectedDiscount, String> discountName = new TableColumn<>("Nama");
        discountName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataDiscountType() != null
                                ? (param.getValue().getDataDiscountType().getIdtype() == 0 //Hotel-Event = '0'
                                        ? "Hotel" : param.getValue().getDataBankCard().getBankCardName())
                                : "-", param.getValue().dataDiscountTypeProperty()));
        discountName.setMinWidth(140);

        TableColumn<SelectedDiscount, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBankCard() != null
                                ? param.getValue().getDataBankCard().getTblBank().getBankName() : "-", param.getValue().dataBankCardProperty()));
        bankName.setMinWidth(140);

        TableColumn<SelectedDiscount, String> cardClassName = new TableColumn<>("Tipe");
        cardClassName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBankCard() != null
                                ? param.getValue().getDataBankCard().getBankCardClassName() : "-",
                        param.getValue().dataBankCardProperty()));
        cardClassName.setMinWidth(140);

        TableColumn<SelectedDiscount, String> cardTypeName = new TableColumn<>("Jenis");
        cardTypeName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBankCard() != null
                                ? param.getValue().getDataBankCard().getRefBankCardType().getTypeName() : "-", param.getValue().dataBankCardProperty()));
        cardTypeName.setMinWidth(140);

        tableSelectedDiscount.getColumns().addAll(discountName, bankName, cardTypeName, cardClassName);

        ObservableList<SelectedDiscount> selectedDiscountItems = FXCollections.observableArrayList(loadAllDataSelectedDiscount());

        cbpSelectedDiscount = new JFXCComboBoxTablePopup<>(
                SelectedDiscount.class, tableSelectedDiscount, selectedDiscountItems, "", "Diskon", true, 580, 350
        );

        //attached to grid-pane
        gpHeaderLayout.add(cbpSelectedDiscount, 0, 0);

        //refresh data popup
        refreshDataPopup();
    }

    private List<SelectedDiscount> loadAllDataSelectedDiscount() {
        List<SelectedDiscount> list = new ArrayList<>();
        //data null
        SelectedDiscount dataNullDiscount = new SelectedDiscount(null, null);
        list.add(dataNullDiscount);
        //data hotel-event
        SelectedDiscount dataHotelDiscount = new SelectedDiscount(null, reservationController.getFReservationManager().getReservationBillDiscountType(0));  //Hotel-Event = '0'
        list.add(dataHotelDiscount);
        //data card-event
        List<TblBankCard> dataBankCards = reservationController.getFReservationManager().getAllDataBankCard();
        for (TblBankCard dataBankCard : dataBankCards) {
            //data bank
            dataBankCard.setTblBank(reservationController.getFReservationManager().getBank(dataBankCard.getTblBank().getIdbank()));
            //data bank card type
            dataBankCard.setRefBankCardType(reservationController.getFReservationManager().getDataBankCardType(dataBankCard.getRefBankCardType().getIdtype()));
            //data selected discount
            SelectedDiscount dataCardDiscount = new SelectedDiscount(dataBankCard, reservationController.getFReservationManager().getReservationBillDiscountType(1));  //Card-Event = '1'
            list.add(dataCardDiscount);
        }
        return list;
    }

    private void refreshDataPopup() {
        //Selected Discount
        ObservableList<SelectedDiscount> selectedDiscountItems = FXCollections.observableArrayList(loadAllDataSelectedDiscount());
        cbpSelectedDiscount.setItems(selectedDiscountItems);

        //set selected data (value)
        cbpSelectedDiscount.setValue(new SelectedDiscount(
                reservationController.dataReservationBill.getTblBankCard() != null ? new TblBankCard(reservationController.dataReservationBill.getTblBankCard()) : null,
                reservationController.dataReservationBill.getRefReservationBillDiscountType() != null ? new RefReservationBillDiscountType(reservationController.dataReservationBill.getRefReservationBillDiscountType()) : null
        ));

        //set disable
        cbpSelectedDiscount.setDisable(!reservationController.dataReservationRoomTypeDetails.isEmpty()
                || (reservationController.selectedData.getTblPartner() != null));

    }

    public class SelectedDiscount {

        private final ObjectProperty<TblBankCard> dataBankCard = new SimpleObjectProperty<>();

        private final ObjectProperty<RefReservationBillDiscountType> dataDiscountType = new SimpleObjectProperty<>();

        public SelectedDiscount(TblBankCard dataBankCard,
                RefReservationBillDiscountType dataDiscountType) {

            this.dataBankCard.set(dataBankCard);

            this.dataDiscountType.set(dataDiscountType);

        }

        public ObjectProperty<TblBankCard> dataBankCardProperty() {
            return dataBankCard;
        }

        public TblBankCard getDataBankCard() {
            return dataBankCardProperty().get();
        }

        public void setDataBankCard(TblBankCard dataBankCard) {
            dataBankCardProperty().set(dataBankCard);
        }

        public ObjectProperty<RefReservationBillDiscountType> dataDiscountTypeProperty() {
            return dataDiscountType;
        }

        public RefReservationBillDiscountType getDataDiscountType() {
            return dataDiscountTypeProperty().get();
        }

        public void setDataDiscountType(RefReservationBillDiscountType dataDiscountType) {
            dataDiscountTypeProperty().set(dataDiscountType);
        }

        @Override
        public String toString() {
            if (getDataDiscountType() != null) {
                if (getDataDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                    return "Hotel";
                }
                if (getDataDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    return getDataBankCard() != null ? getDataBankCard().getBankCardName() : "-";
                }
            }
            return "-";
        }

    }

    private void setCalendarBinding() {
        //go to date value
        JFXButton arrowButtonContent = new JFXButton();
        arrowButtonContent.setPrefSize(185, 25);
        arrowButtonContent.setButtonType(JFXButton.ButtonType.RAISED);
        arrowButtonContent.setTooltip(new Tooltip("Pilih Tanggal.."));
        arrowButtonContent.setOnMouseClicked((e) -> btnGoToDate.show());
        arrowButtonContent.textProperty().bind(Bindings.createStringBinding(()
                -> (btnGoToDate.valueProperty().get() != null)
                        ? btnGoToDate.valueProperty().get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "", btnGoToDate.valueProperty()));
        btnGoToDate.setArrowButtonContent(arrowButtonContent);
        btnGoToDate.valueProperty().bindBidirectional(selectedDate);

        //label - titled
        lblCalendarTitleMin3.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().minusDays(3)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitleMin2.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().minusDays(2)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitleMin1.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().minusDays(1)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitle.textProperty().bind(Bindings.createStringBinding(() -> selectedDate.get().format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus1.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(1)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus2.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(2)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus3.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(3)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus4.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(4)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
    }

    private void setRoomType() {
        roomTypes = loadAllDataRoomType();
        gpRoomTypes.getChildren().clear();
        gpRoomTypes.setVgap(5.0);
        for (int i = 0; i < roomTypes.size(); i++) {
            gpRoomTypes.getRowConstraints().add(new RowConstraints());
            RoomTypeButton button = getRoomTypeButton(roomTypes.get(i));
            button.setPrefSize(100, 80);
            gpRoomTypes.add(button, 0, i);
        }
    }

    private ObservableList<TblRoomType> loadAllDataRoomType() {
        return FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataRoomType());
    }

    private RoomTypeButton getRoomTypeButton(TblRoomType roomType) {
        RoomTypeButton button = new RoomTypeButton(roomType.getRoomTypeName(), roomType);
        button.getStyleClass().add("button-room-type");
        button.setAlignment(Pos.CENTER);
        button.setOnMouseClicked((e) -> {
            e.consume();
            if (e.getClickCount() == 2) {
                dataRRTDCreateHandle(button.getButtonValue(), LocalDate.now(), (LocalDate.now()).plusDays(1));
            }
        });
        button.setTooltip(new Tooltip(getDataRoomTypeHoverInfo(roomType)));
        return button;
    }

    private String getDataRoomTypeHoverInfo(TblRoomType roomType) {
        String result;
        result = "Tipe Kamar : " + roomType.getRoomTypeName() + " \n";
        result += "\n";
        List<TblFloor> dataFloors = reservationController.getFReservationManager().getAllDataFloor();
        List<TblRoom> dataRooms = reservationController.getFReservationManager().getAllDataRoomByIDRoomType(roomType.getIdroomType());
        for (TblFloor dataFloor : dataFloors) {
            result += "- " + dataFloor.getFloorName() + " : ";
            //smoking
            int count = 0;
            for (TblRoom dataRoom : dataRooms) {
                if (dataRoom.getTblLocation().getTblFloor().getIdfloor() == dataFloor.getIdfloor()) {
                    if (dataRoom.getSmokingStatus() != 0) {   //smoking
                        count++;
                    }
                }
            }
            result += count + " (smoking) & ";
            //non smoking
            count = 0;
            for (TblRoom dataRoom : dataRooms) {
                if (dataRoom.getTblLocation().getTblFloor().getIdfloor() == dataFloor.getIdfloor()) {
                    if (dataRoom.getSmokingStatus() == 0) {   //non smoking
                        count++;
                    }
                }
            }
            result += count + " (non smoking)";
            result += " \n";
        }
        result += "\n";
        result += "Fasilitas :  \n";
        List<TblRoomTypeItem> dataRoomItems = reservationController.getFReservationManager().getAllDataRoomTypeItemByIDRoomType(roomType.getIdroomType());
        for (TblRoomTypeItem dataRoomItem : dataRoomItems) {
            result += "- " + dataRoomItem.getTblItem().getItemName() + " (" + ClassFormatter.decimalFormat.cFormat(dataRoomItem.getItemQuantity()) + ") \n";
        }
        result += "\n";
        result += "Layanan :  \n";
        List<TblRoomTypeRoomService> dataRoomServices = reservationController.getFReservationManager().getAllDataRoomTypeRoomServiceByIDRoomType(roomType.getIdroomType());
        for (TblRoomTypeRoomService dataRoomService : dataRoomServices) {
            result += "- " + dataRoomService.getTblRoomService().getServiceName()
                    + (dataRoomService.getPeopleNumber() != 0 ? (" (" + dataRoomService.getPeopleNumber() + " orang)") : "")
                    + " \n";
        }
        result += "\n";
        return result;
    }

    private void setCalendarRoomTypeAvailable() {
        gpCalendars.getChildren().clear();
        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 15) {
                GridPane gpCalendar = new GridPane();
//                gpCalendar.setGridLinesVisible(true);
                gpCalendar.setVgap(5.0);
                for (int j = 0; j < roomTypes.size(); j++) {
                    gpCalendar.getRowConstraints().add(new RowConstraints());
                    CalendarRoomTypeAvailableButton button = getCalendarRoomTypeAvailableButton(roomTypes.get(j), null);
                    button.prefWidthProperty().bind(Bindings.createDoubleBinding(
                            () -> gpCalendars.getWidth() / 8.0,
                            gpCalendars.widthProperty()));
                    button.setPrefHeight(80);
                    gpCalendar.add(button, 0, j);
                }
                gpCalendars.add(gpCalendar, i, 0);
            } else {
                if (i % 2 != 0) {
                    GridPane gpCalendar = new GridPane();
//                    gpCalendar.setGridLinesVisible(true);
                    gpCalendar.setVgap(5.0);
                    for (int j = 0; j < roomTypes.size(); j++) {
                        gpCalendar.getRowConstraints().add(new RowConstraints());
                        LocalDate dateInPosition = getDateInPosition(i);
                        BigDecimal roomTypeCurrentPrice = getRoomTypeCurrentPrice(roomTypes.get(j), getDateInPosition(i));
                        CalendarRoomTypeAvailableButton button = getCalendarRoomTypeAvailableButton(roomTypes.get(j),
                                dateInPosition,
                                getRoomReservedNumber(roomTypes.get(j), dateInPosition),
                                getRoomWantToReserveNumber(roomTypes.get(j), dateInPosition),
                                getRoomAvailableNumber(roomTypes.get(j), dateInPosition),
                                roomTypeCurrentPrice,
                                getRoomDiscountPercentage(roomTypes.get(j), dateInPosition, cbpSelectedDiscount.getValue(), roomTypeCurrentPrice),
                                getRoomDiscountName(roomTypes.get(j), dateInPosition, cbpSelectedDiscount.getValue(), roomTypeCurrentPrice),
                                getBreakfastCost(chbUseBreakfast.isSelected()),
                                getBreakfastDiscountPercentage(dateInPosition, cbpSelectedDiscount.getValue()),
                                getBreakfastDiscountName(dateInPosition, cbpSelectedDiscount.getValue()));
                        button.prefWidthProperty().bind(Bindings.createDoubleBinding(
                                () -> gpCalendars.getWidth() / 8.0,
                                gpCalendars.widthProperty()));
                        button.setPrefHeight(80);
                        setAvailableButtonToDoubleClick(button);
                        gpCalendar.add(button, 0, j);
                    }
                    gpCalendars.add(gpCalendar, i, 0, 2, 1);
                }
            }
        }
    }

    private LocalDate getDateInPosition(int position) {
        int realPosition = (position + 1) / 2;
        switch (realPosition) {
            case 1:
                return selectedDate.get().minusDays(3);
            case 2:
                return selectedDate.get().minusDays(2);
            case 3:
                return selectedDate.get().minusDays(1);
            case 4:
                return selectedDate.get().minusDays(0);
            case 5:
                return selectedDate.get().plusDays(1);
            case 6:
                return selectedDate.get().plusDays(2);
            case 7:
                return selectedDate.get().plusDays(3);
            default:
                return null;
        }
    }

    private int getRoomReservedNumber(TblRoomType roomType, LocalDate date) {
        int result = 0;
        if (reservationController.reservationType.getIdtype() == 0) {   //Customer = '0'
            List<TblReservationRoomPriceDetail> roomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail relation = reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetail roomTypeDetail = reservationController.getFReservationManager().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                roomTypeDetail.setTblReservation(reservationController.getFReservationManager().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                roomTypeDetail.getTblReservation().setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
//                if (roomTypeDetail.getTblRoomType().getRoomTypeName().equals(roomType.getRoomTypeName())    @@@+++
                if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                        && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 0 //Customer = '0'
                        && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                    result++;
                }
            }
        } else {  //Travel Agent
            List<TblReservationRoomPriceDetail> roomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail relation = reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetail roomTypeDetail = reservationController.getFReservationManager().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                roomTypeDetail.setTblReservation(reservationController.getFReservationManager().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                roomTypeDetail.getTblReservation().setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
//                if (roomTypeDetail.getTblRoomType().getRoomTypeName().equals(roomType.getRoomTypeName())    @@@+++
                if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                        && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 1 //Travel Agent = '1'
                        && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                    TblReservationRoomTypeDetailTravelAgentDiscountDetail relation1 = reservationController.getFReservationManager().getReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(roomTypeDetail.getIddetail());
                    TblReservationTravelAgentDiscountDetail travelAgentDiscountDetail = reservationController.getFReservationManager().getReservationTravelAgentDiscountDetail(relation1.getTblReservationTravelAgentDiscountDetail().getIddetail());
                    if (reservationController.currentTravelAgent.getTblPartner().getIdpartner() == travelAgentDiscountDetail.getTblPartner().getIdpartner()) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    private int getRoomWantToReserveNumber(TblRoomType roomType, LocalDate date) {
        int result = 0;
        if (reservationController.dataInputStatus == 0) { //create
            for (TblReservationRoomTypeDetail roomTypeDetail : reservationController.dataReservationRoomTypeDetails) {
                LocalDate checkInDate = LocalDate.of(
                        roomTypeDetail.getCheckInDateTime().getYear() + 1900,
                        roomTypeDetail.getCheckInDateTime().getMonth() + 1,
                        roomTypeDetail.getCheckInDateTime().getDate());
                LocalDate checkoutDate = LocalDate.of(
                        roomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                        roomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                        roomTypeDetail.getCheckOutDateTime().getDate());
                if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                        && !date.isBefore(checkInDate)
                        && date.isBefore(checkoutDate)) {
//                        && date.equals(LocalDate.of(
//                                        roomTypeDetail.getCheckInDateTime().getYear() + 1900,
//                                        roomTypeDetail.getCheckInDateTime().getMonth() + 1,
//                                        roomTypeDetail.getCheckInDateTime().getDate()))) {
                    result++;
                }
            }
        }
        return result;
    }

    private int getRoomAvailableNumber(TblRoomType roomType, LocalDate date) {
        int result = 0;
        if (reservationController.reservationType.getIdtype() == 0) {   //Customer = '0'
            List<TblRoom> listRoom = reservationController.getFReservationManager().getAllDataRoomByIDRoomType(roomType.getIdroomType());
            for (int i = listRoom.size() - 1; i > -1; i--) {
                if (listRoom.get(i).getRefRoomStatus().getIdstatus() == 6) {  //Out Of Service = '6'
                    listRoom.remove(i);
                }
            }
            result = listRoom.size();
            List<TblTravelAgentRoomType> travelAgentRoomTypes = reservationController.getFReservationManager().getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(
                    roomType.getIdroomType(),
                    Date.valueOf(date));
            for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
                result -= travelAgentRoomType.getRoomNumber();
            }
        } else {  //Travel Agent
            TblTravelAgentRoomType travelAgentRoomType = reservationController.getFReservationManager().getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(roomType.getIdroomType(),
                    reservationController.currentTravelAgent.getTblPartner().getIdpartner(),
                    Date.valueOf(date));
            if (travelAgentRoomType != null) {
                result = travelAgentRoomType.getRoomNumber();
            }
        }
        return result;
    }

    private BigDecimal getRoomTypeCurrentPrice(TblRoomType roomType, LocalDate date) {
        return getRoomPrice(reservationController.reservationType, roomType, date);
    }

    private BigDecimal getRoomDiscountPercentage(TblRoomType roomType,
            LocalDate date,
            SelectedDiscount selectedDisount,
            BigDecimal roomPrice) {
        BigDecimal result = new BigDecimal("0");
        if (reservationController.reservationType.getIdtype() == 0 //Customer = '0'
                && selectedDisount != null
                && selectedDisount.getDataDiscountType() != null) {
            if (selectedDisount.getDataDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (roomType.getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(Date.valueOf(date));
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (roomPrice.compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                if ((roomPrice.multiply(dataHotelEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                        .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                    result = (dataHotelEvent.getDiscountNominal().divide(roomPrice)).multiply(new BigDecimal("100"));
                                    break;
                                } else {
                                    result = dataHotelEvent.getRoomDiscountPercentage();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (selectedDisount.getDataDiscountType().getIdtype() == 1) { //Card-Event = '1'
                if (roomType.getCardDiscountable()) {
                    List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
                            Date.valueOf(date),
                            selectedDisount.getDataBankCard().getIdbankCard(),
                            selectedDisount.getDataBankCard().getTblBank().getIdbank());
                    if (!dataCardEvents.isEmpty()) {
                        for (TblBankEventCard dataCardEvent : dataCardEvents) {
                            if (roomPrice.compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                if ((roomPrice.multiply(dataCardEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                        .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                    result = (dataCardEvent.getDiscountNominal().divide(roomPrice)).multiply(new BigDecimal("100"));
                                    break;
                                } else {
                                    result = dataCardEvent.getRoomDiscountPercentage();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String getRoomDiscountName(TblRoomType roomType,
            LocalDate date,
            SelectedDiscount selectedDisount,
            BigDecimal roomPrice) {
        String result = "";
        if (reservationController.reservationType.getIdtype() == 0 //Customer = '0'
                && selectedDisount != null
                && selectedDisount.getDataDiscountType() != null) {
            if (selectedDisount.getDataDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (roomType.getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(Date.valueOf(date));
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (roomPrice.compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                result = dataHotelEvent.getEventName();
                                break;
                            }
                        }
                    }
                }
            }
            if (selectedDisount.getDataDiscountType().getIdtype() == 1) { //Card-Event = '1'
                if (roomType.getCardDiscountable()) {
                    List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
                            Date.valueOf(date),
                            selectedDisount.getDataBankCard().getIdbankCard(),
                            selectedDisount.getDataBankCard().getTblBank().getIdbank());
                    if (!dataCardEvents.isEmpty()) {
                        for (TblBankEventCard dataCardEvent : dataCardEvents) {
                            if (roomPrice.compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                result = dataCardEvent.getEventName();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal getBreakfastCost(boolean usedBreakfast) {
        BigDecimal result = new BigDecimal("0");
        if (usedBreakfast) {
            TblRoomService dataRoomService = reservationController.getFReservationManager().getRoomService(1);   //Breakfast = '1';
            result = dataRoomService.getPrice();
        }
        return result;
    }

    private BigDecimal getBreakfastDiscountPercentage(LocalDate date,
            SelectedDiscount selectedDisount) {
        BigDecimal result = new BigDecimal("0");
        if (reservationController.reservationType.getIdtype() == 0 //Customer = '0'
                && selectedDisount != null
                && selectedDisount.getDataDiscountType() != null) {
            TblRoomService dataBreakfast = reservationController.getFReservationManager().getRoomService(1);    //Breakfast = '1'
            if (selectedDisount.getDataDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataBreakfast.getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(Date.valueOf(date));
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (dataBreakfast.getPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                if ((dataBreakfast.getPrice().multiply(dataHotelEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                        .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                    result = (dataHotelEvent.getDiscountNominal().divide(dataBreakfast.getPrice())).multiply(new BigDecimal("100"));
                                } else {
                                    result = dataHotelEvent.getServiceDiscountPercentage();
                                }
                            }
                        }
                    }
                }
            }
            if (selectedDisount.getDataDiscountType().getIdtype() == 1) { //Card-Event = '1'
                if (dataBreakfast.getCardDiscountable()) {
                    List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
                            Date.valueOf(date),
                            selectedDisount.getDataBankCard().getIdbankCard(),
                            selectedDisount.getDataBankCard().getTblBank().getIdbank());
                    if (!dataCardEvents.isEmpty()) {
                        for (TblBankEventCard dataCardEvent : dataCardEvents) {
                            if (dataBreakfast.getPrice().compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                if ((dataBreakfast.getPrice().multiply(dataCardEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                        .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                    result = (dataCardEvent.getDiscountNominal().divide(dataBreakfast.getPrice())).multiply(new BigDecimal("100"));
                                } else {
                                    result = dataCardEvent.getServiceDiscountPercentage();
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String getBreakfastDiscountName(LocalDate date,
            SelectedDiscount selectedDisount) {
        String result = "";
        if (reservationController.reservationType.getIdtype() == 0 //Customer = '0'
                && selectedDisount != null
                && selectedDisount.getDataDiscountType() != null) {
            TblRoomService dataBreakfast = reservationController.getFReservationManager().getRoomService(1);    //Breakfast = '1'
            if (selectedDisount.getDataDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataBreakfast.getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(Date.valueOf(date));
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (dataBreakfast.getPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                result = dataHotelEvent.getEventName();
                                break;
                            }
                        }
                    }
                }
            }
            if (selectedDisount.getDataDiscountType().getIdtype() == 1) { //Card-Event = '1'
                if (dataBreakfast.getCardDiscountable()) {
                    List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
                            Date.valueOf(date),
                            selectedDisount.getDataBankCard().getIdbankCard(),
                            selectedDisount.getDataBankCard().getTblBank().getIdbank());
                    if (!dataCardEvents.isEmpty()) {
                        for (TblBankEventCard dataCardEvent : dataCardEvents) {
                            if (dataBreakfast.getPrice().compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                result = dataCardEvent.getEventName();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private CalendarRoomTypeAvailableButton getCalendarRoomTypeAvailableButton(TblRoomType roomType, LocalDate date,
            int roomReservedNumber, int roomWantToReserveNumber, int roomAvailableNumber,
            BigDecimal roomPrice, BigDecimal discountRoomPercentage, String discountRoomName,
            BigDecimal breakfastCost, BigDecimal discountBreakfastPercentage, String discountBreakfastName) {
        CalendarRoomTypeAvailableButton button = new CalendarRoomTypeAvailableButton(
                getDataInfo(roomType, roomReservedNumber, roomWantToReserveNumber, roomAvailableNumber, roomPrice, discountRoomPercentage, discountRoomName, breakfastCost, discountBreakfastPercentage, discountBreakfastName),
                roomType, date, roomReservedNumber, roomWantToReserveNumber, roomAvailableNumber, roomPrice, discountRoomPercentage, discountRoomName);
//        button.getStyleClass().add("button-calendar-room-type-available");
        button.setAlignment(Pos.CENTER);
        button.setOnMouseClicked((e) -> {
            e.consume();
            if (e.getClickCount() == 2) {
                dataRRTDCreateHandle(button.getButtonValue(), date, date.plusDays(1));
            }
        });
        button.setWrapText(true);
        button.setTooltip(new Tooltip(getDataHoverInfo(roomType, roomReservedNumber, roomWantToReserveNumber, roomAvailableNumber, roomPrice, discountRoomPercentage, discountRoomName, breakfastCost, discountBreakfastPercentage, discountBreakfastName)));
        return button;
    }

    private String getDataInfo(TblRoomType roomType,
            int roomReservedNumber, int roomWantToReserveNumber, int roomAvailableNumber,
            BigDecimal roomPrice, BigDecimal discountRoomPercentage, String discountRoomName,
            BigDecimal breakfastCost, BigDecimal discountBreakfastPercentage, String discountBreakfastName) {
        String result;
//        result = "( " + (roomReservedNumber + roomWantToReserveNumber) + " / " + roomAvailableNumber + " )\n";
        int currentRoomAvalableNumber = roomAvailableNumber - (roomReservedNumber + roomWantToReserveNumber);
        result = "( " + (currentRoomAvalableNumber == 0 ? "FULL" : currentRoomAvalableNumber) + " )\n";
        result += ClassFormatter.currencyFormat.cFormat(((roomPrice.multiply((new BigDecimal("1")).subtract(discountRoomPercentage.divide(new BigDecimal("100")))))
                .add(breakfastCost.multiply((new BigDecimal("1")).subtract(discountBreakfastPercentage.divide(new BigDecimal("100"))))))
                .multiply((new BigDecimal("1")).add(reservationController.dataReservationBill.getServiceChargePercentage()))
                .multiply((new BigDecimal("1")).add(reservationController.dataReservationBill.getTaxPercentage())));
        return result;
    }

    private String getDataHoverInfo(TblRoomType roomType,
            int roomReservedNumber, int roomWantToReserveNumber, int roomAvailableNumber,
            BigDecimal roomPrice, BigDecimal discountRoomPercentage, String discountRoomName,
            BigDecimal breakfastCost, BigDecimal discountBreakfastPercentage, String discountBreakfastName) {
        String result;
        result = "Total Kamar : " + roomAvailableNumber + " \n";
        result += "Kamar Dipesan : " + (roomReservedNumber + roomWantToReserveNumber) + " \n";
        result += "Sisa Kamar : " + (roomAvailableNumber - (roomReservedNumber + roomWantToReserveNumber)) + " \n";
        result += "\n";
        result += "Harga Kamar : " + ClassFormatter.currencyFormat.cFormat(roomPrice) + "\n";
        result += "Diskon Kamar : " + ClassFormatter.decimalFormat.cFormat((discountRoomPercentage)) + "% " + (discountRoomName.equals("") ? "" : "- " + discountRoomName) + "\n";
//        result += "Harga Breakfast : " + ClassFormatter.currencyFormat.cFormat(breakfastCost) + "\n";
//        result += "Diskon Breakfast : " + ClassFormatter.decimalFormat.cFormat((discountBreakfastPercentage)) + "% " + (discountBreakfastName.equals("") ? "" : "- " + discountBreakfastName) + "\n";
        result += "Total Harga : " + ClassFormatter.currencyFormat.cFormat(((roomPrice.multiply((new BigDecimal("1")).subtract(discountRoomPercentage.divide(new BigDecimal("100")))))
                .add(breakfastCost.multiply((new BigDecimal("1")).subtract(discountBreakfastPercentage.divide(new BigDecimal("100"))))))
                .multiply((new BigDecimal("1")).add(reservationController.dataReservationBill.getServiceChargePercentage()))
                .multiply((new BigDecimal("1")).add(reservationController.dataReservationBill.getTaxPercentage())));
        result += " (Service Charge & Pajak) \n";
        result += "\n";
        return result;
    }

    private void setAvailableButtonToDoubleClick(CalendarRoomTypeAvailableButton button) {
        LocalDate minDateToChange = LocalDate.now();
        if (LocalDateTime.now().isBefore(LocalDateTime.of(minDateToChange, LocalTime.of(reservationController.defaultCheckInTime.getHours(), reservationController.defaultCheckInTime.getMinutes())))) {
            minDateToChange = minDateToChange.minusDays(1);
        }
        if (button.getDate().isBefore(minDateToChange)) {
            button.setDisable(true);
            button.getStyleClass().add("button-calendar-room-type-available-not-used");
        } else {
            if ((button.getRoomAvailableNumber() - (button.getRoomReservedNumber() + button.getRoomWantToReserveNumber()))
                    <= 0) {
//                button.setDisable(true);
                button.getStyleClass().add("button-calendar-room-type-available-full");
            } else {
                button.getStyleClass().add("button-calendar-room-type-available");
            }
        }
    }

    private CalendarRoomTypeAvailableButton getCalendarRoomTypeAvailableButton(TblRoomType roomType, LocalDate date) {
        CalendarRoomTypeAvailableButton button = new CalendarRoomTypeAvailableButton("", roomType, date, 0, 0, 0,
                new BigDecimal("0"), new BigDecimal("0"), "");
        button.getStyleClass().add("button-calendar-room-type-available-not-used");
        button.setAlignment(Pos.CENTER);
        button.setOnMouseClicked((e) -> {
            e.consume();
            if (e.getClickCount() == 2) {
                dataRRTDCreateHandle(button.getButtonValue(), date, date.plusDays(1));
            }
        });
        return button;
    }

    private void refreshCalendarRoomTypeAvailableContetnt() {
        //set room type
        setRoomType();
        //set calendar - room type (available)
        setCalendarRoomTypeAvailable();
    }

    class RoomTypeButton extends JFXButton {

        private TblRoomType buttonValue;

        public RoomTypeButton(String text, TblRoomType value) {
            super(text);
            setButtonType(ButtonType.RAISED);
            buttonValue = value;
        }

        public TblRoomType getButtonValue() {
            return buttonValue;
        }

        public void setButtonValue(TblRoomType value) {
            buttonValue = value;
        }

    }

    class CalendarRoomTypeAvailableButton extends JFXButton {

        private TblRoomType buttonValue;

        private int roomReservedNumber;

        private int roomWantToReserveNumber;

        private int roomAvailableNumber;

        private BigDecimal roomTypeCurrentPrice;

        private BigDecimal roomTypeCurrentDiscountPercentage;

        private String roomTypeCurrentDiscountName;

        private LocalDate date;

        public CalendarRoomTypeAvailableButton(String text, TblRoomType value, LocalDate date,
                int roomReservedNumber, int roomWantToReserveNumber, int roomAvailableNumber,
                BigDecimal roomTypeCurrentPrice,
                BigDecimal roomTypeCurrentDiscountPercentage,
                String roomTypeCurrentDiscountName) {
            super(text);
            setButtonType(ButtonType.FLAT);
            buttonValue = value;
            this.roomReservedNumber = roomReservedNumber;
            this.roomWantToReserveNumber = roomWantToReserveNumber;
            this.roomAvailableNumber = roomAvailableNumber;
            this.roomTypeCurrentPrice = roomTypeCurrentPrice;
            this.date = date;
            this.roomTypeCurrentDiscountPercentage = roomTypeCurrentDiscountPercentage;
            this.roomTypeCurrentDiscountName = roomTypeCurrentDiscountName;
        }

        public TblRoomType getButtonValue() {
            return buttonValue;
        }

        public void setButtonValue(TblRoomType value) {
            buttonValue = value;
        }

        public int getRoomReservedNumber() {
            return roomReservedNumber;
        }

        public void setRoomReservedNumber(int roomReservedNumber) {
            this.roomReservedNumber = roomReservedNumber;
        }

        public int getRoomWantToReserveNumber() {
            return roomWantToReserveNumber;
        }

        public void setRoomWantToReserveNumber(int roomWantToReserveNumber) {
            this.roomWantToReserveNumber = roomWantToReserveNumber;
        }

        public int getRoomAvailableNumber() {
            return roomAvailableNumber;
        }

        public void setRoomAvailableNumber(int roomAvailableNumber) {
            this.roomAvailableNumber = roomAvailableNumber;
        }

        public BigDecimal getRoomTypeCurrentPrice() {
            return roomTypeCurrentPrice;
        }

        public void setRoomTypeCurrentPrice(BigDecimal currentPrice) {
            roomTypeCurrentPrice = currentPrice;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public BigDecimal getRoomTypeCurrentDiscountPercentage() {
            return roomTypeCurrentDiscountPercentage;
        }

        public void setRoomTypeCurrentDiscountPercentage(BigDecimal roomTypeCurrentDiscountPercentage) {
            this.roomTypeCurrentDiscountPercentage = roomTypeCurrentDiscountPercentage;
        }

        public String getRoomTypeCurrentDiscountName() {
            return roomTypeCurrentDiscountName;
        }

        public void setRoomTypeCurrentDiscountName(String roomTypeCurrentDiscountName) {
            this.roomTypeCurrentDiscountName = roomTypeCurrentDiscountName;
        }

    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataRRTD;

    @FXML
    private ScrollPane spFormDataRRTD;

    @FXML
    private JFXTextField txtRoomTypeName;

    @FXML
    private JFXDatePicker dtpCheckInDate;

    @FXML
    private JFXDatePicker dtpCheckOutDate;

    @FXML
    private Spinner<Integer> spnDayNumber;

    @FXML
    private Spinner<Integer> spnRoomNumber;
    
    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblRoomType selectRoomType;

    private final IntegerProperty roomNumber = new SimpleIntegerProperty(0);

    private void initFormDataRRTD() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRRTD.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRRTD.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Reservasi - Tipe Kamar)"));
        btnSave.setOnAction((e) -> {
            dataRRTDSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRRTDCancelHandle();
        });
    }

    private void setSelectedDataToInputForm(LocalDate checkInDate, LocalDate checkOutDate) {
        txtRoomTypeName.textProperty().bind(selectRoomType.roomTypeNameProperty());

        spnRoomNumber.getValueFactory().setValue(1);

        dtpCheckInDate.setValue(checkInDate);
        dtpCheckOutDate.setValue(checkOutDate);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private void dataRRTDCreateHandle(TblRoomType roomType,
            LocalDate checkInDate, LocalDate checkOutDate) {
        selectRoomType = roomType;
        setSelectedDataToInputForm(checkInDate, checkOutDate);
        //open form data bank
        dataRRTDFormShowStatus.set(1.0);
    }

    private void dataRRTDSaveHandle() {
        if (checkDataInputDataRRTD()) {
            if (checkRoomAvailable()) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
                if (alert.getResult() == ButtonType.OK) {
                    //reservation bill (selected - discount)
                    if (cbpSelectedDiscount.getValue() != null) {
                        //data bank card
                        if (cbpSelectedDiscount.getValue().getDataBankCard() != null) {
                            reservationController.dataReservationBill.setTblBankCard(new TblBankCard(cbpSelectedDiscount.getValue().getDataBankCard()));
                        } else {
                            reservationController.dataReservationBill.setTblBankCard(null);
                        }
                        //data reservation bill discount type
                        if (cbpSelectedDiscount.getValue().getDataDiscountType() != null) {
                            reservationController.dataReservationBill.setRefReservationBillDiscountType(new RefReservationBillDiscountType(cbpSelectedDiscount.getValue().getDataDiscountType()));
                        } else {
                            reservationController.dataReservationBill.setRefReservationBillDiscountType(null);
                        }
                    }
                    //reservation room type detail (selected)
                    reservationController.selectedDataRoomTypeDetails = new ArrayList<>();
                    reservationController.selectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
                    reservationController.selectedDataRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
                    reservationController.selectedDataAdditionalServices = new ArrayList<>();
                    reservationController.selectedDataAdditionalItems = new ArrayList<>();
                    //data room servce - breakfast
                    TblRoomService dataBreakfast = reservationController.getFReservationManager().getRoomService(1);    //Breakfast = '1'
                    //data reservation bill type - reservation
                    RefReservationBillType dataReservationBillTypeReservation = reservationController.getFReservationManager().getDataReservationBillType(0);    //Reservation = '0'
                    //save data reservation room type detail
                    for (int i = 0; i < roomNumber.get(); i++) {
                        //reservation room type detail
                        TblReservationRoomTypeDetail dataRRTD = new TblReservationRoomTypeDetail();
                        dataRRTD.setTblReservation(reservationController.selectedData);
                        dataRRTD.setTblRoomType(selectRoomType);
//                    dataRRTD.setCheckInDateTime(Date.valueOf(dtpCheckInDate.getValue()));
                        dataRRTD.setCheckInDateTime(Timestamp.valueOf(LocalDateTime.of(dtpCheckInDate.getValue(), reservationController.defaultCheckInTime.toLocalTime())));
//                    dataRRTD.setCheckOutDateTime(Date.valueOf(dtpCheckOutDate.getValue()));
                        dataRRTD.setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.of(dtpCheckOutDate.getValue(), reservationController.defaultCheckOutTime.toLocalTime())));
                        if (!(LocalDate.of(dataRRTD.getCheckInDateTime().getYear() + 1900,
                                dataRRTD.getCheckInDateTime().getMonth() + 1,
                                dataRRTD.getCheckInDateTime().getDate()))
                                .isAfter((LocalDate.now()).plusDays(1))) { //if check in date-time <= current date time
                            //reservation room type detail - check in/out
                            dataRRTD.setTblReservationCheckInOut(new TblReservationCheckInOut());
                            dataRRTD.getTblReservationCheckInOut().setChildNumber(0);
                            dataRRTD.getTblReservationCheckInOut().setAdultNumber(0);
                            dataRRTD.getTblReservationCheckInOut().setCardUsed(0);
                            dataRRTD.getTblReservationCheckInOut().setCardMissed(0);
                            dataRRTD.getTblReservationCheckInOut().setBrokenCardCharge(new BigDecimal("0"));
                            dataRRTD.getTblReservationCheckInOut().setCardAdditional(0);
                            dataRRTD.getTblReservationCheckInOut().setAdditionalCardCharge(new BigDecimal("0"));
                            dataRRTD.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(0)); //Ready to Check In = '0'
                        }
                        dataRRTD.setRefReservationOrderByType(reservationController.reservationType);
                        LocalDate countDate = dtpCheckInDate.getValue().minusDays(0);
                        while (countDate.isBefore(dtpCheckOutDate.getValue())) {
                            //reservation room price detail
                            TblReservationRoomPriceDetail dataRPD = new TblReservationRoomPriceDetail();
                            dataRPD.setDetailDate(Date.valueOf(countDate));
                            dataRPD.setDetailPrice(getRoomPrice(reservationController.reservationType, selectRoomType, countDate));
                            //reservation room price detail - discount
                            setReservationRoomPriceDetailDiscount(dataRPD, dataRRTD, reservationController.dataReservationBill);
                            //reservation room price detail - complimentary
                            dataRPD.setDetailComplimentary(new BigDecimal("0"));
                            //reservation room type detail - reservation room price detail
                            TblReservationRoomTypeDetailRoomPriceDetail dataRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail();
                            dataRTDRPD.setTblReservationRoomTypeDetail(dataRRTD);
                            dataRTDRPD.setTblReservationRoomPriceDetail(dataRPD);
                            //add to list (reservation room type detail - reservation room price detail)
                            reservationController.selectedDataRoomTypeDetailRoomPriceDetails.add(dataRTDRPD);
                            reservationController.dataReservationRoomTypeDetailRoomPriceDetails.add(dataRTDRPD);
                            //if reservation with travel agent
                            if (reservationController.reservationType.getIdtype() == 1) {   //Travel Agent = '1'
                                //data reservation travel agent detail
                                TblReservationTravelAgentDiscountDetail dataRTADD = new TblReservationTravelAgentDiscountDetail();
                                dataRTADD.setTblPartner(reservationController.currentTravelAgent.getTblPartner());
                                dataRTADD.setDetailDiscount(reservationController.currentTravelAgent.getRoomTypeDiscountPercentage());
                                //data reservation room type detail - reservation travel agent detail
                                TblReservationRoomTypeDetailTravelAgentDiscountDetail dataRRTDTADD = new TblReservationRoomTypeDetailTravelAgentDiscountDetail();
                                dataRRTDTADD.setTblReservationRoomTypeDetail(dataRRTD);
                                dataRRTDTADD.setTblReservationTravelAgentDiscountDetail(dataRTADD);
                                //add to list (reservation room type detail - reservation travel agent detail)
                                reservationController.selectedDataRoomTypeDetailTravelAgentDiscountDetails.add(dataRRTDTADD);
                                reservationController.dataReservationRoomTypeDetailTravelAgentDiscountDetails.add(dataRRTDTADD);
                            }
                            //if include 'breakfast'
                            if (chbUseBreakfast.isSelected()) {
                                //data additional service - breakfast
                                TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                                dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTD);
                                dataAdditionalService.setTblRoomService(new TblRoomService(dataBreakfast));
                                dataAdditionalService.setAdditionalDate(Date.valueOf(countDate));
                                dataAdditionalService.setPrice(dataAdditionalService.getTblRoomService().getPrice());
                                dataAdditionalService.setRefReservationBillType(new RefReservationBillType(dataReservationBillTypeReservation));
                                //reservation additional service (breakfast) - discount
                                setReservationAdditionalBreakfastDiscount(dataAdditionalService, reservationController.dataReservationBill);
                                //add to list (reservation additional service : breakfast)
                                reservationController.selectedDataAdditionalServices.add(dataAdditionalService);
                                reservationController.dataReservationAdditionalServices.add(dataAdditionalService);
                                reservationController.tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationAdditionalServices));
                            }
                            //data reservation additional service (include)
                            List<TblRoomTypeRoomService> dataRTRSs = reservationController.getFReservationManager().getAllDataRoomTypeRoomServiceByIDRoomType(dataRRTD.getTblRoomType().getIdroomType());
                            for (TblRoomTypeRoomService dataRTRS : dataRTRSs) {
                                if (dataRTRS.getAddAsAdditionalService()) {
                                    if (dataRTRS.getPeopleNumber() == 0) {
                                        //data additional service
                                        TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                                        dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTD);
                                        dataAdditionalService.setTblRoomService(new TblRoomService(dataRTRS.getTblRoomService()));
                                        dataAdditionalService.setAdditionalDate(Date.valueOf(countDate));
                                        dataAdditionalService.setPrice(new BigDecimal("0"));
                                        dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                        dataAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(4));  //Include = '4'
//                                        dataAdditionalService.setNote("Jumlah Orang : -");
                                        //add to list (reservation additional service : include)
                                        reservationController.selectedDataAdditionalServices.add(dataAdditionalService);
                                        reservationController.dataReservationAdditionalServices.add(dataAdditionalService);
                                        reservationController.tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationAdditionalServices));
                                    } else {
                                        for (int peopleNumber = 0; peopleNumber < dataRTRS.getPeopleNumber(); peopleNumber++) {
                                            //data additional service
                                            TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                                            dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTD);
                                            dataAdditionalService.setTblRoomService(new TblRoomService(dataRTRS.getTblRoomService()));
                                            dataAdditionalService.setAdditionalDate(Date.valueOf(countDate));
                                            dataAdditionalService.setPrice(new BigDecimal("0"));
                                            dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                            dataAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(4));  //Include = '4'
//                                            dataAdditionalService.setNote("Jumlah Orang : 1");
                                            //add to list (reservation additional service : include)
                                            reservationController.selectedDataAdditionalServices.add(dataAdditionalService);
                                            reservationController.dataReservationAdditionalServices.add(dataAdditionalService);
                                            reservationController.tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationAdditionalServices));
                                        }
                                    }
                                }
                            }
                            //data reservation additional item (include)
                            List<TblRoomTypeItem> dataRTIs = reservationController.getFReservationManager().getAllDataRoomTypeItemByIDRoomType(dataRRTD.getTblRoomType().getIdroomType());
                            for (TblRoomTypeItem dataRTI : dataRTIs) {
                                if (dataRTI.getAddAsAdditionalItem()) {
                                    //data additional item
                                    TblReservationAdditionalItem dataAddtionalItem = new TblReservationAdditionalItem();
                                    dataAddtionalItem.setTblReservationRoomTypeDetail(dataRRTD);
                                    dataAddtionalItem.setTblItem(new TblItem(dataRTI.getTblItem()));
                                    dataAddtionalItem.setAdditionalDate(Date.valueOf(countDate));
                                    dataAddtionalItem.setItemCharge(new BigDecimal("0"));
                                    dataAddtionalItem.setItemQuantity(dataRTI.getItemQuantity());
                                    dataAddtionalItem.setDiscountPercentage(new BigDecimal("0"));
                                    dataAddtionalItem.setTaxable(dataAddtionalItem.getTblItem().getTaxable());
                                    dataAddtionalItem.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(4));  //Include = '4'
                                    //add to list (reservation additional item : include)
                                    reservationController.selectedDataAdditionalItems.add(dataAddtionalItem);
                                    reservationController.dataReservationAdditionalItems.add(dataAddtionalItem);
                                    reservationController.tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationAdditionalItems));
                                }
                            }
                            //increment countDate
                            countDate = countDate.plusDays(1);
                        }
                        //add to list (reservation room type detail)
                        reservationController.selectedDataRoomTypeDetails.add(dataRRTD);
                        reservationController.dataReservationRoomTypeDetails.add(dataRRTD);
                        reservationController.tableDataReservationRoomTypeDetail.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationRoomTypeDetails));
                    }
                    //refresh reservation bill
                    reservationController.refreshDataBill("reservation");
                    if (reservationController.dataInputStatus == 1) { //update
                        //save data to database
                        reservationController.dataReservationSaveHandle(3);
                    } else {
                        //refresh button info
                        reservationController.btnReservationBillDiscountType.setText("Diskon (Reservasi) : "
                                + (reservationController.dataReservationBill.getTblBankCard() != null
                                        ? "Kartu (" + reservationController.dataReservationBill.getTblBankCard().getBankCardName() + ")"
                                        : (reservationController.dataReservationBill.getRefReservationBillDiscountType() != null
                                                ? reservationController.dataReservationBill.getRefReservationBillDiscountType().getTypeName()
                                                : "-")));
                        reservationController.btnCheckOutBillDiscountType.setText("Diskon (Check Out) : " + (reservationController.dataCheckOutBill.getTblBankCard() != null
                                ? "Kartu (" + reservationController.dataCheckOutBill.getTblBankCard().getBankCardName() + ")"
                                : (reservationController.dataCheckOutBill.getRefReservationBillDiscountType() != null
                                        ? reservationController.dataCheckOutBill.getRefReservationBillDiscountType().getTypeName()
                                        : "-")));
                        ClassMessage.showSucceedInsertingDataMessage("", reservationController.dialogStage);
                    }
                    //refresh data from calendar - room type (available) & close form data rrtd
                    chbUseBreakfast.setSelected(false);
                    refreshDataPopup();
//                    selectedDate.set(LocalDate.now());
                    refreshCalendarRoomTypeAvailableContetnt();
                    dataRRTDFormShowStatus.set(0.0);
                }
            } else {
                ClassMessage.showFailedInsertingDataMessage("Jumlah kamar tidak mencukupi..!!", reservationController.dialogStage);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private BigDecimal getRoomPrice(RefReservationOrderByType reservationType,
            TblRoomType roomType,
            LocalDate date) {
        BigDecimal result = new BigDecimal("0");
        TblReservationBar dataBAR;
        TblReservationCalendarBar calendarBAR = reservationController.getFReservationManager().getReservationCalendarBARByCalendarDate(Date.valueOf(date));
        if (calendarBAR == null) {
            TblReservationDefaultBar defaultBAR = reservationController.getFReservationManager().getReservationDefaultBARByDayOfWeek(date.getDayOfWeek().getValue());
            dataBAR = reservationController.getFReservationManager().getReservationBAR(defaultBAR.getTblReservationBar().getIdbar());
        } else {
            dataBAR = reservationController.getFReservationManager().getReservationBAR(calendarBAR.getTblReservationBar().getIdbar());
        }
        switch (reservationType.getIdtype()) {
            case 0:    //Customer = '0'
                result = roomType.getRoomTypePrice().multiply(dataBAR.getBarpercentage());
                break;
            case 1:    //Travel Agent = '1'
                result = (roomType.getRoomTypePrice().multiply(dataBAR.getBarpercentage()))
                        .multiply((new BigDecimal("1")).subtract(reservationController.currentTravelAgent.getRoomTypeDiscountPercentage().divide(new BigDecimal("100"))));
                break;
            default:
                break;
        }
        return result;
    }

    private void setReservationRoomPriceDetailDiscount(TblReservationRoomPriceDetail dataReservationRoomPriceDetail,
            TblReservationRoomTypeDetail dataRoomTypeDetail,
            TblReservationBill dataReservationBill) {
        dataReservationRoomPriceDetail.setTblHotelEvent(null);
        dataReservationRoomPriceDetail.setTblBankEventCard(null);
        dataReservationRoomPriceDetail.setDetailDiscountPercentage(new BigDecimal("0"));
        if (dataReservationBill.getRefReservationBillDiscountType() != null) {
            if (dataReservationBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataRoomTypeDetail.getTblRoomType().getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(dataReservationRoomPriceDetail.getDetailDate());
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (dataHotelEvent.getDayOfWeek() != 0) {
                                if (dataHotelEvent.getDayOfWeek() == (LocalDate.of(dataReservationRoomPriceDetail.getDetailDate().getYear() + 1900,
                                        dataReservationRoomPriceDetail.getDetailDate().getMonth() + 1,
                                        dataReservationRoomPriceDetail.getDetailDate().getDate()).getDayOfWeek().getValue())) {
                                    if (dataReservationRoomPriceDetail.getDetailPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                        if ((dataReservationRoomPriceDetail.getDetailPrice().multiply(dataHotelEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                                .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                            dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                            dataReservationRoomPriceDetail.setDetailDiscountPercentage((dataHotelEvent.getDiscountNominal().multiply(new BigDecimal("100"))).divide(dataReservationRoomPriceDetail.getDetailPrice()));
                                            break;
                                        } else {
                                            dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                            dataReservationRoomPriceDetail.setDetailDiscountPercentage(dataHotelEvent.getRoomDiscountPercentage());
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if (dataReservationRoomPriceDetail.getDetailPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                    if ((dataReservationRoomPriceDetail.getDetailPrice().multiply(dataHotelEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                        dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage((dataHotelEvent.getDiscountNominal().multiply(new BigDecimal("100"))).divide(dataReservationRoomPriceDetail.getDetailPrice()));
                                        break;
                                    } else {
                                        dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage(dataHotelEvent.getRoomDiscountPercentage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (dataReservationBill.getRefReservationBillDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    if (dataRoomTypeDetail.getTblRoomType().getCardDiscountable()) {
                        List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
                                dataReservationRoomPriceDetail.getDetailDate(),
                                dataReservationBill.getTblBankCard().getIdbankCard(),
                                dataReservationBill.getTblBankCard().getTblBank().getIdbank());
                        if (!dataCardEvents.isEmpty()) {
                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
                                if (dataReservationRoomPriceDetail.getDetailPrice().compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                    if ((dataReservationRoomPriceDetail.getDetailPrice().multiply(dataCardEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                        dataReservationRoomPriceDetail.setTblBankEventCard(dataCardEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage((dataCardEvent.getDiscountNominal().multiply(new BigDecimal("100"))).divide(dataReservationRoomPriceDetail.getDetailPrice()));
                                        break;
                                    } else {
                                        dataReservationRoomPriceDetail.setTblBankEventCard(dataCardEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage(dataCardEvent.getRoomDiscountPercentage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setReservationAdditionalBreakfastDiscount(TblReservationAdditionalService dataReservationAdditionalService,
            TblReservationBill dataReservationBill) {
        dataReservationAdditionalService.setDiscountPercentage(new BigDecimal("0"));
        if (dataReservationBill.getRefReservationBillDiscountType() != null) {
            if (dataReservationBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataReservationAdditionalService.getTblRoomService().getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxServiceDiscountPercentage(dataReservationAdditionalService.getAdditionalDate());
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (dataReservationAdditionalService.getPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                if ((dataReservationAdditionalService.getPrice().multiply(dataHotelEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                        .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                    dataReservationAdditionalService.setTblHotelEvent(dataHotelEvent);
                                    dataReservationAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
                                    break;
                                } else {
                                    dataReservationAdditionalService.setTblHotelEvent(dataHotelEvent);
                                    dataReservationAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                if (dataReservationBill.getRefReservationBillDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    if (dataReservationAdditionalService.getTblRoomService().getCardDiscountable()) {
                        List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxServiceDiscountPercentage(
                                dataReservationAdditionalService.getAdditionalDate(),
                                dataReservationBill.getTblBankCard().getIdbankCard(),
                                dataReservationBill.getTblBankCard().getTblBank().getIdbank());
                        if (!dataCardEvents.isEmpty()) {
                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
                                if (dataReservationAdditionalService.getPrice().compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                    if ((dataReservationAdditionalService.getPrice().multiply(dataCardEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                        dataReservationAdditionalService.setTblBankEventCard(dataCardEvent);
                                        dataReservationAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
                                        break;
                                    } else {
                                        dataReservationAdditionalService.setTblBankEventCard(dataCardEvent);
                                        dataReservationAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void dataRRTDCancelHandle() {
        //refresh data from calendar - room type (available) & close form data rrtd
//        selectedDate.set(LocalDate.now());
        dataRRTDFormShowStatus.set(0.0);
    }

    private String errDataInput;

    private boolean checkDataInputDataRRTD() {
        boolean dataInput = true;
        errDataInput = "";
        if (spnRoomNumber.getValue() == null) {
            dataInput = false;
            errDataInput += "Jumlah Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomNumber.get() <= 0) {
                dataInput = false;
                errDataInput += "Jumlah Kamar : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        if (dtpCheckInDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Check In : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (dtpCheckOutDate.getValue() == null) {
                dataInput = false;
                errDataInput += "Tanggal Check Out : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (!dtpCheckOutDate.getValue().isAfter(dtpCheckInDate.getValue())) {
                    dataInput = false;
                    errDataInput += "Tanggal Check In/Out : Tanggal Check In harus sebelum Tanggal Check Out \n";
                }
            }
        }
        return dataInput;
    }

    private boolean checkRoomAvailable() {
        boolean available = true;
        LocalDate dateCount = dtpCheckInDate.getValue().minusDays(0);
        while (dateCount.isBefore(dtpCheckOutDate.getValue())) {
            int availableNumber = getRoomAvailableNumber(selectRoomType, dateCount)
                    - getRoomReservedNumber(selectRoomType, dateCount)
                    - getRoomWantToReserveNumber(selectRoomType, dateCount);
            if ((availableNumber - roomNumber.get()) < 0) {
                available = false;
                break;
            }
            dateCount = dateCount.plusDays(1);
        }
        return available;
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
        setDataRRTDSplitpane();

        //init calendar - room type (available)
        initCalendarRoomTypeAvailable();

        //init form
        initFormDataRRTD();

        spDataRRTD.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRRTDFormShowStatus.set(0.0);
        });
    }

    public ReservationRoomTypeDetailInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
