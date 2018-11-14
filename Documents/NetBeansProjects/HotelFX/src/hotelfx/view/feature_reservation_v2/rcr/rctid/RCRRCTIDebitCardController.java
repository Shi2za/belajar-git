/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rcr.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.view.feature_reservation_v2.rcr.RCRRCTransactionInputController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
public class RCRRCTIDebitCardController implements Initializable {

    @FXML
    private GridPane gpRCTIDebitCard;

    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private JFXCheckBox chbGetRestOfBill;

    @FXML
    private JFXTextField txtTransactionPayment;

    @FXML
    private JFXTextField txtCardNumber;

    @FXML
    private JFXTextField txtApprovalCode;

    private JFXCComboBoxTablePopup<TblBankEdc> cbpBankEDC;

    private JFXCComboBoxTablePopup<TblBankNetworkCard> cbpBankNetworkCard;

    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private JFXTextField txtDiscountPayment;

    @FXML
    private JFXCheckBox chbCardDiscount;

    @FXML
    private AnchorPane ancCardEventLayout;

//    private final JFXCComboBoxPopup<TblBankEventCard> cbpBankCardEvent = new JFXCComboBoxPopup<>(TblBankEventCard.class);
    @FXML
    private JFXButton btnSave;

    private void initFormDataRCTIDebitCard() {
        initDataRCTIDebitCardPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataRCTIDebitCardSaveHandle();
        });

        //rest of bill
        chbGetRestOfBill.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set rest of bill
                totalBill.set(rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                        .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
            }
        });

        totalBill.addListener((obs, oldVal, newVal) -> {
            if (newVal
                    .compareTo((rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                            .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) != 0) {
                chbGetRestOfBill.setSelected(false);
            }
        });

        initImportantFieldColor();

        initNumbericField();

        //discount
//        if(rcrrcTransactionInputController.getParentController().getParentController().dataInputTransactionStatus.equals("reservation")){
//        chbCardDiscount.setDisable(!rcrrcTransactionInputController.getParentController().useCardEventAvailable());
//        }else{
//            chbCardDiscount.setDisable(true);
//        }
//        chbCardDiscount.setSelected(false);
        ancCardEventLayout.setVisible(false);

//        chbCardDiscount.selectedProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal) {
//                ancCardEventLayout.setVisible(true);
//            } else {
//                rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.setTblBankEventCard(null);
//                ancCardEventLayout.setVisible(false);
//            }
//        });
//
//        chbCardDiscount.setSelected(rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard() != null);
        chbCardDiscount.setVisible(false);
    }

    private BigDecimal calculationDiscountPayment() {
        BigDecimal result = new BigDecimal("0");
//        if (rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard() != null) {
//            result = totalBill.get()
//                    .multiply(rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard().getDiscountPercentage().divide(new BigDecimal("100")));
//            if (result > rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard().getDiscountNominal()) {
//                result = rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard().getDiscountNominal();
//            }
//        }
        return result;
    }

    private BigDecimal calculationMDRChargePayment() {
        BigDecimal result = new BigDecimal("0");
        TblBankEdcBankNetworkCard edcNetwrokCard = rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getDataBankEDCBankNetworkCardByIDEDCAndIDNetworkCardAndIDBankCardTypeAndOnUs(
                rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getTblBankEdc().getIdedc(),
                rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getTblBankNetworkCard().getIdnetworkCard(),
                0, //Debit = '0'
                rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getTblBankEdc().getTblBank().getIdbank()
                == rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getTblBank().getIdbank());
        if (edcNetwrokCard != null
                && (edcNetwrokCard.getNetworkMdr()
                .compareTo(new BigDecimal("0")) == 1)) {
            result = (totalBill.get().subtract(calculationDiscountPayment()))
                    .multiply(edcNetwrokCard.getNetworkMdr().divide(new BigDecimal("100")));
        }
        return result;
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtTotalBill,
                cbpBankEDC,
                cbpBankNetworkCard,
                cbpBank,
                txtCardNumber,
                txtApprovalCode);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill,
                txtTransactionPayment);
    }

    private void initDataRCTIDebitCardPopup() {
        //Bank EDC
        TableView<TblBankEdc> tableEDC = new TableView<>();

        TableColumn<TblBankEdc, String> bankEDCName = new TableColumn<>("EDC");
        bankEDCName.setCellValueFactory(cellData -> cellData.getValue().edcnameProperty());
        bankEDCName.setMinWidth(140);

        tableEDC.getColumns().addAll(bankEDCName);

        ObservableList<TblBankEdc> bankEDCItems = FXCollections.observableArrayList(loadAllDataBankEDC());

        cbpBankEDC = new JFXCComboBoxTablePopup<>(
                TblBankEdc.class, tableEDC, bankEDCItems, "", "EDC *", true, 200, 200
        );

        //Bank Network Card
        TableView<TblBankNetworkCard> tableNetworkCard = new TableView<>();

        TableColumn<TblBankNetworkCard, String> bankNetworkCardName = new TableColumn<>("Jaringan Kartu");
        bankNetworkCardName.setCellValueFactory(cellData -> cellData.getValue().networkCardNameProperty());
        bankNetworkCardName.setMinWidth(140);

        tableNetworkCard.getColumns().addAll(bankNetworkCardName);

        ObservableList<TblBankNetworkCard> bankNetworkCardItems = FXCollections.observableArrayList(loadAllDataBankNetworkCard());

        cbpBankNetworkCard = new JFXCComboBoxTablePopup<>(
                TblBankNetworkCard.class, tableNetworkCard, bankNetworkCardItems, "", "Jaringan Kartu *", true, 200, 200
        );

        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getAllDataBank());

        cbpBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableBank, bankItems, "", "Nomor Rekening *", true, 200, 200
        );

