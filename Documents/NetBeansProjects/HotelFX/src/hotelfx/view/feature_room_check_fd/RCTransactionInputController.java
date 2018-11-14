/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

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
import hotelfx.persistence.model.RefReservationBillDiscountType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefVoucherStatus;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithReservationVoucher;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
 * @author ABC-Programmer
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
//                if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
//                    if (reservationBillAndPaymentController.selectedDataReservationBillHotelEvent.getTblHotelEvent() != null) {
//                        discountPercentage = reservationBillAndPaymentController.selectedDataReservationBillHotelEvent.getTblHotelEvent().getDiscountPercentage();
//                    } else {
//                        discountPercentage = new BigDecimal("0");
//                    }
//                } else {  //checkout
//                    if (reservationBillAndPaymentController.selectedDataCheckOutBillHotelEvent.getTblHotelEvent() != null) {
//                        discountPercentage = reservationBillAndPaymentController.selectedDataCheckOutBillHotelEvent.getTblHotelEvent().getDiscountPercentage();
//                    } else {
//                        discountPercentage = new BigDecimal("0");
//                    }
//                }
//            } else {
//                discountPercentage = new BigDecimal("0");
//            }
//            refreshDataBill();
//        });
//        if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
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
//        List<TblHotelEvent> list = reservationBillAndPaymentController.getFReservationManager().getAllDataHotelEvent();
//        //remove invalid data
//        for (int i = list.size() - 1; i > -1; i--) {
//            if ((list.get(i).getMinTransaction() > reservationBillAndPaymentController.calculationTotalBill(reservationBillAndPaymentController.dataInputTransactionStatus, 0))
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

        lblServiceChargePercentage.setText("(" + ClassFormatter.decimalFormat.cFormat(reservationBillAndPaymentController.dataReservationBill.getServiceChargePercentage().multiply(new BigDecimal("100"))) + " %)");
        lblTaxPercentage.setText("(" + ClassFormatter.decimalFormat.cFormat(reservationBillAndPaymentController.dataReservationBill.getTaxPercentage().multiply(new BigDecimal("100"))) + " %)");
//            cbpHotelEvent.valueProperty().bindBidirectional(reservationBillAndPaymentController.selectedDataReservationBillHotelEvent.tblHotelEventProperty());
//            reservationBillAndPaymentController.selectedDataReservationBillHotelEvent.tblHotelEventProperty().addListener((obs, oldVal, newVal) -> {
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
        txtBill.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalReservationRoom()
                .subtract(reservationBillAndPaymentController.calculationTotalReservationRoomCompliment())
                .add(reservationBillAndPaymentController.calculationTotalAdditional())
                .add(reservationBillAndPaymentController.calculationTotalBroken())));
        txtDiscount.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalDiscount()));
        txtTotalServiceCharge.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalServiceCharge()));
        txtTotalTax.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalTax()));
        txtTotalBill.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalBillAfterRounding()));
        lblRoundingValue.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalReservationTransactionRoundingValue()) + ")");
        txtTotalPayment.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalReservationTransaction()));
        txtTotalDiscountPayment.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalReservationTransactionDisountPayment()));
        txtTotalRestOfBill.setText(ClassFormatter.currencyFormat.cFormat(reservationBillAndPaymentController.calculationTotalRestOfBill()));
    }

//    private void saveDataDiscount() {
//        if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
//            if (chbHotelDiscount.isSelected()) {
//                reservationBillAndPaymentController.dataReservationBill.setRefReservationBillDiscountType(reservationBillAndPaymentController.getFReservationManager().getReservationBillDiscountType(0));
//                reservationBillAndPaymentController.dataReservationBill.setDiscountPercentage(reservationBillAndPaymentController.selectedDataReservationBillHotelEvent.getTblHotelEvent().getDiscountPercentage());
//                reservationBillAndPaymentController.dataReservationBillHotelEvent = reservationBillAndPaymentController.selectedDataReservationBillHotelEvent;
//            }
//        } else {  //checkout
//            if (chbHotelDiscount.isSelected()) {
//                reservationBillAndPaymentController.dataCheckOutBill.setRefReservationBillDiscountType(reservationBillAndPaymentController.getFReservationManager().getReservationBillDiscountType(0));
//                reservationBillAndPaymentController.dataCheckOutBill.setDiscountPercentage(reservationBillAndPaymentController.selectedDataCheckOutBillHotelEvent.getTblHotelEvent().getDiscountPercentage());
//                reservationBillAndPaymentController.dataCheckOutBillHotelEvent = reservationBillAndPaymentController.selectedDataCheckOutBillHotelEvent;
//            }
//        }
//    }
    public void saveDataTransaction() {
        //data rc-transaction
        reservationBillAndPaymentController.selectedDataTransaction.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
        reservationBillAndPaymentController.selectedDataTransaction.setTblEmployeeByIdcashier(ClassSession.currentUser.getTblEmployeeByIdemployee());
        reservationBillAndPaymentController.selectedDataTransaction.setRefReservationBillType(reservationBillAndPaymentController.getParentController().getServiceRV2().getDataReservationBillType(0));    //Reservation = '0'
        if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
            reservationBillAndPaymentController.dataReservationPayments.add(reservationBillAndPaymentController.selectedDataTransaction);
        } else {  //update
            for (int i = 0; i < reservationBillAndPaymentController.dataReservationPayments.size(); i++) {
                if (reservationBillAndPaymentController.dataReservationPayments.get(i).equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    reservationBillAndPaymentController.dataReservationPayments.set(i, reservationBillAndPaymentController.selectedDataTransaction);
                    break;
                }
            }
        }
