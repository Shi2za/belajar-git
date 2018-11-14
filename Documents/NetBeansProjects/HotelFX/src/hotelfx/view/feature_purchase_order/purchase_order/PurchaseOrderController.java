/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintPurchaseOrder;
import hotelfx.helper.PrintModel.ClassPrintPurchaseOrderDetail;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblPurchaseOrderRevisionHistory;
import hotelfx.persistence.model.TblPurchaseRequest;
import hotelfx.persistence.model.TblPurchaseRequestDetail;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.model.TblUnit;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.persistence.service.FPurchaseOrderManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_purchase_order.FeaturePurchaseOrderController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
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
public class PurchaseOrderController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPO;

    private DoubleProperty dataPOFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPOLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPOSplitpane() {
        spDataPO.setDividerPositions(1);

        dataPOFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPOFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPO.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPO.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPOFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPOLayout.setDisable(false);
                    tableDataPOLayoutDisableLayer.setDisable(true);
                    tableDataPOLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPOLayout.setDisable(true);
                    tableDataPOLayoutDisableLayer.setDisable(false);
                    tableDataPOLayoutDisableLayer.toFront();
                }
            }
        });

        dataPOFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPOLayout;

    private ClassFilteringTable<TblPurchaseOrder> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataPO;

    private void initTableDataPO() {
        //set table
        setTableDataPO();
        //set control
        setTableControlDataPO();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPO, 15.0);
        AnchorPane.setLeftAnchor(tableDataPO, 15.0);
        AnchorPane.setRightAnchor(tableDataPO, 15.0);
        AnchorPane.setTopAnchor(tableDataPO, 15.0);
        ancBodyLayout.getChildren().add(tableDataPO);
    }

    private void setTableDataPO() {
        TableView<TblPurchaseOrder> tableView = new TableView();

        TableColumn<TblPurchaseOrder, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory(cellData -> cellData.getValue().codePoProperty());
        codePO.setMinWidth(100);

        TableColumn<TblPurchaseOrder, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSupplier().getSupplierName(), param.getValue().tblSupplierProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> codePR = new TableColumn("No. MR");
        codePR.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getTblPurchaseRequest()) != null
                                ? param.getValue().getTblPurchaseRequest().getCodePr()
                                : "-", param.getValue().tblPurchaseRequestProperty()));
        codePR.setMinWidth(100);

//        TableColumn<TblPurchaseOrder, String> codeRetur = new TableColumn("No. Retur");
//        codeRetur.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> (param.getValue().getTblRetur()) != null
//                                ? param.getValue().getTblRetur().getCodeRetur()
//                                : "-", param.getValue().tblReturProperty()));
//        codeRetur.setMinWidth(100);
//        TableColumn<TblPurchaseOrder, String> poDate = new TableColumn<>("Create Date");
//        poDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> (param.getValue().getPodate() != null)
//                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getPodate())
//                                : "-", param.getValue().podateProperty()));
        TableColumn<TblPurchaseOrder, String> poDueDate = new TableColumn<>("Estimasi \n Tgl. Pengiriman");
        poDueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getPodueDate() != null)
                                ? ClassFormatter.dateFormate.format(param.getValue().getPodueDate())
                                : "-", param.getValue().podueDateProperty()));
        poDueDate.setMinWidth(120);

//        TableColumn<TblPurchaseOrder, String> createdDate = new TableColumn<>("Buat");
//        createdDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> (param.getValue().getCreatedDate() != null)
//                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate())
//                                : "-", param.getValue().createdDateProperty()));
//        createdDate.setMinWidth(155);
//
//        TableColumn<TblPurchaseOrder, String> approvedDate = new TableColumn<>("Persetujuan");
//        approvedDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> (param.getValue().getApprovedDate() != null)
//                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate())
//                                : "-", param.getValue().approvedDateProperty()));
//        approvedDate.setMinWidth(155);
//
//        TableColumn<TblPurchaseOrder, String> canceledDate = new TableColumn<>("Pembatalan");
//        canceledDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(() -> (param.getValue().getCanceledDate() != null)
//                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCanceledDate())
//                                : "-", param.getValue().canceledDateProperty()));
//        canceledDate.setMinWidth(155);
//
//        TableColumn<TblPurchaseOrder, String> dateTitle = new TableColumn("Tanggal");
//        dateTitle.getColumns().addAll(createdDate, approvedDate, canceledDate);
        TableColumn<TblPurchaseOrder, String> createdDateBy = new TableColumn<>("Buat");
        createdDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCreatedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCreatedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCreatedBy() != null)
                                ? param.getValue().getTblEmployeeByCreatedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().createdDateProperty()));
        createdDateBy.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> approvedDateBy = new TableColumn<>("Persetujuan");
        approvedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getApprovedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getApprovedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByApprovedBy() != null)
                                ? param.getValue().getTblEmployeeByApprovedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().approvedDateProperty()));
        approvedDateBy.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> orderedDateBy = new TableColumn<>("Pemesanan");
        orderedDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getOrderedDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getOrderedDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByOrderedBy() != null)
                                ? param.getValue().getTblEmployeeByOrderedBy().getTblPeople().getFullName()
                                : ""),
                        param.getValue().orderedDateProperty()));
        orderedDateBy.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> canceledDateBy = new TableColumn<>("Pembatalan");
        canceledDateBy.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> ((param.getValue().getCanceledDate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getCanceledDate())
                                : "")
                        + " - \n"
                        + ((param.getValue().getTblEmployeeByCanceledBy() != null)
                                ? param.getValue().getTblEmployeeByCanceledBy().getTblPeople().getFullName()
                                : "")
                        + (param.getValue().getCanceledNote() != null
                                ? ("\n" + param.getValue().getCanceledNote())
                                : ""),
                        param.getValue().canceledDateProperty()));
        canceledDateBy.setMinWidth(140);

        TableColumn<TblPurchaseOrder, String> dateByTitle = new TableColumn("Tanggal-Oleh");
        dateByTitle.getColumns().addAll(createdDateBy, approvedDateBy, orderedDateBy, canceledDateBy);

        TableColumn<TblPurchaseOrder, String> poStatus = new TableColumn<>("Status");
