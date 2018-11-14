/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
public class ReservationChangeRoomTypeInputController implements Initializable {

    @FXML
    private AnchorPane ancFormReservationChangeRoomType;

    @FXML
    private GridPane gpFormDataReservationChangeRoomType;

    @FXML
    private AnchorPane ancReservationChangeRoomTypeLayout;

    private JFXCComboBoxTablePopup<TblRoomType> cbpRoomType;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationChangeRoomType() {

        initDataPopup();
        
        btnSave.setTooltip(new Tooltip("Simpan (Data Tipe Kamar)"));
        btnSave.setOnAction((e) -> {
            dataReservationChangeRoomTypeSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationChangeRoomTypeCancelHandle();
        });

    }

    private void initDataPopup() {
        //data room type
        TableView<TblRoomType> tableRoomType = new TableView<>();

        TableColumn<TblRoomType, String> roomTypeName = new TableColumn<>("Tipe Kamar");
        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomType, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRoomTypeName(), param.getValue().roomTypeNameProperty()));
        roomTypeName.setMinWidth(140);

        TableColumn<TblRoomType, String> adultNumber = new TableColumn<>("Dewasa");
        adultNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomType, String> param)
                -> Bindings.createStringBinding(() -> String.valueOf(param.getValue().getAdultNumber()),
                        param.getValue().adultNumberProperty()));
        adultNumber.setMinWidth(100);

        TableColumn<TblRoomType, String> childNumber = new TableColumn<>("Anak");
        childNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomType, String> param)
                -> Bindings.createStringBinding(() -> String.valueOf(param.getValue().getChildNumber()),
                        param.getValue().childNumberProperty()));
        childNumber.setMinWidth(100);

        TableColumn<TblRoomType, String> roomTypeBasePrice = new TableColumn<>("Harga (Dasar)");
        roomTypeBasePrice.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomType, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getRoomTypePrice()),
                        param.getValue().roomTypePriceProperty()));
        roomTypeBasePrice.setMinWidth(140);

        tableRoomType.getColumns().addAll(roomTypeName, adultNumber, childNumber, roomTypeBasePrice);

        ObservableList<TblRoomType> roomTypeItems = FXCollections.observableArrayList(loadAllDataRoomType());

        cbpRoomType = new JFXCComboBoxTablePopup<>(
                TblRoomType.class, tableRoomType, roomTypeItems, "", "Tipe Kamar *", true, 500, 300
        );
        
        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpRoomType, 0.0);
        AnchorPane.setLeftAnchor(cbpRoomType, 0.0);
        AnchorPane.setRightAnchor(cbpRoomType, 0.0);
        AnchorPane.setTopAnchor(cbpRoomType, 0.0);
        ancReservationChangeRoomTypeLayout.getChildren().add(cbpRoomType);
    }

    private List<TblRoomType> loadAllDataRoomType() {
        //data room type
        List<TblRoomType> list = reservationController.getFReservationManager().getAllDataRoomType();
        return list;
    }

    private void refreshDataPopup() {
        //data room type
        ObservableList<TblRoomType> roomTypeItems = FXCollections.observableArrayList(loadAllDataRoomType());
        cbpRoomType.setItems(roomTypeItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        cbpRoomType.setValue(reservationController.selectedDataRoomTypeDetail.getTblRoomType());

        cbpRoomType.hide();
    }

    private void dataReservationChangeRoomTypeSaveHandle() {
        if (checkDataInputDataReservationChangeRoomType()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data reservation room type detail - room type (updated)
                reservationController.selectedDataRoomTypeDetail.setTblRoomType(cbpRoomType.getValue());
                //refresh reservation/checkout bill
                reservationController.refreshDataBill(reservationController.selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        ? "reservation" : "checkout");
                //save data to database
                reservationController.dataReservationSaveHandle(15);
                //close dialog input
                reservationController.dialogStage.close();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataReservationChangeRoomTypeCancelHandle() {
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data reservation change room type input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationChangeRoomType() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpRoomType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (cbpRoomType.getValue().getIdroomType() == reservationController.selectedDataRoomTypeDetail.getTblRoomType().getIdroomType()) {
                dataInput = false;
                errDataInput += "Tipe Kamar : Tidak ada perubahan data..!! \n";
            } else {
                if (!checkDataInputRoomTypeAvailable()) {
                    dataInput = false;
                    errDataInput += "Jumlah Kamar : Tidak mencukupi..!! \n";
                }
            }
        }
        return dataInput;
    }

    private boolean checkDataInputRoomTypeAvailable() {
        boolean available = true;
        LocalDate dateCount = LocalDate.of(
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getDate());
        LocalDate dateLast = LocalDate.of(
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                reservationController.selectedDataRoomTypeDetail.getCheckOutDateTime().getDate());
        while (dateCount.isBefore(dateLast)) {
            int availableNumber = getRoomAvailableNumber(cbpRoomType.getValue(), dateCount)
                    - getRoomReservedNumber(cbpRoomType.getValue(), dateCount);
            if ((availableNumber - 1) < 0) {
                available = false;
                break;
            }
            dateCount = dateCount.plusDays(1);
        }
        return available;
    }

    private int getRoomAvailableNumber(TblRoomType roomType, LocalDate date) {
        int result = 0;
        if (reservationController.reservationType.getIdtype() == 0) {   //Customer = '0'
            List<TblRoom> listRoom = reservationController.getFReservationManager().getAllDataRoomByIDRoomType(roomType.getIdroomType());
            for (int i = listRoom.size() - 1; i > -1; i--) {
                if (listRoom.get(i).getRefRoomStatus().getIdstatus() == 6) {  //Out Of Service = '6'
                    listRoom.remove(i);
                }
            }
            result = listRoom.size();
            List<TblTravelAgentRoomType> travelAgentRoomTypes = reservationController.getFReservationManager().getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(
                    roomType.getIdroomType(),
                    Date.valueOf(date));
            for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
                result -= travelAgentRoomType.getRoomNumber();
            }
        } else {  //Travel Agent
            TblTravelAgentRoomType travelAgentRoomType = reservationController.getFReservationManager().getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(roomType.getIdroomType(),
                    reservationController.currentTravelAgent.getTblPartner().getIdpartner(),
                    Date.valueOf(date));
            if (travelAgentRoomType != null) {
                result = travelAgentRoomType.getRoomNumber();
            }
        }
        return result;
    }

    private int getRoomReservedNumber(TblRoomType roomType, LocalDate date) {
        int result = 0;
        if (reservationController.reservationType.getIdtype() == 0) {   //Customer = '0'
            List<TblReservationRoomPriceDetail> roomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail relation = reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetail roomTypeDetail = reservationController.getFReservationManager().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                roomTypeDetail.setTblReservation(reservationController.getFReservationManager().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                roomTypeDetail.getTblReservation().setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
//                if (roomTypeDetail.getTblRoomType().getRoomTypeName().equals(roomType.getRoomTypeName())    @@@+++
                if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                        && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 0 //Customer = '0'
                        && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                    result++;
                }
            }
        } else {  //Travel Agent
            List<TblReservationRoomPriceDetail> roomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(date));
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail relation = reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(roomPriceDetail.getIddetail());
                TblReservationRoomTypeDetail roomTypeDetail = reservationController.getFReservationManager().getReservationRoomTypeDetail(relation.getTblReservationRoomTypeDetail().getIddetail());
                roomTypeDetail.setTblReservation(reservationController.getFReservationManager().getReservation(roomTypeDetail.getTblReservation().getIdreservation()));
                roomTypeDetail.getTblReservation().setRefReservationStatus(reservationController.getFReservationManager().getReservationStatus(roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
//                if (roomTypeDetail.getTblRoomType().getRoomTypeName().equals(roomType.getRoomTypeName())    @@@+++
                if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                        && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 1 //Travel Agent = '1'
                        && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                    TblReservationRoomTypeDetailTravelAgentDiscountDetail relation1 = reservationController.getFReservationManager().getReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(roomTypeDetail.getIddetail());
                    TblReservationTravelAgentDiscountDetail travelAgentDiscountDetail = reservationController.getFReservationManager().getReservationTravelAgentDiscountDetail(relation1.getTblReservationTravelAgentDiscountDetail().getIddetail());
                    if (reservationController.currentTravelAgent.getTblPartner().getIdpartner() == travelAgentDiscountDetail.getTblPartner().getIdpartner()) {
                        result++;
                    }
                }
            }
        }
        return result;
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
        initFormDataReservationChangeRoomType();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationChangeRoomTypeInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
