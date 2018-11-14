/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_supplier.supplier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblSupplierItem;
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
public class SupplierItemInputController implements Initializable {

    @FXML
    private AnchorPane ancFormSupplierItem;

    @FXML
    private GridPane gpFormDataSupplierItem;

    @FXML
    private JFXTextField txtSupplierCodeItem;

    @FXML
    private JFXTextField txtSupplierItemName;

    @FXML
    private JFXTextField txtItemCost;

    @FXML
    private AnchorPane ancItemLayout;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    @FXML
    private JFXButton btnSaveSupplierItem;

    @FXML
    private JFXButton btnCancelSupplierItem;

    private void initFormDataSupplierItem() {
        initDataSupplierItemPopup();

        btnSaveSupplierItem.setTooltip(new Tooltip("Simpan (Data Barang)"));
        btnSaveSupplierItem.setOnAction((e) -> {
            dataSupplierItemSaveHandle();
        });

        btnCancelSupplierItem.setTooltip(new Tooltip("Batal"));
        btnCancelSupplierItem.setOnAction((e) -> {
            dataSupplierItemCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpItem,
                txtSupplierCodeItem,
                txtSupplierItemName,
                txtItemCost);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemCost);
    }

    private void initDataSupplierItemPopup() {
        //Item
        TableView<TblItem> tableItem = new TableView<>();

        TableColumn<TblItem, String> codeItem = new TableColumn<>("ID");
        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemName.setMinWidth(140);

        TableColumn<TblItem, String> itemTypeHK = new TableColumn("Tipe");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemTypeHk() != null
                                ? param.getValue().getTblItemTypeHk().getItemTypeHkname() : "-",
                        param.getValue().tblItemTypeHkProperty()));
        itemTypeHK.setMinWidth(140);

        TableColumn<TblItem, String> itemTypeWH = new TableColumn("Tipe");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItemTypeWh() != null
                                ? param.getValue().getTblItemTypeWh().getItemTypeWhname() : "-",
                        param.getValue().tblItemTypeWhProperty()));
        itemTypeWH.setMinWidth(140);

        tableItem.getColumns().addAll(codeItem, itemName, itemTypeWH);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Data Barang (Sistem) *", true, 420, 300
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
        List<TblItem> list = supplierController.getService().getAllDataItem();
        for (TblItem data : list) {
            //data item type hk
            if (data.getTblItemTypeHk() != null) {
                data.setTblItemTypeHk(supplierController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
            }
            //data item type wh
            if (data.getTblItemTypeWh() != null) {
                data.setTblItemTypeWh(supplierController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        //remove data has been used
        for (int i = list.size() - 1; i > -1; i--) {
            for (TblSupplierItem data : (List<TblSupplierItem>) supplierController.tableDataSupplierItem.getTableView().getItems()) {
                if (list.get(i).getIditem()
                        == data.getTblItem().getIditem()) {
                    if (supplierController.dataInputSupplierItemStatus == 0) {    //insert
                        list.remove(i);
                    } else {
                        if (list.get(i).getIditem()
                                != supplierController.selectedDataSupplierItem.getTblItem().getIditem()) {
                            list.remove(i);
                        }
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

        txtSupplierCodeItem.textProperty().bindBidirectional(supplierController.selectedDataSupplierItem.supllierItemCodeProperty());
        txtSupplierItemName.textProperty().bindBidirectional(supplierController.selectedDataSupplierItem.supplierItemNameProperty());

        Bindings.bindBidirectional(txtItemCost.textProperty(), supplierController.selectedDataSupplierItem.itemCostProperty(), new ClassFormatter.CBigDecimalStringConverter());

        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
//                txtItemName.setText(newVal.getItemName());
            } else {
//                txtItemName.setText("");
            }
        });
        cbpItem.valueProperty().bindBidirectional(supplierController.selectedDataSupplierItem.tblItemProperty());

        cbpItem.hide();
    }

    private void dataSupplierItemSaveHandle() {
        if (checkDataInputDataSupplierItem()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", supplierController.supplierItemDialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (supplierController.dataInputSupplierItemStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", supplierController.supplierItemDialogStage);
                        supplierController.tableDataSupplierItem.getTableView().getItems().add(supplierController.selectedDataSupplierItem);
                        //close form data supplier - item
                        supplierController.supplierItemDialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", supplierController.supplierItemDialogStage);
                        supplierController.tableDataSupplierItem.getTableView().getItems().set(supplierController.tableDataSupplierItem.getTableView().getSelectionModel().getSelectedIndex(),
                                supplierController.selectedDataSupplierItem);
                        //close form data supplier item
                        supplierController.supplierItemDialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, supplierController.supplierItemDialogStage);
        }
    }

    private void dataSupplierItemCancelHandle() {
        //close form data supplier - item
        supplierController.supplierItemDialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataSupplierItem() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Data Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtSupplierCodeItem.getText() == null
                || txtSupplierCodeItem.getText().equals("")) {
            dataInput = false;
            errDataInput += "Kode Barang (Supplier) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtSupplierItemName.getText() == null
                || txtSupplierItemName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Barang (Supplier) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtItemCost.getText() == null
                || txtItemCost.getText().equals("")
                || txtItemCost.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (supplierController.selectedDataSupplierItem.getItemCost().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Barang : Tidak dapat kurang dari '0' ..!! \n";
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
        initFormDataSupplierItem();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public SupplierItemInputController(SupplierController parentController) {
        supplierController = parentController;
    }

    private final SupplierController supplierController;

}