//        poStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
//                -> Bindings.createStringBinding(()
//                        -> param.getValue().getRefPurchaseOrderStatus().getIdstatus() != 5 //5 = 'Dipesan'
//                                ? param.getValue().getRefPurchaseOrderStatus().getStatusName()
//                                : ((param.getValue().getRefPurchaseOrderItemArriveStatus() != null
//                                && param.getValue().getRefPurchaseOrderItemArriveStatus().getIdstatus() == 2) //Sudah  Diterima = '2'
//                                        ? "Selesai"
//                                        : param.getValue().getRefPurchaseOrderStatus().getStatusName()),
//                        param.getValue().refPurchaseOrderStatusProperty()));
        poStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefPurchaseOrderStatus().getStatusName(),
                        param.getValue().refPurchaseOrderStatusProperty()));
        poStatus.setMinWidth(90);

        TableColumn<TblPurchaseOrder, String> revisionInfo = new TableColumn<>("Keterangan (Revisi)");
        revisionInfo.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrder, String> param)
                -> Bindings.createStringBinding(() -> getDataRevisionInfo(param.getValue()),
                        param.getValue().idpoProperty()));
        revisionInfo.setMinWidth(250);

        tableView.getColumns().addAll(poStatus, codePO, supplierName, codePR, poDueDate, dateByTitle);

        tableView.setItems(loadAllDataPO());

        tableView.setRowFactory(tv -> {
            TableRow<TblPurchaseOrder> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        if (dialogStageDetal != null
                                && dialogStageDetal.isShowing()) {
                            dataPORUnshowHandle();
                        } else {
                            dataPOUnshowHandle();
                        }
                    } else {
                        if ((!row.isEmpty())) {
                            if (((TblPurchaseOrder) row.getItem()).getRefPurchaseOrderStatus().getIdstatus() == 4) {  //Revisi = '4'
                                if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                    if (checkDataPOEnableToApprove((TblPurchaseOrder) row.getItem())) {
                                        dataPORevisionHandle();
                                    } else {
                                        dataPORShowHandle();
                                    }
                                } else {
                                    dataPORShowHandle();
                                }
                            } else { //another
                                if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                    if (checkDataPOEnableToUpdate((TblPurchaseOrder) row.getItem())) {
                                        dataPOUpdateHandleDetail();
                                    } else {
                                        dataPOShowHandle();
                                    }
                                } else {
                                    dataPOShowHandle();
                                }
                            }
                        }
                    }
                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (((TblPurchaseOrder) row.getItem()).getRefPurchaseOrderStatus().getIdstatus() == 4) {  //Revisi = '4'
//                                if (DashboardController.feature.getSelectedRoleFeature().getApproveData()) {
//                                    if (checkDataPOEnableToApprove((TblPurchaseOrder) row.getItem())) {
//                                        dataPOApproveHandle();
//                                    } else {
//                                        dataPORShowHandle();
//                                    }
//                                } else {
//                                    dataPORShowHandle();
//                                }
//                            } else { //another
//                                if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                    if (checkDataPOEnableToUpdate((TblPurchaseOrder) row.getItem())) {
//                                        dataPOUpdateHandleDetail();
//                                    } else {
//                                        dataPOShowHandle();
//                                    }
//                                } else {
//                                    dataPOShowHandle();
//                                }
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataPO = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblPurchaseOrder.class,
                tableDataPO.getTableView(),
                tableDataPO.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private String getDataRevisionInfo(TblPurchaseOrder dataPO) {
        String result = "";
//        if(dataPO != null){
//            List<TblPurchaseOrderRevisionHistory> dataPORevisions = getService().getAllDataPurchaseOrderRevisionByIDPurchaseOrderSource(dataPO.getIdpo());
//            if(!dataPORevisions.isEmpty()){
//                for(TblPurchaseOrderRevisionHistory dataPORevision : dataPORevisions){
//                    
//                }
//            }
//        }
        return result.equals("") ? "-" : result;
    }

    private void setTableControlDataPO() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataPOCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataPOUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Revisi");
            buttonControl.setOnMouseClicked((e) -> {
                //listener revision
                dataPORevisionHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getApproveData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Setujui");
            buttonControl.setOnMouseClicked((e) -> {
                //listener approve
                dataPOApproveHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()
                || DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Pesan");
            buttonControl.setOnMouseClicked((e) -> {
                //listener order
                dataPOOrderHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Batal");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataPODeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tutup");
            buttonControl.setOnMouseClicked((e) -> {
                //listener closing
                dataPOCloseHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataPOPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataPO.addButtonControl(buttonControls);
    }

    private ObservableList<TblPurchaseOrder> loadAllDataPO() {
        List<TblPurchaseOrder> list = getService().getAllDataPurchaseOrder();
//        for (TblPurchaseOrder data : list) {
//            //data purchase request
//            if (data.getTblPurchaseRequest() != null) {
//                data.setTblPurchaseRequest(getService().getDataPurchaseRequest(data.getTblPurchaseRequest().getIdpr()));
//            }
//            //data retur
//            if (data.getTblRetur() != null) {
//                data.setTblRetur(getService().getDataRetur(data.getTblRetur().getIdretur()));
//            }
//            //data hotel payable
//            if (data.getTblHotelPayable() != null) {
//                data.setTblHotelPayable(getService().getDataHotelPayable(data.getTblHotelPayable().getIdhotelPayable()));
//            }
//            //data supplier
//            data.setTblSupplier(getService().getDataSupplier(data.getTblSupplier().getIdsupplier()));
//            //data po - status
//            data.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(data.getRefPurchaseOrderStatus().getIdstatus()));
//            //data po - item-arrive status
//            data.setRefPurchaseOrderItemArriveStatus(getService().getDataPurchaseOrderItemArriveStatus(data.getRefPurchaseOrderItemArriveStatus().getIdstatus()));
//            //data po - payment status
//            data.setRefPurchaseOrderPaymentStatus(getService().getDataPurchaseOrderPaymentStatus(data.getRefPurchaseOrderPaymentStatus().getIdstatus()));
//        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataPO;

    @FXML
    private ScrollPane spFormDataPO;

    @FXML
    private JFXDatePicker dtpDueDate;

    @FXML
    private JFXTextArea txtPONote;

    @FXML
    private JFXTextField txtPOPaymentTypeInformation;

    @FXML
    private AnchorPane ancPRLayout;
    private JFXCComboBoxTablePopup<TblPurchaseRequest> cbpPR;

    @FXML
    private AnchorPane ancSupplierLayout;
    private JFXCComboBoxTablePopup<TblSupplier> cbpSupplier;

    @FXML
    private Label lblPRStatus;

    @FXML
    private JFXTextField txtSubTotal;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private JFXTextField txtTax;

    @FXML
    private Spinner<Double> spnTaxPercentage;

    @FXML
    private Label lblTaxPercentage;

    @FXML
    private JFXTextField txtDeliveryCost;

//    @FXML
//    private JFXTextField txtTotal;
    @FXML
    private Label lblTotal;

    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnBack;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblPurchaseOrder selectedData;

    private void initFormDataPO() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPO.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPO.setOnScroll((ScrollEvent scroll) -> {
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

        initDataPopup();

        spnTaxPercentage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 100.00));
        spnTaxPercentage.setEditable(false);

        btnSave.setTooltip(new Tooltip("Simpan (Data Purchase Order)"));
        btnSave.setOnAction((e) -> {
            dataPOSaveHandle();
        });

        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            dataPOCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();

        initNumbericField();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpDueDate);
        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
                dtpDueDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dtpDueDate,
                cbpPR,
                cbpSupplier,
                //                txtPONote,
                txtPOPaymentTypeInformation,
                txtDiscount,
                txtTax, spnTaxPercentage, lblTaxPercentage,
                txtDeliveryCost);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtDiscount,
                txtDeliveryCost);
    }

    private void initDataPopup() {
        //Purchase Request
        TableView<TblPurchaseRequest> tablePR = new TableView<>();

        TableColumn<TblPurchaseRequest, String> codePR = new TableColumn<>("ID");
        codePR.setCellValueFactory(cellData -> cellData.getValue().codePrProperty());
        codePR.setMinWidth(120);

        TableColumn<TblPurchaseRequest, String> prDate = new TableColumn<>("Tanggal MR");
        prDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getPrdate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getPrdate())
                                : "-", param.getValue().prdateProperty()));
        prDate.setMinWidth(160);

        tablePR.getColumns().addAll(codePR, prDate);

        ObservableList<TblPurchaseRequest> prItems = FXCollections.observableArrayList(loadAllDataPR());

        cbpPR = new JFXCComboBoxTablePopup<>(
                TblPurchaseRequest.class, tablePR, prItems, "", "No. MR *", true, 300, 200
        );

        //Supplier
        TableView<TblSupplier> tableSupplier = new TableView<>();

        TableColumn<TblSupplier, String> codeSupplier = new TableColumn<>("ID");
        codeSupplier.setCellValueFactory(cellData -> cellData.getValue().codeSupplierProperty());
        codeSupplier.setMinWidth(120);

        TableColumn<TblSupplier, String> supplierName = new TableColumn<>("Supplier");
        supplierName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        supplierName.setMinWidth(140);

        tableSupplier.getColumns().addAll(codeSupplier, supplierName);

        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());

        cbpSupplier = new JFXCComboBoxTablePopup<>(
                TblSupplier.class, tableSupplier, supplierItems, "", "Supplier *", true, 280, 200
        );

        //attached to grid-pane
        ancPRLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpPR, 0.0);
        AnchorPane.setLeftAnchor(cbpPR, 0.0);
        AnchorPane.setRightAnchor(cbpPR, 0.0);
        AnchorPane.setTopAnchor(cbpPR, 0.0);
        ancPRLayout.getChildren().add(cbpPR);
        ancSupplierLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpSupplier, 0.0);
        AnchorPane.setLeftAnchor(cbpSupplier, 0.0);
        AnchorPane.setRightAnchor(cbpSupplier, 0.0);
        AnchorPane.setTopAnchor(cbpSupplier, 0.0);
        ancSupplierLayout.getChildren().add(cbpSupplier);
    }

    private ObservableList<TblPurchaseRequest> loadAllDataPR() {
        List<TblPurchaseRequest> list = getService().getAllDataPurchaseRequest();
        for (TblPurchaseRequest data : list) {
//            //data supplier
//            data.setTblSupplier(getService().getDataSupplier(data.getTblSupplier().getIdsupplier()));
            //data pr - status
            data.setRefPurchaseRequestStatus(getService().getDataPurchaseRequestStatus(data.getRefPurchaseRequestStatus().getIdstatus()));
        }
        //remove data pr with status != '1' (Approved)
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getRefPurchaseRequestStatus().getIdstatus() != 1) {
                list.remove(i);
            }
        }

        return FXCollections.observableArrayList(list);
    }

    private ObservableList<TblSupplier> loadAllDataSupplier() {
        List<TblSupplier> list = getService().getAllDataSupplier();
        for (TblSupplier data : list) {

        }
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Purchase Request
        ObservableList<TblPurchaseRequest> prItems = FXCollections.observableArrayList(loadAllDataPR());
        cbpPR.setItems(prItems);

        //Supplier
        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
        cbpSupplier.setItems(supplierItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText((selectedData.getCodePo() != null
                ? (selectedData.getCodePo()
                + (selectedData.getRefPurchaseOrderStatus() != null
                        ? (" - " + selectedData.getRefPurchaseOrderStatus().getStatusName())
                        : ""))
                : ""));

        lblPRStatus.setText("");
        lblPRStatus.setVisible(false);

        if (selectedData.getPodueDate() != null) {
            dtpDueDate.setValue(selectedData.getPodueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpDueDate.setValue(null);
        }
        dtpDueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setPodueDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });

        txtPONote.textProperty().bindBidirectional(selectedData.ponoteProperty());
        txtPOPaymentTypeInformation.textProperty().bindBidirectional(selectedData.popaymentTypeInformationProperty());

        Bindings.bindBidirectional(txtDiscount.textProperty(), selectedData.nominalDiscountProperty(), new ClassFormatter.CBigDecimalStringConverter());
        selectedData.nominalDiscountProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        spnTaxPercentage.getValueFactory().setValue(selectedData.getTaxPecentage() != null
                ? selectedData.getTaxPecentage().doubleValue() * 100
                : 0);
        spnTaxPercentage.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedData.setTaxPecentage(BigDecimal.valueOf(newVal / 100));
            }
        });
        selectedData.taxPecentageProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        Bindings.bindBidirectional(txtDeliveryCost.textProperty(), selectedData.deliveryCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        selectedData.deliveryCostProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        cbpPR.valueProperty().bindBidirectional(selectedData.tblPurchaseRequestProperty());
        cbpPR.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (checkDataPRHasBeenUsedWithAnotherPO(newVal)) {
                    lblPRStatus.setText("Data MR pernah telah digunakan sebelumnya");
                } else {
                    lblPRStatus.setText("");
                }
                refreshDataDetail(cbpSupplier.getValue(), newVal);
                refreshDataBill();
            } else {
                lblPRStatus.setText("");
            }
        });

        cbpSupplier.valueProperty().bindBidirectional(selectedData.tblSupplierProperty());
        cbpSupplier.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataDetail(newVal, cbpPR.getValue());
                refreshDataBill();
            }
        });

        cbpSupplier.hide();
        cbpPR.hide();

        initTableDataDetail();

        refreshDataBill();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        txtSubTotal.setDisable(true);
        txtTax.setDisable(true);
