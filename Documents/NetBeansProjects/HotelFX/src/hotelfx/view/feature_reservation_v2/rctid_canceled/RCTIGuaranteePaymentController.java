/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2.rctid_canceled;

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
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPartnerBankAccount;
import hotelfx.view.feature_reservation_v2.ReservationCanceledInputController;
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
public class RCTIGuaranteePaymentController implements Initializable {

    @FXML
    private GridPane gpRCTIGuaranteePayment;
    
    @FXML
    private JFXTextField txtTotalBill;
    
    @FXML
    private JFXTextField txtSenderName;
    
    @FXML
    private JFXTextField txtReceiverName;
    
    @FXML
    private JFXCheckBox chbToPartnerDebt;
    
    @FXML
    private AnchorPane ancSenderBankAccount;
    
    @FXML
    private AnchorPane ancReceiverBankAccount;
    
    private JFXCComboBoxTablePopup<TblPartner> cbpPartner;
    
    private JFXCComboBoxTablePopup<TblBankAccount> cbpSenderBankAccount;
    
    private JFXCComboBoxTablePopup<TblBankAccount> cbpReceiverBankAccount;
    
    @FXML
    private JFXButton btnAddPartnerBankAccount;    
    
    private void initFormDataRCTIGuaranteePayment() {
        initDataRCTIPartnerPopup();
        
        btnAddPartnerBankAccount.setTooltip(new Tooltip("Tambahkan (Nomor Rekening Partner)"));
        btnAddPartnerBankAccount.setOnAction((e) -> {
            dataPartnerBankAccountAddHandle();
        });

        //set rest of bill
        reservationCanceledInputController.totalBill.addListener((obs, oldVal, newVal) -> {
            reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(newVal);
        });
        reservationCanceledInputController.getParentController().selectedDataTransaction.setUnitNominal(reservationCanceledInputController.totalBill.get());
        
        cbpPartner.setDisable(frptName.equals("Travel Agent"));
        
        chbToPartnerDebt.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtSenderName.setText(null);
                txtSenderName.setVisible(!newVal);
                txtReceiverName.setText(null);
                txtReceiverName.setVisible(!newVal);
                cbpSenderBankAccount.setValue(null);
                cbpSenderBankAccount.setVisible(!newVal);
                if(newVal){
                    cbpReceiverBankAccount.setValue(null);
                }else{
                    cbpReceiverBankAccount.setValue(reservationCanceledInputController.getParentController().getDataDefaultBankAccountForGuestTransaction());
                }
                cbpReceiverBankAccount.setVisible(!newVal);
                btnAddPartnerBankAccount.setVisible(!newVal);
            }
        });
        
        txtSenderName.setDisable(true);
        txtReceiverName.setDisable(true);
        
        initImportantFieldColor();
        
        initNumbericField();
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpPartner,
                cbpSenderBankAccount,
                cbpReceiverBankAccount);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtTotalBill);
    }
    
    private void initDataRCTIPartnerPopup() {
        //Partner
        TableView<TblPartner> tablePartner = new TableView<>();
        
        TableColumn<TblPartner, String> codePartner = new TableColumn<>("ID");
        codePartner.setCellValueFactory(cellData -> cellData.getValue().codePartnerProperty());
        codePartner.setMinWidth(120);
        
        TableColumn<TblPartner, String> partnerName = new TableColumn<>("Partner");
        partnerName.setCellValueFactory(cellData -> cellData.getValue().partnerNameProperty());
        partnerName.setMinWidth(140);
        
        tablePartner.getColumns().addAll(codePartner, partnerName);
        
        ObservableList<TblPartner> partnerItems = FXCollections.observableArrayList();
        
        if (frptName != null) {
            switch (frptName) {
                case "Travel Agent":
                    partnerItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataPartnerTravelAgent());
                    break;
                case "Guarantee Letter (Corporate)":
                    partnerItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataPartnerCorporate());
                    break;
                case "Guarantee Letter (Government)":
                    partnerItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataPartnerGovernment());
                    break;
                default:
                    break;
            }
        }
        
        cbpPartner = new JFXCComboBoxTablePopup<>(
                TblPartner.class, tablePartner, partnerItems, "", "Partner *", true, 300, 300
        );

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
        
        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataPartnerBankAccount());
        
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
        gpRCTIGuaranteePayment.add(cbpPartner, 0, 1);
        AnchorPane.setBottomAnchor(cbpSenderBankAccount, 0.0);
        AnchorPane.setLeftAnchor(cbpSenderBankAccount, 0.0);
        AnchorPane.setRightAnchor(cbpSenderBankAccount, 0.0);
        AnchorPane.setTopAnchor(cbpSenderBankAccount, 0.0);
        ancSenderBankAccount.getChildren().clear();
        ancSenderBankAccount.getChildren().add(cbpSenderBankAccount);
        AnchorPane.setBottomAnchor(cbpReceiverBankAccount, 0.0);
        AnchorPane.setLeftAnchor(cbpReceiverBankAccount, 0.0);
        AnchorPane.setRightAnchor(cbpReceiverBankAccount, 0.0);
        AnchorPane.setTopAnchor(cbpReceiverBankAccount, 0.0);
        ancReceiverBankAccount.getChildren().clear();
        ancReceiverBankAccount.getChildren().add(cbpReceiverBankAccount);
