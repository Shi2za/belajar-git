/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_insurance.insurance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblEmployeeInsurance;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_insurance.FeatureInsuranceController;
import java.math.BigDecimal;
import java.net.URL;
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

/**
 *
 * @author Andreas
 */
public class InsuranceController implements Initializable {

    @FXML
    private SplitPane spDataInsurance;
    private DoubleProperty dataInsuranceFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataInsuranceLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataInsuranceSplitPane() {
        spDataInsurance.setDividerPositions(1);
        
        dataInsuranceFormShowStatus = new SimpleDoubleProperty(1.0);
        
        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataInsuranceFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataInsurance.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataInsurance.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataInsuranceFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    tableDataInsuranceLayout.setDisable(false);
                    tableDataInsuranceLayoutDisableLayer.setDisable(true);
                    tableDataInsuranceLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataInsuranceLayout.setDisable(true);
                    tableDataInsuranceLayoutDisableLayer.setDisable(false);
                    tableDataInsuranceLayoutDisableLayer.toFront();
                }
            }
        });

        dataInsuranceFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataInsuranceLayout;
    private ClassTableWithControl tableDataInsurance;

    private void initTableDataInsurance() {
        setTableDataInsurance();
        setTableControlDataInsurance();

        tableDataInsuranceLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataInsurance, 15.0);
        AnchorPane.setLeftAnchor(tableDataInsurance, 15.0);
        AnchorPane.setBottomAnchor(tableDataInsurance, 15.0);
        AnchorPane.setRightAnchor(tableDataInsurance, 15.0);

        tableDataInsuranceLayout.getChildren().add(tableDataInsurance);
    }

    private void setTableDataInsurance() {
        TableView<TblEmployeeInsurance> tableView = new TableView();
        TableColumn<TblEmployeeInsurance, String> nameInsurance = new TableColumn("Name Insurance");
        nameInsurance.setCellValueFactory(cellData -> cellData.getValue().insuranceNameProperty());
        TableColumn<TblEmployeeInsurance, String> nameCompany = new TableColumn("Name Company");
        nameCompany.setCellValueFactory(cellData -> cellData.getValue().insuranceCompanyProperty());
        TableColumn<TblEmployeeInsurance, String> nominalInsurance = new TableColumn("Nominal Insurance");
        nominalInsurance.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeInsurance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getInsuranceNominal()),
                        param.getValue().insuranceNominalProperty()));
        TableColumn<TblEmployeeInsurance, String> noteInsurance = new TableColumn("Note Insurance");
        noteInsurance.setCellValueFactory(cellData -> cellData.getValue().insuranceNoteProperty());
        TableColumn<TblEmployeeInsurance, String> noteCompany = new TableColumn("Note Company");
        noteCompany.setCellValueFactory(cellData -> cellData.getValue().companyNoteProperty());

        tableView.getColumns().addAll(nameInsurance, nameCompany, nominalInsurance, noteInsurance, noteCompany);
        tableView.setItems(loadAllDataInsurance());

        tableView.setRowFactory(tv -> {
            TableRow<TblEmployeeInsurance> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataInsuranceUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataInsuranceUpdateHandle();
                            } else {
                                dataInsuranceShowHandle();
                            }
                        }
                    }
                } else {
                    if (!row.isEmpty()) {
                        if (isShowStatus.get()) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataInsuranceUpdateHandle();
                            } else {
                                dataInsuranceShowHandle();
                            }
                        }
                    }
                }
            });
            return row;
        });

        tableDataInsurance = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataInsurance() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataInsuranceCreateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataInsuranceUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataInsuranceDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Print");
            buttonControl.setOnMouseClicked((e) -> {

            });
            buttonControls.add(buttonControl);
        }
        tableDataInsurance.addButtonControl(buttonControls);
    }

    private ObservableList<TblEmployeeInsurance> loadAllDataInsurance() {
        return FXCollections.observableArrayList(parentController.getFInsuranceManager().getAllDataInsurance());
    }

    @FXML
    private AnchorPane anchorFormInsurance;
    @FXML
    private ScrollPane spFormDataInsurance;
    @FXML
    private GridPane gpFormDataInsurance;
    @FXML
    private JFXTextField txtNameInsurance;
    @FXML
    private JFXTextField txtCompanyName;
    @FXML
    private JFXTextField txtNominalInsurance;
    @FXML
    private JFXTextArea txtNoteInsurance;
    @FXML
    private JFXTextArea txtNoteCompany;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    private TblEmployeeInsurance selectedData;

    private void initFormDataInsurance() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataInsurance.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataInsurance.setOnScroll((ScrollEvent scroll) -> {
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
                    System.out.println("Err:" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setTooltip(new Tooltip("Save (Insurance)"));
        btnSave.setOnAction((e) -> {
            dataInsuranceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Cancel"));
        btnCancel.setOnAction((e) -> {
            dataInsuranceCancelHandle();
        });

        initImportantFieldColor();
        
        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtNameInsurance,
                txtCompanyName,
                txtNominalInsurance);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtNominalInsurance);
    }
    
    private void setSelectedDataToInputForm() {
        txtNameInsurance.textProperty().bindBidirectional(selectedData.insuranceNameProperty());
        txtCompanyName.textProperty().bindBidirectional(selectedData.insuranceCompanyProperty());
        Bindings.bindBidirectional(txtNominalInsurance.textProperty(), selectedData.insuranceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtNoteCompany.textProperty().bindBidirectional(selectedData.companyNoteProperty());
        txtNoteInsurance.textProperty().bindBidirectional(selectedData.insuranceNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataInsurance, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    private int dataInputStatus = 0;

    private void dataInsuranceCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblEmployeeInsurance();
        selectedData.setInsuranceNominal(new BigDecimal("0"));
        setSelectedDataToInputForm();
        dataInsuranceFormShowStatus.set(0);
        dataInsuranceFormShowStatus.set(1);
    }

    private void dataInsuranceUpdateHandle() {
        if (tableDataInsurance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFInsuranceManager().getDataInsurance(((TblEmployeeInsurance) tableDataInsurance.getTableView().getSelectionModel().getSelectedItem()).getIdinsurance());
            setSelectedDataToInputForm();
            dataInsuranceFormShowStatus.set(0);
            dataInsuranceFormShowStatus.set(1);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataInsuranceShowHandle() {
        if (tableDataInsurance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFInsuranceManager().getDataInsurance(((TblEmployeeInsurance) tableDataInsurance.getTableView().getSelectionModel().getSelectedItem()).getIdinsurance());
            setSelectedDataToInputForm();
            dataInsuranceFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataInsuranceUnshowHandle() {
        tableDataInsurance.getTableView().setItems(loadAllDataInsurance());
        dataInsuranceFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataInsuranceDeleteHandle() {
        if (tableDataInsurance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFInsuranceManager().deleteDataInsurance(new TblEmployeeInsurance((TblEmployeeInsurance) tableDataInsurance.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    tableDataInsurance.getTableView().setItems(loadAllDataInsurance());
                    dataInsuranceFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFInsuranceManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataInsuranceSaveHandle() {
        if (checkDataInputInsurance()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblEmployeeInsurance dummySelectedData = new TblEmployeeInsurance(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFInsuranceManager().insertDataInsurance(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            tableDataInsurance.getTableView().setItems(loadAllDataInsurance());
                            dataInsuranceFormShowStatus.set(0);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFInsuranceManager().getErrorMessage(), null);
                        }
                        break;

                    case 1:
                        if (parentController.getFInsuranceManager().updateDataInsurance(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            tableDataInsurance.getTableView().setItems(loadAllDataInsurance());
                            dataInsuranceFormShowStatus.set(0);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFInsuranceManager().getErrorMessage(), null);
                        }
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage("", null);
        }
    }

    private void dataInsuranceCancelHandle() {
        tableDataInsurance.getTableView().setItems(loadAllDataInsurance());
        dataInsuranceFormShowStatus.set(0);
    }

    private String errDataInput;

    private boolean checkDataInputInsurance() {
        boolean check = true;
        errDataInput = "";
        if (txtNameInsurance.getText() == null || txtNameInsurance.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Insurance Name : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtCompanyName.getText() == null || txtCompanyName.getText().equalsIgnoreCase("")) {
            check = false;
            errDataInput += "Company Name : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtNominalInsurance.getText() == null 
                || txtNominalInsurance.getText().equalsIgnoreCase("")
                || txtNominalInsurance.getText().equalsIgnoreCase("-")) {
            check = false;
            errDataInput += "Insurance Nominal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }else{
            if(selectedData.getInsuranceNominal()
                    .compareTo(new BigDecimal("0")) == -1){
                check = false;
                errDataInput += "Insurance Nominal : Tidak boleh kurang dari '0' \n";
            }
        }
        return check;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataInsuranceSplitPane();
        initTableDataInsurance();
        initFormDataInsurance();

        spDataInsurance.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataInsuranceFormShowStatus.set(0);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InsuranceController(FeatureInsuranceController parentController) {
        this.parentController = parentController;
    }

    private final FeatureInsuranceController parentController;
}
