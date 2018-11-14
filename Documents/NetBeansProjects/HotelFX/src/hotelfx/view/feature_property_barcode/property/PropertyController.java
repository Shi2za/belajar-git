/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_property_barcode.property;

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
import hotelfx.view.feature_property_barcode.FeaturePropertyBarcodeController;
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
public class PropertyController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataProperty;

    private DoubleProperty dataPropertyFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPropertyLayoutDisableLayer;

    private final PseudoClass propertyPseudoClass = PseudoClass.getPseudoClass("property");

    private final PseudoClass refreshPseudoClass = PseudoClass.getPseudoClass("refresh");

    private boolean isTimeLinePlaying = false;

    private void setDataPropertySplitpane() {
        spDataProperty.setDividerPositions(1);

        dataPropertyFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPropertyFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataProperty.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataProperty.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPropertyFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPropertyLayout.setDisable(false);
                    tableDataPropertyLayoutDisableLayer.setDisable(true);
                    tableDataPropertyLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPropertyLayout.setDisable(true);
                    tableDataPropertyLayoutDisableLayer.setDisable(false);
                    tableDataPropertyLayoutDisableLayer.toFront();
                }
            }
        });

        dataPropertyFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPropertyLayout;

    private ClassFilteringTable<TblItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataProperty;

    private void initTableDataProperty() {
        //set table
        setTableDataProperty();
        //set control
        setTableControlDataProperty();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataProperty, 15.0);
        AnchorPane.setLeftAnchor(tableDataProperty, 15.0);
        AnchorPane.setRightAnchor(tableDataProperty, 15.0);
        AnchorPane.setTopAnchor(tableDataProperty, 15.0);
        ancBodyLayout.getChildren().add(tableDataProperty);
    }

    private void setTableDataProperty() {
        TableView<TblItem> tableView = new TableView();
        TableColumn<TblItem, String> idProperty = new TableColumn("ID");
        idProperty.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        idProperty.setMinWidth(100);

        TableColumn<TblItem, String> propertyName = new TableColumn("Nama");
        propertyName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        propertyName.setMinWidth(140);

        TableColumn<TblItem, String> propertyBrand = new TableColumn("Merek");
        propertyBrand.setCellValueFactory(cellData -> cellData.getValue().itemBrandProperty());
        propertyBrand.setMinWidth(120);

        TableColumn<TblItem, String> tipeGuest = new TableColumn("Tamu");
        tipeGuest.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getGuestStatus() ? "Guest" : "NonGuest", 
                        param.getValue().guestStatusProperty()));
        tipeGuest.setMinWidth(70);
        TableColumn<TblItem, String> tipeConsumable = new TableColumn("Kadaluarsa");
        tipeConsumable.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getConsumable() == true ? "Kadaluarsa" : "Tidak Kadaluarsa", param.getValue().consumableProperty()));
        tipeConsumable.setMinWidth(70);
        TableColumn<TblItem, String> tipeLeased = new TableColumn("Sewa");
        tipeLeased.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getLeasedStatus() ? "Disewakan" : "Tidak Disewakan", param.getValue().leasedStatusProperty()));
        tipeLeased.setMinWidth(140);

        TableColumn<TblItem, String> statusItem = new TableColumn("Status Barang");
        statusItem.getColumns().addAll(tipeGuest, tipeLeased);
        statusItem.setMinWidth(310);

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
        costItem.setMinWidth(300);

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
        stockItem.setMinWidth(160);

        TableColumn<TblItem, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblUnit().getUnitName(), param.getValue().tblUnitProperty()));
        unitName.setMinWidth(70);

        TableColumn<TblItem, String> propertyNote = new TableColumn("Keterangan");
        propertyNote.setCellValueFactory(cellData -> cellData.getValue().itemNoteProperty());
        tableView.getColumns().addAll(idProperty, propertyName, propertyBrand, statusItem,
                costItem, stockItem, unitName, propertyNote);
        tableView.setItems(loadAllDataProperty());
        propertyNote.setMinWidth(180);

        tableView.setRowFactory(tv -> {
            TableRow<TblItem> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {

                    row.pseudoClassStateChanged(propertyPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == -1);
                    //row.pseudoClassStateChanged(refreshPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == 1);
                    //row.pseudoClassStateChanged(leaveAnnualPseudoClass,newVal.getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now())) && newVal.getRefEmployeeLeaveType().getIdtype()==0);
                    //System.out.println(">>"+newVal.getTblEmployeeByIdemployee().getCodeEmployee());
                } else {
                    row.pseudoClassStateChanged(propertyPseudoClass, false);
                }
                //row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
            });

            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPropertyUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataPropertyUpdateHandle();
                            } else {
                                dataPropertyShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataPropertyUpdateHandle();
//                            } else {
//                                dataPropertyShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataProperty = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItem.class,
                tableDataProperty.getTableView(),
                tableDataProperty.getTableView().getItems());

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
            List<TblItemLocation> itemLocations = parentController.getFPropertyBarcodeManager().getAllDataItemLocationByIDItem(dataItem.getIditem());
            for (TblItemLocation itemLocation : itemLocations) {
                if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4) { //Bin = '4'
                    result = result.add(itemLocation.getItemQuantity());
                }
            }
        }
        return result;
    }

    private void setTableControlDataProperty() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataPropertyCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataPropertyUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataPropertyDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataPropertyPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataProperty.addButtonControl(buttonControls);
    }

    private ObservableList<TblItem> loadAllDataProperty() {
        return FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataItem());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataProperty;

    @FXML
    private ScrollPane spFormDataProperty;

    @FXML
    private Label lblCodeData;