//        //Bank Card Event
//        TableView<TblBankEventCard> tableBankCardEvent = new TableView<>();
//        
//        TableColumn<TblBankEventCard, String> codeBankCardEvent = new TableColumn<>("ID");
//        codeBankCardEvent.setCellValueFactory(cellData -> cellData.getValue().codeEventProperty());
//        codeBankCardEvent.setMinWidth(120);
//        
//        TableColumn<TblBankEventCard, String> bankCardEventName = new TableColumn<>("Event");
//        bankCardEventName.setCellValueFactory(cellData -> cellData.getValue().eventNameProperty());
//        bankCardEventName.setMinWidth(140);
//        
//        TableColumn<TblBankEventCard, String> cardTypeCanBeUsed = new TableColumn<>("Tipe Kartu");
//        cardTypeCanBeUsed.setCellValueFactory(cellData -> cellData.getValue().eventNameProperty());
//        cardTypeCanBeUsed.setMinWidth(140);
//
//        TableColumn<TblBankEventCard, String> bankNameEventCard = new TableColumn("Bank");
//        bankNameEventCard.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
//        bankNameEventCard.setMinWidth(140);
//        
//        TableColumn<TblBankEventCard, String> bankCardEventDiscountPercentage = new TableColumn("Diskon(%)");
//        bankCardEventDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDiscountPercentage()), param.getValue().discountPercentageProperty()));
//        bankCardEventDiscountPercentage.setMinWidth(140);
//        
//        TableColumn<TblBankEventCard, String> bankCardEventMinTransaction = new TableColumn("Minimal Transaksi");
//        bankCardEventMinTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEventCard, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getMinTransaction()), param.getValue().minTransactionProperty()));
//        bankCardEventMinTransaction.setMinWidth(180);
//
//        tableBankCardEvent.getColumns().addAll(codeBankCardEvent, bankCardEventName, cardTypeCanBeUsed, bankNameEventCard, bankCardEventDiscountPercentage, bankCardEventMinTransaction);
//
//        ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//
//        setFunctionPopup(cbpBankCardEvent, tableBankCardEvent, bankCardEventItems, "eventName", "Event *", 880, 300);
        //attached to grid-pane
        gpRCTIDebitCard.add(cbpBankEDC, 0, 3);
        gpRCTIDebitCard.add(cbpBankNetworkCard, 0, 4);
        gpRCTIDebitCard.add(cbpBank, 1, 4);
