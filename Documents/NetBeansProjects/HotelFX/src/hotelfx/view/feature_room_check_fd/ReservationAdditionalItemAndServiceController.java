/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblRoomService;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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
public class ReservationAdditionalItemAndServiceController implements Initializable {

    @FXML
    private AnchorPane ancReservationRoomAdditionalItemAndServiceLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalItemLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalServiceLayout;

    @FXML
    private Label lblDataCode;

    @FXML
    private JFXButton btnBack;

    private void initFormDataReservationAdditionalItemAndService() {
        dataReservationBill = roomStatusFDController.getServiceRV2().getReservationBillByIDReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getIdreservation());
        
        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            dataReservationAdditionalItemAndServiceCancelHandle();
        });
    }

    public TblReservationBill dataReservationBill;
    
    private void setSelectedDataToInputForm() {
        lblDataCode.setText("Kamar "
                + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRoomName()
                + " (" + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getCodeReservation()
                + " : "
                + roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation().getTblCustomer().getTblPeople().getFullName() + ")");

        //data reservation room additional
        initTableDataReservationAdditionalItem();
        initTableDataReservationAdditionalService();
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL ITEM
     */
    public ClassTableWithControl tableDataReservationAdditionalItem;

    private void initTableDataReservationAdditionalItem() {
        //set table
        setTableDataReservationAdditionalItem();
        //set control
        setTableControlDataReservationAdditionalItem();
        //set table-control to content-view
        ancReservationRoomAdditionalItemLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalItem, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalItem, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalItem, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalItem, 0.0);

        ancReservationRoomAdditionalItemLayout.getChildren().add(tableDataReservationAdditionalItem);
    }

    private void setTableDataReservationAdditionalItem() {
        TableView<TblReservationAdditionalItem> tableView = new TableView();

        TableColumn<TblReservationAdditionalItem, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(), param.getValue().tblReservationRoomTypeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationAdditionalItem, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate()), param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblReservationAdditionalItem, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge()), param.getValue().itemChargeProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalCharge = new TableColumn("Total Biaya");
        totalAdditionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity())),
                        param.getValue().idadditionalProperty()));
        totalAdditionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalDiscount = new TableColumn("Total Diskon");
        totalAdditionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat((param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity()))
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        totalAdditionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(),
                        param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, additionalDate, itemName, additionalCharge, itemQuantity, itemUnit, totalAdditionalCharge, totalAdditionalDiscount, additionalType);
        tableView.setItems(loadAllDataReservationAdditionalItem());
        tableDataReservationAdditionalItem = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationAdditionalItem() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationAdditionalItemCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationAdditionalItemDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationAdditionalItem.addButtonControl(buttonControls);
    }

    public ObservableList<TblReservationAdditionalItem> loadAllDataReservationAdditionalItem() {
        List<TblReservationAdditionalItem> list = roomStatusFDController.getServiceRV2().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail.getIddetail());
        for (TblReservationAdditionalItem data : list) {
            //data rrtd
            data.setTblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
            //data item
            data.setTblItem(roomStatusFDController.getServiceRV2().getDataItem(data.getTblItem().getIditem()));
            //data hotel event
            if (data.getTblHotelEvent() != null) {
                data.setTblHotelEvent(roomStatusFDController.getServiceRV2().getHotelEvent(data.getTblHotelEvent().getIdevent()));
            }
            //data card event
            if (data.getTblBankEventCard() != null) {
                data.setTblBankEventCard(roomStatusFDController.getServiceRV2().getBankEventCard(data.getTblBankEventCard().getIdevent()));
            }
            //data reservation bill type
            data.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(data.getRefReservationBillType().getIdtype()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL SERVICE
     */
    public ClassTableWithControl tableDataReservationAdditionalService;

    private void initTableDataReservationAdditionalService() {
        //set table
        setTableDataReservationAdditionalService();
        //set control
        setTableControlDataReservationAdditionalService();
        //set table-control to content-view
        ancReservationRoomAdditionalServiceLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalService, 0.0);

        ancReservationRoomAdditionalServiceLayout.getChildren().add(tableDataReservationAdditionalService);
    }

    private void setTableDataReservationAdditionalService() {
        TableView<TblReservationAdditionalService> tableView = new TableView();

        TableColumn<TblReservationAdditionalService, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(), param.getValue().tblReservationRoomTypeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationAdditionalService, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblRoomService().getIdroomService() != 1
                                ? ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate())
                                : ClassFormatter.dateFormate.format(Date.valueOf(
                                                LocalDate.of(
                                                        param.getValue().getAdditionalDate().getYear() + 1900,
                                                        param.getValue().getAdditionalDate().getMonth() + 1,
                                                        param.getValue().getAdditionalDate().getDate())
                                                .plusDays(1))),
                        param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> serviceName = new TableColumn("Layanan");
        serviceName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomService().getServiceName(), param.getValue().tblRoomServiceProperty()));
        serviceName.setMinWidth(140);

        TableColumn<TblReservationAdditionalService, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()), param.getValue().priceProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalDiscount = new TableColumn("Diskon");
        additionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        additionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(), param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalNote = new TableColumn("Keterangan");
        additionalNote.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> (param.getValue().getRestoTransactionNumber() != null
                                ? "No. Transaksi : " + param.getValue().getRestoTransactionNumber() : "")
                        + (param.getValue().getNote() != null
                                ? param.getValue().getNote() : (param.getValue().getRestoTransactionNumber() != null
                                        ? "" : "-")),
                        param.getValue().noteProperty()));
        additionalNote.setMinWidth(200);

        tableView.getColumns().addAll(codeDetail, additionalDate, serviceName, additionalCharge, additionalDiscount, additionalType, additionalNote);
        tableView.setItems(loadAllDataReservationAdditionalService());
        tableDataReservationAdditionalService = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationAdditionalService() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationAdditionalServiceCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationAdditionalServiceDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationAdditionalService.addButtonControl(buttonControls);
    }

    public ObservableList<TblReservationAdditionalService> loadAllDataReservationAdditionalService() {
        List<TblReservationAdditionalService> list = roomStatusFDController.getServiceRV2().getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail.getIddetail());
        for (TblReservationAdditionalService data : list) {
            //data rrtd
            data.setTblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
            //data room service
            data.setTblRoomService(roomStatusFDController.getServiceRV2().getRoomService(data.getTblRoomService().getIdroomService()));
            //data hotel event
            if (data.getTblHotelEvent() != null) {
                data.setTblHotelEvent(roomStatusFDController.getServiceRV2().getHotelEvent(data.getTblHotelEvent().getIdevent()));
            }
            //data card event
            if (data.getTblBankEventCard() != null) {
                data.setTblBankEventCard(roomStatusFDController.getServiceRV2().getBankEventCard(data.getTblBankEventCard().getIdevent()));
            }
            //data reservation bill type
            data.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(data.getRefReservationBillType().getIdtype()));
        }
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblRoomService().getIdroomService() == 6 //Room Dining = '6'
                    || list.get(i).getTblRoomService().getIdroomService() == 7) {   //Dine in Resto = '7'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * HANDLE DIALOG
     */
    public Stage dialogStage;

    /**
     * insert = '0' update = '1'
     */
    public int dataInputStatus;

    /**
     * HANDLE FOR DATA INPUT RESERVATION ADDITIONAL ITEM
     */
    public TblReservationAdditionalItem selectedAdditionalItem;
    
    public List<TblReservationAdditionalItem> selectedAdditionalItems;

    private void dataReservationAdditionalItemCreateHandle() {
        //set selected data
        selectedAdditionalItem = new TblReservationAdditionalItem();
        selectedAdditionalItem.setItemCharge(new BigDecimal("0"));
        selectedAdditionalItem.setItemQuantity(new BigDecimal("0"));
        selectedAdditionalItem.setDiscountPercentage(new BigDecimal("0"));
        selectedAdditionalItem.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(0));    //Reservasi = '0'
        selectedAdditionalItems = new ArrayList<>();
        //open form data reservation additional item dialog
        showReservationAdditionalItemDialog();
    }

    private void dataReservationAdditionalItemDeleteHandle() {
        if (tableDataReservationAdditionalItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedAdditionalItem = (TblReservationAdditionalItem) tableDataReservationAdditionalItem.getTableView().getSelectionModel().getSelectedItem();
            if (selectedAdditionalService.getRefReservationBillType().getIdtype() == 4) { //Include = '4'
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
                        "Tidak dapat menghapus data tambahan layanan dengan status 'Include'..!", null);
            } else {
//                if ((calculationTotalRestOfBill()
//                        .subtract(calculationTotalAdditionalItem(selectedAdditionalItem, dataReservationBill))).compareTo(new BigDecimal("0")) == -1) {
//                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN BARANG",
//                            "Data tambahan barang yang telah dibayar tidak dapat dihapus..!", null);
//                } else {
                if (!(LocalDateTime.of(selectedAdditionalItem.getAdditionalDate().getYear() + 1900,
                        selectedAdditionalItem.getAdditionalDate().getMonth() + 1,
                        selectedAdditionalItem.getAdditionalDate().getDate(),
                        roomStatusFDController.defaultCheckOutTime.getHours(),
                        roomStatusFDController.defaultCheckOutTime.getMinutes(),
                        0).plusDays(1)).isBefore(LocalDateTime.now())) {
                    Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                    if (alert.getResult() == ButtonType.OK) {
                        //dummy entry
                        TblReservation dummySelectedData = new TblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
                        TblReservationAdditionalItem dummySelectedAdditionalItem = new TblReservationAdditionalItem(selectedAdditionalItem);
                        dummySelectedAdditionalItem.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummySelectedAdditionalItem.getTblReservationRoomTypeDetail()));
                        dummySelectedAdditionalItem.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                        dummySelectedAdditionalItem.setTblItem(new TblItem(dummySelectedAdditionalItem.getTblItem()));
                        //delete..
                        dummySelectedAdditionalItem.setRefRecordStatus(new RefRecordStatus(dummySelectedAdditionalItem.getRefRecordStatus()));
                        if (roomStatusFDController.getServiceRV2().deleteDataReservationAddtionalItem(dummySelectedData, dummySelectedAdditionalItem)) {
                            ClassMessage.showSucceedDeletingDataMessage("", roomStatusFDController.dialogStage);
                            //refresh data additional item
                            tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(loadAllDataReservationAdditionalItem()));
//                                    // refresh all data and close form data - reservation additional item
//                                    roomStatusFDController.setSelectedDataToInputForm();
//                                    roomStatusFDController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedDeletingDataMessage(roomStatusFDController.getServiceRV2().getErrorMessage(), roomStatusFDController.dialogStage);
                        }
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sudah lewat (hari pemesanan) tidak dapat dihapus..!", null);
                }
