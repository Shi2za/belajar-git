/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rcr;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
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
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCRReservationAdditionalItemChangeRoomTypeDetailInputController implements Initializable {

//    @FXML
//    private AnchorPane ancFormAdditionalItem;
//
//    @FXML
//    private GridPane gpFormDataAdditionalItem;
//
//    private final JFXCComboBoxPopup<TblReservationRoomTypeDetail> cbpReservationRoomTypeDetail = new JFXCComboBoxPopup<>(TblReservationRoomTypeDetail.class);
//
//    @FXML
//    private JFXButton btnSave;
//
//    @FXML
//    private JFXButton btnCancel;
//
//    private void initFormDataAdditionalItem() {
//
//        btnSave.setTooltip(new Tooltip("Simpan (Data Kode Reservasi Kamar)"));
//        btnSave.setOnAction((e) -> {
//            dataAdditionalItemSaveHandle();
//        });
//
//        btnCancel.setTooltip(new Tooltip("Batal"));
//        btnCancel.setOnAction((e) -> {
//            dataAdditionalItemCancelHandle();
//        });
//
//        initDataPopup();
//
//    }
//
//    private void initDataPopup() {
//        //Reservation Room Type Detail
//        TableView<TblReservationRoomTypeDetail> tableRRTD = new TableView<>();
//
//        TableColumn<TblReservationRoomTypeDetail, String> codeRRTD = new TableColumn<>("Kode");
//        codeRRTD.setCellValueFactory(cellData -> cellData.getValue().codeDetailProperty());
//        codeRRTD.setMinWidth(120);
//
//        TableColumn<TblReservationRoomTypeDetail, String> rrtdRoomTypeName = new TableColumn<>("Tipe Kamar");
//        rrtdRoomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
//        rrtdRoomTypeName.setMinWidth(140);
//
//        tableRRTD.getColumns().addAll(codeRRTD, rrtdRoomTypeName);
//
//        ObservableList<TblReservationRoomTypeDetail> rrtdItems = FXCollections.observableArrayList(loadAllDataReservationRoomTypeDetail());
//
//        setFunctionPopup(cbpReservationRoomTypeDetail, tableRRTD, rrtdItems, "codeDetail", "Kode Reservasi Kamar *", 300, 200);
//
//        //attached to grid-pane
//        gpFormDataAdditionalItem.add(cbpReservationRoomTypeDetail, 2, 1);
//    }
//
//    private void refreshDataPopup() {
//        //Reservation Room Type Detail
//        ObservableList<TblReservationRoomTypeDetail> rrtdItems = FXCollections.observableArrayList(loadAllDataReservationRoomTypeDetail());
//        cbpReservationRoomTypeDetail.setItems(rrtdItems);
//    }
//
//    private List<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetail() {
//        LocalDate additionalDate = LocalDate.of(reservationChangeRoomInputController.tempSelectedAdditionalItem.getAdditionalDate().getYear() + 1900,
//                reservationChangeRoomInputController.tempSelectedAdditionalItem.getAdditionalDate().getMonth() + 1,
//                reservationChangeRoomInputController.tempSelectedAdditionalItem.getAdditionalDate().getDate());
//        List<TblReservationRoomTypeDetail> list = new ArrayList<>();
//        for (TblReservationRoomTypeDetail data : reservationChangeRoomInputController.getParentController().selectedDataRoomTypeDetails) {
//            if ((!additionalDate.isBefore(LocalDate.of(data.getCheckInDateTime().getYear() + 1900,
//                    data.getCheckInDateTime().getMonth() + 1,
//                    data.getCheckInDateTime().getDate())))
//                    && additionalDate.isBefore(LocalDate.of(
//                                    data.getCheckOutDateTime().getYear() + 1900,
//                                    data.getCheckOutDateTime().getMonth() + 1,
//                                    data.getCheckOutDateTime().getDate()))) {
//                list.add(data);
//            }
//        }
//        return list;
//    }
//
//    private void setFunctionPopup(JFXCComboBoxPopup cbp,
//            TableView table,
//            ObservableList items,
//            String nameFiltered,
//            String promptText,
//            double prefWidth,
//            double prefHeight) {
//        table.getSelectionModel().selectedIndexProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    if (newValue.intValue() != -1) {
//                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
//                    }
//                    cbp.hide();
//                });
//
//        cbp.setPropertyNameForFiltered(nameFiltered);
//
//        cbp.setItems(items);
//    
//    cbp.setOnShowing((e) -> {
//            table.getSelectionModel().clearSelection();
//        });
//
//        // Add observable list data to the table
//        table.itemsProperty().bind(cbp.filteredItemsProperty());
//
//        //popup button
//        JFXButton button = new JFXButton("▾");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(prefWidth, prefHeight);
//
//        content.setCenter(table);
//
//        cbp.setPopupEditor(true);
//        cbp.promptTextProperty().set(promptText);
//        cbp.setLabelFloat(false);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
//
//    private void setSelectedDataToInputForm() {
//        refreshDataPopup();
//
//        cbpReservationRoomTypeDetail.setValue(reservationChangeRoomInputController.tempSelectedAdditionalItem.getTblReservationRoomTypeDetail());
//
////        cbpReservationRoomTypeDetail.valueProperty().bindBidirectional(reservationChangeRoomInputController.tempSelectedAdditionalItem.tblReservationRoomTypeDetailProperty());
//        cbpReservationRoomTypeDetail.hide();
//    }
//
//    private void dataAdditionalItemSaveHandle() {
//        if (checkDataInputDataAdditionalItem()) {
//            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationChangeRoomInputController.dialogStage);
//            if (alert.getResult() == ButtonType.OK) {
//                //update and set data additional item
//                reservationChangeRoomInputController.tempSelectedAdditionalItem.setTblReservationRoomTypeDetail(cbpReservationRoomTypeDetail.getValue());
//                reservationChangeRoomInputController.tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(reservationChangeRoomInputController.getParentController().selectedAdditionalItems));
//                //refresh data bill
//                reservationChangeRoomInputController.refreshBill();
//                //close form data addtional input input
//                reservationChangeRoomInputController.dialogStage.close();
//            }
//        } else {
//            ClassMessage.showWarningInputDataMessage(errDataInput, reservationChangeRoomInputController.dialogStage);
//        }
//    }
//
//    private void dataAdditionalItemCancelHandle() {
//        //close form data additional item input
//        reservationChangeRoomInputController.dialogStage.close();
//    }
//
//    private String errDataInput;
//
//    private boolean checkDataInputDataAdditionalItem() {
//        boolean dataInput = true;
//        errDataInput = "";
//        if (cbpReservationRoomTypeDetail.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Kode Reservasi Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        return dataInput;
//    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        //init form input
//        initFormDataAdditionalItem();
//        //refresh data form input
//        setSelectedDataToInputForm();
    }

    public RCRReservationAdditionalItemChangeRoomTypeDetailInputController(ReservationChangeRoomInputController parentController) {
        reservationChangeRoomInputController = parentController;
    }

    private final ReservationChangeRoomInputController reservationChangeRoomInputController;

}
