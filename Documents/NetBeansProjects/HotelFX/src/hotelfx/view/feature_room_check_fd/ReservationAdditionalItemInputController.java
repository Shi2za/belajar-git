/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalItemReserved;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ReservationAdditionalItemInputController implements Initializable {

    @FXML
    private AnchorPane ancFormAdditionalItem;

    @FXML
    private GridPane gpFormDataAdditionalItem;

    @FXML
    private JFXTextField txtAdditionalCharge;

    @FXML
    private JFXTextField txtAvailableStock;

    @FXML
    private JFXTextField txtItemNumber;

    @FXML
    private Label lblUnit;

    @FXML
    private AnchorPane ancItemLayout;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    @FXML
    private AnchorPane ancTabelDateLayout;

    private TableView<RTDListDate> tabelAdditionalDate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataAdditionalItem() {
        initDataAdditionalItemPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Tambahan Barang)"));
        btnSave.setOnAction((e) -> {
            dataAdditionalItemSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataAdditionalItemCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();

        initDataTableDateSelected(null, null);

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpItem,
                txtItemNumber,
                txtAdditionalCharge);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAdditionalCharge,
                txtItemNumber);
    }

    private void initDataAdditionalItemPopup() {
        //Item
        TableView<TblItem> tableItem = new TableView<>();

        TableColumn<TblItem, String> codeItem = new TableColumn<>("ID");
        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemName.setMinWidth(140);

        tableItem.getColumns().addAll(codeItem, itemName);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 300, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpItem, 0.0);
        AnchorPane.setLeftAnchor(cbpItem, 0.0);
        AnchorPane.setRightAnchor(cbpItem, 0.0);
        AnchorPane.setTopAnchor(cbpItem, 0.0);
        ancItemLayout.getChildren().clear();
        ancItemLayout.getChildren().add(cbpItem);
    }

    private void refreshDataPopup() {
        //Item
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());
        cbpItem.setItems(itemItems);
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list;
        list = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataItem();
        for (int i = list.size() - 1; i > -1; i--) {
            //data unit
            list.get(i).setTblUnit(reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getDataUnit(list.get(i).getTblUnit().getIdunit()));
            if (!list.get(i).getGuestStatus() //!Guest
                    || !list.get(i).getLeasedStatus()) {
                list.remove(i);
            }
        }
        return list;
    }

    private void initDataTableDateSelected(TblReservationRoomTypeDetail dataRRTD,
            TblItem dataItem) {
        tabelAdditionalDate = new TableView();
        tabelAdditionalDate.setEditable(true);

        TableColumn<RTDListDate, String> dataDate = new TableColumn("Tanggal");
        dataDate.setCellValueFactory((TableColumn.CellDataFeatures<RTDListDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(Date.valueOf(param.getValue().getDataDate())), param.getValue().dataDateProperty()));
        dataDate.setMinWidth(140);

        TableColumn<RTDListDate, Boolean> selectedDate = new TableColumn("Buat");
        selectedDate.setCellValueFactory(cellData -> cellData.getValue().selectedDateProperty());
        selectedDate.setMinWidth(80);
        selectedDate.setCellFactory(CheckBoxTableCell.forTableColumn(selectedDate));
        selectedDate.setEditable(true);

        TableColumn<RTDListDate, String> availableStock = new TableColumn("Stok (Tersedia)");
        availableStock.setCellValueFactory((TableColumn.CellDataFeatures<RTDListDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getAvailableStock()),
                        param.getValue().availableStockProperty()));
        availableStock.setMinWidth(140);

        if (dataItem != null
                && dataItem.getPropertyStatus()) { //Property
            tabelAdditionalDate.getColumns().addAll(dataDate, availableStock, selectedDate);
        } else {
            tabelAdditionalDate.getColumns().addAll(dataDate, selectedDate);
        }
        tabelAdditionalDate.setItems(FXCollections.observableArrayList(loadAllDataRTDListDate(dataRRTD, dataItem)));

