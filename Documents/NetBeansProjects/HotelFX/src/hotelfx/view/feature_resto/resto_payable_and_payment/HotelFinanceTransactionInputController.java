/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_resto.resto_payable_and_payment;

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
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.view.feature_resto.resto_payable_and_payment.tid.TIDCashController;
import hotelfx.view.feature_resto.resto_payable_and_payment.tid.TIDCekGiroController;
import hotelfx.view.feature_resto.resto_payable_and_payment.tid.TIDTransferController;
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
 * @author ABC-Programmer
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
        List<RefFinanceTransactionPaymentType> list = restoPayableController.getService().getAllDataFinanceTransactionPaymentType();
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
        cbpFinanceTransactionPaymentType.valueProperty().bindBidirectional(restoPayableController.selectedDataHFT.refFinanceTransactionPaymentTypeProperty());

        cbpFinanceTransactionPaymentType.hide();

        //refresh table hotel payable
        setDataTableHotelPayable();

        //refresh hotel finance transaction nominal
        refreshDataHotelFinanceTransaction();
    }

    @FXML
    private AnchorPane ancTableHotelPayableLayout;

    public TableView tableHotelPayable;

    public void setDataTableHotelPayable() {
        ancTableHotelPayableLayout.getChildren().clear();

        tableHotelPayable = new TableView();
        tableHotelPayable.setEditable(true);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeInvoice.setMinWidth(100);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codeRestoBill = new TableColumn("No. Tagihan");
        codeRestoBill.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getCodeRestoBill(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeRestoBill.setMinWidth(100);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> dueDate = new TableColumn("Tgl. Estimasi\n Bayar");
        dueDate.setMinWidth(120);
        dueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getDueDate(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> nominalRestOfBill = new TableColumn("Sisa Tagihan");
        nominalRestOfBill.setMinWidth(120);
        nominalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getNominalRestOfBill(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> nominalTransaction = new TableColumn("Nominal Transaksi\n Bayar");
        nominalTransaction.setMinWidth(120);
        nominalTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(()
                        -> ClassFormatter.currencyFormat.format(param.getValue().getNominalTransaction() != null
                                        ? param.getValue().getNominalTransaction()
                                        : new BigDecimal("0")),
                        param.getValue().nominalTransactionProperty()));
        Callback<TableColumn<TblHotelFinanceTransactionHotelPayable, String>, TableCell<TblHotelFinanceTransactionHotelPayable, String>> cellFactory
                = new Callback<TableColumn<TblHotelFinanceTransactionHotelPayable, String>, TableCell<TblHotelFinanceTransactionHotelPayable, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellNominalTransaction();
                    }
                };
        nominalTransaction.setCellFactory(cellFactory);
        nominalTransaction.setEditable(true);

        tableHotelPayable.getColumns().addAll(codeRestoBill, codeInvoice, dueDate, nominalRestOfBill, nominalTransaction);
        tableHotelPayable.setItems(FXCollections.observableArrayList(restoPayableController.selectedDataHFTHPs));

        AnchorPane.setBottomAnchor(tableHotelPayable, 0.0);
        AnchorPane.setLeftAnchor(tableHotelPayable, 0.0);
        AnchorPane.setRightAnchor(tableHotelPayable, 0.0);
        AnchorPane.setTopAnchor(tableHotelPayable, 0.0);
        ancTableHotelPayableLayout.getChildren().add(tableHotelPayable);
    }

    private String getCodeRestoBill(TblHotelPayable hotelPayable) {
        String result = "-";
        if (hotelPayable != null) {
            TblReservationAdditionalService ras = restoPayableController.getService().getDataReservationAdditionalServiceByIDHotelPayable(hotelPayable.getIdhotelPayable());
            if (ras != null) {
                result = ras.getRestoTransactionNumber();
            }
        }
        return result;
    }

    private String getCodeInvoice(TblHotelPayable hotelPayable) {
        String result = "-";
        if (hotelPayable != null
                && hotelPayable.getTblHotelInvoice() != null) {
            TblHotelInvoice invoice = restoPayableController.getService().getDataHotelInvoice(hotelPayable.getTblHotelInvoice().getIdhotelInvoice());
            if (invoice != null) {
                result = invoice.getCodeHotelInvoice();
            }
        }
        return result;
    }

    private String getDueDate(TblHotelPayable hotelPayable) {
        String result = "-";
        if (hotelPayable != null
                && hotelPayable.getTblHotelInvoice() != null) {
            TblHotelInvoice invoice = restoPayableController.getService().getDataHotelInvoice(hotelPayable.getTblHotelInvoice().getIdhotelInvoice());
            if (invoice != null
                    && invoice.getDueDate() != null) {
                result = ClassFormatter.dateFormate.format(invoice.getDueDate());
            }
        }
        return result;
    }

    private String getNominalRestOfBill(TblHotelPayable hotelPayable) {
        return ClassFormatter.currencyFormat.format(getRestOfBill(hotelPayable));
    }

    private BigDecimal getRestOfBill(TblHotelPayable hotelPayable) {
        BigDecimal result = new BigDecimal("0");
        if (hotelPayable != null) {
            result = result.add(hotelPayable.getHotelPayableNominal());
            //hotel finance transaction
            List<TblHotelFinanceTransactionHotelPayable> list = restoPayableController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(hotelPayable.getIdhotelPayable());
            for (TblHotelFinanceTransactionHotelPayable data : list) {
                if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                    result = result.add(data.getNominalTransaction());
                } else {
                    result = result.subtract(data.getNominalTransaction());
                }
            }
        }
        return result;
    }

    class EditingCellNominalTransaction extends TableCell<TblHotelFinanceTransactionHotelPayable, String> {

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

                ((TblHotelFinanceTransactionHotelPayable) this.getTableRow().getItem()).nominalTransactionProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null
                            && newVal.compareTo(getRestOfBill(((TblHotelFinanceTransactionHotelPayable) this.getTableRow().getItem()).getTblHotelPayable())) == 1) {
                        ((TblHotelFinanceTransactionHotelPayable) this.getTableRow().getItem()).setNominalTransaction(getRestOfBill(((TblHotelFinanceTransactionHotelPayable) this.getTableRow().getItem()).getTblHotelPayable()));
                    }
                    //refresh total nominal transaction
                    refreshDataHotelFinanceTransaction();
                });

                Bindings.bindBidirectional(nominalTransaction.textProperty(),
                        ((TblHotelFinanceTransactionHotelPayable) this.getTableRow().getItem()).nominalTransactionProperty(),
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

            nominalTransaction.textProperty().unbindBidirectional(((TblHotelFinanceTransactionHotelPayable) this.getTableRow().getItem()).nominalTransactionProperty());

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
                            if (((TblHotelFinanceTransactionHotelPayable) data).getNominalTransaction() != null) {
                                setText(ClassFormatter.currencyFormat.cFormat(((TblHotelFinanceTransactionHotelPayable) data).getNominalTransaction()));
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
            restoPayableController.selectedDataHFT.setTransactionNominal(getNominalAfterRounding(nominalTransaction));
            restoPayableController.selectedDataHFT.setTransactionRoundingValue(getRoundingValue(nominalTransaction));
        } else {
            restoPayableController.selectedDataHFT.setTransactionNominal(calculationHotelFinanceTransactionNominal());
            restoPayableController.selectedDataHFT.setTransactionRoundingValue(new BigDecimal("0"));
        }
        lblHotelFinanceTransaction.setText(ClassFormatter.currencyFormat.format(nominalTransaction));
    }

    public BigDecimal calculationHotelFinanceTransactionNominal() {
        BigDecimal result = new BigDecimal("0");
        for (TblHotelFinanceTransactionHotelPayable data : (List<TblHotelFinanceTransactionHotelPayable>) tableHotelPayable.getItems()) {
            if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                result = result.subtract(data.getNominalTransaction());
            } else {
                result = result.add(data.getNominalTransaction());
            }
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
     * DATA HOTEL PAYABLE
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
                    restoPayableController.selectedDataHFTWithCash = new TblHotelFinanceTransactionWithCash();
                    restoPayableController.selectedDataHFTWithCash.setTblHotelFinanceTransaction(restoPayableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/tid/TIDCash.fxml"));

                    TIDCashController tidCashController = new TIDCashController(this);
                    loader.setController(tidCashController);
                    break;
                case 1:    //Transfer = '1'
                    restoPayableController.selectedDataHFTWithTransfer = new TblHotelFinanceTransactionWithTransfer();
                    restoPayableController.selectedDataHFTWithTransfer.setTblHotelFinanceTransaction(restoPayableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/tid/TIDTransfer.fxml"));

                    TIDTransferController tidTransferController = new TIDTransferController(this);
                    loader.setController(tidTransferController);
                    break;
                case 4: //Cek = '4'
                    restoPayableController.selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
                    restoPayableController.selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(restoPayableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/tid/TIDCekGiro.fxml"));

                    TIDCekGiroController tidCekController = new TIDCekGiroController(this);
                    loader.setController(tidCekController);
                    break;
                case 5:    //Giro = '5'
                    restoPayableController.selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
                    restoPayableController.selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(restoPayableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/tid/TIDCekGiro.fxml"));

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
            ClassMessage.showWarningInputDataMessage(errDataInput, restoPayableController.dialogStageDetal);
        }
    }

    private void dataDetailWithCashSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithCash()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", restoPayableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(restoPayableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithCash dummySelectedDataWithCash = new TblHotelFinanceTransactionWithCash(restoPayableController.selectedDataHFTWithCash);
                dummySelectedDataWithCash.setTblHotelFinanceTransaction(dummySelectedData);
                if (dummySelectedDataWithCash.getTblCompanyBalance() != null) {
                    dummySelectedDataWithCash.setTblCompanyBalance(new TblCompanyBalance(dummySelectedDataWithCash.getTblCompanyBalance()));
                }
                List<TblHotelFinanceTransactionHotelPayable> dummySelectedDataHFTHPs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelPayable selectedDataHFTHP : restoPayableController.selectedDataHFTHPs) {
                    if (selectedDataHFTHP.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelPayable dummySelectedDataHFTHP = new TblHotelFinanceTransactionHotelPayable(selectedDataHFTHP);
                        dummySelectedDataHFTHP.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHP.setTblHotelPayable(new TblHotelPayable(dummySelectedDataHFTHP.getTblHotelPayable()));
                        dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHP.getTblHotelPayable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                    .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 1) {
                                dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                        .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == -1) {
                                    dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHPs.add(dummySelectedDataHFTHP);
                    }
                }
                if (restoPayableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithCash,
                        dummySelectedDataHFTHPs,
                        restoPayableController.selectedData.getRestoName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", restoPayableController.dialogStageDetal);
                    //refresh data table resto - hotel payable
                    restoPayableController.refreshDataTableRestoPayable();
                    //auto selected current data..
                    restoPayableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    restoPayableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(restoPayableController.getService().getErrorMessage(), restoPayableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, restoPayableController.dialogStageDetal);
        }
    }

    private void dataDetailWithTransferSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithTransfer()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", restoPayableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(restoPayableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithTransfer dummySelectedDataWithTransfer = new TblHotelFinanceTransactionWithTransfer(restoPayableController.selectedDataHFTWithTransfer);
                dummySelectedDataWithTransfer.setTblHotelFinanceTransaction(dummySelectedData);
                if (dummySelectedDataWithTransfer.getTblBankAccountBySenderBankAccount() != null) {
                    dummySelectedDataWithTransfer.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedDataWithTransfer.getTblBankAccountBySenderBankAccount()));
                }
                if (dummySelectedDataWithTransfer.getTblBankAccountByReceiverBankAccount() != null) {
                    dummySelectedDataWithTransfer.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedDataWithTransfer.getTblBankAccountByReceiverBankAccount()));
                }
                List<TblHotelFinanceTransactionHotelPayable> dummySelectedDataHFTHPs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelPayable selectedDataHFTHP : restoPayableController.selectedDataHFTHPs) {
                    if (selectedDataHFTHP.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelPayable dummySelectedDataHFTHP = new TblHotelFinanceTransactionHotelPayable(selectedDataHFTHP);
                        dummySelectedDataHFTHP.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHP.setTblHotelPayable(new TblHotelPayable(dummySelectedDataHFTHP.getTblHotelPayable()));
                        dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHP.getTblHotelPayable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                    .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 1) {
                                dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                        .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == -1) {
                                    dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHPs.add(dummySelectedDataHFTHP);
                    }
                }
                if (restoPayableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithTransfer,
                        dummySelectedDataHFTHPs,
                        restoPayableController.selectedData.getRestoName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", restoPayableController.dialogStageDetal);
                    //refresh data table resto - hotel payable
                    restoPayableController.refreshDataTableRestoPayable();
                    //auto selected current data..
                    restoPayableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    restoPayableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(restoPayableController.getService().getErrorMessage(), restoPayableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, restoPayableController.dialogStageDetal);
        }
    }

    private void dataDetailWithCekGiroSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithCekGiro()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", restoPayableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(restoPayableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithCekGiro dummySelectedDataWithCekGiro = new TblHotelFinanceTransactionWithCekGiro(restoPayableController.selectedDataHFTWithCekGiro);
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
                List<TblHotelFinanceTransactionHotelPayable> dummySelectedDataHFTHPs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelPayable selectedDataHFTHP : restoPayableController.selectedDataHFTHPs) {
                    if (selectedDataHFTHP.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelPayable dummySelectedDataHFTHP = new TblHotelFinanceTransactionHotelPayable(selectedDataHFTHP);
                        dummySelectedDataHFTHP.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHP.setTblHotelPayable(new TblHotelPayable(dummySelectedDataHFTHP.getTblHotelPayable()));
                        dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHP.getTblHotelPayable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                    .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 1) {
                                dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                        .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == -1) {
                                    dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(restoPayableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHPs.add(dummySelectedDataHFTHP);
                    }
                }
                if (restoPayableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithCekGiro,
                        dummySelectedDataHFTHPs,
                        restoPayableController.selectedData.getRestoName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", restoPayableController.dialogStageDetal);
                    //refresh data table resto - hotel payable
                    restoPayableController.refreshDataTableRestoPayable();
                    //auto selected current data..
                    restoPayableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    restoPayableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(restoPayableController.getService().getErrorMessage(), restoPayableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, restoPayableController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data hotel finance transaction input
        restoPayableController.dialogStageDetal.close();
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
        if (restoPayableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        } else {
            BigDecimal maxCompanyBalance = restoPayableController.selectedDataHFTWithCash.getTblCompanyBalance().getBalanceNominal();
            if (restoPayableController.selectedDataHFT.getTransactionNominal().compareTo(maxCompanyBalance) == 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi  : Nilai tidak dapat lebih besar " + ClassFormatter.currencyFormat.cFormat(maxCompanyBalance) + " (Total Nominal Kas Besar) \n";
            }
        }
        if (restoPayableController.selectedDataHFTWithCash.getTblCompanyBalance() == null) {
            dataInput = false;
            errDataInput += "Kas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private boolean checkDataInputDataHotelFinanceTransactionWithTransfer() {
        boolean dataInput = true;
        errDataInput = "";
        if (restoPayableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        } else {
            BigDecimal maxCompanyBalance = getMaxCompanyBalance(restoPayableController.selectedDataHFTWithTransfer.getTblBankAccountBySenderBankAccount());
            if (restoPayableController.selectedDataHFT.getTransactionNominal().compareTo(maxCompanyBalance) == 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi  : Nilai tidak dapat lebih besar " + ClassFormatter.currencyFormat.cFormat(maxCompanyBalance) + " (Total Nominal Kas Besar) \n";
            }
        }
        if (restoPayableController.selectedDataHFTWithTransfer.getSenderName() == null
                || restoPayableController.selectedDataHFTWithTransfer.getSenderName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithTransfer.getTblBankAccountBySenderBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithTransfer.getReceiverName() == null
                || restoPayableController.selectedDataHFTWithTransfer.getReceiverName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithTransfer.getTblBankAccountByReceiverBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private boolean checkDataInputDataHotelFinanceTransactionWithCekGiro() {
        boolean dataInput = true;
        errDataInput = "";
        if (restoPayableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        } else {
            BigDecimal maxCompanyBalance = getMaxCompanyBalance(restoPayableController.selectedDataHFTWithCekGiro.getTblBankAccountBySenderBankAccount());
            if (restoPayableController.selectedDataHFT.getTransactionNominal().compareTo(maxCompanyBalance) == 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi  : Nilai tidak dapat lebih besar " + ClassFormatter.currencyFormat.cFormat(maxCompanyBalance) + " (Total Nominal Kas Besar) \n";
            }
        }
        if (restoPayableController.selectedDataHFTWithCekGiro.getCodeCekGiro() == null
                || restoPayableController.selectedDataHFTWithCekGiro.getCodeCekGiro().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Cek/Giro : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithCekGiro.getIssueDateTime() == null) {
            dataInput = false;
            errDataInput += "Tanggal Buat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithCekGiro.getValidUntilDateTime() == null) {
            dataInput = false;
            errDataInput += "Tanggal Jatuh Tempo : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithCekGiro.getTblBank() == null) {
            dataInput = false;
            errDataInput += "Bank Penerbit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithCekGiro.getSenderName() == null
                || restoPayableController.selectedDataHFTWithCekGiro.getSenderName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithCekGiro.getTblBankAccountBySenderBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (restoPayableController.selectedDataHFTWithCekGiro.getReceiverName() == null
                || restoPayableController.selectedDataHFTWithCekGiro.getReceiverName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (restoPayableController.selectedDataHFTWithCekGiro.getTblBankAccountByReceiverBankAccount() == null) {
//            dataInput = false;
//            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        return dataInput;
    }

    public BigDecimal getMaxCompanyBalance(TblBankAccount bankAccount) {
        if (bankAccount != null) {
            TblCompanyBalanceBankAccount dataBalanceBankAccount = restoPayableController.getService().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                    (long) 1, //Kas Besar = '1'
                    bankAccount.getIdbankAccount());
            if (dataBalanceBankAccount != null) {
                return dataBalanceBankAccount.getCompanyBalanceBankAccountNominal();
            }
        }
        return new BigDecimal("0");
    }
    
    /**
     * Initializes the controller class.
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
    
    public HotelFinanceTransactionInputController(RestoPayableController parentController) {
        restoPayableController = parentController;
    }

    private final RestoPayableController restoPayableController;

    public RestoPayableController getParentController() {
        return restoPayableController;
    }
    
}
