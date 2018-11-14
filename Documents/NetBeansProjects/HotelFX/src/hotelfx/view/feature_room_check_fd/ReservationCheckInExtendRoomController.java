/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillDiscountType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservation;
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
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblRoomTypeRoomService;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ReservationCheckInExtendRoomController implements Initializable {

    @FXML
    private AnchorPane ancFormCheckInExtendRoom;

    @FXML
    private GridPane gpFormDataCheckInExtendRoom;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXDatePicker dtpCheckOutDate;

    @FXML
    private Label lblDateCount;

    @FXML
    private Spinner<Integer> spDateCount;

    @FXML
    private JFXTextField txtTotalRoomCost;

    @FXML
    private JFXTextField txtTotalDiscount;

    @FXML
    private JFXTextField txtTotalServiceCharge;

    @FXML
    private JFXTextField txtTotalTax;

    @FXML
    private JFXTextField txtTotalBillExtendRoom;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCheckInExtendRoom() {
        //sys data hardcode (default checkout time)
        defaultCheckOutTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckOutTime = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
        if (sdhDefaultCheckOutTime != null
                && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
            String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
            defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                    Integer.parseInt(dataCheckOutTime[1]),
                    Integer.parseInt(dataCheckOutTime[2]));
        }

        //Travel Agent
        if (roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getTblPartner() != null) {   //Travel Agent
            reservationType = roomStatusFDController.getServiceRV2().getReservationOrderByType(1);
        } else {  //Customer
            reservationType = roomStatusFDController.getServiceRV2().getReservationOrderByType(0);
        }
        dataReservationBill = roomStatusFDController.getServiceRV2().getReservationBillByIDReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getIdreservation());
        selectedDataCheckIn = roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut();
        selectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        selectedDataRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();

        btnSave.setTooltip(new Tooltip("Simpan (Data Tambahan Extend Kamar)"));
        btnSave.setOnAction((e) -> {
            dataCheckInExtendRoomSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCheckInExtendRoomCancelHandle();
        });

        spDateCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spDateCount.setEditable(false);
        spDateCount.getValueFactory().setValue(0);

        spDateCount.valueProperty().addListener((obs, oldVal, newVal) -> {
            refreshDataCheckInOutDayNumber(dtpCheckOutDate.getValue(),
                    newVal,
                    "day-number");
            refreshDataBill();
        });

        dtpCheckOutDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            refreshDataCheckInOutDayNumber(newVal,
                    spDateCount.getValue(),
                    "checkout-date");
            refreshDataBill();
        });

        initDateCalendar();

        initImportantFieldColor();

        initNumbericField();
    }

    private void refreshDataCheckInOutDayNumber(
            LocalDate checkOutDate,
            int dayNumber,
            String newVal) {
        int reCountDayNumber;
        switch (newVal) {
            case "checkout-date":
                if (roomStatusFDController.extendRoomCheckOutDate.get() != null
                        && checkOutDate != null) {
                    reCountDayNumber = (int) ChronoUnit.DAYS.between(roomStatusFDController.extendRoomCheckOutDate.get(), checkOutDate);
                    spDateCount.getValueFactory().setValue(reCountDayNumber);
                }
                break;
            case "day-number":
                if (roomStatusFDController.extendRoomCheckOutDate.get() != null
                        && checkOutDate != null) {
                    dtpCheckOutDate.setValue(roomStatusFDController.extendRoomCheckOutDate.get().plusDays(dayNumber));
                }
                break;
            default:
                break;
        }
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpCheckOutDate);
        ClassFormatter.setDatePickersEnableDateFrom(roomStatusFDController.extendRoomCheckOutDate.get(),
                dtpCheckOutDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dtpCheckOutDate,
                lblDateCount,
                spDateCount);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal");
    }

    public Time defaultCheckOutTime;

    public RefReservationOrderByType reservationType;

    private TblReservationBill dataReservationBill;

    private TblReservationCheckInOut selectedDataCheckIn;

    private List<TblReservationRoomTypeDetailRoomPriceDetail> selectedDataRoomTypeDetailRoomPriceDetails;

    private List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> selectedDataRoomTypeDetailTravelAgentDiscountDetails;

    private List<TblReservationAdditionalItem> selectedDataAdditionalItems = new ArrayList<>();

    private List<TblReservationAdditionalService> selectedDataAdditionalServices = new ArrayList<>();

    private void setSelectedDataToInputForm() {
        txtRoomName.textProperty().bind(selectedDataCheckIn.getTblRoom().roomNameProperty());

        dtpCheckOutDate.setValue(roomStatusFDController.extendRoomCheckOutDate.get());

        refreshDataBill();
    }

    private void refreshDataBill() {
        //refresh data reservation room type detail  - room price detail (s)
        refreshDataReservationRoomTypeDetailRoomPriceDetails();

        txtTotalRoomCost.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalRoomCost()));
        txtTotalDiscount.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount()));
        txtTotalServiceCharge.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge()));
        txtTotalTax.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax()));
        txtTotalBillExtendRoom.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillExtendRoom()));
    }

    private void refreshDataReservationRoomTypeDetailRoomPriceDetails() {
        selectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            LocalDate countDate = roomStatusFDController.extendRoomCheckOutDate.getValue().minusDays(0);
            while (countDate.isBefore(dtpCheckOutDate.getValue())) {
                //reservation room price detail
                TblReservationRoomPriceDetail dataRPD = new TblReservationRoomPriceDetail();
                dataRPD.setDetailDate(Date.valueOf(countDate));
                dataRPD.setDetailPrice(getRoomPrice(
                        reservationType,
                        roomStatusFDController.selectedDataRoomTypeDetail.getTblRoomType(),
                        countDate));
                //reservation room price detail - discount
                setReservationRoomPriceDetailDiscount(
                        dataRPD,
                        roomStatusFDController.selectedDataRoomTypeDetail,
                        dataReservationBill);
                //reservation room price detail - complimentary
                dataRPD.setDetailComplimentary(new BigDecimal("0"));
                //reservation room type detail - reservation room price detail
                TblReservationRoomTypeDetailRoomPriceDetail dataRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail();
                dataRTDRPD.setTblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
                dataRTDRPD.setTblReservationRoomPriceDetail(dataRPD);
                //add to list (reservation room type detail - reservation room price detail)
                selectedDataRoomTypeDetailRoomPriceDetails.add(dataRTDRPD);
                //increment countDate
                countDate = countDate.plusDays(1);
            }
        }
    }

    private BigDecimal getRoomPrice(RefReservationOrderByType reservationType,
            TblRoomType roomType,
            LocalDate date) {
        BigDecimal result = new BigDecimal("0");
        TblReservationBar dataBAR;
        TblReservationCalendarBar calendarBAR = roomStatusFDController.getServiceRV2().getReservationCalendarBARByCalendarDate(Date.valueOf(date));
        if (calendarBAR == null) {
            TblReservationDefaultBar defaultBAR = roomStatusFDController.getServiceRV2().getReservationDefaultBARByDayOfWeek(date.getDayOfWeek().getValue());
            dataBAR = roomStatusFDController.getServiceRV2().getReservationBAR(defaultBAR.getTblReservationBar().getIdbar());
        } else {
            dataBAR = roomStatusFDController.getServiceRV2().getReservationBAR(calendarBAR.getTblReservationBar().getIdbar());
        }
        result = roomType.getRoomTypePrice().multiply(dataBAR.getBarpercentage());
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
                    List<TblHotelEvent> dataHotelEvents = roomStatusFDController.getServiceRV2().getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(dataReservationRoomPriceDetail.getDetailDate());
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
                        List<TblBankEventCard> dataCardEvents = roomStatusFDController.getServiceRV2().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
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

    private BigDecimal calculationTotalRoomCost() {
        BigDecimal result = new BigDecimal("0");
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            for (TblReservationRoomTypeDetailRoomPriceDetail data : selectedDataRoomTypeDetailRoomPriceDetails) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
            }
        }
        return result;
    }

    private BigDecimal calculationTotalDiscount() {
        BigDecimal result = new BigDecimal("0");
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            for (TblReservationRoomTypeDetailRoomPriceDetail data : selectedDataRoomTypeDetailRoomPriceDetails) {
                result = result
                        .add((data.getTblReservationRoomPriceDetail().getDetailPrice()
                                .multiply((data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()
                                        .divide(new BigDecimal("100"))))));
            }
        }
        return result;
    }

    private BigDecimal calculationTotalServiceCharge() {
        BigDecimal result = new BigDecimal("0");
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            result = (calculationTotalRoomCost()
                    .subtract(calculationTotalDiscount()))
                    .multiply((dataReservationBill.getServiceChargePercentage()));
        }
        return result;
    }

    private BigDecimal calculationTotalTax() {
        BigDecimal result = new BigDecimal("0");
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            result = (calculationTotalRoomCost()
                    .subtract(calculationTotalDiscount()
                            .add(calculationTotalServiceCharge())))
                    .multiply((dataReservationBill.getTaxPercentage()));
        }
        return result;
    }

    private BigDecimal calculationTotalBillExtendRoom() {
        BigDecimal result = new BigDecimal("0");
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            result = calculationTotalRoomCost()
                    .subtract(calculationTotalDiscount())
                    .add(calculationTotalServiceCharge())
                    .add(calculationTotalTax());
        }
        return result;
    }

    private void dataCheckInExtendRoomSaveHandle() {
        if (checkDataInputDataCheckInExtendRoom()) {
            if (checkDataInputDataCheckInExtendRoomAvailableRoom()) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusFDController.dialogStage);
                if (alert.getResult() == ButtonType.OK) {
                    //data reservation room type detail
                    roomStatusFDController.selectedDataRoomTypeDetail.setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.of(dtpCheckOutDate.getValue(), defaultCheckOutTime.toLocalTime())));
                    //data reservation room type detail - reservation checkin/out
                    roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusFDController.getServiceRV2().getReservationCheckInOutStatus(1)); //Checked In = '1'
                    //data reservation room type detail - rervation room price detail
                    //....
                    selectedDataAdditionalServices = new ArrayList<>();
                    for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : selectedDataRoomTypeDetailRoomPriceDetails) {
                        //data reservation additional service (included)
                        List<TblRoomTypeRoomService> dataRTRSs = roomStatusFDController.getServiceRV2().getAllDataRoomTypeRoomServiceByIDRoomType(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType());
                        for (TblRoomTypeRoomService dataRTRS : dataRTRSs) {
                            if (dataRTRS.getAddAsAdditionalService()) {
                                if (dataRTRS.getPeopleNumber() == 0) {
                                    //data additional service
                                    TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                                    dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail());
                                    dataAdditionalService.setTblRoomService(new TblRoomService(dataRTRS.getTblRoomService()));
                                    dataAdditionalService.setAdditionalDate(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
                                    dataAdditionalService.setPrice(new BigDecimal("0"));
                                    dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                    dataAdditionalService.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(4));  //Include = '4'
                                    //add to list (reservation additional service : include)
                                    selectedDataAdditionalServices.add(dataAdditionalService);
                                } else {
                                    for (int peopleNumber = 0; peopleNumber < dataRTRS.getPeopleNumber(); peopleNumber++) {
                                        //data additional service
                                        TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                                        dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail());
                                        dataAdditionalService.setTblRoomService(new TblRoomService(dataRTRS.getTblRoomService()));
                                        dataAdditionalService.setAdditionalDate(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
                                        dataAdditionalService.setPrice(new BigDecimal("0"));
                                        dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                        dataAdditionalService.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(4));  //Include = '4'
                                        //add to list (reservation additional service : include)
                                        selectedDataAdditionalServices.add(dataAdditionalService);
                                    }
                                }
                            }
                        }
                        //data reservation additional item (included)
                        selectedDataAdditionalItems = new ArrayList<>();
                        List<TblRoomTypeItem> dataRTIs = roomStatusFDController.getServiceRV2().getAllDataRoomTypeItemByIDRoomType(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType());
                        for (TblRoomTypeItem dataRTI : dataRTIs) {
                            if (dataRTI.getAddAsAdditionalItem()) {
                                //data additional item
                                TblReservationAdditionalItem dataAddtionalItem = new TblReservationAdditionalItem();
                                dataAddtionalItem.setTblReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail());
                                dataAddtionalItem.setTblItem(new TblItem(dataRTI.getTblItem()));
                                dataAddtionalItem.setAdditionalDate(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
                                dataAddtionalItem.setItemCharge(new BigDecimal("0"));
                                dataAddtionalItem.setItemQuantity(dataRTI.getItemQuantity());
                                dataAddtionalItem.setDiscountPercentage(new BigDecimal("0"));
                                dataAddtionalItem.setTaxable(dataAddtionalItem.getTblItem().getTaxable());
                                dataAddtionalItem.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(4));  //Include = '4'
                                //add to list (reservation additional item : include)
                                selectedDataAdditionalItems.add(dataAddtionalItem);
                            }
                        }
                    }
                    //data reservation room type detail - service
                    //...service
                    //data reservation room type detail - item
                    //...service
                    //dummy entry
                    TblReservation dummySelectedData = new TblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
                    TblReservationBill dummyDataReservationBill = new TblReservationBill(dataReservationBill);
                    dummyDataReservationBill.setTblReservation(dummySelectedData);
                    dummyDataReservationBill.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill.getRefReservationBillType()));
                    if (dummyDataReservationBill.getTblBankCard() != null) {
                        dummyDataReservationBill.setTblBankCard(new TblBankCard(dummyDataReservationBill.getTblBankCard()));
                    }
                    if (dummyDataReservationBill.getRefReservationBillDiscountType() != null) {
                        dummyDataReservationBill.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill.getRefReservationBillDiscountType()));
                    }
                    TblReservationRoomTypeDetail dummySelectedDataRoomTypeDetail = new TblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
                    dummySelectedDataRoomTypeDetail.setTblReservation(dummySelectedData);
                    dummySelectedDataRoomTypeDetail.setTblRoomType(new TblRoomType(dummySelectedDataRoomTypeDetail.getTblRoomType()));
                    if (dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut() != null) {
                        dummySelectedDataRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut()));
                        if (dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() != null) {
                            dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom()));
                            dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                        }
                        dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                    }
                    List<TblReservationRoomTypeDetailRoomPriceDetail> dummySelectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
                    List<TblReservationAdditionalItem> dummySelectedDataReservationAdditionalItems = new ArrayList<>();
                    List<TblReservationAdditionalService> dummySelectedDataReservationAdditionalServices = new ArrayList<>();
                    for (TblReservationRoomTypeDetailRoomPriceDetail selectedDataRoomTypeDetailRoomPriceDetail : selectedDataRoomTypeDetailRoomPriceDetails) {
                        TblReservationRoomTypeDetailRoomPriceDetail dummySelectedDataRoomTypeDetailRoomPriceDetail = new TblReservationRoomTypeDetailRoomPriceDetail(selectedDataRoomTypeDetailRoomPriceDetail);
                        dummySelectedDataRoomTypeDetailRoomPriceDetail.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail);
                        dummySelectedDataRoomTypeDetailRoomPriceDetail.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummySelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail()));
                        if (dummySelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                            dummySelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummySelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                        }
                        if (dummySelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                            dummySelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummySelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                        }
                        dummySelectedDataRoomTypeDetailRoomPriceDetails.add(dummySelectedDataRoomTypeDetailRoomPriceDetail);
                    }
                    for (TblReservationAdditionalItem dataSelectedReservationAdditionalItem : selectedDataAdditionalItems) {
                        TblReservationAdditionalItem dummySelectedDataReservationAdditionalItem = new TblReservationAdditionalItem(dataSelectedReservationAdditionalItem);
                        dummySelectedDataReservationAdditionalItem.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail);
                        dummySelectedDataReservationAdditionalItem.setTblItem(new TblItem(dummySelectedDataReservationAdditionalItem.getTblItem()));
                        dummySelectedDataReservationAdditionalItem.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalItem.getRefReservationBillType()));
                        if (dummySelectedDataReservationAdditionalItem.getTblHotelEvent() != null) {
                            dummySelectedDataReservationAdditionalItem.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalItem.getTblHotelEvent()));
                        }
                        if (dummySelectedDataReservationAdditionalItem.getTblBankEventCard() != null) {
                            dummySelectedDataReservationAdditionalItem.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalItem.getTblBankEventCard()));
                        }
                        dummySelectedDataReservationAdditionalItems.add(dummySelectedDataReservationAdditionalItem);
                    }
                    for (TblReservationAdditionalService dataSelectedReservationAdditionalService : selectedDataAdditionalServices) {
                        TblReservationAdditionalService dummySelectedDataReservationAdditionalService = new TblReservationAdditionalService(dataSelectedReservationAdditionalService);
                        dummySelectedDataReservationAdditionalService.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail);
                        dummySelectedDataReservationAdditionalService.setTblRoomService(new TblRoomService(dummySelectedDataReservationAdditionalService.getTblRoomService()));
                        dummySelectedDataReservationAdditionalService.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalService.getRefReservationBillType()));
                        if (dummySelectedDataReservationAdditionalService.getTblHotelEvent() != null) {
                            dummySelectedDataReservationAdditionalService.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalService.getTblHotelEvent()));
                        }
                        if (dummySelectedDataReservationAdditionalService.getTblBankEventCard() != null) {
                            dummySelectedDataReservationAdditionalService.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalService.getTblBankEventCard()));
                        }
                        dummySelectedDataReservationAdditionalServices.add(dummySelectedDataReservationAdditionalService);
                    }
                    if (roomStatusFDController.getServiceRV2().updateDataReservationRoomTypeDetailExtendRoom(
                            dummySelectedData,
                            dummyDataReservationBill,
                            dummySelectedDataRoomTypeDetail,
                            dummySelectedDataRoomTypeDetailRoomPriceDetails,
                            dummySelectedDataReservationAdditionalItems,
                            dummySelectedDataReservationAdditionalServices)) {
                        ClassMessage.showSucceedUpdatingDataMessage("", roomStatusFDController.dialogStage);
                        // refresh all data and close form data - reservation additional service
                        roomStatusFDController.setSelectedDataToInputForm();
                        roomStatusFDController.dialogStage.close();
                    } else {
                        ClassMessage.showFailedUpdatingDataMessage(roomStatusFDController.getServiceRV2().getErrorMessage(), roomStatusFDController.dialogStage);
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusFDController.dialogStage);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusFDController.dialogStage);
        }
    }

    private void dataCheckInExtendRoomCancelHandle() {
        //refresh all data and close form data - reservation additional service
        roomStatusFDController.setSelectedDataToInputForm();
        roomStatusFDController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCheckInExtendRoom() {
        boolean dataInput = true;
        errDataInput = "";
        if (dtpCheckOutDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Check Out : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (spDateCount.getValue() < 1) {
                dataInput = false;
                errDataInput += "Jumlah Hari Extend Kamar : minimal 1 hari\n";
            }
        }
        return dataInput;
    }

    private boolean checkDataInputDataCheckInExtendRoomAvailableRoom() {
        boolean dataInput = true;
        errDataInput = "";
        //room is available
        TblReservationRoomTypeDetail rrtdWantToCheckInToThisRoom = getNextRRTD(
                roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom(),
                Date.valueOf(roomStatusFDController.extendRoomCheckOutDate.get()));
        if (rrtdWantToCheckInToThisRoom != null) {
            dataInput = false;
            errDataInput += "Terdapat reservasi (" + rrtdWantToCheckInToThisRoom.getTblReservation().getCodeReservation() + ") yang akan menempati kamar ini, "
                    + "\nsilahkan pindahkan data reservasi (" + rrtdWantToCheckInToThisRoom.getTblReservation().getCodeReservation() + ") tersebut pada kamar lainnya..\n";
        }
        //room number is available
        if (dataInput) {
            for (TblReservationRoomTypeDetailRoomPriceDetail data : selectedDataRoomTypeDetailRoomPriceDetails) {
                int availableNumber = getRoomAvailableNumber(
                        roomStatusFDController.selectedDataRoomTypeDetail.getTblRoomType(),
                        Date.valueOf(LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                        data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                        data.getTblReservationRoomPriceDetail().getDetailDate().getDate())))
                        - getRoomReservedNumber(
                                roomStatusFDController.selectedDataRoomTypeDetail.getTblRoomType(),
                                Date.valueOf(LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                                data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                                data.getTblReservationRoomPriceDetail().getDetailDate().getDate())));
                if (availableNumber < 1) {
                    dataInput = false;
                    errDataInput += "Jumlah kamar pada " + ClassFormatter.dateFormate.format(data.getTblReservationRoomPriceDetail().getDetailDate()) + " tidak mencukupi..\n";
                    break;
                }
            }
        }
        return dataInput;
    }

    public TblReservationRoomTypeDetail getNextRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(roomStatusFDController.getServiceRV2().getReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(roomStatusFDController.getServiceRV2().getRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusFDController.getServiceRV2().getReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
                            //data reservation
                            rrtd.setTblReservation(roomStatusFDController.getServiceRV2().getReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(roomStatusFDController.getServiceRV2().getReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
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

    private int getRoomAvailableNumber(TblRoomType roomType,
            Date date) {
        int result = 0;
        List<TblRoom> listRoom = roomStatusFDController.getServiceRV2().getAllDataRoomByIDRoomType(roomType.getIdroomType());
        for (int i = listRoom.size() - 1; i > -1; i--) {
            if (listRoom.get(i).getRefRoomStatus().getIdstatus() == 6) {  //Out Of Service = '6'
                listRoom.remove(i);
            }
        }
        result = listRoom.size();
        List<TblTravelAgentRoomType> travelAgentRoomTypes = roomStatusFDController.getServiceRV2().getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(
                roomType.getIdroomType(),
                date);
        for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
            result -= travelAgentRoomType.getRoomNumber();
        }
        return result;
    }

    private int getRoomReservedNumber(TblRoomType roomType, Date date) {
        int result = 0;
        List<TblReservationRoomPriceDetail> roomPriceDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomPriceDetailByDetailDate(date);
        for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
            TblReservationRoomTypeDetailRoomPriceDetail relation = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
            TblReservationRoomTypeDetail roomTypeDetail = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
            roomTypeDetail.setTblReservation(roomStatusFDController.getServiceRV2().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
            roomTypeDetail.getTblReservation().setRefReservationStatus(roomStatusFDController.getServiceRV2().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
            if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                    && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 0 //Customer = '0'
                    && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                    || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                result++;
            }
        }
        return result;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataCheckInExtendRoom();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationCheckInExtendRoomController(RoomStatusFDController parentController) {
        roomStatusFDController = parentController;
    }

    private final RoomStatusFDController roomStatusFDController;

}