//                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private BigDecimal calculationTotalAdditionalItem(TblReservationAdditionalItem addtionalItem,
            TblReservationBill bill) {
        BigDecimal result = new BigDecimal("0");
        if (addtionalItem != null) {
            result = ((new BigDecimal("1")).subtract(addtionalItem.getDiscountPercentage()))
                    .multiply(addtionalItem.getItemQuantity().multiply(addtionalItem.getItemCharge()));
            BigDecimal service = bill.getServiceChargePercentage().multiply(result);
            BigDecimal tax = bill.getTaxPercentage().multiply((result.add(service)));
            result = result.add(service).add(tax);
        }
        return result;
    }

    private void showReservationAdditionalItemDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationAdditionalItemInputDialog.fxml"));

            ReservationAdditionalItemInputController controller = new ReservationAdditionalItemInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

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
     * HANDLE FOR DATA INPUT RESERVATION ADDITIONAL SERVICE
     */
    public TblReservationAdditionalService selectedAdditionalService;
    
    public List<TblReservationAdditionalService> selectedAdditionalServices;

    private void dataReservationAdditionalServiceCreateHandle() {
        //set selected data
        selectedAdditionalService = new TblReservationAdditionalService();
        selectedAdditionalService.setPrice(new BigDecimal("0"));
        selectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
        selectedAdditionalService.setRefReservationBillType(roomStatusFDController.getServiceRV2().getDataReservationBillType(0));    //Reservasi = '0'
        selectedAdditionalServices = new ArrayList<>();
        //open form data reservation additional service dialog
        showReservationAdditionalServiceDialog();
    }

    private void dataReservationAdditionalServiceDeleteHandle() {
        if (tableDataReservationAdditionalService.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedAdditionalService = (TblReservationAdditionalService) tableDataReservationAdditionalService.getTableView().getSelectionModel().getSelectedItem();
            if (selectedAdditionalService.getRefReservationBillType().getIdtype() == 4) { //Include = '4'
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
                        "Tidak dapat menghapus data tambahan layanan dengan status 'Include'..!", null);
            } else {
//                if ((calculationTotalRestOfBill()
//                        .subtract(calculationTotalAdditionalService(selectedAdditionalService, dataReservationBill))).compareTo(new BigDecimal("0")) == -1) {
//                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
//                            "Data tambahan layanan yang telah dibayar tidak dapat dihapus..!", null);
//                } else {
                if (!(LocalDateTime.of(selectedAdditionalService.getAdditionalDate().getYear() + 1900,
                        selectedAdditionalService.getAdditionalDate().getMonth() + 1,
                        selectedAdditionalService.getAdditionalDate().getDate(),
                        roomStatusFDController.defaultCheckOutTime.getHours(),
                        roomStatusFDController.defaultCheckOutTime.getMinutes(),
                        0).plusDays(1)).isBefore(LocalDateTime.now())) {
                    if (selectedAdditionalService.getTblRoomService().getIdroomService() != 2 //early checkin = '2'
                            && selectedAdditionalService.getTblRoomService().getIdroomService() != 3 //late checkout = '3'
                            && selectedAdditionalService.getTblRoomService().getIdroomService() != 4 //bouns voucher = '4'
                            && selectedAdditionalService.getTblRoomService().getIdroomService() != 5) {  //canceling fee = '5'
                        Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                        if (alert.getResult() == ButtonType.OK) {
                            //dummy entry
                            TblReservation dummySelectedData = new TblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
                            TblReservationAdditionalService dummySelectedAdditionalService = new TblReservationAdditionalService(selectedAdditionalService);
                            dummySelectedAdditionalService.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummySelectedAdditionalService.getTblReservationRoomTypeDetail()));
                            dummySelectedAdditionalService.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                            dummySelectedAdditionalService.setTblRoomService(new TblRoomService(dummySelectedAdditionalService.getTblRoomService()));
                            //delete..
                            dummySelectedAdditionalService.setRefRecordStatus(new RefRecordStatus(dummySelectedAdditionalService.getRefRecordStatus()));
                            if (roomStatusFDController.getServiceRV2().deleteDataReservationAddtionalService(dummySelectedData, dummySelectedAdditionalService)) {
                                ClassMessage.showSucceedDeletingDataMessage("", roomStatusFDController.dialogStage);
                                //refresh data additional service
                                tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(loadAllDataReservationAdditionalService()));
//                                    // refresh all data and close form data - reservation additional service
//                                    roomStatusFDController.setSelectedDataToInputForm();
//                                    roomStatusFDController.dialogStage.close();
                            } else {
                                ClassMessage.showFailedDeletingDataMessage(roomStatusFDController.getServiceRV2().getErrorMessage(), roomStatusFDController.dialogStage);
                            }
                        }
                    } else {
                        HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data buatan sistem tidak dapat dihapus (tambahan biaya 'early checkin', 'late checkout', atau 'voucher(bonus)')..!", null);
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sudah lewat (hari pemesanan) tidak dapat dihapus..!", null);
                }
