/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_food_and_beverage.food_and_beverage_expired_date;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameItemExpiredDateDetail;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ItemExpiredDateStockOpnameController implements Initializable {

    @FXML
    private AnchorPane ancFormSOIED;

    @FXML
    private GridPane gpFormDataSOIED;

    @FXML
    private JFXTextField txtCodeSO;

    @FXML
    private JFXTextField txtSODate;

    @FXML
    private JFXTextField txtCodeItemName;

    @FXML
    private JFXTextField txtItemExpiredDate;

    @FXML
    private JFXTextArea txtSONote;

    @FXML
    private JFXTextField txtItemStockSystem;

    @FXML
    private JFXTextField txtItemStockReal;

    @FXML
    private AnchorPane ancDetailItemExpiredDateLayout;

    @FXML
    private JFXTextField txtDifferenceStock;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataSOIED() {

        btnSave.setTooltip(new Tooltip("Simpan (Data StockOpname)"));
        btnSave.setOnAction((e) -> {
            dataSOIEDSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataSOIEDCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField();
    }

    private void setSelectedDataToInputForm() {

        txtCodeSO.setText(foodAndBeverageExpiredDateController.selectedDataSOIED.getCodeStockOpnameIed() != null
                ? foodAndBeverageExpiredDateController.selectedDataSOIED.getCodeStockOpnameIed() : "-");
        txtSODate.setText(ClassFormatter.dateTimeFormate.format(foodAndBeverageExpiredDateController.selectedDataSOIED.getStockOpnameIeddate()));
        txtCodeItemName.setText(foodAndBeverageExpiredDateController.selectedDataSOIED.getTblItem().getCodeItem()
                + " - "
                + foodAndBeverageExpiredDateController.selectedDataSOIED.getTblItem().getItemName());
        txtItemExpiredDate.setText(ClassFormatter.dateFormate.format(foodAndBeverageExpiredDateController.selectedDataSOIED.getExpiredDate()));

        txtSONote.textProperty().bindBidirectional(foodAndBeverageExpiredDateController.selectedDataSOIED.stockOpnameIednoteProperty());

        setTableInputDataItemExpiredDate();
        
        refreshDataStock();
    }

    private void refreshDataStock() {
        foodAndBeverageExpiredDateController.selectedDataSOIED.setItemQuantityReal(new BigDecimal(calculationItemQuantityReal()));
        
        txtItemStockSystem.setText(ClassFormatter.decimalFormat.cFormat(foodAndBeverageExpiredDateController.selectedDataSOIED.getItemQuantitySystem()));
        txtItemStockReal.setText(ClassFormatter.decimalFormat.cFormat(foodAndBeverageExpiredDateController.selectedDataSOIED.getItemQuantityReal()));
        txtDifferenceStock.setText(ClassFormatter.decimalFormat.cFormat((foodAndBeverageExpiredDateController.selectedDataSOIED.getItemQuantityReal()
                .subtract(foodAndBeverageExpiredDateController.selectedDataSOIED.getItemQuantitySystem()))));
    }

    private double calculationItemQuantityReal(){
        double result = 0;
        for(ItemExpiredDateSelected dataIEDS : (List<ItemExpiredDateSelected>) tableInputIEDS.getItems()){
            if(dataIEDS.getSelected()){
                result++;
            }
        }
        return result;
    }
    
    /**
     * DATA (Input - Item Expired Date)
     */
    private TableView<ItemExpiredDateSelected> tableInputIEDS;
    
    private void setTableInputDataItemExpiredDate() {
        //data item expired date - selected
        tableInputIEDS = new TableView();
        tableInputIEDS.setEditable(true);
        
        TableColumn<ItemExpiredDateSelected, String> codeItemExpiredDate = new TableColumn("ID Barang");
        codeItemExpiredDate.setCellValueFactory((TableColumn.CellDataFeatures<ItemExpiredDateSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataItemExpiredDate().getCodeItemExpiredDate(), 
                        param.getValue().dataItemExpiredDateProperty()));
        codeItemExpiredDate.setMinWidth(140);
        
        TableColumn<ItemExpiredDateSelected, Boolean> selectedStatus = new TableColumn("Ada");
        selectedStatus.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectedStatus.setCellFactory(CheckBoxTableCell.forTableColumn(selectedStatus));
        selectedStatus.setMinWidth(140);
        selectedStatus.setEditable(true);
        
        tableInputIEDS.getColumns().addAll(codeItemExpiredDate, selectedStatus);
        tableInputIEDS.setItems(FXCollections.observableArrayList(loadAllDataItemExpiredDateSelected()));
        
        //set content
        ancDetailItemExpiredDateLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableInputIEDS, 0.0);
        AnchorPane.setLeftAnchor(tableInputIEDS, 0.0);
        AnchorPane.setRightAnchor(tableInputIEDS, 0.0);
        AnchorPane.setTopAnchor(tableInputIEDS, 0.0);
        ancDetailItemExpiredDateLayout.getChildren().add(tableInputIEDS);
    }

    private List<ItemExpiredDateSelected> loadAllDataItemExpiredDateSelected(){
        List<ItemExpiredDateSelected> list = new ArrayList<>();
        List<TblItemExpiredDate> tempList = foodAndBeverageExpiredDateController.getService().getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(
                    foodAndBeverageExpiredDateController.selectedDataSOIED.getTblItem().getIditem(),
                    foodAndBeverageExpiredDateController.selectedDataSOIED.getExpiredDate(),
                    1); //Ada = '1'
        for(TblItemExpiredDate tempData : tempList){
//            //data item expired date status
//            tempData.setRefItemExpiredDateStatus(foodAndBeverageExpiredDateController.getService().getDataItemExpiredDateStatus(tempData.getRefItemExpiredDateStatus().getIdstatus()));
            //add to list (item expired date selected : true)
            list.add(new ItemExpiredDateSelected(tempData, true));
        }
        return list;
    }
    
    public class ItemExpiredDateSelected {

        private final ObjectProperty<TblItemExpiredDate> dataItemExpiredDate = new SimpleObjectProperty<>();

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public ItemExpiredDateSelected(TblItemExpiredDate dataItemExpiredDate,
                boolean selected) {
            this.dataItemExpiredDate.set(dataItemExpiredDate);
            this.selected.set(selected);
            
            this.selected.addListener((obs, oldVal, newVal) -> {
                refreshDataStock();
            });
        }

        public ObjectProperty<TblItemExpiredDate> dataItemExpiredDateProperty() {
            return dataItemExpiredDate;
        }

        public TblItemExpiredDate getDataItemExpiredDate() {
            return dataItemExpiredDateProperty().get();
        }

        public void setDataItemExpiredDate(TblItemExpiredDate dataItemExpiredDate) {
            dataItemExpiredDateProperty().set(dataItemExpiredDate);
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean getSelected() {
            return selectedProperty().get();
        }

        public void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

    }

    private void dataSOIEDSaveHandle() {
        if (checkDataInputDataSOIED()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", foodAndBeverageExpiredDateController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblStockOpnameItemExpiredDate dummySelectedDataSOIED = new TblStockOpnameItemExpiredDate(foodAndBeverageExpiredDateController.selectedDataSOIED);
                dummySelectedDataSOIED.setTblItem(new TblItem(dummySelectedDataSOIED.getTblItem()));
                List<TblStockOpnameItemExpiredDateDetail> dummyDetails = new ArrayList<>();
                for(ItemExpiredDateSelected dataIEDS : (List<ItemExpiredDateSelected>)tableInputIEDS.getItems()){
                    if(!dataIEDS.getSelected()){
                        TblStockOpnameItemExpiredDateDetail dummyDetail = new TblStockOpnameItemExpiredDateDetail();
                        dummyDetail.setTblStockOpnameItemExpiredDate(dummySelectedDataSOIED);
                        dummyDetail.setTblItemExpiredDate(new TblItemExpiredDate(dataIEDS.getDataItemExpiredDate()));
//                        dummyDetail.getTblItemExpiredDate().setRefItemExpiredDateStatus(new RefItemExpiredDateStatus(dummyDetail.getTblItemExpiredDate().getRefItemExpiredDateStatus()));
                        dummyDetails.add(dummyDetail);
                    }
                }
                if (foodAndBeverageExpiredDateController.getService().insertDataStockOpnameItemExpiredDate(dummySelectedDataSOIED, 
                        dummyDetails) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", foodAndBeverageExpiredDateController.dialogStage);
                    //refresh data table item expired date
                    foodAndBeverageExpiredDateController.refreshData();
                    //close form data item expired date - stock opname
                    foodAndBeverageExpiredDateController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(foodAndBeverageExpiredDateController.getService().getErrorMessage(), foodAndBeverageExpiredDateController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, foodAndBeverageExpiredDateController.dialogStage);
        }
    }

    private void dataSOIEDCancelHandle() {
        //close form data item expired date - stock opname
        foodAndBeverageExpiredDateController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataSOIED() {
        boolean dataInput = true;
        errDataInput = "";
        return dataInput;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataSOIED();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public ItemExpiredDateStockOpnameController(FoodAndBeverageExpiredDateController parentController) {
        foodAndBeverageExpiredDateController = parentController;
    }

    private final FoodAndBeverageExpiredDateController foodAndBeverageExpiredDateController;
    
}
