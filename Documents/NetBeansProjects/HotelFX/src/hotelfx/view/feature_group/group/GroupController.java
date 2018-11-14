/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_group.group;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintDataGroup;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblLocation;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_group.FeatureGroupController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
public class GroupController implements Initializable {

    @FXML
    private SplitPane spDataGroup;
    private DoubleProperty dataGroupFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataGroupLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataGroupSplitPane() {
        spDataGroup.setDividerPositions(1);

        dataGroupFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataGroupFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataGroup.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataGroup.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataGroupFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    tableDataGroupLayout.setDisable(false);
                    tableDataGroupLayoutDisableLayer.setDisable(true);
                    tableDataGroupLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataGroupLayout.setDisable(true);
                    tableDataGroupLayoutDisableLayer.setDisable(false);
                    tableDataGroupLayoutDisableLayer.toFront();
                }
            }
        });
        dataGroupFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataGroupLayout;

    private ClassFilteringTable<TblGroup> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataGroup;

    private void initTableDataGroup() {
        setTableDataGroup();
        setTableControlDataGroup();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataGroup, 15.0);
        AnchorPane.setLeftAnchor(tableDataGroup, 15.0);
        AnchorPane.setBottomAnchor(tableDataGroup, 15.0);
        AnchorPane.setRightAnchor(tableDataGroup, 15.0);

        ancBodyLayout.getChildren().add(tableDataGroup);
    }

    private void setTableDataGroup() {
        TableView<TblGroup> tableView = new TableView();

        TableColumn<TblGroup, String> codeGroup = new TableColumn("ID Department");
        codeGroup.setCellValueFactory(cellData -> cellData.getValue().codeGroupProperty());
        codeGroup.setMinWidth(120);

        TableColumn<TblGroup, String> nameGroup = new TableColumn("Department");
        nameGroup.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
        nameGroup.setMinWidth(140);

        TableColumn<TblGroup, String> groupNote = new TableColumn("Keterangan");
        groupNote.setCellValueFactory(cellData -> cellData.getValue().groupNoteProperty());
        groupNote.setMinWidth(200);

        tableView.getColumns().addAll(codeGroup, nameGroup, groupNote);

        tableView.setItems(loadAllDataGroup());

        tableView.setRowFactory(tv -> {
            TableRow<TblGroup> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataGroupUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataGroupUpdateHandle();
                            } else {
                                dataGroupShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                dataGroupUpdateHandle();
//                            } else {
//                                dataGroupShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataGroup = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblGroup.class,
                tableDataGroup.getTableView(),
                tableDataGroup.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataGroup() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataGroupCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataGroupUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataGroupDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                dataGroupPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataGroup.addButtonControl(buttonControls);
    }

    private ObservableList<TblGroup> loadAllDataGroup() {
        return FXCollections.observableArrayList(parentController.getFGroupManager().getAllDataGroup());
    }

    @FXML
    private AnchorPane anchorFormGroupLayout;

    @FXML
    private ScrollPane spFormDataGroup;

    @FXML
    private GridPane gpFormDataGroup;

    @FXML
    private JFXTextField txtCodeGroup;

    @FXML
    private JFXTextField txtNameGroup;

    @FXML
    private JFXTextArea txtNoteGroup;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    private TblGroup selectedData;

    private void initFormDataGroup() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataGroup.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataGroup.setOnScroll((ScrollEvent scroll) -> {
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
                    System.out.println("err:" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setTooltip(new Tooltip("Simpan (Data Department)"));
        btnSave.setOnAction((e) -> {
            dataGroupSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataGroupCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtNameGroup);
    }

    private void setSelectedDataToInputForm() {
        txtCodeGroup.textProperty().bindBidirectional(selectedData.codeGroupProperty());
        txtNameGroup.textProperty().bindBidirectional(selectedData.groupNameProperty());
        txtNoteGroup.textProperty().bindBidirectional(selectedData.groupNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeGroup.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataGroup,
                dataInputStatus == 3,
                txtCodeGroup);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    private int dataInputStatus = 0;

    private void dataGroupCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblGroup();
        setSelectedDataToInputForm();
        dataGroupFormShowStatus.set(0);
        dataGroupFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataGroupUpdateHandle() {
        if (tableDataGroup.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFGroupManager().getDataGroup(((TblGroup) tableDataGroup.getTableView().getSelectionModel().getSelectedItem()).getIdgroup());
            //System.out.println(selectedData.getGroupName());
            setSelectedDataToInputForm();
            dataGroupFormShowStatus.set(0.0);
            dataGroupFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataGroupShowHandle() {
        if (tableDataGroup.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFGroupManager().getDataGroup(((TblGroup) tableDataGroup.getTableView().getSelectionModel().getSelectedItem()).getIdgroup());
            setSelectedDataToInputForm();
            dataGroupFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataGroupUnshowHandle() {
        refreshDataTableGroup();
        dataGroupFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataGroupPrintHandle() {
        List<ClassPrintDataGroup> listPrintGroup = new ArrayList();
        for (int i = 0; i < tableDataGroup.getTableView().getItems().size(); i++) {
            TblGroup dataGroup = (TblGroup) tableDataGroup.getTableView().getItems().get(i);
            ClassPrintDataGroup dataPrintGroup = new ClassPrintDataGroup();
            dataPrintGroup.setIdGroup(dataGroup.getCodeGroup());
            dataPrintGroup.setNamaGroup(dataGroup.getGroupName());
            dataPrintGroup.setKeteranganGroup(dataGroup.getGroupNote() != null ? dataGroup.getGroupNote() : "-");
            listPrintGroup.add(dataPrintGroup);
        }
        ClassPrinter.printDataGroup(listPrintGroup);
    }

    private void dataGroupDeleteHandle() {
        if (tableDataGroup.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataEnableToDelete((TblGroup) tableDataGroup.getTableView().getSelectionModel().getSelectedItem())) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    if (parentController.getFGroupManager().deleteDataGroup(new TblGroup((TblGroup) tableDataGroup.getTableView().getSelectionModel().getSelectedItem()))) {
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        refreshDataTableGroup();
                        dataGroupFormShowStatus.set(0);
                    } else {
                        ClassMessage.showFailedDeletingDataMessage(parentController.getFGroupManager().getErrorMessage(), null);
                    }
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data masih digunakan..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataEnableToDelete(TblGroup dataGroup) {
        if(dataGroup != null){
            List<TblLocation> dataLocations = parentController.getFGroupManager().getAllDataLocationByIDGroup(dataGroup.getIdgroup());
            return dataLocations.isEmpty();
        }
        return true;
    }

    private void dataGroupSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblGroup dummySelectedData = new TblGroup(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFGroupManager().insertDataGroup(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            refreshDataTableGroup();
                            dataGroupFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFGroupManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFGroupManager().updateDataGroup(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            refreshDataTableGroup();
                            dataGroupFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFGroupManager().getErrorMessage(), null);
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

    private String errDataInput;

    private boolean checkDataInput() {
        boolean check = true;
        errDataInput = "";
        if (txtNameGroup.getText() == null || txtNameGroup.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Department : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return check;
    }

    private void dataGroupCancelHandle() {
        refreshDataTableGroup();
        dataGroupFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableGroup() {
        tableDataGroup.getTableView().setItems(loadAllDataGroup());
        cft.refreshFilterItems(tableDataGroup.getTableView().getItems());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataGroupSplitPane();
        initTableDataGroup();
        initFormDataGroup();

        spDataGroup.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataGroupFormShowStatus.set(0);
        });
    }

    public GroupController(FeatureGroupController parentController) {
        this.parentController = parentController;
    }

    private final FeatureGroupController parentController;
}
