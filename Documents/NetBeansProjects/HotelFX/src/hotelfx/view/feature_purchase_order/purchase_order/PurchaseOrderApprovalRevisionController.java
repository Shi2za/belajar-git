/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefMemorandumInvoiceStatus;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
import hotelfx.persistence.model.RefPurchaseOrderPaymentStatus;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblMemorandumInvoice;
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
import hotelfx.persistence.service.FPurchaseOrderManager;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PurchaseOrderApprovalRevisionController implements Initializable {

    @FXML
    private AnchorPane ancFormApprovalRevision;

    @FXML
    private GridPane gpFormDataApprovalRevision;

    @FXML
    private Label lblCodeData;

    @FXML
    private AnchorPane ancPOSourceLayout;
    private final JFXCComboBoxPopup<TblPurchaseOrderRevisionHistory> cbpPORevisionHistory = new JFXCComboBoxPopup<>(TblPurchaseOrderRevisionHistory.class);

    @FXML
    private AnchorPane ancPOSourceItemArriveStatusLayout;
    private JFXCComboBoxPopup cbpPORevisionArrivalStatus = new JFXCComboBoxPopup<>(null);

    @FXML
    private AnchorPane ancPOSourcePaymentStatusLayout;
    private JFXCComboBoxPopup cbpPORevisionPaymentStatus = new JFXCComboBoxPopup<>(null);

    @FXML
    private JFXDatePicker dtpDueDate;

    @FXML
    private JFXTextField txtPOPaymentTypeInformation;

    @FXML
    private JFXTextArea txtPONote;

    @FXML
    private AnchorPane ancPRLayout;
    private JFXCComboBoxTablePopup<TblPurchaseRequest> cbpPR;

    @FXML
    private AnchorPane ancSupplierLayout;
    private JFXCComboBoxTablePopup<TblSupplier> cbpSupplier;

    @FXML
    private JFXTextArea txtRevisionReason;

    @FXML
    private JFXTextField txtRevisionNote;

    @FXML
    private JFXTextArea txtDataRevision;

    @FXML
    private JFXTextField txtSubTotal;

    @FXML
    private JFXTextField txtTax;

    @FXML
    private Spinner<Double> spnTaxPercentage;

    @FXML
    private Label lblTaxPercentage;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private JFXTextField txtDeliveryCost;

    @FXML
    private Label lblTotal;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataApprovalRevision() {
        initDataPopup();

        spnTaxPercentage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 100.00));
        spnTaxPercentage.setEditable(false);

        btnSave.setTooltip(new Tooltip("Setujui (Data Revisi Purchase Order)"));
        btnSave.setOnAction((e) -> {
            dataApprovalRevisionSaveHandle();
        });
        btnSave.setVisible(parentController.dataInputDetailStatus != -1);

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataApprovalRevisionCancelHandle();
        });

        initDateCalendar();

        initImportantFieldColor();
    }

    private void initDateCalendar() {
        ClassFormatter.setDatePickersPattern(ClassFormatter.datePartern,
                dtpDueDate);
//        ClassFormatter.setDatePickersEnableDateFrom(LocalDate.now(),
//                dtpDueDate);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField();
    }

    private void initDataPopup() {
        //Purchase Order - Revision History
        TableView<TblPurchaseOrderRevisionHistory> tablePORevisionHistory = new TableView<>();

        TableColumn<TblPurchaseOrderRevisionHistory, String> poRHCodePOSource = new TableColumn<>("No. PO (Lama)");
        poRHCodePOSource.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrderRevisionHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPurchaseOrderByIdposource().getCodePo(),
                        param.getValue().tblPurchaseOrderByIdposourceProperty()));
        poRHCodePOSource.setMinWidth(120);

        TableColumn<TblPurchaseOrderRevisionHistory, String> poRHRevisionDate = new TableColumn<>("Tgl. Revisi");
        poRHRevisionDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrderRevisionHistory, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getRevisionDate()),
                        param.getValue().revisionDateProperty()));
        poRHRevisionDate.setMinWidth(150);

        TableColumn<TblPurchaseOrderRevisionHistory, String> poRHRevisionEmployeeName = new TableColumn<>("Oleh");
        poRHRevisionEmployeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseOrderRevisionHistory, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeByIdemployeeProperty()));
        poRHRevisionEmployeeName.setMinWidth(140);

        tablePORevisionHistory.getColumns().addAll(poRHCodePOSource, poRHRevisionDate, poRHRevisionEmployeeName);

        ObservableList<TblPurchaseOrderRevisionHistory> poRevisionHistoryItems = FXCollections.observableArrayList(loadAllDataPORevision(parentController.selectedDataRevisionHistory));

        setFunctionPopupPORevisionHistory(cbpPORevisionHistory, tablePORevisionHistory, poRevisionHistoryItems, "", "", 420, 350);

        //Purchase Request
        TableView<TblPurchaseRequest> tablePR = new TableView<>();

        TableColumn<TblPurchaseRequest, String> codePR = new TableColumn<>("ID");
        codePR.setCellValueFactory(cellData -> cellData.getValue().codePrProperty());
        codePR.setMinWidth(120);

        TableColumn<TblPurchaseRequest, String> prDate = new TableColumn<>("Tanggal PR");
        prDate.setCellValueFactory((TableColumn.CellDataFeatures<TblPurchaseRequest, String> param)
                -> Bindings.createStringBinding(() -> (param.getValue().getPrdate() != null)
                                ? ClassFormatter.dateTimeFormate.format(param.getValue().getPrdate())
                                : "-", param.getValue().prdateProperty()));
        prDate.setMinWidth(160);

        tablePR.getColumns().addAll(codePR, prDate);

        ObservableList<TblPurchaseRequest> prItems = FXCollections.observableArrayList(loadAllDataPR());

        cbpPR = new JFXCComboBoxTablePopup<>(
                TblPurchaseRequest.class, tablePR, prItems, "", "No. PR", true, 300, 200
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
                TblSupplier.class, tableSupplier, supplierItems, "", "Supplier", true, 280, 200
        );

        //data po - arrrival status
        cbpPORevisionArrivalStatus = getComboBoxPORevisionArrivalStatus(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource());

        //data po - payment status
        cbpPORevisionPaymentStatus = getComboBoxPORevisionPaymentStatus(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource());

        //attached to grid-pane
