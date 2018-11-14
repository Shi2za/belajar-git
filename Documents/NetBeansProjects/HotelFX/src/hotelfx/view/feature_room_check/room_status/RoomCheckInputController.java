/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailItem;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomCheck;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendance;
import hotelfx.persistence.model.TblRoomCheckItemMutationHistory;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblSupplier;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
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
public class RoomCheckInputController implements Initializable {

    @FXML
    private GridPane gpFormDataRoomCheck;

    @FXML
    private Label lblSelectedData;
    
    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXDatePicker dtpCheckDate;

    @FXML
    private JFXTimePicker tmpCheckTime;

    @FXML
    private JFXTextArea txtNote;

    @FXML
    private AnchorPane ancRoomStatusLayout;
    private JFXCComboBoxTablePopup<RefRoomStatus> cbpRoomStatus;

    @FXML
    private AnchorPane ancEmployeeLayout;
    private JFXCComboBoxTablePopup<TblEmployee> cbpEmployee;

    @FXML
    private TitledPane tlpRoomItemList;
    
//    @FXML
//    private Label lblTitleListtem;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private TblReservationRoomTypeDetail dataRRTD = null;

    private LocalDate dataRRTDDate = null;

    private void initFormDataRoomCheck() {
        initDataPopup();
        
        //data rrtd
        setDataRRTD();

        btnSave.setTooltip(new Tooltip("Simpan (Data Cek-Kamar)"));
        btnSave.setOnAction((e) -> {
            dataRoomCheckSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoomCheckCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();
    }

    private void setDataRRTD() {
        TblReservationRoomTypeDetail previousDataRRTD = getPreviousRRTD(roomStatusController.selectedData.getTblRoom(), Date.valueOf(LocalDate.now().minusDays(1)));
        TblReservationRoomTypeDetail nextDataRRTD = getNextRRTD(roomStatusController.selectedData.getTblRoom(), Date.valueOf(LocalDate.now()));
        if (previousDataRRTD != null
                && nextDataRRTD != null) {
            if (previousDataRRTD.getIddetail()
                    == nextDataRRTD.getIddetail()) {
                SysCurrentHotelDate schd = roomStatusController.getService().getDataCurrentHotelDate(2); //House Keeping - Daily Cleaning = '2'
                if (LocalDateTime.now().isBefore(LocalDateTime.of(schd.getCurrentHotelDate().getYear() + 1900,
                        schd.getCurrentHotelDate().getMonth() + 1,
                        schd.getCurrentHotelDate().getDate(),
                        schd.getCurrentHotelDate().getHours(),
                        schd.getCurrentHotelDate().getMinutes()))) {
                    dataRRTD = new TblReservationRoomTypeDetail(previousDataRRTD);
                    dataRRTDDate = LocalDate.now().minusDays(1);
                } else {
                    dataRRTD = new TblReservationRoomTypeDetail(nextDataRRTD);
                    dataRRTDDate = LocalDate.now();
                }
            } else {
                dataRRTD = new TblReservationRoomTypeDetail(previousDataRRTD);
                dataRRTDDate = LocalDate.now().minusDays(1);
            }
        } else {
            if (previousDataRRTD != null) {
                dataRRTD = new TblReservationRoomTypeDetail(previousDataRRTD);
                dataRRTDDate = LocalDate.now().minusDays(1);
            } else {
                if (nextDataRRTD != null) {
                    dataRRTD = new TblReservationRoomTypeDetail(nextDataRRTD);
                    dataRRTDDate = LocalDate.now();
                }
            }
        }
    }

    private TblReservationRoomTypeDetail getPreviousRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = roomStatusController.getService().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = roomStatusController.getService().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = roomStatusController.getService().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(roomStatusController.getService().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(roomStatusController.getService().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusController.getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0 //Ready to Check In = '0'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
                            //data reservation
                            rrtd.setTblReservation(roomStatusController.getService().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(roomStatusController.getService().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(roomStatusController.getService().getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(roomStatusController.getService().getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
                                //retuurn rrtd
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private TblReservationRoomTypeDetail getNextRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = roomStatusController.getService().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = roomStatusController.getService().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = roomStatusController.getService().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(roomStatusController.getService().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(roomStatusController.getService().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusController.getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0 //Ready to Check In = '0'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
                            //data reservation
                            rrtd.setTblReservation(roomStatusController.getService().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(roomStatusController.getService().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(roomStatusController.getService().getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(roomStatusController.getService().getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
                                //return rrtd
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpCheckDate);
//        ClassFormatter.setDatePickersEnableDate(LocalDate.now(),
//                dtpCheckDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpRoomStatus,
                cbpEmployee,
                dtpCheckDate,
                tmpCheckTime);
    }

    private void initDataPopup() {
        //Room Status
        TableView<RefRoomStatus> tableRoomStatus = new TableView<>();

        TableColumn<RefRoomStatus, String> roomStatusName = new TableColumn<>("Status");
        roomStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        roomStatusName.setMinWidth(140);

        TableColumn<RefRoomStatus, String> roomStatusNote = new TableColumn<>("Keterangan");
        roomStatusNote.setCellValueFactory(cellData -> cellData.getValue().statusNoteProperty());
        roomStatusNote.setMinWidth(180);

        tableRoomStatus.getColumns().addAll(roomStatusName, roomStatusNote);

        ObservableList<RefRoomStatus> roomStatusItems = FXCollections.observableArrayList(loadAllDataRoomStatus());

        cbpRoomStatus = new JFXCComboBoxTablePopup<>(
                RefRoomStatus.class, tableRoomStatus, roomStatusItems, "", "Status Kamar *", true, 330, 200
        );
        
        //Employee
        TableView<TblEmployee> tableEmployee = new TableView<>();

        TableColumn<TblEmployee, String> codeEmployee = new TableColumn<>("ID");
        codeEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        codeEmployee.setMinWidth(140);

        TableColumn<TblEmployee, String> employeeName = new TableColumn<>("Nama");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().tblPeopleProperty()));
        employeeName.setMinWidth(140);

        tableEmployee.getColumns().addAll(codeEmployee, employeeName);

        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpEmployee = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableEmployee, employeeItems, "", "Pengecek *", true, 300, 200
        );
        
        //attached to grid-pane
        ancRoomStatusLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setRightAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setTopAnchor(cbpRoomStatus, 0.0);
        ancRoomStatusLayout.getChildren().add(cbpRoomStatus);
        ancEmployeeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpEmployee, 0.0);
        AnchorPane.setTopAnchor(cbpEmployee, 0.0);
        ancEmployeeLayout.getChildren().add(cbpEmployee);
    }

    private ObservableList<RefRoomStatus> loadAllDataRoomStatus() {
        List<RefRoomStatus> list = roomStatusController.getService().getAllDataRoomStatus();
        if (roomStatusController.selectedData != null
                && roomStatusController.selectedData.getTblRoom() != null
                && roomStatusController.selectedData.getTblRoom().getRefRoomStatus() != null) {
            String vooStatus = "";
            if (roomStatusController.selectedData.getTblRoom().getRefRoomStatus().getIdstatus() == 6) {    //Out Of Order
                vooStatus = "3O";
            } else {
                if (roomStatusController.selectedData.getTblRoom().getRefRoomStatus().getIdstatus() == 1 //Occupied Clean = '1'
                        || roomStatusController.selectedData.getTblRoom().getRefRoomStatus().getIdstatus() == 2) {    //Occupied Dirty = '2'
                    vooStatus = "Occupied";
                } else {
                    vooStatus = "Vacant";
                }
            }
            switch (vooStatus) {
                case "Vacant":
                    for (int i = list.size() - 1; i > -1; i--) {
                        if (list.get(i).getIdstatus() != 3 //Vacant Clean = '3'
                                && list.get(i).getIdstatus() != 4 //Vacant Dirty = '4'
                                && list.get(i).getIdstatus() != 5 //Vacant Ready = '5'
                                && list.get(i).getIdstatus() != 6) {    //Out Of Order = '6'
                            list.remove(i);
                        }
                    }
                    break;
                case "Occupied":
                    for (int i = list.size() - 1; i > -1; i--) {
                        if (list.get(i).getIdstatus() != 1 //Occupied Clean = '1'
                                && list.get(i).getIdstatus() != 2 //Occupied Dirty = '2'
                                && list.get(i).getIdstatus() != 6) {    //Out Of Order = '6'                            
                            list.remove(i);
                        }
                    }
                    break;
                case "3O":
                    System.out.println("c");
                    for (int i = list.size() - 1; i > -1; i--) {
                        if (list.get(i).getIdstatus() != 3 //Vacant Clean = '3'
                                && list.get(i).getIdstatus() != 4 //Vacant Dirty = '4'
                                && list.get(i).getIdstatus() != 5 //Vacant Ready = '5'
                                && list.get(i).getIdstatus() != 6) {    //Out Of Order = '6'
                            list.remove(i);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private ObservableList<TblEmployee> loadAllDataEmployee() {
        List<TblEmployee> list = roomStatusController.getService().getAllDataEmployee();
        for (TblEmployee data : list) {
            data.setTblPeople(roomStatusController.getService().getDataPeople(data.getTblPeople().getIdpeople()));
        }
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Room Status
        ObservableList<RefRoomStatus> roomStatusItems = FXCollections.observableArrayList(loadAllDataRoomStatus());
        cbpRoomStatus.setItems(roomStatusItems);

        //Employee
        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
        cbpEmployee.setItems(employeeItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

//        TblRoomCheckHouseKeepingAttendance rchka = roomStatusController.getService().getDataRoomCheckHouseKeepingAttendanceByIDRoom();
//        lblSelectedData.setText(roomStatusController.selectedData.getTblRoom().getCodeRoom()
//                + " - "
//                + (rchka != null ? rchka.getTblEmployeeByIdchecker().getTblPeople().getFullName() : "")
//                + " - "
//                + (rchka != null ? rchka.getTblEmployeeByIdchecker().getTblPeople().getFullName() : "")
//        );
        
        tlpRoomItemList.setText(getDataTitleListItem());

        txtRoomName.textProperty().bindBidirectional(roomStatusController.selectedData.getTblRoom().roomNameProperty());
        txtNote.textProperty().bindBidirectional(roomStatusController.selectedData.checkNoteProperty());

        if (roomStatusController.selectedData.getCheckDateTime() != null) {
            dtpCheckDate.setValue(roomStatusController.selectedData.getCheckDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            tmpCheckTime.setValue(roomStatusController.selectedData.getCheckDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        } else {
            dtpCheckDate.setValue(null);
            tmpCheckTime.setValue(null);
        }

        cbpRoomStatus.valueProperty().bindBidirectional(roomStatusController.selectedData.getTblRoom().refRoomStatusProperty());
        cbpEmployee.valueProperty().bindBidirectional(roomStatusController.selectedData.tblEmployeeByIdcheckerProperty());

        cbpRoomStatus.hide();
        cbpEmployee.hide();

        initTableDataIn();

        initTableDataOut();

        initTableDataRoom();
    }

    private String getDataTitleListItem() {
        if (dataRRTD != null
                && dataRRTDDate != null) {
            return "Daftar Barang : " + dataRRTD.getTblReservation().getTblCustomer().getTblPeople().getFullName();
        }
        return "Daftar Barang";
    }

    /**
     * HANDLE
     */
    private void dataRoomCheckSaveHandle() {
        if (checkDataInputDataRoomCheck()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                LocalDateTime localDateTime = LocalDateTime.of(dtpCheckDate.getValue(), tmpCheckTime.getValue());
                roomStatusController.selectedData.setCheckDateTime(Timestamp.valueOf(localDateTime));
                //dummy entry
                TblRoomCheck dummySelectedData = new TblRoomCheck(roomStatusController.selectedData);
                dummySelectedData.setTblRoom(new TblRoom(dummySelectedData.getTblRoom()));
                dummySelectedData.getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedData.getTblRoom().getRefRoomStatus()));
                List<TblRoomCheckItemMutationHistory> dummyDataRCIMHins = new ArrayList<>();
                List<TblRoomCheckItemMutationHistory> dummyDataRCIMHouts = new ArrayList<>();
                List<TblItemMutationHistoryPropertyBarcode> dummyItemMutationHistoryPropertyBarcodes = new ArrayList<>();
                List<TblItemMutationHistoryItemExpiredDate> dummyItemMutationHistoryItemExpiredDates = new ArrayList<>();
                for (TblRoomCheckItemMutationHistory dataRCIMHin : (List<TblRoomCheckItemMutationHistory>) tableDataIn.getTableView().getItems()) {
                    TblRoomCheckItemMutationHistory dummyDataRCIMHin = new TblRoomCheckItemMutationHistory(dataRCIMHin);
                    dummyDataRCIMHin.setTblRoomCheck(dummySelectedData);
                    dummyDataRCIMHin.setTblItemMutationHistory(new TblItemMutationHistory(dummyDataRCIMHin.getTblItemMutationHistory()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setTblItem(new TblItem(dummyDataRCIMHin.getTblItemMutationHistory().getTblItem()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation(dummyDataRCIMHin.getTblItemMutationHistory().getTblLocationByIdlocationOfSource()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(new TblLocation(dummyDataRCIMHin.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()));
                    dummyDataRCIMHin.getTblItemMutationHistory().setRefItemMutationType(new RefItemMutationType(dummyDataRCIMHin.getTblItemMutationHistory().getRefItemMutationType()));
                    if (dummyDataRCIMHin.getTblItemMutationHistory().getTblEmployeeByIdemployee() != null) {
                        dummyDataRCIMHin.getTblItemMutationHistory().setTblEmployeeByIdemployee(new TblEmployee(dummyDataRCIMHin.getTblItemMutationHistory().getTblEmployeeByIdemployee()));
                    }
                    if (dummyDataRCIMHin.getTblItemMutationHistory().getRefItemObsoleteBy() != null) {
                        dummyDataRCIMHin.getTblItemMutationHistory().setRefItemObsoleteBy(new RefItemObsoleteBy(dummyDataRCIMHin.getTblItemMutationHistory().getRefItemObsoleteBy()));
                    }
                    for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : roomStatusController.itemMutationHistoryPropertyBarcodes) {
                        if (itemMutationHistoryPropertyBarcode.getTblItemMutationHistory().equals(dataRCIMHin.getTblItemMutationHistory())) {
                            TblItemMutationHistoryPropertyBarcode dummyItemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode(itemMutationHistoryPropertyBarcode);
                            dummyItemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dummyDataRCIMHin.getTblItemMutationHistory());
                            dummyItemMutationHistoryPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(dummyItemMutationHistoryPropertyBarcode.getTblPropertyBarcode()));
                            dummyItemMutationHistoryPropertyBarcodes.add(dummyItemMutationHistoryPropertyBarcode);
                        }
                    }
                    for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : roomStatusController.itemMutationHistoryItemExpiredDates) {
                        if (itemMutationHistoryItemExpiredDate.getTblItemMutationHistory().equals(dataRCIMHin.getTblItemMutationHistory())) {
                            TblItemMutationHistoryItemExpiredDate dummyItemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate(itemMutationHistoryItemExpiredDate);
                            dummyItemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dummyDataRCIMHin.getTblItemMutationHistory());
                            dummyItemMutationHistoryItemExpiredDate.setTblItemExpiredDate(new TblItemExpiredDate(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate()));
                            dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().setTblItem(new TblItem(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getTblItem()));
                            dummyItemMutationHistoryItemExpiredDates.add(dummyItemMutationHistoryItemExpiredDate);
                        }
                    }
                    dummyDataRCIMHins.add(dummyDataRCIMHin);
                }
                for (TblRoomCheckItemMutationHistory dataRCIMHout : (List<TblRoomCheckItemMutationHistory>) tableDataOut.getTableView().getItems()) {
                    TblRoomCheckItemMutationHistory dummyDataRCIMHout = new TblRoomCheckItemMutationHistory(dataRCIMHout);
                    dummyDataRCIMHout.setTblRoomCheck(dummySelectedData);
                    dummyDataRCIMHout.setTblItemMutationHistory(new TblItemMutationHistory(dummyDataRCIMHout.getTblItemMutationHistory()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setTblItem(new TblItem(dummyDataRCIMHout.getTblItemMutationHistory().getTblItem()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation(dummyDataRCIMHout.getTblItemMutationHistory().getTblLocationByIdlocationOfSource()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(new TblLocation(dummyDataRCIMHout.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()));
                    dummyDataRCIMHout.getTblItemMutationHistory().setRefItemMutationType(new RefItemMutationType(dummyDataRCIMHout.getTblItemMutationHistory().getRefItemMutationType()));
                    if (dummyDataRCIMHout.getTblItemMutationHistory().getTblEmployeeByIdemployee() != null) {
                        dummyDataRCIMHout.getTblItemMutationHistory().setTblEmployeeByIdemployee(new TblEmployee(dummyDataRCIMHout.getTblItemMutationHistory().getTblEmployeeByIdemployee()));
                    }
                    if (dummyDataRCIMHout.getTblItemMutationHistory().getRefItemObsoleteBy() != null) {
                        dummyDataRCIMHout.getTblItemMutationHistory().setRefItemObsoleteBy(new RefItemObsoleteBy(dummyDataRCIMHout.getTblItemMutationHistory().getRefItemObsoleteBy()));
                    }
                    for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : roomStatusController.itemMutationHistoryPropertyBarcodes) {
                        if (itemMutationHistoryPropertyBarcode.getTblItemMutationHistory().equals(dataRCIMHout.getTblItemMutationHistory())) {
                            TblItemMutationHistoryPropertyBarcode dummyItemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode(itemMutationHistoryPropertyBarcode);
                            dummyItemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dummyDataRCIMHout.getTblItemMutationHistory());
                            dummyItemMutationHistoryPropertyBarcode.setTblPropertyBarcode(new TblPropertyBarcode(dummyItemMutationHistoryPropertyBarcode.getTblPropertyBarcode()));
                            dummyItemMutationHistoryPropertyBarcodes.add(dummyItemMutationHistoryPropertyBarcode);
                        }
                    }
                    for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : roomStatusController.itemMutationHistoryItemExpiredDates) {
                        if (itemMutationHistoryItemExpiredDate.getTblItemMutationHistory().equals(dataRCIMHout.getTblItemMutationHistory())) {
                            TblItemMutationHistoryItemExpiredDate dummyItemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate(itemMutationHistoryItemExpiredDate);
                            dummyItemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dummyDataRCIMHout.getTblItemMutationHistory());
                            dummyItemMutationHistoryItemExpiredDate.setTblItemExpiredDate(new TblItemExpiredDate(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate()));
                            dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().setTblItem(new TblItem(dummyItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getTblItem()));
                            dummyItemMutationHistoryItemExpiredDates.add(dummyItemMutationHistoryItemExpiredDate);
                        }
                    }
                    dummyDataRCIMHouts.add(dummyDataRCIMHout);
                }
                switch (roomStatusController.dataInputStatus) {
                    case 0:
                        if (roomStatusController.getService().insertDataRoomCheck(dummySelectedData,
                                dummyDataRCIMHins,
                                dummyDataRCIMHouts,
                                dummyItemMutationHistoryPropertyBarcodes,
                                dummyItemMutationHistoryItemExpiredDates
                        ) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data room check
                            roomStatusController.setSelectedDataToInputForm();
                            roomStatusController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(roomStatusController.getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (roomStatusController.getService().updateDataRoomCheck(dummySelectedData,
                                dummyDataRCIMHins,
                                dummyDataRCIMHouts,
                                dummyItemMutationHistoryPropertyBarcodes,
                                dummyItemMutationHistoryItemExpiredDates)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data room check
                            roomStatusController.setSelectedDataToInputForm();
                            roomStatusController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(roomStatusController.getService().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataRoomCheckCancelHandle() {
        //refresh data from table & close form data room check
        roomStatusController.setSelectedDataToInputForm();
        roomStatusController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataRoomCheck() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpRoomStatus.getValue() == null) {
            dataInput = false;
            errDataInput += "Status Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpEmployee.getValue() == null) {
            dataInput = false;
            errDataInput += "Pengecek Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpCheckDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Cek Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (tmpCheckTime.getValue() == null) {
            dataInput = false;
            errDataInput += "Waktu Cek Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private final PseudoClass uneditablePseudoClass = PseudoClass.getPseudoClass("uneditable");

    /**
     * DATA (ROOM ~ [RESERVATION])
     */
    @FXML
    private AnchorPane tableDataRoomLayout;

    public TableView tableDataRoom;

    private void initTableDataRoom() {
        //set table
        setTableDataRoom();
        //set table-control to content-view
        tableDataRoomLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoom, 0.0);
        AnchorPane.setLeftAnchor(tableDataRoom, 0.0);
        AnchorPane.setRightAnchor(tableDataRoom, 0.0);
        AnchorPane.setTopAnchor(tableDataRoom, 0.0);
        tableDataRoomLayout.getChildren().add(tableDataRoom);
    }

    private void setTableDataRoom() {
        TableView<DataItemQuantity> tableView = new TableView();

        TableColumn<DataItemQuantity, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<DataItemQuantity, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getDataItem().getCodeItem() + " - \n"
                        + param.getValue().getDataItem().getItemName(),
                        param.getValue().dataItemProperty()));
        itemName.setMinWidth(150);

        TableColumn<DataItemQuantity, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<DataItemQuantity, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()),
                        param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<DataItemQuantity, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<DataItemQuantity, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataItem().getTblUnit().getUnitName(),
                        param.getValue().dataItemProperty()));
        itemUnit.setMinWidth(100);

        tableView.getColumns().addAll(itemName, itemQuantity);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataItemQuantity()));

        tableDataRoom = tableView;
    }

    private List<DataItemQuantity> loadAllDataItemQuantity() {
        List<DataItemQuantity> list = new ArrayList<>();
        if (dataRRTD != null
                && dataRRTDDate != null) {   //by : reservation room type detail
            list = getListItemQuantityByDataRRTDAndDate(dataRRTD, Date.valueOf(dataRRTDDate));
        } else {  //by : room type
            List<TblRoomTypeItem> roomTypeItems = roomStatusController.getService().getAllDataRoomTypeItemByIDRoomType(roomStatusController.selectedData.getTblRoom().getTblRoomType().getIdroomType());
            for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                if (roomTypeItem.getTblItem().getGuestStatus()) {  //Guest
                    //data item
                    TblItem data = roomStatusController.getService().getDataItem(roomTypeItem.getTblItem().getIditem());
                    //data unit
                    data.setTblUnit(roomStatusController.getService().getDataUnit(data.getTblUnit().getIdunit()));
                    //data item type hk
                    if(data.getTblItemTypeHk() != null){
                        data.setTblItemTypeHk(roomStatusController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
                    }
                    //data item type wh
                    if(data.getTblItemTypeWh() != null){
                        data.setTblItemTypeWh(roomStatusController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
                    }
                    //add data item - quantity to list
                    list.add(new DataItemQuantity(data, roomTypeItem.getItemQuantity()));
                }
            }
        }
        return list;
    }

    private List<DataItemQuantity> getListItemQuantityByDataRRTDAndDate(
            TblReservationRoomTypeDetail dataRRTD,
            Date date) {
        List<DataItemQuantity> list = new ArrayList<>();
        //data reservation room type detail - item
        List<TblReservationRoomTypeDetailItem> dataReservationRoomTypeDetailItems = roomStatusController.getService().getAllDataReservationRoomTypeDetailItemByIDReservationRoomTypeDetail(dataRRTD.getIddetail());
        for (TblReservationRoomTypeDetailItem dataReservationRoomTypeDetailItem : dataReservationRoomTypeDetailItems) {
            boolean hasBeenAdded = false;
            for (DataItemQuantity data : list) {
                if (data.getDataItem().getIditem() == dataReservationRoomTypeDetailItem.getTblItem().getIditem()) {
                    //add item quantity
                    data.setItemQuantity(data.getItemQuantity().add(dataReservationRoomTypeDetailItem.getItemQuantity()));
                    hasBeenAdded = true;
                    break;
                }
            }
            if (!hasBeenAdded) {
                //data item
                TblItem item = roomStatusController.getService().getDataItem(dataReservationRoomTypeDetailItem.getTblItem().getIditem());
                //data unit
                item.setTblUnit(roomStatusController.getService().getDataUnit(item.getTblUnit().getIdunit()));
                //data item type hk
                if(item.getTblItemTypeHk() != null){
                    item.setTblItemTypeHk(roomStatusController.getService().getDataItemTypeHK(item.getTblItemTypeHk().getIditemTypeHk()));
                }
                //data item type wh
                if(item.getTblItemTypeWh() != null){
                    item.setTblItemTypeWh(roomStatusController.getService().getDataItemTypeWH(item.getTblItemTypeWh().getIditemTypeWh()));
                }
                //add data item to list (+set item quantity)
                list.add(new DataItemQuantity(item, dataReservationRoomTypeDetailItem.getItemQuantity()));
            }
        }
        //data reservation addtional item -> by reservation room type detail
        List<TblReservationAdditionalItem> dataReservationAdditionalItems = roomStatusController.getService().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAndAdditionalDate(
                dataRRTD.getIddetail(),
                date
        );
        for (TblReservationAdditionalItem dataReservationAdditionalItem : dataReservationAdditionalItems) {
            boolean hasBeenAdded = false;
            for (DataItemQuantity data : list) {
                if (data.getDataItem().getIditem() == dataReservationAdditionalItem.getTblItem().getIditem()) {
                    if ((dataReservationAdditionalItem.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || dataReservationAdditionalItem.getRefReservationBillType().getIdtype() == 1)) {      //Check Out = '1'
                        //add item quantity
                        data.setItemQuantity(data.getItemQuantity().add(dataReservationAdditionalItem.getItemQuantity()));
                    }
                    hasBeenAdded = true;
                    break;
                }
            }
            if (!hasBeenAdded) {
                //data item
                TblItem item = roomStatusController.getService().getDataItem(dataReservationAdditionalItem.getTblItem().getIditem());
                //data unit
                item.setTblUnit(roomStatusController.getService().getDataUnit(item.getTblUnit().getIdunit()));
                //data item type hk
                if(item.getTblItemTypeHk() != null){
                    item.setTblItemTypeHk(roomStatusController.getService().getDataItemTypeHK(item.getTblItemTypeHk().getIditemTypeHk()));
                }
                //data item type wh
                if(item.getTblItemTypeWh() != null){
                    item.setTblItemTypeWh(roomStatusController.getService().getDataItemTypeWH(item.getTblItemTypeWh().getIditemTypeWh()));
                }
                //add data item to list
                list.add(new DataItemQuantity(item, dataReservationAdditionalItem.getItemQuantity()));
            }
        }
        return list;
    }

    public class DataItemQuantity {

        private final ObjectProperty<TblItem> dataItem = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> itemQuantity = new SimpleObjectProperty<>();

        public DataItemQuantity(
                TblItem dataItem,
                BigDecimal itemQuantity) {
            this.dataItem.set(dataItem);
            this.itemQuantity.set(itemQuantity);
        }

        public ObjectProperty<TblItem> dataItemProperty() {
            return dataItem;
        }

        public TblItem getDataItem() {
            return dataItemProperty().get();
        }

        public void setDataItem(TblItem dataItem) {
            dataItemProperty().set(dataItem);
        }

        public ObjectProperty<BigDecimal> itemQuantityProperty() {
            return itemQuantity;
        }

        public BigDecimal getItemQuantity() {
            return itemQuantityProperty().get();
        }

        public void setItemQuantity(BigDecimal itemQuantity) {
            itemQuantityProperty().set(itemQuantity);
        }

    }

    /**
     * DATA (IN)
     */
    @FXML
    private AnchorPane tableDataInLayout;

    public ClassTableWithControl tableDataIn;

    private void initTableDataIn() {
        //set table
        setTableDataIn();
        //set control
        setTableControlDataIn();
        //set table-control to content-view
        tableDataInLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataIn, 0.0);
        AnchorPane.setLeftAnchor(tableDataIn, 0.0);
        AnchorPane.setRightAnchor(tableDataIn, 0.0);
        AnchorPane.setTopAnchor(tableDataIn, 0.0);
        tableDataInLayout.getChildren().add(tableDataIn);
    }

    private void setTableDataIn() {
        TableView<TblRoomCheckItemMutationHistory> tableView = new TableView();

        TableColumn<TblRoomCheckItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblItemMutationHistory().getTblItem().getCodeItem() + " - \n"
                        + param.getValue().getTblItemMutationHistory().getTblItem().getItemName()
                        + getDataPropertyBarcode(param.getValue().getTblItemMutationHistory())
                        + getDataExpiredDate(param.getValue().getTblItemMutationHistory()),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getTblItemMutationHistory().getItemQuantity()), param.getValue().tblItemMutationHistoryProperty()));
        itemQuantity.setMinWidth(80);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemUnit.setMinWidth(100);

        TableColumn<TblRoomCheckItemMutationHistory, String> sourceLocationName = new TableColumn("Lokasi (Sumber)");
        sourceLocationName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> getLocationName(param.getValue().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()), param.getValue().tblItemMutationHistoryProperty()));
        sourceLocationName.setMinWidth(140);

        TableColumn<TblRoomCheckItemMutationHistory, String> mutationAllStatus = new TableColumn("Status");
        mutationAllStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemMutationType().getTypeName()
                        + (param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy() != null
                                ? "\n(" + param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy().getObsoleteByName() + ")"
                                : ""),
                        param.getValue().tblItemMutationHistoryProperty()));
        mutationAllStatus.setMinWidth(120);

        tableView.getColumns().addAll(itemName, itemQuantity, itemUnit, sourceLocationName, mutationAllStatus);
        tableView.setItems(loadAllDataIn());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoomCheckItemMutationHistory> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(uneditablePseudoClass, (newVal.getCreateDate() != null));
                }
            });
            return row;
        });

        tableDataIn = new ClassTableWithControl(tableView);
    }

    private String getDataPropertyBarcode(TblItemMutationHistory dataItemMutationHistory) {
        String result = "";
        if (dataItemMutationHistory != null) {
            for (TblItemMutationHistoryPropertyBarcode dataItemMutationHistoryPropertyBarcode : roomStatusController.itemMutationHistoryPropertyBarcodes) {
                if (dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().equals(dataItemMutationHistory)) {
                    result += "\n(BC : " + dataItemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getCodeBarcode() + ")";
                    break;
                }
            }
        }
        return result;
    }

    private String getDataExpiredDate(TblItemMutationHistory dataItemMutationHistory) {
        String result = "";
        if (dataItemMutationHistory != null) {
            for (TblItemMutationHistoryItemExpiredDate dataItemMutationHistoryItemExpiredDate : roomStatusController.itemMutationHistoryItemExpiredDates) {
                if (dataItemMutationHistoryItemExpiredDate.getTblItemMutationHistory().equals(dataItemMutationHistory)) {
                    result += "\n(Exp. : " + ClassFormatter.dateFormate.format(dataItemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate()) + ")";
                    break;
                }
            }
        }
        return result;
    }
    
    private String getLocationName(TblLocation tblLocation) {
        String locationName = "";
        switch (tblLocation.getRefLocationType().getIdtype()) {
            case 0:  //Kamar = '0'
                TblRoom room = roomStatusController.getService().getDataRoomByIDLocation(tblLocation.getIdlocation());
                locationName = room != null ? room.getRoomName() : "";
                break;
            case 1:   //Gudang = '1'
                TblLocationOfWarehouse warehouse = roomStatusController.getService().getDataWarehouseByIDLocation(tblLocation.getIdlocation());
                locationName = warehouse != null ? warehouse.getWarehouseName() : "";
                break;
            case 2: //Laundry = '2'
                TblLocationOfLaundry laundry = roomStatusController.getService().getDataLaundryByIDLocation(tblLocation.getIdlocation());
                locationName = laundry != null ? laundry.getLaundryName() : "";
                break;
            case 3:    //Supplier = '3'
                TblSupplier supplier = roomStatusController.getService().getDataSupplierByIDLocation(tblLocation.getIdlocation());
                locationName = supplier != null ? supplier.getSupplierName() : "";
                break;
            case 4: //4 = 'Bin'
                TblLocationOfBin bin = roomStatusController.getService().getDataBinByIDLocation(tblLocation.getIdlocation());
                locationName = bin != null ? bin.getBinName() : "";
                break;
            default:
                break;
        }
        return locationName;
    }

    private void setTableControlDataIn() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataInCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataInUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataInDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataIn.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomCheckItemMutationHistory> loadAllDataIn() {
        ObservableList<TblRoomCheckItemMutationHistory> list = FXCollections.observableArrayList(roomStatusController.getService().getAllDataRoomCheckItemMutationHistoryByIDRoomCheck(roomStatusController.selectedData.getIdroomCheck()));
        for (TblRoomCheckItemMutationHistory data : list) {
            //set data room check
            data.setTblRoomCheck(roomStatusController.getService().getDataRoomCheck(data.getTblRoomCheck().getIdroomCheck()));
            //set data item mutation
            data.setTblItemMutationHistory(roomStatusController.getService().getDataItemMutationHistory(data.getTblItemMutationHistory().getIdmutation()));
            //set data item
            data.getTblItemMutationHistory().setTblItem(roomStatusController.getService().getDataItem(data.getTblItemMutationHistory().getTblItem().getIditem()));
            //set data unit
            data.getTblItemMutationHistory().getTblItem().setTblUnit(roomStatusController.getService().getDataUnit(data.getTblItemMutationHistory().getTblItem().getTblUnit().getIdunit()));
            //set data item type hk
            if(data.getTblItemMutationHistory().getTblItem().getTblItemTypeHk() != null){
                data.getTblItemMutationHistory().getTblItem().setTblItemTypeHk(roomStatusController.getService().getDataItemTypeHK(data.getTblItemMutationHistory().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type wh
            if(data.getTblItemMutationHistory().getTblItem().getTblItemTypeWh() != null){
                data.getTblItemMutationHistory().getTblItem().setTblItemTypeWh(roomStatusController.getService().getDataItemTypeWH(data.getTblItemMutationHistory().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
            //set data location
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(roomStatusController.getService().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()));
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(roomStatusController.getService().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
            //set data mutation type
            data.getTblItemMutationHistory().setRefItemMutationType(roomStatusController.getService().getDataMutationType(data.getTblItemMutationHistory().getRefItemMutationType().getIdtype()));
            //set data obsolete by
            if (data.getTblItemMutationHistory().getRefItemMutationType().getIdtype() == 2) {   //Rusak = '2'
                data.getTblItemMutationHistory().setRefItemObsoleteBy(roomStatusController.getService().getDataObsoleteBy(data.getTblItemMutationHistory().getRefItemObsoleteBy().getIdobsoleteBy()));
            }
        }
        //data - in
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()
                    == roomStatusController.selectedData.getTblRoom().getTblLocation().getIdlocation()) {
                list.remove(i);
            }
        }
        //data - item location mutation history property barcode
        for (TblRoomCheckItemMutationHistory data : list) {
            if (data.getTblItemMutationHistory().getTblItem().getPropertyStatus()) {   //Property
                List<TblItemMutationHistoryPropertyBarcode> dataIMHPBs = roomStatusController.getService().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for (TblItemMutationHistoryPropertyBarcode dataIMHPB : dataIMHPBs) {
                    dataIMHPB.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHPB.setTblPropertyBarcode(roomStatusController.getService().getDataPropertyBarcode(dataIMHPB.getTblPropertyBarcode().getIdbarcode()));
                    roomStatusController.itemMutationHistoryPropertyBarcodes.add(dataIMHPB);
                }
            }
        }
        //data - item location mutation history item expired date
        for(TblRoomCheckItemMutationHistory data : list){
            if(data.getTblItemMutationHistory().getTblItem().getConsumable()){
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = roomStatusController.getService().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for(TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs){
                    dataIMHIED.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHIED.setTblItemExpiredDate(roomStatusController.getService().getDataItemExpiredDate(dataIMHIED.getTblItemExpiredDate().getIditemExpiredDate()));
                    roomStatusController.itemMutationHistoryItemExpiredDates.add(dataIMHIED);
                }
            }
        }
        return list;
    }

    public TblRoomCheckItemMutationHistory selectedDataIn;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputInStatus = 0;

    public Stage dialogStageIn;

    public void dataInCreateHandle() {
        dataInputInStatus = 0;
        selectedDataIn = new TblRoomCheckItemMutationHistory();
        selectedDataIn.setTblRoomCheck(roomStatusController.selectedData);
        selectedDataIn.setTblItemMutationHistory(new TblItemMutationHistory());
        selectedDataIn.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(new TblLocation());
        selectedDataIn.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(roomStatusController.selectedData.getTblRoom().getTblLocation());
        selectedDataIn.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
        selectedDataIn.getTblItemMutationHistory().setRefItemMutationType(roomStatusController.getService().getDataMutationType(0));  //Dipindahkan = '0'
        //open form data - in
        showDataInDialog();
    }

    public void dataInUpdateHandle() {
        if (tableDataIn.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputInStatus = 1;
            selectedDataIn = (TblRoomCheckItemMutationHistory) tableDataIn.getTableView().getSelectionModel().getSelectedItem();
            //open form data - in
            showDataInDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataInDeleteHandle() {
        if (tableDataIn.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblRoomCheckItemMutationHistory) tableDataIn.getTableView().getSelectionModel().getSelectedItem()).getCreateDate() == null) {
                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassMessage.showSucceedRemovingDataMessage("", null);
                    //remove data item mutation history - proeprty barcode
                    for (int i = roomStatusController.itemMutationHistoryPropertyBarcodes.size() - 1; i > -1; i--) {
                        if (roomStatusController.itemMutationHistoryPropertyBarcodes.get(i).getTblItemMutationHistory().equals(tableDataIn.getTableView().getSelectionModel().getSelectedItem())) {
                            roomStatusController.itemMutationHistoryPropertyBarcodes.remove(i);
                            break;
                        }
                    }
                    //remove data from table items list
                    tableDataIn.getTableView().getItems().remove(tableDataIn.getTableView().getSelectionModel().getSelectedItem());
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Data tidak dapat dihapus..!", "");
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataInDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/DataInDialog.fxml"));

            DataInController controller = new DataInController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageIn = new Stage();
            dialogStageIn.initModality(Modality.WINDOW_MODAL);
            dialogStageIn.initOwner(roomStatusController.dialogStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageIn, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageIn.initStyle(StageStyle.TRANSPARENT);
            dialogStageIn.setScene(scene);
            dialogStageIn.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageIn.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    /**
     * DATA (OUT)
     */
    @FXML
    private AnchorPane tableDataOutLayout;

    public ClassTableWithControl tableDataOut;

    private void initTableDataOut() {
        //set table
        setTableDataOut();
        //set control
        setTableControlDataOut();
        //set table-control to content-view
        tableDataOutLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataOut, 0.0);
        AnchorPane.setLeftAnchor(tableDataOut, 0.0);
        AnchorPane.setRightAnchor(tableDataOut, 0.0);
        AnchorPane.setTopAnchor(tableDataOut, 0.0);
        tableDataOutLayout.getChildren().add(tableDataOut);
    }

    private void setTableDataOut() {
        TableView<TblRoomCheckItemMutationHistory> tableView = new TableView();

        TableColumn<TblRoomCheckItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblItemMutationHistory().getTblItem().getCodeItem() + " - \n"
                        + param.getValue().getTblItemMutationHistory().getTblItem().getItemName()
                        + getDataPropertyBarcode(param.getValue().getTblItemMutationHistory())
                        + getDataExpiredDate(param.getValue().getTblItemMutationHistory()),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getTblItemMutationHistory().getItemQuantity()), param.getValue().tblItemMutationHistoryProperty()));
        itemQuantity.setMinWidth(80);

        TableColumn<TblRoomCheckItemMutationHistory, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().tblItemMutationHistoryProperty()));
        itemUnit.setMinWidth(100);

        TableColumn<TblRoomCheckItemMutationHistory, String> destinationLocationName = new TableColumn("Lokasi (Tujuan)");
        destinationLocationName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> getLocationName(param.getValue().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination()), param.getValue().tblItemMutationHistoryProperty()));
        destinationLocationName.setMinWidth(140);

        TableColumn<TblRoomCheckItemMutationHistory, String> mutationAllStatus = new TableColumn("Status");
        mutationAllStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemMutationHistory().getRefItemMutationType().getTypeName()
                        + (param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy() != null
                                ? "\n(" + param.getValue().getTblItemMutationHistory().getRefItemObsoleteBy().getObsoleteByName() + ")"
                                : ""),
                        param.getValue().tblItemMutationHistoryProperty()));
        mutationAllStatus.setMinWidth(120);

