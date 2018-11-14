/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room.room_type;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomTypeRoomService;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
public class RoomTypeRoomServiceListController implements Initializable {

    @FXML
    private AnchorPane ancFormRoomTypeRoomServiceList;

    @FXML
    private GridPane gpFormDataRoomTypeRoomServiceList;

    @FXML
    private AnchorPane ancRoomServiceLayout;

    private JFXCComboBoxTablePopup<TblRoomService> cbpRoomService;

    @FXML
    private JFXCheckBox chbUsedPeopleNumber;

    @FXML
    private Spinner<Integer> spPeopleNumber;

    @FXML
    private JFXCheckBox chbAddAsAdditionalService;
    
    @FXML
    private JFXButton btnSaveRoomTypeRoomServiceList;

    @FXML
    private JFXButton btnCancelRoomTypeRoomServiceList;

    private void initFormDataRoomTypeRoomServiceList() {
        initDataRoomTypeRoomServiceListPopup();

        btnSaveRoomTypeRoomServiceList.setTooltip(new Tooltip("Simpan (Data Layanan)"));
        btnSaveRoomTypeRoomServiceList.setOnAction((e) -> {
            dataRoomTypeRoomServiceListSaveHandle();
        });

        btnCancelRoomTypeRoomServiceList.setTooltip(new Tooltip("Batal"));
        btnCancelRoomTypeRoomServiceList.setOnAction((e) -> {
            dataRoomTypeRoomServiceListCancelHandle();
        });

        spPeopleNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spPeopleNumber.setEditable(false);
        spPeopleNumber.setVisible(false);

        chbUsedPeopleNumber.selectedProperty().addListener((obs, oldVal, newVal) -> {
            spPeopleNumber.setVisible(newVal);
            if(!newVal){
                spPeopleNumber.getValueFactory().setValue(0);
            }
        });
        chbUsedPeopleNumber.setSelected(false);

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpRoomService,
                spPeopleNumber);
    }

    private void initDataRoomTypeRoomServiceListPopup() {
        //room service
        TableView<TblRoomService> tableRoomService = new TableView<>();

        TableColumn<TblRoomService, String> codeRoomService = new TableColumn<>("ID");
        codeRoomService.setCellValueFactory(cellData -> cellData.getValue().codeRoomServiceProperty());
        codeRoomService.setMinWidth(120);

        TableColumn<TblRoomService, String> roomServiceName = new TableColumn<>("Layanan");
        roomServiceName.setCellValueFactory(cellData -> cellData.getValue().serviceNameProperty());
        roomServiceName.setMinWidth(140);

        TableColumn<TblRoomService, String> roomServicePrice = new TableColumn("Harga");
        roomServicePrice.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()), param.getValue().priceProperty()));
        roomServicePrice.setMinWidth(140);

        tableRoomService.getColumns().addAll(codeRoomService, roomServiceName, roomServicePrice);

        ObservableList<TblRoomService> roomServiceItems = FXCollections.observableArrayList(loadDataRoomServicesCanBeUsed());

        cbpRoomService = new JFXCComboBoxTablePopup<>(
                TblRoomService.class, tableRoomService, roomServiceItems, "", "Layanan *", true, 420, 200
        );
        
        //attached to anchor-pane
        ancRoomServiceLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpRoomService, 0.0);
        AnchorPane.setLeftAnchor(cbpRoomService, 0.0);
        AnchorPane.setRightAnchor(cbpRoomService, 0.0);
        AnchorPane.setTopAnchor(cbpRoomService, 0.0);
        ancRoomServiceLayout.getChildren().add(cbpRoomService);
    }

    private List<TblRoomService> loadDataRoomServicesCanBeUsed() {
        List<TblRoomService> list = roomTypeController.getService().getAllDataRoomService();
        for (int i = list.size() - 1; i > -1; i--) {
            for (TblRoomTypeRoomService dataCantBeUsed : (List<TblRoomTypeRoomService>) roomTypeController.tableDataRoomTypeRoomServiceList.getTableView().getItems()) {
                if (list.get(i).getIdroomService() == dataCantBeUsed.getTblRoomService().getIdroomService()) {
                    if (roomTypeController.selectedDataRoomTypeRoomServiceList.getTblRoomService() != null) {
                        if (roomTypeController.selectedDataRoomTypeRoomServiceList.getTblRoomService().getIdroomService()
                                == dataCantBeUsed.getTblRoomService().getIdroomService()) {
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
        //Room Service
        ObservableList<TblRoomService> roomServiceItems = FXCollections.observableArrayList(loadDataRoomServicesCanBeUsed());
        cbpRoomService.setItems(roomServiceItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        chbUsedPeopleNumber.setSelected(roomTypeController.selectedDataRoomTypeRoomServiceList.getPeopleNumber() > 0);

        spPeopleNumber.getValueFactory().setValue(roomTypeController.selectedDataRoomTypeRoomServiceList.getPeopleNumber());
        spPeopleNumber.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                roomTypeController.selectedDataRoomTypeRoomServiceList.setPeopleNumber(newVal);
            }
        });

        chbAddAsAdditionalService.selectedProperty().bindBidirectional(roomTypeController.selectedDataRoomTypeRoomServiceList.addAsAdditionalServiceProperty());
        
        cbpRoomService.valueProperty().bindBidirectional(roomTypeController.selectedDataRoomTypeRoomServiceList.tblRoomServiceProperty());

        cbpRoomService.hide();

    }

    private void dataRoomTypeRoomServiceListSaveHandle() {
        if (checkDataInputDataRoomTypeRoomServiceList()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", roomTypeController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                switch (roomTypeController.dataInputRoomTypeRoomServiceListStatus) {
                    case 0:
                        ClassMessage.showSucceedAddingDataMessage("", roomTypeController.dialogStage);
                        roomTypeController.tableDataRoomTypeRoomServiceList.getTableView().getItems().add(roomTypeController.selectedDataRoomTypeRoomServiceList);
                        //close form data room type room service list
                        roomTypeController.dialogStage.close();
                        break;
                    case 1:
                        ClassMessage.showSucceedUpdatingDataMessage("", roomTypeController.dialogStage);
                        roomTypeController.tableDataRoomTypeRoomServiceList.getTableView().getItems().remove(roomTypeController.tableDataRoomTypeRoomServiceList.getTableView().getSelectionModel().getSelectedItem());
                        roomTypeController.tableDataRoomTypeRoomServiceList.getTableView().getItems().add(roomTypeController.selectedDataRoomTypeRoomServiceList);
                        //close form data room type room service list
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

    private void dataRoomTypeRoomServiceListCancelHandle() {
        //close form data room type room service list
        roomTypeController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataRoomTypeRoomServiceList() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpRoomService.getValue() == null) {
            dataInput = false;
            errDataInput += "Layanan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (chbUsedPeopleNumber.isSelected()) {
            if (spPeopleNumber.getValue() == null) {
                dataInput = false;
                errDataInput += "Jumlah Orang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (spPeopleNumber.getValue() <= 0) {
                    dataInput = false;
                    errDataInput += "Jumlah Orang : harus lebih besar dari pada '0' \n";
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
        initFormDataRoomTypeRoomServiceList();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RoomTypeRoomServiceListController(RoomTypeController parentController) {
        roomTypeController = parentController;
    }

    private final RoomTypeController roomTypeController;

}
