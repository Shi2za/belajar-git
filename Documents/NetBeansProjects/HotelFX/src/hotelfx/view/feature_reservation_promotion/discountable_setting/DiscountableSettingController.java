/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_promotion.discountable_setting;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.view.feature_reservation_promotion.FeatureReservationPromotionController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class DiscountableSettingController implements Initializable {

    @FXML
    private AnchorPane ancFormDiscountableSetting;

    @FXML
    private GridPane gpFormDataDiscountableSetting;

    private TableView<TblRoomType> tableRoomType;

    private TableView<TblRoomService> tableRoomService;

    private TableView<TblItem> tableItem;

    @FXML
    private AnchorPane ancDataTableRoomTypeLayout;

    @FXML
    private AnchorPane ancDataTableRoomServiceLayout;

    @FXML
    private AnchorPane ancDataTableItemLayout;

    @FXML
    private JFXButton btnSave;

    private void initFormDataDiscountableSetting() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Pengaturan Diskon)"));
        btnSave.setOnAction((e) -> {
            discountableSettingSaveHandle();
        });

        //set data table
        initDataTable();
    }

    private void initDataTable() {
        //RoomType
        tableRoomType = new TableView();
        tableRoomType.setEditable(true);

        TableColumn<TblRoomType, String> roomTypeName = new TableColumn("Tipe Kamar");
        roomTypeName.setCellValueFactory(cellData -> cellData.getValue().roomTypeNameProperty());
        roomTypeName.setMinWidth(140);

        TableColumn<TblRoomType, Boolean> hotelDiscountableStatus1 = new TableColumn("Hotel");
        hotelDiscountableStatus1.setCellValueFactory(cellData -> cellData.getValue().hotelDiscountableProperty());
        hotelDiscountableStatus1.setCellFactory(CheckBoxTableCell.forTableColumn(hotelDiscountableStatus1));
        hotelDiscountableStatus1.setMinWidth(85);
        hotelDiscountableStatus1.setEditable(true);

        TableColumn<TblRoomType, Boolean> cardDiscountableStatus1 = new TableColumn("Kartu");
        cardDiscountableStatus1.setCellValueFactory(cellData -> cellData.getValue().cardDiscountableProperty());
        cardDiscountableStatus1.setCellFactory(CheckBoxTableCell.forTableColumn(cardDiscountableStatus1));
        cardDiscountableStatus1.setMinWidth(85);
        cardDiscountableStatus1.setEditable(true);

        TableColumn<TblRoomType, String> discountTitle1 = new TableColumn("Diskon");
        discountTitle1.getColumns().addAll(hotelDiscountableStatus1, cardDiscountableStatus1);

        tableRoomType.getColumns().addAll(roomTypeName, discountTitle1);
        tableRoomType.setItems(FXCollections.observableArrayList(loadAllDataRoomType()));

        //Room Service
        tableRoomService = new TableView();
        tableRoomService.setEditable(true);

        TableColumn<TblRoomService, String> codeRoomService = new TableColumn("ID");
        codeRoomService.setCellValueFactory(cellData -> cellData.getValue().codeRoomServiceProperty());
        codeRoomService.setMinWidth(90);

        TableColumn<TblRoomService, String> roomServiceName = new TableColumn("Layanan");
        roomServiceName.setCellValueFactory(cellData -> cellData.getValue().serviceNameProperty());
        roomServiceName.setMinWidth(125);

        TableColumn<TblRoomService, Boolean> hotelDiscountableStatus2 = new TableColumn("Hotel");
        hotelDiscountableStatus2.setCellValueFactory(cellData -> cellData.getValue().hotelDiscountableProperty());
        hotelDiscountableStatus2.setCellFactory(CheckBoxTableCell.forTableColumn(hotelDiscountableStatus2));
        hotelDiscountableStatus2.setMinWidth(60);
        hotelDiscountableStatus2.setEditable(true);

        TableColumn<TblRoomService, Boolean> cardDiscountableStatus2 = new TableColumn("Kartu");
        cardDiscountableStatus2.setCellValueFactory(cellData -> cellData.getValue().cardDiscountableProperty());
        cardDiscountableStatus2.setCellFactory(CheckBoxTableCell.forTableColumn(cardDiscountableStatus2));
        cardDiscountableStatus2.setMinWidth(60);
        cardDiscountableStatus2.setEditable(true);

        TableColumn<TblRoomService, String> discountTitle2 = new TableColumn("Diskon");
        discountTitle2.getColumns().addAll(hotelDiscountableStatus2, cardDiscountableStatus2);

        tableRoomService.getColumns().addAll(codeRoomService, roomServiceName, discountTitle2);
        tableRoomService.setItems(FXCollections.observableArrayList(loadAllDataRoomService()));

        //Item
        tableItem = new TableView();
        tableItem.setEditable(true);

        TableColumn<TblItem, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
        codeItem.setMinWidth(90);

        TableColumn<TblItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemName.setMinWidth(125);

        TableColumn<TblItem, Boolean> hotelDiscountableStatus3 = new TableColumn("Hotel");
        hotelDiscountableStatus3.setCellValueFactory(cellData -> cellData.getValue().hotelDiscountableProperty());
        hotelDiscountableStatus3.setCellFactory(CheckBoxTableCell.forTableColumn(hotelDiscountableStatus3));
        hotelDiscountableStatus3.setMinWidth(60);
        hotelDiscountableStatus3.setEditable(true);

        TableColumn<TblItem, Boolean> cardDiscountableStatus3 = new TableColumn("Kartu");
        cardDiscountableStatus3.setCellValueFactory(cellData -> cellData.getValue().cardDiscountableProperty());
        cardDiscountableStatus3.setCellFactory(CheckBoxTableCell.forTableColumn(cardDiscountableStatus3));
        cardDiscountableStatus3.setMinWidth(60);
        cardDiscountableStatus3.setEditable(true);

        TableColumn<TblItem, String> discountTitle3 = new TableColumn("Diskon");
        discountTitle3.getColumns().addAll(hotelDiscountableStatus3, cardDiscountableStatus3);

        tableItem.getColumns().addAll(codeItem, itemName, discountTitle3);
        tableItem.setItems(FXCollections.observableArrayList(loadAllDataItem()));

        //Attach Tabel to Layout
        AnchorPane.setBottomAnchor(tableRoomType, 5.0);
        AnchorPane.setTopAnchor(tableRoomType, 5.0);
        AnchorPane.setLeftAnchor(tableRoomType, 5.0);
        AnchorPane.setRightAnchor(tableRoomType, 5.0);
        ancDataTableRoomTypeLayout.getChildren().add(tableRoomType);

        AnchorPane.setBottomAnchor(tableRoomService, 5.0);
        AnchorPane.setTopAnchor(tableRoomService, 5.0);
        AnchorPane.setLeftAnchor(tableRoomService, 5.0);
        AnchorPane.setRightAnchor(tableRoomService, 5.0);
        ancDataTableRoomServiceLayout.getChildren().add(tableRoomService);

        AnchorPane.setBottomAnchor(tableItem, 5.0);
        AnchorPane.setTopAnchor(tableItem, 5.0);
        AnchorPane.setLeftAnchor(tableItem, 5.0);
        AnchorPane.setRightAnchor(tableItem, 5.0);
        ancDataTableItemLayout.getChildren().add(tableItem);
    }

    private List<TblRoomType> loadAllDataRoomType() {
        List<TblRoomType> list = parentController.getFReservationPromotionManager().getAllDataRoomType();
        for (TblRoomType data : list) {
            data.hotelDiscountableProperty().addListener((obs, oldVal, newVal) -> {
                boolean found = false;
                for (TblRoomType d : roomTypeUpdateds) {
                    if (data.getIdroomType() == d.getIdroomType()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    roomTypeUpdateds.add(data);
                    //set unsaving data input -> 'true'
                    ClassSession.unSavingDataInput.set(true);
                }
            });
            data.cardDiscountableProperty().addListener((obs, oldVal, newVal) -> {
                boolean found = false;
                for (TblRoomType d : roomTypeUpdateds) {
                    if (data.getIdroomType() == d.getIdroomType()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    roomTypeUpdateds.add(data);
                    //set unsaving data input -> 'true'
                    ClassSession.unSavingDataInput.set(true);
                }
            });
        }
        return list;
    }

    private List<TblRoomService> loadAllDataRoomService() {
        List<TblRoomService> list = parentController.getFReservationPromotionManager().getAllDataRoomService();
        for(int i=list.size()-1; i>-1; i--){
            if(list.get(i).getIdroomService() == 2  //Early CheckIn = '2'
                    || list.get(i).getIdroomService() == 3  //Late CheckOut = '3'
                    || list.get(i).getIdroomService() == 4    //Lainnya (Bonus Voucher) = '4'
                    || list.get(i).getIdroomService() == 5      //Canceling Fee = '5'
                    || list.get(i).getIdroomService() == 6      //Room Dining = '6'
                    || list.get(i).getIdroomService() == 7){    //Dine in Resto = '7'
                list.remove(i);
            }
        }
        for (TblRoomService data : list) {
            data.hotelDiscountableProperty().addListener((obs, oldVal, newVal) -> {
                boolean found = false;
                for (TblRoomService d : roomServiceUpdateds) {
                    if (data.getIdroomService() == d.getIdroomService()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    roomServiceUpdateds.add(data);
                    //set unsaving data input -> 'true'
                    ClassSession.unSavingDataInput.set(true);
                }
            });
            data.cardDiscountableProperty().addListener((obs, oldVal, newVal) -> {
                boolean found = false;
                for (TblRoomService d : roomServiceUpdateds) {
                    if (data.getIdroomService() == d.getIdroomService()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    roomServiceUpdateds.add(data);
                    //set unsaving data input -> 'true'
                    ClassSession.unSavingDataInput.set(true);
                }
            });
        }
        return list;
    }

    private List<TblItem> loadAllDataItem() {
        List<TblItem> list = parentController.getFReservationPromotionManager().getAllDataItem();
        long idRoomCard = -1;
        SysDataHardCode sdhIDGUestCard = parentController.getFReservationPromotionManager().getDataSysDataHardCode(1);   //IDGuestCard = '1'
        if(sdhIDGUestCard != null
                && sdhIDGUestCard.getDataHardCodeValue() != null){
            idRoomCard = Long.parseLong(sdhIDGUestCard.getDataHardCodeValue());
        }
        for(int i=list.size()-1; i>-1; i--){
            if(list.get(i).getIditem() == idRoomCard){    //Item : Room Card
                list.remove(i);
            }
        }
        for (TblItem data : list) {
            data.hotelDiscountableProperty().addListener((obs, oldVal, newVal) -> {
                boolean found = false;
                for (TblItem d : itemUpdateds) {
                    if (data.getIditem() == d.getIditem()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    itemUpdateds.add(data);
                    //set unsaving data input -> 'true'
                    ClassSession.unSavingDataInput.set(true);
                }
            });
            data.cardDiscountableProperty().addListener((obs, oldVal, newVal) -> {
                boolean found = false;
                for (TblItem d : itemUpdateds) {
                    if (data.getIditem() == d.getIditem()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    itemUpdateds.add(data);
                    //set unsaving data input -> 'true'
                    ClassSession.unSavingDataInput.set(true);
                }
            });
        }
        return list;
    }
    
    private List<TblRoomType> roomTypeUpdateds = new ArrayList<>();

    private List<TblRoomService> roomServiceUpdateds = new ArrayList<>();

    private List<TblItem> itemUpdateds = new ArrayList<>();

    private void setSelectedDataToInputForm() {

        roomTypeUpdateds = new ArrayList<>();
        roomServiceUpdateds = new ArrayList<>();
        itemUpdateds = new ArrayList<>();

        tableRoomType.setItems(FXCollections.observableArrayList(loadAllDataRoomType()));
        tableRoomService.setItems(FXCollections.observableArrayList(loadAllDataRoomService()));
        tableItem.setItems(FXCollections.observableArrayList(loadAllDataItem()));

    }

    private void discountableSettingSaveHandle() {
        if (!roomTypeUpdateds.isEmpty()
                || !roomServiceUpdateds.isEmpty()
                || !itemUpdateds.isEmpty()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                List<TblRoomType> dummyRoomTypeUpdates = new ArrayList<>();
                for (TblRoomType roomTypeUpdated : roomTypeUpdateds) {
                    TblRoomType dummyRoomTypeUpdate = new TblRoomType(roomTypeUpdated);
                    dummyRoomTypeUpdates.add(dummyRoomTypeUpdate);
                }
                List<TblRoomService> dummyRoomServiceUpdateds = new ArrayList<>();
                for (TblRoomService roomServiceUpdated : roomServiceUpdateds) {
                    TblRoomService dummyRoomServiceUpdated = new TblRoomService(roomServiceUpdated);
                    dummyRoomServiceUpdateds.add(dummyRoomServiceUpdated);
                }
                List<TblItem> dummyItemUpdateds = new ArrayList<>();
                for (TblItem itemUpdated : itemUpdateds) {
                    TblItem dummyItemUpdated = new TblItem(itemUpdated);
                    dummyItemUpdateds.add(dummyItemUpdated);
                }
                if (parentController.getFReservationPromotionManager().updateDataDiscountable(dummyRoomTypeUpdates,
                        dummyRoomServiceUpdateds,
                        dummyItemUpdateds)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh data input
                    setSelectedDataToInputForm();
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(parentController.getFReservationPromotionManager().getErrorMessage(), null);
                }
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK ADA PERUBAHAN DATA", "Tidak ada data yang harus disimpan..!", null);
        }
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
        initFormDataDiscountableSetting();
        //refresh data form input
        setSelectedDataToInputForm();
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public DiscountableSettingController(FeatureReservationPromotionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReservationPromotionController parentController;

}
