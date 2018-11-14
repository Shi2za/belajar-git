/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd.rctid;

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
import hotelfx.view.feature_room_check_fd.RCTransactionInputDetailController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class RCTICreditCardController implements Initializable {

    @FXML
    private GridPane gpRCTICreditCard;

    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private JFXCheckBox chbGetRestOfBill;

    @FXML
    private JFXTextField txtTransactionPayment;

    @FXML
    private JFXTextField txtCardNumber;

    private JFXCComboBoxTablePopup<TblBankEdc> cbpBankEDC;

    private JFXCComboBoxTablePopup<TblBankNetworkCard> cbpBankNetworkCard;

    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private JFXTextField txtCardHolder;

    @FXML
    private JFXTextField txtApprovalCode;

//    @FXML
//    private JFXDatePicker dtpExpireDate;
    @FXML
    private Spinner<Integer> spnExpiredMonth;

    @FXML
    private Spinner<Integer> spnExpiredYear;

    @FXML
    private Label lblEprDate;

    @FXML
    private JFXTextField txtDiscountPayment;

    @FXML
    private JFXCheckBox chbCardDiscount;

    @FXML
    private AnchorPane ancCardEventLayout;

//    private final JFXCComboBoxPopup<TblBankEventCard> cbpBankCardEvent = new JFXCComboBoxPopup<>(TblBankEventCard.class);
    @FXML
    private JFXButton btnSave;

    private void initFormDataRCTICreditCard() {
        initDataRCTICreditCardPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataRCTICreditCardSaveHandle();
        });

        //rest of bill
        chbGetRestOfBill.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set rest of bill
                totalBill.set(rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                        .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
            }
        });

        totalBill.addListener((obs, oldVal, newVal) -> {
            if (newVal
                    .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) != 0) {
                chbGetRestOfBill.setSelected(false);
            }
        });

        spnExpiredMonth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12));
        spnExpiredYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99));

        initDateCalendar();

        initImportantFieldColor();

        initNumbericField();

        //discount
//        if(rcTransactionInputDetailController.getParentController().getParentController().dataInputTransactionStatus.equals("reservation")){
        chbCardDiscount.setDisable(!rcTransactionInputDetailController.getParentController().useCardEventAvailable());
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

    private BigDecimal calculationMDRChargePayment() {
        BigDecimal result = new BigDecimal("0");
        TblBankEdcBankNetworkCard edcNetwrokCard = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getDataBankEDCBankNetworkCardByIDEDCAndIDNetworkCardAndIDBankCardTypeAndOnUs(
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getIdedc(),
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankNetworkCard().getIdnetworkCard(),
                1, //Kredit = '1'
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getTblBank().getIdbank()
                == rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBank().getIdbank());
        if (edcNetwrokCard != null
                && (edcNetwrokCard.getNetworkMdr().compareTo(new BigDecimal("0")) == 1)) {
            result = (totalBill.get().subtract(result))
                    .multiply(edcNetwrokCard.getNetworkMdr().divide(new BigDecimal("100")));
        }
        return result;
    }

    private void initDateCalendar() {
//        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
//                dtpExpireDate);
//        ClassFormatter.setDatePickersEnableDate(LocalDate.now(),
//                dtpExpireDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtTotalBill,
                cbpBankEDC,
                cbpBankNetworkCard,
                cbpBank,
                txtCardNumber,
                txtCardHolder,
                txtApprovalCode,
                lblEprDate, spnExpiredMonth, spnExpiredYear);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill,
                txtTransactionPayment);
    }

    private void initDataRCTICreditCardPopup() {
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

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getAllDataBank());

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
        gpRCTICreditCard.add(cbpBankEDC, 0, 3);
        gpRCTICreditCard.add(cbpBankNetworkCard, 0, 4);
        gpRCTICreditCard.add(cbpBank, 1, 4);
