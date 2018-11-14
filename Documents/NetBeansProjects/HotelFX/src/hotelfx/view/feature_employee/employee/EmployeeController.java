/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_employee.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefEmployeeSalaryType;
import hotelfx.persistence.model.RefEmployeeStatus;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.service.FEmployeeManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_employee.FeatureEmployeeController;
import insidefx.undecorator.Undecorator;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
 *
 * @author Andreas
 */
public class EmployeeController implements Initializable {

    @FXML
    private SplitPane spDataEmployee;
    private DoubleProperty dataEmployeeFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataEmployeeLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataEmployeeSplitPane() {
        spDataEmployee.setDividerPositions(1);
        
        System.out.println("masuk!!");
        dataEmployeeFormShowStatus = new SimpleDoubleProperty(1.0);
        DoubleProperty divPosition = new SimpleDoubleProperty();
        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataEmployeeFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataEmployee.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataEmployee.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
            div.setPosition(divPosition.get());
            }
        });

        dataEmployeeFormShowStatus.addListener((obs, oladVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    tableDataEmployeeLayout.setDisable(false);
                    tableDataEmployeeLayoutDisableLayer.setDisable(true);
                    tableDataEmployeeLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataEmployeeLayout.setDisable(true);
                    tableDataEmployeeLayoutDisableLayer.setDisable(false);
                    tableDataEmployeeLayoutDisableLayer.toFront();
                }
            }
        });

        dataEmployeeFormShowStatus.set(0.0);
    }

    @FXML
    private AnchorPane tableDataEmployeeLayout;
   
    private ClassFilteringTable<TblEmployee> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;
    
    private ClassTableWithControl tableDataEmployee;

    private void initTableDataEmployee() {
        setTableDataEmployee();
        setTableControlDataEmployee();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataEmployee, 15.0);
        AnchorPane.setLeftAnchor(tableDataEmployee, 15.0);
        AnchorPane.setBottomAnchor(tableDataEmployee, 15.0);
        AnchorPane.setRightAnchor(tableDataEmployee, 15.0);
        ancBodyLayout.getChildren().add(tableDataEmployee);
    }

    private void setTableDataEmployee() {
        TableView<TblEmployee> tableView = new TableView();

        TableColumn<TblEmployee, String> idEmployee = new TableColumn("ID" + "\n" + "Karyawan");
        idEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        idEmployee.setMinWidth(90);

        TableColumn<TblEmployee, String> idNumber = new TableColumn("ID");
        idNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> "KTP:" + (param.getValue().getTblPeople().getKtpnumber() != null ? param.getValue().getTblPeople().getKtpnumber() : "-") + "\n"
                        + "ID BPJS Kesehatan:" + (param.getValue().getCodeBpjsks() != null ? "\n" + param.getValue().getCodeBpjsks() : "-") + "\n"
                        + "ID BPJS Ketenaga Kerjaan:" + (param.getValue().getCodeBpjstk() != null ? "\n" + param.getValue().getCodeBpjstk() : "-") + "\n"
                        + "ID Asuransi:" + (param.getValue().getCodeInsurance() != null ? "\n" + param.getValue().getCodeInsurance() : "-"), param.getValue().tblJobProperty()));
        idNumber.setMinWidth(180);

        TableColumn<TblEmployee, String> nameEmployee = new TableColumn("Nama");
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().getTblPeople().fullNameProperty()));
        nameEmployee.setMinWidth(160);

        TableColumn<TblEmployee, String> genderEmployee = new TableColumn("L/P");
        genderEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblPeople().getRefPeopleGender().getIdgender() == 1 ? "P" : "L"), param.getValue().getTblPeople().refPeopleGenderProperty()));
        genderEmployee.setMinWidth(60);
        TableColumn<TblEmployee, String> employeeIdentity = new TableColumn("Identitas Karyawan");
        employeeIdentity.getColumns().addAll(idNumber, nameEmployee, genderEmployee);

        TableColumn<TblEmployee, String> addressEmployee = new TableColumn("Alamat");
        addressEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> "Jalan:" + (param.getValue().getTblPeople().getAddress() != null ? param.getValue().getTblPeople().getAddress() : "-") + "\n"
                        + "RT/RW:" + (param.getValue().getTblPeople().getRt() != null ? param.getValue().getTblPeople().getRt() : "-") + "/"
                        + (param.getValue().getTblPeople().getRw() != null ? param.getValue().getTblPeople().getRw() : "-") + "\n"
                        + "Kelurahan:" + (param.getValue().getTblPeople().getKelurahan() != null ? param.getValue().getTblPeople().getKelurahan() : "-") + "\n"
                        + "Kecamatan:" + (param.getValue().getTblPeople().getKecamatan() != null ? param.getValue().getTblPeople().getKecamatan() : "-") + "\n"
                        + "Kota/Kode Pos:" + "\n" + (param.getValue().getTblPeople().getTown() != null ? param.getValue().getTblPeople().getTown() : "-") + "\n"
                        + "Provinsi:" + (param.getValue().getTblPeople().getRegion() != null ? param.getValue().getTblPeople().getRegion() : "-") + "\n"
                        + "Negara:" + (param.getValue().getTblPeople().getRefCountry() != null ? param.getValue().getTblPeople().getRefCountry().getCountryName() : "-"),
                        param.getValue().tblPeopleProperty()));
        addressEmployee.setMinWidth(200);

        TableColumn<TblEmployee, String> phoneNumberEmployee = new TableColumn("No Telepon/" + "\n" + "No HP");
        phoneNumberEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblPeople().getTelpNumber() != null ? param.getValue().getTblPeople().getTelpNumber() : "-") + "/" + "\n"
                        + param.getValue().getTblPeople().getHpnumber(), param.getValue().tblPeopleProperty()));
        phoneNumberEmployee.setMinWidth(100);

        TableColumn<TblEmployee, String> emailEmployee = new TableColumn("Email");
        emailEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getEmail(), param.getValue().tblPeopleProperty()));
        emailEmployee.setMinWidth(150);

        TableColumn<TblEmployee, String> contactEmployee = new TableColumn("Kontak Karyawan");
        contactEmployee.getColumns().addAll(phoneNumberEmployee, emailEmployee);

        TableColumn<TblEmployee, String> jobEmployee = new TableColumn("Jabatan");
        jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblJob().getJobName(), param.getValue().tblJobProperty()));
        jobEmployee.setMinWidth(100);

        /*TableColumn<TblEmployee,String> groupEmployee = new TableColumn("Department");
         groupEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
         -> Bindings.createStringBinding(() -> param.getValue().getTblGroup().getGroupName(), param.getValue().tblGroupProperty()));
         groupEmployee.setMinWidth(140);*/
        TableColumn<TblEmployee, String> joinDateEmployee = new TableColumn("Masuk");
        joinDateEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getJoinDate()), param.getValue().joinDateProperty()));
        joinDateEmployee.setMinWidth(100);

        TableColumn<TblEmployee, String> offDateEmployee = new TableColumn("Keluar");
        offDateEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getOffDate() != null ? ClassFormatter.dateFormate.format(param.getValue().getOffDate()) : "-",
                        param.getValue().offDateProperty()));
        offDateEmployee.setMinWidth(100);

        TableColumn<TblEmployee, String> dateEmployee = new TableColumn("Tanggal");
        dateEmployee.getColumns().addAll(joinDateEmployee, offDateEmployee);

        TableColumn<TblEmployee, String> typeEmployee = new TableColumn("Tipe" + "\n" + "Karyawan");
        typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefEmployeeType().getTypeName(), param.getValue().refEmployeeTypeProperty()));
        typeEmployee.setMinWidth(100);

        TableColumn<TblEmployee, String> statusEmployee = new TableColumn("Status" + "\n" + "Karyawan");
        statusEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefEmployeeStatus().getStatusName(), param.getValue().refEmployeeStatusProperty()));
        statusEmployee.setMinWidth(80);

        TableColumn<TblEmployee, String> salaryEmployee = new TableColumn("Gaji Pokok");
        salaryEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getEmployeeSalary()), param.getValue().employeeSalaryProperty()));
        salaryEmployee.setMinWidth(140);

        TableColumn<TblEmployee, String> transportAllowance = new TableColumn("Uang Transport");
        transportAllowance.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransportAllowance()), param.getValue().transportAllowanceProperty()));
        transportAllowance.setMinWidth(140);

        TableColumn<TblEmployee, String> lunchAllowance = new TableColumn("Uang Makan");
        lunchAllowance.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getLunchAllowance()), param.getValue().lunchAllowanceProperty()));
        lunchAllowance.setMinWidth(140);

        TableColumn<TblEmployee, String> codeBpjsks = new TableColumn("ID BPJS Kesehatan");
        codeBpjsks.setCellValueFactory(cellData -> cellData.getValue().codeBpjsksProperty());
        codeBpjsks.setMinWidth(180);

        TableColumn<TblEmployee, String> codeBpjstk = new TableColumn("ID BPJS Tenaga Kerja");
        codeBpjstk.setCellValueFactory(cellData -> cellData.getValue().codeBpjstkProperty());
        codeBpjstk.setMinWidth(180);

        TableColumn<TblEmployee, String> idInsurance = new TableColumn("ID Asuransi");
        idInsurance.setCellValueFactory(cellData -> cellData.getValue().codeInsuranceProperty());
        idInsurance.setMinWidth(140);

        TableColumn<TblEmployee, String> nameContact = new TableColumn("Nama");
        nameContact.setCellValueFactory(cellData -> cellData.getValue().emergencyCallNameProperty());
        nameContact.setMinWidth(140);

        TableColumn<TblEmployee, String> numberContact = new TableColumn("Nomor Telepon");
        numberContact.setCellValueFactory(cellData -> cellData.getValue().emergencyCallNumberProperty());
        numberContact.setMinWidth(140);

        TableColumn<TblEmployee, String> emergencyContact = new TableColumn("Kontak Darurat:");
        emergencyContact.getColumns().addAll(nameContact, numberContact);

        //idInsurance.setCellValueFactory(cellData->cellData.getValue().codeInsuranceProperty());
        TableColumn<TblEmployee, String> note = new TableColumn("Keterangan");
        note.setCellValueFactory(cellData -> cellData.getValue().employeeNoteProperty());
        note.setMinWidth(200);

        tableView.getColumns().addAll(idEmployee, jobEmployee, employeeIdentity, addressEmployee, contactEmployee, statusEmployee);
        //nameEmployee, joinDateEmployee, offDateEmployee, typeEmployee, statusEmployee, jobEmployee, groupEmployee, 
        //codeBpjsks, codeBpjstk, idInsurance, emergencyContact, note);
        tableView.setItems(loadAllDataEmployee());

        tableView.setRowFactory(tv -> {
            TableRow<TblEmployee> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataEmployeeUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataEmployeeUpdateHandle();
                            }else{
                                dataEmployeeShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                dataEmployeeUpdateHandle();
//                            }else{
//                                dataEmployeeShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataEmployee = new ClassTableWithControl(tableView);
        
        //set filter
        cft = new ClassFilteringTable<>(
                TblEmployee.class,
                tableDataEmployee.getTableView(),
                tableDataEmployee.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private ObservableList<TblEmployee> loadAllDataEmployee() {
        List<TblEmployee> list = parentController.getFEmployeeManager().getAllDataEmployee();
        for (TblEmployee data : list) {
            //data people
            data.setTblPeople(parentController.getFEmployeeManager().getDataPeople(data.getTblPeople().getIdpeople()));
            //data job
            data.setTblJob(parentController.getFEmployeeManager().getDataJob(data.getTblJob().getIdjob()));
            //data group
            data.setTblGroup(parentController.getFEmployeeManager().getDataGroup(data.getTblGroup().getIdgroup()));
            //data employee type
            data.setRefEmployeeType(parentController.getFEmployeeManager().getDataEmployeeType(data.getRefEmployeeType().getIdtype()));
            //data employee status
            data.setRefEmployeeStatus(parentController.getFEmployeeManager().getDataEmployeeStatus(data.getRefEmployeeStatus().getIdstatus()));
            //data employee salary type
//            data.setRefEmployeeSalaryType(parentController.getFEmployeeManager().getDataEmployeeSalaryType(data.getRefEmployeeSalaryType().getIdtype()));
        }
        return FXCollections.observableArrayList(list);
    }

    private void setTableControlDataEmployee() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                 btnCancel.setVisible(false);
                dataEmployeeCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                btnCancel.setVisible(true);
                dataEmployeeUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataEmployeeDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataEmployee.addButtonControl(buttonControls);
    }

    @FXML
    private AnchorPane anchorFormEmployee;
    @FXML
    private AnchorPane ancEmployeeWarningLetter;
    @FXML
    private TabPane tabEmployeeAndWarningLetter;
    @FXML
    private Tab tabEmployee;
    @FXML
    private Tab tabWarningLetter;
    @FXML
    private ScrollPane spFormDataEmployee;
    @FXML
    private GridPane gpFormDataEmployee;
    @FXML
    private JFXTextField txtKTPNumber;
    @FXML
    private JFXTextField txtFullName;
    @FXML
    private JFXTextField txtBirthPlace;
    @FXML
    private JFXDatePicker dpBirthDate;
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
    private JFXTextField txtKota;
    @FXML
    private JFXTextField txtZIPCode;
    @FXML
    private JFXTextField txtProvinsi;
    @FXML
    private JFXTextField txtHpNumber;
    @FXML
    private JFXTextField txtHomeNumber;
    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtIdEmployee;
    @FXML
    private AnchorPane ancStartContractDateLayout;
    private JFXDatePicker dtpStartContractDate;
    @FXML
    private AnchorPane ancEndContractDateLayout;
    private JFXDatePicker dtpEndContractDate;
    @FXML
    private JFXDatePicker dpJoinDate;
    @FXML
    private JFXDatePicker dpOffDate;
    @FXML
    private JFXTextField txtSalary;
    @FXML
    private JFXTextField txtTransportAllowance;
    @FXML
    private JFXTextField txtLunchAllowance;
    @FXML
    private JFXTextField txtCodeBpjsks;
    @FXML
    private JFXTextField txtCodeBpjstk;
    @FXML
    private JFXTextField txtCodeNPWP;
    @FXML
    private JFXTextField txtRelationFamily;
    @FXML
    private JFXDatePicker dpBPJSksDate;
    @FXML
    private JFXDatePicker dpBPJSktDate;
    @FXML
    private JFXTextField txtIdInsurance;
    @FXML
    private JFXTextField txtNameContactEmergency;
    @FXML
    private JFXTextField txtNumberContactEmergency;

    @FXML
    private JFXTextArea txtNoteEmployee;
    @FXML
    private ImageView imgPeople;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancelForm;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Label lblEmployee;
    
    @FXML
    private AnchorPane cbpPeopleStatusLayout;
    private final JFXCComboBoxPopup cbpPeopleStatus = new JFXCComboBoxPopup(RefPeopleStatus.class);
    @FXML
    private AnchorPane cbpPeopleGenderLayout;
    private final JFXCComboBoxPopup cbpPeopleGender = new JFXCComboBoxPopup(RefPeopleGender.class);
    @FXML
    private AnchorPane cbpPeopleReligionLayout;
    private final JFXCComboBoxPopup cbpPeopleReligion = new JFXCComboBoxPopup(RefPeopleReligion.class);
    @FXML
    private AnchorPane cbpJobLayout;
    private final JFXCComboBoxPopup cbpJob = new JFXCComboBoxPopup(TblJob.class);
    @FXML
    private AnchorPane cbpGroupLayout;
    private final JFXCComboBoxPopup cbpGroup = new JFXCComboBoxPopup(TblGroup.class);
    @FXML
    private AnchorPane cbpEmployeeTypeLayout;
    private final JFXCComboBoxPopup cbpEmployeeType = new JFXCComboBoxPopup(RefEmployeeType.class);
    @FXML
    private AnchorPane cbpEmployeeIsContractLayout;
    private final JFXCComboBoxPopup cbpEmployeeIsContract = new JFXCComboBoxPopup(EmployeeIsContract.class);
    @FXML
    private AnchorPane cbpEmployeeStatusLayout;
    private final JFXCComboBoxPopup cbpEmployeeStatus = new JFXCComboBoxPopup(RefEmployeeStatus.class);
    private final JFXCComboBoxPopup cbpEmployeeSalaryType = new JFXCComboBoxPopup(RefEmployeeSalaryType.class);

    @FXML
    private AnchorPane ancCountryLayout;
    private final JFXCComboBoxPopup<RefCountry> cbpPeopleCountry = new JFXCComboBoxPopup<>(RefCountry.class);

    public TblEmployee selectedData;
    private BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private String imgSourcePath;

    private void initFormDataEmployee() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataEmployee.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });
        
        
       gpFormDataEmployee.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("err:" + e.getMessage());
                }
            });

            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setTooltip(new Tooltip("Simpan (Data Karyawan)"));
        btnSave.setOnAction((e) -> {
            dataEmployeeSaveHandle();
        });
        
        btnCancel.setOnAction((e)->{
            dataEmployeeCancelHandle();
        });
        
       
        initDateCalendar();

        initDataPopup();

        initImportantFieldColor();
        
       // initNumbericField();

        cbpEmployeeType.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (dataInputStatus != 3) {
                if (newVal != null) {
                    if (((RefEmployeeType) newVal).getIdtype() == 0) {   //Staff = '0'
                        cbpEmployeeIsContract.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, true);
                        cbpEmployeeIsContract.setPromptText("Kontrak *");
                        cbpEmployeeIsContract.setDisable(false);
                    } else {
                        cbpEmployeeIsContract.setValue(null);
                        cbpEmployeeIsContract.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
                        cbpEmployeeIsContract.setPromptText("Kontrak");
                        cbpEmployeeIsContract.setDisable(true);
                    }
                } else {
                    cbpEmployeeIsContract.setValue(null);
                    cbpEmployeeIsContract.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
                    cbpEmployeeIsContract.setPromptText("Kontrak");
                    cbpEmployeeIsContract.setDisable(true);
                }
                ObservableList<EmployeeIsContract> employeeIsContractItems = FXCollections.observableArrayList(loadAllDataEmployeeContract());
                cbpEmployeeIsContract.setItems(employeeIsContractItems);
