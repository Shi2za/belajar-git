/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefBankCardType;
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
public class BankCardController implements Initializable {

    @FXML
    private AnchorPane ancFormBankCard;

    @FXML
    private GridPane gpFormDataBankCard;

    @FXML
    private JFXTextField txtBankCardName;

    @FXML
    private JFXTextField txtBankCardClassName;

    @FXML
    private AnchorPane ancBankCardTypeLayout;
    private JFXCComboBoxTablePopup<RefBankCardType> cbpBankCardType;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataBankCard() {
        initDataBankCardPopup();
        
        btnSave.setTooltip(new Tooltip("Simpan (Data Kartu Bank)"));
        btnSave.setOnAction((e) -> {
            dataBankCardSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataBankCardCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtBankCardName,
                txtBankCardClassName,
                cbpBankCardType);
    }

    private void initDataBankCardPopup() {
        //Bank Card Type
        TableView<RefBankCardType> tableBankCardType = new TableView<>();

        TableColumn<RefBankCardType, String> bankCardTypeName = new TableColumn<>("Jenis Kartu");
        bankCardTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        bankCardTypeName.setMinWidth(140);

        tableBankCardType.getColumns().addAll(bankCardTypeName);

        ObservableList<RefBankCardType> bankCardTypeItems = FXCollections.observableArrayList(bankController.getService().getAllDataBankCardType());

        cbpBankCardType = new JFXCComboBoxTablePopup<>(
                RefBankCardType.class, tableBankCardType, bankCardTypeItems, "", "Jenis Kartu *", true, 200, 200
        );

        //attached to grid-pane
        ancBankCardTypeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBankCardType, 0.0);
        AnchorPane.setLeftAnchor(cbpBankCardType, 0.0);
        AnchorPane.setRightAnchor(cbpBankCardType, 0.0);
        AnchorPane.setTopAnchor(cbpBankCardType, 0.0);
        ancBankCardTypeLayout.getChildren().add(cbpBankCardType);
    }

    private void refreshDataPopup() {
        //Bank Card Type
        ObservableList<RefBankCardType> bankCardTypeItems = FXCollections.observableArrayList(bankController.getService().getAllDataBankCardType());
        cbpBankCardType.setItems(bankCardTypeItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtBankCardName.textProperty().bindBidirectional(bankController.selectedDataBankCard.bankCardNameProperty());
        txtBankCardClassName.textProperty().bindBidirectional(bankController.selectedDataBankCard.bankCardClassNameProperty());

        cbpBankCardType.valueProperty().bindBidirectional(bankController.selectedDataBankCard.refBankCardTypeProperty());

        cbpBankCardType.hide();
    }

    private void dataBankCardSaveHandle() {
        if (checkDataInputDataBankCard()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", bankController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (bankController.dataInputBankCardStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", bankController.dialogStage);
                        bankController.tableDataBankCard.getTableView().getItems().add(bankController.selectedDataBankCard);
                        //close form data bank card
                        bankController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", bankController.dialogStage);
                        bankController.tableDataBankCard.getTableView().getItems().set(bankController.tableDataBankCard.getTableView().getSelectionModel().getSelectedIndex(),
                                bankController.selectedDataBankCard);
                        //close form data bank card
                        bankController.dialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, bankController.dialogStage);
        }
    }

    private void dataBankCardCancelHandle() {
        //close form data bank card
        bankController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataBankCard() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtBankCardName.getText() == null || txtBankCardName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Kartu Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtBankCardClassName.getText() == null || txtBankCardClassName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Tipe Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBankCardType.getValue() == null) {
            dataInput = false;
            errDataInput += "Jenis Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataBankCard();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public BankCardController(BankController parentController) {
        bankController = parentController;
    }

    private final BankController bankController;

}
