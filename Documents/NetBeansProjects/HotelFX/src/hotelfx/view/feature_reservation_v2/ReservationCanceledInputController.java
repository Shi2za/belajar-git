/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCanceled;
import hotelfx.persistence.model.TblReservationCancelingFee;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRescheduleCanceled;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.view.feature_reservation_v2.rctid_canceled.RCTICashController;
import hotelfx.view.feature_reservation_v2.rctid_canceled.RCTICekGiroController;
import hotelfx.view.feature_reservation_v2.rctid_canceled.RCTICreditCardController;
import hotelfx.view.feature_reservation_v2.rctid_canceled.RCTIDebitCardController;
import hotelfx.view.feature_reservation_v2.rctid_canceled.RCTIGuaranteePaymentController;
import hotelfx.view.feature_reservation_v2.rctid_canceled.RCTIReturnPaymentController;
import hotelfx.view.feature_reservation_v2.rctid_canceled.RCTITransferController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
 * @author ANDRI
 */
public class ReservationCanceledInputController implements Initializable {

    @FXML
    private AnchorPane ancFormReservationCanceled;

    @FXML
    private GridPane gpFormDataReservationCanceled;

    @FXML
    private JFXTextField txtCodeReservation;

    @FXML
    private JFXCheckBox chbBlackList;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtCancellationFee;

    @FXML
    private JFXTextField txtTotalPayment;

    @FXML
    private AnchorPane ancPaymentType;
    public JFXCComboBoxTablePopup<RefFinanceTransactionPaymentType> cbpFinanceTransactionPaymentType;

    @FXML
    private AnchorPane ancDataInputDetailTransactionLayout;

    @FXML
    private JFXTextArea txtCancelingNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationCanceled() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Pembatalan Reservasi)"));
        btnSave.setOnAction((e) -> {
            dataReservationCanceledSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationCanceledCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCancellationFee,
                txtCancelingNote);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtCancellationFee);
    }

    private void initDataPopup() {
        //Finance Transaction Payment Type
        TableView<RefFinanceTransactionPaymentType> tableFinanceTransactionPaymentType = new TableView<>();

        TableColumn<RefFinanceTransactionPaymentType, String> typeName = new TableColumn<>("Tipe Pembayaran");
        typeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        typeName.setMinWidth(250);

        tableFinanceTransactionPaymentType.getColumns().addAll(typeName);

        ObservableList<RefFinanceTransactionPaymentType> ftpTypeItems = FXCollections.observableArrayList(loadAllDataFinanceTransactionPaymentType());

        cbpFinanceTransactionPaymentType = new JFXCComboBoxTablePopup<>(
                RefFinanceTransactionPaymentType.class, tableFinanceTransactionPaymentType, ftpTypeItems, "", "Tipe Pembayaran *", true, 270, 300
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setLeftAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setRightAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setTopAnchor(cbpFinanceTransactionPaymentType, 0.0);
        ancPaymentType.getChildren().clear();
        ancPaymentType.getChildren().add(cbpFinanceTransactionPaymentType);
    }

    private void refreshDataPopup() {
        //Finance Transaction Payment Type
        ObservableList<RefFinanceTransactionPaymentType> ftpTypeItems = FXCollections.observableArrayList(loadAllDataFinanceTransactionPaymentType());
        cbpFinanceTransactionPaymentType.setItems(ftpTypeItems);
    }

    private List<RefFinanceTransactionPaymentType> loadAllDataFinanceTransactionPaymentType() {
        List<RefFinanceTransactionPaymentType> list = reservationController.getFReservationManager().getAllDataFinanceTransactionPaymentType();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdtype() == 13 //Open Deposit (Cash) = '13'
                    || list.get(i).getIdtype() == 14 //Close Deposit (Cash) = '14'
                    || list.get(i).getIdtype() == 9 //Draw Deposit = '9'
                    || list.get(i).getIdtype() == 16) { //Canceling Fee = '16'
                list.remove(i);
            } else {
//                if ((reservationController.calculationTotalRestOfBill()
//                        .compareTo(new BigDecimal("0")) == 0)) {
////                        && list.get(i).getIdtype() != 0) { //Tunai = '0'
//                    list.remove(i);
//                } else {
                if (reservationController.dataReservationBill.getTblBankCard() != null) {
                    if (reservationController.dataReservationBill.getTblBankCard().getRefBankCardType().getIdtype() == 0) {   //Debit = '0'
                        if (list.get(i).getIdtype() != 2) {   //Kartu Debit = '2'
                            list.remove(i);
                        }
                    } else {  //Kredit = '1'
                        if (list.get(i).getIdtype() != 3) {   //Kartu Kredit = '3'
                            list.remove(i);
                        }
                    }
                } else {
                    //voucher remove
                    if (list.get(i).getIdtype() == 10) { //Voucher = '10'
                        list.remove(i);
                    } else {
                        //reservation with guarantee payment must be first payment
                        if ((list.get(i).getIdtype() == 7 //Guarantee Letter (Corporate)  = 7
                                || list.get(i).getIdtype() == 8) //Guarantee Letter (Government)  = 8
                                //                                    && (!isFirstPayment())) {    //first payment in bill
                                ) {
                            list.remove(i);
                        } else {
                            //reservation must be order by 'travel agent'
                            if (list.get(i).getIdtype() == 6) { //Travel Agent  = 6
                                if ((reservationController.selectedData.getTblPartner() == null) //order by travel agent
                                        //                                            || (!isFirstPayment())) {    //first payment in bill
                                        ) {
                                    list.remove(i);
                                }
                            }
                        }
                    }
                }
//                }
            }
        }
        return list;
    }

