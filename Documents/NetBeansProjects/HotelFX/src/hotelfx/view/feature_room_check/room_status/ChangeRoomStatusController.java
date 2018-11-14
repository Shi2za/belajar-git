/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblRoom;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
public class ChangeRoomStatusController implements Initializable {

    @FXML
    private AnchorPane ancFormDataRoomStatus;

    @FXML
    private GridPane gpFormDataRoomStatus;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private AnchorPane ancRoomStatusLayout;
    private JFXCComboBoxTablePopup<RefRoomStatus> cbpRoomStatus;

    @FXML
    private Label lblWarningTripleOStatus;
    
    @FXML
    private Label lblRoomStatus;
    
    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private RefRoomStatus currentRoomStatus;

    private void initFormDataChangeRoomStatus() {
        initDataPopup();

        currentRoomStatus = new RefRoomStatus(roomStatusController.selectedDataRoom.getRefRoomStatus());

        btnSave.setTooltip(new Tooltip("Simpan (Data Status Kamar)"));
        btnSave.setOnAction((e) -> {
            dataChangeRoomStatusSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataChangeRoomStatusCancelHandle();
        });

        cbpRoomStatus.valueProperty().addListener((obs, oldVal, newVal) -> {
          //  lblWarningTripleOStatus.setText("");
            if (newVal != null
                    && newVal.getIdstatus() == 6) {  //Out Of Order = '6'
                if (currentRoomStatus.getIdstatus() == 1 //Occupied Clean = '1'
                        || currentRoomStatus.getIdstatus() == 2) {   //Occupied Dirty = '2'
                   // lblWarningTripleOStatus.setText("Jangan lupa untuk melakukan pindah kamar (tamu)");
                }
            }
        });
        
        initImportantFieldColor();
        
        lblRoomStatus.setText("Status Kamar : \n"+ 
        "VD = Vacant Dirty\t\t VC = Vacant Clean\t VR = Vacant Ready \n"+
        "OC = Occupied Clean\t OD = Occupied Dirty\t OOO = Out Of Order");
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpRoomStatus);
    }
    
    private void initDataPopup() {
        //Room Status
        TableView<RefRoomStatus> tableRoomStatus = new TableView<>();

        TableColumn<RefRoomStatus, String> roomStatusName = new TableColumn<>("Status Kamar");
        roomStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        roomStatusName.setMinWidth(140);
        
        TableColumn<RefRoomStatus,String>roomStatusNote = new TableColumn<>("Keterangan");
        roomStatusNote.setCellValueFactory(cellData -> cellData.getValue().statusNoteProperty());
        roomStatusNote.setMinWidth(200);
        tableRoomStatus.getColumns().addAll(roomStatusName,roomStatusNote);

        ObservableList<RefRoomStatus> roomStatusItems = FXCollections.observableArrayList(loadAllDataRoomStatus());

        cbpRoomStatus = new JFXCComboBoxTablePopup<>(
                RefRoomStatus.class, tableRoomStatus, roomStatusItems, "", "Status Kamar *", true, 400,300
        );

        //attached to grid-pane
        ancRoomStatusLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setRightAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setTopAnchor(cbpRoomStatus, 0.0);
        ancRoomStatusLayout.getChildren().add(cbpRoomStatus);
    }

    private List<RefRoomStatus> loadAllDataRoomStatus() {
        List<RefRoomStatus> list = roomStatusController.getService().getAllDataRoomStatus();
        if (roomStatusController.selectedDataRoom != null
                && roomStatusController.selectedDataRoom.getRefRoomStatus() != null) {
            String vooStatus = "";
            if (roomStatusController.selectedDataRoom.getRefRoomStatus().getIdstatus() == 6) {    //Out Of Order
                vooStatus = "3O";
            } else {
                if (roomStatusController.selectedDataRoom.getRefRoomStatus().getIdstatus() == 1 //Occupied Clean = '1'
                        || roomStatusController.selectedDataRoom.getRefRoomStatus().getIdstatus() == 2) {    //Occupied Dirty = '2'
                    vooStatus = "Occupied";
                } else {
                    vooStatus = "Vacant";
                }
            }
            switch (vooStatus) {
                case "Vacant":
                    for (int i = list.size() - 1; i > -1; i--) {
                        if (list.get(i).getIdstatus() != 3 //Vacant Clean = '3'
//                                && list.get(i).getIdstatus() != 4 //Vacant Dirty = '4'
                                && list.get(i).getIdstatus() != 5 //Vacant Ready = '5'
                                && list.get(i).getIdstatus() != 6) {    //Out Of Order = '6'
                            list.remove(i);
                        }
                    }
                    break;
                case "Occupied":
                    for (int i = list.size() - 1; i > -1; i--) {
                        if (list.get(i).getIdstatus() != 1 //Occupied Clean = '1'
//                                && list.get(i).getIdstatus() != 2 //Occupied Dirty = '2'
//                                && list.get(i).getIdstatus() != 6    //Out Of Order = '6'                            
                                ) {
                            list.remove(i);
                        }
                    }
                    break;
                case "3O":
                    for (int i = list.size() - 1; i > -1; i--) {
                        if (list.get(i).getIdstatus() != 3 //Vacant Clean = '3'
//                                && list.get(i).getIdstatus() != 4 //Vacant Dirty = '4'
                                && list.get(i).getIdstatus() != 5 //Vacant Ready = '5'
//                                && list.get(i).getIdstatus() != 6   //Out Of Order = '6'
                                ) {    
                            list.remove(i);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Room Status
        ObservableList<RefRoomStatus> roomStatusItems = FXCollections.observableArrayList(loadAllDataRoomStatus());
        cbpRoomStatus.setItems(roomStatusItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        Bindings.bindBidirectional(txtRoomName.textProperty(), roomStatusController.selectedDataRoom.roomNameProperty());

       // lblWarningTripleOStatus.setText("");

        cbpRoomStatus.valueProperty().bindBidirectional(roomStatusController.selectedDataRoom.refRoomStatusProperty());
        cbpRoomStatus.hide();
    }

    private void dataChangeRoomStatusSaveHandle() {
        if (checkDataInputDataChangeRoomStatus()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblRoom dummySeletedDataRoom = new TblRoom(roomStatusController.selectedDataRoom);
                dummySeletedDataRoom.setRefRoomStatus(new RefRoomStatus(dummySeletedDataRoom.getRefRoomStatus()));
                if (roomStatusController.getService().updateDataRoomStatus(dummySeletedDataRoom)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", roomStatusController.dialogStage);
                    // refresh all data and close form data - change room status
                    roomStatusController.setSelectedDataToInputForm();
                    roomStatusController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(roomStatusController.getService().getErrorMessage(), roomStatusController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusController.dialogStage);
        }
    }

    private void dataChangeRoomStatusCancelHandle() {
        //refresh all data and close form data - change room status
        roomStatusController.setSelectedDataToInputForm();
        roomStatusController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataChangeRoomStatus() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpRoomStatus.getValue() == null) {
            dataInput = false;
            errDataInput += "Status Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataChangeRoomStatus();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ChangeRoomStatusController(RoomStatusController parentController) {
        roomStatusController = parentController;
    }

    private final RoomStatusController roomStatusController;

}
