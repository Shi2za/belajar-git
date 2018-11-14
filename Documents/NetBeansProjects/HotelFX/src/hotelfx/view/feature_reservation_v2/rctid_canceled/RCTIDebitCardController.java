/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rctid_canceled;

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
import hotelfx.view.feature_reservation_v2.ReservationCanceledInputController;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class RCTIDebitCardController implements Initializable {

    @FXML
    private GridPane gpRCTIDebitCard;

    @FXML
    private JFXTextField txtTotalBill;

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
    private void initFormDataRCTIDebitCard() {
        initDataRCTIDebitCardPopup();

        //set rest of bill
        reservationCanceledInputController.totalBill.addListener((obs, oldVal, newVal) -> {
            reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(newVal);
        });
        reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(reservationCanceledInputController.totalBill.get());

        cbpBankEDC.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setPaymentCharge(calculationMDRChargePayment(
                        newVal,
                        cbpBankNetworkCard.getValue(),
                        newVal.getTblBank(),
                        cbpBank.getValue()
                ));
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setTblBankAccount(
                        newVal.getTblBankAccount());
            } else {
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setPaymentCharge(calculationMDRChargePayment(
                        null,
                        cbpBankNetworkCard.getValue(),
                        null,
                        cbpBank.getValue()
                ));
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setTblBankAccount(null);
            }
        });
        cbpBank.valueProperty().addListener((obs, oldVal, newVal) -> {
            reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setPaymentCharge(calculationMDRChargePayment(
                    reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc(),
                    cbpBankNetworkCard.getValue(),
                    reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc() != null
                            ? reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getTblBank() : null,
                    newVal
            ));
        });
        cbpBankNetworkCard.valueProperty().addListener((obs, oldVal, newVal) -> {
            reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setPaymentCharge(calculationMDRChargePayment(
                    cbpBankEDC.getValue(),
                    newVal,
                    cbpBankEDC.getValue() != null
                            ? cbpBankEDC.getValue().getTblBank() : null,
                    cbpBank.getValue()
            ));
        });

        reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setPaymentDiscount(calculationDiscountPayment());
        reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setPaymentCharge(calculationMDRChargePayment(
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc(),
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankNetworkCard(),
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc() != null
                        ? reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getTblBank() : null,
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBank()
        ));
        reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.setTblBankAccount(
                reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc() != null
                        ? reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getTblBankAccount()
                        : null);

        initImportantFieldColor();

        initNumbericField();

        //discount
//        if(rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus.equals("reservation")){
        chbCardDiscount.setDisable(true);
//        }else{
//            chbCardDiscount.setDisable(true);
//        }
//        chbCardDiscount.setSelected(false);
        ancCardEventLayout.setVisible(false);

//        chbCardDiscount.selectedProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal) {
//                ancCardEventLayout.setVisible(true);
//            } else {
//                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.setTblBankEventCard(null);
//                ancCardEventLayout.setVisible(false);
//            }
//        });
//
//        chbCardDiscount.setSelected(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard() != null);
        chbCardDiscount.setVisible(false);
    }

    private BigDecimal calculationDiscountPayment() {
        BigDecimal result = new BigDecimal("0");
//        if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard() != null) {
//            result = totalBill.get()
//                    .multiply(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard().getDiscountPercentage().divide(new BigDecimal("100")));
//            if (result > rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard().getDiscountNominal()) {
//                result = rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEventCard().getDiscountNominal();
//            }
//        }
        return result;
    }

    private BigDecimal calculationMDRChargePayment(
            TblBankEdc bankEDC,
            TblBankNetworkCard bankNetworkCard,
            TblBank bankFromEDC,
            TblBank bankFromCard) {
        BigDecimal result = new BigDecimal("0");
        if (bankEDC != null
                && bankNetworkCard != null
                && bankFromEDC != null
                && bankFromCard != null) {
            TblBankEdcBankNetworkCard edcNetwrokCard = reservationCanceledInputController.getParentController().getFReservationManager().getDataBankEDCBankNetworkCardByIDEDCAndIDNetworkCardAndIDBankCardTypeAndOnUs(
                    bankEDC.getIdedc(),
                    bankNetworkCard.getIdnetworkCard(),
                    0, //Debit = '0'
                    bankFromEDC.getIdbank() == bankFromCard.getIdbank());
            if (edcNetwrokCard != null
                    && (edcNetwrokCard.getNetworkMdr().compareTo(new BigDecimal("0")) == 1)) {
                result = (totalBill.get().subtract(calculationDiscountPayment()))
                        .multiply(edcNetwrokCard.getNetworkMdr().divide(new BigDecimal("100")));
            }
            System.out.println("mdr : " + result);
        }
        return result;
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
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

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataBank());

        cbpBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableBank, bankItems, "", "Bank *", true, 200, 200
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
        List<TblBankEdc> list = reservationCanceledInputController.getParentController().getFReservationManager().getAllDataBankEDC();
        for (TblBankEdc data : list) {
            //data bank
            data.setTblBank(reservationCanceledInputController.getParentController().getFReservationManager().getBank(data.getTblBank().getIdbank()));
            //data bank account
            data.setTblBankAccount(reservationCanceledInputController.getParentController().getFReservationManager().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
        }
        return list;
    }

    private List<TblBankNetworkCard> loadAllDataBankNetworkCard() {
        List<TblBankNetworkCard> list = new ArrayList<>();
        if (reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc() != null) {
            List<TblBankEdcBankNetworkCard> listEDCNetworkCard = reservationCanceledInputController.getParentController().getFReservationManager().getAllDataBankEDCBankNetworkCardByIDEDC(
                    reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getIdedc());
            for (TblBankEdcBankNetworkCard dataEDCNetworkCard : listEDCNetworkCard) {
                //data bank network card
                TblBankNetworkCard data = reservationCanceledInputController.getParentController().getFReservationManager().getBankNetworkCard(dataEDCNetworkCard.getTblBankNetworkCard().getIdnetworkCard());
                //add data to list
                list.add(data);
            }
        }
        return list;
    }

