/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_laundry.laundry;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
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
 *
 * @author Andreas
 */
public class LaundryController implements Initializable {

    @FXML
    private SplitPane spDataLaundry;

    private DoubleProperty dataLaundryFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataLaundryLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataLaundrySplitPane() {
        spDataLaundry.setDividerPositions(1);

        dataLaundryFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataLaundryFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataLaundry.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataLaundry.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataLaundryFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    tableDataLaundryLayout.setDisable(false);
                    tableDataLaundryLayoutDisableLayer.setDisable(true);
                    tableDataLaundryLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataLaundryLayout.setDisable(true);
                    tableDataLaundryLayoutDisableLayer.setDisable(false);
                    tableDataLaundryLayoutDisableLayer.toFront();
                }
            }
        });

        dataLaundryFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataLaundryLayout;

    private ClassFilteringTable<TblLocationOfLaundry> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataLaundry;

    private void initTableDataLaundry() {
        setTableDataLaundry();
        setTableControlDataLaundry();
        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataLaundry, 15.0);
        AnchorPane.setLeftAnchor(tableDataLaundry, 15.0);
        AnchorPane.setBottomAnchor(tableDataLaundry, 15.0);
        AnchorPane.setRightAnchor(tableDataLaundry, 15.0);

        ancBodyLayout.getChildren().add(tableDataLaundry);
    }

    private void setTableDataLaundry() {
        TableView<TblLocationOfLaundry> tableView = new TableView();

        TableColumn<TblLocationOfLaundry, String> codeLaundry = new TableColumn("ID");
        codeLaundry.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfLaundry, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getCodeLocation(), param.getValue().getTblLocation().codeLocationProperty()));
        codeLaundry.setMinWidth(120);

        TableColumn<TblLocationOfLaundry, String> nameLaundry = new TableColumn("Laundry");
        nameLaundry.setCellValueFactory(cellData -> cellData.getValue().laundryNameProperty());
        nameLaundry.setMinWidth(140);

        TableColumn<TblLocationOfLaundry, String> buildingName = new TableColumn("Gedung");
        buildingName.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfLaundry, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblBuilding().getBuildingName(), param.getValue().getTblLocation().tblBuildingProperty()));
        buildingName.setMinWidth(140);

        TableColumn<TblLocationOfLaundry, String> floorName = new TableColumn("Lantai");
        floorName.setCellValueFactory((TableColumn.CellDataFeatures<TblLocationOfLaundry, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblFloor().getFloorName(), param.getValue().getTblLocation().tblFloorProperty()));
        floorName.setMinWidth(140);

        TableColumn<TblLocationOfLaundry, String> noteLaundry = new TableColumn("Keterangan");
        noteLaundry.setCellValueFactory(cellData -> cellData.getValue().laundryNoteProperty());
        noteLaundry.setMinWidth(180);

        tableView.getColumns().addAll(nameLaundry, buildingName, floorName, noteLaundry);
        tableView.setItems(loadAllDataLaundry());

        tableView.setRowFactory(tv -> {
            TableRow<TblLocationOfLaundry> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataLaundryUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataLaundryUpdateHandle();
                            } else {
                                dataLaundryShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataLaundryUpdateHandle();
//                            } else {
//                                dataLaundryShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataLaundry = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblLocationOfLaundry.class,
                tableDataLaundry.getTableView(),
                tableDataLaundry.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private ObservableList<TblLocationOfLaundry> loadAllDataLaundry() {
        List<TblLocationOfLaundry> list = parentController.getFLaundryManager().getAllDataLaundry();
        for (TblLocationOfLaundry data : list) {
            //data location
            data.setTblLocation(parentController.getFLaundryManager().getLocation(data.getTblLocation().getIdlocation()));
            //data building
            data.getTblLocation().setTblBuilding(parentController.getFLaundryManager().getDataBuilding(data.getTblLocation().getTblBuilding().getIdbuilding()));
            //data floor
            data.getTblLocation().setTblFloor(parentController.getFLaundryManager().getDataFloor(data.getTblLocation().getTblFloor().getIdfloor()));
        }
        return FXCollections.observableArrayList(list);
    }

    private void setTableControlDataLaundry() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataLaundryCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataLaundryUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataLaundryDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataLaundryPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataLaundry.addButtonControl(buttonControls);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchorLaundry;

    @FXML
    private GridPane gpFormDataLaundry;

    @FXML
    private ScrollPane spFormDataLaundry;

    @FXML
    private JFXTextField txtLaundryName;

    @FXML
    private JFXTextField txtIdLaundry;

    @FXML
    private JFXTextArea txtLaundryNote;

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

    private TblLocationOfLaundry selectedData;

    private void initFormDataLaundry() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataLaundry.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataLaundry.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Laundry)"));
        btnSave.setOnAction((e) -> {
            dataLaundrySaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataLaundryCancelHandle();
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
                txtLaundryName,
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
        
        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFLaundryManager().getAllDataFloor());
        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFLaundryManager().getAllDataBuilding());
        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFLaundryManager().getAllDataGroup());

        cbpFloor = new JFXCComboBoxTablePopup<>(
                TblFloor.class, tableFloor, floorItems, "", "Lantai *", true, 200, 200
        );
        cbpBuilding = new JFXCComboBoxTablePopup<>(
                TblBuilding.class, tableBuilding, buildingItems, "", "Gedung *", true, 280, 200
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
        AnchorPane.setBottomAnchor(cbpBuilding, 0.0);
        AnchorPane.setLeftAnchor(cbpBuilding, 0.0);
        AnchorPane.setRightAnchor(cbpBuilding, 0.0);
        buildingLayout.getChildren().add(cbpBuilding);
        
        ancGroupLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpGroup, 0.0);
        AnchorPane.setBottomAnchor(cbpGroup, 0.0);
        AnchorPane.setLeftAnchor(cbpGroup, 0.0);
        AnchorPane.setRightAnchor(cbpGroup, 0.0);
        ancGroupLayout.getChildren().add(cbpGroup);
    }

    private void refreshDataPopup() {
        //Floor
        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFLaundryManager().getAllDataFloor());
        cbpFloor.setItems(floorItems);
        
        //Building
        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFLaundryManager().getAllDataBuilding());
        cbpBuilding.setItems(buildingItems);
        
        //Group
        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFLaundryManager().getAllDataGroup());
        cbpGroup.setItems(groupItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtLaundryName.textProperty().bindBidirectional(selectedData.laundryNameProperty());
        txtLaundryNote.textProperty().bindBidirectional(selectedData.laundryNoteProperty());

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
        ClassViewSetting.setDisableForAllInputNode(gpFormDataLaundry, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataLaundryCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblLocationOfLaundry();
        selectedData.setTblLocation(new TblLocation());
        setSelectedDataToInputForm();
        //open form data warehouse
        dataLaundryFormShowStatus.set(0);
        dataLaundryFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataLaundryUpdateHandle() {
        if (tableDataLaundry.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFLaundryManager().getLaundry(((TblLocationOfLaundry) tableDataLaundry.getTableView().getSelectionModel().getSelectedItem()).getIdlaundry());
            selectedData.setTblLocation(parentController.getFLaundryManager().getLocation(selectedData.getTblLocation().getIdlocation()));
            selectedData.getTblLocation().setTblFloor(parentController.getFLaundryManager().getDataFloor(selectedData.getTblLocation().getTblFloor().getIdfloor()));
            selectedData.getTblLocation().setTblBuilding(parentController.getFLaundryManager().getDataBuilding(selectedData.getTblLocation().getTblBuilding().getIdbuilding()));
            setSelectedDataToInputForm();
            //open form data warehouse
            dataLaundryFormShowStatus.set(0);
            dataLaundryFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataLaundryShowHandle() {
        if (tableDataLaundry.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLaundryManager().getLaundry(((TblLocationOfLaundry) tableDataLaundry.getTableView().getSelectionModel().getSelectedItem()).getIdlaundry());
            selectedData.setTblLocation(parentController.getFLaundryManager().getLocation(selectedData.getTblLocation().getIdlocation()));
            selectedData.getTblLocation().setTblFloor(parentController.getFLaundryManager().getDataFloor(selectedData.getTblLocation().getTblFloor().getIdfloor()));
            selectedData.getTblLocation().setTblBuilding(parentController.getFLaundryManager().getDataBuilding(selectedData.getTblLocation().getTblBuilding().getIdbuilding()));
            setSelectedDataToInputForm();
            dataLaundryFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataLaundryUnshowHandle() {
        refreshDataTableLaundry();
        dataLaundryFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataLaundryDeleteHandle() {
        if (tableDataLaundry.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFLaundryManager().deleteDataLaundry((TblLocationOfLaundry) tableDataLaundry.getTableView().getSelectionModel().getSelectedItem())) {
                    //refresh data from table & close form data laundry
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    refreshDataTableLaundry();
                    dataLaundryFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFLaundryManager().getErrorMessage(), null);
                }
            } else {
                dataLaundryFormShowStatus.set(0);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataLaundryPrintHandle() {

    }

    private void dataLaundrySaveHandle() {
        if (checkDataInputDataLaundry()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblLocationOfLaundry dummySelectedData = new TblLocationOfLaundry(selectedData);
                dummySelectedData.setTblLocation(new TblLocation(dummySelectedData.getTblLocation()));
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLaundryManager().insertDataLaundry(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            refreshDataTableLaundry();
                            dataLaundryFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFLaundryManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFLaundryManager().updateDataLaundry(dummySelectedData)) {
                            //refresh data from table & close form data warehouse
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            refreshDataTableLaundry();
                            dataLaundryFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFLaundryManager().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            } else if (alert.getResult() == ButtonType.CANCEL) {
                dataLaundryFormShowStatus.set(1);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataLaundryCancelHandle() {
        //refresh data from table & close form data warehouse
        refreshDataTableLaundry();
        dataLaundryFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableLaundry() {
        tableDataLaundry.getTableView().setItems(loadAllDataLaundry());
        cft.refreshFilterItems(tableDataLaundry.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataLaundry() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtLaundryName.getText() == null || txtLaundryName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Laundry : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpFloor.getValue() == null) {
            dataInput = false;
            errDataInput += "Gedung : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBuilding.getValue() == null) {
            dataInput = false;
            errDataInput += "Lantai : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if(chbJM.isSelected()
                && cbpGroup.getValue() == null){
            dataInput = false;
            errDataInput += "Department : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setDataLaundrySplitPane();

        initTableDataLaundry();

        initFormDataLaundry();

    }

    public LaundryController(FeatureLocationController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLocationController parentController;
}
