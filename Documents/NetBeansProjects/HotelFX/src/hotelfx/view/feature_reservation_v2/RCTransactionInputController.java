/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassSetting;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithReservationVoucher;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationVoucher;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCTransactionInputController implements Initializable {

    @FXML
    private AnchorPane ancFormRCTransaction;

    @FXML
    private GridPane gpFormDataGuestBill;

    @FXML
    private JFXTextField txtBill;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private JFXCheckBox chbHotelDiscount;

    @FXML
    private AnchorPane ancHotelEventLayout;

//    private final JFXCComboBoxPopup<TblHotelEvent> cbpHotelEvent = new JFXCComboBoxPopup<>(TblHotelEvent.class);
    @FXML
    private JFXTextField txtTotalServiceCharge;

    @FXML
    private Label lblServiceChargePercentage;

    @FXML
    private JFXTextField txtTotalTax;

    @FXML
    private Label lblTaxPercentage;

    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private Label lblRoundingValue;

    @FXML
    private JFXTextField txtTotalPayment;

    @FXML
    private JFXTextField txtTotalDiscountPayment;

    @FXML
    private JFXTextField txtTotalRestOfBill;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private AnchorPane ancTableDataTransaction;

//    public BigDecimal discountPercentage;
    private void initFormDataRCTransaction() {

//        discountPercentage = new BigDecimal("0");
        btnSave.setTooltip(new Tooltip("Simpan (Data Pembayaran)"));
        btnSave.setOnAction((e) -> {
            dataRCTTransactionSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRCTransactionCancelHandle();
        });

        initDataRCTransactionPopup();

//        //discount
//        chbHotelDiscount.setSelected(false);
//        chbHotelDiscount.selectedProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal) {
//                ancHotelEventLayout.setVisible(true);
//            } else {
//                ancHotelEventLayout.setVisible(false);
//            }
//        });
        ancHotelEventLayout.setVisible(false);

//        chbHotelDiscount.selectedProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal) {
//                if (reservationController.dataInputTransactionStatus.equals("reservation")) {
//                    if (reservationController.selectedDataReservationBillHotelEvent.getTblHotelEvent() != null) {
//                        discountPercentage = reservationController.selectedDataReservationBillHotelEvent.getTblHotelEvent().getDiscountPercentage();
//                    } else {
//                        discountPercentage = new BigDecimal("0");
//                    }
//                } else {  //checkout
//                    if (reservationController.selectedDataCheckOutBillHotelEvent.getTblHotelEvent() != null) {
//                        discountPercentage = reservationController.selectedDataCheckOutBillHotelEvent.getTblHotelEvent().getDiscountPercentage();
//                    } else {
//                        discountPercentage = new BigDecimal("0");
//                    }
//                }
//            } else {
//                discountPercentage = new BigDecimal("0");
//            }
//            refreshDataBill();
//        });
//        if (reservationController.dataInputTransactionStatus.equals("reservation")) {
//        chbHotelDiscount.setDisable((!useHotelEventAvaliable()));
//        } else {
//            chbHotelDiscount.setDisable(true);
//        }
        chbHotelDiscount.setVisible(false);

    }

    private void initDataRCTransactionPopup() {
//        //Hotel Event
//        TableView<TblHotelEvent> tableHotelEvent = new TableView<>();
//
//        TableColumn<TblHotelEvent, String> codeHotelEvent = new TableColumn<>("ID");
//        codeHotelEvent.setCellValueFactory(cellData -> cellData.getValue().codeEventProperty());
//        codeHotelEvent.setMinWidth(120);
//
//        TableColumn<TblHotelEvent, String> hotelEventName = new TableColumn<>("Event");
//        hotelEventName.setCellValueFactory(cellData -> cellData.getValue().eventNameProperty());
//        hotelEventName.setMinWidth(140);
//
//        TableColumn<TblHotelEvent, String> hotelEventDiscountPercentage = new TableColumn("Diskon(%)");
//        hotelEventDiscountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDiscountPercentage()), param.getValue().discountPercentageProperty()));
//        hotelEventDiscountPercentage.setMinWidth(140);
//
//        TableColumn<TblHotelEvent, String> hotelEventMinTransaction = new TableColumn("Minimal Transaksi");
//        hotelEventMinTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelEvent, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getMinTransaction()), param.getValue().minTransactionProperty()));
//        hotelEventMinTransaction.setMinWidth(180);
//
//        tableHotelEvent.getColumns().addAll(codeHotelEvent, hotelEventName, hotelEventDiscountPercentage, hotelEventMinTransaction);
//
//        ObservableList<TblHotelEvent> hotelEventItems = FXCollections.observableArrayList(loadAllDataHotelEvent());
//
//        setFunctionPopup(cbpHotelEvent, tableHotelEvent, hotelEventItems, "eventName", "Event *");
//
//        //attached to grid-pane
//        AnchorPane.setBottomAnchor(cbpHotelEvent, 0.0);
//        AnchorPane.setBottomAnchor(cbpHotelEvent, 0.0);
//        AnchorPane.setBottomAnchor(cbpHotelEvent, 0.0);
//        AnchorPane.setBottomAnchor(cbpHotelEvent, 0.0);
//        ancHotelEventLayout.getChildren().add(cbpHotelEvent);
    }

//    private List<TblHotelEvent> loadAllDataHotelEvent() {
//        List<TblHotelEvent> list = reservationController.getFReservationManager().getAllDataHotelEvent();
//        //remove invalid data
//        for (int i = list.size() - 1; i > -1; i--) {
//            if ((list.get(i).getMinTransaction() > reservationController.calculationTotalBill(reservationController.dataInputTransactionStatus, 0))
//                    || (list.get(i).getBeginEventDate().after(Timestamp.valueOf(LocalDateTime.now())))
//                    || (list.get(i).getEndEventDate().before(Timestamp.valueOf(LocalDateTime.now())))
//                    || (list.get(i).getDayOfWeek() != 0
//                    && list.get(i).getDayOfWeek() != LocalDate.now().getDayOfWeek().getValue())) {
//                list.remove(i);
//            }
//        }
//        return list;
//    }
//    private void refreshDataPopup() {
//        //Hotel Event
//        ObservableList<TblHotelEvent> hotelEventItems = FXCollections.observableArrayList(loadAllDataHotelEvent());
//        cbpHotelEvent.setItems(hotelEventItems);
//    }
//
//    private void setFunctionPopup(JFXCComboBoxPopup cbp,
//            TableView table,
//            ObservableList items,
//            String nameFiltered,
//            String promptText) {
//        table.getSelectionModel().selectedIndexProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    if (newValue.intValue() != -1) {
//                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
//                    }
//                    cbp.hide();
//                });
//
//        cbp.setPropertyNameForFiltered(nameFiltered);
//
//        cbp.setItems(items);
//    
//    cbp.setOnShowing((e) -> {
//            table.getSelectionModel().clearSelection();
//        });
//
//        // Add observable list data to the table
//        table.itemsProperty().bind(cbp.filteredItemsProperty());
//
//        //popup button
//        JFXButton button = new JFXButton("▾");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(600, 300);
//
//        content.setCenter(table);
//
//        cbp.setPopupEditor(true);
//        cbp.promptTextProperty().set(promptText);
//        cbp.setLabelFloat(true);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
    private void setSelectedDataToInputForm() {

//        refreshDataPopup();
        refreshDataBill();

        lblServiceChargePercentage.setText("(" + ClassFormatter.decimalFormat.cFormat(reservationController.dataReservationBill.getServiceChargePercentage().multiply(new BigDecimal("100"))) + " %)");
        lblTaxPercentage.setText("(" + ClassFormatter.decimalFormat.cFormat(reservationController.dataReservationBill.getTaxPercentage().multiply(new BigDecimal("100"))) + " %)");
//            cbpHotelEvent.valueProperty().bindBidirectional(reservationController.selectedDataReservationBillHotelEvent.tblHotelEventProperty());
//            reservationController.selectedDataReservationBillHotelEvent.tblHotelEventProperty().addListener((obs, oldVal, newVal) -> {
//                if (newVal != null) {
//                    discountPercentage = newVal.getDiscountPercentage();
//                }
//                refreshDataBill();
//            });

//        cbpHotelEvent.hide();
        //data rc-transaction input detail
        initTableDataRCTransactionInputDetail();

    }

    private void refreshDataBill() {
        txtBill.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalReservationRoom()
                .subtract(reservationController.calculationTotalReservationRoomCompliment())
                .add(reservationController.calculationTotalAdditional())
                .add(reservationController.calculationTotalBroken())));
        txtDiscount.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalDiscount()));
        txtTotalServiceCharge.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalServiceCharge()));
        txtTotalTax.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalTax()));
        txtTotalBill.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalBillAfterRounding()));
        lblRoundingValue.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalReservationTransactionRoundingValue()) + ")");
        txtTotalPayment.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalReservationTransaction()));
        txtTotalDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalReservationTransactionDisountPayment()));
        txtTotalRestOfBill.setText(ClassFormatter.currencyFormat.cFormat(reservationController.calculationTotalRestOfBill()));
    }