//    private List<TblBankEventCard> loadAllDataBankEventCard() {
//        List<TblBankEventCard> list = new ArrayList<>();
//        if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBank() != null) {
//            list = rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getAllDataBankEventCard();
//            for (TblBankEventCard data : list) {
//                //data bank
//                data.setTblBank(rcTransactionInputDetailController.getParentController().getParentController().getFReservationManager().getBank(data.getTblBank().getIdbank()));
//            }
//            //remove invalid data
//            for (int i = list.size() - 1; i > -1; i--) {
//                if ((list.get(i).getTblBank().getIdbank()
//                        != rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBank().getIdbank())
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
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataBank());
        cbpBank.setItems(bankItems);

//        //Bank Card Event
//        ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//        cbpBankCardEvent.setItems(bankCardEventItems);
    }

    private final ObjectProperty<BigDecimal> totalBill = new SimpleObjectProperty<>(new BigDecimal("0"));

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        totalBill.bind(reservationCanceledInputController.getParentController().selectedDataTransaction.unitNominalProperty());

        Bindings.bindBidirectional(txtTotalBill.textProperty(), totalBill, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTransactionPayment.textProperty(), reservationCanceledInputController.getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        txtCardNumber.textProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.bankCardNumberProperty());
        txtApprovalCode.textProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.approvalCodeProperty());

        txtDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationDiscountPayment()));

        cbpBankEDC.valueProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.tblBankEdcProperty());
        cbpBankNetworkCard.valueProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.tblBankNetworkCardProperty());
        cbpBank.valueProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.tblBankProperty());
//        cbpBankCardEvent.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankEventCardProperty());

        //if bank card has been selected, use data bank from data bank card
        if (reservationCanceledInputController.getParentController().dataReservationBill.getTblBankCard() != null) {
            cbpBank.setValue(reservationCanceledInputController.getParentController().dataReservationBill.getTblBankCard().getTblBank());
            cbpBank.setDisable(true);
        }

        reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.tblBankEdcProperty().addListener((obs, oldVal, newVal) -> {
            //Bank Network Card
            ObservableList<TblBankNetworkCard> bankNetworkCardItems = FXCollections.observableArrayList(loadAllDataBankNetworkCard());
            cbpBankNetworkCard.setItems(bankNetworkCardItems);
            cbpBankNetworkCard.setValue(null);
        });

//        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankProperty().addListener((obs, oldVal, newVal) -> {
//            //Bank Card Event
//            ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//            cbpBankCardEvent.setItems(bankCardEventItems);
//            cbpBankCardEvent.setValue(null);
//        });
        reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.tblBankEventCardProperty().addListener((obs, oldVal, newVal) -> {
            //Discount Payment
            txtDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationDiscountPayment()));
            //Total Transaction Payment
            reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(
                    totalBill.get().subtract(calculationDiscountPayment()));
        });

        totalBill.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
//                //Bank Card Event
//                ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//                cbpBankCardEvent.setItems(bankCardEventItems);
//                cbpBankCardEvent.setValue(null);
                //Total Transaction Payment
                reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(
                        newVal.subtract(calculationDiscountPayment()));
            }
        });

        cbpBankEDC.hide();
        cbpBankNetworkCard.hide();
        cbpBank.hide();
//        cbpBankCardEvent.hide();
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

    public RCTIDebitCardController(ReservationCanceledInputController parentController) {
        reservationCanceledInputController = parentController;
    }

    private final ReservationCanceledInputController reservationCanceledInputController;

}
