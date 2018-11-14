/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_food_and_beverage.food_and_beverage;

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
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_food_and_beverage.FeatureFoodAndBeverageController;
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
public class FoodAndBeverageController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataFoodAndBeverage;

    private DoubleProperty dataFoodAndBeverageFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataFoodAndBeverageLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataFoodAndBeverageSplitpane() {
        spDataFoodAndBeverage.setDividerPositions(1);

        dataFoodAndBeverageFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataFoodAndBeverageFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataFoodAndBeverage.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataFoodAndBeverage.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataFoodAndBeverageFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataFoodAndBeverageLayout.setDisable(false);
                    tableDataFoodAndBeverageLayoutDisableLayer.setDisable(true);
                    tableDataFoodAndBeverageLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataFoodAndBeverageLayout.setDisable(true);
                    tableDataFoodAndBeverageLayoutDisableLayer.setDisable(false);
                    tableDataFoodAndBeverageLayoutDisableLayer.toFront();
                }
            }
        });

        dataFoodAndBeverageFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataFoodAndBeverageLayout;

    private ClassFilteringTable<TblItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataFoodAndBeverage;

    private final PseudoClass foodandbeveragePseudoClass = PseudoClass.getPseudoClass("foodnbeverage");

    private final PseudoClass refreshPseudoClass = PseudoClass.getPseudoClass("refresh");

    private void initTableDataFoodAndBeverage() {
        //set table
        setTableDataFoodAndBeverage();
        //set control
        setTableControlDataFoodAndBeverage();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataFoodAndBeverage, 15.0);
        AnchorPane.setLeftAnchor(tableDataFoodAndBeverage, 15.0);
        AnchorPane.setRightAnchor(tableDataFoodAndBeverage, 15.0);
        AnchorPane.setTopAnchor(tableDataFoodAndBeverage, 15.0);
        ancBodyLayout.getChildren().add(tableDataFoodAndBeverage);
    }

    private void setTableDataFoodAndBeverage() {
        TableView<TblItem> tableView = new TableView();

        TableColumn<TblItem, String> idFoodAndBeverage = new TableColumn("ID");
        idFoodAndBeverage.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        idFoodAndBeverage.setMinWidth(120);

        TableColumn<TblItem, String> foodAndBeverageName = new TableColumn("Food & Beverage");
        foodAndBeverageName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        foodAndBeverageName.setMinWidth(140);

        TableColumn<TblItem, String> foodAndBeverageBrand = new TableColumn("Merk");
        foodAndBeverageBrand.setCellValueFactory(cellData -> cellData.getValue().itemBrandProperty());
        foodAndBeverageBrand.setMinWidth(120);

        TableColumn<TblItem, String> costOfGoodsSold = new TableColumn("Beli");
        costOfGoodsSold.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCostOfGoodsSold()), param.getValue().itemCostOfGoodsSoldProperty()));
        costOfGoodsSold.setMinWidth(70);

        TableColumn<TblItem, String> additionalCharge = new TableColumn("Jual");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getAdditionalCharge()), param.getValue().additionalChargeProperty()));
        additionalCharge.setMinWidth(70);

        TableColumn<TblItem, String> costItem = new TableColumn("Harga Barang");
        costItem.getColumns().addAll(costOfGoodsSold, additionalCharge);
