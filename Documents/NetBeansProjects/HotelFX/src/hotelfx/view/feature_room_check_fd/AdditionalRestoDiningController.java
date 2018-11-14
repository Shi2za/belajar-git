/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationAdditionalService;
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
 * @author ABC-Programmer
 */
public class AdditionalRestoDiningController implements Initializable {

    @FXML
    private AnchorPane ancFormDataReservationAdditionalService;

    @FXML
    private GridPane gpFormDataReservationAdditionalService;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtCodeReservation;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtRestoTransactionNumber;

    @FXML
    private JFXTextField txtTotalTransaction;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationAdditionalService() {
        btnSave.setTooltip(new Tooltip("Simpan (Data Tagihan Kamar)"));
        btnSave.setOnAction((e) -> {
            dataReservationAddtionalServiceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationAdditionalServiceCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtRestoTransactionNumber,
                txtTotalTransaction);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalTransaction);
    }

    private void setSelectedDataToInputForm() {

        txtRoomName.setText(roomStatusController.selectedDataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName());
        txtCodeReservation.setText(roomStatusController.selectedDataAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation());
        txtCustomerName.setText(roomStatusController.selectedDataAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getFullName());

        txtRestoTransactionNumber.textProperty().bindBidirectional(roomStatusController.selectedDataAdditionalService.restoTransactionNumberProperty());
        txtRestoTransactionNumber.textProperty().bindBidirectional(roomStatusController.selectedDataAdditionalService.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());
    }

    private void dataReservationAddtionalServiceSaveHandle() {
        if (checkDataInputDataReservationAdditionalService()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservationAdditionalService dummySelectedData = new TblReservationAdditionalService(roomStatusController.selectedDataAdditionalService);
                if (roomStatusController.getService().insertDataReservationAdditionalService(dummySelectedData)
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", roomStatusController.dialogStage);
                    // refresh all data and close form data - reservation additional service
                    roomStatusController.setSelectedDataToInputForm();
                    roomStatusController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(roomStatusController.getService().getErrorMessage(), roomStatusController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusController.dialogStage);
        }
    }

    private void dataReservationAdditionalServiceCancelHandle() {
        //refresh all data and close form data - reservation additional service
        roomStatusController.setSelectedDataToInputForm();
        roomStatusController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationAdditionalService() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtRestoTransactionNumber.getText() == null
                || txtRestoTransactionNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Transaksi (Resto)  : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtTotalTransaction.getText() == null
                || txtTotalTransaction.getText().equals("")
                || txtTotalTransaction.getText().equals("-")) {
            errDataInput += "Total Nominal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomStatusController.selectedDataAdditionalService.getPrice()
                    .compareTo(new BigDecimal("0")) < 1) {
                errDataInput += "Total Nominal Transaksi : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
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
        initFormDataReservationAdditionalService();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public AdditionalRestoDiningController(RoomStatusFDController parentController) {
        roomStatusController = parentController;
    }

    private final RoomStatusFDController roomStatusController;

}
