/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse.warehouse_store_request;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblStoreRequestDetail;
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
public class WarehouseStoreRequestDetailController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private Label lblUnitName;

    @FXML
    private AnchorPane ancItemLayout;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {
        initDataDetailPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });
        
        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtItemQuantity, 
                cbpItem);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }
    
    private void initDataDetailPopup() {
        //Item
        TableView<TblItem> tableItem = new TableView<>();

        TableColumn<TblItem, String> codeItem = new TableColumn<>("ID");
        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemName.setMinWidth(140);

        TableColumn<TblItem, String> itemTypeHK = new TableColumn<>("House Keeping");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemTypeHk() != null 
                        ? param.getValue().getTblItemTypeHk().getItemTypeHkname() : "-", 
                        param.getValue().tblItemTypeHkProperty()));
        itemTypeHK.setMinWidth(140);
        
        TableColumn<TblItem, String> itemTypeWH = new TableColumn<>("Warehouse");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemTypeWh() != null 
                        ? param.getValue().getTblItemTypeWh().getItemTypeWhname() : "-", 
                        param.getValue().tblItemTypeWhProperty()));
        itemTypeWH.setMinWidth(140);

        TableColumn<TblItem,String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);
        
        tableItem.getColumns().addAll(codeItem, itemName, titledItemType);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 560, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpItem, 0.0);
        AnchorPane.setLeftAnchor(cbpItem, 0.0);
        AnchorPane.setRightAnchor(cbpItem, 0.0);
        AnchorPane.setTopAnchor(cbpItem, 0.0);
        ancItemLayout.getChildren().clear();
        ancItemLayout.getChildren().add(cbpItem);
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list = warehouseStoreRequestController.getService().getAllDataItem();
        for (TblItem data : list) {
            //data unit
            data.setTblUnit(warehouseStoreRequestController.getService().getDataUnit(data.getTblUnit().getIdunit()));
            //data item type hk
            if(data.getTblItemTypeHk() != null){
                data.setTblItemTypeHk(warehouseStoreRequestController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
            }
            //data item type wh
            if(data.getTblItemTypeWh() != null){
                data.setTblItemTypeWh(warehouseStoreRequestController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        for (int i = list.size() - 1; i > -1; i--) {
            for (int j = 0; j < warehouseStoreRequestController.tableDataDetail.getTableView().getItems().size(); j++) {
                if (((TblStoreRequestDetail) warehouseStoreRequestController.tableDataDetail.getTableView().getItems().get(j)).getTblItem().getIditem() == list.get(i).getIditem()) {
                    if (warehouseStoreRequestController.selectedDataDetail.getTblItem() == null
                            || warehouseStoreRequestController.selectedDataDetail.getTblItem().getIditem() != list.get(i).getIditem()) {
                        list.remove(i);
                    }
                    break;
                }
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Item
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());
        cbpItem.setItems(itemItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        Bindings.bindBidirectional(txtItemQuantity.textProperty(), warehouseStoreRequestController.selectedDataDetail.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        lblUnitName.setText(" ");

        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblUnitName.setText(newVal.getTblUnit().getUnitName());
            } else {
                lblUnitName.setText(" ");
            }
        });

        cbpItem.valueProperty().bindBidirectional(warehouseStoreRequestController.selectedDataDetail.tblItemProperty());

        cbpItem.hide();
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", warehouseStoreRequestController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                switch (warehouseStoreRequestController.dataInputDetailStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", warehouseStoreRequestController.dialogStageDetal);
                        warehouseStoreRequestController.tableDataDetail.getTableView().getItems().add(warehouseStoreRequestController.selectedDataDetail);
                        //close form data detail
                        warehouseStoreRequestController.dialogStageDetal.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", warehouseStoreRequestController.dialogStageDetal);
//                    warehouseStoreRequestController.tableDataDetail.getTableView().getItems().remove(warehouseStoreRequestController.tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
//                    warehouseStoreRequestController.tableDataDetail.getTableView().getItems().add(warehouseStoreRequestController.selectedDataDetail);
                        warehouseStoreRequestController.tableDataDetail.getTableView().getItems().set(warehouseStoreRequestController.tableDataDetail.getTableView().getSelectionModel().getSelectedIndex(), warehouseStoreRequestController.selectedDataDetail);
                        //close form data detail
                        warehouseStoreRequestController.dialogStageDetal.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, warehouseStoreRequestController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        warehouseStoreRequestController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtItemQuantity.getText() == null 
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah (Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (warehouseStoreRequestController.selectedDataDetail.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah (Barang) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
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
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public WarehouseStoreRequestDetailController(WarehouseStoreRequestController parentController) {
        warehouseStoreRequestController = parentController;
    }

    private final WarehouseStoreRequestController warehouseStoreRequestController;
    
}
