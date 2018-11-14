/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rctid_canceled;

import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.view.feature_reservation_v2.ReservationCanceledInputController;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class RCTIReturnPaymentController implements Initializable {

    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private JFXTextField txtTotalBillAfterRounding;

    private void initFormDataRCTIReturn() {
        //set rest of bill
        reservationCanceledInputController.totalBill.addListener((obs, oldVal, newVal) -> {
            totalBillProperty.set(newVal.abs());
        });

        totalBillProperty.addListener((obs, oldVal, newVal) -> {
            reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(getNominalAfterRounding(newVal));
            reservationCanceledInputController.getParentController().selectedDataTransaction.setRoundingValue(getRoundingValue(newVal));
//            txtTotalBill.setText(ClassFormatter.currencyFormat.format(totalBillProperty.get()));
//            txtTotalBillAfterRounding.setText(ClassFormatter.currencyFormat.format(reservationCanceledInputController.getParentController().selectedDataTransaction.getUnitNominal()));
        });

        totalBillProperty.set(reservationCanceledInputController.totalBill.get().abs());

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField();
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill,
                txtTotalBillAfterRounding);
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

    private void setSelectedDataToInputForm() {
        Bindings.bindBidirectional(txtTotalBill.textProperty(), totalBillProperty, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalBillAfterRounding.textProperty(), reservationCanceledInputController.getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
//        txtTotalBill.setText(ClassFormatter.currencyFormat.format(totalBillProperty.get()));
//        txtTotalBillAfterRounding.setText(ClassFormatter.currencyFormat.format(reservationCanceledInputController.getParentController().selectedDataTransaction.getUnitNominal()));
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
        initFormDataRCTIReturn();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCTIReturnPaymentController(ReservationCanceledInputController parentController) {
        reservationCanceledInputController = parentController;
    }

    private final ReservationCanceledInputController reservationCanceledInputController;

}