//    private void saveDataDiscount() {
//        if (reservationController.dataInputTransactionStatus.equals("reservation")) {
//            if (chbHotelDiscount.isSelected()) {
//                reservationController.dataReservationBill.setRefReservationBillDiscountType(reservationController.getFReservationManager().getReservationBillDiscountType(0));
//                reservationController.dataReservationBill.setDiscountPercentage(reservationController.selectedDataReservationBillHotelEvent.getTblHotelEvent().getDiscountPercentage());
//                reservationController.dataReservationBillHotelEvent = reservationController.selectedDataReservationBillHotelEvent;
//            }
//        } else {  //checkout
//            if (chbHotelDiscount.isSelected()) {
//                reservationController.dataCheckOutBill.setRefReservationBillDiscountType(reservationController.getFReservationManager().getReservationBillDiscountType(0));
//                reservationController.dataCheckOutBill.setDiscountPercentage(reservationController.selectedDataCheckOutBillHotelEvent.getTblHotelEvent().getDiscountPercentage());
//                reservationController.dataCheckOutBillHotelEvent = reservationController.selectedDataCheckOutBillHotelEvent;
//            }
//        }
//    }
    public void saveDataTransaction() {
        //data rc-transaction
        reservationController.selectedDataTransaction.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
        reservationController.selectedDataTransaction.setTblEmployeeByIdcashier(ClassSession.currentUser.getTblEmployeeByIdemployee());
        reservationController.selectedDataTransaction.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));    //Reservation = '0'
        if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
            reservationController.dataReservationPayments.add(reservationController.selectedDataTransaction);
        } else {  //update
            for (int i = 0; i < reservationController.dataReservationPayments.size(); i++) {
                if (reservationController.dataReservationPayments.get(i).equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    reservationController.dataReservationPayments.set(i, reservationController.selectedDataTransaction);
                    break;
                }
            }
        }
