/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_food_and_beverage.food_and_beverage_stock;

import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_food_and_beverage.FeatureFoodAndBeverageController;
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
public class FoodAndBeverageStockController implements Initializable {
   
   /* @FXML
    private SplitPane spDataFoodAndBeverageStock;
    DoubleProperty dataFoodAndBeverageStockFormShowStatus;
    
    @FXML
    private AnchorPane tableDataFoodAndBeverageStockLayoutDisableLayer;
    
    @FXML
    private AnchorPane contentLayout; */
    
   /* private void dataFoodAndBeverageSplitPane(){
      dataFoodAndBeverageStockFormShowStatus = new SimpleDoubleProperty(1.0);
      
      DoubleProperty divPosition = new SimpleDoubleProperty();
      
      divPosition.bind(new SimpleDoubleProperty(1.0).subtract((formAnchorFoodAndBeverageStock.prefWidthProperty().divide(spDataFoodAndBeverageStock.widthProperty()))
              .multiply(dataFoodAndBeverageStockFormShowStatus)));
      
      divPosition.addListener((obs,oldVal,newVal)->{
        spDataFoodAndBeverageStock.setDividerPositions(newVal.doubleValue());
      });
      
      SplitPane.Divider div = spDataFoodAndBeverageStock.getDividers().get(0);
      
      div.positionProperty().addListener((obs,oldVal,newVal)->{
        div.setPosition(divPosition.get());
      });
      
      dataFoodAndBeverageStockFormShowStatus.addListener((obs,oldVal,newVal)->{
         if(newVal.doubleValue()==0)
         {
           tableDataFoodAndBeverageStockLayout.setDisable(false);
           tableDataFoodAndBeverageStockLayoutDisableLayer.setDisable(true);
           tableDataFoodAndBeverageStockLayout.toFront();
         }
         
         if(newVal.doubleValue()==1)
         {
           tableDataFoodAndBeverageStockLayout.setDisable(true);
           tableDataFoodAndBeverageStockLayoutDisableLayer.setDisable(false);
           tableDataFoodAndBeverageStockLayoutDisableLayer.toFront();
         }
         
      });
      
      dataFoodAndBeverageStockFormShowStatus.set(0);
    }*/
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @FXML
    private AnchorPane tableDataFoodAndBeverageStockLayout;
    
    private ClassFilteringTable<TblItemLocation> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;
    
    private TableView tableDataFoodAndBeverageStock;
    
    //private ClassTableWithControl tableDataFoodAndBeverageStock;
    
    private void initTableDataFoodAndBeverageStock(){
      setTableDataFoodAndBeverageStock();
      ancBodyLayout.getChildren().clear();
      AnchorPane.setTopAnchor(tableDataFoodAndBeverageStock,15.0);
      AnchorPane.setLeftAnchor(tableDataFoodAndBeverageStock,15.0);
      AnchorPane.setBottomAnchor(tableDataFoodAndBeverageStock,15.0);
      AnchorPane.setRightAnchor(tableDataFoodAndBeverageStock,15.0);
      
      ancBodyLayout.getChildren().add(tableDataFoodAndBeverageStock);
    }
    
