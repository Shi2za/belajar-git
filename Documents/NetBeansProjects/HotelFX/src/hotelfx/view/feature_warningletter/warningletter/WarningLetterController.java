/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warningletter.warningletter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintDataJabatan;
import hotelfx.persistence.model.SysCode;
import hotelfx.persistence.model.TblEmployeeWarningLetterType;
import hotelfx.persistence.model.TblJob;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_warningletter.FeatureWarningLetterController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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

/**
 *
 * @author Andreas
 */
public class WarningLetterController implements Initializable {

    @FXML
    private SplitPane spDataWarningLetter;
    private DoubleProperty dataWarningLetterFormShowStatus;

    @FXML
    private AnchorPane contentLayout;
    
    @FXML
    private AnchorPane borderPaneLayout;
    @FXML
    private AnchorPane tableDataWarningLetterLayoutDisableLayer;

    private void setDataWarningLetterSplitPane() {
        dataWarningLetterFormShowStatus = new SimpleDoubleProperty(1.0);
        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0).subtract(
                (formAnchorWarningLetter.prefWidthProperty().divide(spDataWarningLetter.widthProperty()))
                .multiply(dataWarningLetterFormShowStatus))
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            spDataWarningLetter.setDividerPositions(newVal.doubleValue());
        });

        SplitPane.Divider div = spDataWarningLetter.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            div.setPosition(divPosition.get());
        });

        dataWarningLetterFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    borderPaneLayout.setDisable(false);
                    tableDataWarningLetterLayoutDisableLayer.setDisable(true);
                    borderPaneLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    borderPaneLayout.setDisable(true);
                    tableDataWarningLetterLayoutDisableLayer.setDisable(false);
                    tableDataWarningLetterLayoutDisableLayer.toFront();
                }
            }
        });

        dataWarningLetterFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataWarningLetterLayout;
    @FXML
    private AnchorPane ancFiltering;
    private ClassTableWithControl tableDataWarningLetter;
    private ClassFilteringTable cft;
    
    private void initTableDataWarningLetter() {
        setTableDataWarningLetter();
        setTableControlDataWarningLetter();
        tableDataWarningLetterLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataWarningLetter, 15.0);
        AnchorPane.setLeftAnchor(tableDataWarningLetter, 15.0);
        AnchorPane.setBottomAnchor(tableDataWarningLetter, 15.0);
        AnchorPane.setRightAnchor(tableDataWarningLetter, 15.0);

        tableDataWarningLetterLayout.getChildren().add(tableDataWarningLetter);
    }

    private void setTableDataWarningLetter() {
        TableView<TblEmployeeWarningLetterType> tableView = new TableView();

//       TableColumn<TblJob,String>codeJob = new TableColumn("Code Job");
//       codeJob.setCellValueFactory(cellData -> cellData.getValue().codeJobProperty());
        TableColumn<TblEmployeeWarningLetterType, String> nameWarningLetter = new TableColumn("Tipe Surat Peringatan");
        nameWarningLetter.setMinWidth(180);
        nameWarningLetter.setCellValueFactory(cellData -> cellData.getValue().nameWarningLetterProperty());

        TableColumn<TblEmployeeWarningLetterType,String>noteWarningLetter = new TableColumn("Keterangan");
        noteWarningLetter.setMinWidth(200);
        noteWarningLetter.setCellValueFactory(cellData -> cellData.getValue().noteWarningLetterProperty());

//        TableColumn<TblJob, String> recruitmentStatus = new TableColumn("Recruitment Show Status");
//        recruitmentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblJob, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefJobRecruitmentShowStatus().getStatusName(), param.getValue().refRecordStatusProperty()));
       
        tableView.getColumns().addAll(nameWarningLetter,noteWarningLetter);
        tableView.setItems(loadAllDataWarningLetter());

        tableView.setRowFactory(tv -> {
            TableRow<TblEmployeeWarningLetterType> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataWarningLetterUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataWarningLetterUpdateHandle();
                            } else {
                                dataWarningLetterShowHandle();
                            }
                        }
                    }
                } else {
                    if (!row.isEmpty()) {
                        if (isShowStatus.get()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataWarningLetterUpdateHandle();
                            } else {
                                dataWarningLetterShowHandle();
                            }
                        }
                    }
                }
            });
            return row;
        });

        tableDataWarningLetter = new ClassTableWithControl(tableView);
        cft = new ClassFilteringTable(TblEmployeeWarningLetterType.class,tableDataWarningLetter.getTableView(),tableDataWarningLetter.getTableView().getItems());
        ancFiltering.getChildren().clear();
        AnchorPane.setTopAnchor(cft,15.0);
        AnchorPane.setRightAnchor(cft,15.0);
        AnchorPane.setBottomAnchor(cft,0.0);
        ancFiltering.getChildren().add(cft);
    }

    private void setTableControlDataWarningLetter() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataWarningLetterCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataWarningLetterUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
              dataWarningLetterDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
              //   dataJobPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataWarningLetter.addButtonControl(buttonControls);
    }

    private ObservableList<TblEmployeeWarningLetterType> loadAllDataWarningLetter() {
        //return null;
        return FXCollections.observableArrayList(parentController.getFWarningLetterManager().getAllDataWarningLetter());
    }

    @FXML
    private AnchorPane formAnchorWarningLetter;
    @FXML
    private GridPane gpFormDataWarningLetter;
    @FXML
    private ScrollPane spFormDataWarningLetter;

   
    @FXML
    private JFXTextField txtNameWarningLetter;
    @FXML
    private JFXTextArea txtNoteWarningLetter;

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    private TblEmployeeWarningLetterType selectedData;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
//    private final JFXCComboBoxPopup<RefJobRecruitmentShowStatus> cbpJobRecruitmentShowStatus = new JFXCComboBoxPopup<>(RefJobRecruitmentShowStatus.class);
    private int scrollCounter = 0;

    public void initFormDataWarningLetter() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataWarningLetter.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataWarningLetter.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Tipe Surat Peringatan)"));
        btnSave.setOnAction((e) -> {
          dataWarningLetterSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
           dataWarningLetterCancelHandle();
        });

