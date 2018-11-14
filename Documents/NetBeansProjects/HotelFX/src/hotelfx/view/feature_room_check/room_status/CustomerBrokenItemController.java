/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
public class CustomerBrokenItemController implements Initializable {

    @FXML
    private AnchorPane ancFormDataCustomerBrokenItem;

    @FXML
    private GridPane gpFormDataCustomerBrokenItem;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextField txtCodeReservation;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private AnchorPane ancClassItemLocationLayout;
    private JFXCComboBoxTablePopup<ClassItemLocation> cbpClassItemLocation;

    @FXML
    private JFXTextField txtPropertyBarcode;

    @FXML
    private JFXTextField txtExpiredDate;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private Label lblUnitName;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCustomerBrokenItem() {
        initDataPopup();

        btnSave.setTooltip(new Tooltip("Simpan (Kerusakan Barang oleh Customer)"));
        btnSave.setOnAction((e) -> {
            dataCustomerBrokenItemSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCustomerBrokenItemCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpClassItemLocation,
                txtItemQuantity);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtItemQuantity);
    }

    private void initDataPopup() {
        //Class - Item Location
        TableView<ClassItemLocation> tableClassItemLocation = new TableView<>();

        TableColumn<ClassItemLocation, String> codeItem = new TableColumn<>("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<ClassItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataItemLocation().getTblItem().getCodeItem(),
                        param.getValue().dataItemLocationProperty()));
        codeItem.setMinWidth(100);

