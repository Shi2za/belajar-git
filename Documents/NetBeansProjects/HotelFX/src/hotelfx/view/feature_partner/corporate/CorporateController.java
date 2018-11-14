/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.corporate;

import com.jfoenix.controls.JFXButton;
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
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPartnerBankAccount;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_partner.FeaturePartnerController;
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
public class CorporateController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataCorporate;

    private DoubleProperty dataCorporateFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataCorporateLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataCorporateSplitpane() {
        spDataCorporate.setDividerPositions(1);

        dataCorporateFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataCorporateFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataCorporate.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataCorporate.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataCorporateFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataCorporateLayout.setDisable(false);
                    tableDataCorporateLayoutDisableLayer.setDisable(true);
                    tableDataCorporateLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataCorporateLayout.setDisable(true);
                    tableDataCorporateLayoutDisableLayer.setDisable(false);
                    tableDataCorporateLayoutDisableLayer.toFront();
                }
            }
        });

        dataCorporateFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataCorporateLayout;

    private ClassFilteringTable<TblPartner> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataCorporate;

    private void initTableDataCorporate() {
        //set table
        setTableDataCorporate();
        //set control
        setTableControlDataCorporate();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataCorporate, 15.0);
        AnchorPane.setLeftAnchor(tableDataCorporate, 15.0);
        AnchorPane.setRightAnchor(tableDataCorporate, 15.0);
        AnchorPane.setTopAnchor(tableDataCorporate, 15.0);
        ancBodyLayout.getChildren().add(tableDataCorporate);
    }

    private void setTableDataCorporate() {
        TableView<TblPartner> tableView = new TableView();

        TableColumn<TblPartner, String> codeCorporate = new TableColumn("ID");
        codeCorporate.setCellValueFactory(cellData -> cellData.getValue().codePartnerProperty());
        codeCorporate.setMinWidth(100);

        TableColumn<TblPartner, String> corporateName = new TableColumn("Nama");
        corporateName.setCellValueFactory(cellData -> cellData.getValue().partnerNameProperty());
        corporateName.setMinWidth(140);

        TableColumn<TblPartner, String> address = new TableColumn("Alamat");
        address.setCellValueFactory((TableColumn.CellDataFeatures<TblPartner, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getPartnerAddress(),
                        param.getValue().partnerAddressProperty()));
        address.setMinWidth(200);

        TableColumn<TblPartner, String> telpNumber = new TableColumn("Nomor Telepon");
        telpNumber.setCellValueFactory(cellData -> cellData.getValue().telpNumberProperty());
        telpNumber.setMinWidth(120);

        TableColumn<TblPartner, String> email = new TableColumn("Email");
        email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        email.setMinWidth(200);

        TableColumn<TblPartner, String> website = new TableColumn("Website");
        website.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
        website.setMinWidth(200);

        TableColumn<TblPartner, String> deposit = new TableColumn("Deposit");
        deposit.setCellValueFactory((TableColumn.CellDataFeatures<TblPartner, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPartnerDeposit()), param.getValue().partnerDepositProperty()));
        deposit.setMinWidth(160);

        TableColumn<TblPartner, String> picName = new TableColumn("Nama");
        picName.setCellValueFactory(cellData -> cellData.getValue().picnameProperty());
        picName.setMinWidth(140);

        TableColumn<TblPartner, String> picPhoneNumber = new TableColumn("Telepon");
        picPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().picphoneNumberProperty());
        picPhoneNumber.setMinWidth(120);

        TableColumn<TblPartner, String> picEmailAddress = new TableColumn("Email");
        picEmailAddress.setCellValueFactory(cellData -> cellData.getValue().picemailAddressProperty());
        picEmailAddress.setMinWidth(200);

        TableColumn<TblPartner, String> pciTitle = new TableColumn("PIC");
        pciTitle.getColumns().addAll(picName, picPhoneNumber, picEmailAddress);

        tableView.getColumns().addAll(codeCorporate, corporateName, pciTitle, address, telpNumber);
        tableView.setItems(loadAllDataCorporate());

        tableView.setRowFactory(tv -> {
            TableRow<TblPartner> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataCorporateUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataCorporateUpdateHandle();
                            } else {
                                dataCorporateShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataCorporateUpdateHandle();
//                            } else {
//                                dataCorporateShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataCorporate = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblPartner.class,
                tableDataCorporate.getTableView(),
                tableDataCorporate.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataCorporate() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataCorporateCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataCorporateUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataCorporateDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataCorporatePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataCorporate.addButtonControl(buttonControls);
    }

    private ObservableList<TblPartner> loadAllDataCorporate() {
        return FXCollections.observableArrayList(parentController.getFPartnerManager().getAllDataCorporate());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataCorporate;

    @FXML
    private ScrollPane spFormDataCorporate;

    @FXML
    private JFXTextField txtCodePartner;

    @FXML
    private JFXTextField txtPartnerName;

    @FXML
    private JFXTextArea txtPartnerAddress;

    @FXML
    private JFXTextField txtTelpNumber;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtWebsite;

    @FXML
    private JFXTextField txtPICName;

    @FXML
    private JFXTextField txtPICPhoneNumber;

    @FXML
    private JFXTextField txtPICEmailAddress;

    @FXML
    private JFXTextField txtDeposit;

    @FXML
    private JFXTextArea txtNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Label lblCorporate;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblPartner selectedData;

    private void initFormDataCorporate() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataCorporate.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataCorporate.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Corporate)"));
        btnSave.setOnAction((e) -> {
            dataCorporateSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCorporateCancelHandle();
        });

        initImportantFieldColor();

        //initNumbericField();
        //txtDeposit.setVisible(false);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtPartnerName,
                txtPartnerAddress,
                txtTelpNumber,
                txtPICName,
                txtPICPhoneNumber);
    }

    /* private void initNumbericField() {
     ClassFormatter.setToNumericField(
     "BigDecimal",
     txtDeposit);
     }*/
    private void setSelectedDataToInputForm() {
        //txtCodePartner.textProperty().bindBidirectional(selectedData.codePartnerProperty());
        txtPartnerName.textProperty().bindBidirectional(selectedData.partnerNameProperty());
        txtPartnerAddress.textProperty().bindBidirectional(selectedData.partnerAddressProperty());
        txtTelpNumber.textProperty().bindBidirectional(selectedData.telpNumberProperty());
        txtEmail.textProperty().bindBidirectional(selectedData.emailProperty());
        txtWebsite.textProperty().bindBidirectional(selectedData.websiteProperty());
        txtNote.textProperty().bindBidirectional(selectedData.partnerNoteProperty());

        txtPICName.textProperty().bindBidirectional(selectedData.picnameProperty());
        txtPICPhoneNumber.textProperty().bindBidirectional(selectedData.picphoneNumberProperty());
        txtPICEmailAddress.textProperty().bindBidirectional(selectedData.picemailAddressProperty());

        //Bindings.bindBidirectional(txtDeposit.textProperty(), selectedData.partnerDepositProperty(), new ClassFormatter.CBigDecimalStringConverter());
        //data partner - bank account
        initTableDataPartnerBankAccount();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        /* txtCodePartner.setDisable(true);
         ClassViewSetting.setDisableForAllInputNode(gpFormDataCorporate,
         dataInputStatus == 3,
         txtCodePartner);*/

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataCorporateCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblPartner();
        selectedData.setPartnerDeposit(new BigDecimal("0"));
        lblCorporate.setText("");
        setSelectedDataToInputForm();
        //open form data corporate
        dataCorporateFormShowStatus.set(0.0);
        dataCorporateFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataCorporateUpdateHandle() {
        if (tableDataCorporate.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFPartnerManager().getCorporate(((TblPartner) tableDataCorporate.getTableView().getSelectionModel().getSelectedItem()).getIdpartner());
            lblCorporate.setText(selectedData.getCodePartner() + " - " + selectedData.getPartnerName());
            setSelectedDataToInputForm();
            //open form data corporate
            dataCorporateFormShowStatus.set(0.0);
            dataCorporateFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataCorporateShowHandle() {
        if (tableDataCorporate.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFPartnerManager().getCorporate(((TblPartner) tableDataCorporate.getTableView().getSelectionModel().getSelectedItem()).getIdpartner());
            setSelectedDataToInputForm();
            dataCorporateFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataCorporateUnshowHandle() {
        refreshDataTableCorporate();
        dataCorporateFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataCorporateDeleteHandle() {
        if (tableDataCorporate.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFPartnerManager().deleteDataCorporate((TblPartner) tableDataCorporate.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data corporate
                    refreshDataTableCorporate();
                    dataCorporateFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFPartnerManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataCorporatePrintHandle() {

    }

    private void dataCorporateSaveHandle() {
        if (checkDataInputDataCorporate()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblPartner dummySelectedData = new TblPartner(selectedData);
                List<TblPartnerBankAccount> dummyDataPartnerBankAccounts = new ArrayList<>();
                for (TblPartnerBankAccount dataPartnerBankAccount : (List<TblPartnerBankAccount>) tableDataPartnerBankAccount.getTableView().getItems()) {
                    TblPartnerBankAccount dummyDataPartnerBankAccount = new TblPartnerBankAccount(dataPartnerBankAccount);
                    dummyDataPartnerBankAccount.setTblPartner(dummySelectedData);
                    dummyDataPartnerBankAccount.setTblBankAccount(new TblBankAccount(dummyDataPartnerBankAccount.getTblBankAccount()));
                    dummyDataPartnerBankAccounts.add(dummyDataPartnerBankAccount);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFPartnerManager().insertDataCorporate(dummySelectedData,
                                dummyDataPartnerBankAccounts) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data corporate
                            refreshDataTableCorporate();
                            dataCorporateFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFPartnerManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFPartnerManager().updateDataCorporate(dummySelectedData,
                                dummyDataPartnerBankAccounts)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data corporate
                            refreshDataTableCorporate();
                            dataCorporateFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFPartnerManager().getErrorMessage(), null);
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

    private void dataCorporateCancelHandle() {
        //refresh data from table & close form data corporate
        refreshDataTableCorporate();
        dataCorporateFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableCorporate() {
        tableDataCorporate.getTableView().setItems(loadAllDataCorporate());
        cft.refreshFilterItems(tableDataCorporate.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataCorporate() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtPartnerName.getText() == null || txtPartnerName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtPartnerAddress.getText() == null || txtPartnerAddress.getText().equals("")) {
            dataInput = false;
            errDataInput += "Alamat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtTelpNumber.getText() == null || txtTelpNumber.getText().equals("")) {
            errDataInput += "Nomor Telepon : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtPICName.getText() == null || txtPICName.getText().equals("")) {
            errDataInput += "Nama (PIC) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtPICPhoneNumber.getText() == null || txtPICPhoneNumber.getText().equals("")) {
            errDataInput += "Nomor Telepon (PIC) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
//        if (txtDeposit.getText() == null 
//                || txtDeposit.getText().equals("")
//                || txtDeposit.getText().equals("-")) {
//            errDataInput += "Deposit : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//            dataInput = false;
//        }else{
//            if(selectedData.getPartnerDeposit()
//                    .compareTo(new BigDecimal("0")) == -1){
//                errDataInput += "Deposit : Tidak boleh kurang dari '0' \n";
//                dataInput = false;
//            }
//        }
        return dataInput;
    }

    /**
     * TABLE DATA PARTNER BANK ACCOUNT
     */
    @FXML
    private AnchorPane tableDataPartnerBankAccountLayout;

    public ClassTableWithControl tableDataPartnerBankAccount;

    private void initTableDataPartnerBankAccount() {
        //set table
        setTableDataPartnerBankAccount();
        //set control
        setTableControlDataPartnerBankAccount();
        //set table-control to content-view
        tableDataPartnerBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPartnerBankAccount, 0.0);
        AnchorPane.setLeftAnchor(tableDataPartnerBankAccount, 0.0);
        AnchorPane.setRightAnchor(tableDataPartnerBankAccount, 0.0);
        AnchorPane.setTopAnchor(tableDataPartnerBankAccount, 0.0);
        tableDataPartnerBankAccountLayout.getChildren().add(tableDataPartnerBankAccount);
    }

    private void setTableDataPartnerBankAccount() {
        TableView<TblPartnerBankAccount> tableView = new TableView();

        TableColumn<TblPartnerBankAccount, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblPartnerBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccount.setMinWidth(140);

        TableColumn<TblPartnerBankAccount, String> bankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderName.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderName.setMinWidth(200);

//        TableColumn<TblPartnerBankAccount, String> bankAccountStatus = new TableColumn("Status");
//        bankAccountStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefBankAccountHolderStatus().getStatusName(), param.getValue().refBankAccountHolderStatusProperty()));
        tableView.getColumns().addAll(bankName, bankAccount, bankAccountHolderName);
        tableView.setItems(loadAllDataPartnerBankAccount());
        tableDataPartnerBankAccount = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataPartnerBankAccount() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataPartnerBankAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataPartnerBankAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataPartnerBankAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPartnerBankAccount.addButtonControl(buttonControls);
    }

    private ObservableList<TblPartnerBankAccount> loadAllDataPartnerBankAccount() {
        ObservableList<TblPartnerBankAccount> list = FXCollections.observableArrayList(parentController.getFPartnerManager().getAllDataPartnerBankAccountByIDPartner(selectedData.getIdpartner()));
        for (TblPartnerBankAccount data : list) {
            //set data partner
            data.setTblPartner(parentController.getFPartnerManager().getPartner(data.getTblPartner().getIdpartner()));
            //set data bank account
            data.setTblBankAccount(parentController.getFPartnerManager().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
            //set data bank
            data.getTblBankAccount().setTblBank(parentController.getFPartnerManager().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
        }
        return list;
    }

    public TblPartnerBankAccount selectedDataPartnerBankAccount;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputPartnerBankAccountStatus = 0;

    public Stage dialogStage;

    public void dataPartnerBankAccountCreateHandle() {
        dataInputPartnerBankAccountStatus = 0;
        selectedDataPartnerBankAccount = new TblPartnerBankAccount();
        selectedDataPartnerBankAccount.setTblPartner(selectedData);
        selectedDataPartnerBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data partner bank account
        showPartnerBankAccountDialog();
    }

    public void dataPartnerBankAccountUpdateHandle() {
        if (tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputPartnerBankAccountStatus = 1;
            selectedDataPartnerBankAccount = new TblPartnerBankAccount((TblPartnerBankAccount) tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItem());
            //data bank account
            selectedDataPartnerBankAccount.setTblBankAccount(new TblBankAccount(selectedDataPartnerBankAccount.getTblBankAccount()));
            //data bank
            selectedDataPartnerBankAccount.getTblBankAccount().setTblBank(new TblBank(selectedDataPartnerBankAccount.getTblBankAccount().getTblBank()));
            //open form data partner bank account
            showPartnerBankAccountDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataPartnerBankAccountDeleteHandle() {
        if (tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataPartnerBankAccount.getTableView().getItems().remove(tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showPartnerBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/corporate/CorporateBankAccountDialog.fxml"));

            CorporateBankAccountController controller = new CorporateBankAccountController(this);
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
        setDataCorporateSplitpane();

        //init table
        initTableDataCorporate();

        //init form
        initFormDataCorporate();

        spDataCorporate.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataCorporateFormShowStatus.set(0.0);
        });
    }

    public CorporateController(FeaturePartnerController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePartnerController parentController;

    public FPartnerManager getService() {
        return parentController.getFPartnerManager();
    }

}
