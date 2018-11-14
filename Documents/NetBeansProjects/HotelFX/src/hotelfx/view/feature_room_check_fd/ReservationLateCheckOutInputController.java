/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillDiscountType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationCancelingFee;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithReservationVoucher;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRescheduleCanceled;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgent;
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
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ReservationLateCheckOutInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCheckOut;

    @FXML
    private GridPane gpFormDataCheckOut;

    @FXML
    private GridPane gpFormDataLateCheckOut;

    @FXML
    private JFXTextField txtCheckOutDateTimeDefault;

    @FXML
    private JFXTextField txtCheckOutDateTimeNow;

    @FXML
    private JFXTextField txtTimeDistance;

    @FXML
    private JFXTextField txtAdditionalCharge;

    @FXML
    private JFXCheckBox chbGetFullRoomTypeDetailCost;

    @FXML
    private JFXTextArea txtAdditionalNote;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtCardUsedNumber;

    @FXML
    private JFXTextField txtTotalDeposit;

    @FXML
    private JFXTextField txtCardReturnNumber;

    @FXML
    private JFXTextField txtCardBrokenNumber;

    @FXML
    private JFXTextField txtCardBrokenCharge;

    @FXML
    private JFXTextField txtTotalCharge;

    @FXML
    private JFXTextField txtTotalDepositMustBeReturn;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataLateCheckOut() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Check Out)"));
        btnSave.setOnAction((e) -> {
            dataCheckOutSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCheckOutCancelHandle();
        });

        //total deposit
        txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(roomStatusFDController.selectedDataCheckOut.getCardUsed()))
                .multiply(roomStatusFDController.selectedDataCheckOut.getBrokenCardCharge())));

        //total charge
        txtTotalCharge.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(roomStatusFDController.selectedDataCheckOut.getCardMissed()))
                .multiply(roomStatusFDController.selectedDataCheckOut.getBrokenCardCharge())));
        roomStatusFDController.selectedDataCheckOut.cardMissedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalCharge.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(roomStatusFDController.selectedDataCheckOut.getBrokenCardCharge())));
            } else {
                txtTotalCharge.setText(ClassFormatter.currencyFormat.cFormat(0));
            }
        });

        //total deposit must be return
        txtTotalDepositMustBeReturn.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(cardReturnNumber.get()))
                .multiply(roomStatusFDController.selectedDataCheckOut.getBrokenCardCharge())));
        cardReturnNumber.addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalDepositMustBeReturn.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(roomStatusFDController.selectedDataCheckOut.getBrokenCardCharge())));
                roomStatusFDController.selectedDataCheckOut.setCardMissed(roomStatusFDController.selectedDataCheckOut.getCardUsed() - newValue.intValue());
            } else {
                txtTotalDepositMustBeReturn.setText(ClassFormatter.currencyFormat.cFormat(0));
                roomStatusFDController.selectedDataCheckOut.setCardMissed(roomStatusFDController.selectedDataCheckOut.getCardUsed());
            }
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCardReturnNumber,
                txtAdditionalCharge);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAdditionalCharge,
                txtCardReturnNumber,
                txtCardBrokenCharge);
        ClassFormatter.setToNumericField(
                "Integer",
                txtCardUsedNumber,
                txtCardBrokenNumber);
    }

    public TblReservation selectedData;

    public TblReservationBill dataReservationBill;

    public List<TblReservationRoomTypeDetail> dataReservationRoomTypeDetails;

    public List<TblReservationRoomTypeDetailRoomPriceDetail> dataReservationRoomTypeDetailRoomPriceDetails;

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dataReservationRoomTypeDetailTravelAgentDiscountDetails;

    public List<TblReservationAdditionalItem> dataReservationAdditionalItems;

    public List<TblReservationAdditionalService> dataReservationAdditionalServices;

    public List<TblReservationBrokenItem> dataReservationBrokenItems;

    public List<TblReservationRescheduleCanceled> dataReservationReschedules;

    public List<TblReservationCancelingFee> dataReservationCancelingFees;

    public List<ReservationBillAndPaymentController.ReservationBillPaymentDetail> dataRCOPayments;

    public List<TblReservationPayment> dataReservationPayments;

    public List<TblReservationPaymentWithTransfer> dataReservationPaymentWithTransfers;

    public List<TblReservationPaymentWithBankCard> dataReservationPaymentWithBankCards;

    public List<TblReservationPaymentWithCekGiro> dataReservationPaymentWithCekGiros;

    public List<TblReservationPaymentWithGuaranteePayment> dataReservationPaymentWithGuaranteePayments;

    public List<TblGuaranteeLetterItemDetail> dataGuaranteeLetterItemDetails;

    public List<TblReservationPaymentWithReservationVoucher> dataReservationPaymentWithReservationVouchers;

    public TblTravelAgent currentTravelAgent;

    public RefReservationOrderByType reservationType;

    private final IntegerProperty cardReturnNumber = new SimpleIntegerProperty();

    private void setSelectedDataToInputForm() {
        //data all reservation
        loadDataReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getIdreservation());

        //data late checkout
        txtCheckOutDateTimeDefault.setText(ClassFormatter.dateFormate.format(roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime())
                + " " + (ClassFormatter.timeFormate.format(roomStatusFDController.defaultCheckOutTime)));
        txtCheckOutDateTimeNow.setText(ClassFormatter.dateTimeFormate.format(Timestamp.valueOf(LocalDateTime.now())));
        txtTimeDistance.setText(ClassFormatter.decimalFormat.cFormat(calculationTimeDistance()) + " Jam");
        Bindings.bindBidirectional(txtAdditionalCharge.textProperty(), roomStatusFDController.selectedAdditionalService.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtAdditionalNote.textProperty().bindBidirectional(roomStatusFDController.selectedAdditionalService.noteProperty());

        chbGetFullRoomTypeDetailCost.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                roomStatusFDController.selectedAdditionalService.setPrice(getFullRoomTypeDetailCost());
            }
        });
        chbGetFullRoomTypeDetailCost.setSelected(false);

        roomStatusFDController.selectedAdditionalService.priceProperty().addListener((obs, oldVal, newVal) -> {
            if (chbGetFullRoomTypeDetailCost.isSelected()
                    && (newVal.compareTo(getFullRoomTypeDetailCost()) != 0)) {
                chbGetFullRoomTypeDetailCost.setSelected(false);
            }
        });

        //data checkout
        cardReturnNumber.set(roomStatusFDController.selectedDataCheckOut.getCardUsed());
        Bindings.bindBidirectional(txtCardReturnNumber.textProperty(), cardReturnNumber, new NumberStringConverter());

        txtRoomName.textProperty().bindBidirectional(roomStatusFDController.selectedDataCheckOut.getTblRoom().roomNameProperty());
        Bindings.bindBidirectional(txtCardUsedNumber.textProperty(), roomStatusFDController.selectedDataCheckOut.cardUsedProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCardBrokenCharge.textProperty(), roomStatusFDController.selectedDataCheckOut.brokenCardChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtCardBrokenNumber.textProperty(), roomStatusFDController.selectedDataCheckOut.cardMissedProperty(), new NumberStringConverter());
    }

    private void loadDataReservation(long idReservation) {
        //data reservation
        selectedData = roomStatusFDController.getServiceRV2().getReservation(idReservation);
        selectedData.setTblCustomer(roomStatusFDController.getServiceRV2().getCustomer(selectedData.getTblCustomer().getIdcustomer()));
        selectedData.getTblCustomer().setTblPeople(roomStatusFDController.getServiceRV2().getPeople(selectedData.getTblCustomer().getTblPeople().getIdpeople()));
        selectedData.setRefReservationStatus(roomStatusFDController.getServiceRV2().getReservationStatus(selectedData.getRefReservationStatus().getIdstatus()));
        //data reservation bill
        dataReservationBill = roomStatusFDController.getServiceRV2().getReservationBillByIDReservation(selectedData.getIdreservation());
        dataReservationBill.setTblReservation(selectedData);
        if (dataReservationBill.getTblBankCard() != null) {
            dataReservationBill.setTblBankCard(roomStatusFDController.getServiceRV2().getDataBankCard(dataReservationBill.getTblBankCard().getIdbankCard()));
            dataReservationBill.getTblBankCard().setTblBank(roomStatusFDController.getServiceRV2().getBank(dataReservationBill.getTblBankCard().getTblBank().getIdbank()));
            dataReservationBill.getTblBankCard().setRefBankCardType(roomStatusFDController.getServiceRV2().getDataBankCardType(dataReservationBill.getTblBankCard().getRefBankCardType().getIdtype()));
        }
        if (dataReservationBill.getRefReservationBillDiscountType() != null) {
            dataReservationBill.setRefReservationBillDiscountType(roomStatusFDController.getServiceRV2().getReservationBillDiscountType(dataReservationBill.getRefReservationBillDiscountType().getIdtype()));
        }
        //data reservation room type detail
        dataReservationRoomTypeDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailByIDReservation(selectedData.getIdreservation());
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                data.setTblReservationCheckInOut(roomStatusFDController.getServiceRV2().getReservationCheckInOut(data.getTblReservationCheckInOut().getIdcheckInOut()));
                if (data.getTblReservationCheckInOut().getTblRoom() != null) {
                    data.getTblReservationCheckInOut().setTblRoom(roomStatusFDController.getServiceRV2().getRoom(data.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                }
            }
        }
        //data reservation additional item
        dataReservationAdditionalItems = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationAdditionalItems.addAll(roomStatusFDController.getServiceRV2().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        //data reservation additional service
        dataReservationAdditionalServices = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationAdditionalServices.addAll(roomStatusFDController.getServiceRV2().getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        //data reservation broken item
        dataReservationBrokenItems = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            List<TblReservationBrokenItem> listData = roomStatusFDController.getServiceRV2().getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(data.getIddetail());
            for (TblReservationBrokenItem ld : listData) {
                //data item
                ld.setTblItem(roomStatusFDController.getServiceRV2().getDataItem(ld.getTblItem().getIditem()));
                //data unit
                ld.getTblItem().setTblUnit(roomStatusFDController.getServiceRV2().getDataUnit(ld.getTblItem().getTblUnit().getIdunit()));
            }
            dataReservationBrokenItems.addAll(listData);
        }
        //data reservation reschedule canceled
        dataReservationReschedules = new ArrayList<>();
        dataReservationReschedules.addAll(roomStatusFDController.getServiceRV2().getAllDataReservationRescheduleCanceledByIDReservation(selectedData.getIdreservation()));
        //data reservation canceling fee
        dataReservationCancelingFees = new ArrayList<>();
        dataReservationCancelingFees.addAll(roomStatusFDController.getServiceRV2().getAllDataReservationCancelingFeeByIDReservation(selectedData.getIdreservation()));
        //data reservation payment
        dataReservationPayments = roomStatusFDController.getServiceRV2().getAllDataReservationPaymentByIDReservationBill(dataReservationBill.getIdbill());
        //data reservation payment (detail)
        dataReservationPaymentWithTransfers = new ArrayList<>();
        dataReservationPaymentWithBankCards = new ArrayList<>();
        dataReservationPaymentWithCekGiros = new ArrayList<>();
        dataReservationPaymentWithGuaranteePayments = new ArrayList<>();
        dataGuaranteeLetterItemDetails = new ArrayList<>();
        dataReservationPaymentWithReservationVouchers = new ArrayList<>();
        for (TblReservationPayment dataReservationPayment : dataReservationPayments) {
            switch (dataReservationPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
                case 1:    //Transfer = '1'
                    dataReservationPaymentWithTransfers.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithTransferByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 2:   //Kartu Debit = '2'
                    dataReservationPaymentWithBankCards.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithBankCardByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 3:  //Kartu Kredit '3'
                    dataReservationPaymentWithBankCards.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithBankCardByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 4:     //Cek = '4'
                    dataReservationPaymentWithCekGiros.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithCekGiroByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 5:    //Giro = '5'
                    dataReservationPaymentWithCekGiros.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithCekGiroByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 6:    //Travel Agent = '6'
                    TblReservationPaymentWithGuaranteePayment dataTAGL = roomStatusFDController.getServiceRV2().getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment());
                    dataReservationPaymentWithGuaranteePayments.add(dataTAGL);
                    dataGuaranteeLetterItemDetails.addAll(roomStatusFDController.getServiceRV2().getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(dataTAGL.getIddetail()));
                    break;
                case 7:    //Guarantee Letter (Corporate) = '7'
                    TblReservationPaymentWithGuaranteePayment dataCGL = roomStatusFDController.getServiceRV2().getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment());
                    dataReservationPaymentWithGuaranteePayments.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment()));
                    dataGuaranteeLetterItemDetails.addAll(roomStatusFDController.getServiceRV2().getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(dataCGL.getIddetail()));
                    break;
                case 8:   //Guarantee Letter (Government) = '8'
                    TblReservationPaymentWithGuaranteePayment dataGGL = roomStatusFDController.getServiceRV2().getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment());
                    dataReservationPaymentWithGuaranteePayments.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment()));
                    dataGuaranteeLetterItemDetails.addAll(roomStatusFDController.getServiceRV2().getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(dataGGL.getIddetail()));
                    break;
                case 10: //Voucher = '10'
                    dataReservationPaymentWithReservationVouchers.add(roomStatusFDController.getServiceRV2().getReservationPaymentWithReservationVoucherByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                default:
                    break;
            }
        }
        //data reservation room type detail - room price detail
        dataReservationRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationRoomTypeDetailRoomPriceDetails.addAll(roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            data.setTblReservationRoomTypeDetail(roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(data.getTblReservationRoomTypeDetail().getIddetail()));
            data.setTblReservationRoomPriceDetail(roomStatusFDController.getServiceRV2().getReservationRoomPriceDetail(data.getTblReservationRoomPriceDetail().getIddetail()));
        }
        //data reservation room type detail - travel agent discount detail
        dataReservationRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationRoomTypeDetailTravelAgentDiscountDetails.addAll(roomStatusFDController.getServiceRV2().getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        for (TblReservationRoomTypeDetailTravelAgentDiscountDetail data : dataReservationRoomTypeDetailTravelAgentDiscountDetails) {
            data.setTblReservationRoomTypeDetail(roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(data.getTblReservationRoomTypeDetail().getIddetail()));
            data.setTblReservationTravelAgentDiscountDetail(roomStatusFDController.getServiceRV2().getReservationTravelAgentDiscountDetail(data.getTblReservationTravelAgentDiscountDetail().getIddetail()));
        }
        //Travel Agent
        if (selectedData.getTblPartner() != null) {   //Travel Agent
            selectedData.setTblPartner(roomStatusFDController.getServiceRV2().getPartner(selectedData.getTblPartner().getIdpartner()));
            currentTravelAgent = roomStatusFDController.getServiceRV2().getTravelAgentByIDPartner(selectedData.getTblPartner().getIdpartner());
            reservationType = roomStatusFDController.getServiceRV2().getReservationOrderByType(1);
        } else {  //Customer
            currentTravelAgent = null;
            reservationType = roomStatusFDController.getServiceRV2().getReservationOrderByType(0);
        }
    }

    public BigDecimal calculationTotalReservationRoom() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailPrice());
        }
        return result;
    }

    public BigDecimal calculationTotalReservationRoomCompliment() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailComplimentary());
        }
        return result;
    }

    public BigDecimal calculationTotalAdditional() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
            }
        }
        for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                result = result.add(data.getPrice());
            }
        }
        return result;
    }

    public BigDecimal calculationTotalAdditionalWithOutRestoBill() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
            }
        }
        for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
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
        for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailPrice().multiply(d.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
        }
        for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) { //Check Out = '1'
                result = result.add((data.getItemCharge().multiply(data.getItemQuantity())).multiply(data.getDiscountPercentage().divide(new BigDecimal("100"))));
            }
        }
        for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
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
        result = result.multiply(dataReservationBill.getServiceChargePercentage());
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
        result = result.multiply(dataReservationBill.getTaxPercentage());
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
        for (TblReservationCancelingFee data : dataReservationCancelingFees) {
            result = result.add(data.getCancelingFeeNominal());
        }
        return result;
    }

    public BigDecimal calculationTotalBroken() {
        BigDecimal result = new BigDecimal("0");
        //items
        for (TblReservationBrokenItem data : dataReservationBrokenItems) {
            result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
        }
        return result;
    }

    public int getTotalMissedCard() {
        int result = 0;
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                result += data.getTblReservationCheckInOut().getCardMissed();
            }
        }
        return result;
    }

    public BigDecimal calculationTotalMissedCardCharge() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                result = result.add(((new BigDecimal(data.getTblReservationCheckInOut().getCardMissed())).multiply(data.getTblReservationCheckInOut().getBrokenCardCharge())));
            }
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransaction() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationPayment data : dataReservationPayments) {
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
        for (TblReservationPayment data : dataReservationPayments) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Rservation = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {   //CheckOut = '1'
                if (data.getRefFinanceTransactionPaymentType().getIdtype() == 16) {   //Return = '16'
                    result = result.subtract(data.getRoundingValue());
                } else {
                    result = result.add(data.getRoundingValue());
                }
            }
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransactionDisountPayment() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationPaymentWithBankCard data : dataReservationPaymentWithBankCards) {
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

    public BigDecimal calculationTotalDepositNeeded() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetail rtd : dataReservationRoomTypeDetails) {
            if (rtd.getTblReservationCheckInOut() != null
                    && (rtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0 //Ready to Check In = '0'
                    || rtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1)) {    //Checked In = '1'
                result = result.add(((new BigDecimal(rtd.getTblReservationCheckInOut().getCardUsed())).multiply(rtd.getTblReservationCheckInOut().getBrokenCardCharge())));
            }
        }
        if (result.compareTo(selectedData.getTblCustomer().getDeposit()) == 1) {
            result = result.subtract(selectedData.getTblCustomer().getDeposit());
        } else {
            result = new BigDecimal("0");
        }
        return result;
    }

    public BigDecimal calculationRoomTypeDetailCost(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
            }
        }
        return result;
    }

    public BigDecimal calculationRoomTypeDetailDiscount(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice()
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
            }
        }
        return result;
    }

    private BigDecimal calculationRoomTypeDetailCompliment(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
            }
        }
        return result;
    }

    private double calculationTimeDistance() {
        double result = ChronoUnit.HOURS.between(LocalDateTime.now(), LocalDateTime.of(
                roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate(),
                roomStatusFDController.defaultCheckOutTime.getHours(), roomStatusFDController.defaultCheckOutTime.getMinutes()));
        return result < 0 ? result * (-1) : result;
    }

    //room type - date (after = 'checkout')
    private BigDecimal getFullRoomTypeDetailCost() {
        TblReservationBar dataBAR;
        TblReservationCalendarBar calendarBAR = roomStatusFDController.getServiceRV2().getReservationCalendarBARByCalendarDate(Date.valueOf((LocalDate.of(
                roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate()))));
        if (calendarBAR == null) {
            TblReservationDefaultBar defaultBAR = roomStatusFDController.getServiceRV2().getReservationDefaultBARByDayOfWeek(((LocalDate.of(
                    roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                    roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                    roomStatusFDController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate())))
                    .getDayOfWeek().getValue());
            dataBAR = roomStatusFDController.getServiceRV2().getReservationBAR(defaultBAR.getTblReservationBar().getIdbar());
        } else {
            dataBAR = roomStatusFDController.getServiceRV2().getReservationBAR(calendarBAR.getTblReservationBar().getIdbar());
        }
        return roomStatusFDController.selectedDataRoomTypeDetail.getTblRoomType().getRoomTypePrice().multiply(dataBAR.getBarpercentage());
    }

    private void dataCheckOutSaveHandle() {
        if (checkDataInputDataCheckOut()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusFDController.checkOutDialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data additional service (late checkin)
                roomStatusFDController.selectedAdditionalService.setAdditionalDate(Date.valueOf(LocalDate.now()));
                roomStatusFDController.selectedAdditionalService.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(0));  //Reservation = '0'
                //data checkout
                roomStatusFDController.selectedDataCheckOut.setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.now()));
                roomStatusFDController.selectedDataCheckOut.setRefReservationCheckInOutStatus(roomStatusFDController.getServiceRV2().getReservationCheckInOutStatus(3));
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
                TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail = new TblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
                dummySelectedDataReservationRoomTypeDetail.setTblReservation(dummySelectedData);
                dummySelectedDataReservationRoomTypeDetail.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail.getTblRoomType()));
                dummySelectedDataReservationRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut()));
                dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom()));
                dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                if (dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                    dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                }
                TblReservationAdditionalService dummySelectedAdditionalService = new TblReservationAdditionalService(roomStatusFDController.selectedAdditionalService);
                dummySelectedAdditionalService.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail);
                dummySelectedAdditionalService.setTblRoomService(new TblRoomService(dummySelectedAdditionalService.getTblRoomService()));
                TblReservationBill dummyDataReservationBill = new TblReservationBill(dataReservationBill);
                dummyDataReservationBill.setTblReservation(dummySelectedData);
                dummyDataReservationBill.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill.getRefReservationBillType()));
                if (dummyDataReservationBill.getTblBankCard() != null) {
                    dummyDataReservationBill.setTblBankCard(new TblBankCard(dummyDataReservationBill.getTblBankCard()));
                }
                if (dummyDataReservationBill.getRefReservationBillDiscountType() != null) {
                    dummyDataReservationBill.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill.getRefReservationBillDiscountType()));
                }
                if (roomStatusFDController.getServiceRV2().updateDataReservationRoomTypeDetailLateCheckOut(dummySelectedData,
                        dummySelectedDataReservationRoomTypeDetail,
                        dummySelectedAdditionalService,
                        dummyDataReservationBill,
                        calculationTotalRestOfBill())) {
                    ClassMessage.showSucceedUpdatingDataMessage("", roomStatusFDController.checkOutDialogStage);
                    //show bill & payment
                    roomStatusFDController.dataRoomStatusReservationBillAndPaymentHandle();
                    //dont show dialog (hide)
                    roomStatusFDController.checkOutDialogStage.hide();
                    // refresh all data
                    roomStatusFDController.setSelectedDataToInputForm();
                    //close form data
                    roomStatusFDController.checkOutDialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(roomStatusFDController.getServiceRV2().getErrorMessage(), roomStatusFDController.checkOutDialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusFDController.checkOutDialogStage);
        }
    }

    private void dataCheckOutCancelHandle() {
        //refresh all data and close form data - reservation additional service
        roomStatusFDController.setSelectedDataToInputForm();
        roomStatusFDController.checkOutDialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCheckOut() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtAdditionalCharge.getText() == null
                || txtAdditionalCharge.getText().equals("")
                || txtAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Tambahan Biaya : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomStatusFDController.selectedAdditionalService.getPrice().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Tambahan Biaya : Tidak dapat kurang dari '0' \n";
            }
        }
        if (txtCardReturnNumber.getText() == null
                || txtCardReturnNumber.getText().equals("")
                || txtCardReturnNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Kartu (Dikembalikan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (cardReturnNumber.get() < 0) {
                dataInput = false;
                errDataInput += "Jumlah Kartu (Dikembalikan) : Nilai tidak dapat kurang dari '0' \n";
            } else {
                if (cardReturnNumber.get() > roomStatusFDController.selectedDataCheckOut.getCardUsed()) {
                    dataInput = false;
                    errDataInput += "Jumlah Kartu (Dikembalikan) : Nilai tidak dapat lebih besar dari '" + roomStatusFDController.selectedDataCheckOut.getCardUsed() + "' \n";
                }
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
        //init form input
        initFormDataLateCheckOut();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationLateCheckOutInputController(RoomStatusFDController parentController) {
        roomStatusFDController = parentController;
    }

    private final RoomStatusFDController roomStatusFDController;

}
