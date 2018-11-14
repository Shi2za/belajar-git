/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailChildDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomCheck;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.service.FReservationV2Manager;
import hotelfx.persistence.service.FReservationV2ManagerImpl;
import hotelfx.persistence.service.FRoomCheckManager;
import hotelfx.persistence.service.FRoomCheckManagerImpl;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
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
public class RoomStatusFDController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRoomStatus;

    private DoubleProperty dataRoomStatusFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRoomStatusLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataRoomStatusSplitpane() {
        spDataRoomStatus.setDividerPositions(1);

        dataRoomStatusFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRoomStatusFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRoomStatus.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRoomStatus.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRoomStatusFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 0.0) {    //enable
                tableDataRoomStatusLayout.setDisable(false);
                tableDataRoomStatusLayoutDisableLayer.setDisable(true);
                tableDataRoomStatusLayout.toFront();
            }
            if (newVal.doubleValue() == 1) {  //disable
                tableDataRoomStatusLayout.setDisable(true);
                tableDataRoomStatusLayoutDisableLayer.setDisable(false);
                tableDataRoomStatusLayoutDisableLayer.toFront();
            }
        });

        dataRoomStatusFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataRoomStatusLayout;

    private ClassFilteringTable<DataRoomStatus> cft;

    private String currentTextFilter = "";

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRoomStatus;

    private void initTableDataRoomStatus() {
        //set table
        setTableDataRoomStatus();
        //set control
        setTableControlDataRoomStatus();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoomStatus, 15.0);
        AnchorPane.setLeftAnchor(tableDataRoomStatus, 15.0);
        AnchorPane.setRightAnchor(tableDataRoomStatus, 15.0);
        AnchorPane.setTopAnchor(tableDataRoomStatus, 15.0);
        ancBodyLayout.getChildren().add(tableDataRoomStatus);
    }

    private void setTableDataRoomStatus() {
        TableView<DataRoomStatus> tableView = new TableView();

        LocalDate previousDate = (LocalDate.now()).minusDays(1);
        LocalDate nextDate = LocalDate.now();

        TableColumn<DataRoomStatus, String> bulidingFloorName = new TableColumn("Bagunan/\nLantai");
        bulidingFloorName.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRoom().getTblLocation().getTblBuilding().getBuildingName() + " / "
                        + param.getValue().getDataRoom().getTblLocation().getTblFloor().getFloorName(),
                        param.getValue().dataRoomProperty()));
        bulidingFloorName.setMinWidth(100);

        TableColumn<DataRoomStatus, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRoom().getRoomName()
                        + "\n"
                        + param.getValue().getDataRoom().getTblRoomType().getRoomTypeName(),
                        param.getValue().dataRoomProperty()));
        roomName.setMinWidth(100);

        TableColumn<DataRoomStatus, String> previousCustomerName = new TableColumn<>("Customer");
        previousCustomerName.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getStatusAndCustomerName(param.getValue().getDataPreviousRRTD()),
                        param.getValue().dataPreviousRRTDProperty()));
        previousCustomerName.setMinWidth(140);

        TableColumn<DataRoomStatus, String> previousArrivalDate = new TableColumn<>("Datang");
        previousArrivalDate.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getArrivalDate(param.getValue().getDataPreviousRRTD()),
                        param.getValue().dataPreviousRRTDProperty()));
        previousArrivalDate.setMinWidth(120);

        TableColumn<DataRoomStatus, String> previousDepartureDate = new TableColumn<>("Pergi");
        previousDepartureDate.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getDepartureDate(param.getValue().getDataPreviousRRTD()),
                        param.getValue().dataPreviousRRTDProperty()));
        previousDepartureDate.setMinWidth(120);

        TableColumn<DataRoomStatus, String> previousDateTitle = new TableColumn("Tanggal");
        previousDateTitle.getColumns().addAll(previousArrivalDate, previousDepartureDate);

        TableColumn<DataRoomStatus, String> previousTitle = new TableColumn(ClassFormatter.dateFormate.format(Date.valueOf(previousDate)));
        previousTitle.getColumns().addAll(previousCustomerName, previousDateTitle);

        TableColumn<DataRoomStatus, String> nextCustomerName = new TableColumn<>("Customer");
        nextCustomerName.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getStatusAndCustomerName(param.getValue().getDataNextRRTD()),
                        param.getValue().dataNextRRTDProperty()));
        nextCustomerName.setMinWidth(140);

        TableColumn<DataRoomStatus, String> nextArrivalDate = new TableColumn<>("Datang");
        nextArrivalDate.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getArrivalDate(param.getValue().getDataNextRRTD()),
                        param.getValue().dataNextRRTDProperty()));
        nextArrivalDate.setMinWidth(120);

        TableColumn<DataRoomStatus, String> nextDepartureDate = new TableColumn<>("Pergi");
        nextDepartureDate.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getDepartureDate(param.getValue().getDataNextRRTD()),
                        param.getValue().dataNextRRTDProperty()));
        nextDepartureDate.setMinWidth(120);

        TableColumn<DataRoomStatus, String> nextDateTitle = new TableColumn("Tanggal");
        nextDateTitle.getColumns().addAll(nextArrivalDate, nextDepartureDate);

        TableColumn<DataRoomStatus, String> nextTitle = new TableColumn(ClassFormatter.dateFormate.format(Date.valueOf(nextDate)));
        nextTitle.getColumns().addAll(nextCustomerName, nextDateTitle);

        TableColumn<DataRoomStatus, String> roomStatus = new TableColumn<>("Status \nKamar");
        roomStatus.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRoom().getRefRoomStatus().getStatusName(),
                        param.getValue().dataRoomProperty()));
        roomStatus.setMinWidth(100);

        TableColumn<DataRoomStatus, String> roomStatusDetail = new TableColumn<>("Status Kamar\n (Detail)");
        roomStatusDetail.setCellValueFactory((TableColumn.CellDataFeatures<DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRoom().getRoomStatusNote(),
                        param.getValue().dataRoomProperty()));
        roomStatusDetail.setMinWidth(100);

        tableView.getColumns().addAll(bulidingFloorName, roomName, previousTitle, nextTitle, roomStatus, roomStatusDetail);

        tableView.setItems(loadAllDataRoomWithStatus());

        tableDataRoomStatus = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                DataRoomStatus.class,
                tableDataRoomStatus.getTableView(),
                tableDataRoomStatus.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getStatusAndCustomerName(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return "(" + dataRRTD.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName() + ")\n"
                    + dataRRTD.getTblReservation().getTblCustomer().getTblPeople().getFullName() + " -\n"
                    + dataRRTD.getTblReservation().getCodeReservation();
        }
        return "";
    }

    private String getArrivalDate(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return "\n" + ClassFormatter.dateFormate.format(dataRRTD.getCheckInDateTime());
        }
        return "";
    }

    private String getDepartureDate(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return "\n" + ClassFormatter.dateFormate.format(dataRRTD.getCheckOutDateTime());
        }
        return "";
    }

    public class DataRoomStatus {

        private final ObjectProperty<TblRoom> dataRoom = new SimpleObjectProperty<>();

        private final ObjectProperty<LocalDate> previousDate = new SimpleObjectProperty<>();

        private final ObjectProperty<LocalDate> nextDate = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationRoomTypeDetail> dataPreviousRRTD = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationRoomTypeDetail> dataNextRRTD = new SimpleObjectProperty<>();

        public DataRoomStatus(
                TblRoom dataRoom,
                LocalDate previousDate,
                LocalDate nextDate) {

            this.dataRoom.set(dataRoom);
            this.previousDate.set(previousDate);
            this.nextDate.set(nextDate);

            this.dataRoom.addListener((obs, oldVal, newVal) -> {
                setDataRRTD();
            });
            this.previousDate.addListener((obs, oldVal, newVal) -> {
                setDataRRTD();
            });
            this.nextDate.addListener((obs, oldVal, newVal) -> {
                setDataRRTD();
            });

            setDataRRTD();
        }

        private void setDataRRTD() {
            //data prevoius rrtd
            setDataPreviousRRTD(getPreviousRRTD(getDataRoom(), Date.valueOf(getPreviousDate())));
            //data next rrtd
            setDataNextRRTD(getNextRRTD(getDataRoom(), Date.valueOf(getNextDate())));
        }

        public ObjectProperty<TblRoom> dataRoomProperty() {
            return dataRoom;
        }

        public TblRoom getDataRoom() {
            return dataRoomProperty().get();
        }

        public void setDataRoom(TblRoom dataRoom) {
            dataRoomProperty().set(dataRoom);
        }

        public ObjectProperty<LocalDate> previousDateProperty() {
            return previousDate;
        }

        public LocalDate getPreviousDate() {
            return previousDateProperty().get();
        }

        public void setPreviousDate(LocalDate previousDate) {
            previousDateProperty().set(previousDate);
        }

        public ObjectProperty<LocalDate> nextDateProperty() {
            return nextDate;
        }

        public LocalDate getNextDate() {
            return nextDateProperty().get();
        }

        public void setNextDate(LocalDate nextDate) {
            nextDateProperty().set(nextDate);
        }

        public ObjectProperty<TblReservationRoomTypeDetail> dataPreviousRRTDProperty() {
            return dataPreviousRRTD;
        }

        public TblReservationRoomTypeDetail getDataPreviousRRTD() {
            return dataPreviousRRTDProperty().get();
        }

        public void setDataPreviousRRTD(TblReservationRoomTypeDetail dataPreviousRRTD) {
            dataPreviousRRTDProperty().set(dataPreviousRRTD);
        }

        public ObjectProperty<TblReservationRoomTypeDetail> dataNextRRTDProperty() {
            return dataNextRRTD;
        }

        public TblReservationRoomTypeDetail getDataNextRRTD() {
            return dataNextRRTDProperty().get();
        }

        public void setDataNextRRTD(TblReservationRoomTypeDetail dataNextRRTD) {
            dataNextRRTDProperty().set(dataNextRRTD);
        }
    }

    public TblReservationRoomTypeDetail getPreviousRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = getService().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = getService().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = getService().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(getService().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(getService().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
                            //data reservation
                            rrtd.setTblReservation(getService().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(getService().getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(getService().getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
                                //retuurn rrtd
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public TblReservationRoomTypeDetail getNextRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = getService().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = getService().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = getService().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(getService().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(getService().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
                            //data reservation
                            rrtd.setTblReservation(getService().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(getService().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(getService().getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(getService().getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
                                //retuurn rrtd
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void setTableControlDataRoomStatus() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("CheckIn");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusCheckInHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah Kartu Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusAddCardNumberHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Pindah Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusChangeRoomHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Extend Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusExtendRoomHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("CheckOut");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusCheckOutHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Barang & Layanan");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusReservationAdditionalItemAndServiceHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tagihan Resto");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusCreateRestoAdditionalServiceHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tagihan & Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusReservationBillAndPaymentHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah Status Detail Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //set last filter
                currentTextFilter = cft != null ? cft.getTextFilter() : "";
                //listener
                dataRoomStatusChangeStatusDetailHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataRoomStatus.addButtonControl(buttonControls);
    }

    public ObservableList<DataRoomStatus> loadAllDataRoomWithStatus() {
        List<DataRoomStatus> list = new ArrayList<>();
        LocalDate previousDate = (LocalDate.now()).minusDays(1);
        LocalDate nextDate = LocalDate.now();
        List<TblRoom> rooms = getService().getAllDataRoom();
        for (TblRoom room : rooms) {
            //data location
            room.setTblLocation(getService().getDataLocation(room.getTblLocation().getIdlocation()));
            //data building
            room.getTblLocation().setTblBuilding(getService().getDataBuilding(room.getTblLocation().getTblBuilding().getIdbuilding()));
            //data floor
            room.getTblLocation().setTblFloor(getService().getDataFloor(room.getTblLocation().getTblFloor().getIdfloor()));
            //data room type
            room.setTblRoomType(getService().getDataRoomType(room.getTblRoomType().getIdroomType()));
            //data room status
            room.setRefRoomStatus(getService().getDataRoomStatus(room.getRefRoomStatus().getIdstatus()));
            //data room status
            DataRoomStatus data = new DataRoomStatus(room, previousDate, nextDate);
            list.add(data);
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataRoomCheck;

    @FXML
    private ScrollPane spFormDataRoomCheck;

    private final JFXCComboBoxPopup<RefRoomStatus> cbpRoomStatusHeader = new JFXCComboBoxPopup<>(RefRoomStatus.class);

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblRoomCheck selectedData;

    public List<TblItemMutationHistoryPropertyBarcode> itemMutationHistoryPropertyBarcodes = new ArrayList<>();

    public List<TblItemMutationHistoryItemExpiredDate> itemMutationHistoryItemExpiredDates = new ArrayList<>();

    private void initFormDataRoomCheck() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRoomCheck.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRoomCheck.setOnScroll((ScrollEvent scroll) -> {
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

    }

    private void initDataPopup() {
        //Room Status - Header
        TableView<RefRoomStatus> tableRoomStatusHeader = new TableView<>();

        TableColumn<RefRoomStatus, String> roomStatusNameHeader = new TableColumn<>("Status");
        roomStatusNameHeader.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        roomStatusNameHeader.setMinWidth(140);

        TableColumn<RefRoomStatus, String> roomStatusNoteHeader = new TableColumn<>("Keterangan");
        roomStatusNoteHeader.setCellValueFactory(cellData -> cellData.getValue().statusNoteProperty());
        roomStatusNoteHeader.setMinWidth(180);

        TableColumn<RefRoomStatus, String> roomCount = new TableColumn<>("Total Kamar");
        roomCount.setCellValueFactory((TableColumn.CellDataFeatures<RefRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getRoomCount(param.getValue()),
                        param.getValue().idstatusProperty()));
        roomCount.setMinWidth(120);

        tableRoomStatusHeader.getColumns().addAll(roomStatusNameHeader, roomStatusNoteHeader, roomCount);

        ObservableList<RefRoomStatus> roomStatusHeaderItems = FXCollections.observableArrayList(loadAllDataRoomStatusHeader());

        setFunctionPopupRoomStatusHeader(cbpRoomStatusHeader, tableRoomStatusHeader, roomStatusHeaderItems, "", "", 465, 200);

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpRoomStatusHeader, 10.0);
        AnchorPane.setLeftAnchor(cbpRoomStatusHeader, 15.0);
//        AnchorPane.setRightAnchor(cbpRoomStatusHeader, 10.0);
        AnchorPane.setTopAnchor(cbpRoomStatusHeader, 10.0);
        ancHeaderLayout.getChildren().add(cbpRoomStatusHeader);
    }

    private String getRoomCount(RefRoomStatus dataRoomStatus) {
        if (dataRoomStatus != null) {
            List<TblRoom> rooms = getService().getAllDataRoomByIDRoomStatus(dataRoomStatus.getIdstatus());
            return rooms.size() + " Kamar";
        }
        return "";
    }

    private ObservableList<RefRoomStatus> loadAllDataRoomStatusHeader() {
        List<RefRoomStatus> list = getService().getAllDataRoomStatus();
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Room Status - Header
        ObservableList<RefRoomStatus> roomStatusHeaderItems = FXCollections.observableArrayList(loadAllDataRoomStatusHeader());
        cbpRoomStatusHeader.setItems(roomStatusHeaderItems);
    }

    private void setFunctionPopupRoomStatusHeader(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText,
            double prefWidth,
            double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != -1) {
                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
                    }
                    cbp.hide();
                });

        cbp.setPropertyNameForFiltered(nameFiltered);

        cbp.setItems(items);

        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton("Infomasi Status Kamar");
        button.setOnMouseClicked((e) -> cbp.show());
        button.setPrefSize(200, 30);

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth, prefHeight);

        content.setCenter(table);

        cbp.setPopupEditor(false);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(true);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);

        cbp.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

    }

    public void setSelectedDataToInputForm() {
        //refresh data popup
        refreshDataPopup();
//        ((JFXButton)cbpRoomStatusHeader.getPopupButton()).setText("Tingkat Hunian Kamar : " + getRoomOccupancy());
        //refresh data table
        refreshDataTableRoomStatus();
    }

    private String getRoomOccupancy() {
        List<TblRoom> rooms = getService().getAllDataRoom();
        BigDecimal occupiedNumber = new BigDecimal("0");
        for (TblRoom room : rooms) {
            if (room.getRefRoomStatus().getIdstatus() == 1 //Occupied Clean = '1'
                    || room.getRefRoomStatus().getIdstatus() == 2) { //Occupied Dirty = '2'
                occupiedNumber = occupiedNumber.add(new BigDecimal("1"));
            }
        }
//        return occupiedNumber.compareTo(new BigDecimal("0")) == 0 
//                || rooms.isEmpty()
//                ? "0%" 
//                : ((occupiedNumber.divide(new BigDecimal(String.valueOf(rooms.size())))).setScale(0, RoundingMode.UP)) + "%";
        return "5%";
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputStatus = 0;

    public Stage dialogStage;

    public Time defaultCheckInTime;

    public Time defaultCheckOutTime;

    public DataRoomStatus selectedDataRoomStatus;
    
    public TblRoom selectedDataRoom;

    public TblReservationRoomTypeDetail selectedDataRoomTypeDetail;

    public TblReservationCheckInOut selectedDataCheckIn;

    public List<TblReservationRoomTypeDetailChildDetail> selectedChildDetails;

    public TblReservationAdditionalService selectedAdditionalService;

    private void dataRoomStatusCheckInHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (selectedDataRoomStatus.getDataPreviousRRTD() != null
                    || selectedDataRoomStatus.getDataNextRRTD() != null) {
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                if (selectedDataRoomTypeDetail.getTblReservationCheckInOut() == null) {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-IN",
                            "@_@..!", null);
                } else {
                    if (selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {    //Ready to Check In = '0'
                        selectedDataCheckIn = getServiceRV2().getReservationCheckInOut(selectedDataRoomTypeDetail.getTblReservationCheckInOut().getIdcheckInOut());
                        if (selectedDataCheckIn.getTblRoom() != null) {
                            selectedDataCheckIn.setTblRoom(getServiceRV2().getRoom(selectedDataCheckIn.getTblRoom().getIdroom()));
                        }
                        selectedDataCheckIn.setRefReservationCheckInOutStatus(getServiceRV2().getReservationCheckInOutStatus(selectedDataCheckIn.getRefReservationCheckInOutStatus().getIdstatus()));
                        selectedDataCheckIn.setAdultNumber(selectedDataRoomTypeDetail.getTblRoomType().getAdultNumber());
                        selectedDataCheckIn.setChildNumber(selectedDataRoomTypeDetail.getTblRoomType().getChildNumber());
                        SysDataHardCode defaultGuestCardUsedNumber = getServiceRV2().getDataSysDataHardCode((long) 3); //DefaultGuestCardUsedNumber = '3'
                        selectedDataCheckIn.setCardUsed(defaultGuestCardUsedNumber != null
                                && defaultGuestCardUsedNumber.getDataHardCodeValue() != null
                                        ? Integer.parseInt(defaultGuestCardUsedNumber.getDataHardCodeValue())
                                        : 0);
                        SysDataHardCode defaultGuestCardBrokenCharge = getServiceRV2().getDataSysDataHardCode((long) 4); //DefaultGuestCardBrokenCharge = '4'
                        selectedDataCheckIn.setBrokenCardCharge(defaultGuestCardBrokenCharge != null
                                && defaultGuestCardBrokenCharge.getDataHardCodeValue() != null
                                        ? new BigDecimal(defaultGuestCardBrokenCharge.getDataHardCodeValue())
                                        : new BigDecimal("0"));
                        selectedDataCheckIn.setCardMissed(0);
                        selectedDataCheckIn.setAdditionalCardCharge(new BigDecimal("0"));
                        selectedDataCheckIn.setCardAdditional(0);
                        selectedChildDetails = new ArrayList<>();
                        //sys data hardcode (default checkin time)
                        defaultCheckInTime = new Time(0, 0, 0);
                        SysDataHardCode sdhDefaultCheckInTime = getServiceRV2().getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
                        if (sdhDefaultCheckInTime != null
                                && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
                            String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
                            defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                                    Integer.parseInt(dataCheckInTime[1]),
                                    Integer.parseInt(dataCheckInTime[2]));
                        }
                        if (!LocalDateTime.now().isBefore(LocalDateTime.of(
                                selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900, selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1, selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                defaultCheckInTime.getHours(), defaultCheckInTime.getMinutes())
                                .minusDays(1))) {
                            if (!LocalDateTime.now().isBefore(LocalDateTime.of(
                                    selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900, selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1, selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                    defaultCheckInTime.getHours(), defaultCheckInTime.getMinutes()))) {
                                showReservationCheckInDialog();
                            } else {
                                selectedAdditionalService = new TblReservationAdditionalService();
                                selectedAdditionalService.setTblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                                selectedAdditionalService.setPrice(new BigDecimal("0"));
                                selectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                TblRoomService roomService = getServiceRV2().getRoomService(2);     //early checkin : '2'
                                selectedAdditionalService.setTblRoomService(roomService);
//                                setAdditionalServiceDiscount(selectedAdditionalService, dataCheckOutBill);  //must be checkout bill @@@+++
                                showReservationEarlyCheckInDialog();
                            }
                        } else {
                            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Tidak dapat melakukan check-in pada tanggal reservasi dengan perbedaan waktu lebih dari 1 hari..!", null);
                        }
                    } else {
                        if (selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2) {    //Ready to Check Out = '2'
                            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data telah check-in, silahkan pilih data lainnya..!", null);
                        } else {    //Checked Out = '3'
                            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data telah check-out, silahkan pilih data lainnya..!", null);
                        }
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Reservasi Kamar tidak ditemukan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showReservationCheckInDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationCheckInInputDialog.fxml"));

            ReservationCheckInInputController controller = new ReservationCheckInInputController(this);
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

    private void showReservationEarlyCheckInDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationEarlyCheckInInputDialog.fxml"));

            ReservationEarlyCheckInInputController controller = new ReservationEarlyCheckInInputController(this);
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

    public TblReservationCheckInOut selectedDataCheckOut;

    private void dataRoomStatusCheckOutHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (selectedDataRoomStatus.getDataPreviousRRTD() != null
                    || selectedDataRoomStatus.getDataNextRRTD() != null) {
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                if (selectedDataRoomTypeDetail.getTblReservationCheckInOut() != null
                        && selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 0) { //Ready to Check In = '0'
                    if ((selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 3) //Checked Out = '3'
                            || (selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 4)) {   //Tidak Berlaku = '4'
                        selectedDataCheckOut = selectedDataRoomTypeDetail.getTblReservationCheckInOut();
                        //sys data hardcode (default checkin time)
                        defaultCheckInTime = new Time(0, 0, 0);
                        SysDataHardCode sdhDefaultCheckInTime = getServiceRV2().getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
                        if (sdhDefaultCheckInTime != null
                                && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
                            String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
                            defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                                    Integer.parseInt(dataCheckInTime[1]),
                                    Integer.parseInt(dataCheckInTime[2]));
                        }
                        //sys data hardcode (default checkout time)
                        defaultCheckOutTime = new Time(0, 0, 0);
                        SysDataHardCode sdhDefaultCheckOutTime = getServiceRV2().getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
                        if (sdhDefaultCheckOutTime != null
                                && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
                            String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
                            defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                                    Integer.parseInt(dataCheckOutTime[1]),
                                    Integer.parseInt(dataCheckOutTime[2]));
                        }
                        if (!LocalDateTime.now().isAfter(LocalDateTime.of(
                                selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900, selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1, selectedDataRoomTypeDetail.getCheckOutDateTime().getDate(),
                                defaultCheckOutTime.getHours(), defaultCheckOutTime.getMinutes()))) {
                            showReservationCheckOutDialog();
                        } else {
                            selectedAdditionalService = new TblReservationAdditionalService();
                            selectedAdditionalService.setTblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                            selectedAdditionalService.setPrice(new BigDecimal("0"));
                            selectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                            TblRoomService roomService = getServiceRV2().getRoomService(3);     //early checkin : '3'
                            selectedAdditionalService.setTblRoomService(roomService);
                            showReservationLateCheckOutDialog();
                        }
                    } else {
                        HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data telah check-out/tidak berlaku, silahkan pilih data lainnya..!", null);
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data belum check-in, silahkan pilih data lainnya..!", null);
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Reservasi Kamar tidak ditemukan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public Stage checkOutDialogStage;

    private void showReservationCheckOutDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationCheckOutInputDialog.fxml"));

            ReservationCheckOutInputController controller = new ReservationCheckOutInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            checkOutDialogStage = new Stage();
            checkOutDialogStage.initModality(Modality.WINDOW_MODAL);
            checkOutDialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(checkOutDialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            checkOutDialogStage.initStyle(StageStyle.TRANSPARENT);
            checkOutDialogStage.setScene(scene);
            checkOutDialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            checkOutDialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    private void showReservationLateCheckOutDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationLateCheckOutInputDialog.fxml"));

            ReservationLateCheckOutInputController controller = new ReservationLateCheckOutInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            checkOutDialogStage = new Stage();
            checkOutDialogStage.initModality(Modality.WINDOW_MODAL);
            checkOutDialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(checkOutDialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            checkOutDialogStage.initStyle(StageStyle.TRANSPARENT);
            checkOutDialogStage.setScene(scene);
            checkOutDialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            checkOutDialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    public void dataRoomStatusReservationBillAndPaymentHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (selectedDataRoomStatus.getDataPreviousRRTD() != null
                    || selectedDataRoomStatus.getDataNextRRTD() != null) {
                //sys data hardcode (default checkin time)
                defaultCheckInTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckInTime = getServiceRV2().getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
                if (sdhDefaultCheckInTime != null
                        && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
                    String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
                    defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                            Integer.parseInt(dataCheckInTime[1]),
                            Integer.parseInt(dataCheckInTime[2]));
                }
                //sys data hardcode (default checkout time)
                defaultCheckOutTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckOutTime = getServiceRV2().getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
                if (sdhDefaultCheckOutTime != null
                        && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
                    String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
                    defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                            Integer.parseInt(dataCheckOutTime[1]),
                            Integer.parseInt(dataCheckOutTime[2]));
                }
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                //show dialog input (bill & payment)
                showReservationBillAndPaymentDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Reservasi Kamar tidak ditemukan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showReservationBillAndPaymentDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationBillAndPaymentDialog.fxml"));

            ReservationBillAndPaymentController controller = new ReservationBillAndPaymentController(this);
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

    public IntegerProperty additionalCardUsedNumber = new SimpleIntegerProperty();

    private void dataRoomStatusAddCardNumberHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (checkDataReservationRoomtypeDetail(selectedDataRoomStatus)) {
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                if (selectedDataRoomTypeDetail.getTblReservationCheckInOut() != null
                        && (selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {   //Ready to Check Out = '2'
                    //data additional card used number
                    additionalCardUsedNumber = new SimpleIntegerProperty();
                    additionalCardUsedNumber.set(0);
                    showReservationCheckInAddCardUsedNumberDialog();
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Silahkan pilih data berstatus 'Checked In'..!", null);
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak ada tamu yang sedang 'Checked In' pada kamar yang dipilih..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showReservationCheckInAddCardUsedNumberDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationCheckInAddCardUsedInputDialog.fxml"));

            ReservationCheckInAddCardUsedInputController controller = new ReservationCheckInAddCardUsedInputController(this);
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

    public LocalDateTime currentDateForChangeRoomProcess;

    public List<TblReservationAdditionalItem> selectedAdditionalItems;
    
    public List<TblReservationAdditionalService> selectedAdditionalServices;
    
    public void dataRoomStatusChangeRoomHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (selectedDataRoomStatus.getDataPreviousRRTD() != null
                    || selectedDataRoomStatus.getDataNextRRTD() != null) {
                //sys data hardcode (default checkin time)
                defaultCheckInTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckInTime = getServiceRV2().getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
                if (sdhDefaultCheckInTime != null
                        && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
                    String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
                    defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                            Integer.parseInt(dataCheckInTime[1]),
                            Integer.parseInt(dataCheckInTime[2]));
                }
                //sys data hardcode (default checkout time)
                defaultCheckOutTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckOutTime = getServiceRV2().getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
                if (sdhDefaultCheckOutTime != null
                        && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
                    String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
                    defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                            Integer.parseInt(dataCheckOutTime[1]),
                            Integer.parseInt(dataCheckOutTime[2]));
                }
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                currentDateForChangeRoomProcess = LocalDateTime.of(LocalDate.now(), LocalTime.now());
                if (currentDateForChangeRoomProcess.isAfter(
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(
                                        defaultCheckOutTime.getHours(),
                                        defaultCheckOutTime.getMinutes(),
                                        0)))) {
                    //current day
                    currentDateForChangeRoomProcess = LocalDateTime.of(LocalDate.now(), LocalTime.of(
                            defaultCheckOutTime.getHours(),
                            defaultCheckOutTime.getMinutes(),
                            0));
                } else {
                    //one day before
                    currentDateForChangeRoomProcess = LocalDateTime.of(LocalDate.now(), LocalTime.of(
                            defaultCheckOutTime.getHours(),
                            defaultCheckOutTime.getMinutes(),
                            0)).minusDays(1);
                }
                if (currentDateForChangeRoomProcess.isBefore( //(cannot change room at second time ***)
                        LocalDateTime.of(selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                                selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                                selectedDataRoomTypeDetail.getCheckOutDateTime().getDate(),
                                selectedDataRoomTypeDetail.getCheckOutDateTime().getHours(),
                                selectedDataRoomTypeDetail.getCheckOutDateTime().getMinutes(),
                                0))) {
                    if (currentDateForChangeRoomProcess.isBefore(
                            LocalDateTime.of(selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                                    selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                                    selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                    selectedDataRoomTypeDetail.getCheckInDateTime().getHours(),
                                    selectedDataRoomTypeDetail.getCheckInDateTime().getMinutes(),
                                    0))) {
                        currentDateForChangeRoomProcess = LocalDateTime.of(selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                                selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                                selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                selectedDataRoomTypeDetail.getCheckInDateTime().getHours(),
                                selectedDataRoomTypeDetail.getCheckInDateTime().getMinutes(),
                                0);
                    }
                    //data additional
                    selectedAdditionalItems = getServiceRV2().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(selectedDataRoomTypeDetail.getIddetail());
                    for (int i = selectedAdditionalItems.size() - 1; i > -1; i--) {
                        if ((LocalDateTime.of(selectedAdditionalItems.get(i).getAdditionalDate().getYear() + 1900,
                                selectedAdditionalItems.get(i).getAdditionalDate().getMonth() + 1,
                                selectedAdditionalItems.get(i).getAdditionalDate().getDate(),
                                defaultCheckInTime.getHours(),
                                defaultCheckInTime.getMinutes(),
                                0)).isBefore(currentDateForChangeRoomProcess)
                                || (selectedAdditionalItems.get(i).getRefReservationBillType().getIdtype() != 0 //Reservasi = '0'
                                && selectedAdditionalItems.get(i).getRefReservationBillType().getIdtype() != 1)) {   //Check Out = '1'
                            selectedAdditionalItems.remove(i);
                        } else {
                            //data room type detail
                            selectedAdditionalItems.get(i).setTblReservationRoomTypeDetail(getServiceRV2().getReservationRoomTypeDetail(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getIddetail()));
                            //data reservation
                            selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().setTblReservation(getServiceRV2().getReservation(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
                            //data room type
                            selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().setTblRoomType(getServiceRV2().getRoomType(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
                            //data reservation order by
                            selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().setRefReservationOrderByType(getServiceRV2().getReservationOrderByType(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getRefReservationOrderByType().getIdtype()));
                            //data item
                            selectedAdditionalItems.get(i).setTblItem(getServiceRV2().getDataItem(selectedAdditionalItems.get(i).getTblItem().getIditem()));
                            //data item type hk
                            if (selectedAdditionalItems.get(i).getTblItem().getTblItemTypeHk() != null) {
                                selectedAdditionalItems.get(i).getTblItem().setTblItemTypeHk(getServiceRV2().getDataItemTypeHK(selectedAdditionalItems.get(i).getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                            }
                            //data item type wh
                            if (selectedAdditionalItems.get(i).getTblItem().getTblItemTypeWh() != null) {
                                selectedAdditionalItems.get(i).getTblItem().setTblItemTypeWh(getServiceRV2().getDataItemTypeWH(selectedAdditionalItems.get(i).getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                            }
                            //data hotel event
                            if (selectedAdditionalItems.get(i).getTblHotelEvent() != null) {
                                selectedAdditionalItems.get(i).setTblHotelEvent(getServiceRV2().getHotelEvent(selectedAdditionalItems.get(i).getTblHotelEvent().getIdevent()));
                            }
                            //data card event
                            if (selectedAdditionalItems.get(i).getTblBankEventCard() != null) {
                                selectedAdditionalItems.get(i).setTblBankEventCard(getServiceRV2().getBankEventCard(selectedAdditionalItems.get(i).getTblBankEventCard().getIdevent()));
                            }
                            //data reservation bill type
                            selectedAdditionalItems.get(i).setRefReservationBillType(getServiceRV2().getDataReservationBillType(selectedAdditionalItems.get(i).getRefReservationBillType().getIdtype()));
                        }
                    }
                    selectedAdditionalServices = getServiceRV2().getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(selectedDataRoomTypeDetail.getIddetail());
                    for (int i = selectedAdditionalServices.size() - 1; i > -1; i--) {
                        if ((LocalDateTime.of(selectedAdditionalServices.get(i).getAdditionalDate().getYear() + 1900,
                                selectedAdditionalServices.get(i).getAdditionalDate().getMonth() + 1,
                                selectedAdditionalServices.get(i).getAdditionalDate().getDate(),
                                defaultCheckInTime.getHours(),
                                defaultCheckInTime.getMinutes(),
                                0)).isBefore(currentDateForChangeRoomProcess)
                                || (selectedAdditionalServices.get(i).getRefReservationBillType().getIdtype() != 0 //Reservasi = '0'
                                && selectedAdditionalServices.get(i).getRefReservationBillType().getIdtype() != 1) //Check Out = '1'
                                || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 2 //Early Checkin = '2'
                                || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 3 //Late Checkout = '3'
                                || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 4 //Lainnya = '4'
                                || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 5 //Canceling Fee = '5'
                                || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                                || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 7) {   //Dine in Resto = '7'
                            selectedAdditionalServices.remove(i);
                        } else {
                            //data room type detail
                            selectedAdditionalServices.get(i).setTblReservationRoomTypeDetail(getServiceRV2().getReservationRoomTypeDetail(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getIddetail()));
                            //data reservation
                            selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().setTblReservation(getServiceRV2().getReservation(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
                            //data room type
                            selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().setTblRoomType(getServiceRV2().getRoomType(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
                            //data reservation order by
                            selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().setRefReservationOrderByType(getServiceRV2().getReservationOrderByType(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getRefReservationOrderByType().getIdtype()));
                            //data room service
                            selectedAdditionalServices.get(i).setTblRoomService(getServiceRV2().getRoomService(selectedAdditionalServices.get(i).getTblRoomService().getIdroomService()));
                            //data hotel event
                            if (selectedAdditionalServices.get(i).getTblHotelEvent() != null) {
                                selectedAdditionalServices.get(i).setTblHotelEvent(getServiceRV2().getHotelEvent(selectedAdditionalServices.get(i).getTblHotelEvent().getIdevent()));
                            }
                            //data card event
                            if (selectedAdditionalServices.get(i).getTblBankEventCard() != null) {
                                selectedAdditionalServices.get(i).setTblBankEventCard(getServiceRV2().getBankEventCard(selectedAdditionalServices.get(i).getTblBankEventCard().getIdevent()));
                            }
                            //data reservation bill type
                            selectedAdditionalServices.get(i).setRefReservationBillType(getServiceRV2().getDataReservationBillType(selectedAdditionalServices.get(i).getRefReservationBillType().getIdtype()));
                        }
                    }
                    if(selectedDataRoomTypeDetail.getTblReservationCheckInOut() != null
                            && selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() != null
                            && selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null
                            && (selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1     //Checked In = '1'
                            || selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)){   //Ready to Check Out = '2'
                        //show dialog input (has been check in)
                        showReservationChangeRoomOutInDialog();
                    }else{  
                        //show dialog input
                        showReservationChangeRoomDialog();
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Tidak dapat melakukan 'pindah kamar' pada data reservasi kamar yang telah lewat "
                            + "\n (waktu sekarang (tanggal/jam) melebihi waktu check-out yang ada (tanggal/jam))..!", null);
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Reservasi Kamar tidak ditemukan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showReservationChangeRoomDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationChangeRoomDialog.fxml"));

            ReservationChangeRoomController controller = new ReservationChangeRoomController(this);
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

    private void showReservationChangeRoomOutInDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationChangeRoomOutInDialog.fxml"));

            ReservationChangeRoomOutInController controller = new ReservationChangeRoomOutInController(this);
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
    
    public ObjectProperty<LocalDate> extendRoomCheckOutDate = new SimpleObjectProperty<>();

    public void dataRoomStatusExtendRoomHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (checkDataReservationRoomtypeDetail(selectedDataRoomStatus)) {
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                if (selectedDataRoomTypeDetail.getTblReservationCheckInOut() != null
                        && (selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || selectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {   //Ready to Check Out = '2'
                    //data extend room - check out date
                    extendRoomCheckOutDate = new SimpleObjectProperty<>();
                    extendRoomCheckOutDate.set(LocalDate.of(
                            selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                            selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                            selectedDataRoomTypeDetail.getCheckOutDateTime().getDate()));
                    //open dialog input data extend room
                    showReservationCheckInExtendRoomDialog();
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Silahkan pilih data berstatus 'Checked In' atau 'Ready to Check Out'..!", null);
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak ada tamu yang sedang 'Checked In' pada kamar yang dipilih..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showReservationCheckInExtendRoomDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationCheckInExtendRoomDialog.fxml"));

            ReservationCheckInExtendRoomController controller = new ReservationCheckInExtendRoomController(this);
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

    public TblReservationAdditionalService selectedDataAdditionalService;

    private void dataRoomStatusCreateRestoAdditionalServiceHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (checkDataReservationRoomtypeDetail(selectedDataRoomStatus)) {
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                //show dialog input (additional room dining)
                showRestoAdditionalServiceInputDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak ada tamu yang sedang 'Checked In' pada kamar yang dipilih..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showRestoAdditionalServiceInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationAdditionalServiceRestoDialog.fxml"));

            ReservationAdditionalServiceRestoController controller = new ReservationAdditionalServiceRestoController(this);
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

    private void dataRoomStatusReservationAdditionalItemAndServiceHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (checkDataReservationRoomtypeDetail(selectedDataRoomStatus)) {
                //sys data hardcode (default checkin time)
                defaultCheckInTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckInTime = getServiceRV2().getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
                if (sdhDefaultCheckInTime != null
                        && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
                    String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
                    defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                            Integer.parseInt(dataCheckInTime[1]),
                            Integer.parseInt(dataCheckInTime[2]));
                }
                //sys data hardcode (default checkout time)
                defaultCheckOutTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckOutTime = getServiceRV2().getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
                if (sdhDefaultCheckOutTime != null
                        && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
                    String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
                    defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                            Integer.parseInt(dataCheckOutTime[1]),
                            Integer.parseInt(dataCheckOutTime[2]));
                }
                selectedDataRoomTypeDetail = selectedDataRoomStatus.getDataPreviousRRTD() != null
                        ? selectedDataRoomStatus.getDataPreviousRRTD() : selectedDataRoomStatus.getDataNextRRTD();
                //show dialog input (additional item & service)
                showReservationAdditionalItemAndServiceInputDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak ada tamu yang sedang 'Checked In' pada kamar yang dipilih..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showReservationAdditionalItemAndServiceInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationAdditionalItemAndServiceDialog.fxml"));

            ReservationAdditionalItemAndServiceController controller = new ReservationAdditionalItemAndServiceController(this);
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

    private boolean checkDataReservationRoomtypeDetail(DataRoomStatus dataRoomStatus) {
        if (dataRoomStatus != null) {
            if (dataRoomStatus.getDataPreviousRRTD() != null) {
                if (dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut() != null
                        && (dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) { //Ready to Check Out = '2'
                    return true;
                }
            } else {
                if (dataRoomStatus.getDataNextRRTD() != null) {
                    if (dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut() != null
                            && (dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                            || dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) { //Ready to Check Out = '2'
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void dataRoomStatusCreateRoomDiningHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (checkDataEnableToAddRoomDining(selectedDataRoomStatus)) {
                selectedDataAdditionalService = new TblReservationAdditionalService();
                if (selectedDataRoomStatus.getDataPreviousRRTD() != null) {   //'Checked In' or 'Ready To Checked Out'
                    selectedDataAdditionalService.setTblReservationRoomTypeDetail(selectedDataRoomStatus.getDataPreviousRRTD());
                    selectedDataAdditionalService.setAdditionalDate(Date.valueOf(selectedDataRoomStatus.getPreviousDate()));
                } else {  //'Checked In'
                    selectedDataAdditionalService.setTblReservationRoomTypeDetail(selectedDataRoomStatus.getDataNextRRTD());
                    selectedDataAdditionalService.setAdditionalDate(Date.valueOf(selectedDataRoomStatus.getNextDate()));
                }
                selectedDataAdditionalService.setTblRoomService(getService().getDataRoomService(6));    //Room Dining = '6'
                selectedDataAdditionalService.setPrice(new BigDecimal("0"));
                selectedDataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                selectedDataAdditionalService.setRefReservationBillType(getService().getDataReservationBillType(0));    //Tagihan Resto = '1'
                //show dialog input (additional room dining)
                showAdditionalRoomDiningInputDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak ada tamu yang sedang 'Checked In' pada kamar yang dipilih..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataEnableToAddRoomDining(DataRoomStatus dataRoomStatus) {
        if (dataRoomStatus != null) {
            if (dataRoomStatus.getDataPreviousRRTD() != null) {
                if (dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut() != null
                        && (dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) { //Ready to Check Out = '2'
                    return true;
                }
            } else {
                if (dataRoomStatus.getDataNextRRTD() != null) {
                    if (dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut() != null
                            && (dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                            || dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) { //Ready to Check Out = '2'
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showAdditionalRoomDiningInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/AdditionalRoomDiningDialog.fxml"));

            AdditionalRoomDiningController controller = new AdditionalRoomDiningController(this);
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

    private void dataRoomStatusCreateRestoDiningHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (checkDataEnableToRestoDining(selectedDataRoomStatus)) {
                selectedDataAdditionalService = new TblReservationAdditionalService();
                if (selectedDataRoomStatus.getDataPreviousRRTD() != null) {   //'Checked In' or 'Ready To Checked Out'
                    selectedDataAdditionalService.setTblReservationRoomTypeDetail(selectedDataRoomStatus.getDataPreviousRRTD());
                    selectedDataAdditionalService.setAdditionalDate(Date.valueOf(selectedDataRoomStatus.getPreviousDate()));
                } else {  //'Checked In'
                    selectedDataAdditionalService.setTblReservationRoomTypeDetail(selectedDataRoomStatus.getDataNextRRTD());
                    selectedDataAdditionalService.setAdditionalDate(Date.valueOf(selectedDataRoomStatus.getNextDate()));
                }
                selectedDataAdditionalService.setTblRoomService(getService().getDataRoomService(7));    //Dine in Resto = '7'
                selectedDataAdditionalService.setPrice(new BigDecimal("0"));
                selectedDataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                selectedDataAdditionalService.setRefReservationBillType(getService().getDataReservationBillType(0));    //Reservasi = '0'
                //show dialog input (resto dining)
                showAdditonalRestoDiningInputDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak ada tamu yang sedang 'Checked In' pada kamar yang dipilih..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataEnableToRestoDining(DataRoomStatus dataRoomStatus) {
        if (dataRoomStatus != null) {
            if (dataRoomStatus.getDataPreviousRRTD() != null) {
                if (dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut() != null
                        && (dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || dataRoomStatus.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) { //Ready to Check Out = '2'
                    return true;
                }
            } else {
                if (dataRoomStatus.getDataNextRRTD() != null) {
                    if (dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut() != null
                            && (dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                            || dataRoomStatus.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) { //Ready to Check Out = '2'
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showAdditonalRestoDiningInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/AdditionalRestoDiningDialog.fxml"));

            AdditionalRestoDiningController controller = new AdditionalRestoDiningController(this);
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

    private void dataRoomStatusChangeStatusDetailHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 0;
            selectedDataRoom = getService().getDataRoom(((DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem()).getDataRoom().getIdroom());
            selectedDataRoom.setRefRoomStatus(getService().getDataRoomStatus(selectedDataRoom.getRefRoomStatus().getIdstatus()));
            //show dialog input (change room status detail)
            showChangeRoomStatusDetailInputDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showChangeRoomStatusDetailInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ChangeRoomStatusDetailNoteDialog.fxml"));

            ChangeRoomStatusDetailNoteController controller = new ChangeRoomStatusDetailNoteController(this);
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

    private void refreshDataTableRoomStatus() {
        tableDataRoomStatus.getTableView().setItems(loadAllDataRoomWithStatus());
        cft.refreshFilterItems(tableDataRoomStatus.getTableView().getItems());
        cft.setTextFilter(currentTextFilter);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FRoomCheckManager fRoomCheckManager;

    private FReservationV2Manager fReservationV2Manager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fRoomCheckManager = new FRoomCheckManagerImpl();
        fReservationV2Manager = new FReservationV2ManagerImpl();

        //set splitpane
        setDataRoomStatusSplitpane();

        //init table
        initTableDataRoomStatus();

        //init form
        initFormDataRoomCheck();

        //set data to content
        setSelectedDataToInputForm();

        spDataRoomStatus.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRoomStatusFormShowStatus.set(0.0);
        });
    }

    public FRoomCheckManager getService() {
        return this.fRoomCheckManager;
    }

    public FReservationV2Manager getServiceRV2() {
        return this.fReservationV2Manager;
    }

}
