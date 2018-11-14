/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_asset.asset;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFixedTangibleAssetDepreciationStatus;
import hotelfx.persistence.model.RefFixedTangibleAssetType;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_asset.FeatureAssetController;
import java.math.BigDecimal;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
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
;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
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


public class AssetController implements Initializable {
    
    @FXML
    private SplitPane spDataAsset;
    private DoubleProperty dataAssetFormShowStatus;
    @FXML
    private AnchorPane tableDataAssetLayoutDisableLayer;
    @FXML
    private AnchorPane contentLayout;
    
    private boolean isTimeLinePlaying = false;
    
    private void setDataAssetSplitPane() {
        spDataAsset.setDividerPositions(1);
        
        dataAssetFormShowStatus = new SimpleDoubleProperty(1.0);
        
        DoubleProperty divPosition = new SimpleDoubleProperty();
        
        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataAssetFormShowStatus)
        );
        
        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataAsset.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });
        
        SplitPane.Divider div = spDataAsset.getDividers().get(0);
        
        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });
        
        dataAssetFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    tableDataAssetLayout.setDisable(false);
                    tableDataAssetLayoutDisableLayer.setDisable(true);
                    tableDataAssetLayout.toFront();
                }
                
                if (newVal.doubleValue() == 1.0) {
                    tableDataAssetLayout.setDisable(true);
                    tableDataAssetLayoutDisableLayer.setDisable(false);
                    tableDataAssetLayoutDisableLayer.toFront();
                }
            }
        });
        dataAssetFormShowStatus.set(0.0);
        
        System.out.println("" + spDataAsset.getDividers().get(0).getPosition());
        
    }
    
    @FXML
    private AnchorPane tableDataAssetLayout;
    private ClassTableWithControl tableDataAsset;
    
    private void initTableDataAsset() {
        setTableDataAsset();
        setTableControlDataAsset();
        tableDataAssetLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataAsset, 15.0);
        AnchorPane.setLeftAnchor(tableDataAsset, 15.0);
        AnchorPane.setRightAnchor(tableDataAsset, 15.0);
        AnchorPane.setBottomAnchor(tableDataAsset, 15.0);
        tableDataAssetLayout.getChildren().add(tableDataAsset);
    }
    
    private void setTableDataAsset() {
        TableView<TblFixedTangibleAsset> tableView = new TableView();
        TableColumn<TblFixedTangibleAsset, String> codeAsset = new TableColumn("Code Asset");
        codeAsset.setCellValueFactory(cellData -> cellData.getValue().codeAssetProperty());
        TableColumn<TblFixedTangibleAsset, String> assetName = new TableColumn("Asset Name");
        assetName.setCellValueFactory(cellData -> cellData.getValue().assetNameProperty());
        
        TableColumn<TblFixedTangibleAsset, String> beginDate = new TableColumn("Begin Date");
        beginDate.setCellValueFactory((TableColumn.CellDataFeatures<TblFixedTangibleAsset, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getBeginDate() != null 
                        ? ClassFormatter.dateFormate.format(param.getValue().getBeginDate()) : "-", 
                        param.getValue().beginDateProperty()));
        
        TableColumn<TblFixedTangibleAsset, String> beginValue = new TableColumn("Begin Value");
        beginValue.setCellValueFactory((TableColumn.CellDataFeatures<TblFixedTangibleAsset, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBeginValue()),
                        param.getValue().beginValueProperty()));
        TableColumn<TblFixedTangibleAsset, String> currentValue = new TableColumn("Current Value");
        currentValue.setCellValueFactory((TableColumn.CellDataFeatures<TblFixedTangibleAsset, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getCurrentValue()),
                        param.getValue().currentValueProperty()));
        
        TableColumn<TblFixedTangibleAsset, String> economicLife = new TableColumn("Economic Life");
        economicLife.setCellValueFactory((TableColumn.CellDataFeatures<TblFixedTangibleAsset, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getEconomicLife()), param.getValue().economicLifeProperty()));
        
        TableColumn<TblFixedTangibleAsset, String> assetType = new TableColumn("Asset Type");
        assetType.setCellValueFactory((TableColumn.CellDataFeatures<TblFixedTangibleAsset, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFixedTangibleAssetType() != null
                        ? param.getValue().getRefFixedTangibleAssetType().getTypeName() : "-", 
                        param.getValue().refFixedTangibleAssetTypeProperty()));
        
        TableColumn<TblFixedTangibleAsset, String> assetDepreciationStatus = new TableColumn("Depreciation Status");
        assetDepreciationStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblFixedTangibleAsset, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFixedTangibleAssetDepreciationStatus() != null
                        ? param.getValue().getRefFixedTangibleAssetDepreciationStatus().getStatusName() : "-", 
                        param.getValue().refFixedTangibleAssetDepreciationStatusProperty()));
        
        TableColumn<TblFixedTangibleAsset, String> assetNote = new TableColumn("Asset Note");
        assetNote.setCellValueFactory(cellData -> cellData.getValue().assetNoteProperty());
        
        tableView.getColumns().addAll(codeAsset, assetName, beginDate, beginValue, currentValue, economicLife, assetType, assetDepreciationStatus);
        tableView.setItems(loadAllDataAsset());
        
        tableView.setRowFactory(tv -> {
            TableRow<TblFixedTangibleAsset> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataAssetUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataAssetUpdateHandle();
                            } else {
                                dataAssetShowHandle();
                            }
                        }
                    }
                } else {
                    if (!row.isEmpty()) {
                        if (isShowStatus.get()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataAssetUpdateHandle();
                            } else {
                                dataAssetShowHandle();
                            }
                        }
                    }
                }
            });
            return row;
        });
        
        tableDataAsset = new ClassTableWithControl(tableView);
    }
    
    private void setTableControlDataAsset() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataAssetCreateHandle();
            });
            
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataAssetUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "Are you sure want to delete this data?", null);
                if (alert.getResult() == ButtonType.OK) {
                    dataAssetDeleteHandle();
                }
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
        tableDataAsset.addButtonControl(buttonControls);
    }
    
    private ObservableList<TblFixedTangibleAsset> loadAllDataAsset() {
        return FXCollections.observableArrayList(parentController.getFAssetManager().getAllDataAsset());
    }

    /*Form Input
     */
    @FXML
    private AnchorPane formAnchor;
    @FXML
    private GridPane gpFormDataAsset;
    @FXML
    private ScrollPane spFormDataAsset;
    
    @FXML
    private JFXTextField txtCodeAsset;
    @FXML
    private JFXTextField txtAssetName;
    @FXML
    private JFXTextField txtAssetBeginValue;
    @FXML
    private JFXTextField txtAssetCurrentValue;
    @FXML
    private JFXTextField txtEconomicLife;
    @FXML
    private DatePicker dptDateBeginer;
    @FXML
    private JFXTextArea txtAssetNote;
    private JFXCComboBoxTablePopup<RefFixedTangibleAssetType> cbpAssetType;
    private JFXCComboBoxTablePopup<RefFixedTangibleAssetDepreciationStatus> cbpAssetDepreciationStatus;
    
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    private TblFixedTangibleAsset selectedData;
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    
    private int scrollCounter = 0;
    
    public void initFormDataAsset() {
        isFormScroll.addListener((obs, wasScroll, nowScroll) -> {
            spFormDataAsset.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });
        
        gpFormDataAsset.setOnScroll((ScrollEvent scroll) -> {
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
                    System.out.println("err" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });
        
        initDataPopup();
        
        saveButton.setTooltip(new Tooltip("Save (Data Asset)"));
        saveButton.setOnAction((e) -> {
            dataAssetSaveHandle();
        });
        
        cancelButton.setTooltip(new Tooltip("Cancel"));
        cancelButton.setOnAction((e) -> {
            dataAssetCancelHandle();
        });
        
        initDateCalendar();
        
        initImportantFieldColor();
        
        initNumbericField();
    }
    
    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dptDateBeginer);
