/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_data_account;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefAccountType;
import hotelfx.persistence.model.SysAccount;
import hotelfx.persistence.service.FDataAccountManager;
import hotelfx.persistence.service.FDataAccountManagerImpl;
import hotelfx.view.DashboardController;
import java.net.URL;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class DataAccountController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataAccount;

    private DoubleProperty dataAccountFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataAccountLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataAccountSplitpane() {
        spDataAccount.setDividerPositions(1);

        dataAccountFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataAccountFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataAccount.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataAccount.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataAccountFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataAccountLayout.setDisable(false);
                    tableDataAccountLayoutDisableLayer.setDisable(true);
                    tableDataAccountLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataAccountLayout.setDisable(true);
                    tableDataAccountLayoutDisableLayer.setDisable(false);
                    tableDataAccountLayoutDisableLayer.toFront();
                }
            }
        });
        dataAccountFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataAccountLayout;

    private ClassFilteringTable<SysAccount> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataAccount;

    private void initTableDataAccount() {
        //set table
        setTableDataAccount();
        //set control
        setTableControlDataAccount();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataAccount, 15.0);
        AnchorPane.setLeftAnchor(tableDataAccount, 15.0);
        AnchorPane.setRightAnchor(tableDataAccount, 15.0);
        AnchorPane.setTopAnchor(tableDataAccount, 15.0);
        ancBodyLayout.getChildren().add(tableDataAccount);
    }

    private void setTableDataAccount() {
        TableView<SysAccount> tableView = new TableView();

        TableColumn<SysAccount, String> codeAccount = new TableColumn("Kode Akun");
        codeAccount.setCellValueFactory(cellData -> cellData.getValue().codeAccountProperty());
        codeAccount.setMinWidth(120);

        TableColumn<SysAccount, String> accountName = new TableColumn("Nama Akun");
        accountName.setCellValueFactory(cellData -> cellData.getValue().accountNameProperty());
        accountName.setMinWidth(140);

        TableColumn<SysAccount, String> accountType = new TableColumn<>("Tipe");
        accountType.setCellValueFactory((TableColumn.CellDataFeatures<SysAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefAccountType().getTypeName(),
                        param.getValue().refAccountTypeProperty()));
        accountType.setMinWidth(140);

        TableColumn<SysAccount, String> accountNote = new TableColumn("Keterangan");
        accountNote.setCellValueFactory(cellData -> cellData.getValue().accountNoteProperty());
        accountNote.setMinWidth(200);

        tableView.getColumns().addAll(codeAccount, accountName, accountType, accountNote);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataAccount()));

        tableView.setRowFactory(tv -> {
            TableRow<SysAccount> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataAccountUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataAccountUpdateHandle();
                            } else {
                                dataAccountShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataAccountUpdateHandle();
//                            } else {
//                                dataAccountShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataAccount = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                SysAccount.class,
                tableDataAccount.getTableView(),
                tableDataAccount.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataAccount() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataAccountPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataAccount.addButtonControl(buttonControls);
    }

    private List<SysAccount> loadAllDataAccount() {
        List<SysAccount> list = fDataAccountManager.getAllDataAccount();
        for (SysAccount data : list) {
            //data account type
            data.setRefAccountType(fDataAccountManager.getDataAccountType(data.getRefAccountType().getIdtype()));
        }
        return list;
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataAccount;

    @FXML
    private ScrollPane spFormDataAccount;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeAccount;

    @FXML
    private JFXTextField txtAccountName;

    @FXML
    private AnchorPane ancAccountTypeLayout;
    private JFXCComboBoxTablePopup<RefAccountType> cbpAccountType;

    @FXML
    private JFXTextArea txtAccountNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private SysAccount selectedData;

    private void initFormDataAccount() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataAccount.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataAccount.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Akun)"));
        btnSave.setOnAction((e) -> {
            dataAccountSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataAccountCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodeAccount,
                txtAccountName,
                cbpAccountType);
    }

    private void initDataPopup() {
        //Account Type
        TableView<RefAccountType> tableAccountType = new TableView<>();

        TableColumn<RefAccountType, String> typeName = new TableColumn<>("Tipe");
        typeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        typeName.setMinWidth(160);

        tableAccountType.getColumns().addAll(typeName);

        ObservableList<RefAccountType> accountTypeItems = FXCollections.observableArrayList(loadAllDataAccountType());

        cbpAccountType = new JFXCComboBoxTablePopup<>(
                RefAccountType.class, tableAccountType, accountTypeItems, "", "Tipe *", true, 200, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpAccountType, 0.0);
        AnchorPane.setLeftAnchor(cbpAccountType, 0.0);
        AnchorPane.setRightAnchor(cbpAccountType, 0.0);
        AnchorPane.setTopAnchor(cbpAccountType, 0.0);
        ancAccountTypeLayout.getChildren().clear();
        ancAccountTypeLayout.getChildren().add(cbpAccountType);
    }

    private List<RefAccountType> loadAllDataAccountType() {
        List<RefAccountType> list = fDataAccountManager.getAllDataAccountType();

        return list;
    }

    private void refreshDataPopup() {
        //Account Type
        ObservableList<RefAccountType> accountTypeItems = FXCollections.observableArrayList(loadAllDataAccountType());
        cbpAccountType.setItems(accountTypeItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeAccount() != null
                ? selectedData.getCodeAccount() : "");

        txtCodeAccount.textProperty().bindBidirectional(selectedData.codeAccountProperty());
        txtAccountName.textProperty().bindBidirectional(selectedData.accountNameProperty());
        txtAccountNote.textProperty().bindBidirectional(selectedData.accountNoteProperty());

        cbpAccountType.valueProperty().bindBidirectional(selectedData.refAccountTypeProperty());

        cbpAccountType.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataAccount,
                dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataAccountCreateHandle() {
        dataInputStatus = 0;
        selectedData = new SysAccount();
        setSelectedDataToInputForm();
        //open form data account
        dataAccountFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataAccountUpdateHandle() {
        if (tableDataAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = fDataAccountManager.getDataAccount(((SysAccount) tableDataAccount.getTableView().getSelectionModel().getSelectedItem()).getIdaccount());
            selectedData.setRefAccountType(fDataAccountManager.getDataAccountType(selectedData.getRefAccountType().getIdtype()));
            setSelectedDataToInputForm();
            //open form data account
            dataAccountFormShowStatus.set(0.0);
            dataAccountFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataAccountShowHandle() {
        if (tableDataAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = fDataAccountManager.getDataAccount(((SysAccount) tableDataAccount.getTableView().getSelectionModel().getSelectedItem()).getIdaccount());
            selectedData.setRefAccountType(fDataAccountManager.getDataAccountType(selectedData.getRefAccountType().getIdtype()));
            setSelectedDataToInputForm();
            dataAccountFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataAccountUnshowHandle() {
        refreshDataTableAccount();
        dataAccountFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataAccountDeleteHandle() {
        if (tableDataAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (fDataAccountManager.deleteDataAccount(new SysAccount((SysAccount) tableDataAccount.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data account
                    refreshDataTableAccount();
                    dataAccountFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(fDataAccountManager.getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataAccountPrintHandle() {

    }

    private void dataAccountSaveHandle() {
        if (checkDataInputDataAccount()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                SysAccount dummySelectedData = new SysAccount(selectedData);
                dummySelectedData.setRefAccountType(new RefAccountType(dummySelectedData.getRefAccountType()));
                switch (dataInputStatus) {
                    case 0:
                        if (fDataAccountManager.insertDataAccount(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data account
                            refreshDataTableAccount();
                            dataAccountFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(fDataAccountManager.getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (fDataAccountManager.updateDataAccount(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data account
                            refreshDataTableAccount();
                            dataAccountFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(fDataAccountManager.getErrorMessage(), null);
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

    private void dataAccountCancelHandle() {
        //refresh data from table & close form data account
        refreshDataTableAccount();
        dataAccountFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableAccount() {
        tableDataAccount.getTableView().setItems(FXCollections.observableArrayList(loadAllDataAccount()));
        cft.refreshFilterItems(tableDataAccount.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodeAccount.getText() == null || txtCodeAccount.getText().equals("")) {
            dataInput = false;
            errDataInput += "Kode Akun : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtAccountName.getText() == null || txtAccountName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Akun : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpAccountType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Akun : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FDataAccountManager fDataAccountManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fDataAccountManager = new FDataAccountManagerImpl();

        //set splitpane
        setDataAccountSplitpane();

        //init table
        initTableDataAccount();

        //init form
        initFormDataAccount();

        spDataAccount.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataAccountFormShowStatus.set(0.0);
        });
    }

    public FDataAccountManager getFDataAccountManager() {
        return this.fDataAccountManager;
    }

}
