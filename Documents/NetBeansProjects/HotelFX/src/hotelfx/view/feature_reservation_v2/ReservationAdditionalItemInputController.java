/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

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
 * @author ANDRI
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
    private AnchorPane ancRRTDCodeLayout;
    private JFXCComboBoxTablePopup<TblReservationRoomTypeDetail> cbpRRTD;

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
                txtAdditionalCharge,
                cbpRRTD);
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

        //Reservation Room Type Detail
        TableView<TblReservationRoomTypeDetail> tableRRTD = new TableView<>();

        TableColumn<TblReservationRoomTypeDetail, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeDetail(), param.getValue().codeDetailProperty()));
        codeDetail.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeName = new TableColumn("Tipe Kamar");
        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
        roomTypeName.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                                : param.getValue().getTblReservationCheckInOut().getTblRoom() != null
                                        ? param.getValue().getTblReservationCheckInOut().getTblRoom().getRoomName() : "",
                        param.getValue().tblReservationCheckInOutProperty()));
        roomName.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> checkInDate = new TableColumn("Tanggal Check In");
        checkInDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckInDateTime()), param.getValue().checkInDateTimeProperty()));
        checkInDate.setMinWidth(180);

        TableColumn<TblReservationRoomTypeDetail, String> checkOutDate = new TableColumn("Tanggal Check Out");
        checkOutDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckOutDateTime()), param.getValue().checkOutDateTimeProperty()));
        checkOutDate.setMinWidth(180);

        tableRRTD.getColumns().addAll(codeDetail, roomTypeName, roomName, checkInDate, checkOutDate);

        ObservableList<TblReservationRoomTypeDetail> rrtdItems = FXCollections.observableArrayList(loadAllDataReservationRoomTypeDetail());

        cbpRRTD = new JFXCComboBoxTablePopup<>(
                TblReservationRoomTypeDetail.class, tableRRTD, rrtdItems, "", "Kode Reservasi *", true, 750, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpItem, 0.0);
        AnchorPane.setLeftAnchor(cbpItem, 0.0);
        AnchorPane.setRightAnchor(cbpItem, 0.0);
        AnchorPane.setTopAnchor(cbpItem, 0.0);
        ancItemLayout.getChildren().clear();
        ancItemLayout.getChildren().add(cbpItem);
        AnchorPane.setBottomAnchor(cbpRRTD, 0.0);
        AnchorPane.setLeftAnchor(cbpRRTD, 0.0);
        AnchorPane.setRightAnchor(cbpRRTD, 0.0);
        AnchorPane.setTopAnchor(cbpRRTD, 0.0);
        ancRRTDCodeLayout.getChildren().clear();
        ancRRTDCodeLayout.getChildren().add(cbpRRTD);
    }

    private void refreshDataPopup() {
        //Item
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());
        cbpItem.setItems(itemItems);

        //Reservation Room Type Detail
        ObservableList<TblReservationRoomTypeDetail> rrtdItems = FXCollections.observableArrayList(loadAllDataReservationRoomTypeDetail());
        cbpRRTD.setItems(rrtdItems);

    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list;
            list = reservationController.getFReservationManager().getAllDataItem();
            for (int i = list.size() - 1; i > -1; i--) {
                //data unit
                list.get(i).setTblUnit(reservationController.getFReservationManager().getDataUnit(list.get(i).getTblUnit().getIdunit()));
                if (!list.get(i).getGuestStatus() //!Guest
                        || !list.get(i).getLeasedStatus()) {
                    list.remove(i);
                }
            }
        return list;
    }

    private List<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetail() {
        List<TblReservationRoomTypeDetail> list = new ArrayList<>();
        for(TblReservationRoomTypeDetail dataReservationRoomTypeDetail : reservationController.dataReservationRoomTypeDetails){
            if(dataReservationRoomTypeDetail.getTblReservationCheckInOut() != null
                    || dataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 3    //Checked Out = '3'
                    || dataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 4){ //Tidak Berlaku = '4'
                list.add(dataReservationRoomTypeDetail);
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
            //        LocalDate dateCount = ((java.sql.Date) reservationController.selectedAditionalItem.getTblReservationRoomTypeDetail().getCheckInDateTime()).toLocalDate();
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
                                        reservationController.defaultCheckOutTime.getHours(),
                                        reservationController.defaultCheckOutTime.getMinutes(),
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

        Bindings.bindBidirectional(txtAdditionalCharge.textProperty(), reservationController.selectedAdditionalItem.itemChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemNumber.textProperty(), reservationController.selectedAdditionalItem.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        txtAvailableStock.setText("");
        txtAvailableStock.setVisible(false);
        lblUnit.setText("");

        reservationController.selectedAdditionalItem.itemQuantityProperty().addListener((obs, oldVal, newVal) -> {
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
            reservationController.selectedAdditionalItem.setItemCharge(newVal != null ? newVal.getAdditionalCharge() : new BigDecimal("0"));
//            reservationController.selectedAdditionalItem.setItemQuantity(new BigDecimal("1"));
            //refresh data table
            initDataTableDateSelected(cbpRRTD.getValue(), newVal);
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
        
        cbpRRTD.valueProperty().addListener((obs, oldVal, newVal) -> {
            //refresh data table
            initDataTableDateSelected(newVal, cbpItem.getValue());
            if (cbpItem.getValue() != null) {
                if (!cbpItem.getValue().getPropertyStatus()) {   //Property
                    txtAvailableStock.setText(ClassFormatter.decimalFormat.cFormat(calculationPredictLastStock(null)));
                }
            }
        });

        cbpItem.valueProperty().bindBidirectional(reservationController.selectedAdditionalItem.tblItemProperty());
        cbpRRTD.valueProperty().bindBidirectional(reservationController.selectedAdditionalItem.tblReservationRoomTypeDetailProperty());

        cbpItem.hide();
        cbpRRTD.hide();
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
                        totalStockWantToReserved = reservationController.selectedAdditionalItem.getItemQuantity() != null
                                ? reservationController.selectedAdditionalItem.getItemQuantity() : new BigDecimal("0");
                        break;
                    }
                }
                BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReserved(cbpItem.getValue(), date); //Item, Date, Status = 'Reserved'
                BigDecimal totalStockHasBeenDone = getTotalStockHasBeenDone(cbpItem.getValue(), date);
                BigDecimal totalStockMustBeStockInAllRoom = getTotalStockMustBeStockInAllRoom(cbpItem.getValue());
                BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), cbpRRTD.getValue());
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
                        totalStockWantToReserved = totalStockWantToReserved.add(reservationController.selectedAdditionalItem.getItemQuantity() != null
                                ? reservationController.selectedAdditionalItem.getItemQuantity() : new BigDecimal("0"));
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
                BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), cbpRRTD.getValue());
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
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                reservationController.selectedAdditionalItems = new ArrayList<>();
                //save and set data additional item
                reservationController.selectedAdditionalItem.setTaxable(reservationController.selectedAdditionalItem.getTblItem().getTaxable());
                for (RTDListDate dataDate : tabelAdditionalDate.getItems()) {
                    if (dataDate.getSelectedDate()) {
                        TblReservationAdditionalItem dataAddtional = new TblReservationAdditionalItem();
                        dataAddtional.setTblReservationRoomTypeDetail(reservationController.selectedAdditionalItem.getTblReservationRoomTypeDetail());
                        dataAddtional.setTblItem(reservationController.selectedAdditionalItem.getTblItem());
                        dataAddtional.setItemCharge(reservationController.selectedAdditionalItem.getItemCharge());
                        dataAddtional.setItemQuantity(reservationController.selectedAdditionalItem.getItemQuantity());
                        dataAddtional.setAdditionalDate(Date.valueOf(dataDate.getDataDate()));
                        dataAddtional.setRefReservationBillType(new RefReservationBillType(reservationController.selectedAdditionalItem.getRefReservationBillType()));
                        //set reservation additional item - discount
                        setAdditionalItemDiscount(dataAddtional, reservationController.dataReservationBill);
                        dataAddtional.setTaxable(reservationController.selectedAdditionalItem.getTaxable());
                        reservationController.selectedAdditionalItems.add(dataAddtional);
                        reservationController.dataReservationAdditionalItems.add(dataAddtional);
                    }
                }
                reservationController.tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(reservationController.dataReservationAdditionalItems));
                //update data bill
                reservationController.refreshDataBill();
                //save data to database
                if (reservationController.dataReservationSaveHandle(7)) {
                    //close form data addtional input input
                    reservationController.dialogStage.close();
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
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
                        List<TblBankEventCard> dataCardEvents = reservationController.getFReservationManager().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxItemDiscountPercentage(
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
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data additional item input
        reservationController.dialogStage.close();
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
            if (reservationController.selectedAdditionalItem.getItemQuantity()
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
            if (reservationController.selectedAdditionalItem.getItemCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Tambahan : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (cbpRRTD.getValue() == null) {
            dataInput = false;
            errDataInput += "Kode Reservasi Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
                        BigDecimal totalStockWantToReserved = reservationController.selectedAdditionalItem.getItemQuantity();
                        BigDecimal totalStockHasBeenReserved = getTotalStockHasBeenReserved(cbpItem.getValue(), data.getDataDate()); //Item, Date, Status = 'Reserved'
                        BigDecimal totalStockHasBeenDone = getTotalStockHasBeenDone(cbpItem.getValue(), data.getDataDate());
                        BigDecimal totalStockMustBeStockInAllRoom = getTotalStockMustBeStockInAllRoom(cbpItem.getValue());
                        BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), cbpRRTD.getValue());
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
                        totalStockWantToReserved = totalStockWantToReserved.add(reservationController.selectedAdditionalItem.getItemQuantity());
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
                BigDecimal totalStockMustBeStockInThisRoom = getTotalStockMustBeStockInThisRoom(cbpItem.getValue(), cbpRRTD.getValue());
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
            List<TblItemLocation> itemLocations = reservationController.getFReservationManager().getAllDataItemLocationByIDItem(dataItem.getIditem());
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
            List<TblReservationAdditionalItemReserved> rairs = reservationController.getFReservationManager().getAllDataReservationAdditionalItemReservedByIDItemAndIDRAIRStatus(
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
            List<TblReservationAdditionalItemReserved> rairs = reservationController.getFReservationManager().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateAndIDRAIRStatus(
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
            List<TblReservationAdditionalItemReserved> rairs = reservationController.getFReservationManager().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateAndIDRAIRStatus(
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
            List<TblRoomType> roomTypes = reservationController.getFReservationManager().getAllDataRoomType();
            for (TblRoomType roomType : roomTypes) {
                BigDecimal subResult = new BigDecimal("0");
                //Room Type - Item (in current situation)
                List<TblRoomTypeItem> roomTypeItems = reservationController.getFReservationManager().getAllDataRoomTypeItemByIDRoomTypeAndIDItem(roomType.getIdroomType(), dataItem.getIditem());
                for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                    subResult = subResult.add(roomTypeItem.getItemQuantity());
                }
                List<TblRoom> rooms = reservationController.getFReservationManager().getAllDataRoomByIDRoomType(roomType.getIdroomType());
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
//            List<TblRoomTypeItem> roomTypeItems = reservationController.getFReservationManager().getAllDataRoomTypeItemByIDRoomTypeAndIDItem(dataRRTD.getTblRoomType().getIdroomType(), dataItem.getIditem());
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
            List<TblReservationAdditionalItemReserved> rairs = reservationController.getFReservationManager().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInRangedAndIDRAIRStatus(
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
            List<TblReservationAdditionalItemReserved> rairs = reservationController.getFReservationManager().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInAfterAndIDRAIRStatus(
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
            List<TblReservationAdditionalItemReserved> rairs = reservationController.getFReservationManager().getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInAfterAndIDRAIRStatus(
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
    
    public ReservationAdditionalItemInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;
    
}
