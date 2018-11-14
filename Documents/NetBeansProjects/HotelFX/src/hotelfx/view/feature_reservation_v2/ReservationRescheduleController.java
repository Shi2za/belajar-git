/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCancelingFee;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationRescheduleCanceled;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.service.FReservationV2Manager;
import hotelfx.view.feature_reservation_v2.rr.RRReservationAdditionalItemInputController;
import hotelfx.view.feature_reservation_v2.rr.RRReservationAdditionalServiceInputController;
import hotelfx.view.feature_reservation_v2.rr.RRReservationRoomTypeDetailInputController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ReservationRescheduleController implements Initializable {

    @FXML
    private AnchorPane ancFormReservationReschedule;

    /**
     * RESERVATION OLD
     */
    @FXML
    private ScrollPane scrDataReservationOld;

    @FXML
    private GridPane gpFormDataReservationOld;

    @FXML
    private AnchorPane ancReservationRoomTypeDetailOldLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalServiceOldLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalItemOldLayout;

    /**
     * RESERVATION NEW
     */
    @FXML
    private ScrollPane scrDataReservationNew;

    @FXML
    private GridPane gpFormDataReservationNew;

    @FXML
    private AnchorPane ancReservationRoomTypeDetailNewLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalServiceNewLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalItemNewLayout;

    /**
     * RESERVATION BILL & PAYMENT (NEW)
     */
    @FXML
    private GridPane gpFormDataRCOBillNew;

    @FXML
    private JFXTextField txtTotalRoomCostRCONew;

    @FXML
    private JFXTextField txtTotalAdditionalCostRCONew;

    @FXML
    private JFXTextField txtTotalDiscountRCONew;

    @FXML
    private JFXTextField txtTotalCancelingFeeRCONew;

    @FXML
    private JFXTextField txtTotalServiceChargeRCONew;

    @FXML
    private Label lblServiceChargePercentageInRCOBillNew;

    @FXML
    private JFXTextField txtTotalTaxRCONew;

    @FXML
    private Label lblTaxPercentageInRCOBillNew;

    @FXML
    private JFXTextField txtRCOBillNew;

    @FXML
    private Label lblRoundingValueInRCOBillNew;

    @FXML
    private JFXTextField txtTotalTransactionPaymentRCONew;

    @FXML
    private JFXTextField txtCurrentCancelingFeeRCONew;

    @FXML
    private AnchorPane ancTableTransactionRCONewLayout;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationReschedule() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Perubahan Reservasi)"));
        btnSave.setOnAction((e) -> {
            dataReservationRescheduleSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationRescheduleCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCurrentCancelingFeeRCONew);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtCurrentCancelingFeeRCONew);
    }

    public List<TblReservationRescheduleCanceled> newReservationReschedules = new ArrayList<>();

    public List<TblReservationRoomTypeDetail> newDataReservationRoomTypeDetails = new ArrayList<>();

    public List<TblReservationAdditionalItem> newDataReservationAdditionalItems = new ArrayList<>();

    public List<TblReservationAdditionalService> newDataReservationAdditionalServices = new ArrayList<>();

    public List<TblReservationRoomTypeDetailRoomPriceDetail> newDataReservationRoomTypeDetailRoomPriceDetails = new ArrayList<>();

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> newDataReservationRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();

    private void generateNewData() {
        for (TblReservationRoomTypeDetail rrtd : reservationController.dataReservationRoomTypeDetails) {
            //data rrtd
            TblReservationRoomTypeDetail newRRTD = new TblReservationRoomTypeDetail(rrtd);
            newRRTD.setIddetail(0L);
            newRRTD.setTblReservationAdditionalItemReserveds(null);
            newRRTD.setTblReservationAdditionalItems(null);
            newRRTD.setTblReservationAdditionalServices(null);
            newRRTD.setTblReservationBreakfastVouchers(null);
            newRRTD.setTblReservationBrokenItems(null);
            newRRTD.setTblReservationRescheduleCanceleds(null);
            newRRTD.setTblReservationRoomItems(null);
            newRRTD.setTblReservationRoomTypeDetailChildDetails(null);
            newRRTD.setTblReservationRoomTypeDetailItems(null);
            newRRTD.setTblReservationRoomTypeDetailRoomPriceDetails(null);
            newRRTD.setTblReservationRoomTypeDetailRoomServices(null);
            newRRTD.setTblReservationRoomTypeDetailTravelAgentDiscountDetails(null);
            //data rrtd - room price detail & reschedule canceled
            for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
                if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                        == rrtd.getIddetail()) {
                    //data rrtd - room price detail
                    TblReservationRoomTypeDetailRoomPriceDetail newRRTDRPD = new TblReservationRoomTypeDetailRoomPriceDetail(rrtdrpd);
                    newRRTDRPD.setIdrelation(0L);
                    newRRTDRPD.setTblReservationRoomTypeDetail(newRRTD);
                    newRRTDRPD.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(newRRTDRPD.getTblReservationRoomPriceDetail()));
                    newRRTDRPD.getTblReservationRoomPriceDetail().setIddetail(0L);
                    newRRTDRPD.getTblReservationRoomPriceDetail().setTblReservationRoomTypeDetailRoomPriceDetails(null);
                    newRRTDRPD.setTblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistoriesForIdrelationNew(null);
                    newRRTDRPD.setTblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistoriesForIdrelationOld(null);
                    newDataReservationRoomTypeDetailRoomPriceDetails.add(newRRTDRPD);
                    //data reschedule canceled (+)
                    TblReservationRescheduleCanceled newRRCP = new TblReservationRescheduleCanceled();
                    newRRCP.setTblReservation(reservationController.selectedData);
                    newRRCP.setTblReservationRoomTypeDetail(rrtdrpd.getTblReservationRoomTypeDetail());
                    newRRCP.setReservedDate(newRRTDRPD.getTblReservationRoomPriceDetail().getCreateDate());
                    newRRCP.setReservedForDate(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
                    newRRCP.setQuantity(new BigDecimal("1"));
                    newRRCP.setPrice(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
                    newRRCP.setDiscountPercentage(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage());
                    newRRCP.setComplimentary(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary());
                    newRRCP.setTblHotelEvent(newRRTDRPD.getTblReservationRoomPriceDetail().getTblHotelEvent());
                    newRRCP.setTblBankEventCard(newRRTDRPD.getTblReservationRoomPriceDetail().getTblBankEventCard());
                    newReservationReschedules.add(newRRCP);
                    //data reschedule canceled (-)
                    TblReservationRescheduleCanceled newRRCN = new TblReservationRescheduleCanceled();
                    newRRCN.setTblReservation(reservationController.selectedData);
                    newRRCN.setTblReservationRoomTypeDetail(rrtdrpd.getTblReservationRoomTypeDetail());
                    newRRCN.setReservedDate(Timestamp.valueOf(LocalDateTime.now()));
                    newRRCN.setReservedForDate(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
                    newRRCN.setQuantity(new BigDecimal("1"));
                    newRRCN.setPrice((new BigDecimal("-1")).multiply(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice()));
                    newRRCN.setDiscountPercentage((new BigDecimal("-1")).multiply(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()));
                    newRRCN.setComplimentary((new BigDecimal("-1")).multiply(newRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary()));
                    newRRCN.setTblHotelEvent(newRRTDRPD.getTblReservationRoomPriceDetail().getTblHotelEvent());
                    newRRCN.setTblBankEventCard(newRRTDRPD.getTblReservationRoomPriceDetail().getTblBankEventCard());
                    newReservationReschedules.add(newRRCN);
                }
            }
            //data rrtd - travel agent discount
            for (TblReservationRoomTypeDetailTravelAgentDiscountDetail rrtdtadd : reservationController.dataReservationRoomTypeDetailTravelAgentDiscountDetails) {
                if (rrtdtadd.getTblReservationRoomTypeDetail().getIddetail()
                        == rrtd.getIddetail()) {
                    TblReservationRoomTypeDetailTravelAgentDiscountDetail newRRTDTADD = new TblReservationRoomTypeDetailTravelAgentDiscountDetail(rrtdtadd);
                    newRRTDTADD.setIdrelation(0L);
                    newRRTDTADD.setTblReservationRoomTypeDetail(newRRTD);
                    newRRTDTADD.setTblReservationTravelAgentDiscountDetail(new TblReservationTravelAgentDiscountDetail(newRRTDTADD.getTblReservationTravelAgentDiscountDetail()));
                    newRRTDTADD.getTblReservationTravelAgentDiscountDetail().setIddetail(0L);
                    newRRTDTADD.getTblReservationTravelAgentDiscountDetail().setTblReservationRoomTypeDetailTravelAgentDiscountDetails(null);
                    newDataReservationRoomTypeDetailTravelAgentDiscountDetails.add(newRRTDTADD);
                }
            }
            //data additional item
            for (TblReservationAdditionalItem rai : reservationController.dataReservationAdditionalItems) {
                if (rai.getTblReservationRoomTypeDetail().getIddetail()
                        == rrtd.getIddetail()) {
                    TblReservationAdditionalItem newRAI = new TblReservationAdditionalItem(rai);
                    newRAI.setIdadditional(0L);
                    newRAI.setTblReservationRoomTypeDetail(newRRTD);
                    newDataReservationAdditionalItems.add(newRAI);
                    if (rai.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || rai.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                        //data reschedule canceled (+)
                        TblReservationRescheduleCanceled newRRCP = new TblReservationRescheduleCanceled();
                        newRRCP.setTblReservation(reservationController.selectedData);
                        newRRCP.setTblReservationRoomTypeDetail(rai.getTblReservationRoomTypeDetail());
                        newRRCP.setTblItem(new TblItem(newRAI.getTblItem()));
                        newRRCP.setReservedDate(newRAI.getCreateDate());
                        newRRCP.setReservedForDate(newRAI.getAdditionalDate());
                        newRRCP.setQuantity(newRAI.getItemQuantity());
                        newRRCP.setPrice(newRAI.getItemCharge());
                        newRRCP.setDiscountPercentage(newRAI.getDiscountPercentage());
                        newRRCP.setComplimentary(new BigDecimal("0"));
                        newRRCP.setTblHotelEvent(newRAI.getTblHotelEvent());
                        newRRCP.setTblBankEventCard(newRAI.getTblBankEventCard());
                        newReservationReschedules.add(newRRCP);
                        //data reschedule canceled (-)
                        TblReservationRescheduleCanceled newRRCN = new TblReservationRescheduleCanceled();
                        newRRCN.setTblReservation(reservationController.selectedData);
                        newRRCN.setTblReservationRoomTypeDetail(rai.getTblReservationRoomTypeDetail());
                        newRRCN.setTblItem(new TblItem(newRAI.getTblItem()));
                        newRRCN.setReservedDate(Timestamp.valueOf(LocalDateTime.now()));
                        newRRCN.setReservedForDate(newRAI.getAdditionalDate());
                        newRRCN.setQuantity(newRAI.getItemQuantity());
                        newRRCN.setPrice((new BigDecimal("-1")).multiply(newRAI.getItemCharge()));
                        newRRCN.setDiscountPercentage((new BigDecimal("-1")).multiply(newRAI.getDiscountPercentage()));
                        newRRCN.setComplimentary(new BigDecimal("0"));
                        newRRCN.setTblHotelEvent(newRAI.getTblHotelEvent());
                        newRRCN.setTblBankEventCard(newRAI.getTblBankEventCard());
                        newReservationReschedules.add(newRRCN);
                    }
                }
            }
            //data additional service
            for (TblReservationAdditionalService ras : reservationController.dataReservationAdditionalServices) {
                if (ras.getTblReservationRoomTypeDetail().getIddetail()
                        == rrtd.getIddetail()) {
                    TblReservationAdditionalService newRAS = new TblReservationAdditionalService(ras);
                    newRAS.setIdadditional(0L);
                    newRAS.setTblReservationRoomTypeDetail(newRRTD);
                    newDataReservationAdditionalServices.add(newRAS);
                    if (ras.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || ras.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                        //data reschedule canceled (+)
                        TblReservationRescheduleCanceled newRRCP = new TblReservationRescheduleCanceled();
                        newRRCP.setTblReservation(reservationController.selectedData);
                        newRRCP.setTblReservationRoomTypeDetail(ras.getTblReservationRoomTypeDetail());
                        newRRCP.setTblRoomService(new TblRoomService(newRAS.getTblRoomService()));
                        newRRCP.setReservedDate(newRAS.getCreateDate());
                        newRRCP.setReservedForDate(newRAS.getAdditionalDate());
                        newRRCP.setQuantity(new BigDecimal("1"));
                        newRRCP.setPrice(newRAS.getPrice());
                        newRRCP.setDiscountPercentage(newRAS.getDiscountPercentage());
                        newRRCP.setComplimentary(new BigDecimal("0"));
                        newRRCP.setTblHotelEvent(newRAS.getTblHotelEvent());
                        newRRCP.setTblBankEventCard(newRAS.getTblBankEventCard());
                        newReservationReschedules.add(newRRCP);
                        //data reschedule canceled (-)
                        TblReservationRescheduleCanceled newRRCN = new TblReservationRescheduleCanceled();
                        newRRCN.setTblReservation(reservationController.selectedData);
                        newRRCN.setTblReservationRoomTypeDetail(ras.getTblReservationRoomTypeDetail());
                        newRRCN.setTblRoomService(new TblRoomService(newRAS.getTblRoomService()));
                        newRRCN.setReservedDate(Timestamp.valueOf(LocalDateTime.now()));
                        newRRCN.setReservedForDate(newRAS.getAdditionalDate());
                        newRRCN.setQuantity(new BigDecimal("1"));
                        newRRCN.setPrice((new BigDecimal("-1")).multiply(newRAS.getPrice()));
                        newRRCN.setDiscountPercentage((new BigDecimal("-1")).multiply(newRAS.getDiscountPercentage()));
                        newRRCN.setComplimentary(new BigDecimal("0"));
                        newRRCN.setTblHotelEvent(newRAS.getTblHotelEvent());
                        newRRCN.setTblBankEventCard(newRAS.getTblBankEventCard());
                        newReservationReschedules.add(newRRCN);
                    }
                }
            }
            newDataReservationRoomTypeDetails.add(newRRTD);
        }
        //set canceling fee -> create date
        reservationController.selectedDataCancelingFee.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        //set reserved date for new data
        //rrdt
        for (TblReservationRoomTypeDetail data : newDataReservationRoomTypeDetails) {
            data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        //room - price
        for (TblReservationRoomTypeDetailRoomPriceDetail data : newDataReservationRoomTypeDetailRoomPriceDetails) {
            data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            data.getTblReservationRoomPriceDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        //additional item
        for (TblReservationAdditionalItem data : newDataReservationAdditionalItems) {
            data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        //additional service
        for (TblReservationAdditionalService data : newDataReservationAdditionalServices) {
            data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        }
    }

    private void setSelectedDataToInputForm() {
        //generate new data
        generateNewData();

        lblCodeData.setText(reservationController.selectedData.getCodeReservation()
                + " - " + reservationController.selectedData.getTblCustomer().getTblPeople().getFullName()
                + " : " + reservationController.selectedData.getRefReservationStatus().getStatusName());

        //data bill and transaction (new)
        Bindings.bindBidirectional(txtCurrentCancelingFeeRCONew.textProperty(), reservationController.selectedDataCancelingFee.cancelingFeeNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        reservationController.selectedDataCancelingFee.cancelingFeeNominalProperty().addListener((obs, oldVal, newVal) -> {
            refreshDataBillNew();
            initTableDataRCOTransactionNew();
        });

        refreshDataBillNew();
        initTableDataRCOTransactionNew();

        //data reservation room type detail (old)
        initTableDataReservationRoomTypeDetailOld();

        //data reservation room additional (old)
        initTableDataReservationAdditionalItemOld();
        initTableDataReservationAdditionalServiceOld();

        //data reservation room type detail (new)
        initTableDataReservationRoomTypeDetailNew();

        //data reservation room additional (new)
        initTableDataReservationAdditionalItemNew();
        initTableDataReservationAdditionalServiceNew();
    }

    /**
     * TABLE DATA RESERVATION ROOM TYPE DETAIL (OLD)
     */
    public TableView tableDataReservationRoomTypeDetailOld;

    private void initTableDataReservationRoomTypeDetailOld() {
        //set table
        setTableDataReservationRoomTypeDetailOld();
        //set table-control to content-view
        ancReservationRoomTypeDetailOldLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationRoomTypeDetailOld, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationRoomTypeDetailOld, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationRoomTypeDetailOld, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationRoomTypeDetailOld, 0.0);

        ancReservationRoomTypeDetailOldLayout.getChildren().add(tableDataReservationRoomTypeDetailOld);
    }

    private void setTableDataReservationRoomTypeDetailOld() {
        TableView<TblReservationRoomTypeDetail> tableView = new TableView();

        TableColumn<TblReservationRoomTypeDetail, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeDetail(), param.getValue().codeDetailProperty()));
        codeDetail.setMinWidth(95);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeName = new TableColumn("Tipe Kamar");
        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
        roomTypeName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                                : param.getValue().getTblReservationCheckInOut().getTblRoom() != null
                                        ? param.getValue().getTblReservationCheckInOut().getTblRoom().getRoomName() : "",
                        param.getValue().tblReservationCheckInOutProperty()));
        roomName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> checkInOutStatus = new TableColumn("Status");
        checkInOutStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                                : param.getValue().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName(), param.getValue().tblReservationCheckInOutProperty()));
        checkInOutStatus.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> adultNumber = new TableColumn("Dewasa");
        adultNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getAdultNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        adultNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> childNumber = new TableColumn("Anak");
        childNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getChildNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        childNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> acNumberTitle = new TableColumn("Jumlah");
        acNumberTitle.getColumns().addAll(adultNumber, childNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardUsedNumber = new TableColumn("Digunakan");
        cardUsedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getCardUsed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardUsedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> cardMissedNumber = new TableColumn("Rusak");
        cardMissedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getCardMissed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardMissedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> ubGuestCardNumberTitle = new TableColumn("Jumlah Kartu");
        ubGuestCardNumberTitle.getColumns().addAll(cardUsedNumber, cardMissedNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardDeposit = new TableColumn("Deposit");
        cardDeposit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardUsed()))
                                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardDeposit.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> cardCharge = new TableColumn("Biaya Kartu");
        cardCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardMissed()))
                                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardCharge.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> checkInDate = new TableColumn("Check In");
        checkInDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckInDateTime()), param.getValue().checkInDateTimeProperty()));
        checkInDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> checkOutDate = new TableColumn("Check Out");
        checkOutDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckOutDateTime()), param.getValue().checkOutDateTimeProperty()));
        checkOutDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> ioDateTitle = new TableColumn("Tanggal");
        ioDateTitle.getColumns().addAll(checkInDate, checkOutDate);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCost = new TableColumn("Harga");
        roomTypeDetailCost.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailCostOld(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCost.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailDiscount = new TableColumn("Diskon");
        roomTypeDetailDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailDiscountOld(param.getValue())),
                        param.getValue().lastUpdateDateProperty()));
        roomTypeDetailDiscount.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCompliment = new TableColumn("Compliment");
        roomTypeDetailCompliment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailComplimentOld(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCompliment.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, roomTypeName,
                ioDateTitle,
                roomTypeDetailCost, roomTypeDetailDiscount, roomTypeDetailCompliment);

        tableView.setItems(loadAllDataReservationRoomTypeDetailOld());

        tableDataReservationRoomTypeDetailOld = tableView;

    }

    private ObservableList<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetailOld() {
        return FXCollections.observableArrayList(reservationController.dataReservationRoomTypeDetails);
    }

    /**
     * TABLE DATA RESERVATION ROOM TYPE DETAIL (NEW)
     */
    public ClassTableWithControl tableDataReservationRoomTypeDetailNew;

    private void initTableDataReservationRoomTypeDetailNew() {
        //set table
        setTableDataReservationRoomTypeDetailNew();
        //set control
        setTableControlDataReservationRoomTypeDetailNew();
        //set table-control to content-view
        ancReservationRoomTypeDetailNewLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationRoomTypeDetailNew, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationRoomTypeDetailNew, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationRoomTypeDetailNew, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationRoomTypeDetailNew, 0.0);

        ancReservationRoomTypeDetailNewLayout.getChildren().add(tableDataReservationRoomTypeDetailNew);
    }

    public BigDecimal calculationRoomTypeDetailCostOld(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
            }
        }
        return result;
    }

    public BigDecimal calculationRoomTypeDetailDiscountOld(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice()
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
            }
        }
        return result;
    }

    private BigDecimal calculationRoomTypeDetailComplimentOld(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
            }
        }
        return result;
    }

    private void setTableDataReservationRoomTypeDetailNew() {
        TableView<TblReservationRoomTypeDetail> tableView = new TableView();

        TableColumn<TblReservationRoomTypeDetail, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeDetail(), param.getValue().codeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeName = new TableColumn("Tipe Kamar");
        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
        roomTypeName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                                : param.getValue().getTblReservationCheckInOut().getTblRoom() != null
                                        ? param.getValue().getTblReservationCheckInOut().getTblRoom().getRoomName() : "",
                        param.getValue().tblReservationCheckInOutProperty()));
        roomName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> checkInOutStatus = new TableColumn("Status");
        checkInOutStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                                : param.getValue().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName(), param.getValue().tblReservationCheckInOutProperty()));
        checkInOutStatus.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> adultNumber = new TableColumn("Dewasa");
        adultNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getAdultNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        adultNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> childNumber = new TableColumn("Anak");
        childNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getChildNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        childNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> acNumberTitle = new TableColumn("Jumlah");
        acNumberTitle.getColumns().addAll(adultNumber, childNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardUsedNumber = new TableColumn("Digunakan");
        cardUsedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getCardUsed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardUsedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> cardMissedNumber = new TableColumn("Rusak");
        cardMissedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getCardMissed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardMissedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> ubGuestCardNumberTitle = new TableColumn("Jumlah Kartu");
        ubGuestCardNumberTitle.getColumns().addAll(cardUsedNumber, cardMissedNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardDeposit = new TableColumn("Deposit");
        cardDeposit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardUsed()))
                                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardDeposit.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> cardCharge = new TableColumn("Biaya Kartu");
        cardCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardMissed()))
                                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardCharge.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> checkInDate = new TableColumn("Check In");
        checkInDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckInDateTime()), param.getValue().checkInDateTimeProperty()));
        checkInDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> checkOutDate = new TableColumn("Check Out");
        checkOutDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckOutDateTime()), param.getValue().checkOutDateTimeProperty()));
        checkOutDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> ioDateTitle = new TableColumn("Tanggal");
        ioDateTitle.getColumns().addAll(checkInDate, checkOutDate);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCost = new TableColumn("Harga");
        roomTypeDetailCost.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailCostNew(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCost.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailDiscount = new TableColumn("Diskon");
        roomTypeDetailDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailDiscountNew(param.getValue())),
                        param.getValue().lastUpdateDateProperty()));
        roomTypeDetailDiscount.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCompliment = new TableColumn("Compliment");
        roomTypeDetailCompliment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailComplimentNew(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCompliment.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, roomTypeName,
                ioDateTitle,
                roomTypeDetailCost, roomTypeDetailDiscount, roomTypeDetailCompliment);

        tableView.setItems(loadAllDataReservationRoomTypeDetailNew());

        tableDataReservationRoomTypeDetailNew = new ClassTableWithControl(tableView);

    }

    public BigDecimal calculationRoomTypeDetailCostNew(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : newDataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
            }
        }
        return result;
    }

    public BigDecimal calculationRoomTypeDetailDiscountNew(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : newDataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice()
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
            }
        }
        return result;
    }

    private BigDecimal calculationRoomTypeDetailComplimentNew(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : newDataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
            }
        }
        return result;
    }

    private void setTableControlDataReservationRoomTypeDetailNew() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationRoomTypeDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) { //Reserved = '1'
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationRoomTypeDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationRoomTypeDetailNew.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetailNew() {
        return FXCollections.observableArrayList(newDataReservationRoomTypeDetails);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL ITEM (OLD)
     */
    public TableView tableDataReservationAdditionalItemOld;

    private void initTableDataReservationAdditionalItemOld() {
        //set table
        setTableDataReservationAdditionalItemOld();
        //set table-control to content-view
        ancReservationRoomAdditionalItemOldLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalItemOld, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalItemOld, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalItemOld, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalItemOld, 0.0);

        ancReservationRoomAdditionalItemOldLayout.getChildren().add(tableDataReservationAdditionalItemOld);
    }

    private void setTableDataReservationAdditionalItemOld() {
        TableView<TblReservationAdditionalItem> tableView = new TableView();

        TableColumn<TblReservationAdditionalItem, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(),
                        param.getValue().getTblReservationRoomTypeDetail().codeDetailProperty()));
        codeDetail.setMinWidth(95);

        TableColumn<TblReservationAdditionalItem, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate()), param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblReservationAdditionalItem, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge()), param.getValue().itemChargeProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalCharge = new TableColumn("Total Biaya");
        totalAdditionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity())),
                        param.getValue().idadditionalProperty()));
        totalAdditionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalDiscount = new TableColumn("Total Diskon");
        totalAdditionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat((param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity()))
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        totalAdditionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(),
                        param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, additionalDate, itemName, additionalCharge, itemQuantity, itemUnit, totalAdditionalCharge, totalAdditionalDiscount, additionalType);
        tableView.setItems(loadAllDataReservationAdditionalItemOld());
        tableDataReservationAdditionalItemOld = tableView;

    }

    private ObservableList<TblReservationAdditionalItem> loadAllDataReservationAdditionalItemOld() {
        return FXCollections.observableArrayList(reservationController.dataReservationAdditionalItems);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL ITEM (NEW)
     */
    public ClassTableWithControl tableDataReservationAdditionalItemNew;

    private void initTableDataReservationAdditionalItemNew() {
        //set table
        setTableDataReservationAdditionalItemNew();
        //set control
        setTableControlDataReservationAdditionalItemNew();
        //set table-control to content-view
        ancReservationRoomAdditionalItemNewLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalItemNew, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalItemNew, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalItemNew, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalItemNew, 0.0);

        ancReservationRoomAdditionalItemNewLayout.getChildren().add(tableDataReservationAdditionalItemNew);
    }

    private void setTableDataReservationAdditionalItemNew() {
        TableView<TblReservationAdditionalItem> tableView = new TableView();

        TableColumn<TblReservationAdditionalItem, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(),
                        param.getValue().getTblReservationRoomTypeDetail().codeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationAdditionalItem, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate()), param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblReservationAdditionalItem, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge()), param.getValue().itemChargeProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalCharge = new TableColumn("Total Biaya");
        totalAdditionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity())),
                        param.getValue().idadditionalProperty()));
        totalAdditionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalDiscount = new TableColumn("Total Diskon");
        totalAdditionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat((param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity()))
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        totalAdditionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(),
                        param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, additionalDate, itemName, additionalCharge, itemQuantity, itemUnit, totalAdditionalCharge, totalAdditionalDiscount, additionalType);
        tableView.setItems(loadAllDataReservationAdditionalItemNew());
        tableDataReservationAdditionalItemNew = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationAdditionalItemNew() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationAdditionalItemCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationAdditionalItemDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationAdditionalItemNew.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationAdditionalItem> loadAllDataReservationAdditionalItemNew() {
        return FXCollections.observableArrayList(newDataReservationAdditionalItems);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL SERVICE (OLD)
     */
    public TableView tableDataReservationAdditionalServiceOld;

    private void initTableDataReservationAdditionalServiceOld() {
        //set table
        setTableDataReservationAdditionalServiceOld();
        //set table-control to content-view
        ancReservationRoomAdditionalServiceOldLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalServiceOld, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalServiceOld, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalServiceOld, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalServiceOld, 0.0);

        ancReservationRoomAdditionalServiceOldLayout.getChildren().add(tableDataReservationAdditionalServiceOld);
    }

    private void setTableDataReservationAdditionalServiceOld() {
        TableView<TblReservationAdditionalService> tableView = new TableView();

        TableColumn<TblReservationAdditionalService, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(), param.getValue().tblReservationRoomTypeDetailProperty()));
        codeDetail.setMinWidth(95);

        TableColumn<TblReservationAdditionalService, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblRoomService().getIdroomService() != 1
                                ? ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate())
                                : ClassFormatter.dateFormate.format(Date.valueOf(
                                                LocalDate.of(
                                                        param.getValue().getAdditionalDate().getYear() + 1900,
                                                        param.getValue().getAdditionalDate().getMonth() + 1,
                                                        param.getValue().getAdditionalDate().getDate())
                                                .plusDays(1))),
                        param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> serviceName = new TableColumn("Layanan");
        serviceName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomService().getServiceName(), param.getValue().tblRoomServiceProperty()));
        serviceName.setMinWidth(140);

        TableColumn<TblReservationAdditionalService, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()), param.getValue().priceProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalDiscount = new TableColumn("Diskon");
        additionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        additionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(), param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalNote = new TableColumn("Keterangan");
        additionalNote.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> (param.getValue().getRestoTransactionNumber() != null
                                ? "No. Transaksi : " + param.getValue().getRestoTransactionNumber() : "")
                        + (param.getValue().getNote() != null
                                ? param.getValue().getNote() : (param.getValue().getRestoTransactionNumber() != null
                                        ? "" : "-")),
                        param.getValue().noteProperty()));
        additionalNote.setMinWidth(200);

        tableView.getColumns().addAll(codeDetail, additionalDate, serviceName, additionalCharge, additionalDiscount, additionalType, additionalNote);
        tableView.setItems(loadAllDataReservationAdditionalServiceOld());
        tableDataReservationAdditionalServiceOld = tableView;

    }

    private ObservableList<TblReservationAdditionalService> loadAllDataReservationAdditionalServiceOld() {
        return FXCollections.observableArrayList(reservationController.dataReservationAdditionalServices);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL SERVICE (NEW)
     */
    public ClassTableWithControl tableDataReservationAdditionalServiceNew;

    private void initTableDataReservationAdditionalServiceNew() {
        //set table
        setTableDataReservationAdditionalServiceNew();
        //set control
        setTableControlDataReservationAdditionalServiceNew();
        //set table-control to content-view
        ancReservationRoomAdditionalServiceNewLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalServiceNew, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalServiceNew, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalServiceNew, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalServiceNew, 0.0);

        ancReservationRoomAdditionalServiceNewLayout.getChildren().add(tableDataReservationAdditionalServiceNew);
    }

    private void setTableDataReservationAdditionalServiceNew() {
        TableView<TblReservationAdditionalService> tableView = new TableView();

        TableColumn<TblReservationAdditionalService, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(), param.getValue().tblReservationRoomTypeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationAdditionalService, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblRoomService().getIdroomService() != 1
                                ? ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate())
                                : ClassFormatter.dateFormate.format(Date.valueOf(
                                                LocalDate.of(
                                                        param.getValue().getAdditionalDate().getYear() + 1900,
                                                        param.getValue().getAdditionalDate().getMonth() + 1,
                                                        param.getValue().getAdditionalDate().getDate())
                                                .plusDays(1))),
                        param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> serviceName = new TableColumn("Layanan");
        serviceName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomService().getServiceName(), param.getValue().tblRoomServiceProperty()));
        serviceName.setMinWidth(140);

        TableColumn<TblReservationAdditionalService, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()), param.getValue().priceProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalDiscount = new TableColumn("Diskon");
        additionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        additionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(), param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalNote = new TableColumn("Keterangan");
        additionalNote.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> (param.getValue().getRestoTransactionNumber() != null
                                ? "No. Transaksi : " + param.getValue().getRestoTransactionNumber() : "")
                        + (param.getValue().getNote() != null
                                ? param.getValue().getNote() : (param.getValue().getRestoTransactionNumber() != null
                                        ? "" : "-")),
                        param.getValue().noteProperty()));
        additionalNote.setMinWidth(200);

        tableView.getColumns().addAll(codeDetail, additionalDate, serviceName, additionalCharge, additionalDiscount, additionalType, additionalNote);
        tableView.setItems(loadAllDataReservationAdditionalServiceNew());
        tableDataReservationAdditionalServiceNew = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationAdditionalServiceNew() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationAdditionalServiceCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationAdditionalServiceDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationAdditionalServiceNew.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationAdditionalService> loadAllDataReservationAdditionalServiceNew() {
        return FXCollections.observableArrayList(newDataReservationAdditionalServices);
    }

    /**
     * TABLE DATA RCO TRANSACTION (NEW)
     */
    public TableView tableDataRCOTransactionNew;

    public void initTableDataRCOTransactionNew() {
        //set table
        setTableDataRCOTransaction();
        //set table-control to content-view
        ancTableTransactionRCONewLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataRCOTransactionNew, 15.0);
        AnchorPane.setLeftAnchor(tableDataRCOTransactionNew, 15.0);
        AnchorPane.setBottomAnchor(tableDataRCOTransactionNew, 15.0);
        AnchorPane.setRightAnchor(tableDataRCOTransactionNew, 15.0);

        ancTableTransactionRCONewLayout.getChildren().add(tableDataRCOTransactionNew);
    }

    private void setTableDataRCOTransaction() {
        TableView<ReservationController.ReservationBillPaymentDetail> tableView = new TableView();

        TableColumn<ReservationController.ReservationBillPaymentDetail, String> detailDate = new TableColumn("Tanggal");
        detailDate.setCellValueFactory((TableColumn.CellDataFeatures<ReservationController.ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDetailDate()),
                        param.getValue().detailDateProperty()));
        detailDate.setMinWidth(120);
        detailDate.setSortable(false);

        TableColumn<ReservationController.ReservationBillPaymentDetail, String> detailDescription = new TableColumn("Keterangan");
        detailDescription.setCellValueFactory((TableColumn.CellDataFeatures<ReservationController.ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDetailDescription(),
                        param.getValue().detailDescriptionProperty()));
        detailDescription.setMinWidth(300);
        detailDescription.setSortable(false);

        TableColumn<ReservationController.ReservationBillPaymentDetail, String> detailDebit = new TableColumn("Debit");
        detailDebit.setCellValueFactory((TableColumn.CellDataFeatures<ReservationController.ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDetailDebit()),
                        param.getValue().detailDebitProperty()));
        detailDebit.setMinWidth(140);
        detailDebit.setSortable(false);

        TableColumn<ReservationController.ReservationBillPaymentDetail, String> detailCreadit = new TableColumn("Kredit");
        detailCreadit.setCellValueFactory((TableColumn.CellDataFeatures<ReservationController.ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDetailCreadit()),
                        param.getValue().detailCreaditProperty()));
        detailCreadit.setMinWidth(140);
        detailCreadit.setSortable(false);

        tableView.getColumns().addAll(detailDate, detailDescription, detailDebit, detailCreadit);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataRCOTransaction()));

        tableDataRCOTransactionNew = tableView;
    }

    public ObservableList<TblReservationPayment> loadAllDataReservationTransaction() {
        return FXCollections.observableArrayList(reservationController.dataReservationPayments);
    }

    public ObservableList<ReservationController.ReservationBillPaymentDetail> loadAllDataRCOTransaction() {
        List<ReservationController.ReservationBillPaymentDetail> list = new ArrayList<>();
        //reservation - reschedule - canceled (previous)
        for (TblReservationRescheduleCanceled data : reservationController.dataReservationReschedules) {
            list.add(reservationController.generateReservationBillPaymentDetail(null, null, null, null, data, null, null));
        }
        //reservation - canceling fee (previous)
        for (TblReservationCancelingFee data : reservationController.dataReservationCancelingFees) {
            list.add(reservationController.generateReservationBillPaymentDetail(null, null, null, null, null, data, null));
        }
        //reservation - reschedule - canceled (current)
        for (TblReservationRescheduleCanceled data : newReservationReschedules) {
            list.add(reservationController.generateReservationBillPaymentDetail(null, null, null, null, data, null, null));
        }
        //reservation - canceling fee (current)
        list.add(reservationController.generateReservationBillPaymentDetail(null, null, null, null, null, reservationController.selectedDataCancelingFee, null));
        //rrtd - rpd (room price detail) ~ new
        for (TblReservationRoomTypeDetailRoomPriceDetail data : newDataReservationRoomTypeDetailRoomPriceDetails) {
            list.add(reservationController.generateReservationBillPaymentDetail(data, null, null, null, null, null, null));
        }
        //additional service ~ new
        for (TblReservationAdditionalService data : newDataReservationAdditionalServices) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                list.add(reservationController.generateReservationBillPaymentDetail(null, null, data, null, null, null, null));
            }
        }
        //additional item ~ new
        for (TblReservationAdditionalItem data : newDataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                list.add(reservationController.generateReservationBillPaymentDetail(null, data, null, null, null, null, null));
            }
        }
        //broken item
        for (TblReservationBrokenItem data : reservationController.dataReservationBrokenItems) {
            list.add(reservationController.generateReservationBillPaymentDetail(null, null, null, data, null, null, null));
        }
        //payment
        for (TblReservationPayment data : reservationController.dataReservationPayments) {
            list.add(reservationController.generateReservationBillPaymentDetail(null, null, null, null, null, null, data));
        }
        //sorting by date
        reservationController.sortingReservationBillPaymentDetailByDateASC(list);
        return FXCollections.observableArrayList(list);
    }

    public void refreshDataBillNew() {
        lblServiceChargePercentageInRCOBillNew.setText("(" + ClassFormatter.decimalFormat.cFormat((reservationController.dataReservationBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "%)");
        lblTaxPercentageInRCOBillNew.setText("(" + ClassFormatter.decimalFormat.cFormat((reservationController.dataReservationBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "%)");
        txtTotalRoomCostRCONew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoom()));
        txtTotalAdditionalCostRCONew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditional()));
        txtTotalDiscountRCONew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount()));
        txtTotalCancelingFeeRCONew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalCancelingFee()));
        txtTotalServiceChargeRCONew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge()));
        txtTotalTaxRCONew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax()));
        txtRCOBillNew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillAfterRounding()));
        lblRoundingValueInRCOBillNew.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransactionRoundingValue()) + ")");
        txtTotalTransactionPaymentRCONew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransaction()));
    }

    public BigDecimal calculationTotalReservationRoom() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail d : newDataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailPrice());
        }
        return result;
    }

    public BigDecimal calculationTotalReservationRoomCompliment() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail d : newDataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailComplimentary());
        }
        return result;
    }

    public BigDecimal calculationTotalAdditional() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationAdditionalItem data : newDataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
            }
        }
        for (TblReservationAdditionalService data : newDataReservationAdditionalServices) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                result = result.add(data.getPrice());
            }
        }
        return result;
    }

    public BigDecimal calculationTotalAdditionalWithOutRestoBill() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationAdditionalItem data : newDataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
            }
        }
        for (TblReservationAdditionalService data : newDataReservationAdditionalServices) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                if (data.getTblRoomService().getIdroomService() != 6 //Room Dining = '6'
                        && data.getTblRoomService().getIdroomService() != 7) {   //Dine in Resto = '7'
                    result = result.add(data.getPrice());
                }
            }
        }
        return result;
    }

    public BigDecimal calculationTotalDiscount() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail d : newDataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailPrice().multiply(d.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
        }
        for (TblReservationAdditionalItem data : newDataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) { //Check Out = '1'
                result = result.add((data.getItemCharge().multiply(data.getItemQuantity())).multiply(data.getDiscountPercentage().divide(new BigDecimal("100"))));
            }
        }
        for (TblReservationAdditionalService data : newDataReservationAdditionalServices) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) { //Check Out = '1'                            
                result = result.add(data.getPrice().multiply((data.getDiscountPercentage().divide(new BigDecimal("100")))));
            }
        }
        return result;
    }

    public BigDecimal calculationTotalServiceCharge() {
        BigDecimal result = calculationTotalReservationRoom()
                .subtract(calculationTotalReservationRoomCompliment())
                .add(calculationTotalAdditionalWithOutRestoBill())
                .subtract(calculationTotalDiscount())
                .add(calculationTotalCancelingFee())
                .add(calculationTotalBroken());
        result = result.multiply(reservationController.dataReservationBill.getServiceChargePercentage());
        return result;
    }

    public BigDecimal calculationTotalTax() {
        BigDecimal result = calculationTotalReservationRoom()
                .subtract(calculationTotalReservationRoomCompliment())
                .add(calculationTotalAdditionalWithOutRestoBill())
                .subtract(calculationTotalDiscount())
                .add(calculationTotalCancelingFee()
                        .add(calculationTotalBroken()));
        result = result.add(calculationTotalServiceCharge());
        result = result.multiply(reservationController.dataReservationBill.getTaxPercentage());
        return result;
    }

    public BigDecimal calculationTotalBill() {
        BigDecimal result = calculationTotalReservationRoom()
                .subtract(calculationTotalReservationRoomCompliment())
                .add(calculationTotalAdditional())
                .subtract(calculationTotalDiscount())
                .add(calculationTotalCancelingFee())
                .add(calculationTotalBroken())
                .add(calculationTotalServiceCharge())
                .add(calculationTotalTax());
        return result;
    }

    public BigDecimal calculationTotalBillAfterRounding() {
        //total bill
        BigDecimal result = calculationTotalBill();
        //total rounding value
        result = result.add(calculationTotalReservationTransactionRoundingValue());
        return result;
    }

    public BigDecimal calculationTotalCancelingFee() {
        BigDecimal result = new BigDecimal("0");
        //previous
        for (TblReservationCancelingFee data : reservationController.dataReservationCancelingFees) {
            result = result.add(data.getCancelingFeeNominal());
        }
        //current
        result = result.add(reservationController.selectedDataCancelingFee.getCancelingFeeNominal() != null
                ? reservationController.selectedDataCancelingFee.getCancelingFeeNominal() : new BigDecimal("0"));
        return result;
    }

    public BigDecimal calculationTotalBroken() {
        BigDecimal result = new BigDecimal("0");
        //items
        for (TblReservationBrokenItem data : reservationController.dataReservationBrokenItems) {
            result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransaction() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationPayment data : reservationController.dataReservationPayments) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Rservation = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {   //CheckOut = '1'
                if (data.getRefFinanceTransactionPaymentType().getIdtype() == 16) {   //Return = '16'
                    result = result.subtract(data.getUnitNominal());
                } else {
                    result = result.add(data.getUnitNominal());
                }
            }
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransactionRoundingValue() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationPayment data : reservationController.dataReservationPayments) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Rservation = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {   //CheckOut = '1'
                result = result.add(data.getRoundingValue());
            }
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransactionDisountPayment() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationPaymentWithBankCard data : reservationController.dataReservationPaymentWithBankCards) {
            if (data.getTblReservationPayment().getRefReservationBillType().getIdtype() == 0 //Rservation = '0'
                    || data.getTblReservationPayment().getRefReservationBillType().getIdtype() == 1) {   //CheckOut = '1'
                result = result.add(data.getPaymentDiscount());
            }
        }
        return result;
    }

    public BigDecimal calculationTotalRestOfBill() {
        return calculationTotalBillAfterRounding()
                .subtract(calculationTotalReservationTransaction())
                .subtract(calculationTotalReservationTransactionDisountPayment());
    }

    /**
     * HANDLE FOR DATA INPUT RESERVATION ROOM TYPE DETAIL
     */
    public TblReservationRoomTypeDetail newSelectedDataRoomTypeDetail;

    public List<TblReservationRoomTypeDetail> newSelectedDataRoomTypeDetails;

    public List<TblReservationRoomTypeDetailRoomPriceDetail> newSelectedDataRoomTypeDetailRoomPriceDetails;

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> newSelectedDataRoomTypeDetailTravelAgentDiscountDetails;

    public List<TblReservationAdditionalService> newSelectedDataAdditionalServices;

    public List<TblReservationAdditionalItem> newSelectedDataAdditionalItems;

    public void dataReservationRoomTypeDetailCreateHandle() {
        showReservationRoomTypeDetailDialog();
    }

    private void dataReservationRoomTypeDetailDeleteHandle() {
        if (tableDataReservationRoomTypeDetailNew.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //reservation room type detail
                newSelectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetailNew.getTableView().getSelectionModel().getSelectedItem();
                ClassMessage.showSucceedDeletingDataMessage("", null);
                //reservation room type detail - reservation room price detail
                for (int i = newDataReservationRoomTypeDetailRoomPriceDetails.size() - 1; i > -1; i--) {
                    if (newDataReservationRoomTypeDetailRoomPriceDetails.get(i).getTblReservationRoomTypeDetail().equals(newSelectedDataRoomTypeDetail)) {
                        newDataReservationRoomTypeDetailRoomPriceDetails.remove(i);
                    }
                }
                //reservation room type detail - reservation travel agent discount detail
                for (int i = newDataReservationRoomTypeDetailTravelAgentDiscountDetails.size() - 1; i > -1; i--) {
                    if (newDataReservationRoomTypeDetailTravelAgentDiscountDetails.get(i).getTblReservationRoomTypeDetail().equals(newSelectedDataRoomTypeDetail)) {
                        newDataReservationRoomTypeDetailTravelAgentDiscountDetails.remove(i);
                    }
                }
                //reservation additional item
                for (int i = newDataReservationAdditionalItems.size() - 1; i > -1; i--) {
                    if (newDataReservationAdditionalItems.get(i).getTblReservationRoomTypeDetail().equals(newSelectedDataRoomTypeDetail)) {
                        newDataReservationAdditionalItems.remove(i);
                    }
                }
                tableDataReservationAdditionalItemNew.getTableView().setItems(FXCollections.observableArrayList(newDataReservationAdditionalItems));
                //reservation additional service
                for (int i = newDataReservationAdditionalServices.size() - 1; i > -1; i--) {
                    if (newDataReservationAdditionalServices.get(i).getTblReservationRoomTypeDetail().equals(newSelectedDataRoomTypeDetail)) {
                        newDataReservationAdditionalServices.remove(i);
                    }
                }
                tableDataReservationAdditionalServiceNew.getTableView().setItems(FXCollections.observableArrayList(newDataReservationAdditionalServices));
                //reservation room type detail
                for (int i = newDataReservationRoomTypeDetails.size() - 1; i > -1; i--) {
                    if (newDataReservationRoomTypeDetails.get(i).equals(newSelectedDataRoomTypeDetail)) {
                        newDataReservationRoomTypeDetails.remove(i);
                    }
                }
                //recode
                int countID = 1;
                for (TblReservationRoomTypeDetail newDataReservationRoomTypeDetail : newDataReservationRoomTypeDetails) {
                    String codeDetail = "";
                    for (int i = 0; i < 5; i++) {
                        codeDetail += "0";
                    }
                    codeDetail += String.valueOf(countID);
                    codeDetail = codeDetail.substring(codeDetail.length() - 5);
                    newDataReservationRoomTypeDetail.setCodeDetail(codeDetail);
                    countID++;
                }
                tableDataReservationRoomTypeDetailNew.getTableView().setItems(FXCollections.observableArrayList(newDataReservationRoomTypeDetails));
                refreshDataBillNew();
                initTableDataRCOTransactionNew();
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public Stage dialogStage;

    private void showReservationRoomTypeDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rr/RRReservationRoomTypeDetailInputDialog.fxml"));

            RRReservationRoomTypeDetailInputController controller = new RRReservationRoomTypeDetailInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION ADDITIONAL ITEM
     */
    public TblReservationAdditionalItem newSelectedAdditionalItem;

    public List<TblReservationAdditionalItem> newSelectedAdditionalItems;

    private void dataReservationAdditionalItemCreateHandle() {
        //set selected data
        newSelectedAdditionalItem = new TblReservationAdditionalItem();
        newSelectedAdditionalItem.setItemCharge(new BigDecimal("0"));
        newSelectedAdditionalItem.setItemQuantity(new BigDecimal("0"));
        newSelectedAdditionalItem.setDiscountPercentage(new BigDecimal("0"));
        newSelectedAdditionalItem.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));    //Reservasi = '0'
        //open form data reservation additional item dialog
        showReservationAdditionalItemDialog();
    }

    private void dataReservationAdditionalItemDeleteHandle() {
        if (tableDataReservationAdditionalItemNew.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            newSelectedAdditionalItem = (TblReservationAdditionalItem) tableDataReservationAdditionalItemNew.getTableView().getSelectionModel().getSelectedItem();
            if (newSelectedAdditionalItem.getRefReservationBillType().getIdtype() == 4) {    //Include
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN BARANG",
                        "Tidak dapat menghapus data tambahan barang dengan status 'Include'..!", null);
            } else {
//                if (!(LocalDateTime.of(newSelectedAdditionalItem.getAdditionalDate().getYear() + 1900,
//                        newSelectedAdditionalItem.getAdditionalDate().getMonth() + 1,
//                        newSelectedAdditionalItem.getAdditionalDate().getDate(),
//                        reservationController.defaultCheckOutTime.getHours(),
//                        reservationController.defaultCheckOutTime.getMinutes(),
//                        0).plusDays(1)).isBefore(LocalDateTime.now())) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //reservation additional item
                    newDataReservationAdditionalItems.remove(newSelectedAdditionalItem);
                    tableDataReservationAdditionalItemNew.getTableView().setItems(FXCollections.observableArrayList(newDataReservationAdditionalItems));
                    refreshDataBillNew();
                    initTableDataRCOTransactionNew();
                }
//                } else {
//                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sudah lewat (hari pemesanan) tidak dapat dihapus..!", null);
//                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private BigDecimal calculationTotalAdditionalItem(TblReservationAdditionalItem addtionalItem,
            TblReservationBill bill) {
        BigDecimal result = new BigDecimal("0");
        if (addtionalItem != null) {
            result = ((new BigDecimal("1")).subtract(addtionalItem.getDiscountPercentage()))
                    .multiply(addtionalItem.getItemQuantity().multiply(addtionalItem.getItemCharge()));
            BigDecimal service = bill.getServiceChargePercentage().multiply(result);
            BigDecimal tax = bill.getTaxPercentage().multiply((result.add(service)));
            result = result.add(service).add(tax);
        }
        return result;
    }

    private void showReservationAdditionalItemDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rr/RRReservationAdditionalItemInputDialog.fxml"));

            RRReservationAdditionalItemInputController controller = new RRReservationAdditionalItemInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION ADDITIONAL SERVICE
     */
    public TblReservationAdditionalService newSelectedAdditionalService;

    public List<TblReservationAdditionalService> newSelectedAdditionalServices;

    private void dataReservationAdditionalServiceCreateHandle() {
        //set selected data
        newSelectedAdditionalService = new TblReservationAdditionalService();
        newSelectedAdditionalService.setPrice(new BigDecimal("0"));
        newSelectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
        newSelectedAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));    //Reservasi = '0'
        //open form data reservation additional service dialog
        showReservationAdditionalServiceDialog();
    }

    private void dataReservationAdditionalServiceDeleteHandle() {
        if (tableDataReservationAdditionalServiceNew.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            newSelectedAdditionalService = (TblReservationAdditionalService) tableDataReservationAdditionalServiceNew.getTableView().getSelectionModel().getSelectedItem();
            if (newSelectedAdditionalService.getRefReservationBillType().getIdtype() == 4) { //Include = '4'
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
                        "Tidak dapat menghapus data tambahan layanan dengan status 'Include'..!", null);
            } else {
//                if (!(LocalDateTime.of(newSelectedAdditionalService.getAdditionalDate().getYear() + 1900,
//                        newSelectedAdditionalService.getAdditionalDate().getMonth() + 1,
//                        newSelectedAdditionalService.getAdditionalDate().getDate(),
//                        reservationController.defaultCheckOutTime.getHours(),
//                        reservationController.defaultCheckOutTime.getMinutes(),
//                        0).plusDays(1)).isBefore(LocalDateTime.now())) {
                if (newSelectedAdditionalService.getTblRoomService().getIdroomService() != 2 //early checkin = '2'
                        && newSelectedAdditionalService.getTblRoomService().getIdroomService() != 3 //late checkout = '3'
                        && newSelectedAdditionalService.getTblRoomService().getIdroomService() != 4 //bouns voucher = '4'
                        && newSelectedAdditionalService.getTblRoomService().getIdroomService() != 5) {  //canceling fee = '5'
                    Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                    if (alert.getResult() == ButtonType.OK) {
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        //reservation additional service : remove
                        newDataReservationAdditionalServices.remove(newSelectedAdditionalService);
                        tableDataReservationAdditionalServiceNew.getTableView().setItems(FXCollections.observableArrayList(newDataReservationAdditionalServices));
                        refreshDataBillNew();
                        initTableDataRCOTransactionNew();
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data buatan sistem tidak dapat dihapus (tambahan biaya 'early checkin', 'late checkout', atau 'voucher(bonus)')..!", null);
                }
//                } else {
//                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sudah lewat (hari pemesanan) tidak dapat dihapus..!", null);
//                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private BigDecimal calculationTotalAdditionalService(TblReservationAdditionalService addtionalService,
            TblReservationBill bill) {
        BigDecimal result = new BigDecimal("0");
        if (addtionalService != null) {
            result = ((new BigDecimal("1")).subtract(addtionalService.getDiscountPercentage()))
                    .multiply(addtionalService.getPrice());
            BigDecimal service = new BigDecimal("0");
            BigDecimal tax = new BigDecimal("0");
            if (addtionalService.getTblRoomService().getIdroomService() != 6 //Room Dining = '6'
                    && addtionalService.getTblRoomService().getIdroomService() != 7) {   //Dine in Resto = '7'
                service = bill.getServiceChargePercentage().multiply(result);
                tax = bill.getTaxPercentage().multiply((result.add(service)));
            }
            result = result.add(service).add(tax);
        }
        return result;
    }

    private void showReservationAdditionalServiceDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rr/RRReservationAdditionalServiceInputDialog.fxml"));

            RRReservationAdditionalServiceInputController controller = new RRReservationAdditionalServiceInputController(this);
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

    private void dataReservationRescheduleSaveHandle() {
        if (checkDataInputDataReservationRescheduleCancel()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(reservationController.selectedData);
                if (calculationTotalRestOfBill().compareTo(new BigDecimal("0")) == 1) {
                    dummySelectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(1));  //Reserved = '1'
                } else {
//                    dummySelectedData.setRefReservationStatus(new RefReservationStatus(dummySelectedData.getRefReservationStatus()));
                    dummySelectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
                }
                TblReservationCancelingFee dummySelectedDataCancelingFee = new TblReservationCancelingFee(reservationController.selectedDataCancelingFee);
                dummySelectedDataCancelingFee.setTblReservation(dummySelectedData);
                List<TblReservationRescheduleCanceled> dummyDataReservationRescheduleCanceleds = new ArrayList<>();
                for (TblReservationRescheduleCanceled newReservationReschedule : newReservationReschedules) {
                    TblReservationRescheduleCanceled dummyDataReservationRescheduleCanceled = new TblReservationRescheduleCanceled(newReservationReschedule);
                    dummyDataReservationRescheduleCanceled.setTblReservation(dummySelectedData);
                    dummyDataReservationRescheduleCanceled.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummyDataReservationRescheduleCanceled.getTblReservationRoomTypeDetail()));
                    if (dummyDataReservationRescheduleCanceled.getTblItem() != null) {
                        dummyDataReservationRescheduleCanceled.setTblItem(new TblItem(dummyDataReservationRescheduleCanceled.getTblItem()));
                    }
                    if (dummyDataReservationRescheduleCanceled.getTblRoomService() != null) {
                        dummyDataReservationRescheduleCanceled.setTblRoomService(new TblRoomService(dummyDataReservationRescheduleCanceled.getTblRoomService()));
                    }
                    if (dummyDataReservationRescheduleCanceled.getTblHotelEvent() != null) {
                        dummyDataReservationRescheduleCanceled.setTblHotelEvent(new TblHotelEvent(dummyDataReservationRescheduleCanceled.getTblHotelEvent()));
                    }
                    if (dummyDataReservationRescheduleCanceled.getTblBankEventCard() != null) {
                        dummyDataReservationRescheduleCanceled.setTblBankEventCard(new TblBankEventCard(dummyDataReservationRescheduleCanceled.getTblBankEventCard()));
                    }
                    dummyDataReservationRescheduleCanceleds.add(dummyDataReservationRescheduleCanceled);
                }
                List<TblReservationRoomTypeDetail> dummyDataRoomTypeDetails = new ArrayList<>();
                List<TblReservationRoomTypeDetailRoomPriceDetail> dummyDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
                List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dummyDataRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
                List<TblReservationAdditionalItem> dummyDataReservationAdditionalItems = new ArrayList<>();
                List<TblReservationAdditionalService> dummyDataReservationAdditionalServices = new ArrayList<>();
                for (TblReservationRoomTypeDetail dataRoomTypeDetail : newDataReservationRoomTypeDetails) {
                    TblReservationRoomTypeDetail dummyDataRoomTypeDetail = new TblReservationRoomTypeDetail(dataRoomTypeDetail);
                    dummyDataRoomTypeDetail.setTblReservation(dummySelectedData);
                    dummyDataRoomTypeDetail.setTblRoomType(new TblRoomType(dummyDataRoomTypeDetail.getTblRoomType()));
                    if (dummyDataRoomTypeDetail.getTblReservationCheckInOut() != null) {
                        dummyDataRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummyDataRoomTypeDetail.getTblReservationCheckInOut()));
                        if (dummyDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() != null) {
                            dummyDataRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummyDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom()));
                        }
                        if (dummyDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                            dummyDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummyDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        }
                    }
                    for (TblReservationRoomTypeDetailRoomPriceDetail dataRoomTypeDetailRoomPriceDetail : newDataReservationRoomTypeDetailRoomPriceDetails) {
                        if (dataRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail)) {
                            TblReservationRoomTypeDetailRoomPriceDetail dummyDataRoomTypeDetailRoomPriceDetail = new TblReservationRoomTypeDetailRoomPriceDetail(dataRoomTypeDetailRoomPriceDetail);
                            dummyDataRoomTypeDetailRoomPriceDetail.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail);
                            dummyDataRoomTypeDetailRoomPriceDetail.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummyDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail()));
                            if (dummyDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                                dummyDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummyDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                            }
                            if (dummyDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                                dummyDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummyDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                            }
                            dummyDataRoomTypeDetailRoomPriceDetails.add(dummyDataRoomTypeDetailRoomPriceDetail);
                        }
                    }
                    for (TblReservationRoomTypeDetailTravelAgentDiscountDetail dataRoomTypeDetailTravelAgentDiscountDetail : newDataReservationRoomTypeDetailTravelAgentDiscountDetails) {
                        if (dataRoomTypeDetailTravelAgentDiscountDetail.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail)) {
                            TblReservationRoomTypeDetailTravelAgentDiscountDetail dummyDataRoomTypeDetailTravelAgentDiscountDetail = new TblReservationRoomTypeDetailTravelAgentDiscountDetail(dataRoomTypeDetailTravelAgentDiscountDetail);
                            dummyDataRoomTypeDetailTravelAgentDiscountDetail.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail);
                            dummyDataRoomTypeDetailTravelAgentDiscountDetail.setTblReservationTravelAgentDiscountDetail(new TblReservationTravelAgentDiscountDetail(dummyDataRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail()));
                            dummyDataRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail().setTblPartner(new TblPartner(dummyDataRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail().getTblPartner()));
                            dummyDataRoomTypeDetailTravelAgentDiscountDetails.add(dummyDataRoomTypeDetailTravelAgentDiscountDetail);
                        }
                    }
                    for (TblReservationAdditionalItem dataReservationAdditionalItem : newDataReservationAdditionalItems) {
                        if (dataReservationAdditionalItem.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail)) {
                            TblReservationAdditionalItem dummyDataReservationAdditionalItem = new TblReservationAdditionalItem(dataReservationAdditionalItem);
                            dummyDataReservationAdditionalItem.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail);
                            dummyDataReservationAdditionalItem.setTblItem(new TblItem(dummyDataReservationAdditionalItem.getTblItem()));
                            dummyDataReservationAdditionalItem.setRefReservationBillType(new RefReservationBillType(dummyDataReservationAdditionalItem.getRefReservationBillType()));
                            if (dummyDataReservationAdditionalItem.getTblHotelEvent() != null) {
                                dummyDataReservationAdditionalItem.setTblHotelEvent(new TblHotelEvent(dummyDataReservationAdditionalItem.getTblHotelEvent()));
                            }
                            if (dummyDataReservationAdditionalItem.getTblBankEventCard() != null) {
                                dummyDataReservationAdditionalItem.setTblBankEventCard(new TblBankEventCard(dummyDataReservationAdditionalItem.getTblBankEventCard()));
                            }
                            dummyDataReservationAdditionalItems.add(dummyDataReservationAdditionalItem);
                        }
                    }
                    for (TblReservationAdditionalService dataReservationAdditionalService : newDataReservationAdditionalServices) {
                        if (dataReservationAdditionalService.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail)) {
                            TblReservationAdditionalService dummyDataReservationAdditionalService = new TblReservationAdditionalService(dataReservationAdditionalService);
                            dummyDataReservationAdditionalService.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail);
                            dummyDataReservationAdditionalService.setTblRoomService(new TblRoomService(dummyDataReservationAdditionalService.getTblRoomService()));
                            dummyDataReservationAdditionalService.setRefReservationBillType(new RefReservationBillType(dummyDataReservationAdditionalService.getRefReservationBillType()));
                            if (dummyDataReservationAdditionalService.getTblHotelEvent() != null) {
                                dummyDataReservationAdditionalService.setTblHotelEvent(new TblHotelEvent(dummyDataReservationAdditionalService.getTblHotelEvent()));
                            }
                            if (dummyDataReservationAdditionalService.getTblBankEventCard() != null) {
                                dummyDataReservationAdditionalService.setTblBankEventCard(new TblBankEventCard(dummyDataReservationAdditionalService.getTblBankEventCard()));
                            }
                            dummyDataReservationAdditionalServices.add(dummyDataReservationAdditionalService);
                        }
                    }
                    dummyDataRoomTypeDetails.add(dummyDataRoomTypeDetail);
                }
                if (reservationController.getFReservationManager().insertDataReservationReschedule(
                        dummySelectedData,
                        dummySelectedDataCancelingFee,
                        dummyDataReservationRescheduleCanceleds,
                        dummyDataRoomTypeDetails,
                        dummyDataRoomTypeDetailRoomPriceDetails,
                        dummyDataRoomTypeDetailTravelAgentDiscountDetails,
                        dummyDataReservationAdditionalItems,
                        dummyDataReservationAdditionalServices) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", reservationController.dialogStage);
                    //refresh list data reservation
                    reservationController.refreshDataTableReservation();
                    //close form data reservation canceled input
                    reservationController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(reservationController.getFReservationManager().getErrorMessage(), reservationController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataReservationRescheduleCancelHandle() {
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data close deposit input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationRescheduleCancel() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCurrentCancelingFeeRCONew.getText() == null
                || txtCurrentCancelingFeeRCONew.getText().equals("")
                || txtCurrentCancelingFeeRCONew.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Cancellation Fee (sekarang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedDataCancelingFee.getCancelingFeeNominal().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Nominal Cancellation Fee (sekarang) : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (tableDataReservationRoomTypeDetailNew.getTableView().getItems().isEmpty()) {
            dataInput = false;
            errDataInput += "Reservasi Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        //init form input
        initFormDataReservationReschedule();

        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationRescheduleController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

    public ReservationController getParent() {
        return reservationController;
    }

}
