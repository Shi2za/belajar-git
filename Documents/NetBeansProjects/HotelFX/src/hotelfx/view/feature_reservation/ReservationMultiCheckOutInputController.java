/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationMultiCheckOutInputController implements Initializable {

    @FXML
    private AnchorPane ancFormMultiCheckOut;

    @FXML
    private GridPane gpFormDataMultiCheckOutCardInfo;

    @FXML
    private JFXTextField txtUsedCardNumber;
    
    @FXML
    private JFXTextField txtReturnCardNumber;

    @FXML
    private JFXTextField txtMissedCardNumber;

    @FXML
    private JFXTextField txtTotalReturnDeposit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataMultiCheckOut() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Multi-Check Out)"));
        btnSave.setOnAction((e) -> {
            dataCheckOutSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCheckOutCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {

    }

    private void setSelectedDataToInputForm() {

        iniTabletDataReservationCheckInOutDetaiil();

        refreshDataSummary();

    }

    @FXML
    private AnchorPane ancTableLayout;

    public TableView tableCIODetail;

    private void iniTabletDataReservationCheckInOutDetaiil() {
        ancTableLayout.getChildren().clear();

        tableCIODetail = new TableView();
        tableCIODetail.setEditable(true);

        TableColumn<TblReservationRoomTypeDetail, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut().getTblRoom().getRoomName(),
                        param.getValue().getTblReservationCheckInOut().tblRoomProperty()));
        roomName.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> usedCardNumber = new TableColumn("Jumlah Kartu\n(Digunakan)");
        usedCardNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> String.valueOf(param.getValue().getTblReservationCheckInOut().getCardUsed()),
                        param.getValue().getTblReservationCheckInOut().cardUsedProperty()));
        usedCardNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> totalCardDeposit = new TableColumn("Total Deposit\n(Kartu)");
        totalCardDeposit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationDeposit(param.getValue().getTblReservationCheckInOut())),
                        param.getValue().getTblReservationCheckInOut().cardUsedProperty()));
        totalCardDeposit.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> returnCardNumber = new TableColumn("Jumlah Kartu\n(Dikembalikan)");
        returnCardNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> String.valueOf(param.getValue().getTblReservationCheckInOut().getCardUsed() - param.getValue().getTblReservationCheckInOut().getCardMissed()),
                        param.getValue().getTblReservationCheckInOut().cardMissedProperty()));
        returnCardNumber.setMinWidth(100);
        Callback<TableColumn<TblReservationRoomTypeDetail, String>, TableCell<TblReservationRoomTypeDetail, String>> cellFactory
                = new Callback<TableColumn<TblReservationRoomTypeDetail, String>, TableCell<TblReservationRoomTypeDetail, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCellReturnCardNumber(0, 100);
            }
        };
        returnCardNumber.setCellFactory(cellFactory);
        returnCardNumber.setEditable(true);
        
        TableColumn<TblReservationRoomTypeDetail, String> missedCardNumber = new TableColumn("Jumlah Kartu\n(Rusak)");
        missedCardNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> String.valueOf(param.getValue().getTblReservationCheckInOut().getCardMissed()),
                        param.getValue().getTblReservationCheckInOut().cardMissedProperty()));
        missedCardNumber.setMinWidth(100);
