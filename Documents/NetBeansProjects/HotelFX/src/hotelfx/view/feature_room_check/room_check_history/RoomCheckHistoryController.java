/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_check_history;

import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendance;
import hotelfx.persistence.service.FRoomCheckManager;
import hotelfx.view.feature_room_check.RoomCheckController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RoomCheckHistoryController implements Initializable {

    @FXML
    private AnchorPane tableDataRoomCheckHistoryLayout;

    private ClassFilteringTable<TblRoomCheckHouseKeepingAttendance> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private TableView tableDataRoomCheckHistory;

    private void initTableDataRoomCheckHistory() {
        setTableDataRoomCheckHistory();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataRoomCheckHistory, 15.0);
        AnchorPane.setLeftAnchor(tableDataRoomCheckHistory, 15.0);
        AnchorPane.setBottomAnchor(tableDataRoomCheckHistory, 15.0);
        AnchorPane.setRightAnchor(tableDataRoomCheckHistory, 15.0);
        ancBodyLayout.getChildren().add(tableDataRoomCheckHistory);
    }

    private void setTableDataRoomCheckHistory() {
        TableView<TblRoomCheckHouseKeepingAttendance> tableView = new TableView();

        TableColumn<TblRoomCheckHouseKeepingAttendance, String> roomCheckDate = new TableColumn("Tanggal");
        roomCheckDate.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckHouseKeepingAttendance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate()),
                        param.getValue().createdDateProperty()));
        roomCheckDate.setMinWidth(140);

        TableColumn<TblRoomCheckHouseKeepingAttendance, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckHouseKeepingAttendance, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoom().getRoomName(),
                        param.getValue().tblRoomProperty()));
        roomName.setMinWidth(140);

        TableColumn<TblRoomCheckHouseKeepingAttendance, String> employeeName = new TableColumn("Karyawan");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckHouseKeepingAttendance, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdchecker().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeByIdcheckerProperty()));
        employeeName.setMinWidth(140);

        TableColumn<TblRoomCheckHouseKeepingAttendance, String> before = new TableColumn("Awal");
        before.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckHouseKeepingAttendance, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefRoomStatusByRoomStatusBefore() != null 
                        ? param.getValue().getRefRoomStatusByRoomStatusBefore().getStatusName() : "-",
                        param.getValue().refRoomStatusByRoomStatusBeforeProperty()));
        before.setMinWidth(100);

        TableColumn<TblRoomCheckHouseKeepingAttendance, String> after = new TableColumn("Akhir");
        after.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomCheckHouseKeepingAttendance, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefRoomStatusByRoomStatusAfter() != null 
                        ? param.getValue().getRefRoomStatusByRoomStatusAfter().getStatusName() : "-",
                        param.getValue().refRoomStatusByRoomStatusAfterProperty()));
        after.setMinWidth(100);

        TableColumn<TblRoomCheckHouseKeepingAttendance, String> roomStatus = new TableColumn("Status Kamar");
        roomStatus.getColumns().addAll(before, after);

        tableView.getColumns().addAll(
                roomCheckDate,
                roomName,
                employeeName,
                roomStatus);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataRoomCheckHistory()));

        tableDataRoomCheckHistory = tableView;

        //set filter
        cft = new ClassFilteringTable<>(
                TblRoomCheckHouseKeepingAttendance.class,
                tableDataRoomCheckHistory,
                tableDataRoomCheckHistory.getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public List<TblRoomCheckHouseKeepingAttendance> loadAllDataRoomCheckHistory() {
        List<TblRoomCheckHouseKeepingAttendance> list = parentController.getFRoomCheckManager().getAllHouseKeepingAttendance();
        for(TblRoomCheckHouseKeepingAttendance data : list){
            //data room
            data.setTblRoom(parentController.getFRoomCheckManager().getDataRoom(data.getTblRoom().getIdroom()));
            //data employee (checker)
            data.setTblEmployeeByIdchecker(parentController.getFRoomCheckManager().getDataEmployee(data.getTblEmployeeByIdchecker().getIdemployee()));
            //data people
            data.getTblEmployeeByIdchecker().setTblPeople(parentController.getFRoomCheckManager().getDataPeople(data.getTblEmployeeByIdchecker().getTblPeople().getIdpeople()));
            //data room status (before)
            if(data.getRefRoomStatusByRoomStatusBefore() != null){
                data.setRefRoomStatusByRoomStatusBefore(parentController.getFRoomCheckManager().getDataRoomStatus(data.getRefRoomStatusByRoomStatusBefore().getIdstatus()));
            }
            //data room status (after)
            if(data.getRefRoomStatusByRoomStatusAfter() != null){
                data.setRefRoomStatusByRoomStatusAfter(parentController.getFRoomCheckManager().getDataRoomStatus(data.getRefRoomStatusByRoomStatusAfter().getIdstatus()));
            }
        }
        return list;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableDataRoomCheckHistory();
    }

    public RoomCheckHistoryController(RoomCheckController parentController) {
        this.parentController = parentController;
    }

    private final RoomCheckController parentController;

    public FRoomCheckManager getService() {
        return parentController.getFRoomCheckManager();
    }

}
