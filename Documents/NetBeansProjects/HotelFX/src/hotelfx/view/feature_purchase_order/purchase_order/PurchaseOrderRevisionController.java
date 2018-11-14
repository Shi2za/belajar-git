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
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblPurchaseOrderRevisionHistory;
import hotelfx.persistence.model.TblPurchaseRequest;
import hotelfx.persistence.model.TblPurchaseRequestDetail;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.model.TblUnit;
import hotelfx.persistence.service.FPurchaseOrderManager;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PurchaseOrderRevisionController implements Initializable {

    @FXML
    private AnchorPane ancFormRevision;

    @FXML
    private GridPane gpFormDataRevision;

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

//    private final JFXCComboBoxPopup<TblPurchaseOrderRevisionHistory> cbpPOSourcePaymentStatus = new JFXCComboBoxPopup<>(TblPurchaseOrderRevisionHistory.class);
//    
//    private final JFXCComboBoxPopup<TblPurchaseOrderRevisionHistory> cbpPOSourceReceivingStatus = new JFXCComboBoxPopup<>(TblPurchaseOrderRevisionHistory.class);
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

    private void initFormDataRevision() {
        initDataPopup();

        spnTaxPercentage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 100.00));
        spnTaxPercentage.setEditable(false);

        btnSave.setTooltip(new Tooltip("Simpan (Data Revisi Purchase Order)"));
        btnSave.setOnAction((e) -> {
            dataRevisionSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRevisionCancelHandle();
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
        ClassViewSetting.setImportantField(
                dtpDueDate,
                txtPOPaymentTypeInformation,
//                txtPONote,
                txtRevisionReason,
                txtRevisionNote,
                txtTax, spnTaxPercentage, lblTaxPercentage,
                txtDiscount,
                txtDeliveryCost);
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
        JFXButton button = new JFXButton(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource().getCodePo());
        button.setOnMouseClicked((e) -> cbp.show());
        button.setPrefSize(140, 25);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-revision-infomation");
        button.setAlignment(Pos.BASELINE_LEFT);

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
            button.getStyleClass().add("button-revision-infomation");
            button.setAlignment(Pos.BASELINE_LEFT);
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
            button.getStyleClass().add("button-revision-infomation");
            button.setAlignment(Pos.BASELINE_LEFT);
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
        dtpDueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setPodueDate(Date.from(newVal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                //refresh data revision
                txtDataRevision.setText(getDataRevisionFrom(parentController.selectedDataRevisionHistory));
            }
        });

        txtPOPaymentTypeInformation.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().popaymentTypeInformationProperty());
        txtPOPaymentTypeInformation.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                //refresh data revision
                txtDataRevision.setText(getDataRevisionFrom(parentController.selectedDataRevisionHistory));
            }
        });

        txtPONote.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().ponoteProperty());
        txtPONote.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                //refresh data revision
                txtDataRevision.setText(getDataRevisionFrom(parentController.selectedDataRevisionHistory));
            }
        });

        txtRevisionReason.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.revisionReasonProperty());
        txtRevisionNote.textProperty().bindBidirectional(parentController.selectedDataRevisionHistory.revisionNoteProperty());

        Bindings.bindBidirectional(txtDiscount.textProperty(), parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().nominalDiscountProperty(), new ClassFormatter.CBigDecimalStringConverter());
        parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().nominalDiscountProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        spnTaxPercentage.getValueFactory().setValue(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage() != null
                ? parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage().doubleValue() * 100
                : 0);
        spnTaxPercentage.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().setTaxPecentage(BigDecimal.valueOf(newVal / 100));
            }
        });
        parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().taxPecentageProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

        Bindings.bindBidirectional(txtDeliveryCost.textProperty(), parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().deliveryCostProperty(), new ClassFormatter.CBigDecimalStringConverter());
        parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().deliveryCostProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshDataBill();
            }
        });