//            reservationController.tableDataReservationTransaction.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationPayments));
        //data rc-transaction detail
        saveDataTransactionDetail();
        //reset data rc-transaction input
        reservationController.resetDataTransaction();
//        if (reservationController.dataInputTransactionStatus.equals("reservation")) {
        chbHotelDiscount.setDisable((!useHotelEventAvaliable()));
//        } else {
//            chbHotelDiscount.setDisable(true);
//        }
        refreshDataBill();
        tableDataRCTransactionInputDetail.getTableView().setItems(reservationController.loadAllDataReservationTransaction());
        //close dialog data rc-transaction input detail
        dialogStage.close();
    }

    public boolean useHotelEventAvaliable() {
        boolean available = true;
        if (!ClassSetting.isMultipleDiscountAllowed) {
            for (TblReservationPaymentWithBankCard dataPaymentWithBankCard : reservationController.dataReservationPaymentWithBankCards) {
                if (dataPaymentWithBankCard.getTblBankEventCard() != null) {
                    if (dataPaymentWithBankCard.getTblReservationPayment().getRefReservationBillType().getIdtype() == 0) {    //Reservasi = '0'
                        available = false;
                        break;
                    }
                }
            }
        }
        return available;
    }

    public boolean useCardEventAvailable() {
        boolean available = true;
        if (!ClassSetting.isMultipleDiscountAllowed) {
            available = !chbHotelDiscount.isSelected();
            if (available) {
                for (TblReservationPaymentWithBankCard dataPaymentWithBankCard : reservationController.dataReservationPaymentWithBankCards) {
                    if (dataPaymentWithBankCard.getTblBankEventCard() != null) {
                        if (dataPaymentWithBankCard.getTblReservationPayment().getRefReservationBillType().getIdtype() == 0) {    //Reservasi = '0'
                            if (dataInputRCTransactionInputDetailStatus == 0) {   //insert = '0'
                                available = false;
                                break;
                            } else {
                                if (!dataPaymentWithBankCard.getTblReservationPayment().equals((TblReservationPayment) tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                                    available = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return available;
    }

    private void saveDataTransactionDetail() {
        switch (reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.dataReservationPaymentWithTransfers.add(reservationController.selectedDataTransactionWithTransfer);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithTransfers.size(); i++) {
                        if (reservationController.dataReservationPaymentWithTransfers.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.dataReservationPaymentWithTransfers.set(i, reservationController.selectedDataTransactionWithTransfer);
                            break;
                        }
                    }
                }
                break;
            case 2:    //Debit
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.dataReservationPaymentWithBankCards.add(reservationController.selectedDataTransactionWithBankCard);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithBankCards.size(); i++) {
                        if (reservationController.dataReservationPaymentWithBankCards.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.dataReservationPaymentWithBankCards.set(i, reservationController.selectedDataTransactionWithBankCard);
                            break;
                        }
                    }
                }
                break;
            case 3:    //Credit
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.dataReservationPaymentWithBankCards.add(reservationController.selectedDataTransactionWithBankCard);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithBankCards.size(); i++) {
                        if (reservationController.dataReservationPaymentWithBankCards.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.dataReservationPaymentWithBankCards.set(i, reservationController.selectedDataTransactionWithBankCard);
                            break;
                        }
                    }
                }
                break;
            case 4:    //Cek
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.dataReservationPaymentWithCekGiros.add(reservationController.selectedDataTransactionWithCekGiro);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithCekGiros.size(); i++) {
                        if (reservationController.dataReservationPaymentWithCekGiros.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.dataReservationPaymentWithCekGiros.set(i, reservationController.selectedDataTransactionWithCekGiro);
                            break;
                        }
                    }
                }
                break;
            case 5:    //Giro
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.dataReservationPaymentWithCekGiros.add(reservationController.selectedDataTransactionWithCekGiro);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithCekGiros.size(); i++) {
                        if (reservationController.dataReservationPaymentWithCekGiros.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.dataReservationPaymentWithCekGiros.set(i, reservationController.selectedDataTransactionWithCekGiro);
                            break;
                        }
                    }
                }
                break;
            case 6:    //Travel Agent
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(reservationController.selectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationController.dataReservationPaymentWithGuaranteePayments.add(reservationController.selectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithGuaranteePayments.size(); i++) {
                        if (reservationController.dataReservationPaymentWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationController.dataReservationPaymentWithGuaranteePayments.get(i),
                                    reservationController.selectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationController.dataReservationPaymentWithGuaranteePayments.set(i, reservationController.selectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporatte)
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(reservationController.selectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationController.dataReservationPaymentWithGuaranteePayments.add(reservationController.selectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithGuaranteePayments.size(); i++) {
                        if (reservationController.dataReservationPaymentWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationController.dataReservationPaymentWithGuaranteePayments.get(i),
                                    reservationController.selectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationController.dataReservationPaymentWithGuaranteePayments.set(i, reservationController.selectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(reservationController.selectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationController.dataReservationPaymentWithGuaranteePayments.add(reservationController.selectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithGuaranteePayments.size(); i++) {
                        if (reservationController.dataReservationPaymentWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationController.dataReservationPaymentWithGuaranteePayments.get(i),
                                    reservationController.selectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationController.dataReservationPaymentWithGuaranteePayments.set(i, reservationController.selectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 9:    //Draw Dposit
                break;
            case 10:   //Voucher
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.dataReservationPaymentWithReservationVouchers.add(reservationController.selectedDataTransactionWithReservationVoucher);
                } else {  //update
                    for (int i = 0; i < reservationController.dataReservationPaymentWithReservationVouchers.size(); i++) {
                        if (reservationController.dataReservationPaymentWithReservationVouchers.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.dataReservationPaymentWithReservationVouchers.set(i, reservationController.selectedDataTransactionWithReservationVoucher);
                            break;
                        }
                    }
                }
                break;
            case 13:   //Open Deposit (Cash)
                break;
            case 14:   //Close Deposit (Cash)
                break;
            case 16:   //Canceling Fee
                break;
            default:
                break;
        }
    }

    private void saveGuaranteeLetterItemDetail(TblReservationPaymentWithGuaranteePayment dataGL) {
        //data room type detail
        for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            //data guarantee letter - item detail
            TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
            dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
            dataGLID.setCodeRtd(dataRRTDRPD.getTblReservationRoomTypeDetail().getCodeDetail());
            dataGLID.setDetailName(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName());
            dataGLID.setRoomName(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    ? dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
                            ? dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : null : null);
            dataGLID.setDetailDate(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
//                dataGLID.setDetailCost((long) reservationController.calculationRoomTypeDetailCost(dataRRTD));
            dataGLID.setDetailCost(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
            dataGLID.setDetailQuantity(new BigDecimal("1"));
//                dataGLID.setTotalDiscountNominal((long) reservationController.calculationRoomTypeDetailDiscount(dataRRTD));
            dataGLID.setTotalDiscountNominal(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice()
                    .multiply(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
            dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
            dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
            dataGLID.setDetailType("Room");
            //add data to list
            reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
        }
        //data additional item
        for (TblReservationAdditionalItem dataAdditionalItem : reservationController.dataReservationAdditionalItems) {
            if (dataAdditionalItem.getRefReservationBillType().getIdtype() == 0    //Reservation = '0'
                    || dataAdditionalItem.getRefReservationBillType().getIdtype() == 1) {    //CheckOut = '1' (impossible case)
                //data guarantee letter - item detail
                TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
                dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
                dataGLID.setCodeRtd(dataAdditionalItem.getTblReservationRoomTypeDetail().getCodeDetail());
                dataGLID.setDetailName(dataAdditionalItem.getTblItem().getItemName());
                dataGLID.setRoomName(dataAdditionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                        ? dataAdditionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
                                ? dataAdditionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                                : null : null);
                dataGLID.setDetailDate(dataAdditionalItem.getAdditionalDate());
                dataGLID.setDetailCost(dataAdditionalItem.getItemCharge());
                dataGLID.setDetailQuantity(dataAdditionalItem.getItemQuantity());
                dataGLID.setTotalDiscountNominal((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity()))
                        .multiply(dataAdditionalItem.getDiscountPercentage().divide(new BigDecimal("100"))));
                dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
                dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
                dataGLID.setDetailType("Item");
                //add data to list
                reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
            }
        }
        //data additional service
        for (TblReservationAdditionalService dataAdditionalService : reservationController.dataReservationAdditionalServices) {
            if (dataAdditionalService.getRefReservationBillType().getIdtype() == 0    //Reservation = '0'
                    || dataAdditionalService.getRefReservationBillType().getIdtype() == 1) {    //CheckOut = '1' (impossible case)
                //data guarantee letter - item detail
                TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
                dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
                dataGLID.setCodeRtd(dataAdditionalService.getTblReservationRoomTypeDetail().getCodeDetail());
                dataGLID.setDetailName(dataAdditionalService.getTblRoomService().getServiceName());
                dataGLID.setRoomName(dataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                        ? dataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
                                ? dataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                                : null : null);
                dataGLID.setDetailDate(dataAdditionalService.getAdditionalDate());
                dataGLID.setDetailCost(dataAdditionalService.getPrice());
                dataGLID.setDetailQuantity(new BigDecimal("1"));
                dataGLID.setTotalDiscountNominal((dataAdditionalService.getPrice().multiply(new BigDecimal("1")))
                        .multiply(dataAdditionalService.getDiscountPercentage().divide(new BigDecimal("100"))));
                if (dataAdditionalService.getTblRoomService().getIdroomService() == 6 //Room Dining = 6
                        || dataAdditionalService.getTblRoomService().getIdroomService() == 7 //Dine in Resto = 7
                        ) {   //Resto Bill (impossible case)
                    dataGLID.setServiceChargePercentage(new BigDecimal("0"));
                    dataGLID.setTaxPercentage(new BigDecimal("0"));
                } else {
                    dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
                    dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
                }
                dataGLID.setDetailType("Service");
                //add data to list
                reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
            }
        }
        //data broken item
        for (TblReservationBrokenItem dataRBI : reservationController.dataReservationBrokenItems) {
            //data guarantee letter - item detail
            TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
            dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
            dataGLID.setCodeRtd(dataRBI.getTblReservationRoomTypeDetail().getCodeDetail());
            dataGLID.setDetailName(dataRBI.getTblItem().getItemName());
            dataGLID.setRoomName(dataRBI.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    ? dataRBI.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
                            ? dataRBI.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : null : null);
            dataGLID.setDetailDate(dataRBI.getCreateDate());
            dataGLID.setDetailCost(dataRBI.getItemCharge());
            dataGLID.setDetailQuantity(dataRBI.getItemQuantity());
            dataGLID.setTotalDiscountNominal(new BigDecimal("0"));
            dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
            dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
            dataGLID.setDetailType("Broken-Item");
            //add data to list
            reservationController.dataGuaranteeLetterItemDetails.add(dataGLID);
        }
    }

    private void saveGuaranteeLetterItemDetail(TblReservationPaymentWithGuaranteePayment oldDataGL,
            TblReservationPaymentWithGuaranteePayment newDataGL) {
        //remove data
        for (int i = reservationController.dataGuaranteeLetterItemDetails.size() - 1; i > -1; i--) {
            if (reservationController.dataGuaranteeLetterItemDetails.get(i).getTblReservationPaymentWithGuaranteePayment().equals(oldDataGL)) {
                reservationController.dataGuaranteeLetterItemDetails.remove(i);
            }
        }
        //add data
        saveGuaranteeLetterItemDetail(newDataGL);
    }

    public void dataRCTTransactionSaveHandle() {
        if (reservationController.dataReservationPayments.isEmpty()) {
            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA GAGAL DISIMPAN", "Tidak ada data pembayaran yang harus disimpan..!", "", reservationController.dialogStage);
        } else {
            if (reservationController.isFirstPayment //first payment
                    && reservationController.calculationTotalRestOfBill().compareTo(new BigDecimal("0")) != 0) {    //full payment
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA GAGAL DISIMPAN", "Sisa tagihan lebih besar dari '0', silahkan buat data transaksi pembayaran lainnya..!", "", reservationController.dialogStage);
            } else {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
                if (alert.getResult() == ButtonType.OK) {
                    //update data bill in reservation (view)
                    reservationController.refreshDataBill();
                    //update data reservation status
                    if (reservationController.isFirstPayment) {  //first payment
                        reservationController.selectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
                    }
                    //save data to database
                    if (reservationController.dataReservationSaveHandle(2)) {
                        //set selected current tab (bill & payment : 1)
                        reservationController.tabPaneFormLayout.getSelectionModel().select(1);
                        //close form data reservation payment
                        reservationController.dialogStage.close();
                    }
                }
//                ButtonType btnJustSaveData = new ButtonType("Simpan");
//                ButtonType btnSaveAndPrintReceiptData = new ButtonType("Simpan & Cetak");
//                ButtonType btnCancelData = new ButtonType("Batal", ButtonData.CANCEL_CLOSE);
//                Alert alert = HotelFX.showAlertChooseButton(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menyimpan data ini?", "", reservationController.dialogStage,
//                        btnJustSaveData, 
//                        btnSaveAndPrintReceiptData, 
//                        btnCancelData);
//                if (alert.getResult() == btnJustSaveData) {
//                    //update data bill in reservation (view)
//                    reservationController.refreshDataBill(reservationController.dataInputTransactionStatus);
//                    //update data reservation status
//                    if (reservationController.dataInputTransactionStatus.equals("reservation")) {
//                        reservationController.selectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
//                    } else {  //checkout
//                        reservationController.selectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(5));    //Checkout = '5'
//                    }
//                    //save data to database
//                    if(reservationController.dataReservationSaveHandle(2)){
//                        //close form data reservation payment
//                        reservationController.dialogStage.close();
//                    }
//                } else if (alert.getResult() == btnSaveAndPrintReceiptData) {
//                    //update data bill in reservation (view)
//                    reservationController.refreshDataBill(reservationController.dataInputTransactionStatus);
//                    //update data reservation status
//                    if (reservationController.dataInputTransactionStatus.equals("reservation")) {
//                        reservationController.selectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
//                    } else {  //checkout
//                        reservationController.selectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(5));    //Checkout = '5'
//                    }
//                    //save data to database
//                    if(reservationController.dataReservationSaveHandle(2)){
//                        //print data
//                        if (reservationController.dataInputTransactionStatus.equals("reservation")) {   //reservation
//                            reservationController.dataReservationTransactionPrintHandle();
//                        } else {  //checkout
//                            reservationController.dataCheckOutTransactionPrintHandle();
//                        }
//                        //close form data reservation payment
//                        reservationController.dialogStage.close();
//                    }
//                } else {
//
//                }
//                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
//                if (alert.getResult() == ButtonType.OK) {
//                //save data discount
//                saveDataDiscount();
                //update data bill in reservation (view)
//                    reservationController.refreshDataBill(reservationController.dataInputTransactionStatus);
//                    //update data reservation status
//                    if (reservationController.dataInputTransactionStatus.equals("reservation")) {
//                        reservationController.selectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
//                    } else {  //checkout
//                        reservationController.selectedData.setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(5));    //Checkout = '5'
//                    }
//                    //save data to database
//                    reservationController.dataReservationSaveHandle(2);
//                    //close form data reservation payment
//                    reservationController.dialogStage.close();
//                }
            }
        }
    }

    private void dataRCTransactionCancelHandle() {
        //refresh data selected reservation
        reservationController.refreshDataSelectedReservation();
        //set selected current tab (bill & payment : 1)
        reservationController.tabPaneFormLayout.getSelectionModel().select(1);
        //close form data rc-transaction input
        reservationController.dialogStage.close();
    }

    /**
     * TABLE DATA RC-TRANSACTION INPUT DETAIL
     */
    @FXML
    private AnchorPane tableDataRCTransactionInputDetailLayout;

    public ClassTableWithControl tableDataRCTransactionInputDetail;

    private void initTableDataRCTransactionInputDetail() {
        //set table
        setTableDataRCTransactionInputDetail();
        //set control
        setTableControlDataRCTransactionInputDetail();
        //set table-control to content-view
        tableDataRCTransactionInputDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRCTransactionInputDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataRCTransactionInputDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataRCTransactionInputDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataRCTransactionInputDetail, 0.0);
        tableDataRCTransactionInputDetailLayout.getChildren().add(tableDataRCTransactionInputDetail);
    }

    private void setTableDataRCTransactionInputDetail() {
        TableView<TblReservationPayment> tableView = new TableView();

        TableColumn<TblReservationPayment, String> paymentDate = new TableColumn("Tanggal");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getPaymentDate()), param.getValue().paymentDateProperty()));
        paymentDate.setMinWidth(140);

        TableColumn<TblReservationPayment, String> transactionNominal = new TableColumn("Nominal");
        transactionNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getUnitNominal()), param.getValue().unitNominalProperty()));
        transactionNominal.setMinWidth(140);

        TableColumn<TblReservationPayment, String> paymentTypeName = new TableColumn("Tipe Pembayaran");
        paymentTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType().getTypeName(), param.getValue().refFinanceTransactionPaymentTypeProperty()));
        paymentTypeName.setMinWidth(140);

        TableColumn<TblReservationPayment, String> statusDataPayment = new TableColumn("Status");
        statusDataPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getIdpayment() == 0L
                                ? "-" : "X",
                        param.getValue().idpaymentProperty()));
        statusDataPayment.setMinWidth(80);

        tableView.getColumns().addAll(paymentDate, transactionNominal, paymentTypeName, statusDataPayment);
        tableView.setItems(reservationController.loadAllDataReservationTransaction());
        tableDataRCTransactionInputDetail = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataRCTransactionInputDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRCTransactionInputDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRCTransactionInputDetailUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataRCTransactionInputDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataRCTransactionInputDetail.addButtonControl(buttonControls);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputRCTransactionInputDetailStatus = 0;

    public Stage dialogStage;

    public void dataRCTransactionInputDetailCreateHandle() {
//        if (chbHotelDiscount.isSelected()
//                && cbpHotelEvent.getValue() == null) {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan pilih data diskon Hotel terlebih dahulu..!", null, reservationController.dialogStage);
//        } else {
        dataInputRCTransactionInputDetailStatus = 0;
        //reset data selected rc-transaction
        reservationController.resetDataTransaction();
        //open form data customer bank account
        showRCTransactionInputDetailDialog();
//        }
    }

    public void dataRCTransactionInputDetailUpdateHandle() {
//        if (chbHotelDiscount.isSelected()
//                && cbpHotelEvent.getValue() == null) {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan pilih data diskon Hotel terlebih dahulu..!", null, reservationController.dialogStage);
//        } else {
        if (tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputRCTransactionInputDetailStatus = 1;
            //reset data selected rc-transaction
            reservationController.resetDataTransaction();
            for (TblReservationPayment data : reservationController.dataReservationPayments) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    //set data selected rc-transaction
                    reservationController.selectedDataTransaction = new TblReservationPayment(data);
                    reservationController.selectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(reservationController.selectedDataTransaction.getRefFinanceTransactionPaymentType()));
                    //set data selected rc-transaction detail
                    setRCTransactionDetailData(data);
                    break;
                }
            }
            if (reservationController.selectedDataTransaction.getIdpayment() == 0L) {
                //open form data transaction input detail
                showRCTransactionInputDetailDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIUBAH", "Data yang sudah lewat tidak dapat diubah..!", "", reservationController.dialogStage);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, reservationController.dialogStage);
        }
