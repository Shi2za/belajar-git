/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_customer.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleIdentifierType;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblCustomerBankAccount;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.service.FCustomerManager;
import hotelfx.persistence.service.FCustomerManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_customer.FeatureCustomerController;
import insidefx.undecorator.Undecorator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class CustomerController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataCustomer;

    private DoubleProperty dataCustomerFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataCustomerLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataCustomerSplitpane() {
        spDataCustomer.setDividerPositions(1);

        dataCustomerFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataCustomerFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataCustomer.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataCustomer.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataCustomerFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataCustomerLayout.setDisable(false);
                    tableDataCustomerLayoutDisableLayer.setDisable(true);
                    tableDataCustomerLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataCustomerLayout.setDisable(true);
                    tableDataCustomerLayoutDisableLayer.setDisable(false);
                    tableDataCustomerLayoutDisableLayer.toFront();
                }
            }
        });

        dataCustomerFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataCustomerLayout;

    private ClassFilteringTable<TblCustomer> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataCustomer;

    private void initTableDataCustomer() {
        //set table
        setTableDataCustomer();
        //set control
        setTableControlDataCustomer();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataCustomer, 15.0);
        AnchorPane.setLeftAnchor(tableDataCustomer, 15.0);
        AnchorPane.setRightAnchor(tableDataCustomer, 15.0);
        AnchorPane.setTopAnchor(tableDataCustomer, 15.0);
        ancBodyLayout.getChildren().add(tableDataCustomer);
    }

    private void setTableDataCustomer() {
        TableView<TblCustomer> tableView = new TableView();

        TableColumn<TblCustomer, String> idCustomer = new TableColumn("ID" + "\n" + "Customer");
        idCustomer.setCellValueFactory(cellData -> cellData.getValue().codeCustomerProperty());
        idCustomer.setMinWidth(100);

        TableColumn<TblCustomer, String> regDate = new TableColumn("Tanggal Regristrasi");
        regDate.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRegistrationDate() != null
                                ? ClassFormatter.dateFormate.format(param.getValue().getRegistrationDate())
                                : "-", param.getValue().registrationDateProperty()));
        regDate.setMinWidth(140);

        TableColumn<TblCustomer, String> customerType = new TableColumn("Tipe Customer");
        customerType.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefCustomerType() != null
                                ? param.getValue().getRefCustomerType().getTypeName()
                                : "-", param.getValue().refCustomerTypeProperty()));
        customerType.setMinWidth(140);

        TableColumn<TblCustomer, String> peopleName = new TableColumn("Nama Lengkap");
        peopleName.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().tblPeopleProperty()));
        peopleName.setMinWidth(180);

        TableColumn<TblCustomer, String> identityNumber = new TableColumn("Nomor Identitas");
        identityNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblPeople().getRefPeopleIdentifierType() != null ? param.getValue().getTblPeople().getRefPeopleIdentifierType().getTypeName() : "-") + ":"
                        + (param.getValue().getTblPeople().getCodePeople() != null
                                ? param.getValue().getTblPeople().getCodePeople() : "-"),
                        param.getValue().tblPeopleProperty()));
        identityNumber.setMinWidth(180);

