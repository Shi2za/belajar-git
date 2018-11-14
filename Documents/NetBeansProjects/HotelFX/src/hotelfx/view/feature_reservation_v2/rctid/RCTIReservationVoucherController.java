/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.view.feature_reservation_v2.RCTransactionInputDetailController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCTIReservationVoucherController implements Initializable {

    @FXML
    private GridPane gpRCTIReservationVoucher;

    @FXML
    private JFXTextField txtTotalBill;

    private JFXCComboBoxTablePopup<TblReservationVoucher> cbpReservationVoucher;

    @FXML
    private JFXButton btnSave;

    private void initFormDataRCTIReservationVoucher() {
        initDataRCTIReservationVoucherPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataRCTIReservationVoucherSaveHandle();
        });        

        initImportantFieldColor();

        initNumbericField();
        
        if (rcTransactionInputDetailController.getParentController().dataInputRCTransactionInputDetailStatus == 0) {    //create
            //set nominal '0'
            rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
        }
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpReservationVoucher);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill);
    }
    
    private void initDataRCTIReservationVoucherPopup() {
        //Reservation Voucher
        TableView<TblReservationVoucher> tableReservationVoucher = new TableView<>();

        TableColumn<TblReservationVoucher, String> codeVoucher = new TableColumn<>("ID");
        codeVoucher.setCellValueFactory(cellData -> cellData.getValue().codeVoucherProperty());
        codeVoucher.setMinWidth(120);

        TableColumn<TblReservationVoucher, String> voucherName = new TableColumn<>("Voucher");
        voucherName.setCellValueFactory(cellData -> cellData.getValue().voucherNameProperty());
        voucherName.setMinWidth(140);

        tableReservationVoucher.getColumns().addAll(codeVoucher, voucherName);

        ObservableList<TblReservationVoucher> voucherItems = FXCollections.observableArrayList(loadAllDataReservationVoucher());

        cbpReservationVoucher = new JFXCComboBoxTablePopup<>(
                TblReservationVoucher.class, tableReservationVoucher, voucherItems, "", "Voucher *", true, 280, 250
        );

        //attached to grid-pane
        gpRCTIReservationVoucher.add(cbpReservationVoucher, 0, 0);
    }

    private void refreshDataPopup() {
        //reservation voucher
        ObservableList<TblReservationVoucher> voucherItems = FXCollections.observableArrayList(loadAllDataReservationVoucher());
        cbpReservationVoucher.setItems(voucherItems);
    }

    private List<TblReservationVoucher> loadAllDataReservationVoucher() {
        List<TblReservationVoucher> list = rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getAllDataReservationVoucher();
        for (int i = list.size() - 1; i > -1; i--) {
            list.get(i).setRefVoucherStatus(rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getVoucherStatus(list.get(i).getRefVoucherStatus().getIdstatus()));
            if (list.get(i).getRefVoucherStatus().getIdstatus() != 1 //Ready to Used = '1'
                    || list.get(i).getValidUntil().before(Timestamp.valueOf(LocalDateTime.now()))
                    || (list.get(i).getMinimumPayment()
                    .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1)) {
                list.remove(i);
            }
        }
        return list;
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        Bindings.bindBidirectional(txtTotalBill.textProperty(), rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        cbpReservationVoucher.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithReservationVoucher.tblReservationVoucherProperty());

        cbpReservationVoucher.valueProperty().addListener((obs, oldVal, newVal) -> {
            rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(newVal.getNominal());
        });

        cbpReservationVoucher.hide();
    }

    private void dataRCTIReservationVoucherSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcTransactionInputDetailController.getParentController().dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                        .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                                .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                    rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
                }
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
        if (cbpReservationVoucher.getValue() == null) {
            dataInput = false;
            errDataInput += "Voucher : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataRCTIReservationVoucher();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public RCTIReservationVoucherController(RCTransactionInputDetailController parentController) {
        rcTransactionInputDetailController = parentController;
    }

    private final RCTransactionInputDetailController rcTransactionInputDetailController;
    
}
