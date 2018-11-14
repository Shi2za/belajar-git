/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithReservationVoucher;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.view.feature_reservation_v2.rcr.RCRRCTransactionInputController;
import hotelfx.view.feature_reservation_v2.rcr.RCRReservationAdditionalServiceCreateBonusVoucherInputController;
import hotelfx.view.feature_reservation_v2.rcr.RCRReservationRoomTypeDetailInputController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
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
public class ReservationChangeRoomInputController implements Initializable {

    @FXML
    private AnchorPane ancFormReservationChangeRoom;

    @FXML
    private GridPane gpFormDataReservationChangeRoom;

    @FXML
    private TabPane tabPaneChangeRoom;

    @FXML
    private Tab tabChangeRoom;

    @FXML
    private Tab tabChangeRoomBill;

    @FXML
    private Tab tabChangeRoomHistory;

    @FXML
    private TitledPane tlpReservationRoom;

    @FXML
    private AnchorPane ancTableReservationRoomTypeDetailLayout;

    @FXML
    private AnchorPane ancTableReservationAdditionalItemLayout;

    @FXML
    private AnchorPane ancTableReservationAdditionalServiceLayout;

    @FXML
    private JFXTextField txtTotalBilOld;

    @FXML
    private JFXTextField txtTotalBillNew;

    private ToggleGroup groupPersonInCharge;

    @FXML
    private JFXRadioButton rdbHotel;

    @FXML
    private JFXRadioButton rdbCustomer;

    @FXML
    private JFXTextField txtAdditionalDiscountFromHotel;

    @FXML
    private JFXTextField txtTotalDifferentBill;

    @FXML
    private JFXTextField txtServiceChargeAndTax;

    @FXML
    private JFXTextField txtTotalBillMustBePay;
    
    @FXML
    private Label lblRoundingValue;
    
    @FXML
    private JFXTextField txtTotalPayment;

    @FXML
    private AnchorPane ancTableReservationTransactionLayout;

    @FXML
    private GridPane gpHistoryOfChangedRoom;

