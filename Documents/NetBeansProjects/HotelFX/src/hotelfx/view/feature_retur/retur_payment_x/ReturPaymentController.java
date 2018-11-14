/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_payment_x;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.service.FReturManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_retur.FeatureReturController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
public class ReturPaymentController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataReturPayment;

    private DoubleProperty dataReturPaymentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReturPaymentLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReturPaymentSplitpane() {
        spDataReturPayment.setDividerPositions(1);

        dataReturPaymentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReturPaymentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataReturPayment.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataReturPayment.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReturPaymentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataReturPaymentLayout.setDisable(false);
            tableDataReturPaymentLayoutDisableLayer.setDisable(true);
            tableDataReturPaymentLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReturPaymentLayout.setDisable(false);
                    tableDataReturPaymentLayoutDisableLayer.setDisable(true);
                    tableDataReturPaymentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReturPaymentLayout.setDisable(true);
                    tableDataReturPaymentLayoutDisableLayer.setDisable(false);
                    tableDataReturPaymentLayoutDisableLayer.toFront();
                }
            }
        });

        dataReturPaymentFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataReturPaymentLayout;

    private ClassTableWithControl tableDataReturPayment;

    private void initTableDataReturPayment() {
        //set table
        setTableDataReturPayment();
        //set control
        setTableControlDataReturPayment();
        //set table-control to content-view
        tableDataReturPaymentLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataReturPayment, 15.0);
        AnchorPane.setLeftAnchor(tableDataReturPayment, 15.0);
        AnchorPane.setRightAnchor(tableDataReturPayment, 15.0);
        AnchorPane.setTopAnchor(tableDataReturPayment, 15.0);
        tableDataReturPaymentLayout.getChildren().add(tableDataReturPayment);
    }

    private void setTableDataReturPayment() {
        TableView<TblRetur> tableView = new TableView();

        TableColumn<TblRetur, String> codeRetur = new TableColumn("No. Retur");
        codeRetur.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeRetur(),
                        param.getValue().codeReturProperty()));
        codeRetur.setMinWidth(120);

        TableColumn<TblRetur, String> supplierName = new TableColumn("Supplier");
        supplierName.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSupplier().getSupplierName(),
                        param.getValue().tblSupplierProperty()));
        supplierName.setMinWidth(140);

        TableColumn<TblRetur, String> totalBill = new TableColumn("Total Tagihan");
        totalBill.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalBill(param.getValue())),
                        param.getValue().idreturProperty()));
        totalBill.setMinWidth(140);

        TableColumn<TblRetur, String> totalPayment = new TableColumn("Total Pembayaran");
        totalPayment.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalPayment(param.getValue())),
                        param.getValue().idreturProperty()));
        totalPayment.setMinWidth(140);

        TableColumn<TblRetur, String> totalRestOfBill = new TableColumn("Sisa Tagihan");
        totalRestOfBill.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationTotalRestOfBill(param.getValue())),
                        param.getValue().idreturProperty()));
        totalRestOfBill.setMinWidth(140);

        TableColumn<TblRetur, String> transactionDate = new TableColumn("Tanggal Transaksi");
        transactionDate.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getReturDate()),
                        param.getValue().returDateProperty()));
        transactionDate.setMinWidth(160);

        TableColumn<TblRetur, String> paymentStatus = new TableColumn("Status");
        paymentStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblHotelReceivable() != null
                                ? param.getValue().getTblHotelReceivable().getRefFinanceTransactionStatus().getStatusName()
                                : "Belum Dibayar", param.getValue().tblHotelReceivableProperty()));
        paymentStatus.setMinWidth(140);

        TableColumn<TblRetur, String> codeInvoice = new TableColumn("No. Invoice");
        codeInvoice.setCellValueFactory((TableColumn.CellDataFeatures<TblRetur, String> param)
                -> Bindings.createStringBinding(() -> getCodeInvoice(param.getValue().getTblHotelReceivable()),
                        param.getValue().tblHotelReceivableProperty()));
        codeInvoice.setMinWidth(120);

        tableView.getColumns().addAll(codeRetur, supplierName, totalBill, totalPayment, totalRestOfBill, transactionDate, paymentStatus, codeInvoice);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataHotelReceivableRetur()));

        tableView.setRowFactory(tv -> {
            TableRow<TblRetur> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataPaymentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            dataPaymentShowHandle();
                        }
                    }
                } else {
                    if (!row.isEmpty()) {
                        if (isShowStatus.get()) {
                            dataPaymentShowHandle();
                        }
                    }
                }
            });
            return row;
        });

        tableDataReturPayment = new ClassTableWithControl(tableView);
    }

    public String getCodeInvoice(TblHotelReceivable dataHotelReceivable) {
        String result = "-";
        if (dataHotelReceivable != null
                && dataHotelReceivable.getTblHotelInvoice() != null) {
            dataHotelReceivable.setTblHotelInvoice(getService().getDataHotelInvoice(dataHotelReceivable.getTblHotelInvoice().getIdhotelInvoice()));
            result = dataHotelReceivable.getTblHotelInvoice().getCodeHotelInvoice();
        }
        return result;
    }

    public BigDecimal calculationTotalBill(TblRetur dataRetur) {
        BigDecimal result = new BigDecimal("0");
        if (dataRetur != null
                && dataRetur.getTblHotelReceivable() != null) {
            result = dataRetur.getTblHotelReceivable().getHotelReceivableNominal();
        }
        return result;
    }

    public BigDecimal calculationTotalPayment(TblRetur dataRetur) {
        BigDecimal result = new BigDecimal("0");
        if (dataRetur != null
                && dataRetur.getTblHotelReceivable() != null) {
            List<TblHotelFinanceTransaction> list = getService().getAllDataHotelFinanceTransactionByIDHotelReceivable(dataRetur.getTblHotelReceivable().getIdhotelReceivable());
            for (TblHotelFinanceTransaction data : list) {
                result = result.add(data.getTransactionNominal());
            }
        }
        return result;
    }

    public BigDecimal calculationTotalRestOfBill(TblRetur dataRetur) {
        return calculationTotalBill(dataRetur).subtract(calculationTotalPayment(dataRetur));
    }

    private void setTableControlDataReturPayment() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Transaksi Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReturPaymentCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataReturPaymentPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataReturPayment.addButtonControl(buttonControls);
    }

    private List<TblRetur> loadAllDataHotelReceivableRetur() {
        List<TblRetur> list = new ArrayList<>();
        //data retur
        List<TblRetur> returList = getService().getAllDataRetur();
        for (int i = returList.size() - 1; i > -1; i--) {
            if (returList.get(i).getRefReturStatus().getIdstatus() == 2 //Dibatalkan = '2'
                    || returList.get(i).getRefReturStatus().getIdstatus() == 3) {    //Sudah Tidak Berlaku = '3'
                returList.remove(i);
            }
        }
        for (TblRetur returData : returList) {
            if (returData.getTblHotelReceivable() != null) {
                //data hotel receivable
                returData.setTblHotelReceivable(getService().getDataHotelReceivable(returData.getTblHotelReceivable().getIdhotelReceivable()));
                //data hotel receivale type
                returData.getTblHotelReceivable().setRefHotelReceivableType(getService().getDataHotelReceivableType(returData.getTblHotelReceivable().getRefHotelReceivableType().getIdtype()));
                //data finance transaction status
                returData.getTblHotelReceivable().setRefFinanceTransactionStatus(getService().getDataFinanceTransactionStatus(returData.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus()));
                //data supplier
                returData.setTblSupplier(getService().getDataSupplier(returData.getTblSupplier().getIdsupplier()));
                //add data to list
                list.add(returData);
            }
        }
        //remove data isn't used
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 3) {   //Sudah Tidak Berlaku = '3'
                list.remove(i);
            }
        }
        return list;
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataReturPayment;

    @FXML
    private ScrollPane spFormDataReturPayment;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblRetur selectedDataRetur;

    public TblHotelFinanceTransaction selectedData;

    private void initFormDataReturPayment() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataReturPayment.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataReturPayment.setOnScroll((ScrollEvent scroll) -> {
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

    }

    /**
     * TABLE DATA RETUR PAYMENT
     */
    private void setSelectedDataToInputForm(TblRetur dataRetur) {
        initTableDataDetail(dataRetur);

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        gpFormDataReturPayment.setDisable(dataInputStatus == 3);

//        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    @FXML
    private AnchorPane ancDetailPaymentLayout;

    public TableView<TblHotelFinanceTransaction> tableDataDetail;

    private void initTableDataDetail(TblRetur dataRetur) {
        //set table
        setTableDataDetail(dataRetur);
        //set table to content-view
        ancDetailPaymentLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailPaymentLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail(TblRetur dataRetur) {
        tableDataDetail = new TableView();

        TableColumn<TblHotelFinanceTransaction, String> codePayment = new TableColumn("No. Pembayaran");
        codePayment.setCellValueFactory(cellData -> cellData.getValue().codeTransactionProperty());
        codePayment.setMinWidth(120);

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
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCreateDate()), param.getValue().createDateProperty()));
        paymentDate.setMinWidth(160);

        tableDataDetail.getColumns().addAll(codePayment, paymentDate, paymentNominal, paymentRoundingValue);
        tableDataDetail.setItems(FXCollections.observableArrayList(loadAllDataHotelFinanceTransaction(dataRetur)));
    }

    private List<TblHotelFinanceTransaction> loadAllDataHotelFinanceTransaction(TblRetur dataRetur) {
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
//        if (dataRetur != null
//                && dataRetur.getTblHotelReceivable() != null) {
//            list = getService().getAllDataHotelFinanceTransactionByIDHotelReceivable(dataRetur.getTblHotelReceivable().getIdhotelReceivable());
//            for (TblHotelFinanceTransaction data : list) {
//                //data hotel receivable
//                data.setTblHotelReceivable(getService().getDataHotelReceivable(data.getTblHotelReceivable().getIdhotelReceivable()));
//                //data finance transaction type
//                data.setRefFinanceTransactionType(getService().getDataFinanceTransactionType(data.getRefFinanceTransactionType().getIdtype()));
//            }
//        }
        return list;
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private int dataInputStatus = 0;

    public Stage dialogStageDetal;

    private void dataReturPaymentCreateHandle() {
//        if (tableDataReturPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            if (((TblRetur) tableDataReturPayment.getTableView().getSelectionModel().getSelectedItem()).getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() != 2) {   //2 = 'Lunas'
//                selectedDataRetur = (TblRetur) tableDataReturPayment.getTableView().getSelectionModel().getSelectedItem();
//                selectedData = new TblHotelFinanceTransaction();
//                selectedData.setTblHotelReceivable(selectedDataRetur.getTblHotelReceivable());
//                selectedData.setTransactionNominal(new BigDecimal("0"));
//                selectedData.setRefFinanceTransactionType(getService().getDataFinanceTransactionType(1));    //receivable = '1'
//                //open form data - detail (retur - payment)
//                showDataDetailDialog();
//            } else {
//                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Data Retur telah dibayar (lunas)..!", null);
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataPaymentShowHandle() {
        if (tableDataReturPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedDataRetur = (TblRetur) tableDataReturPayment.getTableView().getSelectionModel().getSelectedItem();
//            selectedDataHotelReceivableRetur = parentController.getFReturManager().getDataHotelReceivableRetur(((TblHotelReceivableRetur) tableDataReturPayment.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
//            selectedDataHotelReceivableRetur.setTblHotelReceivable(parentController.getFReturManager().getDataHotelReceivable(selectedDataHotelReceivableRetur.getTblHotelReceivable().getIdhotelReceivable()));
//            selectedDataHotelReceivableRetur.setTblRetur(parentController.getFReturManager().getDataRetur(selectedDataHotelReceivableRetur.getTblRetur().getIdpo()));
            setSelectedDataToInputForm(selectedDataRetur);
            dataReturPaymentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataPaymentUnshowHandle() {
        tableDataReturPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelReceivableRetur()));
        dataReturPaymentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataReturPaymentPrintHandle() {
        if (tableDataReturPayment.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            printReturPayment(((TblRetur) tableDataReturPayment.getTableView().getSelectionModel().getSelectedItem()));
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void printReturPayment(TblRetur dataRetur) {
        String hotelLogoName = "";
        SysDataHardCode sdhHotelLogoName = getService().getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
        if (sdhHotelLogoName != null
                && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
        //print data - preview
        ClassPrinter.printKuitansiRetur(hotelLogoName,
                dataRetur);
    }

    private void showDataDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_payment/ReturPaymentDetailDialog.fxml"));

            ReturPaymentDetailController controller = new ReturPaymentDetailController(this);
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

    public void refreshData() {
        //data table
        tableDataReturPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataHotelReceivableRetur()));
        //data table detail
        setSelectedDataToInputForm(null);
        //close form table detail
        dataReturPaymentFormShowStatus.set(0.0);
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
        setDataReturPaymentSplitpane();

        //init table
        initTableDataReturPayment();

        //init form
        initFormDataReturPayment();

        spDataReturPayment.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReturPaymentFormShowStatus.set(0.0);
        });
    }

    public ReturPaymentController(FeatureReturController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReturController parentController;

    public FReturManager getService() {
        return parentController.getFReturManager();
    }

}
