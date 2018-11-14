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
import hotelfx.persistence.model.TblItemTypeWh;
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
public class ItemTypeWHController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataItemTypeWH;

    private DoubleProperty dataItemTypeWHFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataItemTypeWHLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataItemTypeWHSplitpane() {
        spDataItemTypeWH.setDividerPositions(1);

        dataItemTypeWHFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataItemTypeWHFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataItemTypeWH.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataItemTypeWH.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataItemTypeWHFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataItemTypeWHLayout.setDisable(false);
                    tableDataItemTypeWHLayoutDisableLayer.setDisable(true);
                    tableDataItemTypeWHLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataItemTypeWHLayout.setDisable(true);
                    tableDataItemTypeWHLayoutDisableLayer.setDisable(false);
                    tableDataItemTypeWHLayoutDisableLayer.toFront();
                }
            }
        });

        dataItemTypeWHFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataItemTypeWHLayout;

    private ClassFilteringTable<TblItemTypeWh> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataItemTypeWH;

    private void initTableDataItemTypeWH() {
        //set table
        setTableDataItemTypeWH();
        //set control
        setTableControlDataItemTypeWH();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataItemTypeWH, 15.0);
        AnchorPane.setLeftAnchor(tableDataItemTypeWH, 15.0);
        AnchorPane.setRightAnchor(tableDataItemTypeWH, 15.0);
        AnchorPane.setTopAnchor(tableDataItemTypeWH, 15.0);
        ancBodyLayout.getChildren().add(tableDataItemTypeWH);
    }

    private void setTableDataItemTypeWH() {
        TableView<TblItemTypeWh> tableView = new TableView();

        TableColumn<TblItemTypeWh, String> idItemTypeWH = new TableColumn("ID");
        idItemTypeWH.setCellValueFactory(cellData -> cellData.getValue().codeItemTypeWhProperty());
        idItemTypeWH.setMinWidth(120);

        TableColumn<TblItemTypeWh, String> itemTypeWHName = new TableColumn("Tipe Barang");
        itemTypeWHName.setCellValueFactory(cellData -> cellData.getValue().itemTypeWhnameProperty());
        itemTypeWHName.setMinWidth(140);

        TableColumn<TblItemTypeWh, String> itemTypeWHNote = new TableColumn("Keterangan");
        itemTypeWHNote.setCellValueFactory(cellData -> cellData.getValue().itemTypeWhnoteProperty());
        itemTypeWHNote.setMinWidth(200);

        tableView.getColumns().addAll(itemTypeWHName, itemTypeWHNote);
        tableView.setItems(loadAllDataItemTypeWH());

        tableView.setRowFactory(tv -> {
            TableRow<TblItemTypeWh> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataItemTypeWHUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataItemTypeWHUpdateHandle();
                            } else {
                                dataItemTypeWHShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataItemTypeWHUpdateHandle();
//                            } else {
//                                dataItemTypeWHShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataItemTypeWH = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblItemTypeWh.class,
                tableDataItemTypeWH.getTableView(),
                tableDataItemTypeWH.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataItemTypeWH() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataItemTypeWHCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataItemTypeWHUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataItemTypeWHDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataItemTypeWHPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataItemTypeWH.addButtonControl(buttonControls);
    }

    private ObservableList<TblItemTypeWh> loadAllDataItemTypeWH() {
        List<TblItemTypeWh> list = parentController.getService().getAllDataItemTypeWH();
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataItemTypeWH;

    @FXML
    private ScrollPane spFormDataItemTypeWH;

//    @FXML
//    private JFXTextField txtCodeItemTypeWH;

    @FXML
    private JFXTextField txtItemTypeWHName;

    @FXML
    private JFXTextArea txtItemTypeWHNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Label lblCode;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblItemTypeWh selectedData;

    private void initFormDataItemTypeWH() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataItemTypeWH.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataItemTypeWH.setOnScroll((ScrollEvent scroll) -> {
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
            dataItemTypeWHSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataItemTypeWHCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtItemTypeWHName);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal");
    }

    private void setSelectedDataToInputForm() {
        lblCode.setText((selectedData.getCodeItemTypeWh()!= null
        ? selectedData.getCodeItemTypeWh()+ " - " : "")
                + (selectedData.getItemTypeWhname()!= null 
                        ? selectedData.getItemTypeWhname(): ""));
        
//        txtCodeItemTypeWH.textProperty().bindBidirectional(selectedData.codeItemTypeWhProperty());
        txtItemTypeWHName.textProperty().bindBidirectional(selectedData.itemTypeWhnameProperty());
        txtItemTypeWHNote.textProperty().bindBidirectional(selectedData.itemTypeWhnoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        txtCodeItemTypeWH.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataItemTypeWH,
                dataInputStatus == 3
//                ,
//                txtCodeItemTypeWH
        );

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataItemTypeWHCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblItemTypeWh();
        setSelectedDataToInputForm();
        //open form data item type wh
        dataItemTypeWHFormShowStatus.set(0);
        dataItemTypeWHFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataItemTypeWHUpdateHandle() {
        if (tableDataItemTypeWH.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getService().getDataItemTypeWH(((TblItemTypeWh) tableDataItemTypeWH.getTableView().getSelectionModel().getSelectedItem()).getIditemTypeWh());
            setSelectedDataToInputForm();
            //open form data item type wh
            dataItemTypeWHFormShowStatus.set(0);
            dataItemTypeWHFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataItemTypeWHShowHandle() {
        if (tableDataItemTypeWH.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getService().getDataItemTypeWH(((TblItemTypeWh) tableDataItemTypeWH.getTableView().getSelectionModel().getSelectedItem()).getIditemTypeWh());
            setSelectedDataToInputForm();
            dataItemTypeWHFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataItemTypeWHUnshowHandle() {
        refreshDataTableItemTypeWH();
        dataItemTypeWHFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataItemTypeWHDeleteHandle() {
        if (tableDataItemTypeWH.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getService().deleteDataItemTypeWH((TblItemTypeWh) tableDataItemTypeWH.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data item type wh
                    refreshDataTableItemTypeWH();
                    dataItemTypeWHFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataItemTypeWHPrintHandle() {

    }

    private void dataItemTypeWHSaveHandle() {
        if (checkDataInputDataItemTypeWH()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblItemTypeWh dummySelectedData = new TblItemTypeWh(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getService().insertDataItemTypeWH(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data item type wh
                            refreshDataTableItemTypeWH();
                            dataItemTypeWHFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getService().updateDataItemTypeWH(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data item type wh
                            refreshDataTableItemTypeWH();
                            dataItemTypeWHFormShowStatus.set(0);
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

    private void dataItemTypeWHCancelHandle() {
        //refresh data from table & close form data item type wh
        refreshDataTableItemTypeWH();
        dataItemTypeWHFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableItemTypeWH() {
        tableDataItemTypeWH.getTableView().setItems(loadAllDataItemTypeWH());
        cft.refreshFilterItems(tableDataItemTypeWH.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataItemTypeWH() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtItemTypeWHName.getText() == null || txtItemTypeWHName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Tipe Barang : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataItemTypeWHSplitpane();

        //init table
        initTableDataItemTypeWH();

        //init form
        initFormDataItemTypeWH();

        spDataItemTypeWH.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataItemTypeWHFormShowStatus.set(0);
        });
    }    
    
    public ItemTypeWHController(ItemTypeController parentController) {
        this.parentController = parentController;
    }

    private final ItemTypeController parentController;

    public FItemAndAssetManager getService() {
        return parentController.getService();
    }
    
}
