/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_unit.unit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_unit.FeatureUnitController;
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
public class UnitController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataUnit;

    private DoubleProperty dataUnitFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataUnitLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataUnitSplitpane() {
        spDataUnit.setDividerPositions(1);

        dataUnitFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataUnitFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataUnit.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataUnit.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataUnitFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataUnitLayout.setDisable(false);
                    tableDataUnitLayoutDisableLayer.setDisable(true);
                    tableDataUnitLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataUnitLayout.setDisable(true);
                    tableDataUnitLayoutDisableLayer.setDisable(false);
                    tableDataUnitLayoutDisableLayer.toFront();
                }
            }
        });

        dataUnitFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataUnitLayout;

    private ClassFilteringTable<TblUnit> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataUnit;

    private void initTableDataUnit() {
        //set table
        setTableDataUnit();
        //set control
        setTableControlDataUnit();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataUnit, 15.0);
        AnchorPane.setLeftAnchor(tableDataUnit, 15.0);
        AnchorPane.setRightAnchor(tableDataUnit, 15.0);
        AnchorPane.setTopAnchor(tableDataUnit, 15.0);
        ancBodyLayout.getChildren().add(tableDataUnit);
    }

    private void setTableDataUnit() {
        TableView<TblUnit> tableView = new TableView();

        TableColumn<TblUnit, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        unitName.setMinWidth(140);

        TableColumn<TblUnit, String> unitNote = new TableColumn("Keterangan");
        unitNote.setCellValueFactory(cellData -> cellData.getValue().unitNoteProperty());
        unitNote.setMinWidth(200);

        tableView.getColumns().addAll(unitName, unitNote);
        tableView.setItems(loadAllDataUnit());

        tableView.setRowFactory(tv -> {
            TableRow<TblUnit> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataUnitUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataUnitUpdateHandle();
                            } else {
                                dataUnitShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataUnitUpdateHandle();
//                            } else {
//                                dataUnitShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataUnit = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblUnit.class,
                tableDataUnit.getTableView(),
                tableDataUnit.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataUnit() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataUnitCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataUnitUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataUnitDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataUnitPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataUnit.addButtonControl(buttonControls);
    }

    private ObservableList<TblUnit> loadAllDataUnit() {
        return FXCollections.observableArrayList(parentController.getFUnitManager().getAllDataUnit());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataUnit;

    @FXML
    private ScrollPane spFormDataUnit;

    @FXML
    private JFXTextField txtUnitName;

    @FXML
    private JFXTextArea txtUnitNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private AnchorPane floorLayout;

    @FXML
    private AnchorPane buildingLayout;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblUnit selectedData;

    private void initFormDataUnit() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataUnit.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataUnit.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Satuan)"));
        btnSave.setOnAction((e) -> {
            dataUnitSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataUnitCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtUnitName);
    }

    private void setSelectedDataToInputForm() {
        txtUnitName.textProperty().bindBidirectional(selectedData.unitNameProperty());
        txtUnitNote.textProperty().bindBidirectional(selectedData.unitNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataUnit, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataUnitCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblUnit();
        setSelectedDataToInputForm();
        //open form data unit
        dataUnitFormShowStatus.set(0.0);
        dataUnitFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataUnitUpdateHandle() {
        if (tableDataUnit.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFUnitManager().getUnit(((TblUnit) tableDataUnit.getTableView().getSelectionModel().getSelectedItem()).getIdunit());
            setSelectedDataToInputForm();
            //open form data unit
            dataUnitFormShowStatus.set(0.0);
            dataUnitFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataUnitShowHandle() {
        if (tableDataUnit.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFUnitManager().getUnit(((TblUnit) tableDataUnit.getTableView().getSelectionModel().getSelectedItem()).getIdunit());
            setSelectedDataToInputForm();
            dataUnitFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataUnitUnshowHandle() {
        refreshDataTableUnit();
        dataUnitFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataUnitDeleteHandle() {
        if (tableDataUnit.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFUnitManager().deleteDataUnit((TblUnit) tableDataUnit.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data unit
                    refreshDataTableUnit();
                    dataUnitFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFUnitManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataUnitPrintHandle() {

    }

    private void dataUnitSaveHandle() {
        if (checkDataInputDataUnit()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblUnit dummySelectedData = new TblUnit(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFUnitManager().insertDataUnit(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data unit
                            refreshDataTableUnit();
                            dataUnitFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFUnitManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFUnitManager().updateDataUnit(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data unit
                            refreshDataTableUnit();
                            dataUnitFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFUnitManager().getErrorMessage(), null);
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

    private void dataUnitCancelHandle() {
        //refresh data from table & close form data unit
        refreshDataTableUnit();
        dataUnitFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableUnit() {
        tableDataUnit.getTableView().setItems(loadAllDataUnit());
        cft.refreshFilterItems(tableDataUnit.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataUnit() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtUnitName.getText() == null || txtUnitName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Satuan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        setDataUnitSplitpane();

        //init table
        initTableDataUnit();

        //init form
        initFormDataUnit();

        spDataUnit.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataUnitFormShowStatus.set(0.0);
        });
    }

    public UnitController(FeatureUnitController parentController) {
        this.parentController = parentController;
    }

    private final FeatureUnitController parentController;

}
