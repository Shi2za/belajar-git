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
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFounderType;
import hotelfx.persistence.model.RefLostFoundStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFoundInformation;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
import hotelfx.persistence.service.FLostFoundInformationManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_lostfound_information.FeatureLostFoundInformationController;
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
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class FoundInformationController implements Initializable {

    @FXML
    private SplitPane spDataFoundInformation;
    private DoubleProperty dataFoundInformationFormShowStatus;

    @FXML
    private AnchorPane contentLayout;
    @FXML
    private AnchorPane tableDataFoundInformationLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataFoundInformationSplitPane() {
        spDataFoundInformation.setDividerPositions(1);

        dataFoundInformationFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataFoundInformationFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataFoundInformation.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataFoundInformation.getDividers().get(0);
        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataFoundInformationFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0) {
                    tableDataFoundInformationLayout.setDisable(false);
                    tableDataFoundInformationLayoutDisableLayer.setDisable(true);
                    tableDataFoundInformationLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataFoundInformationLayout.setDisable(true);
                    tableDataFoundInformationLayoutDisableLayer.setDisable(false);
                    tableDataFoundInformationLayoutDisableLayer.toFront();
                }
            }
        });

        dataFoundInformationFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataFoundInformationLayout;

    private ClassFilteringTable<TblFoundInformation> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    public ClassTableWithControl tableDataFoundInformation;

    private void initTableDataFoundInformation() {
        setTableDataFoundInformation();

        setTableControlFoundInformation();

        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataFoundInformation, 15.0);
        AnchorPane.setBottomAnchor(tableDataFoundInformation, 15.0);
        AnchorPane.setLeftAnchor(tableDataFoundInformation, 15.0);
        AnchorPane.setRightAnchor(tableDataFoundInformation, 15.0);
        ancBodyLayout.getChildren().add(tableDataFoundInformation);
    }

    private void setTableDataFoundInformation() {
        TableView<TblFoundInformation> tblDataFoundInformation = new TableView();
        TableView<TblFoundInformation> tableView = new TableView();
        TableColumn<TblFoundInformation, String> codeFound = new TableColumn("ID");
        codeFound.setMinWidth(90);
        codeFound.setCellValueFactory(cellData -> cellData.getValue().codeFoundProperty());
        TableColumn<TblFoundInformation, String> dateFound = new TableColumn("Tanggal\nLapor");
        dateFound.setMinWidth(100);
        dateFound.setCellValueFactory((TableColumn.CellDataFeatures<TblFoundInformation, String> param)
                -> Bindings.createStringBinding(() -> new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getFoundDate()), param.getValue().foundDateProperty()));
        TableColumn<TblFoundInformation, String> itemName = new TableColumn("Nama Barang");
        itemName.setMinWidth(120);
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        TableColumn<TblFoundInformation, String> foundLocation = new TableColumn("Tempat Ditemukan");
        foundLocation.setMinWidth(160);
        foundLocation.setCellValueFactory(cellData -> cellData.getValue().foundLocationProperty());
        TableColumn<TblFoundInformation, String> founderName = new TableColumn("Ditemukan Oleh");
        founderName.setMinWidth(160);
        founderName.setCellValueFactory(cellData -> cellData.getValue().founderNameProperty());
        TableColumn<TblFoundInformation, String> foundNote = new TableColumn("Keterangan");
        foundNote.setMinWidth(200);
        foundNote.setCellValueFactory(cellData -> cellData.getValue().foundNoteProperty());
        TableColumn<TblFoundInformation, String> foundStatus = new TableColumn("Status");
        foundStatus.setMinWidth(200);
        foundStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblFoundInformation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefLostFoundStatus().getIdstatus() == 2 ? getStatusReturn(param.getValue().getIdfound()) : param.getValue().getRefLostFoundStatus().getStatusName(), param.getValue().refLostFoundStatusProperty()));
        tblDataFoundInformation.getColumns().addAll(codeFound, dateFound, itemName, foundLocation, founderName, foundNote, foundStatus);

        tblDataFoundInformation.setItems(loadAllDataFoundInformation());

        tblDataFoundInformation.setRowFactory(tv -> {
            TableRow<TblFoundInformation> row = new TableRow();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataFoundInformationUnshowHandle();
                    } else {
                        if (!row.isEmpty()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                if (((TblFoundInformation) row.getItem()).getRefLostFoundStatus().getIdstatus() == 1) {
                                    dataFoundInformationUpdateHandleDetail();
                                } else {
                                    dataFoundInformationShowHandle();
                                }
                            } else {
                                dataFoundInformationShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                if (((TblFoundInformation) row.getItem()).getRefLostFoundStatus().getIdstatus() == 1) {
//                                    dataFoundInformationUpdateHandleDetail();
//                                } else {
//                                    dataFoundInformationShowHandle();
//                                }
//                            } else {
//                                dataFoundInformationShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });
        tableDataFoundInformation = new ClassTableWithControl(tblDataFoundInformation);

        //set filter
        cft = new ClassFilteringTable<>(
                TblFoundInformation.class,
                tableDataFoundInformation.getTableView(),
                tableDataFoundInformation.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    public ObservableList<TblFoundInformation> loadAllDataFoundInformation() {
        return FXCollections.observableArrayList(parentController.getFLostFoundInformation().getAllDataFoundInformation());
    }

    public String getStatusReturn(long id) {
        String status = "";
        List<TblLostFoundInformationDetail> listLostFound = new ArrayList();
        listLostFound = parentController.getFLostFoundInformation().getAllDataReturn();
        for (TblLostFoundInformationDetail dataLostFound : listLostFound) {
            if (dataLostFound.getTblFoundInformation().getIdfound() == id) {
                status = "Dikembalikan oleh " + dataLostFound.getReturnName() + "\n Tanggal :"
                        + new SimpleDateFormat("dd MMM yyyy").format(dataLostFound.getReturnDate());
            }
        }
        return status;
    }

    private void setTableControlFoundInformation() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;

        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataFoundInformationCreateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataFoundInformationUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataFoundInformationDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Dikembalikan");
            buttonControl.setOnMouseClicked((e) -> {
                dataFoundInformationReturnHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tidak Terpakai");
            buttonControl.setOnMouseClicked((e) -> {
                dataFoundInformationSetDiscardHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataFoundInformation.addButtonControl(buttonControls);
    }

    @FXML
    private AnchorPane ancForm;
    @FXML
    private ScrollPane spFormDataFoundInformation;
    @FXML
    private GridPane gpFormDataFoundInformation;
    @FXML
    private JFXTextField txtIdFound;
    @FXML
    private JFXDatePicker dpDateFound;
    //@FXML
    private JFXTextField txtFounderName = new JFXTextField();
    @FXML
    private JFXTextField txtItemName;
    @FXML
    private JFXTextArea txtFoundLocation;
    @FXML
    private JFXTextArea txtFoundNote;
    @FXML
    private AnchorPane cbpNameEmployeeLayout;
    @FXML
    private AnchorPane cbpFounderTypeLayout;
    @FXML
    private AnchorPane founderNameLayout;
    @FXML
    private AnchorPane noSelectedLayout;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Label lblFoundInformation;

    private JFXCComboBoxTablePopup<RefFounderType> cbpFounderType;
    private JFXCComboBoxTablePopup<TblEmployee> cbpNameEmployee;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;

    public TblFoundInformation selectedData;
    int dataInputStatus;

    private void initFormDataFoundInformation() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataFoundInformation.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataFoundInformation.setOnScroll((ScrollEvent scroll) -> {
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

        initDataPopupFounderTypeLayout();
        initDataPopupNameEmployee();

        btnSave.setOnAction((e) -> {
            dataFoundInformationSaveHandle();
        });

        btnCancel.setOnAction((e) -> {
            dataFoundInformationCancelHandle();
        });
        //cbpFounderType.setValue(null);
        initDateCalendar();
        cbpFounderType.valueProperty().addListener((obs, oldVal, newVal) -> {
            initDataPopupNameFounderLayout(newVal);
        });

        initImportantFieldColor();

    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern, dpDateFound);
        ClassFormatter.setDatePickersEnableDateUntil(LocalDate.now(),dpDateFound);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dpDateFound,
                cbpFounderType,
                cbpNameEmployee,
                txtFounderName,
                txtItemName,
                txtFoundLocation,
                txtFoundNote);
    }

    private void initDataPopupFounderTypeLayout() {
        initDataPopupFounderType();

        cbpFounderTypeLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cbpFounderType, 0.0);
        AnchorPane.setBottomAnchor(cbpFounderType, 0.0);
        AnchorPane.setLeftAnchor(cbpFounderType, 0.0);
        AnchorPane.setRightAnchor(cbpFounderType, 0.0);
        cbpFounderTypeLayout.getChildren().add(cbpFounderType);
    }

    private void initDataPopupNameFounderLayout(RefFounderType founderType) {
        if (founderType != null) {
            if (founderType.getIdtype() == 0) { // 0: karyawan
                txtFounderName.setText(null);
//                initDataPopupNameEmployee();
                ObservableList<TblEmployee> nameEmployeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
                cbpNameEmployee.setItems(nameEmployeeItems);
                cbpNameEmployeeLayout.getChildren().clear();
                AnchorPane.setTopAnchor(cbpNameEmployee, 0.0);
                AnchorPane.setBottomAnchor(cbpNameEmployee, 0.0);
                AnchorPane.setLeftAnchor(cbpNameEmployee, 0.0);
                AnchorPane.setRightAnchor(cbpNameEmployee, 0.0);
                cbpNameEmployeeLayout.getChildren().add(cbpNameEmployee);
                //txtFounderName.setVisible(false);
                founderNameLayout.setVisible(false);
                noSelectedLayout.setVisible(false);
                cbpNameEmployeeLayout.setVisible(true);
                cbpNameEmployeeLayout.toFront();
            }

            if (founderType.getIdtype() == 1) {
                cbpNameEmployee.valueProperty().set(null);

                txtFounderName.promptTextProperty().set("Nama *");
                txtFounderName.setLabelFloat(true);
                //txtFounderName.setLabelFloat(true);
                txtFounderName.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
                txtFounderName.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
                txtFounderName.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
                founderNameLayout.getChildren().clear();
                AnchorPane.setTopAnchor(txtFounderName, 0.0);
                AnchorPane.setBottomAnchor(txtFounderName, 0.0);
                AnchorPane.setLeftAnchor(txtFounderName, 0.0);
                AnchorPane.setRightAnchor(txtFounderName, 0.0);
                founderNameLayout.getChildren().add(txtFounderName);
                cbpNameEmployeeLayout.setVisible(false);
                noSelectedLayout.setVisible(false);
                founderNameLayout.setVisible(true);
                founderNameLayout.toFront();
            }
        } else {
            founderNameLayout.setVisible(false);
            //txtFounderName.setVisible(false);
            cbpNameEmployeeLayout.setVisible(false);
            noSelectedLayout.setVisible(true);
            noSelectedLayout.toFront();
        }
    }

    private void initDataPopupFounderType() {
        TableView<RefFounderType> tblFounderType = new TableView();
        TableColumn<RefFounderType, String> nameFounderType = new TableColumn("Tipe");
        nameFounderType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        tblFounderType.getColumns().addAll(nameFounderType);

        ObservableList<RefFounderType> founderTypeItems = FXCollections.observableArrayList(loadAllDataFounderType());

        cbpFounderType = new JFXCComboBoxTablePopup<>(
                RefFounderType.class, tblFounderType, founderTypeItems, "", "Tipe *", true, 300, 200
        );
    }

    private List<RefFounderType> loadAllDataFounderType() {
        return parentController.getFLostFoundInformation().getAllDataFounderType();
    }

    private void initDataPopupNameEmployee() {
        TableView<TblEmployee> tblEmployee = new TableView();
        TableColumn<TblEmployee, String> codeEmployee = new TableColumn("ID");
        codeEmployee.setMinWidth(90);
        codeEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        TableColumn<TblEmployee, String> nameEmployee = new TableColumn("Nama");
        nameEmployee.setMinWidth(160);
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().getTblPeople().fullNameProperty()));
        TableColumn<TblEmployee, String> typeEmployee = new TableColumn("Tipe");
        typeEmployee.setMinWidth(160);
        typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefEmployeeType().getTypeName(), param.getValue().refEmployeeTypeProperty()));
        tblEmployee.getColumns().addAll(codeEmployee, nameEmployee, typeEmployee);

        ObservableList<TblEmployee> nameEmployeeItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpNameEmployee = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tblEmployee, nameEmployeeItems, "", "Nama Karyawan *", true, 500, 300
        );

    }

    public List<TblEmployee> loadAllDataEmployee() {
        return parentController.getFLostFoundInformation().getAllDataEmployee((long) 1);
    }

    private void refreshDataPopup() {
        cbpFounderType.setItems(FXCollections.observableArrayList(loadAllDataFounderType()));
        cbpNameEmployee.setItems(FXCollections.observableArrayList(loadAllDataEmployee()));

    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        //    txtIdFound.textProperty().bindBidirectional(selectedData.codeFoundProperty());
        if (selectedData.getFoundDate() != null) {
            dpDateFound.setValue(((java.sql.Date) selectedData.getFoundDate()).toLocalDate());
        } else {
            dpDateFound.setValue(null);
        }

        dpDateFound.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setFoundDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        cbpFounderType.valueProperty().bindBidirectional(selectedData.refFounderTypeProperty());

        cbpFounderType.hide();
        cbpNameEmployee.hide();

        if (selectedData.getTblEmployeeByIdemployeeFounder() != null) {
            cbpNameEmployee.valueProperty().set(selectedData.getTblEmployeeByIdemployeeFounder());
        } else {
            cbpNameEmployee.setValue(null);
        }
        //cbpNameEmployee.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeFounderProperty());
        if (selectedData.getFounderName() != null && selectedData.getTblEmployeeByIdemployeeFounder() == null) {
            txtFounderName.setText(selectedData.getFounderName());
        } else {
            txtFounderName.setText(null);
        }

        txtItemName.textProperty().bindBidirectional(selectedData.itemNameProperty());
        txtFoundLocation.textProperty().bindBidirectional(selectedData.foundLocationProperty());
        txtFoundNote.textProperty().bindBidirectional(selectedData.foundNoteProperty());
        setSelectedDataToInputFormFunctionalComponent();

    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //    txtIdFound.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataFoundInformation, dataInputStatus == 3);
        btnSave.setVisible(dataInputStatus != 3);
        //    btnCancel.setVisible(dataInputStatus != 3);
    }

    private void dataFoundInformationCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblFoundInformation();
        selectedData.setRefLostFoundStatus(new RefLostFoundStatus(1));
        lblFoundInformation.setText("");
        setSelectedDataToInputForm();

        dataFoundInformationFormShowStatus.set(0);
        dataFoundInformationFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataFoundInformationUpdateHandle() {
        if (tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 1) {
                dataFoundInformationUpdateHandleDetail();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH", "Pilih data dengan status 'Ditemukan' !!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataFoundInformationUpdateHandleDetail() {
        dataInputStatus = 1;
        selectedData = parentController.getFLostFoundInformation().getDataFoundInformation(((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getIdfound());
        lblFoundInformation.setText(selectedData.getCodeFound() + " - " + selectedData.getFounderName());
        setSelectedDataToInputForm();

        dataFoundInformationFormShowStatus.set(0);
        dataFoundInformationFormShowStatus.set(1);

        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataFoundInformationShowHandle() {
        if (tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFLostFoundInformation().getDataFoundInformation(((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getIdfound());
            lblFoundInformation.setText(selectedData.getCodeFound() + " - " + selectedData.getFounderName());
            setSelectedDataToInputForm();
            dataFoundInformationFormShowStatus.set(1);
            isShowStatus.set(true);
        }
    }

    private void dataFoundInformationUnshowHandle() {
        refreshDataTableFoundInformation();
        dataFoundInformationFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataFoundInformationDeleteHandle() {
        if (tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 1) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage(null, null);

                if (alert.getResult() == ButtonType.OK) {
                    selectedData = parentController.getFLostFoundInformation().getDataFoundInformation(((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getIdfound());
                    TblFoundInformation dummySelectedData = new TblFoundInformation(selectedData);
                    if (parentController.getFLostFoundInformation().deleteDataFoundInformation(dummySelectedData)) {
                        ClassMessage.showSucceedDeletingDataMessage(null, null);
                        refreshDataTableFoundInformation();
                        dataFoundInformationFormShowStatus.set(0);
                    } else {
                        ClassMessage.showFailedDeletingDataMessage(parentController.getFLostFoundInformation().getErrorMessage(), null);
                    }
                } else {
                    refreshDataTableFoundInformation();
                    dataFoundInformationFormShowStatus.set(0);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH", "Pilih data dengan status 'Ditemukan' !!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataFoundInformationSetDiscardHandle() {
        if (tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 1) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
                if (alert.getResult() == ButtonType.OK) {
                    selectedData = parentController.getFLostFoundInformation().getDataFoundInformation(((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getIdfound());
                    // selectedData.setTblPeople(parentController.getFLostFoundInformation().getDataPeople(((TblFoundInformation)tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getTblPeople().getIdpeople()));
                    TblFoundInformation dummySelectedData = new TblFoundInformation(selectedData);
                    dummySelectedData.setRefLostFoundStatus(new RefLostFoundStatus(3));
                    if (parentController.getFLostFoundInformation().updateDataFoundInformation(dummySelectedData)) {
                        refreshDataTableFoundInformation();
                        dataFoundInformationFormShowStatus.set(0.0);
                    } else {
                        ClassMessage.showFailedUpdatingDataMessage(parentController.getFLostFoundInformation().getErrorMessage(), null);
                    }
                } else {
                    refreshDataTableFoundInformation();
                    dataFoundInformationFormShowStatus.set(0.0);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH!!", "Pilih data dengan status 'Ditemukan' !!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }

    private void dataFoundInformationReturnHandle() {
        if (tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getRefLostFoundStatus().getIdstatus() == 1) {
                selectedData = parentController.getFLostFoundInformation().getDataFoundInformation(((TblFoundInformation) tableDataFoundInformation.getTableView().getSelectionModel().getSelectedItem()).getIdfound());
                showReturnFoundDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA YANG DIPILIH SALAH", "Pilih data dengan status 'Ditemukan' !!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(null, null);
        }
    }
    public Stage dialogStage;

    private void showReturnFoundDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_lostfound_information/found_information/FoundInformationReturnDialogPopup.fxml"));
            FoundInformationReturnDialogPopupController foundInformationReturnDialogPopupController = new FoundInformationReturnDialogPopupController(this);
            loader.setController(foundInformationReturnDialogPopupController);

            Region page = loader.load();

            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FoundInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void dataFoundInformationSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
            if (alert.getResult() == ButtonType.OK) {
                TblFoundInformation dummySelectedData = new TblFoundInformation(selectedData);
                if (selectedData.getRefFounderType().getIdtype() == 0) {
                    selectedData.setTblEmployeeByIdemployeeFounder(cbpNameEmployee.getValue());
                    dummySelectedData.setTblEmployeeByIdemployeeFounder(selectedData.getTblEmployeeByIdemployeeFounder());
                    dummySelectedData.setFounderName(selectedData.getTblEmployeeByIdemployeeFounder().getTblPeople().getFullName());
                } else {
                    selectedData.setTblEmployeeByIdemployeeFounder(null);
                    selectedData.setFounderName(txtFounderName.getText());
                    dummySelectedData.setTblEmployeeByIdemployeeFounder(selectedData.getTblEmployeeByIdemployeeFounder());
                    dummySelectedData.setFounderName(selectedData.getFounderName());
                }

                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFLostFoundInformation().insertDataFounderInformation(dummySelectedData)) {
                            ClassMessage.showSucceedInsertingDataMessage(null, null);
                            refreshDataTableFoundInformation();
                            dataFoundInformationFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        }
                        break;

                    case 1:
                        if (parentController.getFLostFoundInformation().updateDataFoundInformation(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage(null, null);
                            refreshDataTableFoundInformation();
                            dataFoundInformationFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(null, null);
                        }
                        break;
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage(errDataInput, null);
        }
    }

    private void dataFoundInformationCancelHandle() {
        refreshDataTableFoundInformation();
        dataFoundInformationFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableFoundInformation() {
        tableDataFoundInformation.getTableView().setItems(loadAllDataFoundInformation());
        cft.refreshFilterItems(tableDataFoundInformation.getTableView().getItems());
    }

    String errDataInput;

    private boolean checkDataInput() {
        boolean check = true;
        errDataInput = "";
        if (dpDateFound.getValue() == null) {
            errDataInput += "Tanggal Lapor: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (cbpFounderType.getValue() == null) {
            errDataInput += "Tipe: " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (cbpFounderType.getValue() != null) {
            if (cbpFounderType.getValue().getIdtype() == 0) {
                if (cbpNameEmployee.getValue() == null) {
                    errDataInput += "Nama Karyawan :" + ClassMessage.defaultErrorNullValueMessage + "\n";
                    check = false;
                } else if (cbpFounderType.getValue().getIdtype() == 1) {
                    if (txtFounderName.getText() == null || txtFounderName.getText().equalsIgnoreCase("")) {
                        errDataInput += "Nama :" + ClassMessage.defaultErrorNullValueMessage + "\n";
                        check = false;
                    }
                }
            }
        }

        if (txtItemName.getText() == null || txtItemName.getText().equalsIgnoreCase("")) {
            errDataInput += "Nama Barang : " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (txtFoundLocation.getText() == null || txtFoundLocation.getText().equalsIgnoreCase("")) {
            errDataInput += "Lokasi Ditemukan : " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        if (txtFoundNote.getText() == null || txtFoundNote.getText().equalsIgnoreCase("")) {
            errDataInput += "Keterangan : " + ClassMessage.defaultErrorNullValueMessage + "\n";
            check = false;
        }

        return check;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataFoundInformationSplitPane();
        initTableDataFoundInformation();
        initFormDataFoundInformation();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public FoundInformationController(FeatureLostFoundInformationController parentController) {
        this.parentController = parentController;
    }

    private final FeatureLostFoundInformationController parentController;

    public FLostFoundInformationManager getService() {
        return parentController.getFLostFoundInformation();
    }
}
