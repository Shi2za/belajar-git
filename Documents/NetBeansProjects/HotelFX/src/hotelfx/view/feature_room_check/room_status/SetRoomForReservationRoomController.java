/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class SetRoomForReservationRoomController implements Initializable {

    @FXML
    private AnchorPane ancFormDataSetRoomForReservationRoom;

    @FXML
    private GridPane gpFormDataSetRoomForReservationRoom;

    @FXML
    private TitledPane tlpReservationRoom;
    
//    @FXML
//    private Label lblTitle;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataSetRoomForReservationRoom() {
        btnSave.setTooltip(new Tooltip("Simpan (Data Cek-Kamar)"));
        btnSave.setOnAction((e) -> {
            dataSetRoomForReservationRoomSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataSetRoomForReservationRoomCancelHandle();
        });
    }

    private void setSelectedDataToInputForm() {
        Time defaultCheckInTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckInTime = roomStatusController.getService().getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
        if (sdhDefaultCheckInTime != null
                && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
            String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
            defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                    Integer.parseInt(dataCheckInTime[1]),
                    Integer.parseInt(dataCheckInTime[2]));
        }

        tlpReservationRoom.setText("Set Kamar  :  Tanggal "
                + ClassFormatter.dateFormate.format(Date.valueOf(LocalDate.now()))
                + " "
                + ClassFormatter.timeFormate.format(defaultCheckInTime));

        rrtds = loadAllDataRRTDReadyToCheckIn();

        //set table 'set room for reservation room' input
        initTableSetRoomForReservationRoom();
    }

    @FXML
    private AnchorPane ancTableSetRoomForReservationRoom;

    public TableView tableSetRoomForReservationRoom;

    private List<TblReservationRoomTypeDetail> rrtds = new ArrayList<>();

    private void initTableSetRoomForReservationRoom() {
        ancTableSetRoomForReservationRoom.getChildren().clear();

        tableSetRoomForReservationRoom = new TableView();
        tableSetRoomForReservationRoom.setEditable(true);

        TableColumn<TblReservationRoomTypeDetail, String> codeReservation = new TableColumn("No. Reservasi");
        codeReservation.setMinWidth(120);
        codeReservation.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblReservation() == null
                                ? ""
                                : param.getValue().getTblReservation().getCodeReservation(),
                        param.getValue().tblReservationProperty()));

        TableColumn<TblReservationRoomTypeDetail, String> customerName = new TableColumn("Customer");
        customerName.setMinWidth(140);
        customerName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblReservation() == null
                        || param.getValue().getTblReservation().getTblCustomer() == null
                        || param.getValue().getTblReservation().getTblCustomer().getTblPeople() == null
                                ? ""
                                : param.getValue().getTblReservation().getTblCustomer().getTblPeople().getFullName(),
                        param.getValue().tblReservationProperty()));

        TableColumn<TblReservationRoomTypeDetail, String> room = new TableColumn("Lokasi");
        room.setMinWidth(140);
        room.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblReservationCheckInOut() == null
                        || param.getValue().getTblReservationCheckInOut().getTblRoom() == null
                                ? ""
                                : param.getValue().getTblReservationCheckInOut().getTblRoom().getRoomName(),
                        param.getValue().getTblReservationCheckInOut().tblRoomProperty()));
        Callback<TableColumn<TblReservationRoomTypeDetail, String>, TableCell<TblReservationRoomTypeDetail, String>> cellFactory
                = new Callback<TableColumn<TblReservationRoomTypeDetail, String>, TableCell<TblReservationRoomTypeDetail, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellRoom();
                    }
                };
        room.setCellFactory(cellFactory);
        room.setEditable(true);

        tableSetRoomForReservationRoom.getColumns().addAll(codeReservation, customerName, room);

        tableSetRoomForReservationRoom.setItems(FXCollections.observableArrayList(rrtds));
        AnchorPane.setBottomAnchor(tableSetRoomForReservationRoom, 0.0);
        AnchorPane.setLeftAnchor(tableSetRoomForReservationRoom, 0.0);
        AnchorPane.setRightAnchor(tableSetRoomForReservationRoom, 0.0);
        AnchorPane.setTopAnchor(tableSetRoomForReservationRoom, 0.0);
        ancTableSetRoomForReservationRoom.getChildren().add(tableSetRoomForReservationRoom);
    }

    private List<TblReservationRoomTypeDetail> loadAllDataRRTDReadyToCheckIn() {
        List<TblReservationRoomTypeDetail> list = new ArrayList<>();
        List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = roomStatusController.getService().getAllDataReservationRoomPriceDetailByDetailDate(Date.valueOf(LocalDate.now()));
        for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
            TblReservationRoomTypeDetailRoomPriceDetail dataRelation = roomStatusController.getService().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
            if (dataRelation != null
                    && dataRelation.getTblReservationRoomTypeDetail() != null) {
                TblReservationRoomTypeDetail rrtd = roomStatusController.getService().getDataReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                if (rrtd != null) {
                    //data check-in-out
                    if (rrtd.getTblReservationCheckInOut() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(roomStatusController.getService().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        if (rrtd.getTblReservationCheckInOut().getTblRoom() != null) {
                            rrtd.getTblReservationCheckInOut().setTblRoom(roomStatusController.getService().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        }
                        //data checkin-out status
                        if (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                            rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusController.getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        }
                    }
                    //data reservation
                    if (rrtd.getTblReservation() != null) {
                        //data customer
                        rrtd.getTblReservation().setTblCustomer(roomStatusController.getService().getDataCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                        //data people
                        rrtd.getTblReservation().getTblCustomer().setTblPeople(roomStatusController.getService().getDataPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
                        //data reservation status
                        rrtd.getTblReservation().setRefReservationStatus(roomStatusController.getService().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                    }
                    //availabel to add?
                    if (rrtd.getTblReservationCheckInOut() != null
                            && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) //Ready to Check In = '0'
                            && (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                            || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {  //Booked = '2'
                        list.add(rrtd);
                    }
                }
            }
        }
        return list;
    }

    class EditingCellRoom extends TableCell<TblReservationRoomTypeDetail, String> {

        private JFXCComboBoxTablePopup<TblRoom> cbpRoom;

        public EditingCellRoom() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                cbpRoom = getComboBoxRoom(((TblReservationRoomTypeDetail) this.getTableRow().getItem()));

//                ClassViewSetting.setImportantField(
//                        cbpRoom);
                cbpRoom.hide();

                cbpRoom.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                cbpRoom.valueProperty().bindBidirectional(((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().tblRoomProperty());

                setText(null);
                setGraphic(cbpRoom);
                cbpRoom.getEditor().selectAll();

                //cell input color
                if (this.getTableRow() != null) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even");
                        getStyleClass().add("cell-input-even-edit");
                    } else {
                        getStyleClass().remove("cell-input-odd");
                        getStyleClass().add("cell-input-odd-edit");
                    }
                }

            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            cbpRoom.valueProperty().unbindBidirectional(((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().tblRoomProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit");
                    getStyleClass().add("cell-input-even");
                } else {
                    getStyleClass().remove("cell-input-odd-edit");
                    getStyleClass().add("cell-input-odd");
                }
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            if (((TblReservationRoomTypeDetail) data).getTblReservationCheckInOut().getTblRoom() != null) {
                                setText(((TblReservationRoomTypeDetail) data).getTblReservationCheckInOut().getTblRoom().getRoomName());
                            } else {
                                setText("");
                            }
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().add("cell-input-even");

                        } else {
                            getStyleClass().add("cell-input-odd");
                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
        }

    }

    private JFXCComboBoxTablePopup<TblRoom> getComboBoxRoom(TblReservationRoomTypeDetail dataRRTD) {
        TableView<TblRoom> tableRoom = new TableView<>();

        TableColumn<TblRoom, String> roomName = new TableColumn<>("Kamar");
        roomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        roomName.setMinWidth(140);

        tableRoom.getColumns().addAll(roomName);

        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom(dataRRTD));

        JFXCComboBoxTablePopup cbpR = new JFXCComboBoxTablePopup<>(
                TblRoom.class, tableRoom, roomItems, "", " ", true, 200, 200
        );
        
        cbpR.setLabelFloat(false);

        return cbpR;
    }

    private List<TblRoom> loadAllDataRoom(TblReservationRoomTypeDetail dataRRTD) {
        List<TblRoom> list = new ArrayList<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        if (rrtds != null) {
            //filter by 'room type'
            list = roomStatusController.getService().getAllDataRoomByIDRoomType(dataRRTD.getTblRoomType().getIdroomType());
            //filter by 'check in - room'
            for (int i = list.size() - 1; i > -1; i--) {
                TblReservationRoomTypeDetail dataRRTDWithStatusCheckIn = getRRTDWithStatusCheckIn(list.get(i), currentDate);
                if (dataRRTDWithStatusCheckIn != null) {
                    list.remove(i);
                }
            }
            //filter by 'ready  to check in - room' (current data)
            for (int i = list.size() - 1; i > -1; i--) {
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    if (rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null) {
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == list.get(i).getIdroom()) {
                            if (rrtd.getIddetail() != dataRRTD.getIddetail()) {
                                list.remove(i);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private TblReservationRoomTypeDetail getRRTDWithStatusCheckIn(
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
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusController.getService().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1)) {  //Checked In = '1'
                            //data reservation
                            rrtd.setTblReservation(roomStatusController.getService().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            //data reservation - status
                            rrtd.getTblReservation().setRefReservationStatus(roomStatusController.getService().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
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

    private void dataSetRoomForReservationRoomSaveHandle() {
        if (checkDataInputDataSetRoomForReservationRoom()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            //dummy entry
            List<TblReservationRoomTypeDetail> dummyRRTDs = new ArrayList<>();
            for(TblReservationRoomTypeDetail rrtd : rrtds){
                TblReservationRoomTypeDetail dummyRRTD = new TblReservationRoomTypeDetail(rrtd);
                if(dummyRRTD.getTblReservationCheckInOut() != null){
                    dummyRRTD.setTblReservationCheckInOut(new TblReservationCheckInOut(rrtd.getTblReservationCheckInOut()));
                    if(dummyRRTD.getTblReservationCheckInOut().getTblRoom() != null){
                        dummyRRTD.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummyRRTD.getTblReservationCheckInOut().getTblRoom()));
                    }
                    if(dummyRRTD.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null){
                        dummyRRTD.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummyRRTD.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                    }
                }
                dummyRRTDs.add(dummyRRTD);
            }
            if (alert.getResult() == ButtonType.OK) {
                if (roomStatusController.getService().updateDataRRTDReservationCheckInOutRoom(dummyRRTDs)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh data from table & close form data 'set room for reservation room'
                    roomStatusController.setSelectedDataToInputForm();
                    roomStatusController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(roomStatusController.getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private String errDataInput;

    private boolean checkDataInputDataSetRoomForReservationRoom() {
        boolean dataInput = true;
        errDataInput = "";

        return dataInput;
    }

    private void dataSetRoomForReservationRoomCancelHandle() {
        //refresh data from table & close form data 'set room for reservation room'
        roomStatusController.setSelectedDataToInputForm();
        roomStatusController.dialogStage.close();
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
        initFormDataSetRoomForReservationRoom();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public SetRoomForReservationRoomController(RoomStatusController parentController) {
        roomStatusController = parentController;
    }

    private final RoomStatusController roomStatusController;

}
