/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_equipment.equipment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_equipment.FeatureEquipmentController;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class EquipmentController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataEquipment;

    private DoubleProperty dataEquipmentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataEquipmentLayoutDisableLayer;

    private final PseudoClass equipmentPseudoClass = PseudoClass.getPseudoClass("equipment");

    private boolean isTimeLinePlaying = false;

    private void setDataEquipmentSplitpane() {
        spDataEquipment.setDividerPositions(1);

        dataEquipmentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataEquipmentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataEquipment.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataEquipment.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataEquipmentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataEquipmentLayout.setDisable(false);
                    tableDataEquipmentLayoutDisableLayer.setDisable(true);
                    tableDataEquipmentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataEquipmentLayout.setDisable(true);
                    tableDataEquipmentLayoutDisableLayer.setDisable(false);
                    tableDataEquipmentLayoutDisableLayer.toFront();
                }
            }
        });

        dataEquipmentFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataEquipmentLayout;

    private ClassFilteringTable<TblItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataEquipment;

    private void initTableDataEquipment() {
        //set table
        setTableDataEquipment();
        //set control
        setTableControlDataEquipment();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataEquipment, 15.0);
        AnchorPane.setLeftAnchor(tableDataEquipment, 15.0);
        AnchorPane.setRightAnchor(tableDataEquipment, 15.0);
        AnchorPane.setTopAnchor(tableDataEquipment, 15.0);
        ancBodyLayout.getChildren().add(tableDataEquipment);
    }

    private void setTableDataEquipment() {
        TableView<TblItem> tableView = new TableView();

        TableColumn<TblItem, String> idEquipment = new TableColumn("ID");
        idEquipment.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        idEquipment.setMinWidth(100);

        TableColumn<TblItem, String> equipmentName = new TableColumn("Nama Perlengkapan");
        equipmentName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        equipmentName.setMinWidth(140);

        TableColumn<TblItem, String> equipmentBrand = new TableColumn("Merek");
        equipmentBrand.setCellValueFactory(cellData -> cellData.getValue().itemBrandProperty());
        equipmentBrand.setMinWidth(120);

        TableColumn<TblItem, String> tipeGuest = new TableColumn("Tamu");
        tipeGuest.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getGuestStatus() ? "Guest" : "NonGuest", 
                        param.getValue().guestStatusProperty()));
        tipeGuest.setMinWidth(100);

        TableColumn<TblItem, String> tipeConsumable = new TableColumn("Kadaluarsa");
        tipeConsumable.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getConsumable() == true ? "Kadaluarsa" : "Tidak Kadaluarsa", param.getValue().consumableProperty()));
        tipeConsumable.setMinWidth(70);

        TableColumn<TblItem, String> statusItem = new TableColumn("Status Barang");
        statusItem.getColumns().addAll(tipeGuest, tipeConsumable);

        TableColumn<TblItem, String> costOfGoodsSold = new TableColumn("Beli");
        costOfGoodsSold.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCostOfGoodsSold()), param.getValue().itemCostOfGoodsSoldProperty()));
        costOfGoodsSold.setMinWidth(70);

        TableColumn<TblItem, String> additionalCharge = new TableColumn("Jual");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getAdditionalCharge()), param.getValue().additionalChargeProperty()));
        additionalCharge.setMinWidth(70);

        TableColumn<TblItem, String> brokenCharge = new TableColumn("Kerusakan");
        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBrokenCharge()), param.getValue().brokenChargeProperty()));
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
        totalCurrentStock.setMinWidth(70);

        TableColumn<TblItem, String> stockItem = new TableColumn("Jumlah Stok");
        stockItem.getColumns().addAll(minStock, totalCurrentStock);
        stockItem.setMinWidth(210);

        TableColumn<TblItem, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblUnit().getUnitName(), param.getValue().tblUnitProperty()));
        unitName.setMinWidth(70);

        TableColumn<TblItem, String> equipmentNote = new TableColumn("Keterangan");
        equipmentNote.setCellValueFactory(cellData -> cellData.getValue().itemNoteProperty());
        equipmentNote.setMinWidth(180);

        tableView.getColumns().addAll(idEquipment, equipmentName, equipmentBrand, statusItem,
                costItem, stockItem,
                unitName, equipmentNote);
        tableView.setItems(loadAllDataEquipment());

        tableView.setRowFactory(tv -> {
            TableRow<TblItem> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(equipmentPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == -1);
                } else {
                    row.pseudoClassStateChanged(equipmentPseudoClass, false);
                }
            });

            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataEquipmentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataEquipmentUpdateHandle();
                            } else {
                                dataEquipmentShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataEquipmentUpdateHandle();
//                            } else {
//                                dataEquipmentShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataEquipment = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItem.class,
                tableDataEquipment.getTableView(),
                tableDataEquipment.getTableView().getItems());

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
            List<TblItemLocation> itemLocations = parentController.getFEquipmentManager().getAllDataItemLocationByIDItem(dataItem.getIditem());
            for (TblItemLocation itemLocation : itemLocations) {
                if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4) { //Bin = '4'
                    result = result.add(itemLocation.getItemQuantity());
                }
            }
        }
        return result;
    }

    private void setTableControlDataEquipment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataEquipmentCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataEquipmentUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataEquipmentDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataEquipmentPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataEquipment.addButtonControl(buttonControls);
    }

    private ObservableList<TblItem> loadAllDataEquipment() {
        return FXCollections.observableArrayList(parentController.getFEquipmentManager().getAllDataEquipment());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataEquipment;

    @FXML
    private ScrollPane spFormDataEquipment;

    @FXML
    private JFXTextField txtCodeEquipment;

    @FXML
    private JFXCheckBox chbConsumableStatus;

    @FXML
    private JFXTextField txtEquipmentName;

    @FXML
    private JFXTextField txtEquipmentBrand;

    @FXML
    private JFXTextField txtEquipmentCostOfGoodsSold;

    @FXML
    private JFXTextField txtEquipmentAdditionalCharge;

    @FXML
    private JFXTextField txtEquipmentBrokenCharge;

    @FXML
    private JFXTextField txtEquipmentMinStock;

    @FXML
    private JFXTextArea txtEquipmentNote;

    @FXML
    private Label lblEquipment;

    @FXML
    private AnchorPane unitLayout;
    private JFXCComboBoxTablePopup<TblUnit> cbpUnit;

    @FXML
    private AnchorPane guestLayout;
    private JFXCComboBoxTablePopup<RefItemGuestType> cbpGuest;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblItem selectedData;

    private void initFormDataEquipment() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataEquipment.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataEquipment.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Equipment)"));
        btnSave.setOnAction((e) -> {
            dataEquipmentSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Cancel"));
        btnCancel.setOnAction((e) -> {
            dataEquipmentCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtEquipmentName,
                cbpUnit,
                cbpGuest,
                txtEquipmentCostOfGoodsSold,
                txtEquipmentAdditionalCharge,
                txtEquipmentBrokenCharge,
                txtEquipmentMinStock);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtEquipmentCostOfGoodsSold,
                txtEquipmentAdditionalCharge,
                txtEquipmentBrokenCharge,
                txtEquipmentMinStock);
    }

    private void initDataPopup() {
        //Unit
        TableView<TblUnit> tableUnit = new TableView<>();

        TableColumn<TblUnit, String> unitName = new TableColumn<>("Satuan");
        unitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        unitName.setMinWidth(140);

        tableUnit.getColumns().addAll(unitName);

        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFEquipmentManager().getAllDataUnit());

        cbpUnit = new JFXCComboBoxTablePopup<>(
                TblUnit.class, tableUnit, unitItems, "", "Satuan *", true, 160, 200
        );

        //Guest Type
        TableView<RefItemGuestType> tableGuest = new TableView<>();

        TableColumn<RefItemGuestType, String> guestName = new TableColumn<>("Tipe Tamu");
        guestName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        guestName.setMinWidth(140);

        ObservableList<RefItemGuestType> guestItems = FXCollections.observableArrayList(loadAllDataGuestType());
        tableGuest.getColumns().addAll(guestName);

        cbpGuest = new JFXCComboBoxTablePopup<>(
                RefItemGuestType.class, tableGuest, guestItems, "", "Tipe Tamu *", true, 160, 200
        );

        //attached to grid-pane
        // gpFormDataEquipment.add(cbpUnit, 2, 7);
        // gpFormDataEquipment.add(cbpGuest, 1, 2);
        unitLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpUnit, 0.0);
        AnchorPane.setBottomAnchor(cbpUnit, 0.0);
        AnchorPane.setLeftAnchor(cbpUnit, 0.0);
        AnchorPane.setRightAnchor(cbpUnit, 0.0);
        unitLayout.getChildren().add(cbpUnit);

        guestLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpGuest, 0.0);
        AnchorPane.setBottomAnchor(cbpGuest, 0.0);
        AnchorPane.setLeftAnchor(cbpGuest, 0.0);
        AnchorPane.setRightAnchor(cbpGuest, 0.0);
        guestLayout.getChildren().add(cbpGuest);
    }

    private List<RefItemGuestType> loadAllDataGuestType() {
        List<RefItemGuestType> list = parentController.getFEquipmentManager().getAllDataGuest();
        if (dataInputStatus == 1 //update
                && dataHavingUsedWithAnotherData(selectedData)) {
            //remove data not be used
            for (int i = list.size() - 1; i > -1; i--) {
                if (list.get(i).getIdtype() != 0) {   //Guest = '0'
                    list.remove(i);
                }
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Unit
        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFEquipmentManager().getAllDataUnit());
        cbpUnit.setItems(unitItems);

        //Guest Type
        ObservableList<RefItemGuestType> guestItems = FXCollections.observableArrayList(loadAllDataGuestType());
        cbpGuest.setItems(guestItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        chbConsumableStatus.setDisable(dataInputStatus != 0);

        //  txtCodeEquipment.textProperty().bindBidirectional(selectedData.codeItemProperty());
        txtEquipmentName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        txtEquipmentBrand.textProperty().bindBidirectional(selectedData.itemBrandProperty());
        Bindings.bindBidirectional(txtEquipmentCostOfGoodsSold.textProperty(), selectedData.itemCostOfGoodsSoldProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtEquipmentAdditionalCharge.textProperty(), selectedData.additionalChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtEquipmentBrokenCharge.textProperty(), selectedData.brokenChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtEquipmentMinStock.textProperty(), selectedData.stockMinimalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtEquipmentNote.textProperty().bindBidirectional(selectedData.itemNoteProperty());

        chbConsumableStatus.selectedProperty().bindBidirectional(selectedData.consumableProperty());
//        cbpGuest.valueProperty().bindBidirectional(selectedData.refItemGuestTypeProperty());
        cbpGuest.hide();
        cbpUnit.valueProperty().bindBidirectional(selectedData.tblUnitProperty());

        cbpUnit.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //  txtCodeEquipment.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataEquipment,
                dataInputStatus == 3,
                //        txtCodeEquipment,
                chbConsumableStatus);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataEquipmentCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblItem();
        selectedData.setItemCostOfGoodsSold(new BigDecimal("0"));
        selectedData.setAdditionalCharge(new BigDecimal("0"));
        selectedData.setBrokenCharge(new BigDecimal("0"));
        selectedData.setStockMinimal(new BigDecimal("0"));
        lblEquipment.setText("");
        setSelectedDataToInputForm();
        //open form data equipment
        dataEquipmentFormShowStatus.set(0.0);
        dataEquipmentFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataEquipmentUpdateHandle() {
        if (tableDataEquipment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFEquipmentManager().getEquipment(((TblItem) tableDataEquipment.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            lblEquipment.setText(selectedData.getCodeItem() + " - " + selectedData.getItemName());
            setSelectedDataToInputForm();
            //open form data fequipment
            dataEquipmentFormShowStatus.set(0.0);
            dataEquipmentFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showFailedUpdatingDataMessage(null, null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataEquipmentShowHandle() {
        if (tableDataEquipment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFEquipmentManager().getEquipment(((TblItem) tableDataEquipment.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            setSelectedDataToInputForm();
            dataEquipmentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataEquipmentUnshowHandle() {
        refreshDataTableEquipment();
        dataEquipmentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataEquipmentDeleteHandle() {
        if (tableDataEquipment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (!dataHavingUsedWithAnotherData(((TblItem) tableDataEquipment.getTableView().getSelectionModel().getSelectedItem()))) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    if (parentController.getFEquipmentManager().deleteDataEquipment(new TblItem((TblItem) tableDataEquipment.getTableView().getSelectionModel().getSelectedItem()))) {
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        //refresh data from table & close form data equipment
                        refreshDataTableEquipment();
                        dataEquipmentFormShowStatus.set(0.0);
                    } else {
                        ClassMessage.showFailedDeletingDataMessage("", null);
                    }
                } else {
                    refreshDataTableEquipment();
                    dataEquipmentFormShowStatus.set(0.0);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sedang digunakan sebagai data 'Kartu Kamar',\n"
                        + "tidak dapat dihapus ..!", null);
            }

        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    //check data was having used by sys-data-hardcode (item: 'guest-card')
    private boolean dataHavingUsedWithAnotherData(TblItem dataItem) {
        boolean havingUsed = false;
        if (dataItem != null) {
            SysDataHardCode sdhItemGuestCard = parentController.getFEquipmentManager().getDataSysDataHardCode((long) 1);    //IDGuestCard = '1'
            if (sdhItemGuestCard != null
                    && sdhItemGuestCard.getDataHardCodeValue() != null
                    && sdhItemGuestCard.getDataHardCodeValue().equals(String.valueOf(dataItem.getIditem()))) {
                havingUsed = true;
            }
        }
        return havingUsed;
    }

    private void dataEquipmentPrintHandle() {

    }

    private void dataEquipmentSaveHandle() {
        if (checkDataInputDataEquipment()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItem dummySelectedData = new TblItem(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFEquipmentManager().insertDataEquipment(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data equipment
                            refreshDataTableEquipment();
                            dataEquipmentFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFEquipmentManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFEquipmentManager().updateDataEquipment(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data equipment
                            refreshDataTableEquipment();
                            dataEquipmentFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFEquipmentManager().getErrorMessage(), null);
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

    private void dataEquipmentCancelHandle() {
        //refresh data from table & close form data fequipment
        refreshDataTableEquipment();
        dataEquipmentFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableEquipment() {
        tableDataEquipment.getTableView().setItems(loadAllDataEquipment());
        cft.refreshFilterItems(tableDataEquipment.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataEquipment() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtEquipmentName.getText() == null || txtEquipmentName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Perlengkapan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpUnit.getValue() == null) {
            dataInput = false;
            errDataInput += "Satuan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (cbpGuest.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Tamu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtEquipmentCostOfGoodsSold.getText() == null
                || txtEquipmentCostOfGoodsSold.getText().equals("")
                || txtEquipmentCostOfGoodsSold.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Beli (Terakhir) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getItemCostOfGoodsSold().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Beli (Terakhir) : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtEquipmentAdditionalCharge.getText() == null
                || txtEquipmentAdditionalCharge.getText().equals("")
                || txtEquipmentAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Jual : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getAdditionalCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Jual : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtEquipmentBrokenCharge.getText() == null
                || txtEquipmentBrokenCharge.getText().equals("")
                || txtEquipmentBrokenCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Kerusakan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getBrokenCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Kerusakan : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtEquipmentMinStock.getText() == null
                || txtEquipmentMinStock.getText().equals("")
                || txtEquipmentMinStock.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Stok Minimal: " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getStockMinimal()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Stok Minimal: " + ClassMessage.defaultErrorZeroValueMessage + " \n";
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
        setDataEquipmentSplitpane();

        //init table
        initTableDataEquipment();

        //init form
        initFormDataEquipment();

        spDataEquipment.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataEquipmentFormShowStatus.set(0.0);
        });
    }

    public EquipmentController(FeatureEquipmentController parentController) {
        this.parentController = parentController;
    }

    private final FeatureEquipmentController parentController;

}
