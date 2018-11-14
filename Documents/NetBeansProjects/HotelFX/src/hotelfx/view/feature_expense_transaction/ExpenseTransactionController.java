/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_expense_transaction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblHotelExpenseTransaction;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import hotelfx.persistence.service.FExpenseTransactionManager;
import hotelfx.persistence.service.FExpenseTransactionManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_finance_transaction.FeatureFinanceTransactionController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ExpenseTransactionController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataET;

    private DoubleProperty dataETFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataETLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataETSplitpane() {
        spDataET.setDividerPositions(1);

        dataETFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataETFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataET.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataET.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataETFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataETLayout.setDisable(false);
                    tableDataETLayoutDisableLayer.setDisable(true);
                    tableDataETLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataETLayout.setDisable(true);
                    tableDataETLayoutDisableLayer.setDisable(false);
                    tableDataETLayoutDisableLayer.toFront();
                }
            }
        });

        dataETFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataETLayout;

    private ClassFilteringTable<TblHotelExpenseTransaction> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataET;

    private void initTableDataET() {
        //set table
        setTableDataET();
        //set control
        setTableControlDataET();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataET, 15.0);
        AnchorPane.setLeftAnchor(tableDataET, 15.0);
        AnchorPane.setRightAnchor(tableDataET, 15.0);
        AnchorPane.setTopAnchor(tableDataET, 15.0);
        ancBodyLayout.getChildren().add(tableDataET);
    }

    private void setTableDataET() {
        TableView<TblHotelExpenseTransaction> tableView = new TableView();

        TableColumn<TblHotelExpenseTransaction, String> etDateTime = new TableColumn("Tanggal");
        etDateTime.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getExpenseTransactionDate()),
                        param.getValue().expenseTransactionDateProperty()));
        etDateTime.setMinWidth(160);

        TableColumn<TblHotelExpenseTransaction, String> etNote = new TableColumn("Keterangan");
        etNote.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransaction, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getExpenseTransactionNote(), param.getValue().expenseTransactionNoteProperty()));
        etNote.setMinWidth(200);

        TableColumn<TblHotelExpenseTransaction, String> totalTransaction = new TableColumn("Total Transaksi");
        totalTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(getTotalTransaction(param.getValue())),
                        param.getValue().idexpenseTransactionProperty()));
        totalTransaction.setMinWidth(140);

        TableColumn<TblHotelExpenseTransaction, String> createBy = new TableColumn("Dibuat Oleh");
        createBy.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransaction, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByCreateBy().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeByCreateByProperty()));
        createBy.setMinWidth(140);

        TableColumn<TblHotelExpenseTransaction, String> companyBalance = new TableColumn("Kas");
        companyBalance.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransaction, String> param)
                -> Bindings.createStringBinding(() -> getCompanyBlanceBankAccountName(param.getValue()), param.getValue().idexpenseTransactionProperty()));
        companyBalance.setMinWidth(250);

        tableView.getColumns().addAll(etDateTime, etNote, totalTransaction, createBy, companyBalance);

        tableView.setItems(loadAllDataET());

        tableView.setRowFactory(tv -> {
            TableRow<TblHotelExpenseTransaction> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataETUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                if (checkDataETEnableToUpdate((TblHotelExpenseTransaction) row.getItem())) {
                                    //listener update
                                    dataETUpdateHandleDetail();
                                } else {
                                    dataETShowHandle();
                                }
                            } else {
                                dataETShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                if (checkDataETEnableToUpdate((TblHotelExpenseTransaction) row.getItem())) {
//                                    //listener update
//                                    dataETUpdateHandleDetail();
//                                } else {
//                                    dataETShowHandle();
//                                }
//                            } else {
//                                dataETShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataET = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblHotelExpenseTransaction.class,
                tableDataET.getTableView(),
                tableDataET.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private BigDecimal getTotalTransaction(TblHotelExpenseTransaction dataET) {
        BigDecimal result = new BigDecimal("0");
        if (dataET != null) {
            List<TblHotelExpenseTransactionDetail> list = getService().getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(dataET.getIdexpenseTransaction());
            for (TblHotelExpenseTransactionDetail data : list) {
                result = result.add((data.getItemQuantity().multiply(data.getItemCost())));
            }
        }
        return result;
    }

    private String getCompanyBlanceBankAccountName(TblHotelExpenseTransaction dataET) {
        String result = "";
        if (dataET != null
                && dataET.getTblCompanyBalance() != null) {
            result += dataET.getTblCompanyBalance().getBalanceName();
            if (dataET.getTblBankAccountByIdbankAccount() != null) {
                result += " (" + dataET.getTblBankAccountByIdbankAccount().getTblBank().getBankName() + " - " + dataET.getTblBankAccountByIdbankAccount().getCodeBankAccount() + ")";
            }
        }
        return result;
    }

    private void setTableControlDataET() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataETCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataETUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener delete
//                dataETDeleteHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataETPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataET.addButtonControl(buttonControls);
    }

    private ObservableList<TblHotelExpenseTransaction> loadAllDataET() {
        List<TblHotelExpenseTransaction> list = getService().getAllDataHotelExpenseTransaction();
        for (TblHotelExpenseTransaction data : list) {
            //data company balance
            data.setTblCompanyBalance(getService().getDataCompanyBalance(data.getTblCompanyBalance().getIdbalance()));
            if (data.getTblBankAccountByIdbankAccount() != null) {
                //data bank account
                data.setTblBankAccountByIdbankAccount(getService().getDataBankAccount(data.getTblBankAccountByIdbankAccount().getIdbankAccount()));
                //data bank account - bank
                data.getTblBankAccountByIdbankAccount().setTblBank(getService().getDataBank(data.getTblBankAccountByIdbankAccount().getTblBank().getIdbank()));
            }
            if (data.getTblBankAccountByIdbankAccountReceiver() != null) {
                //data bank account
                data.setTblBankAccountByIdbankAccountReceiver(getService().getDataBankAccount(data.getTblBankAccountByIdbankAccountReceiver().getIdbankAccount()));
                //data bank account - bank
                data.getTblBankAccountByIdbankAccountReceiver().setTblBank(getService().getDataBank(data.getTblBankAccountByIdbankAccountReceiver().getTblBank().getIdbank()));
            }
            //data employee
            data.setTblEmployeeByCreateBy(getService().getDataEmployee(data.getTblEmployeeByCreateBy().getIdemployee()));
            //data employee - people
            data.getTblEmployeeByCreateBy().setTblPeople(getService().getDataPeople(data.getTblEmployeeByCreateBy().getTblPeople().getIdpeople()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataET;

    @FXML
    private ScrollPane spFormDataET;

    @FXML
    private JFXDatePicker dtpDate;

    @FXML
    private JFXTimePicker tmpTime;

    @FXML
    private AnchorPane ancCompanyBalanceLayout;
    private JFXCComboBoxTablePopup<CompanyBalanceBankAccount> cbpCBBA;

    @FXML
    private JFXTextField txtNominalBalance;

    @FXML
    private JFXTextField txtDestionationBankAccount;

    @FXML
    private AnchorPane ancDestionationBank;
    private JFXCComboBoxTablePopup<TblBank> cbpDestinationBank;

    @FXML
    private JFXTextField txtDestionationBankAccountHolder;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextArea txtETNote;

    @FXML
    private Label lblTotal;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblHotelExpenseTransaction selectedData;

    private void initFormDataET() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataET.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataET.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            //scroll end..!
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("err " + e.getMessage());
                }

            });
            thread.setDaemon(true);
            thread.start();
        });

        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Pengeluaran Harian)"));
        btnSave.setOnAction((e) -> {
            dataETSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataETCancelHandle();
        });

        cbpCBBA.valueProperty().addListener((obs, oldVal, newVal) -> {
            //refresh information, +nominal
            txtNominalBalance.setText(ClassFormatter.currencyFormat.format(getNominalBalance(newVal)));
            if (newVal != null
                    && newVal.getDataBankAccount() != null) {   //transfer
                txtDestionationBankAccount.setVisible(true);
                txtDestionationBankAccountHolder.setVisible(true);
                ancDestionationBank.setVisible(true);
            } else {  //cash
                //data transfer (destination)
                txtDestionationBankAccount.setText("");
                txtDestionationBankAccountHolder.setText("");
                //refresh data bank
                ObservableList<TblBank> destinationBankItems = FXCollections.observableArrayList(loadAllDataBank());
                cbpDestinationBank.setItems(destinationBankItems);
                cbpDestinationBank.setValue(null);
                txtDestionationBankAccount.setVisible(false);
                txtDestionationBankAccountHolder.setVisible(false);
                ancDestionationBank.setVisible(false);
            }
        });

        initDateCalendar();

        initImportantFieldColor();

    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dtpDate,
                tmpTime,
                cbpCBBA,
                txtDestionationBankAccount,
                cbpDestinationBank,
                txtDestionationBankAccountHolder,
                txtETNote,
                cbpCBBA);
    }

    private void initDataPopup() {
        //Company Balance - Bank Account
        TableView<CompanyBalanceBankAccount> tableCompanyBalanceBankAccount = new TableView<>();

        TableColumn<CompanyBalanceBankAccount, String> companyBalance = new TableColumn("Kas");
        companyBalance.setCellValueFactory((TableColumn.CellDataFeatures<CompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataCompanyBalance().getBalanceName(), param.getValue().dataCompanyBalanceProperty()));
        companyBalance.setMinWidth(140);

        TableColumn<CompanyBalanceBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<CompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataBankAccount() != null
                                ? param.getValue().getDataBankAccount().getCodeBankAccount() + " - " + param.getValue().getDataBankAccount().getTblBank().getBankName()
                                : "-",
                        param.getValue().dataBankAccountProperty()));
        bankAccount.setMinWidth(200);

        tableCompanyBalanceBankAccount.getColumns().addAll(companyBalance, bankAccount);

        ObservableList<CompanyBalanceBankAccount> cbbaItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount());

        cbpCBBA = new JFXCComboBoxTablePopup<>(
                CompanyBalanceBankAccount.class, tableCompanyBalanceBankAccount, cbbaItems, "", "Kas *", true, 350, 200
        );

        //Bank (Destination)
        TableView<TblBank> tableDestionationBank = new TableView<>();

        TableColumn<TblBank, String> destinationBankName = new TableColumn("Bank");
        destinationBankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBank, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getBankName(),
                        param.getValue().bankNameProperty()));
        destinationBankName.setMinWidth(160);

        tableDestionationBank.getColumns().addAll(destinationBankName);

        ObservableList<TblBank> destinationBankItems = FXCollections.observableArrayList(loadAllDataBank());

        cbpDestinationBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableDestionationBank, destinationBankItems, "", "Bank *", true, 180, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpCBBA, 0.0);
        AnchorPane.setLeftAnchor(cbpCBBA, 0.0);
        AnchorPane.setRightAnchor(cbpCBBA, 0.0);
        AnchorPane.setTopAnchor(cbpCBBA, 0.0);
        ancCompanyBalanceLayout.getChildren().clear();
        ancCompanyBalanceLayout.getChildren().add(cbpCBBA);

        AnchorPane.setBottomAnchor(cbpDestinationBank, 0.0);
        AnchorPane.setLeftAnchor(cbpDestinationBank, 0.0);
        AnchorPane.setRightAnchor(cbpDestinationBank, 0.0);
        AnchorPane.setTopAnchor(cbpDestinationBank, 0.0);
        ancDestionationBank.getChildren().clear();
        ancDestionationBank.getChildren().add(cbpDestinationBank);
    }

    private ObservableList<CompanyBalanceBankAccount> loadAllDataCompanyBalanceBankAccount() {
        List<CompanyBalanceBankAccount> list = new ArrayList<>();
        //list company balance
        List<TblCompanyBalance> companyBalances = getService().getAllDataCompanyBalance();
        //remove data company balance : kas besar, kas deposit
        for (int i = companyBalances.size() - 1; i > -1; i--) {
            if (companyBalances.get(i).getIdbalance() == (long) 1 //Kas Besar = '1'
                    || companyBalances.get(i).getIdbalance() == (long) 4) {   //Kas Deposit = '4'
                companyBalances.remove(i);
            }
        }
        //list company balance - bank account (Kas Besar)
        List<TblCompanyBalanceBankAccount> companyBalanceBankAccounts = getService().getAllDataCompanyBalanceBankAccountByIDCompanyBalance((long) 1);    //Kas Besar = '1'
        for (TblCompanyBalanceBankAccount companyBalanceBankAccount : companyBalanceBankAccounts) {
            //data company balance
            companyBalanceBankAccount.setTblCompanyBalance(getService().getDataCompanyBalance(companyBalanceBankAccount.getTblCompanyBalance().getIdbalance()));
            //data bank account
            companyBalanceBankAccount.setTblBankAccount(getService().getDataBankAccount(companyBalanceBankAccount.getTblBankAccount().getIdbankAccount()));
            //data bank
            companyBalanceBankAccount.getTblBankAccount().setTblBank(getService().getDataBank(companyBalanceBankAccount.getTblBankAccount().getTblBank().getIdbank()));
            //add to list
            list.add(new CompanyBalanceBankAccount(companyBalanceBankAccount.getTblCompanyBalance(), companyBalanceBankAccount.getTblBankAccount()));
        }
        //list company balance - bank account (Kas BackOffice, Kas Kasir)
        for (TblCompanyBalance companyBalance : companyBalances) {
            //add to list
            list.add(new CompanyBalanceBankAccount(companyBalance, null));
        }
        //just 'Kas Besar' and 'Kas Back-Office'
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getDataCompanyBalance().getIdbalance() != 1 //Kas Besar = '1'
                    && list.get(i).getDataCompanyBalance().getIdbalance() != 2) { //Kas Back-Office = '2'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private List<TblBank> loadAllDataBank() {
        List<TblBank> list = getService().getAllDataBank();

        return list;
    }

    private void refreshDataPopup() {
        //Company Balance - Bank Account
        ObservableList<CompanyBalanceBankAccount> cbbaItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount());
        cbpCBBA.setItems(cbbaItems);

        //destination bank
        ObservableList<TblBank> destinationBankItems = FXCollections.observableArrayList(loadAllDataBank());
        cbpDestinationBank.setItems(destinationBankItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeExpenseTransaction() != null
                ? "" : "");

        if (selectedData.getExpenseTransactionDate() != null) {
            dtpDate.setValue(selectedData.getExpenseTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            tmpTime.setValue(selectedData.getExpenseTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        } else {
            dtpDate.setValue(null);
            tmpTime.setValue(null);
        }

        txtETNote.textProperty().bindBidirectional(selectedData.expenseTransactionNoteProperty());

        if (dataInputStatus == 0) {
            cbpCBBA.setDisable(false);
        } else {
            cbpCBBA.setDisable(true);
        }
        cbpCBBA.setValue(new CompanyBalanceBankAccount(selectedData.getTblCompanyBalance(), selectedData.getTblBankAccountByIdbankAccount()));

        cbpCBBA.hide();

        txtNominalBalance.setText(ClassFormatter.currencyFormat.format(getNominalBalance(cbpCBBA.getValue())));

        if (dataInputStatus != 0
                && selectedData.getTblBankAccountByIdbankAccountReceiver() != null) {
            txtDestionationBankAccount.setText(selectedData.getTblBankAccountByIdbankAccountReceiver().getCodeBankAccount());
            cbpDestinationBank.setValue(selectedData.getTblBankAccountByIdbankAccountReceiver().getTblBank());
            txtDestionationBankAccountHolder.setText(selectedData.getTblBankAccountByIdbankAccountReceiver().getBankAccountHolderName());
        } else {
            txtDestionationBankAccount.setText("");
            cbpDestinationBank.setValue(null);
            txtDestionationBankAccountHolder.setText("");
        }

        initTableDataDetail();

        refreshDataBill();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private BigDecimal getNominalBalance(CompanyBalanceBankAccount cbba) {
        BigDecimal result = new BigDecimal("0");
        if (cbba != null) {
            if (cbba.getDataCompanyBalance() != null) {
                if (cbba.getDataBankAccount() != null) {
                    TblCompanyBalanceBankAccount companyBalanceBankAccount = getService().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            cbba.getDataCompanyBalance().getIdbalance(),
                            cbba.getDataBankAccount().getIdbankAccount());
                    if (companyBalanceBankAccount != null) {
                        result = companyBalanceBankAccount.getCompanyBalanceBankAccountNominal();
                    }
                } else {
                    result = cbba.getDataCompanyBalance().getBalanceNominal();
                }
            }
        }
        return result;
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataET,
                dataInputStatus == 3
        );

        btnSave.setVisible(dataInputStatus != 3);
    }

    private BigDecimal calculationTotal() {
        BigDecimal result = new BigDecimal("0");
        if (tableDataDetail != null) {
            List<TblHotelExpenseTransactionDetail> list = (List<TblHotelExpenseTransactionDetail>) tableDataDetail.getTableView().getItems();
            for (TblHotelExpenseTransactionDetail data : list) {
                result = result.add(calculationTotalCost(data));
            }
        }
        return result;
    }

    public void refreshDataBill() {
        lblTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationTotal()));
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    public void dataETCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblHotelExpenseTransaction();
        setSelectedDataToInputForm();
        //open form data expense transaction
        dataETFormShowStatus.set(0.0);
        dataETFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataETUpdateHandle() {
        if (tableDataET.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataETEnableToUpdate((TblHotelExpenseTransaction) tableDataET.getTableView().getSelectionModel().getSelectedItem())) {
                dataETUpdateHandleDetail();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat diubah..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataETUpdateHandleDetail() {
        dataInputStatus = 1;
        selectedData = getService().getDataHotelExpenseTransaction(((TblHotelExpenseTransaction) tableDataET.getTableView().getSelectionModel().getSelectedItem()).getIdexpenseTransaction());
        selectedData.setTblCompanyBalance(getService().getDataCompanyBalance(selectedData.getTblCompanyBalance().getIdbalance()));
        if (selectedData.getTblBankAccountByIdbankAccount() != null) {
            selectedData.setTblBankAccountByIdbankAccount(getService().getDataBankAccount(selectedData.getTblBankAccountByIdbankAccount().getIdbankAccount()));
            selectedData.getTblBankAccountByIdbankAccount().setTblBank(getService().getDataBank(selectedData.getTblBankAccountByIdbankAccount().getTblBank().getIdbank()));
        }
        if (selectedData.getTblBankAccountByIdbankAccountReceiver() != null) {
            selectedData.setTblBankAccountByIdbankAccountReceiver(getService().getDataBankAccount(selectedData.getTblBankAccountByIdbankAccountReceiver().getIdbankAccount()));
            selectedData.getTblBankAccountByIdbankAccountReceiver().setTblBank(getService().getDataBank(selectedData.getTblBankAccountByIdbankAccountReceiver().getTblBank().getIdbank()));
        }
        setSelectedDataToInputForm();
        //open form data expense transaction
        dataETFormShowStatus.set(0.0);
        dataETFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private boolean checkDataETEnableToUpdate(TblHotelExpenseTransaction dataET) {
        boolean check = true;
        if (dataET != null
                && dataET.getIdexpenseTransaction() != 0L) {
            TblOpenCloseCashierBalanceDetail opcbDetail = getService().getDataOpenCloseCashierBalanceDetailByIDHotelExpenseTransaction(dataET.getIdexpenseTransaction());
            if (opcbDetail != null) {
                TblOpenCloseCashierBalance opcb = getService().getDataOpenCloseCashierBalance(opcbDetail.getTblOpenCloseCashierBalance().getIdopenCloseCashierBalance());
                if (opcb != null) {
                    check = opcb.getRefOpenCloseCashierBalanceStatus().getIdstatus() == 1;  //Active = '1'
                }
            }
        }
        return check;
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataETShowHandle() {
        if (tableDataET.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getDataHotelExpenseTransaction(((TblHotelExpenseTransaction) tableDataET.getTableView().getSelectionModel().getSelectedItem()).getIdexpenseTransaction());
            selectedData.setTblCompanyBalance(getService().getDataCompanyBalance(selectedData.getTblCompanyBalance().getIdbalance()));
            if (selectedData.getTblBankAccountByIdbankAccount() != null) {
                selectedData.setTblBankAccountByIdbankAccount(getService().getDataBankAccount(selectedData.getTblBankAccountByIdbankAccount().getIdbankAccount()));
                selectedData.getTblBankAccountByIdbankAccount().setTblBank(getService().getDataBank(selectedData.getTblBankAccountByIdbankAccount().getTblBank().getIdbank()));
            }
            if (selectedData.getTblBankAccountByIdbankAccountReceiver() != null) {
                selectedData.setTblBankAccountByIdbankAccountReceiver(getService().getDataBankAccount(selectedData.getTblBankAccountByIdbankAccountReceiver().getIdbankAccount()));
                selectedData.getTblBankAccountByIdbankAccountReceiver().setTblBank(getService().getDataBank(selectedData.getTblBankAccountByIdbankAccountReceiver().getTblBank().getIdbank()));
            }
            setSelectedDataToInputForm();
            dataETFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataETUnshowHandle() {
        refreshDataTableET();
        dataETFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataETDeleteHandle() {
        if (tableDataET.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataETEnableToDelete((TblHotelExpenseTransaction) tableDataET.getTableView().getSelectionModel().getSelectedItem())) {
//                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membatalkan data ini?", null);
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    if (getService().deleteDataHotelExpenseTransaction((TblHotelExpenseTransaction) tableDataET.getTableView().getSelectionModel().getSelectedItem())) {
//                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dibatalkan..!!", null);
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        //refresh data from table & close form data expense transaction
                        refreshDataTableET();
                        dataETFormShowStatus.set(0.0);
                    } else {
//                        HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibatalkan..!!", getService().getErrorMessage());
                        ClassMessage.showFailedDeletingDataMessage(getService().getErrorMessage(), null);
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapaat dibatalkan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataETEnableToDelete(TblHotelExpenseTransaction dataET) {
        boolean check = true;
        if (dataET != null
                && dataET.getIdexpenseTransaction() != 0L) {
            TblOpenCloseCashierBalanceDetail opcbDetail = getService().getDataOpenCloseCashierBalanceDetailByIDHotelExpenseTransaction(dataET.getIdexpenseTransaction());
            if (opcbDetail != null) {
                TblOpenCloseCashierBalance opcb = getService().getDataOpenCloseCashierBalance(opcbDetail.getTblOpenCloseCashierBalance().getIdopenCloseCashierBalance());
                if (opcb != null) {
                    check = opcb.getRefOpenCloseCashierBalanceStatus().getIdstatus() == 1;  //Active = '1'
                }
            }
        }
        return check;
    }

    private void dataETPrintHandle() {
        if (tableDataET.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printET(((TblHotelExpenseTransaction) tableDataET.getTableView().getSelectionModel().getSelectedItem()).getIdexpenseTransaction());
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printET(long idET) {
//        if (idET != 0L) {
        String hotelName = "";
        SysDataHardCode sdhHotelName = getService().getDataSysDataHardCode((long) 12);  //HotelName = '12'
        if (sdhHotelName != null
                && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }
        String hotelAddress = "";
        SysDataHardCode sdhHotelAddress = getService().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
        if (sdhHotelAddress != null
                && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }
        String hotelPhoneNumber = "";
        SysDataHardCode sdhHotelPhoneNumber = getService().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
        if (sdhHotelPhoneNumber != null
                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
        }
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = getService().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        try {
//            ClassPrinter.printET(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    idET);
//        } catch (Throwable ex) {
//            Logger.getLogger(ExpenseTransactionController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
//        }
    }

    private void dataETSaveHandle() {
        if (checkDataInputDataET()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //data expense transaction
                LocalDateTime localDateTime = LocalDateTime.of(dtpDate.getValue(), tmpTime.getValue());
                selectedData.setExpenseTransactionDate(Timestamp.valueOf(localDateTime));
                selectedData.setTblCompanyBalance(cbpCBBA.getValue().getDataCompanyBalance());
                selectedData.setTblBankAccountByIdbankAccount(cbpCBBA.getValue().getDataBankAccount());
                //data expense transaction - data transfer
                if (selectedData.getTblBankAccountByIdbankAccount() != null) {  //transfer
                    if (selectedData.getTblBankAccountByIdbankAccountReceiver() != null) {  //update
                        selectedData.getTblBankAccountByIdbankAccountReceiver().setCodeBankAccount(txtDestionationBankAccount.getText());
                        selectedData.getTblBankAccountByIdbankAccountReceiver().setBankAccountHolderName(txtDestionationBankAccountHolder.getText());
                        selectedData.getTblBankAccountByIdbankAccountReceiver().setTblBank(cbpDestinationBank.getValue());
                    } else {    //create
                        TblBankAccount destinationBankAccount = new TblBankAccount();
                        destinationBankAccount.setCodeBankAccount(txtDestionationBankAccount.getText());
                        destinationBankAccount.setBankAccountHolderName(txtDestionationBankAccountHolder.getText());
                        destinationBankAccount.setTblBank(cbpDestinationBank.getValue());
                        //...
                        selectedData.setTblBankAccountByIdbankAccountReceiver(destinationBankAccount);
                    }
                    selectedData.setRefFinanceTransactionPaymentType(getService().getDataFinanceTransactionType(1));    //Transfer = '1'
                } else {    //tunai
                    selectedData.setTblBankAccountByIdbankAccountReceiver(null);
                    selectedData.setRefFinanceTransactionPaymentType(getService().getDataFinanceTransactionType(0));    //Tunai = '0'
                }
                //dummy entry
                TblHotelExpenseTransaction dummySelectedData = new TblHotelExpenseTransaction(selectedData);
                dummySelectedData.setTblCompanyBalance(new TblCompanyBalance(dummySelectedData.getTblCompanyBalance()));
                if (dummySelectedData.getTblBankAccountByIdbankAccount() != null) {
                    dummySelectedData.setTblBankAccountByIdbankAccount(new TblBankAccount(dummySelectedData.getTblBankAccountByIdbankAccount()));
                }
                dummySelectedData.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedData.getRefFinanceTransactionPaymentType()));
                List<TblHotelExpenseTransactionDetail> dummyDataHotelExpenseTransactionDetails = new ArrayList<>();
                for (TblHotelExpenseTransactionDetail dataHotelExpenseTransactionDetail : (List<TblHotelExpenseTransactionDetail>) tableDataDetail.getTableView().getItems()) {
                    TblHotelExpenseTransactionDetail dummyDataHotelExpenseTransactionDetail = new TblHotelExpenseTransactionDetail(dataHotelExpenseTransactionDetail);
                    dummyDataHotelExpenseTransactionDetail.setTblHotelExpenseTransaction(dummySelectedData);
                    dummyDataHotelExpenseTransactionDetails.add(dummyDataHotelExpenseTransactionDetail);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (getService().insertDataHotelExpenseTransaction(dummySelectedData,
                                dummyDataHotelExpenseTransactionDetails) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data expense transaction
                            refreshDataTableET();
                            dataETFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (getService().updateDataHotelExpenseTransaction(dummySelectedData,
                                dummyDataHotelExpenseTransactionDetails)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data expense transaction
                            refreshDataTableET();
                            dataETFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(getService().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataETCancelHandle() {
        //refresh data from table & close form data expense transaction
        refreshDataTableET();
        dataETFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableET() {
        tableDataET.getTableView().setItems(loadAllDataET());
        cft.refreshFilterItems(tableDataET.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataET() {
        boolean dataInput = true;
        errDataInput = "";
        if (dtpDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (tmpTime.getValue() == null) {
            dataInput = false;
            errDataInput += "Jam : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpCBBA.getValue() == null) {
            dataInput = false;
            errDataInput += "Data Kas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            //check data transfer
            if (cbpCBBA.getValue().getDataBankAccount() != null) {
                if (txtDestionationBankAccount.getText() == null
                        || txtDestionationBankAccount.getText().equals("")) {
                    dataInput = false;
                    errDataInput += "Nomor Rekening (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                }
                if (cbpDestinationBank.getValue() == null) {
                    dataInput = false;
                    errDataInput += "Bank (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                }
                if (txtDestionationBankAccountHolder.getText() == null
                        || txtDestionationBankAccountHolder.getText().equals("")) {
                    dataInput = false;
                    errDataInput += "Pemegang Nomor Rekening (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                }
            }
        }
        if (txtETNote.getText() == null || txtETNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Keterangan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (calculationTotal().compareTo(new BigDecimal("0")) == -1) {
            dataInput = false;
            errDataInput += "Total : Nilai tidak dapat kurang dari '0' \n";
        }
        if (!checkDataInputDataETDetail()) {
            dataInput = false;
            errDataInput += "Tabel (Detail-Beban) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        //check nomial company balance
        if (cbpCBBA.getValue() != null
                && (calculationTotal().compareTo(new BigDecimal("0")) > -1)
                && checkDataInputDataETDetail()) {
            if (!checkCompanyBalanceAvailableToCreateExpenseTransaction()) {
                dataInput = false;
                errDataInput += "Data Kas : Nominal kas tidak mencukupi untuk melakukan transaksi..!! \n";
            }
        }
        return dataInput;
    }

    public boolean checkDataInputDataETDetail() {
        return (tableDataDetail.getTableView().getItems().size() > 0);
    }

    public boolean checkCompanyBalanceAvailableToCreateExpenseTransaction() {
        boolean available = false;
        if (cbpCBBA.getValue().getDataBankAccount() == null) {
            TblCompanyBalance dataCompanyBalance = getService().getDataCompanyBalance(cbpCBBA.getValue().getDataCompanyBalance().getIdbalance());
            if (dataCompanyBalance != null) {
                if (selectedData.getIdexpenseTransaction() == 0L) {   //insert
                    available = calculationTotal().compareTo(dataCompanyBalance.getBalanceNominal()) < 1;
                } else {  //udapte
                    TblHotelExpenseTransaction tblHotelExpenseTransaction = getService().getDataHotelExpenseTransaction(selectedData.getIdexpenseTransaction());
                    tblHotelExpenseTransaction.setTblCompanyBalance(getService().getDataCompanyBalance(tblHotelExpenseTransaction.getTblCompanyBalance().getIdbalance()));
                    if (tblHotelExpenseTransaction.getTblCompanyBalance().getIdbalance()
                            == dataCompanyBalance.getIdbalance()) {
                        BigDecimal totalBeforeUPdate = new BigDecimal("0");
                        List<TblHotelExpenseTransactionDetail> tblHotelExpenseTransactionDetails = getService().getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(tblHotelExpenseTransaction.getIdexpenseTransaction());
                        for (TblHotelExpenseTransactionDetail tblHotelExpenseTransactionDetail : tblHotelExpenseTransactionDetails) {
                            totalBeforeUPdate = totalBeforeUPdate.add((tblHotelExpenseTransactionDetail.getItemCost().multiply(tblHotelExpenseTransactionDetail.getItemQuantity())));
                        }
                        available = (calculationTotal().subtract(totalBeforeUPdate)).compareTo(dataCompanyBalance.getBalanceNominal()) < 1;
                    } else {
                        available = calculationTotal().compareTo(dataCompanyBalance.getBalanceNominal()) < 1;
                    }
                }
            }
        } else {
            TblCompanyBalanceBankAccount dataCompanyBalanceBankAccount = getService().getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(cbpCBBA.getValue().getDataCompanyBalance().getIdbalance(),
                    cbpCBBA.getValue().getDataBankAccount().getIdbankAccount());
            if (dataCompanyBalanceBankAccount != null) {
                dataCompanyBalanceBankAccount.setTblCompanyBalance(getService().getDataCompanyBalance(dataCompanyBalanceBankAccount.getTblCompanyBalance().getIdbalance()));
                dataCompanyBalanceBankAccount.setTblBankAccount(getService().getDataBankAccount(dataCompanyBalanceBankAccount.getTblBankAccount().getIdbankAccount()));
                if (selectedData.getIdexpenseTransaction() == 0L) {   //insert
                    available = calculationTotal().compareTo(dataCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal()) < 1;
                } else {  //udapte
                    TblHotelExpenseTransaction tblHotelExpenseTransaction = getService().getDataHotelExpenseTransaction(selectedData.getIdexpenseTransaction());
                    tblHotelExpenseTransaction.setTblCompanyBalance(getService().getDataCompanyBalance(tblHotelExpenseTransaction.getTblCompanyBalance().getIdbalance()));
                    if (tblHotelExpenseTransaction.getTblBankAccountByIdbankAccount() != null) {
                        tblHotelExpenseTransaction.setTblBankAccountByIdbankAccount(getService().getDataBankAccount(tblHotelExpenseTransaction.getTblBankAccountByIdbankAccount().getIdbankAccount()));
                    }
                    if (tblHotelExpenseTransaction.getTblCompanyBalance().getIdbalance()
                            == dataCompanyBalanceBankAccount.getTblCompanyBalance().getIdbalance()
                            && (tblHotelExpenseTransaction.getTblBankAccountByIdbankAccount() != null
                                    ? (tblHotelExpenseTransaction.getTblBankAccountByIdbankAccount().getIdbankAccount() == dataCompanyBalanceBankAccount.getTblBankAccount().getIdbankAccount())
                                    : false)) {
                        BigDecimal totalBeforeUPdate = new BigDecimal("0");
                        List<TblHotelExpenseTransactionDetail> tblHotelExpenseTransactionDetails = getService().getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(selectedData.getIdexpenseTransaction());
                        for (TblHotelExpenseTransactionDetail tblHotelExpenseTransactionDetail : tblHotelExpenseTransactionDetails) {
                            totalBeforeUPdate = totalBeforeUPdate.add((tblHotelExpenseTransactionDetail.getItemCost().multiply(tblHotelExpenseTransactionDetail.getItemQuantity())));
                        }
                        available = (calculationTotal().subtract(totalBeforeUPdate)).compareTo(dataCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal()) < 1;
                    } else {
                        available = calculationTotal().compareTo(dataCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal()) < 1;
                    }
                }
            }
        }
        return available;
    }

    public class CompanyBalanceBankAccount {

        private final ObjectProperty<TblCompanyBalance> dataCompanyBalance = new SimpleObjectProperty<>();

        private final ObjectProperty<TblBankAccount> dataBankAccount = new SimpleObjectProperty<>();

        public CompanyBalanceBankAccount(TblCompanyBalance tblCompanyBalance,
                TblBankAccount tblBankAccount) {
            this.dataCompanyBalance.set(tblCompanyBalance);
            this.dataBankAccount.set(tblBankAccount);
        }

        public ObjectProperty<TblCompanyBalance> dataCompanyBalanceProperty() {
            return dataCompanyBalance;
        }

        public TblCompanyBalance getDataCompanyBalance() {
            return dataCompanyBalanceProperty().get();
        }

        public void setDataCompanyBalance(TblCompanyBalance tblCompanyBalance) {
            dataCompanyBalanceProperty().set(tblCompanyBalance);
        }

        public ObjectProperty<TblBankAccount> dataBankAccountProperty() {
            return dataBankAccount;
        }

        public TblBankAccount getDataBankAccount() {
            return dataBankAccountProperty().get();
        }

        public void setDataBankAccount(TblBankAccount tblBankAccount) {
            dataBankAccountProperty().set(tblBankAccount);
        }

        @Override
        public String toString() {
            return getDataCompanyBalance() != null
                    ? (getDataBankAccount() != null
                            ? getDataBankAccount().getTblBank().getBankName() + "-" + getDataBankAccount().getCodeBankAccount()
                            : getDataCompanyBalance().getBalanceName())
                    : "-";
        }

    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDetailLayout;

    public ClassTableWithControl tableDataDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set control
        setTableControlDataDetail();
        //set table-control to content-view
        ancDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail() {
        TableView<TblHotelExpenseTransactionDetail> tableView = new TableView();

        TableColumn<TblHotelExpenseTransactionDetail, String> itemName = new TableColumn("Nama Beban");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransactionDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getItemName(), param.getValue().itemNameProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblHotelExpenseTransactionDetail, String> itemCost = new TableColumn("Harga (Satuan)");
        itemCost.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransactionDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCost()), param.getValue().itemCostProperty()));
        itemCost.setMinWidth(140);

        TableColumn<TblHotelExpenseTransactionDetail, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransactionDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(140);

        TableColumn<TblHotelExpenseTransactionDetail, String> totalCost = new TableColumn("Total Harga");
        totalCost.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransactionDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalCost(param.getValue())), param.getValue().iddetailProperty()));
        totalCost.setMinWidth(160);

        TableColumn<TblHotelExpenseTransactionDetail, String> detailNote = new TableColumn("Keterangan");
        detailNote.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelExpenseTransactionDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDetailNote(), param.getValue().detailNoteProperty()));
        detailNote.setMinWidth(160);

        tableView.getColumns().addAll(itemName, itemCost, itemQuantity, totalCost, detailNote);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataETDetail()));

        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private BigDecimal calculationTotalCost(TblHotelExpenseTransactionDetail dataDetail) {
        return dataDetail.getItemCost().multiply(dataDetail.getItemQuantity());
    }

    private void setTableControlDataDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataDetailUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    //listener delete
                    dataDetailDeleteHandle();
                }
            });
            buttonControls.add(buttonControl);
        }
        tableDataDetail.addButtonControl(buttonControls);
    }

    private List<TblHotelExpenseTransactionDetail> loadAllDataETDetail() {
        List<TblHotelExpenseTransactionDetail> list = getService().getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(selectedData.getIdexpenseTransaction());
        for (TblHotelExpenseTransactionDetail data : list) {
            //data hotel expense transaction
            data.setTblHotelExpenseTransaction(getService().getDataHotelExpenseTransaction(data.getTblHotelExpenseTransaction().getIdexpenseTransaction()));
        }
        return list;
    }

    public TblHotelExpenseTransactionDetail selectedDataDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailCreateHandle() {
        dataInputDetailStatus = 0;
        selectedDataDetail = new TblHotelExpenseTransactionDetail();
        selectedDataDetail.setTblHotelExpenseTransaction(selectedData);
        selectedDataDetail.setItemCost(new BigDecimal("0"));
        selectedDataDetail.setItemQuantity(new BigDecimal("0"));
        //open form data - detail
        showDataDetailDialog();
    }

    public void dataDetailUpdateHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputDetailStatus = 1;
            selectedDataDetail = new TblHotelExpenseTransactionDetail((TblHotelExpenseTransactionDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
            selectedDataDetail.setTblHotelExpenseTransaction(selectedData);
            //open form data - detail
            showDataDetailDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataDetailDeleteHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            ClassMessage.showSucceedRemovingDataMessage("", null);
            //remove data from table items list
            tableDataDetail.getTableView().getItems().remove(tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
            //refresh bill
            refreshDataBill();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_expense_transaction/ExpanseTransactionDetailDialog.fxml"));

            ExpanseTransactionDetailController controller = new ExpanseTransactionDetailController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageDetal = new Stage();
            dialogStageDetal.initModality(Modality.WINDOW_MODAL);
            dialogStageDetal.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageDetal, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageDetal.initStyle(StageStyle.TRANSPARENT);
            dialogStageDetal.setScene(scene);
            dialogStageDetal.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageDetal.showAndWait();
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
        //set splitpane
        setDataETSplitpane();

        //init table
        initTableDataET();

        //init form
        initFormDataET();

        spDataET.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataETFormShowStatus.set(0.0);
        });
    }

    public ExpenseTransactionController(FeatureFinanceTransactionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureFinanceTransactionController parentController;

    public FExpenseTransactionManager getService() {
        return parentController.getFExpenseTransactionManager();
    }
    
}