//            reservationBillAndPaymentController.tableDataReservationTransaction.getTableView().setItems(FXCollections.observableArrayList(reservationBillAndPaymentController.dataReservationPayments));
        //data rc-transaction detail
        saveDataTransactionDetail();
        //reset data rc-transaction input
        reservationBillAndPaymentController.resetDataTransaction();
//        if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
        chbHotelDiscount.setDisable((!useHotelEventAvaliable()));
//        } else {
//            chbHotelDiscount.setDisable(true);
//        }
        refreshDataBill();
        tableDataRCTransactionInputDetail.getTableView().setItems(reservationBillAndPaymentController.loadAllDataReservationTransaction());
        //close dialog data rc-transaction input detail
        dialogStage.close();
    }

    public boolean useHotelEventAvaliable() {
        boolean available = true;
        if (!ClassSetting.isMultipleDiscountAllowed) {
            for (TblReservationPaymentWithBankCard dataPaymentWithBankCard : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
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
                for (TblReservationPaymentWithBankCard dataPaymentWithBankCard : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
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
        switch (reservationBillAndPaymentController.selectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationBillAndPaymentController.dataReservationPaymentWithTransfers.add(reservationBillAndPaymentController.selectedDataTransactionWithTransfer);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithTransfers.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithTransfers.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationBillAndPaymentController.dataReservationPaymentWithTransfers.set(i, reservationBillAndPaymentController.selectedDataTransactionWithTransfer);
                            break;
                        }
                    }
                }
                break;
            case 2:    //Debit
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationBillAndPaymentController.dataReservationPaymentWithBankCards.add(reservationBillAndPaymentController.selectedDataTransactionWithBankCard);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithBankCards.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithBankCards.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationBillAndPaymentController.dataReservationPaymentWithBankCards.set(i, reservationBillAndPaymentController.selectedDataTransactionWithBankCard);
                            break;
                        }
                    }
                }
                break;
            case 3:    //Credit
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationBillAndPaymentController.dataReservationPaymentWithBankCards.add(reservationBillAndPaymentController.selectedDataTransactionWithBankCard);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithBankCards.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithBankCards.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationBillAndPaymentController.dataReservationPaymentWithBankCards.set(i, reservationBillAndPaymentController.selectedDataTransactionWithBankCard);
                            break;
                        }
                    }
                }
                break;
            case 4:    //Cek
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.add(reservationBillAndPaymentController.selectedDataTransactionWithCekGiro);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.set(i, reservationBillAndPaymentController.selectedDataTransactionWithCekGiro);
                            break;
                        }
                    }
                }
                break;
            case 5:    //Giro
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.add(reservationBillAndPaymentController.selectedDataTransactionWithCekGiro);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.set(i, reservationBillAndPaymentController.selectedDataTransactionWithCekGiro);
                            break;
                        }
                    }
                }
                break;
            case 6:    //Travel Agent
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.add(reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.get(i),
                                    reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.set(i, reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporatte)
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.add(reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.get(i),
                                    reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.set(i, reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.add(reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.get(i),
                                    reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.set(i, reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 9:    //Draw Dposit
                break;
            case 10:   //Voucher
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers.add(reservationBillAndPaymentController.selectedDataTransactionWithReservationVoucher);
                } else {  //update
                    for (int i = 0; i < reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers.size(); i++) {
                        if (reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers.set(i, reservationBillAndPaymentController.selectedDataTransactionWithReservationVoucher);
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
        for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationBillAndPaymentController.dataReservationRoomTypeDetailRoomPriceDetails) {
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
//                dataGLID.setDetailCost((long) reservationBillAndPaymentController.calculationRoomTypeDetailCost(dataRRTD));
            dataGLID.setDetailCost(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
            dataGLID.setDetailQuantity(new BigDecimal("1"));
//                dataGLID.setTotalDiscountNominal((long) reservationBillAndPaymentController.calculationRoomTypeDetailDiscount(dataRRTD));
            dataGLID.setTotalDiscountNominal(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice()
                    .multiply(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
            dataGLID.setServiceChargePercentage(reservationBillAndPaymentController.dataReservationBill.getServiceChargePercentage());
            dataGLID.setTaxPercentage(reservationBillAndPaymentController.dataReservationBill.getTaxPercentage());
            dataGLID.setDetailType("Room");
            //add data to list
            reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.add(dataGLID);
        }
        //data additional item
        for (TblReservationAdditionalItem dataAdditionalItem : reservationBillAndPaymentController.dataReservationAdditionalItems) {
            if (dataAdditionalItem.getRefReservationBillType().getIdtype() == 0 //Reservation = '0'
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
                dataGLID.setServiceChargePercentage(reservationBillAndPaymentController.dataReservationBill.getServiceChargePercentage());
                dataGLID.setTaxPercentage(reservationBillAndPaymentController.dataReservationBill.getTaxPercentage());
                dataGLID.setDetailType("Item");
                //add data to list
                reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.add(dataGLID);
            }
        }
        //data additional service
        for (TblReservationAdditionalService dataAdditionalService : reservationBillAndPaymentController.dataReservationAdditionalServices) {
            if (dataAdditionalService.getRefReservationBillType().getIdtype() == 0 //Reservation = '0'
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
                    dataGLID.setServiceChargePercentage(reservationBillAndPaymentController.dataReservationBill.getServiceChargePercentage());
                    dataGLID.setTaxPercentage(reservationBillAndPaymentController.dataReservationBill.getTaxPercentage());
                }
                dataGLID.setDetailType("Service");
                //add data to list
                reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.add(dataGLID);
            }
        }
        //data broken item
        for (TblReservationBrokenItem dataRBI : reservationBillAndPaymentController.dataReservationBrokenItems) {
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
            dataGLID.setServiceChargePercentage(reservationBillAndPaymentController.dataReservationBill.getServiceChargePercentage());
            dataGLID.setTaxPercentage(reservationBillAndPaymentController.dataReservationBill.getTaxPercentage());
            dataGLID.setDetailType("Broken-Item");
            //add data to list
            reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.add(dataGLID);
        }
    }

    private void saveGuaranteeLetterItemDetail(TblReservationPaymentWithGuaranteePayment oldDataGL,
            TblReservationPaymentWithGuaranteePayment newDataGL) {
        //remove data
        for (int i = reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.size() - 1; i > -1; i--) {
            if (reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.get(i).getTblReservationPaymentWithGuaranteePayment().equals(oldDataGL)) {
                reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.remove(i);
            }
        }
        //add data
        saveGuaranteeLetterItemDetail(newDataGL);
    }

    public void dataRCTTransactionSaveHandle() {
        if (reservationBillAndPaymentController.dataReservationPayments.isEmpty()) {
            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA GAGAL DISIMPAN", "Tidak ada data pembayaran yang harus disimpan..!", "", reservationBillAndPaymentController.dialogStage);
        } else {
            if (reservationBillAndPaymentController.isFirstPayment //first payment
                    && reservationBillAndPaymentController.calculationTotalRestOfBill().compareTo(new BigDecimal("0")) != 0) {    //full payment
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA GAGAL DISIMPAN", "Sisa tagihan lebih besar dari '0', silahkan buat data transaksi pembayaran lainnya..!", "", reservationBillAndPaymentController.dialogStage);
            } else {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationBillAndPaymentController.dialogStage);
                if (alert.getResult() == ButtonType.OK) {
                    //dummy entry
                    TblReservation dummySelectedData = new TblReservation(reservationBillAndPaymentController.getParentController().selectedDataRoomTypeDetail.getTblReservation());
                    if (reservationBillAndPaymentController.isFirstPayment) {  //first payment
                       dummySelectedData.setRefReservationStatus(reservationBillAndPaymentController.getParentController().getServiceRV2().getReservationStatus(2));  //Booked = '2'
                    }
                    List<TblReservationRoomTypeDetail> dummyDataRoomTypeDetails = new ArrayList<>();
                    for (TblReservationRoomTypeDetail dataRoomTypeDetail : reservationBillAndPaymentController.dataReservationRoomTypeDetails) {
                        TblReservationRoomTypeDetail dummyDataRoomTypeDetail = new TblReservationRoomTypeDetail(dataRoomTypeDetail);
                        dummyDataRoomTypeDetail.setTblReservation(dummySelectedData);
                        dummyDataRoomTypeDetail.setTblRoomType(new TblRoomType(dummyDataRoomTypeDetail.getTblRoomType()));
                        if (dummyDataRoomTypeDetail.getTblReservationCheckInOut() != null) {
                            dummyDataRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummyDataRoomTypeDetail.getTblReservationCheckInOut()));
                            if (dummyDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom() != null) {
                                dummyDataRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummyDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom()));
                            }
                            if (dummyDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                dummyDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummyDataRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                        }
                        dummyDataRoomTypeDetails.add(dummyDataRoomTypeDetail);
                    }
                    TblReservationBill dummyDataReservationBill = new TblReservationBill(reservationBillAndPaymentController.dataReservationBill);
                    dummyDataReservationBill.setTblReservation(dummySelectedData);
                    dummyDataReservationBill.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill.getRefReservationBillType()));
                    if (dummyDataReservationBill.getTblBankCard() != null) {
                        dummyDataReservationBill.setTblBankCard(new TblBankCard(dummyDataReservationBill.getTblBankCard()));
                    }
                    if (dummyDataReservationBill.getRefReservationBillDiscountType() != null) {
                        dummyDataReservationBill.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill.getRefReservationBillDiscountType()));
                    }
                    List<TblReservationPayment> dummyDataReservationPayments = new ArrayList<>();
                    List<TblReservationPaymentWithTransfer> dummyDataReservationPaymentWithTransfersR = new ArrayList<>();
                    List<TblReservationPaymentWithBankCard> dummyDataReservationPaymentWithBankCardsR = new ArrayList<>();
                    List<TblReservationPaymentWithCekGiro> dummyDataReservationPaymentWithCekGirosR = new ArrayList<>();
                    List<TblReservationPaymentWithGuaranteePayment> dummyDataReservationPaymentWithGuaranteePaymentsR = new ArrayList<>();
                    List<TblGuaranteeLetterItemDetail> dummyDataGuaranteeLetterItemDetailsR = new ArrayList<>();
                    List<TblReservationPaymentWithReservationVoucher> dummyDataReservationPaymentWithReservationVouchersR = new ArrayList<>();
                    for (TblReservationPayment dataReservationPayment : reservationBillAndPaymentController.dataReservationPayments) {
                        TblReservationPayment dummyDataReservationPayment = new TblReservationPayment(dataReservationPayment);
                        dummyDataReservationPayment.setTblReservationBill(dummyDataReservationBill);
                        dummyDataReservationPayment.setTblEmployeeByIdcashier(new TblEmployee(dummyDataReservationPayment.getTblEmployeeByIdcashier()));
                        dummyDataReservationPayment.setRefReservationBillType(new RefReservationBillType(dummyDataReservationPayment.getRefReservationBillType()));
                        dummyDataReservationPayment.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummyDataReservationPayment.getRefFinanceTransactionPaymentType()));
                        for (TblReservationPaymentWithTransfer dataReservationPaymentWithTransferR : reservationBillAndPaymentController.dataReservationPaymentWithTransfers) {
                            if (dataReservationPaymentWithTransferR.getTblReservationPayment().equals(dataReservationPayment)) {
                                TblReservationPaymentWithTransfer dummyDataReservationPaymentWithTransferR = new TblReservationPaymentWithTransfer(dataReservationPaymentWithTransferR);
                                dummyDataReservationPaymentWithTransferR.setTblReservationPayment(dummyDataReservationPayment);
                                dummyDataReservationPaymentWithTransferR.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithTransferR.getTblBankAccountBySenderBankAccount()));
                                dummyDataReservationPaymentWithTransferR.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithTransferR.getTblBankAccountByReceiverBankAccount()));
                                dummyDataReservationPaymentWithTransfersR.add(dummyDataReservationPaymentWithTransferR);
                            }
                        }
                        for (TblReservationPaymentWithBankCard dataReservationPaymentWithBankCardR : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
                            if (dataReservationPaymentWithBankCardR.getTblReservationPayment().equals(dataReservationPayment)) {
                                TblReservationPaymentWithBankCard dummyDataReservationPaymentWithBankCardR = new TblReservationPaymentWithBankCard(dataReservationPaymentWithBankCardR);
                                dummyDataReservationPaymentWithBankCardR.setTblReservationPayment(dummyDataReservationPayment);
                                dummyDataReservationPaymentWithBankCardR.setTblBank(new TblBank(dummyDataReservationPaymentWithBankCardR.getTblBank()));
                                dummyDataReservationPaymentWithBankCardR.setTblBankEdc(new TblBankEdc(dummyDataReservationPaymentWithBankCardR.getTblBankEdc()));
                                dummyDataReservationPaymentWithBankCardR.setTblBankNetworkCard(new TblBankNetworkCard(dummyDataReservationPaymentWithBankCardR.getTblBankNetworkCard()));
                                if (dummyDataReservationPaymentWithBankCardR.getTblBankEdcBankNetworkCard() != null) {
                                    dummyDataReservationPaymentWithBankCardR.setTblBankEdcBankNetworkCard(new TblBankEdcBankNetworkCard(dummyDataReservationPaymentWithBankCardR.getTblBankEdcBankNetworkCard()));
                                }
                                if (dummyDataReservationPaymentWithBankCardR.getTblBankEventCard() != null) {
                                    dummyDataReservationPaymentWithBankCardR.setTblBankEventCard(new TblBankEventCard(dummyDataReservationPaymentWithBankCardR.getTblBankEventCard()));
                                }
                                dummyDataReservationPaymentWithBankCardR.setTblBankAccount(new TblBankAccount(dummyDataReservationPaymentWithBankCardR.getTblBankAccount()));
                                dummyDataReservationPaymentWithBankCardsR.add(dummyDataReservationPaymentWithBankCardR);
                            }
                        }
                        for (TblReservationPaymentWithCekGiro dataReservationPaymentWithCekGiroR : reservationBillAndPaymentController.dataReservationPaymentWithCekGiros) {
                            if (dataReservationPaymentWithCekGiroR.getTblReservationPayment().equals(dataReservationPayment)) {
                                TblReservationPaymentWithCekGiro dummyDataReservationPaymentWithCekGiroR = new TblReservationPaymentWithCekGiro(dataReservationPaymentWithCekGiroR);
                                dummyDataReservationPaymentWithCekGiroR.setTblReservationPayment(dummyDataReservationPayment);
                                dummyDataReservationPaymentWithCekGiroR.setTblBank(new TblBank(dummyDataReservationPaymentWithCekGiroR.getTblBank()));
//                                        dummyDataReservationPaymentWithCekGiroR.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithCekGiroR.getTblBankAccountBySenderBankAccount()));
                                dummyDataReservationPaymentWithCekGiroR.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithCekGiroR.getTblBankAccountByReceiverBankAccount()));
                                dummyDataReservationPaymentWithCekGirosR.add(dummyDataReservationPaymentWithCekGiroR);
                            }
                        }
                        for (TblReservationPaymentWithGuaranteePayment dataReservationPaymentWithGuaranteePaymentR : reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments) {
                            if (dataReservationPaymentWithGuaranteePaymentR.getTblReservationPayment().equals(dataReservationPayment)) {
                                TblReservationPaymentWithGuaranteePayment dummyDataReservationPaymentWithGuaranteePaymentR = new TblReservationPaymentWithGuaranteePayment(dataReservationPaymentWithGuaranteePaymentR);
                                dummyDataReservationPaymentWithGuaranteePaymentR.setTblReservationPayment(dummyDataReservationPayment);
                                dummyDataReservationPaymentWithGuaranteePaymentR.setTblPartner(new TblPartner(dummyDataReservationPaymentWithGuaranteePaymentR.getTblPartner()));
                                if (dummyDataReservationPaymentWithGuaranteePaymentR.getTblBankAccountBySenderBankAccount() != null) {
                                    dummyDataReservationPaymentWithGuaranteePaymentR.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithGuaranteePaymentR.getTblBankAccountBySenderBankAccount()));
                                }
                                if (dummyDataReservationPaymentWithGuaranteePaymentR.getTblBankAccountByReceiverBankAccount() != null) {
                                    dummyDataReservationPaymentWithGuaranteePaymentR.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithGuaranteePaymentR.getTblBankAccountByReceiverBankAccount()));
                                }
                                dummyDataReservationPaymentWithGuaranteePaymentsR.add(dummyDataReservationPaymentWithGuaranteePaymentR);
                                //guarantee letter - item detail
                                for (TblGuaranteeLetterItemDetail dataGuaranteeLetterItemDetailR : reservationBillAndPaymentController.dataGuaranteeLetterItemDetails) {
                                    if (dataGuaranteeLetterItemDetailR.getTblReservationPaymentWithGuaranteePayment().equals(dataReservationPaymentWithGuaranteePaymentR)) {
                                        TblGuaranteeLetterItemDetail dummyDataGuaranteeLetterItemDetailR = new TblGuaranteeLetterItemDetail(dataGuaranteeLetterItemDetailR);
                                        dummyDataGuaranteeLetterItemDetailR.setTblReservationPaymentWithGuaranteePayment(dummyDataReservationPaymentWithGuaranteePaymentR);
                                        dummyDataGuaranteeLetterItemDetailsR.add(dummyDataGuaranteeLetterItemDetailR);
                                    }
                                }
                            }
                        }
                        for (TblReservationPaymentWithReservationVoucher dataReservationPaymentWithReservationVoucherR : reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers) {
                            if (dataReservationPaymentWithReservationVoucherR.getTblReservationPayment().equals(dataReservationPayment)) {
                                TblReservationPaymentWithReservationVoucher dummyDataReservationPaymentWithReservationVoucherR = new TblReservationPaymentWithReservationVoucher(dataReservationPaymentWithReservationVoucherR);
                                dummyDataReservationPaymentWithReservationVoucherR.setTblReservationPayment(dummyDataReservationPayment);
                                dummyDataReservationPaymentWithReservationVoucherR.setTblReservationVoucher(new TblReservationVoucher(dummyDataReservationPaymentWithReservationVoucherR.getTblReservationVoucher()));
                                dummyDataReservationPaymentWithReservationVoucherR.getTblReservationVoucher().setRefVoucherStatus(new RefVoucherStatus(dummyDataReservationPaymentWithReservationVoucherR.getTblReservationVoucher().getRefVoucherStatus()));
                                dummyDataReservationPaymentWithReservationVouchersR.add(dummyDataReservationPaymentWithReservationVoucherR);
                            }
                        }
                        dummyDataReservationPayments.add(dummyDataReservationPayment);
                    }
                    if (reservationBillAndPaymentController.getParentController().getServiceRV2().updateDataReservationTransactionPayments(dummySelectedData, dummyDataRoomTypeDetails,
                            dummyDataReservationBill, dummyDataReservationPayments,
                            dummyDataReservationPaymentWithTransfersR, dummyDataReservationPaymentWithBankCardsR, dummyDataReservationPaymentWithCekGirosR,
                            dummyDataReservationPaymentWithGuaranteePaymentsR, dummyDataReservationPaymentWithReservationVouchersR,
                            dummyDataGuaranteeLetterItemDetailsR,
                            reservationBillAndPaymentController.calculationTotalRestOfBill())) {
                        ClassMessage.showSucceedInsertingDataMessage("", reservationBillAndPaymentController.dialogStage);
                        //set selected data
                        reservationBillAndPaymentController.setSelectedDataToInputForm();
                        //close form data reservation payment
                        reservationBillAndPaymentController.dialogStage.close();
                    } else {
                        ClassMessage.showFailedInsertingDataMessage(reservationBillAndPaymentController.getParentController().getServiceRV2().getErrorMessage(), reservationBillAndPaymentController.dialogStage);
                    }
                }
