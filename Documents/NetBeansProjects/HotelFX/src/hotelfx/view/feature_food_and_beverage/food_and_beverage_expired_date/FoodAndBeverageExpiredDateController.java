/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_food_and_beverage.food_and_beverage_expired_date;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameItemExpiredDate;
import hotelfx.persistence.service.FFoodAndBeverageManager;
import hotelfx.view.feature_food_and_beverage.FeatureFoodAndBeverageController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
public class FoodAndBeverageExpiredDateController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataFoodAndBeverageExpiredDate;

    private DoubleProperty dataFoodAndBeverageExpiredDateFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataFoodAndBeverageExpiredDateLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataFoodAndBeverageExpiredDateSplitpane() {
        spDataFoodAndBeverageExpiredDate.setDividerPositions(1);
        
        dataFoodAndBeverageExpiredDateFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataFoodAndBeverageExpiredDateFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataFoodAndBeverageExpiredDate.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataFoodAndBeverageExpiredDate.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataFoodAndBeverageExpiredDateFormShowStatus.addListener((obs, oldVal, newVal) -> {
            tableDataFoodAndBeverageExpiredDateLayout.setDisable(false);
            tableDataFoodAndBeverageExpiredDateLayoutDisableLayer.setDisable(true);
            tableDataFoodAndBeverageExpiredDateLayout.toFront();
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataFoodAndBeverageExpiredDateLayout.setDisable(false);
                    tableDataFoodAndBeverageExpiredDateLayoutDisableLayer.setDisable(true);
                    tableDataFoodAndBeverageExpiredDateLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataFoodAndBeverageExpiredDateLayout.setDisable(true);
                    tableDataFoodAndBeverageExpiredDateLayoutDisableLayer.setDisable(false);
                    tableDataFoodAndBeverageExpiredDateLayoutDisableLayer.toFront();
                }
            }
        });

        dataFoodAndBeverageExpiredDateFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataFoodAndBeverageExpiredDateLayout;

    private ClassFilteringTable<TblItemExpiredDate> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;
    
    private ClassTableWithControl tableDataFoodAndBeverageExpiredDate;

    private void initTableDataFoodAndBeverageExpiredDate() {
        //set table
        setTableDataFoodAndBeverageExpiredDate();
        //set control
        setTableControlDataFoodAndBeverageExpiredDate();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataFoodAndBeverageExpiredDate, 15.0);
        AnchorPane.setLeftAnchor(tableDataFoodAndBeverageExpiredDate, 15.0);
        AnchorPane.setRightAnchor(tableDataFoodAndBeverageExpiredDate, 15.0);
        AnchorPane.setTopAnchor(tableDataFoodAndBeverageExpiredDate, 15.0);
        ancBodyLayout.getChildren().add(tableDataFoodAndBeverageExpiredDate);
    }

    private void setTableDataFoodAndBeverageExpiredDate() {
        TableView<TblItemExpiredDate> tableView = new TableView();

        TableColumn<TblItemExpiredDate, String> codeItem = new TableColumn("ID Barang");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
        codeItem.setMinWidth(120);

        TableColumn<TblItemExpiredDate, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblItemExpiredDate, String> unitName = new TableColumn("Satuan");
        unitName.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(), param.getValue().tblItemProperty()));
        unitName.setMinWidth(140);

        TableColumn<TblItemExpiredDate, String> expiredDate = new TableColumn("Tgl. Kadaluarsa");
        expiredDate.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getItemExpiredDate()), param.getValue().itemExpiredDateProperty()));
        expiredDate.setMinWidth(140);

        TableColumn<TblItemExpiredDate, String> totalCurrentStock = new TableColumn("Ada");
        totalCurrentStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(calculationTotalCurrentStock(param.getValue())), param.getValue().iditemExpiredDateProperty()));
        totalCurrentStock.setMinWidth(120);

        TableColumn<TblItemExpiredDate, String> totalSoldStock = new TableColumn("Terjual");
        totalSoldStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(calculationTotalSoldStock(param.getValue())), param.getValue().iditemExpiredDateProperty()));
        totalSoldStock.setMinWidth(120);

        TableColumn<TblItemExpiredDate, String> totalBrokenStock = new TableColumn("Rusak");
        totalBrokenStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(calculationTotalBrokenStock(param.getValue())), param.getValue().iditemExpiredDateProperty()));
        totalBrokenStock.setMinWidth(120);

        TableColumn<TblItemExpiredDate, String> totalReturStock = new TableColumn("Retur");
        totalReturStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(calculationTotalReturStock(param.getValue())), param.getValue().iditemExpiredDateProperty()));
        totalReturStock.setMinWidth(120);

        TableColumn<TblItemExpiredDate, String> totalSOStock = new TableColumn("StockOpname");
        totalSOStock.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(calculationTotalSOStock(param.getValue())), param.getValue().iditemExpiredDateProperty()));
        totalSOStock.setMinWidth(120);
        
        TableColumn<TblItemExpiredDate, String> stockTitle = new TableColumn("Jumlah");
        stockTitle.getColumns().addAll(totalCurrentStock, totalSoldStock, totalBrokenStock, totalReturStock, totalSOStock);
        
        tableView.getColumns().addAll(codeItem, itemName, expiredDate, stockTitle, unitName);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataItemExpiredDate()));

