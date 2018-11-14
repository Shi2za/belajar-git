/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank_edc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
public class EDCNetworkCardInputController implements Initializable {

    @FXML
    private AnchorPane ancFormEDCNetworkCard;

    @FXML
    private GridPane gpFormDataEDCNetworkCard;

    @FXML
    private JFXTextField txtMDRPercentage;

    @FXML
    private AnchorPane ancBankNetworkCardLayout;
    private JFXCComboBoxTablePopup<TblBankNetworkCard> cbpBankNetworkCard;

    @FXML
    private AnchorPane ancBankCardTypeLayout;
    private JFXCComboBoxTablePopup<RefBankCardType> cbpBankCardType;

    @FXML
    private JFXCheckBox chbOnUs;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataEDCNetworkCard() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data MDR)"));
        btnSave.setOnAction((e) -> {
            dataSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpBankNetworkCard,
                cbpBankCardType,
                chbOnUs,
                txtMDRPercentage);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtMDRPercentage);
    }

    private void initDataPopup() {
        //Bank Network Card
        TableView<TblBankNetworkCard> tableBankNetworkCard = new TableView<>();

        TableColumn<TblBankNetworkCard, String> bankNetworkCardName = new TableColumn<>("Jaringan Kartu");
        bankNetworkCardName.setCellValueFactory(cellData -> cellData.getValue().networkCardNameProperty());
        bankNetworkCardName.setMinWidth(140);

        tableBankNetworkCard.getColumns().addAll(bankNetworkCardName);

        ObservableList<TblBankNetworkCard> bankNetworkCardItems = FXCollections.observableArrayList(loadAllDataBankNetworkCard());

        cbpBankNetworkCard = new JFXCComboBoxTablePopup<>(
                TblBankNetworkCard.class, tableBankNetworkCard, bankNetworkCardItems, "", "Jaringan Kartu *", true, 160, 200
        );

        //Bank Card Type
        TableView<RefBankCardType> tableBankCardType = new TableView<>();

        TableColumn<RefBankCardType, String> bankCardTypeName = new TableColumn<>("Tipe Kartu");
        bankCardTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        bankCardTypeName.setMinWidth(140);

        tableBankCardType.getColumns().addAll(bankCardTypeName);

        ObservableList<RefBankCardType> bankCardTypeItems = FXCollections.observableArrayList(loadAllDataBankCardType());

        cbpBankCardType = new JFXCComboBoxTablePopup<>(
                RefBankCardType.class, tableBankCardType, bankCardTypeItems, "", "Tipe Kartu *", true, 160, 200
        );

        //attached to grid-pane
        ancBankNetworkCardLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBankNetworkCard, 0.0);
        AnchorPane.setLeftAnchor(cbpBankNetworkCard, 0.0);
        AnchorPane.setRightAnchor(cbpBankNetworkCard, 0.0);
        AnchorPane.setTopAnchor(cbpBankNetworkCard, 0.0);
        ancBankNetworkCardLayout.getChildren().add(cbpBankNetworkCard);

        ancBankCardTypeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBankCardType, 0.0);
        AnchorPane.setLeftAnchor(cbpBankCardType, 0.0);
        AnchorPane.setRightAnchor(cbpBankCardType, 0.0);
        AnchorPane.setTopAnchor(cbpBankCardType, 0.0);
        ancBankCardTypeLayout.getChildren().add(cbpBankCardType);
    }

    private List<TblBankNetworkCard> loadAllDataBankNetworkCard() {
        List<TblBankNetworkCard> list = bankEDCController.getService().getAllDataBankNetworkCard();
        //remove data has been used
        for (int i = list.size() - 1; i > -1; i--) {
            for (TblBankEdcBankNetworkCard data : (List<TblBankEdcBankNetworkCard>) bankEDCController.tableDataEDCNetworkCard.getTableView().getItems()) {
                if (list.get(i).getIdnetworkCard() == data.getTblBankNetworkCard().getIdnetworkCard()) {
                    if (bankEDCController.selectedDataEDCNetworkCard.getTblBankNetworkCard() == null
                            || data.getTblBankNetworkCard().getIdnetworkCard() != bankEDCController.selectedDataEDCNetworkCard.getTblBankNetworkCard().getIdnetworkCard()) {
                        list.remove(i);
                    }
                    break;
                }
            }
        }
        return list;
    }

    private List<RefBankCardType> loadAllDataBankCardType() {
        List<RefBankCardType> list = bankEDCController.getService().getAllDataBankCardType();
        //...
        return list;
    }

    private void refreshDataPopup() {
        //Bank Network Card
        ObservableList<TblBankNetworkCard> bankNetworkCardItems = FXCollections.observableArrayList(loadAllDataBankNetworkCard());
        cbpBankNetworkCard.setItems(bankNetworkCardItems);
        //Bank Card Type
        ObservableList<RefBankCardType> bankCardTypeItems = FXCollections.observableArrayList(loadAllDataBankCardType());
        cbpBankCardType.setItems(bankCardTypeItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        Bindings.bindBidirectional(txtMDRPercentage.textProperty(), bankEDCController.selectedDataEDCNetworkCard.networkMdrProperty(), new ClassFormatter.CBigDecimalStringConverter());

        chbOnUs.selectedProperty().bindBidirectional(bankEDCController.selectedDataEDCNetworkCard.onUsProperty());

        cbpBankNetworkCard.valueProperty().bindBidirectional(bankEDCController.selectedDataEDCNetworkCard.tblBankNetworkCardProperty());
        cbpBankCardType.valueProperty().bindBidirectional(bankEDCController.selectedDataEDCNetworkCard.refBankCardTypeProperty());

        cbpBankNetworkCard.hide();
        cbpBankCardType.hide();
    }

    private void dataSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", bankEDCController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (bankEDCController.dataInputEDCNetworkCardStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", bankEDCController.dialogStage);
                        bankEDCController.tableDataEDCNetworkCard.getTableView().getItems().add(bankEDCController.selectedDataEDCNetworkCard);
                        //close form data edc - network card
                        bankEDCController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", bankEDCController.dialogStage);
                        bankEDCController.tableDataEDCNetworkCard.getTableView().getItems().set(bankEDCController.tableDataEDCNetworkCard.getTableView().getSelectionModel().getSelectedIndex(),
                                bankEDCController.selectedDataEDCNetworkCard);
                        //close form data edc - network card
                        bankEDCController.dialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, bankEDCController.dialogStage);
        }
    }

    private void dataCancelHandle() {
        //close form data edc - network card
        bankEDCController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInput() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpBankNetworkCard.getValue() == null) {
            dataInput = false;
            errDataInput += "Jaringan Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBankCardType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtMDRPercentage.getText() == null
                || txtMDRPercentage.getText().equals("")
                || txtMDRPercentage.getText().equals("-")) {
            dataInput = false;
            errDataInput += "MDR(%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (bankEDCController.selectedDataEDCNetworkCard.getNetworkMdr()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "MDR(%) : Tidak boleh kurang dari '0' \n";
            }
        }
//        if (txtMaxMDRNominal.getText() == null 
//                || txtMaxMDRNominal.getText().equals("")
//                || txtMaxMDRNominal.getText().equals("-")) {
//            dataInput = false;
//            errDataInput += "Max. MDR Nominal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        } else {
//            if (bankEDCController.selectedDataEDCNetworkCard.getMaxNetworkMdrnominal()
//                    .compareTo(new BigDecimal("0")) == -1) {
//                dataInput = false;
//                errDataInput += "Max. MDR Nominal : Tidak boleh kurang dari '0' \n";
//            }
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
        initFormDataEDCNetworkCard();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public EDCNetworkCardInputController(BankEDCController parentController) {
        bankEDCController = parentController;
    }

    private final BankEDCController bankEDCController;

}