//        tabelAdditionalDate.setRowFactory(tv -> {
//            TableRow<RTDListDate> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    RTDListDate rowData = row.getItem();
//                    rowData.setSelectedDate(!rowData.getSelectedDate());
//                }
//            });
//            return row;
//        });
        AnchorPane.setBottomAnchor(tabelAdditionalDate, 0.0);
        AnchorPane.setTopAnchor(tabelAdditionalDate, 0.0);
        AnchorPane.setRightAnchor(tabelAdditionalDate, 0.0);
        AnchorPane.setLeftAnchor(tabelAdditionalDate, 0.0);
        ancTabelDateLayout.getChildren().add(tabelAdditionalDate);
    }

    private List<RTDListDate> loadAllDataRTDListDate(TblReservationRoomTypeDetail dataRRTD,
            TblItem dataItem) {
        List<RTDListDate> list = new ArrayList<>();
        if (dataRRTD != null) {
            //        LocalDate dateCount = ((java.sql.Date) reservationAdditionalItemAndServiceController.selectedAditionalItem.getTblReservationRoomTypeDetail().getCheckInDateTime()).toLocalDate();
            LocalDate dateCount = LocalDate.parse(
                    (new SimpleDateFormat("yyyy-MM-dd")).format(dataRRTD.getCheckInDateTime())
                    + "T"
                    + ((new SimpleDateFormat("HH:mm:ss")).format(dataRRTD.getCheckInDateTime())),
                    DateTimeFormatter.ISO_DATE_TIME);
            LocalDate dateLast = LocalDate.parse(
                    (new SimpleDateFormat("yyyy-MM-dd")).format(dataRRTD.getCheckOutDateTime())
                    + "T"
                    + ((new SimpleDateFormat("HH:mm:ss")).format(dataRRTD.getCheckOutDateTime())),
                    DateTimeFormatter.ISO_DATE_TIME);
            dateCount = dateCount.minusDays(0);
            while (dateCount.isBefore(dateLast)) {
                if (LocalDateTime.now().isBefore(
                        LocalDateTime.of(dateCount.plusDays(1), LocalTime.of(
                                        reservationAdditionalItemAndServiceController.getParentController().defaultCheckOutTime.getHours(),
                                        reservationAdditionalItemAndServiceController.getParentController().defaultCheckOutTime.getMinutes(),
                                        0)))) {
                    RTDListDate data;
                    if (dataItem != null
                            && dataItem.getPropertyStatus()) { //Property
                        data = new RTDListDate(dateCount, false, calculationPredictLastStock(dateCount));
                    } else {
                        data = new RTDListDate(dateCount, false);
                    }
                    list.add(data);
                }
                dateCount = dateCount.plusDays(1);
            }
        }
        return list;
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        Bindings.bindBidirectional(txtAdditionalCharge.textProperty(), reservationAdditionalItemAndServiceController.selectedAdditionalItem.itemChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemNumber.textProperty(), reservationAdditionalItemAndServiceController.selectedAdditionalItem.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        txtAvailableStock.setText("");
        txtAvailableStock.setVisible(false);
        lblUnit.setText("");

        reservationAdditionalItemAndServiceController.selectedAdditionalItem.itemQuantityProperty().addListener((obs, oldVal, newVal) -> {
            if (cbpItem.getValue() != null) {
                if (cbpItem.getValue().getPropertyStatus()) {   //Property
                    for (RTDListDate data : tabelAdditionalDate.getItems()) {
                        data.setAvailableStock(calculationPredictLastStock(data.getDataDate()));
                    }
                } else {  //another type
                    //refresh data available stock (another type)
                    txtAvailableStock.setText(ClassFormatter.decimalFormat.cFormat(calculationPredictLastStock(null)));
                }
            }
        });

        cbpItem.valueProperty().addListener((obs, olVal, newVal) -> {
//            txtItemName.setText(newVal.getItemName());
            reservationAdditionalItemAndServiceController.selectedAdditionalItem.setItemCharge(newVal != null ? newVal.getAdditionalCharge() : new BigDecimal("0"));
//            reservationAdditionalItemAndServiceController.selectedAdditionalItem.setItemQuantity(new BigDecimal("1"));
            //refresh data table
            initDataTableDateSelected(reservationAdditionalItemAndServiceController.getParentController().selectedDataRoomTypeDetail, newVal);
            if (newVal != null
                    && newVal.getTblUnit() != null) {
                if (!newVal.getPropertyStatus()) {   //Property
                    txtAvailableStock.setText(ClassFormatter.decimalFormat.cFormat(calculationPredictLastStock(null)));
                    txtAvailableStock.setVisible(true);
                } else {
                    txtAvailableStock.setText("");
                    txtAvailableStock.setVisible(false);
                }
                lblUnit.setText(newVal.getTblUnit().getUnitName());
            } else {
                txtAvailableStock.setText("");
                txtAvailableStock.setVisible(false);
                lblUnit.setText("");
            }
        });

        cbpItem.valueProperty().bindBidirectional(reservationAdditionalItemAndServiceController.selectedAdditionalItem.tblItemProperty());

        cbpItem.hide();
    }

    private BigDecimal calculationPredictLastStock(LocalDate date) {
        BigDecimal totalPredictLastStock = new BigDecimal("0");
        if (cbpItem.getValue() != null) {
            if (cbpItem.getValue().getPropertyStatus()) { //Property
                BigDecimal totalCurrentStock = getTotalCurrentStock(cbpItem.getValue());    //total stock in room, warehouse, laundry
                BigDecimal totalStockWantToReserved = new BigDecimal("0");
                for (RTDListDate data : tabelAdditionalDate.getItems()) {
                    if (data.getDataDate().equals(date)
                            && data.getSelectedDate()) {
                        totalStockWantToReserved = reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity() != null
                                ? reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity() : new BigDecimal("0");
                        break;
                    }
                }
                BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReserved(cbpItem.getValue(), date); //Item, Date, Status = 'Reserved'
                BigDecimal totalStockHasBeenDone = getTotalStockHasBeenDone(cbpItem.getValue(), date);
                BigDecimal totalStockMustBeStockInAllRoom = getTotalStockMustBeStockInAllRoom(cbpItem.getValue());
                BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), reservationAdditionalItemAndServiceController.getParentController().selectedDataRoomTypeDetail);
                totalPredictLastStock = totalCurrentStock
                        .subtract(totalStockWantToReserved)
                        .subtract(totalStockHasBeenReserved)
                        .add(totalStockHasBeenDone)
                        .subtract(totalStockMustBeStockInAllRoom)
                        .subtract(totalStockMustBeStockInThisRoom);
            } else {    //Another type (item)
                BigDecimal totalCurrentStock = getTotalCurrentStock(cbpItem.getValue());    //total stock in room, warehouse, laundry
                BigDecimal totalStockWantToReserved = new BigDecimal("0");
                for (RTDListDate data : tabelAdditionalDate.getItems()) {
                    if (data.getSelectedDate()) {
                        totalStockWantToReserved = totalStockWantToReserved.add(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity() != null
                                ? reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity() : new BigDecimal("0"));
                    }
                }
//                BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReservedInAfterDate(
//                        cbpItem.getValue(),
//                        getReservedStartDate(tabelAdditionalDate.getItems())
//                ); //Item, Date(start), Date(end), Status = 'Reserved'
                BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReserved(
                        cbpItem.getValue()
                ); //Item, Status = 'Reserved'
                BigDecimal totalStockHasBeenDone = getTotalStockHasBeenDoneInAfterDate(
                        cbpItem.getValue(),
                        getReservedStartDate(tabelAdditionalDate.getItems())
                );
                BigDecimal totalStockMustBeStockInAllRoom = getTotalStockMustBeStockInAllRoom(cbpItem.getValue());
                BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), reservationAdditionalItemAndServiceController.getParentController().selectedDataRoomTypeDetail);
                System.out.println("all stock : " + totalCurrentStock);
                System.out.println("want to reserved : " + totalStockWantToReserved);
                System.out.println("has been reserved : " + totalStockHasBeenReserved);
                System.out.println("has been done : " + totalStockHasBeenDone);
                System.out.println("must be in all room : " + totalStockMustBeStockInAllRoom);
                System.out.println("must be in this room : " + totalStockMustBeStockInThisRoom);
                totalPredictLastStock = totalCurrentStock
                        .subtract(totalStockWantToReserved)
                        .subtract(totalStockHasBeenReserved)
                        .add(totalStockHasBeenDone)
                        .subtract(totalStockMustBeStockInAllRoom)
                        .subtract(totalStockMustBeStockInThisRoom);
                System.out.println("predict : " + totalPredictLastStock);
            }
        }
        return totalPredictLastStock;
    }

    private void dataAdditionalItemSaveHandle() {
        if (checkDataInputDataAdditionalItem()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationAdditionalItemAndServiceController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                reservationAdditionalItemAndServiceController.selectedAdditionalItems = new ArrayList<>();
                //save and set data additional item
                reservationAdditionalItemAndServiceController.selectedAdditionalItem.setTaxable(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getTblItem().getTaxable());
                for (RTDListDate dataDate : tabelAdditionalDate.getItems()) {
                    if (dataDate.getSelectedDate()) {
                        TblReservationAdditionalItem dataAddtional = new TblReservationAdditionalItem();
                        dataAddtional.setTblReservationRoomTypeDetail(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getTblReservationRoomTypeDetail());
                        dataAddtional.setTblItem(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getTblItem());
                        dataAddtional.setItemCharge(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemCharge());
                        dataAddtional.setItemQuantity(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity());
                        dataAddtional.setAdditionalDate(Date.valueOf(dataDate.getDataDate()));
                        dataAddtional.setRefReservationBillType(new RefReservationBillType(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getRefReservationBillType()));
                        //set reservation additional item - discount
                        setAdditionalItemDiscount(dataAddtional, reservationAdditionalItemAndServiceController.dataReservationBill);
                        dataAddtional.setTaxable(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getTaxable());
                        reservationAdditionalItemAndServiceController.selectedAdditionalItems.add(dataAddtional);
                    }
                }
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(reservationAdditionalItemAndServiceController.getParentController().selectedDataRoomTypeDetail.getTblReservation());
                List<TblReservationAdditionalItem> dummyReservationAdditionalItems = new ArrayList<>();
                for (TblReservationAdditionalItem selectedAdditionalItem : reservationAdditionalItemAndServiceController.selectedAdditionalItems) {
                    TblReservationAdditionalItem dummyReservationAdditionalItem = new TblReservationAdditionalItem(selectedAdditionalItem);
                    dummyReservationAdditionalItem.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummyReservationAdditionalItem.getTblReservationRoomTypeDetail()));
                    dummyReservationAdditionalItem.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                    dummyReservationAdditionalItem.setTblItem(new TblItem(dummyReservationAdditionalItem.getTblItem()));
                    dummyReservationAdditionalItems.add(dummyReservationAdditionalItem);
                }
                if (reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().insertDataReservationAddtionalItems(dummySelectedData, dummyReservationAdditionalItems)) {
                    ClassMessage.showSucceedInsertingDataMessage("", reservationAdditionalItemAndServiceController.dialogStage);
                    // refresh all data and close form data - reservation additional service
                    reservationAdditionalItemAndServiceController.tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(reservationAdditionalItemAndServiceController.loadAllDataReservationAdditionalItem()));
                    reservationAdditionalItemAndServiceController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getErrorMessage(), reservationAdditionalItemAndServiceController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationAdditionalItemAndServiceController.dialogStage);
        }
    }

    private void setAdditionalItemDiscount(TblReservationAdditionalItem dataAdditionalItem,
            TblReservationBill dataRCOBill) {
        dataAdditionalItem.setDiscountPercentage(new BigDecimal("0"));
        if (dataRCOBill.getRefReservationBillDiscountType() != null) {
            if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataAdditionalItem.getTblItem().getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataHotelEventByDateInRangeOrderByMaxItemDiscountPercentage(dataAdditionalItem.getAdditionalDate());
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if ((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity()))
                                    .compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                if (((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity()))
                                        .multiply(dataHotelEvent.getItemDiscountPercentage().divide(new BigDecimal("100"))))
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
                        List<TblBankEventCard> dataCardEvents = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxItemDiscountPercentage(
                                dataAdditionalItem.getAdditionalDate(),
                                dataRCOBill.getTblBankCard().getIdbankCard(),
                                dataRCOBill.getTblBankCard().getTblBank().getIdbank());
                        if (!dataCardEvents.isEmpty()) {
                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
                                if ((dataAdditionalItem.getItemCharge().multiply(dataAdditionalItem.getItemQuantity()))
                                        .compareTo(dataCardEvent.getMinTransaction()) > -1) {
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

    private void dataAdditionalItemCancelHandle() {
        //close form data additional item input
        reservationAdditionalItemAndServiceController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataAdditionalItem() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtItemNumber.getText() == null
                || txtItemNumber.getText().equals("")
                || txtItemNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah Barang : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (!checkAvailableStock()) { //stock not available
                    dataInput = false;
                    errDataInput += errAvailableStock;
                }
            }
        }
        if (txtAdditionalCharge.getText() == null
                || txtAdditionalCharge.getText().equals("")
                || txtAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Tambahan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Tambahan : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (!checkSelectedDate()) {
            dataInput = false;
            errDataInput += "Tanggal : Tidak ada data yang dipilih \n";
        }
        return dataInput;
    }

    private boolean checkSelectedDate() {
        boolean selected = false;
        for (RTDListDate data : tabelAdditionalDate.getItems()) {
            if (data.getSelectedDate()) {
                selected = true;
                break;
            }
        }
        return selected;
    }

    private String errAvailableStock;

    private boolean checkAvailableStock() {
        boolean avalable = true;
        errAvailableStock = "";
        if (cbpItem.getValue() != null) {
            if (cbpItem.getValue().getPropertyStatus()) {   //Property
                for (RTDListDate data : tabelAdditionalDate.getItems()) {
                    if (data.getSelectedDate()) {
                        BigDecimal totalCurrentStock = getTotalCurrentStock(cbpItem.getValue());    //total stock in room, warehouse, laundry
                        BigDecimal totalStockWantToReserved = reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity();
                        BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReserved(cbpItem.getValue(), data.getDataDate()); //Item, Date, Status = 'Reserved'
                        BigDecimal totalStockHasBeenDone = getTotalStockHasBeenDone(cbpItem.getValue(), data.getDataDate());
                        BigDecimal totalStockMustBeStockInAllRoom = getTotalStockMustBeStockInAllRoom(cbpItem.getValue());
                        BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), reservationAdditionalItemAndServiceController.getParentController().selectedDataRoomTypeDetail);
                        BigDecimal totalPredictLastStock = totalCurrentStock
                                .subtract(totalStockWantToReserved)
                                .subtract(totalStockHasBeenReserved)
                                .add(totalStockHasBeenDone)
                                .subtract(totalStockMustBeStockInAllRoom)
                                .subtract(totalStockMustBeStockInThisRoom);
                        if (totalPredictLastStock
                                .compareTo(new BigDecimal("0")) == -1) {
                            avalable = false;
                            errAvailableStock += "- " + ClassFormatter.dateFormate.format(data.getDataDate()) + " : kurang " + ClassFormatter.decimalFormat.cFormat(totalPredictLastStock.abs()) + "\n";
                        }
                    }
                }
                if (!errAvailableStock.equals("")) {
                    errAvailableStock = "Stok barang tidak mencukupi, \n"
                            + "untuk melakukan pemesanan barang pada tanggal : \n"
                            + errAvailableStock;
                }
            } else {  //Another Type..
                BigDecimal totalCurrentStock = getTotalCurrentStock(cbpItem.getValue());    //total stock in room, warehouse, laundry
                BigDecimal totalStockWantToReserved = new BigDecimal("0");
                for (RTDListDate data : tabelAdditionalDate.getItems()) {
                    if (data.getSelectedDate()) {
                        totalStockWantToReserved = totalStockWantToReserved.add(reservationAdditionalItemAndServiceController.selectedAdditionalItem.getItemQuantity());
                    }
                }
//                BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReservedInAfterDate(
//                        cbpItem.getValue(),
//                        getReservedStartDate(tabelAdditionalDate.getItems())
//                ); //Item, Date(start), Date(end), Status = 'Reserved'
                BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReserved(
                        cbpItem.getValue()
                ); //Item, Status = 'Reserved'
                BigDecimal totalStockHasBeenDone = getTotalStockHasBeenDoneInAfterDate(
                        cbpItem.getValue(),
                        getReservedStartDate(tabelAdditionalDate.getItems())
                );
                BigDecimal totalStockMustBeStockInAllRoom = getTotalStockMustBeStockInAllRoom(cbpItem.getValue());
                BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), reservationAdditionalItemAndServiceController.getParentController().selectedDataRoomTypeDetail);
                BigDecimal totalPredictLastStock = totalCurrentStock
                        .subtract(totalStockWantToReserved)
                        .subtract(totalStockHasBeenReserved)
                        .add(totalStockHasBeenDone)
                        .subtract(totalStockMustBeStockInAllRoom)
                        .subtract(totalStockMustBeStockInThisRoom);
                if (totalPredictLastStock
                        .compareTo(new BigDecimal("0")) == -1) {
                    avalable = false;
                    errAvailableStock += "Stock Barang tidak mencukupi : kurang " + ClassFormatter.decimalFormat.cFormat(totalPredictLastStock.abs()) + "\n";
                }
            }
        }
        return avalable;
    }

    //total stock in room, warehouse, laundry
    private BigDecimal getTotalCurrentStock(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null) {
            List<TblItemLocation> itemLocations = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataItemLocationByIDItem(dataItem.getIditem());
            for (TblItemLocation itemLocation : itemLocations) {
                if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4) { //Bin = '4'
                    result = result.add(itemLocation.getItemQuantity());
                }
            }
        }
        return result;
    }

    private BigDecimal getTotalStockHasBeenReserved(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null) {
            List<TblReservationAdditionalItemReserved> rairs = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataReservationAdditionalItemReservedByIDItemAndIDRAIRStatus(
                    dataItem.getIditem(),
                    1); //Reserved = '1'
            for (TblReservationAdditionalItemReserved rair : rairs) {
                result = result.add(rair.getReservedQuantity());
            }
        }
        return result;
    }

    private BigDecimal getTotalStockHasBeenReserved(TblItem dataItem,
            LocalDate date) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && date != null) {
            List<TblReservationAdditionalItemReserved> rairs = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateAndIDRAIRStatus(
                    dataItem.getIditem(),
                    Date.valueOf(date),
                    1); //Reserved = '1'
            for (TblReservationAdditionalItemReserved rair : rairs) {
                result = result.add(rair.getReservedQuantity());
            }
        }
        return result;
    }

    private BigDecimal getTotalStockHasBeenDone(TblItem dataItem,
            LocalDate date) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && date != null) {
            List<TblReservationAdditionalItemReserved> rairs = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateAndIDRAIRStatus(
                    dataItem.getIditem(),
                    Date.valueOf(date),
                    1); //Reserved = '1'
            for (TblReservationAdditionalItemReserved rair : rairs) {
                result = result.add(rair.getDoneQuantity());
            }
        }
        return result;
    }

    private BigDecimal getTotalStockMustBeStockInAllRoom(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null) {
            List<TblRoomType> roomTypes = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataRoomType();
            for (TblRoomType roomType : roomTypes) {
                BigDecimal subResult = new BigDecimal("0");
                //Room Type - Item (in current situation)
                List<TblRoomTypeItem> roomTypeItems = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataRoomTypeItemByIDRoomTypeAndIDItem(roomType.getIdroomType(), dataItem.getIditem());
                for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                    subResult = subResult.add(roomTypeItem.getItemQuantity());
                }
                List<TblRoom> rooms = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataRoomByIDRoomType(roomType.getIdroomType());
                subResult = subResult.multiply(new BigDecimal(rooms.size()));
                result = result.add(subResult);
            }
        }
        return result;
    }

    private BigDecimal getTotalStockMustBeStockInThisRoom(TblItem dataItem,
            TblReservationRoomTypeDetail dataRRTD) {
        BigDecimal result = new BigDecimal("0");
//        if (dataItem != null
//                && dataRRTD != null) {
//            //Room Type - Item (in current situation)
//            List<TblRoomTypeItem> roomTypeItems = reservationAdditionalItemAndServiceController.getFReservationManager().getAllDataRoomTypeItemByIDRoomTypeAndIDItem(dataRRTD.getTblRoomType().getIdroomType(), dataItem.getIditem());
//            for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
//                result = result.add(roomTypeItem.getItemQuantity());
//            }
//        }
        return result;
    }

    private LocalDate getReservedStartDate(List<RTDListDate> rdDListDates) {
        LocalDate date = null;
        for (RTDListDate rdDListDate : rdDListDates) {
            if (rdDListDate.getSelectedDate()) {
                if (date == null) {
                    date = rdDListDate.getDataDate();
                } else {
                    if (rdDListDate.getDataDate().isBefore(date)) {
                        date = rdDListDate.getDataDate();
                    }
                }
            }
        }
        return date;
    }

    private LocalDate getReservedEndDate(List<RTDListDate> rdDListDates) {
        LocalDate date = null;
        for (RTDListDate rdDListDate : rdDListDates) {
            if (rdDListDate.getSelectedDate()) {
                if (date == null) {
                    date = rdDListDate.getDataDate();
                } else {
                    if (rdDListDate.getDataDate().isAfter(date)) {
                        date = rdDListDate.getDataDate();
                    }
                }
            }
        }
        return date;
    }

    private BigDecimal getTotalStockHasBeenReservedInRangedDate(
            TblItem dataItem,
            LocalDate startDate,
            LocalDate endDate) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && startDate != null
                && endDate != null) {
            List<TblReservationAdditionalItemReserved> rairs = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInRangedAndIDRAIRStatus(
                    dataItem.getIditem(),
                    Date.valueOf(startDate),
                    Date.valueOf(endDate),
                    1); //Reserved = '1'
            for (TblReservationAdditionalItemReserved rair : rairs) {
                result = result.add(rair.getReservedQuantity());
            }
        }
        return result;
    }

    private BigDecimal getTotalStockHasBeenReservedInAfterDate(
            TblItem dataItem,
            LocalDate date) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && date != null) {
            List<TblReservationAdditionalItemReserved> rairs = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInAfterAndIDRAIRStatus(
                    dataItem.getIditem(),
                    Date.valueOf(date),
                    1); //Reserved = '1'
            for (TblReservationAdditionalItemReserved rair : rairs) {
                result = result.add(rair.getReservedQuantity());
            }
        }
        return result;
    }

    private BigDecimal getTotalStockHasBeenDoneInAfterDate(
            TblItem dataItem,
            LocalDate date) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && date != null) {
            List<TblReservationAdditionalItemReserved> rairs = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInAfterAndIDRAIRStatus(
                    dataItem.getIditem(),
                    Date.valueOf(date),
                    1); //Reserved = '1'
            for (TblReservationAdditionalItemReserved rair : rairs) {
                result = result.add(rair.getDoneQuantity());
            }
        }
        return result;
    }

    class RTDListDate {

        private final ObjectProperty<LocalDate> dataDate = new SimpleObjectProperty<>();

        private final BooleanProperty selectedDate = new SimpleBooleanProperty();

        private final ObjectProperty<BigDecimal> availableStock = new SimpleObjectProperty<>();

        public RTDListDate() {

        }

        public RTDListDate(LocalDate date, boolean selected) {
            dataDate.set(date);
            selectedDate.set(selected);
            availableStock.set(new BigDecimal("0"));

            selectedDate.addListener((obs, oldVal, newVal) -> {
                //asset
                availableStock.set(calculationPredictLastStock(dataDate.get()));
                //another type
                txtAvailableStock.setText(ClassFormatter.decimalFormat.cFormat(calculationPredictLastStock(null)));
            });
        }

        public RTDListDate(LocalDate date, boolean selected, BigDecimal as) {
            dataDate.set(date);
            selectedDate.set(selected);
            availableStock.set(as);

            selectedDate.addListener((obs, oldVal, newVal) -> {
                //asset
                availableStock.set(calculationPredictLastStock(dataDate.get()));
                //another type
                txtAvailableStock.setText(ClassFormatter.decimalFormat.cFormat(calculationPredictLastStock(null)));
            });
        }

        public ObjectProperty<LocalDate> dataDateProperty() {
            return dataDate;
        }

        public LocalDate getDataDate() {
            return dataDateProperty().get();
        }

        public void setDataDate(LocalDate date) {
            dataDateProperty().set(date);
        }

        public BooleanProperty selectedDateProperty() {
            return selectedDate;
        }

        public boolean getSelectedDate() {
            return selectedDateProperty().get();
        }

        public void setSelectedDate(boolean selected) {
            selectedDateProperty().set(selected);
        }

        public ObjectProperty<BigDecimal> availableStockProperty() {
            return availableStock;
        }

        public BigDecimal getAvailableStock() {
            return availableStockProperty().get();
        }

        public void setAvailableStock(BigDecimal availableStock) {
            availableStockProperty().set(availableStock);
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
        initFormDataAdditionalItem();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationAdditionalItemInputController(ReservationAdditionalItemAndServiceController parentController) {
        reservationAdditionalItemAndServiceController = parentController;
    }

    private final ReservationAdditionalItemAndServiceController reservationAdditionalItemAndServiceController;

}