//    private boolean isFirstPayment() {
//        boolean firstPayment;
////        if (rcTransactionInputController.dataInputRCTransactionInputDetailStatus == 0) {   //insert
//            firstPayment = reservationController.dataReservationPayments.isEmpty();
////        } else {  //update
////            firstPayment = rcTransactionInputController.getParentController().dataReservationPayments.size() == 1;
////        }
//        return firstPayment;
//    }
    public List<TblReservationRescheduleCanceled> newReservationReschedules = new ArrayList<>();

    public List<TblGuaranteeLetterItemDetail> newDataGuaranteeLetterItemDetails = new ArrayList<>();

    private void generateCanceledData() {
        for (TblReservationRoomTypeDetail rrtd : reservationController.dataReservationRoomTypeDetails) {
            //data rrtd - room price detail & reschedule canceled
            for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
                if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                        == rrtd.getIddetail()) {
                    //data reschedule canceled (+)
                    TblReservationRescheduleCanceled newRRCP = new TblReservationRescheduleCanceled();
                    newRRCP.setTblReservation(reservationController.selectedData);
                    newRRCP.setTblReservationRoomTypeDetail(rrtdrpd.getTblReservationRoomTypeDetail());
                    newRRCP.setReservedDate(rrtdrpd.getTblReservationRoomPriceDetail().getCreateDate());
                    newRRCP.setReservedForDate(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate());
                    newRRCP.setQuantity(new BigDecimal("1"));
                    newRRCP.setPrice(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice());
                    newRRCP.setDiscountPercentage(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage());
                    newRRCP.setComplimentary(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary());
                    newRRCP.setTblHotelEvent(rrtdrpd.getTblReservationRoomPriceDetail().getTblHotelEvent());
                    newRRCP.setTblBankEventCard(rrtdrpd.getTblReservationRoomPriceDetail().getTblBankEventCard());
                    newReservationReschedules.add(newRRCP);
                    //data reschedule canceled (-)
                    TblReservationRescheduleCanceled newRRCN = new TblReservationRescheduleCanceled();
                    newRRCN.setTblReservation(reservationController.selectedData);
                    newRRCN.setTblReservationRoomTypeDetail(rrtdrpd.getTblReservationRoomTypeDetail());
                    newRRCN.setReservedDate(Timestamp.valueOf(LocalDateTime.now()));
                    newRRCN.setReservedForDate(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate());
                    newRRCN.setQuantity(new BigDecimal("1"));
                    newRRCN.setPrice((new BigDecimal("-1")).multiply(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()));
                    newRRCN.setDiscountPercentage((new BigDecimal("-1")).multiply(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()));
                    newRRCN.setComplimentary((new BigDecimal("-1")).multiply(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()));
                    newRRCN.setTblHotelEvent(rrtdrpd.getTblReservationRoomPriceDetail().getTblHotelEvent());
                    newRRCN.setTblBankEventCard(rrtdrpd.getTblReservationRoomPriceDetail().getTblBankEventCard());
                    newReservationReschedules.add(newRRCN);
                }
            }
            //data additional item
            for (TblReservationAdditionalItem rai : reservationController.dataReservationAdditionalItems) {
                if (rai.getTblReservationRoomTypeDetail().getIddetail()
                        == rrtd.getIddetail()) {
                    if (rai.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || rai.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                        //data reschedule canceled (+)
                        TblReservationRescheduleCanceled newRRCP = new TblReservationRescheduleCanceled();
                        newRRCP.setTblReservation(reservationController.selectedData);
                        newRRCP.setTblReservationRoomTypeDetail(rai.getTblReservationRoomTypeDetail());
                        newRRCP.setTblItem(new TblItem(rai.getTblItem()));
                        newRRCP.setReservedDate(rai.getCreateDate());
                        newRRCP.setReservedForDate(rai.getAdditionalDate());
                        newRRCP.setQuantity(rai.getItemQuantity());
                        newRRCP.setPrice(rai.getItemCharge());
                        newRRCP.setDiscountPercentage(rai.getDiscountPercentage());
                        newRRCP.setComplimentary(new BigDecimal("0"));
                        newRRCP.setTblHotelEvent(rai.getTblHotelEvent());
                        newRRCP.setTblBankEventCard(rai.getTblBankEventCard());
                        newReservationReschedules.add(newRRCP);
                        //data reschedule canceled (-)
                        TblReservationRescheduleCanceled newRRCN = new TblReservationRescheduleCanceled();
                        newRRCN.setTblReservation(reservationController.selectedData);
                        newRRCN.setTblReservationRoomTypeDetail(rai.getTblReservationRoomTypeDetail());
                        newRRCN.setTblItem(new TblItem(rai.getTblItem()));
                        newRRCN.setReservedDate(Timestamp.valueOf(LocalDateTime.now()));
                        newRRCN.setReservedForDate(rai.getAdditionalDate());
                        newRRCN.setQuantity(rai.getItemQuantity());
                        newRRCN.setPrice((new BigDecimal("-1")).multiply(rai.getItemCharge()));
                        newRRCN.setDiscountPercentage((new BigDecimal("-1")).multiply(rai.getDiscountPercentage()));
                        newRRCN.setComplimentary(new BigDecimal("0"));
                        newRRCN.setTblHotelEvent(rai.getTblHotelEvent());
                        newRRCN.setTblBankEventCard(rai.getTblBankEventCard());
                        newReservationReschedules.add(newRRCN);
                    }
                }
            }
            //data additional service
            for (TblReservationAdditionalService ras : reservationController.dataReservationAdditionalServices) {
                if (ras.getTblReservationRoomTypeDetail().getIddetail()
                        == rrtd.getIddetail()) {
                    if (ras.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || ras.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                        //data reschedule canceled (+)
                        TblReservationRescheduleCanceled newRRCP = new TblReservationRescheduleCanceled();
                        newRRCP.setTblReservation(reservationController.selectedData);
                        newRRCP.setTblReservationRoomTypeDetail(ras.getTblReservationRoomTypeDetail());
                        newRRCP.setTblRoomService(new TblRoomService(ras.getTblRoomService()));
                        newRRCP.setReservedDate(ras.getCreateDate());
                        newRRCP.setReservedForDate(ras.getAdditionalDate());
                        newRRCP.setQuantity(new BigDecimal("1"));
                        newRRCP.setPrice(ras.getPrice());
                        newRRCP.setDiscountPercentage(ras.getDiscountPercentage());
                        newRRCP.setComplimentary(new BigDecimal("0"));
                        newRRCP.setTblHotelEvent(ras.getTblHotelEvent());
                        newRRCP.setTblBankEventCard(ras.getTblBankEventCard());
                        newReservationReschedules.add(newRRCP);
                        //data reschedule canceled (-)
                        TblReservationRescheduleCanceled newRRCN = new TblReservationRescheduleCanceled();
                        newRRCN.setTblReservation(reservationController.selectedData);
                        newRRCN.setTblReservationRoomTypeDetail(ras.getTblReservationRoomTypeDetail());
                        newRRCN.setTblRoomService(new TblRoomService(ras.getTblRoomService()));
                        newRRCN.setReservedDate(Timestamp.valueOf(LocalDateTime.now()));
                        newRRCN.setReservedForDate(ras.getAdditionalDate());
                        newRRCN.setQuantity(new BigDecimal("1"));
                        newRRCN.setPrice((new BigDecimal("-1")).multiply(ras.getPrice()));
                        newRRCN.setDiscountPercentage((new BigDecimal("-1")).multiply(ras.getDiscountPercentage()));
                        newRRCN.setComplimentary(new BigDecimal("0"));
                        newRRCN.setTblHotelEvent(ras.getTblHotelEvent());
                        newRRCN.setTblBankEventCard(ras.getTblBankEventCard());
                        newReservationReschedules.add(newRRCN);
                    }
                }
            }
        }
        //set canceling fee -> create date
        reservationController.selectedDataCancelingFee.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    public ObjectProperty<BigDecimal> totalBill = new SimpleObjectProperty<>(new BigDecimal("0"));
    
    public ObjectProperty<BigDecimal> totalPayment = new SimpleObjectProperty<>(new BigDecimal("0"));
    
    public IntegerProperty exprMonth = new SimpleIntegerProperty(0);
    
    public IntegerProperty exprYear = new SimpleIntegerProperty(0);

    private void setSelectedDataToInputForm() {
        //refresh data popup
        refreshDataPopup();

        //generate canceled data
        generateCanceledData();

        txtCodeReservation.textProperty().bindBidirectional(reservationController.selectedDataReservationCanceled.getTblReservation().codeReservationProperty());
        txtCustomerName.textProperty().bindBidirectional(reservationController.selectedDataReservationCanceled.getTblReservation().getTblCustomer().getTblPeople().fullNameProperty());

        txtTotalPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationAllTotalReservationTransaction()));

        chbBlackList.setSelected(false);

        Bindings.bindBidirectional(txtCancellationFee.textProperty(), reservationController.selectedDataCancelingFee.cancelingFeeNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        reservationController.selectedDataCancelingFee.cancelingFeeNominalProperty().addListener((obs, oldVal, newVal) -> {
            //set total bill
            totalBill.set(calculationTotalBill(newVal));
        });

        totalBill.addListener((obs, oldVal, newVal) -> {
            if (newVal.compareTo(new BigDecimal("0")) != 1) {
                cbpFinanceTransactionPaymentType.setValue(reservationController.getFReservationManager().getFinanceTransactionPaymentType(16));     //Return = '16'
                cbpFinanceTransactionPaymentType.setDisable(true);
            } else {
                if (cbpFinanceTransactionPaymentType.getValue() != null
                        && cbpFinanceTransactionPaymentType.getValue().getIdtype() == 16) {  //Return Payment
                    cbpFinanceTransactionPaymentType.setValue(null);
                }
                cbpFinanceTransactionPaymentType.setDisable(false);
            }
        });

        txtCancelingNote.textProperty().bindBidirectional(reservationController.selectedDataReservationCanceled.cancelNoteProperty());

        //unvisible (data input transaction)
        for (Node node : ancDataInputDetailTransactionLayout.getChildren()) {
            node.setVisible(false);
        }

        cbpFinanceTransactionPaymentType.valueProperty().addListener((obs, oldVal, newVal) -> {
            setTransactionTypeInputForm(newVal);
        });

        cbpFinanceTransactionPaymentType.valueProperty().bindBidirectional(reservationController.selectedDataTransaction.refFinanceTransactionPaymentTypeProperty());

        cbpFinanceTransactionPaymentType.hide();

        //refresh bill
        totalBill.set(calculationTotalBill(reservationController.selectedDataCancelingFee.getCancelingFeeNominal()));
    }

    public BigDecimal calculationTotalBill(BigDecimal cancelingFeeNominal) {
        BigDecimal result = cancelingFeeNominal != null
                ? cancelingFeeNominal : new BigDecimal("0");
        BigDecimal serviceCharge = result.multiply((reservationController.dataReservationBill.getServiceChargePercentage()));
        BigDecimal tax = (result.add(serviceCharge)).multiply((reservationController.dataReservationBill.getTaxPercentage()));
        result = result.add(serviceCharge).add(tax);
        for (TblReservationPayment data : reservationController.dataReservationPayments) {
            if(data.getRefFinanceTransactionPaymentType().getIdtype() == 16){   //Return = '16'
                result = result.add(data.getUnitNominal());
            }else{
                result = result.subtract(data.getUnitNominal());
            }
        }
        for(TblReservationCancelingFee data : reservationController.dataReservationCancelingFees){
            BigDecimal serviceChargeCF = data.getCancelingFeeNominal().multiply((reservationController.dataReservationBill.getServiceChargePercentage()));
            BigDecimal taxCF = (data.getCancelingFeeNominal().add(serviceChargeCF)).multiply((reservationController.dataReservationBill.getTaxPercentage()));
            result = result.add((data.getCancelingFeeNominal().add(serviceChargeCF).add(taxCF)));
        }
        return result;
    }

    private  RCTICashController rctiCashController;
    
    private RCTITransferController rctiTransferController;
    
    private RCTIDebitCardController rctiDebitCardController;
    
    private RCTICreditCardController rctiCreditCardController;
    
    private RCTICekGiroController rctiCekController;
    
    private RCTICekGiroController rctiGiroController;
    
    private RCTIGuaranteePaymentController rctiTravelAgentController;
    
    private RCTIGuaranteePaymentController rctiCorporateController;
    
    private RCTIGuaranteePaymentController rctiGovernmentController;
    
    private RCTIReturnPaymentController returnPaymentController;
    
    private void setTransactionTypeInputForm(RefFinanceTransactionPaymentType transactionType) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            if (transactionType != null) {
                //reset controller
                rctiCashController = null;
                rctiTransferController = null;
                rctiDebitCardController = null;
                rctiCreditCardController = null;
                rctiCekController = null;
                rctiGiroController = null;
                rctiTravelAgentController = null;
                rctiCorporateController = null;
                rctiGovernmentController = null;
                returnPaymentController = null;
                switch (transactionType.getIdtype()) {
                    case 0:    //Tunai = '0'
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTICash.fxml"));

                        rctiCashController = new RCTICashController(this);
                        loader.setController(rctiCashController);
                        break;
                    case 1:    //Transfer = '1'
                        reservationController.selectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer();
                        reservationController.selectedDataTransactionWithTransfer.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTITransfer.fxml"));

                        rctiTransferController = new RCTITransferController(this);
                        loader.setController(rctiTransferController);
                        break;
                    case 2:   //Debit Card = '2'
                        reservationController.selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard();
                        reservationController.selectedDataTransactionWithBankCard.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIDebitCard.fxml"));

                        rctiDebitCardController = new RCTIDebitCardController(this);
                        loader.setController(rctiDebitCardController);
                        break;
                    case 3:  //Credit Card = '3'
                        reservationController.selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard();
                        reservationController.selectedDataTransactionWithBankCard.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTICreditCard.fxml"));

                        rctiCreditCardController = new RCTICreditCardController(this);
                        loader.setController(rctiCreditCardController);
                        break;
                    case 4: //Cek = '4'
                        reservationController.selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro();
                        reservationController.selectedDataTransactionWithCekGiro.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTICekGiro.fxml"));

                        rctiCekController = new RCTICekGiroController(this);
                        loader.setController(rctiCekController);
                        break;
                    case 5:    //Giro = '5'
                        reservationController.selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro();
                        reservationController.selectedDataTransactionWithCekGiro.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTICekGiro.fxml"));

                        rctiGiroController = new RCTICekGiroController(this);
                        loader.setController(rctiGiroController);
                        break;
                    case 6:    //Travel Agent = '6'
                        reservationController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
                        reservationController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIGuaranteePayment.fxml"));

                        rctiTravelAgentController = new RCTIGuaranteePaymentController(this,
                                "Travel Agent");
                        loader.setController(rctiTravelAgentController);
                        break;
                    case 7:    //Guarantee Letter (Corporate) = '7'
                        reservationController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
                        reservationController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIGuaranteePayment.fxml"));

                        rctiCorporateController = new RCTIGuaranteePaymentController(this,
                                "Guarantee Letter (Corporate)");
                        loader.setController(rctiCorporateController);
                        break;
                    case 8:   //Guarantee Letter (Government) = '8'
                        reservationController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
                        reservationController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationController.selectedDataTransaction);
                        
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIGuaranteePayment.fxml"));

                        rctiGovernmentController = new RCTIGuaranteePaymentController(this,
                                "Guarantee Letter (Government)");
                        loader.setController(rctiGovernmentController);
                        break;
//                case 9:    //Draw Deposit = '9'
//                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIDeposit.fxml"));
//
//                    RCTIDepositController rctiDrawDepositController = new RCTIDepositController(this);
//                    loader.setController(rctiDrawDepositController);
//                    break;
//                case 10:   //Voucher = '10'
//                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIReservationVoucher.fxml"));
//
//                    RCTIReservationVoucherController rctiReservationVoucherController = new RCTIReservationVoucherController(this);
//                    loader.setController(rctiReservationVoucherController);
//                    break;
//                case 13:    //Open Deposit (Cash) = '13'
//                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIDeposit.fxml"));
//
//                    RCTIDepositController rctiOpenDepositController = new RCTIDepositController(this);
//                    loader.setController(rctiOpenDepositController);
//                    break;
//                case 14:    //Close Deposit (Cash) = '14'
//                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIDeposit.fxml"));
//
//                    RCTIDepositController rctiCloseDepositController = new RCTIDepositController(this);
//                    loader.setController(rctiCloseDepositController);
//                    break;
                    case 16: //Canceling Fee = '16'
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIReturnPayment.fxml"));

                        returnPaymentController = new RCTIReturnPaymentController(this);
                        loader.setController(returnPaymentController);
                        break;
                    default:
                        loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTINull.fxml"));
                        break;
                }
            } else {
                loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTINull.fxml"));
            }
            //load loader to node
            Node subContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);

            //set 'sub feature content' into the center of 'feature_bank' contetnt.
            ancDataInputDetailTransactionLayout.getChildren().clear();
            ancDataInputDetailTransactionLayout.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private void dataReservationCanceledSaveHandle() {
        if (checkDataInputDataReservationCanceled()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data transaction (canceling fee)
                reservationController.selectedDataTransaction.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationController.selectedDataTransaction.setTblEmployeeByIdcashier(ClassSession.currentUser.getTblEmployeeByIdemployee());
                reservationController.selectedDataTransaction.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));   //Tambahan = '0'
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(reservationController.selectedData);
                dummySelectedData.setTblCustomer(new TblCustomer(dummySelectedData.getTblCustomer()));
                dummySelectedData.getTblCustomer().setTblPeople(new TblPeople(dummySelectedData.getTblCustomer().getTblPeople()));
                dummySelectedData.getTblCustomer().setRefCustomerType(chbBlackList.isSelected()
                        ? reservationController.getFReservationManager().getCustomerType(2) //BlackList = '2'
                        : new RefCustomerType(dummySelectedData.getTblCustomer().getRefCustomerType()));
                dummySelectedData.setRefReservationStatus(new RefReservationStatus(dummySelectedData.getRefReservationStatus()));
                TblReservationCanceled dummySelectedDataCanceled = new TblReservationCanceled(reservationController.selectedDataReservationCanceled);
                dummySelectedDataCanceled.setTblReservation(dummySelectedData);
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
                TblReservationPayment dummySelectedDataTransaction = new TblReservationPayment(reservationController.selectedDataTransaction);
                dummySelectedDataTransaction.setTblReservationBill(new TblReservationBill(dummySelectedDataTransaction.getTblReservationBill()));
                dummySelectedDataTransaction.getTblReservationBill().setTblReservation(dummySelectedData);
                dummySelectedDataTransaction.getTblReservationBill().setRefReservationBillType(new RefReservationBillType(dummySelectedDataTransaction.getTblReservationBill().getRefReservationBillType()));
                dummySelectedDataTransaction.setTblEmployeeByIdcashier(new TblEmployee(dummySelectedDataTransaction.getTblEmployeeByIdcashier()));
                dummySelectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedDataTransaction.getRefFinanceTransactionPaymentType()));
                dummySelectedDataTransaction.setRefReservationBillType(new RefReservationBillType(dummySelectedDataTransaction.getRefReservationBillType()));
                TblReservationPaymentWithTransfer dummyTransactionWithTransfer;
                if (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 1) {   //Transfer = '1'
                    dummyTransactionWithTransfer = new TblReservationPaymentWithTransfer(reservationController.selectedDataTransactionWithTransfer);
                    dummyTransactionWithTransfer.setTblReservationPayment(dummySelectedDataTransaction);
                    dummyTransactionWithTransfer.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyTransactionWithTransfer.getTblBankAccountBySenderBankAccount()));
                    dummyTransactionWithTransfer.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyTransactionWithTransfer.getTblBankAccountByReceiverBankAccount()));
                } else {
                    dummyTransactionWithTransfer = null;
                }
                TblReservationPaymentWithBankCard dummyTransactionWithBankCard;
                if (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 2 //Debit = '2'
                        || reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 3) {   //Kredit = '3'
                    dummyTransactionWithBankCard = new TblReservationPaymentWithBankCard(reservationController.selectedDataTransactionWithBankCard);
                    dummyTransactionWithBankCard.setTblReservationPayment(dummySelectedDataTransaction);
                    dummyTransactionWithBankCard.setTblBank(new TblBank(dummyTransactionWithBankCard.getTblBank()));
                    dummyTransactionWithBankCard.setTblBankEdc(new TblBankEdc(dummyTransactionWithBankCard.getTblBankEdc()));
                    dummyTransactionWithBankCard.setTblBankNetworkCard(new TblBankNetworkCard(dummyTransactionWithBankCard.getTblBankNetworkCard()));
                    if (dummyTransactionWithBankCard.getTblBankEdcBankNetworkCard() != null) {
                        dummyTransactionWithBankCard.setTblBankEdcBankNetworkCard(new TblBankEdcBankNetworkCard(dummyTransactionWithBankCard.getTblBankEdcBankNetworkCard()));
                    }
                    if (dummyTransactionWithBankCard.getTblBankEventCard() != null) {
                        dummyTransactionWithBankCard.setTblBankEventCard(new TblBankEventCard(dummyTransactionWithBankCard.getTblBankEventCard()));
                    }
                    dummyTransactionWithBankCard.setTblBankAccount(new TblBankAccount(dummyTransactionWithBankCard.getTblBankAccount()));
                } else {
                    dummyTransactionWithBankCard = null;
                }
                TblReservationPaymentWithCekGiro dummyTransactionWithCekGiro;
                if (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 4 //Cek = '4'
                        || reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 5) {   //Giro = '5'
                    dummyTransactionWithCekGiro = new TblReservationPaymentWithCekGiro(reservationController.selectedDataTransactionWithCekGiro);
                    dummyTransactionWithCekGiro.setTblReservationPayment(dummySelectedDataTransaction);
                    dummyTransactionWithCekGiro.setTblBank(new TblBank(dummyTransactionWithCekGiro.getTblBank()));
//                                        dummyTransactionWithCekGiro.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyTransactionWithCekGiro.getTblBankAccountBySenderBankAccount()));
                    dummyTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                } else {
                    dummyTransactionWithCekGiro = null;
                }
                TblReservationPaymentWithGuaranteePayment dummyTransactionWithGuaranteePayment;