//        TableColumn<TblCustomer, String> ktpNumber = new TableColumn("Nomor KTP");
//        ktpNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getKtpnumber(), param.getValue().tblPeopleProperty()));
//        ktpNumber.setMinWidth(140);
//        
//        TableColumn<TblCustomer, String> passportNumber = new TableColumn("Nomor Passport");
//        passportNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getPassportNumber(), param.getValue().tblPeopleProperty()));
//        passportNumber.setMinWidth(180);
//        
//        TableColumn<TblCustomer, String> simNumber = new TableColumn("Nomor SIM");
//        simNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getSimnumber(), param.getValue().tblPeopleProperty()));
//        simNumber.setMinWidth(140);
        TableColumn<TblCustomer, String> birthPlaceDate = new TableColumn("TTL");
        birthPlaceDate.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblPeople().getBirthPlace() != null || param.getValue().getTblPeople().getBirthPlace() != null)
                                ? param.getValue().getTblPeople().getBirthPlace() + ",\n" + ClassFormatter.dateFormate.format(param.getValue().getTblPeople().getBirthDate())
                                : "-", param.getValue().tblPeopleProperty()));
        birthPlaceDate.setMinWidth(140);

        TableColumn<TblCustomer, String> peopleGender = new TableColumn("L/P");
        peopleGender.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getRefPeopleGender() != null
                                ? (param.getValue().getTblPeople().getRefPeopleGender().getIdgender() == 1 ? "P" : "L")
                                : "-", param.getValue().tblPeopleProperty()));
        peopleGender.setMinWidth(30);

        TableColumn<TblCustomer, String> peopleStatus = new TableColumn("Status Perkawinan");
        peopleStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getRefPeopleStatus() != null
                                ? param.getValue().getTblPeople().getRefPeopleStatus().getStatusName()
                                : "-", param.getValue().tblPeopleProperty()));
        peopleStatus.setMinWidth(180);

        TableColumn<TblCustomer, String> peopleAddress = new TableColumn("Alamat");
        peopleAddress.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> "Jalan:" + (param.getValue().getTblPeople().getAddress() != null ? param.getValue().getTblPeople().getAddress() : "-") + "\n"
                        + "RT/RW" + (param.getValue().getTblPeople().getRt() != null ? param.getValue().getTblPeople().getRt() : "-") + " / "
                        + (param.getValue().getTblPeople().getRw() != null ? param.getValue().getTblPeople().getRw() : "-") + "\n"
                        + "Kelurahan:" + (param.getValue().getTblPeople().getKelurahan() != null ? param.getValue().getTblPeople().getKelurahan() : "-") + "\n"
                        + "Kecamatan:" + (param.getValue().getTblPeople().getKecamatan() != null ? param.getValue().getTblPeople().getKecamatan() : "-") + "\n"
                        + "Kota:" + (param.getValue().getTblPeople().getTown() != null ? param.getValue().getTblPeople().getTown() : "-")
                        + "Provinsi:" + (param.getValue().getTblPeople().getRegion() != null ? param.getValue().getTblPeople().getRegion() : "-") + "\n"
                        + "Negara:" + (param.getValue().getTblPeople().getRefCountry() != null ? param.getValue().getTblPeople().getRefCountry().getCountryName() : "-"),
                        param.getValue().tblPeopleProperty()));
        peopleAddress.setMinWidth(200);

        /*TableColumn<TblCustomer, String> rt = new TableColumn("RT");
         rt.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
         -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getRt(), param.getValue().tblPeopleProperty()));
         rt.setMinWidth(80);

         TableColumn<TblCustomer, String> rw = new TableColumn("RW");
         rw.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
         -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getRw(), param.getValue().tblPeopleProperty()));
         rw.setMinWidth(80);

         TableColumn<TblCustomer, String> kecamatan = new TableColumn("Kecamatan");
         kecamatan.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
         -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getKecamatan(), param.getValue().tblPeopleProperty()));
         kecamatan.setMinWidth(180);

         TableColumn<TblCustomer, String> kelurahan = new TableColumn("Kelurahan");
         kelurahan.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
         -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getKelurahan(), param.getValue().tblPeopleProperty()));
         kelurahan.setMinWidth(180);

         TableColumn<TblCustomer, String> town = new TableColumn("Kota");
         town.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
         -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getTown(), param.getValue().tblPeopleProperty()));
         town.setMinWidth(180);*/
        TableColumn<TblCustomer, String> hpNumber = new TableColumn("Nomor HP");
        hpNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getHpnumber(), param.getValue().tblPeopleProperty()));
        hpNumber.setMinWidth(120);

        TableColumn<TblCustomer, String> email = new TableColumn("Email");
        email.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getEmail(), param.getValue().tblPeopleProperty()));
        email.setMinWidth(200);

        tableView.getColumns().addAll(idCustomer, peopleName, identityNumber,
                //                ktpNumber, passportNumber, simNumber, 
                birthPlaceDate, peopleGender, hpNumber, email,
                //peopleStatus, peopleAddress, rt, rw, kecamatan, kelurahan, town,
                customerType);
        tableView.setItems(loadAllDataCustomer());

        tableView.setRowFactory(tv -> {
            TableRow<TblCustomer> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataCustomerUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataCustomerUpdateHandle();
                            } else {
                                dataCustomerShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataCustomerUpdateHandle();
//                            } else {
//                                dataCustomerShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataCustomer = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblCustomer.class,
                tableDataCustomer.getTableView(),
                tableDataCustomer.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataCustomer() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataCustomerCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataCustomerUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataCustomerDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
                showCustomerPrintDialog();
            });
            buttonControls.add(buttonControl);
        }
        tableDataCustomer.addButtonControl(buttonControls);
    }

    private ObservableList<TblCustomer> loadAllDataCustomer() {
        List<TblCustomer> customers = getService().getAllDataCustomer();
        for (TblCustomer customer : customers) {
            //data customer type
            customer.setRefCustomerType(getService().getCustomerType(customer.getRefCustomerType().getIdtype()));
            //data people
            customer.setTblPeople(getService().getPeople(customer.getTblPeople().getIdpeople()));
        }
        return FXCollections.observableArrayList(customers);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataCustomer;

    @FXML
    private ScrollPane spFormDataCustomer;

    @FXML
    private AnchorPane ancPeopleIdentifierType;

    @FXML
    private AnchorPane ancPeopleGender;

    @FXML
    private AnchorPane ancPeopleReligion;

    @FXML
    private AnchorPane ancPeopleStatus;

    @FXML
    private AnchorPane ancCustomerType;

    @FXML
    private JFXTextField txtCodeCustomer;

    @FXML
    private JFXDatePicker dtpCustomerRegDate;

//    @FXML
//    private JFXTextField txtCustomerDeposit;
    @FXML
    private JFXTextArea txtCustomerNote;

    @FXML
    private ImageView imgPeople;

    private JFXCComboBoxTablePopup<RefCustomerType> cbpCustomerType;

//    private final JFXCComboBoxPopup<RefCustomerStatus> cbpCustomerStatus = new JFXCComboBoxPopup<>(RefCustomerStatus.class);
    @FXML
    private JFXTextField txtIdentityNumber;

//    @FXML
//    private JFXTextField txtKTPNumber;
//
//    @FXML
//    private JFXTextField txtPassportNumber;
//
//    @FXML
//    private JFXTextField txtSIMNumber;
    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtBirthPlace;

    @FXML
    private JFXDatePicker dtpBirthDate;

    @FXML
    private JFXTextArea txtPeopleAddress;

    @FXML
    private JFXTextField txtRT;

    @FXML
    private JFXTextField txtRW;

    @FXML
    private JFXTextField txtKecamatan;

    @FXML
    private JFXTextField txtKelurahan;

    @FXML
    private JFXTextField txtKota;

    @FXML
    private JFXTextField txtZIPCode;

    @FXML
    private JFXTextField txtProvinsi;

    @FXML
    private JFXTextField txtHPNumber;

//    @FXML
//    private JFXTextField txtTelpNumber;
    @FXML
    private JFXTextField txtEmail;

//    @FXML
//    private JFXTextArea txtPeopleNote;
    private final JFXCComboBoxPopup<RefPeopleIdentifierType> cbpPeopleIdentifierType = new JFXCComboBoxPopup<>(RefPeopleIdentifierType.class);

    private final JFXCComboBoxPopup<RefPeopleGender> cbpPeopleGender = new JFXCComboBoxPopup<>(RefPeopleGender.class);

    private final JFXCComboBoxPopup<RefPeopleReligion> cbpPeopleReligion = new JFXCComboBoxPopup<>(RefPeopleReligion.class);

    private final JFXCComboBoxPopup<RefPeopleStatus> cbpPeopleStatus = new JFXCComboBoxPopup<>(RefPeopleStatus.class);

    @FXML
    private AnchorPane ancCountryLayout;
    private final JFXCComboBoxPopup<RefCountry> cbpPeopleCountry = new JFXCComboBoxPopup<>(RefCountry.class);

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Label lblNameCustomer;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblCustomer selectedData;

    private String imgSourcePath = "";

    private void initFormDataCustomer() {
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

        initDataPopup();

        initImportantFieldColor();

    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpCustomerRegDate,
                dtpBirthDate);
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
//        ClassViewSetting.setNotNullField(
//                txtIdentityNumber,
//                txtFullName,
//                cbpCustomerType,
//                cbpPeopleIdentifierType,
//                cbpPeopleGender,
//                cbpPeopleStatus,
//                cbpPeopleReligion,
//                cbpPeopleCountry,
//                dtpCustomerRegDate,
//                dtpBirthDate,
//                txtBirthPlace,
//                txtPeopleAddress,
//                txtKota,
//                txtHPNumber, 
//                txtEmail);
        ClassViewSetting.setImportantField(
                txtIdentityNumber,
                txtFullName,
                cbpCustomerType,
                cbpPeopleIdentifierType,
                cbpPeopleGender,
                cbpPeopleStatus,
                cbpPeopleReligion,
                cbpPeopleCountry,
                dtpCustomerRegDate,
                dtpBirthDate,
                txtBirthPlace,
                txtPeopleAddress,
                txtKota,
                txtHPNumber);
    }

    private void initDataPopup() {
        //Customer Type
        TableView<RefCustomerType> tableCustomerType = new TableView<>();

        TableColumn<RefCustomerType, String> customerTypeName = new TableColumn<>("Tipe Customer");
        customerTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        customerTypeName.setMinWidth(140);

        tableCustomerType.getColumns().addAll(customerTypeName);

        ObservableList<RefCustomerType> customerTypeItems = FXCollections.observableArrayList(getService().getAllDataCustomerType());

        cbpCustomerType = new JFXCComboBoxTablePopup<>(
                RefCustomerType.class, tableCustomerType, customerTypeItems, "", "Tipe Customer *", true, 200, 200
        );

//        setFunctionPopup(cbpCustomerType, tableCustomerType, customerTypeItems, "typeName", "Tipe Customer *");
//        //Customer Status
//        TableView<RefCustomerStatus> tableCustomerStatus = new TableView<>();
//        TableColumn<RefCustomerStatus, String> customerStatusName = new TableColumn<>("Customer Status");
//        customerStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
//        tableCustomerStatus.getColumns().addAll(customerStatusName);
//
//        ObservableList<RefCustomerStatus> customerStatusItems = FXCollections.observableArrayList(getService().getAllDataCustomerStatus());
//
//        setFunctionPopup(cbpCustomerStatus, tableCustomerStatus, customerStatusItems, "statusName", "Customer Status");
        //People Identifier Type
        TableView<RefPeopleIdentifierType> tablePeopleIdentifierType = new TableView<>();

        TableColumn<RefPeopleIdentifierType, String> peopleIdentifierTypeName = new TableColumn<>("Tipe Identitas");
        peopleIdentifierTypeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        peopleIdentifierTypeName.setMinWidth(160);

        tablePeopleIdentifierType.getColumns().addAll(peopleIdentifierTypeName);

        ObservableList<RefPeopleIdentifierType> peopleIdentifierTypeItems = FXCollections.observableArrayList(getService().getAllDataPeopleIdentifierType());

        setFunctionPopup(cbpPeopleIdentifierType, tablePeopleIdentifierType, peopleIdentifierTypeItems, "typeName", "Tipe Identitas *");

        //People Gender
        TableView<RefPeopleGender> tablePeopleGender = new TableView<>();

        TableColumn<RefPeopleGender, String> peopleGenderName = new TableColumn<>("Jenis Kelamin");
        peopleGenderName.setCellValueFactory(cellData -> cellData.getValue().genderNameProperty());
        peopleGenderName.setMinWidth(140);

        tablePeopleGender.getColumns().addAll(peopleGenderName);

        ObservableList<RefPeopleGender> peopleGenderItems = FXCollections.observableArrayList(getService().getAllDataPeopleGender());

        setFunctionPopup(cbpPeopleGender, tablePeopleGender, peopleGenderItems, "genderName", "Jenis Kelamin *");

        //People Religion
        TableView<RefPeopleReligion> tablePeopleReligion = new TableView<>();

        TableColumn<RefPeopleReligion, String> peopleReligionName = new TableColumn<>("Agama");
        peopleReligionName.setCellValueFactory(cellData -> cellData.getValue().religionNameProperty());
        peopleReligionName.setMinWidth(140);

        tablePeopleReligion.getColumns().addAll(peopleReligionName);

        ObservableList<RefPeopleReligion> peopleReligionItems = FXCollections.observableArrayList(getService().getAllDataPeopleReligion());

        setFunctionPopup(cbpPeopleReligion, tablePeopleReligion, peopleReligionItems, "religionName", "Agama *");

        //People Status
        TableView<RefPeopleStatus> tablePeopleStatus = new TableView<>();

        TableColumn<RefPeopleStatus, String> peopleStatusName = new TableColumn<>("Status Perkawinan");
        peopleStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        peopleStatusName.setMinWidth(180);

        tablePeopleStatus.getColumns().addAll(peopleStatusName);

        ObservableList<RefPeopleStatus> peopleStatusItems = FXCollections.observableArrayList(getService().getAllDataPeopleStatus());

        setFunctionPopup(cbpPeopleStatus, tablePeopleStatus, peopleStatusItems, "statusName", "Status Perkawinan *");

        //People Country
        TableView<RefCountry> tablePeopleCountry = new TableView<>();

        TableColumn<RefCountry, String> peopleCountryName = new TableColumn<>("Negara");
        peopleCountryName.setCellValueFactory(cellData -> cellData.getValue().countryNameProperty());
        peopleCountryName.setMinWidth(140);

        tablePeopleCountry.getColumns().addAll(peopleCountryName);

        ObservableList<RefCountry> peopleCountryItems = FXCollections.observableArrayList(getService().getAllDataPeopleCountry());

        setFunctionPopup(cbpPeopleCountry, tablePeopleCountry, peopleCountryItems, "countryName", "Negara *");

        //attached to grid-pane
       /* gpFormDataCustomer.add(cbpCustomerType, 3, 3);
         //        gpFormDataCustomer.add(cbpCustomerStatus, 2, 5);
         gpFormDataCustomer.add(cbpPeopleIdentifierType, 1, 5, 2, 1);
         gpFormDataCustomer.add(cbpPeopleGender, 1, 7, 2, 1);
         gpFormDataCustomer.add(cbpPeopleReligion, 3, 7);
         gpFormDataCustomer.add(cbpPeopleStatus, 1, 8, 2, 1);*/
        AnchorPane.setBottomAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleCountry, 0.0);
        ancCountryLayout.getChildren().clear();
        ancCountryLayout.getChildren().add(cbpPeopleCountry);

        AnchorPane.setBottomAnchor(cbpCustomerType, 0.0);
        AnchorPane.setLeftAnchor(cbpCustomerType, 0.0);
        AnchorPane.setRightAnchor(cbpCustomerType, 0.0);
        AnchorPane.setTopAnchor(cbpCustomerType, 0.0);
        ancCustomerType.getChildren().clear();
        ancCustomerType.getChildren().add(cbpCustomerType);

        AnchorPane.setBottomAnchor(cbpPeopleIdentifierType, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleIdentifierType, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleIdentifierType, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleIdentifierType, 0.0);
        ancPeopleIdentifierType.getChildren().clear();
        ancPeopleIdentifierType.getChildren().add(cbpPeopleIdentifierType);

        AnchorPane.setBottomAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleGender, 0.0);
        ancPeopleGender.getChildren().clear();
        ancPeopleGender.getChildren().add(cbpPeopleGender);

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
    }

    private void refreshDataPopup() {
        //Customer Type
        ObservableList<RefCustomerType> customerTypeItems = FXCollections.observableArrayList(getService().getAllDataCustomerType());
        cbpCustomerType.setItems(customerTypeItems);

//        //Customer Status
//        ObservableList<RefCustomerStatus> customerStatusItems = FXCollections.observableArrayList(getService().getAllDataCustomerStatus());
//        cbpCustomerStatus.setItems(customerStatusItems);
        //People Identifier Type
        ObservableList<RefPeopleIdentifierType> peopleIdentifierTypeItems = FXCollections.observableArrayList(getService().getAllDataPeopleIdentifierType());
        cbpPeopleIdentifierType.setItems(peopleIdentifierTypeItems);

        //People Gender
        ObservableList<RefPeopleGender> peopleGenderItems = FXCollections.observableArrayList(getService().getAllDataPeopleGender());
        cbpPeopleGender.setItems(peopleGenderItems);

        //People Religion
        ObservableList<RefPeopleReligion> peopleReligionItems = FXCollections.observableArrayList(getService().getAllDataPeopleReligion());
        cbpPeopleReligion.setItems(peopleReligionItems);

        //People Status
        ObservableList<RefPeopleStatus> peopleStatusItems = FXCollections.observableArrayList(getService().getAllDataPeopleStatus());
        cbpPeopleStatus.setItems(peopleStatusItems);

        //People Country
        ObservableList<RefCountry> peopleCountryItems = FXCollections.observableArrayList(getService().getAllDataPeopleCountry());
        cbpPeopleCountry.setItems(peopleCountryItems);
    }

    private void setFunctionPopup(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText) {
        table.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != -1) {
                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
                    }
                    cbp.hide();
                });

        cbp.setPropertyNameForFiltered(nameFiltered);

        cbp.setItems(items);

        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(200, 200);

        content.setCenter(table);

        cbp.setPopupEditor(true);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(true);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);

        cbp.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

