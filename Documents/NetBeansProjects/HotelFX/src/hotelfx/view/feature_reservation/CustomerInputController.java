/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleIdentifierType;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblCustomerBankAccount;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.service.FReservationManager;
import insidefx.undecorator.Undecorator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class CustomerInputController implements Initializable {

    @FXML
    private AnchorPane ancFormCustomer;

    @FXML
    private GridPane gpFormDataCustomer;

    @FXML
    private ScrollPane spFormDataCustomer;

    @FXML
    private JFXTextField txtCodeCustomer;

    @FXML
    private JFXDatePicker dtpCustomerRegDate;

    @FXML
    private JFXTextArea txtCustomerNote;

    @FXML
    private ImageView imgPeople;
    
    @FXML
    private AnchorPane ancCustomerType;
    private JFXCComboBoxTablePopup<RefCustomerType> cbpCustomerType;

    @FXML
    private JFXTextField txtIdentityNumber;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtBirthPlace;

    @FXML
    private JFXDatePicker dtpBirthDate;

    @FXML
    private JFXTextArea txtAddress;

    @FXML
    private JFXTextField txtRT;

    @FXML
    private JFXTextField txtRW;

    @FXML
    private JFXTextField txtKecamatan;

    @FXML
    private JFXTextField txtKelurahan;

    @FXML
    private JFXTextField txtTown;

    @FXML
    private JFXTextField txtZIPCode;

    @FXML
    private JFXTextField txtProvinsi;

    @FXML
    private JFXTextField txtHPNumber;

    @FXML
    private JFXTextField txtEmail;
    
    @FXML
    private AnchorPane ancPeopleIdentifierType;
    private JFXCComboBoxTablePopup<RefPeopleIdentifierType> cbpPeopleIdentifierType;
    
    @FXML
    private AnchorPane ancPeopleGender;
    private JFXCComboBoxTablePopup<RefPeopleGender> cbpPeopleGender;
    
    @FXML
    private AnchorPane ancPeopleReligion;
    private JFXCComboBoxTablePopup<RefPeopleReligion> cbpPeopleReligion;
    
    @FXML
    private AnchorPane ancPeopleStatus;
    private JFXCComboBoxTablePopup<RefPeopleStatus> cbpPeopleStatus;

    @FXML
    private AnchorPane ancCountryLayout;
    private JFXCComboBoxTablePopup<RefCountry> cbpPeopleCountry;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private String imgSourcePath = "";

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private void initFormDataCustomer() {
        initDataCustomerPopup();

        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataCustomer.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataCustomer.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            //scroll end..!
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("err " + e.getMessage());
                }

            });
            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setTooltip(new Tooltip("Simpan (Data Customer)"));
        btnSave.setOnAction((e) -> {
            dataCustomerSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCustomerCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();

    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpBirthDate,
                dtpCustomerRegDate);
        ClassFormatter.setDatePickersEnableDateUntil(getMaxSelectedDate(),
                //                dtpCustomerRegDate,
                dtpBirthDate);
    }

    private LocalDate getMaxSelectedDate() {
        LocalDate maxSelectedDate = LocalDate.now();
        String guestMinimumAge = "";
        SysDataHardCode sdhGuestMinimumAge = getService().getDataSysDataHardCode((long) 24);  //Guest-MinimumAge = '24'
        if (sdhGuestMinimumAge != null
                && sdhGuestMinimumAge.getDataHardCodeValue() != null) {
            guestMinimumAge = sdhGuestMinimumAge.getDataHardCodeValue();
        }
        if (!guestMinimumAge.equals("")) {
            maxSelectedDate = maxSelectedDate.minusYears(Integer.parseInt(guestMinimumAge));
        }
        return maxSelectedDate;
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtIdentityNumber,
                txtHPNumber,
                txtFullName,
                cbpPeopleIdentifierType);
    }

    private void initDataCustomerPopup() {
        //Customer Type
        TableView<RefCustomerType> tableCustomerType = new TableView<>();

        TableColumn<RefCustomerType, String> customerTypeName = new TableColumn<>("Tipe Customer");
        customerTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        customerTypeName.setMinWidth(140);

        tableCustomerType.getColumns().addAll(customerTypeName);

        ObservableList<RefCustomerType> customerTypeItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataCustomerType());

        cbpCustomerType = new JFXCComboBoxTablePopup<>(
                RefCustomerType.class, tableCustomerType, customerTypeItems, "", "Tipe Customer", true, 200, 200
        );

        //People Identifier Type
        TableView<RefPeopleIdentifierType> tablePeopleIdentifierType = new TableView<>();

        TableColumn<RefPeopleIdentifierType, String> peopleIdentifierTypeName = new TableColumn<>("Tipe Identitas");
        peopleIdentifierTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        peopleIdentifierTypeName.setMinWidth(160);

        tablePeopleIdentifierType.getColumns().addAll(peopleIdentifierTypeName);

        ObservableList<RefPeopleIdentifierType> peopleIdentifierTypeItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleIdentifierType());

        cbpPeopleIdentifierType = new JFXCComboBoxTablePopup<>(
                RefPeopleIdentifierType.class, tablePeopleIdentifierType, peopleIdentifierTypeItems, "", "Tipe Identitas *", true, 200, 200
        );

        //People Gender
        TableView<RefPeopleGender> tablePeopleGender = new TableView<>();

        TableColumn<RefPeopleGender, String> peopleGenderName = new TableColumn<>("Jenis Kelamin");
        peopleGenderName.setCellValueFactory(cellData -> cellData.getValue().genderNameProperty());
        peopleGenderName.setMinWidth(140);

        tablePeopleGender.getColumns().addAll(peopleGenderName);

        ObservableList<RefPeopleGender> peopleGenderItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleGender());

        cbpPeopleGender = new JFXCComboBoxTablePopup<>(
                RefPeopleGender.class, tablePeopleGender, peopleGenderItems, "", "Jenis Kelamin", true, 200, 200
        );

        //People Religion
        TableView<RefPeopleReligion> tablePeopleReligion = new TableView<>();

        TableColumn<RefPeopleReligion, String> peopleReligionName = new TableColumn<>("Agama");
        peopleReligionName.setCellValueFactory(cellData -> cellData.getValue().religionNameProperty());
        peopleReligionName.setMinWidth(160);

        tablePeopleReligion.getColumns().addAll(peopleReligionName);

        ObservableList<RefPeopleReligion> peopleReligionItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleReligion());

        cbpPeopleReligion = new JFXCComboBoxTablePopup<>(
                RefPeopleReligion.class, tablePeopleReligion, peopleReligionItems, "", "Agama", true, 200, 200
        );

        //People Status
        TableView<RefPeopleStatus> tablePeopleStatus = new TableView<>();

        TableColumn<RefPeopleStatus, String> peopleStatusName = new TableColumn<>("Status Pernikahan");
        peopleStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        peopleStatusName.setMinWidth(160);

        tablePeopleStatus.getColumns().addAll(peopleStatusName);

        ObservableList<RefPeopleStatus> peopleStatusItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleStatus());

        cbpPeopleStatus = new JFXCComboBoxTablePopup<>(
                RefPeopleStatus.class, tablePeopleStatus, peopleStatusItems, "", "Status Pernikahan", true, 200, 200
        );

        //People Country
        TableView<RefCountry> tablePeopleCountry = new TableView<>();

        TableColumn<RefCountry, String> peopleCountryName = new TableColumn<>("Negara");
        peopleCountryName.setCellValueFactory(cellData -> cellData.getValue().countryNameProperty());
        peopleCountryName.setMinWidth(140);

        tablePeopleCountry.getColumns().addAll(peopleCountryName);

        ObservableList<RefCountry> peopleCountryItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleCountry());

        cbpPeopleCountry = new JFXCComboBoxTablePopup<>(
                RefCountry.class, tablePeopleCountry, peopleCountryItems, "", "Negara", true, 200, 200
        );

        //attached to grid-pane
        /*gpFormDataCustomer.add(cbpCustomerType, 3, 3);
        gpFormDataCustomer.add(cbpPeopleIdentifierType, 1, 5, 2, 1);
        gpFormDataCustomer.add(cbpPeopleGender, 1, 7, 2, 1);
        gpFormDataCustomer.add(cbpPeopleReligion, 3, 7);
        gpFormDataCustomer.add(cbpPeopleStatus, 1, 8, 2, 1);*/
        
        AnchorPane.setBottomAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleGender, 0.0);
        ancPeopleGender.getChildren().clear();
        ancPeopleGender.getChildren().add(cbpPeopleGender);
        
        AnchorPane.setBottomAnchor(cbpCustomerType, 0.0);
        AnchorPane.setLeftAnchor(cbpCustomerType, 0.0);
        AnchorPane.setRightAnchor(cbpCustomerType, 0.0);
        AnchorPane.setTopAnchor(cbpCustomerType, 0.0);
        ancCustomerType.getChildren().clear();
        ancCustomerType.getChildren().add(cbpCustomerType);
        
        AnchorPane.setBottomAnchor(cbpPeopleReligion, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleReligion, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleReligion, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleReligion, 0.0);
        ancPeopleReligion.getChildren().clear();
        ancPeopleReligion.getChildren().add(cbpPeopleReligion);
        
        AnchorPane.setBottomAnchor(cbpPeopleStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleStatus, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleStatus, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleStatus, 0.0);
        ancPeopleStatus.getChildren().clear();
        ancPeopleStatus.getChildren().add(cbpPeopleStatus);
        
        AnchorPane.setBottomAnchor(cbpPeopleIdentifierType, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleIdentifierType, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleIdentifierType, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleIdentifierType, 0.0);
        ancPeopleIdentifierType.getChildren().clear();
        ancPeopleIdentifierType.getChildren().add(cbpPeopleIdentifierType);
        
        AnchorPane.setBottomAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleCountry, 0.0);
        ancCountryLayout.getChildren().clear();
        ancCountryLayout.getChildren().add(cbpPeopleCountry);
    }

    private void refreshDataPopup() {
        //Customer Type
        ObservableList<RefCustomerType> customerTypeItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataCustomerType());
        cbpCustomerType.setItems(customerTypeItems);

        //People Identifier Type
        ObservableList<RefPeopleIdentifierType> peopleIdentifierTypeItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleIdentifierType());
        cbpPeopleIdentifierType.setItems(peopleIdentifierTypeItems);

        //People Gender
        ObservableList<RefPeopleGender> peopleGenderItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleGender());
        cbpPeopleGender.setItems(peopleGenderItems);

        //People Religion
        ObservableList<RefPeopleReligion> peopleReligionItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleReligion());
        cbpPeopleReligion.setItems(peopleReligionItems);

        //People Status
        ObservableList<RefPeopleStatus> peopleStatusItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleStatus());
        cbpPeopleStatus.setItems(peopleStatusItems);

        //People Country
        ObservableList<RefCountry> peopleCountryItems = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataPeopleCountry());
        cbpPeopleCountry.setItems(peopleCountryItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        txtCodeCustomer.textProperty().bindBidirectional(reservationController.selectedDataCustomer.codeCustomerProperty());
        //txtCustomerNote.textProperty().bindBidirectional(reservationController.selectedDataCustomer.customerNoteProperty());

        if (reservationController.selectedDataCustomer.getRegistrationDate() != null) {
            dtpCustomerRegDate.setValue(LocalDate.of(reservationController.selectedDataCustomer.getRegistrationDate().getYear() + 1900,
                    reservationController.selectedDataCustomer.getRegistrationDate().getMonth() + 1,
                    reservationController.selectedDataCustomer.getRegistrationDate().getDate()));
        } else {
            dtpCustomerRegDate.setValue(null);
        }
        dtpCustomerRegDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reservationController.selectedDataCustomer.setRegistrationDate(Date.valueOf(newVal));
            }
        });

        imgPeople.setPreserveRatio(false);
        if (reservationController.selectedDataCustomer.getTblPeople().getImageUrl() == null
                || reservationController.selectedDataCustomer.getTblPeople().getImageUrl().equals("")) {
            imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + "no_profile_img.gif";
            imgPeople.setImage(new Image("file:///" + imgSourcePath));
        } else {
            imgSourcePath = ClassFolderManager.imageClientRootPath + "/" + reservationController.selectedDataCustomer.getTblPeople().getImageUrl();
            imgPeople.setImage(new Image("file:///" + imgSourcePath));
        }
        imgPeople.setOnMouseClicked((e) -> {
            if (e.getClickCount() == 2) {
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(
                        new File(System.getProperty("user.home"))
                );
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File file = fc.showOpenDialog(reservationController.dialogStage);
                if (file != null) {
                    imgSourcePath = file.getAbsoluteFile().toString();
                    imgPeople.setImage(new Image("file:///" + imgSourcePath));
                }
            }
        });

        cbpCustomerType.valueProperty().bindBidirectional(reservationController.selectedDataCustomer.refCustomerTypeProperty());
        cbpCustomerType.hide();

        txtIdentityNumber.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().codePeopleProperty());
        txtFullName.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().fullNameProperty());
        txtBirthPlace.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().birthPlaceProperty());
        txtAddress.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().addressProperty());
        txtRT.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().rtProperty());
        txtRW.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().rwProperty());
        txtKecamatan.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().kecamatanProperty());
        txtKelurahan.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().kelurahanProperty());
        txtTown.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().townProperty());
        txtZIPCode.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().poscodeProperty());
        txtProvinsi.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().regionProperty());
        txtHPNumber.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().hpnumberProperty());
        txtEmail.textProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().emailProperty());

        if (reservationController.selectedDataCustomer.getTblPeople().getBirthDate() != null) {
            dtpBirthDate.setValue(LocalDate.of(reservationController.selectedDataCustomer.getTblPeople().getBirthDate().getYear() + 1900,
                    reservationController.selectedDataCustomer.getTblPeople().getBirthDate().getMonth() + 1,
                    reservationController.selectedDataCustomer.getTblPeople().getBirthDate().getDate()));
        } else {
            dtpBirthDate.setValue(null);
        }
        dtpBirthDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reservationController.selectedDataCustomer.getTblPeople().setBirthDate(Date.valueOf(newVal));
            }
        });

        cbpPeopleIdentifierType.valueProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().refPeopleIdentifierTypeProperty());
        cbpPeopleGender.valueProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().refPeopleGenderProperty());
        cbpPeopleReligion.valueProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().refPeopleReligionProperty());
        cbpPeopleStatus.valueProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().refPeopleStatusProperty());
        cbpPeopleCountry.valueProperty().bindBidirectional(reservationController.selectedDataCustomer.getTblPeople().refCountryProperty());

        cbpPeopleIdentifierType.hide();
        cbpPeopleGender.hide();
        cbpPeopleReligion.hide();
        cbpPeopleStatus.hide();
        cbpPeopleCountry.hide();

       // initTableDataCustomerBankAccount();
    }

    private void dataCustomerSaveHandle() {
        if (checkDataInputDataCustomer()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblCustomer dummySelectedData = new TblCustomer(reservationController.selectedDataCustomer);
                dummySelectedData.setTblPeople(new TblPeople(dummySelectedData.getTblPeople()));
                List<TblCustomerBankAccount> dummyDataCustomerBankAccounts = new ArrayList<>();
//                for (TblCustomerBankAccount dataCustomerBankAccount : (List<TblCustomerBankAccount>) tableDataCustomerBankAccount.getTableView().getItems()) {
//                    TblCustomerBankAccount dummyDataCustomerBankAccount = new TblCustomerBankAccount(dataCustomerBankAccount);
//                    dummyDataCustomerBankAccount.setTblCustomer(dummySelectedData);
//                    dummyDataCustomerBankAccount.setTblBankAccount(new TblBankAccount(dummyDataCustomerBankAccount.getTblBankAccount()));
//                    dummyDataCustomerBankAccounts.add(dummyDataCustomerBankAccount);
//                }
                switch (reservationController.dataCustomerInputStatus) {
                    case 0:
                        if (reservationController.getFReservationManager().insertDataCustomer(dummySelectedData,
                                imgSourcePath.split("\\.")[1],
                                dummyDataCustomerBankAccounts) != null) {
                            try {
                                //save image
                                ClassFolderManager.copyImage(
                                        imgSourcePath,
                                        reservationController.selectedDataCustomer.getTblPeople().getImageUrl(),
                                        "Client");
                            } catch (IOException ex) {
                                Logger.getLogger(CustomerInputController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            ClassMessage.showSucceedInsertingDataMessage("", reservationController.dialogStage);
                            //refresh data customer popup
                            reservationController.refreshDataPopup();
                            //set data on field
                            reservationController.selectedDataCustomer = reservationController.getFReservationManager().getCustomer(dummySelectedData.getIdcustomer());
                            reservationController.selectedDataCustomer.setTblPeople(reservationController.getFReservationManager().getPeople(reservationController.selectedDataCustomer.getTblPeople().getIdpeople()));
                            reservationController.selectedData.setTblCustomer(reservationController.selectedDataCustomer);
                            //close form data customer input
                            reservationController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(reservationController.getFReservationManager().getErrorMessage(), reservationController.dialogStage);
                        }
                        break;
                    case 1:
                        if (reservationController.getFReservationManager().updateDataCustomer(dummySelectedData,
                                imgSourcePath.split("\\.")[1],
                                dummyDataCustomerBankAccounts)) {
                            if (!imgSourcePath.contains(ClassFolderManager.imageClientRootPath)) {
                                try {
                                    //save image
                                    ClassFolderManager.copyImage(
                                            imgSourcePath,
                                            reservationController.selectedDataCustomer.getTblPeople().getImageUrl(),
                                            "Client");
                                } catch (IOException ex) {
                                    Logger.getLogger(CustomerInputController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            ClassMessage.showSucceedUpdatingDataMessage("", reservationController.dialogStage);
                            //refresh data customer popup
                            reservationController.refreshDataPopup();
                            //set data on field
                            reservationController.selectedDataCustomer = reservationController.getFReservationManager().getCustomer(dummySelectedData.getIdcustomer());
                            reservationController.selectedDataCustomer.setTblPeople(reservationController.getFReservationManager().getPeople(reservationController.selectedDataCustomer.getTblPeople().getIdpeople()));
                            reservationController.selectedData.setTblCustomer(reservationController.selectedDataCustomer);
                            //close form data customer input
                            reservationController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(reservationController.getFReservationManager().getErrorMessage(), reservationController.dialogStage);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCustomerCancelHandle() {
        if (reservationController.dataInputStatus == 1) {   //update
            //refresh data
            reservationController.refreshDataSelectedReservation();
        }
        //close form data customer input
        reservationController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataCustomer() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtIdentityNumber.getText() == null || txtIdentityNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor Identitas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtHPNumber.getText() == null || txtHPNumber.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nomor HP : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtFullName.getText() == null || txtFullName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Nama Lengkap : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleIdentifierType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Identitas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * TABLE DATA CUSTOMER BANK ACCOUNT
     */
    @FXML
    private AnchorPane tableDataCustomerBankAccountLayout;

    public ClassTableWithControl tableDataCustomerBankAccount;

    private void initTableDataCustomerBankAccount() {
        //set table
        setTableDataCustomerBankAccount();
        //set control
        setTableControlDataCustomerBankAccount();
        //set table-control to content-view
        tableDataCustomerBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataCustomerBankAccount, 0.0);
        AnchorPane.setLeftAnchor(tableDataCustomerBankAccount, 0.0);
        AnchorPane.setRightAnchor(tableDataCustomerBankAccount, 0.0);
        AnchorPane.setTopAnchor(tableDataCustomerBankAccount, 0.0);
        tableDataCustomerBankAccountLayout.getChildren().add(tableDataCustomerBankAccount);
    }

    private void setTableDataCustomerBankAccount() {
        TableView<TblCustomerBankAccount> tableView = new TableView();

        TableColumn<TblCustomerBankAccount, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().tblBankAccountProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblCustomerBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().tblBankAccountProperty()));
        bankAccount.setMinWidth(140);

        TableColumn<TblCustomerBankAccount, String> bankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderName.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderName.setMinWidth(200);

        tableView.getColumns().addAll(bankName, bankAccount, bankAccountHolderName);
        tableView.setItems(loadAllDataCustomerBankAccount());
        tableDataCustomerBankAccount = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataCustomerBankAccount() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataCustomerBankAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataCustomerBankAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataCustomerBankAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataCustomerBankAccount.addButtonControl(buttonControls);
    }

    private ObservableList<TblCustomerBankAccount> loadAllDataCustomerBankAccount() {
        ObservableList<TblCustomerBankAccount> list = FXCollections.observableArrayList(reservationController.getFReservationManager().getAllDataCustomerBankAccountByIDCustomer(reservationController.selectedDataCustomer.getIdcustomer()));
        for (TblCustomerBankAccount data : list) {
            //set data customer
            data.setTblCustomer(reservationController.selectedDataCustomer);
            //set data bank account
            data.setTblBankAccount(reservationController.getFReservationManager().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
        }
        return list;
    }

    public TblCustomerBankAccount selectedDataCustomerBankAccount;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputCustomerBankAccountStatus = 0;

    public Stage dialogStage;

    public void dataCustomerBankAccountCreateHandle() {
        dataInputCustomerBankAccountStatus = 0;
        selectedDataCustomerBankAccount = new TblCustomerBankAccount();
        selectedDataCustomerBankAccount.setTblCustomer(reservationController.selectedDataCustomer);
        selectedDataCustomerBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data customer bank account
        showCustomerBankAccountDialog();
    }

    public void dataCustomerBankAccountUpdateHandle() {
        if (tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputCustomerBankAccountStatus = 1;
            selectedDataCustomerBankAccount = (TblCustomerBankAccount) tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItem();
            //open form data customer bank account
            showCustomerBankAccountDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", reservationController.dialogStage);
        }
    }

    public void dataCustomerBankAccountDeleteHandle() {
        if (tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", reservationController.dialogStage);
                //remove data from table items list
                tableDataCustomerBankAccount.getTableView().getItems().remove(tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", reservationController.dialogStage);
        }
    }

    private void showCustomerBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/CustomerBankAccountInputDialog.fxml"));

            CustomerBankAccountInputController controller = new CustomerBankAccountInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(reservationController.dialogStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
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
        initFormDataCustomer();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public CustomerInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

    public FReservationManager getService() {
        return reservationController.getFReservationManager();
    }

}