//                List<TblGuaranteeLetterItemDetail> dummyDataGuaranteeLetterItemDetails = new ArrayList<>();
                if (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 6 //Travel Agent = '6'
                        || reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 7 //Guarantee Letter (Corporate) = '7'
                        || reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 8) {   //Guarantee Letter (Government) = '8'
                    dummyTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(reservationController.selectedDataTransactionWithGuaranteePayment);
                    dummyTransactionWithGuaranteePayment.setTblReservationPayment(dummySelectedDataTransaction);
                    dummyTransactionWithGuaranteePayment.setTblPartner(new TblPartner(dummyTransactionWithGuaranteePayment.getTblPartner()));
                    if (dummyTransactionWithGuaranteePayment.getTblBankAccountBySenderBankAccount() != null) {
                        dummyTransactionWithGuaranteePayment.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyTransactionWithGuaranteePayment.getTblBankAccountBySenderBankAccount()));
                    }
                    if (dummyTransactionWithGuaranteePayment.getTblBankAccountByReceiverBankAccount() != null) {
                        dummyTransactionWithGuaranteePayment.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyTransactionWithGuaranteePayment.getTblBankAccountByReceiverBankAccount()));
                    }
                    //data GL - Detail
                    saveGuaranteeLetterItemDetail(dummyTransactionWithGuaranteePayment);
                } else {
                    dummyTransactionWithGuaranteePayment = null;
                }
                if (reservationController.getFReservationManager().insertDataReservationCanceled(
                        dummySelectedDataCanceled,
                        dummySelectedData,
                        dummySelectedDataCancelingFee,
                        dummyDataReservationRescheduleCanceleds,
                        dummySelectedDataTransaction,
                        dummyTransactionWithTransfer,
                        dummyTransactionWithBankCard,
                        dummyTransactionWithCekGiro,
                        dummyTransactionWithGuaranteePayment,
                        newDataGuaranteeLetterItemDetails) != null) {
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

    private void saveGuaranteeLetterItemDetail(TblReservationPaymentWithGuaranteePayment dataGL) {
//        //data room type detail
//        for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
//            //data guarantee letter - item detail
//            TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
//            dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
//            dataGLID.setCodeRtd(dataRRTDRPD.getTblReservationRoomTypeDetail().getCodeDetail());
//            dataGLID.setDetailName(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName());
//            dataGLID.setRoomName(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
//                    ? dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
//                            ? dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
//                            : null : null);
//            dataGLID.setDetailDate(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
////                dataGLID.setDetailCost((long) reservationController.calculationRoomTypeDetailCost(dataRRTD));
//            dataGLID.setDetailCost(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
//            dataGLID.setDetailQuantity(new BigDecimal("1"));
////                dataGLID.setTotalDiscountNominal((long) reservationController.calculationRoomTypeDetailDiscount(dataRRTD));
//            dataGLID.setTotalDiscountNominal(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice()
//                    .multiply(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
//            dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
//            dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
//            dataGLID.setDetailType("Room");
//            //add data to list
//            reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
//        }
//        //data additional item
//        for (TblReservationAdditionalItem dataAdditionalItem : reservationController.dataReservationAdditionalItems) {
//            if (dataAdditionalItem.getRefReservationBillType().getIdtype() == 0    //Reservation = '0'
//                    || dataAdditionalItem.getRefReservationBillType().getIdtype() == 1) {    //CheckOut = '1' (impossible case)
//                //data guarantee letter - item detail
//                TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
//                dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
//                dataGLID.setCodeRtd(dataAdditionalItem.getTblReservationRoomTypeDetail().getCodeDetail());
//                dataGLID.setDetailName(dataAdditionalItem.getTblItem().getItemName());
//                dataGLID.setRoomName(dataAdditionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
//                        ? dataAdditionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
//                                ? dataAdditionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
//                                : null : null);
//                dataGLID.setDetailDate(dataAdditionalItem.getAdditionalDate());
//                dataGLID.setDetailCost(dataAdditionalItem.getItemCharge());
//                dataGLID.setDetailQuantity(dataAdditionalItem.getItemQuantity());
//                dataGLID.setTotalDiscountNominal((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity()))
//                        .multiply(dataAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100"))));
//                dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
//                dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
//                dataGLID.setDetailType("Item");
//                //add data to list
//                reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
//            }
//        }
//        //data additional service
//        for (TblReservationAdditionalService dataAdditionalService : reservationController.dataReservationAdditionalServices) {
//            if (dataAdditionalService.getRefReservationBillType().getIdtype() == 0    //Reservation = '0'
//                    || dataAdditionalService.getRefReservationBillType().getIdtype() == 1) {    //CheckOut = '1' (impossible case)
//                //data guarantee letter - item detail
//                TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
//                dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
//                dataGLID.setCodeRtd(dataAdditionalService.getTblReservationRoomTypeDetail().getCodeDetail());
//                dataGLID.setDetailName(dataAdditionalService.getTblRoomService().getServiceName());
//                dataGLID.setRoomName(dataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
//                        ? dataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
//                                ? dataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
//                                : null : null);
//                dataGLID.setDetailDate(dataAdditionalService.getAdditionalDate());
//                dataGLID.setDetailCost(dataAdditionalService.getPrice());
//                dataGLID.setDetailQuantity(new BigDecimal("1"));
//                dataGLID.setTotalDiscountNominal((dataAdditionalService.getPrice().multiply(new BigDecimal("1")))
//                        .multiply(dataAdditionalService.getDiscountPercentage().divide(new BigDecimal("100"))));
//                if (dataAdditionalService.getTblRoomService().getIdroomService() == 6 //Room Dining = 6
//                        || dataAdditionalService.getTblRoomService().getIdroomService() == 7 //Dine in Resto = 7
//                        ) {   //Resto Bill (impossible case)
//                    dataGLID.setServiceChargePercentage(new BigDecimal("0"));
//                    dataGLID.setTaxPercentage(new BigDecimal("0"));
//                } else {
//                    dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
//                    dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
//                }
//                dataGLID.setDetailType("Service");
//                //add data to list
//                reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
//            }
//        }
//        //data broken item
//        for (TblReservationBrokenItem dataRBI : reservationController.dataReservationBrokenItems) {
//            //data guarantee letter - item detail
//            TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
//            dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
//            dataGLID.setCodeRtd(dataRBI.getTblReservationRoomTypeDetail().getCodeDetail());
//            dataGLID.setDetailName(dataRBI.getTblItem().getItemName());
//            dataGLID.setRoomName(dataRBI.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
//                    ? dataRBI.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
//                            ? dataRBI.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
//                            : null : null);
//            dataGLID.setDetailDate(dataRBI.getCreateDate());
//            dataGLID.setDetailCost(dataRBI.getItemCharge());
//            dataGLID.setDetailQuantity(dataRBI.getItemQuantity());
//            dataGLID.setTotalDiscountNominal(new BigDecimal("0"));
//            dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
//            dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
//            dataGLID.setDetailType("Broken-Item");
//            //add data to list
//            reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
//        }
        //data guarantee letter - item detail (canceling fee)
            TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
            dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
            dataGLID.setCodeRtd("");
            dataGLID.setDetailName("Cancellation Fee");
            dataGLID.setRoomName(null);
            dataGLID.setDetailDate(reservationController.selectedDataCancelingFee.getCreateDate());
            dataGLID.setDetailCost(reservationController.selectedDataCancelingFee.getCancelingFeeNominal());
            dataGLID.setDetailQuantity(new BigDecimal("1"));
            dataGLID.setTotalDiscountNominal(new BigDecimal("0"));
            dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
            dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
            dataGLID.setDetailType("Cancellation Fee");
            //add data to list
            newDataGuaranteeLetterItemDetails.add(dataGLID);
    }
    
    private void dataReservationCanceledCancelHandle() {
        //refresh list data reservation
        reservationController.refreshDataTableReservation();
        //close form data close deposit input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationCanceled() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCancellationFee.getText() == null
                || txtCancellationFee.getText().equals("")
                || txtCancellationFee.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Cancellation Fee : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedDataCancelingFee.getCancelingFeeNominal().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Nominal Cancellation Fee : Nilai tidak dapat kurang dari '0' \n";
            }
        }
