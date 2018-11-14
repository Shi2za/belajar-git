/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_location.building;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_location.FeatureLocationController;
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
 * FXML Controller class
 *
 * @author ANDRI
 */
public class BuildingController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataBuilding;

    private DoubleProperty dataBuildingFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataBuildingLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataBuildingSplitpane() {
        spDataBuilding.setDividerPositions(1);

        dataBuildingFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataBuildingFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataBuilding.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataBuilding.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataBuildingFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataBuildingLayout.setDisable(false);
                    tableDataBuildingLayoutDisableLayer.setDisable(true);
                    tableDataBuildingLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataBuildingLayout.setDisable(true);
                    tableDataBuildingLayoutDisableLayer.setDisable(false);
                    tableDataBuildingLayoutDisableLayer.toFront();
                }
            }
        });

        dataBuildingFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataBuildingLayout;

    private ClassFilteringTable<TblBuilding> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataBuilding;

    private void initTableDataBuilding() {
        //set table
        setTableDataBuilding();
        //set control
        setTableControlDataBuilding();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataBuilding, 15.0);
        AnchorPane.setLeftAnchor(tableDataBuilding, 15.0);
        AnchorPane.setRightAnchor(tableDataBuilding, 15.0);
        AnchorPane.setTopAnchor(tableDataBuilding, 15.0);
        ancBodyLayout.getChildren().add(tableDataBuilding);
    }

    private void setTableDataBuilding() {
        TableView<TblBuilding> tableView = new TableView();

        TableColumn<TblBuilding, String> idBuilding = new TableColumn("ID");
        idBuilding.setCellValueFactory(cellData -> cellData.getValue().codeBuildingProperty());
        idBuilding.setMinWidth(120);

        TableColumn<TblBuilding, String> buildingName = new TableColumn("Gedung");
        buildingName.setCellValueFactory(cellData -> cellData.getValue().buildingNameProperty());
        buildingName.setMinWidth(140);

        TableColumn<TblBuilding, String> buildingNote = new TableColumn("Keterangan");
        buildingNote.setCellValueFactory(cellData -> cellData.getValue().buildingNoteProperty());
        buildingNote.setMinWidth(200);

        tableView.getColumns().addAll(idBuilding, buildingName, buildingNote);
        tableView.setItems(loadAllDataBuilding());

        tableView.setRowFactory(tv -> {
            TableRow<TblBuilding> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataBuildingUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataBuildingUpdateHandle();
                            } else {
                                dataBuildingShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataBuildingUpdateHandle();
//                            } else {
//                                dataBuildingShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataBuilding = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblBuilding.class,
                tableDataBuilding.getTableView(),
                tableDataBuilding.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataBuilding() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataBuildingCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataBuildingUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataBuildingDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataBuildingPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataBuilding.addButtonControl(buttonControls);
    }

    private ObservableList<TblBuilding> loadAllDataBuilding() {
        return FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataBuilding());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataBuilding;

    @FXML
    private ScrollPane spFormDataBuilding;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeBuilding;

    @FXML
    private JFXTextField txtBuildingName;

    @FXML
    private JFXTextArea txtBuildingNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblBuilding selectedData;

    private void initFormDataBuilding() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataBuilding.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataBuilding.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Gedung)"));
        btnSave.setOnAction((e) -> {
            dataBuildingSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataBuildingCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtBuildingName);
    }

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodeBuilding() != null
                ? selectedData.getCodeBuilding() : "");

        txtCodeBuilding.textProperty().bindBidirectional(selectedData.codeBuildingProperty());
        txtBuildingName.textProperty().bindBidirectional(selectedData.buildingNameProperty());
        txtBuildingNote.textProperty().bindBidirectional(selectedData.buildingNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeBuilding.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataBuilding,
                dataInputStatus == 3,
                txtCodeBuilding);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataBuildingCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblBuilding();
        setSelectedDataToInputForm();
        //open form data building
        dataBuildingFormShowStatus.set(0.0);
        dataBuildingFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataBuildingUpdateHandle() {
        if (tableDataBuilding.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFLocationManager().getBuilding(((TblBuilding) tableDataBuilding.getTableView().getSelectionModel().getSelectedItem()).getIdbuilding());
            setSelectedDataToInputForm();
            //open form data building
            dataBuildingFormShowStatus.set(0.0);
            dataBuildingFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataBuildingShowHandle() {
        if (tableDataBuilding.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLocationManager().getBuilding(((TblBuilding) tableDataBuilding.getTableView().getSelectionModel().getSelectedItem()).getIdbuilding());
            setSelectedDataToInputForm();
            dataBuildingFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataBuildingUnshowHandle() {
        refreshDataTableBuilding();
        dataBuildingFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataBuildingDeleteHandle() {
        if (tableDataBuilding.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFLocationManager().deleteDataBuilding((TblBuilding) tableDataBuilding.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data building
                    refreshDataTableBuilding();
                    dataBuildingFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFLocationManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataBuildingPrintHandle() {

    }

    private void dataBuildingSaveHandle() {
        if (checkDataInputDataBuilding()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblBuilding dummySelectedData = new TblBuilding(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLocationManager().insertDataBuilding(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data buiding
                            refreshDataTableBuilding();
                            dataBuildingFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFLocationManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFLocationManager().updateDataBuilding(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data building
                            refreshDataTableBuilding();
                            dataBuildingFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFLocationManager().getErrorMessage(), null);
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

    private void dataBuildingCancelHandle() {
        //refresh data from table & close form data building
        refreshDataTableBuilding();
        dataBuildingFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableBuilding() {
        tableDataBuilding.getTableView().setItems(loadAllDataBuilding());
        cft.refreshFilterItems(tableDataBuilding.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataBuilding() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtBuildingName.getText() == null || txtBuildingName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Gedung : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
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
        setDataBuildingSplitpane();

        //init table
        initTableDataBuilding();

        //init form
        initFormDataBuilding();

        spDataBuilding.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataBuildingFormShowStatus.set(0.0);
        });
    }

    public BuildingController(FeatureLocationController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLocationController parentController;

}