//        tableView.setRowFactory(tv -> {
//            TableRow<TblItemExpiredDate> row = new TableRow<>();
//            row.setOnMouseClicked((e) -> {
//                if (e.getClickCount() == 2) {
//                    if (isShowStatus.get()) {
//                        dataItemExpiredDateUnshowHandle();
//                    } else {
//                        if ((!row.isEmpty())) {
//                            dataItemExpiredDateShowHandle();
//                        }
//                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            dataItemExpiredDateShowHandle();
//                        }
//                    }
//                }
//            });
//            return row;
//        });

        tableDataFoodAndBeverageExpiredDate = new ClassTableWithControl(tableView);
        
        //set filter
        cft = new ClassFilteringTable<>(
                TblItemExpiredDate.class,
                tableDataFoodAndBeverageExpiredDate.getTableView(),
                tableDataFoodAndBeverageExpiredDate.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataFoodAndBeverageExpiredDate() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Stock Opname");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataStockOpnameCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataFoodAndBeverageExpiredDate.addButtonControl(buttonControls);
    }
    
    private List<TblItemExpiredDate> loadAllDataItemExpiredDate() {
        List<TblItemExpiredDate> list = new ArrayList<>();
        //data item expired date (full)
        List<TblItemExpiredDate> tempList = parentController.getFFoodAndBeverageManager().getAllDataItemExpiredDate();
        for (TblItemExpiredDate tempData : tempList) {
            boolean found = false;
            for (TblItemExpiredDate data : list) {
                if (tempData.getTblItem().getIditem() == data.getTblItem().getIditem()
                        && tempData.getItemExpiredDate().equals(data.getItemExpiredDate())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                //data item expired date
                TblItemExpiredDate data = new TblItemExpiredDate(tempData);
//                //data item expired date - status
//                data.setRefItemExpiredDateStatus(parentController.getFFoodAndBeverageManager().getDataItemExpiredDateStatus(data.getRefItemExpiredDateStatus().getIdstatus()));
                //data item (food and beverage)
                data.setTblItem(parentController.getFFoodAndBeverageManager().getFoodAndBeverage(data.getTblItem().getIditem()));
                //data unit
                data.getTblItem().setTblUnit(parentController.getFFoodAndBeverageManager().getUnit(data.getTblItem().getTblUnit().getIdunit()));
                //add data to group list 
                list.add(data);
            }
        }
        return list;
    }

    private double calculationTotalCurrentStock(TblItemExpiredDate itemExpiredDate) {
        double result = 0;
        if (itemExpiredDate != null) {
            List<TblItemExpiredDate> list = parentController.getFFoodAndBeverageManager().getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(
                    itemExpiredDate.getTblItem().getIditem(),
                    itemExpiredDate.getItemExpiredDate(),
                    1); //1 = 'Ada'
            result = list.size();
        }
        return result;
    }

    private double calculationTotalSoldStock(TblItemExpiredDate itemExpiredDate) {
        double result = 0;
        if (itemExpiredDate != null) {
            List<TblItemExpiredDate> list = parentController.getFFoodAndBeverageManager().getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(
                    itemExpiredDate.getTblItem().getIditem(),
                    itemExpiredDate.getItemExpiredDate(),
                    2); //2 = 'Terjual'
            result = list.size();
        }
        return result;
    }

    private double calculationTotalBrokenStock(TblItemExpiredDate itemExpiredDate) {
        double result = 0;
        if (itemExpiredDate != null) {
            List<TblItemExpiredDate> list = parentController.getFFoodAndBeverageManager().getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(
                    itemExpiredDate.getTblItem().getIditem(),
                    itemExpiredDate.getItemExpiredDate(),
                    3); //3 = 'Rusak'
            result = list.size();
        }
        return result;
    }

    private double calculationTotalReturStock(TblItemExpiredDate itemExpiredDate) {
        double result = 0;
        if (itemExpiredDate != null) {
            List<TblItemExpiredDate> list = parentController.getFFoodAndBeverageManager().getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(
                    itemExpiredDate.getTblItem().getIditem(),
                    itemExpiredDate.getItemExpiredDate(),
                    4); //4 = 'Retur'
            result = list.size();
        }
        return result;
    }

    private double calculationTotalSOStock(TblItemExpiredDate itemExpiredDate) {
        double result = 0;
        if (itemExpiredDate != null) {
            List<TblItemExpiredDate> list = parentController.getFFoodAndBeverageManager().getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(
                    itemExpiredDate.getTblItem().getIditem(),
                    itemExpiredDate.getItemExpiredDate(),
                    5); //5 = 'StockOpname'
            result = list.size();
        }
        return result;
    }
    
    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataFoodAndBeverageExpiredDate;

    @FXML
    private ScrollPane spFormDataFoodAndBeverageExpiredDate;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    public TblItemExpiredDate selectedDataItemExpiredDate;

    private void initFormDataFoodAndBeverageExpiredDate() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataFoodAndBeverageExpiredDate.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataFoodAndBeverageExpiredDate.setOnScroll((ScrollEvent scroll) -> {
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
     * TABLE DATA ITEM EXPIRED DATE
     */
    private void setSelectedDataToInputForm(TblItemExpiredDate dataIED) {
        initTableDataDetail(dataIED);

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        gpFormDataFoodAndBeverageExpiredDate.setDisable(dataInputStatus == 3);

//        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    @FXML
    private AnchorPane ancDetailLayout;

    public TableView<TblItemExpiredDate> tableDataDetail;

    private void initTableDataDetail(TblItemExpiredDate dataIED) {
        //set table
        setTableDataDetail(dataIED);
        //set table to content-view
        ancDetailLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataDetail, 0.0);
        AnchorPane.setTopAnchor(tableDataDetail, 0.0);
        ancDetailLayout.getChildren().add(tableDataDetail);
    }

    private void setTableDataDetail(TblItemExpiredDate dataIED) {
        tableDataDetail = new TableView();

//        TableColumn<TblItemExpiredDate, String> codeItemExpiredDate = new TableColumn("ID Barang");
//        codeItemExpiredDate.setCellValueFactory(cellData -> cellData.getValue().codeItemExpiredDateProperty());
//        codeItemExpiredDate.setMinWidth(120);
//
//        TableColumn<TblItemExpiredDate, String> itemExpiredDateStatus = new TableColumn("Status");
//        itemExpiredDateStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblItemExpiredDate, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefItemExpiredDateStatus().getStatusName(), param.getValue().refItemExpiredDateStatusProperty()));
//        itemExpiredDateStatus.setMinWidth(120);
//
//        tableDataDetail.getColumns().addAll(codeItemExpiredDate, itemExpiredDateStatus);
//        tableDataDetail.setItems(FXCollections.observableArrayList(loadAllDataItemExpiredDateDetail(dataIED)));
    }
    
    private List<TblItemExpiredDate> loadAllDataItemExpiredDateDetail(TblItemExpiredDate dataIED) {
        List<TblItemExpiredDate>  list = new ArrayList<>();
        if (dataIED != null
                && dataIED.getTblItem()!= null) {
            list = parentController.getFFoodAndBeverageManager().getAllDataItemExpiredDateByIDItemAndItemExpiredDate(
                    dataIED.getTblItem().getIditem(),
                    dataIED.getItemExpiredDate());
            for (TblItemExpiredDate data : list) {
//                //data item expired date - status
//                data.setRefItemExpiredDateStatus(parentController.getFFoodAndBeverageManager().getDataItemExpiredDateStatus(data.getRefItemExpiredDateStatus().getIdstatus()));
                //data item (food and beverage)
                data.setTblItem(parentController.getFFoodAndBeverageManager().getFoodAndBeverage(data.getTblItem().getIditem()));
                //data unit
                data.getTblItem().setTblUnit(parentController.getFFoodAndBeverageManager().getUnit(data.getTblItem().getTblUnit().getIdunit()));
            }
        }
        return list;
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    private int dataInputStatus = 0;

    public Stage dialogStage;
    
    public TblStockOpnameItemExpiredDate selectedDataSOIED;

    private void dataStockOpnameCreateHandle() {
        if (tableDataFoodAndBeverageExpiredDate.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedDataItemExpiredDate = (TblItemExpiredDate) tableDataFoodAndBeverageExpiredDate.getTableView().getSelectionModel().getSelectedItem();
            selectedDataSOIED = new TblStockOpnameItemExpiredDate();
            selectedDataSOIED.setTblItem(new TblItem(selectedDataItemExpiredDate.getTblItem()));
            selectedDataSOIED.setExpiredDate(selectedDataItemExpiredDate.getItemExpiredDate());
            selectedDataSOIED.setItemQuantitySystem(new BigDecimal(calculationTotalCurrentStock(selectedDataItemExpiredDate)));
            selectedDataSOIED.setItemQuantityReal(new BigDecimal(calculationTotalCurrentStock(selectedDataItemExpiredDate)));
            selectedDataSOIED.setStockOpnameIeddate(Timestamp.valueOf(LocalDateTime.now()));
            //open form data - item expired date - stock opname
            showDataItemExpiredDateStockOpnameDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }
    
    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataItemExpiredDateShowHandle() {
        if (tableDataFoodAndBeverageExpiredDate.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedDataItemExpiredDate = (TblItemExpiredDate) tableDataFoodAndBeverageExpiredDate.getTableView().getSelectionModel().getSelectedItem();
            setSelectedDataToInputForm(selectedDataItemExpiredDate);
            dataFoodAndBeverageExpiredDateFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataItemExpiredDateUnshowHandle() {
        tableDataFoodAndBeverageExpiredDate.getTableView().setItems(FXCollections.observableArrayList(loadAllDataItemExpiredDate()));
        dataFoodAndBeverageExpiredDateFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void showDataItemExpiredDateStockOpnameDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_food_and_beverage/food_and_beverage_expired_date/ItemExpiredDateStockOpnameDialog.fxml"));

            ItemExpiredDateStockOpnameController controller = new ItemExpiredDateStockOpnameController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }
    
    public void refreshData() {
        //data table
        refreshDataTableFoodAndBeverageExpiredDate();
        //data table detail
        setSelectedDataToInputForm(null);
        //close form table detail
        dataFoodAndBeverageExpiredDateFormShowStatus.set(0.0);
        //set show status = false
        isShowStatus.set(false);
    }
    
    public void refreshDataTableFoodAndBeverageExpiredDate(){
        tableDataFoodAndBeverageExpiredDate.getTableView().setItems(FXCollections.observableArrayList(loadAllDataItemExpiredDate()));
        cft.refreshFilterItems(tableDataFoodAndBeverageExpiredDate.getTableView().getItems());
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
        setDataFoodAndBeverageExpiredDateSplitpane();

        //init table
        initTableDataFoodAndBeverageExpiredDate();

        //init form
        initFormDataFoodAndBeverageExpiredDate();

        spDataFoodAndBeverageExpiredDate.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataFoodAndBeverageExpiredDateFormShowStatus.set(0.0);
        });
    }

    public FoodAndBeverageExpiredDateController(FeatureFoodAndBeverageController parentController) {
        this.parentController = parentController;
    }

    private final FeatureFoodAndBeverageController parentController;

    public FFoodAndBeverageManager getService(){
        return parentController.getFFoodAndBeverageManager();
    }
    
}
