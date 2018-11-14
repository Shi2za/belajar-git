/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_end_of_day.L3;

import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.service.FEndOfDayManager;
import hotelfx.view.feature_end_of_day.EndOfDayController;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class EODL3Controller implements Initializable {

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

    private final PseudoClass eodPseudoClass = PseudoClass.getPseudoClass("eod");

    private ClassFilteringTable<EndOfDayController.DailyReservationRoomBill> cft;

    private TableView<EndOfDayController.DailyReservationRoomBill> tableData;

    private void initData() {
        //set title
        lblTitleData.setText("Data Tagihan Kamar Harian : " + ClassFormatter.dateFormate.format(Date.valueOf(parentController.eodDate)));
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
        TableView<EndOfDayController.DailyReservationRoomBill> tableView = new TableView();

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> roomName = new TableColumn("No. Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> getRoomNumber(param.getValue().getDataRRTDRPD().getTblReservationRoomTypeDetail()),
                        param.getValue().dataRRTDRPDProperty()));
        roomName.setMinWidth(140);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> codeReservation = new TableColumn<>("No. Reservasi");
        codeReservation.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> getReservationCode(param.getValue().getDataRRTDRPD().getTblReservationRoomTypeDetail()),
                        param.getValue().dataRRTDRPDProperty()));
        codeReservation.setMinWidth(120);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> reservationStatus = new TableColumn<>("Status");
        reservationStatus.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> getReservationStatus(param.getValue().getDataRRTDRPD().getTblReservationRoomTypeDetail()),
                        param.getValue().dataRRTDRPDProperty()));
        reservationStatus.setMinWidth(120);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> customerName = new TableColumn<>("Customer");
        customerName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> getCustomerName(param.getValue().getDataRRTDRPD().getTblReservationRoomTypeDetail()),
                        param.getValue().dataRRTDRPDProperty()));
        customerName.setMinWidth(140);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> arrivalDate = new TableColumn<>("CheckIn");
        arrivalDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> getArrivalDate(param.getValue().getDataRRTDRPD().getTblReservationRoomTypeDetail()),
                        param.getValue().dataRRTDRPDProperty()));
        arrivalDate.setMinWidth(120);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> departureDate = new TableColumn<>("CheckOut");
        departureDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> getDepartureDate(param.getValue().getDataRRTDRPD().getTblReservationRoomTypeDetail()),
                        param.getValue().dataRRTDRPDProperty()));
        departureDate.setMinWidth(120);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> titledDate = new TableColumn("Tanggal");
        titledDate.getColumns().addAll(arrivalDate, departureDate);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> dailyBill = new TableColumn<>("Harian");
        dailyBill.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDailyBill()),
                        param.getValue().dailyBillProperty()));
        dailyBill.setMinWidth(140);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> currentBill = new TableColumn<>("Saat Ini");
        currentBill.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getCurrentBill()),
                        param.getValue().currentBillProperty()));
        currentBill.setMinWidth(140);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> billAfterPosting = new TableColumn<>("Setelah Posting");
        billAfterPosting.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DailyReservationRoomBill, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBillAfterPosting()),
                        param.getValue().billAfterPostingProperty()));
        billAfterPosting.setMinWidth(140);

        TableColumn<EndOfDayController.DailyReservationRoomBill, String> titledBill = new TableColumn("Tagihan");
        titledBill.getColumns().addAll(dailyBill, currentBill, billAfterPosting);

        tableView.getColumns().addAll(roomName, codeReservation, reservationStatus, customerName, titledDate, titledBill);

        tableView.setItems(FXCollections.observableArrayList(parentController.dailyReservationRoomBills));

        tableView.setRowFactory(tv -> {
            TableRow<EndOfDayController.DailyReservationRoomBill> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(eodPseudoClass, getEoDError(newVal));
                } else {
                    row.pseudoClassStateChanged(eodPseudoClass, false);
                }
            });
            return row;
        });

        tableData = tableView;

        //set filter (searching)
        cft = new ClassFilteringTable<>(
                EndOfDayController.DailyReservationRoomBill.class,
                tableData,
                tableData.getItems());
        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
//        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getRoomNumber(TblReservationRoomTypeDetail dataRRTD){
        if (dataRRTD != null
                && dataRRTD.getTblReservationCheckInOut() != null
                && dataRRTD.getTblReservationCheckInOut().getTblRoom() != null) {
            return dataRRTD.getTblReservationCheckInOut().getTblRoom().getRoomName();
        }
        return "";
    }
    
    private String getReservationCode(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return dataRRTD.getTblReservation().getCodeReservation();
        }
        return "";
    }

    private String getReservationStatus(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return dataRRTD.getTblReservation().getRefReservationStatus().getStatusName();
        }
        return "";
    }

    private String getCustomerName(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return dataRRTD.getTblReservation().getTblCustomer().getTblPeople().getFullName();
        }
        return "";
    }

    private String getArrivalDate(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return ClassFormatter.dateFormate.format(dataRRTD.getCheckInDateTime());
        }
        return "";
    }

    private String getDepartureDate(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return ClassFormatter.dateFormate.format(dataRRTD.getCheckOutDateTime());
        }
        return "";
    }

    private boolean getEoDError(EndOfDayController.DailyReservationRoomBill dailyReservationRoomBill) {
        if (dailyReservationRoomBill != null) {

        }
        return false;
    }
    
    public void refreshDataTable() {
        tableData.setItems(FXCollections.observableArrayList(parentController.dailyReservationRoomBills));
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
        refreshDataTable();
    }

    public EODL3Controller(EndOfDayController parentController) {
        this.parentController = parentController;
    }

    private final EndOfDayController parentController;

    public FEndOfDayManager getService() {
        return parentController.getService();
    }

}
