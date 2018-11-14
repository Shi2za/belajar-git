/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_supplier.supplier_mutation_history;

import hotelfx.helper.ClassDataMutation;
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
import hotelfx.view.feature_supplier.FeatureSupplierController;
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
public class SupplierMutationHistoryController implements Initializable {

    @FXML
    private TableView tableDataSupplierMutationHistory;
    @FXML
    private AnchorPane tableDataSupplierMutationHistoryLayout;

    private void initTableDataSupplierMutationHistory() {
        setTableDataSupplierMutationHistory();

        tableDataSupplierMutationHistoryLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataSupplierMutationHistory, 15.0);
        AnchorPane.setLeftAnchor(tableDataSupplierMutationHistory, 15.0);
        AnchorPane.setBottomAnchor(tableDataSupplierMutationHistory, 15.0);
        AnchorPane.setRightAnchor(tableDataSupplierMutationHistory, 15.0);
        tableDataSupplierMutationHistoryLayout.getChildren().add(tableDataSupplierMutationHistory);
    }

    private void setTableDataSupplierMutationHistory() {
        TableView<ClassDataMutation> tableView = new TableView();

        TableColumn<ClassDataMutation, String> itemName = new TableColumn("ID-Nama");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getCodeItem() + "-" + param.getValue().getMutationHistory().getTblItem().getItemName(), param.getValue().getMutationHistory().getTblItem().itemNameProperty()));
        itemName.setMinWidth(200);

        TableColumn<ClassDataMutation, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getMutationHistory().getItemQuantity()), param.getValue().getMutationHistory().itemQuantityProperty()));
        itemQuantity.setMinWidth(100);

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

        TableColumn<ClassDataMutation, String> typeItemHK = new TableColumn("Tipe Barang");
        typeItemHK.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getTblItemTypeHk() != null 
                        ? param.getValue().getMutationHistory().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-", 
                        param.getValue().getMutationHistory().getTblItem().tblItemTypeHkProperty()));
        typeItemHK.setMinWidth(140);
        
        TableColumn<ClassDataMutation, String> typeItemWH = new TableColumn("Tipe Barang");
        typeItemWH.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblItem().getTblItemTypeWh() != null 
                        ? param.getValue().getMutationHistory().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-", 
                        param.getValue().getMutationHistory().getTblItem().tblItemTypeWhProperty()));
        typeItemWH.setMinWidth(140);

        TableColumn<ClassDataMutation, String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(typeItemHK, typeItemWH);
        
        TableColumn<ClassDataMutation, String> itemGroup = new TableColumn("Barang");
        itemGroup.getColumns().addAll(titledItemType, itemName, codeProperty, expiredDate);

        TableColumn<ClassDataMutation, String> locationSourceType = new TableColumn("Tipe");
        locationSourceType.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().getRefLocationType().getTypeName(), param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().getRefLocationType().typeNameProperty()));
        locationSourceType.setMinWidth(90);

        TableColumn<ClassDataMutation, String> nameLocationSource = new TableColumn("Nama");
        nameLocationSource.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> getNameLocation(param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource()), param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().locationNameProperty()));
        nameLocationSource.setMinWidth(140);

        TableColumn<ClassDataMutation, String> locationSource = new TableColumn("Lokasi (Sumber)");
        locationSource.getColumns().addAll(locationSourceType, nameLocationSource);

        TableColumn<ClassDataMutation, String> locationDestinationType = new TableColumn("Tipe");
        locationDestinationType.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblLocationByIdlocationOfDestination().getRefLocationType().getTypeName(), param.getValue().getMutationHistory().getTblLocationByIdlocationOfSource().getRefLocationType().typeNameProperty()));
        locationDestinationType.setMinWidth(90);

        TableColumn<ClassDataMutation, String> nameLocationDestination = new TableColumn("Nama");
        nameLocationDestination.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> getNameLocation(param.getValue().getMutationHistory().getTblLocationByIdlocationOfDestination()), param.getValue().getMutationHistory().tblLocationByIdlocationOfDestinationProperty()));
        nameLocationDestination.setMinWidth(140);
        
        TableColumn<ClassDataMutation, String> locationDestination = new TableColumn("Lokasi (Tujuan)");
        locationDestination.getColumns().addAll(locationDestinationType, nameLocationDestination);

        TableColumn<ClassDataMutation, String> employeeName = new TableColumn("Penanggung" + "\n" + "Jawab");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getTblEmployeeByIdemployee() != null
                                ? param.getValue().getMutationHistory().getTblEmployeeByIdemployee().getCodeEmployee() + "-" + param.getValue().getMutationHistory().getTblEmployeeByIdemployee().getTblPeople().getFullName() : "", param.getValue().getMutationHistory().tblEmployeeByIdemployeeProperty()));
        employeeName.setMinWidth(140);

        TableColumn<ClassDataMutation, String> typeMutation = new TableColumn("Tipe Mutasi");
        typeMutation.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getRefItemMutationType().getTypeName(), param.getValue().getMutationHistory().getRefItemMutationType().typeNameProperty()));
        typeMutation.setMinWidth(140);

        TableColumn<ClassDataMutation, String> dateMutation = new TableColumn("Tanggal Mutasi");
        dateMutation.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getMutationDate() != null
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getMutationHistory().getMutationDate()) : "", param.getValue().getMutationHistory().mutationDateProperty()));
        dateMutation.setMinWidth(160);

        TableColumn<ClassDataMutation, String> noteMutation = new TableColumn("Catatan");
        noteMutation.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataMutation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getMutationHistory().getMutationNote(), param.getValue().getMutationHistory().mutationNoteProperty()));
        noteMutation.setMinWidth(200);
        /* itemNote.setCellFactory(new Callback<TableColumn<TblItemLocation,Boolean>,TableCell<TblItemLocation,Boolean>>() {
         @Override
         public TableCell<TblItemLocation, Boolean> call(TableColumn<TblItemLocation, Boolean> param) {
         CheckBoxTableCell<TblItemLocation,Boolean>cell = new CheckBoxTableCell<TblItemLocation,Boolean>(); 
         cell.setAlignment(Pos.CENTER);
         return cell;
         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }
         });*/

        tableView.getColumns().addAll(//typeItem, codeItem, itemName, codeProperty, 
                itemGroup, itemQuantity, itemUnit,
                locationSource, locationDestination,
                employeeName, typeMutation, dateMutation);
        tableView.setItems(loadAllDataMutationHistory());
        tableDataSupplierMutationHistory = tableView;
    }

    public String getNameLocation(TblLocation location) {

        String name = "";

        switch (location.getRefLocationType().getIdtype()) {
            case 0:
                List<TblRoom> room = parentController.getFSupplierManager().getRoomByIdLocation(location.getIdlocation());
                for (TblRoom getRoom : room) {
                    name = getRoom.getRoomName();
                }
                break;
            case 1:
                List<TblLocationOfWarehouse> warehouse = parentController.getFSupplierManager().getWarehouseByIdLocation(location.getIdlocation());
                for (TblLocationOfWarehouse getWarehouse : warehouse) {
                    name = getWarehouse.getWarehouseName();
                }
                break;
            case 2:
                List<TblLocationOfLaundry> laundry = parentController.getFSupplierManager().getLaundryByIdLocation(location.getIdlocation());
                for (TblLocationOfLaundry getLaundry : laundry) {
                    name = getLaundry.getLaundryName();
                }
                break;
            case 3:
                List<TblSupplier> supplier = parentController.getFSupplierManager().getSupplierByIdLocation(location.getIdlocation());
                for (TblSupplier getSupplier : supplier) {
                    name = getSupplier.getSupplierName();
                }
                break;
            case 4:
                List<TblLocationOfBin> bin = parentController.getFSupplierManager().getBinByIdLocation(location.getIdlocation());
                for (TblLocationOfBin getBin : bin) {
                    name = getBin.getBinName();
                }
                break;
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
        List<TblItemMutationHistory> imhs = parentController.getFSupplierManager().getAllDataItemMutationHistory();
        for (TblItemMutationHistory imh : imhs) {
            if (imh.getTblItem().getPropertyStatus()) {   //Property
                TblItemMutationHistoryPropertyBarcode imhpb = parentController.getFSupplierManager().getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(imh.getIdmutation());
                if (imhpb != null) {
                    ClassDataMutation classDataMutation = new ClassDataMutation();
                    classDataMutation.setMutationHistory(imh);
                    classDataMutation.setMutationHistoryPropertyBarcode(imhpb);
                    classDataMutation.getMutationHistoryPropertyBarcode().setTblPropertyBarcode(parentController.getFSupplierManager().getDataPropertyBarcode(classDataMutation.getMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode()));
                    list.add(classDataMutation);
                }
            } else {
                if (imh.getTblItem().getConsumable()) {   //Consumable
                    List<TblItemMutationHistoryItemExpiredDate> imhieds = parentController.getFSupplierManager().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(imh.getIdmutation());
                    for (TblItemMutationHistoryItemExpiredDate imhied : imhieds) {
                        ClassDataMutation classDataMutation = new ClassDataMutation();
                        classDataMutation.setMutationHistory(imh);
                        classDataMutation.setMutationHistoryItemExpiredDate(imhied);
                        classDataMutation.getMutationHistoryItemExpiredDate().setTblItemExpiredDate(parentController.getFSupplierManager().getDataItemExpiredDate(classDataMutation.getMutationHistoryItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate()));
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

        initTableDataSupplierMutationHistory();

    }

    public SupplierMutationHistoryController(FeatureSupplierController parentController) {
        this.parentController = parentController;
    }

    private final FeatureSupplierController parentController;
}
