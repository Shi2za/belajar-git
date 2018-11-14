/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.view.feature_reservation.RCTransactionInputDetailController;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCTICashController implements Initializable {

    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private JFXTextField txtTotalBillAfterRounding;

    @FXML
    private JFXCheckBox chbGetRestOfBill;

    @FXML
    private JFXTextField txtTotalPayment;

    @FXML
    private JFXTextField txtTotalCashBack;

    @FXML
    private JFXButton btnSave;

    private void initFormDataRCTICash() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataRCTICashSaveHandle();
        });

        //rest of bill
        totalBillProperty.set(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                .subtract(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getRoundingValue()));

        totalBillProperty.addListener((obs, oldVal, newVal) -> {
            rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(getNominalAfterRounding(newVal));
            rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setRoundingValue(getRoundingValue(newVal));
            if (newVal
                    .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) != 0) {
                chbGetRestOfBill.setSelected(false);
            }
        });

        chbGetRestOfBill.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set rest of bill
//                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
//                        .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
                totalBillProperty.set(rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
                        .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
            }
        });

//        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal
//                    .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
//                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) != 0) {
//                chbGetRestOfBill.setSelected(false);
//            }
//        });
        totalPaymentProperty.addListener((obs, oldVal, newVal) -> {
            totalRestOfBillProperty.set(newVal
                    .subtract(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()));
        });

        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
            totalRestOfBillProperty.set(totalPaymentProperty.get()
                    .subtract(newVal));
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtTotalBill,
                txtTotalPayment);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill,
                txtTotalBillAfterRounding,
                txtTotalPayment,
                txtTotalCashBack);
    }
    
    private BigDecimal getNominalAfterRounding(BigDecimal nominal) {
        BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("100"))).setScale(0, RoundingMode.UP)).multiply(new BigDecimal("100"));
        return nominalAfterRounding;
    }

    private BigDecimal getRoundingValue(BigDecimal nominal) {
        BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("100"))).setScale(0, RoundingMode.UP)).multiply(new BigDecimal("100"));
        return nominalAfterRounding.subtract(nominal);
    }

    private final ObjectProperty<BigDecimal> totalBillProperty = new SimpleObjectProperty<>(new BigDecimal("0"));
    private final ObjectProperty<BigDecimal> totalPaymentProperty = new SimpleObjectProperty<>(new BigDecimal("0"));
    private final ObjectProperty<BigDecimal> totalRestOfBillProperty = new SimpleObjectProperty<>(new BigDecimal("0"));

    private void setSelectedDataToInputForm() {
        Bindings.bindBidirectional(txtTotalBill.textProperty(), totalBillProperty, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalBillAfterRounding.textProperty(), rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalPayment.textProperty(), totalPaymentProperty, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalCashBack.textProperty(), totalRestOfBillProperty, new ClassFormatter.CBigDecimalStringConverter());

        //if updating data
        if (rcTransactionInputDetailController.getParentController().dataInputRCTransactionInputDetailStatus == 1) {
            totalPaymentProperty.set(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal());
        }

    }

    private void dataRCTICashSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcTransactionInputDetailController.getParentController().dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //save data transaction & close dialog data rc-transaction detail
                rcTransactionInputDetailController.getParentController().saveDataTransaction();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcTransactionInputDetailController.getParentController().dialogStage);
        }
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationPayment() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtTotalBill.getText() == null 
                || txtTotalBill.getText().equals("")
                || txtTotalBill.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi : Tidak boleh kurang dari '0' \n";
            } else {
                if ((rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                        .compareTo(new BigDecimal("0")) == 0)
                        && ((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(
                                rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
                        .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))
                        .compareTo(new BigDecimal("0")) != 0)) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Tidak boleh '0' \n";
                } else {
                    if ((rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                            .subtract(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getRoundingValue()))
                            .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(
                                            rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
                                    .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                        dataInput = false;
                        errDataInput += "Nominal Transaksi : Nilai tidak boleh lebih besar dari '" + (rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill(
                                rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus)
                                .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
                    }
                }
            }
        }
        if (txtTotalPayment.getText() == null 
                || txtTotalPayment.getText().equals("")
                || txtTotalPayment.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Total Pembayaran : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (totalPaymentProperty.get()
                    .compareTo(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()) == -1) {
                dataInput = false;
                errDataInput += "Total Pembayaran : Nilai harus lebih besar dari '" + rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal() + "' \n";
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
        initFormDataRCTICash();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCTICashController(RCTransactionInputDetailController parentController) {
        rcTransactionInputDetailController = parentController;
    }

    private final RCTransactionInputDetailController rcTransactionInputDetailController;

}
