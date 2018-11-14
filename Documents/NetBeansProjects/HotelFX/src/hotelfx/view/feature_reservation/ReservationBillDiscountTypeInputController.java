/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillDiscountType;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class ReservationBillDiscountTypeInputController implements Initializable {

    @FXML
    private AnchorPane ancFormReservationBillDiscountType;

    @FXML
    private GridPane gpFormDataReservationBillDiscountType;

    @FXML
    private AnchorPane ancCBRBDTLayout;

    private JFXCComboBoxTablePopup<SelectedDiscount> cbpSelectedDiscount;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationBillDiscountType() {
        initDataReservationBillDiscountTypePopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Tipe Diskon)"));
        btnSave.setOnAction((e) -> {
            dataReservationBillDiscountTypeSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationBillDiscountTypeCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpSelectedDiscount);
    }

    private void initDataReservationBillDiscountTypePopup() {
        //Reservation Bill Discount Type
        //data selected discount
        TableView<SelectedDiscount> tableSelectedDiscount = new TableView<>();

        TableColumn<SelectedDiscount, String> discountName = new TableColumn<>("Nama");
        discountName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataDiscountType() != null
                                ? (param.getValue().getDataDiscountType().getIdtype() == 0 //Hotel-Event = '0'
                                        ? "Hotel" : param.getValue().getDataBankCard().getBankCardName())
                                : "-", param.getValue().dataDiscountTypeProperty()));
        discountName.setMinWidth(140);

        TableColumn<SelectedDiscount, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBankCard() != null
                                ? param.getValue().getDataBankCard().getTblBank().getBankName() : "-", param.getValue().dataBankCardProperty()));
        bankName.setMinWidth(140);

        TableColumn<SelectedDiscount, String> cardClassName = new TableColumn<>("Tipe");
        cardClassName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBankCard() != null
                                ? param.getValue().getDataBankCard().getBankCardClassName() : "-",
                        param.getValue().dataBankCardProperty()));
        cardClassName.setMinWidth(140);

        TableColumn<SelectedDiscount, String> cardTypeName = new TableColumn<>("Jenis");
        cardTypeName.setCellValueFactory((TableColumn.CellDataFeatures<SelectedDiscount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBankCard() != null
                                ? param.getValue().getDataBankCard().getRefBankCardType().getTypeName() : "-", param.getValue().dataBankCardProperty()));
        cardTypeName.setMinWidth(140);

        tableSelectedDiscount.getColumns().addAll(discountName, bankName, cardTypeName, cardClassName);

        ObservableList<SelectedDiscount> selectedDiscountItems = FXCollections.observableArrayList(loadAllDataSelectedDiscount());

        cbpSelectedDiscount = new JFXCComboBoxTablePopup<>(
                SelectedDiscount.class, tableSelectedDiscount, selectedDiscountItems, "", "Diskon *", true, 580, 350
        );
        
        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpSelectedDiscount, 0.0);
        AnchorPane.setLeftAnchor(cbpSelectedDiscount, 0.0);
        AnchorPane.setRightAnchor(cbpSelectedDiscount, 0.0);
        AnchorPane.setTopAnchor(cbpSelectedDiscount, 0.0);
        ancCBRBDTLayout.getChildren().add(cbpSelectedDiscount);
    }

    private List<SelectedDiscount> loadAllDataSelectedDiscount() {
        List<SelectedDiscount> list = new ArrayList<>();
        //data null
        SelectedDiscount dataNullDiscount = new SelectedDiscount(null, null);
        list.add(dataNullDiscount);
        //data hotel-event
        SelectedDiscount dataHotelDiscount = new SelectedDiscount(null, reservationController.getFReservationManager().getReservationBillDiscountType(0));  //Hotel-Event = '0'
        list.add(dataHotelDiscount);
        //data card-event
        List<TblBankCard> dataBankCards = reservationController.getFReservationManager().getAllDataBankCard();
        for (TblBankCard dataBankCard : dataBankCards) {
            //data bank
            dataBankCard.setTblBank(reservationController.getFReservationManager().getBank(dataBankCard.getTblBank().getIdbank()));
            //data bank card type
            dataBankCard.setRefBankCardType(reservationController.getFReservationManager().getDataBankCardType(dataBankCard.getRefBankCardType().getIdtype()));
            //data selected discount
            SelectedDiscount dataCardDiscount = new SelectedDiscount(dataBankCard, reservationController.getFReservationManager().getReservationBillDiscountType(1));  //Card-Event = '1'
            list.add(dataCardDiscount);
        }
        return list;
    }

    private void refreshDataPopup() {
        //Selected Discount
        ObservableList<SelectedDiscount> selectedDiscountItems = FXCollections.observableArrayList(loadAllDataSelectedDiscount());
        cbpSelectedDiscount.setItems(selectedDiscountItems);
    }

    public class SelectedDiscount {

        private final ObjectProperty<TblBankCard> dataBankCard = new SimpleObjectProperty<>();

        private final ObjectProperty<RefReservationBillDiscountType> dataDiscountType = new SimpleObjectProperty<>();

        public SelectedDiscount(TblBankCard dataBankCard,
                RefReservationBillDiscountType dataDiscountType) {

            this.dataBankCard.set(dataBankCard);

            this.dataDiscountType.set(dataDiscountType);

        }

        public ObjectProperty<TblBankCard> dataBankCardProperty() {
            return dataBankCard;
        }

        public TblBankCard getDataBankCard() {
            return dataBankCardProperty().get();
        }

        public void setDataBankCard(TblBankCard dataBankCard) {
            dataBankCardProperty().set(dataBankCard);
        }

        public ObjectProperty<RefReservationBillDiscountType> dataDiscountTypeProperty() {
            return dataDiscountType;
        }

        public RefReservationBillDiscountType getDataDiscountType() {
            return dataDiscountTypeProperty().get();
        }

        public void setDataDiscountType(RefReservationBillDiscountType dataDiscountType) {
            dataDiscountTypeProperty().set(dataDiscountType);
        }

        @Override
        public String toString() {
            if (getDataDiscountType() != null) {
                if (getDataDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                    return "Hotel";
                }
                if (getDataDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    return getDataBankCard() != null ? getDataBankCard().getBankCardName() : "-";
                }
            }
            return "-";
        }

    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        //set selected data (value)
        if (reservationController.dataInputBillStatus.equals("reservation")) { //reservation
            cbpSelectedDiscount.setValue(new SelectedDiscount(
                    reservationController.dataReservationBill.getTblBankCard() != null ? new TblBankCard(reservationController.dataReservationBill.getTblBankCard()) : null,
                    reservationController.dataReservationBill.getRefReservationBillDiscountType() != null ? new RefReservationBillDiscountType(reservationController.dataReservationBill.getRefReservationBillDiscountType()) : null
            ));
        } else {    //checkout
            cbpSelectedDiscount.setValue(new SelectedDiscount(
                    reservationController.dataCheckOutBill.getTblBankCard() != null ? new TblBankCard(reservationController.dataCheckOutBill.getTblBankCard()) : null,
                    reservationController.dataCheckOutBill.getRefReservationBillDiscountType() != null ? new RefReservationBillDiscountType(reservationController.dataCheckOutBill.getRefReservationBillDiscountType()) : null
            ));
        }

        cbpSelectedDiscount.hide();
    }

    private void dataReservationBillDiscountTypeSaveHandle() {
        if (checkDataInputDataReservationBillDiscountType()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //reservation bill (selected - discount)
                if (cbpSelectedDiscount.getValue() != null) {
                    //data bank card
                    if (cbpSelectedDiscount.getValue().getDataBankCard() != null) {
                        if (reservationController.dataInputBillStatus.equals("reservation")) {    //reservation
                            reservationController.dataReservationBill.setTblBankCard(new TblBankCard(cbpSelectedDiscount.getValue().getDataBankCard()));
                        } else {  //checkout
                            reservationController.dataCheckOutBill.setTblBankCard(new TblBankCard(cbpSelectedDiscount.getValue().getDataBankCard()));
                        }
                    } else {
                        if (reservationController.dataInputBillStatus.equals("reservation")) {    //reservation
                            reservationController.dataReservationBill.setTblBankCard(null);
                        } else {  //checkout
                            reservationController.dataCheckOutBill.setTblBankCard(null);
                        }
                    }
                    //data reservation bill discount type
                    if (cbpSelectedDiscount.getValue().getDataDiscountType() != null) {
                        if (reservationController.dataInputBillStatus.equals("reservation")) {    //reservation
                            reservationController.dataReservationBill.setRefReservationBillDiscountType(new RefReservationBillDiscountType(cbpSelectedDiscount.getValue().getDataDiscountType()));
                        } else {  //checkout
                            reservationController.dataCheckOutBill.setRefReservationBillDiscountType(new RefReservationBillDiscountType(cbpSelectedDiscount.getValue().getDataDiscountType()));
                        }
                    } else {
                        if (reservationController.dataInputBillStatus.equals("reservation")) {    //reservation
                            reservationController.dataReservationBill.setRefReservationBillDiscountType(null);
                        } else {  //checkout
                            reservationController.dataCheckOutBill.setRefReservationBillDiscountType(null);
                        }
                    }
                }
                //data reservation room type detail - room price detail
                if (reservationController.dataInputBillStatus.equals("reservation")) {    //reservation
                    for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
                        //reservation room price detail - discount
                        setReservationRoomPriceDetailDiscount(dataRRTDRPD.getTblReservationRoomPriceDetail(), dataRRTDRPD.getTblReservationRoomTypeDetail(), reservationController.dataReservationBill);
                    }
                    for (TblReservationRoomTypeDetail dataRRTD : reservationController.dataReservationRoomTypeDetails) {
                        dataRRTD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    }
                }
                reservationController.tableDataReservationRoomTypeDetail.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationRoomTypeDetails));
                //data additonal item
                if (reservationController.dataInputBillStatus.equals("reservation")) {    //reservation
                    for (TblReservationAdditionalItem dataRAI : reservationController.dataReservationAdditionalItems) {
                        if (dataRAI.getRefReservationBillType().getIdtype() == 0) { //Reservation = '0'
                            //reservation additional item - discount
                            setAdditionalItemDiscount(dataRAI, reservationController.dataReservationBill);
                        }
                    }
                } else {  //checkout
                    for (TblReservationAdditionalItem dataRAI : reservationController.dataReservationAdditionalItems) {
                        if (dataRAI.getRefReservationBillType().getIdtype() == 1) { //CheckOut = '1'
                            //reservation additional item - discount
                            setAdditionalItemDiscount(dataRAI, reservationController.dataCheckOutBill);
                        }
                    }
                }
                reservationController.tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationAdditionalItems));
                //data additional service
                if (reservationController.dataInputBillStatus.equals("reservation")) {    //reservation
                    for (TblReservationAdditionalService dataRAS : reservationController.dataReservationAdditionalServices) {
                        if (dataRAS.getRefReservationBillType().getIdtype() == 0) { //Reservation = '0'
                            //reservation additional service - discount
                            setAdditionalServiceDiscount(dataRAS, reservationController.dataReservationBill);
                        }
                    }
                } else {  //checkout
                    for (TblReservationAdditionalService dataRAS : reservationController.dataReservationAdditionalServices) {
                        if (dataRAS.getRefReservationBillType().getIdtype() == 1) { //CheckOut = '1'
                            //reservation additional service - discount
                            setAdditionalServiceDiscount(dataRAS, reservationController.dataCheckOutBill);
                        }
                    }
                }
                reservationController.tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationAdditionalServices));
                //refresh reservation/checkout bill
                reservationController.refreshDataBill(reservationController.dataInputBillStatus);
                if (reservationController.dataInputStatus == 1) { //update
                    //save data to database
                    if (reservationController.dataReservationSaveHandle(14)) {
                        //close dialog
                        reservationController.dialogStage.close();
                    }
                } else {
                    //refresh button info
                    reservationController.btnReservationBillDiscountType.setText("Diskon (Reservasi) : "
                            + (reservationController.dataReservationBill.getTblBankCard() != null
                                    ? "Kartu (" + reservationController.dataReservationBill.getTblBankCard().getBankCardName() + ")"
                                    : (reservationController.dataReservationBill.getRefReservationBillDiscountType() != null
                                            ? reservationController.dataReservationBill.getRefReservationBillDiscountType().getTypeName()
                                            : "-")));
                    reservationController.btnCheckOutBillDiscountType.setText("Diskon (Check Out) : " + (reservationController.dataCheckOutBill.getTblBankCard() != null
                            ? "Kartu (" + reservationController.dataCheckOutBill.getTblBankCard().getBankCardName() + ")"
                            : (reservationController.dataCheckOutBill.getRefReservationBillDiscountType() != null
                                    ? reservationController.dataCheckOutBill.getRefReservationBillDiscountType().getTypeName()
                                    : "-")));
                    ClassMessage.showSucceedInsertingDataMessage("", reservationController.dialogStage);
                    //close dialog
                    reservationController.dialogStage.close();
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void setReservationRoomPriceDetailDiscount(TblReservationRoomPriceDetail dataReservationRoomPriceDetail,
            TblReservationRoomTypeDetail dataRoomTypeDetail,
            TblReservationBill dataReservationBill) {
        dataReservationRoomPriceDetail.setTblHotelEvent(null);
        dataReservationRoomPriceDetail.setTblBankEventCard(null);
        dataReservationRoomPriceDetail.setDetailDiscountPercentage(new BigDecimal("0"));
        if (dataReservationBill.getRefReservationBillDiscountType() != null) {
            if (dataReservationBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataRoomTypeDetail.getTblRoomType().getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(dataReservationRoomPriceDetail.getDetailDate());
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (dataHotelEvent.getDayOfWeek() != 0) {
                                if (dataHotelEvent.getDayOfWeek() == (LocalDate.of(dataReservationRoomPriceDetail.getDetailDate().getYear() + 1900,
                                        dataReservationRoomPriceDetail.getDetailDate().getMonth() + 1,
                                        dataReservationRoomPriceDetail.getDetailDate().getDate()).getDayOfWeek().getValue())) {
                                    if (dataReservationRoomPriceDetail.getDetailPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                        if ((dataReservationRoomPriceDetail.getDetailPrice().multiply(dataHotelEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                                .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                            dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                            dataReservationRoomPriceDetail.setDetailDiscountPercentage((dataHotelEvent.getDiscountNominal().multiply(new BigDecimal("100"))).divide(dataReservationRoomPriceDetail.getDetailPrice()));
                                            break;
                                        } else {
                                            dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                            dataReservationRoomPriceDetail.setDetailDiscountPercentage(dataHotelEvent.getRoomDiscountPercentage());
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if (dataReservationRoomPriceDetail.getDetailPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                    if ((dataReservationRoomPriceDetail.getDetailPrice().multiply(dataHotelEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                        dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage((dataHotelEvent.getDiscountNominal().multiply(new BigDecimal("100"))).divide(dataReservationRoomPriceDetail.getDetailPrice()));
                                        break;
                                    } else {
                                        dataReservationRoomPriceDetail.setTblHotelEvent(dataHotelEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage(dataHotelEvent.getRoomDiscountPercentage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (dataReservationBill.getRefReservationBillDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    if (dataRoomTypeDetail.getTblRoomType().getCardDiscountable()) {
                        List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
                                dataReservationRoomPriceDetail.getDetailDate(),
                                dataReservationBill.getTblBankCard().getIdbankCard(),
                                dataReservationBill.getTblBankCard().getTblBank().getIdbank());
                        if (!dataCardEvents.isEmpty()) {
                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
                                if (dataReservationRoomPriceDetail.getDetailPrice().compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                    if ((dataReservationRoomPriceDetail.getDetailPrice().multiply(dataCardEvent.getRoomDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                        dataReservationRoomPriceDetail.setTblBankEventCard(dataCardEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage((dataCardEvent.getDiscountNominal().multiply(new BigDecimal("100"))).divide(dataReservationRoomPriceDetail.getDetailPrice()));
                                        break;
                                    } else {
                                        dataReservationRoomPriceDetail.setTblBankEventCard(dataCardEvent);
                                        dataReservationRoomPriceDetail.setDetailDiscountPercentage(dataCardEvent.getRoomDiscountPercentage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setAdditionalItemDiscount(TblReservationAdditionalItem dataAdditionalItem,
            TblReservationBill dataRCOBill) {
        dataAdditionalItem.setDiscountPercentage(new BigDecimal("0"));
        if (dataRCOBill.getRefReservationBillDiscountType() != null) {
            if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataAdditionalItem.getTblItem().getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxItemDiscountPercentage(dataAdditionalItem.getAdditionalDate());
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if ((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity())).compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                if ((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity()))
                                        .multiply(dataHotelEvent.getItemDiscountPercentage().divide(new BigDecimal("100")))
                                        .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                    dataAdditionalItem.setTblHotelEvent(dataHotelEvent);
                                    dataAdditionalItem.setDiscountPercentage(dataHotelEvent.getItemDiscountPercentage());
                                    break;
                                } else {
                                    dataAdditionalItem.setTblHotelEvent(dataHotelEvent);
                                    dataAdditionalItem.setDiscountPercentage(dataHotelEvent.getItemDiscountPercentage());
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    if (dataAdditionalItem.getTblItem().getCardDiscountable()) {
                        List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxItemDiscountPercentage(
                                dataAdditionalItem.getAdditionalDate(),
                                dataRCOBill.getTblBankCard().getIdbankCard(),
                                dataRCOBill.getTblBankCard().getTblBank().getIdbank());
                        if (!dataCardEvents.isEmpty()) {
                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
                                if ((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity())).compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                    if (((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity()))
                                            .multiply(dataCardEvent.getItemDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                        dataAdditionalItem.setTblBankEventCard(dataCardEvent);
                                        dataAdditionalItem.setDiscountPercentage(dataCardEvent.getItemDiscountPercentage());
                                        break;
                                    } else {
                                        dataAdditionalItem.setTblBankEventCard(dataCardEvent);
                                        dataAdditionalItem.setDiscountPercentage(dataCardEvent.getItemDiscountPercentage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setAdditionalServiceDiscount(TblReservationAdditionalService dataAdditionalService,
            TblReservationBill dataRCOBill) {
        dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
        if (dataRCOBill.getRefReservationBillDiscountType() != null) {
            if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataAdditionalService.getTblRoomService().getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationController.getFReservationManager().getAllDataHotelEventByDateInRangeOrderByMaxServiceDiscountPercentage(dataAdditionalService.getAdditionalDate());
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (dataAdditionalService.getPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                if ((dataAdditionalService.getPrice().multiply(dataHotelEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                        .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                    dataAdditionalService.setTblHotelEvent(dataHotelEvent);
                                    dataAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
                                    break;
                                } else {
                                    dataAdditionalService.setTblHotelEvent(dataHotelEvent);
                                    dataAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    if (dataAdditionalService.getTblRoomService().getCardDiscountable()) {
                        List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxServiceDiscountPercentage(
                                dataAdditionalService.getAdditionalDate(),
                                dataRCOBill.getTblBankCard().getIdbankCard(),
                                dataRCOBill.getTblBankCard().getTblBank().getIdbank());
                        if (!dataCardEvents.isEmpty()) {
                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
                                if (dataAdditionalService.getPrice().compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                    if ((dataAdditionalService.getPrice().multiply(dataCardEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                        dataAdditionalService.setTblBankEventCard(dataCardEvent);
                                        dataAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
                                        break;
                                    } else {
                                        dataAdditionalService.setTblBankEventCard(dataCardEvent);
                                        dataAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void dataReservationBillDiscountTypeCancelHandle() {
        if (reservationController.dataInputStatus == 1) {   //update
            //refresh data
            reservationController.refreshDataSelectedReservation();
        }
        //close form data reservation bill discount type input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationBillDiscountType() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpSelectedDiscount.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Diskon : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataReservationBillDiscountType();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationBillDiscountTypeInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