//        ancPRLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(cbpPR, 0.0);
//        AnchorPane.setLeftAnchor(cbpPR, 0.0);
//        AnchorPane.setRightAnchor(cbpPR, 0.0);
//        AnchorPane.setTopAnchor(cbpPR, 0.0);
//        ancPRLayout.getChildren().add(cbpPR);
//        ancSupplierLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(cbpSupplier, 0.0);
//        AnchorPane.setLeftAnchor(cbpSupplier, 0.0);
//        AnchorPane.setRightAnchor(cbpSupplier, 0.0);
//        AnchorPane.setTopAnchor(cbpSupplier, 0.0);
//        ancSupplierLayout.getChildren().add(cbpSupplier);
        ancPOSourceLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpPORevisionHistory, 0.0);
        AnchorPane.setLeftAnchor(cbpPORevisionHistory, 0.0);
        AnchorPane.setRightAnchor(cbpPORevisionHistory, 0.0);
        AnchorPane.setTopAnchor(cbpPORevisionHistory, 0.0);
        ancPOSourceLayout.getChildren().add(cbpPORevisionHistory);
        ancPOSourceItemArriveStatusLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpPORevisionArrivalStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpPORevisionArrivalStatus, 0.0);
        AnchorPane.setRightAnchor(cbpPORevisionArrivalStatus, 0.0);
        AnchorPane.setTopAnchor(cbpPORevisionArrivalStatus, 0.0);
        ancPOSourceItemArriveStatusLayout.getChildren().add(cbpPORevisionArrivalStatus);
        ancPOSourcePaymentStatusLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpPORevisionPaymentStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpPORevisionPaymentStatus, 0.0);
        AnchorPane.setRightAnchor(cbpPORevisionPaymentStatus, 0.0);
        AnchorPane.setTopAnchor(cbpPORevisionPaymentStatus, 0.0);
        ancPOSourcePaymentStatusLayout.getChildren().add(cbpPORevisionPaymentStatus);
    }

    private List<TblPurchaseOrderRevisionHistory> loadAllDataPORevision(TblPurchaseOrderRevisionHistory dataPORevisionHistory) {
        List<TblPurchaseOrderRevisionHistory> list = new ArrayList<>();
        if (dataPORevisionHistory != null) {
            list.add(dataPORevisionHistory);
            setDataPORevisionPrevious(list);
        }
        if (!list.isEmpty()
                && parentController.dataInputDetailStatus == 0) {    //create
            list.remove(0);
        }
        return list;
    }

    private void setDataPORevisionPrevious(List<TblPurchaseOrderRevisionHistory> list) {
        TblPurchaseOrderRevisionHistory dataPORH = parentController.getService().getDataPurchaseOrderRevisionHistory(list.get(list.size() - 1).getTblPurchaseOrderByIdposource().getIdpo());
        if (dataPORH != null) {
            list.add(dataPORH);
            setDataPORevisionPrevious(list);
        }
    }

    private ObservableList<TblPurchaseRequest> loadAllDataPR() {
        List<TblPurchaseRequest> list = parentController.getService().getAllDataPurchaseRequest();
        for (TblPurchaseRequest data : list) {
//            //data supplier
//            data.setTblSupplier(parentController.getFPurchaseOrderManager().getDataSupplier(data.getTblSupplier().getIdsupplier()));
            //data pr - status
            data.setRefPurchaseRequestStatus(parentController.getService().getDataPurchaseRequestStatus(data.getRefPurchaseRequestStatus().getIdstatus()));
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
        List<TblSupplier> list = parentController.getService().getAllDataSupplier();
        for (TblSupplier data : list) {

        }
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Purchase Order - Revision History
        ObservableList<TblPurchaseOrderRevisionHistory> poRevisionHistoryItems = FXCollections.observableArrayList(loadAllDataPORevision(parentController.selectedDataRevisionHistory));
        cbpPORevisionHistory.setItems(poRevisionHistoryItems);

        //Purchase Request
        ObservableList<TblPurchaseRequest> prItems = FXCollections.observableArrayList(loadAllDataPR());
        cbpPR.setItems(prItems);

        //Supplier
        ObservableList<TblSupplier> supplierItems = FXCollections.observableArrayList(loadAllDataSupplier());
        cbpSupplier.setItems(supplierItems);

        //data po - arrrival status
        cbpPORevisionArrivalStatus = getComboBoxPORevisionArrivalStatus(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource());

        //data po - payment status
        cbpPORevisionPaymentStatus = getComboBoxPORevisionPaymentStatus(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource());
    }

    private void setFunctionPopupPORevisionHistory(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText,
            double prefWidth,
            double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != -1) {
                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
                    }
                    cbp.hide();
                });

        cbp.setPropertyNameForFiltered(nameFiltered);

        cbp.setItems(items);

        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton((parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getCodePo() != null
                ? parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getCodePo() : "-")
                + "");
