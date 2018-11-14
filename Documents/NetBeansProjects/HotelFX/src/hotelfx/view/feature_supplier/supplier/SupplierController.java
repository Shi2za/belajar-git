/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_supplier.supplier;

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
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierBankAccount;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.service.FSupplierManager;
import hotelfx.persistence.service.FSupplierManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_supplier.FeatureSupplierController;
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
public class SupplierController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataSupplier;

    private DoubleProperty dataSupplierFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataSupplierLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataSupplierSplitpane() {
        spDataSupplier.setDividerPositions(1);

        dataSupplierFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataSupplierFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataSupplier.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataSupplier.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataSupplierFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataSupplierLayout.setDisable(false);
                    tableDataSupplierLayoutDisableLayer.setDisable(true);
                    tableDataSupplierLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataSupplierLayout.setDisable(true);
                    tableDataSupplierLayoutDisableLayer.setDisable(false);
                    tableDataSupplierLayoutDisableLayer.toFront();
                }
            }
        });

        dataSupplierFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataSupplierLayout;

    private ClassFilteringTable<TblSupplier> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataSupplier;

    private void initTableDataSupplier() {
        //set table
        setTableDataSupplier();
        //set control
        setTableControlDataSupplier();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataSupplier, 15.0);
        AnchorPane.setLeftAnchor(tableDataSupplier, 15.0);
        AnchorPane.setRightAnchor(tableDataSupplier, 15.0);
        AnchorPane.setTopAnchor(tableDataSupplier, 15.0);
        ancBodyLayout.getChildren().add(tableDataSupplier);
    }

    private void setTableDataSupplier() {
        TableView<TblSupplier> tableView = new TableView();

        TableColumn<TblSupplier, String> idSupplier = new TableColumn("ID");
        idSupplier.setCellValueFactory(cellData -> cellData.getValue().codeSupplierProperty());
        idSupplier.setMinWidth(100);

        TableColumn<TblSupplier, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        supplierName.setMinWidth(140);

        TableColumn<TblSupplier, String> supplierAddress = new TableColumn("Alamat");
        supplierAddress.setCellValueFactory(cellData -> cellData.getValue().supplierAddressProperty());
        supplierAddress.setMinWidth(180);

        TableColumn<TblSupplier, String> supplierPhoneNumber = new TableColumn("Telepon");
        supplierPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().supplierPhoneNumberProperty());
        supplierPhoneNumber.setMinWidth(120);

        TableColumn<TblSupplier, String> supplierFax = new TableColumn("Fax");
        supplierFax.setCellValueFactory(cellData -> cellData.getValue().supplierFaxProperty());
        supplierFax.setMinWidth(120);

        TableColumn<TblSupplier, String> numberTitle = new TableColumn("Nomor");
        numberTitle.getColumns().addAll(supplierPhoneNumber, supplierFax);

        TableColumn<TblSupplier, String> supplierEmailAddress = new TableColumn("Email");
        supplierEmailAddress.setCellValueFactory(cellData -> cellData.getValue().supplierEmailAddressProperty());
        supplierEmailAddress.setMinWidth(200);

        TableColumn<TblSupplier, String> supplierWebsite = new TableColumn("Website");
        supplierWebsite.setCellValueFactory(cellData -> cellData.getValue().supplierWebsiteProperty());
        supplierWebsite.setMinWidth(200);

        TableColumn<TblSupplier, String> picName = new TableColumn("Nama");
        picName.setCellValueFactory(cellData -> cellData.getValue().picnameProperty());
        picName.setMinWidth(140);

        TableColumn<TblSupplier, String> picPhoneNumber = new TableColumn("Telepon");
        picPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().picphoneNumberProperty());
        picPhoneNumber.setMinWidth(120);

        TableColumn<TblSupplier, String> picEmailAddress = new TableColumn("Email");
        picEmailAddress.setCellValueFactory(cellData -> cellData.getValue().picemailAddressProperty());
        picEmailAddress.setMinWidth(200);

        TableColumn<TblSupplier, String> pciTitle = new TableColumn("PIC");
        pciTitle.getColumns().addAll(picName, picPhoneNumber, picEmailAddress);

        TableColumn<TblSupplier, String> supplierNote = new TableColumn("Note");
        supplierNote.setCellValueFactory(cellData -> cellData.getValue().supplierInfoProperty());
        supplierNote.setMinWidth(200);

        tableView.getColumns().addAll(idSupplier, supplierName, pciTitle, supplierAddress,
                numberTitle);
        tableView.setItems(loadAllDataSupplier());

        tableView.setRowFactory(tv -> {
            TableRow<TblSupplier> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataSupplierUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataSupplierUpdateHandle();
                            } else {
                                dataSupplierShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataSupplierUpdateHandle();
//                            } else {
//                                dataSupplierShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataSupplier = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblSupplier.class,
                tableDataSupplier.getTableView(),
                tableDataSupplier.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataSupplier() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataSupplierCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataSupplierUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataSupplierDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataSupplierPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataSupplier.addButtonControl(buttonControls);
    }

    private ObservableList<TblSupplier> loadAllDataSupplier() {
        List<TblSupplier> list = getService().getAllDataSupplier();
        for (TblSupplier data : list) {
            data.setTblLocation(getService().getLocation(data.getTblLocation().getIdlocation()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataSupplier;

    @FXML
    private ScrollPane spFormDataSupplier;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeSupplier;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private JFXTextArea txtSupplierAddress;

    @FXML
    private JFXTextField txtSupplierPhoneNumber;

    @FXML
    private JFXTextField txtSupplierFaxNumber;

    @FXML
    private JFXTextField txtSupplierEmailAddress;

    @FXML
    private JFXTextField txtSupplierWebsite;

    @FXML
    private JFXTextField txtPICName;

    @FXML
    private JFXTextField txtPICPhoneNumber;

    @FXML
    private JFXTextField txtPICEmailAddress;

    @FXML
    private JFXTextArea txtSupplierNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblSupplier selectedData;

    private void initFormDataSupplier() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataSupplier.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataSupplier.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Supplier)"));
        btnSave.setOnAction((e) -> {
            dataSupplierSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataSupplierCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtSupplierName,
                txtSupplierAddress,
                txtSupplierPhoneNumber,
                txtPICName,
                txtPICPhoneNumber);
    }

    private void setSelectedDataToInputForm() {

        lblCodeData.setText(selectedData.getCodeSupplier() != null
                ? selectedData.getCodeSupplier() : "");

        txtCodeSupplier.textProperty().bindBidirectional(selectedData.codeSupplierProperty());
        txtSupplierName.textProperty().bindBidirectional(selectedData.supplierNameProperty());
        txtSupplierAddress.textProperty().bindBidirectional(selectedData.supplierAddressProperty());

        txtSupplierPhoneNumber.textProperty().bindBidirectional(selectedData.supplierPhoneNumberProperty());
        txtSupplierFaxNumber.textProperty().bindBidirectional(selectedData.supplierFaxProperty());
        txtSupplierEmailAddress.textProperty().bindBidirectional(selectedData.supplierEmailAddressProperty());
        txtSupplierWebsite.textProperty().bindBidirectional(selectedData.supplierWebsiteProperty());
        txtPICName.textProperty().bindBidirectional(selectedData.picnameProperty());
        txtPICPhoneNumber.textProperty().bindBidirectional(selectedData.picphoneNumberProperty());
        txtPICEmailAddress.textProperty().bindBidirectional(selectedData.picemailAddressProperty());
        txtSupplierNote.textProperty().bindBidirectional(selectedData.supplierInfoProperty());

        //supplier - bank acoount
        initTableDataSupplierBankAccount();

        //supplier - item
        initTableDataSupplierItem();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeSupplier.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataSupplier,
                dataInputStatus == 3,
                txtCodeSupplier);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataSupplierCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblSupplier();
        selectedData.setTblLocation(new TblLocation());
        setSelectedDataToInputForm();
        //open form data supplier
        dataSupplierFormShowStatus.set(0);
        dataSupplierFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataSupplierUpdateHandle() {
        if (tableDataSupplier.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = getService().getSupplier(((TblSupplier) tableDataSupplier.getTableView().getSelectionModel().getSelectedItem()).getIdsupplier());
            selectedData.setTblLocation(getService().getLocation(selectedData.getTblLocation().getIdlocation()));
            setSelectedDataToInputForm();
            //open form data supplier
            dataSupplierFormShowStatus.set(0);
            dataSupplierFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataSupplierShowHandle() {
        if (tableDataSupplier.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getSupplier(((TblSupplier) tableDataSupplier.getTableView().getSelectionModel().getSelectedItem()).getIdsupplier());
            selectedData.setTblLocation(getService().getLocation(selectedData.getTblLocation().getIdlocation()));
            setSelectedDataToInputForm();
            dataSupplierFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataSupplierUnshowHandle() {
        refreshDataTableSupplier();
        dataSupplierFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataSupplierDeleteHandle() {
        if (tableDataSupplier.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (getService().deleteDataSupplier((TblSupplier) tableDataSupplier.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data supplier
                    refreshDataTableSupplier();
                    dataSupplierFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataSupplierPrintHandle() {

    }

    private void dataSupplierSaveHandle() {
        if (checkDataInputDataSupplier()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblSupplier dummySelectedData = new TblSupplier(selectedData);
                dummySelectedData.setTblLocation(new TblLocation(dummySelectedData.getTblLocation()));
                List<TblSupplierBankAccount> dummyDataSupplierBankAccounts = new ArrayList<>();
                for (TblSupplierBankAccount dataSupplierBankAccount : (List<TblSupplierBankAccount>) tableDataSupplierBankAccount.getTableView().getItems()) {
                    TblSupplierBankAccount dummyDataSupplierBankAccount = new TblSupplierBankAccount(dataSupplierBankAccount);
                    dummyDataSupplierBankAccount.setTblSupplier(dummySelectedData);
                    dummyDataSupplierBankAccount.setTblBankAccount(new TblBankAccount(dummyDataSupplierBankAccount.getTblBankAccount()));
                    dummyDataSupplierBankAccounts.add(dummyDataSupplierBankAccount);
                }
                List<TblSupplierItem> dummyDataSupplierItems = new ArrayList<>();
                for (TblSupplierItem dataSupplierItem : (List<TblSupplierItem>) tableDataSupplierItem.getTableView().getItems()) {
                    TblSupplierItem dummyDataSupplierItem = new TblSupplierItem(dataSupplierItem);
                    dummyDataSupplierItem.setTblSupplier(dummySelectedData);
                    dummyDataSupplierItem.setTblItem(new TblItem(dummyDataSupplierItem.getTblItem()));
                    dummyDataSupplierItems.add(dummyDataSupplierItem);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (getService().insertDataSupplier(dummySelectedData,
                                dummyDataSupplierBankAccounts,
                                dummyDataSupplierItems) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data supplier   
                            refreshDataTableSupplier();
                            dataSupplierFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (getService().updateDataSupplier(dummySelectedData,
                                dummyDataSupplierBankAccounts,
                                dummyDataSupplierItems)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data supplier
                            refreshDataTableSupplier();
                            dataSupplierFormShowStatus.set(0);
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

    private void dataSupplierCancelHandle() {
        //refresh data from table & close form data supplier
        refreshDataTableSupplier();
        dataSupplierFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableSupplier() {
        tableDataSupplier.getTableView().setItems(loadAllDataSupplier());
        cft.refreshFilterItems(tableDataSupplier.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataSupplier() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtSupplierName.getText() == null || txtSupplierName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Supplier : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtSupplierAddress.getText() == null || txtSupplierAddress.getText().equals("")) {
            dataInput = false;
            errDataInput += "Alamat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtSupplierPhoneNumber.getText() == null || txtSupplierPhoneNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Telepon (Supplier) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtPICName.getText() == null || txtPICName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama (PIC) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtPICPhoneNumber.getText() == null || txtPICPhoneNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Telepon (PIC) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * TABLE DATA SUPPLIER BANK ACCOUNT
     */
    @FXML
    private AnchorPane tableDataSupplierBankAccountLayout;

    public ClassTableWithControl tableDataSupplierBankAccount;

    private void initTableDataSupplierBankAccount() {
        //set table
        setTableDataSupplierBankAccount();
        //set control
        setTableControlDataSupplierBankAccount();
        //set table-control to content-view
        tableDataSupplierBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataSupplierBankAccount, 0.0);
        AnchorPane.setLeftAnchor(tableDataSupplierBankAccount, 0.0);
        AnchorPane.setRightAnchor(tableDataSupplierBankAccount, 0.0);
        AnchorPane.setTopAnchor(tableDataSupplierBankAccount, 0.0);
        tableDataSupplierBankAccountLayout.getChildren().add(tableDataSupplierBankAccount);
    }

    private void setTableDataSupplierBankAccount() {
        TableView<TblSupplierBankAccount> tableView = new TableView();

        TableColumn<TblSupplierBankAccount, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblSupplierBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccount.setMinWidth(140);

        TableColumn<TblSupplierBankAccount, String> bankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderName.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderName.setMinWidth(200);

//        TableColumn<TblSupplierBankAccount, String> bankAccountStatus = new TableColumn("Status");
//        bankAccountStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefBankAccountHolderStatus().getStatusName(), param.getValue().refBankAccountHolderStatusProperty()));
        tableView.getColumns().addAll(bankName, bankAccount, bankAccountHolderName);
        tableView.setItems(loadAllDataSupplierBankAccount());
        tableDataSupplierBankAccount = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataSupplierBankAccount() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataSupplierBankAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataSupplierBankAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataSupplierBankAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataSupplierBankAccount.addButtonControl(buttonControls);
    }

    private ObservableList<TblSupplierBankAccount> loadAllDataSupplierBankAccount() {
        ObservableList<TblSupplierBankAccount> list = FXCollections.observableArrayList(getService().getAllDataSupplierBankAccountByIDSupplier(selectedData.getIdsupplier()));
        for (TblSupplierBankAccount data : list) {
            //set data supplier
            data.setTblSupplier(getService().getSupplier(data.getTblSupplier().getIdsupplier()));
            //set data bank account
            data.setTblBankAccount(getService().getDataBankAccount(data.getTblBankAccount().getIdbankAccount()));
            //set data bank
            data.getTblBankAccount().setTblBank(getService().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
        }
        return list;
    }

    public TblSupplierBankAccount selectedDataSupplierBankAccount;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputSupplierBankAccountStatus = 0;

    public Stage dialogStage;

    public void dataSupplierBankAccountCreateHandle() {
        dataInputSupplierBankAccountStatus = 0;
        selectedDataSupplierBankAccount = new TblSupplierBankAccount();
        selectedDataSupplierBankAccount.setTblSupplier(selectedData);
        selectedDataSupplierBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data supplier bank account
        showSupplierBankAccountDialog();
    }

    public void dataSupplierBankAccountUpdateHandle() {
        if (tableDataSupplierBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputSupplierBankAccountStatus = 1;
            selectedDataSupplierBankAccount = new TblSupplierBankAccount((TblSupplierBankAccount) tableDataSupplierBankAccount.getTableView().getSelectionModel().getSelectedItem());
            //data bank account
            selectedDataSupplierBankAccount.setTblBankAccount(new TblBankAccount(selectedDataSupplierBankAccount.getTblBankAccount()));
            //data bank
            selectedDataSupplierBankAccount.getTblBankAccount().setTblBank(new TblBank(selectedDataSupplierBankAccount.getTblBankAccount().getTblBank()));
            //open form data supplier bank account
            showSupplierBankAccountDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataSupplierBankAccountDeleteHandle() {
        if (tableDataSupplierBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataSupplierBankAccount.getTableView().getItems().remove(tableDataSupplierBankAccount.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showSupplierBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_supplier/supplier/SupplierBankAccountDialog.fxml"));

            SupplierBankAccountController controller = new SupplierBankAccountController(this);
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
     * TABLE DATA SUPPLIER - ITEM
     */
    @FXML
    private AnchorPane tableDataSupplierItemLayout;

    public ClassTableWithControl tableDataSupplierItem;

    private void initTableDataSupplierItem() {
        //set table
        setTableDataSupplierItem();
        //set control
        setTableControlDataSupplierItem();
        //set table-control to content-view
        tableDataSupplierItemLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataSupplierItem, 0.0);
        AnchorPane.setLeftAnchor(tableDataSupplierItem, 0.0);
        AnchorPane.setRightAnchor(tableDataSupplierItem, 0.0);
        AnchorPane.setTopAnchor(tableDataSupplierItem, 0.0);
        tableDataSupplierItemLayout.getChildren().add(tableDataSupplierItem);
    }

    private void setTableDataSupplierItem() {
        TableView<TblSupplierItem> tableView = new TableView();

        TableColumn<TblSupplierItem, String> itemSistem = new TableColumn("(Sistem)");
        itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierItem, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblItem().getItemName() != null ? param.getValue().getTblItem().getItemName() : "-")
                        + "\n(" + (param.getValue().getTblItem().getCodeItem() != null ? param.getValue().getTblItem().getCodeItem() : "-") + ")",
                        param.getValue().getTblItem().iditemProperty()));
        itemSistem.setMinWidth(140);

        TableColumn<TblSupplierItem, String> itemSupplier = new TableColumn("(Supplier)");
        itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierItem, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getSupplierItemName() != null ? param.getValue().getSupplierItemName() : "-")
                        + "\n(" + (param.getValue().getSupllierItemCode() != null ? param.getValue().getSupllierItemCode() : "-") + ")",
                        param.getValue().idrelationProperty()));
        itemSupplier.setMinWidth(140);

        TableColumn<TblSupplierItem, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(itemSistem, itemSupplier);

        TableColumn<TblSupplierItem, String> itemTypeHK = new TableColumn("Tipe");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblItemTypeHk() != null 
                        ? param.getValue().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-", 
                        param.getValue().getTblItem().tblItemTypeHkProperty()));
        itemTypeHK.setMinWidth(140);
        
        TableColumn<TblSupplierItem, String> itemTypeWH = new TableColumn("Tipe");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblItemTypeWh() != null 
                        ?param.getValue().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-", 
                        param.getValue().getTblItem().tblItemTypeWhProperty()));
        itemTypeWH.setMinWidth(140);

        TableColumn<TblSupplierItem, String> itemCost = new TableColumn("Harga");
        itemCost.setCellValueFactory((TableColumn.CellDataFeatures<TblSupplierItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCost()), param.getValue().itemCostProperty()));
        itemCost.setMinWidth(140);

        tableView.getColumns().addAll(itemTitle, itemTypeWH, itemCost);
        tableView.setItems(loadAllDataSupplierItem());
        tableDataSupplierItem = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataSupplierItem() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataSupplierItemCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataSupplierItemUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataSupplierItemDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataSupplierItem.addButtonControl(buttonControls);
    }

    private ObservableList<TblSupplierItem> loadAllDataSupplierItem() {
        ObservableList<TblSupplierItem> list = FXCollections.observableArrayList(getService().getAllDataSupplierItemByIDSupplier(selectedData.getIdsupplier()));
        for (TblSupplierItem data : list) {
            //set data supplier
            data.setTblSupplier(getService().getSupplier(data.getTblSupplier().getIdsupplier()));
            //set data item
            data.setTblItem(getService().getDataItem(data.getTblItem().getIditem()));
            //set data item type hk
            if(data.getTblItem().getTblItemTypeHk() != null){
                data.getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(data.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type wh
            if(data.getTblItem().getTblItemTypeWh() != null){
                data.getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(data.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        return list;
    }

    public TblSupplierItem selectedDataSupplierItem;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputSupplierItemStatus = 0;

    public Stage supplierItemDialogStage;

    public void dataSupplierItemCreateHandle() {
        dataInputSupplierItemStatus = 0;
        selectedDataSupplierItem = new TblSupplierItem();
        selectedDataSupplierItem.setTblSupplier(selectedData);
        selectedDataSupplierItem.setTblItem(new TblItem());
        selectedDataSupplierItem.getTblItem().setItemCostOfGoodsSold(new BigDecimal("0"));
        selectedDataSupplierItem.getTblItem().setAdditionalCharge(new BigDecimal("0"));
        selectedDataSupplierItem.getTblItem().setBrokenCharge(new BigDecimal("0"));
        selectedDataSupplierItem.getTblItem().setStockMinimal(new BigDecimal("0"));
        selectedDataSupplierItem.setItemCost(new BigDecimal("0"));
        //open form data supplier - item
        showSupplierItemDialog();
    }

    public void dataSupplierItemUpdateHandle() {
        if (tableDataSupplierItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputSupplierItemStatus = 1;
            selectedDataSupplierItem = new TblSupplierItem((TblSupplierItem) tableDataSupplierItem.getTableView().getSelectionModel().getSelectedItem());
            //data item
            selectedDataSupplierItem.setTblItem(new TblItem(selectedDataSupplierItem.getTblItem()));
            //data item type hk
            if(selectedDataSupplierItem.getTblItem().getTblItemTypeHk() != null){
                selectedDataSupplierItem.getTblItem().setTblItemTypeHk(new TblItemTypeHk(selectedDataSupplierItem.getTblItem().getTblItemTypeHk()));
            }
            //data item type wh
            if(selectedDataSupplierItem.getTblItem().getTblItemTypeWh() != null){
                selectedDataSupplierItem.getTblItem().setTblItemTypeWh(new TblItemTypeWh(selectedDataSupplierItem.getTblItem().getTblItemTypeWh()));
            }
            //open form data supplier - item
            showSupplierItemDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataSupplierItemDeleteHandle() {
        if (tableDataSupplierItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataSupplierItem.getTableView().getItems().remove(tableDataSupplierItem.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showSupplierItemDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_supplier/supplier/SupplierItemInputDialog.fxml"));

            SupplierItemInputController controller = new SupplierItemInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            supplierItemDialogStage = new Stage();
            supplierItemDialogStage.initModality(Modality.WINDOW_MODAL);
            supplierItemDialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(supplierItemDialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            supplierItemDialogStage.initStyle(StageStyle.TRANSPARENT);
            supplierItemDialogStage.setScene(scene);
            supplierItemDialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            supplierItemDialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FSupplierManager fSupplierManager;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(fSupplierManager == null){
            //set service manager
        fSupplierManager = new FSupplierManagerImpl();
        }
        
        //set splitpane
        setDataSupplierSplitpane();

        //init table
        initTableDataSupplier();

        //init form
        initFormDataSupplier();

        spDataSupplier.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataSupplierFormShowStatus.set(0);
        });
    }

    public SupplierController() {
        
    }
    
    public SupplierController(FeatureSupplierController parentController) {
        this.parentController = parentController;
        this.fSupplierManager = parentController.getFSupplierManager();
    }

    private FeatureSupplierController parentController;

    public FSupplierManager getService() {
        return fSupplierManager;
    }

}
