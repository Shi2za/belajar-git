/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rcr.rctid;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCustomerBankAccount;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCRRCTITransferCustomerBankAccountInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCustomerBankAccount;

    @FXML
    private GridPane gpFormDataCustomerBankAccount;

    @FXML
    private JFXTextField txtCodeCustomerBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private AnchorPane ancBankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private JFXButton btnSaveCustomerBankAccount;

    @FXML
    private JFXButton btnCancelCustomerBankAccount;

    private void initFormDataCustomerBankAccount() {
        initDataCustomerBankAccountPopup();

        btnSaveCustomerBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening)"));
        btnSaveCustomerBankAccount.setOnAction((e) -> {
            dataCustomerBankAccountSaveHandle();
        });

        btnCancelCustomerBankAccount.setTooltip(new Tooltip("Batal"));
        btnCancelCustomerBankAccount.setOnAction((e) -> {
            dataCustomerBankAccountCancelHandle();
        });
        
        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeCustomerBankAccount,
                cbpBank,
                txtBankAccountHolderName);
    }

    private void initDataCustomerBankAccountPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcrrctiTransferController.getParentController().getParentController().getParentController().getFReservationManager().getAllDataBank());

        cbpBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableBank, bankItems, "", "Bank *", true, 200, 200
        );

        //attached to grid-pane
        ancBankLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBank, 0.0);
        AnchorPane.setLeftAnchor(cbpBank, 0.0);
        AnchorPane.setRightAnchor(cbpBank, 0.0);
        AnchorPane.setTopAnchor(cbpBank, 0.0);
        ancBankLayout.getChildren().add(cbpBank);
    }

    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcrrctiTransferController.getParentController().getParentController().getParentController().getFReservationManager().getAllDataBank());
        cbpBank.setItems(bankItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtCodeCustomerBankAccount.textProperty().bindBidirectional(rcrrctiTransferController.selectedDataCustomerBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(rcrrctiTransferController.selectedDataCustomerBankAccount.getTblBankAccount().bankAccountHolderNameProperty());

        cbpBank.valueProperty().bindBidirectional(rcrrctiTransferController.selectedDataCustomerBankAccount.getTblBankAccount().tblBankProperty());

        cbpBank.hide();
    }

    private void dataCustomerBankAccountSaveHandle() {
        if (checkDataInputDataCustomerBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcrrctiTransferController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblCustomerBankAccount dummySelectedData = new TblCustomerBankAccount(rcrrctiTransferController.selectedDataCustomerBankAccount);
                dummySelectedData.setTblCustomer(dummySelectedData.getTblCustomer());
                dummySelectedData.setTblBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccount()));
                if (rcrrctiTransferController.getParentController().getParentController().getParentController().getFReservationManager().insertDataCustomerBankAccount(dummySelectedData) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", rcrrctiTransferController.dialogStage);
                    //refresh data customer - bank account popup
                    rcrrctiTransferController.refreshCustomerBankAccountDataPopup();
                    //reload and set data on field
                    rcrrctiTransferController.selectedDataCustomerBankAccount = rcrrctiTransferController.getParentController().getParentController().getParentController().getFReservationManager().getDataCustomerBankAccount(dummySelectedData.getIdrelation());
                    rcrrctiTransferController.selectedDataCustomerBankAccount.setTblCustomer(rcrrctiTransferController.getParentController().getParentController().getParentController().getFReservationManager().getCustomer(rcrrctiTransferController.selectedDataCustomerBankAccount.getTblCustomer().getIdcustomer()));
                    rcrrctiTransferController.selectedDataCustomerBankAccount.setTblBankAccount(rcrrctiTransferController.getParentController().getParentController().getParentController().getFReservationManager().getBankAccount(rcrrctiTransferController.selectedDataCustomerBankAccount.getTblBankAccount().getIdbankAccount()));
                    rcrrctiTransferController.getParentController().getParentController().tempSelectedDataTransactionWithTransfer.setTblBankAccountBySenderBankAccount(rcrrctiTransferController.selectedDataCustomerBankAccount.getTblBankAccount());
                    //close form data customer bank account
                    rcrrctiTransferController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(rcrrctiTransferController.getParentController().getParentController().getParentController().getFReservationManager().getErrorMessage(), rcrrctiTransferController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcrrctiTransferController.dialogStage);
        }
    }

    private void dataCustomerBankAccountCancelHandle() {
        //close form data customer bank account
        rcrrctiTransferController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCustomerBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeCustomerBankAccount.getText() == null || txtCodeCustomerBankAccount.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBank.getValue() == null) {
            dataInput = false;
            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtBankAccountHolderName.getText() == null || txtBankAccountHolderName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Pemegang Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpBankAccountStatus.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Bank Account (Status) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
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
        initFormDataCustomerBankAccount();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCRRCTITransferCustomerBankAccountInputController(RCRRCTITransferController parentController) {
        rcrrctiTransferController = parentController;
    }

    private final RCRRCTITransferController rcrrctiTransferController;

}
