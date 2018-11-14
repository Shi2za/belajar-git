/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_item_and_asset_md.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_item_and_asset_md.FeatureItemAndAssetMDController;
import java.math.BigDecimal;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ItemController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataItem;

    private DoubleProperty dataItemFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataItemLayoutDisableLayer;

    private final PseudoClass itemPseudoClass = PseudoClass.getPseudoClass("item");

    private final PseudoClass refreshPseudoClass = PseudoClass.getPseudoClass("refresh");

    private boolean isTimeLinePlaying = false;

    private void setDataItemSplitpane() {
        spDataItem.setDividerPositions(1);

        dataItemFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataItemFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataItem.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataItem.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataItemFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataItemLayout.setDisable(false);
                    tableDataItemLayoutDisableLayer.setDisable(true);
                    tableDataItemLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataItemLayout.setDisable(true);
                    tableDataItemLayoutDisableLayer.setDisable(false);
                    tableDataItemLayoutDisableLayer.toFront();
                }
            }
        });

        dataItemFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataItemLayout;

    private ClassFilteringTable<TblItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataItem;

    private void initTableDataItem() {
        //set table
        setTableDataItem();
        //set control
        setTableControlDataItem();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataItem, 15.0);
        AnchorPane.setLeftAnchor(tableDataItem, 15.0);
        AnchorPane.setRightAnchor(tableDataItem, 15.0);
        AnchorPane.setTopAnchor(tableDataItem, 15.0);
        ancBodyLayout.getChildren().add(tableDataItem);
    }

    private void setTableDataItem() {
        TableView<TblItem> tableView = new TableView();

        TableColumn<TblItem, String> idItem = new TableColumn("ID");
        idItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        idItem.setMinWidth(100);

        TableColumn<TblItem, String> itemNameAndBrand = new TableColumn("Nama/Merek");
        itemNameAndBrand.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getItemName() + "\nMerek : "
                        + (param.getValue().getItemBrand() != null ? param.getValue().getItemBrand() : "-"),
                        param.getValue().iditemProperty()));
        itemNameAndBrand.setMinWidth(140);

        TableColumn<TblItem, String> itemType = new TableColumn("Tipe Barang");
        itemType.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(()
                        -> "HK : "
                        + (param.getValue().getTblItemTypeHk() != null
                                ? param.getValue().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + "\nG   : "
                        + (param.getValue().getTblItemTypeWh() != null
                                ? param.getValue().getTblItemTypeWh().getItemTypeWhname() : "-"),
                        param.getValue().iditemProperty()));
        itemType.setMinWidth(145);

        TableColumn<TblItem, Boolean> consumableStatus = new TableColumn("Expr.");
        consumableStatus.setCellValueFactory(cellData -> cellData.getValue().consumableProperty());
        consumableStatus.setCellFactory(CheckBoxTableCell.forTableColumn(consumableStatus));
        consumableStatus.setMinWidth(70);

        TableColumn<TblItem, Boolean> propertyStatus = new TableColumn("Properti");
        propertyStatus.setCellValueFactory(cellData -> cellData.getValue().propertyStatusProperty());
        propertyStatus.setCellFactory(CheckBoxTableCell.forTableColumn(propertyStatus));
        propertyStatus.setMinWidth(70);

        TableColumn<TblItem, Boolean> leasedStatus = new TableColumn("Disewakan");
        leasedStatus.setCellValueFactory(cellData -> cellData.getValue().leasedStatusProperty());
        leasedStatus.setCellFactory(CheckBoxTableCell.forTableColumn(leasedStatus));
        leasedStatus.setMinWidth(70);

        TableColumn<TblItem, Boolean> guestStatus = new TableColumn("Guest");
        guestStatus.setCellValueFactory(cellData -> cellData.getValue().guestStatusProperty());
        guestStatus.setCellFactory(CheckBoxTableCell.forTableColumn(guestStatus));
        guestStatus.setMinWidth(70);

        TableColumn<TblItem, String> statusItem = new TableColumn("Status Barang");
        statusItem.getColumns().addAll(consumableStatus, propertyStatus, leasedStatus, guestStatus);
        statusItem.setMinWidth(310);

        TableColumn<TblItem, String> costOfGoodsSold = new TableColumn("Beli");
        costOfGoodsSold.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCostOfGoodsSold()),
                        param.getValue().itemCostOfGoodsSoldProperty()));
        costOfGoodsSold.setMinWidth(70);

        TableColumn<TblItem, String> additionalCharge = new TableColumn("Jual");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getAdditionalCharge()),
                        param.getValue().additionalChargeProperty()));
        additionalCharge.setMinWidth(70);

        TableColumn<TblItem, String> brokenCharge = new TableColumn("Kerusakan");
        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBrokenCharge()),
                        param.getValue().brokenChargeProperty()));
        brokenCharge.setMinWidth(70);

        TableColumn<TblItem, String> costItem = new TableColumn("Harga Barang");
        costItem.getColumns().addAll(costOfGoodsSold, additionalCharge, brokenCharge);

        TableColumn<TblItem, String> minStock = new TableColumn("Minimal");
        minStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getStockMinimal()), param.getValue().stockMinimalProperty()));
        minStock.setMinWidth(70);

        TableColumn<TblItem, String> totalCurrentStock = new TableColumn("Sekarang");
        totalCurrentStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getTotalCurrentStock(param.getValue())), param.getValue().iditemProperty()));
        minStock.setMinWidth(70);

        TableColumn<TblItem, String> stockItem = new TableColumn("Jumlah Stok");
        stockItem.getColumns().addAll(minStock, totalCurrentStock);

        TableColumn<TblItem, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblUnit().getUnitName(), param.getValue().tblUnitProperty()));
        unitName.setMinWidth(70);

        TableColumn<TblItem, String> itemNote = new TableColumn("Keterangan");
        itemNote.setCellValueFactory(cellData -> cellData.getValue().itemNoteProperty());
        itemNote.setMinWidth(180);

        tableView.getColumns().addAll(idItem, itemNameAndBrand,
                itemType,
                statusItem,
                costItem,
                stockItem,
                unitName);

        tableView.setItems(loadAllDataItem());

        tableView.setRowFactory(tv -> {
            TableRow<TblItem> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(itemPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == -1);
                } else {
                    row.pseudoClassStateChanged(itemPseudoClass, false);
                }
            });

            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataItemUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataItemUpdateHandle();
                            } else {
                                dataItemShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataItemUpdateHandle();
//                            } else {
//                                dataItemShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataItem = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItem.class,
                tableDataItem.getTableView(),
                tableDataItem.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private BigDecimal getTotalCurrentStock(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null) {
            List<TblItemLocation> itemLocations = parentController.getFItemAndAssetManager().getAllDataItemLocationByIDItem(dataItem.getIditem());
            for (TblItemLocation itemLocation : itemLocations) {
                if (itemLocation.getTblItem().getConsumable()) {  //consumabel
                    if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4 //Bin = '4'
                            && itemLocation.getTblLocation().getRefLocationType().getIdtype() != 5 //StockOpname = '5'
                            && itemLocation.getTblLocation().getRefLocationType().getIdtype() != 6) { //Digunakan = '6'
                        result = result.add(itemLocation.getItemQuantity());
                    }
                } else {  //!consumable
                    if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4 //Bin = '4'
                            && itemLocation.getTblLocation().getRefLocationType().getIdtype() != 5) { //StockOpname = '5'
                        result = result.add(itemLocation.getItemQuantity());
                    }
                }
            }
        }
        return result;
    }

    private void setTableControlDataItem() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataItemCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataItemUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataItemDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataItemPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataItem.addButtonControl(buttonControls);
    }

    private ObservableList<TblItem> loadAllDataItem() {
        List<TblItem> list = parentController.getFItemAndAssetManager().getAllDataItem();

        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataItem;

    @FXML
    private ScrollPane spFormDataItem;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXCheckBox chbConsumableStatus;

    @FXML
    private JFXCheckBox chbGuestStatus;

    @FXML
    private JFXCheckBox chbPropertyStatus;

    @FXML
    private JFXCheckBox chbLeasedStatus;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtItemBrand;

    @FXML
    private JFXTextField txtItemCostOfGoodsSold;

    @FXML
    private JFXTextField txtItemAdditionalCharge;

    @FXML
    private JFXTextField txtItemBrokenCharge;

    @FXML
    private JFXTextField txtItemMinStock;

    @FXML
    private JFXTextArea txtItemNote;

    @FXML
    private AnchorPane ancItemTypeHKLayout;
    private JFXCComboBoxTablePopup<TblItemTypeHk> cbpItemTypeHK;

    @FXML
    private AnchorPane ancItemTypeWHLayout;
    private JFXCComboBoxTablePopup<TblItemTypeWh> cbpItemTypeWH;

    @FXML
    private AnchorPane ancUnitLayout;
    private JFXCComboBoxTablePopup<TblUnit> cbpUnit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblItem selectedData;

    private void initFormDataItem() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataItem.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataItem.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataItemSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataItemCancelHandle();
        });

        chbConsumableStatus.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                chbPropertyStatus.setSelected(false);
                chbPropertyStatus.setDisable(true);
            } else {
                chbPropertyStatus.setDisable(false);
            }
        });

        chbPropertyStatus.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                chbConsumableStatus.setSelected(false);
                chbConsumableStatus.setDisable(true);
