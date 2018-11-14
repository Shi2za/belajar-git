/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_card_x;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.persistence.service.FRoomCardManager;
import hotelfx.persistence.service.FRoomCardManagerImpl;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RoomCardController implements Initializable {

    @FXML
    private AnchorPane ancFormRoomCard;

    @FXML
    private GridPane gpFormDataRoomCard;

    @FXML
    private JFXTextField txtCodeRoomCard;

    @FXML
    private JFXTextField txtRoomCardName;

    @FXML
    private JFXTextField txtRoomCardCostOfGoodsSold;

    @FXML
    private JFXTextField txtRoomCardAdditionalCharge;

    @FXML
    private JFXTextField txtRoomCardBrokenCharge;

    @FXML
    private JFXTextField txtRoomCardMinStock;

    @FXML
    private JFXTextArea txtRoomCardNote;

    private JFXCComboBoxTablePopup<TblUnit> cbpUnit;

    @FXML
    private JFXButton btnSave;

    private void initFormDataRoomCard() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan Perubahan Data 'Kartu Kamar'"));
        btnSave.setOnAction((e) -> {
            dataRoomCardSaveHandle();
        });

        initNumbericField();
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtRoomCardCostOfGoodsSold,
                txtRoomCardAdditionalCharge,
                txtRoomCardBrokenCharge,
                txtRoomCardMinStock);
    }
    
    private void initDataPopup() {
        //Unit
        TableView<TblUnit> tableUnit = new TableView<>();

        TableColumn<TblUnit, String> unitName = new TableColumn<>("Satuan");
        unitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        unitName.setMinWidth(140);

        tableUnit.getColumns().addAll(unitName);

        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(loadAllDataUnit());

        cbpUnit = new JFXCComboBoxTablePopup<>(
                TblUnit.class, tableUnit, unitItems, "", "Satuan *", true, 200, 200
        );

        //attached to grid-pane
        gpFormDataRoomCard.add(cbpUnit, 3, 6, 2, 1);
    }

    private List<TblUnit> loadAllDataUnit() {
        List<TblUnit> list = fRoomCardManager.getAllDataUnit();
        return list;
    }

    private void refreshDataPopup() {
        //Unit
        ObservableList<TblUnit> unitItems = FXCollections.observableArrayList(loadAllDataUnit());
        cbpUnit.setItems(unitItems);
    }

    private TblItem selectedData;

    private TblItem loadDataRoomCard() {
        return fRoomCardManager.getDataRoomCard();
    }

    private void setSelectedDataToInputForm() {

        selectedData = loadDataRoomCard();

        refreshDataPopup();

        txtCodeRoomCard.textProperty().bindBidirectional(selectedData.codeItemProperty());
        txtRoomCardName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        Bindings.bindBidirectional(txtRoomCardCostOfGoodsSold.textProperty(), selectedData.itemCostOfGoodsSoldProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtRoomCardAdditionalCharge.textProperty(), selectedData.additionalChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtRoomCardBrokenCharge.textProperty(), selectedData.brokenChargeProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtRoomCardMinStock.textProperty(), selectedData.stockMinimalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtRoomCardNote.textProperty().bindBidirectional(selectedData.itemNoteProperty());

        cbpUnit.valueProperty().bindBidirectional(selectedData.tblUnitProperty());

        cbpUnit.hide();

        initTableDataRoomCardStock();
        lblRoomCardTotalCurrentStock.setText("Total Stok Sekarang : " + ClassFormatter.decimalFormat.cFormat(getTotalCurrentStock(selectedData)));

    }

    private BigDecimal getTotalCurrentStock(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null) {
            List<TblItemLocation> itemLocations = fRoomCardManager.getAllDataItemLocationByIDItem(dataItem.getIditem());
            for (TblItemLocation itemLocation : itemLocations) {
                if (itemLocation.getTblLocation().getRefLocationType().getIdtype() != 4) { //Bin = '4'
                    result = result.add(itemLocation.getItemQuantity());
                }
            }
        }
        return result;
    }

    private void dataRoomCardSaveHandle() {
        if (checkDataInputDataRoomCard()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItem dummySelectedData = new TblItem(selectedData);
                if (fRoomCardManager.updateDataRoomCard(dummySelectedData)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh data input
                    setSelectedDataToInputForm();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(fRoomCardManager.getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private String errDataInput;

    private boolean checkDataInputDataRoomCard() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtRoomCardName.getText() == null || txtRoomCardName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Kartu : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpUnit.getValue() == null) {
            dataInput = false;
            errDataInput += "Satuan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomCardCostOfGoodsSold.getText() == null 
                || txtRoomCardCostOfGoodsSold.getText().equals("")
                || txtRoomCardCostOfGoodsSold.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Beli (Terakhir) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomCardAdditionalCharge.getText() == null 
                || txtRoomCardAdditionalCharge.getText().equals("")
                || txtRoomCardAdditionalCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Tambahan (Kartu) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomCardBrokenCharge.getText() == null 
                || txtRoomCardBrokenCharge.getText().equals("")
                || txtRoomCardBrokenCharge.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Kerusakan (Kartu) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomCardMinStock.getText() == null 
                || txtRoomCardMinStock.getText().equals("")
                || txtRoomCardMinStock.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Minimal Stok: " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getStockMinimal()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Minimal Stok: " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        return dataInput;
    }

    /**
     * ROOM CARD - STOCK
     */
    @FXML
    private Label lblRoomCardTotalCurrentStock;

    @FXML
    private AnchorPane ancTableRoomCardStockLayout;

    @FXML
    private TableView tableDataRoomCardStock;

    private void initTableDataRoomCardStock() {
        setTableDataFoodAndBeverageStock();
        ancTableRoomCardStockLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataRoomCardStock, 15.0);
        AnchorPane.setLeftAnchor(tableDataRoomCardStock, 15.0);
        AnchorPane.setBottomAnchor(tableDataRoomCardStock, 15.0);
        AnchorPane.setRightAnchor(tableDataRoomCardStock, 15.0);

        ancTableRoomCardStockLayout.getChildren().add(tableDataRoomCardStock);
    }

    private void setTableDataFoodAndBeverageStock() {
        tableDataRoomCardStock = new TableView();

        TableColumn<TblItemLocation, String> itemCode = new TableColumn("ID Kartu");
        itemCode.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
        itemCode.setMinWidth(120);

        TableColumn<TblItemLocation, String> itemName = new TableColumn("Kartu");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblItemLocation, String> locationName = new TableColumn("Lokasi");
        locationName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> getNameLocation(param.getValue().getTblLocation()), param.getValue().tblLocationProperty()));
        locationName.setMinWidth(140);

        TableColumn<TblItemLocation, String> locationType = new TableColumn("Tipe Lokasi");
        locationType.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getRefLocationType().getTypeName(), param.getValue().getTblLocation().getRefLocationType().typeNameProperty()));
        locationType.setMinWidth(140);

        TableColumn<TblItemLocation, String> itemQuantity = new TableColumn("Stok");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(140);

        TableColumn<TblItemLocation, String> itemNote = new TableColumn("Keterangan");
        itemNote.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
        itemNote.setMinWidth(200);

        tableDataRoomCardStock.getColumns().addAll(itemCode, itemName, locationName, locationType, itemQuantity, itemNote);
        tableDataRoomCardStock.setItems(loadAllDataRoomCardStock());
    }

    private ObservableList<TblItemLocation> loadAllDataRoomCardStock() {
        List<TblItemLocation> list = fRoomCardManager.getAllDataRoomCardStock();
        //remove data with zero value
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    public String getNameLocation(TblLocation location) {

        String name = "";

        switch (location.getRefLocationType().getIdtype()) {
            case 0:
                List<TblRoom> room = fRoomCardManager.getRoomByIdLocation(location.getIdlocation());
                for (TblRoom getRoom : room) {
                    name = getRoom.getRoomName();
                }
                break;
            case 1:
                List<TblLocationOfWarehouse> warehouse = fRoomCardManager.getWarehouseByIdLocation(location.getIdlocation());
                for (TblLocationOfWarehouse getWarehouse : warehouse) {
                    name = getWarehouse.getWarehouseName();
                }
                break;
            case 2:
                List<TblLocationOfLaundry> laundry = fRoomCardManager.getLaundryByIdLocation(location.getIdlocation());
                for (TblLocationOfLaundry getLaundry : laundry) {
                    name = getLaundry.getLaundryName();
                }
                break;
            case 3:
                List<TblSupplier> supplier = fRoomCardManager.getSupplierByIdLocation(location.getIdlocation());
                for (TblSupplier getSupplier : supplier) {
                    name = getSupplier.getSupplierName();
                }
                break;
            case 4:
                List<TblLocationOfBin> bin = fRoomCardManager.getBinByIdLocation(location.getIdlocation());
                for (TblLocationOfBin getBin : bin) {
                    name = getBin.getBinName();
                }
                break;
        }
        return name;
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FRoomCardManager fRoomCardManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fRoomCardManager = new FRoomCardManagerImpl();
        //init form input
        initFormDataRoomCard();
        //refresh data form input
        setSelectedDataToInputForm();
    }

}