//        txtCodeCustomer.textProperty().bindBidirectional(selectedData.codeCustomerProperty());
//        Bindings.bindBidirectional(txtCustomerDeposit.textProperty(), selectedData.depositProperty(), new NumberStringConverter() {
//
//            @Override
//            public String toString() {
//                return super.toString(); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public Number fromString(String value) {
////                Number number = super.fromString(value);
////                txtCustomerDeposit.setText(String.valueOf(number));
////                return number;
//                return super.fromString(value);
//            }
//
//        });
        txtCustomerNote.textProperty().bindBidirectional(selectedData.customerNoteProperty());

        if (selectedData.getRegistrationDate() != null) {
//            ~~~System.out.println("a " + selectedData.getRegistrationDate().getClass().getCanonicalName());
            dtpCustomerRegDate.setValue(selectedData.getRegistrationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpCustomerRegDate.setValue(null);
        }
        dtpCustomerRegDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setRegistrationDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        imgPeople.setPreserveRatio(false);
        if (selectedData.getTblPeople().getImageUrl() == null
                || selectedData.getTblPeople().getImageUrl().equals("")) {
            imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + "no_profile_img.gif";
            imgPeople.setImage(new Image("file:///" + imgSourcePath));
        } else {
            imgSourcePath = ClassFolderManager.imageClientRootPath + "/" + selectedData.getTblPeople().getImageUrl();
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
                File file = fc.showOpenDialog(HotelFX.primaryStage);
                if (file != null) {
                    imgSourcePath = file.getAbsoluteFile().toString();
                    imgPeople.setImage(new Image("file:///" + imgSourcePath));
                }
            }
        });

        cbpCustomerType.valueProperty().bindBidirectional(selectedData.refCustomerTypeProperty());