//                ButtonType btnJustSaveData = new ButtonType("Simpan");
//                ButtonType btnSaveAndPrintReceiptData = new ButtonType("Simpan & Cetak");
//                ButtonType btnCancelData = new ButtonType("Batal", ButtonData.CANCEL_CLOSE);
//                Alert alert = HotelFX.showAlertChooseButton(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menyimpan data ini?", "", reservationBillAndPaymentController.dialogStage,
//                        btnJustSaveData, 
//                        btnSaveAndPrintReceiptData, 
//                        btnCancelData);
//                if (alert.getResult() == btnJustSaveData) {
//                    //update data bill in reservation (view)
//                    reservationBillAndPaymentController.refreshDataBill(reservationBillAndPaymentController.dataInputTransactionStatus);
//                    //update data reservation status
//                    if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
//                        reservationBillAndPaymentController.selectedData.setRefReservationStatus(reservationBillAndPaymentController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
//                    } else {  //checkout
//                        reservationBillAndPaymentController.selectedData.setRefReservationStatus(reservationBillAndPaymentController.getFReservationManager().getReservationStatus(5));    //Checkout = '5'
//                    }
//                    //save data to database
//                    if(reservationBillAndPaymentController.dataReservationSaveHandle(2)){
//                        //close form data reservation payment
//                        reservationBillAndPaymentController.dialogStage.close();
//                    }
//                } else if (alert.getResult() == btnSaveAndPrintReceiptData) {
//                    //update data bill in reservation (view)
//                    reservationBillAndPaymentController.refreshDataBill(reservationBillAndPaymentController.dataInputTransactionStatus);
//                    //update data reservation status
//                    if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
//                        reservationBillAndPaymentController.selectedData.setRefReservationStatus(reservationBillAndPaymentController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
//                    } else {  //checkout
//                        reservationBillAndPaymentController.selectedData.setRefReservationStatus(reservationBillAndPaymentController.getFReservationManager().getReservationStatus(5));    //Checkout = '5'
//                    }
//                    //save data to database
//                    if(reservationBillAndPaymentController.dataReservationSaveHandle(2)){
//                        //print data
//                        if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {   //reservation
//                            reservationBillAndPaymentController.dataReservationTransactionPrintHandle();
//                        } else {  //checkout
//                            reservationBillAndPaymentController.dataCheckOutTransactionPrintHandle();
//                        }
//                        //close form data reservation payment
//                        reservationBillAndPaymentController.dialogStage.close();
//                    }
//                } else {
//
//                }
//                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationBillAndPaymentController.dialogStage);
//                if (alert.getResult() == ButtonType.OK) {
//                //save data discount
//                saveDataDiscount();
                //update data bill in reservation (view)
