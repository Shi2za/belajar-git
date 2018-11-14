/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomStatusDetail;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class ChangeRoomStatusDetailController implements Initializable {

    @FXML
    private AnchorPane ancFormDataRoomStatusDetail;

    @FXML
    private GridPane gpFormDataRoomStatusDetail;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtRoomStatus;

    @FXML
    private JFXButton btnBack;

    private void initFormDataChangeRoomStatusDetail() {

        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            dataChangeRoomStatusDetailCancelHandle();
        });

        initImportantFieldColor();
        
    }

    private void initImportantFieldColor() {
//        ClassViewSetting.setImportantField(
//                );
    }
    
    private void setSelectedDataToInputForm() {

        Bindings.bindBidirectional(txtRoomName.textProperty(), roomStatusController.selectedDataRoom.roomNameProperty());
        Bindings.bindBidirectional(txtRoomStatus.textProperty(), roomStatusController.selectedDataRoom.getRefRoomStatus().statusNameProperty());

        initTableDataRoomStatusDetail();

    }

    /**
     * DATA ROOM STATUS DETAIL
     */
    @FXML
    private AnchorPane ancTableRoomStatusDetailLayout;

    public ClassTableWithControl tableDataRoomStatusDetail;

    private void initTableDataRoomStatusDetail() {
        //set table
        setTableDataRoomStatusDetail();
        //set control
        setTableControlDataRoomStatusDetail();
        //set table-control to content-view
        ancTableRoomStatusDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoomStatusDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataRoomStatusDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataRoomStatusDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataRoomStatusDetail, 0.0);
        ancTableRoomStatusDetailLayout.getChildren().add(tableDataRoomStatusDetail);
    }

    private void setTableDataRoomStatusDetail() {
        TableView<TblRoomStatusDetail> tableView = new TableView();

        TableColumn<TblRoomStatusDetail, String> dateTime = new TableColumn("Tanggal");
        dateTime.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomStatusDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getRoomStatusDetailDate()), param.getValue().roomStatusDetailDateProperty()));
        dateTime.setMinWidth(140);

        TableColumn<TblRoomStatusDetail, String> roomStatusDetail = new TableColumn("Detail Status");
        roomStatusDetail.setCellValueFactory(cellData -> cellData.getValue().roomStatusDetailProperty());
        roomStatusDetail.setMinWidth(140);

        tableView.getColumns().addAll(dateTime, roomStatusDetail);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataRoomStatusDetail()));

        tableDataRoomStatusDetail = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataRoomStatusDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRoomStatusDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomStatusDetailUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataRoomStatusDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Set Status Kamar (Detail)");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomStatusDetailSetHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataRoomStatusDetail.addButtonControl(buttonControls);
    }

    public List<TblRoomStatusDetail> loadAllDataRoomStatusDetail() {
        List<TblRoomStatusDetail> list = roomStatusController.getService().getAllDataRoomStatusDetailByIDRoomAndDetailDateBetween(
                roomStatusController.selectedDataRoom.getIdroom(),
                Timestamp.valueOf((LocalDateTime.now()).minusDays(1)),
                Timestamp.valueOf((LocalDateTime.now()).plusDays(2)));
        return list;
    }

    public TblRoomStatusDetail selectedDataRoomStatusDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputRoomStatusDetailStatus = 0;

    public Stage dialogStage;

    public void dataRoomStatusDetailCreateHandle() {
        dataInputRoomStatusDetailStatus = 0;
        selectedDataRoomStatusDetail = new TblRoomStatusDetail();
        selectedDataRoomStatusDetail.setTblRoom(new TblRoom(roomStatusController.selectedDataRoom));
        //open form data room status detail input
        showRoomStatusDetailInputDialog();
    }

    public void dataRoomStatusDetailUpdateHandle() {
        if (tableDataRoomStatusDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputRoomStatusDetailStatus = 1;
            selectedDataRoomStatusDetail = roomStatusController.getService().getDataRoomStatusDetail(((TblRoomStatusDetail) tableDataRoomStatusDetail.getTableView().getSelectionModel().getSelectedItem()).getIdroomStatusDetail());
            //data room
            selectedDataRoomStatusDetail.setTblRoom(roomStatusController.getService().getDataRoom(selectedDataRoomStatusDetail.getTblRoom().getIdroom()));
            //open form data room status detail input
            showRoomStatusDetailInputDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", roomStatusController.dialogStage);
        }
    }

    public void dataRoomStatusDetailDeleteHandle() {
        if (tableDataRoomStatusDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", roomStatusController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                if (roomStatusController.getService().deleteDataRoomStatusDetail(new TblRoomStatusDetail((TblRoomStatusDetail) tableDataRoomStatusDetail.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", roomStatusController.dialogStage);
                    //refresh data from table
                    tableDataRoomStatusDetail.getTableView().setItems(FXCollections.observableArrayList(loadAllDataRoomStatusDetail()));
                } else {
                    ClassMessage.showFailedDeletingDataMessage(roomStatusController.getService().getErrorMessage(), roomStatusController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", roomStatusController.dialogStage);
        }
    }

    private void dataRoomStatusDetailSetHandle() {
        if (tableDataRoomStatusDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataChangeRoomStatusDetailSaveHandle();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", roomStatusController.dialogStage);
        }
    }

    private void showRoomStatusDetailInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check/room_status/ChangeRoomStatusDetailInputDialog.fxml"));

            ChangeRoomStatusDetailInputController controller = new ChangeRoomStatusDetailInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(roomStatusController.dialogStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    /**
     * HANDLE (SAVE)
     */
    private void dataChangeRoomStatusDetailSaveHandle() {
        if (checkDataInputDataChangeRoomDetailStatus()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblRoom dummySeletedDataRoom = new TblRoom(roomStatusController.selectedDataRoom);
                dummySeletedDataRoom.setRefRoomStatus(new RefRoomStatus(dummySeletedDataRoom.getRefRoomStatus()));
                TblRoomStatusDetail dummySelectedDataRoomStatusDetail = new TblRoomStatusDetail((TblRoomStatusDetail) tableDataRoomStatusDetail.getTableView().getSelectionModel().getSelectedItem());
                dummySelectedDataRoomStatusDetail.setTblRoom(new TblRoom(dummySelectedDataRoomStatusDetail.getTblRoom()));
                if (roomStatusController.getService().updateDataRoomStatusDetail(dummySeletedDataRoom, dummySelectedDataRoomStatusDetail)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", roomStatusController.dialogStage);
                    //refresh all data and close form data - change room status
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

    private void dataChangeRoomStatusDetailCancelHandle() {
        //refresh all data and close form data - change room status
        roomStatusController.setSelectedDataToInputForm();
        roomStatusController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataChangeRoomDetailStatus() {
        boolean dataInput = true;
        errDataInput = "";

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
        initFormDataChangeRoomStatusDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ChangeRoomStatusDetailController(RoomStatusController parentController) {
        roomStatusController = parentController;
    }

    private final RoomStatusController roomStatusController;

    public RoomStatusController getPartentController() {
        return roomStatusController;
    }

}
