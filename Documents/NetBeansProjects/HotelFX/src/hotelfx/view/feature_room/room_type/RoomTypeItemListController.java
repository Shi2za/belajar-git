/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room.room_type;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblRoomTypeItem;
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
public class RoomTypeItemListController implements Initializable {

    @FXML
    private AnchorPane ancFormRoomTypeItemList;

    @FXML
    private GridPane gpFormDataRoomTypeItemList;

    @FXML
    private AnchorPane ancItemLayout;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private Label lblUnit;
    
    @FXML
    private JFXCheckBox chbAddAsAdditionalItem;

    private JFXCComboBoxTablePopup<TblItem> cbpItem;

    @FXML
    private JFXButton btnSaveRoomTypeItemList;

    @FXML
    private JFXButton btnCancelRoomTypeItemList;

    private void initFormDataRoomTypeItemList() {
        initDataRoomTypeItemListPopup();
        
        btnSaveRoomTypeItemList.setTooltip(new Tooltip("Simpan (Barang)"));
        btnSaveRoomTypeItemList.setOnAction((e) -> {
            dataRoomTypeItemListSaveHandle();
        });

        btnCancelRoomTypeItemList.setTooltip(new Tooltip("Batal"));
        btnCancelRoomTypeItemList.setOnAction((e) -> {
            dataRoomTypeItemListCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpItem,
                txtItemQuantity);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }
    
    private void initDataRoomTypeItemListPopup() {
        //Item
        TableView<TblItem> tableItem = new TableView<>();

        TableColumn<TblItem, String> codeItem = new TableColumn<>("ID");
        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        codeItem.setMinWidth(120);

        TableColumn<TblItem, String> itemName = new TableColumn<>("Nama Barang");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemName.setMinWidth(140);

        TableColumn<TblItem, String> additionalCharge = new TableColumn("Biaya Tambahan");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getAdditionalCharge()), param.getValue().additionalChargeProperty()));
        additionalCharge.setMinWidth(140);

        TableColumn<TblItem, String> brokenCharge = new TableColumn("Biaya Kerusakan");
        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBrokenCharge()), param.getValue().brokenChargeProperty()));
        brokenCharge.setMinWidth(140);

        tableItem.getColumns().addAll(codeItem, itemName, additionalCharge, brokenCharge);

        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadDataItemsCanBeUsed());

        cbpItem = new JFXCComboBoxTablePopup<>(
                TblItem.class, tableItem, itemItems, "", "Barang *", true, 550, 400
        );
        
        //attached to anchor-pane
        ancItemLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpItem, 0.0);
        AnchorPane.setLeftAnchor(cbpItem, 0.0);
        AnchorPane.setRightAnchor(cbpItem, 0.0);
        AnchorPane.setTopAnchor(cbpItem, 0.0);
        ancItemLayout.getChildren().add(cbpItem);
    }

    private List<TblItem> loadDataItemsCanBeUsed() {
        List<TblItem> list = roomTypeController.getService().getAllDataItem();
        for (int i = list.size() - 1; i > -1; i--) {
            //data unit
            list.get(i).setTblUnit(roomTypeController.getService().getDataUnit(list.get(i).getTblUnit().getIdunit()));
            for (TblRoomTypeItem dataCantBeUsed : (List<TblRoomTypeItem>) roomTypeController.tableDataRoomTypeItemList.getTableView().getItems()) {
                if (list.get(i).getIditem() == dataCantBeUsed.getTblItem().getIditem()) {
                    if (roomTypeController.selectedDataRoomTypeItemList.getTblItem() != null) {
                        if (roomTypeController.selectedDataRoomTypeItemList.getTblItem().getIditem()
                                == dataCantBeUsed.getTblItem().getIditem()) {
                            break;
                        }
                        list.remove(i);
                        break;
                    } else {
                        list.remove(i);
                        break;
                    }
                }
            }
        }
        return list;
    }

    private void refreshDataPopup() {
        //Item
        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadDataItemsCanBeUsed());
        cbpItem.setItems(itemItems);
    }
    
    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        Bindings.bindBidirectional(txtItemQuantity.textProperty(), roomTypeController.selectedDataRoomTypeItemList.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());
        lblUnit.setText("");

        chbAddAsAdditionalItem.selectedProperty().bindBidirectional(roomTypeController.selectedDataRoomTypeItemList.addAsAdditionalItemProperty());
        
        cbpItem.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null
                    && newVal.getTblUnit() != null) {
                lblUnit.setText(newVal.getTblUnit().getUnitName());
            } else {
                lblUnit.setText("");
            }
        });
        cbpItem.valueProperty().bindBidirectional(roomTypeController.selectedDataRoomTypeItemList.tblItemProperty());

        cbpItem.hide();

//        if (roomTypeController.dataInputRoomTypeItemListStatus == 0) {     //insert data
//            txtItemQuantity.setDisable(false);
//        } else {      //update data
//            txtItemQuantity.setDisable(true);
//        }
    }

    private void dataRoomTypeItemListSaveHandle() {
        if (checkDataInputDataRoomTypeItemList()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomTypeController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (roomTypeController.dataInputRoomTypeItemListStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", roomTypeController.dialogStage);
                        roomTypeController.tableDataRoomTypeItemList.getTableView().getItems().add(roomTypeController.selectedDataRoomTypeItemList);
                        //close form data room type item list
                        roomTypeController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", roomTypeController.dialogStage);
                        roomTypeController.tableDataRoomTypeItemList.getTableView().getItems().remove(roomTypeController.tableDataRoomTypeItemList.getTableView().getSelectionModel().getSelectedItem());
                        roomTypeController.tableDataRoomTypeItemList.getTableView().getItems().add(roomTypeController.selectedDataRoomTypeItemList);
                        //close form data room type item list
                        roomTypeController.dialogStage.close();
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomTypeController.dialogStage);
        }
    }

    private void dataRoomTypeItemListCancelHandle() {
        //close form data room type item list
        roomTypeController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataRoomTypeItemList() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpItem.getValue() == null) {
            dataInput = false;
            errDataInput += "Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtItemQuantity.getText() == null 
                || txtItemQuantity.getText().equals("")
                || txtItemQuantity.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (roomTypeController.selectedDataRoomTypeItemList.getItemQuantity()
                    .compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Jumlah : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
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
        initFormDataRoomTypeItemList();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RoomTypeItemListController(RoomTypeController parentController) {
        roomTypeController = parentController;
    }

    private final RoomTypeController roomTypeController;

}
