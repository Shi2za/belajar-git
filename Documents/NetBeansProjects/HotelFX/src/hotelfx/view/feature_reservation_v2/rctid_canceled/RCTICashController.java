/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rctid_canceled;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
public class RCTICashController implements Initializable {

    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private JFXTextField txtTotalBillAfterRounding;

    @FXML
    private JFXTextField txtTotalPayment;

    @FXML
    private JFXTextField txtTotalCashBack;

    @FXML
    private JFXButton btnSave;

    private void initFormDataRCTICash() {
        //set rest of bill
        reservationCanceledInputController.totalBill.addListener((obs, oldVal, newVal) -> {
            totalBillProperty.set(newVal);
        });
        
        totalBillProperty.addListener((obs, oldVal, newVal) -> {
            reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(getNominalAfterRounding(newVal));
            reservationCanceledInputController.getParentController().selectedDataTransaction.setRoundingValue(getRoundingValue(newVal));
            totalRestOfBillProperty.set(totalPaymentProperty.get()
                    .subtract(reservationCanceledInputController.getParentController().selectedDataTransaction.getUnitNominal()));
        });

        totalBillProperty.set(reservationCanceledInputController.totalBill.get());        
        
        totalPaymentProperty.addListener((obs, oldVal, newVal) -> {
            totalRestOfBillProperty.set(newVal
                    .subtract(reservationCanceledInputController.getParentController().selectedDataTransaction.getUnitNominal()));
            reservationCanceledInputController.totalPayment.set(newVal);
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
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
        Bindings.bindBidirectional(txtTotalBillAfterRounding.textProperty(), reservationCanceledInputController.getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalPayment.textProperty(), totalPaymentProperty, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTotalCashBack.textProperty(), totalRestOfBillProperty, new ClassFormatter.CBigDecimalStringConverter());
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

    public RCTICashController(ReservationCanceledInputController parentController) {
        reservationCanceledInputController = parentController;
    }

    private final ReservationCanceledInputController reservationCanceledInputController;

}
