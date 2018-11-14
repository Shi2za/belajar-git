/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse.warehouse_mutation_history;

import hotelfx.helper.ClassDataMutation;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.view.feature_warehouse.FeatureWarehouseController;
import java.net.URL;
import java.util.ArrayList;
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
public class WarehouseMutationHistoryController implements Initializable {
    
    @FXML
    private AnchorPane tableDataWarehouseMutationHistoryLayout;
    
    private ClassFilteringTable<ClassDataMutation> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private TableView tableDataWarehouseMutationHistory;
    
    private void initTableDataWarehouseMutationHistory() {
        setTableDataWarehouseMutationHistory();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataWarehouseMutationHistory, 15.0);
        AnchorPane.setLeftAnchor(tableDataWarehouseMutationHistory, 15.0);
        AnchorPane.setBottomAnchor(tableDataWarehouseMutationHistory, 15.0);
        AnchorPane.setRightAnchor(tableDataWarehouseMutationHistory, 15.0);
        ancBodyLayout.getChildren().add(tableDataWarehouseMutationHistory);
    }

    private void setTableDataWarehouseMutationHistory() {
        TableView<ClassDataMutation> tableView = new TableView();

        TableColumn<ClassDataMutation, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getCodeItem(), 
                        param.getValue().getMutationHistory().getTblItem().codeItemProperty()));
        codeItem.setMinWidth(100);
        
        TableColumn<ClassDataMutation, String> itemName = new TableColumn("Nama");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getItemName(), 
                        param.getValue().getMutationHistory().getTblItem().itemNameProperty()));
        itemName.setMinWidth(120);
        
        TableColumn<ClassDataMutation, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getMutationHistory().getItemQuantity()), param.getValue().getMutationHistory().itemQuantityProperty()));
        itemQuantity.setMinWidth(80);
        
        TableColumn<ClassDataMutation, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getTblUnit().getUnitName(), 
                        param.getValue().getMutationHistory().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(100);
        
        TableColumn<ClassDataMutation, String> codeProperty = new TableColumn("Barcode");
        codeProperty.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> getCodeProperty(param.getValue()), 
                        param.getValue().mutationHistoryPropertyBarcodeProperty()));
        codeProperty.setMinWidth(100);

        TableColumn<ClassDataMutation, String> expiredDate = new TableColumn("Tgl. Kadarluarsa");
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> getExpiredDate(param.getValue()), 
                        param.getValue().mutationHistoryPropertyBarcodeProperty()));
        expiredDate.setMinWidth(120);
        
        TableColumn<ClassDataMutation, String> typeItemHK = new TableColumn("House Keeping");
        typeItemHK.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getTblItemTypeHk() != null 
                        ? param.getValue().getMutationHistory().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-", 
                        param.getValue().getMutationHistory().getTblItem().tblItemTypeHkProperty()));
        typeItemHK.setMinWidth(120);
        
        TableColumn<ClassDataMutation, String> typeItemWH = new TableColumn("Warehouse");
        typeItemWH.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getTblItemTypeWh() != null 
                        ? param.getValue().getMutationHistory().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-", 
                        param.getValue().getMutationHistory().getTblItem().tblItemTypeWhProperty()));
        typeItemWH.setMinWidth(120);
        
        TableColumn<ClassDataMutation,String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(typeItemHK, typeItemWH);
        
        TableColumn<ClassDataMutation,String>itemGroup = new TableColumn("Barang");
        itemGroup.getColumns().addAll(codeItem, itemName,codeProperty, expiredDate);
        
        TableColumn<ClassDataMutation, String> locationSourceType = new TableColumn("Tipe");
        locationSourceType.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().getRefLocationType().getTypeName(), param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().getRefLocationType().typeNameProperty()));
        locationSourceType.setMinWidth(90);
        
        TableColumn<ClassDataMutation, String> nameLocationSource = new TableColumn("Sumber");
        nameLocationSource.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> getNameLocation(param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource()), param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().locationNameProperty()));
        nameLocationSource.setMinWidth(140);
        