//                    reservationBillAndPaymentController.refreshDataBill(reservationBillAndPaymentController.dataInputTransactionStatus);
//                    //update data reservation status
//                    if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
//                        reservationBillAndPaymentController.selectedData.setRefReservationStatus(reservationBillAndPaymentController.getFReservationManager().getReservationStatus(2));  //Booked = '2'
//                    } else {  //checkout
//                        reservationBillAndPaymentController.selectedData.setRefReservationStatus(reservationBillAndPaymentController.getFReservationManager().getReservationStatus(5));    //Checkout = '5'
//                    }
//                    //save data to database
//                    reservationBillAndPaymentController.dataReservationSaveHandle(2);
//                    //close form data reservation payment
//                    reservationBillAndPaymentController.dialogStage.close();
//                }
            }
        }
    }

    private void dataRCTransactionCancelHandle() {
        //set selected data
        reservationBillAndPaymentController.setSelectedDataToInputForm();
        //close form data rc-transaction input
        reservationBillAndPaymentController.dialogStage.close();
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
        tableView.setItems(reservationBillAndPaymentController.loadAllDataReservationTransaction());
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
//            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan pilih data diskon Hotel terlebih dahulu..!", null, reservationBillAndPaymentController.dialogStage);
//        } else {
        dataInputRCTransactionInputDetailStatus = 0;
        //reset data selected rc-transaction
        reservationBillAndPaymentController.resetDataTransaction();
        //open form data customer bank account
        showRCTransactionInputDetailDialog();
//        }
    }

    public void dataRCTransactionInputDetailUpdateHandle() {
//        if (chbHotelDiscount.isSelected()
//                && cbpHotelEvent.getValue() == null) {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan pilih data diskon Hotel terlebih dahulu..!", null, reservationBillAndPaymentController.dialogStage);
//        } else {
        if (tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputRCTransactionInputDetailStatus = 1;
            //reset data selected rc-transaction
            reservationBillAndPaymentController.resetDataTransaction();
            for (TblReservationPayment data : reservationBillAndPaymentController.dataReservationPayments) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    //set data selected rc-transaction
                    reservationBillAndPaymentController.selectedDataTransaction = new TblReservationPayment(data);
                    reservationBillAndPaymentController.selectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(reservationBillAndPaymentController.selectedDataTransaction.getRefFinanceTransactionPaymentType()));
                    //set data selected rc-transaction detail
                    setRCTransactionDetailData(data);
                    break;
                }
            }
            if (reservationBillAndPaymentController.selectedDataTransaction.getIdpayment() == 0L) {
                //open form data transaction input detail
                showRCTransactionInputDetailDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIUBAH", "Data yang sudah lewat tidak dapat diubah..!", "", reservationBillAndPaymentController.dialogStage);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, reservationBillAndPaymentController.dialogStage);
        }
