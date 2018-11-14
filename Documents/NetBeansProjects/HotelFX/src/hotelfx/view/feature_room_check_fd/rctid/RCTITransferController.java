/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check_fd.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCustomerBankAccount;
import hotelfx.view.feature_room_check_fd.RCTransactionInputDetailController;
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
 * @author ABC-Programmer
 */
public class RCTITransferController implements Initializable {

    @FXML
    private GridPane gpRCTITransfer;

    @FXML
    private JFXTextField txtTotalBill;

    @FXML
    private JFXCheckBox chbGetRestOfBill;

    @FXML
    private JFXTextField txtSenderName;

    @FXML
    private JFXTextField txtReceiverName;

    @FXML
    private AnchorPane ancSenderBankAccount;

    @FXML
    private AnchorPane ancReceiverBankAccount;

    private JFXCComboBoxTablePopup<TblBankAccount> cbpSenderBankAccount;

    private JFXCComboBoxTablePopup<TblBankAccount> cbpReceiverBankAccount;

    @FXML
    private JFXButton btnAddCustomerBankAccount;

    @FXML
    private JFXButton btnSave;

    private void initFormDataRCTITransfer() {
        initDataRCTITransferPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Transaksi)"));
        btnSave.setOnAction((e) -> {
            dataRCTITransferSaveHandle();
        });

        btnAddCustomerBankAccount.setTooltip(new Tooltip("Tambahkan (Nomor Rekening Customer)"));
        btnAddCustomerBankAccount.setOnAction((e) -> {
            dataCustomerBankAccountAddHandle();
        });

        //rest of bill
        chbGetRestOfBill.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //set rest of bill
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.setUnitNominal(rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                        .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()));
            }
        });

        rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal
                    .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn())))
                    != 0) {
                chbGetRestOfBill.setSelected(false);
            }
        });

        txtSenderName.setDisable(true);
        txtReceiverName.setDisable(true);

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtTotalBill,
                cbpSenderBankAccount,
                cbpReceiverBankAccount);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill);
    }

    private void initDataRCTITransferPopup() {
        //Bank Account (Sender)
        TableView<TblBankAccount> tableSenderBankAccount = new TableView<>();

        TableColumn<TblBankAccount, String> senderBankName = new TableColumn("Bank");
        senderBankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
        senderBankName.setMinWidth(140);

        TableColumn<TblBankAccount, String> senderBankAccount = new TableColumn("Nomor Rekening");
        senderBankAccount.setCellValueFactory(cellData -> cellData.getValue().codeBankAccountProperty());
        senderBankAccount.setMinWidth(140);

        TableColumn<TblBankAccount, String> senderBankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        senderBankAccountHolderName.setCellValueFactory(cellData -> cellData.getValue().bankAccountHolderNameProperty());
        senderBankAccountHolderName.setMinWidth(200);

        tableSenderBankAccount.getColumns().addAll(senderBankName, senderBankAccount, senderBankAccountHolderName);

        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataCustomerBankAccount());

        cbpSenderBankAccount = new JFXCComboBoxTablePopup<>(
                TblBankAccount.class, tableSenderBankAccount, senderBankAccountItems, "", "Nomor Rekening (Pengirim) *", true, 500, 250
        );

        //Bank Account (Receiver)
        TableView<TblBankAccount> tableReceiverBankAccount = new TableView<>();

        TableColumn<TblBankAccount, String> receiverBankName = new TableColumn("Bank");
        receiverBankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
        receiverBankName.setMinWidth(140);

        TableColumn<TblBankAccount, String> receiverBankAccount = new TableColumn("Nomor Rekening");
        receiverBankAccount.setCellValueFactory(cellData -> cellData.getValue().codeBankAccountProperty());
        receiverBankAccount.setMinWidth(140);

        TableColumn<TblBankAccount, String> receiverBankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        receiverBankAccountHolderName.setCellValueFactory(cellData -> cellData.getValue().bankAccountHolderNameProperty());
        receiverBankAccountHolderName.setMinWidth(200);

        tableReceiverBankAccount.getColumns().addAll(receiverBankName, receiverBankAccount, receiverBankAccountHolderName);

        ObservableList<TblBankAccount> receiverBankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());

        cbpReceiverBankAccount = new JFXCComboBoxTablePopup<>(
                TblBankAccount.class, tableReceiverBankAccount, receiverBankAccountItems, "", "Nomor Rekening (Penerima) *", true, 500, 250
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpSenderBankAccount, 0.0);
        AnchorPane.setLeftAnchor(cbpSenderBankAccount, 0.0);
        AnchorPane.setRightAnchor(cbpSenderBankAccount, 0.0);
        AnchorPane.setTopAnchor(cbpSenderBankAccount, 0.0);
        ancSenderBankAccount.getChildren().clear();
        ancSenderBankAccount.getChildren().add(cbpSenderBankAccount);
        AnchorPane.setBottomAnchor(cbpSenderBankAccount, 0.0);
        AnchorPane.setLeftAnchor(cbpReceiverBankAccount, 0.0);
        AnchorPane.setRightAnchor(cbpReceiverBankAccount, 0.0);
        AnchorPane.setTopAnchor(cbpReceiverBankAccount, 0.0);
        ancReceiverBankAccount.getChildren().clear();
        ancReceiverBankAccount.getChildren().add(cbpReceiverBankAccount);

