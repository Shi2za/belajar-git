/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank_edc_settlement;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefEdctransactionStatus;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.service.FBankManager;
import hotelfx.persistence.service.FBankManagerImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class EDCSettlementController implements Initializable {

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataEDC;

    @FXML
    private AnchorPane ancEDC;
    private JFXCComboBoxTablePopup<TblBankEdc> cbpEDC;

    @FXML
    private JFXTextField txtEDCNumber;

    @FXML
    private JFXTextField txtBankName;

    @FXML
    private JFXButton btnSettle;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private void initFormDataEDCSettlement() {
//        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
//            //@@@
//            spFormDataEDCSettlement.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
//        });
//
//        //@@@
//        gpFormDataEDC.setOnScroll((ScrollEvent scroll) -> {
//            isFormScroll.set(true);
//
//            scrollCounter++;
//
//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    Platform.runLater(() -> {
//                        if (scrollCounter == 1) {
//                            //scroll end..!
//                            isFormScroll.set(false);
//                        }
//                        scrollCounter--;
//                    });
//                } catch (Exception e) {
//                    System.out.println("err " + e.getMessage());
//                }
//
//            });
//            thread.setDaemon(true);
//            thread.start();
//        });
        initDataPopup();

        initTableDataDetail(null);

        btnSettle.setTooltip(new Tooltip("Settle (Data Transaksi EDC)"));
        btnSettle.setOnAction((e) -> {
            dataEDCSettleSaveHandle();
        });

        cbpEDC.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtEDCNumber.setText(newVal.getEdcnumber());
                txtBankName.setText(newVal.getTblBank().getBankName());
            } else {
                txtEDCNumber.setText("");
                txtBankName.setText("");
            }
            //set table detail
            initTableDataDetail(newVal);
            //set unsaving data input -> 'false'
            ClassSession.unSavingDataInput.set(false);
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpEDC);
    }

    private void initDataPopup() {
        //EDC
        TableView<TblBankEdc> tableEDC = new TableView<>();

        TableColumn<TblBankEdc, String> edcNumber = new TableColumn("No. EDC");
        edcNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdc, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getEdcnumber(),
                        param.getValue().edcnumberProperty()));
        edcNumber.setMinWidth(120);

        TableColumn<TblBankEdc, String> edcName = new TableColumn("EDC");
        edcName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdc, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getEdcname(),
                        param.getValue().edcnameProperty()));
        edcName.setMinWidth(140);

        TableColumn<TblBankEdc, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdc, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(),
                        param.getValue().tblBankProperty()));
        bankName.setMinWidth(140);

        tableEDC.getColumns().addAll(edcName, bankName, edcNumber);

        ObservableList<TblBankEdc> edcItems = FXCollections.observableArrayList(loadAllDataEDC());

        cbpEDC = new JFXCComboBoxTablePopup<>(
                TblBankEdc.class, tableEDC, edcItems, "", "No. EDC *", true, 420, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpEDC, 0.0);
        AnchorPane.setLeftAnchor(cbpEDC, 0.0);
        AnchorPane.setRightAnchor(cbpEDC, 0.0);
        AnchorPane.setTopAnchor(cbpEDC, 0.0);
        ancEDC.getChildren().clear();
        ancEDC.getChildren().add(cbpEDC);
    }

    private List<TblBankEdc> loadAllDataEDC() {
        List<TblBankEdc> list = getService().getAllDataBankEDC();
        for (TblBankEdc data : list) {
            //data bank
            data.setTblBank(getService().getBank(data.getTblBank().getIdbank()));
        }
        return list;
    }

    private void refreshDataPopup() {
        //EDC
        ObservableList<TblBankEdc> edcItems = FXCollections.observableArrayList(loadAllDataEDC());
        cbpEDC.setItems(edcItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        cbpEDC.setValue(null);
        cbpEDC.hide();

        //set data table detail
        initTableDataDetail(null);
    }

    private void dataEDCSettleSaveHandle() {
        if (checkDataInputDataEDCSettlement()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //data reservation payment with bank card (will be updated)
                List<TblReservationPaymentWithBankCard> list = new ArrayList<>();
                for (ReservationPaymentWithBankCardSelected data : (List<ReservationPaymentWithBankCardSelected>) tableDataDetail.getItems()) {
                    if (data.getSelected()
                            && data.getDataRPWBC().getRefEdctransactionStatus().getIdstatus() != 2) {   //CardVer = '2'
                        list.add(data.getDataRPWBC());
                    }
                }
                //dummy entry
                TblBankEdc dummySelectedData = new TblBankEdc(cbpEDC.getValue());
                List<TblReservationPaymentWithBankCard> dummyDataEDCTransactionDetails = new ArrayList<>();
                for (TblReservationPaymentWithBankCard data : list) {
                    TblReservationPaymentWithBankCard dummyDataEDCTransactionDetail = new TblReservationPaymentWithBankCard(data);
                    dummyDataEDCTransactionDetail.setTblReservationPayment(new TblReservationPayment(dummyDataEDCTransactionDetail.getTblReservationPayment()));
                    dummyDataEDCTransactionDetail.setTblBankEdc(dummySelectedData);
                    dummyDataEDCTransactionDetail.setRefEdctransactionStatus(new RefEdctransactionStatus(dummyDataEDCTransactionDetail.getRefEdctransactionStatus()));
                    dummyDataEDCTransactionDetails.add(dummyDataEDCTransactionDetail);
                }
                if (getService().updateSettleEDCDataReservationPaymentWithBankCard(
                        dummyDataEDCTransactionDetails)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh selected data and data from table detail
                    initTableDataDetail(cbpEDC.getValue());
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    public void refreshDataTableEDCSettlement() {
        cbpEDC.setValue(null);
    }

    private String errDataInput;

    private boolean checkDataInputDataEDCSettlement() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpEDC.getValue() == null) {
            dataInput = false;
            errDataInput += "EDC : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (tableDataDetail.getItems().isEmpty()) {
            dataInput = false;
            errDataInput += "Tabel (Detail-Transaksi) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (!checkDataTransaction(tableDataDetail.getItems())) {
                dataInput = false;
                errDataInput += "Tidak ada data transaksi yang harus di-settle, atau terdapat kesalahan pada data transaksi..!\n";
            }
        }
        return dataInput;
    }

    public boolean checkDataTransaction(List<ReservationPaymentWithBankCardSelected> rpwbcs) {
        int count = 0;
        for (ReservationPaymentWithBankCardSelected rpwbc : rpwbcs) {
            if (rpwbc.getDataRPWBC().getRefEdctransactionStatus() == null) {
                return false;
            } else {
                if (rpwbc.getSelected()
                        && rpwbc.getDataRPWBC().getRefEdctransactionStatus().getIdstatus() != 2) {   //CardVer = '2'
                    count++;
                }
            }
        }
        return count > 0;
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDetailLayout;

    public TableView<ReservationPaymentWithBankCardSelected> tableDataDetail;

    private void initTableDataDetail(TblBankEdc dataEDC) {
        //set table
        setTableDataDetail(dataEDC);
        //set table-control to content-view
        ancDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail(TblBankEdc dataEDC) {
        tableDataDetail = new TableView();
        tableDataDetail.setEditable(true);

        TableColumn<ReservationPaymentWithBankCardSelected, String> transactionDate = new TableColumn("Tgl. Transaksi");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<ReservationPaymentWithBankCardSelected, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDataRPWBC().getTblReservationPayment().getPaymentDate()),
                        param.getValue().getDataRPWBC().tblReservationPaymentProperty()));
        transactionDate.setMinWidth(140);

        TableColumn<ReservationPaymentWithBankCardSelected, String> transactionNominal = new TableColumn("Transaksi");
        transactionNominal.setCellValueFactory((TableColumn.CellDataFeatures<ReservationPaymentWithBankCardSelected, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataRPWBC().getTblReservationPayment().getUnitNominal()),
                        param.getValue().getDataRPWBC().tblReservationPaymentProperty()));
        transactionNominal.setMinWidth(120);

        TableColumn<ReservationPaymentWithBankCardSelected, String> mdrNominal = new TableColumn("MDR");
        mdrNominal.setCellValueFactory((TableColumn.CellDataFeatures<ReservationPaymentWithBankCardSelected, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataRPWBC().getPaymentCharge()),
                        param.getValue().getDataRPWBC().paymentChargeProperty()));
        mdrNominal.setMinWidth(120);

        TableColumn<ReservationPaymentWithBankCardSelected, String> nominalTitle = new TableColumn("Nominal");
        nominalTitle.getColumns().addAll(transactionNominal, mdrNominal);

        TableColumn<ReservationPaymentWithBankCardSelected, String> cardNumber = new TableColumn("No. Kartu");
        cardNumber.setCellValueFactory((TableColumn.CellDataFeatures<ReservationPaymentWithBankCardSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRPWBC().getBankCardNumber(),
                        param.getValue().getDataRPWBC().bankCardNumberProperty()));
        cardNumber.setMinWidth(140);

        TableColumn<ReservationPaymentWithBankCardSelected, String> appCode = new TableColumn("App. Code");
        appCode.setCellValueFactory((TableColumn.CellDataFeatures<ReservationPaymentWithBankCardSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataRPWBC().getApprovalCode(),
                        param.getValue().getDataRPWBC().approvalCodeProperty()));
        appCode.setMinWidth(100);

        TableColumn<ReservationPaymentWithBankCardSelected, String> status = new TableColumn("Status");
        status.setMinWidth(120);
        status.setCellValueFactory((TableColumn.CellDataFeatures<ReservationPaymentWithBankCardSelected, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getDataRPWBC().getRefEdctransactionStatus() == null
                                ? ""
                                : param.getValue().getDataRPWBC().getRefEdctransactionStatus().getStatusName(),
                        param.getValue().getDataRPWBC().refEdctransactionStatusProperty()));
        Callback<TableColumn<ReservationPaymentWithBankCardSelected, String>, TableCell<ReservationPaymentWithBankCardSelected, String>> cellFactory
                = new Callback<TableColumn<ReservationPaymentWithBankCardSelected, String>, TableCell<ReservationPaymentWithBankCardSelected, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellStatus();
                    }
                };
        status.setCellFactory(cellFactory);
        status.setEditable(true);

        TableColumn<ReservationPaymentWithBankCardSelected, Boolean> settle = new TableColumn("Settle");
        settle.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        settle.setCellFactory(CheckBoxTableCell.forTableColumn(settle));
        settle.setMinWidth(70);
        settle.setEditable(true);

        tableDataDetail.getColumns().addAll(appCode, transactionDate, nominalTitle, cardNumber, status, settle);

        tableDataDetail.setItems(FXCollections.observableArrayList(loadAllDataReservationPaymentWithBankCardSelected(dataEDC)));
    }

    private List<ReservationPaymentWithBankCardSelected> loadAllDataReservationPaymentWithBankCardSelected(TblBankEdc dataEDC) {
        List<ReservationPaymentWithBankCardSelected> list = new ArrayList<>();
        if (dataEDC != null) {
            List<TblReservationPaymentWithBankCard> rpwbcs = getService().getAllDataReservationPaymentWithBankCardByIDEDC(dataEDC.getIdedc());
            for (TblReservationPaymentWithBankCard rpwbc : rpwbcs) {
                //data reservation payment
                rpwbc.setTblReservationPayment(getService().getDataReservationPayment(rpwbc.getTblReservationPayment().getIdpayment()));
                if (rpwbc.getSettleDate() == null) {
                    //data bank
                    rpwbc.setTblBank(getService().getBank(rpwbc.getTblBank().getIdbank()));
                    //data edc
                    rpwbc.setTblBankEdc(getService().getBankEDC(rpwbc.getTblBankEdc().getIdedc()));
                    //data network
                    rpwbc.setTblBankNetworkCard(getService().getBankNetworkCard(rpwbc.getTblBankNetworkCard().getIdnetworkCard()));
                    //data mdr (?)
                    //data edc transaction status
                    rpwbc.setRefEdctransactionStatus(getService().getDataEDCTransactionStatus(rpwbc.getRefEdctransactionStatus().getIdstatus()));
                    //add to list
                    list.add(new ReservationPaymentWithBankCardSelected(rpwbc, false));
                }
            }
        }
        return list;
    }

    class EditingCellStatus extends TableCell<ReservationPaymentWithBankCardSelected, String> {

        private JFXCComboBoxTablePopup<RefEdctransactionStatus> cbpEDCTransactionStatus;

        public EditingCellStatus() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                cbpEDCTransactionStatus = getComboBoxEDCTransactionStatus();

                ClassViewSetting.setImportantField(
                        cbpEDCTransactionStatus);

                cbpEDCTransactionStatus.hide();

                cbpEDCTransactionStatus.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                cbpEDCTransactionStatus.valueProperty().bindBidirectional(((ReservationPaymentWithBankCardSelected) this.getTableRow().getItem()).getDataRPWBC().refEdctransactionStatusProperty());

                setText(null);
                setGraphic(cbpEDCTransactionStatus);
                cbpEDCTransactionStatus.getEditor().selectAll();

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

            cbpEDCTransactionStatus.valueProperty().unbindBidirectional(((ReservationPaymentWithBankCardSelected) this.getTableRow().getItem()).getDataRPWBC().refEdctransactionStatusProperty());

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
                            if (((ReservationPaymentWithBankCardSelected) data).getDataRPWBC().getRefEdctransactionStatus() != null) {
                                setText(((ReservationPaymentWithBankCardSelected) data).getDataRPWBC().getRefEdctransactionStatus().getStatusName());
                            } else {
                                setText("");
                            }
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

    private JFXCComboBoxTablePopup<RefEdctransactionStatus> getComboBoxEDCTransactionStatus() {
        TableView<RefEdctransactionStatus> tableStatus = new TableView<>();

        TableColumn<RefEdctransactionStatus, String> statusName = new TableColumn<>("Status");
        statusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        statusName.setMinWidth(140);

        tableStatus.getColumns().addAll(statusName);

        ObservableList<RefEdctransactionStatus> statusItems = FXCollections.observableArrayList(loadAllDataEDCTransactionStatus());

        JFXCComboBoxTablePopup<RefEdctransactionStatus> cbpWarehouse = new JFXCComboBoxTablePopup<>(
                RefEdctransactionStatus.class, tableStatus, statusItems, "", " ", true, 160, 200
        );

        cbpWarehouse.setLabelFloat(false);

        return cbpWarehouse;
    }

    private List<RefEdctransactionStatus> loadAllDataEDCTransactionStatus() {
        List<RefEdctransactionStatus> list = getService().getAllDataEDCTransactionStatus();
        //...
        return list;
    }

    public class ReservationPaymentWithBankCardSelected {

        private final ObjectProperty<TblReservationPaymentWithBankCard> dataRPWBC = new SimpleObjectProperty<>();

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public ReservationPaymentWithBankCardSelected(
                TblReservationPaymentWithBankCard dataRPWBC,
                boolean selected) {
            this.dataRPWBC.set(dataRPWBC);
            this.selected.set(selected);
            this.dataRPWBC.get().refEdctransactionStatusProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null
                        && newVal.getIdstatus() == 2) {  //CardVer = '2'
                    this.selected.set(false);
                }
                //set unsaving data input -> 'true'
                ClassSession.unSavingDataInput.set(true);
            });
            this.selected.addListener((obs, oldVal, newVal) -> {
                if (this.dataRPWBC.get().getRefEdctransactionStatus() != null
                        && this.dataRPWBC.get().getRefEdctransactionStatus().getIdstatus() == 2) {  //CardVer = '2'
                    this.selected.set(false);
                }
                //set unsaving data input -> 'true'
                ClassSession.unSavingDataInput.set(true);
            });
        }

        public ObjectProperty<TblReservationPaymentWithBankCard> dataRPWBCProperty() {
            return dataRPWBC;
        }

        public TblReservationPaymentWithBankCard getDataRPWBC() {
            return dataRPWBCProperty().get();
        }

        public void setDataRPWBC(TblReservationPaymentWithBankCard dataRPWBC) {
            dataRPWBCProperty().set(dataRPWBC);
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean getSelected() {
            return selectedProperty().get();
        }

        public void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FBankManager fBankManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fBankManager = new FBankManagerImpl();

        //init form
        initFormDataEDCSettlement();

        //set selected data form
        setSelectedDataToInputForm();
    }

    public FBankManager getService() {
        return this.fBankManager;
    }

}
