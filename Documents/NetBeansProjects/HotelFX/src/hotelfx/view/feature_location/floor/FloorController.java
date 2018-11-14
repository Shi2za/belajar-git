/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_location.floor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblFloor;
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
public class FloorController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataFloor;

    private DoubleProperty dataFloorFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataFloorLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataFloorSplitpane() {
        spDataFloor.setDividerPositions(1);

        dataFloorFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataFloorFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataFloor.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataFloor.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataFloorFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataFloorLayout.setDisable(false);
                    tableDataFloorLayoutDisableLayer.setDisable(true);
                    tableDataFloorLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataFloorLayout.setDisable(true);
                    tableDataFloorLayoutDisableLayer.setDisable(false);
                    tableDataFloorLayoutDisableLayer.toFront();
                }
            }
        });

        dataFloorFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataFloorLayout;

    private ClassFilteringTable<TblFloor> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataFloor;

    private void initTableDataFloor() {
        //set table
        setTableDataFloor();
        //set control
        setTableControlDataFloor();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataFloor, 15.0);
        AnchorPane.setLeftAnchor(tableDataFloor, 15.0);
        AnchorPane.setRightAnchor(tableDataFloor, 15.0);
        AnchorPane.setTopAnchor(tableDataFloor, 15.0);
        ancBodyLayout.getChildren().add(tableDataFloor);
    }

    private void setTableDataFloor() {
        TableView<TblFloor> tableView = new TableView();

        TableColumn<TblFloor, String> floorName = new TableColumn("Lantai");
        floorName.setCellValueFactory(cellData -> cellData.getValue().floorNameProperty());
        floorName.setMinWidth(140);

        TableColumn<TblFloor, String> floorNote = new TableColumn("Keterangan");
        floorNote.setCellValueFactory(cellData -> cellData.getValue().floorNoteProperty());
        floorNote.setMinWidth(200);

        tableView.getColumns().addAll(floorName, floorNote);
        tableView.setItems(loadAllDataFloor());

        tableView.setRowFactory(tv -> {
            TableRow<TblFloor> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataFloorUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataFloorUpdateHandle();
                            } else {
                                dataFloorShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataFloorUpdateHandle();
//                            } else {
//                                dataFloorShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataFloor = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblFloor.class,
                tableDataFloor.getTableView(),
                tableDataFloor.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataFloor() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataFloorCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataFloorUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataFloorDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataFloorPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataFloor.addButtonControl(buttonControls);
    }

    private ObservableList<TblFloor> loadAllDataFloor() {
        return FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataFloor());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataFloor;

    @FXML
    private ScrollPane spFormDataFloor;

    @FXML
    private Label lblCodeData;
    
    @FXML
    private JFXTextField txtFloorName;

    @FXML
    private JFXTextArea txtFloorNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblFloor selectedData;

    private void initFormDataFloor() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataFloor.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataFloor.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Lantai)"));
        btnSave.setOnAction((e) -> {
            dataFloorSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataFloorCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtFloorName);
    }

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodeFloor() != null 
                ? "" : "");
        
        txtFloorName.textProperty().bindBidirectional(selectedData.floorNameProperty());
        txtFloorNote.textProperty().bindBidirectional(selectedData.floorNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataFloor, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataFloorCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblFloor();
        setSelectedDataToInputForm();
        //open form data floor
        dataFloorFormShowStatus.set(0.0);
        dataFloorFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataFloorUpdateHandle() {
        if (tableDataFloor.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFLocationManager().getFloor(((TblFloor) tableDataFloor.getTableView().getSelectionModel().getSelectedItem()).getIdfloor());
            setSelectedDataToInputForm();
            //open form data floor
            dataFloorFormShowStatus.set(0.0);
            dataFloorFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataFloorShowHandle() {
        if (tableDataFloor.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLocationManager().getFloor(((TblFloor) tableDataFloor.getTableView().getSelectionModel().getSelectedItem()).getIdfloor());
            setSelectedDataToInputForm();
            dataFloorFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataFloorUnshowHandle() {
        refreshDataTableFloor();
        dataFloorFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataFloorDeleteHandle() {
        if (tableDataFloor.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFLocationManager().deleteDataFloor((TblFloor) tableDataFloor.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data floor
                    refreshDataTableFloor();
                    dataFloorFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFLocationManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataFloorPrintHandle() {

    }

    private void dataFloorSaveHandle() {
        if (checkDataInputDataFloor()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblFloor dummySelectedData = new TblFloor(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLocationManager().insertDataFloor(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data floor
                            refreshDataTableFloor();
                            dataFloorFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFLocationManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFLocationManager().updateDataFloor(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data floor
                            refreshDataTableFloor();
                            dataFloorFormShowStatus.set(0.0);
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

    private void dataFloorCancelHandle() {
        //refresh data from table & close form data floor
        refreshDataTableFloor();
        dataFloorFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableFloor() {
        tableDataFloor.getTableView().setItems(loadAllDataFloor());
        cft.refreshFilterItems(tableDataFloor.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataFloor() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtFloorName.getText() == null || txtFloorName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Lantai : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        setDataFloorSplitpane();

        //init table
        initTableDataFloor();

        //init form
        initFormDataFloor();

        spDataFloor.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataFloorFormShowStatus.set(0.0);
        });
    }

    public FloorController(FeatureLocationController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLocationController parentController;

}
