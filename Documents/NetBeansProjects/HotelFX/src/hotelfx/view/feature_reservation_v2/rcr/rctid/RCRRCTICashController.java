/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rcr.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.view.feature_reservation_v2.rcr.RCRRCTransactionInputController;
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
public class RCRRCTICashController implements Initializable {

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
        totalBillProperty.set(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()
                .subtract(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getRoundingValue()));

        totalBillProperty.addListener((obs, oldVal, newVal) -> {
            rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.setUnitNominal(getNominalAfterRounding(newVal));
            rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.setRoundingValue(getRoundingValue(newVal));
            if (newVal
                    .compareTo((rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                            .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) != 0) {
                chbGetRestOfBill.setSelected(false);
            }
        });

        chbGetRestOfBill.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set rest of bill
//                rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.setUnitNominal(rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
//                        .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
                totalBillProperty.set(rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                        .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
            }
        });

//        rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal
//                    .compareTo((rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
//                            .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) != 0) {
//                chbGetRestOfBill.setSelected(false);
//            }
//        });
        totalPaymentProperty.addListener((obs, oldVal, newVal) -> {
            totalRestOfBillProperty.set(newVal
                    .subtract(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()));
        });

        rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
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
        if (nominal != null) {
            BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("100"))).setScale(0, RoundingMode.UP)).multiply(new BigDecimal("100"));
            return nominalAfterRounding;
        } else {
            return new BigDecimal("0");
        }
    }

    private BigDecimal getRoundingValue(BigDecimal nominal) {
        if (nominal != null) {
            BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("100"))).setScale(0, RoundingMode.UP)).multiply(new BigDecimal("100"));
            return nominalAfterRounding.subtract(nominal);
        } else {
            return new BigDecimal("0");
        }
    }

    private final ObjectProperty<BigDecimal> totalBillProperty = new SimpleObjectProperty<>(new BigDecimal("0"));
    private final ObjectProperty<BigDecimal> totalPaymentProperty = new SimpleObjectProperty<>(new BigDecimal("0"));
    private final ObjectProperty<BigDecimal> totalRestOfBillProperty = new SimpleObjectProperty<>(new BigDecimal("0"));

    private void setSelectedDataToInputForm() {
        Bindings.bindBidirectional(txtTotalBill.textProperty(), totalBillProperty, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalBillAfterRounding.textProperty(), rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalPayment.textProperty(), totalPaymentProperty, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalCashBack.textProperty(), totalRestOfBillProperty, new ClassFormatter.CBigDecimalStringConverter());

        //if updating data
        if (rcrrcTransactionInputController.getParentController().dataInputRCTransactionInputDetailStatus == 1) {
            totalPaymentProperty.set(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal());
        }
    }

    private void dataRCTICashSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcrrcTransactionInputController.getParentController().dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //save data transaction & close dialog data rc-transaction detail
                rcrrcTransactionInputController.getParentController().saveDataTransaction();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcrrcTransactionInputController.getParentController().dialogStage);
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
            if (rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi : Tidak boleh kurang dari '0' \n";
            } else {
                if ((rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()
                        .compareTo(new BigDecimal("0")) == 0)
                        && ((rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                        .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))
                        .compareTo(new BigDecimal("0")) != 0)) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Tidak boleh '0' \n";
                } else {
                    if ((rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()
                            .subtract(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getRoundingValue()))
                            .compareTo((rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                                    .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                        dataInput = false;
                        errDataInput += "Nominal Transaksi : Nilai tidak boleh lebih besar dari '" + (rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                                .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
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
                    .compareTo(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()) == -1) {
                dataInput = false;
                errDataInput += "Total Pembayaran : Nilai harus lebih besar dari '" + rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal() + "' \n";
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

    public RCRRCTICashController(RCRRCTransactionInputController parentController) {
        rcrrcTransactionInputController = parentController;
    }

    private final RCRRCTransactionInputController rcrrcTransactionInputController;

}
