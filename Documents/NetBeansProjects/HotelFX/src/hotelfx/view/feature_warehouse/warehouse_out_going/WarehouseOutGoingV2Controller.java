/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_warehouse.warehouse_out_going;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblStoreRequest;
import hotelfx.persistence.model.TblStoreRequestDetail;
import hotelfx.persistence.model.TblStoreRequestDetailItemMutationHistory;
import hotelfx.persistence.service.FWarehouseManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_warehouse.FeatureWarehouseController;
import java.math.BigDecimal;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class WarehouseOutGoingV2Controller implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataOG;

    private DoubleProperty dataOGFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataOGLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataOGSplitpane() {
        spDataOG.setDividerPositions(1);

        dataOGFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataOGFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataOG.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataOG.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataOGFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataOGLayout.setDisable(false);
                    tableDataOGLayoutDisableLayer.setDisable(true);
                    tableDataOGLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataOGLayout.setDisable(true);
                    tableDataOGLayoutDisableLayer.setDisable(false);
                    tableDataOGLayoutDisableLayer.toFront();
                }
            }
        });

        dataOGFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataOGLayout;

    private ClassFilteringTable<TblStoreRequest> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataSR;

    private void initTableDataOG() {
        //set table
        setTableDataOG();
        //set control
        setTableControlDataOG();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataSR, 15.0);
        AnchorPane.setLeftAnchor(tableDataSR, 15.0);
        AnchorPane.setRightAnchor(tableDataSR, 15.0);
        AnchorPane.setTopAnchor(tableDataSR, 15.0);
        ancBodyLayout.getChildren().add(tableDataSR);
    }

    private void setTableDataOG() {
        TableView<TblStoreRequest> tableView = new TableView();

        TableColumn<TblStoreRequest, String> codeSR = new TableColumn("No. SR");
        codeSR.setCellValueFactory(cellData -> cellData.getValue().codeSrProperty());
        codeSR.setMinWidth(120);
        TableColumn<TblStoreRequest, String> createdDateBy = new TableColumn<>("Buat");
        createdDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCreatedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCreatedBy() != null)
                                ? param.getValue().getTblEmployeeByCreatedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().createdDateProperty()));
        createdDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> approvedDateBy = new TableColumn<>("Disetuji");
        approvedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getApprovedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByApprovedBy() != null)
                                ? param.getValue().getTblEmployeeByApprovedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().approvedDateProperty()));
        approvedDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> receivedDateBy = new TableColumn<>("Diterima");
        receivedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getReceivedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getReceivedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByReceivedBy() != null)
                                ? param.getValue().getTblEmployeeByReceivedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().receivedDateProperty()));
        receivedDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> canceledDateBy = new TableColumn<>("Pembatalan");
        canceledDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCanceledDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCanceledDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCanceledBy() != null)
                                ? param.getValue().getTblEmployeeByCanceledBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().canceledDateProperty()));
        canceledDateBy.setMinWidth(160);

        TableColumn<TblStoreRequest, String> dateByTitle = new TableColumn("Tanggal-Oleh");
        dateByTitle.getColumns().addAll(createdDateBy, approvedDateBy, receivedDateBy, canceledDateBy);

        TableColumn<TblStoreRequest, String> srStatus = new TableColumn<>("Status");
        srStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblStoreRequest, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefStoreRequestStatus().getStatusName(),
                        param.getValue().refStoreRequestStatusProperty()));
        srStatus.setMinWidth(140);

        tableView.getColumns().addAll(codeSR, dateByTitle, srStatus);

        tableView.setItems(loadAllDataSR());

        tableView.setRowFactory(tv -> {
            TableRow<TblStoreRequest> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataOGUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                if (checkDataOGEnableToUpdate((TblStoreRequest) row.getItem())) {
                                    dataOGUpdateHandleDetail();
                                } else {
                                    dataOGShowHandle();
                                }
                            } else {
                                dataOGShowHandle();
                            }
                        }
                    }
                }
            });
            return row;
        });

        tableDataSR = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblStoreRequest.class,
                tableDataSR.getTableView(),
                tableDataSR.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataOG() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Out Going");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataOGUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataSR.addButtonControl(buttonControls);
    }

    private ObservableList<TblStoreRequest> loadAllDataSR() {
        List<TblStoreRequest> list = parentController.getFWarehouseManager().getAllDataStoreRequest();
        //remove data not used
        for (int i = list.size() - 1; i > -1; i--) {
            //data location (source)
            list.get(i).setTblLocationByIdlocationSource(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocationByIdlocationSource().getIdlocation()));
            //data location (destination)
            list.get(i).setTblLocationByIdlocationDestination(parentController.getFWarehouseManager().getLocation(list.get(i).getTblLocationByIdlocationDestination().getIdlocation()));
            //data sr - status
            list.get(i).setRefStoreRequestStatus(parentController.getFWarehouseManager().getDataStoreRequestStatus(list.get(i).getRefStoreRequestStatus().getIdstatus()));
            if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup() != null) {
                if (list.get(i).getTblLocationByIdlocationSource().getTblGroup() == null
                        || list.get(i).getTblLocationByIdlocationSource().getTblGroup().getIdgroup() != ClassSession.currentUser.getTblEmployeeByIdemployee().getTblGroup().getIdgroup()) {
                    list.remove(i);
                }
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
    private GridPane gpFormDataOG;

    @FXML
    private ScrollPane spFormDataOG;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodeSR;

    @FXML
    private JFXTextField txtReceivedBy;

    @FXML
    private JFXTextField txtSourceLocation;

    @FXML
    private JFXTextField txtDestinationLocation;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblStoreRequest selectedData;

    private void initFormDataOG() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataOG.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataOG.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Out Going (Data Barang)"));
        btnSave.setOnAction((e) -> {
            dataOGSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataOGCancelHandle();
        });
    }

    private void setSelectedDataToInputForm() {

        lblCodeData.setText(selectedData.getCodeSr() != null
                ? selectedData.getCodeSr() : "");

        txtCodeSR.textProperty().bindBidirectional(selectedData.codeSrProperty());

        txtReceivedBy.setText(selectedData.getTblEmployeeByReceivedBy().getTblPeople().getFullName());

        txtSourceLocation.setText(getDataLocationWarehouse(selectedData.getTblLocationByIdlocationSource()));

        txtDestinationLocation.setText(getDataLocationWarehouse(selectedData.getTblLocationByIdlocationDestination()));

        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private String getDataLocationWarehouse(TblLocation tblLocation) {
        if (tblLocation != null) {
            return parentController.getFWarehouseManager().getDataWarehouseByIdLocation(tblLocation.getIdlocation()).getWarehouseName();
        }
        return "-";
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeSR.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataOG,
                dataInputStatus == 3,
                txtCodeSR);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    //do out going
    private void dataOGUpdateHandle() {
        if (tableDataSR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataOGEnableToUpdate((TblStoreRequest) tableDataSR.getTableView().getSelectionModel().getSelectedItem())) {
                dataOGUpdateHandleDetail();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak sesuai, tidak dapat melakukan 'Out Going'..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataOGUpdateHandleDetail() {
        dataInputStatus = 1;
        selectedData = parentController.getFWarehouseManager().getDataStoreRequest(((TblStoreRequest) tableDataSR.getTableView().getSelectionModel().getSelectedItem()).getIdsr());
        selectedData.setTblLocationByIdlocationSource(parentController.getFWarehouseManager().getLocation(selectedData.getTblLocationByIdlocationSource().getIdlocation()));
        selectedData.setTblLocationByIdlocationDestination(parentController.getFWarehouseManager().getLocation(selectedData.getTblLocationByIdlocationDestination().getIdlocation()));
        selectedData.setRefStoreRequestStatus(parentController.getFWarehouseManager().getDataStoreRequestStatus(selectedData.getRefStoreRequestStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data out going
        dataOGFormShowStatus.set(0.0);
        dataOGFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataOGShowHandle() {
        if (tableDataSR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFWarehouseManager().getDataStoreRequest(((TblStoreRequest) tableDataSR.getTableView().getSelectionModel().getSelectedItem()).getIdsr());
            selectedData.setTblLocationByIdlocationSource(parentController.getFWarehouseManager().getLocation(selectedData.getTblLocationByIdlocationSource().getIdlocation()));
            selectedData.setTblLocationByIdlocationDestination(parentController.getFWarehouseManager().getLocation(selectedData.getTblLocationByIdlocationDestination().getIdlocation()));
            selectedData.setRefStoreRequestStatus(parentController.getFWarehouseManager().getDataStoreRequestStatus(selectedData.getRefStoreRequestStatus().getIdstatus()));
            setSelectedDataToInputForm();
            dataOGFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataOGUnshowHandle() {
        refreshDataTableOG();
        dataOGFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private boolean checkDataOGEnableToUpdate(TblStoreRequest dataSR) {
        return (dataSR.getRefStoreRequestStatus().getIdstatus() == 0 //0 = 'Created'
                || dataSR.getRefStoreRequestStatus().getIdstatus() == 1); //1 = 'Approved'
    }

    private void printOG(TblStoreRequest dataSR) {
        String hotelName = "";
        SysDataHardCode sdhHotelName = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 12);  //HotelName = '12'
        if (sdhHotelName != null
                && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }
        String hotelAddress = "";
        SysDataHardCode sdhHotelAddress = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
        if (sdhHotelAddress != null
                && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }
        String hotelPhoneNumber = "";
        SysDataHardCode sdhHotelPhoneNumber = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
        if (sdhHotelPhoneNumber != null
                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
        }
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = parentController.getFWarehouseManager().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        try {
//            ClassPrinter.printOG(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    dataSR);
//        } catch (Throwable ex) {
//            Logger.getLogger(WarehouseStoreRequestController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void dataOGSaveHandle() {
        if (checkDataInputDataOG()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblStoreRequest dummySelectedData = new TblStoreRequest(selectedData);
                List<SRDetailItemMutationHistory> dummyDataSRDIMHs = new ArrayList<>();
                for (SRDetailItemMutationHistory srdimh : (List<SRDetailItemMutationHistory>) tableDataDetail.getItems()) {
                    SRDetailItemMutationHistory dummySRDIMH = new SRDetailItemMutationHistory(
                            new TblStoreRequestDetailItemMutationHistory(srdimh.getDataSRDIMH()),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );
                    dummySRDIMH.getDataSRDIMH().setTblStoreRequestDetail(new TblStoreRequestDetail(dummySRDIMH.getDataSRDIMH().getTblStoreRequestDetail()));
                    dummySRDIMH.getDataSRDIMH().getTblStoreRequestDetail().setTblStoreRequest(dummySelectedData);
                    dummySRDIMH.getDataSRDIMH().setTblItemMutationHistory(new TblItemMutationHistory(dummySRDIMH.getDataSRDIMH().getTblItemMutationHistory()));
                    for (IMHPropertyBarcodeSelected imhpbs : srdimh.getDataIMHPBSs()) {
                        IMHPropertyBarcodeSelected dummyIMHPBS = new IMHPropertyBarcodeSelected(
                                new TblItemMutationHistoryPropertyBarcode(imhpbs.getDataItemMutationHistoryPropertyBarcode()),
                                imhpbs.isSelected());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblItemMutationHistory(dummySRDIMH.getDataSRDIMH().getTblItemMutationHistory());
                        dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().setTblPropertyBarcode(new TblPropertyBarcode(dummyIMHPBS.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode()));
                        dummySRDIMH.getDataIMHPBSs().add(dummyIMHPBS);
                    }
                    for (TblItemMutationHistoryItemExpiredDate imhied : srdimh.getDataIMHIEDs()) {
                        if (imhied.getItemQuantity() != null
                                && imhied.getItemQuantity().compareTo(new BigDecimal("0")) == 1) {
                            TblItemMutationHistoryItemExpiredDate dummyIMHIED = new TblItemMutationHistoryItemExpiredDate(imhied);
                            dummyIMHIED.setTblItemMutationHistory(dummySRDIMH.getDataSRDIMH().getTblItemMutationHistory());
                            dummyIMHIED.setTblItemExpiredDate(new TblItemExpiredDate(dummyIMHIED.getTblItemExpiredDate()));
                            dummySRDIMH.getDataIMHIEDs().add(dummyIMHIED);
                        }
                    }
                    dummyDataSRDIMHs.add(dummySRDIMH);
                }
                switch (dataInputStatus) {
                    case 1:
                        if (parentController.getFWarehouseManager().updateApprovedOGDataStoreRequest(dummySelectedData,
                                dummyDataSRDIMHs)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data out going
                            refreshDataTableOG();
                            dataOGFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                            //pribt data out going
                            printOG(selectedData);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFWarehouseManager().getErrorMessage(), null);
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

    private void dataOGCancelHandle() {
        //refresh data from table & close form data out going
        refreshDataTableOG();
        dataOGFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableOG() {
        tableDataSR.getTableView().setItems(loadAllDataSR());
        cft.refreshFilterItems(tableDataSR.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataOG() {
        boolean dataInput = true;
        errDataInput = "";
        if (tableDataDetail.getItems().isEmpty()) {
            dataInput = false;
            errDataInput += "Tabel (Detail-Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (!checkDataInputDataOGSRDetailQuantityNeeded()) {
                dataInput = false;
                errDataInput += "Jumlah barang 'out going' yang dimasukan, \ntidak sesuai dengan jumlah barang pada 'store request' \n";
            } else {
                if (!checkDataInputDataOGSRDetailQuantityAvailable()) {
                    dataInput = false;
                    errDataInput += "Jumlah stok barang yang tersedia, \ntidak dapat memenuhi jumlah barang pada 'store request' \n";
                }
            }
        }
        return dataInput;
    }

    private boolean checkDataInputDataOGSRDetailQuantityNeeded() {
        for (SRDetailItemMutationHistory srdimh : (List<SRDetailItemMutationHistory>) tableDataDetail.getItems()) {
            if (srdimh.getDataSRDIMH().getTblStoreRequestDetail().getItemQuantity()
                    .compareTo(srdimh.getDataSRDIMH().getTblItemMutationHistory().getItemQuantity()) != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDataInputDataOGSRDetailQuantityAvailable() {
        for (SRDetailItemMutationHistory srdimh : (List<SRDetailItemMutationHistory>) tableDataDetail.getItems()) {
            if (srdimh.getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getPropertyStatus()) { //property
                for (IMHPropertyBarcodeSelected imhpbs : srdimh.getDataIMHPBSs()) {
                    if (imhpbs.isSelected()) {
                        if ((new BigDecimal("1"))
                                .compareTo(getItemLocationAvailableStockPB(
                                                imhpbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode(),
                                                srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem(),
                                                srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                }
            } else {
                if (srdimh.getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getPropertyStatus()) { //consumable
                    for (TblItemMutationHistoryItemExpiredDate imhied : srdimh.getDataIMHIEDs()) {
                        if (imhied.getItemQuantity()
                                .compareTo(getItemLocationAvailableStockIED(
                                                imhied.getTblItemExpiredDate(),
                                                srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem(),
                                                srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                                == 1) {
                            return false;
                        }
                    }
                } else {  //another
                    if (srdimh.getDataSRDIMH().getTblItemMutationHistory().getItemQuantity()
                            .compareTo(getItemLocationAvailableStock(
                                            srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem(),
                                            srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource()))
                            == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private BigDecimal getItemLocationAvailableStock(
            TblItem dataItem,
            TblLocation dataLocation) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && dataLocation != null) {
            TblItemLocation dataItemLocation = parentController.getFWarehouseManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                return dataItemLocation.getItemQuantity();
            }
        }
        return result;
    }

    private BigDecimal getItemLocationAvailableStockPB(
            TblPropertyBarcode dataPropertyBarcode,
            TblItem dataItem,
            TblLocation dataLocation) {
        if (dataPropertyBarcode != null
                && dataItem != null
                && dataLocation != null) {
            TblItemLocation dataItemLocation = parentController.getFWarehouseManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = parentController.getFWarehouseManager().getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                        dataItemLocation.getIdrelation(),
                        dataPropertyBarcode.getIdbarcode()
                );
                if (dataItemLocationPropertyBarcode != null) {
                    return new BigDecimal("1");
                }
            }
        }
        return new BigDecimal("0");
    }

    private BigDecimal getItemLocationAvailableStockIED(
            TblItemExpiredDate dataItemExpiredDate,
            TblItem dataItem,
            TblLocation dataLocation) {
        BigDecimal result = new BigDecimal("0");
        if (dataItemExpiredDate != null
                && dataItem != null
                && dataLocation != null) {
            TblItemLocation dataItemLocation = parentController.getFWarehouseManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    dataLocation.getIdlocation()
            );
            if (dataItemLocation != null) {
                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = parentController.getFWarehouseManager().getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                        dataItemLocation.getIdrelation(),
                        dataItemExpiredDate.getIditemExpiredDate()
                );
                if (dataItemLocationItemExpiredDate != null) {
                    return dataItemLocationItemExpiredDate.getItemQuantity();
                }
            }
        }
        return result;
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDetailLayout;

    public TableView tableDataDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set table-control to content-view
        ancDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail() {
        TableView<SRDetailItemMutationHistory> tableView = new TableView();
        tableView.setEditable(dataInputStatus != 3);

        TableColumn<SRDetailItemMutationHistory, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getCodeItem(),
                        param.getValue().dataSRDIMHProperty()));
        codeItem.setMinWidth(120);

        TableColumn<SRDetailItemMutationHistory, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getItemName(),
                        param.getValue().dataSRDIMHProperty()));
        itemName.setMinWidth(140);

        TableColumn<SRDetailItemMutationHistory, String> itemTypeHK = new TableColumn("House Keeping");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + (param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataSRDIMHProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<SRDetailItemMutationHistory, String> itemTypeWH = new TableColumn("Warehouse");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataSRDIMHProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<SRDetailItemMutationHistory, String> titledItemType = new TableColumn("Tipe Barang");
        titledItemType.getColumns().addAll(itemTypeHK, itemTypeWH);

        TableColumn<SRDetailItemMutationHistory, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getItemQuantity()),
                        param.getValue().dataSRDIMHProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<SRDetailItemMutationHistory, String> srItemQuantity = new TableColumn("SR");
        srItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getItemQuantity()),
                        param.getValue().dataSRDIMHProperty()));
        srItemQuantity.setMinWidth(100);

        TableColumn<SRDetailItemMutationHistory, String> ogItemQuantity = new TableColumn("OG");
        ogItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataSRDIMH().getTblItemMutationHistory().getItemQuantity()),
                        param.getValue().getDataSRDIMH().getTblItemMutationHistory().itemQuantityProperty()));
        ogItemQuantity.setMinWidth(100);

        TableColumn<SRDetailItemMutationHistory, String> detail = new TableColumn("Detail");
        detail.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> "",
                        param.getValue().dataSRDIMHProperty()));
        detail.setMinWidth(100);
        Callback<TableColumn<SRDetailItemMutationHistory, String>, TableCell<SRDetailItemMutationHistory, String>> cellFactory
                = new Callback<TableColumn<SRDetailItemMutationHistory, String>, TableCell<SRDetailItemMutationHistory, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellDetailStockOG();
                    }
                };
        detail.setCellFactory(cellFactory);

        TableColumn<SRDetailItemMutationHistory, String> titledItemQuantity = new TableColumn("Jumlah Barang");
        titledItemQuantity.getColumns().addAll(srItemQuantity, ogItemQuantity);

        TableColumn<SRDetailItemMutationHistory, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataSRDIMHProperty()));
        unitName.setMinWidth(120);

        TableColumn<SRDetailItemMutationHistory, String> itemStock = new TableColumn("Stok Tersedia");
        itemStock.setCellValueFactory((TableColumn.CellDataFeatures<SRDetailItemMutationHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getCurrentAvailableStock(param.getValue().getDataSRDIMH().getTblStoreRequestDetail().getTblItem())),
                        param.getValue().dataSRDIMHProperty()));
        itemStock.setMinWidth(100);

        if (dataInputStatus != 3) {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, titledItemQuantity, unitName, itemStock, detail);
        } else {
            tableView.getColumns().addAll(titledItemType, codeItem, itemName, itemQuantity, unitName);
        }
        tableView.setItems(loadAllDataSRDIMH());
//        tableDataDetail = new ClassTableWithControl(tableView);
        tableDataDetail = tableView;
    }

    private BigDecimal getCurrentAvailableStock(TblItem dataItem) {
        BigDecimal result = new BigDecimal("0");
        if (dataItem != null
                && selectedData != null
                && selectedData.getTblLocationByIdlocationSource() != null) {
            TblItemLocation dataItemLocation = parentController.getFWarehouseManager().getDataItemLocationByIDItemAndIDLocation(
                    dataItem.getIditem(),
                    selectedData.getTblLocationByIdlocationSource().getIdlocation());
            if (dataItemLocation != null) {
                result = dataItemLocation.getItemQuantity();
            }
        }
        return result;
    }

    class EditingCellDetailStockOG extends TableCell<SRDetailItemMutationHistory, String> {

        private JFXCComboBoxPopup cbpStockOGDetail;

        public EditingCellDetailStockOG() {

        }

        @Override
        public void startEdit() {
            super.startEdit();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (this.getTableRow() != null) {
                    Object data = this.getTableRow().getItem();
                    if (data != null
                            && (isProperty(((SRDetailItemMutationHistory) data).getDataSRDIMH().getTblStoreRequestDetail()) //property barcode
                            || isConsumable(((SRDetailItemMutationHistory) data).getDataSRDIMH().getTblStoreRequestDetail()))) {  //consumable
                        cbpStockOGDetail = ((SRDetailItemMutationHistory) data).getCbpListDetail();

                        cbpStockOGDetail.getStyleClass().add("detail-combo-box-popup");

                        cbpStockOGDetail.hide();

                        cbpStockOGDetail.setPrefSize(100, 25);
                        setAlignment(Pos.CENTER);

                        if (isProperty(((SRDetailItemMutationHistory) this.getTableRow().getItem()).getDataSRDIMH().getTblStoreRequestDetail()) //property barcode
                                || isConsumable(((SRDetailItemMutationHistory) this.getTableRow().getItem()).getDataSRDIMH().getTblStoreRequestDetail())) { //consumable
                            cbpStockOGDetail.setDisable(false);
                        } else {
                            cbpStockOGDetail.setDisable(true);
                        }

                        setText(null);
                        setGraphic(cbpStockOGDetail);

                    } else {

                        setText(null);
                        setGraphic(null);

                    }

                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        }

        private boolean isProperty(TblStoreRequestDetail dataSRDetail) {
            return dataSRDetail != null
                    && dataSRDetail.getTblItem() != null
                    && dataSRDetail.getTblItem().getPropertyStatus();   //Property
        }

        private boolean isConsumable(TblStoreRequestDetail dataSRDetail) {
            return dataSRDetail != null
                    && dataSRDetail.getTblItem() != null
                    && dataSRDetail.getTblItem().getConsumable();   //Consumable
        }

    }

    private JFXCComboBoxPopup getComboBoxDetails(TblStoreRequestDetailItemMutationHistory srdimh) {

        JFXCComboBoxPopup cbpDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-property-details");
        button.setTooltip(new Tooltip(""));
        button.setOnMouseClicked((e) -> cbpDetails.show());

        AnchorPane ancDataTableProperties = new AnchorPane();

        //layout
        AnchorPane ancContent = new AnchorPane();
        AnchorPane.setBottomAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setLeftAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setRightAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setTopAnchor(ancDataTableProperties, 0.0);
        ancContent.getChildren().add(ancDataTableProperties);

        //popup content
        BorderPane content = new BorderPane();
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(435, 360);

        content.setCenter(ancContent);

        cbpDetails.setPopupEditor(false);
        cbpDetails.promptTextProperty().set("");
        cbpDetails.setLabelFloat(false);
        cbpDetails.setPopupButton(button);
        cbpDetails.settArrowButton(true);
        cbpDetails.setPopupContent(content);

        return cbpDetails;
    }

    private JFXCComboBoxPopup getComboBoxPropertyDetails(List<IMHPropertyBarcodeSelected> IMHPBSs) {

        JFXCComboBoxPopup cbpPropertyDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-property-details");
        button.setTooltip(new Tooltip("Properti"));
        if (dataInputStatus != 3) {
            button.getStyleClass().add("button-detail-input");
        }
        button.setOnMouseClicked((e) -> cbpPropertyDetails.show());

        AnchorPane ancDataTableProperties = new AnchorPane();

        //set data table
        TableView<IMHPropertyBarcodeSelected> tablePropretyBarcode = new TableView<>();
        tablePropretyBarcode.setEditable(true);

        TableColumn<IMHPropertyBarcodeSelected, Boolean> selected = new TableColumn("Pilih");
        selected.setMinWidth(80);
        selected.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selected.setCellFactory((TableColumn<IMHPropertyBarcodeSelected, Boolean> param) -> new CheckBoxCell<>());
        selected.setEditable(dataInputStatus != 3);

        TableColumn<IMHPropertyBarcodeSelected, String> codeBarcode = new TableColumn("Barcode");
        codeBarcode.setMinWidth(120);
        codeBarcode.setCellValueFactory((TableColumn.CellDataFeatures<IMHPropertyBarcodeSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataItemMutationHistoryPropertyBarcode() == null
                        || param.getValue().getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode() == null
                                ? "" : param.getValue().getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getCodeBarcode(),
                        param.getValue().dataItemMutationHistoryPropertyBarcodeProperty()));

        tablePropretyBarcode.getColumns().addAll(codeBarcode, selected);
        tablePropretyBarcode.setItems(FXCollections.observableArrayList(IMHPBSs));

        //set to layout (data popup) 
        AnchorPane.setBottomAnchor(tablePropretyBarcode, 0.0);
        AnchorPane.setLeftAnchor(tablePropretyBarcode, 0.0);
        AnchorPane.setRightAnchor(tablePropretyBarcode, 0.0);
        AnchorPane.setTopAnchor(tablePropretyBarcode, 0.0);
        ancDataTableProperties.getChildren().add(tablePropretyBarcode);

        //layout
        AnchorPane ancContent = new AnchorPane();
        AnchorPane.setBottomAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setLeftAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setRightAnchor(ancDataTableProperties, 0.0);
        AnchorPane.setTopAnchor(ancDataTableProperties, 0.0);
        ancContent.getChildren().add(ancDataTableProperties);

        //popup content
        BorderPane content = new BorderPane();
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(220, 300);

        content.setCenter(ancContent);

        cbpPropertyDetails.setPopupEditor(false);
        cbpPropertyDetails.promptTextProperty().set("");
        cbpPropertyDetails.setLabelFloat(false);
        cbpPropertyDetails.setPopupButton(button);
        cbpPropertyDetails.settArrowButton(true);
        cbpPropertyDetails.setPopupContent(content);

        return cbpPropertyDetails;
    }

    public class CheckBoxCell<S, T> extends TableCell<S, T> {

        private final CheckBox checkBox;
        private ObservableValue<T> ov;

        public CheckBoxCell() {
            checkBox = new CheckBox();
            checkBox.setAlignment(Pos.CENTER);
            setAlignment(Pos.CENTER);
            setGraphic(checkBox);
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(checkBox);
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                }
                ov = getTableColumn().getCellObservableValue(getIndex());
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
                }
            }
        }

        @Override
        public void startEdit() {
//            super.startEdit();
        }

        @Override
        public void cancelEdit() {
//            super.cancelEdit();
        }

    }

    private JFXCComboBoxPopup getComboBoxItemExpiredDateDetails(List<TblItemMutationHistoryItemExpiredDate> imhieds) {

        JFXCComboBoxPopup cbpItemExpiredDetails = new JFXCComboBoxPopup<>(null);

        //popup button
        JFXButton button = new JFXButton("Detail");
        button.setPrefSize(70, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-property-details");
        button.setTooltip(new Tooltip("Expired Date"));
        if (dataInputStatus != 3) {
            button.getStyleClass().add("button-detail-input");
        }
        button.setOnMouseClicked((e) -> cbpItemExpiredDetails.show());

        AnchorPane ancDataTableItemExpiredDates = new AnchorPane();

        //set data table
        TableView<TblItemMutationHistoryItemExpiredDate> tableItemExpiredDate = new TableView<>();
        tableItemExpiredDate.setEditable(dataInputStatus != 3);

        TableColumn<TblItemMutationHistoryItemExpiredDate, String> expiredDate = new TableColumn("Tgl. Kadaluarsa");
        expiredDate.setMinWidth(160);
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<TblItemMutationHistoryItemExpiredDate, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblItemExpiredDate() == null
                                ? ""
                                : (param.getValue().getTblItemExpiredDate().getItemExpiredDate() == null
                                        ? ""
                                        : ClassFormatter.dateFormate.format(param.getValue().getTblItemExpiredDate().getItemExpiredDate())),
                        param.getValue().getTblItemExpiredDate().itemExpiredDateProperty()));

        TableColumn<TblItemMutationHistoryItemExpiredDate, String> quantity = new TableColumn("Jumlah (Barang)");
        quantity.setMinWidth(140);
        quantity.setCellValueFactory((TableColumn.CellDataFeatures<TblItemMutationHistoryItemExpiredDate, String> param)
                -> Bindings.createStringBinding(()
                        -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()),
                        param.getValue().itemQuantityProperty()));
        Callback<TableColumn<TblItemMutationHistoryItemExpiredDate, String>, TableCell<TblItemMutationHistoryItemExpiredDate, String>> cellFactory
                = new Callback<TableColumn<TblItemMutationHistoryItemExpiredDate, String>, TableCell<TblItemMutationHistoryItemExpiredDate, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellItemExpiredDateQuantity();
                    }
                };
        quantity.setCellFactory(cellFactory);
        quantity.setEditable(dataInputStatus != 3);

        tableItemExpiredDate.getColumns().addAll(expiredDate, quantity);
        tableItemExpiredDate.setItems(FXCollections.observableArrayList(imhieds));

        //set to layout (data popup) 
        AnchorPane.setBottomAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setLeftAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setRightAnchor(tableItemExpiredDate, 0.0);
        AnchorPane.setTopAnchor(tableItemExpiredDate, 0.0);
        ancDataTableItemExpiredDates.getChildren().add(tableItemExpiredDate);

        //layout
        AnchorPane ancContent = new AnchorPane();
        AnchorPane.setBottomAnchor(ancDataTableItemExpiredDates, 0.0);
        AnchorPane.setLeftAnchor(ancDataTableItemExpiredDates, 0.0);
        AnchorPane.setRightAnchor(ancDataTableItemExpiredDates, 0.0);
        AnchorPane.setTopAnchor(ancDataTableItemExpiredDates, 0.0);
        ancContent.getChildren().add(ancDataTableItemExpiredDates);

        //popup content
        BorderPane content = new BorderPane();
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(315, 300);

        content.setCenter(ancContent);

        cbpItemExpiredDetails.setPopupEditor(false);
        cbpItemExpiredDetails.promptTextProperty().set("");
        cbpItemExpiredDetails.setLabelFloat(false);
        cbpItemExpiredDetails.setPopupButton(button);
        cbpItemExpiredDetails.settArrowButton(true);
        cbpItemExpiredDetails.setPopupContent(content);

        return cbpItemExpiredDetails;
    }

    class EditingCellItemExpiredDateQuantity extends TableCell<TblItemMutationHistoryItemExpiredDate, String> {

        private JFXTextField tItemExpiredQuantity;

        public EditingCellItemExpiredDateQuantity() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                tItemExpiredQuantity = new JFXTextField();
                tItemExpiredQuantity.setPromptText("Jumlah Barang");

                ClassViewSetting.setImportantField(
                        tItemExpiredQuantity);

                tItemExpiredQuantity.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                ClassFormatter.setToNumericField("BigDecimal", tItemExpiredQuantity);

                Bindings.bindBidirectional(tItemExpiredQuantity.textProperty(), ((TblItemMutationHistoryItemExpiredDate) this.getTableRow().getItem()).itemQuantityProperty(), new ClassFormatter.CBigDecimalStringConverter());

                setText(null);
                setGraphic(tItemExpiredQuantity);
                tItemExpiredQuantity.selectAll();

                //cell input color
                if (this.getTableRow() != null
                        && dataInputStatus != 3) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even");
                        getStyleClass().add("cell-input-even-edit");
                    } else {
                        getStyleClass().remove("cell-input-odd");
                        getStyleClass().add("cell-input-odd-edit");
                    }
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            tItemExpiredQuantity.textProperty().unbindBidirectional(((TblItemMutationHistoryItemExpiredDate) this.getTableRow().getItem()).itemQuantityProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null
                    && dataInputStatus != 3) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit");
                    getStyleClass().add("cell-input-even");
                } else {
                    getStyleClass().remove("cell-input-odd-edit");
                    getStyleClass().add("cell-input-odd");
                }
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            setText(ClassFormatter.decimalFormat.cFormat(((TblItemMutationHistoryItemExpiredDate) data).getItemQuantity()));
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (dataInputStatus != 3) {
                            if (this.getTableRow().getIndex() % 2 == 0) {
                                getStyleClass().add("cell-input-even");

                            } else {
                                getStyleClass().add("cell-input-odd");
                            }
                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
        }

    }

    private ObservableList<SRDetailItemMutationHistory> loadAllDataSRDIMH() {
        List<SRDetailItemMutationHistory> list = new ArrayList<>();
        ObservableList<TblStoreRequestDetail> srds = FXCollections.observableArrayList(parentController.getFWarehouseManager().getAllDataStoreRequestDetailByIDStoreRequest(selectedData.getIdsr()));
        for (TblStoreRequestDetail srd : srds) {
            //set data item
            srd.setTblItem(parentController.getFWarehouseManager().getDataItem(srd.getTblItem().getIditem()));
            //set data unit
            srd.getTblItem().setTblUnit(parentController.getFWarehouseManager().getDataUnit(srd.getTblItem().getTblUnit().getIdunit()));
            //set data item type (hk)
            if (srd.getTblItem().getTblItemTypeHk() != null) {
                srd.getTblItem().setTblItemTypeHk(parentController.getFWarehouseManager().getDataItemTypeHK(srd.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type (wh)
            if (srd.getTblItem().getTblItemTypeWh() != null) {
                srd.getTblItem().setTblItemTypeWh(parentController.getFWarehouseManager().getDataItemTypeWH(srd.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
            //data srd - imh
            if (dataInputStatus != 3) {
                if (srd.getTblItem().getPropertyStatus()) {   //property
                    //data sr detail - imh
                    TblStoreRequestDetailItemMutationHistory srdimh = new TblStoreRequestDetailItemMutationHistory();
                    srdimh.setTblStoreRequestDetail(srd);
                    srdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                    srdimh.getTblItemMutationHistory().setTblItem(srd.getTblItem());
                    srdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                    srdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                    srdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                    //data imh - property (s)
                    List<TblItemMutationHistoryPropertyBarcode> imhpbs = new ArrayList<>();
                    TblItemLocation dataItemLocation = parentController.getFWarehouseManager().getDataItemLocationByIDLocationAndIDItem(
                            selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                            srd.getTblItem().getIditem()
                    );
                    List<TblItemLocationPropertyBarcode> ilpbs = parentController.getFWarehouseManager().getAllDataItemLocationPropertyBarcodeByIDItemLocation(dataItemLocation.getIdrelation());
                    for (TblItemLocationPropertyBarcode ilpb : ilpbs) {
                        TblItemMutationHistoryPropertyBarcode imhpb = new TblItemMutationHistoryPropertyBarcode();
                        imhpb.setTblItemMutationHistory(srdimh.getTblItemMutationHistory());
                        imhpb.setTblPropertyBarcode(ilpb.getTblPropertyBarcode());
                        imhpbs.add(imhpb);
                    }
                    List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                    for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                        imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, false));
                    }
                    //data 'SRDetailItemMutationHistory'
                    SRDetailItemMutationHistory data = new SRDetailItemMutationHistory(srdimh,
                            imhpbss,
                            new ArrayList<>());
                    list.add(data);
                } else {
                    if (srd.getTblItem().getConsumable()) {   //consumable
                        //data sr detail - imh
                        TblStoreRequestDetailItemMutationHistory srdimh = new TblStoreRequestDetailItemMutationHistory();
                        srdimh.setTblStoreRequestDetail(srd);
                        srdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                        srdimh.getTblItemMutationHistory().setTblItem(srd.getTblItem());
                        srdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                        srdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                        srdimh.getTblItemMutationHistory().setItemQuantity(new BigDecimal("0"));
                        //data imh - item expired date (s)
                        List<TblItemMutationHistoryItemExpiredDate> imhieds = new ArrayList<>();
                        TblItemLocation dataItemLocation = parentController.getFWarehouseManager().getDataItemLocationByIDLocationAndIDItem(
                                selectedData.getTblLocationByIdlocationSource().getIdlocation(),
                                srd.getTblItem().getIditem()
                        );
                        List<TblItemLocationItemExpiredDate> ilieds = parentController.getFWarehouseManager().getAllDataItemLocationItemExpiredDateByIDItemLocation(dataItemLocation.getIdrelation());
                        for (TblItemLocationItemExpiredDate ilied : ilieds) {
                            TblItemMutationHistoryItemExpiredDate imhied = new TblItemMutationHistoryItemExpiredDate();
                            imhied.setTblItemMutationHistory(srdimh.getTblItemMutationHistory());
                            imhied.setTblItemExpiredDate(ilied.getTblItemExpiredDate());
                            imhied.setItemQuantity(new BigDecimal("0"));
                            imhieds.add(imhied);
                        }
                        //data 'SRDetailItemMutationHistory'
                        SRDetailItemMutationHistory data = new SRDetailItemMutationHistory(srdimh,
                                new ArrayList<>(),
                                imhieds);
                        list.add(data);
                    } else {  //another
                        //data sr detail - imh
                        TblStoreRequestDetailItemMutationHistory srdimh = new TblStoreRequestDetailItemMutationHistory();
                        srdimh.setTblStoreRequestDetail(srd);
                        srdimh.setTblItemMutationHistory(new TblItemMutationHistory());
                        srdimh.getTblItemMutationHistory().setTblItem(srd.getTblItem());
                        srdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(selectedData.getTblLocationByIdlocationSource());
                        srdimh.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(selectedData.getTblLocationByIdlocationDestination());
                        srdimh.getTblItemMutationHistory().setItemQuantity(srd.getItemQuantity());
                        //data 'SRDetailItemMutationHistory'
                        SRDetailItemMutationHistory data = new SRDetailItemMutationHistory(srdimh,
                                new ArrayList<>(),
                                new ArrayList<>());
                        list.add(data);
                    }
                }
            } else {
                //data sr detail - imh
                TblStoreRequestDetailItemMutationHistory srdimh = parentController.getFWarehouseManager().getDataStoreRequestDetailItemMutationHistoryByIDStoreRequestDetail(srd.getIddetail());
                srdimh.setTblStoreRequestDetail(srd);
                srdimh.setTblItemMutationHistory(parentController.getFWarehouseManager().getDataItemMutationHistory(srdimh.getTblItemMutationHistory().getIdmutation()));
                //data imh - property (s)
                List<TblItemMutationHistoryPropertyBarcode> imhpbs = parentController.getFWarehouseManager().getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(srdimh.getTblItemMutationHistory().getIdmutation());
                List<IMHPropertyBarcodeSelected> imhpbss = new ArrayList<>();
                for (TblItemMutationHistoryPropertyBarcode imhpb : imhpbs) {
                    imhpbss.add(new IMHPropertyBarcodeSelected(imhpb, true));
                }
                //data imh - item expired date (s)
                List<TblItemMutationHistoryItemExpiredDate> imhieds = parentController.getFWarehouseManager().getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(srdimh.getTblItemMutationHistory().getIdmutation());
                //data 'SRDetailItemMutationHistory'
                SRDetailItemMutationHistory data = new SRDetailItemMutationHistory(srdimh,
                        imhpbss,
                        imhieds);
                list.add(data);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    public class SRDetailItemMutationHistory {

        private final ObjectProperty<TblStoreRequestDetailItemMutationHistory> dataSRDIMH = new SimpleObjectProperty<>();

        private List<IMHPropertyBarcodeSelected> dataIMHPBSs = new ArrayList<>();

        private List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs = new ArrayList<>();

        private final ObjectProperty<JFXCComboBoxPopup> cbpListDetail = new SimpleObjectProperty<>();

        public SRDetailItemMutationHistory(
                TblStoreRequestDetailItemMutationHistory dataSRDIMH,
                List<IMHPropertyBarcodeSelected> dataIMHPBSs,
                List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs) {
            //data srd - imh
            this.dataSRDIMH.set(dataSRDIMH);

            //data imh - pb (s)
            for (IMHPropertyBarcodeSelected dataIMHPBS : dataIMHPBSs) {
                this.dataIMHPBSs.add(dataIMHPBS);
            }

            //data imh - ied (s)
            for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                this.dataIMHIEDs.add(dataIMHIED);
            }

            //data cbListDetail
            if (this.dataSRDIMH == null
                    || this.dataSRDIMH.get().getTblStoreRequestDetail() == null
                    || this.dataSRDIMH.get().getTblStoreRequestDetail().getTblItem() == null) {
                //data cb-list combobox
                this.cbpListDetail.set(getComboBoxDetails(this.dataSRDIMH.get()));
            } else {
                if (this.dataSRDIMH.get().getTblStoreRequestDetail().getTblItem().getPropertyStatus()) {   //Property
                    //data cb-list combobox
                    this.cbpListDetail.set(getComboBoxPropertyDetails(this.dataIMHPBSs));
                } else {
                    if (this.dataSRDIMH.get().getTblStoreRequestDetail().getTblItem().getConsumable()) {    //cosumable
                        for (TblItemMutationHistoryItemExpiredDate dataIMHIED : this.dataIMHIEDs) {
                            dataIMHIED.itemQuantityProperty().addListener((obs, oldVale, newVal) -> {
                                this.dataSRDIMH.get().getTblItemMutationHistory().setItemQuantity(calculationTotalItemExpiredDateOG(
                                        this.dataIMHIEDs
                                ));
                            });
                        }
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxItemExpiredDateDetails(this.dataIMHIEDs));
                    } else {
                        //data cb-list combobox
                        this.cbpListDetail.set(getComboBoxDetails(this.dataSRDIMH.get()));
                    }
                }
            }
        }

        private BigDecimal calculationTotalItemExpiredDateOG(List<TblItemMutationHistoryItemExpiredDate> dataIMHPBSs) {
            BigDecimal result = new BigDecimal("0");
            for (TblItemMutationHistoryItemExpiredDate dataIMHPBS : dataIMHPBSs) {
                if (dataIMHPBS.getItemQuantity() != null) {
                    result = result.add(dataIMHPBS.getItemQuantity());
                }
            }
            return result;
        }

        public ObjectProperty<TblStoreRequestDetailItemMutationHistory> dataSRDIMHProperty() {
            return dataSRDIMH;
        }

        public TblStoreRequestDetailItemMutationHistory getDataSRDIMH() {
            return dataSRDIMHProperty().get();
        }

        public void setDataSRDIMH(TblStoreRequestDetailItemMutationHistory dataSRDIMH) {
            dataSRDIMHProperty().set(dataSRDIMH);
        }

        public List<IMHPropertyBarcodeSelected> getDataIMHPBSs() {
            return this.dataIMHPBSs;
        }

        public void setDataIMHPBs(List<IMHPropertyBarcodeSelected> dataIMHPBSs) {
            this.dataIMHPBSs = new ArrayList<>();
            for (IMHPropertyBarcodeSelected dataIMHPBS : dataIMHPBSs) {
                this.dataIMHPBSs.add(dataIMHPBS);
            }
        }

        public List<TblItemMutationHistoryItemExpiredDate> getDataIMHIEDs() {
            return this.dataIMHIEDs;
        }

        public void setDataIMHIEDs(List<TblItemMutationHistoryItemExpiredDate> dataIMHIEDs) {
            this.dataIMHIEDs = new ArrayList<>();
            for (TblItemMutationHistoryItemExpiredDate dataIMHIED : dataIMHIEDs) {
                this.dataIMHIEDs.add(dataIMHIED);
            }
        }

        public ObjectProperty<JFXCComboBoxPopup> cbpListDetailProperty() {
            return cbpListDetail;
        }

        public JFXCComboBoxPopup getCbpListDetail() {
            return cbpListDetailProperty().get();
        }

        public void setCbpListDetail(JFXCComboBoxPopup cbpListDetail) {
            this.cbpListDetailProperty().set(cbpListDetail);
        }

    }

    public class IMHPropertyBarcodeSelected {

        private final ObjectProperty<TblItemMutationHistoryPropertyBarcode> dataItemMutationHistoryPropertyBarcode = new SimpleObjectProperty<>();

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public IMHPropertyBarcodeSelected(TblItemMutationHistoryPropertyBarcode dataItemMutationHistoryPropertyBarcode,
                boolean selected) {
            //data item mutation history - property barcode
            this.dataItemMutationHistoryPropertyBarcode.set(dataItemMutationHistoryPropertyBarcode);
            //data selected
            this.selected.set(selected);
            //set listener to selected
            this.selected.addListener((obs, oldVal, newVal) -> {
                //refresh store request item mutation history - item quantity
                if (newVal) { //selected
                    dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().setItemQuantity(dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().getItemQuantity().add(new BigDecimal("1")));
                } else {  //unselected
                    dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().setItemQuantity(dataItemMutationHistoryPropertyBarcode.getTblItemMutationHistory().getItemQuantity().subtract(new BigDecimal("1")));
                }
            });
        }

        public ObjectProperty<TblItemMutationHistoryPropertyBarcode> dataItemMutationHistoryPropertyBarcodeProperty() {
            return dataItemMutationHistoryPropertyBarcode;
        }

        public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcode() {
            return dataItemMutationHistoryPropertyBarcodeProperty().get();
        }

        public void setDataItemMutationHistoryPropertyBarcode(TblItemMutationHistoryPropertyBarcode dataItemMutationHistoryPropertyBarcode) {
            dataItemMutationHistoryPropertyBarcodeProperty().set(dataItemMutationHistoryPropertyBarcode);
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selectedProperty().get();
        }

        public void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

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
        setDataOGSplitpane();

        //init table
        initTableDataOG();

        //init form
        initFormDataOG();

        spDataOG.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataOGFormShowStatus.set(0.0);
        });
    }

    public WarehouseOutGoingV2Controller(FeatureWarehouseController parentController) {
        this.parentController = parentController;
    }

    private final FeatureWarehouseController parentController;

    public FWarehouseManager getService() {
        return parentController.getFWarehouseManager();
    }

}
