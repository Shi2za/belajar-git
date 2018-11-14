/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCanceled;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtTotalPayment;

    @FXML
    private JFXTextField txtTotalReturPayment;

    @FXML
    private JFXTextArea txtCancelingNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationCanceled() {

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
                txtTotalReturPayment,
                txtCancelingNote);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalReturPayment);
    }

    private void setSelectedDataToInputForm() {

        txtCodeReservation.textProperty().bindBidirectional(reservationController.selectedDataReservationCanceled.getTblReservation().codeReservationProperty());
        txtCustomerName.textProperty().bindBidirectional(reservationController.selectedDataReservationCanceled.getTblReservation().getTblCustomer().getTblPeople().fullNameProperty());

        txtTotalPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationAllTotalReservationTransaction()));

        Bindings.bindBidirectional(txtTotalReturPayment.textProperty(), reservationController.selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        txtCancelingNote.textProperty().bindBidirectional(reservationController.selectedDataReservationCanceled.cancelNoteProperty());

    }

    private void dataReservationCanceledSaveHandle() {
        if (checkDataInputDataReservationCanceled()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data transaction (canceling fee)
                reservationController.selectedDataTransaction.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationController.selectedDataTransaction.setTblEmployeeByIdcashier(ClassSession.currentUser.getTblEmployeeByIdemployee());
                reservationController.selectedDataTransaction.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(3));   //Canceling Fee = '3'
                reservationController.selectedDataTransaction.setRefFinanceTransactionPaymentType(reservationController.getFReservationManager().getFinanceTransactionPaymentType(16));
                //dummy entry
                TblReservationCanceled dummySelectedData = new TblReservationCanceled(reservationController.selectedDataReservationCanceled);
                dummySelectedData.setTblReservation(dummySelectedData.getTblReservation());
                dummySelectedData.getTblReservation().setTblCustomer(new TblCustomer(dummySelectedData.getTblReservation().getTblCustomer()));
                dummySelectedData.getTblReservation().getTblCustomer().setTblPeople(new TblPeople(dummySelectedData.getTblReservation().getTblCustomer().getTblPeople()));
                dummySelectedData.getTblReservation().setRefReservationStatus(new RefReservationStatus(dummySelectedData.getTblReservation().getRefReservationStatus()));
                TblReservationPayment dummySelectedDataTransaction = new TblReservationPayment(reservationController.selectedDataTransaction);
                dummySelectedDataTransaction.setTblReservationBill(new TblReservationBill(dummySelectedDataTransaction.getTblReservationBill()));
                dummySelectedDataTransaction.getTblReservationBill().setTblReservation(dummySelectedData.getTblReservation());
                dummySelectedDataTransaction.getTblReservationBill().setRefReservationBillType(new RefReservationBillType(dummySelectedDataTransaction.getTblReservationBill().getRefReservationBillType()));
                dummySelectedDataTransaction.setTblEmployeeByIdcashier(new TblEmployee(dummySelectedDataTransaction.getTblEmployeeByIdcashier()));
                dummySelectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedDataTransaction.getRefFinanceTransactionPaymentType()));
                dummySelectedDataTransaction.setRefReservationBillType(new RefReservationBillType(dummySelectedDataTransaction.getRefReservationBillType()));
                if (reservationController.getFReservationManager().insertDataReservationCanceled(reservationController.selectedDataReservationCanceled,
                        reservationController.selectedDataTransaction) != null) {
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

    private void dataReservationCanceledCancelHandle() {
//        //refresh data
//        reservationController.refreshDataSelectedReservation();
        //close form data close deposit input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationCanceled() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtTotalReturPayment.getText() == null
                || txtTotalReturPayment.getText().equals("")
                || txtTotalReturPayment.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Total Uang yang Dikembalikan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedDataTransaction.getUnitNominal().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Total Uang yang Dikembalikan : Nilai tidak dapat kurang dari '0' \n";
            } else {
                BigDecimal totalReservationPayment = calculationAllTotalReservationTransaction();
                if (reservationController.selectedDataTransaction.getUnitNominal()
                        .compareTo(totalReservationPayment) == 1) {
                    dataInput = false;
                    errDataInput += "Total Uang yang Dikembalikan : Nilai tidak dapat lebih besar '"
                            + ClassFormatter.currencyFormat.cFormat(totalReservationPayment) + "' \n";
                } else {
                    if (!isNominalCashierBalanceAvailable()) {
                        dataInput = false;
                        errDataInput += "Total Uang yang Dikembalikan : Melebihi total nominal Kas Kasir..!! \n";
                    }
                }
            }
        }
        if (txtCancelingNote.getText() == null || txtCancelingNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Keterangan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
                result = result.add(data.getUnitNominal());
            }
        }
        //checkout
        for (TblReservationPayment data : reservationController.dataCheckOutPayments) {
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
                result = result.add(data.getUnitNominal());
            }
        }
        //data has been changed to 'income'
        BigDecimal dhbctIncome = new BigDecimal("0");
        //room
        for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            if (dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus() != null
                    && dataRRTDRPD.getTblReservationRoomPriceDetail().getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'
                BigDecimal roomPrice = ((new BigDecimal("1")).subtract((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
                        .multiply((dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice().subtract(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailComplimentary())));
                dhbctIncome = dhbctIncome.add(roomPrice);
                break;
            }
        }
        //additional item
        for (TblReservationAdditionalItem dataAdditionalItem : reservationController.dataReservationAdditionalItems) {
            if (dataAdditionalItem.getRefEndOfDayDataStatus() != null
                    && dataAdditionalItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'){
                BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
                        .multiply((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity())));
                dhbctIncome = dhbctIncome.add(additionalPrice);
            }
        }
        //additional service
        for (TblReservationAdditionalService dataAdditionalService : reservationController.dataReservationAdditionalServices) {
            if (dataAdditionalService.getRefEndOfDayDataStatus() != null
                    && dataAdditionalService.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'){
                BigDecimal additionalPrice = ((new BigDecimal("1")).subtract((dataAdditionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                        .multiply((dataAdditionalService.getPrice().multiply(new BigDecimal("1"))));
                dhbctIncome = dhbctIncome.add(additionalPrice);
            }
        }
        //broken item
        for (TblReservationBrokenItem dataBrokenItem : reservationController.dataReservationBrokenItems) {
            if (dataBrokenItem.getRefEndOfDayDataStatus() != null
                    && dataBrokenItem.getRefEndOfDayDataStatus().getIdstatus() == 1) {   //Income = '1'){
                BigDecimal additionalPrice = (new BigDecimal("1"))
                        .multiply((dataBrokenItem.getItemCharge().multiply(dataBrokenItem.getItemQuantity())));
                dhbctIncome = dhbctIncome.add(additionalPrice);
            }
        }
        //sc + tax
        BigDecimal sc = (dhbctIncome).multiply((reservationController.dataReservationBill.getServiceChargePercentage()));
        BigDecimal tax = (dhbctIncome.add(sc)).multiply((reservationController.dataReservationBill.getTaxPercentage()));
        dhbctIncome = dhbctIncome.add(sc).add(tax);
        return result.subtract(dhbctIncome);
    }

    private boolean isNominalCashierBalanceAvailable() {
        TblCompanyBalance cashierBalance = reservationController.getFReservationManager().getDataCompanyBalance((long) 3);  //Kas Kasir = '3'   
        return reservationController.selectedDataTransaction.getUnitNominal().compareTo(cashierBalance.getBalanceNominal()) < 1;
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

}
