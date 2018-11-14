/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_lostfound_information.lost_information;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblFoundInformation;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
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
public class LostInformationReturnDialogPopupController implements Initializable {

    @FXML
    private AnchorPane cbpFoundInformationLayout;
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
    private JFXDatePicker dateLost;
    @FXML
    private JFXTextField txtItemLost;
    @FXML
    private JFXTextArea txtItemDetailLost;
    @FXML
    private JFXTextField txtNameLost;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    private JFXCComboBoxTablePopup<TblFoundInformation> cbpFoundInformation;

    private TblLostFoundInformationDetail selectedDataReturn;

    private void initFormLostInformationReturn() {
        initDataPopupLayout();
        initDateCalendar();

        btnSave.setOnAction((e) -> {
            dataLostInformationReturnSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
            dataLostInformationReturnCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpFoundInformation,
                dateReturn,
                txtReturnName,
                txtReturnAddress,
                txtPhoneNumber);
    }

    private void initDataPopupLayout() {
        initDataPopup();
        cbpFoundInformationLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpFoundInformation, 0.0);
        AnchorPane.setBottomAnchor(cbpFoundInformation, 0.0);
        AnchorPane.setLeftAnchor(cbpFoundInformation, 0.0);
        AnchorPane.setRightAnchor(cbpFoundInformation, 0.0);
        cbpFoundInformationLayout.getChildren().add(cbpFoundInformation);
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern, dateLost, dateReturn);
    }

    private void initDataPopup() {
        TableView<TblFoundInformation> tblFoundInformation = new TableView();
        TableColumn<TblFoundInformation, String> codeFound = new TableColumn("ID");
        codeFound.setMinWidth(100);
        codeFound.setCellValueFactory(cellData -> cellData.getValue().codeFoundProperty());
        TableColumn<TblFoundInformation, String> dateFound = new TableColumn("Tanggal \n Lapor");
        dateFound.setMinWidth(100);
        dateFound.setCellValueFactory((TableColumn.CellDataFeatures<TblFoundInformation, String> param)
                -> Bindings.createStringBinding(() -> new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getFoundDate()), param.getValue().foundDateProperty()));
        TableColumn<TblFoundInformation, String> locationFound = new TableColumn("Tempat Ditemukan");
        locationFound.setMinWidth(160);
        locationFound.setCellValueFactory(cellData -> cellData.getValue().foundLocationProperty());
        TableColumn<TblFoundInformation, String> nameItem = new TableColumn("Nama Barang");
        nameItem.setMinWidth(160);
        nameItem.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        TableColumn<TblFoundInformation, String> namePeople = new TableColumn("Ditemukan Oleh");
        namePeople.setMinWidth(160);
        namePeople.setCellValueFactory(cellData -> cellData.getValue().founderNameProperty());
        TableColumn<TblFoundInformation, String> detailItem = new TableColumn("Keterangan");
        detailItem.setMinWidth(200);
        detailItem.setCellValueFactory(cellData -> cellData.getValue().foundNoteProperty());
        tblFoundInformation.getColumns().addAll(codeFound, dateFound, locationFound, nameItem, namePeople, detailItem);

        ObservableList<TblFoundInformation> foundInformationItem = FXCollections.observableArrayList(loadAllDataFoundInformation());

        cbpFoundInformation = new JFXCComboBoxTablePopup<>(
                TblFoundInformation.class, tblFoundInformation, foundInformationItem, "", "ID Barang Ditemukan *", true, 900, 300
        );
    }

    private List<TblFoundInformation> loadAllDataFoundInformation() {
        List<TblFoundInformation> listFound = new ArrayList();
        listFound = lostInformationController.getService().getAllDataFoundInformationReturn();
        return listFound;
    }

    private void refreshDataPopup() {
        ObservableList<TblFoundInformation> listFound = FXCollections.observableArrayList(loadAllDataFoundInformation());
        cbpFoundInformation.setItems(listFound);
    }

    private void setSelectedDataToInputForm() {
        //dateReturn
        refreshDataPopup();
        dateReturn.valueProperty().addListener((obs, oldVal, newVal) -> {
            selectedDataReturn.setReturnDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        });

        cbpFoundInformation.valueProperty().bindBidirectional(selectedDataReturn.tblFoundInformationProperty());
        cbpFoundInformation.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtItemNameReturn.textProperty().set(newVal.getItemName());
                txtDetailItemReturn.textProperty().set(newVal.getFoundNote());
            }
        });

        txtReturnName.textProperty().bindBidirectional(selectedDataReturn.returnNameProperty());
        txtReturnAddress.textProperty().bindBidirectional(selectedDataReturn.returnAddressProperty());
        txtPhoneNumber.textProperty().bindBidirectional(selectedDataReturn.returnPhoneProperty());

        if (lostInformationController.selectedData != null) {
            selectedDataReturn.setTblLostInformation(lostInformationController.selectedData);
            dateLost.setValue(((java.sql.Date) lostInformationController.selectedData.getLostDate()).toLocalDate());
            txtItemLost.textProperty().bindBidirectional(lostInformationController.selectedData.itemNameProperty());
            txtItemDetailLost.textProperty().bindBidirectional(lostInformationController.selectedData.lostNoteProperty());
            txtNameLost.textProperty().bindBidirectional(lostInformationController.selectedData.getTblPeople().fullNameProperty());
        }
    }

    private void dataLostInformationReturnCreateHandle() {
        selectedDataReturn = new TblLostFoundInformationDetail();
        setSelectedDataToInputForm();
    }

    private void dataLostInformationReturnSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, lostInformationController.dialogStageReturn);

            if (alert.getResult() == ButtonType.OK) {
                TblLostFoundInformationDetail dummySelectedData = new TblLostFoundInformationDetail(selectedDataReturn);
                if (lostInformationController.getService().insertDataReturnInformation(dummySelectedData)) {
                    ClassMessage.showSucceedInsertingDataMessage(null, lostInformationController.dialogStageReturn);
                    lostInformationController.dialogStageReturn.close();
                    lostInformationController.tableDataLostInformation.getTableView().setItems(lostInformationController.loadAllDataLostInformation());
                } else {
                    ClassMessage.showFailedInsertingDataMessage(lostInformationController.getService().getErrorMessage(), lostInformationController.dialogStageReturn);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, lostInformationController.dialogStageReturn);
        }
    }

    private void dataLostInformationReturnCancelHandle() {
        lostInformationController.dialogStageReturn.close();
        lostInformationController.tableDataLostInformation.getTableView().setItems(lostInformationController.loadAllDataLostInformation());
    }

    String errDataInput;

    private boolean checkDataInput() {
        boolean check = true;

        errDataInput = "";
        if (cbpFoundInformation.getValue() == null) {
            errDataInput += "ID Barang Ditemukan : " + ClassMessage.defaultErrorNullValueMessage + "\n";
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
        initFormLostInformationReturn();
        dataLostInformationReturnCreateHandle();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LostInformationReturnDialogPopupController(LostInformationController lostInformationController) {
        this.lostInformationController = lostInformationController;
    }
    private final LostInformationController lostInformationController;
}
