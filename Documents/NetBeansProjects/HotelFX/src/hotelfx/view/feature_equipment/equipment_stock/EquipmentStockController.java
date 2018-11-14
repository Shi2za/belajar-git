/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_equipment.equipment_stock;

import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_equipment.FeatureEquipmentController;
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
 *
 * @author Andreas
 */
public class EquipmentStockController implements Initializable{
    
    @FXML
    private AnchorPane tableDataEquipmentStockLayout;
    
    private ClassFilteringTable<TblItemLocation> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;
    
    private TableView tableDataEquipmentStock;
    
    private void initTableDataEquipmentStock()
    {
      setTableDataEquipmentStock();
      
      ancBodyLayout.getChildren().clear();
      AnchorPane.setTopAnchor(tableDataEquipmentStock,15.0);
      AnchorPane.setLeftAnchor(tableDataEquipmentStock,15.0);
      AnchorPane.setBottomAnchor(tableDataEquipmentStock,15.0);
      AnchorPane.setRightAnchor(tableDataEquipmentStock,15.0);
      ancBodyLayout.getChildren().add(tableDataEquipmentStock);
    }
    
    private void setTableDataEquipmentStock(){
      TableView<TblItemLocation>tableView = new TableView();
      
      TableColumn<TblItemLocation,String>itemCode = new TableColumn("ID");
       itemCode.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblItem().getCodeItem(),param.getValue().tblItemProperty()));
       itemCode.setMinWidth(100);
      
       TableColumn<TblItemLocation,String>itemName = new TableColumn("Nama Perlengkapan");
      itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
        ->Bindings.createStringBinding(()-> param.getValue().getTblItem().getItemName(),param.getValue().tblItemProperty()));
      itemName.setMinWidth(150);
      
      TableColumn<TblItemLocation,String>locationName = new TableColumn("Nama");
      locationName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
        ->Bindings.createStringBinding(()->getNameLocation(param.getValue().getTblLocation()),param.getValue().tblLocationProperty()));
      locationName.setMinWidth(140);
      
       TableColumn<TblItemLocation,String>locationType = new TableColumn("Tipe");
        locationType.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
        ->Bindings.createStringBinding(()-> param.getValue().getTblLocation().getRefLocationType().getTypeName(),param.getValue().getTblLocation().getRefLocationType().typeNameProperty()));
        locationType.setMinWidth(140);
        
        TableColumn<TblItemLocation,String>locationItem = new TableColumn("Lokasi");
        locationItem.getColumns().addAll(locationType,locationName);
      
        TableColumn<TblItemLocation,String>itemQuantity = new TableColumn("Stok");
      itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
        ->Bindings.createStringBinding(()->ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()),param.getValue().itemQuantityProperty()));
      itemQuantity.setMinWidth(120);
      
      TableColumn<TblItemLocation,String> itemUnit = new TableColumn("Satuan");
       itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
            ->Bindings.createStringBinding(()-> 
                    param.getValue().getTblItem().getTblUnit().getUnitName(),
                    param.getValue().getTblItem().tblUnitProperty()));
       itemUnit.setMinWidth(120);
      
      TableColumn<TblItemLocation,String>note = new TableColumn("Keterangan");
      note.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
      note.setMinWidth(180);
      
      tableView.getColumns().addAll(itemCode,itemName,locationItem,itemQuantity,itemUnit,note);
      tableView.setItems(loadAllDataEquipmentStock());
      tableDataEquipmentStock = tableView;
      
      //set filter
        cft = new ClassFilteringTable<>(
                TblItemLocation.class,
                tableDataEquipmentStock,
                tableDataEquipmentStock.getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }
    
    private ObservableList<TblItemLocation>loadAllDataEquipmentStock(){
        List<TblItemLocation> list = parentController.getFEquipmentManager().getAllDataEquipmentStock();
        //remove data with zero value
        for(int i=list.size()-1; i>-1; i--){
            if(list.get(i).getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1){
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }
    
    public String getNameLocation(TblLocation location){
     //System.out.println("Hsl>>"+location.getRefLocationType().getIdtype());
      
      String name = "";
      
      switch(location.getRefLocationType().getIdtype())
      {
          case 0:
          List<TblRoom> room = parentController.getFEquipmentManager().getRoomByIdLocation(location.getIdlocation());
          for(TblRoom getRoom : room){
            name = getRoom.getRoomName();
          }
          break;
          case 1:
          List<TblLocationOfWarehouse> warehouse = parentController.getFEquipmentManager().getWarehouseByIdLocation(location.getIdlocation());
          for(TblLocationOfWarehouse getWarehouse : warehouse){
             name = getWarehouse.getWarehouseName();
          }
          break;
          case 2:
          List<TblLocationOfLaundry> laundry = parentController.getFEquipmentManager().getLaundryByIdLocation(location.getIdlocation());
          for(TblLocationOfLaundry getLaundry : laundry){
             name = getLaundry.getLaundryName();
          }
          break;
          case 3:
          List<TblSupplier> supplier = parentController.getFEquipmentManager().getSupplierByIdLocation(location.getIdlocation());
          for(TblSupplier getSupplier : supplier){
             name = getSupplier.getSupplierName();
          }
          break;
          case 4:
          List<TblLocationOfBin> bin = parentController.getFEquipmentManager().getBinByIdLocation(location.getIdlocation());
          for(TblLocationOfBin getBin : bin){
             name = getBin.getBinName();
          }
          break;
      }
      return name;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    initTableDataEquipmentStock();    
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public EquipmentStockController(FeatureEquipmentController parentController)
    {
      this.parentController = parentController;
    }
    
    private final FeatureEquipmentController parentController;
}
