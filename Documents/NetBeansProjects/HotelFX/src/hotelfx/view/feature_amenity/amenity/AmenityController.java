    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_amenity.amenity;

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
import hotelfx.view.feature_amenity.FeatureAmenityController;
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
public class AmenityController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataAmenity;

    private DoubleProperty dataAmenityFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataAmenityLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataAmenitySplitpane() {
        spDataAmenity.setDividerPositions(1);

        dataAmenityFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataAmenityFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataAmenity.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataAmenity.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataAmenityFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataAmenityLayout.setDisable(false);
                    tableDataAmenityLayoutDisableLayer.setDisable(true);
                    tableDataAmenityLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataAmenityLayout.setDisable(true);
                    tableDataAmenityLayoutDisableLayer.setDisable(false);
                    tableDataAmenityLayoutDisableLayer.toFront();
                }
            }
        });

        dataAmenityFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataAmenityLayout;

    private ClassFilteringTable<TblItem> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataAmenity;

    private final PseudoClass amenityPseudoClass = PseudoClass.getPseudoClass("amenity");

    private final PseudoClass refreshPseudoClass = PseudoClass.getPseudoClass("refresh");

    private void initTableDataAmenity() {
        //set table
        setTableDataAmenity();
        //set control
        setTableControlDataAmenity();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataAmenity, 15.0);
        AnchorPane.setLeftAnchor(tableDataAmenity, 15.0);
        AnchorPane.setRightAnchor(tableDataAmenity, 15.0);
        AnchorPane.setTopAnchor(tableDataAmenity, 15.0);
        ancBodyLayout.getChildren().add(tableDataAmenity);
    }

    private void setTableDataAmenity() {
        TableView<TblItem> tableView = new TableView();
        TableColumn<TblItem, String> idAmenity = new TableColumn("ID");
        idAmenity.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        idAmenity.setMinWidth(100);
        TableColumn<TblItem, String> amenityName = new TableColumn("Nama Amenities");
        amenityName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        amenityName.setMinWidth(140);
        TableColumn<TblItem, String> amenityBrand = new TableColumn("Merek");
        amenityBrand.setCellValueFactory(cellData -> cellData.getValue().itemBrandProperty());
        amenityBrand.setMinWidth(120);
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

        TableColumn<TblItem, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblUnit().getUnitName(), param.getValue().tblUnitProperty()));
        unitName.setMinWidth(50);

        TableColumn<TblItem, String> amenityNote = new TableColumn("Keterangan");
        amenityNote.setCellValueFactory(cellData -> cellData.getValue().itemNoteProperty());
        amenityNote.setMinWidth(200);

        tableView.getColumns().addAll(idAmenity, amenityName, amenityBrand, statusItem,
                costItem, stockItem, unitName, amenityNote);

        tableView.setItems(loadAllDataAmenity());

        tableView.setRowFactory(tv -> {
            TableRow<TblItem> row = new TableRow<>();

            row.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    row.pseudoClassStateChanged(amenityPseudoClass, getTotalCurrentStock(newVal).compareTo(newVal.getStockMinimal()) == -1);
                } else {
                    row.pseudoClassStateChanged(amenityPseudoClass, false);
                }
            });
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataAmenityUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataAmenityUpdateHandle();
                            } else {
                                dataAmenityShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataAmenityUpdateHandle();
//                            } else {
//                                dataAmenityShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataAmenity = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItem.class,
                tableDataAmenity.getTableView(),
                tableDataAmenity.getTableView().getItems());

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
            List<TblItemLocation> itemLocations = parentController.getFAmenityManager().getAllDataItemLocationByIDItem(dataItem.getIditem());
            for (TblItemLocation itemLocation : itemLocations) {
                if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4) { //Bin = '4'
                    result = result.add(itemLocation.getItemQuantity());
                }
            }
        }
        return result;
    }

    private void setTableControlDataAmenity() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataAmenityCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataAmenityUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataAmenityDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataAmenityPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataAmenity.addButtonControl(buttonControls);
    }

    private ObservableList<TblItem> loadAllDataAmenity() {
        return FXCollections.observableArrayList(parentController.getFAmenityManager().getAllDataAmenity());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataAmenity;

    @FXML
    private ScrollPane spFormDataAmenity;

    @FXML
    private JFXTextField txtCodeAmenity;

    @FXML
    private JFXCheckBox chbConsumableStatus;

    @FXML
    private JFXTextField txtAmenityName;

    @FXML
    private JFXTextField txtAmenityBrand;

    @FXML
    private JFXTextField txtAmenityCostOfGoodsSold;

    @FXML
    private JFXTextField txtAmenityAdditionalCharge;

    @FXML
    private JFXTextField txtAmenityBrokenCharge;

    @FXML
    private JFXTextField txtAmenityMinStock;

    @FXML
    private JFXTextArea txtAmenityNote;

    @FXML
    private Label lblAmenity;

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

    private void initFormDataAmenity() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataAmenity.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataAmenity.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Amenities)"));
        btnSave.setOnAction((e) -> {
            dataAmenitySaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataAmenityCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtAmenityName,
                cbpGuest,
                cbpUnit,
                cbpGuest,
                txtAmenityCostOfGoodsSold,
                txtAmenityAdditionalCharge,
                txtAmenityBrokenCharge,
                txtAmenityMinStock);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAmenityCostOfGoodsSold,
                txtAmenityAdditionalCharge,
                txtAmenityBrokenCharge,
                txtAmenityMinStock);
    }

    private void initDataPopup() {
        //Unit
        TableView<TblUnit> tableUnit = new TableView<>();

        TableColumn<TblUnit, String> unitName = new TableColumn<>("Satuan");
        unitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        unitName.setMinWidth(140);

        tableUnit.getColumns().addAll(unitName);

        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFAmenityManager().getAllDataUnit());

        cbpUnit = new JFXCComboBoxTablePopup<>(
                TblUnit.class, tableUnit, unitItems, "", "Satuan *", true, 160, 200
        );

        //Guest Type
        TableView<RefItemGuestType> tableGuest = new TableView<>();
        TableColumn<RefItemGuestType, String> guestName = new TableColumn<>("Tipe Tamu");
        guestName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        guestName.setMinWidth(140);

        tableGuest.getColumns().addAll(guestName);

        ObservableList<RefItemGuestType> guestItems = FXCollections.observableArrayList(parentController.getFAmenityManager().getAllDataGuest());

        cbpGuest = new JFXCComboBoxTablePopup<>(
                RefItemGuestType.class, tableGuest, guestItems, "", "Tipe Tamu *", true, 160, 200
        );

        //attached to grid-pane
        //  gpFormDataAmenity.add(cbpGuest, 1, 2);
        //  gpFormDataAmenity.add(cbpUnit, 2, 7);
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

    private void refreshDataPopup() {
        //Unit
        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(parentController.getFAmenityManager().getAllDataUnit());
        cbpUnit.setItems(unitItems);

        ObservableList<RefItemGuestType> guestItems = FXCollections.observableArrayList(parentController.getFAmenityManager().getAllDataGuest());
        for (int i = 0; i < guestItems.size(); i++) {
            if (guestItems.get(i).getIdtype() == 1) {
                guestItems.remove(guestItems.get(i));
            }
        }
        cbpGuest.setItems(guestItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        chbConsumableStatus.setDisable(dataInputStatus != 0);

        // txtCodeAmenity.textProperty().bindBidirectional(selectedData.codeItemProperty());
        txtAmenityName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        txtAmenityBrand.textProperty().bindBidirectional(selectedData.itemBrandProperty());
        Bindings.bindBidirectional(txtAmenityCostOfGoodsSold.textProperty(), selectedData.itemCostOfGoodsSoldProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtAmenityAdditionalCharge.textProperty(), selectedData.additionalChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtAmenityBrokenCharge.textProperty(), selectedData.brokenChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtAmenityMinStock.textProperty(), selectedData.stockMinimalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtAmenityNote.textProperty().bindBidirectional(selectedData.itemNoteProperty());

        chbConsumableStatus.selectedProperty().bindBidirectional(selectedData.consumableProperty());

        cbpUnit.valueProperty().bindBidirectional(selectedData.tblUnitProperty());
        cbpUnit.hide();

//        cbpGuest.valueProperty().bindBidirectional(selectedData.refItemGuestTypeProperty());

        cbpGuest.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        // txtCodeAmenity.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataAmenity,
                dataInputStatus == 3,
                //        txtCodeAmenity,
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

    private void dataAmenityCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblItem();
        selectedData.setItemCostOfGoodsSold(new BigDecimal("0"));
        selectedData.setAdditionalCharge(new BigDecimal("0"));
        selectedData.setBrokenCharge(new BigDecimal("0"));
        selectedData.setStockMinimal(new BigDecimal("0"));
        selectedData.setConsumable(false);
        selectedData.setGuestStatus(true);
        lblAmenity.setText("");
        setSelectedDataToInputForm();
        //open form data amenity
        dataAmenityFormShowStatus.set(0.0);
        dataAmenityFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataAmenityUpdateHandle() {
        if (tableDataAmenity.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFAmenityManager().getAmenity(((TblItem) tableDataAmenity.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            lblAmenity.setText(selectedData.getCodeItem() + " - " + selectedData.getItemName());
            setSelectedDataToInputForm();
            //open form data amenity
            dataAmenityFormShowStatus.set(0.0);
            dataAmenityFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataAmenityShowHandle() {
        if (tableDataAmenity.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFAmenityManager().getAmenity(((TblItem) tableDataAmenity.getTableView().getSelectionModel().getSelectedItem()).getIditem());
            lblAmenity.setText(selectedData.getCodeItem() + " - " + selectedData.getItemName());
            setSelectedDataToInputForm();
            dataAmenityFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataAmenityUnshowHandle() {
        refreshDataTableAmenity();
        dataAmenityFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataAmenityDeleteHandle() {
        if (tableDataAmenity.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFAmenityManager().deleteDataAmenity(new TblItem((TblItem) tableDataAmenity.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data amenity
                    refreshDataTableAmenity();
                    dataAmenityFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFAmenityManager().getErrorMessage(), null);

                }
            } else {
                refreshDataTableAmenity();
                dataAmenityFormShowStatus.set(0.0);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataAmenityPrintHandle() {

    }

    private void dataAmenitySaveHandle() {
        if (checkDataInputDataAmenity()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItem dummySelectedData = new TblItem(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFAmenityManager().insertDataAmenity(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data amenity
                            refreshDataTableAmenity();
                            dataAmenityFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFAmenityManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFAmenityManager().updateDataAmenity(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data amenity
                            refreshDataTableAmenity();
                            dataAmenityFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFAmenityManager().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                dataAmenityFormShowStatus.set(1);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataAmenityCancelHandle() {
        //refresh data from table & close form data amenity
        refreshDataTableAmenity();
        dataAmenityFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableAmenity() {
        tableDataAmenity.getTableView().setItems(loadAllDataAmenity());
        cft.refreshFilterItems(tableDataAmenity.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataAmenity() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtAmenityName.getText() == null || txtAmenityName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Amenities : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (cbpGuest.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Tamu :" + ClassMessage.defaultErrorNullValueMessage + "\n";
        }

        if (cbpUnit.getValue() == null) {
            dataInput = false;
            errDataInput += "Satuan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }

        if (cbpGuest.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Tamu : " + ClassMessage.defaultErrorNullValueMessage + " \n";

        }

        if (txtAmenityCostOfGoodsSold.getText() == null
                || txtAmenityCostOfGoodsSold.getText().equals("")
                || txtAmenityCostOfGoodsSold.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Beli (Terakhir) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getItemCostOfGoodsSold().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Beli (Terakhir) : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtAmenityAdditionalCharge.getText() == null
                || txtAmenityAdditionalCharge.getText().equals("")
                || txtAmenityAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Jual : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getAdditionalCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Jual : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtAmenityBrokenCharge.getText() == null
                || txtAmenityBrokenCharge.getText().equals("")
                || txtAmenityBrokenCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Kerusakan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getBrokenCharge().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Kerusakan : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtAmenityMinStock.getText() == null
                || txtAmenityMinStock.getText().equals("")
                || txtAmenityMinStock.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Stok Minimal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        setDataAmenitySplitpane();

        //init table
        initTableDataAmenity();

        //init form
        initFormDataAmenity();

        spDataAmenity.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataAmenityFormShowStatus.set(0.0);
        });
    }

    public AmenityController(FeatureAmenityController parentController) {
        this.parentController = parentController;
    }

    private final FeatureAmenityController parentController;

}
