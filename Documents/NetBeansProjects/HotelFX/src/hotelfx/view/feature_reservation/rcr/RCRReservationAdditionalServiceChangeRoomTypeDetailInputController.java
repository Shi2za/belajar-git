/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rcr;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.view.feature_reservation.ReservationChangeRoomInputController;
import java.net.URL;
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
public class RCRReservationAdditionalServiceChangeRoomTypeDetailInputController implements Initializable {

    @FXML
    private AnchorPane ancFormAdditionalService;

    @FXML
    private GridPane gpFormDataAdditionalService;

    private JFXCComboBoxTablePopup<TblReservationRoomTypeDetail> cbpReservationRoomTypeDetail;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataAdditionalService() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Kode Reservasi Kamar)"));
        btnSave.setOnAction((e) -> {
            dataAdditionalServiceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataAdditionalServiceCancelHandle();
        });

    }

    private void initDataPopup() {
        //Reservation Room Type Detail
        TableView<TblReservationRoomTypeDetail> tableRRTD = new TableView<>();

        TableColumn<TblReservationRoomTypeDetail, String> codeRRTD = new TableColumn<>("Kode");
        codeRRTD.setCellValueFactory(cellData -> cellData.getValue().codeDetailProperty());
        codeRRTD.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> rrtdRoomTypeName = new TableColumn<>("Tipe Kamar");
        rrtdRoomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
        rrtdRoomTypeName.setMinWidth(140);

        tableRRTD.getColumns().addAll(codeRRTD, rrtdRoomTypeName);

        ObservableList<TblReservationRoomTypeDetail> rrtdItems = FXCollections.observableArrayList(loadAllDataReservationRoomTypeDetail());

        cbpReservationRoomTypeDetail = new JFXCComboBoxTablePopup<>(
                TblReservationRoomTypeDetail.class, tableRRTD, rrtdItems, "", "Kode Reservasi Kamar *", true, 300, 200
        );
        
        //attached to grid-pane
        gpFormDataAdditionalService.add(cbpReservationRoomTypeDetail, 2, 1);
    }

    private void refreshDataPopup() {
        //Reservation Room Type Detail
        ObservableList<TblReservationRoomTypeDetail> rrtdItems = FXCollections.observableArrayList(loadAllDataReservationRoomTypeDetail());
        cbpReservationRoomTypeDetail.setItems(rrtdItems);
    }

    private List<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetail() {
        LocalDate additionalDate = LocalDate.of(reservationChangeRoomInputController.tempSelectedAdditionalService.getAdditionalDate().getYear() + 1900,
                reservationChangeRoomInputController.tempSelectedAdditionalService.getAdditionalDate().getMonth() + 1,
                reservationChangeRoomInputController.tempSelectedAdditionalService.getAdditionalDate().getDate());
        List<TblReservationRoomTypeDetail> list = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : reservationChangeRoomInputController.getParentController().selectedDataRoomTypeDetails) {
            if ((!additionalDate.isBefore(LocalDate.of(data.getCheckInDateTime().getYear() + 1900,
                    data.getCheckInDateTime().getMonth() + 1,
                    data.getCheckInDateTime().getDate())))
                    && additionalDate.isBefore(LocalDate.of(
                                    data.getCheckOutDateTime().getYear() + 1900,
                                    data.getCheckOutDateTime().getMonth() + 1,
                                    data.getCheckOutDateTime().getDate()))) {
                list.add(data);
            }
        }
        return list;
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        cbpReservationRoomTypeDetail.setValue(reservationChangeRoomInputController.tempSelectedAdditionalService.getTblReservationRoomTypeDetail());

//        cbpReservationRoomTypeDetail.valueProperty().bindBidirectional(reservationChangeRoomInputController.tempSelectedAdditionalService.tblReservationRoomTypeDetailProperty());
        cbpReservationRoomTypeDetail.hide();
    }

    private void dataAdditionalServiceSaveHandle() {
        if (checkDataInputDataAdditionalService()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationChangeRoomInputController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //update and set data additional service
                reservationChangeRoomInputController.tempSelectedAdditionalService.setTblReservationRoomTypeDetail(cbpReservationRoomTypeDetail.getValue());
                reservationChangeRoomInputController.tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(reservationChangeRoomInputController.getParentController().selectedAdditionalServices));
                //refresh data bill
                reservationChangeRoomInputController.refreshBill();
                //close form data addtional input input
                reservationChangeRoomInputController.dialogStage.close();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationChangeRoomInputController.dialogStage);
        }
    }

    private void dataAdditionalServiceCancelHandle() {
        //close form data additional service input
        reservationChangeRoomInputController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataAdditionalService() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpReservationRoomTypeDetail.getValue() == null) {
            dataInput = false;
            errDataInput += "Kode Reservasi Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
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

    public RCRReservationAdditionalServiceChangeRoomTypeDetailInputController(ReservationChangeRoomInputController parentController) {
        reservationChangeRoomInputController = parentController;
    }

    private final ReservationChangeRoomInputController reservationChangeRoomInputController;

}
