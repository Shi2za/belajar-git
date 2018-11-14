/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblRoomService;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
 * @author ABC-Programmer
 */
public class RestoAdditionalServiceController implements Initializable {

    @FXML
    private AnchorPane ancFormDataReservationAdditionalService;

    @FXML
    private GridPane gpFormDataReservationAdditionalService;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtCodeReservation;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private AnchorPane ancRoomService;
    private JFXCComboBoxTablePopup<TblRoomService> cbpService;
    
    @FXML
    private JFXTextField txtRestoTransactionNumber;

    @FXML
    private JFXDatePicker dtpRestoTransactionDate;
    
    @FXML
    private JFXTextField txtTotalTransaction;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationAdditionalService() {
        initDataAdditionalServicePopup();
        
        btnSave.setTooltip(new Tooltip("Simpan (Data Tagihan Resto)"));
        btnSave.setOnAction((e) -> {
            dataReservationAddtionalServiceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationAdditionalServiceCancelHandle();
        });

        initDateCalendar();
        
        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpRestoTransactionDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpRestoTransactionDate);
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpService,
                txtRestoTransactionNumber,
                dtpRestoTransactionDate,
                txtTotalTransaction);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalTransaction);
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
        ancRoomService.getChildren().clear();
        ancRoomService.getChildren().add(cbpService);
    }
    
    private void refreshDataPopup() {
        //Room Service
        ObservableList<TblRoomService> serviceItems = FXCollections.observableArrayList(loadAllDataService());
        cbpService.setItems(serviceItems);
    }
    
    private List<TblRoomService> loadAllDataService() {
        List<TblRoomService> list = new ArrayList<>();
        //data room dining
        TblRoomService roomDining = reservationAdditionalServiceRestoController.getParentController().getService().getDataRoomService(6);        //Room Dining = '6'
        list.add(roomDining);
        //data dine in resto
        TblRoomService dineInResto = reservationAdditionalServiceRestoController.getParentController().getService().getDataRoomService(7);        //Dine in Resto = '7'
        list.add(dineInResto);
        return list;
    }
    
    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtRoomName.setText(reservationAdditionalServiceRestoController.selectedDataAdditionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName());
        txtCodeReservation.setText(reservationAdditionalServiceRestoController.selectedDataAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation());
        txtCustomerName.setText(reservationAdditionalServiceRestoController.selectedDataAdditionalService.getTblReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getFullName());

        if (reservationAdditionalServiceRestoController.selectedDataAdditionalService.getAdditionalDate()!= null) {
            dtpRestoTransactionDate.setValue(LocalDate.of(reservationAdditionalServiceRestoController.selectedDataAdditionalService.getAdditionalDate().getYear() + 1900,
                    reservationAdditionalServiceRestoController.selectedDataAdditionalService.getAdditionalDate().getMonth() + 1,
                    reservationAdditionalServiceRestoController.selectedDataAdditionalService.getAdditionalDate().getDate()));
        } else {
            dtpRestoTransactionDate.setValue(null);
        }
        dtpRestoTransactionDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reservationAdditionalServiceRestoController.selectedDataAdditionalService.setAdditionalDate(Date.valueOf(newVal));
            }
        });
        
        txtRestoTransactionNumber.textProperty().bindBidirectional(reservationAdditionalServiceRestoController.selectedDataAdditionalService.restoTransactionNumberProperty());
        txtTotalTransaction.textProperty().bindBidirectional(reservationAdditionalServiceRestoController.selectedDataAdditionalService.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());
        
        cbpService.valueProperty().bindBidirectional(reservationAdditionalServiceRestoController.selectedDataAdditionalService.tblRoomServiceProperty());
        
        cbpService.hide();
    }

    private void dataReservationAddtionalServiceSaveHandle() {
        if (checkDataInputDataReservationAdditionalService()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationAdditionalServiceRestoController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservationAdditionalService dummySelectedData = new TblReservationAdditionalService(reservationAdditionalServiceRestoController.selectedDataAdditionalService);
                if (reservationAdditionalServiceRestoController.getParentController().getService().insertDataReservationAdditionalService(dummySelectedData)
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", reservationAdditionalServiceRestoController.dialogStage);
                    // refresh all data and close form data - reservation additional service
                    reservationAdditionalServiceRestoController.tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(reservationAdditionalServiceRestoController.loadAllDataReservationAdditionalService()));
                    reservationAdditionalServiceRestoController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(reservationAdditionalServiceRestoController.getParentController().getService().getErrorMessage(), reservationAdditionalServiceRestoController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationAdditionalServiceRestoController.dialogStage);
        }
    }

    private void dataReservationAdditionalServiceCancelHandle() {
        //close form data additional service input
        reservationAdditionalServiceRestoController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationAdditionalService() {
        boolean dataInput = true;
        errDataInput = "";
        if(cbpService.getValue() == null){
            dataInput = false;
            errDataInput += "Layanan (Resto)  : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRestoTransactionNumber.getText() == null
                || txtRestoTransactionNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Transaksi  : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if(dtpRestoTransactionDate.getValue() == null){
            dataInput = false;
            errDataInput += "Tanggal Transaksi  : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtTotalTransaction.getText() == null
                || txtTotalTransaction.getText().equals("")
                || txtTotalTransaction.getText().equals("-")) {
            errDataInput += "Total Nominal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (reservationAdditionalServiceRestoController.selectedDataAdditionalService.getPrice()
                    .compareTo(new BigDecimal("0")) < 1) {
                errDataInput += "Total Nominal Transaksi : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        return dataInput;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataReservationAdditionalService();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public RestoAdditionalServiceController(ReservationAdditionalServiceRestoController parentController) {
        reservationAdditionalServiceRestoController = parentController;
    }

    private final ReservationAdditionalServiceRestoController reservationAdditionalServiceRestoController;
    
}