    private void setTableDataFoodAndBeverageStock(){
       TableView<TblItemLocation>tableView = new TableView();
       
       TableColumn<TblItemLocation,String>itemCode = new TableColumn("ID");
       itemCode.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblItem().getCodeItem(),param.getValue().tblItemProperty()));
       itemCode.setMinWidth(120);
       
       TableColumn<TblItemLocation,String>itemName = new TableColumn("Nama Food & Beverage");
       itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblItem().getItemName(),param.getValue().tblItemProperty()));
       itemName.setMinWidth(200);
       
       TableColumn<TblItemLocation,String>locationName = new TableColumn("Nama");
       locationName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
           ->Bindings.createStringBinding(()->getNameLocation(param.getValue().getTblLocation()),param.getValue().tblLocationProperty()));
       locationName.setMinWidth(140);
       
       TableColumn<TblItemLocation,String>locationType = new TableColumn("Tipe");
          locationType.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
          ->Bindings.createStringBinding(()->param.getValue().getTblLocation().getRefLocationType().getTypeName(),param.getValue().getTblLocation().getRefLocationType().typeNameProperty()));
          locationType.setMinWidth(140);
       
        TableColumn<TblItemLocation,String>locationItem = new TableColumn("Lokasi");
        locationItem.getColumns().addAll(locationType,locationName);
        locationItem.setMinWidth(280);
       
       TableColumn<TblItemLocation,String>itemQuantity = new TableColumn("Stok");
       itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
            ->Bindings.createStringBinding(()-> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()),param.getValue().itemQuantityProperty()));
       itemQuantity.setMinWidth(120);
       
       TableColumn<TblItemLocation,String> itemUnit = new TableColumn("Satuan");
       itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblItemLocation,String>param)
            ->Bindings.createStringBinding(()-> 
                    param.getValue().getTblItem().getTblUnit().getUnitName(),
                    param.getValue().getTblItem().tblUnitProperty()));
       itemUnit.setMinWidth(120);
       
       TableColumn<TblItemLocation,String>itemNote = new TableColumn("Keterangan");
      /* itemNote.setCellFactory(new Callback<TableColumn<TblItemLocation,Boolean>,TableCell<TblItemLocation,Boolean>>() {
           @Override
           public TableCell<TblItemLocation, Boolean> call(TableColumn<TblItemLocation, Boolean> param) {
               CheckBoxTableCell<TblItemLocation,Boolean>cell = new CheckBoxTableCell<TblItemLocation,Boolean>(); 
               cell.setAlignment(Pos.CENTER);
               return cell;
               //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
           }
       });*/
       itemNote.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
       itemNote.setMinWidth(200);
       
       tableView.getColumns().addAll(itemCode,itemName,locationItem,itemQuantity,itemUnit,itemNote);
       tableView.setItems(loadAllDataFoodAndBeverageStock());
       tableDataFoodAndBeverageStock = tableView;
       
       //set filter
        cft = new ClassFilteringTable<>(
                TblItemLocation.class,
                tableDataFoodAndBeverageStock,
                tableDataFoodAndBeverageStock.getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }
    
    private ObservableList<TblItemLocation>loadAllDataFoodAndBeverageStock(){
        List<TblItemLocation> list = parentController.getFFoodAndBeverageManager().getAllDataFoodAndBeverageStock();
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
      
      String name = "";
         
      switch(location.getRefLocationType().getIdtype())
      {
          case 0:
          List<TblRoom> room = parentController.getFFoodAndBeverageManager().getRoomByIdLocation(location.getIdlocation());
          for(TblRoom getRoom : room){
            name = getRoom.getRoomName();
          }
          break;
          case 1:
          List<TblLocationOfWarehouse> warehouse = parentController.getFFoodAndBeverageManager().getWarehouseByIdLocation(location.getIdlocation());
          for(TblLocationOfWarehouse getWarehouse : warehouse){
             name = getWarehouse.getWarehouseName();
          }
          break;
          case 2:
          List<TblLocationOfLaundry> laundry = parentController.getFFoodAndBeverageManager().getLaundryByIdLocation(location.getIdlocation());
          for(TblLocationOfLaundry getLaundry : laundry){
             name = getLaundry.getLaundryName();
          }
          break;
          case 3:
          List<TblSupplier> supplier = parentController.getFFoodAndBeverageManager().getSupplierByIdLocation(location.getIdlocation());
          for(TblSupplier getSupplier : supplier){
             name = getSupplier.getSupplierName();
          }
          break;
          case 4:
          List<TblLocationOfBin> bin = parentController.getFFoodAndBeverageManager().getBinByIdLocation(location.getIdlocation());
          for(TblLocationOfBin getBin : bin){
             name = getBin.getBinName();
          }
          break;
      }
      return name;
    }
    @FXML
    private AnchorPane formAnchorFoodAndBeverageStock;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableDataFoodAndBeverageStock();
       /* spDataFoodAndBeverageStock.widthProperty().addListener((obs,oldVal,newVal)->{
           dataFoodAndBeverageStockFormShowStatus.set(0.0);
        });*/
//        //set splitpane
//        setDataFoodAndBeverageStockSplitpane();
//
//        //init table
//        initTableDataFoodAndBeverageStock();
//
//        //init form
//        initFormDataFoodAndBeverageStock();
//
//        spDataFoodAndBeverageStock.widthProperty().addListener((obs, oldVal, newVal) -> {
//            dataFoodAndBeverageStockFormShowStatus.set(0.0);
//        });
    }    
    
    public FoodAndBeverageStockController(FeatureFoodAndBeverageController parentController) {
        this.parentController = parentController;
    }

    private final FeatureFoodAndBeverageController parentController;

//    private static class CallbackImpl implements Callback<TableColumn<TblItemLocation, Boolean>, TableCell<TblItemLocation, Boolean>> {
//
//        public CallbackImpl() {
//        }
//
//        @Override
//        public TableCell<TblItemLocation, Boolean> call(TableColumn<TblItemLocation, Boolean> param) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        }
//    }
    
}