//        AnchorPane.setBottomAnchor(cbpBankCardEvent, 0.0);
//        AnchorPane.setLeftAnchor(cbpBankCardEvent, 0.0);
//        AnchorPane.setRightAnchor(cbpBankCardEvent, 0.0);
//        AnchorPane.setTopAnchor(cbpBankCardEvent, 0.0);
//        ancCardEventLayout.getChildren().add(cbpBankCardEvent);
    }

    private List<TblBankEdc> loadAllDataBankEDC() {
        List<TblBankEdc> list = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getAllDataBankEDC();
        for (TblBankEdc data : list) {
            //data bank
            data.setTblBank(rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getBank(data.getTblBank().getIdbank()));
            //data bank account
            data.setTblBankAccount(rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
        }
        return list;
    }

    private List<TblBankNetworkCard> loadAllDataBankNetworkCard() {
        List<TblBankNetworkCard> list = new ArrayList<>();
        if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEdc() != null) {
            List<TblBankEdcBankNetworkCard> listEDCNetworkCard = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getAllDataBankEDCBankNetworkCardByIDEDC(
                    rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getIdedc());
            for (TblBankEdcBankNetworkCard dataEDCNetworkCard : listEDCNetworkCard) {
                //data bank network card
                TblBankNetworkCard data = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getBankNetworkCard(dataEDCNetworkCard.getTblBankNetworkCard().getIdnetworkCard());
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
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getAllDataBank());
        cbpBank.setItems(bankItems);

//        //Bank Card Event
//        ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//        cbpBankCardEvent.setItems(bankCardEventItems);
    }

    private final ObjectProperty<BigDecimal> totalBill = new SimpleObjectProperty<>(new BigDecimal("0"));

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        totalBill.set(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                .add(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getPaymentDiscount()));

        Bindings.bindBidirectional(txtTotalBill.textProperty(), totalBill, new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtTransactionPayment.textProperty(), rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        txtDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationDiscountPayment()));

        txtCardNumber.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.bankCardNumberProperty());
        txtCardHolder.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.bankCardHolderProperty());
        txtApprovalCode.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.approvalCodeProperty());

//        if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getBankCardExprDate() != null) {
//            dtpExpireDate.setValue(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getBankCardExprDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//        } else {
//            dtpExpireDate.setValue(null);
//        }
//        dtpExpireDate.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.setBankCardExprDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//            }
//        });
        if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getBankCardExprDate() != null) {
            spnExpiredMonth.getValueFactory().setValue(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getBankCardExprDate().getMonth() + 1);
            spnExpiredYear.getValueFactory().setValue(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getBankCardExprDate().getYear() + 1900 - 2000);
        } else {
            spnExpiredMonth.getValueFactory().setValue(LocalDate.now().getMonthValue());
            spnExpiredYear.getValueFactory().setValue(LocalDate.now().getYear() - 2000);

        }

        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankEdcProperty().addListener((obs, oldVal, newVal) -> {
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
        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankEventCardProperty().addListener((obs, oldVal, newVal) -> {
            //Discount Payment
            txtDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationDiscountPayment()));
            //Total Transaction Payment
            rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(
                    totalBill.get().subtract(calculationDiscountPayment()));
        });

        totalBill.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
//                //Bank Card Event
//                ObservableList<TblBankEventCard> bankCardEventItems = FXCollections.observableArrayList(loadAllDataBankEventCard());
//                cbpBankCardEvent.setItems(bankCardEventItems);
//                cbpBankCardEvent.setValue(null);
                //Total Transaction Payment
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(
                        newVal.subtract(calculationDiscountPayment()));
            }
        });

        cbpBankEDC.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankEdcProperty());
        cbpBankNetworkCard.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankNetworkCardProperty());
        cbpBank.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankProperty());
//        cbpBankCardEvent.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.tblBankEventCardProperty());

        //if bank card has been selected, use data bank from data bank card
        if (rcTransactionInputDetailController.getParentController().getParentController().dataReservationBill.getTblBankCard() != null) {
            cbpBank.setValue(rcTransactionInputDetailController.getParentController().getParentController().dataReservationBill.getTblBankCard().getTblBank());
            cbpBank.setDisable(true);
        }

        cbpBankEDC.hide();
        cbpBankNetworkCard.hide();
        cbpBank.hide();
//        cbpBankCardEvent.hide();
    }

    private void dataRCTICreditCardSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcTransactionInputDetailController.getParentController().dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.setBankCardExprDate(getExpiredDate(spnExpiredMonth.getValue(), spnExpiredYear.getValue()));
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.setPaymentDiscount(calculationDiscountPayment());
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.setPaymentCharge(calculationMDRChargePayment());
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.setTblBankAccount(
                        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithBankCard.getTblBankEdc().getTblBankAccount());
                //save data transaction & close dialog data rc-transaction detail
                rcTransactionInputDetailController.getParentController().saveDataTransaction();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcTransactionInputDetailController.getParentController().dialogStage);
        }
    }

    private Date getExpiredDate(int month, int year) {
        LocalDate tempDate = LocalDate.of(2000 + year, month, 1);
        tempDate = tempDate.withDayOfMonth(tempDate.lengthOfMonth());
        return Date.valueOf(tempDate);
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
                        .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                                .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Nilai tidak dapat lebih besar dari '" + (rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
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
        if (txtCardHolder.getText() == null || txtCardHolder.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Pemegang Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
//        if (dtpExpireDate.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Tanggal Kadaluarsa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (spnExpiredMonth.getValue() == null) {
            errDataInput += "Bulan Expr. : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spnExpiredYear.getValue() == null) {
            errDataInput += "Tahun Expr. : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataRCTICreditCard();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public RCTICreditCardController(RCTransactionInputDetailController parentController) {
        rcTransactionInputDetailController = parentController;
    }

    private final RCTransactionInputDetailController rcTransactionInputDetailController;
    
}
