/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room.room_type;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblRoomTypeItem;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.converter.BigDecimalStringConverter;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RoomTypeAmenityListController implements Initializable {

//    @FXML
//    private AnchorPane ancFormRoomTypeAmenityList;
//
//    @FXML
//    private GridPane gpFormDataRoomTypeAmenityList;
//
//    @FXML
//    private AnchorPane ancItemLayout;
//
//    @FXML
//    private JFXTextField txtItemQuantity;
//
//    private final JFXCComboBoxPopup<TblItem> cbpItem = new JFXCComboBoxPopup<>(TblItem.class);
//
//    @FXML
//    private JFXButton btnSaveRoomTypeAmenityList;
//
//    @FXML
//    private JFXButton btnCancelRoomTypeAmenityList;
//
//    private void initFormDataRoomTypeAmenityList() {
//
//        btnSaveRoomTypeAmenityList.setTooltip(new Tooltip("Simpan (Data Amenity)"));
//        btnSaveRoomTypeAmenityList.setOnAction((e) -> {
//            dataRoomTypeAmenityListSaveHandle();
//        });
//
//        btnCancelRoomTypeAmenityList.setTooltip(new Tooltip("Batal"));
//        btnCancelRoomTypeAmenityList.setOnAction((e) -> {
//            dataRoomTypeAmenityListCancelHandle();
//        });
//
//        initDataRoomTypeAmenityListPopup();
//
//    }
//
//    private void initDataRoomTypeAmenityListPopup() {
//        //Item
//        TableView<TblItem> tableItem = new TableView<>();
//        
//        TableColumn<TblItem, String> codeItem = new TableColumn<>("ID");
//        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
//        codeItem.setMinWidth(120);
//        
//        TableColumn<TblItem, String> itemName = new TableColumn<>("Nama Barang");
//        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
//        itemName.setMinWidth(140);
//        
//        TableColumn<TblItem, String> additionalCharge = new TableColumn("Biaya Tambahan");
//        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getAdditionalCharge()), param.getValue().additionalChargeProperty()));
//        additionalCharge.setMinWidth(140);
//        
//        TableColumn<TblItem, String> brokenCharge = new TableColumn("Biaya Kerusakan");
//        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getBrokenCharge()), param.getValue().brokenChargeProperty()));
//        brokenCharge.setMinWidth(140);
//
//        tableItem.getColumns().addAll(codeItem, itemName, additionalCharge, brokenCharge);
//
//        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadDataItemsCanBeUsed());
//
//        setFunctionPopup(cbpItem, tableItem, itemItems, "itemName", "Amenity *");
//
//        //attached to anchor-pane
//        ancItemLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(cbpItem, 0.0);
//        AnchorPane.setLeftAnchor(cbpItem, 0.0);
//        AnchorPane.setRightAnchor(cbpItem, 0.0);
//        AnchorPane.setTopAnchor(cbpItem, 0.0);
//        ancItemLayout.getChildren().add(cbpItem);
//    }
//
//    private List<TblItem> loadDataItemsCanBeUsed() {
//        List<TblItem> list = roomTypeController.getService().getAllDataItemAmenity();
//        for (int i = list.size() - 1; i > -1; i--) {
//            for (TblRoomTypeItem dataCantBeUsed : (List<TblRoomTypeItem>) roomTypeController.tableDataRoomTypeAmenityList.getTableView().getItems()) {
//                if (list.get(i).getIditem() == dataCantBeUsed.getTblItem().getIditem()) {
//                    if (roomTypeController.selectedDataRoomTypeAmenityList.getTblItem() != null) {
//                        if (roomTypeController.selectedDataRoomTypeAmenityList.getTblItem().getIditem()
//                                == dataCantBeUsed.getTblItem().getIditem()) {
//                            break;
//                        }
//                        list.remove(i);
//                        break;
//                    } else {
//                        list.remove(i);
//                        break;
//                    }
//                }
//            }
//        }
//        return list;
//    }
//
//    private void refreshDataPopup() {
//        //Item
//        ObservableList<TblItem> itemItems = FXCollections.observableArrayList(loadDataItemsCanBeUsed());
//        cbpItem.setItems(itemItems);
//    }
//
//    private void setFunctionPopup(JFXCComboBoxPopup cbp,
//            TableView table,
//            ObservableList items,
//            String nameFiltered,
//            String promptText) {
//        table.getSelectionModel().selectedIndexProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    if (newValue.intValue() != -1) {
//                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
//                    }
//                    cbp.hide();
//                });
//
//        cbp.setPropertyNameForFiltered(nameFiltered);
//
//        cbp.setItems(items);
//    
//    cbp.setOnShowing((e) -> {
//            table.getSelectionModel().clearSelection();
//        });
//
//        // Add observable list data to the table
//        table.itemsProperty().bind(cbp.filteredItemsProperty());
//
//        //popup button
//        JFXButton button = new JFXButton("▾");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(550, 400);
//
//        content.setCenter(table);
//
//        cbp.setPopupEditor(true);
//        cbp.promptTextProperty().set(promptText);
//        cbp.setLabelFloat(true);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
//
//    private void setSelectedDataToInputForm() {
//
//        refreshDataPopup();
//
//        Bindings.bindBidirectional(txtItemQuantity.textProperty(), roomTypeController.selectedDataRoomTypeAmenityList.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());
//
//        cbpItem.valueProperty().bindBidirectional(roomTypeController.selectedDataRoomTypeAmenityList.tblItemProperty());
//
//        cbpItem.hide();
//
//        if (roomTypeController.dataInputRoomTypeAmenityListStatus == 0) {     //insert data
//            txtItemQuantity.setDisable(false);
//        } else {      //update data
//            txtItemQuantity.setDisable(true);
//        }
//    }
//
//    private void dataRoomTypeAmenityListSaveHandle() {
//        if (checkDataInputDataRoomTypeAmenityList()) {
//            switch (roomTypeController.dataInputRoomTypeAmenityListStatus) {
//                case 0:
//                    ClassMessage.showSucceedAddingDataMessage("", roomTypeController.dialogStage);
//                    roomTypeController.tableDataRoomTypeAmenityList.getTableView().getItems().add(roomTypeController.selectedDataRoomTypeAmenityList);
//                    //close form data room type amenity list
//                    roomTypeController.dialogStage.close();
//                    break;
//                case 1:
//                    ClassMessage.showSucceedUpdatingDataMessage("", roomTypeController.dialogStage);
//                    roomTypeController.tableDataRoomTypeAmenityList.getTableView().getItems().remove(roomTypeController.tableDataRoomTypeAmenityList.getTableView().getSelectionModel().getSelectedItem());
//                    roomTypeController.tableDataRoomTypeAmenityList.getTableView().getItems().add(roomTypeController.selectedDataRoomTypeAmenityList);
//                    //close form data room type amenity list
//                    roomTypeController.dialogStage.close();
//                    break;
//                default:
//                    break;
//            }
//        } else {
//            ClassMessage.showWarningInputDataMessage(errDataInput, roomTypeController.dialogStage);
//        }
//    }
//
//    private void dataRoomTypeAmenityListCancelHandle() {
//        //close form data room type amenity list
//        roomTypeController.dialogStage.close();
//    }
//
//    private String errDataInput;
//
//    private boolean checkDataInputDataRoomTypeAmenityList() {
//        boolean dataInput = true;
//        errDataInput = "";
//        if (cbpItem.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Amenity : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (txtItemQuantity.getText() == null || txtItemQuantity.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Jumlah : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        } else {
//            if (roomTypeController.selectedDataRoomTypeAmenityList.getItemQuantity()
//            .compareTo(new BigDecimal("0")) < 1) {
//                dataInput = false;
//                errDataInput += "Jumlah : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
//            }
//        }
//        return dataInput;
//    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        //init form input
//        initFormDataRoomTypeAmenityList();
//        //refresh data form input
//        setSelectedDataToInputForm();
    }

    public RoomTypeAmenityListController(RoomTypeController parentController) {
        roomTypeController = parentController;
    }

    private final RoomTypeController roomTypeController;

}