//        cbpCustomerStatus.valueProperty().bindBidirectional(selectedData.refCustomerStatusProperty());

        cbpCustomerType.hide();
//        cbpCustomerStatus.hide();

        txtIdentityNumber.textProperty().bindBidirectional(selectedData.getTblPeople().codePeopleProperty());
//        txtKTPNumber.textProperty().bindBidirectional(selectedData.getTblPeople().ktpnumberProperty());
//        txtPassportNumber.textProperty().bindBidirectional(selectedData.getTblPeople().passportNumberProperty());
//        txtSIMNumber.textProperty().bindBidirectional(selectedData.getTblPeople().simnumberProperty());
        txtFullName.textProperty().bindBidirectional(selectedData.getTblPeople().fullNameProperty());
        txtBirthPlace.textProperty().bindBidirectional(selectedData.getTblPeople().birthPlaceProperty());
        txtPeopleAddress.textProperty().bindBidirectional(selectedData.getTblPeople().addressProperty());
        txtRT.textProperty().bindBidirectional(selectedData.getTblPeople().rtProperty());
        txtRW.textProperty().bindBidirectional(selectedData.getTblPeople().rwProperty());
        txtKecamatan.textProperty().bindBidirectional(selectedData.getTblPeople().kecamatanProperty());
        txtKelurahan.textProperty().bindBidirectional(selectedData.getTblPeople().kelurahanProperty());
        txtKota.textProperty().bindBidirectional(selectedData.getTblPeople().townProperty());
        txtZIPCode.textProperty().bindBidirectional(selectedData.getTblPeople().poscodeProperty());
        txtProvinsi.textProperty().bindBidirectional(selectedData.getTblPeople().regionProperty());
        txtHPNumber.textProperty().bindBidirectional(selectedData.getTblPeople().hpnumberProperty());
