/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payment_x;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionType;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblSupplierBankAccount;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
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
public class PurchaseOrderPaymentDetailController implements Initializable {

//    @FXML
//    private AnchorPane ancFormDetail;
//
//    @FXML
//    private GridPane gpFormDataDetail;
//
//    @FXML
//    private JFXTextField txtCodePO;
//
//    @FXML
//    private JFXTextField txtSupplierName;
//
//    @FXML
//    private JFXTextField txtRestOfBill;
//
//    @FXML
//    private JFXTextField txtNominalPayment;
//
//    @FXML
//    private JFXTextField txtSenderName;
//
//    @FXML
//    private JFXTextField txtReceiverName;
//
//    @FXML
//    private AnchorPane ancSenderBankAccountLayout;
//
//    @FXML
//    private AnchorPane ancReceiverBankAccountLayout;
//
//    private JFXCComboBoxTablePopup<TblBankAccount> cbpSenderBankAccount;
//
//    private JFXCComboBoxTablePopup<TblBankAccount> cbpReceiverBankAccount;
//
//    @FXML
//    private JFXButton btnAddSupplierBankAccount;
//
//    @FXML
//    private JFXButton btnSave;
//
//    @FXML
//    private JFXButton btnCancel;
//
//    private void initFormDataDetail() {
//        initDataPopup();
//
//        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
//        btnSave.setOnAction((e) -> {
//            dataDetailSaveHandle();
//        });
//
//        btnCancel.setTooltip(new Tooltip("Batal"));
//        btnCancel.setOnAction((e) -> {
//            dataDetailCancelHandle();
//        });
//
//        btnAddSupplierBankAccount.setTooltip(new Tooltip("Tambahkan (Nomor Rekening Supplier)"));
//        btnAddSupplierBankAccount.setOnAction((e) -> {
//            dataSupplierBankAccountAddHandle();
//        });
//
//        initImportantFieldColor();
//
//        initNumbericField();
//    }
//
//    private void initImportantFieldColor() {
//        ClassViewSetting.setImportantField(
//                txtNominalPayment,
//                cbpSenderBankAccount,
//                cbpReceiverBankAccount);
//    }
//
//    private void initNumbericField() {
//        ClassFormatter.setToNumericField(
//                "BigDecimal",
//                txtNominalPayment);
//    }
//
//    private void initDataPopup() {
//        //Bank Account (Sender)
//        TableView<TblBankAccount> tableSenderBankAccount = new TableView<>();
//
//        TableColumn<TblBankAccount, String> senderBankName = new TableColumn("Bank");
//        senderBankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
//        senderBankName.setMinWidth(140);
//
//        TableColumn<TblBankAccount, String> senderBankAccount = new TableColumn("Nomor Rekening");
//        senderBankAccount.setCellValueFactory(cellData -> cellData.getValue().codeBankAccountProperty());
//        senderBankAccount.setMinWidth(140);
//
//        TableColumn<TblBankAccount, String> senderHolderName = new TableColumn("Nama Pemegang Rekening");
//        senderHolderName.setCellValueFactory(cellData -> cellData.getValue().bankAccountHolderNameProperty());
//        senderHolderName.setMinWidth(200);
//
//        tableSenderBankAccount.getColumns().addAll(senderBankName, senderBankAccount, senderHolderName);
//
//        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
//
//        cbpSenderBankAccount = new JFXCComboBoxTablePopup<>(
//                TblBankAccount.class, tableSenderBankAccount, senderBankAccountItems, "", "Nomor Rekening (Pengirim) *", true, 500, 200
//        );
//
//        //Bank Account (Receiver)
//        TableView<TblBankAccount> tableReceiverBankAccount = new TableView<>();
//
//        TableColumn<TblBankAccount, String> receiverBankName = new TableColumn("Bank");
//        receiverBankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
//        receiverBankName.setMinWidth(140);
//
//        TableColumn<TblBankAccount, String> receiverBankAccount = new TableColumn("Nomor Rekening");
//        receiverBankAccount.setCellValueFactory(cellData -> cellData.getValue().codeBankAccountProperty());
//        receiverBankAccount.setMinWidth(140);
//
//        TableColumn<TblBankAccount, String> receiverHolderName = new TableColumn("Nama Pemegang Rekening");
//        receiverHolderName.setCellValueFactory(cellData -> cellData.getValue().bankAccountHolderNameProperty());
//        receiverHolderName.setMinWidth(200);
//
//        tableReceiverBankAccount.getColumns().addAll(receiverBankName, receiverBankAccount, receiverHolderName);
//
//        ObservableList<TblBankAccount> receiverBankAccountItems = FXCollections.observableArrayList(loadAllDataSupplierBankAccount());
//
//        cbpReceiverBankAccount = new JFXCComboBoxTablePopup<>(
//                TblBankAccount.class, tableReceiverBankAccount, receiverBankAccountItems, "", "Nomor Rekening (Penerima) *", true, 500, 200
//        );
//
//        //attached to grid-pane
//        AnchorPane.setBottomAnchor(cbpSenderBankAccount, 0.0);
//        AnchorPane.setLeftAnchor(cbpSenderBankAccount, 0.0);
//        AnchorPane.setRightAnchor(cbpSenderBankAccount, 0.0);
//        AnchorPane.setTopAnchor(cbpSenderBankAccount, 0.0);
//        ancSenderBankAccountLayout.getChildren().clear();
//        ancSenderBankAccountLayout.getChildren().add(cbpSenderBankAccount);
//        AnchorPane.setBottomAnchor(cbpReceiverBankAccount, 0.0);
//        AnchorPane.setLeftAnchor(cbpReceiverBankAccount, 0.0);
//        AnchorPane.setRightAnchor(cbpReceiverBankAccount, 0.0);
//        AnchorPane.setTopAnchor(cbpReceiverBankAccount, 0.0);
//        ancReceiverBankAccountLayout.getChildren().clear();
//        ancReceiverBankAccountLayout.getChildren().add(cbpReceiverBankAccount);
////        gpFormDataDetail.add(cbpSenderBankAccount, 1, 4, 2, 1);
////        gpFormDataDetail.add(cbpReceiverBankAccount, 1, 6, 2, 1);
//    }
//
//    private List<TblBankAccount> loadAllDataSupplierBankAccount() {
//        List<TblBankAccount> list = new ArrayList<>();
//        List<TblSupplierBankAccount> sbaList = purchaseOrderPaymentController.getService().getAllDataSupplierBankAccountByIDSupplier(purchaseOrderPaymentController.selectedDataPurchaseOrder.getTblSupplier().getIdsupplier());
//        for (TblSupplierBankAccount sbbaData : sbaList) {
//            //data bank account
//            TblBankAccount data = purchaseOrderPaymentController.getService().getBankAccount(sbbaData.getTblBankAccount().getIdbankAccount());
//            //data bank in data bank account
//            data.setTblBank(purchaseOrderPaymentController.getService().getDataBank(data.getTblBank().getIdbank()));
//            //add data bank account to list
//            list.add(data);
//        }
//        return list;
//    }
//
//    private List<TblBankAccount> loadAllDataHotelBankAccount() {
//        List<TblCompanyBalanceBankAccount> cbbaList = purchaseOrderPaymentController.getService().getAllDataCompanyBalanceBankAccount((long) 1);  //hotel balance = '1'
//        List<TblBankAccount> list = new ArrayList<>();
//        for (TblCompanyBalanceBankAccount cbbaData : cbbaList) {
//            //data bank account
//            TblBankAccount data = purchaseOrderPaymentController.getService().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
//            //data bank in data bank account
//            data.setTblBank(purchaseOrderPaymentController.getService().getDataBank(data.getTblBank().getIdbank()));
//            //add data bank account to list
//            list.add(data);
//        }
//        return list;
//    }
//
//    private void refreshDataPopup() {
//        //Sender - Bank Account (Supplier)
//        refreshSupplierBankAccountDataPopup();
//
//        //Sender - Bank Account (Hotel : CompanyBalance(Kas Besar))
//        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
//        cbpSenderBankAccount.setItems(senderBankAccountItems);
//    }
//
//    public void refreshSupplierBankAccountDataPopup() {
//        ObservableList<TblBankAccount> receiverBankAccountItems = FXCollections.observableArrayList(loadAllDataSupplierBankAccount());
//        cbpReceiverBankAccount.setItems(receiverBankAccountItems);
//    }
//
//    private void setSelectedDataToInputForm() {
//
//        refreshDataPopup();
//
//        txtCodePO.textProperty().bind(purchaseOrderPaymentController.selectedDataPurchaseOrder.codePoProperty());
//        txtSupplierName.textProperty().bind(purchaseOrderPaymentController.selectedDataPurchaseOrder.getTblSupplier().supplierNameProperty());
//
//        txtRestOfBill.setText(ClassFormatter.currencyFormat.cFormat(purchaseOrderPaymentController.calculationTotalRestOfBill(purchaseOrderPaymentController.selectedDataPurchaseOrder)));
//        Bindings.bindBidirectional(txtNominalPayment.textProperty(), purchaseOrderPaymentController.selectedData.transactionNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
//
//        txtSenderName.textProperty().bind(purchaseOrderPaymentController.selectedData.senderNameProperty());
//        txtReceiverName.textProperty().bind(purchaseOrderPaymentController.selectedData.receiverNameProperty());
//
//        cbpSenderBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                purchaseOrderPaymentController.selectedData.setSenderName(newVal.getBankAccountHolderName());
//            }
//        });
//        cbpReceiverBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                purchaseOrderPaymentController.selectedData.setReceiverName(newVal.getBankAccountHolderName());
//            }
//        });
//
//        cbpSenderBankAccount.valueProperty().bindBidirectional(purchaseOrderPaymentController.selectedData.tblBankAccountBySenderBankAccountProperty());
//        cbpReceiverBankAccount.valueProperty().bindBidirectional(purchaseOrderPaymentController.selectedData.tblBankAccountByReceiverBankAccountProperty());
//
//        cbpSenderBankAccount.hide();
//        cbpReceiverBankAccount.hide();
//    }
//
//    private void dataDetailSaveHandle() {
////        if (checkDataInputDataDetail()) {
////            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderPaymentController.dialogStageDetal);
////            if (alert.getResult() == ButtonType.OK) {
////                if (purchaseOrderPaymentController.calculationTotalRestOfBill(purchaseOrderPaymentController.selectedDataPurchaseOrder)
////                        .compareTo(purchaseOrderPaymentController.selectedData.getTransactionNominal()) == 0) {
////                    purchaseOrderPaymentController.selectedData.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPaymentController.getService().getDataFinanceTransactionStatus(2)); //2 = 'Lunas'
////                } else {
////                    purchaseOrderPaymentController.selectedData.getTblHotelPayable().setRefFinanceTransactionStatus(purchaseOrderPaymentController.getService().getDataFinanceTransactionStatus(1)); //1 = 'Belum Lunas'
////                }
////                //dummy entry
////                TblHotelFinanceTransaction dummySelectedData = new TblHotelFinanceTransaction(purchaseOrderPaymentController.selectedData);
////                dummySelectedData.setTblHotelPayable(new TblHotelPayable(dummySelectedData.getTblHotelPayable()));
////                dummySelectedData.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccountBySenderBankAccount()));
////                dummySelectedData.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccountByReceiverBankAccount()));
////                dummySelectedData.setRefFinanceTransactionType(new RefFinanceTransactionType(dummySelectedData.getRefFinanceTransactionType()));
////                TblPurchaseOrder dummySelectedDataPurchaseOrder = new TblPurchaseOrder(purchaseOrderPaymentController.selectedDataPurchaseOrder);
////                dummySelectedDataPurchaseOrder.setTblHotelPayable(dummySelectedData.getTblHotelPayable());
////                if (purchaseOrderPaymentController.getService().insertDataHotelFinanceTransaction(dummySelectedData,
////                        dummySelectedDataPurchaseOrder) != null) {
////                    ClassMessage.showSucceedInsertingDataMessage("", purchaseOrderPaymentController.dialogStageDetal);
////                    //refresh data table PO - Payment
////                    purchaseOrderPaymentController.refreshData();
////                    //close form data detail
////                    purchaseOrderPaymentController.dialogStageDetal.close();
////                } else {
////                    ClassMessage.showFailedInsertingDataMessage(purchaseOrderPaymentController.getService().getErrorMessage(), purchaseOrderPaymentController.dialogStageDetal);
////                }
////            }
////        } else {
////            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderPaymentController.dialogStageDetal);
////        }
//    }
//
//    private void dataDetailCancelHandle() {
//        //refresh data table PO - Payment
//        purchaseOrderPaymentController.refreshData();
//        //close form data detail
//        purchaseOrderPaymentController.dialogStageDetal.close();
//    }
//
//    private String errDataInput;
//
//    private boolean checkDataInputDataDetail() {
//        boolean dataInput = true;
//        errDataInput = "";
//        if (txtNominalPayment.getText() == null
//                || txtNominalPayment.getText().equals("")
//                || txtNominalPayment.getText().equals("-")) {
//            dataInput = false;
//            errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        } else {
//            if (purchaseOrderPaymentController.selectedData.getTransactionNominal().compareTo(new BigDecimal("0")) < 1) {
//                dataInput = false;
//                errDataInput += "Nominal Transaksi  : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//            } else {
//                BigDecimal restOfBill = purchaseOrderPaymentController.calculationTotalRestOfBill(purchaseOrderPaymentController.selectedDataPurchaseOrder);
//                if (purchaseOrderPaymentController.selectedData.getTransactionNominal().compareTo(restOfBill) == 1) {
//                    dataInput = false;
//                    errDataInput += "Nominal Transaksi  : Nilai tidak dapat lebih besar " + ClassFormatter.currencyFormat.cFormat(restOfBill) + " (Total Tagihan PO) \n";
//                } else {
//                    BigDecimal maxCompanyBalance = getMaxCompanyBalance();
//                    if (purchaseOrderPaymentController.selectedData.getTransactionNominal().compareTo(maxCompanyBalance) == 1) {
//                        dataInput = false;
//                        errDataInput += "Nominal Transaksi  : Nilai tidak dapat lebih besar " + ClassFormatter.currencyFormat.cFormat(maxCompanyBalance) + " (Total Nominal Kas Besar) \n";
//                    }
//                }
//            }
//        }
//        if (cbpSenderBankAccount.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Nomor Rekening Pengirim : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (cbpReceiverBankAccount.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Nomor Rekening Penerima : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        return dataInput;
//    }
//
//    private BigDecimal getMaxCompanyBalance() {
//        TblCompanyBalance dataBalance = purchaseOrderPaymentController.getService().getDataCompanyBalance((long) 1); //1 = 'Kas Besar'
//        return dataBalance.getBalanceNominal();
//    }
//
//    /**
//     * DATA SUPPLIER BANK ACCOUNT (ADD - HANDLER)
//     */
//    public TblSupplierBankAccount selectedDataSupplierBankAccount;
//
//    public Stage dialogStage;
//
//    private void dataSupplierBankAccountAddHandle() {
//        selectedDataSupplierBankAccount = new TblSupplierBankAccount();
//        selectedDataSupplierBankAccount.setTblSupplier(purchaseOrderPaymentController.selectedDataPurchaseOrder.getTblSupplier());
//        selectedDataSupplierBankAccount.setTblBankAccount(new TblBankAccount());
//        //open form data supplier bank account
//        showSupplierBankAccountDialog();
//    }
//
//    private void showSupplierBankAccountDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payment/POPDSupplierBankAccountInputDialog.fxml"));
//
//            POPDSupplierBankAccountInputController controller = new POPDSupplierBankAccountInputController(this);
//            loader.setController(controller);
//
//            Region page = loader.load();
//
//            // Create the dialog Stage.
//            dialogStage = new Stage();
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(purchaseOrderPaymentController.dialogStageDetal);
//
//            //undecorated
//            Undecorator undecorator = new Undecorator(dialogStage, page);
//            undecorator.getStylesheets().add("skin/undecorator.css");
//            undecorator.getMenuButton().setVisible(false);
//            undecorator.getMaximizeButton().setVisible(false);
//            undecorator.getMinimizeButton().setVisible(false);
//            undecorator.getFullScreenButton().setVisible(false);
//            undecorator.getCloseButton().setVisible(false);
//
//            Scene scene = new Scene(undecorator);
//            scene.setFill(Color.TRANSPARENT);
//
//            dialogStage.initStyle(StageStyle.TRANSPARENT);
//            dialogStage.setScene(scene);
//            dialogStage.setResizable(false);
//
//            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
//        } catch (IOException e) {
//            System.out.println("Err >> " + e.toString());
//        }
//    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        //init form input
//        initFormDataDetail();
//        //refresh data form input
//        setSelectedDataToInputForm();
    }

    public PurchaseOrderPaymentDetailController(PurchaseOrderPaymentController parentController) {
        purchaseOrderPaymentController = parentController;
    }

    private final PurchaseOrderPaymentController purchaseOrderPaymentController;

    public PurchaseOrderPaymentController getParentController() {
        return purchaseOrderPaymentController;
    }

}
