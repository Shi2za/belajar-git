/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_laundry.laundry_in_coming;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
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
 * @author ABC-Programmer
 */
public class LaundryInComingDetailController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private AnchorPane ancItemLayout;
    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {
        initDataDetailPopup();

        btnSave.setTooltip(new Tooltip("Tambah (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpItem);
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

        TableColumn<TblItem, String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);

        tableItem.getColumns().addAll(codeItem, itemName, titledItemType);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadAllDataItem());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 560, 250
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
        List<TblItem> list = laundryInComingController.getService().getAllDataItem();
        for (TblItem data : list) {
            //data unit
            data.setTblUnit(laundryInComingController.getService().getDataUnit(data.getTblUnit().getIdunit()));
            //data item type hk
            if (data.getTblItemTypeHk() != null) {
                data.setTblItemTypeHk(laundryInComingController.getService().getDataItemTypeHK(data.getTblItemTypeHk().getIditemTypeHk()));
            }
            //data item type wh
            if (data.getTblItemTypeWh() != null) {
                data.setTblItemTypeWh(laundryInComingController.getService().getDataItemTypeWH(data.getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        for (int i = list.size() - 1; i > -1; i--) {
            for (int j = 0; j < laundryInComingController.tableDataDetail.getTableView().getItems().size(); j++) {
                if (((LaundryInComingController.ICDetailItemMutationHistory) laundryInComingController.tableDataDetail.getTableView().getItems().get(j)).getDataICDIMH().getTblInComingDetail().getTblItem().getIditem()
                        == list.get(i).getIditem()) {
                    list.remove(i);
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

        cbpItem.hide();
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", laundryInComingController.dialogStageDetal);
            if (alert.getResult() == ButtonType.OK) {
                switch (laundryInComingController.dataInputDetailStatus) {
                    case 0:
                        laundryInComingController.selectedDataDetail = laundryInComingController.generateDataICDetailItemMutationHistory(cbpItem.getValue());
                        if (laundryInComingController.selectedDataDetail != null) {
                            ClassMessage.showSucceedAddingDataMessage("", laundryInComingController.dialogStageDetal);
                            laundryInComingController.tableDataDetail.getTableView().getItems().add(laundryInComingController.selectedDataDetail);
                            //close form data detail
                            laundryInComingController.dialogStageDetal.close();
                        } else {
                            ClassMessage.showFailedAddingDataMessage("", laundryInComingController.dialogStageDetal);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, laundryInComingController.dialogStageDetal);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        laundryInComingController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
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
 
    public LaundryInComingDetailController(LaundryInComingController parentController) {
        laundryInComingController = parentController;
    }

    private final LaundryInComingController laundryInComingController;
    
}
