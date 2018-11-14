/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_rate.reservation_bar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_room_rate.FeatureRoomRateController;
import java.math.BigDecimal;
import java.net.URL;
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

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationBARController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataReservationBAR;

    private DoubleProperty dataReservationBARFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReservationBARLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReservationBARSplitpane() {
        spDataReservationBAR.setDividerPositions(1);

        dataReservationBARFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReservationBARFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataReservationBAR.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataReservationBAR.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReservationBARFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReservationBARLayout.setDisable(false);
                    tableDataReservationBARLayoutDisableLayer.setDisable(true);
                    tableDataReservationBARLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReservationBARLayout.setDisable(true);
                    tableDataReservationBARLayoutDisableLayer.setDisable(false);
                    tableDataReservationBARLayoutDisableLayer.toFront();
                }
            }
        });

        dataReservationBARFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataReservationBARLayout;

    private ClassFilteringTable<TblReservationBar> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataReservationBAR;

    private void initTableDataReservationBAR() {
        //set table
        setTableDataReservationBAR();
        //set control
        setTableControlDataReservationBAR();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataReservationBAR, 15.0);
        AnchorPane.setLeftAnchor(tableDataReservationBAR, 15.0);
        AnchorPane.setRightAnchor(tableDataReservationBAR, 15.0);
        AnchorPane.setTopAnchor(tableDataReservationBAR, 15.0);
        ancBodyLayout.getChildren().add(tableDataReservationBAR);
    }

    private void setTableDataReservationBAR() {
        TableView<TblReservationBar> tableView = new TableView();

        TableColumn<TblReservationBar, String> reservationBARName = new TableColumn("BAR");
        reservationBARName.setCellValueFactory(cellData -> cellData.getValue().barnameProperty());
        reservationBARName.setMinWidth(140);

        TableColumn<TblReservationBar, String> reservationBARPercentage = new TableColumn("Multiple");
        reservationBARPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBar, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getBarpercentage()), param.getValue().barpercentageProperty()));
        reservationBARPercentage.setMinWidth(120);

        tableView.getColumns().addAll(reservationBARName, reservationBARPercentage);
        tableView.setItems(loadAllDataReservationBAR());

        tableView.setRowFactory(tv -> {
            TableRow<TblReservationBar> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataReservationBARUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataReservationBARUpdateHandle();
                            } else {
                                dataReservationBARShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataReservationBARUpdateHandle();
//                            } else {
//                                dataReservationBARShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataReservationBAR = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblReservationBar.class,
                tableDataReservationBAR.getTableView(),
                tableDataReservationBAR.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataReservationBAR() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationBARCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataReservationBARUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationBARDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataReservationBARPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataReservationBAR.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationBar> loadAllDataReservationBAR() {
        List<TblReservationBar> list = parentController.getFRoomRateManager().getAllDataReservationBAR();
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataReservationBAR;

    @FXML
    private ScrollPane spFormDataReservationBAR;

    @FXML
    private JFXTextField txtReservationBARName;

    @FXML
    private JFXTextField txtReservationBARPercentage;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblReservationBar selectedData;

    private void initFormDataReservationBAR() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataReservationBAR.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataReservationBAR.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data BAR)"));
        btnSave.setOnAction((e) -> {
            dataReservationBARSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationBARCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtReservationBARName,
                txtReservationBARPercentage);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtReservationBARPercentage);
    }

    private void setSelectedDataToInputForm() {
        txtReservationBARName.textProperty().bindBidirectional(selectedData.barnameProperty());
        Bindings.bindBidirectional(txtReservationBARPercentage.textProperty(), selectedData.barpercentageProperty(), new ClassFormatter.CBigDecimalStringConverter());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataReservationBAR, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataReservationBARCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblReservationBar();
        selectedData.setBarpercentage(new BigDecimal("0"));
        setSelectedDataToInputForm();
        //open form data reservation bar
        dataReservationBARFormShowStatus.set(0.0);
        dataReservationBARFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataReservationBARUpdateHandle() {
        if (tableDataReservationBAR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFRoomRateManager().getReservationBAR(((TblReservationBar) tableDataReservationBAR.getTableView().getSelectionModel().getSelectedItem()).getIdbar());
            setSelectedDataToInputForm();
            //open form data reservation bar
            dataReservationBARFormShowStatus.set(0.0);
            dataReservationBARFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataReservationBARShowHandle() {
        if (tableDataReservationBAR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFRoomRateManager().getReservationBAR(((TblReservationBar) tableDataReservationBAR.getTableView().getSelectionModel().getSelectedItem()).getIdbar());
            setSelectedDataToInputForm();
            dataReservationBARFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataReservationBARUnshowHandle() {
        refreshDataTableReservationBAR();
        dataReservationBARFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataReservationBARDeleteHandle() {
        if (tableDataReservationBAR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFRoomRateManager().deleteDataReservationBAR((TblReservationBar) tableDataReservationBAR.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data reservation bar
                    refreshDataTableReservationBAR();
                    dataReservationBARFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFRoomRateManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataReservationBARPrintHandle() {

    }

    private void dataReservationBARSaveHandle() {
        if (checkDataInputDataReservationBAR()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservationBar dummySelectedData = new TblReservationBar(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFRoomRateManager().insertDataReservationBAR(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data reservation bar
                            refreshDataTableReservationBAR();
                            dataReservationBARFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFRoomRateManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFRoomRateManager().updateDataReservationBAR(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data reservation bar
                            refreshDataTableReservationBAR();
                            dataReservationBARFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFRoomRateManager().getErrorMessage(), null);
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

    private void dataReservationBARCancelHandle() {
        //refresh data from table & close form data reservation bar
        refreshDataTableReservationBAR();
        dataReservationBARFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableReservationBAR() {
        tableDataReservationBAR.getTableView().setItems(loadAllDataReservationBAR());
        cft.refreshFilterItems(tableDataReservationBAR.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationBAR() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtReservationBARName.getText() == null || txtReservationBARName.getText().equals("")) {
            dataInput = false;
            errDataInput += "BAR (Nama) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtReservationBARPercentage.getText() == null
                || txtReservationBARPercentage.getText().equals("")
                || txtReservationBARPercentage.getText().equals("-")) {
            dataInput = false;
            errDataInput += "BAR (Multiple) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getBarpercentage().doubleValue() <= 1) {
                errDataInput += "BAR (Multiple) : Nilai tidak dapat lebih kecil dari '1' \n";
            }
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
        setDataReservationBARSplitpane();

        //init table
        initTableDataReservationBAR();

        //init form
        initFormDataReservationBAR();

        spDataReservationBAR.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReservationBARFormShowStatus.set(0.0);
        });
    }

    public ReservationBARController(FeatureRoomRateController parentController) {
        this.parentController = parentController;
    }

    private final FeatureRoomRateController parentController;

}