//        gpRCTIGuaranteePayment.add(cbpSenderBankAccount, 0, 2);
//        gpRCTIGuaranteePayment.add(cbpReceiverBankAccount, 0, 4);
    }
    
    private List<TblBankAccount> loadAllDataPartnerBankAccount() {
        List<TblBankAccount> list = new ArrayList<>();
        if (reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.getTblPartner() != null) {
            List<TblPartnerBankAccount> pbaList = reservationCanceledInputController.getParentController().getFReservationManager().getAllDataPartnerBankAccountByIDPartner(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.getTblPartner().getIdpartner());
            for (TblPartnerBankAccount pbbaData : pbaList) {
                //data bank account
                TblBankAccount data = reservationCanceledInputController.getParentController().getFReservationManager().getBankAccount(pbbaData.getTblBankAccount().getIdbankAccount());
                //data bank in data bank account
                data.setTblBank(reservationCanceledInputController.getParentController().getFReservationManager().getBank(data.getTblBank().getIdbank()));
                //add data bank account to list
                list.add(data);
            }
        }
        return list;
    }
    
    private List<TblBankAccount> loadAllDataHotelBankAccount() {
        List<TblCompanyBalanceBankAccount> cbbaList = reservationCanceledInputController.getParentController().getFReservationManager().getAllDataCompanyBalanceBankAccount((long) 1);  //hotel balance = '1'
        List<TblBankAccount> list = new ArrayList<>();
        for (TblCompanyBalanceBankAccount cbbaData : cbbaList) {
            //data bank account
            TblBankAccount data = reservationCanceledInputController.getParentController().getFReservationManager().getBankAccount(cbbaData.getTblBankAccount().getIdbankAccount());
            //data bank in data bank account
            data.setTblBank(reservationCanceledInputController.getParentController().getFReservationManager().getBank(data.getTblBank().getIdbank()));
            //add data bank account to list
            list.add(data);
        }
        return list;
    }
    
    private void refreshDataPopup() {
        //Partner
        ObservableList<TblPartner> partnerItems = FXCollections.observableArrayList();
        
        if (frptName != null) {
            switch (frptName) {
                case "Travel Agent":
                    partnerItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataPartnerTravelAgent());
                    break;
                case "Guarantee Letter (Corporate)":
                    partnerItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataPartnerCorporate());
                    break;
                case "Guarantee Letter (Government)":
                    partnerItems = FXCollections.observableArrayList(reservationCanceledInputController.getParentController().getFReservationManager().getAllDataPartnerGovernment());
                    break;
                default:
                    break;
            }
        }
        cbpPartner.setItems(partnerItems);

        //Sender - Bank Account (Partner)
        refreshPartnerBankAccountDataPopup();

        //Receiver - Bank Account (Hotel : CompanyBalance(Kas Besar))
        ObservableList<TblBankAccount> receiverBankAccountItems = FXCollections.observableArrayList(loadAllDataHotelBankAccount());
        cbpReceiverBankAccount.setItems(receiverBankAccountItems);
    }
    
    public void refreshPartnerBankAccountDataPopup() {
        ObservableList<TblBankAccount> senderBankAccountItems = FXCollections.observableArrayList(loadAllDataPartnerBankAccount());
        cbpSenderBankAccount.setItems(senderBankAccountItems);
    }
    
    private void setSelectedDataToInputForm() {
        refreshDataPopup();
        
        txtSenderName.textProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.senderNameProperty());
        txtReceiverName.textProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.receiverNameProperty());
        
        Bindings.bindBidirectional(txtTotalBill.textProperty(), reservationCanceledInputController.getParentController().selectedDataTransaction.unitNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        
        chbToPartnerDebt.selectedProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.isDebtProperty());
        
        cbpPartner.valueProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.tblPartnerProperty());
        reservationCanceledInputController.getParentController().selectedDataTransactionWithBankCard.tblBankProperty().addListener((obs, oldVal, newVal) -> {
            //Partner - Bank Account
            ObservableList<TblBankAccount> partnerBankAccountItems = FXCollections.observableArrayList(loadAllDataPartnerBankAccount());
            cbpSenderBankAccount.setItems(partnerBankAccountItems);
            cbpSenderBankAccount.setValue(null);
        });
        if (frptName.equals("Travel Agent")) {
            cbpPartner.setValue(reservationCanceledInputController.getParentController().selectedData.getTblPartner());
        }
        
        cbpSenderBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.setSenderName(newVal.getBankAccountHolderName());
            }
        });
        cbpReceiverBankAccount.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.setReceiverName(newVal.getBankAccountHolderName());
            }
        });
        
        cbpSenderBankAccount.valueProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.tblBankAccountBySenderBankAccountProperty());
        cbpReceiverBankAccount.valueProperty().bindBidirectional(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.tblBankAccountByReceiverBankAccountProperty());
        
        cbpPartner.hide();
        cbpSenderBankAccount.hide();
        cbpReceiverBankAccount.hide();
    }

    /**
     * DATA PARTNER BANK ACCOUNT (ADD - HANDLER)
     */
    public TblPartnerBankAccount selectedDataPartnerBankAccount;
    
    public Stage dialogStage;
    
    private void dataPartnerBankAccountAddHandle() {
        if (cbpPartner.getValue() != null) {
            selectedDataPartnerBankAccount = new TblPartnerBankAccount();
            selectedDataPartnerBankAccount.setTblPartner(reservationCanceledInputController.getParentController().selectedDataTransactionWithGuaranteePayment.getTblPartner());
            selectedDataPartnerBankAccount.setTblBankAccount(new TblBankAccount());
            //open form data partner bank account
            showPartnerBankAccountDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan pilih data 'Partner' terlebih dahulu..!", null, reservationCanceledInputController.getParentController().dialogStage);
        }
    }
    
    private void showPartnerBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation_v2/rctid_canceled/RCTIGuaranteePaymentPartnerBankAccountInputDialog.fxml"));
            
            RCTIGuaranteePaymentPartnerBankAccountInputController controller = new RCTIGuaranteePaymentPartnerBankAccountInputController(this);
            loader.setController(controller);
            
            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(reservationCanceledInputController.getParentController().dialogStage);

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
        initFormDataRCTIGuaranteePayment();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    private String frptName = "";
    
    public RCTIGuaranteePaymentController(ReservationCanceledInputController parentController, String frptName) {
        reservationCanceledInputController = parentController;
        this.frptName = frptName;
    }
    
    private final ReservationCanceledInputController reservationCanceledInputController;
    
    public ReservationCanceledInputController getParentController() {
        return reservationCanceledInputController;
    }
    
}
