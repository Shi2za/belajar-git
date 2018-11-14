/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
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
public class PurchaseOrderDetailRevisionController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private JFXTextField txtItemCost;

    @FXML
    private JFXTextField txtItemDiscount;

    @FXML
    private Label lblUnitName;

    @FXML
    private AnchorPane ancItemLayout;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;
    private JFXTextField txtItem;

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

        cbpItem.setDisable(true);

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtItemQuantity);
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
        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
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

        TableColumn<TblItem, String> itemCost = new TableColumn("Harga (Terakhir)");
        itemCost.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCostOfGoodsSold()), param.getValue().itemCostOfGoodsSoldProperty()));
        itemCost.setMinWidth(140);

        tableItem.getColumns().addAll(codeItem, itemName, itemTypeWH, itemCost);
        
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang", true, 560, 300
        );
        
        txtItem = new JFXTextField();
        txtItem.setPromptText("Barang");
        txtItem.setLabelFloat(true);
        txtItem.setDisable(true);
        
        //attached to grid-pane
        ancItemLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(txtItem, 0.0);
        AnchorPane.setLeftAnchor(txtItem, 0.0);
        AnchorPane.setRightAnchor(txtItem, 0.0);
        AnchorPane.setTopAnchor(txtItem, 0.0);
        ancItemLayout.getChildren().add(txtItem);
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list = purchaseOrderRevisionController.getService().getAllDataItem();
        for (TblItem data : list) {
            //data unit
            data.setTblUnit(purchaseOrderRevisionController.getService().getDataUnit(data.getTblUnit().getIdunit()));
            //data item type hk
            if(data.getTblItemTypeHk() != null){
                data.setTblItemTypeHk(purchaseOrderRevisionController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
            }
            //data item type wh
            if(data.getTblItemTypeWh() != null){
                data.setTblItemTypeWh(purchaseOrderRevisionController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        for (int i = list.size() - 1; i > -1; i--) {
            for (int j = 0; j < purchaseOrderRevisionController.tableDataDetail.getTableView().getItems().size(); j++) {
                if (((PurchaseOrderRevisionController.PurchaseOrderDetailCreated) purchaseOrderRevisionController.tableDataDetail.getTableView().getItems().get(j)).getDataPODetail().getTblSupplierItem().getTblItem().getIditem() == list.get(i).getIditem()) {
                    if (purchaseOrderRevisionController.selectedDataDetail.getTblSupplierItem().getTblItem() == null
                            || purchaseOrderRevisionController.selectedDataDetail.getTblSupplierItem().getTblItem().getIditem() != list.get(i).getIditem()) {
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

        Bindings.bindBidirectional(txtItemCost.textProperty(), purchaseOrderRevisionController.selectedDataDetail.itemCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtItemCost.setDisable(true);
        
        Bindings.bindBidirectional(txtItemDiscount.textProperty(), purchaseOrderRevisionController.selectedDataDetail.itemDiscountProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtItemDiscount.setDisable(true);
        
        Bindings.bindBidirectional(txtItemQuantity.textProperty(), purchaseOrderRevisionController.selectedDataDetail.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        lblUnitName.setText(" ");

        cbpItem.setDisable(true);
        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblUnitName.setText(newVal.getTblUnit().getUnitName());
            } else {
                lblUnitName.setText(" ");
            }
        });

        cbpItem.valueProperty().bindBidirectional(purchaseOrderRevisionController.selectedDataDetail.getTblSupplierItem().tblItemProperty());

        cbpItem.hide();
        
        txtItem.setText(purchaseOrderRevisionController.selectedDataDetail.getTblSupplierItem().getSupplierItemName() + " - " 
                + purchaseOrderRevisionController.selectedDataDetail.getTblSupplierItem().getSupllierItemCode());
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderRevisionController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                switch (purchaseOrderRevisionController.dataInputDetailStatus) {
                    case 0:
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", purchaseOrderRevisionController.dialogStageDetal);
                        ((PurchaseOrderRevisionController.PurchaseOrderDetailCreated) purchaseOrderRevisionController.tableDataDetail.getTableView().getItems().get(purchaseOrderRevisionController.tableDataDetail.getTableView().getSelectionModel().getSelectedIndex())).setDataPODetail(purchaseOrderRevisionController.selectedDataDetail);
                        //refresh data bill
                        purchaseOrderRevisionController.refreshDataBill();
                        //close form data detail
                        purchaseOrderRevisionController.dialogStageDetal.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderRevisionController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        purchaseOrderRevisionController.dialogStageDetal.close();
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
            if (purchaseOrderRevisionController.selectedDataDetail.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah (Barang) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
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

    public PurchaseOrderDetailRevisionController(PurchaseOrderRevisionController parentController) {
        purchaseOrderRevisionController = parentController;
    }

    private final PurchaseOrderRevisionController purchaseOrderRevisionController;   
    
}