//        }
    }

    private void setRCTransactionDetailData(TblReservationPayment realRCTransactionPayment) {
        switch (realRCTransactionPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                for (TblReservationPaymentWithTransfer data : reservationBillAndPaymentController.dataReservationPaymentWithTransfers) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithTransfer.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 2:    //Debit
                for (TblReservationPaymentWithBankCard data : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBank(new TblBank(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBank()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankEdc(new TblBankEdc(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankEdc()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankNetworkCard(new TblBankNetworkCard(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankNetworkCard()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankEventCard(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankEventCard() != null
                                ? new TblBankEventCard(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankEventCard()) : null);
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankAccount(new TblBankAccount(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankAccount()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                        break;
                    }
                }
                break;
            case 3:    //Credit
                for (TblReservationPaymentWithBankCard data : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBank(new TblBank(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBank()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankEdc(new TblBankEdc(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankEdc()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankNetworkCard(new TblBankNetworkCard(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankNetworkCard()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankEventCard(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankEventCard() != null
                                ? new TblBankEventCard(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankEventCard()) : null);
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setTblBankAccount(new TblBankAccount(reservationBillAndPaymentController.selectedDataTransactionWithBankCard.getTblBankAccount()));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                        reservationBillAndPaymentController.selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                        break;
                    }
                }
                break;
            case 4:    //Cek
                for (TblReservationPaymentWithCekGiro data : reservationBillAndPaymentController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.setTblBank(new TblBank(reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.getTblBank()));
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                        break;
                    }
                }
                break;
            case 5:    //Giro
                for (TblReservationPaymentWithCekGiro data : reservationBillAndPaymentController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.setTblBank(new TblBank(reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.getTblBank()));
                        reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(reservationBillAndPaymentController.selectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                        break;
                    }
                }
                break;
            case 6:    //Travel Agent
                for (TblReservationPaymentWithGuaranteePayment data : reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporat)
                for (TblReservationPaymentWithGuaranteePayment data : reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                for (TblReservationPaymentWithGuaranteePayment data : reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        break;
                    }
                }
                break;
            case 9:    //Draw Dposit
                break;
            case 10:   //Voucher
                for (TblReservationPaymentWithReservationVoucher data : reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        reservationBillAndPaymentController.selectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher(data);
                        reservationBillAndPaymentController.selectedDataTransactionWithReservationVoucher.setTblReservationPayment(reservationBillAndPaymentController.selectedDataTransaction);
                        reservationBillAndPaymentController.selectedDataTransactionWithReservationVoucher.setTblReservationVoucher(new TblReservationVoucher(reservationBillAndPaymentController.selectedDataTransactionWithReservationVoucher.getTblReservationVoucher()));
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
            for (TblReservationPayment data : reservationBillAndPaymentController.dataReservationPayments) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    result = result.add(data.getUnitNominal());
                    result = result.subtract(data.getRoundingValue());
                    //data transaction payment - with bank card
                    if (data.getRefFinanceTransactionPaymentType().getIdtype() == 2
                            || data.getRefFinanceTransactionPaymentType().getIdtype() == 3) {
                        for (TblReservationPaymentWithBankCard detailData : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
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
            for (TblReservationPayment data : reservationBillAndPaymentController.dataReservationPayments) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    if (data.getIdpayment() == 0L) {
                        Alert alert = ClassMessage.showConfirmationRemovingDataMessage(null, reservationBillAndPaymentController.dialogStage);
                        if (alert.getResult() == ButtonType.OK) {
                            ClassMessage.showSucceedRemovingDataMessage(null, reservationBillAndPaymentController.dialogStage);
                            //remove data rc-trancsation detail
                            removeRCTransactionDetailData(data);
                            //remove data rc-transaction
                            reservationBillAndPaymentController.dataReservationPayments.remove(data);
                        }
                    } else {
                        HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIUBAH", "Data yang sudah lewat tidak dapat dihapus..!", "", reservationBillAndPaymentController.dialogStage);
                    }
                    break;
                }
            }
            //reset data rc-transaction input
            reservationBillAndPaymentController.resetDataTransaction();
//            setSelectedDataToInputForm();
//            if (reservationBillAndPaymentController.dataInputTransactionStatus.equals("reservation")) {
            chbHotelDiscount.setDisable((!useHotelEventAvaliable()));
//            } else {
//                chbHotelDiscount.setDisable(true);
//            }
            refreshDataBill();
            tableDataRCTransactionInputDetail.getTableView().setItems(reservationBillAndPaymentController.loadAllDataReservationTransaction());
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, reservationBillAndPaymentController.dialogStage);
        }
    }

    private void removeRCTransactionDetailData(TblReservationPayment rcTransactionPayment) {
        switch (rcTransactionPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                for (TblReservationPaymentWithTransfer data : reservationBillAndPaymentController.dataReservationPaymentWithTransfers) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationBillAndPaymentController.dataReservationPaymentWithTransfers.remove(data);
                        break;
                    }
                }
                break;
            case 2:    //Debit
                for (TblReservationPaymentWithBankCard data : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationBillAndPaymentController.dataReservationPaymentWithBankCards.remove(data);
                        break;
                    }
                }
                break;
            case 3:    //Credit
                for (TblReservationPaymentWithBankCard data : reservationBillAndPaymentController.dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationBillAndPaymentController.dataReservationPaymentWithBankCards.remove(data);
                        break;
                    }
                }
                break;
            case 4:    //Cek
                for (TblReservationPaymentWithCekGiro data : reservationBillAndPaymentController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.remove(data);
                        break;
                    }
                }
                break;
            case 5:    //Giro
                for (TblReservationPaymentWithCekGiro data : reservationBillAndPaymentController.dataReservationPaymentWithCekGiros) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationBillAndPaymentController.dataReservationPaymentWithCekGiros.remove(data);
                        break;
                    }
                }
                break;
            case 6:    //Travel Agent
                for (TblReservationPaymentWithGuaranteePayment data : reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporatte)
                for (TblReservationPaymentWithGuaranteePayment data : reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                for (TblReservationPaymentWithGuaranteePayment data : reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationBillAndPaymentController.dataReservationPaymentWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 9:    //Draw Deposit
                break;
            case 10:   //Voucher
                for (TblReservationPaymentWithReservationVoucher data : reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationBillAndPaymentController.dataReservationPaymentWithReservationVouchers.remove(data);
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
        for (int i = reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.size() - 1; i > -1; i--) {
            if (reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.get(i).getTblReservationPaymentWithGuaranteePayment().equals(dataGL)) {
                reservationBillAndPaymentController.dataGuaranteeLetterItemDetails.remove(i);
            }
        }
    }

    private void showRCTransactionInputDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/RCTransactionInputDetailDialog.fxml"));

            RCTransactionInputDetailController controller = new RCTransactionInputDetailController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(reservationBillAndPaymentController.dialogStage);

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

    public RCTransactionInputController(ReservationBillAndPaymentController parentController) {
        reservationBillAndPaymentController = parentController;
    }

    private final ReservationBillAndPaymentController reservationBillAndPaymentController;

    public ReservationBillAndPaymentController getParentController() {
        return reservationBillAndPaymentController;
    }

}
