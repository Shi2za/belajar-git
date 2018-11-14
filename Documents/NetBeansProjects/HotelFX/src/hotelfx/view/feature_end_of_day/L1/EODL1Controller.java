/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_end_of_day.L1;

import com.jfoenix.controls.JFXButton;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.service.FEndOfDayManager;
import hotelfx.view.feature_end_of_day.EndOfDayController;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class EODL1Controller implements Initializable {

    @FXML
    private AnchorPane ancBaseLayout;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    /**
     * TABLE DATA
     */
    @FXML
    private Label lblTitleData;

    private ClassFilteringTable<EndOfDayController.DataReservationRoomTypeDetail> cft;

    private TableView<EndOfDayController.DataReservationRoomTypeDetail> tableData;

    private void initData() {
        //set title
        lblTitleData.setText("Data Reservasi : " + ClassFormatter.dateFormate.format(Date.valueOf(parentController.eodDate)));
        //set table
        setTableData();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableData, 0.0);
        AnchorPane.setLeftAnchor(tableData, 15.0);
        AnchorPane.setRightAnchor(tableData, 15.0);
        AnchorPane.setTopAnchor(tableData, 15.0);
        ancBodyLayout.getChildren().add(tableData);
    }

    private void setTableData() {
        TableView<EndOfDayController.DataReservationRoomTypeDetail> tableView = new TableView();

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> codeReservation = new TableColumn("No. Reservasi");
        codeReservation.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getCodeReservation(),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        codeReservation.setMinWidth(110);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> roomName = new TableColumn("No. Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                        && param.getValue().getDataReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null
                                ? param.getValue().getDataReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                                : "-",
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        roomName.setMinWidth(110);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> reservationDate = new TableColumn("Reservasi");
        reservationDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getReservationDate()),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        reservationDate.setMinWidth(120);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> checkInDate = new TableColumn("CheckIn");
        checkInDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDataReservationRoomTypeDetail().getCheckInDateTime()),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        checkInDate.setMinWidth(120);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> checkOutDate = new TableColumn("CheckOut");
        checkOutDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDataReservationRoomTypeDetail().getCheckOutDateTime()),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        checkOutDate.setMinWidth(120);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> dateTitle = new TableColumn("Tanggal");
        dateTitle.getColumns().addAll(reservationDate, checkInDate, checkOutDate);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> peopleName = new TableColumn<>("Nama");
        peopleName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getFullName(),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        peopleName.setMinWidth(140);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> peopleID = new TableColumn<>("No. Identitas");
        peopleID.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getRefPeopleIdentifierType().getTypeName()
                        + " : " + param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getCodePeople(),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        peopleID.setMinWidth(150);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> peopleHPNumber = new TableColumn<>("No. HP");
        peopleHPNumber.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getHpnumber() != null
                                ? param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getHpnumber() : "-",
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        peopleHPNumber.setMinWidth(120);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> peopleEmail = new TableColumn<>("Email");
        peopleEmail.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getEmail() != null
                                ? param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getEmail() : "-",
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        peopleEmail.setMinWidth(220);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> customerTitle = new TableColumn("Customer");
        customerTitle.getColumns().addAll(peopleID, peopleName, peopleHPNumber);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> travelAgentName = new TableColumn<>("Travel Agent");
        travelAgentName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblPartner() == null
                                ? "-"
                                : param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getTblPartner().getPartnerName(),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        travelAgentName.setMinWidth(140);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> reservationStatus = new TableColumn<>("Reservation");
        reservationStatus.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservation().getRefReservationStatus().getStatusName(),
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        reservationStatus.setMinWidth(120);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> checkInStatus = new TableColumn("CheckIn");
        checkInStatus.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                                ? param.getValue().getDataReservationRoomTypeDetail().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName()
                                : "-",
                        param.getValue().dataReservationRoomTypeDetailProperty()));
        checkInStatus.setMinWidth(130);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> statusTitle = new TableColumn("Status");
        statusTitle.getColumns().addAll(reservationStatus, checkInStatus);

        TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> infoButton = new TableColumn("Info");
        infoButton.setMinWidth(45);
        infoButton.setCellFactory((TableColumn<EndOfDayController.DataReservationRoomTypeDetail, String> param) -> new ButtonCellInfo<>());

        tableView.getColumns().addAll(codeReservation, roomName, dateTitle, customerTitle, statusTitle, infoButton);

        tableView.setItems(FXCollections.observableArrayList(parentController.dataReservations));

        tableData = tableView;

        //set filter (searching)
        cft = new ClassFilteringTable<>(
                EndOfDayController.DataReservationRoomTypeDetail.class,
                tableData,
                tableData.getItems());
        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
//        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public class ButtonCellInfo<S, T> extends TableCell<S, T> {

        private JFXButton button;

        public ButtonCellInfo() {

        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (this.getTableRow() != null) {
                    Object data = this.getTableRow().getItem();
                    if (data != null) {
                        this.button = ((EndOfDayController.DataReservationRoomTypeDetail) data).getBtnMessage();
                        setAlignment(Pos.CENTER);

                        setText(null);
                        setGraphic(button);
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        }

        @Override
        public void startEdit() {

        }

        @Override
        public void cancelEdit() {

        }

    }

    public void refreshDataTableReservation() {
        tableData.setItems(FXCollections.observableArrayList(parentController.dataReservations));
        cft.refreshFilterItems(tableData.getItems());
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init data
        initData();
        //refresh data table
        refreshDataTableReservation();
    }

    public EODL1Controller(EndOfDayController parentController) {
        this.parentController = parentController;
    }

    private final EndOfDayController parentController;

    public FEndOfDayManager getService() {
        return parentController.getService();
    }

}