//                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private BigDecimal calculationTotalAdditionalService(TblReservationAdditionalService addtionalService,
            TblReservationBill bill) {
        BigDecimal result = new BigDecimal("0");
        if (addtionalService != null) {
            result = ((new BigDecimal("1")).subtract(addtionalService.getDiscountPercentage()))
                    .multiply(addtionalService.getPrice());
            BigDecimal service = new BigDecimal("0");
            BigDecimal tax = new BigDecimal("0");
            if (addtionalService.getTblRoomService().getIdroomService() != 6 //Room Dining = '6'
                    && addtionalService.getTblRoomService().getIdroomService() != 7) {   //Dine in Resto = '7'
                service = bill.getServiceChargePercentage().multiply(result);
                tax = bill.getTaxPercentage().multiply((result.add(service)));
            }
            result = result.add(service).add(tax);
        }
        return result;
    }

    private void showReservationAdditionalServiceDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/ReservationAdditionalServiceInputDialog.fxml"));

            ReservationAdditionalServiceInputController controller = new ReservationAdditionalServiceInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

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

    private void dataReservationAdditionalItemAndServiceCancelHandle() {
        //refresh all data and close form data - reservation additional service
        roomStatusFDController.setSelectedDataToInputForm();
        roomStatusFDController.dialogStage.close();
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
        initFormDataReservationAdditionalItemAndService();

        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationAdditionalItemAndServiceController(RoomStatusFDController parentController) {
        roomStatusFDController = parentController;
    }

    private final RoomStatusFDController roomStatusFDController;

    public RoomStatusFDController getParentController(){
        return roomStatusFDController;
    }
    
}