//            } else {
//                cbpEmployeeIsContract.setPromptText("Kontrak");
//                cbpEmployeeIsContract.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
//            }
        });

        cbpEmployeeIsContract.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (dataInputStatus != 3) {
                if (newVal != null) {
                    selectedData.setIsContract(((EmployeeIsContract) newVal).getIsContract());
                    if (((EmployeeIsContract) newVal).getIsContract().equals("Kontrak")) {
                        dtpStartContractDate = new JFXDatePicker();
                        dtpStartContractDate.setLabelFloat(true);
                        dtpStartContractDate.setEditable(false);

                        dtpEndContractDate = new JFXDatePicker();
                        dtpEndContractDate.setLabelFloat(true);
                        dtpEndContractDate.setEditable(false);

                        dtpStartContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, true);
                        dtpEndContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, true);
                        dtpStartContractDate.setPromptText("Awal Kontrak *");
                        dtpEndContractDate.setPromptText("Akhir Kontrak *");
                        dtpStartContractDate.setDisable(false);
                        dtpEndContractDate.setDisable(false);

                        AnchorPane.setBottomAnchor(dtpStartContractDate, 0.0);
                        AnchorPane.setLeftAnchor(dtpStartContractDate, 0.0);
                        AnchorPane.setRightAnchor(dtpStartContractDate, 0.0);
                        AnchorPane.setTopAnchor(dtpStartContractDate, 0.0);
                        ancStartContractDateLayout.getChildren().clear();
                        ancStartContractDateLayout.getChildren().add(dtpStartContractDate);

                        AnchorPane.setBottomAnchor(dtpEndContractDate, 0.0);
                        AnchorPane.setLeftAnchor(dtpEndContractDate, 0.0);
                        AnchorPane.setRightAnchor(dtpEndContractDate, 0.0);
                        AnchorPane.setTopAnchor(dtpEndContractDate, 0.0);
                        ancEndContractDateLayout.getChildren().clear();
                        ancEndContractDateLayout.getChildren().add(dtpEndContractDate);
                    } else {
                        dtpStartContractDate = new JFXDatePicker();
                        dtpStartContractDate.setLabelFloat(true);
                        dtpStartContractDate.setEditable(false);

                        dtpEndContractDate = new JFXDatePicker();
                        dtpEndContractDate.setLabelFloat(true);
                        dtpEndContractDate.setEditable(false);

                        dtpStartContractDate.setValue(null);
                        dtpEndContractDate.setValue(null);
                        dtpStartContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
                        dtpEndContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
                        dtpStartContractDate.setPromptText("Awal Kontrak");
                        dtpEndContractDate.setPromptText("Akhir Kontrak");
                        dtpStartContractDate.setDisable(true);
                        dtpEndContractDate.setDisable(true);

                        AnchorPane.setBottomAnchor(dtpStartContractDate, 0.0);
                        AnchorPane.setLeftAnchor(dtpStartContractDate, 0.0);
                        AnchorPane.setRightAnchor(dtpStartContractDate, 0.0);
                        AnchorPane.setTopAnchor(dtpStartContractDate, 0.0);
                        ancStartContractDateLayout.getChildren().clear();
                        ancStartContractDateLayout.getChildren().add(dtpStartContractDate);

                        AnchorPane.setBottomAnchor(dtpEndContractDate, 0.0);
                        AnchorPane.setLeftAnchor(dtpEndContractDate, 0.0);
                        AnchorPane.setRightAnchor(dtpEndContractDate, 0.0);
                        AnchorPane.setTopAnchor(dtpEndContractDate, 0.0);
                        ancEndContractDateLayout.getChildren().clear();
                        ancEndContractDateLayout.getChildren().add(dtpEndContractDate);
                    }
                } else {
                    selectedData.setIsContract("");

                    dtpStartContractDate = new JFXDatePicker();
                    dtpStartContractDate.setLabelFloat(true);
                    dtpStartContractDate.setEditable(false);

                    dtpEndContractDate = new JFXDatePicker();
                    dtpEndContractDate.setLabelFloat(true);
                    dtpEndContractDate.setEditable(false);

                    dtpStartContractDate.setValue(null);
                    dtpEndContractDate.setValue(null);
                    dtpStartContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
                    dtpEndContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
                    dtpStartContractDate.setPromptText("Awal Kontrak");
                    dtpEndContractDate.setPromptText("Akhir Kontrak");
                    dtpStartContractDate.setDisable(true);
                    dtpEndContractDate.setDisable(true);

                    AnchorPane.setBottomAnchor(dtpStartContractDate, 0.0);
                    AnchorPane.setLeftAnchor(dtpStartContractDate, 0.0);
                    AnchorPane.setRightAnchor(dtpStartContractDate, 0.0);
                    AnchorPane.setTopAnchor(dtpStartContractDate, 0.0);
                    ancStartContractDateLayout.getChildren().clear();
                    ancStartContractDateLayout.getChildren().add(dtpStartContractDate);

                    AnchorPane.setBottomAnchor(dtpEndContractDate, 0.0);
                    AnchorPane.setLeftAnchor(dtpEndContractDate, 0.0);
                    AnchorPane.setRightAnchor(dtpEndContractDate, 0.0);
                    AnchorPane.setTopAnchor(dtpEndContractDate, 0.0);
                    ancEndContractDateLayout.getChildren().clear();
                    ancEndContractDateLayout.getChildren().add(dtpEndContractDate);
                }