//        TableColumn<ClassDataMutation,String>locationSource = new TableColumn("Lokasi (Sumber)");
//        locationSource.getColumns().addAll(locationSourceType,nameLocationSource);
        
        TableColumn<ClassDataMutation, String> locationDestinationType = new TableColumn("Tipe");
        locationDestinationType.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblLocationByIdlocationOfDestination().getRefLocationType().getTypeName(), param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().getRefLocationType().typeNameProperty()));
        locationDestinationType.setMinWidth(90);
        
        TableColumn<ClassDataMutation, String> nameLocationDestination = new TableColumn("Tujuan");
        nameLocationDestination.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> getNameLocation(param.getValue().getMutationHistory().getTblLocationByIdlocationOfDestination()), param.getValue().getMutationHistory().tblLocationByIdlocationOfDestinationProperty()));
        nameLocationDestination.setMinWidth(140);
        
//        TableColumn<ClassDataMutation,String>locationDestination = new TableColumn("Lokasi (Tujuan)");
//        locationDestination.getColumns().addAll(locationDestinationType,nameLocationDestination);

        TableColumn<ClassDataMutation,String>locationTitle = new TableColumn("Lokasi");
        locationTitle.getColumns().addAll(nameLocationSource, nameLocationDestination);
        
        
        TableColumn<ClassDataMutation, String> employeeName = new TableColumn("Dibuat Oleh");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblEmployeeByIdemployee() != null
                                ? param.getValue().getMutationHistory().getTblEmployeeByIdemployee().getTblPeople().getFullName() : "", 
                        param.getValue().getMutationHistory().tblEmployeeByIdemployeeProperty()));
        employeeName.setMinWidth(140);
        
        TableColumn<ClassDataMutation, String> typeMutation = new TableColumn("Status");
        typeMutation.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getRefItemMutationType().getTypeName()
                        + (param.getValue().getMutationHistory().getTblEmployeeByIdemployee() != null
                                ? (" :\n" + param.getValue().getMutationHistory().getTblEmployeeByIdemployee().getTblPeople().getFullName())
                                : ""), 
                        param.getValue().getMutationHistory().refItemMutationTypeProperty()));
        typeMutation.setMinWidth(120);
        
        TableColumn<ClassDataMutation, String> dateByMutation = new TableColumn("Tanggal - Oleh");
        dateByMutation.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getMutationHistory().getMutationDate() != null
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getMutationHistory().getMutationDate()) : "")
                        + " -\n"
                        + param.getValue().getMutationHistory().getTblEmployeeByCreateBy().getTblPeople().getFullName(), 
                        param.getValue().getMutationHistory().mutationDateProperty()));
        dateByMutation.setMinWidth(120);
        
        TableColumn<ClassDataMutation, String> noteMutation = new TableColumn("Catatan");
        noteMutation.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getMutationNote(), 
                        param.getValue().getMutationHistory().mutationNoteProperty()));
        noteMutation.setMinWidth(200);

        tableView.getColumns().addAll(
                dateByMutation,
                itemGroup, itemQuantity, itemUnit, 
                locationTitle, typeMutation);
        tableView.setItems(loadAllDataMutationHistory());
        tableDataWarehouseMutationHistory = tableView;
        
        //set filter
        cft = new ClassFilteringTable<>(
                ClassDataMutation.class,
                tableDataWarehouseMutationHistory,
                tableDataWarehouseMutationHistory.getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public String getNameLocation(TblLocation location) {
     //System.out.println("Hsl>>"+location.getRefLocationType().getIdtype());

        String name = "~";

        switch (location.getRefLocationType().getIdtype()) {
            case 0:
                List<TblRoom> room = parentController.getFWarehouseManager().getRoomByIdLocation(location.getIdlocation());
                for (TblRoom getRoom : room) {
                    name = getRoom.getRoomName();
                }
                break;
            case 1:
                List<TblLocationOfWarehouse> warehouse = parentController.getFWarehouseManager().getWarehouseByIdLocation(location.getIdlocation());
                for (TblLocationOfWarehouse getWarehouse : warehouse) {
                    name = getWarehouse.getWarehouseName();
                }
                break;
            case 2:
                List<TblLocationOfLaundry> laundry = parentController.getFWarehouseManager().getLaundryByIdLocation(location.getIdlocation());
                for (TblLocationOfLaundry getLaundry : laundry) {
                    name = getLaundry.getLaundryName();
                }
                break;
            case 3:
                List<TblSupplier> supplier = parentController.getFWarehouseManager().getSupplierByIdLocation(location.getIdlocation());
                for (TblSupplier getSupplier : supplier) {
                    name = getSupplier.getSupplierName();
                }
                break;
//            case 4:
//                List<TblLocationOfBin> bin = parentController.getFWarehouseManager().getBinByIdLocation(location.getIdlocation());
//                for (TblLocationOfBin getBin : bin) {
//                    name = getBin.getBinName();
//                }
//                break;
        }
        return name;
    }

    private String getCodeProperty(ClassDataMutation classDataMutation){
        if(classDataMutation != null){
            if(classDataMutation.getMutationHistoryPropertyBarcode() != null){
                return classDataMutation.getMutationHistoryPropertyBarcode().getTblPropertyBarcode().getCodeBarcode();
            }
        }
        return "-";
    }
    
    private String getExpiredDate(ClassDataMutation classDataMutation){
        if(classDataMutation != null){
            if(classDataMutation.getMutationHistoryItemExpiredDate()!= null){
                return ClassFormatter.dateFormate.format(classDataMutation.getMutationHistoryItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate());
            }
        }
        return "-";
    }
    
    public ObservableList<ClassDataMutation> loadAllDataMutationHistory() {
        List<ClassDataMutation> list = new ArrayList<>();
        List<TblItemMutationHistory> imhs = parentController.getFWarehouseManager().getAllDataItemMutationHistory();
        for (TblItemMutationHistory imh : imhs) {
            if (imh.getTblItem().getPropertyStatus()) {   //Property
                TblItemMutationHistoryPropertyBarcode imhpb = parentController.getFWarehouseManager().getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(imh.getIdmutation());
                if (imhpb != null) {
                    ClassDataMutation classDataMutation = new ClassDataMutation();
                    classDataMutation.setMutationHistory(imh);
                    classDataMutation.setMutationHistoryPropertyBarcode(imhpb);
                    classDataMutation.getMutationHistoryPropertyBarcode().setTblPropertyBarcode(parentController.getFWarehouseManager().getDataPropertyBarcode(classDataMutation.getMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode()));
                    list.add(classDataMutation);
                }
            } else {
                if (imh.getTblItem().getConsumable()) {   //Consumable
                    List<TblItemMutationHistoryItemExpiredDate> imhieds = parentController.getFWarehouseManager().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(imh.getIdmutation());
                    for (TblItemMutationHistoryItemExpiredDate imhied : imhieds) {
                        ClassDataMutation classDataMutation = new ClassDataMutation();
                        classDataMutation.setMutationHistory(imh);
                        classDataMutation.setMutationHistoryItemExpiredDate(imhied);
                        classDataMutation.getMutationHistoryItemExpiredDate().setTblItemExpiredDate(parentController.getFWarehouseManager().getDataItemExpiredDate(classDataMutation.getMutationHistoryItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate()));
                        list.add(classDataMutation);
                    }
                } else {
                    ClassDataMutation classDataMutation = new ClassDataMutation();
                    classDataMutation.setMutationHistory(imh);
                    list.add(classDataMutation);
                }
            }
        }
        return FXCollections.observableArrayList(list);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initTableDataWarehouseMutationHistory();

    }

    public WarehouseMutationHistoryController(FeatureWarehouseController parentController) {
        this.parentController = parentController;
    }

    private final FeatureWarehouseController parentController;
}
