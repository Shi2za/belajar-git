/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.RefSystemLogBy;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemUser;
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
public class ReservationLogBookInputController implements Initializable {

    @FXML
    private AnchorPane ancFormLogBookInput;

    @FXML
    private GridPane gpFormDataLogBookInput;

    @FXML
    private JFXTextField txtHeaderLog;

    @FXML
    private JFXTextArea txtDataLog;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataLogBookInput() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Log Book)"));
        btnSave.setOnAction((e) -> {
            dataSystemLogBookSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataSystemLogBookCancelHandle();
        });

    }

    private void setSelectedDataToInputForm() {

        txtHeaderLog.textProperty().bindBidirectional(reservationLogBookController.selectedDataLogBook.logBookHeaderProperty());

        txtDataLog.textProperty().bindBidirectional(reservationLogBookController.selectedDataLogBook.logBookDetailProperty());

    }

    private void dataSystemLogBookSaveHandle() {
        if (checkDataInputDataLogBookInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationLogBookController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblSystemLogBook dummySelectedData = new TblSystemLogBook(reservationLogBookController.selectedDataLogBook);
                if(dummySelectedData.getTblSystemUser()!= null){
                    dummySelectedData.setTblSystemUser(new TblSystemUser(dummySelectedData.getTblSystemUser()));
                }
                if(dummySelectedData.getTblEmployeeByIdemployee() != null){
                    dummySelectedData.setTblEmployeeByIdemployee(new TblEmployee(dummySelectedData.getTblEmployeeByIdemployee()));
                }
                switch (reservationLogBookController.dataInputStatus) {
                    case 0: //insert
//                        if (reservationLogBookController.getService().insertDataSystemLog(dummySelectedData) != null) {
                        if(true){
                            ClassMessage.showSucceedInsertingDataMessage("", reservationLogBookController.dialogStage);
                            //refresh data from table & close form data log book input
                            reservationLogBookController.tableDataSystemLogBook.getTableView().setItems(reservationLogBookController.loadAllDataSystemLogBook());
                            reservationLogBookController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(reservationLogBookController.getService().getErrorMessage(), reservationLogBookController.dialogStage);
                        }
                        break;
                    case 1: //update
//                        if (reservationLogBookController.getService().updateDataSystemLog(dummySelectedData)) {
                        if(true){
                            ClassMessage.showSucceedUpdatingDataMessage("", reservationLogBookController.dialogStage);
                            //refresh data from table & close form data log book input
                            reservationLogBookController.tableDataSystemLogBook.getTableView().setItems(reservationLogBookController.loadAllDataSystemLogBook());
                            reservationLogBookController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(reservationLogBookController.getService().getErrorMessage(), reservationLogBookController.dialogStage);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationLogBookController.dialogStage);
        }
    }

    private void dataSystemLogBookCancelHandle() {
        //close form data log book input
        reservationLogBookController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataLogBookInput() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtHeaderLog.getText() == null || txtHeaderLog.getText().equals("")) {
            dataInput = false;
            errDataInput += "Log Book : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtDataLog.getText() == null || txtDataLog.getText().equals("")) {
            dataInput = false;
            errDataInput += "Detail Log Book : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataLogBookInput();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationLogBookInputController(ReservationLogBookController parentController) {
        reservationLogBookController = parentController;
    }

    private final ReservationLogBookController reservationLogBookController;

}
