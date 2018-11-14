/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_payment_x;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FReturManager;
import hotelfx.view.DashboardController;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReturPaymentInvoiceController implements Initializable {

//    /**
//     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
//     */
//    @FXML
//    private SplitPane spDataInvoice;
//
//    private DoubleProperty dataInvoiceFormShowStatus;
//
//    @FXML
//    private AnchorPane contentLayout;
//
//    @FXML
//    private AnchorPane tableDataInvoiceLayoutDisableLayer;
//
//    private boolean isTimeLinePlaying = false;
//
//    private void setDataInvoiceSplitpane() {
//        spDataInvoice.setDividerPositions(1);
//
//        dataInvoiceFormShowStatus = new SimpleDoubleProperty(1.0);
//
//        DoubleProperty divPosition = new SimpleDoubleProperty();
//
//        divPosition.bind(new SimpleDoubleProperty(1.0)
//                .subtract(dataInvoiceFormShowStatus)
//        );
//
//        divPosition.addListener((obs, oldVal, newVal) -> {
//            isTimeLinePlaying = true;
//            KeyValue keyValue = new KeyValue(spDataInvoice.getDividers().get(0).positionProperty(), newVal);
//            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
//            timeline.play();
//            timeline.setOnFinished((e) -> {
//                isTimeLinePlaying = false;
//            });
//        });
//
//        SplitPane.Divider div = spDataInvoice.getDividers().get(0);
//        div.positionProperty().addListener((obs, oldValue, newValue) -> {
//            if (!isTimeLinePlaying) {
//                div.setPosition(divPosition.get());
//            }
//        });
//
//        dataInvoiceFormShowStatus.addListener((obs, oldVal, newVal) -> {
//            if (dataInputStatus != 3) {
//                if (newVal.doubleValue() == 0.0) {    //enable
//                    tableDataInvoiceLayout.setDisable(false);
//                    tableDataInvoiceLayoutDisableLayer.setDisable(true);
//                    tableDataInvoiceLayout.toFront();
//                }
//                if (newVal.doubleValue() == 1) {  //disable
//                    tableDataInvoiceLayout.setDisable(true);
//                    tableDataInvoiceLayoutDisableLayer.setDisable(false);
//                    tableDataInvoiceLayoutDisableLayer.toFront();
//                }
//            }
//        });
//
//        dataInvoiceFormShowStatus.set(0.0);
//    }
//
//    /**
//     * TABLE DATA
//     */
//    @FXML
//    private AnchorPane tableDataInvoiceLayout;
//
//    private ClassTableWithControl tableDataInvoice;
//
//    private void initTableDataInvoice() {
//        //set table
//        setTableDataInvoice();
//        //set control
//        setTableControlDataInvoice();
//        //set table-control to content-view
//        tableDataInvoiceLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(tableDataInvoice, 15.0);
//        AnchorPane.setLeftAnchor(tableDataInvoice, 15.0);
//        AnchorPane.setRightAnchor(tableDataInvoice, 15.0);
//        AnchorPane.setTopAnchor(tableDataInvoice, 15.0);
//        tableDataInvoiceLayout.getChildren().add(tableDataInvoice);
//    }
//
//    private void setTableDataInvoice() {
//        TableView<TblHotelInvoice> tableView = new TableView();
//
//        TableColumn<TblHotelInvoice, String> codeInvoice = new TableColumn("No. Invoice");
//        codeInvoice.setCellValueFactory(cellData -> cellData.getValue().codeHotelInvoiceProperty());
//        codeInvoice.setMinWidth(120);
//
//        TableColumn<TblHotelInvoice, String> subject = new TableColumn("Subject");
//        subject.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelInvoice, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getHotelInvoiceSubject(),
//                        param.getValue().hotelInvoiceSubjectProperty()));
//        subject.setMinWidth(140);
//
//        TableColumn<TblHotelInvoice, String> issueDate = new TableColumn<>("Tanggal Buat");
//        issueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelInvoice, String> param)
//                -> Bindings.createStringBinding(() -> (param.getValue().getIssueDate() != null)
//                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getIssueDate())
//                                : "-", param.getValue().issueDateProperty()));
//        issueDate.setMinWidth(160);
//
//        TableColumn<TblHotelInvoice, String> dueDate = new TableColumn<>("Tanggal Jatuh Tempo");
//        dueDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelInvoice, String> param)
//                -> Bindings.createStringBinding(() -> (param.getValue().getDueDate() != null)
//                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getDueDate())
//                                : "-", param.getValue().dueDateProperty()));
//        dueDate.setMinWidth(160);
//
//        TableColumn<TblHotelInvoice, String> supplierName = new TableColumn("Supplier");
//        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelInvoice, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblSupplier().getSupplierName(),
//                        param.getValue().tblSupplierProperty()));
//        supplierName.setMinWidth(140);
//
//        tableView.getColumns().addAll(codeInvoice, subject, issueDate, dueDate, supplierName);
//
//        tableView.setItems(loadAllDataInvoice());
//
//        tableView.setRowFactory(tv -> {
//            TableRow<TblHotelInvoice> row = new TableRow<>();
//            row.setOnMouseClicked((e) -> {
//                if (e.getClickCount() == 2) {
//                    if (isShowStatus.get()) {
//                        dataInvoiceUnshowHandle();
//                    } else {
//                        if ((!row.isEmpty())) {
//                            dataInvoiceShowHandle();
//                        }
//                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataInvoiceShowHandle();
//                        }
//                    }
//                }
//            });
//            return row;
//        });
//
//        tableDataInvoice = new ClassTableWithControl(tableView);
//    }
//
//    private void setTableControlDataInvoice() {
//        //set control from feature
//        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
//        JFXButton buttonControl;
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Buat Invoice");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataInvoiceCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
////        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Ubah");
////            buttonControl.setOnMouseClicked((e) -> {
////                //listener update
////                dataInvoiceUpdateHandle();
////            });
////            buttonControls.add(buttonControl);
////        }
////        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Hapus");
////            buttonControl.setOnMouseClicked((e) -> {
////                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
////                if (alert.getResult() == ButtonType.OK) {
////                    //listener delete
////                    dataInvoiceDeleteHandle();
////                }
////            });
////            buttonControls.add(buttonControl);
////        }
////        if (DashboardController.feature.getSelectedRoleFeature().getApproveData()) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Approve");
////            buttonControl.setOnMouseClicked((e) -> {
////                //listener approve
////                dataInvoiceApproveHandle();
////            });
////            buttonControls.add(buttonControl);
////        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataInvoicePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        tableDataInvoice.addButtonControl(buttonControls);
//    }
//
//    private ObservableList<TblHotelInvoice> loadAllDataInvoice() {
//        List<TblHotelInvoice> list = parentController.getService().getAllDataHotelInvoiceByIDSupplierNotNullAndIDHotelInvoiceType(1);   //Receivable = '1'
//        for (TblHotelInvoice data : list) {
//            //data partner
//            //...
//            //data supplier
//            data.setTblSupplier(parentController.getService().getDataSupplier(data.getTblSupplier().getIdsupplier()));
//        }
//        return FXCollections.observableArrayList(list);
//    }
//
//    /**
//     * FORM INPUT
//     */
//    @FXML
//    private AnchorPane formAnchor;
//
//    @FXML
//    private GridPane gpFormDataInvoice;
//
//    @FXML
//    private ScrollPane spFormDataInvoice;
//
//    @FXML
//    private JFXTextField txtCodeInvoice;
//
//    @FXML
//    private JFXTextField txtSubject;
//
//    @FXML
//    private JFXDatePicker dtpIssueDate;
//
//    @FXML
//    private JFXDatePicker dtpDueDate;
//
//    @FXML
//    private JFXTextArea txtInvoiceNote;
//
//    @FXML
//    private Label lblTotalBill;
//
//    @FXML
//    private AnchorPane ancSupplierLayout;
//    private JFXCComboBoxTablePopup<TblSupplier> cbpSupplier;
//
//    @FXML
//    private JFXButton btnSave;
//
//    @FXML
//    private JFXButton btnCancel;
//
//    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
//
//    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
//
//    private int scrollCounter = 0;
//
//    public TblHotelInvoice selectedData;
//
//    private void initFormDataInvoice() {
//        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
//            //@@@
//            spFormDataInvoice.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
//        });
//
//        //@@@
//        gpFormDataInvoice.setOnScroll((ScrollEvent scroll) -> {
//            isFormScroll.set(true);
//
//            scrollCounter++;
//
//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    Platform.runLater(() -> {
//                        if (scrollCounter == 1) {
//                            //scroll end..!
//                            isFormScroll.set(false);
//                        }
//                        scrollCounter--;
//                    });
//                } catch (Exception e) {
//                    System.out.println("err " + e.getMessage());
//                }
//
//            });
//            thread.setDaemon(true);
//            thread.start();
//        });
//
//        initDataPopup();
//
//        btnSave.getStyleClass().add("button-save");
//        btnSave.setTooltip(new Tooltip("Simpan (Data Invoice)"));
//        btnSave.setOnAction((e) -> {
//            dataInvoiceSaveHandle();
//        });
//
//        btnCancel.getStyleClass().add("button-cancel");
//        btnCancel.setTooltip(new Tooltip("Batal"));
//        btnCancel.setOnAction((e) -> {
//            dataInvoiceCancelHandle();
//        });
//
//        initImportantFieldColor();
//    }
//
//    private void initImportantFieldColor() {
//        ClassViewSetting.setImportantField(
//                txtSubject,
//                dtpIssueDate,
//                dtpDueDate,
//                cbpSupplier);
//    }
//
//    private void initDataPopup() {
//        //Supplier
//        TableView<TblSupplier> tableSupplier = new TableView<>();
//
//        TableColumn<TblSupplier, String> codeSupplier = new TableColumn<>("ID");
//        codeSupplier.setCellValueFactory(cellData -> cellData.getValue().codeSupplierProperty());
//        codeSupplier.setMinWidth(120);
//
//        TableColumn<TblSupplier, String> supplierName = new TableColumn<>("Supplier");
//        supplierName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
//        supplierName.setMinWidth(140);
//
//        tableSupplier.getColumns().addAll(codeSupplier, supplierName);
//
//        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
//
//        cbpSupplier = new JFXCComboBoxTablePopup<>(
//                TblSupplier.class, tableSupplier, supplierItems, "", "Supplier *", true, 200, 200
//        );
//
//        //attached to grid-pane
//        AnchorPane.setBottomAnchor(cbpSupplier, 0.0);
//        AnchorPane.setLeftAnchor(cbpSupplier, 0.0);
//        AnchorPane.setRightAnchor(cbpSupplier, 0.0);
//        AnchorPane.setTopAnchor(cbpSupplier, 0.0);
//        ancSupplierLayout.getChildren().clear();
//        ancSupplierLayout.getChildren().add(cbpSupplier);
//
//    }
//
//    private ObservableList<TblSupplier> loadAllDataSupplier() {
//        List<TblSupplier> list = parentController.getService().getAllDataSupplier();
//        return FXCollections.observableArrayList(list);
//    }
//
//    private void refreshDataPopup() {
//        //Supplier
//        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
//        cbpSupplier.setItems(supplierItems);
//    }
//
//    private void setSelectedDataToInputForm() {
//
//        refreshDataPopup();
//
//        txtCodeInvoice.textProperty().bindBidirectional(selectedData.codeHotelInvoiceProperty());
//        txtSubject.textProperty().bindBidirectional(selectedData.hotelInvoiceSubjectProperty());
//        txtInvoiceNote.textProperty().bindBidirectional(selectedData.hotelInvoiceNoteProperty());
//
//        lblTotalBill.setText("");
//
//        if (selectedData.getIssueDate() != null) {
//            dtpIssueDate.setValue(selectedData.getIssueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//        } else {
//            dtpIssueDate.setValue(null);
//        }
//        dtpIssueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                selectedData.setIssueDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//            }
//        });
//
//        if (selectedData.getDueDate() != null) {
//            dtpDueDate.setValue(selectedData.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//        } else {
//            dtpDueDate.setValue(null);
//        }
//        dtpDueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                selectedData.setDueDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//            }
//        });
//
//        cbpSupplier.valueProperty().bindBidirectional(selectedData.tblSupplierProperty());
//        cbpSupplier.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                refreshDataDetail(newVal);
//                refreshDataBill();
//            }
//        });
//
//        cbpSupplier.hide();
//
//        //init table detail
//        initTableDataDetail();
//
//        refreshDataBill();
//
//        setSelectedDataToInputFormFunctionalComponent();
//    }
//
//    private void setSelectedDataToInputFormFunctionalComponent() {
//        txtCodeInvoice.setDisable(true);
//        ClassViewSetting.setDisableForAllInputNode(gpFormDataInvoice,
//                dataInputStatus == 3,
//                txtCodeInvoice);
//
//        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
//    }
//
//    public BigDecimal calculationTotalBill() {
//        BigDecimal result = new BigDecimal("0");
//        if (tableInvoiceDetail != null) {
//            for (ReturPaymentInvoiceDetailCreated returPaymentInvoiceDetailCreated : (List<ReturPaymentInvoiceDetailCreated>) tableInvoiceDetail.getItems()) {
//                if (returPaymentInvoiceDetailCreated.getDataHotelReceivableRetur() != null
//                        && returPaymentInvoiceDetailCreated.getCreateStatus()) {
//                    result = result.add(calculationTotalRestOfBill(returPaymentInvoiceDetailCreated.getDataHotelReceivableRetur()));
//                }
//            }
//        }
//        return result;
//    }
//
//    public void refreshDataBill() {
//        //calculation total bill
//        lblTotalBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBill()));
//    }
//
//    /**
//     * HANDLE FOR DATA INPUT
//     */
//    //0 = 'insert'
//    //1 = 'update'
//    private int dataInputStatus = 0;
//
//    public void dataInvoiceCreateHandle() {
//        dataInputStatus = 0;
//        selectedData = new TblHotelInvoice();
//        selectedData.setRefHotelInvoiceType(parentController.getService().getDataHotelInvoiceType(1)); //Receivable = '1'
//        setSelectedDataToInputForm();
//        //open form data invoice
//        dataInvoiceFormShowStatus.set(0.0);
//        dataInvoiceFormShowStatus.set(1.0);
//    }
//
//    private void dataInvoiceUpdateHandle() {
//        if (tableDataInvoice.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputStatus = 1;
//            selectedData = parentController.getService().getDataHotelInvoice(((TblHotelInvoice) tableDataInvoice.getTableView().getSelectionModel().getSelectedItem()).getIdhotelInvoice());
//            selectedData.setTblSupplier(parentController.getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
//            selectedData.setRefHotelInvoiceType(parentController.getService().getDataHotelInvoiceType(selectedData.getRefHotelInvoiceType().getIdtype()));
//            setSelectedDataToInputForm();
//            //open form data invoice
//            dataInvoiceFormShowStatus.set(0.0);
//            dataInvoiceFormShowStatus.set(1.0);
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);
//
//    private void dataInvoiceShowHandle() {
//        if (tableDataInvoice.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputStatus = 3;
//            selectedData = parentController.getService().getDataHotelInvoice(((TblHotelInvoice) tableDataInvoice.getTableView().getSelectionModel().getSelectedItem()).getIdhotelInvoice());
//            selectedData.setTblSupplier(parentController.getService().getDataSupplier(selectedData.getTblSupplier().getIdsupplier()));
//            setSelectedDataToInputForm();
//            dataInvoiceFormShowStatus.set(1.0);
//            isShowStatus.set(true);
//        }
//    }
//
//    private void dataInvoiceUnshowHandle() {
//        tableDataInvoice.getTableView().setItems(loadAllDataInvoice());
//        dataInvoiceFormShowStatus.set(0);
//        isShowStatus.set(false);
//    }
//
//    private void dataInvoiceDeleteHandle() {
//        if (tableDataInvoice.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            if (parentController.getService().deleteDataHotelInvoice((TblHotelInvoice) tableDataInvoice.getTableView().getSelectionModel().getSelectedItem())) {
//                ClassMessage.showSucceedDeletingDataMessage("", null);
//                //refresh data from table & close form data invoice
//                tableDataInvoice.getTableView().setItems(loadAllDataInvoice());
//                dataInvoiceFormShowStatus.set(0.0);
//            } else {
//                ClassMessage.showFailedDeletingDataMessage(parentController.getService().getErrorMessage(), null);
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    private void dataInvoicePrintHandle() {
//        if (tableDataInvoice.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            printInvoice(((TblHotelInvoice) tableDataInvoice.getTableView().getSelectionModel().getSelectedItem()));
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    private void printInvoice(TblHotelInvoice dataHotelInvoice) {
////        String hotelName = "";
////        SysDataHardCode sdhHotelName = parentController.getService().getDataSysDataHardCode((long) 12);  //HotelName = '12'
////        if (sdhHotelName != null
////                && sdhHotelName.getDataHardCodeValue() != null) {
////            hotelName = sdhHotelName.getDataHardCodeValue();
////        }
////        String hotelAddress = "";
////        SysDataHardCode sdhHotelAddress = parentController.getService().getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
////        if (sdhHotelAddress != null
////                && sdhHotelAddress.getDataHardCodeValue() != null) {
////            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
////        }
////        String hotelPhoneNumber = "";
////        SysDataHardCode sdhHotelPhoneNumber = parentController.getService().getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
////        if (sdhHotelPhoneNumber != null
////                && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
////            hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
////        }
//        String hotelLogoName = "";
//        SysDataHardCode sdhHotelLogoName = parentController.getService().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
//        if (sdhHotelLogoName != null
//                && sdhHotelLogoName.getDataHardCodeValue() != null) {
//            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
//        }
//        //print data - preview
//        ClassPrinter.printInvoiceRetur(hotelLogoName,
//                dataHotelInvoice);
//    }
//
//    public void dataInvoiceSaveHandle() {
//        if (checkDataInputDataInvoice()) {
//            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
//            if (alert.getResult() == ButtonType.OK) {
//                //list data hotel invoice - receivable
//                List<TblHotelInvoiceReceivable> hotelInvoiceReceivables = new ArrayList<>();
//                for (ReturPaymentInvoiceDetailCreated returPaymentInvoiceDetailCreated : (List<ReturPaymentInvoiceDetailCreated>) tableInvoiceDetail.getItems()) {
//                    if (returPaymentInvoiceDetailCreated.getCreateStatus()) {
//                        hotelInvoiceReceivables.add(returPaymentInvoiceDetailCreated.getDataHotelInvoiceReceivable());
//                    }
//                }
//                //dummy entry
//                TblHotelInvoice dummySelectedData = new TblHotelInvoice(selectedData);
//                if (dummySelectedData.getTblPartner() != null) {
//                    dummySelectedData.setTblPartner(new TblPartner(dummySelectedData.getTblPartner()));
//                }
//                if (dummySelectedData.getTblSupplier() != null) {
//                    dummySelectedData.setTblSupplier(new TblSupplier(dummySelectedData.getTblSupplier()));
//                }
//                if (dummySelectedData.getRefHotelInvoiceType() != null) {
//                    dummySelectedData.setRefHotelInvoiceType(new RefHotelInvoiceType(dummySelectedData.getRefHotelInvoiceType()));
//                }
//                List<TblHotelInvoiceReceivable> dummyHotelInvoiceReceivables = new ArrayList<>();
//                for (TblHotelInvoiceReceivable hotelInvoiceReceivable : hotelInvoiceReceivables) {
//                    TblHotelInvoiceReceivable dummyHotelInvoiceReceivable = new TblHotelInvoiceReceivable(hotelInvoiceReceivable);
//                    dummyHotelInvoiceReceivable.setTblHotelInvoice(dummySelectedData);
//                    dummyHotelInvoiceReceivable.setTblHotelReceivable(new TblHotelReceivable(dummyHotelInvoiceReceivable.getTblHotelReceivable()));
//                    dummyHotelInvoiceReceivables.add(dummyHotelInvoiceReceivable);
//                }
//                switch (dataInputStatus) {
//                    case 0:
//                        if (parentController.getService().insertDataHotelInvoice(dummySelectedData,
//                                dummyHotelInvoiceReceivables) != null) {
//                            ClassMessage.showSucceedInsertingDataMessage("", null);
//                            //refresh data from table & close form data invoice
//                            tableDataInvoice.getTableView().setItems(loadAllDataInvoice());
//                            dataInvoiceFormShowStatus.set(0.0);
//                        } else {
//                            ClassMessage.showFailedInsertingDataMessage(parentController.getService().getErrorMessage(), null);
//                        }
//                        break;
////                case 1:   //need maintenance
////                    if (parentController.getFReturManager().updateDataRetur(dummySelectedData,
////                            dummyDetailLocations)) {
////                        HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Updating data successed..!", null);
////                        //refresh data from table & close form data receiving
////                        tableDataRetur.getTableView().setItems(loadAllDataRetur());
////                        dataReturFormShowStatus.set(0.0);
////                    } else {
////                        //reset id to null
////                        
////                        
////                        for (DetailLocation detailLocation : detailLocations) {
////                            
////                            //data retur detail (property) : item quantity (reset to 1)
////                            if (detailLocation.getTblDetail().getTblItem().getPropertyStatus()) {
////                                detailLocation.getTblDetail().setItemQuantity(new BigDecimal("1"));
////                            }
////                        }
////                        HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Updating data failed..!", parentController.getFReturManager().getErrorMessage());
////                    }
////                    break;
//                    default:
//                        break;
//                }
//            }
//        } else {
//            ClassMessage.showWarningInputDataMessage(errDataInput, null);
//        }
//    }
//
//    private void dataInvoiceCancelHandle() {
//        //refresh data from table & close form data invoice
//        tableDataInvoice.getTableView().setItems(loadAllDataInvoice());
//        dataInvoiceFormShowStatus.set(0.0);
//    }
//
//    private String errDataInput;
//
//    private boolean checkDataInputDataInvoice() {
//        boolean dataInput = true;
//        errDataInput = "";
//        if (txtSubject.getText() == null || txtSubject.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Subject : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (dtpIssueDate.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Tanggal Buat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (dtpDueDate.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Tanggal Jatuh Tempo : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (cbpSupplier.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Supplier : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (checkDataDetailIsEmpty()) {
//            dataInput = false;
//            errDataInput += "Detail data tagihan tidak boleh kosong..! \n";
//        }
//        return dataInput;
//    }
//
//    private boolean checkDataDetailIsEmpty() {
//        boolean empty = true;
//        for (ReturPaymentInvoiceDetailCreated data : (List<ReturPaymentInvoiceDetailCreated>) tableInvoiceDetail.getItems()) {
//            if (data.getCreateStatus()) {
//                empty = false;
//                break;
//            }
//        }
//        return empty;
//    }
//
//    /**
//     * DATA (Detail)
//     */
//    @FXML
//    private AnchorPane ancDataTableDetail;
//
////    public ClassTableWithControl tableInvoiceDetail;
//    public TableView tableInvoiceDetail;
//
//    private void initTableDataDetail() {
//        //set table
//        setTableDataDetail();
////        //set control
////        setTableControlDataDetail();
//        //set table-control to content-view
//        ancDataTableDetail.getChildren().clear();
//        AnchorPane.setBottomAnchor(tableInvoiceDetail, 0.0);
//        AnchorPane.setLeftAnchor(tableInvoiceDetail, 0.0);
//        AnchorPane.setRightAnchor(tableInvoiceDetail, 0.0);
//        AnchorPane.setTopAnchor(tableInvoiceDetail, 0.0);
//        ancDataTableDetail.getChildren().add(tableInvoiceDetail);
//    }
//
//    public void setTableDataDetail() {
//        TableView<ReturPaymentInvoiceDetailCreated> tableView = new TableView();
//        tableView.setEditable(dataInputStatus != 3);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, Boolean> createStatus = new TableColumn("Pilih");
//        createStatus.setCellValueFactory(cellData -> cellData.getValue().createStatusProperty());
//        createStatus.setCellFactory(CheckBoxTableCell.forTableColumn(createStatus));
//        createStatus.setMinWidth(70);
//        createStatus.setEditable(true);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> codeRetur = new TableColumn("No. Transaksi");
//        codeRetur.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDataHotelReceivableRetur().getTblRetur().getCodeRetur(),
//                        param.getValue().getDataHotelReceivableRetur().tblReturProperty()));
//        codeRetur.setMinWidth(120);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> returDate = new TableColumn("Tanggal Transaksi");
//        returDate.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDataHotelReceivableRetur().getTblRetur().getReturDate()),
//                        param.getValue().getDataHotelReceivableRetur().tblReturProperty()));
//        returDate.setMinWidth(160);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> codeSupplier = new TableColumn("ID");
//        codeSupplier.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDataHotelReceivableRetur().getTblRetur().getTblSupplier().getCodeSupplier(),
//                        param.getValue().getDataHotelReceivableRetur().tblReturProperty()));
//        codeSupplier.setMinWidth(120);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> supplierName = new TableColumn("Nama");
//        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDataHotelReceivableRetur().getTblRetur().getTblSupplier().getSupplierName(),
//                        param.getValue().getDataHotelReceivableRetur().tblReturProperty()));
//        supplierName.setMinWidth(140);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> supplierGroup = new TableColumn("Supplier");
//        supplierGroup.getColumns().addAll(codeSupplier, supplierName);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> totalBill = new TableColumn("Total Tagihan");
//        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue().getDataHotelReceivableRetur())),
//                        param.getValue().getDataHotelReceivableRetur().idrelationProperty()));
//        totalBill.setMinWidth(140);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> totalPayment = new TableColumn("Total Pembayaran");
//        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue().getDataHotelReceivableRetur())),
//                        param.getValue().getDataHotelReceivableRetur().idrelationProperty()));
//        totalPayment.setMinWidth(140);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
//        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue().getDataHotelReceivableRetur())),
//                        param.getValue().getDataHotelReceivableRetur().idrelationProperty()));
//        totalRestOfBill.setMinWidth(140);
//
//        TableColumn<ReturPaymentInvoiceDetailCreated, String> dataDetails = new TableColumn("Detail");
//        dataDetails.setMinWidth(120);
//        dataDetails.setCellValueFactory((TableColumn.CellDataFeatures<ReturPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(()
//                        -> "", param.getValue().getDataHotelReceivableRetur().tblReturProperty()));
//        Callback<TableColumn<ReturPaymentInvoiceDetailCreated, String>, TableCell<ReturPaymentInvoiceDetailCreated, String>> cellFactory
//                = new Callback<TableColumn<ReturPaymentInvoiceDetailCreated, String>, TableCell<ReturPaymentInvoiceDetailCreated, String>>() {
//                    @Override
//                    public TableCell call(TableColumn p) {
//                        return new InfoCellDetails();
//                    }
//                };
//        dataDetails.setCellFactory(cellFactory);
//
//        tableView.getColumns().addAll(createStatus, codeRetur, returDate, totalBill, totalPayment, totalRestOfBill, dataDetails);
//
//        tableView.setItems(FXCollections.observableArrayList(loadAllDataReturPaymentInvoiceDetailCreated()));
//
////        tableInvoiceDetail = new ClassTableWithControl(tableView);
//        tableInvoiceDetail = tableView;
//    }
//
//    public BigDecimal calculationTotalBill(TblHotelReceivableRetur dataHRR) {
//        BigDecimal result = new BigDecimal("0");
//        if (dataHRR != null) {
//            result = dataHRR.getTblHotelReceivable().getHotelReceivableNominal();
//        }
//        return result;
//    }
//
//    public BigDecimal calculationTotalPayment(TblHotelReceivableRetur dataHRR) {
//        BigDecimal result = new BigDecimal("0");
//        List<TblHotelFinanceTransaction> list = getService().getAllDataHotelFinanceTransactionByIDHotelReceivable(dataHRR.getTblHotelReceivable().getIdhotelReceivable());
//        for (TblHotelFinanceTransaction data : list) {
//            result = result.add(data.getTransactionNominal());
//        }
//        return result;
//    }
//
//    public BigDecimal calculationTotalRestOfBill(TblHotelReceivableRetur dataHRR) {
//        return calculationTotalBill(dataHRR).subtract(calculationTotalPayment(dataHRR));
//    }
//
//    class InfoCellDetails extends TableCell<ReturPaymentInvoiceDetailCreated, String> {
//
//        private JFXCComboBoxPopup cbpDetails;
//
//        public InfoCellDetails() {
//
//        }
//
//        @Override
//        public void updateItem(String item, boolean empty) {
//            super.updateItem(item, empty);
//            if (empty) {
//                setText(null);
//                setGraphic(null);
//            } else {
//                if (this.getTableRow() != null) {
//                    Object data = this.getTableRow().getItem();
//
//                    if (data != null) {
//                        cbpDetails = getComboBoxDetails(((ReturPaymentInvoiceDetailCreated) data).getDataHotelReceivableRetur().getTblRetur());
//
//                        cbpDetails.getStyleClass().add("detail-combo-box-popup");
//
//                        cbpDetails.showingProperty().addListener((obs, oldVal, newVal) -> {
//                            if (newVal) {
//                                this.getTableView().getSelectionModel().clearAndSelect(this.getTableRow().getIndex());
//                            }
//                        });
//
//                        cbpDetails.hide();
//
//                        cbpDetails.setPrefSize(100, 25);
//                        setAlignment(Pos.CENTER);
//
//                        setText(null);
//                        setGraphic(cbpDetails);
//
//                    } else {
//                        setText(null);
//                        setGraphic(null);
//                    }
//                } else {
//                    setText(null);
//                    setGraphic(null);
//                }
//            }
//        }
//
//        @Override
//        public void startEdit() {
////            super.startEdit();
//        }
//
//        @Override
//        public void cancelEdit() {
////            super.cancelEdit();
//        }
//
//    }
//
//    private JFXCComboBoxPopup getComboBoxDetails(TblRetur data) {
//
//        JFXCComboBoxPopup cbpDetails = new JFXCComboBoxPopup<>(null);
//
//        //popup button
//        JFXButton button = new JFXButton("Detail");
//        button.setPrefSize(100, 25);
//        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-data-details");
//        button.setTooltip(new Tooltip("Detail Data"));
//        button.setOnMouseClicked((e) -> cbpDetails.show());
//
//        //table
//        TableView tableView = new TableView();
//
//        TableColumn<DataDetail, String> itemName = new TableColumn("Barang");
//        itemName.setMinWidth(140);
//        itemName.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getItemName(),
//                        param.getValue().getTblDetail().tblSupplierItemProperty()));
//
//        TableColumn<DataDetail, String> expiredDate = new TableColumn("Tgl. Kadaluarsa");
//        expiredDate.setMinWidth(115);
//        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(()
//                        -> param.getValue().getDetailExpiredDate() == null
//                                ? "-"
//                                : ClassFormatter.dateFormate.format(param.getValue().getDetailExpiredDate()),
//                        param.getValue().detailExpiredDateProperty()));
//
//        TableColumn<DataDetail, String> codePO = new TableColumn("No. PO");
//        codePO.setMinWidth(95);
//        codePO.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblDetail() == null
//                        || param.getValue().getTblDetail().getTblMemorandumInvoice() == null
//                        || param.getValue().getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder() == null
//                                ? "-" : param.getValue().getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getCodePo(), param.getValue().getTblDetail().tblMemorandumInvoiceProperty()));
//
//        TableColumn<DataDetail, String> cost = new TableColumn("Harga");
//        cost.setMinWidth(100);
//        cost.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(()
//                        -> param.getValue().getTblDetail() == null
//                                ? "-"
//                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemCost()), param.getValue().getTblDetail().itemCostProperty()));
//
//        TableColumn<DataDetail, String> discount = new TableColumn("Diskon");
//        discount.setMinWidth(90);
//        discount.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(()
//                        -> param.getValue().getTblDetail() == null
//                                ? "-"
//                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemDiscount()), param.getValue().getTblDetail().itemDiscountProperty()));
//
//        TableColumn<DataDetail, String> quantity = new TableColumn("Jumlah");
//        quantity.setMinWidth(80);
//        quantity.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(()
//                        -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDetailQuantity()), param.getValue().detailQuantityProperty()));
//
//        TableColumn<DataDetail, String> unitName = new TableColumn("Satuan");
//        unitName.setMinWidth(100);
//        unitName.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(()
//                        -> param.getValue().getTblDetail() == null
//                        || param.getValue().getTblDetail().getTblSupplierItem() == null
//                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem() == null
//                        || param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getTblUnit() == null
//                                ? "-"
//                                : param.getValue().getTblDetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName(),
//                        param.getValue().getTblDetail().tblSupplierItemProperty()));
//
//        TableColumn<DataDetail, String> tax = new TableColumn("Tax(%)");
//        tax.setMinWidth(70);
//        tax.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(()
//                        -> param.getValue().getTblDetail() == null
//                                ? "0%"
//                                : ClassFormatter.currencyFormat.cFormat(param.getValue().getTblDetail().getItemTaxPercentage().multiply(new BigDecimal("100"))) + "%",
//                        param.getValue().getTblDetail().itemTaxPercentageProperty()));
//
//        TableColumn<DataDetail, String> totalCost = new TableColumn("Total Harga");
//        totalCost.setMinWidth(105);
//        totalCost.setCellValueFactory((TableColumn.CellDataFeatures<DataDetail, String> param)
//                -> Bindings.createStringBinding(()
//                        -> param.getValue().getTblDetail() == null
//                                ? "-"
//                                : ClassFormatter.currencyFormat.cFormat(calculationTotalCost(param.getValue())), param.getValue().getTblDetail().itemQuantityProperty()));
//
//        tableView.getColumns().addAll(itemName, expiredDate, codePO, cost, discount, quantity, unitName, tax, totalCost);
//
//        tableView.setItems(FXCollections.observableArrayList(loadAllDataDetail(data)));
//
//        //layout
//        AnchorPane ancContent = new AnchorPane();
//        ancContent.getStyleClass().add("anchor-data-content");
//        AnchorPane.setBottomAnchor(tableView, 5.0);
//        AnchorPane.setLeftAnchor(tableView, 5.0);
//        AnchorPane.setRightAnchor(tableView, 5.0);
//        AnchorPane.setTopAnchor(tableView, 5.0);
//        ancContent.getChildren().add(tableView);
//
//        //popup content
//        BorderPane content = new BorderPane();
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(930, 350);
//
//        content.setCenter(ancContent);
//
//        cbpDetails.setPopupEditor(false);
//        cbpDetails.promptTextProperty().set("");
//        cbpDetails.setLabelFloat(false);
//        cbpDetails.setPopupButton(button);
//        cbpDetails.settArrowButton(true);
//        cbpDetails.setPopupContent(content);
//
//        return cbpDetails;
//    }
//
//    private BigDecimal calculationTotalCost(DataDetail dataDetail) {
//        return ((new BigDecimal("1")).add(dataDetail.getTblDetail().getItemTaxPercentage()))
//                .multiply((dataDetail.getTblDetail().getItemCost().subtract(dataDetail.getTblDetail().getItemDiscount()))
//                        .multiply(dataDetail.getDetailQuantity()));
//    }
//
//    private List<DataDetail> loadAllDataDetail(TblRetur dataRetur) {
//        List<DataDetail> list = new ArrayList<>();
//        List<TblReturDetail> dataReturDetails = parentController.getService().getAllDataReturDetailByIDRetur(dataRetur.getIdretur());
//        for (TblReturDetail dataReturDetail : dataReturDetails) {
//            //data retur - detail
//            TblReturDetail dataDetail = dataReturDetail;
//            dataDetail.setTblRetur(dataRetur);
//            dataDetail.setTblMemorandumInvoice(parentController.getService().getDataMemorandumInvoice(dataDetail.getTblMemorandumInvoice().getIdmi()));
//            dataDetail.setTblSupplierItem(parentController.getService().getDataSupplierItem(dataDetail.getTblSupplierItem().getIdrelation()));
//            dataDetail.getTblSupplierItem().setTblItem(parentController.getService().getDataItem(dataDetail.getTblSupplierItem().getTblItem().getIditem()));
//            dataDetail.getTblSupplierItem().getTblItem().setTblUnit(parentController.getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
//            dataDetail.setTblLocation(parentController.getService().getDataLocation(dataDetail.getTblLocation().getIdlocation()));
//            if (dataDetail.getTblSupplierItem().getTblItem().getConsumable()) {    //consumable
//                //data retur - detail - item expired date
//                List<TblReturDetailItemExpiredDate> dataReturDetailPropertyItemExpiredDates
//                        = parentController.getService().getAllDataReturDetailItemExpiredDateByIDReturDetail(dataDetail.getIddetailRetur());
//                for (TblReturDetailItemExpiredDate dataReturDetailPropertyItemExpiredDate : dataReturDetailPropertyItemExpiredDates) {
//                    //data retur - detail
//                    dataReturDetailPropertyItemExpiredDate.setTblReturDetail(dataReturDetail);
//                    //data item expired
//                    dataReturDetailPropertyItemExpiredDate.setTblItemExpiredDate(parentController.getService().getDataItemExpiredDate(dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
////                    //data item
////                    dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().setTblItem(parentController.getService().getDataItem(dataReturDetailPropertyItemExpiredDate.getTblReturDetail().getTblItem().getIditem()));
//                }
//                //data retur - detail - item expired date - quantity
//                List<DetailItemExpiredDateQuantity> dataDetailItemExpiredDateQuantities = new ArrayList<>();
//                for (TblReturDetailItemExpiredDate dataReturDetailPropertyItemExpiredDate : dataReturDetailPropertyItemExpiredDates) {
//                    boolean found = false;
//                    for (DetailItemExpiredDateQuantity dataDetailItemExpiredDateQuantity : dataDetailItemExpiredDateQuantities) {
//                        if (dataReturDetailPropertyItemExpiredDate.getTblReturDetail().getIddetailRetur()
//                                == dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblReturDetail().getIddetailRetur()
//                                && dataReturDetailPropertyItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate().equals(dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate())) {
//                            dataDetailItemExpiredDateQuantity.setItemQuantity(dataDetailItemExpiredDateQuantity.getItemQuantity().add(new BigDecimal("1")));
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (!found) {
//                        DetailItemExpiredDateQuantity detailItemExpiredDateQuantity = new DetailItemExpiredDateQuantity(dataReturDetailPropertyItemExpiredDate,
//                                new BigDecimal("1"));
//                        dataDetailItemExpiredDateQuantities.add(detailItemExpiredDateQuantity);
//                    }
//                }
//                for (DetailItemExpiredDateQuantity dataDetailItemExpiredDateQuantity : dataDetailItemExpiredDateQuantities) {
//                    //data_detail
//                    DataDetail dd = new DataDetail(dataDetail);
//                    dd.setDetailExpiredDate(dataDetailItemExpiredDateQuantity.getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate());
//                    dd.setDetailQuantity(new BigDecimal(String.valueOf(dataDetailItemExpiredDateQuantity.getItemQuantity())));
//                    //add data to list 'data_detail'
//                    list.add(dd);
//                }
//            } else {
//                //add data to list 'data_detail'
//                DataDetail dd = new DataDetail(dataDetail);
//                dd.setDetailQuantity(new BigDecimal(String.valueOf(dataDetail.getItemQuantity())));
//                list.add(dd);
//            }
//        }
//        return list;
//    }
//
//    public class DataDetail {
//
//        private final ObjectProperty<TblReturDetail> tblDetail = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<java.util.Date> detailExpiredDate = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<BigDecimal> detailQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));
//
//        public DataDetail(TblReturDetail tblDetail) {
//
//            //data detail
//            this.tblDetail.set(tblDetail);
//
//        }
//
//        public ObjectProperty<TblReturDetail> tblDetailProperty() {
//            return tblDetail;
//        }
//
//        public TblReturDetail getTblDetail() {
//            return tblDetailProperty().get();
//        }
//
//        public void setTblDetail(TblReturDetail detail) {
//            tblDetailProperty().set(detail);
//        }
//
//        public ObjectProperty<BigDecimal> detailQuantityProperty() {
//            return detailQuantity;
//        }
//
//        public BigDecimal getDetailQuantity() {
//            return detailQuantityProperty().get();
//        }
//
//        public void setDetailQuantity(BigDecimal detailQuantity) {
//            detailQuantityProperty().set(detailQuantity);
//        }
//
//        public ObjectProperty<java.util.Date> detailExpiredDateProperty() {
//            return detailExpiredDate;
//        }
//
//        public java.util.Date getDetailExpiredDate() {
//            return detailExpiredDateProperty().get();
//        }
//
//        public void setDetailExpiredDate(java.util.Date detailExpiredDate) {
//            detailExpiredDateProperty().set(detailExpiredDate);
//        }
//
//    }
//
//    public class DetailItemExpiredDateQuantity {
//
//        private final ObjectProperty<TblReturDetailItemExpiredDate> dataDetailItemExporedDate = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<BigDecimal> itemQuantity = new SimpleObjectProperty<>(new BigDecimal("0"));
//
//        public DetailItemExpiredDateQuantity(
//                TblReturDetailItemExpiredDate dataDetailItemExporedDate,
//                BigDecimal itemQuantity) {
//
//            this.dataDetailItemExporedDate.set(dataDetailItemExporedDate);
//            this.itemQuantity.set(itemQuantity);
//        }
//
//        public ObjectProperty<TblReturDetailItemExpiredDate> dataDetailItemExporedDateProperty() {
//            return dataDetailItemExporedDate;
//        }
//
//        public TblReturDetailItemExpiredDate getDataDetailItemExporedDate() {
//            return dataDetailItemExporedDateProperty().get();
//        }
//
//        public void setDataDetailItemExporedDate(TblReturDetailItemExpiredDate dataDetailItemExporedDate) {
//            dataDetailItemExporedDateProperty().set(dataDetailItemExporedDate);
//        }
//
//        public ObjectProperty<BigDecimal> itemQuantityProperty() {
//            return itemQuantity;
//        }
//
//        public BigDecimal getItemQuantity() {
//            return itemQuantityProperty().get();
//        }
//
//        public void setItemQuantity(BigDecimal itemQuantity) {
//            itemQuantityProperty().set(itemQuantity);
//        }
//
//    }
//
////    private void setTableControlDataDetail() {
////        //set control from feature
////        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
////        JFXButton buttonControl;
////        if (true) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Tambah");
////            buttonControl.setOnMouseClicked((e) -> {
////                //listener add
////                dataDetailCreateHandle();
////            });
////            buttonControls.add(buttonControl);
////        }
////        if (true) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Ubah");
////            buttonControl.setOnMouseClicked((e) -> {
////                //listener update
////                dataDetailUpdateHandle();
////            });
////            buttonControls.add(buttonControl);
////        }
////        if (true) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Hapus");
////            buttonControl.setOnMouseClicked((e) -> {
////                //listener delete
////                dataDetailDeleteHandle();
////            });
////            buttonControls.add(buttonControl);
////        }
////        tableInvoiceDetail.addButtonControl(buttonControls);
////    }
//    private List<ReturPaymentInvoiceDetailCreated> loadAllDataReturPaymentInvoiceDetailCreated() {
//        List<ReturPaymentInvoiceDetailCreated> list = new ArrayList<>();
//        if (dataInputStatus != 3) {
//            //data retur payment invoice detail created
//            list = generateAllDataReturPaymentInvoiceDetailCreatedBySupplier(selectedData.getTblSupplier());
//            //data hotel invoice - receivable
//            List<TblHotelInvoiceReceivable> listHIR = getService().getAllDataHotelInvoiceReceivableByIDHotelInvoice(selectedData.getIdhotelInvoice());
//            //update data has been used (ceraete status = true)
//            for (int i = 0; i < list.size(); i++) {
//                for (TblHotelInvoiceReceivable dataHIR : listHIR) {
//                    if (list.get(i).getDataHotelInvoiceReceivable().getTblHotelReceivable().getIdhotelReceivable()
//                            == dataHIR.getTblHotelReceivable().getIdhotelReceivable()) {
//                        //data hotel receivable - retur
//                        TblHotelReceivableRetur dataHRR = getService().getDataHotelReceivableReturByIDHotelReceivable(dataHIR.getTblHotelReceivable().getIdhotelReceivable());
//                        //data hotel receivable
//                        dataHRR.setTblHotelReceivable(getService().getDataHotelReceivable(dataHRR.getTblHotelReceivable().getIdhotelReceivable()));
//                        //data hotel receivable type
//                        dataHRR.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(dataHRR.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
//                        //data finance transaction status
//                        dataHRR.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(dataHRR.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
//                        //data retur
//                        dataHRR.setTblRetur(getService().getDataRetur(dataHRR.getTblRetur().getIdretur()));
//                        //data supplier
//                        dataHRR.getTblRetur().setTblSupplier(getService().getDataSupplier(dataHRR.getTblRetur().getTblSupplier().getIdsupplier()));
//                        //data employee (created)
//                        dataHRR.setTblEmployeeByCreateBy(getService().getDataEmployee(dataHRR.getTblEmployeeByCreateBy().getIdemployee()));
//                        //data hotel invoice
//                        dataHIR.setTblHotelInvoice(selectedData);
//                        //data hotel receivable
//                        dataHIR.setTblHotelReceivable(dataHRR.getTblHotelReceivable());
//                        //set data retur payment invoice detail created
//                        ReturPaymentInvoiceDetailCreated data = new ReturPaymentInvoiceDetailCreated();
//                        data.setDataHotelInvoiceReceivable(dataHIR);
//                        data.setDataHotelReceivableRetur(dataHRR);
//                        data.setCreateStatus(true);
//                        //set to list
//                        list.set(i, data);
//                        break;
//                    }
//                }
//            }
//        } else {    //just for show detail data
//            //data hotel invoice - receivable
//            List<TblHotelInvoiceReceivable> listHIR = getService().getAllDataHotelInvoiceReceivableByIDHotelInvoice(selectedData.getIdhotelInvoice());
//            for (TblHotelInvoiceReceivable dataHIR : listHIR) {
//                //data hotel receivable - retur
//                TblHotelReceivableRetur dataHRR = getService().getDataHotelReceivableReturByIDHotelReceivable(dataHIR.getTblHotelReceivable().getIdhotelReceivable());
//                //data hotel receivable
//                dataHRR.setTblHotelReceivable(getService().getDataHotelReceivable(dataHRR.getTblHotelReceivable().getIdhotelReceivable()));
//                //data hotel receivable type
//                dataHRR.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(dataHRR.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
//                //data finance transaction status
//                dataHRR.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(dataHRR.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
//                //data retur
//                dataHRR.setTblRetur(getService().getDataRetur(dataHRR.getTblRetur().getIdretur()));
//                //data supplier
//                dataHRR.getTblRetur().setTblSupplier(getService().getDataSupplier(dataHRR.getTblRetur().getTblSupplier().getIdsupplier()));
//                //data employee (created)
//                dataHRR.setTblEmployeeByCreateBy(getService().getDataEmployee(dataHRR.getTblEmployeeByCreateBy().getIdemployee()));
//                //data hotel invoice
//                dataHIR.setTblHotelInvoice(selectedData);
//                //data hotel receivable
//                dataHIR.setTblHotelReceivable(dataHRR.getTblHotelReceivable());
//                //data retur payment invoice detail created
//                ReturPaymentInvoiceDetailCreated data = new ReturPaymentInvoiceDetailCreated();
//                data.setDataHotelInvoiceReceivable(dataHIR);
//                data.setDataHotelReceivableRetur(dataHRR);
//                data.setCreateStatus(true);
//                //add to list
//                list.add(data);
//            }
//        }
//        return list;
//    }
//
//    private void refreshDataDetail(TblSupplier dataSupplier) {
//        //refresh data table detail
////        tableInvoiceDetail.getTableView().setItems(FXCollections.observableArrayList(generateAllDataReturPaymentInvoiceDetailCreatedBySuppleir(dataSupplier)));
//        tableInvoiceDetail.setItems(FXCollections.observableArrayList(generateAllDataReturPaymentInvoiceDetailCreatedBySupplier(dataSupplier)));
//    }
//
//    private List<ReturPaymentInvoiceDetailCreated> generateAllDataReturPaymentInvoiceDetailCreatedBySupplier(
//            TblSupplier dataSupplier) {
//        List<ReturPaymentInvoiceDetailCreated> list = new ArrayList<>();
//        if (dataSupplier != null) {
//            //data hotel receivable - retur (by selected supplier)
//            List<TblHotelReceivableRetur> hotelReceivableReturs
//                    = parentController.getService().getAllDataHotelReceivableReturByIDSupplier(dataSupplier.getIdsupplier());
//            for (TblHotelReceivableRetur hotelReceivableRetur : hotelReceivableReturs) {
//                if (hotelReceivableRetur.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()
//                        != 2) { //Sudah Dibayar = '2'
//                    //set data hotel receivable - retur
//                    TblHotelReceivableRetur dataHRR = parentController.getService().getDataHotelReceivableRetur(hotelReceivableRetur.getIdrelation());
//                    //data hotel receivable
//                    dataHRR.setTblHotelReceivable(getService().getDataHotelReceivable(dataHRR.getTblHotelReceivable().getIdhotelReceivable()));
//                    //data hotel receivable type
//                    dataHRR.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(dataHRR.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
//                    //data finance transaction status
//                    dataHRR.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(dataHRR.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
//                    //data retur
//                    dataHRR.setTblRetur(getService().getDataRetur(dataHRR.getTblRetur().getIdretur()));
//                    //data supplier
//                    dataHRR.getTblRetur().setTblSupplier(getService().getDataSupplier(dataHRR.getTblRetur().getTblSupplier().getIdsupplier()));
//                    //data employee (created)
//                    dataHRR.setTblEmployeeByCreateBy(getService().getDataEmployee(dataHRR.getTblEmployeeByCreateBy().getIdemployee()));
//                    //set data hotel invoice - receivable
//                    TblHotelInvoiceReceivable dataHIR = new TblHotelInvoiceReceivable();
//                    //data hotel invoice
//                    dataHIR.setTblHotelInvoice(selectedData);
//                    //data hotel receivable
//                    dataHIR.setTblHotelReceivable(dataHRR.getTblHotelReceivable());
//                    //data retur payment invoice detail created
//                    ReturPaymentInvoiceDetailCreated data = new ReturPaymentInvoiceDetailCreated();
//                    data.setDataHotelInvoiceReceivable(dataHIR);
//                    data.setDataHotelReceivableRetur(dataHRR);
//                    data.setCreateStatus(false);
//                    //add data to list
//                    list.add(data);
//                }
//            }
//        }
//        return list;
//    }
//
//    public class ReturPaymentInvoiceDetailCreated {
//
//        private final ObjectProperty<TblHotelInvoiceReceivable> dataHotelInvoiceReceivable = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<TblHotelReceivableRetur> dataHotelReceivableRetur = new SimpleObjectProperty<>();
//
//        private final BooleanProperty createStatus = new SimpleBooleanProperty();
//
//        public ReturPaymentInvoiceDetailCreated() {
//
//            createStatus.addListener((obs, oldVal, newVal) -> {
//                refreshDataBill();
//            });
//
//        }
//
//        public ObjectProperty<TblHotelInvoiceReceivable> dataHotelInvoiceReceivableProperty() {
//            return dataHotelInvoiceReceivable;
//        }
//
//        public TblHotelInvoiceReceivable getDataHotelInvoiceReceivable() {
//            return dataHotelInvoiceReceivableProperty().get();
//        }
//
//        public void setDataHotelInvoiceReceivable(TblHotelInvoiceReceivable dataHotelInvoiceReceivable) {
//            dataHotelInvoiceReceivableProperty().set(dataHotelInvoiceReceivable);
//        }
//
//        public ObjectProperty<TblHotelReceivableRetur> dataHotelReceivableReturProperty() {
//            return dataHotelReceivableRetur;
//        }
//
//        public TblHotelReceivableRetur getDataHotelReceivableRetur() {
//            return dataHotelReceivableRetur.get();
//        }
//
//        public void setDataHotelReceivableRetur(TblHotelReceivableRetur dataHotelReceivableRetur) {
//            dataHotelReceivableReturProperty().set(dataHotelReceivableRetur);
//        }
//
//        public BooleanProperty createStatusProperty() {
//            return createStatus;
//        }
//
//        public boolean getCreateStatus() {
//            return createStatusProperty().get();
//        }
//
//        public void setCreateStatus(boolean createStatus) {
//            createStatusProperty().set(createStatus);
//        }
//
//    }
//
//    public ReturPaymentInvoiceDetailCreated selectedDataReturPaymentInvoiceDetailCreated;
//
//    /**
//     * HANDLE FROM DATA INPUT
//     */
//    //0 = 'insert'
//    //1 = 'update'
//    public int dataInputDetailStatus = 0;
//
//    public Stage dialogStageDetal;
//
//    public void dataDetailCreateHandle() {
////        if (selectedData.getTblSupplier() != null) {
////            dataInputDetailStatus = 0;
////            selectedDataReturPaymentInvoiceDetailCreated = new ReturPaymentInvoiceDetailCreated();
////            //...set data
////            //open form data - detail
////            showDataDetailDialog();
////        } else {
////            ClassMessage.showWarningSelectedDataMessage("", null);
////        }
//    }
//
//    public void dataDetailUpdateHandle() {
////        if (tableDataDetailInvoice.getSelectionModel().getSelectedItems().size() == 1) {
////            dataInputDetailStatus = 1;
////            selectedDataReturPaymentInvoiceDetailCreated = new ReturPaymentInvoiceDetailCreated();
////            //...set data
////            //open form data - detail
////            showDataDetailDialog();
////        } else {
////            ClassMessage.showWarningSelectedDataMessage("", null);
////        }
//    }
//
//    public void dataDetailDeleteHandle() {
////        if (tableInvoiceDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
////            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
////            if (alert.getResult() == ButtonType.OK) {
////                ClassMessage.showSucceedRemovingDataMessage("", null);
////                //remove data from table items list
////                tableInvoiceDetail.getTableView().getItems().remove(tableInvoiceDetail.getTableView().getSelectionModel().getSelectedItem());
////                //refresh bill
////                refreshDataBill();
////            }
////        } else {
////            ClassMessage.showWarningSelectedDataMessage("", null);
////        }
//    }
//
//    private void showDataDetailDialog() {
////        try {
////            // Load the fxml file and create a new stage for the popup dialog.
////            FXMLLoader loader = new FXMLLoader();
////            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_payment/ReturPaymentInvoiceDetailDialog.fxml"));
////
////            ReturPaymentInvoiceDetailController controller = new ReturPaymentInvoiceDetailController(this);
////            loader.setController(controller);
////
////            Region page = loader.load();
////
////            // Create the dialog Stage.
////            dialogStageDetal = new Stage();
////            dialogStageDetal.initModality(Modality.WINDOW_MODAL);
////            dialogStageDetal.initOwner(HotelFX.primaryStage);
////
////            //undecorated
////            Undecorator undecorator = new Undecorator(dialogStageDetal, page);
////            undecorator.getStylesheets().add("skin/undecorator.css");
////            undecorator.getMenuButton().setVisible(false);
////            undecorator.getMaximizeButton().setVisible(false);
////            undecorator.getMinimizeButton().setVisible(false);
////            undecorator.getFullScreenButton().setVisible(false);
////            undecorator.getCloseButton().setVisible(false);
////
////            Scene scene = new Scene(undecorator);
////            scene.setFill(Color.TRANSPARENT);
////
////            dialogStageDetal.initStyle(StageStyle.TRANSPARENT);
////            dialogStageDetal.setScene(scene);
////            dialogStageDetal.setResizable(false);
////
////            // Show the dialog and wait until the user closes it
////            dialogStageDetal.showAndWait();
////        } catch (IOException e) {
////            System.out.println("Err >> " + e.toString());
////        }
//    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        //set splitpane
//        setDataInvoiceSplitpane();
//
//        //init table
//        initTableDataInvoice();
//
//        //init form
//        initFormDataInvoice();
//
//        spDataInvoice.widthProperty().addListener((obs, oldVal, newVal) -> {
//            dataInvoiceFormShowStatus.set(0.0);
//        });
    }

    public ReturPaymentInvoiceController(ReturPaymentLayoutController parentController) {
        this.parentController = parentController;
    }

    private final ReturPaymentLayoutController parentController;

    public FReturManager getService() {
        return parentController.getService();
    }

}
