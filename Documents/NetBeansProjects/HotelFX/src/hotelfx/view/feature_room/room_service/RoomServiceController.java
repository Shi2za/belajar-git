/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room.room_service;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_room_and_service_md.FeatureRoomAndServiceMDController;
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
public class RoomServiceController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRoomService;

    private DoubleProperty dataRoomServiceFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRoomServiceLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataRoomServiceSplitpane() {
        spDataRoomService.setDividerPositions(1);

        dataRoomServiceFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRoomServiceFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRoomService.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRoomService.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRoomServiceFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataRoomServiceLayout.setDisable(false);
                    tableDataRoomServiceLayoutDisableLayer.setDisable(true);
                    tableDataRoomServiceLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataRoomServiceLayout.setDisable(true);
                    tableDataRoomServiceLayoutDisableLayer.setDisable(false);
                    tableDataRoomServiceLayoutDisableLayer.toFront();
                }
            }
        });

        dataRoomServiceFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataRoomServiceLayout;

    private ClassFilteringTable<TblRoomService> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRoomService;

    private void initTableDataRoomService() {
        //set table
        setTableDataRoomService();
        //set control
        setTableControlDataRoomService();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoomService, 15.0);
        AnchorPane.setLeftAnchor(tableDataRoomService, 15.0);
        AnchorPane.setRightAnchor(tableDataRoomService, 15.0);
        AnchorPane.setTopAnchor(tableDataRoomService, 15.0);
        ancBodyLayout.getChildren().add(tableDataRoomService);
    }

    private void setTableDataRoomService() {
        TableView<TblRoomService> tableView = new TableView();

        TableColumn<TblRoomService, String> idRoomService = new TableColumn("ID");
        idRoomService.setCellValueFactory(cellData -> cellData.getValue().codeRoomServiceProperty());
        idRoomService.setMinWidth(120);

        TableColumn<TblRoomService, String> roomServiceName = new TableColumn("Layanan");
        roomServiceName.setCellValueFactory(cellData -> cellData.getValue().serviceNameProperty());
        roomServiceName.setMinWidth(140);

        TableColumn<TblRoomService, String> roomServiceCost = new TableColumn("Harga");
        roomServiceCost.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()),
                        param.getValue().priceProperty()));
        roomServiceCost.setMinWidth(120);

        TableColumn<TblRoomService, String> roomServiceNote = new TableColumn("Keterangan");
        roomServiceNote.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
        roomServiceNote.setMinWidth(200);

        tableView.getColumns().addAll(idRoomService, roomServiceName, roomServiceCost, roomServiceNote);
        tableView.setItems(loadAllDataRoomService());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoomService> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataRoomServiceUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataRoomServiceUpdateHandle();
                            } else {
                                dataRoomServiceShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataRoomServiceUpdateHandle();
//                            } else {
//                                dataRoomServiceShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataRoomService = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblRoomService.class,
                tableDataRoomService.getTableView(),
                tableDataRoomService.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataRoomService() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRoomServiceCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomServiceUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataRoomServiceDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataRoomServicePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataRoomService.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomService> loadAllDataRoomService() {
        List<TblRoomService> list = parentController.getFRoomManager().getAllDataRoomService();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdroomService() == (long) 4) {    //Lainnya (Bonus Voucher) = '4'
                list.remove(i);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataRoomService;

    @FXML
    private ScrollPane spFormDataRoomService;

    @FXML
    private JFXTextField txtCodeRoomService;

    @FXML
    private JFXTextField txtRoomServiceName;

    @FXML
    private JFXTextField txtRoomServicePrice;

    @FXML
    private JFXTextArea txtRoomServiceNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Label lblRoomService;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblRoomService selectedData;

    private void initFormDataRoomService() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRoomService.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRoomService.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Room Service)"));
        btnSave.setOnAction((e) -> {
            dataRoomServiceSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoomServiceCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtRoomServiceName,
                txtRoomServicePrice);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtRoomServicePrice);
    }

    private void setSelectedDataToInputForm() {
        // txtCodeRoomService.textProperty().bindBidirectional(selectedData.codeRoomServiceProperty());
        txtRoomServiceName.textProperty().bindBidirectional(selectedData.serviceNameProperty());
        txtRoomServiceNote.textProperty().bindBidirectional(selectedData.noteProperty());

        Bindings.bindBidirectional(txtRoomServicePrice.textProperty(), selectedData.priceProperty(), new ClassFormatter.CBigDecimalStringConverter());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //txtCodeRoomService.setDisable(true);
        /*lassViewSetting.setDisableForAllInputNode(gpFormDataRoomService,
         dataInputStatus == 3,
         txtCodeRoomService); */

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataRoomServiceCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblRoomService();
        selectedData.setPrice(new BigDecimal("0"));
        lblRoomService.setText("");
        setSelectedDataToInputForm();
        //open form data room service
        dataRoomServiceFormShowStatus.set(0);
        dataRoomServiceFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataRoomServiceUpdateHandle() {
        if (tableDataRoomService.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFRoomManager().getRoomService(((TblRoomService) tableDataRoomService.getTableView().getSelectionModel().getSelectedItem()).getIdroomService());
            lblRoomService.setText(selectedData.getCodeRoomService() + " - " + selectedData.getServiceName());
            setSelectedDataToInputForm();
            //open form data roomservice
            dataRoomServiceFormShowStatus.set(0);
            dataRoomServiceFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataRoomServiceShowHandle() {
        if (tableDataRoomService.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFRoomManager().getRoomService(((TblRoomService) tableDataRoomService.getTableView().getSelectionModel().getSelectedItem()).getIdroomService());
            setSelectedDataToInputForm();
            dataRoomServiceFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataRoomServiceUnshowHandle() {
        refreshDataTableRoomService();
        dataRoomServiceFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataRoomServiceDeleteHandle() {
        if (tableDataRoomService.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFRoomManager().deleteDataRoomService((TblRoomService) tableDataRoomService.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data room service
                    refreshDataTableRoomService();
                    dataRoomServiceFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataRoomServicePrintHandle() {

    }

    private void dataRoomServiceSaveHandle() {
        if (checkDataInputDataRoomService()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblRoomService dummySelectedData = new TblRoomService(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFRoomManager().insertDataRoomService(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data room service
                            refreshDataTableRoomService();
                            dataRoomServiceFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFRoomManager().updateDataRoomService(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data room service
                            refreshDataTableRoomService();
                            dataRoomServiceFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
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

    private void dataRoomServiceCancelHandle() {
        //refresh data from table & close form data room service
        refreshDataTableRoomService();
        dataRoomServiceFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableRoomService() {
        tableDataRoomService.getTableView().setItems(loadAllDataRoomService());
        cft.refreshFilterItems(tableDataRoomService.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataRoomService() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtRoomServiceName.getText() == null || txtRoomServiceName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Layanan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomServicePrice.getText() == null
                || txtRoomServicePrice.getText().equals("")
                || txtRoomServicePrice.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Layanan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getPrice().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Layanan : Tidak boleh kurang dari '0' \n";
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
        setDataRoomServiceSplitpane();

        //init table
        initTableDataRoomService();

        //init form
        initFormDataRoomService();

        spDataRoomService.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRoomServiceFormShowStatus.set(0);
        });
    }

    public RoomServiceController(FeatureRoomAndServiceMDController parentController) {
        this.parentController = parentController;
    }

    private final FeatureRoomAndServiceMDController parentController;

}