//        Callback<TableColumn<TblReservationRoomTypeDetail, String>, TableCell<TblReservationRoomTypeDetail, String>> cellFactory
//                = new Callback<TableColumn<TblReservationRoomTypeDetail, String>, TableCell<TblReservationRoomTypeDetail, String>>() {
//                    @Override
//                    public TableCell call(TableColumn p) {
//                        return new EditingCellMissedCardNumber(0, 100);
//                    }
//                };
//        missedCardNumber.setCellFactory(cellFactory);
//        missedCardNumber.setEditable(true);

        TableColumn<TblReservationRoomTypeDetail, String> brokenCharge = new TableColumn("Biaya Kerusakan");
        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge()),
                        param.getValue().getTblReservationCheckInOut().brokenCardChargeProperty()));
        brokenCharge.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> totalBrokenChargeNominal = new TableColumn("Total Biaya\n(Kartu Rusak)");
        totalBrokenChargeNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationBrokenCharge(param.getValue().getTblReservationCheckInOut())),
                        param.getValue().getTblReservationCheckInOut().cardMissedProperty()));
        totalBrokenChargeNominal.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> totalDepositMustBeReturnNominal = new TableColumn("Total Deposit\n(Dikembalikan)");
        totalDepositMustBeReturnNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationDepositMustBeReturn(param.getValue().getTblReservationCheckInOut())),
                        param.getValue().getTblReservationCheckInOut().cardMissedProperty()));
        totalDepositMustBeReturnNominal.setMinWidth(120);
        
        tableCIODetail.getColumns().addAll(roomName, usedCardNumber, totalCardDeposit, returnCardNumber, missedCardNumber, brokenCharge, totalBrokenChargeNominal, totalDepositMustBeReturnNominal);

        tableCIODetail.setItems(FXCollections.observableArrayList(reservationController.selectedDataRoomTypeDetails));
        AnchorPane.setBottomAnchor(tableCIODetail, 0.0);
        AnchorPane.setLeftAnchor(tableCIODetail, 0.0);
        AnchorPane.setRightAnchor(tableCIODetail, 0.0);
        AnchorPane.setTopAnchor(tableCIODetail, 0.0);
        ancTableLayout.getChildren().add(tableCIODetail);

    }

    private boolean isListenerRunning = false;

    class EditingCellMissedCardNumber extends TableCell<TblReservationRoomTypeDetail, String> {

        private final int min;
        private final int max;

        private Spinner<Integer> spRoomNumber;

        public EditingCellMissedCardNumber(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null) {
                super.startEdit();
                spRoomNumber = new Spinner<>();
                spRoomNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));

                ClassViewSetting.setImportantField(
                        spRoomNumber);

                spRoomNumber.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                spRoomNumber.getValueFactory().setValue(((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().getCardMissed());
                spRoomNumber.getValueFactory().valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (!isListenerRunning) {
                        isListenerRunning = true;
                        if (((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().getCardUsed()
                                - newVal < 0) {
                            spRoomNumber.getValueFactory().setValue(oldVal);
                        } else {
                            ((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().setCardMissed(spRoomNumber.getValueFactory().getValue());
                        }
                        refreshDataSummary();
                        isListenerRunning = false;
                    }
                });

                setText(null);
                setGraphic(spRoomNumber);

                //cell input color
                if (this.getTableRow() != null) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even");
                        getStyleClass().add("cell-input-even-edit");
                    } else {
                        getStyleClass().remove("cell-input-odd");
                        getStyleClass().add("cell-input-odd-edit");
                    }
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            spRoomNumber = null;

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit");
                    getStyleClass().add("cell-input-even");
                } else {
                    getStyleClass().remove("cell-input-odd-edit");
                    getStyleClass().add("cell-input-odd");
                }
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            setText(String.valueOf(((TblReservationRoomTypeDetail) data).getTblReservationCheckInOut().getCardMissed()));
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().add("cell-input-even");

                        } else {
                            getStyleClass().add("cell-input-odd");
                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
        }

    }

    class EditingCellReturnCardNumber extends TableCell<TblReservationRoomTypeDetail, String> {

        private final int min;
        private final int max;

        private Spinner<Integer> spRoomNumber;

        public EditingCellReturnCardNumber(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null) {
                super.startEdit();
                spRoomNumber = new Spinner<>();
                spRoomNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));

                ClassViewSetting.setImportantField(
                        spRoomNumber);

                spRoomNumber.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                spRoomNumber.getValueFactory().setValue(((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().getCardUsed()
                        - ((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().getCardMissed());
                spRoomNumber.getValueFactory().valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (!isListenerRunning) {
                        isListenerRunning = true;
                        if (((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().getCardUsed()
                                - newVal < 0) {
                            spRoomNumber.getValueFactory().setValue(oldVal);
                        } else {
                            ((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().setCardMissed(
                                    ((TblReservationRoomTypeDetail) this.getTableRow().getItem()).getTblReservationCheckInOut().getCardUsed()
                                    - newVal);
                        }
                        refreshDataSummary();
                        isListenerRunning = false;
                    }
                });

                setText(null);
                setGraphic(spRoomNumber);

                //cell input color
                if (this.getTableRow() != null) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even");
                        getStyleClass().add("cell-input-even-edit");
                    } else {
                        getStyleClass().remove("cell-input-odd");
                        getStyleClass().add("cell-input-odd-edit");
                    }
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            spRoomNumber = null;

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit");
                    getStyleClass().add("cell-input-even");
                } else {
                    getStyleClass().remove("cell-input-odd-edit");
                    getStyleClass().add("cell-input-odd");
                }
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            setText(String.valueOf(((TblReservationRoomTypeDetail) data).getTblReservationCheckInOut().getCardUsed()
                                    - ((TblReservationRoomTypeDetail) data).getTblReservationCheckInOut().getCardMissed()));
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().add("cell-input-even");

                        } else {
                            getStyleClass().add("cell-input-odd");
                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
        }

    }

    private BigDecimal calculationDeposit(TblReservationCheckInOut dataReservationCheckInOut) {
        BigDecimal result = new BigDecimal("0");
        if (dataReservationCheckInOut != null) {
            result = (new BigDecimal(dataReservationCheckInOut.getCardUsed()))
                    .multiply(dataReservationCheckInOut.getBrokenCardCharge());
        }
        return result;
    }

    private BigDecimal calculationBrokenCharge(TblReservationCheckInOut dataReservationCheckInOut) {
        BigDecimal result = new BigDecimal("0");
        if (dataReservationCheckInOut != null) {
            result = (new BigDecimal(dataReservationCheckInOut.getCardMissed()))
                    .multiply(dataReservationCheckInOut.getBrokenCardCharge());
        }
        return result;
    }

    private BigDecimal calculationDepositMustBeReturn(TblReservationCheckInOut dataReservationCheckInOut){
        BigDecimal result = new BigDecimal("0");
        if (dataReservationCheckInOut != null) {
            result = (new BigDecimal((dataReservationCheckInOut.getCardUsed() - dataReservationCheckInOut.getCardMissed())))
                    .multiply(dataReservationCheckInOut.getBrokenCardCharge());
        }
        return result;
    }
    
    private int calculationTotalUsedCardNumber(){
        int result = 0;
        for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail : reservationController.selectedDataRoomTypeDetails) {
            result += selectedDataRoomTypeDetail.getTblReservationCheckInOut().getCardUsed();
        }
        return result;
    }
    
    private int calculationTotalReturnCardNumber() {
        int result = 0;
        for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail : reservationController.selectedDataRoomTypeDetails) {
            result += selectedDataRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                    - selectedDataRoomTypeDetail.getTblReservationCheckInOut().getCardMissed();
        }
        return result;
    }

    private int calculationTotalMissedCardNumber() {
        int result = 0;
        for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail : reservationController.selectedDataRoomTypeDetails) {
            result += selectedDataRoomTypeDetail.getTblReservationCheckInOut().getCardMissed();
        }
        return result;
    }

    private BigDecimal calculationTotalReturnDeposit() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail : reservationController.selectedDataRoomTypeDetails) {
            result = result.add((new BigDecimal(selectedDataRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                    - selectedDataRoomTypeDetail.getTblReservationCheckInOut().getCardMissed()))
                    .multiply(selectedDataRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
        }
        return result;
    }

    private void refreshDataSummary() {
        txtUsedCardNumber.setText(String.valueOf(calculationTotalUsedCardNumber()));
        txtReturnCardNumber.setText(String.valueOf(calculationTotalReturnCardNumber()));
        txtMissedCardNumber.setText(String.valueOf(calculationTotalMissedCardNumber()));
        txtTotalReturnDeposit.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReturnDeposit()));
    }

    private void dataCheckOutSaveHandle() {
        if (checkDataInputDataCheckOut()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //save and set data checkout
                for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail : reservationController.selectedDataRoomTypeDetails) {
                    selectedDataRoomTypeDetail.getTblReservationCheckInOut().setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.now()));
                    selectedDataRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(reservationController.getFReservationManager().getReservationCheckInOutStatus(3));
                }

                //update data bill
                reservationController.refreshDataBill(reservationController.selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                        ? "reservation" : "checkout");
                //save data to database
                if (reservationController.dataReservationSaveHandle(17)) {
                    //close form data checkout input
                    reservationController.dialogStage.close();
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCheckOutCancelHandle() {
        //selected data details
        reservationController.selectedDataRoomTypeDetails = new ArrayList<>();
        //refresh data
        reservationController.refreshDataSelectedReservation();
        //close form data checkout
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCheckOut() {
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
        initFormDataMultiCheckOut();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationMultiCheckOutInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

}
