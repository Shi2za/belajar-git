/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailChildDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class ReservationCheckInInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCheckIn;

    @FXML
    private GridPane gpFormDataCheckIn;

    @FXML
    private JFXTextField txtAdultNumber;

    @FXML
    private JFXTextField txtChildNumber;

    @FXML
    private JFXTextField txtCardUsedNumber;

    @FXML
    private JFXTextField txtCardBrokenCharge;

    @FXML
    private JFXTextField txtTotalDeposit;

    private JFXCComboBoxTablePopup<TblRoom> cbpRoom;

    @FXML
    private Label lblRoomStatusName;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCheckIn() {
        initDataCheckInPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Check In)"));
        btnSave.setOnAction((e) -> {
            dataCheckInSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCheckInCancelHandle();
        });

        initChildInfo();
        roomStatusFDController.selectedDataCheckIn.childNumberProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                setChildInfo();
            }
        });

        //total deposit (needed)
        txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(roomStatusFDController.selectedDataCheckIn.getCardUsed()))
                .multiply(roomStatusFDController.selectedDataCheckIn.getBrokenCardCharge())));
        roomStatusFDController.selectedDataCheckIn.cardUsedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(roomStatusFDController.selectedDataCheckIn.getBrokenCardCharge())));
            } else {
                txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat(0));
            }
        });
        roomStatusFDController.selectedDataCheckIn.brokenCardChargeProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null
                    && (newValue.compareTo(new BigDecimal("0")) == 1)) {
                txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(roomStatusFDController.selectedDataCheckIn.getCardUsed()))
                        .multiply(newValue)));
            } else {
                txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat(0));
            }
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtAdultNumber,
                txtChildNumber,
                txtCardUsedNumber,
                txtCardBrokenCharge);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtCardBrokenCharge);
        ClassFormatter.setToNumericField(
                "Integer",
                txtAdultNumber,
                txtChildNumber,
                txtCardUsedNumber);
    }

    private void initDataCheckInPopup() {
        //Room
        TableView<TblRoom> tableRoom = new TableView<>();

        TableColumn<TblRoom, String> roomName = new TableColumn<>("Kamar");
        roomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        roomName.setMinWidth(140);

        TableColumn<TblRoom, String> smokingStatus = new TableColumn("Smoking");
        smokingStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getSmokingStatus() == 0 ? "non smoking" : "smoking", param.getValue().smokingStatusProperty()));
        smokingStatus.setMinWidth(140);

        TableColumn<TblRoom, String> roomStatus = new TableColumn("Status Kamar");
        roomStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefRoomStatus().getStatusName(), param.getValue().refRoomStatusProperty()));
        roomStatus.setMinWidth(140);

        tableRoom.getColumns().addAll(roomName, smokingStatus, roomStatus);

        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());

        cbpRoom = new JFXCComboBoxTablePopup<>(
                TblRoom.class, tableRoom, roomItems, "", "Kamar *", true, 440, 300
        );

        //attached to grid-pane
        gpFormDataCheckIn.add(cbpRoom, 0, 0);
    }

    private void refreshDataPopup() {
        //Room
        ObservableList<TblRoom> roomItems = FXCollections.observableArrayList(loadAllDataRoom());
        cbpRoom.setItems(roomItems);
    }

    private List<TblRoom> loadAllDataRoom() {
        List<TblRoom> list = roomStatusFDController.getServiceRV2().getAllDataRoomByIDRoomType(roomStatusFDController.selectedDataRoomTypeDetail.getTblRoomType().getIdroomType());
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            list.get(i).setRefRoomStatus(roomStatusFDController.getServiceRV2().getRoomStatus(list.get(i).getRefRoomStatus().getIdstatus()));
            if (list.get(i).getRefRoomStatus().getIdstatus() != 5) {    //Vacant Ready = '5'
                list.remove(i);
            } else {  //has been leased
                if (getNextRRTD(list.get(i), Date.valueOf(LocalDate.now())) != null) {
                    list.remove(i);
                }
            }
        }
