/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import com.sun.javafx.css.converters.BooleanConverter;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblSystemRole;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import hotelfx.persistence.service.FBankManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_bank.FeatureBankController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class BankController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataBank;

    private DoubleProperty dataBankFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataBankLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataBankSplitpane() {
        spDataBank.setDividerPositions(1);

        dataBankFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

//        divPosition.bind(new SimpleDoubleProperty(1.0)
//                .subtract((new SimpleDoubleProperty(360.0).divide(spDataBank.widthProperty()))
//                        .multiply(dataBankFormShowStatus)
//                )
//        );
//        divPosition.bind(new SimpleDoubleProperty(1.0)
//                .subtract((formAnchor.prefWidthProperty().divide(spDataBank.widthProperty()))
//                        .multiply(dataBankFormShowStatus)
//                )
//        );
        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataBankFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataBank.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataBank.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataBankFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataBankLayout.setDisable(false);
                    tableDataBankLayoutDisableLayer.setDisable(true);
                    tableDataBankLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataBankLayout.setDisable(true);
                    tableDataBankLayoutDisableLayer.setDisable(false);
                    tableDataBankLayoutDisableLayer.toFront();
                }
            }
        });

        dataBankFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataBankLayout;

    private ClassFilteringTable<TblBank> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataBank;

    private void initTableDataBank() {
        //set table
        setTableDataBank();
        //set control
        setTableControlDataBank();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataBank, 15.0);
        AnchorPane.setLeftAnchor(tableDataBank, 15.0);
        AnchorPane.setRightAnchor(tableDataBank, 15.0);
        AnchorPane.setTopAnchor(tableDataBank, 15.0);
        ancBodyLayout.getChildren().add(tableDataBank);
    }

    private void setTableDataBank() {
        TableView<TblBank> tableView = new TableView();

        TableColumn<TblBank, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
        bankName.setMinWidth(140);

        TableColumn<TblBank, String> bankNote = new TableColumn("Keterangan");
        bankNote.setCellValueFactory(cellData -> cellData.getValue().bankNoteProperty());
        bankNote.setMinWidth(200);

//        TableColumn<TblBank, Boolean> chcBox = new TableColumn("Data");
//        chcBox.setCellValueFactory((TableColumn.CellDataFeatures<TblBank, Boolean> param)
//                -> Bindings.createBooleanBinding(() -> param.getValue().getBankName().equals("BNI"), param.getValue().bankNameProperty()));
//        chcBox.setCellFactory((TableColumn<TblBank, Boolean> param) -> new CheckBoxCell<>());
        tableView.getColumns().addAll(bankName, bankNote);
        tableView.setItems(loadAllDataBank());

        tableView.setRowFactory(tv -> {
            TableRow<TblBank> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataBankUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataBankUpdateHandle();
                            } else {
                                dataBankShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataBankUpdateHandle();
//                            } else {
//                                dataBankShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataBank = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblBank.class,
                tableDataBank.getTableView(),
                tableDataBank.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

//    public class CheckBoxCell<S, T> extends TableCell<S, T> {
//
//        private final CheckBox checkBox;
//        private ObservableValue<T> ov;
//
//        public CheckBoxCell() {
//            this.checkBox = new CheckBox();
//            this.checkBox.setAlignment(Pos.CENTER);
//            setAlignment(Pos.CENTER);
//            setGraphic(checkBox);
//        }
//
//        @Override
//        public void updateItem(T item, boolean empty) {
//            super.updateItem(item, empty);
//            if (this.getTableRow() != null) {
//                Object data = this.getTableRow().getItem();
//                checkBox.setVisible(data != null
//                        ? ((TblBank) data).getBankName().equals("BNI")
//                        : false);
//            }
//            if (empty) {
//                setText(null);
//                setGraphic(null);
//            } else {
//                setGraphic(checkBox);
//                if (ov instanceof BooleanProperty) {
//                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
//                }
//                ov = getTableColumn().getCellObservableValue(getIndex());
//                if (ov instanceof BooleanProperty) {
//                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
//                }
//            }
//        }
//
//        @Override
//        public void startEdit() {
//            super.startEdit();
//            if (isEmpty()) {
//                return;
//            }
//            checkBox.setDisable(false);
//            checkBox.requestFocus();
//        }
//
//        @Override
//        public void cancelEdit() {
//            super.cancelEdit();
//            checkBox.setDisable(true);
//        }
//
//    }
    private void setTableControlDataBank() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataBankCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataBankUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataBankDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataBankPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataBank.addButtonControl(buttonControls);
    }

    private ObservableList<TblBank> loadAllDataBank() {
        return FXCollections.observableArrayList(parentController.getFBankManager().getAllDataBank());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataBank;

    @FXML
    private ScrollPane spFormDataBank;

    @FXML
    private JFXTextField txtBankName;

    @FXML
    private JFXTextArea txtBankNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblBank selectedData;

    private void initFormDataBank() {

        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataBank.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataBank.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Bank)"));
        btnSave.setOnAction((e) -> {
            dataBankSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataBankCancelHandle();
        });

        initImportantFieldColor();

//        //validator
//        RequiredFieldValidator validator =  new RequiredFieldValidator();
//        validator.setErrorStyleClass("validator-error");
//        txtBankName.getValidators().add(validator);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtBankName);
    }

    private void setSelectedDataToInputForm() {
        txtBankName.textProperty().bindBidirectional(selectedData.bankNameProperty());
        txtBankNote.textProperty().bindBidirectional(selectedData.bankNoteProperty());

        initTableDataBankCard();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataBank, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataBankCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblBank();
        setSelectedDataToInputForm();
        //open form data bank
        dataBankFormShowStatus.set(0.0);
        dataBankFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataBankUpdateHandle() {
        if (tableDataBank.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFBankManager().getBank(((TblBank) tableDataBank.getTableView().getSelectionModel().getSelectedItem()).getIdbank());
            setSelectedDataToInputForm();
            //open form data bank
            dataBankFormShowStatus.set(0.0);
            dataBankFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataBankShowHandle() {
        if (tableDataBank.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFBankManager().getBank(((TblBank) tableDataBank.getTableView().getSelectionModel().getSelectedItem()).getIdbank());
            setSelectedDataToInputForm();
            dataBankFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataBankUnshowHandle() {
        refreshDataTableBank();
        dataBankFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataBankDeleteHandle() {
        if (tableDataBank.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFBankManager().deleteDataBank(new TblBank((TblBank) tableDataBank.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data bank
                    refreshDataTableBank();
                    dataBankFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataBankPrintHandle() {

    }

    private void dataBankSaveHandle() {
        if (checkDataInputDataBank()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblBank dummySelectedData = new TblBank(selectedData);
                List<TblBankCard> dummyDataBankCards = new ArrayList<>();
                for (TblBankCard dataBankCard : (List<TblBankCard>) tableDataBankCard.getTableView().getItems()) {
                    TblBankCard dummyDataBankCard = new TblBankCard(dataBankCard);
                    dummyDataBankCard.setTblBank(dummySelectedData);
                    dummyDataBankCard.setRefBankCardType(new RefBankCardType(dummyDataBankCard.getRefBankCardType()));
                    dummyDataBankCards.add(dummyDataBankCard);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFBankManager().insertDataBank(dummySelectedData,
                                dummyDataBankCards) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data bank
                            refreshDataTableBank();
                            dataBankFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFBankManager().updateDataBank(dummySelectedData,
                                dummyDataBankCards)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data bank
                            refreshDataTableBank();
                            dataBankFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
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

    private void dataBankCancelHandle() {
        //refresh data from table & close form data bank
        refreshDataTableBank();
        dataBankFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableBank() {
        tableDataBank.getTableView().setItems(loadAllDataBank());
        cft.refreshFilterItems(tableDataBank.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataBank() {
        boolean dataInput = true;
        errDataInput = "";
//        txtBankName.resetValidation();
        if (txtBankName.getText() == null || txtBankName.getText().equals("")) {
//            txtBankName.getValidators().get(0).setMessage("No Input Given..!!!");
//            txtBankName.getValidators().get(0).setIcon(new ImageView(new Image("file:resources/Icon/error_input.png")));
//            JFXButton button = new JFXButton();
//            button.getStyleClass().add("button-error-info");
//            button.setButtonType(JFXButton.ButtonType.RAISED);
//            button.setPrefSize(25, 25);
//            button.setTooltip(new Tooltip("No Input Given..!!!"));
//            txtBankName.getValidators().get(0).setIcon(button);
//            txtBankName.getValidators().get(0).setVisible(true);
//            txtBankName.validate();
            dataInput = false;
            errDataInput += "Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (txtBankNote.getText() == null || txtBankNote.getText().equals("")) {
//            errDataInput += "Bank Note : No Input Given \n";
//            dataInput = false;
//        }
        return dataInput;
    }

    /**
     * TABLE DATA BANK CARD
     */
    @FXML
    private AnchorPane tableDataBankCardLayout;

    public ClassTableWithControl tableDataBankCard;

    private void initTableDataBankCard() {
        //set table
        setTableDataBankCard();
        //set control
        setTableControlDataBankCard();
        //set table-control to content-view
        tableDataBankCardLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataBankCard, 0.0);
        AnchorPane.setLeftAnchor(tableDataBankCard, 0.0);
        AnchorPane.setRightAnchor(tableDataBankCard, 0.0);
        AnchorPane.setTopAnchor(tableDataBankCard, 0.0);
        tableDataBankCardLayout.getChildren().add(tableDataBankCard);
    }

    private void setTableDataBankCard() {
        TableView<TblBankCard> tableView = new TableView();

        TableColumn<TblBankCard, String> bankCardName = new TableColumn("Kartu Bank");
        bankCardName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getBankCardName(), param.getValue().bankCardNameProperty()));
        bankCardName.setMinWidth(140);

        TableColumn<TblBankCard, String> bankCardClassName = new TableColumn("Tipe Kartu");
        bankCardClassName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getBankCardClassName(), param.getValue().bankCardClassNameProperty()));
        bankCardClassName.setMinWidth(140);

        TableColumn<TblBankCard, String> bankCardTypeName = new TableColumn("Jenis Kartu");
        bankCardTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblBankCard, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefBankCardType().getTypeName(), param.getValue().getRefBankCardType().typeNameProperty()));
        bankCardTypeName.setMinWidth(100);

        tableView.getColumns().addAll(bankCardName, bankCardClassName, bankCardTypeName);
        tableView.setItems(loadAllDataBankCard());
        tableDataBankCard = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataBankCard() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataBankCardCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataBankCardUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataBankCardDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataBankCard.addButtonControl(buttonControls);
    }

    private ObservableList<TblBankCard> loadAllDataBankCard() {
        ObservableList<TblBankCard> list = FXCollections.observableArrayList(parentController.getFBankManager().getAllDataBankCardByIDBank(selectedData.getIdbank()));
        for (TblBankCard data : list) {
            //set data bank
            data.setTblBank(parentController.getFBankManager().getBank(data.getTblBank().getIdbank()));
            //set data bank card type
            data.setRefBankCardType(parentController.getFBankManager().getDataBankCardType(data.getRefBankCardType().getIdtype()));
        }
        return list;
    }

    public TblBankCard selectedDataBankCard;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputBankCardStatus = 0;

    public Stage dialogStage;

    public void dataBankCardCreateHandle() {
        dataInputBankCardStatus = 0;
        selectedDataBankCard = new TblBankCard();
        selectedDataBankCard.setTblBank(selectedData);
        //open form data bank card
        showBankCardDialog();
    }

    public void dataBankCardUpdateHandle() {
        if (tableDataBankCard.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputBankCardStatus = 1;
            selectedDataBankCard = new TblBankCard((TblBankCard) tableDataBankCard.getTableView().getSelectionModel().getSelectedItem());
            //data bank card type
            selectedDataBankCard.setRefBankCardType(new RefBankCardType(selectedDataBankCard.getRefBankCardType()));
            //open form data bank card
            showBankCardDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataBankCardDeleteHandle() {
        if (tableDataBankCard.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataBankCard.getTableView().getItems().remove(tableDataBankCard.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showBankCardDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank/BankCardDialog.fxml"));

            BankCardController controller = new BankCardController(this);
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
        setDataBankSplitpane();

        //init table
        initTableDataBank();

        //init form
        initFormDataBank();

        spDataBank.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataBankFormShowStatus.set(0.0);
        });
    }

    public BankController(FeatureBankController parentController) {
        this.parentController = parentController;
    }

    private final FeatureBankController parentController;

    public FBankManager getService() {
        return parentController.getFBankManager();
    }

}
