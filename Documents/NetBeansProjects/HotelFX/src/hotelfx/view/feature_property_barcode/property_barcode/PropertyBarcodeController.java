/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_property_barcode.property_barcode;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFixedTangibleAssetDepreciationStatus;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_property_barcode.FeaturePropertyBarcodeController;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class PropertyBarcodeController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPropertyBarcode;

    private DoubleProperty dataPropertyBarcodeFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPropertyBarcodeLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPropertyBarcodeSplitpane() {
        spDataPropertyBarcode.setDividerPositions(1);

        dataPropertyBarcodeFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPropertyBarcodeFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPropertyBarcode.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPropertyBarcode.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPropertyBarcodeFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPropertyBarcodeLayout.setDisable(false);
                    tableDataPropertyBarcodeLayoutDisableLayer.setDisable(true);
                    tableDataPropertyBarcodeLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPropertyBarcodeLayout.setDisable(true);
                    tableDataPropertyBarcodeLayoutDisableLayer.setDisable(false);
                    tableDataPropertyBarcodeLayoutDisableLayer.toFront();
                }
            }
        });

        dataPropertyBarcodeFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPropertyBarcodeLayout;

    private ClassFilteringTable<PropertyBarcodeStatus> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataPropertyBarcode;

    private void initTableDataPropertyBarcode() {
        //set table
        setTableDataPropertyBarcode();
        //set control
        setTableControlDataPropertyBarcode();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPropertyBarcode, 15.0);
        AnchorPane.setLeftAnchor(tableDataPropertyBarcode, 15.0);
        AnchorPane.setRightAnchor(tableDataPropertyBarcode, 15.0);
        AnchorPane.setTopAnchor(tableDataPropertyBarcode, 15.0);
        ancBodyLayout.getChildren().add(tableDataPropertyBarcode);
    }

    private void setTableDataPropertyBarcode() {
        TableView<PropertyBarcodeStatus> tableView = new TableView();

        TableColumn<PropertyBarcodeStatus, String> codePropertyBarcode = new TableColumn("No. Barcode");
        codePropertyBarcode.setCellValueFactory((TableColumn.CellDataFeatures<PropertyBarcodeStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPropertyBarcode().getCodeBarcode(),
                        param.getValue().getDataPropertyBarcode().codeBarcodeProperty()));
        codePropertyBarcode.setMinWidth(120);

        TableColumn<PropertyBarcodeStatus, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<PropertyBarcodeStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPropertyBarcode().getTblItem().getCodeItem(),
                        param.getValue().getDataPropertyBarcode().tblItemProperty()));
        codeItem.setMinWidth(120);

        TableColumn<PropertyBarcodeStatus, String> itemName = new TableColumn("Nama");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<PropertyBarcodeStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPropertyBarcode().getTblItem().getItemName(),
                        param.getValue().getDataPropertyBarcode().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<PropertyBarcodeStatus, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(codeItem, itemName);

        TableColumn<PropertyBarcodeStatus, String> codeAsset = new TableColumn("ID");
        codeAsset.setCellValueFactory((TableColumn.CellDataFeatures<PropertyBarcodeStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPropertyBarcode().getTblItem().getCodeItem(),
                        param.getValue().getDataPropertyBarcode().tblFixedTangibleAssetProperty()));
        codeAsset.setMinWidth(120);

        TableColumn<PropertyBarcodeStatus, String> assetName = new TableColumn("Nama");
        assetName.setCellValueFactory((TableColumn.CellDataFeatures<PropertyBarcodeStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPropertyBarcode().getTblItem().getItemName(),
                        param.getValue().getDataPropertyBarcode().tblFixedTangibleAssetProperty()));
        assetName.setMinWidth(140);

        TableColumn<PropertyBarcodeStatus, String> assetTitle = new TableColumn("Aset");
        assetTitle.getColumns().addAll(codeAsset, assetName);

        TableColumn<PropertyBarcodeStatus, String> pbStatus = new TableColumn("Status");
        pbStatus.setCellValueFactory((TableColumn.CellDataFeatures<PropertyBarcodeStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataStatus(),
                        param.getValue().dataStatusProperty()));
        pbStatus.setMinWidth(140);

        TableColumn<PropertyBarcodeStatus, String> pbLocation = new TableColumn("Location");
        pbLocation.setCellValueFactory((TableColumn.CellDataFeatures<PropertyBarcodeStatus, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataLocation(),
                        param.getValue().dataLocationProperty()));
        pbLocation.setMinWidth(140);

        tableView.getColumns().addAll(codePropertyBarcode, itemTitle, assetTitle, pbStatus, pbLocation);
        tableView.setItems(loadAllDataPropertyBarcode());

        tableView.setRowFactory(tv -> {
            TableRow<PropertyBarcodeStatus> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPropertyBarcodeUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataPropertyBarcodeUpdateHandle();
                            } else {
                                dataPropertyBarcodeShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataPropertyBarcodeUpdateHandle();
//                            } else {
//                                dataPropertyBarcodeShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataPropertyBarcode = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblPropertyBarcode.class,
                tableDataPropertyBarcode.getTableView(),
                tableDataPropertyBarcode.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public class PropertyBarcodeStatus {

        private final ObjectProperty<TblPropertyBarcode> dataPropertyBarcode = new SimpleObjectProperty<>();

        private final StringProperty dataStatus = new SimpleStringProperty();

        private final StringProperty dataLocation = new SimpleStringProperty();

        public PropertyBarcodeStatus(TblPropertyBarcode dataPropertyBarcode) {
            //data property barcode
            this.dataPropertyBarcode.set(dataPropertyBarcode);
            //data status & location
            setPropertyBarcodeStatus(this.dataPropertyBarcode.get());
        }

        private void setPropertyBarcodeStatus(TblPropertyBarcode dataPB) {
            if (dataPB != null) {
                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = parentController.getFPropertyBarcodeManager().getDataItemLocationPropertyBarcodeByIDPropertyBarcode(
                        dataPB.getIdbarcode()
                );
                if (dataItemLocationPropertyBarcode != null) {
                    TblItemLocation dataItemLocation = parentController.getFPropertyBarcodeManager().getDataItemLocation(dataItemLocationPropertyBarcode.getTblItemLocation().getIdrelation());
                    if (dataItemLocation != null) {
                        TblLocation dataLocation = parentController.getFPropertyBarcodeManager().getDataLocation(dataItemLocation.getTblLocation().getIdlocation());
                        if (dataLocation != null
                                && dataLocation.getRefLocationType() != null) {
                            switch (dataLocation.getRefLocationType().getIdtype()) {
                                case 0:    //Kamar = '0'
                                    TblRoom room = parentController.getFPropertyBarcodeManager().getDataRoomByIDLocation(dataLocation.getIdlocation());
                                    setDataStatus("Ada");
                                    setDataLocation(room != null ? room.getRoomName() : "-");
                                    break;
                                case 1:    //Gudang = '1'
                                    TblLocationOfWarehouse warehouse = parentController.getFPropertyBarcodeManager().getDataWarehouseByIdLocation(dataLocation.getIdlocation());
                                    setDataStatus("Ada");
                                    setDataLocation(warehouse != null ? warehouse.getWarehouseName() : "-");
                                    break;
                                case 2:    //Laundry = '2'
                                    TblLocationOfLaundry laundry = parentController.getFPropertyBarcodeManager().getDataLaundryByIDLocation(dataLocation.getIdlocation());
                                    setDataStatus("Ada");
                                    setDataLocation(laundry != null ? laundry.getLaundryName() : "-");
                                    break;
                                case 3:    //Supplier = '3'
                                    setDataStatus("Retur");
                                    setDataLocation("-");
                                    break;
                                case 4:    //Bin = '4'
                                    setDataStatus("Rusak");
                                    setDataLocation("-");
                                    break;
                                case 5:    //StockOpname = '5'
                                    setDataStatus("Hilang");
                                    setDataLocation("-");
                                    break;
                                case 6:    //Digunakan = '6'
                                    setDataStatus("Digunakan");
                                    setDataLocation("-");
                                    break;
                                default:
                                    setDataStatus("Retur");
                                    setDataLocation("-");
                                    break;
                            }
                        }
                    }
                } else {
                    setDataStatus("Retur");
                    setDataLocation("-");
                }
            } else {
                setDataStatus("-");
                setDataLocation("-");
            }
        }

        public ObjectProperty<TblPropertyBarcode> dataPropertyBarcodeProperty() {
            return dataPropertyBarcode;
        }

        public TblPropertyBarcode getDataPropertyBarcode() {
            return dataPropertyBarcodeProperty().get();
        }

        public void setDataPropertyBarcode(TblPropertyBarcode dataPropertyBarcode) {
            dataPropertyBarcodeProperty().set(dataPropertyBarcode);
        }

        public StringProperty dataStatusProperty() {
            return dataStatus;
        }

        public String getDataStatus() {
            return dataStatusProperty().get();
        }

        public void setDataStatus(String dataStatus) {
            dataStatusProperty().set(dataStatus);
        }

        public StringProperty dataLocationProperty() {
            return dataLocation;
        }

        public String getDataLocation() {
            return dataLocationProperty().get();
        }

        public void setDataLocation(String dataLocation) {
            dataLocationProperty().set(dataLocation);
        }

    }

    private void setTableControlDataPropertyBarcode() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataPropertyBarcodeCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataPropertyBarcodeUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener delete
//                dataPropertyBarcodeDeleteHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataPropertyBarcodePrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPropertyBarcode.addButtonControl(buttonControls);
    }

    private ObservableList<PropertyBarcodeStatus> loadAllDataPropertyBarcode() {
        List<PropertyBarcodeStatus> list = new ArrayList<>();
        List<TblPropertyBarcode> dataPropertyBarcodes = parentController.getFPropertyBarcodeManager().getAllDataPropertyBarcode();
        for (TblPropertyBarcode dataPropertyBarcode : dataPropertyBarcodes) {
            //data item
            dataPropertyBarcode.setTblItem(parentController.getFPropertyBarcodeManager().getItem(dataPropertyBarcode.getTblItem().getIditem()));
            //data asset
            dataPropertyBarcode.setTblFixedTangibleAsset(parentController.getFPropertyBarcodeManager().getFixedTangibleAsset(dataPropertyBarcode.getTblFixedTangibleAsset().getIdasset()));
            //data property barcode - status
            list.add(new PropertyBarcodeStatus(dataPropertyBarcode));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataPropertyBarcode;

    @FXML
    private ScrollPane spFormDataPropertyBarcode;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodePropertyBarcode;

    @FXML
    private JFXTextField txtCodeAsset;

    @FXML
    private JFXTextField txtAssetName;

    @FXML
    private AnchorPane ancAssetDepreciationStatusLayout;
    private JFXCComboBoxTablePopup<RefFixedTangibleAssetDepreciationStatus> cbpAssetDepreciationStatus;

    @FXML
    private JFXTextField txtEconomicLife;

    @FXML
    private JFXDatePicker dtpBeginDate;

    @FXML
    private JFXDatePicker dtpCurrentDate;

    @FXML
    private JFXTextField txtBeginValue;

    @FXML
    private JFXTextField txtCurrentValue;

    @FXML
    private JFXTextArea txtAssetNote;

    @FXML
    private AnchorPane ancItemLayout;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblPropertyBarcode selectedData;

    private void initFormDataPropertyBarcode() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPropertyBarcode.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPropertyBarcode.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Save (Data Property Barcode)"));
        btnSave.setOnAction((e) -> {
            dataPropertyBarcodeSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Cancel"));
        btnCancel.setOnAction((e) -> {
            dataPropertyBarcodeCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpBeginDate,
                dtpCurrentDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpBeginDate,
                dtpCurrentDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpItem,
                dtpBeginDate,
                cbpAssetDepreciationStatus,
                txtBeginValue,
                txtCurrentValue,
                txtEconomicLife);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtBeginValue,
                txtCurrentValue,
                txtEconomicLife);
    }

    private void initDataPopup() {
        //Item
        TableView<TblItem> tableItem = new TableView<>();
        TableColumn<TblItem, String> idItem = new TableColumn<>("ID");
        idItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        TableColumn<TblItem, String> itemName = new TableColumn<>("Nama");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

        tableItem.getColumns().addAll(idItem, itemName);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 200, 200
        );

        //Fixed Tangle Asset Depreciaton Status
        TableView<RefFixedTangibleAssetDepreciationStatus> tableAssetDepreciationStatus = new TableView<>();
        TableColumn<RefFixedTangibleAssetDepreciationStatus, String> assetDepreciationStatusName = new TableColumn<>("Status");
        assetDepreciationStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());

        tableAssetDepreciationStatus.getColumns().addAll(assetDepreciationStatusName);

        ObservableList<RefFixedTangibleAssetDepreciationStatus> assetDepreciationStatusItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataFixedTangibleAssetDepreciationStatus());

        cbpAssetDepreciationStatus = new JFXCComboBoxTablePopup<>(
                RefFixedTangibleAssetDepreciationStatus.class, tableAssetDepreciationStatus, assetDepreciationStatusItems, "", "Status Penurunan *", true, 200, 200
        );

        //attached to grid-pane
        ancItemLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpItem, 0.0);
        AnchorPane.setLeftAnchor(cbpItem, 0.0);
        AnchorPane.setRightAnchor(cbpItem, 0.0);
        AnchorPane.setTopAnchor(cbpItem, 0.0);
        ancItemLayout.getChildren().add(cbpItem);
        ancAssetDepreciationStatusLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpAssetDepreciationStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpAssetDepreciationStatus, 0.0);
        AnchorPane.setRightAnchor(cbpAssetDepreciationStatus, 0.0);
        AnchorPane.setTopAnchor(cbpAssetDepreciationStatus, 0.0);
        ancAssetDepreciationStatusLayout.getChildren().add(cbpAssetDepreciationStatus);
    }

    private void refreshDataPopup() {
        //Item Type
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataItem());
        cbpItem.setItems(itemItems);

        //Asset Depreciation Status
        ObservableList<RefFixedTangibleAssetDepreciationStatus> assetDepreciationStatusItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataFixedTangibleAssetDepreciationStatus());
        cbpAssetDepreciationStatus.setItems(assetDepreciationStatusItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeBarcode() != null
                ? selectedData.getCodeBarcode() : "");

        txtCodePropertyBarcode.textProperty().bindBidirectional(selectedData.codeBarcodeProperty());

        //txtCodeAsset.textProperty().bindBidirectional(selectedData.getTblFixedTangibleAsset().codeAssetProperty());
        //txtAssetName.textProperty().bindBidirectional(selectedData.getTblFixedTangibleAsset().assetNameProperty());
        Bindings.bindBidirectional(txtEconomicLife.textProperty(), selectedData.getTblFixedTangibleAsset().economicLifeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtBeginValue.textProperty(), selectedData.getTblFixedTangibleAsset().beginValueProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtCurrentValue.textProperty(), selectedData.getTblFixedTangibleAsset().currentValueProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtAssetNote.textProperty().bindBidirectional(selectedData.getTblFixedTangibleAsset().assetNoteProperty());

        if (selectedData.getTblFixedTangibleAsset().getBeginDate() != null) {
            dtpBeginDate.setValue(selectedData.getTblFixedTangibleAsset().getBeginDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpBeginDate.setValue(null);
        }
        dtpBeginDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.getTblFixedTangibleAsset().setBeginDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        dtpCurrentDate.setValue(LocalDate.now());

        cbpAssetDepreciationStatus.valueProperty().bindBidirectional(selectedData.getTblFixedTangibleAsset().refFixedTangibleAssetDepreciationStatusProperty());

        cbpAssetDepreciationStatus.hide();

        cbpItem.valueProperty().bindBidirectional(selectedData.tblItemProperty());

        cbpItem.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodePropertyBarcode.setDisable(true);
        dtpCurrentDate.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataPropertyBarcode,
                dataInputStatus == 3,
                txtCodePropertyBarcode,
                dtpCurrentDate);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataPropertyBarcodeCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblPropertyBarcode();
        selectedData.setTblFixedTangibleAsset(new TblFixedTangibleAsset());
        selectedData.getTblFixedTangibleAsset().setBeginValue(new BigDecimal("0"));
        selectedData.getTblFixedTangibleAsset().setCurrentValue(new BigDecimal("0"));
        selectedData.getTblFixedTangibleAsset().setEconomicLife(new BigDecimal("0"));
        setSelectedDataToInputForm();
        //open form data property barcode
        dataPropertyBarcodeFormShowStatus.set(0.0);
        dataPropertyBarcodeFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataPropertyBarcodeUpdateHandle() {
        if (tableDataPropertyBarcode.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFPropertyBarcodeManager().getPropertyBarcode(((PropertyBarcodeStatus) tableDataPropertyBarcode.getTableView().getSelectionModel().getSelectedItem()).getDataPropertyBarcode().getIdbarcode());
            selectedData.setTblItem(parentController.getFPropertyBarcodeManager().getItem((selectedData.getTblItem().getIditem())));
            selectedData.setTblFixedTangibleAsset(parentController.getFPropertyBarcodeManager().getFixedTangibleAsset((selectedData.getTblFixedTangibleAsset().getIdasset())));
            setSelectedDataToInputForm();
            //open form data property barcode
            dataPropertyBarcodeFormShowStatus.set(0.0);
            dataPropertyBarcodeFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPropertyBarcodeShowHandle() {
        if (tableDataPropertyBarcode.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFPropertyBarcodeManager().getPropertyBarcode(((PropertyBarcodeStatus) tableDataPropertyBarcode.getTableView().getSelectionModel().getSelectedItem()).getDataPropertyBarcode().getIdbarcode());
            selectedData.setTblItem(parentController.getFPropertyBarcodeManager().getItem((selectedData.getTblItem().getIditem())));
            selectedData.setTblFixedTangibleAsset(parentController.getFPropertyBarcodeManager().getFixedTangibleAsset((selectedData.getTblFixedTangibleAsset().getIdasset())));
            setSelectedDataToInputForm();
            dataPropertyBarcodeFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPropertyBarcodeUnshowHandle() {
        refreshDataTablePropertyBarcode();
        dataPropertyBarcodeFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPropertyBarcodeDeleteHandle() {
        if (tableDataPropertyBarcode.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFPropertyBarcodeManager().deleteDataPropertyBarcode(((PropertyBarcodeStatus) tableDataPropertyBarcode.getTableView().getSelectionModel().getSelectedItem()).getDataPropertyBarcode())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data property barcode
                    refreshDataTablePropertyBarcode();
                    dataPropertyBarcodeFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFPropertyBarcodeManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataPropertyBarcodePrintHandle() {

    }

    private void dataPropertyBarcodeSaveHandle() {
        if (checkDataInputDataPropertyBarcode()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblPropertyBarcode dummySelectedData = new TblPropertyBarcode(selectedData);
                dummySelectedData.setTblItem(new TblItem(dummySelectedData.getTblItem()));
                dummySelectedData.setTblFixedTangibleAsset(new TblFixedTangibleAsset(dummySelectedData.getTblFixedTangibleAsset()));
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFPropertyBarcodeManager().insertDataPropertyBarcode(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data property barcode
                            refreshDataTablePropertyBarcode();
                            dataPropertyBarcodeFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFPropertyBarcodeManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFPropertyBarcodeManager().updateDataPropertyBarcode(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data property barcode
                            refreshDataTablePropertyBarcode();
                            dataPropertyBarcodeFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFPropertyBarcodeManager().getErrorMessage(), null);
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

    private void dataPropertyBarcodeCancelHandle() {
        //refresh data from table & close form data property barcode
        refreshDataTablePropertyBarcode();
        dataPropertyBarcodeFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTablePropertyBarcode() {
        tableDataPropertyBarcode.getTableView().setItems(loadAllDataPropertyBarcode());
        cft.refreshFilterItems(tableDataPropertyBarcode.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataPropertyBarcode() {
        boolean dataInput = true;
        errDataInput = "";
//        if (txtCodePropertyBarcode.getText() == null || txtCodePropertyBarcode.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Barcode : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Asset : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        /*if (txtAssetName.getText() == null || txtAssetName.getText().equalsIgnoreCase("")) {
         dataInput = false;
         errDataInput += "Asset Name : " + ClassMessage.defaultErrorNullValueMessage + " \n";
         }*/
        if (dtpBeginDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tgl. Beli Asset : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpAssetDepreciationStatus.getValue() == null) {
            dataInput = false;
            errDataInput += "Asset Depreciation Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtBeginValue.getText() == null
                || txtBeginValue.getText().equals("")
                || txtBeginValue.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nilai Awal Asset : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtCurrentValue.getText() == null
                || txtCurrentValue.getText().equals("")
                || txtCurrentValue.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nilai Asset Sekarang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtEconomicLife.getText() == null
                || txtEconomicLife.getText().equals("")
                || txtEconomicLife.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nilai Penurunan Asset : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {

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
        setDataPropertyBarcodeSplitpane();

        //init table
        initTableDataPropertyBarcode();

        //init form
        initFormDataPropertyBarcode();

        spDataPropertyBarcode.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPropertyBarcodeFormShowStatus.set(0.0);
        });
    }

    public PropertyBarcodeController(FeaturePropertyBarcodeController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePropertyBarcodeController parentController;

}