//        if (reservationController.selectedDataTransaction.getUnitNominal().compareTo(new BigDecimal("0")) == -1) {
//            dataInput = false;
//            errDataInput += "Total Nominal Pembayaran : Nilai tidak dapat kurang dari '0' \n";
//        } else {
//            if ((reservationController.selectedDataCancelingFee.getCancelingFeeNominal()
//                    .subtract(reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType() != null
//                                    ? ((reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 16) //Return = '16'
//                                            ? (calculationAllTotalReservationTransaction().subtract(reservationController.selectedDataTransaction.getUnitNominal()))
//                                            : (calculationAllTotalReservationTransaction().add(reservationController.selectedDataTransaction.getUnitNominal())))
//                                    : calculationAllTotalReservationTransaction()))
//                    .compareTo(new BigDecimal("0")) == 1
//                    || chbBlackList.isSelected()) {
//                dataInput = false;
//                errDataInput += "Total Balance : Nilai harus sama dengan '0' \n";
//            } else {
//                if (!isNominalCashierBalanceAvailable()) {
//                    dataInput = false;
//                    errDataInput += "Total Uang yang Dikembalikan : Melebihi total nominal Kas Kasir..!! \n";
//                }
//            }
//        }
        if (txtCancelingNote.getText() == null || txtCancelingNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Keterangan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType() != null) {
            switch (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype()) {
                case 0:    //Tunai
                    if (txtTotalPayment.getText() == null
                            || txtTotalPayment.getText().equals("")
                            || txtTotalPayment.getText().equals("-")) {
                        dataInput = false;
                        errDataInput += "Total Pembayaran : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    } else {
                        if (totalPayment.get()
                                .compareTo(reservationController.selectedDataTransaction.getUnitNominal()) == -1) {
                            dataInput = false;
                            errDataInput += "Total Pembayaran : Nilai harus lebih besar dari '" + reservationController.selectedDataTransaction.getUnitNominal() + "' \n";
                        }
                    }
                    break;
                case 1:    //Transfer
                    if (reservationController.selectedDataTransactionWithTransfer.getSenderName() == null
                            || reservationController.selectedDataTransactionWithTransfer.getSenderName().equals("")) {
                        dataInput = false;
                        errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithTransfer.getTblBankAccountBySenderBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (reservationController.selectedDataTransactionWithTransfer.getReceiverName() == null 
//                            || reservationController.selectedDataTransactionWithTransfer.getReceiverName().equals("")) {
//                        dataInput = false;
//                        errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (reservationController.selectedDataTransactionWithTransfer.getTblBankAccountByReceiverBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 2:     //Kartu Debit
                    if (reservationController.selectedDataTransactionWithBankCard.getTblBankEdc() == null) {
                        dataInput = false;
                        errDataInput += "EDC : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getTblBankNetworkCard() == null) {
                        dataInput = false;
                        errDataInput += "Jaringan Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getTblBank() == null) {
                        dataInput = false;
                        errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (chbCardDiscount.isSelected()
//                            && reservationController.selectedDataTransactionWithBankCard.getTblBankEventCard() == null) {
//                        dataInput = false;
//                        errDataInput += "Event Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getBankCardNumber() == null 
                            || reservationController.selectedDataTransactionWithBankCard.getBankCardNumber().equals("")) {
                        dataInput = false;
                        errDataInput += "Nomor Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    } else {
//                        if (reservationController.selectedDataTransactionWithBankCard.getBankCardNumber().length() < 16
//                                || reservationController.selectedDataTransactionWithBankCard.getBankCardNumber().length() > 20) {
//                            dataInput = false;
//                            errDataInput += "Card Number : Invalid Data Input \n";
//                        }
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getApprovalCode() == null 
                            || reservationController.selectedDataTransactionWithBankCard.getApprovalCode().equals("")) {
                        dataInput = false;
                        errDataInput += "Approval Code : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    } else {
                        if (reservationController.selectedDataTransactionWithBankCard.getApprovalCode().length() != 6) {
                            dataInput = false;
                            errDataInput += "Approval Code : Data tidak sesuai \n";
                        }
                    }
                    break;
                case 3:     //Kartu Kredit
                    if (reservationController.selectedDataTransactionWithBankCard.getTblBankEdc() == null) {
                        dataInput = false;
                        errDataInput += "EDC : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getTblBankNetworkCard() == null) {
                        dataInput = false;
                        errDataInput += "Jaringan Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getTblBank() == null) {
                        dataInput = false;
                        errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (chbCardDiscount.isSelected()
//                            && reservationController.selectedDataTransactionWithBankCard.getTblBankEventCard() == null) {
//                        dataInput = false;
//                        errDataInput += "Event Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getBankCardNumber() == null 
                            || reservationController.selectedDataTransactionWithBankCard.getBankCardNumber().equals("")) {
                        dataInput = false;
                        errDataInput += "Nomor Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    } else {
//                        if (reservationController.selectedDataTransactionWithBankCard.getBankCardNumber().length() < 16
//                                || reservationController.selectedDataTransactionWithBankCard.getBankCardNumber().length() > 20) {
//                            dataInput = false;
//                            errDataInput += "Card Number : Invalid Data Input \n";
//                        }
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getBankCardHolder() == null 
                            || reservationController.selectedDataTransactionWithBankCard.getBankCardNumber().equals("")) {
                        dataInput = false;
                        errDataInput += "Nama Pemegang Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getApprovalCode() == null 
                            || reservationController.selectedDataTransactionWithBankCard.getApprovalCode().equals("")) {
                        dataInput = false;
                        errDataInput += "Approval Code : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    } else {
                        if (reservationController.selectedDataTransactionWithBankCard.getApprovalCode().length() != 6) {
                            dataInput = false;
                            errDataInput += "Approval Code : Data tidak sesuai \n";
                        }
                    }
                    if (reservationController.selectedDataTransactionWithBankCard.getBankCardExprDate() == null) {
                        dataInput = false;
                        errDataInput += "Tanggal Kadaluarsa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (exprMonth.get() == 0) {
//                        errDataInput += "Bulan Expr. : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
//                    if (exprYear.get() == 0) {
//                        errDataInput += "Tahun Expr. : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    break;
                case 4:     //Cek
                    if (reservationController.selectedDataTransactionWithCekGiro.getCodeCekGiro() == null
                            || reservationController.selectedDataTransactionWithCekGiro.getCodeCekGiro().equals("")) {
                        dataInput = false;
                        errDataInput += "Nomor Cek/Giro : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getIssueDateTime() == null) {
                        dataInput = false;
                        errDataInput += "Tanggal Buat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getValidUntilDateTime() == null) {
                        dataInput = false;
                        errDataInput += "Tanggal Jatuh Tempo : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getTblBank() == null) {
                        dataInput = false;
                        errDataInput += "Bank Penerbit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getSenderName() == null
                            || reservationController.selectedDataTransactionWithCekGiro.getSenderName().equals("")) {
                        dataInput = false;
                        errDataInput += "Nama (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (reservationController.selectedDataTransactionWithCekGiro.getReceiverName() == null 
//                            || reservationController.selectedDataTransactionWithCekGiro.getReceiverName().equals("")) {
//                        dataInput = false;
//                        errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 5:     //Giro
                    if (reservationController.selectedDataTransactionWithCekGiro.getCodeCekGiro() == null
                            || reservationController.selectedDataTransactionWithCekGiro.getCodeCekGiro().equals("")) {
                        dataInput = false;
                        errDataInput += "Nomor Cek/Giro : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getIssueDateTime() == null) {
                        dataInput = false;
                        errDataInput += "Tanggal Buat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getValidUntilDateTime() == null) {
                        dataInput = false;
                        errDataInput += "Tanggal Jatuh Tempo : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getTblBank() == null) {
                        dataInput = false;
                        errDataInput += "Bank Penerbit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getSenderName() == null
                            || reservationController.selectedDataTransactionWithCekGiro.getSenderName().equals("")) {
                        dataInput = false;
                        errDataInput += "Nama (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (reservationController.selectedDataTransactionWithCekGiro.getReceiverName() == null 
//                            || reservationController.selectedDataTransactionWithCekGiro.getReceiverName().equals("")) {
//                        dataInput = false;
//                        errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (reservationController.selectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 6:     //Travel Agent
                    if (reservationController.selectedDataTransactionWithGuaranteePayment.getTblPartner() == null) {
                        dataInput = false;
                        errDataInput += "Partner : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
//                            && (reservationController.selectedDataTransactionWithGuaranteePayment.getSenderName() == null 
//                            || reservationController.selectedDataTransactionWithGuaranteePayment.getSenderName().equals(""))) {
//                        dataInput = false;
//                        errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
                            && reservationController.selectedDataTransactionWithGuaranteePayment.getTblBankAccountBySenderBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
//                            && (reservationController.selectedDataTransactionWithGuaranteePayment.getReceiverName() == null 
//                            || reservationController.selectedDataTransactionWithGuaranteePayment.getReceiverName().equals(""))) {
//                        dataInput = false;
//                        errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
                            && reservationController.selectedDataTransactionWithGuaranteePayment.getTblBankAccountByReceiverBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 7:     //Guarantee Letter (Corporate)
                    if (reservationController.selectedDataTransactionWithGuaranteePayment.getTblPartner() == null) {
                        dataInput = false;
                        errDataInput += "Partner : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
//                            && (reservationController.selectedDataTransactionWithGuaranteePayment.getSenderName() == null 
//                            || reservationController.selectedDataTransactionWithGuaranteePayment.getSenderName().equals(""))) {
//                        dataInput = false;
//                        errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
                            && reservationController.selectedDataTransactionWithGuaranteePayment.getTblBankAccountBySenderBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
//                            && (reservationController.selectedDataTransactionWithGuaranteePayment.getReceiverName() == null 
//                            || reservationController.selectedDataTransactionWithGuaranteePayment.getReceiverName().equals(""))) {
//                        dataInput = false;
//                        errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
                            && reservationController.selectedDataTransactionWithGuaranteePayment.getTblBankAccountByReceiverBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 8:     //Guarantee Letter (Government)
                    if (reservationController.selectedDataTransactionWithGuaranteePayment.getTblPartner() == null) {
                        dataInput = false;
                        errDataInput += "Partner : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
//                            && (reservationController.selectedDataTransactionWithGuaranteePayment.getSenderName() == null 
//                            || reservationController.selectedDataTransactionWithGuaranteePayment.getSenderName().equals(""))) {
//                        dataInput = false;
//                        errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
                            && reservationController.selectedDataTransactionWithGuaranteePayment.getTblBankAccountBySenderBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
//                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
//                            && (reservationController.selectedDataTransactionWithGuaranteePayment.getReceiverName() == null 
//                            || reservationController.selectedDataTransactionWithGuaranteePayment.getReceiverName().equals(""))) {
//                        dataInput = false;
//                        errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//                    }
                    if (!reservationController.selectedDataTransactionWithGuaranteePayment.getIsDebt()
                            && reservationController.selectedDataTransactionWithGuaranteePayment.getTblBankAccountByReceiverBankAccount() == null) {
                        dataInput = false;
                        errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    }
                    break;
                case 16:    //Return Payment
                    //check cashier balance
                    if (!isNominalCashierBalanceAvailable()) {
                        dataInput = false;
                        errDataInput += "Total Uang yang Dikembalikan : Melebihi total nominal Kas Kasir..!! \n";
                    }
                    break;
                default:
                    break;
            }
        }
        return dataInput;
    }

    private BigDecimal calculationAllTotalReservationTransaction() {
        BigDecimal result = new BigDecimal("0");
        //reservation
        for (TblReservationPayment data : reservationController.dataReservationPayments) {
            if (data.getRefFinanceTransactionPaymentType().getIdtype() == 6 //Travel Agent = '6'
                    || data.getRefFinanceTransactionPaymentType().getIdtype() == 7 //Guarantee Letter (Corporate) = '7'
                    || data.getRefFinanceTransactionPaymentType().getIdtype() == 8) {    //Guarantee Letter (Government) = '8'
                TblReservationPaymentWithGuaranteePayment rpwgp = reservationController.getFReservationManager().getReservationPaymentWithGuaranteePaymentByIDPayment(data.getIdpayment());
                if (rpwgp != null
                        && rpwgp.getIsDebt()
                        && rpwgp.getTblHotelReceivable() != null) {
                    List<TblHotelFinanceTransactionHotelReceivable> hfthrs = reservationController.getFReservationManager().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(rpwgp.getTblHotelReceivable().getIdhotelReceivable());
                    for (TblHotelFinanceTransactionHotelReceivable hfthr : hfthrs) {
                        if (hfthr.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                            result = result.subtract(hfthr.getNominalTransaction());
                        } else {
                            result = result.add(hfthr.getNominalTransaction());
                        }
                    }
                }
            } else {
                if(data.getRefFinanceTransactionPaymentType().getIdtype() == 16){   //Return  = '16'
                    result = result.subtract(data.getUnitNominal());
                }else{
                    result = result.add(data.getUnitNominal());
                }
            }
        }
//        //data has been changed to 'income'
//        BigDecimal dhbctIncome = new BigDecimal("0");
//        //room
//        for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
//            if (dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus() != null
//                    && dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
//                BigDecimal roomPrice = ((new BigDecimal("1")).subtract((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
//                        .multiply((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice().subtract(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary())));
//                dhbctIncome = dhbctIncome.add(roomPrice);
//                break;
//            }
//        }
//        //additional item
//        for (TblReservationAdditionalItem dataAdditionalItem : reservationController.dataReservationAdditionalItems) {
//            if (dataAdditionalItem.getRefEndOfDayDataStatus() != null
//                    && dataAdditionalItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'){
//                BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
//                        .multiply((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity())));
//                dhbctIncome = dhbctIncome.add(additionalPrice);
//            }
//        }
//        //additional service
//        BigDecimal additonalRestoBill = new BigDecimal("0");
//        for (TblReservationAdditionalService dataAdditionalService : reservationController.dataReservationAdditionalServices) {
//            if (dataAdditionalService.getRefEndOfDayDataStatus() != null
//                    && dataAdditionalService.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'){
//                if (dataAdditionalService.getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
//                        || dataAdditionalService.getTblRoomService().getIdroomService() == 7) {   //Dine in Resto = '7'
//                    BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataAdditionalService.getPrice().multiply(new BigDecimal("1"))));
//                    additonalRestoBill = additonalRestoBill.add(additionalPrice);
//                } else {
//                    BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
//                            .multiply((dataAdditionalService.getPrice().multiply(new BigDecimal("1"))));
//                    dhbctIncome = dhbctIncome.add(additionalPrice);
//                }
//            }
//        }
//        //broken item
//        for (TblReservationBrokenItem dataBrokenItem : reservationController.dataReservationBrokenItems) {
//            if (dataBrokenItem.getRefEndOfDayDataStatus() != null
//                    && dataBrokenItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'){
//                BigDecimal additionalPrice = (new BigDecimal("1"))
//                        .multiply((dataBrokenItem.getItemCharge().multiply(dataBrokenItem.getItemQuantity())));
//                dhbctIncome = dhbctIncome.add(additionalPrice);
//            }
//        }
//        //sc + tax
//        BigDecimal sc = (dhbctIncome).multiply((reservationController.dataReservationBill.getServiceChargePercentage()));
//        BigDecimal tax = (dhbctIncome.add(sc)).multiply((reservationController.dataReservationBill.getTaxPercentage()));
//        dhbctIncome = dhbctIncome.add(sc).add(tax);
//        //additonal service (resto bill)
//        dhbctIncome = dhbctIncome.add(additonalRestoBill);
//        result = result.subtract(dhbctIncome);
//        return result.compareTo(new BigDecimal("0")) == -1
//                ? (new BigDecimal("0"))
//                : result;
        return result;
    }

    private boolean isNominalCashierBalanceAvailable() {
        if (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype() == 16) {  //Return = '16'
            TblCompanyBalance cashierBalance = reservationController.getFReservationManager().getDataCompanyBalance((long) 3);  //Kas Kasir = '3'   
            return reservationController.selectedDataTransaction.getUnitNominal().compareTo(cashierBalance.getBalanceNominal()) < 1;
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
        initFormDataReservationCanceled();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationCanceledInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

    public ReservationController getParentController() {
        return reservationController;
    }

}
