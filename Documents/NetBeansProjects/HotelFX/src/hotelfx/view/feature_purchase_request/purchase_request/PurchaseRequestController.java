/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_request.purchase_request;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblPurchaseRequest;
import hotelfx.persistence.model.TblPurchaseRequestDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.service.FPurchaseRequestManager;
import hotelfx.persistence.service.FPurchaseRequestManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_purchase_request.FeaturePurchaseRequestController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PurchaseRequestController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPR;

    private DoubleProperty dataPRFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPRLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPRSplitpane() {
        spDataPR.setDividerPositions(1);

        dataPRFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPRFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPR.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPR.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPRFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPRLayout.setDisable(false);
                    tableDataPRLayoutDisableLayer.setDisable(true);
                    tableDataPRLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPRLayout.setDisable(true);
                    tableDataPRLayoutDisableLayer.setDisable(false);
                    tableDataPRLayoutDisableLayer.toFront();
                }
            }
        });

        dataPRFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPRLayout;

    private ClassFilteringTable<TblPurchaseRequest> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataPR;

    private void initTableDataPR() {
        //set table
        setTableDataPR();
        //set control
        setTableControlDataPR();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPR, 15.0);
        AnchorPane.setLeftAnchor(tableDataPR, 15.0);
        AnchorPane.setRightAnchor(tableDataPR, 15.0);
        AnchorPane.setTopAnchor(tableDataPR, 15.0);
        ancBodyLayout.getChildren().add(tableDataPR);
    }

    private void setTableDataPR() {
        TableView<TblPurchaseRequest> tableView = new TableView();

        TableColumn<TblPurchaseRequest, String> codePR = new TableColumn("No. MR");
        codePR.setCellValueFactory(cellData -> cellData.getValue().codePrProperty());
        codePR.setMinWidth(120);

        TableColumn<TblPurchaseRequest, String> createdDateBy = new TableColumn<>("Buat");
        createdDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCreatedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCreatedBy() != null)
                                ? param.getValue().getTblEmployeeByCreatedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().createdDateProperty()));
        createdDateBy.setMinWidth(160);

        TableColumn<TblPurchaseRequest, String> approvedDateBy = new TableColumn<>("Persetujuan");
        approvedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getApprovedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByApprovedBy() != null)
                                ? param.getValue().getTblEmployeeByApprovedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().approvedDateProperty()));
        approvedDateBy.setMinWidth(160);

        TableColumn<TblPurchaseRequest, String> canceledDateBy = new TableColumn<>("Pembatalan");
        canceledDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCanceledDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCanceledDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCanceledBy() != null)
                                ? param.getValue().getTblEmployeeByCanceledBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().canceledDateProperty()));
        canceledDateBy.setMinWidth(160);
        
        TableColumn<TblPurchaseRequest, String> closingDateBy = new TableColumn<>("Penutupan");
        closingDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getClosingDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getClosingDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByClosingBy() != null)
                                ? param.getValue().getTblEmployeeByClosingBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().closingDateProperty()));
        closingDateBy.setMinWidth(160);

        TableColumn<TblPurchaseRequest, String> dateByTitle = new TableColumn("Tanggal-Oleh");
        dateByTitle.getColumns().addAll(createdDateBy, approvedDateBy, canceledDateBy, closingDateBy);

        TableColumn<TblPurchaseRequest, String> prStatus = new TableColumn<>("Status");
        prStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefPurchaseRequestStatus().getStatusName(), param.getValue().refPurchaseRequestStatusProperty()));
        prStatus.setMinWidth(140);

        TableColumn<TblPurchaseRequest, String> codePOs = new TableColumn("Keterangan\n(No. PO)");
        codePOs.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> getDataInformationPOs(param.getValue()),
                        param.getValue().idprProperty()));
        codePOs.setMinWidth(250);

        tableView.getColumns().addAll(codePR, dateByTitle, prStatus, codePOs);

        tableView.setItems(loadAllDataPR());

        tableView.setRowFactory(tv -> {
            TableRow<TblPurchaseRequest> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPRUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                if (checkDataPREnableToUpdate((TblPurchaseRequest) row.getItem())) {
                                    dataPRUpdateHandleDetail();
                                } else {
                                    dataPRShowHandle();
                                }
                            } else {
                                dataPRShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                if (checkDataPREnableToUpdate((TblPurchaseRequest) row.getItem())) {
//                                    dataPRUpdateHandleDetail();
//                                } else {
//                                    dataPRShowHandle();
//                                }
//                            } else {
//                                dataPRShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataPR = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblPurchaseRequest.class,
                tableDataPR.getTableView(),
                tableDataPR.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getDataInformationPOs(TblPurchaseRequest dataPR) {
        String result = "";
        if (dataPR != null) {
            List<TblPurchaseOrder> dataPOs = getService().getAllDataPurchaseOrderByIDPurchaseRequest(dataPR.getIdpr());
            for (TblPurchaseOrder dataPO : dataPOs) {
                result += dataPO.getCodePo() + " (" + dataPO.getRefPurchaseOrderStatus().getStatusName() + "), \n";
            }
            if (!result.equals("")) {
                result = result.substring(0, result.length() - 3);
            }
        }
        return result;
    }

    private void setTableControlDataPR() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataPRCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataPRUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getApproveData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Setujui");
            buttonControl.setOnMouseClicked((e) -> {
                //listener approve
                dataPRApproveHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Batal");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataPRDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tutup");
            buttonControl.setOnMouseClicked((e) -> {
                //listener closing
                dataPRCloseHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataPRPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPR.addButtonControl(buttonControls);
    }

    private ObservableList<TblPurchaseRequest> loadAllDataPR() {
        List<TblPurchaseRequest> list = getService().getAllDataPurchaseRequest();
        for (TblPurchaseRequest data : list) {
            //data pr - status
            data.setRefPurchaseRequestStatus(getService().getDataPurchaseRequestStatus(data.getRefPurchaseRequestStatus().getIdstatus()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataPR;

    @FXML
    private ScrollPane spFormDataPR;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXTextField txtCodePR;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblPurchaseRequest selectedData;

    private void initFormDataPR() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPR.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPR.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Material Request)"));
        btnSave.setOnAction((e) -> {
            dataPRSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali"));
        btnCancel.setOnAction((e) -> {
            dataPRCancelHandle();
        });

    }

    private void setSelectedDataToInputForm() {

        lblCodeData.setText(selectedData.getCodePr() != null
                ? selectedData.getCodePr() : "");

        txtCodePR.textProperty().bindBidirectional(selectedData.codePrProperty());

        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodePR.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataPR,
                dataInputStatus == 3 || dataInputStatus == 4 || dataInputStatus == 6,
                txtCodePR);

        btnSave.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    //6 = 'closing'
    private int dataInputStatus = 0;

    public void dataPRCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblPurchaseRequest();
        setSelectedDataToInputForm();
        //open form data purchase request
        dataPRFormShowStatus.set(0.0);
        dataPRFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataPRUpdateHandle() {
        if (tableDataPR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPREnableToUpdate((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem())) {
                dataPRUpdateHandleDetail();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat diubah..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataPRUpdateHandleDetail() {
        dataInputStatus = 1;
        selectedData = getService().getDataPurchaseRequest(((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem()).getIdpr());
        selectedData.setRefPurchaseRequestStatus(getService().getDataPurchaseRequestStatus(selectedData.getRefPurchaseRequestStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data purchase request
        dataPRFormShowStatus.set(0.0);
        dataPRFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPRShowHandle() {
        if (tableDataPR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getDataPurchaseRequest(((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem()).getIdpr());
            selectedData.setRefPurchaseRequestStatus(getService().getDataPurchaseRequestStatus(selectedData.getRefPurchaseRequestStatus().getIdstatus()));
            setSelectedDataToInputForm();
            dataPRFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPRUnshowHandle() {
        refreshDataTablePR();
        dataPRFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private boolean checkDataPREnableToUpdate(TblPurchaseRequest dataPR) {
        return (dataPR.getRefPurchaseRequestStatus().getIdstatus() == 0) //0 = 'Created')
                && getService().getAllDataPurchaseOrderByIDPurchaseRequest(dataPR.getIdpr()).isEmpty();
    }

    private void dataPRDeleteHandle() {
        if (tableDataPR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPREnableToDelete((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem())) {
                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membatalkan data ini?", null);
                if (alert.getResult() == ButtonType.OK) {
                    if (getService().deleteDataPurchaseRequest((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem())) {
                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dibatalkan..!!", null);
                        //refresh data from table & close form data purchase request
                        refreshDataTablePR();
                        dataPRFormShowStatus.set(0.0);
                    } else {
                        HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibatalkan..!!", getService().getErrorMessage());
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat dibatalkan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataPREnableToDelete(TblPurchaseRequest dataPR) {
        return (dataPR.getRefPurchaseRequestStatus().getIdstatus() == 0);   //0 = 'Created'
    }

    private void dataPRApproveHandle() {
        if (tableDataPR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPREnableToApprove((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem())) {
                purchaseOrderApproveHandle();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat disetujui..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void purchaseOrderApproveHandle() {
        dataInputStatus = 4;
        selectedData = getService().getDataPurchaseRequest(((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem()).getIdpr());
        selectedData.setRefPurchaseRequestStatus(getService().getDataPurchaseRequestStatus(selectedData.getRefPurchaseRequestStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data purchase order
        dataPRFormShowStatus.set(0.0);
        dataPRFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Setujui Data Material Request"));
    }
    
    private boolean checkDataPREnableToApprove(TblPurchaseRequest dataPR) {
        //0 = 'Created'
        return dataPR.getRefPurchaseRequestStatus().getIdstatus() == 0;
    }

    private void dataPRCloseHandle() {
        if (tableDataPR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPREnableToClose((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem())) {
                purchaseOrderCloseHandle();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat ditutup..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void purchaseOrderCloseHandle() {
        dataInputStatus = 6;
        selectedData = getService().getDataPurchaseRequest(((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem()).getIdpr());
        selectedData.setRefPurchaseRequestStatus(getService().getDataPurchaseRequestStatus(selectedData.getRefPurchaseRequestStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data purchase order
        dataPRFormShowStatus.set(0.0);
        dataPRFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Tutup Data Material Request"));
    }

    private boolean checkDataPREnableToClose(TblPurchaseRequest dataPR) {
        //1 = 'Approved'
        return dataPR.getRefPurchaseRequestStatus().getIdstatus() == 1; //Disetujui = '1'
    }

    private void dataPRPrintHandle() {
        if (tableDataPR.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printPR(((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printPR(TblPurchaseRequest dataPR) {
        String hotelName = "";
        SysDataHardCode sdhHotelName = getService().getDataSysDataHardCode((long) 12);  //HotelName = '12'
        if (sdhHotelName != null
                && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }
        String hotelAddress = "";
        SysDataHardCode sdhHotelAddress = getService().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
        if (sdhHotelAddress != null
                && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }
        String hotelPhoneNumber = "";
        SysDataHardCode sdhHotelPhoneNumber = getService().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
        if (sdhHotelPhoneNumber != null
                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
        }
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = getService().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
//        try {
//            ClassPrinter.printPR(hotelName,
//                    hotelAddress,
//                    hotelPhoneNumber,
//                    hotelLogoName,
//                    dataPR);
//        } catch (Throwable ex) {
//            Logger.getLogger(PurchaseRequestController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void dataPRSaveHandle() {
        if (dataInputStatus == 4) { //approve
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk menyetujui data ini?", "");
            if (alert.getResult() == ButtonType.OK) {
                if (getService().updateApproveDataPurchaseRequest((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem())) {
                    HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil disetujui..!!", null);
                    //refresh data from table & close form data purchase request
                    refreshDataTablePR();
                    dataPRFormShowStatus.set(0.0);
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal disetujui..!!", getService().getErrorMessage());
                }
            }
        } else {
            if (dataInputStatus == 6) { //closing
                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk menutup data ini?", "");
                if (alert.getResult() == ButtonType.OK) {
                    if (getService().updateClosingDataPurchaseRequest((TblPurchaseRequest) tableDataPR.getTableView().getSelectionModel().getSelectedItem())) {
                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil ditutup..!!", null);
                        //refresh data from table & close form data purchase request
                        refreshDataTablePR();
                        dataPRFormShowStatus.set(0.0);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                    } else {
                        HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal ditutup..!!", getService().getErrorMessage());
                    }
                }
            } else {
                if (checkDataInputDataPR()) {
                    Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                    if (alert.getResult() == ButtonType.OK) {
                        //dummy entry
                        TblPurchaseRequest dummySelectedData = new TblPurchaseRequest(selectedData);
                        List<TblPurchaseRequestDetail> dummyDataPurchaseRequestDetails = new ArrayList<>();
                        for (TblPurchaseRequestDetail dataPurchaseRequestDetail : (List<TblPurchaseRequestDetail>) tableDataDetail.getTableView().getItems()) {
                            TblPurchaseRequestDetail dummyDataPurchaseRequestDetail = new TblPurchaseRequestDetail(dataPurchaseRequestDetail);
                            dummyDataPurchaseRequestDetail.setTblPurchaseRequest(dummySelectedData);
                            dummyDataPurchaseRequestDetail.setTblItem(new TblItem(dummyDataPurchaseRequestDetail.getTblItem()));
                            dummyDataPurchaseRequestDetails.add(dummyDataPurchaseRequestDetail);
                        }
                        switch (dataInputStatus) {
                            case 0:
                                if (getService().insertDataPurchaseRequest(dummySelectedData,
                                        dummyDataPurchaseRequestDetails) != null) {
                                    ClassMessage.showSucceedInsertingDataMessage("", null);
                                    //refresh data from table & close form data purchase request
                                    refreshDataTablePR();
                                    dataPRFormShowStatus.set(0.0);
                                    //set unsaving data input -> 'false'
                                    ClassSession.unSavingDataInput.set(false);
                                } else {
                                    ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
                                }
                                break;
                            case 1:
                                if (getService().updateDataPurchaseRequest(dummySelectedData,
                                        dummyDataPurchaseRequestDetails)) {
                                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                                    //refresh data from table & close form data purchase request
                                    refreshDataTablePR();
                                    dataPRFormShowStatus.set(0.0);
                                    //set unsaving data input -> 'false'
                                    ClassSession.unSavingDataInput.set(false);
                                } else {
                                    ClassMessage.showFailedUpdatingDataMessage(getService().getErrorMessage(), null);
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
        }
    }

    private void dataPRCancelHandle() {
        //refresh data from table & close form data purchase request
        refreshDataTablePR();
        dataPRFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTablePR() {
        tableDataPR.getTableView().setItems(loadAllDataPR());
        cft.refreshFilterItems(tableDataPR.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataPR() {
        boolean dataInput = true;
        errDataInput = "";
        if (tableDataDetail.getTableView().getItems().isEmpty()) {
            dataInput = false;
            errDataInput += "Tabel (Detail-Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDetailLayout;

    public ClassTableWithControl tableDataDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set control
        setTableControlDataDetail();
        //set table-control to content-view
        ancDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail() {
        TableView<TblPurchaseRequestDetail> tableView = new TableView();

        TableColumn<TblPurchaseRequestDetail, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
        codeItem.setMinWidth(120);

        TableColumn<TblPurchaseRequestDetail, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblPurchaseRequestDetail, String> itemTypeHK = new TableColumn("Tipe");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + (param.getValue().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().tblItemProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<TblPurchaseRequestDetail, String> itemTypeWH = new TableColumn("Tipe");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (param.getValue().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().tblItemProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<TblPurchaseRequestDetail, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblPurchaseRequestDetail, String> prItemQuantity = new TableColumn("MR");
        prItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        prItemQuantity.setMinWidth(100);

        TableColumn<TblPurchaseRequestDetail, String> poItemQuantity = new TableColumn("PO");
        poItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getPOItemQuantity(param.getValue())),
                        param.getValue().itemQuantityProperty()));
        poItemQuantity.setMinWidth(100);

        TableColumn<TblPurchaseRequestDetail, String> percentageAllowance = new TableColumn("%allowance");
        percentageAllowance.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getAllowancePercentage(param.getValue())) + " %",
                        param.getValue().itemQuantityProperty()));
        percentageAllowance.setMinWidth(100);
        
        TableColumn<TblPurchaseRequestDetail, String> miItemQuantity = new TableColumn("Terima");
        miItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getMIItemQuantity(param.getValue())),
                        param.getValue().itemQuantityProperty()));
        miItemQuantity.setMinWidth(100);

        TableColumn<TblPurchaseRequestDetail, String> returItemQuantity = new TableColumn("Retur");
        returItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getReturItemQuantity(param.getValue())),
                        param.getValue().itemQuantityProperty()));
        returItemQuantity.setMinWidth(100);

        TableColumn<TblPurchaseRequestDetail, String> differentItemQuantity = new TableColumn("Selisih");
        differentItemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(getDifferentItemQuantity(param.getValue())),
                        param.getValue().itemQuantityProperty()));
        differentItemQuantity.setMinWidth(100);

        TableColumn<TblPurchaseRequestDetail, String> quantityTitle = new TableColumn("Jumlah Barang");
        quantityTitle.getColumns().addAll(prItemQuantity, poItemQuantity, percentageAllowance, miItemQuantity, returItemQuantity, differentItemQuantity);

        TableColumn<TblPurchaseRequestDetail, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequestDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(), param.getValue().tblItemProperty()));
        unitName.setMinWidth(120);

        if ((dataInputStatus == 3 || dataInputStatus == 4 || dataInputStatus == 6)
                && selectedData != null
                && (selectedData.getRefPurchaseRequestStatus().getIdstatus() == 1   //Disetujui = '1'
                || selectedData.getRefPurchaseRequestStatus().getIdstatus() == 4)) {  //Selesai = '4'
            tableView.getColumns().addAll(itemTypeWH, codeItem, itemName, quantityTitle, unitName);
        } else {
            tableView.getColumns().addAll(itemTypeWH, codeItem, itemName, itemQuantity, unitName);
        }

        tableView.setItems(loadAllDataPRDetail());

        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private BigDecimal getPOItemQuantity(TblPurchaseRequestDetail dataPRDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataPRDetail != null) {
            List<TblPurchaseOrderDetail> poDetails = getService().getAllDataPurchaseOrderDetailByIDPurchaseRequestAndIDItem(
                    dataPRDetail.getTblPurchaseRequest().getIdpr(),
                    dataPRDetail.getTblItem().getIditem()
            );
            for (TblPurchaseOrderDetail poDetail : poDetails) {
                if (poDetail.getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus() == 5) {   //Dipesan = '5'
                    //po quantity
                    result = result.add(poDetail.getItemQuantity());
                }
                if (poDetail.getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus() == 6) {   //Selesai = '6'
                    List<TblMemorandumInvoiceDetail> miDetails = getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(
                            poDetail.getTblPurchaseOrder().getIdpo(),
                            poDetail.getTblSupplierItem().getIdrelation()
                    );
                    for (TblMemorandumInvoiceDetail miDetail : miDetails) {
                        if (miDetail.getTblMemorandumInvoice().getRefMemorandumInvoiceStatus().getIdstatus() != 2 //Dibatalkan = '2'
                                && miDetail.getTblMemorandumInvoice().getRefMemorandumInvoiceStatus().getIdstatus() != 3) {  //Sudah Tidak Berlaku = '3'
                            //mi quantity
                            result = result.add(miDetail.getItemQuantity());
                            List<TblReturDetail> returDetails = getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(
                                    miDetail.getTblMemorandumInvoice().getIdmi(),
                                    miDetail.getTblSupplierItem().getIdrelation()
                            );
                            for (TblReturDetail returDetail : returDetails) {
                                if (returDetail.getTblRetur().getRefReturStatus().getIdstatus() == 1) {   //Disetujui = '1'
                                    //retur quantity
                                    result = result.subtract(returDetail.getItemQuantity());
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal getAllowancePercentage(TblPurchaseRequestDetail data) {
        if (data != null
                && data.getTblItem() != null) {
            SysDataHardCode sdhc = getService().getDataSysDataHardCode(28);   //ReceivingPercentageAllowance = '28'
            if (sdhc != null) {
                return new BigDecimal(sdhc.getDataHardCodeValue());
            }
        }
        return new BigDecimal("0");
    }
    
    private BigDecimal getMIItemQuantity(TblPurchaseRequestDetail dataPRDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataPRDetail != null) {
            List<TblPurchaseOrderDetail> poDetails = getService().getAllDataPurchaseOrderDetailByIDPurchaseRequestAndIDItem(
                    dataPRDetail.getTblPurchaseRequest().getIdpr(),
                    dataPRDetail.getTblItem().getIditem()
            );
            for (TblPurchaseOrderDetail poDetail : poDetails) {
                if (poDetail.getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus() == 5 //Dipesan = '5'
                        || poDetail.getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus() == 6) {   //Selesai = '6'
                    List<TblMemorandumInvoiceDetail> miDetails = getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(
                            poDetail.getTblPurchaseOrder().getIdpo(),
                            poDetail.getTblSupplierItem().getIdrelation()
                    );
                    for (TblMemorandumInvoiceDetail miDetail : miDetails) {
                        if (miDetail.getTblMemorandumInvoice().getRefMemorandumInvoiceStatus().getIdstatus() != 2 //Dibatalkan = '2'
                                && miDetail.getTblMemorandumInvoice().getRefMemorandumInvoiceStatus().getIdstatus() != 3) {  //Sudah Tidak Berlaku = '3'
                            //mi quantity
                            result = result.add(miDetail.getItemQuantity());
                        }
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal getReturItemQuantity(TblPurchaseRequestDetail dataPRDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataPRDetail != null) {
            List<TblPurchaseOrderDetail> poDetails = getService().getAllDataPurchaseOrderDetailByIDPurchaseRequestAndIDItem(
                    dataPRDetail.getTblPurchaseRequest().getIdpr(),
                    dataPRDetail.getTblItem().getIditem()
            );
            for (TblPurchaseOrderDetail poDetail : poDetails) {
                if (poDetail.getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus() == 5 //Dipesan = '5'
                        || poDetail.getTblPurchaseOrder().getRefPurchaseOrderStatus().getIdstatus() == 6) {   //Selesai = '6'
                    List<TblMemorandumInvoiceDetail> miDetails = getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(
                            poDetail.getTblPurchaseOrder().getIdpo(),
                            poDetail.getTblSupplierItem().getIdrelation()
                    );
                    for (TblMemorandumInvoiceDetail miDetail : miDetails) {
                        if (miDetail.getTblMemorandumInvoice().getRefMemorandumInvoiceStatus().getIdstatus() != 2 //Dibatalkan = '2'
                                && miDetail.getTblMemorandumInvoice().getRefMemorandumInvoiceStatus().getIdstatus() != 3) {  //Sudah Tidak Berlaku = '3'
                            List<TblReturDetail> returDetails = getService().getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(
                                    miDetail.getTblMemorandumInvoice().getIdmi(),
                                    miDetail.getTblSupplierItem().getIdrelation()
                            );
                            for (TblReturDetail returDetail : returDetails) {
                                if (returDetail.getTblRetur().getRefReturStatus().getIdstatus() == 1) {   //Disetujui = '1'
                                    //retur quantity
                                    result = result.add(returDetail.getItemQuantity());
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal getDifferentItemQuantity(TblPurchaseRequestDetail dataPRDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataPRDetail != null) {
            result = (getMIItemQuantity(dataPRDetail).subtract(getReturItemQuantity(dataPRDetail)))
                    .subtract(dataPRDetail.getItemQuantity());
        }
        return result;
    }

    private void setTableControlDataDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataDetailUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataDetail.addButtonControl(buttonControls);
    }

    private ObservableList<TblPurchaseRequestDetail> loadAllDataPRDetail() {
        ObservableList<TblPurchaseRequestDetail> list = FXCollections.observableArrayList(getService().getAllDataPurchaseRequestDetailByIDPurchaseRequest(selectedData.getIdpr()));
        for (TblPurchaseRequestDetail data : list) {
            //set data item
            data.setTblItem(getService().getDataItem(data.getTblItem().getIditem()));
            //set data unit
            data.getTblItem().setTblUnit(getService().getDataUnit(data.getTblItem().getTblUnit().getIdunit()));
            //set data item type hk
            if (data.getTblItem().getTblItemTypeHk() != null) {
                data.getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(data.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            //set data item type wh
            if (data.getTblItem().getTblItemTypeWh() != null) {
                data.getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(data.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
        }
        return list;
    }

    public TblPurchaseRequestDetail selectedDataDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailCreateHandle() {
        dataInputDetailStatus = 0;
        selectedDataDetail = new TblPurchaseRequestDetail();
        selectedDataDetail.setTblPurchaseRequest(selectedData);
        selectedDataDetail.setItemQuantity(new BigDecimal("0"));
        //open form data - detail
        showDataDetailDialog();
    }

    public void dataDetailUpdateHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputDetailStatus = 1;
            selectedDataDetail = new TblPurchaseRequestDetail();
            selectedDataDetail.setTblPurchaseRequest(selectedData);
            selectedDataDetail.setItemQuantity(((TblPurchaseRequestDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getItemQuantity());
            selectedDataDetail.setTblItem(getService().getDataItem(((TblPurchaseRequestDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblItem().getIditem()));
            selectedDataDetail.getTblItem().setTblUnit(getService().getDataUnit(selectedDataDetail.getTblItem().getTblUnit().getIdunit()));
            if (selectedDataDetail.getTblItem().getTblItemTypeHk() != null) {
                selectedDataDetail.getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(selectedDataDetail.getTblItem().getTblItemTypeHk().getIditemTypeHk()));
            }
            if (selectedDataDetail.getTblItem().getTblItemTypeWh() != null) {
                selectedDataDetail.getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(selectedDataDetail.getTblItem().getTblItemTypeWh().getIditemTypeWh()));
            }
            //open form data - detail
            showDataDetailDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataDetailDeleteHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataDetail.getTableView().getItems().remove(tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_request/purchase_request/PurchaseRequestDetailDialog.fxml"));

            PurchaseRequestDetailController controller = new PurchaseRequestDetailController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageDetal = new Stage();
            dialogStageDetal.initModality(Modality.WINDOW_MODAL);
            dialogStageDetal.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageDetal, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageDetal.initStyle(StageStyle.TRANSPARENT);
            dialogStageDetal.setScene(scene);
            dialogStageDetal.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageDetal.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FPurchaseRequestManager fPurchaseRequestManager;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        if(fPurchaseRequestManager == null){
            fPurchaseRequestManager = new FPurchaseRequestManagerImpl();
        }
        
        //set splitpane
        setDataPRSplitpane();

        //init table
        initTableDataPR();

        //init form
        initFormDataPR();

        spDataPR.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPRFormShowStatus.set(0.0);
        });
    }

    public PurchaseRequestController(){
        
    }
    
    public PurchaseRequestController(FeaturePurchaseRequestController parentController) {
        this.parentController = parentController;
        this.fPurchaseRequestManager = parentController.getFPurchaseRequestManager();
    }

    private FeaturePurchaseRequestController parentController;

    public FPurchaseRequestManager getService() {
        return fPurchaseRequestManager;
    }

}
