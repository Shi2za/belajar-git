/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_item_and_asset_md.item_stock;

import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_item_and_asset_md.FeatureItemAndAssetMDController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ItemStockController implements Initializable {

    private ClassFilteringTable<TblItemLocation> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    @FXML
    private AnchorPane tableDataItemStockLayout;

    private TableView tableDataItemStock;

    private void initTableDataItemStock() {
        setTableDataItemStock();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataItemStock, 15.0);
        AnchorPane.setLeftAnchor(tableDataItemStock, 15.0);
        AnchorPane.setBottomAnchor(tableDataItemStock, 15.0);
        AnchorPane.setRightAnchor(tableDataItemStock, 15.0);
        ancBodyLayout.getChildren().add(tableDataItemStock);
    }

    private void setTableDataItemStock() {
        TableView<TblItemLocation> tableView = new TableView();

        TableColumn<TblItemLocation, String> itemCode = new TableColumn("ID");
        itemCode.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(),
                        param.getValue().tblItemProperty()));
        itemCode.setMinWidth(120);

        TableColumn<TblItemLocation, String> itemNameAndBrand = new TableColumn("Nama/Merek");
        itemNameAndBrand.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName() + "\nMerek : "
                        + (param.getValue().getTblItem().getItemBrand() != null ? param.getValue().getTblItem().getItemBrand() : "-"),
                        param.getValue().tblItemProperty()));
        itemNameAndBrand.setMinWidth(140);

        TableColumn<TblItemLocation, String> itemType = new TableColumn("Tipe Barang");
        itemType.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> "HK : "
                        + (param.getValue().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + "\nG   : "
                        + (param.getValue().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-"),
                        param.getValue().tblItemProperty()));
        itemType.setMinWidth(145);

        TableColumn<TblItemLocation, String> locationName = new TableColumn("Nama");
        locationName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> getNameLocation(param.getValue().getTblLocation()), param.getValue().tblLocationProperty()));
        locationName.setMinWidth(140);
        TableColumn<TblItemLocation, String> locationType = new TableColumn("Tipe");
        locationType.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getRefLocationType().getTypeName(), param.getValue().getTblLocation().getRefLocationType().typeNameProperty()));
        locationType.setMinWidth(140);
        TableColumn<TblItemLocation, String> locationItem = new TableColumn("Lokasi");
        locationItem.getColumns().addAll(locationType, locationName);
        locationItem.setMinWidth(280);

        TableColumn<TblItemLocation, String> itemQuantity = new TableColumn("Stok");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()),
                        param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblItemLocation, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(120);

        TableColumn<TblItemLocation, String> itemNote = new TableColumn("Keterangan");
        itemNote.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
        itemNote.setMinWidth(180);

        tableView.getColumns().addAll(itemCode, itemNameAndBrand, itemType, locationItem, itemQuantity, itemUnit, itemNote);
        tableView.setItems(loadAllDataItemStock());
        tableDataItemStock = tableView;

        //set filter
        cft = new ClassFilteringTable<>(
                TblItemLocation.class,
                tableDataItemStock,
                tableDataItemStock.getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public ObservableList<TblItemLocation> loadAllDataItemStock() {
        List<TblItemLocation> list = parentController.getFItemAndAssetManager().getAllDataItemLocation();
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
                TblRoom room = parentController.getFItemAndAssetManager().getDataRoomByIDLocation(location.getIdlocation());
                if (room != null) {
                    name = room.getRoomName();
                }
                break;
            case 1:
                TblLocationOfWarehouse warehouse = parentController.getFItemAndAssetManager().getDataLocationOfWarehouseByIDLocation(location.getIdlocation());
                if (warehouse != null) {
                    name = warehouse.getWarehouseName();
                }
                break;
            case 2:
                TblLocationOfLaundry laundry = parentController.getFItemAndAssetManager().getDataLocationOfLaundryByIDLocation(location.getIdlocation());
                if (laundry != null) {
                    name = laundry.getLaundryName();
                }
                break;
            case 3:
                TblSupplier supplier = parentController.getFItemAndAssetManager().getDataSupplierByIDLocation(location.getIdlocation());
                if (supplier != null) {
                    name = supplier.getSupplierName();
                }
                break;
            case 4:
                TblLocationOfBin bin = parentController.getFItemAndAssetManager().getDataLocationOfBinByIDLocation(location.getIdlocation());
                if (bin != null) {
                    name = bin.getBinName();
                }
                break;
        }
        return name;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableDataItemStock();
    }

    public ItemStockController(FeatureItemAndAssetMDController parentController) {
        this.parentController = parentController;
    }

    private final FeatureItemAndAssetMDController parentController;

}
