/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_item_and_asset_md.item_type;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.service.FItemAndAssetManager;
import hotelfx.view.DashboardController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ItemTypeHKController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataItemTypeHK;

    private DoubleProperty dataItemTypeHKFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataItemTypeHKLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataItemTypeHKSplitpane() {
        spDataItemTypeHK.setDividerPositions(1);

        dataItemTypeHKFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataItemTypeHKFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataItemTypeHK.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataItemTypeHK.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataItemTypeHKFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataItemTypeHKLayout.setDisable(false);
                    tableDataItemTypeHKLayoutDisableLayer.setDisable(true);
                    tableDataItemTypeHKLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataItemTypeHKLayout.setDisable(true);
                    tableDataItemTypeHKLayoutDisableLayer.setDisable(false);
                    tableDataItemTypeHKLayoutDisableLayer.toFront();
                }
            }
        });

        dataItemTypeHKFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataItemTypeHKLayout;

    private ClassFilteringTable<TblItemTypeHk> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataItemTypeHK;

    private void initTableDataItemTypeHK() {
        //set table
        setTableDataItemTypeHK();
        //set control
        setTableControlDataItemTypeHK();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataItemTypeHK, 15.0);
        AnchorPane.setLeftAnchor(tableDataItemTypeHK, 15.0);
        AnchorPane.setRightAnchor(tableDataItemTypeHK, 15.0);
        AnchorPane.setTopAnchor(tableDataItemTypeHK, 15.0);
        ancBodyLayout.getChildren().add(tableDataItemTypeHK);
    }

    private void setTableDataItemTypeHK() {
        TableView<TblItemTypeHk> tableView = new TableView();

        TableColumn<TblItemTypeHk, String> idItemTypeHK = new TableColumn("ID");
        idItemTypeHK.setCellValueFactory(cellData -> cellData.getValue().codeItemTypeHkProperty());
        idItemTypeHK.setMinWidth(120);

        TableColumn<TblItemTypeHk, String> itemTypeHKName = new TableColumn("Tipe Barang");
        itemTypeHKName.setCellValueFactory(cellData -> cellData.getValue().itemTypeHknameProperty());
        itemTypeHKName.setMinWidth(140);

        TableColumn<TblItemTypeHk, String> itemTypeHKNote = new TableColumn("Keterangan");
        itemTypeHKNote.setCellValueFactory(cellData -> cellData.getValue().itemTypeHknoteProperty());
        itemTypeHKNote.setMinWidth(200);

        tableView.getColumns().addAll(itemTypeHKName, itemTypeHKNote);
        tableView.setItems(loadAllDataItemTypeHK());

        tableView.setRowFactory(tv -> {
            TableRow<TblItemTypeHk> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataItemTypeHKUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataItemTypeHKUpdateHandle();
                            } else {
                                dataItemTypeHKShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataItemTypeHKUpdateHandle();
//                            } else {
//                                dataItemTypeHKShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataItemTypeHK = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItemTypeHk.class,
                tableDataItemTypeHK.getTableView(),
                tableDataItemTypeHK.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataItemTypeHK() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataItemTypeHKCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataItemTypeHKUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataItemTypeHKDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataItemTypeHKPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataItemTypeHK.addButtonControl(buttonControls);
    }

    private ObservableList<TblItemTypeHk> loadAllDataItemTypeHK() {
        List<TblItemTypeHk> list = parentController.getService().getAllDataItemTypeHK();
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataItemTypeHK;

    @FXML
    private ScrollPane spFormDataItemTypeHK;

//    @FXML
//    private JFXTextField txtCodeItemTypeHK;

    @FXML
    private JFXTextField txtItemTypeHKName;

    @FXML
    private JFXTextArea txtItemTypeHKNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Label lblCode;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblItemTypeHk selectedData;

    private void initFormDataItemTypeHK() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataItemTypeHK.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataItemTypeHK.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Tipe Barang)"));
        btnSave.setOnAction((e) -> {
            dataItemTypeHKSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataItemTypeHKCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtItemTypeHKName);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal");
    }

    private void setSelectedDataToInputForm() {
        lblCode.setText((selectedData.getCodeItemTypeHk() != null
        ? selectedData.getCodeItemTypeHk() + " - " : "")
                + (selectedData.getItemTypeHkname() != null 
                        ? selectedData.getItemTypeHkname() : ""));
        
//        txtCodeItemTypeHK.textProperty().bindBidirectional(selectedData.codeItemTypeHkProperty());
        txtItemTypeHKName.textProperty().bindBidirectional(selectedData.itemTypeHknameProperty());
        txtItemTypeHKNote.textProperty().bindBidirectional(selectedData.itemTypeHknoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        txtCodeItemTypeHK.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataItemTypeHK,
                dataInputStatus == 3
//                ,
//                txtCodeItemTypeHK
        );

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataItemTypeHKCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblItemTypeHk();
        setSelectedDataToInputForm();
        //open form data item type hk
        dataItemTypeHKFormShowStatus.set(0);
        dataItemTypeHKFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataItemTypeHKUpdateHandle() {
        if (tableDataItemTypeHK.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getService().getDataItemTypeHK(((TblItemTypeHk) tableDataItemTypeHK.getTableView().getSelectionModel().getSelectedItem()).getIditemTypeHk());
            setSelectedDataToInputForm();
            //open form data item type hk
            dataItemTypeHKFormShowStatus.set(0);
            dataItemTypeHKFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataItemTypeHKShowHandle() {
        if (tableDataItemTypeHK.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getService().getDataItemTypeHK(((TblItemTypeHk) tableDataItemTypeHK.getTableView().getSelectionModel().getSelectedItem()).getIditemTypeHk());
            setSelectedDataToInputForm();
            dataItemTypeHKFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataItemTypeHKUnshowHandle() {
        refreshDataTableItemTypeHK();
        dataItemTypeHKFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataItemTypeHKDeleteHandle() {
        if (tableDataItemTypeHK.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getService().deleteDataItemTypeHK((TblItemTypeHk) tableDataItemTypeHK.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data item type hk
                    refreshDataTableItemTypeHK();
                    dataItemTypeHKFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataItemTypeHKPrintHandle() {

    }

    private void dataItemTypeHKSaveHandle() {
        if (checkDataInputDataItemTypeHK()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItemTypeHk dummySelectedData = new TblItemTypeHk(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getService().insertDataItemTypeHK(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data item type hk
                            refreshDataTableItemTypeHK();
                            dataItemTypeHKFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getService().updateDataItemTypeHK(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data item type hk
                            refreshDataTableItemTypeHK();
                            dataItemTypeHKFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getService().getErrorMessage(), null);
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

    private void dataItemTypeHKCancelHandle() {
        //refresh data from table & close form data item type hk
        refreshDataTableItemTypeHK();
        dataItemTypeHKFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableItemTypeHK() {
        tableDataItemTypeHK.getTableView().setItems(loadAllDataItemTypeHK());
        cft.refreshFilterItems(tableDataItemTypeHK.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataItemTypeHK() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtItemTypeHKName.getText() == null || txtItemTypeHKName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Tipe Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataItemTypeHKSplitpane();

        //init table
        initTableDataItemTypeHK();

        //init form
        initFormDataItemTypeHK();

        spDataItemTypeHK.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataItemTypeHKFormShowStatus.set(0);
        });
    }

    public ItemTypeHKController(ItemTypeController parentController) {
        this.parentController = parentController;
    }

    private final ItemTypeController parentController;

    public FItemAndAssetManager getService() {
        return parentController.getService();
    }

}
