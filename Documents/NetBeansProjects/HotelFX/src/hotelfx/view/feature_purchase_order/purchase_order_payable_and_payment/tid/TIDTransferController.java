/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.tid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblSupplierBankAccount;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.HotelFinanceTransactionInputController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
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
public class TIDTransferController implements Initializable {

    @FXML
    private GridPane gpTIDTransfer;

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
    private JFXButton btnAddSupplierBankAccount;

    private void initFormDataTIDTransfer() {
        initDataTIDTransferPopup();
        
        btnAddSupplierBankAccount.setTooltip(new Tooltip("Tambahkan (Nomor Rekening Supplier)"));
        btnAddSupplierBankAccount.setOnAction((e) -> {
            dataSupplierBankAccountAddHandle();
        });
        
        txtSenderName.setDisable(true);
        txtReceiverName.setDisable(true);
        
        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpSenderBankAccount,
                cbpReceiverBankAccount);
    }

    private void initDataTIDTransferPopup() {
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
        
        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());

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
        
        ObservableList<TblBankAccount> receiverBankAccountItems = FXCollections.observableArrayList(loadAllDataSupplierBankAccount());
        
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
    }

    private List<TblBankAccount> loadAllDataSupplierBankAccount() {
        List<TblSupplierBankAccount> cbaList = hotelFinanceTransactionInputController.getParentController().getService().getAllDataSupplierBankAccountByIDSupplier(hotelFinanceTransactionInputController.getParentController().selectedData.getIdsupplier());
        List<TblBankAccount> list = new ArrayList<>();
        for (TblSupplierBankAccount cbbaData : cbaList) {
            //data bank account
            TblBankAccount data = hotelFinanceTransactionInputController.getParentController().getService().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
            //data bank in data bank account
            data.setTblBank(hotelFinanceTransactionInputController.getParentController().getService().getDataBank(data.getTblBank().getIdbank()));
            //add data bank account to list
            list.add(data);
        }
        return list;
    }

    private List<TblBankAccount> loadAllDataHotelBankAccount() {
        List<TblCompanyBalanceBankAccount> cbbaList = hotelFinanceTransactionInputController.getParentController().getService().getAllDataCompanyBalanceBankAccount((long) 1);  //hotel baalnce = '1'
        List<TblBankAccount> list = new ArrayList<>();
        for (TblCompanyBalanceBankAccount cbbaData : cbbaList) {
            //data bank account
            TblBankAccount data = hotelFinanceTransactionInputController.getParentController().getService().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
            //data bank in data bank account
            data.setTblBank(hotelFinanceTransactionInputController.getParentController().getService().getDataBank(data.getTblBank().getIdbank()));
            //add data bank account to list
            list.add(data);
        }
        return list;
    }

    private void refreshDataPopup() {
        //Receiver - Bank Account (Supplier)
        refreshSupplierBankAccountDataPopup();

        //Sender - Bank Account (Hotel : CompanyBalance(Kas Besar))
        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
        cbpSenderBankAccount.setItems(senderBankAccountItems);
    }

    public void refreshSupplierBankAccountDataPopup() {
        ObservableList<TblBankAccount> receiverBankAccountItems = FXCollections.observableArrayList(loadAllDataSupplierBankAccount());
        cbpReceiverBankAccount.setItems(receiverBankAccountItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtSenderName.textProperty().bindBidirectional(hotelFinanceTransactionInputController.getParentController().selectedDataHFTWithTransfer.senderNameProperty());
        txtReceiverName.textProperty().bindBidirectional(hotelFinanceTransactionInputController.getParentController().selectedDataHFTWithTransfer.receiverNameProperty());

        cbpSenderBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                hotelFinanceTransactionInputController.getParentController().selectedDataHFTWithTransfer.setSenderName(newVal.getBankAccountHolderName());
            }
        });
        cbpReceiverBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                hotelFinanceTransactionInputController.getParentController().selectedDataHFTWithTransfer.setReceiverName(newVal.getBankAccountHolderName());
            }
        });

        cbpSenderBankAccount.valueProperty().bindBidirectional(hotelFinanceTransactionInputController.getParentController().selectedDataHFTWithTransfer.tblBankAccountBySenderBankAccountProperty());
        cbpReceiverBankAccount.valueProperty().bindBidirectional(hotelFinanceTransactionInputController.getParentController().selectedDataHFTWithTransfer.tblBankAccountByReceiverBankAccountProperty());

        cbpSenderBankAccount.hide();
        cbpReceiverBankAccount.hide();
    }

    /**
     * DATA SUPPLIER BANK ACCOUNT (ADD - HANDLER)
     */
    public TblSupplierBankAccount selectedDataSupplierBankAccount;

    public Stage dialogStage;

    private void dataSupplierBankAccountAddHandle() {
        selectedDataSupplierBankAccount = new TblSupplierBankAccount();
        selectedDataSupplierBankAccount.setTblSupplier(hotelFinanceTransactionInputController.getParentController().selectedData);
        selectedDataSupplierBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data suplier bank account
        showSupplierBankAccountDialog();
    }

    private void showSupplierBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/tid/TIDTransferSupplierBankAccountInputDialog.fxml"));

            TIDTransferSupplierBankAccountInputController controller = new TIDTransferSupplierBankAccountInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(hotelFinanceTransactionInputController.getParentController().dialogStageDetal);

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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataTIDTransfer();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public TIDTransferController(HotelFinanceTransactionInputController parentController) {
        hotelFinanceTransactionInputController = parentController;
    }

    private final HotelFinanceTransactionInputController hotelFinanceTransactionInputController;

    public HotelFinanceTransactionInputController getParentController() {
        return hotelFinanceTransactionInputController;
    }

}
