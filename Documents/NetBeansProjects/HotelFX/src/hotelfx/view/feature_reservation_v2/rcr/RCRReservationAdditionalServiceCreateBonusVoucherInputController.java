/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rcr;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.view.feature_reservation_v2.ReservationChangeRoomInputController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
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
public class RCRReservationAdditionalServiceCreateBonusVoucherInputController implements Initializable {

    @FXML
    private AnchorPane ancFormAdditionalService;

    @FXML
    private GridPane gpFormDataAdditionalService;

    @FXML
    private JFXTextField txtServiceName;

    @FXML
    private JFXTextField txtAdditionalCharge;

    @FXML
    private JFXTextArea txtAdditionalNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataAdditionalServiceCreateBonusVoucher() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Tambahan Layanan)"));
        btnSave.setOnAction((e) -> {
            dataAdditionalServiceCreateBonusVoucherSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataAdditionalServiceCreateBonusVoucherCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtAdditionalCharge,
                txtAdditionalNote);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAdditionalCharge);
    }

    private void setSelectedDataToInputForm() {

        txtServiceName.setText(reservationChangeRoomInputController.tempSelectedAdditionalService.getTblRoomService().getServiceName()
                + " (" + reservationChangeRoomInputController.tempSelectedAdditionalService.getTblRoomService().getNote() + ")");

        txtAdditionalCharge.textProperty().bindBidirectional(reservationChangeRoomInputController.tempSelectedAdditionalService.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());

        txtAdditionalNote.textProperty().bindBidirectional(reservationChangeRoomInputController.tempSelectedAdditionalService.noteProperty());
    }

    private void dataAdditionalServiceCreateBonusVoucherSaveHandle() {
        if (checkDataInputDataAdditionalServiceCreateBonusVoucher()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationChangeRoomInputController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //save and set data additional service (bonus voucher)
                reservationChangeRoomInputController.getParentController().selectedAdditionalServices.add(reservationChangeRoomInputController.tempSelectedAdditionalService);
                reservationChangeRoomInputController.tableDataReservationAdditionalService.getTableView().setItems(reservationChangeRoomInputController.loadAllDataReservationAdditionalService());
                //update data bill
                reservationChangeRoomInputController.refreshBill();
                //close form data addtional input input
                reservationChangeRoomInputController.dialogStage.close();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationChangeRoomInputController.dialogStage);
        }
    }

    private void dataAdditionalServiceCreateBonusVoucherCancelHandle() {
        //close form data additional service input
        reservationChangeRoomInputController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataAdditionalServiceCreateBonusVoucher() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtAdditionalCharge.getText() == null
                || txtAdditionalCharge.getText().equals("")
                || txtAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Tambahan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationChangeRoomInputController.tempSelectedAdditionalService.getPrice()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Biaya Tambahan : Nilai tidak dapat kurang dari atau sama dengan '0' \n";
            } else {
                if (reservationChangeRoomInputController.tempSelectedAdditionalService.getPrice()
                        .compareTo(reservationChangeRoomInputController.calculationTotalDifferentBill().abs()) == 1) {
                    dataInput = false;
                    errDataInput += "Biaya Tambahan : Nilai tidak dapat lebih besar dari selisih biaya tagihan (" + ClassFormatter.currencyFormat.cFormat(reservationChangeRoomInputController.calculationTotalDifferentBill().abs()) + ") \n";
                }
            }
        }
        if (txtAdditionalNote.getText() == null || txtAdditionalNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Keterangan Tambahan Layanan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataAdditionalServiceCreateBonusVoucher();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCRReservationAdditionalServiceCreateBonusVoucherInputController(ReservationChangeRoomInputController parentController) {
        reservationChangeRoomInputController = parentController;
    }

    private final ReservationChangeRoomInputController reservationChangeRoomInputController;

}
