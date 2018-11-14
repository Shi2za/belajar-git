/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_job.job;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintDataJabatan;
import hotelfx.persistence.model.SysCode;
import hotelfx.persistence.model.TblJob;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_job.FeatureJobController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @author Andreas
 */
public class JobController implements Initializable {

    @FXML
    private SplitPane spDataJob;
    private DoubleProperty dataJobFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataJobLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataJobSplitPane() {
        spDataJob.setDividerPositions(1);

        dataJobFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataJobFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataJob.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataJob.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataJobFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    tableDataJobLayout.setDisable(false);
                    tableDataJobLayoutDisableLayer.setDisable(true);
                    tableDataJobLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataJobLayout.setDisable(true);
                    tableDataJobLayoutDisableLayer.setDisable(false);
                    tableDataJobLayoutDisableLayer.toFront();
                }
            }
        });

        dataJobFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataJobLayout;

    private ClassFilteringTable<TblJob> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataJob;

    private void initTableDataJob() {
        setTableDataJob();
        setTableControlDataJob();
        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataJob, 15.0);
        AnchorPane.setLeftAnchor(tableDataJob, 15.0);
        AnchorPane.setBottomAnchor(tableDataJob, 15.0);
        AnchorPane.setRightAnchor(tableDataJob, 15.0);

        ancBodyLayout.getChildren().add(tableDataJob);
    }

    private void setTableDataJob() {
        TableView<TblJob> tableView = new TableView();

//       TableColumn<TblJob,String>codeJob = new TableColumn("Code Job");
//       codeJob.setCellValueFactory(cellData -> cellData.getValue().codeJobProperty());
        TableColumn<TblJob, String> nameJob = new TableColumn("Jabatan");
        nameJob.setCellValueFactory(cellData -> cellData.getValue().jobNameProperty());
        nameJob.setMinWidth(140);

        TableColumn<TblJob, Integer> serviceCharge = new TableColumn("Bobot (Service Charge)");
        serviceCharge.setCellValueFactory(cellData -> cellData.getValue().serviceChargeWeightProperty().asObject());
        serviceCharge.setMinWidth(180);

//        TableColumn<TblJob, String> recruitmentStatus = new TableColumn("Recruitment Show Status");
//        recruitmentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblJob, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefJobRecruitmentShowStatus().getStatusName(), param.getValue().refRecordStatusProperty()));
        TableColumn<TblJob, String> jobNote = new TableColumn("Keterangan");
        jobNote.setCellValueFactory(cellData -> cellData.getValue().jobNoteProperty());
        jobNote.setMinWidth(200);

        tableView.getColumns().addAll(nameJob, serviceCharge, jobNote);
        tableView.setItems(loadAllDataJob());

        tableView.setRowFactory(tv -> {
            TableRow<TblJob> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataJobUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataJobUpdateHandle();
                            } else {
                                dataJobShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                dataJobUpdateHandle();
//                            } else {
//                                dataJobShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataJob = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblJob.class,
                tableDataJob.getTableView(),
                tableDataJob.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataJob() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataJobCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataJobUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataJobDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                dataJobPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataJob.addButtonControl(buttonControls);
    }

    private ObservableList<TblJob> loadAllDataJob() {
        //return null;
        return FXCollections.observableArrayList(parentController.getFJobManager().getAllDataJob());
    }

    @FXML
    private AnchorPane formAnchorJob;
    @FXML
    private GridPane gpFormDataJob;
    @FXML
    private ScrollPane spFormDataJob;

    @FXML
    private JFXTextField txtCodePrefixJob;
    @FXML
    private JFXTextField txtNameJob;
    @FXML
    private JFXTextField txtServiceChargeWeight;
    @FXML
    private JFXTextArea txtNoteJob;

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    private TblJob selectedData;
    private SysCode selectedDataSysCode;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
//    private final JFXCComboBoxPopup<RefJobRecruitmentShowStatus> cbpJobRecruitmentShowStatus = new JFXCComboBoxPopup<>(RefJobRecruitmentShowStatus.class);
    private int scrollCounter = 0;

    public void initFormDataJob() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataJob.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataJob.setOnScroll((ScrollEvent scroll) -> {
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
                    System.out.println("err :" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setTooltip(new Tooltip("Simpan (Data Jabatan)"));
        btnSave.setOnAction((e) -> {
            dataJobSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataJobCancelHandle();
        });

//        initDataPopup();
        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtNameJob,
                txtCodePrefixJob,
                txtServiceChargeWeight);
    }

//    private void initDataPopup() {
//        TableView<RefJobRecruitmentShowStatus> tableJobRecruitmentShowStatus = new TableView();
//        TableColumn<RefJobRecruitmentShowStatus, String> jobRecruitmentShowStatusName = new TableColumn<>("Recruitment Show Status");
//        jobRecruitmentShowStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
//        tableJobRecruitmentShowStatus.getColumns().addAll(jobRecruitmentShowStatusName);
//
//        ObservableList<RefJobRecruitmentShowStatus> jobRecruitmentShowStatusItem = FXCollections.observableArrayList(parentController.getFJobManager().getAllJobRecruitmentShowStatus());
//        setFunctionPopup(cbpJobRecruitmentShowStatus, tableJobRecruitmentShowStatus, jobRecruitmentShowStatusItem, "statusName", "Recruitment Show Status *");
//
//        gpFormDataJob.add(cbpJobRecruitmentShowStatus, 2, 3);
//
//    }
//
//    public void setFunctionPopup(JFXCComboBoxPopup cbp, TableView table, ObservableList items, String nameFiltered, String promptText) {
//        table.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal.intValue() != -1) {
//                cbp.valueProperty().set(table.getItems().get(newVal.intValue()));
//            }
//            cbp.hide();
//        });
//
//        cbp.setPropertyNameForFiltered(nameFiltered);
//        cbp.setItems(items);
//    
//    cbp.setOnShowing((e) -> {
//            table.getSelectionModel().clearSelection();
//        });
//
//        table.itemsProperty().bind(cbp.filteredItemsProperty());
//
//        JFXButton button = new JFXButton("▾");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(200, 200);
//
//        content.setCenter(table);
//
//        cbp.setPopupEditor(true);
//        cbp.promptTextProperty().set(promptText);
//        cbp.setLabelFloat(true);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//
//        cbp.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
//        cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
//        cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
//
//    }
//
//    private void refreshDataPopup() {
//        ObservableList<RefJobRecruitmentShowStatus> jobRecruitmentShowStatusItem = FXCollections.observableArrayList(parentController.getFJobManager().getAllJobRecruitmentShowStatus());
//        cbpJobRecruitmentShowStatus.setItems(jobRecruitmentShowStatusItem);
//    }
    private void setSelectedDataToInputForm() {
//        refreshDataPopup();

        txtCodePrefixJob.textProperty().bindBidirectional(selectedDataSysCode.codePrefixProperty());
        txtNameJob.textProperty().bindBidirectional(selectedData.jobNameProperty());
        Bindings.bindBidirectional(txtServiceChargeWeight.textProperty(), selectedData.serviceChargeWeightProperty(), new NumberStringConverter());

//        cbpJobRecruitmentShowStatus.valueProperty().bindBidirectional(selectedData.refJobRecruitmentShowStatusProperty());
//        cbpJobRecruitmentShowStatus.hide();
        txtNoteJob.textProperty().bindBidirectional(selectedData.jobNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();

    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataJob, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    private int dataInputStatus = 0;

    private void dataJobCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblJob();
        selectedDataSysCode = new SysCode();
        selectedDataSysCode.setCodeLastNumber((long) 1);
        setSelectedDataToInputForm();
        dataJobFormShowStatus.set(0.0);
        dataJobFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataJobUpdateHandle() {
        if (tableDataJob.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFJobManager().getJob(((TblJob) tableDataJob.getTableView().getSelectionModel().getSelectedItem()).getIdjob());
            selectedDataSysCode = parentController.getFJobManager().getDataCodeByDataName(selectedData.getJobName());
            setSelectedDataToInputForm();
            dataJobFormShowStatus.set(0.0);
            dataJobFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataJobShowHandle() {
        if (tableDataJob.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFJobManager().getJob(((TblJob) tableDataJob.getTableView().getSelectionModel().getSelectedItem()).getIdjob());
            selectedDataSysCode = parentController.getFJobManager().getDataCodeByDataName(selectedData.getJobName());
            setSelectedDataToInputForm();
            dataJobFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataJobUnshowHandle() {
        refreshDataTableJob();
        dataJobFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataJobPrintHandle() {
        List<ClassPrintDataJabatan> listPrintJabatan = new ArrayList();
        for (int i = 0; i < tableDataJob.getTableView().getItems().size(); i++) {
            TblJob dataJob = (TblJob) tableDataJob.getTableView().getItems().get(i);
            ClassPrintDataJabatan printDataJabatan = new ClassPrintDataJabatan();
            printDataJabatan.setNamaJabatan(dataJob.getJobName());
            printDataJabatan.setBobotServiceChargeJabatan(dataJob.getServiceChargeWeight());
            printDataJabatan.setKeteranganJabatan(dataJob.getJobNote() != null ? dataJob.getJobNote() : "-");
            listPrintJabatan.add(printDataJabatan);
        }
        ClassPrinter.printDataJabatan(listPrintJabatan);
    }

    private void dataJobDeleteHandle() {
        if (tableDataJob.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFJobManager().deleteDataJob(new TblJob((TblJob) tableDataJob.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    refreshDataTableJob();
                    dataJobFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFJobManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataJobSaveHandle() {
        if (checkDataInputJob()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                selectedDataSysCode.setDataName(selectedData.getJobName());
                //dummy entry
                TblJob dummySelectedData = new TblJob(selectedData);
                SysCode dummySelectedtDataSysCode = new SysCode(selectedDataSysCode);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFJobManager().insertDataJob(dummySelectedData, dummySelectedtDataSysCode) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            refreshDataTableJob();
                            dataJobFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFJobManager().getErrorMessage(), null);
                        }
                        break;

                    case 1:
                        if (parentController.getFJobManager().updateDataJob(dummySelectedData, dummySelectedtDataSysCode)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            refreshDataTableJob();
                            dataJobFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFJobManager().getErrorMessage(), null);
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

    private void dataJobCancelHandle() {
        refreshDataTableJob();
        dataJobFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableJob() {
        tableDataJob.getTableView().setItems(loadAllDataJob());
        cft.refreshFilterItems(tableDataJob.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputJob() {
        boolean check = true;
        errDataInput = "";
        if (txtNameJob.getText() == null || txtNameJob.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Jabatan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtCodePrefixJob.getText() == null || txtCodePrefixJob.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Prefix (Kode Jabatan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpJobRecruitmentShowStatus.getValue() == null) {
//            check = false;
//            errDataInput += "Show Status : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (txtServiceChargeWeight.getText() == null || txtServiceChargeWeight.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Bobot (Service Charge) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return check;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataJobSplitPane();

        initTableDataJob();

        initFormDataJob();

        spDataJob.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataJobFormShowStatus.set(0);
        });
    }

    public JobController(FeatureJobController parentController) {
        this.parentController = parentController;
    }

    private final FeatureJobController parentController;
}
