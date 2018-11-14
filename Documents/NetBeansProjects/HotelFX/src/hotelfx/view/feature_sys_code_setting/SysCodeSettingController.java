/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_sys_code_setting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintDataCodeSetting;
import hotelfx.persistence.model.SysCode;
import hotelfx.persistence.service.FSysCodeSettingManager;
import hotelfx.persistence.service.FSysCodeSettingManagerImpl;
import hotelfx.view.DashboardController;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
 * FXML Controller class
 *
 * @author ANDRI
 */
public class SysCodeSettingController implements Initializable {

    /**
     * TAB-PANE
     */
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();

    @FXML
    private TabPane tpSysCodeSetting;

    private void setDataCodeSettingTabpane() {
        unSavingDataInput.bind(ClassSession.unSavingDataInput);

        unSavingDataInput.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                for (Tab tab : tpSysCodeSetting.getTabs()) {
                    if(tpSysCodeSetting.getSelectionModel().getSelectedItem() != null
                            && tpSysCodeSetting.getSelectionModel().getSelectedItem().equals(tab)){
                        tab.setDisable(false);
                    }else{
                        tab.setDisable(true);
                    }
                }
            } else {
                for (Tab tab : tpSysCodeSetting.getTabs()) {
                    tab.setDisable(false);
                }
            }
        });
    }

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataCodeSetting;

    private DoubleProperty dataCodeSettingFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataCodeSettingLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataCodeSettingSplitpane() {
        spDataCodeSetting.setDividerPositions(1);

        dataCodeSettingFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataCodeSettingFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataCodeSetting.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataCodeSetting.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataCodeSettingFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataCodeSettingLayout.setDisable(false);
                    tableDataCodeSettingLayoutDisableLayer.setDisable(true);
                    tableDataCodeSettingLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataCodeSettingLayout.setDisable(true);
                    tableDataCodeSettingLayoutDisableLayer.setDisable(false);
                    tableDataCodeSettingLayoutDisableLayer.toFront();
                }
            }
        });

        dataCodeSettingFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA (SYSTEM)
     */
    @FXML
    private AnchorPane tableDataCodeSettingLayout;

    private ClassFilteringTable<SysCode> cftDS;

    @FXML
    private AnchorPane ancDSHeaderLayout;

    @FXML
    private AnchorPane ancDSBodyLayout;

    private ClassTableWithControl tableDataCodeSetting;

    @FXML
    private JFXButton btnPrint;

    private void initTableDataCodeSetting() {
        //set table
        setTableDataCodeSetting();
        //set control
        setTableControlDataCodeSetting();
        //set table-control to content-view
        ancDSBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataCodeSetting, 15.0);
        AnchorPane.setLeftAnchor(tableDataCodeSetting, 15.0);
        AnchorPane.setRightAnchor(tableDataCodeSetting, 15.0);
        AnchorPane.setTopAnchor(tableDataCodeSetting, 15.0);
        ancDSBodyLayout.getChildren().add(tableDataCodeSetting);
    }

    private void setTableDataCodeSetting() {
        TableView<SysCode> tableView = new TableView();

        TableColumn<SysCode, String> dataName = new TableColumn("Nama Data");
        dataName.setCellValueFactory(cellData -> cellData.getValue().dataNameProperty());
        dataName.setMinWidth(140);

        TableColumn<SysCode, String> codePrefix = new TableColumn("Prefix (Kode)");
        codePrefix.setCellValueFactory(cellData -> cellData.getValue().codePrefixProperty());
        codePrefix.setMinWidth(140);

        TableColumn<SysCode, String> codeLastNumber = new TableColumn("Nomro Terakhir (Kode)");
        codeLastNumber.setCellValueFactory((TableColumn.CellDataFeatures<SysCode, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeLastNumber().toString(), param.getValue().codeLastNumberProperty()));
        codeLastNumber.setMinWidth(180);

        tableView.getColumns().addAll(dataName, codePrefix, codeLastNumber);

        tableView.setItems(loadAllDataCodeSetting(3));

        tableView.setRowFactory(tv -> {
            TableRow<SysCode> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataCodeSettingUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataCodeSettingUpdateHandle();
                            } else {
                                dataCodeSettingShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataCodeSettingUpdateHandle();
//                            } else {
//                                dataCodeSettingShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataCodeSetting = new ClassTableWithControl(tableView);

        //set filter
        cftDS = new ClassFilteringTable<>(
                SysCode.class,
                tableDataCodeSetting.getTableView(),
                tableDataCodeSetting.getTableView().getItems());

        AnchorPane.setBottomAnchor(cftDS, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cftDS, 15.0);
        AnchorPane.setTopAnchor(cftDS, 12.5);
        ancDSHeaderLayout.getChildren().clear();
        ancDSHeaderLayout.getChildren().add(cftDS);
    }

    private void setTableControlDataCodeSetting() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataCodeSettingCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataCodeSettingUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    //listener delete
//                    dataCodeSettingDeleteHandle();
//                }
//            });
//            buttonControls.add(buttonControl);
//        }

        /*if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
         buttonControl = new JFXButton();
         buttonControl.setText("Cetak");
         buttonControl.setOnMouseClicked((e) -> {
         //listener print
         dataCodeSettingPrintHandle();
         });
         buttonControls.add(buttonControl);
         }*/
        tableDataCodeSetting.addButtonControl(buttonControls);

    }

    private ObservableList<SysCode> loadAllDataCodeSetting(int idRecordStatus) {
        return FXCollections.observableArrayList(fSysCodeManager.getAllDataCodeByIDRecordStatus(idRecordStatus));    //data system
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataCodeSetting;

    @FXML
    private ScrollPane spFormDataCodeSetting;

    @FXML
    private Label lblCodeData;
    
    @FXML
    private JFXTextField txtDataName;

    @FXML
    private JFXTextField txtCodePrefix;

    @FXML
    private JFXTextField txtCodeLastNumber;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private SysCode selectedData;

    private void initFormDataCodeSetting() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataCodeSetting.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataCodeSetting.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Kode)"));
        btnSave.setOnAction((e) -> {
            dataCodeSettingSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCodeSettingCancelHandle();
        });

        btnPrint.setOnAction((e) -> {
            dataCodeSettingPrintHandle();
        });
        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtCodePrefix);
    }

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodePrefix() != null 
                ? "" : "");
        
        txtDataName.textProperty().bindBidirectional(selectedData.dataNameProperty());
        txtCodePrefix.textProperty().bindBidirectional(selectedData.codePrefixProperty());
        Bindings.bindBidirectional(txtCodeLastNumber.textProperty(), selectedData.codeLastNumberProperty(), new NumberStringConverter());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeLastNumber.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataCodeSetting, dataInputStatus == 3, txtCodeLastNumber);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataCodeSettingCreateHandle() {
        dataInputStatus = 0;
        selectedData = new SysCode();
        setSelectedDataToInputForm();
        //open form data code setting
        dataCodeSettingFormShowStatus.set(0.0);
        dataCodeSettingFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataCodeSettingUpdateHandle() {
        if (tableDataCodeSetting.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = fSysCodeManager.getDataCode(((SysCode) tableDataCodeSetting.getTableView().getSelectionModel().getSelectedItem()).getIdcode());
            setSelectedDataToInputForm();
            //open form data code setting
            dataCodeSettingFormShowStatus.set(0.0);
            dataCodeSettingFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataCodeSettingShowHandle() {
        if (tableDataCodeSetting.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = fSysCodeManager.getDataCode(((SysCode) tableDataCodeSetting.getTableView().getSelectionModel().getSelectedItem()).getIdcode());
            setSelectedDataToInputForm();
            dataCodeSettingFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataCodeSettingUnshowHandle() {
        refreshDataTableCodeSetting();
        refreshDataTableCodeSettingJob();
        dataCodeSettingFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataCodeSettingPrintHandle() {
        List<ClassPrintDataCodeSetting> listPrintCodeSetting = new ArrayList();
        for (int i = 0; i < tableDataCodeSetting.getTableView().getItems().size(); i++) {
            SysCode sysCode = (SysCode) tableDataCodeSetting.getTableView().getItems().get(i);
            ClassPrintDataCodeSetting printDataCodeSetting = new ClassPrintDataCodeSetting();
            printDataCodeSetting.setTipeCodeSetting("Sistem");
            printDataCodeSetting.setNamaCodeSetting(sysCode.getDataName());
            printDataCodeSetting.setPrefixCodeSetting(sysCode.getCodePrefix());
            printDataCodeSetting.setNomorTerakhirCodeSetting(sysCode.getCodeLastNumber());
            listPrintCodeSetting.add(printDataCodeSetting);
        }

        for (int j = 0; j < tableDataCodeSettingJob.getItems().size(); j++) {
            SysCode sysCode = (SysCode) tableDataCodeSettingJob.getItems().get(j);
            ClassPrintDataCodeSetting printDataCodeSetting = new ClassPrintDataCodeSetting();
            printDataCodeSetting.setTipeCodeSetting("Jabatan");
            printDataCodeSetting.setNamaCodeSetting(sysCode.getDataName());
            printDataCodeSetting.setPrefixCodeSetting(sysCode.getCodePrefix());
            printDataCodeSetting.setNomorTerakhirCodeSetting(sysCode.getCodeLastNumber());
            listPrintCodeSetting.add(printDataCodeSetting);
        }
        ClassPrinter.printDataCodeSetting(listPrintCodeSetting);
    }

    private void dataCodeSettingDeleteHandle() {
        if (tableDataCodeSetting.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (fSysCodeManager.deleteDataCode((SysCode) tableDataCodeSetting.getTableView().getSelectionModel().getSelectedItem())) {
                ClassMessage.showSucceedDeletingDataMessage("", null);
                //refresh data from table & close form data code setting
                refreshDataTableCodeSetting();
                refreshDataTableCodeSettingJob();
                dataCodeSettingFormShowStatus.set(0.0);
            } else {
                ClassMessage.showFailedDeletingDataMessage(fSysCodeManager.getErrorMessage(), null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataCodeSettingSaveHandle() {
        if (checkDataInputDataCodeSetting()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                SysCode dummySelectedData = new SysCode(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (fSysCodeManager.insertDataCode(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data code setting
                            refreshDataTableCodeSetting();
                            refreshDataTableCodeSettingJob();
                            dataCodeSettingFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(fSysCodeManager.getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (fSysCodeManager.updateDataCode(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data code setting
                            refreshDataTableCodeSetting();
                            refreshDataTableCodeSettingJob();
                            dataCodeSettingFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(fSysCodeManager.getErrorMessage(), null);
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

    private String errDataInput;

    private void dataCodeSettingCancelHandle() {
        //refresh data from table & close form data code setting
        refreshDataTableCodeSetting();
        refreshDataTableCodeSettingJob();
        dataCodeSettingFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableCodeSetting() {
        tableDataCodeSetting.getTableView().setItems(loadAllDataCodeSetting(3));
        cftDS.refreshFilterItems(tableDataCodeSetting.getTableView().getItems());
    }

    public void refreshDataTableCodeSettingJob() {
        tableDataCodeSettingJob.setItems(loadAllDataCodeSetting(1));
        cftDJ.refreshFilterItems(tableDataCodeSettingJob.getItems());
    }

    private boolean checkDataInputDataCodeSetting() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtCodePrefix.getText() == null || txtCodePrefix.getText().equals("")) {
            dataInput = false;
            errDataInput += "Prefix (Kode) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (txtCodePrefix.getText().length() > 3) {
                dataInput = false;
                errDataInput += "Prefix (Kode) : Maksimal prefix (kode) hanya terdiri dari 3 huruf \n";
            }
        }
        return dataInput;
    }

    /**
     * TABLE DATA (JOB)
     */
    @FXML
    private AnchorPane tableDataCodeSettingJobLayout;

    private ClassFilteringTable<SysCode> cftDJ;

    @FXML
    private AnchorPane ancDJHeaderLayout;

    @FXML
    private AnchorPane ancDJBodyLayout;

    private TableView tableDataCodeSettingJob;

    private void initTableDataCodeSettingJob() {
        //set table
        setTableDataCodeSettingJob();
        //set table-control to content-view
        ancDJBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataCodeSettingJob, 15.0);
        AnchorPane.setLeftAnchor(tableDataCodeSettingJob, 15.0);
        AnchorPane.setRightAnchor(tableDataCodeSettingJob, 15.0);
        AnchorPane.setTopAnchor(tableDataCodeSettingJob, 15.0);
        ancDJBodyLayout.getChildren().add(tableDataCodeSettingJob);
    }

    private void setTableDataCodeSettingJob() {
        tableDataCodeSettingJob = new TableView();

        TableColumn<SysCode, String> dataName = new TableColumn("Jabatan");
        dataName.setCellValueFactory(cellData -> cellData.getValue().dataNameProperty());
        dataName.setMinWidth(140);

        TableColumn<SysCode, String> codePrefix = new TableColumn("Prefix (Kode)");
        codePrefix.setCellValueFactory(cellData -> cellData.getValue().codePrefixProperty());
        codePrefix.setMinWidth(140);

        TableColumn<SysCode, String> codeLastNumber = new TableColumn("Nomor Terkahir (Kode)");
        codeLastNumber.setCellValueFactory((TableColumn.CellDataFeatures<SysCode, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeLastNumber().toString(), param.getValue().codeLastNumberProperty()));
        codeLastNumber.setMinWidth(180);

        tableDataCodeSettingJob.getColumns().addAll(dataName, codePrefix, codeLastNumber);

        tableDataCodeSettingJob.setItems(loadAllDataCodeSetting(1));

        //set filter
        cftDJ = new ClassFilteringTable<>(
                SysCode.class,
                tableDataCodeSettingJob,
                tableDataCodeSettingJob.getItems());

        AnchorPane.setBottomAnchor(cftDJ, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cftDJ, 15.0);
        AnchorPane.setTopAnchor(cftDJ, 12.5);
        ancDJHeaderLayout.getChildren().clear();
        ancDJHeaderLayout.getChildren().add(cftDJ);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FSysCodeSettingManager fSysCodeManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fSysCodeManager = new FSysCodeSettingManagerImpl();

        //set tab pane
        setDataCodeSettingTabpane();

        //set splitpane
        setDataCodeSettingSplitpane();

        //init table (system)
        initTableDataCodeSetting();

        //init form
        initFormDataCodeSetting();

        spDataCodeSetting.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataCodeSettingFormShowStatus.set(0.0);
        });

        //init table (job)
        initTableDataCodeSettingJob();
    }

}
