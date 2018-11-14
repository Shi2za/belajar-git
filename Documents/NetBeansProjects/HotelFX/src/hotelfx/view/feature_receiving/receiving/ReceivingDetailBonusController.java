/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_receiving.receiving;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblLocationOfWarehouse;
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
public class ReceivingDetailBonusController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private AnchorPane ancItemLayout;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    @FXML
    private Label lblUnitName;

    @FXML
    private AnchorPane ancLocationLayout;
    private JFXCComboBoxTablePopup<TblLocationOfWarehouse> cbpWarehouse;

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
                cbpItem,
                cbpWarehouse);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }

    private void initDataDetailPopup() {
        //Item
        TableView<TblItem> tableItem = new TableView<>();

        TableColumn<TblItem, String> codeItem = new TableColumn<>("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeItem(),
                        param.getValue().codeItemProperty()));
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getItemName(),
                        param.getValue().itemNameProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblItem, String> itemTypeHK = new TableColumn<>("Tipe");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemTypeHk() != null 
                        ? param.getValue().getTblItemTypeHk().getItemTypeHkname() : "-",
                        param.getValue().tblItemTypeHkProperty()));
        itemTypeHK.setMinWidth(140);
        
        TableColumn<TblItem, String> itemTypeWH = new TableColumn<>("Tipe");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemTypeWh() != null 
                        ? param.getValue().getTblItemTypeWh().getItemTypeWhname() : "-",
                        param.getValue().tblItemTypeWhProperty()));
        itemTypeWH.setMinWidth(140);

        tableItem.getColumns().addAll(codeItem, itemName, itemTypeWH);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 420, 300
        );

        //Warehouse
        TableView<TblLocationOfWarehouse> tableWarehouse = new TableView<>();

        TableColumn<TblLocationOfWarehouse, String> warehouseName = new TableColumn<>("Gudang");
        warehouseName.setCellValueFactory(cellData -> cellData.getValue().warehouseNameProperty());
        warehouseName.setMinWidth(140);

        tableWarehouse.getColumns().addAll(warehouseName);

        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());

        cbpWarehouse = new JFXCComboBoxTablePopup<>(
                TblLocationOfWarehouse.class, tableWarehouse, warehouseItems, "", "Gudang *", true, 200, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpItem, 0.0);
        AnchorPane.setLeftAnchor(cbpItem, 0.0);
        AnchorPane.setRightAnchor(cbpItem, 0.0);
        AnchorPane.setTopAnchor(cbpItem, 0.0);
        ancItemLayout.getChildren().clear();
        ancItemLayout.getChildren().add(cbpItem);
        AnchorPane.setBottomAnchor(cbpWarehouse, 0.0);
        AnchorPane.setLeftAnchor(cbpWarehouse, 0.0);
        AnchorPane.setRightAnchor(cbpWarehouse, 0.0);
        AnchorPane.setTopAnchor(cbpWarehouse, 0.0);
        ancLocationLayout.getChildren().clear();
        ancLocationLayout.getChildren().add(cbpWarehouse);
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list = receivingController.getService().getAllDataItem();
        for (TblItem data : list) {
            //data unit
            data.setTblUnit(receivingController.getService().getDataUnit(data.getTblUnit().getIdunit()));
            //data item type hk
            if(data.getTblItemTypeHk() != null){
                data.setTblItemTypeHk(receivingController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
            }
            //data item type wh
            if(data.getTblItemTypeWh() != null){
                data.setTblItemTypeWh(receivingController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        for (int i = list.size() - 1; i > -1; i--) {
            for (int j = 0; j < receivingController.detailLocations.size(); j++) {
                if (receivingController.detailLocations.get(j).getTblDetail().getTblItem() != null
                        && receivingController.detailLocations.get(j).getTblDetail().getTblItem().getIditem() == list.get(i).getIditem()) {
                    if (receivingController.selectedDataDetailLocation.getTblDetail().getTblItem() == null
                            || receivingController.selectedDataDetailLocation.getTblDetail().getTblItem().getIditem() != list.get(i).getIditem()) {
                        list.remove(i);
                    }
                    break;
                }
            }
        }
        return list;
    }

    private List<TblLocationOfWarehouse> loadAllDataLocationOfWarehouse() {
        List<TblLocationOfWarehouse> list = receivingController.getService().getAllDataWarehouse();
        for (TblLocationOfWarehouse data : list) {
            //data location
            data.setTblLocation(receivingController.getService().getDataLocation(data.getTblLocation().getIdlocation()));
        }
        return list;
    }

    private void refreshDataPopup() {
        //supplier item
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());
        cbpItem.setItems(itemItems);

        //warehouse
        ObservableList<TblLocationOfWarehouse> warehouseItems = FXCollections.observableArrayList(loadAllDataLocationOfWarehouse());
        cbpWarehouse.setItems(warehouseItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        Bindings.bindBidirectional(txtItemQuantity.textProperty(), receivingController.selectedDataDetailLocation.getTblDetail().itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        lblUnitName.setText(" ");
        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.getConsumable()) {
                    txtItemQuantity.setDisable(true);
                    receivingController.selectedDataDetailLocation.getTblDetail().setItemQuantity(new BigDecimal("0"));
                } else {
                    txtItemQuantity.setDisable(false);
                }
                lblUnitName.setText(newVal.getTblUnit().getUnitName());
            } else {
                txtItemQuantity.setDisable(false);
                lblUnitName.setText(" ");
            }
        });
        cbpItem.valueProperty().bindBidirectional(receivingController.selectedDataDetailLocation.getTblDetail().tblItemProperty());

//        if(receivingController.selectedDataDetailLocation.getTblLocation() == null){
//            cbpWarehouse.setValue(null);
//        }else{
//            cbpWarehouse.setValue(receivingController.getService().getDataWarehouseByIDLocation(receivingController.selectedDataDetailLocation.getTblLocation().getIdlocation()));
//        }
//        cbpWarehouse.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if(newVal != null){
//                receivingController.selectedDataDetailLocation.setTblLocation(newVal.getTblLocation());
//            }
//        });
        cbpWarehouse.valueProperty().bindBidirectional(receivingController.selectedDataDetailLocation.tblLocationWarehouseProperty());

        cbpItem.hide();
        cbpWarehouse.hide();
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", receivingController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                //unbind data
                txtItemQuantity.textProperty().unbindBidirectional(receivingController.selectedDataDetailLocation.getTblDetail().itemQuantityProperty());
                cbpItem.valueProperty().unbindBidirectional(receivingController.selectedDataDetailLocation.getTblDetail().tblItemProperty());
                cbpWarehouse.valueProperty().unbindBidirectional(receivingController.selectedDataDetailLocation.tblLocationWarehouseProperty());
                //message : success
                ClassMessage.showSucceedAddingDataMessage("", receivingController.dialogStageDetal);
                receivingController.detailLocations.add(receivingController.selectedDataDetailLocation);
                //refresh data detail
                receivingController.setDataDetail();
                //close form data detail
                receivingController.dialogStageDetal.close();
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, receivingController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        receivingController.dialogStageDetal.close();
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
            if (receivingController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Jumlah (Barang) : Tidak boleh kurang dari '0' \n";
            } else {
                if ((receivingController.selectedDataDetailLocation.getTblDetail().getItemQuantity()
                        .compareTo(new BigDecimal("0")) == 0)
                        && !receivingController.selectedDataDetailLocation.getTblDetail().getTblItem().getConsumable()) {
                    dataInput = false;
                    errDataInput += "Jumlah (Barang) : Tidak boleh sama dengan '0' \n";
                }
            }
        }
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpWarehouse.getValue() == null) {
            dataInput = false;
            errDataInput += "Gudang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        //init form input
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReceivingDetailBonusController(ReceivingController parentController) {
        receivingController = parentController;
    }

    private final ReceivingController receivingController;

}