//        AnchorPane.setBottomAnchor(cbpBankCardEvent, 0.0);
//        AnchorPane.setLeftAnchor(cbpBankCardEvent, 0.0);
//        AnchorPane.setRightAnchor(cbpBankCardEvent, 0.0);
//        AnchorPane.setTopAnchor(cbpBankCardEvent, 0.0);
//        ancCardEventLayout.getChildren().add(cbpBankCardEvent);
    }

    private List<TblBankEdc> loadAllDataBankEDC() {
        List<TblBankEdc> list = rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getAllDataBankEDC();
        for (TblBankEdc data : list) {
            //data bank
            data.setTblBank(rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getBank(data.getTblBank().getIdbank()));
            //data bank account
            data.setTblBankAccount(rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
        }
        return list;
    }

    private List<TblBankNetworkCard> loadAllDataBankNetworkCard() {
        List<TblBankNetworkCard> list = new ArrayList<>();
        if (rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getTblBankEdc() != null) {
            List<TblBankEdcBankNetworkCard> listEDCNetworkCard = rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getAllDataBankEDCBankNetworkCardByIDEDC(
                    rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getTblBankEdc().getIdedc());
            for (TblBankEdcBankNetworkCard dataEDCNetworkCard : listEDCNetworkCard) {
                //data bank network card
                TblBankNetworkCard data = rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getBankNetworkCard(dataEDCNetworkCard.getTblBankNetworkCard().getIdnetworkCard());
                //add data to list
                list.add(data);
            }
        }
        return list;
    }

//    private List<TblBankEventCard> loadAllDataBankEventCard() {
//        List<TblBankEventCard> list = new ArrayList<>();
//        if (rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBank() != null) {
//            list = rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getAllDataBankEventCard();
//            for (TblBankEventCard data : list) {
//                //data bank
//                data.setTblBank(rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getBank(data.getTblBank().getIdbank()));
//            }
//            //remove invalid data
//            for (int i = list.size() - 1; i > -1; i--) {
//                if ((list.get(i).getTblBank().getIdbank()
//                        != rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBank().getIdbank())
//                        || (list.get(i).getMinTransaction() > totalBill.get())
//                        || (list.get(i).getBeginEventDate().after(Timestamp.valueOf(LocalDateTime.now())))
//                        || (list.get(i).getEndEventDate().before(Timestamp.valueOf(LocalDateTime.now())))) {
//                    list.remove(i);
//                }
//            }
//        }
//        return list;
//    }
    private void refreshDataPopup() {
        //Bank EDC
        ObservableList<TblBankEdc> bankEDCItems = FXCollections.observableArrayList(loadAllDataBankEDC());
        cbpBankEDC.setItems(bankEDCItems);

        //Bank Network Card
        ObservableList<TblBankNetworkCard> bankNetworkCardItems = FXCollections.observableArrayList(loadAllDataBankNetworkCard());
        cbpBankNetworkCard.setItems(bankNetworkCardItems);

        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcrrcTransactionInputController.getParentController().getParentController().getFReservationManager().getAllDataBank());
        cbpBank.setItems(bankItems);

//        //Bank Card Event
//        ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//        cbpBankCardEvent.setItems(bankCardEventItems);
    }

    private final ObjectProperty<BigDecimal> totalBill = new SimpleObjectProperty<>(new BigDecimal("0"));

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        totalBill.set(rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.getUnitNominal()
                .add(rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getPaymentDiscount()));

        Bindings.bindBidirectional(txtTotalBill.textProperty(), totalBill, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTransactionPayment.textProperty(), rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        txtCardNumber.textProperty().bindBidirectional(rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.bankCardNumberProperty());
        txtApprovalCode.textProperty().bindBidirectional(rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.approvalCodeProperty());

        txtDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationDiscountPayment()));

        cbpBankEDC.valueProperty().bindBidirectional(rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.tblBankEdcProperty());
        cbpBankNetworkCard.valueProperty().bindBidirectional(rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.tblBankNetworkCardProperty());
        cbpBank.valueProperty().bindBidirectional(rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.tblBankProperty());
//        cbpBankCardEvent.valueProperty().bindBidirectional(rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankEventCardProperty());

        //if bank card has been selected, use data bank from data bank card