//        txtTotal.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataPO,
                dataInputStatus == 3 || dataInputStatus == 4 || dataInputStatus == 5,
                txtSubTotal,
                txtTax
        //                txtTotal
        );

        btnSave.setVisible(dataInputStatus != 3);
        btnBack.setDisable(false);
    }

    private boolean checkDataPRHasBeenUsedWithAnotherPO(TblPurchaseRequest dataPR) {
        boolean hasBeenUsed = false;
        List<TblPurchaseOrder> dataPO = getService().getAllDataPurchaseOrderByIDPurchaseRequest(dataPR.getIdpr());
        if (!dataPO.isEmpty()) {
            if (selectedData == null
                    || selectedData.getIdpo() == 0L) {
                hasBeenUsed = true;
            } else {
                if (dataPO.size() == 1) {
                    if (dataPO.get(0).getIdpo() != selectedData.getIdpo()) {
                        hasBeenUsed = true;
                    }
                } else {
                    hasBeenUsed = true;
                }
            }
        }
        return hasBeenUsed;
    }

    private BigDecimal calculationSubTotal() {
        BigDecimal result = new BigDecimal("0");
        if (tableDataDetail != null) {
            List<PurchaseOrderDetailCreated> list = (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems();
            for (PurchaseOrderDetailCreated data : list) {
                if (data.getCreateStatus()) {
                    result = result.add(calculationTotalCost(data.getDataPODetail()));
                }
            }
        }
        return result;
    }

    private BigDecimal calculationTax() {
        return (selectedData.getTaxPecentage()).multiply(calculationSubTotal().subtract(selectedData.getNominalDiscount()));
//        return (selectedData.getTaxPecentage()).multiply(calculationSubTotal());
    }

    private BigDecimal calculationTotal() {
        return (((new BigDecimal("1")).add(selectedData.getTaxPecentage())).multiply(calculationSubTotal().subtract(selectedData.getNominalDiscount())))
                .add(selectedData.getDeliveryCost());
//        return (((new BigDecimal("1")).add(selectedData.getTaxPecentage())).multiply(calculationSubTotal()))
//                .subtract(selectedData.getNominalDiscount())
//                .add(selectedData.getDeliveryCost());
    }

    public void refreshDataBill() {
        txtSubTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationSubTotal()));
        txtTax.setText(ClassFormatter.currencyFormat.cFormat(calculationTax()));