//            } else {
//                dtpStartContractDate = new JFXDatePicker();
//                dtpStartContractDate.setLabelFloat(true);
//                dtpStartContractDate.setEditable(false);
//
//                dtpEndContractDate = new JFXDatePicker();
//                dtpEndContractDate.setLabelFloat(true);
//                dtpEndContractDate.setEditable(false);
//
//                dtpStartContractDate.setPromptText("Awal Kontrak");
//                dtpEndContractDate.setPromptText("Akhir Kontrak");
//                dtpStartContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
//                dtpEndContractDate.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, false);
//
//                AnchorPane.setBottomAnchor(dtpStartContractDate, 0.0);
//                AnchorPane.setLeftAnchor(dtpStartContractDate, 0.0);
//                AnchorPane.setRightAnchor(dtpStartContractDate, 0.0);
//                AnchorPane.setTopAnchor(dtpStartContractDate, 0.0);
//                ancStartContractDateLayout.getChildren().clear();
//                ancStartContractDateLayout.getChildren().add(dtpStartContractDate);
//
//                AnchorPane.setBottomAnchor(dtpEndContractDate, 0.0);
//                AnchorPane.setLeftAnchor(dtpEndContractDate, 0.0);
//                AnchorPane.setRightAnchor(dtpEndContractDate, 0.0);
//                AnchorPane.setTopAnchor(dtpEndContractDate, 0.0);
//                ancEndContractDateLayout.getChildren().clear();
//                ancEndContractDateLayout.getChildren().add(dtpEndContractDate);
//            }
        });
        
       // initEmployeeWarningLetter();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dpBirthDate,
                dpJoinDate,
                dpOffDate);
        
        ClassFormatter.setDatePickersEnableDateUntil(getMaxSelectedDate(),
                //                dtpCustomerRegDate,
        dpBirthDate);
