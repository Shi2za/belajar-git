/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank_edc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PrintEDCTransactionController implements Initializable {

    @FXML
    private AnchorPane ancFormPrintEDCTransaction;

    @FXML
    private GridPane gpFormDataPrintEDCTransaction;

    @FXML
    private JFXDatePicker dtpBeginPeriode;

    @FXML
    private JFXDatePicker dtpEndPeriode;

    @FXML
    private AnchorPane ancDataEDCTransactionLayout;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXButton btnBack;

    private void initFormDataPrintEDCTransaction() {

        btnPrint.setTooltip(new Tooltip("Cetak (Data Transaksi EDC)"));
        btnPrint.setOnAction((e) -> {
            dataPrintHandle();
        });

        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            dataBackHandle();
        });

        initImportantFieldColor();
        
        dtpBeginPeriode.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (dtpEndPeriode.getValue() != null
                        && newVal.isBefore(dtpEndPeriode.getValue())) {
                    initTableDataEDCTransaction(loadAllDataReservationPaymentWithBankCards(newVal, dtpEndPeriode.getValue()));
                } else {
                    initTableDataEDCTransaction(new ArrayList<>());
                }
            } else {
                initTableDataEDCTransaction(new ArrayList<>());
            }
        });

        dtpEndPeriode.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (dtpBeginPeriode.getValue() != null
                        && newVal.isAfter(dtpBeginPeriode.getValue())) {
                    initTableDataEDCTransaction(loadAllDataReservationPaymentWithBankCards(dtpBeginPeriode.getValue(), newVal));
                } else {
                    initTableDataEDCTransaction(new ArrayList<>());
                }
            } else {
                initTableDataEDCTransaction(new ArrayList<>());
            }
        });

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dtpBeginPeriode, 
                dtpEndPeriode);
    }
    
    private List<TblReservationPaymentWithBankCard> loadAllDataReservationPaymentWithBankCards(LocalDate beginPeriode,
            LocalDate endPeriode) {
        List<TblReservationPaymentWithBankCard> list = bankEDCController.getService().getAllDataReservationPaymentWithBankCardByIDEDCAndPaymentDateInRange(
                bankEDCController.selectedData.getIdedc(),
                Date.valueOf(beginPeriode),
                Date.valueOf(endPeriode));
        for (TblReservationPaymentWithBankCard data : list) {
            //data reservation payment
            data.setTblReservationPayment(bankEDCController.getService().getDataReservationPayment(data.getTblReservationPayment().getIdpayment()));
            //data reservation bill
            data.getTblReservationPayment().setTblReservationBill(bankEDCController.getService().getDataReservationBill(data.getTblReservationPayment().getTblReservationBill().getIdbill()));
            //data reservation
            data.getTblReservationPayment().getTblReservationBill().setTblReservation(bankEDCController.getService().getDataReservation(data.getTblReservationPayment().getTblReservationBill().getTblReservation().getIdreservation()));
            //data customer
            //data customer - people
            //data employee (casheir)
            data.getTblReservationPayment().setTblEmployeeByIdcashier(bankEDCController.getService().getDataEmployee(data.getTblReservationPayment().getTblEmployeeByIdcashier().getIdemployee()));
            //data employee - people
            data.getTblReservationPayment().getTblEmployeeByIdcashier().setTblPeople(bankEDCController.getService().getDataPeople(data.getTblReservationPayment().getTblEmployeeByIdcashier().getTblPeople().getIdpeople()));
            //data bank
            data.setTblBank(bankEDCController.getService().getBank(data.getTblBank().getIdbank()));
            //data edc
            data.setTblBankEdc(bankEDCController.getService().getBankEDC(data.getTblBankEdc().getIdedc()));
            //data network
            data.setTblBankNetworkCard(bankEDCController.getService().getBankNetworkCard(data.getTblBankNetworkCard().getIdnetworkCard()));
            //data bank account (hotel-balance)
            data.setTblBankAccount(bankEDCController.getService().getDataBankAccount(data.getTblBankAccount().getIdbankAccount()));
        }
        return list;
    }

    private void setSelectedDataToInputForm() {

        initTableDataEDCTransaction(new ArrayList<>());

    }

    private void initTableDataEDCTransaction(List<TblReservationPaymentWithBankCard> dataReservationPaymentWithBankCards) {
        //set table
        setTableDataEDCTransaction(dataReservationPaymentWithBankCards);
        //set table to content-view
        ancDataEDCTransactionLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataEDCTransaction, 0.0);
        AnchorPane.setLeftAnchor(tableDataEDCTransaction, 0.0);
        AnchorPane.setRightAnchor(tableDataEDCTransaction, 0.0);
        AnchorPane.setTopAnchor(tableDataEDCTransaction, 0.0);
        ancDataEDCTransactionLayout.getChildren().add(tableDataEDCTransaction);
    }

    private TableView<TblReservationPaymentWithBankCard> tableDataEDCTransaction;

    private void setTableDataEDCTransaction(List<TblReservationPaymentWithBankCard> dataReservationPaymentWithBankCards) {
        tableDataEDCTransaction = new TableView();

        TableColumn<TblReservationPaymentWithBankCard, String> transactionDate = new TableColumn("Tgl. Transaksi");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithBankCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getTblReservationPayment().getPaymentDate()),
                        param.getValue().getTblReservationPayment().paymentDateProperty()));
        transactionDate.setMinWidth(160);

        TableColumn<TblReservationPaymentWithBankCard, String> codeReservation = new TableColumn("No. Reservasi");
        codeReservation.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithBankCard, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblReservationPayment().getTblReservationBill().getTblReservation().getCodeReservation(),
                        param.getValue().getTblReservationPayment().getTblReservationBill().getTblReservation().codeReservationProperty()));
        codeReservation.setMinWidth(120);

        TableColumn<TblReservationPaymentWithBankCard, String> bankAccountBalance = new TableColumn("No. Rek EDC");
        bankAccountBalance.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithBankCard, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblBankAccount().getCodeBankAccount(),
                        param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccountBalance.setMinWidth(120);

        TableColumn<TblReservationPaymentWithBankCard, String> transactionNominal = new TableColumn("Nilai Transaksi Reservasi");
        transactionNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithBankCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblReservationPayment().getUnitNominal()), 
                        param.getValue().getTblReservationPayment().unitNominalProperty()));
        transactionNominal.setMinWidth(180);

        TableColumn<TblReservationPaymentWithBankCard, String> mdrNominal = new TableColumn("Nilai MDR");
        mdrNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithBankCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPaymentCharge()), 
                        param.getValue().paymentChargeProperty()));
        mdrNominal.setMinWidth(160);

        TableColumn<TblReservationPaymentWithBankCard, String> noCardAppCode = new TableColumn("Nomor Kartu / App. Code");
        noCardAppCode.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPaymentWithBankCard, String> param)
                -> Bindings.createStringBinding(() -> 
                        "xx" + param.getValue().getBankCardNumber().substring(2, 6)
                        + " / "
                        + param.getValue().getApprovalCode(), 
                        param.getValue().iddetilProperty()));
        noCardAppCode.setMinWidth(180);

        tableDataEDCTransaction.getColumns().addAll(transactionDate, codeReservation, bankAccountBalance, transactionNominal, mdrNominal, noCardAppCode);
        tableDataEDCTransaction.setItems(FXCollections.observableArrayList(dataReservationPaymentWithBankCards));
    }

    private void dataPrintHandle() {
        if (checkDataInput()) {
//            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", bankEDCController.dialogStage);
//            if (alert.getResult() == ButtonType.OK) {
//                switch (bankEDCController.dataInputEDCNetworkCardStatus) {
//                    case 0:
//                        ClassMessage.showSucceedAddingDataMessage("", bankEDCController.dialogStage);
//                        bankEDCController.tableDataEDCNetworkCard.getTableView().getItems().add(bankEDCController.selectedDataEDCNetworkCard);
//                        //close form data edc - network card
//                        bankEDCController.dialogStage.close();
//                        break;
//                    case 1:
//                        ClassMessage.showSucceedUpdatingDataMessage("", bankEDCController.dialogStage);
//                        bankEDCController.tableDataEDCNetworkCard.getTableView().getItems().set(bankEDCController.tableDataEDCNetworkCard.getTableView().getSelectionModel().getSelectedIndex(),
//                                bankEDCController.selectedDataEDCNetworkCard);
//                        //close form data edc - network card
//                        bankEDCController.dialogStage.close();
//                        break;
//                    default:
//                        break;
//                }
//            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, bankEDCController.dialogStage);
        }
    }

    private void dataBackHandle() {
        //close form data print edc transaction
        bankEDCController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInput() {
        boolean dataInput = true;
        errDataInput = "";
        if (tableDataEDCTransaction.getItems().isEmpty()) {
            dataInput = false;
            errDataInput += "Tidak ada data yang harus dicetak..!!\n";
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
        initFormDataPrintEDCTransaction();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public PrintEDCTransactionController(BankEDCController parentController) {
        bankEDCController = parentController;
    }

    private final BankEDCController bankEDCController;

}
