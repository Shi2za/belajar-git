/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.PrintModel.ClassPrintBill;
import hotelfx.helper.PrintModel.ClassPrintBillDetail;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCancelingFee;
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
import hotelfx.persistence.model.TblTravelAgent;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
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
public class ReservationBillAndPaymentController implements Initializable {

    @FXML
    private Label lblServiceChargePercentageInRCOBill;

    @FXML
    private Label lblTaxPercentageInRCOBill;

    @FXML
    private JFXTextField txtTotalRoomCostRCO;

    @FXML
    private JFXTextField txtTotalRoomCostComplimentRCO;

    @FXML
    private JFXTextField txtTotalAdditionalCostRCO;

    @FXML
    private JFXTextField txtTotalDiscountRCO;

    @FXML
    private JFXTextField txtTotalCancelingFeeRCO;

    @FXML
    private JFXTextField txtTotalBrokenItemCostRCO;

    @FXML
    private AnchorPane ancBrokenItemInfoRCOLayout;
    private final JFXCComboBoxPopup<TblReservationBrokenItem> cbpReservationBrokenItemRCO = new JFXCComboBoxPopup<>(TblReservationBrokenItem.class);

    @FXML
    private JFXTextField txtTotalServiceChargeRCO;

    @FXML
    private JFXTextField txtTotalTaxRCO;

    @FXML
    private JFXTextField txtRCOBill;

    @FXML
    private Label lblRoundingValueInRCOBill;

    @FXML
    private JFXTextField txtTotalTransactionPaymentRCO;

    @FXML
    private AnchorPane ancTableTransactionRCOLayout;

    @FXML
    private Label lblDataCode;

    @FXML
    private JFXButton btnBack;

    private void initFormDataReservationBillAndPayment() {
        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            dataReservationBillAndPaymentCancelHandle();
        });
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

    public List<ReservationBillPaymentDetail> dataRCOPayments;

    public List<TblReservationPayment> dataReservationPayments;

    public List<TblReservationPaymentWithTransfer> dataReservationPaymentWithTransfers;

    public List<TblReservationPaymentWithBankCard> dataReservationPaymentWithBankCards;

    public List<TblReservationPaymentWithCekGiro> dataReservationPaymentWithCekGiros;

    public List<TblReservationPaymentWithGuaranteePayment> dataReservationPaymentWithGuaranteePayments;

    public List<TblGuaranteeLetterItemDetail> dataGuaranteeLetterItemDetails;

    public List<TblReservationPaymentWithReservationVoucher> dataReservationPaymentWithReservationVouchers;

    public TblTravelAgent currentTravelAgent;

    public RefReservationOrderByType reservationType;

    public void setSelectedDataToInputForm() {
        //data all reservation
        loadDataReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getIdreservation());

        lblDataCode.setText("Kamar "
                + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRoomName()
                + " (" + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getCodeReservation()
                + " : "
                + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getFullName() + ")"
                + " - Balance : "
                + ClassFormatter.currencyFormat.format(calculationTotalRestOfBill()));

        //data bill and transaction
        refreshDataBill();
        initTableDataRCOTransaction();
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
        //data reservation payment (RCO)
        dataRCOPayments = loadAllDataRCOTransaction();
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

    public void refreshDataBill() {
        lblServiceChargePercentageInRCOBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataReservationBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "%)");
        lblTaxPercentageInRCOBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataReservationBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "%)");
        txtTotalRoomCostRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoom()));
        txtTotalRoomCostComplimentRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoomCompliment()));
        txtTotalAdditionalCostRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditional()));
        txtTotalDiscountRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount()));
        txtTotalCancelingFeeRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalCancelingFee()));
        txtTotalBrokenItemCostRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBroken()));
        txtTotalServiceChargeRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge()));
        txtTotalTaxRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax()));
        txtRCOBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillAfterRounding()));
        lblRoundingValueInRCOBill.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransactionRoundingValue()) + ")");
        txtTotalTransactionPaymentRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransaction()));
    }

    /**
     * TABLE DATA RCO TRANSACTION
     */
    public ClassTableWithControl tableDataRCOTransaction;

    private void initTableDataRCOTransaction() {
        //set table
        setTableDataRCOTransaction();
        //set control
        setTableControlDataRCOTransaction();
        //set table-control to content-view
        ancTableTransactionRCOLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataRCOTransaction, 15.0);
        AnchorPane.setLeftAnchor(tableDataRCOTransaction, 15.0);
        AnchorPane.setBottomAnchor(tableDataRCOTransaction, 15.0);
        AnchorPane.setRightAnchor(tableDataRCOTransaction, 15.0);

        ancTableTransactionRCOLayout.getChildren().add(tableDataRCOTransaction);
    }

    private void setTableDataRCOTransaction() {
        TableView<ReservationBillPaymentDetail> tableView = new TableView();

        TableColumn<ReservationBillPaymentDetail, String> detailDate = new TableColumn("Tanggal");
        detailDate.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDetailDate()),
                        param.getValue().detailDateProperty()));
        detailDate.setMinWidth(120);
        detailDate.setSortable(false);

        TableColumn<ReservationBillPaymentDetail, String> detailDescription = new TableColumn("Keterangan");
        detailDescription.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDetailDescription(),
                        param.getValue().detailDescriptionProperty()));
        detailDescription.setMinWidth(300);
        detailDescription.setSortable(false);

        TableColumn<ReservationBillPaymentDetail, String> detailDebit = new TableColumn("Debit");
        detailDebit.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDetailDebit()),
                        param.getValue().detailDebitProperty()));
        detailDebit.setMinWidth(140);
        detailDebit.setSortable(false);

        TableColumn<ReservationBillPaymentDetail, String> detailCreadit = new TableColumn("Kredit");
        detailCreadit.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDetailCreadit()),
                        param.getValue().detailCreaditProperty()));
        detailCreadit.setMinWidth(140);
        detailCreadit.setSortable(false);

        tableView.getColumns().addAll(detailDate, detailDescription, detailDebit, detailCreadit);
        tableView.setItems(FXCollections.observableArrayList(dataRCOPayments));
        tableDataRCOTransaction = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataRCOTransaction() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Transaksi Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRCOTransactionCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak (Tagihan Reservasi)");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                printDataType = "ReservationBill";
                printHandle(0);
                // prePrintHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak (Transaksi Pembayaran)");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                printDataType = "ReservationReceipt";
