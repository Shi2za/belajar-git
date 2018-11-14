/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_lostfound_information.lost_information;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.TblPeople;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.layout.GridPane;

/**
 *
 * @author Marsella
 */
public class LostInformationDialogPopupController implements Initializable {

    @FXML
    private GridPane gpFormDataLostInformation;
    @FXML
    private JFXTextField txtPeopleName;
    @FXML
    private JFXTextArea txtPeopleAddress;
    @FXML
    private JFXTextField txtPeopleCity;
    @FXML
    private JFXTextField txtPeopleZip;
    @FXML
    private JFXTextField txtPeopleRegion;
    @FXML
    private JFXTextField txtPeoplePhoneNumber;
    @FXML
    private JFXTextField txtPeopleEmail;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    private JFXCComboBoxTablePopup<RefCountry> cbpCountry;

    private TblPeople selectedDataPeople;

    private void initFormLostInformationPopup() {
        initDataPopup();

        btnSave.setOnAction((e) -> {
            dataLostInformationPopupSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
            dataLostInformationPopupCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtPeopleName,
                txtPeopleAddress,
                txtPeopleCity,
                cbpCountry,
                txtPeoplePhoneNumber);
    }

    private void initDataPopup() {
        TableView<RefCountry> tblCountry = new TableView();
        TableColumn<RefCountry, String> nameCountry = new TableColumn("Negara");
        nameCountry.setCellValueFactory(cellData -> cellData.getValue().countryNameProperty());
        tblCountry.getColumns().addAll(nameCountry);

        ObservableList<RefCountry> countryItems = FXCollections.observableArrayList(lostInformationController.getService().getAllDataCountry());

        cbpCountry = new JFXCComboBoxTablePopup<>(
                RefCountry.class, tblCountry, countryItems, "", "Negara *", true, 200, 200
        );
        
        gpFormDataLostInformation.add(cbpCountry, 2, 5);
    }

    private void refreshDataPopup() {
        ObservableList<RefCountry> countryItems = FXCollections.observableArrayList(lostInformationController.getService().getAllDataCountry());
        cbpCountry.setItems(countryItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtPeopleName.textProperty().bindBidirectional(selectedDataPeople.fullNameProperty());
        txtPeopleAddress.textProperty().bindBidirectional(selectedDataPeople.addressProperty());
        txtPeopleCity.textProperty().bindBidirectional(selectedDataPeople.townProperty());
        txtPeopleZip.textProperty().bindBidirectional(selectedDataPeople.poscodeProperty());
        txtPeopleRegion.textProperty().bindBidirectional(selectedDataPeople.regionProperty());
        cbpCountry.valueProperty().bindBidirectional(selectedDataPeople.refCountryProperty());
        txtPeoplePhoneNumber.textProperty().bindBidirectional(selectedDataPeople.hpnumberProperty());
        txtPeopleEmail.textProperty().bindBidirectional(selectedDataPeople.emailProperty());

        cbpCountry.hide();
    }

    private void dataLostInformationPopupCreateHandle() {
        selectedDataPeople = new TblPeople();
        setSelectedDataToInputForm();
     // lostInformationController.selectedData.setTblPeople(new TblPeople());

    }

    private void dataLostInformationPopupSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
            if (alert.getResult() == ButtonType.OK) {
                TblPeople dummySelected = new TblPeople(selectedDataPeople);
                if (lostInformationController.getService().insertDataPeopleInformation(dummySelected)) {
                    ClassMessage.showSucceedInsertingDataMessage(null, null);
                    lostInformationController.refreshDataPopup();

                    selectedDataPeople = lostInformationController.getService().getDataPeople(dummySelected.getIdpeople());
                    lostInformationController.selectedData.setTblPeople(selectedDataPeople);
                    lostInformationController.dialogStagePeople.close();
                  //lostInformationController.cbpPeople.valueProperty().set(lostInformationController.selectedData.getTblPeople());
                    //lostInformationController.dataLostInformationCreateHandle(lostInformationController.selectedData);
                } else {
                    ClassMessage.showFailedInsertingDataMessage(lostInformationController.getService().getErrorMessage(), lostInformationController.dialogStagePeople);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, lostInformationController.dialogStagePeople);
        }

    }

    private void dataLostInformationPopupCancelHandle() {
        lostInformationController.dialogStagePeople.close();
    }

    private String errDataInput;

    private boolean checkDataInput() {
        boolean dataInput = true;
        errDataInput = "";

        if (txtPeopleName.getText() == null || txtPeopleName.getText().equalsIgnoreCase("")) {
            errDataInput += "Nama Pelapor: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }

        if (txtPeopleAddress.getText() == null || txtPeopleAddress.getText().equalsIgnoreCase("")) {
            errDataInput += "Alamat: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }

        if (txtPeopleCity.getText() == null || txtPeopleCity.getText().equalsIgnoreCase("")) {
            errDataInput += "Kota: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }

        if (cbpCountry.getValue() == null) {
            errDataInput += "Negara: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }

        if (txtPeoplePhoneNumber.getText() == null || txtPeoplePhoneNumber.getText().equalsIgnoreCase("")) {
            errDataInput += "Nomor Telepon: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }
        return dataInput;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //();
        initFormLostInformationPopup();
        dataLostInformationPopupCreateHandle();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LostInformationDialogPopupController(LostInformationController lostInformationController) {
        this.lostInformationController = lostInformationController;
    }
    private final LostInformationController lostInformationController;
}