//        txtTelpNumber.textProperty().bindBidirectional(selectedData.getTblPeople().telpNumberProperty());
        txtEmail.textProperty().bindBidirectional(selectedData.getTblPeople().emailProperty());
//        txtPeopleNote.textProperty().bindBidirectional(selectedData.getTblPeople().peopleNoteProperty());

        if (selectedData.getTblPeople().getBirthDate() != null) {
//            dtpBirthDate.setValue(selectedData.getTblPeople().getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            ~~~System.out.println("a " + selectedData.getTblPeople().getBirthDate().getClass().getCanonicalName());
            dtpBirthDate.setValue(((java.sql.Date) selectedData.getTblPeople().getBirthDate()).toLocalDate());
        } else {
//            dtpBirthDate.setValue(null);
            dtpBirthDate.setValue(getMaxSelectedDate());
        }
        dtpBirthDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.getTblPeople().setBirthDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        cbpPeopleIdentifierType.valueProperty().bindBidirectional(selectedData.getTblPeople().refPeopleIdentifierTypeProperty());
        cbpPeopleGender.valueProperty().bindBidirectional(selectedData.getTblPeople().refPeopleGenderProperty());
        cbpPeopleReligion.valueProperty().bindBidirectional(selectedData.getTblPeople().refPeopleReligionProperty());
        cbpPeopleStatus.valueProperty().bindBidirectional(selectedData.getTblPeople().refPeopleStatusProperty());
        cbpPeopleCountry.valueProperty().bindBidirectional(selectedData.getTblPeople().refCountryProperty());

        cbpPeopleIdentifierType.hide();
        cbpPeopleGender.hide();
        cbpPeopleReligion.hide();
        cbpPeopleStatus.hide();
        cbpPeopleCountry.hide();

        initTableDataCustomerBankAccount();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        /*txtCodeCustomer.setDisable(true);
         ClassViewSetting.setDisableForAllInputNode(gpFormDataCustomer,
         dataInputStatus == 3,
         txtCodeCustomer);*/

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataCustomerCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblCustomer();
        selectedData.setTblPeople(new TblPeople());
        lblNameCustomer.setText("");
        setSelectedDataToInputForm();
        //open form data customer
        dataCustomerFormShowStatus.set(0);
        dataCustomerFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataCustomerUpdateHandle() {
        if (tableDataCustomer.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = getService().getCustomer(((TblCustomer) tableDataCustomer.getTableView().getSelectionModel().getSelectedItem()).getIdcustomer());
            selectedData.setTblPeople(getService().getPeople(selectedData.getTblPeople().getIdpeople()));
            lblNameCustomer.setText(selectedData.getCodeCustomer() + "-" + selectedData.getTblPeople().getFullName());
            setSelectedDataToInputForm();
            //open form data customer
            dataCustomerFormShowStatus.set(0);
            dataCustomerFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataCustomerShowHandle() {
        if (tableDataCustomer.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getCustomer(((TblCustomer) tableDataCustomer.getTableView().getSelectionModel().getSelectedItem()).getIdcustomer());
            selectedData.setTblPeople(getService().getPeople(selectedData.getTblPeople().getIdpeople()));
            setSelectedDataToInputForm();
            dataCustomerFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataCustomerUnshowHandle() {
        refreshDataTableCustomer();
        dataCustomerFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataCustomerDeleteHandle() {
        if (tableDataCustomer.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblCustomer dummySelectedData = new TblCustomer((TblCustomer) tableDataCustomer.getTableView().getSelectionModel().getSelectedItem());
                dummySelectedData.setTblPeople(new TblPeople(((TblCustomer) tableDataCustomer.getTableView().getSelectionModel().getSelectedItem()).getTblPeople()));
                if (getService().deleteDataCustomer(dummySelectedData)) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data customer
                    refreshDataTableCustomer();
                    dataCustomerFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataCustomerPrintHandle() {

    }

    private void dataCustomerSaveHandle() {
        if (checkDataInputDataCustomer()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblCustomer dummySelectedData = new TblCustomer(selectedData);
                dummySelectedData.setTblPeople(new TblPeople(dummySelectedData.getTblPeople()));
                List<TblCustomerBankAccount> dummyDataCustomerBankAccounts = new ArrayList<>();
                for (TblCustomerBankAccount dataCustomerBankAccount : (List<TblCustomerBankAccount>) tableDataCustomerBankAccount.getTableView().getItems()) {
                    TblCustomerBankAccount dummyDataCustomerBankAccount = new TblCustomerBankAccount(dataCustomerBankAccount);
                    dummyDataCustomerBankAccount.setTblCustomer(dummySelectedData);
                    dummyDataCustomerBankAccount.setTblBankAccount(new TblBankAccount(dummyDataCustomerBankAccount.getTblBankAccount()));
                    dummyDataCustomerBankAccounts.add(dummyDataCustomerBankAccount);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (getService().insertDataCustomer(dummySelectedData,
                                imgSourcePath.split("\\.")[1],
                                dummyDataCustomerBankAccounts) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            try {
                                //save image
                                ClassFolderManager.copyImage(
                                        imgSourcePath,
                                        dummySelectedData.getTblPeople().getImageUrl(),
                                        "Client");
                            } catch (IOException ex) {
                                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //refresh data from table & close form data customer
                            refreshDataTableCustomer();
                            dataCustomerFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (getService().updateDataCustomer(dummySelectedData,
                                imgSourcePath.split("\\.")[1],
                                dummyDataCustomerBankAccounts)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            if (!imgSourcePath.contains(ClassFolderManager.imageClientRootPath)) {
                                try {
                                    //save image
                                    ClassFolderManager.copyImage(
                                            imgSourcePath,
                                            dummySelectedData.getTblPeople().getImageUrl(),
                                            "Client");
                                } catch (IOException ex) {
                                    Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            //refresh data from table & close form data customer
                            refreshDataTableCustomer();
                            dataCustomerFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(getService().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataCustomerCancelHandle() {
        //refresh data from table & close form data customer
        refreshDataTableCustomer();
        dataCustomerFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableCustomer() {
        tableDataCustomer.getTableView().setItems(loadAllDataCustomer());
        cft.refreshFilterItems(tableDataCustomer.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataCustomer() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtIdentityNumber.getText() == null || txtIdentityNumber.getText().equalsIgnoreCase("")) {
            dataInput = false;
            errDataInput += "Nomor Identitas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if ((txtKTPNumber.getText() == null || txtKTPNumber.getText().equalsIgnoreCase(""))
//                && (txtPassportNumber.getText() == null || txtPassportNumber.getText().equalsIgnoreCase(""))
//                && (txtSIMNumber.getText() == null || txtSIMNumber.getText().equalsIgnoreCase(""))) {
//            dataInput = false;
//            errDataInput += "Nomor Identitas (KTP, Passport, SIM) : Salah satu data harus diisi!! \n";
//        }
        if (txtFullName.getText() == null || txtFullName.getText().equalsIgnoreCase("")) {
            dataInput = false;
            errDataInput += "Nama Lengkap : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpCustomerType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Customer : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpCustomerStatus.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Customer Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (cbpPeopleIdentifierType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Identitas : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleGender.getValue() == null) {
            dataInput = false;
            errDataInput += "Jenis Kelamin : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleStatus.getValue() == null) {
            dataInput = false;
            errDataInput += "Status Perkawinan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleReligion.getValue() == null) {
            dataInput = false;
            errDataInput += "Agama : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleCountry.getValue() == null) {
            dataInput = false;
            errDataInput += "Negara : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpCustomerRegDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Registrasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dtpBirthDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Tanggal Lahir : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtBirthPlace.getText() == null || txtBirthPlace.getText().equalsIgnoreCase("")) {
            dataInput = false;
            errDataInput += "Tempat Lahir : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtPeopleAddress.getText() == null || txtPeopleAddress.getText().equalsIgnoreCase("")) {
            dataInput = false;
            errDataInput += "Alamat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtRT.getText() == null || txtRT.getText().equalsIgnoreCase("")) {
//            dataInput = false;
//            errDataInput += "RT : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (txtRW.getText() == null || txtRW.getText().equalsIgnoreCase("")) {
//            dataInput = false;
//            errDataInput += "RW : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (txtKelurahan.getText() == null || txtKelurahan.getText().equalsIgnoreCase("")) {
//            dataInput = false;
//            errDataInput += "Kelurahan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (txtKecamatan.getText() == null || txtKecamatan.getText().equalsIgnoreCase("")) {
//            dataInput = false;
//            errDataInput += "Kecamatan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (txtKota.getText() == null || txtKota.getText().equalsIgnoreCase("")) {
            dataInput = false;
            errDataInput += "Kota : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtZIPCode.getText() == null || txtZIPCode.getText().equalsIgnoreCase("")) {
//            dataInput = false;
//            errDataInput += "Kode POS : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (txtProvinsi.getText() == null || txtProvinsi.getText().equalsIgnoreCase("")) {
//            dataInput = false;
//            errDataInput += "Provinsi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (txtHPNumber.getText() == null || txtHPNumber.getText().equalsIgnoreCase("")) {
            dataInput = false;
            errDataInput += "Nomor HP : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblCustomerBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccount.setMinWidth(140);

        TableColumn<TblCustomerBankAccount, String> bankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderName.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderName.setMinWidth(200);

//        TableColumn<TblCustomerBankAccount, String> bankAccountStatus = new TableColumn("Status");
//        bankAccountStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomerBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefBankAccountHolderStatus().getStatusName(), param.getValue().refBankAccountHolderStatusProperty()));
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
        ObservableList<TblCustomerBankAccount> list = FXCollections.observableArrayList(getService().getAllDataCustomerBankAccount(selectedData.getIdcustomer()));
        for (TblCustomerBankAccount data : list) {
            //set data customer
            data.setTblCustomer(getService().getCustomer(data.getTblCustomer().getIdcustomer()));
            //set data bank account
            data.setTblBankAccount(getService().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
            //set data bank
            data.getTblBankAccount().setTblBank(getService().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
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
        selectedDataCustomerBankAccount.setTblCustomer(selectedData);
        selectedDataCustomerBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data customer bank account
        showCustomerBankAccountDialog();
    }

    public void dataCustomerBankAccountUpdateHandle() {
        if (tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputCustomerBankAccountStatus = 1;
            selectedDataCustomerBankAccount = new TblCustomerBankAccount((TblCustomerBankAccount) tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItem());
            //data bank account
            selectedDataCustomerBankAccount.setTblBankAccount(new TblBankAccount(selectedDataCustomerBankAccount.getTblBankAccount()));
            //data bank
            selectedDataCustomerBankAccount.getTblBankAccount().setTblBank(new TblBank(selectedDataCustomerBankAccount.getTblBankAccount().getTblBank()));
            //open form data customer bank account
            showCustomerBankAccountDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataCustomerBankAccountDeleteHandle() {
        if (tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataCustomerBankAccount.getTableView().getItems().remove(tableDataCustomerBankAccount.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    Stage dialogStagePrint;

    private void showCustomerPrintDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_customer/customer/CustomerReportDialogView.fxml"));

            CustomerReportDialogController controller = new CustomerReportDialogController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStagePrint = new Stage();
            dialogStagePrint.initModality(Modality.WINDOW_MODAL);
            dialogStagePrint.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStagePrint, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStagePrint.initStyle(StageStyle.TRANSPARENT);
            dialogStagePrint.setScene(scene);
            dialogStagePrint.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStagePrint.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    private void showCustomerBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_customer/customer/CustomerBankAccountDialog.fxml"));

            CustomerBankAccountController controller = new CustomerBankAccountController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

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
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FCustomerManager fCustomerManager;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(fCustomerManager == null){
            //set service manager
            fCustomerManager = new FCustomerManagerImpl();
        }
        
        //set splitpane
        setDataCustomerSplitpane();

        //init table
        initTableDataCustomer();

        //init form
        initFormDataCustomer();

        spDataCustomer.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataCustomerFormShowStatus.set(0);
        });
    }

    public CustomerController() {
        
    }
    
    public CustomerController(FeatureCustomerController parentController) {
        this.parentController = parentController;
        this.fCustomerManager = parentController.getFCustomerManager();
    }

    private FeatureCustomerController parentController;

    public FCustomerManager getService() {
        return fCustomerManager;
    }

}
