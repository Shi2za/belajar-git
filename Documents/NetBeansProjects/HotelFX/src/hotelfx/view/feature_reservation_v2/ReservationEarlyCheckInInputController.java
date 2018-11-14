/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailChildDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
 * @author ANDRI
 */
public class ReservationEarlyCheckInInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCheckIn;

    @FXML
    private GridPane gpFormDataCheckIn;

    @FXML
    private GridPane gpFormDataEarlyCheckIn;
    
    @FXML
    private JFXTextField txtCheckInDateTimeDefault;

    @FXML
    private JFXTextField txtCheckInDateTimeNow;

    @FXML
    private JFXTextField txtTimeDistance;

    @FXML
    private JFXTextField txtAdditionalCharge;

    @FXML
    private JFXCheckBox chbGetFullRoomTypeDetailCost;

    @FXML
    private JFXTextArea txtAdditionalNote;

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

    private void initFormDataEarlyCheckIn() {
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
        reservationController.selectedDataCheckIn.childNumberProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                setChildInfo();
            }
        });

        //total deposit (needed)
        txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(reservationController.selectedDataCheckIn.getCardUsed()))
                .multiply(reservationController.selectedDataCheckIn.getBrokenCardCharge())));
        reservationController.selectedDataCheckIn.cardUsedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.intValue() > 0) {
                txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(newValue.intValue()))
                        .multiply(reservationController.selectedDataCheckIn.getBrokenCardCharge())));
            } else {
                txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat(0));
            }
        });
        reservationController.selectedDataCheckIn.brokenCardChargeProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null
                    && newValue.compareTo(new BigDecimal("0")) == 1) {
                txtTotalDeposit.setText(ClassFormatter.currencyFormat.cFormat((new BigDecimal(reservationController.selectedDataCheckIn.getCardUsed()))
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
                txtCardBrokenCharge,
                txtAdditionalCharge);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAdditionalCharge,
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
        List<TblRoom> list = reservationController.getFReservationManager().getAllDataRoomByIDRoomType(reservationController.selectedDataRoomTypeDetail.getTblRoomType().getIdroomType());
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            list.get(i).setRefRoomStatus(reservationController.getFReservationManager().getRoomStatus(list.get(i).getRefRoomStatus().getIdstatus()));
            if (list.get(i).getRefRoomStatus().getIdstatus() != 5) {    //Vacant Ready = '5'
                list.remove(i);
            }else{  //has been leased
                if(getNextRRTD(list.get(i), Date.valueOf(LocalDate.now())) != null){
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
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = reservationController.getFReservationManager().getAllDataReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = reservationController.getFReservationManager().getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = reservationController.getFReservationManager().getReservationRoomTypeDetail(dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                        //data checkin-out
                        rrtd.setTblReservationCheckInOut(reservationController.getFReservationManager().getReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
                        //data room
                        rrtd.getTblReservationCheckInOut().setTblRoom(reservationController.getFReservationManager().getRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                        //data checkin/out - status
                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                            if ( rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                    && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                    || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
                                //data reservation
                                rrtd.setTblReservation(reservationController.getFReservationManager().getReservation(rrtd.getTblReservation().getIdreservation()));
                                //data customer
                                rrtd.getTblReservation().setTblCustomer(reservationController.getFReservationManager().getCustomer(rrtd.getTblReservation().getTblCustomer().getIdcustomer()));
                                //data people
                                rrtd.getTblReservation().getTblCustomer().setTblPeople(reservationController.getFReservationManager().getPeople(rrtd.getTblReservation().getTblCustomer().getTblPeople().getIdpeople()));
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
        reservationController.selectedChildDetails = new ArrayList<>();
        if (reservationController.selectedDataCheckIn.getChildNumber() > 0) {
            for (int i = 0; i < reservationController.selectedDataCheckIn.getChildNumber(); i++) {
                TblReservationRoomTypeDetailChildDetail data = new TblReservationRoomTypeDetailChildDetail();
                data.setTblReservationRoomTypeDetail(reservationController.selectedDataRoomTypeDetail);
                reservationController.selectedChildDetails.add(data);
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
                (reservationController.selectedDataCheckIn.getChildNumber() > 0
                        ? (reservationController.selectedDataCheckIn.getChildNumber() * 50) + 20
                        : 0));
        //attached to grid-pane
        gpFormDataCheckIn.add(cbpChildInfo, 2, 1);
    }

    private void setChildName() {
        gpFormDataChildName = new GridPane();
        gpFormDataChildName.setVgap(20.0);
        for (int i = 0; i < reservationController.selectedChildDetails.size(); i++) {
            gpFormDataChildName.getRowConstraints().add(new RowConstraints());
            JFXTextField txtName = new JFXTextField();
            txtName.setPromptText("Nama Anak");
            txtName.setLabelFloat(true);
            txtName.setPrefSize(140, 30);
            ClassViewSetting.setImportantField(
                    txtName);
            txtName.textProperty().bindBidirectional(reservationController.selectedChildDetails.get(i).childNameProperty());
            gpFormDataChildName.add(txtName, 0, i);
        }
        gpFormDataChildInfo.getColumnConstraints().add(new ColumnConstraints());
        gpFormDataChildInfo.add(gpFormDataChildName, 0, 0);
    }

    private void setChildAge() {
        gpFormDataChildAge = new GridPane();
        gpFormDataChildAge.setVgap(20.0);
        for (int i = 0; i < reservationController.selectedChildDetails.size(); i++) {
            gpFormDataChildAge.getRowConstraints().add(new RowConstraints());
            JFXTextField txtAge = new JFXTextField();
            txtAge.setPromptText("Umur Anak");
            txtAge.setLabelFloat(true);
            ClassViewSetting.setImportantField(
                    txtAge);
            txtAge.setPrefSize(140, 30);
            ClassFormatter.setToNumericField("Integer", txtAge);
            Bindings.bindBidirectional(txtAge.textProperty(), reservationController.selectedChildDetails.get(i).childAgeProperty(), new NumberStringConverter());
            gpFormDataChildAge.add(txtAge, 0, i);
        }
        gpFormDataChildInfo.getColumnConstraints().add(new ColumnConstraints());
        gpFormDataChildInfo.add(gpFormDataChildAge, 1, 0);
    }

    private void setYear() {
        gpFormDataYear = new GridPane();
        gpFormDataYear.setVgap(20.0);
        for (int i = 0; i < reservationController.selectedChildDetails.size(); i++) {
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

        cbpChildInfo.setItems(FXCollections.observableArrayList(reservationController.selectedChildDetails));

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

        //data early check in
        txtCheckInDateTimeDefault.setText(ClassFormatter.dateFormate.format(reservationController.selectedDataRoomTypeDetail.getCheckInDateTime())
                + " " + (ClassFormatter.timeFormate.format(reservationController.defaultCheckInTime)));
        txtCheckInDateTimeNow.setText(ClassFormatter.dateTimeFormate.format(Timestamp.valueOf(LocalDateTime.now())));
        txtTimeDistance.setText(ClassFormatter.decimalFormat.cFormat(calculationTimeDistance()) + " Jam");
        Bindings.bindBidirectional(txtAdditionalCharge.textProperty(), reservationController.selectedAdditionalService.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtAdditionalNote.textProperty().bindBidirectional(reservationController.selectedAdditionalService.noteProperty());

        chbGetFullRoomTypeDetailCost.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                reservationController.selectedAdditionalService.setPrice(getFullRoomTypeDetailCost());
            }
        });
        chbGetFullRoomTypeDetailCost.setSelected(false);

        reservationController.selectedAdditionalService.priceProperty().addListener((obs, oldVal, newVal) -> {
            if (chbGetFullRoomTypeDetailCost.isSelected()
                    && (newVal.compareTo(getFullRoomTypeDetailCost()) != 0)) {
                chbGetFullRoomTypeDetailCost.setSelected(false);
            }
        });

        //data checkin
        Bindings.bindBidirectional(txtAdultNumber.textProperty(), reservationController.selectedDataCheckIn.adultNumberProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtChildNumber.textProperty(), reservationController.selectedDataCheckIn.childNumberProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCardUsedNumber.textProperty(), reservationController.selectedDataCheckIn.cardUsedProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCardBrokenCharge.textProperty(), reservationController.selectedDataCheckIn.brokenCardChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());

        if(reservationController.selectedDataCheckIn.getTblRoom() != null){
            cbpRoom.setDisable(true);
            lblRoomStatusName.setText(reservationController.selectedDataCheckIn.getTblRoom().getRefRoomStatus().getStatusName());
        }else{
            cbpRoom.setDisable(false);
            lblRoomStatusName.setText("");
        }
        cbpRoom.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                lblRoomStatusName.setText(newVal.getRefRoomStatus().getStatusName());
            }else{
                lblRoomStatusName.setText("");
            }
        });
        cbpRoom.valueProperty().bindBidirectional(reservationController.selectedDataCheckIn.tblRoomProperty());

        cbpRoom.hide();
    }

    private double calculationTimeDistance() {
        double result = ChronoUnit.HOURS.between(LocalDateTime.now(), LocalDateTime.of(
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                reservationController.defaultCheckInTime.getHours(), reservationController.defaultCheckInTime.getMinutes()));
        return result < 0 ? result * (-1) : result;
    }

    //room type - date (before = 'checkin' - 1(days))
    private BigDecimal getFullRoomTypeDetailCost() {
        TblReservationBar dataBAR;
        TblReservationCalendarBar calendarBAR = reservationController.getFReservationManager().getReservationCalendarBARByCalendarDate(Date.valueOf((LocalDate.of(
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getDate()))
                .minusDays(1)));
        if (calendarBAR == null) {
            TblReservationDefaultBar defaultBAR = reservationController.getFReservationManager().getReservationDefaultBARByDayOfWeek(((LocalDate.of(
                    reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                    reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                    reservationController.selectedDataRoomTypeDetail.getCheckInDateTime().getDate()))
                    .minusDays(1))
                    .getDayOfWeek().getValue());
            dataBAR = reservationController.getFReservationManager().getReservationBAR(defaultBAR.getTblReservationBar().getIdbar());
        } else {
            dataBAR = reservationController.getFReservationManager().getReservationBAR(calendarBAR.getTblReservationBar().getIdbar());
        }
        return reservationController.selectedDataRoomTypeDetail.getTblRoomType().getRoomTypePrice().multiply(dataBAR.getBarpercentage());
    }

    private void dataCheckInSaveHandle() {
        if (checkDataInputDataCheckIn()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //data additional service (early checkin)
                reservationController.selectedAdditionalService.setAdditionalDate(Date.valueOf(LocalDate.now()));
                reservationController.selectedAdditionalService.setRefReservationBillType(reservationController.getFReservationManager().getDataReservationBillType(0));  //Reservation = '0'
                //data checkin
                reservationController.selectedDataCheckIn.setCheckInDateTime(Timestamp.valueOf(LocalDateTime.now()));
                reservationController.selectedDataCheckIn.setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(1));
                reservationController.selectedDataCheckIn.setCardMissed(0);
                reservationController.selectedDataRoomTypeDetail.setTblReservationCheckInOut(reservationController.selectedDataCheckIn);
                //update data bill
                reservationController.refreshDataBill();
                //save data to database
                if (reservationController.dataReservationSaveHandle(11)) {
                    //close form data checkin input
                    reservationController.dialogStage.close();
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCheckInCancelHandle() {
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data checkin
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCheckIn() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtAdditionalCharge.getText() == null 
                || txtAdditionalCharge.getText().equals("")
                || txtAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Tambahan Biaya : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedAdditionalService.getPrice().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Tambahan Biaya : Tidak dapat kurang dari '0' \n";
            }
        }
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
            if (reservationController.selectedDataCheckIn.getAdultNumber() < 1) {
                dataInput = false;
                errDataInput += "Jumlah Dewasa : Tidak boleh kurang dari '1' \n";
            }
        }
        if (txtChildNumber.getText() == null 
                || txtChildNumber.getText().equals("")
                || txtChildNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Anak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedDataCheckIn.getChildNumber() < 0) {
                dataInput = false;
                errDataInput += "Jumlah Anak : Tidak boleh kurang dari '0' \n";
            }
        }
        if (txtCardBrokenCharge.getText() == null 
                || txtCardBrokenCharge.getText().equals("")
                || txtCardBrokenCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationController.selectedDataCheckIn.getBrokenCardCharge().compareTo(new BigDecimal("0")) < 1) {
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
            if (reservationController.selectedDataCheckIn.getCardUsed() < 1) {
                dataInput = false;
                errDataInput += "Jumlah Kartu yang Digunakan : Tidak boleh kurang dari '1' \n";
            }
        }
        return dataInput;
    }

    private boolean checkSelectedRoom() {
        boolean available = true;
        //room must vacant ready or vacant clean
        if (reservationController.selectedDataCheckIn.getTblRoom().getRefRoomStatus().getIdstatus() != 5) { //Vacant Ready = '5'
            available = false;
        }
//        //room must clean-ready
//        if (reservationController.selectedDataCheckIn.getTblRoom().getRefRoomCleanStatus().getIdstatus() != 2) {    //Clean-Ready = '2'
//            available = false;
//        }
        return available;
    }

    private boolean checkChildInfo() {
        boolean completed = true;
        for (TblReservationRoomTypeDetailChildDetail data : reservationController.selectedChildDetails) {
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
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataEarlyCheckIn();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public ReservationEarlyCheckInInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;
    
}
