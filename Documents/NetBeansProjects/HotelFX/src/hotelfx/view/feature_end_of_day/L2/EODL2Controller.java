/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_end_of_day.L2;

import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.service.FEndOfDayManager;
import hotelfx.view.feature_end_of_day.EndOfDayController;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class EODL2Controller implements Initializable {

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

    private ClassFilteringTable<EndOfDayController.DataRoomStatus> cft;

    private TableView<EndOfDayController.DataRoomStatus> tableData;

    private void initData() {
        //set title
        lblTitleData.setText("Data CheckIn/Out : "
                + ClassFormatter.dateFormate.format(Date.valueOf(parentController.eodDate.minusDays(1)))
                + " - "
                + ClassFormatter.dateFormate.format(Date.valueOf(parentController.eodDate)));
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
        TableView<EndOfDayController.DataRoomStatus> tableView = new TableView();

        LocalDate previousDate = (LocalDate.now()).minusDays(1);
        LocalDate nextDate = LocalDate.now();

        TableColumn<EndOfDayController.DataRoomStatus, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRoom().getRoomName()
                        + "\n"
                        + param.getValue().getDataRoom().getTblRoomType().getRoomTypeName(),
                        param.getValue().dataRoomProperty()));
        roomName.setMinWidth(100);

        TableColumn<EndOfDayController.DataRoomStatus, String> previousCustomerName = new TableColumn<>("Customer");
        previousCustomerName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getStatusAndCustomerName(param.getValue().getDataPreviousRRTD()),
                        param.getValue().dataPreviousRRTDProperty()));
        Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>> cellFactoryPC
                = new Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new DataCellPCFactory();
                    }
                };
        previousCustomerName.setCellFactory(cellFactoryPC);
        previousCustomerName.setMinWidth(140);

        TableColumn<EndOfDayController.DataRoomStatus, String> previousArrivalDate = new TableColumn<>("Datang");
        previousArrivalDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getArrivalDate(param.getValue().getDataPreviousRRTD()),
                        param.getValue().dataPreviousRRTDProperty()));
        Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>> cellFactoryPI
                = new Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new DataCellPIFactory();
                    }
                };
        previousArrivalDate.setCellFactory(cellFactoryPI);
        previousArrivalDate.setMinWidth(120);

        TableColumn<EndOfDayController.DataRoomStatus, String> previousDepartureDate = new TableColumn<>("Pergi");
        previousDepartureDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getDepartureDate(param.getValue().getDataPreviousRRTD()),
                        param.getValue().dataPreviousRRTDProperty()));
        Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>> cellFactoryPO
                = new Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new DataCellPOFactory();
                    }
                };
        previousDepartureDate.setCellFactory(cellFactoryPO);
        previousDepartureDate.setMinWidth(120);

        TableColumn<EndOfDayController.DataRoomStatus, String> previousDateTitle = new TableColumn("Tanggal");
        previousDateTitle.getColumns().addAll(previousArrivalDate, previousDepartureDate);

        TableColumn<EndOfDayController.DataRoomStatus, String> previousTitle = new TableColumn(ClassFormatter.dateFormate.format(Date.valueOf(previousDate)));
        previousTitle.getColumns().addAll(previousCustomerName, previousDateTitle);

        TableColumn<EndOfDayController.DataRoomStatus, String> nextCustomerName = new TableColumn<>("Customer");
        nextCustomerName.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getStatusAndCustomerName(param.getValue().getDataNextRRTD()),
                        param.getValue().dataNextRRTDProperty()));
        Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>> cellFactoryNC
                = new Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new DataCellNCFactory();
                    }
                };
        nextCustomerName.setCellFactory(cellFactoryNC);
        nextCustomerName.setMinWidth(140);

        TableColumn<EndOfDayController.DataRoomStatus, String> nextArrivalDate = new TableColumn<>("Datang");
        nextArrivalDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getArrivalDate(param.getValue().getDataNextRRTD()),
                        param.getValue().dataNextRRTDProperty()));
        Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>> cellFactoryNI
                = new Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new DataCellNIFactory();
                    }
                };
        nextArrivalDate.setCellFactory(cellFactoryNI);
        nextArrivalDate.setMinWidth(120);

        TableColumn<EndOfDayController.DataRoomStatus, String> nextDepartureDate = new TableColumn<>("Pergi");
        nextDepartureDate.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> getDepartureDate(param.getValue().getDataNextRRTD()),
                        param.getValue().dataNextRRTDProperty()));
        Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>> cellFactoryNO
                = new Callback<TableColumn<EndOfDayController.DataRoomStatus, String>, TableCell<EndOfDayController.DataRoomStatus, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new DataCellNOFactory();
                    }
                };
        nextDepartureDate.setCellFactory(cellFactoryNO);
        nextDepartureDate.setMinWidth(120);

        TableColumn<EndOfDayController.DataRoomStatus, String> nextDateTitle = new TableColumn("Tanggal");
        nextDateTitle.getColumns().addAll(nextArrivalDate, nextDepartureDate);

        TableColumn<EndOfDayController.DataRoomStatus, String> nextTitle = new TableColumn(ClassFormatter.dateFormate.format(Date.valueOf(nextDate)));
        nextTitle.getColumns().addAll(nextCustomerName, nextDateTitle);

        TableColumn<EndOfDayController.DataRoomStatus, String> roomStatus = new TableColumn<>("Status \nKamar");
        roomStatus.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRoom().getRefRoomStatus().getStatusName(),
                        param.getValue().dataRoomProperty()));
        roomStatus.setMinWidth(100);

        TableColumn<EndOfDayController.DataRoomStatus, String> roomStatusDetail = new TableColumn<>("Status Kamar\n (Detail)");
        roomStatusDetail.setCellValueFactory((TableColumn.CellDataFeatures<EndOfDayController.DataRoomStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRoom().getRoomStatusNote(),
                        param.getValue().dataRoomProperty()));
        roomStatusDetail.setMinWidth(100);

        tableView.getColumns().addAll(roomName, previousTitle, nextTitle, roomStatus, roomStatusDetail);

        tableView.setItems(FXCollections.observableArrayList(parentController.dataRoomWithStatuss));

        tableData = tableView;

        //set filter
        cft = new ClassFilteringTable<>(
                EndOfDayController.DataRoomStatus.class,
                tableData,
                tableData.getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getStatusAndCustomerName(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return "(" + dataRRTD.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName() + ")\n"
                    + dataRRTD.getTblReservation().getTblCustomer().getTblPeople().getFullName() + " -\n"
                    + dataRRTD.getTblReservation().getCodeReservation();
        }
        return "";
    }

    private String getArrivalDate(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return "\n" + ClassFormatter.dateFormate.format(dataRRTD.getCheckInDateTime());
        }
        return "";
    }

    private String getDepartureDate(TblReservationRoomTypeDetail dataRRTD) {
        if (dataRRTD != null) {
            return "\n" + ClassFormatter.dateFormate.format(dataRRTD.getCheckOutDateTime());
        }
        return "";
    }

    class DataCellPCFactory extends TableCell<EndOfDayController.DataRoomStatus, String> {

        public DataCellPCFactory() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        EndOfDayController.DataRoomStatus data = (EndOfDayController.DataRoomStatus) this.getTableRow().getItem();
                        if (data != null) {
                            if (data.getDataPreviousRRTD() != null) {
                                setText(getStatusAndCustomerName(data.getDataPreviousRRTD()));
                                if (data.getDataPreviousRRTD().getTblReservationCheckInOut() != null
                                        && data.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2) {   //Ready to Check Out = '2'
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even");
                                        getStyleClass().add("cell-input-even-error");
                                    } else {
                                        getStyleClass().remove("cell-input-odd");
                                        getStyleClass().add("cell-input-odd-error");
                                    }
                                    setStyle("-fx-background-color:#ffcdd2");
                                } else {
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even-error");
                                        getStyleClass().add("cell-input-even");
                                        setStyle("-fx-background-color:#dcedc8");

                                    } else {
                                        getStyleClass().remove("cell-input-odd-error");
                                        getStyleClass().add("cell-input-odd");
                                        setStyle("-fx-background-color:#f1f8e9");
                                    }
                                }
                            } else {
                                setText("");
                                if (this.getTableRow().getIndex() % 2 == 0) {
                                    getStyleClass().remove("cell-input-even-error");
                                    getStyleClass().add("cell-input-even");
                                    setStyle("-fx-background-color:#dcedc8");

                                } else {
                                    getStyleClass().remove("cell-input-odd-error");
                                    getStyleClass().add("cell-input-odd");
                                    setStyle("-fx-background-color:#f1f8e9");
                                }
                            }
                        } else {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().remove("cell-input-even-error");
                                getStyleClass().add("cell-input-even");
                                setStyle("-fx-background-color:#dcedc8");
                            } else {
                                getStyleClass().remove("cell-input-odd-error");
                                getStyleClass().add("cell-input-odd");
                                setStyle("-fx-background-color:#f1f8e9");
                            }
                        }
                    } else {
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().remove("cell-input-even-error");
                            getStyleClass().add("cell-input-even");
                            setStyle("-fx-background-color:#dcedc8");
                        } else {
                            getStyleClass().remove("cell-input-odd-error");
                            getStyleClass().add("cell-input-odd");
                            setStyle("-fx-background-color:#f1f8e9");
                        }
                    }
                }
            }
        }

    }

    class DataCellPIFactory extends TableCell<EndOfDayController.DataRoomStatus, String> {

        public DataCellPIFactory() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        EndOfDayController.DataRoomStatus data = (EndOfDayController.DataRoomStatus) this.getTableRow().getItem();
                        if (data != null) {
                            if (data.getDataPreviousRRTD() != null) {
                                setText(getArrivalDate(data.getDataPreviousRRTD()));
                                if (data.getDataPreviousRRTD().getTblReservationCheckInOut() != null
                                        && data.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2) {   //Ready to Check Out = '2'
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even");
                                        getStyleClass().add("cell-input-even-error");
                                    } else {
                                        getStyleClass().remove("cell-input-odd");
                                        getStyleClass().add("cell-input-odd-error");
                                    }
                                    setStyle("-fx-background-color:#ffcdd2");
                                } else {
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even-error");
                                        getStyleClass().add("cell-input-even");
                                        setStyle("-fx-background-color:#dcedc8");

                                    } else {
                                        getStyleClass().remove("cell-input-odd-error");
                                        getStyleClass().add("cell-input-odd");
                                        setStyle("-fx-background-color:#f1f8e9");
                                    }
                                }
                            } else {
                                setText("");
                                if (this.getTableRow().getIndex() % 2 == 0) {
                                    getStyleClass().remove("cell-input-even-error");
                                    getStyleClass().add("cell-input-even");
                                    setStyle("-fx-background-color:#dcedc8");

                                } else {
                                    getStyleClass().remove("cell-input-odd-error");
                                    getStyleClass().add("cell-input-odd");
                                    setStyle("-fx-background-color:#f1f8e9");
                                }
                            }
                        } else {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().remove("cell-input-even-error");
                                getStyleClass().add("cell-input-even");
                                setStyle("-fx-background-color:#dcedc8");
                            } else {
                                getStyleClass().remove("cell-input-odd-error");
                                getStyleClass().add("cell-input-odd");
                                setStyle("-fx-background-color:#f1f8e9");
                            }
                        }
                    } else {
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().remove("cell-input-even-error");
                            getStyleClass().add("cell-input-even");
                            setStyle("-fx-background-color:#dcedc8");
                        } else {
                            getStyleClass().remove("cell-input-odd-error");
                            getStyleClass().add("cell-input-odd");
                            setStyle("-fx-background-color:#f1f8e9");
                        }
                    }
                }
            }
        }

    }

    class DataCellPOFactory extends TableCell<EndOfDayController.DataRoomStatus, String> {

        public DataCellPOFactory() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        EndOfDayController.DataRoomStatus data = (EndOfDayController.DataRoomStatus) this.getTableRow().getItem();
                        if (data != null) {
                            if (data.getDataPreviousRRTD() != null) {
                                setText(getDepartureDate(data.getDataPreviousRRTD()));
                                if (data.getDataPreviousRRTD().getTblReservationCheckInOut() != null
                                        && data.getDataPreviousRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2) {   //Ready to Check Out = '2'
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even");
                                        getStyleClass().add("cell-input-even-error");
                                    } else {
                                        getStyleClass().remove("cell-input-odd");
                                        getStyleClass().add("cell-input-odd-error");
                                    }
                                    setStyle("-fx-background-color:#ffcdd2");
                                } else {
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even-error");
                                        getStyleClass().add("cell-input-even");
                                        setStyle("-fx-background-color:#dcedc8");

                                    } else {
                                        getStyleClass().remove("cell-input-odd-error");
                                        getStyleClass().add("cell-input-odd");
                                        setStyle("-fx-background-color:#f1f8e9");
                                    }
                                }
                            } else {
                                setText("");
                                if (this.getTableRow().getIndex() % 2 == 0) {
                                    getStyleClass().remove("cell-input-even-error");
                                    getStyleClass().add("cell-input-even");
                                    setStyle("-fx-background-color:#dcedc8");

                                } else {
                                    getStyleClass().remove("cell-input-odd-error");
                                    getStyleClass().add("cell-input-odd");
                                    setStyle("-fx-background-color:#f1f8e9");
                                }
                            }
                        } else {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().remove("cell-input-even-error");
                                getStyleClass().add("cell-input-even");
                                setStyle("-fx-background-color:#dcedc8");
                            } else {
                                getStyleClass().remove("cell-input-odd-error");
                                getStyleClass().add("cell-input-odd");
                                setStyle("-fx-background-color:#f1f8e9");
                            }
                        }
                    } else {
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().remove("cell-input-even-error");
                            getStyleClass().add("cell-input-even");
                            setStyle("-fx-background-color:#dcedc8");
                        } else {
                            getStyleClass().remove("cell-input-odd-error");
                            getStyleClass().add("cell-input-odd");
                            setStyle("-fx-background-color:#f1f8e9");
                        }
                    }
                }
            }
        }

    }

    class DataCellNCFactory extends TableCell<EndOfDayController.DataRoomStatus, String> {

        public DataCellNCFactory() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        EndOfDayController.DataRoomStatus data = (EndOfDayController.DataRoomStatus) this.getTableRow().getItem();
                        if (data != null) {
                            if (data.getDataNextRRTD() != null) {
                                setText(getStatusAndCustomerName(data.getDataNextRRTD()));
                                if (data.getDataNextRRTD().getTblReservationCheckInOut() != null
                                        && data.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {   //Ready to Check In = '0'
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even");
                                        getStyleClass().add("cell-input-even-error");
                                    } else {
                                        getStyleClass().remove("cell-input-odd");
                                        getStyleClass().add("cell-input-odd-error");
                                    }
                                    setStyle("-fx-background-color:#ffcdd2");
                                } else {
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even-error");
                                        getStyleClass().add("cell-input-even");
                                        setStyle("-fx-background-color:#dcedc8");

                                    } else {
                                        getStyleClass().remove("cell-input-odd-error");
                                        getStyleClass().add("cell-input-odd");
                                        setStyle("-fx-background-color:#f1f8e9");
                                    }
                                }
                            } else {
                                setText("");
                                if (this.getTableRow().getIndex() % 2 == 0) {
                                    getStyleClass().remove("cell-input-even-error");
                                    getStyleClass().add("cell-input-even");
                                    setStyle("-fx-background-color:#dcedc8");

                                } else {
                                    getStyleClass().remove("cell-input-odd-error");
                                    getStyleClass().add("cell-input-odd");
                                    setStyle("-fx-background-color:#f1f8e9");
                                }
                            }
                        } else {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().remove("cell-input-even-error");
                                getStyleClass().add("cell-input-even");
                                setStyle("-fx-background-color:#dcedc8");
                            } else {
                                getStyleClass().remove("cell-input-odd-error");
                                getStyleClass().add("cell-input-odd");
                                setStyle("-fx-background-color:#f1f8e9");
                            }
                        }
                    } else {
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().remove("cell-input-even-error");
                            getStyleClass().add("cell-input-even");
                            setStyle("-fx-background-color:#dcedc8");
                        } else {
                            getStyleClass().remove("cell-input-odd-error");
                            getStyleClass().add("cell-input-odd");
                            setStyle("-fx-background-color:#f1f8e9");
                        }
                    }
                }
            }
        }

    }

    class DataCellNIFactory extends TableCell<EndOfDayController.DataRoomStatus, String> {

        public DataCellNIFactory() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        EndOfDayController.DataRoomStatus data = (EndOfDayController.DataRoomStatus) this.getTableRow().getItem();
                        if (data != null) {
                            if (data.getDataNextRRTD() != null) {
                                setText(getArrivalDate(data.getDataNextRRTD()));
                                if (data.getDataNextRRTD().getTblReservationCheckInOut() != null
                                        && data.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {   //Ready to Check In = '0'
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even");
                                        getStyleClass().add("cell-input-even-error");
                                    } else {
                                        getStyleClass().remove("cell-input-odd");
                                        getStyleClass().add("cell-input-odd-error");
                                    }
                                    setStyle("-fx-background-color:#ffcdd2");
                                } else {
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even-error");
                                        getStyleClass().add("cell-input-even");
                                        setStyle("-fx-background-color:#dcedc8");

                                    } else {
                                        getStyleClass().remove("cell-input-odd-error");
                                        getStyleClass().add("cell-input-odd");
                                        setStyle("-fx-background-color:#f1f8e9");
                                    }
                                }
                            } else {
                                setText("");
                                if (this.getTableRow().getIndex() % 2 == 0) {
                                    getStyleClass().remove("cell-input-even-error");
                                    getStyleClass().add("cell-input-even");
                                    setStyle("-fx-background-color:#dcedc8");

                                } else {
                                    getStyleClass().remove("cell-input-odd-error");
                                    getStyleClass().add("cell-input-odd");
                                    setStyle("-fx-background-color:#f1f8e9");
                                }
                            }
                        } else {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().remove("cell-input-even-error");
                                getStyleClass().add("cell-input-even");
                                setStyle("-fx-background-color:#dcedc8");
                            } else {
                                getStyleClass().remove("cell-input-odd-error");
                                getStyleClass().add("cell-input-odd");
                                setStyle("-fx-background-color:#f1f8e9");
                            }
                        }
                    } else {
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().remove("cell-input-even-error");
                            getStyleClass().add("cell-input-even");
                            setStyle("-fx-background-color:#dcedc8");
                        } else {
                            getStyleClass().remove("cell-input-odd-error");
                            getStyleClass().add("cell-input-odd");
                            setStyle("-fx-background-color:#f1f8e9");
                        }
                    }
                }
            }
        }

    }

    class DataCellNOFactory extends TableCell<EndOfDayController.DataRoomStatus, String> {

        public DataCellNOFactory() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        EndOfDayController.DataRoomStatus data = (EndOfDayController.DataRoomStatus) this.getTableRow().getItem();
                        if (data != null) {
                            if (data.getDataNextRRTD() != null) {
                                setText(getDepartureDate(data.getDataNextRRTD()));
                                if (data.getDataNextRRTD().getTblReservationCheckInOut() != null
                                        && data.getDataNextRRTD().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {   //Ready to Check In = '0'
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even");
                                        getStyleClass().add("cell-input-even-error");
                                    } else {
                                        getStyleClass().remove("cell-input-odd");
                                        getStyleClass().add("cell-input-odd-error");
                                    }
                                    setStyle("-fx-background-color:#ffcdd2");
                                } else {
                                    if (this.getTableRow().getIndex() % 2 == 0) {
                                        getStyleClass().remove("cell-input-even-error");
                                        getStyleClass().add("cell-input-even");
                                        setStyle("-fx-background-color:#dcedc8");

                                    } else {
                                        getStyleClass().remove("cell-input-odd-error");
                                        getStyleClass().add("cell-input-odd");
                                        setStyle("-fx-background-color:#f1f8e9");
                                    }
                                }
                            } else {
                                setText("");
                                if (this.getTableRow().getIndex() % 2 == 0) {
                                    getStyleClass().remove("cell-input-even-error");
                                    getStyleClass().add("cell-input-even");
                                    setStyle("-fx-background-color:#dcedc8");

                                } else {
                                    getStyleClass().remove("cell-input-odd-error");
                                    getStyleClass().add("cell-input-odd");
                                    setStyle("-fx-background-color:#f1f8e9");
                                }
                            }
                        } else {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().remove("cell-input-even-error");
                                getStyleClass().add("cell-input-even");
                                setStyle("-fx-background-color:#dcedc8");
                            } else {
                                getStyleClass().remove("cell-input-odd-error");
                                getStyleClass().add("cell-input-odd");
                                setStyle("-fx-background-color:#f1f8e9");
                            }
                        }
                    } else {
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().remove("cell-input-even-error");
                            getStyleClass().add("cell-input-even");
                            setStyle("-fx-background-color:#dcedc8");
                        } else {
                            getStyleClass().remove("cell-input-odd-error");
                            getStyleClass().add("cell-input-odd");
                            setStyle("-fx-background-color:#f1f8e9");
                        }
                    }
                }
            }
        }

    }

    public void refreshDataTable() {
        tableData.setItems(FXCollections.observableArrayList(parentController.dataRoomWithStatuss));
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

    public EODL2Controller(EndOfDayController parentController) {
        this.parentController = parentController;
    }

    private final EndOfDayController parentController;

    public FEndOfDayManager getService() {
        return parentController.getService();
    }

}
