/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank_network_card;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_bank.FeatureBankController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
public class BankNetworkCardController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataBankNetworkCard;

    private DoubleProperty dataBankNetworkCardFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataBankNetworkCardLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataBankNetworkCardSplitpane() {
        spDataBankNetworkCard.setDividerPositions(1);

        dataBankNetworkCardFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

//        divPosition.bind(new SimpleDoubleProperty(1.0)
//                .subtract((formAnchor.prefWidthProperty().divide(spDataBankNetworkCard.widthProperty()))
//                        .multiply(dataBankNetworkCardFormShowStatus)
//                )
//        );
//        
        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataBankNetworkCardFormShowStatus)
        );

//        divPosition.addListener((obs, oldVal, newVal) -> {
//            spDataBankNetworkCard.setDividerPositions(newVal.doubleValue());
//        });
        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataBankNetworkCard.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataBankNetworkCard.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataBankNetworkCardFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataBankNetworkCardLayout.setDisable(false);
                    tableDataBankNetworkCardLayoutDisableLayer.setDisable(true);
                    tableDataBankNetworkCardLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataBankNetworkCardLayout.setDisable(true);
                    tableDataBankNetworkCardLayoutDisableLayer.setDisable(false);
                    tableDataBankNetworkCardLayoutDisableLayer.toFront();
                }
            }
        });

        dataBankNetworkCardFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataBankNetworkCardLayout;

    private ClassFilteringTable<TblBankNetworkCard> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataBankNetworkCard;

    private void initTableDataBankNetworkCard() {
        //set table
        setTableDataBankNetworkCard();
        //set control
        setTableControlDataBankNetworkCard();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataBankNetworkCard, 15.0);
        AnchorPane.setLeftAnchor(tableDataBankNetworkCard, 15.0);
        AnchorPane.setRightAnchor(tableDataBankNetworkCard, 15.0);
        AnchorPane.setTopAnchor(tableDataBankNetworkCard, 15.0);
        ancBodyLayout.getChildren().add(tableDataBankNetworkCard);
    }

    private void setTableDataBankNetworkCard() {
        TableView<TblBankNetworkCard> tableView = new TableView();

        TableColumn<TblBankNetworkCard, String> bankNetworkCardName = new TableColumn("Jaringan Kartu");
        bankNetworkCardName.setCellValueFactory(cellData -> cellData.getValue().networkCardNameProperty());
        bankNetworkCardName.setMinWidth(140);

        TableColumn<TblBankNetworkCard, String> bankNetworkCardNote = new TableColumn("Keterangan");
        bankNetworkCardNote.setCellValueFactory(cellData -> cellData.getValue().networkCardNoteProperty());
        bankNetworkCardNote.setMinWidth(200);

        tableView.getColumns().addAll(bankNetworkCardName, bankNetworkCardNote);
        tableView.setItems(loadAllDataBankNetworkCard());

        tableView.setRowFactory(tv -> {
            TableRow<TblBankNetworkCard> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataBankNetworkCardUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataBankNetworkCardUpdateHandle();
                            } else {
                                dataBankNetworkCardShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataBankNetworkCardUpdateHandle();
//                            } else {
//                                dataBankNetworkCardShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataBankNetworkCard = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblBankNetworkCard.class,
                tableDataBankNetworkCard.getTableView(),
                tableDataBankNetworkCard.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataBankNetworkCard() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataBankNetworkCardCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataBankNetworkCardUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataBankNetworkCardDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataBankNetworkCardPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataBankNetworkCard.addButtonControl(buttonControls);
    }

    private ObservableList<TblBankNetworkCard> loadAllDataBankNetworkCard() {
        return FXCollections.observableArrayList(parentController.getFBankManager().getAllDataBankNetworkCard());
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataBankNetworkCard;

    @FXML
    private ScrollPane spFormDataBankNetworkCard;

    @FXML
    private JFXTextField txtBankNetworkCardName;

    @FXML
    private JFXTextArea txtBankNetworkCardNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblBankNetworkCard selectedData;

    private void initFormDataBank() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataBankNetworkCard.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataBankNetworkCard.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Jaringan Kartu Bank)"));
        btnSave.setOnAction((e) -> {
            dataBankNetworkCardSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataBankNetworkCardCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtBankNetworkCardName);
    }

    private void setSelectedDataToInputForm() {
        txtBankNetworkCardName.textProperty().bindBidirectional(selectedData.networkCardNameProperty());
        txtBankNetworkCardNote.textProperty().bindBidirectional(selectedData.networkCardNoteProperty());

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        ClassViewSetting.setDisableForAllInputNode(gpFormDataBankNetworkCard, dataInputStatus == 3);

        btnSave.setVisible(dataInputStatus != 3);
//        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataBankNetworkCardCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblBankNetworkCard();
        setSelectedDataToInputForm();
        //open form data bank network card
        dataBankNetworkCardFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataBankNetworkCardUpdateHandle() {
        if (tableDataBankNetworkCard.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFBankManager().getBankNetworkCard(((TblBankNetworkCard) tableDataBankNetworkCard.getTableView().getSelectionModel().getSelectedItem()).getIdnetworkCard());
            setSelectedDataToInputForm();
            //open form data bank network card
            dataBankNetworkCardFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataBankNetworkCardShowHandle() {
        if (tableDataBankNetworkCard.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFBankManager().getBankNetworkCard(((TblBankNetworkCard) tableDataBankNetworkCard.getTableView().getSelectionModel().getSelectedItem()).getIdnetworkCard());
            setSelectedDataToInputForm();
            dataBankNetworkCardFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataBankNetworkCardUnshowHandle() {
        refreshDataTableBankNetworkCard();
        dataBankNetworkCardFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataBankNetworkCardDeleteHandle() {
        if (tableDataBankNetworkCard.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFBankManager().deleteDataBankNetworkCard(new TblBankNetworkCard((TblBankNetworkCard) tableDataBankNetworkCard.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data bank network card
                    refreshDataTableBankNetworkCard();
                    dataBankNetworkCardFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataBankNetworkCardPrintHandle() {

    }

    private void dataBankNetworkCardSaveHandle() {
        if (checkDataInputDataBankNetworkCard()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblBankNetworkCard dummySelectedData = new TblBankNetworkCard(selectedData);
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFBankManager().insertDataBankNetworkCard(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data bank network card
                            refreshDataTableBankNetworkCard();
                            dataBankNetworkCardFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFBankManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFBankManager().updateDataBankNetworkCard(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data bank network card
                            refreshDataTableBankNetworkCard();
                            dataBankNetworkCardFormShowStatus.set(0.0);
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

    private void dataBankNetworkCardCancelHandle() {
        //refresh data from table & close form data bank network card
        refreshDataTableBankNetworkCard();
        dataBankNetworkCardFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableBankNetworkCard() {
        tableDataBankNetworkCard.getTableView().setItems(loadAllDataBankNetworkCard());
        cft.refreshFilterItems(tableDataBankNetworkCard.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataBankNetworkCard() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtBankNetworkCardName.getText() == null || txtBankNetworkCardName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Jaringan Kartu Bank : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
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
        setDataBankNetworkCardSplitpane();

        //init table
        initTableDataBankNetworkCard();

        //init form
        initFormDataBank();

        spDataBankNetworkCard.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataBankNetworkCardFormShowStatus.set(0.0);
        });
    }

    public BankNetworkCardController(FeatureBankController parentController) {
        this.parentController = parentController;
    }

    private final FeatureBankController parentController;

}
