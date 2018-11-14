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
import hotelfx.persistence.model.TblPurchaseRequestDetail;
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
public class PurchaseOrderDetailController implements Initializable {

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
                txtItemCost,
                txtItemDiscount,
//                txtItemQuantity,
                cbpItem);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemCost,
                txtItemDiscount,
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
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 560, 300
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
        List<TblItem> list = purchaseOrderController.getService().getAllDataItem();
        for (TblItem data : list) {
            //data unit
            data.setTblUnit(purchaseOrderController.getService().getDataUnit(data.getTblUnit().getIdunit()));
            //data item type hk
            if (data.getTblItemTypeHk() != null) {
                data.setTblItemTypeHk(purchaseOrderController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
            }
            //data item type wh
            if (data.getTblItemTypeWh() != null) {
                data.setTblItemTypeWh(purchaseOrderController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        for (int i = list.size() - 1; i > -1; i--) {
            for (int j = 0; j < purchaseOrderController.tableDataDetail.getTableView().getItems().size(); j++) {
                if (((PurchaseOrderController.PurchaseOrderDetailCreated) purchaseOrderController.tableDataDetail.getTableView().getItems().get(j)).getDataPODetail().getTblSupplierItem().getTblItem().getIditem()
                        == list.get(i).getIditem()) {
                    if (purchaseOrderController.selectedDataDetail.getTblSupplierItem().getTblItem() == null
                            || purchaseOrderController.selectedDataDetail.getTblSupplierItem().getTblItem().getIditem() != list.get(i).getIditem()) {
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

        Bindings.bindBidirectional(txtItemCost.textProperty(), purchaseOrderController.selectedDataDetail.itemCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemDiscount.textProperty(), purchaseOrderController.selectedDataDetail.itemDiscountProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtItemQuantity.textProperty(), purchaseOrderController.selectedDataDetail.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        lblUnitName.setText(" ");

        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblUnitName.setText(newVal.getTblUnit().getUnitName());
            } else {
                lblUnitName.setText(" ");
            }
        });

        cbpItem.valueProperty().bindBidirectional(purchaseOrderController.selectedDataDetail.getTblSupplierItem().tblItemProperty());

        cbpItem.hide();

        txtItem.setText(purchaseOrderController.selectedDataDetail.getTblSupplierItem().getSupplierItemName() + " - "
                + purchaseOrderController.selectedDataDetail.getTblSupplierItem().getSupllierItemCode());
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseOrderController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                switch (purchaseOrderController.dataInputDetailStatus) {
                    case 0:
//                    HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Adding data successed..!", null, purchaseOrderController.dialogStageDetal);
//                    purchaseOrderController.tableDataDetail.getTableView().getItems().add(purchaseOrderController.selectedDataDetail);
//                    //refresh data bill
//                    purchaseOrderController.refreshDataBill();
//                    //close form data detail
//                    purchaseOrderController.dialogStageDetal.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", purchaseOrderController.dialogStageDetal);
//                    purchaseOrderController.tableDataDetail.getTableView().getItems().remove(purchaseOrderController.tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
//                    purchaseOrderController.tableDataDetail.getTableView().getItems().add(purchaseOrderController.selectedDataDetail);
//                    purchaseOrderController.tableDataDetail.getTableView().getItems().set(purchaseOrderController.tableDataDetail.getTableView().getSelectionModel().getSelectedIndex(), purchaseOrderController.selectedDataDetail);
                        ((PurchaseOrderController.PurchaseOrderDetailCreated) purchaseOrderController.tableDataDetail.getTableView().getItems().get(purchaseOrderController.tableDataDetail.getTableView().getSelectionModel().getSelectedIndex())).setDataPODetail(purchaseOrderController.selectedDataDetail);
                        //refresh data bill
                        purchaseOrderController.refreshDataBill();
                        //close form data detail
                        purchaseOrderController.dialogStageDetal.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseOrderController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        purchaseOrderController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtItemCost.getText() == null
                || txtItemCost.getText().equals("")
                || txtItemCost.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga (Satuan) Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (purchaseOrderController.selectedDataDetail.getItemCost()
                    .compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga (Satuan) Barang : Tidak boleh kurang dari '0' \n";
            }
        }
        if (txtItemDiscount.getText() == null
                || txtItemDiscount.getText().equals("")
                || txtItemDiscount.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Diskon (Satuan) Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (purchaseOrderController.selectedDataDetail.getItemDiscount().compareTo(purchaseOrderController.selectedDataDetail.getItemCost()) == 1) {
                dataInput = false;
                errDataInput += "Diskon (Satuan) Barang : Tidak boleh lebih besar dari harga (satuan) barang ..!! \n";
            }
        }
        if (txtItemQuantity.getText() == null
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah (Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (purchaseOrderController.selectedDataDetail.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah (Barang) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }else{
                List<TblPurchaseRequestDetail> prDetails = purchaseOrderController.getService().getAllDataPurchaseRequestDetailByIDPurchaseRequest(purchaseOrderController.selectedData.getTblPurchaseRequest().getIdpr());
                BigDecimal maxPRQuantity = new BigDecimal("0");
                for(TblPurchaseRequestDetail prDetail : prDetails){
                    if(prDetail.getTblItem().getIditem() == purchaseOrderController.selectedDataDetail.getTblSupplierItem().getTblItem().getIditem()){
                        maxPRQuantity = prDetail.getItemQuantity();
                        break;
                    }
                }
                if(purchaseOrderController.selectedDataDetail.getItemQuantity()
                    .compareTo(maxPRQuantity) == 1){
                    dataInput = false;
                    errDataInput += "Jumlah (Barang) : Tidak boleh dari " + ClassFormatter.decimalFormat.format(maxPRQuantity) + " (Jumlah MR) \n";
                }
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

    public PurchaseOrderDetailController(PurchaseOrderController parentController) {
        purchaseOrderController = parentController;
    }

    private final PurchaseOrderController purchaseOrderController;

}
