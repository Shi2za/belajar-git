/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
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
public class PurchaseOrderCanceledController implements Initializable {

    @FXML
    private AnchorPane ancFormCanceled;

    @FXML
    private GridPane gpFormDataCanceled;

    @FXML
    private JFXTextField txtCodePO;

    @FXML
    private JFXTextArea txtCanceledNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCanceled() {
        btnSave.setTooltip(new Tooltip("Simpan"));
        btnSave.setOnAction((e) -> {
            dataCanceledSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCanceledCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCanceledNote);
    }

    private void setSelectedDataToInputForm() {

        txtCodePO.setText(purchaseOrderController.selectedData.getCodePo());

        txtCanceledNote.textProperty().bindBidirectional(purchaseOrderController.selectedData.canceledNoteProperty());
    }

    private void dataCanceledSaveHandle() {
        if (checkDataInputDataCanceled()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membatalkan data ini?", null);
            if (alert.getResult() == ButtonType.OK) {
                if (purchaseOrderController.getService().deleteDataPurchaseOrder(purchaseOrderController.selectedData)) {
                    HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dibatalkan..!!", null);
                    //refresh data from table & close form data purchase order
                    purchaseOrderController.refreshDataTablePO();
                    //close form data detail
                    purchaseOrderController.dialogStageDetal.close();
                } else {
                    HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibatalkan..!!", purchaseOrderController.getService().getErrorMessage());
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderController.dialogStageDetal);
        }
    }

    private void dataCanceledCancelHandle() {
        //close form data canceled
        purchaseOrderController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCanceled() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCanceledNote.getText() == null
                || txtCanceledNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Alasan Pembatalan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataCanceled();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public PurchaseOrderCanceledController(PurchaseOrderController parentController) {
        purchaseOrderController = parentController;
    }

    private final PurchaseOrderController purchaseOrderController;

}
