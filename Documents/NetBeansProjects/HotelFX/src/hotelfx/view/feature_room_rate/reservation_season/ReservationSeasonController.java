/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_rate.reservation_season;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationSeason;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_room_rate.FeatureRoomRateController;
import java.net.URL;
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
public class ReservationSeasonController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataReservationSeason;

    private DoubleProperty dataReservationSeasonFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReservationSeasonLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReservationSeasonSplitpane() {
        spDataReservationSeason.setDividerPositions(1);

        dataReservationSeasonFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReservationSeasonFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataReservationSeason.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataReservationSeason.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReservationSeasonFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReservationSeasonLayout.setDisable(false);
                    tableDataReservationSeasonLayoutDisableLayer.setDisable(true);
                    tableDataReservationSeasonLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReservationSeasonLayout.setDisable(true);
                    tableDataReservationSeasonLayoutDisableLayer.setDisable(false);
                    tableDataReservationSeasonLayoutDisableLayer.toFront();
                }
            }
        });

        dataReservationSeasonFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataReservationSeasonLayout;

    private ClassFilteringTable<TblReservationSeason> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataReservationSeason;

    private void initTableDataReservationSeason() {
        //set table
        setTableDataReservationSeason();
        //set control
        setTableControlDataReservationSeason();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataReservationSeason, 15.0);
        AnchorPane.setLeftAnchor(tableDataReservationSeason, 15.0);
        AnchorPane.setRightAnchor(tableDataReservationSeason, 15.0);
        AnchorPane.setTopAnchor(tableDataReservationSeason, 15.0);
        ancBodyLayout.getChildren().add(tableDataReservationSeason);
    }

    private void setTableDataReservationSeason() {
        TableView<TblReservationSeason> tableView = new TableView();

        TableColumn<TblReservationSeason, String> reservationSeasonName = new TableColumn("Season");
        reservationSeasonName.setCellValueFactory(cellData -> cellData.getValue().seasonNameProperty());
        reservationSeasonName.setMinWidth(140);

        TableColumn<TblReservationSeason, String> reservationSeasonNote = new TableColumn("Keterangan");
        reservationSeasonNote.setCellValueFactory(cellData -> cellData.getValue().seasonNoteProperty());
        reservationSeasonNote.setMinWidth(200);

        tableView.getColumns().addAll(reservationSeasonName, reservationSeasonNote);
        tableView.setItems(loadAllDataReservationSeason());

        tableView.setRowFactory(tv -> {
            TableRow<TblReservationSeason> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataReservationSeasonUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataReservationSeasonUpdateHandle();
                            } else {
                                dataReservationSeasonShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataReservationSeasonUpdateHandle();
//                            } else {
//                                dataReservationSeasonShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataReservationSeason = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblReservationSeason.class,
                tableDataReservationSeason.getTableView(),
                tableDataReservationSeason.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataReservationSeason() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationSeasonCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataReservationSeasonUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationSeasonDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataReservationSeasonPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataReservationSeason.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationSeason> loadAllDataReservationSeason() {
        return FXCollections.observableArrayList(parentController.getFRoomRateManager().getAllDataReservationSeason());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataReservationSeason;

    @FXML
    private ScrollPane spFormDataReservationSeason;

    @FXML
    private JFXTextField txtReservationSeasonName;

    @FXML
    private JFXTextArea txtReservationSeasonNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblReservationSeason selectedData;

    private void initFormDataReservationSeason() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataReservationSeason.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataReservationSeason.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Season)"));
        btnSave.setOnAction((e) -> {
            dataReservationSeasonSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationSeasonCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtReservationSeasonName);
    }

    private void setSelectedDataToInputForm() {
        txtReservationSeasonName.textProperty().bindBidirectional(selectedData.seasonNameProperty());
        txtReservationSeasonNote.textProperty().bindBidirectional(selectedData.seasonNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataReservationSeason, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataReservationSeasonCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblReservationSeason();
        setSelectedDataToInputForm();
        //open form data reservation season
        dataReservationSeasonFormShowStatus.set(0.0);
        dataReservationSeasonFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataReservationSeasonUpdateHandle() {
        if (tableDataReservationSeason.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFRoomRateManager().getReservationSeason(((TblReservationSeason) tableDataReservationSeason.getTableView().getSelectionModel().getSelectedItem()).getIdseason());
            setSelectedDataToInputForm();
            //open form data reservation season
            dataReservationSeasonFormShowStatus.set(0.0);
            dataReservationSeasonFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataReservationSeasonShowHandle() {
        if (tableDataReservationSeason.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFRoomRateManager().getReservationSeason(((TblReservationSeason) tableDataReservationSeason.getTableView().getSelectionModel().getSelectedItem()).getIdseason());
            setSelectedDataToInputForm();
            dataReservationSeasonFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataReservationSeasonUnshowHandle() {
        refreshDataTableReservationSeason();
        dataReservationSeasonFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataReservationSeasonDeleteHandle() {
        if (tableDataReservationSeason.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFRoomRateManager().deleteDataReservationSeason((TblReservationSeason) tableDataReservationSeason.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data reservation season
                    refreshDataTableReservationSeason();
                    dataReservationSeasonFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFRoomRateManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataReservationSeasonPrintHandle() {

    }

    private void dataReservationSeasonSaveHandle() {
        if (checkDataInputDataReservationSeason()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservationSeason dummySelectedData = new TblReservationSeason(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFRoomRateManager().insertDataReservationSeason(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data reservation season
                            refreshDataTableReservationSeason();
                            dataReservationSeasonFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFRoomRateManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFRoomRateManager().updateDataReservationSeason(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data reservation season
                            refreshDataTableReservationSeason();
                            dataReservationSeasonFormShowStatus.set(0.0);
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

    private void dataReservationSeasonCancelHandle() {
        //refresh data from table & close form data reservation season
        refreshDataTableReservationSeason();
        dataReservationSeasonFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableReservationSeason() {
        tableDataReservationSeason.getTableView().setItems(loadAllDataReservationSeason());
        cft.refreshFilterItems(tableDataReservationSeason.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataReservationSeason() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtReservationSeasonName.getText() == null || txtReservationSeasonName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Season : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        setDataReservationSeasonSplitpane();

        //init table
        initTableDataReservationSeason();

        //init form
        initFormDataReservationSeason();

        spDataReservationSeason.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReservationSeasonFormShowStatus.set(0.0);
        });
    }

    public ReservationSeasonController(FeatureRoomRateController parentController) {
        this.parentController = parentController;
    }

    private final FeatureRoomRateController parentController;

}
