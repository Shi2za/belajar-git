/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.travel_agent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCDatePicker;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class TravelAgentRoomTypeSettingInputController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataTARTS;

    private DoubleProperty dataTARTSFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane calendarDataRTAvailableLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataTARTSSplitpane() {
        spDataTARTS.setDividerPositions(1);
        
        dataTARTSFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataTARTSFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataTARTS.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataTARTS.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataTARTSFormShowStatus.addListener((obs, oldVal, newVal) -> {
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

        dataTARTSFormShowStatus.set(0.0);

    }

    /**
     * CALENDAR - ROOM TYPE (AVAILABLE) DATA
     */
    @FXML
    private AnchorPane calendarDataRTAvailableLayout;

    @FXML
    private GridPane gpHeaderLayout;

    private JFXCComboBoxTablePopup<SelectedTravelAgent> cbpSelectedTravelAgent;

    @FXML
    private JFXButton btnFastSetting;

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

    public final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();

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
    }

    private void setCalendarFunction() {
        initDataPopupHeader();
        
        initDataPopupFormInput();
        
        //fast setting
        btnFastSetting.setTooltip(new Tooltip("Pengaturan Ketersediaan Kamar Travel Agent"));
        btnFastSetting.setOnMouseClicked((e) -> {
            dataTARTSCreateHandle();
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
            selectedDate.set(LocalDate.now());
        });

        selectedDate.addListener((obs, oldVal, newVal) -> {
            refreshCalendarRoomTypeAvailableContetnt();
        });

        initDateCalendar();

        cbpSelectedTravelAgent.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshCalendarRoomTypeAvailableContetnt();
            }
        });

        setCalendarBinding();

        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                btnGoToDate,
                dtpBeginPeriodeDate,
                dtpEndPeriodeDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpBeginPeriodeDate,
                dtpEndPeriodeDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpTravelAgent,
                cbpRoomType,
                txtRoomNumber,
                dtpBeginPeriodeDate,
                dtpEndPeriodeDate);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "Integer",
                txtRoomNumber);
    }
    
    private void initDataPopupHeader() {
        //data selected travel agent
        TableView<SelectedTravelAgent> tableSelectedTravelAgent = new TableView<>();

        TableColumn<SelectedTravelAgent, String> partnerName = new TableColumn<>("Nama");
        partnerName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataTravelAgent() != null
                                ? param.getValue().getDataTravelAgent().getTblPartner().getPartnerName()
                                : "-", param.getValue().dataTravelAgentProperty()));
        partnerName.setMinWidth(140);

        tableSelectedTravelAgent.getColumns().addAll(partnerName);

        ObservableList<SelectedTravelAgent> selectedTravelAgentItems = FXCollections.observableArrayList(loadAllDataSelectedTravelAgent());

        cbpSelectedTravelAgent = new JFXCComboBoxTablePopup<>(
                SelectedTravelAgent.class, tableSelectedTravelAgent, selectedTravelAgentItems, "", "Travel Agent *", true, 200, 200
        );       

        //attached to grid-pane
        gpHeaderLayout.add(cbpSelectedTravelAgent, 0, 0);

        //refresh data popup
        refreshDataPopupHeader();
    }

    private List<SelectedTravelAgent> loadAllDataSelectedTravelAgent() {
        List<SelectedTravelAgent> list = new ArrayList<>();
        //data null
        SelectedTravelAgent dataNullTravelAgent = new SelectedTravelAgent(null);
        list.add(dataNullTravelAgent);
        //data travel agent
        List<TblTravelAgent> dataTravelAgents = travelAgentController.getService().getAllDataTravelAgent();
        for (TblTravelAgent dataTravelAgent : dataTravelAgents) {
            //data partner
            dataTravelAgent.setTblPartner(travelAgentController.getService().getPartner(dataTravelAgent.getTblPartner().getIdpartner()));
            //data selected travel agent
            SelectedTravelAgent dataSelectedTravelAgent = new SelectedTravelAgent(dataTravelAgent);
            list.add(dataSelectedTravelAgent);
        }
        return list;
    }

    private void refreshDataPopupHeader() {
        //Selected Travel Agent
        ObservableList<SelectedTravelAgent> selectedTravelAgentItems = FXCollections.observableArrayList(loadAllDataSelectedTravelAgent());
        cbpSelectedTravelAgent.setItems(selectedTravelAgentItems);

        //set selected travel agent value
        cbpSelectedTravelAgent.setValue(new SelectedTravelAgent(null));

    }

    public class SelectedTravelAgent {

        private final ObjectProperty<TblTravelAgent> dataTravelAgent = new SimpleObjectProperty<>();

        public SelectedTravelAgent(TblTravelAgent dataTravelAgent) {

            this.dataTravelAgent.set(dataTravelAgent);

        }

        public ObjectProperty<TblTravelAgent> dataTravelAgentProperty() {
            return dataTravelAgent;
        }

        public TblTravelAgent getDataTravelAgent() {
            return dataTravelAgentProperty().get();
        }

        public void setDataTravelAgent(TblTravelAgent dataTravelAgent) {
            dataTravelAgentProperty().set(dataTravelAgent);
        }

        @Override
        public String toString() {
            if (getDataTravelAgent() != null) {
                return getDataTravelAgent().getTblPartner().getPartnerName();
            }
            return "-";
        }

    }

    private void initDataPopupFormInput() {
        //data travel agent
        TableView<TblTravelAgent> tableTravelAgent = new TableView<>();

        TableColumn<TblTravelAgent, String> partnerName = new TableColumn<>("Travel Agent");
        partnerName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPartnerName(), param.getValue().tblPartnerProperty()));
        partnerName.setMinWidth(140);

        tableTravelAgent.getColumns().addAll(partnerName);

        ObservableList<TblTravelAgent> travelAgentItems = FXCollections.observableArrayList(loadAllDataTravelAgent());
        
        cbpTravelAgent = new JFXCComboBoxTablePopup<>(
                TblTravelAgent.class, tableTravelAgent, travelAgentItems, "", "Travel Agent *", true, 200, 200
        );

        //data room type
        TableView<TblRoomType> tableRoomType = new TableView<>();

        TableColumn<TblRoomType, String> roomTypeName = new TableColumn<>("Tipe Kamar");
        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomType, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRoomTypeName(), param.getValue().roomTypeNameProperty()));
        roomTypeName.setMinWidth(140);

        tableRoomType.getColumns().addAll(roomTypeName);

        ObservableList<TblRoomType> roomtypeItems = FXCollections.observableArrayList(loadAllDataRoomType());
        
        cbpRoomType = new JFXCComboBoxTablePopup<>(
                TblRoomType.class, tableRoomType, roomtypeItems, "", "Tipe Kamar *", true, 200, 200
        );

        //attached to grid-pane
        gpFormDataTARTS.add(cbpTravelAgent, 1, 1);
        gpFormDataTARTS.add(cbpRoomType, 1, 2);
    }

    private List<TblTravelAgent> loadAllDataTravelAgent() {
        List<TblTravelAgent> list = travelAgentController.getService().getAllDataTravelAgent();
        for (TblTravelAgent data : list) {
            //data partner
            data.setTblPartner(travelAgentController.getService().getPartner(data.getTblPartner().getIdpartner()));
        }
        return list;
    }

    private List<TblRoomType> loadAllDataRoomType() {
        List<TblRoomType> list = travelAgentController.getService().getAllDataRoomType();
        return list;
    }

    private void refreshDataPopupFormInput() {
        //Travel Agent
        ObservableList<TblTravelAgent> travelAgentItems = FXCollections.observableArrayList(loadAllDataTravelAgent());
        cbpTravelAgent.setItems(travelAgentItems);

        //Room Type
        ObservableList<TblRoomType> roomTypeItems = FXCollections.observableArrayList(loadAllDataRoomType());
        cbpRoomType.setItems(roomTypeItems);
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
        roomTypes = FXCollections.observableArrayList(loadAllDataRoomType());
        gpRoomTypes.getChildren().clear();
        gpRoomTypes.setVgap(5.0);
        for (int i = 0; i < roomTypes.size(); i++) {
            gpRoomTypes.getRowConstraints().add(new RowConstraints());
            RoomTypeButton button = getRoomTypeButton(roomTypes.get(i));
            button.setPrefSize(100, 80);
            gpRoomTypes.add(button, 0, i);
        }
    }

    private RoomTypeButton getRoomTypeButton(TblRoomType roomType) {
        RoomTypeButton button = new RoomTypeButton(roomType.getRoomTypeName(), roomType);
        button.getStyleClass().add("button-room-type");
        button.setAlignment(Pos.CENTER);
        button.setOnMouseClicked((e) -> {
            e.consume();
            if (e.getClickCount() == 2) {
//                dataTARTSDCreateHandle(button);
            }
        });
        return button;
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
                    CalendarRoomTypeAvailableButton button = getCalendarRoomTypeAvailableButtonNotUsed(roomTypes.get(j), null);
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
                        CalendarRoomTypeAvailableButton button = getCalendarRoomTypeAvailableButton(roomTypes.get(j),
                                dateInPosition);
                        button.prefWidthProperty().bind(Bindings.createDoubleBinding(
                                () -> gpCalendars.getWidth() / 8.0,
                                gpCalendars.widthProperty()));
                        button.setPrefHeight(80);
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

    public int getRoomReservedNumber(TblRoomType roomType, LocalDate date, TblTravelAgent travelAgent) {
        int result = 0;
        if (travelAgent == null) {  //Customer
            List<TblReservationRoomPriceDetail> roomPriceDetails = travelAgentController.getService().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail relation = travelAgentController.getService().getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetail roomTypeDetail = travelAgentController.getService().getDataReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                roomTypeDetail.setTblReservation(travelAgentController.getService().getDataReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                roomTypeDetail.getTblReservation().setRefReservationStatus(travelAgentController.getService().getDataReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
                if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                        && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 0 //Customer = '0'
                        && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                    result++;
                }
            }
        } else {  //Travel Agent
            List<TblReservationRoomPriceDetail> roomPriceDetails = travelAgentController.getService().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail relation = travelAgentController.getService().getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetail roomTypeDetail = travelAgentController.getService().getDataReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                roomTypeDetail.setTblReservation(travelAgentController.getService().getDataReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                roomTypeDetail.getTblReservation().setRefReservationStatus(travelAgentController.getService().getDataReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
                if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                        && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 1 //Travel Agent = '1'
                        && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                    TblReservationRoomTypeDetailTravelAgentDiscountDetail relation1 = travelAgentController.getService().getDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(roomTypeDetail.getIddetail());
                    TblReservationTravelAgentDiscountDetail travelAgentDiscountDetail = travelAgentController.getService().getDataReservationTravelAgentDiscountDetail(relation1.getTblReservationTravelAgentDiscountDetail().getIddetail());
                    if (travelAgent.getTblPartner().getIdpartner() == travelAgentDiscountDetail.getTblPartner().getIdpartner()) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public int getRoomAvailableNumber(TblRoomType roomType, LocalDate date, TblTravelAgent travelAgent) {
        int result = 0;
        if (travelAgent == null) {   //Customer
            List<TblRoom> listRoom = travelAgentController.getService().getAllDataRoomByIDRoomType(roomType.getIdroomType());
            for (int i = listRoom.size() - 1; i > -1; i--) {
                if (listRoom.get(i).getRefRoomStatus().getIdstatus() == 6) {  //Out Of Service = '6'
                    listRoom.remove(i);
                }
            }
            result = listRoom.size();
            List<TblTravelAgentRoomType> travelAgentRoomTypes = travelAgentController.getService().getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(roomType.getIdroomType(),
                    Date.valueOf(date));
            for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
                result -= travelAgentRoomType.getRoomNumber();
            }
        } else {  //Travel Agent
            TblTravelAgentRoomType travelAgentRoomType = travelAgentController.getService().getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(roomType.getIdroomType(),
                    travelAgent.getTblPartner().getIdpartner(),
                    Date.valueOf(date));
            if (travelAgentRoomType != null) {
                result = travelAgentRoomType.getRoomNumber();
            }
        }
        return result;
    }

    private CalendarRoomTypeAvailableButton getCalendarRoomTypeAvailableButton(TblRoomType roomType, LocalDate date) {
        CalendarRoomTypeAvailableButton button = new CalendarRoomTypeAvailableButton(
                getDataInfo(roomType, date),
                roomType, date);
        button.getStyleClass().add("button-calendar-room-type-available");
        button.setAlignment(Pos.CENTER);
        button.setOnMouseClicked((e) -> {
            e.consume();
            if (e.getClickCount() == 2) {
                dataTARTSDCreateHandle(button);
            }
        });
        button.setWrapText(true);
//        button.setTooltip(new Tooltip(getDatasHoverInfo(roomType, date)));
        return button;
    }

    private String getDataInfo(TblRoomType roomType, LocalDate date) {
        String result = "";
        if (cbpSelectedTravelAgent.getValue() == null
                || cbpSelectedTravelAgent.getValue().getDataTravelAgent() == null) {
            result = "Dipesan : " + getRoomReservedNumber(roomType, date, null) + " \n Ketersediaan :  " + getRoomAvailableNumber(roomType, date, null) + "\n";
        } else {
            result = "Dipesan : " + getRoomReservedNumber(roomType, date, cbpSelectedTravelAgent.getValue().getDataTravelAgent())
                    + " \n Ketersediaan :  " + getRoomAvailableNumber(roomType, date, cbpSelectedTravelAgent.getValue().getDataTravelAgent()) + " \n";
        }
        return result;
    }

//    private String getDataHoverInfo(TblRoomType roomType, LocalDate date) {
//        String result;
//        result = "Total Kamar : " + roomAvailableNumber + " \n";
//        result += "\n";
//        return result;
//    }
    private CalendarRoomTypeAvailableButton getCalendarRoomTypeAvailableButtonNotUsed(TblRoomType roomType, LocalDate date) {
        CalendarRoomTypeAvailableButton button = new CalendarRoomTypeAvailableButton("", roomType, date);
        button.getStyleClass().add("button-calendar-room-type-available-not-used");
        button.setAlignment(Pos.CENTER);
        button.setOnMouseClicked((e) -> {
            e.consume();
            if (e.getClickCount() == 2) {
//                dataTARTSDCreateHandle(button);
            }
        });
        return button;
    }

    public void refreshCalendarRoomTypeAvailableContetnt() {
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

    public class CalendarRoomTypeAvailableButton extends JFXButton {

        private TblRoomType buttonValue;

        private LocalDate date;

        public CalendarRoomTypeAvailableButton(String text, TblRoomType value, LocalDate date) {
            super(text);
            setButtonType(ButtonType.FLAT);
            buttonValue = value;
            this.date = date;
        }

        public TblRoomType getButtonValue() {
            return buttonValue;
        }

        public void setButtonValue(TblRoomType value) {
            buttonValue = value;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataTARTS;

    @FXML
    private ScrollPane spFormDataTARTS;

    private JFXCComboBoxTablePopup<TblTravelAgent> cbpTravelAgent;

    private JFXCComboBoxTablePopup<TblRoomType> cbpRoomType;

    @FXML
    private JFXTextField txtRoomNumber;

    @FXML
    private JFXDatePicker dtpBeginPeriodeDate;

    @FXML
    private JFXDatePicker dtpEndPeriodeDate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private final IntegerProperty roomNumber = new SimpleIntegerProperty();

    private void initFormDataTARTS() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataTARTS.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataTARTS.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Travel Agent - Ketersediaan Jumlah Tipe Kamar)"));
        btnSave.setOnAction((e) -> {
            dataTARTSSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataTARTSCancelHandle();
        });
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopupFormInput();

        roomNumber.set(0);
        Bindings.bindBidirectional(txtRoomNumber.textProperty(), roomNumber, new NumberStringConverter());

        dtpBeginPeriodeDate.setValue(null);
        dtpEndPeriodeDate.setValue(null);

        cbpTravelAgent.setValue(null);
        cbpRoomType.setValue(null);

    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private void dataTARTSCreateHandle() {
        setSelectedDataToInputForm();
        //open form data bank
        dataTARTSFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataTARTSSaveHandle() {
        if (checkDataInputDataTARTS()) {
            if (checkRoomAvailable()) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    //dummy entry
                    List<TblTravelAgentRoomType> dummyTravelAgentRoomTypes = new ArrayList<>();
                    LocalDate dateCount = dtpBeginPeriodeDate.getValue().minusDays(0);
                    while (dateCount.isBefore(dtpEndPeriodeDate.getValue())) {
                        TblTravelAgentRoomType dummyTravelAgentRoomType = new TblTravelAgentRoomType();
                        dummyTravelAgentRoomType.setTblRoomType(new TblRoomType(cbpRoomType.getValue()));
                        dummyTravelAgentRoomType.setTblPartner(new TblPartner(cbpTravelAgent.getValue().getTblPartner()));
                        dummyTravelAgentRoomType.setAvailableDate(Date.valueOf(dateCount));
                        dummyTravelAgentRoomType.setRoomNumber(roomNumber.get());
                        dummyTravelAgentRoomTypes.add(dummyTravelAgentRoomType);
                        dateCount = dateCount.plusDays(1);
                    }
                    if (travelAgentController.getService().updateDataTravelAgentRoomType(dummyTravelAgentRoomTypes)) {
                        selectedDate.set(LocalDate.now());
                        refreshCalendarRoomTypeAvailableContetnt();
                        dataTARTSFormShowStatus.set(0.0);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                    } else {
                        ClassMessage.showFailedInsertingDataMessage(travelAgentController.getService().getErrorMessage(), null);
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage(errDataInput, null);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataTARTSCancelHandle() {
        //refresh data from calendar - room type (available) & close form data tarts
        selectedDate.set(LocalDate.now());
        refreshCalendarRoomTypeAvailableContetnt();
        dataTARTSFormShowStatus.set(0.0);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private String errDataInput;

    private boolean checkDataInputDataTARTS() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpTravelAgent.getValue() == null) {
            dataInput = false;
            errDataInput += "Travel Agent : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpRoomType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomNumber.getText() == null 
                || txtRoomNumber.getText().equals("")
                || txtRoomNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomNumber.get() < 0) {
                dataInput = false;
                errDataInput += "Jumlah Kamar : Tidak dapat kurang dari '0' \n";
            }
        }
        if (dtpBeginPeriodeDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Awal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (dtpEndPeriodeDate.getValue() == null) {
                dataInput = false;
                errDataInput += "Tanggal Akhir : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (!dtpEndPeriodeDate.getValue().isAfter(dtpBeginPeriodeDate.getValue())) {
                    dataInput = false;
                    errDataInput += "Tanggal Awal/Akhir : Tanggal Awal harus sebelum Tanggal Akhir \n";
                }
            }
        }
        return dataInput;
    }

    private boolean checkRoomAvailable() {
        boolean available = true;
        errDataInput = "";
        LocalDate dateCount = dtpBeginPeriodeDate.getValue().minusDays(0);
        while (dateCount.isBefore(dtpEndPeriodeDate.getValue())) {
            int availableMinNumber = getRoomReservedNumber(cbpRoomType.getValue(), dateCount, cbpTravelAgent.getValue());
            int availableMaxNumber = getRoomAvailableNumber(cbpRoomType.getValue(), dateCount, cbpTravelAgent.getValue())
                    + (getRoomAvailableNumber(cbpRoomType.getValue(), dateCount, null)
                    - getRoomReservedNumber(cbpRoomType.getValue(), dateCount, null));
            if (roomNumber.get() < availableMinNumber) {
                errDataInput = "Minimal Jumlah Kamar = '" + availableMinNumber + "'";
                available = false;
                break;
            }
            if (roomNumber.get() > availableMaxNumber) {
                errDataInput = "Maksimal Jumlah Kamar = '" + availableMaxNumber + "'";
                available = false;
                break;
            }
            dateCount = dateCount.plusDays(1);
        }
        return available;
    }

    /**
     * DATA DETAIL (TRAVEL AGENT - ROOM TYPE - DATE)
     */
    public CalendarRoomTypeAvailableButton selectedDataCalendarRoomTypeAvailableButton;

    public Stage dialogStage;

    private void dataTARTSDCreateHandle(CalendarRoomTypeAvailableButton button) {
        selectedDataCalendarRoomTypeAvailableButton = button;
        //open form data travel agent - room type - date (detail)
        showTravelAgentRoomTypeDetailInputDialog();
    }

    private void showTravelAgentRoomTypeDetailInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/travel_agent/TravelAgentRoomTypeSettingDetailInputDialog.fxml"));

            TravelAgentRoomTypeSettingDetailInputController controller = new TravelAgentRoomTypeSettingDetailInputController(this);
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataTARTSSplitpane();

        //init calendar - room type (available)
        initCalendarRoomTypeAvailable();

        //init form
        initFormDataTARTS();

        spDataTARTS.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataTARTSFormShowStatus.set(0.0);
        });
    }

    public TravelAgentRoomTypeSettingInputController(TravelAgentController travelAgentController) {
        this.travelAgentController = travelAgentController;
    }

    private final TravelAgentController travelAgentController;

    public TravelAgentController getParentController() {
        return travelAgentController;
    }

}
