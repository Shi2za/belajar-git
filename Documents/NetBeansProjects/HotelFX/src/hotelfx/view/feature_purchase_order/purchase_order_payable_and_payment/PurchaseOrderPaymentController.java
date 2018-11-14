/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.view.DashboardController;
import java.net.URL;
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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
public class PurchaseOrderPaymentController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataPOPayment;

    private DoubleProperty dataPOPaymentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataPOPaymentLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataPOPaymentSplitpane() {
        spDataPOPayment.setDividerPositions(1);

        dataPOPaymentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPOPaymentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPOPayment.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataPOPayment.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataPOPaymentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataPOPaymentLayout.setDisable(false);
                    tableDataPOPaymentLayoutDisableLayer.setDisable(true);
                    tableDataPOPaymentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataPOPaymentLayout.setDisable(true);
                    tableDataPOPaymentLayoutDisableLayer.setDisable(false);
                    tableDataPOPaymentLayoutDisableLayer.toFront();
                }
            }
        });

        dataPOPaymentFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataPOPaymentLayout;

    private ClassFilteringTable<TblHotelFinanceTransaction> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;
    
    private ClassTableWithControl tableDataPOPayment;

    private void initTableDataPOPayment() {
        //set table
        setTableDataPOPayment();
        //set control
        setTableControlDataPOPayment();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPOPayment, 15.0);
        AnchorPane.setLeftAnchor(tableDataPOPayment, 15.0);
        AnchorPane.setRightAnchor(tableDataPOPayment, 15.0);
        AnchorPane.setTopAnchor(tableDataPOPayment, 15.0);
        ancBodyLayout.getChildren().add(tableDataPOPayment);
    }

    private void setTableDataPOPayment() {
        TableView<TblHotelFinanceTransaction> tableView = new TableView();

        TableColumn<TblHotelFinanceTransaction, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory(cellData -> cellData.getValue().codeTransactionProperty());
        codePayment.setMinWidth(120);
        
        TableColumn<TblHotelFinanceTransaction, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> getSupplierName(param.getValue()),
                        param.getValue().idtransactionProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblHotelFinanceTransaction, String> paymentType = new TableColumn("Tipe Pembayaran");
        paymentType.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType() != null
                        ? (param.getValue().getIsReturnTransaction() ? ("Return" + " - ") : "") + param.getValue().getRefFinanceTransactionPaymentType().getTypeName() : "-", 
                        param.getValue().refFinanceTransactionPaymentTypeProperty()));
        paymentType.setMinWidth(160);
        
        TableColumn<TblHotelFinanceTransaction, String> paymentNominal = new TableColumn("Nominal Pembayaran");
        paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionNominal()), 
                        param.getValue().transactionNominalProperty()));
        paymentNominal.setMinWidth(160);

        TableColumn<TblHotelFinanceTransaction, String> paymentRoundingValue = new TableColumn("Nominal Pembulatan");
        paymentRoundingValue.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTransactionRoundingValue()), 
                        param.getValue().transactionRoundingValueProperty()));
        paymentRoundingValue.setMinWidth(160);
        
        TableColumn<TblHotelFinanceTransaction, String> paymentDate = new TableColumn("Tanggal Pembayaran");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransaction, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCreateDate()), 
                        param.getValue().createDateProperty()));
        paymentDate.setMinWidth(160);

        tableView.getColumns().addAll(codePayment, supplierName, paymentNominal, paymentRoundingValue, paymentType, paymentDate);
        tableView.setItems(loadAllDataHotelFinanceTransaction());

        tableView.setRowFactory(tv -> {
            TableRow<TblHotelFinanceTransaction> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPOPaymentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataPOPaymentShowHandle();
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataPOPaymentShowHandle();
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataPOPayment = new ClassTableWithControl(tableView);
        
        //set filter
        cft = new ClassFilteringTable<>(
                TblHotelFinanceTransaction.class,
                tableDataPOPayment.getTableView(),
                tableDataPOPayment.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataPOPayment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataPOPaymentPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPOPayment.addButtonControl(buttonControls);
    }

    private ObservableList<TblHotelFinanceTransaction> loadAllDataHotelFinanceTransaction() {
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
        List<TblPurchaseOrder> poList = parentController.getService().getAllDataPurchaseOrder();
        for (TblPurchaseOrder poData : poList) {
            if (poData.getRefPurchaseOrderStatus().getIdstatus() != 2 //Dibatalkan = '2'
                    && poData.getRefPurchaseOrderStatus().getIdstatus() != 3) {  //Sudah Tidak Berlaku = '3'
                if (poData.getTblHotelPayable() != null) {
                    List<TblHotelFinanceTransactionHotelPayable> hfthpList = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(poData.getTblHotelPayable().getIdhotelPayable());
                    for (TblHotelFinanceTransactionHotelPayable hfthpData : hfthpList) {
                        boolean found = false;
                        TblHotelFinanceTransaction hftData = parentController.getService().getDataHotelFinanceTransaction(hfthpData.getTblHotelFinanceTransaction().getIdtransaction());
                        if (hftData != null) {
                            for (TblHotelFinanceTransaction data : list) {
                                if (data.getIdtransaction() == hftData.getIdtransaction()) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                list.add(hftData);
                            }
                        }
                    }
                }
            }
        }
        //sorting data
        bubbleSort(list);
        return FXCollections.observableArrayList(list);
    }

    private void bubbleSort(List<TblHotelFinanceTransaction> arr) {
        boolean swapped = true;
        int j = 0;
        TblHotelFinanceTransaction tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < arr.size() - j; i++) {
                if (arr.get(i).getCreateDate().before(
                        arr.get(i + 1).getCreateDate())) {
                    tmp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }
    
    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataPOPayment;

    @FXML
    private ScrollPane spFormDataPOPayment;

    @FXML
    private JFXTextField txtCodeHotelFinanceTransaction;
    
    @FXML
    private JFXDatePicker dtpHotelFinanceTransactionDate;
    
    @FXML
    private JFXTextField txtSupplierName;
    
    @FXML
    private JFXTextField txtPaymentType;
    
    @FXML
    private JFXTextField txtRoundingValue;
    
    @FXML
    private Label lblHotelTransactionNominal;
    
    @FXML
    private Label lblCodeData;

    @FXML
    private JFXButton btnBack;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblHotelFinanceTransaction selectedData;

    private void initFormDataPOPayment() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataPOPayment.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataPOPayment.setOnScroll((ScrollEvent scroll) -> {
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

        btnBack.setTooltip(new Tooltip("Kembali"));
        btnBack.setOnAction((e) -> {
            dataPOPaymentBackHandle();
        });

        initDateCalendar();
        
        initImportantFieldColor();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpHotelFinanceTransactionDate);
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField();
    }

    private void setSelectedDataToInputForm() {
        lblCodeData.setText(selectedData.getCodeTransaction());
        
        txtCodeHotelFinanceTransaction.textProperty().bind(selectedData.codeTransactionProperty());
        
        if(selectedData.getCreateDate() != null){
            dtpHotelFinanceTransactionDate.setValue(selectedData.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }else{
            dtpHotelFinanceTransactionDate.setValue(null);
        }
        
        txtSupplierName.setText(getSupplierName(selectedData));
        
        txtPaymentType.setText(selectedData.getRefFinanceTransactionPaymentType() != null 
                ? (selectedData.getIsReturnTransaction() ? ("Return - ") : "") + selectedData.getRefFinanceTransactionPaymentType().getTypeName() : "-");
        txtRoundingValue.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionRoundingValue()));
        
        lblHotelTransactionNominal.setText(ClassFormatter.currencyFormat.format(selectedData.getTransactionNominal()));

        //init data detail
        initTableDataDetail();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private String getSupplierName(TblHotelFinanceTransaction dataHFT){
        if(dataHFT != null){
            List<TblHotelFinanceTransactionHotelPayable> hfthpList = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction(dataHFT.getIdtransaction());
            if(!hfthpList.isEmpty()){
                TblHotelPayable hp = parentController.getService().getDataHotelPayable(hfthpList.get(0).getTblHotelPayable().getIdhotelPayable());
                if(hp != null){
                    TblPurchaseOrder po = parentController.getService().getDataPurchaseOrderByIDHotelPayable(hp.getIdhotelPayable());
                    if(po != null){
                        TblSupplier supplier = parentController.getService().getDataSupplier(po.getTblSupplier().getIdsupplier());
                        if(supplier != null){
                            return supplier.getSupplierName();
                        }
                    }
                }
            }
        }
        return "-";
    }
    
    private void setSelectedDataToInputFormFunctionalComponent() {
        txtCodeHotelFinanceTransaction.setDisable(true);
        dtpHotelFinanceTransactionDate.setDisable(true);
        txtSupplierName.setDisable(true);
        txtPaymentType.setDisable(true);
        txtRoundingValue.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataPOPayment, dataInputStatus == 3, 
                txtCodeHotelFinanceTransaction, 
                dtpHotelFinanceTransactionDate, 
                txtSupplierName, 
                txtPaymentType, 
                txtRoundingValue);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPOPaymentShowHandle() {
        if (tableDataPOPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getService().getDataHotelFinanceTransaction(((TblHotelFinanceTransaction) tableDataPOPayment.getTableView().getSelectionModel().getSelectedItem()).getIdtransaction());
            setSelectedDataToInputForm();
            dataPOPaymentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPOPaymentUnshowHandle() {
        refreshDataTablePOPayment();
        dataPOPaymentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataPOPaymentPrintHandle() {

    }

    private void dataPOPaymentBackHandle() {
        //refresh data from table & close form data detail
        refreshDataTablePOPayment();
        dataPOPaymentFormShowStatus.set(0.0);
        isShowStatus.set(false);
    }

    public void refreshDataTablePOPayment(){
        tableDataPOPayment.getTableView().setItems(loadAllDataHotelFinanceTransaction());
        cft.refreshFilterItems(tableDataPOPayment.getTableView().getItems());
    }
    
    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancTableDetailLayout;

    public TableView tableDataDetail;

    private void initTableDataDetail() {
        //set table
        setTableDataDetail();
        //set table-control to content-view
        ancTableDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 10.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 10.0);
        AnchorPane.setRightAnchor(tableDataDetail, 10.0);
        AnchorPane.setTopAnchor(tableDataDetail, 10.0);
        ancTableDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail() {
        TableView<TblHotelFinanceTransactionHotelPayable> tableView = new TableView();

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codePO = new TableColumn("No. PO");
        codePO.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getCodePO(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codePO.setMinWidth(120);

        TableColumn<TblHotelFinanceTransactionHotelPayable, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelPayable()),
                        param.getValue().tblHotelPayableProperty()));
        codeInvoice.setMinWidth(120);
        
        TableColumn<TblHotelFinanceTransactionHotelPayable, String> nominalTransaction = new TableColumn("Nominal");
        nominalTransaction.setCellValueFactory((TableColumn.CellDataFeatures<TblHotelFinanceTransactionHotelPayable, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getNominalTransaction()), 
                        param.getValue().nominalTransactionProperty()));
        nominalTransaction.setMinWidth(150);
        
        tableView.getColumns().addAll(codePO, codeInvoice, nominalTransaction);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataPOpaymentDetail()));
        tableDataDetail = tableView;
    }
    
    private String getCodePO(TblHotelPayable hotelPayable){
        if(hotelPayable != null){
            TblPurchaseOrder dataPO = parentController.getService().getDataPurchaseOrderByIDHotelPayable(hotelPayable.getIdhotelPayable());
            if(dataPO != null){
                return dataPO.getCodePo();
            }
        }
        return "-";
    }
    
    private String getCodeInvoice(TblHotelPayable hotelPayable){
        if(hotelPayable != null
                && hotelPayable.getTblHotelInvoice() != null){
            return hotelPayable.getTblHotelInvoice().getCodeHotelInvoice();
        }
        return "-";
    }
    
    private List<TblHotelFinanceTransactionHotelPayable> loadAllDataPOpaymentDetail(){
        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
        if(selectedData != null
                && selectedData.getIdtransaction() != 0L){
            list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction(selectedData.getIdtransaction());
            for(TblHotelFinanceTransactionHotelPayable data : list){
                //data hotel payable
                data.setTblHotelPayable(parentController.getService().getDataHotelPayable(data.getTblHotelPayable().getIdhotelPayable()));
            }
        }
        return list;
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
        setDataPOPaymentSplitpane();

        //init table
        initTableDataPOPayment();

        //init form
        initFormDataPOPayment();

        spDataPOPayment.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataPOPaymentFormShowStatus.set(0.0);
        });
    }

    public PurchaseOrderPaymentController(PurchaseOrderPayableAndPaymentController parentController) {
        this.parentController = parentController;
    }

    private final PurchaseOrderPayableAndPaymentController parentController;

    public FPurchaseOrderManager getService() {
        return parentController.getService();
    }

}