//        }
    }

    private void setRCTransactionDetailData(TblReservationPayment realRCTransactionPayment) {
        switch (realRCTransactionPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                for (TblReservationPaymentWithTransfer data : reservationController.dataReservationPaymentWithTransfers) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer(data);
                        reservationController.selectedDataTransactionWithTransfer.setTblReservationPayment(reservationController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 2:    //Debit
                for (TblReservationPaymentWithBankCard data : reservationController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard(data);
                        reservationController.selectedDataTransactionWithBankCard.setTblReservationPayment(reservationController.selectedDataTransaction);
                        reservationController.selectedDataTransactionWithBankCard.setTblBank(new TblBank(reservationController.selectedDataTransactionWithBankCard.getTblBank()));
                        reservationController.selectedDataTransactionWithBankCard.setTblBankEdc(new TblBankEdc(reservationController.selectedDataTransactionWithBankCard.getTblBankEdc()));
                        reservationController.selectedDataTransactionWithBankCard.setTblBankNetworkCard(new TblBankNetworkCard(reservationController.selectedDataTransactionWithBankCard.getTblBankNetworkCard()));
                        reservationController.selectedDataTransactionWithBankCard.setTblBankEventCard(reservationController.selectedDataTransactionWithBankCard.getTblBankEventCard() != null
                                ? new TblBankEventCard(reservationController.selectedDataTransactionWithBankCard.getTblBankEventCard()) : null);
                        reservationController.selectedDataTransactionWithBankCard.setTblBankAccount(new TblBankAccount(reservationController.selectedDataTransactionWithBankCard.getTblBankAccount()));
                        reservationController.selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                        reservationController.selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                        break;
                    }
                }
                break;
            case 3:    //Credit
                for (TblReservationPaymentWithBankCard data : reservationController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard(data);
                        reservationController.selectedDataTransactionWithBankCard.setTblReservationPayment(reservationController.selectedDataTransaction);
                        reservationController.selectedDataTransactionWithBankCard.setTblBank(new TblBank(reservationController.selectedDataTransactionWithBankCard.getTblBank()));
                        reservationController.selectedDataTransactionWithBankCard.setTblBankEdc(new TblBankEdc(reservationController.selectedDataTransactionWithBankCard.getTblBankEdc()));
                        reservationController.selectedDataTransactionWithBankCard.setTblBankNetworkCard(new TblBankNetworkCard(reservationController.selectedDataTransactionWithBankCard.getTblBankNetworkCard()));
                        reservationController.selectedDataTransactionWithBankCard.setTblBankEventCard(reservationController.selectedDataTransactionWithBankCard.getTblBankEventCard() != null
                                ? new TblBankEventCard(reservationController.selectedDataTransactionWithBankCard.getTblBankEventCard()) : null);
                        reservationController.selectedDataTransactionWithBankCard.setTblBankAccount(new TblBankAccount(reservationController.selectedDataTransactionWithBankCard.getTblBankAccount()));
                        reservationController.selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                        reservationController.selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                        break;
                    }
                }
                break;
            case 4:    //Cek
                for (TblReservationPaymentWithCekGiro data : reservationController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro(data);
                        reservationController.selectedDataTransactionWithCekGiro.setTblReservationPayment(reservationController.selectedDataTransaction);
                        reservationController.selectedDataTransactionWithCekGiro.setTblBank(new TblBank(reservationController.selectedDataTransactionWithCekGiro.getTblBank()));
                        reservationController.selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(reservationController.selectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                        break;
                    }
                }
                break;
            case 5:    //Giro
                for (TblReservationPaymentWithCekGiro data : reservationController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro(data);
                        reservationController.selectedDataTransactionWithCekGiro.setTblReservationPayment(reservationController.selectedDataTransaction);
                        reservationController.selectedDataTransactionWithCekGiro.setTblBank(new TblBank(reservationController.selectedDataTransactionWithCekGiro.getTblBank()));
                        reservationController.selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(reservationController.selectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                        break;
                    }
                }
                break;
            case 6:    //Travel Agent
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        reservationController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporat)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        reservationController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        reservationController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 9:    //Draw Dposit
                break;
            case 10:   //Voucher
                for (TblReservationPaymentWithReservationVoucher data : reservationController.dataReservationPaymentWithReservationVouchers) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationController.selectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher(data);
                        reservationController.selectedDataTransactionWithReservationVoucher.setTblReservationPayment(reservationController.selectedDataTransaction);
                        reservationController.selectedDataTransactionWithReservationVoucher.setTblReservationVoucher(new TblReservationVoucher(reservationController.selectedDataTransactionWithReservationVoucher.getTblReservationVoucher()));
                        break;
                    }
                }
                break;
            case 13:   //Open Deposit (Cash)
                break;
            case 14:   //Close Deposit (Cash)
                break;
            case 16:   //Canceling Fee
                break;
            default:
                break;
        }
    }

    public BigDecimal getSelectedDataTransactionNominalMustBeReturn() {
        BigDecimal result = new BigDecimal("0");
        if (dataInputRCTransactionInputDetailStatus == 1) { //update            
            for (TblReservationPayment data : reservationController.dataReservationPayments) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    result = result.add(data.getUnitNominal());
                    result = result.subtract(data.getRoundingValue());
                    //data transaction payment - with bank card
                    if (data.getRefFinanceTransactionPaymentType().getIdtype() == 2
                            || data.getRefFinanceTransactionPaymentType().getIdtype() == 3) {
                        for (TblReservationPaymentWithBankCard detailData : reservationController.dataReservationPaymentWithBankCards) {
                            if (detailData.getTblReservationPayment().equals(data)) {
                                result = result.add(detailData.getPaymentDiscount());
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    public void dataRCTransactionInputDetailDeleteHandle() {
        if (tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            //remove data from table items list
            for (TblReservationPayment data : reservationController.dataReservationPayments) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    if (data.getIdpayment() == 0L) {
                        Alert alert = ClassMessage.showConfirmationRemovingDataMessage(null, reservationController.dialogStage);
                        if (alert.getResult() == ButtonType.OK) {
                            ClassMessage.showSucceedRemovingDataMessage(null, reservationController.dialogStage);
                            //remove data rc-trancsation detail
                            removeRCTransactionDetailData(data);
                            //remove data rc-transaction
                            reservationController.dataReservationPayments.remove(data);
                        }
                    } else {
                        HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIUBAH", "Data yang sudah lewat tidak dapat dihapus..!", "", reservationController.dialogStage);
                    }
                    break;
                }
            }
            //reset data rc-transaction input
            reservationController.resetDataTransaction();
//            setSelectedDataToInputForm();
//            if (reservationController.dataInputTransactionStatus.equals("reservation")) {
            chbHotelDiscount.setDisable((!useHotelEventAvaliable()));
//            } else {
//                chbHotelDiscount.setDisable(true);
//            }
            refreshDataBill();
            tableDataRCTransactionInputDetail.getTableView().setItems(reservationController.loadAllDataReservationTransaction());
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, reservationController.dialogStage);
        }
    }

    private void removeRCTransactionDetailData(TblReservationPayment rcTransactionPayment) {
        switch (rcTransactionPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                for (TblReservationPaymentWithTransfer data : reservationController.dataReservationPaymentWithTransfers) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.dataReservationPaymentWithTransfers.remove(data);
                        break;
                    }
                }
                break;
            case 2:    //Debit
                for (TblReservationPaymentWithBankCard data : reservationController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.dataReservationPaymentWithBankCards.remove(data);
                        break;
                    }
                }
                break;
            case 3:    //Credit
                for (TblReservationPaymentWithBankCard data : reservationController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.dataReservationPaymentWithBankCards.remove(data);
                        break;
                    }
                }
                break;
            case 4:    //Cek
                for (TblReservationPaymentWithCekGiro data : reservationController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.dataReservationPaymentWithCekGiros.remove(data);
                        break;
                    }
                }
                break;
            case 5:    //Giro
                for (TblReservationPaymentWithCekGiro data : reservationController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.dataReservationPaymentWithCekGiros.remove(data);
                        break;
                    }
                }
                break;
            case 6:    //Travel Agent
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationController.dataReservationPaymentWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporatte)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationController.dataReservationPaymentWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationController.dataReservationPaymentWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 9:    //Draw Deposit
                break;
            case 10:   //Voucher
                for (TblReservationPaymentWithReservationVoucher data : reservationController.dataReservationPaymentWithReservationVouchers) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.dataReservationPaymentWithReservationVouchers.remove(data);
                        break;
                    }
                }
                break;
            case 13:   //Open Deposit (Cash)
                break;
            case 14:   //Close Deposit (Cash)
                break;
            case 16:   //Canceling Fee
                break;
            default:
                break;
        }
    }

    private void deleteGuaranteeLetterItemDetail(TblReservationPaymentWithGuaranteePayment dataGL) {
        //remove data
        for (int i = reservationController.dataGuaranteeLetterItemDetails.size() - 1; i > -1; i--) {
            if (reservationController.dataGuaranteeLetterItemDetails.get(i).getTblReservationPaymentWithGuaranteePayment().equals(dataGL)) {
                reservationController.dataGuaranteeLetterItemDetails.remove(i);
            }
        }
    }

    private void showRCTransactionInputDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/RCTransactionInputDetailDialog.fxml"));

            RCTransactionInputDetailController controller = new RCTransactionInputDetailController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(reservationController.dialogStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
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
        initFormDataRCTransaction();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCTransactionInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

    public ReservationController getParentController() {
        return reservationController;
    }

}
