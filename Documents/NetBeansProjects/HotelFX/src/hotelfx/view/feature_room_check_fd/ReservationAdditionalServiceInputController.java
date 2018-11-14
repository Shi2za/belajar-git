/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblRoomService;
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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
public class ReservationAdditionalServiceInputController implements Initializable {

    @FXML
    private AnchorPane ancFormAdditionalService;

    @FXML
    private GridPane gpFormDataAdditionalService;

    @FXML
    private JFXTextField txtAdditionalCharge;

    @FXML
    private AnchorPane ancRoomServiceLayout;
    private JFXCComboBoxTablePopup<TblRoomService> cbpService;

    @FXML
    private JFXCheckBox chbUsedPeopleNumber;

    @FXML
    private Spinner<Integer> spPeopleNumber;

    private IntegerProperty peopleNumber = new SimpleIntegerProperty(0);

    @FXML
    private AnchorPane ancTabelDateLayout;

    private TableView<RTDListDate> tabelAdditionalDate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataAdditionalService() {
        initDataAdditionalServicePopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Tambahan Layanan)"));
        btnSave.setOnAction((e) -> {
            dataAdditionalServiceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataAdditionalServiceCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();

        initDataTableDateSelected(null);

        spPeopleNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spPeopleNumber.setEditable(false);
        spPeopleNumber.setVisible(false);

        chbUsedPeopleNumber.selectedProperty().addListener((obs, oldVal, newVal) -> {
            spPeopleNumber.setVisible(newVal);
            if (!newVal) {
                spPeopleNumber.getValueFactory().setValue(0);
            }
        });
        chbUsedPeopleNumber.setSelected(false);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpService,
                txtAdditionalCharge,
                spPeopleNumber);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAdditionalCharge);
    }

    private void initDataAdditionalServicePopup() {
        //Room Service
        TableView<TblRoomService> tableService = new TableView<>();

        TableColumn<TblRoomService, String> codeService = new TableColumn<>("ID");
        codeService.setCellValueFactory(cellData -> cellData.getValue().codeRoomServiceProperty());
        codeService.setMinWidth(120);

        TableColumn<TblRoomService, String> serviceName = new TableColumn<>("Layanan");
        serviceName.setCellValueFactory(cellData -> cellData.getValue().serviceNameProperty());
        serviceName.setMinWidth(140);

        tableService.getColumns().addAll(codeService, serviceName);

        ObservableList<TblRoomService> serviceItems = FXCollections.observableArrayList(loadAllDataService());

        cbpService = new JFXCComboBoxTablePopup<>(
                TblRoomService.class, tableService, serviceItems, "", "Layanan *", true, 280, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpService, 0.0);
        AnchorPane.setLeftAnchor(cbpService, 0.0);
        AnchorPane.setRightAnchor(cbpService, 0.0);
        AnchorPane.setTopAnchor(cbpService, 0.0);
        ancRoomServiceLayout.getChildren().clear();
        ancRoomServiceLayout.getChildren().add(cbpService);
    }

    private void refreshDataPopup() {
        //Room Service
        ObservableList<TblRoomService> serviceItems = FXCollections.observableArrayList(loadAllDataService());
        cbpService.setItems(serviceItems);
    }

    private List<TblRoomService> loadAllDataService() {
        List<TblRoomService> list = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataRoomService();
//        //remove data not used
//        for(int i=list.size()-1; i>-1; i--){
//            //room type - room service
//            
//            //additional room service
//            
//        }
        return list;
    }

    private void initDataTableDateSelected(TblReservationRoomTypeDetail dataRRTD) {
        ancTabelDateLayout.getChildren().clear();
        tabelAdditionalDate = new TableView();
        tabelAdditionalDate.setEditable(true);

        TableColumn<RTDListDate, String> dataDate = new TableColumn("Tanggal");
        dataDate.setCellValueFactory((TableColumn.CellDataFeatures<RTDListDate, String> param)
                -> Bindings.createStringBinding(()
                        -> reservationAdditionalItemAndServiceController.selectedAdditionalService.getTblRoomService() != null
                        && reservationAdditionalItemAndServiceController.selectedAdditionalService.getTblRoomService().getIdroomService() == 1
                                ? ClassFormatter.dateFormate.format(Date.valueOf(param.getValue().getDataDate().plusDays(1)))
                                : ClassFormatter.dateFormate.format(Date.valueOf(param.getValue().getDataDate())), param.getValue().dataDateProperty()));
        dataDate.setMinWidth(140);

        TableColumn<RTDListDate, Boolean> selectedDate = new TableColumn("Buat");
        selectedDate.setCellValueFactory(cellData -> cellData.getValue().selectedDateProperty());
        selectedDate.setMinWidth(80);
        selectedDate.setCellFactory(CheckBoxTableCell.forTableColumn(selectedDate));
        selectedDate.setEditable(true);

        tabelAdditionalDate.getColumns().addAll(dataDate, selectedDate);
        tabelAdditionalDate.setItems(FXCollections.observableArrayList(loadAllDataRTDListDate(dataRRTD)));

        tabelAdditionalDate.setRowFactory(tv -> {
            TableRow<RTDListDate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    RTDListDate rowData = row.getItem();
                    rowData.setSelectedDate(!rowData.getSelectedDate());
                }
            });
            return row;
        });

        AnchorPane.setBottomAnchor(tabelAdditionalDate, 0.0);
        AnchorPane.setTopAnchor(tabelAdditionalDate, 0.0);
        AnchorPane.setRightAnchor(tabelAdditionalDate, 0.0);
        AnchorPane.setLeftAnchor(tabelAdditionalDate, 0.0);
        ancTabelDateLayout.getChildren().add(tabelAdditionalDate);
    }

    private List<RTDListDate> loadAllDataRTDListDate(TblReservationRoomTypeDetail dataRRTD) {
        List<RTDListDate> list = new ArrayList<>();
        if (dataRRTD != null) {
//        LocalDate dateCount = ((java.sql.Date) reservationAdditionalItemAndServiceController.selectedAdditionalService.getTblReservationRoomTypeDetail().getCheckInDateTime()).toLocalDate();
            LocalDate dateCount = LocalDate.parse(
                    (new SimpleDateFormat("yyyy-MM-dd")).format(dataRRTD.getCheckInDateTime())
                    + "T"
                    + (new SimpleDateFormat("HH:mm:ss").format(dataRRTD.getCheckInDateTime())),
                    DateTimeFormatter.ISO_DATE_TIME);
            LocalDate dateLast = LocalDate.parse(
                    (new SimpleDateFormat("yyyy-MM-dd")).format(dataRRTD.getCheckOutDateTime())
                    + "T"
                    + (new SimpleDateFormat("HH:mm:ss").format(dataRRTD.getCheckOutDateTime())),
                    DateTimeFormatter.ISO_DATE_TIME);
            dateCount = dateCount.minusDays(0);
            while (dateCount.isBefore(dateLast)) {
                if (LocalDateTime.now().isBefore(
                        LocalDateTime.of(dateCount.plusDays(1), LocalTime.of(
                                        reservationAdditionalItemAndServiceController.getParentController().defaultCheckOutTime.getHours(),
                                        reservationAdditionalItemAndServiceController.getParentController().defaultCheckOutTime.getMinutes(),
                                        0)))) {
                    RTDListDate data = new RTDListDate(dateCount, false);
                    list.add(data);
                }
                dateCount = dateCount.plusDays(1);
            }
        }
        return list;
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        Bindings.bindBidirectional(txtAdditionalCharge.textProperty(), reservationAdditionalItemAndServiceController.selectedAdditionalService.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());

//        chbUsedPeopleNumber.setSelected(peopleNumber.get() > 0);
        spPeopleNumber.getValueFactory().setValue(peopleNumber.get());
        spPeopleNumber.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                peopleNumber.set(newVal);
            }
        });

        cbpService.valueProperty().bindBidirectional(reservationAdditionalItemAndServiceController.selectedAdditionalService.tblRoomServiceProperty());

        cbpService.valueProperty().addListener((obs, olVal, newVal) -> {
            if (newVal != null) {
//                txtServiceName.setText(newVal.getServiceName());
                reservationAdditionalItemAndServiceController.selectedAdditionalService.setPrice(newVal.getPrice());
            } else {
//                txtServiceName.setText("");
                reservationAdditionalItemAndServiceController.selectedAdditionalService.setPrice(new BigDecimal("0"));
            }
            initDataTableDateSelected(reservationAdditionalItemAndServiceController.selectedAdditionalService.getTblReservationRoomTypeDetail());
        });

        cbpService.hide();
    }

    private void dataAdditionalServiceSaveHandle() {
        if (checkDataInputDataAdditionalService()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationAdditionalItemAndServiceController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                reservationAdditionalItemAndServiceController.selectedAdditionalServices = new ArrayList<>();
                //save and set data additional service
                for (RTDListDate dataDate : tabelAdditionalDate.getItems()) {
                    if (dataDate.getSelectedDate()) {
                        TblReservationAdditionalService dataAddtional = new TblReservationAdditionalService();
                        dataAddtional.setTblReservationRoomTypeDetail(reservationAdditionalItemAndServiceController.selectedAdditionalService.getTblReservationRoomTypeDetail());
                        dataAddtional.setTblRoomService(reservationAdditionalItemAndServiceController.selectedAdditionalService.getTblRoomService());
                        dataAddtional.setPrice(reservationAdditionalItemAndServiceController.selectedAdditionalService.getPrice());
                        dataAddtional.setAdditionalDate(Date.valueOf(dataDate.getDataDate()));
                        dataAddtional.setRefReservationBillType(new RefReservationBillType(reservationAdditionalItemAndServiceController.selectedAdditionalService.getRefReservationBillType()));
                        //set reservation additonal service - discount
                        setAdditionalServiceDiscount(dataAddtional, reservationAdditionalItemAndServiceController.dataReservationBill);
                        if (chbUsedPeopleNumber.isSelected()) {
                            for (int i = 0; i < peopleNumber.get(); i++) {
                                TblReservationAdditionalService intrDataAddtional = new TblReservationAdditionalService(dataAddtional);
                                reservationAdditionalItemAndServiceController.selectedAdditionalServices.add(intrDataAddtional);
                            }
                        } else {
                            reservationAdditionalItemAndServiceController.selectedAdditionalServices.add(dataAddtional);
                        }
                    }
                }
                //dummy entry
                TblReservation dummySelectedData = new TblReservation(reservationAdditionalItemAndServiceController.getParentController().selectedDataRoomTypeDetail.getTblReservation());
                List<TblReservationAdditionalService> dummyReservationAdditionalServices = new ArrayList<>();
                for (TblReservationAdditionalService selectedAdditionalService : reservationAdditionalItemAndServiceController.selectedAdditionalServices) {
                    TblReservationAdditionalService dummyReservationAdditionalService = new TblReservationAdditionalService(selectedAdditionalService);
                    dummyReservationAdditionalService.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummyReservationAdditionalService.getTblReservationRoomTypeDetail()));
                    dummyReservationAdditionalService.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                    dummyReservationAdditionalService.setTblRoomService(new TblRoomService(dummyReservationAdditionalService.getTblRoomService()));
                    dummyReservationAdditionalServices.add(dummyReservationAdditionalService);
                }
                if (reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().insertDataReservationAddtionalServices(dummySelectedData, dummyReservationAdditionalServices)) {
                    ClassMessage.showSucceedInsertingDataMessage("", reservationAdditionalItemAndServiceController.dialogStage);
                    // refresh all data and close form data - reservation additional service
                    reservationAdditionalItemAndServiceController.tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(reservationAdditionalItemAndServiceController.loadAllDataReservationAdditionalService()));
                    reservationAdditionalItemAndServiceController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getErrorMessage(), reservationAdditionalItemAndServiceController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationAdditionalItemAndServiceController.dialogStage);
        }
    }

    private void setAdditionalServiceDiscount(TblReservationAdditionalService dataAdditionalService,
            TblReservationBill dataRCOBill) {
        dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
        if (dataRCOBill.getRefReservationBillDiscountType() != null) {
            if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
                if (dataAdditionalService.getTblRoomService().getHotelDiscountable()) {
                    List<TblHotelEvent> dataHotelEvents = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataHotelEventByDateInRangeOrderByMaxServiceDiscountPercentage(dataAdditionalService.getAdditionalDate());
                    if (!dataHotelEvents.isEmpty()) {
                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
                            if (dataAdditionalService.getPrice().compareTo(dataHotelEvent.getMinTransaction()) > -1) {
                                if ((dataAdditionalService.getPrice().multiply(dataHotelEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                        .compareTo(dataHotelEvent.getDiscountNominal()) == 1) {
                                    dataAdditionalService.setTblHotelEvent(dataHotelEvent);
                                    dataAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
                                    break;
                                } else {
                                    dataAdditionalService.setTblHotelEvent(dataHotelEvent);
                                    dataAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 1) { //Card-Event = '1'
                    if (dataAdditionalService.getTblRoomService().getCardDiscountable()) {
                        List<TblBankEventCard> dataCardEvents = reservationAdditionalItemAndServiceController.getParentController().getServiceRV2().getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxServiceDiscountPercentage(
                                dataAdditionalService.getAdditionalDate(),
                                dataRCOBill.getTblBankCard().getIdbankCard(),
                                dataRCOBill.getTblBankCard().getTblBank().getIdbank());
                        if (!dataCardEvents.isEmpty()) {
                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
                                if (dataAdditionalService.getPrice().compareTo(dataCardEvent.getMinTransaction()) > -1) {
                                    if ((dataAdditionalService.getPrice().multiply(dataCardEvent.getServiceDiscountPercentage().divide(new BigDecimal("100"))))
                                            .compareTo(dataCardEvent.getDiscountNominal()) == 1) {
                                        dataAdditionalService.setTblBankEventCard(dataCardEvent);
                                        dataAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
                                        break;
                                    } else {
                                        dataAdditionalService.setTblBankEventCard(dataCardEvent);
                                        dataAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
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

    private void dataAdditionalServiceCancelHandle() {
        //close form data additional service input
        reservationAdditionalItemAndServiceController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataAdditionalService() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpService.getValue() == null) {
            dataInput = false;
            errDataInput += "Layanan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtAdditionalCharge.getText() == null
                || txtAdditionalCharge.getText().equals("")
                || txtAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Tambahan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationAdditionalItemAndServiceController.selectedAdditionalService.getPrice().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Tambahan : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (!checkSelectedDate()) {
            dataInput = false;
            errDataInput += "Tanggal : Tidak ada data yang dipilih \n";
        }
        if (chbUsedPeopleNumber.isSelected()) {
            if (spPeopleNumber.getValue() == null) {
                dataInput = false;
                errDataInput += "Jumlah Orang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (spPeopleNumber.getValue() <= 0) {
                    dataInput = false;
                    errDataInput += "Jumlah Orang : harus lebih besar dari pada '0' \n";
                }
            }
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

    class RTDListDate {

        private final ObjectProperty<LocalDate> dataDate = new SimpleObjectProperty<>();

        private final BooleanProperty selectedDate = new SimpleBooleanProperty();

        public RTDListDate() {

        }

        public RTDListDate(LocalDate date, boolean selected) {
            dataDate.set(date);
            selectedDate.set(selected);
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
        initFormDataAdditionalService();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationAdditionalServiceInputController(ReservationAdditionalItemAndServiceController parentController) {
        reservationAdditionalItemAndServiceController = parentController;
    }

    private final ReservationAdditionalItemAndServiceController reservationAdditionalItemAndServiceController;

}