//        ClassFormatter.setDatePickersEnableDate(LocalDate.now(),
//                dpBirthDate,
//                dpJoinDate, 
//                dpOffDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtKTPNumber,
                txtFullName,
                dpBirthDate,
                cbpPeopleGender,
                cbpPeopleStatus,
                cbpPeopleReligion,
                dpJoinDate,
                cbpJob,
                cbpGroup,
                cbpEmployeeType,
                cbpEmployeeStatus,
                cbpPeopleCountry,
                //cbpEmployeeSalaryType,
                txtBirthPlace,
                txtAddress,
                txtRT,
                txtRW,
                txtKelurahan,
                txtKecamatan,
                txtKota,
                txtZIPCode,
                txtProvinsi,
                txtHpNumber,
                txtNameContactEmergency,
                txtNumberContactEmergency,
                txtRelationFamily);
                //txtSalary);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtSalary,
                txtTransportAllowance,
                txtLunchAllowance);
    }
    
    private LocalDate getMaxSelectedDate() {
        LocalDate maxSelectedDate = LocalDate.now();
        String employeeMinimumAge = "";
        SysDataHardCode sdhEmployeeMinimumAge = getService().getDataSysDataHardCode((long) 23);  //Guest-MinimumAge = '24'
        if (sdhEmployeeMinimumAge != null
                && sdhEmployeeMinimumAge.getDataHardCodeValue() != null) {
            employeeMinimumAge = sdhEmployeeMinimumAge.getDataHardCodeValue();
        }
        if (!employeeMinimumAge.equals("")) {
            maxSelectedDate = maxSelectedDate.minusYears(Integer.parseInt(employeeMinimumAge));
        }
        return maxSelectedDate;
    }
    
    private void initEmployeeWarningLetter(TblEmployee employee){
     selectedData = employee;
     showDataEmployeeWarningLetter();
    }
    
    private void showDataEmployeeWarningLetter(){
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(HotelFX.class.getResource("view/feature_employee/employee/EmployeeWarningLetterView.fxml"));
       EmployeeWarningLetterController employeeWarningLetterController = new EmployeeWarningLetterController(this);
       loader.setController(employeeWarningLetterController);
       
        try {
            Node subContent = loader.load();
            
            ancEmployeeWarningLetter.getChildren().clear();
            AnchorPane.setTopAnchor(subContent,0.0);
            AnchorPane.setBottomAnchor(subContent,0.0);
            AnchorPane.setLeftAnchor(subContent,0.0);
            AnchorPane.setRightAnchor(subContent,0.0);
            ancEmployeeWarningLetter.getChildren().add(subContent);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initDataPopup() {

        TableView<TblJob> tableJob = new TableView();

        TableColumn<TblJob, String> nameJob = new TableColumn("Jabatan");
        nameJob.setCellValueFactory(cellData -> cellData.getValue().jobNameProperty());
        nameJob.setMinWidth(140);

        tableJob.getColumns().addAll(nameJob);

        ObservableList<TblJob> jobNameItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataJob());
        setFunctionPopup(cbpJob, tableJob, jobNameItems, "jobName", "Jabatan *", 200, 200);

        TableView<TblGroup> tableGroup = new TableView();

        TableColumn<TblGroup, String> nameGroup = new TableColumn("Department");
        nameGroup.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
        nameGroup.setMinWidth(140);

        tableGroup.getColumns().addAll(nameGroup);

        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataGroup());
        setFunctionPopup(cbpGroup, tableGroup, groupItems, "groupName", "Department *", 200, 200);

        TableView<RefPeopleGender> tablePeopleGender = new TableView();

        TableColumn<RefPeopleGender, String> peopleGender = new TableColumn("Jenis Kelamin");
        peopleGender.setCellValueFactory(cellData -> cellData.getValue().genderNameProperty());
        peopleGender.setMinWidth(140);

        tablePeopleGender.getColumns().addAll(peopleGender);

        ObservableList<RefPeopleGender> genderItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleGender());
        setFunctionPopup(cbpPeopleGender, tablePeopleGender, genderItems, "genderName", "Jenis Kelamin *", 200, 200);

        TableView<RefPeopleStatus> tablePeopleStatus = new TableView();

        TableColumn<RefPeopleStatus, String> peopleStatus = new TableColumn("Status");
        peopleStatus.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        peopleStatus.setMinWidth(140);

        tablePeopleStatus.getColumns().addAll(peopleStatus);

        ObservableList<RefPeopleStatus> statusItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleStatus());
        setFunctionPopup(cbpPeopleStatus, tablePeopleStatus, statusItems, "statusName", "Status Kawin *", 200, 200);

        TableView<RefPeopleReligion> tablePeopleReligion = new TableView();

        TableColumn<RefPeopleReligion, String> peopleReligion = new TableColumn("Agama");
        peopleReligion.setCellValueFactory(cellData -> cellData.getValue().religionNameProperty());
        peopleReligion.setMinWidth(140);

        tablePeopleReligion.getColumns().addAll(peopleReligion);

        ObservableList<RefPeopleReligion> religionItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleReligion());
        setFunctionPopup(cbpPeopleReligion, tablePeopleReligion, religionItems, "religionName", "Agama *", 200, 200);

        TableView<RefEmployeeType> tableEmployeeType = new TableView();

        TableColumn<RefEmployeeType, String> employeeType = new TableColumn("Tipe Karyawan");
        employeeType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        employeeType.setMinWidth(140);

        tableEmployeeType.getColumns().addAll(employeeType);

        ObservableList<RefEmployeeType> employeeTypeItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataEmployeeType());
        setFunctionPopup(cbpEmployeeType, tableEmployeeType, employeeTypeItems, "typeName", "Tipe Karyawan *", 200, 200);

        //Is Contract
        TableView<EmployeeIsContract> tableEmployeeIsConract = new TableView();

        TableColumn<EmployeeIsContract, String> isContract = new TableColumn("Kontrak");
        isContract.setCellValueFactory(cellData -> cellData.getValue().isContractProperty());
        isContract.setMinWidth(160);

        tableEmployeeIsConract.getColumns().addAll(isContract);

        ObservableList<EmployeeIsContract> employeeIsContractItems = FXCollections.observableArrayList(loadAllDataEmployeeContract());
        setFunctionPopup(cbpEmployeeIsContract, tableEmployeeIsConract, employeeIsContractItems, "isContract", "Kontrak *", 150, 180);

        TableView<RefEmployeeStatus> tableEmployeeStatus = new TableView();

        TableColumn<RefEmployeeStatus, String> employeeStatus = new TableColumn("Status Karyawan");
        employeeStatus.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        employeeStatus.setMinWidth(160);

        tableEmployeeStatus.getColumns().addAll(employeeStatus);

        ObservableList<RefEmployeeStatus> employeeStatusItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataEmployeeStatus());
        setFunctionPopup(cbpEmployeeStatus, tableEmployeeStatus, employeeStatusItems, "statusName", "Status Karyawan *", 200, 200);

        TableView<RefEmployeeSalaryType> tableSalaryType = new TableView();

        TableColumn<RefEmployeeSalaryType, String> salaryType = new TableColumn("Tipe Gaji");
        salaryType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        salaryType.setMinWidth(140);

        tableSalaryType.getColumns().addAll(salaryType);

        ObservableList<RefEmployeeSalaryType> salaryTypeStatus = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataSalaryType());
        setFunctionPopup(cbpEmployeeSalaryType, tableSalaryType, salaryTypeStatus, "statusName", "Tipe Gaji *", 200, 200);

        //People Country
        TableView<RefCountry> tablePeopleCountry = new TableView<>();

        TableColumn<RefCountry, String> peopleCountryName = new TableColumn<>("Negara");
        peopleCountryName.setCellValueFactory(cellData -> cellData.getValue().countryNameProperty());
        peopleCountryName.setMinWidth(140);

        tablePeopleCountry.getColumns().addAll(peopleCountryName);

        ObservableList<RefCountry> peopleCountryItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleCountry());

        setFunctionPopup(cbpPeopleCountry, tablePeopleCountry, peopleCountryItems, "countryName", "Negara *", 200, 200);

        //attached to grid-pan
        AnchorPane.setBottomAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleGender, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleGender, 0.0);
        cbpPeopleGenderLayout.getChildren().clear();
        cbpPeopleGenderLayout.getChildren().add(cbpPeopleGender);
        
        AnchorPane.setBottomAnchor(cbpPeopleReligion, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleReligion, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleReligion, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleReligion, 0.0);
        cbpPeopleReligionLayout.getChildren().clear();
        cbpPeopleReligionLayout.getChildren().add(cbpPeopleReligion);
        
        AnchorPane.setBottomAnchor(cbpPeopleStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleStatus, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleStatus, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleStatus, 0.0);
        cbpPeopleStatusLayout.getChildren().clear();
        cbpPeopleStatusLayout.getChildren().add(cbpPeopleStatus);
        
        AnchorPane.setBottomAnchor(cbpEmployeeType, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployeeType, 0.0);
        AnchorPane.setRightAnchor(cbpEmployeeType, 0.0);
        AnchorPane.setTopAnchor(cbpEmployeeType, 0.0);
        cbpEmployeeTypeLayout.getChildren().clear();
        cbpEmployeeTypeLayout.getChildren().add(cbpEmployeeType);
        
        AnchorPane.setBottomAnchor(cbpEmployeeIsContract, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployeeIsContract, 0.0);
        AnchorPane.setRightAnchor(cbpEmployeeIsContract, 0.0);
        AnchorPane.setTopAnchor(cbpEmployeeIsContract, 0.0);
        cbpEmployeeIsContractLayout.getChildren().clear();
        cbpEmployeeIsContractLayout.getChildren().add(cbpEmployeeIsContract);
        
        AnchorPane.setBottomAnchor(cbpEmployeeStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployeeStatus, 0.0);
        AnchorPane.setRightAnchor(cbpEmployeeStatus, 0.0);
        AnchorPane.setTopAnchor(cbpEmployeeStatus, 0.0);
        cbpEmployeeStatusLayout.getChildren().clear();
        cbpEmployeeStatusLayout.getChildren().add(cbpEmployeeStatus);
        
        AnchorPane.setBottomAnchor(cbpJob, 0.0);
        AnchorPane.setLeftAnchor(cbpJob, 0.0);
        AnchorPane.setRightAnchor(cbpJob, 0.0);
        AnchorPane.setTopAnchor(cbpJob, 0.0);
        cbpJobLayout.getChildren().clear();
        cbpJobLayout.getChildren().add(cbpJob);
        
        AnchorPane.setBottomAnchor(cbpGroup, 0.0);
        AnchorPane.setLeftAnchor(cbpGroup, 0.0);
        AnchorPane.setRightAnchor(cbpGroup, 0.0);
        AnchorPane.setTopAnchor(cbpGroup, 0.0);
        cbpGroupLayout.getChildren().clear();
        cbpGroupLayout.getChildren().add(cbpGroup);
        
        AnchorPane.setBottomAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setLeftAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setRightAnchor(cbpPeopleCountry, 0.0);
        AnchorPane.setTopAnchor(cbpPeopleCountry, 0.0);
        ancCountryLayout.getChildren().clear();
        ancCountryLayout.getChildren().add(cbpPeopleCountry);
    }

    private List<EmployeeIsContract> loadAllDataEmployeeContract() {
        List<EmployeeIsContract> list = new ArrayList<>();
        list.add(new EmployeeIsContract("Kontrak"));
        list.add(new EmployeeIsContract("Bukan Kontrak"));
//        list.add(new EmployeeIsContract(""));
        return list;
    }

    public class EmployeeIsContract {

        private final StringProperty isContract = new SimpleStringProperty("");

        public EmployeeIsContract(String isContract) {
            this.isContract.set(isContract);
        }

        public StringProperty isContractProperty() {
            return isContract;
        }

        public String getIsContract() {
            return isContractProperty().get();
        }

        public void setIsContract(String isContract) {
            isContractProperty().set(isContract);
        }

        @Override
        public String toString() {
            return getIsContract();
        }

    }

    private void refreshDataPopup() {
        ObservableList<RefPeopleGender> peopleGenderItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleGender());
        cbpPeopleGender.setItems(peopleGenderItems);

        ObservableList<RefPeopleStatus> peopleStatusItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleStatus());
        cbpPeopleStatus.setItems(peopleStatusItems);

        ObservableList<RefPeopleReligion> peopleReligionItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleReligion());
        cbpPeopleReligion.setItems(peopleReligionItems);

        ObservableList<TblJob> jobItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataJob());
        cbpJob.setItems(jobItems);

        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataGroup());
        cbpGroup.setItems(groupItems);

        ObservableList<RefEmployeeType> employeeTypeItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataEmployeeType());
        cbpEmployeeType.setItems(employeeTypeItems);

        ObservableList<EmployeeIsContract> employeeIsContractItems = FXCollections.observableArrayList(loadAllDataEmployeeContract());
        cbpEmployeeIsContract.setItems(employeeIsContractItems);

        ObservableList<RefEmployeeStatus> employeeStatusItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataEmployeeStatus());
        cbpEmployeeStatus.setItems(employeeStatusItems);

        ObservableList<RefEmployeeSalaryType> salaryTypeItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataSalaryType());
        cbpEmployeeSalaryType.setItems(salaryTypeItems);

        //People Country
        ObservableList<RefCountry> peopleCountryItems = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataPeopleCountry());
        cbpPeopleCountry.setItems(peopleCountryItems);
    }

    private void setFunctionPopup(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText,
            double prefWidth,
            double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() != -1) {
                cbp.valueProperty().set(table.getItems().get(newVal.intValue()));
            }
            cbp.hide();
        });

        cbp.setPropertyNameForFiltered(nameFiltered);
        cbp.setItems(items);
        
        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        table.itemsProperty().bind(cbp.filteredItemsProperty());

        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth, prefHeight);

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

        txtKTPNumber.textProperty().bindBidirectional(selectedData.getTblPeople().codePeopleProperty());
        txtFullName.textProperty().bindBidirectional(selectedData.getTblPeople().fullNameProperty());
        txtBirthPlace.textProperty().bindBidirectional(selectedData.getTblPeople().birthPlaceProperty());

     /*   if (selectedData.getTblPeople().getBirthDate() != null) {
            dpBirthDate.setValue(((java.sql.Date) selectedData.getTblPeople().getBirthDate()).toLocalDate());
        } else {
            dpBirthDate.setValue(null);
        }

        dpBirthDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.getTblPeople().setBirthDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        }); */
        
         if (selectedData.getTblPeople().getBirthDate() != null) {
//            dtpBirthDate.setValue(selectedData.getTblPeople().getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            ~~~System.out.println("a " + selectedData.getTblPeople().getBirthDate().getClass().getCanonicalName());
            dpBirthDate.setValue(((java.sql.Date) selectedData.getTblPeople().getBirthDate()).toLocalDate());
        } else {
//            dtpBirthDate.setValue(null);
            dpBirthDate.setValue(getMaxSelectedDate());
        }
        dpBirthDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.getTblPeople().setBirthDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
        
        if(selectedData.getBpjsksDate()!=null){
           dpBPJSksDate.setValue(((java.sql.Date)selectedData.getBpjsksDate()).toLocalDate());
        }
        else{
          dpBPJSksDate.setValue(null);
        }
        
        if(selectedData.getBpjstkDate()!=null){
          dpBPJSktDate.setValue(((java.sql.Date)selectedData.getBpjstkDate()).toLocalDate());
        }
        else{
          dpBPJSktDate.setValue(null);
        }
        
        dpBPJSksDate.valueProperty().addListener((obs,oldVal,newVal)->{
            if(newVal != null){
              selectedData.setBpjsksDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
        
        dpBPJSktDate.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
              selectedData.setBpjstkDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
           }
        });
        
        txtAddress.textProperty().bindBidirectional(selectedData.getTblPeople().addressProperty());
        txtRT.textProperty().bindBidirectional(selectedData.getTblPeople().rtProperty());
        txtRW.textProperty().bindBidirectional(selectedData.getTblPeople().rwProperty());
        txtKecamatan.textProperty().bindBidirectional(selectedData.getTblPeople().kecamatanProperty());
        txtKelurahan.textProperty().bindBidirectional(selectedData.getTblPeople().kelurahanProperty());
        txtKota.textProperty().bindBidirectional(selectedData.getTblPeople().townProperty());
        txtZIPCode.textProperty().bindBidirectional(selectedData.getTblPeople().poscodeProperty());
        txtProvinsi.textProperty().bindBidirectional(selectedData.getTblPeople().regionProperty());
        txtHomeNumber.textProperty().bindBidirectional(selectedData.getTblPeople().telpNumberProperty());
        txtHpNumber.textProperty().bindBidirectional(selectedData.getTblPeople().hpnumberProperty());
        txtCodeNPWP.textProperty().bindBidirectional(selectedData.codeNpwpProperty());
        txtRelationFamily.textProperty().bindBidirectional(selectedData.emergencyRelationFamilyProperty());
        
        cbpPeopleGender.valueProperty().bindBidirectional(selectedData.getTblPeople().refPeopleGenderProperty());
        cbpPeopleStatus.valueProperty().bindBidirectional(selectedData.getTblPeople().refPeopleStatusProperty());
        cbpPeopleReligion.valueProperty().bindBidirectional(selectedData.getTblPeople().refPeopleReligionProperty());
        cbpPeopleCountry.valueProperty().bindBidirectional(selectedData.getTblPeople().refCountryProperty());

        cbpPeopleGender.hide();
        cbpPeopleStatus.hide();
        cbpPeopleReligion.hide();
        cbpPeopleCountry.hide();

        txtEmail.textProperty().bindBidirectional(selectedData.getTblPeople().emailProperty());

        txtIdEmployee.textProperty().bindBidirectional(selectedData.codeEmployeeProperty());
        if (selectedData.getJoinDate() != null) {
            dpJoinDate.setValue(selectedData.getJoinDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dpJoinDate.setValue(null);
        }

        dpJoinDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setJoinDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        if (selectedData.getOffDate() != null) {
            dpOffDate.setValue(selectedData.getOffDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dpOffDate.setValue(null);
        }

        dpOffDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setOffDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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

        cbpJob.valueProperty().bindBidirectional(selectedData.tblJobProperty());
        cbpGroup.valueProperty().bindBidirectional(selectedData.tblGroupProperty());
        cbpEmployeeStatus.valueProperty().bindBidirectional(selectedData.refEmployeeStatusProperty());

        cbpEmployeeType.valueProperty().bindBidirectional(selectedData.refEmployeeTypeProperty());

        cbpEmployeeIsContract.setValue(new EmployeeIsContract(selectedData.getIsContract()));

        if (selectedData.getContractStartDate() != null) {
            dtpStartContractDate.setValue(((java.sql.Date) selectedData.getContractStartDate()).toLocalDate());
        } else {
            dtpStartContractDate.setValue(null);
        }
        if (selectedData.getContractEndDate() != null) {
            dtpEndContractDate.setValue(((java.sql.Date) selectedData.getContractEndDate()).toLocalDate());
        } else {
            dtpEndContractDate.setValue(null);
        }

        cbpEmployeeSalaryType.valueProperty().bindBidirectional(selectedData.refEmployeeSalaryTypeProperty());

        cbpJob.hide();
        cbpGroup.hide();
        cbpEmployeeStatus.hide();
        cbpEmployeeType.hide();
        cbpEmployeeIsContract.hide();
        cbpEmployeeSalaryType.hide();

       // Bindings.bindBidirectional(txtSalary.textProperty(), selectedData.employeeSalaryProperty(), new ClassFormatter.CBigDecimalStringConverter());
       // Bindings.bindBidirectional(txtTransportAllowance.textProperty(), selectedData.transportAllowanceProperty(), new ClassFormatter.CBigDecimalStringConverter());
       // Bindings.bindBidirectional(txtLunchAllowance.textProperty(), selectedData.lunchAllowanceProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtCodeBpjsks.textProperty().bindBidirectional(selectedData.codeBpjsksProperty());
        txtCodeBpjstk.textProperty().bindBidirectional(selectedData.codeBpjstkProperty());
        txtCodeNPWP.textProperty().bindBidirectional(selectedData.codeNpwpProperty());
//        txtIdInsurance.textProperty().bindBidirectional(selectedData.codeInsuranceProperty());
        txtNameContactEmergency.textProperty().bindBidirectional(selectedData.emergencyCallNameProperty());
        txtNumberContactEmergency.textProperty().bindBidirectional(selectedData.emergencyCallNumberProperty());
        txtNoteEmployee.textProperty().bindBidirectional(selectedData.employeeNoteProperty());

        initTableDataEmployeeBankAccount();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtIdEmployee.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataEmployee,
                dataInputStatus == 3,
                txtIdEmployee,
                cbpEmployeeIsContract,
                dtpStartContractDate,
                dtpEndContractDate);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    int dataInputStatus = 0;

    public void dataEmployeeCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblEmployee();
        selectedData.setTblPeople(new TblPeople());
        tabEmployeeAndWarningLetter.getTabs().clear();
        tabEmployeeAndWarningLetter.getTabs().addAll(tabEmployee);
        selectedData.setIsContract("");
        lblEmployee.setText("");
        setSelectedDataToInputForm();
        dataEmployeeFormShowStatus.set(0);
        dataEmployeeFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    public void dataEmployeeUpdateHandle() {
        if (tableDataEmployee.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFEmployeeManager().getDataEmployee(((TblEmployee) tableDataEmployee.getTableView().getSelectionModel().getSelectedItem()).getIdemployee());    
            selectedData.setTblPeople(parentController.getFEmployeeManager().getDataPeople(selectedData.getTblPeople().getIdpeople()));
            tabEmployeeAndWarningLetter.getTabs().clear();
            tabEmployeeAndWarningLetter.getTabs().addAll(tabEmployee,tabWarningLetter);
            lblEmployee.setText(selectedData.getCodeEmployee()+" - "+selectedData.getTblPeople().getFullName());
            setSelectedDataToInputForm();
            initEmployeeWarningLetter(selectedData);
            //showDataEmployeeWarningLetter();
            dataEmployeeFormShowStatus.set(0);
            dataEmployeeFormShowStatus.set(1);
            //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataEmployeeShowHandle() {
        if (tableDataEmployee.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFEmployeeManager().getDataEmployee(((TblEmployee) tableDataEmployee.getTableView().getSelectionModel().getSelectedItem()).getIdemployee());
            selectedData.setTblPeople(parentController.getFEmployeeManager().getDataPeople(selectedData.getTblPeople().getIdpeople()));
            setSelectedDataToInputForm();
            dataEmployeeFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataEmployeeUnshowHandle() {
        refreshDataTableEmployee();
        dataEmployeeFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    public void dataEmployeeDeleteHandle() {
        if (tableDataEmployee.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblEmployee dummySelectedData = new TblEmployee((TblEmployee) tableDataEmployee.getTableView().getSelectionModel().getSelectedItem());
                dummySelectedData.setTblPeople(new TblPeople(((TblEmployee) tableDataEmployee.getTableView().getSelectionModel().getSelectedItem()).getTblPeople()));
                if (parentController.getFEmployeeManager().deleteDataEmployee(dummySelectedData)) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    refreshDataTableEmployee();
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFEmployeeManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataEmployeeSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if(dtpStartContractDate.getValue() != null){
                    selectedData.setContractStartDate(Date.from(dtpStartContractDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }else{
                    selectedData.setContractStartDate(null);
                }
                if(dtpEndContractDate.getValue() != null){
                    selectedData.setContractEndDate(Date.from(dtpEndContractDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }else{
                    selectedData.setContractEndDate(null);
                }
                //dummy entry
                TblEmployee dummySelectedData = new TblEmployee(selectedData);
                dummySelectedData.setTblPeople(new TblPeople(selectedData.getTblPeople()));
                List<TblEmployeeBankAccount> dummyDataEmployeeBankAccounts = new ArrayList<>();
                for (TblEmployeeBankAccount dataEmployeeBankAccount : (List<TblEmployeeBankAccount>) tableDataEmployeeBankAccount.getTableView().getItems()) {
                    TblEmployeeBankAccount dummyDataEmployeeBankAccount = new TblEmployeeBankAccount(dataEmployeeBankAccount);
                    dummyDataEmployeeBankAccount.setTblEmployeeByIdemployee(dummySelectedData);
                    dummyDataEmployeeBankAccount.setTblBankAccount(new TblBankAccount(dataEmployeeBankAccount.getTblBankAccount()));
                    dummyDataEmployeeBankAccounts.add(dummyDataEmployeeBankAccount);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFEmployeeManager().insertDataEmployee(dummySelectedData,
                                imgSourcePath.split("\\.")[1],
                                dummyDataEmployeeBankAccounts) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            try {
                                ClassFolderManager.copyImage(imgSourcePath,
                                        dummySelectedData.getTblPeople().getImageUrl(),
                                        "Client");
                            } catch (IOException ex) {
                                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            refreshDataTableEmployee();
                            dataEmployeeFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFEmployeeManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFEmployeeManager().updateDataEmployee(dummySelectedData,
                                imgSourcePath.split("\\.")[1],
                                dummyDataEmployeeBankAccounts)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            if (!imgSourcePath.contains(ClassFolderManager.imageClientRootPath)) {
                                try {
                                    ClassFolderManager.copyImage(imgSourcePath,
                                            dummySelectedData.getTblPeople().getImageUrl(),
                                            "Client");
                                } catch (IOException ex) {
                                    Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            refreshDataTableEmployee();
                            dataEmployeeFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFEmployeeManager().getErrorMessage(), null);
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

    public void dataEmployeeCancelHandle() {
        refreshDataTableEmployee();
        dataEmployeeFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableEmployee(){
        tableDataEmployee.getTableView().setItems(loadAllDataEmployee());
        cft.refreshFilterItems(tableDataEmployee.getTableView().getItems());
    }
    
    private String errDataInput;

    public boolean checkDataInput() {
        boolean check = true;
        errDataInput = "";
        if (txtKTPNumber.getText() == null || txtKTPNumber.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Nomor KTP : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtFullName.getText() == null || txtFullName.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Nama Lengkap : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dpBirthDate.getValue() == null) {
            check = false;
            errDataInput += "Tanggal Lahir : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleGender.getValue() == null) {
            check = false;
            errDataInput += "Jenis Kelamin : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleStatus.getValue() == null) {
            check = false;
            errDataInput += "Status Pernikahan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleReligion.getValue() == null) {
            check = false;
            errDataInput += "Agama : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dpJoinDate.getValue() == null) {
            check = false;
            errDataInput += "Tanggal Masuk : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        /*if (dpOffDate.getValue() == null) {
            check = false;
            errDataInput += "Tanggal Keluar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }*/
        if (cbpJob.getValue() == null) {
            check = false;
            errDataInput += "Jabatan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpGroup.getValue() == null) {
            check = false;
            errDataInput += "Group : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpEmployeeType.getValue() == null) {
            check = false;
            errDataInput += "Tipe Karyawan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (((RefEmployeeType) cbpEmployeeType.getValue()).getIdtype() == 0) { //Staff = '0'
                if (cbpEmployeeIsContract.getValue() == null) {
                    check = false;
                    errDataInput += "Status Kontrak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                } else {
                    if (((EmployeeIsContract) cbpEmployeeIsContract.getValue()).getIsContract().equals("")) {
                        check = false;
                        errDataInput += "Status Kontrak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                    } else {
                        if (((EmployeeIsContract) cbpEmployeeIsContract.getValue()).getIsContract().equals("Kontrak")) {
                            if (dtpStartContractDate.getValue() == null) {
                                check = false;
                                errDataInput += "Tgl. Awal Kontrak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                            } else {
                                if (dtpEndContractDate.getValue() == null) {
                                    check = false;
                                    errDataInput += "Tgl. Akhir Kontrak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                                } else {
                                    if (dtpEndContractDate.getValue().isBefore(dtpStartContractDate.getValue())) {
                                        check = false;
                                        errDataInput += "Tgl. Akhir Kontrak tidak boleh lebih awal dari Tgl. Awal Kontrak \n";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (cbpEmployeeStatus.getValue() == null) {
            check = false;
            errDataInput += "Status Karyawan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPeopleCountry.getValue() == null) {
            check = false;
            errDataInput += "Negara : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
       /* if (cbpEmployeeSalaryType.getValue() == null) {
            check = false;
            errDataInput += "Tipe Gaji : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }*/
        if (txtBirthPlace.getText() == null || txtBirthPlace.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Tempat Lahir : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtAddress.getText() == null || txtAddress.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Alamat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRT.getText() == null || txtRT.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "RT : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRW.getText() == null || txtRW.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "RW: " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtKelurahan.getText() == null || txtKelurahan.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Kelurahan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtKecamatan.getText() == null || txtKecamatan.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Kecamatan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtKota.getText() == null || txtKota.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Kota : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtZIPCode.getText() == null || txtZIPCode.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Kode POS : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtProvinsi.getText() == null || txtProvinsi.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Provinsi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtHpNumber.getText() == null || txtHpNumber.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Nomor HP : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        
        if( txtNameContactEmergency.getText()==null || txtNameContactEmergency.getText().equalsIgnoreCase("")){
           check = false;
           errDataInput += "Nama Kontak :"+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(txtNumberContactEmergency.getText()==null || txtNumberContactEmergency.getText().equalsIgnoreCase("")){
          check = false;
           errDataInput += "Nomor Kontak :"+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(txtRelationFamily.getText()==null || txtRelationFamily.getText().equalsIgnoreCase("")){
          check = false;
          errDataInput += "Hubungan Keluarga :"+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
      /*  if (txtSalary.getText() == null 
                || txtSalary.getText().equalsIgnoreCase("")
                || txtSalary.getText().equalsIgnoreCase("-")) {
            check = false;
            errDataInput += "Gaji Pokok : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }else{
            if(selectedData.getEmployeeSalary().compareTo(new BigDecimal("0")) < 1){
                check = false;
                errDataInput += "Gaji Pokok : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        if (txtTransportAllowance.getText() == null 
                || txtTransportAllowance.getText().equalsIgnoreCase("")
                || txtTransportAllowance.getText().equalsIgnoreCase("-")) {
            check = false;
            errDataInput += "Tunjangan Transportasi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }else{
            if(selectedData.getTransportAllowance().compareTo(new BigDecimal("0")) == -1){
                check = false;
                errDataInput += "Tunjangan Transportasi : Tidak boleh lebih kecil dari '0' \n";
            }
        }
        if (txtLunchAllowance.getText() == null 
                || txtLunchAllowance.getText().equalsIgnoreCase("")
                || txtLunchAllowance.getText().equalsIgnoreCase("-")) {
            check = false;
            errDataInput += "Tunjangan Makan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }else{
            if(selectedData.getLunchAllowance().compareTo(new BigDecimal("0")) == -1){
                check = false;
                errDataInput += "Tunjangan Makan : Tidak boleh lebih kecil dari '0' \n";
            }
        }*/
        return check;
    }

    /**
     * TABLE DATA EMPLOYEE BANK ACCOUNT
     */
    @FXML
    private AnchorPane tableDataEmployeeBankAccountLayout;

    public ClassTableWithControl tableDataEmployeeBankAccount;

    private void initTableDataEmployeeBankAccount() {
        //set table
        setTableDataEmployeeBankAccount();
        //set control
        setTableControlDataEmployeeBankAccount();
        //set table-control to content-view
        tableDataEmployeeBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataEmployeeBankAccount, 0.0);
        AnchorPane.setLeftAnchor(tableDataEmployeeBankAccount, 0.0);
        AnchorPane.setRightAnchor(tableDataEmployeeBankAccount, 0.0);
        AnchorPane.setTopAnchor(tableDataEmployeeBankAccount, 0.0);
        tableDataEmployeeBankAccountLayout.getChildren().add(tableDataEmployeeBankAccount);
    }

    private void setTableDataEmployeeBankAccount() {
        TableView<TblEmployeeBankAccount> tableView = new TableView();

        TableColumn<TblEmployeeBankAccount, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblEmployeeBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccount.setMinWidth(140);

        TableColumn<TblEmployeeBankAccount, String> bankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderName.setMinWidth(200);

//        TableColumn<TblEmployeeBankAccount, String> bankAccountStatus = new TableColumn("Status");
//        bankAccountStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefBankAccountHolderStatus().getStatusName(), param.getValue().refBankAccountHolderStatusProperty()));
        tableView.getColumns().addAll(bankName, bankAccount, bankAccountHolderName);
        tableView.setItems(loadAllDataEmployeeBankAccount());
        tableDataEmployeeBankAccount = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataEmployeeBankAccount() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataEmployeeBankAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataEmployeeBankAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataEmployeeBankAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataEmployeeBankAccount.addButtonControl(buttonControls);
    }

    private ObservableList<TblEmployeeBankAccount> loadAllDataEmployeeBankAccount() {
        ObservableList<TblEmployeeBankAccount> list = FXCollections.observableArrayList(parentController.getFEmployeeManager().getAllDataEmployeeBankAccount(selectedData.getIdemployee()));
        for (TblEmployeeBankAccount data : list) {
            //set data employee
            data.setTblEmployeeByIdemployee(parentController.getFEmployeeManager().getDataEmployee(data.getTblEmployeeByIdemployee().getIdemployee()));
            //set data bank account
            data.setTblBankAccount(parentController.getFEmployeeManager().getDataBankAccount(data.getTblBankAccount().getIdbankAccount()));
            //set data bank
            data.getTblBankAccount().setTblBank(parentController.getFEmployeeManager().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
        }
        return list;
    }

    public TblEmployeeBankAccount selectedDataEmployeeBankAccount;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputEmployeeBankAccountStatus = 0;

    public Stage dialogStage;

    public void dataEmployeeBankAccountCreateHandle() {
        dataInputEmployeeBankAccountStatus = 0;
        selectedDataEmployeeBankAccount = new TblEmployeeBankAccount();
        selectedDataEmployeeBankAccount.setTblEmployeeByIdemployee(selectedData);
        selectedDataEmployeeBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data employee bank account
        showEmployeeBankAccountDialog();
    }

    public void dataEmployeeBankAccountUpdateHandle() {
        if (tableDataEmployeeBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputEmployeeBankAccountStatus = 1;
            selectedDataEmployeeBankAccount = new TblEmployeeBankAccount((TblEmployeeBankAccount) tableDataEmployeeBankAccount.getTableView().getSelectionModel().getSelectedItem());
            //data bank account
            selectedDataEmployeeBankAccount.setTblBankAccount(new TblBankAccount(selectedDataEmployeeBankAccount.getTblBankAccount()));
            //data bank
            selectedDataEmployeeBankAccount.getTblBankAccount().setTblBank(new TblBank(selectedDataEmployeeBankAccount.getTblBankAccount().getTblBank()));
            //open form data employee bank account
            showEmployeeBankAccountDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataEmployeeBankAccountDeleteHandle() {
        if (tableDataEmployeeBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataEmployeeBankAccount.getTableView().getItems().remove(tableDataEmployeeBankAccount.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showEmployeeBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_employee/employee/EmployeeBankAccountDialog.fxml"));

            EmployeeBankAccountController controller = new EmployeeBankAccountController(this);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataEmployeeSplitPane();
        initTableDataEmployee();
        initFormDataEmployee();

        spDataEmployee.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataEmployeeFormShowStatus.set(0);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EmployeeController(FeatureEmployeeController parentController) {
        this.parentController = parentController;
    }

    private final FeatureEmployeeController parentController;

    public FEmployeeManager getService() {
        return parentController.getFEmployeeManager();
    }

}
