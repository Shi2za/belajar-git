/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_location.location;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblLocation;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_location.FeatureLocationController;
import java.net.URL;
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
public class LocationController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataLocation;

    private DoubleProperty dataLocationFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataLocationLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataLocationSplitpane() {
        spDataLocation.setDividerPositions(1);
        
        dataLocationFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataLocationFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataLocation.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataLocation.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataLocationFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataLocationLayout.setDisable(false);
                    tableDataLocationLayoutDisableLayer.setDisable(true);
                    tableDataLocationLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataLocationLayout.setDisable(true);
                    tableDataLocationLayoutDisableLayer.setDisable(false);
                    tableDataLocationLayoutDisableLayer.toFront();
                }
            }
        });

        dataLocationFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataLocationLayout;

    private ClassTableWithControl tableDataLocation;

    private void initTableDataLocation() {
        //set table
        setTableDataLocation();
        //set control
        setTableControlDataLocation();
        //set table-control to content-view
        tableDataLocationLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataLocation, 15.0);
        AnchorPane.setLeftAnchor(tableDataLocation, 15.0);
        AnchorPane.setRightAnchor(tableDataLocation, 15.0);
        AnchorPane.setTopAnchor(tableDataLocation, 15.0);
        tableDataLocationLayout.getChildren().add(tableDataLocation);
    }

    private void setTableDataLocation() {
        TableView<TblLocation> tableView = new TableView();

        TableColumn<TblLocation, String> locationName = new TableColumn("Lokasi");
        locationName.setCellValueFactory(cellData -> cellData.getValue().locationNameProperty());
        locationName.setMinWidth(140);

        TableColumn<TblLocation, String> locationNote = new TableColumn("Keterangan");
        locationNote.setCellValueFactory(cellData -> cellData.getValue().locationNoteProperty());
        locationNote.setMinWidth(200);

        TableColumn<TblLocation, String> locationType = new TableColumn("Tipe Lokasi");
        locationType.setCellValueFactory((TableColumn.CellDataFeatures<TblLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefLocationType().getTypeName(), param.getValue().refLocationTypeProperty()));
        locationType.setMinWidth(140);

        TableColumn<TblLocation, String> locationBuilding = new TableColumn("Gedung");
        locationBuilding.setCellValueFactory((TableColumn.CellDataFeatures<TblLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBuilding().getBuildingName(), param.getValue().tblBuildingProperty()));
        locationBuilding.setMinWidth(140);

        TableColumn<TblLocation, String> locationFloor = new TableColumn("Lantai");
        locationFloor.setCellValueFactory((TableColumn.CellDataFeatures<TblLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblFloor().getFloorName(), param.getValue().tblFloorProperty()));
        locationFloor.setMinWidth(140);

        tableView.getColumns().addAll(locationName, locationNote, locationType, locationBuilding, locationFloor);
        tableView.setItems(loadAllDataLocation());

        tableView.setRowFactory(tv -> {
            TableRow<TblLocation> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataLocationUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataLocationUpdateHandle();
                            } else {
                                dataLocationShowHandle();
                            }
                        }
                    }
                } else {
                    if (!row.isEmpty()) {
                        if (isShowStatus.get()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataLocationUpdateHandle();
                            } else {
                                dataLocationShowHandle();
                            }
                        }
                    }
                }
            });
            return row;
        });

        tableDataLocation = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataLocation() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataLocationCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataLocationUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataLocationDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataLocationPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataLocation.addButtonControl(buttonControls);
    }

    private ObservableList<TblLocation> loadAllDataLocation() {
        return FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataLocation());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataLocation;

    @FXML
    private ScrollPane spFormDataLocation;

    @FXML
    private JFXTextField txtLocationName;

    @FXML
    private JFXTextArea txtLocationNote;

    private JFXCComboBoxTablePopup<RefLocationType> cbpType;

    private JFXCComboBoxTablePopup<TblBuilding> cbpBuilding;

    private JFXCComboBoxTablePopup<TblFloor> cbpFloor;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblLocation selectedData;

    private void initFormDataLocation() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataLocation.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataLocation.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Lokasi)"));
        btnSave.setOnAction((e) -> {
            dataLocationSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataLocationCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtLocationName,
                cbpType,
                cbpBuilding,
                cbpFloor);
    }

    private void initDataPopup() {
        //Type
        TableView<RefLocationType> tableType = new TableView<>();

        TableColumn<RefLocationType, String> typeName = new TableColumn<>("Tipe Lokasi");
        typeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        typeName.setMinWidth(140);

        tableType.getColumns().addAll(typeName);

        ObservableList<RefLocationType> typeItems = FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataLocationType());

        cbpType = new JFXCComboBoxTablePopup<>(
                RefLocationType.class, tableType, typeItems, "", "Tipe Lokasi *", true, 200, 200
        );

        //Building
        TableView<TblBuilding> tableBuilding = new TableView<>();

        TableColumn<TblBuilding, String> codeBuilding = new TableColumn<>("ID");
        codeBuilding.setCellValueFactory(cellData -> cellData.getValue().codeBuildingProperty());
        codeBuilding.setMinWidth(120);

        TableColumn<TblBuilding, String> buildingName = new TableColumn<>("Gedung");
        buildingName.setCellValueFactory(cellData -> cellData.getValue().buildingNameProperty());
        buildingName.setMinWidth(140);

        tableBuilding.getColumns().addAll(codeBuilding, buildingName);

        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataBuilding());

        cbpBuilding = new JFXCComboBoxTablePopup<>(
                TblBuilding.class, tableBuilding, buildingItems, "", "Gedung *", true, 280, 280
        );

        //Floor
        TableView<TblFloor> tableFloor = new TableView<>();

        TableColumn<TblFloor, String> floorName = new TableColumn<>("Lantai");
        floorName.setCellValueFactory(cellData -> cellData.getValue().floorNameProperty());
        floorName.setMinWidth(140);

        tableFloor.getColumns().addAll(floorName);

        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataFloor());

        cbpFloor = new JFXCComboBoxTablePopup<>(
                TblFloor.class, tableFloor, floorItems, "", "Lantai *", true, 200, 200
        );

        //attached to grid-pane
        gpFormDataLocation.add(cbpType, 2, 3);
        gpFormDataLocation.add(cbpBuilding, 1, 2);
        gpFormDataLocation.add(cbpFloor, 2, 2);
    }

    private void refreshDataPopup() {
        //Type
        ObservableList<RefLocationType> typeItems = FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataLocationType());
        cbpType.setItems(typeItems);

        //Building
        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataBuilding());
        cbpBuilding.setItems(buildingItems);
        //Floor
        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFLocationManager().getAllDataFloor());
        cbpFloor.setItems(floorItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtLocationName.textProperty().bindBidirectional(selectedData.locationNameProperty());
        txtLocationNote.textProperty().bindBidirectional(selectedData.locationNoteProperty());

        cbpBuilding.valueProperty().bindBidirectional(selectedData.tblBuildingProperty());
        cbpFloor.valueProperty().bindBidirectional(selectedData.tblFloorProperty());
        cbpType.valueProperty().bindBidirectional(selectedData.refLocationTypeProperty());

        cbpBuilding.hide();
        cbpFloor.hide();
        cbpType.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataLocation, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataLocationCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblLocation();
        setSelectedDataToInputForm();
        //open form data location
        dataLocationFormShowStatus.set(0);
        dataLocationFormShowStatus.set(1);
    }

    private void dataLocationUpdateHandle() {
        if (tableDataLocation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFLocationManager().getLocation(((TblLocation) tableDataLocation.getTableView().getSelectionModel().getSelectedItem()).getIdlocation());
            setSelectedDataToInputForm();
            //open form data location
            dataLocationFormShowStatus.set(0);
            dataLocationFormShowStatus.set(1);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataLocationShowHandle() {
        if (tableDataLocation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLocationManager().getLocation(((TblLocation) tableDataLocation.getTableView().getSelectionModel().getSelectedItem()).getIdlocation());
            setSelectedDataToInputForm();
            dataLocationFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataLocationUnshowHandle() {
        tableDataLocation.getTableView().setItems(loadAllDataLocation());
        dataLocationFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataLocationDeleteHandle() {
        if (tableDataLocation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFLocationManager().deleteDataLocation((TblLocation) tableDataLocation.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data location
                    tableDataLocation.getTableView().setItems(loadAllDataLocation());
                    dataLocationFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFLocationManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataLocationPrintHandle() {

    }

    private void dataLocationSaveHandle() {
        if (checkDataInputDataLocation()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblLocation dummySelectedData = new TblLocation(selectedData);
                dummySelectedData.setTblBuilding(new TblBuilding(dummySelectedData.getTblBuilding()));
                dummySelectedData.setTblFloor(new TblFloor(dummySelectedData.getTblFloor()));
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLocationManager().insertDataLocation(dummySelectedData) != null) {
                            //refresh data from table & close form data location
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            tableDataLocation.getTableView().setItems(loadAllDataLocation());
                            dataLocationFormShowStatus.set(0);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFLocationManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFLocationManager().updateDataLocation(dummySelectedData)) {
                            //refresh data from table & close form data location
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            tableDataLocation.getTableView().setItems(loadAllDataLocation());
                            dataLocationFormShowStatus.set(0);
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

    private void dataLocationCancelHandle() {
        //refresh data from table & close form data location
        tableDataLocation.getTableView().setItems(loadAllDataLocation());
        dataLocationFormShowStatus.set(0);
    }

    private String errDataInput;

    private boolean checkDataInputDataLocation() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtLocationName.getText() == null || txtLocationName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Lokasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Lokasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBuilding.getValue() == null) {
            dataInput = false;
            errDataInput += "Gedung : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpFloor.getValue() == null) {
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
        setDataLocationSplitpane();

        //init table
        initTableDataLocation();

        //init form
        initFormDataLocation();

        spDataLocation.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataLocationFormShowStatus.set(0);
        });
    }

    public LocationController(FeatureLocationController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLocationController parentController;

}
