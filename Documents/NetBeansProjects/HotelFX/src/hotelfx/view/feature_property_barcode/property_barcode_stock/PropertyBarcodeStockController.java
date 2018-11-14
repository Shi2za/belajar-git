/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_property_barcode.property_barcode_stock;

import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_property_barcode.FeaturePropertyBarcodeController;
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

public class PropertyBarcodeStockController implements Initializable{
     
      private ClassFilteringTable<TblItemLocationPropertyBarcode> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;
     
    @FXML
    private AnchorPane tableDataPropertyBarcodeStockLayout;
    
    private TableView tableDataPropertyBarcodeStock;
    
    private void initTableDataPropertyBarcodeStock(){
      setTableDataPropertyBarcodeStock();
      
      ancBodyLayout.getChildren().clear();
      AnchorPane.setTopAnchor(tableDataPropertyBarcodeStock,15.0);
      AnchorPane.setLeftAnchor(tableDataPropertyBarcodeStock,15.0);
      AnchorPane.setBottomAnchor(tableDataPropertyBarcodeStock,15.0);
      AnchorPane.setRightAnchor(tableDataPropertyBarcodeStock,15.0);
      ancBodyLayout.getChildren().add(tableDataPropertyBarcodeStock);
    }
    
    private void setTableDataPropertyBarcodeStock(){
       TableView<TblItemLocationPropertyBarcode>tableView = new TableView();
       TableColumn<TblItemLocationPropertyBarcode,String>itemCode = new TableColumn("ID");
       itemCode.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocationPropertyBarcode,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblItemLocation().getTblItem().getCodeItem(),param.getValue().getTblItemLocation().tblItemProperty()));
       itemCode.setMinWidth(120);
       
       TableColumn<TblItemLocationPropertyBarcode,String>itemName = new TableColumn("Nama Asset");
      itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocationPropertyBarcode,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblItemLocation().getTblItem().getItemName(),param.getValue().getTblItemLocation().getTblItem().itemNameProperty()));
      itemName.setMinWidth(140);
      
      TableColumn<TblItemLocationPropertyBarcode,String>locationName = new TableColumn("Nama");
       locationName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocationPropertyBarcode,String>param)
           ->Bindings.createStringBinding(()->getNameLocation(param.getValue().getTblItemLocation().getTblLocation()),param.getValue().getTblItemLocation().tblLocationProperty()));
       locationName.setMinWidth(140);
       
       TableColumn<TblItemLocationPropertyBarcode,String>locationType = new TableColumn("Tipe");
        locationType.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocationPropertyBarcode,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblItemLocation().getTblLocation().getRefLocationType().getTypeName(),param.getValue().getTblItemLocation().getTblLocation().getRefLocationType().typeNameProperty()));
       locationType.setMinWidth(140);
        
        TableColumn<TblItemLocationPropertyBarcode,String>locationItem = new TableColumn("Lokasi");
        locationItem.getColumns().addAll(locationType,locationName);
        
        TableColumn<TblItemLocationPropertyBarcode,String>locationBarcode = new TableColumn("Barcode");
       locationBarcode.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocationPropertyBarcode,String>param)
           ->Bindings.createStringBinding(()->param.getValue().getTblPropertyBarcode().getCodeBarcode(),param.getValue().getTblPropertyBarcode().codeBarcodeProperty()));
       locationBarcode.setMinWidth(120);
       
       tableView.getColumns().addAll(itemCode,itemName,locationBarcode,locationItem);
       tableView.setItems(loadAllDataPropertyBarcodeStock());
       tableDataPropertyBarcodeStock = tableView;
       
       //set filter
        cft = new ClassFilteringTable<>(
                TblItemLocationPropertyBarcode.class,
                tableDataPropertyBarcodeStock,
                tableDataPropertyBarcodeStock.getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }
    
    public ObservableList<TblItemLocationPropertyBarcode>loadAllDataPropertyBarcodeStock(){
      return FXCollections.observableArrayList(parentController.getFPropertyBarcodeManager().getAllDataPropertyBarcodeStock());
    }
    
     public String getNameLocation(TblLocation location){
     //System.out.println("Hsl>>"+location.getRefLocationType().getIdtype());
      
      String name = "";
      
      switch(location.getRefLocationType().getIdtype())
      {
          case 0:
          List<TblRoom> room = parentController.getFPropertyBarcodeManager().getRoomByIdLocation(location.getIdlocation());
          for(TblRoom getRoom : room){
            name = getRoom.getRoomName();
          }
          break;
          case 1:
          List<TblLocationOfWarehouse> warehouse = parentController.getFPropertyBarcodeManager().getWarehouseByIdLocation(location.getIdlocation());
          for(TblLocationOfWarehouse getWarehouse : warehouse){
             name = getWarehouse.getWarehouseName();
          }
          break;
          case 2:
          List<TblLocationOfLaundry> laundry = parentController.getFPropertyBarcodeManager().getLaundryByIdLocation(location.getIdlocation());
          for(TblLocationOfLaundry getLaundry : laundry){
             name = getLaundry.getLaundryName();
          }
          break;
          case 3:
          List<TblSupplier> supplier = parentController.getFPropertyBarcodeManager().getSupplierByIdLocation(location.getIdlocation());
          for(TblSupplier getSupplier : supplier){
             name = getSupplier.getSupplierName();
          }
          break;
          case 4:
          List<TblLocationOfBin> bin = parentController.getFPropertyBarcodeManager().getBinByIdLocation(location.getIdlocation());
          for(TblLocationOfBin getBin : bin){
             name = getBin.getBinName();
          }
          break;
      }
      return name;
    }
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableDataPropertyBarcodeStock();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public PropertyBarcodeStockController(FeaturePropertyBarcodeController parentController){
       this.parentController = parentController;
    }
    
    private final FeaturePropertyBarcodeController parentController;
}