//        TableColumn<TblItem, String> brokenCharge = new TableColumn("Broken Charge");
//        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBrokenCharge()), param.getValue().brokenChargeProperty()));
//        brokenCharge.setMinWidth(140);

        TableColumn<TblItem, String> tipeGuest = new TableColumn("Tamu");
        tipeGuest.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getGuestStatus() ? "Guest" : "NonGuest", 
                        param.getValue().guestStatusProperty()));
        tipeGuest.setMinWidth(70);

        TableColumn<TblItem, String> tipeConsumable = new TableColumn("Kadaluarsa");
        tipeConsumable.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getConsumable() == true ? "Kadaluarsa" : "Tidak Kadaluarsa", param.getValue().consumableProperty()));
        tipeConsumable.setMinWidth(70);

        TableColumn<TblItem, String> statusItem = new TableColumn("Status Barang");
        statusItem.getColumns().addAll(tipeGuest, tipeConsumable);

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

        TableColumn<TblItem, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblUnit().getUnitName(), param.getValue().tblUnitProperty()));
        unitName.setMinWidth(70);

        TableColumn<TblItem, String> foodAndBeverageNote = new TableColumn("Keterangan");
        foodAndBeverageNote.setCellValueFactory(cellData -> cellData.getValue().itemNoteProperty());
        foodAndBeverageNote.setMinWidth(180);

        tableView.getColumns().addAll(idFoodAndBeverage, foodAndBeverageName, foodAndBeverageBrand,
                statusItem, costItem, stockItem,
                unitName, foodAndBeverageNote);

        tableView.setItems(loadAllDataFoodAndBeverage());

        tableView.setRowFactory(tv -> {
            TableRow<TblItem> row = new TableRow<>();

            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {

                    row.pseudoClassStateChanged(foodandbeveragePseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == -1);
                    //.pseudoClassStateChanged(refreshPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == 1);
                    //row.pseudoClassStateChanged(leaveAnnualPseudoClass,newVal.getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now())) && newVal.getRefEmployeeLeaveType().getIdtype()==0);
                    //System.out.println(">>"+newVal.getTblEmployeeByIdemployee().getCodeEmployee());
                } else {
                    row.pseudoClassStateChanged(foodandbeveragePseudoClass, false);
                }
                //row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
            });

            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataFoodAndBeverageUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataFoodAndBeverageUpdateHandle();
                            } else {
                                dataFoodAndBeverageShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataFoodAndBeverageUpdateHandle();
//                            } else {
//                                dataFoodAndBeverageShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataFoodAndBeverage = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItem.class,
                tableDataFoodAndBeverage.getTableView(),
                tableDataFoodAndBeverage.getTableView().getItems());

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
            List<TblItemLocation> itemLocations = parentController.getFFoodAndBeverageManager().getAllDataItemLocationByIDItem(dataItem.getIditem());
            for (TblItemLocation itemLocation : itemLocations) {
                if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4) { //Bin = '4'
                    result = result.add(itemLocation.getItemQuantity());
                }
            }
        }
        return result;
    }

    private void setTableControlDataFoodAndBeverage() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataFoodAndBeverageCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataFoodAndBeverageUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataFoodAndBeverageDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataFoodAndBeveragePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataFoodAndBeverage.addButtonControl(buttonControls);
    }

    private ObservableList<TblItem> loadAllDataFoodAndBeverage() {
        return FXCollections.observableArrayList(parentController.getFFoodAndBeverageManager().getAllDataFoodAndBeverage());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataFoodAndBeverage;

    @FXML
    private ScrollPane spFormDataFoodAndBeverage;

    @FXML
    private JFXTextField txtCodeFoodAndBeverage;

    @FXML
    private JFXCheckBox chbConsumable;

    @FXML
    private JFXTextField txtFoodAndBeverageName;

    @FXML
    private JFXTextField txtFoodAndBeverageBrand;

    @FXML
    private JFXTextField txtFoodAndBeverageCostOfGoodsSold;

    @FXML
    private JFXTextField txtFoodAndBeverageAdditionalCharge;

    @FXML
    private JFXTextField txtFoodAndBeverageBrokenCharge;

    @FXML
    private JFXTextField txtFoodAndBeverageMinStock;

    @FXML
    private JFXTextArea txtFoodAndBeverageNote;

    @FXML
    private Label lblFoodAndBeverage;

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

    private void initFormDataFoodAndBeverage() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataFoodAndBeverage.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataFoodAndBeverage.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Food & Beverage)"));
        btnSave.setOnAction((e) -> {
            dataFoodAndBeverageSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataFoodAndBeverageCancelHandle();
        });

        //txtFoodAndBeverageBrokenCharge.setVisible(false);
        initImportantFieldColor();

        initNumbericField();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtFoodAndBeverageName,
                cbpUnit,
                cbpGuest,
                txtFoodAndBeverageCostOfGoodsSold,
                txtFoodAndBeverageAdditionalCharge,
                txtFoodAndBeverageMinStock);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtFoodAndBeverageCostOfGoodsSold,
                txtFoodAndBeverageAdditionalCharge,
                //  txtFoodAndBeverageBrokenCharge,
                txtFoodAndBeverageMinStock);
    }

    private void initDataPopup() {
        //Unit
        TableView<TblUnit> tableUnit = new TableView<>();

        TableColumn<TblUnit, String> unitName = new TableColumn<>("Satuan");
        unitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        unitName.setMinWidth(140);

        tableUnit.getColumns().addAll(unitName);

        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFFoodAndBeverageManager().getAllDataUnit());

        cbpUnit = new JFXCComboBoxTablePopup<>(
                TblUnit.class, tableUnit, unitItems, "", "Satuan *", true, 160, 200
        );

        //Guest Type
        TableView<RefItemGuestType> tableGuest = new TableView<>();

        TableColumn<RefItemGuestType, String> guestName = new TableColumn<>("Tipe Tamu");
        guestName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        guestName.setMinWidth(140);

        ObservableList<RefItemGuestType> guestItems = FXCollections.observableArrayList(parentController.getFFoodAndBeverageManager().getAllDataGuest());
        tableGuest.getColumns().addAll(guestName);

        cbpGuest = new JFXCComboBoxTablePopup<>(
                RefItemGuestType.class, tableGuest, guestItems, "", "Tipe Tamu *", true, 160, 200
        );

        //attached to grid-pane
        //gpFormDataFoodAndBeverage.add(cbpUnit, 2, 7);
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
        //gpFormDataFoodAndBeverage.add(cbpGuest, 1, 2);
    }

    private void refreshDataPopup() {
        //Unit
        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFFoodAndBeverageManager().getAllDataUnit());
        cbpUnit.setItems(unitItems);

        //Guest Type
        ObservableList<RefItemGuestType> guestItems = FXCollections.observableArrayList(parentController.getFFoodAndBeverageManager().getAllDataGuest());
        cbpGuest.setItems(guestItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();
        chbConsumable.setDisable(true);
        chbConsumable.selectedProperty().bindBidirectional(selectedData.consumableProperty());

//        cbpGuest.valueProperty().bindBidirectional(selectedData.refItemGuestTypeProperty());
        cbpGuest.hide();

        //    txtCodeFoodAndBeverage.textProperty().bindBidirectional(selectedData.codeItemProperty());
        txtFoodAndBeverageName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        txtFoodAndBeverageBrand.textProperty().bindBidirectional(selectedData.itemBrandProperty());
        Bindings.bindBidirectional(txtFoodAndBeverageCostOfGoodsSold.textProperty(), selectedData.itemCostOfGoodsSoldProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtFoodAndBeverageAdditionalCharge.textProperty(), selectedData.additionalChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
//        Bindings.bindBidirectional(txtFoodAndBeverageBrokenCharge.textProperty(), selectedData.brokenChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtFoodAndBeverageMinStock.textProperty(), selectedData.stockMinimalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtFoodAndBeverageNote.textProperty().bindBidirectional(selectedData.itemNoteProperty());

        cbpUnit.valueProperty().bindBidirectional(selectedData.tblUnitProperty());

        cbpUnit.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //   txtCodeFoodAndBeverage.setDisable(true);
        chbConsumable.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataFoodAndBeverage,
                dataInputStatus == 3,
                //         txtCodeFoodAndBeverage,
                chbConsumable);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataFoodAndBeverageCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblItem();
        selectedData.setItemCostOfGoodsSold(new BigDecimal("0"));
        selectedData.setAdditionalCharge(new BigDecimal("0"));
        selectedData.setBrokenCharge(new BigDecimal("0"));
        selectedData.setStockMinimal(new BigDecimal("0"));
        selectedData.setConsumable(true);
        lblFoodAndBeverage.setText("");
        setSelectedDataToInputForm();
        //open form data food and beverage
        dataFoodAndBeverageFormShowStatus.set(0.0);
        dataFoodAndBeverageFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataFoodAndBeverageUpdateHandle() {
        if (tableDataFoodAndBeverage.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFFoodAndBeverageManager().getFoodAndBeverage(((TblItem) tableDataFoodAndBeverage.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            lblFoodAndBeverage.setText(selectedData.getCodeItem() + " - " + selectedData.getItemName());
            setSelectedDataToInputForm();
            //open form data food and beverage
            dataFoodAndBeverageFormShowStatus.set(0.0);
            dataFoodAndBeverageFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataFoodAndBeverageShowHandle() {
        if (tableDataFoodAndBeverage.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFFoodAndBeverageManager().getFoodAndBeverage(((TblItem) tableDataFoodAndBeverage.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            lblFoodAndBeverage.setText(selectedData.getCodeItem() + " - " + selectedData.getItemName());
            setSelectedDataToInputForm();
            dataFoodAndBeverageFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataFoodAndBeverageUnshowHandle() {
        refreshDataTableFoodAndBeverage();
        dataFoodAndBeverageFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataFoodAndBeverageDeleteHandle() {
        if (tableDataFoodAndBeverage.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFFoodAndBeverageManager().deleteDataFoodAndBeverage(new TblItem((TblItem) tableDataFoodAndBeverage.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data food and beverage
                    refreshDataTableFoodAndBeverage();
                    dataFoodAndBeverageFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFFoodAndBeverageManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataFoodAndBeveragePrintHandle() {

    }

    private void dataFoodAndBeverageSaveHandle() {
        if (checkDataInputDataFoodAndBeverage()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //set data food & beverage - broken charge
                selectedData.setBrokenCharge(selectedData.getAdditionalCharge());
                //dummy entry
                TblItem dummySelectedData = new TblItem(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFFoodAndBeverageManager().insertDataFoodAndBeverage(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data food and beverage
                            refreshDataTableFoodAndBeverage();
                            dataFoodAndBeverageFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFFoodAndBeverageManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFFoodAndBeverageManager().updateDataFoodAndBeverage(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data food and beverage
                            refreshDataTableFoodAndBeverage();
                            dataFoodAndBeverageFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFFoodAndBeverageManager().getErrorMessage(), null);
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

    private void dataFoodAndBeverageCancelHandle() {
        //refresh data from table & close form data food and beverage
        refreshDataTableFoodAndBeverage();
        dataFoodAndBeverageFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableFoodAndBeverage() {
        tableDataFoodAndBeverage.getTableView().setItems(loadAllDataFoodAndBeverage());
        cft.refreshFilterItems(tableDataFoodAndBeverage.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataFoodAndBeverage() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtFoodAndBeverageName.getText() == null || txtFoodAndBeverageName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Food & Beverage : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpUnit.getValue() == null) {
            dataInput = false;
            errDataInput += "Satuan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (cbpGuest.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Tamu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (txtFoodAndBeverageCostOfGoodsSold.getText() == null
                || txtFoodAndBeverageCostOfGoodsSold.getText().equals("")
                || txtFoodAndBeverageCostOfGoodsSold.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Beli (Terakhir) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getItemCostOfGoodsSold().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Beli (Terakhir) : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtFoodAndBeverageAdditionalCharge.getText() == null
                || txtFoodAndBeverageAdditionalCharge.getText().equals("")
                || txtFoodAndBeverageAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Jual : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getAdditionalCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Jual : Tidak boleh lebih kecil dari '0' \n";
            }
        }
//        if(txtFoodAndBeverageBrokenCharge.getText() == null 
//                || txtFoodAndBeverageBrokenCharge.getText().equals("")
//                || txtFoodAndBeverageBrokenCharge.getText().equals("-")){
//            dataInput = false;
//            errDataInput += "Biaya Kerusakan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }else{
//            if(selectedData.getBrokenCharge().compareTo(new BigDecimal("0")) == -1){
//                dataInput = false;
//                errDataInput += "Biaya Kerusakan : Tidak boleh lebih kecil dari '0' \n";
//            }
//        }
        if (txtFoodAndBeverageMinStock.getText() == null
                || txtFoodAndBeverageMinStock.getText().equals("")
                || txtFoodAndBeverageMinStock.getText().equals("-")) {
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
        setDataFoodAndBeverageSplitpane();

        //init table
        initTableDataFoodAndBeverage();

        //init form
        initFormDataFoodAndBeverage();

        spDataFoodAndBeverage.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataFoodAndBeverageFormShowStatus.set(0.0);
        });
    }

    public FoodAndBeverageController(FeatureFoodAndBeverageController parentController) {
        this.parentController = parentController;
    }

    private final FeatureFoodAndBeverageController parentController;

}
