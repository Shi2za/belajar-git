/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_allowance.allowance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblEmployeeAllowance;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_allowance.FeatureAllowanceController;
import java.math.BigDecimal;
import java.net.URL;
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
public class AllowanceController implements Initializable {

    @FXML
    private SplitPane spDataAllowance;
    private DoubleProperty dataAllowanceFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataAllowanceLayoutDisableLayer;

    private void setDataAllowanceSplitPane() {
        dataAllowanceFormShowStatus = new SimpleDoubleProperty(1.0);
        DoubleProperty divPosition = new SimpleDoubleProperty();
        divPosition.bind(new SimpleDoubleProperty(1.0).subtract(
                (formAnchorAllowance.prefWidthProperty().divide(spDataAllowance.widthProperty()))
                .multiply(dataAllowanceFormShowStatus))
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            spDataAllowance.setDividerPositions(newVal.doubleValue());
        });

        SplitPane.Divider div = spDataAllowance.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            div.setPosition(divPosition.get());
        });

        dataAllowanceFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {
                    tableDataAllowanceLayout.setDisable(false);
                    tableDataAllowanceLayoutDisableLayer.setDisable(true);
                    tableDataAllowanceLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    tableDataAllowanceLayout.setDisable(true);
                    tableDataAllowanceLayoutDisableLayer.setDisable(false);
                    tableDataAllowanceLayoutDisableLayer.toFront();
                }
            }
        });

        dataAllowanceFormShowStatus.set(0);
    }

    @FXML
    private AnchorPane tableDataAllowanceLayout;

    private ClassFilteringTable<TblEmployeeAllowance> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataAllowance;

    private void initTableDataAllowance() {
        setTableDataAllowance();
        setTableControlDataAllowance();
        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataAllowance, 15.0);
        AnchorPane.setLeftAnchor(tableDataAllowance, 15.0);
        AnchorPane.setBottomAnchor(tableDataAllowance, 15.0);
        AnchorPane.setRightAnchor(tableDataAllowance, 15.0);

        ancBodyLayout.getChildren().add(tableDataAllowance);
    }

    private void setTableDataAllowance() {
        TableView<TblEmployeeAllowance> tableView = new TableView();

        TableColumn<TblEmployeeAllowance, String> allowanceName = new TableColumn("Allowance Name");
        allowanceName.setCellValueFactory(cellData -> cellData.getValue().allowanceNameProperty());
        TableColumn<TblEmployeeAllowance, String> allowanceNominal = new TableColumn("Allowance Nominal");
        allowanceNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeAllowance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getAllowanceNominal()),
                        param.getValue().allowanceNominalProperty()));
        TableColumn<TblEmployeeAllowance, String> allowanceNote = new TableColumn("Allowance Note");
        allowanceNote.setCellValueFactory(cellData -> cellData.getValue().allowanceNoteProperty());

        tableView.getColumns().addAll(allowanceName, allowanceNominal, allowanceNote);
        tableView.setItems(loadAllDataAllowance());

        tableView.setRowFactory(tv -> {
            TableRow<TblEmployeeAllowance> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataAllowanceUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                dataAllowanceUpdateHandle();
                            } else {
                                dataAllowanceShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                dataAllowanceUpdateHandle();
//                            } else {
//                                dataAllowanceShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataAllowance = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblEmployeeAllowance.class,
                tableDataAllowance.getTableView(),
                tableDataAllowance.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private ObservableList<TblEmployeeAllowance> loadAllDataAllowance() {
        return FXCollections.observableArrayList(parentController.getAllowanceManager().getAllDataAllowance());
    }

    private void setTableControlDataAllowance() {
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                dataAllowanceCreateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                dataAllowanceUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                dataAllowanceDeleteHandle();
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
        tableDataAllowance.addButtonControl(buttonControls);

    }

    @FXML
    private AnchorPane formAnchorAllowance;
    @FXML
    private ScrollPane spFormDataAllowance;
    @FXML
    private GridPane gpFormDataAllowance;

    @FXML
    private JFXTextField txtAllowanceName;
    @FXML
    private JFXTextField txtAllowanceNominal;
    @FXML
    private JFXTextArea txtAllowanceNote;

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final PseudoClass onScrollPseudoClassEven = PseudoClass.getPseudoClass("onScrollEven");
    private BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private TblEmployeeAllowance selectedData;
    private int scrollCounter = 0;

    private void initFormDataAllowance() {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            spFormDataAllowance.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        gpFormDataAllowance.setOnScroll((ScrollEvent scroll) -> {
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
                    System.out.println("err" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        btnSave.setTooltip(new Tooltip("Save (Data Allowance)"));
        btnSave.setOnMouseClicked((e) -> {
            dataAllowanceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Cancel"));
        btnCancel.setOnMouseClicked((e) -> {
            dataAllowanceCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtAllowanceName,
                txtAllowanceNominal);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtAllowanceNominal);
    }

    private void setSelectedDataToInputForm() {
        txtAllowanceName.textProperty().bindBidirectional(selectedData.allowanceNameProperty());
        Bindings.bindBidirectional(txtAllowanceNominal.textProperty(), selectedData.allowanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtAllowanceNote.textProperty().bindBidirectional(selectedData.allowanceNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataAllowance, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    private int dataInputStatus = 0;

    private void dataAllowanceCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblEmployeeAllowance();
        selectedData.setAllowanceNominal(new BigDecimal("0"));
        setSelectedDataToInputForm();
        dataAllowanceFormShowStatus.set(0.0);
        dataAllowanceFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataAllowanceUpdateHandle() {
        if (tableDataAllowance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getAllowanceManager().getDataAllowance(((TblEmployeeAllowance) tableDataAllowance.getTableView().getSelectionModel().getSelectedItem()).getIdallowance());
            setSelectedDataToInputForm();
            dataAllowanceFormShowStatus.set(0.0);
            dataAllowanceFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataAllowanceShowHandle() {
        if (tableDataAllowance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getAllowanceManager().getDataAllowance(((TblEmployeeAllowance) tableDataAllowance.getTableView().getSelectionModel().getSelectedItem()).getIdallowance());
            setSelectedDataToInputForm();
            dataAllowanceFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataAllowanceUnshowHandle() {
        refreshDataTableAllowance();
        dataAllowanceFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataAllowanceDeleteHandle() {
        if (tableDataAllowance.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getAllowanceManager().deleteDataAllowance(new TblEmployeeAllowance((TblEmployeeAllowance) tableDataAllowance.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    refreshDataTableAllowance();
                    dataAllowanceFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getAllowanceManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataAllowanceSaveHandle() {
        if (checkDataInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblEmployeeAllowance dummySelctedData = new TblEmployeeAllowance(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getAllowanceManager().insertDataAllowance(dummySelctedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            refreshDataTableAllowance();
                            dataAllowanceFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getAllowanceManager().getErrorMessage(), null);
                        }
                        break;

                    case 1:
                        if (parentController.getAllowanceManager().updateDataAllowance(dummySelctedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            refreshDataTableAllowance();
                            dataAllowanceFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getAllowanceManager().getErrorMessage(), null);
                        }
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private String errDataInput;

    private boolean checkDataInput() {
        boolean check = true;
        errDataInput = "";
        if (txtAllowanceName.getText() == null || txtAllowanceName.getText().equals("")) {
            check = false;
            errDataInput += "Allowance Name : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtAllowanceNominal.getText() == null
                || txtAllowanceNominal.getText().equals("")
                || txtAllowanceNominal.getText().equals("-")) {
            check = false;
            errDataInput += "Allowance Nominal : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getAllowanceNominal()
                    .compareTo(new BigDecimal("0")) == -1) {
                check = false;
                errDataInput += "Allowance Nominal : Tidak boleh kurang dari '0' \n";
            }
        }
        return check;
    }

    private void dataAllowanceCancelHandle() {
        refreshDataTableAllowance();
        dataAllowanceFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableAllowance() {
        tableDataAllowance.getTableView().setItems(loadAllDataAllowance());
        cft.refreshFilterItems(tableDataAllowance.getTableView().getItems());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataAllowanceSplitPane();
        initTableDataAllowance();
        initFormDataAllowance();
        spDataAllowance.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataAllowanceFormShowStatus.set(0.0);
        });
    }

    public AllowanceController(FeatureAllowanceController parentController) {
        this.parentController = parentController;
    }

    private final FeatureAllowanceController parentController;
}