    public int lastCodeNumber;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationChangeRoom() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Perpindahan Kamar)"));
        btnSave.setOnAction((e) -> {
            dataReservationChangeRoomSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationChangeRoomCancelHandle();
        });

        initImportantFieldColor();

        groupPersonInCharge = new ToggleGroup();
        rdbHotel.setToggleGroup(groupPersonInCharge);
        rdbCustomer.setToggleGroup(groupPersonInCharge);

        rdbHotel.selectedProperty().addListener((obs, oldVal, newVal) -> {
            refreshBill();
        });
        rdbCustomer.selectedProperty().addListener((obs, oldVal, newVal) -> {
            refreshBill();
        });

        rdbHotel.setSelected(true);
        rdbCustomer.setSelected(false);

        lastCodeNumber = getLastCodeNumber(reservationController.selectedDataRoomTypeDetail.getTblReservation().getIdreservation());

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                rdbHotel,
                rdbCustomer);
    }

    private int getLastCodeNumber(long idReservation) {
        int result = 0;
        if (idReservation != 0L) {
            List<TblReservationRoomTypeDetail> dataRRTD = reservationController.getFReservationManager().getAllDataReservationRoomTypeDetailByIDReservation(idReservation);
            if (!dataRRTD.isEmpty()) {
                result = Integer.parseInt(dataRRTD.get(dataRRTD.size() - 1).getCodeDetail());
            }
        }
        return result;
    }

    private BigDecimal calculationRoomTypeDetailCost(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
            }
        }
        return result;
    }

    private BigDecimal calculationRoomTypeDetailDiscount(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice()
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
            }
        }
        return result;
    }

    private BigDecimal calculationTotalBillOld() {
        BigDecimal result = new BigDecimal("0");
        //data reservation room type (detail)
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == reservationController.selectedDataRoomTypeDetail.getIddetail()
                    && !(LocalDateTime.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getDate(),
                            reservationController.defaultCheckInTime.getHours(),
                            reservationController.defaultCheckInTime.getMinutes(),
                            0)).isBefore(reservationController.currentDateForChangeRoomProcess)) {
                result = result.add(((new BigDecimal("1")).subtract(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
            }
        }
        return result;
    }

    private BigDecimal calculationTotalBillNew() {
        BigDecimal result = new BigDecimal("0");
        //data reservation room type (detail)
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
            result = result.add(((new BigDecimal("1")).subtract(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                    .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
        }
        //data additional service (bonus voucher)
        for (TblReservationAdditionalService data : reservationController.selectedAdditionalServices) {
            if (data.getTblRoomService().getIdroomService() == 4) {   //Lainnya (Bonus Voucher) = '4'
                result = result.add(((new BigDecimal("1")).subtract(data.getDiscountPercentage().divide(new BigDecimal("100"))))
                        .multiply(data.getPrice()));
            }
        }
        return result;
    }

    private BigDecimal calculationTotalAdditionalDiscount() {
        BigDecimal result = new BigDecimal("0");
        if (rdbHotel.isSelected()) {
            if (calculationTotalBillNew().compareTo(calculationTotalBillOld()) == 1) {
                result = calculationTotalBillNew().subtract(calculationTotalBillOld());
            }
        }
        return result;
    }

    public BigDecimal calculationTotalDifferentBill() {
        return calculationTotalBillNew()
                .subtract(calculationTotalBillOld())
                .subtract(calculationTotalAdditionalDiscount());
    }

    public BigDecimal calculationTotalServiceChargeAndTax() {
        BigDecimal result = new BigDecimal("0");
        if (rdbCustomer.isSelected()) {
            //data service charge
            BigDecimal serviceCharge = reservationController.dataReservationBill.getServiceChargePercentage().multiply(calculationTotalDifferentBill());
            //data tax
            BigDecimal tax = reservationController.dataReservationBill.getTaxPercentage().multiply(calculationTotalDifferentBill().add(serviceCharge));
            //data service charge + tax
            result = serviceCharge.add(tax);
        }
        return result;
    }

    public BigDecimal calculationTotalBillMustBePay() {
        return rdbCustomer.isSelected()
                ? calculationTotalDifferentBill()
                        .add(calculationTotalServiceChargeAndTax())
                        .add(calculationTotalAdditonalPaymentRoundingValue())
                : new BigDecimal("0");
    }

    public BigDecimal calculationTotalRestOfBill() {
        return calculationTotalBillMustBePay()
                .subtract(calculationTotalAdditonalPayment());
    }

    private BigDecimal calculationTotalAdditonalPayment() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationPayment data : reservationController.selectedDataTransactions) {
            if(data.getRefFinanceTransactionPaymentType().getIdtype() == 16){   //Return = '16'
                result = result.subtract(data.getUnitNominal());
            }else{
                result = result.add(data.getUnitNominal());
            }
        }
        return result;
    }

    private BigDecimal calculationTotalAdditonalPaymentRoundingValue() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationPayment data : reservationController.selectedDataTransactions) {
            result = result.add(data.getRoundingValue());
        }
        return result;
    }
    
    private void setSelectedDataToInputForm() {

        //set data tab pane
        tabPaneChangeRoom.getTabs().clear();
        tabPaneChangeRoom.getTabs().addAll(tabChangeRoom, tabChangeRoomBill);

        tlpReservationRoom.setText("Reservasi Kamar => " + getTextPeriode(reservationController.selectedDataRoomTypeDetail));

        initTableDataReservationRoomTypeDetail();

        initTableDataReservationAdditionalService();

        refreshBill();

        initTableDataReservationTransaction();

        initDataTableHistoryOfChangedRoom();

    }

    public void refreshBill() {
        txtTotalBilOld.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillOld()));
        txtTotalBillNew.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillNew()));
        txtAdditionalDiscountFromHotel.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalDiscount()));
        txtTotalDifferentBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDifferentBill()));
        txtServiceChargeAndTax.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceChargeAndTax()));
        txtTotalBillMustBePay.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillMustBePay()));
        lblRoundingValue.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(calculationTotalAdditonalPaymentRoundingValue()) + ")");
        txtTotalPayment.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditonalPayment()));
    }

    private String getTextPeriode(TblReservationRoomTypeDetail dataRRTD) {
        String result = "";
        if (dataRRTD != null) {
            result += "Periode Pidah Kamar :  ";
            result += ClassFormatter.dateFormate.format(Date.valueOf(reservationController.currentDateForChangeRoomProcess.toLocalDate()));
            result += "  s/d  ";
            result += ClassFormatter.dateFormate.format(dataRRTD.getCheckOutDateTime());

        } else {
            result += "Periode Pindah Kamar :  -  s/d  -";
        }
        return result;
    }

    private void dataReservationChangeRoomSaveHandle() {
        //$$$000
        if (checkDataInputDataReservationChangeRoom()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //set data reservation room price detail - complementary
                setReservationRoomPriceDetailComplimentary();
                //set data reservation room type detail - room price detail - change room history
                setDataRoomTypeDetailRoomPriceDetailChangeRoomHistory();
                //refresh reservation bill
                reservationController.refreshDataBill();
                //save data to database
                if (reservationController.dataReservationSaveHandle(16)) {
                    //close dialog input
                    reservationController.dialogStage.close();
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void setReservationRoomPriceDetailComplimentary() {
        //$$$000
        for (TblReservationRoomTypeDetailRoomPriceDetail dataRTDRPD : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
            BigDecimal newPrice = ((new BigDecimal("1")).subtract(dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                    .multiply(dataRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
            BigDecimal oldPrice = new BigDecimal("0");
            BigDecimal oldComplimentary = new BigDecimal("0");
            for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().getIddetail() == reservationController.selectedDataRoomTypeDetail.getIddetail()
                        && (LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                data.getTblReservationRoomPriceDetail().getDetailDate().getDate())).isEqual(
                                LocalDate.of(dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                        dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                        dataRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
                    oldPrice = oldPrice.add(((new BigDecimal("1")).subtract(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))))
                            .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
                    oldComplimentary = oldComplimentary.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
                    break;
                }
            }
            if (rdbCustomer.isSelected()) {
                dataRTDRPD.getTblReservationRoomPriceDetail().setDetailComplimentary(oldComplimentary);
            } else {
                dataRTDRPD.getTblReservationRoomPriceDetail().setDetailComplimentary(
                        (newPrice.subtract(oldPrice)).compareTo(new BigDecimal("0")) < 1
                                ? oldComplimentary
                                : (newPrice.subtract(oldPrice)).add(oldComplimentary));
            }
        }
    }

    private void setDataRoomTypeDetailRoomPriceDetailChangeRoomHistory() {
        List<TblReservationRoomTypeDetailRoomPriceDetail> tempOldDataReservationRoomTypeDetailRoomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(reservationController.selectedDataRoomTypeDetail.getIddetail());
        for (TblReservationRoomTypeDetailRoomPriceDetail tempOldDataReservationRoomTypeDetailRoomPriceDetail : tempOldDataReservationRoomTypeDetailRoomPriceDetails) {
            //data reservation room type detail
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.setTblReservationRoomTypeDetail(reservationController.getFReservationManager().getReservationRoomTypeDetail(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getIddetail()));
            //data reservation
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().setTblReservation(reservationController.getFReservationManager().getReservation(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
            //data room type
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().setTblRoomType(reservationController.getFReservationManager().getRoomType(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
            //data reservation order by type
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().setRefReservationOrderByType(reservationController.getFReservationManager().getReservationOrderByType(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getRefReservationOrderByType().getIdtype()));
            //data room price detail
            tempOldDataReservationRoomTypeDetailRoomPriceDetail.setTblReservationRoomPriceDetail(reservationController.getFReservationManager().getReservationRoomPriceDetail(tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getIddetail()));
            for (TblReservationRoomTypeDetailRoomPriceDetail selectedDataRoomTypeDetailRoomPriceDetail : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
                if (LocalDate.of(
                        tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                        tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                        tempOldDataReservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getDate()).isEqual(LocalDate.of(
                                        selectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                        selectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                        selectedDataRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
                    TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory dataHistory = new TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory();
                    dataHistory.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld(tempOldDataReservationRoomTypeDetailRoomPriceDetail);
                    dataHistory.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew(selectedDataRoomTypeDetailRoomPriceDetail);
                    reservationController.selectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories.add(dataHistory);
                    break;
                }
            }
        }
    }

    private void dataReservationChangeRoomCancelHandle() {
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data reservation change room input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationChangeRoom() {
        boolean dataInput = true;
        errDataInput = "";
        if (!checkDataInputReservationRoomTypeDetail()) {
            dataInput = false;
            errDataInput += "Data Reservasi Kamar : Masih Kurang..!! \n";
        }
        if (!checkDataInputTotalBill()) {
            if (rdbCustomer.isSelected()) {
                if (reservationController.selectedDataTransactions.isEmpty()) {
                    dataInput = false;
                    errDataInput += "Total Pembayaran : Belum ada pembayaranan..!! \n";
                } else {
                    dataInput = false;
                    errDataInput += "Total Pembayaran : Pembayaran masih kurang..!! \n";
                }
            } else {
                dataInput = false;
                errDataInput += "Selisih Tagihan : Harus bernilai '0'..!! \n";
            }
        }
        return dataInput;
    }

    private boolean checkDataInputReservationRoomTypeDetail() {
        boolean dataInput = true;
        LocalDate countDate = reservationController.currentDateForChangeRoomProcess.toLocalDate();
        LocalDate lastDate = LocalDate.of(reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate());
        while (dataInput
                && countDate.isBefore(lastDate)) {
            boolean found = false;
            for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
                LocalDate date = LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                        data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                        data.getTblReservationRoomPriceDetail().getDetailDate().getDate());
                if (date.isEqual(countDate)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                dataInput = false;
            }
            countDate = countDate.plusDays(1);
        }

        return dataInput;
    }

    private boolean checkDataInputTotalBill() {
        return (rdbHotel.isSelected() && (calculationTotalDifferentBill().compareTo(new BigDecimal("0")) == 0))
                || (rdbCustomer.isSelected() && (calculationTotalRestOfBill().compareTo(new BigDecimal("0")) == 0));
    }

    /**
     * RESERVATION ROOM TYPE DETAIL
     */
    public ClassTableWithControl tableDataReservationRoomTypeDetail;

    private void initTableDataReservationRoomTypeDetail() {
        //set table
        setTableDataReservationRoomTypeDetail();
        //set control
        setTableControlDataReservationRoomTypeDetail();
        //set table-control to content-view
        ancTableReservationRoomTypeDetailLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationRoomTypeDetail, 0.0);

        ancTableReservationRoomTypeDetailLayout.getChildren().add(tableDataReservationRoomTypeDetail);
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
        roomTypeName.setMinWidth(140);

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
        roomTypeDetailCost.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailDiscount = new TableColumn("Diskon");
        roomTypeDetailDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailDiscount(param.getValue())),
                        param.getValue().lastUpdateDateProperty()));
        roomTypeDetailDiscount.setMinWidth(120);

        tableView.getColumns().addAll(codeDetail, roomTypeName,
                ioDateTitle, roomTypeDetailCost, roomTypeDetailDiscount);

        tableView.setItems(loadAllDataReservationRoomTypeDetail());

        tableDataReservationRoomTypeDetail = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationRoomTypeDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationRoomTypeDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationRoomTypeDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationRoomTypeDetail.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetail() {
        return FXCollections.observableArrayList(reservationController.selectedDataRoomTypeDetails);
    }

    /**
     * RESERVATION ADDITIONAL SERVICE
     */
    public ClassTableWithControl tableDataReservationAdditionalService;

    private void initTableDataReservationAdditionalService() {
        //set table
        setTableDataReservationAdditionalService();
        //set control
        setTableControlDataReservationAdditionalService();
        //set table-control to content-view
        ancTableReservationAdditionalServiceLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalService, 0.0);

        ancTableReservationAdditionalServiceLayout.getChildren().add(tableDataReservationAdditionalService);
    }

    private void setTableDataReservationAdditionalService() {
        TableView<TblReservationAdditionalService> tableView = new TableView();

        TableColumn<TblReservationAdditionalService, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(), param.getValue().tblReservationRoomTypeDetailProperty()));
        codeDetail.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getAdditionalDate() != null
                                ? (param.getValue().getTblRoomService().getIdroomService() != 1
                                        ? ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate())
                                        : ClassFormatter.dateFormate.format(Date.valueOf(
                                                        LocalDate.of(
                                                                param.getValue().getAdditionalDate().getYear() + 1900,
                                                                param.getValue().getAdditionalDate().getMonth() + 1,
                                                                param.getValue().getAdditionalDate().getDate())
                                                        .plusDays(1)))) : "-",
                        param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(120);

        TableColumn<TblReservationAdditionalService, String> serviceName = new TableColumn("Layanan");
        serviceName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomService().getServiceName(), param.getValue().tblRoomServiceProperty()));
        serviceName.setMinWidth(140);

        TableColumn<TblReservationAdditionalService, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()), param.getValue().priceProperty()));
        additionalCharge.setMinWidth(120);

        TableColumn<TblReservationAdditionalService, String> additionalDiscount = new TableColumn("Diskon");
        additionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        additionalDiscount.setMinWidth(120);

        tableView.getColumns().addAll(additionalDate, serviceName, additionalCharge, additionalDiscount);
        tableView.setItems(loadAllDataReservationAdditionalService());
        tableDataReservationAdditionalService = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationAdditionalService() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah Voucher (Bonus)");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataReservationAdditionalServiceVoucherBonusCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus Voucher (Bonus)");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationAdditionalServiceVoucherBonusDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataReservationAdditionalService.addButtonControl(buttonControls);
    }

    public ObservableList<TblReservationAdditionalService> loadAllDataReservationAdditionalService() {
        List<TblReservationAdditionalService> list = new ArrayList<>();
        for (TblReservationAdditionalService selectedAdditionalService : reservationController.selectedAdditionalServices) {
            if (selectedAdditionalService.getTblRoomService().getIdroomService() == 4) {  //Lainya (Bonus Voucher) = '4'
                list.add(selectedAdditionalService);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * RESERVATION TRANSACTION (PAYMENT)
     */
    public ClassTableWithControl tableDataRCTransactionInputDetail;

    private void initTableDataReservationTransaction() {
        //set table
        setTableDataReservationTransaction();
        //set control
        setTableControlDataReservationTransaction();
        //set table-control to content-view
        ancTableReservationTransactionLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataRCTransactionInputDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataRCTransactionInputDetail, 0.0);
        AnchorPane.setBottomAnchor(tableDataRCTransactionInputDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataRCTransactionInputDetail, 0.0);

        ancTableReservationTransactionLayout.getChildren().add(tableDataRCTransactionInputDetail);
    }

    private void setTableDataReservationTransaction() {
        TableView<TblReservationPayment> tableView = new TableView();

        TableColumn<TblReservationPayment, String> paymentDate = new TableColumn("Tanggal");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getPaymentDate()), param.getValue().paymentDateProperty()));
        paymentDate.setMinWidth(160);

        TableColumn<TblReservationPayment, String> transactionNominal = new TableColumn("Nominal");
        transactionNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getUnitNominal()), param.getValue().unitNominalProperty()));
        transactionNominal.setMinWidth(160);

        TableColumn<TblReservationPayment, String> paymentTypeName = new TableColumn("Tipe Pembayaran");
        paymentTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType().getTypeName(), param.getValue().refFinanceTransactionPaymentTypeProperty()));
        paymentTypeName.setMinWidth(180);

        tableView.getColumns().addAll(paymentDate, transactionNominal, paymentTypeName);
        tableView.setItems(loadAllDataReservationTransaction());
        tableDataRCTransactionInputDetail = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationTransaction() {
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

    public ObservableList<TblReservationPayment> loadAllDataReservationTransaction() {
        return FXCollections.observableArrayList(reservationController.selectedDataTransactions);
    }

    /**
     * History Of Changed Room
     */
    public TableView<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> tableDataHistoryOfChangedRoom;

    private void initDataTableHistoryOfChangedRoom() {
        //set table
        setTableDataHistoryOfChangedRoom();
        //add table to layout
        gpHistoryOfChangedRoom.getChildren().clear();
        gpHistoryOfChangedRoom.add(tableDataHistoryOfChangedRoom, 0, 0);
    }

    private void setTableDataHistoryOfChangedRoom() {
        tableDataHistoryOfChangedRoom = new TableView();

        TableColumn<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> roomTypeNameOld = new TableColumn("Tipe Kamar (Lama)");
        roomTypeNameOld.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName(), param.getValue().tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOldProperty()));
        roomTypeNameOld.setMinWidth(170);

        TableColumn<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> roomTypePriceOld = new TableColumn("Harga Kamar (Lama)");
        roomTypePriceOld.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomPriceDetail().getDetailPrice()), param.getValue().tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOldProperty()));
        roomTypePriceOld.setMinWidth(180);

        TableColumn<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> roomTypeNameNew = new TableColumn("Tipe Kamar (Baru)");
        roomTypeNameNew.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName(), param.getValue().tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty()));
        roomTypeNameNew.setMinWidth(170);

        TableColumn<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> roomTypePriceNew = new TableColumn("Harga Kamar (Baru)");
        roomTypePriceNew.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomPriceDetail().getDetailPrice()), param.getValue().tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty()));
        roomTypePriceNew.setMinWidth(180);

        TableColumn<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> reservationRoomDate = new TableColumn("Tanggal");
        reservationRoomDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomPriceDetail().getDetailDate()), param.getValue().tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty()));
        reservationRoomDate.setMinWidth(140);

        tableDataHistoryOfChangedRoom.getColumns().addAll(reservationRoomDate, roomTypeNameOld, roomTypePriceOld, roomTypeNameNew, roomTypePriceNew);
        tableDataHistoryOfChangedRoom.setItems(FXCollections.observableArrayList(loadAllDataHistoryOfChangedRoom()));
    }

    public List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> loadAllDataHistoryOfChangedRoom() {
        List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> list = reservationController.getFReservationManager().getAllDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistoryByIDReservationRoomTypeDetailNew(reservationController.selectedDataRoomTypeDetail.getIddetail());
        for (TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory data : list) {
            //data reservation room type detail - room price detail (old)
            data.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld(reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetail(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getIdrelation()));
            //data reservation room type detail (old)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().setTblReservationRoomTypeDetail(reservationController.getFReservationManager().getReservationRoomTypeDetail(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getIddetail()));
            //data reservation (old)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setTblReservation(reservationController.getFReservationManager().getReservation(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
            //data room type (old)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setTblRoomType(reservationController.getFReservationManager().getRoomType(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
            //data reservation room price detail (old)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().setTblReservationRoomPriceDetail(reservationController.getFReservationManager().getReservationRoomPriceDetail(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomPriceDetail().getIddetail()));
            //data reservation room type detail - room price detail (new)
            data.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew(reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetail(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getIdrelation()));
            //data reservation room type detail (new)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().setTblReservationRoomTypeDetail(reservationController.getFReservationManager().getReservationRoomTypeDetail(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().getIddetail()));
            //data reservation (new)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().setTblReservation(reservationController.getFReservationManager().getReservation(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
            //data room type (new)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().setTblRoomType(reservationController.getFReservationManager().getRoomType(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
            //data reservation room price detail (new)
            data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().setTblReservationRoomPriceDetail(reservationController.getFReservationManager().getReservationRoomPriceDetail(data.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomPriceDetail().getIddetail()));
        }
        return list;
    }

    /**
     * HANDLE DIALOG
     */
    public Stage dialogStage;

    /**
     * HANDLE FOR DATA INPUT RESERVATION ROOM TYPE DETAIL
     */
    public TblReservationRoomTypeDetail tempSelectedDataRoomTypeDetail;

    public List<TblReservationRoomTypeDetail> tempSelectedDataRoomTypeDetails;

    public List<TblReservationRoomTypeDetailRoomPriceDetail> tempSelectedDataRoomTypeDetailRoomPriceDetails;

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> tempSelectedDataRoomTypeDetailTravelAgentDiscountDetails;

    public List<TblReservationAdditionalService> tempSelectedDataAdditionalServices;

    public void dataReservationRoomTypeDetailCreateHandle() {
        showReservationRoomTypeDetailDialog();
    }

    private void dataReservationRoomTypeDetailDeleteHandle() {
        if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //reservation room type detail
                tempSelectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
                ClassMessage.showSucceedDeletingDataMessage("", reservationController.dialogStage);
                //reservation room type detail - reservation room price detail
                for (int i = reservationController.selectedDataRoomTypeDetailRoomPriceDetails.size() - 1; i > -1; i--) {
                    if (reservationController.selectedDataRoomTypeDetailRoomPriceDetails.get(i).getTblReservationRoomTypeDetail().equals(tempSelectedDataRoomTypeDetail)) {
                        reservationController.selectedDataRoomTypeDetailRoomPriceDetails.remove(i);
                    }
                }
                //reservation room type detail - reservation travel agent discount detail
                for (int i = reservationController.selectedDataRoomTypeDetailTravelAgentDiscountDetails.size() - 1; i > -1; i--) {
                    if (reservationController.selectedDataRoomTypeDetailTravelAgentDiscountDetails.get(i).getTblReservationRoomTypeDetail().equals(tempSelectedDataRoomTypeDetail)) {
                        reservationController.selectedDataRoomTypeDetailTravelAgentDiscountDetails.remove(i);
                    }
                }
                //reservation additional item
                for (int i = reservationController.selectedAdditionalItems.size() - 1; i > -1; i--) {
                    if (reservationController.selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().equals(tempSelectedDataRoomTypeDetail)) {
                        reservationController.selectedAdditionalItems.get(i).setTblReservationRoomTypeDetail(reservationController.selectedDataRoomTypeDetail);
                    }
                }
//                tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(reservationController.selectedAdditionalItems));
                //reservation additional service
                for (int i = reservationController.selectedAdditionalServices.size() - 1; i > -1; i--) {
                    if (reservationController.selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().equals(tempSelectedDataRoomTypeDetail)) {
                        reservationController.selectedAdditionalServices.get(i).setTblReservationRoomTypeDetail(reservationController.selectedDataRoomTypeDetail);
                    }
                }
                tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(loadAllDataReservationAdditionalService()));
                //reservation room type detail
                for (int i = reservationController.selectedDataRoomTypeDetails.size() - 1; i > -1; i--) {
                    if (reservationController.selectedDataRoomTypeDetails.get(i).equals(tempSelectedDataRoomTypeDetail)) {
                        reservationController.selectedDataRoomTypeDetails.remove(i);
                    }
                }
                tableDataReservationRoomTypeDetail.getTableView().setItems(FXCollections.observableArrayList(reservationController.selectedDataRoomTypeDetails));
                //refresh data transasction and additonal service (bonus voucher)
                refreshDataTransactionPaymentAndAdditonalServiceBonusVoucher();
                //refresh data bill
                refreshBill();
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", reservationController.dialogStage);
        }
    }

    public void refreshDataTransactionPaymentAndAdditonalServiceBonusVoucher() {
        //reservation additional service (bonus voucher) : remove
        for (int i = reservationController.selectedAdditionalServices.size() - 1; i > -1; i--) {
            if (reservationController.selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 4) {  //Lainya (Bonus Voucher) = '4'
                reservationController.selectedAdditionalServices.remove(i);
            }
        }
        tableDataReservationAdditionalService.getTableView().setItems(loadAllDataReservationAdditionalService());
        //reservation payment : remove
        for (int i = reservationController.selectedDataTransactions.size() - 1; i > -1; i--) {
            //remove data rc-trancsation detail
            removeRCTransactionDetailData(reservationController.selectedDataTransactions.get(i));
            //remove data rc-transaction
            reservationController.selectedDataTransactions.remove(i);
        }
        //reset data rc-transaction input
        resetDataTransaction();
        tableDataRCTransactionInputDetail.getTableView().setItems(FXCollections.observableArrayList(reservationController.selectedDataTransactions));
    }

    private void showReservationRoomTypeDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rcr/RCRReservationRoomTypeDetailInputDialog.fxml"));

            RCRReservationRoomTypeDetailInputController controller = new RCRReservationRoomTypeDetailInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION ADDITIONAL SERVICE
     */
    public TblReservationAdditionalService tempSelectedAdditionalService;

    private void dataReservationAdditionalServiceVoucherBonusCreateHandle() {
        if (calculationTotalDifferentBill().compareTo(new BigDecimal("0")) == -1) {
            //set selected data
            tempSelectedAdditionalService = new TblReservationAdditionalService();
            tempSelectedAdditionalService.setTblReservationRoomTypeDetail(reservationController.selectedDataRoomTypeDetail);
            tempSelectedAdditionalService.setTblRoomService(reservationController.getFReservationManager().getRoomService(4));  //Bonus Voucher = '4'
            tempSelectedAdditionalService.setAdditionalDate(Date.valueOf(reservationController.currentDateForChangeRoomProcess.toLocalDate()));
            tempSelectedAdditionalService.setPrice(new BigDecimal("0"));
            tempSelectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
            tempSelectedAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));    //Reservation = '0'
            //open form data reservation additional service (bonus voucher) dialog
            showReservationAdditionalServiceCreateBonusVoucherDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Tidak ada data tambahan voucher yang harus dibuat (selisih >= 0)..!", "", reservationController.dialogStage);
        }
    }

    private void dataReservationAdditionalServiceVoucherBonusDeleteHandle() {
        if (tableDataReservationAdditionalService.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            tempSelectedAdditionalService = (TblReservationAdditionalService) tableDataReservationAdditionalService.getTableView().getSelectionModel().getSelectedItem();
            if (tempSelectedAdditionalService.getTblRoomService().getIdroomService() == 4) {  //bonus voucher = '4'
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", reservationController.dialogStage);
                if (alert.getResult() == ButtonType.OK) {
                    ClassMessage.showSucceedDeletingDataMessage("", reservationController.dialogStage);
                    //reservation additional service : remove
                    reservationController.selectedAdditionalServices.remove(tempSelectedAdditionalService);
                    tableDataReservationAdditionalService.getTableView().setItems(loadAllDataReservationAdditionalService());
                    //refresh data bill
                    refreshBill();
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Selain data voucher(bonus) tidak dapat dihapus..!", "", reservationController.dialogStage);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", reservationController.dialogStage);
        }
    }

    private void showReservationAdditionalServiceCreateBonusVoucherDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rcr/RCRReservationAdditionalServiceCreateBonusVoucherInputDialog.fxml"));

            RCRReservationAdditionalServiceCreateBonusVoucherInputController controller = new RCRReservationAdditionalServiceCreateBonusVoucherInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION TRANSACTION
     */
    public TblReservationPayment tempSelectedDataTransaction;

    public TblReservationPaymentWithTransfer tempSelectedDataTransactionWithTransfer;

    public TblReservationPaymentWithBankCard tempSelectedDataTransactionWithBankCard;

    public TblReservationPaymentWithCekGiro tempSelectedDataTransactionWithCekGiro;

    public TblReservationPaymentWithGuaranteePayment tempSelectedDataTransactionWithGuaranteePayment;

    public TblReservationPaymentWithReservationVoucher tempSelectedDataTransactionWithReservationVoucher;

    //0 = 'insert'
    //1 = 'update'
    public int dataInputRCTransactionInputDetailStatus = 0;

    private void dataRCTransactionInputDetailCreateHandle() {
        if (calculationTotalRestOfBill().compareTo(new BigDecimal("0")) == 1) {
            dataInputRCTransactionInputDetailStatus = 0;    //insert
            //reset data transaction
            resetDataTransaction();
            //open form data reservation transaction dialog
            showRCTransactionInputDetailDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Tidak ada data tagihan yang harus dibayar (selisih <= 0)..!", "", reservationController.dialogStage);
        }
    }

    private void resetDataTransaction() {
        tempSelectedDataTransaction = new TblReservationPayment();
        tempSelectedDataTransaction.setTblReservationBill(reservationController.dataReservationBill);
        tempSelectedDataTransaction.setUnitNominal(new BigDecimal("0"));
        tempSelectedDataTransaction.setRoundingValue(new BigDecimal("0"));
        tempSelectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer();
        tempSelectedDataTransactionWithTransfer.setTblReservationPayment(tempSelectedDataTransaction);
        tempSelectedDataTransactionWithTransfer.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
        tempSelectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard();
        tempSelectedDataTransactionWithBankCard.setTblReservationPayment(tempSelectedDataTransaction);
        tempSelectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
        tempSelectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
        tempSelectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro();
        tempSelectedDataTransactionWithCekGiro.setTblReservationPayment(tempSelectedDataTransaction);
        tempSelectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
        tempSelectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
        tempSelectedDataTransactionWithGuaranteePayment.setTblReservationPayment(tempSelectedDataTransaction);
        tempSelectedDataTransactionWithGuaranteePayment.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
        tempSelectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher();
        tempSelectedDataTransactionWithReservationVoucher.setTblReservationPayment(tempSelectedDataTransaction);
    }

    public TblBankAccount getDataDefaultBankAccountForGuestTransaction() {
        String defaultBankAccountForGuestTransaction = "";
        SysDataHardCode sdhDefaultBankAccountForGuestTransaction = reservationController.getFReservationManager().getDataSysDataHardCode((long) 25);  //DefaultBankAccountForGuestTransaction = '25'
        if (sdhDefaultBankAccountForGuestTransaction != null
                && sdhDefaultBankAccountForGuestTransaction.getDataHardCodeValue() != null) {
            defaultBankAccountForGuestTransaction = sdhDefaultBankAccountForGuestTransaction.getDataHardCodeValue();
        }
        if (!defaultBankAccountForGuestTransaction.equals("")) {
            return reservationController.getFReservationManager().getBankAccount(Long.parseLong(defaultBankAccountForGuestTransaction));
        }
        return null;
    }
    
    public void dataRCTransactionInputDetailUpdateHandle() {
        if (tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputRCTransactionInputDetailStatus = 1;    //update
            //reset data selected rc-transaction
            resetDataTransaction();
            for (TblReservationPayment data : reservationController.selectedDataTransactions) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    //set data selected rc-transaction
                    tempSelectedDataTransaction = new TblReservationPayment(data);
                    tempSelectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(tempSelectedDataTransaction.getRefFinanceTransactionPaymentType()));
                    //set data selected rc-transaction detail
                    setRCTransactionDetailData(data);
                    break;
                }
            }
            //open form data transaction input dialog
            showRCTransactionInputDetailDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, reservationController.dialogStage);
        }
    }

    private void setRCTransactionDetailData(TblReservationPayment realRCTransactionPayment) {
        switch (realRCTransactionPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                for (TblReservationPaymentWithTransfer data : reservationController.selectedDataTransactionWithTransfers) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer(data);
                        tempSelectedDataTransactionWithTransfer.setTblReservationPayment(tempSelectedDataTransaction);
                        break;
                    }
                }
                break;
            case 2:    //Debit
                for (TblReservationPaymentWithBankCard data : reservationController.selectedDataTransactionWithBankCards) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard(data);
                        tempSelectedDataTransactionWithBankCard.setTblReservationPayment(tempSelectedDataTransaction);
                        tempSelectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                        tempSelectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                        tempSelectedDataTransactionWithBankCard.setTblBank(new TblBank(tempSelectedDataTransactionWithBankCard.getTblBank()));
                        tempSelectedDataTransactionWithBankCard.setTblBankEdc(new TblBankEdc(tempSelectedDataTransactionWithBankCard.getTblBankEdc()));
                        tempSelectedDataTransactionWithBankCard.setTblBankNetworkCard(new TblBankNetworkCard(tempSelectedDataTransactionWithBankCard.getTblBankNetworkCard()));
                        tempSelectedDataTransactionWithBankCard.setTblBankEventCard(tempSelectedDataTransactionWithBankCard.getTblBankEventCard() != null
                                ? new TblBankEventCard(tempSelectedDataTransactionWithBankCard.getTblBankEventCard()) : null);
                        tempSelectedDataTransactionWithBankCard.setTblBankAccount(new TblBankAccount(tempSelectedDataTransactionWithBankCard.getTblBankAccount()));
                        break;
                    }
                }
                break;
            case 3:    //Credit
                for (TblReservationPaymentWithBankCard data : reservationController.selectedDataTransactionWithBankCards) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard(data);
                        tempSelectedDataTransactionWithBankCard.setTblReservationPayment(tempSelectedDataTransaction);
                        tempSelectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                        tempSelectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                        tempSelectedDataTransactionWithBankCard.setTblBank(new TblBank(tempSelectedDataTransactionWithBankCard.getTblBank()));
                        tempSelectedDataTransactionWithBankCard.setTblBankEdc(new TblBankEdc(tempSelectedDataTransactionWithBankCard.getTblBankEdc()));
                        tempSelectedDataTransactionWithBankCard.setTblBankNetworkCard(new TblBankNetworkCard(tempSelectedDataTransactionWithBankCard.getTblBankNetworkCard()));
                        tempSelectedDataTransactionWithBankCard.setTblBankEventCard(tempSelectedDataTransactionWithBankCard.getTblBankEventCard() != null
                                ? new TblBankEventCard(tempSelectedDataTransactionWithBankCard.getTblBankEventCard()) : null);
                        tempSelectedDataTransactionWithBankCard.setTblBankAccount(new TblBankAccount(tempSelectedDataTransactionWithBankCard.getTblBankAccount()));
                        break;
                    }
                }
                break;
            case 4:    //Cek
                for (TblReservationPaymentWithCekGiro data : reservationController.selectedDataTransactionWithCekGiros) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro(data);
                        tempSelectedDataTransactionWithCekGiro.setTblReservationPayment(tempSelectedDataTransaction);
                        tempSelectedDataTransactionWithCekGiro.setTblBank(new TblBank(tempSelectedDataTransactionWithCekGiro.getTblBank()));
                        tempSelectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(tempSelectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                        break;
                    }
                }
                break;
            case 5:    //Giro
                for (TblReservationPaymentWithCekGiro data : reservationController.selectedDataTransactionWithCekGiros) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro(data);
                        tempSelectedDataTransactionWithCekGiro.setTblReservationPayment(tempSelectedDataTransaction);
                        tempSelectedDataTransactionWithCekGiro.setTblBank(new TblBank(tempSelectedDataTransactionWithCekGiro.getTblBank()));
                        tempSelectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(tempSelectedDataTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                        break;
                    }
                }
                break;
            case 6:    //Travel Agent
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.selectedDataTransactionWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        tempSelectedDataTransactionWithGuaranteePayment.setTblReservationPayment(tempSelectedDataTransaction);
                        break;
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporat)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.selectedDataTransactionWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        tempSelectedDataTransactionWithGuaranteePayment.setTblReservationPayment(tempSelectedDataTransaction);
                        break;
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.selectedDataTransactionWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment(data);
                        tempSelectedDataTransactionWithGuaranteePayment.setTblReservationPayment(tempSelectedDataTransaction);
                        break;
                    }
                }
                break;
            case 9:    //Draw Dposit
                break;
            case 10:   //Voucher
                for (TblReservationPaymentWithReservationVoucher data : reservationController.selectedDataTransactionWithReservationVouchers) {
                    if (data.getTblReservationPayment().equals(realRCTransactionPayment)) {
                        tempSelectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher(data);
                        tempSelectedDataTransactionWithReservationVoucher.setTblReservationPayment(tempSelectedDataTransaction);
                        tempSelectedDataTransactionWithReservationVoucher.setTblReservationVoucher(new TblReservationVoucher(tempSelectedDataTransactionWithReservationVoucher.getTblReservationVoucher()));
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
            for (TblReservationPayment data : reservationController.selectedDataTransactions) {
                if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    result = result.add(data.getUnitNominal());
                    result = result.subtract(data.getRoundingValue());
                    //data transaction payment - bank card
                    if (data.getRefFinanceTransactionPaymentType().getIdtype() == 2
                            || data.getRefFinanceTransactionPaymentType().getIdtype() == 3) {
                        for (TblReservationPaymentWithBankCard detailData : reservationController.selectedDataTransactionWithBankCards) {
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
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage(null, reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage(null, reservationController.dialogStage);
                //remove data from table items list
                for (TblReservationPayment data : reservationController.selectedDataTransactions) {
                    if (data.equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                        //remove data rc-trancsation detail
                        removeRCTransactionDetailData(data);
                        //remove data rc-transaction
                        reservationController.selectedDataTransactions.remove(data);
                        break;
                    }
                }
                //reset data rc-transaction input
                resetDataTransaction();
                //refresh data bill
                refreshBill();
                tableDataRCTransactionInputDetail.getTableView().setItems(FXCollections.observableArrayList(reservationController.selectedDataTransactions));
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, reservationController.dialogStage);
        }
    }

    private void removeRCTransactionDetailData(TblReservationPayment rcTransactionPayment) {
        switch (rcTransactionPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                for (TblReservationPaymentWithTransfer data : reservationController.selectedDataTransactionWithTransfers) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.selectedDataTransactionWithTransfers.remove(data);
                        break;
                    }
                }
                break;
            case 2:    //Debit
                for (TblReservationPaymentWithBankCard data : reservationController.selectedDataTransactionWithBankCards) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.selectedDataTransactionWithBankCards.remove(data);
                        break;
                    }
                }
                break;
            case 3:    //Credit
                for (TblReservationPaymentWithBankCard data : reservationController.selectedDataTransactionWithBankCards) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.selectedDataTransactionWithBankCards.remove(data);
                        break;
                    }
                }
                break;
            case 4:    //Cek
                for (TblReservationPaymentWithCekGiro data : reservationController.selectedDataTransactionWithCekGiros) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.selectedDataTransactionWithCekGiros.remove(data);
                        break;
                    }
                }
                break;
            case 5:    //Giro
                for (TblReservationPaymentWithCekGiro data : reservationController.selectedDataTransactionWithCekGiros) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.selectedDataTransactionWithCekGiros.remove(data);
                        break;
                    }
                }
                break;
            case 6:    //Travel Agent
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.selectedDataTransactionWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationController.selectedDataTransactionWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporate)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.selectedDataTransactionWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationController.selectedDataTransactionWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                for (TblReservationPaymentWithGuaranteePayment data : reservationController.selectedDataTransactionWithGuaranteePayments) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        //delete data guarantee letter - item detail
                        deleteGuaranteeLetterItemDetail(data);
                        //data guarantee letter
                        reservationController.selectedDataTransactionWithGuaranteePayments.remove(data);
                        break;
                    }
                }
                break;
            case 9:    //Draw Deposit
                break;
            case 10:   //Voucher
                for (TblReservationPaymentWithReservationVoucher data : reservationController.selectedDataTransactionWithReservationVouchers) {
                    if (data.getTblReservationPayment().equals(rcTransactionPayment)) {
                        reservationController.selectedDataTransactionWithReservationVouchers.remove(data);
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
        for (int i = reservationController.selectedDataGuaranteeLetterItemDetails.size() - 1; i > -1; i--) {
            if (reservationController.selectedDataGuaranteeLetterItemDetails.get(i).getTblReservationPaymentWithGuaranteePayment().equals(dataGL)) {
                reservationController.selectedDataGuaranteeLetterItemDetails.remove(i);
            }
        }
    }

    private void showRCTransactionInputDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rcr/RCRRCTransactionInputDialog.fxml"));

            RCRRCTransactionInputController controller = new RCRRCTransactionInputController(this);
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

    public void saveDataTransaction() {
        //data rc-transaction
        tempSelectedDataTransaction.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
        tempSelectedDataTransaction.setTblEmployeeByIdcashier(ClassSession.currentUser.getTblEmployeeByIdemployee());
        tempSelectedDataTransaction.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));    //Reservation = '0'
        if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
            reservationController.selectedDataTransactions.add(tempSelectedDataTransaction);
        } else {  //update
            for (int i = 0; i < reservationController.selectedDataTransactions.size(); i++) {
                if (reservationController.selectedDataTransactions.get(i).equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                    reservationController.selectedDataTransactions.set(i, tempSelectedDataTransaction);
                    break;
                }
            }
        }
        //data rc-transaction detail
        saveDataTransactionDetail();
        //reset data rc-transaction input
        resetDataTransaction();
        //refresh data bill
        refreshBill();
        tableDataRCTransactionInputDetail.getTableView().setItems(FXCollections.observableArrayList(reservationController.selectedDataTransactions));
        //close dialog data rc-transaction input detail
        dialogStage.close();
    }

    private void saveDataTransactionDetail() {
        switch (tempSelectedDataTransaction.getRefFinanceTransactionPaymentType().getIdtype()) {
            case 0:    //Cash
                break;
            case 1:    //Transfer
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.selectedDataTransactionWithTransfers.add(tempSelectedDataTransactionWithTransfer);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithTransfers.size(); i++) {
                        if (reservationController.selectedDataTransactionWithTransfers.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.selectedDataTransactionWithTransfers.set(i, tempSelectedDataTransactionWithTransfer);
                            break;
                        }
                    }
                }
                break;
            case 2:    //Debit
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.selectedDataTransactionWithBankCards.add(tempSelectedDataTransactionWithBankCard);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithBankCards.size(); i++) {
                        if (reservationController.selectedDataTransactionWithBankCards.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.selectedDataTransactionWithBankCards.set(i, tempSelectedDataTransactionWithBankCard);
                            break;
                        }
                    }
                }
                break;
            case 3:    //Credit
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.selectedDataTransactionWithBankCards.add(tempSelectedDataTransactionWithBankCard);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithBankCards.size(); i++) {
                        if (reservationController.selectedDataTransactionWithBankCards.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.selectedDataTransactionWithBankCards.set(i, tempSelectedDataTransactionWithBankCard);
                            break;
                        }
                    }
                }
                break;
            case 4:    //Cek
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.selectedDataTransactionWithCekGiros.add(tempSelectedDataTransactionWithCekGiro);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithCekGiros.size(); i++) {
                        if (reservationController.selectedDataTransactionWithCekGiros.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.selectedDataTransactionWithCekGiros.set(i, tempSelectedDataTransactionWithCekGiro);
                            break;
                        }
                    }
                }
                break;
            case 5:    //Giro
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.selectedDataTransactionWithCekGiros.add(tempSelectedDataTransactionWithCekGiro);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithCekGiros.size(); i++) {
                        if (reservationController.selectedDataTransactionWithCekGiros.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.selectedDataTransactionWithCekGiros.set(i, tempSelectedDataTransactionWithCekGiro);
                            break;
                        }
                    }
                }
                break;
            case 6:    //Travel Agent
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(tempSelectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationController.selectedDataTransactionWithGuaranteePayments.add(tempSelectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithGuaranteePayments.size(); i++) {
                        if (reservationController.selectedDataTransactionWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationController.selectedDataTransactionWithGuaranteePayments.get(i),
                                    tempSelectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationController.selectedDataTransactionWithGuaranteePayments.set(i, tempSelectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 7:    //Guarantee Letter (Corporatte)
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(tempSelectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationController.selectedDataTransactionWithGuaranteePayments.add(tempSelectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithGuaranteePayments.size(); i++) {
                        if (reservationController.selectedDataTransactionWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationController.selectedDataTransactionWithGuaranteePayments.get(i),
                                    tempSelectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationController.selectedDataTransactionWithGuaranteePayments.set(i, tempSelectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 8:    //Guarantee Letter (Government)
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    //data GL - Item Detail
                    saveGuaranteeLetterItemDetail(tempSelectedDataTransactionWithGuaranteePayment);
                    //data GL
                    reservationController.selectedDataTransactionWithGuaranteePayments.add(tempSelectedDataTransactionWithGuaranteePayment);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithGuaranteePayments.size(); i++) {
                        if (reservationController.selectedDataTransactionWithGuaranteePayments.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            //data GL - Item Detail
                            saveGuaranteeLetterItemDetail(reservationController.selectedDataTransactionWithGuaranteePayments.get(i),
                                    tempSelectedDataTransactionWithGuaranteePayment);
                            //data GL
                            reservationController.selectedDataTransactionWithGuaranteePayments.set(i, tempSelectedDataTransactionWithGuaranteePayment);
                            break;
                        }
                    }
                }
                break;
            case 9:    //Draw Dposit
                break;
            case 10:   //Voucher
                if (dataInputRCTransactionInputDetailStatus == 0) {   //insert
                    reservationController.selectedDataTransactionWithReservationVouchers.add(tempSelectedDataTransactionWithReservationVoucher);
                } else {  //update
                    for (int i = 0; i < reservationController.selectedDataTransactionWithReservationVouchers.size(); i++) {
                        if (reservationController.selectedDataTransactionWithReservationVouchers.get(i).getTblReservationPayment().equals(tableDataRCTransactionInputDetail.getTableView().getSelectionModel().getSelectedItem())) {
                            reservationController.selectedDataTransactionWithReservationVouchers.set(i, tempSelectedDataTransactionWithReservationVoucher);
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
        for (TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD : reservationController.selectedDataRoomTypeDetailRoomPriceDetails) {
            //data guarantee letter - item detail
            TblGuaranteeLetterItemDetail dataGLID = new TblGuaranteeLetterItemDetail();
            dataGLID.setTblReservationPaymentWithGuaranteePayment(dataGL);
            dataGLID.setCodeRtd(dataRRTDRPD.getTblReservationRoomTypeDetail().getCodeDetail());
            dataGLID.setDetailName(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName());
            dataGLID.setRoomName(dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    ? dataRRTDRPD.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName() : null);
            dataGLID.setDetailDate(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate());
            dataGLID.setDetailCost(calculationRoomTypeDetailCostDF(dataRRTDRPD));
            dataGLID.setDetailQuantity(new BigDecimal("1"));
            dataGLID.setTotalDiscountNominal(calculationRoomTypeDetailDiscountDF(dataRRTDRPD));
            dataGLID.setServiceChargePercentage(reservationController.dataReservationBill.getServiceChargePercentage());
            dataGLID.setTaxPercentage(reservationController.dataReservationBill.getTaxPercentage());
            dataGLID.setDetailType("Room");
            //add data to list
            reservationController.selectedDataGuaranteeLetterItemDetails.add(dataGLID);
        }
    }

    private BigDecimal calculationRoomTypeDetailCostDF(TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD) {
        BigDecimal newPrice = dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice();
        BigDecimal oldPrice = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == reservationController.selectedDataRoomTypeDetail.getIddetail()
                    && (LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getDate())).isEqual(
                            LocalDate.of(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
                oldPrice = oldPrice.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
                break;
            }
        }
        return newPrice.subtract(oldPrice);
    }

    private BigDecimal calculationRoomTypeDetailDiscountDF(TblReservationRoomTypeDetailRoomPriceDetail dataRRTDRPD) {
        BigDecimal result = new BigDecimal("0");
        BigDecimal newDiscountPrice = (dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))
                .multiply(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailPrice());
        BigDecimal oldDiscountPrice = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail data : reservationController.dataReservationRoomTypeDetailRoomPriceDetails) {
            if (data.getTblReservationRoomTypeDetail().getIddetail() == reservationController.selectedDataRoomTypeDetail.getIddetail()
                    && (LocalDate.of(data.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                            data.getTblReservationRoomPriceDetail().getDetailDate().getDate())).isEqual(
                            LocalDate.of(dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getYear() + 1900,
                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getMonth() + 1,
                                    dataRRTDRPD.getTblReservationRoomPriceDetail().getDetailDate().getDate()))) {
                oldDiscountPrice = oldDiscountPrice.add((data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))
                        .multiply(data.getTblReservationRoomPriceDetail().getDetailPrice()));
                break;
            }
        }
        result = result.add(newDiscountPrice.subtract(oldDiscountPrice));
        return result;
    }

    private void saveGuaranteeLetterItemDetail(TblReservationPaymentWithGuaranteePayment oldDataGL,
            TblReservationPaymentWithGuaranteePayment newDataGL) {
        //remove data
        for (int i = reservationController.selectedDataGuaranteeLetterItemDetails.size() - 1; i > -1; i--) {
            if (reservationController.selectedDataGuaranteeLetterItemDetails.get(i).getTblReservationPaymentWithGuaranteePayment().equals(oldDataGL)) {
                reservationController.selectedDataGuaranteeLetterItemDetails.remove(i);
            }
        }
        //add data
        saveGuaranteeLetterItemDetail(newDataGL);
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataReservationChangeRoom();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public ReservationChangeRoomInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

    public ReservationController getParentController() {
        return reservationController;
    }
    
}
