/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room.room_type;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblRoomTypeRoomService;
import hotelfx.persistence.service.FRoomManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_room_and_service_md.FeatureRoomAndServiceMDController;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
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
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RoomTypeController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRoomType;

    private DoubleProperty dataRoomTypeFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRoomTypeLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataRoomTypeSplitpane() {
        spDataRoomType.setDividerPositions(1);

        dataRoomTypeFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRoomTypeFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRoomType.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRoomType.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRoomTypeFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataRoomTypeLayout.setDisable(false);
                    tableDataRoomTypeLayoutDisableLayer.setDisable(true);
                    tableDataRoomTypeLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataRoomTypeLayout.setDisable(true);
                    tableDataRoomTypeLayoutDisableLayer.setDisable(false);
                    tableDataRoomTypeLayoutDisableLayer.toFront();
                }
            }
        });

        dataRoomTypeFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataRoomTypeLayout;

    private ClassFilteringTable<TblRoomType> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRoomType;

    private void initTableDataRoomType() {
        //set table
        setTableDataRoomType();
        //set control
        setTableControlDataRoomType();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoomType, 15.0);
        AnchorPane.setLeftAnchor(tableDataRoomType, 15.0);
        AnchorPane.setRightAnchor(tableDataRoomType, 15.0);
        AnchorPane.setTopAnchor(tableDataRoomType, 15.0);
        ancBodyLayout.getChildren().add(tableDataRoomType);
    }

    private void setTableDataRoomType() {
        TableView<TblRoomType> tableView = new TableView();

        TableColumn<TblRoomType, String> roomTypeName = new TableColumn("Tipe Kamar");
        roomTypeName.setCellValueFactory(cellData -> cellData.getValue().roomTypeNameProperty());
        roomTypeName.setMinWidth(140);

        TableColumn<TblRoomType, String> roomTypeNote = new TableColumn("Keterangan");
        roomTypeNote.setCellValueFactory(cellData -> cellData.getValue().roomTypeNoteProperty());
        roomTypeNote.setMinWidth(200);

        TableColumn<TblRoomType, Integer> adultNumber = new TableColumn("Jumlah Dewasa");
        adultNumber.setCellValueFactory(cellData -> cellData.getValue().adultNumberProperty().asObject());
        adultNumber.setMinWidth(140);

        TableColumn<TblRoomType, Integer> childNumber = new TableColumn("Jumlah Anak");
        childNumber.setCellValueFactory(cellData -> cellData.getValue().childNumberProperty().asObject());
        childNumber.setMinWidth(140);

        TableColumn<TblRoomType, String> roomTypePrice = new TableColumn("Harga Tipe Kamar");
        roomTypePrice.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomType, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getRoomTypePrice()), param.getValue().roomTypePriceProperty()));
        roomTypePrice.setMinWidth(180);

        tableView.getColumns().addAll(roomTypeName, adultNumber, childNumber,
                roomTypePrice, roomTypeNote);
        tableView.setItems(loadAllDataRoomType());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoomType> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataRoomTypeUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataRoomTypeUpdateHandle();
                            } else {
                                dataRoomTypeShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataRoomTypeUpdateHandle();
//                            } else {
//                                dataRoomTypeShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataRoomType = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblRoomType.class,
                tableDataRoomType.getTableView(),
                tableDataRoomType.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataRoomType() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRoomTypeCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomTypeUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataRoomTypeDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataRoomTypePrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataRoomType.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomType> loadAllDataRoomType() {
        return FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomType());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataRoomType;

    @FXML
    private ScrollPane spFormDataRoomType;

    @FXML
    private JFXTextField txtRoomTypeName;

    @FXML
    private JFXTextField txtRoomTypeAdultNumber;

    @FXML
    private JFXTextField txtRoomTypeChildNumber;

    @FXML
    private JFXTextField txtRoomTypeCost;

    @FXML
    private JFXTextArea txtRoomTypeNote;

//    @FXML
//    private AnchorPane tableDataRoomTypeFABListLayout;
//
//    @FXML
//    private AnchorPane tableDataRoomTypeAmenityListLayout;
//
//    @FXML
//    private AnchorPane tableDataRoomTypePropertyBarcodeListLayout;
    @FXML
    private AnchorPane tableDataRoomTypeItemListLayout;

    @FXML
    private AnchorPane tableDataRoomTypeRoomServiceListLayout;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblRoomType selectedData;

    private void initFormDataRoomType() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRoomType.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRoomType.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Room Type)"));
        btnSave.setOnAction((e) -> {
            dataRoomTypeSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoomTypeCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtRoomTypeName,
                txtRoomTypeAdultNumber,
                txtRoomTypeChildNumber,
                txtRoomTypeCost);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtRoomTypeCost);
        ClassFormatter.setToNumericField(
                "Integer",
                txtRoomTypeAdultNumber,
                txtRoomTypeChildNumber);
    }

    private void setSelectedDataToInputForm() {

        txtRoomTypeName.textProperty().bindBidirectional(selectedData.roomTypeNameProperty());
        txtRoomTypeNote.textProperty().bindBidirectional(selectedData.roomTypeNoteProperty());
        Bindings.bindBidirectional(txtRoomTypeAdultNumber.textProperty(), selectedData.adultNumberProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtRoomTypeChildNumber.textProperty(), selectedData.childNumberProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtRoomTypeCost.textProperty(), selectedData.roomTypePriceProperty(), new ClassFormatter.CBigDecimalStringConverter());

//        initTableDataRoomTypeFABList();
//        initTableDataRoomTypeAmenityList();
//        initTableDataRoomTypePropertyBarcodeList();
        initTableDataRoomTypeItemList();
        initTableDataRoomTypeRoomServiceList();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataRoomType, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataRoomTypeCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblRoomType();
        selectedData.setRoomTypePrice(new BigDecimal("0"));
        setSelectedDataToInputForm();
        //open form data room type
        dataRoomTypeFormShowStatus.set(0);
        dataRoomTypeFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataRoomTypeUpdateHandle() {
        if (tableDataRoomType.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFRoomManager().getRoomType(((TblRoomType) tableDataRoomType.getTableView().getSelectionModel().getSelectedItem()).getIdroomType());
            setSelectedDataToInputForm();
            //open form data room type
            dataRoomTypeFormShowStatus.set(0);
            dataRoomTypeFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataRoomTypeShowHandle() {
        if (tableDataRoomType.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFRoomManager().getRoomType(((TblRoomType) tableDataRoomType.getTableView().getSelectionModel().getSelectedItem()).getIdroomType());
            setSelectedDataToInputForm();
            dataRoomTypeFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataRoomTypeUnshowHandle() {
        refreshDataTableRoomType();
        dataRoomTypeFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataRoomTypeDeleteHandle() {
        if (tableDataRoomType.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFRoomManager().deleteDataRoomType((TblRoomType) tableDataRoomType.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data room type
                    refreshDataTableRoomType();
                    dataRoomTypeFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataRoomTypePrintHandle() {

    }

    private void dataRoomTypeSaveHandle() {
        if (checkDataInputDataRoomType()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblRoomType dummySelectedData = new TblRoomType(selectedData);
//            List<TblRoomTypeItem> dummyRoomTypeFABList = new ArrayList<>();
//            for (TblRoomTypeItem dataRoomTypeFABList : (List<TblRoomTypeItem>) tableDataRoomTypeFABList.getTableView().getItems()) {
//                TblRoomTypeItem dummyDataRoomTypeFAB = new TblRoomTypeItem(dataRoomTypeFABList);
//                dummyDataRoomTypeFAB.setTblRoomType(dummySelectedData);
//                dummyDataRoomTypeFAB.setTblItem(new TblItem(dummyDataRoomTypeFAB.getTblItem()));
//                dummyRoomTypeFABList.add(dummyDataRoomTypeFAB);
//            }
//            List<TblRoomTypeItem> dummyRoomTypeAmenityList = new ArrayList<>();
//            for (TblRoomTypeItem dataRoomTypeAmenityList : (List<TblRoomTypeItem>) tableDataRoomTypeAmenityList.getTableView().getItems()) {
//                TblRoomTypeItem dummyDataRoomTypeAmenity = new TblRoomTypeItem(dataRoomTypeAmenityList);
//                dummyDataRoomTypeAmenity.setTblRoomType(dummySelectedData);
//                dummyDataRoomTypeAmenity.setTblItem(new TblItem(dummyDataRoomTypeAmenity.getTblItem()));
//                dummyRoomTypeAmenityList.add(dummyDataRoomTypeAmenity);
//            }
//            List<TblRoomTypeItem> dummyRoomTypePropertyBarcodeList = new ArrayList<>();
//            for (TblRoomTypeItem dataRoomTypePropertyBarcodeList : (List<TblRoomTypeItem>) tableDataRoomTypePropertyBarcodeList.getTableView().getItems()) {
//                TblRoomTypeItem dummyDataRoomTypePropertyBarcode = new TblRoomTypeItem(dataRoomTypePropertyBarcodeList);
//                dummyDataRoomTypePropertyBarcode.setTblRoomType(dummySelectedData);
//                dummyDataRoomTypePropertyBarcode.setTblItem(new TblItem(dummyDataRoomTypePropertyBarcode.getTblItem()));
//                dummyRoomTypePropertyBarcodeList.add(dummyDataRoomTypePropertyBarcode);
//            }
                List<TblRoomTypeItem> dummyRoomTypeItemList = new ArrayList<>();
                for (TblRoomTypeItem dataRoomTypeItemList : (List<TblRoomTypeItem>) tableDataRoomTypeItemList.getTableView().getItems()) {
                    TblRoomTypeItem dummyDataRoomTypeItem = new TblRoomTypeItem(dataRoomTypeItemList);
                    dummyDataRoomTypeItem.setTblRoomType(dummySelectedData);
                    dummyDataRoomTypeItem.setTblItem(new TblItem(dummyDataRoomTypeItem.getTblItem()));
                    dummyRoomTypeItemList.add(dummyDataRoomTypeItem);
                }
                List<TblRoomTypeRoomService> dummyRoomTypeRoomServiceList = new ArrayList<>();
                for (TblRoomTypeRoomService dataRoomTypeRoomServiceList : (List<TblRoomTypeRoomService>) tableDataRoomTypeRoomServiceList.getTableView().getItems()) {
                    TblRoomTypeRoomService dummyDataRoomTypeRoomService = new TblRoomTypeRoomService(dataRoomTypeRoomServiceList);
                    dummyDataRoomTypeRoomService.setTblRoomType(dummySelectedData);
                    dummyDataRoomTypeRoomService.setTblRoomService(new TblRoomService(dummyDataRoomTypeRoomService.getTblRoomService()));
                    dummyRoomTypeRoomServiceList.add(dummyDataRoomTypeRoomService);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFRoomManager().insertDataRoomType(dummySelectedData,
                                //                            dummyRoomTypeFABList,
                                //                            dummyRoomTypeAmenityList,
                                //                            dummyRoomTypePropertyBarcodeList,
                                dummyRoomTypeItemList,
                                dummyRoomTypeRoomServiceList) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data room type
                            refreshDataTableRoomType();
                            dataRoomTypeFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFRoomManager().updateDataRoomType(dummySelectedData,
                                //                            dummyRoomTypeFABList,
                                //                            dummyRoomTypeAmenityList,
                                //                            dummyRoomTypePropertyBarcodeList,
                                dummyRoomTypeItemList,
                                dummyRoomTypeRoomServiceList)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data room type
                            refreshDataTableRoomType();
                            dataRoomTypeFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
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

    private void dataRoomTypeCancelHandle() {
        //refresh data from table & close form data room type
        refreshDataTableRoomType();
        dataRoomTypeFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableRoomType() {
        tableDataRoomType.getTableView().setItems(loadAllDataRoomType());
        cft.refreshFilterItems(tableDataRoomType.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataRoomType() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtRoomTypeName.getText() == null || txtRoomTypeName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Tipe Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtRoomTypeAdultNumber.getText() == null
                || txtRoomTypeAdultNumber.getText().equals("")
                || txtRoomTypeAdultNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Dewasa : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getAdultNumber() <= 0) {
                dataInput = false;
                errDataInput += "Jumlah Dewasa : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            }
        }
        if (txtRoomTypeChildNumber.getText() == null
                || txtRoomTypeChildNumber.getText().equals("")
                || txtRoomTypeChildNumber.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Jumlah Anak : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getChildNumber() < 0) {
                dataInput = false;
                errDataInput += "Jumlah Anak : Tidak dapat kurang dari '0' \n";
            }
        }
        if (txtRoomTypeCost.getText() == null
                || txtRoomTypeCost.getText().equals("")
                || txtRoomTypeCost.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Harga Tipe Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (selectedData.getRoomTypePrice().compareTo(new BigDecimal("0")) == -1) {
                dataInput = false;
                errDataInput += "Harga Tipe Kamar : Tidak dapat kurang dari '0' \n";
            }
        }
        return dataInput;
    }

    /**
     * TABLE DATA ROOM TYPE FAB LIST
     */
//    public ClassTableWithControl tableDataRoomTypeFABList;
//
//    private void initTableDataRoomTypeFABList() {
//        //set table
//        setTableDataRoomTypeFABList();
//        //set control
//        setTableControlDataRoomTypeFABList();
//        //set table-control to content-view
//        tableDataRoomTypeFABListLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(tableDataRoomTypeFABList, 0.0);
//        AnchorPane.setLeftAnchor(tableDataRoomTypeFABList, 0.0);
//        AnchorPane.setRightAnchor(tableDataRoomTypeFABList, 0.0);
//        AnchorPane.setTopAnchor(tableDataRoomTypeFABList, 0.0);
//        tableDataRoomTypeFABListLayout.getChildren().add(tableDataRoomTypeFABList);
//    }
//
//    private void setTableDataRoomTypeFABList() {
//        TableView<TblRoomTypeItem> tableView = new TableView();
//        
//        TableColumn<TblRoomTypeItem, String> codeItem = new TableColumn("ID");
//        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
//        codeItem.setMinWidth(120);
//        
//        TableColumn<TblRoomTypeItem, String> itemName = new TableColumn("Nama Barang");
//        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
//        itemName.setMinWidth(140);
//
//        TableColumn<TblRoomTypeItem, String> additionalCharge = new TableColumn("Biaya Tambahan");
//        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getAdditionalCharge()), param.getValue().tblItemProperty()));
//        additionalCharge.setMinWidth(140);
//        
//        TableColumn<TblRoomTypeItem, String> brokenCharge = new TableColumn("Biaya Kerusakan");
//        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getBrokenCharge()), param.getValue().tblItemProperty()));
//        brokenCharge.setMinWidth(140);
//
//        TableColumn<TblRoomTypeItem, String> listNumber = new TableColumn("Jumlah Barang");
//        listNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
//        listNumber.setMinWidth(140);
//
//        tableView.getColumns().addAll(codeItem, itemName, additionalCharge, brokenCharge, listNumber);
//        tableView.setItems(loadAllDataRoomTypeFABList());
//        tableDataRoomTypeFABList = new ClassTableWithControl(tableView);
//    }
//
//    private void setTableControlDataRoomTypeFABList() {
//        //set control from feature
//        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
//        JFXButton buttonControl;
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataRoomTypeFABListCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataRoomTypeFABListUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    //listener delete
//                    dataRoomTypeFABListDeleteHandle();
//                }
//            });
//            buttonControls.add(buttonControl);
//        }
//        tableDataRoomTypeFABList.addButtonControl(buttonControls);
//    }
//
//    private ObservableList<TblRoomTypeItem> loadAllDataRoomTypeFABList() {
//        return FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomTypeFABList(selectedData.getIdroomType()));
//    }
//
//    public TblRoomTypeItem selectedDataRoomTypeFABList;
//
//    /**
//     * HANDLE FROM DATA INPUT
//     */
//    //0 = 'insert'
//    //1 = 'update'
//    public int dataInputRoomTypeFABListStatus = 0;
//
//    public Stage dialogStage;
//
//    public void dataRoomTypeFABListCreateHandle() {
//        dataInputRoomTypeFABListStatus = 0;
//        selectedDataRoomTypeFABList = new TblRoomTypeItem();
//        selectedDataRoomTypeFABList.setTblRoomType(selectedData);
//        //open form data room type fab list
//        showRoomTypeFABListDialog();
//    }
//
//    public void dataRoomTypeFABListUpdateHandle() {
//        if (tableDataRoomTypeFABList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputRoomTypeFABListStatus = 1;
//            selectedDataRoomTypeFABList = (TblRoomTypeItem) tableDataRoomTypeFABList.getTableView().getSelectionModel().getSelectedItem();
////            selectedDataRoomTypeFABList = parentController.getFRoomManager().getRoomTypeFABList(((TblRoomTypeItem) tableDataRoomTypeFABList.getTableView().getSelectionModel().getSelectedItem()).getId());
////            selectedDataRoomTypeFABList.setTblRoomType(selectedData);
////            selectedDataRoomTypeFABList.setTblItem(parentController.getFRoomManager().getItemFoodAndBeverage(selectedDataRoomTypeFABList.getId().getIditem()));
//            //open form data room type fab list
//            showRoomTypeFABListDialog();
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    public void dataRoomTypeFABListDeleteHandle() {
//        if (tableDataRoomTypeFABList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            ClassMessage.showSucceedRemovingDataMessage("", null);
//            //remove data from table items list
//            tableDataRoomTypeFABList.getTableView().getItems().remove(tableDataRoomTypeFABList.getTableView().getSelectionModel().getSelectedItem());
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    private void showRoomTypeFABListDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_room/room_type/RoomTypeFABListDialog.fxml"));
//
//            RoomTypeFABListController controller = new RoomTypeFABListController(this);
//            loader.setController(controller);
//
//            Region page = loader.load();
//
//            // Create the dialog Stage.
//            dialogStage = new Stage();
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(HotelFX.primaryStage);
//
//            //undecorated
//            Undecorator undecorator = new Undecorator(dialogStage, page);
//            undecorator.getStylesheets().add("skin/undecorator.css");
//            undecorator.getMenuButton().setVisible(false);
//            undecorator.getMaximizeButton().setVisible(false);
//            undecorator.getMinimizeButton().setVisible(false);
//            undecorator.getFullScreenButton().setVisible(false);
//            undecorator.getCloseButton().setVisible(false);
//
//            Scene scene = new Scene(undecorator);
//            scene.setFill(Color.TRANSPARENT);
//
//            dialogStage.initStyle(StageStyle.TRANSPARENT);
//            dialogStage.setScene(scene);
//            dialogStage.setResizable(false);
//
//            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
//        } catch (IOException e) {
//            System.out.println("Err >> " + e.toString());
//        }
//    }
    /**
     * TABLE DATA ROOM TYPE AMENITY LIST
     */
//    public ClassTableWithControl tableDataRoomTypeAmenityList;
//
//    private void initTableDataRoomTypeAmenityList() {
//        //set table
//        setTableDataRoomTypeAmenityList();
//        //set control
//        setTableControlDataRoomTypeAmenityList();
//        //set table-control to content-view
//        tableDataRoomTypeAmenityListLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(tableDataRoomTypeAmenityList, 0.0);
//        AnchorPane.setLeftAnchor(tableDataRoomTypeAmenityList, 0.0);
//        AnchorPane.setRightAnchor(tableDataRoomTypeAmenityList, 0.0);
//        AnchorPane.setTopAnchor(tableDataRoomTypeAmenityList, 0.0);
//        tableDataRoomTypeAmenityListLayout.getChildren().add(tableDataRoomTypeAmenityList);
//    }
//
//    private void setTableDataRoomTypeAmenityList() {
//        TableView<TblRoomTypeItem> tableView = new TableView();
//        
//        TableColumn<TblRoomTypeItem, String> codeItem = new TableColumn("ID");
//        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
//        codeItem.setMinWidth(120);
//        
//        TableColumn<TblRoomTypeItem, String> itemName = new TableColumn("Nama Barang");
//        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
//        itemName.setMinWidth(140);
//
//        TableColumn<TblRoomTypeItem, String> additionalCharge = new TableColumn("Biaya Tambahan");
//        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getAdditionalCharge()), param.getValue().tblItemProperty()));
//        additionalCharge.setMinWidth(140);
//        
//        TableColumn<TblRoomTypeItem, String> brokenCharge = new TableColumn("Biaya Kerusakan");
//        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getBrokenCharge()), param.getValue().tblItemProperty()));
//        brokenCharge.setMinWidth(140);
//
//        TableColumn<TblRoomTypeItem, String> listNumber = new TableColumn("Jumlah Barang");
//        listNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
//        listNumber.setMinWidth(140);
//        
//        tableView.getColumns().addAll(codeItem, itemName, additionalCharge, brokenCharge, listNumber);
//        tableView.setItems(loadAllDataRoomTypeAmenityList());
//        tableDataRoomTypeAmenityList = new ClassTableWithControl(tableView);
//    }
//
//    private void setTableControlDataRoomTypeAmenityList() {
//        //set control from feature
//        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
//        JFXButton buttonControl;
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataRoomTypeAmenityListCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataRoomTypeAmenityListUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    //listener delete
//                    dataRoomTypeAmenityListDeleteHandle();
//                }
//            });
//            buttonControls.add(buttonControl);
//        }
//        tableDataRoomTypeAmenityList.addButtonControl(buttonControls);
//    }
//
//    private ObservableList<TblRoomTypeItem> loadAllDataRoomTypeAmenityList() {
//        return FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomTypeAmenityList(selectedData.getIdroomType()));
//    }
//
//    public TblRoomTypeItem selectedDataRoomTypeAmenityList;
//
//    /**
//     * HANDLE FROM DATA INPUT
//     */
//    //0 = 'insert'
//    //1 = 'update'
//    public int dataInputRoomTypeAmenityListStatus = 0;
//
//    public void dataRoomTypeAmenityListCreateHandle() {
//        dataInputRoomTypeAmenityListStatus = 0;
//        selectedDataRoomTypeAmenityList = new TblRoomTypeItem();
//        selectedDataRoomTypeAmenityList.setTblRoomType(selectedData);
//        //open form data room type amenity list
//        showRoomTypeAmenityListDialog();
//    }
//
//    public void dataRoomTypeAmenityListUpdateHandle() {
//        if (tableDataRoomTypeAmenityList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputRoomTypeAmenityListStatus = 1;
//            selectedDataRoomTypeAmenityList = (TblRoomTypeItem) tableDataRoomTypeAmenityList.getTableView().getSelectionModel().getSelectedItem();
////            selectedDataRoomTypeAmenityList = parentController.getFRoomManager().getRoomTypeAmenityList(((TblRoomTypeItem) tableDataRoomTypeAmenityList.getTableView().getSelectionModel().getSelectedItem()).getId());
////            selectedDataRoomTypeAmenityList.setTblRoomType(parentController.getFRoomManager().getRoomType(selectedDataRoomTypeAmenityList.getId().getIdroomType()));
////            selectedDataRoomTypeAmenityList.setTblItem(parentController.getFRoomManager().getItemAmenity(selectedDataRoomTypeAmenityList.getId().getIditem()));
//            //open form data room type amenity list
//            showRoomTypeAmenityListDialog();
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    public void dataRoomTypeAmenityListDeleteHandle() {
//        if (tableDataRoomTypeAmenityList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            ClassMessage.showSucceedRemovingDataMessage("", null);
//            //remove data from table items list
//            tableDataRoomTypeAmenityList.getTableView().getItems().remove(tableDataRoomTypeAmenityList.getTableView().getSelectionModel().getSelectedItem());
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    private void showRoomTypeAmenityListDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_room/room_type/RoomTypeAmenityListDialog.fxml"));
//
//            RoomTypeAmenityListController controller = new RoomTypeAmenityListController(this);
//            loader.setController(controller);
//
//            Region page = loader.load();
//
//            // Create the dialog Stage.
//            dialogStage = new Stage();
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(HotelFX.primaryStage);
//
//            //undecorated
//            Undecorator undecorator = new Undecorator(dialogStage, page);
//            undecorator.getStylesheets().add("skin/undecorator.css");
//            undecorator.getMenuButton().setVisible(false);
//            undecorator.getMaximizeButton().setVisible(false);
//            undecorator.getMinimizeButton().setVisible(false);
//            undecorator.getFullScreenButton().setVisible(false);
//            undecorator.getCloseButton().setVisible(false);
//
//            Scene scene = new Scene(undecorator);
//            scene.setFill(Color.TRANSPARENT);
//
//            dialogStage.initStyle(StageStyle.TRANSPARENT);
//            dialogStage.setScene(scene);
//            dialogStage.setResizable(false);
//
//            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
//        } catch (IOException e) {
//            System.out.println("Err >> " + e.toString());
//        }
//    }
    /**
     * TABLE DATA ROOM TYPE PROPERTY BARCODE LIST
     */
//    public ClassTableWithControl tableDataRoomTypePropertyBarcodeList;
//
//    private void initTableDataRoomTypePropertyBarcodeList() {
//        //set table
//        setTableDataRoomTypePropertyBarcodeList();
//        //set control
//        setTableControlDataRoomTypePropertyBarcodeList();
//        //set table-control to content-view
//        tableDataRoomTypePropertyBarcodeListLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(tableDataRoomTypePropertyBarcodeList, 0.0);
//        AnchorPane.setLeftAnchor(tableDataRoomTypePropertyBarcodeList, 0.0);
//        AnchorPane.setRightAnchor(tableDataRoomTypePropertyBarcodeList, 0.0);
//        AnchorPane.setTopAnchor(tableDataRoomTypePropertyBarcodeList, 0.0);
//        tableDataRoomTypePropertyBarcodeListLayout.getChildren().add(tableDataRoomTypePropertyBarcodeList);
//    }
//
//    private void setTableDataRoomTypePropertyBarcodeList() {
//        TableView<TblRoomTypeItem> tableView = new TableView();
//        
//        TableColumn<TblRoomTypeItem, String> codeItem = new TableColumn("ID");
//        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
//        codeItem.setMinWidth(120);
//        
//        TableColumn<TblRoomTypeItem, String> itemName = new TableColumn("Nama Barang");
//        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
//        itemName.setMinWidth(140);
//
//        TableColumn<TblRoomTypeItem, String> additionalCharge = new TableColumn("Biaya Tambahan");
//        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getAdditionalCharge()), param.getValue().tblItemProperty()));
//        additionalCharge.setMinWidth(140);
//        
//        TableColumn<TblRoomTypeItem, String> brokenCharge = new TableColumn("Biaya Kerusakan");
//        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getBrokenCharge()), param.getValue().tblItemProperty()));
//        brokenCharge.setMinWidth(140);
//
//        TableColumn<TblRoomTypeItem, String> listNumber = new TableColumn("Jumlah Barang");
//        listNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
//                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
//        listNumber.setMinWidth(140);
//
//        tableView.getColumns().addAll(codeItem, itemName, additionalCharge, brokenCharge, listNumber);
//        tableView.setItems(loadAllDataRoomTypePropertyBarcodeList());
//        tableDataRoomTypePropertyBarcodeList = new ClassTableWithControl(tableView);
//    }
//
//    private void setTableControlDataRoomTypePropertyBarcodeList() {
//        //set control from feature
//        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
//        JFXButton buttonControl;
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataRoomTypePropertyBarcodeListCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataRoomTypePropertyBarcodeListUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
//                if (alert.getResult() == ButtonType.OK) {
//                    //listener delete
//                    dataRoomTypePropertyBarcodeListDeleteHandle();
//                }
//            });
//            buttonControls.add(buttonControl);
//        }
//        tableDataRoomTypePropertyBarcodeList.addButtonControl(buttonControls);
//    }
//
//    private ObservableList<TblRoomTypeItem> loadAllDataRoomTypePropertyBarcodeList() {
//        return FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomTypePropertyBarcodeList(selectedData.getIdroomType()));
//    }
//
//    public TblRoomTypeItem selectedDataRoomTypePropertyBarcodeList;
//
//    /**
//     * HANDLE FROM DATA INPUT
//     */
//    //0 = 'insert'
//    //1 = 'update'
//    public int dataInputRoomTypePropertyBarcodeListStatus = 0;
//
//    public void dataRoomTypePropertyBarcodeListCreateHandle() {
//        dataInputRoomTypePropertyBarcodeListStatus = 0;
//        selectedDataRoomTypePropertyBarcodeList = new TblRoomTypeItem();
//        selectedDataRoomTypePropertyBarcodeList.setTblRoomType(selectedData);
//        //open form data room type property barcode list
//        showRoomTypePropertyBarcodeListDialog();
//    }
//
//    public void dataRoomTypePropertyBarcodeListUpdateHandle() {
//        if (tableDataRoomTypePropertyBarcodeList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputRoomTypePropertyBarcodeListStatus = 1;
//            selectedDataRoomTypePropertyBarcodeList = (TblRoomTypeItem) tableDataRoomTypePropertyBarcodeList.getTableView().getSelectionModel().getSelectedItem();
////            selectedDataRoomTypePropertyBarcodeList = parentController.getFRoomManager().getRoomTypePropertyBarcodeList(((TblRoomTypeItem) tableDataRoomTypePropertyBarcodeList.getTableView().getSelectionModel().getSelectedItem()).getId());
////            selectedDataRoomTypePropertyBarcodeList.setTblRoomType(parentController.getFRoomManager().getRoomType(selectedDataRoomTypePropertyBarcodeList.getId().getIdroomType()));
////            selectedDataRoomTypePropertyBarcodeList.setTblItem(parentController.getFRoomManager().getItemPropertyBarcode(selectedDataRoomTypePropertyBarcodeList.getId().getIditem()));
//            //open form data room type property barcode list
//            showRoomTypePropertyBarcodeListDialog();
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    public void dataRoomTypePropertyBarcodeListDeleteHandle() {
//        if (tableDataRoomTypePropertyBarcodeList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            ClassMessage.showSucceedRemovingDataMessage("", null);
//            //remove data from table items list
//            tableDataRoomTypePropertyBarcodeList.getTableView().getItems().remove(tableDataRoomTypePropertyBarcodeList.getTableView().getSelectionModel().getSelectedItem());
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//    
//    private void showRoomTypePropertyBarcodeListDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_room/room_type/RoomTypePropertyBarcodeListDialog.fxml"));
//
//            RoomTypePropertyBarcodeListController controller = new RoomTypePropertyBarcodeListController(this);
//            loader.setController(controller);
//
//            Region page = loader.load();
//
//            // Create the dialog Stage.
//            dialogStage = new Stage();
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(HotelFX.primaryStage);
//
//            //undecorated
//            Undecorator undecorator = new Undecorator(dialogStage, page);
//            undecorator.getStylesheets().add("skin/undecorator.css");
//            undecorator.getMenuButton().setVisible(false);
//            undecorator.getMaximizeButton().setVisible(false);
//            undecorator.getMinimizeButton().setVisible(false);
//            undecorator.getFullScreenButton().setVisible(false);
//            undecorator.getCloseButton().setVisible(false);
//
//            Scene scene = new Scene(undecorator);
//            scene.setFill(Color.TRANSPARENT);
//
//            dialogStage.initStyle(StageStyle.TRANSPARENT);
//            dialogStage.setScene(scene);
//            dialogStage.setResizable(false);
//
//            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
//        } catch (IOException e) {
//            System.out.println("Err >> " + e.toString());
//        }
//    }
    /**
     * TABLE DATA ROOM TYPE - ITEM LIST
     */
    public ClassTableWithControl tableDataRoomTypeItemList;

    private void initTableDataRoomTypeItemList() {
        //set table
        setTableDataRoomTypeItemList();
        //set control
        setTableControlDataRoomTypeItemList();
        //set table-control to content-view
        tableDataRoomTypeItemListLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoomTypeItemList, 0.0);
        AnchorPane.setLeftAnchor(tableDataRoomTypeItemList, 0.0);
        AnchorPane.setRightAnchor(tableDataRoomTypeItemList, 0.0);
        AnchorPane.setTopAnchor(tableDataRoomTypeItemList, 0.0);
        tableDataRoomTypeItemListLayout.getChildren().add(tableDataRoomTypeItemList);
    }

    private void setTableDataRoomTypeItemList() {
        TableView<TblRoomTypeItem> tableView = new TableView();

        TableColumn<TblRoomTypeItem, String> codeItem = new TableColumn("ID");
        codeItem.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getCodeItem(), param.getValue().tblItemProperty()));
        codeItem.setMinWidth(100);

        TableColumn<TblRoomTypeItem, String> itemName = new TableColumn("Nama Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblRoomTypeItem, String> additionalCharge = new TableColumn("Tambahan");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getAdditionalCharge()), param.getValue().tblItemProperty()));
        additionalCharge.setMinWidth(120);

        TableColumn<TblRoomTypeItem, String> brokenCharge = new TableColumn("Kerusakan");
        brokenCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblItem().getBrokenCharge()), param.getValue().tblItemProperty()));
        brokenCharge.setMinWidth(120);

        TableColumn<TblRoomTypeItem, String> chargeTitle = new TableColumn("Biaya");
        chargeTitle.getColumns().addAll(additionalCharge, brokenCharge);

        TableColumn<TblRoomTypeItem, String> listNumber = new TableColumn("Jumlah Barang");
        listNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        listNumber.setMinWidth(120);

        TableColumn<TblRoomTypeItem, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(100);

        TableColumn<TblRoomTypeItem, Boolean> addAsAdditional = new TableColumn(" Tampilkan \n (Reservasi)");
        addAsAdditional.setCellValueFactory(cellData -> cellData.getValue().addAsAdditionalItemProperty());
        addAsAdditional.setCellFactory(CheckBoxTableCell.forTableColumn(addAsAdditional));
        addAsAdditional.setMinWidth(110);

        tableView.getColumns().addAll(codeItem, itemName, chargeTitle, listNumber, addAsAdditional);
        tableView.setItems(loadAllDataRoomTypeItemList());
        tableDataRoomTypeItemList = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataRoomTypeItemList() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRoomTypeItemListCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomTypeItemListUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataRoomTypeItemListDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataRoomTypeItemList.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomTypeItem> loadAllDataRoomTypeItemList() {
        return FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomTypeItemList(selectedData.getIdroomType()));
    }

    public TblRoomTypeItem selectedDataRoomTypeItemList;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputRoomTypeItemListStatus = 0;

    public void dataRoomTypeItemListCreateHandle() {
        dataInputRoomTypeItemListStatus = 0;
        selectedDataRoomTypeItemList = new TblRoomTypeItem();
        selectedDataRoomTypeItemList.setTblRoomType(selectedData);
        selectedDataRoomTypeItemList.setItemQuantity(new BigDecimal("0"));
        selectedDataRoomTypeItemList.setAddAsAdditionalItem(false);
        //open form data room type item list
        showRoomTypeItemListDialog();
    }

    public void dataRoomTypeItemListUpdateHandle() {
        if (tableDataRoomTypeItemList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputRoomTypeItemListStatus = 1;
            selectedDataRoomTypeItemList = new TblRoomTypeItem((TblRoomTypeItem) tableDataRoomTypeItemList.getTableView().getSelectionModel().getSelectedItem());
            selectedDataRoomTypeItemList.setTblItem(new TblItem(selectedDataRoomTypeItemList.getTblItem()));
//            selectedDataRoomTypeItemList = parentController.getFRoomManager().getRoomTypeItemList(((TblRoomTypeItem) tableDataRoomTypeItemList.getTableView().getSelectionModel().getSelectedItem()).getId());
//            selectedDataRoomTypeItemList.setTblRoomType(parentController.getFRoomManager().getRoomType(selectedDataRoomTypeItemList.getId().getIdroomType()));
//            selectedDataRoomTypeItemList.setTblItem(parentController.getFRoomManager().getItemItem(selectedDataRoomTypeItemList.getId().getIditem()));
            //open form data room type item list
            showRoomTypeItemListDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataRoomTypeItemListDeleteHandle() {
        if (tableDataRoomTypeItemList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataRoomTypeItemList.getTableView().getItems().remove(tableDataRoomTypeItemList.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public Stage dialogStage;

    private void showRoomTypeItemListDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room/room_type/RoomTypeItemListDialog.fxml"));

            RoomTypeItemListController controller = new RoomTypeItemListController(this);
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

    /**
     * TABLE DATA ROOM TYPE - ROOM SERVICE LIST
     */
    public ClassTableWithControl tableDataRoomTypeRoomServiceList;

    private void initTableDataRoomTypeRoomServiceList() {
        //set table
        setTableDataRoomTypeRoomServiceList();
        //set control
        setTableControlDataRoomTypeRoomServiceList();
        //set table-control to content-view
        tableDataRoomTypeRoomServiceListLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoomTypeRoomServiceList, 0.0);
        AnchorPane.setLeftAnchor(tableDataRoomTypeRoomServiceList, 0.0);
        AnchorPane.setRightAnchor(tableDataRoomTypeRoomServiceList, 0.0);
        AnchorPane.setTopAnchor(tableDataRoomTypeRoomServiceList, 0.0);
        tableDataRoomTypeRoomServiceListLayout.getChildren().add(tableDataRoomTypeRoomServiceList);
    }

    private void setTableDataRoomTypeRoomServiceList() {
        TableView<TblRoomTypeRoomService> tableView = new TableView();

        TableColumn<TblRoomTypeRoomService, String> codeRoomService = new TableColumn("ID");
        codeRoomService.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeRoomService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomService().getCodeRoomService(), param.getValue().tblRoomServiceProperty()));
        codeRoomService.setMinWidth(100);

        TableColumn<TblRoomTypeRoomService, String> roomServiceName = new TableColumn("Layanan");
        roomServiceName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeRoomService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomService().getServiceName(), param.getValue().tblRoomServiceProperty()));
        roomServiceName.setMinWidth(140);

        TableColumn<TblRoomTypeRoomService, String> roomServicePrice = new TableColumn("Harga");
        roomServicePrice.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeRoomService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getTblRoomService().getPrice()), param.getValue().tblRoomServiceProperty()));
        roomServicePrice.setMinWidth(140);

        TableColumn<TblRoomTypeRoomService, String> peopleNumber = new TableColumn("Jumlah Orang");
        peopleNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblRoomTypeRoomService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getPeopleNumber() != 0
                                ? String.valueOf(param.getValue().getPeopleNumber())
                                : "-",
                        param.getValue().peopleNumberProperty()));
        peopleNumber.setMinWidth(140);

        TableColumn<TblRoomTypeRoomService, Boolean> addAsAdditional = new TableColumn(" Tampilkan \n (Reservasi)");
        addAsAdditional.setCellValueFactory(cellData -> cellData.getValue().addAsAdditionalServiceProperty());
        addAsAdditional.setCellFactory(CheckBoxTableCell.forTableColumn(addAsAdditional));
        addAsAdditional.setMinWidth(110);

        tableView.getColumns().addAll(codeRoomService, roomServiceName, roomServicePrice, peopleNumber, addAsAdditional);
        tableView.setItems(loadAllDataRoomTypeRoomServiceList());
        tableDataRoomTypeRoomServiceList = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataRoomTypeRoomServiceList() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRoomTypeRoomServiceListCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomTypeRoomServiceListUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataRoomTypeRoomServiceListDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataRoomTypeRoomServiceList.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoomTypeRoomService> loadAllDataRoomTypeRoomServiceList() {
        return FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomTypeRoomServiceList(selectedData.getIdroomType()));
    }

    public TblRoomTypeRoomService selectedDataRoomTypeRoomServiceList;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputRoomTypeRoomServiceListStatus = 0;

    public void dataRoomTypeRoomServiceListCreateHandle() {
        dataInputRoomTypeRoomServiceListStatus = 0;
        selectedDataRoomTypeRoomServiceList = new TblRoomTypeRoomService();
        selectedDataRoomTypeRoomServiceList.setTblRoomType(selectedData);
        selectedDataRoomTypeRoomServiceList.setAddAsAdditionalService(false);
        //open form data room type room service list
        showRoomTypeRoomServiceListDialog();
    }

    public void dataRoomTypeRoomServiceListUpdateHandle() {
        if (tableDataRoomTypeRoomServiceList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputRoomTypeRoomServiceListStatus = 1;
            selectedDataRoomTypeRoomServiceList = new TblRoomTypeRoomService((TblRoomTypeRoomService) tableDataRoomTypeRoomServiceList.getTableView().getSelectionModel().getSelectedItem());
            selectedDataRoomTypeRoomServiceList.setTblRoomService(new TblRoomService(selectedDataRoomTypeRoomServiceList.getTblRoomService()));
//            selectedDataRoomTypeRoomServiceList = parentController.getFRoomManager().getRoomTypeRoomServiceList(((TblRoomTypeRoomService) tableDataRoomTypeRoomServiceList.getTableView().getSelectionModel().getSelectedItem()).getId());
//            selectedDataRoomTypeRoomServiceList.setTblRoomType(parentController.getFRoomManager().getRoomType(selectedDataRoomTypeRoomServiceList.getId().getIdroomType()));
//            selectedDataRoomTypeRoomServiceList.setTblRoomService(parentController.getFRoomManager().getRoomService(selectedDataRoomTypeRoomServiceList.getId().getIdroomService()));
            //open form data room type room service list
            showRoomTypeRoomServiceListDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataRoomTypeRoomServiceListDeleteHandle() {
        if (tableDataRoomTypeRoomServiceList.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataRoomTypeRoomServiceList.getTableView().getItems().remove(tableDataRoomTypeRoomServiceList.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showRoomTypeRoomServiceListDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room/room_type/RoomTypeRoomServiceListDialog.fxml"));

            RoomTypeRoomServiceListController controller = new RoomTypeRoomServiceListController(this);
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataRoomTypeSplitpane();

        //init table
        initTableDataRoomType();

        //init form
        initFormDataRoomType();

        spDataRoomType.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRoomTypeFormShowStatus.set(0);
        });
    }

    public RoomTypeController(FeatureRoomAndServiceMDController parentController) {
        this.parentController = parentController;
    }

    private final FeatureRoomAndServiceMDController parentController;

    public FRoomManager getService() {
        return parentController.getFRoomManager();
    }

}
