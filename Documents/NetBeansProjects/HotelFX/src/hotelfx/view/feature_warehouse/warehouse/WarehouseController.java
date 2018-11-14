/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse.warehouse;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_location.FeatureLocationController;
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
public class WarehouseController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataWarehouse;

    private DoubleProperty dataWarehouseFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataWarehouseLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataWarehouseSplitpane() {
        spDataWarehouse.setDividerPositions(1);

        dataWarehouseFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataWarehouseFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataWarehouse.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataWarehouse.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataWarehouseFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataWarehouseLayout.setDisable(false);
                    tableDataWarehouseLayoutDisableLayer.setDisable(true);
                    tableDataWarehouseLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataWarehouseLayout.setDisable(true);
                    tableDataWarehouseLayoutDisableLayer.setDisable(false);
                    tableDataWarehouseLayoutDisableLayer.toFront();
                }
            }
        });

        dataWarehouseFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataWarehouseLayout;

    private ClassFilteringTable<TblLocationOfWarehouse> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataWarehouse;

    private void initTableDataWarehouse() {
        //set table
        setTableDataWarehouse();
        //set control
        setTableControlDataWarehouse();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataWarehouse, 15.0);
        AnchorPane.setLeftAnchor(tableDataWarehouse, 15.0);
        AnchorPane.setRightAnchor(tableDataWarehouse, 15.0);
        AnchorPane.setTopAnchor(tableDataWarehouse, 15.0);
        ancBodyLayout.getChildren().add(tableDataWarehouse);
    }

    private void setTableDataWarehouse() {
        TableView<TblLocationOfWarehouse> tableView = new TableView();

//        TableColumn<TblLocationOfWarehouse, String> codeLocation = new TableColumn("ID");
//        codeLocation.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfWarehouse, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getCodeLocation(), param.getValue().tblLocationProperty()));
//        codeLocation.setMinWidth(120);
        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(140);

        TableColumn<TblLocationOfWarehouse, String> buildingName = new TableColumn("Gedung");
        buildingName.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfWarehouse, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblBuilding().getBuildingName(), param.getValue().getTblLocation().tblBuildingProperty()));
        buildingName.setMinWidth(140);

        TableColumn<TblLocationOfWarehouse, String> floorName = new TableColumn("Lantai");
        floorName.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfWarehouse, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblFloor().getFloorName(), param.getValue().getTblLocation().tblFloorProperty()));
        floorName.setMinWidth(140);

        TableColumn<TblLocationOfWarehouse, String> warehouseNote = new TableColumn("Keterangan");
        warehouseNote.setCellValueFactory(cellData -> cellData.getValue().warehouseNoteProperty());
        warehouseNote.setMinWidth(200);

        tableView.getColumns().addAll(warehouseName, buildingName, floorName, warehouseNote);
        tableView.setItems(loadAllDataWarehouse());

        tableView.setRowFactory(tv -> {
            TableRow<TblLocationOfWarehouse> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataWarehouseUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataWarehouseUpdateHandle();
                            } else {
                                dataWarehouseShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataWarehouseUpdateHandle();
//                            } else {
//                                dataWarehouseShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataWarehouse = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblLocationOfWarehouse.class,
                tableDataWarehouse.getTableView(),
                tableDataWarehouse.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataWarehouse() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataWarehouseCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataWarehouseUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataWarehouseDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataWarehousePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataWarehouse.addButtonControl(buttonControls);
    }

    private ObservableList<TblLocationOfWarehouse> loadAllDataWarehouse() {
        List<TblLocationOfWarehouse> list = parentController.getFWarehouseManager().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(parentController.getFWarehouseManager().getLocation(data.getTblLocation().getIdlocation()));
            //data building
            data.getTblLocation().setTblBuilding(parentController.getFWarehouseManager().getDataBuilding(data.getTblLocation().getTblBuilding().getIdbuilding()));
            //data floor
            data.getTblLocation().setTblFloor(parentController.getFWarehouseManager().getDataFloor(data.getTblLocation().getTblFloor().getIdfloor()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataWarehouse;

    @FXML
    private ScrollPane spFormDataWarehouse;

    @FXML
    private JFXTextField txtWarehouseName;

    @FXML
    private JFXTextField txtIdWarehouse;
    @FXML
    private JFXTextArea txtWarehouseNote;

    @FXML
    private AnchorPane floorLayout;
    private JFXCComboBoxTablePopup<TblFloor> cbpFloor;

    @FXML
    private AnchorPane buildingLayout;
    private JFXCComboBoxTablePopup<TblBuilding> cbpBuilding;

    @FXML
    private JFXCheckBox chbJM;
    
    @FXML
    private AnchorPane ancGroupLayout;
    private JFXCComboBoxTablePopup<TblGroup> cbpGroup;
    
    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblLocationOfWarehouse selectedData;

    private void initFormDataWarehouse() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataWarehouse.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataWarehouse.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Gudang)"));
        btnSave.setOnAction((e) -> {
            dataWarehouseSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataWarehouseCancelHandle();
        });

        chbJM.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                cbpGroup.setValue(null);
                cbpGroup.setVisible(true);
            }else{
                cbpGroup.setValue(null);
                cbpGroup.setVisible(false);
            }
        });
        chbJM.setSelected(false);
        cbpGroup.setVisible(false);
        
        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtWarehouseName,
                cbpFloor,
                cbpBuilding, 
                cbpGroup);
    }

    private void initDataPopup() {
        TableView<TblFloor> tableFloor = new TableView();

        TableColumn<TblFloor, String> floorName = new TableColumn<>("Lantai");
        floorName.setCellValueFactory(cellData -> cellData.getValue().floorNameProperty());
        floorName.setMinWidth(140);

        tableFloor.getColumns().addAll(floorName);

        TableView<TblBuilding> tableBuilding = new TableView();

        TableColumn<TblBuilding, String> codeBuilding = new TableColumn<>("ID");
        codeBuilding.setCellValueFactory(cellData -> cellData.getValue().codeBuildingProperty());
        codeBuilding.setMinWidth(120);

        TableColumn<TblBuilding, String> buildingName = new TableColumn<>("Gedung");
        buildingName.setCellValueFactory(cellData -> cellData.getValue().buildingNameProperty());
        buildingName.setMinWidth(140);

        tableBuilding.getColumns().addAll(codeBuilding, buildingName);
        
        TableView<TblGroup> tableGroup = new TableView();

        TableColumn<TblGroup, String> groupName = new TableColumn<>("Department");
        groupName.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
        groupName.setMinWidth(140);

        tableGroup.getColumns().addAll(groupName);

        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataFloor());
        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataBuilding());
        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataGroup());

        cbpFloor = new JFXCComboBoxTablePopup<>(
                TblFloor.class, tableFloor, floorItems, "", "Lantai *", true, 200, 200
        );
        cbpBuilding = new JFXCComboBoxTablePopup<>(
                TblBuilding.class, tableBuilding, buildingItems, "", "Gedung *", true, 270, 200
        );
        cbpGroup = new JFXCComboBoxTablePopup<>(
                TblGroup.class, tableGroup, groupItems, "", "Department *", true, 200, 200
        );

        floorLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpFloor, 0.0);
        AnchorPane.setBottomAnchor(cbpFloor, 0.0);
        AnchorPane.setLeftAnchor(cbpFloor, 0.0);
        AnchorPane.setRightAnchor(cbpFloor, 0.0);
        floorLayout.getChildren().add(cbpFloor);

        buildingLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpBuilding, 0.0);
        AnchorPane.setLeftAnchor(cbpBuilding, 0.0);
        AnchorPane.setRightAnchor(cbpBuilding, 0.0);
        AnchorPane.setBottomAnchor(cbpBuilding, 0.0);
        buildingLayout.getChildren().add(cbpBuilding);
        
        ancGroupLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpGroup, 0.0);
        AnchorPane.setLeftAnchor(cbpGroup, 0.0);
        AnchorPane.setRightAnchor(cbpGroup, 0.0);
        AnchorPane.setBottomAnchor(cbpGroup, 0.0);
        ancGroupLayout.getChildren().add(cbpGroup);
    }

    private void refreshDataPopup() {
        //Location
       /* ObservableList<TblLocation> locationItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataLocation());
         cbpLocation.setItems(locationItems);*/

        //Floor
        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataFloor());
        cbpFloor.setItems(floorItems);

        //Building
        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataBuilding());
        cbpBuilding.setItems(buildingItems);
        
        //Group
        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataGroup());
        cbpGroup.setItems(groupItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtWarehouseName.textProperty().bindBidirectional(selectedData.warehouseNameProperty());
        txtWarehouseNote.textProperty().bindBidirectional(selectedData.warehouseNoteProperty());

        chbJM.setSelected(selectedData.getTblLocation().getTblGroup() != null);
        
        cbpFloor.valueProperty().bindBidirectional(selectedData.getTblLocation().tblFloorProperty());
        cbpBuilding.valueProperty().bindBidirectional(selectedData.getTblLocation().tblBuildingProperty());
        cbpGroup.valueProperty().bindBidirectional(selectedData.getTblLocation().tblGroupProperty());

        cbpFloor.hide();
        cbpBuilding.hide();
        cbpGroup.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataWarehouse, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataWarehouseCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblLocationOfWarehouse();
        selectedData.setTblLocation(new TblLocation());
        setSelectedDataToInputForm();
        //open form data warehouse
        dataWarehouseFormShowStatus.set(0);
        dataWarehouseFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataWarehouseUpdateHandle() {
        if (tableDataWarehouse.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFWarehouseManager().getWarehouse(((TblLocationOfWarehouse) tableDataWarehouse.getTableView().getSelectionModel().getSelectedItem()).getIdwarehouse());
            selectedData.setTblLocation(parentController.getFWarehouseManager().getLocation(selectedData.getTblLocation().getIdlocation()));
            selectedData.getTblLocation().setTblFloor(parentController.getFWarehouseManager().getDataFloor(selectedData.getTblLocation().getTblFloor().getIdfloor()));
            selectedData.getTblLocation().setTblBuilding(parentController.getFWarehouseManager().getDataBuilding(selectedData.getTblLocation().getTblBuilding().getIdbuilding()));
            setSelectedDataToInputForm();
            //open form data warehouse
            dataWarehouseFormShowStatus.set(0);
            dataWarehouseFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataWarehouseShowHandle() {
        if (tableDataWarehouse.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFWarehouseManager().getWarehouse(((TblLocationOfWarehouse) tableDataWarehouse.getTableView().getSelectionModel().getSelectedItem()).getIdwarehouse());
            selectedData.setTblLocation(parentController.getFWarehouseManager().getLocation(selectedData.getTblLocation().getIdlocation()));
            selectedData.getTblLocation().setTblFloor(parentController.getFWarehouseManager().getDataFloor(selectedData.getTblLocation().getTblFloor().getIdfloor()));
            selectedData.getTblLocation().setTblBuilding(parentController.getFWarehouseManager().getDataBuilding(selectedData.getTblLocation().getTblBuilding().getIdbuilding()));
            setSelectedDataToInputForm();
            dataWarehouseFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataWarehouseUnshowHandle() {
        refreshDatTableWarehouse();
        dataWarehouseFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataWarehouseDeleteHandle() {
        if (tableDataWarehouse.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (!dataHavingUsedWithAnotherData(((TblLocationOfWarehouse) tableDataWarehouse.getTableView().getSelectionModel().getSelectedItem()).getTblLocation())) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    if (parentController.getFWarehouseManager().deleteDataWarehouse((TblLocationOfWarehouse) tableDataWarehouse.getTableView().getSelectionModel().getSelectedItem())) {
                        //refresh data from table & close form data warehouse
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        refreshDatTableWarehouse();
                        dataWarehouseFormShowStatus.set(0);
                    } else {
                        ClassMessage.showFailedDeletingDataMessage(parentController.getFWarehouseManager().getErrorMessage(), null);
                    }
                } else {
                    dataWarehouseFormShowStatus.set(0);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sedang digunakan sebagai data 'Lokasi Front-Office',\n"
                        + "tidak dapat dihapus ..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    //check data was having used by sys-data-hardcode (location: 'front-office')
    private boolean dataHavingUsedWithAnotherData(TblLocation dataLocation) {
        boolean havingUsed = false;
        if (dataLocation != null) {
            SysDataHardCode sdhLocationFrontOffice = parentController.getFWarehouseManager().getDataSysDataHardCode(2);    //IDFrontOffice = '2'
            if (sdhLocationFrontOffice != null
                    && sdhLocationFrontOffice.getDataHardCodeValue() != null
                    && sdhLocationFrontOffice.getDataHardCodeValue().equals(String.valueOf(dataLocation.getIdlocation()))) {
                havingUsed = true;
            }
        }
        return havingUsed;
    }

    private void dataWarehousePrintHandle() {

    }

    private void dataWarehouseSaveHandle() {
        if (checkDataInputDataWarehouse()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblLocationOfWarehouse dummySelectedData = new TblLocationOfWarehouse(selectedData);
                dummySelectedData.setTblLocation(new TblLocation(dummySelectedData.getTblLocation()));
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFWarehouseManager().insertDataWarehouse(dummySelectedData) != null) {
                            //refresh data from table & close form data warehouse
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            refreshDatTableWarehouse();
                            dataWarehouseFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFWarehouseManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFWarehouseManager().updateDataWarehouse(dummySelectedData)) {
                            //refresh data from table & close form data warehouse
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            refreshDatTableWarehouse();
                            dataWarehouseFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFWarehouseManager().getErrorMessage(), null);
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

    private void dataWarehouseCancelHandle() {
        //refresh data from table & close form data warehouse
        refreshDatTableWarehouse();
        dataWarehouseFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDatTableWarehouse() {
        tableDataWarehouse.getTableView().setItems(loadAllDataWarehouse());
        cft.refreshFilterItems(tableDataWarehouse.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataWarehouse() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtWarehouseName.getText() == null || txtWarehouseName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Gudang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpFloor.getValue() == null) {
            dataInput = false;
            errDataInput += "Lantai : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBuilding.getValue() == null) {
            dataInput = false;
            errDataInput += "Gedung : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if(chbJM.isSelected()
                && cbpGroup.getValue() == null){
            dataInput = false;
            errDataInput += "Department : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        setDataWarehouseSplitpane();

        //init table
        initTableDataWarehouse();

        //init form
        initFormDataWarehouse();

        spDataWarehouse.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataWarehouseFormShowStatus.set(0);
        });
    }

    public WarehouseController(FeatureLocationController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLocationController parentController;

}
