/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank_edc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.service.FBankManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_bank.FeatureBankController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
public class BankEDCController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataBankEDC;

    private DoubleProperty dataBankEDCFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataBankEDCLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataBankEDCSplitpane() {
        spDataBankEDC.setDividerPositions(1);

        dataBankEDCFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataBankEDCFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataBankEDC.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataBankEDC.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataBankEDCFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataBankEDCLayout.setDisable(false);
                    tableDataBankEDCLayoutDisableLayer.setDisable(true);
                    tableDataBankEDCLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataBankEDCLayout.setDisable(true);
                    tableDataBankEDCLayoutDisableLayer.setDisable(false);
                    tableDataBankEDCLayoutDisableLayer.toFront();
                }
            }
        });

        dataBankEDCFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataBankEDCLayout;

    private ClassFilteringTable<TblBankEdc> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataBankEDC;

    private void initTableDataBankEDC() {
        //set table
        setTableDataBankEDC();
        //set control
        setTableControlDataBankEDC();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataBankEDC, 15.0);
        AnchorPane.setLeftAnchor(tableDataBankEDC, 15.0);
        AnchorPane.setRightAnchor(tableDataBankEDC, 15.0);
        AnchorPane.setTopAnchor(tableDataBankEDC, 15.0);
        ancBodyLayout.getChildren().add(tableDataBankEDC);
    }

    private void setTableDataBankEDC() {
        TableView<TblBankEdc> tableView = new TableView();

        TableColumn<TblBankEdc, String> bankNumber = new TableColumn("Nomor EDC");
        bankNumber.setCellValueFactory(cellData -> cellData.getValue().edcnumberProperty());
        bankNumber.setMinWidth(140);

        TableColumn<TblBankEdc, String> bankEDCName = new TableColumn("EDC");
        bankEDCName.setCellValueFactory(cellData -> cellData.getValue().edcnameProperty());
        bankEDCName.setMinWidth(140);

        TableColumn<TblBankEdc, String> bankNote = new TableColumn("Keterangan");
        bankNote.setCellValueFactory(cellData -> cellData.getValue().edcnoteProperty());
        bankNote.setMinWidth(200);

        TableColumn<TblBankEdc, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdc, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblBankEdc, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdc, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().tblBankAccountProperty()));
        bankAccount.setMinWidth(140);

        tableView.getColumns().addAll(bankNumber, bankEDCName, bankName, bankAccount, bankNote);
        tableView.setItems(loadAllDataBankEDC());

        tableView.setRowFactory(tv -> {
            TableRow<TblBankEdc> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataBankEDCUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataBankEDCUpdateHandle();
                            } else {
                                dataBankEDCShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataBankEDCUpdateHandle();
//                            } else {
//                                dataBankEDCShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataBankEDC = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblBankEdc.class,
                tableDataBankEDC.getTableView(),
                tableDataBankEDC.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataBankEDC() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataBankEDCCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataBankEDCUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataBankEDCDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataBankEDCPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak Transaksi EDC");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print edc transaction
//                dataBankEDCPrintEDCTransactionHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataBankEDC.addButtonControl(buttonControls);
    }

    private ObservableList<TblBankEdc> loadAllDataBankEDC() {
        List<TblBankEdc> list = parentController.getFBankManager().getAllDataBankEDC();
        for (TblBankEdc data : list) {
            //data bank
            data.setTblBank(parentController.getFBankManager().getBank(data.getTblBank().getIdbank()));
            //data bank account
            data.setTblBankAccount(parentController.getFBankManager().getDataBankAccount(data.getTblBankAccount().getIdbankAccount()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataBankEDC;

    @FXML
    private ScrollPane spFormDataBankEDC;

    @FXML
    private JFXTextField txtBankEDCNumber;

    @FXML
    private JFXTextField txtBankEDCName;

    @FXML
    private JFXTextArea txtBankEDCNote;

    @FXML
    private AnchorPane ancBankLayout;
    private JFXCComboBoxTablePopup<TblBank> cbpBank;

    @FXML
    private AnchorPane ancBankAccountLayout;
    private JFXCComboBoxTablePopup<TblBankAccount> cbpBankAccount;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblBankEdc selectedData;

    private void initFormDataBankEDC() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataBankEDC.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataBankEDC.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data EDC)"));
        btnSave.setOnAction((e) -> {
            dataBankEDCSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataBankEDCCancelHandle();
        });

        initDataPopup();

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtBankEDCName,
                txtBankEDCNumber,
                cbpBank,
                cbpBankAccount);
    }

    private void initDataPopup() {
        //data bank
        TableView<TblBank> tableBank = new TableView<>();

        TableColumn<TblBank, String> bankName = new TableColumn<>("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        tableBank.getColumns().addAll(bankName);

        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(loadAllDataBank());

        cbpBank = new JFXCComboBoxTablePopup<>(
                TblBank.class, tableBank, bankItems, "", "Bank *", true, 200, 200
        );

        //data bank account
        TableView<TblBankAccount> tableBankAccount = new TableView<>();

        TableColumn<TblBankAccount, String> codeAccount = new TableColumn<>("Nomor Rekening");
        codeAccount.setCellValueFactory(cellData -> cellData.getValue().codeBankAccountProperty());
        codeAccount.setMinWidth(140);

        TableColumn<TblBankAccount, String> bank = new TableColumn("Bank");
        bank.setCellValueFactory((TableColumn.CellDataFeatures<TblBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBank().getBankName(), param.getValue().tblBankProperty()));
        bank.setMinWidth(140);

        TableColumn<TblBankAccount, String> holderName = new TableColumn<>("Nama Pemegang Rekening");
        holderName.setCellValueFactory(cellData -> cellData.getValue().bankAccountHolderNameProperty());
        holderName.setMinWidth(200);

        tableBankAccount.getColumns().addAll(codeAccount, bank, holderName);

        ObservableList<TblBankAccount> bankAccountItems = FXCollections.observableArrayList(loadAllDataBankAccount());

        cbpBankAccount = new JFXCComboBoxTablePopup<>(
                TblBankAccount.class, tableBankAccount, bankAccountItems, "", "Nomor Rekening *", true, 500, 300
        );

//        setFunctionPopup(cbpBankAccount, tableBankAccount, bankAccountItems, "codeBankAccount", "Nomor Rekening *", 500, 300);
        //attached to grid-pane
        ancBankLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBank, 0.0);
        AnchorPane.setLeftAnchor(cbpBank, 0.0);
        AnchorPane.setRightAnchor(cbpBank, 0.0);
        AnchorPane.setTopAnchor(cbpBank, 0.0);
        ancBankLayout.getChildren().add(cbpBank);
        ancBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpBankAccount, 0.0);
        AnchorPane.setLeftAnchor(cbpBankAccount, 0.0);
        AnchorPane.setRightAnchor(cbpBankAccount, 0.0);
        AnchorPane.setTopAnchor(cbpBankAccount, 0.0);
        ancBankAccountLayout.getChildren().add(cbpBankAccount);
    }

    private List<TblBank> loadAllDataBank() {
        List<TblBank> list = parentController.getFBankManager().getAllDataBank();
        for (TblBank data : list) {

        }
        return list;
    }

    private List<TblBankAccount> loadAllDataBankAccount() {   //hotel balance
        List<TblBankAccount> list = new ArrayList<>();
        List<TblCompanyBalanceBankAccount> companyBankAccounts = parentController.getFBankManager().getAllDataCompanyBalanceBankAccountByIDCompanyBalance((long) 1);  //hotel baalnce = '1'
        for (TblCompanyBalanceBankAccount companyBankAccount : companyBankAccounts) {
            //set data bank account
            TblBankAccount data = parentController.getFBankManager().getDataBankAccount(companyBankAccount.getTblBankAccount().getIdbankAccount());
            //set data bank
            data.setTblBank(parentController.getFBankManager().getBank(data.getTblBank().getIdbank()));
            //add data bank account
            list.add(data);
        }
        return list;
    }

    private void refreshDataPopup() {
        //Bank
        ObservableList<TblBank> bankItems = FXCollections.observableArrayList(loadAllDataBank());
        cbpBank.setItems(bankItems);

        //Bank Account
        ObservableList<TblBankAccount> bankAccountItems = FXCollections.observableArrayList(loadAllDataBankAccount());
        cbpBankAccount.setItems(bankAccountItems);
    }

//    private void setFunctionPopup(JFXCComboBoxPopup cbp,
//            TableView table,
//            ObservableList items,
//            String nameFiltered,
//            String promptText,
//            double prefWidth,
//            double prefHeight) {
//        table.getSelectionModel().selectedIndexProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    if (newValue.intValue() != -1) {
//                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
//                    }
//                    cbp.hide();
//                });
//
//        cbp.setPropertyNameForFiltered(nameFiltered);
//
//        cbp.setItems(items);
//
//        cbp.setOnShowing((e) -> {
//            table.getSelectionModel().clearSelection();
//        });
//
//        // Add observable list data to the table
//        table.itemsProperty().bind(cbp.filteredItemsProperty());
//
//        //popup button
//        JFXButton button = new JFXButton("▾");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(prefWidth, prefHeight);
//
//        content.setCenter(table);
//
//        cbp.setPopupEditor(true);
//        cbp.promptTextProperty().set(promptText);
//        cbp.setLabelFloat(true);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtBankEDCNumber.textProperty().bindBidirectional(selectedData.edcnumberProperty());
        txtBankEDCName.textProperty().bindBidirectional(selectedData.edcnameProperty());
        txtBankEDCNote.textProperty().bindBidirectional(selectedData.edcnoteProperty());

        cbpBank.valueProperty().bindBidirectional(selectedData.tblBankProperty());
        cbpBankAccount.valueProperty().bindBidirectional(selectedData.tblBankAccountProperty());

        cbpBank.hide();
        cbpBankAccount.hide();

        //data bank edc - bank network card
        initTableDataEDCNetworkCard();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataBankEDC, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataBankEDCCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblBankEdc();
        setSelectedDataToInputForm();
        //open form data bank edc
        dataBankEDCFormShowStatus.set(0.0);
        dataBankEDCFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataBankEDCUpdateHandle() {
        if (tableDataBankEDC.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFBankManager().getBankEDC(((TblBankEdc) tableDataBankEDC.getTableView().getSelectionModel().getSelectedItem()).getIdedc());
            setSelectedDataToInputForm();
            //open form data bank edc
            dataBankEDCFormShowStatus.set(0.0);
            dataBankEDCFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataBankEDCShowHandle() {
        if (tableDataBankEDC.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFBankManager().getBankEDC(((TblBankEdc) tableDataBankEDC.getTableView().getSelectionModel().getSelectedItem()).getIdedc());
            setSelectedDataToInputForm();
            dataBankEDCFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataBankEDCUnshowHandle() {
        refreshDataTableBankEDC();
        dataBankEDCFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataBankEDCDeleteHandle() {
        if (tableDataBankEDC.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFBankManager().deleteDataBankEDC(new TblBankEdc((TblBankEdc) tableDataBankEDC.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data bank edc
                    refreshDataTableBankEDC();
                    dataBankEDCFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataBankEDCPrintHandle() {

    }

    private void dataBankEDCPrintEDCTransactionHandle() {
        if (tableDataBankEDC.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = parentController.getFBankManager().getBankEDC(((TblBankEdc) tableDataBankEDC.getTableView().getSelectionModel().getSelectedItem()).getIdedc());
            //close form data bank edc
            dataBankEDCFormShowStatus.set(0.0);
            //open print transaction EDC - dialog
            showPrintEDCTransactionDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showPrintEDCTransactionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank_edc/PrintEDCTransactionDialog.fxml"));

            PrintEDCTransactionController controller = new PrintEDCTransactionController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

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

    private void dataBankEDCSaveHandle() {
        if (checkDataInputDataBankEDC()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblBankEdc dummySelectedData = new TblBankEdc(selectedData);
                dummySelectedData.setTblBank(new TblBank(dummySelectedData.getTblBank()));
                dummySelectedData.setTblBankAccount(new TblBankAccount(dummySelectedData.getTblBankAccount()));
                List<TblBankEdcBankNetworkCard> dummyDataEDCNetworkCards = new ArrayList<>();
                for (TblBankEdcBankNetworkCard dataEDCNetworkCard : (List<TblBankEdcBankNetworkCard>) tableDataEDCNetworkCard.getTableView().getItems()) {
                    TblBankEdcBankNetworkCard dummyDataEDCNetworkCard = new TblBankEdcBankNetworkCard(dataEDCNetworkCard);
                    dummyDataEDCNetworkCard.setTblBankEdc(dummySelectedData);
                    dummyDataEDCNetworkCard.setTblBankNetworkCard(new TblBankNetworkCard(dummyDataEDCNetworkCard.getTblBankNetworkCard()));
                    dummyDataEDCNetworkCards.add(dummyDataEDCNetworkCard);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFBankManager().insertDataBankEDC(dummySelectedData,
                                dummyDataEDCNetworkCards) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data bank edc
                            refreshDataTableBankEDC();
                            dataBankEDCFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFBankManager().updateDataBankEDC(dummySelectedData,
                                dummyDataEDCNetworkCards)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data bank edc
                            refreshDataTableBankEDC();
                            dataBankEDCFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
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

    private void dataBankEDCCancelHandle() {
        //refresh data from table & close form data bank edc
        refreshDataTableBankEDC();
        dataBankEDCFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableBankEDC() {
        tableDataBankEDC.getTableView().setItems(loadAllDataBankEDC());
        cft.refreshFilterItems(tableDataBankEDC.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataBankEDC() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtBankEDCName.getText() == null || txtBankEDCName.getText().equals("")) {
            errDataInput += "EDC : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtBankEDCNumber.getText() == null || txtBankEDCNumber.getText().equals("")) {
            errDataInput += "Nomor EDC : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (cbpBank.getValue() == null) {
            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (cbpBankAccount.getValue() == null) {
            errDataInput += "Nomor Rekening : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        return dataInput;
    }

    /**
     * TABLE DATA EDC - NETWORK CARD
     */
    @FXML
    private AnchorPane tableDataEDCNetworkCardLayout;

    public ClassTableWithControl tableDataEDCNetworkCard;

    private void initTableDataEDCNetworkCard() {
        //set table
        setTableDataEDCNetworkCard();
        //set control
        setTableControlDataEDCNetworkCard();
        //set table-control to content-view
        tableDataEDCNetworkCardLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataEDCNetworkCard, 0.0);
        AnchorPane.setLeftAnchor(tableDataEDCNetworkCard, 0.0);
        AnchorPane.setRightAnchor(tableDataEDCNetworkCard, 0.0);
        AnchorPane.setTopAnchor(tableDataEDCNetworkCard, 0.0);
        tableDataEDCNetworkCardLayout.getChildren().add(tableDataEDCNetworkCard);
    }

    private void setTableDataEDCNetworkCard() {
        TableView<TblBankEdcBankNetworkCard> tableView = new TableView();

        TableColumn<TblBankEdcBankNetworkCard, String> networkCardName = new TableColumn("Jaringan Kartu");
        networkCardName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdcBankNetworkCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankNetworkCard().getNetworkCardName(), 
                        param.getValue().tblBankNetworkCardProperty()));
        networkCardName.setMinWidth(120);

        TableColumn<TblBankEdcBankNetworkCard, String> cardTypeName = new TableColumn("Tipe Kartu");
        cardTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdcBankNetworkCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefBankCardType().getTypeName(), 
                        param.getValue().refBankCardTypeProperty()));
        cardTypeName.setMinWidth(120);
        
        TableColumn<TblBankEdcBankNetworkCard, String> status = new TableColumn("On/Off Us");
        status.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdcBankNetworkCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getOnUs() ? "On Us" : "Off Us", 
                        param.getValue().onUsProperty()));
        status.setMinWidth(120);
        
        TableColumn<TblBankEdcBankNetworkCard, String> mdrPercentage = new TableColumn("MDR(%)");
        mdrPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblBankEdcBankNetworkCard, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getNetworkMdr()) + "%", 
                        param.getValue().networkMdrProperty()));
        mdrPercentage.setMinWidth(100);

        tableView.getColumns().addAll(networkCardName, cardTypeName, status, mdrPercentage);
        tableView.setItems(loadAllDataEDCNetworkCard());
        tableDataEDCNetworkCard = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataEDCNetworkCard() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataEDCNetworkCardCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataEDCNetworkCardUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataEDCNetworkCardDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataEDCNetworkCard.addButtonControl(buttonControls);
    }

    private ObservableList<TblBankEdcBankNetworkCard> loadAllDataEDCNetworkCard() {
        ObservableList<TblBankEdcBankNetworkCard> list = FXCollections.observableArrayList(parentController.getFBankManager().getAllDataBankEDCBankNetworkCardByIDBankEDC(selectedData.getIdedc()));
        for (TblBankEdcBankNetworkCard data : list) {
            //set data edc
            data.setTblBankEdc(parentController.getFBankManager().getBankEDC(data.getTblBankEdc().getIdedc()));
            //set data network card
            data.setTblBankNetworkCard(parentController.getFBankManager().getBankNetworkCard(data.getTblBankNetworkCard().getIdnetworkCard()));
        }
        return list;
    }

    public TblBankEdcBankNetworkCard selectedDataEDCNetworkCard;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputEDCNetworkCardStatus = 0;

    public Stage dialogStage;

    public void dataEDCNetworkCardCreateHandle() {
        dataInputEDCNetworkCardStatus = 0;
        selectedDataEDCNetworkCard = new TblBankEdcBankNetworkCard();
        selectedDataEDCNetworkCard.setTblBankEdc(selectedData);
        selectedDataEDCNetworkCard.setNetworkMdr(new BigDecimal("0"));
        selectedDataEDCNetworkCard.setMaxNetworkMdrnominal(new BigDecimal("0"));
        selectedDataEDCNetworkCard.setNetworkDdr(new BigDecimal("0"));
        selectedDataEDCNetworkCard.setMaxNetworkDdrnominal(new BigDecimal("0"));
        selectedDataEDCNetworkCard.setDebitMdrintern(new BigDecimal("0"));
        selectedDataEDCNetworkCard.setDebitMdrextern(new BigDecimal("0"));
        selectedDataEDCNetworkCard.setCreaditMdrintern(new BigDecimal("0"));
        selectedDataEDCNetworkCard.setCreaditMdrextern(new BigDecimal("0"));
        //open form data edc - network card
        showEDCNetworkCardDialog();
    }

    public void dataEDCNetworkCardUpdateHandle() {
        if (tableDataEDCNetworkCard.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputEDCNetworkCardStatus = 1;
            selectedDataEDCNetworkCard = new TblBankEdcBankNetworkCard((TblBankEdcBankNetworkCard) tableDataEDCNetworkCard.getTableView().getSelectionModel().getSelectedItem());
            //data network card
            selectedDataEDCNetworkCard.setTblBankNetworkCard(new TblBankNetworkCard(selectedDataEDCNetworkCard.getTblBankNetworkCard()));
            //open form data edc - network card
            showEDCNetworkCardDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataEDCNetworkCardDeleteHandle() {
        if (tableDataEDCNetworkCard.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataEDCNetworkCard.getTableView().getItems().remove(tableDataEDCNetworkCard.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showEDCNetworkCardDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank_edc/EDCNetworkCardInputDialog.fxml"));

            EDCNetworkCardInputController controller = new EDCNetworkCardInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

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
        //set splitpane
        setDataBankEDCSplitpane();

        //init table
        initTableDataBankEDC();

        //init form
        initFormDataBankEDC();

        spDataBankEDC.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataBankEDCFormShowStatus.set(0.0);
        });
    }

    public BankEDCController(FeatureBankController parentController) {
        this.parentController = parentController;
    }

    private final FeatureBankController parentController;

    public FBankManager getService() {
        return parentController.getFBankManager();
    }

}
