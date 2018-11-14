/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_receiving.receiving;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.view.feature_receiving.receiving.ReceivingController.DetailItemExpiredDateQuantity;
import hotelfx.view.feature_receiving.receiving.ReceivingController.DetailLocation;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReceivingDetailItemExpiredDateQuantityController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtItem;

    @FXML
    private JFXDatePicker dtpItemExpiredDate;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private Label lblUnitName;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {

        btnSave.setTooltip(new Tooltip("Simpan (Data)"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });

        initDateCalendar();
        
        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpItemExpiredDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpItemExpiredDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dtpItemExpiredDate, 
                txtItemQuantity);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }
    
    private void setSelectedDataToInputForm() {

        txtItem.setText(receivingController.selectedDataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem().getCodeItem()
                + " - " + receivingController.selectedDataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem().getItemName());

        if (receivingController.selectedDataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate() != null) {
//            dtpItemExpiredDate.setValue(receivingController.selectedDataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpItemExpiredDate.setValue(null);
        }
        dtpItemExpiredDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                receivingController.selectedDataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().setItemExpiredDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        Bindings.bindBidirectional(txtItemQuantity.textProperty(), receivingController.selectedDataDetailItemExpiredDateQuantity.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        lblUnitName.setText(receivingController.selectedDataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem().getTblUnit().getUnitName());
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", receivingController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //unbind data
                txtItemQuantity.textProperty().unbindBidirectional(receivingController.selectedDataDetailItemExpiredDateQuantity.itemQuantityProperty());
                //message : success
                ClassMessage.showSucceedAddingDataMessage("", receivingController.dialogStageDetal);
                detailItemExpiredDateQuantities.add(receivingController.selectedDataDetailItemExpiredDateQuantity);
                //set data mi detail (item quantity)
                TblMemorandumInvoiceDetail dataMIDetail = ((DetailLocation) receivingController.tableReceivingDetail.getSelectionModel().getSelectedItem()).getTblDetail();
                dataMIDetail.setItemQuantity(dataMIDetail.getItemQuantity().add(receivingController.selectedDataDetailItemExpiredDateQuantity.getItemQuantity()));
                //reset data detail item expired date qunatity input status to '1' (update)
                receivingController.dataDetailItemExpiredDateQuantityInputStatus = 1;
//            comboBoxPopup.show();
                //close form data detail
                receivingController.dialogStageDetal.close();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, receivingController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        receivingController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (dtpItemExpiredDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Kadaluarsa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            for (DetailItemExpiredDateQuantity dataDetailItemExpiredDateQuantity : detailItemExpiredDateQuantities) {
                if (receivingController.selectedDataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate().equals(
                        dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate())) {
                    dataInput = false;
                    errDataInput += "Tanggal Kadaluarsa : Data Sudah Ada (tidak dapat duplikasi data) \n";
                    break;
                }
            }
        }
        if (txtItemQuantity.getText() == null 
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (receivingController.selectedDataDetailItemExpiredDateQuantity.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah Barang : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
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
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReceivingDetailItemExpiredDateQuantityController(ReceivingController parentController,
            JFXCComboBoxPopup comboBoxPopup,
            List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities) {
        receivingController = parentController;
        this.comboBoxPopup = comboBoxPopup;
        this.detailItemExpiredDateQuantities = detailItemExpiredDateQuantities;
    }

    private final ReceivingController receivingController;

    private final JFXCComboBoxPopup comboBoxPopup;

    private final List<DetailItemExpiredDateQuantity> detailItemExpiredDateQuantities;

}