//        button.textProperty().bind((new SimpleStringProperty("Revisi Ke-")).concat(cbp.getItems().s));
        button.setOnMouseClicked((e) -> cbp.show());
        button.setPrefSize(140, 25);

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth, prefHeight);

        content.setCenter(table);

        cbp.setPopupEditor(false);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(true);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);

        cbp.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

    }

    private JFXCComboBoxPopup getComboBoxPORevisionArrivalStatus(TblPurchaseOrder dataPO) {

        JFXCComboBoxPopup cbp = new JFXCComboBoxPopup<>(null);

        if (dataPO != null) {

            //popup button
            JFXButton button = new JFXButton(dataPO.getRefPurchaseOrderItemArriveStatus().getStatusName());
            button.setPrefSize(140, 25);
            button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-property-details");
//        button.setTooltip(new Tooltip("Properti"));
            button.setOnMouseClicked((e) -> cbp.show());

            AnchorPane ancDataTableData = new AnchorPane();

            TableView<DataReceivingDetail> tableData = new TableView<>();

            TableColumn<DataReceivingDetail, String> itemSistem = new TableColumn("(Sistem)");
            itemSistem.setCellValueFactory((TableColumn.CellDataFeatures<DataReceivingDetail, String> param)
                    -> Bindings.createStringBinding(() -> getItemSistem(param.getValue()),
                            param.getValue().tblDetailProperty()));
            itemSistem.setMinWidth(140);

            TableColumn<DataReceivingDetail, String> itemSupplier = new TableColumn("(Supplier)");
            itemSupplier.setCellValueFactory((TableColumn.CellDataFeatures<DataReceivingDetail, String> param)
                    -> Bindings.createStringBinding(() -> getItemSupplier(param.getValue()),
                            param.getValue().tblDetailProperty()));
            itemSupplier.setMinWidth(140);

            TableColumn<DataReceivingDetail, String> itemTitle = new TableColumn("Barang");
            itemTitle.getColumns().addAll(itemSistem, itemSupplier);

            TableColumn<DataReceivingDetail, String> quantityPO = new TableColumn("PO");
            quantityPO.setMinWidth(80);
            quantityPO.setCellValueFactory((TableColumn.CellDataFeatures<DataReceivingDetail, String> param)
                    -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getQuantityPO()), param.getValue().quantityPOProperty()));

            TableColumn<DataReceivingDetail, String> quantityHasBeenGet = new TableColumn("Sudah\nDiterima");
            quantityHasBeenGet.setMinWidth(80);
            quantityHasBeenGet.setCellValueFactory((TableColumn.CellDataFeatures<DataReceivingDetail, String> param)
                    -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getQuantityPO().subtract(getMaxQuantityAvailable(param.getValue().getTblDetail()))),
                            param.getValue().tblDetailProperty()));

            TableColumn<DataReceivingDetail, String> quantity = new TableColumn("Jumlah");
            quantity.getColumns().addAll(quantityPO, quantityHasBeenGet);

            tableData.getColumns().addAll(itemTitle, quantity);

            tableData.setItems(FXCollections.observableArrayList(loadAllDataReceivingDetail(dataPO)));

            AnchorPane.setBottomAnchor(tableData, 0.0);
            AnchorPane.setLeftAnchor(tableData, 0.0);
            AnchorPane.setRightAnchor(tableData, 0.0);
            AnchorPane.setTopAnchor(tableData, 0.0);
            ancDataTableData.getChildren().add(tableData);

            //layout
            AnchorPane ancContent = new AnchorPane();
            AnchorPane.setBottomAnchor(ancDataTableData, 0.0);
            AnchorPane.setLeftAnchor(ancDataTableData, 0.0);
            AnchorPane.setRightAnchor(ancDataTableData, 0.0);
            AnchorPane.setTopAnchor(ancDataTableData, 0.0);
            ancContent.getChildren().add(ancDataTableData);

            //popup content
            BorderPane content = new BorderPane();
            content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            content.setPrefSize(460, 350);

            content.setCenter(ancContent);

            cbp.setPopupEditor(false);
            cbp.promptTextProperty().set("");
            cbp.setLabelFloat(false);
            cbp.setPopupButton(button);
            cbp.settArrowButton(true);
            cbp.setPopupContent(content);

        }

        return cbp;
    }

    private String getItemSistem(DataReceivingDetail data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem() == null) {   //bonus
                result = data.getTblDetail().getTblItem().getItemName()
                        + "\n(" + data.getTblDetail().getTblItem().getCodeItem() + ")";
            } else {  //not bonus
                result = data.getTblDetail().getTblSupplierItem().getTblItem().getItemName()
                        + "\n(" + data.getTblDetail().getTblSupplierItem().getTblItem().getCodeItem() + ")";
            }
        }
        return result;
    }

    private String getItemSupplier(DataReceivingDetail data) {
        String result = "-";
        if (data != null
                && data.getTblDetail() != null) {
            if (data.getTblDetail().getTblSupplierItem() == null) {   //bonus
                result = "-";
            } else {  //not bonus
                if (data.getTblDetail().getTblSupplierItem().getSupplierItemName() != null) {
                    result = data.getTblDetail().getTblSupplierItem().getSupplierItemName()
                            + "\n(" + data.getTblDetail().getTblSupplierItem().getSupllierItemCode() + ")";
                }
            }
        }
        return result;
    }

    private BigDecimal getMaxQuantityAvailable(TblMemorandumInvoiceDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataDetail.getTblItem() == null) {
            result = parentController.getService().getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataDetail.getTblSupplierItem().getIdrelation()).getItemQuantity();
            List<TblMemorandumInvoiceDetail> list = parentController.getService().getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(dataDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataDetail.getTblSupplierItem().getIdrelation());
            for (TblMemorandumInvoiceDetail data : list) {
                result = result.subtract(data.getItemQuantity());
            }
        }
        return result;
    }

    public List<DataReceivingDetail> loadAllDataReceivingDetail(TblPurchaseOrder dataPO) {
        List<DataReceivingDetail> list = new ArrayList<>();
        if (dataPO != null) {
            List<TblMemorandumInvoice> mis = parentController.getService().getAllDataMemorandumInvoiceByIDPurchaseOrder(dataPO.getIdpo());
            for (TblMemorandumInvoice mi : mis) {
                List<TblMemorandumInvoiceDetail> mids = parentController.getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(mi.getIdmi());
                for (TblMemorandumInvoiceDetail mid : mids) {
                    boolean found = false;
                    for (DataReceivingDetail data : list) {
                        if (mid.getTblSupplierItem() == null) {   //bonus
                            found = true;
                            break;
                        } else {    //not bonus
                            if (mid.getTblSupplierItem().getIdrelation() == data.getTblDetail().getTblSupplierItem().getIdrelation()) {
                                data.getTblDetail().setItemQuantity(data.getTblDetail().getItemQuantity().add(mid.getItemQuantity()));
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        DataReceivingDetail data = new DataReceivingDetail(mid, getQuantityPO(mid));
                        list.add(data);
                    }
                }
            }
        }
        return list;
    }

    private BigDecimal getQuantityPO(TblMemorandumInvoiceDetail dataMIDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataMIDetail.getTblItem() == null) {
            result = parentController.getService().getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(dataMIDetail.getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                    dataMIDetail.getTblSupplierItem().getIdrelation()).getItemQuantity();
        }
        return result;
    }

    public class DataReceivingDetail {

        private final ObjectProperty<TblMemorandumInvoiceDetail> tblDetail = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> quantityPO = new SimpleObjectProperty<>(new BigDecimal("0"));

        public DataReceivingDetail(
                TblMemorandumInvoiceDetail tblDetail,
                BigDecimal quantityPO) {
            //data detail
            this.tblDetail.set(tblDetail);

            //data quantity po
            this.quantityPO.set(quantityPO);
        }

        public ObjectProperty<TblMemorandumInvoiceDetail> tblDetailProperty() {
            return tblDetail;
        }

        public TblMemorandumInvoiceDetail getTblDetail() {
            return tblDetailProperty().get();
        }

        public void setTblDetail(TblMemorandumInvoiceDetail detail) {
            tblDetailProperty().set(detail);
        }

        public ObjectProperty<BigDecimal> quantityPOProperty() {
            return quantityPO;
        }

        public BigDecimal getQuantityPO() {
            return quantityPOProperty().get();
        }

        public void setQuantityPO(BigDecimal quantityPO) {
            quantityPOProperty().set(quantityPO);
        }

    }

    private JFXCComboBoxPopup getComboBoxPORevisionPaymentStatus(TblPurchaseOrder dataPO) {

        JFXCComboBoxPopup cbp = new JFXCComboBoxPopup<>(null);

        if (dataPO != null) {
            //payment status
            String paymentStatusName = dataPO.getRefPurchaseOrderPaymentStatus().getStatusName();
            TblHotelPayable dataHP = null;
            if (dataPO.getTblHotelPayable() != null) {
                dataHP = parentController.getService().getDataHotelPayable(dataPO.getTblHotelPayable().getIdhotelPayable());
                paymentStatusName = dataHP.getRefFinanceTransactionStatus().getStatusName();
            }

            //popup button
            JFXButton button = new JFXButton(paymentStatusName);
            button.setPrefSize(140, 25);
            button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-property-details");
//        button.setTooltip(new Tooltip("Properti"));
            button.setOnMouseClicked((e) -> cbp.show());

            AnchorPane ancData = new AnchorPane();
            ancData.getStyleClass().add("anc-current-payment-nominal");

            Label lblData = new Label(ClassFormatter.currencyFormat.format(calculationTotalPayment(dataHP)));
            lblData.setPrefSize(180, 30);
            lblData.getStyleClass().add("label-current-payment-nominal");

            AnchorPane.setBottomAnchor(lblData, 10.0);
            AnchorPane.setLeftAnchor(lblData, 10.0);
            AnchorPane.setRightAnchor(lblData, 10.0);
            AnchorPane.setTopAnchor(lblData, 10.0);
            ancData.getChildren().add(lblData);

            //layout
            AnchorPane ancContent = new AnchorPane();
            AnchorPane.setBottomAnchor(ancData, 0.0);
            AnchorPane.setLeftAnchor(ancData, 0.0);
            AnchorPane.setRightAnchor(ancData, 0.0);
            AnchorPane.setTopAnchor(ancData, 0.0);
            ancContent.getChildren().add(ancData);

            //popup content
            BorderPane content = new BorderPane();
            content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            content.setPrefSize(200, 50);

            content.setCenter(ancContent);

            cbp.setPopupEditor(false);
            cbp.promptTextProperty().set("");
            cbp.setLabelFloat(false);
            cbp.setPopupButton(button);
            cbp.settArrowButton(true);
            cbp.setPopupContent(content);

        }

        return cbp;
    }

    public BigDecimal calculationTotalPayment(TblHotelPayable dataHP) {
        BigDecimal result = new BigDecimal("0");
        if (dataHP != null) {
            List<TblHotelFinanceTransactionHotelPayable> list = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(dataHP.getIdhotelPayable());
            for (TblHotelFinanceTransactionHotelPayable data : list) {
                if (data.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                    result = result.subtract(data.getNominalTransaction());
                } else {
                    result = result.add(data.getNominalTransaction());
                }
            }
        }
        return result;
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        lblCodeData.setText(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getCodePo() != null
                ? (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getCodePo()
                + " - "
                + parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getRefPurchaseOrderStatus().getStatusName()
                + " ("
                + parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblSupplier().getSupplierName()
                + " - "
                + parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest()
                + ")") : "");

        if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPodueDate() != null) {
            dtpDueDate.setValue(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPodueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dtpDueDate.setValue(null);
        }
        dtpDueDate.setDisable(true);

        txtPOPaymentTypeInformation.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().popaymentTypeInformationProperty());
        txtPOPaymentTypeInformation.setDisable(true);

        txtPONote.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().ponoteProperty());
        txtPONote.setDisable(true);

        txtRevisionReason.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.revisionReasonProperty());
        txtRevisionReason.setDisable(true);

        txtRevisionNote.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.revisionNoteProperty());
        txtRevisionNote.setDisable(true);

        Bindings.bindBidirectional(txtDiscount.textProperty(), parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().nominalDiscountProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtDiscount.setDisable(true);

        spnTaxPercentage.getValueFactory().setValue(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage() != null
                ? parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage().doubleValue() * 100
                : 0);
        spnTaxPercentage.setDisable(true);

        Bindings.bindBidirectional(txtDeliveryCost.textProperty(), parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().deliveryCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        txtDeliveryCost.setDisable(true);

        txtDataRevision.setEditable(false);

        cbpPR.valueProperty().bindBidirectional(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().tblPurchaseRequestProperty());
        cbpPR.setDisable(true);

        cbpSupplier.valueProperty().bindBidirectional(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().tblSupplierProperty());
        cbpSupplier.setDisable(true);

        cbpPR.hide();
        cbpSupplier.hide();

        initTableDataDetail();

        refreshDataBill();
    }

    private String getDataRevisionFrom(TblPurchaseOrderRevisionHistory dataPORevisionHistory) {
        String result = "";
        if (tableDataDetail != null
                && tableDataDetail != null
                && dataPORevisionHistory != null) {
            //due date
            LocalDate dueDateSource = LocalDate.of(
                    parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPodueDate().getYear() + 1900,
                    parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPodueDate().getMonth() + 1,
                    parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPodueDate().getDate());
            LocalDate dueDateNew = LocalDate.of(
                    parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPodueDate().getYear() + 1900,
                    parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPodueDate().getMonth() + 1,
                    parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPodueDate().getDate());
            if (dueDateSource.compareTo(dueDateNew) != 0) {
                result += "Estimasi Tgl. Kirim :\n"
                        + "dari " + ClassFormatter.dateFormate.format(Date.valueOf(dueDateSource)) + " "
                        + "menjadi " + ClassFormatter.dateFormate.format(Date.valueOf(dueDateNew)) + " "
                        + "\n\n";
            }
            //payment type
            if (!parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPopaymentTypeInformation()
                    .equals(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPopaymentTypeInformation())) {
                result += "Tipe Pembayaran :\n"
                        + "dari " + parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPopaymentTypeInformation() + " "
                        + "menjadi " + parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPopaymentTypeInformation() + " "
                        + "\n\n";
            }
            //po note
            if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPonote() != null) {
                if (!parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPonote()
                        .equals(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPonote())) {
                    result += "Keterangan PO :\n"
                            + "dari " + parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getPonote() + " "
                            + "menjadi "
                            + (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPonote() != null
                                    ? parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPonote()
                                    : "''")
                            + " "
                            + "\n\n";
                }
            } else {
                if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPonote() != null) {
                    result += "Keterangan PO :\n"
                            + "dari '' "
                            + "menjadi " + parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getPonote() + " "
                            + "\n\n";
                }
            }
            //tax
            if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTaxPecentage()
                    .compareTo(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage())
                    != 0) {
                result += "Pajak :\n"
                        + "dari " + ClassFormatter.decimalFormat.cFormat(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTaxPecentage().multiply(new BigDecimal("100"))) + "% "
                        + "menjadi " + ClassFormatter.decimalFormat.cFormat(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage().multiply(new BigDecimal("100"))) + "% "
                        + "\n\n";
            }
            //discount (all)
            if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getNominalDiscount()
                    .compareTo(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getNominalDiscount())
                    != 0) {
                result += "Diskon (Nominal) :\n"
                        + "dari " + ClassFormatter.currencyFormat.format(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getNominalDiscount()) + " "
                        + "menjadi " + ClassFormatter.currencyFormat.format(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getNominalDiscount()) + " "
                        + "\n\n";
            }
            //delivery cost
            if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getDeliveryCost()
                    .compareTo(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getDeliveryCost())
                    != 0) {
                result += "Biaya Pengiriman :\n"
                        + "dari " + ClassFormatter.currencyFormat.format(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getDeliveryCost()) + " "
                        + "menjadi " + ClassFormatter.currencyFormat.format(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getDeliveryCost()) + " "
                        + "\n\n";
            }
            //po - detail
            List<TblPurchaseOrderDetail> sourceDetails = parentController.getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(dataPORevisionHistory.getTblPurchaseOrderByIdposource().getIdpo());
            for (TblPurchaseOrderDetail sourceDetail : sourceDetails) {
                boolean found = false;
                for (PurchaseOrderDetailCreated newDetail : (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems()) {
                    if (newDetail.getCreateStatus()
                            && sourceDetail.getTblSupplierItem().getIdrelation()
                            == newDetail.getDataPODetail().getTblSupplierItem().getIdrelation()) {
                        String resultDetail = "";
                        //cost
                        if (sourceDetail.getItemCost()
                                .compareTo(newDetail.getDataPODetail().getItemCost()) != 0) {
                            resultDetail += "harga dari " + ClassFormatter.currencyFormat.format(sourceDetail.getItemCost()) + " "
                                    + "menjadi " + ClassFormatter.currencyFormat.format(newDetail.getDataPODetail().getItemCost()) + " "
                                    + "\n";
                        }
                        //discount
                        if (sourceDetail.getItemDiscount()
                                .compareTo(newDetail.getDataPODetail().getItemDiscount()) != 0) {
                            resultDetail += "diskon dari " + ClassFormatter.decimalFormat.cFormat(sourceDetail.getItemDiscount()) + " "
                                    + "menjadi " + ClassFormatter.decimalFormat.cFormat(newDetail.getDataPODetail().getItemDiscount()) + " "
                                    + "\n";
                        }
                        //quantity
                        if (sourceDetail.getItemQuantity()
                                .compareTo(newDetail.getDataPODetail().getItemQuantity()) != 0) {
                            resultDetail += "jumlah dari " + ClassFormatter.decimalFormat.cFormat(sourceDetail.getItemQuantity()) + " "
                                    + "menjadi " + ClassFormatter.decimalFormat.cFormat(newDetail.getDataPODetail().getItemQuantity()) + " "
                                    + sourceDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName()
                                    + "\n";
                        }
                        if (!resultDetail.equals("")) {
                            result += sourceDetail.getTblSupplierItem().getTblItem().getItemName() + " :\n"
                                    + resultDetail
                                    + "\n\n";
                        }
                        found = true;
                        break;
                    }
                }
                //po - detail - deleted
                if (!found) {
                    String resultDetail = "";
                    //cost
                    resultDetail += "- harga : " + ClassFormatter.currencyFormat.format(sourceDetail.getItemCost()) + " "
                            + "\n";
                    //discount
                    resultDetail += "- diskon : " + ClassFormatter.decimalFormat.cFormat(sourceDetail.getItemDiscount()) + " "
                            + "\n";
                    //quantity
                    resultDetail += "- jumlah : " + ClassFormatter.decimalFormat.cFormat(sourceDetail.getItemQuantity()) + " "
                            + sourceDetail.getTblSupplierItem().getTblItem().getTblUnit().getUnitName()
                            + "\n";
                    //data item
                    result += sourceDetail.getTblSupplierItem().getTblItem().getItemName() + " (-) :\n"
                            + resultDetail
                            + "\n\n";
                }
            }
            //po - detail - added
            for (PurchaseOrderDetailCreated newDetail : (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems()) {
                boolean found = false;
                for (TblPurchaseOrderDetail sourceDetail : sourceDetails) {
                    if (sourceDetail.getTblSupplierItem().getIdrelation()
                            == newDetail.getDataPODetail().getTblSupplierItem().getIdrelation()) {
                        found = true;
                        break;
                    }
                }
                //po - detail - added
                if (!found
                        && newDetail.getCreateStatus()) {
                    String resultDetail = "";
                    //cost
                    resultDetail += "- harga : " + ClassFormatter.currencyFormat.format(newDetail.getDataPODetail().getItemCost()) + " "
                            + "\n";
                    //discount
                    resultDetail += "- diskon : " + ClassFormatter.decimalFormat.cFormat(newDetail.getDataPODetail().getItemDiscount()) + " "
                            + "\n";
                    //quantity
                    resultDetail += "- jumlah : " + ClassFormatter.decimalFormat.cFormat(newDetail.getDataPODetail().getItemQuantity()) + " "
                            + newDetail.getDataPODetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName()
                            + "\n";
                    //data item
                    result += newDetail.getDataPODetail().getTblSupplierItem().getTblItem().getItemName() + " (+) :\n"
                            + resultDetail
                            + "\n\n";
                }
            }
            //total cost (po)
            BigDecimal totalCostPOSource = calculationTotal(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource(),
                    sourceDetails);
            BigDecimal totalCostPONew = calculationTotalCreated(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew(),
                    (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems());
            if (totalCostPOSource
                    .compareTo(totalCostPONew)
                    != 0) {
                result += "Total Tagihan PO :\n"
                        + "dari " + ClassFormatter.currencyFormat.format(totalCostPOSource) + " "
                        + "menjadi " + ClassFormatter.currencyFormat.format(totalCostPONew) + " "
                        + "\n\n";
            }
        }
        return result;
    }

    private void dataApprovalRevisionSaveHandle() {
        if (checkDataInputDataApprovalRevision()) {
            if (parentController.checkDataPOEnableToRevisionWithCurrentMaxRevision(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource())) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    List<TblPurchaseOrderDetail> list = new ArrayList<>();
                    for (PurchaseOrderDetailCreated data : (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems()) {
                        if (data.getCreateStatus()) {
                            list.add(data.getDataPODetail());
                        }
                    }
                    //dummy entry
                    TblPurchaseOrderRevisionHistory dummySelectedData = new TblPurchaseOrderRevisionHistory(parentController.selectedDataRevisionHistory);
                    dummySelectedData.setTblPurchaseOrderByIdposource(new TblPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdposource()));
                    dummySelectedData.getTblPurchaseOrderByIdposource().setTblSupplier(new TblSupplier(dummySelectedData.getTblPurchaseOrderByIdposource().getTblSupplier()));
                    if (dummySelectedData.getTblPurchaseOrderByIdposource().getTblPurchaseRequest() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdposource().setTblPurchaseRequest(new TblPurchaseRequest(dummySelectedData.getTblPurchaseOrderByIdposource().getTblPurchaseRequest()));
                    }
                    if (dummySelectedData.getTblPurchaseOrderByIdposource().getTblRetur() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdposource().setTblRetur(new TblRetur(dummySelectedData.getTblPurchaseOrderByIdposource().getTblRetur()));
                    }
                    if (dummySelectedData.getTblPurchaseOrderByIdposource().getRefPurchaseOrderPaymentStatus() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdposource().setRefPurchaseOrderPaymentStatus(new RefPurchaseOrderPaymentStatus(dummySelectedData.getTblPurchaseOrderByIdposource().getRefPurchaseOrderPaymentStatus()));
                    }
                    if (dummySelectedData.getTblPurchaseOrderByIdposource().getRefPurchaseOrderItemArriveStatus() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdposource().setRefPurchaseOrderItemArriveStatus(new RefPurchaseOrderItemArriveStatus(dummySelectedData.getTblPurchaseOrderByIdposource().getRefPurchaseOrderItemArriveStatus()));
                    }
                    dummySelectedData.setTblPurchaseOrderByIdponew(new TblPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdponew()));
                    dummySelectedData.getTblPurchaseOrderByIdponew().setTblSupplier(new TblSupplier(dummySelectedData.getTblPurchaseOrderByIdponew().getTblSupplier()));
                    if (dummySelectedData.getTblPurchaseOrderByIdponew().getTblPurchaseRequest() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdponew().setTblPurchaseRequest(new TblPurchaseRequest(dummySelectedData.getTblPurchaseOrderByIdponew().getTblPurchaseRequest()));
                    }
                    if (dummySelectedData.getTblPurchaseOrderByIdponew().getTblRetur() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdponew().setTblRetur(new TblRetur(dummySelectedData.getTblPurchaseOrderByIdponew().getTblRetur()));
                    }
                    if (dummySelectedData.getTblPurchaseOrderByIdponew().getRefPurchaseOrderPaymentStatus() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdponew().setRefPurchaseOrderPaymentStatus(new RefPurchaseOrderPaymentStatus(dummySelectedData.getTblPurchaseOrderByIdponew().getRefPurchaseOrderPaymentStatus()));
                    }
                    if (dummySelectedData.getTblPurchaseOrderByIdponew().getRefPurchaseOrderItemArriveStatus() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdponew().setRefPurchaseOrderItemArriveStatus(new RefPurchaseOrderItemArriveStatus(dummySelectedData.getTblPurchaseOrderByIdponew().getRefPurchaseOrderItemArriveStatus()));
                    }
                    List<TblPurchaseOrderDetail> dummyDataPurchaseOrderDetails = new ArrayList<>();
                    for (TblPurchaseOrderDetail dataPurchaseOrderDetail : list) {
                        TblPurchaseOrderDetail dummyDataPurchaseOrderDetail = new TblPurchaseOrderDetail(dataPurchaseOrderDetail);
                        dummyDataPurchaseOrderDetail.setTblPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdponew());
                        dummyDataPurchaseOrderDetail.setTblSupplierItem(new TblSupplierItem(dummyDataPurchaseOrderDetail.getTblSupplierItem()));
                        dummyDataPurchaseOrderDetails.add(dummyDataPurchaseOrderDetail);
                    }
                    //another data -> duplicated
                    List<TblMemorandumInvoice> dummyMISources = new ArrayList<>();
                    List<TblMemorandumInvoiceDetail> dummyMIDetailSources = new ArrayList<>();
                    List<TblMemorandumInvoice> dummyMINews = new ArrayList<>();
                    List<TblMemorandumInvoiceDetail> dummyMIDetailNews = new ArrayList<>();
                    List<TblReturDetail> dummyReturDetailSources = new ArrayList<>();
                    List<TblReturDetail> dummyReturDetailNews = new ArrayList<>();
                    List<TblHotelPayable> dummyHPSources = new ArrayList<>();
                    List<TblHotelPayable> dummyHPNews = new ArrayList<>();
                    List<TblHotelFinanceTransactionHotelPayable> dummyHFTHPSources = new ArrayList<>();
//                    List<TblHotelFinanceTransaction> dummyHFTSources = new ArrayList<>();
//                    List<TblHotelFinanceTransactionWithTransfer> dummyHFTWTSources = new ArrayList<>();
//                    List<TblHotelFinanceTransactionWithCekGiro> dummyHFTWCGSources = new ArrayList<>();
                    List<TblHotelFinanceTransactionHotelPayable> dummyHFTHPNews = new ArrayList<>();
//                    List<TblHotelFinanceTransaction> dummyHFTNews = new ArrayList<>();
//                    List<TblHotelFinanceTransactionWithTransfer> dummyHFTWTNews = new ArrayList<>();
//                    List<TblHotelFinanceTransactionWithCekGiro> dummyHFTWCGNews = new ArrayList<>();
                    List<TblHotelInvoice> dummyHISources = new ArrayList<>();
                    List<TblHotelInvoice> dummyHINews = new ArrayList<>();
                    //1. Hotel Payable
                    if (dummySelectedData.getTblPurchaseOrderByIdposource().getTblHotelPayable() != null) {
                        TblHotelPayable hpSource = parentController.getService().getDataHotelPayable(dummySelectedData.getTblPurchaseOrderByIdposource().getTblHotelPayable().getIdhotelPayable());
                        //hotel payable - (new)
                        TblHotelPayable hpNew = new TblHotelPayable(hpSource);
                        hpNew.setIdhotelPayable(0L);
                        hpNew.setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(hpNew.getRefFinanceTransactionStatus()));
                        hpNew.setTblPurchaseOrders(null);
                        hpNew.setTblHotelFinanceTransactionHotelPayables(null);
                        //hotel payable - (source)
                        hpSource.setRefFinanceTransactionStatus(parentController.getService().getDataFinanceTransactionStatus(3));  //Sudah Tidak Berlaku = '3'
                        //add to list for updating data
                        dummyHPSources.add(hpSource);
                        dummyHPNews.add(hpNew);
                        //hotel finnace transaction - hotel payable
                        List<TblHotelFinanceTransactionHotelPayable> hfthpSources = parentController.getService().getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(hpSource.getIdhotelPayable());
                        for (TblHotelFinanceTransactionHotelPayable hfthpSource : hfthpSources) {
                            //hotel finnace transaction
                            TblHotelFinanceTransaction hftSource = parentController.getService().getDataHotelFinanceTransaction(hfthpSource.getTblHotelFinanceTransaction().getIdtransaction());
//                            //hotel finnace transaction (new)
//                            TblHotelFinanceTransaction hftNew = new TblHotelFinanceTransaction(hftSource);
//                            hftNew.setIdtransaction(0L);
//                            hftNew.setTblHotelFinanceTransactionHotelPayables(null);
//                            hftNew.setTblHotelFinanceTransactionWithTransfers(null);
//                            hftNew.setTblHotelFinanceTransactionWithCekGiros(null);
//                            //hotel finnace transaction - with transfer
//                            TblHotelFinanceTransactionWithTransfer hftwtSource = parentController.getService().getDataHotelFinanceTransactionWithTransferByIDHotelFinanceTransaction(hftSource.getIdtransaction());
//                            if (hftwtSource != null) {
//                                //hotel finnace transaction - with transfer (new)
//                                TblHotelFinanceTransactionWithTransfer hftwtNew = new TblHotelFinanceTransactionWithTransfer(hftwtSource);
//                                hftwtNew.setIddetail(0L);
//                                hftwtNew.setTblHotelFinanceTransaction(hftNew);
//                                //hotel finnace transaction - with transfer (source)
//                                hftwtSource.setTblHotelFinanceTransaction(hftSource);
//                                //add to list for updating data
//                                dummyHFTWTSources.add(hftwtSource);
//                                dummyHFTWTNews.add(hftwtNew);
//                            }
//                            //hotel finnace transaction - with cek/giro
//                            TblHotelFinanceTransactionWithCekGiro hftwcgSource = parentController.getService().getDataHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction(hftSource.getIdtransaction());
//                            if (hftwcgSource != null) {
//                                //hotel finnace transaction - with cek/giro (new)
//                                TblHotelFinanceTransactionWithCekGiro hftwcgNew = new TblHotelFinanceTransactionWithCekGiro(hftwcgSource);
//                                hftwcgNew.setIddetail(0L);
//                                hftwcgNew.setTblHotelFinanceTransaction(hftNew);
//                                //hotel finnace transaction - with cek/giro (source)
//                                hftwcgSource.setTblHotelFinanceTransaction(hftSource);
//                                //add to list for updating data
//                                dummyHFTWCGSources.add(hftwcgSource);
//                                dummyHFTWCGNews.add(hftwcgNew);
//                            }
//                            //add to list for updating data
//                            dummyHFTSources.add(hftSource);
//                            dummyHFTNews.add(hftNew);
                            //hotel finnace transaction - hotel payable (new)
                            TblHotelFinanceTransactionHotelPayable hfthpNew = new TblHotelFinanceTransactionHotelPayable(hfthpSource);
                            hfthpNew.setIdrelation(0L);
                            hfthpNew.setTblHotelFinanceTransaction(hftSource);  //create relation with old data hotel finance transaction *
                            hfthpNew.setTblHotelPayable(hpNew);
                            //hotel finnace transaction - hotel payable (source)
                            hfthpSource.setTblHotelFinanceTransaction(hftSource);
                            hfthpSource.setTblHotelPayable(hpSource);
                            //add to list for updating data
                            dummyHFTHPSources.add(hfthpSource);
                            dummyHFTHPNews.add(hfthpNew);
                        }
                        //hotel invoice
                        if (hpSource.getTblHotelInvoice() != null) {
                            TblHotelInvoice hiSouce = parentController.getService().getDataHotelInvoice(hpSource.getTblHotelInvoice().getIdhotelInvoice());
                            //hotel invoice - (new)
                            TblHotelInvoice hiNew = new TblHotelInvoice(hiSouce);
                            hiNew.setIdhotelInvoice(0L);
                            hiNew.setTblHotelPayables(null);
                            hiNew.setTblHotelReceivables(null);
                            //add to list for updating data
                            dummyHISources.add(hiSouce);
                            dummyHINews.add(hiNew);
                            //set data invoice
                            hpSource.setTblHotelInvoice(hiSouce);
                            hpNew.setTblHotelInvoice(hiNew);
                        }
                        dummySelectedData.getTblPurchaseOrderByIdposource().setTblHotelPayable(hpSource);
                        dummySelectedData.getTblPurchaseOrderByIdponew().setTblHotelPayable(hpNew);
                    }
                    //2. Memorandum Invoice
                    List<TblMemorandumInvoice> miSources = parentController.getService().getAllDataMemorandumInvoiceByIDPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdposource().getIdpo());
                    for (TblMemorandumInvoice miSource : miSources) {
                        TblMemorandumInvoice miNew = new TblMemorandumInvoice(miSource);
                        miNew.setIdmi(0L);
                        miNew.setTblPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdponew());
                        miNew.setRefMemorandumInvoiceStatus(new RefMemorandumInvoiceStatus(miNew.getRefMemorandumInvoiceStatus()));
                        miNew.setTblMemorandumInvoiceDetails(null);
                        miNew.setTblReturDetails(null);
                        List<TblMemorandumInvoiceDetail> midSources = parentController.getService().getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(miSource.getIdmi());
                        for (TblMemorandumInvoiceDetail midSource : midSources) {
                            TblMemorandumInvoiceDetail midNew = new TblMemorandumInvoiceDetail(midSource);
                            midNew.setIddetail(0L);
                            midNew.setTblMemorandumInvoice(miNew);
                            midNew.setTblMemorandumInvoiceDetailPropertyBarcodes(null);
                            midNew.setTblMemorandumInvoiceDetailItemExpiredDates(null);
                            //Retur
                            List<TblReturDetail> rdSources = parentController.getService().getAllDataReturDetailByIDMemorandumInvoice(miSource.getIdmi());
                            for (TblReturDetail rdSource : rdSources) {
                                if (rdSource.getTblRetur().getRefReturStatus().getIdstatus() == 1) {  //Disetujui = '1'
                                    //retur detail - (new)
                                    TblReturDetail rdNew = new TblReturDetail(rdSource);
                                    rdNew.setIddetailRetur(0L);
                                    rdNew.setTblMemorandumInvoice(miNew);
                                    rdNew.setTblReturDetailItemExpiredDates(null);
                                    rdNew.setTblReturDetailPropertyBarcodes(null);
                                    //add to list for updating data
                                    rdSource.setTblMemorandumInvoice(miSource);
                                    dummyReturDetailSources.add(rdSource);
                                    dummyReturDetailNews.add(rdNew);
                                }
                            }
                            //add to list for updating data
                            midSource.setTblMemorandumInvoice(miSource);
                            dummyMIDetailSources.add(midSource);
                            dummyMIDetailNews.add(midNew);
                        }
                        miSource.setTblPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdposource());
                        miSource.setRefMemorandumInvoiceStatus(parentController.getService().getDataMemorandumInvoiceStatus(3));  //Sudah Tidak Berlaku = '3'
                        //add to list for updating data
                        dummyMISources.add(miSource);
                        dummyMINews.add(miNew);
                    }
                    //update all status, with current situation
                    //po-new (po-payment status)
                    dummySelectedData.getTblPurchaseOrderByIdponew().setRefPurchaseOrderPaymentStatus(getPOPaymentStatus(dummySelectedData.getTblPurchaseOrderByIdponew(),
                            dummyDataPurchaseOrderDetails,
                            dummyHFTHPNews));
                    //po-new (po-item-arrive status)
                    dummySelectedData.getTblPurchaseOrderByIdponew().setRefPurchaseOrderItemArriveStatus(getPOItemArriveStatus(dummyDataPurchaseOrderDetails,
                            dummyMIDetailNews));
                    //hp-new (transaction-finance status)
                    for (TblHotelPayable dummyHPNew : dummyHPNews) {
                        dummyHPNew.setHotelPayableNominal(getHotelPayableNominal(dummySelectedData.getTblPurchaseOrderByIdponew(),
                                dummyDataPurchaseOrderDetails));
                        dummyHPNew.setRefFinanceTransactionStatus(getFinanceTransactionStatus(dummyHPNew,
                                dummyHFTHPNews));
                    }
                    if (parentController.getService().updateDataPurchaseOrderApproveRevision(
                            dummySelectedData,
                            dummyDataPurchaseOrderDetails,
                            dummyMISources,
                            dummyMIDetailSources,
                            dummyMINews,
                            dummyMIDetailNews,
                            dummyReturDetailSources,
                            dummyReturDetailNews,
                            dummyHPSources,
                            dummyHPNews,
                            dummyHFTHPSources,
                            dummyHFTHPNews,
                            dummyHISources,
                            dummyHINews)) {
                        ClassMessage.showSucceedUpdatingDataMessage("", null);
                        //refresh data from table (po) & close form data purchase order revision
                        parentController.refreshDataTablePO();
                        parentController.dialogStageDetal.close();
                    } else {
                        ClassMessage.showFailedUpdatingDataMessage(parentController.getService().getErrorMessage(), null);
                    }
                }
            } else {
                ClassMessage.showWarningInputDataMessage("Data tidak dapat direvisi lagi, \ndata telah melebihi nilai maksimun revisi..!!", null);
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private RefPurchaseOrderPaymentStatus getPOPaymentStatus(TblPurchaseOrder po,
            List<TblPurchaseOrderDetail> poDetails,
            List<TblHotelFinanceTransactionHotelPayable> hfthps) {
        BigDecimal totalPO = calculationTotal(po, poDetails);
        BigDecimal totalPayment = new BigDecimal("0");
        for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
            if (hfthp.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                totalPayment = totalPayment.subtract(hfthp.getNominalTransaction());
            } else {
                totalPayment = totalPayment.add(hfthp.getNominalTransaction());
            }
        }
        if (totalPayment.compareTo(new BigDecimal("0")) == 0) {   //Belum Dibayar = '0'
            return parentController.getService().getDataPurchaseOrderPaymentStatus(0);
        } else {
            if (totalPayment.compareTo(totalPO) == 0) {   //Sudah Dibayar = '2'
                return parentController.getService().getDataPurchaseOrderPaymentStatus(2);
            } else {
                if (totalPayment.compareTo(totalPO) == -1) {  //Dibayar Sebagian = '1'
                    return parentController.getService().getDataPurchaseOrderPaymentStatus(1);
                } else {  //something wrong..
                    return po.getRefPurchaseOrderPaymentStatus();
                }
            }
        }
    }

    private RefPurchaseOrderItemArriveStatus getPOItemArriveStatus(List<TblPurchaseOrderDetail> poDetails,
            List<TblMemorandumInvoiceDetail> midDetails) {
        int idItemArriveStatus = 0; //Belum Diterima = '0'
        for (TblPurchaseOrderDetail poDetail : poDetails) {
            boolean found = false;
            BigDecimal poQuantity = poDetail.getItemQuantity();
            for (TblMemorandumInvoiceDetail midDetail : midDetails) {
                if (midDetail.getTblSupplierItem() != null //!bonus
                        && midDetail.getTblSupplierItem().getIdrelation() == poDetail.getIddetail()) {   //same item (suplier-item)
                    poQuantity = poQuantity.subtract(midDetail.getItemQuantity());
                    found = true;
                }
            }
            if (found) {
                if (poQuantity.compareTo(new BigDecimal("0")) == 0) { //found all data
                    if (idItemArriveStatus == 0) { //Belum Diterima = '0'
                        idItemArriveStatus = 2; //Sudah Diterima = '2'
                    }
                } else {
                    idItemArriveStatus = 1; //Diterima Sebagian = '1'
                }
            }
        }
        return parentController.getService().getDataPurchaseOrderItemArriveStatus(idItemArriveStatus);
    }

    private BigDecimal getHotelPayableNominal(TblPurchaseOrder po,
            List<TblPurchaseOrderDetail> poDetails) {
        BigDecimal result = calculationTotal(po, poDetails);
        return result;
    }

    private RefFinanceTransactionStatus getFinanceTransactionStatus(TblHotelPayable hp,
            List<TblHotelFinanceTransactionHotelPayable> hfthps) {
        BigDecimal totalPayment = new BigDecimal("0");
        for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
            if (hfthp.getTblHotelFinanceTransaction().getIsReturnTransaction()) {
                totalPayment = totalPayment.subtract(hfthp.getNominalTransaction());
            } else {
                totalPayment = totalPayment.add(hfthp.getNominalTransaction());
            }
        }
        if (totalPayment.compareTo(new BigDecimal("0")) == 0) {   //Belum Dibayar = '0'
            return parentController.getService().getDataFinanceTransactionStatus(0);
        } else {
            if (totalPayment.compareTo(hp.getHotelPayableNominal()) == 0) {  //Sudah Dibayar = '2'
                return parentController.getService().getDataFinanceTransactionStatus(2);
            } else {
                if (totalPayment.compareTo(hp.getHotelPayableNominal()) == -1) {  //Dibayar Sebagian = '1'
                    return parentController.getService().getDataFinanceTransactionStatus(1);
                } else {  //something wrong..
                    return hp.getRefFinanceTransactionStatus();
                }
            }
        }
    }

    private void dataApprovalRevisionCancelHandle() {
        //refresh data from table (po) & close form data purchase order revision
        parentController.refreshDataTablePO();
        parentController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataApprovalRevision() {
        boolean dataInput = true;
        errDataInput = "";
        if (!checkDataInputDataDetailByArrivalData()) {
            dataInput = false;
            errDataInput += "Data barang tidak sesuai dengan data penerimaan barang (PO).. \n";
        }
        if (!checkDataInputDataDetailByPaymentData()) {
            dataInput = false;
            errDataInput += "Data barang tidak sesuai dengan data pembayaran PO.. \n";
        }
        return dataInput;
    }

    private boolean checkDataInputDataDetailByArrivalData() {
        boolean dataInput = true;
        //data receiving detail
        List<DataReceivingDetail> drds = loadAllDataReceivingDetail(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource());
        for (DataReceivingDetail drd : drds) {
            boolean found = false;
            for (PurchaseOrderDetailCreated podc : (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems()) {
                if (drd.getTblDetail().getTblSupplierItem().getIdrelation()
                        == podc.getDataPODetail().getTblSupplierItem().getIdrelation()) {
                    if (drd.getTblDetail().getItemQuantity().compareTo(podc.getDataPODetail().getItemQuantity())
                            == 1) {
                        dataInput = false;
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                dataInput = false;
            }
            if (!dataInput) {
                break;
            }
        }
        return dataInput;
    }

    private boolean checkDataInputDataDetailByPaymentData() {
        if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblHotelPayable() != null) {
            TblHotelPayable dataHP = parentController.getService().getDataHotelPayable(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getTblHotelPayable().getIdhotelPayable());
            BigDecimal previousPayment = calculationTotalPayment(dataHP);
            BigDecimal currentBill = calculationTotalCreated(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew(),
                    (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems());
            return previousPayment.compareTo(currentBill) != 1;
        } else {
            return true;
        }
    }

    /**
     * DATA (Detail)
     */
    @FXML
    private AnchorPane ancDetailLayout;

    public TableView<PurchaseOrderDetailCreated> tableDataDetail;

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
        TableView<PurchaseOrderDetailCreated> tableView = new TableView();

        TableColumn<PurchaseOrderDetailCreated, Boolean> createStatus = new TableColumn("Pilih");
        createStatus.setCellValueFactory(cellData -> cellData.getValue().createStatusProperty());
        createStatus.setCellFactory(CheckBoxTableCell.forTableColumn(createStatus));
        createStatus.setMinWidth(50);

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
        itemCost.setMinWidth(110);

        TableColumn<PurchaseOrderDetailCreated, String> itemDiscount = new TableColumn("Diskon (Satuan)");
        itemDiscount.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDataPODetail().getItemDiscount()), param.getValue().dataPODetailProperty()));
        itemDiscount.setMinWidth(110);

        TableColumn<PurchaseOrderDetailCreated, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getDataPODetail().getItemQuantity()), param.getValue().dataPODetailProperty()));
        itemQuantity.setMinWidth(100);

        TableColumn<PurchaseOrderDetailCreated, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDataPODetail().getTblSupplierItem().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().dataPODetailProperty()));
        unitName.setMinWidth(100);

        TableColumn<PurchaseOrderDetailCreated, String> totalCost = new TableColumn("Total Harga");
        totalCost.setCellValueFactory((TableColumn.CellDataFeatures<PurchaseOrderDetailCreated, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalCost(param.getValue().getDataPODetail())), param.getValue().dataPODetailProperty()));
        totalCost.setMinWidth(130);

        tableView.getColumns().addAll(createStatus, itemTitle, itemCost, itemDiscount, itemQuantity, unitName, totalCost);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataPODetailCreated()));
        tableDataDetail = tableView;
    }

    private BigDecimal calculationTotalCost(TblPurchaseOrderDetail dataDetail) {
        return (dataDetail.getItemCost().subtract(dataDetail.getItemDiscount())).multiply(dataDetail.getItemQuantity());
    }

    private List<PurchaseOrderDetailCreated> loadAllDataPODetailCreated() {
        //data po - detail : supplier - item, purchase request
        List<PurchaseOrderDetailCreated> list = generateAllDataPODetailCreatedBySupplierItemAndPRDetail(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblSupplier(),
                parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTblPurchaseRequest());
        //data po - detail
        List<TblPurchaseOrderDetail> poList = new ArrayList<>();
        if (parentController.dataInputDetailStatus == 0) {   //create
            List<TblPurchaseOrderDetail> poListSource = parentController.getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getIdpo());
            for (TblPurchaseOrderDetail poData : poListSource) {
                TblPurchaseOrderDetail data = new TblPurchaseOrderDetail(poData);
                data.setIddetail(0L);
                //set data po
                data.setTblPurchaseOrder(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew());
                //set data supplier item
                data.setTblSupplierItem((parentController.getService().getDataSupplierItem(data.getTblSupplierItem().getIdrelation())));
                //set data item
                data.getTblSupplierItem().setTblItem(parentController.getService().getDataItem(data.getTblSupplierItem().getTblItem().getIditem()));
                //set data unit
                data.getTblSupplierItem().getTblItem().setTblUnit(parentController.getService().getDataUnit(data.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                //set data item type hk
                if (data.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    data.getTblSupplierItem().getTblItem().setTblItemTypeHk(parentController.getService().getDataItemTypeHK(data.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type wh
                if (data.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    data.getTblSupplierItem().getTblItem().setTblItemTypeWh(parentController.getService().getDataItemTypeWH(data.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
                //add to list
                poList.add(data);
            }
        } else {  //update(1)
            poList = parentController.getService().getAllDataPurchaseOrderDetailByIDPurchaseOrder(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getIdpo());
            for (TblPurchaseOrderDetail poData : poList) {
                //set data po
                poData.setTblPurchaseOrder(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew());
                //set data supplier item
                poData.setTblSupplierItem((parentController.getService().getDataSupplierItem(poData.getTblSupplierItem().getIdrelation())));
                //set data item
                poData.getTblSupplierItem().setTblItem(parentController.getService().getDataItem(poData.getTblSupplierItem().getTblItem().getIditem()));
                //set data unit
                poData.getTblSupplierItem().getTblItem().setTblUnit(parentController.getService().getDataUnit(poData.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                //set data item type hk
                if (poData.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    poData.getTblSupplierItem().getTblItem().setTblItemTypeHk(parentController.getService().getDataItemTypeHK(poData.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type wh
                if (poData.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    poData.getTblSupplierItem().getTblItem().setTblItemTypeWh(parentController.getService().getDataItemTypeWH(poData.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                }
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
        return list;
    }

    private void refreshDataDetail(TblSupplier dataSupplier, TblPurchaseRequest dataPR) {
//        //refresh data supplier
//        selectedData.setTblSupplier(dataPR.getTblSupplier());
        //refresh data table detail
        tableDataDetail.setItems(FXCollections.observableArrayList(generateAllDataPODetailCreatedBySupplierItemAndPRDetail(dataSupplier, dataPR)));
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
            List<TblSupplierItem> supplierItemList = parentController.getService().getAllDataSupplierItemByIDSupplier(dataSupplier.getIdsupplier());
            for (TblSupplierItem supplierItedetailmData : supplierItemList) {
                //set data purchase order detail
                TblPurchaseOrderDetail dataDetail = new TblPurchaseOrderDetail();
                //set data purchase order
                dataDetail.setTblPurchaseOrder(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew());
                //set data supplier item
                dataDetail.setTblSupplierItem((parentController.getService().getDataSupplierItem(supplierItedetailmData.getIdrelation())));
                //set data item
                dataDetail.getTblSupplierItem().setTblItem(parentController.getService().getDataItem(dataDetail.getTblSupplierItem().getTblItem().getIditem()));
                //set data unit
                dataDetail.getTblSupplierItem().getTblItem().setTblUnit(parentController.getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                //set data item type hk
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(parentController.getService().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                }
                //set data item type wh
                if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                    dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(parentController.getService().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
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
                List<TblPurchaseRequestDetail> prList = parentController.getService().getAllDataPurchaseRequestDetailByIDPurchaseRequest(dataPR.getIdpr());
                for (int i = list.size() - 1; i > -1; i--) {
                    boolean found = false;
                    for (TblPurchaseRequestDetail prData : prList) {
                        if (list.get(i).getDataPODetail().getTblSupplierItem().getTblItem().getIditem()
                                == prData.getTblItem().getIditem()) {
                            list.get(i).getDataPODetail().setItemQuantity(prData.getItemQuantity());
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
                List<TblPurchaseRequestDetail> prList = parentController.getService().getAllDataPurchaseRequestDetailByIDPurchaseRequest(dataPR.getIdpr());
                for (TblPurchaseRequestDetail prData : prList) {
                    //set data purchase order detail
                    TblPurchaseOrderDetail dataDetail = new TblPurchaseOrderDetail();
                    //set data purchase order
                    dataDetail.setTblPurchaseOrder(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew());
                    //set data supplier item
                    dataDetail.setTblSupplierItem(new TblSupplierItem());
                    //set data item
                    dataDetail.getTblSupplierItem().setTblItem(parentController.getService().getDataItem(prData.getTblItem().getIditem()));
                    //set data unit
                    dataDetail.getTblSupplierItem().getTblItem().setTblUnit(parentController.getService().getDataUnit(dataDetail.getTblSupplierItem().getTblItem().getTblUnit().getIdunit()));
                    //set data item type hk
                    if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                        dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(parentController.getService().getDataItemTypeHK(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                    }
                    //set data item type wh
                    if (dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                        dataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(parentController.getService().getDataItemTypeWH(dataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                    }
                    //set cost & quantity
                    dataDetail.setItemCost(new BigDecimal("0"));
                    dataDetail.setItemQuantity(prData.getItemQuantity());
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

    private BigDecimal calculationSubTotalCreated(List<PurchaseOrderDetailCreated> list) {
        BigDecimal result = new BigDecimal("0");
        for (PurchaseOrderDetailCreated data : list) {
            if (data.getCreateStatus()) {
                result = result.add(calculationTotalCost(data.getDataPODetail()));
            }
        }
        return result;
    }

    private BigDecimal calculationSubTotal(List<TblPurchaseOrderDetail> list) {
        BigDecimal result = new BigDecimal("0");
        for (TblPurchaseOrderDetail data : list) {
            result = result.add(calculationTotalCost(data));
        }
        return result;
    }

    private BigDecimal calculationTaxCreated(List<PurchaseOrderDetailCreated> list) {
        return (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage()).multiply(calculationSubTotalCreated(list));
    }

    private BigDecimal calculationTotalCreated(TblPurchaseOrder dataPO, List<PurchaseOrderDetailCreated> list) {
        return (((new BigDecimal("1")).add(dataPO.getTaxPecentage())).multiply(calculationSubTotalCreated(list).subtract(dataPO.getNominalDiscount()))
                .add(dataPO.getDeliveryCost()));
    }

    private BigDecimal calculationTotal(TblPurchaseOrder dataPO, List<TblPurchaseOrderDetail> list) {
        return (((new BigDecimal("1")).add(dataPO.getTaxPecentage())).multiply(calculationSubTotal(list).subtract(dataPO.getNominalDiscount()))
                .add(dataPO.getDeliveryCost()));
    }

    public void refreshDataBill() {
        if (tableDataDetail != null
                && tableDataDetail != null) {
            txtSubTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationSubTotalCreated((List<PurchaseOrderDetailCreated>) tableDataDetail.getItems())));
            txtTax.setText(ClassFormatter.currencyFormat.cFormat(calculationTaxCreated((List<PurchaseOrderDetailCreated>) tableDataDetail.getItems())));
            lblTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalCreated(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew(),
                    (List<PurchaseOrderDetailCreated>) tableDataDetail.getItems())));
        }
        //refresh data revision
        txtDataRevision.setText(getDataRevisionFrom(parentController.selectedDataRevisionHistory));
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form
        initFormDataApprovalRevision();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public PurchaseOrderApprovalRevisionController(PurchaseOrderController parentController) {
        this.parentController = parentController;
    }

    private final PurchaseOrderController parentController;

    public FPurchaseOrderManager getService() {
        return parentController.getService();
    }

}