        tableView.getColumns().addAll(itemName, itemQuantity, itemUnit, destinationLocationName, mutationAllStatus);
        tableView.setItems(loadAllDataOut());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoomCheckItemMutationHistory> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(uneditablePseudoClass, (newVal.getCreateDate() != null));
                }
            });
            return row;
        });

        tableDataOut = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataOut() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataOutCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataOutUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataOutDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataOut.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomCheckItemMutationHistory> loadAllDataOut() {
        ObservableList<TblRoomCheckItemMutationHistory> list = FXCollections.observableArrayList(roomStatusController.getService().getAllDataRoomCheckItemMutationHistoryByIDRoomCheck(roomStatusController.selectedData.getIdroomCheck()));
        for (TblRoomCheckItemMutationHistory data : list) {
            //set data room check
            data.setTblRoomCheck(roomStatusController.getService().getDataRoomCheck(data.getTblRoomCheck().getIdroomCheck()));
            //set data item mutation
            data.setTblItemMutationHistory(roomStatusController.getService().getDataItemMutationHistory(data.getTblItemMutationHistory().getIdmutation()));
            //set data item
            data.getTblItemMutationHistory().setTblItem(roomStatusController.getService().getDataItem(data.getTblItemMutationHistory().getTblItem().getIditem()));
            //set data unit
            data.getTblItemMutationHistory().getTblItem().setTblUnit(roomStatusController.getService().getDataUnit(data.getTblItemMutationHistory().getTblItem().getTblUnit().getIdunit()));
            //set data location
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(roomStatusController.getService().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()));
            data.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(roomStatusController.getService().getDataLocation(data.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
            //set data mutation type
            data.getTblItemMutationHistory().setRefItemMutationType(roomStatusController.getService().getDataMutationType(data.getTblItemMutationHistory().getRefItemMutationType().getIdtype()));
            //set data obsolete by
            if (data.getTblItemMutationHistory().getRefItemMutationType().getIdtype() == 2) {   //Rusak = '2'
                data.getTblItemMutationHistory().setRefItemObsoleteBy(roomStatusController.getService().getDataObsoleteBy(data.getTblItemMutationHistory().getRefItemObsoleteBy().getIdobsoleteBy()));
            }
        }
        //data - out
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()
                    == roomStatusController.selectedData.getTblRoom().getTblLocation().getIdlocation()) {
                list.remove(i);
            }
        }
        //data - item location mutation history property barcode
        for (TblRoomCheckItemMutationHistory data : list) {
            if (data.getTblItemMutationHistory().getTblItem().getPropertyStatus()) {   //Property
                List<TblItemMutationHistoryPropertyBarcode> dataIMHPBs = roomStatusController.getService().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for (TblItemMutationHistoryPropertyBarcode dataIMHPB : dataIMHPBs) {
                    dataIMHPB.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHPB.setTblPropertyBarcode(roomStatusController.getService().getDataPropertyBarcode(dataIMHPB.getTblPropertyBarcode().getIdbarcode()));
                    roomStatusController.itemMutationHistoryPropertyBarcodes.add(dataIMHPB);
                }
            }
        }
        //data - item location mutation history item expired date
        for (TblRoomCheckItemMutationHistory data : list) {
            if (data.getTblItemMutationHistory().getTblItem().getConsumable()) {
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = roomStatusController.getService().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(data.getTblItemMutationHistory().getIdmutation());
                for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                    dataIMHIED.setTblItemMutationHistory(data.getTblItemMutationHistory());
                    dataIMHIED.setTblItemExpiredDate(roomStatusController.getService().getDataItemExpiredDate(dataIMHIED.getTblItemExpiredDate().getIditemExpiredDate()));
                    roomStatusController.itemMutationHistoryItemExpiredDates.add(dataIMHIED);
                }
            }
        }
        return list;
    }

    public TblRoomCheckItemMutationHistory selectedDataOut;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputOutStatus = 0;

    public Stage dialogStageOut;

    public void dataOutCreateHandle() {
        dataInputOutStatus = 0;
        selectedDataOut = new TblRoomCheckItemMutationHistory();
        selectedDataOut.setTblRoomCheck(roomStatusController.selectedData);
        selectedDataOut.setTblItemMutationHistory(new TblItemMutationHistory());
        selectedDataOut.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(roomStatusController.selectedData.getTblRoom().getTblLocation());
        selectedDataOut.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
        //open form data - out
        showDataOutDialog();
    }

    public void dataOutUpdateHandle() {
        if (tableDataOut.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputOutStatus = 1;
            selectedDataOut = (TblRoomCheckItemMutationHistory) tableDataOut.getTableView().getSelectionModel().getSelectedItem();
            //open form data - out
            showDataOutDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataOutDeleteHandle() {
        if (tableDataOut.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblRoomCheckItemMutationHistory) tableDataOut.getTableView().getSelectionModel().getSelectedItem()).getCreateDate() == null) {
                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassMessage.showSucceedRemovingDataMessage("", null);
                    //remove data item mutation history - proeprty barcode
                    for (int i = roomStatusController.itemMutationHistoryPropertyBarcodes.size() - 1; i > -1; i--) {
                        if (roomStatusController.itemMutationHistoryPropertyBarcodes.get(i).getTblItemMutationHistory().equals(tableDataOut.getTableView().getSelectionModel().getSelectedItem())) {
                            roomStatusController.itemMutationHistoryPropertyBarcodes.remove(i);
                            break;
                        }
                    }
                    //remove data item mutation history - item expired date
                    for (int i = roomStatusController.itemMutationHistoryItemExpiredDates.size() - 1; i > -1; i--) {
                        if (roomStatusController.itemMutationHistoryItemExpiredDates.get(i).getTblItemMutationHistory().equals(((TblRoomCheckItemMutationHistory) tableDataOut.getTableView().getSelectionModel().getSelectedItem()).getTblItemMutationHistory())) {
                            roomStatusController.itemMutationHistoryItemExpiredDates.remove(i);
                        }
                    }
                    //remove data from table items list
                    tableDataOut.getTableView().getItems().remove(tableDataOut.getTableView().getSelectionModel().getSelectedItem());
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Data tidak dapat dihapus..!", "");
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataOutDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/DataOutDialog.fxml"));

            DataOutController controller = new DataOutController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageOut = new Stage();
            dialogStageOut.initModality(Modality.WINDOW_MODAL);
            dialogStageOut.initOwner(roomStatusController.dialogStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageOut, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageOut.initStyle(StageStyle.TRANSPARENT);
            dialogStageOut.setScene(scene);
            dialogStageOut.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageOut.showAndWait();
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
        initFormDataRoomCheck();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RoomCheckInputController(RoomStatusController parentController) {
        roomStatusController = parentController;
    }

    private final RoomStatusController roomStatusController;

    public RoomStatusController getParentController() {
        return roomStatusController;
    }

}
