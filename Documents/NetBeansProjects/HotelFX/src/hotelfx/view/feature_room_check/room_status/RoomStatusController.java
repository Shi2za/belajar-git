/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomCheck;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendance;
import hotelfx.persistence.service.FRoomCheckManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_room_check.RoomCheckController;
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
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
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
 * @author ANDRI
 */
public class RoomStatusController implements Initializable {

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

        TableColumn<DataRoomStatus, String> statusCheck = new TableColumn<>("  Status \nPengecekan");
        statusCheck.setCellFactory((TableColumn<DataRoomStatus, String> param) -> new StatusCheckButtonCell<>());
        statusCheck.setMinWidth(80);

        tableView.getColumns().addAll(bulidingFloorName, roomName, previousTitle, nextTitle, roomStatus, roomStatusDetail, statusCheck);

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

    public class StatusCheckButtonCell<S, T> extends TableCell<S, T> {

        private JFXButton button;

        public StatusCheckButtonCell() {

        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (this.getTableRow() != null) {
                    Object data = this.getTableRow().getItem();
                    if (data != null) {
                        this.button = new JFXButton();
                        this.button.getStyleClass().add("button-check-in-progress");
                        if (isInProgress(((DataRoomStatus) data))) {
                            this.button.setVisible(true);
                        } else {
                            this.button.setVisible(false);
                        }
                        this.button.setPrefSize(25, 25);
                        setAlignment(Pos.CENTER);

                        setText(null);
                        setGraphic(button);
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        }

        @Override
        public void startEdit() {

        }

        @Override
        public void cancelEdit() {

        }

      

    }
      private boolean isInProgress(DataRoomStatus dataRoomStatus) {
            TblRoomCheckHouseKeepingAttendance roomCheckHouseKeepingAttendance = parentController.getFRoomCheckManager().getDataRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull(
                    dataRoomStatus.getDataRoom().getIdroom()
            );
          selectedDataHouseKeepingAttendance  =  roomCheckHouseKeepingAttendance;
            return roomCheckHouseKeepingAttendance != null;
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
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = parentController.getFRoomCheckManager().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = parentController.getFRoomCheckManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = parentController.getFRoomCheckManager().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(parentController.getFRoomCheckManager().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(parentController.getFRoomCheckManager().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(parentController.getFRoomCheckManager().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
                            //data reservation
                            rrtd.setTblReservation(parentController.getFRoomCheckManager().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(parentController.getFRoomCheckManager().getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(parentController.getFRoomCheckManager().getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
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
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = parentController.getFRoomCheckManager().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = parentController.getFRoomCheckManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = parentController.getFRoomCheckManager().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(parentController.getFRoomCheckManager().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(parentController.getFRoomCheckManager().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(parentController.getFRoomCheckManager().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
                            //data reservation
                            rrtd.setTblReservation(parentController.getFRoomCheckManager().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(parentController.getFRoomCheckManager().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(parentController.getFRoomCheckManager().getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(parentController.getFRoomCheckManager().getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
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
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cek Kamar");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataRoomStatusCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Set Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //listener
                dataRoomStatusSetRoomForReservationRoomHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah Status Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                
                //listener
              dataRoomStatusChangeStatusHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Rusak Barang (Customer)");
            buttonControl.setOnMouseClicked((e) -> {
                //listener
                dataRoomStatusCustomerBrokenItemHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah Status Detail Kamar");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener
//                dataRoomStatusChangeStatusDetailHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak (Worksheet)");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataRoomStatusPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        
        if(true){
           buttonControl = new JFXButton();
           buttonControl.setText("Kode Barang (Worksheet)");
           buttonControl.setOnMouseClicked((e)->{
               showCodeItemWorkSheetDialog();
           });
           buttonControls.add(buttonControl);
        }
        tableDataRoomStatus.addButtonControl(buttonControls);
    }

    public ObservableList<DataRoomStatus> loadAllDataRoomWithStatus() {
        List<DataRoomStatus> list = new ArrayList<>();
        LocalDate previousDate = (LocalDate.now()).minusDays(1);
        LocalDate nextDate = LocalDate.now();
        List<TblRoom> rooms = parentController.getFRoomCheckManager().getAllDataRoom();
        for (TblRoom room : rooms) {
            //data location
            room.setTblLocation(parentController.getFRoomCheckManager().getDataLocation(room.getTblLocation().getIdlocation()));
            //data building
            room.getTblLocation().setTblBuilding(parentController.getFRoomCheckManager().getDataBuilding(room.getTblLocation().getTblBuilding().getIdbuilding()));
            //data floor
            room.getTblLocation().setTblFloor(parentController.getFRoomCheckManager().getDataFloor(room.getTblLocation().getTblFloor().getIdfloor()));
            //data room type
            room.setTblRoomType(parentController.getFRoomCheckManager().getDataRoomType(room.getTblRoomType().getIdroomType()));
            //data room status
            room.setRefRoomStatus(parentController.getFRoomCheckManager().getDataRoomStatus(room.getRefRoomStatus().getIdstatus()));
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
            List<TblRoom> rooms = parentController.getFRoomCheckManager().getAllDataRoomByIDRoomStatus(dataRoomStatus.getIdstatus());
            return rooms.size() + " Kamar";
        }
        return "";
    }

    private ObservableList<RefRoomStatus> loadAllDataRoomStatusHeader() {
        List<RefRoomStatus> list = parentController.getFRoomCheckManager().getAllDataRoomStatus();
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
        List<TblRoom> rooms = parentController.getFRoomCheckManager().getAllDataRoom();
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

    public void dataRoomStatusCreateHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 0;
            selectedData = new TblRoomCheck();
            selectedData.setTblRoom(parentController.getFRoomCheckManager().getDataRoom(((DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem()).getDataRoom().getIdroom()));
            selectedData.getTblRoom().setTblRoomType(parentController.getFRoomCheckManager().getDataRoomType(selectedData.getTblRoom().getTblRoomType().getIdroomType()));
            selectedData.getTblRoom().setRefRoomStatus(parentController.getFRoomCheckManager().getDataRoomStatus(((DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem()).getDataRoom().getRefRoomStatus().getIdstatus()));
            itemMutationHistoryPropertyBarcodes = new ArrayList<>();
            itemMutationHistoryItemExpiredDates = new ArrayList<>();
            //show dialog room check input
            showRoomCheckInputDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showRoomCheckInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/RoomCheckInputDialog.fxml"));

            RoomCheckInputController controller = new RoomCheckInputController(this);
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

    private void dataRoomStatusSetRoomForReservationRoomHandle() {
        //show dialog 'set room for reservation room'
        showSetRoomForReservationRoomDialog();
    }

    private void showSetRoomForReservationRoomDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/SetRoomForReservationRoomDialog.fxml"));

            SetRoomForReservationRoomController controller = new SetRoomForReservationRoomController(this);
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

    public TblRoom selectedDataRoom;
    
    public DataRoomStatus selectedDataRoomStatus;
    
    public TblRoomCheckHouseKeepingAttendance selectedDataHouseKeepingAttendance;
    
    private void dataRoomStatusChangeStatusHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if(isInProgress((DataRoomStatus)tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem())){
               selectedDataRoomStatus = (DataRoomStatus)tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
               showCheckRoomStatusInputDialog();
            }
            else{
              selectedDataRoom = parentController.getFRoomCheckManager().getDataRoom(((DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem()).getDataRoom().getIdroom());
              selectedDataRoom.setRefRoomStatus(parentController.getFRoomCheckManager().getDataRoomStatus(selectedDataRoom.getRefRoomStatus().getIdstatus()));
              showChangeRoomStatusInputDialog();
            }
            dataInputStatus = 0;
            
            //show dialog input (change room status)
            
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }
    
    Stage dialogStageCheckRoom;
    
    private void showCheckRoomStatusInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/CheckRoomDialog.fxml"));

            CheckRoomController controller = new CheckRoomController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageCheckRoom = new Stage();
            dialogStageCheckRoom.initModality(Modality.WINDOW_MODAL);
            dialogStageCheckRoom.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageCheckRoom, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageCheckRoom.initStyle(StageStyle.TRANSPARENT);
            dialogStageCheckRoom.setScene(scene);
            dialogStageCheckRoom.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageCheckRoom.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }
     
    private void showChangeRoomStatusInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/ChangeRoomStatusDialog.fxml"));

            ChangeRoomStatusController controller = new ChangeRoomStatusController(this);
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

    public TblReservationBrokenItem selectedDataBrokenItem;

    private void dataRoomStatusCustomerBrokenItemHandle() {
        if (tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            DataRoomStatus selectedDataRoomStatus = (DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem();
            if (checkDataEnableToCustomerBrokenItem(selectedDataRoomStatus)) {
                selectedDataBrokenItem = new TblReservationBrokenItem();
                if(selectedDataRoomStatus.getDataPreviousRRTD() != null){   //'Checked In' or 'Ready To Checked Out'
                   selectedDataBrokenItem.setTblReservationRoomTypeDetail(selectedDataRoomStatus.getDataPreviousRRTD()); 
                }else{  //'Checked In'
                    selectedDataBrokenItem.setTblReservationRoomTypeDetail(selectedDataRoomStatus.getDataNextRRTD()); 
                }
                //show dialog input (customer broken item)
                showCustomerBrokenItemInputDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Tidak ada tamu yang sedang 'Checked In' pada kamar yang dipilih..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataEnableToCustomerBrokenItem(DataRoomStatus dataRoomStatus) {
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

    private void showCustomerBrokenItemInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/CustomerBrokenItemDialog.fxml"));

            CustomerBrokenItemController controller = new CustomerBrokenItemController(this);
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
            selectedDataRoom = parentController.getFRoomCheckManager().getDataRoom(((DataRoomStatus) tableDataRoomStatus.getTableView().getSelectionModel().getSelectedItem()).getDataRoom().getIdroom());
            selectedDataRoom.setRefRoomStatus(parentController.getFRoomCheckManager().getDataRoomStatus(selectedDataRoom.getRefRoomStatus().getIdstatus()));
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
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/ChangeRoomStatusDetailNoteDialog.fxml"));

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

    private void showChangeRoomStatusDetailInputDialogOldVersion() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/ChangeRoomStatusDetailDialog.fxml"));

            ChangeRoomStatusDetailController controller = new ChangeRoomStatusDetailController(this);
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

    private void dataRoomStatusPrintHandle() {
        showWorkSheetDialog();
    }

    Stage dialogStageWorkSheet;

    private void showWorkSheetDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/PrintWorkSheetHouseKeepingDialog.fxml"));

            PrintWorkSheetHouseKeepingController controller = new PrintWorkSheetHouseKeepingController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageWorkSheet = new Stage();
            dialogStageWorkSheet.initModality(Modality.WINDOW_MODAL);
            dialogStageWorkSheet.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageWorkSheet, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageWorkSheet.initStyle(StageStyle.TRANSPARENT);
            dialogStageWorkSheet.setScene(scene);
            dialogStageWorkSheet.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageWorkSheet.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }
    
    Stage dialogStageCodeItemWorkSheet;
    
     private void showCodeItemWorkSheetDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/CodeItemWorkSheetDialog.fxml"));

            CodeItemWorkSheetController controller = new CodeItemWorkSheetController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageCodeItemWorkSheet = new Stage();
            dialogStageCodeItemWorkSheet.initModality(Modality.WINDOW_MODAL);
            dialogStageCodeItemWorkSheet.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageCodeItemWorkSheet, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageCodeItemWorkSheet.initStyle(StageStyle.TRANSPARENT);
            dialogStageCodeItemWorkSheet.setScene(scene);
            dialogStageCodeItemWorkSheet.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageCodeItemWorkSheet.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }
     
    private void refreshDataTableRoomStatus() {
        tableDataRoomStatus.getTableView().setItems(loadAllDataRoomWithStatus());
        cft.refreshFilterItems(tableDataRoomStatus.getTableView().getItems());
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

    public RoomStatusController(RoomCheckController parentController) {
        this.parentController = parentController;
    }

    private final RoomCheckController parentController;

    public RoomCheckController getParentController() {
        return parentController;
    }

    public FRoomCheckManager getService() {
        return parentController.getFRoomCheckManager();
    }

}