//        txtDataRevision.setText(getDataRevisionFrom(parentController.selectedDataRevisionHistory));
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
                && tableDataDetail.getTableView() != null
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
                for (PurchaseOrderDetailCreated newDetail : (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems()) {
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
            for (PurchaseOrderDetailCreated newDetail : (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems()) {
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
            BigDecimal totalCostPOSource = calculationTotalSource(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource(),
                    sourceDetails);
            BigDecimal totalCostPONew = calculationTotal(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew(),
                    (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems());
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

    private void dataRevisionSaveHandle() {
        if (checkDataInputDataRevision()) {
            if (parentController.checkDataPOEnableToRevisionWithCurrentMaxRevision(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdposource())) {
                Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    List<TblPurchaseOrderDetail> list = new ArrayList<>();
                    for (PurchaseOrderDetailCreated data : (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems()) {
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
                    dummySelectedData.setTblPurchaseOrderByIdponew(new TblPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdponew()));
                    dummySelectedData.getTblPurchaseOrderByIdponew().setTblSupplier(new TblSupplier(dummySelectedData.getTblPurchaseOrderByIdponew().getTblSupplier()));
                    if (dummySelectedData.getTblPurchaseOrderByIdponew().getTblPurchaseRequest() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdponew().setTblPurchaseRequest(new TblPurchaseRequest(dummySelectedData.getTblPurchaseOrderByIdponew().getTblPurchaseRequest()));
                    }
                    if (dummySelectedData.getTblPurchaseOrderByIdponew().getTblRetur() != null) {
                        dummySelectedData.getTblPurchaseOrderByIdponew().setTblRetur(new TblRetur(dummySelectedData.getTblPurchaseOrderByIdponew().getTblRetur()));
                    }
                    List<TblPurchaseOrderDetail> dummyDataPurchaseOrderDetails = new ArrayList<>();
                    for (TblPurchaseOrderDetail dataPurchaseOrderDetail : list) {
                        TblPurchaseOrderDetail dummyDataPurchaseOrderDetail = new TblPurchaseOrderDetail(dataPurchaseOrderDetail);
                        dummyDataPurchaseOrderDetail.setTblPurchaseOrder(dummySelectedData.getTblPurchaseOrderByIdponew());
                        dummyDataPurchaseOrderDetail.setTblSupplierItem(new TblSupplierItem(dummyDataPurchaseOrderDetail.getTblSupplierItem()));
                        dummyDataPurchaseOrderDetails.add(dummyDataPurchaseOrderDetail);
                    }
                    if (parentController.getService().updateDataPurchaseOrderRevision(dummySelectedData,
                            dummyDataPurchaseOrderDetails)) {
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

    private void dataRevisionCancelHandle() {
        //refresh data from table (po) & close form data purchase order revision
        parentController.refreshDataTablePO();
        parentController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataRevision() {
        boolean dataInput = true;
        errDataInput = "";
        if (dtpDueDate.getValue() == null) {
            dataInput = false;
            errDataInput += "Estimasi Tanggal Pengiriman : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtPOPaymentTypeInformation.getText() == null
                || txtPOPaymentTypeInformation.getText().equals("")) {
            dataInput = false;
            errDataInput += "Tipe Pembayaran : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtPONote.getText() == null
//                || txtPONote.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Keterangan PO : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
        if (txtRevisionReason.getText() == null
                || txtRevisionReason.getText().equals("")) {
            dataInput = false;
            errDataInput += "Alasan Revisi : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRevisionNote.getText() == null
                || txtRevisionNote.getText().equals("")) {
            dataInput = false;
            errDataInput += "Resivi (tentang) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtDiscount.getText() == null
                || txtDiscount.getText().equals("")
                || txtDiscount.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal Diskon : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getNominalDiscount().compareTo(new BigDecimal("0")) == -1) {
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
            if (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getDeliveryCost().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Biaya Pengiriman : Nilai tidak dapat kurang dari '0' \n";
            }
        }
        if (calculationTotal(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew(),
                (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems())
                .compareTo(new BigDecimal("0")) == -1) {
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
        tableView.setEditable(true);

        TableColumn<PurchaseOrderDetailCreated, Boolean> createStatus = new TableColumn("Pilih");
        createStatus.setCellValueFactory(cellData -> cellData.getValue().createStatusProperty());
        createStatus.setCellFactory(CheckBoxTableCell.forTableColumn(createStatus));
        createStatus.setMinWidth(50);
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
        tableDataDetail = new ClassTableWithControl(tableView);
    }

    private BigDecimal calculationTotalCost(TblPurchaseOrderDetail dataDetail) {
        return (dataDetail.getItemCost().subtract(dataDetail.getItemDiscount())).multiply(dataDetail.getItemQuantity());
    }

    private void setTableControlDataDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataDetailUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataDetail.addButtonControl(buttonControls);
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

    private BigDecimal calculationSubTotal(List<PurchaseOrderDetailCreated> list) {
        BigDecimal result = new BigDecimal("0");
        for (PurchaseOrderDetailCreated data : list) {
            if (data.getCreateStatus()) {
                result = result.add(calculationTotalCost(data.getDataPODetail()));
            }
        }
        return result;
    }

    private BigDecimal calculationSubTotalSource(List<TblPurchaseOrderDetail> list) {
        BigDecimal result = new BigDecimal("0");
        for (TblPurchaseOrderDetail data : list) {
            result = result.add(calculationTotalCost(data));
        }
        return result;
    }

    private BigDecimal calculationTax(List<PurchaseOrderDetailCreated> list) {
        return (parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew().getTaxPecentage()).multiply(calculationSubTotal(list));
    }

    private BigDecimal calculationTotal(TblPurchaseOrder dataPO, List<PurchaseOrderDetailCreated> list) {
        return (((new BigDecimal("1")).add(dataPO.getTaxPecentage())).multiply(calculationSubTotal(list).subtract(dataPO.getNominalDiscount()))
                .add(dataPO.getDeliveryCost()));
    }

    private BigDecimal calculationTotalSource(TblPurchaseOrder dataPO, List<TblPurchaseOrderDetail> list) {
        return (((new BigDecimal("1")).add(dataPO.getTaxPecentage())).multiply(calculationSubTotalSource(list).subtract(dataPO.getNominalDiscount()))
                .add(dataPO.getDeliveryCost()));
    }

    public void refreshDataBill() {
        if (tableDataDetail != null
                && tableDataDetail.getTableView() != null) {
            txtSubTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationSubTotal((List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems())));
            txtTax.setText(ClassFormatter.currencyFormat.cFormat(calculationTax((List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems())));
            lblTotal.setText(ClassFormatter.currencyFormat.cFormat(calculationTotal(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew(),
                    (List<PurchaseOrderDetailCreated>) tableDataDetail.getTableView().getItems())));
        }
        //refresh data revision
        txtDataRevision.setText(getDataRevisionFrom(parentController.selectedDataRevisionHistory));
    }

    public TblPurchaseOrderDetail selectedDataDetail;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputDetailStatus = 0;

    public Stage dialogStageDetal;

    public void dataDetailUpdateHandle() {
        if (tableDataDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputDetailStatus = 1;
            selectedDataDetail = new TblPurchaseOrderDetail(((PurchaseOrderDetailCreated) tableDataDetail.getTableView().getSelectionModel().getSelectedItem()).getDataPODetail());
            selectedDataDetail.setTblPurchaseOrder(parentController.selectedDataRevisionHistory.getTblPurchaseOrderByIdponew());
            selectedDataDetail.setTblSupplierItem(new TblSupplierItem(selectedDataDetail.getTblSupplierItem()));
            selectedDataDetail.getTblSupplierItem().setTblItem(new TblItem(selectedDataDetail.getTblSupplierItem().getTblItem()));
            selectedDataDetail.getTblSupplierItem().getTblItem().setTblUnit(new TblUnit(selectedDataDetail.getTblSupplierItem().getTblItem().getTblUnit()));
            if (selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk() != null) {
                selectedDataDetail.getTblSupplierItem().getTblItem().setTblItemTypeHk(new TblItemTypeHk(selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeHk()));
            }
            if (selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh() != null) {
                selectedDataDetail.getTblSupplierItem().getTblItem().setTblItemTypeWh(new TblItemTypeWh(selectedDataDetail.getTblSupplierItem().getTblItem().getTblItemTypeWh()));
            }
            //open form data - detail
            showDataDetailDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order/PurchaseOrderDetailRevisionDialog.fxml"));

            PurchaseOrderDetailRevisionController controller = new PurchaseOrderDetailRevisionController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageDetal = new Stage();
            dialogStageDetal.initModality(Modality.WINDOW_MODAL);
            dialogStageDetal.initOwner(parentController.dialogStageDetal);

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
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form
        initFormDataRevision();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public PurchaseOrderRevisionController(PurchaseOrderController parentController) {
        this.parentController = parentController;
    }

    private final PurchaseOrderController parentController;

    public FPurchaseOrderManager getService() {
        return parentController.getService();
    }

}
