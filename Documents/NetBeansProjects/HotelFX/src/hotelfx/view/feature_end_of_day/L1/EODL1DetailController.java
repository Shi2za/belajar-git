/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_end_of_day.L1;

import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class EODL1DetailController implements Initializable {

    @FXML
    private AnchorPane ancReservationRoomTypeDetailLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalServiceLayout;

    @FXML
    private AnchorPane ancReservationRoomAdditionalItemLayout;

    public List<TblReservationRoomTypeDetail> dataReservationRoomTypeDetails;

    public List<TblReservationRoomTypeDetailRoomPriceDetail> dataReservationRoomTypeDetailRoomPriceDetails;

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dataReservationRoomTypeDetailTravelAgentDiscountDetails;

    public List<TblReservationAdditionalItem> dataReservationAdditionalItems;

    public List<TblReservationAdditionalService> dataReservationAdditionalServices;

    /**
     * INIT ALL DATA -LAYOUT
     */
    private void initDataReservationDetail(){
        //load all data
        loadDataReservation(0);
        //init reservation room type detail
        initTableDataReservationRoomTypeDetail();
        //init reservation addtional service
        initTableDataReservationAdditionalService();
        //init reservation addtional item
        initTableDataReservationAdditionalItem();
    }
    
    /**
     * DATA RESERVATION
     */
    private void loadDataReservation(long idReservation) {
        //data reservation room type detail
        dataReservationRoomTypeDetails = parentController.getService().getAllDataReservationRoomTypeDetailByIDReservation(selectedData.getIdreservation());
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                data.setTblReservationCheckInOut(parentController.getService().getDataReservationCheckInOut(data.getTblReservationCheckInOut().getIdcheckInOut()));
                if (data.getTblReservationCheckInOut().getTblRoom() != null) {
                    data.getTblReservationCheckInOut().setTblRoom(parentController.getService().getDataRoom(data.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                }
            }
        }
        //data reservation additional item
        dataReservationAdditionalItems = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationAdditionalItems.addAll(parentController.getService().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        //data reservation additional service
        dataReservationAdditionalServices = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationAdditionalServices.addAll(parentController.getService().getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        //data reservation room type detail - room price detail
        dataReservationRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationRoomTypeDetailRoomPriceDetails.addAll(parentController.getService().getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            data.setTblReservationRoomTypeDetail(parentController.getService().getDataReservationRoomTypeDetail(data.getTblReservationRoomTypeDetail().getIddetail()));
            data.setTblReservationRoomPriceDetail(parentController.getService().getDataReservationRoomPriceDetail(data.getTblReservationRoomPriceDetail().getIddetail()));
        }
        //data reservation room type detail - travel agent discount detail
        dataReservationRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationRoomTypeDetailTravelAgentDiscountDetails.addAll(parentController.getService().getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        for (TblReservationRoomTypeDetailTravelAgentDiscountDetail data : dataReservationRoomTypeDetailTravelAgentDiscountDetails) {
            data.setTblReservationRoomTypeDetail(parentController.getService().getDataReservationRoomTypeDetail(data.getTblReservationRoomTypeDetail().getIddetail()));
            data.setTblReservationTravelAgentDiscountDetail(parentController.getService().getDataReservationTravelAgentDiscountDetail(data.getTblReservationTravelAgentDiscountDetail().getIddetail()));
        }
    }

    /**
     * TABLE DATA RESERVATION ROOM TYPE DETAIL
     */
    public TableView tableDataReservationRoomTypeDetail;

    private void initTableDataReservationRoomTypeDetail() {
        //set table
        setTableDataReservationRoomTypeDetail();
        //set table-control to content-view
        ancReservationRoomTypeDetailLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationRoomTypeDetail, 0.0);

        ancReservationRoomTypeDetailLayout.getChildren().add(tableDataReservationRoomTypeDetail);
    }

    private void setTableDataReservationRoomTypeDetail() {
        TableView<TblReservationRoomTypeDetail> tableView = new TableView();

        TableColumn<TblReservationRoomTypeDetail, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeDetail(), param.getValue().codeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeName = new TableColumn("Tipe Kamar");
        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
        roomTypeName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                : param.getValue().getTblReservationCheckInOut().getTblRoom() != null
                ? param.getValue().getTblReservationCheckInOut().getTblRoom().getRoomName() : "",
                        param.getValue().tblReservationCheckInOutProperty()));
        roomName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> checkInOutStatus = new TableColumn("Status");
        checkInOutStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                : param.getValue().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName(), param.getValue().tblReservationCheckInOutProperty()));
        checkInOutStatus.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> checkInOutStatusInfo = new TableColumn("Keterangan");
        checkInOutStatusInfo.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> getCheckInOutStatusInfo(param.getValue()), param.getValue().tblReservationCheckInOutProperty()));
        checkInOutStatusInfo.setMinWidth(200);

        TableColumn<TblReservationRoomTypeDetail, String> adultNumber = new TableColumn("Dewasa");
        adultNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                : param.getValue().getTblReservationCheckInOut().getAdultNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        adultNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> childNumber = new TableColumn("Anak");
        childNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                : param.getValue().getTblReservationCheckInOut().getChildNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        childNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> acNumberTitle = new TableColumn("Jumlah");
        acNumberTitle.getColumns().addAll(adultNumber, childNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardUsedNumber = new TableColumn("Digunakan");
        cardUsedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                : param.getValue().getTblReservationCheckInOut().getCardUsed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardUsedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> cardMissedNumber = new TableColumn("Rusak");
        cardMissedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                : param.getValue().getTblReservationCheckInOut().getCardMissed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardMissedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> ubGuestCardNumberTitle = new TableColumn("Jumlah Kartu");
        ubGuestCardNumberTitle.getColumns().addAll(cardUsedNumber, cardMissedNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardDeposit = new TableColumn("Deposit");
        cardDeposit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardUsed()))
                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardDeposit.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> cardCharge = new TableColumn("Biaya Kartu");
        cardCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardMissed()))
                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardCharge.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> checkInDate = new TableColumn("Check In");
        checkInDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckInDateTime()), param.getValue().checkInDateTimeProperty()));
        checkInDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> checkOutDate = new TableColumn("Check Out");
        checkOutDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckOutDateTime()), param.getValue().checkOutDateTimeProperty()));
        checkOutDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> ioDateTitle = new TableColumn("Tanggal");
        ioDateTitle.getColumns().addAll(checkInDate, checkOutDate);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCost = new TableColumn("Harga");
        roomTypeDetailCost.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailCost(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCost.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailDiscount = new TableColumn("Diskon");
        roomTypeDetailDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailDiscount(param.getValue())),
                        param.getValue().lastUpdateDateProperty()));
        roomTypeDetailDiscount.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCompliment = new TableColumn("Compliment");
        roomTypeDetailCompliment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailCompliment(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCompliment.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, roomTypeName,
                ioDateTitle,
                roomTypeDetailCost, roomTypeDetailDiscount, roomTypeDetailCompliment,
                //                roomName, checkInOutStatus, acNumberTitle,
                //                ubGuestCardNumberTitle
                checkInOutStatusInfo);

        tableView.setItems(loadAllDataReservationRoomTypeDetail());

        tableDataReservationRoomTypeDetail = tableView;

    }

    public BigDecimal calculationRoomTypeDetailCost(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
            }
        }
        return result;
    }

    public BigDecimal calculationRoomTypeDetailDiscount(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice()
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
            }
        }
        return result;
    }

    private BigDecimal calculationRoomTypeDetailCompliment(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
            }
        }
        return result;
    }

    private String getCheckInOutStatusInfo(TblReservationRoomTypeDetail dataReservationDetail) {
        String result = "";
        if (dataReservationDetail.getTblReservationCheckInOut() != null) {
            result += "Status : " + dataReservationDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName();
            if (dataReservationDetail.getTblReservationCheckInOut().getTblRoom() != null) {
                result += "\n";
                result += "Kamar : " + dataReservationDetail.getTblReservationCheckInOut().getTblRoom().getRoomName();
                if (dataReservationDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 0) {  //Ready to Check In = '0'
                    result += "\n";
                    result += "Jumlah Dewasa/Anak : " + dataReservationDetail.getTblReservationCheckInOut().getAdultNumber() + "/" + dataReservationDetail.getTblReservationCheckInOut().getChildNumber();
                    result += "\n";
                    result += "Jumlah Kartu Digunakan : " + dataReservationDetail.getTblReservationCheckInOut().getCardUsed();
                    result += "\n";
                    result += "Jumlah Kartu Rusak : " + dataReservationDetail.getTblReservationCheckInOut().getCardMissed();
                }
            } else {
                result += "";
            }
        }
        return result;
    }

    private ObservableList<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetail() {
        return FXCollections.observableArrayList(dataReservationRoomTypeDetails);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL ITEM
     */
    public TableView tableDataReservationAdditionalItem;

    private void initTableDataReservationAdditionalItem() {
        //set table
        setTableDataReservationAdditionalItem();
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
        tableDataReservationAdditionalItem = tableView;

    }

    private ObservableList<TblReservationAdditionalItem> loadAllDataReservationAdditionalItem() {
        return FXCollections.observableArrayList(dataReservationAdditionalItems);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL SERVICE
     */
    public TableView tableDataReservationAdditionalService;

    private void initTableDataReservationAdditionalService() {
        //set table
        setTableDataReservationAdditionalService();
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
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(), 
                        param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalNote = new TableColumn("Keterangan");
        additionalNote.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getNote() != null
                ? param.getValue().getNote() : "-",
                        param.getValue().noteProperty()));
        additionalNote.setMinWidth(200);

        tableView.getColumns().addAll(codeDetail, additionalDate, serviceName, additionalCharge, additionalDiscount, additionalType, additionalNote);
        tableView.setItems(loadAllDataReservationAdditionalService());
        tableDataReservationAdditionalService = tableView;

    }

    private ObservableList<TblReservationAdditionalService> loadAllDataReservationAdditionalService() {
        return FXCollections.observableArrayList(dataReservationAdditionalServices);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init data -layout
        initDataReservationDetail();
    }

    public EODL1DetailController(EODL1Controller parentController, TblReservation dataReservation) {
        this.parentController = parentController;
        
        selectedData = dataReservation;
    }

    private final EODL1Controller parentController;
    
    private final TblReservation selectedData;

}
