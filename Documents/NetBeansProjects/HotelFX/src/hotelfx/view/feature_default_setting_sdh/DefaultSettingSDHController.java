/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_default_setting_sdh;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintDataDefault;
import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.SysPasswordDeleteDebt;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.service.FDefaultSettingSDHManager;
import hotelfx.persistence.service.FDefaultSettingSDHManagerImpl;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class DefaultSettingSDHController implements Initializable {

    /**
     * TAB - PANE
     */
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();

    @FXML
    private TabPane tpDefaultSettingSDH;

    private void setDataDefaultSettingSDHTabpane() {
        unSavingDataInput.bind(ClassSession.unSavingDataInput);

        unSavingDataInput.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                for (Tab tab : tpDefaultSettingSDH.getTabs()) {
                    if (tpDefaultSettingSDH.getSelectionModel().getSelectedItem() != null
                            && tpDefaultSettingSDH.getSelectionModel().getSelectedItem().equals(tab)) {
                        tab.setDisable(false);
                    } else {
                        tab.setDisable(true);
                    }
                }
            } else {
                for (Tab tab : tpDefaultSettingSDH.getTabs()) {
                    tab.setDisable(false);
                }
            }
        });
    }

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSaveDataHotel;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private Accordion accordion;

    /**
     * DATA HOTEL
     */
    @FXML
    private TitledPane tpDataHotel;

    @FXML
    private GridPane gpDataHotel;

    @FXML
    private JFXTextField txtHotelName;

    @FXML
    private JFXTextField txtHotelAddress;

    @FXML
    private JFXTextField txtHotelPhoneNumber;

    @FXML
    private JFXTextField txtHotelEmail;

    @FXML
    private JFXTextArea txtHotelSummaryEnglish;

    @FXML
    private JFXTextArea txtHotelSummaryIndonesia;

    @FXML
    private JFXTextArea txtHotelFooterEnglish;

    @FXML
    private JFXTextArea txtHotelFooterIndonesia;

    @FXML
    private Label lblHotelSummaryEnglish;

    @FXML
    private Label lblHotelSummaryIndonesia;

    @FXML
    private Label lblHotelFooterEnglish;

    @FXML
    private Label lblHotelFooterIndonesia;

    @FXML
    private ImageView imgHotelLogo;

    private String imgSourcePath = "";

    /**
     * TIME
     */
    @FXML
    private TitledPane tpTime;

    @FXML
    private GridPane gpTime;

    @FXML
    private JFXTimePicker tmpCheckInTime;

    @FXML
    private JFXTimePicker tmpCheckOutTime;

    @FXML
    private JFXTimePicker tmpCheckRoomTime;

    /**
     * GUEST-CARD
     */
    @FXML
    private TitledPane tpGuestCard;

    @FXML
    private GridPane gpGuestCard;

    @FXML
    private AnchorPane ancItemLayout;

    private final JFXCComboBoxPopup<TblItem> cbpItemForGuestCard = new JFXCComboBoxPopup<>(TblItem.class);

    @FXML
    private AnchorPane ancWarehouseLayout;

    private final JFXCComboBoxPopup<TblLocationOfWarehouse> cbpLocationForGuestCard = new JFXCComboBoxPopup<>(TblLocationOfWarehouse.class);

    @FXML
    private Spinner<Integer> spMinDayForConfirmedPayment;
    
    @FXML
    private Spinner<Integer> spMinDayForCancelingReservation;
    
    @FXML
    private Spinner<Integer> spGuestCardUsedNumber;

    @FXML
    private Spinner<Integer> spGuestCardChargeForDeposit;

    /**
     * SUPPLY - CHAIN
     */
    @FXML
    private TitledPane tpPurchaseOrder;

    @FXML
    private GridPane gpPurchaseOrder;

    @FXML
    private Spinner<Double> spPurchaseOrderMinTransApproval;

    @FXML
    private Spinner<Integer> spPurchaseOrderMaxRevision;
    
    @FXML
    private Spinner<Double> spReceivingPercentageAllowance;

    /**
     * BANK ACCOUNT
     */
    @FXML
    private TitledPane tpBankAccount;

    @FXML
    private GridPane gpBankAccount;

    @FXML
    private AnchorPane ancBankAccountLayout;

    private final JFXCComboBoxPopup<TblBankAccount> cbpBankAccountForCustomerTransation = new JFXCComboBoxPopup<>(TblBankAccount.class);

    /**
     * PERCENTAGE
     */
    @FXML
    private TitledPane tpPercentage;

    @FXML
    private GridPane gpPercentage;

    @FXML
    private Spinner<Double> spServiceChargePercentage;

    @FXML
    private Spinner<Double> spTaxPercentage;

    /**
     * DIGIT
     */
    @FXML
    private TitledPane tpDigit;

    @FXML
    private GridPane gpDigit;

    @FXML
    private Spinner<Integer> spMinPasswordLentgh;

    @FXML
    private Spinner<Integer> spDigitNumberAfterPrefixCode;

    @FXML
    private Spinner<Integer> spMinEmployeeAge;

    @FXML
    private Spinner<Integer> spMinCustomerAge;

    /**
     * PASSWORD HAPUS HUTANG
     */
    @FXML
    private TitledPane tpPasswordDeleteDebt;

    @FXML
    private GridPane gpPasswordDeleteDebt;

    @FXML
    private JFXTextField txtPassword;
    
     /**
     * EMAIL HOTEL
     */
    @FXML
    private JFXTextField txtSmtpHost;
    
    @FXML
    private JFXTextField txtPort;
    
    @FXML
    private JFXTextField txtEmailHotel;
    
    @FXML
    private JFXPasswordField txtPasswordHotel;

    private void initFormDataDefaultSettingSDH() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Default)"));
        btnSave.setOnAction((e) -> {
            dataDefaultSettingSDHSaveHandle();
        });

        btnSaveDataHotel.setOnAction((e) -> {
            dataDefaultSettingDataHotelSaveHandle();
        });

        btnPrint.setOnAction((e) -> {
            dataDefaultSettingDataHotelPrintHandle();
        });

        
        spMinDayForConfirmedPayment.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1));
        spMinDayForCancelingReservation.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1));
        spGuestCardUsedNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1));
        spGuestCardChargeForDeposit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500000, 0, 10000));
        spPurchaseOrderMinTransApproval.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000000.0, 0, 500000.0));
        spPurchaseOrderMaxRevision.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1));
        spReceivingPercentageAllowance.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0, 1.0));
        spServiceChargePercentage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 100.00, 0, 1));
        spTaxPercentage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 100.00, 0, 1));
        spMinPasswordLentgh.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        spDigitNumberAfterPrefixCode.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1));
        spMinEmployeeAge.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        spMinCustomerAge.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));

        lblHotelSummaryEnglish.setText("Persetujuan Pembayaran" + "\n"
                + "Reservasi (Inggris)");
        lblHotelSummaryIndonesia.setText("Persetujuan Pembayaran" + "\n"
                + "Reservasi (Indonesia)");

        lblHotelFooterEnglish.setText("Footer Bon Reservasi" + "\n"
                + "(Inggris)");

        lblHotelFooterIndonesia.setText("Footer Bon Reservasi" + "\n"
                + "(Indonesia)");
        /*  lblHotelSummary.setText("Contoh : \n "
         + "I agree to remain personally liable for the payment of the account \n"
         + "if the corporation or other third party billed fails to \n "
         + "pay part or all of these charges");
        
         lblHotelFooter.setText("Contoh : \n "
         + "Please check that you have not left any valuable in the in room personal safe. \n"
         + "Thank you for choosing to stay with us and we wish you pleasant on ward journey.");*/
