/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_role.role;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblSystemRole;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_role.FeatureRoleController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
 *
 * @author Andreas
 */
public class RoleController implements Initializable {

    @FXML
    private SplitPane spDataRole;
    private DoubleProperty dataRoleFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRoleLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataRoleSplitPane() {
        spDataRole.setDividerPositions(1);

        dataRoleFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRoleFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRole.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRole.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRoleFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0) {
                    tableDataRoleLayout.setDisable(false);
                    tableDataRoleLayoutDisableLayer.setDisable(true);
                    tableDataRoleLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataRoleLayout.setDisable(true);
                    tableDataRoleLayoutDisableLayer.setDisable(false);
                    tableDataRoleLayoutDisableLayer.toFront();
                }
            }
        });
        dataRoleFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataRoleLayout;

    private ClassFilteringTable<TblSystemRole> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRole;

    private void initTableDataRole() {
        setTableDataRole();
        setTableControlDataRole();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataRole, 15.0);
        AnchorPane.setLeftAnchor(tableDataRole, 15.0);
        AnchorPane.setBottomAnchor(tableDataRole, 15.0);
        AnchorPane.setRightAnchor(tableDataRole, 15.0);

        ancBodyLayout.getChildren().add(tableDataRole);
    }

    private void setTableDataRole() {
        TableView<TblSystemRole> tableView = new TableView();

        TableColumn<TblSystemRole, String> nameRole = new TableColumn("Role");
        nameRole.setCellValueFactory(cellData -> cellData.getValue().roleNameProperty());
        nameRole.setMinWidth(140);

        TableColumn<TblSystemRole, String> roleNote = new TableColumn("Keterangan");
        roleNote.setCellValueFactory(cellData -> cellData.getValue().roleNoteProperty());
        roleNote.setMinWidth(200);

        tableView.getColumns().addAll(nameRole, roleNote);
        tableView.setItems(loadAllDataRole());

        tableView.setRowFactory(tv -> {
            TableRow<TblSystemRole> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataRoleUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataRoleUpdateHandle();
                            } else {
                                dataRoleShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                dataRoleUpdateHandle();
//                            }else{
//                                dataRoleShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataRole = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblSystemRole.class,
                tableDataRole.getTableView(),
                tableDataRole.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataRole() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataRoleCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataRoleUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataRoleDeleteHandle();
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
        tableDataRole.addButtonControl(buttonControls);
    }

    private ObservableList<TblSystemRole> loadAllDataRole() {
        return FXCollections.observableArrayList(parentController.getRoleManager().getAllDataRole());
    }

    @FXML
    private AnchorPane anchorFormRole;

    @FXML
    private ScrollPane spFormDataRole;

    @FXML
    private GridPane gpFormDataRole;

    @FXML
    private Label lblCodeData;
    
    @FXML
    private JFXTextField txtNameRole;

    @FXML
    private JFXTextArea txtRoleNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    private TblSystemRole selectedData;

    private void initFormDataRole() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spDataRole.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataRole.setOnScroll((ScrollEvent scroll) -> {
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
                } catch (Exception ex) {
                    System.out.println("err:" + ex.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setTooltip(new Tooltip("Simpan (Data Role)"));
        btnSave.setOnAction((e) -> {
            dataRoleSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoleCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtNameRole);
    }

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodeRole() != null 
                ? selectedData.getCodeRole() : "");
        
        txtNameRole.textProperty().bindBidirectional(selectedData.roleNameProperty());
        txtRoleNote.textProperty().bindBidirectional(selectedData.roleNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataRole, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
    }

    private int dataInputStatus = 0;

    private void dataRoleCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblSystemRole();
        setSelectedDataToInputForm();
        dataRoleFormShowStatus.set(0);
        dataRoleFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataRoleUpdateHandle() {
        if (tableDataRole.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getRoleManager().getDataRole(((TblSystemRole) tableDataRole.getTableView().getSelectionModel().getSelectedItem()).getIdrole());
            setSelectedDataToInputForm();
            dataRoleFormShowStatus.set(0);
            dataRoleFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataRoleShowHandle() {
        if (tableDataRole.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getRoleManager().getDataRole(((TblSystemRole) tableDataRole.getTableView().getSelectionModel().getSelectedItem()).getIdrole());
            setSelectedDataToInputForm();
            dataRoleFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataRoleUnshowHandle() {
        refreshDataTableRole();
        dataRoleFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataRoleDeleteHandle() {
        if (tableDataRole.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getRoleManager().deleteDataRole((TblSystemRole) tableDataRole.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    refreshDataTableRole();
                    dataRoleFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getRoleManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataRoleSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblSystemRole dummySelectedData = new TblSystemRole(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getRoleManager().insertDataRole(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            refreshDataTableRole();
                            dataRoleFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getRoleManager().getErrorMessage(), null);
                        }
                        break;

                    case 1:
                        if (parentController.getRoleManager().updateDataRole(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            refreshDataTableRole();
                            dataRoleFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getRoleManager().getErrorMessage(), null);
                        }
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataRoleCancelHandle() {
        refreshDataTableRole();
        dataRoleFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableRole() {
        tableDataRole.getTableView().setItems(loadAllDataRole());
        cft.refreshFilterItems(tableDataRole.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInput() {
        boolean check = true;
        errDataInput = "";
        if (txtNameRole.getText() == null || txtNameRole.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Role : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return check;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataRoleSplitPane();
        initTableDataRole();
        initFormDataRole();

        spDataRole.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRoleFormShowStatus.set(0);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public RoleController(FeatureRoleController parentController) {
        this.parentController = parentController;
    }

    private final FeatureRoleController parentController;
}
