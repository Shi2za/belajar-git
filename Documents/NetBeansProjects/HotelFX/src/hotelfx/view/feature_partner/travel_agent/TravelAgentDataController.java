/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.travel_agent;

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
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPartnerBankAccount;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.view.DashboardController;
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
public class TravelAgentDataController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataTravelAgent;

    private DoubleProperty dataTravelAgentFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataTravelAgentLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataTravelAgentSplitpane() {
        spDataTravelAgent.setDividerPositions(1);

        dataTravelAgentFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataTravelAgentFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataTravelAgent.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataTravelAgent.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataTravelAgentFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataTravelAgentLayout.setDisable(false);
                    tableDataTravelAgentLayoutDisableLayer.setDisable(true);
                    tableDataTravelAgentLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataTravelAgentLayout.setDisable(true);
                    tableDataTravelAgentLayoutDisableLayer.setDisable(false);
                    tableDataTravelAgentLayoutDisableLayer.toFront();
                }
            }
        });

        dataTravelAgentFormShowStatus.set(0.0);

    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataTravelAgentLayout;

    private ClassFilteringTable<TblTravelAgent> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataTravelAgent;

    private void initTableDataTravelAgent() {
        //set table
        setTableDataTravelAgent();
        //set control
        setTableControlDataTravelAgent();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataTravelAgent, 15.0);
        AnchorPane.setLeftAnchor(tableDataTravelAgent, 15.0);
        AnchorPane.setRightAnchor(tableDataTravelAgent, 15.0);
        AnchorPane.setTopAnchor(tableDataTravelAgent, 15.0);
        ancBodyLayout.getChildren().add(tableDataTravelAgent);
    }

    private void setTableDataTravelAgent() {
        TableView<TblTravelAgent> tableView = new TableView();

        TableColumn<TblTravelAgent, String> codeTravelAgent = new TableColumn("ID");
        codeTravelAgent.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getCodePartner(), param.getValue().tblPartnerProperty()));
        codeTravelAgent.setMinWidth(100);

        TableColumn<TblTravelAgent, String> travelAgentName = new TableColumn("Nama");
        travelAgentName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPartnerName(), param.getValue().tblPartnerProperty()));
        travelAgentName.setMinWidth(140);

        TableColumn<TblTravelAgent, String> address = new TableColumn("Alamat");
        address.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPartnerAddress(),
                        param.getValue().tblPartnerProperty()));
        address.setMinWidth(180);

        TableColumn<TblTravelAgent, String> telpNumber = new TableColumn("Nomor Telepon");
        telpNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getTelpNumber(), param.getValue().tblPartnerProperty()));
        telpNumber.setMinWidth(120);

        TableColumn<TblTravelAgent, String> email = new TableColumn("Email");
        email.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getEmail(), param.getValue().tblPartnerProperty()));
        email.setMinWidth(200);

        TableColumn<TblTravelAgent, String> website = new TableColumn("Website");
        website.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getWebsite(), param.getValue().tblPartnerProperty()));
        website.setMinWidth(200);

        TableColumn<TblTravelAgent, String> discountPercentage = new TableColumn("Diskon(%)");
        discountPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getRoomTypeDiscountPercentage()) + "%", param.getValue().roomTypeDiscountPercentageProperty()));
        discountPercentage.setMinWidth(100);

        TableColumn<TblTravelAgent, String> picName = new TableColumn("Nama");
        picName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPicname(), param.getValue().tblPartnerProperty()));
        picName.setMinWidth(140);

        TableColumn<TblTravelAgent, String> picPhoneNumber = new TableColumn("Telepon");
        picPhoneNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPicphoneNumber(), param.getValue().tblPartnerProperty()));
        picPhoneNumber.setMinWidth(120);

        TableColumn<TblTravelAgent, String> picEmailAddress = new TableColumn("Email");
        picEmailAddress.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPicemailAddress(), param.getValue().tblPartnerProperty()));
        picEmailAddress.setMinWidth(200);

        TableColumn<TblTravelAgent, String> pciTitle = new TableColumn("PIC");
        pciTitle.getColumns().addAll(picName, picPhoneNumber, picEmailAddress);

        tableView.getColumns().addAll(codeTravelAgent, travelAgentName, pciTitle, address, telpNumber, discountPercentage);

        tableView.setItems(loadAllDataTravelAgent());

        tableView.setRowFactory(tv -> {
            TableRow<TblTravelAgent> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataTravelAgentUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataTravelAgentUpdateHandle();
                            } else {
                                dataTravelAgentShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataTravelAgentUpdateHandle();
//                            } else {
//                                dataTravelAgentShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataTravelAgent = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblTravelAgent.class,
                tableDataTravelAgent.getTableView(),
                tableDataTravelAgent.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataTravelAgent() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataTravelAgentCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataTravelAgentUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataTravelAgentDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataTravelAgentPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataTravelAgent.addButtonControl(buttonControls);
    }

    private ObservableList<TblTravelAgent> loadAllDataTravelAgent() {
        List<TblTravelAgent> list = travelAgentController.getService().getAllDataTravelAgent();
        for (TblTravelAgent data : list) {
            data.setTblPartner(travelAgentController.getService().getPartner(data.getTblPartner().getIdpartner()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataTravelAgent;

    @FXML
    private ScrollPane spFormDataTravelAgent;

    @FXML
    private Label lblTravelAgent;

    @FXML
    private JFXTextField txtCodePartner;

    @FXML
    private JFXTextField txtPartnerName;

    @FXML
    private JFXTextArea txtPartnerAddress;

    @FXML
    private JFXTextField txtTelpNumber;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtWebsite;

    @FXML
    private JFXTextField txtPICName;

    @FXML
    private JFXTextField txtPICPhoneNumber;

    @FXML
    private JFXTextField txtPICEmailAddress;

    @FXML
    private JFXTextField txtDiscountPercentage;

    @FXML
    private JFXTextArea txtNote;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblTravelAgent selectedData;

    private void initFormDataTravelAgent() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataTravelAgent.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataTravelAgent.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Travel Agent)"));
        btnSave.setOnAction((e) -> {
            dataTravelAgentSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataTravelAgentCancelHandle();
        });

        initImportantFieldColor();

        initNumbericField();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtPartnerName,
                txtPartnerAddress,
                txtTelpNumber,
                txtPICName,
                txtPICPhoneNumber,
                txtDiscountPercentage);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtDiscountPercentage);
    }

    private void setSelectedDataToInputForm() {
        //txtCodePartner.textProperty().bindBidirectional(selectedData.getTblPartner().codePartnerProperty());
        txtPartnerName.textProperty().bindBidirectional(selectedData.getTblPartner().partnerNameProperty());
        txtPartnerAddress.textProperty().bindBidirectional(selectedData.getTblPartner().partnerAddressProperty());
        txtTelpNumber.textProperty().bindBidirectional(selectedData.getTblPartner().telpNumberProperty());
        txtEmail.textProperty().bindBidirectional(selectedData.getTblPartner().emailProperty());
        txtWebsite.textProperty().bindBidirectional(selectedData.getTblPartner().websiteProperty());
        txtNote.textProperty().bindBidirectional(selectedData.getTblPartner().partnerNoteProperty());

        txtPICName.textProperty().bindBidirectional(selectedData.getTblPartner().picnameProperty());
        txtPICPhoneNumber.textProperty().bindBidirectional(selectedData.getTblPartner().picphoneNumberProperty());
        txtPICEmailAddress.textProperty().bindBidirectional(selectedData.getTblPartner().picemailAddressProperty());

        Bindings.bindBidirectional(txtDiscountPercentage.textProperty(), selectedData.roomTypeDiscountPercentageProperty(), new ClassFormatter.CBigDecimalStringConverter());

        //data partner - bank account
        initTableDataPartnerBankAccount();

//        //data travel agent - room type
//        initTableDataTravelAgentRoomType();
        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        //txtCodePartner.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataTravelAgent,
                dataInputStatus == 3,
                txtCodePartner);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataTravelAgentCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblTravelAgent();
        selectedData.setTblPartner(new TblPartner());
        selectedData.getTblPartner().setPartnerDeposit(new BigDecimal("0"));
        selectedData.setRoomTypeDiscountPercentage(new BigDecimal("0"));
        lblTravelAgent.setText("");
        setSelectedDataToInputForm();
        //open form data travelAgent
        dataTravelAgentFormShowStatus.set(0.0);
        dataTravelAgentFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataTravelAgentUpdateHandle() {
        if (tableDataTravelAgent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = travelAgentController.getService().getTravelAgent(((TblTravelAgent) tableDataTravelAgent.getTableView().getSelectionModel().getSelectedItem()).getIdtravelAgent());
            selectedData.setTblPartner(travelAgentController.getService().getPartner(selectedData.getTblPartner().getIdpartner()));
            lblTravelAgent.setText(selectedData.getTblPartner().getCodePartner() + "-" + selectedData.getTblPartner().getPartnerName());
            setSelectedDataToInputForm();
            //open form data travelAgent
            dataTravelAgentFormShowStatus.set(0.0);
            dataTravelAgentFormShowStatus.set(1.0);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataTravelAgentShowHandle() {
        if (tableDataTravelAgent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = travelAgentController.getService().getTravelAgent(((TblTravelAgent) tableDataTravelAgent.getTableView().getSelectionModel().getSelectedItem()).getIdtravelAgent());
            selectedData.setTblPartner(travelAgentController.getService().getPartner(selectedData.getTblPartner().getIdpartner()));
            setSelectedDataToInputForm();
            dataTravelAgentFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataTravelAgentUnshowHandle() {
        refreshDataTableTravelAgent();
        dataTravelAgentFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataTravelAgentDeleteHandle() {
        if (tableDataTravelAgent.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (travelAgentController.getService().deleteDataTravelAgent((TblTravelAgent) tableDataTravelAgent.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data travelAgent
                    refreshDataTableTravelAgent();
                    dataTravelAgentFormShowStatus.set(0.0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(travelAgentController.getService().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataTravelAgentPrintHandle() {

    }

    private void dataTravelAgentSaveHandle() {
        if (checkDataInputDataTravelAgent()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblTravelAgent dummySelectedData = new TblTravelAgent(selectedData);
                dummySelectedData.setTblPartner(new TblPartner(dummySelectedData.getTblPartner()));
                List<TblPartnerBankAccount> dummyDataPartnerBankAccounts = new ArrayList<>();
                for (TblPartnerBankAccount dataPartnerBankAccount : (List<TblPartnerBankAccount>) tableDataPartnerBankAccount.getTableView().getItems()) {
                    TblPartnerBankAccount dummyDataPartnerBankAccount = new TblPartnerBankAccount(dataPartnerBankAccount);
                    dummyDataPartnerBankAccount.setTblPartner(dummySelectedData.getTblPartner());
                    dummyDataPartnerBankAccount.setTblBankAccount(new TblBankAccount(dummyDataPartnerBankAccount.getTblBankAccount()));
                    dummyDataPartnerBankAccounts.add(dummyDataPartnerBankAccount);
                }
                switch (dataInputStatus) {
                    case 0:
                        if (travelAgentController.getService().insertDataTravelAgent(dummySelectedData,
                                dummyDataPartnerBankAccounts) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data travelAgent
                            refreshDataTableTravelAgent();
                            dataTravelAgentFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(travelAgentController.getService().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (travelAgentController.getService().updateDataTravelAgent(dummySelectedData,
                                dummyDataPartnerBankAccounts)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data travelAgent
                            refreshDataTableTravelAgent();
                            dataTravelAgentFormShowStatus.set(0.0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(travelAgentController.getService().getErrorMessage(), null);
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

    private void dataTravelAgentCancelHandle() {
        //refresh data from table & close form data travelAgent
        refreshDataTableTravelAgent();
        dataTravelAgentFormShowStatus.set(0.0);
        isShowStatus.setValue(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private void refreshDataTableTravelAgent() {
        tableDataTravelAgent.getTableView().setItems(loadAllDataTravelAgent());
        cft.refreshFilterItems(tableDataTravelAgent.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataTravelAgent() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtPartnerName.getText() == null || txtPartnerName.getText().equals("")) {
            errDataInput += "Travel Agent : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtPartnerAddress.getText() == null || txtPartnerAddress.getText().equals("")) {
            errDataInput += "Alamat : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtTelpNumber.getText() == null || txtTelpNumber.getText().equals("")) {
            errDataInput += "Nomor Telepon : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtPICName.getText() == null || txtPICName.getText().equals("")) {
            errDataInput += "Nama (PIC) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtPICPhoneNumber.getText() == null || txtPICPhoneNumber.getText().equals("")) {
            errDataInput += "Nomor Telepon (PIC) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        }
        if (txtDiscountPercentage.getText() == null
                || txtDiscountPercentage.getText().equals("")
                || txtDiscountPercentage.getText().equals("-")) {
            errDataInput += "Diskon(%) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            dataInput = false;
        } else {
            if (selectedData.getRoomTypeDiscountPercentage()
                    .compareTo(new BigDecimal("0")) < 1) {
                errDataInput += "Diskon(%) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
                dataInput = false;
            }
        }
//        if (!checkDataInputDataTRavelAgentRoomType()) {
//            dataInput = false;
//        }
        return dataInput;
    }

    /**
     * TABLE DATA PARTNER BANK ACCOUNT
     */
    @FXML
    private AnchorPane tableDataPartnerBankAccountLayout;

    public ClassTableWithControl tableDataPartnerBankAccount;

    private void initTableDataPartnerBankAccount() {
        //set table
        setTableDataPartnerBankAccount();
        //set control
        setTableControlDataPartnerBankAccount();
        //set table-control to content-view
        tableDataPartnerBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataPartnerBankAccount, 0.0);
        AnchorPane.setLeftAnchor(tableDataPartnerBankAccount, 0.0);
        AnchorPane.setRightAnchor(tableDataPartnerBankAccount, 0.0);
        AnchorPane.setTopAnchor(tableDataPartnerBankAccount, 0.0);
        tableDataPartnerBankAccountLayout.getChildren().add(tableDataPartnerBankAccount);
    }

    private void setTableDataPartnerBankAccount() {
        TableView<TblPartnerBankAccount> tableView = new TableView();

        TableColumn<TblPartnerBankAccount, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblPartnerBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccount.setMinWidth(140);

        TableColumn<TblPartnerBankAccount, String> bankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderName.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderName.setMinWidth(200);

//        TableColumn<TblPartnerBankAccount, String> bankAccountStatus = new TableColumn("Status");
//        bankAccountStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblPartnerBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefBankAccountHolderStatus().getStatusName(), param.getValue().refBankAccountHolderStatusProperty()));
        tableView.getColumns().addAll(bankName, bankAccount, bankAccountHolderName);
        tableView.setItems(loadAllDataPartnerBankAccount());
        tableDataPartnerBankAccount = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataPartnerBankAccount() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataPartnerBankAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataPartnerBankAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataPartnerBankAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataPartnerBankAccount.addButtonControl(buttonControls);
    }

    private ObservableList<TblPartnerBankAccount> loadAllDataPartnerBankAccount() {
        ObservableList<TblPartnerBankAccount> list = FXCollections.observableArrayList(travelAgentController.getService().getAllDataPartnerBankAccountByIDPartner(selectedData.getTblPartner().getIdpartner()));
        for (TblPartnerBankAccount data : list) {
            //set data partner
            data.setTblPartner(travelAgentController.getService().getPartner(data.getTblPartner().getIdpartner()));
            //set data bank account
            data.setTblBankAccount(travelAgentController.getService().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
            //set data bank
            data.getTblBankAccount().setTblBank(travelAgentController.getService().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
        }
        return list;
    }

    public TblPartnerBankAccount selectedDataPartnerBankAccount;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputPartnerBankAccountStatus = 0;

    public Stage dialogStage;

    public void dataPartnerBankAccountCreateHandle() {
        dataInputPartnerBankAccountStatus = 0;
        selectedDataPartnerBankAccount = new TblPartnerBankAccount();
        selectedDataPartnerBankAccount.setTblPartner(selectedData.getTblPartner());
        selectedDataPartnerBankAccount.setTblBankAccount(new TblBankAccount());
        //open form data partner bank account
        showPartnerBankAccountDialog();
    }

    public void dataPartnerBankAccountUpdateHandle() {
        if (tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputPartnerBankAccountStatus = 1;
            selectedDataPartnerBankAccount = new TblPartnerBankAccount((TblPartnerBankAccount) tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItem());
            //data bank account
            selectedDataPartnerBankAccount.setTblBankAccount(new TblBankAccount(selectedDataPartnerBankAccount.getTblBankAccount()));
            //data bank
            selectedDataPartnerBankAccount.getTblBankAccount().setTblBank(new TblBank(selectedDataPartnerBankAccount.getTblBankAccount().getTblBank()));
            //open form data partner bank account
            showPartnerBankAccountDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataPartnerBankAccountDeleteHandle() {
        if (tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationRemovingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                ClassMessage.showSucceedRemovingDataMessage("", null);
                //remove data from table items list
                tableDataPartnerBankAccount.getTableView().getItems().remove(tableDataPartnerBankAccount.getTableView().getSelectionModel().getSelectedItem());
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showPartnerBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/travel_agent/TravelAgentBankAccountDialog.fxml"));

            TravelAgentBankAccountController controller = new TravelAgentBankAccountController(this);
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

//    private boolean checkDataInputDataTRavelAgentRoomType() {
//        boolean available = true;
//        for (TblTravelAgentRoomType data : (List<TblTravelAgentRoomType>) tableDataTravelAgentRoomType.getTableView().getItems()) {
//            //check data reserved by travel agent
//            if (data.getRoomNumber() < travelAgentController.getService().getMaxNumberRoomTypeNumberHasBeenUsedByTravelAgent(
//                    data.getTblRoomType(),
//                    Date.valueOf(LocalDate.now()),
//                    data.getTblPartner())) {
//                available = false;
//            }
//            //check data reserved by customer
//            if ((getRoomAvailableNumber(data.getTblRoomType())
//                    - data.getRoomNumber())
//                    < travelAgentController.getService().getMaxNumberRoomTypeNumberHasBeenUsedByCustomer(
//                            data.getTblRoomType(),
//                            Date.valueOf(LocalDate.now()))) {
//                available = false;
//            }
//        }
//        return available;
//    }
//
//    private int getRoomAvailableNumber(TblRoomType roomType) {
//        int result = 0;
//        result = travelAgentController.getService().getAllDataRoomByIDRoomType(roomType.getIdroomType()).size();
//        List<TblTravelAgentRoomType> travelAgentRoomTypes = travelAgentController.getService().getAllDataTravelAgentRoomTypeByIDRoomType(roomType.getIdroomType());
//        for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
//            result -= travelAgentRoomType.getRoomNumber();
//        }
//        return result;
//    }
//
//    /**
//     * TABLE DATA TRAVEL AGENT - ROOM TYPE
//     */
//    @FXML
//    private AnchorPane tableDataTravelAgentRoomTypeLayout;
//
//    public ClassTableWithControl tableDataTravelAgentRoomType;
//
//    private void initTableDataTravelAgentRoomType() {
//        //set table
//        setTableDataTravelAgentRoomType();
//        //set control
//        setTableControlDataCustomerBankAccount();
//        //set table-control to content-view
//        tableDataTravelAgentRoomTypeLayout.getChildren().clear();
//        AnchorPane.setBottomAnchor(tableDataTravelAgentRoomType, 0.0);
//        AnchorPane.setLeftAnchor(tableDataTravelAgentRoomType, 0.0);
//        AnchorPane.setRightAnchor(tableDataTravelAgentRoomType, 0.0);
//        AnchorPane.setTopAnchor(tableDataTravelAgentRoomType, 0.0);
//        tableDataTravelAgentRoomTypeLayout.getChildren().add(tableDataTravelAgentRoomType);
//    }
//
//    private void setTableDataTravelAgentRoomType() {
//        TableView<TblTravelAgentRoomType> tableView = new TableView();
//
//        TableColumn<TblTravelAgentRoomType, String> roomTypeName = new TableColumn("Room Type");
//        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgentRoomType, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
//        TableColumn<TblTravelAgentRoomType, String> roomNumber = new TableColumn("Room Number");
//        roomNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgentRoomType, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRoomNumber().toString(), param.getValue().roomNumberProperty()));
//
//        tableView.getColumns().addAll(roomTypeName, roomNumber);
//        tableView.setItems(loadAllDataTravelAgentRoomType());
//        tableDataTravelAgentRoomType = new ClassTableWithControl(tableView);
//    }
//
//    private void setTableControlDataCustomerBankAccount() {
//        //set control from feature
//        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
////        JFXButton buttonControl;
////        if (true) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Tambah");
////            buttonControl.setOnMouseClicked((e) -> {
////                //listener add
////                dataTravelAgentRoomTypeCreateHandle();
////            });
////            buttonControls.add(buttonControl);
////        }
////        if (true) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Ubah");
////            buttonControl.setOnMouseClicked((e) -> {
////                //listener update
////                dataTravelAgentRoomTypeUpdateHandle();
////            });
////            buttonControls.add(buttonControl);
////        }
////        if (true) {
////            buttonControl = new JFXButton();
////            buttonControl.setText("Hapus");
////            buttonControl.setOnMouseClicked((e) -> {
////                Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "Are you sure want to remove this data?", null);
////                if (alert.getResult() == ButtonType.OK) {
////                    //listener delete
////                    dataTravelAgentRoomTypeDeleteHandle();
////                }
////            });
////            buttonControls.add(buttonControl);
////        }
//        tableDataTravelAgentRoomType.addButtonControl(buttonControls);
//    }
//
//    private ObservableList<TblTravelAgentRoomType> loadAllDataTravelAgentRoomType() {
//        ObservableList<TblTravelAgentRoomType> list = FXCollections.observableArrayList(travelAgentController.getService().getAllDataTravelAgentRoomTypeByIDPartner(selectedData.getTblPartner().getIdpartner()));
//        for (TblTravelAgentRoomType data : list) {
//            //set data partner
//            data.setTblPartner(selectedData.getTblPartner());
//            //set data room type
//            data.setTblRoomType(travelAgentController.getService().getDataRoomType(data.getTblRoomType().getIdroomType()));
//        }
//        return list;
//    }
//
//    public TblTravelAgentRoomType selectedDataTravelAgentRoomType;
//
//    /**
//     * HANDLE FROM DATA INPUT
//     */
//    //0 = 'insert'
//    //1 = 'update'
//    public int dataInputTravelAgentRoomTypeStatus = 0;
//
//    public Stage dialogStage;
//
//    public void dataTravelAgentRoomTypeCreateHandle() {
//        dataInputTravelAgentRoomTypeStatus = 0;
//        selectedDataTravelAgentRoomType = new TblTravelAgentRoomType();
//        selectedDataTravelAgentRoomType.setTblPartner(selectedData.getTblPartner());
//        selectedDataTravelAgentRoomType.setTblRoomType(new TblRoomType());
//        //open form data travel agent - room type
//        showTravelAgentRoomTypeDialog();
//    }
//
//    public void dataTravelAgentRoomTypeUpdateHandle() {
//        if (tableDataTravelAgentRoomType.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputTravelAgentRoomTypeStatus = 1;
//            selectedDataTravelAgentRoomType = (TblTravelAgentRoomType) tableDataTravelAgentRoomType.getTableView().getSelectionModel().getSelectedItem();
//            //open form data ctravel agent - room type
//            showTravelAgentRoomTypeDialog();
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
//        }
//    }
//
//    public void dataTravelAgentRoomTypeDeleteHandle() {
//        if (tableDataTravelAgentRoomType.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Removing data successed..!", null);
//            //remove data from table items list
//            tableDataTravelAgentRoomType.getTableView().getItems().remove(tableDataTravelAgentRoomType.getTableView().getSelectionModel().getSelectedItem());
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please selected item..!", null);
//        }
//    }
//
//    private void showTravelAgentRoomTypeDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_partner/travel_agent/TravelAgentRoomTypeInputDialog.fxml"));
//
//            TravelAgentRoomTypeInputController controller = new TravelAgentRoomTypeInputController(this);
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
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set splitpane
        setDataTravelAgentSplitpane();

        //init table
        initTableDataTravelAgent();

        //init form
        initFormDataTravelAgent();

        spDataTravelAgent.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataTravelAgentFormShowStatus.set(0.0);
        });
    }

    public TravelAgentDataController(TravelAgentController travelAgentController) {
        this.travelAgentController = travelAgentController;
    }

    private final TravelAgentController travelAgentController;

    public FPartnerManager getService() {
        return travelAgentController.getService();
    }

}
