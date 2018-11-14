/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblPartner;
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
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ReservationChangeRoomController implements Initializable {

    @FXML
    private AnchorPane ancFormChangeRoom;

    @FXML
    private GridPane gpFormDataChangeRoom;

    @FXML
    private JFXTextField txtOldRoomNumber;

    @FXML
    private JFXTextField txtOldRoomType;

    @FXML
    private JFXTextField txtCodeReservation;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private AnchorPane ancNewRoomNumberLayout;
    private JFXCComboBoxTablePopup<TblRoom> cbpRoom;

    @FXML
    private JFXTextField txtNewRoomType;

    @FXML
    private JFXTextField txtTotalBillOld;

    @FXML
    private JFXTextField txtTotalBillNew;

    @FXML
    private JFXTextField txtTotalDifferentBill;

    @FXML
    private JFXCheckBox chbUseComplimentOrBonusVoucher;

    public int lastCodeNumber;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationChangeRoom() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Perpindahan Kamar)"));
        btnSave.setOnAction((e) -> {
            dataReservationChangeRoomSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationChangeRoomCancelHandle();
        });

        initImportantFieldColor();

        lastCodeNumber = getLastCodeNumber(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getIdreservation());
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpRoom);
    }

    private void initDataPopup() {
        //room
        TableView<TblRoom> tableRoom = new TableView();

        TableColumn<TblRoom, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        roomName.setMinWidth(120);

        TableColumn<TblRoom, String> roomType = new TableColumn("Tipe Kamar");
        roomType.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(),
                        param.getValue().tblRoomTypeProperty()));
        roomType.setMinWidth(160);

        TableColumn<TblRoom, String> smokingStatus = new TableColumn("Jenis Kamar");
        smokingStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getSmokingStatus() == 0 ? "Non Smoking" : "Smoking",
                        param.getValue().smokingStatusProperty()));
        smokingStatus.setMinWidth(120);

        tableRoom.getColumns().addAll(roomName, roomType, smokingStatus);

        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());

        cbpRoom = new JFXCComboBoxTablePopup<>(
                TblRoom.class, tableRoom, roomItems, "", "Kamar (Baru) *", true, 410, 300
        );

        ancNewRoomNumberLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpRoom, 0.0);
        AnchorPane.setBottomAnchor(cbpRoom, 0.0);
        AnchorPane.setLeftAnchor(cbpRoom, 0.0);
        AnchorPane.setRightAnchor(cbpRoom, 0.0);
        ancNewRoomNumberLayout.getChildren().add(cbpRoom);
    }

    private void refreshDataPopup() {
        //room
        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());
        cbpRoom.setItems(roomItems);
    }

    private List<TblRoom> loadAllDataRoom() {
        List<TblRoom> list = roomStatusFDController.getServiceRV2().getAllDataRoom();
        for (TblRoom data : list) {
            //data room type
            data.setTblRoomType(roomStatusFDController.getServiceRV2().getRoomType(data.getTblRoomType().getIdroomType()));
            //data room status
            data.setRefRoomStatus(roomStatusFDController.getServiceRV2().getRoomStatus(data.getRefRoomStatus().getIdstatus()));
        }
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
//            if (list.get(i).getRefRoomStatus().getIdstatus() != 3 //Vacant Clean = '3'
//                    && list.get(i).getRefRoomStatus().getIdstatus() != 4 //Vacant Dirty = '4'
//                    && list.get(i).getRefRoomStatus().getIdstatus() != 5) { //Vacant Ready = '5'
            if (list.get(i).getRefRoomStatus().getIdstatus() != 5) { //Vacant Ready = '5'
                list.remove(i);
            } else {
                if (roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut() != null
                        && roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() != null
                        && (roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getIdroom() == list.get(i).getIdroom())) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private int getLastCodeNumber(long idReservation) {
        int result = 0;
        if (idReservation != 0L) {
            List<TblReservationRoomTypeDetail> dataRRTD = roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailByIDReservation(idReservation);
            if (!dataRRTD.isEmpty()) {
                result = Integer.parseInt(dataRRTD.get(dataRRTD.size() - 1).getCodeDetail());
            }
        }
        return result;
    }

    private BigDecimal calculationTotalBillOld() {
        BigDecimal result = new BigDecimal("0");
        //data reservation room type (detail)
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == roomStatusFDController.selectedDataRoomTypeDetail.getIddetail()
                    && !(LocalDateTime.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getDate(),
                            roomStatusFDController.defaultCheckInTime.getHours(),
                            roomStatusFDController.defaultCheckInTime.getMinutes(),
                            0)).isBefore(roomStatusFDController.currentDateForChangeRoomProcess)) {
                result = result.add(((new BigDecimal("1")).subtract(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
            }
        }
        return result;
    }

    private BigDecimal calculationTotalBillNew() {
        BigDecimal result = new BigDecimal("0");
        //data reservation room type (detail)
        for (TblReservationRoomTypeDetailRoomPriceDetail data : newSelectedDataRoomTypeDetailRoomPriceDetails) {
            result = result.add(((new BigDecimal("1")).subtract(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                    .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
        }
        return result;
    }

    public BigDecimal calculationTotalDifferentBill() {
        return calculationTotalBillNew()
                .subtract(calculationTotalBillOld());
    }

    private TblReservationBill dataReservationBill;

    private List<TblReservationRoomTypeDetailRoomPriceDetail> dataReservationRoomTypeDetailRoomPriceDetails;

    private TblReservationRoomTypeDetail newSelectedDataRoomTypeDetail;

    private List<TblReservationRoomTypeDetailRoomPriceDetail> newSelectedDataRoomTypeDetailRoomPriceDetails;

    public List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> newSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories = new ArrayList<>();

    public TblReservationAdditionalService newSelectedDataAdditionalServiceBonusVoucher;

    private void setSelectedDataToInputForm() {
        dataReservationBill = roomStatusFDController.getServiceRV2().getReservationBillByIDReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getIdreservation());
        dataReservationRoomTypeDetailRoomPriceDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail.getIddetail());

        generateNewData();

        refreshDataPopup();

        txtOldRoomNumber.setText(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRoomName());
        txtOldRoomType.setText(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getTblRoomType().getRoomTypeName());
        txtCodeReservation.setText(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getCodeReservation());
        txtCustomerName.setText(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getFullName());

//        tlpReservationRoom.setText("Reservasi Kamar => " + getTextPeriode(roomStatusFDController.selectedDataRoomTypeDetail));
        txtNewRoomType.setText("");

        chbUseComplimentOrBonusVoucher.setVisible(false);

        cbpRoom.valueProperty().addListener((obs, oldVal, newVal) -> {
            generateNewData();
            if (newVal != null) {
                txtNewRoomType.setText(newVal.getTblRoomType().getRoomTypeName());
                refreshBill();
                if (calculationTotalDifferentBill().compareTo(new BigDecimal("0")) == 0) {    //=
                    chbUseComplimentOrBonusVoucher.setVisible(false);
                } else {
                    if (calculationTotalDifferentBill().compareTo(new BigDecimal("0")) == 1) {    //+
                        chbUseComplimentOrBonusVoucher.setText("Compliment");
                    } else {  //-
                        chbUseComplimentOrBonusVoucher.setText("Voucher Tambahan");
                    }
                    chbUseComplimentOrBonusVoucher.setVisible(true);
                }
            } else {
                txtNewRoomType.setText("");
                refreshBill();
                chbUseComplimentOrBonusVoucher.setVisible(false);

            }
        });

        cbpRoom.hide();

        refreshBill();
        System.out.println("h");
    }

    private void generateNewData() {
        newSelectedDataRoomTypeDetail = new TblReservationRoomTypeDetail();
        newSelectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        newSelectedDataAdditionalServiceBonusVoucher = new TblReservationAdditionalService();
        if (cbpRoom.getValue() != null) {
            //code detail
            lastCodeNumber++;
            String codeDetail = "";
            for (int j = 0; j < 5; j++) {
                codeDetail += "0";
            }
            codeDetail += String.valueOf(lastCodeNumber);
            codeDetail = codeDetail.substring(codeDetail.length() - 5);
            newSelectedDataRoomTypeDetail.setCodeDetail(codeDetail);
            newSelectedDataRoomTypeDetail.setTblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
            newSelectedDataRoomTypeDetail.setTblRoomType(cbpRoom.getValue().getTblRoomType());
            newSelectedDataRoomTypeDetail.setCheckInDateTime(Timestamp.valueOf(roomStatusFDController.currentDateForChangeRoomProcess));
            newSelectedDataRoomTypeDetail.setCheckOutDateTime(roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime());
            if (!(LocalDate.of(newSelectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                    newSelectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                    newSelectedDataRoomTypeDetail.getCheckInDateTime().getDate()))
                    .isAfter((LocalDate.now()).plusDays(1))) { //if check in date-time <= current date time
                //reservation room type detail - check in/out
                newSelectedDataRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut());
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(cbpRoom.getValue());
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setChildNumber(0);
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setAdultNumber(0);
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setCardUsed(0);
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setCardMissed(0);
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setBrokenCardCharge(new BigDecimal("0"));
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setCardAdditional(0);
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setAdditionalCardCharge(new BigDecimal("0"));
                newSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusFDController.getServiceRV2().getReservationCheckInOutStatus(0)); //Ready to Check In = '0'
            }
            newSelectedDataRoomTypeDetail.setRefReservationOrderByType(roomStatusFDController.getServiceRV2().getReservationOrderByType(0));   //Customer = '0'
            LocalDate countDate = LocalDate.of(newSelectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                    newSelectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                    newSelectedDataRoomTypeDetail.getCheckInDateTime().getDate())
                    .minusDays(0);
            while (countDate.isBefore(LocalDate.of(newSelectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                    newSelectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                    newSelectedDataRoomTypeDetail.getCheckOutDateTime().getDate()))) {
                //reservation room price detail
                TblReservationRoomPriceDetail dataRPD = new TblReservationRoomPriceDetail();
                dataRPD.setDetailDate(Date.valueOf(countDate));
                dataRPD.setDetailPrice(getRoomPrice(
                        newSelectedDataRoomTypeDetail.getTblRoomType(), countDate));
                //reservation room price detail - discount
                setReservationRoomPriceDetailDiscount(dataRPD, newSelectedDataRoomTypeDetail,
                        dataReservationBill);
                //reservation room price detail - complimentary
                setReservationRoomPriceDetailLastComplementary(dataRPD);
                //reservation room type detail - reservation room price detail
                TblReservationRoomTypeDetailRoomPriceDetail dataRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail();
                dataRTDRPD.setTblReservationRoomTypeDetail(newSelectedDataRoomTypeDetail);
                dataRTDRPD.setTblReservationRoomPriceDetail(dataRPD);
                //add to list (reservation room type detail - reservation room price detail)
                newSelectedDataRoomTypeDetailRoomPriceDetails.add(dataRTDRPD);
                //data additional item
                for (TblReservationAdditionalItem dataAdditionalItem : roomStatusFDController.selectedAdditionalItems) {
                    if (LocalDate.of(dataAdditionalItem.getAdditionalDate().getYear() + 1900,
                            dataAdditionalItem.getAdditionalDate().getMonth() + 1,
                            dataAdditionalItem.getAdditionalDate().getDate()).isEqual(countDate)) {
                        dataAdditionalItem.setTblReservationRoomTypeDetail(newSelectedDataRoomTypeDetail);
                    }
                }
                //data additional service
                for (TblReservationAdditionalService dataAdditionalService : roomStatusFDController.selectedAdditionalServices) {
                    if (LocalDate.of(dataAdditionalService.getAdditionalDate().getYear() + 1900,
                            dataAdditionalService.getAdditionalDate().getMonth() + 1,
                            dataAdditionalService.getAdditionalDate().getDate()).isEqual(countDate)) {
                        dataAdditionalService.setTblReservationRoomTypeDetail(newSelectedDataRoomTypeDetail);
                    }
                }
                //increment countDate
                countDate = countDate.plusDays(1);
            }
            //data additional service - bonus voucher
//            if (calculationTotalDifferentBill().compareTo(new BigDecimal("0")) == -1 //minus
//                    && chbUseComplimentOrBonusVoucher.isSelected()) {   //bonus voucher
            newSelectedDataAdditionalServiceBonusVoucher = new TblReservationAdditionalService();
            newSelectedDataAdditionalServiceBonusVoucher.setTblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
            newSelectedDataAdditionalServiceBonusVoucher.setTblRoomService(roomStatusFDController.getServiceRV2().getRoomService(4));  //Bonus Voucher = '4'
            newSelectedDataAdditionalServiceBonusVoucher.setAdditionalDate(Date.valueOf(roomStatusFDController.currentDateForChangeRoomProcess.toLocalDate()));
            newSelectedDataAdditionalServiceBonusVoucher.setPrice(calculationTotalDifferentBill().abs());
            newSelectedDataAdditionalServiceBonusVoucher.setDiscountPercentage(new BigDecimal("0"));
            newSelectedDataAdditionalServiceBonusVoucher.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(0));    //Reservation = '0'
//            }
        }
    }

    private BigDecimal getRoomPrice(
            TblRoomType roomType,
            LocalDate date) {
        TblReservationBar dataBAR;
        TblReservationCalendarBar calendarBAR = roomStatusFDController.getServiceRV2().getReservationCalendarBARByCalendarDate(Date.valueOf(date));
        if (calendarBAR == null) {
            TblReservationDefaultBar defaultBAR = roomStatusFDController.getServiceRV2().getReservationDefaultBARByDayOfWeek(date.getDayOfWeek().getValue());
            dataBAR = roomStatusFDController.getServiceRV2().getReservationBAR(defaultBAR.getTblReservationBar().getIdbar());
        } else {
            dataBAR = roomStatusFDController.getServiceRV2().getReservationBAR(calendarBAR.getTblReservationBar().getIdbar());
        }
        return roomType.getRoomTypePrice().multiply(dataBAR.getBarpercentage());
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

    private void setReservationRoomPriceDetailLastComplementary(TblReservationRoomPriceDetail dataReservationRoomPriceDetail) {
        dataReservationRoomPriceDetail.setDetailComplimentary(new BigDecimal("0"));
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == roomStatusFDController.selectedDataRoomTypeDetail.getIddetail()
                    && (LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getDate())).isEqual(
                            LocalDate.of(dataReservationRoomPriceDetail.getDetailDate().getYear() + 1900,
                                    dataReservationRoomPriceDetail.getDetailDate().getMonth() + 1,
                                    dataReservationRoomPriceDetail.getDetailDate().getDate()))) {
                dataReservationRoomPriceDetail.setDetailComplimentary(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
                break;
            }
        }
    }

    public void refreshBill() {
        txtTotalBillOld.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillOld()));
        txtTotalBillNew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillNew()));
        txtTotalDifferentBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDifferentBill()));
    }

    private String getTextPeriode(TblReservationRoomTypeDetail dataRRTD) {
        String result = "";
        if (dataRRTD != null) {
            result += "Periode Pidah Kamar :  ";
            result += ClassFormatter.dateFormate.format(Date.valueOf(roomStatusFDController.currentDateForChangeRoomProcess.toLocalDate()));
            result += "  s/d  ";
            result += ClassFormatter.dateFormate.format(dataRRTD.getCheckOutDateTime());

        } else {
            result += "Periode Pindah Kamar :  -  s/d  -";
        }
        return result;
    }

    private void dataReservationChangeRoomSaveHandle() {
        //$$$000
        if (checkDataInputDataReservationChangeRoom()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusFDController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //set data reservation room price detail - complementary
                setReservationRoomPriceDetailComplimentary();
                //set data reservation room type detail - room price detail - change room history
                setDataRoomTypeDetailRoomPriceDetailChangeRoomHistory();
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
                TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail = new TblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
                dummySelectedDataReservationRoomTypeDetail.setTblReservation(dummySelectedData);
                dummySelectedDataReservationRoomTypeDetail.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail.getTblRoomType()));
                if (dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut() != null) {
                    dummySelectedDataReservationRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut()));
                    if (dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() != null) {
                        dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom()));
                        dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                    }
                    if (dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                    }
                }
                TblReservationRoomTypeDetail dummyNewSelectedDataRoomTypeDetail = new TblReservationRoomTypeDetail(newSelectedDataRoomTypeDetail);
                dummyNewSelectedDataRoomTypeDetail.setTblReservation(dummySelectedData);
                dummyNewSelectedDataRoomTypeDetail.setTblRoomType(new TblRoomType(dummyNewSelectedDataRoomTypeDetail.getTblRoomType()));
                if (dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut() != null) {
                    dummyNewSelectedDataRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut()));
                    if (dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() != null) {
                        dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom()));
                        dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                    }
                    if (dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummyNewSelectedDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                    }
                }
                List<TblReservationRoomTypeDetailRoomPriceDetail> dummyNewSelectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
                List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories = new ArrayList<>();
                for (TblReservationRoomTypeDetailRoomPriceDetail newSelectedDataRoomTypeDetailRoomPriceDetail : newSelectedDataRoomTypeDetailRoomPriceDetails) {
                    if (newSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().equals(newSelectedDataRoomTypeDetail)) {
                        TblReservationRoomTypeDetailRoomPriceDetail dummyNewSelectedDataRoomTypeDetailRoomPriceDetail = new TblReservationRoomTypeDetailRoomPriceDetail(newSelectedDataRoomTypeDetailRoomPriceDetail);
                        dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.setTblReservationRoomTypeDetail(dummyNewSelectedDataRoomTypeDetail);
                        dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail()));
                        if (dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                            dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                        }
                        if (dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                            dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummyNewSelectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                        }
                        for (TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory newSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory : newSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories) {
                            if (newSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().equals(newSelectedDataRoomTypeDetailRoomPriceDetail)) {
                                TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory = new TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory(newSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory);
                                dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld(new TblReservationRoomTypeDetailRoomPriceDetail(dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld()));
                                dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail()));
                                dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setTblReservation(new TblReservation(dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblReservation()));
                                dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setTblRoomType(new TblRoomType(dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblRoomType()));
                                dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setRefReservationOrderByType(new RefReservationOrderByType(dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getRefReservationOrderByType()));
                                dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew(dummyNewSelectedDataRoomTypeDetailRoomPriceDetail);
                                dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories.add(dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory);
                            }
                        }
                        dummyNewSelectedDataRoomTypeDetailRoomPriceDetails.add(dummyNewSelectedDataRoomTypeDetailRoomPriceDetail);
                    }
                }
                List<TblReservationAdditionalItem> dummyNewSelectedDataReservationAdditionalItems = new ArrayList<>();
                for (TblReservationAdditionalItem newSelectedReservationAdditionalItem : roomStatusFDController.selectedAdditionalItems) {
                    if (newSelectedReservationAdditionalItem.getTblReservationRoomTypeDetail().equals(newSelectedDataRoomTypeDetail)) {
                        TblReservationAdditionalItem dummyNewSelectedDataReservationAdditionalItem = new TblReservationAdditionalItem(newSelectedReservationAdditionalItem);
                        dummyNewSelectedDataReservationAdditionalItem.setTblReservationRoomTypeDetail(dummyNewSelectedDataRoomTypeDetail);
                        dummyNewSelectedDataReservationAdditionalItem.setTblItem(new TblItem(dummyNewSelectedDataReservationAdditionalItem.getTblItem()));
                        dummyNewSelectedDataReservationAdditionalItem.setRefReservationBillType(new RefReservationBillType(dummyNewSelectedDataReservationAdditionalItem.getRefReservationBillType()));
                        if (dummyNewSelectedDataReservationAdditionalItem.getTblHotelEvent() != null) {
                            dummyNewSelectedDataReservationAdditionalItem.setTblHotelEvent(new TblHotelEvent(dummyNewSelectedDataReservationAdditionalItem.getTblHotelEvent()));
                        }
                        if (dummyNewSelectedDataReservationAdditionalItem.getTblBankEventCard() != null) {
                            dummyNewSelectedDataReservationAdditionalItem.setTblBankEventCard(new TblBankEventCard(dummyNewSelectedDataReservationAdditionalItem.getTblBankEventCard()));
                        }
                        dummyNewSelectedDataReservationAdditionalItems.add(dummyNewSelectedDataReservationAdditionalItem);
                    }
                }
                List<TblReservationAdditionalService> dummyNewSelectedDataReservationAdditionalServices = new ArrayList<>();
                for (TblReservationAdditionalService newSelectedReservationAdditionalService : roomStatusFDController.selectedAdditionalServices) {
                    if (newSelectedReservationAdditionalService.getTblReservationRoomTypeDetail().equals(newSelectedDataRoomTypeDetail)) {
                        TblReservationAdditionalService dummyNewSelectedDataReservationAdditionalService = new TblReservationAdditionalService(newSelectedReservationAdditionalService);
                        dummyNewSelectedDataReservationAdditionalService.setTblReservationRoomTypeDetail(dummyNewSelectedDataRoomTypeDetail);
                        dummyNewSelectedDataReservationAdditionalService.setTblRoomService(new TblRoomService(dummyNewSelectedDataReservationAdditionalService.getTblRoomService()));
                        dummyNewSelectedDataReservationAdditionalService.setRefReservationBillType(new RefReservationBillType(dummyNewSelectedDataReservationAdditionalService.getRefReservationBillType()));
                        if (dummyNewSelectedDataReservationAdditionalService.getTblHotelEvent() != null) {
                            dummyNewSelectedDataReservationAdditionalService.setTblHotelEvent(new TblHotelEvent(dummyNewSelectedDataReservationAdditionalService.getTblHotelEvent()));
                        }
                        if (dummyNewSelectedDataReservationAdditionalService.getTblBankEventCard() != null) {
                            dummyNewSelectedDataReservationAdditionalService.setTblBankEventCard(new TblBankEventCard(dummyNewSelectedDataReservationAdditionalService.getTblBankEventCard()));
                        }
                        dummyNewSelectedDataReservationAdditionalServices.add(dummyNewSelectedDataReservationAdditionalService);
                    } else {
                        if (newSelectedReservationAdditionalService.getTblReservationRoomTypeDetail().equals(roomStatusFDController.selectedDataRoomTypeDetail)) {    //old
                            TblReservationAdditionalService dummyNewSelectedDataReservationAdditionalService = new TblReservationAdditionalService(newSelectedReservationAdditionalService);
                            dummyNewSelectedDataReservationAdditionalService.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail);
                            dummyNewSelectedDataReservationAdditionalService.setTblRoomService(new TblRoomService(dummyNewSelectedDataReservationAdditionalService.getTblRoomService()));
                            dummyNewSelectedDataReservationAdditionalService.setRefReservationBillType(new RefReservationBillType(dummyNewSelectedDataReservationAdditionalService.getRefReservationBillType()));
                            if (dummyNewSelectedDataReservationAdditionalService.getTblHotelEvent() != null) {
                                dummyNewSelectedDataReservationAdditionalService.setTblHotelEvent(new TblHotelEvent(dummyNewSelectedDataReservationAdditionalService.getTblHotelEvent()));
                            }
                            if (dummyNewSelectedDataReservationAdditionalService.getTblBankEventCard() != null) {
                                dummyNewSelectedDataReservationAdditionalService.setTblBankEventCard(new TblBankEventCard(dummyNewSelectedDataReservationAdditionalService.getTblBankEventCard()));
                            }
                            dummyNewSelectedDataReservationAdditionalServices.add(dummyNewSelectedDataReservationAdditionalService);
                        }
                    }
                }
                //bonus voucher?
                if (calculationTotalDifferentBill().compareTo(new BigDecimal("0")) == -1 //minus
                        && chbUseComplimentOrBonusVoucher.isSelected()
                        && newSelectedDataAdditionalServiceBonusVoucher != null) {
                    TblReservationAdditionalService dummyNewSelectedDataAdditionalServiceBonusVoucher = new TblReservationAdditionalService(newSelectedDataAdditionalServiceBonusVoucher);
                    dummyNewSelectedDataAdditionalServiceBonusVoucher.setTblReservationRoomTypeDetail(dummyNewSelectedDataRoomTypeDetail);
                    dummyNewSelectedDataAdditionalServiceBonusVoucher.setTblRoomService(new TblRoomService(dummyNewSelectedDataAdditionalServiceBonusVoucher.getTblRoomService()));
                    dummyNewSelectedDataAdditionalServiceBonusVoucher.setRefReservationBillType(new RefReservationBillType(dummyNewSelectedDataAdditionalServiceBonusVoucher.getRefReservationBillType()));
                    if (dummyNewSelectedDataAdditionalServiceBonusVoucher.getTblHotelEvent() != null) {
                        dummyNewSelectedDataAdditionalServiceBonusVoucher.setTblHotelEvent(new TblHotelEvent(dummyNewSelectedDataAdditionalServiceBonusVoucher.getTblHotelEvent()));
                    }
                    if (dummyNewSelectedDataAdditionalServiceBonusVoucher.getTblBankEventCard() != null) {
                        dummyNewSelectedDataAdditionalServiceBonusVoucher.setTblBankEventCard(new TblBankEventCard(dummyNewSelectedDataAdditionalServiceBonusVoucher.getTblBankEventCard()));
                    }
                    //add to list reservation additonal item
                    dummyNewSelectedDataReservationAdditionalServices.add(dummyNewSelectedDataAdditionalServiceBonusVoucher);
                }
                if (roomStatusFDController.getServiceRV2().updateDataReservationRoomTypeDetailChangeRoom(
                        dummySelectedData,
                        dummySelectedDataReservationRoomTypeDetail, //update data
                        dummyNewSelectedDataRoomTypeDetail, //insert data
                        dummyNewSelectedDataRoomTypeDetailRoomPriceDetails, //insert data
                        dummyNewSelectedDataReservationAdditionalItems, //update data
                        dummyNewSelectedDataReservationAdditionalServices, //update data, insert data (bonus voucher?)
                        dummyNewSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories,
                        roomStatusFDController.currentDateForChangeRoomProcess,
                        roomStatusFDController.defaultCheckInTime)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", roomStatusFDController.dialogStage);
                    // refresh all data and close form data - change room
                    roomStatusFDController.setSelectedDataToInputForm();
                    roomStatusFDController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(roomStatusFDController.getServiceRV2().getErrorMessage(), roomStatusFDController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusFDController.dialogStage);
        }
    }

    private void setReservationRoomPriceDetailComplimentary() {
        //$$$000
        for (TblReservationRoomTypeDetailRoomPriceDetail dataRTDRPD : newSelectedDataRoomTypeDetailRoomPriceDetails) {
            BigDecimal newPrice = ((new BigDecimal("1")).subtract(dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                    .multiply(dataRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
            BigDecimal oldPrice = new BigDecimal("0");
            BigDecimal oldComplimentary = new BigDecimal("0");
            for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().getIddetail() == roomStatusFDController.selectedDataRoomTypeDetail.getIddetail()
                        && (LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                data.getTblReservationRoomPriceDetail().getDetailDate().getDate())).isEqual(
                                LocalDate.of(dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                        dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                        dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
                    oldPrice = oldPrice.add(((new BigDecimal("1")).subtract(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                            .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
                    oldComplimentary = oldComplimentary.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
                    break;
                }
            }
            if (chbUseComplimentOrBonusVoucher.isSelected() //complimentary
                    && calculationTotalDifferentBill().compareTo(new BigDecimal("0")) == 1) {   //plus
                dataRTDRPD.getTblReservationRoomPriceDetail().setDetailComplimentary(
                        (newPrice.subtract(oldPrice)).compareTo(new BigDecimal("0")) < 1
                                ? oldComplimentary
                                : (newPrice.subtract(oldPrice)).add(oldComplimentary));
            } else {
                dataRTDRPD.getTblReservationRoomPriceDetail().setDetailComplimentary(oldComplimentary);
            }
        }
    }

    private void setDataRoomTypeDetailRoomPriceDetailChangeRoomHistory() {
        List<TblReservationRoomTypeDetailRoomPriceDetail> tempOldDataReservationRoomTypeDetailRoomPriceDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail.getIddetail());
        for (TblReservationRoomTypeDetailRoomPriceDetail tempOldDataReservationRoomTypeDetailRoomPriceDetail : tempOldDataReservationRoomTypeDetailRoomPriceDetails) {
            //data reservation room type detail
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.setTblReservationRoomTypeDetail(roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getIddetail()));
            //data reservation
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().setTblReservation(roomStatusFDController.getServiceRV2().getReservation(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
            //data room type
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().setTblRoomType(roomStatusFDController.getServiceRV2().getRoomType(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
            //data reservation order by type
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().setRefReservationOrderByType(roomStatusFDController.getServiceRV2().getReservationOrderByType(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getRefReservationOrderByType().getIdtype()));
            //data room price detail
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.setTblReservationRoomPriceDetail(roomStatusFDController.getServiceRV2().getReservationRoomPriceDetail(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getIddetail()));
            for (TblReservationRoomTypeDetailRoomPriceDetail selectedDataRoomTypeDetailRoomPriceDetail : newSelectedDataRoomTypeDetailRoomPriceDetails) {
                if (LocalDate.of(
                        tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                        tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                        tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getDate()).isEqual(LocalDate.of(
                                        selectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                        selectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                        selectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
                    TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory dataHistory = new TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory();
                    dataHistory.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld(tempOldDataReservationRoomTypeDetailRoomPriceDetail);
                    dataHistory.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew(selectedDataRoomTypeDetailRoomPriceDetail);
                    newSelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories.add(dataHistory);
                    break;
                }
            }
        }
    }

    private void dataReservationChangeRoomCancelHandle() {
        //refresh all data and close form data - change room
        roomStatusFDController.setSelectedDataToInputForm();
        roomStatusFDController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationChangeRoom() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpRoom.getValue() == null) {
            dataInput = false;
            errDataInput += "Kamar (Baru) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if ((roomStatusFDController.selectedDataRoomTypeDetail.getTblRoomType().getIdroomType()
                    != newSelectedDataRoomTypeDetail.getTblRoomType().getIdroomType())
                    && !checkRoomAvailable(newSelectedDataRoomTypeDetail, null)) {
                dataInput = false;
                errDataInput += "Kamar (Baru) : Jumlah tipe kamar tidak mencukupi untuk reservasi kamar lainnya..!!\n";
            }
        }
        return dataInput;
    }

    private boolean checkRoomAvailable(TblReservationRoomTypeDetail reservationRoomTypeDetail,
            TblPartner partner) {
        boolean available = true;
        LocalDate dateCount = LocalDate.parse(
                (new SimpleDateFormat("yyyy-MM-dd")).format(reservationRoomTypeDetail.getCheckInDateTime())
                + "T"
                + (new SimpleDateFormat("HH:mm:ss").format(reservationRoomTypeDetail.getCheckInDateTime())),
                DateTimeFormatter.ISO_DATE_TIME);
        while (Date.valueOf(dateCount).before(reservationRoomTypeDetail.getCheckOutDateTime())) {
            int availableNumber = getRoomAvailableNumber(reservationRoomTypeDetail.getTblRoomType(), dateCount, partner)
                    - getRoomReservedNumber(reservationRoomTypeDetail.getTblRoomType(), dateCount, partner);
            if ((availableNumber - 1) < 0) {
                available = false;
                break;
            }
            dateCount = dateCount.plusDays(1);
        }
        return available;
    }

    private int getRoomAvailableNumber(TblRoomType roomType, LocalDate date, TblPartner partner) {
        int result = 0;
        if (partner == null) {
            List<TblRoom> listRoom = roomStatusFDController.getServiceRV2().getAllDataRoomByIDRoomType(roomType.getIdroomType());
            for (int i = listRoom.size() - 1; i > -1; i--) {
                if (listRoom.get(i).getRefRoomStatus().getIdstatus() == 6) {  //Out Of Order = '6'
                    listRoom.remove(i);
                }
            }
            result = listRoom.size();
            List<TblTravelAgentRoomType> travelAgentRoomTypes = roomStatusFDController.getServiceRV2().getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(
                    roomType.getIdroomType(),
                    Date.valueOf(date));
            for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
                result -= travelAgentRoomType.getRoomNumber();
            }
        } else {  //Travel Agent
            List<TblTravelAgentRoomType> tempList = roomStatusFDController.getServiceRV2().getAllDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(
                    roomType.getIdroomType(),
                    partner.getIdpartner(),
                    Date.valueOf(date));
            TblTravelAgentRoomType travelAgentRoomType = tempList.isEmpty() ? null : tempList.get(0);
            if (travelAgentRoomType != null) {
                result = travelAgentRoomType.getRoomNumber();
            }
        }
        return result;
    }

    private int getRoomReservedNumber(TblRoomType roomType, LocalDate date, TblPartner partner) {
        int result = 0;
        if (partner == null) {
            List<TblReservationRoomPriceDetail> roomPriceDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                List<TblReservationRoomTypeDetailRoomPriceDetail> tempList = roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetailRoomPriceDetail relation = tempList.isEmpty() ? null : tempList.get(0);
                if (relation != null) {
                    TblReservationRoomTypeDetail roomTypeDetail = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                    roomTypeDetail.setTblReservation(roomStatusFDController.getServiceRV2().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                    roomTypeDetail.getTblReservation().setRefReservationStatus(roomStatusFDController.getServiceRV2().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
                    if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                            && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 0 //Customer = '0'
                            && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                            || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                        //@ctive? + in controller (getRoomReservedNumber())
                        result++;
                    }
                }
            }
        } else {  //Travel Agent
            List<TblReservationRoomPriceDetail> roomPriceDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                List<TblReservationRoomTypeDetailRoomPriceDetail> tempList = roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetailRoomPriceDetail relation = tempList.isEmpty() ? null : tempList.get(0);
                if (relation != null) {
                    TblReservationRoomTypeDetail roomTypeDetail = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                    roomTypeDetail.setTblReservation(roomStatusFDController.getServiceRV2().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                    roomTypeDetail.getTblReservation().setRefReservationStatus(roomStatusFDController.getServiceRV2().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
                    if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                            && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 1 //Travel Agent = '1'
                            && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                            || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> tempList1 = roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(roomTypeDetail.getIddetail());
                        TblReservationRoomTypeDetailTravelAgentDiscountDetail relation1 = tempList1.isEmpty() ? null : tempList1.get(0);
                        if (relation1 != null) {
                            TblReservationTravelAgentDiscountDetail travelAgentDiscountDetail
                                    = roomStatusFDController.getServiceRV2().getReservationTravelAgentDiscountDetail(relation1.getTblReservationTravelAgentDiscountDetail().getIddetail());
                            if (partner.getIdpartner() == travelAgentDiscountDetail.getTblPartner().getIdpartner()) {
                                //@ctive? + in controller (getRoomReservedNumber())
                                result++;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

//    private BigDecimal calculationRoomTypeDetailCostDF(TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD) {
//        BigDecimal newPrice = dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice();
//        BigDecimal oldPrice = new BigDecimal("0");
//        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
//            if (data.getTblReservationRoomTypeDetail().getIddetail() == roomStatusFDController.selectedDataRoomTypeDetail.getIddetail()
//                    && (LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
//                            data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
//                            data.getTblReservationRoomPriceDetail().getDetailDate().getDate())).isEqual(
//                            LocalDate.of(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
//                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
//                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
//                oldPrice = oldPrice.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
//                break;
//            }
//        }
//        return newPrice.subtract(oldPrice);
//    }
//
//    private BigDecimal calculationRoomTypeDetailDiscountDF(TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD) {
//        BigDecimal result = new BigDecimal("0");
//        BigDecimal newDiscountPrice = (dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))
//                .multiply(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
//        BigDecimal oldDiscountPrice = new BigDecimal("0");
//        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
//            if (data.getTblReservationRoomTypeDetail().getIddetail() == roomStatusFDController.selectedDataRoomTypeDetail.getIddetail()
//                    && (LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
//                            data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
//                            data.getTblReservationRoomPriceDetail().getDetailDate().getDate())).isEqual(
//                            LocalDate.of(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
//                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
//                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
//                oldDiscountPrice = oldDiscountPrice.add((data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))
//                        .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
//                break;
//            }
//        }
//        result = result.add(newDiscountPrice.subtract(oldDiscountPrice));
//        return result;
//    }
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataReservationChangeRoom();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationChangeRoomController(RoomStatusFDController parentController) {
        roomStatusFDController = parentController;
    }

    private final RoomStatusFDController roomStatusFDController;

    public RoomStatusFDController getParentController() {
        return roomStatusFDController;
    }

}
