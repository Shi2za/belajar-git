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
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefLostFoundStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
import hotelfx.persistence.model.TblLostInformation;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.service.FLostFoundInformationManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_lostfound_information.FeatureLostFoundInformationController;
import hotelfx.view.feature_schedule.employee_schedule.EmployeeScheduleController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class LostInformationController implements Initializable {

    @FXML
    private SplitPane spDataLostInformation;
    private DoubleProperty dataLostInformationFormShowStatus;

    @FXML
    private AnchorPane contentLayout;
    @FXML
    private AnchorPane tableDataLostInformationLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataLostInformationSplitpane() {
        spDataLostInformation.setDividerPositions(1);

        dataLostInformationFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataLostInformationFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataLostInformation.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataLostInformation.getDividers().get(0);
        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataLostInformationFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0) {
                    tableDataLostInformationLayout.setDisable(false);
                    tableDataLostInformationLayoutDisableLayer.setDisable(true);
                    tableDataLostInformationLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataLostInformationLayout.setDisable(true);
                    tableDataLostInformationLayoutDisableLayer.setDisable(false);
                    tableDataLostInformationLayoutDisableLayer.toFront();
                }
            }
        });

        dataLostInformationFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataLostInformationLayout;

    private ClassFilteringTable<TblLostInformation> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    public ClassTableWithControl tableDataLostInformation;

    private void initTableLostInformation() {
        setTableDataLostInformation();

        setTableControlDataLostInformation();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataLostInformation, 15.0);
        AnchorPane.setBottomAnchor(tableDataLostInformation, 15.0);
        AnchorPane.setLeftAnchor(tableDataLostInformation, 15.0);
        AnchorPane.setRightAnchor(tableDataLostInformation, 15.0);

        ancBodyLayout.getChildren().add(tableDataLostInformation);
    }

    private void setTableDataLostInformation() {
        TableView<TblLostInformation> tableView = new TableView();
        TableColumn<TblLostInformation, String> codeLost = new TableColumn("ID");
        codeLost.setMinWidth(90);
        codeLost.setCellValueFactory(cellData -> cellData.getValue().codeLostProperty());
        TableColumn<TblLostInformation, String> dateLostInformation = new TableColumn("Tanggal" + "\n" + "Lapor");
        dateLostInformation.setMinWidth(90);
        dateLostInformation.setCellValueFactory((TableColumn.CellDataFeatures<TblLostInformation, String> param)
                -> Bindings.createStringBinding(() -> (new SimpleDateFormat("dd MMM YYYY").format(param.getValue().getLostDate())), param.getValue().lostDateProperty()));
        TableColumn<TblLostInformation, String> itemName = new TableColumn("Nama\nBarang");
        itemName.setMinWidth(110);
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        TableColumn<TblLostInformation, String> lostLocation = new TableColumn(" Tempat Kehilangan");
        lostLocation.setMinWidth(140);
        lostLocation.setCellValueFactory(cellData -> cellData.getValue().lostLocationProperty());
        TableColumn<TblLostInformation, String> peopleName = new TableColumn("Nama");
        peopleName.setMinWidth(120);
        peopleName.setCellValueFactory((TableColumn.CellDataFeatures<TblLostInformation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().tblPeopleProperty()));
        TableColumn<TblLostInformation, String> peopleAddress = new TableColumn("Alamat");
        peopleAddress.setMinWidth(160);
        peopleAddress.setCellValueFactory((TableColumn.CellDataFeatures<TblLostInformation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getAddress() + "\n" + "Kota: " + param.getValue().getTblPeople().getTown() + "\nNegara: " + param.getValue().getTblPeople().getRefCountry().getCountryName(), param.getValue().tblPeopleProperty()));
        TableColumn<TblLostInformation, String> peopleContact = new TableColumn("Kontak");
        peopleContact.setMinWidth(140);
        peopleContact.setCellValueFactory((TableColumn.CellDataFeatures<TblLostInformation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getHpnumber() == null && param.getValue().getTblPeople().getEmail() == null ? ""
                                : param.getValue().getTblPeople().getHpnumber() == null && param.getValue().getTblPeople().getEmail() != null ? "Email:" + param.getValue().getTblPeople().getEmail()
                                        : param.getValue().getTblPeople().getEmail() == null && param.getValue().getTblPeople().getHpnumber() != null ? "HP:" + param.getValue().getTblPeople().getHpnumber()
                                                : "HP: " + param.getValue().getTblPeople().getHpnumber() + "\n"
                                                + "Email: " + param.getValue().getTblPeople().getEmail(), param.getValue().tblPeopleProperty()));
        TableColumn<TblLostInformation, String> peopleInformation = new TableColumn("Informasi Pelapor");
        peopleInformation.getColumns().addAll(peopleName, peopleAddress, peopleContact);
        TableColumn<TblLostInformation, String> lostNote = new TableColumn("Keterangan");
        lostNote.setMinWidth(160);
        lostNote.setCellValueFactory(cellData -> cellData.getValue().lostNoteProperty());
        TableColumn<TblLostInformation, String> lostStatus = new TableColumn("Status");
        lostStatus.setMinWidth(120);
        lostStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblLostInformation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefLostFoundStatus().getIdstatus() == 2 ? getStatusReturn(param.getValue().getIdlost()) : param.getValue().getRefLostFoundStatus().getStatusName(), param.getValue().refLostFoundStatusProperty()));

        tableView.getColumns().addAll(codeLost, dateLostInformation, itemName, lostLocation, peopleInformation, lostNote, lostStatus);
        tableView.setItems(loadAllDataLostInformation());

        tableView.setRowFactory(tv -> {
            TableRow<TblLostInformation> row = new TableRow();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataLostInformationUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                if (((TblLostInformation) row.getItem()).getRefLostFoundStatus().getIdstatus() == 0) {
                                    dataLostInformationUpdateHandleDetail();
                                } else {
                                    dataLostInformationShowHandle();
                                }
                            } else {
                                dataLostInformationShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                if (((TblLostInformation) row.getItem()).getRefLostFoundStatus().getIdstatus() == 0) {
//                                    dataLostInformationUpdateHandleDetail();
//                                } else {
//                                    dataLostInformationShowHandle();
//                                }
//                            } else {
//                                dataLostInformationShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });
        tableDataLostInformation = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblLostInformation.class,
                tableDataLostInformation.getTableView(),
                tableDataLostInformation.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public ObservableList<TblLostInformation> loadAllDataLostInformation() {
        return FXCollections.observableArrayList(parentController.getFLostFoundInformation().getAllDataLostInformation());
    }

    public String getStatusReturn(long id) {
        String status = "";
        List<TblLostFoundInformationDetail> listLostFound = new ArrayList();
        listLostFound = parentController.getFLostFoundInformation().getAllDataReturn();
        for (TblLostFoundInformationDetail dataLostFound : listLostFound) {
            if (dataLostFound.getTblLostInformation().getIdlost() == id) {
                status = "Dikembalikan oleh :" + dataLostFound.getReturnName() + "\n Tanggal :"
                        + new SimpleDateFormat("dd MMM yyyy").format(dataLostFound.getReturnDate());
            }
        }
        return status;
    }

    private void setTableControlDataLostInformation() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;

        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //dataLostInformationRefresh();

                dataLostInformationCreateHandle(selectedData);

            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataLostInformationUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataLostInformationDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Dikembalikan");
            buttonControl.setOnMouseClicked((e) -> {
                dataLostInformationReturnHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tidak Terpakai");
            buttonControl.setOnMouseClicked((e) -> {
                dataLostInformationSetDiscardHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataLostInformation.addButtonControl(buttonControls);
    }

    @FXML
    private AnchorPane formAnchor;
    @FXML
    private ScrollPane spFormDataLostInformation;
    @FXML
    private GridPane gpFormDataLostInformation;
    @FXML
    private JFXTextField txtIdLost;
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
    private JFXTextField txtItemName;
    @FXML
    private JFXDatePicker dpDateLost;
    @FXML
    private JFXTextArea txtLostLocation;
    @FXML
    private JFXTextArea txtNote;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private AnchorPane cbpPeopleLayout;
    @FXML
    private AnchorPane cbpCountryLayout;
    @FXML
    private Label lblLostInformation;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private JFXCComboBoxTablePopup<TblPeople> cbpPeople;
    private JFXCComboBoxTablePopup<RefCountry> cbpCountry;

    public TblLostInformation selectedData;
    private int dataInputStatus;

    private void initFormDataLostInformation() {
        isFormScroll.addListener((obs, oldScroll, nowScroll) -> {
            spFormDataLostInformation.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        gpFormDataLostInformation.setOnScroll((ScrollEvent scroll) -> {
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
                    System.out.println("err>" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        initDataPopupPeopleLayout();
        initDataPopupCountryLayout();

        btnAdd.setOnAction((a) -> {
            //dataLostInformationCreateHandle();
            //selectedData = new TblLostInformation();
            //selectedData.setTblPeople(new TblPeople());

            showAddPeopleDialog();
            setSelectedDataToInputForm();

            // cbpPeople.valueProperty().set(selectedData.getTblPeople());
            //System.out.println("diAdd:"+selectedData.getTblPeople().getFullName());
            //dataLostInformationCreateHandle();
        });

        btnSave.setOnAction((e) -> {
            dataLostInformationSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
            dataLostInformationCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();
    }

    private void initDataPopupPeopleLayout() {
        initDataPopupPeople();

        cbpPeopleLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpPeople, 0.0);
        AnchorPane.setBottomAnchor(cbpPeople, 0.0);
        AnchorPane.setLeftAnchor(cbpPeople, 0.0);
        AnchorPane.setRightAnchor(cbpPeople, 0.0);
        cbpPeopleLayout.getChildren().add(cbpPeople);

    }

    private void initDataPopupCountryLayout() {

        initDataPopupCountry();

        cbpCountryLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpCountry, 0.0);
        AnchorPane.setBottomAnchor(cbpCountry, 0.0);
        AnchorPane.setLeftAnchor(cbpCountry, 0.0);
        AnchorPane.setRightAnchor(cbpCountry, 0.0);
        cbpCountryLayout.getChildren().add(cbpCountry);
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern, dpDateLost);
        ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(), dpDateLost);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtItemName,
                dpDateLost,
                txtLostLocation,
                cbpPeople,
                txtNote);
    }

    private void initDataPopupPeople() {
        TableView<TblPeople> tblPeople = new TableView();
        TableColumn<TblPeople, String> idPeople = new TableColumn("ID");
        idPeople.setMinWidth(120);
        idPeople.setCellValueFactory(cellData -> cellData.getValue().codePeopleProperty());
        TableColumn<TblPeople, String> namePeople = new TableColumn("Nama");
        namePeople.setMinWidth(160);
        namePeople.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        tblPeople.getColumns().addAll(idPeople, namePeople);

        ObservableList<TblPeople> peopleItems = FXCollections.observableArrayList(loadAllDataPeople());
        cbpPeople = new JFXCComboBoxTablePopup<>(
                TblPeople.class, tblPeople, peopleItems, "", "Nama Pelapor *", true, 300, 300
        );
    }

    private List<TblPeople> loadAllDataPeople() {
        List<TblPeople> listPeople = new ArrayList();
        listPeople = parentController.getFLostFoundInformation().getAllDataPeople();
        List<TblEmployee> listEmployee = new ArrayList();
        listEmployee = parentController.getFLostFoundInformation().getAllDataEmployee((long) 0);

        for (int i = 0; i < listEmployee.size(); i++) {
            for (int j = 0; j < listPeople.size(); j++) {
                if (listEmployee.get(i).getTblPeople().getIdpeople() == listPeople.get(j).getIdpeople()) {
                    listPeople.remove(listPeople.get(j));
                }
            }
        }
        return listPeople;
    }

    private void initDataPopupCountry() {

        TableView<RefCountry> tblCountry = new TableView();
        TableColumn<RefCountry, String> nameCountry = new TableColumn("Negara");
        nameCountry.setMinWidth(160);
        nameCountry.setCellValueFactory(cellData -> cellData.getValue().countryNameProperty());
        tblCountry.getColumns().addAll(nameCountry);

        ObservableList<RefCountry> countryItems = FXCollections.observableArrayList(parentController.getFLostFoundInformation().getAllDataCountry());
        cbpCountry = new JFXCComboBoxTablePopup<>(
                RefCountry.class, tblCountry, countryItems, "", "Negara *", true, 300, 300
        );

    }

    public void refreshDataPopup() {
        ObservableList<TblPeople> peopleItems = FXCollections.observableArrayList(loadAllDataPeople());
        cbpPeople.setItems(peopleItems);

        ObservableList<RefCountry> countryItems = FXCollections.observableArrayList(parentController.getFLostFoundInformation().getAllDataCountry());
        cbpCountry.setItems(countryItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        /*cbpCustomer.valueProperty().addListener((obs,oldVal,newVal)->{
         if(newVal!=null){
             
         //setSelectedDataToInputForm(selectedData);
         }*/
        cbpPeople.valueProperty().bindBidirectional(selectedData.tblPeopleProperty());
        cbpPeople.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtPeopleAddress.setText(newVal.getAddress());
                txtPeopleCity.setText(newVal.getTown());
                txtPeopleZip.setText(newVal.getPoscode());
                txtPeopleRegion.setText(newVal.getRegion());
                cbpCountry.setValue(newVal.getRefCountry());
                txtPeoplePhoneNumber.setText(newVal.getHpnumber());
                txtPeopleEmail.setText(newVal.getEmail());
            }

        });
        //txtPeopleName.textProperty().bindBidirectional(selectedData.getTblPeople().fullNameProperty());

        //    txtIdLost.textProperty().bindBidirectional(selectedData.codeLostProperty());
        txtPeopleAddress.textProperty().bindBidirectional(selectedData.getTblPeople().addressProperty());
        txtPeopleCity.textProperty().bindBidirectional(selectedData.getTblPeople().townProperty());
        txtPeopleZip.textProperty().bindBidirectional(selectedData.getTblPeople().poscodeProperty());
        txtPeopleRegion.textProperty().bindBidirectional(selectedData.getTblPeople().regionProperty());
        cbpCountry.valueProperty().bindBidirectional(selectedData.getTblPeople().refCountryProperty());
        txtPeoplePhoneNumber.textProperty().bindBidirectional(selectedData.getTblPeople().hpnumberProperty());
        txtPeopleEmail.textProperty().bindBidirectional(selectedData.getTblPeople().emailProperty());
        txtItemName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        if (selectedData.getLostDate() != null) {
            dpDateLost.setValue(((java.sql.Date) selectedData.getLostDate()).toLocalDate());
        } else {
            dpDateLost.setValue(null);
        }

        dpDateLost.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setLostDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        txtLostLocation.textProperty().bindBidirectional(selectedData.lostLocationProperty());
        txtNote.textProperty().bindBidirectional(selectedData.lostNoteProperty());
        cbpCountry.hide();
        cbpPeople.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //    txtIdLost.setDisable(true);
        txtPeopleAddress.setDisable(true);
        txtPeopleCity.setDisable(true);
        txtPeopleZip.setDisable(true);
        txtPeopleRegion.setDisable(true);
        cbpCountry.setDisable(true);
        txtPeoplePhoneNumber.setDisable(true);
        txtPeopleEmail.setDisable(true);

        ClassViewSetting.setDisableForAllInputNode(gpFormDataLostInformation, dataInputStatus == 3,
                // txtIdLost, 
                txtPeopleAddress, txtPeopleCity, txtPeopleZip,
                txtPeopleRegion, cbpCountry, txtPeoplePhoneNumber, txtPeopleEmail);
        btnSave.setVisible(dataInputStatus != 3);
        // btnCancel.setVisible(dataInputStatus != 3);
    }

    public Stage dialogStageReturn;

    private void showReturnDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_lostfound_information/lost_information/LostInformationReturnDialogPopup.fxml"));
            LostInformationReturnDialogPopupController lostInformationReturnDialogPopupController = new LostInformationReturnDialogPopupController(this);
            loader.setController(lostInformationReturnDialogPopupController);

            Region page = loader.load();

            dialogStageReturn = new Stage();
            dialogStageReturn.initModality(Modality.WINDOW_MODAL);
            dialogStageReturn.initOwner(HotelFX.primaryStage);

            Undecorator undecorator = new Undecorator(dialogStageReturn, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageReturn.initStyle(StageStyle.TRANSPARENT);
            dialogStageReturn.setScene(scene);
            dialogStageReturn.setResizable(false);

            dialogStageReturn.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(LostInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Stage dialogStagePeople;

    private void showAddPeopleDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_lostfound_information/lost_information/LostInformationDialogPopup.fxml"));
            LostInformationDialogPopupController lostInformationDialogPopupController = new LostInformationDialogPopupController(this);
            loader.setController(lostInformationDialogPopupController);

            Region page = loader.load();

            dialogStagePeople = new Stage();
            dialogStagePeople.initModality(Modality.WINDOW_MODAL);
            dialogStagePeople.initOwner(HotelFX.primaryStage);

            Undecorator undecorator = new Undecorator(dialogStagePeople, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStagePeople.initStyle(StageStyle.TRANSPARENT);
            dialogStagePeople.setScene(scene);
            dialogStagePeople.setResizable(false);

            dialogStagePeople.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(EmployeeScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dataLostInformationCreateHandle(TblLostInformation tblLostInformation) {
        refreshDataPopup();
        dataInputStatus = 0;
        selectedData = new TblLostInformation();
        selectedData.setTblPeople(new TblPeople());
        /*cbpPeople.valueProperty().set(null);
         cbpPeople.valueProperty().addListener((obs,oldVal,newVal)->{
         if(newVal!=null && dataInputStatus==0){
         selectedData.setTblPeople(newVal);
         setSelectedDataToInputForm();
         //selectedData.setRefLostFoundStatus(new RefLostFoundStatus(0));
         }
         });*/
        lblLostInformation.setText("");
        setSelectedDataToInputForm();

        //selectedData = new TblLostInformation();
        // selectedData.setTblPeople(new TblPeople());
        //setSelectedDataToInputForm(selectedData);
        dataLostInformationFormShowStatus.set(0);
        dataLostInformationFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataLostInformationUpdateHandle() {
        if (tableDataLostInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 0) {
                dataLostInformationUpdateHandleDetail();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH!!", "Pilih data dengan status 'Hilang' !!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataLostInformationUpdateHandleDetail() {
        dataInputStatus = 1;
        //btnAdd.setVisible(false);
        selectedData = parentController.getFLostFoundInformation().getDataLostInformation(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getIdlost());
        selectedData.setTblPeople(parentController.getFLostFoundInformation().getDataPeople(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getTblPeople().getIdpeople()));
        lblLostInformation.setText(selectedData.getCodeLost() + " - " + selectedData.getTblPeople().getFullName());
        setSelectedDataToInputForm();
        /* cbpPeople.valueProperty().set(selectedData.getTblPeople());
         setSelectedDataToInputForm();
         cbpPeople.valueProperty().addListener((obs,oldVal,newVal)->{
         if(newVal!=null && dataInputStatus == 1){
         selectedData = parentController.getFLostFoundInformation().getDataLostInformation(((TblLostInformation)tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getIdlost());
         selectedData.setTblPeople(newVal);
         setSelectedDataToInputForm();
         }
         });*/
        dataLostInformationFormShowStatus.set(0);
        dataLostInformationFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataLostInformationShowHandle() {
        if (tableDataLostInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLostFoundInformation().getDataLostInformation(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getIdlost());
            selectedData.setTblPeople(parentController.getFLostFoundInformation().getDataPeople(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getTblPeople().getIdpeople()));
            cbpPeople.setValue(selectedData.getTblPeople());
            lblLostInformation.setText(selectedData.getCodeLost() + " - " + selectedData.getTblPeople().getFullName());
            setSelectedDataToInputForm();
            dataLostInformationFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataLostInformationUnshowHandle() {
        refreshDataTableLostInformation();
        dataLostInformationFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataLostInformationDeleteHandle() {
        if (tableDataLostInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 0) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage(null, null);
                if (alert.getResult() == ButtonType.OK) {
                    selectedData = parentController.getFLostFoundInformation().getDataLostInformation(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getIdlost());
                    selectedData.setTblPeople(parentController.getFLostFoundInformation().getDataPeople(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getTblPeople().getIdpeople()));
                    TblLostInformation dummySelectedData = new TblLostInformation(selectedData);
                    dummySelectedData.setTblPeople(new TblPeople(dummySelectedData.getTblPeople()));
                    if (parentController.getFLostFoundInformation().deleteDataLostInformation(dummySelectedData)) {
                        ClassMessage.showSucceedDeletingDataMessage(null, null);
                        refreshDataTableLostInformation();
                        dataLostInformationFormShowStatus.set(0.0);
                    } else {
                        ClassMessage.showFailedDeletingDataMessage(parentController.getFLostFoundInformation().getErrorMessage(), null);
                    }
                } else {
                    refreshDataTableLostInformation();
                    dataLostInformationFormShowStatus.set(0.0);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH!!", "Pilih data dengan status 'Hilang' !!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataLostInformationSetDiscardHandle() {
        if (tableDataLostInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 0) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
                if (alert.getResult() == ButtonType.OK) {
                    selectedData = parentController.getFLostFoundInformation().getDataLostInformation(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getIdlost());
                    selectedData.setTblPeople(parentController.getFLostFoundInformation().getDataPeople(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getTblPeople().getIdpeople()));
                    TblLostInformation dummySelectedData = new TblLostInformation(selectedData);
                    dummySelectedData.setRefLostFoundStatus(new RefLostFoundStatus(3));
                    if (parentController.getFLostFoundInformation().updateDataLostInformation(dummySelectedData)) {
                        refreshDataTableLostInformation();
                        dataLostInformationFormShowStatus.set(0.0);
                    } else {
                        ClassMessage.showFailedUpdatingDataMessage(parentController.getFLostFoundInformation().getErrorMessage(), null);
                    }
                } else {
                    refreshDataTableLostInformation();
                    dataLostInformationFormShowStatus.set(0.0);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH!!", "Pilih data dengan status 'Hilang' !!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataLostInformationReturnHandle() {
        if (tableDataLostInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 0) {
                selectedData = parentController.getFLostFoundInformation().getDataLostInformation(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getIdlost());
                selectedData.setTblPeople(parentController.getFLostFoundInformation().getDataPeople(((TblLostInformation) tableDataLostInformation.getTableView().getSelectionModel().getSelectedItem()).getTblPeople().getIdpeople()));
                showReturnDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH!!", "Pilih data dengan status 'Hilang' !!", null);
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH!!", "Pilih data dengan status hilang!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataLostInformationSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
            if (alert.getResult() == ButtonType.OK) {
                TblLostInformation dummySelectedData = new TblLostInformation(selectedData);
                dummySelectedData.setTblPeople(new TblPeople(dummySelectedData.getTblPeople()));
                //System.out.println("msk db:"+dummySelectedData.getTblPeople().getFullName());
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLostFoundInformation().insertDataLostInformation(dummySelectedData)) {
                            ClassMessage.showSucceedInsertingDataMessage(null, null);
                            refreshDataTableLostInformation();
                            dataLostInformationFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFLostFoundInformation().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFLostFoundInformation().updateDataLostInformation(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage(null, null);
                            refreshDataTableLostInformation();
                            dataLostInformationFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFLostFoundInformation().getErrorMessage(), null);
                        }
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataLostInformationCancelHandle() {
        refreshDataTableLostInformation();
        dataLostInformationFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableLostInformation() {
        tableDataLostInformation.getTableView().setItems(loadAllDataLostInformation());
        cft.refreshFilterItems(tableDataLostInformation.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInput() {
        boolean dataInput = true;
        errDataInput = "";

        /* if(txtPeopleAddress.getText()==null || txtPeopleAddress.getText().equalsIgnoreCase("")){
         errDataInput += "Alamat Pelapor: "+ClassMessage.defaultErrorNullValueMessage+"\n";
         dataInput = false;
         }
       
         if(txtPeopleCity.getText()==null || txtPeopleCity.getText().equalsIgnoreCase("")){
         errDataInput += "Kota: "+ClassMessage.defaultErrorNullValueMessage+"\n";
         dataInput = false;
         }
        
         if(cbpCountry.getValue()==null){
         errDataInput += "Negara: "+ClassMessage.defaultErrorNullValueMessage+"\n";
         dataInput = false;
         }
        
         if(txtPeoplePhoneNumber.getText()==null || txtPeoplePhoneNumber.getText().equalsIgnoreCase("")){
         errDataInput += "Nomor Telepon: "+ClassMessage.defaultErrorNullValueMessage+"\n"; 
         dataInput = false;
         }*/
        if (txtItemName.getText() == null || txtItemName.getText().equalsIgnoreCase("")) {
            errDataInput += "Nama Barang: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }

        if (dpDateLost.getValue() == null) {
            errDataInput += "Tanggal Lapor: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }
        if (txtLostLocation.getText() == null || txtLostLocation.getText().equalsIgnoreCase("")) {
            errDataInput += "Tempat Kehilangan: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }

        if (cbpPeople.getValue() == null) {
            errDataInput += "Nama Pelapor: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }

        if (txtNote.getText() == null || txtNote.getText().equalsIgnoreCase("")) {
            errDataInput += "Keterangan: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            dataInput = false;
        }
        return dataInput;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataLostInformationSplitpane();
        initTableLostInformation();
        initFormDataLostInformation();
        spDataLostInformation.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataLostInformationFormShowStatus.set(0);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LostInformationController(FeatureLostFoundInformationController parentController) {
        this.parentController = parentController;
    }
    private final FeatureLostFoundInformationController parentController;

    public FLostFoundInformationManager getService() {
        return parentController.getFLostFoundInformation();
    }
}
