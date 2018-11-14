/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_user_account.user_account;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefSystemUserLockStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblSystemRole;
import hotelfx.persistence.model.TblSystemUser;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_user_account.FeatureUserAccountController;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class UserAccountController implements Initializable {

    @FXML
    private SplitPane spDataUserAccount;
    private DoubleProperty dataUserAccountFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableUserAccountLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataUserAccountSplitPane() {
        spDataUserAccount.setDividerPositions(1);

        dataUserAccountFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataUserAccountFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataUserAccount.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataUserAccount.getDividers().get(0);
        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataUserAccountFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (inputDataStatus != 3) {
                if (newVal.doubleValue() == 0) {
                    tableDataUserAccountLayout.setDisable(false);
                    tableUserAccountLayoutDisableLayer.setDisable(true);
                    tableDataUserAccountLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataUserAccountLayout.setDisable(true);
                    tableUserAccountLayoutDisableLayer.setDisable(false);
                    tableUserAccountLayoutDisableLayer.toFront();
                }
            }
        });

        dataUserAccountFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataUserAccountLayout;

    private ClassFilteringTable<TblSystemUser> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataUserAccount;

    private void initTableDataUserAccount() {
        setTableDataUserAccount();
        setTableControlDataUserAccount();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataUserAccount, 15.0);
        AnchorPane.setLeftAnchor(tableDataUserAccount, 15.0);
        AnchorPane.setBottomAnchor(tableDataUserAccount, 15.0);
        AnchorPane.setRightAnchor(tableDataUserAccount, 15.0);

        ancBodyLayout.getChildren().add(tableDataUserAccount);
    }

    private void setTableDataUserAccount() {
        TableView<TblSystemUser> tableView = new TableView();

        TableColumn<TblSystemUser, String> idUser = new TableColumn("User Name");
        idUser.setCellValueFactory(cellData -> cellData.getValue().codeUserProperty());
        idUser.setMinWidth(140);

        TableColumn<TblSystemUser, String> userPassword = new TableColumn("Password");
//        userPassword.setCellValueFactory(cellData -> cellData.getValue().userPasswordProperty());
        userPassword.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemUser, String> param)
                -> Bindings.createStringBinding(() -> "************",
                        param.getValue().userPasswordProperty()));
        userPassword.setMinWidth(120);

        TableColumn<TblSystemUser, String> IdEmployee = new TableColumn("ID Karyawan");
        IdEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemUser, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(), param.getValue().getTblEmployeeByIdemployee().codeEmployeeProperty()));
        IdEmployee.setMinWidth(120);

        TableColumn<TblSystemUser, String> nameEmployee = new TableColumn("Nama Karyawan");
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemUser, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(), param.getValue().tblEmployeeByIdemployeeProperty()));
        nameEmployee.setMinWidth(140);

        TableColumn<TblSystemUser, String> nameJob = new TableColumn("Jabatan");
        nameJob.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemUser, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblJob().getJobName(), param.getValue().getTblEmployeeByIdemployee().tblJobProperty()));
        nameJob.setMinWidth(140);

        TableColumn<TblSystemUser, String> nameRole = new TableColumn("Role");
        nameRole.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemUser, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSystemRole().getRoleName(), param.getValue().tblSystemRoleProperty()));
        nameRole.setMinWidth(140);

        TableColumn<TblSystemUser, String> statusLogin = new TableColumn("Status - Login");
        statusLogin.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemUser, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefSystemUserLoginStatus().getStatusName(), param.getValue().refSystemUserLoginStatusProperty()));
        statusLogin.setMinWidth(140);

        TableColumn<TblSystemUser, String> statusLock = new TableColumn("Status - Lock");
        statusLock.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemUser, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefSystemUserLockStatus().getStatusName(), param.getValue().refSystemUserLockStatusProperty()));
        statusLock.setMinWidth(140);

        tableView.getColumns().addAll(idUser, userPassword, nameEmployee, nameRole, statusLogin, statusLock);
        tableView.setItems(loadAllDataUserAccount());

        tableView.setRowFactory(tv -> {
            TableRow<TblSystemUser> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataUserAccountUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataUserAccountUpdateHandle();
                            } else {
                                dataUserAccountShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                dataUserAccountUpdateHandle();
//                            } else {
//                                dataUserAccountShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataUserAccount = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblSystemUser.class,
                tableDataUserAccount.getTableView(),
                tableDataUserAccount.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private ObservableList<TblSystemUser> loadAllDataUserAccount() {
        List<TblSystemUser> list = parentController.getFUserAccountManager().getAllDataUserAccount();
        for (TblSystemUser data : list) {
            //data employee
            data.setTblEmployeeByIdemployee(parentController.getFUserAccountManager().getDataEmployee(data.getTblEmployeeByIdemployee().getIdemployee()));
            //data people
            data.getTblEmployeeByIdemployee().setTblPeople(parentController.getFUserAccountManager().getDataPeople(data.getTblEmployeeByIdemployee().getTblPeople().getIdpeople()));
            //data job
            data.getTblEmployeeByIdemployee().setTblJob(parentController.getFUserAccountManager().getDataJob(data.getTblEmployeeByIdemployee().getTblJob().getIdjob()));
            //data lock status
            data.setRefSystemUserLockStatus(parentController.getFUserAccountManager().getDataLockStatus(data.getRefSystemUserLockStatus().getIdstatus()));
            //data login status
            data.setRefSystemUserLoginStatus(parentController.getFUserAccountManager().getDataLoginStatus(data.getRefSystemUserLoginStatus().getIdstatus()));
        }

        return FXCollections.observableArrayList(parentController.getFUserAccountManager().getAllDataUserAccount());
    }

    private void setTableControlDataUserAccount() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataUserAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataUserAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataUserAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataUserAccount.addButtonControl(buttonControls);
    }

    @FXML
    private AnchorPane formAnchorUserAccount;
    @FXML
    private ScrollPane spFormDataUserAccount;
    @FXML
    private GridPane gpFormDataUserAccount;
    @FXML
    private Label lblCodeData;
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private Label lblCodeEmployee;
    @FXML
    private JFXTextField txtNameJob;
    @FXML
    private ImageView imageEmployee;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    @FXML
    private AnchorPane ancEmployeeLayout;
    private JFXCComboBoxTablePopup<TblEmployee> cbpEmployee;
    
    @FXML
    private AnchorPane ancRoleLayout;
    private JFXCComboBoxTablePopup<TblSystemRole> cbpRole;
    
    @FXML
    private AnchorPane ancStatusLockLayout;
    private JFXCComboBoxTablePopup<RefSystemUserLockStatus> cbpStatusLock;

    private TblSystemUser selectedData;
    private BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private String imgSourcePath;
    private String urlImage;

    private void initFormDataUserAccount() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataUserAccount.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataUserAccount.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("Err:" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        initDataPopup();

        btnSave.setOnAction((e) -> {
            dataUserAccountSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
            dataUserAccountCancelHandle();
        });

        initImportantFieldColor();
        
        lblCodeEmployee.setVisible(false);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtUserName,
                txtPassword,
                cbpEmployee,
                cbpRole,
                cbpStatusLock);
    }

    private void initDataPopup() {
        //Employee
        TableView<TblEmployee> tableEmployee = new TableView();

        TableColumn<TblEmployee, String> codeEmployee = new TableColumn("ID Karyawan");
        codeEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        codeEmployee.setMinWidth(140);

        TableColumn<TblEmployee, String> nameEmployee = new TableColumn("Nama Karyawan");
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().getTblPeople().fullNameProperty()));
        nameEmployee.setMinWidth(140);

        TableColumn<TblEmployee, String> nameJob = new TableColumn("Jabatan");
        nameJob.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblJob().getJobName(), param.getValue().tblJobProperty()));
        nameJob.setMinWidth(140);

        tableEmployee.getColumns().addAll(codeEmployee, nameEmployee);

        ObservableList<TblEmployee> codeEmployeeItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpEmployee = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableEmployee, codeEmployeeItems, "", "Karyawan *", true, 440, 200
        );

        //Role
        TableView<TblSystemRole> tableRole = new TableView();

        TableColumn<TblSystemRole, String> nameRole = new TableColumn("Role");
        nameRole.setCellValueFactory(cellData -> cellData.getValue().roleNameProperty());
        nameRole.setMinWidth(140);

        tableRole.getColumns().addAll(nameRole);

        ObservableList<TblSystemRole> systemRoleItems = FXCollections.observableArrayList(parentController.getFUserAccountManager().getAllDataRole());

        cbpRole = new JFXCComboBoxTablePopup<>(
                TblSystemRole.class, tableRole, systemRoleItems, "", "Role *", true, 200, 200
        );

        //User Lock Status
        TableView<RefSystemUserLockStatus> tableUserLock = new TableView();

        TableColumn<RefSystemUserLockStatus, String> nameUserLock = new TableColumn("Status-Lock");
        nameUserLock.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        nameUserLock.setMinWidth(140);

        tableUserLock.getColumns().addAll(nameUserLock);

        ObservableList<RefSystemUserLockStatus> userLockItems = FXCollections.observableArrayList(parentController.getFUserAccountManager().getAllDataLockStatus());

        cbpStatusLock = new JFXCComboBoxTablePopup<>(
                RefSystemUserLockStatus.class, tableUserLock, userLockItems, "", "Status-Lock *", true, 200, 200
        );
        
        //attached
        ancEmployeeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpEmployee, 0.0);
        AnchorPane.setTopAnchor(cbpEmployee, 0.0);
        ancEmployeeLayout.getChildren().add(cbpEmployee);
        
        ancRoleLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpRole, 0.0);
        AnchorPane.setLeftAnchor(cbpRole, 0.0);
        AnchorPane.setRightAnchor(cbpRole, 0.0);
        AnchorPane.setTopAnchor(cbpRole, 0.0);
        ancRoleLayout.getChildren().add(cbpRole);
        
        ancStatusLockLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpStatusLock, 0.0);
        AnchorPane.setLeftAnchor(cbpStatusLock, 0.0);
        AnchorPane.setRightAnchor(cbpStatusLock, 0.0);
        AnchorPane.setTopAnchor(cbpStatusLock, 0.0);
        ancStatusLockLayout.getChildren().add(cbpStatusLock);
    }

    private List<TblEmployee> loadAllDataEmployee() {
        List<TblEmployee> list = parentController.getFUserAccountManager().getAllDataEmployee();
        for (TblEmployee data : list) {
            //data people
            data.setTblPeople(parentController.getFUserAccountManager().getDataPeople(data.getTblPeople().getIdpeople()));
            //data job
            data.setTblJob(parentController.getFUserAccountManager().getDataJob(data.getTblJob().getIdjob()));
        }
        return list;
    }

    public int inputDataStatus = 0;

    private void refeshDataPopup() {
        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
        cbpEmployee.setItems(employeeItems);
        ObservableList<TblSystemRole> roleItems = FXCollections.observableArrayList(parentController.getFUserAccountManager().getAllDataRole());
        cbpRole.setItems(roleItems);
        ObservableList<RefSystemUserLockStatus> lockItems = FXCollections.observableArrayList(parentController.getFUserAccountManager().getAllDataLockStatus());
        cbpStatusLock.setItems(lockItems);
    }

    private void setSelectedDataToInputForm() {
        refeshDataPopup();
        
        lblCodeData.setText(selectedData.getCodeUser() != null 
                ? "" : "");
        
        txtUserName.textProperty().bindBidirectional(selectedData.codeUserProperty());
        txtPassword.textProperty().bindBidirectional(selectedData.userPasswordProperty());

        imageEmployee.setPreserveRatio(false);
        if (selectedData.getUserUrlImage() == null || selectedData.getUserUrlImage().equals("")) {
            imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + "no_profile_img.gif";
            imageEmployee.setImage(new Image("file:///" + imgSourcePath));
        } else {
            imgSourcePath = ClassFolderManager.imageClientRootPath + "/" + selectedData.getUserUrlImage();
            imageEmployee.setImage(new Image("file:///" + imgSourcePath));
        }

        if (selectedData.getTblEmployeeByIdemployee() != null) {
            lblCodeEmployee.setText("(" + selectedData.getTblEmployeeByIdemployee().getCodeEmployee() + ")");
            txtNameJob.setText(parentController.getFUserAccountManager().getDataJob(selectedData.getTblEmployeeByIdemployee().getTblJob().getIdjob()).getJobName());
        } else {
            lblCodeEmployee.setText("");
            txtNameJob.setText("");
        }

        cbpEmployee.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblCodeEmployee.setText("(" + newVal.getCodeEmployee() + ")");
                txtNameJob.setText(parentController.getFUserAccountManager().getDataJob(newVal.getTblJob().getIdjob()).getJobName());

//                urlImage = parentController.getFUserAccountManager().getDataPeople(newVal.getTblPeople().getIdpeople()).getImageUrl();
//                selectedData.setUserUrlImage(urlImage);
//                imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + urlImage;
//                imageEmployee.setImage(new Image("file:///" + imgSourcePath));
            }

        });
        cbpEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());

        cbpRole.valueProperty().bindBidirectional(selectedData.tblSystemRoleProperty());
        cbpStatusLock.valueProperty().bindBidirectional(selectedData.refSystemUserLockStatusProperty());

        cbpEmployee.hide();
        cbpRole.hide();
        cbpStatusLock.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtNameJob.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataUserAccount,
                inputDataStatus == 3,
                txtNameJob);

        btnSave.setVisible(inputDataStatus != 3);
    }

    private void dataUserAccountCreateHandle() {
        inputDataStatus = 0;
        selectedData = new TblSystemUser();
        setSelectedDataToInputForm();
        dataUserAccountFormShowStatus.set(0);
        dataUserAccountFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataUserAccountUpdateHandle() {
        if (tableDataUserAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            inputDataStatus = 1;
            selectedData = parentController.getFUserAccountManager().getDataUserAccount(((TblSystemUser) tableDataUserAccount.getTableView().getSelectionModel().getSelectedItem()).getIduser());
            setSelectedDataToInputForm();
            dataUserAccountFormShowStatus.set(0);
            dataUserAccountFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataUserAccountShowHandle() {
        if (tableDataUserAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            inputDataStatus = 3;
            selectedData = parentController.getFUserAccountManager().getDataUserAccount(((TblSystemUser) tableDataUserAccount.getTableView().getSelectionModel().getSelectedItem()).getIduser());
            setSelectedDataToInputForm();
            dataUserAccountFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataUserAccountUnshowHandle() {
        refreshDataTableUserAccount();
        dataUserAccountFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataUserAccountDeleteHandle() {
        if (tableDataUserAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFUserAccountManager().deleteDataUserAccount((TblSystemUser) tableDataUserAccount.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    refreshDataTableUserAccount();
                    dataUserAccountFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFUserAccountManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataUserAccountSaveHandle() {
        if (checkDataInputDataUserAccount()) {
            if (selectedData.getRefSystemUserLoginStatus() == null
                    || selectedData.getRefSystemUserLoginStatus().getIdstatus() != 1) {    //Login = '1'
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    //dummy entry
                    TblSystemUser dummySelectedData = new TblSystemUser(selectedData);
                    dummySelectedData.setTblSystemRole(new TblSystemRole(dummySelectedData.getTblSystemRole()));
                    dummySelectedData.setTblEmployeeByIdemployee(new TblEmployee(dummySelectedData.getTblEmployeeByIdemployee()));
                    switch (inputDataStatus) {
                        case 0:
                            if (parentController.getFUserAccountManager().insertDataUserAccount(dummySelectedData) != null) {
                                ClassMessage.showSucceedInsertingDataMessage("", null);

                                refreshDataTableUserAccount();
                                dataUserAccountFormShowStatus.set(0);
                                //set unsaving data input -> 'false'
                                ClassSession.unSavingDataInput.set(false);
                            } else {
                                ClassMessage.showFailedInsertingDataMessage(parentController.getFUserAccountManager().getErrorMessage(), null);
                            }
                            break;

                        case 1:
                            if (parentController.getFUserAccountManager().updateDataUserAccount(dummySelectedData)) {
                                ClassMessage.showSucceedUpdatingDataMessage("", null);
                                refreshDataTableUserAccount();

                                dataUserAccountFormShowStatus.set(0);
                                //set unsaving data input -> 'false'
                                ClassSession.unSavingDataInput.set(false);
                            } else {
                                ClassMessage.showFailedUpdatingDataMessage(parentController.getFUserAccountManager().getErrorMessage(), null);
                            }
                            break;
                    }
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN PERUBAHAN DATA", "User sedang 'Login', silahkan 'Logout'-kan terlebih dahulu'..!!", "");
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataUserAccountCancelHandle() {
        //refresh data from table & close form data user account
        refreshDataTableUserAccount();
        dataUserAccountFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableUserAccount() {
        tableDataUserAccount.getTableView().setItems(loadAllDataUserAccount());
        cft.refreshFilterItems(tableDataUserAccount.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataUserAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtUserName.getText() == null || txtUserName.getText().equals("")) {
            dataInput = false;
            errDataInput += "User Name : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtPassword.getText() == null || txtPassword.getText().equals("")) {
            dataInput = false;
            errDataInput += "Password : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            int defaultMinimumUserAccountPasswordLength = 0;
            SysDataHardCode sdhDafaultMinimumUserAccountPasswordLength = parentController.getFUserAccountManager().getDataSysDataHardCode(10);    //DefaultMinimumUserAccountPasswordLength = '10'
            if (sdhDafaultMinimumUserAccountPasswordLength != null
                    && sdhDafaultMinimumUserAccountPasswordLength.getDataHardCodeValue() != null) {
                defaultMinimumUserAccountPasswordLength = Integer.parseInt(sdhDafaultMinimumUserAccountPasswordLength.getDataHardCodeValue());
            }
            if (txtPassword.getText().length() < defaultMinimumUserAccountPasswordLength) {
                dataInput = false;
                errDataInput += "Password : Panjang password tidak dapat kurang dari " + defaultMinimumUserAccountPasswordLength + " \n";
            }
        }
        if (cbpEmployee.getValue() == null) {
            dataInput = false;
            errDataInput += "ID Karyawan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpRole.getValue() == null) {
            dataInput = false;
            errDataInput += "Role : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpStatusLock.getValue() == null) {
            dataInput = false;
            errDataInput += "Status-Lock : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataUserAccountSplitPane();
        initTableDataUserAccount();
        initFormDataUserAccount();

        spDataUserAccount.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataUserAccountFormShowStatus.set(0);
        });

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UserAccountController(FeatureUserAccountController parentController) {
        this.parentController = parentController;
    }

    private final FeatureUserAccountController parentController;

}