//        if (rcrrcTransactionInputController.getParentController().getParentController().dataInputTransactionStatus.equals("reservation")
//                && rcrrcTransactionInputController.getParentController().getParentController().dataReservationBill.getTblBankCard() != null) {
        if (rcrrcTransactionInputController.getParentController().getParentController().dataReservationBill.getTblBankCard() != null) {
            cbpBank.setValue(rcrrcTransactionInputController.getParentController().getParentController().dataReservationBill.getTblBankCard().getTblBank());
            cbpBank.setDisable(true);
        }
//        if (rcrrcTransactionInputController.getParentController().getParentController().dataInputTransactionStatus.equals("checkout")
//                && rcrrcTransactionInputController.getParentController().getParentController().dataCheckOutBill.getTblBankCard() != null) {
//            cbpBank.setValue(rcrrcTransactionInputController.getParentController().getParentController().dataCheckOutBill.getTblBankCard().getTblBank());
//            cbpBank.setDisable(true);
//        }

        rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.tblBankEdcProperty().addListener((obs, oldVal, newVal) -> {
            //Bank Network Card
            ObservableList<TblBankNetworkCard> bankNetworkCardItems = FXCollections.observableArrayList(loadAllDataBankNetworkCard());
            cbpBankNetworkCard.setItems(bankNetworkCardItems);
            cbpBankNetworkCard.setValue(null);
        });

//        rcrrcTransactionInputController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankProperty().addListener((obs, oldVal, newVal) -> {
//            //Bank Card Event
//            ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//            cbpBankCardEvent.setItems(bankCardEventItems);
//            cbpBankCardEvent.setValue(null);
//        });
        rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.tblBankEventCardProperty().addListener((obs, oldVal, newVal) -> {
            //Discount Payment
            txtDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationDiscountPayment()));
            //Total Transaction Payment
            rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.setUnitNominal(
                    totalBill.get().subtract(calculationDiscountPayment()));
        });

        totalBill.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
//                //Bank Card Event
//                ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//                cbpBankCardEvent.setItems(bankCardEventItems);
//                cbpBankCardEvent.setValue(null);
                //Total Transaction Payment
                rcrrcTransactionInputController.getParentController().tempSelectedDataTransaction.setUnitNominal(
                        newVal.subtract(calculationDiscountPayment()));
            }
        });

        cbpBankEDC.hide();
        cbpBankNetworkCard.hide();
        cbpBank.hide();
//        cbpBankCardEvent.hide();
    }

    private void dataRCTIDebitCardSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcrrcTransactionInputController.getParentController().dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.setPaymentDiscount(calculationDiscountPayment());
                rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.setPaymentCharge(calculationMDRChargePayment());
                rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.setTblBankAccount(
                        rcrrcTransactionInputController.getParentController().tempSelectedDataTransactionWithBankCard.getTblBankEdc().getTblBankAccount());
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
            if (totalBill.get()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (totalBill.get()
                        .compareTo((rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                                .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Tidak dapat lebih besar dari '" + (rcrrcTransactionInputController.getParentController().calculationTotalRestOfBill()
                            .add(rcrrcTransactionInputController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
                }
            }
        }
        if (cbpBankEDC.getValue() == null) {
            dataInput = false;
            errDataInput += "EDC : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBankNetworkCard.getValue() == null) {
            dataInput = false;
            errDataInput += "Jaringan Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBank.getValue() == null) {
            dataInput = false;
            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (chbCardDiscount.isSelected()
//                && cbpBankCardEvent.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Event Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (txtCardNumber.getText() == null || txtCardNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
//            if (txtCardNumber.getText().length() < 16
//                    || txtCardNumber.getText().length() > 20) {
//                dataInput = false;
//                errDataInput += "Card Number : Invalid Data Input \n";
//            }
        }
        if (txtApprovalCode.getText() == null || txtApprovalCode.getText().equals("")) {
            dataInput = false;
            errDataInput += "Approval Code : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (txtApprovalCode.getText().length() != 6) {
                dataInput = false;
                errDataInput += "Approval Code : Data tidak sesuai \n";
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
        initFormDataRCTIDebitCard();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCRRCTIDebitCardController(RCRRCTransactionInputController parentController) {
        rcrrcTransactionInputController = parentController;
    }

    private final RCRRCTransactionInputController rcrrcTransactionInputController;

}
