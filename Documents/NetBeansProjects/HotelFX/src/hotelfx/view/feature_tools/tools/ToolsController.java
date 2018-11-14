/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_tools.tools;

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
import hotelfx.view.feature_tools.FeatureToolsController;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class ToolsController implements Initializable {

    @FXML
    private SplitPane spDataTools;

    private DoubleProperty dataToolsFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataToolsLayoutDisableLayer;

    private final PseudoClass toolsPseudoClass = PseudoClass.getPseudoClass("tools");

    private final PseudoClass refreshPseudoClass = PseudoClass.getPseudoClass("refresh");

    private boolean isTimeLinePlaying = false;

    private void setDataToolsSplitPane() {
        spDataTools.setDividerPositions(1);

        dataToolsFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataToolsFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataTools.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataTools.getDividers().get(0);
        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataToolsFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (inputDataStatus != 3) {
                if (newVal.doubleValue() == 0) {
                    tableDataToolsLayout.setDisable(false);
                    tableDataToolsLayoutDisableLayer.setDisable(true);
                    tableDataToolsLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataToolsLayout.setDisable(true);
                    tableDataToolsLayoutDisableLayer.setDisable(false);
                    tableDataToolsLayoutDisableLayer.toFront();
                }
            }

        });
    }

    @FXML
    private AnchorPane tableDataToolsLayout;

    private ClassFilteringTable<TblItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataTools;

    private void initTableDataTools() {
        setTableDataTools();

        setTableControlDataTools();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataTools, 15.0);
        AnchorPane.setBottomAnchor(tableDataTools, 15.0);
        AnchorPane.setLeftAnchor(tableDataTools, 15.0);
        AnchorPane.setRightAnchor(tableDataTools, 15.0);
        ancBodyLayout.getChildren().addAll(tableDataTools);
    }

    private void setTableDataTools() {
        TableView<TblItem> tblView = new TableView();

        TableColumn<TblItem, String> idTools = new TableColumn("ID");
        idTools.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        idTools.setMinWidth(100);
        TableColumn<TblItem, String> nameTools = new TableColumn("Nama Peralatan");
        nameTools.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        nameTools.setMinWidth(140);
        TableColumn<TblItem, String> brandTools = new TableColumn("Merek");
        brandTools.setCellValueFactory(cellData -> cellData.getValue().itemBrandProperty());
        brandTools.setMinWidth(100);

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
        statusItem.setMinWidth(300);

        TableColumn<TblItem, String> costOfGoodSold = new TableColumn("Beli");
        costOfGoodSold.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCostOfGoodsSold()), param.getValue().itemCostOfGoodsSoldProperty()));
        costOfGoodSold.setMinWidth(70);

        TableColumn<TblItem, String> itemAdditionalCharge = new TableColumn("Jual");
        itemAdditionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getAdditionalCharge()), param.getValue().additionalChargeProperty()));
        itemAdditionalCharge.setMinWidth(70);

        TableColumn<TblItem, String> itemBrokenCharge = new TableColumn("Kerusakan");
        itemBrokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBrokenCharge()), param.getValue().brokenChargeProperty()));
        itemBrokenCharge.setMinWidth(70);

        TableColumn<TblItem, String> costItem = new TableColumn("Harga Barang");
        costItem.getColumns().addAll(costOfGoodSold, itemAdditionalCharge, itemBrokenCharge);
        costItem.setMinWidth(300);

        TableColumn<TblItem, String> minStock = new TableColumn("Minimal");
        minStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getStockMinimal()), param.getValue().stockMinimalProperty()));

        TableColumn<TblItem, String> currentStock = new TableColumn("Sekarang");
        currentStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(getTotalCurrentStock(param.getValue())), param.getValue().itemCostOfGoodsSoldProperty()));
        TableColumn<TblItem, String> stockItem = new TableColumn("Jumlah Stok");
        stockItem.getColumns().addAll(minStock, currentStock);
        stockItem.setMinWidth(300);

        TableColumn<TblItem, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblUnit().getUnitName(), param.getValue().tblUnitProperty()));
        unitName.setMinWidth(70);

        TableColumn<TblItem, String> noteItem = new TableColumn("Keterangan");
        noteItem.setCellValueFactory(cellData -> cellData.getValue().itemNoteProperty());
        noteItem.setMinWidth(180);

        tblView.getColumns().addAll(idTools, nameTools, brandTools, statusItem, costItem, stockItem, unitName, noteItem);
        tblView.setItems(loadAllDataTools());

        tblView.setRowFactory(tv -> {
            TableRow<TblItem> row = new TableRow();
            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {

                    row.pseudoClassStateChanged(toolsPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == -1);
                    row.pseudoClassStateChanged(refreshPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == 1);
                    //row.pseudoClassStateChanged(leaveAnnualPseudoClass,newVal.getSysCalendar().getCalendarDate().before(Date.valueOf(LocalDate.now())) && newVal.getRefEmployeeLeaveType().getIdtype()==0);
                    //System.out.println(">>"+newVal.getTblEmployeeByIdemployee().getCodeEmployee());
                } else {
                    row.pseudoClassStateChanged(toolsPseudoClass, false);
                }
                //row.pseudoClassStateChanged(schedulePseudoClass,((TblCalendarEmployeeLeave)tableDataLeave.getTableView().getItems().get(i)).getSysCalendar().getCalendarDate().equals(newVal.getSysCalendar().getCalendarDate())); 
            });

            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataToolsUnshowHandle();
                    } else {
                        if (!row.isEmpty()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataToolsUpdateHandle();
                            } else {
                                dataToolsShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                dataToolsUpdateHandle();
//                            } else {
//                                dataToolsShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });
        tableDataTools = new ClassTableWithControl(tblView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItem.class,
                tableDataTools.getTableView(),
                tableDataTools.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private BigDecimal getTotalCurrentStock(TblItem item) {
        BigDecimal result = new BigDecimal("0");
        if (item != null) {
            List<TblItemLocation> listItemLocation = parentController.getFToolsManager().getAllDataItemLocationByIDItem(item.getIditem());
            for (TblItemLocation getItemLocation : listItemLocation) {
                if (getItemLocation.getTblLocation().getRefLocationType().getIdtype() != 4) { //Bin = '4'
                    result = result.add(getItemLocation.getItemQuantity());
                }
            }
        }
        return result;
    }

    private ObservableList<TblItem> loadAllDataTools() {
        return FXCollections.observableArrayList(parentController.getFToolsManager().getAllDataTools());
    }

    private void setTableControlDataTools() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataToolsCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataToolsUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataToolsDeleteHandle();
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
        tableDataTools.addButtonControl(buttonControls);
    }

    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataTools;

    @FXML
    private ScrollPane spFormDataTools;

    @FXML
    private JFXTextField txtCodeTools;

    @FXML
    private JFXCheckBox chbConsumableStatus;

    @FXML
    private JFXTextField txtToolsName;

    @FXML
    private JFXTextField txtToolsBrand;

    @FXML
    private JFXTextField txtToolsCostOfGoodSold;

    @FXML
    private JFXTextField txtToolsAdditionalCharge;

    @FXML
    private JFXTextField txtToolsBrokenCharge;

    @FXML
    private JFXTextField txtToolsMinStock;

    @FXML
    private JFXTextArea txtToolsNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Label lblTools;

    @FXML
    private AnchorPane guestLayout;
    private JFXCComboBoxTablePopup<TblUnit> cbpUnit;

    @FXML
    private AnchorPane unitLayout;
    private JFXCComboBoxTablePopup<RefItemGuestType> cbpGuest;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;

    private TblItem selectedData;

    private int inputDataStatus = 0;

    private void initFormDataTools() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataTools.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataTools.setOnScroll((ScrollEvent scroll) -> {
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
                } catch (InterruptedException ex) {
                    System.out.println("err " + ex.getMessage());
                    //Logger.getLogger(ToolsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        initDataPopup();

        btnSave.setOnAction((e) -> {
            dataToolsSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
            dataToolsCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtToolsName,
                txtToolsCostOfGoodSold,
                txtToolsAdditionalCharge,
                txtToolsBrokenCharge,
                txtToolsMinStock,
                cbpUnit,
                cbpGuest);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtToolsCostOfGoodSold,
                txtToolsAdditionalCharge,
                txtToolsBrokenCharge,
                txtToolsMinStock);
    }

    private void initDataPopup() {
        TableView<TblUnit> tblUnit = new TableView();

        TableColumn<TblUnit, String> nameUnit = new TableColumn("Satuan");
        nameUnit.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        nameUnit.setMinWidth(140);

        tblUnit.getColumns().add(nameUnit);

        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFToolsManager().getAllDataUnit());

        cbpUnit = new JFXCComboBoxTablePopup<>(
                TblUnit.class, tblUnit, unitItems, "", "Satuan *", true, 160, 200
        );

        //Guest Type
        TableView<RefItemGuestType> tableGuest = new TableView();

        TableColumn<RefItemGuestType, String> nameGuest = new TableColumn("Tipe Tamu");
        nameGuest.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        tableGuest.getColumns().add(nameGuest);
        nameGuest.setMinWidth(140);

        ObservableList<RefItemGuestType> guestItems = FXCollections.observableArrayList(parentController.getFToolsManager().getAllGuestType());

        cbpGuest = new JFXCComboBoxTablePopup<>(
                RefItemGuestType.class, tableGuest, guestItems, "", "Tipe Tamu *", true, 160, 200
        );

    //    gpFormDataTools.add(cbpUnit, 2, 7);
        //    gpFormDataTools.add(cbpGuest, 1, 2);
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

    private void setSelectedDataToInputForm() {
    //    txtCodeTools.textProperty().bindBidirectional(selectedData.codeItemProperty());
        //    chbConsumableStatus.selectedProperty().bindBidirectional(selectedData.consumableProperty());
        //    chbConsumableStatus.setDisable(true);
//        cbpGuest.valueProperty().bindBidirectional(selectedData.refItemGuestTypeProperty());
        txtToolsName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        txtToolsBrand.textProperty().bindBidirectional(selectedData.itemBrandProperty());
        Bindings.bindBidirectional(txtToolsCostOfGoodSold.textProperty(), selectedData.itemCostOfGoodsSoldProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtToolsAdditionalCharge.textProperty(), selectedData.additionalChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtToolsBrokenCharge.textProperty(), selectedData.brokenChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtToolsMinStock.textProperty(), selectedData.stockMinimalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        cbpUnit.valueProperty().bindBidirectional(selectedData.tblUnitProperty());
        txtToolsNote.textProperty().bindBidirectional(selectedData.itemNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
    //    txtCodeTools.setDisable(true);
        //    chbConsumableStatus.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataTools,
                inputDataStatus == 3
                //          txtCodeTools,
                //          chbConsumableStatus,
        );

        btnSave.setVisible(inputDataStatus != 3);

        btnCancel.setVisible(inputDataStatus != 3);
    }

    private void dataToolsCreateHandle() {
        inputDataStatus = 0;
        selectedData = new TblItem();
        selectedData.setItemCostOfGoodsSold(new BigDecimal("0"));
        selectedData.setAdditionalCharge(new BigDecimal("0"));
        selectedData.setBrokenCharge(new BigDecimal("0"));
        selectedData.setStockMinimal(new BigDecimal("0"));
        selectedData.setConsumable(false);
        lblTools.setText("");
        setSelectedDataToInputForm();
        dataToolsFormShowStatus.set(0);
        dataToolsFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataToolsUpdateHandle() {
        if (tableDataTools.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = parentController.getFToolsManager().getTools(((TblItem) tableDataTools.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            lblTools.setText(selectedData.getCodeItem() + " - " + selectedData.getItemName());
            setSelectedDataToInputForm();
            inputDataStatus = 1;
            dataToolsFormShowStatus.set(0);
            dataToolsFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataToolsShowHandle() {
        if (tableDataTools.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = parentController.getFToolsManager().getTools(((TblItem) tableDataTools.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            lblTools.setText(selectedData.getCodeItem() + " - " + selectedData.getItemName());
            setSelectedDataToInputForm();
            inputDataStatus = 3;
            dataToolsFormShowStatus.set(1);
            isShowStatus.set(true);
        }
    }

    private void dataToolsUnshowHandle() {
        refreshDataTableTools();
        dataToolsFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataToolsDeleteHandle() {
        if (tableDataTools.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFToolsManager().deleteDataTools((TblItem) tableDataTools.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    refreshDataTableTools();
                    dataToolsFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFToolsManager().getErrMessage(), null);
                }
            } else {
                refreshDataTableTools();
                dataToolsFormShowStatus.set(0);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataToolsSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItem dummySelected = new TblItem(selectedData);
                switch (inputDataStatus) {
                    case 0:
                        if (parentController.getFToolsManager().insertDataTools(dummySelected) != null) {
                            ClassMessage.showSucceedInsertingDataMessage(null, null);
                            refreshDataTableTools();
                            dataToolsFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFToolsManager().getErrMessage(), null);
                        }
                        break;

                    case 1:
                        if (parentController.getFToolsManager().updateDataTools(dummySelected)) {
                            ClassMessage.showSucceedUpdatingDataMessage(null, null);
                            refreshDataTableTools();
                            dataToolsFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        }
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataToolsCancelHandle() {
        refreshDataTableTools();
        dataToolsFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableTools() {
        tableDataTools.getTableView().setItems(loadAllDataTools());
        cft.refreshFilterItems(tableDataTools.getTableView().getItems());
    }

    String errDataInput;

    private boolean checkDataInput() {
        boolean check = true;

        if (txtToolsName.getText() == null || txtToolsName.getText().equals("")) {
            check = false;
            errDataInput += "Nama Peralatan : " + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (txtToolsCostOfGoodSold.getText() == null
                || txtToolsCostOfGoodSold.getText().equals("")
                || txtToolsCostOfGoodSold.getText().equals("-")) {
            check = false;
            errDataInput += "Harga Beli (Terakhir) :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        } else {
            if (selectedData.getItemCostOfGoodsSold().compareTo(new BigDecimal("0")) == -1) {
                check = false;
                errDataInput += "Harga Beli (Terakhir) : Tidak boleh lebih kecil dari '0' \n";
            }
        }

        if (txtToolsAdditionalCharge.getText() == null
                || txtToolsAdditionalCharge.getText().equals("")
                || txtToolsAdditionalCharge.getText().equals("-")) {
            check = false;
            errDataInput += "Harga Jual :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        } else {
            if (selectedData.getAdditionalCharge().compareTo(new BigDecimal("0")) == -1) {
                check = false;
                errDataInput += "Harga Jual : Tidak boleh lebih kecil dari '0' \n";
            }
        }

        if (txtToolsBrokenCharge.getText() == null
                || txtToolsBrokenCharge.getText().equals("")
                || txtToolsBrokenCharge.getText().equals("-")) {
            check = false;
            errDataInput += "Biaya Kerusakan :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        } else {
            if (selectedData.getBrokenCharge().compareTo(new BigDecimal("0")) == -1) {
                check = false;
                errDataInput += "Biaya Kerusakan : Tidak boleh lebih kecil dari '0' \n";
            }
        }

        if (txtToolsMinStock.getText() == null
                || txtToolsMinStock.getText().equals("")
                || txtToolsMinStock.getText().equals("-")) {
            check = false;
            errDataInput += "Stok Minimal :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        } else {
            if (selectedData.getStockMinimal()
                    .compareTo(new BigDecimal("0")) < 1) {
                check = false;
                errDataInput += "Stok Minimal: " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }

        if (cbpUnit.getValue() == null) {
            check = false;
            errDataInput += "Satuan :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (cbpGuest.getValue() == null) {
            check = false;
            errDataInput += "Tipe Tamu :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        return check;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataToolsSplitPane();

        initTableDataTools();

        initFormDataTools();

        spDataTools.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataToolsFormShowStatus.set(0);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ToolsController(FeatureToolsController parentController) {
        this.parentController = parentController;
    }

    private final FeatureToolsController parentController;
}