//                chbLeasedStatus.setVisible(true);
            } else {
                chbConsumableStatus.setDisable(false);
//                chbLeasedStatus.setSelected(false);
//                chbLeasedStatus.setVisible(false);
            }
        });

        initImportantFieldColor();

        initNumbericField();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtItemName,
                cbpItemTypeHK,
                cbpItemTypeWH,
                cbpUnit,
                txtItemCostOfGoodsSold,
                txtItemAdditionalCharge,
                txtItemBrokenCharge,
                txtItemMinStock);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemCostOfGoodsSold,
                txtItemAdditionalCharge,
                txtItemBrokenCharge,
                txtItemMinStock);
    }

    private void initDataPopup() {
        //Item Type HK
        TableView<TblItemTypeHk> tableItemTypeHK = new TableView<>();

        TableColumn<TblItemTypeHk, String> itemTypeHK = new TableColumn<>("Tipe Barang");
        itemTypeHK.setCellValueFactory(cellData -> cellData.getValue().itemTypeHknameProperty());
        itemTypeHK.setMinWidth(140);

        tableItemTypeHK.getColumns().addAll(itemTypeHK);

        ObservableList<TblItemTypeHk> itemTypeHKItems = FXCollections.observableArrayList(parentController.getFItemAndAssetManager().getAllDataItemTypeHK());

        cbpItemTypeHK = new JFXCComboBoxTablePopup<>(
                TblItemTypeHk.class, tableItemTypeHK, itemTypeHKItems, "", "Tipe Barang (HK) *", true, 160, 200
        );

        //Item Type WH
        TableView<TblItemTypeWh> tableItemTypeWH = new TableView<>();

        TableColumn<TblItemTypeWh, String> itemTypeWH = new TableColumn<>("Tipe Barang");
        itemTypeWH.setCellValueFactory(cellData -> cellData.getValue().itemTypeWhnameProperty());
        itemTypeWH.setMinWidth(140);

        tableItemTypeWH.getColumns().addAll(itemTypeWH);

        ObservableList<TblItemTypeWh> itemTypeWHItems = FXCollections.observableArrayList(parentController.getFItemAndAssetManager().getAllDataItemTypeWH());

        cbpItemTypeWH = new JFXCComboBoxTablePopup<>(
                TblItemTypeWh.class, tableItemTypeWH, itemTypeWHItems, "", "Tipe Barang (G) *", true, 160, 200
        );

        //Unit
        TableView<TblUnit> tableUnit = new TableView<>();

        TableColumn<TblUnit, String> unitName = new TableColumn<>("Satuan");
        unitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        unitName.setMinWidth(140);

        tableUnit.getColumns().addAll(unitName);

        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFItemAndAssetManager().getAllDataUnit());

        cbpUnit = new JFXCComboBoxTablePopup<>(
                TblUnit.class, tableUnit, unitItems, "", "Satuan *", true, 160, 200
        );

        //attached to grid-pane
        ancItemTypeHKLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpItemTypeHK, 0.0);
        AnchorPane.setLeftAnchor(cbpItemTypeHK, 0.0);
        AnchorPane.setRightAnchor(cbpItemTypeHK, 0.0);
        AnchorPane.setTopAnchor(cbpItemTypeHK, 0.0);
        ancItemTypeHKLayout.getChildren().add(cbpItemTypeHK);
        ancItemTypeWHLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpItemTypeWH, 0.0);
        AnchorPane.setLeftAnchor(cbpItemTypeWH, 0.0);
        AnchorPane.setRightAnchor(cbpItemTypeWH, 0.0);
        AnchorPane.setTopAnchor(cbpItemTypeWH, 0.0);
        ancItemTypeWHLayout.getChildren().add(cbpItemTypeWH);
        ancUnitLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpUnit, 0.0);
        AnchorPane.setLeftAnchor(cbpUnit, 0.0);
        AnchorPane.setRightAnchor(cbpUnit, 0.0);
        AnchorPane.setTopAnchor(cbpUnit, 0.0);
        ancUnitLayout.getChildren().add(cbpUnit);
    }

    private void refreshDataPopup() {
        //Item Type HK
        ObservableList<TblItemTypeHk> itemTypeHKItems = FXCollections.observableArrayList(parentController.getFItemAndAssetManager().getAllDataItemTypeHK());
        cbpItemTypeHK.setItems(itemTypeHKItems);

        //Item Type WH
        ObservableList<TblItemTypeWh> itemTypeWHItems = FXCollections.observableArrayList(parentController.getFItemAndAssetManager().getAllDataItemTypeWH());
        cbpItemTypeWH.setItems(itemTypeWHItems);

        //Unit
        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFItemAndAssetManager().getAllDataUnit());
        cbpUnit.setItems(unitItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeItem() != null
                ? selectedData.getCodeItem() : "");

        chbConsumableStatus.selectedProperty().bindBidirectional(selectedData.consumableProperty());

        chbPropertyStatus.selectedProperty().bindBidirectional(selectedData.propertyStatusProperty());
        chbLeasedStatus.selectedProperty().bindBidirectional(selectedData.leasedStatusProperty());

        chbLeasedStatus.selectedProperty().bindBidirectional(selectedData.leasedStatusProperty());

        txtItemName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        txtItemBrand.textProperty().bindBidirectional(selectedData.itemBrandProperty());
        Bindings.bindBidirectional(txtItemCostOfGoodsSold.textProperty(), selectedData.itemCostOfGoodsSoldProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemAdditionalCharge.textProperty(), selectedData.additionalChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemBrokenCharge.textProperty(), selectedData.brokenChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemMinStock.textProperty(), selectedData.stockMinimalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtItemNote.textProperty().bindBidirectional(selectedData.itemNoteProperty());

        cbpItemTypeHK.valueProperty().bindBidirectional(selectedData.tblItemTypeHkProperty());
        cbpItemTypeWH.valueProperty().bindBidirectional(selectedData.tblItemTypeWhProperty());
        cbpUnit.valueProperty().bindBidirectional(selectedData.tblUnitProperty());

        cbpItemTypeHK.hide();
        cbpItemTypeWH.hide();
        cbpUnit.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataItem,
                dataInputStatus == 3,
                chbConsumableStatus,
                chbPropertyStatus);

        chbConsumableStatus.setDisable(dataInputStatus != 0);   //editing, just for 'create data'
        chbPropertyStatus.setDisable(dataInputStatus != 0); //editing, just for 'create data'

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataItemCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblItem();
        selectedData.setItemCostOfGoodsSold(new BigDecimal("0"));
        selectedData.setAdditionalCharge(new BigDecimal("0"));
        selectedData.setBrokenCharge(new BigDecimal("0"));
        selectedData.setStockMinimal(new BigDecimal("0"));
        selectedData.setConsumable(false);
        selectedData.setGuestStatus(false);
        selectedData.setPropertyStatus(false);
        selectedData.setLeasedStatus(false);
        setSelectedDataToInputForm();
        //open form data item
        dataItemFormShowStatus.set(0.0);
        dataItemFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataItemUpdateHandle() {
        if (tableDataItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFItemAndAssetManager().getDataItem(((TblItem) tableDataItem.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            setSelectedDataToInputForm();
            //open form data item
            dataItemFormShowStatus.set(0.0);
            dataItemFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataItemShowHandle() {
        if (tableDataItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFItemAndAssetManager().getDataItem(((TblItem) tableDataItem.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            setSelectedDataToInputForm();
            dataItemFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataItemUnshowHandle() {
        refreshDataTableItem();
        dataItemFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataItemDeleteHandle() {
        if (tableDataItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFItemAndAssetManager().deleteDataItem((TblItem) tableDataItem.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedInsertingDataMessage("", null);
                    //refresh data from table & close form data item
                    refreshDataTableItem();
                    dataItemFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage("", null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataItemPrintHandle() {

    }

    private void dataItemSaveHandle() {
        if (checkDataInputDataItem()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItem dummySelectedData = new TblItem(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFItemAndAssetManager().insertDataItem(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data item
                            refreshDataTableItem();
                            dataItemFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFItemAndAssetManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFItemAndAssetManager().updateDataItem(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data item
                            refreshDataTableItem();
                            dataItemFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage("", null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(errDataInput, null);
        }
    }

    private void dataItemCancelHandle() {
        //refresh data from table & close form data item
        refreshDataTableItem();
        dataItemFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableItem() {
        tableDataItem.getTableView().setItems(loadAllDataItem());
        cft.refreshFilterItems(tableDataItem.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataItem() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtItemName.getText() == null || txtItemName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpItemTypeHK.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Barang (House Keeping) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpItemTypeWH.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Barang (Gudang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpUnit.getValue() == null) {
            dataInput = false;
            errDataInput += "Satuan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtItemCostOfGoodsSold.getText() == null
                || txtItemCostOfGoodsSold.getText().equals("")
                || txtItemCostOfGoodsSold.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Beli (Terakhir) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getItemCostOfGoodsSold().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Beli (Terakhir) : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtItemAdditionalCharge.getText() == null
                || txtItemAdditionalCharge.getText().equals("")
                || txtItemAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Jual : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getAdditionalCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Jual : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtItemBrokenCharge.getText() == null
                || txtItemBrokenCharge.getText().equals("")
                || txtItemBrokenCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Kerusakan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getBrokenCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Kerusakan : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtItemMinStock.getText() == null
                || txtItemMinStock.getText().equals("")
                || txtItemMinStock.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Stok Minimal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getStockMinimal()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Stok Minimal : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
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
        setDataItemSplitpane();

        //init table
        initTableDataItem();

        //init form
        initFormDataItem();

        spDataItem.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataItemFormShowStatus.set(0.0);
        });
    }

    public ItemController(FeatureItemAndAssetMDController parentController) {
        this.parentController = parentController;
    }

    private final FeatureItemAndAssetMDController parentController;

}