//        initDataPopup();
        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtNameWarningLetter);
    }
    
    private void refreshDataFilter(){
       tableDataWarningLetter.getTableView().setItems(loadAllDataWarningLetter());
       cft.refreshFilterItems(tableDataWarningLetter.getTableView().getItems());
    }
    private void setSelectedDataToInputForm() {
//        refreshDataPopup();
        txtNameWarningLetter.textProperty().bindBidirectional(selectedData.nameWarningLetterProperty());
        txtNoteWarningLetter.textProperty().bindBidirectional(selectedData.noteWarningLetterProperty());

        setSelectedDataToInputFormFunctionalComponent();

    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataWarningLetter, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    private int dataInputStatus = 0;

    private void dataWarningLetterCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblEmployeeWarningLetterType();
        setSelectedDataToInputForm();
        dataWarningLetterFormShowStatus.set(0.0);
        dataWarningLetterFormShowStatus.set(1.0);
    }

    private void dataWarningLetterUpdateHandle() {
        if (tableDataWarningLetter.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
         dataInputStatus = 1;
         selectedData = parentController.getFWarningLetterManager().getDataWarningLetter(((TblEmployeeWarningLetterType)tableDataWarningLetter.getTableView().getSelectionModel().getSelectedItem()).getIdwarningLetter());
         setSelectedDataToInputForm();
         dataWarningLetterFormShowStatus.set(0.0);
         dataWarningLetterFormShowStatus.set(1.0);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataWarningLetterShowHandle() {
        if (tableDataWarningLetter.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFWarningLetterManager().getDataWarningLetter(((TblEmployeeWarningLetterType)tableDataWarningLetter.getTableView().getSelectionModel().getSelectedItem()).getIdwarningLetter());
            //selectedDataSysCode = parentController.getFJobManager().getDataCodeByDataName(selectedData.getJobName());
            setSelectedDataToInputForm();
            dataWarningLetterFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataWarningLetterUnshowHandle() {
        tableDataWarningLetter.getTableView().setItems(loadAllDataWarningLetter());
        dataWarningLetterFormShowStatus.set(0);
        isShowStatus.set(false);
    }
    
    /*private void dataJobPrintHandle(){
       List<ClassPrintDataJabatan>listPrintJabatan = new ArrayList();
       for(int i = 0; i<tableDataJob.getTableView().getItems().size();i++){
          TblJob dataJob = (TblJob)tableDataJob.getTableView().getItems().get(i);
          ClassPrintDataJabatan printDataJabatan = new ClassPrintDataJabatan();
          printDataJabatan.setNamaJabatan(dataJob.getJobName());
          printDataJabatan.setBobotServiceChargeJabatan(dataJob.getServiceChargeWeight());
          printDataJabatan.setKeteranganJabatan(dataJob.getJobNote()!=null ? dataJob.getJobNote() : "-");
          listPrintJabatan.add(printDataJabatan);
       }
       ClassPrinter.printDataJabatan(listPrintJabatan);
    }*/
    
   private void dataWarningLetterDeleteHandle() {
        if (tableDataWarningLetter.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFWarningLetterManager().deleteDataWarningLetterType(new TblEmployeeWarningLetterType((TblEmployeeWarningLetterType) tableDataWarningLetter.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    tableDataWarningLetter.getTableView().setItems(loadAllDataWarningLetter());
                    dataWarningLetterFormShowStatus.set(0.0);
                    refreshDataFilter();
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFWarningLetterManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

  private void dataWarningLetterSaveHandle() {
        if (checkDataInputWarningLetter()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblEmployeeWarningLetterType dummySelectedData = new TblEmployeeWarningLetterType(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFWarningLetterManager().insertDataWarningLetter(dummySelectedData)) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            tableDataWarningLetter.getTableView().setItems(loadAllDataWarningLetter());
                            dataWarningLetterFormShowStatus.set(0.0);
                            refreshDataFilter();;
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFWarningLetterManager().getErrorMessage(), null);
                        }
                        break;

                    case 1:
                       if (parentController.getFWarningLetterManager().updateDataWarningLetter(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            tableDataWarningLetter.getTableView().setItems(loadAllDataWarningLetter());
                            dataWarningLetterFormShowStatus.set(0.0);
                            refreshDataFilter();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFWarningLetterManager().getErrorMessage(), null);
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

    private void dataWarningLetterCancelHandle() {
        tableDataWarningLetter.getTableView().setItems(loadAllDataWarningLetter());
        dataWarningLetterFormShowStatus.set(0.0);
        refreshDataFilter();
    }

    private String errDataInput;

   private boolean checkDataInputWarningLetter() {
        boolean check = true;
        errDataInput = "";
        if (txtNameWarningLetter.getText() == null || txtNameWarningLetter.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Tipe Surat Peringatan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return check;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataWarningLetterSplitPane();

        initTableDataWarningLetter();

        initFormDataWarningLetter();

        spDataWarningLetter.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataWarningLetterFormShowStatus.set(0);
        });
    }

    public WarningLetterController(FeatureWarningLetterController parentController) {
        this.parentController = parentController;
    }

    private final FeatureWarningLetterController parentController;
}