//        //remove data has been used
//        for (int i = list.size() - 1; i > -1; i--) {
//            //data has been used
//            for (TblReservationRoomTypeDetail data : reservationController.dataReservationRoomTypeDetails) {
//                if (data.getTblReservationCheckInOut() != null
//                        && data.getTblReservationCheckInOut().getTblRoom().getIdroom() == list.get(i).getIdroom()) {
//                    list.remove(i);
//                    break;
//                }
//            }
//        }
        return list;
    }

    private TblReservationRoomTypeDetail getNextRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = roomStatusFDController.getServiceRV2().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = roomStatusFDController.getServiceRV2().getReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(roomStatusFDController.getServiceRV2().getReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(roomStatusFDController.getServiceRV2().getRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(roomStatusFDController.getServiceRV2().getReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
                            //data reservation
                            rrtd.setTblReservation(roomStatusFDController.getServiceRV2().getReservation(rrtd.getTblReservation().getIdreservation()));
                            //data customer
                            rrtd.getTblReservation().setTblCustomer(roomStatusFDController.getServiceRV2().getCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                            //data people
                            rrtd.getTblReservation().getTblCustomer().setTblPeople(roomStatusFDController.getServiceRV2().getPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
                            //retuurn rrtd
                            return rrtd;
                        }
                    }
                }
            }
        }
        return null;
    }

    private JFXCComboBoxPopup<TblReservationRoomTypeDetailChildDetail> cbpChildInfo = new JFXCComboBoxPopup(TblReservationRoomTypeDetailChildDetail.class);

    private GridPane gpFormDataChildInfo;

    private GridPane gpFormDataChildName;

    private GridPane gpFormDataChildAge;

    private GridPane gpFormDataYear;

    private void initChildInfo() {
        //attached to grid-pane
        gpFormDataCheckIn.add(cbpChildInfo, 2, 1);
        //set child info
        setChildInfo();
    }

    private void setChildInfo() {
        //remove to grid-pane
        gpFormDataCheckIn.getChildren().remove(cbpChildInfo);
        cbpChildInfo = new JFXCComboBoxPopup(TblReservationRoomTypeDetailChildDetail.class);
        //data
        roomStatusFDController.selectedChildDetails = new ArrayList<>();
        if (roomStatusFDController.selectedDataCheckIn.getChildNumber() > 0) {
            for (int i = 0; i < roomStatusFDController.selectedDataCheckIn.getChildNumber(); i++) {
                TblReservationRoomTypeDetailChildDetail data = new TblReservationRoomTypeDetailChildDetail();
                data.setTblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
                roomStatusFDController.selectedChildDetails.add(data);
            }
        }
        //content
        gpFormDataChildInfo = new GridPane();
        gpFormDataChildInfo.setHgap(10);
        gpFormDataChildInfo.getRowConstraints().add(new RowConstraints());
        //layout
        AnchorPane ancContent = new AnchorPane();
        AnchorPane.setBottomAnchor(gpFormDataChildInfo, 20.0);
        AnchorPane.setLeftAnchor(gpFormDataChildInfo, 10.0);
        AnchorPane.setRightAnchor(gpFormDataChildInfo, 10.0);
        AnchorPane.setTopAnchor(gpFormDataChildInfo, 20.0);
        ancContent.getChildren().add(gpFormDataChildInfo);
        //Name
        setChildName();
        //Age
        setChildAge();
        //Year (label)
        setYear();
        //add content to popup content
        setChildInfoPopup(ancContent, 370,
                (roomStatusFDController.selectedDataCheckIn.getChildNumber() > 0
                        ? (roomStatusFDController.selectedDataCheckIn.getChildNumber() * 50) + 20
                        : 0));
        //attached to grid-pane
        gpFormDataCheckIn.add(cbpChildInfo, 2, 1);
    }

    private void setChildName() {
        gpFormDataChildName = new GridPane();
        gpFormDataChildName.setVgap(20.0);
        for (int i = 0; i < roomStatusFDController.selectedChildDetails.size(); i++) {
            gpFormDataChildName.getRowConstraints().add(new RowConstraints());
            JFXTextField txtName = new JFXTextField();
            txtName.setPromptText("Nama Anak");
            txtName.setLabelFloat(true);
            ClassViewSetting.setImportantField(
                    txtName);
            txtName.setPrefSize(140, 30);
            txtName.textProperty().bindBidirectional(roomStatusFDController.selectedChildDetails.get(i).childNameProperty());
            gpFormDataChildName.add(txtName, 0, i);
        }
        gpFormDataChildInfo.getColumnConstraints().add(new ColumnConstraints());
        gpFormDataChildInfo.add(gpFormDataChildName, 0, 0);
    }

    private void setChildAge() {
        gpFormDataChildAge = new GridPane();
        gpFormDataChildAge.setVgap(20.0);
        for (int i = 0; i < roomStatusFDController.selectedChildDetails.size(); i++) {
            gpFormDataChildAge.getRowConstraints().add(new RowConstraints());
            JFXTextField txtAge = new JFXTextField();
            txtAge.setPromptText("Umur Anak");
            txtAge.setLabelFloat(true);
            ClassViewSetting.setImportantField(
                    txtAge);
            txtAge.setPrefSize(140, 30);
            ClassFormatter.setToNumericField("Integer", txtAge);
            Bindings.bindBidirectional(txtAge.textProperty(), roomStatusFDController.selectedChildDetails.get(i).childAgeProperty(), new NumberStringConverter());
            gpFormDataChildAge.add(txtAge, 0, i);
        }
        gpFormDataChildInfo.getColumnConstraints().add(new ColumnConstraints());
        gpFormDataChildInfo.add(gpFormDataChildAge, 1, 0);
    }

    private void setYear() {
        gpFormDataYear = new GridPane();
        gpFormDataYear.setVgap(20.0);
        for (int i = 0; i < roomStatusFDController.selectedChildDetails.size(); i++) {
            gpFormDataYear.getRowConstraints().add(new RowConstraints());
            Label lblYear = new Label();
            lblYear.setText("Tahun");
            lblYear.setPrefSize(50, 30);
            gpFormDataYear.add(lblYear, 0, i);
        }
        gpFormDataChildInfo.getColumnConstraints().add(new ColumnConstraints());
        gpFormDataChildInfo.add(gpFormDataYear, 2, 0);
    }

    private void setChildInfoPopup(Node dataContent, double prefWidth, double prefHeight) {
        //popup button
        JFXButton button = new JFXButton(" ");
        button.setPrefSize(25, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-info");
        button.setTooltip(new Tooltip("Child-Info"));
        button.setOnMouseClicked((e) -> cbpChildInfo.show());

        cbpChildInfo.setItems(FXCollections.observableArrayList(roomStatusFDController.selectedChildDetails));

        //popup content
        BorderPane content = new BorderPane();
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth, prefHeight);

        content.setCenter(dataContent);

        cbpChildInfo.setPopupEditor(false);
        cbpChildInfo.promptTextProperty().set("");
        cbpChildInfo.setLabelFloat(false);
        cbpChildInfo.setPopupButton(button);
        cbpChildInfo.settArrowButton(true);
        cbpChildInfo.setPopupContent(content);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        Bindings.bindBidirectional(txtAdultNumber.textProperty(), roomStatusFDController.selectedDataCheckIn.adultNumberProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtChildNumber.textProperty(), roomStatusFDController.selectedDataCheckIn.childNumberProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCardUsedNumber.textProperty(), roomStatusFDController.selectedDataCheckIn.cardUsedProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCardBrokenCharge.textProperty(), roomStatusFDController.selectedDataCheckIn.brokenCardChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());

        if (roomStatusFDController.selectedDataCheckIn.getTblRoom() != null) {
            cbpRoom.setDisable(true);
            lblRoomStatusName.setText("Status : " + roomStatusFDController.selectedDataCheckIn.getTblRoom().getRefRoomStatus().getStatusName());
        } else {
            cbpRoom.setDisable(false);
            lblRoomStatusName.setText("");
        }
        cbpRoom.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblRoomStatusName.setText("Status : " + newVal.getRefRoomStatus().getStatusName());
            } else {
                lblRoomStatusName.setText("");
            }
        });
        cbpRoom.valueProperty().bindBidirectional(roomStatusFDController.selectedDataCheckIn.tblRoomProperty());

        cbpRoom.hide();
    }

    private void dataCheckInSaveHandle() {
        if (checkDataInputDataCheckIn()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusFDController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //set data checkin
                roomStatusFDController.selectedDataCheckIn.setCheckInDateTime(Timestamp.valueOf(LocalDateTime.now()));
                roomStatusFDController.selectedDataCheckIn.setRefReservationCheckInOutStatus(roomStatusFDController.getServiceRV2().getReservationCheckInOutStatus(1));
                roomStatusFDController.selectedDataCheckIn.setCardMissed(0);
                //set data rrtd
                roomStatusFDController.selectedDataRoomTypeDetail.setTblReservationCheckInOut(roomStatusFDController.selectedDataCheckIn);
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(roomStatusFDController.selectedDataRoomTypeDetail.getTblReservation());
                TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail = new TblReservationRoomTypeDetail(roomStatusFDController.selectedDataRoomTypeDetail);
                dummySelectedDataReservationRoomTypeDetail.setTblReservation(dummySelectedData);
                dummySelectedDataReservationRoomTypeDetail.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail.getTblRoomType()));
                dummySelectedDataReservationRoomTypeDetail.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut()));
                dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom()));
                dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                List<TblReservationRoomTypeDetailChildDetail> dummyReservationChildInfos = new ArrayList<>();
                for (TblReservationRoomTypeDetailChildDetail reservationChildInfo : roomStatusFDController.selectedChildDetails) {
                    TblReservationRoomTypeDetailChildDetail dummyReservationChildInfo = new TblReservationRoomTypeDetailChildDetail(reservationChildInfo);
                    dummyReservationChildInfo.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail);
                    dummyReservationChildInfos.add(dummyReservationChildInfo);
                }
                if (roomStatusFDController.getServiceRV2().updateDataReservationRoomTypeDetailCheckIn(
                        dummySelectedData,
                        dummySelectedDataReservationRoomTypeDetail,
                        dummyReservationChildInfos)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", roomStatusFDController.dialogStage);
                    // refresh all data and close form data - reservation additional service
                    roomStatusFDController.setSelectedDataToInputForm();
                    roomStatusFDController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(roomStatusFDController.getServiceRV2().getErrorMessage(), roomStatusFDController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusFDController.dialogStage);
        }
    }

    private void dataCheckInCancelHandle() {
        //refresh all data and close form data - reservation additional service
        roomStatusFDController.setSelectedDataToInputForm();
        roomStatusFDController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCheckIn() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpRoom.getValue() == null) {
            dataInput = false;
            errDataInput += "Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (!checkSelectedRoom()) {
                dataInput = false;
                errDataInput += "Kamar : Tidak tersedia \n";
            }
        }
        if (!checkChildInfo()) {
            dataInput = false;
            errDataInput += "Informasi Anak : Terdapat data yang tidak sesuai \n";
        }
        if (txtAdultNumber.getText() == null
                || txtAdultNumber.getText().equals("")
                || txtAdultNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Dewasa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomStatusFDController.selectedDataCheckIn.getAdultNumber() < 1) {
                dataInput = false;
                errDataInput += "Jumlah Dewasa : Nilai tidak dapat kurang dari '1' \n";
            }
        }
        if (txtChildNumber.getText() == null
                || txtChildNumber.getText().equals("")
                || txtChildNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Anak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomStatusFDController.selectedDataCheckIn.getChildNumber() < 0) {
                dataInput = false;
                errDataInput += "Jumlah Anak : Nilai tidak dapat kurag dari '0' \n";
            }
        }
        if (txtCardBrokenCharge.getText() == null
                || txtCardBrokenCharge.getText().equals("")
                || txtCardBrokenCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomStatusFDController.selectedDataCheckIn.getBrokenCardCharge().compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Biaya Kartu : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        if (txtCardUsedNumber.getText() == null
                || txtCardUsedNumber.getText().equals("")
                || txtCardUsedNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Kartu yang Digunakan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomStatusFDController.selectedDataCheckIn.getCardUsed() < 1) {
                dataInput = false;
                errDataInput += "Jumlah Kartu yang Digunakan : Nilai tidak dapat kurang dari '1' \n";
            }
        }
        return dataInput;
    }

    private boolean checkSelectedRoom() {
        boolean available = true;
        //room must vacant ready or vacant clean
        if (roomStatusFDController.selectedDataCheckIn.getTblRoom().getRefRoomStatus().getIdstatus() != 5) { //Vacant Ready = '5'
            available = false;
        }
//        //room must clean-ready
//        if (reservationController.selectedDataCheckIn.getTblRoom().getRefRoomCleanStatus().getIdstatus() != 2) {   //Clean-Ready = '2'
//            available = false;
//        }
        return available;
    }

    private boolean checkChildInfo() {
        boolean completed = true;
        for (TblReservationRoomTypeDetailChildDetail data : roomStatusFDController.selectedChildDetails) {
            if (data.getChildName() == null || data.getChildName().equals("")
                    || data.getChildAge() == null) {
                completed = false;
                break;
            } else {
                if (data.getChildAge() < 0) {
                    completed = false;
                    break;
                }
            }
        }
        return completed;
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
        initFormDataCheckIn();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationCheckInInputController(RoomStatusFDController parentController) {
        roomStatusFDController = parentController;
    }

    private final RoomStatusFDController roomStatusFDController;

}
