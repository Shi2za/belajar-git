/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.travel_agent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
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
public class TravelAgentBankAccountController implements Initializable {

    @FXML
    private AnchorPane ancFormPartnerBankAccount;

    @FXML
    private GridPane gpFormDataPartnerBankAccount;

    @FXML
    private JFXTextField txtCodeBankAccount;

    @FXML
    private JFXTextField txtBankAccountHolderName;

    @FXML
    private AnchorPane ancBankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataPartnerBankAccount() {
        initDataPartnerBankAccountPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Nomor Rekening Bank)"));
        btnSave.setOnAction((e) -> {
            dataPartnerBankAccountSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataPartnerBankAccountCancelHandle();
        });
        
        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeBankAccount,
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

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(partnerController.getService().getAllDataBank());

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
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(partnerController.getService().getAllDataBank());
        cbpBank.setItems(bankItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtCodeBankAccount.textProperty().bindBidirectional(partnerController.selectedDataPartnerBankAccount.getTblBankAccount().codeBankAccountProperty());
        txtBankAccountHolderName.textProperty().bindBidirectional(partnerController.selectedDataPartnerBankAccount.getTblBankAccount().bankAccountHolderNameProperty());

        cbpBank.valueProperty().bindBidirectional(partnerController.selectedDataPartnerBankAccount.getTblBankAccount().tblBankProperty());

        cbpBank.hide();
    }

    private void dataPartnerBankAccountSaveHandle() {
        if (checkDataInputDataPartnerBankAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", partnerController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (partnerController.dataInputPartnerBankAccountStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", partnerController.dialogStage);
                        partnerController.tableDataPartnerBankAccount.getTableView().getItems().add(partnerController.selectedDataPartnerBankAccount);
                        //close form data partner bank account
                        partnerController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", partnerController.dialogStage);
                        partnerController.tableDataPartnerBankAccount.getTableView().getItems().set(partnerController.tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedIndex(),
                                partnerController.selectedDataPartnerBankAccount);
                        //close form data partner bank account
                        partnerController.dialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, partnerController.dialogStage);
        }
    }

    private void dataPartnerBankAccountCancelHandle() {
        //close form data partner bank account
        partnerController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataPartnerBankAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeBankAccount.getText() == null || txtCodeBankAccount.getText().equals("")) {
            dataInput = false;
            errDataInput += "\"Nomor Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBank.getValue() == null) {
            dataInput = false;
            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtBankAccountHolderName.getText() == null || txtBankAccountHolderName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Pemegang Rekenig : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpBankAccountStatus.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Bank Account Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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

    public TravelAgentBankAccountController(TravelAgentDataController parentController) {
        partnerController = parentController;
    }

    private final TravelAgentDataController partnerController;

}