        TableColumn<ClassItemLocation, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<ClassItemLocation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataItemLocation().getTblItem().getItemName(),
                        param.getValue().dataItemLocationProperty()));
        itemName.setMinWidth(140);

        TableColumn<ClassItemLocation, String> codeBarcode = new TableColumn<>("Barcode");
        codeBarcode.setCellValueFactory((TableColumn.CellDataFeatures<ClassItemLocation, String> param)
                -> Bindings.createStringBinding(() -> getDataBarcode(param.getValue()),
                        param.getValue().dataItemLocationPropertyBarcodeProperty()));
        codeBarcode.setMinWidth(120);

        TableColumn<ClassItemLocation, String> expiredDate = new TableColumn<>("Tgl. Kadarluarsa");
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<ClassItemLocation, String> param)
                -> Bindings.createStringBinding(() -> getDataExpiredDate(param.getValue()),
                        param.getValue().dataItemLocationItemExpiredDateProperty()));
        expiredDate.setMinWidth(120);

        tableClassItemLocation.getColumns().addAll(codeItem, itemName, codeBarcode);

        ObservableList<ClassItemLocation> classItemLocationItems = FXCollections.observableArrayList(loadAllDataClassItemLocation());

        cbpClassItemLocation = new JFXCComboBoxTablePopup<>(
                ClassItemLocation.class, tableClassItemLocation, classItemLocationItems, "", "Barang *", true, 380, 200
        );

        //attached to grid-pane
        ancClassItemLocationLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpClassItemLocation, 0.0);
        AnchorPane.setLeftAnchor(cbpClassItemLocation, 0.0);
        AnchorPane.setRightAnchor(cbpClassItemLocation, 0.0);
        AnchorPane.setTopAnchor(cbpClassItemLocation, 0.0);
        ancClassItemLocationLayout.getChildren().add(cbpClassItemLocation);
    }

    private String getDataBarcode(ClassItemLocation classItemLocation) {
        if (classItemLocation != null
                && classItemLocation.getDataItemLocationPropertyBarcode() != null
                && classItemLocation.getDataItemLocationPropertyBarcode().getTblPropertyBarcode() != null) {
            return classItemLocation.getDataItemLocationPropertyBarcode().getTblPropertyBarcode().getCodeBarcode();
        }
        return "-";
    }

    private String getDataExpiredDate(ClassItemLocation classItemLocation) {
        if (classItemLocation != null
                && classItemLocation.getDataItemLocationItemExpiredDate() != null
                && classItemLocation.getDataItemLocationItemExpiredDate().getTblItemExpiredDate() != null
                && classItemLocation.getDataItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate() != null) {
            return ClassFormatter.dateFormate.format(classItemLocation.getDataItemLocationItemExpiredDate().getTblItemExpiredDate().getItemExpiredDate());
        }
        return "-";
    }

    private List<ClassItemLocation> loadAllDataClassItemLocation() {
        List<ClassItemLocation> list = new ArrayList<>();
        TblLocation usedLocation = roomStatusController.getService().getDataLocationByIDLocationTypeAndFirstData(6);    //Digunakan = '6'
        if (usedLocation != null) {
            List<TblItemLocation> itemLocations = roomStatusController.getService().getAllDataItemLocationByIDLocation(usedLocation.getIdlocation());
            for (TblItemLocation itemLocation : itemLocations) {
                //data location
                itemLocation.setTblLocation(usedLocation);
                //data item
                itemLocation.setTblItem(roomStatusController.getService().getDataItem(itemLocation.getTblItem().getIditem()));
                if (itemLocation.getTblItem().getPropertyStatus()) {  //property
                    List<TblItemLocationPropertyBarcode> itemLocationPropertyBarcodes = roomStatusController.getService().getAllDataItemLocationPropertyBarcodeByIDItemLocation(itemLocation.getIdrelation());
                    for (TblItemLocationPropertyBarcode itemLocationPropertyBarcode : itemLocationPropertyBarcodes) {
                        //data item location
                        itemLocationPropertyBarcode.setTblItemLocation(itemLocation);
                        //data property barcode
                        itemLocationPropertyBarcode.setTblPropertyBarcode(roomStatusController.getService().getDataPropertyBarcode(itemLocationPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                        //data class item location
                        list.add(new ClassItemLocation(itemLocation, itemLocationPropertyBarcode, null));
                    }
                } else {
                    if (itemLocation.getTblItem().getConsumable()) {  //consumable
                        List<TblItemLocationItemExpiredDate> itemLocationItemExpiredDates = roomStatusController.getService().getAllDataItemLocationItemExpiredDateByIDItemLocation(itemLocation.getIdrelation());
                        for (TblItemLocationItemExpiredDate itemLocationItemExpiredDate : itemLocationItemExpiredDates) {
                            //data item location
                            itemLocationItemExpiredDate.setTblItemLocation(itemLocation);
                            //data item expired date
                            itemLocationItemExpiredDate.setTblItemExpiredDate(roomStatusController.getService().getDataItemExpiredDate(itemLocationItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
//                            //data class item location
//                        list.add(new ClassItemLocation(itemLocation, null, itemLocationItemExpiredDate));
                        }
                    } else {  //another
                        //do nothing...
                        //data class item location
                        list.add(new ClassItemLocation(itemLocation, null, null));
                    }
                }
            }
        }

        return list;
    }

    private void refreshDataPopup() {
        //Class - Item Location
        ObservableList<ClassItemLocation> classItemLocationItems = FXCollections.observableArrayList(loadAllDataClassItemLocation());
        cbpClassItemLocation.setItems(classItemLocationItems);
    }

    public class ClassItemLocation {

        private final ObjectProperty<TblItemLocation> dataItemLocation = new SimpleObjectProperty<>();

        private final ObjectProperty<TblItemLocationPropertyBarcode> dataItemLocationPropertyBarcode = new SimpleObjectProperty<>();

        private final ObjectProperty<TblItemLocationItemExpiredDate> dataItemLocationItemExpiredDate = new SimpleObjectProperty<>();

        public ClassItemLocation(
                TblItemLocation dataItemLocation,
                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode,
                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate) {
            this.dataItemLocation.set(dataItemLocation);
            this.dataItemLocationPropertyBarcode.set(dataItemLocationPropertyBarcode);
            this.dataItemLocationItemExpiredDate.set(dataItemLocationItemExpiredDate);
        }

        public ObjectProperty<TblItemLocation> dataItemLocationProperty() {
            return dataItemLocation;
        }

        public TblItemLocation getDataItemLocation() {
            return dataItemLocationProperty().get();
        }

        public void setDataItemLocation(TblItemLocation dataItemLocation) {
            dataItemLocationProperty().set(dataItemLocation);
        }

        public ObjectProperty<TblItemLocationPropertyBarcode> dataItemLocationPropertyBarcodeProperty() {
            return dataItemLocationPropertyBarcode;
        }

        public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcode() {
            return dataItemLocationPropertyBarcodeProperty().get();
        }

        public void setDataItemLocationPropertyBarcode(TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode) {
            dataItemLocationPropertyBarcodeProperty().set(dataItemLocationPropertyBarcode);
        }

        public ObjectProperty<TblItemLocationItemExpiredDate> dataItemLocationItemExpiredDateProperty() {
            return dataItemLocationItemExpiredDate;
        }

        public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDate() {
            return dataItemLocationItemExpiredDateProperty().get();
        }

        public void setDataItemLocationItemExpiredDate(TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate) {
            dataItemLocationItemExpiredDateProperty().set(dataItemLocationItemExpiredDate);
        }

        @Override
        public String toString(){
            return getDataItemLocation().getTblItem().getItemName();
        }
        
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtRoomName.setText(roomStatusController.selectedDataBrokenItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName());
        txtCodeReservation.setText(roomStatusController.selectedDataBrokenItem.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation());
        txtCustomerName.setText(roomStatusController.selectedDataBrokenItem.getTblReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getFullName());

        txtItemQuantity.textProperty().bindBidirectional(roomStatusController.selectedDataBrokenItem.itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

        cbpClassItemLocation.valueProperty().addListener((obs, oldVal, newVal) -> {
            txtItemQuantity.setDisable(false);
            if (newVal != null) {
                txtPropertyBarcode.setText(getDataBarcode(newVal));
                txtExpiredDate.setText(getDataExpiredDate(newVal));
                lblUnitName.setText(newVal.getDataItemLocation().getTblItem().getTblUnit().getUnitName());
                if (newVal.getDataItemLocationPropertyBarcode() != null) {    //property barcode
                    roomStatusController.selectedDataBrokenItem.setItemQuantity(new BigDecimal("1"));
                    txtItemQuantity.setDisable(true);
                }
            } else {
                txtPropertyBarcode.setText("-");
                txtExpiredDate.setText("-");
                lblUnitName.setText("");
            }
        });
        cbpClassItemLocation.hide();
    }

    private void dataCustomerBrokenItemSaveHandle() {
        if (checkDataInputDataCustomerBrokenItem()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomStatusController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                roomStatusController.selectedDataBrokenItem.setTblItem(cbpClassItemLocation.getValue().getDataItemLocation().getTblItem());
                //dummy entry
                TblReservationBrokenItem dummyReservationBrokenItem = new TblReservationBrokenItem(roomStatusController.selectedDataBrokenItem);
                dummyReservationBrokenItem.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummyReservationBrokenItem.getTblReservationRoomTypeDetail()));
                dummyReservationBrokenItem.setTblItem(new TblItem(dummyReservationBrokenItem.getTblItem()));
                ClassItemLocation dummyClassItemLocation = new ClassItemLocation(
                        new TblItemLocation(cbpClassItemLocation.getValue().getDataItemLocation()),
                        cbpClassItemLocation.getValue().getDataItemLocationPropertyBarcode() != null ? new TblItemLocationPropertyBarcode(cbpClassItemLocation.getValue().getDataItemLocationPropertyBarcode()) : null,
                        cbpClassItemLocation.getValue().getDataItemLocationItemExpiredDate() != null ? new TblItemLocationItemExpiredDate(cbpClassItemLocation.getValue().getDataItemLocationItemExpiredDate()) : null);
                dummyClassItemLocation.getDataItemLocation().setTblItem(new TblItem(dummyClassItemLocation.getDataItemLocation().getTblItem()));
                dummyClassItemLocation.getDataItemLocation().setTblLocation(new TblLocation(dummyClassItemLocation.getDataItemLocation().getTblLocation()));
                if (dummyClassItemLocation.getDataItemLocationPropertyBarcode() != null) {
                    dummyClassItemLocation.getDataItemLocationPropertyBarcode().setTblItemLocation(dummyClassItemLocation.getDataItemLocation());
                    dummyClassItemLocation.getDataItemLocationPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(dummyClassItemLocation.getDataItemLocationPropertyBarcode().getTblPropertyBarcode()));
                }
                if (dummyClassItemLocation.getDataItemLocationItemExpiredDate() != null) {
                    dummyClassItemLocation.getDataItemLocationItemExpiredDate().setTblItemLocation(dummyClassItemLocation.getDataItemLocation());
                    dummyClassItemLocation.getDataItemLocationItemExpiredDate().setTblItemExpiredDate(new TblItemExpiredDate(dummyClassItemLocation.getDataItemLocationItemExpiredDate().getTblItemExpiredDate()));
                }
                if (roomStatusController.getService().insertDataReservationBrokenItem(
                        dummyReservationBrokenItem,
                        dummyClassItemLocation) != null) {
                    ClassMessage.showSucceedInsertingDataMessage("", roomStatusController.dialogStage);
                    // refresh all data and close form data - customer broken item
                    roomStatusController.setSelectedDataToInputForm();
                    roomStatusController.dialogStage.close();
                } else {
                    ClassMessage.showFailedInsertingDataMessage(roomStatusController.getService().getErrorMessage(), roomStatusController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusController.dialogStage);
        }
    }

    private void dataCustomerBrokenItemCancelHandle() {
        //refresh all data and close form data - customer broken item
        roomStatusController.setSelectedDataToInputForm();
        roomStatusController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCustomerBrokenItem() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpClassItemLocation.getValue() == null) {
            dataInput = false;
            errDataInput += "Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (txtItemQuantity.getText() == null
                    || txtItemQuantity.getText().equals("")
                    || txtItemQuantity.getText().equals("-")) {
                errDataInput += "Jumlah Barang Rusak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (roomStatusController.selectedDataBrokenItem.getItemQuantity()
                        .compareTo(new BigDecimal("0")) < 1) {
                    errDataInput += "Jumlah Barang Rusak : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
                } else {
                    if (cbpClassItemLocation.getValue().getDataItemLocation().getItemQuantity()
                            .compareTo(new BigDecimal("0")) != 0) {
                        if (cbpClassItemLocation.getValue().getDataItemLocationPropertyBarcode() != null) { //property
                            if (roomStatusController.selectedDataBrokenItem.getItemQuantity()
                                    .compareTo(new BigDecimal("1")) == 1) {
                                errDataInput += "Stok barang tidak mencukupi!! \n";
                            }
                        } else {
                            if (cbpClassItemLocation.getValue().getDataItemLocationItemExpiredDate() != null) { //consumable
                                if (roomStatusController.selectedDataBrokenItem.getItemQuantity()
                                        .compareTo(cbpClassItemLocation.getValue().getDataItemLocationItemExpiredDate().getItemQuantity()) == 1) {
                                    errDataInput += "Stok barang tidak mencukupi!! \n";
                                }
                            } else {
                                if (roomStatusController.selectedDataBrokenItem.getItemQuantity()
                                        .compareTo(cbpClassItemLocation.getValue().getDataItemLocation().getItemQuantity()) == 1) {
                                    errDataInput += "Stok barang tidak mencukupi!! \n";
                                }
                            }
                        }
                    } else {
                        errDataInput += "Stok barang habis";
                    }
                }
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
        initFormDataCustomerBrokenItem();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public CustomerBrokenItemController(RoomStatusController parentController) {
        roomStatusController = parentController;
    }

    private final RoomStatusController roomStatusController;

}