//        txtTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationTotal()));
        lblTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationTotal()));
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    public void dataPOCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblPurchaseOrder();
        selectedData.setNominalDiscount(new BigDecimal("0"));
        BigDecimal taxPercentage = new BigDecimal("0");
        SysDataHardCode sdhDefaultTaxPercentage = getService().getDataSysDataHardCode((long) 9);  //DefaultTaxPercentage = '9'
        if (sdhDefaultTaxPercentage != null
                && sdhDefaultTaxPercentage.getDataHardCodeValue() != null) {
            double defaultTaxPercentage = Double.parseDouble(sdhDefaultTaxPercentage.getDataHardCodeValue()) / 100;
            taxPercentage = new BigDecimal(String.valueOf(defaultTaxPercentage));
        }
        selectedData.setTaxPecentage(taxPercentage);
        selectedData.setDeliveryCost(new BigDecimal("0"));
        setSelectedDataToInputForm();
        //open form data purchase order
        dataPOFormShowStatus.set(0.0);
        dataPOFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Simpan (Data Purchase Order)"));
    }

    private void dataPOUpdateHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToUpdate((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                dataPOUpdateHandleDetail();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat diubah..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataPOUpdateHandleDetail() {
        dataInputStatus = 1;
        selectedData = getService().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        if (selectedData.getTblPurchaseRequest() != null) {
            selectedData.setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
        }
        selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        selectedData.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data purchase order
        dataPOFormShowStatus.set(0.0);
        dataPOFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Simpan (Data Purchase Order)"));
    }

    private boolean checkDataPOEnableToUpdate(TblPurchaseOrder dataPO) {
        //arrive = '0' (not yet), and payment = '0' (not paid)
        return dataPO.getTblRetur() == null //data retur not found (data PO not created by data retur)
                && (dataPO.getRefPurchaseOrderStatus().getIdstatus() == 0) //0 = 'Created'
                && dataPO.getRefPurchaseOrderItemArriveStatus().getIdstatus() == 0 //0 = 'Not Yet' (arrived)
                //                && dataPO.getRefPurchaseOrderPaymentStatus().getIdstatus() == 0;
                && (dataPO.getTblHotelPayable() == null);  //data hotel payable (po) not found
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPOShowHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = getService().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
            if (selectedData.getTblPurchaseRequest() != null) {
                selectedData.setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
            }
            selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
            selectedData.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
            setSelectedDataToInputForm();
            dataPOFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPORShowHandle() {
        dataInputDetailStatus = -1;
        //purchase order - revision history
        selectedDataRevisionHistory = getService().getDataPurchaseOrderRevisionHistoryByIDPONew(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        //selected data (source)
        selectedDataRevisionHistory.setTblPurchaseOrderByIdposource(getService().getDataPurchaseOrder(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getIdpo()));
        selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblSupplier(getService().getDataSupplier(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblSupplier().getIdsupplier()));
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblPurchaseRequest() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblPurchaseRequest().getIdpr()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblRetur() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblRetur(getService().getDataRetur(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblRetur().getIdretur()));
        }
        selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getRefPurchaseOrderStatus().getIdstatus()));
        //new
        selectedDataRevisionHistory.setTblPurchaseOrderByIdponew(getService().getDataPurchaseOrder(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getIdpo()));
        selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblSupplier(getService().getDataSupplier(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblSupplier().getIdsupplier()));
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest().getIdpr()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblRetur() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblRetur(getService().getDataRetur(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblRetur().getIdretur()));
        }
        selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getRefPurchaseOrderStatus().getIdstatus()));
        //open form data (po - approval revision)
        showDataApprovalRevisionDialog();
    }

    private void dataPOUnshowHandle() {
        refreshDataTablePO();
        dataPOFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPORUnshowHandle() {
        refreshDataTablePO();
        dialogStageDetal.close();
        isShowStatus.set(false);
    }

    private void dataPODeleteHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToDelete((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                selectedData = getService().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
                if (selectedData.getTblPurchaseRequest() != null) {
                    selectedData.setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
                }
                selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
                selectedData.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
                //show dialog canceled
                showDataCanceledDialog();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat dibatalkan" + invalidDataToCancelInfo, null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataCanceledDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order/PurchaseOrderCanceledDialog.fxml"));

            PurchaseOrderCanceledController controller = new PurchaseOrderCanceledController(this);
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

    private String invalidDataToCancelInfo;

    private boolean checkDataPOEnableToDelete(TblPurchaseOrder dataPO) {
        invalidDataToCancelInfo = "";
        if (dataPO.getRefPurchaseOrderStatus().getIdstatus() == 0 //0 = 'Created'
                || dataPO.getRefPurchaseOrderStatus().getIdstatus() == 4 //4 = 'Revisi'
                || dataPO.getRefPurchaseOrderStatus().getIdstatus() == 1) { //Disetujui = '1'
            return true;
        } else {
            if (dataPO.getRefPurchaseOrderStatus().getIdstatus() == 5) { //Dipesan = '5'
                if ((dataPO.getTblHotelPayable() == null || dataPO.getTblHotelPayable().getTblHotelInvoice() == null) //hotel payable or invoice not found
                        && dataPO.getRefPurchaseOrderItemArriveStatus().getIdstatus() == 0 //Belum Diterima = '0'
                        && dataPO.getRefPurchaseOrderPaymentStatus().getIdstatus() == 0) {  //Belum Dibayar = '0'){
                    return true;
                } else {
                    invalidDataToCancelInfo = ", \ntelah terjadi penerimaan barang atau pembayaran pada data PO..!!";
                    return false;
                }
            } else {
                invalidDataToCancelInfo = "..!!";
                return false;
            }
        }
    }

    private void dataPORevisionHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToRevision((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                if (((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getRefPurchaseOrderStatus().getIdstatus() == 1 //1 = 'Disetujui'
                        || ((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getRefPurchaseOrderStatus().getIdstatus() == 5) {   //5 = 'Dipesan'
                    if (checkDataPOEnableToRevisionWithNoAnotherRevisionIsActive((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                        if (checkDataPOEnableToRevisionWithCurrentMaxRevision((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                            dataPORevisionCreateHandleDetail();
                        } else {
                            ClassMessage.showWarningInputDataMessage("Data tidak dapat direvisi lagi, \ndata telah melebihi nilai maksimun revisi..!!", null);
                        }
                    } else {
                        ClassMessage.showWarningInputDataMessage("Terdapat data revisi yang masih aktif, \ntidak dapat membuat lebih dari 1 data revisi pada waktu yang sama..!!", null);
                    }
                } else {
                    if (((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getRefPurchaseOrderStatus().getIdstatus() == 4) {   //4 = 'Revisi'
                        dataPORevisionUpdateHandleDetail();
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat direvisi, \nhanya data dengan status 'Disetujui' atau 'Revisi' saja yang dapat direvisi..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkDataPOEnableToRevision(TblPurchaseOrder dataPO) {
        return dataPO.getRefPurchaseOrderStatus().getIdstatus() == 1 //1 = 'Disetujui'
                || dataPO.getRefPurchaseOrderStatus().getIdstatus() == 5 //5 = 'Dipesan'
                || dataPO.getRefPurchaseOrderStatus().getIdstatus() == 4;   //4 = 'Revisi'
    }

    private boolean checkDataPOEnableToRevisionWithNoAnotherRevisionIsActive(TblPurchaseOrder dataPO) {
        List<TblPurchaseOrderRevisionHistory> dataPORevisionHistories = getService().getAllDataPurchaseOrderRevisionHistoryByIDPOSource(dataPO.getIdpo());
        for (TblPurchaseOrderRevisionHistory dataPORevisionHistory : dataPORevisionHistories) {
            if (dataPORevisionHistory.getTblPurchaseOrderByIdponew().getRefPurchaseOrderStatus().getIdstatus() == 4) {    //Revisi = '4'
                return false;
            }
        }
        return true;
    }

    public boolean checkDataPOEnableToRevisionWithCurrentMaxRevision(TblPurchaseOrder dataPO) {
        String strMaxRevision = "";
        SysDataHardCode sdhPOMaxRevision = getService().getDataSysDataHardCode((long) 27);  //MaxRevisionPO = '27'
        if (sdhPOMaxRevision != null
                && sdhPOMaxRevision.getDataHardCodeValue() != null) {
            strMaxRevision = sdhPOMaxRevision.getDataHardCodeValue();
        }
        if (strMaxRevision != null
                && !strMaxRevision.equals("")) {
            int maxRevision = Integer.parseInt(strMaxRevision);
            TblPurchaseOrderRevisionHistory dataPORevisionHistory = getService().getDataPurchaseOrderRevisionHistoryByIDPONew(dataPO.getIdpo());
            List<TblPurchaseOrderRevisionHistory> list = new ArrayList<>();
            if (dataPORevisionHistory != null) {
                list.add(dataPORevisionHistory);
                setDataPORevisionPrevious(list);
            }
            return (maxRevision - list.size()) > 0;
        }
        return false;
    }

    private void setDataPORevisionPrevious(List<TblPurchaseOrderRevisionHistory> list) {
        TblPurchaseOrderRevisionHistory dataPORH = getService().getDataPurchaseOrderRevisionHistoryByIDPONew(list.get(list.size() - 1).getTblPurchaseOrderByIdposource().getIdpo());
        if (dataPORH != null) {
            list.add(dataPORH);
            setDataPORevisionPrevious(list);
        }
    }

    public TblPurchaseOrderRevisionHistory selectedDataRevisionHistory;

    private void dataPORevisionCreateHandleDetail() {
        dataInputDetailStatus = 0;
        //purchase order - revision history
        selectedDataRevisionHistory = new TblPurchaseOrderRevisionHistory();
        //source
        selectedData = getService().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        if (selectedData.getTblPurchaseRequest() != null) {
            selectedData.setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
        }
        if (selectedData.getTblRetur() != null) {
            selectedData.setTblRetur(getService().getDataRetur(selectedData.getTblRetur().getIdretur()));
        }
        if (selectedData.getTblHotelPayable() != null) {
            selectedData.setTblHotelPayable(getService().getDataHotelPayable(selectedData.getTblHotelPayable().getIdhotelPayable()));
        }
        selectedData.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
        selectedDataRevisionHistory.setTblPurchaseOrderByIdposource(selectedData);
        //new (copy from selected data (PO))
        TblPurchaseOrder selectedDataNew = new TblPurchaseOrder(selectedData);
        selectedDataNew.setIdpo(0L);
//        selectedDataNew.setCodePo(null);
        selectedDataNew.setTblSupplier(new TblSupplier(selectedDataNew.getTblSupplier()));
        if (selectedDataNew.getTblPurchaseRequest() != null) {
            selectedDataNew.setTblPurchaseRequest(new TblPurchaseRequest(selectedDataNew.getTblPurchaseRequest()));
        }
        if (selectedDataNew.getTblRetur() != null) {
            selectedDataNew.setTblRetur(new TblRetur(selectedDataNew.getTblRetur()));
        }
        selectedDataNew.setTblHotelPayable(null);
        selectedDataNew.setRefPurchaseOrderStatus(new RefPurchaseOrderStatus(selectedDataNew.getRefPurchaseOrderStatus()));
        selectedDataRevisionHistory.setTblPurchaseOrderByIdponew(selectedDataNew);
        //open form data (po - revision)
        showDataRevisionDialog();
    }

    private void dataPORevisionUpdateHandleDetail() {
        dataInputDetailStatus = 1;
        //purchase order - revision history
        selectedDataRevisionHistory = getService().getDataPurchaseOrderRevisionHistoryByIDPONew(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        //selected data (source)
        selectedDataRevisionHistory.setTblPurchaseOrderByIdposource(getService().getDataPurchaseOrder(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getIdpo()));
        selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblSupplier(getService().getDataSupplier(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblSupplier().getIdsupplier()));
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblPurchaseRequest() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblPurchaseRequest().getIdpr()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblRetur() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblRetur(getService().getDataRetur(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblRetur().getIdretur()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblHotelPayable() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblHotelPayable(getService().getDataHotelPayable(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblHotelPayable().getIdhotelPayable()));
        }
        selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getRefPurchaseOrderStatus().getIdstatus()));
        //new
        selectedDataRevisionHistory.setTblPurchaseOrderByIdponew(getService().getDataPurchaseOrder(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getIdpo()));
        selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblSupplier(getService().getDataSupplier(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblSupplier().getIdsupplier()));
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest().getIdpr()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblRetur() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblRetur(getService().getDataRetur(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblRetur().getIdretur()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblHotelPayable() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblHotelPayable(getService().getDataHotelPayable(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblHotelPayable().getIdhotelPayable()));
        }
        selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getRefPurchaseOrderStatus().getIdstatus()));
        //open form data (po - revision)
        showDataRevisionDialog();
    }

    private void showDataRevisionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order/PurchaseOrderRevisionDialog.fxml"));

            PurchaseOrderRevisionController controller = new PurchaseOrderRevisionController(this);
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

    private void dataPOApproveHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToApprove((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                if (((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getRefPurchaseOrderStatus().getIdstatus() == 0) {   //created = '1'
                    purchaseOrderApproveHandle();
                } else {  //Revisi = '4'
                    purchaseOrderApproveRevisionHandle();
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat disetujui..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void purchaseOrderApproveHandle() {
        dataInputStatus = 4;
        selectedData = getService().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        if (selectedData.getTblPurchaseRequest() != null) {
            selectedData.setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
        }
        selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        selectedData.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data purchase order
        dataPOFormShowStatus.set(0.0);
        dataPOFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Setujui (Data Purchase Order)"));
    }

    private void purchaseOrderApproveRevisionHandle() {
        dataInputDetailStatus = 1;
        //purchase order - revision history
        selectedDataRevisionHistory = getService().getDataPurchaseOrderRevisionHistoryByIDPONew(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        //selected data (source)
        selectedDataRevisionHistory.setTblPurchaseOrderByIdposource(getService().getDataPurchaseOrder(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getIdpo()));
        selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblSupplier(getService().getDataSupplier(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblSupplier().getIdsupplier()));
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblPurchaseRequest() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblPurchaseRequest().getIdpr()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblRetur() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setTblRetur(getService().getDataRetur(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblRetur().getIdretur()));
        }
        selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getRefPurchaseOrderStatus().getIdstatus()));
        //new
        selectedDataRevisionHistory.setTblPurchaseOrderByIdponew(getService().getDataPurchaseOrder(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getIdpo()));
        selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblSupplier(getService().getDataSupplier(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblSupplier().getIdsupplier()));
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest().getIdpr()));
        }
        if (selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblRetur() != null) {
            selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTblRetur(getService().getDataRetur(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblRetur().getIdretur()));
        }
        selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getRefPurchaseOrderStatus().getIdstatus()));
        //open form data (po - approval revision)
        showDataApprovalRevisionDialog();
    }

    private void showDataApprovalRevisionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order/PurchaseOrderApprovalRevisionDialog.fxml"));

            PurchaseOrderApprovalRevisionController controller = new PurchaseOrderApprovalRevisionController(this);
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

    private boolean checkDataPOEnableToApprove(TblPurchaseOrder dataPO) {
        return dataPO.getRefPurchaseOrderStatus().getIdstatus() == 0 //0 = 'Created'
                || dataPO.getRefPurchaseOrderStatus().getIdstatus() == 4;   //4 = 'Revisi'
    }

    private void dataPOOrderHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToOrder((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                purchaseOrderOrderHandle();
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat dipesan..!!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void purchaseOrderOrderHandle() {
        dataInputStatus = 5;
        selectedData = getService().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        if (selectedData.getTblPurchaseRequest() != null) {
            selectedData.setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
        }
        selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        selectedData.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
        setSelectedDataToInputForm();
        //open form data purchase order
        dataPOFormShowStatus.set(0.0);
        dataPOFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
        //set button tooltip
        btnSave.setTooltip(new Tooltip("Print PO dan Pesan Barang"));
    }

    private boolean checkDataPOEnableToOrder(TblPurchaseOrder dataPO) {
        return dataPO.getRefPurchaseOrderStatus().getIdstatus() == 1;   //1 = 'Disetujui'
    }

    private void dataPOCloseHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (checkDataPOEnableToClose((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
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
        selectedData = getService().getDataPurchaseOrder(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()).getIdpo());
        if (selectedData.getTblPurchaseRequest() != null) {
            selectedData.setTblPurchaseRequest(getService().getDataPurchaseRequest(selectedData.getTblPurchaseRequest().getIdpr()));
        }
        selectedData.setTblSupplier(getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
        selectedData.setRefPurchaseOrderStatus(getService().getDataPurchaseOrderStatus(selectedData.getRefPurchaseOrderStatus().getIdstatus()));
        //show closing dialog
        showDataClosingDialog();
    }

    private boolean checkDataPOEnableToClose(TblPurchaseOrder dataPO) {
        return dataPO.getRefPurchaseOrderStatus().getIdstatus() == 5;   //5 = 'Dipesan'
    }

    private void showDataClosingDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order/PurchaseOrderClosingDialog.fxml"));

            PurchaseOrderClosingController controller = new PurchaseOrderClosingController(this);
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

    private void dataPOPrintHandle() {
        if (tableDataPO.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printPO(((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printPO(TblPurchaseOrder dataPO) {
        List<ClassPrintPurchaseOrder> listPurchaseOrder = new ArrayList();
        SysDataHardCode dataHardCode = getService().getDataSysDataHardCode((long) 28);
        ClassPrintPurchaseOrder printPurchaseOrder = new ClassPrintPurchaseOrder();
        printPurchaseOrder.setKodePO(dataPO.getCodePo());
        printPurchaseOrder.setTanggalPO(new SimpleDateFormat("dd MMMM yyyy", new Locale("id")).format(dataPO.getPodate()));
        printPurchaseOrder.setTanggalKirim(new SimpleDateFormat("dd MMMM yyyy", new Locale("id")).format(dataPO.getPodueDate()));
        printPurchaseOrder.setNamaSupplier(dataPO.getTblSupplier().getSupplierName());
        printPurchaseOrder.setAlamatSupplier(dataPO.getTblSupplier().getSupplierAddress());
        printPurchaseOrder.setNoTeleponSupplier(dataPO.getTblSupplier().getSupplierPhoneNumber());
        printPurchaseOrder.setTipePembayaran(dataPO.getPopaymentTypeInformation());
        printPurchaseOrder.setKeterangan("No.Material Request : " + dataPO.getTblPurchaseRequest().getCodePr() + "\n"
                + "Estimasi tanggal kirim : " + printPurchaseOrder.getTanggalKirim() + "\n"
                + "Tipe Pembayaran : " + printPurchaseOrder.getTipePembayaran() + "\n"
                + "Allowance :" + ClassFormatter.decimalFormat.cFormat(new BigDecimal(dataHardCode.getDataHardCodeValue())) + "%");
        printPurchaseOrder.setNamaPembuat(dataPO.getTblEmployeeByCreateBy().getTblPeople().getFullName());
        printPurchaseOrder.setJabatanPembuat(dataPO.getTblEmployeeByCreateBy().getTblJob() == null ? "-" : dataPO.getTblEmployeeByCreateBy().getTblJob().getJobName());
        printPurchaseOrder.setDiskonTambahan(dataPO.getNominalDiscount());
        printPurchaseOrder.setPajak(dataPO.getTaxPecentage());
        printPurchaseOrder.setOngkosKirim(dataPO.getDeliveryCost());
        printPurchaseOrder.setNamaApproval(dataPO.getTblEmployeeByApprovedBy() == null ? "" : dataPO.getTblEmployeeByApprovedBy().getTblPeople().getFullName());
        printPurchaseOrder.setJabatanApproval(dataPO.getTblEmployeeByApprovedBy() == null ? "" : (dataPO.getTblEmployeeByApprovedBy().getTblJob() == null ? "-" : dataPO.getTblEmployeeByApprovedBy().getTblJob().getJobName()));
        printPurchaseOrder.setNamaPIC(dataPO.getTblSupplier().getPicname());
        printPurchaseOrder.setNoTeleponPIC(dataPO.getTblSupplier().getPicphoneNumber());

        List<ClassPrintPurchaseOrderDetail> listPurchaseOrderDetail = new ArrayList();
        List<TblPurchaseOrderDetail> list = getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(dataPO.getIdpo());
        for (TblPurchaseOrderDetail getPurchaseOrderDetail : list) {
            ClassPrintPurchaseOrderDetail purchaseOrderDetail = new ClassPrintPurchaseOrderDetail();
            purchaseOrderDetail.setDiskon(getPurchaseOrderDetail.getItemDiscount());
            purchaseOrderDetail.setHargaBarang(getPurchaseOrderDetail.getItemCost());
            purchaseOrderDetail.setJumlah(getPurchaseOrderDetail.getItemQuantity());
            purchaseOrderDetail.setKodeBarang(getPurchaseOrderDetail.getTblSupplierItem().getSupllierItemCode());
            purchaseOrderDetail.setNamaBarang(getPurchaseOrderDetail.getTblSupplierItem().getSupplierItemName());
            purchaseOrderDetail.setSatuan(getPurchaseOrderDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName());
            BigDecimal totalHarga = (purchaseOrderDetail.getHargaBarang().subtract(purchaseOrderDetail.getDiskon())).multiply(getPurchaseOrderDetail.getItemQuantity());
            purchaseOrderDetail.setTotalHarga(totalHarga);
            listPurchaseOrderDetail.add(purchaseOrderDetail);
        }

        TblPurchaseOrderRevisionHistory revisionHistory = getService().getDataPurchaseOrderRevisionHistoryByIDPONew(dataPO.getIdpo());
        String revisi = "";
        String hslRevisi = "";
        if (revisionHistory != null) {
            System.out.println("hsl :" + revisionHistory.getRevisionDate());
            // hslRevisi = (new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(revisionHistory.getRevisionDate()))+":"+revisionHistory.getRevisionNote();
            int count = 0;

            // printPurchaseOrder.setRevisiLabel(" ("+new SimpleDateFormat("dd/MM/yyyy",new Locale("id")).format(revisionHistory.getRevisionDate())+")");
            // printPurchaseOrder.setKeteranganRevisi(revisionHistory.getRevisionNote());
            List<String> listRevisi = new ArrayList();
            while (revisionHistory != null) {
                count++;
                hslRevisi = "(" + new SimpleDateFormat("dd/MM/yyyy").format(revisionHistory.getRevisionDate()) + ") : " + revisionHistory.getRevisionNote();
                revisionHistory = getService().getDataPurchaseOrderRevisionHistoryByIDPONew(revisionHistory.getTblPurchaseOrderByIdposource().getIdpo());
                listRevisi.add(hslRevisi);
            }

            System.out.println("ukuran list :" + listRevisi.size());
            for (int i = listRevisi.size() - 1; i >= 0; i--) {
                System.out.println("hsl list:" + listRevisi.get(i));
                System.out.println("hsl:" + i);
                revisi += "R" + (listRevisi.size() - i) + listRevisi.get(i) + "\n";
            }
            printPurchaseOrder.setKeteranganRevisi(revisi);
            printPurchaseOrder.setRevisiLabel("R" + count);
        } else {
            printPurchaseOrder.setKeteranganRevisi("");
            printPurchaseOrder.setRevisiLabel("");
        }
        // List<TblPurchaseOrderRevisionHistory>list = getService().getAllDataPurchaseOrderRevisionHistoryByIDPONew()
        printPurchaseOrder.setDetailPurchaseOrder(listPurchaseOrderDetail);
        listPurchaseOrder.add(printPurchaseOrder);
        ClassPrinter.printPO(listPurchaseOrder, dataPO, printPurchaseOrder.getRevisiLabel());
//        if (idPO != 0L) {
      /*  String hotelName = "";
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
         try {
         ClassPrinter.printPO(hotelName,
         hotelAddress,
         hotelPhoneNumber,
         hotelLogoName,
         dataPO);
         } catch (Throwable ex) {
         Logger.getLogger(PurchaseOrderController.class.getName()).log(Level.SEVERE, null, ex);
         } */
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
//        }
    }

    private void dataPOSaveHandle() {
        if (dataInputStatus == 4) { //approve
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk menyetujui data ini?", "");
            if (alert.getResult() == ButtonType.OK) {
                if (getService().updateApproveDataPurchaseOrder((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                    HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil disetujui..!!", null);
                    //refresh data from table & close form data purchase order
                    refreshDataTablePO();
                    dataPOFormShowStatus.set(0.0);
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal disetujui..!!", getService().getErrorMessage());
                }
            }
        } else {
            if (dataInputStatus == 5) { //order
                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk melakukan pemesanan barang?", "");
                if (alert.getResult() == ButtonType.OK) {
                    if (getService().updateOrderDataPurchaseOrder((TblPurchaseOrder) tableDataPO.getTableView().getSelectionModel().getSelectedItem())) {
                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dipesan..!!", null);
                        //refresh data from table & close form data purchase order
                        refreshDataTablePO();
                        dataPOFormShowStatus.set(0.0);
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                        //show data po, print-handle
                        printPO(selectedData);
                    } else {
                        HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dipesan..!!", getService().getErrorMessage());
                    }
                }
            } else {
                if (checkDataInputDataPO()) {
                    Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                    if (alert.getResult() == ButtonType.OK) {
                        //data po - detail
                        List<TblPurchaseOrderDetail> list = new ArrayList<>();
                        for (PurchaseOrderDetailCreated data : (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems()) {
                            if (data.getCreateStatus()) {
                                list.add(data.getDataPODetail());
                            }
                        }
                        //dummy entry
                        TblPurchaseOrder dummySelectedData = new TblPurchaseOrder(selectedData);
                        dummySelectedData.setTblSupplier(dummySelectedData.getTblSupplier());
                        if (dummySelectedData.getTblPurchaseRequest() != null) {
                            dummySelectedData.setTblPurchaseRequest(new TblPurchaseRequest(dummySelectedData.getTblPurchaseRequest()));
                        }
                        if (dummySelectedData.getTblRetur() != null) {
                            dummySelectedData.setTblRetur(new TblRetur(dummySelectedData.getTblRetur()));
                        }
                        if (dummySelectedData.getTblHotelPayable() != null) {
                            dummySelectedData.setTblHotelPayable(new TblHotelPayable(dummySelectedData.getTblHotelPayable()));
                        }
                        List<TblPurchaseOrderDetail> dummyDataPurchaseOrderDetails = new ArrayList<>();
                        for (TblPurchaseOrderDetail dataPurchaseOrderDetail : list) {
                            TblPurchaseOrderDetail dummyDataPurchaseOrderDetail = new TblPurchaseOrderDetail(dataPurchaseOrderDetail);
                            dummyDataPurchaseOrderDetail.setTblPurchaseOrder(dummySelectedData);
                            dummyDataPurchaseOrderDetail.setTblSupplierItem(new TblSupplierItem(dummyDataPurchaseOrderDetail.getTblSupplierItem()));
                            dummyDataPurchaseOrderDetail.getTblSupplierItem().setTblItem(new TblItem(dummyDataPurchaseOrderDetail.getTblSupplierItem().getTblItem()));
                            dummyDataPurchaseOrderDetails.add(dummyDataPurchaseOrderDetail);
                        }
                        switch (dataInputStatus) {
                            case 0:
                                if (getService().insertDataPurchaseOrder(dummySelectedData,
                                        dummyDataPurchaseOrderDetails) != null) {
                                    ClassMessage.showSucceedInsertingDataMessage("", null);
                                    //refresh data from table & close form data purchase order
                                    refreshDataTablePO();
                                    dataPOFormShowStatus.set(0.0);
                                    //set unsaving data input -> 'false'
                                    ClassSession.unSavingDataInput.set(false);
                                } else {
                                    ClassMessage.showFailedInsertingDataMessage(getService().getErrorMessage(), null);
                                }
                                break;
                            case 1:
                                if (getService().updateDataPurchaseOrder(dummySelectedData,
                                        dummyDataPurchaseOrderDetails)) {
                                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                                    //refresh data from table & close form data purchase order
                                    refreshDataTablePO();
                                    dataPOFormShowStatus.set(0.0);
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

    private void dataPOCancelHandle() {
        //refresh data from table & close form data purchase order
        refreshDataTablePO();
        dataPOFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTablePO() {
        tableDataPO.getTableView().setItems(loadAllDataPO());
        cft.refreshFilterItems(tableDataPO.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataPO() {
        boolean dataInput = true;
        errDataInput = "";
        if (dtpDueDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Estimasi Tanggal Pengiriman : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtPONote.getText() == null
//                || txtPONote.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Tipe Keterangan : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (txtPOPaymentTypeInformation.getText() == null
                || txtPOPaymentTypeInformation.getText().equals("")) {
            dataInput = false;
            errDataInput += "Tipe Pembayaran : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpPR.getValue() == null) {
            dataInput = false;
            errDataInput += "No. MR : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpSupplier.getValue() == null) {
            dataInput = false;
            errDataInput += "Supplier : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtDiscount.getText() == null
                || txtDiscount.getText().equals("")
                || txtDiscount.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Diskon : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getNominalDiscount().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Nominal Diskon : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (spnTaxPercentage.getValue() == null) {
            dataInput = false;
            errDataInput += "Pajak (%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtDeliveryCost.getText() == null
                || txtDeliveryCost.getText().equals("")
                || txtDeliveryCost.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Biaya Pengiriman : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getDeliveryCost().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Pengiriman : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (calculationTotal().compareTo(new BigDecimal("0")) == -1) {
            dataInput = false;
            errDataInput += "Total : Nilai tidak dapat kurang dari '0' \n";
        }
        if (!checkDataInputDataPODetail()) {
            dataInput = false;
            errDataInput += "Tabel (Detail-Barang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    public boolean checkDataInputDataPODetail() {
        boolean dataInput = false;
        for (PurchaseOrderDetailCreated data : (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems()) {
            if (data.getCreateStatus()
                    && data.getDataPODetail().getItemQuantity()
                    .compareTo(new BigDecimal("0")) == 1) {
                dataInput = true;
            }
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
        TableView<PurchaseOrderDetailCreated> tableView = new TableView();
        tableView.setEditable(dataInputStatus != 3 && dataInputStatus != 4 && dataInputStatus != 5);

        TableColumn<PurchaseOrderDetailCreated, Boolean> createStatus = new TableColumn("Pilih");
        createStatus.setCellValueFactory(cellData -> cellData.getValue().createStatusProperty());
        createStatus.setCellFactory(CheckBoxTableCell.forTableColumn(createStatus));
        createStatus.setMinWidth(70);
        createStatus.setEditable(true);

        TableColumn<PurchaseOrderDetailCreated, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getCodeItem(),
                        param.getValue().dataPODetailProperty()));
        codeItem.setMinWidth(120);

        TableColumn<PurchaseOrderDetailCreated, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getItemName(),
                        param.getValue().dataPODetailProperty()));
        itemName.setMinWidth(140);

        TableColumn<PurchaseOrderDetailCreated, String> itemTypeHK = new TableColumn("Tipe");
        itemTypeHK.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeHk() != null
                                ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeHk().getItemTypeHkname() : "-")
                        + (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataPODetailProperty()));
        itemTypeHK.setMinWidth(150);

        TableColumn<PurchaseOrderDetailCreated, String> itemTypeWH = new TableColumn("Tipe");
        itemTypeWH.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeWh() != null
                                ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblItemTypeWh().getItemTypeWhname() : "-")
                        + (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getGuestStatus() //Guest
                                ? " (G)" : " (NG)"),
                        param.getValue().dataPODetailProperty()));
        itemTypeWH.setMinWidth(150);

        TableColumn<PurchaseOrderDetailCreated, String> itemSistem = new TableColumn("(Sistem)");
        itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getItemName() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getItemName() : "-")
                        + "\n(" + (param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getCodeItem() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getCodeItem() : "-") + ")",
                        param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().iditemProperty()));
        itemSistem.setMinWidth(140);

        TableColumn<PurchaseOrderDetailCreated, String> itemSupplier = new TableColumn("(Supplier)");
        itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getDataPODetail().getTblSupplierItem().getSupplierItemName() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getSupplierItemName() : "-")
                        + "\n(" + (param.getValue().getDataPODetail().getTblSupplierItem().getSupllierItemCode() != null ? param.getValue().getDataPODetail().getTblSupplierItem().getSupllierItemCode() : "-") + ")",
                        param.getValue().getDataPODetail().getTblSupplierItem().idrelationProperty()));
        itemSupplier.setMinWidth(140);

        TableColumn<PurchaseOrderDetailCreated, String> itemTitle = new TableColumn("Barang");
        itemTitle.getColumns().addAll(itemSistem, itemSupplier);

        TableColumn<PurchaseOrderDetailCreated, String> itemCost = new TableColumn("Harga Barang");
        itemCost.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataPODetail().getItemCost()), param.getValue().dataPODetailProperty()));
        itemCost.setMinWidth(120);

        TableColumn<PurchaseOrderDetailCreated, String> itemDiscount = new TableColumn("Diskon (Satuan)");
        itemDiscount.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataPODetail().getItemDiscount()), param.getValue().dataPODetailProperty()));
        itemDiscount.setMinWidth(120);

        TableColumn<PurchaseOrderDetailCreated, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataPODetail().getItemQuantity()), param.getValue().dataPODetailProperty()));
        itemQuantity.setMinWidth(110);

        TableColumn<PurchaseOrderDetailCreated, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataPODetailProperty()));
        unitName.setMinWidth(110);

        TableColumn<PurchaseOrderDetailCreated, String> totalCost = new TableColumn("Total Harga");
        totalCost.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalCost(param.getValue().getDataPODetail())), param.getValue().dataPODetailProperty()));
        totalCost.setMinWidth(150);

        tableView.getColumns().addAll(createStatus, itemTitle, itemCost, itemDiscount, itemQuantity, unitName, totalCost);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataPODetailCreated()));
        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private BigDecimal calculationTotalCost(TblPurchaseOrderDetail dataDetail) {
        return (dataDetail.getItemCost().subtract(dataDetail.getItemDiscount())).multiply(dataDetail.getItemQuantity());
    }

    private void setTableControlDataDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataDetailCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setTooltip(new Tooltip("Ubah"));
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataDetailUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    //listener delete
//                    dataDetailDeleteHandle();
//                }
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataDetail.addButtonControl(buttonControls);
    }

    private List<PurchaseOrderDetailCreated> loadAllDataPODetailCreated() {
        List<PurchaseOrderDetailCreated> list = new ArrayList<>();
        if (dataInputStatus != 3 && dataInputStatus != 4 && dataInputStatus != 5) {
            //data po - detail : supplier - item, purchase request
            list = generateAllDataPODetailCreatedBySupplierItemAndPRDetail(selectedData.getTblSupplier(), selectedData.getTblPurchaseRequest());
            //data po - detail
            List<TblPurchaseOrderDetail> poList = getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(selectedData.getIdpo());
            for (TblPurchaseOrderDetail poData : poList) {
                //set data po
                poData.setTblPurchaseOrder(selectedData);
                //set data supplier item
                poData.setTblSupplierItem((getService().getDataSupplierItem(poData.getTblSupplierItem().getIdrelation())));
                //set data item
                poData.getTblSupplierItem().setTblItem(getService().getDataItem(poData.getTblSupplierItem().getTblItem().getIditem()));
                //set data unit
                poData.getTblSupplierItem().getTblItem().setTblUnit(getService().getDataUnit(poData.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                //set data item type hk
                if (poData.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    poData.getTblSupplierItem().getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(poData.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type wh
                if (poData.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    poData.getTblSupplierItem().getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(poData.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
            }
            for (int i = 0; i < list.size(); i++) {
                for (TblPurchaseOrderDetail poData : poList) {
                    if (list.get(i).getDataPODetail().getTblSupplierItem().getIdrelation()
                            == poData.getTblSupplierItem().getIdrelation()) {
                        //set data purchase order detail created
                        PurchaseOrderDetailCreated data = new PurchaseOrderDetailCreated();
                        data.setDataPODetail(poData);
                        data.setCreateStatus(true);
                        //set to list
                        list.set(i, data);
                        break;
                    }
                }
            }
        } else {    //just for show detail data
            //data po - detail
            List<TblPurchaseOrderDetail> poList = getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(selectedData.getIdpo());
            for (TblPurchaseOrderDetail poData : poList) {
                //set data po
                poData.setTblPurchaseOrder(selectedData);
                //set data supplier item
                poData.setTblSupplierItem((getService().getDataSupplierItem(poData.getTblSupplierItem().getIdrelation())));
                //set data item
                poData.getTblSupplierItem().setTblItem(getService().getDataItem(poData.getTblSupplierItem().getTblItem().getIditem()));
                //set data unit
                poData.getTblSupplierItem().getTblItem().setTblUnit(getService().getDataUnit(poData.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                //set data item type hk
                if (poData.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    poData.getTblSupplierItem().getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(poData.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type wh
                if (poData.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    poData.getTblSupplierItem().getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(poData.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
            }
            for (TblPurchaseOrderDetail poData : poList) {
                //set data purchase order detail created
                PurchaseOrderDetailCreated data = new PurchaseOrderDetailCreated();
                data.setDataPODetail(poData);
                data.setCreateStatus(true);
                //add to list
                list.add(data);
            }
        }
        return list;
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
            result = dataPRDetail.getItemQuantity()
                    .subtract(getMIItemQuantity(dataPRDetail).subtract(getReturItemQuantity(dataPRDetail)));
        }
        return result.compareTo(new BigDecimal("0")) == -1 ? new BigDecimal("0") : result;
    }

    private void refreshDataDetail(TblSupplier dataSupplier, TblPurchaseRequest dataPR) {
//        //refresh data supplier
//        selectedData.setTblSupplier(dataPR.getTblSupplier());
        //refresh data table detail
        tableDataDetail.getTableView().setItems(FXCollections.observableArrayList(generateAllDataPODetailCreatedBySupplierItemAndPRDetail(dataSupplier, dataPR)));
//        //reset data bill
//        selectedData.setNominalDiscount(new BigDecimal("0"));
//        spnTaxPercentage.getValueFactory().setValue(ClassDataHardcode.taxPercentage.multiply(new BigDecimal("100")));
//        selectedData.setDeliveryCost(new BigDecimal("0"));
    }

    private List<PurchaseOrderDetailCreated> generateAllDataPODetailCreatedBySupplierItemAndPRDetail(
            TblSupplier dataSupplier,
            TblPurchaseRequest dataPR) {
        List<PurchaseOrderDetailCreated> list = new ArrayList<>();
        if (dataSupplier != null) {
            //data Supplier - Item
            List<TblSupplierItem> supplierItemList = getService().getAllDataSupplierItemByIDSupplier(dataSupplier.getIdsupplier());
            for (TblSupplierItem supplierItedetailmData : supplierItemList) {
                //set data purchase order detail
                TblPurchaseOrderDetail dataDetail = new TblPurchaseOrderDetail();
                //set data purchase order
                dataDetail.setTblPurchaseOrder(selectedData);
                //set data supplier item
                dataDetail.setTblSupplierItem((getService().getDataSupplierItem(supplierItedetailmData.getIdrelation())));
                //set data item
                dataDetail.getTblSupplierItem().setTblItem(getService().getDataItem(dataDetail.getTblSupplierItem().getTblItem().getIditem()));
                //set data unit
                dataDetail.getTblSupplierItem().getTblItem().setTblUnit(getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                //set data item type hk
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type wh
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                //set cost & quantity
                dataDetail.setItemCost(supplierItedetailmData.getItemCost());
                dataDetail.setItemQuantity(new BigDecimal("0"));
                dataDetail.setItemDiscount(new BigDecimal("0"));
                //set data purchase order detail created
                PurchaseOrderDetailCreated data = new PurchaseOrderDetailCreated();
                data.setDataPODetail(dataDetail);
                data.setCreateStatus(false);
                //add data to list
                list.add(data);
            }
            //data PR - Detail
            if (dataPR != null) {
                List<TblPurchaseRequestDetail> prList = getService().getAllDataPurchaseRequestDetailByIDPurchaseRequest(dataPR.getIdpr());
                for (int i = list.size() - 1; i > -1; i--) {
                    boolean found = false;
                    for (TblPurchaseRequestDetail prData : prList) {
                        if (list.get(i).getDataPODetail().getTblSupplierItem().getTblItem().getIditem()
                                == prData.getTblItem().getIditem()) {
//                            list.get(i).getDataPODetail().setItemQuantity(prData.getItemQuantity());
                            list.get(i).getDataPODetail().setItemQuantity(getDifferentItemQuantity(prData));
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        list.remove(i);
                    }
                }
            }
        } else {
            //data PR - Detail
            if (dataPR != null) {
                List<TblPurchaseRequestDetail> prList = getService().getAllDataPurchaseRequestDetailByIDPurchaseRequest(dataPR.getIdpr());
                for (TblPurchaseRequestDetail prData : prList) {
                    //set data purchase order detail
                    TblPurchaseOrderDetail dataDetail = new TblPurchaseOrderDetail();
                    //set data purchase order
                    dataDetail.setTblPurchaseOrder(selectedData);
                    //set data supplier item
                    dataDetail.setTblSupplierItem(new TblSupplierItem());
                    //set data item
                    dataDetail.getTblSupplierItem().setTblItem(getService().getDataItem(prData.getTblItem().getIditem()));
                    //set data unit
                    dataDetail.getTblSupplierItem().getTblItem().setTblUnit(getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                    //set data item type hk
                    if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                        dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(getService().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    //set data item type wh
                    if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                        dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(getService().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                    //set cost & quantity
                    dataDetail.setItemCost(new BigDecimal("0"));
//                    dataDetail.setItemQuantity(prData.getItemQuantity());
                    dataDetail.setItemQuantity(getDifferentItemQuantity(prData));
                    dataDetail.setItemDiscount(new BigDecimal("0"));
                    //set data purchase order detail created
                    PurchaseOrderDetailCreated data = new PurchaseOrderDetailCreated();
                    data.setDataPODetail(dataDetail);
                    data.setCreateStatus(false);
                    //add data to list
                    list.add(data);
                }
            }
        }
        return list;
    }

    public class PurchaseOrderDetailCreated {

        private final ObjectProperty<TblPurchaseOrderDetail> dataPODteail = new SimpleObjectProperty<>();

        private final BooleanProperty createStatus = new SimpleBooleanProperty();

        public PurchaseOrderDetailCreated() {

            createStatus.addListener((obs, oldVal, newVal) -> {
                refreshDataBill();
            });

        }

        public ObjectProperty<TblPurchaseOrderDetail> dataPODetailProperty() {
            return dataPODteail;
        }

        public TblPurchaseOrderDetail getDataPODetail() {
            return dataPODetailProperty().get();
        }

        public void setDataPODetail(TblPurchaseOrderDetail dataPODetail) {
            dataPODetailProperty().set(dataPODetail);
        }

        public BooleanProperty createStatusProperty() {
            return createStatus;
        }

        public boolean getCreateStatus() {
            return createStatusProperty().get();
        }

        public void setCreateStatus(boolean createStatus) {
            createStatusProperty().set(createStatus);
        }

    }

    public TblPurchaseOrderDetail selectedDataDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailCreateHandle() {
        dataInputDetailStatus = 0;
        selectedDataDetail = new TblPurchaseOrderDetail();
        selectedDataDetail.setTblPurchaseOrder(selectedData);
        selectedDataDetail.setItemCost(new BigDecimal("0"));
        selectedDataDetail.setItemQuantity(new BigDecimal("0"));
        selectedDataDetail.setItemDiscount(new BigDecimal("0"));
        //open form data - detail
        showDataDetailDialog();
    }

    public void dataDetailUpdateHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputDetailStatus = 1;
            selectedDataDetail = new TblPurchaseOrderDetail(((PurchaseOrderDetailCreated) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getDataPODetail());
            selectedDataDetail.setTblPurchaseOrder(selectedData);
//            selectedDataDetail.setItemCost(((TblPurchaseOrderDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getItemCost());
//            selectedDataDetail.setItemDiscount(((TblPurchaseOrderDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getItemDiscount());
//            selectedDataDetail.setItemQuantity(((TblPurchaseOrderDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getItemQuantity());
//            selectedDataDetail.setTblItem(getService().getDataItem(((TblPurchaseOrderDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getTblItem().getIditem()));
//            selectedDataDetail.getTblItem().setTblUnit(getService().getDataUnit(selectedDataDetail.getTblItem().getTblUnit().getIdunit()));
            selectedDataDetail.setTblSupplierItem(new TblSupplierItem(selectedDataDetail.getTblSupplierItem()));
            selectedDataDetail.getTblSupplierItem().setTblItem(new TblItem(selectedDataDetail.getTblSupplierItem().getTblItem()));
            selectedDataDetail.getTblSupplierItem().getTblItem().setTblUnit(new TblUnit(selectedDataDetail.getTblSupplierItem().getTblItem().getTblUnit()));
            if (selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                selectedDataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(new TblItemTypeHk(selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk()));
            }
            if (selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                selectedDataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(new TblItemTypeWh(selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh()));
            }
//            selectedDataDetail = (TblPurchaseOrderDetail) tableDataDetail.getTableView().getSelectionModel().getSelectedItem();
            //open form data - detail
            showDataDetailDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataDetailDeleteHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            ClassMessage.showSucceedRemovingDataMessage("", null);
            //remove data from table items list
            tableDataDetail.getTableView().getItems().remove(tableDataDetail.getTableView().getSelectionModel().getSelectedItem());
            //refresh bill
            refreshDataBill();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order/PurchaseOrderDetailDialog.fxml"));

            PurchaseOrderDetailController controller = new PurchaseOrderDetailController(this);
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
    private FPurchaseOrderManager fPurchaseOrderManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        if (fPurchaseOrderManager == null) {
            fPurchaseOrderManager = new FPurchaseOrderManagerImpl();
        }

        //set splitpane
        setDataPOSplitpane();

        //init table
        initTableDataPO();

        //init form
        initFormDataPO();

        spDataPO.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPOFormShowStatus.set(0.0);
        });
    }

    public PurchaseOrderController() {

    }

    public PurchaseOrderController(FeaturePurchaseOrderController parentController) {
        this.parentController = parentController;
        this.fPurchaseOrderManager = parentController.getFPurchaseOrderManager();
    }

    private FeaturePurchaseOrderController parentController;

    public FPurchaseOrderManager getService() {
        return fPurchaseOrderManager;
    }

}
