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
import hotelfx.persistence.model.TblPartnerBankAccount;
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
public class RCRRCTIGuaranteePaymentPartnerBankAccountInputController implements Initializable {

    @FXML
    private AnchorPane ancFormPartnerBankAccount;

    @FXML
    private GridPane gpFormDataPartnerBankAccount;

    @FXML
    private JFXTextField txtCodePartnerBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private AnchorPane ancBankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private JFXButton btnSavePartnerBankAccount;

    @FXML
    private JFXButton btnCancelPartnerBankAccount;

    private void initFormDataPartnerBankAccount() {
        initDataPartnerBankAccountPopup();

        btnSavePartnerBankAccount.setTooltip(new Tooltip("Simpan (Data Nomor Rekening)"));
        btnSavePartnerBankAccount.setOnAction((e) -> {
            dataPartnerBankAccountSaveHandle();
        });

        btnCancelPartnerBankAccount.setTooltip(new Tooltip("Batal"));
        btnCancelPartnerBankAccount.setOnAction((e) -> {
            dataPartnerBankAccountCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodePartnerBankAccount,
                cbpBank,
                txtBankAccountHolderName);
    }

    private void initDataPartnerBankAccountPopup() {
        //Bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcrrctiGuaranteePaymentController.getParentController().getParentController().getParentController().getFReservationManager().getAllDataBank());

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
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(rcrrctiGuaranteePaymentController.getParentController().getParentController().getParentController().getFReservationManager().getAllDataBank());
        cbpBank.setItems(bankItems);

    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtCodePartnerBankAccount.textProperty().bindBidirectional(rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.getTblBankAccount().bankAccountHolderNameProperty());

        cbpBank.valueProperty().bindBidirectional(rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.getTblBankAccount().tblBankProperty());

        cbpBank.hide();
    }

    private void dataPartnerBankAccountSaveHandle() {
        if (checkDataInputDataPartnerBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", rcrrctiGuaranteePaymentController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblPartnerBankAccount dummySelectedData = new TblPartnerBankAccount(rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount);
                dummySelectedData.setTblPartner(dummySelectedData.getTblPartner());
                dummySelectedData.setTblBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccount()));
                if (rcrrctiGuaranteePaymentController.getParentController().getParentController().getParentController().getFReservationManager().insertDataPartnerBankAccount(dummySelectedData) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", rcrrctiGuaranteePaymentController.dialogStage);
                    //refresh data partner - bank account popup
                    rcrrctiGuaranteePaymentController.refreshPartnerBankAccountDataPopup();
                    //reload and set data on field
                    rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount = rcrrctiGuaranteePaymentController.getParentController().getParentController().getParentController().getFReservationManager().getDataPartnerBankAccount(dummySelectedData.getIdrelation());
                    rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.setTblPartner(rcrrctiGuaranteePaymentController.getParentController().getParentController().getParentController().getFReservationManager().getPartner(rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.getTblPartner().getIdpartner()));
                    rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.setTblBankAccount(rcrrctiGuaranteePaymentController.getParentController().getParentController().getParentController().getFReservationManager().getBankAccount(rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.getTblBankAccount().getIdbankAccount()));
                    rcrrctiGuaranteePaymentController.getParentController().getParentController().tempSelectedDataTransactionWithGuaranteePayment.setTblBankAccountBySenderBankAccount(rcrrctiGuaranteePaymentController.selectedDataPartnerBankAccount.getTblBankAccount());
                    //close form data partner bank account
                    rcrrctiGuaranteePaymentController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(rcrrctiGuaranteePaymentController.getParentController().getParentController().getParentController().getFReservationManager().getErrorMessage(), rcrrctiGuaranteePaymentController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, rcrrctiGuaranteePaymentController.dialogStage);
        }
    }

    private void dataPartnerBankAccountCancelHandle() {
        //close form data partner - bank account
        rcrrctiGuaranteePaymentController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataPartnerBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodePartnerBankAccount.getText() == null || txtCodePartnerBankAccount.getText().equals("")) {
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
        initFormDataPartnerBankAccount();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCRRCTIGuaranteePaymentPartnerBankAccountInputController(RCRRCTIGuaranteePaymentController parentController) {
        rcrrctiGuaranteePaymentController = parentController;
    }

    private final RCRRCTIGuaranteePaymentController rcrrctiGuaranteePaymentController;

}
