/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.partner_payment_x;

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
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FPartnerManager;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PartnerPaymentInvoiceController implements Initializable {

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
//        TableColumn<TblHotelInvoice, String> partnerName = new TableColumn("Partner");
//        partnerName.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelInvoice, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPartnerName(),
//                        param.getValue().tblPartnerProperty()));
//        partnerName.setMinWidth(140);
//
//        tableView.getColumns().addAll(codeInvoice, subject, issueDate, dueDate, partnerName);
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
//        List<TblHotelInvoice> list = parentController.getService().getAllDataHotelInvoiceByIDPartnerNotNullAndIDHotelInvoiceType(1);    //Receivable = '1'
//        for (TblHotelInvoice data : list) {
//            //data partner
//            data.setTblPartner(parentController.getService().getDataPartner(data.getTblPartner().getIdpartner()));
//            //data supplier
//            //...
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
//    private AnchorPane ancPartnerLayout;
//    private JFXCComboBoxTablePopup<TblPartner> cbpPartner;
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
//                cbpPartner);
//    }
//
//    private void initDataPopup() {
//        //Partner
//        TableView<TblPartner> tablePartner = new TableView<>();
//
//        TableColumn<TblPartner, String> partnerName = new TableColumn<>("Partner");
//        partnerName.setCellValueFactory(cellData -> cellData.getValue().partnerNameProperty());
//        partnerName.setMinWidth(140);
//
//        TableColumn<TblPartner, String> typeName = new TableColumn<>("Tipe");
//        typeName.setCellValueFactory((TableColumn.CellDataFeatures<TblPartner, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefPartnerType().getTypeName(),
//                        param.getValue().refPartnerTypeProperty()));
//        typeName.setMinWidth(140);
//
//        tablePartner.getColumns().addAll(partnerName, typeName);
//
//        ObservableList<TblPartner> partnerItems = FXCollections.observableArrayList(loadAllDataPartner());
//
//        cbpPartner = new JFXCComboBoxTablePopup<>(
//                TblPartner.class, tablePartner, partnerItems, "", "Partner *", true, 300, 200
//        );
//        
//        //attached to grid-pane
//        AnchorPane.setBottomAnchor(cbpPartner, 0.0);
//        AnchorPane.setLeftAnchor(cbpPartner, 0.0);
//        AnchorPane.setRightAnchor(cbpPartner, 0.0);
//        AnchorPane.setTopAnchor(cbpPartner, 0.0);
//        ancPartnerLayout.getChildren().clear();
//        ancPartnerLayout.getChildren().add(cbpPartner);
//    }
//
//    private ObservableList<TblPartner> loadAllDataPartner() {
//        List<TblPartner> list = parentController.getService().getAllDataPartner();
//        for (TblPartner data : list) {
//            //data partner type
//            data.setRefPartnerType(parentController.getService().getDataPartnerType(data.getRefPartnerType().getIdtype()));
//        }
//        return FXCollections.observableArrayList(list);
//    }
//
//    private void refreshDataPopup() {
//        //Partner
//        ObservableList<TblPartner> partnerItems = FXCollections.observableArrayList(loadAllDataPartner());
//        cbpPartner.setItems(partnerItems);
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
//        cbpPartner.valueProperty().bindBidirectional(selectedData.tblPartnerProperty());
//        cbpPartner.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                refreshDataDetail(newVal);
//                refreshDataBill();
//            }
//        });
//
//        cbpPartner.hide();
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
//            for (PartnerPaymentInvoiceDetailCreated partnerPaymentInvoiceDetailCreated : (List<PartnerPaymentInvoiceDetailCreated>) tableInvoiceDetail.getItems()) {
//                if (partnerPaymentInvoiceDetailCreated.getDataHotelReceivableReservationPaymentWithGuaranteeLetter() != null
//                        && partnerPaymentInvoiceDetailCreated.getCreateStatus()) {
//                    result = result.add(calculationTotalRestOfBill(partnerPaymentInvoiceDetailCreated.getDataHotelReceivableReservationPaymentWithGuaranteeLetter()));
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
//            selectedData.setTblPartner(parentController.getService().getDataPartner(selectedData.getTblPartner().getIdpartner()));
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
//            selectedData.setTblPartner(parentController.getService().getDataPartner(selectedData.getTblPartner().getIdpartner()));
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
////        try {
//        //print data - preview
//        ClassPrinter.printInvoiceHotel(
//                hotelLogoName,
//                dataHotelInvoice);
////        } catch (Throwable ex) {
////            Logger.getLogger(PartnerPaymentInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
////        }
//    }
//
//    public void dataInvoiceSaveHandle() {
//        if (checkDataInputDataInvoice()) {
//            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
//            if (alert.getResult() == ButtonType.OK) {
//                //list data hotel invoice - receivable
//                List<TblHotelInvoiceReceivable> hotelInvoiceReceivables = new ArrayList<>();
//                for (PartnerPaymentInvoiceDetailCreated partnerPaymentInvoiceDetailCreated : (List<PartnerPaymentInvoiceDetailCreated>) tableInvoiceDetail.getItems()) {
//                    if (partnerPaymentInvoiceDetailCreated.getCreateStatus()) {
//                        hotelInvoiceReceivables.add(partnerPaymentInvoiceDetailCreated.getDataHotelInvoiceReceivable());
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
//        if (cbpPartner.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Partner : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
//        for (PartnerPaymentInvoiceDetailCreated data : (List<PartnerPaymentInvoiceDetailCreated>) tableInvoiceDetail.getItems()) {
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
//        TableView<PartnerPaymentInvoiceDetailCreated> tableView = new TableView();
//        tableView.setEditable(dataInputStatus != 3);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, Boolean> createStatus = new TableColumn("Pilih");
//        createStatus.setCellValueFactory(cellData -> cellData.getValue().createStatusProperty());
//        createStatus.setCellFactory(CheckBoxTableCell.forTableColumn(createStatus));
//        createStatus.setMinWidth(70);
//        createStatus.setEditable(true);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> codeReservationPayment = new TableColumn("No. Transaksi");
//        codeReservationPayment.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getCodePayment(),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().tblReservationPaymentWithGuaranteePaymentProperty()));
//        codeReservationPayment.setMinWidth(120);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> transactionDate = new TableColumn("Tanggal Transaksi");
//        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getPaymentDate()),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().tblReservationPaymentWithGuaranteePaymentProperty()));
//        transactionDate.setMinWidth(160);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> codeReservation = new TableColumn("No. Reservasi");
//        codeReservation.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getTblReservation().getCodeReservation(),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().tblReservationPaymentWithGuaranteePaymentProperty()));
//        codeReservation.setMinWidth(120);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> partnerType = new TableColumn("Tipe");
//        partnerType.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().getTblReservationPaymentWithGuaranteePayment().getTblPartner().getRefPartnerType().getTypeName(),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().tblReservationPaymentWithGuaranteePaymentProperty()));
//        partnerType.setMinWidth(120);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> partnerName = new TableColumn("Nama");
//        partnerName.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().getTblReservationPaymentWithGuaranteePayment().getTblPartner().getPartnerName(),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().tblReservationPaymentWithGuaranteePaymentProperty()));
//        partnerName.setMinWidth(140);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> partnerGroup = new TableColumn("Partner");
//        partnerGroup.getColumns().addAll(partnerType, partnerName);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> totalBill = new TableColumn("Total Tagihan");
//        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter())),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().idrelationProperty()));
//        totalBill.setMinWidth(140);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> totalPayment = new TableColumn("Total Pembayaran");
//        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter())),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().idrelationProperty()));
//        totalPayment.setMinWidth(140);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
//        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter())),
//                        param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().idrelationProperty()));
//        totalRestOfBill.setMinWidth(140);
//
//        TableColumn<PartnerPaymentInvoiceDetailCreated, String> dataDetails = new TableColumn("Detail");
//        dataDetails.setMinWidth(120);
//        dataDetails.setCellValueFactory((TableColumn.CellDataFeatures<PartnerPaymentInvoiceDetailCreated, String> param)
//                -> Bindings.createStringBinding(()
//                        -> "", param.getValue().getDataHotelReceivableReservationPaymentWithGuaranteeLetter().tblReservationPaymentWithGuaranteePaymentProperty()));
//        Callback<TableColumn<PartnerPaymentInvoiceDetailCreated, String>, TableCell<PartnerPaymentInvoiceDetailCreated, String>> cellFactory
//                = new Callback<TableColumn<PartnerPaymentInvoiceDetailCreated, String>, TableCell<PartnerPaymentInvoiceDetailCreated, String>>() {
//                    @Override
//                    public TableCell call(TableColumn p) {
//                        return new InfoCellDetails();
//                    }
//                };
//        dataDetails.setCellFactory(cellFactory);
//
//        tableView.getColumns().addAll(createStatus, codeReservationPayment, transactionDate, totalBill, totalPayment, totalRestOfBill, dataDetails);
//
//        tableView.setItems(FXCollections.observableArrayList(loadAllDataPartnerPaymentInvoiceDetailCreated()));
//
////        tableInvoiceDetail = new ClassTableWithControl(tableView);
//        tableInvoiceDetail = tableView;
//    }
//
//    public BigDecimal calculationTotalBill(TblHotelReceivalbeReservationPaymentWithGuaranteeLetter dataHRGL) {
//        BigDecimal result = new BigDecimal("0");
//        if (dataHRGL != null) {
//            result = dataHRGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getUnitNominal();
//        }
//        return result;
//    }
//
//    public BigDecimal calculationTotalPayment(TblHotelReceivalbeReservationPaymentWithGuaranteeLetter dataHRGL) {
//        BigDecimal result = new BigDecimal("0");
//        List<TblHotelFinanceTransaction> list = getService().getAllDataHotelFinanceTransactionByIDHotelReceivable(dataHRGL.getTblHotelReceivable().getIdhotelReceivable());
//        for (TblHotelFinanceTransaction data : list) {
//            result = result.add(data.getTransactionNominal());
//        }
//        return result;
//    }
//
//    public BigDecimal calculationTotalRestOfBill(TblHotelReceivalbeReservationPaymentWithGuaranteeLetter dataHRGL) {
//        return calculationTotalBill(dataHRGL).subtract(calculationTotalPayment(dataHRGL));
//    }
//
//    class InfoCellDetails extends TableCell<PartnerPaymentInvoiceDetailCreated, String> {
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
//                        cbpDetails = getComboBoxDetails((PartnerPaymentInvoiceDetailCreated) data);
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
//    private JFXCComboBoxPopup getComboBoxDetails(PartnerPaymentInvoiceDetailCreated data) {
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
//        //content
//        AnchorPane dataContent = generateDataContent(data.getDataHotelReceivableReservationPaymentWithGuaranteeLetter().getTblReservationPaymentWithGuaranteePayment());
//
//        //layout
//        AnchorPane ancContent = new AnchorPane();
//        ancContent.getStyleClass().add("anchor-data-content");
//        AnchorPane.setBottomAnchor(dataContent, 5.0);
//        AnchorPane.setLeftAnchor(dataContent, 5.0);
//        AnchorPane.setRightAnchor(dataContent, 5.0);
//        AnchorPane.setTopAnchor(dataContent, 5.0);
//        ancContent.getChildren().add(dataContent);
//
//        //popup content
//        BorderPane content = new BorderPane();
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(1120, 500);
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
//    private AnchorPane generateDataContent(TblReservationPaymentWithGuaranteePayment dataReservationPaymentWithGuaranteePayment) {
//        AnchorPane dataContent = new AnchorPane();
//        if (dataReservationPaymentWithGuaranteePayment != null
//                && dataReservationPaymentWithGuaranteePayment.getIddetail() != 0L) {
//            //set load all data
//            String codeReservation = dataReservationPaymentWithGuaranteePayment.getTblReservationPayment()
//                    .getTblReservationBill().getTblReservation().getCodeReservation();
//            List<TblGuaranteeLetterItemDetail> dataGLIDs = parentController.getService().getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(dataReservationPaymentWithGuaranteePayment.getIddetail());
//            //data 'Reservation Room'
//            List<TblGuaranteeLetterItemDetail> dataGLIDRRs = new ArrayList<>();
//            for (TblGuaranteeLetterItemDetail dataGLID : dataGLIDs) {
//                if (dataGLID.getDetailType().equals("Room")) {
//                    dataGLIDRRs.add(dataGLID);
//                }
//            }
//            //data 'Reservation Service'
//            List<TblGuaranteeLetterItemDetail> dataGLIDRSs = new ArrayList<>();
//            for (TblGuaranteeLetterItemDetail dataGLID : dataGLIDs) {
//                if (dataGLID.getDetailType().equals("Service")) {
//                    dataGLIDRSs.add(dataGLID);
//                }
//            }
//            //data 'Reservation Item'
//            List<TblGuaranteeLetterItemDetail> dataGLIDRIs = new ArrayList<>();
//            for (TblGuaranteeLetterItemDetail dataGLID : dataGLIDs) {
//                if (dataGLID.getDetailType().equals("Item")) {
//                    dataGLIDRIs.add(dataGLID);
//                }
//            }
//            //set data layout
//            ScrollPane sp = new ScrollPane();
//            sp.getStyleClass().add("sp-data-detail");
//
//            AnchorPane.setBottomAnchor(sp, 0.0);
//            AnchorPane.setLeftAnchor(sp, 0.0);
//            AnchorPane.setRightAnchor(sp, 0.0);
//            AnchorPane.setTopAnchor(sp, 0.0);
//            dataContent.getChildren().add(sp);
//
//            GridPane gp = new GridPane();
//            gp.setHgap(15.0);
//            gp.setVgap(15.0);
//
//            gp.getColumnConstraints().add(new ColumnConstraints(5.0));
//            gp.getColumnConstraints().add(new ColumnConstraints(250.0));
//            gp.getColumnConstraints().add(new ColumnConstraints(USE_COMPUTED_SIZE));
//            gp.getColumnConstraints().add(new ColumnConstraints(5.0));
//
//            gp.getRowConstraints().add(new RowConstraints(5.0));
//
//            gp.getRowConstraints().add(new RowConstraints(30.0));
//            Label lblCodeReservation = new Label("No. Reservasi : " + codeReservation);
//            lblCodeReservation.getStyleClass().add("title-data-detail");
//            gp.add(lblCodeReservation, 1, 1);
//
//            gp.getRowConstraints().add(new RowConstraints(30.0));
//            Label lblReservationRoom = new Label("Reservasi Kamar");
//            lblReservationRoom.getStyleClass().add("title-data-detail");
//            gp.add(lblReservationRoom, 1, 2);
//
//            gp.getRowConstraints().add(new RowConstraints(200.0));
//            gp.add(getTableContent(dataGLIDRRs), 1, 3, 2, 1);
//
//            gp.getRowConstraints().add(new RowConstraints(30.0));
//            Label lblReservationService = new Label("Tambahan Layanan");
//            lblReservationService.getStyleClass().add("title-data-detail");
//            gp.add(lblReservationService, 1, 4);
//
//            gp.getRowConstraints().add(new RowConstraints(200.0));
//            gp.add(getTableContent(dataGLIDRSs), 1, 5, 2, 1);
//
//            gp.getRowConstraints().add(new RowConstraints(30.0));
//            Label lblReservationItem = new Label("Tambahan Barang");
//            lblReservationItem.getStyleClass().add("title-data-detail");
//            gp.add(lblReservationItem, 1, 6);
//
//            gp.getRowConstraints().add(new RowConstraints(200.0));
//            gp.add(getTableContent(dataGLIDRIs), 1, 7, 2, 1);
//
//            gp.getRowConstraints().add(new RowConstraints(5.0));
//
//            sp.setContent(gp);
//
//        }
//        return dataContent;
//    }
//
//    private TableView getTableContent(List<TblGuaranteeLetterItemDetail> dataGLIDs) {
//        TableView tableView = new TableView();
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> codeRTD = new TableColumn("Kode");
//        codeRTD.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getCodeRtd(),
//                        param.getValue().codeRtdProperty()));
//        codeRTD.setMinWidth(100);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> detailData = new TableColumn("Detail");
//        detailData.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getDetailName(),
//                        param.getValue().detailNameProperty()));
//        detailData.setMinWidth(140);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> detailDate = new TableColumn("Tanggal");
//        detailDate.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getDetailDate()),
//                        param.getValue().detailDateProperty()));
//        detailDate.setMinWidth(140);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> cost = new TableColumn("Harga");
//        cost.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDetailCost()),
//                        param.getValue().detailCostProperty()));
//        cost.setMinWidth(120);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> quantity = new TableColumn("Jumlah");
//        quantity.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> String.valueOf(param.getValue().getDetailQuantity()),
//                        param.getValue().detailQuantityProperty()));
//        quantity.setMinWidth(80);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> discount = new TableColumn("Diskon");
//        discount.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTotalDiscountNominal()),
//                        param.getValue().totalDiscountNominalProperty()));
//        discount.setMinWidth(100);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> serviceCharge = new TableColumn("Service Charge(%)");
//        serviceCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getServiceChargePercentage()),
//                        param.getValue().serviceChargePercentageProperty()));
//        serviceCharge.setMinWidth(140);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> tax = new TableColumn("Pajak(%)");
//        tax.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getTaxPercentage()),
//                        param.getValue().taxPercentageProperty()));
//        tax.setMinWidth(100);
//
//        TableColumn<TblGuaranteeLetterItemDetail, String> total = new TableColumn("Total");
//        total.setCellValueFactory((TableColumn.CellDataFeatures<TblGuaranteeLetterItemDetail, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalCost(param.getValue())),
//                        param.getValue().iddetailProperty()));
//        total.setMinWidth(120);
//
//        tableView.getColumns().addAll(codeRTD, detailData, detailDate, cost, quantity, discount, serviceCharge, tax, total);
//
//        tableView.setItems(FXCollections.observableArrayList(dataGLIDs));
//
//        return tableView;
//    }
//
//    private BigDecimal calculationTotalCost(TblGuaranteeLetterItemDetail dataGLID) {
//        BigDecimal result = new BigDecimal("0");
//        if (dataGLID != null) {
//            result = (dataGLID.getDetailCost().multiply(dataGLID.getDetailQuantity()))
//                    .subtract(dataGLID.getTotalDiscountNominal());
//            BigDecimal sc = result.multiply(dataGLID.getServiceChargePercentage());
//            BigDecimal tax = (result.add(sc)).multiply(dataGLID.getTaxPercentage());
//            result = result.add((sc.add(tax)));
//        }
//        return result;
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
//    private List<PartnerPaymentInvoiceDetailCreated> loadAllDataPartnerPaymentInvoiceDetailCreated() {
//        List<PartnerPaymentInvoiceDetailCreated> list = new ArrayList<>();
//        if (dataInputStatus != 3) {
//            //data partner payment invoice detail created
//            list = generateAllDataPartnerPaymentInvoiceDetailCreatedByPartner(selectedData.getTblPartner());
//            //data hotel invoice - receivable
//            List<TblHotelInvoiceReceivable> listHIR = getService().getAllDataHotelInvoiceReceivableByIDHotelInvoice(selectedData.getIdhotelInvoice());
//            //update data has been used (ceraete status = true)
//            for (int i = 0; i < list.size(); i++) {
//                for (TblHotelInvoiceReceivable dataHIR : listHIR) {
//                    if (list.get(i).getDataHotelInvoiceReceivable().getTblHotelReceivable().getIdhotelReceivable()
//                            == dataHIR.getTblHotelReceivable().getIdhotelReceivable()) {
//                        //data hotel receivable - reservation payment with guarantee letter
//                        TblHotelReceivalbeReservationPaymentWithGuaranteeLetter dataHRRPWGL = getService().getDataHotelReceivalbeReservationPaymentWithGuaranteeLetterByIDHotelReceivable(dataHIR.getTblHotelReceivable().getIdhotelReceivable());
//                        //data hotel receivable
//                        dataHRRPWGL.setTblHotelReceivable(getService().getDataHotelReceivable(dataHRRPWGL.getTblHotelReceivable().getIdhotelReceivable()));
//                        //data hotel receivable type
//                        dataHRRPWGL.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(dataHRRPWGL.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
//                        //data finance transaction status
//                        dataHRRPWGL.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(dataHRRPWGL.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
//                        //data reservation guarantee letter
//                        dataHRRPWGL.setTblReservationPaymentWithGuaranteePayment(getService().getDataReservationPaymentWithGuaranteePayment(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getIddetail()));
//                        //data partner
//                        dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().setTblPartner(getService().getDataPartner(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().getIdpartner()));
//                        //data partner type
//                        dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().setRefPartnerType(getService().getDataPartnerType(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().getRefPartnerType().getIdtype()));
//                        //data reservation payment
//                        dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().setTblReservationPayment(getService().getDataReservationPayment(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getIdpayment()));
//                        //data reservation bill
//                        dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().setTblReservationBill(getService().getDataReservationBill(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getIdbill()));
//                        //data reservation
//                        dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().setTblReservation(getService().getDataReservation(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getTblReservation().getIdreservation()));
//                        //data employee (created)
//                        dataHRRPWGL.setTblEmployeeByCreateBy(getService().getDataEmployee(dataHRRPWGL.getTblEmployeeByCreateBy().getIdemployee()));
//                        //data hotel invoice
//                        dataHIR.setTblHotelInvoice(selectedData);
//                        //data hotel receivable
//                        dataHIR.setTblHotelReceivable(dataHRRPWGL.getTblHotelReceivable());
//                        //set data partner payment invoice detail created
//                        PartnerPaymentInvoiceDetailCreated data = new PartnerPaymentInvoiceDetailCreated();
//                        data.setDataHotelInvoiceReceivable(dataHIR);
//                        data.setdataHotelReceivableReservationPaymentWithGuaranteeLetter(dataHRRPWGL);
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
//                //data hotel receivable - reservation payment with guarantee letter
//                TblHotelReceivalbeReservationPaymentWithGuaranteeLetter dataHRRPWGL = getService().getDataHotelReceivalbeReservationPaymentWithGuaranteeLetterByIDHotelReceivable(dataHIR.getTblHotelReceivable().getIdhotelReceivable());
//                //data hotel receivable
//                dataHRRPWGL.setTblHotelReceivable(getService().getDataHotelReceivable(dataHRRPWGL.getTblHotelReceivable().getIdhotelReceivable()));
//                //data hotel receivable type
//                dataHRRPWGL.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(dataHRRPWGL.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
//                //data finance transaction status
//                dataHRRPWGL.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(dataHRRPWGL.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
//                //data reservation guarantee letter
//                dataHRRPWGL.setTblReservationPaymentWithGuaranteePayment(getService().getDataReservationPaymentWithGuaranteePayment(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getIddetail()));
//                //data partner
//                dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().setTblPartner(getService().getDataPartner(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().getIdpartner()));
//                //data partner type
//                dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().setRefPartnerType(getService().getDataPartnerType(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().getRefPartnerType().getIdtype()));
//                //data reservation payment
//                dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().setTblReservationPayment(getService().getDataReservationPayment(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getIdpayment()));
//                //data reservation bill
//                dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().setTblReservationBill(getService().getDataReservationBill(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getIdbill()));
//                //data reservation
//                dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().setTblReservation(getService().getDataReservation(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getTblReservation().getIdreservation()));
//                //data employee (created)
//                dataHRRPWGL.setTblEmployeeByCreateBy(getService().getDataEmployee(dataHRRPWGL.getTblEmployeeByCreateBy().getIdemployee()));
//                //data hotel invoice
//                dataHIR.setTblHotelInvoice(selectedData);
//                //data hotel receivable
//                dataHIR.setTblHotelReceivable(dataHRRPWGL.getTblHotelReceivable());
//                //data partner payment invoice detail created
//                PartnerPaymentInvoiceDetailCreated data = new PartnerPaymentInvoiceDetailCreated();
//                data.setDataHotelInvoiceReceivable(dataHIR);
//                data.setdataHotelReceivableReservationPaymentWithGuaranteeLetter(dataHRRPWGL);
//                data.setCreateStatus(true);
//                //add to list
//                list.add(data);
//            }
//        }
//        return list;
//    }
//
//    private void refreshDataDetail(TblPartner dataPartner) {
//        //refresh data table detail
////        tableInvoiceDetail.getTableView().setItems(FXCollections.observableArrayList(generateAllDataPartnerPaymentInvoiceDetailCreatedByPartner(dataPartner)));
//        tableInvoiceDetail.setItems(FXCollections.observableArrayList(generateAllDataPartnerPaymentInvoiceDetailCreatedByPartner(dataPartner)));
//    }
//
//    private List<PartnerPaymentInvoiceDetailCreated> generateAllDataPartnerPaymentInvoiceDetailCreatedByPartner(
//            TblPartner dataPartner) {
//        List<PartnerPaymentInvoiceDetailCreated> list = new ArrayList<>();
//        if (dataPartner != null) {
//            //data hotel receivable - reservation guarantee payment (by selected partner)
//            List<TblHotelReceivalbeReservationPaymentWithGuaranteeLetter> hotelReceivalbeReservationPaymentWithGuaranteeLetters
//                    = parentController.getService().getAllDataHotelReceivalbeReservationPaymentWithGuaranteeLetterByIDPartner(dataPartner.getIdpartner());
//            for (TblHotelReceivalbeReservationPaymentWithGuaranteeLetter hotelReceivalbeReservationPaymentWithGuaranteeLetter : hotelReceivalbeReservationPaymentWithGuaranteeLetters) {
//                if (hotelReceivalbeReservationPaymentWithGuaranteeLetter.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()
//                        != 2) { //Sudah Dibayar = '2'
//                    //set data hotel receivable - reservation payment with guarantee letter
//                    TblHotelReceivalbeReservationPaymentWithGuaranteeLetter dataHRRPWGL = parentController.getService().getDataHotelReceivalbeReservationPaymentWithGuaranteeLetter(hotelReceivalbeReservationPaymentWithGuaranteeLetter.getIdrelation());
//                    //data hotel receivable
//                    dataHRRPWGL.setTblHotelReceivable(getService().getDataHotelReceivable(dataHRRPWGL.getTblHotelReceivable().getIdhotelReceivable()));
//                    //data hotel receivable type
//                    dataHRRPWGL.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(dataHRRPWGL.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
//                    //data finance transaction status
//                    dataHRRPWGL.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(dataHRRPWGL.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
//                    //data reservation guarantee letter
//                    dataHRRPWGL.setTblReservationPaymentWithGuaranteePayment(getService().getDataReservationPaymentWithGuaranteePayment(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getIddetail()));
//                    //data partner
//                    dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().setTblPartner(getService().getDataPartner(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().getIdpartner()));
//                    //data partner type
//                    dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().setRefPartnerType(getService().getDataPartnerType(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblPartner().getRefPartnerType().getIdtype()));
//                    //data reservation payment
//                    dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().setTblReservationPayment(getService().getDataReservationPayment(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getIdpayment()));
//                    //data reservation bill
//                    dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().setTblReservationBill(getService().getDataReservationBill(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getIdbill()));
//                    //data reservation
//                    dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().setTblReservation(getService().getDataReservation(dataHRRPWGL.getTblReservationPaymentWithGuaranteePayment().getTblReservationPayment().getTblReservationBill().getTblReservation().getIdreservation()));
//                    //data employee (created)
//                    dataHRRPWGL.setTblEmployeeByCreateBy(getService().getDataEmployee(dataHRRPWGL.getTblEmployeeByCreateBy().getIdemployee()));
//                    //set data hotel invoice - receivable
//                    TblHotelInvoiceReceivable dataHIR = new TblHotelInvoiceReceivable();
//                    //data hotel invoice
//                    dataHIR.setTblHotelInvoice(selectedData);
//                    //data hotel receivable
//                    dataHIR.setTblHotelReceivable(dataHRRPWGL.getTblHotelReceivable());
//                    //data partner payment invoice detail created
//                    PartnerPaymentInvoiceDetailCreated data = new PartnerPaymentInvoiceDetailCreated();
//                    data.setDataHotelInvoiceReceivable(dataHIR);
//                    data.setdataHotelReceivableReservationPaymentWithGuaranteeLetter(hotelReceivalbeReservationPaymentWithGuaranteeLetter);
//                    data.setCreateStatus(false);
//                    //add data to list
//                    list.add(data);
//                }
//            }
//        }
//        return list;
//    }
//
//    public class PartnerPaymentInvoiceDetailCreated {
//
//        private final ObjectProperty<TblHotelInvoiceReceivable> dataHotelInvoiceReceivable = new SimpleObjectProperty<>();
//
//        private final ObjectProperty<TblHotelReceivalbeReservationPaymentWithGuaranteeLetter> dataHotelReceivableReservationPaymentWithGuaranteeLetter = new SimpleObjectProperty<>();
//
//        private final BooleanProperty createStatus = new SimpleBooleanProperty();
//
//        public PartnerPaymentInvoiceDetailCreated() {
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
//        public ObjectProperty<TblHotelReceivalbeReservationPaymentWithGuaranteeLetter> dataHotelReceivableReservationPaymentWithGuaranteeLetterProperty() {
//            return dataHotelReceivableReservationPaymentWithGuaranteeLetter;
//        }
//
//        public TblHotelReceivalbeReservationPaymentWithGuaranteeLetter getDataHotelReceivableReservationPaymentWithGuaranteeLetter() {
//            return dataHotelReceivableReservationPaymentWithGuaranteeLetter.get();
//        }
//
//        public void setdataHotelReceivableReservationPaymentWithGuaranteeLetter(TblHotelReceivalbeReservationPaymentWithGuaranteeLetter dataHotelReceivableReservationPaymentWithGuaranteeLetter) {
//            dataHotelReceivableReservationPaymentWithGuaranteeLetterProperty().set(dataHotelReceivableReservationPaymentWithGuaranteeLetter);
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
//    public PartnerPaymentInvoiceDetailCreated selectedDataPartnerPaymentInvoiceDetailCreated;
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
////        if (selectedData.getTblPartner() != null) {
////            dataInputDetailStatus = 0;
////            selectedDataPartnerPaymentInvoiceDetailCreated = new PartnerPaymentInvoiceDetailCreated();
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
////            selectedDataPartnerPaymentInvoiceDetailCreated = new PartnerPaymentInvoiceDetailCreated();
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
////            loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_payment/PartnerPaymentInvoiceDetailDialog.fxml"));
////
////            PartnerPaymentInvoiceDetailController controller = new PartnerPaymentInvoiceDetailController(this);
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

    public PartnerPaymentInvoiceController(PartnerPaymentLayoutController parentController) {
        this.parentController = parentController;
    }

    private final PartnerPaymentLayoutController parentController;

    public FPartnerManager getService() {
        return parentController.getService();
    }

}
