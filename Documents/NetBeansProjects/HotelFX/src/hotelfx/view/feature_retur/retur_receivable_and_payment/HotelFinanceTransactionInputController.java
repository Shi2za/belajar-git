/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_receivable_and_payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefFinanceTransactionType;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblRetur;
import hotelfx.view.feature_retur.retur_receivable_and_payment.tid.TIDCashController;
import hotelfx.view.feature_retur.retur_receivable_and_payment.tid.TIDCekGiroController;
import hotelfx.view.feature_retur.retur_receivable_and_payment.tid.TIDTransferController;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
public class HotelFinanceTransactionInputController implements Initializable {

    @FXML
    private AnchorPane ancFormHFT;

    @FXML
    private GridPane gpFormDataHFT;

    @FXML
    private Label lblHotelFinanceTransaction;

    @FXML
    private AnchorPane ancPaymentType;
    public JFXCComboBoxTablePopup<RefFinanceTransactionPaymentType> cbpFinanceTransactionPaymentType;

    @FXML
    public JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataHFT() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpFinanceTransactionPaymentType);
    }

    private void initDataPopup() {
        //Finance Transaction Payment Type
        TableView<RefFinanceTransactionPaymentType> tableFinanceTransactionPaymentType = new TableView<>();

        TableColumn<RefFinanceTransactionPaymentType, String> typeName = new TableColumn<>("Tipe Pembayaran");
        typeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        typeName.setMinWidth(250);

        tableFinanceTransactionPaymentType.getColumns().addAll(typeName);

        ObservableList<RefFinanceTransactionPaymentType> ftpTypeItems = FXCollections.observableArrayList(loadAllDataFinanceTransactionPaymentType());

        cbpFinanceTransactionPaymentType = new JFXCComboBoxTablePopup<>(
                RefFinanceTransactionPaymentType.class, tableFinanceTransactionPaymentType, ftpTypeItems, "", "Tipe Pembayaran *", false, 200, 150
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setLeftAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setRightAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setTopAnchor(cbpFinanceTransactionPaymentType, 0.0);
        ancPaymentType.getChildren().clear();
        ancPaymentType.getChildren().add(cbpFinanceTransactionPaymentType);
    }

    private List<RefFinanceTransactionPaymentType> loadAllDataFinanceTransactionPaymentType() {
        List<RefFinanceTransactionPaymentType> list = returReceivableController.getService().getAllDataFinanceTransactionPaymentType();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdtype() != 0 //Tunai = '0'
                    && list.get(i).getIdtype() != 1 //Transafer = '1'
                    && list.get(i).getIdtype() != 4 //Cek = '4'
                    && list.get(i).getIdtype() != 5) {   //Giro = '5'
                list.remove(i);
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Finance Transaction Payment Type
        ObservableList<RefFinanceTransactionPaymentType> ftpTypeItems = FXCollections.observableArrayList(loadAllDataFinanceTransactionPaymentType());
        cbpFinanceTransactionPaymentType.setItems(ftpTypeItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        cbpFinanceTransactionPaymentType.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                setTransactionTypeInputForm(newVal);
                refreshDataHotelFinanceTransaction();
            }
        });
        cbpFinanceTransactionPaymentType.valueProperty().bindBidirectional(returReceivableController.selectedDataHFT.refFinanceTransactionPaymentTypeProperty());

        cbpFinanceTransactionPaymentType.hide();

        //refresh table hotel receivable
        setDataTableHotelReceivable();

        //refresh hotel finance transaction nominal
        refreshDataHotelFinanceTransaction();
    }

    @FXML
    private AnchorPane ancTableHotelReceivableLayout;

    public TableView tableHotelReceivable;

    public void setDataTableHotelReceivable() {
        ancTableHotelReceivableLayout.getChildren().clear();

        tableHotelReceivable = new TableView();
        tableHotelReceivable.setEditable(true);

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeInvoice.setMinWidth(100);

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> codeRetur = new TableColumn("No. Retur");
        codeRetur.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> getCodeRetur(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeRetur.setMinWidth(100);

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> dueDate = new TableColumn("Tgl. Estimasi\n Bayar");
        dueDate.setMinWidth(120);
        dueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> getDueDate(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> nominalRestOfBill = new TableColumn("Sisa Tagihan");
        nominalRestOfBill.setMinWidth(120);
        nominalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(() -> getNominalRestOfBill(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));

        TableColumn<TblHotelFinanceTransactionHotelReceivable, String> nominalTransaction = new TableColumn("Nominal Transaksi\n Bayar");
        nominalTransaction.setMinWidth(120);
        nominalTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelReceivable, String> param)
                -> Bindings.createStringBinding(()
                        -> ClassFormatter.currencyFormat.format(param.getValue().getNominalTransaction() != null
                                        ? param.getValue().getNominalTransaction()
                                        : new BigDecimal("0")),
                        param.getValue().nominalTransactionProperty()));
        Callback<TableColumn<TblHotelFinanceTransactionHotelReceivable, String>, TableCell<TblHotelFinanceTransactionHotelReceivable, String>> cellFactory
                = new Callback<TableColumn<TblHotelFinanceTransactionHotelReceivable, String>, TableCell<TblHotelFinanceTransactionHotelReceivable, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellNominalTransaction();
                    }
                };
        nominalTransaction.setCellFactory(cellFactory);
        nominalTransaction.setEditable(true);

        tableHotelReceivable.getColumns().addAll(codeRetur, codeInvoice, dueDate, nominalRestOfBill, nominalTransaction);
        tableHotelReceivable.setItems(FXCollections.observableArrayList(returReceivableController.selectedDataHFTHRs));

        AnchorPane.setBottomAnchor(tableHotelReceivable, 0.0);
        AnchorPane.setLeftAnchor(tableHotelReceivable, 0.0);
        AnchorPane.setRightAnchor(tableHotelReceivable, 0.0);
        AnchorPane.setTopAnchor(tableHotelReceivable, 0.0);
        ancTableHotelReceivableLayout.getChildren().add(tableHotelReceivable);
    }

    private String getCodeRetur(TblHotelReceivable hotelReceivable) {
        String result = "-";
        if (hotelReceivable != null) {
            TblRetur retur = returReceivableController.getService().getDataReturByIDHotelReceivable(hotelReceivable.getIdhotelReceivable());
            if (retur != null) {
                result = retur.getCodeRetur();
            }
        }
        return result;
    }

    private String getCodeInvoice(TblHotelReceivable hotelReceivable) {
        String result = "-";
        if (hotelReceivable != null
                && hotelReceivable.getTblHotelInvoice() != null) {
            TblHotelInvoice invoice = returReceivableController.getService().getDataHotelInvoice(hotelReceivable.getTblHotelInvoice().getIdhotelInvoice());
            if (invoice != null) {
                result = invoice.getCodeHotelInvoice();
            }
        }
        return result;
    }

    private String getDueDate(TblHotelReceivable hotelReceivable) {
        String result = "-";
        if (hotelReceivable != null
                && hotelReceivable.getTblHotelInvoice() != null) {
            TblHotelInvoice invoice = returReceivableController.getService().getDataHotelInvoice(hotelReceivable.getTblHotelInvoice().getIdhotelInvoice());
            if (invoice != null
                    && invoice.getDueDate() != null) {
                result = ClassFormatter.dateFormate.format(invoice.getDueDate());
            }
        }
        return result;
    }

    private String getNominalRestOfBill(TblHotelReceivable hotelReceivable) {
        return ClassFormatter.currencyFormat.format(getRestOfBill(hotelReceivable));
    }

    private BigDecimal getRestOfBill(TblHotelReceivable hotelReceivable) {
        BigDecimal result = new BigDecimal("0");
        if (hotelReceivable != null) {
            result = result.add(hotelReceivable.getHotelReceivableNominal());
            //hotel finance transaction
            List<TblHotelFinanceTransactionHotelReceivable> list = returReceivableController.getService().getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(hotelReceivable.getIdhotelReceivable());
            for (TblHotelFinanceTransactionHotelReceivable data : list) {
                result = result.subtract(data.getNominalTransaction());
            }
        }
        return result;
    }

    class EditingCellNominalTransaction extends TableCell<TblHotelFinanceTransactionHotelReceivable, String> {

        private JFXTextField nominalTransaction;

        public EditingCellNominalTransaction() {

        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null) {
                super.startEdit();
                nominalTransaction = new JFXTextField();
                nominalTransaction.setPromptText("Nominal Bayar");

                ClassViewSetting.setImportantField(
                        nominalTransaction);

                nominalTransaction.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", nominalTransaction);

                ((TblHotelFinanceTransactionHotelReceivable) this.getTableRow().getItem()).nominalTransactionProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null
                            && newVal.compareTo(getRestOfBill(((TblHotelFinanceTransactionHotelReceivable) this.getTableRow().getItem()).getTblHotelReceivable())) == 1) {
                        ((TblHotelFinanceTransactionHotelReceivable) this.getTableRow().getItem()).setNominalTransaction(getRestOfBill(((TblHotelFinanceTransactionHotelReceivable) this.getTableRow().getItem()).getTblHotelReceivable()));
                    }
                    //refresh total nominal transaction
                    refreshDataHotelFinanceTransaction();
                });

                Bindings.bindBidirectional(nominalTransaction.textProperty(),
                        ((TblHotelFinanceTransactionHotelReceivable) this.getTableRow().getItem()).nominalTransactionProperty(),
                        new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(nominalTransaction);
                nominalTransaction.selectAll();

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

            nominalTransaction.textProperty().unbindBidirectional(((TblHotelFinanceTransactionHotelReceivable) this.getTableRow().getItem()).nominalTransactionProperty());

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
                            if (((TblHotelFinanceTransactionHotelReceivable) data).getNominalTransaction() != null) {
                                setText(ClassFormatter.currencyFormat.cFormat(((TblHotelFinanceTransactionHotelReceivable) data).getNominalTransaction()));
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

    private void refreshDataHotelFinanceTransaction() {
        BigDecimal nominalTransaction = calculationHotelFinanceTransactionNominal();
        if (cbpFinanceTransactionPaymentType.getValue() != null
                && cbpFinanceTransactionPaymentType.getValue().getIdtype() == 0) {   //Tunai = '0'
            returReceivableController.selectedDataHFT.setTransactionNominal(getNominalAfterRounding(nominalTransaction));
            returReceivableController.selectedDataHFT.setTransactionRoundingValue(getRoundingValue(nominalTransaction));
        } else {
            returReceivableController.selectedDataHFT.setTransactionNominal(calculationHotelFinanceTransactionNominal());
            returReceivableController.selectedDataHFT.setTransactionRoundingValue(new BigDecimal("0"));
        }
        lblHotelFinanceTransaction.setText(ClassFormatter.currencyFormat.format(nominalTransaction));
    }

    public BigDecimal calculationHotelFinanceTransactionNominal() {
        BigDecimal result = new BigDecimal("0");
        for (TblHotelFinanceTransactionHotelReceivable data : (List<TblHotelFinanceTransactionHotelReceivable>) tableHotelReceivable.getItems()) {
            result = result.add(data.getNominalTransaction());
        }
        return result;
    }

    public BigDecimal getNominalAfterRounding(BigDecimal nominal) {
        BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("1"))).setScale(0, RoundingMode.HALF_UP)).multiply(new BigDecimal("1"));
        return nominalAfterRounding;
    }

    public BigDecimal getRoundingValue(BigDecimal nominal) {
        BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("1"))).setScale(0, RoundingMode.HALF_UP)).multiply(new BigDecimal("1"));
        return nominalAfterRounding.subtract(nominal);
    }

    /**
     * DATA HOTEL RECEIVABLE
     */
    /**
     * DATA HOTEL FINANCE TRANSACTION -> BY TYPE
     */
    @FXML
    private AnchorPane ancHFTDetailLayout;

    private void setTransactionTypeInputForm(RefFinanceTransactionPaymentType transactionType) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (transactionType.getIdtype()) {
                case 0:    //Tunai = '0'
                    returReceivableController.selectedDataHFTWithCash = new TblHotelFinanceTransactionWithCash();
                    returReceivableController.selectedDataHFTWithCash.setTblHotelFinanceTransaction(returReceivableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_receivable_and_payment/tid/TIDCash.fxml"));

                    TIDCashController tidCashController = new TIDCashController(this);
                    loader.setController(tidCashController);
                    break;
                case 1:    //Transfer = '1'
                    returReceivableController.selectedDataHFTWithTransfer = new TblHotelFinanceTransactionWithTransfer();
                    returReceivableController.selectedDataHFTWithTransfer.setTblHotelFinanceTransaction(returReceivableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_receivable_and_payment/tid/TIDTransfer.fxml"));

                    TIDTransferController tidTransferController = new TIDTransferController(this);
                    loader.setController(tidTransferController);
                    break;
                case 4: //Cek = '4'
                    returReceivableController.selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
                    returReceivableController.selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(returReceivableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_receivable_and_payment/tid/TIDCekGiro.fxml"));

                    TIDCekGiroController tidCekController = new TIDCekGiroController(this);
                    loader.setController(tidCekController);
                    break;
                case 5:    //Giro = '5'
                    returReceivableController.selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
                    returReceivableController.selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(returReceivableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_receivable_and_payment/tid/TIDCekGiro.fxml"));

                    TIDCekGiroController tidGiroController = new TIDCekGiroController(this);
                    loader.setController(tidGiroController);
                    break;
                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }

            //load loader to node
            Node subContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);

            //set 'sub feature content' into the center of 'feature_bank' contetnt.
            ancHFTDetailLayout.getChildren().clear();
            ancHFTDetailLayout.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataHotelFinanceTransaction()) {
            switch (cbpFinanceTransactionPaymentType.getValue().getIdtype()) {
                case 0:    //Tunai = '0'
                    dataDetailWithCashSaveHandle();
                    break;
                case 1:    //Transfer = '1'
                    dataDetailWithTransferSaveHandle();
                    break;
                case 4:    //Cek = '4'
                    dataDetailWithCekGiroSaveHandle();
                    break;
                case 5:    //Giro = '5'
                    dataDetailWithCekGiroSaveHandle();
                    break;
                default:
                    break;
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, returReceivableController.dialogStageDetal);
        }
    }

    private void dataDetailWithCashSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithCash()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", returReceivableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(returReceivableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }

                TblHotelFinanceTransactionWithCash dummySelectedDataWithCash = new TblHotelFinanceTransactionWithCash(returReceivableController.selectedDataHFTWithCash);
                dummySelectedDataWithCash.setTblHotelFinanceTransaction(dummySelectedData);
                if (dummySelectedDataWithCash.getTblCompanyBalance() != null) {
                    dummySelectedDataWithCash.setTblCompanyBalance(new TblCompanyBalance(dummySelectedDataWithCash.getTblCompanyBalance()));
                }
                List<TblHotelFinanceTransactionHotelReceivable> dummySelectedDataHFTHRs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelReceivable selectedDataHFTHR : returReceivableController.selectedDataHFTHRs) {
                    if (selectedDataHFTHR.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelReceivable dummySelectedDataHFTHR = new TblHotelFinanceTransactionHotelReceivable(selectedDataHFTHR);
                        dummySelectedDataHFTHR.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHR.setTblHotelReceivable(new TblHotelReceivable(dummySelectedDataHFTHR.getTblHotelReceivable()));
                        dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHR.getTblHotelReceivable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                    .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == 1) {
                                dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                        .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == -1) {
                                    dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHRs.add(dummySelectedDataHFTHR);
                    }
                }
                if (returReceivableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithCash,
                        dummySelectedDataHFTHRs, 
                        returReceivableController.selectedData.getSupplierName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", returReceivableController.dialogStageDetal);
                    //refresh data table supplier - hotel receivable
                    returReceivableController.refreshDataTableReturReceivable();
                    //auto selected current data..
                    returReceivableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    returReceivableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(returReceivableController.getService().getErrorMessage(), returReceivableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, returReceivableController.dialogStageDetal);
        }
    }

    private void dataDetailWithTransferSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithTransfer()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", returReceivableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(returReceivableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithTransfer dummySelectedDataWithTransfer = new TblHotelFinanceTransactionWithTransfer(returReceivableController.selectedDataHFTWithTransfer);
                dummySelectedDataWithTransfer.setTblHotelFinanceTransaction(dummySelectedData);
                if (dummySelectedDataWithTransfer.getTblBankAccountBySenderBankAccount() != null) {
                    dummySelectedDataWithTransfer.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedDataWithTransfer.getTblBankAccountBySenderBankAccount()));
                }
                if (dummySelectedDataWithTransfer.getTblBankAccountByReceiverBankAccount() != null) {
                    dummySelectedDataWithTransfer.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedDataWithTransfer.getTblBankAccountByReceiverBankAccount()));
                }
                List<TblHotelFinanceTransactionHotelReceivable> dummySelectedDataHFTHRs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelReceivable selectedDataHFTHR : returReceivableController.selectedDataHFTHRs) {
                    if (selectedDataHFTHR.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelReceivable dummySelectedDataHFTHR = new TblHotelFinanceTransactionHotelReceivable(selectedDataHFTHR);
                        dummySelectedDataHFTHR.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHR.setTblHotelReceivable(new TblHotelReceivable(dummySelectedDataHFTHR.getTblHotelReceivable()));
                        dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHR.getTblHotelReceivable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                    .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == 1) {
                                dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                        .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == -1) {
                                    dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHRs.add(dummySelectedDataHFTHR);
                    }
                }
                if (returReceivableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithTransfer,
                        dummySelectedDataHFTHRs, 
                        returReceivableController.selectedData.getSupplierName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", returReceivableController.dialogStageDetal);
                    //refresh data table supplier - hotel receivable
                    returReceivableController.refreshDataTableReturReceivable();
                    //auto selected current data..
                    returReceivableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    returReceivableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(returReceivableController.getService().getErrorMessage(), returReceivableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, returReceivableController.dialogStageDetal);
        }
    }

    private void dataDetailWithCekGiroSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithCekGiro()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", returReceivableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(returReceivableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithCekGiro dummySelectedDataWithCekGiro = new TblHotelFinanceTransactionWithCekGiro(returReceivableController.selectedDataHFTWithCekGiro);
                dummySelectedDataWithCekGiro.setTblHotelFinanceTransaction(dummySelectedData);
                if (dummySelectedDataWithCekGiro.getTblBank() != null) {
                    dummySelectedDataWithCekGiro.setTblBank(new TblBank(dummySelectedDataWithCekGiro.getTblBank()));
                }
                if (dummySelectedDataWithCekGiro.getTblBankAccountBySenderBankAccount() != null) {
                    dummySelectedDataWithCekGiro.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedDataWithCekGiro.getTblBankAccountBySenderBankAccount()));
                }
                if (dummySelectedDataWithCekGiro.getTblBankAccountByReceiverBankAccount() != null) {
                    dummySelectedDataWithCekGiro.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedDataWithCekGiro.getTblBankAccountByReceiverBankAccount()));
                }
                List<TblHotelFinanceTransactionHotelReceivable> dummySelectedDataHFTHRs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelReceivable selectedDataHFTHR : returReceivableController.selectedDataHFTHRs) {
                    if (selectedDataHFTHR.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelReceivable dummySelectedDataHFTHR = new TblHotelFinanceTransactionHotelReceivable(selectedDataHFTHR);
                        dummySelectedDataHFTHR.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHR.setTblHotelReceivable(new TblHotelReceivable(dummySelectedDataHFTHR.getTblHotelReceivable()));
                        dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHR.getTblHotelReceivable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                    .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == 1) {
                                dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHR.getTblHotelReceivable())
                                        .compareTo(dummySelectedDataHFTHR.getNominalTransaction()) == -1) {
                                    dummySelectedDataHFTHR.getTblHotelReceivable().setRefFinanceTransactionStatus(returReceivableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHRs.add(dummySelectedDataHFTHR);
                    }
                }
                if (returReceivableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithCekGiro,
                        dummySelectedDataHFTHRs, 
                        returReceivableController.selectedData.getSupplierName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", returReceivableController.dialogStageDetal);
                    //refresh data table supplier - hotel receivable
                    returReceivableController.refreshDataTableReturReceivable();
                    //auto selected current data..
                    returReceivableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    returReceivableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(returReceivableController.getService().getErrorMessage(), returReceivableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, returReceivableController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data hotel finance transaction input
        returReceivableController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataHotelFinanceTransaction() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpFinanceTransactionPaymentType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Pembayaran : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private boolean checkDataInputDataHotelFinanceTransactionWithCash() {
        boolean dataInput = true;
        errDataInput = "";
        if (returReceivableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        }
        if (returReceivableController.selectedDataHFTWithCash.getTblCompanyBalance() == null) {
            dataInput = false;
            errDataInput += "Kas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private boolean checkDataInputDataHotelFinanceTransactionWithTransfer() {
        boolean dataInput = true;
        errDataInput = "";
        if (returReceivableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        }
        if (returReceivableController.selectedDataHFTWithTransfer.getSenderName() == null
                || returReceivableController.selectedDataHFTWithTransfer.getSenderName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithTransfer.getTblBankAccountBySenderBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithTransfer.getReceiverName() == null
                || returReceivableController.selectedDataHFTWithTransfer.getReceiverName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithTransfer.getTblBankAccountByReceiverBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private boolean checkDataInputDataHotelFinanceTransactionWithCekGiro() {
        boolean dataInput = true;
        errDataInput = "";
        if (returReceivableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        }
        if (returReceivableController.selectedDataHFTWithCekGiro.getCodeCekGiro() == null
                || returReceivableController.selectedDataHFTWithCekGiro.getCodeCekGiro().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Cek/Giro : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithCekGiro.getIssueDateTime() == null) {
            dataInput = false;
            errDataInput += "Tanggal Buat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithCekGiro.getValidUntilDateTime() == null) {
            dataInput = false;
            errDataInput += "Tanggal Jatuh Tempo : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithCekGiro.getTblBank() == null) {
            dataInput = false;
            errDataInput += "Bank Penerbit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithCekGiro.getSenderName() == null
                || returReceivableController.selectedDataHFTWithCekGiro.getSenderName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (returReceivableController.selectedDataHFTWithCekGiro.getTblBankAccountBySenderBankAccount() == null) {
//            dataInput = false;
//            errDataInput += "Nomor Rekening (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (returReceivableController.selectedDataHFTWithCekGiro.getReceiverName() == null
                || returReceivableController.selectedDataHFTWithCekGiro.getReceiverName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (returReceivableController.selectedDataHFTWithCekGiro.getTblBankAccountByReceiverBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataHFT();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public HotelFinanceTransactionInputController(ReturReceivableController parentController) {
        returReceivableController = parentController;
    }

    private final ReturReceivableController returReceivableController;

    public ReturReceivableController getParentController() {
        return returReceivableController;
    }

}
