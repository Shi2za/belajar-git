/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment;

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
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.tid_r.TIDRCashController;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.tid_r.TIDRCekGiroController;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.tid_r.TIDRTransferController;
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
public class HotelFinanceTransactionReturnInputController implements Initializable {

    @FXML
    private AnchorPane ancFormHFTR;

    @FXML
    private GridPane gpFormDataHFTR;

    @FXML
    private Label lblHotelFinanceTransactionReturn;

    @FXML
    private AnchorPane ancPaymentType;
    public JFXCComboBoxTablePopup<RefFinanceTransactionPaymentType> cbpFinanceTransactionPaymentType;

    @FXML
    public JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataHFTR() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi Pengembalian Pembayaran)"));
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
        List<RefFinanceTransactionPaymentType> list = purchaseOrderPayableController.getService().getAllDataFinanceTransactionPaymentType();
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
        cbpFinanceTransactionPaymentType.valueProperty().bindBidirectional(purchaseOrderPayableController.selectedDataHFT.refFinanceTransactionPaymentTypeProperty());

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

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getCodePO(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codePO.setMinWidth(100);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> dueDate = new TableColumn("Tgl. Estimasi\n Bayar");
        dueDate.setMinWidth(120);
        dueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getDueDate(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> nominalRestOfBill = new TableColumn("Total Nominal\n Kelebihan Bayar");
        nominalRestOfBill.setMinWidth(120);
        nominalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getNominalRestOfBill(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> nominalTransaction = new TableColumn("Nominal Transaksi\n Pengembalian");
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

        tableHotelPayable.getColumns().addAll(codePO, codeInvoice, dueDate, nominalRestOfBill, nominalTransaction);
        tableHotelPayable.setItems(FXCollections.observableArrayList(purchaseOrderPayableController.selectedDataHFTHPs));

        AnchorPane.setBottomAnchor(tableHotelPayable, 0.0);
        AnchorPane.setLeftAnchor(tableHotelPayable, 0.0);
        AnchorPane.setRightAnchor(tableHotelPayable, 0.0);
        AnchorPane.setTopAnchor(tableHotelPayable, 0.0);
        ancTableHotelPayableLayout.getChildren().add(tableHotelPayable);
    }

    private String getCodePO(TblHotelPayable hotelPayable) {
        String result = "-";
        if (hotelPayable != null) {
            TblPurchaseOrder purchaseOrder = purchaseOrderPayableController.getService().getDataPurchaseOrderByIDHotelPayable(hotelPayable.getIdhotelPayable());
            if (purchaseOrder != null) {
                result = purchaseOrder.getCodePo();
            }
        }
        return result;
    }

    private String getCodeInvoice(TblHotelPayable hotelPayable) {
        String result = "-";
        if (hotelPayable != null
                && hotelPayable.getTblHotelInvoice() != null) {
            TblHotelInvoice invoice = purchaseOrderPayableController.getService().getDataHotelInvoice(hotelPayable.getTblHotelInvoice().getIdhotelInvoice());
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
            TblHotelInvoice invoice = purchaseOrderPayableController.getService().getDataHotelInvoice(hotelPayable.getTblHotelInvoice().getIdhotelInvoice());
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
            List<TblHotelFinanceTransactionHotelPayable> list = purchaseOrderPayableController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(hotelPayable.getIdhotelPayable());
            for (TblHotelFinanceTransactionHotelPayable data : list) {
                if(data.getTblHotelFinanceTransaction().getIsReturnTransaction()){
                    result = result.add(data.getNominalTransaction());
                }else{
                    result = result.subtract(data.getNominalTransaction());
                }
            }
        }
        return result.multiply(new BigDecimal("-1"));
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
            purchaseOrderPayableController.selectedDataHFT.setTransactionNominal(getNominalAfterRounding(nominalTransaction));
            purchaseOrderPayableController.selectedDataHFT.setTransactionRoundingValue(getRoundingValue(nominalTransaction));
        } else {
            purchaseOrderPayableController.selectedDataHFT.setTransactionNominal(calculationHotelFinanceTransactionNominal());
            purchaseOrderPayableController.selectedDataHFT.setTransactionRoundingValue(new BigDecimal("0"));
        }
        lblHotelFinanceTransactionReturn.setText(ClassFormatter.currencyFormat.format(nominalTransaction));
    }

    public BigDecimal calculationHotelFinanceTransactionNominal() {
        BigDecimal result = new BigDecimal("0");
        for (TblHotelFinanceTransactionHotelPayable data : (List<TblHotelFinanceTransactionHotelPayable>) tableHotelPayable.getItems()) {
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
     * DATA HOTEL PAYABLE
     */
    /**
     * DATA HOTEL FINANCE TRANSACTION -> BY TYPE
     */
    @FXML
    private AnchorPane ancHFTRDetailLayout;

    private void setTransactionTypeInputForm(RefFinanceTransactionPaymentType transactionType) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (transactionType.getIdtype()) {
                case 0:    //Tunai = '0'
                    purchaseOrderPayableController.selectedDataHFTWithCash = new TblHotelFinanceTransactionWithCash();
                    purchaseOrderPayableController.selectedDataHFTWithCash.setTblHotelFinanceTransaction(purchaseOrderPayableController.selectedDataHFT);
                    
                    loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/tid_r/TIDRCash.fxml"));

                    TIDRCashController tidrCashController = new TIDRCashController(this);
                    loader.setController(tidrCashController);
                    break;
                case 1:    //Transfer = '1'
                    purchaseOrderPayableController.selectedDataHFTWithTransfer = new TblHotelFinanceTransactionWithTransfer();
                    purchaseOrderPayableController.selectedDataHFTWithTransfer.setTblHotelFinanceTransaction(purchaseOrderPayableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/tid_r/TIDRTransfer.fxml"));

                    TIDRTransferController tidrTransferController = new TIDRTransferController(this);
                    loader.setController(tidrTransferController);
                    break;
                case 4: //Cek = '4'
                    purchaseOrderPayableController.selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
                    purchaseOrderPayableController.selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(purchaseOrderPayableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/tid_r/TIDRCekGiro.fxml"));

                    TIDRCekGiroController tidrCekController = new TIDRCekGiroController(this);
                    loader.setController(tidrCekController);
                    break;
                case 5:    //Giro = '5'
                    purchaseOrderPayableController.selectedDataHFTWithCekGiro = new TblHotelFinanceTransactionWithCekGiro();
                    purchaseOrderPayableController.selectedDataHFTWithCekGiro.setTblHotelFinanceTransaction(purchaseOrderPayableController.selectedDataHFT);

                    loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/tid_r/TIDRCekGiro.fxml"));
                    
                    TIDRCekGiroController tidrGiroController = new TIDRCekGiroController(this);
                    loader.setController(tidrGiroController);
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

            ancHFTRDetailLayout.getChildren().clear();
            ancHFTRDetailLayout.getChildren().add(subContent);

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
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPayableController.dialogStageDetal);
        }
    }

    private void dataDetailWithCashSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithCash()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderPayableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(purchaseOrderPayableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithCash dummySelectedDataWithCash = new TblHotelFinanceTransactionWithCash(purchaseOrderPayableController.selectedDataHFTWithCash);
                dummySelectedDataWithCash.setTblHotelFinanceTransaction(dummySelectedData);
                if (dummySelectedDataWithCash.getTblCompanyBalance() != null) {
                    dummySelectedDataWithCash.setTblCompanyBalance(new TblCompanyBalance(dummySelectedDataWithCash.getTblCompanyBalance()));
                }
                List<TblHotelFinanceTransactionHotelPayable> dummySelectedDataHFTHPs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelPayable selectedDataHFTHP : purchaseOrderPayableController.selectedDataHFTHPs) {
                    if (selectedDataHFTHP.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelPayable dummySelectedDataHFTHP = new TblHotelFinanceTransactionHotelPayable(selectedDataHFTHP);
                        dummySelectedDataHFTHP.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHP.setTblHotelPayable(new TblHotelPayable(dummySelectedDataHFTHP.getTblHotelPayable()));
                        dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHP.getTblHotelPayable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                    .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == -1) {
                                dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                        .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 1) {
                                    dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHPs.add(dummySelectedDataHFTHP);
                    }
                }
                if (purchaseOrderPayableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithCash,
                        dummySelectedDataHFTHPs, 
                        purchaseOrderPayableController.selectedData.getSupplierName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", purchaseOrderPayableController.dialogStageDetal);
                    //refresh data table supplier - hotel payable
                    purchaseOrderPayableController.refreshDataTablePOPayable();
                    //auto selected current data..
                    purchaseOrderPayableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    purchaseOrderPayableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(purchaseOrderPayableController.getService().getErrorMessage(), purchaseOrderPayableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPayableController.dialogStageDetal);
        }
    }
    
    private void dataDetailWithTransferSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithTransfer()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderPayableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(purchaseOrderPayableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithTransfer dummySelectedDataWithTransfer = new TblHotelFinanceTransactionWithTransfer(purchaseOrderPayableController.selectedDataHFTWithTransfer);
                dummySelectedDataWithTransfer.setTblHotelFinanceTransaction(dummySelectedData);
                if (dummySelectedDataWithTransfer.getTblBankAccountBySenderBankAccount() != null) {
                    dummySelectedDataWithTransfer.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedDataWithTransfer.getTblBankAccountBySenderBankAccount()));
                }
                if (dummySelectedDataWithTransfer.getTblBankAccountByReceiverBankAccount() != null) {
                    dummySelectedDataWithTransfer.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedDataWithTransfer.getTblBankAccountByReceiverBankAccount()));
                }
                List<TblHotelFinanceTransactionHotelPayable> dummySelectedDataHFTHPs = new ArrayList<>();
                for (TblHotelFinanceTransactionHotelPayable selectedDataHFTHP : purchaseOrderPayableController.selectedDataHFTHPs) {
                    if (selectedDataHFTHP.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelPayable dummySelectedDataHFTHP = new TblHotelFinanceTransactionHotelPayable(selectedDataHFTHP);
                        dummySelectedDataHFTHP.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHP.setTblHotelPayable(new TblHotelPayable(dummySelectedDataHFTHP.getTblHotelPayable()));
                        dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHP.getTblHotelPayable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                    .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == -1) {
                                dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                        .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 1) {
                                    dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHPs.add(dummySelectedDataHFTHP);
                    }
                }
                if (purchaseOrderPayableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithTransfer,
                        dummySelectedDataHFTHPs, 
                        purchaseOrderPayableController.selectedData.getSupplierName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", purchaseOrderPayableController.dialogStageDetal);
                    //refresh data table supplier - hotel payable
                    purchaseOrderPayableController.refreshDataTablePOPayable();
                    //auto selected current data..
                    purchaseOrderPayableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    purchaseOrderPayableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(purchaseOrderPayableController.getService().getErrorMessage(), purchaseOrderPayableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPayableController.dialogStageDetal);
        }
    }

    private void dataDetailWithCekGiroSaveHandle() {
        if (checkDataInputDataHotelFinanceTransactionWithCekGiro()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderPayableController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //dummy
                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(purchaseOrderPayableController.selectedDataHFT);
                if (dummySelectedData.getRefFinanceTransactionPaymentType() != null) {
                    dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                }
                if (dummySelectedData.getRefFinanceTransactionType() != null) {
                    dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
                }
                TblHotelFinanceTransactionWithCekGiro dummySelectedDataWithCekGiro = new TblHotelFinanceTransactionWithCekGiro(purchaseOrderPayableController.selectedDataHFTWithCekGiro);
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
                for (TblHotelFinanceTransactionHotelPayable selectedDataHFTHP : purchaseOrderPayableController.selectedDataHFTHPs) {
                    if (selectedDataHFTHP.getNominalTransaction().compareTo(new BigDecimal("0")) == 1) {    //payment was found
                        TblHotelFinanceTransactionHotelPayable dummySelectedDataHFTHP = new TblHotelFinanceTransactionHotelPayable(selectedDataHFTHP);
                        dummySelectedDataHFTHP.setTblHotelFinanceTransaction(dummySelectedData);
                        dummySelectedDataHFTHP.setTblHotelPayable(new TblHotelPayable(dummySelectedDataHFTHP.getTblHotelPayable()));
                        dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(dummySelectedDataHFTHP.getTblHotelPayable().getRefFinanceTransactionStatus()));
                        if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 0) {
                            dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(2)); //Sudah Dibayar = '2'
                        } else {
                            if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                    .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == -1) {
                                dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(1)); //Dibayar Sebagian = '1'
                            } else {
                                if (getRestOfBill(dummySelectedDataHFTHP.getTblHotelPayable())
                                        .compareTo(dummySelectedDataHFTHP.getNominalTransaction()) == 1) {
                                    dummySelectedDataHFTHP.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPayableController.getService().getDataFinanceTransactionStatus(5)); //Kelebihan Bayar = '5'   
                                }
                            }
                        }
                        //data PO (status) ...?
                        dummySelectedDataHFTHPs.add(dummySelectedDataHFTHP);
                    }
                }
                if (purchaseOrderPayableController.getService().insertDataHotelFinanceTransaction(
                        dummySelectedData,
                        dummySelectedDataWithCekGiro,
                        dummySelectedDataHFTHPs, 
                        purchaseOrderPayableController.selectedData.getSupplierName())
                        != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", purchaseOrderPayableController.dialogStageDetal);
                    //refresh data table supplier - hotel payable
                    purchaseOrderPayableController.refreshDataTablePOPayable();
                    //auto selected current data..
                    purchaseOrderPayableController.refreshSelectedData();
                    //close form data hotel finance transaction input
                    purchaseOrderPayableController.dialogStageDetal.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(purchaseOrderPayableController.getService().getErrorMessage(), purchaseOrderPayableController.dialogStageDetal);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPayableController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data hotel finance transaction input
        purchaseOrderPayableController.dialogStageDetal.close();
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
        if (purchaseOrderPayableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithCash.getTblCompanyBalance() == null) {
            dataInput = false;
            errDataInput += "Kas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }
    
    private boolean checkDataInputDataHotelFinanceTransactionWithTransfer() {
        boolean dataInput = true;
        errDataInput = "";
        if (purchaseOrderPayableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithTransfer.getSenderName() == null
                || purchaseOrderPayableController.selectedDataHFTWithTransfer.getSenderName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithTransfer.getTblBankAccountBySenderBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithTransfer.getReceiverName() == null
                || purchaseOrderPayableController.selectedDataHFTWithTransfer.getReceiverName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithTransfer.getTblBankAccountByReceiverBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    private boolean checkDataInputDataHotelFinanceTransactionWithCekGiro() {
        boolean dataInput = true;
        errDataInput = "";
        if (purchaseOrderPayableController.selectedDataHFT.getTransactionNominal()
                .compareTo(new BigDecimal("0")) < 1) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : Tidak boleh kurang dari atau sama dengan '0'\n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getCodeCekGiro() == null
                || purchaseOrderPayableController.selectedDataHFTWithCekGiro.getCodeCekGiro().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Cek/Giro : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getIssueDateTime() == null) {
            dataInput = false;
            errDataInput += "Tanggal Buat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getValidUntilDateTime() == null) {
            dataInput = false;
            errDataInput += "Tanggal Jatuh Tempo : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getTblBank() == null) {
            dataInput = false;
            errDataInput += "Bank Penerbit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getSenderName() == null
                || purchaseOrderPayableController.selectedDataHFTWithCekGiro.getSenderName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getTblBankAccountBySenderBankAccount() == null) {
//            dataInput = false;
//            errDataInput += "Nomor Rekening (Pembuat) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getReceiverName() == null
                || purchaseOrderPayableController.selectedDataHFTWithCekGiro.getReceiverName().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (purchaseOrderPayableController.selectedDataHFTWithCekGiro.getTblBankAccountByReceiverBankAccount() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    public BigDecimal getMaxCompanyBalance(TblBankAccount bankAccount) {
        if (bankAccount != null) {
            TblCompanyBalanceBankAccount dataBalanceBankAccount = purchaseOrderPayableController.getService().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
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
        initFormDataHFTR();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public HotelFinanceTransactionReturnInputController(PurchaseOrderPayableController parentController) {
        purchaseOrderPayableController = parentController;
    }

    private final PurchaseOrderPayableController purchaseOrderPayableController;

    public PurchaseOrderPayableController getParentController() {
        return purchaseOrderPayableController;
    }
    
}
