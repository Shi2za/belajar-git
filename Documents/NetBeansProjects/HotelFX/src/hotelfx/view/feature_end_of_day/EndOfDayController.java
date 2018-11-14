/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_end_of_day;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.service.FEndOfDayManager;
import hotelfx.persistence.service.FEndOfDayManagerImpl;
import hotelfx.view.feature_end_of_day.L1.EODL1Controller;
import hotelfx.view.feature_end_of_day.L2.EODL2Controller;
import hotelfx.view.feature_end_of_day.L3.EODL3Controller;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class EndOfDayController implements Initializable {

    private boolean runnigAnimation = false;

    @FXML
    private SplitPane spData;

    private DoubleProperty show1;
    private DoubleProperty show2;
    private DoubleProperty show3;
    private DoubleProperty show4;

    private void initSplitPaneFunction() {
//        runnigAnimation = true;
        spData.setDividerPositions(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        spData.setDividerPositions(0, 0, 0, 0, 0, 0, 0, 0, 0, 1);
        spData.setDividerPositions(0, 0, 0, 0, 0, 0, 0, 0, 1, 1);
        spData.setDividerPositions(0, 0, 0, 0, 0, 0, 0, 1, 1, 1);
        spData.setDividerPositions(0, 0, 0, 0, 0, 0, 1, 1, 1, 1);
        spData.setDividerPositions(0, 0, 0, 0, 0, 1, 1, 1, 1, 1);
        spData.setDividerPositions(0, 0, 0, 0, 1, 1, 1, 1, 1, 1);
        spData.setDividerPositions(0, 0, 0, 1, 1, 1, 1, 1, 1, 1);
        spData.setDividerPositions(0, 0, 1, 1, 1, 1, 1, 1, 1, 1);
        spData.setDividerPositions(0, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        spData.setDividerPositions(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
//        runnigAnimation = false;
//        for (SplitPane.Divider div : spData.getDividers()) {
//            div.positionProperty().addListener((obs, oldVal, newVal) -> {
//                if (!runnigAnimation) {
//                    div.setPosition(oldVal.doubleValue());
//                }
//            });
//        }
    }

    public boolean enableToClosing = true;

    @FXML
    private JFXButton btnClosingEoD;

    @FXML
    private AnchorPane ancLayoutL1;

    @FXML
    private JFXButton btnNext1;

    @FXML
    private AnchorPane ancLayoutL2;

    @FXML
    private JFXButton btnPrevious2;

    @FXML
    private JFXButton btnNext2;

    @FXML
    private AnchorPane ancLayoutL3;

    @FXML
    private JFXButton btnPrevious3;

    @FXML
    private JFXButton btnNext3;

    @FXML
    private AnchorPane ancLayoutL4;

    @FXML
    private JFXButton btnPrevious4;

    @FXML
    private JFXButton btnNext4;

    @FXML
    private AnchorPane ancLayoutL5;

    @FXML
    private JFXButton btnPrevious5;

    @FXML
    private JFXButton btnNext5;

    @FXML
    private AnchorPane ancLayoutL6;

    @FXML
    private JFXButton btnPrevious6;

    @FXML
    private JFXButton btnNext6;

    @FXML
    private AnchorPane ancLayoutL7;

    @FXML
    private JFXButton btnPrevious7;

    @FXML
    private JFXButton btnNext7;

    @FXML
    private AnchorPane ancLayoutL8;

    @FXML
    private JFXButton btnPrevious8;

    @FXML
    private JFXButton btnNext8;

    @FXML
    private AnchorPane ancLayoutL9;

    @FXML
    private JFXButton btnPrevious9;

    @FXML
    private JFXButton btnNext9;

    @FXML
    private AnchorPane ancLayoutL10;

    @FXML
    private JFXButton btnPrevious10;

    @FXML
    private JFXButton btnNext10;

    @FXML
    private AnchorPane ancLayoutL11;

    @FXML
    private JFXButton btnPrevious11;

    /**
     * LISTENER FOR BUTTON 'CLOSING EOD'
     */
    private void closingEoDHandle() {
        if (checkDataEnableToClosing()) {
            if (!isAllEmpty()) {
                SysCurrentHotelDate dummySysCurrentHotelDate = new SysCurrentHotelDate(schd);
                dummySysCurrentHotelDate.setCurrentHotelDate(Date.valueOf(eodDate.plusDays(1)));
                List<TblReservationRoomTypeDetailRoomPriceDetail> dummyRRTDRPDs = new ArrayList<>();
                List<TblReservationAdditionalItem> dummyRAdditionalItems = new ArrayList<>();
                List<TblReservationAdditionalService> dummyRAdditionalServices = new ArrayList<>();
                List<TblReservationBrokenItem> dummyRBrokenItems = new ArrayList<>();
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    TblReservationRoomTypeDetailRoomPriceDetail dummyRRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail(rrtdrpd);
                    dummyRRTDRPD.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummyRRTDRPD.getTblReservationRoomPriceDetail()));
                    dummyRRTDRPD.getTblReservationRoomPriceDetail().setRefEndOfDayDataStatus(fEndOfDayManager.getDataEoDStatusData(1));    //Income = '1'
                    dummyRRTDRPDs.add(dummyRRTDRPD);
                }
                for (TblReservationAdditionalItem rAdditionalItem : rAdditionalItems) {
                    TblReservationAdditionalItem dummyRAdditionalItem = new TblReservationAdditionalItem(rAdditionalItem);
                    dummyRAdditionalItem.setRefEndOfDayDataStatus(fEndOfDayManager.getDataEoDStatusData(1));    //Income = '1'
                    dummyRAdditionalItems.add(dummyRAdditionalItem);
                }
                for (TblReservationAdditionalService rAdditionalService : rAdditionalServices) {
                    TblReservationAdditionalService dummyRAdditionalService = new TblReservationAdditionalService(rAdditionalService);
                    dummyRAdditionalService.setRefEndOfDayDataStatus(fEndOfDayManager.getDataEoDStatusData(1));    //Income = '1'
                    dummyRAdditionalServices.add(dummyRAdditionalService);
                }
                for (TblReservationBrokenItem rBrokenItem : rBrokenItems) {
                    TblReservationBrokenItem dummyRBrokenItem = new TblReservationBrokenItem(rBrokenItem);
                    dummyRBrokenItem.setRefEndOfDayDataStatus(fEndOfDayManager.getDataEoDStatusData(1));    //Income = '1'
                    dummyRBrokenItems.add(dummyRBrokenItem);
                }
                if (fEndOfDayManager.closingEndOfDay(dummySysCurrentHotelDate,
                        dummyRRTDRPDs,
                        dummyRAdditionalItems,
                        dummyRAdditionalServices,
                        dummyRBrokenItems)) {
                    ClassMessage.showSucceedInsertingDataMessage("", null);
                    //reload data (with data saved)
                    refreshAllData();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(fEndOfDayManager.getErrorMessage(), null);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN 'POSTING DATA'",
                        "Tidak ada yang harus di-posting..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN 'POSTING DATA'",
                    "'Posting Data' hanya dapat dilakukan apabila semua data telah sesuai..!", null);
        }
    }

    private boolean checkDataEnableToClosing() {
        boolean currentData = true;
        for (DataReservationRoomTypeDetail dataReservation : dataReservations) {
            if (!dataReservation.getBtnMessage().getTooltip().getText().equals("Data Telah Sesuai..!!")) {
                currentData = false;
                break;
            }
        }
        return currentData;
    }

    private boolean isAllEmpty() {
//        return dailyReservationRoomBills.isEmpty();
        return false;
    }

    private void refreshAllData() {
        //L1 - Data Reservation
        dataReservations = loadAllDataReservation();
//        //L2 - Data Room Status
//        dataRoomWithStatuss = loadAllDataRoomWithStatus();
        //L3 - Daily Room Bill
        dailyReservationRoomBills = loadAllDataReservationRoomBill();
    }

    /**
     * L1 - Reservation
     */
    public List<DataReservationRoomTypeDetail> dataReservations = new ArrayList<>();

    private List<DataReservationRoomTypeDetail> loadAllDataReservation() {
        List<DataReservationRoomTypeDetail> list = new ArrayList<>();
        List<TblReservationRoomTypeDetail> rrtds = fEndOfDayManager.getAllDataReservationRoomTypeDetailByFirstCheckInDateAndEndCheckOutDate(
                Timestamp.valueOf(LocalDateTime.of(eodDate, LocalTime.of(defaultCheckInTime.getHours(), defaultCheckInTime.getMinutes(), defaultCheckInTime.getSeconds()))),
                Timestamp.valueOf(LocalDateTime.of(eodDate, LocalTime.of(defaultCheckOutTime.getHours(), defaultCheckOutTime.getMinutes(), defaultCheckOutTime.getSeconds())))
        );
        for (TblReservationRoomTypeDetail rrtd : rrtds) {
            list.add(new DataReservationRoomTypeDetail(rrtd));
        }
        return list;
    }

    public class DataReservationRoomTypeDetail {

        private final ObjectProperty<TblReservationRoomTypeDetail> dataReservationRoomTypeDetail = new SimpleObjectProperty<>();

        private JFXButton btnMessage = new JFXButton("");

        public DataReservationRoomTypeDetail(TblReservationRoomTypeDetail dataReservationRoomTypeDetail) {
            this.dataReservationRoomTypeDetail.set(dataReservationRoomTypeDetail);
            //generate button message
            generateButtonMessage(dataReservationRoomTypeDetail);
        }

        private void generateButtonMessage(TblReservationRoomTypeDetail dataReservationRoomTypeDetail) {
            btnMessage.getStyleClass().clear();
            String classCSSName = "button-mark-valid";
            String tooltipInfo = "";
            if (dataReservationRoomTypeDetail != null) {
                //1. Reservation Status : 'Reserved'
                if (dataReservationRoomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1) {  //Reserved = '1'
                    classCSSName = "button-mark-error";
                    tooltipInfo += "- Reservasi Kamar belum dibayar..!!\n";
                } else {
                    if (dataReservationRoomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
                        //2. Room Name : '-'
                        if (dataReservationRoomTypeDetail.getTblReservationCheckInOut() == null
                                || dataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() == null) {
                            classCSSName = "button-mark-error";
                            tooltipInfo += "- Nomor Kamar belum ditentukan..!!\n";
                        }
                        //3. CheckInOut Status : 'Ready To Check In'
                        if (dataReservationRoomTypeDetail.getTblReservationCheckInOut() == null
                                || dataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {   //Ready to Check In = '0'
                            classCSSName = "button-mark-error";
                            tooltipInfo += "- Belum CheckIn..!!\n";
                        }
                        //4. CheckInOut Status : 'Ready To Check Out'
                        if (dataReservationRoomTypeDetail.getTblReservationCheckInOut() == null
                                || dataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2) {   //Ready to Check Out = '2'
                            classCSSName = "button-mark-error";
                            tooltipInfo += "- Belum CheckOut..!!\n";
                        }
                    }
                }
                //valid..
                if (tooltipInfo.equals("")) {
                    tooltipInfo = "Data Telah Sesuai..!!";
                }
            }
            btnMessage.getStyleClass().add(classCSSName);
            Tooltip tooltip = new Tooltip(tooltipInfo);
            tooltip.setStyle("-fx-font-size: 9.5pt");
            btnMessage.setTooltip(tooltip);
            btnMessage.setPrefSize(25, 25);
        }

        public ObjectProperty<TblReservationRoomTypeDetail> dataReservationRoomTypeDetailProperty() {
            return dataReservationRoomTypeDetail;
        }

        public TblReservationRoomTypeDetail getDataReservationRoomTypeDetail() {
            return dataReservationRoomTypeDetailProperty().get();
        }

        public void setDataReservationRoomTypeDetail(TblReservationRoomTypeDetail dataReservationRoomTypeDetail) {
            dataReservationRoomTypeDetailProperty().set(dataReservationRoomTypeDetail);
        }

        public JFXButton getBtnMessage() {
            return btnMessage;
        }

        public void setBtnMessage(JFXButton btnMessage) {
            this.btnMessage = btnMessage;
        }

    }

    /**
     * L2 - Room Status (Data CheckIn/Out)
     */
    public List<DataRoomStatus> dataRoomWithStatuss = new ArrayList<>();

    private List<DataRoomStatus> loadAllDataRoomWithStatus() {
        List<DataRoomStatus> list = new ArrayList<>();
        LocalDate previousDate = (LocalDate.now()).minusDays(1);
        LocalDate nextDate = LocalDate.now();
        List<TblRoom> rooms = fEndOfDayManager.getAllDataRoom();
        for (TblRoom room : rooms) {
            //data room status
            room.setRefRoomStatus(fEndOfDayManager.getDataRoomStatus(room.getRefRoomStatus().getIdstatus()));
            //data room status
            DataRoomStatus data = new DataRoomStatus(room, previousDate, nextDate);
            if (data.getDataPreviousRRTD() != null
                    || data.getDataNextRRTD() != null) {
                list.add(data);
            }
        }
        return list;
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
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = fEndOfDayManager.getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = fEndOfDayManager.getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = fEndOfDayManager.getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(fEndOfDayManager.getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(fEndOfDayManager.getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(fEndOfDayManager.getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
                            //data reservation
                            rrtd.setTblReservation(fEndOfDayManager.getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(fEndOfDayManager.getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(fEndOfDayManager.getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
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
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = fEndOfDayManager.getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = fEndOfDayManager.getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = fEndOfDayManager.getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(fEndOfDayManager.getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(fEndOfDayManager.getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(fEndOfDayManager.getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
                            //data reservation
                            rrtd.setTblReservation(fEndOfDayManager.getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(fEndOfDayManager.getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(fEndOfDayManager.getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(fEndOfDayManager.getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
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

    /**
     * L3 - Daily Room Bill
     */
    private List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds = new ArrayList<>();

    private List<TblReservationAdditionalItem> rAdditionalItems = new ArrayList<>();

    private List<TblReservationAdditionalService> rAdditionalServices = new ArrayList<>();

    private List<TblReservationBrokenItem> rBrokenItems = new ArrayList<>();

    public List<DailyReservationRoomBill> dailyReservationRoomBills = new ArrayList<>();

    private List<DailyReservationRoomBill> loadAllDataReservationRoomBill() {
        List<DailyReservationRoomBill> list = new ArrayList<>();
        //refresh data
        rrtdrpds = new ArrayList<>();
        rAdditionalItems = new ArrayList<>();
        rAdditionalServices = new ArrayList<>();
        rBrokenItems = new ArrayList<>();
        dailyReservationRoomBills = new ArrayList<>();
        //room price
        List<TblReservationRoomTypeDetailRoomPriceDetail> dataRRTDRPDs = fEndOfDayManager.getAllDataReservationRoomTypeDetailRoomPriceDetailByDetailDate(Date.valueOf(eodDate));
        for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : dataRRTDRPDs) {
            //data reservation room type detail
            dataRRTDRPD.setTblReservationRoomTypeDetail(fEndOfDayManager.getDataReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail().getIddetail()));
            //data reservation room price detail
            dataRRTDRPD.setTblReservationRoomPriceDetail(fEndOfDayManager.getDataReservationRoomPriceDetail(dataRRTDRPD.getTblReservationRoomPriceDetail().getIddetail()));
            if ((dataRRTDRPD.getTblReservationRoomTypeDetail().getRefRecordStatus().getIdstatus() == 1
                    || dataRRTDRPD.getTblReservationRoomTypeDetail().getRefRecordStatus().getIdstatus() == 3)
                    && (dataRRTDRPD.getTblReservationRoomPriceDetail().getRefRecordStatus().getIdstatus() == 1
                    || dataRRTDRPD.getTblReservationRoomPriceDetail().getRefRecordStatus().getIdstatus() == 3)) {
                list.add(new DailyReservationRoomBill(dataRRTDRPD));
            }
        }
        //addtional service : breakfast (-1 day)
        List<TblReservationAdditionalService> dataRAdditionalItems = fEndOfDayManager.getAllDataReservationAdditionalServiceByIDRoomServiceAndAdditionalDate(
                1, //Breakfast = '1'
                Date.valueOf(eodDate.minusDays(1)));
        for (TblReservationAdditionalService dataRAdditionalItem : dataRAdditionalItems) {
            if (dataRAdditionalItem.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || dataRAdditionalItem.getRefReservationBillType().getIdtype() == 1) {  //CheckOut = '1'
                boolean found = false;
                for (DailyReservationRoomBill data : list) {
                    if (dataRAdditionalItem.getTblReservationRoomTypeDetail().getIddetail()
                            == data.getDataRRTDRPD().getTblReservationRoomTypeDetail().getIddetail()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail();
                    dataRRTDRPD.setTblReservationRoomTypeDetail(dataRAdditionalItem.getTblReservationRoomTypeDetail());
                    dataRRTDRPD.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail());
                    dataRRTDRPD.getTblReservationRoomPriceDetail().setDetailDate(Date.valueOf(eodDate));
                    list.add(new DailyReservationRoomBill(dataRRTDRPD));
                }
            }
        }
        //broken item
        List<TblReservationBrokenItem> dataBrokenItems = fEndOfDayManager.getAllDataReservationBrokenItemByCreateDate(Date.valueOf(eodDate));
        for (TblReservationBrokenItem dataBrokenItem : dataBrokenItems) {
            boolean found = false;
            for (DailyReservationRoomBill data : list) {
                if (dataBrokenItem.getTblReservationRoomTypeDetail().getIddetail()
                        == data.getDataRRTDRPD().getTblReservationRoomTypeDetail().getIddetail()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail();
                dataRRTDRPD.setTblReservationRoomTypeDetail(dataBrokenItem.getTblReservationRoomTypeDetail());
                dataRRTDRPD.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail());
                dataRRTDRPD.getTblReservationRoomPriceDetail().setDetailDate(Date.valueOf(eodDate));
                list.add(new DailyReservationRoomBill(dataRRTDRPD));
            }
        }
        return FXCollections.observableArrayList(list);
    }

    public class DailyReservationRoomBill {

        private final ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> dataRRTDRPD = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> dailyBill = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> currentBill = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> billAfterPosting = new SimpleObjectProperty<>();

        public DailyReservationRoomBill(TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD) {
            //data rrtdrpd
            this.dataRRTDRPD.set(dataRRTDRPD);
            //set all base data
            setAllData(dataRRTDRPD);
        }

        private void setAllData(TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD) {
            BigDecimal resultDailyBill = new BigDecimal("0");
            BigDecimal resultCurrentBill = new BigDecimal("0");
            //data room price
            if (dataRRTDRPD != null
                    && dataRRTDRPD.getTblReservationRoomPriceDetail().getIddetail() != 0L) {
                //daily bill
                BigDecimal roomPrice = ((new BigDecimal("1")).subtract((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
                        .multiply((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice().subtract(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary())));
                resultDailyBill = resultDailyBill.add(roomPrice);
                //data rrtdrpd - update
                rrtdrpds.add(dataRRTDRPD);
                //current bill
                BigDecimal roomPriceBills = new BigDecimal("0");
                List<TblReservationRoomTypeDetailRoomPriceDetail> dRRTDRPDs = fEndOfDayManager.getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail().getIddetail());
                for (TblReservationRoomTypeDetailRoomPriceDetail dRRTDRPD : dRRTDRPDs) {
                    if (dRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus() != null
                            && dRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
                        roomPriceBills = roomPriceBills
                                .add(((new BigDecimal("1")).subtract((dRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
                                        .multiply((dRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice().subtract(dRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary()))));
                    }
                }
                resultCurrentBill = resultCurrentBill.add(roomPriceBills);
            }
            //data additional item
            if (dataRRTDRPD != null
                    && dataRRTDRPD.getTblReservationRoomPriceDetail().getIddetail() != 0L) {
                List<TblReservationAdditionalItem> dAdditionalItems = fEndOfDayManager.getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail().getIddetail());
                //daily bill
                BigDecimal additionalPrice = new BigDecimal("0");
                for (TblReservationAdditionalItem dAdditionalItem : dAdditionalItems) {
                    if (dAdditionalItem.getAdditionalDate().compareTo(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate()) == 0
                            && (dAdditionalItem.getRefEndOfDayDataStatus() == null
                            || dAdditionalItem.getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
                        additionalPrice = additionalPrice
                                .add(((new BigDecimal("1")).subtract((dAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
                                        .multiply((dAdditionalItem.getItemCharge().multiply(dAdditionalItem.getItemQuantity()))));
                        //data additional item - update
                        rAdditionalItems.add(dAdditionalItem);
                    }
                }
                resultDailyBill = resultDailyBill.add(additionalPrice);
                //current bill
                BigDecimal additionalPrices = new BigDecimal("0");
                for (TblReservationAdditionalItem dAdditionalItem : dAdditionalItems) {
                    if (dAdditionalItem.getRefEndOfDayDataStatus() != null
                            && dAdditionalItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
                        additionalPrices = additionalPrices
                                .add(((new BigDecimal("1")).subtract((dAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
                                        .multiply((dAdditionalItem.getItemCharge().multiply(dAdditionalItem.getItemQuantity()))));
                    }
                }
                resultCurrentBill = resultCurrentBill.add(additionalPrices);
            }
            //data addtional service
            BigDecimal additionalRestoBill = new BigDecimal("0");
            BigDecimal additionalRestoBills = new BigDecimal("0");
            if (dataRRTDRPD != null //                    && dataRRTDRPD.getTblReservationRoomPriceDetail() == null
                    ) {
                List<TblReservationAdditionalService> dAdditionalServices = fEndOfDayManager.getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail().getIddetail());
                BigDecimal additionalPrice = new BigDecimal("0");
                //daily bill ~~~ !breakfast
                for (TblReservationAdditionalService dAdditionalService : dAdditionalServices) {
                    if (dAdditionalService.getAdditionalDate().compareTo(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate()) == 0
                            && dAdditionalService.getTblRoomService().getIdroomService() != 1 //Breakfast = '1'
                            && (dAdditionalService.getRefEndOfDayDataStatus() == null
                            || dAdditionalService.getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
                        if (dAdditionalService.getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                                || dAdditionalService.getTblRoomService().getIdroomService() == 7) { //Dine in Resto = '7'
                            additionalRestoBill = additionalRestoBill
                                    .add(((new BigDecimal("1")).subtract((dAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                                            .multiply((dAdditionalService.getPrice().multiply(new BigDecimal("1")))));
                        } else {
                            additionalPrice = additionalPrice
                                    .add(((new BigDecimal("1")).subtract((dAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                                            .multiply((dAdditionalService.getPrice().multiply(new BigDecimal("1")))));
                        }
                        //data additional service - update
                        rAdditionalServices.add(dAdditionalService);
                    }
                }
                resultDailyBill = resultDailyBill.add(additionalPrice);
                //daily bill ~~~ breakfast
                for (TblReservationAdditionalService dAdditionalService : dAdditionalServices) {
                    LocalDate additionalLocalDate = LocalDate.of(dAdditionalService.getAdditionalDate().getYear() + 1900,
                            dAdditionalService.getAdditionalDate().getMonth() + 1,
                            dAdditionalService.getAdditionalDate().getDate());
                    LocalDate detailLocalDate = LocalDate.of(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                            dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                            dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getDate());
                    if ((additionalLocalDate.plusDays(1)).compareTo(detailLocalDate) == 0
                            && dAdditionalService.getTblRoomService().getIdroomService() == 1 //Breakfast = '1'
                            && (dAdditionalService.getRefEndOfDayDataStatus() == null
                            || dAdditionalService.getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
                        additionalPrice = additionalPrice
                                .add(((new BigDecimal("1")).subtract((dAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                                        .multiply((dAdditionalService.getPrice().multiply(new BigDecimal("1")))));
                        //data additional service - update
                        rAdditionalServices.add(dAdditionalService);
                    }
                }
                resultDailyBill = resultDailyBill.add(additionalPrice);
                //current bill
                BigDecimal additionalPrices = new BigDecimal("0");
                for (TblReservationAdditionalService dAdditionalService : dAdditionalServices) {
                    if (dAdditionalService.getRefEndOfDayDataStatus() != null
                            && dAdditionalService.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'

                        if (dAdditionalService.getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                                || dAdditionalService.getTblRoomService().getIdroomService() == 7) { //Dine in Resto = '7'
                            additionalRestoBills = additionalPrices
                                    .add(((new BigDecimal("1")).subtract((dAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                                            .multiply((dAdditionalService.getPrice().multiply(new BigDecimal("1")))));
                        } else {
                            additionalPrices = additionalPrices
                                    .add(((new BigDecimal("1")).subtract((dAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                                            .multiply((dAdditionalService.getPrice().multiply(new BigDecimal("1")))));
                        }
                    }
                }
                resultCurrentBill = resultCurrentBill.add(additionalPrices);
            }
            //data broken item
            if (dataRRTDRPD != null //                    && dataRRTDRPD.getTblReservationRoomPriceDetail() == null
                    ) {
                List<TblReservationBrokenItem> dBrokenItems = fEndOfDayManager.getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail().getIddetail());
                //daily bill
                BigDecimal brokenPrice = new BigDecimal("0");
                for (TblReservationBrokenItem dataBrokenItem : dBrokenItems) {
                    if (dataBrokenItem.getCreateDate().compareTo(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate()) == 0
                            && (dataBrokenItem.getRefEndOfDayDataStatus() == null
                            || dataBrokenItem.getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
                        brokenPrice = brokenPrice
                                .add((new BigDecimal("1"))
                                        .multiply((dataBrokenItem.getItemCharge().multiply(dataBrokenItem.getItemQuantity()))));
                        //data broken item - update
                        rBrokenItems.add(dataBrokenItem);
                    }
                }
                resultDailyBill = resultDailyBill.add(brokenPrice);
                //current bill
                BigDecimal brokenPrices = new BigDecimal("0");
                for (TblReservationBrokenItem dataBrokenItem : dBrokenItems) {
                    if (dataBrokenItem.getRefEndOfDayDataStatus() != null
                            && dataBrokenItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
                        brokenPrices = brokenPrices
                                .add((new BigDecimal("1"))
                                        .multiply((dataBrokenItem.getItemCharge().multiply(dataBrokenItem.getItemQuantity()))));
                    }
                }
                resultCurrentBill = resultCurrentBill.add(brokenPrices);
            }
            //+sc +tax ~~~ reservation bill
            if (dataRRTDRPD != null) {
                TblReservationBill dataReservationBill = fEndOfDayManager.getDataReservationBillByIDReservationAndIDReservationBillType(
                        dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation(),
                        0); //Reservasi = '0'
                //+sc +tax ~~~ daily bill
                BigDecimal sc = new BigDecimal("0");
                BigDecimal tax = new BigDecimal("0");
                if (dataReservationBill != null) {
                    sc = resultDailyBill.multiply((dataReservationBill.getServiceChargePercentage()));
                    tax = (resultDailyBill.add(sc)).multiply(dataReservationBill.getTaxPercentage());
                }
                resultDailyBill = resultDailyBill.add(sc).add(tax);
                //+sc +tax ~~~ current bill
                sc = new BigDecimal("0");
                tax = new BigDecimal("0");
                if (dataReservationBill != null) {
                    sc = resultCurrentBill.multiply((dataReservationBill.getServiceChargePercentage()));
                    tax = (resultCurrentBill.add(sc)).multiply(dataReservationBill.getTaxPercentage());
                }
                resultCurrentBill = resultCurrentBill.add(sc).add(tax);
            }
            //+resto bill ~~~ reservation bill
            if (dataRRTDRPD != null) {
                //+resto bill ~~~ daily bill
                resultDailyBill = resultDailyBill.add(additionalRestoBill);
                //+resto bill ~~~ current bill
                resultCurrentBill = resultCurrentBill.add(additionalRestoBills);
            }
            //daily bill
            dailyBill.set(resultDailyBill);
            //current bill
            currentBill.set(resultCurrentBill);
            //bill after posting
            billAfterPosting.set(resultDailyBill.add(resultCurrentBill));
        }

        public ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> dataRRTDRPDProperty() {
            return dataRRTDRPD;
        }

        public TblReservationRoomTypeDetailRoomPriceDetail getDataRRTDRPD() {
            return dataRRTDRPDProperty().get();
        }

        public void setDataRRTDRPD(TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD) {
            dataRRTDRPDProperty().set(dataRRTDRPD);
        }

        public ObjectProperty<BigDecimal> dailyBillProperty() {
            return dailyBill;
        }

        public BigDecimal getDailyBill() {
            return dailyBillProperty().get();
        }

        public void setDailyBill(BigDecimal dailyBill) {
            dailyBillProperty().set(dailyBill);
        }

        public ObjectProperty<BigDecimal> currentBillProperty() {
            return currentBill;
        }

        public BigDecimal getCurrentBill() {
            return currentBillProperty().get();
        }

        public void setCurrentBill(BigDecimal currentBill) {
            currentBillProperty().set(currentBill);
        }

        public ObjectProperty<BigDecimal> billAfterPostingProperty() {
            return billAfterPosting;
        }

        public BigDecimal getBillAfterPosting() {
            return billAfterPostingProperty().get();
        }

        public void setBillAfterPosting(BigDecimal billAfterPosting) {
            billAfterPostingProperty().set(billAfterPosting);
        }

    }

//    public class DailyReservationRoomBill {
//
//        private final ObjectProperty<TblRoom> dataRoom = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<TblReservationRoomTypeDetail> dataRRTD = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<BigDecimal> dailyBill = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<BigDecimal> currentBill = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<BigDecimal> billAfterPosting = new SimpleObjectProperty<>();
//
//        DailyReservationRoomBill(TblRoom dataRoom,
//                LocalDate date) {
//            //data room
//            this.dataRoom.set(dataRoom);
//            //data base
//            setDataBaseData(this.dataRoom.get(), Date.valueOf(date));
//        }
//
//        private void setDataBaseData(TblRoom dataRoom,
//                Date date) {
//            if (dataRoom != null
//                    && date != null) {
//                List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = fEndOfDayManager.getAllDataReservationRoomPriceDetailByDetailDate(date);
//                for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
//                    TblReservationRoomTypeDetailRoomPriceDetail dataRelation = fEndOfDayManager.getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
//                    if (dataRelation != null
//                            && dataRelation.getTblReservationRoomTypeDetail() != null) {
//                        TblReservationRoomTypeDetail rrtd = fEndOfDayManager.getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
//                        if (rrtd != null
//                                && rrtd.getTblReservationCheckInOut() != null
//                                && rrtd.getTblReservationCheckInOut().getTblRoom() != null
//                                && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
//                            //data checkin-out
//                            rrtd.setTblReservationCheckInOut(fEndOfDayManager.getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
//                            //data room
//                            rrtd.getTblReservationCheckInOut().setTblRoom(fEndOfDayManager.getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
//                            //data checkin/out - status
//                            rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(fEndOfDayManager.getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
//                            if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
//                                    && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2 //Checked Out = '2'
//                                    || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
//                                    || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
//                                //data reservation
//                                rrtd.setTblReservation(fEndOfDayManager.getDataReservation(rrtd.getTblReservation().getIdreservation()));
//                                //data reservation - status
//                                rrtd.getTblReservation().setRefReservationStatus(fEndOfDayManager.getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
//                                if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
//                                        || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2 //Booked = '2'
//                                        || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 5) {  //Checked Out = '5'
//                                    //data customer
//                                    rrtd.getTblReservation().setTblCustomer(fEndOfDayManager.getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
//                                    //data people
//                                    rrtd.getTblReservation().getTblCustomer().setTblPeople(fEndOfDayManager.getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
//                                    //set rrtd - rpd
//                                    dataRelation.setTblReservationRoomTypeDetail(rrtd);
//                                    //set base data
//                                    setDataRRTD(rrtd);
//                                    setDailyBill(getDailyBill(rrtd, date));
//                                    setCurrentBill(getCurrentBill(rrtd));
//                                    setBillAfterPosting(getCurrentBill().add(getDailyBill()));
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                //set base data
//                setDataRRTD(null);
//                setDailyBill(new BigDecimal("0"));
//                setCurrentBill(new BigDecimal("0"));
//                setBillAfterPosting(new BigDecimal("0"));
//            }
//        }
//
//        private BigDecimal getDailyBill(TblReservationRoomTypeDetail dataRRTD, Date date) {
//            BigDecimal result = new BigDecimal("0");
//            //room
//            List<TblReservationRoomTypeDetailRoomPriceDetail> dataRRTDRPDs = fEndOfDayManager.getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : dataRRTDRPDs) {
//                if (dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().compareTo(date) == 0
//                        && (dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus() == null
//                        || dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
//                    BigDecimal roomPrice = ((new BigDecimal("1")).subtract((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice().subtract(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary())));
//                    result = result.add(roomPrice);
//                    break;
//                }
//            }
//            //additional item
//            List<TblReservationAdditionalItem> dataAdditionalItems = fEndOfDayManager.getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationAdditionalItem dataAdditionalItem : dataAdditionalItems) {
//                if (dataAdditionalItem.getAdditionalDate().compareTo(date) == 0
//                        && (dataAdditionalItem.getRefEndOfDayDataStatus() == null
//                        || dataAdditionalItem.getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
//                    BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity())));
//                    result = result.add(additionalPrice);
//                }
//            }
//            //additional service
//            List<TblReservationAdditionalService> dataAdditionalServices = fEndOfDayManager.getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationAdditionalService dataAdditionalService : dataAdditionalServices) {
//                if (dataAdditionalService.getAdditionalDate().compareTo(date) == 0
//                        && (dataAdditionalService.getRefEndOfDayDataStatus() == null
//                        || dataAdditionalService.getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
//                    BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataAdditionalService.getPrice().multiply(new BigDecimal("1"))));
//                    result = result.add(additionalPrice);
//                }
//            }
//            //broken item
//            List<TblReservationBrokenItem> dataBrokenItems = fEndOfDayManager.getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationBrokenItem dataBrokenItem : dataBrokenItems) {
//                if (dataBrokenItem.getCreateDate().compareTo(date) == 0
//                        && (dataBrokenItem.getRefEndOfDayDataStatus() == null
//                        || dataBrokenItem.getRefEndOfDayDataStatus().getIdstatus() == 0)) { //Uncheck = '0'
//                    BigDecimal additionalPrice = (new BigDecimal("1"))
//                            .multiply((dataBrokenItem.getItemCharge().multiply(dataBrokenItem.getItemQuantity())));
//                    result = result.add(additionalPrice);
//                }
//            }
//            //+sc +tax
//            BigDecimal sc = new BigDecimal("0");
//            BigDecimal tax = new BigDecimal("0");
//            TblReservationBill dataReservationBill = fEndOfDayManager.getDataReservationBillByIDReservationAndIDReservationBillType(
//                    dataRRTD.getTblReservation().getIdreservation(),
//                    0); //Reservasi = '0'
//            if (dataReservationBill != null) {
//                sc = result.multiply((dataReservationBill.getServiceChargePercentage()));
//                tax = (result.add(sc)).multiply(dataReservationBill.getTaxPercentage());
//            }
//            return result.add(sc).add(tax);
//        }
//
//        private BigDecimal getCurrentBill(TblReservationRoomTypeDetail dataRRTD) {
//            BigDecimal result = new BigDecimal("0");
//            //room
//            List<TblReservationRoomTypeDetailRoomPriceDetail> dataRRTDRPDs = fEndOfDayManager.getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : dataRRTDRPDs) {
//                if (dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus() != null
//                        && dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
//                    BigDecimal roomPrice = ((new BigDecimal("1")).subtract((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice().subtract(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary())));
//                    result = result.add(roomPrice);
//                }
//            }
//            //additional item
//            List<TblReservationAdditionalItem> dataAdditionalItems = fEndOfDayManager.getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationAdditionalItem dataAdditionalItem : dataAdditionalItems) {
//                if (dataAdditionalItem.getRefEndOfDayDataStatus() != null
//                        && dataAdditionalItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
//                    BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity())));
//                    result = result.add(additionalPrice);
//                }
//            }
//            //additional service
//            List<TblReservationAdditionalService> dataAdditionalServices = fEndOfDayManager.getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationAdditionalService dataAdditionalService : dataAdditionalServices) {
//                if (dataAdditionalService.getRefEndOfDayDataStatus() != null
//                        && dataAdditionalService.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
//                    BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataAdditionalService.getPrice().multiply(new BigDecimal("1"))));
//                    result = result.add(additionalPrice);
//                }
//            }
//            //broken item
//            List<TblReservationBrokenItem> dataBrokenItems = fEndOfDayManager.getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
//            for (TblReservationBrokenItem dataBrokenItem : dataBrokenItems) {
//                if (dataBrokenItem.getRefEndOfDayDataStatus() != null
//                        && dataBrokenItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
//                    BigDecimal additionalPrice = (new BigDecimal("1"))
//                            .multiply((dataBrokenItem.getItemCharge().multiply(dataBrokenItem.getItemQuantity())));
//                    result = result.add(additionalPrice);
//                }
//            }
//            //+sc +tax
//
//            return result;
//        }
//
//        public ObjectProperty<TblRoom> dataRoomProperty() {
//            return dataRoom;
//        }
//
//        public TblRoom getDataRoom() {
//            return dataRoomProperty().get();
//        }
//
//        public void setDataRoom(TblRoom dataRoom) {
//            dataRoomProperty().set(dataRoom);
//        }
//
//        public ObjectProperty<TblReservationRoomTypeDetail> dataRRTDProperty() {
//            return dataRRTD;
//        }
//
//        public TblReservationRoomTypeDetail getDataRRTD() {
//            return dataRRTDProperty().get();
//        }
//
//        public void setDataRRTD(TblReservationRoomTypeDetail dataRRTD) {
//            dataRRTDProperty().set(dataRRTD);
//        }
//
//        public ObjectProperty<BigDecimal> dailyBillProperty() {
//            return dailyBill;
//        }
//
//        public BigDecimal getDailyBill() {
//            return dailyBillProperty().get();
//        }
//
//        public void setDailyBill(BigDecimal dailyBill) {
//            dailyBillProperty().set(dailyBill);
//        }
//
//        public ObjectProperty<BigDecimal> currentBillProperty() {
//            return currentBill;
//        }
//
//        public BigDecimal getCurrentBill() {
//            return currentBillProperty().get();
//        }
//
//        public void setCurrentBill(BigDecimal currentBill) {
//            currentBillProperty().set(currentBill);
//        }
//
//        public ObjectProperty<BigDecimal> billAfterPostingProperty() {
//            return billAfterPosting;
//        }
//
//        public BigDecimal getBillAfterPosting() {
//            return billAfterPostingProperty().get();
//        }
//
//        public void setBillAfterPosting(BigDecimal billAfterPosting) {
//            billAfterPostingProperty().set(billAfterPosting);
//        }
//
//    }
    /**
     * LISTENER FOR EACH BUTTON goTo : "previous or next"
     */
    private void doButtonAction(String goTo, int currentLayoutNumber) {
        runnigAnimation = true;
        double target;
        KeyValue keyValue;
        Timeline timeline;
        switch (goTo) {
            case "previous":
                switch (currentLayoutNumber) {
                    case 1:
                        break;
                    case 2:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(0).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(1, 1, 1, 1);
                        break;
                    case 3:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(1).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(0, 1, 1, 1);
                        break;
                    case 4:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(2).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(0, 0, 1, 1);
                        break;
                    case 5:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(3).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(0, 0, 0, 1);
                        break;
                    case 6:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(4).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 7:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(5).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 8:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(6).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 9:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(7).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 10:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(8).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 11:
                        target = 1.0;
                        keyValue = new KeyValue(spData.getDividers().get(9).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                }
                break;
            case "next":
                switch (currentLayoutNumber) {
                    case 1:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(0).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(0, 1, 1, 1);
                        break;
                    case 2:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(1).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(0, 0, 1, 1);
                        break;
                    case 3:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(2).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(0, 0, 0, 1);
                        break;
                    case 4:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(3).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
//                        spData.setDividerPositions(0, 0, 0, 0);
                        break;
                    case 5:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(4).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 6:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(5).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 7:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(6).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 8:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(7).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 9:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(8).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 10:
                        target = 0.0;
                        keyValue = new KeyValue(spData.getDividers().get(9).positionProperty(), target);
                        timeline = new Timeline(new KeyFrame(Duration.millis(200), keyValue));
                        timeline.play();
                        break;
                    case 11:
                        break;
                }
                break;
            default:
                break;
        }
        runnigAnimation = false;
    }

    private void initAllLayoutFuntionButton() {
        btnNext1.setOnAction((e) -> {
            doButtonAction("next", 1);
        });

        btnPrevious2.setOnAction((e) -> {
            doButtonAction("previous", 2);
        });
        btnNext2.setOnAction((e) -> {
            doButtonAction("next", 2);
        });

        btnPrevious3.setOnAction((e) -> {
            doButtonAction("previous", 3);
        });
        btnNext3.setOnAction((e) -> {
            doButtonAction("next", 3);
        });

        btnPrevious4.setOnAction((e) -> {
            doButtonAction("previous", 4);
        });
        btnNext4.setOnAction((e) -> {
            doButtonAction("next", 4);
        });

        btnPrevious5.setOnAction((e) -> {
            doButtonAction("previous", 5);
        });
        btnNext5.setOnAction((e) -> {
            doButtonAction("next", 5);
        });

        btnPrevious6.setOnAction((e) -> {
            doButtonAction("previous", 6);
        });
        btnNext6.setOnAction((e) -> {
            doButtonAction("next", 6);
        });

        btnPrevious7.setOnAction((e) -> {
            doButtonAction("previous", 7);
        });
        btnNext7.setOnAction((e) -> {
            doButtonAction("next", 7);
        });

        btnPrevious8.setOnAction((e) -> {
            doButtonAction("previous", 8);
        });
        btnNext8.setOnAction((e) -> {
            doButtonAction("next", 8);
        });

        btnPrevious9.setOnAction((e) -> {
            doButtonAction("previous", 9);
        });
        btnNext9.setOnAction((e) -> {
            doButtonAction("next", 9);
        });

        btnPrevious10.setOnAction((e) -> {
            doButtonAction("previous", 10);
        });
        btnNext10.setOnAction((e) -> {
            doButtonAction("next", 10);
        });

        btnPrevious11.setOnAction((e) -> {
            doButtonAction("previous", 11);
        });
//        btnNext11.setOnAction((e) -> {
//            doButtonAction("next", 11);
//        });
    }

    /**
     * DATA EACH LAYOUT
     */
    private void initAllDataLayout() {
        //set data reservation layout
        setReservationDataLayout();
//        //set data room checkin/checkout
//        setRoomCheckInOutDataLayout();
        //set data daily reservation room bill
        setDailyReservationRoomBillLayout();
    }

    private void setReservationDataLayout() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_end_of_day/L1/EODL1View.fxml"));

            //set controller
            EODL1Controller eodL1Controller = new EODL1Controller(this);
            loader.setController(eodL1Controller);

            Node reservationDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(reservationDataContent, 0.0);
            AnchorPane.setLeftAnchor(reservationDataContent, 0.0);
            AnchorPane.setRightAnchor(reservationDataContent, 0.0);
            AnchorPane.setBottomAnchor(reservationDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancLayoutL1.getChildren().clear();
            ancLayoutL1.getChildren().add(reservationDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private void setRoomCheckInOutDataLayout() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_end_of_day/L2/EODL2View.fxml"));

            //set controller
            EODL2Controller eodL2Controller = new EODL2Controller(this);
            loader.setController(eodL2Controller);

            Node roomCheckInOutDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(roomCheckInOutDataContent, 0.0);
            AnchorPane.setLeftAnchor(roomCheckInOutDataContent, 0.0);
            AnchorPane.setRightAnchor(roomCheckInOutDataContent, 0.0);
            AnchorPane.setBottomAnchor(roomCheckInOutDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancLayoutL2.getChildren().clear();
            ancLayoutL2.getChildren().add(roomCheckInOutDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private void setDailyReservationRoomBillLayout() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_end_of_day/L3/EODL3View.fxml"));

            //set controller
            EODL3Controller eodL3Controller = new EODL3Controller(this);
            loader.setController(eodL3Controller);

            Node dailyReservationRoomBillDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(dailyReservationRoomBillDataContent, 0.0);
            AnchorPane.setLeftAnchor(dailyReservationRoomBillDataContent, 0.0);
            AnchorPane.setRightAnchor(dailyReservationRoomBillDataContent, 0.0);
            AnchorPane.setBottomAnchor(dailyReservationRoomBillDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancLayoutL2.getChildren().clear();
            ancLayoutL2.getChildren().add(dailyReservationRoomBillDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private Time defaultCheckInTime;

    private Time defaultCheckOutTime;

    private void setDefaultCheckInOutTime() {
        //sys data hardcode (default checkin time)
        defaultCheckInTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckInTime = fEndOfDayManager.getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
        if (sdhDefaultCheckInTime != null
                && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
            String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
            defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                    Integer.parseInt(dataCheckInTime[1]),
                    Integer.parseInt(dataCheckInTime[2]));
        }
        //sys data hardcode (default checkout time)
        defaultCheckOutTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckOutTime = fEndOfDayManager.getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
        if (sdhDefaultCheckOutTime != null
                && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
            String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
            defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                    Integer.parseInt(dataCheckOutTime[1]),
                    Integer.parseInt(dataCheckOutTime[2]));
        }
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FEndOfDayManager fEndOfDayManager;

    private SysCurrentHotelDate schd;

    public LocalDate eodDate;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service
        fEndOfDayManager = new FEndOfDayManagerImpl();
        //set eod date
        schd = fEndOfDayManager.getDataSysCurrentHotelDate(6);  //End of Day = '6'
        eodDate = LocalDate.of(schd.getCurrentHotelDate().getYear() + 1900,
                schd.getCurrentHotelDate().getMonth() + 1,
                schd.getCurrentHotelDate().getDate());
        if (eodDate == null) {
            eodDate = LocalDate.now();
        }
        //set default checkin/out time
        setDefaultCheckInOutTime();
        //refresh all data
        refreshAllData();
        //set closing EoD button function
        btnClosingEoD.setTooltip(new Tooltip("Posting Data"));
        btnClosingEoD.setOnMouseClicked((e) -> {
            closingEoDHandle();
        });
        //init split pane
        initSplitPaneFunction();
        //init prev and next button
        initAllLayoutFuntionButton();
        //init data each layout
        initAllDataLayout();
    }

    public FEndOfDayManager getService() {
        return this.fEndOfDayManager;
    }

}