//        gpRCTITransfer.add(cbpSenderBankAccount, 0, 1);
//        gpRCTITransfer.add(cbpReceiverBankAccount, 0, 3);
    }

    private List<TblBankAccount> loadAllDataCustomerBankAccount() {
        List<TblCustomerBankAccount> cbaList = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getAllDataCustomerBankAccountByIDCustomer(rcTransactionInputDetailController.getParentController().getParentController().selectedData.getTblCustomer().getIdcustomer());
        List<TblBankAccount> list = new ArrayList<>();
        for (TblCustomerBankAccount cbbaData : cbaList) {
            //data bank account
            TblBankAccount data = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
            //data bank in data bank account
            data.setTblBank(rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getBank(data.getTblBank().getIdbank()));
            //add data bank account to list
            list.add(data);
        }
        return list;
    }

    private List<TblBankAccount> loadAllDataHotelBankAccount() {
        List<TblCompanyBalanceBankAccount> cbbaList = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getAllDataCompanyBalanceBankAccount((long) 1);  //hotel baalnce = '1'
        List<TblBankAccount> list = new ArrayList<>();
        for (TblCompanyBalanceBankAccount cbbaData : cbbaList) {
            //data bank account
            TblBankAccount data = rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
            //data bank in data bank account
            data.setTblBank(rcTransactionInputDetailController.getParentController().getParentController().getParentController().getServiceRV2().getBank(data.getTblBank().getIdbank()));
            //add data bank account to list
            list.add(data);
        }
        return list;
    }

    private void refreshDataPopup() {
        //Sender - Bank Account (Customer)
        refreshCustomerBankAccountDataPopup();

        //Receiver - Bank Account (Hotel : CompanyBalance(Kas Besar))
        ObservableList<TblBankAccount> receiverBankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
        cbpReceiverBankAccount.setItems(receiverBankAccountItems);
    }

    public void refreshCustomerBankAccountDataPopup() {
        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataCustomerBankAccount());
        cbpSenderBankAccount.setItems(senderBankAccountItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        Bindings.bindBidirectional(txtTotalBill.textProperty(), rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtSenderName.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithTransfer.senderNameProperty());
        txtReceiverName.textProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithTransfer.receiverNameProperty());

        cbpSenderBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithTransfer.setSenderName(newVal.getBankAccountHolderName());
            }
        });
        cbpReceiverBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithTransfer.setReceiverName(newVal.getBankAccountHolderName());
            }
        });

        cbpSenderBankAccount.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithTransfer.tblBankAccountBySenderBankAccountProperty());
        cbpReceiverBankAccount.valueProperty().bindBidirectional(rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransactionWithTransfer.tblBankAccountByReceiverBankAccountProperty());

        cbpSenderBankAccount.hide();
        cbpReceiverBankAccount.hide();
    }

    private void dataRCTITransferSaveHandle() {
        if (checkDataInputDataReservationPayment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcTransactionInputDetailController.getParentController().dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //save data transaction & close dialog data rc-transaction detail
                rcTransactionInputDetailController.getParentController().saveDataTransaction();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcTransactionInputDetailController.getParentController().dialogStage);
        }
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationPayment() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtTotalBill.getText() == null
                || txtTotalBill.getText().equals("")
                || txtTotalBill.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Nominal Transaksi : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (rcTransactionInputDetailController.getParentController().getParentController().selectedDataTransaction.getUnitNominal()
                        .compareTo((rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                                .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn()))) == 1) {
                    dataInput = false;
                    errDataInput += "Nominal Transaksi : Nilai tidak dapat lebih besar dari '" + (rcTransactionInputDetailController.getParentController().getParentController().calculationTotalRestOfBill()
                            .add(rcTransactionInputDetailController.getParentController().getSelectedDataTransactionNominalMustBeReturn())) + "' \n";
                }
            }
        }
        if (txtSenderName.getText() == null || txtSenderName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpSenderBankAccount.getValue() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Pengirim) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtReceiverName.getText() == null || txtReceiverName.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Nama (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (cbpReceiverBankAccount.getValue() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Penerima) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * DATA CUSTOMER BANK ACCOUNT (ADD - HANDLER)
     */
    public TblCustomerBankAccount selectedDataCustomerBankAccount;

    public Stage dialogStage;

    private void dataCustomerBankAccountAddHandle() {
        selectedDataCustomerBankAccount = new TblCustomerBankAccount();
        selectedDataCustomerBankAccount.setTblCustomer(rcTransactionInputDetailController.getParentController().getParentController().selectedData.getTblCustomer());
        selectedDataCustomerBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data customer bank account
        showCustomerBankAccountDialog();
    }

    private void showCustomerBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_check_fd/rctid/RCTITransferCustomerBankAccountInputDialog.fxml"));

            RCTITransferCustomerBankAccountInputController controller = new RCTITransferCustomerBankAccountInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rcTransactionInputDetailController.getParentController().dialogStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataRCTITransfer();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public RCTITransferController(RCTransactionInputDetailController parentController) {
        rcTransactionInputDetailController = parentController;
    }

    private final RCTransactionInputDetailController rcTransactionInputDetailController;

    public RCTransactionInputDetailController getParentController() {
        return rcTransactionInputDetailController;
    }
    
}
