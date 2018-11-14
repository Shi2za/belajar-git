/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_lostfound_information.found_information;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
import hotelfx.persistence.model.TblLostInformation;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
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
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class FoundInformationReturnDialogPopupController implements Initializable {

    @FXML
    private AnchorPane cbpLostInformationLayout;
    @FXML
    private JFXDatePicker dateReturn;
    @FXML
    private JFXTextField txtItemNameReturn;
    @FXML
    private JFXTextArea txtDetailItemReturn;
    @FXML
    private JFXTextField txtReturnName;
    @FXML
    private JFXTextArea txtReturnAddress;
    @FXML
    private JFXTextField txtPhoneNumber;
    @FXML
    private JFXDatePicker dateFound;
    @FXML
    private JFXTextField txtItemFound;
    @FXML
    private JFXTextArea txtItemDetailFound;
    @FXML
    private JFXTextField txtFounderName;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    private JFXCComboBoxTablePopup<TblLostInformation> cbpLostInformation;

    private TblLostFoundInformationDetail selectedDataReturn;

    private void initFormFoundInformationReturn() {
        
        initDataPopupLayout();
        
        initDateCalendar();

        btnSave.setOnAction((e) -> {
            dataFoundInformationReturnSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
            dataFoundInformationReturnCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpLostInformation,
                dateReturn,
                txtReturnName,
                txtReturnAddress,
                txtPhoneNumber);
    }

    private void initDataPopupLayout() {
        initDataPopup();
        cbpLostInformationLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpLostInformation, 0.0);
        AnchorPane.setBottomAnchor(cbpLostInformation, 0.0);
        AnchorPane.setLeftAnchor(cbpLostInformation, 0.0);
        AnchorPane.setRightAnchor(cbpLostInformation, 0.0);
        cbpLostInformationLayout.getChildren().add(cbpLostInformation);
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern, dateReturn, dateFound);
    }

    private void initDataPopup() {
        TableView<TblLostInformation> tblLostInformation = new TableView();
        TableColumn<TblLostInformation, String> codeLost = new TableColumn("ID");
        codeLost.setMinWidth(90);
        codeLost.setCellValueFactory(cellData -> cellData.getValue().codeLostProperty());
        TableColumn<TblLostInformation, String> dateLost = new TableColumn("Tanggal \n Lapor");
        dateLost.setMinWidth(100);
        dateLost.setCellValueFactory((TableColumn.CellDataFeatures<TblLostInformation, String> param)
                -> Bindings.createStringBinding(() -> new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getLostDate()), param.getValue().lostDateProperty()));
        TableColumn<TblLostInformation, String> locationLost = new TableColumn("Tempat Kehilangan");
        locationLost.setMinWidth(140);
        locationLost.setCellValueFactory(cellData -> cellData.getValue().lostLocationProperty());
        TableColumn<TblLostInformation, String> nameItem = new TableColumn("Nama Barang");
        nameItem.setMinWidth(160);
        nameItem.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        TableColumn<TblLostInformation, String> namePeople = new TableColumn("Nama Pelapor");
        namePeople.setMinWidth(160);
        namePeople.setCellValueFactory((TableColumn.CellDataFeatures<TblLostInformation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().tblPeopleProperty()));
        TableColumn<TblLostInformation, String> detailItem = new TableColumn("Keterangan");
        detailItem.setMinWidth(200);
        detailItem.setCellValueFactory(cellData -> cellData.getValue().lostNoteProperty());
        tblLostInformation.getColumns().addAll(codeLost, dateLost, locationLost, nameItem, namePeople, detailItem);

        ObservableList<TblLostInformation> lostInformationItem = FXCollections.observableArrayList(loadAllDataLostInformation());

        cbpLostInformation = new JFXCComboBoxTablePopup<>(
                TblLostInformation.class, tblLostInformation, lostInformationItem, "", "ID Barang Hilang *", true, 900, 300
        );
        
    }

    private List<TblLostInformation> loadAllDataLostInformation() {
        List<TblLostInformation> listLost = new ArrayList();
        listLost = foundInformationController.getService().getAllDataLostInformationReturn();
        return listLost;
    }

    private void setSelectedDataToInputForm() {
        //dateReturn
        dateReturn.valueProperty().addListener((obs, oldVal, newVal) -> {
            selectedDataReturn.setReturnDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        });

        cbpLostInformation.valueProperty().bindBidirectional(selectedDataReturn.tblLostInformationProperty());
        cbpLostInformation.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtItemNameReturn.textProperty().set(newVal.getItemName());
                txtDetailItemReturn.textProperty().set(newVal.getLostNote());
            }
        });

        txtReturnName.textProperty().bindBidirectional(selectedDataReturn.returnNameProperty());
        txtReturnAddress.textProperty().bindBidirectional(selectedDataReturn.returnAddressProperty());
        txtPhoneNumber.textProperty().bindBidirectional(selectedDataReturn.returnPhoneProperty());

        if (foundInformationController.selectedData != null) {
            selectedDataReturn.setTblFoundInformation(foundInformationController.selectedData);
            dateFound.setValue(((java.sql.Date) foundInformationController.selectedData.getFoundDate()).toLocalDate());
            txtItemFound.textProperty().bindBidirectional(foundInformationController.selectedData.itemNameProperty());
            txtItemDetailFound.textProperty().bindBidirectional(foundInformationController.selectedData.foundNoteProperty());
            txtFounderName.textProperty().bindBidirectional(foundInformationController.selectedData.founderNameProperty());
        }
    }

    private void dataFoundInformationReturnCreateHandle() {
        selectedDataReturn = new TblLostFoundInformationDetail();
        setSelectedDataToInputForm();
    }

    private void dataFoundInformationReturnSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, foundInformationController.dialogStage);

            if (alert.getResult() == ButtonType.OK) {
                TblLostFoundInformationDetail dummySelectedData = new TblLostFoundInformationDetail(selectedDataReturn);
                if (foundInformationController.getService().insertDataReturnInformation(dummySelectedData)) {
                    ClassMessage.showSucceedInsertingDataMessage(null, foundInformationController.dialogStage);
                    foundInformationController.dialogStage.close();
                    foundInformationController.tableDataFoundInformation.getTableView().setItems(foundInformationController.loadAllDataFoundInformation());
                } else {
                    ClassMessage.showFailedInsertingDataMessage(null, foundInformationController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, foundInformationController.dialogStage);
        }
    }

    private void dataFoundInformationReturnCancelHandle() {
        foundInformationController.dialogStage.close();
        foundInformationController.tableDataFoundInformation.getTableView().setItems(foundInformationController.loadAllDataFoundInformation());
    }

    String errDataInput;

    private boolean checkDataInput() {
        boolean check = true;

        errDataInput = "";
        if (cbpLostInformation.getValue() == null) {
            errDataInput += "ID Barang Hilang : " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (dateReturn.getValue() == null) {
            errDataInput += "Tanggal Dikembalikan :" + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (txtReturnName.getText() == null || txtReturnName.getText().equalsIgnoreCase("")) {
            errDataInput += "Nama Pengembali : " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (txtReturnAddress.getText() == null || txtReturnAddress.getText().equalsIgnoreCase("")) {
            errDataInput += "Alamat : " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (txtPhoneNumber.getText() == null || txtPhoneNumber.getText().equalsIgnoreCase("")) {
            errDataInput += "Nomor Telepon : " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }
        return check;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFormFoundInformationReturn();
        dataFoundInformationReturnCreateHandle();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public FoundInformationReturnDialogPopupController(FoundInformationController foundInformationController) {
        this.foundInformationController = foundInformationController;
    }
    private final FoundInformationController foundInformationController;
}