//                prePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataRCOTransaction.addButtonControl(buttonControls);
    }

    public ObservableList<TblReservationPayment> loadAllDataReservationTransaction() {
        return FXCollections.observableArrayList(dataReservationPayments);
    }

    public ObservableList<ReservationBillPaymentDetail> loadAllDataRCOTransaction() {
        List<ReservationBillPaymentDetail> list = new ArrayList<>();
        //reservation - reschedule - canceled
        for (TblReservationRescheduleCanceled data : dataReservationReschedules) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //reservation - canceling fee
        for (TblReservationCancelingFee data : dataReservationCancelingFees) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //rrtd - rpd (room price detail)
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //additional service
        for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                list.add(new ReservationBillPaymentDetail(data));
            }
        }
        //additional item
        for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                list.add(new ReservationBillPaymentDetail(data));
            }
        }
        //broken item
        for (TblReservationBrokenItem data : dataReservationBrokenItems) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //payment
        for (TblReservationPayment data : dataReservationPayments) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //sorting by date
        sortingReservationBillPaymentDetailByDateASC(list);
        return FXCollections.observableArrayList(list);
    }

    public void sortingReservationBillPaymentDetailByDateASC(List<ReservationBillPaymentDetail> arr) {
        boolean swapped = true;
        int j = 0;
        ReservationBillPaymentDetail tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < arr.size() - j; i++) {
//                LocalDate date1 = LocalDate.of(arr.get(i).getDetailDate().getYear() + 1900, 
//                        arr.get(i).getDetailDate().getMonth() + 1, 
//                        arr.get(i).getDetailDate().getDate());
//                LocalDate date2 = LocalDate.of(arr.get(i+1).getDetailDate().getYear() + 1900, 
//                        arr.get(i+1).getDetailDate().getMonth() + 1, 
//                        arr.get(i+1).getDetailDate().getDate());
//                if (date1.isAfter(date2)) {
                if (arr.get(i).getDetailDate().after(arr.get(i + 1).getDetailDate())) {
                    tmp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }

    public class ReservationBillPaymentDetail {

        private final BooleanProperty detailSelected = new SimpleBooleanProperty();

        private final BooleanProperty detailEnableToSelect = new SimpleBooleanProperty();

        private final ObjectProperty<java.util.Date> detailDate = new SimpleObjectProperty<>();

        private final StringProperty detailDescription = new SimpleStringProperty();

        private final ObjectProperty<BigDecimal> detailDebit = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> detailCreadit = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> detailBalance = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationRoomTypeDetail> detailRRD = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> detailRRDRPD = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationAdditionalItem> detailAddtionalItem = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationAdditionalService> detailAddtionalService = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationBrokenItem> detailBrokenItem = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationRescheduleCanceled> detailRescheduleCanceled = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationCancelingFee> detailCancelingFee = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationPayment> detailPayment = new SimpleObjectProperty<>();

        ClassPrintBillDetail billDetail = new ClassPrintBillDetail();

        BigDecimal totalBalance = new BigDecimal(0);

        public ReservationBillPaymentDetail(TblReservationRoomTypeDetail rrtd) {
            //data selected

            //data date
            detailDate.set(rrtd.getCreateDate());
            //data description~~~
            detailDescription.set("K");
            //data debit
            BigDecimal nominal = calculationRoomTypeDetailCost(rrtd).subtract(calculationRoomTypeDetailCompliment(rrtd)).subtract(calculationRoomTypeDetailDiscount(rrtd));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));

            //data rrtd
        }

        public ReservationBillPaymentDetail(TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd) {
            //data selected

            //data date
            detailDate.set(rrtdrpd.getCreateDate());
            //data description~~~
            String roomID = (rrtdrpd.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && rrtdrpd.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? rrtdrpd.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : rrtdrpd.getTblReservationRoomTypeDetail().getCodeDetail();
            detailDescription.set("1 Kamar "
                    + rrtdrpd.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + " - "
                    + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate())
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + (rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "%") : "")
                    + (rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary().compareTo(new BigDecimal("0")) == 1
                            ? (", Compliment:" + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary())) : "")
                    + "]"
            );
            //data debit
            BigDecimal nominal = ((new BigDecimal("1")).subtract((rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
                    .multiply(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()).subtract(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary());
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            totalBalance = detailDebit.get().subtract(detailCreadit.get());
            billDetail.setBalance(totalBalance);
            billDetail.setCredit(detailCreadit.get());
            billDetail.setDebit(detailDebit.get());
            billDetail.setDetail("1 "
                    + rrtdrpd.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + " Room - "
                    + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate()));
            billDetail.setKeterangan("Room ID:" + roomID
                    + (rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Discount:" + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "%") : "")
                    + (rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary().compareTo(new BigDecimal("0")) == 1
                            ? (", Compliment:" + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary())) : ""));
            billDetail.setServiceCharge(serviceCharge);
            billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
            billDetail.setTax(tax);

            //data rrtd
        }

        public ReservationBillPaymentDetail(TblReservationAdditionalItem additionalItem) {
            //data selected

            //data date
            detailDate.set(additionalItem.getCreateDate());
            //data description
            String roomID = (additionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && additionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? additionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : additionalItem.getTblReservationRoomTypeDetail().getCodeDetail();
            detailDescription.set(ClassFormatter.decimalFormat.cFormat(additionalItem.getItemQuantity()) + " "
                    + additionalItem.getTblItem().getItemName() + " - "
                    + ClassFormatter.dateFormate.format(additionalItem.getAdditionalDate())
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + (additionalItem.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(additionalItem.getDiscountPercentage()) + "%") : "")
                    + "]"
            );
            //data debit
            BigDecimal nominal = ((new BigDecimal("1")).subtract((additionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
                    .multiply((additionalItem.getItemCharge().multiply(additionalItem.getItemQuantity())));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            totalBalance = totalBalance.add(detailDebit.get().subtract(detailCreadit.get()));
            billDetail.setBalance(totalBalance);
            billDetail.setCredit(detailCreadit.get());
            billDetail.setDebit(detailDebit.get());
            billDetail.setDetail(ClassFormatter.decimalFormat.cFormat(additionalItem.getItemQuantity()) + " "
                    + additionalItem.getTblItem().getItemName() + " - " + ClassFormatter.dateFormate.format(additionalItem.getAdditionalDate()));
            billDetail.setKeterangan("Room ID:" + roomID
                    + (additionalItem.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Discount:" + ClassFormatter.decimalFormat.cFormat(additionalItem.getDiscountPercentage()) + "%") : ""));
            billDetail.setServiceCharge(serviceCharge);
            billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
            billDetail.setTax(tax);
            //data additional item

        }

        public ReservationBillPaymentDetail(TblReservationAdditionalService additionalService) {
            //data selected

            //data date
            detailDate.set(additionalService.getCreateDate());
            //data description
            String roomID = (additionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && additionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? additionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : additionalService.getTblReservationRoomTypeDetail().getCodeDetail();
            LocalDate date = LocalDate.of(additionalService.getAdditionalDate().getYear() + 1900,
                    additionalService.getAdditionalDate().getMonth() + 1,
                    additionalService.getAdditionalDate().getDate());
            if (additionalService.getTblRoomService().getIdroomService() == 1) {   //Breakfast = '1'
                date = date.plusDays(1);
            }
            detailDescription.set("1 "
                    + additionalService.getTblRoomService().getServiceName() + " - "
                    + ClassFormatter.dateFormate.format(Date.valueOf(date))
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + (additionalService.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(additionalService.getDiscountPercentage()) + "%") : "")
                    + "]"
            );
            //data debit
            BigDecimal nominal = ((new BigDecimal("1")).subtract((additionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                    .multiply(additionalService.getPrice());
            BigDecimal serviceCharge = new BigDecimal("0");
            BigDecimal tax = new BigDecimal("0");
            if (additionalService.getTblRoomService().getIdroomService() != 6 //Room Dining = '6'
                    && additionalService.getTblRoomService().getIdroomService() != 7) {  //Dine in Resto = '7'
                serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
                tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            }
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data additional service

            totalBalance = totalBalance.add(detailDebit.get().subtract(detailCreadit.get()));
            billDetail.setBalance(totalBalance);
            billDetail.setCredit(detailCreadit.get());
            billDetail.setDebit(detailDebit.get());
            billDetail.setDetail("1 "
                    + additionalService.getTblRoomService().getServiceName() + " - "
                    + ClassFormatter.dateFormate.format(Date.valueOf(date)));
            billDetail.setKeterangan("Room ID:" + roomID
                    + (additionalService.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Discount:" + ClassFormatter.decimalFormat.cFormat(additionalService.getDiscountPercentage()) + "%") : ""));
            billDetail.setServiceCharge(serviceCharge);
            billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
            billDetail.setTax(tax);
        }

        public ReservationBillPaymentDetail(TblReservationBrokenItem brokenItem) {
            //data selected

            //data date
            detailDate.set(brokenItem.getCreateDate());
            //data description
            String roomID = (brokenItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && brokenItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? brokenItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : brokenItem.getTblReservationRoomTypeDetail().getCodeDetail();
            detailDescription.set(ClassFormatter.decimalFormat.cFormat(brokenItem.getItemQuantity()) + " "
                    + brokenItem.getTblItem().getItemName() + " - "
                    + ClassFormatter.dateFormate.format(brokenItem.getCreateDate())
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + "]"
            );
            //data debit
            BigDecimal nominal = (new BigDecimal("1"))
                    .multiply((brokenItem.getItemCharge().multiply(brokenItem.getItemQuantity())));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data broken item

            totalBalance = totalBalance.add(detailDebit.get().subtract(detailCreadit.get()));
            billDetail.setBalance(totalBalance);
            billDetail.setCredit(detailCreadit.get());
            billDetail.setDebit(detailDebit.get());
            billDetail.setDetail("1 "
                    + ClassFormatter.decimalFormat.cFormat(brokenItem.getItemQuantity()) + " "
                    + brokenItem.getTblItem().getItemName() + " - "
                    + ClassFormatter.dateFormate.format(brokenItem.getCreateDate()));
            billDetail.setKeterangan("Room ID :" + roomID);
            billDetail.setServiceCharge(serviceCharge);
            billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
            billDetail.setTax(tax);
        }

        public ReservationBillPaymentDetail(TblReservationRescheduleCanceled rescheduleCanceled) {
            //data selected

            //data date
            detailDate.set(rescheduleCanceled.getReservedDate());
            //data description
            String roomID = (rescheduleCanceled.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && rescheduleCanceled.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? rescheduleCanceled.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : rescheduleCanceled.getTblReservationRoomTypeDetail().getCodeDetail();
            if (rescheduleCanceled.getTblItem() != null) {    //additional item
                detailDescription.set(ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getQuantity()) + " "
                        + rescheduleCanceled.getTblItem().getItemName() + " - "
                        + ClassFormatter.dateFormate.format(rescheduleCanceled.getReservedForDate())
                        + "\n"
                        + "[ID Kamar:" + roomID
                        + (rescheduleCanceled.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                                ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getDiscountPercentage()) + "%") : "")
                        + "]"
                );
            } else {
                if (rescheduleCanceled.getTblRoomService() != null) { //additional service
                    LocalDate date = LocalDate.of(rescheduleCanceled.getReservedForDate().getYear() + 1900,
                            rescheduleCanceled.getReservedForDate().getMonth() + 1,
                            rescheduleCanceled.getReservedForDate().getDate());
                    if (rescheduleCanceled.getTblRoomService().getIdroomService() == 1) {   //Breakfast = '1'
                        date = date.plusDays(1);
                    }
                    detailDescription.set("1 "
                            + rescheduleCanceled.getTblRoomService().getServiceName() + " - "
                            + ClassFormatter.dateFormate.format(Date.valueOf(date))
                            + "\n"
                            + "[ID Kamar:" + roomID
                            + (rescheduleCanceled.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                                    ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getDiscountPercentage()) + "%") : "")
                            + "]"
                    );
                } else {  //reservation room
                    detailDescription.set("1 Kamar "
                            + rescheduleCanceled.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + " - "
                            + ClassFormatter.dateFormate.format(rescheduleCanceled.getReservedForDate())
                            + "\n"
                            + "[ID Kamar:" + roomID
                            + (rescheduleCanceled.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                                    ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getDiscountPercentage()) + "%") : "")
                            + (rescheduleCanceled.getComplimentary().compareTo(new BigDecimal("0")) == 1
                                    ? (", Compliment:" + ClassFormatter.currencyFormat.cFormat(rescheduleCanceled.getComplimentary())) : "")
                            + "]"
                    );
                }
            }
            BigDecimal serviceCharge;
            BigDecimal tax;
            BigDecimal nominal;
            if (rescheduleCanceled.getTblItem() != null) {    //additional item
                nominal = ((new BigDecimal("1")).subtract((rescheduleCanceled.getDiscountPercentage().divide(new BigDecimal("100")))))
                        .multiply((rescheduleCanceled.getPrice().multiply(rescheduleCanceled.getQuantity())));
                serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
                tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            } else {
                if (rescheduleCanceled.getTblRoomService() != null) { //additional service
                    nominal = ((new BigDecimal("1")).subtract((rescheduleCanceled.getDiscountPercentage().divide(new BigDecimal("100")))))
                            .multiply(rescheduleCanceled.getPrice());
                    serviceCharge = new BigDecimal("0");
                    tax = new BigDecimal("0");
                    if (rescheduleCanceled.getTblRoomService().getIdroomService() != 6 //Room Dining = '6'
                            && rescheduleCanceled.getTblRoomService().getIdroomService() != 7) {  //Dine in Resto = '7'
                        serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
                        tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
                    }
                } else {  //reservation room
                    nominal = ((new BigDecimal("1")).subtract((rescheduleCanceled.getDiscountPercentage().divide(new BigDecimal("100")))))
                            .multiply(rescheduleCanceled.getPrice()).subtract(rescheduleCanceled.getComplimentary());
                    serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
                    tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
                }
            }
            if (rescheduleCanceled.getPrice().compareTo(new BigDecimal("0")) == -1) {   //minus to 'creadit'
                //data debit
                detailDebit.set(new BigDecimal("0"));
                //data creadit
                detailCreadit.set((nominal.add(serviceCharge).add(tax)).abs());
            } else {    //plus to 'debit'
                //data debit
                detailDebit.set(nominal.add(serviceCharge).add(tax));
                //data creadit
                detailCreadit.set(new BigDecimal("0"));
            }
            //data reschedule canceled

            if (rescheduleCanceled.getTblItem() != null) {    //additional item
                totalBalance = totalBalance.add(detailDebit.get().subtract(detailCreadit.get()));
                billDetail.setBalance(totalBalance);
                billDetail.setCredit(detailCreadit.get());
                billDetail.setDebit(detailDebit.get());
                billDetail.setDetail(ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getQuantity()) + " "
                        + rescheduleCanceled.getTblItem().getItemName() + " - " + ClassFormatter.dateFormate.format(rescheduleCanceled.getReservedForDate()));
                billDetail.setKeterangan("Room ID:" + roomID
                        + (rescheduleCanceled.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                                ? (", Discount:" + ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getDiscountPercentage()) + "%") : ""));
                billDetail.setServiceCharge(serviceCharge);
                billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
                billDetail.setTax(tax);
            } else {
                if (rescheduleCanceled.getTblRoomService() != null) { //additional service
                    LocalDate date = LocalDate.of(rescheduleCanceled.getReservedForDate().getYear() + 1900,
                            rescheduleCanceled.getReservedForDate().getMonth() + 1,
                            rescheduleCanceled.getReservedForDate().getDate());
                    if (rescheduleCanceled.getTblRoomService().getIdroomService() == 1) {   //Breakfast = '1'
                        date = date.plusDays(1);
                    }
                    totalBalance = totalBalance.add(detailDebit.get().subtract(detailCreadit.get()));
                    billDetail.setBalance(totalBalance);
                    billDetail.setCredit(detailCreadit.get());
                    billDetail.setDebit(detailDebit.get());
                    billDetail.setDetail("1 "
                            + rescheduleCanceled.getTblRoomService().getServiceName() + " - "
                            + ClassFormatter.dateFormate.format(Date.valueOf(date)));
                    billDetail.setKeterangan("Room ID:" + roomID
                            + (rescheduleCanceled.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                                    ? (", Discount:" + ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getDiscountPercentage()) + "%") : ""));
                    billDetail.setServiceCharge(serviceCharge);
                    billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
                    billDetail.setTax(tax);
                } else {  //reservation room
                    totalBalance = detailDebit.get().subtract(detailCreadit.get());
                    billDetail.setBalance(totalBalance);
                    billDetail.setCredit(detailCreadit.get());
                    billDetail.setDebit(detailDebit.get());
                    billDetail.setDetail("1 "
                            + rescheduleCanceled.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + " Room - "
                            + ClassFormatter.dateFormate.format(rescheduleCanceled.getReservedForDate()));
                    billDetail.setKeterangan("Room ID:" + roomID
                            + (rescheduleCanceled.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                                    ? (", Discount:" + ClassFormatter.decimalFormat.cFormat(rescheduleCanceled.getDiscountPercentage()) + "%") : "")
                            + (rescheduleCanceled.getComplimentary().compareTo(new BigDecimal("0")) == 1
                                    ? (", Compliment:" + ClassFormatter.currencyFormat.cFormat(rescheduleCanceled.getComplimentary())) : ""));
                    billDetail.setServiceCharge(serviceCharge);
                    billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
                    billDetail.setTax(tax);
                }
            }
        }

        public ReservationBillPaymentDetail(TblReservationCancelingFee cancelingFee) {
            //data selected

            //data date
            detailDate.set(cancelingFee.getCreateDate() != null ? cancelingFee.getCreateDate() : Timestamp.valueOf(LocalDateTime.now()));
            //data description
            detailDescription.set("Cancellation Fee - "
                    + ClassFormatter.dateFormate.format(detailDate.get())
            );
            //data debit
            BigDecimal nominal = (new BigDecimal("1"))
                    .multiply((cancelingFee.getCancelingFeeNominal() != null ? cancelingFee.getCancelingFeeNominal() : new BigDecimal("0")));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data canceling fee

            totalBalance = totalBalance.add(detailDebit.get().subtract(detailCreadit.get()));
            billDetail.setBalance(totalBalance);
            billDetail.setCredit(detailCreadit.get());
            billDetail.setDebit(detailDebit.get());
            billDetail.setDetail("1 Cancellation Fee - "
                    + ClassFormatter.dateFormate.format(detailDate.get()));
            billDetail.setKeterangan("");
            billDetail.setServiceCharge(serviceCharge);
            billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
            billDetail.setTax(tax);
        }

        public ReservationBillPaymentDetail(TblReservationPayment payment) {
            //data selected
            detailSelected.set(true);
            detailEnableToSelect.set(false);
            //data date
            detailDate.set(payment.getPaymentDate());
            //data description
            String additionDescription = "";
            switch (payment.getRefFinanceTransactionPaymentType().getIdtype()) {
                case 1:    //Transfer = '1'
                    TblReservationPaymentWithTransfer dataTransfer = roomStatusFDController.getServiceRV2().getReservationPaymentWithTransferByIDPayment(payment.getIdpayment());
                    additionDescription = "#" + dataTransfer.getIddetail();
                    break;
                case 2:    //Kartu Debit = '2'
                    TblReservationPaymentWithBankCard dataDebitCard = roomStatusFDController.getServiceRV2().getReservationPaymentWithBankCardByIDPayment(payment.getIdpayment());
                    additionDescription = "#XX" + dataDebitCard.getBankCardNumber().substring(dataDebitCard.getBankCardNumber().length() - 3, dataDebitCard.getBankCardNumber().length());
                    break;
                case 3:    //Kartu Kredit = '3'
                    TblReservationPaymentWithBankCard dataCreaditCard = roomStatusFDController.getServiceRV2().getReservationPaymentWithBankCardByIDPayment(payment.getIdpayment());
                    additionDescription = "#XX" + dataCreaditCard.getBankCardNumber().substring(dataCreaditCard.getBankCardNumber().length() - 3, dataCreaditCard.getBankCardNumber().length());
                    break;
            }
            detailDescription.set("Pembayaran : " + payment.getRefFinanceTransactionPaymentType().getTypeName()
                    + additionDescription);
            if (payment.getRefFinanceTransactionPaymentType().getIdtype() == 16) {    //Return = '16'
                //data debit
                detailDebit.set(payment.getUnitNominal());
                //data creadit
                detailCreadit.set(payment.getRoundingValue());
            } else {
                //data debit
                detailDebit.set(payment.getRoundingValue());
                //data creadit
                detailCreadit.set(payment.getUnitNominal());
            }
            //data payment

            totalBalance = totalBalance.add(detailDebit.get().subtract(detailCreadit.get()));
            billDetail.setBalance(totalBalance);
            billDetail.setCredit(detailCreadit.get());
            billDetail.setDebit(detailDebit.get());
            billDetail.setDetail("Payment : " + paymentTypeInEnglish(payment.getRefFinanceTransactionPaymentType().getIdtype())//payment.getRefFinanceTransactionPaymentType().getTypeName()
                    + additionDescription);
            billDetail.setTanggal(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(detailDate.getValue()));
        }

        public ClassPrintBillDetail getBillDetail() {
            return billDetail;
        }

        public void setBillDetail(ClassPrintBillDetail billDetail) {
            this.billDetail = billDetail;
        }

        public BooleanProperty detailSelectedProperty() {
            return detailSelected;
        }

        public boolean getDetailSelected() {
            return detailSelectedProperty().get();
        }

        public void setDetailSelected(boolean selected) {
            detailSelectedProperty().set(selected);
        }

        public ObjectProperty<java.util.Date> detailDateProperty() {
            return detailDate;
        }

        public java.util.Date getDetailDate() {
            return detailDateProperty().get();
        }

        public void setDetailDate(java.util.Date date) {
            detailDateProperty().set(date);
        }

        public StringProperty detailDescriptionProperty() {
            return detailDescription;
        }

        public String getDetailDescription() {
            return detailDescriptionProperty().get();
        }

        public void setDetailDescription(String description) {
            detailDescriptionProperty().set(description);
        }

        public ObjectProperty<BigDecimal> detailDebitProperty() {
            return detailDebit;
        }

        public BigDecimal getDetailDebit() {
            return detailDebitProperty().get();
        }

        public void setDetailDebit(BigDecimal debit) {
            detailDebitProperty().set(debit);
        }

        public ObjectProperty<BigDecimal> detailCreaditProperty() {
            return detailCreadit;
        }

        public BigDecimal getDetailCreadit() {
            return detailCreaditProperty().get();
        }

        public void setDetailCreadit(BigDecimal creadit) {
            detailCreaditProperty().set(creadit);
        }

        public ObjectProperty<BigDecimal> detailBalanceProperty() {
            return detailBalance;
        }

        public BigDecimal getDetailBalance() {
            return detailBalanceProperty().get();
        }

        public void setDetailBalance(BigDecimal balance) {
            detailBalanceProperty().set(balance);
        }

        public ObjectProperty<TblReservationRoomTypeDetail> detailRRDProperty() {
            return detailRRD;
        }

        public TblReservationRoomTypeDetail getDetailRRD() {
            return detailRRDProperty().get();
        }

        public void setDetailRRD(TblReservationRoomTypeDetail rrtd) {
            detailRRDProperty().set(rrtd);
        }

        public ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> detailRRDRPDProperty() {
            return detailRRDRPD;
        }

        public TblReservationRoomTypeDetailRoomPriceDetail getDetailRRDRPD() {
            return detailRRDRPDProperty().get();
        }

        public void setDetailRRDRPD(TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd) {
            detailRRDRPDProperty().set(rrtdrpd);
        }

        public ObjectProperty<TblReservationAdditionalItem> detailAddtionalItemProperty() {
            return detailAddtionalItem;
        }

        public TblReservationAdditionalItem getDetailAddtionalItem() {
            return detailAddtionalItemProperty().get();
        }

        public void setDetailAddtionalItem(TblReservationAdditionalItem additionalItem) {
            detailAddtionalItemProperty().set(additionalItem);
        }

        public ObjectProperty<TblReservationAdditionalService> detailAddtionalServiceProperty() {
            return detailAddtionalService;
        }

        public TblReservationAdditionalService getDetailAddtionalService() {
            return detailAddtionalServiceProperty().get();
        }

        public void setDetailAddtionalService(TblReservationAdditionalService additionalService) {
            detailAddtionalServiceProperty().set(additionalService);
        }

        public ObjectProperty<TblReservationBrokenItem> detailBrokenItemProperty() {
            return detailBrokenItem;
        }

        public TblReservationBrokenItem getDetailBrokenItem() {
            return detailBrokenItemProperty().get();
        }

        public void setDetailBrokenItem(TblReservationBrokenItem brokenItem) {
            detailBrokenItemProperty().set(brokenItem);
        }

        public ObjectProperty<TblReservationRescheduleCanceled> detailRescheduleCanceledProperty() {
            return detailRescheduleCanceled;
        }

        public TblReservationRescheduleCanceled getDetailRescheduleCanceled() {
            return detailRescheduleCanceledProperty().get();
        }

        public void setDetailRescheduleCanceled(TblReservationRescheduleCanceled rescheduleCanceled) {
            detailRescheduleCanceledProperty().set(rescheduleCanceled);
        }

        public ObjectProperty<TblReservationCancelingFee> detailCancelingFeeProperty() {
            return detailCancelingFee;
        }

        public TblReservationCancelingFee getDetailCancelingFee() {
            return detailCancelingFeeProperty().get();
        }

        public void setDetailCancelingFee(TblReservationCancelingFee cancelingFee) {
            detailCancelingFeeProperty().set(cancelingFee);
        }

        public ObjectProperty<TblReservationPayment> detailPaymentProperty() {
            return detailPayment;
        }

        public TblReservationPayment getDetailPayment() {
            return detailPaymentProperty().get();
        }

        public void setDetailPayment(TblReservationPayment payment) {
            detailPaymentProperty().set(payment);
        }

    }

    public ReservationBillPaymentDetail generateReservationBillPaymentDetail(
            TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd,
            TblReservationAdditionalItem additionalItem,
            TblReservationAdditionalService additionalService,
            TblReservationBrokenItem brokenItem,
            TblReservationRescheduleCanceled rescheduleCanceled,
            TblReservationCancelingFee cancelingFee,
            TblReservationPayment payment) {
        //room
        if (rrtdrpd != null) {
            return new ReservationBillPaymentDetail(rrtdrpd);
        }
        //additional item
        if (additionalItem != null) {
            return new ReservationBillPaymentDetail(additionalItem);
        }
        //additional service
        if (additionalService != null) {
            return new ReservationBillPaymentDetail(additionalService);
        }
        //broken item
        if (brokenItem != null) {
            return new ReservationBillPaymentDetail(brokenItem);
        }
        //reschedule canceled
        if (rescheduleCanceled != null) {
            return new ReservationBillPaymentDetail(rescheduleCanceled);
        }
        //canceling fee
        if (cancelingFee != null) {
            return new ReservationBillPaymentDetail(cancelingFee);
        }
        //payment
        if (payment != null) {
            return new ReservationBillPaymentDetail(payment);
        }
        return null;
    }

    private String paymentTypeInEnglish(int id) {
        String hsl = "";
        switch (id) {
            case 0:
                hsl = "Cash";
                break;
            case 1:
                hsl = "Transfer";
                break;
            case 2:
                hsl = "Debit Card";
                break;
            case 3:
                hsl = "Credit Card";
                break;
            case 4:
                hsl = "Cheque";
                break;
        }
        return hsl;
    }

    /**
     * HANDLE FOR DATA INPUT RESERVATION-CHECKOUT TRANSACTION
     */
    public TblReservationPayment selectedDataTransaction;

    public TblReservationPaymentWithTransfer selectedDataTransactionWithTransfer;

    public TblReservationPaymentWithBankCard selectedDataTransactionWithBankCard;

    public TblReservationPaymentWithCekGiro selectedDataTransactionWithCekGiro;

    public TblReservationPaymentWithGuaranteePayment selectedDataTransactionWithGuaranteePayment;

    public TblReservationPaymentWithReservationVoucher selectedDataTransactionWithReservationVoucher;

    public boolean isFirstPayment;

    private void dataRCOTransactionCreateHandle() {
        if (calculationTotalRestOfBill().compareTo(new BigDecimal("0")) == 1) {   //need something to pay 
            if (selectedData.getRefReservationStatus().getIdstatus() == 1) { //Reserved = '1'
                isFirstPayment = true;  //first payment
                resetDataTransaction();
                //open form data reservation-checkout transaction dialog
                showReservationTransactionDialog();
            } else {
                isFirstPayment = false;
                resetDataTransaction();
                //open form data reservation-checkout transaction dialog
                showReservationTransactionDialog();
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA TRANSAKSI (RESERVASI)",
                    "Tidak ada tagihan yang harus dibayar..!", null);
        }
    }

    private void dataReservationBillPrintHandle() {
        List<ReservationBillPaymentDetail> list = tableDataRCOTransaction.getTableView().getItems();
        List<ClassPrintBillDetail> listBillDetail = new ArrayList();
        List<ClassPrintBill> listBill = new ArrayList();

        String emailHotel = "";
        SysDataHardCode sdhHotelEmail = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 16);
        emailHotel = sdhHotelEmail.getDataHardCodeValue();
        String summaryHotel = "";
        SysDataHardCode sdhHotelSummary = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 17);
        summaryHotel = sdhHotelSummary.getDataHardCodeValue();
        String noteHotel = "";
        SysDataHardCode sdhHotelNote = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 18);
        noteHotel = sdhHotelNote.getDataHardCodeValue();

        for (ReservationBillPaymentDetail getBillDetail : list) {
            listBillDetail.add(getBillDetail.getBillDetail());
        }
        ClassPrintBill printBill = new ClassPrintBill();
        printBill.setArrivalDate(selectedData.getArrivalTime() != null ? new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("id")).format(selectedData.getArrivalTime()) : "-");
        printBill.setCashier(ClassSession.currentUser.getTblEmployeeByIdemployee().getCodeEmployee() + "-" + ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
        printBill.setCodeInvoice(dataReservationBill.getCodeBill());
        printBill.setInvoiceDate(new SimpleDateFormat("dd MMMM yyyy", new Locale("id")).format(new java.sql.Date(dataReservationBill.getCreateDate().getTime())));
        printBill.setDepartureDate(selectedData.getDepartureTime() != null ? new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("id")).format(selectedData.getDepartureTime()) : "-");
        printBill.setEmailHotel(emailHotel);
        printBill.setNote(noteHotel);
        printBill.setSummary(summaryHotel);
        printBill.setCustomerName(selectedData.getTblCustomer().getTblPeople().getFullName());
        printBill.setListBonReservasiDetail(listBillDetail);

        listBill.add(printBill);

        ClassPrinter.printBillReservasi(listBill, dataReservationBill);
        /*  if (dataBill.getTblReservation().getIdreservation() != 0L) {
         String hotelName = "";
         SysDataHardCode sdhHotelName = fReservationManager.getDataSysDataHardCode((long) 12);  //HotelName = '12'
         if (sdhHotelName != null
         && sdhHotelName.getDataHardCodeValue() != null) {
         hotelName = sdhHotelName.getDataHardCodeValue();
         }
         String hotelAddress = "";
         SysDataHardCode sdhHotelAddress = fReservationManager.getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
         if (sdhHotelAddress != null
         && sdhHotelAddress.getDataHardCodeValue() != null) {
         hotelAddress = sdhHotelAddress.getDataHardCodeValue();
         }
         String hotelPhoneNumber = "";
         SysDataHardCode sdhHotelPhoneNumber = fReservationManager.getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
         if (sdhHotelPhoneNumber != null
         && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
         hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
         }
         String hotelLogoName = "";
         SysDataHardCode sdhHotelLogoName = fReservationManager.getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
         if (sdhHotelLogoName != null
         && sdhHotelLogoName.getDataHardCodeValue() != null) {
         hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
         }
         try {
         ClassPrinter.printBon(hotelName,
         hotelAddress,
         hotelPhoneNumber,
         "",
         hotelLogoName,
         dataBill,
         language);
         } catch (Throwable ex) {
         Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
         }
         } else {
         HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
         } */
    }

    public void dataReservationTransactionPrintHandle(int language) {
        if (dataReservationBill.getTblReservation().getIdreservation() != 0L) {
            String hotelName = "";
            SysDataHardCode sdhHotelName = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 12);  //HotelName = '12'
            if (sdhHotelName != null
                    && sdhHotelName.getDataHardCodeValue() != null) {
                hotelName = sdhHotelName.getDataHardCodeValue();
            }
            String hotelAddress = "";
            SysDataHardCode sdhHotelAddress = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
            if (sdhHotelAddress != null
                    && sdhHotelAddress.getDataHardCodeValue() != null) {
                hotelAddress = sdhHotelAddress.getDataHardCodeValue();
            }
            String hotelPhoneNumber = "";
            SysDataHardCode sdhHotelPhoneNumber = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
            if (sdhHotelPhoneNumber != null
                    && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
                hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
            }
            String hotelLogoName = "";
            SysDataHardCode sdhHotelLogoName = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
            if (sdhHotelLogoName != null
                    && sdhHotelLogoName.getDataHardCodeValue() != null) {
                hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
            }
            try {
                ClassPrinter.printReceipt(hotelName,
                        hotelAddress,
                        hotelPhoneNumber,
                        hotelLogoName,
                        dataReservationBill,
                        language);
            } catch (Throwable ex) {
                Logger.getLogger(ReservationBillAndPaymentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    private String printDataType = "";

    //English = '0'
    //Indonesian = '1'
    public void printHandle(int language) {
        switch (printDataType) {
            case "ReservationBill":
                dataReservationBillPrintHandle(); //Reservation = '0'
                break;
            case "ReservationReceipt":
                dataReservationTransactionPrintHandle(language);
                break;
//            case "CancelingReceipt":
//                dataReservationCancelingTransactionPrintHandle(language);
//                break;
            default:
                break;
        }
    }

    //RCO###
    public void resetDataTransaction() {
        selectedDataTransaction = new TblReservationPayment();
        selectedDataTransaction.setTblReservationBill(dataReservationBill);
        selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
        selectedDataTransaction.setRoundingValue(new BigDecimal("0"));
        selectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer();
        selectedDataTransactionWithTransfer.setTblReservationPayment(selectedDataTransaction);
        selectedDataTransactionWithTransfer.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
        selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard();
        selectedDataTransactionWithBankCard.setTblReservationPayment(selectedDataTransaction);
        selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
        selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
        selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro();
        selectedDataTransactionWithCekGiro.setTblReservationPayment(selectedDataTransaction);
        selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
        selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
        selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(selectedDataTransaction);
        selectedDataTransactionWithGuaranteePayment.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
        selectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher();
        selectedDataTransactionWithReservationVoucher.setTblReservationPayment(selectedDataTransaction);
    }

    public TblBankAccount getDataDefaultBankAccountForGuestTransaction() {
        String defaultBankAccountForGuestTransaction = "";
        SysDataHardCode sdhDefaultBankAccountForGuestTransaction = roomStatusFDController.getServiceRV2().getDataSysDataHardCode((long) 25);  //DefaultBankAccountForGuestTransaction = '25'
        if (sdhDefaultBankAccountForGuestTransaction != null
                && sdhDefaultBankAccountForGuestTransaction.getDataHardCodeValue() != null) {
            defaultBankAccountForGuestTransaction = sdhDefaultBankAccountForGuestTransaction.getDataHardCodeValue();
        }
        if (!defaultBankAccountForGuestTransaction.equals("")) {
            return roomStatusFDController.getServiceRV2().getBankAccount(Long.parseLong(defaultBankAccountForGuestTransaction));
        }
        return null;
    }

    public Stage dialogStage;

    private void showReservationTransactionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/RCTransactionInputDialog.fxml"));

            RCTransactionInputController controller = new RCTransactionInputController(this);
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

    private void dataReservationBillAndPaymentCancelHandle() {
        if (calculationTotalRestOfBill().compareTo(new BigDecimal("0")) == 1) {
            String confirmation;
            if (isAllCheckOut()) {
                confirmation = "Masih terdapat tagihan (No. Reservasi : "
                        + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getCodeReservation() + ") yang belum terbayar dan seluruh kamar telah 'CheckOut', "
                        + "\nApakah anda ingin tetap keluar?";
            } else {
                confirmation = "Masih terdapat tagihan (No. Reservasi : "
                        + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getCodeReservation() + ") yang belum terbayar, "
                        + "\nApakah anda ingin tetap keluar?";
            }
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", confirmation, "", roomStatusFDController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //refresh all data and close form data - reservation additional service
                roomStatusFDController.setSelectedDataToInputForm();
                roomStatusFDController.dialogStage.close();
            }
        } else {
            //refresh all data and close form data - reservation additional service
            roomStatusFDController.setSelectedDataToInputForm();
            roomStatusFDController.dialogStage.close();
        }
    }

    private boolean isAllCheckOut() {
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() == null) {
                return false;
            } else {
                if (data.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 3) {  //Checked Out = '3'
                    return false;
                }
            }
        }
        return true;
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
        initFormDataReservationBillAndPayment();

        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationBillAndPaymentController(RoomStatusFDController parentController) {
        roomStatusFDController = parentController;
    }

    private final RoomStatusFDController roomStatusFDController;

    public RoomStatusFDController getParentController() {
        return roomStatusFDController;
    }

}