//        spGuestCardUsedNumber.setEditable(true);
//        spGuestCardChargeForDeposit.setEditable(true);
//        spServiceChargePercentage.setEditable(true);
//        spTaxPercentage.setEditable(true);
//        spMinPasswordLentgh.setEditable(true);
//        spDigitNumberAfterPrefixCode.setEditable(true);
//        tmpCheckInTime.setLabelFloat(false);
//        tmpCheckInTime.setLabelFloat(false);
        
        setUnSavingDataInputFieldEvent();
        
        initDataPopup();

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtHotelName,
                txtHotelAddress,
                txtHotelPhoneNumber,
                tmpCheckInTime,
                tmpCheckOutTime,
                tmpCheckRoomTime,
                cbpItemForGuestCard,
                cbpLocationForGuestCard,
                spMinDayForConfirmedPayment, 
                spMinDayForCancelingReservation,
                spGuestCardUsedNumber,
                spGuestCardChargeForDeposit,
                spPurchaseOrderMinTransApproval,
                spPurchaseOrderMaxRevision,
                spReceivingPercentageAllowance, 
                cbpBankAccountForCustomerTransation,
                spServiceChargePercentage,
                spTaxPercentage,
                spMinPasswordLentgh,
                spDigitNumberAfterPrefixCode,
                spMinEmployeeAge, spMinCustomerAge,
                txtHotelEmail, txtHotelSummaryEnglish,
                txtHotelSummaryIndonesia,
                txtHotelFooterEnglish, txtHotelFooterIndonesia, 
                txtPassword,txtSmtpHost,txtPort,txtEmailHotel,txtPasswordHotel);
    }

    private void initDataPopup() {
        //Item - (Perlengkapan + Guest)
        TableView<TblItem> tableItem = new TableView<>();

        TableColumn<TblItem, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemName.setMinWidth(140);

        tableItem.getColumns().addAll(codeItem, itemName);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        setFunctionPopup(cbpItemForGuestCard, tableItem, itemItems, "itemName", "Barang *", 280, 200, false);

        //Warehouse : Location
        TableView<TblLocationOfWarehouse> tableWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn("Lokasi");
        warehouseName.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfWarehouse, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getWarehouseName(), param.getValue().warehouseNameProperty()));
        warehouseName.setMinWidth(140);

        tableWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());

        setFunctionPopup(cbpLocationForGuestCard, tableWarehouse, warehouseItems, "warehouseName", "Lokasi *", 200, 200, false);

        //Bank Account - For Customer Transaction
        TableView<TblBankAccount> tableBankAccountForCustomerTransaction = new TableView<>();

        TableColumn<TblBankAccount, String> bafctBankName = new TableColumn("Bank");
        bafctBankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
        bafctBankName.setMinWidth(140);

        TableColumn<TblBankAccount, String> bafctBankAccount = new TableColumn("Nomor Rekening");
        bafctBankAccount.setCellValueFactory(cellData -> cellData.getValue().codeBankAccountProperty());
        bafctBankAccount.setMinWidth(140);

        TableColumn<TblBankAccount, String> bafctBankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bafctBankAccountHolderName.setCellValueFactory(cellData -> cellData.getValue().bankAccountHolderNameProperty());
        bafctBankAccountHolderName.setMinWidth(200);

        tableBankAccountForCustomerTransaction.getColumns().addAll(bafctBankName, bafctBankAccount, bafctBankAccountHolderName);

        ObservableList<TblBankAccount> bafctBankAccountItems = FXCollections.observableArrayList(loadAllDataBankAccountForCompanyBalance());

        setFunctionPopup(cbpBankAccountForCustomerTransation, tableBankAccountForCustomerTransaction, bafctBankAccountItems, "codeBankAccount", "Nomor Rekening (Hotel) *", 500, 250, false);

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpItemForGuestCard, 0.0);
        AnchorPane.setLeftAnchor(cbpItemForGuestCard, 0.0);
        AnchorPane.setRightAnchor(cbpItemForGuestCard, 0.0);
        AnchorPane.setTopAnchor(cbpItemForGuestCard, 0.0);
        ancItemLayout.getChildren().add(cbpItemForGuestCard);

        AnchorPane.setBottomAnchor(cbpLocationForGuestCard, 0.0);
        AnchorPane.setLeftAnchor(cbpLocationForGuestCard, 0.0);
        AnchorPane.setRightAnchor(cbpLocationForGuestCard, 0.0);
        AnchorPane.setTopAnchor(cbpLocationForGuestCard, 0.0);
        ancWarehouseLayout.getChildren().add(cbpLocationForGuestCard);

        AnchorPane.setBottomAnchor(cbpBankAccountForCustomerTransation, 0.0);
        AnchorPane.setLeftAnchor(cbpBankAccountForCustomerTransation, 0.0);
        AnchorPane.setRightAnchor(cbpBankAccountForCustomerTransation, 0.0);
        AnchorPane.setTopAnchor(cbpBankAccountForCustomerTransation, 0.0);
        ancBankAccountLayout.getChildren().add(cbpBankAccountForCustomerTransation);
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list = fDefaultSettingSDHManager.getAllDataItemByConsumableAndPropertyStatusAndLeasedStatusAndGuestStatus(
                false,  //!consumable
                false,  //!Property
                false,  //!Leased
                true);  //Guest
        return list;
    }

    private List<TblLocationOfWarehouse> loadAllDataLocationOfWarehouse() {
        List<TblLocationOfWarehouse> list = fDefaultSettingSDHManager.getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(fDefaultSettingSDHManager.getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private List<TblBankAccount> loadAllDataBankAccountForCompanyBalance() {
        List<TblBankAccount> list = new ArrayList<>();
        List<TblCompanyBalanceBankAccount> companyBalanceBankAccounts = fDefaultSettingSDHManager.getAllDataCompanyBalanceBankAccount();
        for (TblCompanyBalanceBankAccount companyBalanceBankAccount : companyBalanceBankAccounts) {
            //data bank account
            TblBankAccount data = fDefaultSettingSDHManager.getDataBankAccount(companyBalanceBankAccount.getTblBankAccount().getIdbankAccount());
            //data bank
            data.setTblBank(fDefaultSettingSDHManager.getDataBank(data.getTblBank().getIdbank()));
            //add data to list
            list.add(data);
        }
        return list;
    }

    private void refreshDataPopup() {
        //Item - (Perlengkapan + Guest)
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());
        cbpItemForGuestCard.setItems(itemItems);

        //Warehouse : Location
        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());
        cbpLocationForGuestCard.setItems(warehouseItems);

        //Bank Account - For Customer Transaction
        ObservableList<TblBankAccount> bankAccountItems = FXCollections.observableArrayList(loadAllDataBankAccountForCompanyBalance());
        cbpBankAccountForCustomerTransation.setItems(bankAccountItems);
    }

    private void setFunctionPopup(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText,
            double prefWidth,
            double prefHeight,
            boolean labelFloat) {
        table.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != -1) {
                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
                    }
                    cbp.hide();
                });

        cbp.setPropertyNameForFiltered(nameFiltered);

        cbp.setItems(items);

        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth, prefHeight);

        content.setCenter(table);

        cbp.setPopupEditor(true);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(labelFloat);
        cbp.setPopupButton(button);
        cbp.settArrowButton(false);
        cbp.setPopupContent(content);
    }

    /**
     * DATA HOTEL
     */
    private SysDataHardCode hotelName;

    private SysDataHardCode hotelAddress;

    private SysDataHardCode hotelPhoneNumber;

    private SysDataHardCode hotelLogoName;

    private SysDataHardCode hotelEmail;

    private SysDataHardCode hotelSummaryEnglish;

    private SysDataHardCode hotelFooterEnglish;

    private SysDataHardCode hotelSummaryIndonesia;

    private SysDataHardCode hotelFooterIndonesia;

    /**
     * TIME
     */
    private SysDataHardCode checkInTime;

    private SysDataHardCode checkOutTime;

    private SysCurrentHotelDate lastCheckRoomDate;

    /**
     * RESERVATION
     */
    private SysDataHardCode minDayForConfirmedPayment;
    
    private SysDataHardCode minDayForCancelingReservation;
    
    private SysDataHardCode guestCard;

    private SysDataHardCode frontOffice;

    private SysDataHardCode guestCardUsedNumber;

    private SysDataHardCode guestCardBrokeCharge;

    /**
     * SUPPLY - CHAIN
     */
    private SysDataHardCode minPOTransactionForApproval;

    private SysDataHardCode maxPORevision;
    
    private SysDataHardCode receivingPercentageAllowance;

    /**
     * BANK ACCOUNT - FOR CUSTOMER TRANSACTION
     */
    private SysDataHardCode bankAccountForCustomerTransaction;

    /**
     * PERCENTAGE
     */
    private SysDataHardCode serviceChargePercentage;

    private SysDataHardCode taxPercentage;

    /**
     * DIGIT
     */
    private SysDataHardCode minPasswordLength;

    private SysDataHardCode codeDigitNumber;

    private SysDataHardCode minEmployeeAge;

    private SysDataHardCode minCustomerAge;

    /**
     * PASSWORD HAPUS HUTANG
     */
    private SysPasswordDeleteDebt passwordDeleteDebt;
    
    /**
     * EMAIL HOTEL
     */
    private SysDataHardCode emailHotel;
    private SysDataHardCode passwordHotel;
    private SysDataHardCode smtpHost;
    private SysDataHardCode port;
    
    private void loadAllDataSDH() {
        //Data Hotel
        hotelName = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 12);   //HotelName = '12'
        hotelAddress = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 13);    //HotelAddress= '13'
        hotelPhoneNumber = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 14);    //HotelPhoneNumber = '14'
        hotelEmail = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 16); //HotelEmail = '16'
        hotelSummaryEnglish = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 17); //HotelSummaryEnglish = '17'
        hotelSummaryIndonesia = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 20);//HotelSummaryIndonesia = '20'
        hotelFooterEnglish = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 18); // HotelNote = '18'
        hotelFooterIndonesia = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 21);
        hotelLogoName = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 15);   //HotelLogoName = '15'
        //Data Time
        checkInTime = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 6);  //DeafultCheckInTime = '6'
        checkOutTime = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 7);  //DefaultCheckOutTime = '7'
        lastCheckRoomDate = fDefaultSettingSDHManager.getDataSysCurrentHotelDate(2);  //House Keeping - Daily Cleaning = '2'
        //Data Reservation
        minDayForConfirmedPayment = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 33);    //MinDayForConfirmationReservationPayment = '33'
        minDayForCancelingReservation = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 29);    //MinDayForCancelingReservation = '29'
        guestCard = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 1);    //IDGuestCard = '1'
        frontOffice = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 2);  //IDFrontOffice = '2'
        guestCardUsedNumber = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 3);  //DefaultGuestCardUsedNumber = '3'
        guestCardBrokeCharge = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 4); //DefaultGuestCardBrokenCharge = '4'
        //Data Supply - Chain
        minPOTransactionForApproval = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 26);  //ApprovalPO-MinTransactionNominal = '26'
        maxPORevision = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 27); //MaxRevisionPO = '27'
        receivingPercentageAllowance = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 28); //ReceivingPercentageAllowance = '28'
        //Data Bank Account - For Customer Transaction
        bankAccountForCustomerTransaction = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 25);    //DefaultBankAccountForGuestTransaction = '25'
        //Data Percentage
        serviceChargePercentage = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 8);  //DefaultServiceChargePercentage = '8'
        taxPercentage = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 9);    //DeafultTaxPercentage = '9'
        //Data Digit
        minPasswordLength = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 10);   //DefaultMinimumUserAccountPasswordLength ='10'
        codeDigitNumber = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 5);  //DefaultCodeDigitNumber = '5'
        minEmployeeAge = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 23);   //Employee-MinimumAge ='23'
        minCustomerAge = fDefaultSettingSDHManager.getDataSysDataHardcode((long) 24);   //Guest-MinimumAge ='24'
        //Data Password
        passwordDeleteDebt = fDefaultSettingSDHManager.getPasswordDeleteDebt();
        //Email Hotel
        emailHotel = fDefaultSettingSDHManager.getDataSysDataHardcode((long)16);
        passwordHotel = fDefaultSettingSDHManager.getDataSysDataHardcode((long)37);
        smtpHost = fDefaultSettingSDHManager.getDataSysDataHardcode((long)35);
        port = fDefaultSettingSDHManager.getDataSysDataHardcode((long)36);
    
    }

    private void setSelectedDataToInputForm() {
        //load all data
        loadAllDataSDH();

        //Data Hotel
        txtHotelName.setText(hotelName != null ? hotelName.getDataHardCodeValue() : "");
        txtHotelAddress.setText(hotelAddress != null ? hotelAddress.getDataHardCodeValue() : "");
        txtHotelPhoneNumber.setText(hotelPhoneNumber != null ? hotelPhoneNumber.getDataHardCodeValue() : "");
        txtHotelEmail.setText(hotelEmail != null ? hotelEmail.getDataHardCodeValue() : "");
        txtHotelSummaryEnglish.setText(hotelSummaryEnglish != null ? hotelSummaryEnglish.getDataHardCodeValue() : "");
        txtHotelSummaryIndonesia.setText(hotelSummaryIndonesia != null ? hotelSummaryIndonesia.getDataHardCodeValue() : "");
        txtHotelFooterEnglish.setText(hotelFooterEnglish != null ? hotelFooterEnglish.getDataHardCodeValue() : "");
        txtHotelFooterIndonesia.setText(hotelFooterIndonesia != null ? hotelFooterIndonesia.getDataHardCodeValue() : "");

        imgHotelLogo.setPreserveRatio(false);
        if (hotelLogoName == null
                || hotelLogoName.getDataHardCodeValue() == null
                || hotelLogoName.getDataHardCodeValue().equals("")) {
            imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + "no_profile_img.gif";
            imgHotelLogo.setImage(new Image("file:///" + imgSourcePath));
        } else {
            imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName.getDataHardCodeValue();
            imgHotelLogo.setImage(new Image("file:///" + imgSourcePath));
        }
        imgHotelLogo.setOnMouseClicked((e) -> {
            if (e.getClickCount() == 2) {
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(
                        new File(System.getProperty("user.home"))
                );
                fc.getExtensionFilters().addAll(
                        //                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg")
                //                        ,
                //                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File file = fc.showOpenDialog(HotelFX.primaryStage);
                if (file != null) {
                    imgSourcePath = file.getAbsoluteFile().toString();
                    imgHotelLogo.setImage(new Image("file:///" + imgSourcePath));
                }
            }
        });

        //Data Time
        if (checkInTime != null
                && checkInTime.getDataHardCodeValue() != null) {
            String[] dataCheckInTime = checkInTime.getDataHardCodeValue().split(":");
            tmpCheckInTime.setValue(LocalTime.of(Integer.parseInt(dataCheckInTime[0]), Integer.parseInt(dataCheckInTime[1]), Integer.parseInt(dataCheckInTime[2])));
        } else {
            tmpCheckInTime.setValue(null);
        }
        if (checkOutTime != null
                && checkOutTime.getDataHardCodeValue() != null) {
            String[] dataCheckOutTime = checkOutTime.getDataHardCodeValue().split(":");
            tmpCheckOutTime.setValue(LocalTime.of(Integer.parseInt(dataCheckOutTime[0]), Integer.parseInt(dataCheckOutTime[1]), Integer.parseInt(dataCheckOutTime[2])));
        } else {
            tmpCheckOutTime.setValue(null);
        }

        if (lastCheckRoomDate != null
                && lastCheckRoomDate.getCurrentHotelDate() != null) {
            tmpCheckRoomTime.setValue(LocalTime.of(
                    lastCheckRoomDate.getCurrentHotelDate().getHours(),
                    lastCheckRoomDate.getCurrentHotelDate().getMinutes(),
                    lastCheckRoomDate.getCurrentHotelDate().getSeconds()));
        } else {
            tmpCheckRoomTime.setValue(null);
        }

        //Data Guest Card
        cbpItemForGuestCard.setDisable(true);
        if (guestCard != null
                && guestCard.getDataHardCodeValue() != null) {
            cbpItemForGuestCard.setValue(fDefaultSettingSDHManager.getDataItem(Long.parseLong(guestCard.getDataHardCodeValue())));
        } else {
            cbpItemForGuestCard.setValue(null);
        }
        cbpLocationForGuestCard.setDisable(true);
        if (frontOffice != null
                && frontOffice.getDataHardCodeValue() != null) {
            cbpLocationForGuestCard.setValue(fDefaultSettingSDHManager.getDataWarehouseByIDLocation(Long.parseLong(frontOffice.getDataHardCodeValue())));
        } else {
            cbpLocationForGuestCard.setValue(null);
        }        
        if (minDayForConfirmedPayment != null
                && minDayForConfirmedPayment.getDataHardCodeValue() != null) {
            spMinDayForConfirmedPayment.getValueFactory().setValue(Integer.parseInt(minDayForConfirmedPayment.getDataHardCodeValue()));
        } else {
            spMinDayForConfirmedPayment.getValueFactory().setValue(0);
        }
        if (minDayForCancelingReservation != null
                && minDayForCancelingReservation.getDataHardCodeValue() != null) {
            spMinDayForCancelingReservation.getValueFactory().setValue(Integer.parseInt(minDayForCancelingReservation.getDataHardCodeValue()));
        } else {
            spMinDayForCancelingReservation.getValueFactory().setValue(0);
        }
        if (guestCardUsedNumber != null
                && guestCardUsedNumber.getDataHardCodeValue() != null) {
            spGuestCardUsedNumber.getValueFactory().setValue(Integer.parseInt(guestCardUsedNumber.getDataHardCodeValue()));
        } else {
            spGuestCardUsedNumber.getValueFactory().setValue(0);
        }
        if (guestCardBrokeCharge != null
                && guestCardBrokeCharge.getDataHardCodeValue() != null) {
            spGuestCardChargeForDeposit.getValueFactory().setValue(Integer.parseInt(guestCardBrokeCharge.getDataHardCodeValue()));
        } else {
            spGuestCardChargeForDeposit.getValueFactory().setValue(0);
        }

        //Data Supply - Chain
        if (minPOTransactionForApproval != null
                && minPOTransactionForApproval.getDataHardCodeValue() != null) {
            spPurchaseOrderMinTransApproval.getValueFactory().setValue(Double.parseDouble(minPOTransactionForApproval.getDataHardCodeValue()));
        } else {
            spPurchaseOrderMinTransApproval.getValueFactory().setValue(0.0);
        }
        if (maxPORevision != null
                && maxPORevision.getDataHardCodeValue() != null) {
            spPurchaseOrderMaxRevision.getValueFactory().setValue(Integer.parseInt(maxPORevision.getDataHardCodeValue()));
        } else {
            spPurchaseOrderMaxRevision.getValueFactory().setValue(0);
        }
        if (receivingPercentageAllowance != null
                && receivingPercentageAllowance.getDataHardCodeValue() != null) {
            spReceivingPercentageAllowance.getValueFactory().setValue(Double.parseDouble(receivingPercentageAllowance.getDataHardCodeValue()));
        } else {
            spReceivingPercentageAllowance.getValueFactory().setValue(0.0);
        }

        //Data Bank Account - For Customer Transaction
        if (bankAccountForCustomerTransaction != null
                && bankAccountForCustomerTransaction.getDataHardCodeValue() != null) {
            cbpBankAccountForCustomerTransation.setValue(fDefaultSettingSDHManager.getDataBankAccount(Long.parseLong(bankAccountForCustomerTransaction.getDataHardCodeValue())));
        } else {
            cbpBankAccountForCustomerTransation.setValue(null);
        }

        //Data Percentage
        if (serviceChargePercentage != null
                && serviceChargePercentage.getDataHardCodeValue() != null) {
            spServiceChargePercentage.getValueFactory().setValue(Double.parseDouble(serviceChargePercentage.getDataHardCodeValue()));
        } else {
            spServiceChargePercentage.getValueFactory().setValue(0.00);
        }
        if (taxPercentage != null
                && taxPercentage.getDataHardCodeValue() != null) {
            spTaxPercentage.getValueFactory().setValue(Double.parseDouble(taxPercentage.getDataHardCodeValue()));
        } else {
            spTaxPercentage.getValueFactory().setValue(0.00);
        }

        //Data Digit
        if (minPasswordLength != null
                && minPasswordLength.getDataHardCodeValue() != null) {
            spMinPasswordLentgh.getValueFactory().setValue(Integer.parseInt(minPasswordLength.getDataHardCodeValue()));
        } else {
            spMinPasswordLentgh.getValueFactory().setValue(0);
        }
        if (codeDigitNumber != null
                && codeDigitNumber.getDataHardCodeValue() != null) {
            spDigitNumberAfterPrefixCode.getValueFactory().setValue(Integer.parseInt(codeDigitNumber.getDataHardCodeValue()));
        } else {
            spDigitNumberAfterPrefixCode.getValueFactory().setValue(0);
        }
        if (minEmployeeAge != null
                && minEmployeeAge.getDataHardCodeValue() != null) {
            spMinEmployeeAge.getValueFactory().setValue(Integer.parseInt(minEmployeeAge.getDataHardCodeValue()));
        } else {
            spMinEmployeeAge.getValueFactory().setValue(0);
        }
        if (minCustomerAge != null
                && minCustomerAge.getDataHardCodeValue() != null) {
            spMinCustomerAge.getValueFactory().setValue(Integer.parseInt(minCustomerAge.getDataHardCodeValue()));
        } else {
            spMinCustomerAge.getValueFactory().setValue(0);
        }

        //Data Password Hapus Hutang
        txtPassword.setText(passwordDeleteDebt != null || passwordDeleteDebt.getPasswordValue() != null ? passwordDeleteDebt.getPasswordValue() : " ");
        
        //Email Hotel
        txtSmtpHost.setText(smtpHost.getDataHardCodeValue());
        txtPort.setText(port.getDataHardCodeValue());
        txtEmailHotel.setText(emailHotel.getDataHardCodeValue());
        txtPasswordHotel.setText(passwordHotel.getDataHardCodeValue());
        
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private void setUnSavingDataInputFieldEvent() {
        //Data Hotel
        txtHotelName.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtHotelAddress.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtHotelPhoneNumber.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtHotelEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtHotelSummaryEnglish.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtHotelSummaryIndonesia.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtHotelFooterEnglish.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        txtHotelFooterIndonesia.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        
        imgHotelLogo.imageProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        //Data Time
        tmpCheckInTime.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        tmpCheckOutTime.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        tmpCheckRoomTime.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        //Data Guest Card
        cbpItemForGuestCard.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        cbpLocationForGuestCard.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spMinDayForConfirmedPayment.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spMinDayForCancelingReservation.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spGuestCardUsedNumber.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spGuestCardChargeForDeposit.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        //Data Supply - Chain
        spPurchaseOrderMinTransApproval.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spPurchaseOrderMaxRevision.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spReceivingPercentageAllowance.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        //Data Bank Account - For Customer Transaction
        cbpBankAccountForCustomerTransation.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        //Data Percentage
        spServiceChargePercentage.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spTaxPercentage.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        //Data Digit
        spMinPasswordLentgh.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spDigitNumberAfterPrefixCode.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spMinEmployeeAge.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        spMinCustomerAge.valueProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        //Data Password Hapus Hutang
        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });
        
        //Data Email Hotel
        txtSmtpHost.textProperty().addListener((obs,oldVal,newVal)->{
            ClassSession.unSavingDataInput.set(true);
        });
        
        txtPort.textProperty().addListener((obs,oldVal,newVal)->{
            ClassSession.unSavingDataInput.set(true);
        });
        
        txtEmailHotel.textProperty().addListener((obs,oldVal,newVal)->{
            ClassSession.unSavingDataInput.set(true);
        });
        
        txtPasswordHotel.textProperty().addListener((obs,oldVal,newVal)->{
            ClassSession.unSavingDataInput.set(true);
        });

    }

    private void setAllDataSDH() {
        //Data Hotel
       /* hotelName.setDataHardCodeValue(txtHotelName.getText());
         hotelAddress.setDataHardCodeValue(txtHotelAddress.getText());
         hotelPhoneNumber.setDataHardCodeValue(txtHotelPhoneNumber.getText());*/
        //Data Time
        checkInTime.setDataHardCodeValue(DateTimeFormatter.ofPattern("HH:mm:ss").format(tmpCheckInTime.getValue()));
        checkOutTime.setDataHardCodeValue(DateTimeFormatter.ofPattern("HH:mm:ss").format(tmpCheckOutTime.getValue()));
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(
                        lastCheckRoomDate.getCurrentHotelDate().getYear() + 1900,
                        lastCheckRoomDate.getCurrentHotelDate().getMonth() + 1,
                        lastCheckRoomDate.getCurrentHotelDate().getDate()),
                tmpCheckRoomTime.getValue());
        lastCheckRoomDate.setCurrentHotelDate(Timestamp.valueOf(localDateTime));
        //Data Guest Card
        guestCard.setDataHardCodeValue(String.valueOf(cbpItemForGuestCard.getValue().getIditem()));
        frontOffice.setDataHardCodeValue(String.valueOf(cbpLocationForGuestCard.getValue().getTblLocation().getIdlocation()));
        minDayForConfirmedPayment.setDataHardCodeValue(String.valueOf(spMinDayForConfirmedPayment.getValue()));
        minDayForCancelingReservation.setDataHardCodeValue(String.valueOf(spMinDayForCancelingReservation.getValue()));
        guestCardUsedNumber.setDataHardCodeValue(String.valueOf(spGuestCardUsedNumber.getValue()));
        guestCardBrokeCharge.setDataHardCodeValue(String.valueOf(spGuestCardChargeForDeposit.getValue()));
        //Data Supply - Chain
        minPOTransactionForApproval.setDataHardCodeValue(String.valueOf(spPurchaseOrderMinTransApproval.getValue()));
        maxPORevision.setDataHardCodeValue(String.valueOf(spPurchaseOrderMaxRevision.getValue()));
        receivingPercentageAllowance.setDataHardCodeValue(String.valueOf(spReceivingPercentageAllowance.getValue()));
        //Data Bank Account - For Customer Transaction
        bankAccountForCustomerTransaction.setDataHardCodeValue(String.valueOf(cbpBankAccountForCustomerTransation.getValue().getIdbankAccount()));
        //Data Percentage
        serviceChargePercentage.setDataHardCodeValue(String.valueOf(spServiceChargePercentage.getValue()));
        taxPercentage.setDataHardCodeValue(String.valueOf(spTaxPercentage.getValue()));
        //Data Digit
        minPasswordLength.setDataHardCodeValue(String.valueOf(spMinPasswordLentgh.getValue()));
        codeDigitNumber.setDataHardCodeValue(String.valueOf(spDigitNumberAfterPrefixCode.getValue()));
        minEmployeeAge.setDataHardCodeValue(String.valueOf(spMinEmployeeAge.getValue()));
        minCustomerAge.setDataHardCodeValue(String.valueOf(spMinCustomerAge.getValue()));

        //Data Password hapus hutang
        passwordDeleteDebt.setPasswordValue(txtPassword.getText());
        
        //Data Email Hotel
        emailHotel.setDataHardCodeValue(txtEmailHotel.getText());
        passwordHotel.setDataHardCodeValue(txtPasswordHotel.getText());
        smtpHost.setDataHardCodeValue(txtSmtpHost.getText());
        port.setDataHardCodeValue(txtPort.getText());
    }

    //Data Hotel
    private void setDataHotel() {
        hotelName.setDataHardCodeValue(txtHotelName.getText());
        hotelAddress.setDataHardCodeValue(txtHotelAddress.getText());
        hotelPhoneNumber.setDataHardCodeValue(txtHotelPhoneNumber.getText());
        hotelEmail.setDataHardCodeValue(txtHotelEmail.getText());
        hotelSummaryEnglish.setDataHardCodeValue(txtHotelSummaryEnglish.getText());
        hotelSummaryIndonesia.setDataHardCodeValue(txtHotelSummaryIndonesia.getText());
        hotelFooterEnglish.setDataHardCodeValue(txtHotelFooterEnglish.getText());
        hotelFooterIndonesia.setDataHardCodeValue(txtHotelFooterIndonesia.getText());
    }

    private List<SysDataHardCode> getDummyDataHotelList() {
        List<SysDataHardCode> dummyList = new ArrayList<>();

        dummyList.add(new SysDataHardCode(hotelName));
        dummyList.add(new SysDataHardCode(hotelAddress));
        dummyList.add(new SysDataHardCode(hotelPhoneNumber));
        dummyList.add(new SysDataHardCode(hotelEmail));
        dummyList.add(new SysDataHardCode(hotelLogoName));
        dummyList.add(new SysDataHardCode(hotelSummaryEnglish));
        dummyList.add(new SysDataHardCode(hotelSummaryIndonesia));
        dummyList.add(new SysDataHardCode(hotelFooterEnglish));
        dummyList.add(new SysDataHardCode(hotelFooterIndonesia));
        return dummyList;
    }

    private List<SysDataHardCode> getDummyEntryList() {
        List<SysDataHardCode> dummyList = new ArrayList<>();
        //Data Hotel
       /* dummyList.add(new SysDataHardCode(hotelName));
         dummyList.add(new SysDataHardCode(hotelAddress));
         dummyList.add(new SysDataHardCode(hotelPhoneNumber));
         dummyList.add(new SysDataHardCode(hotelLogoName));*/
        //Data Time
        dummyList.add(new SysDataHardCode(checkInTime));
        dummyList.add(new SysDataHardCode(checkOutTime));
        //Data Reservation
        dummyList.add(new SysDataHardCode(minDayForConfirmedPayment));
        dummyList.add(new SysDataHardCode(minDayForCancelingReservation));
        dummyList.add(new SysDataHardCode(guestCard));
        dummyList.add(new SysDataHardCode(frontOffice));
        dummyList.add(new SysDataHardCode(guestCardUsedNumber));
        dummyList.add(new SysDataHardCode(guestCardBrokeCharge));
        //Data Supply - Chain
        dummyList.add(new SysDataHardCode(minPOTransactionForApproval));
        dummyList.add(new SysDataHardCode(maxPORevision));
        dummyList.add(new SysDataHardCode(receivingPercentageAllowance));
        //Data Bank Account - For Customer Transaction
        dummyList.add(new SysDataHardCode(bankAccountForCustomerTransaction));
        //Data Percentage
        dummyList.add(new SysDataHardCode(serviceChargePercentage));
        dummyList.add(new SysDataHardCode(taxPercentage));
        //Data Digit
        dummyList.add(new SysDataHardCode(minPasswordLength));
        dummyList.add(new SysDataHardCode(codeDigitNumber));
        dummyList.add(new SysDataHardCode(minEmployeeAge));
        dummyList.add(new SysDataHardCode(minCustomerAge));
        
        //Data Email Hotel
        dummyList.add(new SysDataHardCode(emailHotel));
        dummyList.add(new SysDataHardCode(passwordHotel));
        dummyList.add(new SysDataHardCode(port));
        dummyList.add(new SysDataHardCode(smtpHost));
        
        return dummyList;
    }

    private void dataDefaultSettingDataHotelPrintHandle() {
        List<ClassPrintDataDefault> listDataDefault = new ArrayList();
        List<SysDataHardCode> listSistem = getDummyEntryList();
        List<SysDataHardCode> listPrint = getDummyDataHotelList();
        for (SysDataHardCode getListSistem : listSistem) {
            ClassPrintDataDefault dataDefault = new ClassPrintDataDefault();
            dataDefault.setTipeDefault("Sistem");
            dataDefault.setNamaDefault(getListSistem.getDataHardCodeName());
            dataDefault.setNilaiDefault(getListSistem.getDataHardCodeValue());
            dataDefault.setKeteranganDefault(getListSistem.getNote() != null ? getListSistem.getNote() : "-");
            listDataDefault.add(dataDefault);
        }

        for (SysDataHardCode getListPrint : listPrint) {
            ClassPrintDataDefault dataDefault = new ClassPrintDataDefault();
            dataDefault.setTipeDefault("Print");
            dataDefault.setNamaDefault(getListPrint.getDataHardCodeName());
            dataDefault.setNilaiDefault(getListPrint.getDataHardCodeValue());
            dataDefault.setKeteranganDefault(getListPrint.getNote() != null ? getListPrint.getNote() : "-");
            listDataDefault.add(dataDefault);
        }

        ClassPrinter.printDataDefault(listDataDefault);
    }

    private void dataDefaultSettingDataHotelSaveHandle() {
        if (checkDataInputdataDefaultSettingDataHotel()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);

            if (alert.getResult() == ButtonType.OK) {
                setDataHotel();

                List<SysDataHardCode> dummySysDataHardCodes = getDummyDataHotelList();

                if (fDefaultSettingSDHManager.updateDataSysDataHardcodeHotel(dummySysDataHardCodes)) {
                    ClassMessage.showSucceedUpdatingDataMessage(null, null);

                    if (!imgSourcePath.contains(ClassFolderManager.imageSystemRootPath)) {
                        try {
                            ClassFolderManager.copyImage(imgSourcePath, hotelLogoName.getDataHardCodeValue(), "System");
                        } catch (IOException ex) {
                            Logger.getLogger(DefaultSettingSDHController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    setSelectedDataToInputForm();
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(fDefaultSettingSDHManager.getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataDefaultSettingSDHSaveHandle() {
        if (checkDataInputdataDefaultSettingSDH()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //set all data
                setAllDataSDH();
                //data dummy entry (list)
                List<SysDataHardCode> dummySysDataHardCodes = getDummyEntryList();
                SysCurrentHotelDate dummyLastCheckRoomDate = new SysCurrentHotelDate(lastCheckRoomDate);
                SysPasswordDeleteDebt dummyPasswordDeleteDebt = new SysPasswordDeleteDebt(passwordDeleteDebt);
                if (fDefaultSettingSDHManager.updateDataSysDataHardcode(dummySysDataHardCodes,
                        dummyLastCheckRoomDate, dummyPasswordDeleteDebt)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //data image
                   /*if (!imgSourcePath.contains(ClassFolderManager.imageSystemRootPath)) {
                     try {
                     //save image
                     ClassFolderManager.copyImage(
                     imgSourcePath,
                     hotelLogoName.getDataHardCodeValue(),
                     "System");
                     } catch (IOException ex) {
                     Logger.getLogger(DefaultSettingSDHController.class.getName()).log(Level.SEVERE, null, ex);
                     }
                     }*/
                    //refresh data input
                    setSelectedDataToInputForm();
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(fDefaultSettingSDHManager.getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private String errDataInput;

    private boolean checkDataInputdataDefaultSettingDataHotel() {
        boolean dataInput = true;

        errDataInput = "";
        if (txtHotelName.getText() == null || txtHotelName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Hotel : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtHotelAddress.getText() == null || txtHotelAddress.getText().equals("")) {
            dataInput = false;
            errDataInput += "Alamat Hotel : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtHotelPhoneNumber.getText() == null || txtHotelPhoneNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Telepon Hotel : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (txtHotelEmail.getText() == null || txtHotelEmail.getText().equals("")) {
            dataInput = false;
            errDataInput += "Email Hotel : " + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (txtHotelSummaryEnglish.getText() == null || txtHotelSummaryEnglish.getText().equals("")) {
            dataInput = false;
            errDataInput += "Persetujuan Pembayaran Reservasi(Inggris) : " + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (txtHotelSummaryIndonesia.getText() == null || txtHotelSummaryIndonesia.getText().equals("")) {
            dataInput = false;
            errDataInput += "Persetujuan Pembayaran Reservasi (Indonesia) :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (txtHotelFooterEnglish.getText() == null || txtHotelFooterEnglish.getText().equals("")) {
            dataInput = false;
            errDataInput += "Footer Bill Reservasi(Inggris) : " + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (txtHotelFooterIndonesia.getText() == null || txtHotelFooterIndonesia.getText().equals("")) {
            dataInput = false;
            errDataInput += "Footer Bill Reservasi (Indonesia) :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        return dataInput;
    }

    private boolean checkDataInputdataDefaultSettingSDH() {
        boolean dataInput = true;

        if (tmpCheckInTime.getValue() == null) {
            dataInput = false;
            errDataInput += "Waktu Check In : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (tmpCheckOutTime.getValue() == null) {
            dataInput = false;
            errDataInput += "Waktu Check Out : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (tmpCheckRoomTime.getValue() == null) {
            dataInput = false;
            errDataInput += "Waktu Check Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpItemForGuestCard.getValue() == null) {
            dataInput = false;
            errDataInput += "Guest Card (Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpLocationForGuestCard.getValue() == null) {
            dataInput = false;
            errDataInput += "Lokasi Guest Card (FO) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spMinDayForConfirmedPayment.getValue() == null) {
            dataInput = false;
            errDataInput += "Minimal Konfirmasi Pembayaran Reservasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spMinDayForCancelingReservation.getValue() == null) {
            dataInput = false;
            errDataInput += "Minimal Pembatalan Reservasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spGuestCardUsedNumber.getValue() == null) {
            dataInput = false;
            errDataInput += "Jumlah Guest Card / Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spGuestCardChargeForDeposit.getValue() == null) {
            dataInput = false;
            errDataInput += "Biaya Deposit Guest Card : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spPurchaseOrderMinTransApproval.getValue() == null) {
            dataInput = false;
            errDataInput += "Minimal Nominal Transaksi PO (Butuh Persetujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spPurchaseOrderMaxRevision.getValue() == null) {
            dataInput = false;
            errDataInput += "Maksimum Revisi PO : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spReceivingPercentageAllowance.getValue() == null) {
            dataInput = false;
            errDataInput += "Persentase Allowance Penerimaan Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBankAccountForCustomerTransation.getValue() == null) {
            dataInput = false;
            errDataInput += "Nomor Rekening (Untuk Pembayaran Customer) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spServiceChargePercentage.getValue() == null) {
            dataInput = false;
            errDataInput += "Service Charge (%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spTaxPercentage.getValue() == null) {
            dataInput = false;
            errDataInput += "Pajak (%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spMinPasswordLentgh.getValue() == null) {
            dataInput = false;
            errDataInput += "Minimal Jumlah Pajang 'Password' : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spDigitNumberAfterPrefixCode.getValue() == null) {
            dataInput = false;
            errDataInput += "Jumlah Digit Pada Kode Setelah 'Prefix-Kode' : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spMinEmployeeAge.getValue() == null) {
            dataInput = false;
            errDataInput += "Minimal Umur Karyawan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (spMinCustomerAge.getValue() == null) {
            dataInput = false;
            errDataInput += "Minimal Umur Customer : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (txtPassword.getText() == null || txtPassword.getText().equals("")) {
            dataInput = false;
            errDataInput += "Password : " + ClassMessage.defaultErrorNullValueMessage + "\n";
        }
     
        if(txtSmtpHost==null){
           dataInput = false;
           errDataInput+="SMTP Host : "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(txtPort.getText()==null || txtPort.getText().equals("")){
           dataInput = false;
           errDataInput+="Port : "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(txtEmailHotel==null || txtEmailHotel.getText().equals("")){
           dataInput = false;
           errDataInput+="Email Hotel : "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(txtPasswordHotel ==null || txtPassword.getText().equals("")){
           dataInput = false;
           errDataInput+="Password : "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        return dataInput;
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FDefaultSettingSDHManager fDefaultSettingSDHManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fDefaultSettingSDHManager = new FDefaultSettingSDHManagerImpl();

        //set tab pane
        setDataDefaultSettingSDHTabpane();

        //init form input
        initFormDataDefaultSettingSDH();

        //refresh data form input
        setSelectedDataToInputForm();
    }

}
