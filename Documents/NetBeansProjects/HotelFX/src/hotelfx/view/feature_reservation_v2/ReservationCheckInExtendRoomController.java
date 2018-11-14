/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
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
 * @author ANDRI
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
                if (reservationController.extendRoomCheckOutDate.get() != null
                        && checkOutDate != null) {
                    reCountDayNumber = (int) ChronoUnit.DAYS.between(reservationController.extendRoomCheckOutDate.get(), checkOutDate);
                    spDateCount.getValueFactory().setValue(reCountDayNumber);
                }
                break;
            case "day-number":
                if (reservationController.extendRoomCheckOutDate.get() != null
                        && checkOutDate != null) {
                    dtpCheckOutDate.setValue(reservationController.extendRoomCheckOutDate.get().plusDays(dayNumber));
                }
                break;
            default:
                break;
        }
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpCheckOutDate);
        ClassFormatter.setDatePickersEnableDateFrom(reservationController.extendRoomCheckOutDate.get(),
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

    private void setSelectedDataToInputForm() {
        txtRoomName.textProperty().bind(reservationController.selectedDataCheckIn.getTblRoom().roomNameProperty());

        dtpCheckOutDate.setValue(reservationController.extendRoomCheckOutDate.get());

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
        reservationController.selectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            LocalDate countDate = reservationController.extendRoomCheckOutDate.getValue().minusDays(0);
            while (countDate.isBefore(dtpCheckOutDate.getValue())) {
                //reservation room price detail
                TblReservationRoomPriceDetail dataRPD = new TblReservationRoomPriceDetail();
                dataRPD.setDetailDate(Date.valueOf(countDate));
                dataRPD.setDetailPrice(getRoomPrice(
                        reservationController.reservationType,
                        reservationController.selectedDataRoomTypeDetail.getTblRoomType(),
                        countDate));
                //reservation room price detail - discount
                setReservationRoomPriceDetailDiscount(
                        dataRPD,
                        reservationController.selectedDataRoomTypeDetail,
                        reservationController.dataReservationBill);
                //reservation room price detail - complimentary
                dataRPD.setDetailComplimentary(new BigDecimal("0"));
                //reservation room type detail - reservation room price detail
                TblReservationRoomTypeDetailRoomPriceDetail dataRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail();
                dataRTDRPD.setTblReservationRoomTypeDetail(reservationController.selectedDataRoomTypeDetail);
                dataRTDRPD.setTblReservationRoomPriceDetail(dataRPD);
                //add to list (reservation room type detail - reservation room price detail)
                reservationController.selectedDataRoomTypeDetailRoomPriceDetails.add(dataRTDRPD);
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
        TblReservationCalendarBar calendarBAR = reservationController.getFReservationManager().getReservationCalendarBARByCalendarDate(Date.valueOf(date));
        if (calendarBAR == null) {
            TblReservationDefaultBar defaultBAR = reservationController.getFReservationManager().getReservationDefaultBARByDayOfWeek(date.getDayOfWeek().getValue());
            dataBAR = reservationController.getFReservationManager().getReservationBAR(defaultBAR.getTblReservationBar().getIdbar());
        } else {
            dataBAR = reservationController.getFReservationManager().getReservationBAR(calendarBAR.getTblReservationBar().getIdbar());
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

    private BigDecimal calculationTotalRoomCost() {
        BigDecimal result = new BigDecimal("0");
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
            }
        }
        return result;
    }

    private BigDecimal calculationTotalDiscount() {
        BigDecimal result = new BigDecimal("0");
        if (dtpCheckOutDate.getValue() != null
                && spDateCount.getValue() >= 1) {
            for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
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
                    .multiply((reservationController.dataReservationBill.getServiceChargePercentage()));
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
                    .multiply((reservationController.dataReservationBill.getTaxPercentage()));
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
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
                if (alert.getResult() == ButtonType.OK) {
                    //data reservation room type detail
                    reservationController.selectedDataRoomTypeDetail.setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.of(dtpCheckOutDate.getValue(), reservationController.defaultCheckOutTime.toLocalTime())));
                    //data reservation room type detail - reservation checkin/out
                    reservationController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(1)); //Checked In = '1'
                    //data reservation room type detail - rervation room price detail
                    //....
                    reservationController.selectedDataAdditionalServices = new ArrayList<>();
                    for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
                        //data reservation additional service (included)
                        List<TblRoomTypeRoomService> dataRTRSs = reservationController.getFReservationManager().getAllDataRoomTypeRoomServiceByIDRoomType(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType());
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
                                    dataAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(4));  //Include = '4'
                                    //add to list (reservation additional service : include)
                                    reservationController.selectedDataAdditionalServices.add(dataAdditionalService);
                                } else {
                                    for (int peopleNumber = 0; peopleNumber < dataRTRS.getPeopleNumber(); peopleNumber++) {
                                        //data additional service
                                        TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                                        dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTDRPD.getTblReservationRoomTypeDetail());
                                        dataAdditionalService.setTblRoomService(new TblRoomService(dataRTRS.getTblRoomService()));
                                        dataAdditionalService.setAdditionalDate(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
                                        dataAdditionalService.setPrice(new BigDecimal("0"));
                                        dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                        dataAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(4));  //Include = '4'
                                        //add to list (reservation additional service : include)
                                        reservationController.selectedDataAdditionalServices.add(dataAdditionalService);
                                    }
                                }
                            }
                        }
                        //data reservation additional item (included)
                        reservationController.selectedDataAdditionalItems = new ArrayList<>();
                        List<TblRoomTypeItem> dataRTIs = reservationController.getFReservationManager().getAllDataRoomTypeItemByIDRoomType(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType());
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
                                dataAddtionalItem.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(4));  //Include = '4'
                                //add to list (reservation additional item : include)
                                reservationController.selectedDataAdditionalItems.add(dataAddtionalItem);
                            }
                        }
                    }
                    //data reservation room type detail - service
                    //...service
                    //data reservation room type detail - item
                    //...service
                    //update data bill
                    reservationController.refreshDataBill();
                    //save data to database
                    if (reservationController.dataReservationSaveHandle(18)) {
                        //close form data checkin - extend room
                        reservationController.dialogStage.close();
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCheckInExtendRoomCancelHandle() {
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data checkin - extend room
        reservationController.dialogStage.close();
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
                reservationController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom(),
                Date.valueOf(reservationController.extendRoomCheckOutDate.get()));
        if (rrtdWantToCheckInToThisRoom != null) {
            dataInput = false;
            errDataInput += "Terdapat reservasi (" + rrtdWantToCheckInToThisRoom.getTblReservation().getCodeReservation() + ") yang akan menempati kamar ini, "
                    + "\nsilahkan pindahkan data reservasi (" + rrtdWantToCheckInToThisRoom.getTblReservation().getCodeReservation() + ") tersebut pada kamar lainnya..\n";
        }
        //room number is available
        if (dataInput) {
            for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
                int availableNumber = getRoomAvailableNumber(
                        reservationController.selectedDataRoomTypeDetail.getTblRoomType(),
                        Date.valueOf(LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                        data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                        data.getTblReservationRoomPriceDetail().getDetailDate().getDate())))
                        - getRoomReservedNumber(
                                reservationController.selectedDataRoomTypeDetail.getTblRoomType(),
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
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = reservationController.getFReservationManager().getReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(reservationController.getFReservationManager().getReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(reservationController.getFReservationManager().getRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
                            //data reservation
                            rrtd.setTblReservation(reservationController.getFReservationManager().getReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
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
        List<TblRoom> listRoom = reservationController.getFReservationManager().getAllDataRoomByIDRoomType(roomType.getIdroomType());
        for (int i = listRoom.size() - 1; i > -1; i--) {
            if (listRoom.get(i).getRefRoomStatus().getIdstatus() == 6) {  //Out Of Service = '6'
                listRoom.remove(i);
            }
        }
        result = listRoom.size();
        List<TblTravelAgentRoomType> travelAgentRoomTypes = reservationController.getFReservationManager().getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(
                roomType.getIdroomType(),
                date);
        for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
            result -= travelAgentRoomType.getRoomNumber();
        }
        return result;
    }

    private int getRoomReservedNumber(TblRoomType roomType, Date date) {
        int result = 0;
        List<TblReservationRoomPriceDetail> roomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomPriceDetailByDetailDate(date);
        for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
            TblReservationRoomTypeDetailRoomPriceDetail relation = reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
            TblReservationRoomTypeDetail roomTypeDetail = reservationController.getFReservationManager().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
            roomTypeDetail.setTblReservation(reservationController.getFReservationManager().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
            roomTypeDetail.getTblReservation().setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
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
    
    public ReservationCheckInExtendRoomController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;
    
}
