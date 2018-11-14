/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_request.purchase_request;

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
public class PurchaseRequestDetailController implements Initializable {

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
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 420, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpItem, 0.0);
        AnchorPane.setLeftAnchor(cbpItem, 0.0);
        AnchorPane.setRightAnchor(cbpItem, 0.0);
        AnchorPane.setTopAnchor(cbpItem, 0.0);
        ancItemLayout.getChildren().clear();
        ancItemLayout.getChildren().add(cbpItem);
//        gpFormDataDetail.add(cbpItem, 1, 1);
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list = purchaseRequestController.getService().getAllDataItem();
        for (TblItem data : list) {
            //data unit
            data.setTblUnit(purchaseRequestController.getService().getDataUnit(data.getTblUnit().getIdunit()));
            //data item type hk
            if (data.getTblItemTypeHk() != null) {
                data.setTblItemTypeHk(purchaseRequestController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
            }
            //data item type wh
            if (data.getTblItemTypeWh() != null) {
                data.setTblItemTypeWh(purchaseRequestController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        for (int i = list.size() - 1; i > -1; i--) {
            for (int j = 0; j < purchaseRequestController.tableDataDetail.getTableView().getItems().size(); j++) {
                if (((TblPurchaseRequestDetail) purchaseRequestController.tableDataDetail.getTableView().getItems().get(j)).getTblItem().getIditem() == list.get(i).getIditem()) {
                    if (purchaseRequestController.selectedDataDetail.getTblItem() == null
                            || purchaseRequestController.selectedDataDetail.getTblItem().getIditem() != list.get(i).getIditem()) {
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

        Bindings.bindBidirectional(txtItemQuantity.textProperty(), purchaseRequestController.selectedDataDetail.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        lblUnitName.setText(" ");

        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblUnitName.setText(newVal.getTblUnit().getUnitName());
            } else {
                lblUnitName.setText(" ");
            }
        });

        cbpItem.valueProperty().bindBidirectional(purchaseRequestController.selectedDataDetail.tblItemProperty());

        cbpItem.hide();
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", purchaseRequestController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                switch (purchaseRequestController.dataInputDetailStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", purchaseRequestController.dialogStageDetal);
                        purchaseRequestController.tableDataDetail.getTableView().getItems().add(purchaseRequestController.selectedDataDetail);
                        //close form data detail
                        purchaseRequestController.dialogStageDetal.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", purchaseRequestController.dialogStageDetal);
//                    purchaseRequestController.tableDataDetail.getTableView().getItems().remove(purchaseRequestController.tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
//                    purchaseRequestController.tableDataDetail.getTableView().getItems().add(purchaseRequestController.selectedDataDetail);
                        purchaseRequestController.tableDataDetail.getTableView().getItems().set(purchaseRequestController.tableDataDetail.getTableView().getSelectionModel().getSelectedIndex(), purchaseRequestController.selectedDataDetail);
                        //close form data detail
                        purchaseRequestController.dialogStageDetal.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, purchaseRequestController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        purchaseRequestController.dialogStageDetal.close();
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
            if (purchaseRequestController.selectedDataDetail.getItemQuantity()
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

    public PurchaseRequestDetailController(PurchaseRequestController parentController) {
        purchaseRequestController = parentController;
    }

    private final PurchaseRequestController purchaseRequestController;

}