//    @FXML
//    private JFXTextField txtCodeProperty;

    @FXML
    private JFXCheckBox chbConsumableStatus;

    @FXML
    private JFXCheckBox chbLeasedStatus;

    @FXML
    private JFXTextField txtPropertyName;

    @FXML
    private JFXTextField txtPropertyBrand;

    @FXML
    private JFXTextField txtPropertyCostOfGoodsSold;

    @FXML
    private JFXTextField txtPropertyAdditionalCharge;

    @FXML
    private JFXTextField txtPropertyBrokenCharge;

    @FXML
    private JFXTextField txtPropertyMinStock;

    @FXML
    private JFXTextArea txtPropertyNote;

    @FXML
    private AnchorPane ancGuestTypeLayout;
    private JFXCComboBoxTablePopup<RefItemGuestType> cbpGuest;

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

    private void initFormDataProperty() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataProperty.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataProperty.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Asset)"));
        btnSave.setOnAction((e) -> {
            dataPropertySaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataPropertyCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtPropertyName,
                cbpUnit,
                cbpGuest,
                txtPropertyCostOfGoodsSold,
                txtPropertyAdditionalCharge,
                txtPropertyBrokenCharge,
                txtPropertyMinStock);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtPropertyCostOfGoodsSold,
                txtPropertyAdditionalCharge,
                txtPropertyBrokenCharge,
                txtPropertyMinStock);
    }

    private void initDataPopup() {
        //Guest Type
        TableView<RefItemGuestType> tableGuest = new TableView<>();

        TableColumn<RefItemGuestType, String> guestType = new TableColumn<>("Tipe Tamu");
        guestType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        guestType.setMinWidth(140);

        tableGuest.getColumns().addAll(guestType);

        ObservableList<RefItemGuestType> guestTypeItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllGuestType());

        cbpGuest = new JFXCComboBoxTablePopup<>(
                RefItemGuestType.class, tableGuest, guestTypeItems, "", "Tipe Tamu *", true, 160, 200
        );

        //Unit
        TableView<TblUnit> tableUnit = new TableView<>();

        TableColumn<TblUnit, String> unitName = new TableColumn<>("Satuan");
        unitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        unitName.setMinWidth(140);

        tableUnit.getColumns().addAll(unitName);

        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataUnit());

        cbpUnit = new JFXCComboBoxTablePopup<>(
                TblUnit.class, tableUnit, unitItems, "", "Satuan *", true, 160, 200
        );

        //attached to grid-pane
        ancGuestTypeLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpGuest, 0.0);
        AnchorPane.setLeftAnchor(cbpGuest, 0.0);
        AnchorPane.setRightAnchor(cbpGuest, 0.0);
        AnchorPane.setTopAnchor(cbpGuest, 0.0);
        ancGuestTypeLayout.getChildren().add(cbpGuest);
        ancUnitLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpUnit, 0.0);
        AnchorPane.setLeftAnchor(cbpUnit, 0.0);
        AnchorPane.setRightAnchor(cbpUnit, 0.0);
        AnchorPane.setTopAnchor(cbpUnit, 0.0);
        ancUnitLayout.getChildren().add(cbpUnit);
    }

    private void refreshDataPopup() {
        ObservableList<RefItemGuestType> guestTypeItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllGuestType());
        cbpGuest.setItems(guestTypeItems);

        //Unit
        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataUnit());
        cbpUnit.setItems(unitItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        lblCodeData.setText(selectedData.getCodeItem() != null
                ? selectedData.getCodeItem() : "");

        chbConsumableStatus.selectedProperty().bindBidirectional(selectedData.consumableProperty());
        chbConsumableStatus.setDisable(true);
        chbConsumableStatus.setVisible(false);

        chbLeasedStatus.selectedProperty().bindBidirectional(selectedData.leasedStatusProperty());

        txtPropertyName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        txtPropertyBrand.textProperty().bindBidirectional(selectedData.itemBrandProperty());
        Bindings.bindBidirectional(txtPropertyCostOfGoodsSold.textProperty(), selectedData.itemCostOfGoodsSoldProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtPropertyAdditionalCharge.textProperty(), selectedData.additionalChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtPropertyBrokenCharge.textProperty(), selectedData.brokenChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtPropertyMinStock.textProperty(), selectedData.stockMinimalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtPropertyNote.textProperty().bindBidirectional(selectedData.itemNoteProperty());

        cbpUnit.valueProperty().bindBidirectional(selectedData.tblUnitProperty());
        
        cbpUnit.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        chbConsumableStatus.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataProperty,
                dataInputStatus == 3,
                chbConsumableStatus);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataPropertyCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblItem();
        selectedData.setItemCostOfGoodsSold(new BigDecimal("0"));
        selectedData.setAdditionalCharge(new BigDecimal("0"));
        selectedData.setBrokenCharge(new BigDecimal("0"));
        selectedData.setStockMinimal(new BigDecimal("0"));
        selectedData.setConsumable(false);
        setSelectedDataToInputForm();
        //open form data property
        dataPropertyFormShowStatus.set(0.0);
        dataPropertyFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataPropertyUpdateHandle() {
        if (tableDataProperty.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFPropertyBarcodeManager().getItem(((TblItem) tableDataProperty.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            setSelectedDataToInputForm();
            //open form data property
            dataPropertyFormShowStatus.set(0.0);
            dataPropertyFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPropertyShowHandle() {
        if (tableDataProperty.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFPropertyBarcodeManager().getItem(((TblItem) tableDataProperty.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            setSelectedDataToInputForm();
            dataPropertyFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPropertyUnshowHandle() {
        refreshDataTableProperty();
        dataPropertyFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPropertyDeleteHandle() {
        if (tableDataProperty.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFPropertyBarcodeManager().deleteDataItem((TblItem) tableDataProperty.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedInsertingDataMessage("", null);
                    //refresh data from table & close form data property
                    refreshDataTableProperty();
                    dataPropertyFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage("", null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataPropertyPrintHandle() {

    }

    private void dataPropertySaveHandle() {
        if (checkDataInputDataProperty()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItem dummySelectedData = new TblItem(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFPropertyBarcodeManager().insertDataItem(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data property
                            refreshDataTableProperty();
                            dataPropertyFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFPropertyBarcodeManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFPropertyBarcodeManager().updateDataItem(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data property
                            refreshDataTableProperty();
                            dataPropertyFormShowStatus.set(0.0);
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

    private void dataPropertyCancelHandle() {
        //refresh data from table & close form data property
        refreshDataTableProperty();
        dataPropertyFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableProperty() {
        tableDataProperty.getTableView().setItems(loadAllDataProperty());
        cft.refreshFilterItems(tableDataProperty.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataProperty() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtPropertyName.getText() == null || txtPropertyName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Asset : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpUnit.getValue() == null) {
            dataInput = false;
            errDataInput += "Satuan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (cbpGuest.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Tamu :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (txtPropertyCostOfGoodsSold.getText() == null
                || txtPropertyCostOfGoodsSold.getText().equals("")
                || txtPropertyCostOfGoodsSold.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Beli (Terakhir) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getItemCostOfGoodsSold().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Beli (Terakhir) : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtPropertyAdditionalCharge.getText() == null
                || txtPropertyAdditionalCharge.getText().equals("")
                || txtPropertyAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Jual : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getAdditionalCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Jual : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtPropertyBrokenCharge.getText() == null
                || txtPropertyBrokenCharge.getText().equals("")
                || txtPropertyBrokenCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Kerusakan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getBrokenCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Kerusakan : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtPropertyMinStock.getText() == null
                || txtPropertyMinStock.getText().equals("")
                || txtPropertyMinStock.getText().equals("-")) {
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
        setDataPropertySplitpane();

        //init table
        initTableDataProperty();

        //init form
        initFormDataProperty();

        spDataProperty.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPropertyFormShowStatus.set(0.0);
        });
    }

    public PropertyController(FeaturePropertyBarcodeController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePropertyBarcodeController parentController;

}