//        ClassFormatter.setDatePickersEnableDate(LocalDate.now(),
//                dptDateBeginer);
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtAssetName,
                dptDateBeginer,
                cbpAssetType,
                cbpAssetDepreciationStatus,
                txtAssetBeginValue,
                txtAssetCurrentValue,
                txtEconomicLife);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAssetBeginValue,
                txtAssetCurrentValue,
                txtEconomicLife);
    }
    
    private void initDataPopup() {
        
        TableView<RefFixedTangibleAssetDepreciationStatus> tableAssetDepreciationStatus = new TableView();
        TableColumn<RefFixedTangibleAssetDepreciationStatus, String> assetDepreciationStatusName = new TableColumn<>("Asset Depreciation Status");
        assetDepreciationStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        tableAssetDepreciationStatus.getColumns().addAll(assetDepreciationStatusName);
        
        TableView<RefFixedTangibleAssetType> tableAssetType = new TableView();
        TableColumn<RefFixedTangibleAssetType, String> assetTypeName = new TableColumn<>("Asset Type");
        assetTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        tableAssetType.getColumns().addAll(assetTypeName);
        
        ObservableList<RefFixedTangibleAssetDepreciationStatus> assetDepreciationStatusItem = FXCollections.observableArrayList(parentController.getFAssetManager().getAllAssetDepreciationStatus());
        cbpAssetDepreciationStatus = new JFXCComboBoxTablePopup<>(
                RefFixedTangibleAssetDepreciationStatus.class, tableAssetDepreciationStatus, assetDepreciationStatusItem, "", "Asset Depreciation Status *", true, 200, 200
        );
        
        ObservableList<RefFixedTangibleAssetType> assetTypeItem = FXCollections.observableArrayList(parentController.getFAssetManager().getAllAssetType());
        cbpAssetType = new JFXCComboBoxTablePopup<>(
                RefFixedTangibleAssetType.class, tableAssetType, assetTypeItem, "", "Asset Type *", true, 200, 200
        );
        
        gpFormDataAsset.add(cbpAssetType, 2, 4);
        gpFormDataAsset.add(cbpAssetDepreciationStatus, 1, 4);
    }
    
    private void refreshDataPopup() {
        ObservableList<RefFixedTangibleAssetType> assetTypeItems = FXCollections.observableArrayList(parentController.getFAssetManager().getAllAssetType());
        cbpAssetType.setItems(assetTypeItems);
        
        ObservableList<RefFixedTangibleAssetDepreciationStatus> assetDepreciationStatusItems = FXCollections.observableArrayList(parentController.getFAssetManager().getAllAssetDepreciationStatus());
        cbpAssetDepreciationStatus.setItems(assetDepreciationStatusItems);
        
    }
    
    private void setSelectedDataToInputForm() {
        refreshDataPopup();
        
        txtCodeAsset.textProperty().bindBidirectional(selectedData.codeAssetProperty());
        txtAssetName.textProperty().bindBidirectional(selectedData.assetNameProperty());
        if (selectedData.getBeginDate() != null) {
            dptDateBeginer.setValue(selectedData.getBeginDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dptDateBeginer.setValue(null);
        }
        dptDateBeginer.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setBeginDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
        
        Bindings.bindBidirectional(txtAssetBeginValue.textProperty(), selectedData.beginValueProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtAssetCurrentValue.textProperty(), selectedData.currentValueProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtEconomicLife.textProperty(), selectedData.economicLifeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        
        cbpAssetType.valueProperty().bindBidirectional(selectedData.refFixedTangibleAssetTypeProperty());
        cbpAssetDepreciationStatus.valueProperty().bindBidirectional(selectedData.refFixedTangibleAssetDepreciationStatusProperty());
        
        cbpAssetType.hide();
        cbpAssetDepreciationStatus.hide();
        
        txtAssetNote.textProperty().bindBidirectional(selectedData.assetNoteProperty());
        
        setSelectedDataToInputFormFunctionalComponent();
    }
    
    private void setSelectedDataToInputFormFunctionalComponent() {
        gpFormDataAsset.setDisable(dataInputStatus == 3);
        
        saveButton.setVisible(dataInputStatus != 3);
        cancelButton.setVisible(dataInputStatus != 3);
    }
    
    private int dataInputStatus = 0;
    
    private void dataAssetCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblFixedTangibleAsset();
        selectedData.setBeginValue(new BigDecimal("0"));
        selectedData.setCurrentValue(new BigDecimal("0"));
        selectedData.setEconomicLife(new BigDecimal("0"));
        setSelectedDataToInputForm();
        dataAssetFormShowStatus.set(0.0);
        dataAssetFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }
    
    private void dataAssetUpdateHandle() {
        if (tableDataAsset.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFAssetManager().getAsset(((TblFixedTangibleAsset) tableDataAsset.getTableView().getSelectionModel().getSelectedItem()).getIdasset());
            setSelectedDataToInputForm();
            dataAssetFormShowStatus.set(0.0);
            dataAssetFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item!!", null);
        }
    }
    
    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);
    
    private void dataAssetShowHandle() {
        if (tableDataAsset.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFAssetManager().getAsset(((TblFixedTangibleAsset) tableDataAsset.getTableView().getSelectionModel().getSelectedItem()).getIdasset());
            setSelectedDataToInputForm();
            dataAssetFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }
    
    private void dataAssetUnshowHandle() {
        tableDataAsset.getTableView().setItems(loadAllDataAsset());
        dataAssetFormShowStatus.set(0);
        isShowStatus.set(false);
    }
    
    private void dataAssetDeleteHandle() {
        if (tableDataAsset.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (parentController.getFAssetManager().deleteDataAsset(new TblFixedTangibleAsset((TblFixedTangibleAsset) tableDataAsset.getTableView().getSelectionModel().getSelectedItem()))) {
                HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Deleting data successed..!", null);
                tableDataAsset.getTableView().setItems(loadAllDataAsset());
                dataAssetFormShowStatus.set(0.0);
            } else {
                HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Deleting data failed!!", parentController.getFAssetManager().getErrorMessage());
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item!!", null);
        }
    }
    
    private void dataAssetSaveHandle() {
        if (checkDataInputAsset()) {
            //dummy entry
            TblFixedTangibleAsset dummySelectedData = new TblFixedTangibleAsset(selectedData);
            switch (dataInputStatus) {
                case 0:
                    if (parentController.getFAssetManager().insertDataAsset(dummySelectedData) != null) {
                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Inserting data successed..!", null);
                        tableDataAsset.getTableView().setItems(loadAllDataAsset());
                        dataAssetFormShowStatus.set(0.0);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                    } else {
                        HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Inserting data failed!!!", parentController.getFAssetManager().getErrorMessage());
                    }
                    break;
                
                case 1:
                    if (parentController.getFAssetManager().updateDataAsset(dummySelectedData)) {
                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Updating data successed..!", null);
                        tableDataAsset.getTableView().setItems(loadAllDataAsset());
                        dataAssetFormShowStatus.set(0.0);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                    } else {
                        HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Updating data failed!!!", parentController.getFAssetManager().getErrorMessage());
                    }
                    break;
                default:
                    break;
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please check data input!!!", errDataInput);
        }
    }
    
    private void dataAssetCancelHandle() {
        tableDataAsset.getTableView().setItems(loadAllDataAsset());
        dataAssetFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }
    
    private String errDataInput;
    
    private boolean checkDataInputAsset() {
        boolean check = true;
        errDataInput = "";
        if (txtAssetName.getText() == null || txtAssetName.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Asset Name : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dptDateBeginer.getValue() == null) {
            check = false;
            errDataInput += "Begin Date : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpAssetType.getValue() == null) {
            check = false;
            errDataInput += "Asset Type : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpAssetDepreciationStatus.getValue() == null) {
            check = false;
            errDataInput += "Asset Depreciation Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtAssetBeginValue.getText() == null
                || txtAssetBeginValue.getText().equals("")
                || txtAssetBeginValue.getText().equals("-")) {
            check = false;
            errDataInput += "Begin Value : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtAssetCurrentValue.getText() == null
                || txtAssetCurrentValue.getText().equals("")
                || txtAssetCurrentValue.getText().equals("-")) {
            check = false;
            errDataInput += "Current Value : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtEconomicLife.getText() == null
                || txtEconomicLife.getText().equals("")
                || txtEconomicLife.getText().equals("-")) {
            check = false;
            errDataInput += "Economic Life : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataAssetSplitPane();
        initTableDataAsset();
        initFormDataAsset();
        spDataAsset.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataAssetFormShowStatus.set(0.0);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public AssetController(FeatureAssetController parentController) {
        this.parentController = parentController;
    }
    
    private final FeatureAssetController parentController;
}
